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
package org.wings.plaf.css;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.session.Browser;
import org.wings.session.BrowserType;
import org.wings.util.SStringBuilder;
import org.wings.session.SessionManager;
import org.wings.util.PropertyUtils;

import java.io.IOException;
import java.util.Properties;

public class CSSLookAndFeel
        extends org.wings.plaf.LookAndFeel
{
    private static final long serialVersionUID = 1L;
    private final transient static Log log = LogFactory.getLog(CSSLookAndFeel.class);
    private static final String PROPERTIES_FILENAME_DEFAULT = "default";
    private static final String PROPERTIES_FILENAME_XCOMPONENT = "xcomponents";
    private static final String PROPERTIES_FILENAME_END = ".properties";
    private static final String PROPERTIES_CLASSPATH = CSSLookAndFeel.class.getPackage().getName().replace('.','/').concat("/");
    private static final String PROPERTIES_CLASSPATH_XCOMPONENT = "org/wingx/plaf/css/";

    public CSSLookAndFeel() throws IOException {
        super(loadProperties());
    }

    private static Properties loadProperties() throws IOException {
        
        // default properties
        SStringBuilder propertiesFilename = new SStringBuilder(PROPERTIES_CLASSPATH);
        propertiesFilename.append(PROPERTIES_FILENAME_DEFAULT);
        propertiesFilename.append(PROPERTIES_FILENAME_END);                        
        
        Properties properties = PropertyUtils.loadProperties(propertiesFilename.toString());
                
        SStringBuilder xcomponentsPropertiesFilename = new SStringBuilder(PROPERTIES_CLASSPATH_XCOMPONENT);
        xcomponentsPropertiesFilename
                .append(PROPERTIES_FILENAME_XCOMPONENT)
                .append(PROPERTIES_FILENAME_END);                        
        
        try {
            properties.putAll(PropertyUtils.loadProperties(xcomponentsPropertiesFilename.toString()));
            log.debug(xcomponentsPropertiesFilename.toString()+" attached");
        } catch (IOException e) {
            log.info("Unable to open xcomponents specific properties file '"+ xcomponentsPropertiesFilename.toString()+"'. This is OK if you are not using wingX.");
        }
                
        // browser dependent properties
        Browser userAgent = SessionManager.getSession().getUserAgent();
        String browserType = userAgent.getBrowserType().getShortName();

        SStringBuilder browserPropertiesFilename = new SStringBuilder(PROPERTIES_CLASSPATH);
        browserPropertiesFilename.append(browserType);
        browserPropertiesFilename.append(PROPERTIES_FILENAME_END);
                            
        try {
            properties.putAll(PropertyUtils.loadProperties(browserPropertiesFilename.toString()));
        } catch (IOException e) {
            log.info("Unable to open browser specific properties file '"+browserPropertiesFilename.toString()+"'. This is OK.");
        }
        
        SStringBuilder browserXcomponentsPropertiesFilename = new SStringBuilder(PROPERTIES_CLASSPATH_XCOMPONENT);
            browserXcomponentsPropertiesFilename
                .append(PROPERTIES_FILENAME_XCOMPONENT)
                .append("_").append(browserType)
                .append(PROPERTIES_FILENAME_END);        
        
        try {
            properties.putAll(PropertyUtils.loadProperties(browserXcomponentsPropertiesFilename.toString()));                                
        } catch (IOException e) {
            log.info("Unable to open xcomponents specific properties file '"+ browserXcomponentsPropertiesFilename.toString()+"'. This is OK if you are not using wingX.");                
        }
                                        
        // special properties for IE 7    
        if ((userAgent.getBrowserType().getId() == BrowserType.IE.getId()) &&
            (userAgent.getMajorVersion() == 7)) {                                 

            log.info("xcomponents cgs for ie 7 will be loaded");
            SStringBuilder ie7Properties = new SStringBuilder(PROPERTIES_CLASSPATH_XCOMPONENT);
            ie7Properties
                    .append(PROPERTIES_FILENAME_XCOMPONENT)
                    .append("_").append(browserType).append("7")
                    .append(PROPERTIES_FILENAME_END);
            
            try {
                properties.putAll(PropertyUtils.loadProperties(ie7Properties.toString()));                                
            } catch (IOException e) {
                log.info("Unable to open xcomponents specific properties file '"+ ie7Properties.toString()+"'.");                
            }
            
        }        
                
        return properties;
    }
}


