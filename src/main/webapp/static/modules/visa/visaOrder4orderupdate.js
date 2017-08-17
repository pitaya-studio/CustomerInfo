var agentList = [];

function loadAgent()
{
	agentList = [];
	$.ajax({
		   type: "GET",
		   url: g_context_url + "/visa/preorder/agentList",
		   dataType:"json",
		   success: function(msg){
			   if(msg && msg.agentList){
				   agentList = msg.agentList;
				   appentAgentOptions(agentList);
			   }
		   }
		});
}


function appentAgentOptions(agentList){
	var array = [];
	var agentId = $("#agentId").val();
	$.each(agentList,function(i,n){
		array.push('<option value="' + n.id + '">' + n.agentName + '</option>	');
	});
	$("#agentIdIn").append(array.join(""));
	$("#agentIdIn").val(agentId);
	agentChange();
}

function agentChange()
{
	var value = $("#agentIdIn").val();
	if(value == "" || value == -1){
		$("#signedAgentInfo").hide();
		return;
	}
	
	$("#agentId").val(value);
	//找到渠道信息，显示渠道详细信息
	showAgentInfo(value);
	$("#signedAgentInfo").show();
}

function showAgentInfo(agentId){
	var targetAgent = null;
	for(var i = 0; i < agentList.length; i++){
		if(agentList[i].id == agentId){
			targetAgent = agentList[i];
			break;
		}
	}
	$("#orderPersonName").val(targetAgent.agentContact);
	$("#orderPersonPhoneNum").val(targetAgent.agentContactMobile);
	$("#tel").val(targetAgent.agentContactTel);
	$("#address").val(targetAgent.agentContactAddress);
	$("#fix").val(targetAgent.agentContactFax);
	$("#qq").val(targetAgent.agentContactQQ);
	$("#email").val(targetAgent.agentContactEmail);
	
}

var isdoSave = false;
var count = 0;
outerrorList = new Array();
var formatNumberArray = new Array();
formatNumberArray.push("sum");
jQuery(function($) {
	$.fn.datepicker = function(option) {
		var opt = $.extend( {}, option);
		$(this).click(function() {
			WdatePicker(opt);
		});
	}
});

$(function() {

/*	$("#addTraveler").click(
			function() {
				var $table = $("#travelerTemplate").children();
				var _travelerForm = $table.clone().addClass("travelerTable");
				_travelerForm.find("input[name^='personType']").attr("name",
						"personTypeCount" + count++);
				$("#traveler").append(_travelerForm);
				_travelerForm.find("input[name^='personType']:checked").trigger("change");
				
				dodatePicker();
				

			});
	$("#orderPersonNumChild").blur(function() {
		changeorderPersonelNum();
	});

	$("#orderPersonNumAdult").blur(function() {
		changeorderPersonelNum();
	});
	
	$("#orderPersonNumSpecial").blur(function() {
		changeorderPersonelNum();
	});

	$("#orderPersonelNum").blur(function() {
		var travelerNum = $(this).val();
		checkFreePosition();
		var deposit = $("#payDeposit").val();
		$("#frontMoney").val(deposit * travelerNum);
		changeTotalPrice();
	});*/

	getPassportValidity();

	dodatePicker();
   //游客信息 onblur 事件的处理	  游客名称失去焦点时触发
   $("#traveler").delegate(
			"input[name='travelerName']",
			"blur",
			function() {
				var srcname = $(this).val();
				
				var name = $(this).closest("form").find(".tourist-t-off .name").text(srcname);
				
				if ($.trim(srcname).length <= 0) {
					return false;
				}

				var pinying = $(this).closest("form").find(
						"input[name='travelerPinyin']").eq(0);
				var tName = $(this).closest("form").find("span[name='tName']").eq(0).html(srcname);
				$.ajax( {
					type : "POST",
					url : g_context_url+"/orderCommon/manage/getPingying",
					data : {
						srcname : $.trim(srcname)
					},
					success : function(msg) {
						pinying.val(msg);
					}
				});
		});

	$("#traveler").delegate(
			"a[name='deleteTraveler']",
			"click",
			function() {
				var $this = $(this);
				var travelerId = $(this).closest('.travelerTable').find("input.traveler[name='id']").val();
				$.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f) {
					if (v == 'ok') {
						// 显示按钮
						//$("#addTraveler").parent().show();
						if (travelerId == "") {
							// 无需记录
							var travlelerIndex = $this.closest('.travelerTable').find(".travelerIndex").text();
							travelerTotalPriceArr.splice(travlelerIndex-1,1);
							$this.closest('.travelerTable').remove();
							//记录游客删除的标记
							changeTotalPrice();
							//计算游客编号
							recountIndexTraveler();
							deleteTravelerAfter($this.closest('form'));
						} else {
							
								// 需要删除数据库中记录
								$.ajax( {
									type : "POST",
									url : g_context_url + "/visa/preorder/deleteTraveler",
									data : {
										travelerId : travelerId
									},
									success : function(msg) {
										top.$.jBox.tip('删除成功', 'success');
										// 无需记录
										var travlelerIndex = $this.closest('.travelerTable').find(".travelerIndex").text();
										travelerTotalPriceArr.splice(travlelerIndex-1,1);
										$this.closest('.travelerTable').remove();
										//记录游客删除的标记
										changeTotalPrice();
										//计算游客编号
										recountIndexTraveler();
										deleteTravelerAfter($this.closest('form'));
									}
								});
						}
				} else if (v == 'cancel') {

				}
			}	);
				
		
			});
/**
 * 删除游客后调整游客和价格
 * @param travelerForm 游客表单对象
 */
function deleteTravelerAfter(travelerForm){
	var travlelerIndex = travelerForm.find(".travelerIndex").text();
	travelerTotalPriceArr.splice(travlelerIndex - 1,1);
	travelerTotalClearPriceArr.splice(travlelerIndex - 1,1);
	travelerForm.remove();
	changeTotalPrice();
	recountIndexTraveler();
}

	// 改变人员类型时
	$("#traveler").delegate(
			"input[name^='personType']",
			"change",
			function() {
				var texamt = Number($("#texamt").val());
				var etj = Number($("#etj").val()) + texamt;
				var crj = Number($("#crj").val()) + texamt;
				var tsj = Number($("#tsj").val()) + texamt;
				
				if ($(this).val() == 2) {
					$(this).closest("form").find("input[name='payPrice']").val(
							etj);
					$(this).closest("form").find(".tourist-t-off .ydFont2").text(etj.toString()
							.formatNumberMoney('#,##0.00'));
					
					$(this).closest("form").find("input[name='payPrice']")
							.prev().text(
									etj.toString()
											.formatNumberMoney('#,##0.00'));
				} else if ($(this).val() == 1) {
					$(this).closest("form").find("input[name='payPrice']").val(
							crj);
					$(this).closest("form").find(".tourist-t-off .ydFont2").text(crj.toString()
							.formatNumberMoney('#,##0.00'));
					$(this).closest("form").find("input[name='payPrice']")
							.prev().text(
									crj.toString()
											.formatNumberMoney('#,##0.00'));
				} else {
					$(this).closest("form").find("input[name='payPrice']").val(
							tsj);
					$(this).closest("form").find(".tourist-t-off .ydFont2").text(tsj.toString()
							.formatNumberMoney('#,##0.00'));
					$(this).closest("form").find("input[name='payPrice']")
							.prev().text(
									tsj.toString()
											.formatNumberMoney('#,##0.00'));
				}
				changeTotalPrice();
			});

	// 第一步的下一步
	$("#oneToSecondStepDiv").click(function() {
		  
		   // 验证办签人数，不能为0
			if (!checkFreePosition()) {
				return;
			}
			var visaPersonNum = $("#orderPersonNumAdult").val();
			if (!visaPersonNum||visaPersonNum==0||visaPersonNum>1000){
				 top.$.jBox.tip("办签人数必须大于零小于一千！");
				return;
			}
			
		   
			var agentinfoName = $("#agentinfoNameId").val();
			if(agentinfoName){
				if (agentinfoName.replace( /^\s+/, "" ).replace( /\s+$/, "" ).length<1){
					 top.$.jBox.tip("非签约渠道名称为必填项！");
					 return;
				}
			}

			outerrorList = new Array();
			_doValidateorderpersonMesdtail();
			//_doValidateproductOrderTotal();
			createDivInTable(outerrorList);
			//校验渠道信息
			if(outerrorList.length > 0){
				return;
			}
		
			//保存订单基本信息
			if(!saveOrderBasicInfo()){
				//有错误直接退出
				return;
			}
			
			$("#productOrderTotal").disableContainer( {
				blankText : "—",
				formatNumber : formatNumberArray
			});
			$("#orderpersonMesdtail").disableContainer( {
				blankText : "—",
				formatNumber : formatNumberArray
			});
			$("#addContact").hide();
			$("#orderpersonMesdtail span.gray").hide();
			
			$("#manageOrder_new").show();
			$("#manageOrder_m").show();
			$("#oneToSecondOutStepDiv").hide();
			$("#secondDiv").show();

			var _closeOrExpand = $(".closeOrExpand").eq(0);
			if (_closeOrExpand.hasClass("ydExpand")) {
				_closeOrExpand.click();
			}
			_closeOrExpand.parent().next().next(".orderPersonMsg").show();
			$("#stepbar").removeClass("yd-step1").addClass("yd-step2");
			_addTravelCount();
			
		});
	
	

	// 第二步的上一步
	$("#secondToOneStepDiv").click(function() {
		$("#productOrderTotal").undisableContainer();
		$("#orderpersonMesdtail").undisableContainer();
		$("#addContact").show();
		$("#orderpersonMesdtail span.gray").show();
		$("#oneToSecondOutStepDiv").show();
		$("#secondDiv").hide();
		$("#manageOrder_new").hide();
		$("#manageOrder_m").hide();
		$("#stepbar").removeClass("yd-step2").addClass("yd-step1");
		var _closeOrExpand = $(".closeOrExpand").eq(0);
		_closeOrExpand.parent().next().next(".orderPersonMsg").hide();

		if (_closeOrExpand.hasClass("ydClose")) {
			_closeOrExpand.click();
		}

	});
	// 第二步的下一步
	$("#secondToThirdStepDiv").click(
			function() {
				//验证号码必填
				var flag = true;
				$("input[name='idCard']:visible").each(function(index, obj) {
					if($(this).val() == "") {
						var name = $(this).parent().children("label").text();
						top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
		                top.$('.jbox-body .jbox-icon').css('top','55px');
		                $(this).focus();
		                flag = false;
		                return false;
					}
				});
				
				if(!flag) {
					return false;
				} 
				
/*				if ($("#orderPersonNumAdult").val() < countAdult()
						|| $("#orderPersonNumChild").val() < countChild()
						|| $("#orderPersonNumSpecial").val() < countSpecial()) {
					top.$.jBox.tip('成人、儿童或特殊人数与初始值不匹配请修改', 'error');
					return false;
				}*/
				
				//进入第三步前校验
				  /*	  var travelerTables = $("#traveler form");
			  var orderPersonnum = $("#orderPersonelNum").val();
			  var orderPersonNumAdult = $("#orderPersonNumAdult").val();
			  
			  alert("travelerTables=="+travelerTables.length);
			  alert("orderPersonnum=="+orderPersonnum);
			  alert("orderPersonNumAdult=="+orderPersonNumAdult);
			  
			  
			 	$.each(travelerTables, function(key, value) {
					return;
				});
						
				var orderPersonelNum = $("#orderPersonelNum").val();
				var neededNum = orderPersonelNum - travelerTables.length;
				if (orderPersonelNum!=travelerTables.length) {
					 top.$.jBox.tip("您还需添加" +neededNum+ "个游客！");
					return;
				}
				*/
				
				var issave = true;
				$("#traveler form").each(function(index, element) {
                	var travelerId=$(element).find("input.traveler[name='id']").val();
					if(travelerId==""){
						var errortip="游客"+(index+1)+"没有保存"
						top.$.jBox.tip(errortip, 'error');
						issave = false;
						return false;
					}
             	})
			 	if(!issave) {
					return false;
				}else{
					if ($("#orderPersonNumAdult").val() !=$("#traveler form").length) {
						top.$.jBox.tip('添加游客数与办签人数不符，请添加游客！', 'error');
						return false;
					}
				}
			//------------------------------------	
				
				outerrorList = new Array();
				_doValidatecontactForm();
				_doValidatetravelerForm();
				_doValidateorderpersonMesdtail();
				_doValidateproductOrderTotal();

				createDivInDiv(outerrorList);

				if (outerrorList.length > 0) {
					return;
				}
				
				//保存信息, 在保存并支付时保存包括特殊需求，订单状态灯信息;
				//saveAllSecondInfo();
				
				$("#productOrderTotal").disableContainer( {
					blankText : "—",
					formatNumber : formatNumberArray
				});
				$("#orderpersonMesdtail").disableContainer( {
					blankText : "—",
					formatNumber : formatNumberArray
				});
				$("#manageOrder_new").disableContainer( {
					blankText : "—",
					formatNumber : formatNumberArray
				});
				$("#manageOrder_m").disableContainer( {
					blankText : "—",
					formatNumber : formatNumberArray
				});
				var sums = $(".tourist-right").find("input[name='sum']");
				$.each(sums, function(key, value) {
					var _$span = $(value).next();
					_$span.text("￥" + _$span.text());
					;
				});

				if(flag) {
					//游客身份证类型不能修改
					$("input[name='papersType']").attr("disabled", true);
				}
				
				$("#secondDiv").hide();
				$("#thirdDiv").show();
				var _closeOrExpand = $(".closeOrExpand").eq(0);
				_closeOrExpand.parent().next().next(".orderPersonMsg").hide();
				$("#stepbar").removeClass("yd-step2").addClass("yd-step3");
			});
	
	
	$("#thirdToSecondTStepDiv").click(function() {
		//游客身份证类型可以修改
		$("input[name='papersType']").attr("disabled", false);
		$("#manageOrder_new").undisableContainer();
		$("#manageOrder_m").undisableContainer();

		if ($("#orderid").val() != "") {
			$("#productOrderTotal").undisableContainer();
			$("#orderpersonMesdtail").undisableContainer();
		}

		$("#secondDiv").show();
		$("#thirdDiv").hide();
		// 如果是修改 这个地方不能显示

			if ($("#orderid").val() == "") {
				var _closeOrExpand = $(".closeOrExpand").eq(0);
				_closeOrExpand.parent().next().next(".orderpersonMesdtail").show();
			}

			$("#stepbar").removeClass("yd-step3").addClass("yd-step2");
	});
	
	//保存 保存并支付
	$("#thirdToFourthStepDiv").click(function() {
		  //doPay();
		// copy  自  single  
		var currencyId = paramCurrencyId.join(",");//currencyID 币种ID{1,2,5,4}
		var aparamCurrenctPricetemp = [];
		$.each(paramCurrenctPrice, function(key, value) {
			value = value.toFixed(2);
			aparamCurrenctPricetemp.push(value);
		});
		var currencyPrice = aparamCurrenctPricetemp.join(",");
		
			
		var currencyClearId = paramClearCurrencyId.join(",");
		var aparamClearCurrenctPricetemp = [];
		$.each(paramClearCurrenctPrice, function(key, value) {
			value = value.toFixed(2);
			aparamClearCurrenctPricetemp.push(value);
		});
		var currencyClearPrice = aparamClearCurrenctPricetemp.join(",");
		
		
		var orderid =$("#orderid").val();
		var specialremark = $('#specialremark').val();
		
		
		$.ajax({
			type : "POST",
			url : g_context_url +"/visa/preorder/lastSave",
			data : {
				"orderid" : orderid,
                "specialremark" : specialremark,
                "costcurrencyId" : currencyId, //成本价货币ID
                "costcurrencyPrice" : currencyPrice,//成本价货币金额
                "currencyId" : currencyClearId, //结算价货币ID
                "currenctPrice" : currencyClearPrice//结算价货币金额
			},
			success : function(msg) {
				if (msg.errorMsg) {
					top.$.jBox.tip(msg.errorMsg, 'error');
					return false;
				} else {
					
					var agentinfoId;
					if(msg.productOrder.agentinfoId==null||msg.productOrder.agentinfoId==undefined){
						agentinfoId=-1;
					}else{
						agentinfoId=msg.productOrder.agentinfoId;
					}
					
					/**
					 * 支付参数说明
					 * orderId：订单ID
					 * orderNum：订单编号
					 * payPriceType：付款状态
					 * businessType:业务类型 1-订单，2-游客   
					 * orderType：订单类型
					 *agentId：渠道ID
					 *paramCurrencyId：
					 *paramCurrencyPrice：
					 *paramTotalCurrencyId:
					 *paramTotalCurrencyPrice:
					 */
					//currencyPrice = currencyPrice.a.toFixed(2);
					
					var param = "orderId="+ msg.productOrder.id 
					+ "&orderNum="+ msg.productOrder.orderNo
					+ "&payPriceType="+ msg.productOrder.payStatus
					+ "&businessType=1"
					+ "&orderType=6"
					+ "&agentId=" + agentinfoId
					+ "&paramCurrencyId=" + currencyClearId
					+ "&paramCurrencyPrice=" + currencyClearPrice
					+ "&paramTotalCurrencyId=" + currencyClearId
					+ "&paramTotalCurrencyPrice=" + currencyClearPrice
					+ "&orderDetailUrl="+g_context_url+"/visa/order/goUpdateVisaOrderForSales?visaOrderId="+msg.productOrder.id+"%26details=1";// %26转义代替"&"
					top.$.jBox.tip('保存成功', 'success');
					//window.location.href =g_context_url+ "/orderPay/pay?"+ param;		
					window.location.href =g_context_url+ "/visa/preorder/pay?"+ param;	
				}
			}
		});
	});
	
	

	$(".closeOrExpand").click(function() {
		var obj_this = $(this);
		if (obj_this.attr("class").match("ydClose")) {
			obj_this.removeClass("ydClose");
			obj_this.parent().next("[flag=messageDiv]").eq(0).show();
		}
	});
});

function doPay(){
	var orderId = $("#orderid").val();
	var payPriceType = $("#payMode").val();
	var price = $("#travelerSumPrice").text().replace(/\,/g,'');
	var agentId = $("#agentId").val();
	var orderPersionNum = $("#orderPersonelNum").val();
	document.location = g_context_url + "/orderPay/pay?orderId="+orderId 
	             + "&payPriceType=" + payPriceType + "&orderType=7&price=" 
	             + price + "&agentId=" + agentId + "&orderNum=" + orderPersionNum;
}

function saveAllSecondInfo(){
	var forms = $("#traveler form");
	var travelerArray = new Array();
	$.each(forms, function(key, value) {
		travelerArray.push(getTravelerFormData(value));
	});
	var specialRemark = $("form.contactTable textarea").val();
	var orderid =$("#orderid").val();
	var dataObj = {"travelerJSON" : JSON.stringify(travelerArray), "specialRemark" : specialRemark, "orderId" : orderid};
	
	$.ajax({
		  type: "POST",
		  url: g_context_url + "/order/airticket/secondStepSave",
		  dataType: "json",
		  data: dataObj,
		  async: false,
		  success:function(msg){
			  
		  }
		});
	
}

function getTravelerFormData(form){
	var personType = $("input[name^='personType']:checked", form).val();
	var travelerName = $("input[name='travelerName']", form).val();
	var travelerPinyin = $("input[name='travelerPinyin']", form).val();
	var travelerSex = $("select[name='travelerSex']", form).val();
	var passportType = $("select[name='passportType']", form).val();
	var nationality = $("select[name='nationality']", form).val();
	var birthDay = $("input[name='birthDay']", form).val();
	var telephone = $("input[name='telephone']", form).val();
	var passportCode = $("input[name='passportCode']", form).val();
	var issuePlace = $("input[name='issuePlace']", form).val();
	var passportValidity = $("input[name='passportValidity']", form).val();
	var remark = $("textarea[name='remark']", form).val();
	var orderid =$("#orderid").val();
	var travelerId = $("input[name='id']", form).val();
	var idCard = $("input[name='idCard']", form).val();
	var srcPrice = $("input[name='payPrice']", form).val();
	var passportStatus = 0;
	
	var obj = { 
			personType : personType,
			name : travelerName,
			nameSpell : travelerPinyin,
			sex : travelerSex,
			passportType:passportType,
			nationality : nationality,
			birthDay : birthDay,
			telephone : telephone,
			passportCode : passportCode,
			issuePlace : issuePlace,
			passportValidity : passportValidity,
			remark : remark,
			id:travelerId,
			idCard : idCard,
			orderId : orderid,
			srcPrice : srcPrice,
			payPrice : srcPrice,
			passportStatus : passportStatus
	}
	
	return obj;
}

//保存游客及其  签证  附件信息 
function saveTravelerInfo(obj){
	var blresult = false;
	var activityId = $("#productId").val();//产品ID
	var payType = $("#payType").val();//付款方式
	var orderid =$("#orderid").val();//订单ID
	var specialremark  = $("#specialremark").val();
	
	var form = $(obj).closest("form");
	
	var passportdocID = $("input[name='passportdocID']", form).val();
	var idcardfrontdocID = $("input[name='idcardfrontdocID']", form).val();
	var entry_formdocID = $("input[name='entry_formdocID']", form).val();
	var photodocID = $("input[name='photodocID']", form).val();
	var idcardbackdocID = $("input[name='idcardbackdocID']", form).val();
	var otherdocID = $("input[name='otherdocID']", form).val();
	var familyRegisterdocID = $("input[name='familyRegisterdocID']", form).val();//户口本ID
	var houseEvidencedocID = $("input[name='houseEvidencedocID']", form).val();//房产证ID
	
	var forecastStartOut = $("input[name='forecastStartOut']", form).val();//预计出团时间
	var forecastContract = $("input[name='forecastContract']", form).val();//预计签约时间
	var remark = $("textarea[name='remark']", form).val(); //备注

	
	var obj = getTravelerFormData(form);//游客基本信息

	//var dataObj = {"travelerJSON" : JSON.stringify([obj])};
	
	   //获取游客费用
	   var costtr = $('.cost',$(form));
	   var datacost = new Array();
	   $.each(costtr, function(keycost, valuecost) {
	       var costs = {};
	       var costinputs = $(valuecost).find("input");
	       $.each(costinputs, function(keyin, valuein) {
	           costs[$(valuein).attr("name")] = $(valuein).val();
	       });
	       var costselects = $(valuecost).find("select");
	       $.each(costselects, function(keyin, valuein) {
	           costs[$(valuein).attr("name")] = $(valuein).val();
	       });
	       datacost.push(costs);
	   });
	   
	   //获取游客成本价
/*	   var costtr = $('.cost',$(form));
	   var datacost = new Array();
	   $.each(costtr, function(keycost, valuecost) {
	       var costs = {};
	       var costinputs = $(valuecost).find("input");
	       $.each(costinputs, function(keyin, valuein) {
	           costs[$(valuein).attr("name")] = $(valuein).val();
	       });
	       var costselects = $(valuecost).find("select");
	       $.each(costselects, function(keyin, valuein) {
	           costs[$(valuein).attr("name")] = $(valuein).val();
	       });
	       datacost.push(costs);
	   });*/
	
	// wxw added 添加附件信息 以及 签证附属信息
	var tmpData = {"activityId" : activityId, 
	        "travelerJSON" :  JSON.stringify([obj]), 
	        "orderid" : orderid, 
	        "specialremark" : specialremark, 
	        "passportdocID" : passportdocID, 
	        "idcardfrontdocID" : idcardfrontdocID, 
	        "entry_formdocID" : entry_formdocID, 
	        "photodocID" : photodocID,
	        "idcardbackdocID" : idcardbackdocID,
	        "otherdocID" : otherdocID,
	        "familyRegisterdocID" : familyRegisterdocID,
	        "houseEvidencedocID" : houseEvidencedocID,
	        "forecastStartOut" : forecastStartOut,
	        "forecastContract" : forecastContract,
	        "remark" : remark,
	        "costs" : JSON.stringify(datacost),
	        "costPrice":JSON.stringify(getTravelerPayPrice($(form))),//成本价
	        "payPrice": JSON.stringify(getTravelerClearPrice($(form))),	//结算价
	        "payType":payType
	        
	        };
	
	$.ajax({
		  type: "POST",
		  url: g_context_url + "/visa/preorder/addTravelers",
		  dataType: "json",
		 // data: dataObj,
		  data: tmpData,
		  async: false,
		  success:function(msg){
			  if(msg && msg.travelerId){
				  blresult = true;
				  $("input[name='id']", form).val(msg.travelerId);
			  }
		  }
		});
	
	return blresult;
}


/**
 * 获取游客的结算价格   --wxw  added----------
 * @param traveler 游客对象
 * @returns {Array} 结算价数组
 */
function getTravelerClearPrice(traveler){
	var travelerPrice = [];
	$("div[name=inputClearPriceDiv]",traveler).each(function(index,element){
		var priceObj = new Object();
		var inputArr = $(element).find("input");
		$(inputArr).each(function(index,element){
			if($(element).attr("name") == "inputCurreyId"){
				priceObj.currencyId = $(element).val();
				priceObj.currencyName = $(element).attr("alt");
			}
			if($(element).attr("name") == "inputClearPrice"){
				priceObj.price = $(element).val();
			}
		});
		travelerPrice.push(priceObj);
	});
	return travelerPrice;
}


/**
 * 获取游客的成本价  --wxw  added----------
 * @param traveler 游客对象
 * @returns {Array} 结算价数组
 */
function getTravelerPayPrice(traveler){
	var travelerPrice = [];
	var travelerPayPrice = new CurrencyMoney(currencyList); 
	var priceObj = new Object();
	//获取其他费用
	var otherCostObj = $(".payfor-otherDiv .payfor-other",traveler);
	$.each(otherCostObj, function(key, value) {
		travelerPayPrice[$(value).find("select[name='currency']").val()] += Number($(value).find("input[name='sum']").val());
	});
	
	//获取游客单价
	var src = traveler.find("input[name='srcPrice']").val();
	var srcPriceCurrency = traveler.find("input[name='srcPriceCurrency']").val();
	travelerPayPrice[srcPriceCurrency] += Number(src);
	
	//获取游客联运价格
/*	var intermodalType = $("input[name=travelerIntermodalType]:checked",traveler).val();
    if(intermodalType == "1"){
    	var intermodalCurrencyId = $("select[name=intermodalStrategy]",traveler).find("option:selected").attr("priceCurrencyId");
    	var intermodalPrice = $("select[name=intermodalStrategy]",traveler).find("option:selected").val();
    	priceObj = new Object();
		priceObj.currencyId = intermodalCurrencyId;
		priceObj.price = intermodalPrice;
		travelerPrice.push(priceObj);
    }*/
	    $.each(travelerPayPrice, function(key, value) {
    	if(value > 0){
	    	priceObj = new Object();
			priceObj.currencyId = key;
			priceObj.price = value;
			travelerPrice.push(priceObj);
	    }
    });
    return travelerPrice;
}





//保存基本信息
function saveOrderBasicInfo(){
	var blresult = false;
	var productId = $("#productId").val();
	var payMode = $("#payMode").val();
	var orderid =$("#orderid").val();
	var orderPersonNumAdult = $("#orderPersonNumAdult").val();
	var orderPersionNum = $("#orderPersonelNum").val();
	var agentId = $("#agentId").val();
	var totalPrice = $("#travelerSumPrice").text().replace(/\,/g,'');
	var placeHolderType = $("#placeHolderType").val();
	var payType = $("#payType").val();
	//添加非签约渠道联系人
	var agentinfoName = $("#agentinfoNameId").val();
	
	// 联系人
	var contacttables = $("#ordercontact .ydbz_qd");
	var datacontacts = new Array();
	$.each(contacttables, function(key, valueout) {
		var contactinputs = $(valueout).find("input");
		var datacontact = {};
		$.each(contactinputs, function(key, value) {
			var _nametemp = $(value).attr("name");
			datacontact[_nametemp] = $(value).val();
		});
		datacontacts.push(datacontact);
	});
	
	
	var tmpData = {"productId" : productId, 
			        "payMode" : payMode, 
			        "orderid" : orderid, 
			        "orderPersonNumAdult" : orderPersonNumAdult, 
			        "orderPersionNum" : orderPersionNum,
			        "orderContactsJSON" : JSON.stringify(datacontacts),
			        "agentId" : agentId,
			        "totalPrice" : totalPrice,
			        "placeHolderType" : placeHolderType,
			        "payType" : payType,
			        "agentinfoName":agentinfoName
			        };
	
	$.ajax({
		  type: "POST",
		  url: g_context_url + "/visa/preorder/saveVisaOrder",
		  dataType: "json",
		  data: tmpData,
		  async: false,
		  success:function(msg){
			  if(msg && msg.orderid){
				  blresult = true;
				  $("#orderid").val(msg.orderid);
				  $("#agentId").val(msg.agentId);
				  $("#payStatus").val(msg.visaOrderStatus);
			  }
		  }
		});
	
	return blresult;
}


/*function changePayPriceByCostChange($this) {
	var totalcost = 0;
	var costall = $this.parent().parent().parent().eq(0).find(
			"input[name='sum']");
	$.each(costall, function(key, value) {
		totalcost = totalcost + Number($(value).val());
	});
	var travler = $this.closest(".travelerTable");
	var src = travler.find("input[name='srcPrice']").val();
	var singleDiff = travler.find("input[name='singleDiff']").val();

	if (Number(singleDiff) + Number(src) + totalcost <= 0) {
		top.$.jBox.tip('游客结算价不可小于0', 'error');
	}

	travler.find("input[name='payPrice']").val(
			Number(singleDiff) + Number(src) + totalcost);
	travler.find("input[name='payPrice']").parent().find(".ydFont2").text(
			((Number(singleDiff) + Number(src) + totalcost)).toString()
					.formatNumberMoney('#,###'));
	changeTotalPrice();
}*/

/*
 function changeTotalPrice() {
	recountIndexTraveler();
	var allPersonNum = $("#orderPersonelNum").val();
	if (allPersonNum == "" || allPersonNum == undefined) {
		allPersonNum = 0;
	}
	// 计算总人数
	var travelerTables = $("#traveler form.travelerTable");

	var payStatus = $("#payStatus").val();
	if (travelerTables.length == allPersonNum) {
		$("#addTraveler").parent().hide();
	} else {
		$("#addTraveler").parent().show();
	}

	if (travelerTables.length > allPersonNum) {
		$("#orderPersonelNum").val(travelerTables.length);
	}

	// 所有数据相加
	var topPrice = 0;
	var prices = $("#traveler input[name='payPrice']");
	$.each(prices, function(key, value) {
		var tempnum = $(value).val();
		if (tempnum != "") {
			topPrice += Number(tempnum);
		}
	});
	// 总共需要计算的人数
	var len = prices.length;// 有名单的人数
	var texamt = Number($("#texamt").val());
	var etj = Number($("#etj").val()) + texamt;
	var crj = Number($("#crj").val()) + texamt;
	var tsj = Number($("#tsj").val()) + texamt;

	// 预订人数
	var adultNum = $("#orderPersonNumAdult").val();
	var childNum = $("#orderPersonNumChild").val();
	var specialNum = $("#orderPersonNumSpecial").val();
	// 实际人数
	if (Number(allPersonNum) - Number(len) > 0) {
		// 总人数
		topPrice += Number(Math.abs(adultNum - countAdult())) * crj
				+ Number(Math.abs(childNum - countChild())) * etj
				+ Number(Math.abs(specialNum - countSpecial())) * tsj;
	}
	$("#travelerSumPrice").text(topPrice.toString().formatNumberMoney('#,###'));
	return topPrice;
}*/

function dodatePicker() {
	var birthdays = $("#traveler input[name='birthDay']");
	var validityDates = $("#traveler input[name='validityDate']");

	$.each(birthdays, function(key, value) {
		$(value).datepicker( {
			maxDate : new Date()
		});
	});
	$.each(validityDates, function(key, value) {
		$(value).datepicker();
	});
	
	var issuePlaces = $("#traveler input[name='issuePlace']");
	$.each(issuePlaces, function(key, value) {
		$(value).datepicker();
	});
	
	var passportValiditys = $("#traveler input[name='passportValidity']");
	$.each(passportValiditys, function(key, value) {
		$(value).datepicker( {
			minDate : new Date()
		});
	});
	changeTotalPrice();
}

//防止多次提交
var submit_times = 0;
function dosave(isnext, type) {
//	
//	var blresult = false;
//	// 现在预订人数
//	var orderPersionNum = $("#orderPersonelNum").val();
//	// 余位数
//	var freePosition = $("#freePosition").val();
//	
//	if (Number(orderPersionNum) <= 0) {
//		top.$.jBox.tip('订单至少有一个游客!', 'error');
//		return false;
//	}
//
//	var activityId = $("#productId").val();
//	var payMode = $("#payMode").val();
//	var orderPersonNumChild = $("#orderPersonNumChild").val();
//	var orderPersonNumAdult = $("#orderPersonNumAdult").val();
//	var orderPersonNumSpecial = $("#orderPersonNumSpecial").val();
//	var agentId = $("#agentId").val();
//	var totalPrice = $("#travelerSumPrice").text().replace(/\,/g,'');
//	
//	
//	// 联系人
//	var contacttables = $("#ordercontact ul");
//	var datacontacts = new Array();
//	$.each(contacttables, function(key, valueout) {
//
//		var contactinputs = $(valueout).find("input");
//		var datacontact = {};
//		$.each(contactinputs, function(key, value) {
//			var _nametemp = $(value).attr("name");
//			datacontact[_nametemp] = $(value).val();
//		});
//
//		datacontacts.push(datacontact);
//	});
//	
//	
//	// 游客信息
//	var datatraveler = new Array();
//	var travelerTables = $("#traveler form");
//
//	$.each(travelerTables, function(key, form) {
//		var personType = $("input[name^='personType']:checked", form).val();
//		var travelerName = $("input[name='travelerName']", form).val();
//		var travelerPinyin = $("input[name='travelerPinyin']", form).val();
//		var travelerSex = $("select[name='travelerSex']", form).val();
//		var nationality = $("select[name='nationality']", form).val();
//		var birthDay = $("input[name='birthDay']", form).val();
//		var telephone = $("input[name='telephone']", form).val();
//		var passportCode = $("input[name='passportCode']", form).val();
//		var validityDate = $("input[name='validityDate']", form).val();
//		var remark = $("textarea[name='remark']", form).val();
//		var travelerId = $("input[name='id']", form).val();
//		
//		datatraveler.push({ 
//					personType : personType,
//					travelerName : travelerName,
//					travelerPinyin : travelerPinyin,
//					travelerSex : travelerSex,
//					nationality : nationality,
//					birthDay : birthDay,
//					telephone : telephone,
//					passportCode : passportCode,
//					validityDate : validityDate,
//					remark : remark,
//					travelerId:travelerId
//			});
//		
//	});
//	
//	
//	var tmpData = {"activityId" : activityId, 
//			        "payMode" : payMode, 
//			        "orderPersonNumChild" : orderPersonNumChild, 
//			        "orderPersonNumAdult" : orderPersonNumAdult, 
//			        "orderPersonNumSpecial" : orderPersonNumSpecial, 
//			        "orderPersionNum" : orderPersionNum,
//			        "orderContactsJSON" : JSON.stringify(datacontacts),
//			        "travelerJSON" : JSON.stringigy(datatraveler),
//			        "agentId" : agentId,
//			        "totalPrice" : totalPrice
//			        };
//	
//	$.ajax({
//		  type: "POST",
//		  url: g_context_url + "/order/airticket/saveAirticketOrder",
//		  dataType: "json",
//		  data: tmpData,
//		  async: false,
//		  success:function(msg){
//			  if(msg && msg.orderId){
//				  blresult = true;
//			  }
//		  }
//		});
//	
//	return blresult;
}

//  验证渠道联系人及订单联系人
function _doValidateorderpersonMesdtail() {
	var flag = true;
	var pot = $("#orderpersonMesdtail").validate( {
		showErrors : function(errorMap, errorList) {
			this.defaultShowErrors();
			outerrorList = outerrorList.concat(errorList);
		}
	}).form();
	if (!pot) {
		flag = false;
	}
	return outerrorList;
}
// 填写预订人信息 验证
function _doValidateproductOrderTotal() {
	var flag = true;
	var pot = $("#productOrderTotal").validate( {
		showErrors : function(errorMap, errorList) {
			this.defaultShowErrors();
			outerrorList = outerrorList.concat(errorList);
		}
	}).form();
	if (!pot) {
		flag = false;
	}
	return outerrorList;
}

function _doValidatecontactForm() {
	var forms = $("#contact form");
	$.each(forms, function(key, value) {
		var tempFlag = $(value).validate( {
			showErrors : function(errorMap, errorList) {
				this.defaultShowErrors();
				outerrorList = outerrorList.concat(errorList);
			}
		}).form();
		if (!tempFlag) {
			flag = false;
		}
	});
	return outerrorList;
}

function _doValidatetravelerForm() {
	var forms = $("#traveler form");
	$.each(forms, function(key, value) {
		var tempFlag = $(value).validate( {
			showErrors : function(errorMap, errorList) {
				this.defaultShowErrors();
				outerrorList = adderrorList(outerrorList, errorList);
			}
		}).form();
		if (!tempFlag) {
			flag = false;
		}
	});
	return outerrorList;
}

function validate() {
	var flag = true;
	var forms = $("#contact form");

	var pot = $("#productOrderTotal").validate( {}).form();
	if (!pot) {
		flag = false;
	}

	$.each(forms, function(key, value) {
		var tempFlag = $(value).validate( {
			showErrors : function(errorMap, errorList) {
				this.defaultShowErrors();
				outerrorList = outerrorList.concat(errorList);
			}
		}).form();
		if (!tempFlag) {
			flag = false;
		}
	});
	forms = $("#traveler form");
	$.each(forms, function(key, value) {
		var tempFlag = $(value).validate( {
			showErrors : function(errorMap, errorList) {
				this.defaultShowErrors();
				outerrorList = adderrorList(outerrorList, errorList);
			}
		}).form();
		if (!tempFlag) {
			flag = false;
		}
	});

	if (isdoSave) {
		createDiv(outerrorList);
	}

	return flag;
}
/**
 * 重新计算游客编号
 * @return
 */
function recountIndexTraveler() {
	
	var travelerTables = $("#traveler form");
	if (travelerTables.length <= 0) {
		$(".warningtravelerNum").text("暂无游客信息");
	} else {
		$(".warningtravelerNum").text("");
	}
	travelerTotalPriceArr =[];
	travelerTotalClearPriceArr=[];
	$.each(travelerTables, function(key, value) {
		var index = key + 1;
		$(value).find(".travelerIndex").text(index);
		
		// 2016年3月25日21:12:16  add by shangpengfei 需求 295
		if($("input[name='travelerName']").attr('loginName')=='7a81b21a77a811e5bc1e000c29cf2586'){
			$(value).find("input[name='travelerName']").val("游客"+index);
		}
		
		var selFlag = false;
		var selJsPrice = 0;
		var selSrcPriceCurrency;
		var selSrcPriceCurrencyMark;
		var selSrcPriceCurrencyName;
		var personType = 0;
		//if ($("#orderPersonNumAdult").val() >= countAdult()){
			selFlag = true;
			selJsPrice = $('#crj').val();
			selSrcPriceCurrency = $('#crbz').val();
			selSrcPriceCurrencyMark  = $('#crbzm').val();
			selSrcPriceCurrencyName = $('#crbmc').val();
			personType = 0;
		//}
		
		var priceObj = new Object();
		priceObj.currencyId = selSrcPriceCurrency;
		priceObj.price =  selJsPrice;
		var priceObjArr = new Array();
		priceObjArr.push(priceObj);
		var travelerJsPrice = new Object();
		travelerJsPrice.jsPrice = priceObjArr;
		travelerTotalPriceArr.push(travelerJsPrice);
		//结算价
		var travelerClearPrice = new Object();
		travelerClearPrice.travelerClearPrice = priceObjArr;
		travelerTotalClearPriceArr.push(travelerClearPrice);
		
		//changeClearPriceByInputChange(value);
		
	});
}

function checkFreePosition() {
	
	// 现在预订人数
	var orderPersonelNum = $("#orderPersonelNum").val();

	var freePosition = $("#freePosition").val();
	var leftpayReservePosition = $("#leftpayReservePosition").val();
	var placeHolderType = $("#placeHolderType").val();
	if (placeHolderType == 1) {
		// 表示为切位 用切位数去判断余位
		freePosition = leftpayReservePosition;
	}

	// 余位数
	// 现在订单人数
	var orderPosition = $("#orderPosition").val();

	if (orderPosition == "" || orderPosition == undefined) {
		orderPosition = 0;
	}

	if (Number(orderPosition) <= 0 && Number(orderPersonelNum) <= 0) {
		top.$.jBox.tip('出行人数必须大于零', 'error');
		return false;
	}

	//不涉及余位 校验的问题
	//if ((Number(freePosition) + Number(orderPosition) - Number(orderPersonelNum)) < 0) {
	//	top.$.jBox.tip('余位数不足', 'error');
		//return false;
	//}
	return true;
}

function createDivInTable(errorList) {
	if ($("#showErrorDiv")) {
		$("#showErrorDiv").remove();
	}
	if (errorList.length <= 0) {
		return;
	}
	var div = $("<div id='showErrorDiv' class='show_m_div'></div>");
	var _closeSpan = $("<span title=\"关闭\" class=\"show_m_div_close\">&times;</span>")
	div.append($("<div class=\"show_m_div_title\">提示信息</div>").append(
			_closeSpan));
	_closeSpan.click(function() {
		$("#showErrorDiv").remove();
	});
	var _ul = $("<ul></ul>");
	_ul.appendTo(div);
	$.each(errorList, function(keyin, valuein) {
		var textTemp = $(valuein.element).parent().text()
				.replace(/[\:\：]/g, '');
		if (!$.trim(textTemp)) {
			textTemp = $(valuein.element).prev().text().replace(/[\:\：]/g, '');
		}
		if (!$.trim(textTemp)) {
			if ($(valuein.element).attr("name") == "sum") {
				textTemp = "金额";
			} else if ($(valuein.element).attr("name") == "name") {
				textTemp = "费用变更";
			}
		}
		textTemp = $.trim(textTemp) + "为";
		var modifyButton = $("<input type='button' value='修改'/>");
		modifyButton.click(function(element) {
			return function() {
				$(element).focus()
			};
		}(valuein.element));
		textTemp = textTemp.replace(valuein.message, "");
		textTemp = textTemp.replace(/人\s人/g, "人");
		textTemp = textTemp.replace(/儿童\s人/g, "儿童");
		_ul.append($("<li></li>").append(
				$("<em>" + textTemp + valuein.message + "</em>")).append(
				modifyButton));
	});
	div.appendTo(document.body);
	// letDivCenter(div[0]);
	isdoSave = false;
}

function createDivInDiv(errorList) {
	if ($("#showErrorDiv")) {
		$("#showErrorDiv").remove();
	}
	if (errorList.length <= 0) {
		return;
	}
	var div = $("<div id='showErrorDiv' class='show_m_div'></div>");
	var _closeSpan = $("<span title=\"关闭\" class=\"show_m_div_close\">&times;</span>")
	div.append($("<div class=\"show_m_div_title\">提示信息</div>").append(
			_closeSpan));
	_closeSpan.click(function() {
		$("#showErrorDiv").remove();
	});
	var _ul = $("<ul></ul>");
	_ul.appendTo(div);
	$.each(errorList, function(keyin, valuein) {
		var textTemp = $(valuein.element).parent().find("span").eq(0).text()
				.replace(/[\:\：\*]/g, '');
		if (!$.trim(textTemp)) {
			textTemp = $(valuein.element).prev().text()
					.replace(/[\:\：\*]/g, '');
		}
		if (!$.trim(textTemp)) {
			if ($(valuein.element).attr("name") == "sum") {
				textTemp = "金额";
			} else if ($(valuein.element).attr("name") == "name") {
				textTemp = "费用变更";
			}
		}
		textTemp = $.trim(textTemp) + "为";
		var modifyButton = $("<input type='button' value='修改'/>");
		modifyButton.click(function(element) {
			return function() {
				$(element).focus()
			};
		}(valuein.element));
		_ul.append($("<li></li>").append(
				$("<em>" + textTemp + valuein.message + "</em>")).append(
				modifyButton));
	});
	div.appendTo(document.body);
	// letDivCenter(div[0]);
	isdoSave = false;
}

function letDivCenter(divName) {
	var top = ($(window).height() - $(divName).height());
	var left = ($(window).width() - $(divName).width());
	var left = 0;
	// var scrollTop = $(document).scrollTop();
	// var scrollLeft = $(document).scrollLeft();
	var scrollTop = 0;
	var scrollLeft = 0;
	$(divName).css( {
		'top' : top + scrollTop,
		'left' : left + scrollLeft
	}).show();
}

function adderrorList(outerrorList, errorList) {
	outerrorList = outerrorList.concat(errorList);
	return outerrorList;
}

/*
function changeorderPersonelNum() {
	$("#orderPersonelNum").val(
			Number($("#orderPersonNumChild").val())
					+ Number(($("#orderPersonNumAdult").val() == "" ? 0 : $(
							"#orderPersonNumAdult").val()))
					+ Number(($("#orderPersonNumSpecial").val() == "" ? 0 : $(
					"#orderPersonNumSpecial").val()))).blur();
}


function countAdult() {
	var radios = $("#traveler input[type='radio'][value=1]:checked");
	return radios.length;
}
function countChild() {
	var radios = $("#traveler input[type='radio'][value=2]:checked");
	return radios.length;
}
function countSpecial() {
	var radios = $("#traveler input[type='radio'][value=3]:checked");
	return radios.length;
}
*/


//预定保存游客信息
function SavePeopleTableData(obj){
	
	//游客姓名
	$(obj).parent().parent().find("input[name='travelerName']").each(function(index, obj) {
		if($(this).val() == "") {
			var name = $(this).parent().children("label").text();
			top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
            $(this).focus();
            throw "error！"; 
		}
	});
	//护照号
/*	$(obj).parent().parent().find("input[name='passportCode']").each(function(index, obj) {
		if($(this).val() == "") {
			var name = $(this).parent().children("label").text();
			top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
            $(this).focus();
            throw "error！"; 
		}
	});*/
	
	//护照签发日期
/*	$(obj).parent().parent().find("input[name='issuePlace']").each(function(index, obj) {
		if($(this).val() == "") {
			var name = $(this).parent().children("label").text();
			top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
			$(this).focus();
			throw "error！"; 
		}
	});*/
	
	//护照有效期至
/*	$(obj).parent().parent().find("input[name='passportValidity']").each(function(index, obj) {
		if($(this).val() == "") {
			var name = $(this).parent().children("label").text();
			top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
			$(this).focus();
			throw "error！"; 
		}
	});*/
	
	//结算价校验
	var treveler = $(obj).closest("form");
	var travelerPrice = getTravelerClearPrice(treveler);	//获取游客的结算价
	
	for(var i= 0; i < travelerPrice.length; i++){
		var travelerPriceObj = travelerPrice[i];	
		if(""==travelerPriceObj.price){
			top.$.jBox.tip('游客的结算价'+travelerPriceObj.currencyName+'不可小于0', 'error');
			return;
		}
	}
	
	var totalPrice = getOrderToltalClearPrice(travelerPrice, 1);//各种币种相加的结算结果
	if (totalPrice == '') {
		top.$.jBox.tip('游客结算价不可小于0', 'error');
		return;
	}
	
		
	
	if($(obj).text()=="保存"){
		//保存数据
		saveTravelerInfo(obj);
	}
	
	
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
	if($(obj).text()=="保存"){
		    $(obj).text("修改");
			$(obj).parent().prev().hide();
			$(obj).parent().parent().find('.tourist-t-off').css("display","inline");
			$(obj).parent().parent().find('.tourist-t-on').hide();
			$(obj).parent().parent().find('.add_seachcheck').addClass('boxCloseOnAdd');
			$(obj).parent().parent().find(".btn-addBlue").hide();
		}else{
			$(obj).text("保存");
			$(obj).parent().prev().show();
			$(obj).parent().parent().find('.tourist-t-off').css("display","none");
			$(obj).parent().parent().find('.tourist-t-on').show();
			$(obj).parent().parent().find('.add_seachcheck').removeClass('boxCloseOnAdd');
			$(obj).parent().parent().find(".btn-addBlue").show();
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
function getPassportValidity(){
	$("#traveler").delegate("input[name='issuePlace']", "blur", function() {
		// 发证日期
		var _$validityDate = $(this).closest(".tourist-info1").find("input[name='passportValidity']");
		var minDate = $(this).val();
		var curDate = new Date();
		
		//护照有效期默认为发证日期加10年，然后减一年
		if(minDate != "") {
			var lastDate = new Date(new Date(minDate.replace(/-/g,'/')));
			lastDate.setFullYear(lastDate.getFullYear() + 10);
			lastDate.setDate(lastDate.getDate() - 1);
			$(_$validityDate).val(lastDate.getFullYear() + "-" + (lastDate.getMonth() + 1) + "-" + lastDate.getDate());
		}
		
		if (new Date(minDate) < curDate) {
			minDate = curDate;
		}

		_$validityDate.unbind();
		_$validityDate.datepicker( {
			minDate : minDate
		});
	});
	$("#traveler").delegate("input[name='passportValidity']", "blur", function() {
		// 发证日期
		var _$issuePlace = $(this).closest(".tourist-info1").find("input[name='issuePlace']");
		var maxDate = $(this).val();
		_$issuePlace.unbind();
		_$issuePlace.datepicker( {
			maxDate : maxDate
		});
	});
}

function updateAgent() {
	var orderId = $("[name='orderId']").val();
	var agentId = $("#agentInfoId").val();
	var contacInfoArr = $(".ydbz_qd").find("li[class!='ydbz_qd_close']");
	var dataArr = new Array();
	for(var i = 0; i < contacInfoArr.length; i++) {
		dataArr.push($(contacInfoArr[i]).find("span").text());
	}
	
	$.ajax({
		   type: "POST",
		   url: g_context_url + "/visa/order/updateOrderAgent",
		   traditional: true,
		   data:{
			   orderId:orderId,
			   agentId:agentId,
			   dataArr:dataArr
		   },
		   success: function(msg){
			   $.jBox.tip("保存成功！", "success");
		   }
	});
}

function getAgentinfo() {
	var agentId = $("#agentInfoId").val();
	$.ajax({
		   type: "GET",
		   url: g_context_url + "/agent/manager/getAgentinfo",
		   data:{
			   agentId:agentId
		   },
		   success: function(msg){
			   var infoProArr = [msg.agentContact, msg.agentContactMobile, msg.agentContactTel, msg.agentContactAddress, 
			                     msg.agentContactFax, msg.agentContactQQ, msg.agentContactEmail]; 
			   var contacInfoArr = $(".ydbz_qd").find("li[class!='ydbz_qd_close']");
			   for(var i = 0; i < infoProArr.length; i++) {
				   $(contacInfoArr[i]).find("span").text(null == infoProArr[i] ? "" : infoProArr[i]);
			   }
		   }
		});
}

function updateGroupRebates() {
	var orderId = $("[name='orderId']").val();
	var groupRebatesCurrency = $("#groupRebatesCurrency").val();
	var groupRebatesMoney = $("#groupRebatesMoney").val();
	
	$.ajax({
			type: "POST",
			url: g_context_url + "/visa/order/updateGroupRebates",
			traditional: true,
			data:{
				"orderId":orderId,
				"groupRebatesCurrency":groupRebatesCurrency,
				"groupRebatesMoney":groupRebatesMoney
			},
			success: function(msg){
				$.jBox.tip("预计团队返佣保存成功！", "success");
			}
	});
}


//------------需求C333 开始-------------
//预定保存游客信息
function SavePeopleTableData4AddTraveler(obj){
	debugger;
	//游客姓名
	$(obj).parent().parent().find("input[name='travelerName']").each(function(index, obj) {
		if($(this).val() == "") {
			var name = $(this).parent().children("label").text();
			top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
            $(this).focus();
            throw "error！"; 
		}
	});
	//护照号
/*	$(obj).parent().parent().find("input[name='passportCode']").each(function(index, obj) {
		if($(this).val() == "") {
			var name = $(this).parent().children("label").text();
			top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
            $(this).focus();
            throw "error！"; 
		}
	});*/
	
	//护照签发日期
/*	$(obj).parent().parent().find("input[name='issuePlace']").each(function(index, obj) {
		if($(this).val() == "") {
			var name = $(this).parent().children("label").text();
			top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
			$(this).focus();
			throw "error！"; 
		}
	});*/
	
	//护照有效期至
/*	$(obj).parent().parent().find("input[name='passportValidity']").each(function(index, obj) {
		if($(this).val() == "") {
			var name = $(this).parent().children("label").text();
			top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
			$(this).focus();
			throw "error！"; 
		}
	});*/
	
	//结算价校验
	var treveler = $(obj).closest("form");
	var travelerPrice = getTravelerClearPrice(treveler);	//获取游客的结算价
	
	for(var i= 0; i < travelerPrice.length; i++){
		var travelerPriceObj = travelerPrice[i];	
		if(""==travelerPriceObj.price){
			top.$.jBox.tip('游客的结算价'+travelerPriceObj.currencyName+'不可小于0', 'error');
			return;
		}
	}
	
	var totalPrice = getOrderToltalClearPrice(travelerPrice, 1);//各种币种相加的结算结果
	if (totalPrice == '') {
		top.$.jBox.tip('游客结算价不可小于0', 'error');
		return;
	}
	
		
	
	if($(obj).text()=="保存"){
		//保存数据
		var resultFlag=saveTravelerInfo4AddTraveler(obj);//resultFlag为预计出团时间和预计回团时间的校验通过标识
		if("failForecastStartOut"==resultFlag){
			 $.jBox.tip("请填写预计出团时间", "error");
			 return false;
		}
		if("failForecastBackDate"==resultFlag){
			 $.jBox.tip("请填写预计回团时间", "error"); 
			 return false;
		}
	}
	
	
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
/*	if($(obj).text()=="保存"){
		    $(obj).text("修改");
			$(obj).parent().prev().hide();
			$(obj).parent().parent().find('.tourist-t-off').css("display","inline");
			$(obj).parent().parent().find('.tourist-t-on').hide();
			$(obj).parent().parent().find('.add_seachcheck').addClass('boxCloseOnAdd');
			$(obj).parent().parent().find(".btn-addBlue").hide();
		}else{
			$(obj).text("保存");
			$(obj).parent().prev().show();
			$(obj).parent().parent().find('.tourist-t-off').css("display","none");
			$(obj).parent().parent().find('.tourist-t-on').show();
			$(obj).parent().parent().find('.add_seachcheck').removeClass('boxCloseOnAdd');
			$(obj).parent().parent().find(".btn-addBlue").show();
			}*/
	
	//删除			
	var deleltecost=$(obj).parent().parent().find("a[name='deleltecost']");
			
	$(deleltecost).hide();
	
	//应用全部			
	var useall=$(obj).parent().parent().find(".yd-total a");
	if($(useall).css("display")=="none") {
			$(useall).show();
				}else{
			$(useall).hide();
				}
}



//保存游客及其  签证  附件信息 
function saveTravelerInfo4AddTraveler(obj){
	var blresult = false;
	var activityId = $("#productId").val();//产品ID
	var payType = $("#payType").val();//付款方式
	var orderid =$("#orderid").val();//订单ID
	var specialremark  = $("#specialremark").val();
	
	var form = $(obj).closest("form");
	var passportdocID = $("input[name='passportdocID']", form).val();
	var idcardfrontdocID = $("input[name='idcardfrontdocID']", form).val();
	var entry_formdocID = $("input[name='entry_formdocID']", form).val();
	var photodocID = $("input[name='photodocID']", form).val();
	var idcardbackdocID = $("input[name='idcardbackdocID']", form).val();
	var otherdocID = $("input[name='otherdocID']", form).val();
	var familyRegisterdocID = $("input[name='familyRegisterdocID']", form).val();//户口本ID
	var houseEvidencedocID = $("input[name='houseEvidencedocID']", form).val();//房产证ID
	//签证附件
	var visa_annexdocID = $("input[name='visa_annexdocID']", form).val();
	
	var forecastStartOut = $("input[name='forecastStartOut']", form).val();//预计出团时间
	var forecastContract = $("input[name='forecastContract']", form).val();//预计签约时间
	//---0211需求,新增预计回团时间,限定为批发商星徽四海-s--//
	var forecastBackDate=$("input[name='forecastBackDate']", form).val();//预计回团时间
	
	//-- 0615需求,新增是否需要押金,是否需要上传资料限定为批发商大洋国旅
	/*var depositValue=$(obj).parent().parent().find("input[name='depositValue']:checked").val();//是否需要押金
	var datumValue =$(obj).parent().parent().find("input[name='datumValue']:checked").val();//是否需要上传文件
	if(depositValue==undefined){
		depositValue=0;
	}
	if(datumValue==undefined){
		datumValue=0;
	}*/
	 //------ 0615需求,新增是否需要押金,是否需要上传资料限定为批发商大洋国旅
	
	
	//TODO 需要获得用户对应的批发商的uuid进行校验,若为星徽四海,则预计出团日期也是要进行必填校验的--当前未做-tgy
	  var companyUuid=$("input[name='companyUuid']", form).val();
	  if('0e19ac500f78483d8a9f4bb768608629'==companyUuid){ //若批发商为星徽四海
		  if(forecastStartOut==''){ //星徽四海的预计出团时间是必填项,所以不能为空
			  return "failForecastStartOut";//返回预计出团时间校验失败
		  }
		  if(forecastBackDate==''){
			  return "failForecastBackDate";//返回预计回团时间校验失败
		  }
	  }
	//---0211需求,新增预计回团时间,限定为批发商星徽四海-e--//
	var remark = $("textarea[name='remark']", form).val(); //备注

	
	var obj = getTravelerFormData4AddTraveler(form);//游客基本信息

	//var dataObj = {"travelerJSON" : JSON.stringify([obj])};
	
	   //获取游客费用
	   var costtr = $('.cost',$(form));
	   var datacost = new Array();
	   $.each(costtr, function(keycost, valuecost) {
	       var costs = {};
	       var costinputs = $(valuecost).find("input");
	       $.each(costinputs, function(keyin, valuein) {
	           costs[$(valuein).attr("name")] = $(valuein).val();
	       });
	       var costselects = $(valuecost).find("select");
	       $.each(costselects, function(keyin, valuein) {
	           costs[$(valuein).attr("name")] = $(valuein).val();
	       });
	       datacost.push(costs);
	   });
	   
	   //获取游客成本价
/*	   var costtr = $('.cost',$(form));
	   var datacost = new Array();
	   $.each(costtr, function(keycost, valuecost) {
	       var costs = {};
	       var costinputs = $(valuecost).find("input");
	       $.each(costinputs, function(keyin, valuein) {
	           costs[$(valuein).attr("name")] = $(valuein).val();
	       });
	       var costselects = $(valuecost).find("select");
	       $.each(costselects, function(keyin, valuein) {
	           costs[$(valuein).attr("name")] = $(valuein).val();
	       });
	       datacost.push(costs);
	   });*/
	
	// wxw added 添加附件信息 以及 签证附属信息
	var tmpData = {"activityId" : activityId, 
	        "travelerJSON" :  JSON.stringify([obj]), 
	        "orderid" : orderid, 
	        "specialremark" : specialremark, 
	        "passportdocID" : passportdocID, 
	        "idcardfrontdocID" : idcardfrontdocID, 
	        "entry_formdocID" : entry_formdocID, 
	        "photodocID" : photodocID,
	        "idcardbackdocID" : idcardbackdocID,
	        "otherdocID" : otherdocID,
	        "visa_annexdocID" : visa_annexdocID,
	        "familyRegisterdocID" : familyRegisterdocID,
	        "houseEvidencedocID" : houseEvidencedocID,
	        "forecastStartOut" : forecastStartOut,
	        "forecastContract" : forecastContract,
	        "forecastBackDate":forecastBackDate,//0211需求,新增预计回团时间,限定为批发商星徽四海
	        "remark" : remark,
	        "costs" : JSON.stringify(datacost),
	        "costPrice":JSON.stringify(getTravelerPayPrice($(form))),//成本价
	        "payPrice": JSON.stringify(getTravelerClearPrice4AddTraveler($(form))),	//结算价
	        "rebatesPayPrice": JSON.stringify(getTravelerRebatePrice4AddTraveler($(form))),	//返佣费用
	        "payType":payType
	        //"depositValue":depositValue,//是否需要押金
	        //"datumValue":datumValue//是否需要文件
	        };
	
	$.ajax({
		  type: "POST",
		  url: g_context_url + "/visa/order/addTravelers4update",
		  dataType: "json",
		 // data: dataObj,
		  data: tmpData,
		  async: false,
		  success:function(msg){
			  
			  if(msg && msg.travelerId){
				  blresult = true;
				  $("input[name='id']", form).val(msg.travelerId);
			  }
			  debugger;
			  window.location.href = g_context_url + "/visa/order/goUpdateVisaOrderForSales?visaOrderId=" + orderid;
			  
		  }
		});
	
	return blresult;
}



function getTravelerFormData4AddTraveler(form){
	var personType = $("input[name^='personType']:checked", form).val();
	var travelerName = $("input[name='travelerName']", form).val();
	var travelerPinyin = $("input[name='travelerPinyin']", form).val();
	var travelerSex = $("select[name='travelerSex']", form).val();
	var passportType = $("select[name='passportType']", form).val();
	var nationality = $("select[name='nationality']", form).val();
	var birthDay = $("input[name='birthDay']", form).val();
	var telephone = $("input[name='telephone']", form).val();
	var passportCode = $("input[name='passportCode']", form).val();
	var issuePlace = $("input[name='issuePlace']", form).val();
	var issuePlace1 = $("input[name='issuePlace1']", form).val();
	var passportValidity = $("input[name='passportValidity']", form).val();
	var remark = $("textarea[name='remark']", form).val();
	var orderid =$("#orderid").val();
	var travelerId = $("input[name='id']", form).val();
	var idCard = $("input[name='idCard']", form).val();
	var srcPrice = $("input[name='payPrice']", form).val();
	var passportStatus = 0;
	
	var obj = { 
			personType : personType,
			name : travelerName,
			nameSpell : travelerPinyin,
			sex : travelerSex,
			passportType:passportType,
			nationality : nationality,
			birthDay : birthDay,
			telephone : telephone,
			passportCode : passportCode,
			issuePlace : issuePlace,
			issuePlace1 : issuePlace1,
			passportValidity : passportValidity,
			remark : remark,
			id:travelerId,
			idCard : idCard,
			orderId : orderid,
			srcPrice : srcPrice,
			payPrice : srcPrice,
			passportStatus : passportStatus
	}
	
	return obj;
}

/**
 * 获取游客的结算价格   --wxw  added----------
 * @param traveler 游客对象
 * @returns {Array} 结算价数组
 */
function getTravelerRebatePrice4AddTraveler(traveler){


var cur = $("select[name='rebatesCurrency']", traveler).val();
var money = $("input[name='rebatesMoney']", traveler).val();

//var cur = $("input[name='rebatesCurrency']", traveler).val();
//var money = $("input[name='rebatesMoney']", traveler).val();
	return   cur+","+money;
}

/**
 * 获取游客的结算价格   --wxw  added----------
 * @param traveler 游客对象
 * @returns {Array} 结算价数组
 */
function getTravelerClearPrice4AddTraveler(traveler){
	var travelerPrice = [];
	$("div[name=inputClearPriceDiv]",traveler).each(function(index,element){
		var priceObj = new Object();
		var inputArr = $(element).find("input");
		$(inputArr).each(function(index,element){
			if($(element).attr("name") == "inputCurreyId"){
				priceObj.currencyId = $(element).val();
				priceObj.currencyName = $(element).attr("alt");
			}
			if($(element).attr("name") == "inputClearPrice"){
				priceObj.price = $(element).val();
			}
		});
		travelerPrice.push(priceObj);
	});
	return travelerPrice;
}

//-----计算页面的  结算价 和 成本价----

/**
 * Created by ding on 2015/11/24.
 */

//游客容器
var $travelers;
$(document).ready(function () {
    $travelers=$('#traveler');
});
//设置结算价
function setClearPrice(){
    var clearPrices=[];
    $travelers.find('[name="inputClearPriceDiv"]').each(function () {
        var currencyId = $(this).find('[name="inputCurreyId"]').val();
        var currency = currencyList.filter(function (item) {
            if(item.id == currencyId){
                return true;
            }
        })[0];
        var price =$(this).find('[name="inputClearPrice"]').val();
        clearPrices.push({
            currencyId:currencyId,
            currencyMark:currency.currencyMark,
            price:price
        });
    });
    var groupedClearPrices = groupPrice(clearPrices);
    var priceString='';
    for(var i =0;i<groupedClearPrices.length;i++){
        if(i==0){
            priceString += groupedClearPrices[i].currencyMark+fmoney(groupedClearPrices[i].price);
        }else{
            priceString += '+'+groupedClearPrices[i].currencyMark+fmoney(groupedClearPrices[i].price);
        }

    }
    $('#travelerSumClearPrice').text(priceString);
}

function setCostPrice(){
    var priceStrings =$('#costTotalMoney4addTraveler').val().split(' + ');
    var oldCosts =[];
    for(var i =0;i<priceStrings.length;i++){
        var price=rmoney(priceStrings[i]);
        var currencyMark = priceStrings[i].replace(/[\d\.\,\-]/g, "");
        oldCosts.push({
            price: price,
            currencyMark: currencyMark
        });
    }
    var unitPrice = rmoney($('#productCost4addTraveler').val());
    var unitCurrencyMark=$('#productCost4addTraveler').val().replace(/[\d\.\,\-]/g, "");
    var newCosts=[];
    $travelers.find('form[name="travelerForm"]').each(function () {
        var travelerId=$(this).find("input.traveler[name='id']").val();
        if(travelerId==''){
            var $this = $(this);
            newCosts.push({
                currencyMark:unitCurrencyMark,
                price:unitPrice
            });
            $this.find('.payfor-otherDiv .payfor-other').each(function () {
                var currencyId= $(this).find('select').val();
                var currency = currencyList.filter(function (item) {
                    if(item.id == currencyId){
                        return true;
                    }
                })[0];
                var price =$(this).find('[name="sum"]').val();
                if(price>0){
                    newCosts.push({
                        currencyMark:currency.currencyMark,
                        price:price
                    });
                }
            });
        }
    });

    var groupedCostPrices = groupPrice(oldCosts.concat(newCosts));
    var priceString='';
    for(var i =0;i<groupedCostPrices.length;i++){
        if(i==0){
            priceString += groupedCostPrices[i].currencyMark+fmoney(groupedCostPrices[i].price);
        }else{
            priceString += '+'+groupedCostPrices[i].currencyMark+fmoney(groupedCostPrices[i].price);
        }
    }

    $('#travelerSumPrice').text(priceString);
}


//按照货币 对金额进行分组求和
function groupPrice (amounts, amountKey) {
    var index = 0;
    if (!amounts) {
        return amounts;
    }
    if (!amountKey) {
        amountKey = 'price';
    }
    var groupedAmounts = [];

    while (amounts[index]) {
        var amount = amounts[index];
        var groupedAmount = groupedAmounts.filter(function (amount) {
            return amount.currencyMark== amounts[index].currencyMark;
        });
        if (groupedAmount.length) {
            groupedAmount[0][amountKey] = (+groupedAmount[0][amountKey]) + (+amount[amountKey] );
        } else {
            groupedAmount = {
                price: amount[amountKey],
                currencyName: amount.currencyName,
                currencyMark: amount.currencyMark,
                currencyId: amount.currencyId
            };
            groupedAmounts.push(groupedAmount);
        }
        index++;
    }
    return groupedAmounts;
};

//将     ￥12,212,44.0223 ==>> 1221244.02
function fmoney(s, n) {
    n = n > 0 && n <= 20 ? n : 2;
    s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
    var l = s.split(".")[0].split("").reverse(),
        r = s.split(".")[1];
    t = "";
    for(i = 0; i < l.length; i ++ )
    {
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
    }
    return t.split("").reverse().join("") + "." + r;
}
//1221244.02 ==>> 12,212,44.02
function rmoney(s) {
    return parseFloat(s.replace(/[^\d\.-]/g, ""));
}


//------------需求C333 结束-------------


