/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings;

import javax.swing.*;
import java.util.List;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SDefaultComboBoxModel
    extends SDefaultListModel
    implements MutableComboBoxModel
{
    /**
     * TODO: documentation
     */
    protected Object selectedItem = null;

    /**
     * TODO: documentation
     *
     * @param d
     */
    public SDefaultComboBoxModel(List d) {
        super(d);
    }

    public SDefaultComboBoxModel(Object[] d) {
        super(d);
    }

    /**
     * TODO: documentation
     *
     */
    public SDefaultComboBoxModel() {
    }

    /**
     * TODO: documentation
     *
     * @param anItem
     */
    public void setSelectedItem(Object anItem) {
        selectedItem = anItem;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public Object getSelectedItem() {
        return selectedItem;
    }

    /**
     * TODO: documentation
     *
     * @param obj
     */
    public void addElement(Object obj) {
        data.add(obj);
        fireIntervalAdded(this, getSize()-1, getSize()-1);
    }

    /**
     * TODO: documentation
     *
     * @param obj
     */
    public void removeElement(Object obj) {
        int index = data.indexOf(obj);
        removeElementAt(index);
    }

    public void insertElementAt(Object obj, int index) {
        data.add(index, obj);
        fireIntervalAdded(this, Math.min(index,getSize()-1),
                          Math.min(index,getSize()-1));
    }

    /**
     * TODO: documentation
     *
     * @param index
     */
    public void removeElementAt(int index) {
        if ( index>=0 && index<getSize() ) {
            data.remove(index);
            fireIntervalRemoved(this, index, index);
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
