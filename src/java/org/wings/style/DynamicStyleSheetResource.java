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

package org.wings.style;

import org.wings.*;
import org.wings.border.SBorder;
import org.wings.plaf.ComponentCG;
import org.wings.io.Device;
import org.wings.util.ComponentVisitor;

import java.io.IOException;
import java.util.Iterator;
import java.util.Collection;

/**
 * Traverses the component hierarchy of a frame and gathers the dynamic styles
 * (attributes) of all components in a style sheet.
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class DynamicStyleSheetResource
    extends DynamicResource
{
    public DynamicStyleSheetResource(SFrame frame) {
        super(frame, "css", "text/css");
    }

    public void write(Device out)
        throws IOException
    {
        try {
            StyleSheetWriter visitor = new StyleSheetWriter(out);
            getFrame().invite(visitor);
        }
        catch (IOException e) {
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getMessage()); // UndeclaredThrowable
        }
    }

    protected static class StyleSheetWriter
        implements ComponentVisitor
    {
        Device out;

        public StyleSheetWriter(Device out) {
            this.out = out;
        }
        
        private void writeAttributesFrom(SComponent component)
            throws IOException
        {
            String selectorPrefix = "#" + component.getComponentId();
            if (component instanceof SFrame)
                selectorPrefix = "body";

            Collection dynamicStyles = component.getDynamicStyles();
            if (dynamicStyles != null) {
                ComponentCG cg = component.getCG();
                for (Iterator iterator = dynamicStyles.iterator(); iterator.hasNext();) {
                    Style style = (Style)iterator.next();
                    String selector = style.getSelector();
                    selector = cg.mapSelector(selector);
                    writeAttributes(selectorPrefix + selector, style);
                }
            }

            SBorder border = component.getBorder();
            if (border != null) {
                writeAttributes(selectorPrefix, border.getAttributes());
            }
        }
        
        private void writeAttributes(String selector, Style style)
            throws IOException
        {
            String backup = style.getSelector();
            style.setSelector(selector);
            style.write(out);
            style.setSelector(backup);
        }

        private void writeAttributes(String selector, AttributeSet attributes)
            throws IOException
        {
            out.print(selector).print("{");
            attributes.write(out);
            out.print("}\n");
        }

        public void visit(SComponent component) throws Exception {
            writeAttributesFrom(component);
        }

        public void visit(SContainer container) throws Exception {
            writeAttributesFrom(container);
            container.inviteEachComponent(this);
        }
    }
}
