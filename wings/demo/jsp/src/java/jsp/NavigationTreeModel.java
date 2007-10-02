/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package jsp;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * @author hengels
 */
public class NavigationTreeModel {
    public static final TreeNode ROOT_NODE = generateTree();

    static TreeNode generateTree() {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Navigation");
        DefaultMutableTreeNode category;
        DefaultMutableTreeNode process;
        DefaultMutableTreeNode activity;

        // Termine
        category = new DefaultMutableTreeNode("Termine");
        top.add(category);

        category.add(process = new DefaultMutableTreeNode("Tag"));
        category.add(process = new DefaultMutableTreeNode("Woche"));
        category.add(process = new DefaultMutableTreeNode("Monat"));


        // Aufgaben
        category = new DefaultMutableTreeNode("Aufgaben");
        top.add(category);

        // Bedarf
        category.add(process = new DefaultMutableTreeNode("Bedarf"));
        process.add(activity = new DefaultMutableTreeNode("<html>pr&uuml;fen"));
        process.add(activity = new DefaultMutableTreeNode("genehmigen"));

        // Urlaubsantrag
        category.add(process = new DefaultMutableTreeNode("Urlaubsantrag"));
        process.add(activity = new DefaultMutableTreeNode("genehmigen"));

        // Reklamation
        category.add(process = new DefaultMutableTreeNode("Reklamation"));
        process.add(activity = new DefaultMutableTreeNode("<html>pr&uuml;fen"));
        process.add(activity = new DefaultMutableTreeNode("regulieren"));


        // Vorg�nge
        category = new DefaultMutableTreeNode("<html>Vorg&auml;nge");
        top.add(category);

        // Bedarf
        category.add(process = new DefaultMutableTreeNode("Bedarf"));
        process.add(activity = new DefaultMutableTreeNode("laufende"));
        process.add(activity = new DefaultMutableTreeNode("abgeschlossene"));

        // Urlaubsantrag
        category.add(process = new DefaultMutableTreeNode("Urlaubsantrag"));
        process.add(activity = new DefaultMutableTreeNode("laufende"));
        process.add(activity = new DefaultMutableTreeNode("abgeschlossene"));


        // Neuer Vorgang
        category = new DefaultMutableTreeNode("<html>Neuer Vorgang");
        top.add(category);

        // Bedarf
        category.add(process = new DefaultMutableTreeNode("Bedarf"));
        category.add(process = new DefaultMutableTreeNode("Urlaubsantrag"));

        return top;
    }
}
