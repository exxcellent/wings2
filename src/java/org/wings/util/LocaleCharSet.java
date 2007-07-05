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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Locale;
import java.util.Properties;

/**
 * Map {@link java.util.Locale} to html/iso character set via <code>org/wings/util/charset.properties</code>.
 *
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 */
public class LocaleCharSet {
    /**
     * The default character encoding "UTF-8".
     */
    public final static String DEFAULT_ENCODING = "UTF-8";

    private final static String CHARSET_PROPERTIES = "org/wings/util/charset.properties";
    private final static Log log = LogFactory.getLog(LocaleCharSet.class);
    private Properties charsetMap;
    private static LocaleCharSet instance = null;

    protected LocaleCharSet() {
        try {
            charsetMap = PropertyUtils.loadProperties(CHARSET_PROPERTIES);
        } catch (Exception e) {
            log.warn("Unexpected error on loading " + CHARSET_PROPERTIES + " via class path.", e);
            charsetMap = new Properties();
        }
    }

    /**
     * Get a instance of LocaleCharSet.
     *
     * @return Instance of LocaleCharset
     */
    public static LocaleCharSet getInstance() {
        if (instance == null) {
            instance = new LocaleCharSet();
        }
        return instance;
    }

    /**
     * Try to find a matching character set for this locale.
     *
     * @param aLocale The Locale to retrieve the default charset for.
     * @return if found the charset, DEFAULT_ENCODING otherwise
     */
    public String getCharSet(Locale aLocale) {
        String charset = null;
        if (aLocale == null) {
            return DEFAULT_ENCODING;
        }

        charset = charsetMap.getProperty(aLocale.getCountry() + "_" + aLocale.getLanguage());

        if (charset == null) {
            charset = charsetMap.getProperty(aLocale.getCountry());
        }

        if (charset == null) {
            charset = charsetMap.getProperty(aLocale.getLanguage());
        }

        return charset != null ? charset : DEFAULT_ENCODING;
    }
}