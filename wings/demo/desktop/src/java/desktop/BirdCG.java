/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package desktop;

import org.wings.SComponent;
import org.wings.io.Device;
import org.wings.plaf.css.AbstractComponentCG;
import org.wings.plaf.css.Utils;

import java.io.IOException;

/**
 * @author hengels
 */
public class BirdCG
        extends AbstractComponentCG {
    protected static final String WINDOWICON_CLASSNAME = "WindowIcon";
    protected static final String BUTTONICON_CLASSNAME = "WindowButton";

    public BirdCG() {
    }

    public void installCG(SComponent component) {
        component.setStyle("Bird");
    }

    public void writeInternal(final Device device, final SComponent _c)
            throws IOException {
        Bird frame = (Bird) _c;
        SComponent titleBar = frame.getTitleBar();
        titleBar.getCG().write(device, titleBar);

        // write the actual content
        if (!frame.isIconified()) {
            device.print("<div class=\"WindowContent\">");
            Utils.renderContainer(device, frame);
            device.print("</div>");
        }
    }
}
