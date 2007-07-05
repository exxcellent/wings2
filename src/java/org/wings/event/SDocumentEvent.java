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
package org.wings.event;

import org.wings.text.SDocument;

import java.awt.*;

/**
 * A document event fire on any document change (i.e. change of a text in a {@link org.wings.STextField}).
 *
 * @author hengels
 */
public class SDocumentEvent extends AWTEvent {
    /**
     * An style attribute changed. <b>DO NOT EXPECT CHARACTER CHANGES HERE!</b> This event type will not occur inside wings!
     */
    public final static int CHANGE = 1;
    /**
     * A string has been inserted.
      */
    public final static int INSERT = 2;
    /**
     * A string has been removed.
     */
    public final static int REMOVE = 3;

    private int offset;
    private int length;

    public SDocumentEvent(SDocument document, int offset, int length, int type) {
        super(document, type);
        this.offset = offset;
        this.length = length;
    }

    /**
     * Gets the source document of the change event.
     * @return the document
     */
    public SDocument getDocument() {
        return (SDocument) getSource();
    }

    /**
     * Returns the offset where the change in the document starts.
     * @return Offset where remove/insert occured (0 = first letter)
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Returns the length of the change.
     * @return The length of the insert/remove
     */
    public int getLength() {
        return length;
    }

    /**
     * Gets the type of the Event.
     * @return {@link #INSERT} or {@link #REMOVE}
     */
    public int getType() {
        return getID();
    }
}
