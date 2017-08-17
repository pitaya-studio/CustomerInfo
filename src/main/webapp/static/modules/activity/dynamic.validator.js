function add_productinfo(){
//	if($("#oneStepDiv").find("#travelTypeId").length != 0)
//        $("#travelTypeId").rules("add",{required:true,messages:{required:"必填信息"}});  
	if($("#oneStepDiv").find("#targetAreaName").length != 0)
        $("#targetAreaName").rules("add",{required:true,messages:{required:"必填信息"}});  
	if($("#oneStepDiv").find("#acitivityName").length != 0)
        $("#acitivityName").rules("add",{required:true,messages:{required:"必填信息"}});
	if($("#oneStepDiv").find("#groupNoMark").length != 0)
        $("#groupNoMark").rules("add",{required:true,messages:{required:"必填信息"}});
	if($("#oneStepDiv").find("#deptId").length != 0)
        $("#deptId").rules("add",{required:true,messages:{required:"必填信息"}});
	if($("#oneStepDiv").find(":checkbox[name='payMode']").length != 0)
        $(":checkbox[name='payMode']").rules("add",{required:true,messages:{required:"付款方式 为必选信息"}});
	if($("#oneStepDiv").find("#fromArea").length != 0)
        $("#fromArea").rules("add",{areaRequired:true,messages:{areaRequired:"必填信息"}});
	if($("#oneStepDiv").find("#backArea").length != 0)
        $("#backArea").rules("add",{areaRequired:true,messages:{areaRequired:"必填信息"}});
	if($("#oneStepDiv").find("#opUserId").length != 0)
        $("#opUserId").rules("add",{required:true,messages:{required:"必填信息"}});
	//if($("#oneStepDiv").find("#groupLead").length != 0)
    //    $("#groupLead").rules("add",{required:true,messages:{required:"必填信息"}});  
//	if($("#oneStepDiv").find("#activityLevelId").length != 0)
//        $("#activityLevelId").rules("add",{required:true,messages:{required:"必填信息"}});
//    if($("#oneStepDiv").find("#activityTypeId").length != 0)    
//        $("#activityTypeId").rules("add",{required:true,messages:{required:"必填信息"}});  
    if($("#oneStepDiv").find("#trafficMode").length != 0)
        $("#trafficMode").rules("add",{required:true,messages:{required:"必填信息"}});  
    if($("#oneStepDiv").find("#activityDuration").length != 0)
        $("#activityDuration").rules("add",{required:true,digits:true,min:1,messages:{required:"必填信息",digits:"请输入正确的数字",min:"不能为0"}});
    if($("#oneStepDiv").find("input[name='intermodalGroupPart']").length != 0)
    	$("input[name='intermodalGroupPart']").rules("add",{required:true,checkSameIntermodal:true,messages:{required:"必填信息",checkSameIntermodal:"联运分区重复"}});

//        $("#remainDays").rules("add",{required:true,digits:true,min:1,messages:{required:"必填信息",digits:"请输入正确的数字",min:"保留天数需要大于0"}});
        //$("#remainDays").rules("add",{required:true,digits:true,messages:{required:"必填信息",digits:"请输入正确的数字"}});
        //$("#groupOpenDate").rules("add",{required:true,date:true,messages:{required:"必填信息",date:"日期格式错误"}});
        //$("#groupCloseDate").rules("add",{required:true,date:true,messages:{required:"必填信息",date:"日期格式错误"}});
	}
	function remove_productinfo(){
		if($("#oneStepDiv").find("#travelTypeId").length !=0)
			$("#travelTypeId").rules("remove");  
		if($("#oneStepDiv").find("#targetAreaName").length !=0)
        	$("#targetAreaName").rules("remove");  
		if($("#oneStepDiv").find("#acitivityName").length !=0)
        	$("#acitivityName").rules("remove");  
		if($("#oneStepDiv").find("#fromArea").length !=0)
        	$("#fromArea").rules("remove");  
		if($("#oneStepDiv").find("#opUserId").length !=0)
        	$("#opUserId").rules("remove"); 
		if($("#oneStepDiv").find("#activityLevelId").length !=0)
        	$("#activityLevelId").rules("remove");  
		if($("#oneStepDiv").find("#activityTypeId").length !=0)
        	$("#activityTypeId").rules("remove");  
		if($("#oneStepDiv").find("#trafficMode").length !=0)
        	$("#trafficMode").rules("remove");  
		if($("#oneStepDiv").find("#introduction").length !=0)
        	$("#introduction").rules("remove");  
		if($("#oneStepDiv").find("#activityDuration").length !=0)
        	$("#activityDuration").rules("remove");
		if($("#oneStepDiv").find(":checkbox[name='payMode']").length !=0)
        	$(":checkbox[name='payMode']").rules("remove");
        //$("#groupOpenDate").rules("remove");
        //$("#groupCloseDate").rules("remove");
	}
	function add_modproductinfo() {
		if($("#oneStepContent").find("#acitivityName").length != 0) {
			$("#acitivityName").rules("add",{required:true,messages:{required:"必填信息"}});
		}
		if($("#oneStepContent").find("#fromArea").length != 0) {
			$("#fromArea").rules("add",{required:true,messages:{required:"必填信息"}});
		}
		if($("#oneStepContent").find("#opUserId").length != 0) {
			$("#opUserId").rules("add",{required:true,messages:{required:"必填信息"}});
		}
		if($("#oneStepContent").find("#targetArea").length != 0) {
			$("#targetArea").rules("add",{required:true,messages:{required:"必填信息"}});
		}
		if($("#oneStepContent").find("#trafficMode").length != 0) {
			$("#trafficMode").rules("add",{required:true,messages:{required:"必填信息"}});
		}
		if($("#oneStepContent").find("#activityDuration").length != 0) {
			$("#activityDuration").rules("add",{required:true,messages:{required:"必填信息"}});
		}
//		if($("#oneStepContent").find("#groupLead").length != 0) {
//			$("#groupLead").rules("add",{required:true,messages:{required:"必填信息"}});
//		}
		if($("#oneStepContent").find(":checkbox[name='payMode']").length != 0) {
			$(":checkbox[name='payMode']").rules("add",{required:true,messages:{required:"必填信息"}});
		}
	}
	function remove_modproductinfo() {
		if($("#oneStepContent").find("#acitivityName").length !=0) {
        	$("#acitivityName").rules("remove");
		}
		if($("#oneStepContent").find("#fromArea").length !=0) {
        	$("#fromArea").rules("remove");
		}
		if($("#oneStepContent").find("#opUserId").length !=0) {
        	$("#opUserId").rules("remove");
		}
		if($("#oneStepContent").find("#targetArea").length !=0) {
        	$("#targetArea").rules("remove");
		}
		if($("#oneStepContent").find("#trafficMode").length !=0) {
        	$("#trafficMode").rules("remove");
		}
		if($("#oneStepContent").find("#activityDuration").length !=0) {
        	$("#activityDuration").rules("remove");
		}
		//if($("#oneStepContent").find("#groupLead").length !=0) {
        //	$("#groupLead").rules("remove");
		//}
		if($("#oneStepContent").find(":checkbox[name='payMode']").length !=0) {
        	$(":checkbox[name='payMode']").rules("remove");
		}
	}
	
	
	
	function add_groupsvalidator(){
		var len = $("#contentTable tbody").find("input").length;
		if(len!=0){
			$(".add2-money").find("#selectCurrency").rules("add",{required:true,messages:{required:"必填信息"}});
//			$("#contentTable tbody tr").find("input[name='groupCloseDate']").rules("add",{required:true,messages:{required:"必填信息"}});
			
			//解决bug12826 游轮产品团号重复校验特殊处理
			//$("#contentTable tbody tr").find("input[name='groupCode']").rules("add",{required:true,nosamecode:true,messages:{required:"必填信息",nosamecode:"团号重复"}});
			var activityKind = $("#activityKind").val();
			if(activityKind=='10'){
				$("#contentTable tbody tr").find("input[name='groupCode']").rules("add",{required:true,nosamecode4YL:true,messages:{required:"必填信息",nosamecode4YL:"团号重复"}});
			}else{
				$("#contentTable tbody tr").find("input[name='groupCode']").rules("add",{required:true,nosamecode:true,messages:{required:"必填信息",nosamecode:"团号重复"}});
			}
			
			
			//$("#contentTable tbody tr").find("input[name='visaCountry']").rules("add",{required:true,messages:{required:"必填信息"}});
//			$("#contentTable tbody tr").find("input[name='visaDate']").rules("add",{required:true,messages:{required:"必填信息"}});
			$("#contentTable tbody tr").find("input[name='settlementAdultPrice']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
			$("#contentTable tbody tr").find("input[name='settlementcChildPrice']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
			if($("#contentTable tbody tr").find("input[name='settlementSpecialPrice']").length != 0) {
				$("#contentTable tbody tr").find("input[name='settlementSpecialPrice']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
			}
//			$("#contentTable tbody tr").find("input[name='trekizPrice']").rules("add",{number:true,addtrekiz:true,nonzero:true,messages:{number:"必须输入数字",addtrekiz:"trekiz价不能为空或大于同行价",nonzero:"输入不能为0"}});
//			$("#contentTable tbody tr").find("input[name='trekizChildPrice']").rules("add",{number:true,addtrekizchild:true,nonzero:true,messages:{number:"必须输入数字",addtrekizchild:"trekiz价不能大于同行价",nonzero:"输入不能为0"}});
			
			if($("#contentTable tbody tr").find("input[name='suggestAdultPrice']").length != 0)
				$("#contentTable tbody tr").find("input[name='suggestAdultPrice']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
			if($("#contentTable tbody tr").find("input[name='suggestChildPrice']").length != 0)
				$("#contentTable tbody tr").find("input[name='suggestChildPrice']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
			if($("#contentTable tbody tr").find("input[name='suggestSpecialPrice']").length != 0)
				$("#contentTable tbody tr").find("input[name='suggestSpecialPrice']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
			
			$("#contentTable tbody tr").find("input[name='singleDiff']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"单房差不能小于0"}});
			$("#contentTable tbody tr").find("input[name='payDeposit']").rules("add",{number:true,addpaydeposit:true,nonzero:true,min:0,messages:{number:"必须输入数字",addpaydeposit:"订金不能大于同行价",nonzero:"输入不能为0",min:"订金不能小于0"}});
			$("#contentTable tbody tr").find("input[name='planPosition']").rules("add",{required:true,digits:true,min:0,messages:{required:"必填信息",digits:"必须输入数字",min:"预收人数不能小于0"}});
			$("#contentTable tbody tr").find("input[name='freePosition']").rules("add",{required:true,digits:true,min:0,messages:{required:"必填信息",digits:"必须输入数字",min:"余位数不能小于0"}});
		}
	}
	function remove_groupsvalidator(){
		var len = $("#contentTable tbody").find("input").length;
		if(len!=0){
//			$("#contentTable tbody tr").find("input[name='groupCloseDate']").rules("remove");
			$("#contentTable tbody tr").find("input[name='groupCode']").rules("remove");
			//$("#contentTable tbody tr").find("input[name='visaCountry']").rules("remove");
//			$("#contentTable tbody tr").find("input[name='visaDate']").rules("remove");
			$("#contentTable tbody tr").find("input[name='settlementAdultPrice']").rules("remove");
			$("#contentTable tbody tr").find("input[name='settlementcChildPrice']").rules("remove");
			if($("#contentTable tbody tr").find("input[name='settlementSpecialPrice']").length != 0) {
				$("#contentTable tbody tr").find("input[name='settlementSpecialPrice']").rules("remove");
			}
//			$("#contentTable tbody tr").find("input[name='trekizPrice']").rules("remove");
//			$("#contentTable tbody tr").find("input[name='trekizChildPrice']").rules("remove");
			if($("#contentTable tbody tr").find("input[name='suggestAdultPrice']").length != 0)
				$("#contentTable tbody tr").find("input[name='suggestAdultPrice']").rules("remove");
			if($("#contentTable tbody tr").find("input[name='suggestChildPrice']").length != 0)
				$("#contentTable tbody tr").find("input[name='suggestChildPrice']").rules("remove");
			if($("#contentTable tbody tr").find("input[name='suggestSpecialPrice']").length != 0)
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
//			$("#modTable tbody tr").find("input[name='groupCloseDate']").rules("add",{required:true,messages:{required:"必填信息"}});
			//$("#modTable tbody tr").find("input[name='groupCode']").rules("add",{required:true,nosamecode:true,messages:{required:"必填信息",nosamecode:"团号重复"}});
			//$("#modTable tbody tr").find("input[name='visaCountry']").rules("add",{required:true,messages:{required:"必填信息"}});
//			$("#modTable tbody tr").find("input[name='visaDate']").rules("add",{required:true,messages:{required:"必填信息"}});
			if($("#modTable tbody tr").find("input[name='settlementAdultPrice']").length != 0) {
				$("#modTable tbody tr").find("input[name='settlementAdultPrice']").rules("add", {
					number: true,
					min: 0,
					messages: {number: "必须输入数字", nonzero: "输入不能为0", min: "价格不能小于0"}
				});
			}
			if($("#modTable tbody tr").find("input[name='settlementcChildPrice']").length != 0) {
				$("#modTable tbody tr").find("input[name='settlementcChildPrice']").rules("add", {
					number: true,
					min: 0,
					messages: {number: "必须输入数字", nonzero: "输入不能为0", min: "价格不能小于0"}
				});
			}
			if($("#modTable tbody tr").find("input[name='settlementSpecialPrice']").length != 0) {
				$("#modTable tbody tr").find("input[name='settlementSpecialPrice']").rules("add",{number:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
			}
//			$("#modTable tbody tr").find("input[name='trekizPrice']").rules("add",{number:true,modtrekiz:true,nonzero:true,messages:{number:"必须输入数字",modtrekiz:"trekiz不能为空或大于同行价",nonzero:"输入不能为0"}});
//			$("#modTable tbody tr").find("input[name='trekizChildPrice']").rules("add",{number:true,modtrekizchild:true,nonzero:true,messages:{number:"必须输入数字",modtrekizchild:"trekiz不能大于同行价",nonzero:"输入不能为0"}});
			if($("#modTable tbody tr").find("input[name='suggestAdultPrice']").length != 0)
				$("#modTable tbody tr").find("input[name='suggestAdultPrice']").rules("add",{number:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
			if($("#modTable tbody tr").find("input[name='suggestChildPrice']").length != 0)
				$("#modTable tbody tr").find("input[name='suggestChildPrice']").rules("add",{number:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
			if($("#modTable tbody tr").find("input[name='suggestSpecialPrice']").length != 0)
				$("#modTable tbody tr").find("input[name='suggestSpecialPrice']").rules("add",{number:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
			//$("#modTable tbody tr").find("input[name='singleDiff']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"单房差不能小于0"}});//以前用这个
			if($("#modTable tbody tr").find("input[name='singleDiff']").length != 0)
				$("#modTable tbody tr").find("input[name='singleDiff']").rules("add",{number:true,min:0,messages:{number:"必须输入数字",min:"单房差不能小于0"}});
			if($("#modTable tbody tr").find("input[name='payDeposit']").length != 0)
				$("#modTable tbody tr").find("input[name='payDeposit']").rules("add",{number:true,modpaydeposit:true,nonzero:true,min:0,messages:{number:"必须输入数字",modpaydeposit:"订金不能大于同行价",nonzero:"输入不能为0",min:"订金不能小于0"}});
			if($("#modTable tbody tr").find("input[name='planPosition']").length != 0)
				$("#modTable tbody tr").find("input[name='planPosition']").rules("add",{required:true,digits:true,min:0,messages:{required:"必填信息",digits:"必须输入数字",min:"预收人数不能小于0"}});
			if($("#modTable tbody tr").find("input[name='freePosition']").length != 0)
				$("#modTable tbody tr").find("input[name='freePosition']").rules("add",{required:true,digits:true,min:0,messages:{required:"必填信息",digits:"必须输入数字",min:"余位数不能小于0"}});
		}
	}
	function remove_modgroupsvalidator(){
		var len = $("#modTable tbody").find("input").length;
		if(len!=0){
//			$("#modTable tbody tr").find("input[name='groupCloseDate']").rules("remove");
			//$("#modTable tbody tr").find("input[name='groupCode']").rules("add",{required:true,nosamecode:true,messages:{required:"必填信息",nosamecode:"团号重复"}});
			//$("#modTable tbody tr").find("input[name='visaCountry']").rules("remove");
//			$("#modTable tbody tr").find("input[name='visaDate']").rules("remove");
			$("#modTable tbody tr").find("input[name='settlementAdultPrice']").rules("remove");
			$("#modTable tbody tr").find("input[name='settlementcChildPrice']").rules("remove");
			if($("#modTable tbody tr").find("input[name='settlementSpecialPrice']").length != 0) {
				$("#modTable tbody tr").find("input[name='settlementSpecialPrice']").rules("remove");
			}
//			$("#modTable tbody tr").find("input[name='trekizPrice']").rules("remove");
//			$("#modTable tbody tr").find("input[name='trekizChildPrice']").rules("remove");
			if($("#modTable tbody tr").find("input[name='suggestAdultPrice']").length != 0) {
				$("#modTable tbody tr").find("input[name='suggestAdultPrice']").rules("remove");
			}
			if($("#modTable tbody tr").find("input[name='suggestChildPrice']").length != 0) {
				$("#modTable tbody tr").find("input[name='suggestChildPrice']").rules("remove");
			}
			if($("#modTable tbody tr").find("input[name='suggestSpecialPrice']").length != 0) {
				$("#modTable tbody tr").find("input[name='suggestSpecialPrice']").rules("remove");
			}
			$("#modTable tbody tr").find("input[name='singleDiff']").rules("remove");
			$("#modTable tbody tr").find("input[name='payDeposit']").rules("remove");
			$("#modTable tbody tr").find("input[name='planPosition']").rules("remove");
			$("#modTable tbody tr").find("input[name='freePosition']").rules("remove");
		}
	}
	function add_filevalidator(){
		
			if($("#introductionVaildator").length>0){
			$("#introductionVaildator").rules("add",{required:true,messages:{required:"必填信息"}});
		}
		
	}
	
	function add_visaFileValidator() {
		if($("[name='country']").length != 0 && $("[name='visaType']").length != 0) {
			$("[name='country']").rules("add",{required:true,messages:{required:"必填信息"}});
			$("[name='visaType']").rules("add",{required:true,messages:{required:"必填信息"}});
		}
	}
	function remove_filevalidator(){
		if($("#introductionVaildator").length>0){
			$("#introductionVaildator").rules("remove");
		}
	}
	//如果是单团产品，则不需要验证产品编号
	function remove_serNumValidator() {
		$("#activitySerNum").rules("remove");
	}
	