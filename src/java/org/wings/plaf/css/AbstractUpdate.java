package org.wings.plaf.css;

import org.wings.SComponent;
import org.wings.plaf.Update;

abstract class AbstractUpdate implements Update {

    protected static long instanceCounter = 1;

	protected SComponent component;
    protected long positioningIndex;

	public AbstractUpdate(SComponent component) {
        if (component == null)
            throw new IllegalArgumentException("Component must not be null!");

		this.component = component;
        positioningIndex = instanceCounter++;
	}

	public SComponent getComponent() {
		return component;
	}

	public long getPositioning() {
		return positioningIndex;
	}

	public int getProperty() {
		return STANDARD_UPDATE;
	}

	public abstract Handler getHandler();

    public String toString() {
        String clazz = getClass().getName();
        int index = clazz.lastIndexOf("$");
        if (index < 0)
            index = clazz.lastIndexOf(".");
        return clazz.substring(++index) + "[" + getPositioning() + "]";
    }

	public boolean equals(Object object) {
		if (object == this)
			return true;
		if (object == null || object.getClass() != this.getClass())
			return false;

		Update update = (Update) object;

        if ((this.getProperty() & MULTIPLE_UPDATE) == MULTIPLE_UPDATE)
            return false;

        if (!this.getComponent().equals(update.getComponent()))
            return false;
        if (!this.getHandler().getName().equals(update.getHandler().getName()))
            return false;
        if (this.getProperty() != update.getProperty())
            return false;

        return true;
	}

    public int hashCode() {
        int hashCode = 17;
        int dispersionFactor = 37;

        hashCode = hashCode * dispersionFactor + this.getComponent().hashCode();
        hashCode = hashCode * dispersionFactor + this.getHandler().getName().hashCode();
        hashCode = hashCode * dispersionFactor + this.getProperty();

        return hashCode;
    }

}