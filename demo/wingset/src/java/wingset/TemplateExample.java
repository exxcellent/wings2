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
import org.wings.template.propertymanagers.DefaultPropertyManager;

import javax.swing.tree.DefaultTreeModel;

/**
 * @author <a href="mailto:hzeller@to.com">Henner Zeller</a>
 * @version $Revision$
 */
public class TemplateExample
        extends WingSetPane
        implements SConstants {

    protected SComponent createExample() {
        SForm c = new SForm();

        try {
            java.net.URL templateURL =
                    getSession().getServletContext().getResource("/templates/TemplateExample.thtml");
            if (templateURL == null) {
                c.add(new SLabel("Sorry, can't find TemplateExample.thtml. Are you using a JAR-File?"));
                return c;
            }
            // you can of course directly give files here.
            STemplateLayout layout = new STemplateLayout(templateURL);
            c.setLayout(layout);
        } catch (java.io.IOException except) {
            except.printStackTrace();
        }
        
        //c.add(new STextArea(), "DemoArea");
        c.add(new SLabel("BeanScript support not enabled. Define value 'true' for " +
                "property "+ DefaultPropertyManager.BEANSCRIPT_ENABLE+" in web.xml " +
                "to enable BeanScript support!"), "theLabel");
        c.add(new SButton("Press Me"), "TESTBUTTON");
        c.add(new STextField(), "NAME");
        c.add(new STextField(), "FIRSTNAME");
        SButtonGroup group = new SButtonGroup();
        for (int i = 0; i < 3; i++) {
            SRadioButton b = new SRadioButton("Radio " + (i + 1));
            group.add(b);
            c.add(b, "SELVAL=" + i);
        }

        STree tree = new STree(new DefaultTreeModel(HugeTreeModel.ROOT_NODE));
        c.add(tree, "TREE");
        return c;
    }
}


