/***************************************************************************************************
 * WINGS.AJAX  --  contains: functions used to process ajax requests
 **************************************************************************************************/

// Create module namespace
wingS.namespace("ajax");


/**
 * Requests any available component updates from the server.
 */
wingS.ajax.requestUpdates = function() {
    wingS.request.sendEvent(null, false, true);
};

/**
 * Submits the given form by means of an asynchronous request.
 * @param {Object} form - the form to be submitted
 */
wingS.ajax.submitForm = function(form) {
    var enc = "multipart/form-data";
    if (form.enctype == enc || form.encoding == enc) {
        YAHOO.util.Connect.setForm(form, true);
    } else {
        YAHOO.util.Connect.setForm(form);
    }

    wingS.ajax.sendRequest(form.method.toUpperCase(), wingS.util.getUpdateResource());
};

/**
 * Invokes an asynchronous request with the given parameters.
 * @param {String} method - the HTTP transaction method
 * @param {String} uri - the fully qualified path of resource
 * @param {String} postData - the POST body (in case of "POST")
 */
wingS.ajax.sendRequest = function(method, uri, postData) {
    wingS.ajax.requestIsActive = true;
    wingS.ajax.setActivityIndicatorsVisible(true);

    // Since some browsers cache GET requests via the XMLHttpRequest
    // object, an additional parameter called "_xhrID" will be added
    // to the request URI with a unique numeric value.
    if (method.toUpperCase() == "GET") {
        uri += ((uri.indexOf("?")>-1) ? "&" : "?");
        uri += "_xhrID=" + new Date().getTime();
    }

    wingS.ajax.connectionObject =
        YAHOO.util.Connect.asyncRequest(method, uri, wingS.ajax.callbackObject, postData);
};

/**
 * Aborts the currently active request, in case there is any.
 * @return {boolean} true if successfully aborted, false otherwise
 */
wingS.ajax.abortRequest = function() {
    if (wingS.ajax.connectionObject != null) {
        return YAHOO.util.Connect.abort(wingS.ajax.connectionObject, wingS.ajax.callbackObject);
    }
    return false;
};

/**
 * Callback method which processes any request failures.
 * @param {Object} request - the request to process
 */
wingS.ajax.processRequestFailure = function(request) {
    // Reset activity indicators and active flag
    wingS.ajax.setActivityIndicatorsVisible(false);
    wingS.ajax.requestIsActive = false;

    // Clear enqueued request
    wingS.ajax.requestQueue = new Array();

    if (request.status == -1) {
        alert("Transaction has been aborted!");
        return;
    } else if (request.status == 0) {
        // Happens in case of a communication
        // failure, i.e if the server has mean-
        // while been shut down --> do reload!
        wingS.request.reloadFrame();
        return;
    }

    // Write error message (maybe it is a wingS
    // error template) received from the server
    document.close();
    document.open("text/html");
    document.write(request.responseText);
    document.close();
};

/**
 * Callback method which processes any successful request.
 * @param {Object} request - the request to process
 */
wingS.ajax.processRequestSuccess = function(request) {
    if (wingS.global.debugMode)
        wingS.ajax.updateDebugView(request);

    // Get the received XML response
    var xmlDoc = request.responseXML;
    if (xmlDoc == null) {
        // In case we don't get any XML there is nothing more
        // what we can do here; the only thing --> do reload!
        wingS.request.reloadFrame();
        // Better?: wingS.ajax.processRequestFailure(request);
        return;
    }

    // Get the document's root element
    var xmlRoot = xmlDoc.getElementsByTagName("updates")[0];
    if (xmlRoot == null) {
        // Workaround to prevent IE from showing JS errors
        // if session has meanwhile timed out --> do reload!
        wingS.request.reloadFrame();
        // Better?: wingS.ajax.processRequestFailure(request);
        return;
    }

    // Process each incremental update
    var updates = xmlRoot.getElementsByTagName("update");
    if (updates.length > 0) {
        for (var i = 0; i < updates.length; i++) {
            try {
                // Dispatch update to the corresponding
                // handler function simply by evaluation
                window.eval(updates[i].firstChild.data);
            } catch(e) {
                var errorMsg = "Failure while processing the reponse of an AJAX request!\n" +
                               "**********************************************\n\n" +
                               "Error Message: " + e.message + "!\n\n" +
                               "The error occurred while evaluating the following JS code:\n" +
                               updates[i].firstChild.data;
                alert(errorMsg);
            }
        }
    }

    // So far we applied all updates. If there are
    // no headers downloaded asynchronously at this
    // stage, we are finished with this request and
    // ready to process the next one from the queue.
    // Otherwise there will be a callback when all
    // header downloads and header callbacks ended.
    if (wingS.global.asyncHeaderQueue.length == 0) {
        // Reset activity indicators and active flag
        wingS.ajax.setActivityIndicatorsVisible(false);
        wingS.ajax.requestIsActive = false;

        // Send the next enqueued request
        wingS.ajax.dequeueNextRequest();
    }
};

/**
 * Enqueues the given request if another one is still in action.
 * @param {Function} send - the function to send the request with
 * @param {Array} args - the arguments needed by the send function
 * @return {boolean} true, if request was enqueued, false otherwise
 */
wingS.ajax.enqueueThisRequest = function(send, args) {
    if (wingS.ajax.requestIsActive) {
        wingS.ajax.requestQueue.push( {"send" : send, "args" : args} );
        return true;
    }
    return false;
};

/**
 * Grabs the next available request from the queue and invokes it.
 */
wingS.ajax.dequeueNextRequest = function() {
    if (wingS.ajax.requestQueue.length > 0) {
        var request = wingS.ajax.requestQueue.shift();
        var args = request.args;
        request.send(args[0], args[1], args[2], args[3], args[4]);
    }
};

/**
 * Makes the activity indicators either visible or invisible.
 * @param {boolean} visible - true to set indicators visible
 */
wingS.ajax.setActivityIndicatorsVisible = function(visible) {
    if (wingS.global.updateCursor.enabled) {
        wingS.ajax.activityCursor.setVisible(visible);
        // An alternative to the cursor might be something like
        // if (visible) document.body.style.cursor = "progress";
        // else document.body.style.cursor = "default";
    }
    var indicator = document.getElementById("ajaxActivityIndicator");
    if (indicator != null) {
        if (visible) indicator.style.visibility = "visible";
        else indicator.style.visibility = "hidden";
    }
};

/**
 * Initializes the appearance of the activity cursor.
 */
wingS.ajax.ActivityCursor = function() {
    this.dx = wingS.global.updateCursor.dx;
    this.dy = wingS.global.updateCursor.dy;
    this.div = document.createElement("div");
    this.div.style.position = "absolute";
    this.div.style.zIndex = "9999";
    this.div.style.display = "none";
    this.div.innerHTML = "<img src=\"" + wingS.global.updateCursor.image + "\"/>";
    document.body.insertBefore(this.div, document.body.firstChild);
    document.onmousemove = this.followMouse.bind(this);
};

/**
 * Calculates the new position of the activity cursor.
 * @param {Object} event - the event object
 */
wingS.ajax.ActivityCursor.prototype.followMouse = function(event) {
    event = wingS.event.getEvent(event);
    var target = wingS.event.getTarget(event);

    var posX = 0;
    var posY = 0;
    if (event.pageX || event.pageY) {
        posX = event.pageX;
        posY = event.pageY;
    } else if (event.clientX || event.clientY) {
        posX = event.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
        posY = event.clientY + document.body.scrollTop + document.documentElement.scrollTop;
    }
    if (target.nodeName == "OPTION" && !wingS.util.checkUserAgent('msie')) {
        posX += wingS.util.absLeft(target);
        posY += wingS.util.absTop(target.parentNode) + 18;
    }
    
    var newX = posX + this.dx;
    if (newX > 0 && newX < (YAHOO.util.Dom.getDocumentWidth() - wingS.global.updateCursor.width - 2)) {
        this.div.style.left = newX + "px";
    }
    var newY = posY + this.dy;
    if (newY > 0 && newY < (YAHOO.util.Dom.getDocumentHeight() - wingS.global.updateCursor.height - 2)) {
	    this.div.style.top = newY + "px";
    }
};

/**
 * Sets the activity cursor either visible or invisible.
 * @param {boolean} visible - true to set cursor visible
 */
wingS.ajax.ActivityCursor.prototype.setVisible = function(visible) {
    if (visible) this.div.style.display = "block";
    else this.div.style.display = "none";
};

/**
 * Prints some debug information about the given AJAX request.
 * @param {Object} request - the request object to debug
 */
wingS.ajax.updateDebugView = function(request) {
    var debugArea = document.getElementById("ajaxDebugView");
    if (debugArea == null) {
        var debugHtmlCode =
            '<div align="center" style="margin-top:50px; padding-bottom:3px;">\n' +
            '  <strong>AJAX DEBUG VIEW:</strong> &nbsp;XML RESPONSE\n' +
            '  &nbsp;<span style="font:11px monospace"></span></div>\n' +
            '<textarea readonly="readonly" style="width:100%; height:200px;\n' +
            '  border-top:1px dashed #000000; border-bottom:1px dashed #000000;\n' +
            '  font:11px monospace; padding:10px;"></textarea>\n';
        debugArea = document.createElement("div");
        debugArea.id = "ajaxDebugView";
        debugArea.style.display = "none";
        debugArea.innerHTML = debugHtmlCode;
        document.body.appendChild(debugArea);
    }

    var output;
    if (request != null) {
        output = request.responseText;
        if (output == null)
            output = (new XMLSerializer()).serializeToString(request.responseXML);
        debugArea.getElementsByTagName("SPAN")[0].innerHTML = "| " + output.length + " chars";
    } else {
        output = "Currently there is no XML response available..." +
                 "\nProbably no asynchronous request has been sent.";
    }
    debugArea.getElementsByTagName("TEXTAREA")[0].value = output;
};

/**
 * Returns the current visibility state of the debug view.
 * @return {boolean} true if view is visible, false otherwise
 */
wingS.ajax.isDebugViewVisible = function() {
    var debugArea = document.getElementById("ajaxDebugView");
    if (debugArea != null && debugArea.style.display != "none") {
        return true;
    }
    return false;
};

/**
 * Makes the enabled debug view either visible or invisible.
 * @param {boolean} visible - true to set debug view visible
 */
wingS.ajax.setDebugViewVisible = function(visible) {
    var debugArea = document.getElementById("ajaxDebugView");
    if (debugArea != null) {
        if (visible) debugArea.style.display = "block";
        else debugArea.style.display = "none";
    } else {
        wingS.ajax.updateDebugView();
        wingS.ajax.setDebugViewVisible(true);
    }
};

/**
 * Toggles the visibility of the previously enabled debug view.
 */
wingS.ajax.toggleDebugView = function() {
    if (wingS.ajax.isDebugViewVisible())
        wingS.ajax.setDebugViewVisible(false);
    else wingS.ajax.setDebugViewVisible(true);
};

