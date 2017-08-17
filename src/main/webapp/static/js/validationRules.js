/**
 * name:getCaret
 * function:for IE browser to  get current cursor position,
 *          due to under IE8 not support property:selectionStart
 * @param obj
 */  
   function getCaret(el) {
        if (el.selectionStart) {//IE9 or Standard browser
            return el.selectionStart;
        } else if (document.selection) {//under IE8
            el.focus();
            var r = document.selection.createRange();
            if (r == null) {
                return 0;
            }
            var re = el.createTextRange(), rc = re.duplicate();
            re.moveToBookmark(r.getBookmark());
            rc.setEndPoint('EndToStart', re);
            return rc.text.length;
        }
        return 0;
    }

/**
         * name:checkValue
         * function:to validate the value,which is the rule:
         *          integer max-length is 9 digits,float max
         *          -length is 2 digits.
         * @param obj
         */
        function checkValue(obj){
        	var selectionStart=null;
        	if(navigator.userAgent.indexOf('MSIE') >= 0){
        		selectionStart=getCaret(obj);
        	}else{
        		selectionStart = obj.selectionStart;
        	}
            var rr = $(obj).val(); //get current input value
            var rule = /^[^0-9|\.]$/; //validation rule
            if(rr.length>0){   //validate when has value
                var newStr='';  //temp string
                //filter
                for(var i=0;i<rr.length;i++){
                    var c = rr.substr(i,1);
                    if(!rule.test(c)){ //pass the rule
                        newStr+=c;
                    }
                }
                if(newStr!=''){
                    //only a point left,remove extra 0
                    var szfds = newStr.split('.');//szfds
                    var zs = ''; //interger
                    var xs = ''; //float

                    if(szfds.length>1){
                        zs=szfds[0];
                        xs=szfds[1];
                        for(var i=1;i<zs.length;i++){
                            var zs_char = zs.substr(0,1);
                            if(zs_char=='0'){
                                zs = zs.substring(1,zs.length);
                            }
                        }
                        //9 digits interger
                        if(zs.length>9){
                            zs=zs.substring(0,9);
                        }
                        //2 digits float
                        if(xs.length>2){
                            xs = xs.substring(0,2);
                        }

                        newStr = zs+'.'+xs;
                    }else{
                        zs=szfds[0];

                        if(zs.length>9){
                            newStr=zs.substring(0,9);
                        }
                        for(var i=1;i<zs.length;i++){
                            var zs_char = zs.substr(0,1);
                            if(zs_char=='0'){
                                newStr = zs.substring(1,zs.length);
                            }
                        }
                    }

                    //auto add 0 in front of point
                    if(newStr.indexOf('.')==0){
                        newStr='0'+newStr;
                    }
                }


                $(obj).val(newStr);
                //set cursor position---s//
                if(obj.setSelectionRange)
                {
                    obj.focus();
                    obj.setSelectionRange(selectionStart,selectionStart);
                }
                else if (obj.createTextRange) {
                    var range = obj.createTextRange();
                    range.collapse(true);
                    range.moveEnd('character', selectionStart);
                    range.moveStart('character', selectionStart);
                    range.select();
                }
                //set cursor position---e//
            }

        }