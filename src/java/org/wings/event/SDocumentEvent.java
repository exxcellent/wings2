/* $Id$ */
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
package org.wings.event;

import org.wings.text.SDocument;

import java.awt.*;

/**
 * @author hengels
 * @version $Revision$
 */
public class SDocumentEvent extends AWTEvent {
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
        return (SDocument) getSource();
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
