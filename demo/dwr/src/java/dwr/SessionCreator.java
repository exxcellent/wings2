/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package dwr;

import uk.ltd.getahead.dwr.Creator;

import org.w3c.dom.Element;

/**
 * @author hengels
 * @version $Revision$
 */
public class SessionCreator implements Creator
{
    private Object callable;

    public SessionCreator(Object callable) {
        this.callable = callable;
    }

    public void init(Element config) {
    }

    public Class getType() {
        return callable.getClass();
    }

    public Object getInstance() {
        return callable;
    }
}
