/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package dwr;

import org.wings.session.SessionManager;
import org.wings.session.Session;

import uk.ltd.getahead.dwr.ExecutionContext;

/**
 * @author hengels
 * @version $Revision$
 */
public class CallableManager
{
    SessionCreatorManager creatorManager = new SessionCreatorManager();

    public static CallableManager getInstance() {
        CallableManager callableManager = (CallableManager)SessionManager.getSession().getProperty("CallableManager");
        if (callableManager == null) {
            callableManager = new CallableManager();
            SessionManager.getSession().setProperty("CallableManager", callableManager);
        }
        return callableManager;
    }

    public void registerCallable(String scriptName, Object callable) {
        Session session = SessionManager.getSession();
        ExecutionContext.setExecutionContext(session.getServletRequest(), session.getServletResponse(), null);
        creatorManager.addCreator(scriptName, callable);
        ExecutionContext.unset();
    }

    public void unregisterCallable(String scriptName) {
        Session session = SessionManager.getSession();
        ExecutionContext.setExecutionContext(session.getServletRequest(), session.getServletResponse(), null);
        creatorManager.removeCreator(scriptName);
        ExecutionContext.unset();
    }
}
