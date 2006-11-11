/***************************************************************************************************
 * WINGS.LAYOUT  --  contains: functions used to layout components
 **************************************************************************************************/


wingS.layout.layoutScrollPaneFF = function(outerId) {
    var outer = document.getElementById(outerId);
    var div = outer.getElementsByTagName("DIV")[0];
    div.style.height =
        document.defaultView.getComputedStyle(outer, null).getPropertyValue("height");
    div.style.display = "block";
};

wingS.layout.layoutScrollPaneIE = function(outerId) {
    var outer = document.getElementById(outerId);
    var div = outer.getElementsByTagName("DIV")[0];
    var td = wingS.util.getParentByTagName(div, "TD");
    div.style.height = td.clientHeight + "px";
    div.style.width = td.clientWidth + "px";
    div.style.position = "absolute";
    div.style.display = "block";
    div.style.overflow = "auto";
};

wingS.layout.layoutAvailableSpaceIE = function(tableId) {
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

    for (var i = 0; i < rows.length; i++) {
      var row = rows[i];
      var yweight = row.getAttribute("yweight");
      if (yweight) {
          var oversize = row.getAttribute("oversize");
          row.height = Math.max(Math.floor((diff * yweight) / 100) - oversize, oversize);
      }
    }
};