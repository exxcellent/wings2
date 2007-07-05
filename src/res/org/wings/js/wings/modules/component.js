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

wingS.component.initTooltips = function(delay, duration, followMouse) {
    if (config && config.Delay && config.Duration && config.FollowMouse) {
    	config.Delay = delay;
		config.Duration = duration;
		config.FollowMouse = followMouse;
    }
};