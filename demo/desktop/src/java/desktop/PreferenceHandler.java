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
	private static String COOKIE_NAME = "PreferencesCookie";
	private Preferences pref;
	private static String PANE_SUFFIX ="desktoppanes";
	private static String ITEM_SUFFIX = "desktopitems";
	private static Integer userID = 0;
	private static String NEXT_FREE_USER_ID = "nextFreeUserID";
	private Preferences rootPref;
	
	
	private PreferenceHandler(){
		//rootPref = new DesktopPreferences(null, "");
		rootPref = Preferences.userNodeForPackage(this.getClass());
		
		File configFile = new File("desktop.xml");
		if(configFile.exists()){
			try{
				rootPref.importPreferences(new FileInputStream(configFile));
				userID = rootPref.getInt(NEXT_FREE_USER_ID, 0);
			}catch(Exception ex){ex.printStackTrace();}
		}
				
		pref = rootPref.node("temp");
	}
	
	public boolean returningUser(){
		HttpServletRequest request = org.wings.session.SessionManager.getSession().getServletRequest();
		boolean isReturning = false;
		
		if(request.getUserPrincipal()!= null && request.getUserPrincipal().getName()!= null){
			String userName = request.getUserPrincipal().getName();
			pref = rootPref.node(userName);
			File file = new File(userName + ".xml");
			if(file.exists()){
				isReturning = true;
				try{
					pref.importPreferences(new FileInputStream(file));
				}catch(Exception ex){ex.printStackTrace();}
			}
			else
				pref = rootPref.node(userName);
		}
		else{
			Cookie[] cookies = request.getCookies();
			for(int i=0; i< cookies.length; i++){
				if(cookies[i].getName().equals(COOKIE_NAME)){
					pref = rootPref.node(cookies[i].getValue());
					File file = new File(pref.name() + ".xml");
					if(file.exists()){
						try{
							InputStream is = new BufferedInputStream(new FileInputStream(file));
							pref.importPreferences(is);
						}catch(Exception ex){ex.printStackTrace();}
					}
					else{
						isReturning = false;
						break;
					}
					
					isReturning = true;
					break;
				}
			}
			
			if(!isReturning){
				pref = rootPref.node(userID.toString());
				Cookie cookie= new Cookie(COOKIE_NAME, ((Integer)rootPref.getInt(NEXT_FREE_USER_ID, 0)).toString());
				
				
				cookie.setMaxAge(1000000000);
				org.wings.session.SessionManager.getSession().getServletResponse().addCookie(cookie);
				userID++;
				rootPref.putInt(NEXT_FREE_USER_ID, userID);
				
				try{
					OutputStream os = new BufferedOutputStream(new FileOutputStream("desktop.xml"));
					rootPref.exportNode(os);
				}catch(Exception ex){ex.printStackTrace();}
			}
		}
				
		
		pref.addNodeChangeListener(new DesktopNodeChangeListener());
		
		pref.addPreferenceChangeListener(new DesktopPreferenceChangeListener());
		
		return isReturning;
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
