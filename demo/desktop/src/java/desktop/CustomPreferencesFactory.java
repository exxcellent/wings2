package desktop;

import java.io.File;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

public class CustomPreferencesFactory implements PreferencesFactory {
	
	
	@Override
	public Preferences systemRoot() {
		return CustomPreferences.getSystemRoot();
	}

	@Override
	public Preferences userRoot() {
		return CustomPreferences.getUserRoot();
	}

}
