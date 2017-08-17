// JavaScript Document

function renderSelects(callback){
	var $sels = $('select[url]');
	renderSelect($sels,0,callback);
}
function renderSelect($sels,i,callback){
	var $select =  $sels.eq(i);
	var promise = $.ajax({
		url:$select.attr('url'),
		type:"post"
	});
	promise.done(function(result){
		//TODO 判断是否加载出错
		var result = JSON.parse(result);
		var data = result.data;
		$.each(data,function(){
			if(this){
				$select.append('<option value="'+this.id+'">'+this.text+'</option>')
			}
		});
	});
	if(i==$sels.length-1){
		promise.always($.isFunction(callback)?callback():'');
	}else{
		promise.always(renderSelect($sels,++i,callback));
	}

}

/**
 * 确认已收发票，取消已收发票   217   只针对奢华之旅  成本付款 退款付款
 * @param ctx			网站路径
 * @param uuid	        cost_record  ID
 * @param status		发票状态，0：未收，1：已收
 * @param flag			0：确认已收发票，1：撤销已收发票
 * @author jinxin.gao
 */
function confirmOrCannelInvoice(ctx, uuid, status, flag){
	var msg = "";
	if(0 == flag){
		msg = "是否确认收到发票？";
	}else{
		msg = "是否确认撤销收到发票？";
	}
	$.jBox.confirm(msg,"提示",function(v,h,f){
		if(v=='ok'){
			$.ajax({
				type:"POST",
				url:ctx+"/orderCommon/manage/confimOrCancelInvoice",
				data:{id:uuid,status:status},
				success:function(data){
					if(data.flag){
						$.jBox.tip('操作成功', 'success');
						$("#searchForm").submit();
					}else{
						$.jBox.tip('操作失败,原因:' + data.msg, 'error');
						return false;
					}
				}
			})
		}
		if(v=='cancel'){
			return true;
		}
	})
}

/**
 * 批量确认发票   217   只针对奢华之旅  成本付款 退款付款
 * @param ctx			网站路径
 * @param uuid	    cost_record ID
 * @param status		发票状态，0：未收，1：已收
 * @param flag			0：确认已收发票，1：撤销已收发票
 * @author jinxin.gao
 */
function confirmOrCannelInvoiceValues(ctx, uuids, status){
	var tmp = '';
	$("input[name='"+uuids+"']").each(function(){
		if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
			tmp=tmp+$(this).attr('value')+",";
		}
	});
	if(tmp==""){
		alert("请选择至少一条记录");
		return;
	}
	$.jBox.confirm("是否批量确认收到发票？","提示",function(v,h,f){
		if(v=='ok'){
			$.ajax({
				type:"POST",
				url:ctx+"/orderCommon/manage/confimOrCancelInvoice",
				data:{id:tmp,status:status},
				success:function(data){
					if(data.flag){
						$.jBox.tip('操作成功', 'success');
						$("#searchForm").submit();
					}else{
						$.jBox.tip('操作失败,原因:' + data.msg, 'error');
						return false;
					}
				}
			})
		}
		if(v=='cancel'){
			return true;
		}
	})
}
//展开筛选按钮
//界面改版，筛选字段不改变
function launch(){
	$('.zksx').click(function() {
		if($('.ydxbd').is(":hidden")==true) {
			$('.ydxbd').show();
			// $(this).text('收起筛选');
			$(this).addClass('zksx-on');
		}else{
			$('.ydxbd').hide();
			// $(this).text('展开筛选');
			$(this).removeClass('zksx-on');
		}
	});
}

//产品名称获得焦点显示隐藏
function inputTips(){
	$("input[flag=istips]").focusin(function(){
		var obj_this = $(this);
		obj_this.next("span").hide();
	}).focusout(function(){
		var obj_this = $(this);
		if(obj_this.val()!=""){
			obj_this.next("span").hide();
		}else{
			obj_this.next("span").show();
		}
	});
	$("input[flag=istips]").each(function(index, element) {
        if($(element).val()!=""){
			$(element).next("span").hide();
		}
    });
	$(".ipt-tips").click(function(){
		var obj_this = $(this);
		obj_this.prev("input").focus();
	});
}
//产品名称获得焦点显示隐藏--动态添加的元素
function inputTipsDynamic(parentObj){
	parentObj.find("input[flag=istips]").focusin(function(){
		var obj_this = $(this);
		obj_this.next("span").hide();
	}).focusout(function(){
		var obj_this = $(this);
		if(obj_this.val()!=""){
			obj_this.next("span").hide();
		}else{
			obj_this.next("span").show();
		}
	});
	parentObj.find("input[flag=istips]").each(function(index, element) {
        if($(element).val()!=""){
			$(element).next("span").hide();
		}
    });
	parentObj.find(".ipt-tips").click(function(){
		var obj_this = $(this);
		obj_this.prev("input").focus();
	});
}

//接待社回复价 底部添加
function tianjia(){
		$('.ydbz_s').click(function() {		
			$(this).parents('.wbyu').find('dl:last').after("<dl class='wbyu-bot'><dt><label>其它：</label><input name='' type='text' /></dt><dt><label>价格：</label><input class='rmbp17' type='text' />元/人 <input class='seach_shortinput' type='text' />人</dt><dd class='ydbz_s gray clear-btn'>删除</dd></dl>");
			$('.clear-btn').click(function(){$(this).parent().remove();});
		});
		
}


//定义分币种金额对象
function CurrencyMoney(ObjCurrency){
	var obj = {};
	ObjCurrency.each(function(index,element){
		var currencyNameShow = $(element).text().replace(/[ ]/g,"");
		obj[currencyNameShow] = 0.00;
	});	
	return obj;
}
//改价记录 底部增加
function gaijia(productType){
	//改价产品类型
	var theProductType = productType ? productType : "";
	var $optionCurrency = $("#currencyTemplate option");
	//总价--当前金额=游客改价中的当前应收列相加
	var oldSum = new CurrencyMoney($optionCurrency);
	var originalSum = new CurrencyMoney($optionCurrency);
	var futureSum = new CurrencyMoney($optionCurrency);
	$("tr[group^='travler']").each(function(index, element) {
        var currencyName = $(element).find("span[name='gaijiaCurency']").text().replace(/[ ]/g,"");
		oldSum[currencyName] += isFloat($(element).find("span[flag='beforeys']").text().replace(/[ ]/g,"").replace(/,/g,""));
		originalSum[currencyName] += isFloat($(element).find("span[flag='original']").text().replace(/[ ]/g,"").replace(/,/g,""));
    });
	//如果是签证产品
//	if("visa" == theProductType){
//		$("#teamVisa tbody tr").each(function(index, element) {
//			var currencyName = $(element).find("span[name='gaijiaCurency']").text();
//			oldSum[currencyName] += isFloat($(element).find("span[flag='beforedj']").text().replace(/[ ]/g,"").replace(/,/g,""));
//		});
//	}
	//总价--当前金额输出
	var html_oldSum = '';
	$("#currencyTemplate option").each(function(index, element) {
		var currencyNameShow = $(element).text();
		html_oldSum = sumToStr(html_oldSum,currencyNameShow,oldSum[currencyNameShow]);
	});
	$("#totalBefore").html(html_oldSum);
	$("#totalNowtime").append(html_oldSum);
	
	//总价--原始金额输出 add by jyang
	var html_origSum = '';
	$("#currencyTemplate option").each(function(index, element) {
		var currencyNameShow = $(element).text();
		html_origSum = sumToStr(html_origSum,currencyNameShow,originalSum[currencyNameShow]);
	});
	$("#totalOriginal").append(html_origSum);
	
	//总价--差额、改后初始化
	calculateSum(1);
	
	//增加团队改价
	$('.gai-price-btn').click(function() {
		var html = '<li><i><input type="text" name="teamkx" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">其他款项</span></i>&nbsp;';
		html += '<i><select name="teamCurrency">{0}<select></i>&nbsp;'.replace("{0}",$("#currencyTemplate").html());
		html += '<i><input type="text" name="teamMoney" class="gai-price-ipt1" flag="istips" onkeyup="validNum(this)" onafterpast="validNum(this)" /><span class="ipt-tips ipt-tips2">费用</span></i>&nbsp;';
		//html += '<i><input type="text" name="teamremark" class="gai-price-ipt2" flag="istips" /><span class="ipt-tips ipt-tips2">备注</span></i>&nbsp;';
		html += "<i><textarea name='teamremark' cols='180' rows='1' onclick="+ '"' + "this.innerHTML=''" + '"' + ">备注</textarea></i>&nbsp;";
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
	}).on("focusout","input[name='teamMoney']",function(){//修改款项金额
		//验证是否是数字
		validNumFinally(this);
		$(this).next(".ipt-tips2").hide();
		calculateSum(1);
	}).on("change","select[name='teamCurrency']",function(){//修改款项币种
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
		//币种变化
		var $thisSelect = $(this);
		var oldValue = $thisSelect.attr("nowValue");
		var $addOption = $("#currencyTemplate option[value='" + oldValue + "']");
		$thisSelect.attr("nowValue",$thisSelect.val());
		var groupName = $thisSelect.parents("tr").attr("group");
		var $allTr = $('.modifyPrice-table tr[group='+ groupName + ']');
		$allTr.each(function(index, element) {
            var $select = $(element).find("select[name='gaijiaCurency']");
			if($select.find("option[value='" + oldValue +"']").length == 0){
				$select.append($addOption.clone());
				$select.find("option[value='" + $thisSelect.val() +"']").remove();
			}
        });
		//重新计算总价
		calculateSum(1);
	}).on("focusout","input[name=plusys],input[name=plusdj]",function(){
		//验证是否是数字
		validNumFinally(this);
		//重新计算改后金额
		//对应的input框的name值
		var newSumName = $(this).attr("name").replace("plus","after");
		var oldSumName = $(this).attr("name").replace("plus","before");
		var theValue = milliFormat(parseFloat($(this).val()) + parseFloat($(this).parents("tr").find("[flag='" + oldSumName + "']").text().replace(/[ ]/g,"").replace(/,/g,"")),1);
		$(this).parents("tr").find("[flag='" + newSumName + "']").text(theValue);
		//重新计算总价
		if(($(this).attr("name") == "plusys") || ("visa" == theProductType)){
			calculateSum(1);
		}
	}).on("click","[flag=appAll]",function(){
		//对应的input框的name值
		var inputName = $(this).parents("td").find("input").attr("name");
		//应用全部
		var inputValue = $(this).parents("td").find("input[name='" + inputName + "']").val();
		var $thisCurrency = $(this).parents("tr").find("span[name='gaijiaCurency']");
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
				//判断是否包括该币种
				var $thisTr = $(".modifyPrice-table tr[group='" + element +"']");
				var isIncludeCurrency = 0;
				$thisTr.each(function(){
					var $currency1 = $(this).find("span[name='gaijiaCurency']");
					var currencyName1 = ($currency1[0].tagName=="SPAN") ? $currency1.text() : $currency1.find("option:selected").text();
					if(currencyName1 == thisCurrencyName){
						isIncludeCurrency = 1;
						$(this).find("input[name='" + inputName +"']").val(inputValue).trigger("focusout");
						return;
					}
				})
				//如果不包含应用的币种，则新增一条币种记录
//				if(!isIncludeCurrency){
//					$(".modifyPrice-table tr[group='" + element +"'] .gaijia-add").eq(0).trigger("click");
//					var $modifyTr = $(".modifyPrice-table tr[group='" + element +"']").last();
//					//设置币种的选中状态
//					$modifyTr.find("select[name='gaijiaCurency'] option").each(function(index,element){
//						if($(this).text() == thisCurrencyName){
//							$(this).prop("selected",true);
//						}
//					});
//					//设置改价差额
//					$modifyTr.find("input[name='" + inputName + "']").val(inputValue).attr("defaultvalue",inputValue).trigger("focusout");
//				}
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
	
	//添加币种
	$(".gaijia-add").click(function(){
		var $this = $(this);
		var groupName = $this.parents("tr").attr("group");
		//剔除已存在的币种
		var $selectCurrency = $("#currencyTemplate").clone(false);
		var $allTr = $('.modifyPrice-table tr[group='+ groupName + ']');
		var tid ;
		$allTr.each(function(index, element) {
			var currencyName;
            if(0 != $(element).find("span[name=gaijiaCurency]").length){
				currencyName = $(element).find("span[name=gaijiaCurency]").text();
			}else{
				currencyName = $(element).find("select[name=gaijiaCurency] option:selected").text();
			}
			$selectCurrency.find("option").each(function(index, element) {
				if($(element).text() == currencyName){
					$(element).remove();
				}
			});
        });
		//拼添加的html
		//币种
		var HTMLAdd = '<tr group="' + groupName +'">';
		//币种
		HTMLAdd += '<td><select id="" name="gaijiaCurency">' + $selectCurrency.html() + '</select><div class="pr"><i class="gaijia-delete" title="删除币种"></i></div></td>';
		if("employee" == theProductType){
			HTMLAdd += '<td class="tc"><input name="" type="text" value="" /></td>';
		}
		//原始应收价
		HTMLAdd += '<td class="tr" flag="beforeys">0.00</td>';
		//当前应收价
		HTMLAdd += '<td class="tr">0.00</td>';
		//改价差额
		HTMLAdd += '<td class="tc"><input type="hidden" name="travelerids" value=""/><dl class="huanjia">';
		HTMLAdd += '<dt><input name="plusys" type="text" value="0.00" defaultValue="0.00" onafterpast="validNum(this)" onkeyup="validNum(this)" /></dt>';
		HTMLAdd += '<dd><div class="ydbz_x" flag="appAll">应用全部</div><div class="ydbz_x gray" flag="reset">还原</div></dd>';
		HTMLAdd += '</dl></td>';
		//备注
		//HTMLAdd += '<td class="tc"><input name="travelerremark" type="text" value="" /></td>';
		HTMLAdd += '<td class="tc"><textarea name="travelerremark" cols="180" rows="1"></textarea></td>';
		//改后应收价
		HTMLAdd += '<td class="tr" flag="afterys">0</td>';
		//第一行的colspan加1
		var $tdFirst = $allTr.first().find("td").eq(0);
		var rowsapnNum = $tdFirst.attr("rowspan") ? $tdFirst.attr("rowspan"):1;
		$tdFirst.attr("rowspan",Number(rowsapnNum) + 1);
		$allTr.last().after(HTMLAdd);
		//设置select的值
		var $newAllTr = $('.modifyPrice-table tr[group='+ groupName + ']');
		$newAllTr.last().find("select[name=gaijiaCurency] option").eq(0).prop("selected",true);
		var newOptionValue = $newAllTr.last().find("select[name=gaijiaCurency]").val();
		$newAllTr.last().find("select[name=gaijiaCurency]").attr("nowValue",newOptionValue);
		//删除新增行以上有改币种的选项
		$allTr.each(function(index, element) {
            $(element).find("select[name=gaijiaCurency] option").each(function(index, element) {
                if(newOptionValue == $(element).val()){
					$(element).remove();
					return;
				}
            });
        });
		//如果已添加所有币种，则添加按钮消失
		if($newAllTr.length == $("#currencyTemplate").find("option").length){
			$this.hide();
		}
	});
	
	//删除币种
	$(".modifyPrice-table").on("click",".gaijia-delete",function(){	
		var $this = $(this);
		var groupName = $this.parents("tr").attr("group");
		//被删除币种的选中项
		var $optionDelete = $this.parents("td").find("select[name='gaijiaCurency'] option:selected");
		$optionDelete.removeAttr("selected").prop("selected",false);
		//删除币种行
		$this.parents("tr").remove();
		var $allTr = $('.modifyPrice-table tr[group='+ groupName + ']');
		var $tdFirst = $allTr.first().find("td").eq(0);
		var rowsapnNum = $tdFirst.attr("rowspan") ? $tdFirst.attr("rowspan"):1;
		$tdFirst.attr("rowspan",Number(rowsapnNum) - 1);
		//tr组中个项添加删除的币种
		var $newAllTr = $('.modifyPrice-table tr[group='+ groupName + ']');
		$newAllTr.each(function(index, element) {
            var $theselect = $(element).find("select[name='gaijiaCurency']");
			if($theselect.length){
				$theselect.append($optionDelete.clone());
			}
        });
		//显示添加币种
		$newAllTr.find(".gaijia-add").show();
		//重新计算价格
		calculateSum(1);
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
		//改后总额,直接接受input输入 ad by jyang（优化需求）
		var futureSum = new CurrencyMoney($optionCurrency);
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
			plusSum_team[currencyName] += isFloat($(element).find("input[name='teamMoney']").val());
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
		//计算改后总额（优化需求）
		if("1" == thiscalculateType){
			$("tr[group^='travler']").each(function(index, element) {
				var currencyName = $(element).find("span[name='gaijiaCurency']").length ? $(element).find("span[name='gaijiaCurency']").text() : $(element).find("select[name='gaijiaCurency'] option:selected").text();
				futureSum[currencyName] += isFloat($(element).find("[name='plusys']").val());
				var afterPrice = $(element).find("[name='plusys']").val();
				var beforePrice = $(element).find("[flag='beforeys']").text().replace(/[ ]/g,"").replace(/,/g,"");
				var currentPlusy = afterPrice - beforePrice;
				$(element).find("[name='plusysTrue']").val(currentPlusy);
			});
		}
		//如果是签证，则计算“团队签证改价”
		if("visa" == theProductType){
			$("#teamVisa tbody tr").each(function(index, element) {
                var currencyName = $(element).find("span[name='gaijiaCurency']").text();
				plusSum_visa[currencyName] += isFloat($(element).find("[name='plusdj']").val());
            });
		}
		//计算总额 （原有的）
//		for(var p in newSum){
//			//游客改价差额计算
//			if("1" == thiscalculateType){
//				if(0 != plusSum_traveler[p]){
//					var theMoney = milliFormat(plusSum_traveler[p],1);
//					var sign = ' <span class="tdgreen">+</span> ';
//					var isNegative = /^\-/.test(theMoney);
//					if(isNegative){
//						sign = ' <span class="tdred fbold">-</span> ';
//						theMoney = theMoney.replace(/^\-/g,'');
//					}
//					if(sumHTML_traveler != '' || (sumHTML_traveler == '' && isNegative)){
//						sumHTML_traveler += sign;
//					}
//					sumHTML_traveler += '<font class="gray14" flag="bz">' + p + '</font><span class="f20 tdred">' + theMoney + '</span>';
//				}
//			}
//			//改价差额计算=游客改价差额 + 团队改价差额
//			plusSum[p] = plusSum_traveler[p] + plusSum_team[p];
//			
//			//如果是签证，差价应包括“团队签证改价”
//			if("visa" == theProductType){
//				plusSum[p] += plusSum_visa[p];
//			}
//			
//			//改后总额计算
//			newSum[p] = plusSum[p] + oldSum[p];
//			//改价差额html
//			html_plusSum = sumToStr(html_plusSum,p,plusSum[p]);
//			//改后总额html
//			html_newSum = sumToStr(html_newSum,p,newSum[p]);
//		}
		
		for(var p in newSum){
			//游客改价差额计算
			if("1" == thiscalculateType){
				if(0 != futureSum[p]){
					var theMoney = milliFormat(futureSum[p],1);
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
			plusSum[p] = futureSum[p] + plusSum_team[p];
			
			//如果是签证，差价应包括“团队签证改价”
			if("visa" == theProductType){
				plusSum[p] += plusSum_visa[p];
			}
			
			//改后总差额计算
			newSum[p] = futureSum[p] - oldSum[p];
			//改后总额html
			html_plusSum = sumToStr(html_plusSum,p,futureSum[p]);
			//改后总差额html
			html_newSum = sumToStr(html_newSum,p,newSum[p]);
		}
		
		
		//游客改价差额输出
		if("1" == thiscalculateType){
			$("#totalTravelerPlus").html(sumHTML_traveler);			
			$("#totalFuture").html('').append(sumHTML_traveler);
		}
		
		//改价差额输出
		$("#totalPlus").html(html_newSum);
		//改后总额输出
		$("#totalAfter").html(html_plusSum);
	}
}



//改价记录 底部增加
function gaijia2(productType){
	//改价产品类型
	var theProductType = productType ? productType : "";
	var $optionCurrency = $("#currencyTemplate option");
	//总价--当前金额=游客改价中的当前应收列相加
	var oldSum = new CurrencyMoney($optionCurrency);
	$("tr[group^='travler']").each(function(index, element) {
        var currencyName = $(element).find("span[name='gaijiaCurency']").text().replace(/[ ]/g,"");
		oldSum[currencyName] += isFloat($(element).find("span[flag='beforeys']").text().replace(/[ ]/g,"").replace(/,/g,""));
    });
	//如果是签证产品
//	if("visa" == theProductType){
//		$("#teamVisa tbody tr").each(function(index, element) {
//			var currencyName = $(element).find("span[name='gaijiaCurency']").text();
//			oldSum[currencyName] += isFloat($(element).find("span[flag='beforedj']").text().replace(/[ ]/g,"").replace(/,/g,""));
//		});
//	}
	//总价--当前金额输出
	var html_oldSum = '';
	$("#currencyTemplate option").each(function(index, element) {
		var currencyNameShow = $(element).text();
		html_oldSum = sumToStr(html_oldSum,currencyNameShow,oldSum[currencyNameShow]);
	});
	$("#totalBefore").html(html_oldSum);
	
	//总价--差额、改后初始化
	calculateSum(1);
	
	//增加团队改价
	$('.gai-price-btn').click(function() {
		var html = '<li><i><input type="text" name="teamkx" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">其他款项</span></i>&nbsp;';
		html += '<i><select name="teamCurrency">{0}<select></i>&nbsp;'.replace("{0}",$("#currencyTemplate").html());
		html += '<i><input type="text" name="teamMoney" class="gai-price-ipt1" flag="istips" onkeyup="validNum(this)" onafterpast="validNum(this)" /><span class="ipt-tips ipt-tips2">费用</span></i>&nbsp;';
		//html += '<i><input type="text" name="teamremark" class="gai-price-ipt2" flag="istips" /><span class="ipt-tips ipt-tips2">备注</span></i>&nbsp;';
		html += "<i><textarea name='teamremark' cols='180' rows='1' onclick="+ '"' + "this.innerHTML=''" + '"' + ">备注</textarea></i>&nbsp;";
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
	}).on("focusout","input[name='teamMoney']",function(){//修改款项金额
		//验证是否是数字
		validNumFinally(this);
		$(this).next(".ipt-tips2").hide();
		calculateSum(1);
	}).on("change","select[name='teamCurrency']",function(){//修改款项币种
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
		//币种变化
		var $thisSelect = $(this);
		var oldValue = $thisSelect.attr("nowValue");
		var $addOption = $("#currencyTemplate option[value='" + oldValue + "']");
		$thisSelect.attr("nowValue",$thisSelect.val());
		var groupName = $thisSelect.parents("tr").attr("group");
		var $allTr = $('.modifyPrice-table tr[group='+ groupName + ']');
		$allTr.each(function(index, element) {
            var $select = $(element).find("select[name='gaijiaCurency']");
			if($select.find("option[value='" + oldValue +"']").length == 0){
				$select.append($addOption.clone());
				$select.find("option[value='" + $thisSelect.val() +"']").remove();
			}
        });
		//重新计算总价
		calculateSum(1);
	}).on("focusout","input[name=plusys],input[name=plusdj]",function(){
		//验证是否是数字
		validNumFinally(this);
		//重新计算改后金额
		//对应的input框的name值
		var newSumName = $(this).attr("name").replace("plus","after");
		var oldSumName = $(this).attr("name").replace("plus","before");
		var theValue = milliFormat(parseFloat($(this).val()) + parseFloat($(this).parents("tr").find("[flag='" + oldSumName + "']").text().replace(/[ ]/g,"").replace(/,/g,"")),1);
		$(this).parents("tr").find("[flag='" + newSumName + "']").text(theValue);
		//重新计算总价
		if(($(this).attr("name") == "plusys") || ("visa" == theProductType)){
			calculateSum(1);
		}
	}).on("click","[flag=appAll]",function(){
		//对应的input框的name值
		var inputName = $(this).parents("td").find("input").attr("name");
		//应用全部
		var inputValue = $(this).parents("td").find("input[name='" + inputName + "']").val();
		var $thisCurrency = $(this).parents("tr").find("span[name='gaijiaCurency']");
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
				//判断是否包括该币种
				var $thisTr = $(".modifyPrice-table tr[group='" + element +"']");
				var isIncludeCurrency = 0;
				$thisTr.each(function(){
					var $currency1 = $(this).find("span[name='gaijiaCurency']");
					var currencyName1 = ($currency1[0].tagName=="SPAN") ? $currency1.text() : $currency1.find("option:selected").text();
					if(currencyName1 == thisCurrencyName){
						isIncludeCurrency = 1;
						$(this).find("input[name='" + inputName +"']").val(inputValue).trigger("focusout");
						return;
					}
				})
				//如果不包含应用的币种，则新增一条币种记录
//				if(!isIncludeCurrency){
//					$(".modifyPrice-table tr[group='" + element +"'] .gaijia-add").eq(0).trigger("click");
//					var $modifyTr = $(".modifyPrice-table tr[group='" + element +"']").last();
//					//设置币种的选中状态
//					$modifyTr.find("select[name='gaijiaCurency'] option").each(function(index,element){
//						if($(this).text() == thisCurrencyName){
//							$(this).prop("selected",true);
//						}
//					});
//					//设置改价差额
//					$modifyTr.find("input[name='" + inputName + "']").val(inputValue).attr("defaultvalue",inputValue).trigger("focusout");
//				}
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
	
	//添加币种
	$(".gaijia-add").click(function(){
		var $this = $(this);
		var groupName = $this.parents("tr").attr("group");
		//剔除已存在的币种
		var $selectCurrency = $("#currencyTemplate").clone(false);
		var $allTr = $('.modifyPrice-table tr[group='+ groupName + ']');
		var tid ;
		$allTr.each(function(index, element) {
			var currencyName;
            if(0 != $(element).find("span[name=gaijiaCurency]").length){
				currencyName = $(element).find("span[name=gaijiaCurency]").text();
			}else{
				currencyName = $(element).find("select[name=gaijiaCurency] option:selected").text();
			}
			$selectCurrency.find("option").each(function(index, element) {
				if($(element).text() == currencyName){
					$(element).remove();
				}
			});
        });
		//拼添加的html
		//币种
		var HTMLAdd = '<tr group="' + groupName +'">';
		//币种
		HTMLAdd += '<td><select id="" name="gaijiaCurency">' + $selectCurrency.html() + '</select><div class="pr"><i class="gaijia-delete" title="删除币种"></i></div></td>';
		if("employee" == theProductType){
			HTMLAdd += '<td class="tc"><input name="" type="text" value="" /></td>';
		}
		//原始应收价
		HTMLAdd += '<td class="tr" flag="beforeys">0.00</td>';
		//当前应收价
		HTMLAdd += '<td class="tr">0.00</td>';
		//改价差额
		HTMLAdd += '<td class="tc"><input type="hidden" name="travelerids" value=""/><dl class="huanjia">';
		HTMLAdd += '<dt><input name="plusys" type="text" value="0.00" defaultValue="0.00" onafterpast="validNum(this)" onkeyup="validNum(this)" /></dt>';
		HTMLAdd += '<dd><div class="ydbz_x" flag="appAll">应用全部</div><div class="ydbz_x gray" flag="reset">还原</div></dd>';
		HTMLAdd += '</dl></td>';
		//备注
		//HTMLAdd += '<td class="tc"><input name="travelerremark" type="text" value="" /></td>';
		HTMLAdd += '<td class="tc"><textarea name="travelerremark" cols="180" rows="1"></textarea></td>';
		//改后应收价
		HTMLAdd += '<td class="tr" flag="afterys">0</td>';
		//第一行的colspan加1
		var $tdFirst = $allTr.first().find("td").eq(0);
		var rowsapnNum = $tdFirst.attr("rowspan") ? $tdFirst.attr("rowspan"):1;
		$tdFirst.attr("rowspan",Number(rowsapnNum) + 1);
		$allTr.last().after(HTMLAdd);
		//设置select的值
		var $newAllTr = $('.modifyPrice-table tr[group='+ groupName + ']');
		$newAllTr.last().find("select[name=gaijiaCurency] option").eq(0).prop("selected",true);
		var newOptionValue = $newAllTr.last().find("select[name=gaijiaCurency]").val();
		$newAllTr.last().find("select[name=gaijiaCurency]").attr("nowValue",newOptionValue);
		//删除新增行以上有改币种的选项
		$allTr.each(function(index, element) {
            $(element).find("select[name=gaijiaCurency] option").each(function(index, element) {
                if(newOptionValue == $(element).val()){
					$(element).remove();
					return;
				}
            });
        });
		//如果已添加所有币种，则添加按钮消失
		if($newAllTr.length == $("#currencyTemplate").find("option").length){
			$this.hide();
		}
	});
	
	//删除币种
	$(".modifyPrice-table").on("click",".gaijia-delete",function(){	
		var $this = $(this);
		var groupName = $this.parents("tr").attr("group");
		//被删除币种的选中项
		var $optionDelete = $this.parents("td").find("select[name='gaijiaCurency'] option:selected");
		$optionDelete.removeAttr("selected").prop("selected",false);
		//删除币种行
		$this.parents("tr").remove();
		var $allTr = $('.modifyPrice-table tr[group='+ groupName + ']');
		var $tdFirst = $allTr.first().find("td").eq(0);
		var rowsapnNum = $tdFirst.attr("rowspan") ? $tdFirst.attr("rowspan"):1;
		$tdFirst.attr("rowspan",Number(rowsapnNum) - 1);
		//tr组中个项添加删除的币种
		var $newAllTr = $('.modifyPrice-table tr[group='+ groupName + ']');
		$newAllTr.each(function(index, element) {
            var $theselect = $(element).find("select[name='gaijiaCurency']");
			if($theselect.length){
				$theselect.append($optionDelete.clone());
			}
        });
		//显示添加币种
		$newAllTr.find(".gaijia-add").show();
		//重新计算价格
		calculateSum(1);
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
			plusSum_team[currencyName] += isFloat($(element).find("input[name='teamMoney']").val());
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
		//如果是签证，则计算“团队签证改价”
		if("visa" == theProductType){
			$("#teamVisa tbody tr").each(function(index, element) {
                var currencyName = $(element).find("span[name='gaijiaCurency']").text();
				plusSum_visa[currencyName] += isFloat($(element).find("[name='plusdj']").val());
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
			
			//如果是签证，差价应包括“团队签证改价”
			if("visa" == theProductType){
				plusSum[p] += plusSum_visa[p];
			}
			
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
}



//验证是否是数值
function isFloat(str){
	return parseFloat(str) ? parseFloat(str) : 0.00;
}

//正负数字验证
function validNum(dom){
	var thisvalue = $(dom).val();
	var minusSign = false;
	if(thisvalue){
		if(/^\-/.test(thisvalue)){
			minusSign = true;
			thisvalue = thisvalue.substring(1);
		}
		//thisvalue = thisvalue.replace(/\D/g,"");
		thisvalue = thisvalue.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
		var txt = thisvalue.split(".");
        thisvalue = txt[0]+(txt.length>1?"."+txt[1]:"");
		if(minusSign){
			thisvalue = '-' + thisvalue;
		}
		$(dom).val(thisvalue);
	}
}
//失去焦点格式化带两位小数的浮点数
function validNumFinally(dom){
	var thisvalue = $(dom).val();
	var minusSign = false;
	if(thisvalue){
		if(/^\-/.test(thisvalue)){
			minusSign = true;
			thisvalue = thisvalue.substring(1);
		}
		thisvalue = (thisvalue.replace(/^(\d*)$/,"$1.") + "00").replace(/(\d*\.\d\d)\d*/,"$1").replace(/^\./,"0.");
		if(minusSign){
			thisvalue = '-' + thisvalue;
		}
		$(dom).val(thisvalue);
	}else{
		$(dom).val("0.00");
	}
}

//数字添加千位符
function milliFormat(s,isFloat){
	var minusSign = false;
	if(isFloat){//弥补JavaScript浮点运算的一个bug
		try{
			s = s.toFixed(2);
		}catch (e){ 
			
		}
	}

	if((typeof s) != String){
		s = s.toString();
	}
	if(/^\-/.test(s)){
		minusSign = true;
		s = s.substring(1);
	}
	if(/[^0-9\.]/.test(s)) return "invalid value";
	s=s.replace(/^(\d*)$/,"$1.");
	s=(s+"00").replace(/(\d*\.\d\d)\d*/,"$1");
	s=s.replace(".",",");
	var re=/(\d)(\d{3},)/;
	while(re.test(s)){
		s=s.replace(re,"$1,$2");
	}
	if(isFloat){
		s=s.replace(/,(\d\d)$/,".$1");
	}else{
		s=s.replace(/,(\d\d)$/,"");
	}
	if(minusSign){
		s= '-' + s;
	}
	return s.replace(/^\./,"0.");
}
//改价详情--重新申请弹出框
function reApply(){
	var html = '<p class="gaijiaBox">“重新申请”会取消本次改价申请，<br />并发起新的改价申请。</p>';
	$.jBox(html, {title: "提示",buttons:{"取 消":"0","重新申请":"1"},submit:function(v, h, f){
		if (v=="0"){
			//取消的操作
		}else if(v=="1"){
			//重新申请的操作
		}
	},height:155,width:300});
}

//返佣 底部增加
//function fanyong(){
//		$('.fan-price-btn').click(function() {		
//			$(this).parents('.gai-price').find('.gai-price-nr:last').after("<dl class='gai-price-nr'><dt><label>返佣名称：</label><input type='text' name=''></dt><dd><select name=''><option value=''>人民币</option></select></dd><dt><label>费用：</label><input type='text' class='rmb' name=''></dt><dt><label>备注：</label><input type='text' name=''></dt><dd><a class='ydbz_s gray clear-btn'>删除</a></dd></dl>");
//			$('.clear-btn').click(function(){$(this).parents('.gai-price-nr').remove();});
//		});
//		
//}
//发布产品3步-添加其他文件
function addfile(obj){
	var file = "<div style=\"margin-top:8px;\">"+
				$("#othertemplate").clone().html()+                                 
				"</div>";
	
	$(obj).parent().parent().append(file);
}
//发布产品3步-添加签证资料
function addvisafile(obj){
	var html = 
	"<div id=\"visafile\" class=\"mod_information_d8_2\" style=\"margin-top:5px;\">"+$("#signtemplate").clone().html()+"</div>";
	$("#otherflag").prev().prev().after(html);
	$("#thirdStepDiv .mod_information_d8_2:last select[name='country']").comboboxSingle();
}
//操作浮框
function operateHandler() {
    $(document).on("mouseover mouseout", ".handle", function (event) {
        if (event.type == "mouseover") {
            //鼠标悬浮
            if (0 != $(this).find('a').length) {
                $(this).addClass('handle-on');
                $(this).find('dd').addClass('block');
            }
        } else if (event.type == "mouseout") {
            //鼠标离开
            $(this).removeClass('handle-on');
            $(this).find('dd').removeClass('block');
        }
    });
}

function confirmBox(msg, callback,cancelback) {
    top.$.jBox.confirm(msg, '系统提示', function (v) {
        if (v == 'ok') {
            return !callback || callback();
        }else{
            return !cancelback || cancelback();
        }
    }, {
        buttonsFocus: 1
    });
    return false;
}

function infoBox(msg) {
    $.jBox.info(msg, '系统提示');
    return false;
}
function warningBox(msg,callback) {
    $.jBox.warning(msg, '系统提示',function(v){
        if(v=="ok"){
            return !callback || callback();
        }
    });
    return false;
}

//新增询价第一到第二
    function oneToTwo(){
	   $('.inquiry_box1').hide();
	   $('.inquiry_box2').show();
	   $('.inquiry_num').addClass('inquiry_num2')
	}
//新增询价第二到第三
	function TwoToThree (){
	  $('.inquiry_box2').hide();
	  $('.inquiry_box3').show();
	  $('.inquiry_num').addClass('inquiry_num3');
	   inquiry_box3_check(); 
	   inquiry_radio_flights();
	   
	 }
//新增询价第三到第二
	function ThreeToTwo(){
	   $('.inquiry_box3').hide();
	   $('.inquiry_box2').show();
	   $('.inquiry_num').removeClass().addClass('inquiry_num inquiry_num2')
		}
//新增询价第二到第一
	function TwoToOne(){
	   $('.inquiry_box2').hide();
	   $('.inquiry_box1').show();
	   $('.inquiry_num').removeClass().addClass('inquiry_num');
		}
//是否申请机票
	function inquiry_box3_check(){
		if($("input[name='isAppFlight']").prop("checked")){
			$('.inquiry_box3_check').show();
			}else{
			$('.inquiry_box3_check').hide();
			}
		}
//询价客户类型
	function inquiry_radio_peoples(){
		if($("#inquiry_radio_people3").prop("checked")){
			 $('.inquiry_radio_people1').hide();
			 $('.inquiry_radio_people2').hide();
			 $('.inquiry_radio_people3').show();
			}else if($("#inquiry_radio_people2").prop("checked")){
			 $('.inquiry_radio_people1').hide();
			 $('.inquiry_radio_people3').hide();
			 $('.inquiry_radio_people2').show();
			}
			else if($("#inquiry_radio_people").prop("checked")){
			 $('.inquiry_radio_people2').hide();
			 $('.inquiry_radio_people3').hide();
			 $('.inquiry_radio_people1').show();
				}
		}
//机票种类
	function inquiry_radio_flights(){
//			alert(111+$("#inquiry_radio_flights1").prop("checked"));
//			alert($("#inquiry_radio_flights1").prop("checked"));
//			alert($("#inquiry_radio_flights2").prop("checked"));
//			alert($("#inquiry_radio_flights3").prop("checked"));
		if($("#inquiry_radio_flights1").prop("checked")){
			 $('.inquiry_flight1').show();
			 $('.inquiry_flights2').hide(); 
			 $('.inquiry_flights3').hide();
			}else if($("#inquiry_radio_flights2").prop("checked")){
			 $('.inquiry_flights2').show();
			 $('.inquiry_flight1').hide(); 
			 $('.inquiry_flights3').hide();
			}else if($("#inquiry_radio_flights3").prop("checked")){
			 $('.inquiry_flights3').show();
			 $('.inquiry_flight1').hide(); 
			 $('.inquiry_flights2').hide();
			}
		}
//询价添加询价要求
 function inquiry2AddIpt(){
	  $(".seach100").delegate(".inquiry2AddIpt","click",function(){
                $(this).parent().before('<div class="seach25 seach100 pr jd-xs"><p>&nbsp;</p><input type="text" class="seach_longinput" onblur="inquiry2AddOut(this)" onfocus="inquiry2AddFoc(this)"><span class="ipt-tips">填写新要求</span><a onclick="inquiry2DelIpt(this)">删除</a></div>')
            });
	 }
	 function inquiry2DelIpt(obj){
		 $(obj).parent().remove();
		 }
	//得到焦点事件：隐藏填写费用名称提示
	function inquiry2AddFoc(obj) {
	    $(obj).siblings(".ipt-tips").hide();
	}
	//失去焦点事件：如果输入框中没有值,显示提示信息
	function inquiry2AddOut(obj){
	    var obj = $(obj);
	    if(!obj.val()){
	        obj.siblings(".ipt-tips").show();
	    }
	}
//成本价滑过显示具体内容
  function inquiryPriceCon(){
	   $('.Inquiry_c').hover(function(){
		   $(this).find('.inquiry_mouse').show();
		 },function(){
		    $(this).find('.inquiry_mouse').hide();
		 })
	  }	    
		 //新增第二步增加删除询价要求
		 function inquiryTwoText(){
            $(".seach100").delegate(".inquiry_zxdel","click",function(){
				
/*				 $(this).parent().prev().addClass('seach_labelnow')
				 $(this).parent().parent().find('a').show();*/
				 $(this).parent().remove();
            });
		 }
		//新增第二步添加复选条件
		/*function inquiryAddSeachcheck(){
			$(".add_seachcheck").click(function(){
				$(this).prev().removeClass('seach_labelnow')
				var inputName=$(this).prev().find('input').attr('name');
    $(this).before('<span class="seach_check"><input type="radio" name='+inputName+'><input type="text"><a class="inquiry_zxdel">删除</a></span>')
	$(this).parent().find('.add_seachcheck').hide();
		})
	  }*/
	  
//新增询价多选计调和线路国家
function  inquiryCheckBOX() {
	$(".seach_checkbox").on("click","em",function(){
		$(this).parent().remove();
	})
	$(".seach_checkbox").on("hover","a",function(){
		$(this).append("<em></em>")
	})
	$(".seach_checkbox").on("mouseleave","a",function(){
		$(this).parent().find('em').remove();
	})
}
//各块信息展开与收起
function boxCloseOn(){
	$(".closeOrExpand").click(function(){
		var obj_this = $(this);
		if(obj_this.attr("class").match("ydExpand")) {
			obj_this.removeClass("ydExpand").addClass("ydClose");
			obj_this.parent().nextAll("[flag=messageDiv]").eq(0).hide();
		} else {
			obj_this.addClass("ydExpand").removeClass("ydClose");
			obj_this.parent().nextAll("[flag=messageDiv]").eq(0).show();
		}
	});
}

/*//上传 动作
function btfile(){
	$('.input-file').change(function(){
		var inputval=$(this).val();
		$(this).siblings('input[type=text]').val(inputval);								 
	});
	$('.sc-chuan').click(function(){				 
		$(this).siblings('.input-file').click();	
	});

}*/
//上传 动作
function btfile(){
	$('.bgMainRight').delegate(".sc-chuan","click",function(){
		$(this).siblings('.input-file').click();		
		})	;			 
	$('.bgMainRight').delegate(".input-file","change",function(){
		var inputval=$(this).val();
		$(this).siblings('input[type=text]').val(inputval);	
     });
	
}
function addClickEvent(obj){
	obj.change(function(){
		var inputval=$(this).val();
		$(this).siblings('input[type=text]').val(inputval);	
		return;							 
	});
}
/*联运*/
function transportchg(){
	var value=$("#intermodalType option:selected").attr("id");
	if("none" == value){
		$('#nationalTrans').hide();
		$('#groupTrans').hide();
		$("#outArea").prev("p").children(".xing").hide();
//                $('#intermodalType').parent().next().show();
//                $('#intermodalType').parent().next().next().hide();
	} else if ("group" == value){
		$('#nationalTrans').hide();
		$('#groupTrans').show();
		$("#outArea").prev("p").children(".xing").show();
//                $('#intermodalType').parent().next().hide();
//                $('#intermodalType').parent().next().next().show();
	} else if("national" == value){
		$('#nationalTrans').show();
		$('#groupTrans').hide();
		$("#outArea").prev("p").children(".xing").show();
//                $('#intermodalType').parent().next().hide();
//                $('#intermodalType').parent().next().next().hide();
	}else{
		$('#nationalTrans').hide();
		$('#groupTrans').hide();
		$("#outArea").prev("p").children(".xing").hide();
	}
}
//预定第一步添加信息
function yd1AddPeople(obj){
	var contactPeopleNum = $("ul[name=orderpersonMes]").length;
	$(obj).parent().parent().parent().append('<ul class="ydbz_qd" name="orderpersonMes">'+
		'<li><label><span class="xing">*</span>渠道联系人<font>' + (contactPeopleNum+1) +'</font>：</label><input maxlength="10" type="text" id="orderPersonName" value="" class="required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()"/></li>'+
		'<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label><input maxlength="15" type="text" id="orderPersonPhoneNum" value="" class="required" onkeyup="this.value=this.value.replace(/[^\\d\+\\-]/g,\'\')"  onafterpaste="this.value=this.value.replace(/[^\\d\+\\-]/g,\'\')"/><div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,\'\',\'2\')">展开全部</div><span class="ydbz_x gray" onclick="yd1DelPeople(this)">删除联系人</span></li>'+
		'<li flag="messageDiv" class="ydbz_qd_close"><ul>'+
		'<li><label>固定电话：</label><input type="text" /></li>'+
		'<li><label>渠道地址：</label><input type="text"/></li>'+
        '<li><label>传真：</label><input maxlength="" type="text"/></li>'+
        '<li><label>QQ：</label><input maxlength="" type="text"/></li>'+
        '<li><label>Email：</label><input maxlength="" type="text"/></li>'+
        '<li><label>渠道邮编：</label><input maxlength="" type="text"/></li>'+
        '<li><label>其他：</label><input maxlength="" type="text"/></li>'+
        '</ul></li></ul>')
}
/* function yd1AddPeople(obj){
	 $(obj).parent().parent().append(' <li class="ydbz_qd_lilong"><span class="pr jd-xs"><label>添加名称：</label><input type="text" onfocus="inquiry2AddFoc(this)" onblur="inquiry2AddOut(this)"/><span class="ipt-tips">例如Email</span></span><span class="pr jd-xs"><label>添加内容：</label><input type="text" onfocus="inquiry2AddFoc(this)" onblur="inquiry2AddOut(this)"/><span class="ipt-tips">123@trekiz.com</span></span><span class="ydbz_x gray yd1AddPeople" onclick="yd1DelPeople(this)">删除</span></li>')
	 }*/
//预定第一步删除信息
function yd1DelPeople(obj){
	$(obj).parent().parent().remove();
	//重置联系人序号
	$("ul[name=orderpersonMes]").each(function(index, element) {
        $(element).children("li").eq(0).find("font").text(index+1);
    });
}
//预定第二步是否联运
 function ydbz2intermodal(obj){
	if($(obj).val()==1){
		    $(obj).parents('label').nextAll('span').show();
		} else{
			$(obj).parents('label').nextAll('span').hide();
			}
	 }
function ydbz2interradio(){
    var obj=$('.tourist-t-r');
	$(obj).each(function() {
		var input=$(this).find('input:checked');
		input.each(function() {
            var value=$(this).val();
			if("1" == value){
				$(this).parents('label').nextAll('span').show();
            } else{
                $(this).parents('label').nextAll('span').hide();
            }
        });
	
	  
    });
	}
/*预定第二步联运显示价格*/
function ydbz2interselect(obj){
	var value=$(obj).find("option:selected").val();
	$(obj).parent().find('em').html(value);
	}
	
//预定第二步签证类型为其他时,show
function ydbz2CardSelChgs(obj){
	var value=$(obj).find("option:selected").val();
	if("100" == value){
		$(obj).parent().next().show();
	} else{
		$(obj).parent().next().hide();
	}
}
function ydbz2CardSel(){
	var obj=$('.ydbz2CardSelChg');
	$(obj).each(function() {
		var value=$(this).find('option:selected').val();
		if("100" == value){
			$(this).parent().next().show();
		} else{
			$(this).parent().next().hide();
		}
    });
}
//预定第二步自备签
function ydbz2zibeiqian(){
	$("#traveler").on("click","input[name=zibeiqian]",function(){
		var $this = $(this);
		var $siblingsCkb = $this.parents(".tourist-ckb").children("input[type=checkbox]");
		var thisIndex = $siblingsCkb.index($this);
		var $tips = $this.parents(".ydbz_tit_child").siblings(".zjlx-tips").eq(0);
		if($this.attr('checked')) {
			if(!$tips.is(":visible")) {
				$tips.show();
			}
			$tips.children("ul").eq(thisIndex).show();
		} else {
			$tips.children("ul").eq(thisIndex).hide(500,function() {
				var isshow = 0;
				$tips.children("ul").each(function(index, element) {
					if($(element).is(":visible")){
						isshow++;
					}
				});
				if(0 == isshow) {
					$tips.hide();
				}
			});
			
		}
	});
}
//预定步骤展开收起模块
	function boxCloseOnAdd(obj,val,text){
		var obj_this = $(obj);
			if(obj_this.attr("class").match("boxCloseOnAdd")) {
				obj_this.removeClass("boxCloseOnAdd");
				obj_this.parent().nextAll("[flag=messageDiv]").eq(0).show();
					if(text==2){
					obj_this.text("收起全部")
					}
			  if(val==1){
				  obj_this.parent().find('.tourist-t-off').css("display","none");
			     obj_this.parent().find('.tourist-t-on').show();
			  }
			} else {
				obj_this.addClass("boxCloseOnAdd");
				obj_this.parent().nextAll("[flag=messageDiv]").eq(0).hide();
				if(text==2){
					obj_this.text("展开全部");
					}
				 if(val==1){
					obj_this.parent().find('.tourist-t-off').css("display","inline");
			     obj_this.parent().find('.tourist-t-on').hide();
			  }
			}
	}

/*预定第二步上传资料*/
	function ydbz2interfile(obj){
			var dest = $(obj).parent().find("span");
    			var res = $(obj).val();      		
        		$(dest).html(res);
				/*var ishave=$(obj).parent().find('.visaImg');
				if(ishave.length>0){
					$(obj).parent().find('.visaImg').html('<img width="300" onclick="showBigImg(\'images/body-bg.jpg\')" title="点击查看大图" src="images/body-bg.jpg">')
					}else{
						$(obj).parent().append('<div class="visaImg"><img width="300" onclick="showBigImg(\'images/body-bg.jpg\')" title="点击查看大图" src="images/body-bg.jpg"></div>')
						}*/
    	}	
//发布单团产品--添加团期和价格--币种选择
function selectCurrency(){
	//单个设置币种
	$(".choose-currency").hover(function(){
		$(this).addClass("choose-currency-on");
	},function(){
		$(this).removeClass("choose-currency-on");
	}).on("click","dd p",function(){
		var $this = $(this);
		if(!$this.hasClass("p-checked")){
			var oldCurrency = $this.siblings("p.p-checked").text();
			var oldClass = $this.siblings("p.p-checked").attr("addClass");
			$this.addClass("p-checked").siblings("p").removeClass("p-checked");
			//设置币种/人
			var $currency = $this.parents(".add2_nei_table").next("td").find(".currency").eq(0);
			if(0 != $currency.length){
				var txt_currency = $this.text() == "人民币" ? "元" : $this.text();
				if("人民币" == oldCurrency){oldCurrency = "元";}//alert(oldCurrency);
				$currency.html($currency.html().replace(oldCurrency,txt_currency));
			}
			//设置币种图标
			var $input_currency = $this.parents(".add2_nei_table").next("td").find(".ipt-currency").eq(0);
			$input_currency.removeClass(oldClass).addClass($this.attr("addClass"));
			//设置对应的select的选中项
			var $select = $this.parents("td.add2_nei_table").find("select");
			$select.find("option:selected").removeAttr("selected");
			$select.children("option[value=" + $this.attr("value") + "]").attr("selected",true);
		}
	});
	//统一设置币种
	$("#selectCurrency").change(function(){
		var theValue = $(this).val();
		$.each($(".choose-currency"),function(index,element){
			var $this_dl = $(element);
			$this_dl.find("dd p[value=" + theValue + "]").click();
		});
	});
}
//展开关联机票产品信息
function showAirInfo(dom){
	var $this = $(dom);
	var $airInfo = $(".airInfo").eq(0);
	if($airInfo.is(":visible")){
		$airInfo.slideUp();
		$this.siblings(".airInfo-arrow").hide();
		$this.text($this.text().replace("收起","展开"));
	}else{
		$airInfo.slideDown();
		$this.siblings(".airInfo-arrow").show();
		$this.text($this.text().replace("展开","收起"));
	}
}

//驳回
function jbox_bohui(){
	var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
	html += '<textarea style="width:80%; margin:10px auto;" name="" cols="" rows="" ></textarea>';
	html += '</div>';
	$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
			
		}
	},height:250,width:500});

}

//发布单团产品--基础信息填写--联运选择
function transportAdd(element, index){
	var $selectCurrency = $("#templateCurrency").clone();
	var html_selectCurrency = $selectCurrency.removeAttr("id").removeAttr("style")[0].outerHTML;
	var middle = 'onkeyup="this.value=this.value.replace(/[^\\d\\+\\-]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^\\d\\+\\-]/g,\'\')"';
	$(element).parent().parent().append('<p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart' + (index+1) + '" name="intermodalGroupPart" maxlength="50" type="text"><label>联运价格：</label>' + html_selectCurrency +'&nbsp;<input class="valid rmb" id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="8" type="text" ' + middle + ' /><span class="currency">元</span><a class="ydbz_s gray transportDel">删除</a></p>');
	$('.transportAdd').attr('onclick', 'transportAdd(this, ' + (index + 1) + ')');
}
function transportSelect(){
	$(".transport_city").delegate(".transportDel","click",function(){
		$(this).parent().remove();
	});
	//发布单团产品--填写基础信息--联运币种选择
	$(".transport_city").on("change","select[name=selectCurrency]",function(){
		var $this = $(this);
		var theValue = $this.val();
		var oldCurrency = $this.attr("nowClass");
		var newCurrency = $this.children("option:selected").attr("addClass");
		var $currency = $this.siblings(".currency").eq(0);
		if(0 != $currency.length){
			var txt_currency = $this.find("option:selected").text() == "人民币" ? "元" : $this.find("option:selected").text();
			$currency.html(txt_currency);
		}
		$this.next("input[type=text]").removeClass(oldCurrency).addClass(newCurrency);
		$this.attr("nowClass",newCurrency);
	});
}
//发布单团产品-交通方式-航空
function trafficchg(){
	var value=$("#trafficMode option:selected").val();
	if("1,".indexOf(value)>=0&&value!=""){
		$("#trafficName").css("display","inline-block");
		$(".lianyun_select .xing").show();
	}
	else {
		$("#trafficName").css("display","none");
		$(".lianyun_select .xing").hide();
		$("#trafficName option[value='']").attr("selected", true);
	}
}
//发布产品-交通方式-航空-关联机票产品
function linkAirTicket(){
	var html = '<div class="add_allactivity"><label>输入机票产品编号：</label>';
	html += '<input type="text" />';
	html += '</div>';
	$.jBox(html, { title: "选择机票产品",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
			h.find('.nothisPro').hide('fast', function () { $(this).remove(); });
			//如果没有机票产品
			$('<p class="nothisPro" style="display: none;">没有这个产品</p>').appendTo(h).show('slow');
			return false;
		}
	},height:180,width:500});
}
//订单-返佣记录-返佣详情-重新申请
function jbox_cxsq() {
	var submit = function (v, h, f) {
		if(v==true)
		{
			
		}
		return true;
	};

	jBox.warning("“重新申请”会取消本次返佣申请，并发起新的返佣申请。", "提示", submit, { buttons: { '取消': false, '重新申请': true} });
}
//订单-机票订单-航班备注
function jbox_hbbz() {
	var html = '<div style="padding:10px; text-align:center;">';
	html += '<textarea name="" cols="" rows="" style="margin:10px auto; width:90%;"></textarea>';
	html += '</div>';
	$.jBox(html, { title: "航班备注",buttons:{'确定': true,'取消': false }, submit:function(v, h, f){
		if (v==true){
			
			 return true;
		}
	},height:200,width:500});

	
}
//订单-计调订单-退团原因
function jbox_ttyy() {
	var html = '<div style="padding:10px; text-align:center;">';
	html += '<textarea name="" cols="" rows="" style="margin:10px auto; width:90%;"></textarea>';
	html += '</div>';
	$.jBox(html, { title: "退团原因",buttons:{'确定': true,'取消': false }, submit:function(v, h, f){
		if (v==true){
			
			 return true;
		}
	},height:200,width:500});

	
}

//订单-签务身份订单-借款
function jbox_jk() {
	var html = '<div style="margin-top:20px;"><label class="jbox-label">借款金额：</label>';
	html += '<input type="text" /><br /><label class="jbox-label">备注：</label><textarea name="" cols="" rows="" ></textarea>';
	html += '</div>';
	$.jBox(html, { title: "借款",buttons:{'申请借款': true}, submit:function(v, h, f){
		if (v==true){
			
			 return true;
		}
	},height:220,width:380});

	
}

//订单-签务身份订单-申请借款
function jbox_sqjk() {
	var html = '<div style="margin-top:20px;text-align:center;">';
	html += '<p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">姓名:刘铭</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">护照号:UHOG70934589</span></p><p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">下单人:销售_张三</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">下单时间:2014-09-02 11:38:56</span></p><p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">团队类型:单办签</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">收客人:李四</span></p><p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">订单编号:TES347943553</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">操作人:王海，马云，腾飞</span></p><p><span style="width:90%; display:inline-block; text-align:left;overflow:hidden; height:30px;"><label style="width:60px; text-align:right; line-height:30px; float:left;">借款金额：</label><input type="text" class="rmb" value="5000" style="width:70px; margin-left:10px;"></span></p><p ><span style="width:90%; line-height:180%; display:inline-block; text-align:left;overflow:hidden; height:auto;"><label style="width:60px; text-align:right; line-height:30px; float:left;">备注：</label><textarea name="" cols="" rows="" style="margin-left:10px; width:380px;"></textarea></span></p>';
	html += '</div>';
	$.jBox(html, { title: "申请借款",buttons:{'提交审批': true}, submit:function(v, h, f){
		if (v==true){
			
			 return true;
		}
	},height:380,width:550});

					
}

//订单-签务身份订单-借款明细
function jbox_jkmx() {
	var html = '<div style="margin-top:20px;text-align:center;">';
	html += '<p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">姓名:刘铭</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">护照号:UHOG70934589</span></p><p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">下单人:销售_张三</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">下单时间:2014-09-02 11:38:56</span></p><p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">团队类型:单办签</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">收客人:李四</span></p><p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">订单编号:TES347943553</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">操作人:王海，马云，腾飞</span></p><p><span style="width:90%; display:inline-block; text-align:left;overflow:hidden; height:28px;">借款金额:¥5000</span></p><p ><span style="width:90%; display:inline-block; text-align:left;overflow:hidden; height:auto;">备注:</span></p>';
	html += '</div>';
	$.jBox(html, { title: "借款明细",buttons:{'关闭': true}, submit:function(v, h, f){
		if (v==true){
			
			 return true;
		}
	},height:330,width:550});

					
}

//订单-签务身份订单-借护照
function jbox_jhz(travelerId,visaId) {
	if((document.getElementById("passportStatus_"+travelerId).value)=="1"){
		top.$.jBox.tip('护照状态不符！');
		return;
	}
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<label class="jbox-label" style="width:120px;"><span class="xing">*</span>护照借出领取人：</label><input id="passportOperator" type="text" /><br /><label class="jbox-label" style="width:120px;"><span class="xing">*</span>护照借出时间：</label><input id="passportOperateTime" class="inputTxt dateinput" onfocus="WdatePicker({dateFmt:'+"'"+'yyyy-MM-dd HH:mm'+"'"+'})" type="text" style="width:206px;" /><br /><label class="jbox-label" style="width:120px;">护照借出备注：</label><input id="passportOperateRemark" type="text" />';
	html += '</div>';
	
	
	$.jBox(html, { title: "借护照",buttons:{'护照领取单': 1,'确认领取': 2}, submit:function(v, h, f){
		 var passportOperator = $("#passportOperator").val();
		 var passportOperateTime = $("#passportOperateTime").val();
		 
		 if(passportOperator==""){
			 $.jBox.info("护照借出领取人为空，请重新填写！", "信息");
			 return false;
		 }
		 if(passportOperateTime==""){
			 $.jBox.info("护照借出时间为空，请重新选择！", "信息");
			 return false;
		 }
		 
		if (v=="1"){
			if(null != document.getElementById("huzhaolingqu")){
				document.getElementById("huzhaolingqu").click(); 
			}
		}
		
		if (v=="2"){
			 var passportOperator = $("#passportOperator").val();
			 var passportOperateTime = $("#passportOperateTime").val();
			 var passportOperateRemark = $("#passportOperateRemark").val();
			 $("#passportStatus"+visaId+" option").eq(1).attr("selected",true);
			 
			 $.ajax({                 
					cache: true,                 
					type: "POST",                 
					url:g_context_url+ "/visa/order/updatePassportStatus",                 
					data:{id:visaId, 
						travelerId:travelerId, 
						passportStatus:"1",
						passportOperator:passportOperator,
						passportOperateTime:passportOperateTime,
						passportOperateRemark:passportOperateRemark
						},                
					async: false,                 
					error: function(request) {  
						top.$.jBox.tip('操作失败');
					},                 
					success: function(data) {  	
						$("#hidden_passportStatus_"+data).val(1);
						$("#passportStatus_"+data+" option").eq(1).attr("selected",true);
						top.$.jBox.tip('操作成功');
						return true;
					}             
				});
		}
	},height:220,width:380});

	
}
//签证订单修改页面--借护照
function jbox_jhz1(travelerId,visaId) {
	
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<label class="jbox-label" style="width:120px;"><span class="xing">*</span>护照借出领取人：</label><input id="passportOperator" type="text" /><br /><label class="jbox-label" style="width:120px;"><span class="xing">*</span>护照借出时间：</label><input id="passportOperateTime" class="inputTxt dateinput" onfocus="WdatePicker({dateFmt:'+"'"+'yyyy-MM-dd HH:mm'+"'"+'})" type="text" style="width:206px;" /><br /><label class="jbox-label" style="width:120px;">护照借出备注：</label><input id="passportOperateRemark" type="text" />';
	html += '</div>';
	
	
	$.jBox(html, { title: "借护照",buttons:{'护照领取单': 1,'确认领取': 2}, submit:function(v, h, f){
		 var passportOperator = $("#passportOperator").val();
		 var passportOperateTime = $("#passportOperateTime").val();
		 
		 if(passportOperator==""){
			 $.jBox.info("护照借出领取人为空，请重新填写！", "信息");
			 return false;
		 }
		 if(passportOperateTime==""){
			 $.jBox.info("护照借出时间为空，请重新选择！", "信息");
			 return false;
		 }
		 
		if (v=="1"){
			if(null != document.getElementById("huzhaolingqu")){
				document.getElementById("huzhaolingqu").click(); 
			}
		}
		
		if (v=="2"){
			 var passportOperator = $("#passportOperator").val();
			 var passportOperateTime = $("#passportOperateTime").val();
			 var passportOperateRemark = $("#passportOperateRemark").val();
			 $("#passportStatus"+visaId+" option").eq(1).attr("selected",true);
			 
			 $.ajax({                 
					cache: true,                 
					type: "POST",                 
					url:g_context_url+ "/visa/order/updatePassportStatus",                 
					data:{id:visaId, 
						travelerId:travelerId, 
						passportStatus:"1",
						passportOperator:passportOperator,
						passportOperateTime:passportOperateTime,
						passportOperateRemark:passportOperateRemark
						},                
					async: false,                 
					error: function(request) {  
						top.$.jBox.tip('操作失败');
					},                 
					success: function(data) {  	
						$("#hidden_passportStatus_"+data).val(1);
						$("#passportStatus_"+data+" option").eq(1).attr("selected",true);
						top.$.jBox.tip('操作成功');
						return true;
					}             
				});
		}
	},height:220,width:380});

	
}


//订单-签务身份订单-还护照
function jbox_hhz(travelerId,visaId) {
//	if(!(document.getElementById("passportStatus_"+travelerId).value=="1")){
//		top.$.jBox.tip('护照状态不符！');
//		return;
//	}
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<label class="jbox-label" style="width:120px;"><span class="xing">*</span>护照归还领取人：</label><input id="passportOperator" type="text" /><br /><label class="jbox-label" style="width:120px;"><span class="xing">*</span>护照归还时间：</label><input id="passportOperateTime" class="inputTxt dateinput" onfocus="WdatePicker({dateFmt:'+"'"+'yyyy-MM-dd HH:mm'+"'"+'})" type="text"  style="width:206px;" /><br /><label class="jbox-label" style="width:120px;">护照归还备注：</label><input id="passportOperateRemark" type="text" />';
	//html += '<label class="jbox-label" style="width:100px;">护照归还领取人：</label><input name="" type="text" /><br /><label class="jbox-label" style="width:100px;">护照归还时间：</label><input name="" type="text" /><br /><label class="jbox-label" style="width:100px;">护照归还备注：</label><input name="" type="text" />';
	html += '</div>';
	$.jBox(html, { title: "还护照",buttons:{'确认归还': 1}, submit:function(v, h, f){
		var passportOperator = $("#passportOperator").val();
		var passportOperateTime = $("#passportOperateTime").val();
		if(passportOperator==""){
			 $.jBox.info("护照归还领取人为空，请重新填写！", "信息");
			 return false;
		 }
		 if(passportOperateTime==""){
			 $.jBox.info("护照归还时间为空，请重新选择！", "信息");
			 return false;
		 }
		if (v=="1"){
			var passportOperator = $("#passportOperator").val();
			 var passportOperateTime = $("#passportOperateTime").val();
			 var passportOperateRemark = $("#passportOperateRemark").val();
			 $("#passportStatus"+visaId+" option").eq(3).attr("selected",true);
			 
			 $.ajax({                 
					cache: true,                 
					type: "POST",                 
					url:g_context_url+ "/visa/order/updatePassportStatus",                 
					data:{id:visaId, 
						travelerId:travelerId, 
						passportStatus:"4",
						passportOperator:passportOperator,
						passportOperateTime:passportOperateTime,
						passportOperateRemark:passportOperateRemark,
						passportOperateRemark:passportOperateRemark},                
					async: false,                 
					error: function(request) {                     
						top.$.jBox.tip('操作失败');
					},                 
					success: function(data) {                     
						//alert($("#passportStatus_"+data+" option").value);
						$("#hidden_passportStatus_"+data).val(1);
						 $("#passportStatus_"+data+" option").eq(3).attr("selected",true);
						top.$.jBox.tip('操作成功');
						return true;
					}             
				});
		}
	},height:220,width:380});

	
}

//订单-签务身份订单-还收据
function jbox_hyjsj() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<label class="jbox-label">收据金额：</label><input name="" type="text" /><br /><label class="jbox-label">还收据人：</label><input name="" type="text" /><br /><label class="jbox-label">归还时间：</label><input name="" type="text" />';
	html += '</div>';
	$.jBox(html, { title: "还押金收据",buttons:{'还收据': 1}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}
	},height:220,width:380});

	
}
//订单-签务身份订单-还押金收据
function jbox_hsj() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<label class="jbox-label">收据金额：</label><input name="" type="text" /><br /><label class="jbox-label">还领取人：</label><input name="" type="text" /><br /><label class="jbox-label">归还时间：</label><input name="" type="text" />';
	html += '</div>';
	$.jBox(html, { title: "还收据",buttons:{'还收据': 1}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}
	},height:220,width:380});

	
}


//订单-签证-销售身份-参团签证订单修改-撤签
function jbox_cq() {
	var html = '<div style="margin-top:20px; font-size:20px; text-align:center;">';
	html += '是否提交撤签申请';
	html += '</div>';
	$.jBox(html, { title: "签证已送签",persistent:true,buttons:{'确定': 1,'取消': 0}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}else{
			$('.tourist-ckb').find('.traveler:eq(0)').removeAttr('checked');
		}
	},height:150,width:380});

	
}


//订单-签务身份订单-预约表
//function jbox_yyb() {
//	var html = '<div style="margin-top:20px; padding-left:20px;">';
//	html += '<div class="seach25 seach100" style="width:90%; padding-bottom:5px;"><p style="width:50px;">办签人：</p><p class="seach_r" style="width:auto;"><span class="seach_check"><select name="vendorOperator"><option value="0" selected="selected">办签人</option><option value="1">张三</option><option value="2">李四</option><option value="2">王五五</option><option value="2">赵紫龙</option><option value="2">田日照</option></select></span><span class="seach_checkbox" id="vendorOperatorShow"><a>张三</a></span></p></div><label style="width:50px; text-align:right; line-height:30px;">备注：</label><textarea name="" cols="" rows="" ></textarea>';
//	html += '</div>';
//	$.jBox(html, { title: "编辑预约表",buttons:{'确定': 1}, submit:function(v, h, f){
//		if (v=="1"){
//			
//			 return true;
//		}
//	},height:260,width:380});
//	$("select[name='vendorOperator']" ).comboboxInquiry({
//		"afterInvalid":function(event,data){
//			var Array_default = new Array("办签人");
//			if(-1 == $.inArray(data,Array_default)){
//				var isIncluded = 0;
//				$("#vendorOperatorShow a").each(function(index, element) {
//					if(data == $(element).text()){
//						isIncluded = 1;
//						return;
//					}
//				});
//				if(isIncluded){
//					jBox.tip("您已选择");
//				}else{
//					$("#vendorOperatorShow").append('<a>{0}</a>'.replace("{0}",data));
//				}
//			}
//		}
//	});
//	inquiryCheckBOX();
//}
//

//询价-销售询价记录
function jbox_xzxj(){
	var html = '<div class="add_allactivity"><label>产品类型：</label>';
	html += '<select><option>单团</option><option>机票</option><option>签证</option><option>自由行</option><option>大客户</option><option>游学</option></select>';
	html += '</div>';
	$.jBox(html, { title: "询价产品类型选择",buttons:{'提交': 1}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}
	},height:220,width:410});	
}

//运控-散拼库存切位-切位
function jbox_qw() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<p>您好，请选择需要切位对象  ！</p><dl style="width:300px;"><dt style="height:30px; float:left; width:100%; display:none;"><span style="width:50%; display:inline-block;"><input name="qiewei" type="radio" style="margin:0;" value="" checked /> 内部切位</span><span style="width:50%; display:inline-block;"><input name="qiewei" style="margin:0;" type="radio" value="" /> 渠道切位</span></dt><dd style="display:none; margin:0; float:left; width:100%;"><select name="" ><option>全部</option><option>大客户部</option><option>自由行部</option><option>游学部</option></select></dd><dd style="display:block; margin:0; float:left; width:100%;"><select name=""><option>飞扬假期</option><option>大洋国际</option></select></dd></dl>';
	html += '</div>';
	$.jBox(html, { title: "切位对象选择",buttons:{'提交': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}else{
			return true;
		}
	},height:220,width:380});
	
}

//运控-散拼库存切位-归还切位
function jbox_ghqw() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<p>当前余位数量：30</p><dl style="padding-right:5px;"><dt style="height:30px; float:left; width:100%;"><span style="width:50%; display:none;"><input name="qiewei" type="radio" style="margin:0;" value="" /> 内部切位</span><span style="width:50%; display:inline-block;"><input name="qiewei" style="margin:0;" type="radio" value="" checked/> 渠道切位</span></dt><dd style="display:none; margin:0; float:left; width:100%;"><table class="table table-striped table-bordered table-condensed"><thead><tr><th width="25%">序号</th><th width="25%">部门</th><th width="25%">切位</th><th width="25%">已售出切位</th></tr></thead>	<tbody><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 1</td><td class="tc">自由行部</td><td class="tc">10</td><td class="tc">0</td></tr><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 2</td><td class="tc">自由行部</td><td class="tc">10</td><td class="tc">0</td></tr><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 3</td><td class="tc">自由行部</td><td class="tc">10</td><td class="tc">0</td></tr></tbody></table></dd><dd style="display:block; margin:0; float:left; width:100%;"><table class="table table-striped table-bordered table-condensed"><thead><tr><th width="25%">序号</th><th width="25%">部门</th><th width="25%">切位</th><th width="25%">已售出切位</th></tr></thead>	<tbody><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 1</td><td class="tc">飞扬假期</td><td class="tc">10</td><td class="tc">0</td></tr><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 2</td><td class="tc">大洋国际</td><td class="tc">10</td><td class="tc">0</td></tr></tbody></table></dd></dl>';
	html += '<label style="width:90px; text-align:right; line-height:30px; height:30px; float:left;">归还还位数量：</label>';
	html += '<input type="text" /><br /><label style="width:100%; line-height:30px;">请填写您的还位原因！</label><textarea name="" cols="" rows="" style="width:90%;"></textarea>';
	html += '</div>';
	$.jBox(html, { title: "归还切位录入框",buttons:{'提交': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}else{
			return true;
		}
	},height:420,width:480});
}

//运控-散拼库存切位-归还切位 归还一个
function jbox_ghqwone() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<p>当前余位数量：30</p><dl style="overflow:hidden; padding-right:5px;"><dt style="height:30px; float:left; width:100%;"><span style="width:50%; display:inline-block;"><input name="qiewei" style="margin:0;" type="radio" value="" checked/> 渠道切位</span></dt><dd style="margin:0; float:left; width:100%;"><table class="table table-striped table-bordered table-condensed"><thead><tr><th width="25%">序号</th><th width="25%">部门</th><th width="25%">切位</th><th width="25%">已售出切位</th></tr></thead>	<tbody><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 1</td><td class="tc">飞扬假期</td><td class="tc">10</td><td class="tc">0</td></tr></tbody></table></dd></dl>';
	html += '<label style="width:100%; line-height:30px;">请填写您的还位原因！</label><textarea name="" cols="" rows="" style="width:90%;"></textarea>';
	html += '</div>';
	$.jBox(html, { title: "归还切位录入框",buttons:{'提交': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}else{
			return true;
		}
	},height:420,width:480});
	qiewei();
}


//基础信息维护-币种信息
function jbox_bzxx(){
	var html = '<div style=" padding:20px; overflow:hidden;">';
	html += '<dl class="bzxx">';
	html += '<dt><span>币种：</span><input name="" type="text" /></dt>';
	html += '<dt><span>符号：</span><select name=""><option>￥</option><option>$</option><option>€</option></select><span>汇率：</span><input name="" type="text" /></dt>';
	html += '<dt><b>换汇汇率：</b></dt>';
	html += '<dt><span>现金收款：</span><input name="" type="text" /><span>对公收款：</span><input name="" type="text" /></dt>';
	html += '<dt><span>中行折算价：</span><input name="" type="text" /></dt>';
	html += '<dt><span>最低汇率标准：</span><input name="" type="text" /></dt>';
	html += '</dl>';
	html += '</div>';
	$.jBox(html, { title: "新增币种信息",buttons:{'取消':0,'提交': 1}, submit:function(v, h, f){
		if (v=="1"){
			 return true;
		}else{
			return true;
		}
	},height:320,width:530});
}

//基础信息维护-地域城市-添加地域城市
function jbox_gncjqy(){
	var html = '<div style=" padding:20px; overflow:hidden;">';
	html += '<label class="jbox-label" style="width:120px;">二级区域：</label><input name="" type="text" /><br />';
	html += '<label class="jbox-label" style="width:120px;">省/直辖市关联区域：</label>';
	html += '<div class="jbox-nrqy"><ul><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">北京市</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">呼和浩特市</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">湖南省</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">北京市</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">北京市</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">北京市</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">北京市</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">北京市</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">北京市</label></li></ul></div>';
	html += '</div>';
	$.jBox(html, { title: "新增二级区域",buttons:{'取消':0,'提交': 1}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}else{
			return true;
		}
	},height:300,width: 530});
}
function jbox_gncjqy2(){
	var html = '<div style=" padding:20px; overflow:hidden;">';
	html += '<label class="jbox-label" style="width:120px;">区域：</label><input name="" type="text" /><br />';
	html += '<label class="jbox-label" style="width:120px;">国家关联区域：</label>';
	html += '<div class="jbox-nrqy"><ul><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">美国</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">日本</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">韩国</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">美国</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">日本</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">韩国</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">美国</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">日本</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">韩国</label></li></ul></div>';
	html += '</div>';
	$.jBox(html, { title: "新增一级区域",buttons:{'取消':0,'提交': 1}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}else{
			return true;
		}
	},height:300,width: 530});
}


//基础信息维护-地域城市-创建省/市
function jbox_gncjss(){
	var html = '<div style=" padding:20px; overflow:hidden;">';
	html += '<label class="jbox-label" style="width:120px;">省/直辖市：</label><input name="" type="text" /><br />';
	html += '</div>';
	$.jBox(html, { title: "新增创建省/市",buttons:{'取消':0,'提交': 1}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}else{
			return true;
		}
	},height:160,width: 530});
}

//基础信息维护-地域城市-城市添加
function jcxxwh_cszj(){
	$('.jbox-zj').click(function(){				 
		$(this).parent().after('<div class="kongr20"></div><div class="seach25 seach100"><p>城市：</p><input name="" type="text" class="fl"/><div class="ydbz_s jbox-dle gray ml20">删除</div></div>');					 
	});
	$('.jbox-gwzj').click(function(){				 
		$(this).parent().after('<div class="kongr20"></div><div class="seach25 seach100"><p>国家：</p><input name="" type="text" class="fl"/><div class="ydbz_s jbox-dle gray ml20">删除</div></div>');					 
	});
	$('.jbox-dle').live('click',function(){
		$(this).parent().prev().remove()
		$(this).parent().remove();	
		
	});
	
}


//基础信息维护-交通信息-航空公司-新增航空公司
function jbox_xzhkgs() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<dl style="overflow:hidden; padding-right:5px;">';
	html += '<dt style="height:30px; float:left; width:100%;">';
	html += '	<span class="fl jbox-span-radio"><input name="qiewei" type="radio" style="margin:0;" value="" checked /> 国内航空公司</span>';
	html += '	<span class="fl jbox-span-radio"><input name="qiewei" style="margin:0;" type="radio" value="" /> 国外航空公司</span>';
	html += '</dt>';
	html += '<dd class="jbox-margin0 fl"  style=" display:none;">';
	html += '	<label class="jbox-label">国家：</label><select name="" class="jbox-width100"><option value="">中国</option></select><br />';
	html += '	<div class="jbox-margin0 fl">';
	html += '		<label class="jbox-label">航空公司：</label><input type="text" value="" class="fl jbox-width100">';
	html += '		<label class="jbox-label">二字码：</label><input type="text" value="" class="fl jbox-width100">';
	html += '		<div class="ydbz_s jbox-zj">增加</div>';
	html += '	</div>';
	html += '</dd>';
	html += '<dd class="jbox-margin0 fl" style=" display:none;">';
	html += '	<label class="jbox-label">国家：</label><select name="" class="jbox-width100"><option value="">美国</option><option value="">日本</option></select><br />';
	html += '	<div class="jbox-margin0 fl">';
	html += '		<label class="jbox-label">航空公司：</label><input type="text" value="" class="fl jbox-width100">';
	html += '		<label class="jbox-label">二字码：</label><input type="text" value="" class="fl jbox-width100">';
	html += '		<div class="ydbz_s jbox-zj">增加</div>';
	html += '	</div>';
	html += '</dd>';
	html += '</dl>';
	html += '</div>';
	$.jBox(html, { title: "新增航空公司",buttons:{'取消': 0,'提交':1}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}else{
			return true;
		}
	},height:320,width:520});
	
	$('.jbox-zj').click(function(){				 
		$(this).parent().after('<div class="jbox-margin0 fl"><label class="jbox-label">航空公司：</label><input type="text" value="" class="fl jbox-width100"><label class="jbox-label">二字码：</label><input type="text" value="" class="fl jbox-width100"><div class="ydbz_s jbox-dle gray">删除</div></div>');					 
	});
	$('.jbox-dle').live('click',function(){
		$(this).parent().remove();									 
	});
	
	qiewei();	
}
//基础信息维护-交通信息-机场信息-新增机场信息
function jbox_xzjcxx() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<dl style="overflow:hidden; padding-right:5px;">';
	html += '<dt style="height:30px; float:left; width:100%;">';
	html += '	<span class="fl jbox-span-radio"><input name="qiewei" type="radio" style="margin:0;" value="" checked /> 国内机场</span>';
	html += '	<span class="fl jbox-span-radio"><input name="qiewei" style="margin:0;" type="radio" value="" /> 国外机场</span>';
	html += '</dt>';
	html += '<dd class="jbox-margin0 fl"  style=" display:none;">';
	html += '	<label class="jbox-label">国家：</label><select name="" class="jbox-width100"><option value="">中国</option></select><br />';
	html += '	<label class="jbox-label">城市：</label><select name="" class="jbox-width100"><option value="">北京</option><option value="">武汉</option></select><br />';
	html += '	<div class="jbox-margin0 fl">';
	html += '		<label class="jbox-label">机场：</label><input type="text" value="" class="fl jbox-width100">';
	html += '		<label class="jbox-label">三字码：</label><input type="text" value="" class="fl jbox-width100">';
	html += '		<div class="ydbz_s jbox-zj">增加</div>';
	html += '	</div>';
	html += '</dd>';
	html += '<dd class="jbox-margin0 fl" style=" display:none;">';
	html += '	<label class="jbox-label">国家：</label><select name="" class="jbox-width100"><option value="">美国</option><option value="">日本</option></select><br />';
	html += '	<label class="jbox-label">城市：</label><select name="" class="jbox-width100"><option value="">纽约</option><option value="">爱尔兰</option></select><br />';
	html += '	<div class="jbox-margin0 fl">';
	html += '		<label class="jbox-label">机场：</label><input type="text" value="" class="fl jbox-width100">';
	html += '		<label class="jbox-label">三字码：</label><input type="text" value="" class="fl jbox-width100">';
	html += '		<div class="ydbz_s jbox-zj">增加</div>';
	html += '	</div>';
	html += '</dd>';
	html += '</dl>';
	html += '</div>';
	$.jBox(html, { title: "新增机场信息",buttons:{'取消': 0,'提交':1}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}else{
			return true;
		}
	},height:320,width:520});
	
	$('.jbox-zj').click(function(){				 
		$(this).parent().after('<div class="jbox-margin0 fl"><label class="jbox-label">机场：</label><input type="text" value="" class="fl jbox-width100"><label class="jbox-label">三字码：</label><input type="text" value="" class="fl jbox-width100"><div class="ydbz_s jbox-dle gray">删除</div></div>');					 
	});
	$('.jbox-dle').live('click',function(){
		$(this).parent().remove();									 
	});
	
	qiewei();	
}
function qiewei(){
	var qw;
	$('input[name=qiewei]').each(function(){
		if($(this).is(':checked'))
		{
			
			qw=$(this).parent().index();
			$(this).parents('dt').siblings('dd').eq(qw).show().siblings('dd').hide();
		}
	
	});
	
	$('input[name=qiewei]').click(function(){
		qw=$(this).parent().index();
		$(this).parents('dt').siblings('dd').eq(qw).show().siblings('dd').hide();
								   
	});
}

//订单-签证-销售身份-签证参团-申请参团
function jbox_sqct(){
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<p>余位不足</p>';
	html += '</div>';
	$.jBox(html, { title: "申请参团",buttons:{'确定': 1}, submit:function(v, h, f){
		if (v=="1"){
			 jbox_sqct1();
			 return true;
		}
	},height:180,width:410});	
}
function jbox_sqct1(){
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<p>已发送申请</p>';
	html += '</div>';
	$.jBox(html, { title: "申请参团",buttons:{'确定': 1}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}
	},height:180,width:410});	
}




//运控-成本录入-添加项目
function jbox_tjxm(){
	var html = '<div class="costBox">';
	var $templateClone = $("#addItem").clone(false);
	//重置表单元素的id和name值
	$templateClone.find("[id]").each(function(index, element) {
        $(element).attr("id",$(element).attr("id").replace("0","")).attr("name",$(element).attr("name").replace("0",""));
    });
	//重置表单的批发商、渠道商label的for
	$templateClone.find("label[for]").each(function(index, element) {
        $(element).attr("for",$(element).attr("for").replace("0",""));
    });
	html += $templateClone.html();
	html += '</div>';
	$.jBox(html, { title: "成本录入",buttons:{'提交': true,'取消': false}, submit:function(v, h, f){
		if (v==true){
			var container = "";
			var tbodyFirstTdTxt = "";
			var tfootFirstTdTxt = "";
			var name = $.trim(f.name);
            var price = $.trim(f.price);
			var account = $.trim(f.account);
            var comment = f.comment;
			var sorc; //批发商或渠道商的名字
			
			if(1 == f.customer){
				sorc = $("#supply option[value=" + f.supply + "]").text();
			}else{
				sorc = $("#channels option[value=" + f.channels + "]").text();
			}

			//判断是境内还是境外明细
			if("1" == f.detailType){
				container = "#costDomestic";
				firstTrTdTxt = "境内付款明细";
				tfootFirstTdTxt = "境内小计";
			}else{
				container = "#costForeign";
				firstTrTdTxt = "境外付款明细";
				tfootFirstTdTxt = "境外小计";
			}
			//为空判断
			if(name==""){
                top.$.jBox.tip('项目名称不能为空', 'error', { focusId: 'name' }); return false;
            }else if(price==""){
                top.$.jBox.tip('单价不能为空', 'error', { focusId: 'price' }); return false;
            }else if(comment.length > 200){
                top.$.jBox.tip('项目备注不能大于200字','error' ,{ focusId: 'comment' }); return false;
            }else if(account==""){
                top.$.jBox.tip('数量不能为空', 'error', { focusId: 'account' }); return false;
            }else {
				//开发编写保存数据
				//var jsonRes = {name : name, price : price, comment : comment, activityGroupId : groupId};
				//$.ajax({
//					type: "POST",
//					url: "/trekiz_wholesaler/a/cost/manager/save",
//					cache:false,
//					dataType:"json",
//					async:false,
//					data:{value:JSON.stringify(jsonRes),
//						type : classType},
//					success: function(array){
//						for(var i = 0; i < array.length; i++){
//							var index = parseInt($List.find("td:first-child").last().html());
//							if(!index){
//								index = 0;
//							}
//							$List.append('<tr id=' + classType + "_" + array[i].id + '> <td>' + (index + i + 1) + '</td><td>'+array[i].name+'</td><td price-sum-bind="' + classType + '">'+array[i].price+'</td><td>'+array[i].comment+'</td><td><a href="javascript:void(0)" onclick="modifyCost('+ array[i].id +', \'' + classType + '\')">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="return deleteCost('+array[i].id+', \'' + classType + '\');" class="button_delcb"></a></td></tr>');
//						}
//						eventHandler('operatorBudget', $('#contentTable'));
//					},
//					error : function(e){
//						top.$.jBox.tip('请求失败。','error' ,{ focusId: 'price' });
//						return false;
//					}
//				});

				//向table中添加数据
				var html_tr = '<tr value="{10}">'.replace("{10}",f.detailType);
				if($(container).find("tbody tr").length){
					//行合并
					var $firstTd = $(container).find("tbody tr:first td:first")
					var rowspan_count = $firstTd.attr("rowspan") ? Number($firstTd.attr("rowspan")) : 1;
					$firstTd.attr("rowspan",rowspan_count + 1);
				}else{
					html_tr += '<td rowspan="1">{6}</td>'.replace("{6}",firstTrTdTxt);
					$(container).find("tfoot").append('<tr><td>{0}</td><td colspan="7"></td></tr>'.replace("{0}",tfootFirstTdTxt));
				}
				html_tr += '<td width="17%" name="tdName">{0}</td><td class="tr" width="10%" name="tdAccount">{1}</td><td width="15%" name="tdSupply" tovalue="{21}">{2}</td><td width="10%" name="tdCurrencyName">{3}</td><td class="tr" width="10%" name="tdPrice">{4}</td><td width="20%" name="tdComment">{5}</td><td width="8%" class="tc"><a href="javascript:void(0)" onclick="modifyCost(this)">修改</a>&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="deleteCost(this)">删除</a></td></tr>'.replace("{0}",name).replace("{1}",account).replace("{2}",sorc).replace("{3}",$("#currencyType option[value=" + f.currencyType + "]").text()).replace("{4}",milliFormat(price,'1')).replace("{5}",comment).replace("{21}",f.customer);
				$(container).find("tbody").append(html_tr);
				costSum($(container));
				return true;
            }
		}
	},height:470,width:450});
	switchCustomer();
}
//选择客户类别
function switchCustomer(){
	$(".costBox input[name=customer]").change(function(){
		var flag_showDiv = $(this).val();
		if("1" == flag_showDiv){
			$(".costBox").find("[flag=customer1]").show();
			$(".costBox").find("[flag=customer2]").hide();
		}else{
			$(".costBox").find("[flag=customer2]").show();
			$(".costBox").find("[flag=customer1]").hide();
		}
	});
}
//运控-成本录入-添加项目--小计
function costSum(obj){
	var objMoney = {};
	obj.find("tbody tr").each(function(index, element) {
		var currencyName = $(element).find("td[name='tdCurrencyName']").text();
		var thisAccount = $(element).find("td[name='tdAccount']").text();
		var thisPrice = $(element).find("td[name='tdPrice']").text();
		if(typeof objMoney[currencyName] == "undefined"){
			objMoney[currencyName] = parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(",",""),10);
		}else{
			objMoney[currencyName] += parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(",",""),10);
		}
	});
	//输出结果
	var strCurrency = "";
	var sign = " + ";
	var $footTd = obj.find("tfoot tr:first td:last");
	for(var i in objMoney){
		var isNegative = /^\-/.test(objMoney[i]);
		if(isNegative){
			sign = " - ";
		}
		if(strCurrency != '' || (strCurrency == '' && isNegative)){
			strCurrency += sign;
		}
		strCurrency += i + milliFormat(objMoney[i].toString().replace(/^\-/g,''),'1');
	}
	if($footTd.length){
		$footTd.html(strCurrency);
	}
}
//运控-成本录入-修改项目
function modifyCost(dom){
	//清除正在编辑的状态
	$("tr[ismodify='1']").each(function(index, element) {
        $(element).removeAttr("ismodify");
    });
	//设置正在编辑项目tr
	var $thisTr = $(dom).parents("tr");
	$thisTr.attr("ismodify","1");
	//弹出框中加载数据
	var thisDetailType = $thisTr.attr("value");/*境内、境外*/
	var thisSupply = $thisTr.find("td[name='tdSupply']").text();/*批发商*/
	var sorcValue = $thisTr.find("td[name='tdSupply']").attr("tovalue");/*用来判断是批发商or渠道商的value值*/
	var thisName = $thisTr.find("td[name='tdName']").text();/*项目名称*/
	var thisCurrencyType = $thisTr.find("td[name='tdCurrencyName']").text();/*币种*/
	var thisPrice = $thisTr.find("td[name='tdPrice']").text();/*单价*/
	var thisAccount = $thisTr.find("td[name='tdAccount']").text();/*数量*/
	var thisComment = $thisTr.find("td[name='tdComment']").text();/*备注*/
	//绑定数据
	var $templateClone = $("#addItem").clone(false);
	//重置表单元素的id和name值
	$templateClone.find("[id]").each(function(index, element) {
        $(element).attr("id",$(element).attr("id").replace("0","")).attr("name",$(element).attr("name").replace("0",""));
    });
	//重置表单的批发商、渠道商label的for
	$templateClone.find("label[for]").each(function(index, element) {
        $(element).attr("for",$(element).attr("for").replace("0",""));
    });
	
	//去除select的选项
	$templateClone.find("option:selected").removeAttr("selected");
	//境内、境外
	$templateClone.find("#detailType option[value='" + thisDetailType + "']").attr("selected","selected");
	
	//批发商or渠道商
	var $sorcSelect;
	if("1" == sorcValue){
		$sorcSelect = $templateClone.find("#supply option");
		
	}else{
		$sorcSelect = $templateClone.find("#channels option");
	}
	$sorcSelect.each(function(index, element) {
        if($(element).text() == thisSupply){
			$(element).attr("selected","selected");
			return;
		}
    });
	//定位批发商or渠道商的radio
	$templateClone.find("input[name='customer']").removeAttr("checked");
	$templateClone.find("input[name='customer'][value='" + sorcValue + "']").attr("checked","checked");;
	//显示隐藏对应的批发商or渠道商
	$templateClone.find("[flag^='customer']").hide();
	$templateClone.find("[flag='customer" + sorcValue + "']").show();
	
	//项目名称
	$templateClone.find("#name").attr("value",thisName);
	//币种
	$templateClone.find("#currencyType option").each(function(index, element) {
        if($(element).text() == thisCurrencyType){
			$(element).attr("selected","selected");
			return;
		}
    });
	//单价
	$templateClone.find("#price").attr("value",thisPrice.replace(/,/g,''));
	//数量
	$templateClone.find("#account").attr("value",thisAccount);
	//备注
	$templateClone.find("#comment").text(thisComment);
	$.jBox('<div class="costBox">'+$templateClone.html()+'</div>', { title: "成本修改",buttons:{'提交': true,'取消': false}, submit:function(v, h, f){
		if (v==true){
			var $containerTr = $("tr[ismodify='1']");
			var name = $.trim(f.name);
            var price = $.trim(f.price);
			var account = $.trim(f.account);
            var comment = f.comment;
			var sorc; //批发商或渠道商的名字
			
			if(1 == f.customer){
				sorc = $("#supply option[value=" + f.supply + "]").text();
			}else{
				sorc = $("#channels option[value=" + f.channels + "]").text();
			}
			
			//为空判断
			if(name==""){
                top.$.jBox.tip('项目名称不能为空', 'error', { focusId: 'name' }); return false;
            }else if(price==""){
                top.$.jBox.tip('单价不能为空', 'error', { focusId: 'price' }); return false;
            }else if(comment.length > 200){
                top.$.jBox.tip('项目备注不能大于200字','error' ,{ focusId: 'comment' }); return false;
            }else if(account==""){
                top.$.jBox.tip('数量不能为空', 'error', { focusId: 'account' }); return false;
            }else {
				//修改table中的数据
				$containerTr.find("td[name='tdSupply']").text(sorc).attr("tovalue",f.customer);
				$containerTr.find("td[name='tdName']").text(name);
				$containerTr.find("td[name='tdCurrencyName']").text($("#currencyType option[value=" + f.currencyType + "]").text());
				$containerTr.find("td[name='tdPrice']").text(milliFormat(price,'1'));
				$containerTr.find("td[name='tdAccount']").text(account);
				$containerTr.find("td[name='tdComment']").text(comment);
				
				var containerFrom;
				var containerTo;
				var tbodyFirstTdTxtTo = "";
				var tfootFirstTdTxtTo = "";
				
				//判断是境内还是境外明细
				if("1" == f.detailType){
					containerTo = "#costDomestic";
					firstTrTdTxtTo = "境内付款明细";
					tfootFirstTdTxtTo = "境内小计";
					containerFrom = "#costForeign";
				}else{
					containerTo = "#costForeign";
					firstTrTdTxtTo = "境外付款明细";
					tfootFirstTdTxtTo = "境外小计";
					containerFrom = "#costDomestic";
				}
				//若修改境内境外类型
				if(thisDetailType != f.detailType){					
					var tdFromTxt = "";
					//被移除行是否是第一行
					if(!$containerTr.index()){
						tdFromTxt = $containerTr.find("td:first").text();
					}
					//设置境内境外标志
					$containerTr.attr("value",f.detailType).appendTo($(containerTo).find("tbody"));
					$containerTr = $(containerTo).find("tbody tr:last");
					//移入行操作
					if(1 == $(containerTo).find("tbody tr").length){
						if(7 == $containerTr.find("td").length){
							$containerTr.prepend('<td width="10%" rowspan="{0}">{1}</td>'.replace("{0}","1").replace("{1}",firstTrTdTxtTo));
						}else{
							$containerTr.find("td:first").text(firstTrTdTxtTo).attr("rowspan","1");
						}
						$(containerTo).find("tfoot").append('<tr><td width="10%">{0}</td><td colspan="7"></td></tr>'.replace("{0}",tfootFirstTdTxtTo));
					}else{
						if(8 == $containerTr.find("td").length){
							$containerTr.find("td:first").remove();
						}
						//合并行
						var $firstTd = $(containerTo).find("tbody tr:first td:first")
						var rowspan_count = $firstTd.attr("rowspan") ? Number($firstTd.attr("rowspan")) : 1;
						$firstTd.attr("rowspan",rowspan_count + 1);
					}
					
					//被移出行操作
					if(0 == $(containerFrom).find("tbody tr").length){
						$(containerFrom).find("tbody").empty();
						$(containerFrom).find("tfoot").empty();
					}else{
						if("" != tdFromTxt){
							$(containerFrom).find("tbody tr:first").prepend('<td width="10%" rowspan="{0}">{1}</td>'.replace("{0}",$(containerFrom).find("tbody tr").length).replace("{1}",tdFromTxt));
						}else{
							$(containerFrom).find("tbody tr:first td:first").attr("rowspan",$(containerFrom).find("tbody tr").length);
						}
					}
					costSum($(containerFrom));				
				}
				costSum($(containerTo));
				return true;
            }
		}
	},height:470,width:450});
	switchCustomer();
}
//删除成本录入
function deleteCost(dom){
	var $thisTr = $(dom).parents("tr");
	var tableName = "#" + $(dom).parents("table").attr("id");
	var $firstTd = $(tableName).find("tr:first td:first");
	if(1 == $(tableName).find("tbody tr").length){
		$(tableName).find("tbody").empty();
		$(tableName).find("tfoot").empty();
	}else{
		if($thisTr.index()){
			$firstTd.attr("rowspan",Number($firstTd.attr("rowspan"))-1);
		}else{
			$thisTr.next().prepend('<td width="10%" rowspan="{0}">{1}</td>'.replace("{0}",$(tableName).find("tbody tr").length-1).replace("{1}",$firstTd.text()));
		}
		$thisTr.remove();
	}
	costSum($(tableName));
}


//发布签证产品--价格--币种选择
function selectCurrencyVisa(){
	$(".sel-currency").change(function(){
		var theValue = $(this).val();
		var oldCurrency = $(this).attr("nowClass");
		var newCurrency = $(this).children("option:selected").attr("addClass");
		$(this).parent().parent().parent().find("input.ipt-currency").removeClass(oldCurrency).addClass(newCurrency);
		$(this).attr("nowClass",newCurrency);
	});
}


/**notice 公告**/
function notice(){
	

	
	$.fn.myScroll = function(options){
	//默认配置
	var defaults = {
		speed:40,  //滚动速度,值越大速度越慢
		rowHeight:20 //每行的高度
	};
	
	var opts = $.extend({}, defaults, options),intId = [];
	
	function marquee(obj, step){
	
		obj.find("ul").animate({
			marginTop: '-=1'
		},0,function(){
				var s = Math.abs(parseInt($(this).css("margin-top")));
				if(s >= step){
					$(this).find("li").slice(0, 1).appendTo($(this));
					$(this).css("margin-top", 0);
				}
			});
		}
		
		this.each(function(i){
			var sh = opts["rowHeight"],speed = opts["speed"],_this = $(this);
			intId[i] = setInterval(function(){
				if(_this.find("ul").height()<=_this.height()){
					clearInterval(intId[i]);
				}else{
					marquee(_this, sh);
				}
			}, speed);

			_this.hover(function(){
				clearInterval(intId[i]);
			},function(){
				intId[i] = setInterval(function(){
					if(_this.find("ul").height()<=_this.height()){
						clearInterval(intId[i]);
					}else{
						marquee(_this, sh);
					}
				}, speed);
			});
		
		});

	}

}

//订单上传资料补充资料
function orderAddifile(){
	$(".orderAddifile").click(function(){
		$(this).parent().before('<li>名称：<input type="text" disabled="disabled" class="seach_longinput"><input type="button" class="btn btn-primary sc-chuan" value="上传"><input type="file" name="" class="input-file"><a class="ydbz_x inquiry_zxdel">删除</a></li>');
	})
}
//申请发票合开发票
//开票方式为合并类型时，显示添加合开订单按钮
function invoiceTypeChg(){
	 var value=$(".invoiceTypeChg option:selected").index();
	 totalinvoice();
	 $(".invoicemain").each(function(index, element) {
      if(value==index){
		  $(this).show();
		  }else{
			  $(this).hide();
			  }
    });
	}
//添加合开订单
 function invoiceOrder(){
		$('.invoiceAdd').delegate(".ydbz_x","click",function(){
			    var obj=$(this);
				var html = '<div class="add_allactivity"><label>订单号：</label>';
	html += '<input type="text" class="invoiceOrderInput"/>';
	html += '</div>';
	$.jBox(html, { title: "添加合开订单", submit:function(v, h, f){
		if(v=='ok'){
			var values=h.find('.invoiceOrderInput').val();
			if(values==""){
				 h.find('.nothisPro').hide('fast', function () { $(this).remove(); });
			     //如果没订单
			     $('<p class="nothisPro" style="display: none;">没有这个订单</p>').appendTo(h).show('slow');
			     return false;
				}else{
					var orderNum = $(obj).parents(".invoicemain").find(".invoicediv").length;
					var html=$(".invoicedivnone").clone(true);
					$(obj).parent().before(html);
					html.show().removeClass("invoicedivnone")
					html.find(".ydbz_tit span").html(orderNum);
					}
			}	
	},height:180,width:500});
		
	
		})	
	 }
//删除合开订单
	function invoiceOrderDel(obj){
		var invoicemain=$(obj).parents(".invoicemain");
		$(obj).parent().parent().remove();
		invoicemain.find(".invoicediv").each(function(index, element) {
            $(this).find('.ydbz_tit span').html(index);
        });
		totalinvoice();
    }
	
//订单-签证-参团签证订单修改  查看大图
function showBigImg(imgsrc){
	var loaddingPositionTop = $(window).height()/3;
	var html = '<div class="jboxblack"></div>';
	html += '<div class="bigImg" style="top:' + $(window).scrollTop() +'px;"><div class="loading" style="padding-top:' + loaddingPositionTop +'px;"><img src="images/loading.gif" width="64" height="64" /></div></div>';
	$("html,body").attr("style","overflow:hidden;");
	$("body").append(html);
	//按ESC键盘
	$(document).keydown(function(e){
		if(e.keyCode == 27){
			$(".jboxblack").remove();
			$(".bigImg").remove();
			$("html,body").removeAttr("style");
		}
	});
	// 创建对象
	var img = new Image();
	//图片加载完以后
	img.onload = function(){
		var perInit = 1;
		var rate_w = Math.round((window.screen.availWidth/img.width)*1000)/1000;
		var rate_h = Math.round(((window.screen.availHeight-70)/img.height)*1000)/1000;
		var bigImgPaddingTop = 0;
		if(rate_w < rate_h){
			if(perInit > rate_w){
				perInit = rate_w;
			}
		}else{
			if(perInit > rate_h){
				perInit = rate_h;
			}
		}
		if(1 == perInit){
			if($(window).height() > img.height){
				bigImgPaddingTop = ($(window).height() - img.height)/2;
			}
		}
		var html_loadImg = '<div style="padding-top:' + bigImgPaddingTop +'px;"><img src="' + imgsrc +'" height="' + img.height*perInit + '"';
		html_loadImg += '" /></div>';
		html_loadImg += '<div class="tips-exit">按<strong>ESC</strong>键退出全屏浏览</div>';
		
		$(".bigImg").html(html_loadImg);
		//返回顶部
		//document.documentElement.scrollTop = document.body.scrollTop = 0;
	};
	// 改变图片的src
	img.src = imgsrc;
}
//转入团号
 function changeGroups(obj){
	 $(obj).parent().find("div").show();
	 }
//新增航段
function inquiryFlights3Add(obj){
	var id=$(obj).parent().parent().find('.addFlights3Div').length;
	var cloneDiv = $(".addFlights3None").clone(true);
	//重置label与input的id、name、for
	var $lable = cloneDiv.find(".title_samil .seach_check label");
	$lable.each(function(index, element) {
		var str_flag = "-" + (index+1);
        $(element).attr("for","radio3" + id + str_flag);
		var $input = $(element).find("input[type=radio]");
		$input.attr("id","radio3" + id + str_flag).attr("name","searchRadio3" + id);
    });
	cloneDiv.appendTo($(obj).parent().parent());
	cloneDiv.show().removeClass('addFlights3None').find('em').text(id);
}
//删除航段
function inquiryFlights3Del(obj){
	$(obj).parent().parent().parent().remove();
	$('.addFlights3Div').each(function(index, element){
		$(this).find('em').text(index);
		//重置label与input的id、name、for
		if(2 < index){
			var num_i = index;
			var $lable = $(element).find(".title_samil .seach_check label");
			$lable.each(function(index, element) {
				var str_flag = "-" + (index+1);
				$(element).attr("for","radio3" + num_i + str_flag);
				var $input = $(element).find("input[type=radio]");
				$input.attr("id","radio3" + num_i + str_flag).attr("name","searchRadio3" + num_i);
			});
		}
	});
}

//select模糊匹配插件 使用jquery.ui的widget
(function ($) {
    if ($.widget) {
        //下拉框模糊匹配多选
        $.widget("custom.comboboxInquiry", {
        	widgetEventPrefix: "comboboxInquiry",
            _create: function () {
                this.wrapper = $("<span>").addClass("custom-combobox").insertAfter(this.element);
                this.element.hide();
                this._createAutocomplete();
                this._createShowAllButton();
                this.reset();
            },

            _createAutocomplete: function () {
                var selected = this.element.children(":selected"),
				value = selected.val() ? selected.text() : "";

                this.input = $("<input>").appendTo(this.wrapper).val(value).attr("title", "").addClass("custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left").autocomplete({
                    delay: 0,
                    minLength: 0,
                    source: $.proxy(this, "_source")
                });
               var input = this.input;
                this._on(this.input, {
                    autocompleteselect: function (event, ui) {
                        ui.item.option.selected = true;
                        this._trigger("select", event, {
                            item: ui.item.option
                        });
                    }, autocompletechange: "_removeIfInvalid"
                });
            },

            _createShowAllButton: function () {
                var input = this.input,
				wasOpen = false;

                $("<a>").attr("tabIndex", -1).attr("title", "选择").tooltip().appendTo(this.wrapper).button({
                    icons: { primary: "ui-icon-triangle-1-s" },
                    text: false
                }).removeClass("ui-corner-all").addClass("custom-combobox-toggle ui-corner-right").mousedown(function () {
                    wasOpen = input.autocomplete("widget").is(":visible");
                }).click(function () {
                	if(input.is(':disabled')){
                        return;
                    }
                    input.focus();

                    // Close if already visible
                    if (wasOpen) {
                        return;
                    }

                    // Pass empty string as value to search for, displaying all results
                    input.autocomplete("search", "");
                });
            },

            _source: function (request, response) {
                var matcher = new RegExp($.ui.autocomplete.escapeRegex(request.term), "i");
                response(this.element.children("option").map(function () {
                    var text = $(this).text();
                    if ((!request.term || matcher.test(text)))
                        return {
                            label: text,
                            value: text,
                            option: this
                        };
                }));
            },

            _removeIfInvalid: function (event, ui) {

                // Selected an item, nothing to do
                if (ui.item) {//console.log(ui.item);
                    this._trigger("afterInvalid", null, ui.item.value);
                    return;
                }

                // Search for a match (case-insensitive)
                var value = this.input.val(),
					valueLowerCase = value.toLowerCase(),
					valid = false;
                this.element.children("option").each(function () {
                    if ($(this).text().toLowerCase() === valueLowerCase) {
                        this.selected = valid = true;
                        return false;
                    }
                });

                // Found a match, nothing to do
                if (valid) {
                    this._trigger("afterInvalid", null, value);
                    return;
                }


                this.element.val("");
                this.reset();
                this.input.data("ui-autocomplete").term = "";
            },

            _destroy: function () {
                this.wrapper.remove();
                this.element.show();
            },
            //下拉框的数据源发送变化的时候调用该方法
            reset:function(){
                this.input.val(this.element.children(':selected').text());
                this.input.attr('title',this.element.children(':selected').text());
            },
            showTitle:function(){
                var $sl = this.element;
                var $input = this.input;
                $sl.on('comboboxinquiryselect',function(){
                    //改变ui-title中存储的title值
                    $input.data('ui-tooltip-title',$sl.find('option:selected').text());
                    $input.tooltip('close');//关闭的时候,会将ui-tooltip-title 付给title
                });
                $input.attr('title',$sl.find('option:selected').text());
                $input.tooltip({
                    position: { my: "left center", at: "left top-20" }
                });
            }
        });
        //下拉框模糊匹配
        $.widget("custom.comboboxSingle", {
        	widgetEventPrefix: "comboboxSingle",
            _create: function () {
                this.wrapper = $("<span>")
                  .addClass("custom-combobox")
                  .insertAfter(this.element);

                this.element.hide();
                this._createAutocomplete();
                this._createShowAllButton();
                this.reset();
            },

            _createAutocomplete: function () {
                var selected = this.element.children(":selected"),
                  value = selected.val() ? selected.text() : "";

                this.input = $("<input>")
                  .appendTo(this.wrapper)
                  .val(value)
                  .attr("title", "")
                  .addClass("custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left")
                  .autocomplete({
                      delay: 0,
                      minLength: 0,
                      source: $.proxy(this, "_source")
                  })

                this._on(this.input, {
                    autocompleteselect: function (event, ui) {
                        ui.item.option.selected = true;
                        this._trigger("select", event, {
                            item: ui.item.option
                        });
                    },

                    autocompletechange: "_removeIfInvalid"
                });
            },

            _createShowAllButton: function () {
                var input = this.input,
                  wasOpen = false;

                $("<a>")
                  .attr("tabIndex", -1)
                  .attr("title", "选择")
                  .tooltip()
                  .appendTo(this.wrapper)
                  .button({
                      icons: {
                          primary: "ui-icon-triangle-1-s"
                      },
                      text: false
                  })
                  .removeClass("ui-corner-all")
                  .addClass("custom-combobox-toggle ui-corner-right")
                  .mousedown(function () {
                      wasOpen = input.autocomplete("widget").is(":visible");
                  })
                  .click(function () {
                	  if(input.is(':disabled')){
                          return;
                      }
                      input.focus();

                      // Close if already visible
                      if (wasOpen) {
                          return;
                      }

                      // Pass empty string as value to search for, displaying all results
                      input.autocomplete("search", "");
                  });
            },

            _source: function (request, response) {
                var matcher = new RegExp($.ui.autocomplete.escapeRegex(request.term), "i");
                response(this.element.children("option").map(function () {
                    var text = $(this).text();
                    if ((!request.term || matcher.test(text)))
                        return {
                            label: text,
                            value: text,
                            option: this
                        };
                }));
            },

            _removeIfInvalid: function (event, ui) {

                // Selected an item, nothing to do
                if (ui.item) {
                    return;
                }

                // Search for a match (case-insensitive)
                var value = this.input.val(),
                  valueLowerCase = value.toLowerCase(),
                  valid = false;
                this.element.children("option").each(function () {
                    if ($(this).text().toLowerCase() === valueLowerCase) {
                        this.selected = valid = true;
                        return false;
                    }
                });

                // Found a match, nothing to do
                if (valid) {
                    return;
                }

                // Remove invalid value
               /* this.input
                  .val("")
                  .attr("title", value + "")
                  .tooltip("open");*/
                this.element.val("");
                //this._delay(function () {
                //    this.input.tooltip("close").attr("title", "");
                //}, 2500);
                this.reset();
                this.input.data("ui-autocomplete").term = "";
            },
            _destroy: function () {
                this.wrapper.remove();
                this.element.show();
            },
            //下拉框的数据源发送变化的时候调用该方法
            reset:function(){
                this.input.val(this.element.children(':selected').text());
                this.input.attr('title',this.element.children(':selected').text());
            },
            showTitle:function(){
                var $sl = this.element;
                var $input = this.input;
                $sl.on('comboboxsingleselect',function(){
                    //改变ui-title中存储的title值
                    $input.data('ui-tooltip-title',$sl.find('option:selected').text());
                    $input.tooltip('close');//关闭的时候,会将ui-tooltip-title 付给title
                });
                $input.attr('title',$sl.find('option:selected').text());
                $input.tooltip({
                    position: { my: "left center", at: "left top-20" }
                });
            }
        });
    }
})(jQuery);

//table中“团号“、”产品“切换
function switchNumAndPro(){
	//点击团号
	$(".activitylist_bodyer_table").delegate(".tuanhao","click",function(){
        $(this).addClass("on").siblings().removeClass('on');
        $('.chanpin_cen').removeClass('onshow');
        $('.tuanhao_cen').addClass('onshow');
    });
    //点击产品
    $(".activitylist_bodyer_table").delegate(".chanpin","click",function(){
         $(this).addClass("on").siblings().removeClass('on');
         $('.tuanhao_cen').removeClass('onshow');
         $('.chanpin_cen').addClass('onshow');
    });
}
//add by jiangyang table中点击销售/下单人切换
function switchSalerAndPicker() {
    //点击销售
    $(".activitylist_bodyer_table").on("click", ".order-saler-title", function() {
        $(this).addClass("on").siblings().removeClass('on');
        var $table=$(this).parents('table:first');
        $table.find('.order-saler').addClass('onshow');
        $table.find('.order-picker').removeClass('onshow');
    });
    //点击下单人
    $(".activitylist_bodyer_table").on("click", ".order-picker-title", function() {
        $(this).addClass("on").siblings().removeClass('on');
        var $table=$(this).parents('table:first');
        $table.find('.order-saler').removeClass('onshow');
        $table.find('.order-picker').addClass('onshow');
    });
}

function switchSalerAndPicker() {
    //点击团号
    $(".activitylist_bodyer_table").on("click", ".order-saler-title", function() {
        $(this).addClass("on").siblings().removeClass('on');
        var $table=$(this).parents('table:first');
        $table.find('.order-saler').addClass('onshow');
        $table.find('.order-picker').removeClass('onshow');
    });
    //点击产品
    $(".activitylist_bodyer_table").on("click", ".order-picker-title", function() {
        $(this).addClass("on").siblings().removeClass('on');
        var $table=$(this).parents('table:first');
        $table.find('.order-saler').removeClass('onshow');
        $table.find('.order-picker').addClass('onshow');
    });
}

//是否分段报价
	function flyDivCheck(){
		if($("input[name='flyDivInput']").prop("checked")){
			$('.flyMoreDiv').show();
			}else{
			$('.flyMoreDiv').hide();
			}
		}
//预定保存游客信息
function SavePeopleTable(obj){
	
		if($(obj).text()=="保存"){
			
			if($(obj).parent().parent().parent().validate({}).form()){
				$.ajax({                 
					cache: true,                 
					type: "POST",                 
					url:g_context_url+ "/visa/order/doUpdateVisaOrder",                 
					data:$(obj).parent().parent().parent().serialize(),// 你的form                
					async: false,                 
					error: function(request) {                     
						$.jBox.tip("保存失败！", "error");  
					},                 
					success: function(data) {                     
						$.jBox.tip("保存成功！", "success");    
						/**
						var input=$(obj).parent().parent().find("input");
						var textarea=$(obj).parent().parent().find("textarea");
						var selects=$(obj).parent().parent().find("select");
						if($(input).prop("disabled")){
								    $(input).removeAttr("disabled","disabled");
								}else{
									$(input).attr("disabled","disabled");
								}
						if($(textarea).prop("disabled")){
								    $(textarea).removeAttr("disabled","disabled");
								}else{
									$(textarea).attr("disabled","disabled");
								}
						if($(selects).prop("disabled")){
								    $(selects).removeAttr("disabled","disabled");
								}else{
									$(selects).attr("disabled","disabled");
						}
						**/
						window.location.reload();
					}            
				});
			}else{
				$.jBox.tip("请仔细检查表单信息！", "error");
			}
			
		    $(obj).text("修改");
			$(obj).parent().prev().hide();
			$(obj).parent().parent().find('.tourist-t-off').css("display","inline");
			$(obj).parent().parent().find('.tourist-t-on').hide();
			$(obj).parent().parent().find('.add_seachcheck').addClass('boxCloseOnAdd');
		}else{
			$(obj).text("保存");
			$(obj).parent().prev().show();
			$(obj).parent().parent().find('.tourist-t-off').css("display","none");
			$(obj).parent().parent().find('.tourist-t-on').show();
			$(obj).parent().parent().find('.add_seachcheck').removeClass('boxCloseOnAdd');
		}
		//添加费用
		var addcost=$(obj).parent().parent().find(".btn-addBlue");
		if($(addcost).css("display")=="none") {
				$(addcost).show();
					}else{
				$(addcost).hide();
					}
		//删除			
		var deleltecost=$(obj).parent().parent().find("a[name='deleltecost']");
		if($(deleltecost).css("display")=="none") {
				$(deleltecost).show();
					}else{
				$(deleltecost).hide();
					}
		//应用全部			
		var useall=$(obj).parent().parent().find(".yd-total a");
		if($(useall).css("display")=="none") {
				$(useall).show();
					}else{
				$(useall).hide();
					}
}
/*AA码记录*/
	function AAHover(){
		$(".AAHover").hover(function(){
			$(this).find(".AAboxmain").show();
			},function(){
			$(this).find(".AAboxmain").hide();
			})
		}
//报价应用全部
function useAllPrice(obj){
	var selecthouse=$(obj).closest("tourist-right").find("select");
	var selectvalue=selecthouse.val();
		       $(selecthouse).find( "option" ).each(function() {
			         $(this).removeAttr("selected")
				if ( $( this ).val() == selectvalue ) {
				$(this).attr("selected", "selected");
				}
    });
	var cloneDiv = $(obj).closest("tourist-right").clone();
	var touristleft=$(".travelerTable .tourist-left");
	touristleft.after(cloneDiv);
	touristleft.next().next().remove();
	$(".bgMainRight .rightBtn a").each(function() {
	var value=$(this).text();
	  if("修改" == value){
         $(this).parent().parent().find("a[name='deleltecost']").hide();
		 $(this).parent().parent().find(".yd-total a").hide();
		 $(this).parent().parent().find(".btn-addBlue").hide();
		 $(this).parent().parent().find("textarea").attr("disabled","disabled");
		 $(this).parent().parent().find("input").attr("disabled","disabled");
		 $(this).parent().parent().find("select").attr("disabled","disabled");
            }
    });
}

//团队退款
function refunds(){
	$('.refund-price-btn').click(function() {
		var html = '<li><i><input type="text" name="" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">款项</span></i>&nbsp;';
		html += '<i><select class="selectrefund">{0}<select></i>&nbsp;'.replace("{0}",$("#currencyTemplate").html());
		html += '<i><input type="text" class="gai-price-ipt1" name="refund" data-type="rmb" flag="istips" onkeyup="refundInput(this)" onafterpaste="refundInput(this)" onblur="refundInputs(this)"/><span class="ipt-tips ipt-tips2">费用</span></i>&nbsp;';
		html += '<i><input type="text" name="" class="gai-price-ipt2" flag="istips" /><span class="ipt-tips ipt-tips2">备注</span></i>&nbsp;';
		html += '<i><a class="ydbz_s gray clear-btn">删除</a></i>';
		$(this).parents('.gai-price-ol').append(html);
		//新增改价项目中，input获得失去焦点提示信息显示隐藏
		inputTips();
	});
	//删除团队退款一项
	$('.gai-price-ol').on("click",".clear-btn",function(){
		$(this).parents('li').remove();
		totalRefund();
	});
	$('.bgMainRight').on("click",'.gaijia-delete',function(){
		$(this).parent().parent().parent().remove();
		totalRefund();
		})
	$('.bgMainRight').on("click",'.gaijia-add',function(){
		//$(this).parent().parent().parent().remove();
		var html='<tr>';
            html+='<td class="refundtd"><input type="text"><div class="pr"><i class="gaijia-delete" title="删除款项"></i></div></td>';
			html+='<td class="tc"><select style="width:90%;" class="selectrefund">'+$("#currencyTemplate").html()+'</select></td>';
			html+='<td class="tr"><span class="tdgreen">11,900</span></td>';
			html+='<td><input type="text" onkeyup="refundInput(this)" onafterpaste="refundInput(this)" onblur="refundInputs(this)" name="refund" data-type="eur"></td>';
			html+='<td><input type="text"></td></tr>';
		$(this).parents('.refundTable').append(html);
		//$(this).parents('tbody').find('td[rowspan]')
		totalRefund();
		})		
}
	//订单团队退款
	function refundInput(obj){
		var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
		// 只有当检测到不合法字符时才进行替换，因为实时替换会导致光标乱窜
		if (ms != obj.value) {
			var txt = ms.split(".");
			obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
		}
		totalRefund();
	}
	
	function refundInput2(obj){
		var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
		// 只有当检测到不合法字符时才进行替换，因为实时替换会导致光标乱窜
		if (ms != obj.value) {
			var txt = ms.split(".");
			obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
		}
	}
   function refundInputs(obj){
	   objs=obj.value;
	   objs=objs.replace(/^(\d*)$/,"$1.");
	   objs=(objs+"00").replace(/(\d*\.\d\d)\d*/,"$1");
	   objs= objs.replace(/^\./,"0.");
	   $(obj).val(objs)
       $(obj).next("span").hide()
	   
	   }
	function changeRefund(){
		 $(".bgMainRight").delegate("select[class='selectrefund']","change",function(){
		   totalRefund();
		 })
		 $(".table_borderLeftN").delegate("input[type='checkbox']","click",function(){
		   totalRefund();
		 })
		  $(".table_borderLeftN").delegate("input[name='allChk']","click",function(){
		      if($(this).prop("checked")){
			     $(".table_borderLeftN").find("input[type='checkbox']").attr("checked",'true'); totalRefund();
			  }else{
			     $(".table_borderLeftN").find("input[type='checkbox']").removeAttr("checked"); totalRefund();
			  }
		 })
	}
	function totalRefund(){

		 var selects=$("select[class='selectrefund']");
		 var totalcost="";
		 $("#currencyTemplate option").each(function(i, e) {
		    var datatype=$(e).text();
		    e=$(e).val();
			var money = 0;
			selects.each(function(index, element) {
				var si = $(selects[index]);
                var s = si.val();
				var checkinput=si.parents("tr").find("input[type='checkbox']");
				if(checkinput.prop("checked") || checkinput.length==0){
				    if(s == e){
					     var n = si.parent().parent().find("input[name='refund']").val();
					     if(n==""){n=0;}else{money += parseFloat(n);}
				    }  
				}			
            });
			if(money!=""||money!=0){
					datatype="<font class='tdgreen'> + </font><font class='gray14'>"+datatype+"</font>";
					money="<span class='tdred'>"+milliFormat(money,'1')+"</span>"
			        totalcost+=datatype+money;
				}
		});
		if(totalcost==0){$('.all-money').find('span').html(0)}else{$('.all-money').find('span').html(totalcost);}
		$('.all-money').find('span').find(".tdgreen").first().hide();
	}
	
	//渠道 银行账户
	function account_tj(){
		$('.account dt em').live('click',function(){$(this).parents('.account').remove();});
		$('.account_tj').click(function(){
			var ykhtml=$(this).parent().next('.account').html();
			$(this).parent().before('<dl class="account">'+ykhtml+'</dl>');
			
		});
	}
	//渠道 渠道资质
	function qdzz_add(){
		$('.wbyu-bot').on('click','.qdzz-add-remove',function(){$(this).parent('dt').remove();});
		$('.qdzz-add').click(function(){
			$(this).parent().after('<dt><label>其他文件：</label><div class="pr fl"><input value="" class="inputTxt inputTxtlong" name="orderNum" id="orderNum" flag="istips"><span class="ipt-tips">文件名称</span></div><p class="fl"><input type="text" class="w210" name=""  disabled="disabled"><input type="button" value="浏览" class="btn btn-primary sc-chuan"><input type="file" class="input-file" name=""></p><div class="ydbz_s qdzz-add-remove gray">删除</div></dt>');
			inputTips();
			addClickEvent($(this).parent().next().find("input[type='file']"));
		});
	}
	//批发商管理添加地区
	function shopAddressAdd(obj){
		var html=$(obj).parent().next("p").clone(true);
		$(obj).parent().after(html);
		html.show();
		}
	function shopAddressDel(obj){
		$(obj).parent().remove();
		}
	//批发商管理添加联系人
	 function shopPeopleAdd(obj){
		 var id=$(obj).parent().parent().find('p').length;
	     var cloneDiv = $(".shopPeopleNone").clone(true);
	     cloneDiv.appendTo($(obj).parent().parent());
	     cloneDiv.show().removeClass('shopPeopleNone').addClass("shopPeopleP").find('em').text(id);
		 }
    ////批发商管理删除联系人
	function shopPeopleDel(obj){
		$(obj).parent().remove();
		$('.shopPeopleP').each(function(index, element){
		   $(this).find('em').text(index+1);
	      });
		}
//申请发票
     function invoiceInputin(obj){
		 var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
		// 只有当检测到不合法字符时才进行替换，因为实时替换会导致光标乱窜
		if (ms != obj.value) {
			var txt = ms.split(".");
			obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
		}
		 totalinvoice();
   }
   function totalinvoice(){
	   var total=0;
	   var totalone=0;
	   $(".invoicediv").each(function(index, element) {
           var invoiceIn=$(this).find(".invoicetd");
		    invoiceIn.each(function(index, element) {
			 var vals=$(this).val();
			if(vals==""){vals=0;}else{totalone += parseFloat(vals);}
        });
		total+=totalone;
		$(this).find(".totalInvoiceOne").html(milliFormat(totalone,'1'));
		totalone=0;
		$(".totalinvoice").attr("value",milliFormat(total,'1'))
    }); 
   }
   function exchangerateDiv(){
	    $("td[name='exchangeratetd']").hover(function(){
			$(".exchangerate_mouse").show();
			$(this).css("cursor","pointer")
		    var left = $(this).offset().left;
		    var top = $(this).parents("tr").offset().top;
		    $(".exchangerate_mouse").css({"left":left+10,"top":top+30}).show();
		},function(){
			$(".exchangerate_mouse").hide();
		});  
	   }
	   
//新增渠道，结款方式
 function agentpayfor(obj){
	 var sysselect_s=$(".agentpayfor option:selected").index();
	 var indexs=$(obj).find("option:selected").index();
	 if(indexs==1||sysselect_s==1){
		   $(obj).parent().next().show();
		   $(".agentpayfor").next().show();

			$(obj).next().find(".spinner").attr("disabled", false);
			$(".agentpayfor").next().find("input").attr("disabled", false);
		 }else{
			$(obj).next().hide(); 
			$(".agentpayfor").next().hide();

			$(obj).next().find(".spinner").attr("disabled", true);
			$(".agentpayfor").next().find("input").attr("disabled", true);
		}
	 
	//结算方式为按周结算时
	 if(indexs==4||sysselect_s==4){
		 $(obj).parent().next().next().show();
		 $(".agentpayfor").next().next().show();
		 
		 $(obj).next().next().find(".weekSettlement").attr("disabled", false);
		 $(".agentpayfor").next().next().find(".weekSettlement").attr("disabled", false);
	 } else {
		 $(obj).next().next().hide(); 
		 $(".agentpayfor").next().next().hide();
		 
		 $(obj).next().next().find(".weekSettlement").attr("disabled", true);
		 $(".agentpayfor").next().next().find(".weekSettlement").attr("disabled", true);
	 }
 }
 

//发布产品-上传资料-发布选择
function jboxReleaseSelect(){
	var html = '<div class="releaseSelect">';
	html += '<span><input type="checkbox" name="releaseType" id="releaseType1" /><label for="releaseType1">全部可见</label></span>';
	html += '<span><input type="checkbox" name="releaseType" id="releaseType2" /><label for="releaseType2">内部可见</label></span>';
	html += '<span><input type="checkbox" name="releaseType" id="releaseType3" /><label for="releaseType3">渠道可见</label></span>';
	html += '</div>';
	$.jBox(html, { title: "发布选择",buttons:{'提交': true }, submit:function(v, h, f){
		if (v==true){
			return true;
		}
	},height:170,width:350});
}
//发布产品-发布产品类型选择弹框
function jbox_productType(){
	var html = '<div class="add_allactivity" style="padding-left:50px;"><label>产品类型：</label>';
	html += '<select><option value="">单团</option><option value="">散拼</option><option value="">签证</option><option value="">机票</option><option value="">自由行</option></select>';
	html += '</div>';
	$.jBox(html, { title: "发布产品类型选择",buttons:{"预 定":"1"}, submit:function(v, h, f){
		if (v=="1"){
			
		}
	},height:200,width:380});
}
//发布产品-产品基本信息-产品名称长度
function getAcitivityNameLength1(sizeNum) {
	var strSize = sizeNum ? sizeNum : 200;
	var acitivityNameLength = strSize-($("#acitivityName").val().length);
	if(acitivityNameLength>=0){
		$(".acitivityNameSize").text(acitivityNameLength);
	}
}
//询价-细分人数
function peopleNumMore(obj){
	$(obj).parents(".seach25").nextAll(".peoplemore").toggle()
	}

//产品列表-修改产品
function savegroup(srcActivityId,modbtn,delbtn,cancelbtn,obj,childform,childtr,url){
	//显示文字、隐藏文本框
	$(obj).parent().parent().find("span").show();
	$(obj).parent().parent().find("input[type='text']").css("display","none");
   	//$(obj).parent().parent().find("input[type='text']").attr("disabled",true);
	//操作列文字切换
	$(modbtn).show();
	$(delbtn).show();
	$(cancelbtn).hide();
	$(obj).hide();
}
// 删除确认对话框
function confirmxCopy(mess,id,proId,obj,child){
	top.$.jBox.confirm(mess,'系统提示',function(v){
		if(v=='ok'){			
			$(obj).parent().parent().remove();
			if($("#"+child+" tbody").find("tr").length==0){
				$("#"+child).hide();
				$("#groupdate"+proId).removeClass("td-extend");
				$("#groupdate"+proId).parent("tr").removeClass("tr-hover");
			}
		}
	},{buttonsFocus:1});
	return false;
}
//修改
function modgroup(groupid,savebtn,delbtn,cancelbtn,obj){    	
	$(obj).parent().parent().find("span").hide();
	$(obj).parent().parent().find("span").eq(0).show();
	$(obj).parent().parent().find("input[type='text']").css("display","inline-block");
	//$(obj).parent().parent().find("input[type='text']").attr("disabled",false);
	$(groupid).hide();
	//$(groupid).attr("disabled",false);
	$(savebtn).show();
	$(delbtn).hide();
	$(cancelbtn).show();
	$(obj).hide();
}
//取消修改
function cancelgroup(modbtn,savebtn,delbtn,obj){
	$(modbtn).show();
	$(savebtn).hide();
	$(delbtn).show();
	$(obj).hide();
	$(obj).parent().parent().find("span").show();
	$(obj).parent().parent().find("input[type='text']").css("display","none");
	//$(obj).parent().parent().find("input[type='text']").attr("disabled",true);
	//$(obj).parent().parent().find("label",".error").remove();
}
//排序
function Sorting_add(){
	var $_Sorting=$('.activitylist_paixu_left');
	$_Sorting.find('li').click(function(){
		$(this).addClass('activitylist_paixu_moren').siblings().removeClass('activitylist_paixu_moren');									
	});
}



/*add by wangyaru on 20150131
 *财务-结算管理-订单收款确认/财务-结算管理-签证订单收款确认
 */
//模拟下拉框
(function($){
	$.fn.jQSelect = function(settings){
	
		var $div = this;
		var $cartes = $div.find(".cartes");
		var $lists = $div.find(".lists");
		
		var listTxt = $cartes.find(".listTxt");
		var listVal = $cartes.find(".listVal");

		var items = $lists.find("ul > li");
		
		//$div.hover(function(){
//			$(this).addClass("hover");
//		},function(){
//			$(this).removeClass("hover");	
//		});
		$div.click(function(e){
			if("li"!=e.target.tagName.toLowerCase()){
				$(this).addClass("hover");
			}
		}).hover(function(){},function(){
			$(this).removeClass("hover");	
		});
		
		//绑定点击事件
		items.click(function(){
			listVal.val($(this).attr("value"));
			var str = $(this).text();
			listTxt.val($(this).text()).attr("value",$(this).text());
			
			$div.removeClass("hover");
		}).mouseover(function(){
			$(this).removeClass("cwhite");
			$(this).addClass("cgray");
		}).mouseout(function(){
			$(this).removeClass("cgray");
			$(this).addClass("cwhite");
		});
		
	};
})(jQuery);

// 页面打印方法
// 参数 domObj：dom对象，如果不传入参数，打印整个页面，如果传入参数，打印dom对象的内容（包含自身）
// 调用：打印局部 printPage(document.getElementById('ID')); 
//       打印整个页面 printPage(); 
function printPage(domObj) {
    pagesetup_null();
	if (domObj) {
		window.location.reload(true);
		var allHtml = window.document.body.innerHTML;
		var printHtml = domObj.outerHTML;
		window.document.body.innerHTML = printHtml;		
		window.print();
		window.document.body.innerHTML = allHtml;
	} else {
		window.print();
	}
	pagesetup_default();
}

var hkey_root, hkey_path, hkey_key
hkey_root = "HKEY_CURRENT_USER"
hkey_path = "\\software\\Microsoft\\Internet Explorer\\PageSetup\\"

//设置网页打印的页眉页脚边距为空 
function pagesetup_null() {
		try {
			var RegWsh = new ActiveXObject("WScript.Shell");
			hkey_key = "header";
			RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "");
			hkey_key = "footer";
			RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "");
			hkey_key = "margin_left";
			RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.0");
			hkey_key = "margin_right";
			RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.0");
			hkey_key = "margin_top";
			RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.0");
			hkey_key = "margin_bottom";
			RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.0");
		} catch (e) {}
	}

//设置网页打印的页眉页脚边距为默认值 
function pagesetup_default() {
	try {
		var RegWsh = new ActiveXObject("WScript.Shell");
		hkey_key = "header";
		RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "&w&b页码，&p/&P");
		hkey_key = "footer";
		RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "&u&b&d");
		hkey_key = "margin_left";
		RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.750000");
		hkey_key = "margin_right";
		RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.750000");
		hkey_key = "margin_top";
		RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.750000");
		hkey_key = "margin_bottom";
		RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.750000");
	} catch (e) {}
}

//打印回调函数，将打印前一列中“未打印”变为“已打印”
function printCallback(domObj) {
	var $parent = $(domObj);
	while (!$parent.is("td")) {
		$parent = $parent.parent();
	}
	var $prevTD = $parent.prev("td");
	var html = $prevTD.html();
	if (html.contains("未打印")) {
		html = html.replace("未打印", "已打印");
		$prevTD.html(html);
	}
}

//操作浮框 2015.04.17添加 预定-单团
function yd_dt_Handler(){
	$('.yd_dt_handle').hover(function() {
		if(0 != $(this).find('p').length){
			$(this).addClass('handle-on');
			$(this).find('dd').addClass('block');
		}
	},function(){
		$('.yd_dt_handle').removeClass('handle-on');
		$(this).find('dd').removeClass('block');
	});
}

//生成ID
var buildID = (function (prefix, length) {
    var count = 0;
    var baseNum = Math.pow(10, length);
    return function () {
        count++;
        var id = prefix + (count / baseNum).toFixed(length).substr(2);
        if ($("#" + id).length) {
            id = buildID();
        }
        return id;
    }
})("abc", 8);

//运控-成本录入-成本录入-海岛游-预算成本弹出层
function jbox_tianjiayusuanchengben_fab() {
    $.jBox($("#jbox_tianjiayusuanchengben_fab").html(), {
        title: "预算成本录入", buttons: { '保存': 1, '提交审核': 2 }, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: '597', width: 380
    });
    inquiryCheckBOX();
}



//运控-成本录入-成本录入-海岛游-实际成本弹出层
function jbox_tianjiashijichengben_fab() {
    $.jBox($("#jbox_tianjiayusuanchengben_fab").html(), {
        title: "实际成本录入", buttons: { '保存': 1, '提交审核': 2 }, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: '597', width: 380
    });
    inquiryCheckBOX();
}


//财务-结算管理-付款弹出层
function jbox_jiesuanguanli_fkjl_fab() {
    $.jBox($("#finance_pop_receord01").html(), {
        title: "支付记录", buttons: { '撤销': 1, '关闭': 2 }, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: '597', width: 600
    });
    inquiryCheckBOX();
}



//财务-收款-支付确认-订单列表收款确认弹出层
function jbox__caiwushoukzhifuqueren_fab() {
    $.jBox($("#finance_pop_receord01_sk").html(), {
        title: "收款确认账户信息修改", buttons: { '确认收款': 1 }, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: '597', width: 600
    });
    inquiryCheckBOX();
}

//财务-收款-支付确认-订单列表驳回弹出层
function jbox__caiwushoukzhifubohui_fab() {
    $.jBox($("#finance_pop_receord01_bh").html(), {
        title: " 驳回确认", buttons: { '提交': 1 }, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: '597', width: 600
    });
    inquiryCheckBOX();
}

//海岛游-
$(document).ready(function (e) {
    $("span.ydExpand").on('click', function () {
        var $this = $(this);
        var target = $(this).attr("data-target");
        var $target = target ? $(target) : $("div.ydbz_tit").has(this).next();
        $target.slideToggle();
        $this.toggleClass("ydClose");
    });
});



//运控-成本录入-添加其它收入录入项目  add on 2015/05/22
function jbox_tjxm_qt() {
    var html = '<div class="costBox">';
    var $templateClone = $("#addItem_others").clone(false);
    //重置表单元素的id和name值
    $templateClone.find("[id]").each(function (index, element) {
        $(element).attr("id", $(element).attr("id").replace("0", "")).attr("name", $(element).attr("name").replace("0", ""));
    });
    //重置表单的供应商、渠道商label的for
    $templateClone.find("label[for]").each(function (index, element) {
        $(element).attr("for", $(element).attr("for").replace("0", ""));
    });
    html += $templateClone.html();
    html += '</div>';
    $.jBox(html, {
        title: "其它收入录入",
        buttons: {
            '提交': true,
            '取消': false
        },
        submit: function (v, h, f) {
            if (v == true) {
                var container = "";
                var tbodyFirstTdTxt = "";
                var tfootFirstTdTxt = "";
                var name = $.trim(f.name);
                var price = $.trim(f.price);
                var account = $.trim(f.account);
                var comment = f.comment;
                var sorc; //供应商或渠道商的名字

                if (1 == f.customer) {
                    sorc = $("#supply option[value=" + f.supply + "]").text();
                } else {
                    sorc = $("#channels option[value=" + f.channels + "]").text();
                }

                //为空判断
                if (name == "") {
                    top.$.jBox.tip('款项名称不能为空', 'error', {
                        focusId: 'name'
                    });
                    return false;
                } else if (comment.length > 200) {
                    top.$.jBox.tip('项目备注不能大于200字', 'error', {
                        focusId: 'comment'
                    });
                    return false;
                } else {

                    //向table中添加数据
                    var html_tr = '<tr value="{10}">'.replace("{10}", f.detailType);
                    if ($(container).find("tbody tr").length) {
                        //行合并
                        var $firstTd = $(container).find("tbody tr:first td:first")
                        var rowspan_count = $firstTd.attr("rowspan") ? Number($firstTd.attr("rowspan")) : 1;
                        $firstTd.attr("rowspan", rowspan_count + 1);
                    } else {
                        html_tr += '<td rowspan="1">{6}</td>'.replace("{6}", firstTrTdTxt);
                        $(container).find("tfoot").append('<tr><td>{0}</td><td colspan="7"></td></tr>'.replace("{0}", tfootFirstTdTxt));
                    }
                    html_tr += '<td width="17%" name="tdName">{0}</td><td class="tr" width="10%" name="tdAccount">{1}</td><td width="15%" name="tdSupply" tovalue="{21}">{2}</td><td width="10%" name="tdCurrencyName">{3}</td><td class="tr" width="10%" name="tdPrice">{4}</td><td width="20%" name="tdComment">{5}</td><td width="8%" class="tc"><a href="javascript:void(0)" onclick="modifyCost(this)">修改</a>&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="deleteCost(this)">删除</a></td></tr>'.replace("{0}", name).replace("{1}", account).replace("{2}", sorc).replace("{3}", $("#currencyType option[value=" + f.currencyType + "]").text()).replace("{4}", milliFormat(price, '1')).replace("{5}", comment).replace("{21}", f.customer);
                    $(container).find("tbody").append(html_tr);
                    costSum($(container));
                    return true;
                }
            }
        },
        height: 470,
        width: 450
    });
    switchCustomer();
}

//运控-成本录入-海岛游-收款记录-弹出层
function jbox__haidaoyskjl_fab() {
    $.jBox($("#sea_cost_recorded").html(), {
        title: "收款记录", buttons: { '关闭': 1 }, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: '350', width: 870
    });
    inquiryCheckBOX();
}


//选择客户类别

function switchCustomer() {
    $(".costBox input[name=customer]").change(function () {
        var flag_showDiv = $(this).val();
        if ("1" == flag_showDiv) {
            $(".costBox").find("[flag=customer1]").show();
            $(".costBox").find("[flag=customer2]").hide();
        } else {
            $(".costBox").find("[flag=customer2]").show();
            $(".costBox").find("[flag=customer1]").hide();
        }
    });
}



//运控-成本录入-修改项目

function modifyCost(dom) {
    //清除正在编辑的状态
    $("tr[ismodify='1']").each(function (index, element) {
        $(element).removeAttr("ismodify");
    });
    //设置正在编辑项目tr
    var $thisTr = $(dom).parents("tr");
    $thisTr.attr("ismodify", "1");
    //弹出框中加载数据
    var thisDetailType = $thisTr.attr("value"); /*境内、境外*/
    var thisSupply = $thisTr.find("td[name='tdSupply']").text(); /*供应商*/
    var sorcValue = $thisTr.find("td[name='tdSupply']").attr("tovalue"); /*用来判断是供应商or渠道商的value值*/
    var thisName = $thisTr.find("td[name='tdName']").text(); /*项目名称*/
    var thisCurrencyType = $thisTr.find("td[name='tdCurrencyName']").text(); /*币种*/
    var thisPrice = $thisTr.find("td[name='tdPrice']").text(); /*单价*/
    var thisAccount = $thisTr.find("td[name='tdAccount']").text(); /*数量*/
    var thisComment = $thisTr.find("td[name='tdComment']").text(); /*备注*/
    //绑定数据
    var $templateClone = $("#addItem").clone(false);
    //重置表单元素的id和name值
    $templateClone.find("[id]").each(function (index, element) {
        $(element).attr("id", $(element).attr("id").replace("0", "")).attr("name", $(element).attr("name").replace("0", ""));
    });
    //重置表单的供应商、渠道商label的for
    $templateClone.find("label[for]").each(function (index, element) {
        $(element).attr("for", $(element).attr("for").replace("0", ""));
    });

    //去除select的选项
    $templateClone.find("option:selected").removeAttr("selected");
    //境内、境外
    $templateClone.find("#detailType option[value='" + thisDetailType + "']").attr("selected", "selected");

    //供应商or渠道商
    var $sorcSelect;
    if ("1" == sorcValue) {
        $sorcSelect = $templateClone.find("#supply option");

    } else {
        $sorcSelect = $templateClone.find("#channels option");
    }
    $sorcSelect.each(function (index, element) {
        if ($(element).text() == thisSupply) {
            $(element).attr("selected", "selected");
            return;
        }
    });
    //定位供应商or渠道商的radio
    $templateClone.find("input[name='customer']").removeAttr("checked");
    $templateClone.find("input[name='customer'][value='" + sorcValue + "']").attr("checked", "checked");;
    //显示隐藏对应的供应商or渠道商
    $templateClone.find("[flag^='customer']").hide();
    $templateClone.find("[flag='customer" + sorcValue + "']").show();

    //项目名称
    $templateClone.find("#name").attr("value", thisName);
    //币种
    $templateClone.find("#currencyType option").each(function (index, element) {
        if ($(element).text() == thisCurrencyType) {
            $(element).attr("selected", "selected");
            return;
        }
    });
    //单价
    $templateClone.find("#price").attr("value", thisPrice.replace(/,/g, ''));
    //数量
    $templateClone.find("#account").attr("value", thisAccount);
    //备注
    $templateClone.find("#comment").text(thisComment);
    $.jBox('<div class="costBox">' + $templateClone.html() + '</div>', {
        title: "成本修改",
        buttons: {
            '提交': true,
            '取消': false
        },
        submit: function (v, h, f) {
            if (v == true) {
                var $containerTr = $("tr[ismodify='1']");
                var name = $.trim(f.name);
                var price = $.trim(f.price);
                var account = $.trim(f.account);
                var comment = f.comment;
                var sorc; //供应商或渠道商的名字

                if (1 == f.customer) {
                    sorc = $("#supply option[value=" + f.supply + "]").text();
                } else {
                    sorc = $("#channels option[value=" + f.channels + "]").text();
                }

                //为空判断
                if (name == "") {
                    top.$.jBox.tip('项目名称不能为空', 'error', {
                        focusId: 'name'
                    });
                    return false;
                } else if (price == "") {
                    top.$.jBox.tip('单价不能为空', 'error', {
                        focusId: 'price'
                    });
                    return false;
                } else if (comment.length > 200) {
                    top.$.jBox.tip('项目备注不能大于200字', 'error', {
                        focusId: 'comment'
                    });
                    return false;
                } else if (account == "") {
                    top.$.jBox.tip('数量不能为空', 'error', {
                        focusId: 'account'
                    });
                    return false;
                } else {
                    //修改table中的数据
                    $containerTr.find("td[name='tdSupply']").text(sorc).attr("tovalue", f.customer);
                    $containerTr.find("td[name='tdName']").text(name);
                    $containerTr.find("td[name='tdCurrencyName']").text($("#currencyType option[value=" + f.currencyType + "]").text());
                    $containerTr.find("td[name='tdPrice']").text(milliFormat(price, '1'));
                    $containerTr.find("td[name='tdAccount']").text(account);
                    $containerTr.find("td[name='tdComment']").text(comment);

                    var containerFrom;
                    var containerTo;
                    var tbodyFirstTdTxtTo = "";
                    var tfootFirstTdTxtTo = "";

                    //判断是境内还是境外明细
                    if ("1" == f.detailType) {
                        containerTo = "#costDomestic";
                        firstTrTdTxtTo = "境内付款明细";
                        tfootFirstTdTxtTo = "境内小计";
                        containerFrom = "#costForeign";
                    } else {
                        containerTo = "#costForeign";
                        firstTrTdTxtTo = "境外付款明细";
                        tfootFirstTdTxtTo = "境外小计";
                        containerFrom = "#costDomestic";
                    }
                    //若修改境内境外类型
                    if (thisDetailType != f.detailType) {
                        var tdFromTxt = "";
                        //被移除行是否是第一行
                        if (!$containerTr.index()) {
                            tdFromTxt = $containerTr.find("td:first").text();
                        }
                        //设置境内境外标志
                        $containerTr.attr("value", f.detailType).appendTo($(containerTo).find("tbody"));
                        $containerTr = $(containerTo).find("tbody tr:last");
                        //移入行操作
                        if (1 == $(containerTo).find("tbody tr").length) {
                            if (7 == $containerTr.find("td").length) {
                                $containerTr.prepend('<td width="10%" rowspan="{0}">{1}</td>'.replace("{0}", "1").replace("{1}", firstTrTdTxtTo));
                            } else {
                                $containerTr.find("td:first").text(firstTrTdTxtTo).attr("rowspan", "1");
                            }
                            $(containerTo).find("tfoot").append('<tr><td width="10%">{0}</td><td colspan="7"></td></tr>'.replace("{0}", tfootFirstTdTxtTo));
                        } else {
                            if (8 == $containerTr.find("td").length) {
                                $containerTr.find("td:first").remove();
                            }
                            //合并行
                            var $firstTd = $(containerTo).find("tbody tr:first td:first")
                            var rowspan_count = $firstTd.attr("rowspan") ? Number($firstTd.attr("rowspan")) : 1;
                            $firstTd.attr("rowspan", rowspan_count + 1);
                        }

                        //被移出行操作
                        if (0 == $(containerFrom).find("tbody tr").length) {
                            $(containerFrom).find("tbody").empty();
                            $(containerFrom).find("tfoot").empty();
                        } else {
                            if ("" != tdFromTxt) {
                                $(containerFrom).find("tbody tr:first").prepend('<td width="10%" rowspan="{0}">{1}</td>'.replace("{0}", $(containerFrom).find("tbody tr").length).replace("{1}", tdFromTxt));
                            } else {
                                $(containerFrom).find("tbody tr:first td:first").attr("rowspan", $(containerFrom).find("tbody tr").length);
                            }
                        }
                        costSum($(containerFrom));
                    }
                    costSum($(containerTo));
                    return true;
                }
            }
        },
        height: 470,
        width: 450
    });
    switchCustomer();
}

$(document).ready(function () {
    $("div.filter_sort").on('click', "a", function () {
        var $sort = $(this).find("i.i_sort");
        if ($sort.length) {
            if ($sort.is("i.i_sort_down")) {
                $sort.addClass("i_sort_up");
                $sort.removeClass("i_sort_down");
            } else {
                $sort.remove("i_sort_up");
                $sort.addClass("i_sort_down");
            }
        }
    });

});


//订单-海岛游-订单列表页-弹出层
function jbox__haidaodingdanliebiao_fab() {
    $.jBox($("#cancel_order_pop_o").html(), {
        title: "取消订单", buttons: { '确认': 1 }, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: '350', width: 400
    });
    inquiryCheckBOX();
}


//数字输入
$(document).ready(function () {
    $("input[data-type='number']").css("ime-mode", "disabled");
    $("input[data-type='float']").css("ime-mode", "disabled");
    $("input[data-type='amount']").css("ime-mode", "disabled");

    //金额验证
    $(document).on("keypress", "input[data-type='amount']", function (event) {
           var retValue = false;
        var keyCode = event.charCode == undefined ? event.keyCode : event.charCode;
        if ((keyCode >= 48 && keyCode <= 57) || keyCode == 99 || keyCode == 118) {
            retValue = true;
        } else if (keyCode == 46 || keyCode == 45) {
            retValue = $(this).val().indexOf(String.fromCharCode(keyCode)) < 0;
        }
        var amountReg = new RegExp('^\\d{0,}(\\.\\d{0,2}){0,1}$');
        var isDecimal_2 = new RegExp('^\\d{0,}(\\.\\d{2})$');
        if($(this).val() && !amountReg.test( $(this).val())){
            retValue=false;
        }

        return keyCode == 0 || retValue;
    });
    $(document).on("keyup", "input[data-type='amount']", function () {
        var amountReg = new RegExp('^\\d{0,}(\\.\\d{0,2}){0,1}$');
        if($(this).val() && !amountReg.test( $(this).val())){
            $(this).val($(this).data("oldvalue"));
        }else {
            $(this).data("oldvalue",  $(this).val());
        }
    })

    $(document).on("keypress", "input[data-type='number']", function (event) {
        var retValue = false;
        var keyCode = event.charCode == undefined ? event.keyCode : event.charCode;
        if ((keyCode >= 48 && keyCode <= 57) || keyCode == 99 || keyCode == 118) {
            retValue = true;
        } else if (keyCode == 45) {
            retValue = $(this).val().indexOf(String.fromCharCode(keyCode)) < 0;
        }
        if (retValue) {
            $(this).data("oldvalue", $(this).val());
        }
        return keyCode == 0 || retValue;
    });
    $(document).on("keyup", "input[data-type='number']", function () {
    	var value = $(this).val();
        var numberReg = new RegExp('^\\d{0,}$');
        if($(this).val() && !numberReg.test( $(this).val())){
            $(this).val($(this).data("oldvalue"));
        }else {
            $(this).data("oldvalue",  $(this).val());
            
            if($(this).attr('limit-zero')){
                if(/^0+[1-9]*$/.test(+value)){
                    $(this).data("oldvalue","");
                    $(this).val("");
                }
                if(value!='-'){
                    $(this).val(+value==0?"":+value);
                }
                return ;
            }
        }
    })
    $(document).on("keypress", "input[data-type='float']", function (event) {
        var retValue = false;
        var keyCode = event.charCode == undefined ? event.keyCode : event.charCode;
        if ((keyCode >= 48 && keyCode <= 57) || keyCode == 99 || keyCode == 118) {
            retValue = true;
        } else if (keyCode == 46 || keyCode == 45) {
            retValue = $(this).val().indexOf(String.fromCharCode(keyCode)) < 0;
        }
        if (retValue) {
            $(this).data("oldvalue", $(this).val());
        }
        return keyCode == 0 || retValue;
    });
    $(document).on("keyup", "input[data-type='number'], input[data-type='float'],input[data-type='amount']", function () {
        var value = $(this).val();
        //检查是否是非数字值
        if (isNaN(value) && value !== "-") {
            $(this).val($(this).data("oldvalue"));
        } else {
            $(this).data("oldvalue", value);
        }
    });


    $(document).on("change", "input[data-type='number'],input[data-type='float'],input[data-type='amount']", function () {
        var value = $(this).val();
        //检查是否是非数字值
        var num = (+$(this).val());
        var max = $(this).attr("data-max");
        var min = $(this).attr("data-min");
        if (isNaN(value) || num > (+max) || num < (+min)) {
            $(this).val($(this).attr("data-default") || '');
        }
    });
});

// 日期输入
$(document).ready(function () {
    $(document).on('click', "input[data-type='date']", function () {
        var $this = $(this);
        var group = $this.attr("data-group");
        var options = {};
        if ($this.attr("data-default")) {
            options.startDate = $this.attr("data-default");
        }
        if ($this.attr("data-min")) {
            options.minDate = $this.attr("data-min");
        }
        if ($this.attr("data-max")) {
            options.maxDate = $this.attr("data-max");
        }
        if ($this.attr("data-fmt")) {
            options.dateFmt = $this.attr("data-fmt");
        }
        if (group) {
            var $dateInputs = $("input[data-type='date'][data-group='" + group + "']");
            var $otherInputs = $dateInputs.not($this);
            var fun;
            if ($dateInputs.index($this) === 0) {
                fun = function (dateStr) {
                    $otherInputs.attr("data-min", dateStr);
                };
                if ($otherInputs.val()) {
                    options.maxDate = $otherInputs.val();
                }
            } else {
                fun = function (dateStr) {
                    $otherInputs.attr("data-max", dateStr);
                };
                if ($otherInputs.val()) {
                    options.minDate = $otherInputs.val();
                }
            }
            options.ychanged = function () { fun($dp.cal.getNewDateStr()) };
            options.Mchanged = function () { fun($dp.cal.getNewDateStr()) };
            options.dchanged = function () { fun($dp.cal.getNewDateStr()) };
        }
        WdatePicker(options);
    });
});
//订单-海岛游-订单列表页-下载资料-弹出层
function jbox__haidaoyouxzzliao_fab() {
    $.jBox($("#order_downloadfiels_pop_o").html(), {
        title: "下载资料", buttons: { '关闭': 1 }, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: '350', width: 400
    });
    inquiryCheckBOX();
}

//订单-海岛游-订单列表页-改收款凭证-弹出层
function jbox__haidaoyougskpz_fab() {
    $.jBox($("#update_certificate_receipts").html(), {
        title: "收款凭证", buttons: { '关闭': 1 }, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: '350', width: 625
    });
    inquiryCheckBOX();
}


//订单-海岛游-订单详情-弹出层
function jbox__haidaoyouddxiangq_fab() {
    $.jBox($("#finance_zhuangtai_view").html(), {
        title: "收款凭证", buttons: { '关闭': 1 }, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: '350', width: 800
    });
    inquiryCheckBOX();
}

// 列表全选

$(document).ready(function () {
    $(document).on('change', "input:checkbox[data-group]", function () {
        var $this = $(this);
        var group = $this.attr("data-group");
        var checked = $this.prop("checked");
        var $chks = $("input:checkbox[data-group='" + group + "']:visible");
        if ($this.is(".chkAll")) {
            $chks.prop("checked", checked);
        } else {
            $chks.filter(".chkAll").prop("checked", !$chks.not(".chkAll,:checked").length);
        }
    });
});



//订单-海岛游-退款保存-弹出层
function jbox__haidaoyouddtuikbaoc_fab() {
    $.jBox($("#jbox_batch_change_price_bg002").html(), {
        title: "提醒", buttons: { '确认': 1 }, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: '350', width: 200
    });
    inquiryCheckBOX();
}


//订单-酒店---海岛游-弹出层
function jbox__order_hotel_orderlist_fab() {
  $.jBox($("#order_hotel_orderlist").html(), {
      title: "凭证列表", buttons: { '确认': 1 }, submit: function (v, h, f) {
          if (v == "1") {
              return true;
          }
      }, height: '350', width: 200
  });
  inquiryCheckBOX();
}
/**
* Created by kuang on 2015/6/14. 对金额符号的处理
*/
$(document).ready(function(){
  $(".amt").each(function (index,domElement) {
      var $this = $(domElement);
      var currencyCode = $this.attr("amt-code");
      $this.wrap("<span></span>");
      $this.parent().css({position:"relative",display:"inline-block"});
      $this.parent().prepend("<em class='gray14' style='position: absolute;top:5px;left: 5px;'>"+currencyCode+"<em>");
      $this.css({paddingLeft:"15px"});
      $this.attr("currency",currencyCode);
  });
});

function resetCurrency(){
    $(".amt").each(function (index,domElement) {
        var $this = $(domElement);
        if($this.is("[currency]"));
        $this.wrap("<span></span>");
        var currencyCode = $this.attr("amt-code");
        $this.parent().css({position:"relative",display:"inline-block"});
        $this.parent().prepend("<em class='gray14' style='position: absolute;top:5px;left: 5px;'>"+currencyCode+"<em>");
        $this.css({paddingLeft:"15px"});
        $this.attr("currency",currencyCode);
    });
}

//订单-酒店--列表----凭证弹出层
function jbox__certificate_of_receipts_fab() {
  $.jBox($("#certificate_of_receipts").html(), {
      title: "凭证列表", buttons: { '确定': 1 }, submit: function (v, h, f) {
          if (v == "1") {
              return true;
          }
      }, height: '350', width: 464
  });
  inquiryCheckBOX();
}


//订单收款确认-其他收入收款驳回确认弹出层
function jbox__shoukuanqueren_qitashoukuan_fab() {
  $.jBox($("#rejected_collect_confirms").html(), {
      title: "驳回确认", buttons: { '取消': 1,'确认': 2 }, submit: function (v, h, f) {
          if (v == "1") {
              return true;
          }
      }, height: '597', width: 600
  });
  inquiryCheckBOX();
}



//订单收款确认-其他收入收款撤销弹出层
function jbox__shoukuanqueren_chexiao_fab() {
  $.jBox($("#rejected_cancel_confirms").html(), {
      title: "系统提示", buttons: { '取消': 1,'确定': 2 }, submit: function (v, h, f) {
          if (v == "1") {
              return true;
          }
      }, height: '597', width: 300
  });
  inquiryCheckBOX();
}
//销售和下单人切换
function switchSalerAndPicker() {
    //点击团号
    $(".activitylist_bodyer_table").on("click", ".order-saler-title", function() {
        $(this).addClass("on").siblings().removeClass('on');
        var $table=$(this).parents('table:first');
        $table.find('.order-saler').addClass('onshow');
        $table.find('.order-picker').removeClass('onshow');
    });
    //点击产品
    $(".activitylist_bodyer_table").on("click", ".order-picker-title", function() {
        $(this).addClass("on").siblings().removeClass('on');
        var $table=$(this).parents('table:first');
        $table.find('.order-saler').removeClass('onshow');
        $table.find('.order-picker').addClass('onshow');
    });
}

//删除数组中对应的元素
function removeItem(arr,item){
    for(var i= 0,len=arr.length;i<len;i++){
        if(arr[i]==item){
            arr.splice(i,1);
            return;
        }
    }
}

//审核优化-主配置-弹出层
function jbox__audit_optimization_fab() {
  $.jBox($("#pop-department-pro-flow").html(), {
      title: "主配置界面", buttons: {}, submit: function (v, h, f) {
          if (v == "1") {
              return true;
          }
      }, height: '500', width: '900'
  });
  inquiryCheckBOX();
}
//删除或批量删除时重排序号
function resetIndex($obj){
    $obj.find('[name="product"]').each(function(index){
        $(this).find('input[name="groupCheck"]').next().text(index+1);
    });
}

//审核优化-职务管理-弹出层
function jbox__pop_add_duty_fab() {
   var $pop= $.jBox($("#pop_add_duty").html(), {
        title: "新增职务", buttons: {'取消': 1, '保存': 2}, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
            if(v == "2"){
            	var type = $pop.find("#orderShowType option:selected").val();
            	var name = $pop.find("#jobName").val();
            	if(name==null || name==''){
            		top.$.jBox.tip('请输入职务名称!','error');
            		return;
            	}
            	if( name.replace(/^\s+|\s+$/g,"")==''){
            		top.$.jBox.tip('职务名称不能为空!','error');
            		return;
            	}
            	
            	$.ajax({
            		cache : true,
            		type : "POST",
            		url :g_context_url+ "/sys/department/checkRepeat4JobManagement",
            		data:{ 
            		    "name":name,
            		    "uuid":""
            			}, 
            		async: false,
            		success: function(msg){
            			if(msg == "success"){
            				$.ajax({
                        		cache : true,
                        		type : "POST",
                        		url :g_context_url+ "/sys/department/saveJobManagement",
                        		data:{ 
                        		    "type":type,
                        		    "name":name
                        			}, 
                        		async: false,
                        		success: function(msg){
                        			$("#searchForm").submit();
                        		}
                        	});
            			}else{
            				top.$.jBox.tip('职务名称已存在!','error');
            				return;
            			}
            		}
            	});
            	
            }
        }, height:'500', width:'900'
    });
    inquiryCheckBOX();
}

function jbox__pop_mod_duty_fab(type,name,uuid) {
	var html = '<div style="margin-top:20px;padding:0 10px;overflow:scroll;" id="borrowWin">';
	html += '<div class="pop_add_duty"><p><label for="">职务类别：</label><select name="orderShowType1" id="orderShowType1">';
	if(type==0){
		html +='<option value="0" selected="selected">计调类</option>';
		html +='<option value="1">销售类</option>';
		html +='<option value="2">财务类</option>';
		html +='<option value="3">管理类</option>';
		html +='<option value="5">行政类</option>';
	}
	if(type==1){
		html +='<option value="0">计调类</option>';
		html +='<option value="1" selected="selected">销售类</option>';
		html +='<option value="2">财务类</option>';
		html +='<option value="3">管理类</option>';
		html +='<option value="5">行政类</option>';
	}
	if(type==2){
		html +='<option value="0">计调类</option>';
		html +='<option value="1">销售类</option>';
		html +='<option value="2" selected="selected">财务类</option>';
		html +='<option value="3">管理类</option>';
		html +='<option value="5">行政类</option>';
	}
	if(type==3){
		html +='<option value="0">计调类</option>';
		html +='<option value="1">销售类</option>';
		html +='<option value="2">财务类</option>';
		html +='<option value="3" selected="selected">管理类</option>';
		html +='<option value="5">行政类</option>';
	}
//	if(type==4){
//		html +='<option value="0">计调类</option>';
//		html +='<option value="1">销售类</option>';
//		html +='<option value="2">财务类</option>';
//		html +='<option value="3">管理类</option>';
//		html +='<option value="4" selected="selected">签证类</option>';
//		html +='<option value="5">行政类</option>';
//	}
	if(type==5){
		html +='<option value="0">计调类</option>';
		html +='<option value="1">销售类</option>';
		html +='<option value="2">财务类</option>';
		html +='<option value="3">管理类</option>';
		html +='<option value="5" selected="selected">行政类</option>';
	}
	html += '</select></p>';
	html += '<p><label>职务名称：</label>';
	html += '<input value="'+name+'" class="txtPro inputTxt" id="jobName1" />';
	html += '</p></div></div>';
	
	$.jBox(html, { title: "修改职务",buttons:{'取消': 1, '保存': 2}, submit:function(v, h, f){
		if (v == "1") {
            return true;
        }
        if(v == "2"){
        	var type = $("#orderShowType1 option:selected").val();
        	var name = $("#jobName1").val();
        	if(name==null || name==''){
        		top.$.jBox.tip('请输入职务名称!','error');
        		return;
        	}
        	if( name.replace(/^\s+|\s+$/g,"")==''){
        		top.$.jBox.tip('职务名称不能为空!','error');
        		return;
        	}
        	
        	$.ajax({
        		cache : true,
        		type : "POST",
        		url :g_context_url+ "/sys/department/checkRepeat4JobManagement",
        		data:{ 
        		    "name":name,
        		    "uuid":uuid
        			}, 
        		async: false,
        		success: function(msg){
        			if(msg == "success"){
        				$.ajax({
        	        		cache : true,
        	        		type : "POST",
        	        		url :g_context_url+ "/sys/department/modifyJobManagement",
        	        		data:{ 
        	        		    "type":type,
        	        		    "name":name,
        	        		    "uuid":uuid
        	        			}, 
        	        		async: false,
        	        		success: function(msg){
        	        			$("#searchForm").submit();
        	        		}
        	        	});
        			}else{
        				top.$.jBox.tip('职务名称已存在!','error');
        				return;
        			}
        		}
        	});
        	
        }
	},height:'500', width:'900'});
	
//	var $pop= $.jBox($("#pop_add_duty").html(), {
//	        title: "修改职务", buttons: {'取消': 1, '保存': 2}, submit: function (v, h, f) {
//	        	if (v == "1") {
//	                return true;
//	            }
//	            if(v == "2"){
//	            	var type = $pop.find("#orderShowType option:selected").val();
//	            	var name = $pop.find("#jobName").val();
//	            	$.ajax({
//	            		cache : true,
//	            		type : "POST",
//	            		url :"/trekiz_wholesaler_tts/a/sys/department/modifyJobManagement",
//	            		data:{ 
//	            		    "type":type,
//	            		    "name":name,
//	            		    "uuid":uuid
//	            			}, 
//	            		async: false,
//	            		success: function(msg){
//	            			$("#searchForm").submit();
//	            		}
//	            	});
//	            }
//	        }, height:'500', width:'900'
//	    });
	    inquiryCheckBOX();
	}
//审核优化-职务管理-弹出层
function jbox__pop_del_duty_fab(uuid) {
    $.jBox($("#pop_del_duty").html(), {
        title: "提示", buttons: {'取消': 1, '删除': 2}, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
            if(v == "2"){
            	$.ajax({
            		cache : true,
            		type : "POST",
            		url :g_context_url+"/sys/department/deleteJobManagement",
            		data:{ 
            		    "uuid":uuid
            			}, 
            		async: false,
            		success: function(msg){
            			$("#searchForm").submit();
            		}
            	});
            }
        }, height: '500', width: '900'
    });
    inquiryCheckBOX();
}



//审核优化-选择审批人-弹出层
//function jbox__pop_select_duty_fab() {
//  $.jBox($("#pop_select_duty_fab").html(), {
//      title: "选择职务", buttons: {'取消': 1, '确定': 2}, submit: function (v, h, f) {
//          if (v == "1") {
//              return true;
//          }
//      }, height: '342', width: '320'
//  });
//  inquiryCheckBOX();
//}
//审核优化-选择审批人-凭证弹出层
//审核优化-选择审批人-批量审批弹出层

/*S流程为完成，删除提示框*/
function jbox__delete_no_Tip_fab() {
  $.jBox($("#delete-no-Tip").html(), {
      title: "", buttons: { '驳回': 0}, submit: function (v, h, f) {
          if (v == "1") {
              return true;
          }
      }, height: 250, width: 350
  });
  inquiryCheckBOX();
}

/*S审批优化-预览-弹窗图片*/
function jbox_approve_view_img_pop(url) {
	$("#approve_view_img_pop img").attr("src",url);
	$.jBox($("#approve_view_img_pop").html(), {
		title: "预览", submit: function (v, h, f) {
			if (v == "1") {
				return true;
			}
		}, height: 600, width: 900
	});
	inquiryCheckBOX();
}
/*E审批优化-预览-弹窗图片*/

/*C476--打开下载附件窗口start*/
/**
 * 成本付款下载附件
 * @param ctx
 * @param docId 文件Id
 * @param hasPermission 是否显示删除按钮，true显示，false不显示
 * */
function showDownloadWin(ctx,docId,hasPermission){
	if(!docId){
		$.jBox.tip("无附件");
		console.log("无附件:" + docId);
		return false;
	}
    $.jBox('iframe:'+ctx+"/sys/docinfo/getFile?docId="+docId,{
        title:'下载附件',
        width:500,
        height:400,
        buttons:{'关闭':true},
        loaded:function(h){
            //消除滚动条
            $(".jbox-content", top.document).css("overflow-y","hidden");
            if(hasPermission){
                $(h.find("iframe")[0].contentDocument).find('li').append('<em class="ico-del" title="删除" onclick="deleteAttachment(this)"></em>');
            }
        }
    })
}

function deleteAttachment(ctx,obj,docId,costId){
    $.jBox.confirm('确定删除该附件么','系统提示', function (v, h, f){
        if(v=='ok'){
            $(obj).parent().remove();
            $.ajax({
                type:"POST",
                url:ctx+"/cost/common/deleteCostVoucher",
                data:{docId:docId,costId:costId},
                success:function(msg) {
                }
            });
        }
    });
}

//获取URL参数的方法
function GetRequest() {
    var url = location.search; //获取url中"?"符后的字串
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for(var i = 0; i < strs.length; i ++) {
            theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
        }
    }
    return theRequest;
}

/* C492操作已上传附件*/
function  inquiryCheckBOXLocal(){
    $(document).on("click",".seach_checkbox_user em",function(e){
        $(this).parent().parent().remove();
        stopDefault(e);
    })
    $(document).on("hover",".seach_checkbox_user a",function(e){
        $(this).append("<em></em>")
    })
    $(document).on("mouseleave",".seach_checkbox_user a",function(e){
        $(this).parent().find('em').remove();
    })
}
/* C492选中项操作*/

	function stopBubble(e) {
	//如果提供了事件对象，则这是一个非IE浏览器
	    if ( e && e.stopPropagation )
	    //因此它支持W3C的stopPropagation()方法
	        e.stopPropagation();
	    else
	    //否则，我们需要使用IE的方式来取消事件冒泡
	        window.event.cancelBubble = true;
	}
	//阻止浏览器的默认行为
	function stopDefault( e ) {
	    //阻止默认浏览器动作(W3C)
	    if ( e && e.preventDefault )
	        e.preventDefault();
	    //IE中阻止函数器默认动作的方式
	    else
	        window.event.returnValue = false;
	    return false;
	}
	/* C492选中项操作*/

	//删除或批量删除时重排序号
	function resetIndex($obj){
	    $obj.find('[name="product"]').each(function(index){
	        $(this).find('input[name="groupCheck"]').next().text(index+1);
	    });
	}
	
	/**
	 * 获取js属性信息
	 * @param attribute
	 * @returns
	 */
	function getTextByAttribute(attribute) {
		if(attribute == null || attribute == undefined) {
			return "";
		} else {
			return attribute;
		}
	}

/*C463为团期添加备注--start*/
$(document).on('click', '.groupNote', function () {
    $(this).parents('tr:first').next().show();
});
$(document).on('click', '.clearNotes', function () {
    $(this).siblings().val('');
});
$(document).on('click', '.unSaveNotes', function () {
    $(this).parent().parent().hide();
})
$(document).on('click', '.unSaveNotes01', function () {
    $(this).parent().parent().hide();
})
//新增同日团期
$('.addSameDayGroup').click(function () {
    var $currentTbody = $(this).parents('tbody:first');
    var $newTbody = $currentTbody.clone();
    $newTbody.find('.addSameDayGroup').remove();
    $newTbody.find('tr:first td:last dl').removeClass('handle-on');
    $newTbody.find('tr:first td:last dd').removeClass('block');
    $currentTbody.after($newTbody);
});
//详情状态下团期备注的显示情况
$('.detailNoteHide').click(function(){
    $(this).parents('tr:first').hide();
})
/*C463为团期添加备注--end*/

function getPostData(obj){
    var postData = {
        requestType:"mtour data",
        param:base64encode(JSON.stringify(obj))
    }
    return postData;
}

/**
 * 通过指定属性和属性值 获取数组中的该对象
 * @param arr 对象数组
 * @param propName 属性名
 * @param propValue 属性值
 * @returns {*} 指定属性的对象
 */
function getObjectByProp(arr, propName, propValue) {
    if (!arr || arr.length == 0) {
        return null;
    }
    for (var index in arr) {
        if (arr[index][propName] == propValue) {
            return arr[index];
        }
    }
}

/*C147签证国家->编辑->jbox__editvisa->start-tgy*/
  function jbox__editvisa(obj,urlContext) {
	
	
	//$(obj).prev().find("tbody").find("tr").eq(0).attr("gvcid")
	
	var gvcids = '';
	
	//-----获得group_control_visa表的id数组-start
	var arrGCVIds=[];
	 $(obj).prev().find("tbody").find("tr").each(function(index,element){
		 arrGCVIds[index]=$(element).attr("gvcid");
	 });
	//-----获得group_control_visa表的id数组-end
	/*$(obj).prev().find("tbody").find("tr").each(function(index,element){
		
		gvcids+=$(element).attr("gvcid")+",";
		
	});*/
	   var urlValue=urlContext+"/grouphandle/getGroupControlVisaInfo?arrGCVIds="+arrGCVIds;
	   var type="POST";
		$.ajax({
			url:urlValue,
			type:type,
			success:function(resultMap){
				if(resultMap.result=='success'){
					var groupHandleVisaInfos=resultMap.groupHandleVisaList;
					//*********************拼接147需求的编辑浮窗-start*************************//
					var html='<div id="edit_visa_o" class="inline-block">';
					    html+='<div class="edit_visa" >';
					    html+='<table class="table activitylist_bodyer_table orderlist-list" style="margin:0 auto;">';
					    html+='<thead>';
					    html+='<tr>';
					    html+='<th class="tc" width="7%">游客</th>';
					    html+='<th class="tc" width="7%">护照号</th>';
					    html+='<th class="tc" width="7%">国家</th>';
					    html+='<th class="tc" width="7%">类型</th>';
					    html+='<th class="tc" width="7%">领区</th>';
					    html+='<th class="tc" width="7%">办签单位</th>';
					    html+='<th class="tc" width="7%">签证状态</th>';
					    html+='<th class="tc"width="7%">预计约签时间<br>实际约签时间</th>';
					    html+='<th class="tc"width="7%">送签时间</th>';
					    html+='<th class="tc"width="7%">出签时间</th>';
					    html+='<th class="tc"width="7%">补资料时间</th>';
					    html+='</tr>';
					    html+='</thead>';
					    html+='<tbody>';
					    html+='<tr>';
					    //表格内数据-s
					    //游客和护照号
					   for(var i=0;i<groupHandleVisaInfos.length;i++){
						//隐含的字段用于存储group_control_visa表的id  
						html+='<input type="hidden" id="'+groupHandleVisaInfos[i].id+'" value="'+groupHandleVisaInfos[i].id+'" name="groupControlVisaId">';  
					    if(i==0){	//对于游客和护照号表列,只在第一次循环时候拼接,因为其本身跨行
					    html+='<td rowspan="'+groupHandleVisaInfos.length+'" class="tc">'+groupHandleVisaInfos[i].traveler_name+'</td>';
					    //***********处理护照号为null的情况-s************//
					     if(groupHandleVisaInfos[i].passport_num!=null){//护照号不为空时展示护照号
					     html+='<td rowspan="'+groupHandleVisaInfos.length+'" class="tc">'+groupHandleVisaInfos[i].passport_num+'</td>';
					     }else{//护照号为空时,不显示护照号  
					     html+='<td rowspan="'+groupHandleVisaInfos.length+'" class="tc"></td>';	 
					     }
					    //***********处理护照号为null的情况-e************//
					    }
					    //国家
					    html+='<td class="tc">'+groupHandleVisaInfos[i].visa_country_name+'</td>';
					    //类型
					    html+='<td class="tc">'+groupHandleVisaInfos[i].visa_type_name+'</td>';
					    //领区
					    html+='<td class="tc">'+groupHandleVisaInfos[i].visa_consulardistric_name+'</td>';
					    //办签单位
					    if(groupHandleVisaInfos[i].visa_handle_unit==null){
					      html+='<td class="tc"><input value="" class="inputTxt inputTxtlong"  name="visaHandleUnit"></td>';
					    }else{
					      html+='<td class="tc"><input value="'+groupHandleVisaInfos[i].visa_handle_unit+'" class="inputTxt inputTxtlong"  name="visaHandleUnit"></td>';
					    }
					    //签证状态
					    html+='<td class="tc"><select  name="visaStatus" style="width:100%;">';
					   
					    if(groupHandleVisaInfos[i].visa_stauts==-1){
					    	html+='<option value="-1" selected>请选择</option>';
					    }else{
					    	html+='<option value="-1">请选择</option>';
					    }
					    if(groupHandleVisaInfos[i].visa_stauts==0){
					    	html+='<option value="0" selected>未送签</option>';
					    }else{
					    	html+='<option value="0" >未送签</option>';
					    }
					    if(groupHandleVisaInfos[i].visa_stauts==1){
					    	 html+='<option value="1" selected>送签</option>';
					    }else{
					    	 html+='<option value="1" >送签</option>';
					    }
					    if(groupHandleVisaInfos[i].visa_stauts==2){
					    	 html+='<option value="2" selected>约签</option>';
					    }else{
					    	 html+='<option value="2" >约签</option>';
					    }
					    if(groupHandleVisaInfos[i].visa_stauts==3){
					    	html+='<option value="3" selected>出签</option>';
					    }else{
					    	html+='<option value="3" >出签</option>';
					    }
					    if(groupHandleVisaInfos[i].visa_stauts==4){
					    	html+='<option value="4" selected>未约签</option>';
					    }else{
					    	html+='<option value="4" >未约签</option>';
					    }
					    if(groupHandleVisaInfos[i].visa_stauts==5){
					    	html+='<option value="5" selected>已撤签</option>';
					    }else{
					    	html+='<option value="5" >已撤签</option>';
					    }
					    if(groupHandleVisaInfos[i].visa_stauts==7){
					    	 html+='<option value="7" selected>拒签</option>';
					    }else{
					    	 html+='<option value="7" >拒签</option>';
					    }
					    if(groupHandleVisaInfos[i].visa_stauts==8){
					    	 html+='<option value="8" selected>调查</option>';
					    }else{
					    	 html+='<option value="8" >调查</option>';
					    }
					    if(groupHandleVisaInfos[i].visa_stauts==9){
					    	html+='<option value="9" selected>续补资料</option>';
					    }else{
					    	html+='<option value="9" >续补资料</option>';
					    }
					    html+='</select>';
					    html+='</td>';
					    //预计约签时间&实际约签时间
					    if(groupHandleVisaInfos[i].about_signing_time==null){//处理预计约签时间为null的情况
					    	groupHandleVisaInfos[i].about_signing_time='';
					    }
					    if(groupHandleVisaInfos[i].signing_time==null){//处理实际约签时间为null的情况
					    	groupHandleVisaInfos[i].signing_time='';
					    }
					    html+='<td class="tc"><span class="fbold"style="color:#eb0301">'+groupHandleVisaInfos[i].about_signing_time+'</span><br><input readonly="" onclick="WdatePicker()" value="'+groupHandleVisaInfos[i].signing_time+'" class="inputTxt dateinput"  name="signingTime"> </td>';
					    //送签时间
					    if(groupHandleVisaInfos[i].visa_delivery_time==null){//处理送签时间为null的情况
					    	groupHandleVisaInfos[i].visa_delivery_time='';
					    }
					    html+='<td class="tc"><input readonly="" onclick="WdatePicker()" value="'+groupHandleVisaInfos[i].visa_delivery_time+'" class="inputTxt dateinput"  name="visaDeliveryTime"></td>';
					    //出签时间
					    if(groupHandleVisaInfos[i].visa_got_time==null){ //处理出签时间为null的情况
					    	groupHandleVisaInfos[i].visa_got_time='';
					    }
					    html+='<td class="tc"><input readonly="" onclick="WdatePicker()" value="'+groupHandleVisaInfos[i].visa_got_time+'" class="inputTxt dateinput"  name="visaGotTime"></td>';
					    //补资料时间
					    if(groupHandleVisaInfos[i].supplementaryinfo_time==null){ //处理出签时间为null的情况
					    	groupHandleVisaInfos[i].supplementaryinfo_time='';
					    }
					    html+='<td class="tc"><input readonly="" onclick="WdatePicker()" value="'+groupHandleVisaInfos[i].supplementaryinfo_time+'" class="inputTxt dateinput"  name="supplementaryInfoTime"></td>';
					    html+='</tr>';
					   
					    //表格内数据-e
					    }
					    html+='</tr>';
					    html+='</tbody>';
					    html+='</table>';
					    html+='</div>';
					    html+='</div>';
					  
					    $.jBox(html, { title: "编辑签证",buttons:{'保存':1,'关闭':2}, submit:function(v, h, f){
							if (v=="2"){
								return true;//关闭浮窗
							}else{
							//*******************处理"保存"->start*****************************//
								var groupControlVisaIdValues=[];
								var visaHandleUnitsValues=[];
								var visaStatusSelectedValues=[];
								var signingTimeValues=[];
								var visaDeliveryTimeValues=[];
								var visaGotTimeValues=[];
								var supplementaryInfoTimeValues=[];
								for(var i=0;i<groupHandleVisaInfos.length;i++){
									groupControlVisaIdValues[i]=$("input:hidden[name='groupControlVisaId']")[i].value;
									//处理办签单位值为空的情况
									if($("input[name='visaHandleUnit']").eq(i).val().trim()==""){
										visaHandleUnitsValues[i]="null";
									}else{
										visaHandleUnitsValues[i]=$("input[name='visaHandleUnit']")[i].value;
									}
									visaStatusSelectedValues[i]=$("select[name='visaStatus'] option:selected")[i].value;
									//处理实际约签时间为空的情况
									if($("input[name='signingTime']").eq(i).val().trim()==""){
										signingTimeValues[i]="null";
									}else{
										signingTimeValues[i]=$("input[name='signingTime']")[i].value;
									}
									//处理送签时间为空的情况
									if($("input[name='visaDeliveryTime']").eq(i).val().trim()==""){
										visaDeliveryTimeValues[i]="null";
									}else{
										visaDeliveryTimeValues[i]=$("input[name='visaDeliveryTime']")[i].value;
									}
									//处理到签时间为空的情况
									if($("input[name='visaGotTime']").eq(i).val().trim()==""){
										visaGotTimeValues[i]="null";	
									}else{
										visaGotTimeValues[i]= $("input[name='visaGotTime']")[i].value;
									}
									//处理续补资料时间为空的情况
									if($("input[name='supplementaryInfoTime']").eq(i).val().trim()==""){
										supplementaryInfoTimeValues[i]="null";
									}else{
										supplementaryInfoTimeValues[i]=$("input[name='supplementaryInfoTime']")[i].value;     
									}
									
								}
								
								$.ajax({                 
										type: "POST",                 
										url:urlContext+ "/grouphandle/editPartVisaInfo",                 
										data:{
											"groupControlVisaIdValues":groupControlVisaIdValues.toString(),
											"visaHandleUnitsValues":visaHandleUnitsValues.toString(),
											"visaStatusSelectedValues":visaStatusSelectedValues.toString(),
											"signingTimeValues":signingTimeValues.toString(),
											"visaDeliveryTimeValues":visaDeliveryTimeValues.toString(),
											"visaGotTimeValues":visaGotTimeValues.toString(),
											"supplementaryInfoTimeValues":supplementaryInfoTimeValues.toString()
										},                
										success: function(result) { 
											if(result=='success'){
												top.$.jBox.tip('保存成功');
												window.location.reload();//自动刷新当前页面,查看更新后的结果
												return true;
											}else{
												top.$.jBox.tip('保存失败!','error');
											}
											
										}             
									});  
							//*******************处理"保存"->end*******************************//
							}
						},height:400,width:1400});
					  //*********************拼接147需求的编辑浮窗-end*************************// 
				}
			}
		}); 
	
	//alert(gvcids);
	   
}
/*C147签证国家->编辑->jbox__editvisa->end-tgy*/

//299-start
function pricePlan(obj) {
	if (!$(obj).parents('tr:first').siblings('.pricePlanContainer').is(':hidden')) {
//                if (!$('.pricePlanContainer').is(':hidden')) {
		$(obj).parents('tr:first').siblings('.pricePlanContainer').hide();
		$(obj).html('展开价格方案');
	}
	else {
		$(obj).parents('tr:first').siblings('.pricePlanContainer').show();
		$(obj).html('收起价格方案');
	}
}

function pricePlanPop() {
	$.jBox($('.pricePlan_container').html(), {
				title    : "价格方案", buttons: {'关闭': 1}, submit: function (v, h, f) {
					if (v == "1") {

					}
				}, height: 400, width: 1100
			}
	);
}
//299-end
/*C147签证国家->编辑->jbox__editvisa->end-tgy*/
Array.prototype.remove = function (val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};

//137 start 返佣付款确认付款弹窗
function jbox_paymentconfirm(ctx, review_uuid, status, flag, prdtype) {
	$.ajax({
		type: 'POST',
		url: ctx + '/costNew/payManager/getRebateInfo',
		data: {
			reviewUuid : review_uuid, prdtype : prdtype
		},
		success: function (data) {
			html = 	"<div id='payment_confirm_pop_o'>" +
				"<div class='payment_confirm_pop'>" +
				"<table class='table activitylist_bodyer_table orderlist-list' style='margin:0 auto;'>" +
				"<thead>" +
				"<tr>" +
				"<th style='display:none;'>rebatesId</th>" +
				"<th class='tc' width='7%'>付款金额</th>" +
				"<th class=tc' width='4%'></th>" +
				"<th class='tc' width='5%'>汇率</th>" +
				"<th class='tc' width='4%'></th>" +
				"<th class='tc' width='15%'>转换后金额</th>" +
				"<th class='tc' width='7%'>出纳确认时间</th>" +
				"</tr>" +
				"</thead>" +
				"<tbody>" +
				"<tr>" +
				"<td  style='display:none;'><input name='rebatesId' value = '" + data.rebatesId + "'/></td>" +
				"<td class='tc payment'>"+data.currencyMark+"<span name='payment'>"+parseFloat(data.rebatesDiff).toFixed(2)+"</span></td>" +
				"<td class='tc'>x</td>" +
				"<td class='tc'><input class='rate inputTxt inputTxtlong' value='"+data.rate+"' onkeyup='validNum(this)' onafterpaste='validNum(this)' name='rate'";
			if("¥" == data.currencyMark) {
				html += "readOnly='true'";
			}
			html += "></td>" +
				"<td class='tc'>=</td>" +
				"<td class='afterexchange tc'>¥<span name='afterexchange'>"+parseFloat(data.rebatesDiff*data.rate).toFixed(2)+"</span></td>" +
				"<td class='tc'><input name='cashierConfirmDate' class='inputTxt dateinput' value='"+new Date().Format("yyyy-MM-dd")+"' readonly onClick='WdatePicker()'></td>" +  // 出纳确认日期
				"</tr>" +
				"<tbody>" +
				"</table>" +
				"</div>" +
				"</div>";

			var $pop = $.jBox(html, {
				title : "付款确认", buttons: {'确认': 1, '取消': 2}, submit: function (v, h, f) {
					if (v == "1") {
						if ($pop.find('.rate').val() == "") {
							top.$.jBox.info("汇率不能为空", "提示");
							return false;
						}

						if ($pop.find('.rate').val() == 0) {
							top.$.jBox.info("汇率不能为0", "提示");
							return false;
						}
						
						if($pop.find("input[name='cashierConfirmDate']").val() == "") {
							top.$.jBox.info("出纳确认时间不能为空", "提示");
							return false;
						}

						var rate = $pop.find('.rate').val();
						var price = $pop.find("span[name='afterexchange']").html();
						var rebatesId = $pop.find("input[name='rebatesId']").val();
						var temp_CashierConfirmDate = $pop.find("input[name='cashierConfirmDate']").val();
						$.ajax({
							type:"POST",
							url:ctx+"/review/common/web/confimOrCancelPay",
							data:{reviewId:review_uuid,status:status,rate:rate,price:price,rebatesId:rebatesId,flag:202, cashierConfirmDate:temp_CashierConfirmDate},
							success:function(data){
								if(data.flag){
									$.jBox.tip('操作成功', 'success');
									$("#searchForm").submit();
								}else{
									$.jBox.tip('操作失败,原因:' + data.msg, 'error');
									return false;
								}
							}
						})
					}
				}, height: '500', width: 500
			});

			$pop.find("input[name='rate']").keyup(function () {
				var rate = $pop.find("input[name='rate']").val();
				var payment = $pop.find("span[name='payment']").html();
				var afterexchange = payment * rate;
				$pop.find("span[name='afterexchange']").html(parseFloat(afterexchange).toFixed(2));
			});
		}
	});

	inquiryCheckBOX();
}

//137 start 返佣付款确认付款弹窗
function jbox_paymentconfirm2(ctx, review_uuid, status, flag, prdtype) {
	$.ajax({
		type: 'POST',
		url: ctx + '/costNew/payManager/getRebateInfo2',
		data: {
			reviewUuid : review_uuid, prdtype : prdtype
		},
		success: function (data) {
			html = 	"<div id='payment_confirm_pop_o'>" +
				"<div class='payment_confirm_pop'>" +
				"<table class='table activitylist_bodyer_table orderlist-list' style='margin:0 auto;'>" +
				"<thead>" +
				"<tr>" +
				"<th style='display:none;'>uuid</th>" +
				"<th style='display:none;'>rebatesId</th>" +
				"<th class='tc' width='7%'>付款金额</th>" +
				"<th class=tc' width='4%'></th>" +
				"<th class='tc' width='5%'>汇率</th>" +
				"<th class='tc' width='4%'></th>" +
				"<th class='tc' width='15%'>转换后金额</th>" +
				"<th class='tc' width='7%'>出纳确认时间</th>" +
				"</tr>" +
				"</thead>" +
				"<tbody id='context'>";
			for(var i = 0; i < data.length; i++) {
				html += "<tr>" +
					"<td  style='display:none;'><input name='uuid' value = '" + data[i].id + "'/></td>" +
					"<td  style='display:none;'><input name='rebatesId' value = '" + data[i].rebatesId + "'/></td>" +
					"<td class='tc payment'>"+data[i].currencyMark+"<span name='payment'>"+parseFloat(data[i].rebatesDiff).toFixed(2)+"</span></td>" +
					"<td class='tc'>x</td>"+
					"<td class='tc'><input class='rate inputTxt inputTxtlong' value='"+data[i].rate+"' onkeyup='validNum(this)' onafterpaste='validNum(this)' name='rate'";
				
				if("¥" == data[i].currencyMark) {
					html += "readOnly='true'";
				}
				
				html += "></td>" +
					"<td class='tc'>=</td>" +
					"<td class='afterexchange tc'>¥<span name='afterexchange'>"+parseFloat(data[i].rebatesDiff*data[i].rate).toFixed(2)+"</span></td>";
				
				if(i==0) {
					html += "<td class='tc' rowspan='"+data.length+"'><input name='cashierConfirmDate' class='inputTxt dateinput' value='"+new Date().Format("yyyy-MM-dd")+"' readonly onclick='WdatePicker()'></td>";  // 出纳确认日期
				}
				html += "</tr>";
			}

			html += "<tbody>" +
				"</table>" +
				"</div>" +
				"</div>";

			var $pop = $.jBox(html, {
				title    : "付款确认", buttons: {'确认': 1, '取消': 2}, submit: function (v, h, f) {
					if (v == "1") {
						if ($pop.find('.rate').val() == "") {
							top.$.jBox.info("汇率不能为空", "提示");
							return false;
						}

						if ($pop.find('.rate').val() == 0) {
							top.$.jBox.info("汇率不能为0", "提示");
							return false;
						}
						
						if($pop.find("input[name='cashierConfirmDate']").val() == "") {
							top.$.jBox.info("出纳确认时间不能为空", "提示");
							return false;
						}
						
						var datas = new Array();
						var dn = 0;
						var errmsg = "";
						var review_uuid = $("input[name='uuid']")[0].value;
						var cashierConfirmDate = $("input[name='cashierConfirmDate']")[0].value;
						
						$('#context tr').each(function (){							
							var rebatesId = $("input[name='rebatesId']")[dn].value;
							var rate = $("input[name='rate']")[dn].value;
							var price = $("span[name='afterexchange']")[dn].firstChild.data;
							
							if((rate == null || rate == "") && errmsg == ""){
								errmsg += "汇率不能为空";
							}
							if((rate == 0) && errmsg == ""){
								errmsg += "汇率不能为0";
							}

							var s = new Object();
							s.rebatesId = rebatesId;
							s.rate = rate;
							s.price = price;
							datas[dn] = s;
							dn++;
						});
						if(errmsg != ""){
							$.jBox.tip(errmsg);
							return false;
						}

						$.ajax({
							type:"POST",
							url:ctx+"/review/common/web/confimOrCancelPay2",
							data : {datas:JSON.stringify(datas), prdtype:prdtype, flag:202, uuid:review_uuid, payConfirmDate:cashierConfirmDate},
							success:function(data){
								if(data){
									$.jBox.tip('操作成功', 'success');
									$("#searchForm").submit();
								}else{
									$.jBox.tip('操作失败,原因:' + data.msg, 'error');
									return false;
								}
							}
						})
					}
				}, height: '500', width: 500
			});

			$pop.find("input[name='rate']").keyup(function () {
				$pop.find("span[name='afterexchange']").each(function(){
					var rate = $(this).parent().prev().prev().find("input[name='rate']").val();
					var payment = $(this).parent().prev().prev().prev().prev().find("span").text();
					var afterexchange = payment * rate;
					$(this).html(parseFloat(afterexchange).toFixed(2));
				});
			});
		}
	});

	inquiryCheckBOX();
//137 END 确认付款弹窗
}

//137 start 返佣付款确认付款弹窗--旧数据
function jbox_paymentconfirmold(ctx, review_uuid, type, status, flag, prdtype) {
	$.ajax({
		type: 'POST',
		url: ctx + '/costNew/payManager/getOldRebateInfo',
		data: {
			reviewUuid : review_uuid, prdtype : prdtype
		},
		success: function (data) {
			html = 	"<div id='payment_confirm_pop_o'>" +
				"<div class='payment_confirm_pop'>" +
				"<table class='table activitylist_bodyer_table orderlist-list' style='margin:0 auto;'>" +
				"<thead>" +
				"<tr>" +
				"<th class='tc' width='7%'>付款金额</th>" +
				"<th class=tc' width='4%'></th>" +
				"<th class='tc' width='5%'>汇率</th>" +
				"<th class='tc' width='4%'></th>" +
				"<th class='tc' width='15%'>转换后金额</th>" +
				"<th class='tc' width='7%'>出纳确认时间</th>" +
				"</tr>" +
				"</thead>" +
				"<tbody>" +
				"<tr>" +
				"<td class='tc payment'>"+data.currencyMark+"<span name='payment'>"+parseFloat(data.rebatesDiff).toFixed(2)+"</span></td>" +
				"<td class='tc'>x</td>" +
				"<td class='tc'><input class='rate inputTxt inputTxtlong' value='"+data.rate+"' onkeyup='validNum(this)' onafterpaste='validNum(this)' name='rate'";
			if("¥" == data.currencyMark) {
				html += "readOnly='true'";
			}
			html += "></td>" +
				"<td class='tc'>=</td>" +
				"<td class='afterexchange tc'>¥<span name='afterexchange'>"+parseFloat(data.rebatesDiff*data.rate).toFixed(2)+"</span></td>" +
				"<td class='tc'><input name='cashierConfirmDate' class='inputTxt dateinput' value='"+new Date().Format("yyyy-MM-dd")+"' readonly onClick='WdatePicker()'></td>" +  // 出纳确认日期
			"</tr>" +
			"<tbody>" +
			"</table>" +
			"</div>" +
			"</div>";

			var $pop = $.jBox(html, {
				title : "付款确认", buttons: {'确认': 1, '取消': 2}, submit: function (v, h, f) {
					if (v == "1") {
						if ($pop.find('.rate').val() == "") {
							top.$.jBox.info("汇率不能为空", "提示");
							return false;
						}
						if ($pop.find('.rate').val() == 0) {
							top.$.jBox.info("汇率不能为0", "提示");
							return false;
						}
						
						if($pop.find("input[name='cashierConfirmDate']").val() == "") {
							top.$.jBox.info("出纳确认时间不能为空", "提示");
							return false;
						}

						var rate = $pop.find('.rate').val();
						var cashierConfirmDate = $pop.find("input[name='cashierConfirmDate']").val();
						$.ajax({
							type:"POST",
							url:ctx+"/orderCommon/manage/confirmPay",
							data:{id:review_uuid,type:type,status:1,rate:rate,flag:201,orderType:prdtype,confirmCashierDate:cashierConfirmDate},
							success:function(data){
								if(data.flag){
									$.jBox.tip('操作成功', 'success');
									$("#searchForm").submit();
								}else{
									$.jBox.tip('操作失败,原因:' + data.msg, 'error');
									return false;
								}
							}
						})
					}
				}, height: '500', width: 500
			});

			$pop.find("input[name='rate']").keyup(function () {
				var rate = $pop.find("input[name='rate']").val();
				var payment = $pop.find("span[name='payment']").html();
				var afterexchange = payment * rate;
				$pop.find("span[name='afterexchange']").html(parseFloat(afterexchange).toFixed(2));
			});
		}
	});

	inquiryCheckBOX();
}

function add_confirm_date(html, all_uuid){
	var uid_count_array = all_uuid.split(',');
	for(var i=0;i<uid_count_array.length;i++){
		var uid_count = uid_count_array[i].split('_');
		$('#payment_confirmall_pop_o').find('td[name="'+uid_count[0]+'_confirmdate"]')
				.attr('rowspan',uid_count[1]);
	}
}

// 137 START 返佣付款批量弹窗
function paymentconfirmall(ctx,id,type,status) {
	var tmp = '';
	var flag = false;
	$("input[name='"+id+"']").each(function(){
		if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
			tmp=tmp+$(this).attr('value')+",";
			var value = $(this).parent().parent().find("font[class='noPay']").text();
			if(value == "已付"){
				flag = true;
			}
		}
	});
	if(tmp==""){
		top.$.jBox.tip("请选择数据！");
		return;
	}
	if(flag){
		top.$.jBox.tip("已有数据付款，请重新选择！");
		return;
	}
	$.ajax({
		type: 'POST',
		url: ctx + '/costNew/payManager/getRebatesInfo',
		data: {
			ids : tmp
		},
		success: function (data) {
			html = 	"<div id='payment_confirmall_pop_o'>" +
				"<div class='payment_confirmall_pop' >" +
				"<table class='table activitylist_bodyer_table orderlist-list' style='margin:0 auto;'>" +
				"<thead>" +
				"<tr>" +
				"<th style='display:none;'>uuid</th>" +
				"<th style='display:none;'>区分新旧数据</th>" +
				"<th style='display:none;'>产品类型</th>" +
				"<th style='display:none;'>rebatesId</th>" +
				"<th class='tc' width='7%'>申请日期</th>" +
				"<th class='tc' width='7%'>团号</th>" +
				"<th class='tc' width='7%'>款项</th>" +
				"<th class='tc' width='7%'>付款金额</th>" +
				"<th class='tc' width='7%'>汇率<br><input id='rateCopy' class='inputTxt inputTxtlong' value='' onkeyup='validNum(this)' onafterpaste='validNum(this)' ><br><input id='visaCopyBtn' type='button' value='复制'  class='rateCopy visa_copy'></th>" +
				"<th class='tc' width='7%'>转换后金额</th>" +
				"<th class='tc' width='7%'>出纳确认日期<br><input id='confirmDateCopy' class='inputTxt dateinput' readonly onClick='WdatePicker()'><br><input id='dateCopyBtn' type='button' value='复制'  class='rateCopy visa_copy'></th>" +
				"</tr>" +
				"</thead>" +
				"<tbody  id='context'>";
			var count = 1;
			var all_uuid = "";
			for(var i = 0; i < data.length;i++) {
				//是否已存在此uuid
				var index = all_uuid.indexOf(data[i].id + "_");
				if(-1 == index){
					count = 1;
					if(all_uuid){
						// 表示有多条返佣审批
						all_uuid += "," + data[i].id + "_" + count;
					}else{
						all_uuid = data[i].id + "_" + count;
					}
				}else{
					//一条返佣，多条币种
					count++;
					all_uuid = all_uuid.replace(data[i].id + "_" + (count-1),data[i].id + "_" + count);
				}
				html += "<tr>" +
					"<td  style='display:none;'><input name='uuid' value = '" + data[i].id + "'/></td>" +
					"<td  style='display:none;'><input name='reviewflag' value = '" + data[i].reviewflag + "'/></td>" +
					"<td  style='display:none;'><input name='orderType' value = '" + data[i].orderType + "'/></td>" +
					"<td  style='display:none;'><input name='rebatesId' value = '" + data[i].rebatesId + "'/></td>" +
					"<td class='tc'>"+data[i].createDate+"</td>" +
					"<td class='tc'>"+data[i].groupCode+"</td>" +
					"<td class='tc'>"+data[i].costname+"</td>" +
					"<td class='tc payment'>"+data[i].currencyMark+"<span name='payment'>"+parseFloat(data[i].rebatesDiff).toFixed(2)+"</span></td>" +
					"<td class='tc'><input name='rateinput' class='rate inputTxt inputTxtlong' value='"+data[i].rate+"' onkeyup='validNum(this)' onafterpaste='validNum(this)'";
				if("¥" == data[i].currencyMark) {
					html += "readOnly='true'";
				}
				html += " ></td>" +
					"<td class='afterexchange tc'>¥<span name='afterexchange'>"+parseFloat(data[i].rebatesDiff*data[i].rate).toFixed(2)+"</span></td>";
				if(1 == count){
					// 只有第一行添加出纳确认日期，其他币种合并到当前行
					html += "<td class='tc' rowspan=1 name='"+data[i].id+"_confirmdate'><input name='cashierConfirmDate_"+data[i].id+"' class='inputTxt dateinput' value='"+new Date().Format("yyyy-MM-dd")+"' readonly onClick='WdatePicker()'></td>" ;
				}
				html += "</tr>";
			}

			html += "</tbody></table></div></div>";

			var $pop = $.jBox(html, {
				title    : "付款确认", buttons: {'确认': 1, '取消': 2}, submit: function (v, h, f) {
					if (v == "1") {

						if ($pop.find('.rate').val() == "") {
							top.$.jBox.info("汇率不能为空", "提示");
							return false;
						}

						if ($pop.find('.rate').val() == 0) {
							top.$.jBox.info("汇率不能为0", "提示");
							return false;
						}
						
						var $cashierConfirmDate = $pop.find("input[name^='cashierConfirmDate']");
						for(var i=0; i<$cashierConfirmDate.length; i++) {
							if($cashierConfirmDate[i].value == "") {
								top.$.jBox.info("出纳确认时间不能为空", "提示");
								return false;
							}
						}

						var rate = $pop.find('.rate').val();

						var datas = new Array();
						var dn = 0;
						var errmsg = "";
						$('#context tr').each(function (){
							var uuid = $("input[name='uuid']")[dn].value;
							var reviewflag = $("input[name='reviewflag']")[dn].value;
							var orderType = $("input[name='orderType']")[dn].value;
							var rebatesId = $(this).find("input[name='rebatesId']").val();
							var rate = $("input[name='rateinput']")[dn].value;
							var price = $("span[name='afterexchange']")[dn].firstChild.data;
							var cashierConfirmDate = $("input[name='cashierConfirmDate_"+uuid+"']")[0].value;
							if((rate == null || rate == "") && errmsg == ""){
								errmsg += "汇率不能为空";
							}
							if((rate == 0) && errmsg == ""){
								errmsg += "汇率不能为0";
							}

							var s = new Object();
							s.uuid = uuid;
							s.reviewflag = reviewflag;
							s.orderType = orderType;
							s.rebatesId = rebatesId;
							s.rate = rate;
							s.price = price;
							s.cashierConfirmDate = cashierConfirmDate;
							datas[dn] = s;
							dn++;
						});
						if(errmsg != ""){
							$.jBox.tip(errmsg);
							return false;
						}
						$.ajax({
							type:"POST",
							url:ctx+"/costNew/payManager/batchConfirmPay",
							data : {datas : JSON.stringify(datas), flag : 202},
							success:function(data){
								if(data){
									$.jBox.tip('操作成功', 'success');
									$("#searchForm").submit();
								}else{
									$.jBox.tip('操作失败,原因:' + data.msg, 'error');
									return false;
								}
							}
						})
					}
				}, height: '500', width: 800
			});
			//必须在html代码加载完成之后再执行此函数，函数位置不能向前提
			add_confirm_date(html, all_uuid);
			$pop.find("#dateCopyBtn").on("click", function () {
				var datecopy = $pop.find("#confirmDateCopy").val();
				if (datecopy != "") {
					$pop.find("input[name^='cashierConfirmDate']").val(datecopy);
				}
			});
			
			$pop.find(".rateCopy").on("click", function () {
				var ratecopy = $pop.find("#rateCopy").val();
				if (ratecopy != "") {
					$pop.find("input[name='rateinput']:not([readOnly])").val(ratecopy);

					$pop.find("span[name='afterexchange']").each(function(){
						var rate = $(this).parent().prev().find("input[name='rateinput']").val();
						var payment =$(this).parent().prev().prev().find("span").text();
						var afterexchange = payment * rate;
						$(this).html(parseFloat(afterexchange).toFixed(2));
					});
				}
			});

			$pop.find("input[name='rateinput']").keyup(function () {
				$pop.find("span[name='afterexchange']").each(function(){
					var rate = $(this).parent().prev().find("input[name='rateinput']").val();
					var payment =$(this).parent().prev().prev().find("span").text();
					var afterexchange = payment * rate;
					$(this).html(parseFloat(afterexchange).toFixed(2));
				});
			});
		}
	});
}
//137 END

//137 start 退款付款确认付款弹窗
function jbox_paymentconfirmrefund(ctx, review_uuid, status, flag) {
	$.ajax({
		type: 'POST',
		url: ctx + '/costNew/payManager/getRefundInfo',
		data: {
			reviewUuid : review_uuid
		},
		success: function (data) {
			//if (data.flag == 'success') {
			html = 	"<div id='payment_confirm_pop_o'>" +
				"<div class='payment_confirm_pop'>" +
				"<table class='table activitylist_bodyer_table orderlist-list' style='margin:0 auto;'>" +
				"<thead>" +
				"<tr>" +
				"<th class='tc' width='7%'>付款金额</th>" +
				"<th class=tc' width='4%'></th>" +
				"<th class='tc' width='5%'>汇率</th>" +
				"<th class='tc' width='4%'></th>" +
				"<th class='tc' width='15%'>转换后金额</th>" +
				"<th class='tc' width='15%'>出纳确认时间</th>" +
				"</tr>" +
				"</thead>" +
				"<tbody>" +
				"<tr>" +
				"<td class='tc payment'>"+data.currencyMark+"<span name='payment'>"+parseFloat(data.rebatesDiff).toFixed(2)+"</span></td>" +
				"<td class='tc'>x</td>" +
				"<td class='tc'><input class='rate inputTxt inputTxtlong' value='"+data.rate+"' onkeyup='validNum(this)' onafterpaste='validNum(this)' name='rate'";
			if("¥" == data.currencyMark) {
				html += "readOnly='true'";
			}
			html += "></td>" +
				"<td class='tc'>=</td>" +
				"<td class='afterexchange tc'>¥<span name='afterexchange'>"+parseFloat(data.rebatesDiff*data.rate).toFixed(2)+"</span></td>" +
				"<td class='tc'><input class='inputTxt dateinput' name='cashierConfirmDate' value='"+ new Date().Format("yyyy-MM-dd") +"' onClick='WdatePicker()'></td>" +
				"</tr>" +
				"<tbody>" +
				"</table>" +
				"</div>" +
				"</div>";

			var $pop = $.jBox(html, {
				title    : "付款确认", buttons: {'确认': 1, '取消': 2}, submit: function (v, h, f) {
					if (v == "1") {

						if ($pop.find('.rate').val() == "") {
							top.$.jBox.info("汇率不能为空", "提示");
							return false;
						}

						if ($pop.find('.rate').val() == 0) {
							top.$.jBox.info("汇率不能为0", "提示");
							return false;
						}

						if ($pop.find("input[name='cashierConfirmDate']").val() == "") {
							top.$.jBox.info("出纳确认时间为必填信息！", "提示");
							return false;
						}

						var rate = $pop.find('.rate').val();
						var price = $pop.find("span[name='afterexchange']").html();
						var cashierConfirmDate = $pop.find("input[name='cashierConfirmDate']").val();
						$.ajax({
							type:"POST",
							url:ctx+"/review/common/web/confimOrCancelPay",
							data:{reviewId:review_uuid,status:status,rate:rate,price:price,cashierConfirmDate:cashierConfirmDate,flag:201},
							success:function(data){
								if(data.flag){
									$.jBox.tip('操作成功', 'success');
									$("#searchForm").submit();
								}else{
									$.jBox.tip('操作失败,原因:' + data.msg, 'error');
									return false;
								}
							}
						})
					}
				}, height: '500', width: 500
			});

			$pop.find("input[name='rate']").keyup(function () {
				var rate = $pop.find("input[name='rate']").val();
				var payment = $pop.find("span[name='payment']").html();
				var afterexchange = payment * rate;
				$pop.find("span[name='afterexchange']").html(parseFloat(afterexchange).toFixed(2));
			});
			//}
		}
	});

	// var payment = $pop.find('.payment').html();
	// var rate = $pop.find('.rate').val();
	// var afterchange = payment * rate;
	// $pop.find('.afterchange').html(afterchange);

	inquiryCheckBOX();

	// });

//137 END 确认付款弹窗
}

//137 start 退款付款确认付款弹窗--旧数据
function jbox_paymentconfirmrefundold(ctx, review_uuid, type, status, flag) {
	$.ajax({
		type: 'POST',
		url: ctx + '/costNew/payManager/getOldRefundInfo',
		data: {
			reviewUuid : review_uuid
		},
		success: function (data) {
			//if (data.flag == 'success') {
			html = 	"<div id='payment_confirm_pop_o'>" +
				"<div class='payment_confirm_pop'>" +
				"<table class='table activitylist_bodyer_table orderlist-list' style='margin:0 auto;'>" +
				"<thead>" +
				"<tr>" +
				"<th class='tc' width='7%'>付款金额</th>" +
				"<th class=tc' width='4%'></th>" +
				"<th class='tc' width='5%'>汇率</th>" +
				"<th class='tc' width='4%'></th>" +
				"<th class='tc' width='15%'>转换后金额</th>" +
				"<th class='tc' width='15%'>出纳确认时间</th>" +
				"</tr>" +
				"</thead>" +
				"<tbody>" +
				"<tr>" +
				"<td class='tc payment'>"+data.currencyMark+"<span name='payment'>"+parseFloat(data.rebatesDiff).toFixed(2)+"</span></td>" +
				"<td class='tc'>x</td>" +
				"<td class='tc'><input class='rate inputTxt inputTxtlong' value='"+data.rate+"' onkeyup='validNum(this)' onafterpaste='validNum(this)' name='rate'";
			if("¥" == data.currencyMark) {
				html += "readOnly='true'";
			}
			html += "></td>" +
				"<td class='tc'>=</td>" +
				"<td class='afterexchange tc'>¥<span name='afterexchange'>"+parseFloat(data.rebatesDiff*data.rate).toFixed(2)+"</span></td>" +
				"<td class='tc'><input class='inputTxt dateinput' name='confirmCashierDate' value='"+ new Date().Format("yyyy-MM-dd") +"' onClick='WdatePicker()'></td>" +
				"</tr>" +
				"<tbody>" +
				"</table>" +
				"</div>" +
				"</div>";

			var $pop = $.jBox(html, {
				title    : "付款确认", buttons: {'确认': 1, '取消': 2}, submit: function (v, h, f) {
					if (v == "1") {

						if ($pop.find('.rate').val() == "") {
							top.$.jBox.info("汇率不能为空", "提示");
							return false;
						}

						if ($pop.find('.rate').val() == 0) {
							top.$.jBox.info("汇率不能为0", "提示");
							return false;
						}

						if ($pop.find("input[name='cashierConfirmDate']").val() == "") {
							top.$.jBox.info("出纳确认时间为必填信息！", "提示");
							return false;
						}

						var rate = $pop.find('.rate').val();
						var confirmCashierDate = $pop.find("input[name='confirmCashierDate']").val();//出纳确认时间，为了和退款返佣统一，使用该名称。
						$.ajax({
							type:"POST",
							url:ctx+"/orderCommon/manage/confirmPay",
							data:{id:review_uuid,type:type,status:1,rate:rate,confirmCashierDate:confirmCashierDate,flag:201},
							success:function(data){
								if(data.flag){
									$.jBox.tip('操作成功', 'success');
									$("#searchForm").submit();
								}else{
									$.jBox.tip('操作失败,原因:' + data.msg, 'error');
									return false;
								}
							}
						})
					}
				}, height: '500', width: 500
			});

			$pop.find("input[name='rate']").keyup(function () {
				var rate = $pop.find("input[name='rate']").val();
				var payment = $pop.find("span[name='payment']").html();
				var afterexchange = payment * rate;
				$pop.find("span[name='afterexchange']").html(parseFloat(afterexchange).toFixed(2));
			});
			//}
		}
	});

	// var payment = $pop.find('.payment').html();
	// var rate = $pop.find('.rate').val();
	// var afterchange = payment * rate;
	// $pop.find('.afterchange').html(afterchange);

	inquiryCheckBOX();

	// });

//137 END 确认付款弹窗
}

// 137 START 退款付款批量弹窗
function paymentconfirmallrefunds(ctx,id,type,status) {

	var tmp = '';
	var flag = false;
	$("input[name='"+id+"']").each(function(){
		if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
			tmp=tmp+$(this).attr('value')+",";
			var value = $(this).parent().parent().find("font[class='noPay']").text();
			if(value == "已付款"){
				flag = true;
			}
		}
	});
	if(tmp==""){
		top.$.jBox.tip("请选择数据！");
		return;
	}
	if(flag){
		top.$.jBox.tip('已有数据付款，请重新选择！');
		return;
	}
	$.ajax({
		type: 'POST',
		url: ctx + '/costNew/payManager/getRefundsInfo',
		data: {
			ids : tmp
		},
		success: function (data) {
			//if (data.flag == 'success') {
			html = 	"<div id='payment_confirmall_pop_o'>" +
				"<div class='payment_confirmall_pop' >" +
				"<table class='table activitylist_bodyer_table orderlist-list' style='margin:0 auto;'>" +
				"<thead>" +
				"<tr>" +

				"<th style='display:none;'>uuid</th>" +
				"<th style='display:none;'>区分新旧数据</th>" +
				"<th style='display:none;'>产品类型</th>" +
				"<th class='tc' width='7%'>申请日期</th>" +
				"<th class='tc' width='7%'>团号</th>" +
				"<th class='tc' width='7%'>款项</th>" +
				"<th class='tc' width='7%'>付款金额</th>" +
				"<th class='tc' width='7%'>汇率<br><input id='rateCopy' class='inputTxt inputTxtlong' value='' onkeyup='validNum(this)' onafterpaste='validNum(this)' ><br><input id='visaCopyBtn' type='button' value='复制'  class='rateCopy visa_copy'></th>" +
				"<th class='tc' width='7%'>转换后金额</th>" +
				"<th class='tc' width='7%'>出纳确认时间<br>" +
					"<input class='inputTxt dateinput' id='cashierConfirmDateCopy' onClick='WdatePicker()'><br><input id='dateCopyBtn' type='button' value='复制' class='rateCopy visa_copy'></th>" +

				"</tr>" +
				"</thead>" +
				"<tbody  id='context'>";
			for(var i = 0; i < data.length;i++) {
				html += "<tr>" +
					"<td  style='display:none;'><input name='uuid' value = '" + data[i].id + "'/></td>" +
					"<td  style='display:none;'><input name='reviewflag' value = '" + data[i].reviewflag + "'/></td>" +
					"<td  style='display:none;'><input name='orderType' value = '" + data[i].orderType + "'/></td>" +
					"<td class='tc'>"+data[i].createDate1+"</td>" +
					"<td class='tc'>"+data[i].groupCode+"</td>" +
					"<td class='tc'>"+data[i].refundName+"</td>" +
					"<td class='tc payment'>"+data[i].currencyMark+"<span name='payment'>"+parseFloat(data[i].rebatesDiff).toFixed(2)+"</span></td>" +

					"<td class='tc'><input name='rateinput' class='rate inputTxt inputTxtlong' value='"+data[i].rate+"' onkeyup='validNum(this)' onafterpaste='validNum(this)'";
				if("¥" == data[i].currencyMark) {
					html += "readOnly='true'";
				}
				html += " ></td>" +

					"<td class='afterexchange tc'>¥<span name='afterexchange'>"+parseFloat(data[i].rebatesDiff*data[i].rate).toFixed(2)+"</span></td>" +
					"<td class='tc'><input class='inputTxt dateinput' name='cashierConfirmDate' value='"+ new Date().Format("yyyy-MM-dd") +"' onClick='WdatePicker()'></td>" +
					"</tr>";
			}

			html += "</tbody>" +
				"</table>" +
				"</div>" +
				"</div>";

			var $pop = $.jBox(html, {
				title    : "付款确认", buttons: {'确认': 1, '取消': 2}, submit: function (v, h, f) {
					if (v == "1") {

						if ($pop.find('.rate').val() == "") {
							top.$.jBox.info("汇率不能为空", "提示");
							return false;
						}

						if ($pop.find('.rate').val() == 0) {
							top.$.jBox.info("汇率不能为0", "提示");
							return false;
						}

						var datas = new Array();
						var dn = 0;
						var errmsg = "";
						$('#context tr').each(function (){
							var uuid = $("input[name='uuid']")[dn].value;
							var reviewflag = $("input[name='reviewflag']")[dn].value;
							var orderType = $("input[name='orderType']")[dn].value;
							var rate = $("input[name='rateinput']")[dn].value;
							var price = $("span[name='afterexchange']")[dn].firstChild.data;
							var cashierConfirmDate = $("input[name='cashierConfirmDate']")[dn].value;
							if((rate == null || rate == "") && errmsg == ""){
								errmsg += "汇率不能为空";
								//return;
							}
							if((rate == 0) && errmsg == ""){
								errmsg += "汇率不能为0";
								//return;
							}
							if ((cashierConfirmDate == null || cashierConfirmDate == "") && errmsg == ""){
								errmsg += "出纳确认时间为必填信息！";
							}
							var s = new Object();
							s.uuid = uuid;
							s.reviewflag = reviewflag;
							s.orderType = orderType;
							s.rate = rate;
							s.price = price;
							s.cashierConfirmDate = cashierConfirmDate;
							datas[dn] = s;
							dn++;
						});
						if(errmsg != ""){
							top.$.jBox.info(errmsg, "提示");
							return false;
						}

						$.ajax({
							type:"POST",
							url:ctx+"/costNew/payManager/batchConfirmPay",
							//data:{reviewId:review_uuid,type:type,status:status,rate:rate},
							data : {datas : JSON.stringify(datas), flag : 201},
							success:function(data){
								if(data){
									$.jBox.tip('操作成功', 'success');
									$("#searchForm").submit();
								}else{
									$.jBox.tip('操作失败,原因:' + data.msg, 'error');
									return false;
								}
							}
						})
					}
				}, height: '500', width: 600
			});

			$("#jbox-content").parent("div").css("max-width","625px");
			$("#jbox-content").parent("div").css("width","auto");
			
			$pop.find("#dateCopyBtn").on("click",function(){
				var dateStr = $pop.find("#cashierConfirmDateCopy").val();;
				if (dateStr != ""){
					$pop.find("input[name='cashierConfirmDate']").val(dateStr);
				}
			});

			$pop.find(".rateCopy").on("click", function () {
				var ratecopy = $pop.find("#rateCopy").val();
				if (ratecopy != "") {
					$pop.find("input[name='rateinput']:not([readOnly])").val(ratecopy);

					$pop.find("span[name='afterexchange']").each(function(){
						var rate = $(this).parent().prev().find("input[name='rateinput']").val();
						var payment =$(this).parent().prev().prev().find("span").text();
						var afterexchange = payment * rate;
						$(this).html(parseFloat(afterexchange).toFixed(2));
					});
				}
			});

			$pop.find("input[name='rateinput']").keyup(function () {
				$pop.find("span[name='afterexchange']").each(function(){
					var rate = $(this).parent().prev().find("input[name='rateinput']").val();
					var payment =$(this).parent().prev().prev().find("span").text();
					var afterexchange = payment * rate;
					$(this).html(parseFloat(afterexchange).toFixed(2));
				});
			});
			//}
		}
	});

}
//137 END

//正负数字验证
function validNum(dom){
	var thisvalue = $(dom).val();

	if(thisvalue.length >15){
		top.$.jBox.info("改价金额位数不合法", "提示");
		thisvalue = '0.00';
	}

	var minusSign = false;
	if(thisvalue){
		if(/^\-/.test(thisvalue)){
			minusSign = true;
			thisvalue = thisvalue.substring(1);
		}
		thisvalue = thisvalue.replace(/[^\d\.]/g,"").replace(/(\.\d{4}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
		var txt = thisvalue.split(".");
		thisvalue = txt[0]+(txt.length>1?"."+txt[1]:"");
		if(minusSign){
			thisvalue = thisvalue;
		}
		$(dom).val(thisvalue);
	}
}

//对Date的扩展，将 Date 转化为指定格式的String
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
//例子： 
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.Format = function (fmt) { //author: meizz 
	var o = {
		"M+": this.getMonth() + 1, //月份 
		"d+": this.getDate(), //日 
		"h+": this.getHours(), //小时 
		"m+": this.getMinutes(), //分 
		"s+": this.getSeconds(), //秒 
		"q+": Math.floor((this.getMonth() + 3) / 3), //季度 
		"S": this.getMilliseconds() //毫秒 
	};
	if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

/**
 *进行批量退款等操作时，把批量修改时间输入框的值，复制到下方的单个条目上。
 * yudong.xu 2016.5.23
 */
function batchChangeConfirmTime(obj){
	var dateStr = obj.value;
	$("#context input[name='cashierConfirmDate']").each(function(){
		this.value = dateStr;
	});
}


//504 确认付款弹出框 S
function PaymentConfirmpop(ctx, orderId, type){
    $pop = $.jBox($("#payment_confirm_pop_o").html(), {
        title    : "付款确认", buttons: {'确认': 1,'取消':0}, submit: function (v, h, f) {
            if (v == "1") {
            	var confirmDate = $pop.find("input[name=confirmDate]").eq(1).val();
            	if (confirmDate == "") {
            		top.$.jBox.info("出纳确认时间为必填信息！", "提示");
            		return false;
            	}
            	
            	$.ajax({
            		type : "POST",
            		url : ctx + "/finance/serviceCharge/paymentConfirm",
            		data : {
            			confirmDate : confirmDate,
            			orderId : orderId,
            			type : type
            		},
            		async : false,
            		success: function (data) {
            			if (data.flag) {
            				$.jBox.tip('操作成功', 'success');
            				$('#searchForm').submit();
            			} else {
            				$.jBox.tip('操作失败，原因：' + data.msg, 'error');
            				return false;
            			}
            		}
            	});
            }
        },  width: 840
    });
    return $pop;
}
function PCpopGetData (ctx, orderId, type) {
	var html = "";
	$.ajax({
		type : "POST",
		url : ctx + "/finance/serviceCharge/getPaymentConfirmInfo",
		data : {
			orderId: orderId, 
			type: type
		},
		async: false,
		success : function (data) {
			if (data.flag) {
				html = "<tr>" +
				   "<td class='tc'>" + data.agentName + "</td>" +
				   "<td class='tc'>" + data.groupCode + "</td>" +
				   "<td class='tc'>" + data.orderNum + "</td>" +
				   "<td class='tc p0'><span class='span-ellipsis'>" + data.serviceChargeSerialNum + "</span></td>" +
				   "<td class='tc'><input name='confirmDate' class='inputTxt dateinput date' value='" + new Date().Format("yyyy-MM-dd") + "' onclick='WdatePicker()'></td>" +
				   "</tr>";
			}
		}
	});
	return html;

}

/**
 * 服务费确认付款
 * @param obj 用以判断是否为批量确认
 * @param orderId 订单id
 * @param type 服务费类型
 * @author yang.wang
 * @date 2016.9.2
 * */
function jyPaymentConfirm(obj, ctx, orderId, type){
	if ($(obj).is("a")) {
		$("#div1").attr("class","display-none");
		var $pop = PaymentConfirmpop(ctx, orderId, type);
		var html = PCpopGetData(ctx, orderId, type);     
		$pop.find("#paymentConfirmList").append(html);  
		$pop.find(".span-ellipsis").each(function(){
			$(this).attr("title",$(this).text());
		});     
	}else{
		var inputs=$(".box:checked");
		// 重新再选择一下，如果选择了已付款的就显示提示返回。和原有代码不好结合，故重新选择了一下。
		var payed = $(".box:checked[payStatus='1']");
		if (payed.length > 0){
			$.jBox.tip('已有数据付款，请重新选择','warnning');
			return false;
		}
		if(inputs.length != 0){
			var datas = getData(inputs);
			$("#div1").attr("class","");
			var $pop=batchPaymentConfirmpop(ctx);
			var html="";
			html+=batchPCpopGetData(ctx,JSON.stringify(datas));
			$pop.find("#paymentConfirmList").append(html);
			$pop.find(".span-ellipsis").each(function(){
				$(this).attr("title",$(this).text());
			});
		}else{
			$.jBox.tip('请选择数据','warnning');
			return false;
		}
	}
}

/**
 * 撤销付款
 * */
function paymentCancel(ctx, orderId, type){
    $.jBox.confirm("取消付款确认吗？", "提示", function(v,h,f){
        if(v=='ok'){
           $.ajax({
        	   type : "POST",
        	   url : ctx + "/finance/serviceCharge/cancelPaymentConfirm",
        	   data : {
        		   orderId : orderId,
        		   type : type
        	   },
        	   success: function (data) {
        		   if (data.flag) {
        			   $.jBox.tip('操作成功', 'success');
        			   $('#searchForm').submit();
        		   } else {
        			   $.jBox.tip('操作失败，原因：' + data.msg, 'error');
        		   }
        	   }
           });
        }
    });
}

/**
 * 批量付款查询
 * @param ctx
 * @param orderId
 * @param type
 * @returns {String}
 */
function batchPCpopGetData (ctx, orderData) {
	var html = "";
	$.ajax({
		type : "POST",
		url : ctx + "/finance/serviceCharge/getBatchPaymentConfirmInfo",
		data : {
			datas:orderData
		},
		async: false,
		success : function (result) {
			$.each(result,function(i,data){
				html += "<tr>" +
				   "<td class='tc'>" + data.agentName + " <input type = 'hidden' value='"+data.orderId+"' name='orderId'/><input type = 'hidden' value='"+data.type+"' name='type'/></td>" +
				   "<td class='tc'>" + data.groupCode + "</td>" +
				   "<td class='tc'>" + data.orderNum + "</td>" +
				   "<td class='tc p0'><span class='span-ellipsis'>" + data.serviceChargeSerialNum + "</span></td>" +
				   "<td class='tc'><input name='confirmDate' class='inputTxt dateinput date' value='" + new Date().Format("yyyy-MM-dd") + "' onclick='WdatePicker()'></td>" +
				   "</tr>"
			});
		}
	});
	return html;
}

//服务费确认付款列表查询，将选中的checkbox中的orderId组装成数组
function getData(obj){
	var datas = [];
	$.each(obj,function(i,input){
		var data = {};
		data.orderId = $(input).val();
		data.type = $(input).next().val();
		datas.push(data);
	});
	return datas;
}

//服务费付款复制按钮
function copy(obj){
	$(".date").val($(obj).prev().prev().val());
}

//504 批量确认付款弹出框 S
function batchPaymentConfirmpop(ctx){
    $pop = $.jBox($("#payment_confirm_pop_o").html(), {
        title    : "付款确认", buttons: {'确认': 1,'取消':0}, submit: function (v, h, f) {
            if (v == "1") {
            	var confirmDates = $pop.find("input[class='inputTxt dateinput date']");
            	var check = false;
            	for(var i = 0 ; i < confirmDates.length ; i++){
            		if($(confirmDates[i]).val() == ""){
            			check = true;
            		}
            	}
            	if(check){
            		top.$.jBox.info("出纳确认时间为必填信息！", "提示");
            		return false;
            	}
            	var datas = [];
            	for(var i = 0 ; i < confirmDates.length ; i++){
            		var data = {};
            		var confirmDate = $(confirmDates[i]);
            		data.confirmDate = confirmDate.val();
            		data.orderId = confirmDate.parent().parent().find("input[name='orderId']").val();
            		data.type = confirmDate.parent().parent().find("input[name='type']").val();
            		datas.push(data);
            	}
            	$.ajax({
            		type : "POST",
            		url : ctx + "/finance/serviceCharge/batchPaymentConfirm",
            		data : {
            			datas:JSON.stringify(datas)
            		},
            		success: function (data) {
            			if (data.flag) {
            				$.jBox.tip('操作成功', 'success');
            				$('#searchForm').submit();
            			} else {
            				$.jBox.tip('操作失败，原因：' + data.msg, 'error');
            				return false;
            			}
            		}
            	});
            }
        },  width: 840
    });
    return $pop;
}

$(function(){
	
	//518 鼠标hover表格时上架下架标志变换 S
	//获取静态资源的地址
	ctxStatic = $("#ctxStatic").val();
	$(".g-w").on("mouseover mouseleave",function(event){
		if(event.type=="mouseover"){
			$(this).css("background-image","none").prev().show();
		}else if(event.type=="mouseleave"){
			$(this).css("background-image","url("+ctxStatic+"/images/g-w.png)").prev().hide();
		}
	});


	//518 鼠标hover表格时上架下架标志变换 E

	//518 更多价格 S
	$(".morePrice").on("mouseenter mouseout",function(event){
		if(event.type=="mouseenter"){
			$(this).next().show();
		}else if(event.type=="mouseout"){
			$(this).next().hide();
		}

	});

	//518 更多价格 E

	//518 批量上架表格内checkbox全选中时全选checkbox也选中 S


	$(".tableCheckBox,.table-checkReverse").on("click",function(){
		var $td = $(this).parents("td");
		var $form = $td.find("form");
		var $checkbox = $td.find("input[name='groupNo']").not(":disabled");
		var length = $checkbox.length;
		var checkedLength = $form.find("input[name='groupNo']:checked").length;
		if(checkedLength==length){
			$td.find(".table-checkAll").prop("checked", true);
		}else{
			$td.find(".table-checkAll").removeAttr("checked");
		}
	});
});

//518 批量上架全选反选 S

function tableCheckAll(obj){
	var $check = $(obj).parent().parent().prev().find("input[name='groupNo']").not(":disabled");
	if($(obj).is(":checked")){
		$check.attr('checked', true);
	}else{
		$check.removeAttr("checked");
	}
}
function tableCheckReverse(obj){
	var $checkReverse = $(obj).parent().prev().find("input[name='groupNo']").not(":disabled");
	$checkReverse.each(function(){
		this.checked=!this.checked;
	});
}
