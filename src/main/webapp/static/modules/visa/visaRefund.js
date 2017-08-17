//团队退款
function refunds(type){//type:visa-签证退款
	$('.refund-price-btn').click(function() {
		var ID = $(this).parents(".gai-price-ol").find("li").length +1;
		var html = '<li><i><input type="hidden" value="0" name="travelerId"/><input type="hidden" value="团队退款" name="travelerName"/>';
		html += '<input type="text" name="refundProject' + ID + '" class="gai-price-ipt1" flag="istips" id="refundProject0-' + ID +'" /><span class="ipt-tips ipt-tips2">款项</span></i>&nbsp;';
		html += '<i><select class="selectrefund" name="refundCurrency">{0}<select></i>&nbsp;'.replace("{0}",$("#currencyTemplate").html());
		html += '<i><input type="hidden" value="" name="payPrice"/><input type="text" class="gai-price-ipt1" var="travelerRefundPirce" name="refund" data-type="rmb" flag="istips" onkeyup="refundInput(this)" onafterpaste="refundInput(this)" onblur="refundInputs(this,1)"/><span class="ipt-tips ipt-tips2">费用</span></i>&nbsp;';
		html += '<i><input type="text" name="refundMark" class="gai-price-ipt2" flag="istips" /><span class="ipt-tips ipt-tips2">备注</span></i>&nbsp;';
		html += '<i><a class="ydbz_s gray clear-btn">删除</a></i>';
		$(this).parents('.gai-price-ol').append(html);
		//新增改价项目中，input获得失去焦点提示信息显示隐藏
		inputTips();
	});
	//删除团队退款一项
	$('.gai-price-ol').on("click",".clear-btn",function(){
		var $ol = $(this).parents(".gai-price-ol");
		$(this).parents('li').remove();
		$ol.find(".refundProject").each(function(index,element){
			var ID = $(element).attr("id");
			$(element).attr("id",ID.replace(/\-\d+$/,"-"+(index+1)));
			$(element).attr("name",ID.replace(/\-\d+$/,"-"+(index+1)));
		});
		totalRefund();
	});
	$('.bgMainRight').on("click",'.gaijia-delete',function(){
		var $table = $(this).parents(".refundTable");
		$(this).parent().parent().parent().remove();
		//如果是签证退款
		if("visa" == type){
			$table.find(".refundtd input:text").each(function(index, element) {
				var ID = $(element).attr("id");
				$(element).attr("id",ID.replace(/\-\d+$/,"-" + (index + 1)));
			});
		}
		totalRefund();
	});
	$('.bgMainRight').on("click",'.gaijia-add',function(){
		//$(this).parent().parent().parent().remove();
		var html_ipt = '<input type="text" />';
		//如果是签证退款
		if("visa" == type){
			var $similarIpt = $(this).parent().prev("input:text");
			var $table = $(this).parents(".refundTable");
			html_ipt = '<input type="text" name="' + $similarIpt.attr("name").replace(/\-\d+$/,"-" + ($table.find("tr").length + 1)) + '" id="' + $similarIpt.attr("id").replace(/\-\d+$/,"-" + ($table.find("tr").length + 1)) + '" />';
		}
		var html='<tr>';
		html+='<input type="hidden" value="' + $(this).parent().parent().parent().find("[name='travelerId']").val() + '" name="travelerId"/>';
		html+='<input type="hidden" value="' + $(this).parent().parent().parent().find("[name='travelerName']").val() + '" name="travelerName"/>';
		html+='<td class="refundtd">' + html_ipt + '<div class="pr"><i class="gaijia-delete" title="删除款项"></i></div></td>';
		html+='<td class="tc"><select style="width:90%;" class="selectrefund" name="refundCurrency">'+$("#currencyTemplate").html()+'</select></td>';
		html+='<td class="tr"><span class="tdgreen">' + $(this).parent().parent().parent().find(".tdgreen").text() + '</span><input type="hidden" value="' + $(this).parent().parent().parent().find(".tdgreen").text() + '" name="payPrice"/></td>';
		html+='<td><input type="text" var="travelerRefundPirce" onkeyup="refundInput(this)" onafterpaste="refundInput(this)" onblur="refundInputs(this,1)" name="refund" data-type="eur"></td>';
		html+='<td><input name="refundMark" type="text"></td></tr>';
		$(this).parents('.refundTable').append(html);
		//$(this).parents('tbody').find('td[rowspan]')
		totalRefund();
	});		
}

function changeRefund(){
	$(".bgMainRight").delegate("select[class*='selectrefund']","change",function(){
		totalRefund();
	});
	$(".table_borderLeftN").delegate("input[type='checkbox']","click",function(){
		totalRefund();
	});
	$(".table_borderLeftN").delegate("input[name='allChk']","click",function(){
		if($(this).prop("checked")){
			$(".table_borderLeftN").find("input[type='checkbox']").attr("checked",'true'); totalRefund();
		}else{
			$(".table_borderLeftN").find("input[type='checkbox']").removeAttr("checked"); totalRefund();
		}
	});
}
function totalRefund(){
	var selects=$("select[name='refundCurrency']");
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
					var n = si.parent().parent().find("input[var='travelerRefundPirce']").val();
					if(n==""){n=0;}else{money += parseFloat(n);}
				}  
			}			
		});
		if(money!=""||money!=0){
			datatype="<font class='tdgreen'> + </font><font class='gray14'>"+datatype+"</font>";
			money="<span class='tdred'>"+milliFormat(money,'1')+"</span>";
			totalcost+=datatype+money;
		}
	});
	if(totalcost==0){$('.all-money').find('span').html(0);}else{$('.all-money').find('span').html(totalcost);}
	$('.all-money').find('span').find(".tdgreen").first().hide();
}
