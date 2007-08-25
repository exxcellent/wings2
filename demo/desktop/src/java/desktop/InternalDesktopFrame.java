package desktop;

import org.wings.*;
import org.wings.dnd.DragSource;
import org.wings.dnd.DropTarget;
import org.wings.event.*;
import org.wings.session.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class InternalDesktopFrame
    extends SInternalFrame
    implements SInternalFrameListener, DragSource, DropTarget, ItemContainer
{

    private ArrayList<SComponentDropListener> componentDropListeners = new ArrayList<SComponentDropListener>();
    private boolean dragEnabled = true;
    private DesktopItem item;
    private Preferences pref;

    private static org.wings.util.SessionLocal<Integer> frameNo = new org.wings.util.SessionLocal<Integer>()
    {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };


    public InternalDesktopFrame() {
        super();
        pref = Preferences.userRoot().node("desktopitems");

        this.setName("frame" + frameNo.get().toString());
        frameNo.set(frameNo.get() + 1);
        addInternalFrameListener(this);

        this.setDragEnabled(true);
        componentDropListeners = new ArrayList<SComponentDropListener>();

        addComponentDropListener(new SComponentDropListener()
        {
            public boolean handleDrop(SComponent dragSource) {

                SContainer sourceContainer = dragSource.getParent();

                //different panes
                if (InternalDesktopFrame.this.getParent() != sourceContainer) {
                    if (sourceContainer instanceof DesktopPane) {

                        //EditorPanel.this.setMaximized(false);
                        if (dragSource instanceof SInternalFrame)
                            ((SInternalFrame)dragSource).setMaximized(false);


                        DesktopPane targetPane = (DesktopPane)InternalDesktopFrame.this.getParent();
                        DesktopPane sourcePane = (DesktopPane)sourceContainer;
                        int sIndex = sourcePane.getIndexOf(dragSource);
                        int tIndex = targetPane.getIndexOf(InternalDesktopFrame.this);

                        DesktopItem dtItem = ((InternalDesktopFrame)dragSource).getItem();

                        sourcePane.remove(sIndex);
                        targetPane.add(dragSource, tIndex);
                        pref.node((String)dtItem.getValue(DesktopItem.KEY)).put(DesktopItem.DESKTOPPANE, targetPane.getName());
                        for (int i = sIndex; i < sourcePane.getComponentCount() - 1; i++) {
                            DesktopItem it = ((InternalDesktopFrame)sourcePane.getComponent(i)).getItem();
                            pref.node((String)it.getValue(DesktopItem.KEY)).putInt(DesktopItem.POSITION_ON_PANE, i);
                        }

                        for (int i = tIndex; i < targetPane.getComponentCount() - 1; i++) {
                            DesktopItem it = ((InternalDesktopFrame)targetPane.getComponent(i)).getItem();
                            pref.node((String)it.getValue(DesktopItem.KEY)).putInt(DesktopItem.POSITION_ON_PANE, i);
                        }

                        return true;
                    }
                }
                //same pane
                else if (sourceContainer instanceof DesktopPane) {
                    DesktopPane pane = (DesktopPane)sourceContainer;
                    int tindex = pane.getIndexOf(InternalDesktopFrame.this);
                    int sindex = pane.getIndexOf(dragSource);

                    //do nothing if drag on itself..
                    if (sindex == tindex)
                        return false;

                    //EditorPanel.this.setMaximized(false);
                    if (dragSource instanceof SInternalFrame)
                        ((SInternalFrame)dragSource).setMaximized(false);

                    DesktopItem dtItem = ((InternalDesktopFrame)dragSource).getItem();

                    pane.remove(sindex);

                    if (tindex > sindex)
                        pane.add(dragSource, tindex - 1);
                    else
                        pane.add(dragSource, tindex);

                    for (int i = java.lang.Math.min(sindex, tindex); i < pane.getComponentCount() - 1; i++) {
                        DesktopItem it = ((InternalDesktopFrame)pane.getComponent(i)).getItem();
                        pref.node((String)it.getValue(DesktopItem.KEY)).putInt(DesktopItem.POSITION_ON_PANE, i);
                    }

                    return true;
                }
                return false;
            }
        });
    }

    public void destroy() {
        item.destroyed();
        dispose();

    }


    public void setIcon(SURLIcon icon) {
        super.setIcon(icon);

    }

    public void setTitle(String title) {
        super.setTitle(title);
    }


    public DesktopItem getItem() {
        return item;
    }


    public void setItem(DesktopItem item) {
        this.item = item;
        getContentPane().addComponent(item.getComponent());
        setTitle((String)item.getValue(DesktopItem.NAME));
        setIcon((SURLIcon)item.getValue(DesktopItem.ICON));
        item.activated();
    }

    public void addComponentDropListener(SComponentDropListener listener) {
        componentDropListeners.add(listener);
        SessionManager.getSession().getDragAndDropManager().registerDropTarget(this);
    }

    public List<SComponentDropListener> getComponentDropListeners() {
        return this.componentDropListeners;
    }

    public boolean isDragEnabled() {
        return this.dragEnabled;
    }

    public void setDragEnabled(boolean dragEnabled) {
        this.dragEnabled = dragEnabled;
        if (dragEnabled) {
            SessionManager.getSession().getDragAndDropManager().registerDragSource(this);
        }
        else {
            SessionManager.getSession().getDragAndDropManager().deregisterDragSource(this);
        }
    }

    public void internalFrameClosed(SInternalFrameEvent e) {
        destroy();
    }

    public void internalFrameOpened(SInternalFrameEvent e) {
    }

    public void internalFrameIconified(SInternalFrameEvent e) {
    }

    public void internalFrameDeiconified(SInternalFrameEvent e) {
    }

    public void internalFrameMaximized(SInternalFrameEvent e) {
    }

    public void internalFrameUnmaximized(SInternalFrameEvent e) {
    }


}
