/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.wings.plaf.css;


import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SContainer;
import org.wings.io.Device;
import org.wings.plaf.AbstractComponentCG;

public class ContainerCG
    extends AbstractComponentCG
    implements SConstants, org.wings.plaf.PanelCG
{
    public void writeContent(final Device device, final SComponent component)
        throws java.io.IOException
    {
        Utils.renderContainer(device, (SContainer)component);
    }
}
