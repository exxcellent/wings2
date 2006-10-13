// faking a namespace
if (!wingS) {
	var wingS = new Object();
}
else if (typeof wingS != "object") {
	throw new Error("wingS already exists and is not an object");
}

/**
 * In place editor widget.
 * @param {Object} element id of element to build editor upon
 */
wingS.InplaceEditor = function(element, dwrSetter) {
    this.elementId = element;
    this.timeout   = 6000; // ms
    this.dwrSetter = dwrSetter;    
    
    this.EDITOR_DIV      = this.elementId + "_editor";
    this.EDITOR_TEXTAREA = this.elementId + "_editarea";
    this.EDITOR_STATUS   = this.elementId + "_status";
    
    this.registerTooltip();
    this.registerListeners();
    
};

wingS.InplaceEditor.prototype.registerListeners = function() {
    YAHOO.util.Event.addListener(this.elementId, "mouseover", this.highlightOn.bind(this));
    YAHOO.util.Event.addListener(this.elementId, "mouseout", this.highlightOff.bind(this));
    YAHOO.util.Event.addListener(this.elementId, "click", this.edit.bind(this));
};    

wingS.InplaceEditor.prototype.registerTooltip = function() {
    this.toolTip = new YAHOO.widget.Tooltip("myTooltip", {
        context:this.elementId, text:"click here to edit", showDelay:250 
    }); 	
};
	
wingS.InplaceEditor.prototype.highlightOn = function() {
    YAHOO.util.Dom.addClass(this.elementId, "editable");			
};
	
wingS.InplaceEditor.prototype.highlightOff = function() {
    YAHOO.util.Dom.removeClass(this.elementId, "editable");		
};

/**
 * removes editor functionality and restores original view 
 */
wingS.InplaceEditor.prototype.cleanUp = function() {		
    MochiKit.DOM.removeElement(this.EDITOR_DIV);
    MochiKit.Style.showElement(this.elementId);	
};

/**
 * save changes by sending to server
 */
wingS.InplaceEditor.prototype.saveChanges = function() {		    
    MochiKit.Style.hideElement(this.EDITOR_DIV);
            
    var statusDiv = DIV({'id': this.EDITOR_STATUS});    
    var statusText = document.createTextNode("Saving ...");
    statusDiv.appendChild(statusText);    
    
    wingS.util.insertAfter(statusDiv, $(this.elementId));
                
    this.dwrSetter.setText($(this.EDITOR_TEXTAREA).value, 
        {callback: this.successHandler.bind(this), 
         timeout: this.timeout, 
         errorHandler: this.timeoutHandler.bind(this)}
    );
};

/**
 * Timeout-Handler for saveChanges(). Restores the editor.
 */
wingS.InplaceEditor.prototype.timeoutHandler = function() {
    // TODO timeout message
    MochiKit.DOM.removeElement(this.EDITOR_STATUS);
    MochiKit.Style.showElement(this.EDITOR_DIV);
};

wingS.InplaceEditor.prototype.successHandler = function(data) {    
    MochiKit.DOM.removeElement(this.EDITOR_STATUS);    
    this.getTextSpan().innerHTML = data;
    MochiKit.Style.showElement(this.elementId);
    //getUpdates(); // call Stephans function to renew now dirty XInplaceEditor component
};    
    
/**
 * Returns the HTML element which contains the static text.
 */
wingS.InplaceEditor.prototype.getTextSpan = function() {
    return wingS.util.findElement(this.elementId, "span");        
}

wingS.InplaceEditor.prototype.edit = function() {			
    MochiKit.Style.hideElement(this.elementId); // hide the static text element
        
    var htmlElement = MochiKit.DOM.getElement(this.elementId);
        
    // generate editor div
    var editor = DIV({'id': this.EDITOR_DIV});

    // generate text area
    var textArea = TEXTAREA({'id': this.EDITOR_TEXTAREA, 'rows': 4, 'cols': 60} );    
    var textAreaText = document.createTextNode(this.getTextSpan().innerHTML);
    textArea.appendChild(textAreaText);
    editor.appendChild(textArea);
    var br = BR();
    editor.appendChild(br);

    // generate belonging buttons	
    var buttonSave = INPUT({'id': this.elementId+'_save', 'type': "button", 'value': "Speichern"});
    editor.appendChild(buttonSave);	
    var buttonCancel = INPUT({'id': this.elementId+'_cancel', 'type': "button", 'value': "Abbrechen"});
    editor.appendChild(buttonCancel);
    	
    wingS.util.insertAfter(editor, htmlElement);

    // register event handler    
    YAHOO.util.Event.addListener(this.elementId+'_cancel', "click", this.cleanUp.bind(this));
    YAHOO.util.Event.addListener(this.elementId+'_save', "click", this.saveChanges.bind(this));				
};