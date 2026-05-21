package org.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * ClassInspector uses the Java Reflection API to inspect and display
 * detailed metadata of any loaded Java class.
 */
public class ClassInspector {

    /**
     * Inspects a class and prints all of its metadata to the console.
     *
     * @param clazz The class to inspect.
     */
    public static void inspect(Class<?> clazz) {
        System.out.println("========================================================================");
        System.out.println("                         CLASS METADATA REPORT                          ");
        System.out.println("========================================================================");
        
        // 1. Basic Class Information
        System.out.printf("%-18s: %s%n", "Class Name", clazz.getSimpleName());
        System.out.printf("%-18s: %s%n", "Fully Qualified", clazz.getName());
        System.out.printf("%-18s: %s%n", "Package", clazz.getPackage() != null ? clazz.getPackage().getName() : "default");
        
        int modifiers = clazz.getModifiers();
        System.out.printf("%-18s: %s%n", "Modifiers", Modifier.toString(modifiers));
        
        Class<?> superclass = clazz.getSuperclass();
        System.out.printf("%-18s: %s%n", "Superclass", superclass != null ? superclass.getName() : "None");
        
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            String intfList = Arrays.stream(interfaces)
                    .map(Class::getName)
                    .collect(Collectors.joining(", "));
            System.out.printf("%-18s: %s%n", "Interfaces", intfList);
        } else {
            System.out.printf("%-18s: %s%n", "Interfaces", "None");
        }
        
        System.out.println("------------------------------------------------------------------------");
        
        // 2. Class Annotations
        System.out.println("Annotations:");
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        if (annotations.length > 0) {
            for (Annotation annotation : annotations) {
                System.out.println("  @ " + annotation.annotationType().getName());
            }
        } else {
            System.out.println("  None");
        }
        
        System.out.println("------------------------------------------------------------------------");
        
        // 3. Fields Information
        System.out.println("Fields:");
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length > 0) {
            for (Field field : fields) {
                String fieldMods = Modifier.toString(field.getModifiers());
                String fieldModStr = fieldMods.isEmpty() ? "" : fieldMods + " ";
                System.out.printf("  %s%s %s%n", fieldModStr, field.getType().getSimpleName(), field.getName());
                
                // Show field annotations if any
                Annotation[] fieldAnnos = field.getDeclaredAnnotations();
                for (Annotation anno : fieldAnnos) {
                    System.out.println("    @ " + anno.annotationType().getSimpleName());
                }
            }
        } else {
            System.out.println("  None");
        }
        
        System.out.println("------------------------------------------------------------------------");
        
        // 4. Constructors Information
        System.out.println("Constructors:");
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (constructors.length > 0) {
            for (Constructor<?> constructor : constructors) {
                String constMods = Modifier.toString(constructor.getModifiers());
                String constModStr = constMods.isEmpty() ? "" : constMods + " ";
                
                String params = Arrays.stream(constructor.getParameterTypes())
                        .map(Class::getSimpleName)
                        .collect(Collectors.joining(", "));
                
                System.out.printf("  %s%s(%s)%n", constModStr, clazz.getSimpleName(), params);
            }
        } else {
            System.out.println("  None");
        }
        
        System.out.println("------------------------------------------------------------------------");
        
        // 5. Methods Information
        System.out.println("Methods:");
        Method[] methods = clazz.getDeclaredMethods();
        if (methods.length > 0) {
            for (Method method : methods) {
                String methodMods = Modifier.toString(method.getModifiers());
                String methodModStr = methodMods.isEmpty() ? "" : methodMods + " ";
                
                String params = Arrays.stream(method.getParameterTypes())
                        .map(Class::getSimpleName)
                        .collect(Collectors.joining(", "));
                
                String exceptions = "";
                Class<?>[] exceptionTypes = method.getExceptionTypes();
                if (exceptionTypes.length > 0) {
                    exceptions = " throws " + Arrays.stream(exceptionTypes)
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(", "));
                }
                
                System.out.printf("  %s%s %s(%s)%s%n", 
                        methodModStr, 
                        method.getReturnType().getSimpleName(), 
                        method.getName(), 
                        params, 
                        exceptions);
                
                // Show method annotations if any
                Annotation[] methodAnnos = method.getDeclaredAnnotations();
                for (Annotation anno : methodAnnos) {
                    System.out.println("    @ " + anno.annotationType().getSimpleName());
                }
            }
        } else {
            System.out.println("  None");
        }
        System.out.println("========================================================================");
    }
}
