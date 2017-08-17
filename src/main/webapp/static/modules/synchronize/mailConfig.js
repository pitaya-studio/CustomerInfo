$(document).ready(function() {
	var sendEmailState = $("#sendEmailStateHidden").val();
	var addressHidden = $("#addressHidden").val();
	if(sendEmailState && sendEmailState != "" && sendEmailState == "0") {
		$("[name='sendEmailState']:eq(1)").attr("checked",true);
	}
	var addressArr = addressHidden.split(",");
	for(var i=0;i<addressArr.length;i=i+1) {
		var mailAddress = $("[name='mailAddress']:last");
		$(mailAddress).val(addressArr[i]);
		if(i != addressArr.length-1) {
			$(mailAddress).parent().clone(true).appendTo($(mailAddress).parent().parent());
		}
	}
});

//复制节点
function copyNode(obj) {
	$(obj).parent().clone(true).appendTo($(obj).parent().parent());
}

//删除节点
function deleteNode(obj) {
	if($("[name='mailAddress']").length == 1) {
		alert("不能删除");
		return false;
	} else {
		$(obj).parent().remove();
	}
}

//保存
function saveMileConfig() {
	var para = '?';
	var sendEmailState = $("[name='sendEmailState']:checked").val();
	var mailAddress = '';
	$("[name='mailAddress']").each(function(index,obj) {
		mailAddress += (this.value + ",");
	});
	var para = (para + "sendEmailState=" + sendEmailState + "&" + "mailAddress=" +  mailAddress);
	var actionVal = $("#mailConfigForm").attr("action");
	$("#mailConfigForm").attr("action",actionVal+para).submit();
}