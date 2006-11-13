/***************************************************************************************************
 * WINGS.GLOBAL  --  contains: namespaces, global variables, etc.
 **************************************************************************************************/


/**
 * JavaScript namespaces
 */
if (!wingS) {
    var wingS = new Object();
} else if (typeof wingS != "object") {
    throw new Error("wingS already exists and is not an object");
}

if (!wingS.global) {
    wingS.global = new Object();
} else if (typeof wingS.global != "object") {
    throw new Error("wingS.global already exists and is not an object");
}

if (!wingS.events) {
    wingS.events = new Object();
} else if (typeof wingS.events != "object") {
    throw new Error("wingS.events already exists and is not an object");
}

if (!wingS.util) {
    wingS.util = new Object();
} else if (typeof wingS.util != "object") {
    throw new Error("wingS.util already exists and is not an object");
}

if (!wingS.layout) {
    wingS.layout = new Object();
} else if (typeof wingS.layout != "object") {
    throw new Error("wingS.layout already exists and is not an object");
}

if (!wingS.request) {
    wingS.request = new Object();
} else if (typeof wingS.request != "object") {
    throw new Error("wingS.request already exists and is not an object");
}

if (!wingS.ajax) {
    wingS.ajax = new Object();
} else if (typeof wingS.ajax != "object") {
    throw new Error("wingS.ajax already exists and is not an object");
}

if (!wingS.component) {
    wingS.component = new Object();
} else if (typeof wingS.component != "object") {
    throw new Error("wingS.component already exists and is not an object");
}

/**
 * Moves the execution context of the function used upon to the given object. Useful when using
 * setTimeout or event handling, e.g.: setTimeout(func1.bind(someObject), 1); The function func1
 * will be called within the context of someObject. NB: Function object is extended by bind()!
 *
 * @param {Object} obj new execution context
 */
Function.prototype.bind = function(obj) {
    var method = this;
    temp = function() {
        return method.apply(obj, arguments);
    };

    return temp;
};


/**
 * Global variables
 */
wingS.global.event_epoch;                    // Maintains the event epoch of this frame
wingS.global.completeUpdateId;               // Holds the ID of the CompleteUpdateResource
wingS.global.incrementalUpdateId;            // Holds the ID of the IncrementalUpdateResource
wingS.global.incrementalUpdateEnabled;       // True if this frame allows incremental updates
wingS.global.incrementalUpdateCursor;        // An object whose properties "enabled", "image"
                                             // "dx" and "dy" hold the settings of the cursor
wingS.global.incrementalUpdateHighlight;     // An object whose properties "enabled", "color"
                                             // and "duration" store the 3 highlight settings