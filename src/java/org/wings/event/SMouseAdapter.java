package org.wings.event;

/**
 * If you derive from SMouseAdapter, your class stays compilable, if we decide
 * to add new methods to the SMouseListener interface.
 *
 * @author hengels
 * @version $Revision$
 */
public abstract class SMouseAdapter
    implements SMouseListener
{
    public void mouseClicked(SMouseEvent e) {}
}
