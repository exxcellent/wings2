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
package org.wings.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Map {@link java.util.Locale} to html/iso character set via <code>org/wings/util/charset.properties</code>.
 *
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 */
public class LocaleCharSet {
    /** The default character encoding "ISO-8859-1". */
    public final static String DEFAULT_ENCODING = "ISO-8859-1";
    private static LocaleCharSet fInstance = null;
    private static Logger logger = Logger.getLogger("org.wings.util");
    private Properties fCharSet;

    protected LocaleCharSet() {
        fCharSet = new Properties();
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("org/wings/util/charset.properties");
            fCharSet.load(in);
            in.close();
        }
        catch (IOException e) {
            logger.log(Level.WARNING, "Unexpected error on loading org/wings/util/charset.properties via CP.", e);
        } 
    }

    /**
     * Get a instance of LocaleCharSet.
     *
     * @return Instance of LocaleCharset
     */
    public static LocaleCharSet getInstance() {
        if (fInstance == null) {
            fInstance = new LocaleCharSet();
        }
        return fInstance;
    }

    /**
     * Try to find a matching character set for this locale.
     *
     * @param aLocale The Locale to retrieve the default charset for.
     *
     * @return if found the charset, DEFAULT_ENCODING otherwise
     */
    public String getCharSet(Locale aLocale) {
        String cs = null;
        if (aLocale == null) {
            return DEFAULT_ENCODING;
        }

        cs = fCharSet.getProperty(aLocale.getCountry() + "_" + aLocale.getLanguage());

        if (cs == null) {
            cs = fCharSet.getProperty(aLocale.getCountry());
        }

        if (cs == null) {
            cs = fCharSet.getProperty(aLocale.getLanguage());
        }

        return cs != null ? cs : DEFAULT_ENCODING;
    }
}