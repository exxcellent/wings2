/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package desktop;

import org.wings.SInternalFrame;
import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.session.SessionManager;
import org.wings.event.SComponentDropListener;
import org.wings.event.SContainerEvent;
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
{
    TitleBar titleBar = new TitleBar(this);

    public Bird() {
        getComponentList().add(0, titleBar);
        getConstraintList().add(0, "");
        titleBar.setParent(this);
        getLayout().addComponent(titleBar, "", 0);

        titleBar.addComponentDropListener(new SComponentDropListener() {
            public boolean handleDrop(SComponent dragSource) {
                if (dragSource instanceof Bird.TitleBar) {
                    Bird.TitleBar titleBar = (Bird.TitleBar)dragSource;
                    dragSource = titleBar.getBird();
                    if (dragSource == Bird.this)
                        return false;
                    SContainer sourceParent = dragSource.getParent();
                    SContainer targetParent = Bird.this.getParent();
                    int index = -1;
                    SComponent[] components = targetParent.getComponents();
                    for (int i = 0; i < components.length; i++) {
                        SComponent component = components[i];
                        if (Bird.this == component)
                            index = i;
                    }
                    sourceParent.remove(dragSource);
                    targetParent.addComponent(dragSource, null, index);
                    return true;
                }
                return false;
            }
        });
    }

    public void updateCG() {
        setCG(new BirdCG());
    }

    public TitleBar getTitleBar() {
        return titleBar;
    }

    public static class TitleBar
        extends SComponent
        implements DragSource, DropTarget
    {
        private boolean dragEnabled;
        private Bird bird;

        private List componentDropListeners = new ArrayList();

        public void addComponentDropListener(SComponentDropListener listener) {
            componentDropListeners.add(listener);
            SessionManager.getSession().getDragAndDropManager().registerDropTarget(this);
        }

        public List getComponentDropListeners() {
            return componentDropListeners;
        }

        public TitleBar(Bird bird) {
            this.bird = bird;
            setDragEnabled(true);
        }

        public void updateCG() {
            setCG(new BirdTitleBarCG());
        }

        public Bird getBird() {
            return bird;
        }

        public boolean isDragEnabled() {
            return dragEnabled;
        }

        public void setDragEnabled(boolean dragEnabled) {
            this.dragEnabled = dragEnabled;
            if (dragEnabled) {
                SessionManager.getSession().getDragAndDropManager().registerDragSource(this);
            } else {
                SessionManager.getSession().getDragAndDropManager().deregisterDragSource(this);
            }
        }
    }
}
