<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/REC-html40/strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<%@ page language="java"
import="org.wings.*,
        org.wings.jsp.WingsSession,
        jsp.NavigationTreeModel,
        javax.swing.tree.DefaultTreeModel,
        jsp.NavigationTreeCellRenderer,
        org.wings.tree.STreeSelectionModel,
        org.wings.tree.SDefaultTreeSelectionModel,
        javax.swing.table.DefaultTableModel,
        jsp.TasksTableModel,
        org.wings.plaf.css.TreeCG,
        jsp.SubjectsTableModel,
        javax.swing.event.TreeSelectionListener,
        javax.swing.event.TreeSelectionEvent,
        javax.swing.tree.DefaultMutableTreeNode,
        javax.swing.tree.TreePath"
%>

<%
    // call the event dispatcher
    WingsSession.dispatchEvents(request, response);

    STree tree = null;
    STable tasks = null;
    STable subjects = null;
    SPanel panel = null;
    SLabel tableLabel = null;

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
                tree.getSelectionModel().setSelectionMode(STreeSelectionModel.SINGLE_SELECTION);
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
</head>
<body bgcolor="#f0f0f0">

<table>
<tr style="background-color: #ccccff">
<th align="left">Navigation</th>
<th align="left"><%WingsSession.writeComponent(request, response, out, tableLabel);%></th>
</tr><tr>
<td valign="top"><%WingsSession.writeComponent(request, response, out, tree);%></td>
<td valign="top"><%WingsSession.writeComponent(request, response, out, panel);%>
</tr>

</table>

</body>
</html>
