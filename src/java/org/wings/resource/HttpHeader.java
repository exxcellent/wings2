// (c) copyright 2006 by eXXcellent solutions, Ulm. Author: bschmid

package org.wings.resource;

import java.util.Map;

/**
 * A HTTP header entry which will be delivered in the header of the low-level HTTP response. 
 *
 * @author Benjamin Schmid <B.Schmid@exxcellent.de>
 */
public interface HttpHeader extends Map.Entry<String, Object>{

    /**
     * @return The HTTP head parameter name
     */
    String getKey();

    /**
     * @return The HTTP paramter value. This may be a <code>String</code>, <code>Integer</code> or <code>Date</code>.
     */
    Object getValue();

    /**
     * @param value The HTTP paramter value. This may be a <code>String</code>, <code>Integer</code> or <code>Date</code>.
     * @return The previous value.
     */
    Object setValue(Object value);
}
