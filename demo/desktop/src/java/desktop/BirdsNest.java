/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package desktop;

import org.wings.SDesktopPane;
import org.wings.SComponent;
import org.wings.event.SComponentDropListener;


/**
 * @author hengels
 * @version $Revision$
 */
public class BirdsNest
        extends SDesktopPane
{
    public SComponent addComponent(SComponent component, final Object constraints, int index) {
        if (component instanceof Bird) {
            final Bird bird = (Bird)component;
            bird.addComponentDropListener(new SComponentDropListener() {
                public boolean handleDrop(SComponent dragSource) {
                    if (dragSource == bird)
                        return false;
                    int index = getComponentList().indexOf(bird);
                    remove(dragSource);
                    BirdsNest.super.addComponent(dragSource, null, index);
                    return true;
                }
            });
        }
        return super.addComponent(component, constraints, index);
    }
}
