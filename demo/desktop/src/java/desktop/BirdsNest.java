/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package desktop;

import org.wings.SDesktopPane;


/**
 * @author hengels
 */
public class BirdsNest
        extends SDesktopPane
{
    public void updateCG() {
        setCG(new BirdsNestCG());
    }
}
