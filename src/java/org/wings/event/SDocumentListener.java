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

import java.util.EventListener;

/**
 * Listener on {@link org.wings.STextComponent}s to be notified on updates on the components text (document).
 *
 * @author hengels
 */
public interface SDocumentListener extends EventListener {
    /**
     * Gives notification that there was an insert into the document.  The
     * range given by the DocumentEvent bounds the freshly inserted region.
     *
     * @param e the document event
     */
    void insertUpdate(SDocumentEvent e);

    /**
     * Gives notification that a portion of the document has been
     * removed.  The range is given in terms of what the view last
     * saw (that is, before updating sticky positions).
     *
     * @param e the document event
     */
    void removeUpdate(SDocumentEvent e);

    /**
     * Gives notification that an attribute or set of attributes changed for styled
     * documents.
     * <p><b>NOTE:</b> This is for swing compatibility. Currently you will not
     * receive this event at any time inside web contexts!
     *
     * @param e the document event
     */
    void changedUpdate(SDocumentEvent e);
}
