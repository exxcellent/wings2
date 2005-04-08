/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.tree;

import org.wings.SDelayedEventModel;
import org.wings.SListSelectionModel;

import javax.swing.tree.TreeSelectionModel;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public interface STreeSelectionModel extends TreeSelectionModel, SDelayedEventModel {
    /**
     * Tree selection model. See {@link STreeSelectionModel#setSelectionMode(int)}
     */
    public static final int SINGLE_SELECTION = SListSelectionModel.SINGLE_SELECTION;
    /**
     * Tree selection model. See {@link STreeSelectionModel#setSelectionMode(int)}
     */
    public static final int SINGLE_INTERVAL_SELECTION = SListSelectionModel.SINGLE_INTERVAL_SELECTION;
    /**
     * Tree selection model. See {@link STreeSelectionModel#setSelectionMode(int)}
     */
    public static final int MULTIPLE_INTERVAL_SELECTION = SListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
}


