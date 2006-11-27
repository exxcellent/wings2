// (c) copyright 2006 by eXXcellent solutions, Ulm. Author: bschmid

package org.wings.border;

import java.awt.*;

import org.wings.SComponent;

/**
 * This is a place-holder for the 'default' border of an CG for selected components.
 * <p>
 * For example {@link org.wings.SButton} typically render by default as bevelled clickables (via default CSS).
 * If you want to have a button with only an icon and any default border just set the border to <code>null</code>.
 * The CG will ommit any default rendered borders.
 *
 * @author Benjamin Schmid <B.Schmid@exxcellent.de>
 */
public class SDefaultBorder extends SAbstractBorder {

    /**
     * Shared immutable instance.
     */
    public static final SDefaultBorder DEFAULT = new ImmutableSDefaultBorder(); 

    public SDefaultBorder() {
    }

    /**
     * Constructs a new default border
     * @param insets The desired insets / paddings.
     * @see #setInsets(java.awt.Insets)
     */
    public SDefaultBorder(Insets insets) {
        super(insets);
    }

    /**
     * Constructs a new default border
     * @param top top padding in px
     * @param left left padding in px
     * @param bottom bottom padding in px
     * @param right right padding in px
     */
    public SDefaultBorder(int top, int left, int bottom, int right) {
        this(new Insets(top, left, bottom, right));
    }

    private static class ImmutableSDefaultBorder extends SDefaultBorder {
        private boolean locked;

        public ImmutableSDefaultBorder() {
            super();
            locked = true;
        }

        public void setComponent(SComponent newComponent) {
            // super.setComponent(newComponent); ommit as this can be shared!
        }

        public void setInsets(Insets insets) {
            if (locked)
                throw new IllegalStateException("Immutable border. Please use your own instance");
            super.setInsets(insets);
        }

        public void setColor(Color color, int position) {
            if (locked)
                throw new IllegalStateException("Immutable border. Please use your own instance");
            super.setColor(color, position);
        }

        public void setThickness(int thickness, int position) {
            if (locked)
                throw new IllegalStateException("Immutable border. Please use your own instance");
            super.setThickness(thickness, position);
        }

        public void setStyle(String style, int position) {
            if (locked)
                throw new IllegalStateException("Immutable border. Please use your own instance");
            super.setStyle(style, position);
        }
    }

}
