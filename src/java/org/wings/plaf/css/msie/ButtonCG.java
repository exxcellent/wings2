package org.wings.plaf.css.msie;

import java.io.IOException;

import org.wings.RequestURL;
import org.wings.io.Device;

public class ButtonCG extends org.wings.plaf.css.ButtonCG {

    protected void writeLinkStart(Device device, RequestURL addr) throws IOException {
        device.print("<a onclick=\"javascript:location.href='");
        addr.write(device);
        device.print("';\"");
    }

}
