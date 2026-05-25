package org.reflection;

import java.lang.reflect.*;

/**
 * ClassMethodInvoker handles checking and dynamic invocation of the
 * 'run' method with no arguments on a loaded Java class.
 */
public class ClassMethodInvoker {

    /**
     * Checks if the class contains a method named 'run' with no arguments.
     *
     * @param clazz The class to check.
     * @return true if the method exists, false otherwise.
     */
    public static boolean hasRunMethod(Class<?> clazz) {
        try {
            // Check for a declared method named 'run' with no parameters
            clazz.getDeclaredMethod("run");
            return true;
        } catch (NoSuchMethodException e) {
            // Check in parent classes (in case it is inherited but not declared in the class itself)
            try {
                clazz.getMethod("run");
                return true;
            } catch (NoSuchMethodException ex) {
                return false;
            }
        }
    }

    /**
     * Invokes the 'run' method with no arguments using Java Reflection.
     * If the method is static, it invokes it directly on the class.
     * If the method is an instance method, it instantiates the class using its
     * default (no-argument) constructor first, then invokes it.
     *
     * @param clazz The class on which to invoke the run method.
     * @throws Exception If any reflective operation or the method invocation fails.
     */
    public static void invokeRunMethod(Class<?> clazz) throws Exception {
        Method runMethod = null;
        
        // 1. Try to find the run method (either declared directly or inherited)
        try {
            runMethod = clazz.getDeclaredMethod("run");
        } catch (NoSuchMethodException e) {
            try {
                runMethod = clazz.getMethod("run");
            } catch (NoSuchMethodException ex) {
                throw new NoSuchMethodException("No 'run' method with no arguments found in class " + clazz.getName());
            }
        }

        // Make it accessible in case it is protected, package-private, or private
        runMethod.setAccessible(true);

        boolean isStatic = Modifier.isStatic(runMethod.getModifiers());
        System.out.println("Invoking: " + (isStatic ? "static " : "instance ") + runMethod.getName() + "() in " + clazz.getName());
        
        long startTime = System.nanoTime();
        Object result;
        
        if (isStatic) {
            // Invoking static method doesn't need an instance (pass null)
            result = runMethod.invoke(null);
        } else {
            // Instantiating the class using the default constructor
            Constructor<?> constructor;
            try {
                constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new NoSuchMethodException("The method 'run' is an instance method, but " 
                        + clazz.getName() + " does not have a public or declared no-argument constructor.");
            }
            
            Object instance = constructor.newInstance();
            result = runMethod.invoke(instance);
        }
        
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;

        System.out.printf("Execution complete (took %.3f ms).%n", durationMs);
        if (runMethod.getReturnType() != void.class) {
            System.out.println("Return value: " + result);
        } else {
            System.out.println("Return value: void");
        }
    }

    /**
     * Invokes methods annotated with specific annotations.
     * Handles methods with 0 arguments, or 1 integer argument (using a mock value).
     *
     * @param clazz The class to inspect and invoke methods on.
     * @param targetAnnotations Set of annotation class names to look for.
     */
    public static void invokeAnnotatedMethods(Class<?> clazz, java.util.Set<String> targetAnnotations) {
        Method[] methods = clazz.getDeclaredMethods();
        Object instance = null;
        boolean instanceCreated = false;

        for (Method method : methods) {
            boolean hasTargetAnno = false;
            for (java.lang.annotation.Annotation anno : method.getDeclaredAnnotations()) {
                if (targetAnnotations.contains(anno.annotationType().getName())) {
                    hasTargetAnno = true;
                    break;
                }
            }

            if (!hasTargetAnno) continue;

            method.setAccessible(true);
            boolean isStatic = Modifier.isStatic(method.getModifiers());
            Class<?>[] paramTypes = method.getParameterTypes();

            if (paramTypes.length == 0 || (paramTypes.length == 1 && (paramTypes[0] == int.class || paramTypes[0] == Integer.class))) {
                try {
                    if (!isStatic && !instanceCreated) {
                        Constructor<?> constructor = clazz.getDeclaredConstructor();
                        constructor.setAccessible(true);
                        instance = constructor.newInstance();
                        instanceCreated = true;
                    }

                    Object target = isStatic ? null : instance;
                    Object[] args = new Object[paramTypes.length];
                    if (paramTypes.length == 1) {
                        args[0] = 42; // Mock value
                    }

                    System.out.println("Invoking annotated method: " + method.getName() + " with args: " + java.util.Arrays.toString(args));
                    method.invoke(target, args);
                    
                } catch (Exception e) {
                    System.err.println("Failed to invoke method " + method.getName() + ": " + e.getCause());
                }
            } else {
                System.out.println("Skipping annotated method " + method.getName() + " (Unsupported parameters)");
            }
        }
    }
}
