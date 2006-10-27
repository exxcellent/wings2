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
package org.wings.externalizer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class ExternalizeManager extends AbstractExternalizeManager {

    private static final Externalizer[] DEFAULT_EXTERNALIZERS = {
        ImageExternalizer.SHARED_PNG_INSTANCE,
        ImageExternalizer.SHARED_GIF_INSTANCE,
        ImageExternalizer.SHARED_JPG_INSTANCE,
        ImageIconExternalizer.SHARED_PNG_INSTANCE,
        ImageIconExternalizer.SHARED_GIF_INSTANCE,
        ImageIconExternalizer.SHARED_JPG_INSTANCE,
        StaticResourceExternalizer.SHARED_INSTANCE,
        StringResourceExternalizer.SHARED_INSTANCE,
        DynamicResourceExternalizer.SHARED_INSTANCE,
        ResourceExternalizer.SHARED_INSTANCE,
    };

    protected final HashMap externalizerByClass = new HashMap();

    protected final HashMap externalizerByMimeType = new HashMap();

    protected final Map externalized = Collections.synchronizedMap(new HashMap());


    public ExternalizeManager() {
        this(true);
    }

    public ExternalizeManager(boolean initWithDefaultExternalizers) {
        if (initWithDefaultExternalizers) {
            addDefaultExternalizers();
        }
    }


    public final void addDefaultExternalizers() {
        for (int i = 0; i < DEFAULT_EXTERNALIZERS.length; i++) {
            addExternalizer(DEFAULT_EXTERNALIZERS[i]);
        }
    }

    protected final void storeExternalizedResource(String identifier,
                                                   ExternalizedResource extInfo) {
        if (log.isDebugEnabled()) {
            log.debug("store identifier " + identifier + " " + extInfo.getObject().getClass());
            log.debug("flags " + extInfo.getFlags());
        }
        externalized.put(identifier, extInfo);
    }

    public final Object getExternalizedObject(String identifier) {
        ExternalizedResource info = getExternalizedResource(identifier);

        if (info != null)
            return info.getObject();

        return null;
    }

    /**
     * stripts the identifier from attachments to the external names.
     */
    private String stripIdentifier(String identifier) {
        if (identifier == null || identifier.length() < 1)
            return null;

        int pos = identifier.indexOf(org.wings.SConstants.UID_DIVIDER);
        if (pos > -1) {
            identifier = identifier.substring(pos + 1);
        }
        if (identifier.length() < 1) {
            return null;
        }
        return identifier;
    }

    public final ExternalizedResource getExternalizedResource(String identifier) {
        identifier = stripIdentifier(identifier);
        if (identifier == null) return null;

        // SystemExternalizeManager hat Minus as prefix.
        if (identifier.charAt(0) == '-') {
            return SystemExternalizeManager.getSharedInstance().
                    getExternalizedResource(identifier);
        }

        return (ExternalizedResource) externalized.get(identifier);
    }

    public final void removeExternalizedResource(String identifier) {
        identifier = stripIdentifier(identifier);
        if (identifier == null) return;
        Object externalizedResource = externalized.remove(identifier);
        reverseExternalized.remove(externalizedResource);
    }


    public String externalize(Object obj) {
        return externalize(obj, SESSION);
    }


    public String externalize(Object obj, Collection headers) {
        return externalize(obj, headers, SESSION);
    }


    public String externalize(Object obj, int flags) {
        return externalize(obj, (Collection) null, flags);
    }


    public String externalize(Object obj, Collection headers, int flags) {
        if (obj == null)
            throw new IllegalArgumentException("object must not be null");

        Externalizer externalizer = getExternalizer(obj.getClass());
        if (externalizer == null) {
            log.warn("could not find externalizer for " +
                    obj.getClass().getName());
            return NOT_FOUND_IDENTIFIER;
        }

        return externalize(obj, externalizer, headers, flags);
    }


    public String externalize(Object obj, String mimeType) {
        return externalize(obj, mimeType, SESSION);
    }


    public String externalize(Object obj, String mimeType, Collection headers) {
        return externalize(obj, mimeType, headers, SESSION);
    }


    public String externalize(Object obj, String mimeType, int flags) {
        return externalize(obj, mimeType, null, flags);
    }


    public String externalize(Object obj, String mimeType,
                              Collection headers, int flags) {
        if (obj == null)
            throw new IllegalStateException("no externalizer");

        Externalizer externalizer = mimeType != null ? getExternalizer(mimeType) : null;
        if (externalizer == null) {
            return externalize(obj, headers, flags);
        } else {
            return externalize(obj, externalizer, headers, flags);
        }
    }

    /**
     * Adds an externalizer. If an externalizer is already
     * registered for a class or a mime type, it will be replaced.
     */
    public void addExternalizer(Externalizer externalizer) {
        if (externalizer != null) {
            Class c[] = externalizer.getSupportedClasses();
            if (c != null)
                for (int i = 0; i < c.length; i++)
                    if (c[i] != null)
                        externalizerByClass.put(c[i], externalizer);

            String mimeTypes[] = externalizer.getSupportedMimeTypes();
            if (mimeTypes != null)
                for (int i = 0; i < mimeTypes.length; i++)
                    if (mimeTypes[i] != null &&
                            mimeTypes[i].trim().length() > 0)
                        externalizerByMimeType.put(mimeTypes[i].trim().toLowerCase(),
                                externalizer);
        }
    }

    /**
     * Adds an Externalizer
     */
    public void addExternalizer(Externalizer externalizer, String mimeType) {
        if (externalizer != null && mimeType != null)
            externalizerByMimeType.put(mimeType, externalizer);
    }

    /**
     * Returns an object externalizer for a class. If one could not be found,
     * it goes down the inheritance tree and looks for an object externalizer
     * for the super classes. If one still could not be found, it goes
     * through the list of interfaces of the class and checks for object
     * externalizers for every interface. If this also doesn't return an
     * object externalizer, null is returned.
     */
    public Externalizer getExternalizer(Class c) {
        Externalizer externalizer = null;
        if (c != null) {
            externalizer = getSuperclassExternalizer(c);
            if (externalizer == null)
                externalizer = getInterfaceExternalizer(c);
        }
        return externalizer;
    }


    private Externalizer getSuperclassExternalizer(Class c) {
        Externalizer externalizer = null;
        if (c != null) {
            externalizer = (Externalizer) externalizerByClass.get(c);
            if (externalizer == null)
                externalizer = getExternalizer(c.getSuperclass());
        }
        return externalizer;
    }


    private Externalizer getInterfaceExternalizer(Class c) {
        Externalizer externalizer = null;
        Class[] ifList = c.getInterfaces();
        for (int i = 0; i < ifList.length; i++) {
            externalizer = (Externalizer) externalizerByClass.get(ifList[i]);
            if (externalizer != null)
                break;
        }
        return externalizer;
    }

    /**
     * returns an object externalizer for a mime type
     */
    public Externalizer getExternalizer(String mimeType) {
        Externalizer externalizer = null;
        if (mimeType != null && mimeType.length() > 0) {
            externalizer = (Externalizer) externalizerByMimeType.get(mimeType);
            if (externalizer == null) {
                if (mimeType.indexOf('/') >= 0)
                    externalizer =
                            getExternalizer(mimeType.substring(0, mimeType.indexOf('/')));
            }
        }
        return externalizer;
    }

    public void clear() {
        super.clear();
        externalizerByClass.clear();
        externalizerByMimeType.clear();
        externalized.clear();
    }
}


