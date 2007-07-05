/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package wingset;

import org.wings.SBorderLayout;
import org.wings.SBoxLayout;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDimension;
import org.wings.SIcon;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.SURLIcon;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class WingsImage
        extends SPanel {
    private static final SIcon WINGS_IMAGE = new SURLIcon("../icons/wings-logo.png");

    public WingsImage() {
        setLayout(new SBoxLayout(SBoxLayout.VERTICAL));
        add(createExample());
        setPreferredSize(SDimension.FULLAREA);
        setVerticalAlignment(SConstants.CENTER_ALIGN);
    }

    public SComponent createExample() {
        SPanel p = new SPanel(new SBorderLayout());

        SLabel label = new SLabel(WINGS_IMAGE);
        label.setHorizontalAlignment(SConstants.CENTER);
        p.add(label, SBorderLayout.CENTER);

        label = new SLabel("Welcome to");
        label.setHorizontalAlignment(SConstants.CENTER);
        p.add(label, SBorderLayout.NORTH);

        label = new SLabel("Have fun!");
        label.setHorizontalAlignment(SConstants.CENTER);
        p.add(label, SBorderLayout.SOUTH);

        return p;
    }
}


