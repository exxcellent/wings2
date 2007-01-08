package org.wings.plaf.css.script;

import org.wings.script.ScriptListener;

public class LayoutScrollPaneScript
    implements ScriptListener
{
    String name;

    public LayoutScrollPaneScript(String name) {
        this.name = name;
    }

    public String getEvent() {
        return null;
    }

    public String getCode() {
        return null;
    }

    public String getScript() {
        return "wingS.layout.scrollPane('" + name + "');";
    }

    public int getPriority() {
        return DEFAULT_PRIORITY;
    }
}
