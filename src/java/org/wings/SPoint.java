package org.wings;

import java.io.Serializable;

/**
 * A <b>virtual mouse</b> point. In Swing the {@link java.awt.Point} class denotes a
 * mouse coordinate. In web applications we cannot determine the mouse click coordinates
 * but the targeted virtual coordinate. Hence the content of this point is dependend
 * on the concrete usage context.
 *
 * @see STable#rowAtPoint(SPoint)
 * @see STable#columnAtPoint(SPoint)
 * @see org.wings.STree#rowAtPoint(SPoint) 
 *
 * @author hengels
 * @author Benjamin Schmid <B.Schmid@exxcellent.de>
 */
public class SPoint implements Serializable {
    private String coordinates;

    public SPoint(String coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * The 'conceptual' coordinates. Content depends on source component. Look in source component for conversion methods.
     * @return The 'conceptual' coordinates
     */
    String getCoordinates() {
        return coordinates;
    }

    /**
     * The 'conceptual' coordinates. Content depends on source component. Look in source component for conversion methods.
     * @param coordinates 'conceptual' coordinates
     */
    void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String toString() {
        return coordinates;
    }
}
