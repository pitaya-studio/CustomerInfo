/*!
 * JQuery for the module of products(产品模块的脚本)
 *
 * Depends:common.js
 * the public functions used by this module:
 *
 * Date: 2015-01-13
 */
 
//产品列表 发布产品-发布产品类型选择弹框
// function jbox_productType(){
	// var html = '<div class="add_allactivity" style="padding-left:50px;"><label>产品类型：</label>';
	// html += '<select><option value="">单团</option><option value="">散拼</option><option value="">签证</option><option value="">机票</option><option value="">自由行</option></select>';
	// html += '</div>';
	// $.jBox(html, { title: "发布产品类型选择",buttons:{"预 定":"1"}, submit:function(v, h, f){
		// if (v=="1"){
			
		// }
	// },height:200,width:380});
// }

/*=============发布产品-产品基本信息-产品名称长度 begin===============*/
function getAcitivityNameLength1(sizeNum) {
	var strSize = sizeNum ? sizeNum : 200;
	var text = $("#acitivityName").val();
	if(text.length>strSize){
	    text = text.substr(0,strSize);
	    $("#acitivityName").val(text);
	}
	$(".acitivityNameSize").text(strSize-text.length);
}
/*=============发布产品-产品基本信息-产品名称长度 end===============*/

/*=============发布机票产品 begin===============*/
//基础信息填写--新增分区联运
function transportAdd(element, index){
	var $selectCurrency = $("#templateCurrency").clone();
	var html_selectCurrency = $selectCurrency.removeAttr("id").removeAttr("style")[0].outerHTML;
	var middle = 'onkeyup="this.value=this.value.replace(/[^\\d\\+\\-]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^\\d\\+\\-]/g,\'\')"';
	$(element).parent().parent().append('<p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input id="intermodalGroupPart' + (index+1) + '" name="intermodalGroupPart" maxlength="50" type="text"><label>联运价格：</label>' + html_selectCurrency +'&nbsp;<input id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="8" type="text" ' + middle + ' /><span class="currency">元</span><a class="ydbz_s gray transportDel">删除</a></p>');
	$('.transportAdd').attr('onclick', 'transportAdd(this, ' + (index + 1) + ')');
}
//基础信息填写--分区联运
function transportSelect(){
	//删除分区联运
	$(".transport_city").delegate(".transportDel","click",function(){
		$(this).parent().remove();
	});
	//联运币种选择
	$(".transport_city").on("change","select[name=selectCurrency]",function(){
		var $this = $(this);
		var theValue = $this.val();
		//var oldCurrency = $this.attr("nowClass");
		//var newCurrency = $this.children("option:selected").attr("addClass");
		var newCurrency = $this.children("option:selected").text();
		var $currency = $this.siblings(".currency").eq(0);
		if(0 != $currency.length){
			var txt_currency = newCurrency == "人民币" ? "元" : newCurrency;
			$currency.html(txt_currency);
		}
		//$this.next("input[type=text]").removeClass(oldCurrency).addClass(newCurrency);
		//$this.attr("nowClass",newCurrency);
	});
}

//基础信息填写
function planeTrip(){
	//直飞 中转 经停 切换
	$('.tripTitle label').live('click',function(){
		$('.tripTitle').find('label').each(function(index, element){
			if($(element).find('input[type=radio]').attr('checked')){
				$('.tripCen > div').eq(index).show().siblings().hide();
			}
		});						
	});
	//属地选择，国际：显示国家；国内：隐藏国家
	$("input[name='territorial']").change(function(){
		var thisvalue = $(this).val();
		var $showCountry = $("#territorialDiv");
		if("1" == thisvalue){//国际
			$showCountry.show();
		}else{//国内
			$showCountry.hide();
		}
	});
	//单程、往返、联程切换
	$("input[type='radio'][name='radio_flight']").change(function(){
		$("div[data-flight]").hide();
		$("div[data-flight='" + $(this).val() + "']").show();
	});
}
//中转 添加
function transit_add(obj,idFlag){
	var transit_html=$('.transit-bottom').html();
	var $transit = $(obj).parents('.transit');
	$transit.append('<div class="transit-cen">'+transit_html+'</div>');
	var $transitCen = $transit.find(".transit-cen");
	$transitCen.last().find('.seach25 p em').text($transitCen.length);
	//替换form元素的name和id
	var $replaceName = $transitCen.last().find("[name^='tmp0-']");
	$replaceName.each(function(index,element){
		var name = $(element).attr("name");
		var ID = $(element).attr("id");
		$(element).attr("name",name.replace("tmp0-",idFlag) + '-' + $transitCen.length);
		if(ID){
			$(element).attr("id",ID.replace("tmp0-",idFlag) + '-' + $transitCen.length);
		}
	});
}
//删除中转城市
function transit_delete(obj){
	var $transit = $(obj).parents('.transit');
	$(obj).parents('.transit-cen').remove();
	$transit.find('.transit-cen').each(function(index, element){
		var transitNum=index+1;
		$(element).find('.seach25 p em').text(transitNum);
		$(element).find("[name*='trans']").each(function(index, element) {
			var name = $(element).attr("name");
			var ID = $(element).attr("id");
			$(element).attr("name",name.replace(/\-\d+/,'-' + transitNum));
			if(ID){
				$(element).attr("id",ID.replace(/\-\d+/,'-' + transitNum));
			}
		});
	});
}

//联程 添加
function connecting_add(obj){
	var $connecting_tmp = $(obj).parents('.connecting').find('.aircraftRoute:last');
	$connecting_tmp.before('<div class="aircraftRoute">'+$connecting_tmp.html()+'</div>');
	var $newAdd = $connecting_tmp.prev(".aircraftRoute");
	var length = $(obj).parents('.connecting').find('.aircraftRoute:visible').length;
	//航段序号设置
	$newAdd.find('.tripTitle b em').text(length);
	//航段表单元素name/ID设置
	var $replaceName = $newAdd.find("[name*='0-']");
	$replaceName.each(function(index, element) {
		if("tripConnecting0-" == $(element).attr("name")){
			$(element).attr("name","tripConnecting" + length);
		}else{
			var name = $(element).attr("name");
			var ID = $(element).attr("id");
			$(element).attr("name",name.replace(name.split("-")[0],name.split("-")[0].replace("0",length)));
			if(ID){
				$(element).attr("id",ID.replace(ID.split("-")[0],ID.split("-")[0].replace("0",length)));
			}
		}
	});
	//connecting_Num();	
}
//删除联程
function connecting_deleat(obj){
	$(obj).parents('.aircraftRoute').remove();
	$('.connecting').find('.aircraftRoute').each(function(index, element){
		if($(element).is(":visible")){
			var connectingNum=index+1;
			$(element).find('.tripTitle b em').text(connectingNum);
			//航段表单元素name/ID设置
			$(element).find("[name^='tripConnecting']").attr("name","tripConnecting" + (index+1));
			var $replaceName = $(element).find("[name]");
			$replaceName.each(function(index, element) {
				var name = $(element).attr("name");
				var ID = $(element).attr("id");
				$(element).attr("name",name.replace(/(^[a-zA-z]+)\d+\-/,"$1"+connectingNum+"-"));
				if(ID){
					$(element).attr("id",ID.replace(/(^[a-zA-z]+)\d+\-/,"$1"+connectingNum+"-"));
				}
			});
		}
	});
}
//机场选择
function airportClick(tarObj){
	var paraname=$(tarObj).attr("id");
	// 是否限制选择，如果限制，设置为disabled
	if ("" == "disabled"){
		return true;
	}
	// 正常打开	
	top.$.jBox.open("iframe:http://192.168.1.203:8080/a/tag/treeselect?url="+encodeURIComponent("/airTicket/filterAirportInfoData")+"&module=&checked=&extId=&selectIds="+$("#"+paraname+"Id").val(), "选择机场", 300, 420,{
		buttons:{"确定":"ok", "关闭":true}, submit:function(v, h, f){
			if (v=="ok"){
				var tree = h.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
				var ids = [], names = [], nodes = [];
				if ("" == "true"){
					nodes = tree.getCheckedNodes(true);
				}else{
					nodes = tree.getSelectedNodes();
				}
				for(var i=0; i<nodes.length; i++) {//
					ids.push(nodes[i].id);
					names.push(nodes[i].name);//
					break; // 如果为非复选框选择，则返回第一个选择  
				}
				$("#"+paraname+"Id").val(ids);
				$("#"+paraname+"Name").val(names);
				$("#"+paraname+"Name").focus();
				$("#"+paraname+"Name").blur();
			}//
		}, loaded:function(h){
			$(".jbox-content", top.document).css("overflow-y","hidden");
		},persistent:true
	});
}

//价格--币种选择
function selectCurrencyAir(){
	//初始化币种符号
	var initCurrency = $(this).children("option:selected").attr("addClass");
	$(".planeTick-table").find("input.ipt-currency").prev().text(initCurrency);
	$(".sel-currency").change(function(){
		var newCurrency = $(this).children("option:selected").attr("addClass");
		$(".planeTick-table").find("input.ipt-currency").prev().text(newCurrency);
	});
}
//是否分段报价
function flyDivCheck(){
	if($("input[name='flyDivInput']").prop("checked")){
		$('.flyMoreDiv').show();
	}else{
		$('.flyMoreDiv').hide();
	}
}
/*=============发布机票产品 end===============*/
 
/*=============发布单团、散拼、海岛产品 begin===============*/
//基础信息填写-交通方式-航空
function trafficchg(){
	var value=$("#trafficMode option:selected").val();
	if("1,".indexOf(value)>=0&&value!=""){
		var $trafficName = $("#trafficName");
		$trafficName.css("display","inline-block");
		$(".lianyun_select .xing").show();
		//海岛游
		$trafficName.parent().next().next(".relatedInformation").show();
		//单团、散拼
		$trafficName.next(".linkAir").show();
		if($trafficName.next().find(".airInfo-arrow").is(":visible")){
			$trafficName.parent().next(".airInfo").show();
		}
	}
	else {
		var $trafficName = $("#trafficName");
		$trafficName.css("display","none");
		$(".lianyun_select .xing").hide();
		$("#trafficName option[value='']").attr("selected", true);
		//海岛游
		$trafficName.parent().next().next(".relatedInformation").hide();
		//单团、散拼
		$trafficName.next(".linkAir").hide();
		$trafficName.next().find(".airInfo-arrow").hide();
		$trafficName.parent().next(".airInfo").hide();
	}
}
//基础信息填写-交通方式-航空-关联机票产品
function linkAirTicket(dom){
	var html = '<div class="add_allactivity"><label>输入机票产品编号：</label>';
	html += '<input type="text" />';
	html += '</div>';
	$.jBox(html, { title: "选择机票产品",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
			h.find('.nothisPro').hide('fast', function () { $(this).remove(); });
			//如果没有机票产品
			//$('<p class="nothisPro" style="display: none;">没有这个产品</p>').appendTo(h).show('slow');
			//return false;
			//如果有机票产品
			if(dom){
				$(dom).parents(".mod_information_d2").next(".airInfo").show();
				//散拼和单团产品
				$(dom).next(".linkAir").show().find(".airInfo-arrow").show();
				$(dom).parents(".mod_information_d2").next(".airInfo").show();
			}
		}
	},height:180,width:500});
}
//基础信息填写-住宿选择-酒店
function residencechg(){
	var value=$("#residenceMode option:selected").val();
	if("1,".indexOf(value)>=0&&value!=""){
		var $residenceName = $("#residenceName");
		$residenceName.css("display","inline-block");
		//海岛游
		$residenceName.parent().next().next(".relatedInformation").show();
		//单团、散拼
		$residenceName.next(".linkAir").show();
		if($residenceName.next().find(".airInfo-arrow").is(":visible")){
			$residenceName.parent().next(".airInfo").show();
		}
	}
	else {
		var $residenceName = $("#residenceName");
		$("#residenceName").css("display","none");
		//海岛游
		$residenceName.parent().next().next(".relatedInformation").hide();
		//单团、散拼
		$residenceName.next(".linkAir").hide();
		$residenceName.next().find(".airInfo-arrow").hide();
		$residenceName.parent().next(".airInfo").hide();
	}
}
//基础信息填写-交通方式-航空-关联机票产品
function hdlinkAirTicket(dom){
	var html = '<div style="padding:30px 20px 20px 20px;">';
	html += '<label class="jbox-label ml20">到达城市：</label><select class="fl jbox-width100"><option>请选择</option><option>纽约</option><option>法国</option><option>意大利</option></select>';
	html += '<label class="jbox-label ml20">机票类型：</label><select class="fl jbox-width100"><option>请选择</option><option>单程</option><option>往返</option><option>联程</option></select><div class="kongr" style="height:15px;"></div>';
	html += '<label class="jbox-label ml20">航程类型：</label><select class="fl jbox-width100"><option>直飞</option><option>中转</option><option>经停</option></select>';
	html += '<label class="jbox-label ml20">机票产品编号：</label><input type="text" class="fl jbox-width100" />';
	html += '</div>';
	$.jBox(html, { title: "选择机票产品",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
			h.find('.nothisPro').hide('fast', function () { $(this).remove(); });
			//如果没有机票产品
			//$('<p class="nothisPro" style="display: no ne;">没有这个产品</p>').appendTo(h).show('slow');
			//return false;
			//如果有机票产品
			if(dom){
				$(dom).parents(".mod_information_d2").next().next(".relatedInformation").show();
			}
		}
	},height:230,width:530});
}
//基础信息填写-交通方式-航空-关联酒店产品
function linkHotel(dom){
	$.jBox("id:jboxLinkHotel", { title: "选择酒店产品",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
			h.find('.nothisPro').hide('fast', function () { $(this).remove(); });
			//如果没有机票产品
			//return false;
			//如果有机票产品
			if(dom){
				$(dom).parents(".mod_information_d2").next().next(".relatedInformation").show();
				//散拼和单团产品
				$(dom).next(".linkAir").show().find(".airInfo-arrow").show();
				$(dom).parents(".mod_information_d2").next(".airInfo").show();
			}
		}
	},height:500,width:740});
}
//基础信息填写-交通方式-航空-关联酒店产品-新增酒店
function jboxAddHotel(dom){
	$(dom).before($("#TemplateLinkHotel").html());
	var $parent = $("#jbox-content");
	$(dom).prev().find("[data='nameOrder']").text($parent.find(".linkHotel").length);
}
//基础信息填写-交通方式-航空-关联酒店产品-删除酒店
function jboxDelHotel(dom){
	var $parent = $("#jbox-content");
	$(dom).parent().remove();
	$parent.find(".linkHotel").each(function(index, element) {
        $(element).find("[data='nameOrder']").text(index+1);
    });
}
//海岛-基础信息填写-关联信息删除
function showDelete(obj){
	$(obj).parents('.showText').remove();
}

//添加团期和价格--币种选择
function selectCurrency(){
	//单个设置币种
	//$(".choose-currency").hover(function(){
		//$(this).addClass("choose-currency-on");
	//},function(){
		//$(this).removeClass("choose-currency-on");
	//}).on("click","dd p",function(){
	$(".mod_information").on("hover",".choose-currency",function(){
		$(this).addClass("choose-currency-on");
	}).on("mouseleave",".choose-currency",function(){
		$(this).removeClass("choose-currency-on");
	}).on("click",".choose-currency dd p",function(){
		var $this = $(this);
		if(!$this.hasClass("p-checked")){
			var oldCurrency = $this.siblings("p.p-checked").text();
			var oldClass = $this.siblings("p.p-checked").attr("addClass");
			$this.addClass("p-checked").siblings("p").removeClass("p-checked");
			//设置币种/人
			var $currency = $this.parents(".add2_nei_table").next("td").find(".currency").eq(0);
			if(0 != $currency.length){
				var txt_currency = $this.text() == "人民币" ? "元" : $this.text();
				if("人民币" == oldCurrency){oldCurrency = "元";}//alert(oldCurrency);
				$currency.html($currency.html().replace(oldCurrency,txt_currency));
			}
			//设置币种图标
			var $input_currency = $this.parents(".add2_nei_table").next("td").find(".ipt-currency").eq(0);
			//$input_currency.removeClass(oldClass).addClass($this.attr("addClass"));
			$input_currency.prev().text($this.attr("addClass"));
			//设置对应的select的选中项
			var $select = $this.parents("td.add2_nei_table").find("select");
			$select.find("option:selected").removeAttr("selected");
			$select.children("option[value=" + $this.attr("value") + "]").attr("selected",true);
		}
	});
	//统一设置币种
	$("#selectCurrency").change(function(){
		var theValue = $(this).val();
		$.each($(".choose-currency"),function(index,element){
			var $this_dl = $(element);
			$this_dl.find("dd p[value=" + theValue + "]").click();
		});
	});
}
/*=============发布单团、散拼、海岛产品 end===============*/

/*=============发布酒店产品--添加团期和价格 begin===============*/
//币种选择
function selectCurrencyHotel(){
	selectCurrency();
	//统一设置币种
	//$(".selectCurrency").change(function(){
	$(".mod_information").on("change",".selectCurrency",function(){
		var theValue = $(this).val();
		var theDiv=$(this).parents(".add2_hotel").find(".choose-currency");
		$.each(theDiv,function(index,element){
			var $this_dl = $(element);
			$this_dl.find("dd p[value=" + theValue + "]").click();
		});
		var theIValue=$(this).parents(".add2_hotel").find("i[name='unit']");
		var newCurrency=$(this).find("option:selected").text().toString().split("　")[0];
		var newCurrency = newCurrency == "人民币" ? "元" : newCurrency;
		$.each(theIValue,function(index,element){
			var oldCurrency	=$(element).text().toString().split("/")[0];
			$(element).text($(element).text().replace(oldCurrency,newCurrency));
		})
		
	});
}
//是否优惠
function isDiscount(){
	$('.add2-isdiscount').find('input[type="checkbox"]').each(function(index, element){
		if($(element).attr('checked')){
			$('.add2-isdiscountCon p').eq(index).show();
		}else{
			$('.add2-isdiscountCon p').eq(index).hide();
		}
	});		
}
/*保存并添加房型*/
function groupHotelAdd(obj){
	var addGroup=$(obj).parents(".add2_hotel")
	var htmls='<div class="add2_information_dzhan add2_hotel" id="secondStepContent"><div class="hotel_del"><a onclick="hotelDel(this)">删除</a></div>'+addGroup.formhtml()+'</div>'
	addGroup.parent().append(htmls)
	}
/*删除房型*/
function hotelDel(obj){
	$(obj).parents('.add2_hotel').remove();
}
/*=============发布酒店产品--添加团期和价格 end===============*/

/*=============发布签证产品--价格 begin===============*/
//币种选择
function selectCurrencyVisa(){
	//币种初始化
	$(".sel-currency").each(function(index,element){
		var theValue = $(element).val();
		var newCurrency = $(element).children("option:selected").attr("addClass");
		$(element).parent().parent().parent().find("input.ipt-currency").prev().text(newCurrency);
	});
	$(".sel-currency").change(function(){
		var theValue = $(this).val();
		//var oldCurrency = $(this).attr("nowClass");
		var newCurrency = $(this).children("option:selected").attr("addClass");
		//$(this).parent().parent().parent().find("input.ipt-currency").removeClass(oldCurrency).addClass(newCurrency);
		$(this).parent().parent().parent().find("input.ipt-currency").prev().text(newCurrency);
		//$(this).attr("nowClass",newCurrency);
	});
}
/*=============发布签证产品--价格 end===============*/

/*=============发布单团、散拼、机票、海岛、酒店、签证产品--上传资料 begin===============*/
//删除签证资料文件
function removefile(msg,obj){
	top.$.jBox.confirm(msg,'系统提示',function(v){
		if(v=='ok'){
			var divobj = $(obj).parent().parent().parent().parent().parent();
			$(divobj).remove();
			//$(obj).parent().remove();
		}
	},{buttonsFocus:1});
	return false;           
}
//发布产品3步-添加其他文件
// function addfile(obj){
	// var file = "<div style=\"margin-top:8px;\">"+$("#othertemplate").clone().html()+"</div>";
	// $(obj).parent().parent().append(file);
// }
//发布产品3步-添加签证资料
function addvisafile(obj){
	var html = "<div class=\"mod_information_d8_2\" style=\"margin-top:5px;\">"+$("#signtemplate").clone().html()+"</div>";
	$("#visaflag").after(html);
	$("#thirdStepDiv .mod_information_d8_2:first select[name='country']").comboboxSingle();
}
//删除现有的文件
function deleteFileInfo(inputVal, objName, obj) {
	top.$.jBox.confirm('确定要删除该文件？','系统提示',function(v){
		if(v=='ok'){
			if(inputVal != null && objName != null) {
				// var delInput = $(".uploadPath").find("input[name='"+ objName +"'][value='"+ inputVal +"']");
				// delInput.next().eq(0).remove();
				// delInput.next().eq(0).remove();
				// delInput.remove();
				
				// /*删除上传文件后，文件信息会存放在id为currentFiles的div中，也需要把该div相关的上传文件信息删除*/
				// var docName = $(obj).parent("li").parent("ol").parent().find("#currentFiles").find("input[name='"+ objName +"'][value='"+ inputVal +"']");
				// docName.next().eq(0).remove();
				// docName.next().eq(0).remove();
				// docName.remove();
			}else if(inputVal == null && objName == null) {
				$(obj).parent().remove();
			}
			$(obj).parent("li").remove();
			
			//如果是产品行程介绍文件删除的话，需要进行必填验证
			// if("introduction" == objName) {
				// if(0 == $("#introductionVaildator").next().next(".batch-ol").find("li").length) {
					// $("#introductionVaildator").val("");
				// }
			// }
		}
	},{buttonsFocus:1});
	top.$('.jbox-body .jbox-icon').css('top','55px');		
}
//发布选择：点击“提交发布按钮”的弹框
function jboxReleaseSelect(){
	var html = '<div class="releaseSelect">';
	html += '<span><input type="checkbox" name="releaseType" id="releaseType1" /><label for="releaseType1">全部可见</label></span>';
	html += '<span><input type="checkbox" name="releaseType" id="releaseType2" /><label for="releaseType2">内部可见</label></span>';
	html += '<span><input type="checkbox" name="releaseType" id="releaseType3" /><label for="releaseType3">渠道可见</label></span>';
	html += '</div>';
	$.jBox(html, { title: "发布选择",buttons:{'提交': true }, submit:function(v, h, f){
		if (v==true){
			return true;
		}
	},height:170,width:350});
}
/*=============发布单团、散拼、机票、海岛、酒店、签证产品--上传资料 end===============*/

/*=============发布单团、散拼、机票、海岛、酒店、签证产品--付款方式 begin===============*/
//付款方式
/*
function paychg(obj) {
	if($(obj).prop("checked")){
		$(obj).next().next().find("span").css("display","inline");
		//$(obj).next().next().next().find("input[name^='remainDays']").rules("remove");
		$(obj).next().next().nextUntil('br').find("input[name^='remainDays']").removeAttr("disabled");
		//$(obj).next().next().next().find("input[name^='remainDays']").rules("add",{required:true,digits:true,nonzero:true,messages:{required:"必填信息",digits:"请输入正确的数字",nonzero:"输入不能为0"}});
	}else{
		$(obj).next().next().find("span").css("display","none");
		//$(obj).next().next().next().find("input[name^='remainDays']").rules("remove");
		$(obj).next().next().nextUntil('br').find("input[name^='remainDays']").val("");
		$(obj).next().next().nextUntil('br').find("input[name^='remainDays']").attr("disabled","disabled");
	}
}
*/
function paychg(obj) {
	if($(obj).prop("checked")){
		$(obj).next().next().find("span").css("display","inline");
		//$(obj).next().next().nextUntil('br').find("input[name^='remainDays']").rules("remove");
		$(obj).next().next().nextUntil('br').find("input[name^='remainDays']").removeAttr("disabled");
		//$(obj).next().next().nextUntil('br').find("input[name^='remainDays']").rules("add",{required:true,digits:true,nonzero:true,messages:{required:"必填信息",digits:"请输入正确的数字",nonzero:"输入不能为0"}});
	}else{
		$(obj).next().next().find("span").css("display","none");
		//$(obj).next().next().nextUntil('br').find("input[name^='remainDays']").rules("remove");
		$(obj).next().next().nextUntil('br').find("input[name^='remainDays']").val("");
		$(obj).next().next().nextUntil('br').find("input[name^='remainDays']").attr("disabled","disabled");
	}
}
// 判断，如果支付方式没有被选中，则清空等待时间
function clearValue(){
	$("input[name='payMode']").each(function(){
		if($(this).prop("checked")){
			$(this).next().next().find("span.xing").show();
		}else{
			$(this).next().next().nextAll().find("input[name^='remainDays']").val("");
			$(this).next().next().nextAll().find("input[name^='remainDays']").attr("disabled","disabled");
			$(this).next().next().find("span.xing").hide();
		}
	});
}
/*=============发布单团、散拼、机票、海岛、酒店、签证产品--付款方式 end===============*/

/*=============单团、散拼产品列表页-团期修改 begin===============*/
//产品列表-修改产品
function savegroup(srcActivityId,modbtn,delbtn,cancelbtn,obj,childform,childtr,url){
	//显示文字、隐藏文本框
	$(obj).parent().parent().find("span").show();
	$(obj).parent().parent().find("input[type='text']").css("display","none");
   	//$(obj).parent().parent().find("input[type='text']").attr("disabled",true);
	//操作列文字切换
	$(modbtn).show();
	$(delbtn).show();
	$(cancelbtn).hide();
	$(obj).hide();
}
// 删除确认对话框
function confirmxCopy(mess,id,proId,obj,child){
	top.$.jBox.confirm(mess,'系统提示',function(v){
		if(v=='ok'){			
			$(obj).parent().parent().remove();
			if($("#"+child+" tbody").find("tr").length==0){
				$("#"+child).hide();
				$("#groupdate"+proId).removeClass("td-extend");
				$("#groupdate"+proId).parent("tr").removeClass("tr-hover");
			}
		}
	},{buttonsFocus:1});
	return false;
}
//修改
function modgroup(groupid,savebtn,delbtn,cancelbtn,obj){    	
	$(obj).parent().parent().find("span").hide();
	$(obj).parent().parent().find("span").eq(0).show();
	$(obj).parent().parent().find("span.rm").show();
	$(obj).parent().parent().find("input[type='text']").css("display","inline-block");
	//$(obj).parent().parent().find("input[type='text']").attr("disabled",false);
	$(groupid).hide();
	//$(groupid).attr("disabled",false);
	$(savebtn).show();
	$(delbtn).hide();
	$(cancelbtn).show();
	$(obj).hide();
}
//取消修改
function cancelgroup(modbtn,savebtn,delbtn,obj){
	$(modbtn).show();
	$(savebtn).hide();
	$(delbtn).show();
	$(obj).hide();
	$(obj).parent().parent().find("span").show();
	$(obj).parent().parent().find("input[type='text']").css("display","none");
	//$(obj).parent().parent().find("input[type='text']").attr("disabled",true);
	//$(obj).parent().parent().find("label",".error").remove();
}
/*=============单团、散拼产品列表页-团期修改 end===============*/