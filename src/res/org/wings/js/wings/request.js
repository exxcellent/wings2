/***************************************************************************************************
 * WINGS.REQUEST  --  contains: functions used to send new request
 **************************************************************************************************/


/**
 * Create according namespace
 */
if (!wingS.request) {
    wingS.request = new Object();
} else if (typeof wingS.request != "object") {
    throw new Error("wingS.request already exists and is not an object");
}

/**
 * Sends a request to the ReloadResource attached to this frame thereby forcing a complete reload.
 */
wingS.request.reloadFrame = function() {
    window.location.href = wingS.util.getReloadResource();
};

/**
 * Each and every form submit that occurs within a wingS application is done through this method.
 * @param {boolean} ajaxEnabled - true if the form should be submitted by an asynchronous request
 * @param {Object} event - the event object
 * @param {String} eventName - the name of the event or component respectively
 * @param {String} eventValue - the value of the event or component respectively
 * @param {Array} scriptCodeArray - the scripts to invoke before submitting the form
 */
wingS.request.submitForm = function(ajaxEnabled, event, eventName, eventValue, scriptCodeArray) {
    var submitForm = wingS.request.submitForm;
    // Enqueue AJAX request and return if there is another one which has not been processed yet
    if (ajaxEnabled && wingS.ajax.enqueueThisRequest(submitForm, submitForm.arguments)) return;

    // Collect all the stuff we need
    event = wingS.events.getEvent(event);
    var target = wingS.events.getTarget(event);
    var form = wingS.util.getParentByTagName(target, "FORM");
    if (eventName != null) {
        var eidProvider = wingS.util.getParentWearingAttribute(target, "eid");
        if (eidProvider == null) {
            alert("[DEBUG] submitForm():\ntarget = " + target + "\nform = " + form);
            return;
        }
        eventName = eidProvider.getAttribute("eid");
    }

    if (wingS.util.invokeScriptCodeArray(scriptCodeArray)) {
        if (form != null) {
            // Generate unique IDs for the nodes we have to insert
            // dynamically into the form (workaround because of IE)
            var formId = form.getAttribute("id");
            var epochNodeId = "event_epoch_" + formId;
            var triggerNodeId = "event_trigger_" + formId;

            // var debug = "Elements before: " + form.elements.length;

            // Always encode the current event epoch
            var epochNode = document.getElementById(epochNodeId);
            if (epochNode == null) {
                // Append this node only once, then reuse it
                epochNode = document.createElement("input");
                epochNode.setAttribute("type", "hidden");
                epochNode.setAttribute("name", "event_epoch");
                epochNode.setAttribute("id", epochNodeId);
                form.appendChild(epochNode);
            }
            epochNode.setAttribute("value", wingS.global.eventEpoch);

            // Encode the event trigger if available
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

            // debug += "\nElements after: " + form.elements.length;
            // for (var i = 0; i < form.elements.length; i++) {
            //     debug += "\n - name: " + form.elements[i].name +
            //              " | value: " + form.elements[i].value;
            // }
            // alert(debug);

            // Submit the from, either via AJAX or the traditional way
            if (wingS.global.updateEnabled && ajaxEnabled) {
                wingS.ajax.doSubmit(form);
            } else {
                form.submit();
            }
        } else {
            // If we've got a form, it might be alright to submit it
            // without having an "eventName" or "eventValue". This is
            // because all form components are automatically in their
            // "correct" state BEFORE the submit takes place - this is
            // the way HTML functions. However, if we've got no form,
            // we need to send the name and the value of the component
            // which generated the event we want to process. I.e. this
            // is needed for textfields, textareas or comboboxes with
            // attached listeners (onChange="wingS.request.submitForm(
            // true, event)") that are then not placed inside a form.
            if (eventName == null) {
                eventName = target.getAttribute("id");
                var eventNode = document.getElementById(eventName);
                if (eventNode.value) eventValue = eventNode.value;
            }
            var data = wingS.request.encodeEvent(eventName, eventValue);

            // Send the event, either via AJAX or the traditional way
            if (wingS.global.updateEnabled && ajaxEnabled) {
                wingS.ajax.doRequest("POST", wingS.util.getUpdateResource(), data);
            } else {
                window.location.href = wingS.util.getReloadResource() + "?" + data;
            }
        }
    }
};

/**
 * All normal requests (except form submits) in a wingS application are done through this method.
 * @param {boolean} ajaxEnabled - true if the request should be invoked asynchronously
 * @param {String} eventName - the name of the event or component respectively
 * @param {String} eventValue - the value of the event or component respectively
 * @param {Array} scriptCodeArray - the scripts to invoke before sending the request
 */
wingS.request.followLink = function(ajaxEnabled, eventName, eventValue, scriptCodeArray) {
    var followLink = wingS.request.followLink;
    // Enqueue AJAX request and return if there is another one which has not been processed yet
    if (ajaxEnabled && wingS.ajax.enqueueThisRequest(followLink, followLink.arguments)) return;

    if (wingS.util.invokeScriptCodeArray(scriptCodeArray)) {
        var data = wingS.request.encodeEvent(eventName, eventValue, "?");

        // Send the event, either via AJAX or the traditional way
        if (wingS.global.updateEnabled && ajaxEnabled) {
            wingS.ajax.doRequest("GET", wingS.util.getUpdateResource() + data);
        } else {
            window.location.href = wingS.util.getReloadResource() + data;
        }
    }
};

/**
 * Encodes the given event with the frame's current event epoch and returns the generated string.
 * @param {String} eventName - the name of the event or component respectively
 * @param {String} eventValue - the value of the event or component respectively
 * @param {String} prefix - an optional string which is prepended to the result
 */
wingS.request.encodeEvent = function(eventName, eventValue, prefix) {
    var data = "";
    if (eventName != null && eventValue != null) {
        if (prefix != null) data += prefix;
        // We don't need to encode the stuff we send since this is already done on server side
        data += "event_epoch=" + wingS.global.eventEpoch + "&" + eventName + "=" + eventValue;
    }
    return data;
};