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

package org.wings.plaf.css;

/**
 * This Interface is a helper. It is used to identify components who, in their
 * representation, are relying on button tags.
 * IE has a bug. It sends all button tags as name value pairs, not only the one
 * which was pressed. So we need to filter out the falsely sent events while
 * dispatching. If a ComponentCG implements this interface, it will be handled
 * by the described workaround.
 * @author ole
 *
 */
public interface MSIEButtonFix {

}
