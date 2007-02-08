package org.wings.util;

import org.wings.session.SessionManager;

/**
 * A helper class for storing session local variables similar to {@link ThreadLocal} for thread local variables.
 */
public class SessionLocal<T> {

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
    private static class ValueWrapper<T> {
        T value;
    }

    /**
     * return the current value of this variable from the session
     *
     * @return The current value or the result of {@link #initialValue()} if called for the first time.
     */
    public T get() {
        ValueWrapper<T> valueWrapper = (ValueWrapper<T>) SessionManager.getSession().getProperty(this.propertyName);
        /*
         * null means that the property is being used for the first time this session,
         * initialize the value, which may be null.
         */
        if (valueWrapper == null) {
            T value = initialValue();
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
     * @return The stored value
     */
    protected T initialValue() {
        return null;
    }

    /**
     * Set the value.  the value which is set may be null,
     * but the object set is never null because it is wrapped.
     *
     * @param value Value. <code>null</code> is also a valid value.
     */
    public void set(T value) {
        ValueWrapper<T> valueWrapper = new ValueWrapper<T>();
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
