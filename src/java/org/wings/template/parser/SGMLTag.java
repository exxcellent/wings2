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

import java.io.*;
import java.util.*;

/**
 * Convenient class for parsing SGML tokens from a page.
 *
 * <p>This class is optimized for speed, not ease of use.
 * (Though I'd contend its fairly easy to use anyway!).
 *
 * <p>Other than earlier versions of this class this one reads
 * its content from a <code>Reader</code> to avoid reading
 * the whole file into a String before parsing it.
 * The Reader is required to support the <code>mark()</code>
 * operation.
 *
 * <p>Tags are only read enough to find out what the tag name is;
 * If you want to read the full tag call <code>parse(inputReader)</code>.
 * This is done so that applications don't spend time processing
 * tags about which they care little.
 *
 * <p>Here's a sample piece of code which uses this class to read
 * all SGML tags on a page:
 *
 * <pre>
 * void showTags(PrintWriter out, Reader input)
 * {
 *      SGMLTag tag = new SGMLTag(input);
 *      while (!tag.finished()) {
 *          out.println ("tag: " + tag.toString());
 *          tag = new SGMLTag (input);
 *      }
 * }
 * </pre>
 *
 * @author <a href="mailto:williams@ugsolutions.com">Tim Williams</a>
 * @author <a href="mailto:zeller@think.de">Henner Zeller</a>
 * @version $Revision$ $Date$
 */

/*
 * ToDo: (hen)
 * - read incomplete TAGs <input type="checkbox" checked> => checked=1
 */

public class SGMLTag {
    public final static char singleQuote = '\'';
    public final static char doubleQuote = '\"';

    /**
     * Name of this SGML tag, in uppercase format.
     * This is only public for compatibility reasons.
     * @deprecated do not use; use getName() instead
     */
    public String name = null;

    /**
     * The token that closes this tag.
     * Different for SSI and SGML tags.
     */
    private String closeTag = null;

    /**
     * Number of characters skipped <
     */
    private int offset = 0;

    /* These attributes are to be compatible with the 'old'
     * SGMLTag using Strings
     */
    /**
     * @deprecated
     */
    public int start = 0;
    /**
     * @deprecated
     */
    public int end = 0;

    // private stuff
    private LinkedList attrs = null;            // tag attributes (mixed)
    private LinkedHashMap values = null;        // tag attribute values (uc)
    private boolean wellFormed = true;      // looks good?
    private boolean attr_ready = false;
  
    // comment delimitation
    static final String COMMENT_START = "!--", COMMENT_END = "-->";
    static final String SSI_START = COMMENT_START + "#", SSI_END = COMMENT_END;
  
   /**
    * for historical reasons only; behaves like the
    * old SGMLTag().
    *
    * @deprecated use the Reader based constructors instead
    */
    public SGMLTag(String textContent, int begin) {
      PositionReader r = new PositionReader (new StringReader (textContent));
      try {
	r.skip (begin);
	offset = begin;
	searchStart (r);
	start = offset;
	// do a full parse here; since the usage of the
	// String based SGMLTag() is deprecated this
	// performance penalty doesn't matter
	parse (r); 
      } catch (IOException reading_from_string_should_never_fail) {
	offset = -1;
      }
      end = (int) r.getPosition();
    }

    /**
     * Create new SGML tag reference, starting at current location
     * of the Reader.
     * At first, only the type of tag (first argument) is read if
     * <code>parseIt</code> is false.
     * Tag may not be well-formed: if interested, call "parse(input)"
     * directly afterwards (without reading any characters
     * from the Reader) to get the attributes.
     *
     * <p>Note that this constructor skips over any HTML-style comments,
     * as denoted by matched <tt>&lt;--</tt> ... <tt>--&gt;</tt> pairs.
     * @param input the Reader being parsed for SGML tags
     * @param parseIt boolean which denotes if SGMLTag should be
     *                parsed fully
     * @see #attributes
     */
     public SGMLTag (Reader input, boolean parseIt) 
       throws IOException {
      searchStart (input);
      if (parseIt) readAttributes (input);
     }

    /**
     * Create new SGML tag reference, starting at current location
     * of the Reader. Read all attributes.
     *
     * <p>Note that this constructor skips over any HTML-style comments,
     * as denoted by matched <tt>&lt;--</tt> ... <tt>--&gt;</tt> pairs.
     * @param input the Reader being parsed for SGML tags
     * @see #attributes
     */
    public SGMLTag (Reader input)
      throws IOException {
      this (input, true);
    }

    public void parse (Reader input) 
      throws IOException {
      readAttributes (input);
    }

    /**
     * Skip over any HTML-style comments,
     * as denoted by matched <tt>&lt;--</tt> ... <tt>--&gt;</tt> pairs.
     *
     * @param reader the reader being parsed for SGMLtags
     */
  protected void searchStart (Reader input)
    throws IOException {
      int c = 0, num;
      char buff[] = new char[8]; // must at least hold the length of COMMENT_(START|END)
      String cmpStr;
      
      // skipping over comments, find first tag
        while (true) {
	  // find starting character of SGML tag
	  while (c >= 0 && c != '<') {
	    c = input.read();
	    offset++;
	  }
	  if (c == -1) { offset = -1; return; } // EOF
	  offset--;

	  /* -- check if we just found a comment
	   * <!--# - SSI Commands start just like
	   * ordinary comments, so we've to make sure
	   * that exclude these (<!--) but not those (<!--#)
	   */
	  input.mark (SSI_START.length());
	  int pos;
	  num=0;
	  for (pos=0; pos >= 0 && num < SSI_START.length(); num+=pos)
	      pos = input.read (buff, pos, SSI_START.length()-pos);
	  if (pos == -1) { offset = -1; return; } // EOF

	  cmpStr = new String (buff, 0, num);
	  if (SSI_START.equals(cmpStr) || 
	      !(cmpStr.startsWith (COMMENT_START))) {
	    input.reset();
	    break;         // No comment .. real start of a SGML / SSI Tag
	  }

	  /*
	   * ok, we got an comment; but since we read SSI_START length
	   * characters, we've to reset and just read COMMENT_START so
	   * we're in a defined state ..
	   */
	  input.reset();
	  num = 0;
	  for (pos=0; pos >= 0 && num < COMMENT_START.length(); num+=pos)
	      pos = input.read (buff, pos, COMMENT_START.length()-pos);
	  // since length(COMMENT_START) < length(SSI_START) (which we
	  // already successfully read), we don't have to check for EOF here

	  offset += COMMENT_START.length() + 1; // +1 for the starting '<'
	  // otherwise skip extent of commented area
	  boolean endOfComment = false;
	  int len=0, ringHead = 0;
	  int checkpos, p;
	  while (!endOfComment) {
	    c = input.read();
	    if (c == -1) { offset=-1; return; } // EOF
	    len++;
	    offset++;
	    // since we don't have '-1' here anymore, cast is save:
	    buff[ringHead] = (char) c;  // buffer is a ringbuffer
	    if (len >= COMMENT_END.length()) {
	      // compare, beginning from the last position backward
	      for (checkpos = ringHead + buff.length, p = COMMENT_END.length()-1 ; 
		   p >= 0 ; --checkpos, --p) {
		if (COMMENT_END.charAt(p) != buff[checkpos % buff.length])
		  break;
	      }
	      endOfComment = (p == -1);
	    }
	    ringHead = (++ringHead) % buff.length;
	  } 

	}
	
	// get the name
	// do not skip Whitespaces, since the Tagname must
	// start just after the '<'
        name = nextToken(input, false); 
        if (name != null)
            name = name.toUpperCase();

	// set the token that closes this tag
	if (name != null && name.startsWith(SSI_START)) {
	    closeTag = SSI_END; // SSI tag
	} else {
	    closeTag = ">"; // SGML tag
	}
    }


    /**
     * Checked whether this tag indicates we're at the end of the list.
     * Note: The end tag is not usuable as an SGML tag.
     * @return true if this tag represents end of tags, and is not usuable
     */
    public boolean finished() {
        return offset == -1 && name == null;
    }

    /**
     * Check name of tag.
     * (Comparision is case-insensitive.)
     * @return true if passed tag matches this one.
     */
    public boolean isNamed(String name) {
        return this.name != null && this.name.equals(name.toUpperCase());
    }

    /**
     * Check for well-formedness of this tag.
     * Note that calling this method causes rest of tag to be parsed.
     * @return true if tag is a well-formed SGML tag, false otherwise
     */
    public boolean isWellFormed() {
        if (name == null) return false;
        if (!attr_ready || values == null) return false;
        return wellFormed;
    }

   /**
    * returns the number of chars skipped before the
    * starting '&lt'
    */
    public int getOffset () {
      return offset;
    }

    /**
     * get the Name of this SGML tag, in uppercase format.
     * For example, P for paragraph, B for bold, etc.
     * This value is set to null when whitespace or another
     * problem was encountered where the tag would be.
     */
    public String getName () {
      return name;
    }

    /**
     * Get list of attribute names.
     * @param upperCase true returns names in all uppercase (good for
     * case-insensitive applications), false returns attribute names
     * with same case as in original text
     * @return enumeration of attribute names specified as strings,
     * or null if this tag is poorly formed
     */
    public Iterator attributes(boolean upperCase) {
        // check to make sure attributes have been read
        if (!isWellFormed())
            return null;

        // or return uppercase names?
        if (upperCase) {
            return values.keySet().iterator();
        } else {
            return attrs.iterator();
        }
    }

    /**
     * Get attribute value, or default if not set.
     * Case is ignored, <tt>value("a")</tt> will return the same
     * result as <tt>value("A")</tt>.  Note also that if wish to
     * check whether value was set, you can pass <tt>null</tt>
     * as the defaultValue.
     * @param attributeName attribute for which to check
     * @param default value if attribute unset
     * @return value of attribute, or defaultValue if not available
     */
    public String value(String attributeName, String defaultValue) {
        if (!isWellFormed())
	  return null;
        String value = (String) values.get(attributeName.toUpperCase());
        return value == null ? defaultValue : value;
    }

    /**
     * Attempt to read attributes from tag if not already read.
     * @return true if everything was read fine, false otherwise
     */
    private boolean readAttributes(Reader input) 
    throws IOException {
      // just try to read Attributes once

        if (attr_ready)
	  return wellFormed && values != null;
        attr_ready = true;

        if (values == null && wellFormed) {
            String key = null, token;
            wellFormed = false;
            attrs = new LinkedList();
            values = new LinkedHashMap();

            while (true) {
		// check for valid value tag (or end delimiter)
		if (key == null)
		    key = nextToken(input);

		// close-Tag
		if (key != null && key.equals(closeTag)) {
		    wellFormed = true;
		    break;
		}

		// close-Tag
		if (key != null && key.equals("/>")) {
		    wellFormed = true;
		    break;
		}

		// 'key'-part 
		if (key == null 
		    || isDelimiter(key.charAt(0))
		    || key.charAt(0) == doubleQuote
		    || key.charAt(0) == singleQuote)
		    break;

		// ok, we have a key. Now insure that we have an equals sign
		token = nextToken(input);
		if (token == null || token.charAt(0) != '=') {
		    attrs.add(key);
		    if (token == null)
			break;
		    key = token; // this token is the next key
		    continue;
		}

		// read value of tag
		token = nextToken(input);
		if (token == null || isDelimiter(token.charAt(0)))
		    break;

		// strip quotes
		if (token.charAt(0) == doubleQuote || token.charAt(0) == singleQuote) 
		    token = token.substring(1, token.length() - 1);

		// store attribute name with original case
		String upperCase = key.toUpperCase();
		if (!values.containsKey(upperCase))
		    attrs.add(key);

		// store assignment in case-insensitive manner
		values.put(upperCase, token);
		key = null; // clear this key; next token is our next key.
            }
        }
        return wellFormed && values != null;
    }

    /**
     * Return value of attribute (parameter) setting in SGML tag.
     * @param key name (uppercase) of attribute for which to check
     * @param default value if attribute unset
     * @deprecated use <tt>attributes()</tt> and <tt>value()</tt> instead
     * @see #attributes
     * @see #value
     * @return value of that attribute, or default if not defined
     */
    public String getAttribute(String key, String defaultValue) {
        return value(key, defaultValue);
    }

    /**
     * Return tag attributes and values.
     * @param ignoreCase false converts all keys to uppercase, true leaves
     * case of values alone
     * @return parameter key / value pairs
     * @deprecated use <tt>attributes()</tt> and <tt>value()</tt> instead
     * @see #attributes
     * @see #value
     */
    public HashMap getAttributes() {
        return isWellFormed() ? values : null;
    }

    /**
     * Read next token from string.
     * A token is a space-delimited word, a string in quotes
     * (returned with quotes), a delimiter such as a greater-than,
     * less-than, or equals sign.
     * Quotes marks inside quoted strings may be escaped with a
     * backslash (\) character.
     * @param string string begin parsed
     * @param index location within string to start examining
     * @return next token, or null if whitespace was encountered
     */
    public String nextToken(Reader input) 
	throws IOException {
	return nextToken (input, true);
    }

    /**
     * Read next token from string.
     * A token is a space-delimited word, a string in quotes
     * (returned with quotes), a delimiter such as a greater-than,
     * less-than, or equals sign.
     * Quotes marks inside quoted strings may be escaped with a
     * backslash (\) character.
     * @param string string begin parsed
     * @param index location within string to start examining
     * @return next token, or null if whitespace was encountered
     */
    public String nextToken(Reader input, boolean skipWhitespaces) 
	throws IOException {
	StringBuffer token = new StringBuffer();

	if (skipWhitespaces) 
	    skipWhiteSpace (input);
      
	input.mark(1);
	int c = input.read();

	if (c == -1) { offset = -1; return null; }

	// quoted string? (handle both single and double)
	if (c == doubleQuote || c == singleQuote) {
	    boolean inSingle = false;
	    boolean inDouble = false;
	    if (c == singleQuote) inSingle = true; else inDouble = true;
	    token.append ((char) c);
	    do {
		c = input.read();
		if (c == -1) {
		    offset = -1;
		    String reportString = token.toString();
		    if (reportString.length() > 30) {
			reportString = reportString.substring(0, 30) + 
			    " (truncated, length is " + reportString.length() + ")";
		    }
		    throw new IOException ("EOF in String: " +  reportString);
		}
		if (c == '\\') {
		    int quoted = input.read();
		    if (quoted >= 0) token.append ((char) quoted);
		}
		else 
		    token.append((char) c);
	    } while ((inDouble && c != doubleQuote) || (inSingle && c != singleQuote));
	} 

	// parameter delimiter? read just one
	else if (isDelimiter((char) c)) { 
	    token.append((char) c);
	}

	// Inserted for token "-->".
	// Like a word token, but includes the delimiter ">".
	else if (c == '-') {
	    do { 
		token.append((char) c); 
		input.mark(1);
		c = input.read(); 
	    } 
	    while (c >= 0 && 
		   !Character.isWhitespace((char) c) && 
		   !isDelimiter((char) c));
	    input.reset();
	    token.append((char) input.read());
	}

	// If we did not skip Whitespaces but actually got one
	// this token is empty.
	else if (!skipWhitespaces && 
		 Character.isWhitespace((char) c)) {
	    input.reset();
	    return null;
	}
	
	// word token or />
	else {
	    do { 
		token.append((char) c); 
		input.mark(1);
		c = input.read(); 
	    } 
	    while (c >= 0 && 
		   !Character.isWhitespace((char) c) && 
		   !isDelimiter((char) c));
	    if (token.length() == 1 && token.charAt(0) == '/')
		token.append((char)c);
	    else
		input.reset();
	}
	return token.toString();
    }

    /**
   * could be overwritten
   */
    public static int skipWhiteSpace(Reader r) 
	throws IOException {
	int c, len=0;
	do {
	    r.mark(1);
	    c = r.read();
	    len++;
	} 
	while (c >= 0 && Character.isWhitespace((char) c));
	r.reset();
	return len - 1;
    }

    /**
     * Decide whether character is SGML delimiter or equals.
     * @param c character in question
     * @deprecated this is an internal function an should not be used
     *             and may be private some time
     * @return true if character is an SGML delimiter
     */
    public static boolean isDelimiter(char c) {
        return c == '<' || c == '=' || c == '>';
    }

    /**
     * Render this tag as a string.
     * @return SGML tag as string, showing range and values
     */
    public String toString() {
      StringBuffer str = new StringBuffer();
      str.append("[SGMLTag ").append(name).append(": (").append(getOffset()).append(",---)");
        if (attrs != null && wellFormed) {
            Iterator iter = attributes(true);
            while (iter.hasNext()) {
                String key = (String)iter.next();
                str.append(" ").append(key).append("=\"").append(value(key, null)).append("\"");
            }
        } else {
            str.append(" *MALFORMED TAG*");
        }
        str.append(" ]");
        return str.toString();
    }
}


/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
