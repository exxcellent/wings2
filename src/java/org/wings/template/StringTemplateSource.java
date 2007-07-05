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
package org.wings.template;

import org.wings.session.SessionManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;


/**
 * A simple template source, which provides a template from a String
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 */
public class StringTemplateSource implements TemplateSource, Serializable {

    private static long COUNTER = 0;

    private static final synchronized long getNextId() {
        return COUNTER++;
    }

    /**
     * Try to make generate a unique canonical name.
     */
    private final String canonicalName = getClass().getName() + "_" + getNextId();

    private String template;

    private long lastModified;


    public StringTemplateSource() {
    }


    public StringTemplateSource(String template) {
        setTemplate(template);
    }

    public final void setTemplate(String t) {
        this.template = t;
        lastModified = System.currentTimeMillis();
    }
    
    // Implementation of org.wings.template.TemplateSource

    public long lastModified() {
        return lastModified;
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(template.getBytes(SessionManager.getSession().getCharacterEncoding()));
    }

    public final String getCanonicalName() {
        return canonicalName;
    }

}// StringTemplateSource

