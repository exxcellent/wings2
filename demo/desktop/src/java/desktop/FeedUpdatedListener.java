package desktop;

import com.sun.syndication.feed.synd.SyndFeed;
import org.wings.session.Session;

public interface FeedUpdatedListener
{
    boolean feedUpdated(SyndFeed feed);

    Session getSession();
}
