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

import java.io.*;
import java.util.HashMap;
import java.lang.reflect.*;
import java.util.logging.*;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class UploadFilterManager
{
    private final static Logger logger = Logger.getLogger("org.wings.servlet");

    private static HashMap filterMappings = new HashMap();

    /**
     * TODO: documentation
     */
    public static void registerFilter(String name, Class filter, String errorText) {
        if (!FilterOutputStream.class.isAssignableFrom(filter))
            throw new IllegalArgumentException("class is not a FilterOutputStream!");

        Entry entry = new Entry(filter);
        entry.errorText = errorText;
        filterMappings.put(name, entry);
    }

    /**
     * TODO: documentation
     */
    public static void registerFilter(String name, Class filter) {
        registerFilter(name, filter, null);
    }

    /**
     * TODO: documentation
     */
    public static Class getFilterClass(String name) {
        int dividerIndex = name.indexOf(SConstants.UID_DIVIDER);
        name = name.substring(dividerIndex+1);

        return (Class)((Entry)filterMappings.get(name)).filterClass;
    }

    /**
     * TODO: documentation
     */
    public static FilterOutputStream createFilterInstance(String name, OutputStream out) {
        FilterOutputStream filter = null;
        try {
            Entry entry = getFilterEntry(name);
            if (entry == null)
                filter = new FilterOutputStream(out);
            else {
                Class filterClass = entry.filterClass;
                if (filterClass != null) {
                    logger.info("using " + filterClass.getName() + " for " + name);
                    Constructor constructor = filterClass.getConstructor(new Class[] { OutputStream.class });
                    filter = (FilterOutputStream)constructor.newInstance(new Object[] { out });
                    entry.filterInstance = filter;
                }
            }
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        return filter;
    }

    /**
     * TODO: documentation
     */
    public static FilterOutputStream getFilterInstance(String name) {
        try {
            Entry entry = getFilterEntry(name);
            FilterOutputStream filterInstance = entry.filterInstance;
            return filterInstance;
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            return null;
        }
    }

    private static Entry getFilterEntry(String name) {
        int dividerIndex = name.indexOf(SConstants.UID_DIVIDER);
        name = name.substring(dividerIndex+1);

        return (Entry)filterMappings.get(name);
    }

    private static class Entry
    {
        public Class filterClass;
        public FilterOutputStream filterInstance;
        public String errorText;

        /**
         * TODO: documentation
         *
         * @param filterClass
         */
        public Entry(Class filterClass) {
            this.filterClass = filterClass;
        }

        /**
         * TODO: documentation
         *
         * @param filterInstance
         */
        public void setInstance(FilterOutputStream filterInstance) {
            this.filterInstance = filterInstance;
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
