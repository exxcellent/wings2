<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/REC-html40/strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<%@ page language="java"
import="org.wings.*,
        org.wings.jsp.WingsSession,
        javax.swing.tree.DefaultTreeModel,
        org.wings.tree.STreeSelectionModel,
        org.wings.plaf.css.TreeCG,
        javax.swing.event.TreeSelectionListener,
        javax.swing.event.TreeSelectionEvent,
        javax.swing.tree.DefaultMutableTreeNode,
        javax.swing.tree.TreePath,
        jsp.*"
%>

<%
    // call the event dispatcher
    WingsSession.dispatchEvents(request, response);

    STree tree = null;
    STable tasks;
    STable subjects;
    SPanel panel = null;
    SLabel tableLabel = null;
    DocumentTools documentTools = null;

    synchronized (session) {
        try {
            // before you can use the wingS API, you have to call the following method.
            // it will associate the wingS Session with the current thread
            WingsSession wingsSession = WingsSession.getSession(request, response);

            // if this is the first request for this page, create the tree component
            tree = (STree)wingsSession.getComponent("tree");
            if (tree == null) {
                tree = new STree();
                tree.setRootVisible(false);
                tree.setModel(new DefaultTreeModel(NavigationTreeModel.ROOT_NODE));
                tree.getSelectionModel().setSelectionMode(STreeSelectionModel.SINGLE_TREE_SELECTION);
                tree.setCellRenderer(new NavigationTreeCellRenderer());
                TreeCG treeCG = ((TreeCG)tree.getCG());
                treeCG.setLeafControlIcon(treeCG.getEmptyFillIcon());
                wingsSession.addComponent("tree", tree);
            }

            tableLabel = (SLabel)wingsSession.getComponent("tablelabel");
            if (tableLabel == null) {
                tableLabel = new SLabel();
                wingsSession.addComponent("tablelabel", tableLabel);
            }

            panel = (SPanel)wingsSession.getComponent("panel");
            if (panel == null) {
                tasks = new STable();
                tasks.setModel(TasksTableModel.MODEL);
                tasks.getSelectionModel().setSelectionMode(SListSelectionModel.SINGLE_SELECTION);

                subjects = new STable();
                subjects.setModel(SubjectsTableModel.MODEL);
                subjects.getSelectionModel().setSelectionMode(SListSelectionModel.SINGLE_SELECTION);

                final SCardLayout cards = new SCardLayout();
                panel = new SPanel(cards);
                panel.add(tasks);
                panel.add(subjects);
                wingsSession.addComponent("panel", panel);

                final STable localTasks = tasks;
                final STable localSubjects = subjects;
                final SLabel localTableLabel = tableLabel;

                tree.addTreeSelectionListener(new TreeSelectionListener() {
                    public void valueChanged(TreeSelectionEvent e) {
                        TreePath newLeadSelectionPath = e.getNewLeadSelectionPath();
                        if (newLeadSelectionPath == null)
                            return;

                        DefaultMutableTreeNode node = (DefaultMutableTreeNode)newLeadSelectionPath.getPath()[1];
                        if ("Aufgaben".equals(node.getUserObject()))
                            cards.show(localTasks);
                        else if ("<html>Vorg&auml;nge".equals(node.getUserObject()))
                            cards.show(localSubjects);

                        localTableLabel.setText((String)node.getUserObject());
                    }
                });
            }

            documentTools = (DocumentTools) wingsSession.getComponent("documentTools");
            if (documentTools == null) {
                documentTools = new DocumentTools();
                wingsSession.addComponent("documentTools", documentTools);
            }
        }
        finally {
            // release the thread association
            WingsSession.removeSession();
        }
    }
%>

<%
        // headers are required for proper operation
        WingsSession.writeHeaders(request, response, out);
%>

<title>Test wingS JSP integration</title>
<body bgcolor="#f0f0f0">

<table>
<tr>
<th align="left" style="background-color: #ccccff">Navigation</th>
<th align="left" style="background-color: #ccccff"><%WingsSession.writeComponent(request, response, out, tableLabel);%></th>
</tr><tr>
<td valign="top" rowspan="3"><%WingsSession.writeComponent(request, response, out, tree);%></td>
<td valign="top"><%WingsSession.writeComponent(request, response, out, panel);%></td>
</tr><tr>
<th align="left" style="background-color: #ccccff; height: 22px">
<table cellpadding="0" cellspacing="0" style="width: 100%; margin: 0px">
<tr><th>Bedarf</th><td align="right"><%WingsSession.writeComponent(request, response, out, documentTools);%></td></tr>
</table></th>
</tr><tr>
<td valign="top" align="left"><button style="border: 1px solid gray">stornieren</button>
<br>
<br>
Here goes the document ..
</td>
</tr>
</table>

</body>
</html>
