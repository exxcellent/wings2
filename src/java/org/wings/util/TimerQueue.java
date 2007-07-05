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
package org.wings.util;

/*
 * Die Klasse ist praktisch von der Swing Implementierung
 * abgeleitet. Leider brauch ich einen Timer, der auch innerhalb des
 * Swing Event Threads Impulse gibt. Deshalb angepasst. Ich hoffe das
 * gibt keine Problemen mit dem Swing Team.
 */

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public final class TimerQueue
        implements Runnable {
    /*
     * Es gibt nur eine Queue pro Application.
     */
    private static TimerQueue sharedInstance = new TimerQueue();

    private Timer firstTimer;
    private boolean running;

    /*
     * Es gibt nur eine TimerQueue pro Application.
     */
    private TimerQueue() {
        super();

        // Now start the TimerQueue thread.
        start();
    }

    /*
     * Die eine Queue!!
     */
    public static TimerQueue sharedInstance() {
        return sharedInstance;
    }

    private synchronized void start() {
        System.err.println("Timer started");
        if (running) {
            throw new RuntimeException("Can't start a TimerQueue that is already running");
        } else {
            Thread timerThread = new Thread(this, "TimerQueue");

            try {
                timerThread.setDaemon(true);
            } catch (SecurityException e) {}
            timerThread.start();
            running = true;
        }
    }

    private synchronized void stop() {
        running = false;
        notifyAll();
    }

    public synchronized void addTimer(Timer timer, long expirationTime) {
        Timer previousTimer, nextTimer;

        // If the Timer is already in the queue, then ignore the add.
        if (timer.running) {
            return;
        }

        previousTimer = null;
        nextTimer = firstTimer;

        // Insert the Timer into the linked list in the order they will
        // expire.  If two timers expire at the same time, put the newer entry
        // later so they expire in the order they came in.

        while (nextTimer != null) {
            if (nextTimer.expirationTime > expirationTime)
                break;

            previousTimer = nextTimer;
            nextTimer = nextTimer.nextTimer;
        }

        if (previousTimer == null)
            firstTimer = timer;
        else
            previousTimer.nextTimer = timer;

        timer.expirationTime = expirationTime;
        timer.nextTimer = nextTimer;
        timer.running = true;
        notifyAll();
    }

    public synchronized void removeTimer(Timer timer) {
        boolean found;
        Timer previousTimer, nextTimer;

        if (!timer.running)
            return;

        previousTimer = null;
        nextTimer = firstTimer;
        found = false;

        while (nextTimer != null) {
            if (nextTimer == timer) {
                found = true;
                break;
            }

            previousTimer = nextTimer;
            nextTimer = nextTimer.nextTimer;
        }

        if (!found)
            return;

        if (previousTimer == null)
            firstTimer = timer.nextTimer;
        else
            previousTimer.nextTimer = timer.nextTimer;

        timer.expirationTime = 0;
        timer.nextTimer = null;
        timer.running = false;
    }

    public synchronized boolean containsTimer(Timer timer) {
        return timer.running;
    }

    // If there are a ton of timers, this method may never return.  It loops
    // checking to see if the head of the Timer list has expired.  If it has,
    // it posts the Timer and reschedules it if necessary.

    public synchronized long postExpiredTimers() {
        long currentTime, timeToWait;
        Timer timer;

        // The timeToWait we return should never be negative and only be zero
        // when we have no Timers to wait for.

        do {
            timer = firstTimer;
            if (timer == null)
                return 0;

            currentTime = System.currentTimeMillis();
            timeToWait = timer.expirationTime - currentTime;

            if (timeToWait <= 0) {
                try {
                    timer.post();  // have timer post an event
                } catch (SecurityException e) {}

                // remove the timer from the queue
                removeTimer(timer);

                // This tries to keep the interval uniform at the cost of
                // drift.
                if (timer.isRepeats()) {
                    addTimer(timer, currentTime + timer.getDelay());
                }
            }

            // Allow other threads to call addTimer() and removeTimer()
            // even when we are posting Timers like mad.  Since the wait()
            // releases the lock, be sure not to maintain any state
            // between iterations of the loop.

            try {
                wait(1);
            } catch (InterruptedException e) {
            }
        } while (timeToWait <= 0);

        return timeToWait;
    }

    public synchronized void run() {
        long timeToWait;

        try {
            while (running) {
                timeToWait = postExpiredTimers();
                try {
                    wait(timeToWait);
                } catch (InterruptedException e) { }
            }
        } catch (ThreadDeath td) {
            running = false;
            // Mark all the timers we contain as not being queued.
            Timer timer = firstTimer;
            while (timer != null) {
                timer.eventQueued = false;
                timer = timer.nextTimer;
            }
            sharedInstance.start();
            throw td;
        }
    }

    public synchronized String toString() {
        SStringBuilder buf;
        Timer nextTimer;

        buf = new SStringBuilder();
        buf.append("TimerQueue (");

        nextTimer = firstTimer;
        while (nextTimer != null) {
            buf.append(nextTimer.toString());

            nextTimer = nextTimer.nextTimer;
            if (nextTimer != null)
                buf.append(", ");
        }

        buf.append(")");
        return buf.toString();
    }
}


