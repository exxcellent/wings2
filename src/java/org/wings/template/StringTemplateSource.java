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
package org.wings.template;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * A simple template source, which provides a template from a String
 *
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class StringTemplateSource implements TemplateSource {

    private static long COUNTER = 0;

    private static final synchronized long getNextId() {
        return COUNTER++;
    }

    /**
     * Try to make generate a unique canonical name.
     */
    private final String canonicalName = getClass().getName() + "_" + getNextId();

    private String template;

    private long lastModified;


    public StringTemplateSource() {
    }


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
   Revision 1.4  2004/12/01 07:54:28  hengels
   o wings is not j-wings
   o styles are not lower case (they're derived from the class name)
   o the gecko.css should be modified carefully, because the konqueror.css is following it
   o the css files should be as small as possible

   Revision 1.3  2004/11/24 21:40:25  blueshift
   + commons logging
   + further empty javdoc removal

   Revision 1.2  2004/11/24 18:13:18  blueshift
   TOTAL CLEANUP:
   - removed document me TODOs
   - updated/added java file headers
   - removed emacs stuff
   - removed deprecated methods

   Revision 1.1.1.1  2004/10/04 16:13:30  hengels
   o start development of wings 2

   Revision 1.1  2002/10/26 11:59:43  ahaaf
   o initial

*/
