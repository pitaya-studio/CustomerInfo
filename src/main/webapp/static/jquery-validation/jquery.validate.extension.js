/**
 * liangjingming
 * 自定义验证规则
 */
(function($) {
	/*//新增时验证空位数量不能大于预收人数
	$.validator.addMethod("addposition", function(value, element) {       
	      
	    var free = value;
		var plan = $(element).parent().parent().find("input[name='planPosition']").val();
		if(isNaN(free) || isNaN(plan))
			return false;
		else{
			if(parseInt(free)>parseInt(plan)){ 				
				return false;
			}else
				return true;
		}
	 }, "余位数量不能大于预收人数");
	
	//修改时验证空位数量不能大于预收人数
	$.validator.addMethod("modposition", function(value, element) {       
	      
	    var free = value;
		var plan = $(element).parent().parent().find("input[name='planPosition']").val();
		if(isNaN(free) || isNaN(plan))
			return false;
		else{
			if(parseInt(free)>parseInt(plan)){ 				
				return false;
			}else
				return true;
		}
	 }, "余位数量不能大于预收人数");*/
	
	//新增时trekiz成人价不能大于成人同行价
	$.validator.addMethod("addtrekiz", function(value, element){
	
		var trekiz = value;
		var settlement = $(element).parent().parent().find("input[name='settlementAdultPrice']").val();
		if((settlement &&  !trekiz) || isNaN(trekiz) || isNaN(settlement))
			return false;
		else{
			if(parseInt(trekiz)>parseInt(settlement)){
				return false;
			}else
				return true;
		}
	},"trekiz价不能为空或大于同行价");
	
	//新增时trekiz儿童价不能大于儿童同行价
	$.validator.addMethod("addtrekizchild", function(value, element){
		
		var trekiz = value;
		var settlement = $(element).parent().parent().find("input[name='settlementcChildPrice']").val();
		if(isNaN(trekiz) || isNaN(settlement))
			return false;
		else{
			if(parseInt(trekiz)>parseInt(settlement)){ 				
				return false;
			}else
				return true;
		}
	},"trekiz价不能大于同行价");
	
	//修改时trekiz成人价不能大于成人同行价
	$.validator.addMethod("modtrekiz", function(value, element){
		
		var trekiz = value;
		var settlement = $(element).parent().parent().find("input[name='settlementAdultPrice']").val();
		if((settlement &&  !trekiz) || isNaN(trekiz) || isNaN(settlement))
			return false;
		else{
			if(parseInt(trekiz)>parseInt(settlement)){ 				
				return false;
			}else
				return true;
		}
	},"trekiz价不能为空或大于同行价");
	
	//修改时trekiz儿童价不能大于儿童同行价
	$.validator.addMethod("modtrekizchild", function(value, element){
		
		var trekiz = value;
		var settlement = $(element).parent().parent().find("input[name='settlementcChildPrice']").val();
		if(isNaN(trekiz) || isNaN(settlement))
			return false;
		else{
			if(parseInt(trekiz)>parseInt(settlement)){ 				
				return false;
			}else
				return true;
		}
	},"trekiz价不能大于同行价");
	
	//新增时成人建议零售价不能小于成人同行价
	$.validator.addMethod("addadultsuggest", function(value, element){
		
		//如果是单团产品，不需要填写直客价
		if($("#suggestAdultPriceDefine").length<0?true:false)
			return true;
		
		var suggest = value;
		var settlement = $(element).parent().parent().find("input[name='settlementAdultPrice']").val();
		if(isNaN(suggest) || isNaN(settlement))
			return false;
		else{
			if(parseInt(suggest)<parseInt(settlement)){ 	
				return false;
			}else
				return true;
		}
	},"直客价不能小于同行价");
	
	//新增时儿童建议零售价不能小于儿童同行价
	$.validator.addMethod("addchildsuggest", function(value, element){
		
		//如果是单团产品，不需要填写直客价
		if($("#suggestAdultPriceDefine").length<0?true:false)
			return true;
		
		var suggest = value;
		var settlement = $(element).parent().parent().find("input[name='settlementcChildPrice']").val();
		if(isNaN(suggest) || isNaN(settlement))
			return false;
		else{
			if(parseInt(suggest)<parseInt(settlement)){ 				
				return false;
			}else
				return true;
		}
	},"直客价不能小于同行价");

	//新增时儿童建议零售价不能小于儿童同行价
	$.validator.addMethod("addspecialsuggest", function(value, element){
		
		//如果是单团产品，不需要填写直客价
		if($("#suggestAdultPriceDefine").length<0?true:false)
			return true;
		
		var suggest = value;
		var settlement = $(element).parent().parent().find("input[name='settlementSpecialPrice']").val();
		if(isNaN(suggest) || isNaN(settlement))
			return false;
		else{
			if(parseInt(suggest)<parseInt(settlement)){ 				
				return false;
			}else
				return true;
		}
	},"直客价不能小于同行价");
	
	//修改时成人建议零售价不能小于成人同行价
	$.validator.addMethod("modadultsuggest", function(value, element){
		
		//如果是单团产品，不需要填写直客价
		if($("#suggestAdultPriceDefine").length<0?true:false)
			return true;
		
		var suggest = value;
		var settlement = $(element).parent().parent().find("input[name='settlementAdultPrice']").val();
		if(isNaN(suggest) || isNaN(settlement))
			return false;
		else{
			if(parseInt(suggest)<parseInt(settlement)){ 				
				return false;
			}else
				return true;
		}
	},"直客价不能小于同行价");
	
	//修改时儿童建议零售价不能小于儿童同行价
	$.validator.addMethod("modchildsuggest", function(value, element){
		
		//如果是单团产品，不需要填写直客价
		if($("#suggestAdultPriceDefine").length<0?true:false)
			return true;
		
		var suggest = value;
		var settlement = $(element).parent().parent().find("input[name='settlementcChildPrice']").val();
		if(isNaN(suggest) || isNaN(settlement))
			return false;
		else{
			if(parseInt(suggest)<parseInt(settlement)){ 				
				return false;
			}else
				return true;
		}
	},"直客价不能小于同行价");
	
	//修改时成人建议零售价不能小于成人同行价
	$.validator.addMethod("modspecialsuggest", function(value, element){
		
		//如果是单团产品，不需要填写直客价
		if($("#suggestAdultPriceDefine").length<0?true:false)
			return true;
		
		var suggest = value;
		var settlement = $(element).parent().parent().find("input[name='settlementSpecialPrice']").val();
		if(isNaN(suggest) || isNaN(settlement))
			return false;
		else{
			if(parseInt(suggest)<parseInt(settlement)){ 				
				return false;
			}else
				return true;
		}
	},"直客价不能小于同行价");
	
	//新增时订金不能大于同行价
	$.validator.addMethod("addpaydeposit", function(value, element){
		
		if($("#suggestAdultPriceDefine").length<0?true:false)
			return true;
		
		var paydeposit = value;
		var e = $(element).parent().parent().find("input[name='settlementAdultPrice']")[0];
		var settlement = $(e).val();
		if(isNaN(paydeposit) || isNaN(settlement))
			return false;
		else{
			if(parseInt(paydeposit)>parseInt(settlement)){ 				
				return false;
			}else
				return true;
		}
	},"订金不能大于同行价");
	
	//修改时订金不能大于同行价
	$.validator.addMethod("modpaydeposit", function(value, element){
		
		var paydeposit = value;
		var e = $(element).parent().parent().find("input[name='settlementAdultPrice']")[0];
		var settlement = $(e).val();
		if(isNaN(paydeposit) || isNaN(settlement))
			return false;
		else{
			if(parseInt(paydeposit)>parseInt(settlement)){ 				
				return false;
			}else
				return true;
		}
	},"订金不能大于同行价");
	
	//价格不能输入0
	$.validator.addMethod("nonzero", function(value,element){
		
		var result = value;
		if(isNaN(result))
			return false;
		else{
			if(parseInt(result)==0){
				return false;
			}else
				return true;
		}
	},"输入不能为0");
	
	//判断团号不能重复
	$.validator.addMethod("nosamecode", function(value,element){
		var result = value;
		var targetid = $(element).attr("id");
		var tab = $(element).parent().parent().parent();
		var tds = $(tab).children().find("td").find("input[name='groupCode']");
		var modtds = $("#modTable").find("tbody").children().find("td").find("input[name='groupCode']");
//		console.log(modtds.length);
		if(modtds.length != 0){
			for(var i=0;i<modtds.length;i++){
				tds.push(modtds[i]);
			}
		}
//		console.log(tds.length);
		var flag = false;
		if(tds.length!=1){
			$(tds).each(function(index,input){
				var val = $(input).val();
				var tmpid = $(input).attr("id");		
				if(val.toLowerCase() == result.toLowerCase() && tmpid!=targetid && result!=""){
					flag = true;
					return false;//跳出each循环
				}
					
			});
			if(flag){
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	},"团号重复");
	
	
	//校验团号不能重复-针对游轮产品
	//游轮产品新增舱型导致现有団期中的团号可重复，这里只校验新增的団期团号不能和现有的団期团号重复
	$.validator.addMethod("nosamecode4YL", function(value,element){
		var result = value;
		var targetid = $(element).attr("id");
		var sumtds = [];
		//新增団期团号input
		var tds = $("#contentTable").find("tbody").children().find("td").find("input[name='groupCode']");
		//现有団期团号input
		var modtds = $("#modTable").find("tbody").children().find("td").find("input[name='groupCode']");
		
		if(tds.length != 0){
			for(var i=0;i<tds.length;i++){
				sumtds.push(tds[i]);
			}
		}
		if(modtds.length != 0){
			for(var i=0;i<modtds.length;i++){
				sumtds.push(modtds[i]);
			}
		}
		var flag = false;
		if(sumtds.length!=0){
			$(sumtds).each(function(index,input){
				var val = $(input).val();
				var tmpid = $(input).attr("id");
				//判断当前校验的input是否为新增団期里的，若为现有団期里的不需校验
				var tableId = $(element).parents('table').attr("id");
				if(tableId!='modTable' && result.toLowerCase()==val.toLowerCase() && targetid!=tmpid && result!="" ){
					flag = true;
					return false;//跳出each循环
				}
					
			});
			if(flag){
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	},"团号重复");
	

    $.validator.addMethod("checkSameIntermodal", function(value,element){
        var flag = false;
        var intermodalNames = document.getElementsByName("intermodalGroupPart");
        for(var i = 0; i < intermodalNames.length; i++){
            var intermodalName = intermodalNames[i].value;
            if(value == intermodalName && intermodalNames[i] != element && value != ""){
                flag = true;
                break;
            }

        }
        return !flag;
    },"联运分区重复");

})(jQuery);