/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings;

import org.wings.io.Device;
import org.wings.plaf.DesktopPaneCG;
import org.wings.plaf.DesktopPaneCG;

import java.io.IOException;

/**
 * A DesktopPane holds SInternalFrames.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SDesktopPane
        extends SContainer
        implements SConstants {
    SDesktopLayout layout = new SDesktopLayout();

    public SDesktopPane() {
        super();
        super.setLayout(layout);
    }

    public void setLayout(SLayoutManager l) {}

    /**
     * @param component   The internal frame to be added.
     * @param constraints nothing
     */
    public SComponent addComponent(SComponent component,
                                   Object constraints, int index) {
        if (constraints == null)
            constraints = component.getLowLevelEventId();
        return super.addComponent(component, constraints, index);
    }

    /**
     * Sets the position for the specified component.
     *
     * @param c        the Component to set the layer for
     * @param position an int specifying the position, where
     *                 0 is the topmost position and
     *                 -1 is the bottommost position
     */
    public void setPosition(SComponent c, int position) {
        getComponentList().remove(c);
        getComponentList().add(position, c);
    }

    /**
     * Returns the index of the specified Component.
     * This is the absolute index, ignoring layers.
     * Index numbers, like position numbers, have the topmost component
     * at index zero. Larger numbers are closer to the bottom.
     *
     * @param c the Component to check
     * @return an int specifying the component's index
     */
    public int getIndexOf(SComponent c) {
        int i, count;

        count = getComponentCount();
        for (i = 0; i < count; i++) {
            if (c == getComponent(i))
                return i;
        }
        return -1;
    }

    /**
     * Get the position of the component.
     *
     * @param c the Component to check
     * @return an int giving the component's position, where 0 is the
     *         topmost position and the highest index value = the count
     *         count of components minus 1
     * @see #getComponentCountInLayer
     */
    public int getPosition(SComponent c) {
        return getIndexOf(c);
    }

    private class SDesktopLayout extends SAbstractLayoutManager {
        private SContainer container = null;

        public SDesktopLayout() {}

        public void updateCG() {}

        public void addComponent(SComponent c, Object constraint, int index) {}

        public void removeComponent(SComponent c) {}

        public SComponent getComponentAt(int i) {
            return (SComponent) container.getComponent(i);
        }

        public void setContainer(SContainer c) {
            container = c;
        }

        // ** FIXME: move to plaf ..
        public void write(Device d)
                throws IOException {
            d.print("<table cellpadding=\"0\" cellspacing=\"7\" border=\"0\" width=\"100%\">\n");

            int componentCount = getComponentCount();
            // hack ? einfach nur das erste maximized ausgeben, oder was ?
            // das funktioniert nur unter der Annhame, dass der User nicht
            // zwei maximieren kann. Vielleicht sollte das ueber eine
            // button-group oder so gemacht werden ..
            for (int i = 0; i < componentCount; i++) {
                SInternalFrame frame = (SInternalFrame) container.getComponent(i);
                if (!frame.isClosed() && frame.isMaximized()) {
                    d.print("<tr><td>\n");
                    frame.write(d);
                    d.print("</td></tr></table>\n");
                    return;
                }
            }

            for (int i = 0; i < componentCount; i++) {
                SInternalFrame frame = (SInternalFrame) container.getComponent(i);
                if (!frame.isClosed()) {
                    d.print("<tr><td>\n");
                    frame.write(d);
                    d.print("</td></tr>\n");
                }
            }
            d.print("</table>\n");
        }
    }

    public void setCG(DesktopPaneCG cg) {
        super.setCG(cg);
    }
}


