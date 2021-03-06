/*
   wingS 2 Utility JavaScript functions

   This file contains commonly used javascript which might be also useful to wings user.

   In order to avoid typical js namespace clutter, all functions and variables
   in this file should be prepended by the string "wu_".
*/

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

function getParentWithAttribute(element, attribute) {
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

/* Submits whole form and attaches an event name/value pair to submit.
   Calls onclick handlers in advance. */
function sendEvent(event, eventValue, eventName, clientHandlers) {
    event = getEvent(event);
    var target = getTarget(event)
    var form = getParentByTagName(target, "FORM");
    var eidprovider = target;
    if (!eventName) {
        eidprovider = getParentWithAttribute(target, "eid");
        eventName = eidprovider.getAttribute("eid");
    }

    var doSubmit = true;
    if (clientHandlers) {
        for (var i = 0; i < clientHandlers.length; i++) {
            doSubmit = clientHandlers[i]();
            if (doSubmit == false) break;
        }
    }

    if (doSubmit == undefined || doSubmit) {
        if (form != null) {
            var eventNode = document.createElement("input");
            eventNode.setAttribute('type', 'hidden');
            eventNode.setAttribute('name', eventName);
            eventNode.setAttribute('value', eventValue);
            form.appendChild(eventNode);
            form.submit();
        }
        else {
            document.location = "?" + eventName + "=" + eventValue;
        }
    }
}

/* JavaScript to follow a link without submitting the form.
   Calls concurrent onclick listeners. */
function followLink(url, clientHandlers) {
    var doSubmit = true;
    if (clientHandlers) {
        for (var i = 0; i < clientHandlers.length; i++) {
            doSubmit = clientHandlers[i]();
            if (doSubmit == false) break;
        }
    }

    if (doSubmit == undefined || doSubmit) {
        document.location = url;
    }

    return false;
}

/* Remove focus from a component and respect
   additonal custom script listeners attached
   by user.
   Core usage/doc see Utils.printButtonStart()*/
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

/* Set focus to a component and respect
   additonal custom script listeners attached
   by user.
   Core usage/doc see Utils.printButtonStart()*/
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

/* Search and return the first HTML element with the given tag name
   inside the HTML code generated by wings for the passed component id

   This function is i.e. helpful if you want to modify i.e. the
   INPUT element of a STextField which probably is wrapped into
   TABLE elements wearing the component ID generated by wingS
   for layouting purposes
*/
function wu_findElement(id, tagname) {
    var div = document.getElementById(id);
    if (div) {
        var elements = div.getElementsByTagName(tagname);
        if (elements && elements.length > 0)
            return elements[0];
    }
}

/* Set the focus to a component identified by a wingS id.
   Also do some heuristic trace-down of the real component mean.
   i.e. a STextFields renders as <table id=...><input...></table>
   but you want the focus to be the input element. Not the table element. */
function requestFocus(id) {
    var div = document.getElementById(id);
    window.focus = id;
    if (div) {
        if (div.getAttribute("foc") == id) {
            if (!div.disabled && div.style.display != "none")
                div.focus();
            return;
        }

        var elements = div.getElementsByTagName("INPUT");
        for (var i = 0; i < elements.length; i++) {
            var element = elements[i];
            if (element.getAttribute("foc") == id && !element.disabled && element.style.display != "none") {
                element.focus();
                return;
            }
        }
        elements = div.getElementsByTagName("A");
        for (var i = 0; i < elements.length; i++) {
            var element = elements[i];
            if (element.getAttribute("foc") == id && !element.disabled && element.style.display != "none") {
                element.focus();
                return;
            }
        }
    }
}

function getCookie(name)
{
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
        // Leerzeichen nach ; �berspringen
    }
    if (name) return c[name];
    return c;
}

function setCookie(name, value, days, path)
{
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
        if (scrollableElement.nodeName == 'DIV' || scrollableElement.nodeName == 'TBODY') {
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

    var div = getParentWithAttribute(target, "eid");
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

/*
the following two functions are a workaround for IE to open a link in the right target/new window
used in AnchorCG
*/

/*
check if a target exists
*/
function wu_checkTarget(target) {
    for (var i = 0; i < parent.frames.length; i++) {
        if (parent.frames[i].name == target)
            return true;
    }
    return false;
}
/*
if the target exists => change URL, else => open URL in new window
*/
function wu_openlink(target, url) {
    if (target == null) {
        location.href = url;
    }
    else {
        if (wu_checkTarget(target)) {
            parent.frames[target].location.href = url;
        }
        else {
            window.open(url, target);
        }
    }
}

/* Utility method to determine available inner space of the show window on
   all browsers. Returns a numeric value of available pixel width */
function wu_framewidth() {
    if (self.innerHeight) // all except Explorer
    {
        return self.innerWidth;
    }
    else if (document.documentElement && document.documentElement.clientHeight)
    // Explorer 6 Strict Mode
    {
        return document.documentElement.clientWidth;
    }
    else if (document.body) // other Explorers
    {
        return document.body.clientWidth;
    }
    else
        return -1;
}

/*
  Cross-Browser mehtod to register an event listener on the passed obj.
  Only Mozilla will support captive mode of event handling.
  eventType is withouth the onPrefix.
  Example: wu_addEvent(document,'focus',storeFocus,false);
*/
function wu_registerEvent(obj, eventType, func, useCaption) {
    if (obj.addEventListener) {
        obj.addEventListener(eventType, func, useCaption);
        return true;
    }
    else if (obj.attachEvent) {
        var retVal = object.attachEvent("on" + eventType, func);
        return retVal;
    }
    else {
        return false;
    }
}

/*
 *  SFormattedTextField JavaScript Code
 */
function ftextFieldCallback(result) {
    var elem    = document.getElementById(result[0]);
    var data    = result[1];
    var invalid = result[2];
    if (!elem)
        return; // dwr bug
    if (invalid) {
        elem.style.color = '#ff0000';
    } else {
        elem.style.color = '';
    }
    elem.value = data;
}

function spinnerCallback(result) {
    var elem = document.getElementById(result[0]);
    if (!elem)
        return; // dwr bug
    var data = result[1];
    if (data) {
        elem.value = data;
        elem.setAttribute("lastValid", data);
    }
}

/**
 * Adds a function to the window.onresize functionality.
 * This allows you to execute more than one function.
 */
windowOnResizes = new Array();
function addWindowOnResizeFunction(func) {
    windowOnResizes.push(func);
}

/**
 * The execution of all added window.onresize functions.
 */
window.onresize = performWindowOnResize;
function performWindowOnResize() {
    for (var i = 0; i < windowOnResizes.length; i++) {
        eval(windowOnResizes[i]);
    }
}

/**
 * Adds a function to the window.onload functionality.
 * This allows you to execute more than one function.
 */
windowOnLoads = new Array();
function addWindowOnLoadFunction(func) {
    windowOnLoads.push(func);
}

/**
 * The execution of all added window.onload functions.
 */
window.onload = performWindowOnLoad;
function performWindowOnLoad() {
    for (var i = 0; i < windowOnLoads.length; i++) {
        eval(windowOnLoads[i]);
    }
}

/**
 * Shows the modal dialog at the center of the component.
 * (SFrame or SInternalFrame)
 */
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

    if (dialogWidth > window.innerWidth) {
        dialog.style.left = '0px';
    } else {
        dialog.style.left = (positionX - (dialogWidth / 2)) + 'px';
    }

    if(dialogHeight > window.innerHeight) {
        dialog.style.top = '0px';
    } else {
        dialog.style.top = (positionY - (dialogHeight / 2)) + 'px';
    }
    
    dialog.style.zIndex = 99;
}

function layoutFill(table) {
  if (table.style.height == table.getAttribute("layoutHeight"))
    return;

  var consumedHeight = 0;
  var rows = table.rows;
  for (var i=0; i < rows.length; i++) {
    var row = rows[i];

    var yweight = row.getAttribute("yweight");
    if (!yweight)
      consumedHeight += row.offsetHeight;
  }

  /*
  if (table.getAttribute("layoutHeight") == "100%") {
    var height = table.parentNode.clientHeight;
    var oversize = table.parentNode.oversize;
    if (oversize)
      height -= oversize;
    table.style.height = height + "px";
  }
  else
  */
    table.style.height = table.getAttribute("layoutHeight");

  var diff = table.clientHeight - consumedHeight;

  for (var i=0; i < rows.length; i++) {
    var row = rows[i];
    var yweight = row.getAttribute("yweight");
    if (yweight) {
      var oversize = row.getAttribute("oversize");
      row.height = Math.max(Math.floor((diff * yweight) / 100) - oversize, oversize);
    }
  }
}

function layoutFix(table) {
  var consumedHeight = 0;
  var rows = table.rows;
  for (var i=0; i < rows.length; i++) {
    var row = rows[i];
    consumedHeight += row.offsetHeight;
  }

  table.style.height = consumedHeight + "px";
}

function layoutScrollPane(table) {
    var div = table.getElementsByTagName("div")[0];

    if (document.defaultView) {
        div.style.height = document.defaultView.getComputedStyle(table, null).getPropertyValue("height");
        div.style.display = "block";
    }
    else {
        var parent_td = getParentByTagName(div, "TD");
        div.style.height = parent_td.clientHeight + "px";
        div.style.width = parent_td.clientWidth + "px";
        div.style.position = 'absolute';
        div.style.display = 'block';
        //div.style.overflow = 'auto';
    }
}

/**
 * Calculates the absolute position of the element to the left.
 */
function absLeft(el) {
    return (el.offsetParent) ? el.offsetLeft + absLeft(el.offsetParent) : el.offsetLeft;
}

/**
 * Calculates the absolute position of the element to the top.
 */
function absTop(el) {
    return (el.offsetParent) ? el.offsetTop + absTop(el.offsetParent) : el.offsetTop;
}

function wu_toolTip(event, element) {
    domTT_activate(element, event, 'content', element.getAttribute('tip'), 'predefined', 'default');
    return true;
}