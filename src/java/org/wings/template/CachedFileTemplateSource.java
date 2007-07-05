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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;

/**
 * A <CODE>CachedFileDataSource</CODE> implements a DataSource
 * for a file, but caches small ones.
 *
 * @author <A href="mailto:zeller@think.de">Henner Zeller</A>
 */
public class CachedFileTemplateSource extends FileTemplateSource {
    private final  static Log log = LogFactory.getLog(CachedFileTemplateSource.class);

    private final static class CacheEntry {
        private byte[] filebuffer = null;
        private long lastModified;
        private File file;

        public CacheEntry(File f) throws IOException {
            this.file = f;
            lastModified = -1;
            // we want to throw an IOException here if the file is not found
            if (file != null) {
                lastModified = file.lastModified();
                refresh();
            }
        }

        public CacheEntry(URL url) throws IOException {
            file = null;
            lastModified = System.currentTimeMillis();
            InputStream in = url.openStream();
            /*
             * unfortnunatly, we do not know the length of
             * the data ..
             */
            int totLen = 0;
            int copyLen = 0;
            byte[] tempBuffer = new byte[1024];
            do {
                copyLen = in.read(tempBuffer);
                if (copyLen > 0) {
                    byte[] newFileBuf = new byte[totLen + copyLen];
                    if (filebuffer != null)
                        System.arraycopy(filebuffer, 0, newFileBuf, 0, totLen);
                    System.arraycopy(tempBuffer, 0, newFileBuf, totLen,
                            copyLen);
                    totLen += copyLen;
                    filebuffer = newFileBuf;
                }
            } while (copyLen >= 0);
            in.close();
        }

        public byte[] getBuffer() {
            return filebuffer;
        }

        /**
         * returns the time, this file has been
         * last modified. This checks the Timestamp of
         * the file and initiates a reload to the cache
         * if it changed.
         */
        public long lastModified() {
            checkModified();
            return lastModified;
        }

        private void checkModified() {
            if (file == null)
                return;
            long timestamp = file.lastModified();
            if (lastModified != timestamp) {
                lastModified = timestamp;
                try {
                    refresh();
                } catch (IOException e) {
                    /* ignore currently, file might have been deleted, but is
                     * still in cache.
                     */
                    if (log.isErrorEnabled()) {
                        log.error(file.getAbsolutePath() + " not found. Maybe it has been deleted from the filesystem.");
                    }
                }
            }
        }

        private void refresh() throws IOException {
            int len = (int) file.length();
            filebuffer = new byte[len];
            FileInputStream in = new FileInputStream(file);
            int pos = 0;
            while (pos < len) {
                pos += in.read(filebuffer, pos, len - pos);
            }
            in.close();
        }
    }

    /*
     * we should provide a way to expunge old
     * entries here ...
     */
    private static Hashtable cache = new Hashtable();
    private static final int CACHED_LIMIT = 1024;

    private transient CacheEntry entry;

    public CachedFileTemplateSource(File f) throws IOException {
        super(f);

        entry = (CacheEntry) cache.get(f);
        if (entry == null && f.length() <= CACHED_LIMIT) {
            entry = new CacheEntry(f);
            cache.put(f, entry);
        }
    }

    public CachedFileTemplateSource(URL url)
            throws IOException {
        super(null); // we never read the file directly
        entry = (CacheEntry) cache.get(url);
        if (entry == null) {
            entry = new CacheEntry(url);
            cache.put(url, entry);
        }
        canonicalName = url.toString();
    }

    /**
     * Returns the time the content of this File
     * was last modified.
     * <p/>
     * The return value is used to decide whether to reparse a
     * Source or not. Reparsing is done if the value returned
     * here differs from the value returned at the last processing
     * time.
     *
     * @return long a modification time
     */
    public long lastModified() {
        if (entry != null)
            return entry.lastModified();
        else
            return super.lastModified();
    }

    /**
     * Gets an InputStream of the File.
     */
    public InputStream getInputStream()
            throws IOException {
        if (entry != null)
            return new ByteArrayInputStream(entry.getBuffer());
        else
            return super.getInputStream();
    }
}


