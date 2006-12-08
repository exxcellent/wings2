/***************************************************************************************************
 * WINGS.GLOBAL  --  contains: global variables, function shortcuts and extensions, etc.
 **************************************************************************************************/


/**
 * Create according namespace
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

/**
 * Global variables
 */
wingS.global.eventEpoch;          // Keeps the event epoch of this frame (needed for all events)
wingS.global.reloadResource;      // Stores the URI of the ReloadResource (without event epoch)
wingS.global.updateResource;      // Stores the URI of the UpdateResource (without event epoch)
wingS.global.updateEnabled;       // A flag indicating if this frame allows incremental updates
wingS.global.updateCursor;        // An object holding necessary settings of the update cursor

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