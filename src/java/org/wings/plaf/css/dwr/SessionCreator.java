/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css.dwr;

import org.directwebremoting.extend.Creator;
import org.wings.session.Session;
import org.wings.session.SessionManager;

import java.util.Map;

/**
 * @author hengels
 * @author raedler
 */
public class SessionCreator implements Creator
{
    private Object callable;
    Session session = SessionManager.getSession();

    public SessionCreator(Object callable) {
        this.callable = callable;
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

    public boolean isCacheable() {
        return true;
    }

    public String getJavascript() {
        return null;
    }

    public Session getSession() {
        return session;
    }
}
