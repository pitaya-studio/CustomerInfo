$(function(){

	//treplayEPrice.service.trafficLinesShow();
	
	$("input[name=adultPrice]").focus(function(){
		$("input[name=adultPrice]").val("");
	});
	$("input[name=childPrice]").focus(function(){
		$("input[name=childPrice]").val("");
	});
	$("input[name=specialPersonPrice]").focus(function(){
		$("input[name=specialPersonPrice]").val("");
	});
	
	$("input[name=adultPrice]").blur(function(){
		var price = $("input[name=adultPrice]").val();
		if(isNaN(price)){
			$("input[name=adultPrice]").val("");
			$("input[name=adultPrice]").val(0);
		}
		treplayEPrice.service.countPrice();
	});
	$("input[name=childPrice]").blur(function(){
		var price1 = $("input[name=childPrice]").val();
		if(isNaN(price1)){
			$("input[name=childPrice]").val("");
			$("input[name=childPrice]").val(0);
		}
		treplayEPrice.service.countPrice();
	});
	$("input[name=specialPersonPrice]").blur(function(){
		var price2 = $("input[name=specialPersonPrice]").val();
		if(isNaN(price2)){
			$("input[name=specialPersonPrice]").val("");
			$("input[name=specialPersonPrice]").val(0);
		}
		treplayEPrice.service.countPrice();
	});
	
	// 绑定提交回复报价
	$("#return_price").click(function(){
		 var adultNum = $("#replay-price-form-id input[name=adultSum]").val();
		 var childNum = $("#replay-price-form-id input[name=childSum]").val();
		 var specialNum = $("#replay-price-form-id input[name=specialSum]").val();
		 var errorMsg = "";
	 
		 var boolAll = true;
		 if(parseInt(adultNum)>0){
			 $("ul.adult-dt-price li:visible select[name=adultCurrency]  option:selected").each(function(){
				 var currcy = parseFloat($(this).val());
				 if(currcy<=0 || isNaN(currcy)){
					 boolAll = false;
					 errorMsg = "请选择成人单价币种";
				 }
			 });
			 $("ul.adult-dt-price li:visible input[name=adultAmount]").each(function(){
				 var amount = parseFloat($(this).val());
				 if(amount<=0 || isNaN(amount)){
					 boolAll = false;
					 errorMsg = "请填写成人单价";
				 }
			 });
		 }
		 if(parseInt(childNum)>0){
			 $("ul.child-dt-price li:visible select[name=childCurrency]  option:selected").each(function(){
				 var currcy = parseFloat($(this).val());
				 if(currcy<=0 || isNaN(currcy)){
					 boolAll = false;
					 errorMsg = "请选择儿童单价币种";
				 }
			 });
			 $("ul.child-dt-price li:visible input[name=childAmount]").each(function(){
				 var amount = parseFloat($(this).val());
				 if(amount<=0 || isNaN(amount)){
					 boolAll = false;
					 errorMsg = "请填写儿童单价";
				 }
			 });
		 }
		 if(parseInt(specialNum)>0){
			 $("ul.special-dt-price li:visible select[name=specialCurrency]  option:selected").each(function(){
				 var currcy = parseFloat($(this).val());
				 if(currcy<=0 || isNaN(currcy)){
					 boolAll = false;
					 errorMsg = "请选择特殊人群单价币种";
				 }
			 });
			 $("ul.special-dt-price li:visible input[name=specialAmount]").each(function(){
				 var amount = parseFloat($(this).val());
				 if(amount<=0 || isNaN(amount)){
					 boolAll = false;
					 errorMsg = "请填写特殊人群单价";
				 }
			 });
		 }
		 if(boolAll){
			$.jBox.confirm("提交报价，您确定吗？","提示",function(v, h, f){
				 if(v=="ok"){
					 //treplayEPrice.ajax.replyPriceAjax();
					 $("#replay-price-form-id").submit();
				 }else{
					 $.jBox.tip("提交报价取消");
				 }
			 });
		 }else{
			 $.jBox.tip(errorMsg);
		 }
	});
	
	$("[name=history-back]").click(function(){
	    location.href=contextPath+"/eprice/manager/project/erecordtrafficlist";
	});
	
	// 票务计调备注处理(失去焦点)
	$("textarea[name=remark]").focus(function(){
		treplayEPrice.service.replyRemarkAreaTextClean();
	});
	// 票务计调备注处理(得到焦点)
	$("textarea[name=remark]").blur(function(){
		treplayEPrice.service.replyRemarkAreaText();
	});
	$("textarea[name=remark]").val("请输入备注详情，不超过500字");
	/**
     * 20150511新增 begin
     *  */
	/**
     * 新增汇率不为0验证，并捎带总值计算 20150511
     */
    $("input.exchangerate").live("blur",function(){
    	  var num = $(this).val();
	  	  var reg = new RegExp(/^\d+\.?\d{0,3}$/);
	  	  if(!reg.test(num)){
	  		  if(parseFloat(num)>0){
	      		  $.jBox.tip("汇率值最多保留三位小数","提示");
	  			  $(this).val(Math.round(parseFloat(num)*1000)/1000);
	  		  }else{
	      		  $.jBox.tip("输入汇率值有误","提示");
	      		  $(this).val("1.000");
	  		  }
	  	  }
	  	  // 调用总值计算
		  allEprice();
    });
    // 成人整体报价
    $("#price-detaill-dl-all-id ul.adult-dt-price input[name=addNewPrice]").live("click",function(){
   	 var number = $("input[name=adultSum]").val();
   	 if(parseInt(number)>0){
       	 var copy= $("li.adult-dt-price:first").clone();
   		 $("ul.adult-dt-price").append(copy);
   		 $("ul.adult-dt-price li.adult-dt-price:last").css("display","");
   		 $("ul.adult-dt-price label.adultTitle:last").html("");
   		 $("ul.adult-dt-price label.adultTitle:last").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
   		 $("ul.adult-dt-price input[name=addNewPrice]:last").remove();
   		 $("ul.adult-dt-price input[name=adultAmount]:last").after('&nbsp;&nbsp;<input type="button" name="delNewPrice" class="btn btn-primary gray mar_xjia_del" value="删除"/>');
   	 }else{
   		 jBox.tip("当前人数为0","info");
   	 }
    });
    // 儿童整体报价
    $("#price-detaill-dl-all-id ul.child-dt-price input[name=addNewPrice]").live("click",function(){ 
   	 var number = $("input[name=childSum]").val();
   	 if(parseInt(number)>0){
       	 var copy= $("li.child-dt-price:first").clone();
   		 $("ul.child-dt-price").append(copy);
   		 $("ul.child-dt-price li.child-dt-price:last").css("display","");
   		 $("ul.child-dt-price label.childTitle:last").html("");
   		 $("ul.child-dt-price label.childTitle:last").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
   		 
   		 $("ul.child-dt-price input[name=addNewPrice]:last").remove();
   		 $("ul.child-dt-price input[name=childAmount]:last").after('&nbsp;&nbsp;<input type="button" name="delNewPrice" class="btn btn-primary gray mar_xjia_del" value="删除"/>');
   	 }else{
   		 jBox.tip("当前人数为0","info");
   	 }
    });
    // 特殊人群整体报价
    $("#price-detaill-dl-all-id ul.special-dt-price input[name=addNewPrice]").live("click",function(){
   	 var number = $("input[name=specialSum]").val();
   	 if(parseInt(number)>0){
       	 var copy= $("li.special-dt-price:first").clone();
   		 $("ul.special-dt-price").append(copy);
   		 $("ul.special-dt-price li.special-dt-price:last").css("display","");
   		 $("ul.special-dt-price label.specialTitle:last").html("");
   		 $("ul.special-dt-price label.specialTitle:last").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
   		 
   		 $("ul.special-dt-price input[name=addNewPrice]:last").remove();
   		 $("ul.special-dt-price input[name=specialAmount]:last").after('&nbsp;&nbsp;<input type="button" name="delNewPrice" class="btn btn-primary gray mar_xjia_del" value="删除"/>');
   	 }else{
   		 jBox.tip("当前人数为0","info");
   	 }
    });
    // 删除成人新增整体报价
    $("#price-detaill-dl-all-id ul.adult-dt-price input[name=delNewPrice]").live("click",function(){
   	 $(this).parents("li.adult-dt-price").remove();
   	 allEprice();
    });
    // 删除儿童新增整体报价
    $("#price-detaill-dl-all-id ul.child-dt-price input[name=delNewPrice]").live("click",function(){
   	 $(this).parents("li.child-dt-price").remove();
   	 allEprice();
    });
    // 删除特殊人群新增整体报价
    $("#price-detaill-dl-all-id ul.special-dt-price input[name=delNewPrice]").live("click",function(){
   	 $(this).parents("li.special-dt-price").remove();
   	 allEprice();
    });
 // 整体报价 切换成人币种
    $("#price-detaill-dl-all-id select[name=adultCurrency]").live("change",function(){
   	 var val = $(this).val(); // 币种汇率
   	 var id = $("option:selected", this).attr("id"); // 币种ID
   	 var mark = $("option:selected", this).attr("title"); // 币种标识
   	 var name = $("option:selected", this).text(); // 币种名称
   	 $(this).nextAll("input[name=adultExchangerate]").val(val);
   	 $(this).nextAll("input[name=adultCurrencyId]").val(id);
   	 $(this).nextAll("input[name=adultCurrencyName]").val(name);
   	 $(this).nextAll("span.adultCurrencyMark").text("");
   	 $(this).nextAll("span.adultCurrencyMark").text(mark);
	 allEprice();
    });
    // 整体报价 切换儿童币种
    $("#price-detaill-dl-all-id select[name=childCurrency]").live("change",function(){
   	 var val = $(this).val(); // 币种汇率
   	 var id = $("option:selected", this).attr("id"); // 币种ID
   	 var mark = $("option:selected", this).attr("title"); // 币种标识
   	 var name = $("option:selected", this).text(); // 币种名称
   	 $(this).nextAll("input[name=childExchangerate]").val(val);
   	 $(this).nextAll("input[name=childCurrencyId]").val(id);
   	 $(this).nextAll("input[name=childCurrencyName]").val(name);
   	 $(this).nextAll("span.childCurrencyMark").text("");
   	 $(this).nextAll("span.childCurrencyMark").text(mark);
	 allEprice();
    });
    // 整体报价 切换特殊人群币种
    $("#price-detaill-dl-all-id select[name=specialCurrency]").live("change",function(){
   	 var val = $(this).val(); // 币种汇率
   	 var id = $("option:selected", this).attr("id"); // 币种ID
   	 var mark = $("option:selected", this).attr("title"); // 币种标识
   	 var name = $("option:selected", this).text(); // 币种名称
   	 $(this).nextAll("input[name=specialExchangerate]").val(val);
   	 $(this).nextAll("input[name=specialCurrencyId]").val(id);
   	 $(this).nextAll("input[name=specialCurrencyName]").val(name);
   	 $(this).nextAll("span.specialCurrencyMark").text("");
   	 $(this).nextAll("span.specialCurrencyMark").text(mark);
    });
    // 整体报价总计
    $("#price-detaill-dl-all-id input.allNum").live("blur",function(){
    	if(parseFloat($(this).val())>0){
   		 var num = Math.round($(this).val()*100)/100; // 保留两位小数
   		 $(this).val(num);
       	 allEprice();
   	 }else{
   		 $(this).val(0);
   		 $.jBox.tip("输入数值不合法","提示");
   	 }
    });
    /**
     * 20150511新增 end
     *  */
});
/** 20150511新增 begin*/
//整体报价总计
//整体报价总计
function  allEprice(){
	// 计算成人总价
	var adultMap ={}; // key 结构：币种ID_币种名称_币种符号_币种汇率
	// 合计多币种成人价格
	$("#price-detaill-dl-all-id ul.adult-dt-price  input[name=adultAmount]").each(function(){
		 var id = $(this).prev().prev().val();
		 var name = $(this).prev().val();
		 var mark = $(this).prev().prev().prev().text();
		 var currency = $(this).parents("li.adult-dt-price").find("input[name=adultExchangerate]").val(); // 币种汇率
		 if(parseInt($(this).val())>0){
			 if(adultMap[id+"_"+name+"_"+mark+"_"+currency]){ // 如果数组中已经存在，则累加
//				 var num = parseInt(adultMap[id+"_"+name+"_"+mark+"_"+currency])+parseInt($(this).val());
//				 adultMap[id+"_"+name+"_"+mark+"_"+currency] = num;
				 $.jBox.tip("出现汇率相同的重复币种！","警告");
				 $(this).val(0);
			 }else{ // 如果数组中不存在，则放入
				 var num =parseFloat($(this).val());
				 adultMap[id+"_"+name+"_"+mark+"_"+currency] = num;
			 }
//			 var num = parseInt(parseInt($(this).val()));
//			 adultMap[id+"_"+name+"_"+mark+"_"+currency] = num;
		 }
	});
	// 计算乘以成人人数后的成人价格
	var adultNum = $("input[name=adultSum]").val(); // 成人数量
	if(adultMap && adultNum!=0){
		for(var k in adultMap){
			//console.log("成人原值统计："+k+"--"+adultMap[k]);
			adultMap[k] = Math.round(parseFloat(adultMap[k])*adultNum*100)/100;
			//console.log("成人乘以人数统计："+k+"--"+adultMap[k]);
		};
	}
	
	// 计算儿童总价
	var childMap = {};// key 结构：币种ID_币种名称_币种符号_币种汇率
	$("#price-detaill-dl-all-id ul.child-dt-price  input[name=childAmount]").each(function(){
		var id = $(this).prev().prev().val();
		 var name = $(this).prev().val();
		 var mark = $(this).prev().prev().prev().text();
		 var currency = $(this).parents("li.child-dt-price").find("input[name=childExchangerate]").val(); // 币种汇率
		 if(parseInt($(this).val())>0){
			 if(childMap[id+"_"+name+"_"+mark+"_"+currency]){ // 如果数组中已经存在，则累加
//				 var num = parseInt(childMap[id+"_"+name+"_"+mark+"_"+currency])+parseInt($(this).val());
//				 childMap[id+"_"+name+"_"+mark+"_"+currency] = num;
				 $.jBox.tip("出现汇率相同的重复币种！","警告");
				 $(this).val(0);
			 }else{ // 如果数组中不存在，则放入
				 var num =parseFloat($(this).val());
				 childMap[id+"_"+name+"_"+mark+"_"+currency] = num;
			 }
//			 var num = parseInt(parseInt($(this).val()));
//			 childMap[id+"_"+name+"_"+mark+"_"+currency] = num;
		 }
	});
	// 计算乘以儿童人数后的价格
	var childNum = $("input[name=childSum]").val(); // 成人数量
	if(childMap && childNum!=0){
		for(var k in childMap){
			//console.log("儿童原值统计："+k+"--"+childMap[k]);
			childMap[k] = Math.round(parseFloat(childMap[k])*childNum*100)/100;
			//console.log("儿童乘以人数统计："+k+"--"+childMap[k]); 
		};
	}
	
	// 计算特殊人群总价
	var specialMap = {};// key 结构：币种ID_币种名称_币种符号_币种汇率
	$("#price-detaill-dl-all-id ul.special-dt-price  input[name=specialAmount]").each(function(){
		var id = $(this).prev().prev().val();
		 var name = $(this).prev().val();
		 var mark = $(this).prev().prev().prev().text();
		 var currency = $(this).parents("li.special-dt-price").find("input[name=specialExchangerate]").val(); // 币种汇率
		 if(parseInt($(this).val())>0){
			 if(specialMap[id+"_"+name+"_"+mark+"_"+currency]){ // 如果数组中已经存在，则累加
//				 var num = parseInt(specialMap[id+"_"+name+"_"+mark+"_"+currency])+parseInt($(this).val());
//				 specialMap[id+"_"+name+"_"+mark+"_"+currency] = num;
				 $.jBox.tip("出现汇率相同的重复币种！","警告");
				 $(this).val(0);
			 }else{ // 如果数组中不存在，则放入
				 var num = parseFloat($(this).val());
				 specialMap[id+"_"+name+"_"+mark+"_"+currency] = num;
			 }
//			 var num = parseInt(parseInt($(this).val()));
//			 specialMap[id+"_"+name+"_"+mark+"_"+currency] = num;
		 }
	});
	// 计算乘以特殊人群人数后的价格
	var specialNum = $("input[name=specialSum]").val(); // 特殊人群数量
	if(specialMap && specialNum!=0){
		for(var k in specialMap){
			//console.log("特殊人群原值统计："+k+"--"+specialMap[k]);
			specialMap[k] = Math.round(parseFloat(specialMap[k])*specialNum*100)/100;
			//console.log("特殊人群乘以人数统计："+k+"--"+specialMap[k]);
		};
	}
	
	
	
	/* 展示在“整体报价”总计栏中(合计计算)
	 // 总计成人、儿童、特殊人群的总价
	var allMap = {}; // 全部总价
	// 加入成人总价
	if(adultMap && adultNum!=0){ 
		for(var k in adultMap){
			if(allMap[k]!=null){
				allMap[k] = parseInt(allMap[k])+parseInt(adultMap[k]);
			}else{
				allMap[k] = parseInt(adultMap[k]);
			}
		}
	}
	// 加入儿童总价
	if(childMap && childNum!=0){ 
		for(var k in childMap){
			if(allMap[k]!=null){
				allMap[k] = parseInt(allMap[k])+parseInt(childMap[k]);
			}else{
				allMap[k] = parseInt(childMap[k]);
			}
		}
	}
	// 加入特殊人群总价
	if(specialMap && specialNum!=0){ 
		for(var k in specialMap){
			if(allMap[k]!=null){
				allMap[k] = parseInt(allMap[k])+parseInt(specialMap[k]);
			}else{
				allMap[k] = parseInt(specialMap[k]);
			}
		}
	}
	var strall = "总计：<em class='red20'>0.00</em>";
	if(allMap){
		strall = "总计：";
		for(var k in allMap){
			if(parseInt(allMap[k])!=NaN){
				var ss = k.split("_"); //key 结构：币种ID_币种名称_币种符号_币种汇率
				strall += "<em class='gray14'>"+ss[2]+"</em> <em class='red20'>"+allMap[k]+".00</em> <em class='gray14'>"+ss[1]+"</em>  ";
			}
		}
	}*/
	/* 展示在“整体报价”总计栏中（分类计算） */
	var strall = "";
	if(adultMap && parseInt(adultNum)>0){ // 成人总价
		strall += "合计成人报价，其中：<em class='blur20'>"+adultNum+"</em>人共 ";
		for(var k in adultMap){
			if(parseInt(adultMap[k])!=NaN){ //key 结构：币种ID_币种名称_币种符号_币种汇率
				var part = k.split("_");
				strall += "<em class='gray14'>"+part[2]+"</em> <em class='red20'>"+adultMap[k]+"</em> ； ";
			}
		}
		strall +="<br/>";
	}
	if(childMap && parseInt(childNum)>0){ // 儿童总价
		strall += "合计儿童报价，其中：<em class='blur20'>"+childNum+"</em>人共 ";
		for(var k in childMap){
			if(parseInt(childMap[k])!=NaN){
				var part = k.split("_");
				strall += " <em class='gray14'>"+part[2]+"</em> <em class='red20'>"+childMap[k]+"</em> ； ";
			}
		}
		strall +="<br/>";
	}
	if(specialMap && parseInt(specialNum)>0){ // 特殊人群总价
		strall += "合计特殊人群报价，其中：<em class='blur20'>"+specialNum+"</em>人共  ";
		for(var k in specialMap){
			if(parseInt(specialMap[k])!=NaN){
				var part = k.split("_");
				strall += "<em class='gray14'>"+part[2]+"</em> <em class='red20'>"+specialMap[k]+"</em> ； ";
			}
		}
		strall +="<br/>";
	}
	//console.log("总计："+strall);
	$("#price-detaill-dl-all-id p.strAll").html(strall);
}
/** 20150511新增 end*/
var treplayEPrice = {
		nolist : [["一","二","三","四","五","六","七","八","九","十"],["","","十","百","千"]],
		
		service : {
			/**
			 * 展示询价交通路线中的一些类型信息，如：出发时刻、舱位等级、舱位、段号
			 * @author lihua.xu
			 * @时间 2014年10月8日
			 */
			trafficLinesShow : function(){
				$("#epirce-traffic-line-list-id div[name=eprice-traffic-line-name]").each(function(){
					var $d = $(this).find("p[name=startTimeType]");
					var v = $d.attr("vtype");
					if(v==1){
						$d.html("早");
					}else if(v==2){
						$d.html("中");
					}else{
						$d.html("晚");
					}
					
					$d = $(this).find("p[name=aircraftSpaceLevel]");
					v = $d.attr("vtype");
					if(v==1){
						$d.html("头等舱");
					}else if(v==2){
						$d.html("公务舱");
					}else if(v==3){
						$d.html("经济舱");
					}else{
						$d.html("不限");
					}
					
					$d = $(this).find("p[name=aircraftSpace]");
					v = $d.attr("vtype");
					if(v==0){
						$d.html("不限");
					}
					
					var i = $(this).attr("nov");
					var d;
					if(i<=10){
						d = treplayEPrice.nolist[0][i-1];
					}else if(10<i && i<100){
						d = treplayEPrice.nolist[0][i/10]+treplayEPrice.nolist[0][9]+treplayEPrice.nolist[0][i%10];
					}else{
						d = i;
					}
					$(this).find("div.title_samil").html("第"+d+"段:");
					
				});
			},
			// 计算利润、成本价
			countPrice : function(){
				var adultPrice = $("input[name=adultPrice]").val();
				var childPrice = $("input[name=childPrice]").val();
				var specialPersonPrice = $("input[name=specialPersonPrice]").val();
				var adultSum = $("#adult_sum").text();
				var childSum = $("#child_sum").text();
				var specialSum = $("#special_sum").text();
				
				var all_count = 0;
				
				all_count += (isNaN(adultPrice)==false?Number(adultPrice):0)*(isNaN(adultSum)==false?Number(adultSum):0);
				all_count += (isNaN(childPrice)==false?Number(childPrice):0)*(isNaN(childSum)==false?Number(childSum):0);
				all_count += (isNaN(specialPersonPrice)==false?Number(specialPersonPrice):0)*(isNaN(specialSum)==false?Number(specialSum):0);
				all_count = all_count.toFixed(2);
				$("#all_count").empty();
				$("#all_count").text(all_count);
				$("#operatorTotalPrice").empty();
				$("#operatorTotalPrice").val(all_count);
			},
			
			// 生成提交报价信息所用的参数字符串
			replyPriceFormSerialize : function(){
				var param = "adultPrice="+$("input[name=adultPrice]").val();
				param += "&childPrice="+$("input[name=childPrice]").val();
				param += "&specialPersonPrice="+$("input[name=specialPersonPrice]").val();
				param += "&operatorTotalPrice="+$("#operatorTotalPrice").val();
				param += "&pid="+$("input[name=pid]").val();
				param += "&rid="+$("input[name=rid]").val();
				param += "&rpid="+$("input[name=rpid]").val();
				param += "&adultSum="+$("input[name=adultSum]").val();
				param += "&childSum="+$("input[name=childSum]").val();
				param += "&specialSum="+$("input[name=specialSum]").val();
				var remark = $("textarea[name=remark]").val();
				if(remark!="请输入备注详情，不超过500字"){
					param += "&remark="+remark;
				}else{
					param += "&remark=null";
				}
				
				return param;
			},
			
			// 计调回复备注说明文字
			replyRemarkAreaText: function(){
				var str = $("textarea[name=remark]").val();
				if(str==""){
					str = "请输入备注详情，不超过500字";
				}
				$("textarea[name=remark]").val(str);
			},
			// 点击清空备注说明文字
			replyRemarkAreaTextClean: function(){
				var str = $("textarea[name=remark]").val();
				if(str == "请输入备注详情，不超过500字"){
					$("textarea[name=remark]").val("");
				}
			}
		},
		
		ajax : {
			// 提交报价信息
			replyPriceAjax : function(){
				var the_param =treplayEPrice.service.replyPriceFormSerialize() ;
				$.ajax({
					type : "POST",
					url : contextPath+"/eprice/manager/ajax/project/replytrafficprice",
					data : the_param,
					dataType : "text",
					success : function(html){
						var json;
						try{
							json = $.parseJSON(html);
						}catch(e){
							json = {res:"error"};			
						}
						if(json.res=="success"){
							jBox.tip('提交成功','info');
							location.href=contextPath+"/eprice/manager/project/erecordtrafficlist";
						}else if(json.res=="data_error"){
							jBox.tip(json.mes,'info');
						}else{
							jBox.tip("系统错误", 'error');
						}
					}
 				});
			}
		}
};