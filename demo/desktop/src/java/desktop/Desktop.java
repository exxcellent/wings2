/*
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of the wingS demo (http://j-wings.org).
 *
 * The wingS demo is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package desktop;

import org.wings.*;
import org.wings.border.SEmptyBorder;
import org.wings.event.SContainerEvent;
import org.wings.event.SContainerListener;
import org.wings.header.Link;
import org.wings.resource.DefaultURLResource;
import org.wings.style.CSSProperty;
import org.wings.util.ComponentVisitor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @author Holger Engels
 */
public class Desktop
    implements SConstants
{
    static {
        System.setProperty("java.util.prefs.PreferencesFactory", CustomPreferencesFactory.class.getName());
    }

    SFrame frame;
    Map<String, DesktopPane> panes = new HashMap<String, DesktopPane>();
    SMenu windowMenu;
    SMenuBar menuBar;
    int editorNumber = 0;
    int xPos = 0;
    Preferences prefRoot;

    public Desktop() {

        frame = new SFrame("Desktop");
        frame.setAttribute(CSSProperty.MARGIN, "4px");
        frame.setVisible(true);
        prefRoot = Preferences.userRoot();
        try{
            if(prefRoot.nodeExists("desktoppanes"))
                Desktop.this.loadExisting(frame.getContentPane());
            else
                Desktop.this.createNew(frame.getContentPane());
        }catch(BackingStoreException ex){
            System.out.println("BackingStoreException. Falling back to default settings");
            Desktop.this.createNew(frame.getContentPane());
        }

        frame.addHeader(new Link("stylesheet", null, "text/css", null, new DefaultURLResource("../desktop.css")));
        flushPreferences();
        frame.show();
    }

    private void createNew(SContainer contentPane){
        
        //first clear all old stuf..
        try{
            for(String name: Preferences.userRoot().childrenNames()){
                Preferences.userRoot().node(name).removeNode();
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        contentPane.removeAll();
        EditorPanel.resetEditorNo();

        menuBar = createMenu();

        final DesktopPane desktop = new DesktopPane();

        // add the frames to the window-menu ..
        desktop.addContainerListener(new DesktopFrameListener(desktop));

        final DesktopPane feeds = new DesktopPane();

        feeds.addContainerListener(new DesktopFrameListener(feeds));

        contentPane.setLayout(new SGridBagLayout());

        GridBagConstraints c1 = new GridBagConstraints();
        GridBagConstraints c2 = new GridBagConstraints();
        GridBagConstraints c3 = new GridBagConstraints();
        c1.gridwidth = GridBagConstraints.REMAINDER;
        c1.weightx = 1.0;
        contentPane.add(menuBar, c1);
        c2.gridx = xPos;
        xPos++;
        c2.gridy = 1;
        c2.weightx = 0.7;
        contentPane.add(desktop, c2);
        panes.put(desktop.getName(), desktop);
        prefRoot.node("desktoppanes").node(desktop.getName()).putDouble(DesktopPane.WEIGHTX, c2.weightx);

        c3.gridx = xPos;
        xPos++;
        c3.gridy = 1;
        c3.weightx = 0.3;
        contentPane.add(feeds, c3);
        prefRoot.node("desktoppanes").node(feeds.getName()).putDouble(DesktopPane.WEIGHTX, c3.weightx);
        panes.put(feeds.getName(), feeds);

        DesktopItem ed = new EditorItem();
        ed.putValue(EditorItem.TEXT, getStory());
        desktop.addDesktopItem(ed);

        DesktopItem feed = new NewsFeedItem();
        feed.putValue(DesktopItem.NAME, "heise news ticker");
        feed.putValue(NewsFeedItem.FEED, "http://www.heise.de/english/newsticker/news.rdf");
        feeds.addDesktopItem(feed);

        DesktopItem feed2 = new NewsFeedItem();
        feed2.putValue(DesktopItem.NAME, "the server side");
        feed2.putValue(NewsFeedItem.FEED, "http://www.theserverside.com/rss/theserverside-rss2.xml");
        feeds.addDesktopItem(feed2);
    }

    private void loadExisting(SContainer contentPane) {
        contentPane.setLayout(new SGridBagLayout());
        menuBar = createMenu();
        GridBagConstraints c1 = new GridBagConstraints();
        c1.gridwidth = GridBagConstraints.REMAINDER;
        c1.weightx = 1.0;
        c1.gridy = 0;
        contentPane.add(menuBar, c1);

        int firstFreePane = 0;
        int firstFreeItem = 0;

        try {
            for(String paneName:  prefRoot.node("desktoppanes").childrenNames()){
                Preferences paneNode =  prefRoot.node("desktoppanes").node(paneName);
                DesktopPane p = new DesktopPane(paneName);
                p.addContainerListener(new DesktopFrameListener(p));
                firstFreePane = Math.max(firstFreePane, paneNode.getInt(DesktopPane.FIRST_FREE_INDEX, 0));
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = xPos;
                c.gridy = 1;
                xPos++;
                c.weightx = paneNode.getDouble(DesktopPane.WEIGHTX, 0.2);
                contentPane.add(p, c);
                panes.put(p.getName(), p);
                List<DesktopItem> items = new ArrayList<DesktopItem>();

                for(String itemName: prefRoot.node("desktopitems").childrenNames()){

                    Preferences itemNode = prefRoot.node("desktopitems").node(itemName);

                    if (!paneName.equals(itemNode.get(DesktopItem.DESKTOPPANE, "nix")))
                        continue;


                    firstFreeItem = Math.max(firstFreeItem, itemNode.getInt(DesktopItem.FIRST_FREE_INDEX, 0));
                    DesktopItem item = ToolRegistry.getToolRegistry().getRegisteredTools().get(itemNode.get(DesktopItem.TOOL, "EditorTool")).getExistingItem(itemName);


                    String[] keys = itemNode.keys();

                    for (String key : keys) {


                        if (!key.equals(DesktopItem.DESKTOPPANE) && !key.equals(DesktopItem.FIRST_FREE_INDEX) && !key.equals(DesktopItem.TOOL)) {
                            if (key.equals(DesktopItem.KEY))
                                item.putValue(key, itemNode.get(key, ""));
                            else if (key.equals(DesktopItem.POSITION_ON_PANE))
                                item.putValue(key, itemNode.getInt(key, -100));
                            else if (key.equals(DesktopItem.TEXT))
                                item.putValue(key, itemNode.get(key, ""));
                            else if (isIntValue(itemNode, key))
                                item.putValue(key, (Integer)itemNode.getInt(key, Integer.MIN_VALUE));
                            else if (isBooleanValue(itemNode, key))
                                item.putValue(key, itemNode.getBoolean(key, true));
                            else if (isFloatValue(itemNode, key))
                                item.putValue(key, itemNode.getFloat(key, Float.NaN));
                            else if (isDoubleValue(itemNode, key))
                                item.putValue(key, Double.NaN);
                            else if (isLongValue(itemNode, key))
                                item.putValue(key, itemNode.getLong(key, Long.MIN_VALUE));
                            else {
                                item.putValue(key, itemNode.get(key, ""));
                            }

                        }
                    }

                    boolean putToEnd = true;

                    for (int i = 0; i < items.size(); i++) {
                        int itemValue = ((Integer)item.getValue(DesktopItem.POSITION_ON_PANE)).intValue();
                        int listValue = ((Integer)items.get(i).getValue(DesktopItem.POSITION_ON_PANE)).intValue();

                        if (itemValue < listValue) {
                            items.add(i, item);
                            putToEnd = false;
                            break;
                        }
                    }

                    if (putToEnd)
                        items.add(item);
                }
                for (DesktopItem item : items) {
                    p.addExistingDesktopItem(item);
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        DesktopPane.paneNo.set(firstFreePane);
        AbstractDesktopItem.itemNo.set(firstFreeItem);
    }

    private boolean isBooleanValue(Preferences node, String key) {
        boolean b1 = node.getBoolean(DesktopItem.ICON, true);
        boolean b2 = node.getBoolean(DesktopItem.ICON, false);
        return b1 == b2;
    }

    private boolean isFloatValue(Preferences node, String key) {
        Float f = node.getFloat(key, Float.NaN);
        return !f.equals(Float.NaN);
    }

    private boolean isDoubleValue(Preferences node, String key) {
        Double d = node.getDouble(key, Double.NaN);
        return !d.equals(Double.NaN);
    }

    private boolean isLongValue(Preferences node, String key) {
        Long l = node.getLong(key, Long.MIN_VALUE);
        return !l.equals(Long.MIN_VALUE);
    }

    private boolean isIntValue(Preferences node, String key) {
        int i = node.getInt(key, -3245);
        return i != -3245;
    }

    private boolean isByteArrayValue(Preferences node, String key) {
        byte[] ba = node.getByteArray(key, new byte[0]);

        return ba.length != 0;
    }

    protected String getStory() {
        return "Ein Philosoph ist jemand, der in einem absolut dunklen Raum " +
            "mit verbundenen Augen nach einer schwarzen Katze sucht, die gar nicht " +
            "da ist. Ein Theologe ist jemand der genau das gleiche macht und ruft: " +
            "\"ich hab sie!\"";
    }

    protected SMenuBar createMenu() {

        SMenu fileMenu = new SMenu("File");
        SMenuItem fileMenuItem;

        for (final DesktopTool tool : ToolRegistry.getToolRegistry().getRegisteredTools().values()) {
            fileMenuItem = new SMenuItem(tool.getText());
            fileMenuItem.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent evt) {
                    DesktopPane paneToAddTo = null;
                    for (DesktopPane pane : panes.values()) {
                        if (pane.getParentFrame() != null) {
                            paneToAddTo = pane;
                            break;
                        }
                    }
                    if (paneToAddTo == null)
                        paneToAddTo = new DesktopPane();

                    paneToAddTo.addDesktopItem(tool.getItem());
                }
            });
            fileMenu.add(fileMenuItem);
        }

        windowMenu = new SMenu("Window");

        SMenuItem newPaneItem = new SMenuItem("new Column");
        newPaneItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt) {
                SContainer contentPane = frame.getContentPane();
                int paneCount = contentPane.getComponentCount() - 1;

                int ct = 0;
                if (contentPane.getComponentCount() >= 3) {
                    for (int i = 0; i < contentPane.getComponentCount(); i++) {
                        if (ct > 1)
                            break;

                        if (contentPane.getComponent(i) instanceof DesktopPane) {
                            if (((GridBagConstraints)contentPane.getConstraintAt(i)).weightx >= 0.2) {
                                ((GridBagConstraints)contentPane.getConstraintAt(i)).weightx -= 0.1;
                                prefRoot.node("desktoppanes").node(contentPane.getComponent(i).getName()).putDouble(DesktopPane.WEIGHTX, ((GridBagConstraints)contentPane.getConstraintAt(i)).weightx);
                                ct++;
                            }

                        }
                    }
                }
                else {
                    for (int i = 0; i < contentPane.getComponentCount(); i++) {
                        if (contentPane.getComponent(i) instanceof DesktopPane) {
                            ((GridBagConstraints)contentPane.getConstraintAt(i)).weightx -= 0.2;
                            prefRoot.node("desktoppanes").node(contentPane.getComponent(i).getName()).putDouble(DesktopPane.WEIGHTX, ((GridBagConstraints)contentPane.getConstraintAt(i)).weightx);
                            break;
                        }
                    }
                }


                final DesktopPane newPane = new DesktopPane();
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = xPos;
                xPos++;
                c.gridy = 1;
                c.weightx = 0.2;

                newPane.addContainerListener(new DesktopFrameListener(newPane));

                panes.put(newPane.getName(), newPane);
                contentPane.add(newPane, c);
                prefRoot.node("desktoppanes").node(newPane.getName()).putDouble(DesktopPane.WEIGHTX, c.weightx);
                flushPreferences();

            }
        });

        SMenuItem editPanesItem = new SMenuItem("edit Columns");
        editPanesItem.addActionListener(new ActionListener()
        {

            class EditOptionPane
                extends SOptionPane
            {
                public void updateOkButtonEnabledState() {
                    if (freeSpace < 0)
                        this.optionOK.setEnabled(false);
                    else
                        this.optionOK.setEnabled(true);
                }
            }


            int freeSpace = 100;
            List<String> backups = new ArrayList<String>();
            final SPanel inputPanel = new SPanel();
            EditOptionPane optionPane = new EditOptionPane();
            List<DesktopPane> toBeRemoved = new ArrayList<DesktopPane>();

            private double roundTo1Digit(double toRound) {
                if (toRound > 0)
                    return (int)(toRound * 100 + 0.5) / 100.0;
                else
                    return (int)(toRound * 100 - 0.5) / 100.0;
            }

            public void actionPerformed(ActionEvent evt) {
                final STextField freeSpaceField = new STextField(String.valueOf(freeSpace));
                inputPanel.removeAll();
                inputPanel.setLayout(new SGridLayout(frame.getContentPane().getComponentCount(), 6));
                inputPanel.setBorder(new SEmptyBorder(10, 10, 10, 10));
                freeSpace = 100;
                backups.clear();
                toBeRemoved.clear();
                for (int i = 0; i < frame.getContentPane().getComponentCount(); i++) {
                    backups.add(String.valueOf(((GridBagConstraints)frame.getContentPane().getConstraintAt(i)).weightx));
                }

                for (int i = 0; i < frame.getContentPane().getComponentCount(); i++) {
                    if (frame.getContentPane().getComponent(i) instanceof DesktopPane) {
                        final DesktopPane pane = (DesktopPane)frame.getContentPane().getComponent(i);
                        final SLabel nameLabel = new SLabel(pane.getName() + " ");
                        inputPanel.add(nameLabel);
                        final GridBagConstraints c = (GridBagConstraints)frame.getContentPane().getConstraintAt(i);

                        freeSpace -= Math.round(100 * c.weightx);

                        final STextField tf = new STextField(String.valueOf(Math.round(100 * c.weightx)));
                        tf.addActionListener(new ActionListener()
                        {
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    int input = Integer.parseInt(e.getActionCommand());
                                    if (input >= 0 && input <= 100) {

                                        freeSpace -= (input - Math.round(100 * c.weightx));
                                        freeSpaceField.setText(String.valueOf(freeSpace));
                                        c.weightx = input * 0.01;
                                        prefRoot.node("desktoppanes").node(pane.getName()).putDouble(DesktopPane.WEIGHTX, c.weightx);

                                        optionPane.updateOkButtonEnabledState();
                                    }
                                    else
                                        tf.setText(String.valueOf(Math.round(100 * c.weightx)));
                                }
                                catch (java.lang.NumberFormatException ex) {
                                    tf.setText(String.valueOf(Math.round(100 * c.weightx)));
                                }

                            }
                        });

                        inputPanel.add(tf);
                        final SLabel percentLabel = new SLabel(" %  ");
                        inputPanel.add(percentLabel);

                        final SButton plusButton = new SButton("+");
                        plusButton.addActionListener(new ActionListener()
                        {
                            public void actionPerformed(ActionEvent e) {
                                c.weightx = roundTo1Digit(c.weightx + 0.1);
                                prefRoot.node("desktoppanes").node(pane.getName()).putDouble(DesktopPane.WEIGHTX, c.weightx);
                                freeSpace -= 10;
                                tf.setText(String.valueOf(Math.round(100 * c.weightx)));
                                freeSpaceField.setText(String.valueOf(freeSpace));

                                optionPane.updateOkButtonEnabledState();

                            }
                        });

                        final SButton minusButton = new SButton("-");
                        minusButton.addActionListener(new ActionListener()
                        {
                            public void actionPerformed(ActionEvent e) {
                                if (Double.parseDouble(tf.getText()) >= 10) {
                                    c.weightx = roundTo1Digit(c.weightx - 0.1);
                                    prefRoot.node("desktoppanes").node(pane.getName()).putDouble(DesktopPane.WEIGHTX, c.weightx);
                                    freeSpace += 10;
                                    tf.setText(String.valueOf(Math.round(100.0 * c.weightx)));
                                    freeSpaceField.setText(String.valueOf(freeSpace));

                                    optionPane.updateOkButtonEnabledState();
                                }
                            }
                        });

                        final SButton removeButton = new SButton("remove");
                        removeButton.addActionListener(new ActionListener()
                        {
                            public void actionPerformed(ActionEvent e) {
                                toBeRemoved.add(pane);
                                freeSpace += (int)Math.round(100 * c.weightx);
                                freeSpaceField.setText(String.valueOf(freeSpace));

                                //remove column from dialog
                                inputPanel.remove(nameLabel);
                                inputPanel.remove(tf);
                                inputPanel.remove(percentLabel);
                                inputPanel.remove(plusButton);
                                inputPanel.remove(minusButton);
                                inputPanel.remove(removeButton);

                                optionPane.updateOkButtonEnabledState();

                            }
                        });

                        inputPanel.add(plusButton);
                        inputPanel.add(minusButton);
                        inputPanel.add(removeButton);
                    }
                }
                inputPanel.add(new SLabel("Free Space"));
                freeSpaceField.setText(String.valueOf(freeSpace));

                inputPanel.add(freeSpaceField);
                inputPanel.add(new SLabel(" %  "));

                optionPane = new EditOptionPane();
                optionPane.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e) {
                        if (e.getActionCommand() == SOptionPane.OK_ACTION) {

                            for (int i = 0; i < frame.getContentPane().getComponentCount(); i++) {
                                if (frame.getContentPane().getComponent(i) instanceof DesktopPane) {
                                    DesktopPane dp = (DesktopPane)frame.getContentPane().getComponent(i);
                                    if (toBeRemoved.contains(dp)) {
                                        dp.removeAll();
                                        frame.getContentPane().remove(dp);
                                        try {
                                            prefRoot.node("desktoppanes").node(dp.getName()).removeNode();
                                        }
                                        catch (BackingStoreException ex) {

                                        }

                                        i--;
                                    }
                                }
                            }

                            frame.getContentPane().setLayout(((SGridBagLayout)frame.getContentPane().getLayout()));

                        }

                        else {
                            //put backup back in
                            for (int i = 0; i < frame.getContentPane().getComponentCount(); i++) {
                                //ignore the menu
                                if (frame.getContentPane().getComponent(i) instanceof SMenuBar)
                                    continue;

                                GridBagConstraints c = (GridBagConstraints)frame.getContentPane().getConstraintAt(i);
                                c.weightx = java.lang.Double.parseDouble(backups.get(i));
                                prefRoot.node("desktoppanes").node(frame.getContentPane().getComponent(i).getName()).putDouble(DesktopPane.WEIGHTX, ((GridBagConstraints)frame.getContentPane().getConstraintAt(i)).weightx);
                            }
                        }
                    }
                });

                optionPane.showInput(frame, "Resize Desktop Panes", inputPanel, "Resize Desktop Panes");
                flushPreferences();
            }
        });
        
        SMenuItem resetItem = new SMenuItem("Reset Desktop");
        resetItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                Desktop.this.createNew(frame.getContentPane());
            }
        });
        
        SMenu desktopMenu = new SMenu("Desktop");
        desktopMenu.add(newPaneItem);
        desktopMenu.add(editPanesItem);
        desktopMenu.add(resetItem);


        SMenuBar menuBar = new SMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(desktopMenu);
        menuBar.add(windowMenu);

        return menuBar;
    }


    /**
     * Servletinfo
     */
    public String getServletInfo() {
        return "Desktop ($Revision$)";
    }

    private void flushPreferences() {
        try {
            prefRoot.flush();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * A menu item, that handles the position of an internal frame within
     * the desktop - whenever it is clicked, the frame is put on top. This
     * MenuItem is added in some component listener, that gets activated
     * whenever a window is added or removed from the desktop.
     */
    private static class WindowMenuItem
        extends SMenuItem
    {


        @Override
        public void setText(String t) {

            SMenu menu = (SMenu)this.getParentMenu();
            if (menu != null) {
                SMenu bar = (SMenu)menu.getParentMenu();
                bar.remove(menu);
                menu.remove(this);
                super.setText(t);
                menu.add(this);
                bar.add(menu);
            }
            else
                super.setText(t);

        }


        private final SInternalFrame frame;

        public WindowMenuItem(SInternalFrame f) {
            frame = f;
            this.setText(f.getTitle());
            this.setIcon(f.getIcon());
        }

        public WindowMenuItem(final SDesktopPane d, final SInternalFrame f) {
            frame = f;
            this.setText(f.getTitle());
            this.setIcon(f.getIcon());
            /*
             * when clicked, put that frame on top. If some other frame was
             * maximized at that point, then maximize _our_ frame instead.
             */
            addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent evt) {
                    d.setPosition(f, 0);
                    if (f.isIconified()) {
                        f.setIconified(false);
                    }
                    /*
                    * if some other frame is maximized, then we want
                    * to toggle this maximization..
                    */
                    try {
                        d.invite(new ComponentVisitor()
                        {
                            public void visit(SComponent c) { /*ign*/ }

                            public void visit(SContainer c) {
                                if (!(c instanceof SInternalFrame))
                                    return;
                                SInternalFrame ff = (SInternalFrame)c;
                                if (ff != frame && ff.isMaximized()) {
                                    ff.setMaximized(false);
                                    // set _our_ frame maximized, then.
                                    frame.setMaximized(true);
                                }
                            }
                        });
                    }
                    catch (Exception e) {
                        System.err.println(e);
                    }
                }
            });
        }

        /**
         * returns the title of the frame.
         */
        public String getText() {
            String title = frame.getTitle();
            return (title == null || title.length() == 0) ? "[noname]" : title;
        }


        /**
         * remove menu item by frame ..
         */
        public boolean equals(Object o) {
            if (o instanceof WindowMenuItem) {
                WindowMenuItem wme = (WindowMenuItem)o;
                return (frame != null && wme.frame == frame);
            }
            return false;
        }

        public void updateItem(WindowMenuItem updatedItem) {

        }
    }

    private class DesktopFrameListener
        implements SContainerListener
    {

        private DesktopPane pane;

        public DesktopFrameListener(DesktopPane pane) {
            this.pane = pane;
        }

        public void componentAdded(SContainerEvent e) {
            SInternalFrame frame = (SInternalFrame)e.getChild();
            SMenuItem windowItem = new WindowMenuItem(pane, frame);
            windowMenu.add(windowItem);
        }

        public void componentRemoved(SContainerEvent e) {
            SInternalFrame frame = (SInternalFrame)e.getChild();
            SMenuItem windowItem = new WindowMenuItem(frame);
            windowMenu.remove(windowItem);
        }
    }

}
