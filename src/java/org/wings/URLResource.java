/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings;

import java.io.Serializable;

/**
 * Some Resource, that can be accessed through an URL.
 *
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 */
public interface URLResource extends Serializable
{
    /**
     * returns the URL, this resource can be fetched from. This URL may
     * be relative, usually if generated from the externalizer.
     */
    SimpleURL getURL();
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
