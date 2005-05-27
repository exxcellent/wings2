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

/**
 * A CSS attribute is a property on any abritriary HTML element which can be set via CSS.
 *
 * You use CSS selectors ({@link CSSSelector}) to define <b>which</b> elements you want to modify and define
 * with CSS properties {@link CSSProperty} <b>what</b> visual property you want to modify.
 *
 * {@see http://www.w3.org/TR/REC-CSS2/selector.html}
 *
 * @author bschmid
 */
public class CSSProperty {

    public final static CSSProperty BACKGROUND = new CSSProperty("background");

    public final static CSSProperty BACKGROUND_ATTACHMENT = new CSSProperty("background-attachment");

    public final static CSSProperty BACKGROUND_COLOR = new CSSProperty("background-color");

    public final static CSSProperty BACKGROUND_IMAGE = new CSSProperty("background-image");

    public final static CSSProperty BACKGROUND_POSITION = new CSSProperty("background-position");

    public final static CSSProperty BACKGROUND_REPEAT = new CSSProperty("background-repeat");

    public final static CSSProperty BORDER = new CSSProperty("border");

    public final static CSSProperty BORDER_BOTTOM = new CSSProperty("border-bottom");

    public final static CSSProperty BORDER_BOTTOM_WIDTH = new CSSProperty("border-bottom-width");

    public final static CSSProperty BORDER_COLOR = new CSSProperty("border-color");

    public final static CSSProperty BORDER_LEFT = new CSSProperty("border-left");

    public final static CSSProperty BORDER_LEFT_WIDTH = new CSSProperty("border-left-width");

    public final static CSSProperty BORDER_RIGHT = new CSSProperty("border-right");

    public final static CSSProperty BORDER_RIGHT_WIDTH = new CSSProperty("border-right-width");

    public final static CSSProperty BORDER_STYLE = new CSSProperty("border-style");

    public final static CSSProperty BORDER_TOP = new CSSProperty("border-top");

    public final static CSSProperty BORDER_TOP_WIDTH = new CSSProperty("border-top-width");

    public final static CSSProperty BORDER_WIDTH = new CSSProperty("border-width");

    public final static CSSProperty CLEAR = new CSSProperty("clear");

    public final static CSSProperty COLOR = new CSSProperty("color");

    public final static CSSProperty DISPLAY = new CSSProperty("display");

    public final static CSSProperty FLOAT = new CSSProperty("float");

    public final static CSSProperty FONT = new CSSProperty("font");

    public final static CSSProperty FONT_COLOR = new CSSProperty("font-color");

    public final static CSSProperty FONT_FAMILY = new CSSProperty("font-family");

    public final static CSSProperty FONT_SIZE = new CSSProperty("font-size");

    public final static CSSProperty FONT_STYLE = new CSSProperty("font-style");

    public final static CSSProperty FONT_VARIANT = new CSSProperty("font-variant");

    public final static CSSProperty FONT_WEIGHT = new CSSProperty("font-weight");

    public final static CSSProperty HEIGHT = new CSSProperty("height");

    public final static CSSProperty LETTER_SPACING = new CSSProperty("letter-spacing");

    public final static CSSProperty LINE_HEIGHT = new CSSProperty("line-height");

    public final static CSSProperty LIST_STYLE = new CSSProperty("list-style");

    public final static CSSProperty LIST_STYLE_IMAGE = new CSSProperty("list-style-image");

    public final static CSSProperty LIST_STYLE_POSITION = new CSSProperty("list-style-position");

    public final static CSSProperty LIST_STYLE_TYPE = new CSSProperty("list-style-type");

    public final static CSSProperty MARGIN = new CSSProperty("margin");

    public final static CSSProperty MARGIN_BOTTOM = new CSSProperty("margin-bottom");

    public final static CSSProperty MARGIN_LEFT = new CSSProperty("margin-left");

    public final static CSSProperty MARGIN_RIGHT = new CSSProperty("margin-right");

    public final static CSSProperty MARGIN_TOP = new CSSProperty("margin-top");

    public final static CSSProperty PADDING = new CSSProperty("padding");

    public final static CSSProperty PADDING_BOTTOM = new CSSProperty("padding-bottom");

    public final static CSSProperty PADDING_LEFT = new CSSProperty("padding-left");

    public final static CSSProperty PADDING_RIGHT = new CSSProperty("padding-right");

    public final static CSSProperty PADDING_TOP = new CSSProperty("padding-top");

    public final static CSSProperty TEXT_ALIGN = new CSSProperty("text-align");

    public final static CSSProperty TEXT_DECORATION = new CSSProperty("text-decoration");

    public final static CSSProperty TEXT_INDENT = new CSSProperty("text-indent");

    public final static CSSProperty TEXT_TRANSFORM = new CSSProperty("text-transform");

    public final static CSSProperty VERTICAL_ALIGN = new CSSProperty("vertical-align");

    public final static CSSProperty WORD_SPACING = new CSSProperty("word-spacing");

    public final static CSSProperty WHITE_SPACE = new CSSProperty("white-space");

    public final static CSSProperty WIDTH = new CSSProperty("width");

    public final static CSSProperty BORDER_SPACING = new CSSProperty("border-spacing");

    public final static CSSProperty CAPTION_SIDE = new CSSProperty("caption-side");

    private final String name;

    public CSSProperty(String cssAttributeName) {
        this.name = cssAttributeName;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CSSProperty)) return false;
        final CSSProperty cssProperty = (CSSProperty) o;

        // CSS properties are CASE INSENSITIVE!
        return (name.equalsIgnoreCase(cssProperty.name));
    }

    public int hashCode() {
        return name.hashCode();
    }

    public String toString() {
        return getName();
    }

}
