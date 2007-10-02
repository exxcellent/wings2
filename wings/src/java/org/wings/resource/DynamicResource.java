/*
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.RequestURL;
import org.wings.Resource;
import org.wings.SFrame;
import org.wings.SimpleURL;
import org.wings.externalizer.ExternalizeManager;
import org.wings.session.PropertyService;
import org.wings.session.SessionManager;
import org.wings.util.StringUtil;

import java.util.Collection;
import java.util.Set;

/**
 * Dynamic Resources are web resources representing rendered components
 * and are individually loaded by Browsers as different 'files'.
 * Dynamic Resources include therefore frames, cascading stylesheets or
 * script files. The externalizer gives them a uniqe name.
 * The resources may change in the consequence of some internal change of
 * the components. This invalidation process yields a new 'version', called
 * epoch here. The epoch is part of the externalized name.
 */
public abstract class DynamicResource
        extends Resource {
    private final transient static Log log = LogFactory.getLog(DynamicResource.class);

    /**
     * The epoch of this resource. With each invalidation, this counter
     * is incremented.
     */
    private int epoch = 0;

    // byte[] ? Since we use this to write it to a stream ..
    /**
     * The epoch as string representation. The epoch is converted in a string
     * that is as short as possible. This is done once whenever the epoch
     * changes to do this conversion only once.
     */
    private String epochCache = "W" + StringUtil.toShortestAlphaNumericString(epoch);

    /**
     * The frame, to which this resource belongs.
     */
    private SFrame frame;

    protected DynamicResource(String extension, String mimeType) {
        super(extension, mimeType);
    }


    public DynamicResource(SFrame frame) {
        this(frame, "", "");
    }


    public DynamicResource(SFrame frame, String extension, String mimeType) {
        super(extension, mimeType);
        this.frame = frame;
    }

    /**
     * Return the frame, to which this resource belongs.
     */
    public final SFrame getFrame() {
        return frame;
    }

    public String getId() {
        if (id == null) {
            ExternalizeManager ext = SessionManager.getSession().getExternalizeManager();
            id = ext.getId(ext.externalize(this));
            log.debug("new " + getClass().getName() + " with id " + id);
        }
        return id;
    }

    /**
     * Mark this dynamic resource as to be re-rendered. This method is
     * called, whenever some change took place in the frame, so that this
     * dynamic resource is to be externalized with a new version-number.
     */
    public final void invalidate() {
        epochCache = "W" + StringUtil.toShortestAlphaNumericString(++epoch);
        if (log.isDebugEnabled()) {
            String name = getClass().getName();
            name = name.substring(name.lastIndexOf(".") + 1);
            log.debug("[" + name + "] " +
                    "invalidate - epoch: " + epochCache);
        }

    }


    public final String getEpoch() {
        return epochCache;
    }

    public SimpleURL getURL() {
        RequestURL requestURL = (RequestURL) getPropertyService().getProperty("request.url");
        if (requestURL != null) {
            requestURL = (RequestURL) requestURL.clone();
            requestURL.setEpoch(getEpoch());
            requestURL.setResource(getId());
        }
        return requestURL;
    }

    private PropertyService propertyService;

    protected PropertyService getPropertyService() {
        if (propertyService == null)
            propertyService = SessionManager.getSession();
        return propertyService;
    }


    public String toString() {
        return getId() + " " + getEpoch();
    }


    /**
     * Get additional http-headers.
     * Returns <tt>null</tt>, if there are no additional headers to be set.
     *
     * @return Set of {@link java.util.Map.Entry} (key-value pairs)
     */
    public Collection getHeaders() {
        return null;
    }

    /**
     * Get additional http-headers.
     * Returns <tt>null</tt>, if there are no additional headers to be set.
     *
     * @return Set of {@link java.util.Map.Entry} (key-value pairs)
     */
    public Set getCookies() {
        return null;
    }
}


