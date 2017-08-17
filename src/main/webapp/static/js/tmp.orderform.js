/*!
 * JQuery for the module of orderform(订单模块的脚本)
 *
 * Depends:common.js
 * the public functions used by this module:noTraveler、getBirthday、formhtml、addCosts、deleteCosts、textBoxAddPeople、
 * 	   textBox_del、sumInput、mealSelection、CurrencyMoney、isFloat、milliFormat、showBigImg、refundInputs、validNumFinally、validNum、inquiryCheckBOX
 *
 * Date: 2015-01-15
 */

/*=============订单-单团、散拼、海岛游修改 begin===============*/
/*游客类型、单价、出行人数、币种
  @type:1-成人；2-儿童，3-特殊人群
*/
var priceAndNum = [{'type':1,'currencyType':"人民币",'price':0,'amount':0},{'type':2,'currencyType':"人民币",'price':0,'amount':0},{'type':3,'currencyType':"人民币",'price':0,'amount':0}];
function preOrder(){
	$("#traveler").on("click",".visitorTit .visitorTit-label",function(){//游客信息展开收起
		var $span = $(this).find("span").eq(0);
		var $parent = $(this).parents(".textBox");
		if($span.hasClass("efx-expand")){//如果是展开状态
			$parent.find(".forVisitorName").text($parent.find("[name='travelerName']").val());
			$parent.find(".visitorTit-off").show();
			$parent.find(".visitorTit-on").hide();
			$parent.find(".visitorCon").hide();
			$span.removeClass("efx-expand");
		}else{
			$parent.find(".visitorTit-off").hide();
			$parent.find(".visitorTit-on").show();
			$parent.find(".visitorCon").show();
			$span.addClass("efx-expand");
		}
	}).on("click",".visitorCon-tit .visitorTit-label",function(){//基本信息展开收起
		var $span = $(this).find("span").eq(0);
		var $parent = $(this).parent();
		if($span.hasClass("efx-expand")){//如果是展开状态
			$parent.next("[flag='messageDiv']").hide();
			$span.removeClass("efx-expand");
		}else{
			$parent.next("[flag='messageDiv']").show();
			$span.addClass("efx-expand");
		}
	}).on("change",".travelerTable [name='airTicket']",function(){//是否需要机票
		var thisValue = $(this).val();
		var $dd = $(this).parents(".visitorCon-dl").find("dd");
		if("1" == thisValue){
			$dd.show();
		}else{
			$dd.hide();
		}
		if("1" == $dd.find("[name='intermodalType']:checked").val() && "-1" != $dd.find("[name='airLink']").val()){
			calculateSingleTraveler($(this).parents("form"));
		}
	}).on("change",".travelerTable [name='airLink']",function(){//联运价格变化
		var thisValue = $(this).val();
		if("-1" == thisValue){
			$(this).next("span").hide();
		}else{
			var $showSpan = $(this).next("span");
			var array = thisValue.split("-");
			$showSpan.find("i").text(array[0]);
			$showSpan.find("em").text(milliFormat(array[1].replace(/[ ]/g,"").replace(/,/g,"")),1);
			$showSpan.show();
		}
		calculateSingleTraveler($(this).parents("form"));
	}).on("focus",".travelerTable [name='otherCost']",function(){//其他费用为0，获得焦点清空数据，失去焦点为0
		var thisValue = $(this).val();
		if("0" == thisValue){
			$(this).val("");
		}
	}).on("change",".travelerTable [name='personTypeinner']",function(){//更改游客类型
		calculateSingleTraveler($(this).parents("form"),0);
	}).on("change",".travelerTable [name='housingDemand']",function(){//住房要求
		var $singleRoom = $(this).parent().nextAll('.singleRoom');
		if($(this).val()==1){
			$singleRoom.hide();
			$singleRoom.nextAll("label").show();
		}else{
			$singleRoom.show();	
			$singleRoom.nextAll("label").hide();
		}
		calculateSingleTraveler($(this).parents("form"));
	}).on("click",".travelerTable [name='addbed']",function(){//是否加床
		calculateSingleTraveler($(this).parents("form"));
	}).on("click",".travelerTable [name^='Lmeal']",function(){//是否升餐
		var value = $(this).val();
		if("1" == value){
			$(this).parents(".otherExpenses-list").find('.orMeal-term').show();
		}else{
			$(this).parents(".otherExpenses-list").find('.orMeal-term').hide();	
		}
		calculateSingleTraveler($(this).parents("form"));
	}).on("change",".travelerTable [data='currencyType']",function(){//其他费用更换币种
		calculateSingleTraveler($(this).parents("form"));
	}).on("click",".textBox-delete",function(){//删除游客
		var $form = $(this).parents("form.travelerTable");
		$.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				//是否包含撤签
				if($form.find("[data-hadOrderd='1']").length){
					var html = '<div style="margin-top:20px; font-size:20px; text-align:center;">';
					html += '是否提交撤签申请';
					html += '</div>';
					$.jBox(html, { title: "签证已送签",persistent:true,buttons:{'确定': 1,'取消': 0}, submit:function(v, h, f){
						if (v=="1"){
							return true;
						}else{
							
						}
					},height:150,width:380});
				}			
				$form.remove();
				$("#traveler form").each(function(index,element){
					$(element).find(".visitorTit-label i").text(index+1);
					//其他费用名的id重置
					$(element).find("[name='costName']").each(function(i,obj){
						var ID = $(obj).attr("id").replace(/\d+\-\d+$/,(index+1)+"-"+(i+1));
						$(obj).attr("id",ID);
						$(obj).next("label.error").attr("for",ID);
					});
				});
				//暂无游客信息的显示与隐藏
				noTraveler();
				//添加游客的显示与隐藏
				isAddAllTraveler();
				//计算总价
				calculateTotalMoney();
				//提示信息
				top.$.jBox.tip('删除成功', 'success');
				
//				if (travelerId == "") {
//					// 无需记录
//					$this.closest('.travelerTable').remove();
//					changeTotalPrice();
//				}else{
//					// 需要删除数据库中记录
//					$.ajax( {
//						type : "POST",
//						url : "../../order/manage/deleteTraveler",
//						data : {
//							travelerId : travelerId
//						},
//						success : function(msg) {
//							top.$.jBox.tip('删除成功', 'success');
//							$this.closest('.travelerTable').remove();
//							changeTotalPrice();
//						}
//					});
//				}
			}else if(v == 'cancel'){}
		});
	}).on("click",".btn-save",function(){
		var $this = $(this);
		var btnTxt = $this.text();
		var $form = $this.parents("form");
		if("保 存" == btnTxt){
			//添加表单验证
			var isvalidate = $form.validate({ignore:""}).form();
			if(isvalidate){
				$form.disableContainer( {
					blankText : "—",
					selectIsVisible:0
					//formatNumber : formatNumberArray
				});
				$form.find(".textBox-delete").hide();
				$form.find(".deleteCosts").hide();
				$form.find(".addCosts").hide();
				$form.find(".tourist-info1").addClass("tourist-info1Txt");
				//是否加床
				$form.find("[name='addbed']").each(function(index,element){
					if($(element).is(":checked")){
						$(element).hide();
					}else{
						$(element).parent().hide();
						$(element).parent().next().hide();
					}
				});
				//自备签
				$form.find("[name='zibeiqian']").each(function(index,element){
					$(element).hide();
					if(!$(element).is(":checked")){
						$(element).next("label").hide();
					}
				});
				$this.text("修 改");
			}else{
				top.$.jBox.tip('“游客' + $form.find(".visitorTit .visitorTit-label i").text() +'”中有未填写的必填项！', 'error');
			}
		}else{
			$form.undisableContainer( {
				blankText : "—"//,
				//formatNumber : formatNumberArray
			});
			$form.find(".textBox-delete").show();
			$form.find(".deleteCosts").show();
			$form.find(".addCosts").show();
			$form.find(".tourist-info1").removeClass("tourist-info1Txt");
			//是否加床
			$form.find("[name='addbed']").each(function(index,element){
				if($(element).is(":checked")){
					$(element).show();
				}else{
					$(element).parent().show();
					$(element).parent().next().show();
				}
			});
			//自备签
			$form.find("[name='zibeiqian']").each(function(index,element){
				$(element).show();
				if(!$(element).is(":checked")){
					$(element).next("label").show();
				}
			});
			$this.text("保 存");
		}
	}).on("click",".btn-appAll",function(){//报价应用全部
		var $form = $(this).parents("form");
		//被复制项标志
		$form.attr("isCopied",1);
		var $otherCost = $form.find("[name='otherCost']");	
		$.jBox.confirm("是否将“其他费用”中所有款项应用于全部已添加的游客？<span style='color:#ff0000;'>—“费用名称”为空的不应用</span>", "提示", function(v, h, f) {
			if (v == 'ok') {
				//遍历游客的form表单
				$("#traveler form").each(function(index,element){
					if("1" != $(element).attr("isCopied")){
						var isModified = 0;
						//循环其他费用的源						
						$otherCost.each(function(i, ele) {
							var $parent = $(ele).parents(".otherExpenses");
							var $otherCost_to = $(element).find("[name='otherCost']");//被应用的其他费用
							var compareNum = 0;
							$otherCost_to.each(function(j,other){
								var $parent_other =  $(other).parents(".otherExpenses");
								if(($parent.find("[name='costName']").val() == $parent_other.find("[name='costName']").val()) && ($parent.find("[data='currencyType']").val() == $parent_other.find("[data='currencyType']").val()) && ($parent.find("[name='otherCost']").val() == $parent_other.find("[name='otherCost']").val())){//如果找到一条“费用名称”、币种、价格都相等的费用项则退出循环
									return;
								}else{
									compareNum++;
								}
							});
							if(($otherCost_to.length == compareNum) && ("" != $parent.find("[name='costName']").val())){//如果没有相同费用项，则进行复制(“费用名称”为空的不复制)
								$(element).find(".addCosts").before('<div class="otherExpenses">' + $parent.formhtml() + '</div>');
								isModified++;
							}
						});
						
						if(isModified){
							var $btnSave = $(element).find(".btn-save");
							var isTxtState = ($btnSave.text() == "保 存") ? 0 : 1;
							//$(element).find("[data='quotation']").html($quotation.formhtml());
							//计算游客模板中的结算价
							calculateSingleTraveler($(element),0);
							//如果是已保存状态，需要出发保存按钮使表单变为文本
							if(isTxtState){
								$(element).find(".btn-save").trigger("click").trigger("click");
							}
						}
					}
				});
				//计算订单总价
				calculateTotalMoney();
				$form.removeAttr("isCopied");
			}else if(v == 'cancel'){}
		});
	});
	
	//自备签类型
	ydbz2zibeiqian();
	
	//初始化priceAndNum
	//单价、币种初始化
	//儿童
	if($("#etj").length){
		var array = $("#etj").attr("price").split("-");
		priceAndNum[1].currencyType = array[0];
		priceAndNum[1].price = isFloat(array[1]);
	}
	//成人
	if($("#crj").length){
		var array = $("#crj").attr("price").split("-");
		priceAndNum[0].currencyType = array[0];
		priceAndNum[0].price = isFloat(array[1]);
	}
	//特殊人群
	if($("#tsj").length){
		var array = $("#tsj").attr("price").split("-");
		priceAndNum[2].currencyType = array[0];
		priceAndNum[2].price = isFloat(array[1]);
	}
	//出行人数初始化
	$("[id^='orderPersonNum']").each(function(index, element) {
		setTravelerAmount($(element).attr("id"),$(element).val());
    });
	//修改出行人数
	$("[id^='orderPersonNum']").blur(function(){
		//如果总出行人数超出总舱位数，则设置可行的最大值
		var $this = $(this);
		var total_new = 0;
		var hastravelerNum = $("#traveler").find("form").length;//已填写游客的个数
		$("[id^='orderPersonNum']").each(function(index,element){
			total_new += Number($(element).val().replace(/ /g,""));
		});
		if(total_new < hastravelerNum){
			var minNum = Number($this.val().replace(/ /g,"")) - total_new + hastravelerNum;
			$this.val(minNum);
			//重获焦点 firefox中需使用这种方式
			setTimeout(function (){$this[0].focus();},0);
			top.$.jBox.tip('出行总人数不得小于已填写的游客数（' + minNum + '人）！', 'error');	
		}else{
			setTravelerAmount($(this).attr("id"),$(this).val());
			//“添加游客”按钮的显示与隐藏
			isAddAllTraveler();
			//计算订单总价
			calculateTotalMoney();
		}
	});
	//计算表单游客对象的结算价
	$("#traveler").find("form").each(function(index, element) {
		//重置各表单
		element.reset();
        calculateSingleTraveler($(element),0);
    });
	//计算游客模板中的结算价
	calculateSingleTraveler($("#travelerTemplate"),0);
	//计算订单总价
	calculateTotalMoney();
	//暂无游客信息的显示与隐藏
	noTraveler();
	//添加游客的显示与隐藏
	isAddAllTraveler();
	
	//添加游客
	$(".addVisitor").click(function(){
		var $this = $(this);
		var $container = $("#traveler");
		$container.append('<form class="travelerTable">' + $("#travelerTemplate").html() + '</form>');
		$container.find("form:last").find(".visitorTit-label i").text($container.find("form").length);
		//暂无游客信息的显示与隐藏
		noTraveler();
		//添加游客的显示与隐藏
		isAddAllTraveler();
		if($this.parent().hasClass("centerNew")){
			var top = $container.find("form:last").offset().top;
			$(window).scrollTop(top-45);
		}
	});
}
//保存并支付
function toSave(){
	var totalNum = 0;
	//var isTravelerPass = true;
	$.each(priceAndNum,function(index,element){
		totalNum += Number(element.amount);
	});
	if(0 === Number(totalNum)){
		top.$.jBox.tip('出行人数必须大于零', 'error');
	}else if (priceAndNum[0].amount < countAdult() || priceAndNum[1].amount < countChild() || priceAndNum[2].amount < countSpecial()){
		top.$.jBox.tip('成人、儿童或特殊人数与初始值不匹配请修改', 'error');
		return false;
	}else{
		$forms = $("#traveler form");
		$forms.each(function(index,element){
			var $btn = $(element).find(".btn-save");
			if($btn.text() == "保 存"){
				$(element).find(".btn-save").trigger("click");
			}
		});
		//联系人必填验证
		var contactPeople = $("#addForm").validate().form();
		//如果通过验证
		if(contactPeople){
			
		}else{
			top.$.jBox.tip('“预订人信息”中有未填写的必填项！', 'error');
		}
	}
}
//添加游客的显示与隐藏
function isAddAllTraveler(){
	var total = 0;
	$.each(priceAndNum,function(index,obj){
		total += Number(priceAndNum[index].amount);
	});
	//添加游客的显示与隐藏
	if($("#traveler form").length >= total){
		$(".addVisitor").eq(0).hide();
		$(".addVisitor").eq(1).parent().hide();
		return 1;
	}else{
		$(".addVisitor").eq(0).show();
		$(".addVisitor").eq(1).parent().show();
		return 0;
	}
}

//游客中成人的人数
function countAdult() {
	var $radios = $("#traveler input[type='radio'][name^='personType'][value=1]:checked");
	return $radios.length;
}
//游客中儿童的人数
function countChild() {
	var $radios = $("#traveler input[type='radio'][name^='personType'][value=2]:checked");
	return $radios.length;
}
//游客中特殊人群的人数
function countSpecial() {
	var $radios = $("#traveler input[type='radio'][name^='personType'][value=3]:checked");
	return $radios.length;
}
//设置出行人数
function setTravelerAmount(str,num){
	var theNum = Number(num) ? Number(num) : 0;
	if("-1" != str.indexOf("Adult")){//成人
		priceAndNum[0].amount = theNum;
	}else if("-1" != str.indexOf("Child")){//儿童
		priceAndNum[1].amount = theNum;
	}else if("-1" != str.indexOf("Special")){//特殊人群
		priceAndNum[2].amount = theNum;
	}
}
//计算单个游客的结算价
/*@$form:单个表单游客对象
  @tag:0--不计算订单总价
*/
function calculateSingleTraveler($form,tag){
	//币种类型
	var $currencyOption = $("[data='currencyType']").eq(0).find("option");
	//定义分币种对象
	var sumSingle = new CurrencyMoney($currencyOption);
	//游客类型的索引
	var indexPersonType = Number($form.find("[name='personTypeinner']:checked").val()) - 1;
	var currencyType = priceAndNum[indexPersonType].currencyType;
	var currencyPrice = priceAndNum[indexPersonType].price;
	sumSingle[currencyType] += isFloat(currencyPrice.toString().replace(/[ ]/g,"").replace(/,/g,""));
	//联运价格
	if(0 != $form.find("[name='airLink']:visible").length){
		var arr_linkMoney = $form.find("[name='airLink']").val().split("-");
		sumSingle[arr_linkMoney[0]] += isFloat(arr_linkMoney[1].replace(/[ ]/g,"").replace(/,/g,""));
	}
	$form.find("[data='quotation'] .otherExpenses").each(function(index, element) {
        if(0 == $(element).find("[name='otherCost']").length){
			//酒店房型单价
			var  hotelPrice = $(element).find(".otherExpenses-title").attr("price");
			hotelPrice = hotelPrice ? hotelPrice.split("-") : ['人民币',"0"];
			//酒店房型标准入住人数
			var  hotelCapacity = Number($(element).find(".otherExpenses-title").attr("capacity"));
			hotelCapacity = hotelCapacity ? hotelCapacity : 1;
			
			if("0" == $(element).find("[name='housingDemand']").val()){//单住
				$singleRoom = $(element).find(".singleRoom");
				if(0 != $singleRoom.length){
					var arr_nightMoney = $singleRoom.attr("price").split("-");
					arr_nightMoney = arr_nightMoney ? arr_nightMoney : ['人民币',0];
					//累计“房型价格/标准入住人数”
					sumSingle[hotelPrice[0]] += isFloat(hotelPrice[1].replace(/[ ]/g,"").replace(/,/g,"")) / hotelCapacity;
					//累计单房差
					sumSingle[arr_nightMoney[0]] += isFloat(arr_nightMoney[1].replace(/[ ]/g,"").replace(/,/g,""));
				}else{
					sumSingle[hotelPrice[0]] += isFloat(hotelPrice[1].replace(/[ ]/g,"").replace(/,/g,""));
				}
			}else{//合住
				//是否加床
				var $addbed = $(element).find("[name='addbed']:checked");
				if(0 != $addbed.length){//加床
					var arr_bedMoney = $addbed.attr("price").split("-");
					sumSingle[arr_bedMoney[0]] += isFloat(arr_bedMoney[1].replace(/[ ]/g,"").replace(/,/g,""));
				}else{//不加床
					//累计“房型价格/标准入住人数”
					sumSingle[hotelPrice[0]] += isFloat(hotelPrice[1].replace(/[ ]/g,"").replace(/,/g,"")) / hotelCapacity;
				}
			}
			//升餐
			if("1" == $(element).find("[name^='Lmeal']:checked").val()){
				var arr_mealMoney = $(element).find("[name='meal'] option:selected").attr("price").split("-");
				sumSingle[arr_mealMoney[0]] += isFloat(arr_mealMoney[1].replace(/[ ]/g,"").replace(/,/g,""));
			}
		}else{
			//其他费用
			var $ipt = $(element).find("[name='otherCost']");
			if(("" != $ipt.val()) && ("0" != $ipt.val())){
				var otherCostType = $(element).find("select option:selected").text();
				sumSingle[otherCostType] += isFloat($ipt.val());
			}
		}
    });
	//设置结算价
	var html = "";
	for(var p in sumSingle){
		if(0 != sumSingle[p]){
			if("" != html){
				html += "+";
			}
			html += '<i>' + p +'</i><strong>' + milliFormat(sumSingle[p],1) + '</strong>';
		}
	}
	$form.find("[data='sPrice']").html(html);
	if(0 != tag){
		calculateTotalMoney();
	}
}

//计算订单总价
function calculateTotalMoney(){
	//币种类型
	var $currencyOption = $("[data='currencyType']").eq(0).find("option");
	//定义分币种对象
	var totalMoney = new CurrencyMoney($currencyOption);
	$("#traveler form.travelerTable").each(function(index, element) {
        var $sprice = $(element).find("[data='sPrice']").eq(0);
		//分币种计算
		$sprice.find("i").each(function(i, obj) {
            var currencyType = $(obj).text().replace(/[ ]/g,"");
			var money = $(obj).next("strong").text().replace(/[ ]/g,"").replace(/,/g,"");
			totalMoney[currencyType] += isFloat(money);
        });
    });
	//单价*出行人数
	$.each(priceAndNum,function(index,obj){
		var number = 0;
		if(1 == priceAndNum[index].type){
			number = priceAndNum[index].amount - countAdult();
		}else if(2 == priceAndNum[index].type){
			number = priceAndNum[index].amount - countChild();
		}else{
			number = priceAndNum[index].amount - countSpecial();
		}
		totalMoney[priceAndNum[index].currencyType] += isFloat(priceAndNum[index].price * number);
	});
	//设置订单总价
	var html = '';//totalMoney
	for(var p in totalMoney){
		if(0 != totalMoney[p]){
			if("" != html){
				html += "+";
			}
			html += '<i>' + p +'</i><strong>' + milliFormat(totalMoney[p],1) + '</strong>';
		}
	}
	if("" == html){
		$("[data='totalMoney']").html(0);
	}else{
		$("[data='totalMoney']").html(html);
	}
}
/*=============订单-单团、散拼、海岛游修改 end===============*/

/*==============订单-签证修改 begin=============*/
//定义预定签证对象：应收价格、币种、办签人数
var preVisa = {'currencyType':"人民币",'price':0,'amount':0};
function preOrderVisa(){
	$("#traveler").on("click",".visitorTit .visitorTit-label",function(){//游客信息展开收起
		var $span = $(this).find("span").eq(0);
		var $parent = $(this).parents(".textBox");
		if($span.hasClass("efx-expand")){//如果是展开状态
			$parent.find(".forVisitorName").text($parent.find("[name='travelerName']").val());
			$parent.find(".visitorTit-off").show();
			$parent.find(".visitorTit-on").hide();
			$parent.find(".visitorCon").hide();
			$span.removeClass("efx-expand");
		}else{
			$parent.find(".visitorTit-off").hide();
			$parent.find(".visitorTit-on").show();
			$parent.find(".visitorCon").show();
			$span.addClass("efx-expand");
		}
	}).on("click",".visitorCon-tit .visitorTit-label",function(){//基本信息展开收起
		var $span = $(this).find("span").eq(0);
		var $parent = $(this).parent();
		if($span.hasClass("efx-expand")){//如果是展开状态
			$parent.next("[flag='messageDiv']").hide();
			$span.removeClass("efx-expand");
		}else{
			$parent.next("[flag='messageDiv']").show();
			$span.addClass("efx-expand");
		}
	}).on("focus",".travelerTable [name='otherCost']",function(){//其他费用为0，获得焦点清空数据，失去焦点为0
		var thisValue = $(this).val();
		if("0" == thisValue){
			$(this).val("");
		}
	}).on("change",".travelerTable [data='currencyType']",function(){//其他费用更换币种
		calculateSingleTraveler_visa($(this).parents("form"));
	}).on("click",".btn-save",function(){
		var $this = $(this);
		var btnTxt = $this.text();
		var $form = $this.parents("form");
		if("保 存" == btnTxt){
			//添加表单验证
			var isvalidate = $form.validate({ignore:""}).form();
			if(isvalidate){
				$form.disableContainer( {
					blankText : "—",
					selectIsVisible:0
					//formatNumber : formatNumberArray
				});
				$form.find(".textBox-delete").hide();
				$form.find(".deleteCosts").hide();
				$form.find(".addCosts").hide();
				$form.find(".tourist-info1").addClass("tourist-info1Txt");
				//订单修改中的办签资料
				if(0 != $(".tourist-ckb").length){
					$(".tourist-ckb").each(function(index, element) {
                        var $ckb = $(element).find("input[type='checkbox']");
						$ckb.each(function(i, obj) {
                           if(!$(obj).is(":checked")){//如果没有选中，隐藏后面的文字
								$(obj).next().hide();
							}
							$(obj).hide();
                        });
                    });
				}
				$this.text("修 改");
			}else{
				top.$.jBox.tip('“游客' + $form.find(".visitorTit .visitorTit-label i").text() +'”中有未填写的必填项！', 'error');
			}
		}else{
			$form.undisableContainer( {
				blankText : "—"//,
				//formatNumber : formatNumberArray
			});
			$form.find(".textBox-delete").show();
			$form.find(".deleteCosts").show();
			$form.find(".addCosts").show();
			$form.find(".tourist-info1").removeClass("tourist-info1Txt");
			//订单修改中的办签资料
			if(0 != $(".tourist-ckb").length){
				$(".tourist-ckb").each(function(index, element) {
					var $ckb = $(element).find("input[type='checkbox']");
					$ckb.each(function(i, obj) {
					   if(!$(obj).is(":checked")){//如果没有选中，隐藏后面的文字
							$(obj).next().show();
						}
						$(obj).show();
					});
				});
			}
			$this.text("保 存");
		}
	}).on("click",".textBox-delete",function(){//删除游客
		var $form = $(this).parents("form.travelerTable");
		$.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				//是否包含撤签
				if($form.find("[data-hadOrderd='1']").length){
					var html = '<div style="margin-top:20px; font-size:20px; text-align:center;">';
					html += '是否提交撤签申请';
					html += '</div>';
					$.jBox(html, { title: "签证已送签",persistent:true,buttons:{'确定': 1,'取消': 0}, submit:function(v, h, f){
						if (v=="1"){
							return true;
						}else{
							
						}
					},height:150,width:380});
				}
				
				$form.remove();
				$("#traveler form").each(function(index,element){
					$(element).find(".visitorTit-label i").text(index+1);
					//其他费用名的id重置
					$(element).find("[name='costName']").each(function(i,obj){
						var ID = $(obj).attr("id").replace(/\d+\-\d+$/,(index+1)+"-"+(i+1));
						$(obj).attr("id",ID);
						$(obj).next("label.error").attr("for",ID);
					});
				});
				//暂无游客信息的显示与隐藏
				noTraveler();
				//添加游客的显示与隐藏
				isAddAllTraveler_visa();
				//计算总价
				calculateTotalMoney_visa();
				//提示信息
				top.$.jBox.tip('删除成功', 'success');
				
			}else if(v == 'cancel'){}
		});
	}).on("click",".btn-appAll",function(){//报价应用全部
		var $form = $(this).parents("form");
		//被复制项标志
		$form.attr("isCopied",1);
		var $otherCost = $form.find("[name='otherCost']");	
		$.jBox.confirm("是否将“其他费用”中所有款项应用于全部已添加的游客？<span style='color:#ff0000;'>—“费用名称”为空的不应用</span>", "提示", function(v, h, f) {
			if (v == 'ok') {
				//遍历游客的form表单
				$("#traveler form").each(function(index,element){
					if("1" != $(element).attr("isCopied")){
						var isModified = 0;
						//循环其他费用的源						
						$otherCost.each(function(i, ele) {
							var $parent = $(ele).parents(".otherExpenses");
							var $otherCost_to = $(element).find("[name='otherCost']");//被应用的其他费用
							var compareNum = 0;
							$otherCost_to.each(function(j,other){
								var $parent_other =  $(other).parents(".otherExpenses");
								if(($parent.find("[name='costName']").val() == $parent_other.find("[name='costName']").val()) && ($parent.find("[data='currencyType']").val() == $parent_other.find("[data='currencyType']").val()) && ($parent.find("[name='otherCost']").val() == $parent_other.find("[name='otherCost']").val())){//如果找到一条“费用名称”、币种、价格都相等的费用项则退出循环
									return;
								}else{
									compareNum++;
								}
							});
							if(($otherCost_to.length == compareNum) && ("" != $parent.find("[name='costName']").val())){//如果没有相同费用项，则进行复制(“费用名称”为空的不复制)
								$(element).find(".addCosts").before('<div class="otherExpenses">' + $parent.formhtml() + '</div>');
								isModified++;
							}
						});
						
						if(isModified){
							var $btnSave = $(element).find(".btn-save");
							var isTxtState = ($btnSave.text() == "保 存") ? 0 : 1;
							//$(element).find("[data='quotation']").html($quotation.formhtml());
							//计算游客模板中的结算价
							calculateSingleTraveler_visa($(element),0);
							//如果是已保存状态，需要出发保存按钮使表单变为文本
							if(isTxtState){
								$(element).find(".btn-save").trigger("click").trigger("click");
							}
						}
					}
				});
				//计算订单总价
				calculateTotalMoney_visa();
				$form.removeAttr("isCopied");
			}else if(v == 'cancel'){}
		});
	});
	
	//自备签类型
	ydbz2zibeiqian();
	//自备签复选框勾选，firefox中表单元素缓存
	$("[type='checkbox'][data-hadorderd='1']").each(function(index,element){
		$(element).prop("checked",true);
	});
	
	//初始化预定签证对象
	//签证的应收价格
	var $initNum = $("[name='pepoleNum']").eq(0);
	var arr_visaPrice = $initNum.attr("price").split("-");
	preVisa.currencyType = arr_visaPrice[0];
	preVisa.price = isFloat(arr_visaPrice[1].replace(/[ ]/g,"").replace(/,/g,""));
	if(("" != $initNum.val()) && ("0" != $initNum.val())){
		preVisa.amount = Number($initNum.val());
	}
	
	//计算表单游客对象的结算价
	$("#traveler").find("form").each(function(index, element) {
        calculateSingleTraveler_visa($(element),0);
    });
	//计算游客模板中的结算价
	if(0 != $("#travelerTemplate").length){
		calculateSingleTraveler_visa($("#travelerTemplate"),0);
	}
	
	//计算订单总价
	calculateTotalMoney_visa();
	//暂无游客信息的显示与隐藏
	noTraveler();
	//添加游客的显示与隐藏
	isAddAllTraveler_visa();
	
	//办签人数
	$("[name='pepoleNum']").focus(function(){
		var thisValue = $(this).val();
		if("0" == thisValue){
			$(this).val("");
		}
	}).blur(function(){
		var $this = $(this);
		var thisValue = $this.val();
		thisValue = thisValue ? thisValue : 0;
		var hastravelerNum = $("#traveler form").length;
		if(thisValue < hastravelerNum){
			$this.val(hastravelerNum);
			//重获焦点 firefox中需使用这种方式
			setTimeout(function (){$this[0].focus();},0);
			top.$.jBox.tip('办签人数不得小于已填写的游客数（' + hastravelerNum + '人）！', 'error');	
		}else{
			//修改人数
			preVisa.amount = Number($(this).val());
			isAddAllTraveler_visa();
			//计算订单总价
			calculateTotalMoney_visa();
		}		
	});
	
	//添加游客
	$(".addVisitor").click(function(){
		var $this = $(this);
		var $container = $("#traveler");
		$container.append('<form class="travelerTable">' + $("#travelerTemplate").html() + '</form>');
		$container.find("form:last").find(".visitorTit-label i").text($container.find("form").length);
		//暂无游客信息的显示与隐藏
		noTraveler();
		//添加游客的显示与隐藏
		isAddAllTraveler_visa();
		if($this.parent().hasClass("centerNew")){
			var top = $container.find("form:last").offset().top;
			$(window).scrollTop(top-45);
		}
	});
}

/*签证-计算单个游客的结算价
@form:表单对象
tag：是否计算订单总价。0：不计算
*/
function calculateSingleTraveler_visa($form,tag){
	//币种类型
	var $currencyOption = $("[data='currencyType']").eq(0).find("option");
	//定义分币种对象
	var sumSingle = new CurrencyMoney($currencyOption);
	//应收价格
	sumSingle[preVisa.currencyType] += isFloat(preVisa.price);
	$form.find("[data='quotation'] .otherExpenses").each(function(index, element) {
        if(0 != $(element).find("[name='otherCost']").length){
			//其他费用
			var $ipt = $(element).find("[name='otherCost']");
			if(("" != $ipt.val()) && ("0" != $ipt.val())){
				var otherCostType = $(element).find("select option:selected").text();
				sumSingle[otherCostType] += isFloat($ipt.val());
			}
		}
    });
	//设置结算价
	var html = "";
	for(var p in sumSingle){
		if(0 != sumSingle[p]){
			if("" != html){
				html += "+";
			}
			html += '<i>' + p +'</i><strong>' + milliFormat(sumSingle[p],1) + '</strong>';
		}
	}
	$form.find("[data='sPrice']").html(html);
	if(0 != tag){
		calculateTotalMoney_visa();
	}
}
//计算订单总价-签证
function calculateTotalMoney_visa(){
	//币种类型
	var $currencyOption = $("[data='currencyType']").eq(0).find("option");
	//定义分币种对象
	var totalMoney = new CurrencyMoney($currencyOption);
	$("#traveler form.travelerTable").each(function(index, element) {
        var $sprice = $(element).find("[data='sPrice']").eq(0);
		//分币种计算
		$sprice.find("i").each(function(i, obj) {
            var currencyType = $(obj).text().replace(/[ ]/g,"");
			var money = $(obj).next("strong").text().replace(/[ ]/g,"").replace(/,/g,"");
			totalMoney[currencyType] += isFloat(money);
        });
    });
	//应收价格*办签人数
	totalMoney[preVisa.currencyType] += isFloat(preVisa.price * (preVisa.amount - $("#traveler form.travelerTable").length));
	//设置订单总价
	var html = '';//totalMoney
	for(var p in totalMoney){
		if(0 != totalMoney[p]){
			if("" != html){
				html += "+";
			}
			html += '<i>' + p +'</i><strong>' + milliFormat(totalMoney[p],1) + '</strong>';
		}
	}
	if("" == html){
		$("[data='totalMoney']").html(0);
	}else{
		$("[data='totalMoney']").html(html);
	}
}

//添加游客的显示与隐藏
function isAddAllTraveler_visa(){
	var total = preVisa.amount;
	//添加游客的显示与隐藏
	if($("#traveler form").length >= total){
		$(".addVisitor").eq(0).hide();
		$(".addVisitor").eq(1).parent().hide();
		return 1;
	}else{
		$(".addVisitor").eq(0).show();
		$(".addVisitor").eq(1).parent().show();
		return 0;
	}
}

//保存并支付-签证
function toSave_visa(){
	var totalNum = preVisa.amount;
	$forms = $("#traveler form");
	if(0 === Number(totalNum)){
		top.$.jBox.tip('出行人数必须大于零', 'error');
	}else if (totalNum < $forms.length){
		top.$.jBox.tip('办签人数小于游客总数，请删除多余游客！', 'error');
		return false;
	}else{
		//分表单提交保存
		$forms.each(function(index,element){
			var $btn = $(element).find(".btn-save");
			if($btn.text() == "保 存"){
				$(element).find(".btn-save").trigger("click");
			}
		});
		//联系人必填验证
		var contactPeople = $("#addForm").validate().form();
		//如果通过验证
		if(contactPeople){
			
		}else{
			top.$.jBox.tip('“预订人信息”中有未填写的必填项！', 'error');
		}
	}
}
/*==============订单-签证修改 end=============*/

/*==============订单-机票修改 begin=============*/
/*定义成人、儿童、特殊人群的各类舱型的单价、币种，税费
 @tax：税费
 @space*：某种舱位，其中*动态取得舱位的编码
 @currencyType：币种
 @price：单价
*/
//成人
var price_adult = {"tax":{'currencyType':"美元",'price':0}};
//儿童
var price_child = {"tax":{'currencyType':"美元",'price':0}};
//特殊人群
var price_special = {"tax":{'currencyType':"美元",'price':0}};

//各个舱位的人数
var numSpace = {"total":0};
//出行人数
var numTravel = {"adult":0,"child":0,"special":0,"total":0}

//var priceAndNum = [{'type':1,'currencyType':"人民币",'price':0,'amount':0},{'type':2,'currencyType':"人民币",'price':0,'amount':0},{'type':3,'currencyType':"人民币",'price':0,'amount':0}];
function preOrder_air(){
	$("#traveler").on("click",".visitorTit .visitorTit-label",function(){//游客信息展开收起
		var $span = $(this).find("span").eq(0);
		var $parent = $(this).parents(".textBox");
		if($span.hasClass("efx-expand")){//如果是展开状态
			$parent.find(".forVisitorName").text($parent.find("[name='travelerName']").val());
			$parent.find(".visitorTit-off").show();
			$parent.find(".visitorTit-on").hide();
			$parent.find(".visitorCon").hide();
			$span.removeClass("efx-expand");
		}else{
			$parent.find(".visitorTit-off").hide();
			$parent.find(".visitorTit-on").show();
			$parent.find(".visitorCon").show();
			$span.addClass("efx-expand");
		}
	}).on("click",".visitorCon-tit .visitorTit-label",function(){//基本信息展开收起
		var $span = $(this).find("span").eq(0);
		var $parent = $(this).parent();
		if($span.hasClass("efx-expand")){//如果是展开状态
			$parent.next("[flag='messageDiv']").hide();
			$span.removeClass("efx-expand");
		}else{
			$parent.next("[flag='messageDiv']").show();
			$span.addClass("efx-expand");
		}
	}).on("change",".travelerTable [name='airLink']",function(){//联运价格变化
		var thisValue = $(this).val();
		if("-1" == thisValue){
			$(this).next("span").hide();
		}else{
			var $showSpan = $(this).next("span");
			var array = thisValue.split("-");
			$showSpan.find("i").text(array[0]);
			$showSpan.find("em").text(milliFormat(array[1].replace(/[ ]/g,"").replace(/,/g,"")),1);
			$showSpan.show();
		}
		calculateSingleTraveler_air($(this).parents("form"));
	 }).on("change",".travelerTable [name='personTypeinner']",function(){//更改游客类型
		calculateSingleTraveler_air($(this).parents("form"));
	 }).on("change",".travelerTable [name='spaceGrade']",function(){//更改舱位等级
		calculateSingleTraveler_air($(this).parents("form"));
	 }).on("change",".travelerTable [data='currencyType']",function(){//其他费用更换币种
		calculateSingleTraveler_air($(this).parents("form"));
	 }).on("click",".textBox-delete",function(){//删除游客
		var $form = $(this).parents("form.travelerTable");
		$.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				$form.remove();
				$("#traveler form").each(function(index,element){
					$(element).find(".visitorTit-label i").text(index+1);
				});
				//暂无游客信息的显示与隐藏
				noTraveler();
				//添加游客的显示与隐藏
				isAddAllTraveler_air();
				//计算总价
				calculateTotalMoney_air();
				//提示信息
				top.$.jBox.tip('删除成功', 'success');
			}else if(v == 'cancel'){}
		});
	 }).on("click",".btn-save",function(){
		var $this = $(this);
		var btnTxt = $this.text();
		var $form = $this.parents("form");
		if("保 存" == btnTxt){
			//添加表单验证
			var isvalidate = $form.validate({ignore:""}).form();
			if(isvalidate){
				$form.disableContainer( {
					blankText : "—",
					selectIsVisible:0
					//formatNumber : formatNumberArray
				});
				$form.find(".textBox-delete").hide();
				$form.find(".deleteCosts").hide();
				$form.find(".addCosts").hide();
				$form.find(".tourist-info1").addClass("tourist-info1Txt");
				$this.text("修 改");
			}else{
				top.$.jBox.tip('“游客' + $form.find(".visitorTit .visitorTit-label i").text() +'”中有未填写的必填项！', 'error');
			}
		}else{
			$form.undisableContainer( {
				blankText : "—"//,
				//formatNumber : formatNumberArray
			});
			$form.find(".textBox-delete").show();
			$form.find(".deleteCosts").show();
			$form.find(".addCosts").show();
			$form.find(".tourist-info1").removeClass("tourist-info1Txt");
			$this.text("保 存");
		}
	});
		
	//初始化舱位数量
	$("#numSpaceList").find("li[id]").each(function(index,element){
		var spaceName = $(element).attr("id").replace(/^num/,"");
		var spaceAmount = Number($(element).find("strong").text());
		spaceAmount = spaceAmount ? spaceAmount : 0;
		numSpace[spaceName] = spaceAmount;
		numSpace.total += spaceAmount;
	});
	
	//出行人数初始化
	$("[id^='orderPersonNum']").each(function(index, element) {
		setTravelerAmount_air($(element).attr("id"),$(element).val());
    });
	
	//初始化不同游客类型、不同舱位的价格
	$("li[id^='pricespace']").each(function(index,element){
		var priceName = $(element).attr("id").replace(/^price/,"");
		$(element).find("[data-price]").each(function(i,obj){
			var priceDetail = {};
			var theArr =  $(obj).attr("data-price").split("-");
			priceDetail.currencyType = theArr[0].replace(/ /g,"");
			priceDetail.price = isFloat(theArr[1].replace(/ /g,"").replace(/,/g,""));
			if(0 == i){
				price_adult[priceName] = priceDetail;
			}else if(1 == i){
				price_child[priceName] = priceDetail;
			}else if(2 == i){
				price_special[priceName] = priceDetail;
			}
		});
	});
	
	//初始化税费
	$("#tax").find("[data-price]").each(function(i,obj){
		var priceDetail = {};
		var theArr =  $(obj).attr("data-price").split("-");
		priceDetail.currencyType = theArr[0].replace(/ /g,"");
		priceDetail.price = isFloat(theArr[1].replace(/ /g,"").replace(/,/g,""));
		if(0 == i){
			price_adult.tax = priceDetail;
		}else if(1 == i){
			price_child.tax = priceDetail;
		}else if(2 == i){
			price_special.tax = priceDetail;
		}
	});
	
	//修改出行人数
	$("[id^='orderPersonNum']").blur(function(){
		//如果总出行人数超出总舱位数，则设置可行的最大值
		var $this = $(this);
		var total_new = 0;
		var hastravelerNum = $("#traveler").find("form").length;//已填写游客的个数
		$("[id^='orderPersonNum']").each(function(index,element){
			total_new += Number($(element).val().replace(/ /g,""));
		});
		if(total_new > numSpace.total){
			var maxNum = Number($this.val().replace(/ /g,"")) - total_new + numSpace.total;
			$this.val(maxNum);
			//重获焦点 firefox中需使用这种方式
			setTimeout(function (){$this[0].focus();},0);
			top.$.jBox.tip('设置的人数不能大于' + maxNum, 'error');		
		}else if(total_new < hastravelerNum){
			var minNum = Number($this.val().replace(/ /g,"")) - total_new + hastravelerNum;
			$this.val(minNum);
			//重获焦点 firefox中需使用这种方式
			setTimeout(function (){$this[0].focus();},0);
			top.$.jBox.tip('出行总人数不得小于已填写的游客数（' + minNum + '人）！', 'error');	
		}else{
			setTravelerAmount_air($(this).attr("id"),$(this).val());
			//“添加游客”按钮的显示与隐藏
			isAddAllTraveler_air();
			//计算订单总价
			calculateTotalMoney_air();
		}
	});
	
	//计算表单游客对象的结算价
	$("#traveler").find("form").each(function(index, element) {
		//重置表单元素
		element.reset();
        calculateSingleTraveler_air($(element),0);
    });
	//计算游客模板中的结算价
	calculateSingleTraveler_air($("#travelerTemplate"),0);
	// //计算订单总价
	 calculateTotalMoney_air();
	//暂无游客信息的显示与隐藏
	noTraveler();
	//添加游客的显示与隐藏
	isAddAllTraveler_air();
	
	//添加游客
	$(".addVisitor").click(function(){
		var $this = $(this);
		var $container = $("#traveler");
		$container.append('<form class="travelerTable">' + $("#travelerTemplate").html() + '</form>');
		$container.find("form:last").find(".visitorTit-label i").text($container.find("form").length);
		//暂无游客信息的显示与隐藏
		noTraveler();
		//添加游客的显示与隐藏
		isAddAllTraveler_air();
		if($this.parent().hasClass("centerNew")){
			var top = $container.find("form:last").offset().top;
			$(window).scrollTop(top-45);
		}
		calculateTotalMoney_air();
	});
}

//设置出行人数
function setTravelerAmount_air(str,num){
	if("-1" != str.indexOf("Adult")){//成人
		numTravel.adult = Number(num) ? Number(num) : 0;
	}else if("-1" != str.indexOf("Child")){//儿童
		numTravel.child = Number(num) ? Number(num) : 0;
	}else if("-1" != str.indexOf("Special")){//特殊人群
		numTravel.special = Number(num) ? Number(num) : 0;
	}
	//出行总人数
	numTravel.total = numTravel.adult + numTravel.child + numTravel.special;
}

//计算单个游客的结算价
/*@$form:单个表单游客对象
  @tag:0--不计算订单总价
*/
function calculateSingleTraveler_air($form,tag){
	//币种类型
	var $currencyOption = $("[data='currencyType']").eq(0).find("option");
	//定义分币种对象
	var sumSingle = new CurrencyMoney($currencyOption);
	//游客类型
	var indexPersonType = $form.find("[name='personTypeinner']:checked").val();
	//舱位等级
	var spaceGrade = "space" + $form.find("[name='spaceGrade']").val();
	var priceObj;
	if("1" == indexPersonType){
		priceObj = price_adult[spaceGrade];
		//添加税费
		sumSingle[price_adult.tax.currencyType] += isFloat(price_adult.price);
	}else if("2" == indexPersonType){
		priceObj = price_child[spaceGrade];
		//添加税费
		sumSingle[price_child.tax.currencyType] += isFloat(price_child.price);
	}else if("3" == indexPersonType){
		priceObj = price_special[spaceGrade];
		//添加税费
		sumSingle[price_special.tax.currencyType] += isFloat(price_special.price);
	}
	//机票价格
	sumSingle[priceObj.currencyType] += isFloat(priceObj.price);
	
	//联运价格
	if(0 != $form.find("[name='airLink']:visible").length){
		var arr_linkMoney = $form.find("[name='airLink']").val().split("-");
		sumSingle[arr_linkMoney[0]] += isFloat(arr_linkMoney[1].replace(/[ ]/g,"").replace(/,/g,""));
	}
	//其他费用
	$form.find("[data='quotation'] .otherExpenses").each(function(index, element) {
        if(0 != $(element).find("[name='otherCost']").length){
			//其他费用
			var $ipt = $(element).find("[name='otherCost']");
			if(("" != $ipt.val()) && ("0" != $ipt.val())){
				var otherCostType = $(element).find("select option:selected").text();
				sumSingle[otherCostType] += isFloat($ipt.val());
			}
		}
    });
	
	//设置结算价
	var html = "";
	for(var p in sumSingle){
		if(0 != sumSingle[p]){
			if("" != html){
				html += "+";
			}
			html += '<i>' + p +'</i><strong>' + milliFormat(sumSingle[p],1) + '</strong>';
		}
	}
	$form.find("[data='sPrice']").html(html);
	if(0 != tag){
		calculateTotalMoney_air();
	}
}

//计算订单总价，按照添加的游客来计算
function calculateTotalMoney_air(){
	//币种类型
	var $currencyOption = $("[data='currencyType']").eq(0).find("option");
	//定义分币种对象
	var totalMoney = new CurrencyMoney($currencyOption);
	$("#traveler form.travelerTable").each(function(index, element) {
        var $sprice = $(element).find("[data='sPrice']").eq(0);
		//分币种计算
		$sprice.find("i").each(function(i, obj) {
            var currencyType = $(obj).text().replace(/[ ]/g,"");
			var money = $(obj).next("strong").text().replace(/[ ]/g,"").replace(/,/g,"");
			totalMoney[currencyType] += isFloat(money);
        });
    });
	
	//设置订单总价
	var html = '';
	for(var p in totalMoney){
		if(0 != totalMoney[p]){
			if("" != html){
				html += "+";
			}
			html += '<i>' + p +'</i><strong>' + milliFormat(totalMoney[p],1) + '</strong>';
		}
	}
	if("" == html){
		$("[data='totalMoney']").html(0);
	}else{
		$("[data='totalMoney']").html(html);
	}
}

//"添加游客“按钮的显示与隐藏
function isAddAllTraveler_air(){
	//添加游客的显示与隐藏
	if($("#traveler form").length >= numTravel.total){
		$(".addVisitor").eq(0).hide();
		$(".addVisitor").eq(1).parent().hide();
		return 1;
	}else{
		$(".addVisitor").eq(0).show();
		$(".addVisitor").eq(1).parent().show();
		return 0;
	}
}

//提交
function toSave_air(){
	var tips_spaceGrade = "";
	$("[name='spaceGrade']").eq(0).find("option").each(function(index,element){
		var thisValue = $(element).val();
		var allThisSpaceNum = 0;
		$("#traveler select[name='spaceGrade']").each(function(i,obj){
			if(thisValue == $(obj).val()){
				allThisSpaceNum++;
			}
		});
		if(allThisSpaceNum > numSpace["space" + thisValue]){
			if("" == tips_spaceGrade){
				tips_spaceGrade += '”';
			}else{
				tips_spaceGrade += "、";
			}
			tips_spaceGrade += $(element).text().trim();
		}
	});
	
	if(0 === numTravel.total){
		top.$.jBox.tip('出行人数必须大于零', 'error');
	}else if(numTravel.adult < countAdult() || numTravel.child < countChild() || numTravel.special < countSpecial()){
		top.$.jBox.tip('成人、儿童或特殊人数与初始值不匹配请修改', 'error');
		return false;
	}else{
		if(("" != tips_spaceGrade)){
			top.$.jBox.tip(tips_spaceGrade + '“超出舱位设置的初始值，请修改！', 'error');
		}else{
			$forms = $("#traveler form");
			$forms.each(function(index,element){
				var $btn = $(element).find(".btn-save");
				if($btn.text() == "保 存"){
					$(element).find(".btn-save").trigger("click");
				}
			});
			//联系人必填验证
			var contactPeople = $("#addForm").validate().form();
			//如果通过验证
			if(contactPeople){
				
			}else{
				top.$.jBox.tip('“预订人信息”中有未填写的必填项！', 'error');
			}
		}
	}
}
/*==============订单-机票修改 end=============*/

/*==============订单-酒店修改 begin=============*/
//酒店总计={预定总人数：初始化为0，预定房间总价格：{人民币：100，美元：100……}}
var total_hotel = {totalNum:0,totalMoney:{}};
//房型对象={房型1（与房型的value值对应）：{房型名称：水房，该房型是否不可选：0否，1是，该房型包括的日期：{2014-09-20：{该房型在该日期下的价格：【币种，数值】，该日期下该房型的容量：【标准容量，可加床数】，已占用的容量：【已占用的标准容量，已占用的加床数】，该日期下该房型的预定数量：数值，该日期是否可用：0可用，1不可用}，……}}，……}
//rooms = {"room1":{"name":"水房","isdisabled":0,dates:{"2014-09-20":{"price":["人民币",200],"capacity":[6,3],"capacitied":[2,1],"amount":3,"isdisabled":0},……}},……}
var rooms = {};
function preOrderHotel(){
	//初始化酒店的总价格
	var $currencyTypeOption = $("[data='currencyType']").eq(0).find("option");
	total_hotel.totalMoney = new CurrencyMoney($currencyTypeOption);
	//各房型总价的html
	var html_hotelType = '';
	//初始化房型
	$("[data='room']").each(function(index,element){
		//该房型的总价
		var thisRoomPrice = new CurrencyMoney($currencyTypeOption);
		//该房型对象
		var thisRoom = {};
		//房型名称
		thisRoom.name = $(element).children("p:first").text().replace(/：$/,'');
		//该房型是否不可选
		thisRoom.isdisabled = 0;
		//该房型的日期
		thisRoom.dates = {};
		$(element).find("ul li").each(function(i,obj){
			var thisDate = {};
			//日期
			var thisDateName = $(obj).children("p:first").find("span").text().replace(/ /g,"");
			//包含日期数据的dom
			var $domData = $(obj).find("strong").eq(0);
			//该房型该日期下的价格
			thisDate.price = $domData.attr("data-price").split("-");
			thisDate.price[1] = parseFloat(thisDate.price[1]);
			//该房型该日期下的预定数量
			thisDate.amount = parseInt($domData.text());
			//该房型该日期下的总容量
			thisDate.capacity = [];
			var thisCapacity = $domData.attr("data-capacity").split("-");
			thisDate.capacity[0] = parseInt(thisCapacity[0]);
			thisDate.capacity[1] = parseInt(thisCapacity[1]);
			//该房型该日期下已预定的容量
			thisDate.capacitied = [0,0];
			//该日期是否可见
			thisDate.isdisabled = 0;
			//添加日期对象
			thisRoom.dates[thisDateName] = thisDate;
			
			//酒店总价累计
			total_hotel.totalMoney[thisDate.price[0]] += thisDate.amount * parseFloat(thisDate.price[1]);
			//酒店总人数累计
			total_hotel.totalNum += thisDate.capacity[0] + thisDate.capacity[1];
			
			//房型总价
			thisRoomPrice[thisDate.price[0]] += thisDate.amount * parseFloat(thisDate.price[1]);
		});
		//添加房型数据
		rooms["room" + (index+1)] = thisRoom;
		//拼接房型总价
		var html1 = '';
		for(var p in thisRoomPrice){
			if(0.00 != thisRoomPrice[p]){
				if(html1 != ''){
					html1 += '+';
				}
				html1 += p + '<font color="#FF0000">' + milliFormat(thisRoomPrice[p],1) +'</font>';
			}
		}
		html_hotelType += '<li>' + thisRoom.name + '：' + html1 + '</li>';
	});
	//设置房型总价
	$("[data='hotelTypePrice']").html(html_hotelType);
	//对于订单修改
	$("#traveler form").each(function(index,element){
		//重置form表单
		element.reset();
		//初始化单个游客的结算价
		calculateSingleTraveler_hotel($(element),0);
	});
	
	//暂无游客信息的显示与隐藏
	noTraveler();
	//添加游客的显示与隐藏
	isAddAllTraveler_hotel();
	//初始化订单总价
	calculateTotalMoney_hotel();
	
	//设置占用的房间数
	initCapacitied($("#traveler form"));
	//游客模板中“选择房型”的设置
	var html_roomType = '';
	for(var p in rooms){
		var str_disabled = '';
		if(rooms[p].isdisabled){
			str_disabled = 'disabled:disabled';
		}
		html_roomType += '<label><input name="roomtype" type="checkbox" value="' + p + '" ' + str_disabled + ' />' + rooms[p].name + '</label>';
	}
	$("#travelerTemplate [data='quotation'] dl.theHotelRoom dt").append(html_roomType);
	//绑定事件
	$("#traveler").on("click",".visitorTit .visitorTit-label",function(){//游客信息展开收起
		var $span = $(this).find("span").eq(0);
		var $parent = $(this).parents(".textBox");
		if($span.hasClass("efx-expand")){//如果是展开状态
			$parent.find(".forVisitorName").text($parent.find("[name='travelerName']").val());
			$parent.find(".visitorTit-off").show();
			$parent.find(".visitorTit-on").hide();
			$parent.find(".visitorCon").hide();
			$span.removeClass("efx-expand");
		}else{
			$parent.find(".visitorTit-off").hide();
			$parent.find(".visitorTit-on").show();
			$parent.find(".visitorCon").show();
			$span.addClass("efx-expand");
		}
	}).on("click",".visitorCon-tit .visitorTit-label",function(){//基本信息展开收起
		var $span = $(this).find("span").eq(0);
		var $parent = $(this).parent();
		if($span.hasClass("efx-expand")){//如果是展开状态
			$parent.next("[flag='messageDiv']").hide();
			$span.removeClass("efx-expand");
		}else{
			$parent.next("[flag='messageDiv']").show();
			$span.addClass("efx-expand");
		}
	}).on("focus",".travelerTable [name='otherCost']",function(){//其他费用为0，获得焦点清空数据，失去焦点为0
		var thisValue = $(this).val();
		if("0" == thisValue){
			$(this).val("");
		}
	}).on("change",".travelerTable [name*='housingDemand']",function(){//住房要求
		//是否加床
		var $addBed = $(this).parent().next('label');
		//加床价格
		var $addBedPrice = $(this).parent().next().next('label');
		//是否计算订单总价。0：不计算，1：计算
		var flag = 0;
		//房型对象名
		var roomName = $(this).attr("name").split("-")[0];
		//房型中的日期
		var strDate = $(this).attr("name").replace(/.*?(\d{8}$)/g,'$1');
		strDate = strDate.replace(/(\d{4})?(\d{2})?(\d{2})/g,'$1-$2-$3');
		var objDate = rooms[roomName].dates[strDate];
		
		if($(this).val()==1){//合住
			//已占用空间计算
			objDate.capacitied[0] -= objDate.capacity[0];
			objDate.capacitied[1] -= objDate.capacity[1];
			//如果有加床
			if(0 != $addBed.length){
				$addBed.show();
				$addBedPrice.show();
				if(0 != $addBed.find("input[type='checkbox']:checked").length){
					flag = 1;
					objDate.capacitied[1] += 1;
				}else{
					objDate.capacitied[0] += 1;
				}
			}else{
				objDate.capacitied[0] += 1;
			}
		}else{//单住
			//已占用空间计算
			objDate.capacitied[0] += objDate.capacity[0];
			objDate.capacitied[1] += objDate.capacity[1];
			//如果有加床
			if(0 != $addBed.length){
				$addBed.hide();
				$addBedPrice.hide();
				if(0 != $addBed.find("input[type='checkbox']:checked").length){
					flag = 1;
					objDate.capacitied[1] -= 1;
				}else{
					objDate.capacitied[0] -= 1;
				}
			}else{
				objDate.capacitied[0] -= 1;
			}
		}
		//计算单个游客的结算价
		calculateSingleTraveler_hotel($(this).parents("form"),flag);
		//设置房型、日期是否可选
		setDateIsDisabled(roomName,strDate,1);
		//添加游客的显示与隐藏
		isAddAllTraveler_hotel();
	}).on("click",".travelerTable [name*='addbed']",function(){//是否加床
		//房型对象名
		var roomName = $(this).attr("name").split("-")[0];
		//房型中的日期
		var strDate = $(this).attr("name").replace(/.*?(\d{8}$)/g,'$1');
		strDate = strDate.replace(/(\d{4})?(\d{2})?(\d{2})/g,'$1-$2-$3');
		var objDate = rooms[roomName].dates[strDate];
		if(this.checked){
			objDate.capacitied[1] += 1;
			objDate.capacitied[0] -= 1;
		}else{
			objDate.capacitied[1] -= 1;
			objDate.capacitied[0] += 1;
		}
		calculateSingleTraveler_hotel($(this).parents("form"));
		//设置房型、日期是否可选
		setDateIsDisabled(roomName,strDate,1);
		//添加游客的显示与隐藏
		isAddAllTraveler_hotel();
	}).on("click",".travelerTable [name*='Lmeal']",function(){//是否升餐
		var value = $(this).val();
		if("1" == value){
			$(this).parents(".otherExpenses-list").find('.orMeal-term').show();
		}else{
			$(this).parents(".otherExpenses-list").find('.orMeal-term').hide();	
		}
		calculateSingleTraveler_hotel($(this).parents("form"));
	}).on("change",".travelerTable [data='currencyType']",function(){//其他费用更换币种
		calculateSingleTraveler_hotel($(this).parents("form"));
	}).on("click",".textBox > .textBox-delete",function(){//删除游客
		var $form = $(this).parents("form.travelerTable");
		$.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				$form.remove();
				$("#traveler form").each(function(index,element){
					$(element).find(".visitorTit-label i").text(index+1);
					//其他费用名的id重置
					$(element).find("[name='costName']").each(function(i,obj){
						var ID = $(obj).attr("id").replace(/\d+\-\d+$/,(index+1)+"-"+(i+1));
						$(obj).attr("id",ID);
						$(obj).next("label.error").attr("for",ID);
					});
				});
				//暂无游客信息的显示与隐藏
				noTraveler();
				//计算总价
				calculateTotalMoney_hotel();
				//重新计算房间占用容量
				initCapacitied($("#traveler form"));
				//提示信息
				top.$.jBox.tip('删除成功', 'success');
			}else if(v == 'cancel'){}
		});
	}).on("click","input[type='checkbox'][name$='dates']",function(){
		//日期
		var xdtime = $(this).parent().text();
		//只有数字的日期串
		var xdtime_num = xdtime.replace(/-/g,'');
		//房型对象名称
		var roomName = $(this).attr("name").split("-")[0];
		if($(this).is(':checked')){//追加的html中，部分数据需要通过ajax取得
			//获取点击的值 追加给 hotelOrder-list
			$(this).parents(".hotelOrder-top").siblings().find('.hotelOrder-list').append('<li><span>'+xdtime+'</span><i onclick="xdhotel_remove(this)">X</i></li>');
			//追加给
			var html='<div class="otherExpenses no">';
			html+='<div class="otherExpenses-list">';
			html+='<label>住宿要求：<select class="" name="' + roomName + '-housingDemand' + xdtime_num + '">';
			html+='<option value="1" selected="selected">合住</option>';
			html+='<option value="0">单住</option>';
			html+='</select></label>&nbsp;';
			html+='<label><input type="checkbox" value="1" price="人民币-100" name="' + roomName + '-addbed' + xdtime_num + '" />加床（儿童）</label>&nbsp;';
			html+='<label>加床价格：￥100元</label>';
			html+='</div>';
			//是否存在升餐，需要通过ajax请求数据进行判断
			html+='<div class="efx-bottom-dotted"></div>';
			html+='<div class="otherExpenses-list">';
			html+='<div class="orMeal">';
			html+='<span>是否升餐：</span>&nbsp;';
			html+='<label><input name="' + roomName + '-Lmeal' + xdtime_num + '" type="radio" value="0" checked="checked"/>不需要</label>&nbsp;';
			html+='<label><input name="' + roomName + '-Lmeal' + xdtime_num + '" type="radio" value="1" />需要</label>';
			html+='</div>';
			html+='<div class="orMeal-term" style="display:none;">&nbsp;';
			html+='<label>餐型选择：<select class="mealSelection" onchange="mealSelection(this,\'hotel\')" name="' + roomName + '-meal' + xdtime_num + '" autocomplete="off">';
			html+='<option value="1" price="人民币-30.01" selected="selected">BB 早餐</option>';
			html+='<option value="2" price="美元-5.01">HB 早晚餐</option>';
			html+='<option value="3" price="欧元-3.01">FB 早中晚餐</option>';
			html+='<option value="4" price="人民币-100.01">A2 三餐+部分酒水</option>';
			html+='</select></label>&nbsp;';
			html+='<label>币种：<span>人民币</span></label>&nbsp;';
			html+='<label>升餐价格：<span>30.01</span></label>';
			html+='</div>';
			html+='</div>';
			html+='</div>';
			var $container = $(this).parents(".hotelOrder-top").siblings(".hotelOrder-center");
			$container.append(html);
			//如果只有一个日期，则设置当前状态
			if(1 == $container.find(".otherExpenses").length){
				$container.find(".otherExpenses").eq(0).show();
				$container.find(".hotelOrder-list li:first").addClass("hotel-lion");
			}
			
			//设置住房占用量
			var theDate = rooms[roomName].dates[xdtime];
			if(theDate.capacitied[0] == theDate.capacity[0]* theDate.amount){
				$container.find(".otherExpenses:last").find("[name*='addbed']").prop("checked",true);
				theDate.capacitied[1] += 1;
			}else{
				theDate.capacitied[0] += 1;
			}
			setDateIsDisabled(roomName,xdtime,1);
		}else{
			$(this).parents(".hotelOrder-top").siblings().find('.hotelOrder-list li').each(function(i,e){
				if($(e).find('span').text()==xdtime){
					$(e).find('i').click();
				}
			});
		}
		calculateSingleTraveler_hotel($(this).parents("form"));
		//添加游客的显示与隐藏
		isAddAllTraveler_hotel();
	}).on("mouseover","ul.hotelOrder-list li",function(){
		var $parentUl = $(this).parent("ul");
		//当前状态设置
		$(this).addClass('hotel-lion').siblings().removeClass('hotel-lion');
		//设置相应内容的显示
		var index = $parentUl.find("li").index($(this));
		$parentUl.siblings('.otherExpenses').addClass('no').eq(index).removeClass('no');
	}).on("click",".btn-save",function(){
		var $this = $(this);
		var btnTxt = $this.text();
		var $form = $this.parents("form");
		if("保 存" == btnTxt){
			//添加表单验证
			var isvalidate = $form.validate({ignore:""}).form();
			if(isvalidate){
				$form.disableContainer( {
					blankText : "—",
					selectIsVisible:0
					//formatNumber : formatNumberArray
				});
				$form.find(".textBox-delete").hide();
				$form.find(".deleteCosts").hide();
				$form.find(".addCosts").hide();
				$form.find(".tourist-info1").addClass("tourist-info1Txt");
				//是否加床
				$form.find("[name*='addbed']").each(function(index,element){
					if($(element).is(":checked")){
						$(element).hide();
					}else{
						$(element).parent().hide();
						$(element).parent().next().hide();
					}
				});
				//选择房型、日期
				$form.find("[name='roomtype'],[name$='dates']").each(function(index,element){
					if($(element).is(":checked")){
						$(element).hide();
					}else{
						$(element).parent().hide();
					}
				});
				//日期后的删除按钮
				$form.find(".hotelOrder-list li i").each(function(index,element){
					$(element).css("display","none");
				});
				$this.text("修 改");
			}else{
				top.$.jBox.tip('“游客' + $form.find(".visitorTit .visitorTit-label i").text() +'”中有未填写的必填项！', 'error');
			}
		}else{
			$form.undisableContainer( {
				blankText : "—"//,
				//formatNumber : formatNumberArray
			});
			$form.find(".textBox-delete").show();
			$form.find(".deleteCosts").show();
			$form.find(".addCosts").show();
			$form.find(".tourist-info1").removeClass("tourist-info1Txt");
			//是否加床
			$form.find("[name*='addbed']").each(function(index,element){
				if($(element).is(":checked")){
					$(element).show();
				}else{
					$(element).parent().show();
					$(element).parent().next().show();
				}
			});
			//选择房型、日期
			$form.find("[name='roomtype'],[name$='dates']").each(function(index,element){
				if($(element).is(":checked")){
					$(element).show();
				}else{
					$(element).parent().show();
				}
			});
			//日期后的删除按钮
			$form.find(".hotelOrder-list li i").each(function(index,element){
				$(element).removeAttr("style");
			});
			$this.text("保 存");
		}
	}).on("click",".hotelOrder-top .textBox-delete",function(){//删除房型
		var $form = $(this).parents("form.travelerTable");
		var $dd = $(this).parents("dd");
		$.jBox.confirm("确定要删除该房型吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				//对应的房型复选框取消勾选
				var $roomType = $form.find("input[name='roomtype'][value='" + $dd.attr("data-for") + "']");
				if($roomType.is(":checked")){
					$roomType.prop("checked",false);
				}
				//删除房型
				$dd.remove();
				//重新计算结算价和订单总价
				calculateSingleTraveler_hotel($form);
				//重新计算已占用的房间容量
				initCapacitied($("#traveler form"));
				//提示信息
				top.$.jBox.tip('删除成功', 'success');
			}else if(v == 'cancel'){}
		});
	}).on("click","input[name='roomtype']",function(){//房型勾选
		var $roomType = $(this);
		var roomTypeName = $roomType.val();
		var $form = $roomType.parents("form");
		if($roomType.is(":checked")){//如果勾选
			//var $dl = $form.find("dl.theHotelRoom");
			var theDates = rooms[roomTypeName].dates;
			var html = '<dd data-for="' + roomTypeName + '"><div class="hotelOrder-top">';
			//删除房型
			html += '<div class="textBox-delete">删除</div>';
			//房型名称
			html += '<b>' + rooms[roomTypeName].name + '</b>'
			//遍历该房型下的可选日期
			html += '<div class="dates">';
			for(var d in theDates){
				var str_disabled = '';
				if(theDates[d].isdisabled){
					str_disabled = 'disabled="disabled"';
				}
				html += '<label><input type="checkbox" value="' + d + '" name="' + roomTypeName + '-dates" ' + str_disabled + ' />' + d + '</label>';
			}
			html += '</div>';
			html += '</div><div class="hotelOrder-center"><ul class="hotelOrder-list"></ul></div>';
			html += '</dd>';
			$form.find("dl.theHotelRoom dt").after(html);
		}else{//取消勾选
			var $dd = $form.find("dd[data-for='" + roomTypeName + "']");
			if($dd.length){
				$dd.find(".textBox-delete").trigger("click");
			}
		}
	});
	
	//添加游客
	$(".addVisitor").click(function(){
		var $this = $(this);
		var $container = $("#traveler");
		$container.append('<form class="travelerTable">' + $("#travelerTemplate").html() + '</form>');
		$container.find("form:last").find(".visitorTit-label i").text($container.find("form").length);
		//暂无游客信息的显示与隐藏
		noTraveler();
		if($this.parent().hasClass("centerNew")){
			var top = $container.find("form:last").offset().top;
			$(window).scrollTop(top-45);
		}
	});
}

//设置已占用的房间容量
function initCapacitied($forms){
	//重置各capacitied为0
	for(var r in rooms){
		for(var d in rooms[r].dates){
			rooms[r].dates[d].capacitied = [0,0];
		}
	}
	$forms.each(function(index,element){
		$(element).find("dd[data-for^='room']").each(function(i,obj){
			//房型
			var roomValue = $(obj).attr("data-for");
			//对应的日期
			var $dates = $(obj).find(".hotelOrder-list li");
			//对应的div
			var $datesDiv = $(obj).find(".otherExpenses");
			$dates.each(function(j,dt){
				var theDate = rooms[roomValue].dates[$(dt).find("span").text().replace(/ /g,'')];
				var $housingDemand = $datesDiv.eq(j).find("select[name*='housingDemand']");
				if("1" == $housingDemand.val()){//合住
					if($datesDiv.eq(j).find("input[name*='addbed']").attr("checked")){
						theDate.capacitied[1] += 1;
					}else{
						theDate.capacitied[0] += 1;
					}
				}else{//单住
					theDate.capacitied[0] += theDate.capacity[0];
					theDate.capacitied[1] += theDate.capacity[1];
				}
			});
		});
	});
	//设置房型、日期是否可选
	for(var p in rooms){
		for(var pd in rooms[p].dates){
			setDateIsDisabled(p,pd);
		}
		setRoomIsDisabled(p);
	}
	//添加游客的显示与隐藏
	isAddAllTraveler_hotel();
}

//设置房型是否可选 @r:房型
function setRoomIsDisabled(r){
	//房型对象
	var thisRoom = rooms[r];
	var flag = 1;
	for(var p in thisRoom.dates){
		//如果某日期可选，那么房型可选
		if(0 == thisRoom.dates[p].isdisabled){
			flag = 0;
			break;
		}
	}
	thisRoom.disabled = flag;
	//设置房型可选与否
	$("input[type='checkbox'][name='roomtype'][value='" + r + "']").each(function(index,element){
		if(!element.checked){
			if(flag){
				$(element).prop("disabled",true);
			}else{
				$(element).prop("disabled",false);
			}
		}
	});
}

//设置某房型下某日期是否可选 @r：房型，@d：日期，tag：是否要重新设置房型可选（1：是，0：否）
function setDateIsDisabled(r,d,tag){
	//房型对象
	var thisDate = rooms[r].dates[d];
	//最多容纳数
	var capacitied0 = thisDate.capacity[0]*thisDate.amount;
	//最多加床数
	var capacitied1 = thisDate.capacity[1]*thisDate.amount;
	if((capacitied0 == thisDate.capacitied[0]) && (capacitied1 == thisDate.capacitied[1])){
		rooms[r].dates[d].isdisabled = 1;
	}else{
		rooms[r].dates[d].isdisabled = 0;
	}
	//设置日期是否可选
	$("[data-for='"+ r + "']").each(function(index,element){
		var $ckb = $(element).find(".dates input[type='checkbox'][value='" + d + "']");
		if(!$ckb.prop("checked")){
			if(rooms[r].dates[d].isdisabled){
				$(element).prop("disabled",true);
			}else{
				$(element).prop("disabled",false);
			}
		}
	});
	//住宿要求单住、合住的disabled设置
	var housingDemandName = r+"-housingDemand"+d.replace(/-/g,'');
	//剩余的非加床容量
	var remainder = capacitied0 - thisDate.capacitied[0];
	//住宿要求select
	$("[name='" + housingDemandName + "']").each(function(index,element){
		//未选中的”住宿要求“选项是否可选设置
		var tagOption = 0;
		//加床复选框
		var $thisBed = $(element).parent().next("label").find("[name*='addbed']");
		if("1" == $(element).val()){//当前是合住状态
			if(0 != $thisBed.length){//有加床选项
				if($thisBed.prop("checked") && (remainder < thisDate.capacity[0])){//选择加床
					tagOption = 1;
				}
				if(!$thisBed.prop("checked") && (remainder < (thisDate.capacity[0] - 1))){//未选择加床
					tagOption = 1;
				}
			}
			if(tagOption){
				$(element).find("option[value='0']").prop("disabled",true);
			}else{
				$(element).find("option[value='0']").prop("disabled",false);
			}
		}else{//当前是单住状态
			if(0 != $thisBed.length){//有加床选项
				if(capacitied1 == thisDate.capacitied[1]){
					if($thisBed.prop("checked")){
						$thisBed.prop("checked",false);
					}
					$thisBed.prop("disabled",true);
				}else{
					$thisBed.prop("disabled",false);
				}
			}
		}
	});
	
	//加床复选框的disabled设置
	//加床复选框
	var addBedName = r+"-addbed"+d.replace(/-/g,'');
	if(capacitied1 == thisDate.capacitied[1]){
		$("[name='" + addBedName + "']").each(function(index,element){
			if(!element.checked){
				$(element).prop("disabled",true);
			}
		});		
	}else{
		$("[name='" + addBedName + "']").each(function(index,element){
			$(element).prop("disabled",false);
		});	
	}
	//重新设置房型
	if(1 == tag){
		setRoomIsDisabled(r);
	}
}

//添加游客的显示与隐藏
function isAddAllTraveler_hotel(){
	//累计占用的房间容量
	var nowNum = 0;
	//遍历房间类型
	for(var r in rooms){
		//遍历某房型下的日期
		for(var d in rooms[r].dates){
			nowNum += (rooms[r].dates[d].capacitied[0] + rooms[r].dates[d].capacitied[1]);
		}
	}
	if(total_hotel.totalNum >  nowNum){
		$(".addVisitor").eq(0).show();
		$(".addVisitor").eq(1).parent().show();
		return 0;
	}else{
		$(".addVisitor").eq(0).hide();
		$(".addVisitor").eq(1).parent().hide();
		return 1;
	}
}

//计算单个游客的结算价
/*@$form:单个表单游客对象
  @tag:0--不计算订单总价
*/
function calculateSingleTraveler_hotel($form,tag){
	//币种类型
	var $currencyOption = $("[data='currencyType']").eq(0).find("option");
	//定义分币种对象
	var sumSingle = new CurrencyMoney($currencyOption);
	//住房、用餐费用
	$form.find("[data='quotation'] .otherExpenses").each(function(index, element) {
        if(0 == $(element).find("[name='otherCost']").length){
			//房型块dd
			var $dd = $(element).parents("dd[data-for^='room']");
			//取得该房型的日期
			var str_date = $dd.find(".hotelOrder-list li").eq($dd.find(".hotelOrder-center .otherExpenses").index(element)).find("span").text().replace(/ /g,'');
			var objDate = rooms[$dd.attr("data-for").replace(/ /g,'')].dates[str_date];
			//住房要求
			if("0" == $(element).find("[name*='housingDemand']").val()){//单住
				sumSingle[objDate.price[0]] += objDate.price[1];
			}else{//合住
				var $addbed = $(element).find("[name*='addbed']:checked");
				if(0 != $addbed.length){//加床
					var arr_bedMoney = $addbed.attr("price").split("-");
					sumSingle[arr_bedMoney[0]] += isFloat(arr_bedMoney[1].replace(/[ ]/g,"").replace(/,/g,""));
				}else{//不加床
					sumSingle[objDate.price[0]] += objDate.price[1]/objDate.capacity[0];
				}
			}
			//升餐
			if("1" == $(element).find("[name*='Lmeal']:checked").val()){
				var arr_mealMoney = $(element).find("[name*='meal'] option:selected").attr("price").split("-");
				sumSingle[arr_mealMoney[0]] += isFloat(arr_mealMoney[1].replace(/[ ]/g,"").replace(/,/g,""));
			}
		}else{
			//其他费用
			var $ipt = $(element).find("[name='otherCost']");
			if(("" != $ipt.val()) && ("0" != $ipt.val())){
				var otherCostType = $(element).find("select option:selected").text();
				sumSingle[otherCostType] += isFloat($ipt.val());
			}
		}
    });
	
	//设置结算价
	var html = "";
	for(var p in sumSingle){
		if(0 != sumSingle[p]){
			if("" != html){
				html += "+";
			}
			html += '<i>' + p +'</i><strong>' + milliFormat(sumSingle[p],1) + '</strong>';
		}
	}
	if("" != html){
		$form.find("[data='sPrice']").html(html);
	}else{
		$form.find("[data='sPrice']").html("0");
	}
	
	if(0 != tag){
		calculateTotalMoney_hotel();
	}
}
//计算订单总价
function calculateTotalMoney_hotel(){
	//定义分币种对象
	var thistotalMoney = {};
	//设置酒店总价格
	for(var p in total_hotel.totalMoney){
		thistotalMoney[p] = total_hotel.totalMoney[p];
	}
	//累计加床、餐费、其他费用
	$("#traveler form.travelerTable").each(function(i, obj) {
		$(obj).find("[data='quotation'] .otherExpenses").each(function(index, element) {
			if(0 == $(element).find("[name='otherCost']").length){
				//房型块dd
				var $dd = $(element).parents("dd[data-for^='room']");
				//取得该房型的日期
				var str_date = $dd.find(".hotelOrder-list li").eq($dd.find(".hotelOrder-center .otherExpenses").index(element)).find("span").text().replace(/ /g,'');
				var objDate = rooms[$dd.attr("data-for").replace(/ /g,'')].dates[str_date];
				//住房要求
				if("1" == $(element).find("[name*='housingDemand']").val()){//合住
					var $addbed = $(element).find("[name*='addbed']:checked");
					if(0 != $addbed.length){//加床
						var arr_bedMoney = $addbed.attr("price").split("-");
						thistotalMoney[arr_bedMoney[0]] += isFloat(arr_bedMoney[1].replace(/[ ]/g,"").replace(/,/g,""));
					}
				}
				//升餐
				if("1" == $(element).find("[name*='Lmeal']:checked").val()){
					var arr_mealMoney = $(element).find("[name*='meal'] option:selected").attr("price").split("-");
					thistotalMoney[arr_mealMoney[0]] += isFloat(arr_mealMoney[1].replace(/[ ]/g,"").replace(/,/g,""));
				}
			}else{
				//其他费用
				var $ipt = $(element).find("[name='otherCost']");
				if(("" != $ipt.val()) && ("0" != $ipt.val())){
					var otherCostType = $(element).find("select option:selected").text();
					thistotalMoney[otherCostType] += isFloat($ipt.val());
				}
			}
		});
    });
	
	//设置订单总价
	var html = '';
	for(var p in thistotalMoney){
		if(0 != thistotalMoney[p]){
			if("" != html){
				html += "+";
			}
			html += '<i>' + p +'</i><strong>' + milliFormat(thistotalMoney[p],1) + '</strong>';
		}
	}
	if("" == html){
		$("[data='totalMoney']").html(0);
	}else{
		$("[data='totalMoney']").html(html);
	}
}

//删除住房日期
function xdhotel_remove(dom){
	var removeNum=$(dom).parent().index();//获取当前要删除的序列
	var ispan=$(dom).prev().text().replace(/ /g,'');//获取当前删除的文本
	$(dom).parents('.hotelOrder-center').prev('.hotelOrder-top').find('label').each(function(i,e){
		if($(e).text().replace(/ /g,'')==ispan){
			$(e).find('input[type=checkbox]').prop('checked',false);
		}
		return;
	});
	//修改房型容量
	var $deleteCon = $(dom).parent().parent().siblings('.otherExpenses').eq(removeNum);
	var $housingDemand = $deleteCon.find("[name*='housingDemand']");
	var roomName = $housingDemand.attr("name").split("-")[0];
	var theDate = rooms[roomName].dates[ispan];
	if("1" == $housingDemand.val()){//合住
		if(($deleteCon.find("[name*='addbed']").length != 0) && $deleteCon.find("[name*='addbed']").is(":checked")){
			theDate.capacitied[1] -= 1;
		}else{
			theDate.capacitied[0] -= 1;
		}
	}else{
		theDate.capacitied[0] -= theDate.capacity[0];
	}
	setDateIsDisabled(roomName,ispan,1);
	//当前表单
	var $form = $deleteCon.parents("form");
	//删除当前后给谁追加类名
	if(removeNum == 0){
		$(dom).parent().next().addClass('hotel-lion').siblings().removeClass('hotel-lion');
		$deleteCon.next().show();
	}else{
		$(dom).parent().prev().addClass('hotel-lion').siblings().removeClass('hotel-lion');
		$deleteCon.prev().show();
	}
	//删除对应项
	$deleteCon.remove();
	$(dom).parent().remove();
	calculateSingleTraveler_hotel($form);
}

//提交
function toSave_hotel(){
	//分表单提交保存
	$("#traveler form").each(function(index,element){
		var $btn = $(element).find(".btn-save");
		if($btn.text() == "保 存"){
			$(element).find(".btn-save").trigger("click");
		}
	});
}
/*==============订单-酒店修改 end=============*/

/*==============订单-单团、参团签证 begin=============*/
//签证撤签
// function jbox_cq(){
	// var html = '<div style="margin-top:20px; font-size:20px; text-align:center;">';
	// html += '是否提交撤签申请';
	// html += '</div>';
	// $.jBox(html, { title: "签证已送签",persistent:true,buttons:{'确定': 1,'取消': 0}, submit:function(v, h, f){
		// if (v=="1"){
			// return true;
		// }else{
			// $('.tourist-ckb').find('.traveler:eq(0)').removeAttr('checked');
		// }
	// },height:150,width:380});
// }

//自备签勾选显示隐藏
function ydbz2zibeiqian(){
	$("#traveler").on("click","input[name=zibeiqian]",function(){
		var $this = $(this);
		var $siblingsCkb = $this.parents(".tourist-ckb").children("input[type=checkbox]");
		var thisIndex = $siblingsCkb.index($this);
		var $tips = $this.parents(".ydbz_tit_child").siblings(".zjlx-tips").eq(0);
		if($this.attr('checked')) {
			if(!$tips.is(":visible")) {
				$tips.show();
				$tips.closest("form.travelerTable").find("input[name='idCard']").addClass('required');
				$tips.closest("form.travelerTable").find("input[name='idCard']").prev().find("span").show();
			}
			$tips.children("ul").eq(thisIndex).show();
			if($this.siblings("input[name='zibeiqian']").not("input:checked").length == 0){
				$tips.nextAll().hide();
			}else{
				$tips.nextAll().show();
			}
		} else {
			//是否撤签弹出框
			if("1" == $this.attr("data-hadOrderd")){//有撤签
				var html = '<div style="margin-top:20px; font-size:20px; text-align:center;">';
				html += '是否提交撤签申请';
				html += '</div>';
				$.jBox(html, { title: "签证已送签",persistent:true,buttons:{'确定': 1,'取消': 0}, submit:function(v, h, f){
					if (v=="1"){//确定撤签
						$tips.children("ul").eq(thisIndex).hide(500,function() {
							var isshow = 0;
							$tips.children("ul").each(function(index, element) {
								if($(element).is(":visible")){
									isshow++;
								}
							});
							if(0 == isshow) {
								$tips.hide();
								$tips.closest("form.travelerTable").find("input[name='idCard']").removeClass('required');
								$tips.closest("form.travelerTable").find("input[name='idCard']").prev().find("span").hide();
							}
						});
						$tips.nextAll().show();
						$this.removeAttr("data-hadOrderd");
						return true;
					}else{//取消撤签
						$this.prop('checked',true);
					}
				},height:150,width:380});
			}else{//没有撤签
				$tips.children("ul").eq(thisIndex).hide(500,function() {
					var isshow = 0;
					$tips.children("ul").each(function(index, element) {
						if($(element).is(":visible")){
							isshow++;
						}
					});
					if(0 == isshow) {
						$tips.hide();
						$tips.closest("form.travelerTable").find("input[name='idCard']").removeClass('required');
						$tips.closest("form.travelerTable").find("input[name='idCard']").prev().find("span").hide();
					}
				});
				$tips.nextAll().show();
			}
		}
	});
}
/*==============订单-单团、参团签证 end=============*/

/*==============订单-签务身份签证修改 begin=============*/
function orderVisa_sign(){
	//展开收起
	expandAndClose();
	//单个游客的保存
	$(".btn-save").click(function(){
		var $this = $(this);
		var btnTxt = $this.text();
		var $form = $this.parents("form");
		if("保 存" == btnTxt){
			//添加表单验证
			var isvalidate = $form.validate({ignore:""}).form();
			if(isvalidate){
				$form.disableContainer( {
					blankText : "—",
					selectIsVisible:0
					//formatNumber : formatNumberArray
				});
				//订单修改中的办签资料
				if(0 != $(".tourist-ckb").length){
					$(".tourist-ckb").each(function(index, element) {
                        var $ckb = $(element).find("input[type='checkbox']");
						$ckb.each(function(i, obj) {
                           if(!$(obj).is(":checked")){//如果没有选中，隐藏后面的文字
								$(obj).next().hide();
							}
							$(obj).hide();
                        });
                    });
				}
				$this.text("修 改");
			}else{
				top.$.jBox.tip('“游客' + $form.find(".visitorTit .visitorTit-label i").text() +'”中有未填写的必填项！', 'error');
			}
		}else{
			$form.undisableContainer( {
				blankText : "—"//,
				//formatNumber : formatNumberArray
			});
			//订单修改中的办签资料
			if(0 != $(".tourist-ckb").length){
				$(".tourist-ckb").each(function(index, element) {
					var $ckb = $(element).find("input[type='checkbox']");
					$ckb.each(function(i, obj) {
					   if(!$(obj).is(":checked")){//如果没有选中，隐藏后面的文字
							$(obj).next().show();
						}
						$(obj).show();
					});
				});
			}
			$this.text("保 存");
		}
	});
	//页面的“确定”按钮
	$("#confirm").click(function(){
		//分表单提交保存
		$("#traveler form").each(function(index,element){
			var $btn = $(element).find(".btn-save");
			if($btn.text() == "保 存"){
				$(element).find(".btn-save").trigger("click");
			}
		});
	});
}

//借护照弹出框
function jbox_jhz() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<label class="jbox-label" style="width:100px;">护照借出领取人：</label><input name="" type="text" /><br /><label class="jbox-label" style="width:100px;">护照借出时间：</label><input name="" type="text" /><br /><label class="jbox-label" style="width:100px;">护照借出备注：</label><input name="" type="text" />';
	html += '</div>';
	$.jBox(html, { title: "借护照",buttons:{'护照领取单': 1,'确认领取': 2}, submit:function(v, h, f){
		if (v=="2"){
			jbox_hhz();
			return true;
		}
	},height:220,width:380});
}

//还护照弹出框
function jbox_hhz() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<label class="jbox-label" style="width:100px;">护照归还领取人：</label><input name="" type="text" /><br /><label class="jbox-label" style="width:100px;">护照归还时间：</label><input name="" type="text" /><br /><label class="jbox-label" style="width:100px;">护照归还备注：</label><input name="" type="text" />';
	html += '</div>';
	$.jBox(html, { title: "还护照",buttons:{'确认归还': 1}, submit:function(v, h, f){
		if (v=="1"){
			return true;
		}
	},height:220,width:380});
}

//申请借款弹出框
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
/*==============订单-签务身份签证修改 end=============*/

/*==============订单-签证-签务身份订单列表 begin=============*/
//订单-签务身份订单-借款弹出框
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

//订单-签务身份订单-借款明细弹出框
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

//订单-签务身份订单-还押金收据弹出框
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

//订单-签务身份订单-办签资料弹出框
function jbox_qszl() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<label class="jbox-label" style="width:100px; float:none;">资料原件：</label><span style=" width:80px; display:inline-block;"><input name="" type="checkbox" value="" style="margin:0; margin-right:5px;" />护照借出</span><span style=" width:80px; display:inline-block;"><input name="" type="checkbox" value="" style="margin:0; margin-right:5px;"/>身份证借出</span><br /><label class="jbox-label" style="width:100px; float:none;">复印件：</label><span style=" width:80px; display:inline-block;"><input name="" type="checkbox" value="" style="margin:0; margin-right:5px;"/>户口本</span><span style=" width:80px; display:inline-block;"><input name="" type="checkbox" value="" style="margin:0; margin-right:5px;" />房产证</span>';
	html += '</div>';
	$.jBox(html, { title: "办签资料",buttons:{'确定': 1}, submit:function(v, h, f){
		if (v=="1"){
			return true;
		}
	},height:220,width:380});
}

//订单-签务身份订单-交押金弹出框
function jbox_jyj() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<p><label class="jbox-label">选择币种：</label><select class="jbox-width100"><option>人民币</option><option>美元</option></select></p><label class="jbox-label">押金金额：</label><input type="text" class="jbox-width100" value=""> 元';
	html += '</div>';
	$.jBox(html, { title: "确定需交押金？",buttons:{'需交押金': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			jbox_xgyj();
			return true;
		}
	},height:200,width:380});
}

//订单-签务身份订单-修改押金弹出框
function jbox_xgyj() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<label class="jbox-label">人民币：</label><input type="text" class="jbox-width100" value=""> 元';
	html += '</div>';
	$.jBox(html, { title: "修改押金金额",buttons:{'确定': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			jbox_cxyj();
			return true;
		}
	},height:150,width:380});
}

//订单-签务身份订单-撤销押金弹出框
function jbox_cxyj() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<p style="padding:20px; line-height:180%;">此游客正在押金转担保审核中，修改押金金额将撤销申请，是否继续修改押金？';
	html += '</div>';
	$.jBox(html, { title: "撤销押金转担保申请？",buttons:{'确定': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			return true;
		}
	},height:220,width:380});
}
/*==============订单-签证-签务身份订单列表 end=============*/

/*==============订单-签证-销售订单列表 begin=============*/
//还押金收据弹出框
function jbox_hyjsj() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	//收据金额
	html += '<label class="jbox-label">收据金额：</label><input name="" type="text" />';
	//还收据人
	html += '<br /><label class="jbox-label">还收据人：</label><input name="" type="text" />';
	//归还时间
	html += '<br /><label class="jbox-label">归还时间：</label><input name="" type="text" class="dateinput jbox-dateinput" onclick="WdatePicker()" />';
	//备注
	html += '<br /><label class="jbox-label">备注：</label><textarea></textarea>';
	html += '</div>';
	$.jBox(html, { title: "还押金收据",buttons:{'还收据': 1}, submit:function(v, h, f){
		if (v=="1"){
			return true;
		}
	},height:300,width:380});
}
/*==============订单-签证-销售订单列表 end=============*/

/*==============改价申请 begin=============*/
/*改价记录 底部增加
 *@productType：产品类型。visa：签证；
*/
function gaijia(productType){
	//改价产品类型
	var theProductType = productType ? productType : "";
	var $optionCurrency = $("#currencyTemplate option");
	//总价--当前金额=游客改价中的当前应收列相加
	var oldSum = new CurrencyMoney($optionCurrency);
	$("tr[group^='travler']").each(function(index, element) {
        var currencyName = $(element).find("span[name='gaijiaCurency']").text().replace(/[ ]/g,"");
		oldSum[currencyName] += isFloat($(element).find("td[flag='beforeys']").text().replace(/[ ]/g,"").replace(/,/g,""));
    });
	//如果是签证产品
	if("visa" == theProductType){
		$("#teamVisa tbody tr").each(function(index, element) {
			var currencyName = $(element).find("td[name='gaijiaCurency']").text();
			oldSum[currencyName] += isFloat($(element).find("td[flag='beforedj']").text().replace(/[ ]/g,"").replace(/,/g,""));
		});
	}
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
		var html = '<li><i><input type="text" name="" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">其他款项</span></i>&nbsp;';
		html += '<i><select name="teamCurrency">{0}<select></i>&nbsp;'.replace("{0}",$("#currencyTemplate").html());
		html += '<i><input type="text" name="teamMoney" class="gai-price-ipt1" flag="istips" onkeyup="validNum(this)" onafterpast="validNum(this)" maxlength="20" /><span class="ipt-tips ipt-tips2">费用</span></i>&nbsp;';
		html += '<i><input type="text" name="" class="gai-price-ipt2" flag="istips" /><span class="ipt-tips ipt-tips2">备注</span></i>&nbsp;';
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
		calculateSum(0);
	}).on("change","select[name='teamCurrency']",function(){//修改款项币种
		calculateSum(0);
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
				//判断是否包括该币种
				var $thisTr = $(".modifyPrice-table tr[group='" + element +"']");
				var isIncludeCurrency = 0;
				$thisTr.each(function(){
					var $currency1 = $(this).find("[name='gaijiaCurency']");
					var currencyName1 = ($currency1[0].tagName=="SPAN") ? $currency1.text() : $currency1.find("option:selected").text();
					if(currencyName1 == thisCurrencyName){
						isIncludeCurrency = 1;
						$(this).find("input[name='" + inputName +"']").val(inputValue).trigger("focusout");
						return;
					}
				})
				//如果不包含应用的币种，则新增一条币种记录
				if(!isIncludeCurrency){
					$(".modifyPrice-table tr[group='" + element +"'] .gaijia-add").eq(0).trigger("click");
					var $modifyTr = $(".modifyPrice-table tr[group='" + element +"']").last();
					//设置币种的选中状态
					$modifyTr.find("select[name='gaijiaCurency'] option").each(function(index,element){
						if($(this).text() == thisCurrencyName){
							$(this).prop("selected",true);
						}
					});
					//设置改价差额
					$modifyTr.find("input[name='" + inputName + "']").val(inputValue).attr("defaultvalue",inputValue).trigger("focusout");
				}
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
		
		//原始应收价
		HTMLAdd += '<td class="tr" flag="beforeys">0.00</td>';
		//当前应收价
		HTMLAdd += '<td class="tr">0.00</td>';
		//改价差额
		HTMLAdd += '<td class="tc"><dl class="huanjia">';
		HTMLAdd += '<dt><input name="plusys" type="text" value="0.00" defaultValue="0.00" onafterpast="validNum(this)" onkeyup="validNum(this)" maxlength="20" /></dt>';
		HTMLAdd += '<dd><div class="ydbz_x" flag="appAll">应用全部</div><div class="ydbz_x gray" flag="reset">还原</div></dd>';
		HTMLAdd += '</dl></td>';
		//备注
		HTMLAdd += '<td class="tc"><input name="" type="text" value="" /></td>';
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
				var $addOption = $optionDelete.clone();
				$addOption.removeAttr("selected").prop("selected",false);
				$theselect.append($addOption);
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
                var currencyName = $(element).find("td[name='gaijiaCurency']").text();
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
/*==============改价申请 end=============*/

/*==============改价详情页 begin=============*/
//重新申请弹出框
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
/*==============改价详情页 end=============*/

/*==============转团申请 begin=============*/
//转入团号
function changeGroups(obj){
	$(obj).parent().find("div").show();
}
/*==============转团申请 end=============*/

/*==============发票申请 begin=============*/
//申请发票合开发票
//“开票方式”为合并类型时，显示添加合开订单按钮
function invoiceTypeChg(){
	var value=$(".invoiceTypeChg option:selected").index();
	$(".invoicetd").val("")
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
						//var html=$(".invoicedivnone").clone(true);
						var html='<div class="invoicediv invoicedivnone">';
							html+='<div class="ydbz_tit orderdetails_titpr">订单<span>'+(orderNum + 1)+'</span><a class="ydbz_x" onclick="invoiceOrderDel(this)">删除</a></div>';
							html+='<table border="0" style="margin-bottom:10px">';
							html+=' <tbody>';
							html+=' <tr>';
							html+='  <td class="mod_details2_d1">团号：</td>';
							html+='<td class="mod_details2_d2">rtreter</td>';
							html+=' <td class="mod_details2_d1">出团日期：</td>';
							html+='<td class="mod_details2_d2">2014-09-12</td>';
							html+=' <td class="mod_details2_d1">订单号：</td>';
							html+='<td class="mod_details2_d2">JHG140912049</td>';
							html+='<td class="mod_details2_d1">预定日期：</td>';
							html+='<td class="mod_details2_d2">2014-09-05 20:19:17</td>';
							html+='</tr>';
							html+='<tr>';
							html+='<td class="mod_details2_d1">人数：</td>';
							html+='<td class="mod_details2_d2">1</td>';
							html+='<td class="mod_details2_d1">应收尾款：</td>';
							html+='<td class="mod_details2_d2">¥1,000</td>';
							html+=' <td class="mod_details2_d1">财务到账：</td>';
							html+='<td class="mod_details2_d2">¥20</td>';
							html+='<td class="mod_details2_d1">已开发票：</td>';
							html+='<td class="mod_details2_d2">¥0</td>';
							html+='</tr>';
							html+=' </tbody>';
							html+='</table>';
							html+='<table class="table table-striped table-bordered">';
							html+='<thead>';
							html+=' <tr>';
							html+='<th width="10%">付款金额</th>';
							html+='<th width="10%">达账金额</th>';
							html+='<th width="10%">退款金额</th>';
							html+='<th width="10%">已开票金额</th>';
							html+='<th width="10%">可开票金额</th>';
							html+='<th width="10%">本次开票金额</th>';
							html+='</tr>';
							html+=' </thead>';
							html+='<tbody>';
							html+=' <tr>';
							html+=' <td class="tr" name="exchangeratetd">¥20</td>';
							html+='<td class="tr">¥20</td>';
							html+=' <td class="tr">-</td>';
							html+=' <td class="tr">¥0</td>';
							html+='<td class="tr">¥20</td>';
							html+='<td class="tc"><input type="text" class="rmb invoicetd" onblur="refundInputs(this)" onafterpaste="invoiceInputin(this)" onkeyup="invoiceInputin(this)"></td>';
							html+='</tr>';
							html+='</tbody>';
							html+=' </table>';
							html+=' </div>';
						$(obj).parent().before(html);
						//新增加的订单，“付款金额”列显示汇率
						exchangerateDiv();
					}
					return true;
				}	
		},height:180,width:500});
	});
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
//申请发票
function invoiceInputin(obj){
	var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
	// 只有当检测到不合法字符时才进行替换，因为实时替换会导致光标乱窜
	if (ms != obj.value) {
		var txt = ms.split(".");
		obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
	}
	if(obj.value=='.'){
		return;
	}
	totalinvoice();
}

//计算开票金额，并为“开票金额”文本框赋值
function totalinvoice(){
	var total=0;
	var totalone=0;
   	$(".invoicediv").each(function(index, element) {
		var invoiceIn=$(this).find(".invoicetd");
		invoiceIn.each(function(index, element) {
			var vals=$(this).val();
			if(vals==""){
					vals=0;
			}else{
				totalone += parseFloat(vals);
			}
		});
		total+=totalone;
		//$(this).find(".totalInvoiceOne").html(milliFormat(totalone,'1'));
		totalone=0;
		$(".totalinvoice").attr("value",milliFormat(total,'1'))
	}); 
}
/*==============发票申请 end=============*/

/*==============订单-销售订单修改（单团、散拼、签证）、订单-销售上传资料 begin=============*/
/*上传签证资料--图片*/
function ydbz2interfile(obj){
	var dest = $(obj).parent().find("span");
	var res = $(obj).val();      		
	$(dest).html(res);
	var ishave=$(obj).parent().find('.visaImg');
	if(ishave.length>0){
		$(obj).parent().find('.visaImg').html('<img width="300" onclick="showBigImg(\'images/body-bg.jpg\')" title="点击查看大图" src="images/body-bg.jpg">')
	}else{
		$(obj).parent().append('<div class="visaImg"><img width="300" onclick="showBigImg(\'images/body-bg.jpg\')" title="点击查看大图" src="images/body-bg.jpg"></div>')
	}
	$(obj).next().val('重新上传');
	var pdleft=$(obj).prev().width();
	$(obj).siblings('.visaImg');
}
/*==============订单-销售订单修改（单团、散拼、签证）、订单-销售上传资料 end=============*/

/*==============退款申请、切位退款 begin=============*/
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
	//游客退款-删除退款
	$('#contentTable').on("click",'.gaijia-delete',function(){
		$(this).parent().parent().parent().remove();
		totalRefund();
	})
	//游客退款-新增退款
	$('#contentTable').on("click",'.gaijia-add',function(){
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
//币种变化、单选、复选框点击：重新计算退款总金额
function changeRefund(){
	//币种变化
	$("#contentTable,.gai-price-ol").delegate("select[class='selectrefund']","change",function(){
		totalRefund();
	})
	//单个游客的复选框
	$(".table_borderLeftN").delegate("input[type='checkbox']","click",function(){
		totalRefund();
	})
	//全选
	$(".table_borderLeftN").delegate("input[name='allChk']","click",function(){
		if($(this).prop("checked")){
			$(".table_borderLeftN").find("input[type='checkbox']").attr("checked",'true'); totalRefund();
		}else{
			$(".table_borderLeftN").find("input[type='checkbox']").removeAttr("checked"); totalRefund();
		}
	})
}
//计算退款总金额
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

//货币文本框keyup
function refundInput(obj){
	var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
	// 只有当检测到不合法字符时才进行替换，因为实时替换会导致光标乱窜
	if (ms != obj.value) {
		var txt = ms.split(".");
		obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
	}
	if(obj.value=='.'){
		return;
	}
	totalRefund();
}
/*==============退款申请、切位退款 end=============*/

/*==============订单-签证-销售订单 end=============*/
//收款确认提醒
function paymentTips(){
	$(".notice_price").hover(function(){
		$(this).find("span").show();
	},function(){
		$(this).find("span").hide();
	});
}
/*==============退款申请、切位退款 end=============*/

/*==============订单-签证-销售身份-签证参团 begin=============*/
//申请参团弹出框
function jbox_sqct(){
	//var html = '<div style="margin-top:20px; padding-left:20px;">';
//	html += '<p>余位不足</p>';
//	html += '</div>';
//	$.jBox(html, { title: "申请参团",buttons:{'确定': 1}, submit:function(v, h, f){
//		if (v=="1"){
//			jbox_sqct1();
//			return true;
//		}
//	},height:180,width:410});
	
	var html = '<div class="add_allactivity" style="padding-top:15px;">';
	html += '<p><label>付款方式：</label><select><option value="">预占位</option><option value="">订金占位</option></select></p>';
	html += '<p><label>占位方式：</label><select><option value="">切位</option><option value="">切位2</option></select></p>';
	html += '</div>';
	html += '<div class="tc" style="padding-bottom:5px;"><input type="button" onclick="" value="参团" class="btn btn-primary"></div>';
	$.jBox(html, { title: "选择付款方式",buttons:{},height:180,width:410});
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
/*==============订单-签证-销售身份-签证参团 end=============*/

/*==============返佣申请 begin=============*/
function employee(){
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
	$("#totalBefore").html(html_oldSum);
	
	//总价--差额、改后初始化
	calculateSum(1);
	
	//增加团队改价
	$('.gai-price-btn').click(function() {
		var html = '<li><i><input type="text" name="" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">其他款项</span></i>&nbsp;';
		html += '<i><select name="teamCurrency">{0}<select></i>&nbsp;'.replace("{0}",$("#currencyTemplate").html());
		html += '<i><input type="text" name="teamMoney" class="gai-price-ipt1" flag="istips" onkeyup="validNum(this)" onafterpast="validNum(this)" /><span class="ipt-tips ipt-tips2">费用</span></i>&nbsp;';
		html += '<i><input type="text" name="" class="gai-price-ipt2" flag="istips" /><span class="ipt-tips ipt-tips2">备注</span></i>&nbsp;';
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
			oldMoney[moneyName] += parseFloat(thisInput.val());
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
/*==============返佣申请 end=============*/

/*==============订单-销售订单（单团、散拼、海岛、酒店、机票）上传确认单 begin=============*/
function uploadConfirmationt(ticket){
	/*ticket  ticket:机票出票确认单*/
	//var orderId = $("#airticketOrderId").val();
	var html = '<div style="margin-top:20px; padding-left:45px;"><p class="red">温馨提示:当前操作会替换您之前上传的确认单 !</p>';
		html += '<form id="uploadConfirmation" method="post" enctype="multipart/form-data" action="">';
		//html += '<input type="hidden" name="orderId" value=""/>';
		var text = "";
		if(ticket){ text="出票";}
		html += '<p>上传'+text+'确认单：<input name="orderConfirmation" type="file" value="上传"/></p>';
		html += '</form>';
		html += '</div>';
	$.jBox(html, { title: "本地上传",buttons:{'取消': 0,'确定': 1}, submit:function(v, h, f){
		if (v=="1"){
			$("#uploadConfirmation").submit();
			return true;
		}
	},height:200,width:410});	
}

/*==============订单-销售订单（单团、散拼、海岛、酒店、机票）上传确认单 end=============*/

/*==============订单-单团-销售订单列表 begin=============*/
//退团原因弹出框
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
/*==============订单-单团-销售订单列表 end=============*/

/*==============订单-计调-机票订单列表 begin=============*/
//航班备注弹出框
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
/*==============订单-计调-机票订单列表 end=============*/



// //订单-返佣记录-返佣详情-重新申请
// function jbox_cxsq() {
	// var submit = function (v, h, f) {
		// if(v==true)
		// {
			
		// }
		// return true;
	// };
	// jBox.warning("“重新申请”会取消本次返佣申请，并发起新的返佣申请。", "提示", submit, { buttons: { '取消': false, '重新申请': true} });
// }

//订单-签务身份订单-预约表
//function jbox_yyb() {
//	var html = '<div style="margin-top:20px; padding-left:20px;">';
//	html += '<div class="seach25 seach100" style="width:90%; padding-bottom:5px;"><p style="width:50px;">办签人：</p><p class="seach_r" style="width:auto;"><span class="seach_check"><select name="vendorOperator"><option value="0" selected="selected">办签人</option><option value="1">张三</option><option value="2">李四</option><option value="2">王五五</option><option value="2">赵紫龙</option><option value="2">田日照</option></select></span><span class="seach_checkbox" id="vendorOperatorShow"><a>张三</a></span></p></div><label style="width:50px; text-align:right; line-height:30px;">备注：</label><textarea name="" cols="" rows="" ></textarea>';
//	html += '</div>';
//	$.jBox(html, { title: "编辑预约表",buttons:{'确定': 1}, submit:function(v, h, f){
//		if (v=="1"){
//			return true;
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