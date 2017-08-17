/*!
 * JQuery for the module of basic information(基础信息维护模块的脚本)
 *
 * Depends:common.js
 * the public functions used by this module:
 *
 * Date: 2015-01-14
 */

/*=============酒店-基础信息添加 begin===============*/
//酒店类型select
function typeOfHotel(obj){
	if($(obj).val()== 1){
		$('.typeOfHost').hide();
	}else if($(obj).val()== 2){
		$('.typeOfHost').show();
	}else{
		$('.typeOfHost').hide();
	}
}
//添加联系人
function addContacts(obj){
	var $thisForm = $(obj).parents("form");
	$(obj).parent().before($("#otherExpensesTemplate").html());
	lxrSorting();
}
//删除联系人
function deleteContacts(obj){
	var $form = $(obj).parents("form");
	$(obj).parent().parent().remove();
	lxrSorting();
}
//联系人排序
function lxrSorting(){
	$('.otherExpenses').each(function(index, element){
		$(element).find('label:eq(0) em').text(index);
	});
}
/*=============酒店-基础信息添加 end===============*/

/*=============添加岛屿信息、添加连锁、酒店基础信息添加页面 begin===============*/
//select 酒店类别 添加
/*function addType(obj){
	var oneText=$(obj).find('option:selected').text();
	var href=$(obj).val();
	if(oneText == '+添加'){
		var text=$(obj).parent().find('option:eq(0)').prop("selected",true);
		window.open(href);
	}
	
}*/
//岛屿类型添加
function jbox_islandsAdd(obj){
	var html = '<div class="jbox_type">'+
			   '<p><label>岛屿类型：</label><input type="text" name="addHost"/></p>'+
	           '<p><label>排序：</label><input type="text" name=""/></p>'+
			   '<p><label>描述：</label><textarea name="" id="" class=""></textarea></p>'+
	           '</div>';
	$.jBox(html, { title: "添加信息",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
			var addHost=$('input[name=addHost]').val();
			$(obj).before('<option value="">'+addHost+'</option>');
			$(obj).prev().prop("selected",true);
		}
	},height:260});
}
//岛屿名称添加
function jbox_islandnameAdd(obj){
	var html = '<div class="jbox_type">'+
			   '<p><label>岛屿名称：</label><input type="text" name="addHost"/></p>'+
	           '<p><label>排序：</label><input type="text" name=""/></p>'+
			   '<p><label>描述：</label><textarea name="" id="" class=""></textarea></p>'+
	           '</div>';
	$.jBox(html, { title: "添加信息",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
			var addHost=$('input[name=addHost]').val();
			$(obj).before('<option value="">'+addHost+'</option>');
			$(obj).prev().prop("selected",true);
		}
	},height:260});
}
//上岛添加
function jbox_sdAdd(obj){
	var html = '<div class="jbox_type">'+
			   '<p><label>上岛方式：</label><input type="text" name="addHost"/></p>'+
	           '<p><label>排序：</label><input type="text" name=""/></p>'+
			   '<p><label>描述：</label><textarea name="" id="" class=""></textarea></p>'+
	           '</div>';
	$.jBox(html, { title: "添加信息",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
			var addHost=$('input[name=addHost]').val();
			$(obj).before('<option value="">'+addHost+'</option>');
			$(obj).prev().prop("selected",true);
		}
	},height:260});
}
//酒店类别添加
function jbox_hostlbAdd(obj){
	var html = '<div class="jbox_type">'+
			   '<p><label>酒店类别：</label><input type="text" name="addHost"/></p>'+
	           '<p><label>排序：</label><input type="text" name=""/></p>'+
			   '<p><label>描述：</label><textarea name="" id="" class=""></textarea></p>'+
	           '</div>';
	$.jBox(html, { title: "添加信息",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
			var addHost=$('input[name=addHost]').val();
			$(obj).before('<option value="">'+addHost+'</option>');
			$(obj).prev().prop("selected",true);
		}
	},height:260});
}
//酒店星级添加
function jbox_hoststarAdd(obj){
	var html = '<div class="jbox_type">'+
			   '<p><label>酒店星级：</label><select name="addHost"><option>五星级</option><option>四星级</option><option>三星级</option><option>二星级</option></select></p>'+
	           '<p><label>排序：</label><input type="text" name=""/></p>'+
			   '<p><label>描述：</label><textarea name="" id="" class=""></textarea></p>'+
	           '</div>';
	$.jBox(html, { title: "添加信息",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
			var addHost=$('select[name=addHost]').val();
			$(obj).before('<option value="">'+addHost+'</option>');
			$(obj).prev().prop("selected",true);
		}
	},height:260});
}

//境内境外 改变地址展现形式
function domesticOverseas(obj){
	var doVal=$('.domesticOverseas option:selected').val();
	if(doVal == 1){
		$('.overseas').hide();
		$('.domestic').show();
	}else if(doVal == 2){
		$('.domestic').hide();
		$('.overseas').show();	
	}else{
		$('.overseas').hide();
		$('.domestic').hide();
	}
}
function getGeoSelectState(type,obj,name){
	if((type||obj)&&name){
		$.ajax({
			type: "POST",
		   	url: $ctx+"/geography/getGeoListAjax",
		   	data: {
				"type":type,
				"parentId":$(obj).val()
			},
			dataType: "json",
		   	success: function(data){
		   		if(data){
		   			if(name=="country"){
		   				$state = $("#country");
		   				$state.empty();
		   				if (data) {
		   					$state.append("<option value=''>请选择</option>");
		   					for (var index in data) {
		   						var state = data[index];
		   						var optionTheme = "<option value='" + state.uuid + "'>" + state.nameCn + "</option>";
		   						$state.append(optionTheme);
		   					}
		   				}
		   				$state.trigger("comboboxinquiryselect");
		   				$state.comboboxInquiry('reset');
	   				}else if(name=="province"){
	   					var $province = $("#province");
	   					$province.empty();
	   					if (data) {
	   						$province.append("<option value=''>请选择</option>");
	   						for (var index in data) {
	   							var province = data[index];
	   							var optionTheme = "<option value='" + province.uuid + "'>" + province.nameCn + "</option>";
	   							$province.append(optionTheme);
	   						}
	   					
	   					}
	   					$province.trigger("comboboxinquiryselect");
	   					$province.comboboxInquiry("reset");
	   				
	   				}else if(name=="city"){
	   					var $city = $("#city");
	   					$city.empty();
		   				if(data){
		   					$city.append("<option value=''>请选择</option>");
		   					for (var index in data) {
				   				var city=data[index];
			   					var optionTheme = "<option value='"+city.uuid+"'>"+city.nameCn+"</option>";
			   					$city.append(optionTheme);
				   			}
		   					
		   				}
		   				$city.trigger("comboboxinquiryselect");
		   				$city.comboboxInquiry("reset");
	   				}else if(name=="district"){
	   					$district = $("#district");
	   					$district.empty();
	   					$district.append("<option value=''>请选择</option>");
	   					if(data){
	   						for (var index in data) {
				   				var district=data[index];
				   				var optionTheme = "<option value='"+district.uuid+"'>"+district.nameCn+"</option>";
				   				$district.append(optionTheme);
				   			}
	   					}
	   					$district.trigger("comboboxinquiryselect");
	   					$district.comboboxInquiry("reset");
	   				}else if(name=="overseas_state"){
	   					$state = $("#overseas_state");
	   					$state.empty();
	   					$state.append("<option value=''>请选择</option>");
	   					if(data){
	   						for (var index in data) {
				   				var state=data[index];
				   				var optionTheme = "<option value='"+state.uuid+"'>"+state.nameCn+"</option>";
				   				$state.append(optionTheme);
				   			}
	   					}
	   					$state.trigger("comboboxinquiryselect");
	   					$state.comboboxInquiry("reset");
	   				}else if(name=="overseas_province"){
	   					$province = $("#overseas_province");
	   					$province.empty();
	   					$province.append("<option value=''>请选择</option>");
	   					if(data){
	   						for (var index in data) {
				   				var province=data[index];
				   				var optionTheme = "<option value='"+province.uuid+"'>"+province.nameCn+"</option>";
				   				$province.append(optionTheme);
				   			}
	   					}
	   					$province.trigger("comboboxinquiryselect");
	   					$province.comboboxInquiry("reset");
	   				}else if(name=="overseas_city"){
	   					$city = $("#overseas_city");
	   					$city.empty();
	   					
	   					$city.append("<option value=''>请选择</option>");
	   					if(data){
	   						for (var index in data) {
				   				var city=data[index];
				   				var optionTheme = "<option value='"+city.uuid+"'>"+city.nameCn+"</option>";
				   				$city.append(optionTheme);
				   			}
	   					}
	   					$city.trigger("comboboxinquiryselect");
	   					$city.comboboxInquiry("reset");
	   				}
		   			
		   		}
				
		   	}
		});
	}
}

$(function(){
	$("#country").comboboxInquiry();
	$("#province").comboboxInquiry();
	$("#city").comboboxInquiry();
	$("#district").comboboxInquiry();

	$("#overseas_state").comboboxInquiry();
	$("#overseas_city").comboboxInquiry();
	$("#overseas_province").comboboxInquiry();
	
	$(document).on("change", ".domesticOverseas", function () {
		var areaType = $(this).val();
		if (areaType == 1) {
			$state = $("#country");
			getGeoSelectState($(this).val(),'','country');
		}else if (areaType ==2){
			$state = $("#overseas_state");
			getGeoSelectState($(this).val(),'','overseas_state');
		}else if (areaType ==""){
			$('.overseas').hide();
			$('.domestic').hide();
		}
		
	});
	$("#country").on("comboboxinquiryselect", function () {
		getGeoSelectState('',$("#country"),'province');
	});
	
	$("#province").on("comboboxinquiryselect",function () {
		getGeoSelectState('',$("#province"),'city');		
	});
	$("#city").on("comboboxinquiryselect",  function () {
		getGeoSelectState('',$("#city"),'district');	
	});
	
	$("#overseas_state").on("comboboxinquiryselect", function () {
		getGeoSelectState('',$("#overseas_state"),'overseas_province');	
	});

	$("#overseas_province").on("comboboxinquiryselect", function () {
		getGeoSelectState('',"#overseas_province",'overseas_city');	
	});
	
});

/*=============添加岛屿信息、添加连锁、酒店基础信息添加页面 end===============*/

/*=============酒店餐型管理、餐型类型管理等多个列表页面 begin===============*/
//排序
function updateOrder(obj){
	// bug17526 UG_V2将按钮统一为input，没有text()的情况下判断value by tlw at 20170307
	var txt=$(obj).text()||$(obj).val();
	if(txt=="修改排序"){
		// $(obj).text("提交");
		$(obj).text()?$(obj).text("提交"):$(obj).val("提交");
		$("input[class='maintain_sort']").removeAttr("disabled");
	}else{
		$(obj).text()?$(obj).text("修改排序"):$(obj).val("修改排序");
		$("input[class='maintain_sort']").attr("disabled","disabled");
		//提交修改排序后的回调函数
		orderSubmitCallBack();
	}
}
/*=============酒店餐型管理、餐型类型管理等多个列表页面 end===============*/

/*=============酒店基础信息添加、添加房型、添加岛屿信息页面 begin===============*/
//酒店主题 特色 设施 添加
function jbox_hostadd(obj){
	var html = '<div class="jbox_type">'+
			   '<p><label>名称：</label><input type="text" name="addHost"/></p>'+
	           '<p><label>排序：</label><input type="text" name=""/></p>'+
			   '<p><label>描述：</label><textarea name="" id="" class=""></textarea></p>'+
	           '</div>';
	$.jBox(html, { title: "添加信息",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
			var addHost=$('input[name=addHost]').val();
			if(addHost ==""){
			}else{
				$(obj).before('<label><input type="checkbox" name="shoptype">'+addHost+'</label>');
			}

		}
	},height:260});
}
/*=============酒店基础信息添加、添加房型、添加岛屿信息页面 end===============*/

/*=============连锁酒店管理-添加连锁、酒店-基础信息添加页面 begin===============*/
//删除已上传的文件
function deleteFile(thisDom,fileID){
	$(thisDom).parent("li").remove();
}
/*=============连锁酒店管理-添加连锁、酒店-基础信息添加页面 end===============*/

//基础信息维护-地域城市-添加地域城市
function jbox_gncjqy(){
	var html = '<div style=" padding:20px; overflow:hidden;">';
	html += '<label class="jbox-label" style="width:120px;">二级区域：</label><input name="" type="text" /><br />';
	html += '<label class="jbox-label" style="width:120px;">省/直辖市关联区域：</label>';
	html += '<div class="jbox-nrqy"><ul><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">北京市</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">呼和浩特市</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">湖南省</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">北京市</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">北京市</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">北京市</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">北京市</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">北京市</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">北京市</label></li></ul></div>';
	html += '</div>';
	$.jBox(html, { title: "新增二级区域",buttons:{'取消':0,'提交': 1}, submit:function(v, h, f){
		if (v=="1"){
			return true;
		}else{
			return true;
		}
	},height:300,width: 530});
}
function jbox_gncjqy2(){
	var html = '<div style=" padding:20px; overflow:hidden;">';
	html += '<label class="jbox-label" style="width:120px;">区域：</label><input name="" type="text" /><br />';
	html += '<label class="jbox-label" style="width:120px;">国家关联区域：</label>';
	html += '<div class="jbox-nrqy"><ul><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">美国</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">日本</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">韩国</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">美国</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">日本</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">韩国</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">美国</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">日本</label></li><li><input name="" type="checkbox" value="" id="ck" /><label for="ck">韩国</label></li></ul></div>';
	html += '</div>';
	$.jBox(html, { title: "新增一级区域",buttons:{'取消':0,'提交': 1}, submit:function(v, h, f){
		if (v=="1"){
			return true;
		}else{
			return true;
		}
	},height:300,width: 530});
}


//基础信息维护-地域城市-创建省/市
function jbox_gncjss(){
	var html = '<div style=" padding:20px; overflow:hidden;">';
	html += '<label class="jbox-label" style="width:120px;">省/直辖市：</label><input name="" type="text" /><br />';
	html += '</div>';
	$.jBox(html, { title: "新增创建省/市",buttons:{'取消':0,'提交': 1}, submit:function(v, h, f){
		if (v=="1"){
			return true;
		}else{
			return true;
		}
	},height:160,width: 530});
}

//基础信息维护-地域城市-城市添加
function jcxxwh_cszj(){
	$('.jbox-zj').click(function(){				 
		$(this).parent().after('<div class="kongr20"></div><div class="seach25 seach100"><p>城市：</p><input name="" type="text" class="fl"/><div class="ydbz_s jbox-dle gray ml20">删除</div></div>');					 
	});
	$('.jbox-gwzj').click(function(){
		$(this).parent().after('<div class="kongr20"></div><div class="seach25 seach100"><p>国家：</p><input name="" type="text" class="fl"/><div class="ydbz_s jbox-dle gray ml20">删除</div></div>');					 
	});
	$('.jbox-dle').live('click',function(){
		$(this).parent().prev().remove()
		$(this).parent().remove();
	});
}

//基础信息维护-交通信息-航空公司-新增航空公司
//function jbox_xzhkgs() {
//	var html = '<div style="margin-top:20px; padding-left:20px;">';
//	html += '<dl style="overflow:hidden; padding-right:5px;">';
//	html += '<dt style="height:30px; float:left; width:100%;">';
//	html += '	<span class="fl jbox-span-radio"><input name="qiewei" type="radio" style="margin:0;" value="" checked /> 国内航空公司</span>';
//	html += '	<span class="fl jbox-span-radio"><input name="qiewei" style="margin:0;" type="radio" value="" /> 国外航空公司</span>';
//	html += '</dt>';
//	html += '<dd class="jbox-margin0 fl"  style=" display:none;">';
//	html += '	<label class="jbox-label">国家：</label><select name="" class="jbox-width100"><option value="">中国</option></select><br />';
//	html += '	<div class="jbox-margin0 fl">';
//	html += '		<label class="jbox-label">航空公司：</label><input type="text" value="" class="fl jbox-width100">';
//	html += '		<label class="jbox-label">二字码：</label><input type="text" value="" class="fl jbox-width100">';
//	html += '		<div class="ydbz_s jbox-zj">增加</div>';
//	html += '	</div>';
//	html += '</dd>';
//	html += '<dd class="jbox-margin0 fl" style=" display:none;">';
//	html += '	<label class="jbox-label">国家：</label><select name="" class="jbox-width100"><option value="">美国</option><option value="">日本</option></select><br />';
//	html += '	<div class="jbox-margin0 fl">';
//	html += '		<label class="jbox-label">航空公司：</label><input type="text" value="" class="fl jbox-width100">';
//	html += '		<label class="jbox-label">二字码：</label><input type="text" value="" class="fl jbox-width100">';
//	html += '		<div class="ydbz_s jbox-zj">增加</div>';
//	html += '	</div>';
//	html += '</dd>';
//	html += '</dl>';
//	html += '</div>';
//	$.jBox(html, { title: "新增航空公司",buttons:{'取消': 0,'提交':1}, submit:function(v, h, f){
//		if (v=="1"){
//			return true;
//		}else{
//			return true;
//		}
//	},height:320,width:520});
//	
//	$('.jbox-zj').click(function(){				 
//		$(this).parent().after('<div class="jbox-margin0 fl"><label class="jbox-label">航空公司：</label><input type="text" value="" class="fl jbox-width100"><label class="jbox-label">二字码：</label><input type="text" value="" class="fl jbox-width100"><div class="ydbz_s jbox-dle gray">删除</div></div>');					 
//	});
//	$('.jbox-dle').live('click',function(){
//		$(this).parent().remove();									 
//	});
//	
//	qiewei();	
//}
//基础信息维护-交通信息-机场信息-新增机场信息
function jbox_xzjcxx() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<dl style="overflow:hidden; padding-right:5px;">';
	html += '<dt style="height:30px; float:left; width:100%;">';
	html += '	<span class="fl jbox-span-radio"><input name="qiewei" type="radio" style="margin:0;" value="" checked /> 国内机场</span>';
	html += '	<span class="fl jbox-span-radio"><input name="qiewei" style="margin:0;" type="radio" value="" /> 国外机场</span>';
	html += '</dt>';
	html += '<dd class="jbox-margin0 fl"  style=" display:none;">';
	html += '	<label class="jbox-label"><i class="xing">*</i>国家：</label><select name="" class="jbox-width100"><option value="">中国</option></select><br />';
	html += '	<label class="jbox-label"><i class="xing">*</i>城市：</label><select name="" class="jbox-width100"><option value="">北京</option><option value="">武汉</option></select><br />';
	html += '	<div class="jbox-margin0 fl">';
	html += '		<label class="jbox-label"><i class="xing">*</i>机场：</label><input type="text" value="" class="fl jbox-width100">';
	html += '		<label class="jbox-label"><i class="xing">*</i>三字码：</label><input type="text" value="" class="fl jbox-width100">';
	html += '		<div class="ydbz_s jbox-zj">增加</div>';
	html += '	</div>';
	html += '</dd>';
	html += '<dd class="jbox-margin0 fl" style=" display:none;">';
	html += '	<label class="jbox-label"><i class="xing">*</i>国家：</label><select name="" class="jbox-width100"><option value="">美国</option><option value="">日本</option></select><br />';
	html += '	<label class="jbox-label"><i class="xing">*</i>城市：</label><select name="" class="jbox-width100"><option value="">纽约</option><option value="">爱尔兰</option></select><br />';
	html += '	<div class="jbox-margin0 fl">';
	html += '		<label class="jbox-label"><i class="xing">*</i>机场：</label><input type="text" value="" class="fl jbox-width100">';
	html += '		<label class="jbox-label"><i class="xing">*</i>三字码：</label><input type="text" value="" class="fl jbox-width100">';
	html += '		<div class="ydbz_s jbox-zj">增加</div>';
	html += '	</div>';
	html += '</dd>';
	html += '</dl>';
	html += '</div>';
	$.jBox(html, { title: "新增机场信息",buttons:{'取消': 0,'提交':1}, submit:function(v, h, f){
		if (v=="1"){
			return true;
		}else{
			return true;
		}
	},height:320,width:520});
	
	$('.jbox-zj').click(function(){				 
		$(this).parent().after('<div class="jbox-margin0 fl"><label class="jbox-label">机场：</label><input type="text" value="" class="fl jbox-width100"><label class="jbox-label">三字码：</label><input type="text" value="" class="fl jbox-width100"><div class="ydbz_s jbox-dle gray">删除</div></div>');					 
	});
	$('.jbox-dle').live('click',function(){
		$(this).parent().remove();									 
	});
	
	qiewei();	
}
//切位
function qiewei(){
	var qw;
	$('input[name=qiewei]').each(function(){
		if($(this).is(':checked')){
			qw=$(this).parent().index();
			$(this).parents('dt').siblings('dd').eq(qw).show().siblings('dd').hide();
		}
	
	});
	
	$('input[name=qiewei]').click(function(){
		qw=$(this).parent().index();
		$(this).parents('dt').siblings('dd').eq(qw).show().siblings('dd').hide();
	});
}

/*==============基础信息维护-交通信息-新增航空公司 begin=============*/
//选择不同的radio，进行内容的切换
function inquiry_radio_flights(){
	if($("#inquiry_radio_flights1").prop("checked")){
		$('.inquiry_flights1').show();
		$('.inquiry_flights2').hide(); $('.inquiry_flights3').hide();
	}else if($("#inquiry_radio_flights2").prop("checked")){
		$('.inquiry_flights2').show();
		$('.inquiry_flights1').hide(); $('.inquiry_flights3').hide();
	}else if($("#inquiry_radio_flights3").prop("checked")){
		$('.inquiry_flights3').show();
		$('.inquiry_flights1').hide(); $('.inquiry_flights2').hide();
	}
}
/*==============基础信息维护-交通信息-新增航空公司 end=============*/
// ----------------------------------------------------------------------
// <summary>
// 限制只能输入字母
// </summary>
// ----------------------------------------------------------------------
$.fn.onlyAlpha = function () {
    $(this).keypress(function (event) {
        var eventObj = event || e;
        var keyCode = eventObj.keyCode || eventObj.which;
        if ((keyCode >= 65 && keyCode <= 90) || (keyCode >= 97 && keyCode <= 122))
            return true;
        else
            return false;
    }).focus(function () {
        this.style.imeMode = 'disabled';
    }).bind("paste", function () {
        var clipboard = window.clipboardData.getData("Text");
        if (/^[a-zA-Z]+$/.test(clipboard))
            return true;
        else
            return false;
    });
};