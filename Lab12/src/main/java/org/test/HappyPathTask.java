package org.test;

/**
 * A standard, public runnable class with a public run method.
 */
public class HappyPathTask {

    public HappyPathTask() {
        System.out.println("HappyPathTask: Constructor called!");
    }

    public void run() {
        System.out.println("HappyPathTask: Hello! The run() method was successfully invoked via reflection!");
    }
}
