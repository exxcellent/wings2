/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.session.SessionManager;

import javax.swing.event.EventListenerList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * This class is used to create a multiple-exclusion scope for a set of
 * buttons. Creating a set of buttons with the same ButtonGroup object means
 * that turning "on" one of those buttons turns off all other buttons in the
 * group.
 * <p/>
 * <p>A SButtonGroup can be used with any set of objects that inherit from
 * {@link SAbstractButton}, because they support the selected state.
 * <p/>
 * <p>Initially, all buttons in the group are unselected. Once any button is
 * selected, one button is always selected in the group. There is no way to
 * turn a button programmatically to "off", in order to clear the button
 * group.
 * <p/>
 * <p><em>Details:</em>The implementation of the button group is a
 * bit tricky for the HTML generation. In HTML, groups of components are
 * usually formed by giving them all the same name. The problem is, that
 * any {@link SComponent}, especially the {@link SAbstractButton}, have globally
 * <em>unique</em> names. So this implementation gives all buttons in the
 * group the name of this SButtonGroup, and sets their <em>value</em> to
 * their actual name. So a bit of dispatching is already done here.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 * @see javax.swing.ButtonGroup
 */
public class SButtonGroup implements SDelayedEventModel {
    private final transient static Log log = LogFactory.getLog(SButtonGroup.class);
    public static final String SELECTION_CHANGED = "SelectionChanged";

    protected final ArrayList buttons = new ArrayList(2);

    private SAbstractButton selection = null;

    /* */
    private transient String componentId = null;

    protected final EventListenerList listenerList = new EventListenerList();

    /**
     * indicates if we should fire event immediately when they arise, or if we
     * should collect them for a later delivery
     */
    private boolean delayEvents = false;

    /**
     * all delayed events are stored here.
     */
    protected final ArrayList delayedEvents = new ArrayList(2);

    public SButtonGroup() {}

    /**
     * Return a jvm wide unique id.
     *
     * @return an id
     */
    public final String getComponentId() {
        if (componentId == null)
            componentId = SessionManager.getSession().createUniqueId();
        return componentId;
    }

    protected void setSelection(SAbstractButton button) {
        SAbstractButton oldSelection = selection;

        selection = button;

        if (oldSelection != null && oldSelection.getGroup() == this)
            oldSelection.setSelected(false);

        if (selection != null)
            selection.setSelected(true);


        fireActionPerformed(selection != null ? selection.getActionCommand() :
                SELECTION_CHANGED);
    }

    /*
     * Konzeptionell richtiger waere hier SAbstractButton. Macht aber keinen
     * Sinn. Macht nur Sinn abstract Checkboxes(eventuell nur RadioButtons ???).
     */
    public void add(SAbstractButton button) {
        if (buttons != null && !buttons.contains(button)) {
            buttons.add(button);
            button.setGroup(this);
            if (selection == null && button.isSelected()) {
                setSelection(button);
            }
        }
    }

    public void remove(SAbstractButton button) {
        if (button == null || button.getGroup() != this)
            return;

        buttons.remove(button);
        button.setGroup(null);

        if (button == selection) {
            setSelection(null);
        }
    }

    public void removeAll() {
        while (buttons.size() > 0)
            remove((SAbstractButton) buttons.get(0));
    }

    public final SAbstractButton getSelection() {
        return selection;
    }

    public void setSelected(SAbstractButton b, boolean selected) {
        if (selected && b != selection && b != null) {
            setSelection(b);
        }
        // if button should be set to unselected, clear selection
        if (!selected && b != null && b.equals(selection)) {
            setSelection(null);
            
        }
    }

    public boolean isSelected(SAbstractButton button) {
        return button == getSelection();
    }

    public Iterator iterator() {
        return buttons.iterator();
    }

    public Enumeration getElements() {
        return Collections.enumeration(buttons);
    }

    public String getLowLevelEventId() {
        return getComponentId();
    }

    /**
     * Adds an action listener to this group of buttons.
     * If one of the buttons contained in this group gets selected, an action event will occur.
     *
     * @param listener
     */
    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    public void removeActionListener(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }


    /**
     * Fire an ActionEvent at each registered listener.
     */
    protected void fireActionPerformed(String command) {
        fireActionEvent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                command));
    }

    /**
     * Fire an ActionEvent at each registered listener.
     */
    protected void fireActionEvent(ActionEvent e) {
        if (e == null)
            return;

        if (delayEvents) {
            delayedEvents.add(e);
            return;
        }

        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ActionListener.class) {
                ((ActionListener) listeners[i + 1]).actionPerformed(e);
            }
        }
    }

    public void setDelayEvents(boolean b) {
        delayEvents = b;
    }

    public boolean getDelayEvents() {
        return delayEvents;
    }


    public void fireDelayedIntermediateEvents() {}

    public void fireDelayedFinalEvents() {
        for (Iterator iter = delayedEvents.iterator(); iter.hasNext();) {
            ActionEvent e = (ActionEvent) iter.next();

            fireActionEvent(e);
        }
        delayedEvents.clear();
    }
}


