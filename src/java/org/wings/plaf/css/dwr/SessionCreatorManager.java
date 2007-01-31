/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css.dwr;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

import javax.servlet.http.HttpSession;

import org.wings.session.SessionManager;

import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.WebContextFactory;

/**
 * The callables are referenced weakly, only. Thus, most callables are destroyed as soon as there's
 * no component's client property referencing them anymore.
 *
 * @author hengels
 * @author raedler
 */
public class SessionCreatorManager implements CreatorManager {

    public SessionCreatorManager() {
        // empty
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.CreatorManager#isDebug()
     */
    public boolean isDebug()
    {
        return false;
    }        

    public void addCreatorType(String typename, String clazz) {
    }

    public void addCreator(String typename, String scriptName, Map params) throws InstantiationException, IllegalAccessException, IllegalArgumentException {
    }

    public Collection getCreatorNames() {
        Map map = getCreatorMap();
        return map.keySet();
    }

    public Creator getCreator(String name) {
        Map map = getCreatorMap();
        SessionCreator creator = (SessionCreator) map.get(name);
        if (SessionManager.getSession() == null)
            SessionManager.setSession(creator.getSession());
        return creator;
    }

    public void setCreators(Map creators) {
    }

    public void addCreator(String s, Creator creator) {
        Map map = getCreatorMap();
        map.put(s, creator);
    }

    public void removeCreator(String scriptName) {
        Map map = getCreatorMap();
        map.remove(scriptName);
    }

    private Map getCreatorMap() {
        HttpSession session = WebContextFactory.get().getSession();
        Map map = (Map) session.getAttribute("CreatorMap");
        if (map == null) {
            map = new WeakHashMap();
            session.setAttribute("CreatorMap", map);
        }
        return map;
    }
}
