/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css.dwr;

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

import javax.servlet.http.HttpSession;

import org.w3c.dom.Element;
import org.wings.session.SessionManager;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.CreatorManager;
import uk.ltd.getahead.dwr.ExecutionContext;

/**
 * The callables are referenced weakly, only. Thus, most callables are destroyed as soon as there's
 * no component's client property referencing them anymore.
 *
 * @author hengels
 * @version $Revision$
 */
public class SessionCreatorManager
    implements CreatorManager
{
    private boolean debug;

    public SessionCreatorManager() {
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void addCreatorType(String typename, Class clazz) {
    }

    public void addCreator(String typename, String scriptName, Map params) throws InstantiationException, IllegalAccessException, IllegalArgumentException {
    }

    public void addCreator(String type, String javascript, Element allower) {
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

    public void addCreator(String s, Object callable) {
        Map map = getCreatorMap();
        map.put(s, new SessionCreator(callable));
    }

    public void removeCreator(String scriptName) {
        Map map = getCreatorMap();
        map.remove(scriptName);
    }

    private Map getCreatorMap() {
        HttpSession session = ExecutionContext.get().getSession();
        Map map = (Map) session.getAttribute("CreatorMap");
        if (map == null) {
            map = new WeakHashMap();
            session.setAttribute("CreatorMap", map);
        }
        return map;
    }
}
