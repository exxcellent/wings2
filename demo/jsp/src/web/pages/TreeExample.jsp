<%@ page language="java"
import="org.wings.*,
        org.wings.jsp.WingsSession,
        jsp.HugeTreeModel,
        javax.swing.tree.DefaultTreeModel"
%>

<%
    // call the event dispatcher
    WingsSession.dispatchEvents(request, response);

    STree tree;

    synchronized (session) {
        try {
            // before you can use the wingS API, you have to call the following method.
            // it will associate the wingS Session with the current thread
            WingsSession wingsSession = WingsSession.getSession(request, response);

            // if this is the first request for this page, create the tree component
            tree = (STree)wingsSession.getComponent("tree");
            if (tree == null) {
                tree = new STree();
                tree.setModel(new DefaultTreeModel(HugeTreeModel.ROOT_NODE));
                wingsSession.addComponent("tree", tree);
            }
        }
        finally {
            // release the thread association
            WingsSession.removeSession();
        }
    }
%>

<html>
<head>

<%
    // headers are required for proper operation
    WingsSession.writeHeaders(request, response, out);
%>

<title>Test wingS JSP integration</title>
</head>
<body bgcolor="#f0f0f0">

<%
    // write a component
    WingsSession.writeComponent(request, response, out, tree);
%>

</body>
</html>
