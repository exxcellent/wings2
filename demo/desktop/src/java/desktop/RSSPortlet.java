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
    
    @Override
	public void addComponentDropListener(SComponentDropListener listener) {
    	componentDropListeners.add(listener);
        SessionManager.getSession().getDragAndDropManager().registerDropTarget(this);
        
	}

	@Override
	public List<SComponentDropListener> getComponentDropListeners() {
		return this.componentDropListeners;
	}

	@Override
	public boolean isDragEnabled() {
		return this.dragEnabled;
	}

	@Override
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
	
	@Override
	public void internalFrameClosed(SInternalFrameEvent e) {close();}
	@Override
	public void internalFrameDeiconified(SInternalFrameEvent e) {}
	@Override
	public void internalFrameIconified(SInternalFrameEvent e) {}
	@Override
	public void internalFrameMaximized(SInternalFrameEvent e) {}
	@Override
	public void internalFrameOpened(SInternalFrameEvent e) {}
	@Override
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
