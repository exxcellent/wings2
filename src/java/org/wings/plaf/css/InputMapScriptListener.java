package org.wings.plaf.css;

import org.wings.SComponent;
import org.wings.script.JavaScriptListener;
import org.wings.script.ScriptListener;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

/**
 * @author hengels
 * @version $Revision$
 */
class InputMapScriptListener
    extends JavaScriptListener
{
    public InputMapScriptListener(String event, String code, String script) {
        super(event, code, script);
    }

    static void install(SComponent component) {
        ScriptListener[] scriptListeners = component.getScriptListeners();

        for (int i = 0; i < scriptListeners.length; i++) {
            ScriptListener scriptListener = scriptListeners[i];
            if (scriptListener instanceof InputMapScriptListener)
                component.removeScriptListener(scriptListener);
        }

        InputMap inputMap = component.getInputMap();
        
        StringBuffer pressed = new StringBuffer();
        StringBuffer typed = new StringBuffer();
        StringBuffer released = new StringBuffer();
        KeyStroke[] keyStrokes = inputMap.keys();

        for (int i = 0; i < keyStrokes.length; i++) {
            KeyStroke keyStroke = keyStrokes[i];
            Object binding = inputMap.get(keyStroke);

            switch (keyStroke.getKeyEventType()) {
                case KeyEvent.KEY_PRESSED:
                    writeMatch(pressed, keyStroke);
                    writeRequest(pressed, binding);
                    break;
                case KeyEvent.KEY_TYPED:
                    writeMatch(typed, keyStroke);
                    writeRequest(typed, binding);
                    System.out.println("typed binding = " + binding);
                    break;
                case KeyEvent.KEY_RELEASED:
                    writeMatch(released, keyStroke);
                    writeRequest(released, binding);
                    System.out.println("released binding = " + binding);
                    break;
            }
        }

        if (pressed.length() > 0)
            component.addScriptListener(new InputMapScriptListener("onkeydown", "pressed_" + component.getName() + "(event)",
                "function pressed_" + component.getName() + "(event) {\n  " +
                "event = getEvent(event); target = getTarget(event);\n  " +
                pressed.toString() + "  return false;\n}\n"));
        if (typed.length() > 0)
            component.addScriptListener(new InputMapScriptListener("onkeypress", "typed_" + component.getName() + "(event)",
                "function typed_" + component.getName() + "(event) {\n  " +
                "event = getEvent(event); target = getTarget(event);\n  " +
                typed.toString() + "  return false;\n}\n"));
        if (released.length() > 0)
            component.addScriptListener(new InputMapScriptListener("onkeyup", "released_" + component.getName() + "(event)",
                "function released_" + component.getName() + "(event) {\n" +
                "event = getEvent(event); target = getTarget(event);\n  " +
                released.toString() + "  return false;\n}\n"));
    }

    private static void writeMatch(StringBuffer buffer, KeyStroke keyStroke) {
        buffer.append("if (event.keyCode == " + keyStroke.getKeyCode());
        if ((keyStroke.getModifiers() & InputEvent.SHIFT_DOWN_MASK) != 0)
            buffer.append(" && event.shiftKey");
        if ((keyStroke.getModifiers() & InputEvent.CTRL_DOWN_MASK) != 0)
            buffer.append(" && event.ctrlKey");
        if ((keyStroke.getModifiers() & InputEvent.ALT_DOWN_MASK) != 0)
            buffer.append(" && event.altKey");
        buffer.append(")");
    }

    private static void writeRequest(StringBuffer buffer, Object binding) {
        buffer.append(" { preventDefault(event); sendEvent(event, \"").append(binding).append("\"); }\n");
    }
}
