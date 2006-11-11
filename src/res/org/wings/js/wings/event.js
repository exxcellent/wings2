/***************************************************************************************************
 * WINGS.EVENT  --  contains: functions used to do event handling
 **************************************************************************************************/


wingS.events.getEvent = function(event) {
    if (window.event)
        return window.event;
    else
        return event;
};

wingS.events.getTarget = function(event) {
    if (event.srcElement)
        return event.srcElement; // IE
    else
        return event.target; // W3C
};

/**
 * Cross-browser method to register an event listener on the passed object. Only Mozilla will
 * support captive mode of event handling. The 'eventType' is without the 'on'-prefix.
 * Example: wingS.events.registerEvent(document,'focus',storeFocus,false);
 *
 * Deprecated! Use YAHOO.util.Event.addListener() instead of this function.
 */
wingS.events.registerEvent = function(obj, eventType, func, useCaption) {
    if (obj.addEventListener) {
        obj.addEventListener(eventType, func, useCaption);
        return true;
    } else if (obj.attachEvent) {
        var retVal = object.attachEvent("on" + eventType, func);
        return retVal;
    } else {
        return false;
    }
};