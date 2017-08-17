(function($){
	
	$.extend($.fn,{
		
		addRules:function(){
			if(this.find("input[name='groupOpenDate']").length!=0)
				this.find("input[name='groupOpenDate']").rules("add",{required:true,messages:{required:"必填信息"}});
//			if(this.find("input[name='groupCloseDate']").length!=0)
//				this.find("input[name='groupCloseDate']").rules("add",{required:true,CloseDateValidator:true,messages:{required:"必填信息"}});
			if(this.find("input[name='groupCode']").length!=0)
				this.find("input[name='groupCode']").rules("add",{required:true,messages:{required:"必填信息"}});
//			if(this.find("input[name='visaDate']").length!=0)
//				this.find("input[name='visaDate']").rules("add",{required:true,visaDateValidator:true,messages:{required:"必填信息"}});
			if(this.find("input[name='settlementAdultPrice']").length!=0)
				this.find("input[name='settlementAdultPrice']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
			if(this.find("input[name='settlementcChildPrice']").length!=0)
				this.find("input[name='settlementcChildPrice']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
			if(this.find("input[name='settlementSpecialPrice']").length!=0)
				this.find("input[name='settlementSpecialPrice']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
//			if(this.find("input[name='trekizPrice']").length!=0)
//				this.find("input[name='trekizPrice']").rules("add",{number:true,nonzero:true,modtrekiz:true,messages:{number:"必须输入数字",nonzero:"输入不能为0",modtrekiz:"trekiz价不能为空或大于同行价"}});
//			if(this.find("input[name='trekizChildPrice']").length!=0)
//				this.find("input[name='trekizChildPrice']").rules("add",{number:true,nonzero:true,modtrekizchild:true,messages:{number:"必须输入数字",nonzero:"输入不能为0",modtrekizchild:"trekiz价不能大于同行价"}});
			if(this.find("input[name='suggestAdultPrice']").length!=0)
				this.find("input[name='suggestAdultPrice']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",modadultsuggest:"直客价不能小于同行价",min:"价格不能小于0"}});
			if(this.find("input[name='suggestChildPrice']").length!=0)
				this.find("input[name='suggestChildPrice']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",modchildsuggest:"直客价不能小于同行价",min:"价格不能小于0"}});
			if(this.find("input[name='suggestSpecialPrice']").length!=0)
				this.find("input[name='suggestSpecialPrice']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",modspecialsuggest:"直客价不能小于同行价",min:"价格不能小于0"}});
			if(this.find("input[name='maxPeopleCount']").length!=0)
				this.find("input[name='maxPeopleCount']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0"}});
			if(this.find("input[name='payDeposit']").length!=0)
				this.find("input[name='payDeposit']").rules("add",{number:true,nonzero:true,modpaydeposit:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",modpaydeposit:"订金不能大于同行价",min:"价格不能小于0"}});
			if(this.find("input[name='singleDiff']").length!=0)
				this.find("input[name='singleDiff']").rules("add",{number:true,nonzero:true,min:0,messages:{number:"必须输入数字",nonzero:"输入不能为0",min:"价格不能小于0"}});
			if(this.find("input[name='planPosition']").length!=0)
				this.find("input[name='planPosition']").rules("add",{required:true,digits:true,min:0,messages:{required:"必填信息",digits:"必须输入数字",min:"预收不能小于0"}});			
			if(this.find("input[name='freePosition']").length!=0)
				this.find("input[name='freePosition']").rules("add",{required:true,digits:true,min:0,messages:{required:"必填信息",digits:"必须输入数字",min:"余位不能小于0"}});
		},
	
		removeAllRules:function(){
			this.find("input[type='text']").each(function(index,obj){
				$(obj).rules("remove"); 
			});
		}
		
	});
	
	//	验证出团日期不能小于截团日期
	jQuery.validator.addMethod("CloseDateValidator", function(value, element) {  
		var groupOpenDate = $(element).parent().parent().find("input[name='groupOpenDate']").val();
		var groupCloseDate = value;
    	return this.optional(element) || groupOpenDate >= groupCloseDate;       
 	}, "出团日期不能小于截团日期");   
	
	jQuery.validator.addMethod("visaDateValidator", function(value, element) {  
		var groupOpenDate = $(element).parent().parent().find("input[name='groupOpenDate']").val();
		var visaDate = value;
    	return this.optional(element) || groupOpenDate >= visaDate;       
 	}, "出团日期不能小于材料截止日期");   
	
})(jQuery);