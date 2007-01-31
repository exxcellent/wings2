package org.wings.session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.wings.SComponent;
import org.wings.SFrame;
import org.wings.SMenu;
import org.wings.SPopupMenu;

public class MenuManager {

    private Map menuLinkMap = new HashMap();

    public void registerMenuLink(SMenu menu, SComponent component) {
        register(menu, component);
    }

    public void registerMenuLink(SPopupMenu menu, SComponent component) {
        register(menu, component);
    }

    private void register(SComponent menu, SComponent component) {
        if (menu == null)
            throw new IllegalArgumentException("Menu must not be null!");

        Set components = getComponents(menu);
        components.add(component);
        menuLinkMap.put(menu, components);
    }

    public void deregisterMenuLink(SMenu menu, SComponent component) {
        deregister(menu, component);
    }

    public void deregisterMenuLink(SPopupMenu menu, SComponent component) {
        deregister(menu, component);
    }

    private void deregister(SComponent menu, SComponent component) {
        if (menu == null)
            throw new IllegalArgumentException("Menu must not be null!");

        Set components = getComponents(menu);
        components.remove(component);

        if (components.isEmpty()) {
            menuLinkMap.remove(menu);
        } else {
            menuLinkMap.put(menu, components);
        }
    }

    public boolean isMenuLinked(SMenu menu) {
        return !getComponents(menu).isEmpty();
    }

    public boolean isMenuLinked(SPopupMenu menu) {
        return !getComponents(menu).isEmpty();
    }

    public Set getMenueLinks(SMenu menu) {
        return getComponents(menu);
    }

    public Set getMenueLinks(SPopupMenu menu) {
        return getComponents(menu);
    }

    public Set getMenues() {
        return menuLinkMap.keySet();
    }

    public Set getMenues(SFrame frame) {
        Set menuesUsedInFrame = new HashSet(menuLinkMap.keySet());
        for (Iterator i = menuesUsedInFrame.iterator(); i.hasNext();) {
            if (((SComponent) i.next()).getParentFrame() != frame) {
                i.remove();
            }
        }
        return menuesUsedInFrame;
    }

    private Set getComponents(SComponent menu) {
        Set links = (Set) menuLinkMap.get(menu);
        if (links == null) {
            links = new HashSet(2);
        }
        return links;
    }

}