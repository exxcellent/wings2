package org.wings.plaf.css;

import org.wings.SComponent;
import org.wings.SRawTextComponent;
import org.wings.io.Device;

import java.io.IOException;

public final class RawTextComponentCG extends AbstractComponentCG {
    private static final long serialVersionUID = 1L;

    public void write(Device device, SComponent component) throws IOException {
        component.fireRenderEvent(SComponent.START_RENDERING);
        SRawTextComponent _c = (SRawTextComponent) component;
        device.print(_c.getText());
        component.fireRenderEvent(SComponent.DONE_RENDERING);

    }

    public void writeInternal(Device device, SComponent component) throws IOException {
        // must overwrite write to ommit comment!!
    }
}
