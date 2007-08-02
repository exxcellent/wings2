/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package desktop;

import org.wings.SBorderLayout;
import org.wings.SPanel;
import org.wings.SComponent;
import org.wings.SLabel;
import org.wings.SContainer;
import org.wings.SDefaultListModel;
import org.wings.SDesktopPane;
import org.wings.SInternalFrame;
import org.wings.SList;
import org.wings.dnd.DragSource;
import org.wings.dnd.DropTarget;
import org.wings.event.SComponentDropListener;
import org.wings.event.SParentFrameListener;
import org.wings.event.SParentFrameEvent;
import org.wings.session.Session;
import org.wings.session.SessionManager;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;


import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.XmlReader;
import com.sun.syndication.feed.synd.SyndEntryImpl;

/**
 * @author hengels
 */
public class NewsFeedPanel
        extends SPanel
        implements FeedUpdatedListener
{
	private static Poller poller;
	private String feed;
    private String user;
    private String password;
    private SList list = new SList();
    private java.util.Map<String, Object> values = new java.util.HashMap<String,Object>();

    private static org.wings.util.SessionLocal<Integer> feedNo = new org.wings.util.SessionLocal<Integer>(){
		@Override
		protected Integer initialValue() {
			return 0;
		}};
    
    public NewsFeedPanel() {
    	setName("newsfeedpanel"+ feedNo.get().toString());
    	feedNo.set(feedNo.get() +1);
    	setLayout(new SBorderLayout());
    }
    
   public String getFeed() {
		return feed;
	}

	public void setFeed(String feed) {
		this.feed = feed;
		
		if(poller == null)
        	poller = new Poller();
        
        poller.registerFeedUpdatedListener(feed, (FeedUpdatedListener)this);
        
        list = new SList();
        list.setShowAsFormComponent(false);
        list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        list.setOrderType("none");
               
        list.addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent e) {
        		if(list.getSelectedIndex() == -1)
        			return;
        		
        		NewsFeedEntry entry = (NewsFeedEntry)list.getSelectedValue();
        		SessionManager.getSession().setRedirectAddress(entry.getUrl());
        		list.clearSelection();
            }
        });
        
        add(list);
        this.reload();
	}
    
    public boolean feedUpdated(SyndFeed feed) {
		SDefaultListModel model = new SDefaultListModel();
		
		for (Object obj: feed.getEntries()){
			SyndEntryImpl entry = (SyndEntryImpl)obj;
			model.addElement(new NewsFeedEntry(entry.getTitle(), entry.getLink()));
		}
		
		list.setModel(model);
		list.reload();
		return true;
	}
    
   	
	
	public void destroy(){
		poller.unregisterFeedUpdatedListener(this.feed, (FeedUpdatedListener)this);
		
	}
	
	private class NewsFeedEntry{
    	private String text;
    	private String url;
		public NewsFeedEntry(String text, String url) {
			this.text = text;
			this.url = url;
		}
		
		public String getText() {return text;}
		public void setText(String text) {this.text = text;}
		public String getUrl() {return url;}
		public void setUrl(String url) {this.url = url;}
		
		public String toString(){return getText();}
    }
	
}
