/*
 * Copyright 2000,2006 wingS development team.
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
 * Empty default implementation of {@link SDocumentListener}.
 *
 * @author Christian Schyma 
 * @see SDocumentEvent
 * @see SDocumentListener
 */
public abstract class SDocumentAdapter implements SDocumentListener {
    public void insertUpdate(SDocumentEvent e) {};
    public void removeUpdate(SDocumentEvent e) {};
    public void changedUpdate(SDocumentEvent e) {};
}
