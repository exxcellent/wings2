/***************************************************************************************************
 * WINGS.AJAX  --  contains: functions used to process ajax requests
 **************************************************************************************************/

wingS.ajax.getUpdates = function() {
    wingS.request.followLink(true);
};

wingS.ajax.initAjaxCallbacks = function() {
    return {
        "onLoading" : function(request) { wingS.ajax.showAjaxActivityIndicator(); },
        "onSuccess" : function(request) { wingS.ajax.processAjaxRequest(request); },
        "onError"   : function(request) { wingS.ajax.handleRequestError(request); }
    };
};

wingS.ajax.doAjaxSubmit = function(form) {
    var requestObject = wingS.ajax.initAjaxCallbacks();
    return AjaxRequest.submit(form, requestObject);
};

wingS.ajax.doAjaxRequest = function(args) {
    var requestObject = wingS.ajax.initAjaxCallbacks();
    // Extend basic request object with arguments
    for (var i in args) requestObject[i] = args[i];

    // Use POST unless the request object defines GET
    if (typeof requestObject.method == "string" &&
        requestObject.method.toUpperCase() == "GET") {
        AjaxRequest.get(requestObject);
    } else {
        AjaxRequest.post(requestObject);
    }
};

/**
 * Prints some hidden debugging infos about the given AJAX request at the bottom of the page.
 */
wingS.ajax.enableAjaxDebugging = function(request) {
    var debug = document.getElementById("ajaxDebugging");
    if (debug == null) {
        var debugHtmlCode =
            '<div style="margin-top:50px; padding-bottom:3px;">\n' +
            '  <strong>AJAX DEBUGGING:</strong> &nbsp;XML RESPONSE\n' +
            '  &nbsp;<span style="font:11px monospace"></span></div>\n' +
            '<textarea readonly="readonly" style="width:100%; height:200px;\n' +
            '  border-top:1px dashed #000000; border-bottom:1px dashed #000000;\n' +
            '  font:11px monospace;"></textarea>\n';
        debug = document.createElement("div");
        debug.id = "ajaxDebugging";
        debug.style.display = "none";
        debug.innerHTML = debugHtmlCode;
        document.body.appendChild(debug);
    }
    var txt = request.responseText;
    debug.getElementsByTagName("TEXTAREA")[0].value = txt;
    debug.getElementsByTagName("SPAN")[0].innerHTML = "| " + txt.length + " chars";
};

/**
 * This function shows the previously enabled AJAX debugging view, if available.
 */
wingS.ajax.showAjaxDebugging = function() {
    var debug = document.getElementById("ajaxDebugging");
    if (debug != null) debug.style.display = "block";
};

/**
 * This function hides the previously enabled AJAX debugging view, if available.
 */
wingS.ajax.hideAjaxDebugging = function() {
    var debug = document.getElementById("ajaxDebugging");
    if (debug != null) debug.style.display = "none";
};

wingS.ajax.handleRequestError = function(request) {
    //var errorMsg = "An error occured while processing an AJAX request!\n" +
    //               ">> " + request.statusText + " (" + request.status + ")";
    //wingS.ajax.hideAjaxActivityIndicator();
    //alert(errorMsg);
    //window.location.href = wingS.util.encodeUpdateId(wingS.global.completeUpdateId);
    //dequeueNextRequest();

    wingS.ajax.hideAjaxActivityIndicator();
    document.close();
    document.open("text/html");
    document.write(request.responseText);
    document.close();
};

wingS.ajax.processAjaxRequest = function(request) {
    wingS.ajax.enableAjaxDebugging(request);

    // Get the received XML response
    var xmlDoc = request.responseXML;
    // In case we do not get any XML
    if (xmlDoc == null) {
        wingS.ajax.hideAjaxActivityIndicator();
        //alert("DEBUG: WHAT SHALL WE DO HERE?");
        window.location.href = request.url;
        return;
    }

    // Get the root element of the received XML response
    var xmlRoot = xmlDoc.getElementsByTagName("update")[0];
    // Workaround to prevent IE from showing JS errors when
    // session has meanwhile timed out -> try a full reload
    if (xmlRoot == null) {
        wingS.ajax.hideAjaxActivityIndicator();
        window.location.href = wingS.util.encodeUpdateId(wingS.global.completeUpdateId);
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
            // Update the event epoch of this frame
            wingS.global.event_epoch = getFirstChildData("event_epoch");
            // Hightlight the components updated above
            if (wingS.global.incrementalUpdateHighlight.enabled) {
                wingS.ajax.highlightComponentUpdates(componentIds);
            }
        }
    }
    wingS.ajax.hideAjaxActivityIndicator();
    // Send next queued request
    wingS.request.dequeueNextRequest();
};

/**
 * Replaces the old HTML code of the component with the given ID by the new one. In other words,
 * this methods acts like a cross-browser "outerHTML"-method.
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
 * Highlights the components with the given IDs for a certain time interval with a specific back-
 * ground color. The duration and color that will be used is defined by the properties with the
 * same name of the "wingS.global.incrementalUpdateHighlight"-object.
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
 * Shows the AJAX activity cursor and makes a user-predefined element with the CSS ID
 * "ajaxActivityIndicator" visible. The latter is typically an animated GIF.
 */
wingS.ajax.showAjaxActivityIndicator = function() {
    if (wingS.global.incrementalUpdateCursor.enabled) AjaxActivityCursor.show();
    var indicator = document.getElementById("ajaxActivityIndicator");
    if (indicator != null) {
        indicator.style.visibility = "visible";
    }
};

/**
 * Hides the AJAX activity cursor and makes a user-predefined element with the CSS ID
 * "ajaxActivityIndicator" invisible. The latter is typically an animated GIF.
 */
wingS.ajax.hideAjaxActivityIndicator = function() {
    if (wingS.global.incrementalUpdateCursor.enabled) AjaxActivityCursor.hide();
    var indicator = document.getElementById("ajaxActivityIndicator");
    if (indicator != null && !AjaxRequest.isActive()) {
        indicator.style.visibility = "hidden";
    }
};

/**
 * An object that encapsulates functions to show and hide an animated GIF besides the mouse cursor.
 * Such a cursor can be used to indicate an active AJAX request.
 */
wingS.ajax.AjaxActivityCursor = function() {
    this.dx  = 0;
    this.dy  = 15;
    this.div = false;
};

// Initialize cursor
wingS.ajax.AjaxActivityCursor.prototype.init = function() {
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

// Callback function
wingS.ajax.AjaxActivityCursor.prototype.followMouse = function(event) {
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

// Show cursor
wingS.ajax.AjaxActivityCursor.prototype.show = function() {
    this.div.style.display = "block";
    return false;
};

// Hide cursor
wingS.ajax.AjaxActivityCursor.prototype.hide = function() {
    this.div.style.display = "none";
    return false;
};

// Instantiate cursor
var AjaxActivityCursor = new wingS.ajax.AjaxActivityCursor();

/**
 * The AjaxRequest class is a wrapper for the XMLHttpRequest objects which
 * are available in most modern browsers. It simplifies the interfaces for
 * making Ajax requests, adds commonly-used convenience methods, and makes
 * the process of handling state changes more intuitive.
 * An object may be instantiated and used, or the Class methods may be used
 * which internally create an AjaxRequest object.
 *
 * Notice: The actual code has been taken from http://www.ajaxtoolbox.com!
 *         Its author Matt Kruse allows usage "for any purpose, commercial
 *         or private, without any further permission". VERSION: 2005/06/22
 *
 *         The following changes have been made to the original source code:
 *         - updated the AjaxRequest.getXmlHttpRequest() method according to
 *           http://jibbering.com/2002/4/httprequest.html (Version: Jan. 06)
 *         - adopted the condition around line 187 in order to reflect the
 *           new return type of the AjaxRequest.getXmlHttpRequest() method
 *         - renamed "AjaxRequestUniqueId" parameter into "ajax_request_uid"
 *         - added ";" after closing "}" of AjaxRequest class
 */
function AjaxRequest() {
    var req = new Object();

    // -------------------
    // Instance properties
    // -------------------

    /**
     * Timeout period (in ms) until an async request will be aborted, and
     * the onTimeout function will be called
     */
    req.timeout = null;

    /**
     * Since some browsers cache GET requests via XMLHttpRequest, an
     * additional parameter called AjaxRequestUniqueId will be added to
     * the request URI with a unique numeric value appended so that the requested
     * URL will not be cached.
     */
    req.generateUniqueUrl = true;

    /**
     * The url that the request will be made to, which defaults to the current
     * url of the window
     */
    req.url = window.location.href;

    /**
     * The method of the request, either GET (default), POST, or HEAD
     */
    req.method = "GET";

    /**
     * Whether or not the request will be asynchronous. In general, synchronous
     * requests should not be used so this should rarely be changed from true
     */
    req.async = true;

    /**
     * The username used to access the URL
     */
    req.username = null;

    /**
     * The password used to access the URL
     */
    req.password = null;

    /**
     * The parameters is an object holding name/value pairs which will be
     * added to the url for a GET request or the request content for a POST request
     */
    req.parameters = new Object();

    /**
     * The sequential index number of this request, updated internally
     */
    req.requestIndex = AjaxRequest.numAjaxRequests++;

    /**
     * Indicates whether a response has been received yet from the server
     */
    req.responseReceived = false;

    /**
     * The name of the group that this request belongs to, for activity
     * monitoring purposes
     */
    req.groupName = null;

    /**
     * The query string to be added to the end of a GET request, in proper
     * URIEncoded format
     */
    req.queryString = "";

    /**
     * After a response has been received, this will hold the text contents of
     * the response - even in case of error
     */
    req.responseText = null;

    /**
     * After a response has been received, this will hold the XML content
     */
    req.responseXML = null;

    /**
     * After a response has been received, this will hold the status code of
     * the response as returned by the server.
     */
    req.status = null;

    /**
     * After a response has been received, this will hold the text description
     * of the response code
     */
    req.statusText = null;

    /**
     * An internal flag to indicate whether the request has been aborted
     */
    req.aborted = false;

    /**
     * The XMLHttpRequest object used internally
     */
    req.xmlHttpRequest = null;

    // --------------
    // Event handlers
    // --------------

    /**
     * If a timeout period is set, and it is reached before a response is
     * received, a function reference assigned to onTimeout will be called
     */
    req.onTimeout = null;

    /**
     * A function reference assigned will be called when readyState=1
     */
    req.onLoading = null;

    /**
     * A function reference assigned will be called when readyState=2
     */
    req.onLoaded = null;

    /**
     * A function reference assigned will be called when readyState=3
     */
    req.onInteractive = null;

    /**
     * A function reference assigned will be called when readyState=4
     */
    req.onComplete = null;

    /**
     * A function reference assigned will be called after onComplete, if
     * the statusCode=200
     */
    req.onSuccess = null;

    /**
     * A function reference assigned will be called after onComplete, if
     * the statusCode != 200
     */
    req.onError = null;

    /**
     * If this request has a group name, this function reference will be called
     * and passed the group name if this is the first request in the group to
     * become active
     */
    req.onGroupBegin = null;

    /**
     * If this request has a group name, and this request is the last request
     * in the group to complete, this function reference will be called
     */
    req.onGroupEnd = null;

    // Get the XMLHttpRequest object itself
    req.xmlHttpRequest = AjaxRequest.getXmlHttpRequest();
    if (!req.xmlHttpRequest) { return null; }

    // -------------------------------------------------------
    // Attach the event handlers for the XMLHttpRequest object
    // -------------------------------------------------------
    req.xmlHttpRequest.onreadystatechange =
    function() {
        if (req==null || req.xmlHttpRequest==null) { return; }
        if (req.xmlHttpRequest.readyState==1) { req.onLoadingInternal(req); }
        if (req.xmlHttpRequest.readyState==2) { req.onLoadedInternal(req); }
        if (req.xmlHttpRequest.readyState==3) { req.onInteractiveInternal(req); }
        if (req.xmlHttpRequest.readyState==4) { req.onCompleteInternal(req); }
    };

    // ---------------------------------------------------------------------------
    // Internal event handlers that fire, and in turn fire the user event handlers
    // ---------------------------------------------------------------------------
    // Flags to keep track if each event has been handled, in case of
    // multiple calls (some browsers may call the onreadystatechange
    // multiple times for the same state)
    req.onLoadingInternalHandled = false;
    req.onLoadedInternalHandled = false;
    req.onInteractiveInternalHandled = false;
    req.onCompleteInternalHandled = false;
    req.onLoadingInternal =
        function() {
            if (req.onLoadingInternalHandled) { return; }
            AjaxRequest.numActiveAjaxRequests++;
            if (AjaxRequest.numActiveAjaxRequests==1 && typeof(window['AjaxRequestBegin'])=="function") {
                AjaxRequestBegin();
            }
            if (req.groupName!=null) {
                if (typeof(AjaxRequest.numActiveAjaxGroupRequests[req.groupName])=="undefined") {
                    AjaxRequest.numActiveAjaxGroupRequests[req.groupName] = 0;
                }
                AjaxRequest.numActiveAjaxGroupRequests[req.groupName]++;
                if (AjaxRequest.numActiveAjaxGroupRequests[req.groupName]==1 && typeof(req.onGroupBegin)=="function") {
                    req.onGroupBegin(req.groupName);
                }
            }
            if (typeof(req.onLoading)=="function") {
                req.onLoading(req);
            }
            req.onLoadingInternalHandled = true;
        };
    req.onLoadedInternal =
        function() {
            if (req.onLoadedInternalHandled) { return; }
            if (typeof(req.onLoaded)=="function") {
                req.onLoaded(req);
            }
            req.onLoadedInternalHandled = true;
        };
    req.onInteractiveInternal =
        function() {
            if (req.onInteractiveInternalHandled) { return; }
            if (typeof(req.onInteractive)=="function") {
                req.onInteractive(req);
            }
            req.onInteractiveInternalHandled = true;
        };
    req.onCompleteInternal =
        function() {
            if (req.onCompleteInternalHandled || req.aborted) { return; }
            req.onCompleteInternalHandled = true;
            AjaxRequest.numActiveAjaxRequests--;
            if (AjaxRequest.numActiveAjaxRequests==0 && typeof(window['AjaxRequestEnd'])=="function") {
                AjaxRequestEnd(req.groupName);
            }
            if (req.groupName!=null) {
                AjaxRequest.numActiveAjaxGroupRequests[req.groupName]--;
                if (AjaxRequest.numActiveAjaxGroupRequests[req.groupName]==0 && typeof(req.onGroupEnd)=="function") {
                    req.onGroupEnd(req.groupName);
                }
            }
            req.responseReceived = true;
            req.status = req.xmlHttpRequest.status;
            req.statusText = req.xmlHttpRequest.statusText;
            req.responseText = req.xmlHttpRequest.responseText;
            req.responseXML = req.xmlHttpRequest.responseXML;
            if (typeof(req.onComplete)=="function") {
                req.onComplete(req);
            }
            if (req.xmlHttpRequest.status==200 && typeof(req.onSuccess)=="function") {
                req.onSuccess(req);
            }
            else if (typeof(req.onError)=="function") {
                req.onError(req);
            }

            // Clean up so IE doesn't leak memory
            delete req.xmlHttpRequest['onreadystatechange'];
            req.xmlHttpRequest = null;
        };
    req.onTimeoutInternal =
        function() {
            if (req!=null && req.xmlHttpRequest!=null && !req.onCompleteInternalHandled) {
                req.aborted = true;
                req.xmlHttpRequest.abort();
                AjaxRequest.numActiveAjaxRequests--;
                if (AjaxRequest.numActiveAjaxRequests==0 && typeof(window['AjaxRequestEnd'])=="function") {
                    AjaxRequestEnd(req.groupName);
                }
                if (req.groupName!=null) {
                    AjaxRequest.numActiveAjaxGroupRequests[req.groupName]--;
                    if (AjaxRequest.numActiveAjaxGroupRequests[req.groupName]==0 && typeof(req.onGroupEnd)=="function") {
                        req.onGroupEnd(req.groupName);
                    }
                }
                if (typeof(req.onTimeout)=="function") {
                    req.onTimeout(req);
                }
            // Opera won't fire onreadystatechange after abort, but other browsers do.
            // So we can't rely on the onreadystate function getting called. Clean up here!
            delete req.xmlHttpRequest['onreadystatechange'];
            req.xmlHttpRequest = null;
            }
        };

    // ----------------
    // Instance methods
    // ----------------
    /**
     * The process method is called to actually make the request. It builds the
     * querystring for GET requests (the content for POST requests), sets the
     * appropriate headers if necessary, and calls the
     * XMLHttpRequest.send() method
    */
    req.process =
        function() {
            if (req.xmlHttpRequest!=null) {
                // Some logic to get the real request URL
                if (req.generateUniqueUrl && req.method=="GET") {
                    req.parameters["ajax_request_uid"] = new Date().getTime() + "" + req.requestIndex;
                }
                var content = null; // For POST requests, to hold query string
                for (var i in req.parameters) {
                    if (req.queryString.length>0) { req.queryString += "&"; }
                    req.queryString += encodeURIComponent(i) + "=" + encodeURIComponent(req.parameters[i]);
                }
                if (req.method=="GET") {
                    if (req.queryString.length>0) {
                        req.url += ((req.url.indexOf("?")>-1)?"&":"?") + req.queryString;
                    }
                }
                req.xmlHttpRequest.open(req.method,req.url,req.async,req.username,req.password);
                if (req.method=="POST") {
                    if (typeof(req.xmlHttpRequest.setRequestHeader)!="undefined") {
                        req.xmlHttpRequest.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
                    }
                    content = req.queryString;
                }
                if (req.timeout>0) {
                    setTimeout(req.onTimeoutInternal,req.timeout);
                }
                req.xmlHttpRequest.send(content);
            }
        };

    /**
     * An internal function to handle an Object argument, which may contain
     * either AjaxRequest field values or parameter name/values
     */
    req.handleArguments =
        function(args) {
            for (var i in args) {
                // If the AjaxRequest object doesn't have a property which was passed, treat it as a url parameter
                if (typeof(req[i])=="undefined") {
                    req.parameters[i] = args[i];
                }
                else {
                    req[i] = args[i];
                }
            }
        };

    /**
     * Returns the results of XMLHttpRequest.getAllResponseHeaders().
     * Only available after a response has been returned
     */
    req.getAllResponseHeaders =
        function() {
            if (req.xmlHttpRequest!=null) {
                if (req.responseReceived) {
                    return req.xmlHttpRequest.getAllResponseHeaders();
                }
                alert("Cannot getAllResponseHeaders because a response has not yet been received");
            }
        };

    /**
     * Returns the the value of a response header as returned by
     * XMLHttpRequest,getResponseHeader().
     * Only available after a response has been returned
     */
    req.getResponseHeader =
        function(headerName) {
            if (req.xmlHttpRequest!=null) {
                if (req.responseReceived) {
                    return req.xmlHttpRequest.getResponseHeader(headerName);
                }
                alert("Cannot getResponseHeader because a response has not yet been received");
            }
        };

    return req;
};

// ---------------------------------------
// Static methods of the AjaxRequest class
// ---------------------------------------

/**
 * Returns an XMLHttpRequest object, either as a core object or an ActiveX
 * implementation. If an object cannot be instantiated, it will return null;
 */
AjaxRequest.getXmlHttpRequest = function() {
    var xmlhttp = false;
    /*@cc_on @*/
    /*@if (@_jscript_version >= 5)
    try {
        xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
    } catch (e) {
        try {
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        } catch (E) {
            xmlhttp = false;
        }
    }
    @end @*/

    if (!xmlhttp && typeof XMLHttpRequest != 'undefined') {
        try {
            xmlhttp = new XMLHttpRequest();
        } catch (e) {
            xmlhttp = false;
        }
    }

    if (!xmlhttp && window.createRequest) {
        try {
            xmlhttp = window.createRequest();
        } catch (e) {
            xmlhttp = false;
        }
    }

    return xmlhttp;
};

/**
 * See if any request is active in the background
 */
AjaxRequest.isActive = function() {
    return (AjaxRequest.numActiveAjaxRequests>0);
};

/**
 * Make a GET request. Pass an object containing parameters and arguments as
 * the second argument.
 * These areguments may be either AjaxRequest properties to set on the request
 * object or name/values to set in the request querystring.
 */
AjaxRequest.get = function(args) {
    AjaxRequest.doRequest("GET",args);
};

/**
 * Make a POST request. Pass an object containing parameters and arguments as
 * the second argument.
 * These areguments may be either AjaxRequest properties to set on the request
 * object or name/values to set in the request querystring.
 */
AjaxRequest.post = function(args) {
    AjaxRequest.doRequest("POST",args);
};

/**
 * The internal method used by the .get() and .post() methods
 */
AjaxRequest.doRequest = function(method,args) {
    if (typeof(args)!="undefined" && args!=null) {
        var myRequest = new AjaxRequest();
        myRequest.method = method;
        myRequest.handleArguments(args);
        myRequest.process();
    }
};

/**
 * Submit a form. The requested URL will be the form's ACTION, and the request
 * method will be the form's METHOD.
 * Returns true if the submittal was handled successfully, else false so it
 * can easily be used with an onSubmit event for a form, and fallback to
 * submitting the form normally.
 */
AjaxRequest.submit = function(theform, args) {
    var myRequest = new AjaxRequest();
    if (myRequest==null) { return false; }
    var serializedForm = AjaxRequest.serializeForm(theform);
    myRequest.method = theform.method.toUpperCase();
    myRequest.url = theform.action;
    myRequest.handleArguments(args);
    myRequest.queryString = serializedForm;
    myRequest.process();
    return true;
};

/**
 * Serialize a form into a format which can be sent as a GET string or a POST
 * content.It correctly ignores disabled fields, maintains order of the fields
 * as in the elements[] array. The 'file' input type is not supported, as
 * its content is not available to javascript. This method is used internally
 * by the submit class method.
 */
AjaxRequest.serializeForm = function(theform) {
    var els = theform.elements;
    var len = els.length;
    var queryString = "";
    this.addField =
        function(name,value) {
            if (queryString.length>0) {
                queryString += "&";
            }
            queryString += encodeURIComponent(name) + "=" + encodeURIComponent(value);
        };
    for (var i=0; i<len; i++) {
        var el = els[i];
        if (!el.disabled) {
            switch(el.type) {
                case 'text': case 'password': case 'hidden': case 'textarea':
                    this.addField(el.name,el.value);
                    break;
                case 'select-one':
                    if (el.selectedIndex>=0) {
                        this.addField(el.name,el.options[el.selectedIndex].value);
                    }
                    break;
                case 'select-multiple':
                    for (var j=0; j<el.options.length; j++) {
                        if (el.options[j].selected) {
                            this.addField(el.name,el.options[j].value);
                        }
                    }
                    break;
                case 'checkbox': case 'radio':
                    if (el.checked) {
                        this.addField(el.name,el.value);
                    }
                    break;
            }
        }
    }
    return queryString;
};

// -----------------------
// Static Class variables
// -----------------------

/**
 * The number of total AjaxRequest objects currently active and running
 */
AjaxRequest.numActiveAjaxRequests = 0;

/**
 * An object holding the number of active requests for each group
 */
AjaxRequest.numActiveAjaxGroupRequests = new Object();

/**
 * The total number of AjaxRequest objects instantiated
 */
AjaxRequest.numAjaxRequests = 0;