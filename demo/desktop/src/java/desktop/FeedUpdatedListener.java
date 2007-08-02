package desktop;

import org.wings.session.Session;
import com.sun.syndication.feed.synd.SyndFeed;

public interface FeedUpdatedListener {
	boolean feedUpdated(SyndFeed feed);
	Session getSession();
}
