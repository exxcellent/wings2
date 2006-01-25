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
package wingset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.SBoxLayout;
import org.wings.SButton;
import org.wings.SComponent;
import org.wings.SDimension;
import org.wings.SForm;
import org.wings.SGridLayout;
import org.wings.SIcon;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.SURLIcon;
import org.wings.border.SEmptyBorder;
import org.wings.dnd.DragSource;
import org.wings.dnd.DropTarget;
import org.wings.event.SComponentDropListener;
import org.wings.session.SessionManager;
import org.wings.style.CSSProperty;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * example for showing the drag and drop capabilities of wingS.
 * And "it's got wingS ;)" 
 * @author ole
 *
 */
public class DragAndDropExample extends WingSetPane {
    private final static Log log = LogFactory.getLog(DragAndDropExample.class);

    private static final SURLIcon ICON_BEE1 = new SURLIcon("../icons/bee_1.jpg");
    private static final SURLIcon ICON_BEE2 = new SURLIcon("../icons/bee_2.jpg");
    private static final SURLIcon ICON_BEE3 = new SURLIcon("../icons/bee_3.jpg");
    private static final SURLIcon ICON_BEE4 = new SURLIcon("../icons/bee_4.jpg");
    private static final SURLIcon ICON_BEE5 = new SURLIcon("../icons/bee_5.jpg");
    private static final SURLIcon ICON_BEE6 = new SURLIcon("../icons/bee_6.jpg");
    private static final SURLIcon ICON_BEE7 = new SURLIcon("../icons/bee_7.jpg");
    private static final SURLIcon ICON_BEE8 = new SURLIcon("../icons/bee_8.jpg");
    private static final SURLIcon ICON_BEE9 = new SURLIcon("../icons/bee_9.jpg");

    private final SDragLabel dragIconOne = new SDragLabel();
    private final SDragLabel dragIconTwo = new SDragLabel();
    private final SDragLabel dragIconThree = new SDragLabel();
    private final SDragLabel dragIconFour = new SDragLabel();
    private final SDragLabel dragIconFive = new SDragLabel();
    private final SDragLabel dragIconSix = new SDragLabel();
    private final SDragLabel dragIconSeven = new SDragLabel();
    private final SDragLabel dragIconEight = new SDragLabel();
    private final SDragLabel dragIconNine = new SDragLabel();

    private final SDropLabel dropIconOne = new SDropLabel();
    private final SDropLabel dropIconTwo = new SDropLabel();
    private final SDropLabel dropIconThree = new SDropLabel();
    private final SDropLabel dropIconFour = new SDropLabel();
    private final SDropLabel dropIconFive = new SDropLabel();
    private final SDropLabel dropIconSix = new SDropLabel();
    private final SDropLabel dropIconSeven = new SDropLabel();
    private final SDropLabel dropIconEight = new SDropLabel();
    private final SDropLabel dropIconNine = new SDropLabel();

    private final SIcon[] beeIcons = new SIcon[] {ICON_BEE1, ICON_BEE2, ICON_BEE3, ICON_BEE4, ICON_BEE5, ICON_BEE6, ICON_BEE7, ICON_BEE8, ICON_BEE9};
    private final SDragLabel[] dragIcons = new SDragLabel[] {dragIconOne, dragIconTwo, dragIconThree, dragIconFour, dragIconFive, dragIconSix, dragIconSeven, dragIconEight, dragIconNine};
    private final SDropLabel[] dropIcons = new SDropLabel[] {dropIconOne, dropIconTwo, dropIconThree, dropIconFour, dropIconFive, dropIconSix, dropIconSeven, dropIconEight, dropIconNine};

    private int[] shuffleTable = new int[] {0,1,2,3,4,5,6,7,8};

    private int piecesRight;
    private final SLabel statusLabel = new SLabel();

    protected SComponent createExample() {
        final SForm container = new SForm();
        final SPanel puzzleContainer = new SPanel(new SBoxLayout(SBoxLayout.VERTICAL));
        final SPanel controlContainer = new SPanel(new SBoxLayout(SBoxLayout.VERTICAL));
        container.setLayout(new SBoxLayout(SBoxLayout.HORIZONTAL));
        controlContainer.setPreferredSize(new SDimension("150", null));
        controlContainer.setBorder(new SEmptyBorder(0, 20,0,0));

        // control components
        final SButton resetButton = new SButton("Reset");
        resetButton.addActionListener(new wingset.SerializableActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetPuzzle();
            }
        });

        // initialize the drag components
        for (int i = 0; i < dragIcons.length; i++) {
            dragIcons[i].setDragEnabled(true);
            dragIcons[i].setPreferredSize(new SDimension(30,30));
        }

        // init the shuffle transformation table
        Random random = new Random();
        for (int i = 0; i < shuffleTable.length; i++) {
            int swapWith = random.nextInt(shuffleTable.length);
            int oldVal = shuffleTable[i];
            shuffleTable[i] = shuffleTable[swapWith];
            shuffleTable[swapWith] = oldVal;
        }

        // initialize the drop components
        for (int i = 0; i < dropIcons.length; i++) {
            final int position = i;
            final SDropLabel dropIcon = dropIcons[i];
            dropIcon.setPreferredSize(new SDimension(100,100));
            dropIcon.addComponentDropListener(new SComponentDropListener() {

                // the drag and drop magic
                public boolean handleDrop(SComponent dragSource) {
                    if (dragIcons[shuffleTable[position]].equals(dragSource)) {
                        dragIcons[shuffleTable[position]].setIcon(null);
                        beeIcons[position].setIconHeight(100);
                        beeIcons[position].setIconWidth(100);
                        dropIcon.setIcon(beeIcons[position]);
                        piecesRight++;
                        if (piecesRight == 9) {
                            statusLabel.setText("Look, it's got wingS ;) !");
                            statusLabel.setAttribute(CSSProperty.FONT_WEIGHT, "600");
                            statusLabel.setAttribute(CSSProperty.COLOR, "red");
                        } else {
                            statusLabel.setText("You have " + piecesRight + " pieces right!\nCan you guess what it is?");
                        }
                        return true;
                    }
                    statusLabel.setText("That piece doesn't belong there!\nWhat are you thinking?");
                    return false;
                }
                
            });
        }
        resetPuzzle();
        
        // build the puzzle
        final SPanel puzzle = new SPanel();
        final SGridLayout gridLayout = new SGridLayout(3,3);
        gridLayout.setBorder(1);
        puzzle.setLayout(gridLayout);
        for (int i = 0; i < dropIcons.length; i++) {
            puzzle.add(dropIcons[i]);
        }
        
        // build the pieces area
        final SPanel pieces = new SPanel();
        final SBoxLayout boxLayout = new SBoxLayout(SBoxLayout.HORIZONTAL);
        boxLayout.setBorder(1);
        pieces.setLayout(boxLayout);
        for (int i = 0; i < dragIcons.length; i++) {
            pieces.add(dragIcons[i]);
        }

        puzzleContainer.add(puzzle);
        puzzleContainer.add(pieces);
        
        controlContainer.add(statusLabel);
        controlContainer.add(resetButton);
        
        container.add(puzzleContainer);
        container.add(controlContainer);
        
        return container;
    }

    protected void resetPuzzle() {
        piecesRight = 0;
        statusLabel.setText("Try to solve the puzzle.");
        statusLabel.setAttribute(CSSProperty.FONT_WEIGHT, "normal");
        statusLabel.setAttribute(CSSProperty.COLOR, "black");

        // init the icons
        for (int i = 0; i < beeIcons.length; i++) {
            beeIcons[i].setIconHeight(30);
            beeIcons[i].setIconWidth(30);
        }
        // initialize the drag components
        for (int i = 0; i < dragIcons.length; i++) {
            SDragLabel dragIcon = dragIcons[shuffleTable[i]];
            dragIcon.setIcon(beeIcons[i]);
        }
        // initialize the drop components
        for (int i = 0; i < dropIcons.length; i++) {
            dropIcons[i].setIcon(null);
        }
    }

    /**
     * This class extends the SLabel class with Drag functionality.
     * @author ole
     *
     */
    private static class SDragLabel extends SLabel implements DragSource {

        private boolean dragEnabled;

        public boolean isDragEnabled() {
            return dragEnabled;
        }

        public void setDragEnabled(boolean dragEnabled) {
            this.dragEnabled = dragEnabled;
            if (dragEnabled) {
                SessionManager.getSession().getDragAndDropManager().registerDragSource((DragSource)this);
            } else {
                SessionManager.getSession().getDragAndDropManager().deregisterDragSource((DragSource)this);
            }
        }
        
    }
    
    
    /**
     * This class extends the SLabel class with Drop functionality.
     * @author ole
     *
     */
    private static class SDropLabel extends SLabel implements DropTarget {
        private ArrayList componentDropListeners = new ArrayList();

        /* (non-Javadoc)
         * @see org.wings.dnd.DropTarget#addComponentDropListener(org.wings.event.SComponentDropListener)
         */
        public void addComponentDropListener(SComponentDropListener listener) {
            componentDropListeners.add(listener);
            SessionManager.getSession().getDragAndDropManager().registerDropTarget(this);
        }

        /* (non-Javadoc)
         * @see org.wings.dnd.DropTarget#getComponentDropListeners()
         */
        public List getComponentDropListeners() {
            return componentDropListeners;
        }        
    }
}
