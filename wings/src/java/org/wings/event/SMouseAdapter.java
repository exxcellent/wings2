package org.wings.event;

/**
 * An abstract adapter class for receiving mouse events. The methods in this class are empty. This class exists as
 * convenience for creating listener objects.
 *
 * If you derive from SMouseAdapter, your class stays compilable, if we decide
 * to add new methods to the SMouseListener interface.
 *
 * @author hengels
 */
public abstract class SMouseAdapter implements SMouseListener {
    public void mouseClicked(SMouseEvent e) {}
}
