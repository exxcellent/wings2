/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package dwr;

import org.wings.session.SessionManager;

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
        SessionCreatorManager.setSession(SessionManager.getSession().getServletRequest().getSession());
        creatorManager.addCreator(scriptName, callable);
        SessionCreatorManager.removeSession();
    }

    public void unregisterCallable(String scriptName) {
        SessionCreatorManager.setSession(SessionManager.getSession().getServletRequest().getSession());
        creatorManager.removeCreator(scriptName);
        SessionCreatorManager.removeSession();
    }
}
