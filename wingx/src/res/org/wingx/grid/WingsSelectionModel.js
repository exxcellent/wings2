/**
 @class YAHOO.ext.grid.WingsDefaultSelectionModel
 * @extends YAHOO.ext.grid.DefaultSelectionModel
 * The default WingsSelectionModel used by {@link YAHOO.ext.grid.Grid}. 
 It supports multiple selections and keyboard selection/navigation. <br><br>
 @constructor
 * Creates a new WingsDefaultSelectionModel
*/
YAHOO.ext.grid.WingsDefaultSelectionModel = function(){
    YAHOO.ext.grid.WingsDefaultSelectionModel.superclass.constructor.call(this);
};

YAHOO.extendX(YAHOO.ext.grid.WingsDefaultSelectionModel, YAHOO.ext.grid.DefaultSelectionModel);

YAHOO.ext.grid.WingsDefaultSelectionModel.prototype.selectAll = function(){
    if(this.isLocked()) return;
    this.selectedRows = [];
    this.selectedRowIds = [];
    for(var j = 0, len = this.grid.rows.length; j < len; j++){
        this.setRowState(this.grid.rows[j], true, true);
    }
    this.fireEvent('selectionchange', this );
};

YAHOO.ext.grid.WingsDefaultSelectionModel.prototype.rowClick = function(grid, rowIndex, e){
    if(this.isLocked()) return;
    var row = grid.getRow(rowIndex);
    if(this.isSelectable(row)){
        if(e.shiftKey && this.lastSelectedRow){
            var lastIndex = this.lastSelectedRow.rowIndex;
            this.selectRange(this.lastSelectedRow, row, e.ctrlKey);
            this.lastSelectedRow = this.grid.el.dom.rows[lastIndex];
        }else{
            this.focusRow(row);
            var rowState = e.ctrlKey ? !this.isSelected(row) : true;
            this.setRowState(row, rowState, e.hasModifier());
        }
    }
    this.fireEvent('selectionchange', this );
};

YAHOO.ext.grid.WingsDefaultSelectionModel.prototype.setRowState = function(row, selected, keepExisting){
    if(this.isLocked()) return;
    if(this.isSelectable(row)){
        if(selected){
            if(!keepExisting){
                this.clearSelections();
            }
            this.setRowClass(row, 'selected');
            row.selected = true;
            this.selectedRows.push(row);
            this.selectedRowIds.push(this.grid.dataModel.getRowId(row.rowIndex));
            this.lastSelectedRow = row;
        }else{
            this.setRowClass(row, '');
            row.selected = false;
            this._removeSelected(row);
        }
        this.fireEvent('rowselect', this, row, selected);
    }
};

/**
 @class YAHOO.ext.grid.WingsSingleSelectionModel
 @extends YAHOO.ext.grid.WingsDefaultSelectionModel
 Allows only one row to be selected at a time.
 @constructor
 * Create new WingsSingleSelectionModel
 */
YAHOO.ext.grid.WingsSingleSelectionModel = function(){
    YAHOO.ext.grid.WingsSingleSelectionModel.superclass.constructor.call(this);
};

YAHOO.extendX(YAHOO.ext.grid.WingsSingleSelectionModel, YAHOO.ext.grid.WingsDefaultSelectionModel);

YAHOO.ext.grid.WingsSingleSelectionModel.prototype.setRowState = function(row, selected){
    YAHOO.ext.grid.WingsSingleSelectionModel.superclass.setRowState.call(this, row, selected, false);
};

/**
 @class YAHOO.ext.grid.WingsDefaultEditorSelectionModel
 @extends YAHOO.ext.grid.EditorSelectionModel
 It supports multiple selections and keyboard selection/navigation. <br><br>
 @constructor
 * Create new WingsEditorDefaultSelectionModel
 */
YAHOO.ext.grid.WingsDefaultEditorSelectionModel = function(){
  YAHOO.ext.grid.WingsDefaultEditorSelectionModel.superclass.constructor.call(this);
};

YAHOO.extendX(YAHOO.ext.grid.WingsDefaultEditorSelectionModel, YAHOO.ext.grid.EditorSelectionModel);

YAHOO.ext.grid.WingsDefaultEditorSelectionModel.prototype.initEvents = function(){
 YAHOO.ext.grid.EditorSelectionModel.prototype.initEvents.call(this);
 this.grid.removeListener("keydown", this.keyDown, this, true);
 YAHOO.ext.grid.WingsDefaultSelectionModel.prototype.initEvents.call(this);
};

YAHOO.ext.grid.WingsDefaultEditorSelectionModel.prototype.keyDown = function(e){
 var m = YAHOO.ext.grid.WingsDefaultEditorSelectionModel;
 var superclass = null;
 if(this.gridHasActiveEditor())
 {
    YAHOO.ext.grid.EditorSelectionModel.prototype.keyDown.call(this, e);
 }
 YAHOO.ext.grid.WingsDefaultSelectionModel.prototype.keyDown.call(this, e);
};

YAHOO.ext.grid.WingsDefaultEditorSelectionModel.prototype.gridHasActiveEditor = function(){                                                                                 
 // hack
 var colConfigs = this.grid.getColumnModel().config;
 var editing = false;
 for (var i = 0; i < colConfigs.length && !editing; ++i)
 {
    var c = colConfigs[i];
    editing = c.editor && c.editor.editing;
 }
 return editing;
}


YAHOO.ext.grid.WingsDefaultEditorSelectionModel.prototype.getSelectedRows = YAHOO.ext.grid.WingsDefaultSelectionModel.prototype.getSelectedRows;
YAHOO.ext.grid.WingsDefaultEditorSelectionModel.prototype.selectAll = YAHOO.ext.grid.WingsDefaultSelectionModel.prototype.selectAll;
YAHOO.ext.grid.WingsDefaultEditorSelectionModel.prototype.rowClick = YAHOO.ext.grid.WingsDefaultSelectionModel.prototype.rowClick;

YAHOO.ext.grid.WingsDefaultEditorSelectionModel.prototype.focusRow = YAHOO.ext.grid.WingsDefaultSelectionModel.prototype.focusRow;
YAHOO.ext.grid.WingsDefaultEditorSelectionModel.prototype.setRowState = YAHOO.ext.grid.WingsDefaultSelectionModel.prototype.setRowState;

/**
 @class YAHOO.ext.grid.WingsSingleEditorSelectionModel
 @extends YAHOO.ext.grid.WingsDefaultEditorSelectionModel
 Allows only one row to be selected at a time.
 @constructor
 * Create new WingsSingleEditorSelectionModel
 */
YAHOO.ext.grid.WingsSingleEditorSelectionModel = function(){
    YAHOO.ext.grid.WingsSingleEditorSelectionModel.superclass.constructor.call(this);
};

YAHOO.extendX(YAHOO.ext.grid.WingsSingleEditorSelectionModel, YAHOO.ext.grid.WingsDefaultEditorSelectionModel);

YAHOO.ext.grid.WingsSingleEditorSelectionModel.prototype.initEvents = function(){
 YAHOO.ext.grid.EditorSelectionModel.prototype.initEvents.call(this);
 this.grid.removeListener("keydown", this.keyDown, this, true);
 YAHOO.ext.grid.WingsSingleSelectionModel.prototype.initEvents.call(this);
};

YAHOO.ext.grid.WingsSingleEditorSelectionModel.prototype.keyDown = function(e){
 var m = YAHOO.ext.grid.WingsDefaultEditorSelectionModel;
 var superclass = null;
 if(this.gridHasActiveEditor())
 {
    YAHOO.ext.grid.EditorSelectionModel.prototype.keyDown.call(this, e);
 }
 YAHOO.ext.grid.WingsSingleSelectionModel.prototype.keyDown.call(this, e);
};

YAHOO.ext.grid.WingsSingleEditorSelectionModel.prototype.getSelectedRows = YAHOO.ext.grid.WingsSingleSelectionModel.prototype.getSelectedRows;
YAHOO.ext.grid.WingsSingleEditorSelectionModel.prototype.selectAll = YAHOO.ext.grid.WingsSingleSelectionModel.prototype.selectAll;
YAHOO.ext.grid.WingsSingleEditorSelectionModel.prototype.rowClick = YAHOO.ext.grid.WingsSingleSelectionModel.prototype.rowClick;

YAHOO.ext.grid.WingsSingleEditorSelectionModel.prototype.focusRow = YAHOO.ext.grid.WingsSingleSelectionModel.prototype.focusRow;
YAHOO.ext.grid.WingsSingleEditorSelectionModel.prototype.setRowState = YAHOO.ext.grid.WingsSingleSelectionModel.prototype.setRowState;