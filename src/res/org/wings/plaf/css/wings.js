// =============================================================================
/*
   wingS - Utility JavaScript functions

   This file contains commonly used JavaScript which might also be useful for
   wings users. In order to avoid typical namespace cluttering, all functions
   and variables in this file should be prefixed by the string "wu_".
*/
// =============================================================================

function getEvent(event) {
    if (window.event)
        return window.event;
    else
        return event;
}

function getTarget(event) {
    if (event.srcElement)
        return event.srcElement;
    else
        return event.target;
}

function getParentByTagName(element, tag) {
    while (element != null) {
        if (tag == element.tagName)
            return element;
        element = element.parentNode;
    }
    return null;
}

function getParentWearingAttribute(element, attribute) {
    while (element != null) {
        if (element.getAttribute && element.getAttribute(attribute)) {
            return element;
        }
        element = element.parentNode;
    }
    return null;
}

function preventDefault(event) {
    if (event.preventDefault)
        event.preventDefault();
    if (event.returnValue)
        event.returnValue = false;
    event.cancelBubble = true;
}

// =============================================================================

var event_epoch;                // Maintains the event epoch of this frame
var completeUpdateId;           // Holds the ID of the CompleteUpdateResource
var incrementalUpdateId;        // Holds the ID of the IncrementalUpdateResource
var incrementalUpdateEnabled;   // True if this frame allows incremental updates
var incrementalUpdateCursor;    // An object whose properties "enabled", "image"
                                // "dx" and "dy" hold the settings of the cursor
var incrementalUpdateHighlight; // An object whose properties "enabled", "color"
                                // and "duration" store the 3 highlight settings
var requestQueue = new Array(); // A queue which stores requests to be processed

function submitForm(ajaxEnabled, event, eventName, eventValue, scriptCodes) {
    // Enqueue this request if another one hasn't been processed yet
    if (enqueueThisRequest(submitForm, submitForm.arguments)) return;

    // Needed preparations
    event = getEvent(event);
    var target = getTarget(event);
    var form = getParentByTagName(target, "FORM");
    if (eventName != null) {
        eidprovider = getParentWearingAttribute(target, "eid");
        eventName = eidprovider.getAttribute("eid");
    }

    if (invokeScriptListeners(scriptCodes)) {
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
            epochNode.setAttribute("value", event_epoch);

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
            if (incrementalUpdateEnabled && ajaxEnabled) {
                form.action = encodeUpdateId(incrementalUpdateId);
                submitted = doAjaxSubmit(form);
            }
            // Always (re-)set the form's action to the
            // URL of the CompleteUpdateResource, since
            // this should remain the default that will
            // be used whenever a form is NOT submitted
            // via this method - even though it should!
            form.action = encodeUpdateId(completeUpdateId);
            // Default form submit (fallback mechanism)
            if (!submitted) form.submit();
        } else {
            // If there's no form we can't submit it...
            followLink(ajaxEnabled, eventName, eventValue);
        }
    }
}

function followLink(ajaxEnabled, eventName, eventValue, scriptCodes) {
    // Enqueue this request if another one hasn't been processed yet
    if (enqueueThisRequest(followLink, followLink.arguments)) return;

    if (invokeScriptListeners(scriptCodes)) {
        if (incrementalUpdateEnabled && ajaxEnabled) {
            // Send request via AJAX
            var args = {};
            args.method = "GET";
            if (eventName != null && eventValue != null) {
                args.event_epoch = event_epoch;
                args[eventName] = eventValue;
            }
            args.url = encodeUpdateId(incrementalUpdateId);
            doAjaxRequest(args);
        } else {
            // Send a default request
            url = encodeUpdateId(completeUpdateId);
            window.location = url + "?event_epoch=" + event_epoch +
                                    "&" + eventName + "=" + eventValue;
        }
    }
}

function getUpdates() {
    followLink(true);
}

function enqueueThisRequest(send, args) {
    if (AjaxRequest.isActive()) {
        requestQueue.push( {"send" : send, "args" : args} );
        return true;
    }
    return false;
}

function dequeueNextRequest() {
    if (requestQueue.length > 0) {
        var request = requestQueue.shift();
        var args = request.args;
        request.send(args[0], args[1], args[2], args[3]);
    }
}

function invokeScriptListeners(scriptCodes) {
    if (scriptCodes) {
        for (var i = 0; i < scriptCodes.length; i++) {
            invokeNext = scriptCodes[i]();
            if (invokeNext == false) return false;
        }
    }
    return true;
}

function initAjaxCallbacks() {
    return {
        "onLoading" : function(request) { showAjaxActivityIndicator(); },
        "onSuccess" : function(request) { processAjaxRequest(request); },
        "onError"   : function(request) { handleRequestError(request); }
    };
}

function doAjaxSubmit(form) {
    var requestObject = initAjaxCallbacks();
    return AjaxRequest.submit(form, requestObject);
}

function doAjaxRequest(args) {
    var requestObject = initAjaxCallbacks();
    // Extend basic request object with arguments
    for (var i in args) requestObject[i] = args[i];

    // Use POST unless the request object defines GET
    if (typeof requestObject.method == "string" &&
        requestObject.method.toUpperCase() == "GET") {
        AjaxRequest.get(requestObject);
    } else {
        AjaxRequest.post(requestObject);
    }
}

function encodeUpdateId(id) {
    return event_epoch + "-" + id;
}

// =============================================================================

/* Remove focus from a component and respect  additonal custom script listeners
   attached by user. Core usage/doc see Utils.printButtonStart() */
function wu_blurComponent(component, clientHandlers) {
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
}

/* Set focus to a component and respect additonal custom script listeners
   attached by user. Core usage/doc see Utils.printButtonStart() */
function wu_focusComponent(component, clientHandlers) {
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
}

/* Search and return the first HTML element with the given tag name inside
   the HTML code generated by wings for the passed component id.

   This function is i.e. helpful if you want to modify i.e. the INPUT element
   of a STextField which is probably wrapped into TABLE elements wearing the
   component ID generated by wingS for layouting purposes. */
function wu_findElement(id, tagname) {
    var div = document.getElementById(id);
    if (div) {
        var elements = div.getElementsByTagName(tagname);
        if (elements && elements.length > 0)
            return elements[0];
    }
}

/* Set the focus to a component identified by a wingS id. Also do some heuristic
   trace-down to the actual HTML element, i.e. a STextField renders as <table
   id=...><input...></table> but you want the focus to be the input element and
   not the table element. */
function requestFocus(id) {
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
}

function getCookie(name) {
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
}

function setCookie(name, value, days, path) {
    if (!days) days = -1;
    var expire = new Date();
    expire.setTime(expire.getTime() + 86400000 * days);

    document.cookie = name + "=" + escape(value)
            + "; expires=" + expire.toGMTString() + ";"
            + (path ? 'path=' + path : '');
}

function storeScrollPosition(event) {
    event = getEvent(event);

    var target = getTarget(event);
    var scrollableElement = getScrollableElement(target);
    if (scrollableElement && target) {
        var pos = target.scrollTop;
        if (scrollableElement.nodeName == 'DIV' ||
            scrollableElement.nodeName == 'TBODY') {
            var targetId = scrollableElement.getAttribute("id");
            setCookie("scroll_pos", "" + pos, 1);
            setCookie("scroll_target", "" + targetId, 1);
        }
    }
}

function restoreScrollPosition() {
    var pos = getCookie("scroll_pos");
    var target = getCookie("scroll_target");
    var el = document.getElementById(target);
    if (el) {
        el.scrollTop = pos;
    }
}

function getScrollableElement(el) {
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
}

function storeFocus(event) {
    event = getEvent(event);
    var target = getTarget(event);

    var div = getParentWearingAttribute(target, "eid");
    var body = getParentByTagName(target, "BODY");
    /* Avoid rembering FORM as focus component as this automatically gains
       focus on pressing Enter in MSIE. */
    if (div && body && div.tagName != "FORM") {
        setCookie(body.getAttribute("id") + "_focus", div.getAttribute("id"), 1);
    }
}

var wu_dom = document.getElementById?1:0;
var wu_ns4 = (document.layers && !wu_dom)?1:0;
var wu_ns6 = (wu_dom && !document.all)?1:0;
var wu_ie5 = (wu_dom && document.all)?1:0;
var wu_konqueror = wu_checkUserAgent('konqueror')?1:0;
var wu_opera = wu_checkUserAgent('opera')?1:0;
var wu_safari = wu_checkUserAgent('safari')?1:0;

function wu_checkUserAgent(string) {
    return navigator.userAgent.toLowerCase().indexOf(string) + 1;
}

/* The following two functions are a workaround for IE to open a link in the
   right target/new window used in AnchorCG. */
function wu_checkTarget(target) {
    for (var i = 0; i < parent.frames.length; i++) {
        if (parent.frames[i].name == target) return true;
    }
    return false;
}

function wu_openlink(target, url, scriptCodes) {
  if (invokeScriptListeners(scriptCodes)) {
      // if the target exists => change URL, else => open URL in new window
      if (target == null) {
          window.location.href = url;
      } else {
          if (wu_checkTarget(target)) {
              parent.frames[target].location.href = url;
          } else {
              window.open(url, target);
          }
      }
  }
}

/* Utility method to determine available inner space of the show window on
   all browsers. Returns a numeric value of available pixel width. */
function wu_framewidth() {
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
}

/* Cross-browser method to register an event listener on the passed object. Only
   Mozilla will support captive mode of event handling. The 'eventType' is without
   the 'on'-prefix. Example: wu_registerEvent(document,'focus',storeFocus,false); */
function wu_registerEvent(obj, eventType, func, useCaption) {
    if (obj.addEventListener) {
        obj.addEventListener(eventType, func, useCaption);
        return true;
    } else if (obj.attachEvent) {
        var retVal = object.attachEvent("on" + eventType, func);
        return retVal;
    } else {
        return false;
    }
}

function wu_toolTip(event, element) {
    domTT_activate(element, event, "content", element.getAttribute("tip"), "predefined", "default");
    return true;
}

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
    if (incrementalUpdateCursor.enabled) {
      AjaxActivityCursor.init();
    }
    hideAjaxActivityIndicator();
    for (var i = 0; i < windowOnLoads.length; i++) {
        eval(windowOnLoads[i]);
    }
}

// =============================================================================

/* Shows the modal dialog at the center of the component. (SFrame or SInternalFrame) */
function showModalDialog(dialogId, modalId) {
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
            positionX += absLeft(parent);
            positionY += absTop(parent);
            modalDialog.style.left = absLeft(parent) + 'px';
            modalDialog.style.top = absTop(parent) + 'px';
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
}

function layoutScrollPane(outerId) {
    var outer = document.getElementById(outerId);
    var div = outer.getElementsByTagName("div")[0];
    div.style.height =
        document.defaultView.getComputedStyle(outer, null).getPropertyValue("height");
    div.style.display = "block";
}

function layoutScrollPaneIE(outerId) {
    var outer = document.getElementById(outerId);
    var div = outer.getElementsByTagName("div")[0];
    var td = getParentByTagName(div, "TD");
    div.style.height = td.clientHeight + "px";
    div.style.width = td.clientWidth + "px";
    div.style.position = "absolute";
    div.style.display = "block";
    div.style.overflow = "auto";
}

function layoutAvailableSpaceIE(tableId) {
	var table = document.getElementById(tableId);
    if (table.style.height == table.getAttribute("layoutHeight")) return;

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
}

/* Calculates the absolute position of the element to the left. */
function absLeft(el) {
    return (el.offsetParent) ? el.offsetLeft + absLeft(el.offsetParent) : el.offsetLeft;
}

/* Calculates the absolute position of the element to the top. */
function absTop(el) {
    return (el.offsetParent) ? el.offsetTop + absTop(el.offsetParent) : el.offsetTop;
}

// =============================================================================

var filterOnAjaxRequest = new Array("addWindowOnLoadFunction"); // AJAX filters

/* At some points wingS uses functions to dynamically add callback methods which
   will be executed when special events occur, i.e. addWindowOnLoadFunction().
   Functions added via this method are executed when the page is loaded. However,
   this only makes sense on full page updates because there will be no reload on
   incremental updates. So in this case we have to invoke the desired callback
   function directly. In order to indicate that a funtion has to be filtered on
   an AJAX request, you simply have to add it to the "filterOnAjaxRequest" array. */
for (var i = 0; i < filterOnAjaxRequest.length; i++) {
    filterOnAjaxRequest[i] = new RegExp().compile(".*" + filterOnAjaxRequest[i] +
                             "\\s*\\(\\s*('(.*)'|\"(.*)\")\\s*\\)\\s*;.*", "g");
    // without escaping: /.*<FUNCTION NAME>\s*\(\s*('(.*)'|"(.*)")\s*\)\s*;.*/g
}

/* Prints some hidden debugging infos about the given AJAX request at the bottom
   of the page. This is done by dynamically attaching a dedicated textarea. */
function enableAjaxDebugging(request) {
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
    debug.getElementsByTagName("textarea")[0].value = txt;
    debug.getElementsByTagName("span")[0].innerHTML = "| " + txt.length + " chars";
}

/* This function shows the previously enabled AJAX debugging view, if available. */
function showAjaxDebugging() {
    var debug = document.getElementById("ajaxDebugging");
    if (debug != null) debug.style.display = "block";
}

/* This function hides the previously enabled AJAX debugging view, if available. */
function hideAjaxDebugging() {
    var debug = document.getElementById("ajaxDebugging");
    if (debug != null) debug.style.display = "none";
}

function handleRequestError(request) {
    var errorMsg = "An error occured while processing an AJAX request!  -->  " +
                   "Status code: " + request.status + " | " + request.statusText +
                   "\n\n\nResponse from the server:\n" + request.responseText;
    hideAjaxActivityIndicator();
    alert(errorMsg);
    dequeueNextRequest();
}

function processAjaxRequest(request) {
    enableAjaxDebugging(request);

    // Get the received XML response
    var xmlDoc = request.responseXML;
    // In case we do not get any XML
    if (xmlDoc == null) {
        hideAjaxActivityIndicator();
        window.location.href = request.url;
        return;
    }

    // Private convenience function
    function getFirstChildData(tagName) {
        return xmlDoc.getElementsByTagName(tagName)[0].firstChild.data;
    }
    // Get the root element of the received XML response
    var xmlRoot = xmlDoc.getElementsByTagName("update")[0];
    // Process the response depending on the update mode
    var updateMode = xmlRoot.getAttribute("mode");
    if (updateMode == "complete") {
        window.location.href = getFirstChildData("redirect");
    }
    else if (updateMode == "incremental") {
        var components = xmlRoot.getElementsByTagName("component");
        if (components.length > 0) {
            var componentIds = new Array();
            // Replace HTML needed for component updates
            for (var i = 0; i < components.length; i++) {
                var id = components[i].getAttribute("id");
                var html = components[i].firstChild.data;
                replaceComponentHtml(id, html);
                componentIds.push(id);
            }
            // Execute scripts needed for component updates
            var scripts = xmlRoot.getElementsByTagName("script");
            for (var i = 0; i < scripts.length; i++) {
                var code = scripts[i].firstChild.data;
                try {
                    for (var j = 0; j < filterOnAjaxRequest.length; j++) {
                        // Either $2 or $3 will always be an empty string
                        code = code.replace(filterOnAjaxRequest[j], "$2$3;");
                    }
                    eval(code);
                } catch(e) {
                    alert(e);
                }
            }
            // Update the event epoch of this frame
            event_epoch = getFirstChildData("event_epoch");
            // Hightlight the components updated above
            if (incrementalUpdateHighlight.enabled) {
                highlightComponentUpdates(componentIds);
            }
        }
    }
    hideAjaxActivityIndicator();
    // Send next queued request
    dequeueNextRequest();
}

/* Replaces the old HTML code of the component with the given ID by the new one.
   In other words, this methods acts like a cross-browser "outerHTML"-method. */
function replaceComponentHtml(id, html) {
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
        var lastChildElementTag = null;
        for (var i = 0; i < parent.childNodes.length; i++) {
            // We have to filter everything except element nodes
            // since browsers treat whitespace nodes differently
            if (parent.childNodes[i].nodeType == 1) {
                nrOfChildElements++;
                lastChildElementTag = parent.childNodes[i].tagName;
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
}

/* Highlights the components with the given IDs for a certain time interval with a
   specific background color. The duration and color that will be used is defined by
   the properties with the same name of the "incrementalUpdateHighlight"-object. */
function highlightComponentUpdates(componentIds) {
    for (var i = 0; i < componentIds.length; i++) {
        var component = document.getElementById(componentIds[i]);
        if (component == null) return;
        highlightComponent(component);
    }

    function highlightComponent(component) {
        var initialBackgroundColor = component.style.backgroundColor;
        component.style.backgroundColor = incrementalUpdateHighlight.color;
        var resetColor = function() {
            component.style.backgroundColor = initialBackgroundColor;
        };
        setTimeout(resetColor, incrementalUpdateHighlight.duration);
    }
}

/* Shows the AJAX activity cursor and makes a user-predefined element with the CSS
   ID "ajaxActivityIndicator" visible. The latter is typically an animated GIF. */
function showAjaxActivityIndicator() {
    if (incrementalUpdateCursor.enabled) AjaxActivityCursor.show();
    var indicator = document.getElementById("ajaxActivityIndicator");
    if (indicator != null) {
        indicator.style.visibility = "visible";
    }
}

/* Hides the AJAX activity cursor and makes a user-predefined element with the CSS
   ID "ajaxActivityIndicator" invisible. The latter is typically an animated GIF. */
function hideAjaxActivityIndicator() {
    if (incrementalUpdateCursor.enabled) AjaxActivityCursor.hide();
    var indicator = document.getElementById("ajaxActivityIndicator");
    if (indicator != null && !AjaxRequest.isActive()) {
        indicator.style.visibility = "hidden";
    }
}

/* An object that encapsulates functions to show and hide an animated GIF besides
   the mouse cursor. Such a cursor can be used to indicate an active AJAX request. */
var AjaxActivityCursor = {
    dx : 0,
    dy : 15,
    div : false,

    // Initialize cursor
    init : function () {
    AjaxActivityCursor.dx = incrementalUpdateCursor.dx;
    AjaxActivityCursor.dy = incrementalUpdateCursor.dy;
        AjaxActivityCursor.div = document.createElement("div");
        AjaxActivityCursor.div.style.position = "absolute";
        AjaxActivityCursor.div.style.zIndex = "1000";
        AjaxActivityCursor.div.style.display = "none";
        AjaxActivityCursor.div.innerHTML = "<img src=\"" + incrementalUpdateCursor.image + "\"/>";
        document.body.insertBefore(AjaxActivityCursor.div, document.body.firstChild);
        document.onmousemove = AjaxActivityCursor.followMouse;
    },

    // Callback function
    followMouse : function (event) {
        var pos, isIE;
        event = getEvent(event);
        pos = { left : event.clientX, top : event.clientY };
        isIE = (window.document.compatMode &&
                window.document.compatMode == "CSS1Compat") ?
                    window.document.documentElement : window.document.body || null;
        if (isIE) {
            pos.left += isIE.scrollLeft;
            pos.top += isIE.scrollTop;
        }
        AjaxActivityCursor.div.style.left = pos.left + AjaxActivityCursor.dx + "px";
        AjaxActivityCursor.div.style.top = pos.top + AjaxActivityCursor.dy + "px";
    },

    // Show cursor
    show : function () {
        AjaxActivityCursor.div.style.display = "block";
        return false;
    },

    // Hide cursor
    hide : function () {
        AjaxActivityCursor.div.style.display = "none";
        return false;
    }
};