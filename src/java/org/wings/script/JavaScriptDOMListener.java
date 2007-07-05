/*
 * Copyright 2006 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.script;

import org.wings.SComponent;
import org.wings.util.SStringBuilder;

/**
 * Specialized ScriptListener for DOM events. The original JavaScriptListener
 * uses inline events (e.g. <p onclick="alert('huzza!')">), this one the events of
 * the DOM.
 * @author Christian Schyma
 */
public class JavaScriptDOMListener extends JavaScriptListener {

    /**
     * object to override default event scope
     */
    private String customObject = null;

    /**
     * the component this listener is associated with
     */
    private SComponent component;

    /**
     * @param event one of JavaScriptDOMEvent (e.g. JavaScriptDOMEvent.ON_CLICK) to act on
     * @param code callback function fired on event
     * @param component the component this listener is associated with
     */
    public JavaScriptDOMListener(String event, String code, SComponent component) {
        super(event, code);
        this.component = component;
    }

    /**
     * @param event one of JavaScriptDOMEvent (e.g. JavaScriptDOMEvent.ON_CLICK) to act on
     * @param code callback function fired on event
     * @param customObject object that is used for the execution scope (so it becomes "this" in the callback) instead of
     * the default execution scope of the element the event was fired upon
     * @param component the component this listener is associated with
     */
    public JavaScriptDOMListener(String event, String code, String customObject, SComponent component) {
        super(event, code);
        this.customObject = customObject;
        this.component = component;
    }

    /**
     * Returns executable code to initialize the JavaScript listener for the
     * given component.
     * @return init code
     */
    public String getScript() {
        SStringBuilder elementId = new SStringBuilder();
        elementId
                .append("'")
                .append(component.getName())
                .append("'");

        // special Yahoo UI event, see JavaScriptDOMEvent.ON_AVAILABLE docu for more
        if (this.getEvent().compareTo(JavaScriptEvent.ON_AVAILABLE) == 0) {

            SStringBuilder initCode = new SStringBuilder();
            initCode
                    .append("YAHOO.util.Event.onAvailable(")
                    .append(elementId).append(", ")
                    .append(this.getCode()).append("); ");
            return initCode.toString();

        } else {

            // the 'on' has to be removed for W3C DOM Event Handling
            // e.g. 'onload' becomes 'load''
            String modifiedEventName = this.getEvent();
            if (modifiedEventName.startsWith("on")) {
                modifiedEventName = modifiedEventName.substring(2);
            }

            // some events are only registerable to special browser objects
            if (modifiedEventName.compareTo("load") == 0)
                elementId.delete(0, elementId.length()).append("window");
            else if (modifiedEventName.compareTo("resize") == 0)
                elementId.delete(0, elementId.length()).append("window");
            else if (modifiedEventName.compareTo("scroll") == 0)
                elementId.delete(0, elementId.length()).append("window");
            else if (modifiedEventName.compareTo("focus") == 0)
                elementId.delete(0, elementId.length()).append("document");

            SStringBuilder initCode = new SStringBuilder();
            initCode
                    .append("YAHOO.util.Event.addListener(")
                    .append(elementId).append(", ")
                    .append("'").append(modifiedEventName).append("', ")
                    .append(this.getCode());

            // modifies the scope of the callback function
            if (this.customObject != null) {
                initCode
                    .append(", ").append(customObject)
                    .append(", true");
            }

            initCode.append(");");

            return initCode.toString();

        }
    }

}
