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

/**
 * For the current selection state of any component that display a list of values. 
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public interface SListSelectionModel extends ListSelectionModel, SDelayedEventModel {
    /**
     * A value for the selection mode disallowing selection of any list element
     */
    int NO_SELECTION = -1;
}


