/* wingS2 Drag and Drop, using walter zorns approach.
   wdnd stands for wings Drag and Drop and is used to avoid
   typical js namespace clutter.
   my is the prefix used by walter zorns library.
*/

var wdnd_textCopy;

function my_PickFunc()
{
	if (dd.obj.dragsource && !dd.obj.oimg) { // we need a copy of this to stay in place while this is dragged around...
		srcDiv = dd.getDiv(dd.obj.name);
		wdnd_textCopy=srcDiv.cloneNode(true);
		// set some differing styles
		wdnd_textCopy.style.position='absolute';
		wdnd_textCopy.style.left=dd.obj.x+'px';
		wdnd_textCopy.style.top=dd.obj.y+'px';
		wdnd_textCopy.style.width=dd.obj.w+'px';
		wdnd_textCopy.style.height=dd.obj.h+'px';
		wdnd_textCopy.style.zIndex=dd.obj.z-1;
		if (dd.n6) wdnd_textCopy.style.MozOpacity = 1;
		if (dd.ie && !dd.iemac) wdnd_textCopy.style.filter = "Alpha(opacity=100)";
		// add it to the body
		document.getElementsByTagName('body')[0].appendChild(wdnd_textCopy);
	}
}

function my_DropFunc()
{
	if (dd.obj.dragsource) {
		for(i=0;i<dd.elements.length;i++) {
			var tmpEl = dd.elements[i];
			if (tmpEl != dd.obj && tmpEl.droptarget) {
				if ( (dd.e.x >= tmpEl.x) && (dd.e.x <= (tmpEl.x + tmpEl.w)) && (dd.e.y >= tmpEl.y) && (dd.e.y <= (tmpEl.y + tmpEl.h)) ) {
					loc = document.location.href;
					loc = loc.substring(0,loc.indexOf('?')+1); 
					// + tmpEl.name.substring(0,tmpEl.name.indexOf('_Box')) + '=dropped' + dd.obj.name.substring(0,dd.obj.name.indexOf('_Box'));
					loc = loc + wdnd_managerId + '=dropTarget' + tmpEl.name + '&' + wdnd_managerId + '=dragSource' + dd.obj.name;
					document.location.href=loc;
				}
			}
		}
		//move back
		dd.obj.moveTo(dd.obj.defx, dd.obj.defy);
		if (wdnd_textCopy) {
			document.getElementsByTagName('body')[0].removeChild(wdnd_textCopy);
			wdnd_textCopy=null;
		}
	}
}
