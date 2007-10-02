/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css;

import org.wings.script.JavaScriptListener;
import org.wings.SComponent;

/**
 * @author hengels
 */
public class DWRScriptListener extends JavaScriptListener {

    protected String callableName;
    protected Object callable;

    public DWRScriptListener(String event, String code, String callableName, Object callable) {
        super(event, code);

        this.callableName = callableName;
        this.callable = callable;
    }

    public DWRScriptListener(String event, String code, String script, String callableName, Object callable) {
        super(event, code, script);

        this.callableName = callableName;
        this.callable = callable;
    }

    public DWRScriptListener(String event, String code, String script, SComponent[] components) {
        super(event, code, script, components);
    }

    public String getCallableName() {
        return callableName;
    }

    public Object getCallable() {
        return callable;
    }
}
