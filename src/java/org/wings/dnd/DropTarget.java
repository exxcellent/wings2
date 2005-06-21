/**
 * 
 */
package org.wings.dnd;

import java.util.List;

import org.wings.event.SComponentDropListener;

/**
 * @author ole
 *
 */
public interface DropTarget {
    public void addComponentDropListener(SComponentDropListener listener);
    public List getComponentDropListeners();
}
