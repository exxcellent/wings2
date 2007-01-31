package org.wings.plaf.css.script;

import org.wings.script.ScriptListener;

public class LayoutFillScript
    implements ScriptListener
{
    String name;

    public LayoutFillScript(String name) {
        this.name = name;
    }

    public String getEvent() {
        return null;
    }

    public String getCode() {
        return null;
    }

    public String getScript() {
        return "wingS.layout.fill('" + name + "');";
    }

    public int getPriority() {
        return DEFAULT_PRIORITY;
    }
}
