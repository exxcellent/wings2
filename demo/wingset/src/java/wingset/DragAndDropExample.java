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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.SComponent;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.STextField;
import org.wings.dnd.DragAndDropManager;
import org.wings.dnd.DragSource;
import org.wings.dnd.DropTarget;
import org.wings.event.SComponentDropListener;
import org.wings.session.SessionManager;

/**
 * example for showing the drag and drop capabilities of wingS
 * @author ole
 *
 */
public class DragAndDropExample extends WingSetPane {
    private final static Log log = LogFactory.getLog(DragAndDropExample.class);
    private final SDndTextField dragSource = new SDndTextField("This is the DragSource.");
    private final SDndTextField dropTarget = new SDndTextField("This is the DropTarget.");
    private final SDndTextField normalField = new SDndTextField("This is a normal Label.");
    private final SPanel container = new SPanel();
    
    protected SComponent createExample() {
        dragSource.setDragEnabled(true);
        dropTarget.addComponentDropListener(new SComponentDropListener() {

            public boolean handleDrop(SComponent dragSource) {
                if (dragSource instanceof STextField) {
                    // allow drop
                    dropTarget.setText(((STextField)dragSource).getText());
                }
                container.add(new SLabel("Drag and Drop Action triggered"));
                
                return true;
            }
            
        });
        container.add(dragSource);
        container.add(normalField);
        container.add(dropTarget);
        return container;
    }

    /**
     * This class extends the STextField class with Drag and Drop functionality.
     * @author ole
     *
     */
    private class SDndTextField extends STextField implements DragSource, DropTarget {
        private boolean dragEnabled;
        private ArrayList componentDropListeners = new ArrayList();

        public SDndTextField() {
            super();
        }

        public SDndTextField(String string) {
            super(string);
        }

        /* (non-Javadoc)
         * @see org.wings.dnd.DragSource#isDragEnabled()
         */
        public boolean isDragEnabled() {
            return dragEnabled;
        }

        /* (non-Javadoc)
         * @see org.wings.dnd.DragSource#setDragEnabled(boolean)
         */
        public void setDragEnabled(boolean dragEnabled) {
            this.dragEnabled = dragEnabled;
            if (dragEnabled) {
                SessionManager.getSession().getDragAndDropManager().registerDragSource((DragSource)this);
            } else {
                SessionManager.getSession().getDragAndDropManager().deregisterDragSource((DragSource)this);
            }
        }

        /* (non-Javadoc)
         * @see org.wings.dnd.DropTarget#addComponentDropListener(org.wings.event.SComponentDropListener)
         */
        public void addComponentDropListener(SComponentDropListener listener) {
            componentDropListeners.add(listener);
            SessionManager.getSession().getDragAndDropManager().registerDropTarget((DropTarget)this);
        }

        /* (non-Javadoc)
         * @see org.wings.dnd.DropTarget#getComponentDropListeners()
         */
        public List getComponentDropListeners() {
            return componentDropListeners;
        }        
    }
}
