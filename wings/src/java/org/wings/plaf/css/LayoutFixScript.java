package org.wings.plaf.css;

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
        return "layoutFix(document.getElementById('" + name + "'));\n";
    }

    public int getPriority() {
        return 0;
    }
}
