package org.wings.event;

import java.util.EventListener;

/**
 * A listener to react on 'virtual' mouse clicks. Refer to {@link org.wings.STable#addMouseListener(SMouseListener)}
 *  and {@link org.wings.STree#addMouseListener(SMouseListener)}.
 * 
 * If you derive from SMouseAdapter, your class stays compilable, if we decide
 * to add new methods to the SMouseListener interface.
 *
 * @author hengels
 */
public interface SMouseListener extends EventListener
{
    /**
     * Invoked when the mouse button has been clicked on a component.
     */
    void mouseClicked(SMouseEvent e);
}
