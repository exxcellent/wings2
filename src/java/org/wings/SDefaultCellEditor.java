/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.EventObject;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

import org.wings.event.SRequestEvent;
import org.wings.event.SRequestListener;
import org.wings.session.SessionManager;
import org.wings.table.STableCellEditor;

/**
 * A default Table cell Editor. In order to see the graphics, you need the
 * Java look and feel graphics (jlfgr*.jar)
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class SDefaultCellEditor
    implements STableCellEditor {
    /**
     * The default ok button icon.
     */
    public static final SIcon OK_BUTTON_ICON = new SResourceIcon("toolbarButtonGraphics/general/Save16.gif");

    /**
     * The default cancel button icon.
     */
    public static final SIcon CANCEL_BUTTON_ICON = new SResourceIcon("toolbarButtonGraphics/general/Stop16.gif");


    /**
     * Label for displaying (error)-messages. It is unvisible, until a message
     * is set.
     */
    protected final SLabel messageLabel;

    /**
     * Panel for edit fields.
     */
    protected final SPanel editorPanel;

    /**
     * If this button is pressed, editing is tried to stop. If input validation
     * found no error, editing is stopped, else an error message is displayed
     */
    protected final SButton ok;

    /**
     * If this button is pressed, editing is canceled.
     */
    protected final SButton cancel;

    /**
     * Store here the CellEditorListener
     */
    protected final EventListenerList listenerList;

    /**
     * Event listener, which set the fire... indicators. This event listener is
     * added to the three buttons {@link #ok}, {@link #cancel} and {@link #undo}
     * and the {@link #editorFrom form}
     */
    private final ActionListener fireEventListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == ok) {
                stopCellEditing();
            }
            else if (e.getSource() == cancel) {
                cancelCellEditing();
            }
        }
    };

    /**
     * Fast edit support is editing with reduced interaction. E.g. a boolean
     * value can only have to states, true or false. So if editing is started,
     * the editor just flips the state and fires editing stopped.
     */
    private boolean fastEditSupport = true;

    protected EditorDelegate delegate;

    protected SComponent editorComponent;

    /**
     * Initialize the DefaultCellEditor with an editor component (like an text
     * field for instance). After calling this constructor, the
     * {@link EditorDelegate}, that links the CellEditor and the
     * editorComponent has to be passed to the delegate instance variable.
     *
     * @param editorComponent   the component used
     * @param initializeButtons flag to indicate if the button texts and icons
     *                          should be initialized.
     */
    protected SDefaultCellEditor(SComponent editorComponent,
                                 boolean initializeButtons) {
        this.messageLabel = new SLabel();
        this.editorPanel = new SPanel(new SFlowLayout());
        this.ok = new SButton();
        this.cancel = new SButton();
        this.listenerList = new EventListenerList();
        this.editorComponent = editorComponent;

        editorPanel.add(messageLabel);
        editorPanel.add(editorComponent);
        if (initializeButtons) {
            initButtons();
        }
    }

    /**
     * Constructs a DefaultCellEditor that uses a text field.
     *
     * @param x a STextField object ...
     */
    public SDefaultCellEditor(STextField x) {
        this(x, true);
        this.delegate = new EditorDelegate() {
            public void setValue(Object v) {
                super.setValue(v);

                if (v != null)
                    ((STextField)editorComponent).setText(v.toString());
                else
                    ((STextField)editorComponent).setText(null);
            }

            public Object getCellEditorValue() {
                return ((STextField)editorComponent).getText();
            }

            public boolean stopCellEditing() {
                return true;
            }

            public boolean shouldSelectCell(EventObject anEvent) {
                return true;
            }
        };
    }

    /**
     * Constructs a DefaultCellEditor object that uses a check box.
     *
     * @param x a SCheckBox object ...
     */
    public SDefaultCellEditor(SCheckBox x) {
        this(x, true);
        this.delegate = new EditorDelegate() {
            public void setValue(Object v) {
                // Try my best to do the right thing with v
                boolean bool;
                if (v instanceof Boolean) {
                    bool = ((Boolean)v).booleanValue();
                }
                else if (v instanceof String) {
                    Boolean b = Boolean.valueOf((String)v);
                    bool = b.booleanValue();
                }
                else {
                    bool = false;
                }

                if (fastEditSupport) {
                    ((SCheckBox)editorComponent).setSelected(!bool);
                    SDefaultCellEditor.this.stopCellEditing();
                }
                else {
                    ((SCheckBox)editorComponent).setSelected(bool);
                }
            }

            public Object getCellEditorValue() {
                return Boolean.valueOf(((SCheckBox)editorComponent).isSelected());
            }

            public boolean stopCellEditing() {
                return true;
            }

            public boolean shouldSelectCell(EventObject anEvent) {
                return false;
            }
        };
    }

    /**
     * Intializes the buttons with default icons, tooltip text and listener.
     */
    protected void initButtons() {
        ok.addActionListener(fireEventListener);
        ok.setIcon(OK_BUTTON_ICON);
        ok.setToolTipText("ok");

        cancel.addActionListener(fireEventListener);
        cancel.setIcon(CANCEL_BUTTON_ICON);
        cancel.setToolTipText("cancel");

        editorPanel.add(ok);
        editorPanel.add(cancel);
    }

    /**
     * Returns a reference to the editor component.
     *
     * @return the editor Component
     */
    public final SComponent getComponent() {
        return editorComponent;
    }

    public final SButton getOKButton() {
        return ok;
    }

    public final SButton getCancelButton() {
        return cancel;
    }

    /**
     * Fast edit support is editing with reduced interaction. E.g. a boolean
     * value can only have to states, true or false. So if editing is started,
     * the editor just flips the state and fires editing stopped.
     *
     * @param b a <code>boolean</code> value
     */
    public final void setFastEdit(boolean b) {
        fastEditSupport = b;
    }

    /**
     * Return if fast edit is activated.
     *
     * @return a <code>boolean</code> value
     * @see #setFastEdit
     */
    public final boolean getFastEdit() {
        return fastEditSupport;
    }

    public Object getCellEditorValue() {
        return delegate.getCellEditorValue();
    }

    public boolean isCellEditable(EventObject anEvent) {
        return delegate.isCellEditable(anEvent);
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return delegate.shouldSelectCell(anEvent);
    }

    public boolean stopCellEditing() {
        if (delegate.stopCellEditing()) {
            fireEditingStopped();
            return true;
        }

        return false;
    }

    public void cancelCellEditing() {
        delegate.cancelCellEditing();
        fireEditingCanceled();
    }

    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    private ChangeEvent changeEvent = null;

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    protected void fireEditingStopped() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((CellEditorListener)listeners[i + 1]).editingStopped(changeEvent);
            }
        }
    }


    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    protected void fireEditingCanceled() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((CellEditorListener)listeners[i + 1]).editingCanceled(changeEvent);
            }
        }
    }

    public SComponent getTreeCellEditorComponent(STree tree, Object value,
                                                 boolean isSelected,
                                                 boolean expanded,
                                                 boolean leaf, int row) {

        delegate.setValue(value);
        return editorPanel;
    }

    public SComponent getTableCellEditorComponent(STable table, Object value,
                                                  boolean isSelected,
                                                  int row, int column) {
        delegate.setValue(value);

        return editorPanel;
    }


    //
    //  Protected EditorDelegate class
    //

    /**
     * The interface all editing boils down to: setting the value for
     * the editor and retrieve its value.
     */
    protected class EditorDelegate {
        protected Object value;

        /**
         * Retrieve the value from the component used as Editor.
         * This method is called by the CellEditor to retrieve the
         * value after editing.
         *
         * @return the value managed by the Editor.
         */
        public Object getCellEditorValue() {
            return value;
        }

        /**
         * Set the Editors value. The task of this method is to
         * pass the value to the editor component so that editing
         * can be started.
         *
         * @param x the value to be edited.
         */
        public void setValue(Object x) {
            this.value = x;
        }

        public boolean isCellEditable(EventObject anEvent) {
            return true;
        }

        public boolean stopCellEditing() {
            return true;
        }

        public void cancelCellEditing() {
        }

        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }
    }
}
