package org.wings.plaf.css;

import org.wings.SComponent;
import org.wings.plaf.Update;

public abstract class AbstractUpdate implements Update {

	protected SComponent component;

	public AbstractUpdate(SComponent component) {
        if (component == null)
            throw new IllegalArgumentException("Component must not be null!");

		this.component = component;
	}

	public SComponent getComponent() {
		return component;
	}

    public int getProperty() {
        return FINE_GRAINED_UPDATE;
    }

    public int getPriority() {
        return 1;
    }

	public abstract Handler getHandler();

    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (object == null || object.getClass() != this.getClass())
            return false;

        Update other = (Update) object;

        if (!this.getComponent().equals(other.getComponent()))
            return false;
        if (this.getProperty() != other.getProperty())
            return false;
        if (this.getPriority() != other.getPriority())
            return false;

        return true;
    }

    public int hashCode() {
        int hashCode = 17;
        int dispersionFactor = 37;

        hashCode = hashCode * dispersionFactor + this.getComponent().hashCode();
        hashCode = hashCode * dispersionFactor + this.getProperty();
        hashCode = hashCode * dispersionFactor + this.getPriority();

        return hashCode;
    }

}