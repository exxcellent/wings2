package desktop;

import com.sun.syndication.feed.synd.SyndFeed;

public class FeedUpdatedEvent
    implements Runnable
{

    private SyndFeed feed;
    private FeedUpdatedListener listener;

    public FeedUpdatedEvent(SyndFeed feed, FeedUpdatedListener listener) {
        this.feed = feed;
        this.listener = listener;
    }

    public SyndFeed getFeed() {
        return feed;
    }

    public void run() {
        listener.feedUpdated(feed);
    }
}
