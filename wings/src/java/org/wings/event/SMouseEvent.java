package org.wings.event;

import org.wings.SPoint;
import org.wings.SComponent;

import java.util.EventObject;

/**
 * A 'virtual' mouse event. To convert the inner <code>SPoint</code> to a sensefule meaning you should use the
 * according converions methods like {@link org.wings.STree#rowAtPoint(org.wings.SPoint)} or
 * {@link org.wings.STable#columnAtPoint(org.wings.SPoint)} .
 *
 * @author hengels
 * @author Benjamin Schmid <B.Schmid@exxcellent.de>
 */
public class SMouseEvent
    extends EventObject
{
    protected int id;
    protected boolean consumed;
    protected SComponent component;
    protected SPoint point;

    /**
     * Constructs a new mouse event
     * @param component source component
     * @param id id
     * @param point An SPoint. Intepretation is component dependent.
     */
    public SMouseEvent(SComponent component, int id, SPoint point) {
        super(component);
        this.component = component;
        this.id = id;
        this.point = point;
    }

    public int getId() {
        return id;
    }

    /**
     * Denotes if this event was already consumed.
     * @return <code>true</code> if the mouse event was consumed an no furter event processing should occur.
     */
    public boolean isConsumed() {
        return consumed;
    }

    /**
     * Call of this method inhibits the further dispatching of this mouse event.
     * I.e. if you want to avoid the default actions for a mouse click (like call of
     * normal table cell editor.
     */
    public void consume() {
        consumed = true;
    }

    /**
     * Gets the mouse event source component
     * @return The mouse event source component
     */
    public SComponent getComponent() {
        return component;
    }

    /**
     * Sets the mouse event source component.
     * @param component The mouse event source component
     */
    public void setComponent(SComponent component) {
        this.component = component;
    }

    /**
     * The object denoting where inside the source component a mouse click occured.
     * @return An virtual mouse click point. Intepretation is component dependent.
     */
    public SPoint getPoint() {
        return point;
    }

    /**
     * The object denoting where inside the source component a mouse click occured.
     * @param  point An virtual mouse click point. Intepretation is component dependent.
     */
    public void setPoint(SPoint point) {
        this.point = point;
    }

    public String toString() {
        return getClass().getName() + "[" + point + "] on " + component.getName();
    }
}
