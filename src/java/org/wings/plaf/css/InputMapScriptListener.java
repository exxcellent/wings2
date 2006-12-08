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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.SComponent;
import org.wings.SFrame;
import org.wings.script.JavaScriptListener;
import org.wings.script.ScriptListener;
import org.wings.util.SStringBuilder;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

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

        SStringBuilder pressed = new SStringBuilder();
        SStringBuilder typed = new SStringBuilder();
        SStringBuilder released = new SStringBuilder();
        createScriptCodes(inputMap, component, pressed, typed, released);

        if (pressed.length() > 0)
            component.addScriptListener(new InputMapScriptListener("onkeydown", "return pressed_" + component.getName() + "(event)",
                    "function pressed_" + component.getName() + "(event) {\n  " +
                            "event = wingS.events.getEvent(event);\n  " +
                            pressed.toString() + "  return true;\n}\n"));
        if (typed.length() > 0)
            component.addScriptListener(new InputMapScriptListener("onkeypress", "return typed_" + component.getName() + "(event)",
                    "function typed_" + component.getName() + "(event) {\n  " +
                            "event = wingS.events.getEvent(event);\n  " +
                            typed.toString() + "  return true;\n}\n"));
        if (released.length() > 0)
            component.addScriptListener(new InputMapScriptListener("onkeyup", "return released_" + component.getName() + "(event)",
                    "function released_" + component.getName() + "(event) {\n" +
                            "event = wingS.events.getEvent(event);\n  " +
                            released.toString() + "  return true;\n}\n"));
    }

    public static void installToFrame(SFrame frame, SComponent component) {

        InputMap inputMap = component.getInputMap(SComponent.WHEN_IN_FOCUSED_FRAME);

        SStringBuilder pressed = new SStringBuilder();
        SStringBuilder typed = new SStringBuilder();
        SStringBuilder released = new SStringBuilder();
        createScriptCodes(inputMap, component, pressed, typed, released);

        if (pressed.length() > 0)
            frame.addScriptListener(new InputMapScriptListener("onkeydown", "pressed_frame_" + component.getName() + "(event)",
                    "function pressed_frame_" + component.getName() + "(event) {\n  " +
                    "event = wingS.events.getEvent(event);\n  " +
                    pressed.toString() + "  return true;\n}\n"));
        if (typed.length() > 0)
            frame.addScriptListener(new InputMapScriptListener("onkeypress", "typed_frame_" + component.getName() + "(event)",
                    "function typed_frame_" + component.getName() + "(event) {\n  " +
                    "event = wingS.events.getEvent(event);\n  " +
                    typed.toString() + "  return true;\n}\n"));
        if (released.length() > 0)
            frame.addScriptListener(new InputMapScriptListener("onkeyup", "released_frame_" + component.getName() + "(event)",
                    "function released_frame_" + component.getName() + "(event) {\n" +
                    "event = wingS.events.getEvent(event);\n  " +
                    released.toString() + "  return true;\n}\n"));
    }

    private static void createScriptCodes(InputMap inputMap, SComponent component, SStringBuilder pressed, SStringBuilder typed, SStringBuilder released) {
        KeyStroke[] keyStrokes = inputMap.keys();

        for (int i = 0; i < keyStrokes.length; i++) {
            KeyStroke keyStroke = keyStrokes[i];
            Object binding = inputMap.get(keyStroke);

            switch (keyStroke.getKeyEventType()) {
                case KeyEvent.KEY_PRESSED:
                    appendMatchCode(pressed, keyStroke);
                    appendSendRequestCode(pressed, binding, component);
                    break;
                case KeyEvent.KEY_TYPED:
                    appendMatchCode(typed, keyStroke);
                    appendSendRequestCode(typed, binding, component);
                    log.debug("typed binding = " + binding);
                    break;
                case KeyEvent.KEY_RELEASED:
                    appendMatchCode(released, keyStroke);
                    appendSendRequestCode(released, binding, component);
                    log.debug("released binding = " + binding);
                    break;
            }
        }
    }

    private static void appendMatchCode(SStringBuilder buffer, KeyStroke keyStroke) {
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

    private static void appendSendRequestCode(SStringBuilder buffer, Object binding, SComponent targetComponent) {
        buffer.append(" { wingS.request.submitForm(" + !targetComponent.isReloadForced());
        buffer.append(",event,\"");
        buffer.append(targetComponent == null ? "" : targetComponent.getName());
        buffer.append("\",\"").append(binding);
        buffer.append("\"); return false; }\n");
    }
}
