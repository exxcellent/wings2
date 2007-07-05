/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
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
 * Default implementation of a model for {@link SComboBox} components.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class SDefaultComboBoxModel
        extends SDefaultListModel
        implements MutableComboBoxModel {
    protected Object selectedItem = null;


    public SDefaultComboBoxModel(List d) {
        super(d);
    }

    public SDefaultComboBoxModel(Object[] d) {
        super(d);
    }


    public SDefaultComboBoxModel() {
    }


    public void setSelectedItem(Object anItem) {
        selectedItem = anItem;
    }


    public Object getSelectedItem() {
        return selectedItem;
    }


    public void addElement(Object obj) {
        data.add(obj);
        fireIntervalAdded(this, getSize() - 1, getSize() - 1);
    }


    public void removeElement(Object obj) {
        int index = data.indexOf(obj);
        removeElementAt(index);
    }

    public void insertElementAt(Object obj, int index) {
        data.add(index, obj);
        fireIntervalAdded(this, Math.min(index, getSize() - 1),
                Math.min(index, getSize() - 1));
    }


    public void removeElementAt(int index) {
        if (index >= 0 && index < getSize()) {
            data.remove(index);
            fireIntervalRemoved(this, index, index);
        }
    }
}


