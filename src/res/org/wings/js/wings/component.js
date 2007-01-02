/***************************************************************************************************
 * WINGS.COMPONENT  --  contains: functions used for special components
 **************************************************************************************************/

/**
 * Create according namespace
 */
if (!wingS.component) {
    wingS.component = new Object();
} else if (typeof wingS.component != "object") {
    throw new Error("wingS.component already exists and is not an object");
}

/**
 * SFormattedTextField JavaScript Code
 */
wingS.component.ftextFieldCallback = function(result) {
    var elem = document.getElementById(result[0]);
    var data = result[1];
    var invalid = result[2];
    if (!elem) return; // dwr bug
    if (invalid) {
        elem.style.color = '#ff0000';
    } else {
        elem.style.color = '';
    }
    elem.value = data;
};

wingS.component.spinnerCallback = function(result) {
    var elem = document.getElementById(result[0]);
    if (!elem) return; // dwr bug
    var data = result[1];
    if (data) {
        elem.value = data;
        elem.setAttribute("lastValid", data);
    }
};

