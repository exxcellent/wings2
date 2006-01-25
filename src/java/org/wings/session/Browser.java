/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.session;

import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.Serializable;
import java.util.Locale;
import java.util.StringTokenizer;


/**
 * Detect the browser from the user-agent string passed in the HTTP header.
 *
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @version $Revision$
 */
public class Browser implements Serializable {
    private String agent;
    private int majorVersion;
    private double minorVersion;
    private String release;
    private String os;
    private OSType osType = OSType.UNKNOWN;
    private String osVersion;
    private String browserName;
    private Locale browserLocale;
    private BrowserType browserType = BrowserType.UNKNOWN;

    /**
     * Create a new browser object and start scanning for
     * browser, os and client language in given string.
     *
     * @param agent the "User-Agent" string from the request.
     */
    public Browser(String agent) {
        this.agent = agent;
        detect();
    }

    /**
     * Get the browser browserName (Mozilla, MSIE, Opera etc.).
     */
    public String getBrowserName() {
        return browserName;
    }

    /**
     * Gets the classification of the browser, this can be either GECKO, IE, KONQUEROR, MOZILLA, OPERA or UNKNOWN.
     * @return A classification of the browser {@link BrowserType}
     */
    public BrowserType getBrowserType() {
        return browserType;
    }

    /**
     * Get the browser major version.
     * <br>f.e. the major version for <i>Netscape 6.2</i> is <i>6</i>.
     *
     * @return the major version or <i>0</i> if not found
     */
    public int getMajorVersion() {
        return majorVersion;
    }

    /**
     * Get the minor version. This is the number after the
     * dot in the version string.
     * <br>f.e. the minor version for <i>Netscape 6.01</i> is <i>0.01</i>.
     *
     * @return the minor version if found, <i>0</i> otherwise
     */
    public double getMinorVersion() {
        return minorVersion;
    }

    /**
     * Get additional information about browser version.
     * <br>f.e. the release for <i>MSIE 6.1b</i> is <i>b</i>.
     *
     * @return the release or <i>null</i>, if not available.
     */
    public String getRelease() {
        return release;
    }

    /**
     * Get the operating system string provided by the browser. {@link OSType}
     *
     * @return the os browserName or <i>null</i>, if not available.
     */
    public String getOs() {
        return os;
    }

    /**
     * Get the operating system version.
     *
     * @return the os version or <i>null</i>, if not available.
     */
    public String getOsVersion() {
        return osVersion;
    }

    /**
     * Get the operating system type.
     *
     * @return A valid {@link OSType}
     */
    public OSType getOsType() {
        return osType;
    }

    /**
     * Get the browser/client locale.
     *
     * @return the found locale or the default server locale
     *         specified by {@link Locale#getDefault} if not found.
     */
    public Locale getClientLocale() {
        return browserLocale;
    }

    /* regexps are not threadsafe, we have to create them. */
    private transient RE RE_START;
    private transient RE RE_MSIE;
    private transient RE RE_MSIE_WIN_LANG_OS;
    private transient RE RE_MSIE_MAC_LANG_OS;
    private transient RE RE_NS_LANG_OS;
    private transient RE RE_NS_X11_LANG_OS;
    private transient RE RE_NS6_LANG_OS;
    private transient RE RE_LANG;
    private transient RE RE_OPERA;
    private transient RE RE_OPERA8X_CLOAKED;
    private transient RE RE_OPERA_LANG_OS;
    private transient RE RE_KONQUEROR_OS;
    private transient RE RE_GALEON_OS;
    private transient RE RE_GECKO_ENGINE;

    /**
     * Lazily create RE as they are not serializable. Will be null after session restore.
     */
    private void createREs() {
        try {
            if (RE_START == null) {
                RE_START = new RE("^([a-zA-Z0-9_\\-]+)(/([0-9])\\.([0-9]+))?");
                RE_MSIE = new RE("MSIE ([0-9])\\.([0-9]+)([a-z])?");
                RE_MSIE_WIN_LANG_OS = new RE("[wW]in(dows)? ([A-Z0-9]+) ?([0-9]\\.[0-9])?");
                RE_MSIE_MAC_LANG_OS = new RE("Mac_PowerPC");
                RE_NS_LANG_OS = new RE("\\[([a-z-]+)\\][ a-zA-Z0-9-]*\\(([a-zA-Z\\-]+)/?([0-9]* ?[.a-zA-Z0-9 ]*);");
                RE_NS_X11_LANG_OS = new RE("\\(X11; U; ([a-zA-Z-]+) ([0-9\\.]+)[^\\);]+\\)");
                RE_NS6_LANG_OS = new RE("\\(([a-zA-Z0-9]+); [a-zA-Z]+; ([a-zA-Z0-9_]+)( ([a-zA-Z0-9]+))?; ([_a-zA-Z-]+);");
                RE_LANG = new RE("\\[([_a-zA-Z-]+)\\]");
                RE_OPERA = new RE("((; )|\\()([a-zA-Z0-9\\-]+)[ ]+([a-zA-Z0-9\\.]+)([^;\\)]*)(; U)?\\) Opera ([0-9]+)\\.([0-9]+)[ ]+\\[([_a-zA-Z-]+)\\]");
                RE_OPERA8X_CLOAKED = new RE("((; )|\\()([a-zA-Z0-9\\-]+)[ ]+([a-zA-Z0-9\\.]+)([^;\\)]*)[; ]+([_a-zA-Z-]+)\\) Opera ([0-9]+)\\.([0-9]+)");
                RE_OPERA_LANG_OS = new RE("\\(([a-zA-Z0-9\\-]+) ([0-9\\.]+)[^)]+\\)[ \t]*\\[([a-z_]+)\\]");
                RE_KONQUEROR_OS = new RE("Konqueror/([0-9\\.]+); ([a-zA-Z0-9\\-]+)");
                RE_GALEON_OS = new RE("\\(([a-zA-Z0-9]+); U; Galeon; ([0-9]+)\\.([0-9]+);");
                RE_GECKO_ENGINE = new RE("Gecko/[0-9]*( ([a-zA-Z]+)+[0-9]*/([0-9]+)\\.([0-9]+)([a-zA-Z0-9]*))?");
            }
        } catch (RESyntaxException e) {
            throw new RuntimeException(e); // You broke one of th REs!
        }
    }

    /**
     * That does all the work.
     */
    protected void detect() {
        if (agent == null || agent.length() == 0)
            return;
        String mav, miv, lang = null;

        createREs();

        if (RE_START.match(agent)) {
            browserName = RE_START.getParen(1);
            mav = RE_START.getParen(3);
            miv = RE_START.getParen(4);
            
            /* RE_MSIE hides itself behind Mozilla or different browserName,
               good idea, congratulation Bill !
            */
            if (RE_MSIE.match(agent)) {
                browserName = "MSIE";
                browserType = BrowserType.IE;
                mav = RE_MSIE.getParen(1);
                miv = RE_MSIE.getParen(2);
                release = RE_MSIE.getParen(3);

                if (RE_MSIE_WIN_LANG_OS.match(agent)) {
                    osType = OSType.WINDOWS;
                    os = "Windows";
                    osVersion = RE_MSIE_WIN_LANG_OS.getParen(2) +
                            (RE_MSIE_WIN_LANG_OS.getParen(3) == null ?
                            "" :
                            " " + RE_MSIE_WIN_LANG_OS.getParen(3));
                } else if (RE_MSIE_MAC_LANG_OS.match(agent)) {
                    os = "MacOS";
                    osType = OSType.MACOS;
                }
            }
            /* Mozilla has two different id's; one up to version 4
               and a second for version >= 5
            */
            else if (browserName.equals("Mozilla") || browserName == null) {
                browserName = "Mozilla";
                browserType = BrowserType.MOZILLA;

                /* old mozilla */
                if (RE_NS_LANG_OS.match(agent)) {
                    lang = RE_NS_LANG_OS.getParen(1);
                    os = RE_NS_LANG_OS.getParen(2);
                    osVersion = RE_NS_LANG_OS.getParen(3);

                    if (os.equals("X")) {
                        if (RE_NS_X11_LANG_OS.match(agent)) {
                            os = RE_NS_X11_LANG_OS.getParen(1);
                            osVersion = RE_NS_X11_LANG_OS.getParen(2);
                            osType = OSType.UNIX;
                        }
                    }
                }
                /* NS5, NS6 Galeon etc. */
                else if (RE_GALEON_OS.match(agent)) {
                    browserName = "Galeon";
                    browserType = BrowserType.GECKO;
                    os = RE_GALEON_OS.getParen(1);
                    if (os.equals("X11")) {
                        os = "Unix";
                        osType = OSType.UNIX;
                    }
                    mav = RE_GALEON_OS.getParen(2);
                    miv = RE_GALEON_OS.getParen(3);
                } else if (RE_NS6_LANG_OS.match(agent)) {
                    os = RE_NS6_LANG_OS.getParen(2);
                    lang = RE_NS6_LANG_OS.getParen(5);
                }
                /* realy seldom but is there */
                else if (RE_MSIE_WIN_LANG_OS.match(agent)) {
                    os = "Windows";
                    osType = OSType.WINDOWS;
                    osVersion = RE_MSIE_WIN_LANG_OS.getParen(2) +
                            (RE_MSIE_WIN_LANG_OS.getParen(3) == null ?
                            "" :
                            " " + RE_MSIE_WIN_LANG_OS.getParen(3));
                }
                /* Konqueror */
                else if (RE_KONQUEROR_OS.match(agent)) {
                    browserName = "Konqueror";
                    browserType = BrowserType.KONQUEROR;
                    StringTokenizer strtok = new StringTokenizer(RE_KONQUEROR_OS.getParen(1), ".");
                    mav = strtok.nextToken();
                    if (strtok.hasMoreTokens())
                        miv = strtok.nextToken();
                    if (strtok.hasMoreTokens())
                        release = strtok.nextToken();
                    os = RE_KONQUEROR_OS.getParen(2);
                }
                /* f*ck, what's that ??? */
                else {
                    browserName = "Mozilla";
                    browserType = BrowserType.MOZILLA;
                }
                
                /* reformat browser os */
                if (os != null && os.startsWith("Win") &&
                        (osVersion == null || osVersion.length() == 0)
                ) {
                    osVersion = os.substring(3, os.length());
                    os = "Windows";
                    osType = OSType.WINDOWS;
                }
                /* just any windows */
                if (os != null && os.equals("Win")) {
                    os = "Windows";
                    osType = OSType.WINDOWS;
                }
            }
            /* Opera identified as opera, that's easy! */
            else if (browserName.equals("Opera")) {
                browserType = BrowserType.OPERA;
                if (RE_MSIE_WIN_LANG_OS.match(agent)) {
                    os = "Windows";
                    osType = OSType.WINDOWS;
                    osVersion = RE_MSIE_WIN_LANG_OS.getParen(2) +
                            (RE_MSIE_WIN_LANG_OS.getParen(3) == null ?
                            "" :
                            " " + RE_MSIE_WIN_LANG_OS.getParen(3));
                } else if (RE_OPERA_LANG_OS.match(agent)) {
                    os = RE_OPERA_LANG_OS.getParen(1);
                    osVersion = RE_OPERA_LANG_OS.getParen(2);
                    lang = RE_OPERA_LANG_OS.getParen(3);
                }
            }
            
            /* Opera identified as something else (Mozilla, IE ...) */
            if (RE_OPERA.match(agent)) {
                browserName = "Opera";
                browserType = BrowserType.OPERA;
                os = RE_OPERA.getParen(3);
                osVersion = RE_OPERA.getParen(4);
                mav = RE_OPERA.getParen(7);
                miv = RE_OPERA.getParen(8);
                lang = RE_OPERA.getParen(10);
            }
            
            /* Opera 8.xx identified as something else (Mozilla, IE ...) */
            if (RE_OPERA8X_CLOAKED.match(agent)){
                browserName = "Opera";
                browserType = BrowserType.OPERA;   
                os = RE_OPERA8X_CLOAKED.getParen(3);
                osVersion = RE_OPERA8X_CLOAKED.getParen(4);
                mav = RE_OPERA8X_CLOAKED.getParen(7);
                miv = RE_OPERA8X_CLOAKED.getParen(8);
                lang = RE_OPERA8X_CLOAKED.getParen(6);
            }

            /* detect gecko */
            if (RE_GECKO_ENGINE.match(agent)) {
                browserType = BrowserType.GECKO;
                if (RE_GECKO_ENGINE.getParen(2) != null)
                    browserName = RE_GECKO_ENGINE.getParen(2);
                if (RE_GECKO_ENGINE.getParen(3) != null)
                    mav = RE_GECKO_ENGINE.getParen(3);
                if (RE_GECKO_ENGINE.getParen(4) != null)
                    miv = RE_GECKO_ENGINE.getParen(4);
                if (RE_GECKO_ENGINE.getParen(5) != null)
                    release = RE_GECKO_ENGINE.getParen(5);
            }
            
            /* try to find language in uncommon places if not detected before */
            if (lang == null) {
                if (RE_LANG.match(agent)) {
                    lang = RE_LANG.getParen(1);
                }
            }

            try {
                majorVersion = new Integer(mav).intValue();
            } catch (NumberFormatException ex) {
                majorVersion = 0;
            }

            try {
                minorVersion = new Double("0." + miv).doubleValue();
            } catch (NumberFormatException ex) {
                minorVersion = 0f;
            }

            if (lang == null)
                browserLocale = Locale.getDefault();
            else {
                /* Mozilla does that, maybe any other browser too ? */
                lang = lang.replace('-', '_');

                /* test for country extension */
                StringTokenizer strtok = new StringTokenizer(lang, "_");
                String l = strtok.nextToken();
                if (strtok.hasMoreElements())
                    browserLocale = new Locale(l, strtok.nextToken());
                else
                    browserLocale = new Locale(l, "");
            }

            if (osType == OSType.UNKNOWN && os != null) {
                if (os.equals("Windows"))
                    osType = OSType.WINDOWS;
                else if (os.equals("MacOS"))
                    osType = OSType.MACOS;
                else if (
                        os.equals("Linux") ||
                        os.equals("AIX") ||
                        os.equals("SunOS") ||
                        os.equals("HP-UX") ||
                        os.equals("Solaris") ||
                        os.equals("BSD")
                ) {
                    osType = OSType.UNIX;
                } else if (os.equals("os")) {
                    osType = OSType.IBMOS;
                }
            }
        }
    }

    /**
     * @return true if browser supports the following notation for CSS selectors: <code>DIV > P</code>
     */
    public boolean supportsCssChildSelector() {
        return browserType != BrowserType.IE;
    }

    /**
     * just for testing ...
     */
    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                System.err.println("Usage: java " + new Browser("").getClass().getName() + " <agents file>");
                return;
            }
            FileReader fi = new FileReader(args[0]);
            LineNumberReader lnr = new LineNumberReader(fi);
            String line;
            while ((line = lnr.readLine()) != null) {
                System.out.println(line);
                System.out.println("\t" + new Browser(line).toString());
            }
            fi.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get a full human readable representation of the browser.
     */
    public String toString() {
        return browserName + " v" + (majorVersion + minorVersion) + (release == null ? "" : "-" + release) +
                "["+browserType+"], " + browserLocale + ", " + osType.getName() + ": " + os + " " + osVersion;
    }


}
