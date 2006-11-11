/***************************************************************************************************
 * WINGS.COMPONENT  --  contains: functions used for special components
 **************************************************************************************************/


/**
 * SFormattedTextField JavaScript Code
 */
wingS.component.ftextFieldCallback = function(result) {
    var elem = document.getElementById(result[0]);
    if (!elem) return; // dwr bug
    var data = result[1];
    if (!data) {
        elem.style.color = '#ff0000';
    } else {
        elem.style.color = '';
        elem.value = data;
        elem.setAttribute("lastValid", data);
    }
}

wingS.component.spinnerCallback = function(result) {
    var elem = document.getElementById(result[0]);
    if (!elem) return; // dwr bug
    var data = result[1];
    if (data) {
        elem.value = data;
        elem.setAttribute("lastValid", data);
    }
}

// ====================================   TO BE MOVED   ============================================

/**
 * XCalendar JavaScript Code
 */
wingS.component.onFieldChange = function(key, name, value) {
    xcalendar.onFieldChange(wingS.component.onFieldChangeCallback, key, name, value);
}

wingS.component.onFieldChangeCallback = function(result) {
    var elem = document.getElementById(result[0]);
    if (!elem) return; // dwr bug
    var data = result[1];
    elem.value = data;
}

wingS.component.onCalUpdate = function(cal) {
    xcalendar.onCalUpdate(wingS.component.onCalUpdateCallback, cal.params.formatter,
                          cal.params.textField, cal.date);
}
wingS.component.onCalUpdateCallback = function(result) {
    var elem = document.getElementById(result[0]);
    if (!elem) return; // dwr bug
    var data = result[1];
    elem.value = data;
    elem.style.color = '';
}

// =================================================================================================