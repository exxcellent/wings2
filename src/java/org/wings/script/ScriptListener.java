/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.script;

import java.util.EventListener;

/**
 * Script Listener is a interface declaring a (untypized) script which should be executed on an
 * untypized Event.
 *
 * <p>Typical instance is {@link JavaScriptListener}
 *
 * @author Holger Engels
 */
public interface ScriptListener extends EventListener {

    /**
     * A default value for {@link #getPriority()}
     */
    final int DEFAULT_PRIORITY = 0;

    /**
     * A high priority value for {@link #getPriority()}
     */
    final int HIGH_PRIORITY = 100;

    /**
     * A low priority value for {@link #getPriority()}
     */
    final int LOW_PRIORITY = -100;

    /**
     * Event on which this script should be triggered.
     */
    String getEvent();

    /**
     * Inline code which should be executed on occuration of this event
     * @return Inline Code
     */
    String getCode();

    /**
     * Optional, additional script code which should be rendered externally (i.e. external .JS file).
     * @return Optional scripting code, may be <code>null</code>
     */
    String getScript();

    /**
     * A numeric value which prioritzes the execution order of various script listeners listening on the same event.
     * @return {@link #DEFAULT_PRIORITY} if you don't care.
     * Lower values are executed later (low priority), higher values eraliert (higher priority).
     */
    int getPriority();

    // SComponent[] getComponents();
}
