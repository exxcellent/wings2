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
import org.wings.session.SessionManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Some service methods related with handling {@link java.util.Properties} in wingS.
 *
 * @author bschmid
 */
public class PropertyUtils {
    private final static Log log = LogFactory.getLog(PropertyUtils.class);
    private static final String WEB_INF = "WEB-INF/";

    /**
     * Loads properties from a file. First looks in the classPath for the
     * passed default file (i.e. org/wings/myprops.properties). Throws an
     * exception if that is not found. Then looks for an overriding file in
     * the webapp container with a file name including the package name
     * (i.e. WEB-INF/org.wings.myprops.properties) and loads/overwrites with
     * these additional settings.
     * If no file in the WEB-INF path is found, log this info.
     *
     * @param propertiesFilename the name of the properties file.
     * @throws java.io.IOException if default (in classPath) is not found.
     * @return The Properties loaded
     */
    public static Properties loadProperties(String propertiesFilename) throws IOException {
        Properties properties;
        // first load defaults from classpath, and if it fails, throw Exception
        properties = loadPropertiesFromClasspath(propertiesFilename);
        // now load from webapp folder, log if fails.
        String webappUrl = WEB_INF + propertiesFilename.replace('/','.');
        properties.putAll(loadPropertiesFromContainer(webappUrl));
        return properties;
    }

    /**
     * Loads a file from the webapp's dir into a properties file.
     * @param webappUrl the file's url
     * @return The Properties loaded
     */
    public static Properties loadPropertiesFromContainer(String webappUrl) {
        Properties properties = new Properties();
        InputStream in;
        try {
            in = SessionManager.getSession().getServletContext().getResourceAsStream(webappUrl);
            properties.load(in);
            in.close();
        } catch (Exception e) {
            final String warn = "Unable to open " + webappUrl + ". It seems you didn't provide a custom configuration. Using defaults.";
            log.info(warn);
        }
        return properties;
    }

    /**
     * Loads a file from the webapp's classpath into a properties file.
     * @param classPath the file's classpath
     * @return The Properties loaded
     * @throws IOException
     */
    public static Properties loadPropertiesFromClasspath(final String classPath) throws IOException {
        Properties properties = new Properties();
        InputStream in;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(classPath);
            properties.load(in);
            in.close();
        } catch (Exception e) {
            final String error = "Unable to open " + classPath + " from classpath due "+e+". Please check deployment!";
            throw new IOException(error);
        }
        return properties;
    }


}
