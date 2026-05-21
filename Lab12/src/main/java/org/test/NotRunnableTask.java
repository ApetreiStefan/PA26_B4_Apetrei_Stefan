package org.test;

/**
 * A class that is NOT runnable because it lacks a run() method.
 */
public class NotRunnableTask {

    public NotRunnableTask() {
        System.out.println("NotRunnableTask: Constructor called!");
    }

    public void doSomethingElse() {
        System.out.println("NotRunnableTask: Doing something completely unrelated!");
    }
}
