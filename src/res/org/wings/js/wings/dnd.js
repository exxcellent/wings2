if (!wingS.dnd) {
    wingS.dnd = new Object();
} else if (typeof wingS.dnd != "object") {
    throw new Error("wingS.dnd already exists and is not an object");
}

/**
 * @class a YAHOO.util.DDFramed implementation. During the drag over event, the
 * dragged element is inserted before the dragged-over element.
 *
 * @extends YAHOO.util.DDProxy
 * @constructor
 * @param {String} id the id of the linked element
 * @param {String} sGroup the group of related DragDrop objects
 */
wingS.dnd.DD = function(id, sGroup, config) {
    this.wingSInit(id, sGroup, config);
};

YAHOO.extend(wingS.dnd.DD, YAHOO.util.DDProxy);

wingS.dnd.DD.prototype.wingSInit = function(id, sGroup, config) {
    if (!id) { return; }

    this.init(id, sGroup, config);
    this.initFrame();
    this.setInitPosition();
};

wingS.dnd.DD.TYPE = "wingS.DD";

wingS.dnd.DD.prototype.onDrag = function(e) { };
wingS.dnd.DD.prototype.onDragOver = function(e) { };

wingS.dnd.DD.prototype.onDragDrop = function(event, id) {
    // get the drag and drop object that was targeted
    var target;

    if ("string" == typeof id) {
        target = YAHOO.util.DDM.getDDById(id);
    } else {
        target = YAHOO.util.DDM.getBestMatch(id);
    }

    this.resetConstraints();
    target.resetConstraints();

    wingS.request.sendEvent(event, true, true, dragAndDropManager, this.id + ':' + id);
};

wingS.dnd.DD.prototype.endDrag = function(e) {
};
