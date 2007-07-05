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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.EventObject;
import java.util.Arrays;

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

import org.wings.resource.ResourceManager;
import org.wings.table.STableCellEditor;

/**
 * Default table cell editor.
 * <p/> 
 * In order to see the graphics,
 * you need the Java look and feel graphics (jlfgr*.jar)
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 */
public class SDefaultCellEditor
        implements STableCellEditor {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * The default ok button icon.
     */
    private static final SIcon OK_BUTTON_ICON = (SIcon) ResourceManager.getObject("SDefaultCellEditor.okIcon", SIcon.class);

    /**
     * The default cancel button icon.
     */
    private static final SIcon CANCEL_BUTTON_ICON = (SIcon) ResourceManager.getObject("SDefaultCellEditor.cancelIcon", SIcon.class);

    /**
     * Label for displaying (error)-messages. It is unvisible, until a message
     * is set.
     */
    protected final SLabel messageLabel;

    /**
     * Panel for edit fields.
     */
    protected final EditorPanel editorPanel;

    /**
     * If this button is pressed, editing is tried to stop. If input validation
     * found no error, editing is stopped, else an error message is displayed
     */
    protected final SClickable ok;

    /**
     * If this button is pressed, editing is canceled.
     */
    protected final SClickable cancel;

    /**
     * Store here the CellEditorListener
     */
    protected final EventListenerList listenerList;

    /**
     * Event listener, which set the fire... indicators. This event listener is
     * added to the buttons {@link #ok} and {@link #cancel}
     */
    private final ActionListener fireEventListener = new FireEventListener();

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
        this.editorPanel = new EditorPanel();
        this.ok = new SClickable();
        this.cancel = new SClickable();
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
                ((STextField) editorComponent).setText(v !=  null ? v.toString() : null);
            }

            public Object getCellEditorValue() {
                String text = ((STextField) editorComponent).getText();
                return "".equals(text) ? null : text;
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
                    bool = ((Boolean) v).booleanValue();
                } else if (v instanceof String) {
                    Boolean b = Boolean.valueOf((String) v);
                    bool = b.booleanValue();
                } else {
                    bool = false;
                }

                if (fastEditSupport) {
                    ((SCheckBox) editorComponent).setSelected(!bool);
                    SDefaultCellEditor.this.stopCellEditing();
                } else {
                    ((SCheckBox) editorComponent).setSelected(bool);
                }
            }

            public Object getCellEditorValue() {
                return Boolean.valueOf(((SCheckBox) editorComponent).isSelected());
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
        ok.setEvent("ok");
        ok.setIcon(OK_BUTTON_ICON);
        ok.setToolTipText("ok");
        ok.setEventTarget(editorPanel);

        cancel.setEvent("cancel");
        cancel.setIcon(CANCEL_BUTTON_ICON);
        cancel.setToolTipText("cancel");
        cancel.setEventTarget(editorPanel);

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

    public final SClickable getOKButton() {
        return ok;
    }

    public final SClickable getCancelButton() {
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
                ((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
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
                ((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
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

    private final class FireEventListener implements ActionListener, Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == ok) {
                stopCellEditing();
            } else if (e.getSource() == cancel) {
                cancelCellEditing();
            }
        }
    }

    private class EditorPanel extends SPanel
        implements LowLevelEventListener
    {
        boolean okFinalEvents;
        boolean cancelFinalEvents;

        public EditorPanel() {
            super(new SBoxLayout(SBoxLayout.HORIZONTAL));
        }

        public void processLowLevelEvent(String name, String[] values) {
            if (name.endsWith("_panel")) {
                if ("ok".equals(values[0]))
                    okFinalEvents = true;
                else if ("cancel".equals(values[0]))
                    cancelFinalEvents = true;
            }
            else if (name.endsWith("_ed") && editorComponent instanceof LowLevelEventListener) {
                LowLevelEventListener lowLevelEventListener = (LowLevelEventListener) editorComponent;
                lowLevelEventListener.processLowLevelEvent(name, values);
            }

            SForm.addArmedComponent(this);
        }

        public void fireIntermediateEvents() {
            if (okFinalEvents)
                stopCellEditing();
            if (cancelFinalEvents)
                cancelCellEditing();
            okFinalEvents = false;
            cancelFinalEvents = false;
        }

        public void fireFinalEvents() {
            if (editorComponent instanceof LowLevelEventListener) {
                LowLevelEventListener lowLevelEventListener = (LowLevelEventListener) editorComponent;
                lowLevelEventListener.fireFinalEvents();
            }
        }

        public void setNameRaw(String uncheckedName) {
            super.setNameRaw(uncheckedName + "_panel");
            editorComponent.setNameRaw(uncheckedName + "_ed");
            ok.setNameRaw(uncheckedName + "_ok");
            cancel.setNameRaw(uncheckedName + "_cl");
        }

        public boolean isEpochCheckEnabled() {
            return true;
        }
    }
}
