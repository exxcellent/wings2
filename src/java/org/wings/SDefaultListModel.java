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

import java.util.List;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.event.ListDataListener;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class SDefaultListModel
    extends AbstractListModel
{
    /**
     * TODO: documentation
     */
    protected final ArrayList data = new ArrayList(2);

    /**
     * TODO: documentation
     *
     * @param d
     */
    public SDefaultListModel(List d) {
        data.clear();
        if ( d != null ) {
            for ( int i=0; i<d.size(); i++ )
                data.add(d.get(i));
        }
    }

    /**
     * TODO: documentation
     *
     * @param d
     */
    public SDefaultListModel(Object[] d) {
        data.clear();
        if ( d != null ) {
            for ( int i=0; i<d.length; i++ )
                data.add(d[i]);
        }
    }

    /**
     * TODO: documentation
     *
     */
    public SDefaultListModel() {
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getSize() {
        return data.size();
    }

    /**
     * TODO: documentation
     *
     * @param i
     * @return
     */
    public Object getElementAt(int i) {
        return data.get(i);
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
