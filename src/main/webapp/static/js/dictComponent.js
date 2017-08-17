/**
 * 基础信息组件弹出框
 */
function addCompont(ctx ,obj, compName, type, inputType){
	if(inputType == 'select') {
		if($(obj).find('option:last').attr('selected') != 'selected') {
			return ;
		}
	}
	var html = '<div class="jbox_type">'+
			   '<p><label>名称：</label><input type="text" name="'+ compName +'" id="'+ compName +'"/></p><div id="'+ compName +'Div" align="center" style="color:orange;height:20px;" height="20px;"></div>'+
	           '<p><label>排序：</label><input type="text" name="'+ compName +'Sort" id="'+ compName +'Sort" maxlength="4" /></p><div id="'+ compName +'SortDiv" align="center" style="color:orange;height:20px;" height="20px;"></div>'+
			   '<p><label>描述：</label><textarea name="'+ compName +'Description" id="'+ compName +'Description" class="" maxlength="99"></textarea></p>'+
	           '</div>';
	
	$.jBox(html, {title: "添加信息",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
			var comp=$("#"+compName).val();
			var compSort=$("#"+compName+"Sort").val();
			var description=$("#"+ compName +"Description").val();
			
			if(comp == "") {
				$("#"+ compName +"Div").text("名称不能为空");
			} else if($("#"+ compName +"Div").text() == "名称不能为空") {
				$("#"+ compName +"Div").text("");
			}
			
			if(compSort == "") {
				$("#"+ compName +"SortDiv").text("排序不能为空");
			} else if($("#"+ compName +"SortDiv").text() == "排序不能为空") {
				$("#"+ compName +"SortDiv").text("");
			}
			
			if(($("#"+ compName +"Div").text() == "") && ($("#"+ compName +"SortDiv").text() == "")) {
				$.post(ctx + "/sysCompanyDictView/saveDict", {"type":type,"label":$("#" + compName).val(),"sort":compSort,"description":description},
					function(data){
						if(data.result=="1"){
							top.$.jBox.tip('添加成功','success');
							if(inputType == 'select') {
								$(obj).find('option:selected').before('<option value="' + data.uuid + '">'+comp+'</option>');
								$(obj).find('option:selected').parent().val(data.uuid);
							} else {
								$(obj).before('<label for="'+ compName +'_' + data.uuid + '"><input id="'+ compName +'_' + data.uuid + '" type="checkbox" value="' + data.uuid + '" name="'+ compName +'" checked>'+comp+'</label>');
							}
							/** 回调函数（添加完基础信息后调用） */
							addCompontCallBack();
							window.parent.window.jBox.close();
						}else{
							top.$.jBox.tip(data.message,'warning');
						}
					}
				);
			}
			return true;
		} else {
			return false;
		}
	},height:330,persistent: true});
	
	$("#"+compName).blur(function(){
		$.post(ctx + "/sysCompanyDictView/check", {"type":type,"label":$("#"+compName).val()},
			function(data){
				if(data == "false") {
					$("#"+ compName +"Div").text("名称已存在");
				} else if(data == "true"){
					$("#"+ compName +"Div").text("");
				}
			}
		);
	});
	
	$("#"+ compName +"Sort").blur(function(){
		var reg = new RegExp("^[0-9]*$");
		if(!reg.test($("#"+ compName +"Sort").val())) {
			$("#"+ compName +"SortDiv").text("只能输入整数");
		} else {
			$("#"+ compName +"SortDiv").text("");
		}
	});
}