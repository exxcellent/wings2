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
import org.wings.plaf.ComponentCG;
import org.wings.plaf.Update;
import org.wings.util.SStringBuilder;

/**
 * Default implementation of the reload manager.
 *
 * @author Stephan Schuster
 */
public class DefaultReloadManager implements ReloadManager {

    private final transient static Log log = LogFactory.getLog(DefaultReloadManager.class);

    private int updateCount = 0;

    private boolean updateMode = false;

    private boolean deliveryPhase = false;

    protected final Set componentsToReload = new HashSet();

    protected final Map updatesByComponent = new HashMap();

    public void reload(SComponent component) {
        if (updateMode)
            addUpdate(component.getCG().getComponentUpdate(component));
        else
            componentsToReload.add(component);
    }

    public void addUpdate(Update update) {
        if (update == null) {
            String msg = "Update must not be null! \nEvery CG is required to implement the update " +
                    "methods imposed by the according interface under \"org.wings.plaf\". If a CG " +
                    "doesn't want to support fine grained updates, it may be sufficient to return " +
                    "\"null\" here. However, every CGs is expected to implement at least \"update(" +
                    "SComponent component)\" forced by the \"org.wings.plaf.ComponentCG\" interface.";
            throw new IllegalArgumentException(msg);
        } else if (update.getComponent() == null) {
            String msg = "Component must not be null! Every update belongs to a specific component.";
            throw new IllegalArgumentException(msg);
        }

        SComponent component = update.getComponent();

        if (!deliveryPhase) {
            PotentialUpdate potentialUpdate = new PotentialUpdate(update);
            Set updatesOfComponent = getUpdatesOfComponent(component);
            updatesOfComponent.remove(potentialUpdate);
            updatesOfComponent.add(potentialUpdate);
            updatesByComponent.put(component, updatesOfComponent);

            if ((update.getProperty() & Update.AFFECTS_COMPLETE_COMPONENT)
                    == Update.AFFECTS_COMPLETE_COMPONENT) {
                componentsToReload.add(component);
            }
        } else if (log.isDebugEnabled()) {
            log.debug("Component " + component + " changed during delivery phase.");
        }
    }

    public List getUpdates() {
        filterUpdates();

        List filteredUpdates = new ArrayList();
        for (Iterator i = updatesByComponent.values().iterator(); i.hasNext();) {
            filteredUpdates.addAll((Set) i.next());
        }
        Collections.sort(filteredUpdates, getOrderOfUpdates());

        return filteredUpdates;
    }

    public Set getDirtyComponents() {
        if (updateMode)
            return updatesByComponent.keySet();
        else
            return componentsToReload;
    }

    public Set getDirtyFrames() {
        final Set dirtyFrames = new HashSet(5);
        for (Iterator i = getDirtyComponents().iterator(); i.hasNext();) {
            SFrame parentFrame = ((SComponent) i.next()).getParentFrame();
            if (parentFrame != null)
                dirtyFrames.add(parentFrame);
        }
        return dirtyFrames;
    }

    public void invalidateFrames() {
        Iterator i = getDirtyFrames().iterator();
        while (i.hasNext()) {
            ((SFrame) i.next()).invalidate();
            i.remove();
        }
        deliveryPhase = true;
    }

    public void notifyCGs() {
        for (Iterator i = getDirtyComponents().iterator(); i.hasNext();) {
            SComponent component = (SComponent) i.next();
            ComponentCG componentCG = component.getCG();
            if (componentCG != null)
                componentCG.componentChanged(component);
        }
    }

    public void clear() {
        updateCount = 0;
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

    protected Comparator getOrderOfUpdates() {
        return
            new CombinedComparator(
                new InverseComparator(new PriorityComparator()),
                new PositionComparator()
            );
    }

    protected void filterUpdates() {
        if (log.isDebugEnabled())
            printAllUpdates("potential updates");

        for (Iterator i = updatesByComponent.keySet().iterator(); i.hasNext();) {
            SComponent component = (SComponent) i.next();
            if (!component.isRecursivelyVisible() || component.getParentFrame() == null) {
                i.remove();
                componentsToReload.remove(component);
            }
        }

        SortedMap componentsByPath = new TreeMap(new PathComparator());
        for (Iterator i = componentsToReload.iterator(); i.hasNext();) {
            SComponent component = (SComponent) i.next();
            componentsByPath.put(calculatePath(component), component);
        }
        for (Iterator i = componentsByPath.keySet().iterator(); i.hasNext();) {
            String topPath = (String) i.next();
            retainUpdatesWithProperty(
                    getUpdatesOfComponent((SComponent) componentsByPath.get(topPath)),
                    Update.AFFECTS_COMPLETE_COMPONENT);
            while (i.hasNext()) {
                String subPath = (String) i.next();
                if (subPath.startsWith(topPath)) {
                    componentsToReload.remove(componentsByPath.get(subPath));
                    updatesByComponent.keySet().remove(componentsByPath.get(subPath));
                    i.remove();
                }
            }
            i = componentsByPath.tailMap(topPath + "\0").keySet().iterator();
        }

        if (log.isDebugEnabled())
            printAllUpdates("effective updates");
    }

    private String calculatePath(SComponent component) {
        SStringBuilder path = new SStringBuilder("/").append(component.getName());
        while (component.getParent() != null) {
            path = path.insert(0, "/").insert(1, component.getParent().getName());
            component = component.getParent();
        }
        return path.toString();
    }

    private void removeUpdatesWithProperty(Iterable iterable, int propertyMask) {
        Iterator i = iterable.iterator();
        while (i.hasNext()) {
            Update update = (Update) i.next();
            if ((update.getProperty() & propertyMask) == propertyMask)
                i.remove();
        }
    }

    private void retainUpdatesWithProperty(Iterable iterable, int propertyMask) {
        Iterator i = iterable.iterator();
        while (i.hasNext()) {
            Update update = (Update) i.next();
            if ((update.getProperty() & propertyMask) != propertyMask)
                i.remove();
        }
    }

    private void printAllUpdates(String header) {
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

    private final class PotentialUpdate implements Update {

        private Update update;
        private int position;

        public PotentialUpdate(Update update) {
            this.update = update;
            this.position = updateCount++;
        }

        public SComponent getComponent() {
            return update.getComponent();
        }

        public Handler getHandler() {
            return update.getHandler();
        }

        public int getProperty() {
            return update.getProperty();
        }

        public int getPriority() {
            return update.getPriority();
        }

        public boolean equals(Object object) {
            if (object == this)
                return true;
            if (object == null || object.getClass() != this.getClass())
                return false;

            PotentialUpdate other = (PotentialUpdate) object;

            return update.equals(other.update);
        }

        public int hashCode() {
            return update.hashCode();
        }

        public String toString() {
            String clazz = update.getClass().getName();
            int index = clazz.lastIndexOf("$");
            if (index < 0)
                index = clazz.lastIndexOf(".");
            return clazz.substring(++index) + "[" + getPriority() + "|" + getPosition() + "]";
        }

        public int getPosition() {
            return position;
        }

    }

    private static class PathComparator implements Comparator {

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

    private static class PositionComparator implements Comparator {

        public int compare(Object object1, Object object2) {
            PotentialUpdate update1 = (PotentialUpdate) object1;
            PotentialUpdate update2 = (PotentialUpdate) object2;
            if (update1.getPosition() < update2.getPosition()) return -1;
            if (update1.getPosition() > update2.getPosition()) return 1;
            return 0;
        }

    }

    private static class PriorityComparator implements Comparator {

        public int compare(Object object1, Object object2) {
            PotentialUpdate update1 = (PotentialUpdate) object1;
            PotentialUpdate update2 = (PotentialUpdate) object2;
            if (update1.getPriority() < update2.getPriority()) return -1;
            if (update1.getPriority() > update2.getPriority()) return 1;
            return 0;
        }

    }

    private static class CombinedComparator implements Comparator {

        private Comparator comparator1;
        private Comparator comparator2;

        public CombinedComparator(Comparator c1, Comparator c2) {
            this.comparator1 = c1;
            this.comparator2 = c2;
        }

        public int compare(Object object1, Object object2) {
            int result = comparator1.compare(object1, object2);
            if (result == 0)
                return comparator2.compare(object1, object2);
            else
                return result;
        }
    }

    private static class InverseComparator implements Comparator {

        private Comparator comparator;

        public InverseComparator(Comparator c) {
            this.comparator = c;
        }

        public int compare(Object object1, Object object2) {
            return -comparator.compare(object1, object2);
        }
    }

}