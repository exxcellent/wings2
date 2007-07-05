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
package org.wings.plaf;

import org.wings.SComponent;
import org.wings.SFrame;


public interface FrameCG extends ComponentCG {

	public Update getAddHeaderUpdate(SFrame frame, Object header);

    public Update getAddHeaderUpdate(SFrame frame, int index, Object header);

    public Update getRemoveHeaderUpdate(SFrame frame, Object header);

    public Update getEpochUpdate(SFrame frame, String epoch);

    public Update getFocusUpdate(SFrame frame, SComponent focus);

    public Update getUpdateEnabledUpdate(SFrame frame, boolean enabled);

}