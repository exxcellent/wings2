package org.wings.util;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;

public class ExtendedFormatter
    extends java.util.logging.Formatter
{
    private DateFormat dateFormatter;

    public ExtendedFormatter() {
        dateFormatter = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
    }

    public String format(LogRecord record) {
        StringBuffer msg = new StringBuffer();
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
