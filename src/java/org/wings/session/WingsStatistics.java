package org.wings.session;

import java.text.DateFormat;
import java.util.Date;

/**
 * <!--
 * Erstellt: 15.05.2003 10:40:11   Von: armin
 * Geändert: 15.05.2003 10:40:11   Von: armin
 * Copyright:     Copyright (c) 2003
 * Organisation:  Müller GmbH & Co. KG
 *  -->
 *
 *
 * @author <a href="mailto:@mueller.de">armin</a>
 * @version $Revision$
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
        StringBuffer tResult = new StringBuffer();

        tResult.append("birthday: ").append(DateFormat.getDateTimeInstance().format(new Date(birthDay))).append("\n")
            .append("sessions: ").append(sessionCounter).append(" / ").append(activeSessionCounter).append(" / ").append(allocatedSessionCounter).append("\n")
            .append("requests: ").append(requestCounter).append(" / ").append(requestCounter == 0 ? 0 : requestDuration / requestCounter).append(" ms\n")
            .append("dispatch: ").append(dispatchCounter).append(" / ").append(dispatchCounter == 0 ? 0 : dispatchDuration / dispatchCounter).append(" ms\n")
            .append("deliver: ").append(deliverCounter).append(" / ").append(deliverCounter == 0 ? 0 : deliverDuration / deliverCounter).append(" ms\n");

        return tResult.toString();

    }
}

/*
   $Log$
   Revision 1.1  2004/10/04 16:13:26  hengels
   Initial revision

   Revision 1.1  2003/06/04 08:20:18  arminhaaf
   o collect statistics

 */