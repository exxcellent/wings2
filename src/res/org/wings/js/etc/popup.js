// faking a namespace
if (!wingS) {
    var wingS = new Object();
}
else if (typeof wingS != "object") {
    throw new Error("wingS already exists and is not an object");
}

/**
 * Creates an ajaxified popup.
 *
 * @param {String} id element id of the popup to be created
 * @param {String} dwrObject DWR interface object to communicate with the server
 * @param {int} x position (origin top left)
 * @param {int} y position (origin top left)
 * @param {int} width
 * @param {int} height
 * @param {String} context element to anchor the popup to
 * @param {String} contentsCorner "tr"|"tl"|"br"|"bl"
 * @param {String} contextCorner "tr"|"tl"|"br"|"bl"
 *
 * for information about contentsCorner and contextCorner see 
 * http://developer.yahoo.com/yui/container/overlay/index.html
 *
 * @author Christian Schyma
 */
wingS.Popup = function(id, dwrObject, x, y, width, height, context,
			contentsCorner, contextCorner) {    
    this.id             = id;    
    this.x              = x;
    this.y              = y;
    this.width          = width;
    this.height         = height;    
    this.context        = context;    	
    this.contentsCorner = contentsCorner;
    this.contextCorner  = contextCorner;    
    this.timeout        = 5000;    
    
    // method has to be exposed by DWR   
    this.dwrGetter      = dwrObject["getRenderedContent"];
    
    this.hideRequest       = false;
    this.requestingContent = false;
    this.popup;	               
};

/**
 * Shows this popup.
 *
 * Sends a request to the server. The actual rendering will be done at
 * showPopupCallback().
 */
wingS.Popup.prototype.show = function show() {        
    if (!this.requestingContent) {
        this.hideRequest = false;
        wingS.ajax.setActivityIndicatorsVisible(true);
        this.requestingContent = true;        
               
        this.dwrGetter(
        {
            callback: this.showPopupCallback.bind(this),
            timeout: this.timeout, 
            errorHandler: this.errorHandler.bind(this)            
        });			
    }
}

/**
 * Response handler of show(). Renders and shows the popup.
 */
wingS.Popup.prototype.showPopupCallback = function showPopupCallback(response) {    
    this.requestingContent = false;    
    
    if (!this.hideRequest) {        
        // should the popup be anchored to an element?
        if (this.context) {        
            this.popup = new YAHOO.widget.Overlay(this.id,
            {   context:[this.context, this.contentsCorner, this.contextCorner],     	               
                width:   this.width + "px",
                height:  this.height + "px",                
                monitorresize: false        
            });                         
        // ... or set by a given coordinate
        } else {
            this.popup = new YAHOO.widget.Overlay(this.id, 
            {   xy:      [this.x, this.y],	              
                width:   this.width + "px",
                height:  this.height + "px",                
                monitorresize: false        
            });                
        }
        
        this.popup.setBody(response); 	
        this.popup.render(document.body);
                        
        this.popup.show();		   
                                
        wingS.ajax.setActivityIndicatorsVisible(false);                                       
    }
}

/**
 * Simple error handler.
 * @param {Object} message
 */
wingS.Popup.prototype.errorHandler = function errorHandler(message) {    
    alert(message);            
}

/**
 * Hides this popup.
 * 
 * Why does it get destroyed and not hidden? Because hide() does not clean 
 * up the DOM which will then lead to a steadily increasing DOM if wingS 
 * is in incremental update mode. 
 */
wingS.Popup.prototype.hide = function hide() {   
 
    if (this.requestingContent) {        
        this.hideRequest = true;
        wingS.ajax.setActivityIndicatorsVisible(false);
    }    

    if (this.popup)
        this.popup.destroy();
}
