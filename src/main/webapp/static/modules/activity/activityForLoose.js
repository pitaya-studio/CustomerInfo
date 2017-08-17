var ctx ;
var url;
var shelfRightsStatus;
$(function(){
	ctx = $("#ctx").val();
	url = ctx;
	shelfRightsStatus = $("#shelfRightsStatus").val();
});


//518 修改前判断是否上架 如上架则弹出弹框
function savegroupForLoose(obj){
	var $tr = $(obj).parent().parent();
	var planPositon = $(obj).parent().parent().find("input[name='planPosition']").val();
	var freePositon = $(obj).parent().parent().find("input[name='freePosition']").val();
	var maxChildrenCount = $tr.find("input[name='maxChildrenCount']").val();
	var maxPeopleCount = $tr.find("input[name='maxPeopleCount']").val();

	var groupOpenDateInput = $(obj).parent().parent().find("input[name='groupOpenDate']").val();
	var groupOpenDateSpan = $(obj).parent().parent().find("span[name='groupOpenDate']").text();
	
	var suggestAdultPrice = $(obj).parent().parent().find("input[name='suggestAdultPrice']").val();
	if (requiredStraightPrice == 'true') {
		if (suggestAdultPrice == '0' || suggestAdultPrice == '0.0' || suggestAdultPrice == '0.00' 
			|| suggestAdultPrice == '' || suggestAdultPrice == null) {
			top.$.jBox.info("必须设置直客价", "警告");
			return;
		}
	}
	
	if(groupOpenDateInput != groupOpenDateSpan){
		var flag = 0;
		var groupOpenDates = $(obj).parents('table:first').find('span[name="groupOpenDate"]').each(function(){
			//var aaa = this.parent().find('span[name="groupOpenDate"]').text();
			if(this.innerText == groupOpenDateInput) {
				flag++;
			}
		});

		if(flag > 0){
			top.$.jBox.info("同一产品下出团日期不能重复", "警告");
			return;
		}
	}

	//判断儿童最高人数与 预收 值
	if(planPositon && maxChildrenCount && parseInt(planPositon) < parseInt(maxChildrenCount)){
		top.$.jBox.info("儿童最高人数不能大于预收", "警告");
		return;
	}
	//判断特殊人群最高人数与预收值
	if(planPositon && maxPeopleCount && parseInt(planPositon) < parseInt(maxPeopleCount)){
		top.$.jBox.info("特殊人群最高人数不能大于预收", "警告");
		return;
	}
	//判断儿童最高人数+特殊人群最高人数与 预收 余位的值
	if(planPositon && maxPeopleCount && maxChildrenCount && parseInt(planPositon) < parseInt(maxPeopleCount)+parseInt(maxChildrenCount)){
		top.$.jBox.info("儿童与特殊人群最高人数之和不能大于预收", "警告");
		return;
	}
	var groupOpenDateElts = $(obj).parent().parent().find("input[name='groupOpenDate']").val();
	var groupCloseDateElts = $(obj).parent().parent().find("input[name='groupCloseDate']").val();
	if (groupOpenDateElts < groupCloseDateElts){
		top.$.jBox.info("出团日期不能早于截团日期", "警告");
		return;
	}
	
	if($(obj).parent().parent().find('.g-w').hasClass('grounding')){
		var groupIdsBuffer = $(obj).parent().parent().find('input[name="groupCode"]').attr("id");
		$.jBox.info("该团期已上架旅游交易预订系统，如需修改则产品将下架，是否确认修改？","提示",{
			buttons: {"关闭": "0", "确认": "1"},
			submit:function(v,h,f)
			{
				if (v == '1') {

					$.ajax({
						type: "POST",
						url: ctx + "/activity/manager/confimIsT1Off",
						data: {groupIdsBuffer: groupIdsBuffer},
						success: function (data) {
							if (data.flag) {
								//$.jBox.tip('操作成功', 'success');
								savegroupForLooseSecond(obj);
							} else {
								$.jBox.tip('操作失败,原因:' + data.msg, 'error');
								return false;
							}
						}
					})
					//已上架标签改为已下架
					$(obj).parent().parent().find('.g-w').removeClass('grounding').addClass('withdraw');
					 $(obj).parent().parent().find('.grounding-one').removeClass('grounding-hover').addClass('withdraw-hover');
					 $(obj).parent().parent().find('.grounding-one').html();
					 $(obj).parent().parent().find('.grounding-one').html('已下架旅游交易系统');
					 //复选框改为可用
					 //$(obj).parent().parent().find('input[type="checkbox"]').removeAttr("disabled");
					 //更多价格消失
					 //$(obj).parent().find('.morePrice').css('display','none');
					 //平台上架按钮出现
					 //$(obj).parent().find('.stack-btn').show()
					 //设定定价策略按钮出现
					 //$(obj).parent().find('.setting-btn').hide();
				}
				if (v == '0') {
					return true;
				}
			}
		})
	}else{
		savegroupForLooseSecond(obj);
	}
}

//产品列表-保存产品
function savegroupForLooseSecond(obj){
	// 433需求 只有预收或只有余位数变更后，保存时，弹出提示
	var planPositon = $(obj).parent().parent().find("input[name='planPosition']").val();
	var freePositon = $(obj).parent().parent().find("input[name='freePosition']").val();
	
	//var groupId = $(obj).parents("tr:first").find("td").eq(0).find("input").attr("id");
	var groupId = $(obj).parent().parent().find('input[name="groupCode"]').attr("id");
	var flag;
	$.ajax({
		type:"POST",
		url:url+"/activity/manager/getGroupPlanAndFreePositon",
		dataType:"json",
		contentType: "application/x-www-form-urlencoded;charset=utf-8",//默认按照jsp页面的contentType编码来encode,所以这个可以不用设置
		data:{	groupId:groupId},
		success:function(result){
			if(planPositon != result.planPositon && freePositon == result.freePositon){// 只修改了预收数，没有修改余位数
				$.jBox.confirm("预收数已发生变化，是否要修改余位数？", "系统提示", function (v, h, f) {
		            if (v == 'ok') {
		                return;
		            } else if (v == 'cancel') {
						confirmSaveGroupForLoose(obj);
		            }
		        });
			}else if(planPositon == result.planPositon && freePositon != result.freePositon){// 只修改了余位数，没有修改预收数时
				$.jBox.confirm("余位数已发生变化，是否要修改预收数？", "系统提示", function (v, h, f) {
		            if (v == 'ok') {
		                return; 
		            } else if (v == 'cancel') {
						confirmSaveGroupForLoose(obj);
		            }
		        });
			}else{ // 当预收数和余位数都不变化或都变化时
				confirmSaveGroupForLoose(obj);
			}
		}
	})

}

// 保存
function confirmSaveGroupForLoose(obj){
	//获取团期tr
	var $tr = $(obj).parent().parent();
	//获取团期所在的form表单
	var childform = $tr.parents("form:first");
	//获取旧团号
	var groupCodeSpan = $(obj).parents("tr:first").find("td").eq(0).find("span").text();
	var groupCodeInput = $(obj).parents("tr:first").find("td").eq(0).find("input[name='groupCode']").val();
	//获取团期id
	var groupId = $tr.find('input[name="groupCode"]').attr("id");
	//获取产品的Id
	var srcActivityId = $tr.find('input[name="srcActivityId"]').val();
	if (groupCodeSpan != groupCodeInput) {
		/**
		 * 对应需求  c460
		 * 团期进行修改操作时给出提示信息
		 */
		$.jBox.confirm("团号修改后该团期下订单数据、财务数据、审批数据对应的团号会相应变化，确认修改？", "提示", function (v, h, f) {
			if (v == 'ok') {
				var validator1 = childform.validate();
				//$(childform).removeAllRules();
				$tr.addRules();
				if (validator1.form()) {
					$.ajax({
						type: "POST",
						url: url + "/activity/manager/savegroup2/" + srcActivityId,
						dataType: "json",
						contentType: "application/x-www-form-urlencoded;charset=utf-8",//默认按照jsp页面的contentType编码来encode,所以这个可以不用设置
						data: {
							srcActivityId: srcActivityId,
							groupid: groupId,
							groupOpenDate: $tr.find("input[name='groupOpenDate']").val(),
							groupCloseDate: $tr.find("input[name='groupCloseDate']").val(),
							groupCode: $tr.find("input[name='groupCode']").val(),
							settlementAdultPrice: $tr.find("input[name='settlementAdultPrice']").val(),
							settlementcChildPrice: $tr.find("input[name='settlementcChildPrice']").val(),
							settlementSpecialPrice: $tr.find("input[name='settlementSpecialPrice']").val(),
							settlementAdultCurrencyType: $tr.find("input[name='settlementAdultPrice']").parent().find(".rm").text(),//当前币种的符号
							suggestAdultCurrencyType: $tr.find("input[name='suggestAdultPrice']").parent().find(".rm").text(),
							suggestAdultPrice: $tr.find("input[name='suggestAdultPrice']").val(),
							suggestChildPrice: $tr.find("input[name='suggestChildPrice']").val(),
							suggestSpecialPrice: $tr.find("input[name='suggestSpecialPrice']").val(),
							maxChildrenCount: $tr.find("input[name='maxChildrenCount']").val(),
							maxPeopleCount: $tr.find("input[name='maxPeopleCount']").val(),
							payDeposit: $tr.find("input[name='payDeposit']").val(),
							singleDiff: $tr.find("input[name='singleDiff']").val(),
							planPosition: $tr.find("input[name='planPosition']").val(),
							freePosition: $tr.find("input[name='freePosition']").val(),
							//0258需求,新增发票税,展开团期,修改->确认--s//
							invoiceTax: $tr.find("input[name='invoiceTax']").val(),
							//0258需求,新增发票税,展开团期,修改->确认--e//
							visaDate: $tr.find("input[name='visaDate']").val(),
							visaCountry: $tr.find("input[name='visaCountry']").val(),
							groupRemark: $tr.next().find('[name="groupNote"]').val()
						},
						success: function (result) {
							if (result == null) {
								top.$.jBox.info("该团期已经存在", "警告");
								return;
							} else if (result.groupCode == "groupCodeRepeat") {
								top.$.jBox.info("该团号已经存在", "警告");
								return;
							} else if (result.error == "errorChildNum") {
								top.$.jBox.info("儿童最高人数不能小于团期已报名儿童人数", "警告");
								return;
							} else if (result.error == "errorSpecialNum") {
								top.$.jBox.info("特殊人去最高人数不能小于团期已报名特殊人数", "警告");
								return;
							} else {
								var group = result.group;
								if (result.groupopendate && result.groupclosedate) {
									if (result.groupopendate == result.groupclosedate) {
										$("#groupdate" + srcActivityId + " #truedate").find("span").html(result.groupopendate);
									}
									else {
										$("#groupdate" + srcActivityId + " #truedate").find("span").html(result.groupopendate + "至" + result.groupclosedate);
									}
									$("#groupdate" + srcActivityId + " #truedate").find("a").html("关闭全部团期");
									$("#groupdate" + srcActivityId + " #truedate").show();
									$("#groupdate" + srcActivityId + " #falsedate").hide();
								}
								else {
									$("#groupdate" + srcActivityId + " #falsedate").show();
									$("#groupdate" + srcActivityId + " #truedate").show();
								}
								if (result.settlementadultprice) {
									if (result.settlementAdultPriceCMark == "-1") {
										var settlementAdultPriceCMark = $(obj).parent().parent().find("input[name='settlementAdultPrice']").parent().find("span[class=rm]").text();
										$("#settleadultprice" + srcActivityId).html(settlementAdultPriceCMark + "<span class='tdred fbold'>" + $(document).commafy(result.settlementadultprice) + "</span>起");
									} else {
										$("#settleadultprice" + srcActivityId).html(result.settlementAdultPriceCMark + "<span class='tdred fbold'>" + $(document).commafy(result.settlementadultprice) + "</span>起");
									}
								} else {
									$("#settleadultprice" + srcActivityId).html("价格待定");
								}
								if (result.suggestadultprice) {
									if (result.suggestAdultPriceCMark == "-1") {
										var suggestAdultPriceCMark = $(obj).parent().parent().find("input[name='suggestAdultPrice']").parent().find("span[class=rm]").text();
										$("#suggestadultprice" + srcActivityId).html(suggestAdultPriceCMark + "<span class='tdblue fbold'>" + $(document).commafy(result.suggestadultprice) + "</span>起");
									} else {
										$("#suggestadultprice" + srcActivityId).html(result.suggestAdultPriceCMark + "<span class='tdblue fbold'>" + $(document).commafy(result.suggestadultprice) + "</span>起");
									}
								} else {
									$("#suggestadultprice" + srcActivityId).html("价格待定");
								}
								//回显团期修改数据
								groupEcho(obj, group);
								//如果上架下架标签没有“withdraw”（表示状态为已下架），则无需提示上架
								if ($(obj).parent().parent().find('.g-w').hasClass('withdraw')) {
									onLineProductToT1(result.group.pricingStrategyStatus, obj);
								}
							}
						},
						error: function () {
							top.$.jBox.info("更新失败", "警告");
						}
					});
				} else {
					top.$.jBox.info("请先修改完错误再提交", "警告");
				}
			} else if (v == 'cancel') {

			}
		});
	} else {
		var validator1 = childform.validate();
		//$(childform).removeAllRules();

		var settlementAdultPrice_old = $tr.find("input[name='settlementAdultPrice']").val();
		var settlementcChildPrice_old = $tr.find("input[name='settlementcChildPrice']").val();
		var settlementSpecialPrice_old = $tr.find("input[name='settlementSpecialPrice']").val();
		var suggestAdultPrice_old = $tr.find("input[name='suggestAdultPrice']").val();
		var suggestChildPrice_old = $tr.find("input[name='suggestChildPrice']").val();
		var suggestSpecialPrice_old = $tr.find("input[name='suggestSpecialPrice']").val();

		$tr.addRules();

		if (validator1.form()) {
			$.ajax({
				type: "POST",
				url: url + "/activity/manager/savegroup2/" + srcActivityId,
				dataType: "json",
				contentType: "application/x-www-form-urlencoded;charset=utf-8",//默认按照jsp页面的contentType编码来encode,所以这个可以不用设置
				data: {
					srcActivityId: srcActivityId,
					groupid: groupId,
					groupOpenDate: $tr.find("input[name='groupOpenDate']").val(),
					groupCloseDate: $tr.find("input[name='groupCloseDate']").val(),
					groupCode: $tr.find("input[name='groupCode']").val(),
					settlementAdultPrice: $tr.find("input[name='settlementAdultPrice']").val(),
					settlementcChildPrice: $tr.find("input[name='settlementcChildPrice']").val(),
					settlementSpecialPrice: $tr.find("input[name='settlementSpecialPrice']").val(),
					settlementAdultCurrencyType: $tr.find("input[name=settlementAdultPrice]").parent().find(".rm").text(),//当前币种的符号
					suggestAdultCurrencyType: $tr.find("input[name='suggestAdultPrice']").parent().find(".rm").text(),
					suggestAdultPrice: $tr.find("input[name='suggestAdultPrice']").val(),
					suggestChildPrice: $tr.find("input[name='suggestChildPrice']").val(),
					suggestSpecialPrice: $tr.find("input[name='suggestSpecialPrice']").val(),
					maxChildrenCount: $tr.find("input[name='maxChildrenCount']").val(),
					maxPeopleCount: $tr.find("input[name='maxPeopleCount']").val(),
					payDeposit: $tr.find("input[name='payDeposit']").val(),
					singleDiff: $tr.find("input[name='singleDiff']").val(),
					planPosition: $tr.find("input[name='planPosition']").val(),
					freePosition: $tr.find("input[name='freePosition']").val(),

					//0258需求,新增发票税,展开团期,修改->确认--s//
					invoiceTax: $tr.find("input[name='invoiceTax']").val(),
					//0258需求,新增发票税,展开团期,修改->确认--e//

					visaDate: $tr.find("input[name='visaDate']").val(),
					visaCountry: $tr.find("input[name='visaCountry']").val(),
					groupRemark: $tr.next().find('[name="groupNote"]').val()
				},
				error: function () {
					top.$.jBox.info("更新失败", "警告");
				},
				success: function (result) {
					if (result == null) {
						top.$.jBox.info("该团期已经存在", "警告");
					} else if (result.groupCode == "groupCodeRepeat") {
						top.$.jBox.info("该团号已经存在", "警告");
						return;
					} else if (result.error == "errorChildNum") {
						top.$.jBox.info("儿童最高人数不能小于团期已报名儿童人数", "警告");
						return;
					} else if (result.error == "errorSpecialNum") {
						top.$.jBox.info("特殊人去最高人数不能小于团期已报名特殊人数", "警告");
						return;
					} else {
						var group = result.group;
						if (result.groupopendate && result.groupclosedate) {
							if (result.groupopendate == result.groupclosedate) {
								$("#groupdate" + srcActivityId + " #truedate").find("span").html(result.groupopendate);
							}
							else {
								$("#groupdate" + srcActivityId + " #truedate").find("span").html(result.groupopendate + "至" + result.groupclosedate);
							}

							$("#groupdate" + srcActivityId + " #truedate").find("a").html("关闭全部团期");
							$("#groupdate" + srcActivityId + " #truedate").show();
							$("#groupdate" + srcActivityId + " #falsedate").hide();
						}
						else {
							$("#groupdate" + srcActivityId + " #falsedate").show();
							$("#groupdate" + srcActivityId + " #truedate").show();
						}
						if (result.settlementadultprice) {
							if (result.settlementAdultPriceCMark == "-1") {
								var settlementAdultPriceCMark = $(obj).parent().parent().find("input[name='settlementAdultPrice']").parent().find("span[class=rm]").text();
								$("#settleadultprice" + srcActivityId).html(settlementAdultPriceCMark + "<span class='tdred fbold'>" + $(document).commafy(result.settlementadultprice) + "</span>起");
							} else {
								$("#settleadultprice" + srcActivityId).html(result.settlementAdultPriceCMark + "<span class='tdred fbold'>" + $(document).commafy(result.settlementadultprice) + "</span>起");
							}
						} else {
							$("#settleadultprice" + srcActivityId).html("价格待定");
						}

						if (result.suggestadultprice) {
							if (result.suggestAdultPriceCMark == "-1") {
								var suggestAdultPriceCMark = $(obj).parent().parent().find("input[name='suggestAdultPrice']").parent().find("span[class=rm]").text();
								$("#suggestadultprice" + srcActivityId).html(suggestAdultPriceCMark + "<span class='tdblue fbold'>" + $(document).commafy(result.suggestadultprice) + "</span>起");
							} else {
								$("#suggestadultprice" + srcActivityId).html(result.suggestAdultPriceCMark + "<span class='tdblue fbold'>" + $(document).commafy(result.suggestadultprice) + "</span>起");
							}
						} else {
							$("#suggestadultprice" + srcActivityId).html("价格待定");
						}

						//团期数据回显
						groupEcho(obj, group);
						//如果上架下架标签没有“withdraw”（表示状态为已下架），则无需提示上架
						if ($(obj).parent().parent().find('.g-w').hasClass('withdraw')) {
							onLineProductToT1(result.group.pricingStrategyStatus, obj);
						}
					}
				}
			});
		} else {
			top.$.jBox.info("请先修改完错误再提交", "警告");
		}
	}
}

/**
 * 回显团期数据
 * @param obj  addby:dujw
 */
function groupEcho(obj, group){
	$(obj).parent().parent().find("input[name='groupid']").css("display", "none").attr("disabled", true);
	//出团日期
	var groupOpenDate = $(obj).parent().parent().find("input[name='groupOpenDate']")[0];
	$(obj).parent().parent().find("input[name='groupOpenDate']").val(group.groupOpenDate);
	$(obj).parent().parent().find("input[name='groupOpenDate']").parent().find("span").html(group.groupOpenDate);
	//$(groupOpenDate).prev().prev().html(group.groupOpenDate);
	//截团日期
	$(obj).parent().parent().find("input[name='groupCloseDate']").val(group.groupCloseDate);
	$(obj).parent().parent().find("input[name='groupCloseDate']").parent().find("span").html(group.groupCloseDate);
	//签证国家
	$(obj).parent().parent().find("input[name='visaCountry']").val(group.visaCountry);
	$(obj).parent().parent().find("input[name='visaCountry']").parent().find("span").html(group.visaCountry);
	//团号
	$(obj).parent().parent().find("input[name='groupCode']").val(group.groupCode);
	$(obj).parent().parent().find("input[name='groupCode']").parent().find("span").html(group.groupCode);
	//同行价-成人
	$(obj).parent().parent().find("input[name='settlementAdultPrice']").val(getFloatPrice(group.settlementAdultPrice));
	if (spanRMlength($(obj).parent().parent().find("input[name='settlementAdultPrice']").parent().find("span[class=rm]").length)) {
		if (group.settlementAdultPrice != 0) {
			$(obj).parent().parent().find("input[name='settlementAdultPrice']").parent().append("<span class='tdred'>" + getFloatPrice(group.settlementAdultPrice) + "</span>");
		}
	} else {
		$(obj).parent().parent().find("input[name='settlementAdultPrice']").parent().find("span[class!='rm']").html((getFloatPrice(group.settlementAdultPrice)));
	}
	//同行价-儿童
	$(obj).parent().parent().find("input[name='settlementcChildPrice']").val(getFloatPrice(group.settlementcChildPrice));
	if (spanRMlength($(obj).parent().parent().find("input[name='settlementcChildPrice']").parent().find("span[class=rm]").length)) {
		if (group.settlementcChildPrice != 0) {
			$(obj).parent().parent().find("input[name='settlementcChildPrice']").parent().append("<span class='rm'><span class='tdred'>" + (getFloatPrice(group.settlementcChildPrice)) + "</span></span>");
		}
	} else {
		$(obj).parent().parent().find("input[name='settlementcChildPrice']").parent().find("span[class!='rm']").html((getFloatPrice(group.settlementcChildPrice)));
	}
	//同行价-特殊人群
	$(obj).parent().parent().find("input[name='settlementSpecialPrice']").val(getFloatPrice(group.settlementSpecialPrice));
	if (spanRMlength($(obj).parent().parent().find("input[name='settlementSpecialPrice']").parent().find("span[class=rm]").length)) {
		if (group.settlementSpecialPrice != 0) {
			$(obj).parent().parent().find("input[name='settlementSpecialPrice']").parent().append("<span class='rm'><span class='tdred'>" + (getFloatPrice(group.settlementSpecialPrice)) + "</span></span>");
		}
	} else {
		$(obj).parent().parent().find("input[name='settlementSpecialPrice']").parent().find("span[class!='rm']").html((getFloatPrice(group.settlementSpecialPrice)));
	}
	//直客价-成人
	$(obj).parent().parent().find("input[name='suggestAdultPrice']").val(getFloatPrice(group.suggestAdultPrice));
	if (spanRMlength($(obj).parent().parent().find("input[name='suggestAdultPrice']").parent().find("span[class=rm]").length)) {
		if (group.suggestAdultPrice != 0) {
			$(obj).parent().parent().find("input[name='suggestAdultPrice']").parent().append("<span class='rm'><span class='tdblue'>" + (getFloatPrice(group.suggestAdultPrice)) + "</span></span>");
		}
	} else {
		$(obj).parent().parent().find("input[name='suggestAdultPrice']").parent().find("span[class!='rm']").html((getFloatPrice(group.suggestAdultPrice)));
	}
	//直客价-儿童
	$(obj).parent().parent().find("input[name='suggestChildPrice']").val(getFloatPrice(group.suggestChildPrice));
	if (spanRMlength($(obj).parent().parent().find("input[name='suggestChildPrice']").parent().find("span[class=rm]").length)) {
		if (group.suggestChildPrice != 0) {
			$(obj).parent().parent().find("input[name='suggestChildPrice']").parent().append("<span class='rm'><span class='tdblue'>" + (getFloatPrice(group.suggestChildPrice)) + "</span></span>");
		}
	} else {
		$(obj).parent().parent().find("input[name='suggestChildPrice']").parent().find("span[class!='rm']").html((getFloatPrice(group.suggestChildPrice)));
	}
	//直客价-特殊人群
	$(obj).parent().parent().find("input[name='suggestSpecialPrice']").val(getFloatPrice(group.suggestSpecialPrice));
	if (spanRMlength($(obj).parent().parent().find("input[name='suggestSpecialPrice']").parent().find("span[class=rm]").length)) {
		if (group.suggestSpecialPrice != 0) {
			$(obj).parent().parent().find("input[name='suggestSpecialPrice']").parent().append("<span class='rm'><span class='tdblue'>" + (getFloatPrice(group.suggestSpecialPrice)) + "</span></span>");
		}
	} else {
		$(obj).parent().parent().find("input[name='suggestSpecialPrice']").parent().find("span[class!='rm']").html((getFloatPrice(group.suggestSpecialPrice)));
	}
	//最大儿童数
	if (group.maxChildrenCount != 0) {
		$(obj).parent().parent().find("input[name='maxChildrenCount']").val(group.maxChildrenCount);
		$(obj).parent().parent().find("input[name='maxChildrenCount']").parent().find("span").html(group.maxChildrenCount);
	} else {
		$(obj).parent().parent().find("input[name='maxChildrenCount']").val("");
		$(obj).parent().parent().find("input[name='maxChildrenCount']").parent().find("span").html("");
	}
	//最大特殊人群数
	if (group.maxPeopleCount != 0) {
		$(obj).parent().parent().find("input[name='maxPeopleCount']").val(group.maxPeopleCount);
		$(obj).parent().parent().find("input[name='maxPeopleCount']").parent().find("span").html(group.maxPeopleCount);
	} else {
		$(obj).parent().parent().find("input[name='maxPeopleCount']").val("");
		$(obj).parent().parent().find("input[name='maxPeopleCount']").parent().find("span").html("");
	}
	//定金
	$(obj).parent().parent().find("input[name='payDeposit']").val(getFloatPrice(group.payDeposit));
	if (spanRMlength($(obj).parent().parent().find("input[name='payDeposit']").parent().find("span[class=rm]").length)) {
		if (group.payDeposit != 0) {
			$(obj).parent().parent().find("input[name='payDeposit']").parent().append("<span class='rm'><span class='tdorange'>" + (getFloatPrice(group.payDeposit)) + "</span></span>");
		}
	} else {
		$(obj).parent().parent().find("input[name='payDeposit']").parent().find("span[class!='rm']").html((getFloatPrice(group.payDeposit)));
	}
	//单房差
	$(obj).parent().parent().find("input[name='singleDiff']").val(getFloatPrice(group.singleDiff));
	if (spanRMlength($(obj).parent().parent().find("input[name='singleDiff']").parent().find("span[class=rm]").length)) {
		if (group.singleDiff != 0) {
			$(obj).parent().parent().find("input[name='singleDiff']").parent().append("<span class='rm'><span class='tdred'>" + (getFloatPrice(group.singleDiff)) + "</span></span>");
		}
	} else {
		$(obj).parent().parent().find("input[name='singleDiff']").parent().find("span[class!='rm']").html((getFloatPrice(group.singleDiff)));
	}
	//预收
	$(obj).parent().parent().find("input[name='planPosition']").val(group.planPosition);
	$(obj).parent().parent().find("input[name='planPosition']").parent().find("span").html(group.planPosition);
	//余位
	$(obj).parent().parent().find("input[name='freePosition']").val(group.freePosition);
	$(obj).parent().parent().find("input[name='freePosition']").parent().find("span").html(group.freePosition);

	//0258-新增发票税-列表-s//
	$(obj).parent().parent().find("input[name='invoiceTax']").val(group.invoiceTax);
	//将td中第1个span里的内容变为修改后的值.
	$(obj).parent().parent().find("input[name='invoiceTax']").parent().find("span").eq(0).html(group.invoiceTax + "&nbsp;%");
	//0258-新增发票税-列表-e//

	$(obj).parent().parent().find("input[name='visaDate']").val(group.visaDate);
	$(obj).parent().parent().find("input[name='visaDate']").parent().find("span").html(group.visaDate);

	$(obj).parent().parent().find("span").show();
	$(obj).parent().parent().find("input[type='text']").css("display", "none");

	//0258-发票税-列表-s//
	//将td中发票税文本框后的span进行隐藏
	$(obj).parent().parent().find("input[name='invoiceTax']").parent().find("span").eq(1).css("display", "none");
	//0258-发票税-列表-e//

	$(obj).parent().parent().find("input[type='text']").attr("disabled", true);
	$(obj).parent().parent().find("input[type='checkbox']").attr("disabled", true);
	//复选框
	if($(obj).parent().parent().find('.g-w').hasClass('withdraw')){
		$(obj).parent().parent().find('input[type="checkbox"]').show().removeAttr('disabled');
	}
	//备注
	var remark = $(obj).parent().parent().next().find('[name="groupNote"]').val();
	$(obj).parent().parent().next().find('[name="groupNoteContent"]').text(remark);
	$(obj).parent().parent().next().find('div:first').show().next().hide();

	//$(obj).parent().parent().find('.expandNotes').text('展开备注');
	//$(obj).parent().parent().find('.expandNotes').show();
	if (isOnlinePage != null && isOnlinePage == "0") {//产品上架页调用
		$(obj).parent().parent().next().hide();//隐藏备注行
	}

	if (remark == null || remark == 'undefined' || remark == '') {
		$(obj).parent().parent().find('.groupNoteTipImg').hide();
	} else {
		$(obj).parent().parent().find('.groupNoteTipImg').show();
	}
	//更多操作按钮出现
	$(obj).parent().find('dl.more-op-style').show();
	//修改按钮出现
	$(obj).parent().find('a[name="modbtn"]').show();
	//取消按钮隐藏
	$(obj).parent().find('a[name="cancelbtn"]').hide();
	//保存按钮隐藏
	$(obj).hide();
	//判断该开发商是否具有t1上架权限，如果有上架权限，则显示以下按钮
	if(shelfRightsStatus == 0){
		//设定定价策略按钮
		$(obj).parent().find('.setting-btn').show();
		//518如果状态为已下架，则显示平台上架按钮
		if($(obj).parent().parent().find('.g-w').hasClass('withdraw')){
			//平台上架按钮出现
			$(obj).parent().find('.stack-btn').show();
			//复选框出现
			$(obj).parent().find("input[type='checkbox']").show();
		}
		if($(obj).parent().parent().find('.g-w').hasClass('grounding')){
			//如果为上架状态，则出现价格表
			$tr.find('.morePrice-parent').show();
		}
	}
}


/**
 * 点击保存后是否直接上架到t1平台
 * @param flag  是否修改了同行价或直客价标志
 * @param obj       addby:djw
 */
function onLineProductToT1(flag,obj){
	var groupId = $(obj).parent().parent().find('input[name="groupCode"]').attr("id");
	if(flag != 1){
		var submit = function (v, h, f) {
			if (v == true) {
				//上架
				//stack($tr);
				$.ajax({
					type:"post",
					url:url + "/activity/manager/confimIsT1On",
					async:false,
					contentType: "application/x-www-form-urlencoded;charset=utf-8",
					data:{groupId:groupId},
					success:function(){
						//$(obj).parent().find()
						//保存按钮隐藏
						$(obj).css("display","none");
						//标签改变
						$(obj).parent().parent().find('.g-w').removeClass('withdraw').addClass('grounding');
						$(obj).parent().parent().find('.grounding-one').html();
						$(obj).parent().parent().find('.grounding-one').html('已上架旅游交易系统');
						$(obj).parent().parent().find('.grounding-one').removeClass('withdraw-hover').addClass('grounding-hover');
						//复选框隐藏
						$(obj).parent().parent().find('input[type="checkbox"]').attr('disabled','disabled');
						//设定定价策略按钮
						$(obj).parent().parent().find('.setting-btn').show();
						//平台上架按钮隐藏
						$(obj).parent().find('.stack-btn').hide();
						//更多价格隐藏//如果为上架状态，则出现价格表
						$(obj).parent().find('.morePrice-parent').css('display','block');
						top.$.jBox.info("上架成功！");
					},
					error:function(){
						top.$.jBox.info("上架失败！");
					}
				});
			}else{
				//window.location.reload();
				//withdraw($tr);
			}
		};
		jBox.warning("保存成功，是否上架旅游交易预订系统?", "提示", submit, {
			//closed:withdraw($tr),
			buttons: {
				'否':false,
				'是': true
			}
		});
	}else{
		var submit = function (v, h, f) {
			if (v == true) {
				return true;
			}
		};
		jBox.warning("保存成功，因修改同行价或直客价，请重新设置定价策略上架旅游交易预订系统。", "提示", submit, {
			buttons: {
				'确定': true
			}
		});
	}
}

//平台上架
function grounding(groupId,obj){
	var title;
	$.ajax({
		type:"post",
		url:url + "/activity/manager/checkPriceStatus",
		data:{groupId:groupId},
		success:function(result){
			if(result.flag){
				title = "上架失败，因调整同行价或直客价请重新调整策略";
			}else {
				title = "上架成功";
				$.ajax({
					type: "POST",
					url: url + "/activity/manager/confimIsT1On",
					data: {groupId:groupId},
					success: function (data) {
						if (data.flag) {
							//$.jBox.tip('操作成功！');
							$(obj).css("display","none");
							$(obj).parent().parent().find('.g-w').removeClass('withdraw').addClass('grounding');
							$(obj).parent().parent().find('.grounding-one').html();
							$(obj).parent().parent().find('.grounding-one').html('已上架旅游交易系统');
							$(obj).parent().parent().find('.grounding-one').removeClass('withdraw-hover').addClass('grounding-hover');
							$(obj).parent().parent().find('input[type="checkbox"]').attr('disabled','disabled');
							$(obj).parent().find('.morePrice-parent').css('display','block');
						} else {
							$.jBox.tip('操作失败' + data.msg, 'error');
						}
					}
				})
			}
			$.jBox.info(title,"提示",{
				buttons: {"关闭": "0"},
				submit:function(v,h,f)
				{
					if (v == '0') {
						return true;
					}
				}
			})
		}
	})
}

//批量平台上架
function batchGrounding(obj){
	var length = $(obj).parent().prev().find(".withdraw-relative input[type='checkbox']:checked").length;
	if(length==0){
		$.jBox.tip('请选择要操作的数据','warnning');
		return false;
	}
	var $check = $(obj).parent().parent().find("input[name='groupNo']:checked").not(":disabled").next().next("input[name='groupCode']");
	var ids = "";
	$check.each(function(obj){
		ids = ids + $(this).attr('id') + ",";
	});
	var i = 0;
	$.ajax({
		type:"post",
		url:url + "/activity/manager/batchCheckPriceRecord",
		data:{groupIds : ids},
		success:function(result){
			if(result.flag){
				i++;
				//$.jBox.tip('请选择要操作的数据','warnning');
				$.jBox.info(result.msg, "提示", {
					buttons: {"确定": "0"},
					submit:function(v,h,f)
					{
						if (v == '0') {
							return;
						}
					}
				})
			}else {
				$check.each(function(obj){
					var $obj =  $(this);
					var groupId = $obj.attr('id');
					$.ajax({
						type: "POST",
						url: url + "/activity/manager/confimIsT1On",
						data: {groupId:groupId},
						success: function (data) {
							if (data.flag) {
								//$(obj).css("display","none");
								$obj.parent().parent().find('.g-w').removeClass('withdraw').addClass('grounding');
								$obj.parent().parent().find('.grounding-one').html();
								$obj.parent().parent().find('.grounding-one').html('已上架旅游交易系统');
								$obj.parent().parent().find('.grounding-one').removeClass('withdraw-hover').addClass('grounding-hover');
								$obj.parent().parent().find('input[type="checkBox"]').removeAttr("checked");
								$obj.parent().parent().find('input[type="checkBox"]').attr("disabled","true");
								$obj.parent().parent().parent().find('.stack-btn').hide();
								$obj.parent().parent().parent().find('.morePrice-parent').css('display','block');
							} else {
								$.jBox.tip('操作失败,原因:' + data.msg, 'error');

							}
						}
					})
				});
			}
		}
	});

	/*if (i > 0){
		return;
	}
	debugger;
	$check.each(function(obj){
		var $obj =  $(this);
		var groupId = $obj.attr('id');
		$.ajax({
			type: "POST",
			url: url + "/activity/manager/confimIsT1On",
			data: {groupId:groupId},
			success: function (data) {
				if (data.flag) {
					//$(obj).css("display","none");
					$obj.parent().parent().find('.g-w').removeClass('withdraw').addClass('grounding');
					$obj.parent().parent().find('.grounding-one').html();
					$obj.parent().parent().find('.grounding-one').html('已上架旅游交易系统');
					$obj.parent().parent().find('.grounding-one').removeClass('withdraw-hover').addClass('grounding-hover');
					$obj.parent().parent().find('input[type="checkBox"]').attr("disabled","true");
					$obj.parent().parent().parent().find('.stack-btn').hide();
					$obj.parent().parent().parent().find('.morePrice').css('display','block');
				} else {
					$.jBox.tip('操作失败,原因:' + data.msg, 'error');

				}
			}
		})
	});*/
	//window.location.reload();
}

function spanRMlength(length){
	if(length==0){
		return true;
	}else{
		return false;
	}
}


//把后台传回来的价格信息变成带有两位小数的数字
function getFloatPrice(obj) {
	var price = obj + "";
	if(0 == price.length || null == price || 0 == price) {
		return "";
	}else{
		if(price.indexOf(".") < 0) {
			return xround(obj,2);
		}else{
			return xround(obj,2);
		}
	}
}

/**
 * 转化后台数据quauq价  转化规则:null和""转化为""  数字字符串转化为数字
 */
function getFloatPriceForQuauq(obj){
	if(obj == null){
		return "";
	}else{
		var price = obj + "";
		if(0 == price.length || null == price) {
			return "";
		}else{
			return xround(obj,2);
		}
	}
}




/**
 *四舍五入，保留位数为
 *num:要格式化的数字 
 *scale: 保留的位数
 */
function xround(num,scale){
    var resultTemp = Math.round(num * Math.pow(10, scale)) / Math.pow(10, scale);
    return resultTemp.toFixed(2);
}







//生成币种ID字符串
function createCurrencyIdStr() {
	$("#contentTable").children("tbody").find("tr").each(function(index,obj){
		var currencyIdStr = "";
		$(obj).find("input").each(function(index1,obj1) {
			if($(obj1).hasClass("ipt-currency")) {
    			currencyIdStr += $(obj1).attr("var") + ",";
    			}
    		});
   			currencyIdStr = currencyIdStr.substring(0,currencyIdStr.length-1);
   			if($(obj).find("[name='groupCurrencyType']").length != 0) {
   				$(obj).find("[name='groupCurrencyType']").remove();
   			}
    		$(obj).append("<input type='hidden' name='groupCurrencyType' value='"+currencyIdStr+"' />");
    });
}



//上传文件时，点击后弹窗进行上传文件(多文件上传)
//方法参数：${ctx}、显示上传文件名称input的id、当前点击元素
function uploadFiles(ctx, inputId, obj) {
	var fls=flashChecker();
	var s="";
	if(fls.f) {
//		alert("您安装了flash,当前flash版本为: "+fls.v+".x");
	} else {
		alert("请您退出当前浏览器,并安装当前浏览器的flash player软件,然后才能上传文件"); 
		return;
	}
	
	//新建一个隐藏的div，用来保存文件上传后返回的数据
	if($(obj).parent().find(".uploadPath").length == 0) {
		$(obj).parent().append('<div class="uploadPath" style="display: none" ></div>');
		$(obj).parent().append('<div id="currentFiles" style="display: none" ></div>');		
	}
	
	$(obj).addClass("clickBtn");
	
	/*移除产品行程校验提示信息label标签*/
	$("#modIntroduction").remove();
	
	$.jBox("iframe:"+ ctx +"/activity/manager/uploadFilesPage", {
	    title: "多文件上传",
		width: 340,
   		height: 365,
   		buttons: {'关闭':true},
   		persistent:true,
   		loaded: function (h) {},
   		submit: function (v, h, f) {
			$(h.find("iframe")[0].contentWindow.file_upload).next("p").find(".successUpload").click();
			if($(obj).parent().find("#currentFiles").find("[name='docOriName']").length != 0) {
				/*添加<li>之前，先将之前的<li>删除，然后再累加，以防止重复累加问题*/
//				if($(obj).attr("name") != 'costagreement'){
//					$(obj).next(".batch-ol").find("li").remove();
//				}
				
				$(obj).parent().find("#currentFiles").find("[name='docOriName']").each(function(index, obj1) {
					//如果是产品行程介绍
					if($(obj).attr("name") == 'introduction') {
						$(obj).next().next("#introductionVaildator").val("true").trigger("blur");
					}
					//如果是签证资料的文件上传
					if($(obj).attr("name").indexOf("signmaterial") >= 0) {
						$(obj).parent().parent().parent().parent().next(".batch-ol").append('<li><span style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;max-width:173px;display:inline-block;float: left;" title="'+$(obj1).val() +'">'+ $(obj1).val() +'</span><a class="batchDel" style="float:left;" href="javascript:void(0)" onclick="deleteFileInfo(\''+ $(obj1).prev().val() +'\',\''+ $(obj).attr("name") +'\',this);">删除</a></li>');
					}else{
						$(obj).next(".batch-ol").append('<li><span style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;max-width:173px;display:inline-block;float: left;" title="'+$(obj1).val() +'">'+ $(obj1).val() +'</span><a class="batchDel"  style="float:left;" href="javascript:void(0)" onclick="deleteFileInfo(\''+ $(obj1).prev().val() +'\',\''+ $(obj).attr("name") +'\',this);">删除</a><br></li>');
					}
				});
				if($(obj).parent().find("#currentFiles").children().length != 0) {
					$(obj).parent().find("#currentFiles").children().remove();
				}
			}
			
			$(".clickBtn",window.parent.document).removeClass("clickBtn");
   		}
	});
	$("#jbox-content").css("overflow", "hidden");
	$(".jbox-close").hide();
}
//删除现有的文件
function deleteFileInfo(inputVal, objName, obj) {
	top.$.jBox.confirm('确定要删除该文件？','系统提示',function(v){
		if(v=='ok'){
			if(inputVal != null && objName != null) {
				var delInput = $(".uploadPath").find("input[name='"+ objName +"'][value='"+ inputVal +"']");
				delInput.next().eq(0).remove();
				delInput.next().eq(0).remove();
				delInput.remove();
				
				/*删除上传文件后，文件信息会存放在id为currentFiles的div中，也需要把该div相关的上传文件信息删除*/
				var docName = $(obj).parent("li").parent("ol").parent().find("#currentFiles").find("input[name='"+ objName +"'][value='"+ inputVal +"']");
				docName.next().eq(0).remove();
				docName.next().eq(0).remove();
				docName.remove();
			
				
			}else if(inputVal == null && objName == null) {
				$(obj).parent().remove();
			}
			$(obj).parent("li").remove();
			
			//如果是产品行程介绍文件删除的话，需要进行必填验证
			if("introduction" == objName) {
				if(0 == $("#introductionVaildator").prev(".batch-ol").find("li").length) {
					$("#introductionVaildator").val("").trigger("blur");
				}
			}
		}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');		
}

//发布产品-交通方式-航空-关联机票产品
//机票产品信息是否为空,默认是
var isBlank;
function linkAirTicket1(ctx, oldProductCode){
	if(!oldProductCode) {
		oldProductCode = "";
	}
    var html = '<div class="add_allactivity"><label>输入机票产品编号：</label>';
    html += '<input type="text" id="productCode" value="' + oldProductCode + '"/>';
    html += '</div>';
    isBlank = true;
    $.jBox(html, { title: "选择机票产品",buttons:{"提 交":"1"}, submit:function(v, h, f){
        if (v=="1"){
        	var productCode = $("#productCode").val();
        		if(productCode) {
        			getAirTicketInfo(productCode, ctx);
	            //如果没有机票产品
	        	if(isBlank) {
		            h.find('.nothisPro').hide('fast', function () { $(this).remove(); });
		            $('<p class="nothisPro" style="display: none;">没有这个产品</p>').appendTo(h).show('slow');
		            return false;
	            }else{
	            	$.jBox.info('已完成机票关联','系统提示');
	            }
        	}else{
				h.find('.nothisPro').hide('fast', function () { $(this).remove(); });
	            $('<p class="nothisPro" style="display: none;">必填信息</p>').appendTo(h).show('slow');
	            return false;
        		}
        }
    },height:180,width:500});
}
	
function getAirTicketInfo(productCode, ctx) {
	$.ajax({
		type:"POST",
		url: ctx + "/activity/manager/getAirticketByProCode?productCode="+productCode,
		async : false,
		success:function(result){
		if(result != null && result.length != 0) {
			isBlank = false;
			//清空先前查询的机票产品信息
			if($(".airInfo").find("div").length != 0) {
				$(".airInfo-tit1").remove();
				$(".airInfo-con").remove();
				$(".otherInfo").remove();
			}
			var airticketAreaType;
			var ticketAreaTypeArr = ["","内陆","国际","内陆+国际","国内"];		
			switch(result[0].airType) {
				case '3':airticketAreaType = "单程" + ticketAreaTypeArr[result[1].ticketAreaType]; break;
				case '2':airticketAreaType = "往返"; break;
				case '1':airticketAreaType = "多段"; break;
				default:airticketAreaType = "无";
			}
			$(".airInfo").append('<div class="airInfo-tit1">航空行程：'+ result[0].leaveCountry + '-' + result[0].destination + '(' + airticketAreaType + ')</div>');
			var outerHtml = $('<div class="airInfo-con"></div>');
			//航段数中文只能处理十以内
			var charArr = ["零","一","二","三","四","五","六","七","八","九","十"];
			var innerHtmlArr = new Array(result.length);
				for(var innerCount = 1; innerCount < result.length;) {
					var innerData = result[innerCount];
					var innerHtml = $('<div class="title_samil">第' + charArr[innerData.number] + '段：' + ticketAreaTypeArr[result[innerCount].ticketAreaType] + '</div>'
							+ '<div class="seach25"><p>航空公司：</p><p class="seach_r"><span class="disabledshowspan">'
							+ innerData.airlines
								+ '</span></p></div><div class="seach25"><p>航班号：</p><p class="seach_r"><span class="disabledshowspan">'
							+ innerData.flightNumber
								+ '</span></p></div><div class="kong"></div><div class="seach25"><p>出发城市：</p><p class="seach_r"><span class="disabledshowspan">'
							+ innerData.leaveAirport
								 + '</span></p></div><div class="seach25"><p>出发时刻：</p><p class="seach_r"><span class="disabledshowspan">'
							+ innerData.startTime
								 + '</span></p></div><div class="seach25"><p>舱位等级：</p><p class="seach_r"><span class="disabledshowspan">'
							+ innerData.spaceGrade
								 + '</span></p></div><div class="kong"></div><div class="seach25"><p>到达城市：</p><p class="seach_r"><span class="disabledshowspan">'
							+ innerData.destinationAirpost
								 + '</span></p></div><div class="seach25"><p>到达时刻：</p><p class="seach_r"><span class="disabledshowspan">'
							+ innerData.arrivalTime
								 + '</span></p></div><div class="seach25"><p>舱位：</p><p class="seach_r"><span class="disabledshowspan">'
							+ innerData.airspace
								 + '</span></p></div>');
					innerHtmlArr[parseInt(innerData.number)] = innerHtml;
					innerCount++;
				}
				for(var keyCon = 1; keyCon <= innerHtmlArr.length; keyCon++) {
					$(outerHtml).append(innerHtmlArr[keyCon]);
				}
				var ht="<div class=\"otherInfo\"><div><p class=\"seach_r\">联运类型：<span class=\"disabledshowspan\">"+ result[0].intermodalType+"</span></p></div><div ><p  class=\"seach_r\">出票日期：" +
						"<span class=\"disabledshowspan\">"+result[0].outTicketTime+"</span></p></div><div><p  class=\"seach_r\">预收人数：" +
								"<span class=\"disabledshowspan\">"+result[0].reservationsNum+"</span></p></div></div>" ;
				$(".airInfo").append(outerHtml).append(ht);
				if($(".activityAirTicketId").length != 0) {
					$(".activityAirTicketId").val(result[0].airticketId);
				}
			}
		}
	});
}

//出团通知文件上传    0234 屏蔽上传出团通知
//function uploadGroupFile(ctx, obj) {
//	$(obj).addClass("clickA");
//	var docId = "";
//	if($(obj).attr("id") != undefined && $(obj).attr("id") != "")
//		docId = $(obj).attr("id");
//	
//    var iframe = "iframe:" + ctx + "/activity/manager/uploadFileForm?docId="+docId;
//    $.jBox(iframe, {
//            title: "上传出团通知",
//            width: 350,
//            height: 300,
//            buttons: {}
//     });
//    $("#jbox-content").css("overflow", "hidden");
//     return false;
//}

function validatorFloat(obj){
	var money = obj.value;  
	if(money=="") {obj.value="0"};
	var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
	var txt = ms.split(".");
	if("" == txt[0]){
		txt[0] = "0";
	}
	obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
}

(function($){
	$.validator.addMethod("areaRequired", function(value,element){
        var inputValue = $(element).next().find(".custom-combobox-input").val();
        if(" " == value || ("" == inputValue)){
			return false;
		}else{
			return true;
		}
    },"必填信息");
 })(jQuery);


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
