/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css.dwr;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;
import org.wings.header.SessionHeaders;
import org.wings.header.JavaScriptHeader;
import org.wings.session.SessionManager;
import org.wings.session.Session;
import org.wings.resource.SessionResource;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.directwebremoting.impl.DefaultWebContextBuilder;
import org.directwebremoting.impl.DefaultContainer;

import java.util.Collection;
import java.io.Serializable;

/**
 * @author hengels
 */
public class CallableManager implements Serializable {

    private static final long serialVersionUID = 1L;

    private SessionCreatorManager creatorManager = new SessionCreatorManager();
    private SessionAccessControl accessControl = new SessionAccessControl();

    public static CallableManager getInstance() {
        Session session = SessionManager.getSession();
        CallableManager callableManager = (CallableManager) session.getProperty("CallableManager");
        if (callableManager == null) {
            callableManager = new CallableManager();
            session.setProperty("CallableManager", callableManager);
        }
        return callableManager;
    }

    /**
     * Register callable and expose all public methods.
     * @param scriptName
     * @param callable
     */
    public void registerCallable(String scriptName, Object callable) {
        Session session = SessionManager.getSession();

        WebContextBuilder builder = new DefaultWebContextBuilder();
        builder.set(session.getServletRequest(), session.getServletResponse(), null, session.getServletContext(), new DefaultContainer());

        WebContextFactory.setWebContextBuilder(builder);

        creatorManager.addCreator(scriptName, new SessionCreator(callable));
        builder.unset();

        JavaScriptHeader header = new JavaScriptHeader(new SessionResource("../dwr/interface/" + scriptName + ".js"));
        SessionHeaders.getInstance().registerHeader(header);
    }
    
    /**
     * Register callable and only expose a limited number of methods.
     * @param scriptName
     * @param callable
     * @param exportInterface interface that defines the methods to be exposed 
     * to the client by DWR
     */
    public void registerCallable(String scriptName, Object callable, Class exportInterface) {
        Session session = SessionManager.getSession();

        WebContextBuilder builder = new DefaultWebContextBuilder();
        builder.set(session.getServletRequest(), session.getServletResponse(), null, session.getServletContext(), new DefaultContainer());

        WebContextFactory.setWebContextBuilder(builder);

        Method[] methods = exportInterface.getMethods();
        
        for (int i = 0; i < methods.length; i++) {
            accessControl.addIncludeRule(scriptName, methods[i].getName());            
        }
                
        creatorManager.addCreator(scriptName, new SessionCreator(callable));
        builder.unset();

        JavaScriptHeader header = new JavaScriptHeader(new SessionResource("../dwr/interface/" + scriptName + ".js"));
        SessionHeaders.getInstance().registerHeader(header);
    }
    
    public void unregisterCallable(String scriptName) {
        Session session = SessionManager.getSession();

        WebContextBuilder builder = new DefaultWebContextBuilder();
        builder.set(session.getServletRequest(), session.getServletResponse(), null, session.getServletContext(), new DefaultContainer());

        WebContextFactory.setWebContextBuilder(builder);
        creatorManager.removeCreator(scriptName);
        builder.unset();

        JavaScriptHeader header = new JavaScriptHeader(new SessionResource("../dwr/interface/" + scriptName + ".js"));
        SessionHeaders.getInstance().deregisterHeader(header);
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
