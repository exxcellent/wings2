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
package org.wings.template;

import java.io.IOException;
import java.io.InputStream;

/**
 * A TemplateSource for a Template. This encapsulates what is the notion
 * of a template from the parsers point of view: some named entity, that
 * might change over time and thus has a modification time; an input stream
 * to access the contents.
 * <p/>
 * So <CODE>TemplateSource</CODE> is a general source where templates can come
 * from. You can think of Files, URLs, Database-BLOBS ..
 *
 * @author <A href="mailto:H.Zeller@acm.org">Henner Zeller</A>
 */
public interface TemplateSource {
    /**
     * Returns a canonical name of this TemplateSource.
     * <p/>
     * The name returned here is used to look up the parsing
     * result of the internal caching, so it should differ
     * for all different Sources :-)
     * May return <code>null</code> if this Source is supposed
     * to be parsed each time. The canonical name would be something
     * like a filename or an URL.
     *
     * @return the canonical name of the source. like 'file:/web/template.html'
     */
    String getCanonicalName();

    /**
     * Returns the time the content of this TemplateSource
     * was last modified.
     * <p/>
     * The return value is used to decide whether to reparse a
     * Source or not. Reparsing is done if the value returned
     * here <em>differs</em> from the value returned at the last processing
     * time. This may not return a 'real' time, it needs just
     * to be comparable to itself; so some sort of version counter instead
     * of time would be perfect as well.
     *
     * @return long a modification time
     */
    long lastModified();

    /**
     * Gets an InputStream of this TemplateSource.
     * <p/>
     * <em>Note</em>
     * that this method may be called twice if the page has to
     * be parsed first. So you probably have to implement a
     * buffer if your underlying source is transient ..
     *
     * @return an InputStream the content of the template is read from.
     */
    InputStream getInputStream() throws IOException;
}




