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
import org.wings.session.SessionManager;
import org.wings.border.SEmptyBorder;
import org.wings.header.Link;
import org.wings.resource.DefaultURLResource;
import org.wings.style.CSSProperty;

import java.io.*;
import java.net.URL;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Arrays;

/**
 * The root of the WingSet demo application.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:B.Schmid@eXXcellent.de">Benjamin Schmid</a>
 */
public class WingSet implements Serializable {
    /**
     * Jakarta commons logger.
     */
    private final static Log log = LogFactory.getLog(WingSet.class);

    // Some different icons used inside the demo applciation
    private final static SIcon JAVA_CUP_ICON = new SResourceIcon("org/wings/icons/JavaCup.gif");
    private final static SIcon SMALL_COW_ICON = new SURLIcon("../icons/cowSmall.gif");
    private final static SURLIcon STANDARD_TAB_BACKGROUND = new SURLIcon("../icons/ButtonsBackground.gif");
    private final static SURLIcon SELECTED_TAB_BACKGROUND = new SURLIcon("../icons/ButtonsBackgroundHighlighted.gif");

    /**
     * If true then use {@link StatisticsTimerTask} to log statistics on a regular basis to a logging file.
     * (Typically a file named wings-statisticsxxxlog placed in jakarta-tomcat/temp directory)
     */
    private static final boolean LOG_STATISTICS_TO_FILE = true;

    /**
     * Optional external custom CSS stylesheet to style your application according to your needs.
     */
    private Link customStyleSheetLink;

    static {
        if (LOG_STATISTICS_TO_FILE) {
            StatisticsTimerTask.startStatisticsLogging(60);
        }
    }

    /**
     * The root frame of the WingSet application.
     */
    private final SFrame frame;

    private final STabbedPane tab;

    private boolean customStyleApplied;

    /**
     * Constructor of the wingS application.
     * <p/>
     * <p>This class is referenced in the <code>web.xml</code> as root entry point for the wingS application.
     * For every new client an new {@link org.wings.session.Session} is created which constructs a new instance of this class.
     */
    public WingSet() {
        // Create root frame
        frame = new SFrame("WingSet Demo");

        // Create the tabbed pane containing all the wingset example tabs
        tab = new STabbedPane(SConstants.TOP);
        tab.setName("examples");
        tab.setPreferredSize(new SDimension("100%", "580px"));

        // do some global styling of the wingSet application
        styleWingsetApp();

        tab.add(new WingsImage(), "wingS!");

        String dirName = SessionManager.getSession().getServletContext().getRealPath("/WEB-INF/classes/wingset");
        System.out.println("dirName = " + dirName);
        File dir = new File(dirName);
        String[] exampleClassFileNames = dir.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith("Example.class");
            }
        });
        Arrays.sort(exampleClassFileNames);
        System.out.println("exampleClassFileNames = " + Arrays.asList(exampleClassFileNames));
        for (int i = 0; i < exampleClassFileNames.length; i++) {
            String exampleClassFileName = exampleClassFileNames[i];
            String exampleClassName = "wingset." + exampleClassFileName.substring(0, exampleClassFileName.length() - ".class".length());
            try {
                Class exampleClass = Thread.currentThread().getContextClassLoader().loadClass(exampleClassName);
                addExample(exampleClass);
            }
            catch (Throwable e) {
                System.err.println("Could not load example: " + exampleClassName);
                e.printStackTrace();
            }
        }

        // Add component to content pane using a layout constraint (
        frame.getContentPane().add(tab);

        SButton switchStyleButton = new SButton("Toggle WingSet styling");
        switchStyleButton.setShowAsFormComponent(false);
        switchStyleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (customStyleApplied)
                    unstyleWingsetApp();
                else
                    styleWingsetApp();
            }
        });
        switchStyleButton.setBorder(new SEmptyBorder(5, 0, 5, 0));
        frame.getContentPane().add(switchStyleButton, SBorderLayout.SOUTH);
        frame.getContentPane().setPreferredSize(SDimension.FULLAREA);

        frame.show();
    }

    private void addExample(Class exampleClass) throws IllegalAccessException, InstantiationException {
        WingSetPane example = (WingSetPane)exampleClass.newInstance();
        tab.add(example, example.getExampleName());
    }

    /**
     * This method demonstrates some mehtods to influence the style of an wingS application
     */
    private void styleWingsetApp() {
        // 1) Apply custom HTML layout template as template for the root frame of the application
        try {
            URL templateURL = frame.getSession().getServletContext().getResource("/templates/ExampleFrame.thtml");
            if (templateURL != null) {
                SRootLayout layout = new SRootLayout(templateURL);
                frame.setLayout(layout);
            }
        } catch (java.io.IOException except) {
            log.warn("Exception", except);
        }

        // 2) Include an application specific CSS stylesheet to extend/overwrite the default wingS style set.
        customStyleSheetLink = new Link("stylesheet", null, "text/css", null, new DefaultURLResource("../css/wingset.css"));
        frame.addHeader(customStyleSheetLink);

        // 3) More advanced version of 3)
        //    Here we demonstrate how you can style single component by applying a CSS properties on a
        //    part of a component using component specific "css selectors" or "pseudo css selectors".
        //
        //    As we want to set styles on specific areas of the componentn and not the component itself, we use
        //    "CSS selectors" provided by the componentn to address these areas. They are dynamically resolved
        //     to the according CSS statements during the rednering process.
        //
        //  Set a background image on selected and unselected tabs (gradient grey image or orange image):
        tab.setAttribute(STabbedPane.SELECTOR_UNSELECTED_TAB, CSSProperty.BACKGROUND_IMAGE, STANDARD_TAB_BACKGROUND);
        tab.setAttribute(STabbedPane.SELECTOR_SELECTED_TAB, CSSProperty.BACKGROUND_IMAGE, SELECTED_TAB_BACKGROUND);

        customStyleApplied = true;
    }

    /**
     * Revert styling done in {@link #styleWingsetApp()}
     */
    private void unstyleWingsetApp() {
        frame.setLayout(new SRootLayout());
        frame.removeHeader(customStyleSheetLink);
        tab.setAttribute(STabbedPane.SELECTOR_UNSELECTED_TAB, CSSProperty.BACKGROUND_IMAGE, (SIcon) null);
        tab.setAttribute(STabbedPane.SELECTOR_SELECTED_TAB, CSSProperty.BACKGROUND_IMAGE, (SIcon) null);
        customStyleApplied = false;
    }
}
