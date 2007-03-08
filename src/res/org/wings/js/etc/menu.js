/* wingS2 Menu, used to emulate the Swing JMenuBar / JPopupMenu.
   wpm stands for wings popup menu and is used to avoid
   typical js namespace clutter.
*/

var wu_dom = document.getElementById?1:0;
var wu_ns4 = (document.layers && !wu_dom)?1:0;
var wu_ns6 = (wu_dom && !document.all)?1:0;
var wu_ie5 = (wu_dom && document.all)?1:0;
var wu_konqueror = wingS.util.checkUserAgent('konqueror')?1:0;
var wu_opera = wingS.util.checkUserAgent('opera')?1:0;
var wu_safari = wingS.util.checkUserAgent('safari')?1:0;

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
var wpm_timeOut = 600;
/* ClickDelta in px. When a click is recieved, the coordinates of the click
   are compared with the coordinates of the event that was recieved last. If
   the clicks are not farther apart than the ClickDelta in x and y, the event
   might be invalid.
   This circumvents mutiple events and thus enables the use of
   cascaded menus.
*/
var wpm_clickDelta = 0;
var wpm_openMenus = new Array();

function wpm_getEvent(event) {
    if (window.event) return window.event;
    else return event;
}
function wpm_point(x, y) {
    this.x = x;
    this.y = y;
}

function wpm_getCoordinates(event) {
    var coord;
    if (wu_ie5) {
        coord = new wpm_point(event.x, event.y);
    }
    else {
        coord = new wpm_point(event.pageX, event.pageY);
    }
    return coord;
}

function wpm_getMenuPosition(event) {
    var el;
    if (wu_ie5) {
        el = event.srcElement;
    }
    else {
        el = event.target;
        if (el.nodeType == 3) // defeat KHTML bug
            el = el.parentNode;
    }
    return new wpm_point(wpm_findPosX(el), wpm_findPosY(el) + el.offsetHeight);
}
function wpm_findPosX(obj)
{
    var curleft = 0;
    var origobj = obj;
    if (obj.offsetParent)
    {
        while (obj.offsetParent)
        {
            curleft += obj.offsetLeft;
            obj = obj.offsetParent;
        }
    }
    else if (obj.x)
        curleft += obj.x;

    /* Workaround for right aligned menues:
       Return the RIGHT X of the Menu as a negative number, not the left X
    if (wpm_getFrameWidth() < curleft + 200) {
        curleft = - (curleft + origobj.offsetWidth);
    }
    */

    return curleft;
}

function wpm_getFrameWidth() {
    if (self.innerHeight) {
        // all except Explorer
        return self.innerWidth;
    } else if (document.documentElement && document.documentElement.clientHeight) {
        // Explorer 6 Strict Mode
        return document.documentElement.clientWidth;
    } else if (document.body) {
        // other Explorers
        return document.body.clientWidth;
    } else
        return -1;
}

function wpm_findPosY(obj)
{
    var curtop = 0;
    if (obj.offsetParent)
    {
        while (obj.offsetParent)
        {
            curtop += obj.offsetTop;
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

function wpm_menu(event, menu) {
    var target = wingS.event.getTarget(event);
    var form = wingS.util.getParentByTagName(target, "FORM");
    if (form != null)
        document.getElementById(menu).setAttribute("form", form.id);

    event = wpm_getEvent(event);
    menuPos = wpm_getMenuPosition(event);
    eventPos = wpm_getCoordinates(event);
    if (!wpm_isValidEvent(eventPos)) return false;
    if (event.button < 2) { // left click hopefully
        wpm_hideActiveMenu();
        wpm_showMenu(menu, menuPos, eventPos);
        wpm_menuCalled = 1;
        setTimeout('wpm_menuCalled = 0;', wpm_timeOut / 2);
    }
}

function wpm_changeMenu(event, menu) {
    event = wpm_getEvent(event);
    if (wpm_menuOpen && (wpm_activeMenu != menu)) {
        menuPos = wpm_getMenuPosition(event);
        eventPos = wpm_getCoordinates(event);
        wpm_hideActiveMenu();
        wpm_showMenu(menu, menuPos, eventPos);
    }
}

function wpm_menuPopup(event, menu) {
    var target = wingS.event.getTarget(event);
    var form = wingS.util.getParentByTagName(target, "FORM");
    if (form != null)
        document.getElementById(menu).setAttribute("form", form.id);

    event = wpm_getEvent(event);
    var coord = wpm_getCoordinates(event);
    if (!wpm_isValidEvent(coord)) return false;
    if (event.type == 'mousedown') {
        if (wu_konqueror || wu_opera || wu_safari) {
            if (event.ctrlKey && (event.button == 2)) {
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
    else if (event.type == 'contextmenu') {
        // ie && firefox
        if (event.ctrlKey || event.shiftKey) {
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

    /* Workaround for right aligned menues: Refer to wpm_findPosX(obj) */
    if (coord.x < 0) {
    	elStyle.left = (-coord.x - document.getElementById(menuId).offsetWidth) + 'px';
    }

    wpm_openMenus[wpm_openMenus.length] = menuId;
    wpm_toggleFormElements(wpm_buildBoundsArray(wpm_openMenus));
    wpm_menuCalled = 1;
    wpm_menuOpen = 1;
    wpm_activeMenu = menuId;
    setTimeout('wpm_menuCalled = 0;', wpm_timeOut);
    if (typeof(eventCoord) == 'undefined') wpm_lastCoord = coord;
    else wpm_lastCoord = eventCoord;
}

function wpm_handleBodyClicks(event) {
    event = wpm_getEvent(event);
    coords = wpm_getCoordinates(event);
    if (wpm_menuCalled == 0 && wpm_isValidEvent(coords)) {
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


function wpm_openMenu(event, id, parentId) {
    var event = wingS.event.getEvent(event);

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
        var pos = wpm_getMenuPosition(event)
        var left = node.getAttribute("origLeft");
        if (!left) {
            left = pos.x;
            node.setAttribute("origLeft", ""+ left);
        }

        node.style.display = 'block';
        var totalWidth = node.parentNode.offsetWidth + node.offsetWidth + parseInt(left);

        if (totalWidth > document.body.clientWidth) {
            node.style.left = -node.offsetWidth + "px";
        } else {
            node.style.left = (node.parentNode.offsetWidth - 2) + "px";
        }

        wpm_openMenus[wpm_openMenus.length] = id;
    }
    wpm_toggleFormElements(wpm_buildBoundsArray(wpm_openMenus));
    wingS.util.preventDefault(event);
    return false;
}

function parentULId(event) {
    var node = wingS.event.getTarget(event);
    while (node) {
        if (node.tagName == "UL") {
            return node.id;
        }
        node = node.parentNode;
    }
    return "";
}

function wpm_closeMenu(event, id, parentId) {
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
    this.object = object;
}

wpm_Bounds.prototype.toString = function() {
    return this.x + ":" + this.y + ":" + this.width + ":" + this.height;
}

wpm_Bounds.prototype.intersect = function(other) {
    return other.x + other.width > (this.x - getScrolledExtendLeft(this.object)) &&
        other.y + other.height > (this.y - getScrolledExtendTop(this.object)) &&
        other.x <= (this.x - getScrolledExtendLeft(this.object)) + this.width &&
        other.y <= (this.y - getScrolledExtendTop(this.object)) + this.height;
}

function wpm_buildBoundsArray(menuarray) {
    var bounds = new Array(menuarray.length);
    for (var i = 0; i < menuarray.length; i++) {
        bounds[i] = new wpm_Bounds(document.getElementById(menuarray[i]));
    }
    return bounds;
}

function getScrolledExtendLeft(o) {
    while(true) {
        o = o.parentNode;
        if (o) {
            if (o.scrollLeft && o.scrollLeft > 0)
                return o.scrollLeft;
        } else {
            return 0;
        }
    }
    return 0;
}

function getScrolledExtendTop(o) {
    while(true) {
        o = o.parentNode;
        if (o) {
            if (o.scrollTop && o.scrollTop > 0)
                return o.scrollTop;
        } else {
            return 0;
        }
    }
    return 0;
}

