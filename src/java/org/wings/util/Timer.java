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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Vector;

/*
 * Die Klasse ist praktisch von der Swing Implementierung
 * abgeleitet. Leider brauch ich einen Timer, der auch innerhalb des
 * Swing Event Threads Impulse gibt. Deshalb angepasst. Ich hoffe das
 * gibt keine Problemen mit dem Swing Team.
 */

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public final class Timer
        implements Serializable {
    private final static Log log = LogFactory.getLog(Timer.class);

    /*
     * Die Verzoegerung, bis das erste mal ein Impuls (ActionEvent)
     * kommt.
     */
    private long initialDelay;

    /*
     * Die Dauer zwischen 2 Impulsen (ActionEvent)
     */
    private long delay;

    /*
     * Einmaliger Impuls, oder andauernder.
     */
    private boolean repeats = true;

    /*
     * Sollen Impulse, die zur gleichen Zeit auflaufen (etwa weil der
     * {@link TimerQueue} so lange geschlafen hat, bis mehrere Impulse vom
     * gleichen Timer aufgelaufen sind) sind zusammengefasst und nur einer
     * geschickt werden ?
     */
    private boolean coalesce = true;

    /*
     * Alle ActionListener, die zu benachrichtigen sind.
     */
    private final Vector listenerList = new Vector();

    boolean eventQueued = false;

    /**
     * Command used when action is fired.
     */
    private String actionCommand = null;

    /*
     * Sollen Impulse geloggt werden.
     */
    private static boolean logTimers;

    // These fields are maintained by TimerQueue.
    // eventQueued can also be reset by the TimerQueue, but will only ever
    // happen in applet case when TimerQueues thread is destroyed.
    long expirationTime;
    Timer nextTimer;
    boolean running;

    /**
     * Creates a Timer that will notify its listeners every
     * <i>delay</i> milliseconds.
     *
     * @param delay    The number of milliseconds between listener notification
     * @param listener An initial listener
     * @see #setInitialDelay
     * @see #setRepeats
     */
    public Timer(long delay, ActionListener listener) {
        super();
        this.delay = delay;
        this.initialDelay = delay;

        addActionListener(listener);
    }

    /**
     * Adds an actionListener to the Timer
     */
    public void addActionListener(ActionListener listener) {
        listenerList.addElement(listener);
    }

    /**
     * Removes an ActionListener from the Timer.
     */
    public void removeActionListener(ActionListener listener) {
        listenerList.removeElement(listener);
    }

    /**
     * Sets action command for this timer.
     */
    public void setActionCommand(String command) {
        actionCommand = command;
    }

    /**
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     */
    protected void fireActionPerformed(ActionEvent e) {
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listenerList.size() - 1; i >= 0; i--) {
            ((ActionListener) listenerList.elementAt(i)).actionPerformed(e);
        }
    }

    /**
     * Returns the timer queue.
     */
    TimerQueue timerQueue() {
        return TimerQueue.sharedInstance();
    }

    /**
     * Enables or disables the timer log. When enabled, a message
     * is posted to System.out whenever the timer goes off.
     *
     * @param flag true to enable logging
     * @see #getLogTimers
     */
    public static void setLogTimers(boolean flag) {
        logTimers = flag;
    }

    /**
     * Returns true if logging is enabled.
     *
     * @return true if logging is enabled
     * @see #setLogTimers
     */
    public static boolean getLogTimers() {
        return logTimers;
    }

    /**
     * Sets the Timer's delay, the number of milliseconds between successive
     * <b>actionPerfomed()</b> messages to its listeners
     *
     * @see #setInitialDelay
     */
    public void setDelay(long delay) {
        TimerQueue queue;

        if (delay < 0) {
            throw new RuntimeException("Invalid initial delay: " + delay);
        }
        this.delay = delay;

        if (isRunning()) {
            queue = timerQueue();
            queue.removeTimer(this);
            cancelEvent();
            queue.addTimer(this, System.currentTimeMillis() + delay);
        }
    }

    /**
     * Returns the Timer's delay.
     *
     * @see #setDelay
     */
    public long getDelay() {
        return delay;
    }

    /**
     * Sets the Timer's initial delay.  This will be used for the first
     * "ringing" of the Timer only.  Subsequent ringings will be spaced
     * using the delay property.
     *
     * @see #setDelay
     */
    public void setInitialDelay(int initialDelay) {
        if (initialDelay < 0) {
            throw new RuntimeException("Invalid initial delay: " +
                    initialDelay);
        }
        this.initialDelay = initialDelay;
    }

    /**
     * Returns the Timer's initial delay.
     *
     * @see #setDelay
     */
    public long getInitialDelay() {
        return initialDelay;
    }

    /**
     * If <b>flag</b> is <b>false</b>, instructs the Timer to send
     * <b>actionPerformed()</b> to its listeners only once, and then stop.
     */
    public void setRepeats(boolean flag) {
        repeats = flag;
    }

    /**
     * Returns <b>true</b> if the Timer will send a <b>actionPerformed()</b>
     * message to its listeners multiple times.
     *
     * @see #setRepeats
     */
    public boolean isRepeats() {
        return repeats;
    }

    /**
     * Sets whether the Timer coalesces multiple pending ActionEvent firings.
     * A busy application may not be able
     * to keep up with a Timer's message generation, causing multiple
     * <b>actionPerformed()</b> message sends to be queued.  When processed,
     * the application sends these messages one after the other, causing the
     * Timer's listeners to receive a sequence of <b>actionPerformed()</b>
     * messages with no delay between them. Coalescing avoids this situation
     * by reducing multiple pending messages to a single message send. Timers
     * coalesce their message sends by default.
     */
    public void setCoalesce(boolean flag) {
        coalesce = flag;
    }

    /**
     * Returns <b>true</b> if the Timer coalesces multiple pending
     * <b>performCommand()</b> messages.
     *
     * @see #setCoalesce
     */
    public boolean isCoalesce() {
        return coalesce;
    }

    /**
     * Starts the Timer, causing it to send <b>actionPerformed()</b> messages
     * to its listeners.
     *
     * @see #stop
     */
    public void start() {
        timerQueue().addTimer(this, System.currentTimeMillis() + getInitialDelay());
    }

    /**
     * Returns <b>true</b> if the Timer is running.
     *
     * @see #start
     */
    public boolean isRunning() {
        return timerQueue().containsTimer(this);
    }

    /**
     * Stops a Timer, causing it to stop sending <b>actionPerformed()</b>
     * messages to its Target.
     *
     * @see #start
     */
    public void stop() {
        timerQueue().removeTimer(this);
        cancelEvent();
    }

    /**
     * Restarts a Timer, canceling any pending firings, and causing
     * it to fire with its initial dely.
     */
    public void restart() {
        stop();
        start();
    }

    synchronized void cancelEvent() {
        eventQueued = false;
    }

    synchronized void post() {
        if (eventQueued == false) {
            eventQueued = true;
            if (logTimers) {
                log.debug("Timer ringing: " + Timer.this);
            }
            if (eventQueued) {
                fireActionPerformed(new ActionEvent(Timer.this, 0, this.actionCommand));
                cancelEvent();
            }
        }
    }
}


