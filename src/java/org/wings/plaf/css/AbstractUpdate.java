package org.wings.plaf.css;

import org.wings.SComponent;
import org.wings.plaf.Update;

abstract class AbstractUpdate implements Update {

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
        return DEFAULT_INCREMENTAL_UPDATE;
    }

    public int getPriority() {
        return 0;
    }

	public abstract Handler getHandler();

}