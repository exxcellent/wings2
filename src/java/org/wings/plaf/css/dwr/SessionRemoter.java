package org.wings.plaf.css.dwr;

import org.directwebremoting.impl.DefaultRemoter;
import org.wings.plaf.css.Utils;

public class SessionRemoter
    extends DefaultRemoter
{
    public String generateInterfaceScript(String scriptName, String path) throws SecurityException {
        String script = super.generateInterfaceScript(scriptName, path);
        script += "\n" + Utils.HEADER_LOADED_CALLBACK;
        return script;
    }
}
