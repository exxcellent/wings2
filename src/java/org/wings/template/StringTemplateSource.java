/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of the wingS demo (http://wings.mercatis.de).
 *
 * The wingS demo is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.template;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.wings.template.TemplateSource;



/**
 * A simple template source, which provides a template from a String
 *
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class StringTemplateSource  implements TemplateSource {

    private static long COUNTER = 0;
    
    private static final synchronized long getNextId() {
	return COUNTER++;
    }

    /**
     * Try to make generate a unique canonical name.
     *
     */
    private final String canonicalName = getClass().getName() + "_" + getNextId();

    private String template;

    private long lastModified;

    /**
     * 
     */
    public StringTemplateSource() {
    }

    /**
     * 
     */
    public StringTemplateSource(String template) {
	setTemplate(template);
    }

    public final void setTemplate(String t) {
	this.template = t;
	lastModified = System.currentTimeMillis();
    }
    
    // Implementation of org.wings.template.TemplateSource

    public long lastModified() {
	return lastModified;
    }

    public InputStream getInputStream() throws IOException {
	return new ByteArrayInputStream(template.getBytes());
    }

    public final String getCanonicalName() {
	return canonicalName;
    }
    
}// StringTemplateSource

/*
   $Log$
   Revision 1.1  2004/10/04 16:13:30  hengels
   Initial revision

   Revision 1.1  2002/10/26 11:59:43  ahaaf
   o initial

*/
