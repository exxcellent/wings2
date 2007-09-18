package desktop;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.wings.event.*;

import java.net.URL;
import java.util.*;


public class Poller
    extends Timer
{

    private List<PollerTask> pollerTasks = new ArrayList<PollerTask>();
    private long pollingInterval = 60000;

    public long getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(long pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    public synchronized void registerFeedUpdatedListener(String url, FeedUpdatedListener listener) {
        PollerTask pt = getPollerTask(url);

        if (pt != null)
            pt.addListener(listener);
        else {
            pt = new PollerTask(url);
            //pt.setUrl(url);
            pt.addListener(listener);
            pollerTasks.add(pt);
            System.out.println("Listener Session: " + listener.getSession().toString());


            listener.getSession().addExitListener(new SessionExitListener(pt));
            this.scheduleAtFixedRate(pt, 0, pollingInterval);
        }
        //run the poller immediately to have the feeds filled at startup
        //pt.run();
    }

    public synchronized void unregisterFeedUpdatedListener(String url, FeedUpdatedListener listener) {
        PollerTask pt = getPollerTask(url);

        if (pt == null) {
            System.err.println("Cannot unregister FeedUpdateListener because it doesn't exist");
            return;
        }

        pt.removeListener(listener);

        if (pt.getListeners().isEmpty()) {
            System.out.println("Destroying listener for URL " + pt.getUrl());
            pt.cancel();

            pollerTasks.remove(pt);
        }
    }

    private PollerTask getPollerTask(String url) {
        for (PollerTask pt : pollerTasks) {
            if (pt.getUrl().equalsIgnoreCase(url))
                return pt;
        }
        return null;
    }

    

    private class SessionExitListener
        implements SExitListener
    {

        private PollerTask task;

        public SessionExitListener(PollerTask task) {
            this.task = task;
        }

        public void prepareExit(SExitEvent e) throws ExitVetoException {
            System.out.println("Session " + e.getSourceSession().toString() + " timed out.");
            List<FeedUpdatedListener> listenersToRemove = new ArrayList<FeedUpdatedListener>();
            for (FeedUpdatedListener ful : task.getListeners()) {
                if (e.getSourceSession().equals(ful.getSession())) {
                    System.out.println("Unregistering Listener for " + task.getUrl() + " from Session " + ful.getSession());
                    listenersToRemove.add(ful);
                }
            }
            for (FeedUpdatedListener ful : listenersToRemove) {
                Poller.this.unregisterFeedUpdatedListener(task.getUrl(), ful);
            }
        }
    }
}
