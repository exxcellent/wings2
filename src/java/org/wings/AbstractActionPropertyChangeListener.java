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

import java.beans.*;
import java.lang.ref.*;
import javax.swing.Action;

/**
 * TODO: documentation
 *
 * @author
 * @version $Revision$
 */
abstract class AbstractActionPropertyChangeListener
    implements PropertyChangeListener
{
    private static ReferenceQueue queue;

    private WeakReference target;
    private Action action;

    AbstractActionPropertyChangeListener(SComponent c, Action a) {
	super();
	setTarget(c);
	this.action = a;
    }

    public void setTarget(SComponent c) {
	if (queue == null) {
	    queue = new ReferenceQueue();
	}

	OwnedWeakReference r;
	while ((r = (OwnedWeakReference)queue.poll()) != null) {
	    AbstractActionPropertyChangeListener oldPCL = 
		(AbstractActionPropertyChangeListener)r.getOwner();
	    Action oldAction = oldPCL.getAction();
	    if (oldAction!=null)
		oldAction.removePropertyChangeListener(oldPCL);
	}
	this.target = new OwnedWeakReference(c, queue, this);
    }

    public SComponent getTarget() {
	return (SComponent)this.target.get();
    }

    public Action getAction() {
	return action;
    }

    private static class OwnedWeakReference extends WeakReference {
	private Object owner;

	OwnedWeakReference(Object target, ReferenceQueue queue, Object owner) {
	    super(target, queue);
	    this.owner = owner;
	}

	public Object getOwner() {
	    return owner;
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
