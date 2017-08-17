function add_productinfo(){
	$("#travelTypeId").rules("add",{required:true,messages:{required:"必填信息"}});  
	$("#targetAreaName").rules("add",{required:true,messages:{required:"必填信息"}});  
	$("#acitivityName").rules("add",{required:true,messages:{required:"必填信息"}});
	$(":checkbox[name='payMode']").rules("add",{required:true,messages:{required:"付款方式 为必选信息"}});
	$("#fromArea").rules("add",{required:true,messages:{required:"必填信息"}});  
	$("#activityLevelId").rules("add",{required:true,messages:{required:"必填信息"}});  
	$("#activityTypeId").rules("add",{required:true,messages:{required:"必填信息"}});  
	$("#trafficMode").rules("add",{required:true,messages:{required:"必填信息"}});  
	$("#activityDuration").rules("add",{required:true,digits:true,messages:{required:"必填信息",digits:"请输入正确的数字"}});
	$("input[name='intermodalGroupPart']").rules("add",{required:true,checkSameIntermodal:true,messages:{required:"必填信息",checkSameIntermodal:"联运分区重复"}});

//        $("#remainDays").rules("add",{required:true,digits:true,min:1,messages:{required:"必填信息",digits:"请输入正确的数字",min:"保留天数需要大于0"}});
	//$("#remainDays").rules("add",{required:true,digits:true,messages:{required:"必填信息",digits:"请输入正确的数字"}});
	//$("#groupOpenDate").rules("add",{required:true,date:true,messages:{required:"必填信息",date:"日期格式错误"}});
	//$("#groupCloseDate").rules("add",{required:true,date:true,messages:{required:"必填信息",date:"日期格式错误"}});
}
function remove_productinfo(){
	$("#travelTypeId").rules("remove");  
	$("#targetAreaName").rules("remove");  
	$("#acitivityName").rules("remove");  
	$("#fromArea").rules("remove");  
	$("#activityLevelId").rules("remove");  
	$("#activityTypeId").rules("remove");  
	$("#trafficMode").rules("remove");  
	$("#introduction").rules("remove");  
	$("#activityDuration").rules("remove");
	$(":checkbox[name='payMode']").rules("remove");
	//$("#groupOpenDate").rules("remove");
	//$("#groupCloseDate").rules("remove");
}
function add_groupsvalidator(){
	var len = $("#contentTable tbody").find("input").length;
	if(len!=0){
		$("#contentTable tbody tr").find("input[name='groupCloseDate']").rules("add",{required:true,messages:{required:"必填信息"}});
		$("#contentTable tbody tr").find("input[name='groupCode']").rules("add",{required:true,nosamecode:true,messages:{required:"必填信息",nosamecode:"团号重复"}});
		//$("#contentTable tbody tr").find("input[name='visaCountry']").rules("add",{required:true,messages:{required:"必填信息"}});
		$("#contentTable tbody tr").find("input[name='visaDate']").rules("add",{required:true,messages:{required:"必填信息"}});
		$("#contentTable tbody tr").find("input[name='settlementAdultPrice']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
		$("#contentTable tbody tr").find("input[name='settlementcChildPrice']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
		$("#contentTable tbody tr").find("input[name='settlementSpecialPrice']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
//			$("#contentTable tbody tr").find("input[name='trekizPrice']").rules("add",{number:true,addtrekiz:true,nonzero:true,messages:{number:"必须输入数字",addtrekiz:"trekiz价不能为空或大于同行价",nonzero:"输入不能为0"}});
//			$("#contentTable tbody tr").find("input[name='trekizChildPrice']").rules("add",{number:true,addtrekizchild:true,nonzero:true,messages:{number:"必须输入数字",addtrekizchild:"trekiz价不能大于同行价",nonzero:"输入不能为0"}});
		$("#contentTable tbody tr").find("input[name='suggestAdultPrice']").rules("add",{number:true,addadultsuggest:true,nonzero:true,min:0,messages:{number:"必须输入数字",addadultsuggest:"直客价不能小于同行价",nonzero:"输入不能为0",min:"价格不能小于0"}});
		$("#contentTable tbody tr").find("input[name='suggestChildPrice']").rules("add",{number:true,addchildsuggest:true,nonzero:true,min:0,messages:{number:"必须输入数字",addchildsuggest:"直客价不能小于同行价",nonzero:"输入不能为0",min:"价格不能小于0"}});
		$("#contentTable tbody tr").find("input[name='suggestSpecialPrice']").rules("add",{number:true,addspecialsuggest:true,nonzero:true,min:0,messages:{number:"必须输入数字",addspecialsuggest:"直客价不能小于同行价",nonzero:"输入不能为0",min:"价格不能小于0"}});
		$("#contentTable tbody tr").find("input[name='singleDiff']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"单房差不能小于0"}});
		$("#contentTable tbody tr").find("input[name='payDeposit']").rules("add",{number:true,addpaydeposit:true,nonzero:true,min:0,messages:{number:"必须输入数字",addpaydeposit:"订金不能大于同行价",nonzero:"输入不能为0",min:"订金不能小于0"}});
		$("#contentTable tbody tr").find("input[name='planPosition']").rules("add",{required:true,digits:true,min:0,messages:{required:"必填信息",digits:"必须输入数字",min:"预收人数不能小于0"}});
		$("#contentTable tbody tr").find("input[name='freePosition']").rules("add",{required:true,digits:true,min:0,messages:{required:"必填信息",digits:"必须输入数字",min:"余位数不能小于0"}});
	}
}
function remove_groupsvalidator(){
	var len = $("#contentTable tbody").find("input").length;
	if(len!=0){
		$("#contentTable tbody tr").find("input[name='groupCloseDate']").rules("remove");
		$("#contentTable tbody tr").find("input[name='groupCode']").rules("remove");
		//$("#contentTable tbody tr").find("input[name='visaCountry']").rules("remove");
		$("#contentTable tbody tr").find("input[name='visaDate']").rules("remove");
		$("#contentTable tbody tr").find("input[name='settlementAdultPrice']").rules("remove");
		$("#contentTable tbody tr").find("input[name='settlementcChildPrice']").rules("remove");
		$("#contentTable tbody tr").find("input[name='settlementSpecialPrice']").rules("remove");
//			$("#contentTable tbody tr").find("input[name='trekizPrice']").rules("remove");
//			$("#contentTable tbody tr").find("input[name='trekizChildPrice']").rules("remove");
		$("#contentTable tbody tr").find("input[name='suggestAdultPrice']").rules("remove");
		$("#contentTable tbody tr").find("input[name='suggestChildPrice']").rules("remove");
		$("#contentTable tbody tr").find("input[name='suggestSpecialPrice']").rules("remove");
		$("#contentTable tbody tr").find("input[name='singleDiff']").rules("remove");
		$("#contentTable tbody tr").find("input[name='payDeposit']").rules("remove");
		$("#contentTable tbody tr").find("input[name='planPosition']").rules("remove");
		$("#contentTable tbody tr").find("input[name='freePosition']").rules("remove");
	}
}
function add_modgroupsvalidator(){
	var len = $("#modTable tbody").find("input").length;
	if(len!=0){
		$("#modTable tbody tr").find("input[name='groupCloseDate']").rules("add",{required:true,messages:{required:"必填信息"}});
		//$("#modTable tbody tr").find("input[name='groupCode']").rules("add",{required:true,nosamecode:true,messages:{required:"必填信息",nosamecode:"团号重复"}});
		//$("#modTable tbody tr").find("input[name='visaCountry']").rules("add",{required:true,messages:{required:"必填信息"}});
		$("#modTable tbody tr").find("input[name='visaDate']").rules("add",{required:true,messages:{required:"必填信息"}});
		$("#modTable tbody tr").find("input[name='settlementAdultPrice']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
		$("#modTable tbody tr").find("input[name='settlementcChildPrice']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
		$("#modTable tbody tr").find("input[name='settlementSpecialPrice']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
//			$("#modTable tbody tr").find("input[name='trekizPrice']").rules("add",{number:true,modtrekiz:true,nonzero:true,messages:{number:"必须输入数字",modtrekiz:"trekiz不能为空或大于同行价",nonzero:"输入不能为0"}});
//			$("#modTable tbody tr").find("input[name='trekizChildPrice']").rules("add",{number:true,modtrekizchild:true,nonzero:true,messages:{number:"必须输入数字",modtrekizchild:"trekiz不能大于同行价",nonzero:"输入不能为0"}});
		$("#modTable tbody tr").find("input[name='suggestAdultPrice']").rules("add",{number:true,modadultsuggest:true,nonzero:true,min:0,messages:{number:"必须输入数字",modadultsuggest:"建议零售价不能小于同行价",nonzero:"输入不能为0",min:"价格不能小于0"}});
		$("#modTable tbody tr").find("input[name='suggestChildPrice']").rules("add",{number:true,modchildsuggest:true,nonzero:true,min:0,messages:{number:"必须输入数字",modchildsuggest:"建议零售价不能小于同行价",nonzero:"输入不能为0",min:"价格不能小于0"}});
		$("#modTable tbody tr").find("input[name='suggestSpecialPrice']").rules("add",{number:true,modspecialsuggest:true,nonzero:true,min:0,messages:{number:"必须输入数字",modspecialsuggest:"建议零售价不能小于同行价",nonzero:"输入不能为0",min:"价格不能小于0"}});
		$("#modTable tbody tr").find("input[name='singleDiff']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"单房差不能小于0"}});
		$("#modTable tbody tr").find("input[name='payDeposit']").rules("add",{number:true,modpaydeposit:true,nonzero:true,min:0,messages:{number:"必须输入数字",modpaydeposit:"订金不能大于同行价",nonzero:"输入不能为0",min:"订金不能小于0"}});
		$("#modTable tbody tr").find("input[name='planPosition']").rules("add",{required:true,digits:true,min:0,messages:{required:"必填信息",digits:"必须输入数字",min:"预收人数不能小于0"}});
		$("#modTable tbody tr").find("input[name='freePosition']").rules("add",{required:true,digits:true,min:0,messages:{required:"必填信息",digits:"必须输入数字",min:"余位数不能小于0"}});
	}
}
function remove_modgroupsvalidator(){
	var len = $("#modTable tbody").find("input").length;
	if(len!=0){
		$("#modTable tbody tr").find("input[name='groupCloseDate']").rules("remove");
		//$("#modTable tbody tr").find("input[name='groupCode']").rules("add",{required:true,nosamecode:true,messages:{required:"必填信息",nosamecode:"团号重复"}});
		//$("#modTable tbody tr").find("input[name='visaCountry']").rules("remove");
		$("#modTable tbody tr").find("input[name='visaDate']").rules("remove");
		$("#modTable tbody tr").find("input[name='settlementAdultPrice']").rules("remove");
		$("#modTable tbody tr").find("input[name='settlementcChildPrice']").rules("remove");
		$("#modTable tbody tr").find("input[name='settlementSpecialPrice']").rules("remove");
//			$("#modTable tbody tr").find("input[name='trekizPrice']").rules("remove");
//			$("#modTable tbody tr").find("input[name='trekizChildPrice']").rules("remove");
		$("#modTable tbody tr").find("input[name='suggestAdultPrice']").rules("remove");
		$("#modTable tbody tr").find("input[name='suggestChildPrice']").rules("remove");
		$("#modTable tbody tr").find("input[name='suggestSpecialPrice']").rules("remove");
		$("#modTable tbody tr").find("input[name='singleDiff']").rules("remove");
		$("#modTable tbody tr").find("input[name='payDeposit']").rules("remove");
		$("#modTable tbody tr").find("input[name='planPosition']").rules("remove");
		$("#modTable tbody tr").find("input[name='freePosition']").rules("remove");
	}
}
function add_filevalidator(){
	$("#introduction").rules("add",{required:true,messages:{required:"必填信息"}});
}
function remove_filevalidator(){
	$("#introduction").rules("remove");
}

//发布机票产品添加验证规则
function add_productinfoAir(){
	$("#startCity").next().find(".custom-combobox-input").attr("name","startCity1").attr("id","startCity1").rules("add",{cityRequired:true});  
	$("#endCity").next().find(".custom-combobox-input").attr("name","endCity1").attr("id","endCity1").rules("add",{cityRequired:true});
	$(":checkbox[name='payMode']").rules("add",{required:true,messages:{required:"付款方式 为必选信息"}});
	//出发、到达机场验证
	$(".input-append .appendtext").each(function(index, element) {
        $(element).rules("add",{required:true,messages:{required:"必填信息"}});
    });
	//预约舱位等级
	var array = new Array();
	$(":checkbox[name*='grade']").each(function(index, element) {
		var cbxName = $(element).attr("name");
        if("-1" == $.inArray(cbxName,array)){
			$(":checkbox[name='" + cbxName + "']").rules("add",{required:true,messages:{required:"预约舱位等级 为必选信息"}});
			array.push(cbxName);
		}
    });
	//$(":checkbox[name*='sdirectgrade']").rules("add",{required:true,messages:{required:"预约舱位等级 为必选信息"}});
}