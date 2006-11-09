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

/**
 * JavaScript DOM events.
 * @author Christian Schyma
 */
public interface JavaScriptDOMEvent extends JavaScriptEvent {
    
    /**
     * special YUI library DOM event, see http://developer.yahoo.com/yui/event/#onavailable
     * onAvailable lets you define a function that will execute as soon as 
     * an element is detected in the DOM. The intent is to reduce the 
     * occurrence of timing issues when rendering script and html inline. 
     * It is not meant to be used to define handlers for elements that may 
     * eventually be in the document; it is meant to be used to detect 
     * elements you are in the process of loading.
     */
    public static final String ON_AVAILABLE = "onavailable";
    
}
