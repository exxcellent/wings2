/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package dwr;

import uk.ltd.getahead.dwr.CreatorManager;
import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.ExecutionContext;
import org.w3c.dom.Element;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

/**
 * @author hengels
 * @version $Revision$
 */
public class SessionCreatorManager
    implements CreatorManager
{
    static ThreadLocal sessions = new ThreadLocal();
    boolean debug;

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

    public void addCreator(String type, String javascript, Element allower) {
    }

    public Collection getCreatorNames() {
        Map map = getCreatorMap();
        return map.keySet();
    }

    public Creator getCreator(String name) {
        Map map = getCreatorMap();
        return (Creator)map.get(name);
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
            map = new HashMap();
            session.setAttribute("CreatorMap", map);
        }
        return map;
    }
}
