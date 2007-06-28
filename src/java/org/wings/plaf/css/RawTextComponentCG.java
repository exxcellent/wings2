package org.wings.plaf.css;

import java.io.IOException;

import org.wings.SComponent;
import org.wings.SRawTextComponent;
import org.wings.io.Device;

public final class RawTextComponentCG extends AbstractComponentCG implements org.wings.plaf.RawTextComponentCG {

	private static final long serialVersionUID = 1L;

	public void writeInternal(Device device, SComponent component) throws IOException {
		SRawTextComponent _c = (SRawTextComponent) component;
        device.print("<span");
        Utils.optAttribute(device, "id", _c.getName());
        device.print(">");
        device.print(_c.getText());
        device.print("</span>");
	}
}
