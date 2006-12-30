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

    private boolean acceptChanges = true;

    protected final Set componentsToReload = new HashSet();

    protected final Map fullReplaceUpdates = new HashMap();

    protected final Map fineGrainedUpdates = new HashMap();

    public void reload(SComponent component) {
        if (component == null)
            throw new IllegalArgumentException("Component must not be null!");

        if (updateMode)
            addUpdate(component, null);
        else
            componentsToReload.add(component);
    }

    public void addUpdate(SComponent component, Update update) {
        if (component == null)
            throw new IllegalArgumentException("Component must not be null!");

        if (update == null) {
            update = component.getCG().getComponentUpdate(component);
            if (update == null) {
                SFrame frame = component.getParentFrame();
                if (frame != null)
                    fullReplaceUpdates.put(frame, null);
                return;
            }
        }

        component = update.getComponent();

        if (acceptChanges) {
            PotentialUpdate potentialUpdate = new PotentialUpdate(update);

            if ((update.getProperty() & Update.FULL_REPLACE_UPDATE) == Update.FULL_REPLACE_UPDATE) {
                fullReplaceUpdates.put(component, potentialUpdate);
            } else {
                Set potentialUpdates = getFineGrainedUpdates(component);
                potentialUpdates.remove(potentialUpdate);
                potentialUpdates.add(potentialUpdate);
                fineGrainedUpdates.put(component, potentialUpdates);
            }
        } else if (log.isDebugEnabled()) {
            log.debug("Component " + component + " changed after invalidation of frames.");
        }
    }

    public List getUpdates() {
        filterUpdates();

        List filteredUpdates = new ArrayList(fullReplaceUpdates.values());
        for (Iterator i = fineGrainedUpdates.values().iterator(); i.hasNext();) {
            filteredUpdates.addAll((Set) i.next());
        }
        Collections.sort(filteredUpdates, getUpdateComparator());

        return filteredUpdates;
    }

    public Set getDirtyComponents() {
        final Set dirtyComponents = new HashSet();

        if (updateMode) {
            dirtyComponents.addAll(fullReplaceUpdates.keySet());
            dirtyComponents.addAll(fineGrainedUpdates.keySet());
        } else {
            dirtyComponents.addAll(componentsToReload);
        }

        return dirtyComponents;
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
        acceptChanges = false;
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
        acceptChanges = true;
        componentsToReload.clear();
        fullReplaceUpdates.clear();
        fineGrainedUpdates.clear();
    }

    public boolean isUpdateMode() {
        return updateMode;
    }

    public void setUpdateMode(boolean updateMode) {
        this.updateMode = updateMode;
    }

    public boolean isReloadRequired(SFrame frame) {
        if (updateMode)
            return fullReplaceUpdates.containsKey(frame);
        else
            return true;
    }

    protected Set getFineGrainedUpdates(SComponent component) {
        Set potentialUpdates = (Set) fineGrainedUpdates.get(component);
        if (potentialUpdates == null) {
            potentialUpdates = new HashSet(5);
        }
        return potentialUpdates;
    }

    protected void filterUpdates() {
        if (log.isDebugEnabled())
            printAllUpdates("potential updates");

        fineGrainedUpdates.keySet().removeAll(fullReplaceUpdates.keySet());

        SortedMap componentHierarchy = new TreeMap(new PathComparator());

        for (Iterator i = getDirtyComponents().iterator(); i.hasNext();) {
            SComponent component = (SComponent) i.next();
            if ((!component.isRecursivelyVisible() && !(component instanceof SMenu)) ||
                    component.getParentFrame() == null) {
                fullReplaceUpdates.remove(component);
                fineGrainedUpdates.remove(component);
            } else {
                componentHierarchy.put(getHierarchyPath(component), component);
            }
        }

        for (Iterator i = componentHierarchy.keySet().iterator(); i.hasNext();) {
            String topPath = (String) i.next();
            if (fullReplaceUpdates.containsKey(componentHierarchy.get(topPath))) {
                while (i.hasNext()) {
                    String subPath = (String) i.next();
                    if (subPath.startsWith(topPath)) {
                        fullReplaceUpdates.remove(componentHierarchy.get(subPath));
                        fineGrainedUpdates.remove(componentHierarchy.get(subPath));
                        i.remove();
                    }
                }
            }
            i = componentHierarchy.tailMap(topPath + "\0").keySet().iterator();
        }

        if (log.isDebugEnabled())
            printAllUpdates("effective updates");
    }

    private String getHierarchyPath(SComponent component) {
        SStringBuilder path = new SStringBuilder("/").append(component.getName());
        if (component instanceof SMenuItem) {
            SMenuItem menuItem = (SMenuItem) component;
            while (menuItem.getParentMenu() != null) {
                SComponent parentMenu = menuItem.getParentMenu();
                path = path.insert(0, "/").insert(1, parentMenu.getName());
                if (parentMenu instanceof SMenuItem) {
                    menuItem = (SMenuItem) parentMenu;
                } else {
                    break;
                }
            }
        } else {
            while (component.getParent() != null) {
                path = path.insert(0, "/").insert(1, component.getParent().getName());
                component = component.getParent();
            }
        }
        return path.toString();
    }

    private void removeUpdates(Iterable iterable, int propertyMask) {
        Iterator i = iterable.iterator();
        while (i.hasNext()) {
            Update update = (Update) i.next();
            if ((update.getProperty() & propertyMask) == propertyMask)
                i.remove();
        }
    }

    private void retainUpdates(Iterable iterable, int propertyMask) {
        Iterator i = iterable.iterator();
        while (i.hasNext()) {
            Update update = (Update) i.next();
            if ((update.getProperty() & propertyMask) != propertyMask)
                i.remove();
        }
    }

    private boolean containsUpdate(Iterable iterable, int propertyMask) {
        Iterator i = iterable.iterator();
        while (i.hasNext()) {
            Update update = (Update) i.next();
            if ((update.getProperty() & propertyMask) == propertyMask)
                return true;
        }
        return false;
    }

    private void printAllUpdates(String header) {
        int numberOfUpdates = 0;
        log.debug("--- " + header + " ---");
        for (Iterator i = getDirtyComponents().iterator(); i.hasNext();) {
            StringBuilder output = new StringBuilder();
            SComponent component = (SComponent) i.next();
            output.append(component + ":");
            if (fullReplaceUpdates.containsKey(component)) {
                output.append(" " + fullReplaceUpdates.get(component));
                if (fullReplaceUpdates.get(component) == null)
                    output.append(" [no component update supported --> reload frame!!!]");
                ++numberOfUpdates;
            }
            for (Iterator j = getFineGrainedUpdates(component).iterator(); j.hasNext();) {
                output.append(" " + j.next());
                ++numberOfUpdates;
            }
            log.debug(output.toString());
        }
        log.debug("--> " + numberOfUpdates);
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

        public int getPosition() {
            return position;
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

    }

    private Comparator getUpdateComparator() {
        return
            new CombinedComparator(
                new InverseComparator(new PriorityComparator()),
                new PositionComparator()
            );
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
