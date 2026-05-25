package org.reflection;

import javassist.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Handles class bytecode instrumentation using Javassist.
 */
public class BytecodeInstrumenter {

    private final ClassPool classPool;
    private final Set<String> annotationClassNames;
    private final List<Class<?>> instrumentedClasses;

    public BytecodeInstrumenter(String rootDirectory) throws NotFoundException {
        classPool = ClassPool.getDefault();
        classPool.insertClassPath(rootDirectory);
        annotationClassNames = new HashSet<>();
        instrumentedClasses = new ArrayList<>();
    }

    /**
     * Processes the directory: identifies annotations, instruments methods in public classes,
     * and loads them.
     */
    public void processDirectory(File rootDir) throws Exception {
        List<String> classNames = scanClassNames(rootDir, rootDir);

        // First pass: identify annotation types
        for (String className : classNames) {
            try {
                CtClass ctClass = classPool.get(className);
                if (ctClass.isAnnotation()) {
                    annotationClassNames.add(className);
                    System.out.println("[Instrumenter] Identified annotation: " + className);
                }
            } catch (NotFoundException e) {
                System.err.println("Could not load class for inspection: " + className);
            }
        }

        // Create a single classloader to load all modified classes from the directory
        java.net.URLClassLoader directoryLoader;
        try {
            java.net.URI uri = rootDir.toURI();
            String uriStr = uri.toString();
            if (!uriStr.endsWith("/")) {
                uriStr += "/";
            }
            java.net.URL[] urls = { new java.net.URL(uriStr) };
            directoryLoader = new java.net.URLClassLoader(urls, BytecodeInstrumenter.class.getClassLoader());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create classloader for directory", e);
        }

        // Second pass: instrument public classes
        for (String className : classNames) {
            try {
                CtClass ctClass = classPool.get(className);
                
                // Skip if it is an annotation or interface or not public
                if (ctClass.isAnnotation() || ctClass.isInterface() || !Modifier.isPublic(ctClass.getModifiers())) {
                    continue;
                }

                boolean modified = false;

                for (CtMethod method : ctClass.getDeclaredMethods()) {
                    if (hasTargetAnnotation(method)) {
                        System.out.println("[Instrumenter] Instrumenting method: " + method.getName() + " in " + className);
                        // Modify bytecode
                        method.insertBefore("System.out.println(\"[Javassist] Executing target method: " 
                                + method.getName() + "\");");
                        modified = true;
                    }
                }

                // Save the modified bytecode back to the file system to avoid module issues
                if (modified) {
                    ctClass.writeFile(rootDir.getAbsolutePath());
                }

                // Load the class via the shared directory URLClassLoader
                try {
                    Class<?> loadedClass = directoryLoader.loadClass(className);
                    instrumentedClasses.add(loadedClass);
                } catch (Exception e) {
                    System.err.println("Could not load modified class: " + className + " - " + e.getMessage());
                }

            } catch (Exception e) {
                System.err.println("Error processing class " + className + ": " + e.getMessage());
            }
        }
        
        try {
            directoryLoader.close();
        } catch(IOException ignored) {}
    }

    private boolean hasTargetAnnotation(CtMethod method) {
        for (String knownAnno : annotationClassNames) {
            if (method.hasAnnotation(knownAnno)) {
                return true;
            }
        }
        return false;
    }

    private List<String> scanClassNames(File rootDir, File currentDir) {
        List<String> classNames = new ArrayList<>();
        File[] files = currentDir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    classNames.addAll(scanClassNames(rootDir, f));
                } else if (f.getName().endsWith(".class")) {
                    String relativePath = rootDir.toURI().relativize(f.toURI()).getPath();
                    String className = relativePath.replace('/', '.').replace('\\', '.');
                    className = className.substring(0, className.length() - ".class".length());
                    classNames.add(className);
                }
            }
        }
        return classNames;
    }

    public List<Class<?>> getInstrumentedClasses() {
        return instrumentedClasses;
    }
    
    public Set<String> getAnnotationClassNames() {
        return annotationClassNames;
    }
}
