/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.StringTokenizer;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class DebugUtil {
    private static final boolean DEBUG = true;

    /**
     * No way to instantiate
     */
    private DebugUtil() {
    }

    /*
     * Wieviele Methoden Aufrufe brauch der interne Mechanismus fuer
     * getCodeLine(int).
     */
    private static final int INTERNAL_CODE_LINE_CALL_LEVEL = 3;

    /*
     * Um ein Exception Stacktrace in einen String zu schreiben
     */
    private static final StringWriter STACK_TRACE_STRING_WRITER =
            new StringWriter();

    /*
     * Um ein Exception Stacktrace in einen String zu schreiben
     */
    private static final PrintWriter STACK_TRACE_PRINT_WRITER =
            new PrintWriter(STACK_TRACE_STRING_WRITER);

    private static final Throwable STACK_TRACE_EXCEPTION = new Throwable();

    private static final synchronized String getStackTraceString() {
        return getStackTraceString(STACK_TRACE_EXCEPTION.fillInStackTrace());
        // kann sein, dass das schneller ist (vor allem mit JIT), verbraucht aber
        // sicher mehr Resourcen
        //return getStackTraceString(new Throwable());
    }

    public static final synchronized String getStackTraceString(Throwable e) {
        STACK_TRACE_STRING_WRITER.getBuffer().setLength(0);
        e.printStackTrace(STACK_TRACE_PRINT_WRITER);
        return STACK_TRACE_STRING_WRITER.toString();
    }

    public static final String getCodeLineStack(int callLevel) {
        StringTokenizer t =
                new StringTokenizer(getStackTraceString(),
                        System.getProperty("line.separator"));

        for (int i = 0; i < callLevel + INTERNAL_CODE_LINE_CALL_LEVEL &&
                t.hasMoreTokens(); i++)
            t.nextToken();

        if (t.hasMoreTokens())
            return t.nextToken().trim();
        else
            return "";
    }

    public static final String getCodeLineStack() {
        return getCodeLineStack(1);
    }

    public static final String getCodeLine() {
        return getCodeLine(1);
    }

    public static final String getCodeLine(int callLevel) {
        String erg = getCodeLineStack(callLevel + 1);
        int pindex = erg.lastIndexOf("(");
        if (pindex >= 0) {
            erg = erg.substring(pindex + 1, erg.length() - 1);
            int cindex = erg.indexOf(":");
            if (cindex >= 0)
                return erg.substring(cindex + 1);
        }
        return "?";
    }


    public static final String getDebugMessage(Class c, String mesg) {
        return "[" + c.getName() + ":" + getCodeLine(2) + "] " + mesg;
    }

    /**
     * This method should be used in a Class lokal static method like
     * <CODE>
     * private static final void debug(String mesg) {
     * if ( DEBUG && de.mercatis.Global.DEBUG )
     * de.mercatis.util.DebugUtil.printDebugMessage(this.class, mesg));
     * }
     * </CODE>
     */
    public static final void printDebugMessage(Class c, String mesg) {
        //    System.out.println("[" + c.getName() + ":" + getCodeLine(2) + "] " +
        //                       mesg);

        System.err.println("[" + c.getName() + "] " + mesg);
    }
}


