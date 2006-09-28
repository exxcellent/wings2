package org.wings;

import org.wings.session.SessionManager;
import org.wings.session.Session;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: hengels
 * Date: Aug 27, 2006
 * Time: 9:13:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class WingSUtilities
{
    /**
     * Enqueues the runnable to be called within the next browser request right after
     * events have been dispatched.
     *
     * <code>javax.swing.SwingUtilities.invokeLater()</code>.
     */
    public static void invokeLater(Session session, Runnable doRun) {
        session.getDispatcher().invokeLater(doRun);
    }


    /**
     * Waits for the next browser request and calls the runnable right after
     * events have been dispatched.
     *
     * <code>javax.swing.SwingUtilities.invokeAndWait()</code>.
    public static void invokeAndWait(Session session, final Runnable doRun)
        throws InterruptedException, InvocationTargetException
    {
    }
     TODO: this is quite complex. wait until either the next request arrives
           or until the session terminates
     */

    /**
     * Returns true if the current thread is a wingS event dispatching thread.
     * <code>javax.swing.SwingUtilities.isDispatchThread()</code>.
     *
     * @return true if the current thread is a wingS event dispatching thread
     */
    public static boolean isEventDispatchThread()
    {
        return SessionManager.getSession() != null;
    }
}
