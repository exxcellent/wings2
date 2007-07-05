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

import org.wings.SButton;
import org.wings.SComponent;
import org.wings.util.SStringBuilder;

public class JavaScriptListener
        implements ScriptListener {
    private String event;
    private String code;
    private String script;
    private SComponent[] components;
    private int priority = DEFAULT_PRIORITY;

    /**
     * Use this java script implementation to submit forms on button click
     */
    // TODO: Implement handling of formless submits
    // TODO: Avoid triggering of enter key catchers
    public final static String JS_FORM_SUBMIT_SCRIPT = "wingS.request.sendEvent(event, true, true);";

    /**
     * Use i.e. {@link SButton#addScriptListener(org.wings.script.ScriptListener)} to add this scripts.
     */
    public final static JavaScriptListener JS_ON_CHANGE_SUBMIT_FORM = new JavaScriptListener(JavaScriptEvent.ON_CHANGE, JS_FORM_SUBMIT_SCRIPT);

    /**
     * @param event one of 'onclick', 'onmouseover', ..
     * @param code  the code that is written as a value of the event attribute
     */
    public JavaScriptListener(String event, String code) {
        this.event = event;
        this.code = code;
    }

    /**
     * @param event  one of 'onclick', 'onmouseover', ..
     * @param code   the code that is written as a value of the event attribute
     * @param script larger code block (java script functions), that is written
     *               to a combined script file, that is linked in the header
     */
    public JavaScriptListener(String event, String code, String script) {
        this.event = event;
        this.code = code;
        this.script = script;
    }

    /**
     * The code is parsed and all occurrences of '{0..components.length}' are substituted
     * with that component's id. You can use this to address elements by id.
     * This mechanism is highly dependant on the code, the plaf generates for a component.
     *
     * @param event      one of 'onclick', 'onmouseover', ..
     * @param code       the code that is written as a value of the event attribute
     * @param components the components whose ids are substituted into the code
     */
    public JavaScriptListener(String event, String code, SComponent[] components) {
        this.event = event;
        this.code = substituteIds(code, components);
        this.components = components;
    }

    /**
     * The code is parsed and all occurrences of '{0..components.length}' are substituted
     * with that component's id. You can use this to address elements by id.
     * This mechanism is highly dependant on the code, the plaf generates for a component.
     *
     * @param event      one of 'onclick', 'onmouseover', ..
     * @param code       the code that is written as a value of the event attribute
     * @param script     larger code block (java script functions), that is written
     *                   to a separate script file, that is linked in the header
     * @param components the components whose ids are substituted into the code
     */
    public JavaScriptListener(String event, String code, String script, SComponent[] components) {
        this.event = event;
        this.code = substituteIds(code, components);
        this.script = substituteIds(script, components);
        this.components = components;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEvent() { return event; }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() { return code; }

    public void setScript(String script) {
        this.script = script;
    }

    public String getScript() { return script; }

    public void setComponents(SComponent[] components) {
        this.components = components;
    }

    public SComponent[] getComponents() { return components; }

    private String substituteIds(String code, SComponent[] components) {
        SStringBuilder buffer = new SStringBuilder();

        int startPos = 0;
        int endPos = 0;
        char lastChar = code.charAt(0);
        for (int i = 1; i < code.length(); ++i) {
            char c = code.charAt(i);
            endPos = i;
            if (lastChar == '{' && Character.isDigit(c)) {
                int varIndex = (c - '0');
                while (Character.isDigit(code.charAt(++i))) {
                    varIndex *= 10;
                    varIndex += (code.charAt(i) - '0');
                }
                c = code.charAt(i);
                if (c == '}') {
                    buffer.append(code.substring(startPos, endPos - 1));
                    startPos = i + 1;
                    buffer.append(components[varIndex].getName());
                } else {
                    throw new IllegalArgumentException("Expected closing '}' after '{" + varIndex + "'");
                }
            }
            lastChar = c;
        }
        buffer.append(code.substring(startPos));

        return buffer.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        /* only checking for instanceof, not exact class, so we don't
         * need to implement this in inherited classes
         */
        if (!(obj instanceof JavaScriptListener)) {
            return false;
        }
        JavaScriptListener testObj = (JavaScriptListener) obj;

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

        if (testObj.getComponents() == null) {
            if (getComponents() != null) {
                return false;
            }
        } else {
            if (!testObj.getComponents().equals(getComponents())) {
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

}
