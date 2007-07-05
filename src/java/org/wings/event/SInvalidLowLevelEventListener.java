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
package org.wings.event;

import java.util.EventListener;

/**
 * This interface is used to notify the application about dropped dispatches due to invalid (old)
 * Epoch ids.
 * Can be used to i.e. simulate an application back button.
 * @author Benjamin Schmid
 */
public interface SInvalidLowLevelEventListener extends EventListener {

    void invalidLowLevelEvent(InvalidLowLevelEvent e);

}
