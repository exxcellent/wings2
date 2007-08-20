/***************************************************************************************************
 * WINGS.COMPONENT  --  contains: functions used for special components
 **************************************************************************************************/

// Create module namespace
wingS.namespace("component");


wingS.component.initTooltips = function(delay, duration, followMouse) {
    if (config && config.Delay && config.Duration && config.FollowMouse) {
    	config.Delay = delay;
		config.Duration = duration;
		config.FollowMouse = followMouse;
    }
};

wingS.component.splitPaneResized = function(sb, size) {
    wingS.request.sendEvent(null, true, true, sb.el.id, size);
}
