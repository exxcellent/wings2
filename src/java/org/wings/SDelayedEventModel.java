/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public interface SDelayedEventModel {
    /**
     * if this is set to true, events are not fired immediately. They are
     * collected and fired after setting this to false...
     */
    void setDelayEvents(boolean b);

    /**
     *
     */
    boolean getDelayEvents();

    /**
     * fire delayed events which describes a "in progress"
     * state change, like TreeWillExpand, or ListSelectionEvent with
     * getIsAdjusting() true, ...
     */
    void fireDelayedIntermediateEvents();

    /**
     * fire remaining delayed events. In this level all events, which
     * are important to an application should be fired. All listeners, which are
     * notified in this level can assume that the components are in a consistent
     * (considering user interaction) state.
     */
    void fireDelayedFinalEvents();
}


