/*
 *   XCalendar JavaScript Code
 */

/**
 * Uses  Mihai Bazon calendar and Yahoo event functionality to implement a date picker.
 *
 * @param  {string}  formatterKey    A lookup key for the validaton method.
 * @param  {string}  inputfieldId    The HTML element id of the hidden input field
 * @param  {string}  textfieldId     The HTML element id of the SFormattedTextField
 * @param  {string}  editButtonId    The HTML element id of the button triggering the calendar
 * @param  {string}  resetButtonId   The HTML element id of the button triggering the calendar reset
 */
function XCalendar(formatterKey, inputfieldId, textfieldId, editButtonId, resetButtonId, onUpdateCom) {
    this.formatterKey = formatterKey;
    this.inputfield = document.getElementById(inputfieldId);
    this.textfield = document.getElementById(textfieldId);
    this.editButton = document.getElementById(editButtonId);
    this.onUpdateCommit = onUpdateCom;
    if (resetButtonId)
        this.resetButton = document.getElementById(resetButtonId);
}

XCalendar.prototype.onFieldChange = function onFieldChange() {
    this.inputfield.value = this.textfield.value;
    xcalendar.onFieldChange(this.onFieldChangeCallback, this.formatterKey, this.textfield.id, this.textfield.value, this.inputfield.id);
}

XCalendar.prototype.onFieldChangeCallback = function onFieldChangeCallback(result) {
    var visibleTextfield = document.getElementById(result[0]);
    if (!visibleTextfield)
        return; // dwr bug
    visibleTextfield.value = result[1];

    var hiddenField = document.getElementById(result[2]);
    if (!hiddenField)
        return; // dwr bug
    hiddenField.value = result[3];
}

XCalendar.prototype.onCalUpdate = function onCalUpdate(cal) {
    /* this.onCalUpdateCallback doesn't work as the this scope is lost. */
    xcalendar.onCalUpdate(XCalendar.prototype.onCalUpdateCallback, cal.params.formatter, cal.params.textField, cal.date, cal.params.onUpdateCommit );
}

XCalendar.prototype.onCalUpdateCallback = function onCalUpdateCallback(result) {
    var elem = document.getElementById(result[0]);
    if (!elem)
        return; // dwr bug
    var data = result[1];
    elem.value = data;
    elem.style.color = '';
    if ( result[2] == 'true' ) {
        elem.form.submit();
    }
}

XCalendar.prototype.clearCalendar = function clearCalendar(ev) {
    this.textfield.value = '';
    this.inputfield.value = '';
    YAHOO.util.Event.preventDefault(ev);
    return false;
}

XCalendar.prototype.initXCal = function initXCal() {
    this.calendar = Calendar.setup({
          inputField     : this.inputfield.id
        , textField      : this.textfield.id
        , ifFormat       : "%Y.%m.%d"
        , button         : this.editButton.id
        , showOthers     : true
        , electric       : false
        , onUpdate       : this.onCalUpdate
        , formatter      : this.formatterKey
        , onUpdateCommit : this.onUpdateCommit
     });

    YAHOO.util.Event.addListener(this.textfield.id, "change", this.onFieldChange, this, true);

    if (this.resetButton) {
        YAHOO.util.Event.addListener(this.resetButton.id, "click", this.clearCalendar, this, true);
    }
}
