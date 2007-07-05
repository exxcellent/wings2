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
 * A <CODE>StreamDataSource</CODE> implements a DataSource
 * as a wrapper around a Stream.
 *
 * @author <A href="mailto:joachim.karrer@mercatis.de">Joachim Karrer</A>
 */
public class StreamTemplateSource implements TemplateSource {
    /**
     * enable debug output
     */
    private static final boolean DEBUG = true;

    /**
     * <p>the <code>InputStream</code> from which we are reading data.</p>
     */
    private InputStream iStream;

    /**
     * <p>the last time the <code>InputStream</code> was updated.</p>
     */
    private long modificationTime;

    /**
     * <p>generates a new StreamDataSource with the given Stream</p>
     *
     * @param iStream the InputStream from which the template is read.
     */
    public StreamTemplateSource(InputStream iStream) {
        setInputStream(iStream);
    }

    /**
     * <p>sets the InputStream and the modificationTime</p>
     *
     * @param iStream the InputStream from which the template is read.
     */
    public void setInputStream(InputStream iStream) {
        if (iStream == null) {
            throw new IllegalArgumentException("stream is null, this is invalid!!");
        }
        this.iStream = iStream;
        setModificationTime();
    }

    /**
     * <p>sets the modificationTime to the currentTimeMillis</p>
     */
    public void setModificationTime() {
        modificationTime = System.currentTimeMillis();
    }

    /**
     * <p>returns the canonical name of the inputStream (uaaaah!)</p>
     *
     * @return <p>always null, because stream is always to be parsed</p>
     */
    public String getCanonicalName() {
        return null;
    }

    /**
     * <p>Returns the time the content of this stream
     * was last modified.</p>
     * <p>The return value is used to decide whether to reparse a
     * Source or not. Reparsing is done if the value returned
     * here differs from the value returned at the last processing
     * time.</p>
     * <p><em>Attention: Modificationtime is only updated if a new stream is set!</em></p>
     *
     * @return long a modification time
     */
    public long lastModified() {
        return modificationTime;
    }

    /**
     * Gets an InputStream of the File.
     *
     * @return the actually set InputStream
     */
    public InputStream getInputStream() throws IOException {
        iStream.reset();
        return iStream;
    }
}


