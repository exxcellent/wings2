/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.externalizer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This singleton externalizes
 * {#link AbstractExternalizeManager#GLOBAL global} scope. Every object
 * externalized by the SystemExternalizeManager (global scope) is available
 * over the life time of the servlet container and is not garbage collected.
 * <p/>
 * Created: Sat Nov 10 15:49:15 2001
 *
 * @author <a href="mailto:armin@hyperion.intranet.mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */

public class SystemExternalizeManager extends AbstractExternalizeManager {
    /**
     * singleton implementation
     */
    private static final SystemExternalizeManager sharedInstance = new SystemExternalizeManager();

    private final String MY_PREFIX_TIMESLICE_STRING = "-" + PREFIX_TIMESLICE_STRING;

    protected final Map/*<String, ExternalizedResource>*/ externalized;


    private SystemExternalizeManager() {
        externalized = Collections.synchronizedMap(new HashMap());
    }

    /**
     * get the single system wide instance.
     *
     * @return the SystemExternalizeManager instance.
     */
    public static SystemExternalizeManager getSharedInstance() {
        return sharedInstance;
    }


    protected String getPrefix() {
        return MY_PREFIX_TIMESLICE_STRING;
    }

    protected void storeExternalizedResource(String identifier,
                                             ExternalizedResource extInfo) {
        if (log.isDebugEnabled()) {
            log.debug("store identifier " + identifier + " " + extInfo.getObject().getClass());
            log.debug("flags " + extInfo.getFlags());
        }

        externalized.put(identifier, extInfo);
    }

    public ExternalizedResource getExternalizedResource(String identifier) {
        if (identifier == null || identifier.length() < 1)
            return null;

        log.debug("system externalizer: " + identifier);

        int pos = identifier.indexOf(".");
        if (pos > -1) {
            identifier = identifier.substring(0, pos);
        }

        log.debug("system externalizer " + identifier);
        return (ExternalizedResource) externalized.get(identifier);
    }

    public final void removeExternalizedResource(String identifier) {
        externalized.remove(identifier);
    }
}



