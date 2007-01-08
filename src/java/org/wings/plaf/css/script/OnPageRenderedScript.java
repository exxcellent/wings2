package org.wings.plaf.css.script;

import org.wings.script.ScriptListener;

public class OnPageRenderedScript
    implements ScriptListener
{
    String script;

    public OnPageRenderedScript(String script) {
        this.script = script;
    }

    public String getEvent() {
        return null;
    }

    public String getCode() {
        return null;
    }

    public String getScript() {
        return script;
    }

    public int getPriority() {
        return DEFAULT_PRIORITY;
    }
}
