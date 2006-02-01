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
package org.wings.style;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.SFrame;
import org.wings.io.Device;
import org.wings.resource.DynamicResource;
import java.io.IOException;

/**
 * The DynamicStyleSheetResource is an dynamically generated and updated
 * externalized resource containing a dynamically generated CSS file.
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class DynamicStyleSheetResource extends DynamicResource {
    /**
     * Apache jakarta commons logger
     */
    private final static Log log = LogFactory.getLog(DynamicStyleSheetResource.class);

    public DynamicStyleSheetResource(SFrame frame) {
        super(frame, "css", "text/css");
    }

    public void write(Device out)
            throws IOException {
        try {
            CSSStyleSheetWriter visitor = new CSSStyleSheetWriter(out);
            getFrame().invite(visitor);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Unexpected exception", e);
            throw new IOException(e.getMessage()); // UndeclaredThrowable
        }
    }

}
