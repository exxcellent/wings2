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
import org.wings.util.SStringBuilder;
import org.wings.template.parser.ParseContext;
import org.wings.template.parser.PositionReader;
import org.wings.template.parser.SGMLTag;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * A TemplateTagHandler
 *
 * @author Holger Engels
 */
public class LabelTagHandler
        extends TemplateTagHandler {
    boolean close_is_missing = false;
    SStringBuilder content = new SStringBuilder();

    /**
     * @param context  the parsing context
     * @param input    the PositionReader, located after the name token of the tag
     * @param startPosition the position where parsing of this token began
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
         * and to place the Reader to the position
         * after the closing '>'
         */
        startTag.parse(input);

        /*
         * The offset is the space the reader skipped
         * before it reached the opening '<'
         */
        startPos = startPosition + startTag.getOffset();

        /*
         * get properties
         */
        properties = startTag.getAttributes();

        if (startTag.value("FOR", null) == null)
            return null;

        endPos = input.getPosition();  // in case </label> is missing

        SGMLTag endTag;
        int len;
        do {
            len = readContent(input, content);
            endTag = new SGMLTag(input, false);
        } while (!endTag.finished() && !endTag.isNamed(endTagName));

        if (startTag.finished())
            close_is_missing = true;
        else
            endPos = input.getPosition();

        return endTag;
    }

    public int readContent(Reader r, SStringBuilder content)
            throws IOException {
        int c, len = 0;
        do {
            r.mark(1);
            c = r.read();
            len++;
            content.append((char) c);
        } while (c >= 0 && c != '<');
        r.reset();
        content.setLength(content.length() - 1);
        return len - 1;
    }

    public String getContent() {
        return content.toString();
    }

    public String getFor() {
        return (String) properties.get("FOR");
    }

    /**
     * actually perform the action associated with this tag.
     *
     * @throws Exception anything can happen .. and throw an Exception
     *                   which is caught in PageParser
     */
    public void executeTag(ParseContext context, InputStream input)
            throws Exception {
        TemplateParseContext tcontext = (TemplateParseContext) context;
        copy(input, tcontext.getDevice(), getTagLength(), new byte[512]);

        // warn, if the closing tag was not found ..
        if (close_is_missing) {
            Device sink = tcontext.getDevice();
            sink.print("<table bgcolor='#FFAA55'><tr><td>");
            sink.print("&nbsp;<blink><b>");
            sink.print("closing tag missing");
            sink.print(" for '<em>" + name + "</em>'");
            sink.print("</b></blink>&nbsp;");
            sink.print("</td></tr></table>");
        }
    }

    private static void copy(InputStream in, Device device, long length, byte buf[])
            throws IOException {
        int len;
        boolean limited = (length >= 0);
        int rest = limited ? (int) length : buf.length;
        while (rest > 0 &&
                (len = in.read(buf, 0,
                        (rest > buf.length) ? buf.length : rest)) > 0) {
            device.write(buf, 0, len);
            if (limited) rest -= len;
        }
    }
}


