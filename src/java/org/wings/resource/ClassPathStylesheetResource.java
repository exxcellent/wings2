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
package org.wings.resource;

import org.wings.Resource;
import org.wings.StaticResource;
import org.wings.externalizer.ExternalizeManager;
import org.wings.session.SessionManager;
import org.wings.template.StreamTemplateSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A ClassPathStylesheetResource is a static resource whose content is
 * read from a classloader. It is special for its handling of occurences
 * of "url([classPath])" strings. These are read from classPath and 
 * externalized.
 *
 * @author ole
 * @version $$
 */
public class ClassPathStylesheetResource
        extends StaticResource {
    /**
     * The default max size for the buffer. since we do some replacement every time we
     * read the input stream, we want this to be high, so we can cache the
     * interpreted style sheet.
     */
    private static final int MAX_BUFFER_SIZE = 24 * 1024; // 24kb
    /**
     * The class loader from which the resource is loaded
     */
    protected final ClassLoader classLoader;

    /**
     * The name that identifies the resource in the classpath
     */
    protected final String resourceFileName;

    private ExternalizeManager extManager;

    /**
     * A static css resource that is obtained from the default classpath.
     */
    public ClassPathStylesheetResource(String resourceFileName) {
        this(Resource.class.getClassLoader(), resourceFileName, "unkonwn");
    }

    /**
     * A static css resource that is obtained from the default classpath.
     */
    public ClassPathStylesheetResource(String resourceFileName, String mimeType) {
        this(Resource.class.getClassLoader(), resourceFileName, mimeType);
    }

    /**
     * A static css resource that is obtained from the specified class loader
     *
     * @param classLoader      the classLoader from which the resource is obtained
     * @param resourceFileName the css resource relative to the baseClass
     */
    public ClassPathStylesheetResource(ClassLoader classLoader, String resourceFileName) {
        this(classLoader, resourceFileName, "unknown");
    }

    /**
     * A static css resource that is obtained from the specified class loader
     *
     * @param classLoader      the classLoader from which the resource is obtained
     * @param resourceFileName the css resource relative to the baseClass
     * @param mimeType         the mimetype of the resource
     */
    public ClassPathStylesheetResource(ClassLoader classLoader, String resourceFileName, String mimeType) {
        this(classLoader, resourceFileName, "unknown", MAX_BUFFER_SIZE);
    }

    /**
     * A static css resource that is obtained from the specified class loader
     *
     * @param classLoader      the classLoader from which the resource is obtained
     * @param resourceFileName the css resource relative to the baseClass
     * @param mimeType         the mimetype of the resource
     * @param maxBufferSize    the maximum buffer size for the style sheet. If
     *                         big enough, stylesheet is cached, else parsed again. 
     */
    public ClassPathStylesheetResource(ClassLoader classLoader, String resourceFileName, String mimeType, int maxBufferSize) {
        super(null, mimeType);
        this.classLoader = classLoader;
        this.resourceFileName = resourceFileName;
        int dotIndex = resourceFileName.lastIndexOf('.');
        if (dotIndex > -1) {
            extension = resourceFileName.substring(dotIndex + 1);
        }
        externalizerFlags = ExternalizeManager.GLOBAL | ExternalizeManager.FINAL;
        extManager = SessionManager.getSession().getExternalizeManager();
    }


    public String toString() {
        return getId() + " " + resourceFileName;
    }

    /* (non-Javadoc)
     * @see org.wings.StaticResource#getResourceStream()
     */
    protected final InputStream getResourceStream() {
        InputStream in = classLoader.getResourceAsStream(resourceFileName);
        CssUrlFilterInputStream stream = new CssUrlFilterInputStream(in, extManager);
        return stream;
    }

    /*
     * the equal() and hashCode() method make sure, that the same resources
     * get the same name in the SystemExternalizer.
     */

    /**
     * resources using the same classloader and are denoting the same
     * name, do have the same hashCode(). Thus the same resources get the
     * same ID in the System externalizer.
     *
     * @return a hashcode, comprised from the hashcodes of the classloader
     *         and from the file name of the resource.
     */
    public int hashCode() {
        return classLoader.hashCode() ^ resourceFileName.hashCode();
    }

    /**
     * Two ClasspathResouces are equal if both of them use the same
     * classloader and point to a resource with the same name.
     *
     * @return true if classloader and resource name are equal.
     */
    public boolean equals(Object o) {
        if (o instanceof ClassPathStylesheetResource) {
            ClassPathStylesheetResource other = (ClassPathStylesheetResource) o;
            return ((this == other)
                    || (classLoader.equals(other.classLoader)
                    && resourceFileName.equals(other.resourceFileName)));
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.wings.StaticResource#bufferResource()
     */
    protected LimitedBuffer bufferResource() throws IOException {
        // we need a bigger buffer, so that the parsing only happens once
        setMaxBufferSize(MAX_BUFFER_SIZE);
        return super.bufferResource();
    }
}


