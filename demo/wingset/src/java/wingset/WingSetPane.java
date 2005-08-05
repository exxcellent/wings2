/*
 * $Id$
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
import org.wings.SAnchor;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SFlowDownLayout;
import org.wings.SLabel;
import org.wings.SPanel;
import org.wings.SResourceIcon;
import org.wings.SDimension;
import org.wings.event.SComponentAdapter;
import org.wings.event.SComponentEvent;

/**
 * A basic WingSet Pane, which implements some often needed functions.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
abstract public class WingSetPane extends SPanel implements SConstants {
    protected final static transient Log log = LogFactory.getLog(WingSetPane.class);
    private static final SResourceIcon SOURCE_LABEL_ICON = new SResourceIcon("org/wings/icons/File.gif");
    private boolean initialized = false;

    public WingSetPane() {
        setLayout(new SFlowDownLayout());
        // Stretch panel/layout to full available width of the tabbed pane
        setPreferredSize(SDimension.FULLWIDTH);

        SAnchor anchor = new SAnchor("../" + getClass().getName().substring(getClass().getName().indexOf('.') + 1) + ".java");
        anchor.setTarget("sourceWindow");
        anchor.add(new SLabel("view java source code", SOURCE_LABEL_ICON));
        add(anchor, "viewSource");

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
            SComponent exampleComponent = createExample();
            exampleComponent.setHorizontalAlignment(SConstants.CENTER);
            add(exampleComponent, 0); // content generated by example
            initialized = true;
        }
    }

    /**
     * Override this.
     */
    protected abstract SComponent createExample();
}
