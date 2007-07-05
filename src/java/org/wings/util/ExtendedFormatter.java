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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogRecord;

public class ExtendedFormatter
        extends java.util.logging.Formatter {
    private DateFormat dateFormatter;

    public ExtendedFormatter() {
        dateFormatter = new SimpleDateFormat("yyyyMMdd hh:mm:ss,S");
    }

    public String format(LogRecord record) {
        SStringBuilder msg = new SStringBuilder();
        msg.append(dateFormatter.format(new Date()));
        msg.append(" ");
        msg.append(record.getLevel());
        msg.append(" ");
        msg.append(record.getSourceClassName());
        msg.append(" ");
        msg.append(record.getSourceMethodName());
        msg.append(": ");
        msg.append(formatMessage(record));
        msg.append("\n");
        if (record.getThrown() != null)
            msg.append(getBackTrace(record.getThrown()));

        return msg.toString();
    }

    protected String getBackTrace(Throwable theThrown) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        theThrown.printStackTrace(pw);
        pw.close();
        return sw.getBuffer().toString();
    }
}
