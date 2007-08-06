package desktop;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.prefs.Preferences;

public class PreferenceHandler
{

    private static PreferenceHandler handler;
    private static String COOKIE_NAME = "PreferencesCookie";
    private Preferences pref;
    private static String PANE_SUFFIX = "desktoppanes";
    private static String ITEM_SUFFIX = "desktopitems";
    private static Integer userID = 0;
    private static String NEXT_FREE_USER_ID = "nextFreeUserID";
    private static Preferences systemPref;


    private PreferenceHandler() {
        systemPref = Preferences.systemRoot();
        System.out.println(systemPref.getInt(NEXT_FREE_USER_ID, 0));
        System.out.println();
        //pref= Preferences.userRoot().node("temp");
        //systemPref = Preferences.systemNodeForPackage(this.getClass());
        /*
          File configFile = new File("desktop.xml");

          if(configFile.exists()){
              try{
                  System.out.println(configFile.getAbsolutePath());
                  Preferences.importPreferences(new FileInputStream(configFile));
                  String[] keys = systemPref.keys();

                  userID = systemPref.getInt(NEXT_FREE_USER_ID, 0);
                  System.out.println("UserID: "+userID);

              }catch(Exception ex){ex.printStackTrace();}
          }

          //pref = Preferences.userRoot().node("temp");

          */
    }

    public boolean returningUser() {
        HttpServletRequest request = org.wings.session.SessionManager.getSession().getServletRequest();
        boolean isReturning = false;

        if (request.getUserPrincipal() != null && request.getUserPrincipal().getName() != null) {
            String userName = request.getUserPrincipal().getName();
            pref = Preferences.userRoot().node(userName);

        }
        else {
            Cookie[] cookies = request.getCookies();
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(COOKIE_NAME)) {
                    pref = Preferences.userRoot().node(cookies[i].getValue());

                    isReturning = true;
                    break;
                }
            }

            if (!isReturning) {
                pref = Preferences.userRoot().node(userID.toString());
                Cookie cookie = new Cookie(COOKIE_NAME, ((Integer)systemPref.getInt(NEXT_FREE_USER_ID, 0)).toString());


                cookie.setMaxAge(1000000000);
                org.wings.session.SessionManager.getSession().getServletResponse().addCookie(cookie);
                userID++;
                systemPref.putInt(NEXT_FREE_USER_ID, userID);


                try {
                    systemPref.flush();
                    //OutputStream os = new BufferedOutputStream(new FileOutputStream("desktop.xml"));
                    //systemPref.exportNode(os);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }


        return isReturning;
    }

    public static PreferenceHandler getPreferenceHandler() {
        if (handler == null)
            handler = new PreferenceHandler();

        return handler;
    }


    public Preferences getUserRootPreference() {
        return pref;
    }

    public Preferences getPanePreferences() {
        return pref.node(PANE_SUFFIX);
    }

    public Preferences getItemPreferences() {
        return pref.node(ITEM_SUFFIX);
    }


}
