package org.test;

/**
 * A class with a run method that accepts parameters.
 * Should NOT be matched as runnable since it is not parameterless.
 */
public class ParameterizedRunTask {

    public ParameterizedRunTask() {
        System.out.println("ParameterizedRunTask: Constructor called!");
    }

    // This method takes an argument, so it is NOT parameterless
    public void run(String prefix) {
        System.out.println("ParameterizedRunTask: " + prefix + " run() with parameters was invoked!");
    }
}
