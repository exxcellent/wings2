var dom = document.getElementById?1:0;
var ns4 = (document.layers && !dom)?1:0;
var ns6 = (dom && !document.all)?1:0;
var ie5 = (dom && document.all)?1:0;
var konqueror = checkUserAgent('konqueror')?1:0;

function checkUserAgent(string) {
	return navigator.userAgent.toLowerCase().indexOf(string) + 1;
}

function concatArrays(array1, array2) {
    var result = new Array(array1.length+array2.length);
    var index = 0;
    for ( var i=0; i<array1.length; i++ ) {
        result[index++]=array1[i];
    }
    for ( var i=0; i<array2.length; i++ ) {
        result[index++]=array2[i];
    }

    return result;
}

String.prototype.endsWith = function(c) {
    return this.startsWith(c, this.length - c.length);
}

String.prototype.startsWith = function(c) {
    return this.startsWidth(c, 0);
}

String.prototype.startsWith = function(c, startIndex) {
    for ( var i = 0; i<c.length; i++ ) {
        if ( i+startIndex >= this.length ||
             this.charAt(i+startIndex) != c.charAt(i) ) {
            return false;
        }
    }
    return true;
}


var MENUS = new Array();

function Menu(parent, hookElement, element) {
    this.parent = parent;
    this.hookElement = hookElement;
    this.element = element;
    this.direction = (parent == null) ? 'vertical' : 'horizontal';
    this.element.style.position = 'absolute';

    // bei allen child hook elementen mouseOver und mouseOut setzen
    this.setMouseMotion(element);
}


Menu.prototype.hideAll = function() {
    for (var id in MENUS) {
        var menu = MENUS[id];
        setMenuVisible(menu.element, false);
    }
}

Menu.prototype.printAll = function() {
    var string = "items: ";
    for (var id in MENUS) {
        menuItem = MENUS[id];
        string = string + menuItem.hookElement.id + " ";
    }
    //alert(string);
}

Menu.prototype.toggle = function(parentId, hookId, menuId) {
    var menuElement = document.getElementById(menuId);

    if (!isMenuVisible(menuElement)) {
        var parent = MENUS[parentId];

        var menu = MENUS[menuId];
        if (menu == null) {
            var hookElement = document.getElementById(hookId);
            menu = new Menu(parent, hookElement, menuElement);
            MENUS[menuId] = menu;
        }

        menu.show();
    }
    else {
        var menu = MENUS[menuId];

        if ( menu ) {
            menu.hide();
        }
    }

    this.toggleFormElements();
}

Menu.prototype.show = function() {
    this.applyPosition();
    setMenuVisible(this.element, true);
    setStyleClass(this.hookElement, "smenu");

    for (var id in MENUS) {
        var menuItem = MENUS[id];
        if (!this.isParent(menuItem)) {
            setStyleClass(menuItem.hookElement, "menu");
            setMenuVisible(menuItem.element, false);
        } else {
            setStyleClass(menuItem.hookElement, "smenu");
            setMenuVisible(menuItem.element, true);
        }
    }
}

Menu.prototype.hide = function() {
    setStyleClass(this.hookElement, "menu");
    for (var id in MENUS) {
        menuItem = MENUS[id];
        if (this.isChild(menuItem)) {
            setMenuVisible(menuItem.element, false);
        }
    }
}

Menu.prototype.isParent = function(target) {
    if (target == this)
        return true;

    var object = this;
	while (object.parent != null) {
	    object = object.parent;
        if (target == object)
            return true;
	}

	return false;
}

Menu.prototype.isChild = function(target) {
    if (target == this)
        return true;

	while (target.parent != null) {
	    target = target.parent;
        if (target == this)
            return true;
	}

	return false;
}

Menu.prototype.applyPosition = function() {
    var bounds = new Bounds(this.hookElement);
    if (this.direction == 'vertical') {
        this.element.style.top = (bounds.y + bounds.height + 1) + "px";
        this.element.style.left = (bounds.x + 1) + "px";
        if ( ie5 ) {
	        this.element.posX = bounds.x + 1;
	        this.element.posY = bounds.y + bounds.height + 1;
        }
    }
    else if (this.direction == 'horizontal') {
        this.element.style.top = (bounds.y + 1) + "px";
        this.element.style.left = (bounds.x + bounds.width + 1) + "px";
        if ( ie5 ) {
	        this.element.posX = bounds.x + bounds.width + 1;
	        this.element.posY = bounds.y + 1;
        }
    }

    //alert("set position " + this.element.style.left + " " + this.element.style.top + " bounds " +
    //        bounds.x + ":" + bounds.y + ":" + bounds.width + ":" + bounds.height);
}

Menu.prototype.toggleFormElements = function() {
    var menuBounds = new Array();
    var i = 0;
    for (var id in MENUS) {
        menuItem = MENUS[id];
        if ( isMenuVisible(menuItem.element) )
            menuBounds[i++] = new Bounds(menuItem.element);
    }
    toggleFormElements(menuBounds);
}




Menu.prototype.setMouseMotion = function(element) {
    if ( element.id && element.id.endsWith("hook") ) {
        this.setMouseMotionStyles(element);
    } else {
        var kids = element.childNodes;
        for (var i = 0; i < kids.length; i++) {
            this.setMouseMotion(kids[i]);
        }
    }
}

Menu.prototype.setMouseMotionStyles = function(element) {
    if ( element.className!="disabledmenu" &&
         element.className!="disabledmenuitem" ) {
        element.onmouseover = function(event) { Menu.prototype.setMouseOverStyle(element); };
        element.onmouseout = function(event) { Menu.prototype.setMouseOutStyle(element); };
    }
}

Menu.prototype.setMouseOutStyle = function(element) {
    if ( element.className!="smenu" ) {
        if ( element.className == "amenuitem" ) {
            setStyleClass(element, 'menuitem');
        } else {
            setStyleClass(element, 'menu');
        }
    }
}


Menu.prototype.setMouseOverStyle = function(element) {
    if ( element.className!="smenu" ) {
        if ( element.className == "menuitem" ) {
            setStyleClass(element, 'amenuitem');
        } else {
            setStyleClass(element, 'amenu');
        }
    }
}



function printChildNodes(element) {

    var result = element.id;

    var kids = element.childNodes;
    for (var i = 0; i < kids.length; i++) {
        result += ":" + printChildNodes(kids[i]);


    }

    return result;
}

function toggleFormElements(elementBounds) {

    //var string = "";

    var selects = document.getElementsByTagName('select');

    if ( konqueror ) {
        // konqueror needs to hide all form elements
        var inputs = document.getElementsByTagName('input');
        selects = concatArrays(selects, inputs);
        var textAreas = document.getElementsByTagName('textarea');
        selects = concatArrays(selects, textAreas);
    }

    for ( var i=0; i<selects.length; i++ ) {
	    var select = selects[i];
        var selectBounds = new Bounds(select);

	    var selectBoundsString = selectBounds.x + ":" +
                                 selectBounds.y + ":" + selectBounds.width + ":" +
                                 selectBounds.height;

        var intersects = false;
        for (var b = 0; b < elementBounds.length; ++b) {
            var bounds = elementBounds[b];
            //var elemBoundsString = bounds.x + ":" + bounds.y + ":" + bounds.width + ":" + bounds.height;

            if (selectBounds.intersect(bounds)) {
                intersects = true;
                //string = string + elemBoundsString + " intersects " + selectBoundsString + " \n";
  		        break;
            }
            else {
                //string = string + elemBoundsString + " not intersects " + selectBoundsString + " \n";
           }
        }
        setVisible(select, !intersects);
    }
    //alert(string);
}

function setMenuVisible(element, visible) {
    if (visible) {
        if ( konqueror ) {
            element.style.display = '';
         } else {
            element.style.display = 'inline';
         }
    } else {
        element.style.display= 'none';
    }
}

function isMenuVisible(element) {
    return element.style.display!= 'none';
}

function setVisible(element, visible) {
    if (visible) {
        element.style.visibility = 'visible';
    } else {
        element.style.visibility= 'hidden';
    }
}

function isVisible(element) {
    return element.style.visibility!= 'hidden';
}

function getBoundsText(obj) {
  if ( obj == null ) {
    return "";
  } else {
    return obj.id + obj.offsetTop + ":" + obj.offsetLeft + ":" + obj.offsetWidth + ":" + obj.offsetHeight;
  }
}

function Bounds(object) {

    this.x = 0;
    this.y = 0;
    this.width = object.offsetWidth;/* + object.style.marginLeft + object.style.borderLeftWidth +
                object.style.paddingLeft + object.style.paddingRight +
                object.style.borderRightWidth + object.style.marginRight;*/
    this.height = object.offsetHeight;

    if (object.offsetParent) {

        // konqueror fix, a tr gives here 0 width and height, so use width and height of parent table
        if ( konqueror ) {
            var parent = object.offsetParent;
            while ( this.width == 0 && parent ) {
                this.width = parent.offsetWidth;
                parent = parent.offsetParent;
            }
            parent = object.offsetParent;
            while ( this.height == 0 && parent ) {
                this.height = parent.offsetHeight;
                parent = parent.offsetParent;
            }
        }
        while (object ) {
                /*alert("offset parent " + object.tagName + "\n" +
                      " top " + object.offsetTop + "\n" +
                      " left " + object.offsetLeft + "\n" +
                      " width " + object.offsetWidth + "\n" +
                      " height " + object.offsetHeight + "\n" +
                      "");*/
            this.y += object.offsetTop;
            this.x += object.offsetLeft;

            if  ( object.style.position == "absolute" ) {
                break;
            }
            object = object.offsetParent;
        }
    } else if (object.x && object.y) {
        this.x = object.x;
        this.y = object.y;
    } else {
       // alert("offset parent not defined " + object.tagName + " " + object.id);
    }


    //alert("bounds " + object.id + " " + this.x + ":" + this.y + ":" + this.width + ":" + this.height);


    //this.x = object.posX ? object.posX : 0;
    //this.y = object.posY ? object.posY : 0;
    //this.width = object.offsetWidth;
    //this.height = object.offsetHeight;

    // only calc if not a fixed position
    //if ( !object.posX) {
    //    while (object != null) {
    //        this.x += object.offsetLeft;
    //        this.y += object.offsetTop;
    //        object = object.offsetParent;
    //    }
    //}
}

Bounds.prototype.intersect = function(other) {
    x1 = Math.max(this.x, other.x);
    y1 = Math.max(this.y, other.y);
    x2 = Math.min(this.x + this.width, other.x + other.width);
    y2 = Math.min(this.y + this.height, other.y + other.height);
    if (x1 >= x2 || y1 >= y2)
        return false;
    else
        return true;
}

function setStyleClass(item, newClass) {
    setVisible(item, false);
    item.className = newClass;
    setVisible(item, true);
}
