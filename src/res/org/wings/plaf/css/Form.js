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

function getParentByAttributeName(element, attribute) {
  while (element != null) {
      if (element.attributes && element.attributes.contains(attribute))
          return element;
    element = element.parentNode;
  }
  return null;
}

function preventDefault(event) {
    if (event.preventDefault)
        event.preventDefault();
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
        eidprovider = getParentByAttributeName(target, "eid");
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
        if ( form != null ) {
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

function requestFocus(id) {
    var div = document.getElementById(id);
	if (div) {
        if (div.getAttribute("foc") == id) {
            div.focus();
            return;
        }

        var elements = div.getElementsByTagName("INPUT");
	    for (var i = 0; i < elements.length; i++) {
	        if (elements[i].getAttribute("foc") == id) {
	            elements[i].focus();
	            return;
	        }
	    }
	    elements = div.getElementsByTagName("SELECT");
	    for (var i = 0; i < elements.length; i++) {
	        if (elements[i].getAttribute("foc") == id) {
	            elements[i].focus();
	            return;
	        }
	    }
	    elements = div.getElementsByTagName("TEXTAREA");
	    for (var i = 0; i < elements.length; i++) {
	        if (elements[i].getAttribute("foc") == id) {
	            elements[i].focus();
	            return;
	        }
	    }
	    elements = div.getElementsByTagName("A");
	    for (var i = 0; i < elements.length; i++) {
	        if (elements[i].getAttribute("foc") == id) {
	            elements[i].focus();
	            return;
	        }
	    }
	    /* this produces javascript errors on konqueror, so hide (2005-03-11) */
	    if (!(wu_konqueror)) {
		    elements = div.getElementsByTagName("BUTTON");
		    for (var i = 0; i < elements.length; i++) {
		        if (elements[i].getAttribute("foc") == id) {
		            elements[i].focus();
		            return;
		        }
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
         var endstr = document.cookie.indexOf (";", i);
         if (endstr == -1) endstr = document.cookie.length;

         var v = unescape(document.cookie.substring(i, endstr));
         var key = v.substring(0, v.indexOf("=", 0));
         var val = v.substring(v.indexOf("=") + 1);
         c[key] = val;
         i = endstr + 2; // Leerzeichen nach ; �berspringen
    }
    if(name) return c[name];
    return c;
}

function setCookie(name, value, days, path)
{
    if(!days) days = -1;
    var expire = new Date();
    expire.setTime(expire.getTime() + 86400000 * days);

    document.cookie = name + "=" + escape(value)
    +  "; expires=" + expire.toGMTString() + ";"
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
	} else {
		return arguments.callee(parent);
	}
}

function storeFocus(event) {
    event = getEvent(event);
    var target = getTarget(event);

    var div = getParentByAttributeName(target, "eid");
    var body = getParentByTagName(target, "BODY");
    if (div && body)
        setCookie(body.getAttribute("id") + "-focus", "focus_" + div.getAttribute("id"), 1);
}
