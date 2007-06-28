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
wingS.global.debugMode = false;      // This flag might be set in order to control debug outputs
wingS.global.asyncHeaderCount = 0;   // Count of headers which are currently loaded asynchronously
wingS.global.asyncHeaderQueue = [];  // Queue of functions each of which downloads an async header
wingS.global.asyncHeaderCalls = [];  // Callbacks which are invoked when all headers are available

/**
 * Callback method which initializes the current frame. This method is called upon each reload.
 * @param {String} eventEpoch - keeps the event epoch of this frame (needed for all events)
 * @param {String} reloadResource - stores the URI of the ReloadResource (without the event epoch)
 * @param {String} updateResource - stores the URI of the UpdateResource (without the event epoch)
 * @param {boolean} updateEnabled - a flag indicating if this frame allows incremental updates
 * @param {Object} updateCursor - an object holding necessary settings of the update cursor
 */
wingS.global.init =  function(eventEpoch, reloadResource, updateResource, updateEnabled, updateCursor) {
    // Initialize -wingS.global-
    wingS.global.eventEpoch = eventEpoch;
    wingS.global.reloadResource = reloadResource;
    wingS.global.updateResource = updateResource;
    wingS.global.updateEnabled = updateEnabled;
    wingS.global.updateCursor = updateCursor;

    // Initialize -wingS.ajax-
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
wingS.global.onHeadersLoaded = function(callback) {
    if (wingS.global.asyncHeaderCount == 0 &&
        wingS.global.asyncHeaderQueue.length == 0) {
        // If there is no header download going on we
        // are free to invoke this callback directly.
        callback();
    } else {
        // Otherwise we have to enqueue this callback
        wingS.global.asyncHeaderCalls.push(callback);
    }
};

/**
 * Increases a counter which indicates the number of headers (asynchronously) loaded at the moment.
 */
wingS.global.startLoadingHeader = function() {
    wingS.global.asyncHeaderCount++;
};

/**
 * Decreases a counter which indicates the number of headers (asynchronously) loaded at the moment.
 */
wingS.global.finishedLoadingHeader = function() {
    if (wingS.global.asyncHeaderCount > 0) {
        // Only if something is going on
        wingS.global.asyncHeaderCount--;
        wingS.global.dequeueNextHeader();
    }
};

/**
 * Enqueues the given header download if another one is still in action.
 * @param {Function} load - the function to load the header with
 * @param {Array} args - the arguments needed by the load function
 * @return {boolean} true, if header was enqueued, false otherwise
 */
wingS.global.enqueueThisHeader = function(load, args) {
    if (wingS.global.asyncHeaderCount > 0) { // Load one header after another
        // This is because header 2 might require header 1 to be fully loaded
        wingS.global.asyncHeaderQueue.push( {"load" : load, "args" : args} );
        return true;
    }
    return false;
};

/**
 * Grabs the next available header download from the queue and starts it.
 */
wingS.global.dequeueNextHeader = function() {
    if (wingS.global.asyncHeaderQueue.length > 0) {
        // Remove and start first enqueued header download
        var header = wingS.global.asyncHeaderQueue.shift();
        var args = header.args;
        header.load(args[0], args[1], args[2], args[3]);
    } else {
        // Invoke all callback methods which have registered interest
        for (var i = 0; i < wingS.global.asyncHeaderCalls.length; i++) {
            wingS.global.asyncHeaderCalls[i]();
        }
        // Clear all callbacks upon each new request
        wingS.global.asyncHeaderCalls = new Array();

        // Reset activity indicators and active flag
        wingS.ajax.setActivityIndicatorsVisible(false);
        wingS.ajax.requestIsActive = false;

        // Send the next enqueued request
        wingS.ajax.dequeueNextRequest();
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

