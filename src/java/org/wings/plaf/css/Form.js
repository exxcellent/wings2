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

function submitForm(event, eventName, eventValue) {
    event = getEvent(event);
    var form = getParentByTagName(getTarget(event), "FORM");

    if ( form != null ) {
        var eventNode = document.createElement("input");
        eventNode.setAttribute('type', 'hidden');
        eventNode.setAttribute('name', eventName);
        eventNode.setAttribute('value', eventValue);
        form.appendChild(eventNode);
        form.submit();
    } else {
        alert("form not found for " + getTarget(event));
    }

    return false;
}

function getParentByTagName(element, tag) {
  while (element != null) {
    if (tag.toUpperCase() == element.tagName.toUpperCase())
      return element;
    element = element.parentNode;
  }
  return null;
}