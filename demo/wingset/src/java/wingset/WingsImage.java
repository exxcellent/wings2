/*
 * $Id$
 * Copyright 2000,2005 j-wingS development team.
 *
 * This file is part of j-wingS (http://www.j-wings.org).
 *
 * j-wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package wingset;

import org.wings.*;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class WingsImage
        extends SPanel {
    private static final SIcon WINGS_IMAGE = new SURLIcon("../icons/j-wings-logo.png");

    public WingsImage() {
        add(createExample());
    }

    public SComponent createExample() {
        SPanel p = new SPanel();
        final SBorderLayout layout = new SBorderLayout();

        p.setLayout(layout);
        p.setPreferredSize(new SDimension("100%", null));

        SLabel label = new SLabel(WINGS_IMAGE);
        label.setHorizontalAlignment(CENTER);
        p.add(label, SBorderLayout.CENTER);

        label = new SLabel("Welcome to");
        label.setHorizontalAlignment(CENTER);
        p.add(label, SBorderLayout.NORTH);


        label = new SLabel("Have fun!");
        label.setHorizontalAlignment(CENTER);
        p.add(label, SBorderLayout.SOUTH);

        return p;
    }
}


