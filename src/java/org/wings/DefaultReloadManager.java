package org.wings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.plaf.Update;
import org.wings.util.SStringBuilder;

/**
 * Default implementation of the reload manager.
 *
 * @author Stephan Schuster
 */
public class DefaultReloadManager implements ReloadManager {

    private final transient static Log log = LogFactory.getLog(DefaultReloadManager.class);

    private boolean updateMode = false;

    private boolean deliveryPhase = false;

    protected final Set componentsToReload = new HashSet();

    protected final Map updatesByComponent = new HashMap();

    public void reload(SComponent component) {
        if (component == null)
            return;

        if (!deliveryPhase) {
            componentsToReload.add(component);
        } else if (log.isDebugEnabled()) {
            log.debug("Component " + component + " requested reload during delivery phase.");
        }
    }

    public void addUpdate(Update update) {
        if (update == null)
            return;
        SComponent component = update.getComponent();
        if (component == null || component.getParentFrame() == null)
            return;

        if (!deliveryPhase) {
            Set updatesOfComponent = getUpdatesOfComponent(component);
            updatesOfComponent.remove(update);
            updatesOfComponent.add(update);
            updatesByComponent.put(component, updatesOfComponent);
        } else if (log.isDebugEnabled()) {
            log.debug("Component " + component + " requested update during delivery phase.");
        }
    }

    public List getUpdates() {
        filterUpdates();

        List filteredUpdates = new ArrayList();
        for (Iterator i = updatesByComponent.values().iterator(); i.hasNext();) {
            filteredUpdates.addAll((Set) i.next());
        }
        for (Iterator i = componentsToReload.iterator(); i.hasNext();) {
            SComponent component = (SComponent) i.next();
            filteredUpdates.add(component.getCG().update(component));
        }
        Collections.sort(filteredUpdates, new PositioningComparator());

        return filteredUpdates;
    }

    public Set getDirtyComponents() {
        int maxSize = componentsToReload.size() + updatesByComponent.size();
        Set dirtyComponents = new HashSet(maxSize);
        dirtyComponents.addAll(componentsToReload);
        dirtyComponents.addAll(updatesByComponent.keySet());
        return dirtyComponents;
    }

    public Set getDirtyFrames() {
        final Set dirtyFrames = new HashSet(5);
        for (Iterator iterator = getDirtyComponents().iterator(); iterator.hasNext();) {
            SFrame parentFrame = ((SComponent) iterator.next()).getParentFrame();
            if (parentFrame != null)
                dirtyFrames.add(parentFrame);
        }
        return dirtyFrames;
    }

    public void invalidateFrames() {
        Iterator it = getDirtyFrames().iterator();
        while (it.hasNext()) {
            ((SFrame) it.next()).invalidate();
            it.remove();
        }
        deliveryPhase = true;
    }

    public void notifyCGs() {
        for (Iterator iterator = getDirtyComponents().iterator(); iterator.hasNext();) {
            SComponent component = (SComponent) iterator.next();
            component.getCG().componentChanged(component);
        }
    }

    public void clear() {
        updateMode = false;
        deliveryPhase = false;
        componentsToReload.clear();
        updatesByComponent.clear();
    }

    public boolean componentRequestedReload(SComponent component) {
        return componentsToReload.contains(component);
    }

    public boolean componentRequestedUpdate(SComponent component) {
        return updatesByComponent.get(component) != null;
    }

    public boolean isUpdateMode() {
        return updateMode;
    }

    public void setUpdateMode(boolean updateMode) {
        this.updateMode = updateMode;
    }

    protected Set getUpdatesOfComponent(SComponent component) {
        Set updatesOfComponent = (Set) updatesByComponent.get(component);
        if (updatesOfComponent == null) {
            updatesOfComponent = new HashSet(5);
        }
        return updatesOfComponent;
    }

    protected Set getUpdatesOfComponent(SComponent component, int property) {
        Set updatesOfComponentWithProperty = new HashSet(5);
        Set updatesOfComponent = getUpdatesOfComponent(component);
        for (Iterator i = updatesOfComponent.iterator(); i.hasNext();) {
            Update update = (Update) i.next();
            if (update.getProperty() == property) {
                updatesOfComponentWithProperty.add(update);
            }
        }
        return updatesOfComponentWithProperty;
    }

    protected void filterUpdates() {
        if (log.isDebugEnabled()) {
            debugReloads("Reloads");
            debugUpdates("Overall Updates");
        }

        updatesByComponent.keySet().removeAll(componentsToReload);

        SortedMap componentsByPath = new TreeMap(new PathToParentComparator());
        for (Iterator i = componentsToReload.iterator(); i.hasNext();) {
            SComponent component = (SComponent) i.next();
            componentsByPath.put(getPathToParentFrame(component), component);
        }
        for (Iterator i = componentsByPath.keySet().iterator(); i.hasNext();) {
            String topPath = (String) i.next();
            while (i.hasNext()) {
                String subPath = (String) i.next();
                if (subPath.startsWith(topPath)) {
                    componentsToReload.remove(componentsByPath.get(subPath));
                    i.remove();
                }
            }
            i = componentsByPath.tailMap(topPath + "\0").keySet().iterator();
        }

        if (log.isDebugEnabled()) {
            debugUpdates("Filtered Updates");
        }
    }

    protected String getPathToParentFrame(SComponent component) {
        if (component == null)
            throw new IllegalArgumentException("Component must not be null!");

        SStringBuilder path = new SStringBuilder("/").append(component.getName());
        while (component.getParent() != null) {
            path = path.insert(0, "/").insert(1, component.getParent().getName());
            component = component.getParent();
        }
        return path.toString();
    }

    private class PathToParentComparator implements Comparator {

        public int compare(Object object1, Object object2) {
            String path1 = (String) object1;
            String path2 = (String) object2;
            int depthOfPath1 = path1.split("/").length;
            int depthOfPath2 = path2.split("/").length;
            if (depthOfPath1 < depthOfPath2) return -1;
            if (depthOfPath1 > depthOfPath2) return 1;
            return path1.compareTo(path2);
        }

    }

    private class PositioningComparator implements Comparator {

        public int compare(Object object1, Object object2) {
            Update update1 = (Update) object1;
            Update update2 = (Update) object2;
            if (update1.getPositioning() < update2.getPositioning()) return -1;
            if (update1.getPositioning() > update2.getPositioning()) return 1;
            return 0;
        }

    }

    private void debugReloads(String header) {
        int numberOfReloads = 0;
        log.debug("--- " + header + " ---");
        for (Iterator i = componentsToReload.iterator(); i.hasNext();) {
            log.debug(i.next());
            ++numberOfReloads;
        }
        log.debug("> " + numberOfReloads);
    }

    private void debugUpdates(String header) {
        int numberOfUpdates = 0;
        log.debug("--- " + header + " ---");
        for (Iterator i = updatesByComponent.keySet().iterator(); i.hasNext();) {
            StringBuilder output = new StringBuilder();
            SComponent component = (SComponent) i.next();
            output.append(component + ":");
            for (Iterator j = getUpdatesOfComponent(component).iterator(); j.hasNext();) {
                output.append(" " + j.next());
                ++numberOfUpdates;
            }
            log.debug(output.toString());
        }
        log.debug("> " + numberOfUpdates);
    }

}