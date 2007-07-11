/***************************************************************************************************
 * WINGS.LAYOUT  --  contains: functions used to layout components
 **************************************************************************************************/

// Create module namespace
wingS.namespace("layout");


wingS.layout.fill = function(tableId) {
    var table = document.getElementById(tableId);
    if (table == null || table.style.height == table.getAttribute("layoutHeight")) return;

    var consumedHeight = 0;
    var rows = table.rows;
    for (var i = 0; i < rows.length; i++) {
        var row = rows[i];
        var yweight = row.getAttribute("yweight");
        if (!yweight) consumedHeight += row.offsetHeight;
    }

    table.style.height = table.getAttribute("layoutHeight");
    var diff = table.clientHeight - consumedHeight;

    if (diff > 0) {
        for (var i = 0; i < rows.length; i++) {
            var row = rows[i];
            var yweight = row.getAttribute("yweight");
            if (yweight) {
                var oversize = row.getAttribute("oversize");
                if (oversize == null) oversize = 0;
                row.height = Math.max(Math.floor((diff * yweight) / 100) - oversize, oversize);
            }
        }
    }
};

wingS.layout.fix = function(tableId) {
    var table = document.getElementById(tableId);
    if (table == null) return;

    var consumedHeight = 0;
    var rows = table.rows;
    for (var i = 0; i < rows.length; i++) {
        var row = rows[i];
        consumedHeight += row.offsetHeight;
    }

    table.style.height = consumedHeight + "px";
};

wingS.layout.scrollPane = function(tableId) {
    var table = document.getElementById(tableId);
    if (table == null) return;

    var div = table.getElementsByTagName("DIV")[0];

    if (document.defaultView) {
        div.style.height = document.defaultView.getComputedStyle(table, null).getPropertyValue("height");
        div.style.display = "block";
    } else {
        var td = wingS.util.getParentByTagName(div, "TD");
        div.style.height = td.clientHeight + "px";
        div.style.width = td.clientWidth + "px";
        div.style.position = "absolute";
        div.style.display = "block";
        //div.style.overflow = "auto";
    }
};

