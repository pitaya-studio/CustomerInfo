/*!
 * JQuery for the module of preordain(预定模块的脚本)
 *
 * Depends:common.js
 * the public functions used by this module:noTraveler、getBirthday、formhtml、addCosts、deleteCosts、textBoxAddPeople、
 * 	   textBox_del、sumInput、mealSelection、CurrencyMoney、isFloat、milliFormat、showBigImg
 *
 * Date: 2015-01-13
 */

/*=============预定-酒店列表 begin===============*/
//弹出框中--选择房型
function jboxHotelRoom(){
	$("input[name=hotelRoom]").live('click',function(){
		if($(this).is(":checked")==true){ 
			var objText=$(this).val();
			var html='<dd>';
			html+='<div class="hotelRoom"><b>'+objText+'</b>';
			html+='<label><span class="xing">*</span>入住日期：</label>';
			html+='<input readonly="readonly" onClick="WdatePicker({minDate:\'%y-%M-%d\',onpicked:function(){WdateNew(this);}})"  class="inputTxt dateinput"></div>';
			html+='</dd>';
			$(this).parent().parent().after(html);
			
		}else{
			var objText=$(this).val();
			$(this).parent().parent().siblings('dd').each(function()
			{
				if($(this).find('.hotelRoom b').text()==objText)
				{
					$(this).remove();
					
				}
			});
		}
	});
}
//删除入住时间
function hotelRemove(obj){
	$(obj).parent().remove();
}
//选择入住日期
var hotelName=1;
function WdateNew(obj){
	var isInclude = 0;
	var theTime=$(obj).val();	
	$(obj).parents('dd').find('.hotelRoom-list').each(function(index, element) {
       var a= $(element).find('label').eq(0).text();
	   if(a==theTime){
			isInclude = 1;
			return;
		}
    });
	if(0 == isInclude){
		hotelName=hotelName+1;
		var dateHtml='<div class="hotelRoom-list">';
		dateHtml+='<label>'+theTime+'</label>';
		dateHtml+='<label><input type="radio" checked="checked" name="hotel_'+hotelName+'">切位订单</label>';
		dateHtml+='<input name="" type="text" class="w50" />个';
		dateHtml+='<label><input type="radio" checked="checked" name="hotel_'+hotelName+'">占位订单</label>';
		dateHtml+='<input name="" type="text" class="w50" />个';
		dateHtml+='<label onclick="hotelRemove(this)">删除</label>';
		dateHtml+='</div>';
		$(obj).parent().after(dateHtml);
	}
	return true;
}
/*=============预定-酒店列表 end===============*/

/*=============预定-单团、散拼、海岛游填写下单信息 begin===============*/
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
/*=============预定-单团、散拼、海岛游填写下单信息 end===============*/

/*==============预定-签证填写下单信息 begin=============*/
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
/*==============预定-签证填写下单信息 end=============*/

/*==============预定-机票填写下单信息 begin=============*/
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

//保存并支付
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
/*==============预定-机票填写下单信息 end=============*/

/*==============预定-酒店填写下单信息 begin=============*/
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
//保存并支付
function toSave_hotel(){
	//联系人必填验证
	var contactPeople = $("#addForm").validate().form();
	//如果通过验证
	if(contactPeople){
			
	}else{
		top.$.jBox.tip('“预订人信息”中有未填写的必填项！', 'error');
	}
	//分表单提交保存
	$("#traveler form").each(function(index,element){
		var $btn = $(element).find(".btn-save");
		if($btn.text() == "保 存"){
			$(element).find(".btn-save").trigger("click");
		}
	});
}
/*==============预定-酒店填写下单信息 end=============*/

/*==============预定-单团、散拼 begin=============*/
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
	});
}
/*==============预定-单团、散拼 end=============*/

/*==============预定-填写下单信息（单团、散拼、签证） begin=============*/
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
	$(obj).siblings('.visaImg').css({'padding-left':+pdleft+'px'});
}
/*==============预定-填写下单信息（单团、散拼、签证） end=============*/