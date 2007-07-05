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
package org.wings.template;

import org.wings.io.Device;
import org.wings.template.parser.ParseContext;
import org.wings.template.parser.PositionReader;
import org.wings.template.parser.SGMLTag;

import java.io.IOException;
import java.io.InputStream;

/**
 * A TemplateTagHandler
 *
 * @author <A href="mailto:zeller@think.de">Henner Zeller</A>
 */
public class RangeTagHandler extends TemplateTagHandler {
    boolean close_is_missing = false;

    /**
     * Parse special tag.
     *
     * @param context  The ParseContext
     * @param input    The PositionReader, located after the Name token of the Tag
     * @param startPosition The Position parsing of this token began
     * @param startTag the SGMLTag found in the file.
     */
    public SGMLTag parseTag(ParseContext context,
                            PositionReader input,
                            long startPosition,
                            SGMLTag startTag)
            throws IOException {
        final String startTagName = startTag.getName();
        final String endTagName = "/" + startTagName;

        /*
         * parse the full tag to get all parameters
         * (i.e. an optional 'format'-parameter)
         * and to place the Reader at the position
         * after the closing '>'
         */
        startTag.parse(input);

        /*
         * The Offset is the space the reader skipped
         * before it reached the opening '<'
         *   <!-- a comment --> some garbage <DATE>
         * ^----- offset --------------------^
         */
        startPos = startPosition + startTag.getOffset();

        /*
         * get properties
         */
        properties = startTag.getAttributes();

        name = startTag.value("NAME", null);
        if (name == null)
            return null;

        endPos = input.getPosition();  // in case </component> is missing

        while (!startTag.finished()) {
            startTag = new SGMLTag(input, true);
            if (startTag.isNamed(endTagName) || startTag.isNamed(startTagName))
                break;
        }

        // Either EOF or newly opened COMPONENT (unexpectedly)
        if (startTag.finished() || startTag.isNamed(startTagName)) {
            close_is_missing = true;
        } else {
            // The current Position is after the closing '>'
            endPos = input.getPosition();
        }

        // remove properties, which are not necessary for the PropertyManager
        properties.remove("NAME");
        properties.remove("TYPE");

        return startTag;
    }

    /**
     * actually perform the action associated with this tag.
     *
     * @throws Exception anything can happen .. and throw an Exception
     *                   which is caught in PageParser
     */
    public void executeTag(ParseContext context, InputStream input)
            throws Exception {
        super.executeTag(context, input);

        // warn, if the closing tag was not found ..
        if (close_is_missing) {
            TemplateParseContext tcontext = (TemplateParseContext) context;
            Device sink = tcontext.getDevice();
            sink.print("<table bgcolor='#FFAA55'><tr><td>");
            sink.print("&nbsp;<blink><b>");
            sink.print("closing tag missing");
            sink.print(" for '<em>" + name + "</em>'");
            sink.print("</b></blink>&nbsp;");
            sink.print("</td></tr></table>");
        }
    }
}


