$(function(){
	inputTips();
	addTrans();
	selectMoneyTypeChange();
	initMoney();
});


function cancel(orderId){
	window.location.href=contextPath+"/orderCommon/transferMoney/transfersMoneyHref/"+orderId;
}

var reviewSign = 0;
function submitForm(){
	if(reviewSign == 0){
		reviewSign = 1;
		$.ajax({
			type : "POST",
			url : contextPath+"/orderCommon/transferMoney/transfersMoneyApplySub",
			data : $("#sumitForm").serialize(),
			dataType : "text",
			success : function(html){
				var json;
				try{
					json = $.parseJSON(html);
				}catch(e){
					json = {res:"error"};							
				}
				
				if(json.res=="success"){
					jBox.tip("提交申请成功", 'success');
					window.location.href=contextPath+"/orderCommon/transferMoney/transfersMoneyHref/"+$("#myOrderId").val();
				}else if(json.res=="data_error"){
					jBox.tip("输入数据不正确", 'error');
					reviewSign = 0;
				}else{
					jBox.tip(json.res, 'error');
					reviewSign = 0;
				}
			}
		
		});
	}
	
}

//订单转款
function transInputs(obj){
	var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
	// 只有当检测到不合法字符时才进行替换，因为实时替换会导致光标乱窜
	if (ms != obj.value) {
		var txt = ms.split(".");
		obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
	}
	totalTrans($(obj).parent().parent().parent());
};

function selectMoneyTypeChange(){
	$('.selectrefund').on("change",function(){
//		alert(11);
		var obj = $(this).parent().parent().parent();
		totalTrans(obj);
	});
}

function initMoney(){
	 $(".gai-price-ol").each(function(i, e) {
		 totalTrans($(this));
	 });
}

function totalTrans(obj){
//	 alert(obj.attr('id'));
	 var objId = obj.attr('id');
	 var loop = obj.attr('val');
	 var selects=$("#"+objId+" select[class='selectrefund']");
	 var totalcost="";
	 var hidTotalCost="";
	 $("#"+objId+" #currencyTemplate option").each(function(i, e) {
		var dataVal = $(e).val();
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
				     if(n==""|| n ==undefined){n=0;}else{money += parseFloat(n);}
			    }  
			}			
      });
		if(money!=""||money!=0){
			    hidTotalCost +=":"+ dataVal+"|"+money;
				datatype="<font class='tdgreen'> + </font><font class='gray14'>"+datatype+"</font>";
				money="<span class='tdred'>"+milliFormat(money,'1')+"</span>";
		        totalcost+=datatype+money;
			}
	});
	if(totalcost==0){
		$("#all-money"+loop).find('span').html(0);
		$("#all-money"+loop).find('input').val(0)
	}else{
		$("#all-money"+loop).find('span').html(totalcost);
		$("#all-money"+loop).find('input').val(hidTotalCost);
	}
	$("#all-money"+loop).find('span').find(".tdgreen").first().hide();

};

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
};




//团队退款
function addTrans(){
	$('.refund-price-btn').click(function() {
		var html = '<li>';
		html += '<i><select class="selectrefund">{0}<select></i>&nbsp;'.replace("{0}",$(this).parent().parent().find("select[class='selectrefund']").html());
		html += '<i><input type="text" class="gai-price-ipt1" name="refund" data-type="rmb" flag="istips" onkeyup="transInputs(this)" onafterpaste="transInputs(this)" onblur="transInputs(this)"/><span class="ipt-tips ipt-tips2">费用</span></i>&nbsp;';
		html += '<i><a class="ydbz_s gray clear-btn">删除</a></i>';
		$(this).parents('.gai-price-ol').append(html);
		//新增改价项目中，input获得失去焦点提示信息显示隐藏
		inputTips();
		selectMoneyTypeChange();
	});
	//删除团队退款一项
	$('.gai-price-ol').on("click",".clear-btn",function(){
		var obj = $(this).parent().parent().parent();
		$(this).parents('li').remove();
		totalTrans(obj);
	});
	
}

