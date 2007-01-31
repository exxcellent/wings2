package org.wings;

import java.util.List;
import java.util.Set;
import java.io.Serializable;

import org.wings.plaf.Update;

/**
 * A reload manger is responsible for managing reloads and updates of components as
 * well as for invalidating the epoch of frames whose contained components changed.
 *
 * @author Stephan Schuster
 * @version $Revision$
 */
public interface ReloadManager extends Serializable {

    /**
     * Marks an entire component change.
     * @param component  the component that changed
     */
    void reload(SComponent component);

    /**
     * Inserts an update for a component.
     * @param component  the component that changed
     * @param update  the update for this component
     */
    void addUpdate(SComponent component, Update update);

    /**
     * Returns a (filtered) list of all updates.
     * @return a list of all needed updates
     */
    List getUpdates();

    /**
     * Returns a set of all components marked dirty.
     * @return a set of all dirty components
     */
    Set getDirtyComponents();

    /**
     * Return a set of all frames marked dirty.
     * @return a set of all dirty frames
     */
    Set getDirtyFrames();

    /**
     * Invalidates all frames containing dirty components.
     */
    void invalidateFrames();

    /**
     * Notifies the CG's of dirty components about changes.
     */
    void notifyCGs();

    /**
     * Clears all requested reloads and updates.
     */
    void clear();

    /**
     * Returns the current operation mode.
     * @return true if in update mode
     */
    boolean isUpdateMode();

    /**
     * Sets the current operation mode.
     * @param enabled  true to enable update mode
     */
    void setUpdateMode(boolean enabled);

    /**
     * Returns a reload suggestion.
     * @return true if a reload is required
     */
    boolean isReloadRequired(SFrame frame);

}
