/***************************************************************************************************
 * WINGS.GLOBAL  --  contains: global variables and functions, function extensions, etc.
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
wingS.global.debugMode = true;      // This flag might be set to control debug outputs accordingly
wingS.global.headerLoadCount = 0;   // Count of headers which are currently loaded asynchronously
wingS.global.headerCallbacks = [];  // Callbacks which are invoked when all headers are available

/**
 * Callback method which initializes the current frame. This method is called upon each reload.
 * @param {String} eventEpoch - keeps the event epoch of this frame (needed for all events)
 * @param {String} reloadResource - stores the URI of the ReloadResource (without the event epoch)
 * @param {String} updateResource - stores the URI of the UpdateResource (without the event epoch)
 * @param {boolean} updateEnabled - a flag indicating if this frame allows incremental updates
 * @param {Object} updateCursor - an object holding necessary settings of the update cursor
 */
wingS.global.init =  function(eventEpoch, reloadResource, updateResource, updateEnabled, updateCursor) {

    wingS.global.eventEpoch = eventEpoch;
    wingS.global.reloadResource = reloadResource;
    wingS.global.updateResource = updateResource;
    wingS.global.updateEnabled = updateEnabled;
    wingS.global.updateCursor = updateCursor;

    wingS.ajax.requestIsActive = false;
    wingS.ajax.requestQueue = new Array();
    if (wingS.global.updateCursor.enabled) {
        wingS.ajax.activityCursor = new wingS.ajax.ActivityCursor();
    }
    wingS.ajax.callbackObject = {
        success : function(request) { wingS.ajax.processRequestSuccess(request); },
        failure : function(request) { wingS.ajax.processRequestFailure(request); },
        upload  : function(request) { wingS.ajax.processRequestSuccess(request); }
    };
    wingS.ajax.setActivityIndicatorsVisible(false);
};

/**
 * Adds a callback function which is invoked when all (asynchronously loaded) headers are available.
 * @param {Function} callback - the callback function to invoke
 */
wingS.global.onHeadersAvailable = function(callback) {
    if (wingS.global.headerLoadCount == 0) callback();
    else wingS.global.headerCallbacks.push(callback);
};

/**
 * Increases a counter which indicates the number of headers (asynchronously) loaded at the moment.
 */
wingS.global.startLoadingHeader = function() {
    wingS.global.headerLoadCount++;
};

/**
 * Decreases a counter which indicates the number of headers (asynchronously) loaded at the moment.
 */
wingS.global.finishedLoadingHeader = function() {
    if (wingS.global.headerLoadCount > 0) {
        wingS.global.headerLoadCount--;
        if (wingS.global.headerLoadCount == 0) {
            for (var i = 0; i < wingS.global.headerCallbacks.length; i++) {
                wingS.global.headerCallbacks[i]();
            }
            wingS.global.headerCallbacks = new Array();
        }
    }
};

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

