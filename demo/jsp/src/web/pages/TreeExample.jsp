<%@ page language="java"
import="jsp.*,org.wings.*,
        org.wings.style.*,
        org.wings.session.*,
        org.wings.externalizer.*,
        org.wings.io.*,
        org.wings.session.*,
        java.io.*,javax.swing.tree.*,
        jsp.HugeTreeModel,
        org.wings.event.SRequestEvent,
        java.util.*,
        org.wings.jsp.WingsSession"
%>

<%
    WingsSession wingsSession = WingsSession.getSession(request, response);
    SFrame frame = wingsSession.getFrame("TreeExample.jsp");

    wingsSession.fireRequestEvent(SRequestEvent.REQUEST_START);
    wingsSession.dispatchEvents(request);

    STree tree = (STree) session.getAttribute("tree");
    if (tree == null) {
        tree = new STree();
        tree.setModel(new DefaultTreeModel(HugeTreeModel.ROOT_NODE));
        frame.getContentPane().add(tree, "tree");
        session.setAttribute("tree", tree);
    }

    if (request.getPathInfo() != null) {
        String s = "/ExternalizerServlet/" + request.getPathInfo();
        System.out.println("s = " + s);
        RequestDispatcher dispatcher = request.getRequestDispatcher(s);
        if (dispatcher != null)
            dispatcher.forward(request, response);
        return;
    }
%>

<html><head>
<%
    StringBufferDevice headerdev = new StringBufferDevice();
    for (Iterator iterator = frame.headers().iterator(); iterator.hasNext();) {
        Object next = iterator.next();
        if (next instanceof Renderable) {
            ((Renderable) next).write(headerdev);
        } else {
            org.wings.plaf.Utils.write(headerdev, next.toString());
        }
        headerdev.write("\n".getBytes());
    }
    out.print(headerdev);
%>

<title>Test wingS JSP integration</title>
</head>
<body bgcolor="#f0f0f0">

<%
    StringBufferDevice outdev = new StringBufferDevice();
    tree.write(outdev);
    out.print(outdev);
%>

</body>
</html>

<%
    wingsSession.fireRequestEvent(SRequestEvent.REQUEST_END);
    SessionManager.removeSession();
%>