package desktop;

import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

public class CustomPreferencesFactory
    implements PreferencesFactory
{

    public Preferences systemRoot() {
        return CustomPreferences.getSystemRoot();
    }

    public Preferences userRoot() {
        return CustomPreferences.getUserRoot();
    }

}
