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
package org.wings.session;

import java.text.DateFormat;
import java.util.Date;

import org.wings.util.SStringBuilder;

/**
 * @author <a href="mailto:@mueller.de">armin</a>
 */
public class WingsStatistics {

    private static final WingsStatistics STATISTICS = new WingsStatistics();

    public static final WingsStatistics getStatistics() {
        return STATISTICS;
    }

    private int sessionCounter = 0;
    private int activeSessionCounter = 0;
    private int allocatedSessionCounter = 0;
    private final long birthDay = System.currentTimeMillis();

    private int requestCounter = 0;
    private long requestDuration = 0;


    public final int getRequestCount() {
        return requestCounter;
    }

    public final long getRequestDuration() {
        return requestDuration;
    }

    public final long getUptime() {
        return System.currentTimeMillis() - birthDay;
    }

    synchronized final void incrementRequestCount(long duration) {
        requestCounter++;
        requestDuration += duration;
    }

    synchronized final void incrementSessionCount() {
        sessionCounter++;
    }

    synchronized final void incrementActiveSessionCount() {
        activeSessionCounter++;
    }

    synchronized final void incrementAllocatedSessionCount() {
        allocatedSessionCounter++;
    }

    synchronized final void decrementActiveSessionCount() {
        activeSessionCounter--;
    }

    synchronized final void decrementAllocatedSessionCount() {
        allocatedSessionCounter--;
    }


    public final int getOverallSessionCount() {
        return sessionCounter;
    }

    public final int getActiveSessionCount() {
        return activeSessionCounter;
    }

    public final int getAllocatedSessionCount() {
        return allocatedSessionCounter;
    }


    private int dispatchCounter = 0;
    private long dispatchDuration = 0;

    private int deliverCounter = 0;
    private long deliverDuration = 0;


    synchronized final void incrementDispatchCount(long duration) {
        dispatchCounter++;
        dispatchDuration += duration;
    }

    synchronized final void incrementDeliverCount(long duration) {
        deliverCounter++;
        deliverDuration += duration;
    }


    public final int getDispatchCount() {
        return dispatchCounter;
    }

    public final long getDispatchDuration() {
        return dispatchDuration;
    }

    public final int getDeliverCount() {
        return deliverCounter;
    }

    public final long getDeliverDuration() {
        return deliverDuration;
    }

    public final String toString() {
        SStringBuilder tResult = new SStringBuilder();

        tResult.append("birthday: ").append(DateFormat.getDateTimeInstance().format(new Date(birthDay))).append("\n")
                .append("sessions: ").append(sessionCounter).append(" / ").append(activeSessionCounter).append(" / ").append(allocatedSessionCounter).append("\n")
                .append("requests: ").append(requestCounter).append(" / ").append(requestCounter == 0 ? 0 : requestDuration / requestCounter).append(" ms\n")
                .append("dispatch: ").append(dispatchCounter).append(" / ").append(dispatchCounter == 0 ? 0 : dispatchDuration / dispatchCounter).append(" ms\n")
                .append("deliver: ").append(deliverCounter).append(" / ").append(deliverCounter == 0 ? 0 : deliverDuration / deliverCounter).append(" ms\n");

        return tResult.toString();

    }
}
