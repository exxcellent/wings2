<%@ page language="java"
import="jsp.*,org.wings.*,org.wings.style.*,org.wings.session.*,org.wings.externalizer.*,org.wings.io.*,org.wings.session.*,java.awt.*,java.io.*,javax.swing.tree.*,
        jsp.HugeTreeModel,
        org.wings.event.SRequestEvent,
        java.util.Enumeration,
        java.util.Iterator"
%>

<%!
%>
<%
    JSPSession wingsSession = (JSPSession)session.getAttribute("Session:" + getClass().getName());

    // common initializations
    if (wingsSession == null) {
        wingsSession = new JSPSession();
        session.setAttribute("Session:" + getClass().getName(), wingsSession);
        SessionManager.setSession(wingsSession);

        wingsSession.init(config, request);
        RequestURL requestURL = new RequestURL("TreeExample.jsp", response.encodeURL("TreeExample.jsp"));
        wingsSession.setProperty("request.url", requestURL);

        SFrame frame = new SFrame();
        frame.setTargetResource("");
        frame.show();
    }

    SessionManager.setSession(wingsSession);
    wingsSession.setServletRequest(request);
    wingsSession.setServletResponse(response);

    SFrame frame = wingsSession.getRootFrame();

    // event dispatching
    wingsSession.fireRequestEvent(SRequestEvent.REQUEST_START);

    SForm.clearArmedComponents();

    try {
        Enumeration en = request.getParameterNames();

        if (en.hasMoreElements()) {
            wingsSession.fireRequestEvent(SRequestEvent.DISPATCH_START);

            while (en.hasMoreElements()) {
                String paramName = (String) en.nextElement();
                String[] value = request.getParameterValues(paramName);
                wingsSession.getDispatcher().dispatch(paramName, value);
            }
            SForm.fireEvents();

            wingsSession.fireRequestEvent(SRequestEvent.DISPATCH_DONE);
        }

        wingsSession.fireRequestEvent(SRequestEvent.PROCESS_REQUEST);
        wingsSession.fireRequestEvent(SRequestEvent.DELIVER_START);
    }
    catch (Exception e) {
        e.printStackTrace();
    }
%>

<%  // application specific

    STree tree = (STree) session.getAttribute("tree");
    if (tree == null) {
        tree = new STree();
        tree.setModel(new DefaultTreeModel(HugeTreeModel.ROOT_NODE));
        frame.getContentPane().add(tree, "tree");
        session.setAttribute("tree", tree);
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
    wingsSession.fireRequestEvent(SRequestEvent.DELIVER_DONE);
    wingsSession.fireRequestEvent(SRequestEvent.REQUEST_END);
    SessionManager.removeSession();
%>