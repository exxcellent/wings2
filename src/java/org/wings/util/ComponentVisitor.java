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
package org.wings.util;

import org.wings.SComponent;
import org.wings.SContainer;

/**
 * A visitor that is visits component hierarchies.
 * The SComponent and SContainer implement the corresponding
 * invite method.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 */
public interface ComponentVisitor {
    /**
     * Visit a SComponent.
     *
     * @param component the component to be visited
     */
    void visit(SComponent component) throws Exception;

    /**
     * Visit a SContainer. A container contains multiple
     * elements. If you are interested in these components,
     * invite yourself
     * ({@link SContainer#inviteEachComponent(ComponentVisitor)})
     *
     * @param container the component to be visited
     */
    void visit(SContainer container) throws Exception;
}


