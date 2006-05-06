/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css.dwr;

import org.wings.session.SessionManager;
import org.wings.session.Session;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.WebContextBuilder;
import org.directwebremoting.impl.DefaultWebContextBuilder;
import org.directwebremoting.impl.DefaultContainer;

import java.util.Collection;
import java.io.Serializable;

/**
 * @author hengels
 * @version $Revision$
 */
public class CallableManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SessionCreatorManager creatorManager = new SessionCreatorManager();

    public static CallableManager getInstance() {
        Session session = SessionManager.getSession();
        CallableManager callableManager = (CallableManager) session.getProperty("CallableManager");
        if (callableManager == null) {
            callableManager = new CallableManager();
            session.setProperty("CallableManager", callableManager);
        }
        return callableManager;
    }

    public void registerCallable(String scriptName, Object callable) {
        Session session = SessionManager.getSession();

        WebContextBuilder builder = new DefaultWebContextBuilder();
        builder.set(session.getServletRequest(), session.getServletResponse(), null, session.getServletContext(), new DefaultContainer());

        WebContextFactory.setWebContextBuilder(builder);
        creatorManager.addCreator(scriptName, new SessionCreator(callable));
        builder.unset();
    }

    public void unregisterCallable(String scriptName) {
        Session session = SessionManager.getSession();

        WebContextBuilder builder = new DefaultWebContextBuilder();
        builder.set(session.getServletRequest(), session.getServletResponse(), null, session.getServletContext(), new DefaultContainer());

        WebContextFactory.setWebContextBuilder(builder);
        creatorManager.removeCreator(scriptName);
        builder.unset();
    }

    public boolean containsCallable(String scriptName) {
        Session session = SessionManager.getSession();

        WebContextBuilder builder = new DefaultWebContextBuilder();
        builder.set(session.getServletRequest(), session.getServletResponse(), null, session.getServletContext(), new DefaultContainer());

        WebContextFactory.setWebContextBuilder(builder);
        boolean b = creatorManager.getCreatorNames().contains(scriptName);
        builder.unset();

        return b;
    }

    public Collection callableNames() {
        Session session = SessionManager.getSession();

        WebContextBuilder builder = new DefaultWebContextBuilder();
        builder.set(session.getServletRequest(), session.getServletResponse(), null, session.getServletContext(), new DefaultContainer());

        WebContextFactory.setWebContextBuilder(builder);
        Collection c = creatorManager.getCreatorNames();
        builder.unset();

        return c;
    }
}