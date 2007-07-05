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
package org.wings;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Default implementation of a list selection model.
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @see javax.swing.ListSelectionModel
 * @see SList
 * @see STable
 */
public class SDefaultListSelectionModel
        extends DefaultListSelectionModel
        implements SListSelectionModel {

    /**
     * indicates the the selection model is in {@link #NO_SELECTION} mode. This
     * is necessary, because we cannot set the selection mode of the swing
     * DefaultSelectionModel to a value we want  (it does not provide
     * for NO_SELEFCTION), so we have to wrap it...
     */
    private boolean noSelection = false;

    /**
     * indicates if we should fire event immediately when they arise, or if we
     * should collect them for a later delivery
     */
    private boolean delayEvents = false;

    /**
     * should we fire adjusting events. This is an optimization feature. In most
     * scenarios we don't need that events. In fact I cannot imagine a scenario
     * where we need this events...
     */
    private boolean fireAdjustingEvents = false;

    /**
     * all delayed events are stored here.
     */
    protected final ArrayList delayedEvents = new ArrayList(5);

    public int getMinSelectionIndex() {
        if (noSelection) {
            return -1;
        } else {
            return super.getMinSelectionIndex();
        }
    }

    public int getMaxSelectionIndex() {
        if (noSelection) {
            return -1;
        } else {
            return super.getMaxSelectionIndex();
        }
    }

    public boolean isSelectedIndex(int index) {
        if (noSelection) {
            return false;
        } else {
            return super.isSelectedIndex(index);
        }
    }

    public int getAnchorSelectionIndex() {
        if (noSelection) {
            return -1;
        } else {
            return super.getAnchorSelectionIndex();
        }
    }

    public int getLeadSelectionIndex() {
        if (noSelection) {
            return -1;
        } else {
            return super.getLeadSelectionIndex();
        }
    }

    public boolean isSelectionEmpty() {
        if (noSelection) {
            return true;
        } else {
            return super.isSelectionEmpty();
        }
    }

    public int getSelectionMode() {
        if (noSelection) {
            return NO_SELECTION;
        } else {
            return super.getSelectionMode();
        }
    }

    public void setSelectionMode(int selectionMode) {
        if (selectionMode == NO_SELECTION) {
            noSelection = true;
        } else {
            noSelection = false;
            super.setSelectionMode(selectionMode);
        }
    }

    protected void fireDelayedEvents(boolean onlyAdjusting) {
        for (Iterator iter = delayedEvents.iterator(); iter.hasNext();) {
            ListSelectionEvent e = (ListSelectionEvent) iter.next();

            if (!onlyAdjusting || e.getValueIsAdjusting()) {
                fireValueChanged(e.getFirstIndex(), e.getLastIndex(),
                        e.getValueIsAdjusting());
                iter.remove();
            }
        }
    }

    public boolean getDelayEvents() {
        return delayEvents;
    }

    public void setDelayEvents(boolean b) {
        delayEvents = b;
    }

    public boolean getFireAdjustingEvents() {
        return fireAdjustingEvents;
    }

    public void setFireAdjustingEvents(boolean b) {
        fireAdjustingEvents = b;
    }

    /**
     * fire event with isValueIsAdjusting true
     */
    public void fireDelayedIntermediateEvents() {
        if (fireAdjustingEvents) {
            fireDelayedEvents(true);
        }
    }

    public void fireDelayedFinalEvents() {
        if (!delayEvents) {
            fireDelayedEvents(false);
            delayedEvents.clear();
        }
    }

    protected void fireValueChanged(int firstIndex, int lastIndex,
                                    boolean isAdjusting) {
        if (!noSelection) {

            if (delayEvents) {
                if (!isAdjusting || fireAdjustingEvents) {
                    delayedEvents.add(new ListSelectionEvent(this,
                            firstIndex, lastIndex,
                            isAdjusting));
                }
            } else {
                super.fireValueChanged(firstIndex, lastIndex, isAdjusting);
            }
        }
    }

    /**
     * use this with care. It is only a way to speed up your application if you
     * don't need selection in your list. If you set this selection model, the
     * only way to switch between selection modes is to set a new selection
     * model, which supports the wished selection mode
     */
    public static final ListSelectionModel NO_SELECTION_LIST_SELECTION_MODEL =
            new ListSelectionModel() {

                public void setSelectionInterval(int index0, int index1) {}

                public void addSelectionInterval(int index0, int index1) {}

                public void removeSelectionInterval(int index0, int index1) {}

                public int getMinSelectionIndex() { return -1;}

                public int getMaxSelectionIndex() { return -1;}

                public boolean isSelectedIndex(int index) { return false;}

                public int getAnchorSelectionIndex() { return -1;}

                public void setAnchorSelectionIndex(int index) {}

                public int getLeadSelectionIndex() { return -1;}

                public void setLeadSelectionIndex(int index) {}

                public void clearSelection() {}

                public boolean isSelectionEmpty() { return true;}

                public void insertIndexInterval(int index, int length,
                                                boolean before) {}

                public void removeIndexInterval(int index0, int index1) {}

                public void setValueIsAdjusting(boolean valueIsAdjusting) {}

                public boolean getValueIsAdjusting() { return false;}

                public void setSelectionMode(int selectionMode) {}

                public int getSelectionMode() { return NO_SELECTION; }

                public void addListSelectionListener(ListSelectionListener x) {}

                public void removeListSelectionListener(ListSelectionListener x) {}

                public boolean getDelayEvents() { return false;}

                public void setDelayEvents(boolean b) {}

                public void fireDelayedIntermediateEvents() {}

                public void fireDelayedFinalEvents() {}
            };

}


