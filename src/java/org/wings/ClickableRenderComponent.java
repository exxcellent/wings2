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

import org.wings.RequestURL;

/**
 * Tagging interface - a component implementing this interface states, that
 * it is able to produce events on user mouse click.
 *
 * <p>E.g. the TreeCG implementation renders nodes with each 
 * node being a SLable with icon and text. Or the TableCG needs to receive
 * events on clicked cells to switch to the edit cell renderer.
 * 
 * <p>What is simple in graphical user interfaces -- they just receive a click
 * and know the component below where it occured -- is not that easy
 * in HTML rendering. In order to receive events here, we've to write
 * some anchor around it.
 * This is what this ClickableRenderComponent is all
 * about: it states, that it can render an anchor around its elements it
 * renders, so that the application gets the event.
 * 
 * <p>Why is this needed, and we cannot just write an anchor tag around
 * the component being rendered? Consider the layout for the SLabel
 * for instance. It renderes the Icon and text using a html table, so the
 * the part that wants the event (e.g. the TreeCG) cannot just
 * generate anchor tags around the whole SLabel (browsers cannot handle
 * a-href tags, that surround complex containers like tables).
 * A ClickableRenderComponent takes care of this (The SLabel implements a
 * ClickableRenderComponent and its plaf writes the appropriate a-href
 * tags around each element (icon and text)). 
 * See the LabelCG plaf implementation, if you are curious.
 *
 * <p>If you want your own components be rendered correctly in STree, STables 
 * or SAnchors, you need to implement this interface. And if you are lazy, then
 * you just use the SLabel which already does this ;-)
 *
 * <p>This interface is only a tagging interface, the actual work is done
 * in the plaf of this component.
 * The use of this interface is as follows: if a plaf needs to be informed by
 * an event from some sub-component and that component is 
 * <code>instanceof</code> ClickableRenderComponent, it pushes the URL on
 * some {@link org.wings.util.AnchorRenderStack global stack} and lets 
 * the sub-component render itself ({@link SComponent#write(Device)}).
 * Finally, the URL is popped. So the plaf of the ClickableRender-Component 
 * has to take care, that the URL is rendered.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 * @version $Revision$
 */
public interface ClickableRenderComponent
{   
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
