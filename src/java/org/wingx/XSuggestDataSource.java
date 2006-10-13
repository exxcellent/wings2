/*
 * $Id: wings.js 2790 2006-10-05 13:59:11Z cjschyma $
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

package org.wingx;

import java.util.List;

/**
 *
 * @author Christian Schyma
 */
public interface XSuggestDataSource {
    List getData(String part);
    
}
