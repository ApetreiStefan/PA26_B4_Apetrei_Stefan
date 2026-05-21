package org.test;

/**
 * A class with a static run method.
 * Demonstrates invocation without class instantiation.
 */
public class StaticRunTask {

    // Prevent instantiation to prove we don't instantiate it
    private StaticRunTask() {
        throw new AssertionError("StaticRunTask: Should not be instantiated!");
    }

    public static void run() {
        System.out.println("StaticRunTask: Static run() method was successfully executed without creating an object!");
    }
}
