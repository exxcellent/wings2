/* BUGFIX: adds a hidden form field for identification of button events in IE */
function addHiddenField(form, elementname, value) {
    var doc = window.document;
    // ID-value has to be unique
    var unique_id = form.name+"_"+elementname;
    var existingInput = doc.getElementById(unique_id);
    
    if(existingInput){
      form.removeChild(existingInput);    
    }

    var input = doc.createElement("input");
    input.setAttribute("type", "hidden");
    input.setAttribute("name", elementname);
    input.setAttribute("id", unique_id);
    input.setAttribute("value", value);
    form.appendChild(input);
}

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

function preventDefault(event) {
    if (event.preventDefault)
        event.preventDefault();
    else
        alert("!event.preventDefault");
    event.cancelBubble = true;
}

function sendEvent(event, eventValue, eventName) {
    event = getEvent(event);
    var form = getParentByTagName(event.currentTarget, "FORM");
    if (!eventName) {
        var div = getParentByTagName(event.currentTarget, "DIV");
        eventName = div.getAttribute("eid");
    }

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

function requestFocus(id) {
    var div = document.getElementById(id);
	if (div) {
	    var elements = div.getElementsByTagName("INPUT");
	    for (var i = 0; i < elements.length; i++) {
	        if (elements[i].getAttribute("focus") == id) {
	            elements[i].focus();
	            return;
	        }
	    }
	    elements = div.getElementsByTagName("SELECT");
	    for (var i = 0; i < elements.length; i++) {
	        if (elements[i].getAttribute("focus") == id) {
	            elements[i].focus();
	            return;
	        }
	    }
	    elements = div.getElementsByTagName("TEXTAREA");
	    for (var i = 0; i < elements.length; i++) {
	        if (elements[i].getAttribute("focus") == id) {
	            elements[i].focus();
	            return;
	        }
	    }
	    elements = div.getElementsByTagName("A");
	    for (var i = 0; i < elements.length; i++) {
	        if (elements[i].getAttribute("focus") == id) {
	            elements[i].focus();
	            return;
	        }
	    }
	    /* this produces javascript errors on konqueror, so hide (2005-03-11) */
	    if (!(wu_konqueror)) {
		    elements = div.getElementsByTagName("BUTTON");
		    for (var i = 0; i < elements.length; i++) {
		        if (elements[i].getAttribute("focus") == id) {
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
         i = endstr + 2; // Leerzeichen nach ; überspringen
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
/*
    event = getEvent(event);

    var div = getParentByTagName(getTarget(event), "DIV");
    var body = getParentByTagName(getTarget(event), "BODY");
    if (div && body)
        setCookie(body.getAttribute("id"), "scroll_" + div.getAttribute("id"), 1);
        */
}

function storeFocus(event) {
    event = getEvent(event);

    var div = getParentByTagName(getTarget(event), "DIV");
    var body = getParentByTagName(getTarget(event), "BODY");
    if (div && body)
        setCookie(body.getAttribute("id") + "_focus", "focus_" + div.getAttribute("id"), 1);
}

