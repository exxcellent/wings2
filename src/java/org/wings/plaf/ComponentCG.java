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
package org.wings.plaf;

import org.wings.SComponent;
import org.wings.style.CSSSelector;
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

    /**
     * A component may have multiple stylable areas. A tabbed pane for example has a content, the tabs,
     * the selected tab, etc. These areas are addressed by so called pseudo selectors. There are constants
     * in SComponents and derivates (prefixed with SELECTOR_), that address commonly used areas. It's the
     * responsibility of the CG to apply the styling to the respective areas. In case of HTML, one can use
     * arbitrary CSS selectors in order to style what ever is addressable by means of CSS selector.
     *
     * @param  component The component addressed by the pseudo selector
     * @param pseudoSelector A unqiue name naming a specific area of the passed component (i.e. content pane or button area).
     * @return The real-life css selector for the current browser.
     */
    CSSSelector mapSelector(SComponent component, CSSSelector pseudoSelector);
}
