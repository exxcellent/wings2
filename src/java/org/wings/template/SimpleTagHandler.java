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

package org.wings.template;

import java.io.*;
import java.util.*;
import org.wings.template.parser.*;

/**
 * A TemplateTagHandler 
 *
 * @author <A href="mailto:zeller@think.de">Henner Zeller</A>
 * @version $Revision$
 */
public class SimpleTagHandler extends TemplateTagHandler
{
    /**
     * Parse special tag.
     * @param config    Servlet configuration
     * @param input     The PositionReader, located after the Name token of the Tag
     * @param startPos  The Position parsing of this token began
     * @param startTag  the SGMLTag found in the file.
     */
    public SGMLTag parseTag(ParseContext context,
                            PositionReader input,
                            long startPosition,
                            SGMLTag tag)
        throws IOException
    {
        /*
         * parse the full tag to get all parameters
         * (i.e. an optional 'format'-parameter)
         * and to place the Reader at the position
         * after the closing '>'
         */
        tag.parse(input);

        /*
         * The Offset is the space the reader skipped
         * before it reached the opening '<'
         *   <!-- a comment --> some garbage <DATE>
         * ^----- offset --------------------^
         */
        startPos = startPosition + tag.getOffset();

        /*
         * get required properties
         */
        name = tag.value ("NAME", null);
        if (name == null)
            return null;
        
        /*
         * special handling for radio buttons. They react on the
         * constraint name "NAME=VALUE"
         */
        String type = tag.value("TYPE", null);
        if (type != null && "RADIO".equals(type.toUpperCase())) {
            String value = tag.value("VALUE", null);
            if (value != null) 
                name = name + "=" + value;
        }

        endPos = input.getPosition();

        properties = tag.getAttributes();
        properties.remove("NAME");
        properties.remove("TYPE");
        properties.remove("VALUE");

        return tag;
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
