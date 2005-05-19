package org.wings.plaf.css.msie;

import java.io.IOException;

import org.wings.RequestURL;
import org.wings.io.Device;

public class ListCG extends org.wings.plaf.css.ListCG {

    /* (non-Javadoc)
     * @see org.wings.plaf.css.ListCG#writeLinkStart(org.wings.io.Device, org.wings.RequestURL)
     */
    protected void writeLinkStart(Device device, RequestURL selectionAddr) throws IOException {
        device.print("<a onclick=\"javascript:location.href='");
        selectionAddr.write(device);
        device.print("';\"");
    }
}
