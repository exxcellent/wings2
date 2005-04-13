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

