/* $Id $ */
package org.wings.text;

import javax.swing.text.BadLocationException;
import javax.swing.event.EventListenerList;

import org.wings.event.*;

/**
 * @author hengels
 * @version $Revision$
 */
public class DefaultDocument implements SDocument
{
    private final StringBuffer buffer = new StringBuffer();
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
        buffer.append(text);
        fireChangeUpdate(0, buffer.length());
    }

    public String getText() {
        return buffer.toString();
    }

    public String getText(int offset, int length) throws BadLocationException {
        try {
            return buffer.substring(offset, length);
        }
        catch (IndexOutOfBoundsException e) {
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
        }
        catch (IndexOutOfBoundsException e) {
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
        }
        catch (IndexOutOfBoundsException e) {
            throw new BadLocationException(e.getMessage(), offset);
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
                ((SDocumentListener)listeners[i + 1]).insertUpdate(e);
        }
    }

    protected void fireRemoveUpdate(int offset, int length) {
        if (listeners == null || listeners.getListenerCount() == 0)
            return;

        SDocumentEvent e = new SDocumentEvent(this, offset, length, SDocumentEvent.INSERT);

        Object[] listeners = this.listeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            ((SDocumentListener)listeners[i + 1]).removeUpdate(e);
        }
    }

    protected void fireChangeUpdate(int offset, int length) {
        if (listeners == null || listeners.getListenerCount() == 0)
            return;

        SDocumentEvent e = new SDocumentEvent(this, offset, length, SDocumentEvent.INSERT);

        Object[] listeners = this.listeners.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            ((SDocumentListener)listeners[i + 1]).changedUpdate(e);
        }
    }
}
