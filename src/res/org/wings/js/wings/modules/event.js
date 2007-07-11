/***************************************************************************************************
 * WINGS.EVENT  --  contains: functions used to do event handling
 **************************************************************************************************/

// Create module namespace
wingS.namespace("event");


wingS.event.getEvent = function(event) {
    if (window.event)
        return window.event;
    else
        return event;
};

wingS.event.getTarget = function(event) {
    var target;
    if (event.srcElement)
        // according to IE
        target = event.srcElement;
    else if (event.target)
        // according to W3C
        target = event.target;
    if (target.nodeType == 3)
        // defeat Safari bug
        target = target.parentNode;
    return target;
};

/**
 * Cross-browser method to register an event listener on the passed object. Only Mozilla will
 * support captive mode of event handling. The 'eventType' is without the 'on'-prefix.
 * Example: wingS.event.registerEvent(document,'focus',storeFocus,false);
 *
 * Deprecated! Use YAHOO.util.Event.addListener() instead of this function.
 */
wingS.event.registerEvent = function(obj, eventType, func, useCaption) {
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

