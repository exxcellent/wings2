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

/**
 * <CODE>SpecialTagHandler</CODE> represents a tag which requires
 * handling within server side parsed page. This could be a
 * class handling &lt;SERVLET&gt;-Tags or any other tag. See
 * PageParser, SSI and ParameterPropagatingSSI for examples.
 *
 * <p>A class to plug into PageParser <em>must</em> implement 
 * this interface and <em>must</em> have a simple constructor 
 * without parameters and can then be added with
 * <code>addTagHandler()</code> to a PageParser servlet.
 *
 * @version $Revision$ $Date$
 * @author <A href="mailto:zeller@think.de">Henner Zeller</A>
 *
 */

package org.wings.template.parser;

import java.io.*;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;

public interface SpecialTagHandler {
    /**
     * Parse tag.
     * This method is invoked if a tag with a name
     * this class is registered for is found.
     * It gets the ServletConfiguration of the PageParser
     * servet and a PositionReader, placed after name of
     * the current tag.<p> 
     * The <CODE>parseTag()</CODE> method is called <em>before</em>
     * any processing is done. Its purpose is to parse the area
     * this tag spans and probably read in parameters used at
     * execution time.<p>
     * This method returns the last tag belonging to the
     * area handled by this handler (for server side included
     * servlets, this would be <CODE>&lt;/SERVLET&gt;</CODE>).
     * For simple one-tag Handlers this is simply the tag passed
     * to this method. If the Handler decides not to handle this tag, this 
     * method should return <CODE>null</CODE>.
     * @param config    Servlet configuration
     * @param input     The PositionReader, located after the Name token 
     *                  of the Tag
     * @param startPos  The Position parsing of this token began
     * @param startTag  the SGMLTag found in the file.
     */
    SGMLTag parseTag(ParseContext context,
		     PositionReader input, 
		     long startPos,
		     SGMLTag startTag)
	throws IOException;
  
    /**
     * Get start position of the area in the sourcefile this
     * handler processes.
     * This is usually the position of the &quot;<code>&lt;</code>&quot;
     * of the <code>&lt;SpecialTag&gt;</code> within the inputfile.
     */
    long getTagStart();
  
    /**
     * Get the length of the area in the sourcefile.
     * The area this handler processes is skipped in the inputfile.
     * Usually this represents the length of the single
     * <code>&lt;SpecialTag&gt;</code> or the 
     * area <code>&lt;SpecialTag&gt;...&lt;/SpecialTag&gt;</code>
     */
    long getTagLength();
  
    /**
     * actually perform the action associated with this tag.
     * @exception Exception anything can happen .. and throw an Exception 
     *            which is caught in PageParser
     */
    void executeTag (ParseContext context, InputStream input)
	throws Exception;
}

/* 
 * Local variables:
 * c-basic-offset: 4
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */


