/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css.dwr;

import uk.ltd.getahead.dwr.Creator;

import org.w3c.dom.Element;
import org.wings.session.Session;
import org.wings.session.SessionManager;

import java.util.Map;

/**
 * @author hengels
 * @version $Revision$
 */
public class SessionCreator implements Creator
{
    private Object callable;
    Session session = SessionManager.getSession();

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

    public Session getSession() {
        return session;
    }
}
