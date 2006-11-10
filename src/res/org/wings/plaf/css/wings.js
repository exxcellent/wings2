// =============================================================================
/*
   wingS - Utility JavaScript functions

   This file contains commonly used JavaScript which might also be useful for
   wings users.
*/
// =============================================================================

// faking a namespace
if (!wingS) {
    var wingS = new Object();
}
else if (typeof wingS != "object") {
    throw new Error("wingS already exists and is not an object");
}

if (!wingS.util) {
    wingS.util = new Object();
}
else if (typeof wingS.util != "object") {
    throw new Error("wingS.util already exists and is not an object");
}

if (!wingS.request) {
    wingS.request = new Object();
}
else if (typeof wingS.request != "object") {
    throw new Error("wingS.request already exists and is not an object");
}

if (!wingS.ajax) {
    wingS.ajax = new Object();
}
else if (typeof wingS.ajax != "object") {
    throw new Error("wingS.ajax already exists and is not an object");
}

if (!wingS.layout) {
    wingS.layout = new Object();
}
else if (typeof wingS.layout != "object") {
    throw new Error("wingS.layout already exists and is not an object");
}

if (!wingS.globals) {
    wingS.globals = new Object();
}
else if (typeof wingS.globals != "object") {
    throw new Error("wingS.globals already exists and is not an object");
}

if (!wingS.events) {
    wingS.events = new Object();
}
else if (typeof wingS.events != "object") {
    throw new Error("wingS.events already exists and is not an object");
}


// =============================================================================

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

wingS.util.getParentByTagName = function(element, tag) {
    while (element != null) {
        if (tag == element.tagName)
            return element;
        element = element.parentNode;
    }
    return null;
};

wingS.util.getParentWearingAttribute = function(element, attribute) {
    while (element != null) {
        if (element.getAttribute && element.getAttribute(attribute)) {
            return element;
        }
        element = element.parentNode;
    }
    return null;
};

// TODO document + event.stopPropagation()
wingS.util.preventDefault = function(event) {
    if (event.preventDefault)
        event.preventDefault();
    if (event.returnValue)
        event.returnValue = false;
    event.cancelBubble = true;
};

/**
 * Can be used to prevent a form submit.
 * By calling 'return wingS.util.preventSubmit()' on the input event
 * 'onkeypress', false will be returned when the return key was hit and
 * by that avoiding a form submit.
 */
wingS.util.preventSubmit = function() {
  return !(window.event && window.event.keyCode == 13);
};

/**
 * Inserts a node into the childNodes array after the specified child node refChild.
 * Note: Because there is no function insertAfter, it is done by raping insertBefore.
 * @param {Object} newChild node to insert
 * @param {Object} refChild node to insert after
 */
wingS.util.insertAfter = function(newChild, refChild) {
    refChild.parentNode.insertBefore(newChild, refChild.nextSibling);
};

/**
 * Moves the execution context of the function used upon to the given
 * object. Usefull when using setTimeout or event handling.
 * e.g.: setTimeout(func1.bind(someObject), 1);
 * The function func1 will be called within the context of someObject.
 * NB: Function object is extended by bind()!
 * @param {Object} obj new execution context
 */
Function.prototype.bind = function(obj) {
    var method = this;
    temp = function() {
        return method.apply(obj, arguments);
    };

    return temp;
};

// =============================================================================

wingS.globals.event_epoch;                // Maintains the event epoch of this frame
wingS.globals.completeUpdateId;           // Holds the ID of the CompleteUpdateResource
wingS.globals.incrementalUpdateId;        // Holds the ID of the IncrementalUpdateResource
wingS.globals.incrementalUpdateEnabled;   // True if this frame allows incremental updates
wingS.globals.incrementalUpdateCursor;    // An object whose properties "enabled", "image"
                                          // "dx" and "dy" hold the settings of the cursor
wingS.globals.incrementalUpdateHighlight; // An object whose properties "enabled", "color"
                                          // and "duration" store the 3 highlight settings
wingS.globals.requestQueue = new Array(); // A queue which stores requests to be processed

wingS.request.submitForm = function(ajaxEnabled, event, eventName, eventValue, scriptCodes) {
    // Enqueue this request if another one hasn't been processed yet
    if (wingS.request.enqueueThisRequest(wingS.request.submitForm, wingS.request.submitForm.arguments)) return;

    // Needed preparations
    event = wingS.events.getEvent(event);
    var target = wingS.events.getTarget(event);
    var form = wingS.util.getParentByTagName(target, "FORM");
    if (eventName != null) {
        var eidProvider = wingS.util.getParentWearingAttribute(target, "eid");
        if (eidProvider == null) {
            alert("DEBUG:\n target: " + target + "\nform: " + form);
            return;
        }
        eventName = eidProvider.getAttribute("eid");
    }

    if (wingS.request.invokeScriptListeners(scriptCodes)) {

        if (form != null) {
            // Generate unique IDs for the nodes we have to insert
            // dynamically into the form (workaround because of IE)
            var formId = form.getAttribute("id");
            var epochNodeId = "event_epoch_" + formId;
            var triggerNodeId = "event_trigger_" + formId;

            var debug = "Elements before: " + form.elements.length;

            // Always encode current event epoch
            var epochNode = document.getElementById(epochNodeId);
            if (epochNode == null) {
                // Append this node only once, then reuse it
                epochNode = document.createElement("input");
                epochNode.setAttribute("type", "hidden");
                epochNode.setAttribute("name", "event_epoch");
                epochNode.setAttribute("id", epochNodeId);
                form.appendChild(epochNode);
            }
            epochNode.setAttribute("value", wingS.globals.event_epoch);

            // Encode event trigger if available
            var triggerNode = document.getElementById(triggerNodeId);
            if (eventName != null) {
                if (triggerNode == null) {
                    // Append this node only once, then reuse it
                    triggerNode = document.createElement("input");
                    triggerNode.setAttribute("type", "hidden");
                    triggerNode.setAttribute("name", "event_trigger");
                    triggerNode.setAttribute("id", triggerNodeId);
                    form.appendChild(triggerNode);
                }
                triggerNode.setAttribute("value", eventName + "|" + eventValue);
            } else if (triggerNode != null) {
                // If we don't need an event trigger we have to
                // ensure that old event triggers (if existing)
                // are deleted. Otherwise they get fired again!
                form.removeChild(triggerNode);
            }

            if (false) {
                debug += "\nElements after: " + form.elements.length;
                for (var i = 0; i < form.elements.length; i++) {
                    debug += "\n - name: " + form.elements[i].name + " | value: " + form.elements[i].value;
                }
                alert(debug);
            }

            var submitted = false;
            // Form submit by means of AJAX
            if (wingS.globals.incrementalUpdateEnabled && ajaxEnabled) {
                form.action = wingS.util.encodeUpdateId(wingS.globals.incrementalUpdateId);
                submitted = wingS.ajax.doAjaxSubmit(form);
            }
            // Always (re-)set the form's action to the
            // URL of the CompleteUpdateResource, since
            // this should remain the default that will
            // be used whenever a form is NOT submitted
            // via this method - even though it should!
            form.action = wingS.util.encodeUpdateId(wingS.globals.completeUpdateId);
            // Default form submit (fallback mechanism)
            if (!submitted) form.submit();
        } else {
            // If we've got a form, it might be alright to submit it
            // without having an "eventName" or "eventValue". This is
            // because all form components are automatically in their
            // "correct" state BEFORE the submit takes place - this is
            // the way HTML functions. However, if we've got no form,
            // we need to send the name and the value of the component
            // which generated the event we want to process. Let's go!
            if (eventName == null) {
                eventName = target.getAttribute("id");
                var eventNode = document.getElementById(eventName);
                if (eventNode.value) eventValue = eventNode.value;
            }
            wingS.request.followLink(ajaxEnabled, eventName, eventValue);
        }
    }
};

wingS.request.followLink = function(ajaxEnabled, eventName, eventValue, scriptCodes) {
    // Enqueue this request if another one hasn't been processed yet
    if (wingS.request.enqueueThisRequest(wingS.request.followLink, wingS.request.followLink.arguments)) return;

    if (wingS.request.invokeScriptListeners(scriptCodes)) {
        if (wingS.globals.incrementalUpdateEnabled && ajaxEnabled) {
            // Send request via AJAX
            var args = {};
            args.method = "GET";
            if (eventName != null && eventValue != null) {
                args.event_epoch = wingS.globals.event_epoch;
                args[eventName] = eventValue;
            }
            args.url = wingS.util.encodeUpdateId(wingS.globals.incrementalUpdateId);
            wingS.ajax.doAjaxRequest(args);
        } else {
            // Send a default HTTP request
            url = wingS.util.encodeUpdateId(wingS.globals.completeUpdateId);
            window.location.href = url + "?event_epoch=" + wingS.globals.event_epoch +
                                   "&" + eventName + "=" + eventValue;
        }
    }
};

wingS.ajax.getUpdates = function() {
    wingS.request.followLink(true);
};

wingS.request.enqueueThisRequest = function(send, args) {
    if (AjaxRequest.isActive()) {
        wingS.globals.requestQueue.push( {"send" : send, "args" : args} );
        return true;
    }
    return false;
};

wingS.request.dequeueNextRequest = function() {
    if (wingS.globals.requestQueue.length > 0) {
        var request = wingS.globals.requestQueue.shift();
        var args = request.args;
        request.send(args[0], args[1], args[2], args[3]);
    }
};

wingS.request.invokeScriptListeners = function(scriptCodes) {
    if (scriptCodes) {
        for (var i = 0; i < scriptCodes.length; i++) {
            invokeNext = scriptCodes[i]();
            if (invokeNext == false) return false;
        }
    }
    return true;
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

wingS.util.encodeUpdateId = function(id) {
    return wingS.globals.event_epoch + "-" + id;
};

// =============================================================================

/* Remove focus from a component and respect  additonal custom script listeners
   attached by user. Core usage/doc see Utils.printButtonStart() */
wingS.util.blurComponent = function(component, clientHandlers) {
    var success = true;
    if (clientHandlers) {
        for (var i = 0; i < clientHandlers.length; i++) {
            success = clientHandlers[i]();
            if (success == false) break;
        }
    }

    if (success == undefined || success && component.blur()) {
        component.blur();
    }

    return true;
};

/* Set focus to a component and respect additonal custom script listeners
   attached by user. Core usage/doc see Utils.printButtonStart() */
wingS.util.focusComponent = function(component, clientHandlers) {
    var success = true;
    if (clientHandlers) {
        for (var i = 0; i < clientHandlers.length; i++) {
            success = clientHandlers[i]();
            if (success == false) break;
        }
    }

    if (success == undefined || success && component.focus()) {
        component.focus();
    }

    return true;
};

/* Search and return the first HTML element with the given tag name inside
   the HTML code generated by wings for the passed component id.

   This function is i.e. helpful if you want to modify i.e. the INPUT element
   of a STextField which is probably wrapped into TABLE elements wearing the
   component ID generated by wingS for layouting purposes. */
wingS.util.findElement = function(id, tagname) {
    var div = document.getElementById(id);
    if (div) {
        var elements = div.getElementsByTagName(tagname);
        if (elements && elements.length > 0)
            return elements[0];
    }
};

/* Set the focus to a component identified by a wingS id. Also do some heuristic
   trace-down to the actual HTML element, i.e. a STextField renders as <table
   id=...><input...></table> but you want the focus to be the input element and
   not the table element. */
wingS.util.requestFocus = function(id) {
    var div = document.getElementById(id);
    window.focus = id;
    if (div) {
        if (div.getAttribute("foc") == id) {
            if (!div.disabled && !div.style.display == "none") div.focus();
            return;
        }

        var elements = div.getElementsByTagName("INPUT");
        for (var i = 0; i < elements.length; i++) {
            var element = elements[i];
            if (element.getAttribute("foc") == id &&
                !element.disabled &&
                !element.style.display == "none") {
                element.focus();
                return;
            }
        }
        elements = div.getElementsByTagName("A");
        for (var i = 0; i < elements.length; i++) {
            var element = elements[i];
            if (element.getAttribute("foc") == id &&
                !element.disabled &&
                !element.style.display == "none") {
                element.focus();
                return;
            }
        }
    }
};

wingS.util.getCookie = function(name) {
    var c = new Object();
    var i = 0;
    var clen = document.cookie.length;
    while (i < clen)
    {
        var endstr = document.cookie.indexOf(";", i);
        if (endstr == -1) endstr = document.cookie.length;

        var v = unescape(document.cookie.substring(i, endstr));
        var key = v.substring(0, v.indexOf("=", 0));
        var val = v.substring(v.indexOf("=") + 1);
        c[key] = val;
        i = endstr + 2;
        // skip whitespace after ;
    }
    if (name) return c[name];
    return c;
};

wingS.util.setCookie = function(name, value, days, path) {
    if (!days) days = -1;
    var expire = new Date();
    expire.setTime(expire.getTime() + 86400000 * days);

    document.cookie = name + "=" + escape(value)
            + "; expires=" + expire.toGMTString() + ";"
            + (path ? 'path=' + path : '');
};

wingS.util.storeScrollPosition = function(event) {
    var target = wingS.events.getTarget(event);
    var scrollableElement = wingS.util.getScrollableElement(target);
    if (scrollableElement && target) {
        var pos = target.scrollTop;
        if (scrollableElement.nodeName == 'DIV' ||
            scrollableElement.nodeName == 'TBODY') {
            var targetId = scrollableElement.getAttribute("id");
            wingS.util.getCookie("scroll_pos", "" + pos, 1);
            wingS.util.getCookie("scroll_target", "" + targetId, 1);
        }
    }
};

wingS.util.restoreScrollPosition = function() {
    var pos = wingS.util.getCookie("scroll_pos");
    var target = wingS.util.getCookie("scroll_target");
    var el = document.getElementById(target);
    if (el) {
        el.scrollTop = pos;
    }
};

wingS.util.getScrollableElement = function(el) {
    if (!el) return;
    if (el.scrollTop > 0)
        return el;

    var parent = el.parentNode;
    if (null == parent) return null;
    if (parent.scrollTop != 0) {
        return parent;
    }
    else {
        return arguments.callee(parent);
    }
};

wingS.util.storeFocus = function(event) {
    var target = wingS.events.getTarget(event);
    var div = wingS.util.getParentWearingAttribute(target, "eid");
    var body = wingS.util.getParentByTagName(target, "BODY");
    /* Avoid rembering FORM as focus component as this automatically gains
       focus on pressing Enter in MSIE. */
    if (div && body && div.tagName != "FORM") {
        wingS.util.getCookie(body.getAttribute("id") + "_focus", div.getAttribute("id"), 1);
    }
};

/**
 * Alerts all fields/elements of a given object. NB: you will also get
 * object methods (which are function valued properties).
 * helper function to debug
 * @param {Object} obj
 */
wingS.util.printAllFields = function(obj) {
    for(var i in obj) {
        logDebug(obj[i], obj);
    }
};

wingS.util.checkUserAgent = function(string) {
    return navigator.userAgent.toLowerCase().indexOf(string) + 1;
};

/* The following two functions are a workaround for IE to open a link in the
   right target/new window used in AnchorCG. */
wingS.util.checkTarget = function(target) {
    for (var i = 0; i < parent.frames.length; i++) {
        if (parent.frames[i].name == target) return true;
    }
    return false;
};

wingS.util.openlink = function(target, url, scriptCodes) {
  if (wingS.request.invokeScriptListeners(scriptCodes)) {
      // if the target exists => change URL, else => open URL in new window
      if (target == null) {
          window.location.href = url;
      } else {
          if (wingS.util.checkTarget(target)) {
              parent.frames[target].location.href = url;
          } else {
              window.open(url, target);
          }
      }
  }
};

/* Utility method to determine available inner space of the show window on
   all browsers. Returns a numeric value of available pixel width. */
wingS.util.framewidth = function() {
    if (self.innerHeight) {
        // all except Explorer
        return self.innerWidth;
    } else if (document.documentElement && document.documentElement.clientHeight) {
        // Explorer 6 Strict Mode
        return document.documentElement.clientWidth;
    } else if (document.body) {
        // other Explorers
        return document.body.clientWidth;
    } else
        return -1;
};

/* Cross-browser method to register an event listener on the passed object. Only
   Mozilla will support captive mode of event handling. The 'eventType' is without
   the 'on'-prefix. Example: wingS.events.registerEvent(document,'focus',storeFocus,false); */
// Deprecated! Use YAHOO.util.Event.addListener() instead of this function.
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

// =============================================================================

/* XCalendar JavaScript Code */
function onFieldChange(key, name, value) {
    xcalendar.onFieldChange(onFieldChangeCallback, key, name, value);
}

function onFieldChangeCallback(result) {
    var elem = document.getElementById(result[0]);
    if (!elem) return; // dwr bug
    var data = result[1];
    elem.value = data;
}

function onCalUpdate(cal) {
    xcalendar.onCalUpdate(onCalUpdateCallback, cal.params.formatter,
                          cal.params.textField, cal.date);
}

function onCalUpdateCallback(result) {
    var elem = document.getElementById(result[0]);
    if (!elem) return; // dwr bug
    var data = result[1];
    elem.value = data;
    elem.style.color = '';
}

// =============================================================================

/* SFormattedTextField JavaScript Code */
function ftextFieldCallback(result) {
    var elem = document.getElementById(result[0]);
    if (!elem) return; // dwr bug
    var data = result[1];
    if (!data) {
        elem.style.color = '#ff0000';
    } else {
        elem.style.color = '';
        elem.value = data;
        elem.setAttribute("lastValid", data);
    }
}

function spinnerCallback(result) {
    var elem = document.getElementById(result[0]);
    if (!elem) return; // dwr bug
    var data = result[1];
    if (data) {
        elem.value = data;
        elem.setAttribute("lastValid", data);
    }
}

// =============================================================================

// DEPRECATED functions to handle onresize and onload

/* Adds a function to the window.onresize functionality.
   This allows you to execute more than one function. */
var windowOnResizes = new Array();
function addWindowOnResizeFunction(func) {
    windowOnResizes.push(func);
}

/* The execution of all added window.onresize functions. */
window.onresize = performWindowOnResize;
function performWindowOnResize() {
    for (var i = 0; i < windowOnResizes.length; i++) {
        eval(windowOnResizes[i]);
    }
}

/* Adds a function to the window.onload functionality.
   This allows you to execute more than one function. */
var windowOnLoads = new Array();
function addWindowOnLoadFunction(func) {
    windowOnLoads.push(func);
}

/* The execution of all added window.onload functions. */
window.onload = performWindowOnLoad;
function performWindowOnLoad() {
    for (var i = 0; i < windowOnLoads.length; i++) {
        eval(windowOnLoads[i]);
    }
}

// =============================================================================

/* Shows the modal dialog at the center of the component. (SFrame or SInternalFrame) */
wingS.util.showModalDialog = function(dialogId, modalId) {
    var positionX = (document.all) ? document.body.offsetWidth : window.innerWidth;
    var positionY = (document.all) ? document.body.offsetHeight : window.innerHeight;
    positionX = positionX / 2;
    positionY = positionY / 2;
    var dialog = document.getElementById(dialogId);
    var modalDialog = document.getElementById(modalId);
    for (var parent = dialog.parentNode; parent != null; parent = parent.parentNode) {
        if (parent.nodeType != 1) {
            if (document.all) {
                modalDialog.style.width = document.body.offsetWidth + 'px';
                modalDialog.style.height = document.body.offsetHeight + 'px';
            }
            else {
                modalDialog.style.width = window.innerWidth;
                modalDialog.style.height = window.innerHeight;
            }
            break;
        }
        if (parent.getAttribute('SComponentClass') == 'org.wings.SInternalFrame') {
            positionX = parent.offsetWidth / 2;
            positionY = parent.offsetHeight / 2;
            positionX += wingS.util.absLeft(parent);
            positionY += wingS.util.absTop(parent);
            modalDialog.style.left = wingS.util.absLeft(parent) + 'px';
            modalDialog.style.top = wingS.util.absTop(parent) + 'px';
            modalDialog.style.width = parent.offsetWidth + 'px';
            modalDialog.style.height = parent.offsetHeight + 'px';
            break;
        }
    }
    var dialogWidth = dialog.offsetWidth;
    var dialogHeight = dialog.offsetHeight;
    dialog.style.left = (positionX - (dialogWidth / 2)) + 'px';
    dialog.style.top = (positionY - (dialogHeight / 2)) + 'px';
    dialog.style.zIndex = 1000;
};

wingS.layout.layoutScrollPaneFF = function(outerId) {
    var outer = document.getElementById(outerId);
    var div = outer.getElementsByTagName("DIV")[0];
    div.style.height =
        document.defaultView.getComputedStyle(outer, null).getPropertyValue("height");
    div.style.display = "block";
};

wingS.layout.layoutScrollPaneIE = function(outerId) {
    var outer = document.getElementById(outerId);
    var div = outer.getElementsByTagName("DIV")[0];
    var td = wingS.util.getParentByTagName(div, "TD");
    div.style.height = td.clientHeight + "px";
    div.style.width = td.clientWidth + "px";
    div.style.position = "absolute";
    div.style.display = "block";
    div.style.overflow = "auto";
};

wingS.layout.layoutAvailableSpaceIE = function(tableId) {
    var table = document.getElementById(tableId);
    if (table == null || table.style.height == table.getAttribute("layoutHeight")) return;

    var consumedHeight = 0;
    var rows = table.rows;
    for (var i = 0; i < rows.length; i++) {
        var row = rows[i];
        var yweight = row.getAttribute("yweight");
        if (!yweight) consumedHeight += row.offsetHeight;
    }

    table.style.height = table.getAttribute("layoutHeight");
    var diff = table.clientHeight - consumedHeight;

    for (var i = 0; i < rows.length; i++) {
      var row = rows[i];
      var yweight = row.getAttribute("yweight");
      if (yweight) {
          var oversize = row.getAttribute("oversize");
          row.height = Math.max(Math.floor((diff * yweight) / 100) - oversize, oversize);
      }
    }
};

/* Calculates the absolute position of the element to the left. */
wingS.util.absLeft = function(el) {
    return (el.offsetParent) ? el.offsetLeft + wingS.util.absLeft(el.offsetParent) : el.offsetLeft;
};

/* Calculates the absolute position of the element to the top. */
wingS.util.absTop = function(el) {
    return (el.offsetParent) ? el.offsetTop + wingS.util.absTop(el.offsetParent) : el.offsetTop;
};

// =============================================================================

wingS.ajax.filterOnAjaxRequest = new Array("addWindowOnLoadFunction"); // AJAX filters

/* At some points wingS uses functions to dynamically add callback methods which
   will be executed when special events occur, i.e. addWindowOnLoadFunction().
   Functions added via this method are executed when the page is loaded. However,
   this only makes sense on full page updates because there will be no reload on
   incremental updates. So in this case we have to invoke the desired callback
   function directly. In order to indicate that a funtion has to be filtered on
   an AJAX request, you simply have to add it to the "filterOnAjaxRequest" array. */
for (var i = 0; i < wingS.ajax.filterOnAjaxRequest.length; i++) {
    wingS.ajax.filterOnAjaxRequest[i] = new RegExp().compile(".*" + wingS.ajax.filterOnAjaxRequest[i] +
                             "\\s*\\(\\s*('(.*)'|\"(.*)\")\\s*\\)\\s*;.*", "g");
    // without escaping: /.*<FUNCTION NAME>\s*\(\s*('(.*)'|"(.*)")\s*\)\s*;.*/g
}

/* Prints some hidden debugging infos about the given AJAX request at the bottom
   of the page. This is done by dynamically attaching a dedicated textarea. */
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

/* This function shows the previously enabled AJAX debugging view, if available. */
wingS.ajax.showAjaxDebugging = function() {
    var debug = document.getElementById("ajaxDebugging");
    if (debug != null) debug.style.display = "block";
};

/* This function hides the previously enabled AJAX debugging view, if available. */
wingS.ajax.hideAjaxDebugging = function() {
    var debug = document.getElementById("ajaxDebugging");
    if (debug != null) debug.style.display = "none";
};

wingS.ajax.handleRequestError = function(request) {
    //var errorMsg = "An error occured while processing an AJAX request!\n" +
    //               ">> " + request.statusText + " (" + request.status + ")";
    //wingS.ajax.hideAjaxActivityIndicator();
    //alert(errorMsg);
    //window.location.href = wingS.util.encodeUpdateId(wingS.globals.completeUpdateId);
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
        hideAjaxActivityIndicator();
        window.location.href = encodeUpdateId(completeUpdateId);
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
                    for (var j = 0; j < wingS.ajax.filterOnAjaxRequest.length; j++) {
                        // Either $2 or $3 will always be an empty string
                        code = code.replace(wingS.ajax.filterOnAjaxRequest[j], "$2$3;");
                    }
                    eval(code);
                } catch(e) {
                    var errorMsg = "An error occured while processing an AJAX request!\n" +
                                   ">> " + e.message + "\n\n\n The following JavaScript " +
                                   "code could not be executed:\n" + code;
                    alert(errorMsg);
                }
            }
            // Update the event epoch of this frame
            wingS.globals.event_epoch = getFirstChildData("event_epoch");
            // Hightlight the components updated above
            if (wingS.globals.incrementalUpdateHighlight.enabled) {
                wingS.ajax.highlightComponentUpdates(componentIds);
            }
        }
    }
    wingS.ajax.hideAjaxActivityIndicator();
    // Send next queued request
    wingS.request.dequeueNextRequest();
};

/* Replaces the old HTML code of the component with the given ID by the new one.
   In other words, this methods acts like a cross-browser "outerHTML"-method. */
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

/* Highlights the components with the given IDs for a certain time interval with a
   specific background color. The duration and color that will be used is defined by
   the properties with the same name of the "wingS.globals.incrementalUpdateHighlight"-object. */
wingS.ajax.highlightComponentUpdates = function(componentIds) {
    for (var i = 0; i < componentIds.length; i++) {
        var component = document.getElementById(componentIds[i]);
        if (component == null) return;
        highlightComponent(component);
    }

    function highlightComponent(component) {
        var initialBackgroundColor = component.style.backgroundColor;
        component.style.backgroundColor = wingS.globals.incrementalUpdateHighlight.color;
        var resetColor = function() {
            component.style.backgroundColor = initialBackgroundColor;
        };
        setTimeout(resetColor, wingS.globals.incrementalUpdateHighlight.duration);
    }
};

/* Shows the AJAX activity cursor and makes a user-predefined element with the CSS
   ID "ajaxActivityIndicator" visible. The latter is typically an animated GIF. */
wingS.ajax.showAjaxActivityIndicator = function() {
    if (wingS.globals.incrementalUpdateCursor.enabled) AjaxActivityCursor.show();
    var indicator = document.getElementById("ajaxActivityIndicator");
    if (indicator != null) {
        indicator.style.visibility = "visible";
    }
};

/* Hides the AJAX activity cursor and makes a user-predefined element with the CSS
   ID "ajaxActivityIndicator" invisible. The latter is typically an animated GIF. */
wingS.ajax.hideAjaxActivityIndicator = function() {
    if (wingS.globals.incrementalUpdateCursor.enabled) AjaxActivityCursor.hide();
    var indicator = document.getElementById("ajaxActivityIndicator");
    if (indicator != null && !AjaxRequest.isActive()) {
        indicator.style.visibility = "hidden";
    }
};

/* An object that encapsulates functions to show and hide an animated GIF besides
   the mouse cursor. Such a cursor can be used to indicate an active AJAX request. */
wingS.ajax.AjaxActivityCursor = function() {
    this.dx  = 0;
    this.dy  = 15;
    this.div = false;
};

// Initialize cursor
wingS.ajax.AjaxActivityCursor.prototype.init = function() {    
    this.dx = wingS.globals.incrementalUpdateCursor.dx;
    this.dy = wingS.globals.incrementalUpdateCursor.dy;
    this.div = document.createElement("div");
    this.div.style.position = "absolute";
    this.div.style.zIndex = "1000";
    this.div.style.display = "none";
    this.div.innerHTML = "<img src=\"" + wingS.globals.incrementalUpdateCursor.image + "\"/>";
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

var AjaxActivityCursor = new wingS.ajax.AjaxActivityCursor();