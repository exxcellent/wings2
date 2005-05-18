/* wingS2 Menu, used to emulate the Swing JMenuBar / JPopupMenu.
   wpm stands for wings popup menu and is used to avoid
   typical js namespace clutter.
*/

var wpm_menuOpen = 0; // is a menu open
var wpm_activeMenu; // currently active menu
var wpm_menuCalled = 0; // was the function called lately
var wpm_lastCoord = new wpm_point(0,0); // position of currently active menu
/* Timeout in ms. When a menu is opened, this timeout is triggered.
   While it is active, no other menu can show which is triggered by
   an event with the same coordinates.
   This circumvents mutiple events and thus enables the use of
   cascaded menus.
*/ 
var wpm_timeOut = 1000;
/* ClickDelta in px. When a click is recieved, the coordinates of the click
   are compared with the coordinates of the event that was recieved last. If
   the clicks are not farther apart than the ClickDelta in x and y, the event
   might be invalid.
   This circumvents mutiple events and thus enables the use of
   cascaded menus.
*/ 
var wpm_clickDelta = 0;

function wpm_getEvent(e) {
	if (window.event) return window.event;
	else return e;
}
function wpm_point(x,y) {
	this.x = x;
	this.y = y;
}

function wpm_getCoordinates(e) {
	var coord; 
	if (wu_ie5) {
		coord = new wpm_point(e.x, e.y);
	} else {
		coord = new wpm_point(e.pageX, e.pageY);
	}
	return coord;
}

function wpm_getMenuPosition(e) {
	var e;
	if (wu_ie5) {
		el = e.srcElement;
	} else {
		el = e.target;
        if (el.nodeType == 3) // defeat KHTML bug
            el = el.parentNode;
	}
	return new wpm_point(wpm_findPosX(el), wpm_findPosY(el) + el.offsetHeight);
}

function wpm_findPosX(obj)
{
    var curleft = 0;
    if (obj.offsetParent)
    {
        while (obj.offsetParent)
        {
                curleft += obj.offsetLeft
                obj = obj.offsetParent;
        }
    } else if (obj.x) {
        curleft += obj.x;
    } else if (obj.offsetLeft) {
        curleft += obj.offsetLeft;
    }
    if (wu_opera) curleft += 10; // why?
    return curleft;
}

function wpm_findPosY(obj)
{
    var curtop = 0;
    if (obj.offsetParent)
    {
        while (obj.offsetParent)
        {
            curtop += obj.offsetTop
            obj = obj.offsetParent;
        }
    }
    else if (obj.y) {
        curtop += obj.y;
    } else if (obj.offsetTop) {
        curtop += obj.offsetTop;
    }
    if (wu_opera) curtop += 10; // why?
    return curtop;
}

function wpm_isValidEvent(coord) {
	if ( (Math.abs(wpm_lastCoord.x - coord.x) <= wpm_clickDelta) && (Math.abs(wpm_lastCoord.y - coord.y) <= wpm_clickDelta) && (wpm_menuCalled == 1) ) {
		// see timeout and clickdelta comments
		return false;
	} else return true;
}

function wpm_menu(e, menu) {
	e = wpm_getEvent(e);
	menuPos = wpm_getMenuPosition(e);
	eventPos = wpm_getCoordinates(e);
	if (!wpm_isValidEvent(eventPos)) return false;
	if (e.button < 2) { // left click hopefully
		wpm_hideActiveMenu();
		wpm_showMenu(menu, menuPos, eventPos);
	} 
}

function wpm_menuPopup(e, menu) {
	e = wpm_getEvent(e);
	var coord = wpm_getCoordinates(e);
	if (!wpm_isValidEvent(coord)) return false;
	if (e.type == 'mousedown') {
		if (wu_konqueror || wu_opera || wu_safari) {
			if (e.ctrlKey && (e.button == 2)) {
				// ctrl right click
				wpm_hideActiveMenu();
				wpm_showMenu(menu, coord);
				return false;
			} else {
				wpm_hideActiveMenu();
			}
		} else {
			// handle other actions as contextmenu for ie && firefox and mousedown + ctrl for other browsers
			wpm_hideActiveMenu();
		}
	} else if (e.type == 'contextmenu') {
		// ie && firefox
		if (e.ctrlKey || e.shiftKey) {
			// only handle straight right clicks, let others pass
			return true;
		}
		wpm_hideActiveMenu();
		wpm_showMenu(menu, coord);
		return false;
	}
}

function wpm_hideActiveMenu() {
	if (wpm_menuOpen == 1) {
		document.getElementById(wpm_activeMenu).style.display = 'none';
		toggleFormElements(true);
		wpm_menuOpen = 0;
	}
}

function wpm_showMenu(menuId, coord, eventCoord) {
	elStyle = document.getElementById(menuId).style;
	elStyle.top = coord.y + 'px';
	elStyle.left = coord.x + 'px';
	toggleFormElements(false);
	elStyle.display = 'block';
	wpm_menuCalled = 1;
	wpm_menuOpen = 1;
	wpm_activeMenu = menuId;
	setTimeout('wpm_menuCalled = 0;',wpm_timeOut);
	if (typeof(eventCoord) == 'undefined') wpm_lastCoord = coord;
	else wpm_lastCoord = eventCoord;
}

function wpm_handleBodyClicks(e) {
	e = wpm_getEvent(e);
	coords = wpm_getCoordinates(e);
	if (wpm_isValidEvent(coords)) {
		wpm_hideActiveMenu();
	}
}

function toggleFormElements(visible) {
	if ( wu_ie5 ) {
		// only ie needs to hide select elements
	    var selects = document.getElementsByTagName('select');
		for ( var i=0; i<selects.length; i++ ) {
			setVisible(selects[i], visible);
		}
	}
}

function setVisible(element, visible) {
    if (visible) {
        element.style.visibility = 'visible';
    } else {
        element.style.visibility= 'hidden';
    }
}
