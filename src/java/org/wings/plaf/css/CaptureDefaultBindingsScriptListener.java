/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css;

import org.wings.SComponent;
import org.wings.script.JavaScriptListener;
import org.wings.script.ScriptListener;
import org.wings.util.SStringBuilder;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.util.StringTokenizer;

/**
 * @author hengels
 * @version $Revision$
 */
class CaptureDefaultBindingsScriptListener
        extends JavaScriptListener
{
    public CaptureDefaultBindingsScriptListener(String event, String code, String script) {
        super(event, code, script);
    }

    static void install(SComponent component) {
        ScriptListener[] scriptListeners = component.getScriptListeners();

        for (int i = 0; i < scriptListeners.length; i++) {
            ScriptListener scriptListener = scriptListeners[i];
            if (scriptListener instanceof CaptureDefaultBindingsScriptListener)
                component.removeScriptListener(scriptListener);
        }

        String string = (String)component.getSession().getProperty("wings.capture.default.bindings", "");

        SStringBuilder typed = new SStringBuilder();
        for (StringTokenizer tokenizer = new StringTokenizer(string, ","); tokenizer.hasMoreTokens();) {
            String token = tokenizer.nextToken();
            KeyStroke keyStroke = KeyStroke.getKeyStroke(token);

            writeMatch(typed, keyStroke);
            writeCapture(typed);
        }

        if (typed.length() > 0)
            component.addScriptListener(new CaptureDefaultBindingsScriptListener("onkeypress", "return typed_" + component.getName() + "(event)",
                    "function typed_" + component.getName() + "(event) {\n  " +
                    "event = getEvent(event);\n  " +
                    typed.toString() + "  return true;\n}\n"));
    }

    private static void writeMatch(SStringBuilder buffer, KeyStroke keyStroke) {
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

    private static void writeCapture(SStringBuilder buffer) {
        buffer.append(" { preventDefault(event); return true; }\n");
    }
}
