/*
 * Created on 02.03.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.wings.plaf;

import java.io.IOException;
import java.io.Serializable;

import org.wings.SComponent;
import org.wings.io.Device;

/**
 * @author ole
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface PrefixAndSuffixDelegate extends Serializable {
    public void writePrefix(Device device, SComponent component) throws IOException;
    public void writeSuffix(Device device, SComponent component) throws IOException;
}