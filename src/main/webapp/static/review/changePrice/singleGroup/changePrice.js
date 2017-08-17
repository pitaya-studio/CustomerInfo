var ctx;
$(function(){
	
	//程序路径
	ctx = $("#ctx").val();
	
	//游客复选框单选事件
	var $items = $(":checkbox[name='travelerId']");
	//游客单选事件
	$("#checkedAllBox").click(function(){
		$(":checkbox[name='travelerId']").prop("checked", this.checked);
	});
	//游客复选框单击后修改全选框
	$items.click(function(){
		$("#checkedAllBox").prop("checked",($items.filter(":checked").length == $items.length));
	});
	
	//改价js
	gaijia();
})


// 【游客 团队 其它】   改价差额计算(HPT 重写 common.js validNumFinally函数)

//正负数字验证
function validNum(dom){
	var thisvalue = $(dom).val();
	
	if(thisvalue.length >15){
		alert("改价金额位数不合法!");
		thisvalue = '0.00';
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
		$(dom).val(thisvalue);
	}
}

var tokenTimes = 0;

function check_activity_uppricess() {
	
	//校验是否有游客被选中，如果没有被选游客则返回
	var checkedTraveler = $(":checkbox[name='travelerId']:checked");
	if (checkedTraveler.length == 0) {
		top.$.jBox.tip('请选择需要改价的游客','warning');
        top.$('.jbox-body .jbox-icon').css('top','55px');
        return false;
	}
	
	//此块代码暂时未细看
	$(":checkbox[name='travelerId']:not(:checked)").parents("tr").each(function(index, obj) {
		var test = $(this).attr("group");
		$("#form1").find("tr[group][group='"+ test +"']").each(function(){
			$(this).find("input[name='plusysTrue']").val("0.00");
		});
	});
	
	//禁止多次提交，提交成功后不能再次提交
	// tokenTimes++;
	// if (tokenTimes != 1) {
	// 	return;
	// }

	var isEnd = false;
	var travelerArr = [];
	$("#toursChangePrice tr input").each(function(){
		if($(this).is(":checked")){
			var traveler = new Object();
			var travelerId = $(this).parent().nextAll().find("[name=travelerids]").val();
			var travelerremark = $(this).parent().nextAll().find("#travelerremark").val();
			traveler.travelerId = travelerId;
			traveler.travelerremark = travelerremark;
			$target=$(this).parent().nextAll().find(".payfor-otherDiv");
			if($target.length == 0) {
				top.$.jBox.tip('请输入费用信息','warning');
				isEnd = true;
				return;
			}
			traveler.feeArr = [];
			$target.find(".data_value").each(function (e, item) {
				var fee = new Object();
				fee.name = $(this).attr("data-name");
				fee.number = $(this).attr("data-number");
				fee.price = $(this).attr("data-price");
				fee.currencyId = $(this).attr("data-currencyId");
				fee.sum = $(this).attr("data-result");
				traveler.feeArr.push(fee);
			});
			travelerArr.push(traveler);
		}
	})
	if(isEnd) {
		return false;
	}

	//提交改价申请
	var form1 = $("#form1").serialize();
	var orderId = $("#orderId").val();
	var productType = $("#productType").val();
	$.ajax({
		url : ctx + '/newChangePrice/applyChangePrice',
		type : 'post',
		data : {travelerArr:JSON.stringify(travelerArr),orderId:orderId,productType:productType},
		async : true,
		success : function(data) {
			if (data.result == "success") {
				top.$.jBox.tip("改价申请成功", "success");
				window.location.href = ctx + '/newChangePrice/list?orderId=' + orderId + '&productType=' + productType;
			} else {
				tokenTimes = 0;
				top.$.jBox.tip(data.msg, "warning");
			}
		}
	});
}



