package org.reflection;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Utility for dynamically compiling Java source files.
 */
public class CompilerUtil {

    /**
     * Compiles all .java files found in the given directory or file.
     *
     * @param directory The directory containing .java files, or a single .java file.
     * @return true if compilation is successful or no files needed compiling.
     */
    public static boolean compileJavaFiles(File directory) {
        if (!directory.exists()) {
            System.err.println("Directory does not exist: " + directory.getAbsolutePath());
            return false;
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            System.err.println("JavaCompiler is not available. Please make sure you are running with a JDK, not a JRE.");
            return false;
        }

        try (Stream<Path> paths = Files.walk(directory.toPath())) {
            String[] javaFiles = paths
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(p -> p.endsWith(".java"))
                    .toArray(String[]::new);

            if (javaFiles.length == 0) {
                return true; // Nothing to compile
            }

            System.out.println("Compiling " + javaFiles.length + " Java file(s)...");
            
            // Build arguments for compiler. We could add classpath here if needed.
            int result = compiler.run(null, null, null, javaFiles);
            
            if (result == 0) {
                System.out.println("Compilation successful.");
                return true;
            } else {
                System.err.println("Compilation failed with exit code: " + result);
                return false;
            }
        } catch (IOException e) {
            System.err.println("Failed to read directory for compilation: " + e.getMessage());
            return false;
        }
    }
}
