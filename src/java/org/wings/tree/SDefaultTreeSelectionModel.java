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
package org.wings.tree;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class SDefaultTreeSelectionModel
        extends DefaultTreeSelectionModel
        implements STreeSelectionModel {

    /**
     * indicates if we should fire event immediately when they arise, or if we
     * should collect them for a later delivery
     */
    private boolean delayEvents = false;

    /**
     * got a delayed Event?
     */
    protected final ArrayList delayedEvents = new ArrayList();

    public SDefaultTreeSelectionModel() {
        super();
    }

    public boolean getDelayEvents() {
        return delayEvents;
    }

    public void setDelayEvents(boolean b) {
        delayEvents = b;
    }

    /**
     * fire event with isValueIsAdjusting true
     */
    public void fireDelayedIntermediateEvents() {}

    public void fireDelayedFinalEvents() {
        for (Iterator iter = delayedEvents.iterator(); iter.hasNext();) {
            TreeSelectionEvent e = (TreeSelectionEvent) iter.next();

            fireValueChanged(e);
        }
        delayedEvents.clear();
    }

    protected void fireValueChanged(TreeSelectionEvent e) {
        if (delayEvents) {
            delayedEvents.add(e);
        } else {
            super.fireValueChanged(e);
        }
    }


    /**
     * Unique shared instance. No selection possible.
     */
    public static final SDefaultTreeSelectionModel NO_SELECTION_MODEL =
            new SDefaultTreeSelectionModel() {
                /**
                 * A null implementation that selects nothing
                 */
                public void setSelectionPaths(TreePath[] pPaths) {
                }

                /**
                 * A null implementation that adds nothing
                 */
                public void addSelectionPaths(TreePath[] paths) {
                }

                /**
                 * A null implementation that removes nothing
                 */
                public void removeSelectionPaths(TreePath[] paths) {
                }

                // don't fire events, because there should be not state change
                protected void fireValueChanged(TreeSelectionEvent e) {}

    };

}


