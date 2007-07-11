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

import java.util.Collection;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.RequestURL;
import org.wings.Resource;
import org.wings.SFrame;
import org.wings.SimpleURL;
import org.wings.externalizer.ExternalizeManager;
import org.wings.session.PropertyService;
import org.wings.session.SessionManager;

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
     * The frame, to which this resource belongs.
     */
    private SFrame frame;

    private PropertyService propertyService;

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

    public SimpleURL getURL() {
        RequestURL requestURL = (RequestURL)SessionManager.getSession().getProperty("request.url");
        if (requestURL != null) {
            requestURL = (RequestURL) requestURL.clone();
            requestURL.setResource(getId());
        }
        return requestURL;
    }

    public String toString() {
        return getId() + " " + getFrame().getEventEpoch();
    }

    /**
     * Get additional http-headers.
     * Returns <tt>null</tt>, if there are no additional headers to be set.
     *
     * @return Set of {@link java.util.Map.Entry} (key-value pairs)
     */
    public Collection<HttpHeader> getHeaders() {
        return null;
    }
}


