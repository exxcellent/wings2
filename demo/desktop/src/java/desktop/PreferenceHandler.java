package desktop;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.prefs.NodeChangeEvent;
import java.util.prefs.NodeChangeListener;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

public class PreferenceHandler {
	
	private static PreferenceHandler handler;
	private Preferences pref;
	private static String PANE_SUFFIX ="desktoppanes";
	private static String ITEM_SUFFIX = "desktopitems";
	private static Integer userID = 0;
	private static String NEXT_FREE_USER_ID = "nextFreeUserID";
	private Preferences rootPref;
	
	
	private PreferenceHandler(){
		//rootPref = new DesktopPreferences(null, "");
		rootPref = Preferences.userNodeForPackage(this.getClass());
		try{
			InputStream is = new BufferedInputStream(new FileInputStream("desktop.xml"));
			rootPref.importPreferences(is);
			userID = rootPref.getInt(NEXT_FREE_USER_ID, 0);
		}catch(Exception ex){ex.printStackTrace();}
				
		pref = rootPref.node("temp");
	}
	
	public boolean cookieExists(){
		Cookie[] cookies = org.wings.session.SessionManager.getSession().getServletRequest().getCookies();
		boolean cookieExists = false;
		
		for(int i=0; i< cookies.length; i++){
			if(cookies[i].getName().equals("DesktopDemoCookie")){
				pref = rootPref.node(cookies[i].getValue());
				try{
					InputStream is = new BufferedInputStream(new FileInputStream(pref.name() + ".xml"));
					pref.importPreferences(is);
				}catch(Exception ex){ex.printStackTrace();}
				
				cookieExists = true;
				break;
			}
		}
		
		if(!cookieExists){
			pref = rootPref.node(userID.toString());
			Cookie cookie= new Cookie("DesktopDemoCookie", ((Integer)rootPref.getInt(NEXT_FREE_USER_ID, 0)).toString());
			
			
			cookie.setMaxAge(1000000000);
			org.wings.session.SessionManager.getSession().getServletResponse().addCookie(cookie);
			userID++;
			rootPref.putInt(NEXT_FREE_USER_ID, userID);
			
			try{
				OutputStream os = new BufferedOutputStream(new FileOutputStream("desktop.xml"));
				rootPref.exportNode(os);
			}catch(Exception ex){ex.printStackTrace();}
		}
		
		pref.addNodeChangeListener(new DesktopNodeChangeListener());
		
		pref.addPreferenceChangeListener(new DesktopPreferenceChangeListener());
		
		return cookieExists;
	}
	
	public static PreferenceHandler getPreferenceHandler(){
		if(handler == null)
			handler = new PreferenceHandler();
		
		return handler;
	}
	
	private void export(){
		try{
			pref.flush();
			OutputStream os = new BufferedOutputStream(new FileOutputStream(pref.name()+".xml"));
			pref.exportSubtree(os);
		}catch(Exception ex){ex.printStackTrace();}
	}
	
	public Preferences getUserRootPreference(){
		return pref;
	}
	
	public Preferences getPanePreferences(){
		return pref.node(PANE_SUFFIX);
	}
	
	public Preferences getItemPreferences(){
		return pref.node(ITEM_SUFFIX);
	}
	
	private class DesktopNodeChangeListener implements NodeChangeListener{
		public void childAdded(NodeChangeEvent evt){
			evt.getChild().addNodeChangeListener(new DesktopNodeChangeListener());
			evt.getChild().addPreferenceChangeListener(new DesktopPreferenceChangeListener());
			export();
		}
		
		public void childRemoved(NodeChangeEvent evt){
			export();
		}
		
	}
	
	private class DesktopPreferenceChangeListener implements PreferenceChangeListener{
		public void preferenceChange(PreferenceChangeEvent arg0) {
			export();
			
		}
	}
}
