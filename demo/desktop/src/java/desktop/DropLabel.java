package desktop;

import org.wings.*;
import org.wings.dnd.DropTarget;
import org.wings.event.SComponentDropListener;
import org.wings.session.SessionManager;
import org.wings.style.CSSProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class DropLabel
    extends SLabel
    implements DropTarget
{

    private ArrayList<SComponentDropListener> componentDropListeners = new ArrayList<SComponentDropListener>();
    private Preferences pref;

    private static org.wings.util.SessionLocal<Integer> labelNo = new org.wings.util.SessionLocal<Integer>()
    {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public DropLabel() {
        super();
        this.setName("droplabel" + labelNo.get().toString());
        labelNo.set(labelNo.get() + 1);

        pref = Preferences.userRoot();

        this.setVerticalAlignment(SConstants.TOP_ALIGN);
        this.setAttribute(CSSProperty.HEIGHT, "200px");
        this.setAttribute(CSSProperty.WIDTH, "100%");
        this.setEnabled(true);
        this.setVisible(true);

        addComponentDropListener(new SComponentDropListener()
        {
            public boolean handleDrop(SComponent dragSource) {
                DesktopPane sourcePane = (DesktopPane)dragSource.getParent();
                DesktopPane targetPane = (DesktopPane)DropLabel.this.getParent();
                int sourceIndex = sourcePane.getIndexOf(dragSource);

                if (sourcePane == targetPane && sourcePane.getIndexOf(dragSource) == sourcePane.getComponentCount() - 2)
                    return false;

                DesktopItem item = ((InternalDesktopFrame)dragSource).getItem();

                sourcePane.remove(dragSource);
                int inputPos = targetPane.getComponentCount() - 1;
                targetPane.add(dragSource, inputPos);

                for (int i = sourceIndex; i < sourcePane.getComponentCount() - 1; i++) {
                    DesktopItem it = ((InternalDesktopFrame)sourcePane.getComponent(i)).getItem();
                    pref.node("desktopitems").node((String)it.getValue(DesktopItem.KEY)).putInt(DesktopItem.POSITION_ON_PANE, i);
                }

                pref.node("desktopitems").node((String)item.getValue(DesktopItem.KEY)).put(DesktopItem.DESKTOPPANE, targetPane.getName());
                pref.node("desktopitems").node((String)item.getValue(DesktopItem.KEY)).putInt(DesktopItem.POSITION_ON_PANE, inputPos);
                return true;
            }
        });

    }

    public void addComponentDropListener(SComponentDropListener listener) {
        componentDropListeners.add(listener);
        SessionManager.getSession().getDragAndDropManager().registerDropTarget(this);
    }

    public List<SComponentDropListener> getComponentDropListeners() {
        return componentDropListeners;
    }

}
