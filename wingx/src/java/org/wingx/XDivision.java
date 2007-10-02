package org.wingx;

import org.wings.*;

/**
 * Created by IntelliJ IDEA.
 * User: hengels
 * Date: Aug 27, 2006
 * Time: 9:58:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class XDivision
    extends SContainer
    implements LowLevelEventListener
{
    String title;
    SIcon icon;
    boolean shaded;

    public XDivision(SLayoutManager l) {
        super(l);
    }

    public XDivision() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        reloadIfChange(this.title, title);
        this.title = title;
    }

    public SIcon getIcon() {
        return icon;
    }

    public void setIcon(SIcon icon) {
        reloadIfChange(this.icon, icon);
        this.icon = icon;
    }

    public boolean isShaded() {
        return shaded;
    }

    public void setShaded(boolean shaded) {
        reloadIfChange(this.shaded, shaded);
        this.shaded = shaded;
    }

    public void processLowLevelEvent(String name, String[] values) {
        if (values.length == 1 && "t".equals(values[0])) {
            shaded = !shaded;
            reload(ReloadManager.STATE);
        }

        /*
        TODO: first focusable component
        if (!shaded && getComponentCount() > 0)
            getComponent(0).requestFocus();
        else
            requestFocus();
        */
    }

    public void fireIntermediateEvents() {
    }

    public boolean isEpochCheckEnabled() {
        return false;
    }
}
