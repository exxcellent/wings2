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

function sendEvent(event, eventValue) {
    event = getEvent(event);
    var form = getParentByTagName(getTarget(event), "FORM");
    var div = getParentByTagName(getTarget(event), "DIV");
    var eventName = div.getAttribute("event");
    event.stopPropagation();
    event.stopImmediatePropagation();
    event.preventDefault();

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

    return false;
}

function requestFocus(id) {
    var div = document.getElementById(id);

    var elements = div.getElementsByTagName("INPUT");
    for (var i = 0; i < elements.length; i++) {
        if (elements[i].getAttribute("focus") == id) {
            elements[i].focus();
            return;
        }
    }
    var elements = div.getElementsByTagName("SELECT");
    for (var i = 0; i < elements.length; i++) {
        if (elements[i].getAttribute("focus") == id) {
            elements[i].focus();
            return;
        }
    }
    var elements = div.getElementsByTagName("TEXTAREA");
    for (var i = 0; i < elements.length; i++) {
        if (elements[i].getAttribute("focus") == id) {
            elements[i].focus();
            return;
        }
    }
    var elements = div.getElementsByTagName("A");
    for (var i = 0; i < elements.length; i++) {
        if (elements[i].getAttribute("focus") == id) {
            elements[i].focus();
            return;
        }
    }
    var elements = div.getElementsByTagName("BUTTON");
    for (var i = 0; i < elements.length; i++) {
        if (elements[i].getAttribute("focus") == id) {
            elements[i].focus();
            return;
        }
    }
}
