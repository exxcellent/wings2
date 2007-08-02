package desktop;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;

public class DesktopPreferences extends AbstractPreferences {

	private AbstractPreferences parent = null;
	private String name = "";
	private Map<String, DesktopPreferences> children = new HashMap<String, DesktopPreferences>();
	
	public String getName(){
		return name;
	}
	
	Preferences p;
	DesktopPreferences(AbstractPreferences parent, String name){
		super(null, name);
		this.name = name;
		this.parent = parent;
	}
	
	public DesktopPreferences node(String name) {
		return (DesktopPreferences)childSpi(name);
	}
	
	@Override
	protected AbstractPreferences childSpi(String name) {
		if(!children.containsKey(name))
			children.put(name, new DesktopPreferences(this, name));
		
		return children.get(name);
	}

	@Override
	protected String[] childrenNamesSpi() throws BackingStoreException {
		String[] strArr =new  String[children.size()];
		return children.keySet().toArray(strArr);
	}

	@Override
	protected void flushSpi() throws BackingStoreException {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getSpi(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] keysSpi() throws BackingStoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void putSpi(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void removeNodeSpi() throws BackingStoreException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void removeSpi(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void syncSpi() throws BackingStoreException {
		// TODO Auto-generated method stub

	}

}
