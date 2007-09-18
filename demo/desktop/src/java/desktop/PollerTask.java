package desktop;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class PollerTask
    extends java.util.TimerTask
    implements javax.servlet.ServletContextListener
{
    
    private String url = "";
    private SyndFeed lastFeed = new SyndFeedImpl();
    private List<FeedUpdatedListener> listeners = new ArrayList<FeedUpdatedListener>();
    static List<PollerTask> tasks = new ArrayList<PollerTask>();
    
    public PollerTask(){
	super();
    }
    

    public PollerTask(String url) {
	super();
	this.url = url;
	System.out.println("New Poller Task for feed: " + url);
	tasks.add(this);
	
    }

    public String getUrl() {
	return this.url;
    }
    
    public void setUrl(String url){
	this.url = url; 
    }

    public List<FeedUpdatedListener> getListeners() {
	return listeners;
    }

    public void addListener(FeedUpdatedListener listener) {
	if (!listeners.contains(listener))
	    listeners.add(listener);

	System.out.println(listeners.size() + " Listeners for task " + this.url);
    }

    public void removeListener(FeedUpdatedListener listener) {
	if (listeners.contains(listener))
	    listeners.remove(listener);

	System.out.println(listeners.size() + " Listeners for task " + this.url);
    }

    public void contextDestroyed(ServletContextEvent arg0) {
	System.out.println("Context Shutdown detected; Stopping PollerTasks");
	
	for(PollerTask task: tasks){
	    task.cancel();
	}
    }

    
    public void contextInitialized(ServletContextEvent arg0) {
	
    }
    
    public void run() {
	System.out.println("Polling " + url);
	try {
	    URL feedUrl = new URL(this.getUrl());
	    SyndFeedInput input = new SyndFeedInput();
	    SyndFeed feed = input.build(new XmlReader(feedUrl));

	    if (feed != lastFeed) {
		lastFeed = feed;

		for (FeedUpdatedListener listener : listeners) {
		    if (listener.getSession() != null && listener.getSession().getDispatcher() != null)
			listener.getSession().getDispatcher().invokeLater(new FeedUpdatedEvent(feed, listener));
		}
	    }
	    System.out.println("Finished polling");
	}
	catch (java.net.MalformedURLException e1) {
	    System.out.println("MalformedURLException");
	    e1.printStackTrace();
	}
	catch (java.io.IOException e2) {
	    System.out.println("IOException");
	    e2.printStackTrace();
	}
	catch (com.sun.syndication.io.FeedException e3) {
	    System.out.println("FeedException");
	    e3.printStackTrace();
	}
    }
    
    
}
