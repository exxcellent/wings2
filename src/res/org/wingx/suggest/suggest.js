// TODO ... correct suggest div position for Opera

// faking a namespace
if (!wingS) {
    var wingS = new Object();
}
else if (typeof wingS != "object") {
    throw new Error("wingS already exists and is not an object");
}

/**
 * Creates a new suggest widget.
 * 
 * (based upon the example of Dave Crane, Eric Pascarello and Darren James 
 *  see 'Ajax in Action', Manning Publications Co. October 2005, ISBN: 1932394613)
 * 
 * @param {Object} inputId input field to extend with suggest functionality
 * @param {Object} populator function to call on input update
 * @param {Object} options see setOptions()
 * @author Christian Schyma
 */
wingS.Suggest = function(inputId, populatorFunc, options) {	
    this.id           = inputId;    
    this.textInput    = $(this.id);    
    this.populator    = populatorFunc;
    this.ignoreInput  = false;
    this.pendingInput = false;
    this.suggestions  = [];
    
    this.browser      = navigator.userAgent.toLowerCase();
    this.isIE         = this.browser.indexOf("msie") != -1;
    this.isOpera      = this.browser.indexOf("opera")!= -1;
    
    this.setOptions(options);		
    this.injectBehavior();        
};

/**
 * Set options. If no options given, set default values.
 * @param {Object} options object literal with the following optional attributes:
 * matchAnywhere: true|false, ignoreCase: true|false, maxSuggestions: int, 
 * preventSubmit: true|false, suggestDivClassName: string, suggestionClassName: string,
 * inputDelay: int in ms
 */
wingS.Suggest.prototype.setOptions = function(options) {
	
    // default values	           
    this.matchAnywhere       = false;			
    this.ignoreCase          = false;
    this.maxSuggestions      = 10;		// number of displayed suggestions
    this.preventSubmit       = false;		// prevent submit on hitting enter
    this.suggestDivClassName = "suggestDiv";
    this.suggestionClassName = "suggestion";
    this.inputDelay          = 500;
	
    // set given values
    if (typeof options == "object") {
        if (options.ignoreCase)
            this.ignoreCase = options.ignoreCase;

        if (options.matchAnywhere)
            this.matchAnywhere = options.matchAnywhere;

        if (options.maxSuggestions)
            this.maxSuggestions = options.maxSuggestions;

        if (options.preventSubmit)
            this.preventSubmit = options.preventSubmit;

        if (options.suggestDivClassName)
            this.suggestDivClassName = options.suggestDivClassName;

        if (options.suggestionClassName)
            this.suggestionClassName = options.suggestionClassName;
        
        if (options.inputDelay)
            this.inputDelay = options.inputDelay;
    }
			
};

/**
 * Prepares and extends the input field to work as an suggest widget.
 */
wingS.Suggest.prototype.injectBehavior = function() {
    var keyEventHandler = new wingS.SuggestKeyHandler(this);

    // disable browser native autocomplete
    MochiKit.DOM.setNodeAttribute(this.textInput, "autocomplete", "off");

    // prevent submit by hitting Enter
    if (this.preventSubmit) {
        MochiKit.DOM.setNodeAttribute(this.textInput, "onkeypress", "return wingS.util.preventSubmit()");
        //var preventSubmitButton = INPUT({'type': 'text','id': this.id+'_preventtsubmit', 'style': 'display:none'} );		
        //wingS.util.insertAfter(preventSubmitButton, this.textInput);					
    }

    // create suggestion div
    this.suggestionsDiv = document.createElement("div");
    this.suggestionsDiv.className = this.suggestDivClassName;	
    var divStyle = this.suggestionsDiv.style;
    divStyle.position = 'absolute';
    divStyle.zIndex   = 101;
    divStyle.display  = "none";
    this.textInput.parentNode.appendChild(this.suggestionsDiv);	    
};

/**
 *
 */
wingS.Suggest.prototype.updateSuggestionsDiv = function() {
    this.suggestionsDiv.innerHTML = "";
    var suggestLines = this.createSuggestionSpans();
    for (var i = 0 ; i < suggestLines.length ; i++)
    	this.suggestionsDiv.appendChild(suggestLines[i]);
};

/**
 * Handler used by handleTextInput.
 */
wingS.Suggest.prototype.inputTimeoutHandler = function() {
    this.ignoreInput = false; 
    
    if (this.pendingInput) {
        this.pendingInput = false;
        this.handleTextInput();
    }
};
    
/**
 * Handles new text input of the input field and avoids to send too
 * much requests to the server by using a timeout.
 */
wingS.Suggest.prototype.handleTextInput = function() {    
    if (this.ignoreInput) {        
        this.pendingInput = true;
        return;
    }
    else {
        this.ignoreInput = true;
        setTimeout(this.inputTimeoutHandler.bind(this), this.inputDelay);
    }
    
    var previousRequest    = this.lastRequestString;
    this.lastRequestString = this.textInput.value;
    if (this.lastRequestString == "")
    	this.hideSuggestions();
    else if (this.lastRequestString != previousRequest) {
    	this.sendRequestForSuggestions();
    }    
};

/**
 * Move suggestion selection index up.
 */
wingS.Suggest.prototype.moveSelectionUp = function() {	
    if (this.selectedIndex > 0) {
        this.updateSelection(this.selectedIndex - 1);
    }
};

/**
 * Move suggestion selection index down.
 */
wingS.Suggest.prototype.moveSelectionDown = function() {    
    if (this.selectedIndex < (this.suggestions.length - 1)) {
    	this.updateSelection(this.selectedIndex + 1);
    }
};

/**
 * Update the suggestion selection by setting a CSS class.
 */
wingS.Suggest.prototype.updateSelection = function(n) {
    var span = $(this.id + "_" + this.selectedIndex);
    if (span){    	
        MochiKit.DOM.removeElementClass(span, "selected");
    }
    this.selectedIndex = n;
    var span = $( this.id + "_" + this.selectedIndex );
    if (span){    	
	MochiKit.DOM.addElementClass(span, "selected");		 
    }
};


wingS.Suggest.prototype.sendRequestForSuggestions = function() {    
    if (this.handlingRequest) {
    	this.pendingRequest = true;
        return;
    }
    this.handlingRequest = true;	     
    
    /*
    callParms.push( 'maxSuggestions='          + this.options.maxSuggestions);
    callParms.push( 'query='          + this.lastRequestString);
    callParms.push( 'match_anywhere=' + this.options.matchAnywhere);
    callParms.push( 'ignore_case='    + this.options.ignoreCase);
    */
    
    this.populator(this, this.lastRequestString);
};

wingS.Suggest.prototype.createSuggestions = function(suggestions) {    
    this.suggestions = [];
    
    for (var i = 0; i < suggestions.length; i++) {
        this.suggestions.push(suggestions[i]);
    }         
};

wingS.Suggest.prototype.updateSuggestions = function(ajaxResponse) {
    this.createSuggestions(ajaxResponse);

    if (this.suggestions.length == 0) {
    	this.hideSuggestions();        
    }
    else {
    	this.updateSuggestionsDiv();
        this.showSuggestions();
        this.updateSelection(0);
    }

    this.handlingRequest = false;

    if (this.pendingRequest) {
    	this.pendingRequest    = false;
        this.lastRequestString = this.textInput.value;
        this.sendRequestForSuggestions();
    }
};

/**
 * Puts selected suggestion to the input field and closes the suggestion div.
 */
wingS.Suggest.prototype.setInputFromSelection = function() {
    //var hiddenInput = $( this.id + "_hidden" );
    var suggestion = this.suggestions[this.selectedIndex];

    this.textInput.value = suggestion;    
    this.hideSuggestions();
};

/**
 * Makes the suggestion div visible.
 */
wingS.Suggest.prototype.showSuggestions = function() {
    var divStyle = this.suggestionsDiv.style;
    
    if (divStyle.display == '')
    	return;
    
    this.positionSuggestionsDiv();
    //divStyle.display = '';     
    MochiKit.Visual.appear(this.suggestionsDiv);
};

/**
 * Hides the suggestion div by setting display:none.
 */
wingS.Suggest.prototype.hideSuggestions = function() {		
    //MochiKit.Style.hideElement(this.suggestionsDiv);	
    MochiKit.Visual.fade(this.suggestionsDiv);	
};

/**
 * Sets the position of the suggestion box right below then input field and 
 * sizes its width to match the width of the input field.
 */
wingS.Suggest.prototype.positionSuggestionsDiv = function() {      
    var divStyle = this.suggestionsDiv.style;      	
    var textInputPos = MochiKit.Style.getElementPosition(this.textInput);	

    // if IE and its backward compatibility mode (quirks mode) is used,
    // the CSS-Box-Model-Bug has to be handled 
    if ((this.isIE) && (document.compatMode == "BackCompat")) {				

        var tBorder = MochiKit.Style.computedStyle(this.suggestionsDiv, "borderTopWidth",  "border-top-width" );
        var bBorder = MochiKit.Style.computedStyle(this.suggestionsDiv, "borderBottomWidth", "border-bottom-width" );
        var vertBorder = parseInt(tBorder) + parseInt(bBorder);			
        divStyle.top   = (textInputPos.y + this.textInput.offsetHeight) - vertBorder + "px";		

        var lBorder = MochiKit.Style.computedStyle(this.suggestionsDiv, "borderLeftWidth",  "border-left-width" );
        var rBorder = MochiKit.Style.computedStyle(this.suggestionsDiv, "borderRightWidth", "border-right-width" );
        var horizBorder = parseInt(lBorder) + parseInt(rBorder);			
        divStyle.left   = textInputPos.x - horizBorder + "px"; 

        divStyle.width = this.textInput.offsetWidth + "px";	
    }
    else if (this.isIE) {
        divStyle.top   = (textInputPos.y + this.textInput.offsetHeight) + "px";
        divStyle.left  = textInputPos.x + "px";
        divStyle.width = (this.textInput.offsetWidth - this.padding()) + "px";	
    }
    else {
        divStyle.top   = (textInputPos.y + this.textInput.offsetHeight) + "px";
        divStyle.left  = textInputPos.x + "px";        
        divStyle.width = (this.textInput.offsetWidth) + "px";	
    }
    		
};

wingS.Suggest.prototype.createSuggestionSpans = function() {
    var suggestionSpans = [];
    for (var i = 0; i < this.suggestions.length; i++) {
        suggestionSpans.push(this.createSuggestionSpan(i));
    }
    return suggestionSpans;
};

wingS.Suggest.prototype.createSuggestionSpan = function( n ) {
    var suggestion = this.suggestions[n];

    var suggestionSpan = document.createElement("span");
    suggestionSpan.className = this.suggestionClassName;
    suggestionSpan.style.width   = '100%';
    suggestionSpan.style.display = 'block';
    suggestionSpan.id            = this.id + "_" + n;
    YAHOO.util.Event.addListener(suggestionSpan, "mouseover", this.mouseoverHandler.bind(this));
    YAHOO.util.Event.addListener(suggestionSpan, "click", this.itemClickHandler.bind(this));
	
    suggestionSpan.appendChild(document.createTextNode(suggestion));

    return suggestionSpan;
};

wingS.Suggest.prototype.mouseoverHandler = function(e) {
    var src = e.srcElement ? e.srcElement : e.target;
    var index = parseInt(src.id.substring(src.id.lastIndexOf('_')+1));
    this.updateSelection(index);
};

wingS.Suggest.prototype.itemClickHandler = function(e) {
    this.mouseoverHandler(e);
    this.setInputFromSelection();
    this.hideSuggestions();
    this.textInput.focus();
};

wingS.Suggest.prototype.getElementContent = function(element) {
    return element.firstChild.data;
};

/**
 * Calculate padding of the suggestions box. 
 * 
 * This is done due to the possibility to style the input box using CSS. Otherwise
 * a user could use CSS borders or padding and thus making the visual alignment to
 * the width of the input box wrong.
 */
wingS.Suggest.prototype.padding = function() {	
    var lPad    = MochiKit.Style.computedStyle(this.suggestionsDiv, "paddingLeft",      "padding-left");
    var rPad    = MochiKit.Style.computedStyle(this.suggestionsDiv, "paddingRight",     "padding-right" );
    var lBorder = MochiKit.Style.computedStyle(this.suggestionsDiv, "borderLeftWidth",  "border-left-width" );
    var rBorder = MochiKit.Style.computedStyle(this.suggestionsDiv, "borderRightWidth", "border-right-width" );					
		
    return parseInt(lPad) + parseInt(rPad) + parseInt(lBorder) + parseInt(rBorder);
};












/**
 *
 */
wingS.SuggestKeyHandler = function(textSuggest) {
    this.textSuggest = textSuggest;
    this.input       = this.textSuggest.textInput;
    this.addKeyHandling();
};

wingS.SuggestKeyHandler.prototype.addKeyHandling = function() {
    YAHOO.util.Event.addListener(this.input, "keyup", this.keyupHandler.bind(this));
    YAHOO.util.Event.addListener(this.input, "keydown", this.keydownHandler.bind(this));
    YAHOO.util.Event.addListener(this.input, "blur", this.onblurHandler.bind(this));
};
	
wingS.SuggestKeyHandler.prototype.keydownHandler = function(e) {
    var upArrow   = 38;
    var downArrow = 40;

    if (e.keyCode == upArrow) {
	this.textSuggest.moveSelectionUp();
        setTimeout( this.moveCaretToEnd.bind(this), 1 );										
    }
    else if ( e.keyCode == downArrow ){
    	this.textSuggest.moveSelectionDown();
    }
};

wingS.SuggestKeyHandler.prototype.keyupHandler = function(e) {    
    if ( this.input.length == 0 && !this.isOpera )
        this.textSuggest.hideSuggestions();

    if ( !this.handledSpecialKeys(e) )
        this.textSuggest.handleTextInput();
};

/**
 * 
 */
wingS.SuggestKeyHandler.prototype.handledSpecialKeys = function(e) {
    var enterKey  = 13;
    var upArrow   = 38;
    var downArrow = 40;
    var tabKey    = 9;
    
    // these key have to be ignored    
    if ( e.keyCode == upArrow || e.keyCode == downArrow  || e.keyCode == tabKey) {
    	return true;
    }
    else if ( e.keyCode == enterKey ) {
    	this.textSuggest.setInputFromSelection();
        return true;
   }    

    return false;
};

wingS.SuggestKeyHandler.prototype.moveCaretToEnd = function() {
    var pos = this.input.value.length;

    if (this.input.setSelectionRange) {
        this.input.setSelectionRange(pos, pos);
    }
    else if(this.input.createTextRange){
    	var m = this.input.createTextRange();
        m.moveStart('character',pos);
        m.collapse();
        m.select();
    }
};

wingS.SuggestKeyHandler.prototype.onblurHandler = function(e) {
    if (this.textSuggest.suggestionsDiv.style.display == '' )
        this.textSuggest.setInputFromSelection();
    
    this.textSuggest.hideSuggestions();
};
