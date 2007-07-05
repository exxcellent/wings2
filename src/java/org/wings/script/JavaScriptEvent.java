/*
 * Copyright 2000,2005 wingS development team.
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

/**
 * Some widely implemented JavaScript events.
 *
 * @author Holger Engels
 */
public interface JavaScriptEvent {

    // STANDARD JAVASCRIPT EVENTS

    public static final String ON_BLUR = "onblur";
    public static final String ON_CLICK = "onclick";
    public static final String ON_DBLCLICK = "ondblclick";
    public static final String ON_CHANGE = "onchange";
    public static final String ON_FOCUS = "onfocus";
    public static final String ON_KEY_DOWN = "onkeydown";
    public static final String ON_KEY_PRESS = "onkeypress";
    public static final String ON_KEY_UP = "onkeyup";
    public static final String ON_LOAD = "onload";
    public static final String ON_MOUSE_DOWN = "onmousedown";
    public static final String ON_MOUSE_MOVE = "onmousemove";
    public static final String ON_MOUSE_OUT = "onmouseout";
    public static final String ON_MOUSE_OVER = "onmouseover";
    public static final String ON_MOUSE_UP = "onmouseup";
    public static final String ON_RESET = "onreset";
    public static final String ON_RESIZE = "onresize";
    public static final String ON_SCROLL = "onscroll";
    public static final String ON_SELECT = "onselect";
    public static final String ON_SUBMIT = "onsubmit";
    public static final String ON_UNLOAD = "onunload";

    // PROPRIETARY JAVASCRIPT EVENTS

    /**
     * special event of IE that fires when the object is set as the active element
     * see: http://msdn.microsoft.com/workshop/author/dhtml/reference/events/onactivate.asp
     */
    public static final String ON_ACTIVATE = "onactivate";
    /**
     * special YUI library DOM event, see http://developer.yahoo.com/yui/event/#onavailable
     * onAvailable lets you define a function that will execute as soon as an element is
     * detected in the DOM. The intent is to reduce the occurrence of timing issues when
     * rendering script and html inline. It is not meant to be used to define handlers for
     * elements that may eventually be in the document; it is meant to be used to detect
     * elements you are in the process of loading.
     */
    public static final String ON_AVAILABLE = "onavailable";
}
