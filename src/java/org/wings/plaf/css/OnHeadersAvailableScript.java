package org.wings.plaf.css;

import org.wings.script.ScriptListener;
import org.wings.util.SStringBuilder;

public class OnHeadersAvailableScript implements ScriptListener
{
    String script;
    boolean wrapAsFunction;

    public OnHeadersAvailableScript(String script) {
        this(script, true);
    }

    public OnHeadersAvailableScript(String script, boolean wrapAsFunction) {
        this.script = script;
        this.wrapAsFunction = wrapAsFunction;
    }

    public String getEvent() {
        return null;
    }

    public String getCode() {
        return null;
    }

    public String getScript() {
        final SStringBuilder output = new SStringBuilder();

        output.append("wingS.global.onHeadersAvailable(");
        if (wrapAsFunction) output.append("function() {");
        output.append(script);
        if (wrapAsFunction) output.append("}");
        output.append(");");

        return output.toString();
    }

    public int getPriority() {
        return 0;
    }
}
