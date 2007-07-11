/***************************************************************************************************
 * WINGS.UTIL  --  contains: functions used to do common tasks
 **************************************************************************************************/

// Create module namespace
wingS.namespace("util");


wingS.util.getReloadResource = function() {
    return wingS.global.eventEpoch + "-" + wingS.global.reloadResource;
};

wingS.util.getUpdateResource = function() {
    return wingS.global.eventEpoch + "-" + wingS.global.updateResource;
};

wingS.util.invokeScriptCodeArray = function(scriptCodeArray) {
    if (scriptCodeArray) {
        for (var i = 0; i < scriptCodeArray.length; i++) {
            invokeNext = scriptCodeArray[i]();
            if (invokeNext == false) return false;
        }
    }
    return true;
};

/**
 * Returns an array of elements with the specified properties.
 * @param {Object} parent - the element whose children will be processed
 * @param {String} tagName - the tag name of the elements to look in (use * for all)
 * @param {String} attributeName - the name of the attribute to look for
 * @param {String} attributeValue - the value of the attribute to look for (optional)
 */
wingS.util.getElementsByAttribute = function(parent, tagName, attributeName, attributeValue) {
    var elements = (tagName == "*" && parent.all) ?
                   parent.all : parent.getElementsByTagName(tagName);
    var value = (typeof attributeValue != "undefined") ?
                new RegExp("(^|\\s)" + attributeValue + "(\\s|$)") : null;
    var element;
    var attribute;
    var result = new Array();
    for (var i = 0; i < elements.length; i++) {
        element = elements[i];
        attribute = element.getAttribute && element.getAttribute(attributeName);
        if (typeof attribute == "string" && attribute.length > 0) {
            if (typeof attributeValue == "undefined" || (value && value.test(attribute))) {
                result.push(element);
            }
        }
    }
    return result;
};

wingS.util.getParentByAttribute = function(element, attributeName) {
    var attribute;
    while (element != null) {
        attribute = element.getAttribute && element.getAttribute(attributeName);
        if (typeof attribute == "string" && attribute.length > 0) {
            return element;
        }
        element = element.parentNode;
    }
    return null;
};

wingS.util.getParentByAttributeAndValue = function(element, attributeName, attributeValue) {
    var attribute;
    while (element != null) {
        attribute = element.getAttribute && element.getAttribute(attributeName);
        if (attribute == attributeValue) {
            return element;
        }
        element = element.parentNode;
    }
    return null;
};


wingS.util.getParentByTagName = function(element, tag) {
    while (element != null) {
        if (tag == element.tagName)
            return element;
        element = element.parentNode;
    }
    return null;
};

wingS.util.openLink = function(target, url, scriptCodeArray) {
  if (wingS.util.invokeScriptCodeArray(scriptCodeArray)) {
      // if the target exists => change URL, else => open URL in new window
      if (target == null) {
          wingS.request.sendRedirect(url);
      } else {
          for (var i = 0; i < parent.frames.length; i++) {
              if (parent.frames[i].name == target) {
                  parent.frames[target].location.href = url;
                  return;
              }
          }
          window.open(url, target);
      }
  }
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
 * Can be used to prevent a form submit. By calling 'return wingS.util.preventSubmit()' on the
 * input event 'onkeypress', false will be returned when the return key was hit and by that
 * avoiding a form submit.
 */
wingS.util.preventSubmit = function() {
  return !(window.event && window.event.keyCode == 13);
};

/**
 * All normal requests (except form submits) in a wingS application are done through this method.
 * @param {Object} element - the element on which text selection should be enabled or disabled
 * @param {boolean} prevent - true, if text selection should be prevented - false otherwise
 */
wingS.util.preventTextSelection = function(element, prevent) {
    element.onmousedown = function () { return prevent; }   // Mozilla
    element.onselectstart = function () { return prevent; } // IE
};

/**
 * Inserts a node into the childNodes array after the specified child node refChild. Note:
 * Because there is no function insertAfter, it is done by raping insertBefore.
 * @param {Object} newChild node to insert
 * @param {Object} refChild node to insert after
 */
wingS.util.insertAfter = function(newChild, refChild) {
    refChild.parentNode.insertBefore(newChild, refChild.nextSibling);
};

wingS.util.appendHTML = function(element, html) {
    if (element.insertAdjacentHTML) {
        element.insertAdjacentHTML("BeforeEnd", html);
    } else if (document.createRange) {
        var range = document.createRange();
        if (!range.selectNodeContents || !range.createContextualFragment) {
            return false;
        }
        range.selectNodeContents(element);
        var fragment = range.createContextualFragment(html);
        element.appendChild(fragment);
    } else {
        return false;
    }
    return true;
};

/**
 * Search and return the first HTML element with the given tag name inside the HTML code generated
 * by wings for the passed component id. This function is i.e. helpful if you want to modify i.e.
 * the INPUT element of a STextField which is probably wrapped into TABLE elements wearing the
 * component ID generated by wingS for layouting purposes.
 */
wingS.util.findElement = function(id, tagname) {
    var div = document.getElementById(id);
    if (div) {
        var elements = div.getElementsByTagName(tagname);
        if (elements && elements.length > 0)
            return elements[0];
    }
};

/**
 * Highlights the element with the given ID for a certain time span.
 * @param {String} id - the ID of the element to highlight
 * @param {String} color - the color to highlight with
 * @param {int} duration - the highlight duration in ms
 */
wingS.util.highlightElement = function(id, color, duration) {
    var initialColor = document.getElementById(id).style.backgroundColor;
    document.getElementById(id).style.backgroundColor = color;
    var resetColor = "document.getElementById('" + id + "').style." +
                     "backgroundColor = '" + initialColor + "';";
    setTimeout(resetColor, duration);
};

/**
 * Remove focus from a component and respect additonal custom script listeners attached by user.
 */
wingS.util.blurComponent = function(component, scriptCodeArray) {
    if (wingS.util.invokeScriptCodeArray(scriptCodeArray)) {
        component.blur();
    }
    return true;
};

/**
 * Set focus to a component and respect additonal custom script listeners attached by user.
 */
wingS.util.focusComponent = function(component, scriptCodeArray) {
    if (wingS.util.invokeScriptCodeArray(scriptCodeArray)) {
        component.focus();
    }
    return true;
};

/**
 * Set the focus to a component identified by a wingS id. Also do some heuristic trace-down to the
 * actual HTML element, i.e. a STextField renders as <table id=...><input...></table> but you want
 * the focus to be the input element and not the table element.
 */
wingS.util.requestFocus = function(id) {
    window.focus();
    var parent = document.getElementById(id);
    if (parent != null) {
    	if (!parent.getAttribute("foc")) {
    		parent.setAttribute("foc", id);
    	}
        if (parent.getAttribute("foc") == id) {
            if (parent.style.display != "none" && !parent.disabled) {
                // Workaround for IE6! Without timeout IE6 hangs the complete
                // GUI in case 'parent' is a combobox and the newly selected
                // index is smaller than before. Unbelieveable, but true!!!
                window.setTimeout(function() { parent.focus() }, 100);
            }
            return;
        }

        var tags = new Array("INPUT", "A", "SELECT", "TEXTAREA");
        for (var i = 0; i < tags.length; i++) {
            var elements = parent.getElementsByTagName(tags[i]);
            for (var j = 0; j < elements.length; j++) {
                var element = elements[j];
                if (element.getAttribute("foc") == id &&
                    element.style.display != "none" &&
                    !element.disabled) {
                    window.setTimeout(function() { element.focus() }, 100);
                    return;
                }
            }
        }
    }
};

wingS.util.storeFocus = function(event) {
    var target = wingS.event.getTarget(event);
    var eidProvider = wingS.util.getParentByAttribute(target, "eid");
    var body = wingS.util.getParentByTagName(target, "BODY");
    // Avoid rembering FORM as focus component as this automatically
    // gains focus on pressing Enter in MSIE.
    if (eidProvider && body && eidProvider.tagName != "FORM") {
        wingS.util.setCookie(body.id + "_focus", eidProvider.id, 1);
    }
};

wingS.util.getCookie = function(name) {
    var c = new Object();
    var i = 0;
    var clen = document.cookie.length;
    while (i < clen) {
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

wingS.util.checkUserAgent = function(string) {
    return navigator.userAgent.toLowerCase().indexOf(string) + 1;
};

wingS.util.handleBodyClick = function(event) {
    if (window.wpm_handleBodyClicks != undefined) {
        wpm_handleBodyClicks(event);
    }
};

/**
 * Shows the modal dialog at the center of the component. (SFrame or SInternalFrame)
 */
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
    if (dialogWidth > window.innerWidth) {
        dialog.style.left = '0px';
    } else {
        dialog.style.left = (positionX - (dialogWidth / 2)) + 'px';
    }

    if (dialogHeight > window.innerHeight) {
        dialog.style.top = '0px';
    } else {
        dialog.style.top = (positionY - (dialogHeight / 2)) + 'px';
    }
    dialog.style.zIndex = 1000;
};

/**
 * Calculates the absolute position of the element to the left.
 */
wingS.util.absLeft = function(el) {
    return (el.offsetParent) ? el.offsetLeft + wingS.util.absLeft(el.offsetParent) : el.offsetLeft;
};

/**
 * Calculates the absolute position of the element to the top.
 */
wingS.util.absTop = function(el) {
    return (el.offsetParent) ? el.offsetTop + wingS.util.absTop(el.offsetParent) : el.offsetTop;
};

/**
 * Alerts all fields/elements of a given object. NB: you will also get object methods (which are
 * function valued properties). helper function to debug
 * @param {Object} obj
 */
wingS.util.printAllFields = function(obj) {
    for(var i in obj) {
        logDebug(obj[i], obj);
    }
};

