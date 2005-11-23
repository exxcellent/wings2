package org.wings;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a LayoutManager which just writes the components one after another.
 * 
 * @author ole
 *
 */
public class SNullLayout extends SAbstractLayoutManager {

    protected ArrayList components = new ArrayList(2);

    public void addComponent(SComponent c, Object constraint, int index) {
        components.add(index, c);
    }

    public void removeComponent(SComponent c) {
        components.remove(c);
    }

    /**
     * returns a list of all components
     *
     * @return all components
     */
    public List getComponents() {
        return components;
    }

    /**
     * returns the component at the given position
     *
     * @param i position
     * @return component
     */
    public SComponent getComponentAt(int i) {
        return (SComponent) components.get(i);
    }


}
