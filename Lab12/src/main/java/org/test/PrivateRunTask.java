package org.test;

/**
 * A class with a private run method.
 * Demonstrates that Java Reflection can invoke private methods by bypassing visibility checks.
 */
public class PrivateRunTask {

    public PrivateRunTask() {
        System.out.println("PrivateRunTask: Constructor called!");
    }

    private void run() {
        System.out.println("PrivateRunTask: Wow! A private run() method was invoked via reflection!");
    }
}
