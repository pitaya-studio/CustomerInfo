var rebates = {
	showDetail: function(reviewId){
		window.open(contextPath + "/order/newAirticketRebate/airticketRebatesDetail?reviewId=" + reviewId);
	},
	addRebates: function(orderId, orderType){
		rebates.ajax.getRebatesStatusAjax(orderId, orderType);
	},
	saveRebates: function(){
		$("input[name='apply']").prop("disabled",true);
		//返佣对象的校验
		if($("#rebateObject").val()==1){
			if($("#rebateTarget").val()=="请选择"){
				 jBox.tip("请选择返佣对象！");
				 $("input[name='apply']").prop("disabled",false);
				 return false;
			}
			if($("#rebateTarget").val()==2){
				if($("#supplyInfo").val()=="请选择"){
					 jBox.tip("请选择供应商名称！");
					 $("input[name='apply']").prop("disabled",false);
					 return false;
				}
				if($("#accountType").val()=="请选择"){
					 jBox.tip("请选择账户类型！");
					 $("input[name='apply']").prop("disabled",false);
					 return false;
				}
				if($("#accountName").val()=="请选择"){
					 jBox.tip("请选择开户行名称！");
					 $("input[name='apply']").prop("disabled",false);
					 return false;
				}
				if($("#accountNo").val()=="请选择"){
					 jBox.tip("请选择账户号码！");
					 $("input[name='apply']").prop("disabled",false);
					 return false;
				}
			}
		}
		var rebatesList = [];
		var orderId = $("#orderId").val();
		var orderType = $("#orderType").val();
		var validFlag = true;
		$("tr[group^='travler']").each(function(index, element) {
			var costname = $(element).find("input[name='costname']").val();
			var rebatesDiff = $(element).find(".huanjia input").val();
			if(rebatesDiff != ""  && costname == ""){
				 jBox.tip("个人返佣差额大于0，请输入款项！");
				 $("input[name='apply']").prop("disabled",false);
				 validFlag = false;
				 return;
			}else if(rebatesDiff != "" && costname != ""){
				var travelerId = $(element).find("input[name='travelerId']").val();
				var currencyId = $(element).find("select[name='gaijiaCurency'] option:selected").val();
				var oldRebates = $(element).find("input[name='oldRebates']").val();
				//var remark = $(element).find("input[name='remark']").val();
				var remark = $("[name='remarks']").val();
				var Cumulative = $("[flag='leijiprice']").val();
				var newRebates = [];
				$(element).find("[name='gaijiaCurencyNew']").each(function(index,element){
					var currencyId = $(element).attr("currencyId");
					var amount = isFloat($(element).attr("data").replace(/[ ]/g,"").replace(/,/g,""));
					newRebates.push(rebates._getMoneyAmount(currencyId,amount));
				});
				rebatesList.push(rebates._getRebates(orderId, travelerId, currencyId, isFloat(rebatesDiff), oldRebates, newRebates, costname, remark, orderType,Cumulative));
			}
		});
		$(".gai-price-ol li").each(function(index, element) {
			var rebatesDiff = $(element).find("input[name='plusys']").val();
			var costname = $(element).find("input[name='costname']").val();
			if(rebatesDiff != ""   && costname == ""){
				jBox.tip("团队返佣差额大于0，请输入款项！");
				$("input[name='apply']").prop("disabled",false);
				validFlag = false;
				return;
			}else if(rebatesDiff != "" && costname != ""){
				var currencyId = $(element).find("select[name='teamCurrency'] option:selected").val();
//				var remark = $(element).find("input[name='remark']").val();
				var remark = $("[name='remarks']").val();
				var Cumulative = $("[flag='leijiprice']").val();
				var newRebates = [];
				newRebates.push(rebates._getMoneyAmount(currencyId,isFloat(rebatesDiff)));
				rebatesList.push(rebates._getRebates(orderId,"",currencyId,rebatesDiff, "", newRebates,costname,remark,orderType,Cumulative));
			};
		});
		if(validFlag && rebatesList.length > 0){
			rebates.ajax.saveRebatesAjax(rebatesList, orderId, orderType);
		}else{
			if(validFlag){
				jBox.tip("请输入返佣信息！");
				$("input[name='apply']").prop("disabled",false);
			}
		}
	},
	cancleRebates:function(rid){
		$.jBox.confirm("确定要取消申请吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				if(rid != null){
					rebates.ajax.cancleRebatesAjax(rid);
				}else{
					window.close();
				}
			}
		});
	},
	_getRebates:function(orderId,travelerId,currencyId,rebatesDiff,oldRebates,newRebates,costname,remark,orderType,Cumulative){
		var rebates = new Object();
		rebates.orderId = orderId;
		rebates.travelerId = travelerId;
		rebates.currencyId = currencyId;
		rebates.rebatesDiff = rebatesDiff;
		rebates.oldRebates = oldRebates;
		rebates.newRebates = newRebates;
		rebates.costname = costname;
		rebates.remark = remark;
		rebates.orderType =orderType;
		rebates.nowCumulative = Cumulative;
		return rebates;
	},
	_getMoneyAmount: function(currencyId,amount){
		var moneyAmount = new Object();
		moneyAmount.currencyId = currencyId;
		moneyAmount.amount = amount;
		return moneyAmount;
	},
	ajax:{
		saveRebatesAjax: function(rebatesList, orderId, orderType){
			var rebatesTotal = $("#rebatesArray").val();//累计返佣金额($1,￥2,...)
			var supplyId = $("#supplyInfo").val();//返佣供应商表(rebate_supplier)的ID
			var supplyName = $("#supplyInfo").find("option:selected").text();//返佣供应商表(rebate_supplier)的name
			var accountId = $("#accountNo").val();//plat_bank_info表id
			$.ajax( {
				type : "POST",
				url : contextPath + "/order/newAirticketRebate/airticketRebatesSave",
				data : {
					rebatesList : JSON.stringify(rebatesList),
					rebatesTotal : rebatesTotal,
					supplyId : supplyId,
					supplyName : supplyName,
					accountId : accountId
				},
				success : function(msg) {
					if(msg == "success"){
						jBox.tip("保存成功！");
						window.location.href = contextPath + "/order/newAirticketRebate/airticketRebatesList?orderId=" + orderId + "&productType=" + 7;
					}else if(msg==""){
						jBox.tip("不可以重复提交！");
						$("input[name='apply']").prop("disabled",false);
					}else{
						jBox.tip("保存失败！" + msg);
						$("input[name='apply']").prop("disabled",false);
					}
				}
			});
		},
		cancleRebatesAjax:function(rid){
			$.ajax( {
				type : "POST",
				url : contextPath + "/order/newAirticketRebate/cancleRebates",
				data : {
					rid : rid
				},
				success : function(msg) {
					if(msg == "success"){
						jBox.tip("取消成功");
						location.reload();
					}else{
						jBox.tip("取消失败");
					}
				}
			});
		},
		getRebatesStatusAjax:function(orderId, orderType){
			$.ajax( {
				type : "POST",
				async: false, 
				url : contextPath + "/order/newAirticketRebate/validRebates",
				data : {
					orderId : orderId,
					orderType : orderType
				},
				success : function(msg) {
					if(msg == "true"){
						window.open(contextPath + "/order/newAirticketRebate/airticketRebatesApply?orderId="+orderId);
					}else if(msg=="jiesuan_locked"){
						jBox.tip("结算单已锁定，不能发起申请！");
					}
				}
			});
		}
	}
};
//定义分币种金额对象
function CurrencyMoney(ObjCurrency){
	var obj = {};
	ObjCurrency.each(function(index,element){
		var currencyNameShow = $(element).text().replace(/[ ]/g,"");
		obj[currencyNameShow] = 0.00;
	});	
	return obj;
}
//验证是否是数值
function isFloat(str){
	return parseFloat(str) ? parseFloat(str) : 0.00;
}

function refundInput(obj){
	var thisvalue = $(obj).val();
	if(thisvalue==""||thisvalue=="0.00"||thisvalue=="0"){
		alert("请输入正确数字!");
		$(obj).val("");
	}
	if(thisvalue.length >15){
		alert("借款金额位数不合法!");
		$(obj).val("");
		return false;
	}
	var minusSign = false;
	if(thisvalue){
		if(/^\-/.test(thisvalue)){
			minusSign = true;
			thisvalue = thisvalue.substring(1);
		}
		thisvalue = thisvalue.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
		var txt = thisvalue.split(".");
        thisvalue = txt[0]+(txt.length>1?"."+txt[1]:"");
		if(minusSign){
			thisvalue = '-' + thisvalue;
		}
		$(obj).val(thisvalue);
	}
	if(thisvalue.length==1 && thisvalue == "-"){
		return false;
	}else{
		totallend();
	}
}
function initRebates(productType){
	$("input[name='apply']").click(function(){
		rebates.saveRebates();
	});
	//改价产品类型
	var theProductType = productType ? productType : "";
	var $optionCurrency = $("#currencyTemplate option");
	//总价--当前金额=游客改价中的当前应收列相加
	var oldSum = new CurrencyMoney($optionCurrency);
	$("span[name^='gaijiaCurencyOld']").each(function(index, element) {
		var currencyName = $(element).text().replace(/[ ]/g,"");
		oldSum[currencyName] += isFloat($(element).attr("data").replace(/[ ]/g,"").replace(/,/g,""));	
	});
	//总价--当前金额输出
	var html_oldSum = '';
	$("#currencyTemplate option").each(function(index, element) {
		var currencyNameShow = $(element).text();
		html_oldSum = sumToStr(html_oldSum,currencyNameShow,oldSum[currencyNameShow]);
	});
	if(html_oldSum == ''){
		html_oldSum = '<font class="f14" flag="bz" value="0">人民币</font><span class="f20">0</span>';
	}
	$("#totalBefore").html(html_oldSum);
	
	//总价--差额、改后初始化
	calculateSum(1);
	
	//增加团队改价
	$('.gai-price-btn').click(function() {
		var html = '<li><i><input type="text" name="costname" value="机票返佣" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">其他款项</span></i>&nbsp;';
		html += '<i><select class="selectrefund" name="teamCurrency" onchange="totalSelect();">{0}<select></i>&nbsp;'.replace("{0}",$("#currencyTemplate").html());
		html += '<i><input type="text" name="plusys" class="gai-price-ipt1" flag="istips" onkeyup="refundInput(this)" onafterpast="refundInput(this)" /><span class="ipt-tips ipt-tips2">费用</span></i>&nbsp;';
//		html += '<i><input type="hidden" name="remark" class="gai-price-ipt2" flag="istips" /><span class="ipt-tips ipt-tips2">备注</span></i>&nbsp;';
		html += '<i><a class="ydbz_s gray clear-btn">删除</a></i>';
		$(this).parents('.gai-price-ol').append(html);
		$(this).parents('.gai-price-ol').find("li:last").find("select[name='teamCurrency'] option:first").prop("selected",true);
		//新增改价项目中，input获得失去焦点提示信息显示隐藏
		inputTipsDynamic($(this).parents('.gai-price-ol').find("li:last"));
	});
    
	//团队改价操作
	$('.gai-price-ol').on("click",".clear-btn",function(){//删除团队改价一项
		$(this).parents('li').remove();
		calculateSum(0);
	}).on("focusout","input[name='plusys']",function(){//修改款项金额
		//验证是否是数字
		validNumFinally(this);
		$(this).next(".ipt-tips2").hide();
		newNumTrTotal();
		calculateSum(1);
	}).on("change","select[name='teamCurrency']",function(){//修改款项币种
		newNumTrTotal();
		calculateSum(1);
	});
	
	//操作显示隐藏
	$('.modifyPrice-table').on("mouseover",".huanjia",function(){
		//显示操作按钮
		$(this).addClass("huanjia-hover").find('dt input').attr('defaultValue');
		$(this).find('dd').show();
	}).on("mouseout",".huanjia",function(){
		//隐藏操作按钮
		$(this).removeClass("huanjia-hover").find('dd').hide();
	}).on("change","select[name='gaijiaCurency']",function(){
		//重新计算改后金额
		newNumTrTotal();
		//重新计算总价
		calculateSum(1);
	}).on("focusout","input[name=plusys],input[name=plusdj]",function(){
		//验证是否是数字
		validNumFinally(this);
		//重新计算改后金额
		newNumTrTotal();
		//重新计算总价
		if(($(this).attr("name") == "plusys") || ("visa" == theProductType)){
			calculateSum(1);
		}
	}).on("click","[flag=appAll]",function(){
		//对应的input框的name值
		var inputName = $(this).parents("td").find("input").attr("name");
		//应用全部
		var inputValue = $(this).parents("td").find("input[name='" + inputName + "']").val();
		var $thisCurrency = $(this).parents("tr").find("[name='gaijiaCurency']");
		var thisCurrencyName = $thisCurrency[0].tagName=="SPAN" ? $thisCurrency.text() : $thisCurrency.find("option:selected").text();
		var groupName = $(this).parents("tr").attr("group");
		//获取游客的姓名数组
		var array_groupName = new Array();
		$(".modifyPrice-table tr[group]").each(function(index, element) {
            if($.inArray($(element).attr("group"),array_groupName) < 0){
				array_groupName.push($(element).attr("group"));
			}else{
				return;
			}
        });
		$.each(array_groupName,function(index, element){
			if(element != groupName){
				var $thisTr = $(".modifyPrice-table tr[group='" + element +"']");
				$thisTr.each(function(){
					$(this).find("input[name='" + inputName +"']").val(inputValue).trigger("focusout");
					return;
				})
			}
		});
	}).on("click","[flag=reset]",function(){
		//还原
		var $inputLink = $(this).parents(".huanjia").find("input");
		$inputLink.val($inputLink.attr("defaultvalue")).trigger("focusout");
	});
	
	//全部还原
	$(".re-storeall").click(function(){
		$(".huanjia input").each(function(index, element) {
            $(element).val($(element).attr("defaultvalue")).trigger("focusout");
        });
	});
	

	//总价html拼接
	function sumToStr(str,currencyName,thenumber){
		var html = str;
		if(thenumber && thenumber != 0){
			var theMoney = milliFormat(thenumber,1);
			var sign = ' <span class="tdgreen">+</span> ';
			var isNegative = /^\-/.test(theMoney);
			if(isNegative){
				sign = ' <span class="tdred fbold">-</span> ';
				theMoney = theMoney.replace(/^\-/g,'');
			}
			if(html != '' || (html == '' && isNegative)){
				html += sign;
			}
			html += '<font class="f14">' + currencyName + '</font><span class="f20">' + theMoney + '</span>';
		}
		return html;
	}

	
	//游客改价差额计算
	function calculateSum(calculateType){
		/*calculateType计算类型说明 0:只计算团队改价；1:计算游客改价和团队改价*/
		var thiscalculateType = ("0" == calculateType) ? calculateType : 1;
		//游客改价差额
		var plusSum_traveler = new CurrencyMoney($optionCurrency);
		//团队改价差额
		var plusSum_team = new CurrencyMoney($optionCurrency);
		//团队签证改价差额
		var plusSum_visa = new CurrencyMoney($optionCurrency);
		//改价差额=游客改价差额 + 团队改价差额( + 团队签证改价--签证)
		var plusSum = new CurrencyMoney($optionCurrency);
		//改后总额
		var newSum = new CurrencyMoney($optionCurrency);
		//游客改价金额统计的html
		var sumHTML_traveler = '';
		//改价差额html
		var html_plusSum = '';
		//改后总额html
		var html_newSum = '';
		//计算团队改价
		$(".gai-price-ol li").each(function(index, element) {
			var currencyName = $(element).find("select[name='teamCurrency'] option:selected").text();
			plusSum_team[currencyName] += isFloat($(element).find("input[name='plusys']").val());
		});
		//通过html取得游客改价差额
		if("0" == thiscalculateType){
			$("#totalTravelerPlus [flag='bz']").each(function(index, element) {
				var money = isFloat($(element).next().text().replace(/[ ]/g,"").replace(/,/g,""));
				var currencyName = $(element).text();
				plusSum_traveler[currencyName] = money;
			});
		}
		//计算游客改价
		if("1" == thiscalculateType){
			$("tr[group^='travler']").each(function(index, element) {
				var currencyName = $(element).find("span[name='gaijiaCurency']").length ? $(element).find("span[name='gaijiaCurency']").text() : $(element).find("select[name='gaijiaCurency'] option:selected").text();
				plusSum_traveler[currencyName] += isFloat($(element).find("[name='plusys']").val());
			});
		}
	
		//计算总额
		for(var p in newSum){
			//游客改价差额计算
			if("1" == thiscalculateType){
				if(0 != plusSum_traveler[p]){
					var theMoney = milliFormat(plusSum_traveler[p],1);
					var sign = ' <span class="tdgreen">+</span> ';
					var isNegative = /^\-/.test(theMoney);
					if(isNegative){
						sign = ' <span class="tdred fbold">-</span> ';
						theMoney = theMoney.replace(/^\-/g,'');
					}
					if(sumHTML_traveler != '' || (sumHTML_traveler == '' && isNegative)){
						sumHTML_traveler += sign;
					}
					sumHTML_traveler += '<font class="gray14" flag="bz">' + p + '</font><span class="f20 tdred">' + theMoney + '</span>';
				}
			}
			//改价差额计算=游客改价差额 + 团队改价差额
			plusSum[p] = plusSum_traveler[p] + plusSum_team[p];

			//改后总额计算
			newSum[p] = plusSum[p] + oldSum[p];
			//改价差额html
			html_plusSum = sumToStr(html_plusSum,p,plusSum[p]);
			//改后总额html
			html_newSum = sumToStr(html_newSum,p,newSum[p]);
			
			
			
		}
		
		//游客改价差额输出
		if("1" == thiscalculateType){
			$("#totalTravelerPlus").html(sumHTML_traveler);
		}
		//改价差额输出
		$("#totalPlus").html(html_plusSum);
		//改后总额输出
		$("#totalAfter").html(html_newSum);
	}
	//每行游客返佣价
	function newNumTrTotal(){
		$("tr[group^='travler']").each(function(index, element) {
			var thisInput = $(element).find(".huanjia input");
			var newSumName = thisInput.attr("name").replace("plus","after");
			var oldSumName = thisInput.attr("name").replace("plus","before");
			var oldMoney = new CurrencyMoney($optionCurrency);
			var $olds = $(element).find("[name='gaijiaCurencyOld']").each(function(index,element){
				oldMoney[$(element).text()] = isFloat($(element).attr("data").replace(/[ ]/g,"").replace(/,/g,""));;
			});
			var moneyName = $(element).find("select[name='gaijiaCurency'] option:selected").text();
			var rebatesDiff = thisInput.val();
			if(rebatesDiff==null||rebatesDiff==""){
				rebatesDiff = 0;
			}
			oldMoney[moneyName] += parseFloat(rebatesDiff);
			var newStr = "";
			for(var p in oldMoney){
				if(oldMoney[p] != 0){
					var theMoney = milliFormat(oldMoney[p],1);
					var sign = '';
					var isNegative = /^\-/.test(theMoney);
					if(isNegative){
						sign = '-';
						theMoney = theMoney.replace(/^\-/g,'');
					}
					if(newStr != '' || (newStr == '' && isNegative)){
						newStr += sign;
					}
					var currencyId = "";
					$("#currencyTemplate option").each(function(index, element) {
						var currencyNameShow = $(element).text();
						if(p==currencyNameShow){
							currencyId=$(element).val();
						}
					});
					newStr += '<span name="gaijiaCurencyNew" data="'+sign+theMoney+'" currencyId="'+currencyId+'">'+ p +'</span>'+ theMoney +'<br/>';
					
				}
			}
			$(element).find("[flag='" + newSumName + "']").html(newStr);
		});
	}
}


currencyObj ={};

function loadCurrency(){
	   $.ajax({
			  type: "GET",
			  url: contextPath + "/order/refund/currencyJson",
			  dataType: "json",
			  async: false,
			  success:function(msg){
				  if(msg && msg.currencyList){
				      var options = [];
				      var sel = "";
				      $.each(msg.currencyList, function(i,n){
				    	 currencyObj[n.id] = n;
				         sel = "";
				         options.push("<option " + sel + "   currencyName ="+n.currencyName+"   currencyExchangerate="+n.convertLowest+"  currencyMark ="+n.currencyMark+"  value='" + n.id + "'>" + n.currencyName + "</option>");
				      });
				      
				      $("#currencyTemplate, select.selectrefund").append(options.join(""));
				      
				      var reviewId = $("#reviewId").val();
				      if(reviewId != ""){
				   	   $("select.selectrefund").val($("#reviewId").attr("data-type"));
				      }
				  }
			  }
			});
	}

function totalSelect(){
	totallend();
}

function totallend(){
	 var selects=$("select[class='selectrefund']");
	 var inputs=$("input[name='plusys']");
	 var currencyCalObj = {};
	 for(u=0;u<selects.length;u++){
	      var money_sel = selects.eq(u).val();
		  var money_input = selects.eq(u).parent().parent().find("input[name='plusys']");
		  $(money_input).attr("data-type",money_sel);
	    }
	 inputs.each(function(index, element) {
		  var money_text=$(this).val();
		  if(money_text==""){
			  money_text=0;
		  }else{
			  money_text=parseFloat(money_text).toFixed(2);
	      }
		  var datatype =$(this).attr("data-type");
		  var currObj = currencyCalObj[datatype];
		  //存在对象
		  if(currObj){
			  money_text  = (Number(money_text) + Number(currObj)).toFixed(2);
		  }
		  currencyCalObj[datatype] = money_text;
	 });
	var showArray = [];
	var leijiArray = [];
	var rebatesArray = [];//累计返佣金额($1,￥2,...)
	var cObj = null;
	for(var key in currencyCalObj){
		cObj = currencyObj[key];
		if(currencyCalObj[key].toString()==0){
//			showArray.push("<font class='gray14'>"+""+"</font><span class='tdred'>"+""+"</span>");
		}else{
			showArray.push("<font class='gray14'>"+cObj.currencyName+"</font><span class='tdred'>"+milliFormat(currencyCalObj[key].toString(),1)+"</span>");
			leijiArray.push(cObj.id+":"+milliFormat(currencyCalObj[key].toString(),1));
			rebatesArray.push(cObj.currencyMark + " " + milliFormat(currencyCalObj[key].toString(),1));
		}
	}
	$('.all-money').find('span').html(showArray.join("+"));
	$("[flag=leijiprice]").val(leijiArray.join("+"));
	$("#rebatesArray").val(rebatesArray.join("+"));
//	alert($("[class=all-money]").html());
}
