/***************************************************************************************************
 * WINGS.REQUEST  --  contains: functions used to send new request
 **************************************************************************************************/


wingS.request.submitForm = function(ajaxEnabled, event, eventName, eventValue, scriptCodes) {
    var submitForm = wingS.request.submitForm;
    // Enqueue this request if another one hasn't been processed yet
    if (wingS.request.enqueueThisRequest(submitForm, submitForm.arguments)) return;

    // Needed preparations
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

    if (wingS.request.invokeScriptListeners(scriptCodes)) {

        if (form != null) {
            // Generate unique IDs for the nodes we have to insert
            // dynamically into the form (workaround because of IE)
            var formId = form.getAttribute("id");
            var epochNodeId = "event_epoch_" + formId;
            var triggerNodeId = "event_trigger_" + formId;

            //var debug = "Elements before: " + form.elements.length;

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
            epochNode.setAttribute("value", wingS.global.event_epoch);

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

            //debug += "\nElements after: " + form.elements.length;
            //for (var i = 0; i < form.elements.length; i++) {
            //    debug += "\n - name: " + form.elements[i].name +
            //             " | value: " + form.elements[i].value;
            //}
            //alert(debug);

            var submitted = false;
            // Form submit by means of AJAX
            if (wingS.global.incrementalUpdateEnabled && ajaxEnabled) {
                form.action = wingS.util.encodeUpdateId(wingS.global.incrementalUpdateId);
                submitted = wingS.ajax.doAjaxSubmit(form);
            }
            // Always (re-)set the form's action to the URL of the CompleteUpdateResource,
            // since this resource should remain the default that will be used whenever a
            // form is NOT submitted via this method - even though it should!
            form.action = wingS.util.encodeUpdateId(wingS.global.completeUpdateId);
            // Default form submit (fallback mechanism)
            if (!submitted) form.submit();
        } else {
            // If we've got a form, it might be alright to submit it
            // without having an "eventName" or "eventValue". This is
            // because all form components are automatically in their
            // "correct" state BEFORE the submit takes place - this is
            // the way HTML functions. However, if we've got no form,
            // we need to send the name and the value of the component
            // which generated the event we want to process. Let's go!
            if (eventName == null) {
                eventName = target.getAttribute("id");
                var eventNode = document.getElementById(eventName);
                if (eventNode.value) eventValue = eventNode.value;
            }
            wingS.request.followLink(ajaxEnabled, eventName, eventValue);
        }
    }
};

wingS.request.followLink = function(ajaxEnabled, eventName, eventValue, scriptCodes) {
    var followLink = wingS.request.followLink;
    // Enqueue this request if another one hasn't been processed yet
    if (wingS.request.enqueueThisRequest(followLink, followLink.arguments)) return;

    if (wingS.request.invokeScriptListeners(scriptCodes)) {
        if (wingS.global.incrementalUpdateEnabled && ajaxEnabled) {
            // Send request via AJAX
            var args = {};
            args.method = "GET";
            if (eventName != null && eventValue != null) {
                args.event_epoch = wingS.global.event_epoch;
                args[eventName] = eventValue;
            }
            args.url = wingS.util.encodeUpdateId(wingS.global.incrementalUpdateId);
            wingS.ajax.doAjaxRequest(args);
        } else {
            // Send a default HTTP request
            url = wingS.util.encodeUpdateId(wingS.global.completeUpdateId);
            window.location.href = url + "?event_epoch=" + wingS.global.event_epoch +
                                   "&" + eventName + "=" + eventValue;
        }
    }
};

wingS.request.enqueueThisRequest = function(send, args) {
    if (AjaxRequest.isActive()) {
        wingS.global.requestQueue.push( {"send" : send, "args" : args} );
        return true;
    }
    return false;
};

wingS.request.dequeueNextRequest = function() {
    if (wingS.global.requestQueue.length > 0) {
        var request = wingS.global.requestQueue.shift();
        var args = request.args;
        request.send(args[0], args[1], args[2], args[3]);
    }
};

wingS.request.invokeScriptListeners = function(scriptCodes) {
    if (scriptCodes) {
        for (var i = 0; i < scriptCodes.length; i++) {
            invokeNext = scriptCodes[i]();
            if (invokeNext == false) return false;
        }
    }
    return true;
};