/*
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
package org.wings.dnd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.LowLevelEventListener;
import org.wings.SComponent;
import org.wings.SForm;
import org.wings.event.SComponentDropListener;

/**
 * The Drag and Drop Manager. It receives DnD events and dispatches them to the
 * registered components. You must register your component with this class,
 * which is accessible via the wings session. you should do this in the
 * @link org.wings.dnd.DragSource#setDragEnabled or
 * @link org.wings.dnd.DropTarget#addComponentDropListener methods.
 * @author ole
 *
 */
public class DragAndDropManager extends SComponent implements LowLevelEventListener {
    private final static Log log = LogFactory.getLog(DragAndDropManager.class);
    private ArrayList dragSources;
    private ArrayList dropTargets;
    private HashMap namesToComponentsMap;
    private Object sourceName;
    private Object targetName;

    /**
     * The constructor. It initializes all fields
     */
    public DragAndDropManager() {
        super();
        dragSources = new ArrayList();
        dropTargets = new ArrayList();
        namesToComponentsMap = new HashMap();
        setParentFrame(getSession().getRootFrame());
        getSession().getDispatcher().register(this);
    }

    /**
     * register a dragSource with the Manager. After registering it will be
     * receivable by dropTargets.
     * @param dragSource the SComponent which is the DragSource
     */
    public void registerDragSource(DragSource dragSource) {
        // don't add a component more than once
        if (!dragSources.contains(dragSource)) {
            dragSources.add(dragSource);
            namesToComponentsMap.put(((SComponent)dragSource).getName(), dragSource);
            getParentFrame().reload();
        }
    }

    /**
     * deregister a dragSource.
     * @param dragSource the SComponent which is the DragSource to deregister
     */
    public void deregisterDragSource(DragSource dragSource) {
        dragSources.remove(dragSource);
        namesToComponentsMap.remove(((SComponent)dragSource).getName());
        getParentFrame().reload();
    }

    /**
     * register a dropTarget with the Manager. After registering it can receive
     * drop events.
     * @param dropTarget the SComponent which is the DropTarget
     */
    public void registerDropTarget(DropTarget dropTarget) {
        // don't add a component more than once
        if (!dropTargets.contains(dropTarget)) {
            dropTargets.add(dropTarget);
            namesToComponentsMap.put(((SComponent)dropTarget).getName(), dropTarget);
            getParentFrame().reload();
        }
    }

    /**
     * deregister a dropTarget.
     * @param dropTarget the SComponent which is the DropTarget to deregister
     */
    public void deregisterDropTarget(DropTarget dropTarget) {
        dropTargets.remove(dropTarget);
        namesToComponentsMap.remove(((SComponent)dropTarget).getName());
        getParentFrame().reload();
    }

    public SComponent getComponentByName(String name) {
        return (SComponent)namesToComponentsMap.get(name);
    }

    /**
     * getter for the list of drag sources. Used for initializing them in the
     * client code.
     * @return a List of all drag sources
     */
    public List getDragSources() {
        return dragSources;
    }

    /**
     * getter for the list of drop targets. Used for initializing them in the
     * client code.
     * @return a List of all drop targets
     */
    public List getDropTargets() {
        return dropTargets;
    }

    /* (non-Javadoc)
     * @see org.wings.SComponent#processLowLevelEvent(java.lang.String, java.lang.String[])
     */
    public void processLowLevelEvent(String name, String[] values) {
        log.debug("processLowLevelEvent processing");
        // handle the somehow coded drag to drop connection, look for the components and dispatch
        sourceName = null;
        targetName = null;
        for (int i = 0; i < values.length; i++) {
            int sourceIndex = values[i].indexOf("dragSource");
            if (sourceIndex != -1) {
                /* extract name from request value. remove string "dragSource"
                 * from the beginning.
                 */
                sourceName = values[i].substring(sourceIndex + 10,values[i].length());
            }
            int targetIndex = values[i].indexOf("dropTarget");
            if (targetIndex != -1) {
                // see above
                targetName = values[i].substring(targetIndex + 10,values[i].length());
            }
        }
        log.debug("sourcename: " + sourceName);
        log.debug("targetname: " + targetName);
        SForm.addArmedComponent(this);
    }

    /* (non-Javadoc)
     * @see org.wings.LowLevelEventListener#fireIntermediateEvents()
     */
    public void fireIntermediateEvents() {
    }

    /* (non-Javadoc)
     * @see org.wings.SComponent#fireFinalEvents()
     */
    public void fireFinalEvents() {
        log.debug("fireFinalEvents processing");
        if (sourceName != null && targetName != null) {
            log.debug("fireFinalEvents processing");
            DropTarget target = (DropTarget)namesToComponentsMap.get(targetName);
            DragSource source = (DragSource)namesToComponentsMap.get(sourceName);
            if (target != null && source != null) {
                Iterator listIter = target.getComponentDropListeners().iterator();
                SComponentDropListener listener;
                while (listIter.hasNext()) {
                    log.debug("fireFinalEvents listener");
                    listener = (SComponentDropListener)listIter.next();
                    // TODO: evaluate return value of handleDrop and visualize it in the client
                    listener.handleDrop((SComponent)source);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see org.wings.SComponent#isEnabled()
     */
    public boolean isEnabled() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.wings.LowLevelEventListener#isEpochCheckEnabled()
     */
    public boolean isEpochCheckEnabled() {
        return true;
    }

    public boolean isVisible() {
        final Iterator dragIter = dragSources.iterator();
        final Iterator dropIter = dropTargets.iterator();
        boolean result = false;
        while (dragIter.hasNext()) {
            if (((SComponent)dragIter.next()).isRecursivelyVisible()) {
                result = true;
                break;
            }
        }
        if (result) return true;
        while (dropIter.hasNext()) {
            if (((SComponent)dropIter.next()).isRecursivelyVisible()) {
                result = true;
                break;
            }
        }
        return result;
    }
}
