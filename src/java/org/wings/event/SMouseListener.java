package org.wings.event;

import java.util.EventListener;

/**
 * If you derive from SMouseAdapter, your class stays compilable, if we decide
 * to add new methods to the SMouseListener interface.
 *
 * @author hengels
 * @version $Revision$
 */
public interface SMouseListener extends EventListener
{
    /**
     * Invoked when the mouse button has been clicked on a component.
     */
    public void mouseClicked(SMouseEvent e);
}
