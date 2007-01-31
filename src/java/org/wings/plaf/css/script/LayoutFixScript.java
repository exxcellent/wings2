package org.wings.plaf.css.script;

import org.wings.script.ScriptListener;

public class LayoutFixScript
    implements ScriptListener
{
    String name;

    public LayoutFixScript(String name) {
        this.name = name;
    }

    public String getEvent() {
        return null;
    }

    public String getCode() {
        return null;
    }

    public String getScript() {
        return "wingS.layout.fix('" + name + "');";
    }

    public int getPriority() {
        return DEFAULT_PRIORITY;
    }
}
