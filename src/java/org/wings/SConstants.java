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
package org.wings;


// FIXME10 remove unneeded stuff.

/**
 * This interface contains several constants we use at several places.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public interface SConstants {
    String UID_DIVIDER = "_";

    int NO_ALIGN = -1;
    int LEFT_ALIGN = 0;
    int LEFT = LEFT_ALIGN;
    int RIGHT_ALIGN = 1;
    int RIGHT = RIGHT_ALIGN;
    int CENTER_ALIGN = 2;
    int CENTER = CENTER_ALIGN;
    int BLOCK_ALIGN = 3;
    int TOP_ALIGN = 4;
    int TOP = TOP_ALIGN;
    int BOTTOM_ALIGN = 5;
    int BOTTOM = BOTTOM_ALIGN;
    int JUSTIFY = BLOCK_ALIGN;
    int BASELINE = 6;

    String ORDERED_LIST = "ol";
    String UNORDERED_LIST = "ul";
    String MENU_LIST = "menu";
    String DIR_LIST = "dir";

    String[] ORDER_TYPE_CIRCLE = {"ul", "circle"};
    String[] ORDER_TYPE_SQUARE = {"ul", "square"};
    String[] ORDER_TYPE_DISC = {"ul", "disc"};
    String[] ORDER_TYPE_BIG_ALPHA = {"ol", "A"};
    String[] ORDER_TYPE_SMALL_ALPHA = {"ol", "a"};
    String[] ORDER_TYPE_NUMBER = {"ol", null};
    String[] ORDER_TYPE_NORMAL = {"ul", null};
    String[] ORDER_TYPE_BIG_ROMAN = {"ol", "I"};
    String[] ORDER_TYPE_SMALL_ROMAN = {"ol", "i"};

    // Textarea related
    int NO_WRAP = 0;
    int VIRTUAL_WRAP = 1;
    int PHYSICAL_WRAP = 2;


    int CLEAR_NO = 0;
    int CLEAR_LEFT = 1;
    int CLEAR_RIGHT = 2;
    int CLEAR_ALL = 3;

    int NO_SELECTION = SListSelectionModel.NO_SELECTION;
    int SINGLE_SELECTION = SListSelectionModel.SINGLE_SELECTION;
    int SINGLE_INTERVAL_SELECTION = SListSelectionModel.SINGLE_INTERVAL_SELECTION;
    int MULTIPLE_SELECTION = SListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
    int MULTIPLE_INTERVAL_SELECTION = SListSelectionModel.MULTIPLE_INTERVAL_SELECTION;

    int VERTICAL = 1;
    int HORIZONTAL = 0;

    int VERTICAL_SPLIT = VERTICAL;
    int HORIZONTAL_SPLIT = HORIZONTAL;

    String ENC_TYPE_TEXT_PLAIN = "text/plain";
    String ENC_TYPE_MULTIPART_FORM = "multipart/form-data";

    String JAVASCRIPT_LOAD_TWO_FRAMES =
            "  function loadTwoFrames(F1, URL1, F2, URL2) {\n" +
            "    F1.location.href=URL1;\n" +
            "    F2.location.href=URL2;\n" +
            "  }";

    int FONT = 1;
    int BASEFONT = 2;

    int PLAIN = java.awt.Font.PLAIN;
    int ITALIC = java.awt.Font.ITALIC;
    int BOLD = java.awt.Font.BOLD;
}


