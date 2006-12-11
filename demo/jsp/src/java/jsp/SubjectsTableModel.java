/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package jsp;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;

/**
 * @author hengels
 */
public class SubjectsTableModel {
    public static final TableModel MODEL = generateTable();

    static TableModel generateTable() {
        DefaultTableModel model = new DefaultTableModel(new Object[][] {
            { "Bedarf",        "<html>l&auml;uft", "Schreibtisch",          "Ole Langbehn" },
            { "Bedarf",        "<html>l&auml;uft", "Bildschirm",            "Ole Langbehn" },
            { "Urlaubsantrag", "abgeschlossen",   "15.8.2005 - 29.8.2005", "Henner Zeller" },
            { "Urlaubsantrag", "abgeschlossen",   "29.8.2005 - 09.9.2005", "Holger Engels" },
            { "Urlaubsantrag", "abgeschlossen",   "29.8.2005 - 09.9.2005", "Armin Haaf" },
        }, new String[] {
            "Prozess",
            "Status",
            "Gegenstand",
            "Von",
        });
        
        return model;
    }
}
