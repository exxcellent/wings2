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

import org.wings.event.SDocumentEvent;
import org.wings.event.SDocumentListener;
import org.wings.text.DefaultDocument;
import org.wings.text.SDocument;

import javax.swing.text.BadLocationException;

/**
 * Abstract base class of input text components like {@link STextArea} and {@link STextField}.
 * Requires a surrounding {@link SForm} element!
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public abstract class STextComponent extends SComponent implements LowLevelEventListener, SDocumentListener {

    private boolean editable = true;

    private SDocument document;

    /**
     * @see LowLevelEventListener#isEpochCheckEnabled()
     */
    private boolean epochCheckEnabled = true;

    public STextComponent() {
        this(new DefaultDocument(), true);
    }

    public STextComponent(String text) {
        this(new DefaultDocument(text), true);
    }

    public STextComponent(SDocument document) {
        this(document, true);
    }

    public STextComponent(SDocument document, boolean editable) {
        setDocument(document);
        setEditable(editable);
    }

    public SDocument getDocument() {
        return document;
    }

    public void setDocument(SDocument document) {
        if (document == null)
            throw new IllegalArgumentException("null");

        SDocument oldDocument = this.document;
        this.document = document;
        if (oldDocument != null)
            oldDocument.removeDocumentListener(this);
        document.addDocumentListener(this);
        reloadIfChange(oldDocument, document, ReloadManager.STATE);
    }

    /**
     * Defines if the textcomponent is editable or not.
     * @see #isEditable()
     * @param ed true if the text component is to be editable false if not.
     */
    public void setEditable(boolean ed) {
        boolean oldEditable = editable;
        editable = ed;
        if (editable != oldEditable)
            reload(ReloadManager.STATE);
    }


    public boolean isEditable() {
        return editable;
    }

    /**
     * Sets the text of the component to the specified text.
     * @see #getText()
     * @param text the new text for the component.
     */
    public void setText(String text) {
        document.setText(text);
    }


    public String getText() {
        return document.getText();
    }

    /**
     * Appends the given text to the end of the document. Does nothing
     * if the string is null or empty.
     *
     * @param text the text to append.
     */
    public void append(String text) {
        try {
            document.insert(document.getLength(), text);
        } catch (BadLocationException e) {
        }
    }

    public void processLowLevelEvent(String action, String[] values) {
        processKeyEvents(values);

        if (isEditable() && isEnabled()) {
            if (getText() == null || !getText().equals(values[0])) {
                getDocument().setDelayEvents(true);
                setText(values[0]);
                getDocument().setDelayEvents(false);
                SForm.addArmedComponent(this);
            }
        }
    }

    public void addDocumentListener(SDocumentListener listener) {
        getDocument().addDocumentListener(listener);
    }

    public void removeDocumentListener(SDocumentListener listener) {
        getDocument().removeDocumentListener(listener);
    }

    public void fireIntermediateEvents() {
    	getDocument().fireDelayedIntermediateEvents();
    }
    
    public void fireFinalEvents() {
        super.fireFinalEvents();
        getDocument().fireDelayedFinalEvents();
    }

    /**
     * @see LowLevelEventListener#isEpochCheckEnabled()
     */
    public boolean isEpochCheckEnabled() {
        return epochCheckEnabled;
    }

    /**
     * @see LowLevelEventListener#isEpochCheckEnabled()
     */
    public void setEpochCheckEnabled(boolean epochCheckEnabled) {
        this.epochCheckEnabled = epochCheckEnabled;
    }

    // Changes to the document should force a reload.
    public void insertUpdate(SDocumentEvent e) {
        reload(ReloadManager.STATE);
    }

    public void removeUpdate(SDocumentEvent e) {
        reload(ReloadManager.STATE);
    }

    public void changedUpdate(SDocumentEvent e) {
        reload(ReloadManager.STATE);
    }
}


