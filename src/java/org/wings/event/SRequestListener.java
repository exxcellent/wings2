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
 * Request listeners can be registered in the current {@link org.wings.session.Session}
 * and will receive different events during the various request phases on processing an
 * HTTP request for this session.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public interface SRequestListener extends EventListener {

    /**
     * The session is currently processing an HTTP request in the passed state.
     * <p><b>NOTE:</b> This method will be called several times caused by the differen request phases / events
     * during one HTTP request!
     *
     * @param e The current request event type.
     */
    void processRequest(SRequestEvent e);

}


