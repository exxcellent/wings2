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

package org.wings.plaf.xhtml;

import java.io.IOException;
import org.wings.SLayoutManager;
import org.wings.STemplateLayout;
import org.wings.SComponent;
import org.wings.io.Device;
import org.wings.plaf.LayoutCG;
import org.wings.template.TemplateParseContext;
import org.wings.template.RangeTagHandler;
import org.wings.template.SimpleTagHandler;
import org.wings.template.LabelTagHandler;
import org.wings.template.TemplateSource;
import org.wings.template.parser.PageParser;

/**
 * @author Achim Derigs
 * @version $Revision$
 */
public class TemplateLayoutCG
    implements LayoutCG
{
    /**
     * The parser looks for the '<OBJECT></OBJECT>' - tags.
     */
    static {
        PageParser parser = PageParser.getInstance();
        parser.addTagHandler("OBJECT", RangeTagHandler.class);
        parser.addTagHandler("WINGSOBJECT", RangeTagHandler.class);
        parser.addTagHandler("TEXTAREA", RangeTagHandler.class);
        parser.addTagHandler("SELECT",   RangeTagHandler.class);
        parser.addTagHandler("INPUT",    SimpleTagHandler.class);
        parser.addTagHandler("LABEL",    LabelTagHandler.class);
    }

    /**
     *
     */
    private void write(Device device, STemplateLayout layout)
        throws IOException {

        final TemplateSource source = layout.getTemplateSource();
        SComponent container = ( SComponent ) layout.getContainer();

        if(source == null) {
            device.print("Unable to open template-file <b>'");
            device.print(source);
            device.print("'</b>");
        }
	else {
            if (Utils.hasSpanAttributes(container)) {
                device.print(" <span style=\"");
                Utils.writeSpanAttributes( device, container );
            	device.print("\">");
            }

            layout.getPageParser().process(source, new TemplateParseContext(device, layout));

            if ( Utils.hasSpanAttributes( container ) ) {
                device.print("</span>");
            }
        }
    }

    /**
     * TODO: documentation
     *
     * @param device the device to write the code to
     * @param manager the layout manager
     * @throws IOException
     */
    public void write(Device device, SLayoutManager manager)
        throws IOException {

        write(device, (STemplateLayout) manager);
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
