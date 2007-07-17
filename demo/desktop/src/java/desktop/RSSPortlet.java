/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package desktop;

import org.wings.SComponent;
import org.wings.SLabel;
import org.wings.SContainer;
import org.wings.SDesktopPane;
import org.wings.SInternalFrame;
import org.wings.SList;
import org.wings.dnd.DragSource;
import org.wings.dnd.DropTarget;
import org.wings.event.SComponentDropListener;
import org.wings.event.SInternalFrameEvent;
import org.wings.event.SInternalFrameListener;
import org.wings.session.SessionManager;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;


import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.XmlReader;
import com.sun.syndication.feed.synd.SyndEntryImpl;

/**
 * @author hengels
 */
public class RSSPortlet
        extends SInternalFrame implements SInternalFrameListener, DragSource, DropTarget
{
    

	private List<SComponentDropListener> componentDropListeners;
	private boolean dragEnabled;
	private String feed;
    private String user;
    private String password;
    private List entries;

    public RSSPortlet(String name, String feed) {
        this(name, feed, null, null);
    }

    public RSSPortlet(String name, String feed, String user, String password) {
        setTitle(name);
        this.feed = feed;
        this.user = user;
        this.password = password;
        
        entries = getNews();
        DefaultListModel model = new DefaultListModel();
        
        for(Object obj: entries){
        	model.addElement(((NewsFeedEntry)obj).getText());
        }
        
        final SList list = new SList(model);
        list.setShowAsFormComponent(false);
        list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        list.setOrderType("none");
        
        list.addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent e) {
                if (list.getSelectedIndex() == -1)
                    return;

                NewsFeedEntry entry = (NewsFeedEntry)entries.get(list.getSelectedIndex());
				System.out.println(entry.getUrl());
				SessionManager.getSession().setRedirectAddress(entry.getUrl());
                list.clearSelection();
            }
        });

        contentPane.add(list);
                                       
        setDragEnabled(true);
        componentDropListeners = new ArrayList<SComponentDropListener>();
        
        addInternalFrameListener(this);
        
        addComponentDropListener(new SComponentDropListener() {
            public boolean handleDrop(SComponent dragSource) {
            	
            	SContainer cont = dragSource.getParent();
            	
            	if(RSSPortlet.this.getParent() != cont){
            		if(cont instanceof SDesktopPane){
            			RSSPortlet.this.setMaximized(false);
            			if(dragSource instanceof SInternalFrame)
                    		((SInternalFrame)dragSource).setMaximized(false);
            			
            			SDesktopPane targetPane = (SDesktopPane)RSSPortlet.this.getParent();
            			SDesktopPane sourcePane = (SDesktopPane)cont;
            			sourcePane.remove(sourcePane.getIndexOf(dragSource));
            			targetPane.add(dragSource, targetPane.getIndexOf(RSSPortlet.this));
            			return true;
            		}
            	}
            	else if(cont instanceof SDesktopPane)
            	{
            		SDesktopPane pane = (SDesktopPane)cont;
            		int tindex = pane.getIndexOf(RSSPortlet.this);
            		int sindex = pane.getIndexOf(dragSource);
            		
            		if(sindex == tindex)
            			return false;
            		
            		RSSPortlet.this.setMaximized(false);
            		if(dragSource instanceof SInternalFrame)
                		((SInternalFrame)dragSource).setMaximized(false);
            		
            		pane.remove(sindex);
            		
            		if(tindex > sindex)
                		pane.add(dragSource, tindex -1);
                	else
                		pane.add(dragSource, tindex);
                	
            		return true;
            	}
            	return false;
            }
            });
            
    }
    
	public void addComponentDropListener(SComponentDropListener listener) {
    	componentDropListeners.add(listener);
        SessionManager.getSession().getDragAndDropManager().registerDropTarget(this);
        
	}

	public List<SComponentDropListener> getComponentDropListeners() {
		return this.componentDropListeners;
	}

	public boolean isDragEnabled() {
		return this.dragEnabled;
	}

	public void setDragEnabled(boolean dragEnabled) {
		this.dragEnabled = dragEnabled;
        if (dragEnabled) {
            SessionManager.getSession().getDragAndDropManager().registerDragSource((DragSource)this);
        } else {
            SessionManager.getSession().getDragAndDropManager().deregisterDragSource((DragSource)this);
        }
		
	}
	
	public void addInternalFrameListener(SInternalFrameListener listener) {
        addEventListener(SInternalFrameListener.class, listener);
    }
	
	public void close(){
		super.dispose();
	}
	
	public void internalFrameClosed(SInternalFrameEvent e) {close();}
	public void internalFrameDeiconified(SInternalFrameEvent e) {}
	public void internalFrameIconified(SInternalFrameEvent e) {}
	public void internalFrameMaximized(SInternalFrameEvent e) {}
	public void internalFrameOpened(SInternalFrameEvent e) {}
	public void internalFrameUnmaximized(SInternalFrameEvent e) {}

    List getNews() {
    	try{
    		URL feedUrl = new URL(this.feed); 
    		SyndFeedInput input = new SyndFeedInput();
    		SyndFeed feed = input.build(new XmlReader(feedUrl));
    		
    		ArrayList list = new ArrayList();
    		
    		
    		for (Object obj: feed.getEntries()){
    			SyndEntryImpl entry = (SyndEntryImpl)obj;
    			list.add(new NewsFeedEntry(entry.getTitle(), entry.getLink()));
    		}
    		
    		return list;
    	}
    	catch(Exception e){
    		System.err.println(e);
    	}
    	
    	return new ArrayList();
    }

    private void copy(InputStream in, PrintStream out) throws IOException {
        byte[] buffer = new byte[256];
        int len;
        while ((len = in.read(buffer)) > -1)
            out.write(buffer, 0, len);
    }

    private InputStream openFeed() throws IOException {
        URL url = new URL(feed);
        URLConnection connection = url.openConnection();
        connection.setDoInput(true);
        if (user != null) {
            String userPassword = user + ":" + password;
            String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
            connection.setRequestProperty("Authorization", "Basic " + encoding);
        }
        return connection.getInputStream();
    }
    
    private class NewsFeedEntry{
    	private String text;
    	private String url;
		public NewsFeedEntry(String text, String url) {
			this.text = text;
			this.url = url;
		}
		
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		
    	
    }
    
}
