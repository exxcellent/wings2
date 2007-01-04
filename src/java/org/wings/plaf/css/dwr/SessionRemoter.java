package org.wings.plaf.css.dwr;

import org.directwebremoting.impl.DefaultRemoter;

public class SessionRemoter
    extends DefaultRemoter
{
    public String generateInterfaceScript(String scriptName, String path) throws SecurityException {
        String script = super.generateInterfaceScript(scriptName, path);
        script += "alert('hallo');";
        return script;
    }
}
