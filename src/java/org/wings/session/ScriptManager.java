package org.wings.session;

import org.wings.script.ScriptListener;

import java.util.List;
import java.util.LinkedList;

public class ScriptManager
{
    private final List scriptListenerList = new LinkedList();

    public static ScriptManager getInstance() {
        return SessionManager.getSession().getScriptManager();
    }

    public final void addScriptListener(ScriptListener listener) {
        if (scriptListenerList.contains(listener))
            return;

        int placingPosition = -1;
        for (int i = 0; i < scriptListenerList.size() && placingPosition < 0; i++) {
            ScriptListener existingListener = (ScriptListener)scriptListenerList.get(i);
            if (existingListener.getPriority() < listener.getPriority())
                placingPosition = i;
        }

        if (placingPosition >= 0)
            scriptListenerList.add(placingPosition, listener);
        else
            scriptListenerList.add(listener);
    }

    public final void addScriptListeners(ScriptListener[] listeners) {
        for (int i = 0; i < listeners.length; ++i) {
            addScriptListener(listeners[i]);
        }
    }

    public final void removeScriptListener(ScriptListener listener) {
        scriptListenerList.remove(listener);
    }

    public ScriptListener[] getScriptListeners() {
        return (ScriptListener[]) scriptListenerList.toArray(new ScriptListener[scriptListenerList.size()]);
    }

    public void clearScriptListeners() {
        scriptListenerList.clear();
    }
}
