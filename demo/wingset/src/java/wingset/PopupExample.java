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

import org.wings.*;

/**
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class PopupExample extends WingSetPane {

    public SComponent createExample() {

        SPopupMenu menu = new SPopupMenu();
        menu.add(new SMenuItem("Cut"));
        menu.add(new SMenuItem("Copy"));
        menu.add(new SMenuItem("Paste"));

        SMenu subMenu = new SMenu("Help");
        subMenu.add(new SMenuItem("About"));
        subMenu.add(new SMenuItem("Topics"));
        menu.add(subMenu);
        
        SPopupMenu menu2 = new SPopupMenu();
        menu2.add(new SMenuItem("Open"));
        menu2.add(new SMenuItem("Save"));
        menu2.add(new SMenuItem("Close"));

        SLabel testLabel = new SLabel("This label has a context menu.");
        testLabel.setComponentPopupMenu(menu);
        SLabel testLabel2 = new SLabel("This label has the same context menu.");
        testLabel2.setComponentPopupMenu(menu);
        SLabel testLabel3 = new SLabel("This label has another context menu.");
        testLabel3.setComponentPopupMenu(menu2);

        SPanel all = new SPanel();
        all.add(testLabel);
        all.add(testLabel2);
        all.add(testLabel3);
        return all;
    }
}
