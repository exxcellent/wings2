/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */

package wingset;

import org.wings.*;

/**
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
public class PopupExample extends WingSetPane
{

    public SComponent createExample() {
        SLabel testLabel = new SLabel("This label has a context menu.");

        SPopupMenu menu = new SPopupMenu();
        menu.add(new SMenuItem("blub"));
        testLabel.setComponentPopupMenu(menu);

        SPanel all = new SPanel();
        all.add(testLabel);
        return all;
    }
}
