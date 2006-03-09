package org.wings.event;

import org.wings.SPoint;
import org.wings.SComponent;

/**
 * @author hengels
 * @version $Revision$
 */
public class SMouseEvent
{
    protected int id;
    protected boolean consumed;
    protected SComponent component;
    protected SPoint point;

    public SMouseEvent(SComponent component, int id, SPoint point) {
        this.component = component;
        this.id = id;
        this.point = point;
    }

    public int getId() {
        return id;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void consume() {
        consumed = true;
    }

    public SComponent getComponent() {
        return component;
    }

    public void setComponent(SComponent component) {
        this.component = component;
    }

    public SPoint getPoint() {
        return point;
    }

    public void setPoint(SPoint point) {
        this.point = point;
    }

    public String toString() {
        return getClass().getName() + "[" + point + "] on " + component.getName();
    }
}
