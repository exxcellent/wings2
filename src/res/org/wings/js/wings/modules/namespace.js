/***************************************************************************************************
 * WINGS.NAMESPACE  --  contains: global namespace object and related functions
 **************************************************************************************************/

/**
 * Create global namespace object
 */
if (typeof wingS == "undefined") {
    var wingS = {};
}

/**
 * Returns the namespace specified and creates it if it doesn't exist. For example both,
 * 'wingS.namespace("property.package");' and 'wingS.namespace("wingS.property.package");'
 * would create wingS.property, then wingS.property.package --> (@see YAHOO.namespace()).
 * @param {String} arguments - 1 to n namespaces to create (separated with '.')
 * @return {Object} reference to the last namespace object created
 */
wingS.namespace = function() {
    var a=arguments, o=null, i, j, d;
    for (i=0; i<a.length; i=i+1) {
        d=a[i].split(".");
        o=wingS;
        
        // wingS is implied, so it is ignored if it is included
        for (j=(d[0] == "wingS") ? 1 : 0; j<d.length; j=j+1) {
            o[d[j]]=o[d[j]] || {};
            o=o[d[j]];
        }
    }
    return o;
};

