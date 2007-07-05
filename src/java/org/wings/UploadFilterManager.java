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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * Provides FilterOutputStream, that filters incoming files in {@link SFileChooser} components.
 *
 * You can use UploadFilters to inspect the stream or rewrite it to some own format.
 *
 * @author Holger Engels
 */
public class UploadFilterManager {
    private final transient static Log log = LogFactory.getLog(UploadFilterManager.class);

    private static HashMap filterMappings = new HashMap();

    public static void registerFilter(String name, Class filter, String errorText) {
        if (!FilterOutputStream.class.isAssignableFrom(filter))
            throw new IllegalArgumentException("class is not a FilterOutputStream!");

        Entry entry = new Entry(filter);
        entry.errorText = errorText;
        filterMappings.put(name, entry);
    }

    public static void registerFilter(String name, Class filter) {
        registerFilter(name, filter, null);
    }

    public static Class getFilterClass(String name) {
        int dividerIndex = name.indexOf(SConstants.UID_DIVIDER);
        name = name.substring(dividerIndex + 1);

        return ((Entry) filterMappings.get(name)).filterClass;
    }

    public static FilterOutputStream createFilterInstance(String name, OutputStream out) {
        FilterOutputStream filter = null;
        try {
            Entry entry = getFilterEntry(name);
            if (entry == null)
                filter = new FilterOutputStream(out);
            else {
                Class filterClass = entry.filterClass;
                if (filterClass != null) {
                    log.info("using " + filterClass.getName() + " for " + name);
                    Constructor constructor = filterClass.getConstructor(new Class[]{OutputStream.class});
                    filter = (FilterOutputStream) constructor.newInstance(new Object[]{out});
                    entry.filterInstance = filter;
                }
            }
        } catch (Exception e) {
            log.fatal("Exception", e);
        }
        return filter;
    }

    public static FilterOutputStream getFilterInstance(String name) {
        try {
            Entry entry = getFilterEntry(name);
            FilterOutputStream filterInstance = entry.filterInstance;
            return filterInstance;
        } catch (Exception e) {
            log.fatal(null, e);
            return null;
        }
    }

    private static Entry getFilterEntry(String name) {
        int dividerIndex = name.indexOf(SConstants.UID_DIVIDER);
        name = name.substring(dividerIndex + 1);

        return (Entry) filterMappings.get(name);
    }

    private static class Entry {
        public Class filterClass;
        public FilterOutputStream filterInstance;
        public String errorText;


        public Entry(Class filterClass) {
            this.filterClass = filterClass;
        }


        public void setInstance(FilterOutputStream filterInstance) {
            this.filterInstance = filterInstance;
        }
    }
}


