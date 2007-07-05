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
 * Render listener are called during the rendering process of a component.
 * The rendering process starts after completion of the event processing (parsing and
 * delegating the events contained in the initial request).
 *
 * Created: Wed Nov  6 10:17:41 2002
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:B.Schmid@eXXcellent.de">Benjamin Schmid</a>
 */
public interface SRenderListener extends EventListener {

    /**
     * The rendering of the listened component is abut to start.
     * @param renderEvent The render event information.
     */
    void startRendering(SRenderEvent renderEvent);

    /**
     * The rendering of the listened component finished.
     * @param renderEvent The render event information.
     */
    void doneRendering(SRenderEvent renderEvent);

}

