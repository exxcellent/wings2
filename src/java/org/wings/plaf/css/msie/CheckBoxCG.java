package org.wings.plaf.css.msie;

import java.io.IOException;

import org.wings.RequestURL;
import org.wings.SAbstractButton;
import org.wings.SComponent;
import org.wings.SIcon;
import org.wings.io.Device;
import org.wings.plaf.css.IconTextCompound;
import org.wings.plaf.css.Utils;

public class CheckBoxCG extends org.wings.plaf.css.CheckBoxCG {
    /* (non-Javadoc)
     * @see org.wings.plaf.css.CheckBoxCG#writeLinkStart(org.wings.io.Device, org.wings.RequestURL)
     */
    protected void writeLinkStart(final Device device, RequestURL addr) throws IOException {
        device.print("<a onclick=\"javascript:location.href='");
        addr.write(device);
        device.print("';\"");
    }
}
