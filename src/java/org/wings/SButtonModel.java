/* $Id$ */
package org.wings;

import javax.swing.event.ChangeListener;

public interface SButtonModel {
    void setSelected(boolean selected);
    boolean isSelected();

    void addChangeListener(ChangeListener listener);
    void removeChangeListener(ChangeListener listener);
}
