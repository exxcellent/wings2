/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
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
 * The LowLevelEventListener interface is implemented by all components
 * that take part at the event dispatching process.
 * WingS event dispatching is complex. This is because we have to process many
 * requests at once (asynchronous user interaction). There are three levels of
 * dispatching:
 * <ol>
 * <li>process http requests ({@link #processLowLevelEvent}): If a component is
 * registered at the session specific 
 * {@link org.wings.session.LowLevelEventDispatcher} it gets all the 
 * parameters is is registered for. This parameter consists of a name-value
 * pair. Most time the component itself has encoded this parameter, so it is
 * able to decode it and change its internal state. This should be done in
 * {@link #processLowLevelEvent}. Be careful, the change of the internal state shold
 * not trigger any events, because in case of a form request, many requests are
 * processed and many states of components are changed, so if you trigger an
 * event, the listener may access a component which has not yet processed its
 * request parameters and so it is in an inconsistent state.
 * </li>
 * <li>fire intermediate events: fire events which describes a "in progress"
 * state change, like TreeWillExpand, or ListSelectionEvent with
 * getIsAdjusting() true, ...
 * After this level are components must be in a consistant state
 * </li>
 * <li>fire final events: fire remaining events. In this level all events, which
 * are important to an application should be fired. All listeners, which are
 * notified in this level can assume that the components are in a consistent
 * (considering user interaction) state.
 * </li>
 * </ol>
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public interface LowLevelEventListener
{
    /**
     * Deliver low level/http events (parameters).
     * The name-value-pairs of the HTTPRequest are considered low level events.
     * @param name the name-value-pair's name
     * @param value the name-value-pair's value
     */
    void processLowLevelEvent(String name, String[] values);

    /**
     * The id of an event which will be forwarded by the dispatcher to this
     * component for processing. A component is registered at the 
     * {@link org.wings.session.LowLevelEventDispatcher dispatcher}. This
     * dispatcher forwards servlet parameters (low level events) to each
     * LowLevelEventListener registered for this event. A LowLevelEventListener
     * is registered for an event, if the event id is equals to the result of
     * this method.
     */
    String getLowLevelEventId();

    /**
     * Get the encoded low level event id. This is a convenience method for 
     * SComponent.encodeLowLevelEventId(getLowLevelEventId());
     * Encodes a low level event id for using it in a request parameter. Every 
     * {@link LowLevelEventListener} should encode its LowLevelEventId before
     * using it in a request parameter. This encoding adds consistency checking
     * for outtimed requests ("Back Button"). Mainly used in CG's
     */
    String getEncodedLowLevelEventId();

    /**
     * If the dispatcher is configured to use named event, the return value of
     * this method is used to identiy a LowLevelEventListener by name. E.g. in a
     * http request you might give an action a special name, like
     * "ActivateUpload" to make uploads possible. This action is a SButton in
     * wings with the name "ActivateUpload" and an ActionListener which make the
     * upload form visible. A designer might call your servlet with 
     * "servlet/_?ActivateUpload=1" to make the upload form visible by hand. Be
     * careful, this so called "Named Events" are not under control of wings, so
     * they will no be outtimed and might lead to strange effects.
     */
    String getName();

    /**
     * fire events which describes a "in progress"
     * state change, like TreeWillExpand, or ListSelectionEvent with
     * getIsAdjusting() true, ...
     */
    void fireIntermediateEvents();

    /**
     * fire remaining events. In this level all events, which
     * are important to an application should be fired. All listeners, which are
     * notified in this level can assume that the components are in a consistent
     * (considering user interaction) state.
     */
    void fireFinalEvents();

    boolean isEnabled();

    boolean checkEpoch();
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
