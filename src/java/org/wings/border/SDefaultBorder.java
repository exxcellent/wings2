// (c) copyright 2006 by eXXcellent solutions, Ulm. Author: bschmid

package org.wings.border;

import java.awt.*;

import org.wings.SComponent;
import org.wings.style.CSSAttributeSet;

/**
 * If a default border is set, CGs will omit inline borders and paddings.
 * @author Benjamin Schmid <B.Schmid@exxcellent.de>
 */
public class SDefaultBorder
    extends SAbstractBorder
{
    public static final SDefaultBorder INSTANCE = new SDefaultBorder();

    private boolean locked = false;

    private SDefaultBorder() {
        locked = true;
    }

    /**
     * The default border is not modifyable, thus no component needs to be informed.
     * @param newComponent
     */
    public void setComponent(SComponent newComponent) {
    }

    /**
     * Return no attributes.
     * @return
     */
    public CSSAttributeSet getAttributes() {
        return null;
    }

    public void setInsets(Insets insets) {
        if (locked)
            throw new IllegalStateException("DefaultBorder is not configurable");
    }

    public void setColor(Color color, int position) {
        if (locked)
            throw new IllegalStateException("DefaultBorder is not configurable");
    }

    public void setThickness(int thickness, int position) {
        if (locked)
            throw new IllegalStateException("DefaultBorder is not configurable");
    }

    public void setStyle(String style, int position) {
        if (locked)
            throw new IllegalStateException("DefaultBorder is not configurable");
    }
}
