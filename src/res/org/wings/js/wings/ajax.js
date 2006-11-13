/***************************************************************************************************
 * WINGS.AJAX  --  contains: functions used to process ajax requests
 **************************************************************************************************/


var requestIsActive;    // Indicates if a request is active
var requestQueue;       // A queue of requests to be invoked
var activityCursor;     // The activity cursor (animated GIF)
var callbackObject;     // Object defining callback functions
var connectionObject;   // Holds the active connection object

/**
 * Callback method that is called when the frame is loaded.
 */
wingS.ajax.initializeFrame = function() {
    requestIsActive = false;
    requestQueue = new Array();
    activityCursor = new wingS.ajax.ActivityCursor();
    callbackObject = {
        success : function(request) { wingS.ajax.processRequestSuccess(request); },
        failure : function(request) { wingS.ajax.processRequestFailure(request); }
    };
    wingS.ajax.setActivityIndicatorsVisible(false);
};

/**
 * Requests any available component updates from the server.
 */
wingS.ajax.requestUpdates = function() {
    wingS.ajax.doRequest("GET", wingS.util.getIncrementalUpdateResource());
};

/**
 * Submits the given form by means of an asynchronous request.
 * @param {Object} form - the form to be submitted
 */
wingS.ajax.doSubmit = function(form) {
    YAHOO.util.Connect.setForm(form);
    wingS.ajax.doRequest(form.method.toUpperCase(), wingS.util.getIncrementalUpdateResource());
};

/**
 * Invokes an asynchronous request with the given parameters.
 * @param {String} method - the HTTP transaction method
 * @param {String} uri - the fully qualified path of resource
 * @param {String} postData - the POST body (in case of "POST")
 */
wingS.ajax.doRequest = function(method, uri, postData) {
    requestIsActive = true;
    wingS.ajax.setActivityIndicatorsVisible(true);

    // Since some browsers cache GET requests via the XMLHttpRequest
    // object, an additional parameter called "_xhrID" will be added
    // to the request URI with a unique numeric value.
    if (method.toUpperCase() == "GET") {
        uri += ((uri.indexOf("?")>-1) ? "&" : "?");
        uri += "_xhrID=" + new Date().getTime();
    }

    connectionObject = YAHOO.util.Connect.asyncRequest(method, uri, callbackObject, postData);
};

/**
 * Aborts the currently active request, in case there is any.
 * @return {boolean} true if successfully aborted, false otherwise
 */
wingS.ajax.abortRequest = function() {
    if (connectionObject != null) {
        return YAHOO.util.Connect.abort(connectionObject, callbackObject);
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
    requestIsActive = false;

    // Clear enqueued request
    requestQueue = new Array();

    if (request.status == -1) {
        alert("Transaction aborted!");
        return;
    } else if (request.status == 0) {
        alert("Communication failure!");
        return;
    }

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
    wingS.ajax.updateDebugView(request);

    // Get the received XML response
    var xmlDoc = request.responseXML;
    // In case we do not get any XML
    if (xmlDoc == null) {
        window.location.href = wingS.util.getCompleteUpdateResource();
        // Alternative? --> wingS.ajax.processRequestFailure(request);
        return;
    }

    // Get the root element of the received XML response
    var xmlRoot = xmlDoc.getElementsByTagName("update")[0];
    // Workaround to prevent IE from showing JS errors when
    // session has meanwhile timed out -> try a full reload
    if (xmlRoot == null) {
        window.location.href = wingS.util.getCompleteUpdateResource();
        // Alternative? --> wingS.ajax.processRequestFailure(request);
        return;
    }

    // Private convenience function
    function getFirstChildData(tagName) {
        return xmlDoc.getElementsByTagName(tagName)[0].firstChild.data;
    }

    // Process the response depending on the update mode
    var updateMode = xmlRoot.getAttribute("mode");
    if (updateMode == "complete") {
        window.location.href = getFirstChildData("redirect");
        return;
    }
    else if (updateMode == "incremental") {
        var components = xmlRoot.getElementsByTagName("component");
        if (components.length > 0) {
            var componentIds = new Array();
            // Replace HTML needed for component updates
            for (var i = 0; i < components.length; i++) {
                var id = components[i].getAttribute("id");
                var html = components[i].firstChild.data;
                wingS.util.replaceComponentHtml(id, html);
                componentIds.push(id);
            }
            // Execute scripts needed for component updates
            var scripts = xmlRoot.getElementsByTagName("script");
            for (var i = 0; i < scripts.length; i++) {
                var code = scripts[i].firstChild.data;
                try {
                    eval(code);
                } catch(e) {
                    var errorMsg = "An error occured while processing an AJAX request!\n" +
                                   ">> " + e.message + "\n\n\n The following JavaScript " +
                                   "code could not be executed:\n" + code;
                    alert(errorMsg);
                }
            }
            // Hightlight the components updated above
            if (wingS.global.incrementalUpdateHighlight.enabled) {
                wingS.ajax.highlightComponentUpdates(componentIds);
            }

            // Update the event epoch of this frame
            wingS.global.event_epoch = getFirstChildData("event_epoch");
        }
    }

    // Reset activity indicators and active flag
    wingS.ajax.setActivityIndicatorsVisible(false);
    requestIsActive = false;

    // Send the next enqueued request
    wingS.ajax.dequeueNextRequest();
};

/**
 * Replaces the HTML code of the component with the given ID.
 * @param {String} id - the ID of the component to replace
 * @param {String} html - the new HTML code of the component
 */
wingS.util.replaceComponentHtml = function(id, html) {
    var component = document.getElementById(id);
    if (component == null) return;

    // Handle layout workaround for IE (if necessary)
    var wrapping = component.getAttribute("wrapping");
    if (wrapping != null && !isNaN(wrapping)) {
        for (var i = 0; i < parseInt(wrapping); i++) {
            component = component.parentNode;
        }
    }

    if (typeof component.outerHTML != "undefined") {
        // Use outerHTML if available
        component.outerHTML = html;
    } else {
        var parent = component.parentNode;
        if (parent == null) return;

        var nrOfChildElements = 0;
        for (var i = 0; i < parent.childNodes.length; i++) {
            // We have to filter everything except element nodes
            // since browsers treat whitespace nodes differently
            if (parent.childNodes[i].nodeType == 1) {
                nrOfChildElements++;
            }
        }

        if (nrOfChildElements == 1) {
            // If there is only one child it must be our component
            parent.innerHTML = html;
        } else {
            var range;
            // If there is no other way we have to use proprietary methods
            if (document.createRange && (range = document.createRange()) &&
                range.createContextualFragment) {
                range.selectNode(component);
                var newComponent = range.createContextualFragment(html);
                parent.replaceChild(newComponent, component);
            }
        }
    }
};

/**
 * Enqueues the given request if another one is still in action.
 * @param {Function} send - the function to send the request with
 * @param {Array} args - the arguments needed by the send function
 * @return {boolean} true if request was enqueued, false otherwise
 */
wingS.ajax.enqueueThisRequest = function(send, args) {
    if (requestIsActive) {
        requestQueue.push( {"send" : send, "args" : args} );
        return true;
    }
    return false;
};

/**
 * Grabs the next available request from the queue and invokes it.
 */
wingS.ajax.dequeueNextRequest = function() {
    if (requestQueue.length > 0) {
        var request = requestQueue.shift();
        var args = request.args;
        // Fifth argument might be "undefined", but that's ok
        request.send(args[0], args[1], args[2], args[3], args[4]);
    }
};

/**
 * Makes the activity indicators either visible or invisible.
 * @param {boolean} visible - true to set indicators visible
 */
wingS.ajax.setActivityIndicatorsVisible = function(visible) {
    if (wingS.global.incrementalUpdateCursor.enabled) {
        activityCursor.setVisible(visible);
    }
    var indicator = document.getElementById("ajaxActivityIndicator");
    if (indicator != null) {
        if (visible) indicator.style.visibility = "visible";
        else indicator.style.visibility = "hidden";
    }
};

/**
 * Briefly highlights the components with the given IDs.
 * @param {Array} componentIds - the IDs of the components
 */
wingS.ajax.highlightComponentUpdates = function(componentIds) {
    for (var i = 0; i < componentIds.length; i++) {
        var component = document.getElementById(componentIds[i]);
        if (component == null) return;
        highlightComponent(component);
    }

    function highlightComponent(component) {
        var initialBackgroundColor = component.style.backgroundColor;
        component.style.backgroundColor = wingS.global.incrementalUpdateHighlight.color;
        var resetColor = function() {
            component.style.backgroundColor = initialBackgroundColor;
        };
        setTimeout(resetColor, wingS.global.incrementalUpdateHighlight.duration);
    }
};

/**
 * Initializes the appearance of the activity cursor.
 */
wingS.ajax.ActivityCursor = function() {
    this.dx = wingS.global.incrementalUpdateCursor.dx;
    this.dy = wingS.global.incrementalUpdateCursor.dy;
    this.div = document.createElement("div");
    this.div.style.position = "absolute";
    this.div.style.zIndex = "1000";
    this.div.style.display = "none";
    this.div.innerHTML = "<img src=\"" + wingS.global.incrementalUpdateCursor.image + "\"/>";
    document.body.insertBefore(this.div, document.body.firstChild);
    document.onmousemove = this.followMouse.bind(this);
};

/**
 * Calculates the new position of the activity cursor.
 * @param {Object} event - the event object
 */
wingS.ajax.ActivityCursor.prototype.followMouse = function(event) {
    event = wingS.events.getEvent(event);
    var target = wingS.events.getTarget(event);
    var pos = { left : event.clientX, top : event.clientY };
    var doc = (window.document.compatMode && window.document.compatMode == "CSS1Compat") ?
        window.document.documentElement : window.document.body || null;
    if (doc) {
        pos.left += doc.scrollLeft;
        pos.top += doc.scrollTop;
    }
    if (target.nodeName == "OPTION" && !wingS.util.checkUserAgent('msie')) {
        pos.left += document.defaultView.getComputedStyle(target, null).getPropertyValue("left");
        pos.top += document.defaultView.getComputedStyle(target, null).getPropertyValue("top");
    }
    this.div.style.left = pos.left + this.dx + "px";
    this.div.style.top = pos.top + this.dy + "px";
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
 * @param {Object} request - the request to debug
 */
wingS.ajax.updateDebugView = function(request) {
    var debugArea = document.getElementById("ajaxDebugView");
    if (debugArea == null) {
        var debugHtmlCode =
            '<div style="margin-top:50px; padding-bottom:3px;">\n' +
            '  <strong>AJAX DEBUG VIEW:</strong> &nbsp;XML RESPONSE\n' +
            '  &nbsp;<span style="font:11px monospace"></span></div>\n' +
            '<textarea readonly="readonly" style="width:100%; height:200px;\n' +
            '  border-top:1px dashed #000000; border-bottom:1px dashed #000000;\n' +
            '  font:11px monospace;"></textarea>\n';
        debugArea = document.createElement("div");
        debugArea.id = "ajaxDebugView";
        debugArea.style.display = "none";
        debugArea.innerHTML = debugHtmlCode;
        document.body.appendChild(debugArea);
    }
    var txt = request.responseText;
    debugArea.getElementsByTagName("TEXTAREA")[0].value = txt;
    debugArea.getElementsByTagName("SPAN")[0].innerHTML = "| " + txt.length + " chars";
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
        alert("The AJAX debug view has not been enabled yet!");
    }
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