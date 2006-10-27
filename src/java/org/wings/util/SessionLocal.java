package org.wings.util;

import org.wings.session.SessionManager;

public class SessionLocal {

    private final String propertyName;

    public SessionLocal() {
        this.propertyName = getClass().getName() + "." + System.identityHashCode(this);
    }

    /**
     * use a wrapper for the value so that it is possible
     * to get and set a null value, and also to determine
     * whether the property has been initialized
     * for the current session
     */
    private class ValueWrapper {
        Object value;
    }

    /**
     * return the current value of this variable from the session
     *
     * @return
     */
    public Object get() {
        ValueWrapper valueWrapper = (ValueWrapper) SessionManager.getSession().getProperty(this.propertyName);
        /*
         * null means that the property is being used for the first time this session,
         * initialize the value, which may be null.
         */
        if (valueWrapper == null) {
            Object value = initialValue();
            set(value);
            return value;
        }
        else {
            return valueWrapper.value;
        }
    }

    /**
     * override this method to get the initial value for a new session
     *
     * @return
     */
    protected Object initialValue() {
        return null;
    }

    /**
     * set the value.  the value which is set may be null,
     * but the object set is never null because it is wrapped.
     *
     * @param value
     */
    public void set(Object value) {
        ValueWrapper valueWrapper = new ValueWrapper();
        valueWrapper.value = value;
        SessionManager.getSession().setProperty(this.propertyName, valueWrapper);
    }

    /**
     * remove the value,
     * if get is called, the value will be reinitialized
     */
    public void remove() {
        SessionManager.getSession().setProperty(this.propertyName, null);
    }

}
