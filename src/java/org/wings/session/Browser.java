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

import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.Serializable;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


/**
 * Detect the browser from the user-agent string passed in the HTTP header.
 *
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
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
     *
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
    private transient Pattern RE_START;
    private transient Pattern RE_MSIE;
    private transient Pattern RE_MSIE_WIN_LANG_OS;
    private transient Pattern RE_MSIE_MAC_LANG_OS;
    private transient Pattern RE_NS_LANG_OS;
    private transient Pattern RE_NS_X11_LANG_OS;
    private transient Pattern RE_NS6_LANG_OS;
    private transient Pattern RE_LANG;
    private transient Pattern RE_LANG2;
    private transient Pattern RE_OPERA;
    private transient Pattern RE_OPERA8X_CLOAKED;
    private transient Pattern RE_OPERA_LANG_OS;
    private transient Pattern RE_KONQUEROR_OS;
    private transient Pattern RE_GALEON_OS;
    private transient Pattern RE_GECKO_ENGINE;

    /**
     * Lazily create RE as they are not serializable. Will be null after session restore.
     */
    private void createREs() {
        if (RE_START == null) {
            RE_START = Pattern.compile("^([a-zA-Z0-9_\\-]+)(/([0-9])\\.([0-9]+))?");
            RE_MSIE = Pattern.compile("MSIE\\s([0-9])\\.([0-9]+)([a-z])?");
            RE_MSIE_WIN_LANG_OS = Pattern.compile("[wW]in(dows)?\\s([A-Z0-9]+)\\s?([0-9]\\.[0-9])?(;\\s[UIN])?(;\\s(\\w{2}[_-]?\\w{0,2}))");
            RE_MSIE_MAC_LANG_OS = Pattern.compile("Mac_PowerPC");
            RE_NS_LANG_OS = Pattern.compile("\\[([a-z-]+)\\][\\sa-zA-Z0-9-]*\\(([a-zA-Z\\-]+)/?([0-9]*\\s?[.a-zA-Z0-9\\s]*);");
            RE_NS_X11_LANG_OS = Pattern.compile("\\(X11;\\s[UIN];\\s([a-zA-Z-]+)\\s([0-9\\.]+)[^\\);]+\\)");
            RE_NS6_LANG_OS = Pattern.compile("\\(([a-zA-Z0-9]+);\\s[a-zA-Z]+;\\s([a-zA-Z0-9_]+)(\\s([a-zA-Z0-9]+))?;\\s([_a-zA-Z-]+);");
            RE_LANG = Pattern.compile("\\[([_a-zA-Z-]+)\\]");
            /* Search for any xx_XX or xx-XX */
            RE_LANG2 = Pattern.compile("\\b(\\D{2}[_-]\\D{2})\\b");
            RE_OPERA = Pattern.compile("((;\\s)|\\()([a-zA-Z0-9\\-]+)[\\s]+([a-zA-Z0-9\\.]+)([^;\\)]*)(;\\s\\w+)?\\)\\sOpera\\s([0-9]+)\\.([0-9]+)[\\s]+\\[([_a-zA-Z-]+)\\]");
            RE_OPERA8X_CLOAKED = Pattern.compile("((;\\s)|\\()([a-zA-Z0-9\\-]+)[\\s]+([a-zA-Z0-9\\.\\s]+)([^;\\)]*)(;\\s[UIN])?;\\s([_a-zA-Z-]+)\\)\\sOpera\\s([0-9]+)\\.([0-9]+)");
            RE_OPERA_LANG_OS = Pattern.compile("\\((\\w+;\\s)?([a-zA-Z0-9\\-]+)\\s([\\w\\.\\s]+)(;\\s[UIN])?(;\\s(\\w{2}[-_]?\\w{0,2}))");
            RE_KONQUEROR_OS = Pattern.compile("Konqueror/([rc0-9\\.-]+);\\s([a-zA-Z0-9\\-]+)");
            RE_GALEON_OS = Pattern.compile("\\(([a-zA-Z0-9]+);\\s[UIN];\\sGaleon;\\s([0-9]+)\\.([0-9]+);");
            RE_GECKO_ENGINE = Pattern.compile("Gecko/[0-9]*(\\s([a-zA-Z]+)+[0-9]*/([0-9]+)\\.([0-9]+)([a-zA-Z0-9]*))?");
        }
    }

    /**
     * That does all the work.
     */
    protected void detect() {
        if (agent == null || agent.length() == 0) {
            return;
        }
        String mav, miv, lang = null;

        createREs();

        final Matcher RE_START_MATCHER = RE_START.matcher(agent);
        if (RE_START_MATCHER.find()) {
            browserName = RE_START_MATCHER.group(1);
            mav = RE_START_MATCHER.group(3);
            miv = RE_START_MATCHER.group(4);

            /* RE_MSIE hides itself behind Mozilla or different browserName,
               good idea, congratulation Bill !
            */
            final Matcher RE_MSIE_MATCHER = RE_MSIE.matcher(agent);
            final Matcher RE_MSIE_WIN_LANG_OS_MATCHER = RE_MSIE_WIN_LANG_OS.matcher(agent);
            if (RE_MSIE_MATCHER.find()) {
                browserName = "MSIE";
                browserType = BrowserType.IE;
                mav = RE_MSIE_MATCHER.group(1);
                miv = RE_MSIE_MATCHER.group(2);
                release = RE_MSIE_MATCHER.group(3);

                if (RE_MSIE_WIN_LANG_OS_MATCHER.find()) {
                    osType = OSType.WINDOWS;
                    os = "Windows";
                    osVersion = RE_MSIE_WIN_LANG_OS_MATCHER.group(2) +
                            (RE_MSIE_WIN_LANG_OS_MATCHER.group(3) == null ?
                                    "" :
                                    " " + RE_MSIE_WIN_LANG_OS_MATCHER.group(3));
                } else {
                    final Matcher RE_MSIE_MAC_LANG_MATCHER = RE_MSIE_MAC_LANG_OS.matcher(agent);
                    if (RE_MSIE_MAC_LANG_MATCHER.find()) {
                        os = "MacOS";
                        osType = OSType.MACOS;
                    }
                }
            }
            /* Mozilla has two different id's; one up to version 4
               and a second for version >= 5
            */
            else if (browserName.equals("Mozilla") || browserName == null) {
                browserName = "Mozilla";
                browserType = BrowserType.MOZILLA;

                /* old mozilla */
                final Matcher RE_NS_LANG_OS_MATCHER = RE_NS_LANG_OS.matcher(agent);
                if (RE_NS_LANG_OS_MATCHER.find()) {
                    lang = RE_NS_LANG_OS_MATCHER.group(1);
                    os = RE_NS_LANG_OS_MATCHER.group(2);
                    osVersion = RE_NS_LANG_OS_MATCHER.group(3);

                    if (os.equals("X")) {
                        final Matcher RE_NS_X11_LANG_OS_MATCHER = RE_NS_X11_LANG_OS.matcher(agent);
                        if (RE_NS_X11_LANG_OS_MATCHER.find()) {
                            os = RE_NS_X11_LANG_OS_MATCHER.group(1);
                            osVersion = RE_NS_X11_LANG_OS_MATCHER.group(2);
                            osType = OSType.UNIX;
                        }
                    }
                }
                /* NS5, NS6 Galeon etc. */
                else {
                    final Matcher RE_GALEON_OS_MATCHER = RE_GALEON_OS.matcher(agent);
                    if (RE_GALEON_OS_MATCHER.find()) {
                        browserName = "Galeon";
                        browserType = BrowserType.GECKO;
                        os = RE_GALEON_OS_MATCHER.group(1);
                        if (os.equals("X11")) {
                            os = "Unix";
                            osType = OSType.UNIX;
                        }
                        mav = RE_GALEON_OS_MATCHER.group(2);
                        miv = RE_GALEON_OS_MATCHER.group(3);
                    } else {
                        final Matcher RE_NS6_LANG_OS_MATCHER = RE_NS6_LANG_OS.matcher(agent);
                        if (RE_NS6_LANG_OS_MATCHER.find()) {
                            os = RE_NS6_LANG_OS_MATCHER.group(2);
                            lang = RE_NS6_LANG_OS_MATCHER.group(5);
                        }
                        /* realy seldom but is there */
                        else {
                            if (RE_MSIE_WIN_LANG_OS_MATCHER.find()) {
                                os = "Windows";
                                osType = OSType.WINDOWS;
                                osVersion = RE_MSIE_WIN_LANG_OS_MATCHER.group(2) +
                                        (RE_MSIE_WIN_LANG_OS_MATCHER.group(3) == null ?
                                                "" :
                                                " " + RE_MSIE_WIN_LANG_OS_MATCHER.group(3));
                            }
                            /* Konqueror */
                            else {
                                final Matcher RE_KONQUEROR_OS_MATCHER = RE_KONQUEROR_OS.matcher(agent);
                                if (RE_KONQUEROR_OS_MATCHER.find()) {
                                    browserName = "Konqueror";
                                    browserType = BrowserType.KONQUEROR;
                                    StringTokenizer strtok = new StringTokenizer(RE_KONQUEROR_OS_MATCHER.group(1), ".");
                                    mav = strtok.nextToken();
                                    if (strtok.hasMoreTokens()) {
                                        miv = strtok.nextToken();
                                    }
                                    if (strtok.hasMoreTokens()) {
                                        release = strtok.nextToken();
                                    }
                                    os = RE_KONQUEROR_OS_MATCHER.group(2);
                                }
                                /* f*ck, what's that ??? */
                                else {
                                    browserName = "Mozilla";
                                    browserType = BrowserType.MOZILLA;
                                }
                            }
                        }
                    }
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
                if (RE_MSIE_WIN_LANG_OS_MATCHER.find()) {
                    os = "Windows";
                    osType = OSType.WINDOWS;
                    osVersion = RE_MSIE_WIN_LANG_OS_MATCHER.group(2) +
                            (RE_MSIE_WIN_LANG_OS_MATCHER.group(3) == null ?
                                    "" :
                                    " " + RE_MSIE_WIN_LANG_OS_MATCHER.group(3));
                    if(RE_MSIE_WIN_LANG_OS_MATCHER.group(6) != null)
                        lang = RE_MSIE_WIN_LANG_OS_MATCHER.group(6);
                } else {
                    final Matcher RE_OPERA_LANG_OS_MATCHER = RE_OPERA_LANG_OS.matcher(agent);
                    if (RE_OPERA_LANG_OS_MATCHER.find()) {
                        os = RE_OPERA_LANG_OS_MATCHER.group(2);
                        osVersion = RE_OPERA_LANG_OS_MATCHER.group(3);
                        lang = RE_OPERA_LANG_OS_MATCHER.group(6);
                    }
                }
            }

            /* Opera identified as something else (Mozilla, IE ...) */
            final Matcher RE_OPERA_MATCHER = RE_OPERA.matcher(agent);
            if (RE_OPERA_MATCHER.find()) {
                browserName = "Opera";
                browserType = BrowserType.OPERA;
                os = RE_OPERA_MATCHER.group(3);
                osVersion = RE_OPERA_MATCHER.group(4);
                mav = RE_OPERA_MATCHER.group(7);
                miv = RE_OPERA_MATCHER.group(8);
                lang = RE_OPERA_MATCHER.group(9);
            }

            /* Opera 8.xx identified as something else (Mozilla, IE ...) */
            final Matcher RE_OPERA8X_CLOAKED_MATCHER = RE_OPERA8X_CLOAKED.matcher(agent);
            if (RE_OPERA8X_CLOAKED_MATCHER.find()) {
                browserName = "Opera";
                browserType = BrowserType.OPERA;
                os = RE_OPERA8X_CLOAKED_MATCHER.group(3);
                osVersion = RE_OPERA8X_CLOAKED_MATCHER.group(4);
                mav = RE_OPERA8X_CLOAKED_MATCHER.group(8);
                miv = RE_OPERA8X_CLOAKED_MATCHER.group(9);
                lang = RE_OPERA8X_CLOAKED_MATCHER.group(7);
            }

            /* detect gecko */
            final Matcher RE_GECKO_ENGINE_MATCHER = RE_GECKO_ENGINE.matcher(agent);
            if (RE_GECKO_ENGINE_MATCHER.find()) {
                browserType = BrowserType.GECKO;
                if (RE_GECKO_ENGINE_MATCHER.group(2) != null) {
                    browserName = RE_GECKO_ENGINE_MATCHER.group(2);
                }
                if (RE_GECKO_ENGINE_MATCHER.group(3) != null) {
                    mav = RE_GECKO_ENGINE_MATCHER.group(3);
                }
                if (RE_GECKO_ENGINE_MATCHER.group(4) != null) {
                    miv = RE_GECKO_ENGINE_MATCHER.group(4);
                }
                if (RE_GECKO_ENGINE_MATCHER.group(5) != null) {
                    release = RE_GECKO_ENGINE_MATCHER.group(5);
                }
            }

            /* try to find language in uncommon places if not detected before */
            if (lang == null) {
                final Matcher RE_LANG_MATCHER = RE_LANG.matcher(agent);
                if (RE_LANG_MATCHER.find()) {
                    lang = RE_LANG_MATCHER.group(1);
                }
            }
            if (lang == null) {
                final Matcher RE_LANG2_MATCHER = RE_LANG2.matcher(agent);
                if (RE_LANG2_MATCHER.find()) {
                    lang = RE_LANG2_MATCHER.group(1);
                }
            }

            try {
                majorVersion = Integer.parseInt(mav);
            } catch (NumberFormatException ex) {
                majorVersion = 0;
            }

            try {
                minorVersion = Double.parseDouble("0." + miv);
            } catch (NumberFormatException ex) {
                minorVersion = 0f;
            }

            if (lang == null) {
                browserLocale = Locale.getDefault();
            } else {
                /* Mozilla does that, maybe any other browser too ? */
                lang = lang.replace('-', '_');

                /* test for country extension */
                StringTokenizer strtok = new StringTokenizer(lang, "_");
                String l = strtok.nextToken();
                if (strtok.hasMoreElements()) {
                    browserLocale = new Locale(l, strtok.nextToken());
                } else {
                    browserLocale = new Locale(l, "");
                }
            }

            if (osType == OSType.UNKNOWN && os != null) {
                if (os.equals("Windows")) {
                    osType = OSType.WINDOWS;
                } else if (os.equals("MacOS")) {
                    osType = OSType.MACOS;
                } else if (
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
     * just for testing ...
     */
    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                System.err.println("Usage: java " + Browser.class.getName() + " <agents file>");
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
                " browsertype:[" + browserType + "], locale:" + browserLocale + ", ostype:" + osType.getName() + ": os:" + os + " osv:" + osVersion;
    }


}
