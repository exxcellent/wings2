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
public class TasksTableModel {
    public static final TableModel MODEL = generateTable();

    static TableModel generateTable() {
        DefaultTableModel model = new DefaultTableModel(new Object[][] {
            { "Bedarfsposition", "genehmigen",        "Schreibtisch",          "Ole Langbehn" },
            { "Bedarfsposition", "genehmigen",        "Bildschirm",            "Ole Langbehn" },
            { "Reklamation",     "<html>pr&uuml;fen", "Seifenblase",           "Benjamin Schmid" },
            { "Urlaubsantrag",   "genehmigen",        "15.8.2005 - 29.8.2005", "Henner Zeller" },
            { "Urlaubsantrag",   "genehmigen",        "29.8.2005 - 09.9.2005", "Holger Engels" },
            { "Urlaubsantrag",   "genehmigen",        "29.8.2005 - 09.9.2005", "Armin Haaf" },
        }, new String[] {
            "Prozess",
            "<html>Aktivit&auml;t",
            "Gegenstand",
            "Von",
        });
        
        return model;
    }
}
