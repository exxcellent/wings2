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
package org.wings.resource;

import org.wings.StaticResource;
import org.wings.externalizer.ExternalizeManager;
import java.io.InputStream;

/**
 * A Classpath Resource is a static resource whose content is
 * read from a classloader.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 */
public class ClassPathResource
        extends StaticResource
    implements NamedResource
{
    /**
     * The class loader from which the resource is loaded
     */
    private final transient ClassLoader classLoader;

    /**
     * The name that identifies the resource in the classpath
     */
    protected final String resourceFileName;

    /**
     * A static resource that is obtained from the default classpath.
     */
    public ClassPathResource(String resourceFileName) {
        this(null, resourceFileName, "unkonwn");
    }

    /**
     * A static resource that is obtained from the default classpath.
     */
    public ClassPathResource(String resourceFileName, String mimeType) {
        this(null, resourceFileName, mimeType);
    }

    /**
     * A static resource that is obtained from the specified class loader
     *
     * @param classLoader      the classLoader from which the resource is obtained
     * @param resourceFileName the resource relative to the baseClass
     */
    public ClassPathResource(ClassLoader classLoader, String resourceFileName) {
        this(classLoader, resourceFileName, "unknown");
    }

    /**
     * A static resource that is obtained from the specified class loader
     *
     * @param classLoader      the classLoader from which the resource is obtained
     * @param resourceFileName the resource relative to the baseClass
     */
    public ClassPathResource(ClassLoader classLoader, String resourceFileName, String mimeType) {
        super(null, mimeType);
        this.classLoader = classLoader;
        this.resourceFileName = resourceFileName;
        int dotIndex = resourceFileName.lastIndexOf('.');
        if (dotIndex > -1) {
            extension = resourceFileName.substring(dotIndex + 1);
        }
        externalizerFlags = ExternalizeManager.GLOBAL | ExternalizeManager.FINAL;
    }

    protected InputStream getResourceStream() {
        InputStream stream = getClassLoader().getResourceAsStream(resourceFileName);
        if (stream == null)
            throw new IllegalArgumentException("Resource not found: " + resourceFileName);
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
        return (classLoader != null ? classLoader.hashCode() : 0) ^ resourceFileName.hashCode();
    }

    /**
     * Two ClasspathResouces are equal if both of them use the same
     * classloader and point to a resource with the same name.
     *
     * @return true if classloader and resource name are equal.
     */
    public boolean equals(Object o) {
        if (o instanceof ClassPathResource) {
            ClassPathResource other = (ClassPathResource) o;
            return ((this == other)
                    || ((classLoader == other.classLoader || (classLoader != null && classLoader.equals(other.classLoader)))
                    && resourceFileName.equals(other.resourceFileName)));
        }
        return false;
    }

    /**
     * @return The stored classloader or the current context classloader if none fixed passed (prefer this case due to session serialization!)
     */
    protected ClassLoader getClassLoader() {
        return this.classLoader == null ? Thread.currentThread().getContextClassLoader() : this.classLoader;
    }

    public String getResourceName() {
        return resourceFileName;
    }
}
