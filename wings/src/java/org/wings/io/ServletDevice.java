/*
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
package org.wings.io;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * A Device encapsulating a ServletOutputStream.
 *
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 */
public final class ServletDevice implements Device {
    private ServletOutputStream out;
    private final String encoding;

    /**
     * Creates a Device which writes to a servlet output stream.
     *
     * @param out The servlet <b>byte</b> output stream
     * @param  characterEncoding The encoding to transcode Strings before passing to the byte output stream.
     */
    public ServletDevice (ServletOutputStream out, String characterEncoding) {
        this.out = out;
        this.encoding = characterEncoding;
    }

    public boolean isSizePreserving() { return true; }

    /**
     * Flush this Stream.
     */
    public void flush() throws IOException {
        out.flush();
    }

    public void close() throws IOException { 
        out.close(); 
    }

    /**
     * Print a String.
     */
    public Device print(String s) throws IOException {
        if (s == null)
            out.print("null");
        else {
            try {
                byte[] bytes = s.getBytes(this.encoding);
                for (int i = 0; i < bytes.length; i++) {
                    out.write(bytes[i]);
                }
            } catch (UnsupportedEncodingException e) {
                throw new IOException("Unknown session encoding?!" + e);
            }
        }
        return this;
    }

    /**
     * Print an integer.
     */
    public Device print(int i) throws IOException {
        out.print(i);
        return this;
    }

    /**
     * Print any Object
     */
    public Device print(Object o) throws IOException {
        if (o == null)
            out.print("null");
        else {
            try {
                byte[] bytes = o.toString().getBytes(this.encoding);
                for (int i = 0; i < bytes.length; i++) {
                    out.write(bytes[i]);
                }
            } catch (UnsupportedEncodingException e) {
                throw new IOException("Unknown session encoding?!" + e);
            }
        }
        return this;
    }

    /**
     * Print a character.
     */
    public Device print(char c) throws IOException {
        return print(new String(new char[] {c})); // reuse encoding handling of print(String)
    }

    /**
     * Print an array of chars.
     */
    public Device print(char[] c) throws IOException {
        return print(new String(c));
    }

    /**
     * Print a character array.
     */
    public Device print(char[] c, int start, int len) throws IOException {
        final int end = start + len;
        for (int i = start; i < end; ++i)
            out.print(c[i]);
        return this;
    }

    /**
     * Writes the specified byte to this data output stream.
     */
    public Device write(int c) throws IOException {
        out.write(c);
        return this;
    }

    /**
     * Writes b.length bytes from the specified byte array to this
     * output stream.
     */
    public Device write(byte b[]) throws IOException {
        out.write(b);
        return this;
    }

    /**
     * Writes len bytes from the specified byte array starting at offset
     * off to this output stream.
     */
    public Device write(byte b[], int off, int len) throws IOException {
        out.write(b, off, len);
        return this;
    }
}


