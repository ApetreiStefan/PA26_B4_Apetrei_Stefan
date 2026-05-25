package org;

import org.reflection.ClassInspector;
import org.reflection.ClassMethodInvoker;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.reflection.BytecodeInstrumenter;
import org.reflection.CompilerUtil;

/**
 * Loads unknown Java classes and invokes their parameterless 'run' methods.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("========================================================================");
        System.out.println("                WELCOME TO THE REFLECTION METHOD RUNNER                 ");
        System.out.println("========================================================================");

        try {
            if (args.length > 0) {
                // Command Line Mode
                File inputFile = new File(args[0]);
                if (inputFile.isDirectory()) {
                    processDirectoryMode(inputFile);
                } else {
                    String classFilePath = args[0];
                    String knownPackage = args.length > 1 ? args[1] : null;
                    processClassFile(classFilePath, knownPackage);
                }
            } else {
                // Interactive Mode
                runInteractiveMode();
            }
        } catch (Exception e) {
            System.err.println("\n[FATAL ERROR] An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Processes a directory: compiles .java files, instruments classes, and runs annotated methods.
     */
    private static void processDirectoryMode(File dir) throws Exception {
        System.out.println("\n--- Processing Directory Mode ---");
        System.out.println("Directory: " + dir.getAbsolutePath());
        
        // 1. Compile .java files
        CompilerUtil.compileJavaFiles(dir);

        // 2. Instrument and Load Classes using Javassist
        BytecodeInstrumenter instrumenter = new BytecodeInstrumenter(dir.getAbsolutePath());
        instrumenter.processDirectory(dir);

        List<Class<?>> classes = instrumenter.getInstrumentedClasses();
        Set<String> annotations = instrumenter.getAnnotationClassNames();

        System.out.println("\nSummary: Discovered " + annotations.size() + " annotation(s) and instrumented/loaded " + classes.size() + " public class(es).");

        for (Class<?> clazz : classes) {
            // Display prototype
            ClassInspector.inspect(clazz);

            // Invoke annotated methods
            System.out.println("\nInvoking annotated methods for " + clazz.getName() + "...");
            ClassMethodInvoker.invokeAnnotatedMethods(clazz, annotations);
        }
        System.out.println("--- Directory Processing Complete ---\n");
    }

    /**
     * Runs the interactive CLI menu for scanning and selecting classes.
     */
    private static void runInteractiveMode() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("No command-line arguments provided. Entering Interactive Mode...");
        System.out.println("Scanning classpath for available classes...");
        
        // Try scanning target/classes
        File targetClassesDir = new File("target/classes");
        File srcMainJavaDir = new File("src/main/java");
        
        List<ClassInfo> discoveredClasses = new ArrayList<>();
        
        if (targetClassesDir.exists() && targetClassesDir.isDirectory()) {
            scanDirectory(targetClassesDir, targetClassesDir, discoveredClasses, ".class");
        } else if (srcMainJavaDir.exists() && srcMainJavaDir.isDirectory()) {
            System.out.println("[Info] 'target/classes' not found. Scanning 'src/main/java' instead...");
            scanDirectory(srcMainJavaDir, srcMainJavaDir, discoveredClasses, ".java");
        }
        
        if (discoveredClasses.isEmpty()) {
            System.out.println("\n[Warning] No classes found to scan in 'target/classes' or 'src/main/java'.");
            System.out.println("Please compile the project first using 'mvn compile'.");
        } else {
            System.out.println("\nDiscovered Classes in Classpath:");
            for (int i = 0; i < discoveredClasses.size(); i++) {
                ClassInfo info = discoveredClasses.get(i);
                System.out.printf("  %2d) %-40s (%s)%n", (i + 1), info.fullyQualifiedName, info.file.getName());
            }
        }
        
        while (true) {
            System.out.println("\n========================================================================");
            System.out.println("Options:");
            System.out.println("  - Enter class number (e.g. '1') to run a discovered class");
            System.out.println("  - Enter absolute/relative path to a .class file");
            System.out.println("  - Enter 'q' or 'exit' to quit");
            System.out.print("Your choice > ");
            
            String input = reader.readLine();
            if (input == null) break;
            input = input.trim();
            
            if (input.equalsIgnoreCase("q") || input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting. Thank you for using the Reflection Method Runner!");
                break;
            }
            
            if (input.isEmpty()) {
                continue;
            }
            
            // Check if input is a number
            try {
                int index = Integer.parseInt(input) - 1;
                if (index >= 0 && index < discoveredClasses.size()) {
                    ClassInfo info = discoveredClasses.get(index);
                    System.out.println("\nSelected: " + info.fullyQualifiedName);
                    
                    // If scanning source directories (.java), we'll try to find the compiled .class in target/classes
                    File fileToLoad = info.file;
                    if (info.file.getName().endsWith(".java")) {
                        String relativeClassPath = info.fullyQualifiedName.replace('.', '/') + ".class";
                        File compiledClass = new File(targetClassesDir, relativeClassPath);
                        if (compiledClass.exists()) {
                            fileToLoad = compiledClass;
                        } else {
                            System.out.println("[Warning] The selected class has not been compiled yet.");
                            System.out.println("Please build the project first or provide a valid .class file.");
                            continue;
                        }
                    }
                    
                    processClassFile(fileToLoad.getAbsolutePath(), getPackagePrefix(info.fullyQualifiedName));
                    continue;
                } else {
                    System.out.println("[Error] Choice is out of range.");
                    continue;
                }
            } catch (NumberFormatException e) {
                // Input is not a number, treat it as a file path
                System.out.print("Enter known package name (or press Enter to auto-infer) > ");
                String knownPkg = reader.readLine();
                if (knownPkg != null) {
                    knownPkg = knownPkg.trim();
                    if (knownPkg.isEmpty()) knownPkg = null;
                }
                
                try {
                    processClassFile(input, knownPkg);
                } catch (Exception ex) {
                    System.err.println("[Error] Failed to process file: " + ex.getMessage());
                }
            }
        }
    }

    /**
     * Processes a single class file: resolves its name, loads it, inspects it,
     * and runs its 'run' method if found.
     *
     * @param filePath     Path to the .class file.
     * @param knownPackage The known package name (optional).
     */
    private static void processClassFile(String filePath, String knownPackage) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("Class file does not exist: " + filePath);
        }
        if (!file.isFile() || !file.getName().endsWith(".class")) {
            throw new IllegalArgumentException("Path does not point to a valid .class file: " + filePath);
        }

        System.out.println("\nResolving class details...");
        System.out.println("File path: " + file.getCanonicalPath());

        // 1. Resolve the fully qualified class name and the classpath root
        ResolvedClassDetails details = resolveClassDetails(file, knownPackage);
        System.out.println("Resolved Class Name  : " + details.fullyQualifiedName);
        System.out.println("Resolved Classpath Root: " + details.classpathRoot.getCanonicalPath());

        // 2. Load the class
        Class<?> clazz;
        try {
            // First, try standard Class.forName() if it's already in the classpath
            clazz = Class.forName(details.fullyQualifiedName);
            System.out.println("[Info] Loaded class via standard ClassLoader.");
        } catch (ClassNotFoundException e) {
            // If not found in standard classpath, load it dynamically using URLClassLoader
            System.out.println("[Info] Class not in standard classpath. Loading dynamically...");
            URL[] urls = new URL[]{details.classpathRoot.toURI().toURL()};
            try (URLClassLoader customLoader = new URLClassLoader(urls, Main.class.getClassLoader())) {
                clazz = customLoader.loadClass(details.fullyQualifiedName);
                System.out.println("[Info] Loaded class dynamically using custom URLClassLoader.");
            }
        }

        // 3. Inspect the loaded class
        System.out.println("\nPerforming Reflective Class Inspection...");
        ClassInspector.inspect(clazz);

        // 4. Verify run method and invoke it
        System.out.println("\nChecking for parameterless 'run' method...");
        if (ClassMethodInvoker.hasRunMethod(clazz)) {
            System.out.println("[Success] Parameterless 'run' method found!");
            try {
                ClassMethodInvoker.invokeRunMethod(clazz);
            } catch (Exception e) {
                System.err.println("\n[Error during execution] Invocation failed: " + e.getCause());
                e.printStackTrace();
            }
        } else {
            System.out.println("[Notice] This class does not contain a parameterless 'run' method.");
        }
    }

    /**
     * Scans a directory recursively for files with a specific extension.
     */
    private static void scanDirectory(File rootDir, File currentDir, List<ClassInfo> classes, String extension) {
        File[] files = currentDir.listFiles();
        if (files == null) return;
        
        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(rootDir, file, classes, extension);
            } else if (file.getName().endsWith(extension)) {
                // Build the fully qualified name relative to the root directory
                String relativePath = rootDir.toURI().relativize(file.toURI()).getPath();
                // Remove extension
                relativePath = relativePath.substring(0, relativePath.length() - extension.length());
                // Replace path separators with dots
                String fqName = relativePath.replace('/', '.').replace('\\', '.');
                // Clean trailing dots
                if (fqName.startsWith(".")) fqName = fqName.substring(1);
                
                classes.add(new ClassInfo(file, fqName));
            }
        }
    }

    /**
     * Helper to get package prefix from a fully qualified class name.
     */
    private static String getPackagePrefix(String fqName) {
        int lastDot = fqName.lastIndexOf('.');
        if (lastDot == -1) return null;
        return fqName.substring(0, lastDot);
    }

    /**
     * Resolves the fully qualified name and the classpath root for a .class file.
     */
    private static ResolvedClassDetails resolveClassDetails(File file, String knownPackage) throws Exception {
        String canonicalPath = file.getCanonicalPath().replace('\\', '/');
        String fileName = file.getName();
        String simpleClassName = fileName.substring(0, fileName.lastIndexOf('.'));

        // 1. Try resolving using standard build output directories
        String[] classpathMarkers = {
                "/target/classes/", 
                "/target/test-classes/", 
                "/classes/", 
                "/bin/", 
                "/out/production/",
                "/out/test/"
        };

        for (String marker : classpathMarkers) {
            int markerIndex = canonicalPath.toLowerCase().indexOf(marker);
            if (markerIndex != -1) {
                String remainder = canonicalPath.substring(markerIndex + marker.length());
                String relativeClassPath = remainder.substring(0, remainder.lastIndexOf('.'));
                String fqName = relativeClassPath.replace('/', '.');
                
                File cpRoot = new File(file.getCanonicalPath().substring(0, markerIndex + marker.length()));
                return new ResolvedClassDetails(fqName, cpRoot);
            }
        }

        // 2. If a known package is supplied, use it to split the path
        if (knownPackage != null && !knownPackage.isEmpty()) {
            String pkgPath = knownPackage.replace('.', '/');
            int pkgIndex = canonicalPath.indexOf(pkgPath);
            if (pkgIndex != -1) {
                String remainder = canonicalPath.substring(pkgIndex);
                String relativeClassPath = remainder.substring(0, remainder.lastIndexOf('.'));
                String fqName = relativeClassPath.replace('/', '.');
                
                File cpRoot = new File(file.getCanonicalPath().substring(0, pkgIndex));
                return new ResolvedClassDetails(fqName, cpRoot);
            } else {
                // If package path is not matched inside file path, construct fully qualified name
                String fqName = knownPackage + "." + simpleClassName;
                // Classpath root is simply the parent directory of the file since it has no relative folders matched
                return new ResolvedClassDetails(fqName, file.getParentFile());
            }
        }

        // 3. Fallback: Assume package is 'org' or check for 'org/' in path
        int orgIndex = canonicalPath.indexOf("/org/");
        if (orgIndex == -1) {
            orgIndex = canonicalPath.indexOf("/org\\");
        }
        if (orgIndex != -1) {
            String remainder = canonicalPath.substring(orgIndex + 1);
            String relativeClassPath = remainder.substring(0, remainder.lastIndexOf('.'));
            String fqName = relativeClassPath.replace('/', '.');
            
            File cpRoot = new File(file.getCanonicalPath().substring(0, orgIndex + 1));
            return new ResolvedClassDetails(fqName, cpRoot);
        }

        // 4. Ultimate fallback: assume default package (no package)
        System.out.println("[Warning] Could not detect package structure from path. Assuming default package.");
        return new ResolvedClassDetails(simpleClassName, file.getParentFile());
    }

    private static class ClassInfo {
        final File file;
        final String fullyQualifiedName;

        ClassInfo(File file, String fullyQualifiedName) {
            this.file = file;
            this.fullyQualifiedName = fullyQualifiedName;
        }
    }

    private static class ResolvedClassDetails {
        final String fullyQualifiedName;
        final File classpathRoot;

        ResolvedClassDetails(String fullyQualifiedName, File classpathRoot) {
            this.fullyQualifiedName = fullyQualifiedName;
            this.classpathRoot = classpathRoot;
        }
    }
}
