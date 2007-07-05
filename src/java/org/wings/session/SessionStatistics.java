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
import java.io.Serializable;

import org.wings.util.SStringBuilder;

/**
 * @author <a href="mailto:@mueller.de">armin</a>
 */
public class SessionStatistics implements Serializable {

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
        SStringBuilder tResult = new SStringBuilder();

        tResult.append("birthday: ").append(DateFormat.getDateTimeInstance().format(new Date(birthDay))).append("\n")
                .append("requests: ").append(requestCounter).append(" / ").append(requestCounter == 0 ? 0 : requestDuration / requestCounter).append(" ms\n")
                .append("dispatch: ").append(dispatchCounter).append(" / ").append(dispatchCounter == 0 ? 0 : dispatchDuration / dispatchCounter).append(" ms\n")
                .append("deliver: ").append(deliverCounter).append(" / ").append(deliverCounter == 0 ? 0 : deliverDuration / deliverCounter).append(" ms\n");

        return tResult.toString();
    }
}
