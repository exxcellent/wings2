package desktop;

import java.beans.*;
import java.io.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


public abstract class AbstractDesktopItem
    implements DesktopItem
{

    protected java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
    protected PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    protected ItemContainer container;
    
    protected Preferences pref;


    public static org.wings.util.SessionLocal<Integer> itemNo = new org.wings.util.SessionLocal<Integer>()
    {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };


    protected AbstractDesktopItem() {

    	pref = Preferences.userRoot().node("desktopitems").node("desktopitem" + itemNo.get().toString());
        this.putValue(KEY, "desktopitem" + itemNo.get().toString());
        itemNo.set(itemNo.get() + 1);
        pref.putInt(FIRST_FREE_INDEX, itemNo.get());

        addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName() == NAME) {
                    if (container != null)
                        container.setTitle((String)e.getNewValue());
                }
                else if (e.getPropertyName() == ICON) {
                    if (container != null)
                        container.setIcon((org.wings.SURLIcon)e.getNewValue());
                }
            }
        });
    }

    protected AbstractDesktopItem(String name) {

        pref = Preferences.userRoot().node("desktopitems").node(name);
        this.putValue(KEY, name);
        addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName() == NAME) {
                    if (container != null)
                        container.setTitle((String)e.getNewValue());
                }
                else if (e.getPropertyName() == ICON) {
                    if (container != null)
                        container.setIcon((org.wings.SURLIcon)e.getNewValue());
                }
            }
        });
    }


    public Object getValue(String key) {
        return attributes.get(key);
    }

    public void putValue(String key, Object value) {
        Object oldValue = attributes.put(key, value);
        changeSupport.firePropertyChange(key, oldValue, value);
        if (value instanceof String)
            pref.put(key, (String)value);
        else if (value instanceof Double)
            pref.putDouble(key, (Double)value);
        else if (value instanceof Boolean)
            pref.putBoolean(key, (Boolean)value);
        else if (value instanceof Integer)
            pref.putInt(key, (Integer)value);
        else if (value instanceof Long)
            pref.putLong(key, (Long)value);
        else if (value instanceof Float)
            pref.putFloat(key, (Float)value);

        try {
            pref.flush();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    public String toString() {
        return (String)attributes.get(NAME);
    }

    public ItemContainer getContainer() {
        return container;
    }

    public void setContainer(ItemContainer container) {
        this.container = container;
        container.setItem(this);

    }

    protected byte[] getByteArray(Object object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(object);
        oos.flush();
        oos.close();
        bos.close();
        return bos.toByteArray();
    }

    public void destroyed() {
        try {
            pref.removeNode();
            //pref.flush();
        }
        catch (BackingStoreException ex) {
            ex.printStackTrace();
        }

    }

}
