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

/**
 * This interface contains several constants use at several places in wingS.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public interface SConstants {
    /**
     * Character to separate epoch id from component id in lowleve event requests.
     */
    String UID_DIVIDER = "_";

    /**
     * Component alignment constant: Do not explicitly align.
     */
    int NO_ALIGN = -1;
    /**
     * Component alignment constant: Align left.
     */
    int LEFT_ALIGN = 0;
    /**
     * Component alignment constant: Align left.
     */
    int LEFT = LEFT_ALIGN;
    /**
     * Component alignment constant: Align right.
     */
    int RIGHT_ALIGN = 1;
    /**
     * Component alignment constant: Align right.
     */
    int RIGHT = RIGHT_ALIGN;
    /**
     * Component alignment constant: Center align.
     */
    int CENTER_ALIGN = 2;
    /**
     * Component alignment constant: Center align.
     */
    int CENTER = CENTER_ALIGN;
    /**
     * Component alignment constant: Block align -- stretch over full width.
     */
    int BLOCK_ALIGN = 3;
    /**
     * Component alignment constant: Vertically align at top.
     */
    int TOP_ALIGN = 4;
    /**
     * Component alignment constant: Vertically align at top.
     */
    int TOP = TOP_ALIGN;
    /**
     * Component alignment constant: Vertically align at bottom.
     */
    int BOTTOM_ALIGN = 5;
    /**
     * Component alignment constant: Vertically align at bottom.
     */
    int BOTTOM = BOTTOM_ALIGN;
    /**
     * Component alignment constant: Block align -- stretch over full width.
     */
    int JUSTIFY = BLOCK_ALIGN;
    /**
     * Align at font baseline. (Images).
     */
    int BASELINE = 6;

    /**
     * List type for for {@link SList}
     */
    String ORDERED_LIST = "ol";
    /**
     * List type for for {@link SList}
     */
    String UNORDERED_LIST = "ul";
    /**
     * List type for for {@link SList}
     */
    String MENU_LIST = "menu";
    /**
     * List type for for {@link SList}
     */
    String DIR_LIST = "dir";

    /**
     * Order type for for {@link SList}
     */
    String[] ORDER_TYPE_CIRCLE = {"ul", "circle"};
    /**
     * Order type for for {@link SList}
     */
    String[] ORDER_TYPE_SQUARE = {"ul", "square"};
    /**
     * Order type for for {@link SList}
     */
    String[] ORDER_TYPE_DISC = {"ul", "disc"};
    /**
     * Order type for for {@link SList}
     */
    String[] ORDER_TYPE_BIG_ALPHA = {"ol", "A"};
    /**
     * Order type for for {@link SList}
     */
    String[] ORDER_TYPE_SMALL_ALPHA = {"ol", "a"};
    /**
     * Order type for for {@link SList}
     */
    String[] ORDER_TYPE_NUMBER = {"ol", null};
    /**
     * Order type for for {@link SList}
     */
    String[] ORDER_TYPE_NORMAL = {"ul", null};
    /**
     * Order type for for {@link SList}
     */
    String[] ORDER_TYPE_BIG_ROMAN = {"ol", "I"};
    /**
     * Order type for for {@link SList}
     */
    String[] ORDER_TYPE_SMALL_ROMAN = {"ol", "i"};

    // Textarea related
    /**
     * Text wrapping behaviour for {@link STextArea}: Don't wrap.
     */
    int NO_WRAP = 0;
    /**
     * Text wrapping behaviour for {@link STextArea}: Wrap at width.
     */
    int VIRTUAL_WRAP = 1;
    /**
     * Text wrapping behaviour for {@link STextArea}: Wrap at physical input box borders.
     */
    int PHYSICAL_WRAP = 2;

    /* Obsolete?
    int CLEAR_NO = 0;
    int CLEAR_LEFT = 1;
    int CLEAR_RIGHT = 2;
    int CLEAR_ALL = 3;*/

    /**
     * Table selection model. See {@link SListSelectionModel}
     */
    int NO_SELECTION = SListSelectionModel.NO_SELECTION;
    /**
     * Table selection model. See {@link SListSelectionModel}
     */
    int SINGLE_SELECTION = SListSelectionModel.SINGLE_SELECTION;
    /**
     * Table selection model. See {@link SListSelectionModel}
     */
    int SINGLE_INTERVAL_SELECTION = SListSelectionModel.SINGLE_INTERVAL_SELECTION;
    /**
     * Table selection model. See {@link SListSelectionModel}
     */
    int MULTIPLE_SELECTION = SListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
    /**
     * Table selection model. See {@link SListSelectionModel}
     */
    int MULTIPLE_INTERVAL_SELECTION = SListSelectionModel.MULTIPLE_INTERVAL_SELECTION;

    int VERTICAL = 1;
    int HORIZONTAL = 0;

    /**
     * Default Form encoding type. {@link SForm}
     */
    String ENC_TYPE_TEXT_PLAIN = "text/plain";
    /**
     * Multipart form encoding. Needed for file uploads. {@link SForm}
     */
    String ENC_TYPE_MULTIPART_FORM = "multipart/form-data";

    // Obsolete FrameSet support
    /*int VERTICAL_SPLIT = VERTICAL;
    int HORIZONTAL_SPLIT = HORIZONTAL;
    String JAVASCRIPT_LOAD_TWO_FRAMES =
            "  function loadTwoFrames(F1, URL1, F2, URL2) {\n" +
            "    F1.location.href=URL1;\n" +
            "    F2.location.href=URL2;\n" +
            "  }";
     */

    //int FONT = 1;
    //int BASEFONT = 2;

    /**
     * Plain font style for {@link SFont} constructor.
     */
    int PLAIN = java.awt.Font.PLAIN;
    /**
     * Italic font style for {@link SFont} constructor.
     */
    int ITALIC = java.awt.Font.ITALIC;
    /**
     * Bold font style for {@link SFont} constructor.
     */
    int BOLD = java.awt.Font.BOLD;

    /**
     * Default font size for {@link SFont} constructor.
     */
    int DEFAULT_SIZE = -1;
}


