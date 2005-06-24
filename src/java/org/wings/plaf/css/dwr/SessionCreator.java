/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css.dwr;

import uk.ltd.getahead.dwr.Creator;

import org.w3c.dom.Element;

import java.util.Map;

/**
 * @author hengels
 * @version $Revision$
 */
public class SessionCreator implements Creator
{
    private Object callable;

    public SessionCreator(Object callable) {
        this.callable = callable;
    }

    public void init(Element config) {
    }

    public void setProperties(Map params) throws IllegalArgumentException {
    }

    public Class getType() {
        return callable.getClass();
    }

    public Object getInstance() {
        return callable;
    }

    public String getScope() {
        return SESSION;
    }
}
