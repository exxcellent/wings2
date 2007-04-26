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

wingS.component.spinnerCallback = function(result) {
    var elem = document.getElementById(result[0]);
    if (!elem) return; // dwr bug
    var data = result[1];
    if (data) {
        elem.value = data;
        elem.setAttribute("lastValid", data);
    }
};

