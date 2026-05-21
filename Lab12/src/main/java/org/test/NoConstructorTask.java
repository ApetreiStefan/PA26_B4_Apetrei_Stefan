package org.test;

/**
 * A class with an instance run method but no no-argument constructor.
 * Used to test error handling when instantiation fails.
 */
public class NoConstructorTask {

    private final String message;

    // Only constructor requires an argument - no default constructor is available
    public NoConstructorTask(String message) {
        this.message = message;
        System.out.println("NoConstructorTask: Constructor called with message: " + message);
    }

    public void run() {
        System.out.println("NoConstructorTask: Running with message: " + message);
    }
}
