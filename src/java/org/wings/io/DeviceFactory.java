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
package org.wings.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.externalizer.ExternalizedResource;
import org.wings.session.SessionManager;

import java.io.IOException;

public abstract class DeviceFactory {
    private final transient static Log log = LogFactory.getLog(DeviceFactory.class);

    private static String DEFAULT_DEVICE_FACTORY = "org.wings.io.DeviceFactory$Default";

    private static DeviceFactory factory;

    public static void setDeviceFactory(DeviceFactory factory) {
        DeviceFactory.factory = factory;
    }

    public static DeviceFactory getDeviceFactory() {
        if (factory == null) {
            synchronized (DeviceFactory.class) {
                if (factory == null) {
                    String className = (String) SessionManager.getSession().getProperty("wings.device.factory");
                    if (className == null)
                        className = DEFAULT_DEVICE_FACTORY;

                    try {
                        Class factoryClass = null;
                        try {
                            factoryClass = Class.forName(className, true,
                                    Thread.currentThread()
                                    .getContextClassLoader());
                        } catch (ClassNotFoundException e) {
                            // fallback, in case the servlet container fails to set the
                            // context class loader.
                            factoryClass = Class.forName(className);
                        }
                        factory = (DeviceFactory) factoryClass.newInstance();
                    } catch (Exception e) {
                        log.fatal("could not load wings.device.factory: " +
                                className, e);
                        throw new RuntimeException("could not load wings.device.factory: " +
                                className + "(" + e.getMessage() + ")");
                    }
                }
            }
        }
        return factory;
    }

    public static Device createDevice(ExternalizedResource externalizedResource)
            throws IOException {
        return getDeviceFactory().create(externalizedResource);
    }

    protected abstract Device create(ExternalizedResource externalizedResource) throws IOException;

    static class Default
            extends DeviceFactory {
        protected Device create(ExternalizedResource externalizedResource)
                throws IOException {
            return new ServletDevice(SessionManager.getSession().getServletResponse().getOutputStream());
        }
    }
}
