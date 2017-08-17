
function save(){
	
}
	
	
function upload(){
	
	
}

function preSave(){
	
	
}



function removeCodeCss(obj){
    $(obj).removeAttr("style");
}
function replaceStr(obj) {
	var selectionStart = obj.selectionStart;
    //先将全角转换成半角(全角括号除外)
    var tmp = "";
    for (var i = 0; i < obj.value.length; i++) {
        if (obj.value.charCodeAt(i) > 65248 && obj.value.charCodeAt(i) < 65375 && obj.value.charCodeAt(i) != 65288 && obj.value.charCodeAt(i) != 65289) {
            tmp += String.fromCharCode(obj.value.charCodeAt(i) - 65248);
        } else {
            tmp += String.fromCharCode(obj.value.charCodeAt(i));
        }
    }
    obj.value = tmp;
    //删除掉规定外的字符
    obj.value = obj.value.replace(/[^\u4e00-\u9fa5\w\(\)\（\）\-\+\\/\—\\]/g, '');
    //设置光标的位置
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


    $("#groupCode").attr("title", $("#groupCode").val());
}


/**
 * 
 * @param {} obj
 */
var flag=10;
function validateLong(obj)
{
	replaceStr(obj);
	if($(obj).val().length<=49){
		flag = 10;
	}
	if($(obj).val().length>=50)
		{ 
			if($(obj).val().length==50 && flag==10){
				//$.jBox.tip("团号只能输入50个字符","true");
				flag++;
			}else{
				$.jBox.tip("团号超过50个字符，请修改","error");  
			}
			return false;  
		}
}
