

function showSeachVisaResult(reslutUl,data,visaType) {
    reslutUl.empty();
    $.each(data,function(key,value){
        var li = $("<li></li>");
        var visaTypeName="";
        $.each(visaType,function(keyin,valuein){
            if(valuein.value==value.visaType){
                visaTypeName = valuein.label;
            }
        });
        var a = $("<a class=\"resultli\">"+value.countryName+"("+visaTypeName+")</a>");
        a.click(function(){
            
            if(value.srcDocId==null||value.srcDocId==undefined){
                return false;
            }
            window.open ('../../sys/docinfo/download/'+value.srcDocId);
           /*$.ajax({
                type: "POST",
                url: '../../sys/docinfo/downloadDoc/'+value.srcDocId,
                success: function(msg){
                    if(msg!=null&&msg!=undefined&&msg!=""){
                       var  uri = encodeURI("../../../doc/"+msg);
                        window.open (uri);
                    }else{
                        alert("没有可下载签证文件");
                    }
                }
             });*/
        });
        li.append(a);
        reslutUl.append(li);
    });
}

(function($){
	//发布签证产品 签证国家和签证领区必填
	$.validator.addMethod("visaRequired", function(value,element){
        var inputValue = $(element).next().find(".custom-combobox-input").val();//alert(inputValue);
        if(" " == value || ("" == inputValue)){
			return false;
		}else{
			return true;
		}
    },"必填信息");
 })(jQuery);

function findMoreProduct(ctx) {
	//获取按自动规则生成的团号
	getMaxCount(ctx);
	var sysCountry = $("#sysCountryId").val();
	var collarZoning = $("#collarZoning").val();
	var visaType = $("#visaType").val();
	var proId = $("#visaProductId").val();
	var deptId = $("#deptId").val();
	//当前团号生成方式:0 手动；1 自动
	var groupCodeRuleQZ = $("#groupCodeRuleQZ").val();
	//C460 获取团号，以便校验是否重复
	var groupCodeOld = $("#groupCodeOld").val();
	var groupCode = $("#groupCode").val();
	//团号自动生成时 或没有修改团号时 不需校验重复
	if(groupCodeRuleQZ==1 || groupCodeOld==groupCode){
		groupCode = '';
	}
	$.ajax({
		type:"POST",
		url: ctx + "/visa/visaProducts/findMoreProduct",
		async : false,
		data:{
			sysCountry : sysCountry,
			collarZoning : collarZoning,
			visaType : visaType,
			proId : proId,
			deptId : deptId,
			groupCode : groupCode
		},
		success:function(result){
			if('true' == result) {
				$("#addForm").submit();
			}else if('error' == result){
				$.jBox.info("您的部门信息有误，请确认后再发布！", "系统提示");
			}else if('wrong' == result){
				$.jBox.info("团号重复！", "系统提示");
			}else{
				$.jBox.info("已有同类产品，请确认是否已经删除！", "系统提示");
			}
		}
	});
}


function getMaxCount(ctx) {
	var groupNum = '';
	//新行者团号生成规则
	if($("#groupCodeRule").length != 0) {
		var groupOpenDate = "";
		
		if($("#groupCodeRule").children("option:selected").text() == 'BJ-SP' || $("#secondStepEnd").find("[name='groupCode'][value*='BJ-DT'],[value*='BJ-DB'],[value*='BJ-ZM']").length == 0) {		
			$.ajax({
				type : "POST",
				async : false,
				url : ctx + "/activity/manager/getCurrentDateMaxGroupCode?groupOpenDate=" + groupOpenDate,
				success : function(result){
					groupNum = result;
				}
			});
		//如果是新行者的规则再次添加团期，则需要在本地累加，不从数据库获取。
		}else{
			var countArr = groupNum.split("-");
			var count = Number(countArr[countArr.length - 1]) + 1;
			if(1 == count.toString().length){
				countArr[countArr.length - 1] = "000" + count;
			}else if(2 == count.toString().length){
				countArr[countArr.length - 1] = "00" + count;
			}else if(3 == count.toString().length){
				countArr[countArr.length - 1] = "0" + count;
			}else{
				countArr[countArr.length - 1] = count;
			}
			groupNum = countArr.join("-");
		}
		$("#groupCode").val( $("#groupCodeRule option:selected").text()+"-"+groupNum );
	}
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
