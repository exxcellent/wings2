/* wingS2 Menu, used to emulate the Swing JMenuBar / JPopupMenu.
   wpm stands for wings popup menu and is used to avoid
   typical js namespace clutter.
*/

var wpm_menuOpen = 0;
// is a menu open
var wpm_activeMenu;
// currently active menu
var wpm_menuCalled = 0;
// was the function called lately
var wpm_lastCoord = new wpm_point(0, 0);
// position of currently active menu
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
var wpm_openMenus = new Array();

function wpm_getEvent(e) {
    if (window.event) return window.event;
    else return e;
}
function wpm_point(x, y) {
    this.x = x;
    this.y = y;
}

function wpm_getCoordinates(e) {
    var coord;
    if (wu_ie5) {
        coord = new wpm_point(e.x, e.y);
    }
    else {
        coord = new wpm_point(e.pageX, e.pageY);
    }
    return coord;
}

function wpm_getMenuPosition(e) {
    var e;
    if (wu_ie5) {
        el = e.srcElement;
    }
    else {
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
    }
    else if (obj.x)
        curleft += obj.x;
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
    else if (obj.y)
        curtop += obj.y;
    return curtop;
}

function wpm_isValidEvent(coord) {
    if ((Math.abs(wpm_lastCoord.x - coord.x) <= wpm_clickDelta) && (Math.abs(wpm_lastCoord.y - coord.y) <= wpm_clickDelta) && (wpm_menuCalled == 1)) {
        // see timeout and clickdelta comments
        return false;
    }
    else return true;
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

function wpm_changeMenu(e, menu) {
    e = wpm_getEvent(e);
    if (wpm_menuOpen && (wpm_activeMenu != menu)) {
        menuPos = wpm_getMenuPosition(e);
        eventPos = wpm_getCoordinates(e);
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
            }
            else {
                wpm_hideActiveMenu();
            }
        }
        else {
            // handle other actions as contextmenu for ie && firefox and mousedown + ctrl for other browsers
            wpm_hideActiveMenu();
        }
    }
    else if (e.type == 'contextmenu') {
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
        // close all open
        for (var i = wpm_openMenus.length - 1; i >= 0; i--) {
            document.getElementById(wpm_openMenus[i]).style.display = 'none';
        }
        document.getElementById(wpm_activeMenu).style.display = 'none';
        wpm_openMenus = new Array();
        wpm_toggleFormElements();
        wpm_menuOpen = 0;
    }
}

function wpm_showMenu(menuId, coord, eventCoord) {
    elStyle = document.getElementById(menuId).style;
    elStyle.top = coord.y + 'px';
    elStyle.left = coord.x + 'px';
    elStyle.display = 'block';
    elStyle.zIndex = 100000;
    wpm_openMenus[wpm_openMenus.length] = menuId;
    wpm_toggleFormElements(wpm_buildBoundsArray(wpm_openMenus));
    wpm_menuCalled = 1;
    wpm_menuOpen = 1;
    wpm_activeMenu = menuId;
    setTimeout('wpm_menuCalled = 0;', wpm_timeOut);
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

function wpm_setVisible(element, visible) {
    if (visible) {
        element.style.visibility = 'visible';
    }
    else {
        element.style.visibility = 'hidden';
    }
}


function wpm_openMenu(id, parentId) {
    var event = window.event;

    if (parentULId(event) != parentId) {
        // don't bubble
        return;
    }

    // search in the openMenus, close all decent menus of the parent
    // open the id
    for (var i = wpm_openMenus.length - 1; i >= 0; i--) {
        if (wpm_openMenus[i] == parentId) {
            break;
        }
        document.getElementById(wpm_openMenus[i]).style.display = 'none';
        wpm_openMenus = wpm_openMenus.slice(0, wpm_openMenus.length - 1);
    }
    var node = document.getElementById(id);
    if (node) {
        node.style.display = 'block';
        wpm_openMenus[wpm_openMenus.length] = id;
    }
    wpm_toggleFormElements(wpm_buildBoundsArray(wpm_openMenus));
}

function parentULId(event) {
    var node = event.srcElement;
    while (node) {
        if (node.tagName == "UL") {
            return node.id;
        }
        node = node.parentNode;
    }
    return "";
}

function wpm_closeMenu(id, parentId) {
    var event = window.event;
    if (parentULId(event) != parentId) {
        return;
    }
    document.getElementById(id).style.display = 'none';
    wpm_openMenus = wpm_openMenus.slice(0, wpm_openMenus.length - 1);
    wpm_toggleFormElements(wpm_buildBoundsArray(wpm_openMenus));
}

function wpm_toggleFormElements(elementBounds) {
    if (wu_ie5) {
        var selects = document.getElementsByTagName('select');
        if (!elementBounds) {
            for (var i = 0; i < selects.length; i++) {
                wpm_setVisible(selects[i], true);
            }
        }
        else {
            for (var i = 0; i < selects.length; i++) {
                var select = selects[i];
                var selectBounds = new wpm_Bounds(select);
                var intersects = false;
                for (var b = 0; b < elementBounds.length; b++) {
                    var bounds = elementBounds[b];
                    if (selectBounds.intersect(bounds)) {
                        intersects = true;
                        break;
                    }
                }
                if (intersects) {
                    wpm_setVisible(select, false);
                }
            }
            // rof
        }
        // fi !elementBounds
    }  // fi wu_ie5
}

function wpm_Bounds(object) {
    this.x = wpm_findPosX(object);
    this.y = wpm_findPosY(object);
    this.width = object.offsetWidth;
    this.height = object.offsetHeight;
}

wpm_Bounds.prototype.toString = function() {
    return this.x + ":" + this.y + ":" + this.width + ":" + this.height;
}

wpm_Bounds.prototype.intersect = function(other) {
    var y1 = Math.max(this.y, other.y);
    var x1 = Math.max(this.x, other.x);
    var x2 = Math.min(this.x + this.width, other.x + other.width);
    var y2 = Math.min(this.y + this.height, other.y + other.height);
    if (x1 >= x2 || y1 >= y2)
        return false;
    else
        return true;
}

function wpm_buildBoundsArray(menuarray) {
    var bounds = new Array(menuarray.length);
    for (var i = 0; i < menuarray.length; i++) {
        bounds[i] = new wpm_Bounds(document.getElementById(menuarray[i]));
    }
    return bounds;
}
