/* $Id $ */
package org.wings.text;

import javax.swing.text.BadLocationException;

import org.wings.event.SDocumentListener;

/**
 * @author hengels
 * @version $Revision$
 */
public interface SDocument
{
    /**
     * Returns number of characters of content currently
     * in the document.
     *
     * @return number of characters >= 0
     */
    public int getLength();

    /**
     * Registers the given observer to begin receiving notifications
     * when changes are made to the document.
     *
     * @param listener the observer to register
     * @see SDocument#removeDocumentListener
     */
    public void addDocumentListener(SDocumentListener listener);

    /**
     * Unregisters the given observer from the notification list
     * so it will no longer receive change updates.
     *
     * @param listener the observer to register
     * @see SDocument#addDocumentListener
     */
    public void removeDocumentListener(SDocumentListener listener);

    /**
     * Removes a portion of the content of the document.
     * This will cause a DocumentEvent of type
     * DocumentEvent.EventType.REMOVE to be sent to the
     * registered DocumentListeners, unless an exception
     * is thrown.  The notification will be sent to the
     * listeners by calling the removeUpdate method on the
     * DocumentListeners.
     *
     * @param offs  the offset from the beginning >= 0
     * @param len   the number of characters to remove >= 0
     * @exception BadLocationException  some portion of the removal range
     *   was not a valid part of the document.  The location in the exception
     *   is the first bad position encountered.
     * @see javax.swing.event.DocumentEvent
     * @see javax.swing.event.DocumentListener
     * @see javax.swing.event.UndoableEditEvent
     * @see javax.swing.event.UndoableEditListener
     */
    public void remove(int offs, int len) throws BadLocationException;

    /**
     * Inserts a string of content.  This will cause a DocumentEvent
     * of type DocumentEvent.EventType.INSERT to be sent to the
     * registered DocumentListers, unless an exception is thrown.
     * The DocumentEvent will be delivered by calling the
     * insertUpdate method on the DocumentListener.
     * The offset and length of the generated DocumentEvent
     * will indicate what change was actually made to the Document.
     *
     * @param offset  the offset into the document to insert the content >= 0.
     *    All positions that track change at or after the given location
     *    will move.
     * @param string the string to insert
     */
    public void insert(int offset, String string) throws BadLocationException;


    /**
     * Fetches the text contained within the given portion
     * of the document.
     *
     * @param offset  the offset into the document representing the desired
     *   start of the text >= 0
     * @param length  the length of the desired string >= 0
     * @return the text, in a String of length >= 0
     * @exception BadLocationException  some portion of the given range
     *   was not a valid part of the document.  The location in the exception
     *   is the first bad position encountered.
     */
    public String getText(int offset, int length) throws BadLocationException;

    public String getText();

    public void setText(String text);
}
