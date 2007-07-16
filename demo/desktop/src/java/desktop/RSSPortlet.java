/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package desktop;

import org.wings.SBorderLayout;
import org.wings.SComponent;
import org.wings.SDimension;
import org.wings.SFlowDownLayout;
import org.wings.SForm;
import org.wings.SLabel;
import org.wings.SContainer;
import org.wings.SGridLayout;
import org.wings.SDesktopPane;
import org.wings.SInternalFrame;
import org.wings.dnd.DragSource;
import org.wings.dnd.DropTarget;
import org.wings.event.SComponentDropListener;
import org.wings.event.SInternalFrameEvent;
import org.wings.event.SInternalFrameListener;
import org.wings.session.SessionManager;
import org.wings.style.CSSProperty;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
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

    public RSSPortlet(String name, String feed) {
        this(name, feed, null, null);
    }

    public RSSPortlet(String name, String feed, String user, String password) {
        setTitle(name);
        this.feed = feed;
        this.user = user;
        this.password = password;
                
        SLabel label = new SLabel("<html>" + getNews());
                
        contentPane.add(label);
                                       
        setDragEnabled(true);
        componentDropListeners = new ArrayList<SComponentDropListener>();
        
        addInternalFrameListener(this);
        
        addComponentDropListener(new SComponentDropListener() {
            public boolean handleDrop(SComponent dragSource) {
            	if(!((DragSource)dragSource).isDragEnabled())
            		return false;
            	
            	SContainer cont = dragSource.getParent();
            	
            	if(RSSPortlet.this.getParent() != cont){
            		if(cont instanceof SDesktopPane){
            			RSSPortlet.this.setMaximized(false);
            			if(dragSource instanceof SInternalFrame)
                    		((SInternalFrame)dragSource).setMaximized(false);
            			
            			SDesktopPane targetPane = (SDesktopPane)RSSPortlet.this.getParent();
            			SDesktopPane sourcePane = (SDesktopPane)cont;
            			int sindex = sourcePane.getIndexOf(dragSource);
            			int tindex = targetPane.getIndexOf(RSSPortlet.this);
            			sourcePane.remove(sindex);
            			targetPane.remove(tindex);
            			sourcePane.add(RSSPortlet.this, sindex);
            			targetPane.add(dragSource, tindex);
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
            		
            		if(tindex > sindex){
                		pane.remove(tindex);
                		pane.remove(sindex);
                		pane.add(RSSPortlet.this, sindex);
                		pane.add(dragSource, tindex);
                		
                	}
                	else{
                		pane.remove(sindex);
                		pane.remove(tindex);
                		pane.add(dragSource, tindex);
                		pane.add(RSSPortlet.this, sindex);
                		
                	}
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

    String getNews() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            TransformerFactory tFactory = TransformerFactory.newInstance();
            String ctx = getSession().getServletContext().getRealPath("") + System.getProperty("file.separator");
            Source xslSource = new StreamSource(new URL("file", "", ctx + "rss.xsl").openStream());
            Transformer transformer = tFactory.newTransformer(xslSource);

            copy(openFeed(), System.out);
            Source xmlSource = new StreamSource(openFeed());
            transformer.transform(xmlSource, new StreamResult(out));
            return out.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
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
}
