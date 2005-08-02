/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package desktop;

import org.wings.SInternalFrame;
import org.wings.session.SessionManager;
import org.wings.event.SComponentDropListener;
import org.wings.dnd.DragSource;
import org.wings.dnd.DropTarget;

import java.util.List;
import java.util.ArrayList;

/**
 * @author hengels
 * @version $Revision$
 */
public class Bird
        extends SInternalFrame
        implements DragSource, DropTarget
{
    private boolean dragEnabled;
    private List componentDropListeners = new ArrayList();

    public Bird() {
        setDragEnabled(true);
    }

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

    public void addComponentDropListener(SComponentDropListener listener) {
        componentDropListeners.add(listener);
        SessionManager.getSession().getDragAndDropManager().registerDropTarget(this);
    }

    public List getComponentDropListeners() {
        return componentDropListeners;
    }
}
