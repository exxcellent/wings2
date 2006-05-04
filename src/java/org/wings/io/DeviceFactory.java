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
package org.wings.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.externalizer.ExternalizedResource;
import org.wings.session.SessionManager;

import java.io.IOException;

/**
 * The factory creating the output devies for externalized resources.
 * To declare and use your own device factory (i.e. to compress returned output streams or
 * log the device output) declare an init property <code>wings.device.factory</code>
 * in your web xml.
 * <p>Example:<br/>
 * <pre>        &lt;init-param&gt;
            &lt;param-name&gt;wings.device.factory&lt;/param-name&gt;
            &lt;param-value&gt;com.mycompany.MyDeviceFactory&lt;/param-value&gt;
        &lt;/init-param&gt;
</pre>
 */
public abstract class DeviceFactory {
    private final transient static Log log = LogFactory.getLog(DeviceFactory.class);

    private static String DEFAULT_DEVICE_FACTORY = "org.wings.io.DeviceFactory$Default";

    private static DeviceFactory factory;

    /**
     * Overrides the current device factory.
     */
    public static void setDeviceFactory(DeviceFactory factory) {
        DeviceFactory.factory = factory;
    }

    /**
     * Returns or lazily creates the current device factory. Use {@link #setDeviceFactory(DeviceFactory)} or
     * an <code>web.xml</code> init property <code>wings.device.factory</code> to declare an alternative deivce factory.
     * @return The current device factory.
     */
    public static DeviceFactory getDeviceFactory() {
        if (factory == null) {
            synchronized (DeviceFactory.class) {
                if (factory == null) {
                    String className = (String) SessionManager.getSession().getProperty("wings.device.factory");
                    if (className == null) {
                        className = DEFAULT_DEVICE_FACTORY;
                    }

                    try {
                        Class factoryClass = null;
                        try {
                            factoryClass = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
                        } catch (ClassNotFoundException e) {
                            // fallback, in case the servlet container fails to set the
                            // context class loader.
                            factoryClass = Class.forName(className);
                        }
                        factory = (DeviceFactory) factoryClass.newInstance();
                    } catch (Exception e) {
                        log.fatal("could not load wings.device.factory: " + className, e);
                        throw new RuntimeException("could not load wings.device.factory: " +
                                className + "(" + e.getMessage() + ")");
                    }
                }
            }
        }
        return factory;
    }

    /**
     * Creates a output device for the passed resource using the current device factory.
     * @param externalizedResource The resource to ouput.
     */
    public static Device createDevice(ExternalizedResource externalizedResource)  throws IOException {
        return getDeviceFactory().create(externalizedResource);
    }

    protected abstract Device create(ExternalizedResource externalizedResource) throws IOException;

    /**
     * Default device factory.
     */
    static class Default extends DeviceFactory {
        protected Device create(ExternalizedResource externalizedResource) throws IOException {
            //return new ServletDevice(SessionManager.getSession().getServletResponse().getOutputStream());
            if (externalizedResource.getExtension().equalsIgnoreCase("html"))
              return new CachingServletDevice(new ServletDevice(SessionManager.getSession().getServletResponse().getOutputStream()));
             //      return new LazyDevice(new ServletDevice(SessionManager.getSession().getServletResponse().getOutputStream()));
            else
                return new ServletDevice(SessionManager.getSession().getServletResponse().getOutputStream());
        }
    }

    /*static class LazyDevice implements Device {
        private static final Log log = LogFactory.getLog(DeviceFactory.class);
        private final Device finalDevice ;

        public LazyDevice(Device finalDevice) {
            this.finalDevice = finalDevice;
        }

        public void flush() throws IOException {
            sleep();
            finalDevice.flush();
        }

        public void close() throws IOException {
            sleep();
            finalDevice.close();
            log.warn(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  lazy call count:"+count);
        }

        public boolean isSizePreserving() {
            sleep();
            return finalDevice.isSizePreserving();
        }

        public Device print(char c) throws IOException {
            sleep();
            return finalDevice.print(c);
        }

        public Device print(char[] c) throws IOException {
            sleep();
            return finalDevice.print(c);
        }

        public Device print(char[] c, int start, int len) throws IOException {
            sleep();
            return finalDevice.print(c, start, len);
        }

        public Device print(String s) throws IOException {
            sleep();
            return finalDevice.print(s);
        }

        public Device print(int i) throws IOException {
            sleep();
            return finalDevice.print(i);
        }

        public Device print(Object o) throws IOException {
            sleep();
            return finalDevice.print(o);
        }

        public Device write(int c) throws IOException {
            sleep();
            return finalDevice.write(c);
        }

        public Device write(byte[] b) throws IOException {
            sleep();
            return finalDevice.write(b);
        }

        public Device write(byte[] b, int off, int len) throws IOException {
            sleep();
            return finalDevice.write(b, off, len);

        }

        private int count = 0;
        private void sleep() {
            try {
                count ++;
                long start = System.currentTimeMillis();
                //Thread.currentThread().wait(30); dud ned
                if (count % 10 == 0)
                    while (start + 1 > System.currentTimeMillis()) ;
            } catch (Exception e) {
                //foo
            }
        }
    }*/

    static class CachingServletDevice  implements Device {
        private final StringBufferDevice bufferDevice = new StringBufferDevice();
        private final Device finalDevice ;

        public CachingServletDevice(Device finalDevice) {
            this.finalDevice = finalDevice;
        }

        public String toString() {
            return bufferDevice.toString();
        }

        public boolean isSizePreserving() {
            return bufferDevice.isSizePreserving();
        }

        public void flush() throws IOException {
            bufferDevice.flush();
        }

        public void close() throws IOException {
            bufferDevice.flush();
            finalDevice.print(bufferDevice.toString());
            finalDevice.close();
            bufferDevice.close();
        }

        public void reset() {
            bufferDevice.reset();
        }

        public Device print(String s) {
            return bufferDevice.print(s);
        }

        public Device print(char c) {
            return bufferDevice.print(c);
        }

        public Device print(char[] c) throws IOException {
            return bufferDevice.print(c);
        }

        public Device print(char[] c, int start, int len) throws IOException {
            return bufferDevice.print(c, start, len);
        }

        public Device print(int i) {
            return bufferDevice.print(i);
        }

        public Device print(Object o) {
            return bufferDevice.print(o);
        }

        public Device write(int c) throws IOException {
            return bufferDevice.write(c);
        }

        public Device write(byte[] b) throws IOException {
            return bufferDevice.write(b);
        }

        public Device write(byte[] b, int off, int len) throws IOException {
            return bufferDevice.write(b, off, len);
        }
    }
}
