package org.wings.session;

import java.text.DateFormat;
import java.util.Date;

/**
 * <!--
 * Erstellt: 15.05.2003 10:29:14   Von: armin
 * Geändert: 15.05.2003 10:29:14   Von: armin
 * Copyright:     Copyright (c) 2003
 * Organisation:  Müller GmbH & Co. KG
 *  -->
 *
 *
 * @author <a href="mailto:@mueller.de">armin</a>
 * @version $Revision$
 */
public class SessionStatistics {

    private final long birthDay = System.currentTimeMillis();

    private long dispatchStartTime = 0;
    private int dispatchCounter = 0;
    private long dispatchDuration = 0;

    private long deliverStartTime = 0;
    private int deliverCounter = 0;
    private long deliverDuration = 0;

    private long requestStartTime = 0;
    private int requestCounter = 0;
    private long requestDuration = 0;


    public final long getBirthDay() {
        return birthDay;
    }

    public final long getUptime() {
        return System.currentTimeMillis() - birthDay;
    }

    final void startRequest() {
        requestStartTime = System.currentTimeMillis();
    }

    final void endRequest() {
        endDispatching();
        endDelivering();
        if (requestStartTime > 0) {
            long duration = System.currentTimeMillis() - requestStartTime;
            WingsStatistics.getStatistics().incrementRequestCount(duration);

            requestCounter++;
            requestDuration += duration;
            requestStartTime = -1;
        }
    }

    public final long getRequestCount() {
        return requestCounter;
    }

    public final long getRequestDuration() {
        return requestDuration;
    }

    final void startDispatching() {
        dispatchStartTime = System.currentTimeMillis();
    }

    final void endDispatching() {
        if (dispatchStartTime > 0) {
            long duration = System.currentTimeMillis() - dispatchStartTime;
            WingsStatistics.getStatistics().incrementDispatchCount(duration);

            dispatchCounter++;
            dispatchDuration += duration;
            dispatchStartTime = -1;
        }
    }

    public final int getDispatchCount() {
        return dispatchCounter;
    }

    public final long getDispatchDuration() {
        return dispatchDuration;
    }


    final void startDelivering() {
        deliverStartTime = System.currentTimeMillis();
    }

    final void endDelivering() {
        if (deliverStartTime > 0) {
            long duration = System.currentTimeMillis() - deliverStartTime;
            WingsStatistics.getStatistics().incrementDeliverCount(duration);

            deliverCounter++;
            deliverDuration += duration;
            deliverStartTime = -1;
        }
    }

    public final int getDeliverCount() {
        return deliverCounter;
    }

    public final long getDeliverDuration() {
        return deliverDuration;
    }

    public String toString() {
        StringBuffer tResult = new StringBuffer();

        tResult.append("birthday: ").append(DateFormat.getDateTimeInstance().format(new Date(birthDay))).append("\n")
            .append("requests: ").append(requestCounter).append(" / ").append(requestCounter == 0 ? 0 : requestDuration / requestCounter).append(" ms\n")
            .append("dispatch: ").append(dispatchCounter).append(" / ").append(dispatchCounter == 0 ? 0 : dispatchDuration / dispatchCounter).append(" ms\n")
            .append("deliver: ").append(deliverCounter).append(" / ").append(deliverCounter == 0 ? 0 : deliverDuration / deliverCounter).append(" ms\n");

        return tResult.toString();
    }
}

/*
   $Log$
   Revision 1.1  2004/10/04 16:13:25  hengels
   Initial revision

   Revision 1.1  2003/06/04 08:20:17  arminhaaf
   o collect statistics

 */