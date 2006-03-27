/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
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
package org.wings.plaf.css;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.InputMap;
import javax.swing.KeyStroke;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.SComponent;
import org.wings.SFrame;
import org.wings.script.JavaScriptListener;
import org.wings.script.ScriptListener;

/**
 * @author hengels
 * @version $Revision$
 */
public class InputMapScriptListener
        extends JavaScriptListener {
    private final static transient Log log = LogFactory.getLog(InputMapScriptListener.class);

    public InputMapScriptListener(String event, String code, String script) {
        super(event, code, script);
    }

    public static void install(SComponent component) {
        ScriptListener[] scriptListeners = component.getScriptListeners();

        for (int i = 0; i < scriptListeners.length; i++) {
            ScriptListener scriptListener = scriptListeners[i];
            if (scriptListener instanceof InputMapScriptListener)
                component.removeScriptListener(scriptListener);
        }

        InputMap inputMap = component.getInputMap();
        if (inputMap.size() == 0) return; // we're done

        StringBuffer pressed = new StringBuffer();
        StringBuffer typed = new StringBuffer();
        StringBuffer released = new StringBuffer();
        KeyStroke[] keyStrokes = inputMap.keys();

        for (int i = 0; i < keyStrokes.length; i++) {
            KeyStroke keyStroke = keyStrokes[i];
            Object binding = inputMap.get(keyStroke);

            /*
            writeMatch(typed, keyStroke);
            writeRequest(typed, binding);
            */

            switch (keyStroke.getKeyEventType()) {
                case KeyEvent.KEY_PRESSED:
                    writeMatch(pressed, keyStroke);
                    writeRequest(pressed, binding);
                    break;
                case KeyEvent.KEY_TYPED:
                    writeMatch(typed, keyStroke);
                    writeRequest(typed, binding);
                    log.debug("typed binding = " + binding);
                    break;
                case KeyEvent.KEY_RELEASED:
                    writeMatch(released, keyStroke);
                    writeRequest(released, binding);
                    log.debug("released binding = " + binding);
                    break;
            }
        }

        if (pressed.length() > 0)
            component.addScriptListener(new InputMapScriptListener("onkeydown", "return pressed_" + component.getName() + "(event)",
                    "function pressed_" + component.getName() + "(event) {\n  " +
                    "event = getEvent(event);\n  " +
                    pressed.toString() + "  return true;\n}\n"));
        if (typed.length() > 0)
            component.addScriptListener(new InputMapScriptListener("onkeypress", "return typed_" + component.getName() + "(event)",
                    "function typed_" + component.getName() + "(event) {\n  " +
                    "event = getEvent(event);\n  " +
                    typed.toString() + "  return true;\n}\n"));
        if (released.length() > 0)
            component.addScriptListener(new InputMapScriptListener("onkeyup", "return released_" + component.getName() + "(event)",
                    "function released_" + component.getName() + "(event) {\n" +
                    "event = getEvent(event);\n  " +
                    released.toString() + "  return true;\n}\n"));
    }

    public static void installToFrame(SFrame frame, SComponent component) {

        InputMap inputMap = component.getInputMap(SComponent.WHEN_IN_FOCUSED_FRAME);

        StringBuffer pressed = new StringBuffer();
        StringBuffer typed = new StringBuffer();
        StringBuffer released = new StringBuffer();
        KeyStroke[] keyStrokes = inputMap.keys();

        for (int i = 0; i < keyStrokes.length; i++) {
            KeyStroke keyStroke = keyStrokes[i];
            Object binding = inputMap.get(keyStroke);

            /*
            writeMatch(typed, keyStroke);
            writeRequest(typed, binding);
            */

            switch (keyStroke.getKeyEventType()) {
                case KeyEvent.KEY_PRESSED:
                    writeMatch(pressed, keyStroke);
                    writeRequestForFrame(pressed, binding, component.getName());
                    break;
                case KeyEvent.KEY_TYPED:
                    writeMatch(typed, keyStroke);
                    writeRequestForFrame(typed, binding, component.getName());
                    log.debug("typed binding = " + binding);
                    break;
                case KeyEvent.KEY_RELEASED:
                    writeMatch(released, keyStroke);
                    writeRequestForFrame(released, binding, component.getName());
                    log.debug("released binding = " + binding);
                    break;
            }
        }

        if (pressed.length() > 0)
            frame.addScriptListener(new InputMapScriptListener("onkeydown", "pressed_frame_" + component.getName() + "(event)",
                    "function pressed_frame_" + component.getName() + "(event) {\n  " +
                    "event = getEvent(event);\n  " +
                    pressed.toString() + "  return true;\n}\n"));
        if (typed.length() > 0)
            frame.addScriptListener(new InputMapScriptListener("onkeypress", "typed_frame_" + component.getName() + "(event)",
                    "function typed_frame_" + component.getName() + "(event) {\n  " +
                    "event = getEvent(event);\n  " +
                    typed.toString() + "  return true;\n}\n"));
        if (released.length() > 0)
            frame.addScriptListener(new InputMapScriptListener("onkeyup", "released_frame_" + component.getName() + "(event)",
                    "function released_frame_" + component.getName() + "(event) {\n" +
                    "event = getEvent(event);\n  " +
                    released.toString() + "  return true;\n}\n"));
    }

    private static void writeMatch(StringBuffer buffer, KeyStroke keyStroke) {
        buffer.append("if (event.keyCode == " + keyStroke.getKeyCode());
        if ((keyStroke.getModifiers() & InputEvent.SHIFT_DOWN_MASK) != 0)
            buffer.append(" && event.shiftKey");
        else
            buffer.append(" && !event.shiftKey");
        if ((keyStroke.getModifiers() & InputEvent.CTRL_DOWN_MASK) != 0)
            buffer.append(" && event.ctrlKey");
        else
            buffer.append(" && !event.ctrlKey");
        if ((keyStroke.getModifiers() & InputEvent.ALT_DOWN_MASK) != 0)
            buffer.append(" && event.altKey");
        else
            buffer.append(" && !event.altKey");
        buffer.append(")");
    }

    private static void writeRequest(StringBuffer buffer, Object binding) {
        buffer.append(" { sendEvent(event, \"").append(binding).append("\"); return false; }\n");
    }
    private static void writeRequestForFrame(StringBuffer buffer, Object binding, String eventId) {
        buffer.append(" { sendEvent(event, \"").append(binding).append("\", \"").append(eventId).append("\"); return false; }\n");
    }
}
