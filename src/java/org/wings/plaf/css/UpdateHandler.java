package org.wings.plaf.css;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.wings.plaf.Update;

public class UpdateHandler implements Update.Handler {

    protected String name;
    protected List<String> parameters;

    public UpdateHandler(String name) {
        if (name == null)
            throw new IllegalArgumentException("Handler name must not be null!");

        this.name = name;
        parameters = new ArrayList<String>(5);
    }

    public String getName() {
        return "wingS.update." + name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addParameter(Object o) {
        parameters.add(Utils.encodeJS(o));
    }

    public Iterator getParameters() {
        return parameters.iterator();
    }

    public Object getParameter(int index) {
        return parameters.get(index);
    }

    public Object removeParameter(int index) {
        return parameters.remove(index);
    }

    public void addParameter(int index, Object o) {
        parameters.add(index, Utils.encodeJS(o));
    }

    public Object setParameter(int index, Object o) {
        return parameters.set(index, Utils.encodeJS(o));
    }

    public String toString() {
        return getClass().getName() + "[" + name + "]";
    }

    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (object == null || object.getClass() != this.getClass())
            return false;

        UpdateHandler handler = (UpdateHandler) object;

        if (!this.getName().equals(handler.getName()))
            return false;
        if (!parameters.equals(handler.parameters))
            return false;

        return true;
    }

}