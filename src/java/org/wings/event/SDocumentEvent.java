/* $Id$ */
package org.wings.event;

import java.awt.*;

import org.wings.text.SDocument;

/**
 * @author hengels
 * @version $Revision$
 */
public class SDocumentEvent extends AWTEvent
{
    public final static int CHANGE = 1;
    public final static int INSERT = 2;
    public final static int REMOVE = 3;

    private int offset;
    private int length;

    public SDocumentEvent(SDocument document, int offset, int length, int type) {
        super(document, type);
        this.offset = offset;
        this.length = length;
    }

    public SDocument getDocument() {
        return (SDocument)getSource();
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public int getType() {
        return getID();
    }
}
