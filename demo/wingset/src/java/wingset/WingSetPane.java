/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package wingset;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.*;
import org.wings.plaf.css.Utils;
import org.wings.border.*;
import org.wings.event.SComponentAdapter;
import org.wings.event.SComponentEvent;

import java.awt.*;

/**
 * A basic WingSet Pane, which implements some often needed functions.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
abstract public class WingSetPane
        extends SForm
        implements SConstants
{
    protected final static transient Log log = LogFactory.getLog(WingSetPane.class);
    private static final SResourceIcon SOURCE_LABEL_ICON = new SResourceIcon("org/wings/icons/source_java.png");
    private boolean initialized = false;

    public WingSetPane() {
        setLayout(new SBorderLayout());
        setPreferredSize(SDimension.FULLAREA);

        SAnchor anchor = new SAnchor("../" + getClass().getName().substring(getClass().getName().indexOf('.') + 1) + ".java");
        anchor.setTarget("sourceWindow");
        anchor.add(new SLabel("View Java Source Code", SOURCE_LABEL_ICON));
        anchor.setPreferredSize(SDimension.FULLWIDTH);

        SPanel south = new SPanel();
        if (!Utils.isMSIE(this))
            south.setBorder(new SEmptyBorder(0, 1, 0, 1));
        south.setPreferredSize(SDimension.FULLWIDTH);
        south.add(anchor);

        SBorder border = new SLineBorder(1, new Insets(0, 3, 0, 6));
        border.setColor(new Color(255, 255, 255), SConstants.TOP);
        border.setColor(new Color(255, 255, 255), SConstants.LEFT);
        border.setColor(new Color(190, 190, 190), SConstants.RIGHT);
        border.setColor(new Color(190, 190, 190), SConstants.BOTTOM);
        south.setBorder(border);
        south.setBackground(new Color(240,240,240));

        add(south, SBorderLayout.SOUTH);

        // lazily initialize components when first shown
        addComponentListener(new SComponentAdapter() {
            public void componentShown(SComponentEvent e) {
                initializePanel();
            }

            /* Debug method to test memory leak with invisible low level event listerner (i.e. back button example)
             public void componentHidden(SComponentEvent e) {
                initialized = false;
                if (getComponentCount() > 1)
                    remove(0);
            }*/
        });
    }

    protected void initializePanel() {
        if (!initialized) {
            SComponent controlsComponent = createControls();
            if (controlsComponent != null) {
                if (!Utils.isMSIE(this) && controlsComponent instanceof SContainer)
                    controlsComponent.setBorder(new SEmptyBorder(0, 1, 0, 1));
                controlsComponent.setVerticalAlignment(SConstants.TOP_ALIGN);
                if (controlsComponent.getHorizontalAlignment() == SConstants.NO_ALIGN)
                    controlsComponent.setHorizontalAlignment(SConstants.LEFT_ALIGN);
                add(controlsComponent, SBorderLayout.NORTH);
            }

            SComponent exampleComponent = createExample();
            if (exampleComponent != null) {
                if (!Utils.isMSIE(this))
                    exampleComponent.setBorder(new SEmptyBorder(0, 1, 1, 1));
                if (exampleComponent.getVerticalAlignment() == SConstants.NO_ALIGN)
                    exampleComponent.setVerticalAlignment(SConstants.TOP_ALIGN);
                if (exampleComponent.getHorizontalAlignment() == SConstants.NO_ALIGN)
                    exampleComponent.setHorizontalAlignment(SConstants.CENTER_ALIGN);
                add(exampleComponent, SBorderLayout.CENTER);
            }
            initialized = true;
        }
    }

    protected abstract SComponent createControls();
    protected abstract SComponent createExample();

    public String getExampleName() {
        String name = getClass().getName();
        name = name.substring(name.lastIndexOf('.') + 1);
        if (name.endsWith("Example"))
            name = name.substring(0, name.length() - "Example".length());
        else if (name.endsWith("Test"))
            name = name.substring(0, name.length() - "Test".length());
        else if (name.endsWith("Experiment"))
            name = name.substring(0, name.length() - "Experiment".length());
        return name;
    }


    public String toString() {
        return getExampleName();
    }
}
