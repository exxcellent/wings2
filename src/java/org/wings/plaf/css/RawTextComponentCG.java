package org.wings.plaf.css;

import org.wings.SComponent;
import org.wings.SRawTextComponent;
import org.wings.io.Device;

import java.io.IOException;

public class RawTextComponentCG extends AbstractComponentCG {
    private static final long serialVersionUID = 1L;

    public void writeInternal(final Device device, final SComponent component)
            throws IOException {
        SRawTextComponent _c = (SRawTextComponent) component;
        device.print(_c.getText());
    }
}
