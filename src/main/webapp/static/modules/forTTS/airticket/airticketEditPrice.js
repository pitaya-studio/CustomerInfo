
//发布签证产品--价格--币种选择
function selectCurrencyVisa(){
	//加载初始化

	
	//币种变化
	$(".sel-currency").change(function(){
		var theValue = $(this).val();
		var newCurrency = $(this).children("option:selected").attr("addClass");
		$(this).parent().parent().parent().find("input.ipt-currency").prev().text(newCurrency);
	});
}
/*是否含税*/
function isTaxation(){
	$(".ckb-tax").each(function(index, element) {
		if($(this).prop("checked")){
			$(this).parent().next().find("input[name$='taxamt']").removeAttr("disabled");
		}else{
			$(this).parent().next().find("input[name$='taxamt']").attr("disabled","disabled");
		}
    });
	$(".add2_nei_table").on("click",".ckb-tax",function(){
		if($(this).prop("checked")){
			$(this).parent().next().find("input[name$='taxamt']").removeAttr("disabled");
		}else{
			$(this).parent().next().find("input[name$='taxamt']").attr("disabled","disabled");
		}
	})
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

//只能输入数字
function onlyNum() { 
    if(!(event.keyCode==46)&&!(event.keyCode==8)&&!(event.keyCode==37)&&!(event.keyCode==39)) 
	if(!((event.keyCode>=48&&event.keyCode<=57)||(event.keyCode>=96&&event.keyCode<=105))) 
	event.returnValue=false; 
} 

function secondToOne(){
	//window.location.href = "../airTicket/form.htm";
	//history.go(-1);
	history.back();
}


//验证是否是数值
function isFloat(str){
	return parseFloat(str) ? parseFloat(str) : 0.00;
}

function secondToThird(){
	var companyUUID=$("#companyUUID").val();
	//C460 获取当前团号配置规则 0:手动；1:自动
	var groupCodeRuleJP = $("#groupCodeRuleJP").val();
	var flag = false;
	
	//青岛凯撒、诚品旅游、懿洋假期、日信观光、大洋、 非常国际、优加、起航、手动配置 C460
	if(companyUUID=='7a81a03577a811e5bc1e000c29cf2586'
		|| companyUUID=='1d4462b514a84ee2893c551a355a82d2'
		|| companyUUID=='7a81c5d777a811e5bc1e000c29cf2586'
		|| companyUUID=='5c05dfc65cd24c239cd1528e03965021'
		|| companyUUID=='7a8177e377a811e5bc1e000c29cf2586'
		|| companyUUID=='ed88f3507ba0422b859e6d7e62161b00'
		|| companyUUID=='f5c8969ee6b845bcbeb5c2b40bac3a23'
		|| companyUUID=='58a27feeab3944378b266aff05b627d2'
		|| groupCodeRuleJP==0 
		){
		var groupCode = $("#groupCode").val();
		if(groupCode==""){
			top.$.jBox.tip("请填写团号!", 'warnning');
			return;
		}else{
			var txt_ticketId=$("#txt_ticketId").val();
			if(txt_ticketId == null || txt_ticketId == ''){
				$.ajax({
					'type': "POST",
					'async':false,
					'url': g_context_url+"/activity/manager/checkAirTicketGroupCode",
					'data': {
						"groupCode" : groupCode
					},
					success: function(data){
						if(data.result=="0"){
							top.$.jBox.tip("团号重复，请修改!", 'warnning');
						}else{
							flag = true;
						}
					}
				});
			}else{
				flag = true;
			}
			if (flag==false) {
				return false;
			}
		}
	}
	
    // 处理bug 12939
	//if(!$("#secondStepContent").find("input[name='maxPeopleCount']").val().trim()=="")
	if(!$("#secondStepContent").find("input[name='maxPeopleCount']").val()=="" )
	{
		var maxPeopleCount= $("#secondStepContent").find("input[name='maxPeopleCount']").val();
		var lgth = $("#yushourenshu").text().length;
		var ysCount=$("#yushourenshu").text().substring(0,lgth-1);
		if(parseInt(maxPeopleCount)>parseInt(ysCount))
		{
			top.$.jBox.tip("特殊人群最高人数不能大于预收数", 'warnning');
			return;
		}
	}
	if(!$("#secondStepContent").find("input[name='maxChildrenCount']").val()=="" )
	{
		var maxChildrenCount= $("#secondStepContent").find("input[name='maxChildrenCount']").val();
		var lgth = $("#yushourenshu").text().length;
		var ysCount=$("#yushourenshu").text().substring(0,lgth-1);
		if(parseInt(maxChildrenCount)>parseInt(ysCount))
		{
			top.$.jBox.tip("儿童最高人数不能大于预收数", 'warnning');
			return;
		}
	}
	if(!$("#secondStepContent").find("input[name='maxChildrenCount']").val()=="" && !$("#secondStepContent").find("input[name='maxPeopleCount']").val()=="")
	{
		var maxPeopleCount= $("#secondStepContent").find("input[name='maxPeopleCount']").val();
		var maxChildrenCount= $("#secondStepContent").find("input[name='maxChildrenCount']").val();
		var lgth = $("#yushourenshu").text().length;
		var ysCount=$("#yushourenshu").text().substring(0,lgth-1);
		if((parseInt(maxChildrenCount)+parseInt(maxPeopleCount))>parseInt(ysCount))
		{
			top.$.jBox.tip("儿童与特殊人群最高人数之和不能大于预收数", 'warnning');
			return;
		}
		
	}
	
	
	

	var settlementAdultPrice = $("#settlementAdultPrice").val();
	var settlementcChildPrice = $("#settlementcChildPrice").val();
	var settlementSpecialPrice = $("#settlementSpecialPrice").val();
	//*0258需求,发票税:针对懿洋假期-tgy-s*//
	var invoiceTax=$("#invoiceTax").val();
	/*if(invoiceTax==""){  //发票税为空,系统默认发票税为0
		$("#invoiceTax").val(0);
	}*/
	//*258需求,发票税:针对懿洋假期-tgy-e*//
	if(settlementAdultPrice==""){
	    top.$.jBox.tip("请填写成人同行价!", 'warnning');
		return;
	}
	if(settlementcChildPrice==""){
	 	top.$.jBox.tip("请填写儿童同行价!", 'warnning');
		return;
	}
	if(settlementSpecialPrice==""){
		top.$.jBox.tip("请填写特殊人群同行价!", 'warnning');
		return;
	}
	
	var str="";
	$("input[name='more_istax']").each(function() {  
        if($(this).attr("checked") =="checked"){
        	str+=$(this).val()+",";
        }else{
        	str+="-1,";
        }
    });
	$("#txt_istax").val(str);
	//选择分段报价
	if($("input[name='flyDivInput']").prop("checked")){
		var adultAll=$("input[name='settlementAdultPrice']").val();
		var childAll=$("input[name='settlementcChildPrice']").val();
		var specialAll=$("input[name='settlementSpecialPrice']").val();
		//*0258需求,发票税:针对懿洋假期-tgy-s*//
		var invoiceTaxAll=$("input[name='invoiceTax']").val();
		//*258需求,发票税:针对懿洋假期-tgy-e*//
		var adultGroup=0;
		var childGroup=0;
		var specialGroup=0;
		$(".flyMoreDiv input[name^='more_settlementAdultPrice']").each(function(index, element) {
			adultGroup += isFloat($(element).val().replace(/[ ]/g,"").replace(/,/g,""));
		});
		$(".flyMoreDiv input[name^='more_settlementcChildPrice']").each(function(index, element) {
			childGroup += isFloat($(element).val().replace(/[ ]/g,"").replace(/,/g,""));
		});
		$(".flyMoreDiv input[name^='more_settlementSpecialPrice']").each(function(index, element) {
			specialGroup += isFloat($(element).val().replace(/[ ]/g,"").replace(/,/g,""));
		});
		if(adultGroup!=adultAll|| childGroup!=childAll || specialGroup!=specialAll){
			var html = '<p class="gaijiaBox">分段报价不等于整体报价</p>';
			$.jBox(html, {title: "提示",buttons:{"取 消":"0","确定":"1"},submit:function(v, h, f){
				 if(v=="0"){
					//取消的操作
					return true;
				}else if(v=="1"){
					//确定的操作	
					$("#form1").submit(); 
				}
			},height:155,width:300});
		}else{
			$("#form1").submit(); 
		}
	}else{
		//整体报价		
		$("#form1").submit(); 
	}
	

}