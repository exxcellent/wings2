/*
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://wingsframework.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings;


/**
 * A basic icon-text compound
 * where you can set an event by hand or which could be
 * used as a base class to extend from.
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class SClickable
        extends SAbstractClickable {

    /**
     * if this is set (!=null) this event is rendered as anchor. If it is null,
     * the anchor in the AnchorRenderStack is rendered
     */
    private String event;

    /**
     * if this is set (!=null) this event is rendered as anchor. If it is null,
     * the anchor in the AnchorRenderStack is rendered
     */
    private LowLevelEventListener requestTarget;

    /**
     * Creates a new <code>SClickable</code> instance with the specified text
     * (left alligned) and no icon.
     *
     * @param text The text to be displayed by the label.
     */
    public SClickable(String text) {
        this(text, null, SConstants.LEFT);
    }

    /**
     * Creates a new <code>SClickable</code> instance with no text and no icon.
     */
    public SClickable() {
        this((String) null);
    }

    /**
     * Creates a new <code>SClickable</code> instance with the specified icon
     * (left alligned) and no text.
     *
     * @param icon The image to be displayed by the label.
     */
    public SClickable(SIcon icon) {
        this(icon, SConstants.LEFT);
    }

    /**
     * Creates a new <code>SClickable</code> instance with the specified icon
     * (alligned as specified) and no text.
     *
     * @param icon                The image to be displayed by the clickable.
     * @param horizontalAlignment One of the following constants defined in
     *                            <code>SConstants</code>:
     *                            <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>.
     * @see SConstants
     */
    public SClickable(SIcon icon, int horizontalAlignment) {
        this(null, icon, horizontalAlignment);
    }

    /**
     * Creates a new <code>SClickable</code> instance with the specified icon
     * and the specified text (left alligned).
     *
     * @param text The text to be displayed by the SClickable.
     * @param icon The image to be displayed by the SClickable.
     */
    public SClickable(String text, SIcon icon) {
        setText(text);
        setIcon(icon);
        setHorizontalAlignment(SConstants.LEFT);
    }

    /**
     * Creates a new <code>SClickable</code> instance with the specified icon
     * and the specified text (alligned as specified).
     *
     * @param text                The text to be displayed by the SClickable.
     * @param icon                The image to be displayed by the SClickable.
     * @param horizontalAlignment One of the following constants defined in
     *                            <code>SConstants</code>:
     *                            <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>.
     * @see SConstants
     */
    public SClickable(String text, SIcon icon, int horizontalAlignment) {
        setText(text);
        setIcon(icon);
        setHorizontalAlignment(horizontalAlignment);
    }

    /**
     * Creates a new <code>SClickable</code> instance with the specified text
     * (alligned as specified) and no icon.
     *
     * @param text                The text to be displayed by the SClickable.
     * @param horizontalAlignment One of the following constants defined in
     *                            <code>SConstants</code>:
     *                            <code>LEFT</code>, <code>CENTER</code>, <code>RIGHT</code>.
     * @see SConstants
     */
    public SClickable(String text, int horizontalAlignment) {
        this(text, null, horizontalAlignment);
    }

    public boolean isEpochCheckEnabled() {
        return requestTarget == null ? true : requestTarget.isEpochCheckEnabled();
    }

    /**
     * if this is set (!=null) this event is rendered as anchor. If it is null,
     * the anchor in the AnchorRenderStack is rendered
     */
    public final void setEvent(String event, LowLevelEventListener requestTarget) {
        setEvent(event);
        setEventTarget(requestTarget);
    }

    /**
     * if this is set (!=null) this event is rendered as anchor. If it is null,
     * the anchor in the AnchorRenderStack is rendered
     */
    public void setEvent(String e) {
        if (isDifferent(event, e)) {
            event = e;
            reload();
        }
    }

    /**
     * if this is set (!=null) this event is rendered as anchor. If it is null,
     * the anchor in the AnchorRenderStack is rendered
     */
    public final String getEvent() {
        return event;
    }

    /**
     * sets the LowLevelEventListener for which to create a event.
     */
    public void setEventTarget(LowLevelEventListener t) {
        if (isDifferent(requestTarget, t)) {
            requestTarget = t;
            reload();
        }
    }

    /**
     * if this is set (!=null) this event is rendered as anchor. If it is null,
     * the anchor in the AnchorRenderStack is rendered
     */
    public final LowLevelEventListener getEventTarget() {
        return requestTarget;
    }

    public SimpleURL getURL() {
        if (getEvent() != null && getEventTarget() != null) {
            RequestURL u = getRequestURL();
            if (!isEpochCheckEnabled()) {
                u.setEventEpoch(null);
                u.setResource(null);
            }

            u.addParameter(getEventTarget(),
                    getEvent());
            return u;
        } else {
            return null;
        }
    }


}


