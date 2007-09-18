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

package org.wings.style;

import java.io.Serializable;
import java.util.*;

/**
 * A CSS attribute is a property on any abritriary HTML element which can be set via CSS.
 *
 * You use CSS selectors ({@link Selector}) to define <b>which</b> elements you want to modify and define
 * with CSS properties {@link CSSProperty} <b>what</b> visual property you want to modify.
 *
 * <p>Please refer to <a href="http://www.w3.org/TR/REC-CSS2/selector.html">http://www.w3.org/TR/REC-CSS2/selector.html</a> 
 * for details
 *
 * @author bschmid
 */
public class CSSProperty implements Serializable {

    /* These CSSProperty Constants are the complete CSS2 standard for visual
     * media as in the CSS2 spec at http://www.w3.org/TR/REC-CSS2/propidx.html
     */

    public final static CSSProperty BACKGROUND = new CSSProperty("background");

    public final static CSSProperty BACKGROUND_ATTACHMENT = new CSSProperty("background-attachment");

    public final static CSSProperty BACKGROUND_COLOR = new CSSProperty("background-color");

    public final static CSSProperty BACKGROUND_IMAGE = new CSSProperty("background-image");

    public final static CSSProperty BACKGROUND_POSITION = new CSSProperty("background-position");

    public final static CSSProperty BACKGROUND_REPEAT = new CSSProperty("background-repeat");

    public final static CSSProperty BORDER = new CSSProperty("border");

    public final static CSSProperty BORDER_BOTTOM = new CSSProperty("border-bottom");

    public final static CSSProperty BORDER_BOTTOM_COLOR = new CSSProperty("border-bottom-color");

    public final static CSSProperty BORDER_BOTTOM_STYLE = new CSSProperty("border-bottom-style");

    public final static CSSProperty BORDER_BOTTOM_WIDTH = new CSSProperty("border-bottom-width");

    public final static CSSProperty BORDER_COLLAPSE = new CSSProperty("border-collapse");

    public final static CSSProperty BORDER_COLOR = new CSSProperty("border-color");

    public final static CSSProperty BORDER_LEFT = new CSSProperty("border-left");

    public final static CSSProperty BORDER_LEFT_COLOR = new CSSProperty("border-left-color");

    public final static CSSProperty BORDER_LEFT_STYLE = new CSSProperty("border-left-style");

    public final static CSSProperty BORDER_LEFT_WIDTH = new CSSProperty("border-left-width");

    public final static CSSProperty BORDER_RIGHT = new CSSProperty("border-right");

    public final static CSSProperty BORDER_RIGHT_COLOR = new CSSProperty("border-right-color");

    public final static CSSProperty BORDER_RIGHT_STYLE = new CSSProperty("border-right-style");

    public final static CSSProperty BORDER_RIGHT_WIDTH = new CSSProperty("border-right-width");

    public final static CSSProperty BORDER_SPACING = new CSSProperty("border-spacing");

    public final static CSSProperty BORDER_STYLE = new CSSProperty("border-style");

    public final static CSSProperty BORDER_TOP = new CSSProperty("border-top");

    public final static CSSProperty BORDER_TOP_COLOR = new CSSProperty("border-top-color");

    public final static CSSProperty BORDER_TOP_STYLE = new CSSProperty("border-top-style");

    public final static CSSProperty BORDER_TOP_WIDTH = new CSSProperty("border-top-width");

    public final static CSSProperty BORDER_WIDTH = new CSSProperty("border-width");

    public final static CSSProperty BOTTOM = new CSSProperty("bottom");

    public final static CSSProperty CAPTION_SIDE = new CSSProperty("caption-side");

    public final static CSSProperty CLEAR = new CSSProperty("clear");

    public final static CSSProperty COLOR = new CSSProperty("color");

    /* This is just used for :before and :after pseudo elements. We don't have those
     * public final static CSSProperty CONTENT = new CSSProperty("content");
     */

    public final static CSSProperty COUNTER_INCREMENT = new CSSProperty("counter-increment");

    public final static CSSProperty COUNTER_RESET = new CSSProperty("counter-reset");

    public final static CSSProperty CURSOR = new CSSProperty("cursor");

    public final static CSSProperty DIRECTION = new CSSProperty("direction");

    public final static CSSProperty DISPLAY = new CSSProperty("display");

    public final static CSSProperty EMPTY_CELLS = new CSSProperty("empty-cells");

    public final static CSSProperty FLOAT = new CSSProperty("float");

    public final static CSSProperty FONT = new CSSProperty("font");

    public final static CSSProperty FONT_FAMILY = new CSSProperty("font-family");

    public final static CSSProperty FONT_SIZE = new CSSProperty("font-size");

    public final static CSSProperty FONT_SIZE_ADJUST = new CSSProperty("font-size-adjust");

    public final static CSSProperty FONT_STRETCH = new CSSProperty("font-stretch");

    public final static CSSProperty FONT_STYLE = new CSSProperty("font-style");

    public final static CSSProperty FONT_VARIANT = new CSSProperty("font-variant");

    public final static CSSProperty FONT_WEIGHT = new CSSProperty("font-weight");

    public final static CSSProperty HEIGHT = new CSSProperty("height");

    public final static CSSProperty LEFT = new CSSProperty("left");

    public final static CSSProperty LETTER_SPACING = new CSSProperty("letter-spacing");

    public final static CSSProperty LINE_HEIGHT = new CSSProperty("line-height");

    public final static CSSProperty LIST_STYLE = new CSSProperty("list-style");

    public final static CSSProperty LIST_STYLE_IMAGE = new CSSProperty("list-style-image");

    public final static CSSProperty LIST_STYLE_POSITION = new CSSProperty("list-style-position");

    public final static CSSProperty LIST_STYLE_TYPE = new CSSProperty("list-style-type");

    public final static CSSProperty MARGIN = new CSSProperty("margin");

    public final static CSSProperty MARGIN_TOP = new CSSProperty("margin-top");

    public final static CSSProperty MARGIN_BOTTOM = new CSSProperty("margin-bottom");

    public final static CSSProperty MARGIN_LEFT = new CSSProperty("margin-left");

    public final static CSSProperty MARGIN_RIGHT = new CSSProperty("margin-right");

    // only for paged output mediums, we don't need it at the moment
    //public final static CSSProperty MARKS = new CSSProperty("marks");

    public final static CSSProperty MAX_HEIGHT = new CSSProperty("max-height");

    public final static CSSProperty MAX_WIDTH = new CSSProperty("max-width");

    public final static CSSProperty MIN_HEIGHT = new CSSProperty("min-height");

    public final static CSSProperty MIN_WIDTH = new CSSProperty("min-width");

    // only for paged output mediums, we don't need it at the moment
    //public final static CSSProperty ORPHANS = new CSSProperty("orphans");

    public final static CSSProperty OUTLINE = new CSSProperty("outline");

    public final static CSSProperty OUTLINE_COLOR = new CSSProperty("outline-color");

    public final static CSSProperty OUTLINE_STYLE = new CSSProperty("outline-style");

    public final static CSSProperty OUTLINE_WIDTH = new CSSProperty("outline-width");

    public final static CSSProperty OVERFLOW = new CSSProperty("overflow");

    public final static CSSProperty PADDING = new CSSProperty("padding");

    public final static CSSProperty PADDING_BOTTOM = new CSSProperty("padding-bottom");

    public final static CSSProperty PADDING_LEFT = new CSSProperty("padding-left");

    public final static CSSProperty PADDING_RIGHT = new CSSProperty("padding-right");

    public final static CSSProperty PADDING_TOP = new CSSProperty("padding-top");

    // only for paged output mediums, we don't need it at the moment
    /*
     public final static CSSProperty PAGE = new CSSProperty("page");

     public final static CSSProperty PAGE_BREAK_AFTER = new CSSProperty("page-break-after");

     public final static CSSProperty PAGE_BREAK_BEFORE = new CSSProperty("page-break-before");

     public final static CSSProperty PAGE_BREAK_INSIDE = new CSSProperty("page-break-inside");
     */

    public final static CSSProperty POSITION = new CSSProperty("position");

    public final static CSSProperty QUOTES = new CSSProperty("quotes");

    public final static CSSProperty RIGHT = new CSSProperty("right");

    // only for paged output mediums, we don't need it at the moment
    //public final static CSSProperty SIZE = new CSSProperty("size");

    public final static CSSProperty TABLE_LAYOUT = new CSSProperty("table-layout");

    public final static CSSProperty TEXT_ALIGN = new CSSProperty("text-align");

    public final static CSSProperty TEXT_DECORATION = new CSSProperty("text-decoration");

    public final static CSSProperty TEXT_INDENT = new CSSProperty("text-indent");

    public final static CSSProperty TEXT_SHADOW = new CSSProperty("text-shadow");

    public final static CSSProperty TEXT_TRANSFORM = new CSSProperty("text-transform");

    public final static CSSProperty TOP = new CSSProperty("top");

    public final static CSSProperty UNICODE_BIDI = new CSSProperty("unicode-bidi");

    public final static CSSProperty VERTICAL_ALIGN = new CSSProperty("vertical-align");

    public final static CSSProperty VISIBILITY = new CSSProperty("visibility");

    public final static CSSProperty WHITE_SPACE = new CSSProperty("white-space");

    // only for paged output mediums, we don't need it at the moment
    //public final static CSSProperty WIDOWS = new CSSProperty("widows");

    public final static CSSProperty WIDTH = new CSSProperty("width");

    public final static CSSProperty WORD_SPACING = new CSSProperty("word-spacing");

    public final static CSSProperty Z_INDEX = new CSSProperty("z-index");

    /**
     * List of CSS properties which may not be applied to SComponents but their borders.
     */
    public static final Set<CSSProperty> BORDER_PROPERTIES = Collections.unmodifiableSet(new HashSet<CSSProperty>(Arrays.asList(
            CSSProperty.BORDER,
            //CSSProperty.BORDER_COLLAPSE,
            CSSProperty.BORDER_COLOR,
            //CSSProperty.BORDER_SPACING,
            CSSProperty.BORDER_STYLE, CSSProperty.BORDER_WIDTH, CSSProperty.BORDER_BOTTOM,
            CSSProperty.BORDER_BOTTOM_COLOR, CSSProperty.BORDER_BOTTOM_STYLE, CSSProperty.BORDER_BOTTOM_WIDTH,
            CSSProperty.BORDER_LEFT, CSSProperty.BORDER_LEFT_COLOR, CSSProperty.BORDER_LEFT_STYLE,
            CSSProperty.BORDER_LEFT_WIDTH, CSSProperty.BORDER_RIGHT, CSSProperty.BORDER_RIGHT_COLOR,
            CSSProperty.BORDER_RIGHT_STYLE, CSSProperty.BORDER_RIGHT_WIDTH, CSSProperty.BORDER_TOP,
            CSSProperty.BORDER_TOP_COLOR, CSSProperty.BORDER_TOP_STYLE, CSSProperty.BORDER_TOP_WIDTH,
            CSSProperty.PADDING, CSSProperty.PADDING_TOP, CSSProperty.PADDING_RIGHT,
            CSSProperty.PADDING_BOTTOM, CSSProperty.PADDING_LEFT)));

    private final String name;

    public CSSProperty(String cssAttributeName) {
        this.name = cssAttributeName;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CSSProperty))
            return false;
        final CSSProperty cssProperty = (CSSProperty) o;

        // CSS properties are CASE INSENSITIVE!
        return (name.equalsIgnoreCase(cssProperty.name));
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return getName();
    }

}
