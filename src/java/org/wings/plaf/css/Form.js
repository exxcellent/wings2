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

function preventDefault(event) {
  if (event.preventDefault)
    event.preventDefault();
}

function sendEvent(event, eventValue) {
    event = getEvent(event);
    var form = getParentByTagName(getTarget(event), "FORM");
    var div = getParentByTagName(getTarget(event), "DIV");
    var eventName = div.getAttribute("event");

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

function getParentByTagName(element, tag) {
  while (element != null) {
    if (tag == element.tagName)
      return element;
    element = element.parentNode;
  }
  return null;
}
