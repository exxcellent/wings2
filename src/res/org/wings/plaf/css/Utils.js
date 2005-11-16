/* wingS2 Utils, commonly used javascript even useful
   to a wings user. In order to avoid typical js namespace
   clutter, all functions and variables are prepended by the
   string "wu_".
*/
var wu_dom = document.getElementById?1:0;
var wu_ns4 = (document.layers && !wu_dom)?1:0;
var wu_ns6 = (wu_dom && !document.all)?1:0;
var wu_ie5 = (wu_dom && document.all)?1:0;
var wu_konqueror = wu_checkUserAgent('konqueror')?1:0;
var wu_opera = wu_checkUserAgent('opera')?1:0;
var wu_safari = wu_checkUserAgent('safari')?1:0;

function wu_checkUserAgent(string) {
	return navigator.userAgent.toLowerCase().indexOf(string) + 1;
}

/*
the following two functions are a workaround for IE to open a link in the right target/new window
used in AnchorCG
*/

/*
check if a target exists
*/
function wu_checkTarget(target){
    for (var i=0;i<parent.frames.length;i++) {
        if (parent.frames[i].name == target)
            return true;
    }
    return false;
}
/*
if the target exists => change URL, else => open URL in new window
*/
function wu_openlink(target, url){
    if(target==null){
        location.href = url;
    }
    else{
        if(wu_checkTarget(target)){
            parent.frames[target].location.href = url;
        }
        else{
            window.open(url, target);
        }
    }
}
