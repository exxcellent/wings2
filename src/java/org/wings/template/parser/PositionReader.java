/*
 * Copyright (c) 1997-1999 The Java Apache Project.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. All advertising materials mentioning features or use of this
 *    software must display the following acknowledgment:
 *    "This product includes software developed by the Java Apache 
 *    Project for use in the Apache JServ servlet engine project
 *    (http://java.apache.org/)."
 *
 * 4. The names "Apache JServ", "Apache JServ Servlet Engine" and 
 *    "Java Apache Project" must not be used to endorse or promote products 
 *    derived from this software without prior written permission.
 *
 * 5. Products derived from this software may not be called "Apache JServ"
 *    nor may "Apache" nor "Apache JServ" appear in their names without 
 *    prior written permission of the Java Apache Project.
 *
 * 6. Redistributions of any form whatsoever must retain the following
 *    acknowledgment:
 *    "This product includes software developed by the Java Apache 
 *    Project for use in the Apache JServ servlet engine project
 *    (http://java.apache.org/)."
 *    
 * THIS SOFTWARE IS PROVIDED BY THE JAVA APACHE PROJECT "AS IS" AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE JAVA APACHE PROJECT OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Java Apache Group. For more information
 * on the Java Apache Project and the Apache JServ Servlet Engine project,
 * please see <http://java.apache.org/>.
 *
 */

package org.wings.template.parser;

import java.io.Reader;
import java.io.IOException;

/**
 * PositionReader can be asked for the 
 * current position in the stream.
 * @author <a href="mailto:zeller@think.de">Henner Zeller</a>
 * @version $Revision$ $Date$
 */
public class PositionReader 
  extends Reader {
  
  private Reader reader = null;
  private long position;
  private long savePosition = -1;

  public PositionReader (Reader r) {
    this.reader = r;
    position = 0;
  }

  /* ---- PositionReader ---- */
  public long getPosition () {
    return position;
  }

  /* ---- Implementation of Reader ---- */

  public int read () 
    throws IOException {
    int c = reader.read();
    if (c != -1) position++;
    return c;
  }

  public int read (char cbuff[]) 
    throws IOException {
    int num = reader.read (cbuff);
    if (num > 0) position += num;
    return num;
  }

  public int read (char cbuff[], int off, int len)
    throws IOException {
    int num = reader.read (cbuff, off, len);
    if (num > 0) position += num;
    return num;
  }

  public long skip (long n) 
    throws IOException {
    long skipped = reader.skip (n);
    position += skipped;
    return skipped;
  }

  public boolean ready () 
    throws IOException {
    return reader.ready();
  }

  public boolean markSupported () {
    return reader.markSupported();
  }

  public void mark (int readAheadLimit) 
    throws IOException {
    savePosition = position;
    reader.mark (readAheadLimit);
  }

  public void reset () 
    throws IOException {
    reader.reset();
    // this should be noticed by the reader before
    if (savePosition < 0)
      throw new IOException ("mark() not set before");
    position = savePosition;
  }

  public void close ()
    throws IOException {
    reader.close();
  }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
