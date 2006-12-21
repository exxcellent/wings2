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

    private Map menuLinks = new HashMap();

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
        menuLinks.put(menu, components);
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
            menuLinks.remove(menu);
        } else {
            menuLinks.put(menu, components);
        }
    }

    public boolean isMenuLinked(SMenu menu) {
        return !getComponents(menu).isEmpty();
    }

    public boolean isMenuLinked(SPopupMenu menu) {
        return !getComponents(menu).isEmpty();
    }

    public Set getMenues() {
        return menuLinks.keySet();
    }

    public Set getMenues(SFrame frame) {
        Set menuesUsedInFrame = new HashSet(menuLinks.keySet());
        for (Iterator i = menuesUsedInFrame.iterator(); i.hasNext();) {
            if (((SComponent) i.next()).getParentFrame() != frame) {
                i.remove();
            }
        }
        return menuesUsedInFrame;
    }

    public Set getMenueLinks(SMenu menu) {
        return getComponents(menu);
    }

    public Set getMenueLinks(SPopupMenu menu) {
        return getComponents(menu);
    }

    private Set getComponents(SComponent menu) {
        Set links = (Set) menuLinks.get(menu);
        if (links == null) {
            links = new HashSet(2);
        }
        return links;
    }

}