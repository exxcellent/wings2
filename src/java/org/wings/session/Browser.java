/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.session;

import org.apache.regexp.*;
import java.io.*;
import java.util.Locale;
import java.util.StringTokenizer;


/**
 * Detect the browser from the user-agent string.
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 * @version $Revision$
 */
public class Browser
{
    /**
      * Operating system information could not be found.
      */
    public static final int OS_UNKNOWN = 0;
    
    /**
      * Browser os is of type Unix.
      */
    public static final int UNIX = 1;
    
    /**
      * Browser os is of type Windows.
      */
    public static final int WINDOWS = 2;
    
    /**
      * Browser os is of type MacOS
      */
    public static final int MACOS = 3;

    /**
      * Browser os is of type IBM-OS.
      * <br>f.e. OS/2
      */
    public static final int IBMOS = 4;

    protected String    agent;

    private String  Name;
    private int     MajorVersion;
    private double  MinorVersion;
    private String  Release;
    private String	OS;
    private int     OSType = OS_UNKNOWN;
    private String  OSVersion;
    private Locale  BLocale;
    private boolean	HasGecko = false;

    /**
      * Create a new browser object and start scanning for
      * browser, os and client language in given string.
      * @param agent the "User-Agent" string from the request.
      */
    public Browser(String agent)
        throws org.apache.regexp.RESyntaxException
    {
        this.agent = agent;
        detect();
    }
    
    /**
      * Get the browser name (Mozilla, MSIE, Opera etc.).
      */
    public String getBrowserName()
    {
        return Name;
    }
    
    /**
      * Get the browser major version.
      * <br>f.e. the major version for <i>Netscape 6.2</i> is <i>6</i>.
      * @return the major version or <i>0</i> if not found
      */
    public int getMajorVersion()
    {
        return MajorVersion;
    }
    
    /**
      * Get the minor version. This is the number after the
      * dot in the version string.
      * <br>f.e. the minor version for <i>Netscape 6.01</i> is <i>0.01</i>.
      * @return the minor version if found, <i>0</i> otherwise
      */
    public double getMinorVersion()
    {
        return MinorVersion;
    }
    
    /**
      * Get additional information about browser version.
      * <br>f.e. the release for <i>MSIE 6.1b</i> is <i>b</i>.
      * @return the release or <i>null</i>, if not available.
      */
    public String getRelease()
    {
        return Release;
    }
    
    /**
      * Get the operating system name.
      * @return the os name or <i>null</i>, if not available.
      */
    public String getOS()
    {
        return OS;
    }
    
    /**
      * Get the operating system version.
      * @return the os version or <i>null</i>, if not available.
      */
    public String getOSVersion()
    {
        return OSVersion;
    }


    /**
      * Get the operating system type.
      * @return one of 
      * 	<li>{@link #OS_UNKNOWN}</li>
      * 	<li>{@link #UNIX}</li>
      * 	<li>{@link #WINDOWS}</li>
      * 	<li>{@link #MACOS}</i>
      * 	<li>{@link #IBMOS}</i>
      */
    public int getOSType()
    {
        return OSType;
    }

    /**
      * Get the browser/client locale.
      * @return the found locale or the default server locale
      *     specified by {@link Locale#getDefault} if not found.
      */
    public Locale getClientLocale()
    {
        return BLocale;
    }

	/**
	 * Get if the browser uses netscape gecko
	 * rendering engine.
	 * @return true, if it is a netscape6/mozilla clone,
	 *  false otherwise.
	 */
	public boolean hasGecko()
	{
	    return HasGecko;
	}

    /**
      * That does all the work.
      * ToDo: use pre-compiled regexps in final stage.
      */
    protected void detect()
        throws org.apache.regexp.RESyntaxException
    {
	if (agent == null || agent.length() == 0)
	    return;

        /* regexps are not threadsafe, we have to create them. */
        RE Start = new RE("^([a-zA-Z0-9_\\-]+)(/([0-9])\\.([0-9]+))?");
        RE MSIE = new RE("MSIE ([0-9])\\.([0-9]+)([a-z])?");
        RE MSIE_Win_Lang_Os = new RE("[wW]in(dows)? ([A-Z0-9]+) ?([0-9]\\.[0-9])?");
        RE MSIE_Mac_Lang_Os = new RE("Mac_PowerPC");
        RE NS_Lang_Os = new RE("\\[([a-z-]+)\\][ a-zA-Z0-9-]*\\(([a-zA-Z\\-]+)/?([0-9]* ?[.a-zA-Z0-9 ]*);");
        RE NS_X11_Lang_Os = new RE("\\(X11; U; ([a-zA-Z-]+) ([0-9\\.]+)[^\\);]+\\)");
        RE NS6_Lang_Os = new RE("\\(([a-zA-Z0-9]+); [a-zA-Z]+; ([a-zA-Z0-9_]+)( ([a-zA-Z0-9]+))?; ([_a-zA-Z-]+);");
        RE Lang = new RE("\\[([_a-zA-Z-]+)\\]");
        RE Opera = new RE("((; )|\\()([a-zA-Z0-9\\-]+)[ ]+([a-zA-Z0-9\\.]+)([^;\\)]*)(; U)?\\) Opera ([0-9]+)\\.([0-9]+)[ ]+\\[([_a-zA-Z-]+)\\]");
        RE Opera_Lang_Os = new RE("\\(([a-zA-Z0-9\\-]+) ([0-9\\.]+)[^)]+\\)[ \t]*\\[([a-z_]+)\\]");
        RE Konqueror_Os = new RE("Konqueror/([0-9\\.]+); ([a-zA-Z0-9\\-]+)");
        RE Galeon_Os = new RE("\\(([a-zA-Z0-9]+); U; Galeon; ([0-9]+)\\.([0-9]+);");
        RE Gecko_Engine = new RE("Gecko/[0-9]*( ([a-zA-Z]+)+[0-9]*/([0-9]+)\\.([0-9]+)([a-zA-Z0-9]*))?$");
        
        String mav, miv, lang = null;
        
        if (Start.match(agent))
        {
            Name = Start.getParen(1);
            mav = Start.getParen(3);
            miv = Start.getParen(4);
            
            /* MSIE hides itself behind Mozilla or different name,
               good idea, congratulation Bill !
            */
            if (MSIE.match(agent))
            {
                Name = "MSIE";
                mav = MSIE.getParen(1);
                miv = MSIE.getParen(2);
                Release = MSIE.getParen(3);

                if (MSIE_Win_Lang_Os.match(agent))
                {
                    OS = "Windows";
                    OSVersion= MSIE_Win_Lang_Os.getParen(2) +
                        (MSIE_Win_Lang_Os.getParen(3) == null?
                            "":
                            " " + MSIE_Win_Lang_Os.getParen(3));
                }
                else if (MSIE_Mac_Lang_Os.match(agent))
                {
                    OS = "MacOS";
                    OSType = MACOS;
                }
            }
            /* Mozilla has to different id's; one up to version 4
               and a second for version >= 5
            */
            else if (Name.equals("Mozilla") || Name == null)
            {
                Name = "Mozilla";
                
                /* old mozilla */
                if (NS_Lang_Os.match(agent))
                {
                    lang = NS_Lang_Os.getParen(1);
                    OS = NS_Lang_Os.getParen(2);
                    OSVersion = NS_Lang_Os.getParen(3);

                    if (OS.equals("X"))
                    {
                        if (NS_X11_Lang_Os.match(agent))
                        {
                            OS = NS_X11_Lang_Os.getParen(1);
                            OSVersion = NS_X11_Lang_Os.getParen(2);
                            OSType = UNIX;
                        }
                    }
                }
                /* NS5, NS6 Galeon etc. */
                else if (Galeon_Os.match(agent))
                {
                    Name = "Galeon";
                    OS = Galeon_Os.getParen(1);
                    if (OS.equals("X11"))
                    {
                        OS = "Unix";
                        OSType = UNIX;
                    }
                    mav = Galeon_Os.getParen(2);
                    miv = Galeon_Os.getParen(3);
                    HasGecko = true;
                }
                else if (NS6_Lang_Os.match(agent))
                {
                    OS = NS6_Lang_Os.getParen(2);
                    lang = NS6_Lang_Os.getParen(5);
                    HasGecko = true;
                }
                /* realy seldom but is there */
                else if (MSIE_Win_Lang_Os.match(agent))
                {
                    OS = "Windows";
                    OSType = WINDOWS;
                    OSVersion= MSIE_Win_Lang_Os.getParen(2) + 
                        (MSIE_Win_Lang_Os.getParen(3) == null?
                            "":
                            " " + MSIE_Win_Lang_Os.getParen(3));
                }
                /* Konqueror */
                else if (Konqueror_Os.match(agent))
                {
                    Name = "Konqueror";
                    StringTokenizer strtok = new StringTokenizer(Konqueror_Os.getParen(1), ".");
                    mav = strtok.nextToken();
                    if (strtok.hasMoreTokens())
                        miv = strtok.nextToken();
                    if (strtok.hasMoreTokens())
                        Release = strtok.nextToken();
                    OS = Konqueror_Os.getParen(2);
                }
                /* f*ck, what's that ??? */
                else
                {
                    Name = "Mozilla";
                }
                
                /* reformat browser os */
                if (OS != null && OS.startsWith("Win") &&
                    (OSVersion == null || OSVersion.length() == 0)
                    )
                {
                    OSVersion = OS.substring(3,OS.length());
                    OS = "Windows";
                    OSType = WINDOWS;
                }
                /* just any windows */
                if (OS != null && OS.equals("Win"))
                {
                    OS = "Windows";
                    OSType = WINDOWS;
                }
            }
            /* Opera identified as opera, that's easy! */
            else if (Name.equals("Opera"))
            {
                if (MSIE_Win_Lang_Os.match(agent))
                {
                    OS = "Windows";
                    OSType = WINDOWS;
                    OSVersion= MSIE_Win_Lang_Os.getParen(2) +
                            (MSIE_Win_Lang_Os.getParen(3) == null?
                                "":
                                " " + MSIE_Win_Lang_Os.getParen(3));
                }
                else
                if (Opera_Lang_Os.match(agent))
                {
                    OS = Opera_Lang_Os.getParen(1);
                    OSVersion = Opera_Lang_Os.getParen(2);
                    lang = Opera_Lang_Os.getParen(3);
                }
            }
            
            /* Opera identified as something else (Mozilla, IE ...) */
            if (Opera.match(agent))
            {
                Name = "Opera";
                OS = Opera.getParen(3);
                OSVersion = Opera.getParen(4);
                mav = Opera.getParen(7);
                miv = Opera.getParen(8);
                lang = Opera.getParen(10);
            }
            
            /* detect gecko */
            if (Gecko_Engine.match(agent))
            {
                HasGecko = true;
                if (Gecko_Engine.getParen(2) != null)
                	Name = Gecko_Engine.getParen(2);
                if (Gecko_Engine.getParen(3) != null)
                	mav = Gecko_Engine.getParen(3);
                if (Gecko_Engine.getParen(4) != null)
                	miv = Gecko_Engine.getParen(4);
                if (Gecko_Engine.getParen(5) != null)
                	Release = Gecko_Engine.getParen(5);
            }
            
            /* try to find language in uncommon places 
               if not detected before
            */
            if (lang == null)
            {
                if (Lang.match(agent))
                {
                    lang = Lang.getParen(1);
                }
            }
            
            try { MajorVersion = new Integer(mav).intValue(); }
            catch (NumberFormatException ex) { MajorVersion=0; }
            
            try { MinorVersion = new Double("0."+miv).doubleValue(); }
            catch (NumberFormatException ex) { MinorVersion=0f; }
            
            if (lang == null)
                BLocale = Locale.getDefault();
            else
            {
                /* Mozilla does that, maybe any other browser too ? */
                lang = lang.replace('-', '_');

                /* test for country extension */
                StringTokenizer strtok = new StringTokenizer(lang, "_");
                String l = strtok.nextToken();
                if (strtok.hasMoreElements())
                    BLocale = new Locale(l, strtok.nextToken());
                else
                    BLocale = new Locale(l, "");
            }
            
            if (OSType == OS_UNKNOWN && OS != null)
            {
                if (OS.equals("Windows"))
                    OSType = WINDOWS;
                else if (OS.equals("MacOS"))
                    OSType = MACOS;
                else if (
                    OS.equals("Linux") || 
                    OS.equals("AIX") ||
                    OS.equals("SunOS") || 
                    OS.equals("HP-UX") ||
                    OS.equals("Solaris") ||
                    OS.equals("BSD")
                    )
                {
                    OSType = UNIX;
                }
                else if (OS.equals("OS"))
                {
                    OSType = IBMOS;
                }
            }
        }
    }

    public boolean supportsChildSelector() {
        return !"MSIE".equals(Name);
    }

    /**
      * just for testing ...
      */
    public static void main(String[] args)
    {
        try
        {
	        if (args.length != 1)
	        {
	            System.err.println("Usage: java "+new Browser("").getClass().getName()+" <agents file>");
	            return;
	        }
            FileReader fi = new FileReader(args[0]);
            LineNumberReader lnr = new LineNumberReader(fi);
            String line;
            while ((line = lnr.readLine()) != null)
            {
                System.out.println(line);
                System.out.println("\t"+ new Browser(line).toString());
            }
            fi.close();
        }

        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
      * Get a full human readable representation of
      * the browser.
      */
    public String toString()
    {
        String t = "";
        switch (OSType)
        {
            case UNIX:
                t = "Unix"; break;
            case WINDOWS:
                t = "Windows"; break;
            case MACOS:
                t = "MacOS"; break;
            case IBMOS:
                t = "IBM OS"; break;
            default:
                t = "Unknown os"; break;
        }
        return Name + " v" + (MajorVersion+MinorVersion) + (Release==null?"":"-"+Release) + 
            ", " + BLocale + 
            ", " + t + ": " + OS + " " + OSVersion + 
            (HasGecko?" + Gecko-Engine":"");
    }
}
