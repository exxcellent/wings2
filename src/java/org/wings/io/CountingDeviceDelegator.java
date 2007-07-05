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
package org.wings.io;

import java.io.IOException;

public final class CountingDeviceDelegator implements Device {
    private final Device deligee;
    private long byteCount;

    public CountingDeviceDelegator(Device d) {
        deligee = d;
        byteCount = 0;
    }

    public boolean isSizePreserving() { return deligee.isSizePreserving(); }

    /**
     * Flush this Device.
     */
    public void flush() throws IOException { deligee.flush(); }

    public void close() throws IOException { deligee.close(); }

    /**
     * returns the number of bytes written to this data sink.
     */
    public long getSize() { return byteCount; }

    /**
     * reset the number of bytes to zero.
     */
    public void resetSize() { byteCount = 0; }

    /**
     * Print a character.
     */
    public Device print(char c) throws IOException {
        ++byteCount;
        deligee.print(c);
        return this;
    }

    /**
     * Print a character array.
     */
    public Device print(char[] c) throws IOException {
        if (c != null) byteCount += c.length;
        deligee.print(c);
        return this;
    }

    /**
     * Print len characters from the specified char array starting at offset
     * off to this Device.
     */
    public Device print(char[] c, int start, int len) throws IOException {
        byteCount += len;
        deligee.print(c, start, len);
        return this;
    }

    //-- print basic objects --

    /**
     * Print a String.
     */
    public Device print(String s) throws IOException {
        if (s != null) byteCount += s.length();
        deligee.print(s);
        return this;
    }

    /**
     * Print an integer.
     */
    public Device print(int i) throws IOException {
        byteCount += String.valueOf(i).length();
        deligee.print(i);
        return this;
    }

    /**
     * Print any Object
     */
    public Device print(Object o) throws IOException {
        if (o != null) byteCount += o.toString().length();
        deligee.print(o);
        return this;
    }

    /*-------------*
     ** Methods which write raw bytes to the Device. Much like an OutputStream.
     **-------------*/

    /**
     * Writes the specified byte to this data output stream.
     */
    public Device write(int c) throws IOException {
        ++byteCount;
        deligee.write(c);
        return this;
    }

    /**
     * Writes b.length bytes from the specified byte array to this
     * output stream.
     */
    public Device write(byte b[]) throws IOException {
        if (b != null) byteCount += b.length;
        deligee.write(b);
        return this;
    }

    /**
     * Writes len bytes from the specified byte array starting at offset
     * off to this Device.
     */
    public Device write(byte b[], int off, int len) throws IOException {
        if (b != null) byteCount += len;
        deligee.write(b, off, len);
        return this;
    }
}


