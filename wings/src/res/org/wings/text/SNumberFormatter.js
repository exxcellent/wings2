var old;

function saveOld (__field) {
	old = __field.value;
}

function numberFormatter (__minIntDig, __maxIntDig, __minFracDig, __maxFracDig, __minVal, __maxVal, __groupUsed, __intOnly, __decSep, __groSep, __field) {

        var re;
	
        if ( __groupUsed ) {
                var regexpString = "(^[0-9]*$)|(^[0-9]*\\"+ __decSep +"[0-9]*$)|(^[0-9]{1,3}\\"+ __groSep +"([0-9]{3}\\"+ __groSep +")*[0-9]{3}\\"+ __decSep +"[0-9]*$)";
                re = new RegExp( regexpString );
	} else {
                var regexpString = "(^[0-9]*$)|(^[0-9]*\\"+ __decSep +"[0-9]*$)";
                re = new RegExp( regexpString );
	}
	
	if ( re.test( __field.value ) ) {
                var regexpString = "\\"+__decSep;

                // alert("DecimalSepReg ="+ regexpString); 
                var searchPoint = new RegExp(regexpString);

		var splitValue = __field.value.search(searchPoint);
		if (splitValue == -1) {
                        // alert("splitValue == -1");
			var integerDigits  = __field.value;
			var fractionDigits = "";
		} else {
			var split = new String(__field.value);
			var fractionDigits = split.substring(splitValue + 1);
			var integerDigits = split.substring(0,splitValue);
                        // alert( "FracDig : " + fractionDigits + " IntDig : " + integerDigits ); 
		}

		if ( integerDigits.length > __maxIntDig) {
                        integerDigits = integerDigits.substring( integerDigits.length - __maxIntDig , integerDigits.length ); 
		} else if (integerDigits.length < __minIntDig) { 
                        var fillInt = __minIntDig - integerDigits.length; 
                        for (var i = 0; i < fillInt; i++){ 
                            integerDigits =  "0" + integerDigits ; 
                        }
            	}

		if ( __intOnly == false ) {
			if (fractionDigits.length > __maxFracDig ){
                        	var round = fractionDigits.substring(__maxFracDig-1,__maxFracDig) +"."+ fractionDigits.substring( __maxFracDig );
                        	round = new String(Math.round( round ));
                        	fractionDigits = fractionDigits.substring(0, __maxFracDig-1) + round;
            		} else if (fractionDigits.length < __minFracDig && fractionDigits.length >= 0 ){
                        	var fillFrac = new String( fractionDigits );
                        	for(var i = 0; i < __minFracDig - fractionDigits.length; i++) {
                            		fillFrac = fillFrac + "0";
                        	}
                        	fractionDigits = fillFrac;
            		}
		}

		if ( __field.value < __minVal ) {
                        // alert("Field.value("+__field.value+") < minVal("+__minVal+")");
			__field.value = __minVal;	
			return true;
		} else if ( __field.value > __maxVal) {
			// alert("Field.value("+__field.value+") > maxVal("+__maxVal+")");
                        __field.value = __maxVal;
			return true;
		} else {
			if ( __intOnly ) {
				__field.value = integerDigits;
			} else {
				__field.value = integerDigits + __decSep + fractionDigits;
			}
			return true;
		}

        //      if ( __intOnly ) {
        //              __field.value = Math.round( __field.value );
        //      }


	} else {
		__field.value = old;
		return true;
	}
}
