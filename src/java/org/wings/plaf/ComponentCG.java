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

package org.wings.plaf;

import org.wings.SComponent;
import org.wings.io.Device;

import java.io.IOException;
import java.io.Serializable;

public interface ComponentCG
    extends Serializable {
    /**
     * Installs the CG.
     * <p/>
     * <p><b>Note</b>: Be very careful here since this method is called from
     * the SComponent constructor! Don't call any methods which rely on
     * something that will be constructed in a subconstructor later!
     */
    void installCG(SComponent c);

    /**
     * Uninstalls the CG.
     */
    void uninstallCG(SComponent c);

    /**
     * Writes the given component to the Device.
     * <p/>
     * <p>This renders the component according to this pluggable look and
     * feel; it reads the properties of the component and genereates the
     * HTML, XML or whatever representation that is written to the Device.
     * <p/>
     * <p>This method should be called from the write method in SComponent or
     * a subclass. It delegates
     *
     * @param device    the output device.
     * @param component the component to be rendered.
     */
    void write(Device device, SComponent component) throws IOException;

    String mapSelector(String selector);
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
