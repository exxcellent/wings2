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
package org.wings.util;

import java.util.StringTokenizer;

/**
 * Some string manipulation utilities.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class StringUtil {
    /**
     * replaces substrings with content 'toFind' with 'replace' in
     * s and returns the result ('s/$toFind/$replace/g')
     *
     * @param s       The String the substrings should be replaced in.
     * @param toFind  The substring to be replaced
     * @param replace The replacement.
     * @return the string with all replacements.
     */
    public static final String replace(String s,
                                       String toFind, String replace) {
        SStringBuilder erg = new SStringBuilder();

        int lastindex = 0;
        int indexOf = s.indexOf(toFind);
        if (indexOf == -1) return s;
        while (indexOf != -1) {
            erg.append(s.substring(lastindex, indexOf)).append(replace);
            lastindex = indexOf + toFind.length();
            indexOf = s.indexOf(toFind, lastindex);
        }

        erg.append(s.substring(lastindex));

        return erg.toString();
    }

    /* slower ..
      ist langsamer, mit jit 1/3, weniger als halb so schnell
     public static String replaceNew(String s, String toFind, String replace) {
     SStringBuilder erg = new SStringBuilder();

     StringTokenizer t = new StringTokenizer(s, toFind);
     while ( t.hasMoreTokens() ) {
     erg.append(t.nextToken().trim()).append(replace);
     }
     return erg.toString();
     }
     */

    /**
     * replaces all newlines in the given String 's' with the replacement
     * string 'r'. Each line is trimmed from leading and trailing whitespaces,
     * then the new line-delimiter is added.
     *
     * @param s the source string.
     * @param r the new line delimiter
     * @return the resulting string.
     */
    public static final String replaceNewLines(String s, String r) {
        SStringBuilder result = new SStringBuilder();

        StringTokenizer t = new StringTokenizer(s, "\n");
        while (t.hasMoreTokens()) {
            result.append(t.nextToken().trim()).append(r);
        }
        return result.toString();
    }


    /**
     * concatenates two arrays of strings.
     *
     * @param s1 the first array of strings.
     * @param s2 the second array of strings.
     * @return the resulting array with all strings in s1 and s2
     */
    public static final String[] concat(String[] s1, String[] s2) {
        String[] erg = new String[s1.length + s2.length];

        System.arraycopy(s1, 0, erg, 0, s1.length);
        System.arraycopy(s2, 0, erg, s1.length, s2.length);

        return erg;
    }

    private final static char[] ALPHAS = {
        'a', 'b',
        'c', 'd', 'e', 'f', 'g', 'h',
        'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't',
        'u', 'v', 'w', 'x', 'y', 'z',
    };

    /**
     * All possible digits for representing a number as a String
     * This is conservative and does not include 'special'
     * characters since some browsers don't handle them right.
     * The IE for instance seems to be case insensitive in class
     * names for CSSs. Grrr.
     */
    private final static char[] DIGITS = {
        '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'a', 'b',
        'c', 'd', 'e', 'f', 'g', 'h',
        'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't',
        'u', 'v', 'w', 'x', 'y', 'z',
        /* This %@&!-IE is case insensitive for certain
         * URLs and IDs
         * 'A' , 'B' ,
         * 'C' , 'D' , 'E' , 'F' , 'G' , 'H' ,
         * 'I' , 'J' , 'K' , 'L' , 'M' , 'N' ,
         * 'O' , 'P' , 'Q' , 'R' , 'S' , 'T' ,
         * 'U' , 'V' , 'W' , 'X' , 'Y' , 'Z'
         */
    };

    public static final int MAX_RADIX = DIGITS.length;

    /**
     * Codes number up to radix 62.
     * Note, this method is only public for backward compatiblity. don't
     * use it.
     *
     * @param minDigits returns a string with a least minDigits digits
     */
    public static String toString(long i, int radix, int minDigits) {
        char[] buf = new char[65];

        radix = Math.min(Math.abs(radix), MAX_RADIX);
        minDigits = Math.min(buf.length - 1, Math.abs(minDigits));


        int charPos = buf.length - 1;

        boolean negative = (i < 0);
        if (negative) {
            i = -i;
        }

        while (i >= radix) {
            buf[charPos--] = DIGITS[(int) (i % radix)];
            i /= radix;
        }
        buf[charPos] = DIGITS[(int) i];

        // if minimum length of the result string is set, pad it with the
        // zero-representation (that is: '0')
        while (charPos > buf.length - minDigits)
            buf[--charPos] = DIGITS[0];

        if (negative) {
            buf[--charPos] = '-';
        }

        return new String(buf, charPos, buf.length - charPos);
    }

    /**
     * creates a shortest possible string representation of the given
     * long number that qualifies as an identifier in common programming
     * languages (and HTML-id's :-)
     * That is, it must start with a letter.
     *
     * @param val the long value to be encoded
     * @return a string represantation of the given value that qualifies
     *         as an identifier.
     */
    public static String toIdentifierString(long val) {
        char buf[] = new char[14];
        int i = 0;
        if (val < 0) {
            buf[i++] = '_';
            val = -(val + 1);
        }
        buf[i++] = ALPHAS[(int) (val % ALPHAS.length)];
        val /= ALPHAS.length;
        while (val != 0 && i < buf.length) {
            buf[i++] = DIGITS[(int) (val % DIGITS.length)];
            val /= DIGITS.length;
        }
        return new String(buf, 0, i);
    }

    /**
     * generates a shortest representation as string for the given long.
     * This is used to generate session or resource IDs.
     */
    public static String toShortestAlphaNumericString(long i) {
        return toString(i, MAX_RADIX, 0);
    }

    /**
     * generates a shortest representation as string for the given with
     * at least minDigits digits. Unused digits are padded with zero.
     */
    public static String toShortestAlphaNumericString(long i, int minDigits) {
        return toString(i, MAX_RADIX, minDigits);
    }

    public static String delimitedString(Object[] array) {
        if (array == null)
            return null;
        if (array.length == 0)
            return "";

        SStringBuilder buffer = new SStringBuilder("" + array[0]);
        for (int i = 1; i < array.length; i++) {
            if (array[i] == null)
                buffer.append(", null");
            else {
                buffer.append(", ");
                buffer.append(array[i]);
            }
        }
        return buffer.toString();
    }

    /*
    public static void main(String args[]) {
        System.out.println(StringUtil.toString(9124, 10, 0));

        System.out.println(StringUtil.toShortestAlphaNumericString(9124));
        System.out.println(StringUtil.toShortestAlphaNumericString(-9124));

        System.out.println(StringUtil.toString(1, MAX_RADIX, 4));

        System.out.println(StringUtil.toString(9124, MAX_RADIX, 1));
        System.out.println(StringUtil.toString(9124, MAX_RADIX, 4));
        System.out.println(StringUtil.toString(9124, MAX_RADIX, 5));

        System.out.println(StringUtil.toString(-1, MAX_RADIX, 64));

        System.out.println("\"" + StringUtil.toShortestAlphaNumericString(3843,
                                                                          2));
        for (int i=-10; i < 300; ++i) {
            System.out.println(toIdentifierString(i));
        }
        System.out.println(toIdentifierString(Long.MIN_VALUE));
        System.out.println(toIdentifierString(Long.MAX_VALUE));
    }
    */
}





