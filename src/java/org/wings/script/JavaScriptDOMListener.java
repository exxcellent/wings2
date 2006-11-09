/*
 * Copyright 2006 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
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

/**
 * Specialized ScriptListener for DOM events. The original JavaScriptListener
 * uses inline events (e.g. <p onclick="alert('huzza!')">), this one the events of
 * the DOM.
 * @author Christian Schyma
 */
public class JavaScriptDOMListener implements ScriptListener {
    
    private String event;
    private String code;    
    private int priority = DEFAULT_PRIORITY;
    
    /**
     * @param event one of JavaScriptDOMEvent (e.g. JavaScriptDOMEvent.ON_CLICK)
     * @param code the code that is written as a value of the event attribute
     */
    public JavaScriptDOMListener(String event, String code) {
        this.event = event;
        this.code = code;
    }

    public String getEvent() {
        return event;
    }

    public String getCode() {
        return code;
    }

    public String getScript() {
        return null;
    }

    public int getPriority() {        
        return priority;
    }

    /**
     * Modifies the scripting priority of this script.
     *
     * @param priority New priority as describe inn {@link org.wings.script.ScriptListener#getPriority()}
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    /**
     * Returns executable code to initialize the JavaScript listener for the
     * given component.
     * @param component component to initialze listener for
     * @return init code
     */        
    public String getInitCode(SComponent component) {
        String elementId = "'"+ component.getName() +"'";
        
        if (this.event.compareTo(JavaScriptDOMEvent.ON_AVAILABLE) == 0) {
            
            String initCode = "YAHOO.util.Event.onAvailable("+
                    elementId +", "+
                    this.getCode() +"); ";
            return initCode;
            
        } else {
            
            // the 'on' has to be removed for W3C DOM Event Handling
            // e.g. 'onload' becomes 'load''
            String modifiedEventName = this.getEvent();
            if (modifiedEventName.startsWith("on")) {
                modifiedEventName = modifiedEventName.substring(2);
            }
            
            // some events are only registerable to special browser objects
            if (modifiedEventName.compareTo("load") == 0)
                elementId = "window";
            else if (modifiedEventName.compareTo("resize") == 0)
                elementId = "window";
            else if (modifiedEventName.compareTo("scroll") == 0)
                elementId = "window";
            else if (modifiedEventName.compareTo("focus") == 0)
                elementId = "document";
            
            String initCode = "YAHOO.util.Event.addListener("+
                    elementId +", "+
                    "'"+ modifiedEventName +"', "+
                    this.getCode() +");";
            
            return initCode;
            
        }
    }   
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        /* only checking for instanceof, not exact class, so we don't
         * need to implement this in inherited classes
         */
        if (!(obj instanceof JavaScriptDOMListener)) {
            return false;
        }
        JavaScriptDOMListener testObj = (JavaScriptDOMListener) obj;

        if (testObj.getEvent() == null) {
            if (getEvent() != null) {
                return false;
            }
        } else {
            if (!testObj.getEvent().equals(getEvent())) {
                return false;
            }
        }

        if (testObj.getCode() == null) {
            if (getCode() != null) {
                return false;
            }
        } else {
            if (!testObj.getCode().equals(getCode())) {
                return false;
            }
        }        

        if (testObj.getScript() == null) {
            if (getScript() != null) {
                return false;
            }
        } else {
            if (!testObj.getScript().equals(getScript())) {
                return false;
            }
        }                

        return true;
    }

    public int hashCode() {
        return code != null ? code.hashCode() : super.hashCode();
    }
 
}
