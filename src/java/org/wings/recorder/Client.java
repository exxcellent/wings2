/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.recorder;

public class Client
        extends Thread {
    private Script script;
    protected String userid;
    protected String passwd;
    private int initialDelay = 0;
    private int iterations = 1;
    private int currentIteration = 0;

    public void init(Script script) {
        this.script = script;
    }

    public String getUserid() { return userid; }

    public void setUserid(String userid) { this.userid = userid; }

    public void setPasswd(String passwd) { this.passwd = passwd; }

    public void setInitialDelay(int initialDelay) {
        this.initialDelay = initialDelay;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getCurrentIteration() { return currentIteration; }

    public void run() {
        try {
            if (initialDelay > 0)
                sleep(initialDelay);
        } catch (InterruptedException e) { /* shit happens */ }

        login();

        for (currentIteration = 1; currentIteration <= iterations; currentIteration++) {
            try {
                long millis = System.currentTimeMillis();
                System.out.println("CLIENT: " + getUserid() + " begin (" + currentIteration + ")");
                script.execute();
                System.out.println("CLIENT: " + getUserid() + " end (" + currentIteration + ") after " +
                        (System.currentTimeMillis() - millis) + "ms");
            } catch (Exception e) {
                System.out.println("CLIENT: " + getUserid() + " in iteration " + currentIteration
                        + " threw exception: " +
                        e.getClass().getName() + ": " + e.getMessage());
                System.err.println(e.getMessage());
                e.printStackTrace(System.err);
            }
        }
    }

    protected void login() {}
}


