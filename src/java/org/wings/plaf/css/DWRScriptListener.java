/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css;

import org.wings.script.JavaScriptListener;
import org.wings.SComponent;

/**
 * @author hengels
 * @version $Revision$
 */
class DWRScriptListener
        extends JavaScriptListener {
    public DWRScriptListener(String event, String code, String script, SComponent[] components) {
        super(event, code, script, components);
    }
}
