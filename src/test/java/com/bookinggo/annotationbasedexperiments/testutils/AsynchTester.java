package com.bookinggo.annotationbasedexperiments.testutils;

public class AsynchTester {
    private Thread thread;
    private AssertionError exc;

    public AsynchTester(final Runnable runnable) {
        thread = new Thread(() -> {
            try {
                runnable.run();
            } catch (AssertionError e) {
                exc = e;
            }
        });
    }

    public void start() {
        thread.start();
    }

    public void test() throws InterruptedException {
        thread.join();
        if (exc != null)
            throw exc;
    }
}