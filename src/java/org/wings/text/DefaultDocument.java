/* $Id $ */
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
package org.wings.text;

import java.lang.reflect.Array;
import java.util.List;

import javax.swing.event.DocumentEvent;
import javax.swing.event.EventListenerList;
import javax.swing.text.BadLocationException;

import org.wings.event.SDocumentEvent;
import org.wings.event.SDocumentListener;
import org.wings.util.SStringBuilder;
import org.wings.util.EditTranscriptGenerator;

/**
 * @author hengels
 * @version $Revision$
 */
public class DefaultDocument implements SDocument {
    private final SStringBuilder buffer = new SStringBuilder();
    private EventListenerList listeners = null;

    public DefaultDocument() {
    }

    public DefaultDocument(String text) {
        buffer.append(text);
    }

    public void setText(String text) {
        String origText = buffer.toString();
        if (origText.equals(text)) {
            return;
        }
        buffer.setLength(0);
        if (text != null){
            buffer.append(text);
            if(listeners.getListenerCount() > 0){
                // If there are any document listeners: Generate document change events!
            	List actions = EditTranscriptGenerator.generateEvents(origText, text);
                // and fire them!
            	for(int i = 0; i < actions.size(); i++){
            		DocumentEvent de = (DocumentEvent) actions.get(i);
            		if(de.getType().equals(DocumentEvent.EventType.INSERT)){
            			fireInsertUpdate(de.getOffset(), de.getLength());
            		} else if(de.getType().equals(DocumentEvent.EventType.REMOVE)){
            			fireRemoveUpdate(de.getOffset(), de.getLength());
            		}
            	}
            }
        }
    }

    public String getText() {
        return buffer.toString();
    }

    public String getText(int offset, int length) throws BadLocationException {
        try {
            return buffer.substring(offset, length);
        } catch (IndexOutOfBoundsException e) {
            throw new BadLocationException(e.getMessage(), offset);
        }
    }

    public int getLength() {
        return buffer.length();
    }

    public void remove(int offset, int length) throws BadLocationException {
        if (length == 0) {
            return;
        }
        try {
            buffer.delete(offset, offset + length);
            fireRemoveUpdate(offset, length);
        } catch (IndexOutOfBoundsException e) {
            throw new BadLocationException(e.getMessage(), offset);
        }
    }

    public void insert(int offset, String string) throws BadLocationException {
        if (string == null || string.length() == 0) {
            return;
        }
        try {
            buffer.insert(offset, string);
            fireInsertUpdate(offset, string.length());
        } catch (IndexOutOfBoundsException e) {
            throw new BadLocationException(e.getMessage(), offset);
        }
    }

    public SDocumentListener[] getDocumentListeners() {
    	if (listeners != null) {
            return (SDocumentListener[]) listeners.getListeners(SDocumentListener.class);
        } else {
            return (SDocumentListener[]) Array.newInstance(SDocumentListener.class, 0);
        }
	}

	public void addDocumentListener(SDocumentListener listener) {
        if (listeners == null)
            listeners = new EventListenerList();
        listeners.add(SDocumentListener.class, listener);
    }

    public void removeDocumentListener(SDocumentListener listener) {
        if (listeners == null)
            return;
        listeners.remove(SDocumentListener.class, listener);
    }

    protected void fireInsertUpdate(int offset, int length) {
        if (listeners == null || listeners.getListenerCount() == 0)
            return;

        SDocumentEvent e = new SDocumentEvent(this, offset, length, SDocumentEvent.INSERT);

        Object[] listeners = this.listeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            ((SDocumentListener) listeners[i + 1]).insertUpdate(e);
        }
    }

    protected void fireRemoveUpdate(int offset, int length) {
        if (listeners == null || listeners.getListenerCount() == 0)
            return;

        SDocumentEvent e = new SDocumentEvent(this, offset, length, SDocumentEvent.REMOVE);

        Object[] listeners = this.listeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            ((SDocumentListener) listeners[i + 1]).removeUpdate(e);
        }
    }

    protected void fireChangeUpdate(int offset, int length) {
        if (listeners == null || listeners.getListenerCount() == 0)
            return;

        SDocumentEvent e = new SDocumentEvent(this, offset, length, SDocumentEvent.CHANGE);

        Object[] listeners = this.listeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            ((SDocumentListener) listeners[i + 1]).changedUpdate(e);
        }
    }
}
