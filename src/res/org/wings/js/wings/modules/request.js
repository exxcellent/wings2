/***************************************************************************************************
 * WINGS.REQUEST  --  contains: functions used to send new request
 **************************************************************************************************/

// Create module namespace
wingS.namespace("request");


/**
 * Redirects the browser to the specified url.
 * @param {String} url - the url to redirect to
 */
wingS.request.sendRedirect = function(url) {
    window.location.href = url;
};

/**
 * Sends a request to the ReloadResource attached to this frame thereby forcing a complete reload.
 * @param {String} parameters - the parameter string that is encoded into the URL (optional)
 */
wingS.request.reloadFrame = function(parameters) {
    var reloadRequestUrl = wingS.util.getReloadResource();
    if (parameters != null) {
    	if (parameters.charAt(0) != "?") {
    		reloadRequestUrl += "?";
    	}
        reloadRequestUrl += parameters;
    }
    window.location.href = reloadRequestUrl;
};

/**
 * This method prepares any requests and dispatches them to the appropriate send method.
 * @param {Object} event - an object that provides information about the occured event
 * @param {boolean} submit - true, if this request entails a form submit
 * @param {boolean} async - true, if this request is sent asynchronously
 * @param {String} eventName - the name of the event or component respectively
 * @param {String} eventValue - the value of the event or component respectively
 * @param {Array} scriptCodeArray - the scripts to invoke before sending the request
 */
wingS.request.sendEvent = function(event, submit, async, eventName, eventValue, scriptCodeArray) {
    event = wingS.event.getEvent(event);

    var target;
    if (event != null)
        target = wingS.event.getTarget(event);
    else
        target = document.forms[0];

    // Prepare the appropriate method call
    var sendMethod;
    if (submit && target != null) {
        sendMethod = wingS.request.submitForm;
    } else {
        sendMethod = wingS.request.followLink;
    }
    var sendMethodArgs = new Array(target, async, eventName, eventValue, scriptCodeArray);

    // Enqueue asynchronous requests in case another one hasn't been processed yet
    if (async && wingS.ajax.enqueueThisRequest(sendMethod, sendMethodArgs)) return;
    // Otherwise instantly send the request by calling the appropriate send method
    sendMethod(target, async, eventName, eventValue, scriptCodeArray);
};

/**
 * Each and every form submit within a wingS application is done through this method.
 * @param {Object} target - the element in the DOM which initiated the form submit
 * @param {boolean} async - true, if the form should be submitted asynchronously
 * @param {String} eventName - the name of the event or component respectively
 * @param {String} eventValue - the value of the event or component respectively
 * @param {Array} scriptCodeArray - the scripts to invoke before submitting the form
 */
wingS.request.submitForm = function(target, async, eventName, eventValue, scriptCodeArray) {
    var form; // The form that we want to submit

    // Try to get the desired form from an according provider (i.e. this is
    // used by menues which reside out of any forms but want to submit one)
    var formProvider = wingS.util.getParentByAttribute(target, "form");

    if (formProvider != null) { // Use the form suggested by the provider
        form = document.getElementById(formProvider.getAttribute("form"));
    } else { // By default we walk up until we find the first form
        form = wingS.util.getParentByTagName(target, "FORM");
    }

    if (wingS.util.invokeScriptCodeArray(scriptCodeArray)) {
        if (form != null) {
            // Generate unique IDs for the nodes we have to insert
            // dynamically into the form (workaround because of IE)
            var formId = form.id;
            var epochNodeId = "event_epoch_" + formId;
            var triggerNodeId = "event_trigger_" + formId;

            // Uncomment this to debug the name/value pairs being sent
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

            // Uncomment this to debug the name/value pairs being sent
            // debug += "\nElements after: " + form.elements.length;
            // for (var i = 0; i < form.elements.length; i++) {
            //     debug += "\n - name: " + form.elements[i].name +
            //              " | value: " + form.elements[i].value;
            // }
            // alert(debug);

            // Submit the from...
            if (wingS.global.updateEnabled && async) {
                // asynchronously
                wingS.ajax.submitForm(form);
            } else {
                // traditionally
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
            // attached listeners (onChange="wingS.request.sendEvent(
            // event, true, true)") which are not placed inside a form.
            if (eventName == null) {
                eventName = target.id;
                var eventNode = document.getElementById(eventName);
                if (eventNode.value) eventValue = eventNode.value;
            }
            var data = wingS.request.encodeEvent(eventName, eventValue);

            // Send the event...
            if (wingS.global.updateEnabled && async) {
                // asynchronously
                wingS.ajax.sendRequest("POST", wingS.util.getUpdateResource(), data);
            } else {
                // traditionally
                wingS.request.reloadFrame(data);
            }
        }
    }
};

/**
 * Apart from form submits any request in a wingS application is handled by this method.
 * @param {Object} target - the element in the DOM which initiated the request
 * @param {boolean} async - true, if the request should be invoked asynchronously
 * @param {String} eventName - the name of the event or component respectively
 * @param {String} eventValue - the value of the event or component respectively
 * @param {Array} scriptCodeArray - the scripts to invoke before sending the request
 */
wingS.request.followLink = function(target, async, eventName, eventValue, scriptCodeArray) {
    if (wingS.util.invokeScriptCodeArray(scriptCodeArray)) {
        var data = wingS.request.encodeEvent(eventName, eventValue);

        // Send the event...
        if (wingS.global.updateEnabled && async) {
            // asynchronously
            wingS.ajax.sendRequest("GET", wingS.util.getUpdateResource() + "?" + data);
        } else {
            // traditionally
            wingS.request.reloadFrame(data);
        }
    }
};

/**
 * Encodes the given event with the frame's current event epoch and returns the generated string.
 * @param {String} eventName - the name of the event or component respectively
 * @param {String} eventValue - the value of the event or component respectively
 */
wingS.request.encodeEvent = function(eventName, eventValue) {
    var data = "";
    // In order to be consistent we do always encode
    // the epoch - even without any name/value pair.
    data += "event_epoch=" + wingS.global.eventEpoch

    if (eventName != null && eventValue != null) {
        // Encoding is already done on server side
        data += "&" + eventName + "=" + eventValue;
    }
    return data;
};

