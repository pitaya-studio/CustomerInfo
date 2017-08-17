<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>快速生成订单</title>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link href="${ctxStatic}/css/j.suggest.css" rel="stylesheet" type="text/css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/default.validator.js"></script>
<script src="${ctxStatic}/js/input-comp/j.dimensions.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/input-comp/j.suggest.js" type="text/javascript"></script>
<script type="text/javascript">
var orderInfo= {
		   			traveller: [],
				    // 订单总额
        			totalCost: {},
        		    // 应收金额 
        		    accounts: {},
        		    // 已收金额
        			receipted: {},
        		    // 总人数
        			totalCount: 0
		   		};
//房型添加或减少
function hotel_room_xing_add_con() {
    var r_c_add = $('.hotel_room_xing_add_con').html();
    $('.hotel_room_xing_add_con').after(r_c_add);
}
function hotel_room_xing_del_con(obj) {
    $(obj).parent().remove();
}

//附件管理弹窗
function up_files_pop(obj) {
	var up_files_pop = $(obj).parent().find(".up_files_pop");
	var html = '<div style="min-width:300px;margin:0 auto;padding:20px;" class="up_files_pop">';
	html += up_files_pop.html();
	html += '</div>';
	$.jBox(html, {
		title: "附件管理", buttons: { '关闭': 1 }, submit: function (v, h, f) {
			if (v == "1") {
				up_files_pop.html($(h).find(".up_files_pop").html());
				return true;
			}
		}, width: 400
	});
}

function commenFunction(obj,fileIDList,fileNameList,filePathList) {
	var fileIdArr = fileIDList.split(";");
	var fileNameArr = fileNameList.split(";");
	var filePathArr = filePathList.split(";");
	for(var i=0; i<fileIdArr.length-1; i++) {
		var html = '<li><a class="padr10" href="javascript:void(0)" onclick="downloads('+ fileIdArr[i] +')">'+ fileNameArr[i] +'</a>';
		html += '<span class="tdred" style="cursor:pointer;" onclick="deleteFileInfo(null,\'hotelFeaturesAnnexDocId\',this)">删除</span>';
		html += '<input type="hidden" name="hotelTraveler_files" />';
		html += '<input type="hidden" name="docId" value="' + fileIdArr[i] + '" />';
		html += '<input type="hidden" name="docName" value="'+ fileNameArr[i] +'"/>';
		html += '<input type="hidden" name="docPath" value="'+ filePathArr[i] +'"/>';
		html += '</li>';
		$(obj).parent().find("ul").append(html);
	}
}

//下载文件
function downloads(docid){
	window.open("${ctx}/sys/docinfo/download/"+docid);
}

//删除现有的文件
function deleteFileInfo(inputVal, objName, obj) {
	top.$.jBox.confirm('确定要删除该文件？','系统提示',function(v){
		if(v=='ok'){
			if(inputVal != null && objName != null) {
				var delInput = $(".uploadPath").find("input[name='"+ objName +"'][value='"+ inputVal +"']");
				delInput.next().eq(0).remove();
				delInput.next().eq(0).remove();
				delInput.remove();
				
				/*删除上传文件后，文件信息会存放在id为currentFiles的div中，也需要把该div相关的上传文件信息删除*/
				var docName = $(obj).parent("li").parent("ol").parent().find("#currentFiles").find("input[name='"+ objName +"'][value='"+ inputVal +"']");
				docName.next().eq(0).remove();
				docName.next().eq(0).remove();
				docName.remove();
			
				
			}else if(inputVal == null && objName == null) {
				$(obj).parent().remove();
			}
			$(obj).parent("li").remove();

		}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');		
}

//保存游客信息
function save_tours_obj(obj) {
    if ($(obj).text() == "保存") {
        $('#passengerInfoTable tr').has(obj).find('input,select').attr('disabled', 'disabled');
        $(obj).text('修改');
    } else {
        $('#passengerInfoTable tr').has(obj).find('input,select').removeAttr('disabled');
        $(obj).text('保存');
    }
}
//删除游客信息
function del_tours_obj(obj) {
    $('#passengerInfoTable tr').has(obj).remove();
    updateSequence();
}
$(document).ready(function () {
    $('.acitivityName').on('keyup', function () {
        var maxlength = $(this).attr('maxlength');
        var $sizeElement = $(this).parent().find('.acitivityNameSize');
        if($sizeElement && maxlength){
            var remainLength = parseInt( maxlength)- $(this).val().length;
            $sizeElement.text(remainLength);
        }
    });
});
//添加游客信息
var idGen = 1;
function add_tours_obj() {
    //if ($('#passengerInfoTable tbody tr').length >= orderInfo.totalCount) return;
	//$('#passengerInfoTable tr').find('input,select').removeAttr('disabled');
	if ($('#passengerInfoTable tbody tr').length >= orderInfo.totalCount){
        var vselno = $("#selNo").val();
        if(vselno=='1'){
        	top.$.jBox.tip("同行价及人数必须成对填写,且至少填写一个游客类型的同行价及人数");
			return;
        }else{
		    $.jBox.tip("请输入游客人数");
	    	return;
        }
    } 
	$('#passengerInfoTable').show();
	var htmlstr = $('#add_tours_obj_tr').html().replace("id=\"suggestcountry\"","id=\"suggestcountry"+idGen+"\"");
	htmlstr = htmlstr.replace("id=\"country\"","id=\"country"+idGen+"\"");
	htmlstr = htmlstr.replace("id=\"Divsuggestcountry\"","id=\"Divsuggestcountry"+idGen+"\"");
	htmlstr = htmlstr.replace("add_tours_obj_tr_index", idGen);
	var html='<tr class="passenger_info_tr">'+htmlstr+'</tr>';
	$('#passengerInfoTable tbody').append(html);
	idGen++;
	updateSequence();
	$("select[name=hotelTraveler_personType]").each(function(){
	    $(this).change();
	});
}
function updateSequence() {
        $("#passengerInfoTable").children("tbody").children("tr").each(function (i) {
            $(this).children("td").first().text(i+1);
        });
    }
$(function(){
	$("#orderCompany").comboboxInquiry().on('comboboxinquiryselect', function(event, obj){
	    	var channelType = $("#channelType").val();
	    	var agentId = $("#orderCompany").val();
	    	if(channelType == '1' && agentId != '') {
	    		$.post("${ctx}/islandOrder/loadAgentInfo",{"id":agentId }, function(data){
	    			if(data.agentinfo){
	    				//初始化渠道商联系人信息
	    				var signChannel = $("#signChannelList ul").eq(0);
	    				signChannel.find("input[name=orderContacts_contactsName]").val(data.agentinfo.agentContact);
	    				signChannel.find("input[name=orderContacts_contactsTel]").val(data.agentinfo.agentContactMobile);
	    				signChannel.find("input[name=orderContacts_contactsTixedTel]").val(data.agentinfo.agentContactTel);
	    				signChannel.find("input[name=orderContacts_contactsAddress]").val(data.agentinfo.agentAddress);
	    				signChannel.find("input[name=orderContacts_contactsFax]").val(data.agentinfo.agentContactFax);
	    				signChannel.find("input[name=orderContacts_contactsQQ]").val(data.agentinfo.agentContactQQ);
	    				signChannel.find("input[name=orderContacts_contactsEmail]").val(data.agentinfo.agentContactEmail);
	    				signChannel.find("input[name=orderContacts_contactsZipCode]").val(data.agentinfo.agentPostcode);
	    				signChannel.find("input[name=orderContacts_remark]").val(data.agentinfo.remarks);
	    			}
	    		});
	    	}
	});
	
	
    initSuggest("${ctx}/geography/getAllConListAjax",{});
	initSuggestClass("${ctx}/geography/getAllConListAjax",{});
	
	$("#groupSelect").comboboxInquiry().on('comboboxinquiryselect', function(event, obj){
     	getActivityAndGroupInfo(this); 
    }); 
    
    $("#groupSelect1").comboboxInquiry().on('comboboxinquiryselect', function(event, obj){
    });
    
    $("#groupSelect2").comboboxInquiry().on('comboboxinquiryselect', function(event, obj){
    });
	
	$(".suggest").each(function(i,o){
		var id = $(this).attr("id");
		var name = $(this).attr("name");
		/* $(this).parent().append('<input id="'+id.replace("suggest","")+'" type="hidden" name="'+name.replace("suggest","")+'" value="">');
		$(this).parent().append('<span id="Div'+id+'" class="suggest_ac_results ac_results" style="top: 363px; left: 168px; display: none;"></span>'); */
	});
					
    $(document).on("click",".suggest",function(){
    	var id = $(this).attr("id");
		var name = $(this).attr("name");
    	$(this).suggest(countrys, {
			hot_list : commoncountrys,
			dataContainer : '#'+id.replace("suggest",""),
			attachObject : "#Div"+id
		});
    }).on("mouseover",".suggest",function(){
    	var id = $(this).attr("id");
		var name = $(this).attr("name");
    	$(this).suggest(countrys, {
			hot_list : commoncountrys,
			dataContainer : '#'+id.replace("suggest",""),
			attachObject : "#Div"+id
		});
    });
});
</script>
<script type="text/javascript">
$(function () {
    //自备签类型
    ydbz2zibeiqian();
    //是否占位联运
    ydbz2interradio();
    //新增游客
    $("#addTraveler").click(function () {
        var $table = $("#travelerTemplate").children();
        var _travelerForm = $table.clone().addClass("travelerTable");
        _travelerForm.find("input[name='personType']").attr("name", "personTypeCount" + count++);
        $("#traveler").append(_travelerForm);
        //dodatePicker();
    });
    
});
//得到焦点事件：隐藏填写费用名称提示
function payforotherIn(doc) {
    var obj = $(doc);
    obj.siblings(".ipt-tips2").hide();
}
//失去焦点事件：如果输入框中没有值，则提示填写费用名称

function payforotherOut(doc) {
    var obj = $(doc);
    if (!obj.val()) {
        obj.siblings(".ipt-tips2").show();
    }
}
//点击提示错误信息中 "修改" 后错误输入框得到焦点

function focusIpt(doc) {
    $(doc).parent().find('input[type=text].ipt2').trigger("focus");
}
$(function () {
    //选择联运方式
    transportSelect();
    clearValue();
    //行程天数
    $(".spinner").spinner({
        spin: function (event, ui) {
            if (ui.value > 365) {
                $(this).spinner("value", 1);
                return false;
            } else if (ui.value < 0) {
                $(this).spinner("value", 365);
                return false;
            }
        }
    });
    //小时
    $(".spinnerH").spinner({
        spin: function (event, ui) {
            if (ui.value > 24) {
                $(this).spinner("value", 1);
                return false;
            } else if (ui.value < 0) {
                $(this).spinner("value", 24);
                return false;
            }
        }
    });
    //分钟
    $(".spinnerS").spinner({
        spin: function (event, ui) {
            if (ui.value > 59) {
                $(this).spinner("value", 1);
                return false;
            } else if (ui.value < 0) {
                $(this).spinner("value", 59);
                return false;
            }
        }
    });
});
//付款方式
function paychg(obj) {
    if ($(obj).prop("checked")) {
        $(obj).next().next().find("span").css("display", "inline");
        //$(obj).next().next().nextUntil('br').find("input[name^='remainDays']").rules("remove");
        $(obj).next().next().nextUntil('br').find("input[name^='remainDays']").removeAttr("disabled");
        //$(obj).next().next().nextUntil('br').find("input[name^='remainDays']").rules("add",{required:true,digits:true,nonzero:true,messages:{required:"必填信息",digits:"请输入正确的数字",nonzero:"输入不能为0"}});
    } else {
        $(obj).next().next().find("span").css("display", "none");
        //$(obj).next().next().nextUntil('br').find("input[name^='remainDays']").rules("remove");
        $(obj).next().next().nextUntil('br').find("input[name^='remainDays']").val("");
        $(obj).next().next().nextUntil('br').find("input[name^='remainDays']").attr("disabled", "disabled");
    }
}
// 判断，如果支付方式没有被选中，则清空等待时间

function clearValue() {
    $("input[name='payMode']").each(function () {
        if ($(this).prop("checked")) {
            $(this).next().next().find("span.xing").show();
        } else {
            $(this).next().next().nextAll().find("input[name^='remainDays']").val("");
            $(this).next().next().nextAll().find("input[name^='remainDays']").attr("disabled", "disabled");
            $(this).next().next().find("span.xing").hide();
        }
    });
}
</script>
<!-- 我的脚本 -->                            
<script type="text/javascript" language="JavaScript">
//不可编辑状态    直接由下拉事件塞值
function getActivityAndGroupInfo(obj){
	if(obj.value==0){
		return;
	}
	window.location = "${ctx}/speedGenOrder/speedGenOrderRefreshPage?uuid="+obj.value+"&selNo="+$("#selNo").val();
}
//加载渠道商联系人信息
function loadAgentInfo() {
	var channelType = $("#channelType").val();
	var agentId = $("#orderCompany").val();
	if(channelType == '1' && agentId != '') {
		$.post("${ctx}/islandOrder/loadAgentInfo",{"id":agentId }, function(data){
			if(data.agentinfo){
				//初始化渠道商联系人信息
				var signChannel = $("#signChannelList ul").eq(0);
				signChannel.find("input[name=orderContacts_contactsName]").val(data.agentinfo.agentContact);
				signChannel.find("input[name=orderContacts_contactsTel]").val(data.agentinfo.agentContactMobile);
				signChannel.find("input[name=orderContacts_contactsTixedTel]").val(data.agentinfo.agentContactTel);
				signChannel.find("input[name=orderContacts_contactsAddress]").val(data.agentinfo.agentAddress);
				signChannel.find("input[name=orderContacts_contactsFax]").val(data.agentinfo.agentContactFax);
				signChannel.find("input[name=orderContacts_contactsQQ]").val(data.agentinfo.agentContactQQ);
				signChannel.find("input[name=orderContacts_contactsEmail]").val(data.agentinfo.agentContactEmail);
				signChannel.find("input[name=orderContacts_contactsZipCode]").val(data.agentinfo.agentPostcode);
				signChannel.find("input[name=orderContacts_remark]").val(data.agentinfo.remarks);
			}
		});
	}
}
/* //提交预报名
function submitSignUp() {
	$("#isReceive").val("0");
	$("#baseInfo").submit();
}
//提交并收款
function submitAndReceive() {
	$("#isReceive").val("1");
	$("#baseInfo").submit();
} */
$(function(){
	initSuggestClass("${ctx}/geography/getAllConListAjax",{});
	
	$(".suggest").each(function(i,o){
		var id = $(this).attr("id");
		var name = $(this).attr("name");
		$(this).parent().append('<input id="'+id.replace("suggest","")+'" type="hidden" name="'+name.replace("suggest","")+'" value="">');
		$(this).parent().append('<span id="Div'+id+'" class="suggest_ac_results ac_results" style="top: 363px; left: 168px; display: none;"></span>');
	});
					
    $(document).on("click",".suggest",function(){
    	var id = $(this).attr("id");
		var name = $(this).attr("name");
    	$(this).suggest(countrys, {
			hot_list : commoncountrys,
			dataContainer : '#'+id.replace("suggest",""),
			attachObject : "#Div"+id
		});
    }).on("mouseover",".suggest",function(){
    	var id = $(this).attr("id");
		var name = $(this).attr("name");
    	$(this).suggest(countrys, {
			hot_list : commoncountrys,
			dataContainer : '#'+id.replace("suggest",""),
			attachObject : "#Div"+id
		});
    });
});

function checkedMassage(obj){
 	if($(obj).val()==null || $(obj).val()==''){
 		$.jBox.tip("团号不能为空！！！");
 		/* setTimeout(function(){
 		 	obj.focus();
 		}); */
 		return;
 	}else{
		$.ajax({
			type:"post",
			url:"${ctx}/activityHotel/checkedGroup",
			data:{
				"groupCode":$(obj).val()
			},
			success:function(data){
				if(data && data.message=="true"){
					$.jBox.tip($(obj).val()+"，该团号已存在！！！");
				    setTimeout(function(){
 		 				obj.focus();
 					});
					return;
				}
			}
		});
 	}
}

//提交表单 
$(function(){
	$("#baseInfo").validate({
		rules:{
		},
		submitHandler: function(form){
			//把所有的餐型都保存起来
			var foodtype = "";
		    $("div[name='roomType']").each(function(){
				var food = "";
				$(this).find("input:checked").each(function(){
					food += $(this).val()+",";
				});
				foodtype+=food.substring(0,food.length-1)+";";
			});
			$("#allfood").val(foodtype);
		      
		    var typefrom = '${typeFrom}';
		    //检查游客类型人数
		    //1.获得费用及人数
		    var map_should = {};
		    $(".groupPrices_tr:visible").each(function(){
				var orderPriceNum = $(this).find("input[name=hotelOrderPrice_num]").val();
				var travelerType = $(this).find("input[name=hotelOrderPrice_travelerType]").val();
				orderPriceNum = (orderPriceNum == '' || orderPriceNum==null) ? 0 : orderPriceNum;
				map_should[travelerType]=orderPriceNum;
			});
		    //2.获得添加的游客类型和个数
		    var map_fact = {};
		    var map_fact_name = {};
			$("#passengerInfoTable select[name=hotelTraveler_personType] option:selected").each(function(){
				var key = $(this).val(); 
				if(key in map_fact) {
					map_fact[key] = map_fact[key] + 1;
				} else {
					map_fact[key] = 1;
					map_fact_name[key] = $(this).text();
				}
			});
			//3.进行验证
			for(var k in map_fact){
				if(!map_should[k]){
					top.$.jBox.tip("不允许添加游客类型为  "+map_fact_name[k]+" 的信息");
					return false;
				}
				if(map_should[k]<map_fact[k]){
					top.$.jBox.tip(map_fact_name[k] + " 人数不得超过  " + map_should[k]);
					return false;
				}
			}
		    
		    var vselno = $("#selNo").val();
		    if(vselno=='1'){
		    	/* var singleCurrency = $("select[name='singleCurrency']").val();
		    	if(singleCurrency==null || singleCurrency==""){
		    		$.jBox.tip("请选择单房差币种");
		    		return;
		    	}
		    	var frontCurrency = $("select[name='frontCurrency']").val();
		    	if(singleCurrency==null || singleCurrency==""){
		    		$.jBox.tip("请选择订金币种");
		    		return;
		    	} */
		    	var groupCode = $("input[name='groupCode']").val(); 
			    if(groupCode==null || groupCode==""){
			    	$.jBox.tip("请输入团号");
			    	return;
			    }
			    var activityName = $("input[name='activityName']").val(); 
			    if(activityName==null || activityName==""){
			    	$.jBox.tip("请输入产品名称");
			    	return;
			    }
			    /* var activitySerNum = $("input[name='activitySerNum']").val(); 
			    if(activitySerNum==null || activitySerNum==""){
			    	$.jBox.tip("请输入控房编号");
			    	return;
			    } */
			    if(vselno=='1'){
			    	var groupDate = $("#groupOpenDate_yes").val();
				    if(groupDate==null || groupDate==""){
				    	$.jBox.tip("请选择出团日期");
				    	return;
				    }
			    }
			    var country = $("input[name='country']").val(); 
			    if(country==null || country==""){
			    	$.jBox.tip("请选择国家");
			    	return;
			    }
			    var island = $("select[name='island']").val(); 
			    if(island==null || island==""){
			    	$.jBox.tip("请选择岛屿");
			    	return;
			    }
			    var hotel = $("select[name='hotel']").val(); 
			    if(hotel==null || hotel==""){
			    	$.jBox.tip("请选择酒店");
			    	return;
			    }
			    if(vselno=='1'){
			        if(!$("input[name='islandWay']").is(':checked')){
			    		$.jBox.tip("请选择交通方式");
			    		return;
			    	}
			    }
		    }
		    if(vselno=='1'){
			    var preOrderNum_no = $("#preOrderNum_yes").val();
			    var remNumber_no = $("#remNumber_yes").val();
			    if(remNumber_no==null || remNumber_no==""){
			    	$.jBox.tip("请输入余位数");
			    	return;
			    }
			    if((preOrderNum_no-remNumber_no)>0){
			    	$.jBox.tip("预报名人数不得大于余位数");
			    	return;
			    }
			    var roomtypeflag = false;
			    $("select[name='roomtype']").each(function(){
			    	if($(this).val()){
			    		roomtypeflag = true;
			    	}
			    });
			    if(!roomtypeflag){
			    	$.jBox.tip("请选择房型");
			    	return;
			    }
			    roomtypeflag = false;
			    $("input[name='night']").each(function(){
			    	if($(this).val()){
			    		roomtypeflag = true;
			    	}
			    });
			    if(!roomtypeflag){
			    	$.jBox.tip("请输入房型对应的晚数");
			    	return;
			    }
			    if(!$("input[name='foodtype']").is(':checked')){
		    		$.jBox.tip("请选择餐型");
		    		return;
			    }
		    }
		    if(vselno=='2'){
			    var preOrderNum_no = $("#preOrderNum_no").val();
			    var remNumber_no = $("#remNumber_no").val();
			    if((preOrderNum_no-remNumber_no)>0){
			    	$.jBox.tip("预报名人数不得大于余位数");
			    	return;
			    }
		    }
		    
		    //此处统一校验渠道联系人及联系电话
			var v_channelType = $("#channelType").val();
			var channelFlag = true;
			if(v_channelType=='1'){
				$("#signChannelList").find("input[name=orderContacts_contactsName]").each(function(){
           			if($(this).val() == '' || $(this).val()==null) {
           				$.jBox.tip("签约渠道:请输入渠道联系人");
           				$(this).focus();
           				channelFlag = false;
        				return;
           			}
           		});
           		
           		if(!channelFlag){
           			return;
           		}
           		
           		$("#signChannelList").find("input[name=orderContacts_contactsTel]").each(function(){
           			if($(this).val() == '') {
           				$.jBox.tip("签约渠道:请输入渠道联系人电话");
           				$(this).focus();
           				channelFlag = false;
        				return;
           			}
           		});
           		if(!channelFlag){
           			return;
           		}
			}else if(v_channelType=='2'){
				var v_orderCompanyName = $("input[name=orderCompanyName]").val();
				if(v_orderCompanyName=='' || v_orderCompanyName==null){
					$.jBox.tip("非签约渠道:请输入渠道名称");
					$(this).focus();
       				channelFlag = false;
       				return;
				}
				if(!channelFlag){
           			return;
           		}
				$("#nonChannelList").find("input[name=orderContacts_contactsName]").each(function(){
           			if($(this).val() == '' || $(this).val()==null) {
           				$.jBox.tip("非签约渠道:请输入渠道联系人");
           				$(this).focus();
           				channelFlag = false;
        				return;
           			}
           		});
           		if(!channelFlag){
           			return;
           		}
           		$("#nonChannelList").find("input[name=orderContacts_contactsTel]").each(function(){
           			if($(this).val() == ''|| $(this).val()==null) {
           				$.jBox.tip("非签约渠道:请输入渠道联系人电话");
           				$(this).focus();
           				channelFlag = false;
        				return;
           			}
           		});
           		if(!channelFlag){
           			return;
           		}
			}
		    
		    if(vselno=='1'){
			    //最少输入一个游客及游客对应的费用
			    var groupPriceVali = false;
			    $(".groupPrices_tr:visible").each(function(){
				 	var num = $(this).find("input[name=hotelOrderPrice_num]").val();
				 	var price = $(this).find("input[name=hotelOrderPrice_price]").val();
				 	if(num && price) {
				 	    if(num>0 && price>0){
				 	    	groupPriceVali = true;
				 	    }
				 	}else if((!num && price && price>0)||(!price && num && num>0)){
				 		groupPriceVali = false;
				 	}
				 });
				 if(!groupPriceVali) {
				 	top.$.jBox.tip("同行价及人数必须成对填写,且至少填写一个游客类型的同行价及人数");
			   		return;
				 }
			    
		    }
		    if(vselno=='2'){
			    //最少输入一个游客及游客对应的费用
			    var groupPriceVali = false;
			    $(".groupPrices_tr:visible").each(function(){
				 	var num = $(this).find("input[name=hotelOrderPrice_num]").val();
				 	if(num && num>0) {
				 	    groupPriceVali = true;
				 	} else {
				 		$(this).find("input").attr('disabled','disabled');
				 	}
				 });
				 if(!groupPriceVali) {
				 	top.$.jBox.tip("最少输入一个游客");
				 	//将文本框还原
					$(".groupPrices_tr").each(function(){
					 	$(this).find("input").removeAttr('disabled');
				 	});
			   		return;
				 }
		    }
		    
			var url = "${ctx}/speedGenOrder/save";
			/**
			拼接规则 country#visaTypeId 如： [111#222；444#555] 如有空值用“_”占位
			<td class="tc table_padings_none">
				<input type="hidden" name="hotelTraveler_visaInfo" />
				<p class="hotelTraveler_visaInfo">
					<select name="country" class="w40b">
						<option value="c89e0a6661b64d1e809d8873cf85bc80">中国</option>
						<option value="e4ea7467bdb6497fa456b6addcb8fb9a">美国</option>
					</select>
					<select name="visaTypeId" class="w40b">
						<c:forEach items="${visaTypes }" var="visa">
							<option value="${visa.id }">${visa.label}</option>
						</c:forEach>
					</select>
					<i class="price_sale_house_01"></i>
				</p>
			</td>
			*/
			$(".passenger_info_tr p.hotelTraveler_visaInfo").each(function(){
				var countryArray = $(this).find("input[name=country]");
				var visaTypeIdArray = $(this).find("select[name=visaTypeId]");
				var hotelTraveler_visaInfo = $(this).parent().find("input[name=hotelTraveler_visaInfo]");
				var visaInfoStr = "";
				for(var i=0; i<countryArray.length; i++) {
					visaInfoStr += $(countryArray[i]).val()==''?'_':$(countryArray[i]).val();
					visaInfoStr += "#";
					visaInfoStr += $(visaTypeIdArray[i]).val()==''?'_':$(visaTypeIdArray[i]).val();
					if(i != countryArray.length-1) {
						visaInfoStr += ";";
					}
				}
				hotelTraveler_visaInfo.val(visaInfoStr);
			});
			
			/**
			拼接规则 papersType#idCard#validityDate 如： [111#222#333；444#555#666] 如有空值用“_”占位
			<td>
				<input type="hidden" name="hotelTraveler_papersType" />
				<p class="hotelTraveler_papersType">
					<select name="papersType" class="w80">
						<option value="">请选择</option>
						<option value="1">身份证</option>
						<option value="2">护照</option>
						<option value="3">警官证</option>
						<option value="4">军官证</option>
						<option value="5">其他</option>
					</select> 
					<input type="text" name="idCard" class="w130 input_pad" />
					<input type="text" name="validityDate" onclick="WdatePicker()" class="dateinput required w90 input_pad" />
					<i class="price_sale_house_01"></i>
				</p>
			</td>
			*/
			$(".passenger_info_tr p.hotelTraveler_papersType").each(function(){
				var papersTypeArray = $(this).parent().find("select[name=papersType]");
				var idCardArray = $(this).parent().find("input[name=idCard]");
				var validityDateArray = $(this).parent().find("input[name=validityDate]");
				var hotelTraveler_papersType = $(this).parent().find("input[name=hotelTraveler_papersType]");
				var papersTypeStr = "";
				for(var i=0; i<papersTypeArray.length; i++) {
					if($(papersTypeArray[i]).val() == '' && $(idCardArray[i]).val()=='' && $(validityDateArray[i]).val()=='') {
						continue;
					}
					
					papersTypeStr += $(papersTypeArray[i]).val()==''?'_':$(papersTypeArray[i]).val();
					papersTypeStr += "#";
					papersTypeStr += $(idCardArray[i]).val()==''?'_':$(idCardArray[i]).val();
					papersTypeStr += "#";
					papersTypeStr += $(validityDateArray[i]).val()==''?'_':$(validityDateArray[i]).val();
					if(i != papersTypeArray.length-1) {
						papersTypeStr += ";";
					}
				}
				hotelTraveler_papersType.val(papersTypeStr);
			});
			
			/**
			拼接规则 currencyId#amount 如： [111#222;444#555] 如有空值用“_”占位
			<td class="tc">
				<input type="hidden" name="hotelTraveler_amount" />
				<p class="hotelTraveler_amount">
					<select name="currency" class="w30b">
						<c:forEach items="${currencyList }" var="currency">
							<option value="${currency.id }">${currency.currencyName}</option>
						</c:forEach>
					</select> 
					<input type="text" name="price" class="w30b " /> 
					<i class="price_sale_house_01"></i>
				</p>
			</td>
			*/
			
			$(".passenger_info_tr p.hotelTraveler_amount").each(function(){
				var currencyArray = $(this).parent().find("select[name=currency]");
				var priceArray = $(this).parent().find("input[name=price]");
				var hotelTraveler_amount = $(this).parent().find("input[name=hotelTraveler_amount]");
				var travelerAmountStr = "";
				for(var i=0; i<currencyArray.length; i++) {
					travelerAmountStr += $(currencyArray[i]).val()==''?'_':$(currencyArray[i]).val();
					travelerAmountStr += "#";
					travelerAmountStr += $(priceArray[i]).val()==''?'_':$(priceArray[i]).val();
					if(i != currencyArray.length-1) {
						travelerAmountStr += ";";
					}
				}
				hotelTraveler_amount.val(travelerAmountStr);
			});
			
			/**
			拼接规则 docId#docName#docPath 如： [111#222#333；444#555#666] 如有空值用“_”占位
			 <li>
			 	 <a class="padr10" href="javascript:void(0)" onclick="downloads()"></a>
				 <input type="hidden" name="hotelTraveler_files" />
				 <input type="hidden" name="docId" />
				 <input type="hidden" name="docName"/>
				 <input type="hidden" name="docPath"/>
			 </li>
			 */
			 
			 $("input[name=hotelTraveler_files]").each(function(){
			 	var docId = $(this).parent().find("input[name=docId]");
			 	var docName = $(this).parent().find("input[name=docName]");
			 	var docPath = $(this).parent().find("input[name=docPath]");
			 	var travelerFilesStr = "";
			 	for(var i=0; i<docId.length; i++) {
			 		travelerFilesStr += $(docId[i]).val()==''?'_':$(docId[i]).val();
					travelerFilesStr += "#";
			 		travelerFilesStr += $(docName[i]).val()==''?'_':$(docName[i]).val();
					travelerFilesStr += "#";
			 		travelerFilesStr += $(docPath[i]).val()==''?'_':$(docPath[i]).val();
			 		if(i != travelerFilesStr.length-1) {
						travelerFilesStr += ";";
					}
			 	}
			 	$(this).val(travelerFilesStr);
			 });
			 
			 //设置其他金额正负显示值
			 /**
			 <tr class="add_other_charges" style="display:none;">
				<td class="tr">金额名称：
					<input type="hidden" name="hotelOrderPrice_priceType" value="4"/>
					<input type="hidden" name="hotelOrderPrice_num" value="_" />
					<input type="hidden" name="hotelOrderPrice_activityHotelGroupPriceUuid" value="_" />
				</td>
				<td>
					<span class="tl">
						<input type="text" class="price_sale_house_w93" name="hotelOrderPrice_priceName" id="input8" maxlength="" />
					</span>
				</td>
				<td class="tr tr_other_u">
					<input name="other_u" type="radio" class="dis_inlineblock" id="u138_input" value="radio" checked="checked" data-label="增加" />
					<label for="u138_input">增加</label>
					<input name="other_u" type="radio" id="u138_input2" value="radio" data-label="减少" />
					<label for="u140_input">减少：</label>
				</td>
				<td class="tl">
					<label>
						<select name="hotelOrderPrice_currencyId" class="w80">
							<c:forEach items="${currencyList }" var="currency">
								<option value="${currency.id }" data-currency="${currency.currencyMark}">${currency.currencyName}</option>
							</c:forEach>
						</select>
					</label>
					<input type="text" class="price_sale_house_w93 price" name="hotelOrderPrice_price" id="input7" />
				</td>
				<td class="tr">备注：</td>
				<td class="tl">
					<input type="text" class="price_sale_house_w93" name="hotelOrderPrice_remark" id="input11" />
					<span class="padr10"></span>
					<i class="price_sale_house_02" onclick="del_other_charges(this);"></i>
				</td>
			</tr>
			 */
			 
			 $(".clone_other_charges").each(function(){
			 	//当选中减少时,将相应金额改成负数
			 	if($(this).find("input:radio").prop("checked") == false) {
			 		var priceBakObj = $(this).find("input[name=hotelOrderPrice_priceBak]");
			 		var priceObj = $(this).find("input[name=hotelOrderPrice_price]");
			 		if(priceBakObj.val() != '') {
			 			priceObj.val(0-priceBakObj.val());
			 		}  
			 	} else {
			 		var priceBakObj = $(this).find("input[name=hotelOrderPrice_priceBak]");
			 		var priceObj = $(this).find("input[name=hotelOrderPrice_price]");
			 		if(priceBakObj.val() != '') {
			 			priceObj.val(priceBakObj.val());
			 		}
			 	}
			 });
			 
			 //计算订单总额和结算总额(游客类型)
			 /**
			 <c:forEach items="${groupPrices }" var="groupPrice">
				<tr id="${groupPrice.uuid}">
					<td class="tc">
						<trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${groupPrice.type}"/>
						<input type="hidden" name="hotelOrderPrice_activityHotelGroupPriceUuid" value="${groupPrice.type}" />
						<input type="hidden" name="hotelOrderPrice_remark" value="_"/>
						<input type="hidden" name="hotelOrderPrice_priceType" value="1" />
						<input type="hidden" name="hotelOrderPrice_priceName" value="_" />
					</td>
					<td class="tc">
						<span><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groupPrice.currencyId }"/>${groupPrice.price }</span>
						<input type="hidden" name="hotelOrderPrice_currencyId" value="${groupPrice.currencyId }" />
						<input type="hidden" name="hotelOrderPrice_price" value="${groupPrice.price }" />
					</td>
					<td class="tc"><input type="text" class="price_sale_house_w100" name="hotelOrderPrice_num"/></td>
					<td class="tc"><span></span>
					</td>
				</tr>
			</c:forEach>
			 */
			 var currencyIdArr = [];
			 var priceArr = [];
			 $(".groupPrices_tr:visible").each(function(){
			 	if($("#selNo").val()=='1'){
				 	var currency = $(this).find("select[name=hotelOrderPrice_currencyId]").val();
				 	currencyIdArr.push(currency);   
			 	}else{
			 	 	var currency = $(this).find("input[name=hotelOrderPrice_currencyId]").val();
			 		currencyIdArr.push(currency);                       
			 	}
			 	var price = $(this).find("input[name=hotelOrderPrice_price]").val();
			 	var num = $(this).find("input[name=hotelOrderPrice_num]").val();
			 	priceArr.push(price*num);
			 });
			 //订单总额
			 $("#total_currency").val(currencyIdArr.join(";"));
			 $("#total_amount").val(priceArr.join(";"));
			 //计算结算总额(包括订单总额、优惠金额、退款金额和其他费用)
			 $("#add_other_charges_table").find("tr.calc_amount_tr").each(function(i){
			 	var currency = $(this).find("select[name=hotelOrderPrice_currencyId]").val();
			 	currencyIdArr.push(currency);
			 	var price = $(this).find("input[name=hotelOrderPrice_price]").val();
			 	if(i < 1) {
			 		priceArr.push(0 - price);
			 	} else {
			 		priceArr.push(price);
			 	}
			 });
			//结算总额
			$("#result_currency").val(currencyIdArr.join(";"));
			$("#result_amount").val(priceArr.join(";"));
			//应收金额
			$("#to_receive_currency").val(currencyIdArr.join(";"));
			$("#to_receive_amount").val(priceArr.join(";"));
			
			if($("#passengerInfoTable").is(":visible")) {
				$('#passengerInfoTable tr').find('input,select').removeAttr('disabled');
			} else {
				$('#passengerInfoTable tr').find('input,select').attr('disabled','disabled');
			}
			
			
			//过滤掉人数为空的团期价格
			 $(".groupPrices_tr:visible").each(function(){
			 	var num = $(this).find("input[name=hotelOrderPrice_num]").val();
			 	var vv_price = $(this).find("input[name=hotelOrderPrice_price]").val();
			 	if(num == '' || num == 0) {
			 		$(this).find("input").attr('disabled','disabled');
			 		$(this).find("select").attr('disabled','disabled');
			 	}
			 	if(vv_price == '' || vv_price == 0) {
			 		$(this).find("input").attr('disabled','disabled');
			 		$(this).find("select").attr('disabled','disabled');
			 	}
			 	
			 });
			var flag = true;
			/* //金额格式校验
			$(".price").each(function(){
				if(($(this).val() != '') && (!isMoney($(this).val()))) {
					flag = false;
					$(this).focus();
					top.$.jBox.tip("请输入正确的金额");
					return false;
				}
			}); */
			
			if(!flag) {
				return false;
			}
			if($("#isReceive").val() == "0"){
				$.jBox.confirm("是否确认提交预报名", "提示", function(v, h, f) {
					if (v == 'ok') {
						$.post(url,$("#baseInfo").serialize(),function(data){
							if(data.message=="1"){
								$.jBox.tip("保存成功!");
								window.location="${ctx}/speedGenOrder/page";
							}else if(data.message=="3"){
								$.jBox.tip(data.error,'warning');
								$("#btnSubmit").attr("disabled", false);
							}else{
								$.jBox.tip('系统异常，请重新操作!','warning');
								$("#btnSubmit").attr("disabled", false);
							}
							//将文本框还原
							$(".groupPrices_tr").each(function(){
							 	$(this).find("input").removeAttr('disabled');
							 	$(this).find("select").removeAttr('disabled');
						 	});
						});
					}
					//将文本框还原
							$(".groupPrices_tr").each(function(){
							 	$(this).find("input").removeAttr('disabled');
							 	$(this).find("select").removeAttr('disabled');
						 	});
				});
			}else if($("#isReceive").val() == "1"){
				$.jBox.confirm("是否确认提交预报名并收款", "提示", function(v, h, f) {
					if (v == 'ok') {
						$.post(url,$("#baseInfo").serialize(),function(data){
							if(data.message=="1"){
								$.jBox.tip("保存成功!");
								var resultCurrency = $("#result_currency").val();
								var resultAmount = $("#result_amount").val();
								var cancelPayUrl = encodeURIComponent("/hotelOrder/list/0?_m=417&_mc=852");
								window.location="${ctx}/hotelOrder/payHotelOrder/" + data.orderUuid+"?resultCurrency="+resultCurrency+"&resultAmount="+resultAmount+"&cancelPayUrl="+cancelPayUrl;
							}else if(data.message=="3"){
								$.jBox.tip(data.error,'warning');
								$("#btnSubmit").attr("disabled", false);
							}else{
								$.jBox.tip('系统异常，请重新操作!','warning');
								$("#btnSubmit").attr("disabled", false);
							}
							//将文本框还原
							$(".groupPrices_tr").each(function(){
							 	$(this).find("input").removeAttr('disabled');
							 	$(this).find("select").removeAttr('disabled');
						 	});
						});
					}
					//将文本框还原
							$(".groupPrices_tr").each(function(){
							 	$(this).find("input").removeAttr('disabled');
							 	$(this).find("select").removeAttr('disabled');
						 	});
				});
			}
		},
	});
	
	//加载供应商信息
	loadAgentInfo();
	
	//将非签约渠道设置为不可用
    $("#nonChannelList").find("input").attr("disabled",true);
	//将已有团号的游客类型价格置为空
    $("#no_edit_tbody").find("input").attr("disabled",true);
    $("#no_edit_tbody").find("select").attr("disabled",true);
	//将已有团号的编辑设置不可用
    $("#no_edit_msg").find("input").attr("disabled",true);
});
//级联查询
function getAjaxSelect(type,obj,value){
	$.ajax({
		type: "POST",
	   	url: "${ctx}/hotelControl/ajaxCheck",
	   	data: {
				"type":type,
				"uuid":$(obj).val()
			  },
		//async:false,
		dataType: "json",
	   	success: function(data){
	   		if(type != "islandway" && type != "foodtype" && type != "roomtype"){
		   		$("#"+type).empty();
		   		$("#"+type).append("<option value=''>不限</option>");
	   		}else if(type=="foodtype"){
	   			$(obj).parents('[name="roomType"]:first').find("#foodtype").text("");
	   		}else{
	   			//$("#hotel").empty();
		   		//$("#hotel").append("<option value=''>不限</option>");
	   			$("#roomtype").empty();
	   		}
	   		if(data){
	   			if(type=="hotel"){
		   			$.each(data.hotelList,function(i,n){
		   				$("#"+type).append($("<option/>").text(n.nameCn).attr("value",n.uuid));
		   			});
		   			if(value){
		   				$("#"+type).val(value);
		   					
	   					getAjaxSelect(tranferObj(type),$("#"+type),tranferValue(type));
		   			}
		   			
		   			
	   			}else if(type=="roomtype"){
	   				//先将房型清空
	   				$('.house_house_sel').children(':gt(0)').remove();
	   				$("#roomtype").empty();
	   				$.each(data.roomtype,function(i,n){
	   					$("#"+type).append($("<option/>").text(n.roomName).attr("value",n.uuid));
	   				});
	   				setTimeout($("#roomtype").change(),1000);
	   				var hotelUuid = $("#hotel").val();
					setTimeout(writeTravel(hotelUuid),1000);
	   				if(value){
	   					$("#"+type).val(value);
	   					getAjaxSelect(tranferObj(type),$("#"+type),tranferValue(type));
	   				}
	   			}else if(type=="foodtype"){
	   				$(obj).parents('[name="roomType"]').find('input[name="night"]').each(function(){
						$(this).val("");
					});
	   				$.each(data.roomMeals,function(i,n){
   	   					var islandwayTd = $(obj).parents('[name="roomType"]:first').find("#foodtype");
               			var $meal= $('<span><input />'+n.mealName+'</span>');
               			$meal.find('input')
               			.attr("class","price_sale_house_sel_input")
               			.attr("type","checkbox")
               			.attr("name","foodtype")
               			.val(n.uuid);
               			$(islandwayTd).append($meal);
        	   		});
	   			}else if(type=="islandway"){
	   				$.each(data.listIslandWay,function(i,n){
	   					$("#"+type).append($("<option/>").text(n.label).attr("value",n.uuid));
	   				});
           			
	   				$("#hotel").empty();
	   				$.each(data.hotelList,function(i,n){
 	   					$("#hotel").append($("<option/>").text(n.nameCn).attr("value",n.uuid));
	   				});
	   				if(value){
	   					$("#islandway").val(value);
	   					$("#hotel").val("${hotelControlQuery.hotel}");
	   					getAjaxSelect(tranferObj("hotel"),$("#hotel"),tranferValue("hotel"));
	   				}
	   			}else if(type=="island"){
	   				$("#hotel").empty();
	   				$("#roomtype").empty();
	   				$("#foodtype").empty();
	   				$("#islandways").empty();
	   				$("#hotel_hotelGroup").empty();
	   				$('.house_house_sel').children(':gt(0)').remove();
	   				$('.house_house_sel').find('input').val("");
	   				$.each(data.islandList,function(i,n){
	   					$("#island").append($("<option/>").text(n.islandName).attr("value",n.uuid));
	   				});
	   				if(value){
	   					$("#"+type).val(value);
		   				getAjaxSelect(tranferObj(type),$("#"+type),tranferValue(type));
	   				}
	   			
	   			}
	   		}
	   	}
	});
}
$(document).on("change", "#hotel", function () {
    	var hotelUuid = $("#hotel").val();
        if(!hotelUuid) {
        	$("#hotel_hotelGroup").text("");
        } else {
        	$.ajax({
        		type: "POST",
        	   	url: "${ctx}/hotelPl/getHotelDetailInfo",
        	   	async: false,
        	   	data: {
        				"hotelUuid":hotelUuid
        			  },
        		dataType: "json",
        	   	success: function(data){
        	   		if(data){
        	   			currentHotel = data;
    	   	        	$("#hotel_hotelGroup").text(currentHotel.hotelGroup);
        	   		}
        	   	}
        	});
        }
    });

//岛屿一次行级联所有信息
function getAjaxSelectAll(type,obj,value){
	$("#yes_edit_tbody .groupPrices_tr:visible").remove();
	getAjaxSelectSyn(type,obj);
	$("#hotel").change();
	var hotelUuid = $("#hotel").val();
	if(hotelUuid){
		setTimeout(writeTravel(hotelUuid),1000);
	}
	setTimeout($("#roomtype").change(),1000);
}

//将游客类型写出页面
function writeTravel(hotelUuid){
	$("#yes_edit_tbody .groupPrices_tr:visible").remove();
	$("select[name=hotelTraveler_personType]").empty();
	if(hotelUuid){
		$.ajax({
			type: "POST",
		   	url: "${ctx}/speedGenOrder/getTravelersByHotelUuid",
		   	data: {"hotelUuid":hotelUuid},
			async:false,
			dataType: "json",
		   	success: function(data){
		   	  orderInfo = {
		   			traveller: new Array(data.length),
				    // 订单总额
        			totalCost: {},
        		    // 应收金额 
        		    accounts: {},
        		    // 已收金额
        			receipted: {},
        		    // 总人数
        			totalCount: 0
		   		};
		   		var $travel = $("#yes_edit_tbody");
		   		for(var i=data.length-1;i>=0;i--){
		   			var travelerObj = {type:data[i].uuid,name:data[i].name,cost:""};
				    orderInfo.traveller[i]=travelerObj;
		        }
		   		for(var i=data.length-1;i>=0;i--){
					$travel.prepend('<tr id="'+data[i].uuid+'" class="groupPrices_tr"><td class="tc">'+data[i].name
					+'<input type="hidden" name="hotelOrderPrice_activityHotelGroupPriceUuid" value="'+data[i].uuid+'" />'
					+'<input type="hidden" name="hotelOrderPrice_remark" value="_"/>'
					+'<input type="hidden" name="hotelOrderPrice_priceType" value="1" />'
					+'<input type="hidden" name="hotelOrderPrice_priceName" value="_" />'
					+'<input type="hidden" name="hotelOrderPrice_travelerType" value="'+data[i].uuid+'" />'
					+'</td>'
					+'<td class="tc"><p><select name="hotelOrderPrice_currencyId" class="w80">'+
					+'<c:forEach items="${currencyList }" var="currency">'
					+'<option value="${currency.id }" data-currency="${currency.currencyMark}">${currency.currencyName}</option>'
					+'</c:forEach></select>'
					+'<input style="ime-mode: disabled;" data-type="float" class="w30b " type="text" name="hotelOrderPrice_price" value=""/></p>'
					+'</td>'
					+'<td class="tc"><input type="text" data-type="number" class="price_sale_house_w100" name="hotelOrderPrice_num" value=""/></td>'
					+'<td class="tc"><span></span></td></tr>');
					$("select[name=hotelTraveler_personType]").prepend('<option value='+data[i].uuid+'>'+data[i].name+'</option>');
				}
	   		}
		});
	}
}

function getAjaxSelectSyn(type,obj,value){
	$.ajax({
		type: "POST",
	   	url: "${ctx}/hotelControl/ajaxCheck",
	   	data: {
				"type":type,
				"uuid":$(obj).val()
			  },
		async:false,
		dataType: "json",
	   	success: function(data){
	   		if(type != "islandway" && type != "foodtype" && type != "roomtype"){
		   		$("#"+type).empty();
		   		$("#"+type).append("<option value=''>不限</option>");
	   		}else if(type=="foodtype"){
	   			$(obj).parents('[name="roomType"]:first').find("#foodtype").text("");
	   		}else{
	   			$("#islandway").empty();
	   			$("#hotel").empty();
	   			$("#roomtype").empty();
	   		}
	   		if(data){
	   			if(type=="hotel"){
		   			$.each(data.hotelList,function(i,n){
		   				$("#"+type).append($("<option/>").text(n.nameCn).attr("value",n.uuid));
		   			});
		   			if(value){
		   			$("#"+type).val(value);
	   				getAjaxSelect(tranferObj(type),$("#"+type),tranferValue(type));
		   			}
		   			$("#hotel").trigger('change');
	   			}else if(type=="roomtype"){
	   				$.each(data.roomtype,function(i,n){
	   					$("#"+type).append($("<option/>").text(n.roomName).attr("value",n.uuid));
	   				});
	   				if(value){
	   				$("#"+type).val(value);
	   					getAjaxSelect(tranferObj(type),$("#"+type),tranferValue(type));
	   				}
	   			}else if(type=="foodtype"){
	   				$.each(data.roomMeals,function(i,n){
   	   					var islandwayTd = $(obj).parents('[name="roomType"]:first').find("#foodtype");
               			$(islandwayTd).append(n.mealName);
               			$(islandwayTd).append($("<input/>").attr("class","redio_martop_4").attr("type","checkbox").attr("name","foodtype").val(n.uuid));
        	   		});
	   			}else if(type=="islandway"){
	   				$("#islandways").empty();
	   				var islandways1 = $("#islandways");
	   				$.each(data.listIslandWay,function(i,n){
	   					//$("#"+type).append($("<option/>").text(n.label).attr("value",n.uuid));
	           			var $islandways2= $('<span><input />'+n.label+'</span>');
	           			$islandways2.find('input')
	           			.attr("class","price_sale_house_sel_input")
	           			.attr("type","checkbox")
	           			.attr("name","islandWay")
	           			.val(n.uuid);
	           			$(islandways1).append($islandways2);
		   				});
	   				$("#hotel").empty();
	   				$.each(data.hotelList,function(i,n){
 	   					$("#hotel").append($("<option/>").text(n.nameCn).attr("value",n.uuid));
	   				});
	   				if(value){
	   					$("#islandway").val(value);
	   					$("#hotel").val("${hotelControlQuery.hotel}");
	   					getAjaxSelect(tranferObj("hotel"),$("#hotel"),tranferValue("hotel"));
	   				}
	   			}else if(type=="island"){
	   				$.each(data.islandList,function(i,n){
	   					$("#island").append($("<option/>").text(n.islandName).attr("value",n.uuid));
	   				});
	   				if(value){
	   					$("#"+type).val(value);
		   				getAjaxSelect(tranferObj(type),$("#"+type),tranferValue(type));
	   				}
	   			}
	   		}
	   	}
	});
}

//提交预报名
function submitSignUp() {
   /* $.jBox($("#forecast-name-submit").html(), {title: "确认预报名", buttons: { '确认': 1}, submit: function (v, h, f) {
        if (v == "1") {
     	$("#isReceive").val("0");
     	$("#baseInfo").submit();
            return true;
        }
      }, height: '140', width: 300
  }); */
  $("#isReceive").val("0");
  $("#baseInfo").submit();
}
        
//提交并收款
function submitAndReceive() {
   /* $.jBox($("#forecast-name-submit-collections").html(), {
    title: "确认预报名并收款", buttons: { '确认': 1}, submit: function (v, h, f) {
        if (v == "1") {
     	$("#isReceive").val("1");
     	$("#baseInfo").submit();
            return true;
        }
    }, height: '200', width: 300
	}); */
	$("#isReceive").val("1");
	$("#baseInfo").submit();
}
</script>
<style>
.price_sale_house_sel_input{ width:20px!important; margin:0px !important;}
</style>
</head>
<body>
<div class="ydbzbox fs">
	<div class="ydbz_tit">
		<span class="ydExpand"></span>订单基本信息
	</div>
	<form:form modelAttribute="speedOrderInput" enctype="multipart/form-data" method="post"
		action="" class=" form-search" id="baseInfo" novalidate="novalidate">
		<!-- 酒店订单所有金额隐藏域  -->
		<!-- 订单总额 -->
		<input type="hidden" name="hotelMoneyAmount_currencyId" id="total_currency" />
		<input type="hidden" name="hotelMoneyAmount_amount" id="total_amount" />
		<input type="hidden" name="hotelMoneyAmount_moneyType" value="28"/>
		
		<!-- 结算总额 -->
		<input type="hidden" name="hotelMoneyAmount_currencyId" id="result_currency" />
		<input type="hidden" name="hotelMoneyAmount_amount" id="result_amount" />
		<input type="hidden" name="hotelMoneyAmount_moneyType" value="13"/>
		
		<!-- 应收金额 -->
		<input type="hidden" name="hotelMoneyAmount_currencyId" id="to_receive_currency" />
		<input type="hidden" name="hotelMoneyAmount_amount" id="to_receive_amount" />
		<input type="hidden" name="hotelMoneyAmount_moneyType" value="19"/>
		
		<%-- 酒店产品信息 --%>
		<input type="hidden" name="activityHotelUuid" value="${activityHotel.uuid }" />
		<input type="hidden" name="activityHotelGroupUuid" value="${activityHotelGroup.uuid }" />

		<%-- 预定人数 --%>
		<input type="hidden" name="orderPersonNum" id="orderPersonNum" />		
		<%-- 是否收款 --%>
		<input type="hidden" name="isReceive" id="isReceive" />
		<input type="hidden" name="allfood" id="allfood" />
		<div class="mod_information_dzhan">
			<div class="mod_information_dzhan_d error_add1" id="oneStepContent"
				style=" overflow:hidden;">
				<div class="mod_information_d2" style="min-width:334px;">
					<label> <span class="xing">*</span>团号选择：
					</label> <select id="selNo" class="w180_30" name="selNo">
						<option value="1" <c:if test="${selNo!='2'}">selected="selected"</c:if> >创建新团</option>
						<option value="2" <c:if test="${selNo=='2'}">selected="selected"</c:if> >使用老团</option>
					</select>
					<!--<label for="acitivityName" class="error">必填信息</label>-->
				</div>
				<span class="group_orders_number">
					<div class="mod_information_d2 ">
						<label for="groupCode"><span class="xing">*</span>团号：</label> 
						<input  type="text" value="" id="groupCode" name="groupCode" maxlength="100" onblur="checkedMassage(this);"/>
						<span id="error_groupCode"></span> 
						<span id="tdredgroupcode" class="tdred" style="display:none;">此团号已存在！</span>
					</div>
					<div class="mod_information_d2 ">
						<label>下单人：</label>  
						<form:select path="orderMan" id="groupSelect1" cssStyle="width:150px;">
								<c:forEach items="${userList }" var="entity">
									<option value="${entity.id }" <c:if test="${entity.id==user.id }">selected="selected"</c:if> >${entity.name }</option>
								</c:forEach>
						</form:select>
						</select>
					</div>

				</span> <span class="group_number_orders">
					<div class="mod_information_d2 ">
						<label><span class="xing">*</span>团号：</label>
							<form:select path="activityHotelGroup.groupCode" id="groupSelect">
								<option value="0">请选择</option>
								<c:forEach items="${activityHotelGroupList }" var="entity">
									<option value="${entity.uuid }" <c:if test="${entity.uuid == activityHotelGroup.uuid }">selected="selected"</c:if>>${entity.groupCode }</option>
								</c:forEach>
							</form:select>
					</div>
					<div class="mod_information_d2 ">
						<label>下单人：</label>
						<form:select path="orderMan" id="groupSelect2" cssStyle="width:150px;">
								<c:forEach items="${userList }" var="entity">
									<option value="${entity.id }" <c:if test="${entity.id==user.id }">selected="selected"</c:if> >${entity.name }</option>
								</c:forEach>
						</form:select>
					</div>
				</span>

			</div>
		</div>
		<div class="mod_information_dzhan yes_edit_msg">
			<div class="mod_information_dzhan_d error_add1" id="oneStepContent"
				style=" overflow:hidden;">
				<div class="kongx" style="height:10px;"></div>
				<div class="mod_information_d1">
					<label> <span class="xing">*</span>产品名称：</label> 
					<!-- <input type="text" maxlength="200" value="" class="inputTxt error" name="activityName" id="acitivityName_yes" style="width:60%" /> 
					<span style="color:#b2b2b2" class="acitivityNameSizeSpan">还可输入200个字</span> -->
					<input type="text" maxlength="200" value="" class="acitivityName" name="activityName" id="acitivityName_yes" style="width:60%" /> 
					<span style="color:#b2b2b2" class="acitivityNameSizeSpan">还可输入<span class="acitivityNameSize">200</span>个字</span>
				</div>
				<div class="kongx"></div>
				<!-- <div class="mod_information_d2" style="min-width:334px;">
					<label> <span class="xing">*</span>控房单号： </label> 
					<input class="valid" type="text" id="activitySerNum_yes" name="activitySerNum" />
				</div> -->
				<div class="mod_information_d2 ">
					<label> <span class="xing">*</span>出团日期：</label> 
					<input type="text" onclick="WdatePicker()" class="dateinput" id="groupOpenDate_yes" name="groupOpenDateString" />
				</div>
				<div class="mod_information_d2" style="min-width:334px;">
					<label> <span class="xing">*</span>国家： </label>
						<trekiz:suggest name="country" style="width:150px;" defaultValue="80415d01488c4d789494a67b638f8a37"
							callback="getAjaxSelect('island',$('#country'))" displayValue="${countryName}" ajaxUrl="${ctx}/geography/getAllConListAjax" />
				<script language="javascript">
					getAjaxSelect('island',$('#country'));
				</script>
				</div>
				<div class="kongx"></div>
				<div class="mod_information_d2 " style="min-width:334px;">
					<label> <span class="xing">*</span>岛屿名称：</label> 
					<select name="island" id="island" onchange="getAjaxSelectAll('islandway',this);">
							<option value="" selected="selected">不限</option>
					</select>
					</div>
				<div class="mod_information_d2 ">
					<label> <span class="xing">*</span>酒店名称：</label> 
					<select name="hotel" id="hotel" onchange="getAjaxSelect('roomtype',this);">
							<option value="" selected="selected">不限</option>
					</select> 
				</div>
				<div class="mod_information_d2 ">
					<label>酒店集团：</label> 
					<span id="hotel_hotelGroup"></span>
				</div>
				<div class="kongx"></div>
				<div class="mod_information_d2 " style="width:100%" >
					<label> <span class="xing">*</span>交通：</label> 
					<%-- <trekiz:defineDict name="islandWay" type="islands_way" input="checkbox" style="width:20px!important; margin:0px !important;"/> --%>
					<span id="islandways"></span> 
				</div>
				<div class="kongx"></div>
					<div class="mod_information_d2 " style="min-width:334px;">
                        <label> <span class="xing">*</span>余位： </label>
                        <input class="valid" type="text" name="remNumber" id="remNumber_yes" data-type="number"/>
                    </div>
                    <div class="mod_information_d2 ">
                        <label> 预报名间数： </label>
                        <input class="valid" type="text" name="forecaseReportNum" id="preOrderNum_yes" data-type="number"/>
                    </div>
				<div class="kongx"></div>
				<div class="house_house_sel">
					<!--房型晚数&餐型循环开始-->
					<div name="roomType">
						<div class="mod_information_d2 " style="height:auto;min-width:334px;">
							<label> <span class="xing">*</span>房型：</label> 
							<select class="houseTypeSel" id="roomtype" onchange="getAjaxSelect('foodtype',this);" name="roomtype"/>
							</select> 
							<label class="price_sale_house_label">X</label> <input class="valid price_sale_house_w40" type="text" name="night"/>晚 
							<span class="padr10"></span> <i class="price_sale_house_01"></i>
						</div>
						<div class="mod_information_d2 mealTypeDiv" style="width:60%;">
							<label> <span class="xing">*</span>餐型：</label> 
							<span id="foodtype"></span> 
						</div>
						<div class="kongx"></div>
					</div>
					<!--房型晚数&餐型循环结束-->
				</div>
			</div>
		</div>
		<div class="mod_information_dzhan no_edit_msg" style="display:none;">
			<div class="mod_information_dzhan_d error_add1" id="oneStepContent"
				style=" overflow:hidden;">
				<div class="kongx"></div>
				<div class="mod_information_d1">
					<label> 产品名称： </label>${activityHotel.activityName }
					<!--<label for="acitivityName" class="error">必填信息</label>-->
				</div>
				<div class="kongx"></div>
				<%-- <div class="mod_information_d2" style="min-width:334px;">
					<label> 控房单号： </label>${activityHotel.activitySerNum }
					<!--<label for="acitivityName" class="error">必填信息</label>-->
				</div> --%>
				<div class="mod_information_d2 ">
					<label> 出团日期： </label>
					<fmt:formatDate value="${activityHotelGroup.groupOpenDate}" pattern="yyyy-MM-dd" />
				</div>
				<div class="mod_information_d2 ">
					<label> 国家： </label>
						<trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${activityHotel.country }"/>
				</div>
				<div class="kongx"></div>
				<div class="mod_information_d2 " style="min-width:334px;">
					<label> 岛屿名称： </label>
					<trekiz:autoId2Name4Table tableName="island" sourceColumnName="uuid" srcColumnName="island_name" value="${activityHotel.islandUuid}" />					
				</div>
				<div class="mod_information_d2 ">
					<label> 酒店名称： </label>
					<trekiz:autoId2Name4Table tableName="hotel" sourceColumnName="uuid" srcColumnName="name_cn" value="${activityHotel.hotelUuid}" />
				</div>
				<div class="mod_information_d2 ">
                    <label> 酒店集团： </label>${hotelGroup }
				</div>
				<div class="kongx"></div>
				<div class="mod_information_d2 " style="width:100%" >
					<label> 交通： </label>
						<c:forEach items="${fn:split(activityHotelGroup.islandWay,';')}" var="var">
								<trekiz:defineDict name="island_way" type="islands_way" defaultValue="${var}" readonly="true" style="width:20px!important; margin:0px !important;"/>
						</c:forEach>
				</div>
				<div class="kongx"></div>
					<div class="mod_information_d2 "
						style="height:auto;min-width:334px;">
						<c:choose>
							<c:when test="${fn:length(groupRoomList)>0 }">
								<c:forEach items="${groupRoomList }" var="room">
									<p class="margin_padding_none">
										<label> 房型： </label>
										<trekiz:autoId2Name4Class classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${room.hotelRoomUuid }"/> 
										<label class="price_sale_house_label">*</label>${room.nights }晚
									</p>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<label> 房型： </label>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="mod_information_d2 ">
						<c:choose>
							<c:when test="${fn:length(groupRoomList)>0 }">
								<c:forEach items="${groupRoomList }" var="room">
									<p class="margin_padding_none">
									<label> 餐型： </label>
									<c:forEach items="${room.activityHotelGroupMealList }" var="mealvar">
									    <trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealvar.hotelMealUuid }" />
									</c:forEach>
									</p>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<label> 餐型： </label>
							</c:otherwise>
						</c:choose>
					</div>
				<div class="kongx"></div>
				<div class="payment_information">
					<div class="mod_information_d2 ">
						<label> 余位： </label>
						<input id="remNumber_no" value="${activityHotelGroup.remNumber }" readonly="readonly" type="text" data-type="number"/>
					</div>
					<div class="mod_information_d2 ">
						<label> 预报名间数： </label>
						<input id="preOrderNum_no" name="forecaseReportNum" type="text" data-type="number"/>
					</div>

				</div>
			</div>
		</div>
	<!--<div class="ydbz_tit">填写预订人信息</div>-->
	<div class="ydbz_tit">
		<span class="ydExpand" data-target="#bookingPeopleInfo"></span>填写预订人信息
	</div>
	<div id="bookingPeopleInfo">
		<div id="orderpersonMesdtail">
			<div class="mod_information_dzhan" id="secondStepDiv">
				<div class="mod_information_dzhan_d error_add1" id="oneStepContent" style=" overflow:hidden;">
					<div class="mod_information_d2 ">
						<label><span class="xing">*</span>渠道：</label> 
						<select name="xunjiafgs" id="channelType" class="required">
							<option value="1">签约渠道</option>
							<option value="2">非签约渠道</option>
						</select>
					</div>
					<div class="mod_information_d2" id="signChannel">
						<label><span class="xing">*</span>渠道总社：</label>
						<select name="orderCompany" id="orderCompany" onchange="loadAgentInfo();" class="required">
							<c:forEach items="${agentList}" var="agentinfo">
								<option value="${agentinfo.id}">${agentinfo.agentName}</option>
<%-- 								<option value="${agentinfo.id},${agentinfo.agentName},${agentinfo.agentContact},${agentinfo.agentContactMobile},${agentinfo.agentTel},${agentinfo.agentAddress},${agentinfo.agentContactFax},${agentinfo.agentContactQQ},${agentinfo.agentContactEmail},${agentinfo.agentPostcode}" <c:if test="${agentinfo.agentName eq (productorder.orderCompanyName) }">selected</c:if> >${agentinfo.agentName}</option> --%>
							</c:forEach>
						</select>
					</div>
					<div class="mod_information_d2" id="nonChannel" style="display:none;">
						<label class="price_sale_house_label02"><span class="xing">*</span>非签约渠道名称：</label>
						<input type="text" name="orderCompanyName"/>
					</div>
				</div>
			</div>
			<p class="ydbz_qdmc"></p>
			<!--签约渠道-->
			<div id="signChannelList">
				<ul class="ydbz_qd" id="orderpersonMes" name="orderpersonMes">
					<li><label><span class="xing">*</span>渠道联系人：</label>
						<input type="text" name="orderContacts_contactsName" onafterpaste="this.value=this.value.replaceSpecialChars()" onkeyup="this.value=this.value.replaceSpecialChars()" maxlength="10" />
					</li>
					<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label>
						<input type="text" name="orderContacts_contactsTel" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" id="orderPersonPhoneNum" maxlength="15" />
						<div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div> 
						<span class="ydbz_x yd1AddPeople">添加联系人</span>
					</li>
					<li flag="messageDiv" class="ydbz_qd_close">
						<ul>
							<li><label>固定电话：</label> <input type="text"  name="orderContacts_contactsTixedTel" /></li>
							<li><label>渠道地址：</label> <input type="text"  name="orderContacts_contactsAddress" /></li>
							<li><label>传真：</label> <input type="text"  name="orderContacts_contactsFax" /></li>
							<li><label>QQ：</label> <input type="text"  name="orderContacts_contactsQQ" /></li>
							<li><label>Email：</label> <input type="text"  name="orderContacts_contactsEmail" /></li>
							<li><label>渠道邮编：</label> <input type="text"  name="orderContacts_contactsZipCode" /></li>
							<li><label>其他：</label> <input type="text"  name="orderContacts_remark" />
							</li>
						</ul></li>
				</ul>
			</div>
			<!--非签约渠道-->
			<div id="nonChannelList" style="display:none;">
				<ul class="ydbz_qd" id="orderpersonMes" name="orderpersonMes">
					<li class="kong kongnofl"></li>
					<li><label><span class="xing">*</span>渠道联系人：</label> 
						<input type="text" name="orderContacts_contactsName" onkeyup="this.value=this.value.replaceSpecialChars()" onafterpaste="this.value=this.value.replaceSpecialChars()" maxlength="10"/>
					</li>
					<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label>
						<input type="text" name="orderContacts_contactsTel" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="15" />
						<div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div> 
						<span class="ydbz_x yd1AddPeople">添加联系人</span>
					</li>
					<li flag="messageDiv" class="ydbz_qd_close">
						<ul>
							<li>
								<label>固定电话：</label>
								<input type="text" name="orderContacts_contactsTixedTel" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" />
							</li>
							<li><label>渠道地址：</label> <input maxlength="" type="text" name="orderContacts_contactsAddress" />
							</li>
							<li><label>传真：</label> <input maxlength="" type="text" name="orderContacts_contactsFax" />
							</li>
							<li><label>QQ：</label> <input maxlength="" type="text" name="orderContacts_contactsQQ" />
							</li>
							<li><label>Email：</label> <input maxlength="" type="text" name="orderContacts_contactsEmail" />
							</li>
							<li><label>渠道邮编：</label> <input maxlength="" type="text" name="orderContacts_contactsZipCode" />
							</li>
							<li><label>其他：</label> <input maxlength="" type="text" name="orderContacts_remark" />
							</li>
						</ul>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<!--<div class="ydbz_tit"><span class="ydExpand" data-target="#specialDemand"></span>特殊需求</div>
                        <div class="orderdetails3" id="specialDemand">
                            <div class="ydbz2_lxr">
                                <span class="ydbz_tit ydbz_tit_child">
                                    <textarea name="textarea" class="price_sale_house_text">护照已领取、房产证未领取护照已领取、房产证未领取护照已领取、房产证未领取</textarea>
                                </span>
                            </div>
                        </div>-->
	<div class="ydbz_tit">
		<span class="ydExpand" data-target="#costTable"></span>费用及人数
	</div>
	<div id="costTable">
		<table class="table activitylist_bodyer_table_new contentTable_preventive">
			<thead>
				<tr>
					<th width="13%">游客类型</th>
					<th width="12%"><span class="xing">*</span>同行价/人</th>
					<th width="25%"><span class="xing">*</span>人数</th>
					<th width="25%">小计</th>
				</tr>
			</thead>
			<tbody id="no_edit_tbody">
                <c:forEach items="${priceList }" var="groupPrice">
					<tr id="${groupPrice.type}" class="groupPrices_tr">
						<td class="tc">
							<trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${groupPrice.type}"/>
							<!-- 费用调整 -->
							<input type="hidden" name="hotelOrderPrice_activityHotelGroupPriceUuid" value="${groupPrice.uuid}" />
							<input type="hidden" name="hotelOrderPrice_remark" value="_"/>
							<input type="hidden" name="hotelOrderPrice_priceType" value="1" />
							<input type="hidden" name="hotelOrderPrice_priceName" value="_" />
							<input type="hidden" name="hotelOrderPrice_travelerType" value="${groupPrice.type}" />
						</td>
						<td class="tc">
							<span data-cost="<trekiz:autoId2Name4Table tableName='currency' sourceColumnName='currency_id' srcColumnName='currency_mark' value='${groupPrice.currencyId }'/>:${groupPrice.price }">
							<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groupPrice.currencyId }"/><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${groupPrice.price }" /> </span>
							<input type="hidden" name="hotelOrderPrice_currencyId" value="${groupPrice.currencyId }" />
							<input type="hidden" name="hotelOrderPrice_price" value="${groupPrice.price }" />
						</td>
						<td class="tc"><input  type="text" data-type="number" class="price_sale_house_w100" name="hotelOrderPrice_num"/></td>
						<td class="tc"><span></span>
						</td>
					</tr>
				</c:forEach>
                <tr>
                    <td colspan="4" class="tr">
                        <span class="price_sale_houser_25"><label>合计人数：</label> <em> <span class="totalPeopleCount"> 0 </span> 人</em></span>
                        <span class="price_sale_houser_25"><label>合计金额：</label><em><span class="totalCost">0</span></em></span>
                    </td>
                </tr>
                <tr>
                    <td colspan="4" class="tl">
                        <span class="price_sale_houser_25" style="margin-left:89px;"><label>单房差：</label>
                        <trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${activityHotelGroup.currencyId }"/>
                       	<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${activityHotelGroup.singlePrice }" /> 
                        </span>
                        <span class="price_sale_houser_25" style="margin-left:200px;"><label>需交订金：</label>
                        <trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${activityHotelGroup.frontMoneyCurrencyId }"/>
                        <fmt:formatNumber  type="currency" pattern="#,##0.00" value="${activityHotelGroup.frontMoney }" /> 
                        </span>
                    </td>
                </tr>
            </tbody>
            <tbody id="yes_edit_tbody">
            	<%-- <c:forEach items="${travellerList }" var="groupPrice" varStatus="s">
					<tr id="${groupPrice.uuid}" class="groupPrices_tr">
						<td class="tc">${groupPrice.name }
						<!-- 费用调整 -->
						<input type="hidden" name="hotelOrderPrice_activityHotelGroupPriceUuid" value="${groupPrice.uuid}" />
						<input type="hidden" name="hotelOrderPrice_remark" value="_"/>
						<input type="hidden" name="hotelOrderPrice_priceType" value="1" />
						<input type="hidden" name="hotelOrderPrice_priceName" value="_" />
					    <input type="hidden" name="hotelOrderPrice_travelerType" value="${groupPrice.uuid}" />
						</td>
						<td class="tc">
							<p><select name="hotelOrderPrice_currencyId" class="w80">
												<c:forEach items="${currencyList }" var="currency">
													<option value="${currency.id }" data-currency="${currency.currencyMark}">${currency.currencyName}</option>
												</c:forEach>
											</select> 
											<input style="ime-mode: disabled;" data-type="float" class="w30b " type="text" name="hotelOrderPrice_price" /></p>
						</td>
						<td class="tc"><input type="text" data-type="number" class="price_sale_house_w100" name="hotelOrderPrice_num"/></td>
						<td class="tc"><span></span></td>
					</tr>
				</c:forEach> --%>
                <tr>
                    <td colspan="4" class="tr">
                        <span class="price_sale_houser_25"><label>合计人数：</label> <em> <span class="totalPeopleCount"> 0 </span> 人</em></span>
                        <span class="price_sale_houser_25"><label>合计金额：</label><em><span class="totalCost">0</span></em></span>
                    </td>
                </tr>
                <tr>
                    <td colspan="4" class="tl">
                        <div class="price_sale_house_single">
                            <span>
                                <label style="float:left;">单房差：</label>
                                <p>
                                    <select name="singleCurrency" class="w80">
									<c:forEach items="${currencyList }" var="currency">
										<option value="${currency.id }" data-currency="${currency.currencyMark}">${currency.currencyName}</option>
									</c:forEach>
									</select> 
                                    <input style="ime-mode: disabled;" data-type="float" class="w30b " type="text" name="singlePrice"/>
                                </p>
                            </span>
                        </div>
                        <div class="price_sale_house_single price_sale_house_single_ord">
                            <span>
                                <label style="float:left;">需交订金：</label>
                                <p>
                                    <select name="frontCurrency" class="w80">
									<c:forEach items="${currencyList }" var="currency">
										<option value="${currency.id }" data-currency="${currency.currencyMark}">${currency.currencyName}</option>
									</c:forEach>
									</select> 
                                    <input style="ime-mode: disabled;" data-type="float" class="w30b " type="text" name="frontMoney"/>
                                </p>
                            </span>
                        </div>
                    </td>
                </tr>
            </tbody>
		</table>
	</div>
	<!--费用调整开始-->
	<div class="ydbz_tit">
		<span class="ydExpand" data-target="#adjustCost"></span>费用调整
	</div>
	<div id="adjustCost">
		<div class="mod_information_dzhan" id="dddddStepDiv">
			<div class="mod_information_dzhan_d error_add1" id="oneStepContent" style="overflow:hidden;">
				<table class="contentTable_preventive table_padings" id="add_other_charges_table">
					<tr>
						<td width="100" class="tr">
							返佣金额：<input type="hidden" name="hotelOrderPrice_priceType" value="2" />
							<input type="hidden" name="hotelOrderPrice_num" value="_" />
							<input type="hidden" name="hotelOrderPrice_priceName" value="返佣金额" />
							<input type="hidden" name="hotelOrderPrice_activityHotelGroupPriceUuid" value="_" />
							<input type="hidden" name="hotelOrderPrice_travelerType" value="_" />
						</td>
						<td width="280" class="tl">
							<label> 
								<select name="hotelOrderPrice_currencyId" class="w80">
									<c:forEach items="${currencyList }" var="currency">
										<option value="${currency.id }" data-currency="${currency.currencyMark}">${currency.currencyName}</option>
									</c:forEach>
								</select> 
							</label> 
							<input type="text" class="price_sale_house_w93" data-type="amount"  name="hotelOrderPrice_price" id="input5" />
						</td>
						<td width="180" class="tr">备注：</td>
						<td colspan="3">
							<span class="tl"><input type="text" class="price_sale_house_w300" name="hotelOrderPrice_remark" id="input9" /></span>
						</td>
					</tr>
					<tr class="calc_amount_tr">
						<td class="tr">
							优惠金额：<input type="hidden" name="hotelOrderPrice_priceType" value="3" />
							<input type="hidden" name="hotelOrderPrice_num" value="_" />
							<input type="hidden" name="hotelOrderPrice_priceName" value="优惠金额" />
							<input type="hidden" name="hotelOrderPrice_activityHotelGroupPriceUuid" value="_" />
							<input type="hidden" name="hotelOrderPrice_travelerType" value="_" />
						</td>
						<td class="tl">
							<label> 
								<select name="hotelOrderPrice_currencyId" class="w80">
									<c:forEach items="${currencyList }" var="currency">
										<option value="${currency.id }" data-currency="${currency.currencyMark}">${currency.currencyName}</option>
									</c:forEach>
								</select> 
							</label> 
							<input type="text" class="price_sale_house_w93 price" data-type="amount" name="hotelOrderPrice_price" id="input6" maxlength="" />
						</td>
						<td class="tr">备注：</td>
						<td colspan="3">
							<span class="tl"> 
								<input type="text" class="price_sale_house_w300" name="hotelOrderPrice_remark" id="input10" maxlength="" />
							</span>
						</td>
					</tr>
					<tr>
						<td class="tr">退款金额：<input type="hidden" name="hotelOrderPrice_priceType" value="5" />
							<input type="hidden" name="hotelOrderPrice_num" value="_" />
							<input type="hidden" name="hotelOrderPrice_priceName" value="退款金额" />
							<input type="hidden" name="hotelOrderPrice_activityHotelGroupPriceUuid" value="_" />
							<input type="hidden" name="hotelOrderPrice_travelerType" value="_" />
						</td>
						<td class="tl">
							<label>
								<select name="hotelOrderPrice_currencyId" class="w80">
									<c:forEach items="${currencyList }" var="currency">
										<option value="${currency.id }" data-currency="${currency.currencyMark}">${currency.currencyName}</option>
									</c:forEach>
								</select>
							</label>
							<input type="text" name="hotelOrderPrice_price"  data-type="amount" class="price_sale_house_w93" id="input6" />
						</td>
						<td class="tr">备注：</td>
						<td colspan="3">
							<span class="tl">
                                         <input type="text" name="hotelOrderPrice_remark" class="price_sale_house_w300" id="input10" />
                                     </span>
							<span class="padr10"></span>
                                     <input value="增加其他费用" style="width:auto;" class="btn btn-primary" type="button" onclick="add_other_charges();" />
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<!--费用调整结束-->
	<!--费用结算开始-->
	<div class="ydbz_tit">
		<span class="ydExpand" data-target="#costSettlement"></span>费用结算
	</div>
	<!-- <div id="costSettlement">
		<div class="mod_information_dzhan">
			<div class="mod_information_dzhan_d error_add1" id="oneStepContent"
				style=" overflow:hidden;">
				<div class="mod_information_d2">
					<label>订单总额：</label><span class="totalCost"></span>
				</div>
				<div class="mod_information_d2 ">
					<label>结算总额：</label><span class="red accounts"></span>
				</div>
			</div>
		</div>
		<p class="ydbz_qdmc"></p>
		<p class="price_sale_houser_line"></p>
		<div class="mod_information_dzhan">
			<div class="mod_information_dzhan_d error_add1" id="oneStepContent"
				style=" overflow:hidden;">
				<div class="mod_information_d2">
					<label>应收金额：</label><span class="accounts"></span>
				</div>
				<div class="mod_information_d2 ">
					<label>已收金额：</label><span>0</span>
				</div>
				<div class="mod_information_d2 ">
					<label>未收金额：</label><span class="green unReceipted"></span>
				</div>
			</div>
		</div>
	</div> -->
	<table width="100%">
	    <tbody><tr>
	        <td class="tr valign_top" width="8%">订单总额：</td>
	        <td class="tl valign_top" width="17%"><span class="totalCost"></span></td>
	        <td class="tr valign_top" width="8%">结算总额：</td>
	        <td class="tl valign_top" width="17%"><span class="red accounts"></span></td>
	        <td class="tr valign_top" width="8%"></td>
	        <td class="tl valign_top" width="17%"></td>
	        <td class="tr valign_top" width="8%"></td>
	        <td class="tl valign_top" width="17%"></td>
	    </tr>
		</tbody>
	</table>
	<p class="price_sale_houser_line"></p>
	<table width="100%">
	    <tbody><tr>
	        <td class="tr valign_top" width="8%">应收金额：</td>
	        <td class="tl valign_top" width="17%"><span class="accounts"></span></td>
	        <td class="tr valign_top" width="8%">已收金额：</td>
	        <td class="tl valign_top" width="17%"><span>0</span></td>
	        <td class="tr valign_top" width="8%">未收金额:</td>
	        <td class="tl valign_top" width="17%"><span class="green unReceipted"></span></td>
	        <td class="tr valign_top" width="8%"></td>
	        <td class="tl valign_top" width="17%"></td>
	    </tr>
		</tbody>
	</table>
	<!--费用结算结束-->
	<!--旅客信息开始-->
	<div class="ydbz_tit">
		<span class="ydExpand" data-target="#passengerInfo"></span>游客信息
	</div>
	<div id="passengerInfo">
		<input value="添加游客信息" onclick="add_tours_obj();" class="btn btn-primary" type="button" />
		<table id="passengerInfoTable" class="table activitylist_bodyer_table_new contentTable_preventive" style="min-width:1400px;display:none;">
			<thead>
				<tr>
					<th width="3%">序号</th>
					<th width="6%"><span class="xing">*</span>姓名</th>
					<th width="5%">英文姓名</th>
					<th width="8%">游客类型</th>
					<th width="7%">性别</th>
					<th width="12%">签证国家及类型</th>
					<th width="25%">证件类型/证件号码/有效期</th>
					<th width="13%">价格</th>
					<th width="8%">备注</th>
					<th width="6%">资料上传</th>
					<th width="6%">操作</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		<!--旅客信息结束-->
		<!--<p class="price_sale_houser_line"></p>-->
		<!--<p class="ydbz_qdmc" style="padding: 12px 0px;"></p>-->
		<div flag="messageDiv" class="ydbz2_lxr">
			<p class="hotel_discount_count_mar20">
				<label>备注：</label>
				<textarea style="width: 786px; height: 83px;" name="remark"></textarea>
			</p>
		</div>
	</div>
	<div class="release_next_add">
		<input value="取消" onclick="window.location.reload();" class="btn btn-primary gray" type="button" /> 
		<input value="提交预报名" onclick="submitSignUp();" class="btn btn-primary" type="button" />
		<input value="提交并收款" onclick="submitAndReceive();" class="btn btn-primary" type="button" />
	</div>
	</form:form>
	<!-- 其他费用模板 -->
		<table>
			<tr class="add_other_charges calc_amount_tr" style="display:none;">
				<td class="tr">金额名称：
					<input type="hidden" name="hotelOrderPrice_priceType" value="4"/>
					<input type="hidden" name="hotelOrderPrice_num" value="_" />
					<input type="hidden" name="hotelOrderPrice_activityHotelGroupPriceUuid" value="_" />
					<input type="hidden" name="hotelOrderPrice_travelerType" value="_" />
				</td>
				<td>
					<span class="tl">
						<input type="text" class="price_sale_house_w93" name="hotelOrderPrice_priceName" id="input8" maxlength="" />
					</span>
				</td>
				<td class="tr tr_other_u">
					<input name="other_u" type="radio" class="dis_inlineblock" id="u138_input" value="radio" checked="checked" data-label="增加" />
					<label for="u138_input">增加</label>
					<input name="other_u" type="radio" id="u138_input2" value="radio" data-label="减少" />
					<label for="u140_input">减少：</label>
				</td>
				<td class="tl">
					<label>
						<select name="hotelOrderPrice_currencyId" class="w80">
							<c:forEach items="${currencyList }" var="currency">
								<option value="${currency.id }" data-currency="${currency.currencyMark}">${currency.currencyName}</option>
							</c:forEach>
						</select>
					</label>
					<input type="text" class="price_sale_house_w93 price" data-type="number" name="hotelOrderPrice_priceBak" id="input7" />
					<input type="hidden" name="hotelOrderPrice_price"/>
				</td>
				<td class="tr">备注：</td>
				<td class="tl">
					<input type="text" class="price_sale_house_w93" name="hotelOrderPrice_remark" id="input11" />
					<span class="padr10"></span>
					<i class="price_sale_house_02" onclick="del_other_charges(this);"></i>
				</td>
			</tr>
		</table>
		<!-- 其他费用模板 -->
		
		<!-- 游客模板信息 -->
		<table class="table activitylist_bodyer_table_new contentTable_preventive" style="min-width: 1400px;">
			<tr style="display:none;" id="add_tours_obj_tr">
				<td class="tc">add_tours_obj_tr_index</td>
				<td class="tc"><input type="text" name="hotelTraveler_name" class="price_sale_house_w93 required"/></td>
				<td class="tc"><input type="text" name="hotelTraveler_nameSpell" class="price_sale_house_w93" /></td>
				<td class="tc">
					<select name="hotelTraveler_personType" class="w80 display_inline" onchange="setTitle(this);">
						<%-- <c:forEach items="${travellerList }" var="groupPrice">
							<option value="${groupPrice.uuid}">${groupPrice.name}</option>
						</c:forEach> --%>
					</select>
					<script type="text/javascript" language="javascript">
						function setTitle(obj){
							$(obj).attr('title',$(obj).find("option:selected").text());
						}
					</script>
				</td>
				<td class="tc">
					<select name="hotelTraveler_sex" class="w80 display_inline">
						<option value="1">男</option>
						<option value="2">女</option>
					</select>
				</td>
				<td class="tc table_padings_none">
					<input type="hidden" name="hotelTraveler_visaInfo" />
					<p class="hotelTraveler_visaInfo">
						<input id="suggestcountry" type="text" autocomplete="off" name="suggestcountry" class="suggest" style="width: 70px; color: rgb(0, 0, 0);" value="" />
						<select name="visaTypeId" class="w80">
							<c:forEach items="${visaTypes }" var="visa">
								<option value="${visa.id }">${visa.label}</option>
							</c:forEach>
						</select>
						<i class="price_sale_house_01"></i>
					</p>
				</td>
				<td>
					<input type="hidden" name="hotelTraveler_papersType" />
					<p class="hotelTraveler_papersType">
						<select name="papersType" class="w80">
							<option value="">请选择</option>
							<option value="1">身份证</option>
							<option value="2">护照</option>
							<option value="3">警官证</option>
							<option value="4">军官证</option>
							<option value="5">其他</option>
						</select> 
						<input type="text" name="idCard" class="w130 input_pad" />
						<input type="text" name="validityDate" onclick="WdatePicker()" class="dateinput w90 input_pad" />
						<i class="price_sale_house_01"></i>
					</p>
				</td>
				<td class="tc">
					<input type="hidden" name="hotelTraveler_amount" />
					<p class="hotelTraveler_amount">
						<select name="currency" class="w80">
							<c:forEach items="${currencyList }" var="currency">
								<option value="${currency.id }">${currency.currencyName}</option>
							</c:forEach>
						</select> 
						<input type="text" name="price" class="w30b price" data-type="float"/> 
						<i class="price_sale_house_01"></i>
					</p>
				</td>
				<td class="tc"><input type="text" name="hotelTraveler_remark" class="price_sale_house_w93" /></td>
				<td class="tc">
					<a name="" class="btn_addBlue_file" id="addcost" onclick="up_files_pop(this);">附件管理</a> 
					<!--上传附件弹窗层开始-->
					<div class="up_files_pop" style="display:none;">
						<ul style="margin-left:0;"></ul>
						<a name="addFiles" class="btn_addBlue_file" onclick="uploadFiles('${ctx}','',this,'false')">上传附件</a>
					</div>
					<!--上传附件弹窗层开始--></td>
				<td class="tc"><a onclick="save_tours_obj(this)">保存</a> | <a onclick="del_tours_obj(this)">删除</a></td>
			</tr>
		</table>
		<!-- 游客模板信息 -->
		
	<!--提交预报名弹窗层开始-->
	<div class="forecast-name-submit" style="display:none;" id="forecast-name-submit">
		<ul>
			<li style="height:50px; line-height:50px; text-align:center;">您确认提交预报名吗？</li>
		</ul>
	</div>
	<!--提交预报名弹窗层结束-->
	
	<!--提交并收款弹窗层开始-->
	<div class="forecast-name-submit-collections" style="display:none;" id="forecast-name-submit-collections">
		<ul>
			<li style="height:50px; line-height:50px; text-align:center;">您确认提交预报名并进行收款吗？</li>
		</ul>
	</div>
	<!--提交并收款弹窗层结束-->
</div>
<script type="text/javascript">
var selNoInit = $("#selNo").val();
if(selNoInit=='1'){
	/* orderInfo = {
	    	traveller: [
	            	<c:forEach items="${travellerList }" var="groupPrice" varStatus="status">
	                	{ type: "${groupPrice.uuid}", name: "${groupPrice.name}",
	                		cost: ""},
	            	</c:forEach>
	            ],
		    // 订单总额
			totalCost: {},
		    // 应收金额 accounts: {},
		    // 已收金额
			receipted: {},
		    // 总人数
			totalCount: 0
		} */
}else{
	orderInfo = {
	    /* traveller: [
	        { type: "adult", name: "成人" },
	        { type: "baby", name: "儿童" },
	        { type: "baby1", name: "儿童占床" }
	    ], */
    	traveller: [
                	<c:forEach items="${priceList }" var="groupPrice" varStatus="status">
                    	{ type: "${groupPrice.type}", name: "<trekiz:autoId2Name4Table tableName='traveler_type' sourceColumnName='uuid' srcColumnName='name' value='${groupPrice.type}'/>",
                    		cost: { '<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groupPrice.currencyId}"/>': "${groupPrice.price}"}},
                	</c:forEach>
            ],
	    // 订单总额
        totalCost: {},
	    // 应收金额 accounts: {},
	    // 已收金额
		receipted: {},
	    // 总人数
		totalCount: 0
	}
}

//将游客类型写出页面
function writeTravelSel(hotelUuid){
	$("select[name=hotelTraveler_personType]").empty();
	if(hotelUuid){
		$.ajax({
			type: "POST",
		   	url: "${ctx}/speedGenOrder/getTravelersByHotelUuid",
		   	data: {"hotelUuid":hotelUuid},
			async:false,
			dataType: "json",
		   	success: function(data){
		   		for(var i=data.length-1;i>=0;i--){
					$("select[name=hotelTraveler_personType]").prepend('<option value='+data[i].uuid+'>'+data[i].name+'</option>');
				}
	   		}
		});
	}
}

$(document).ready(function (e) {
	initSuggest({});
	initSuggestClass({});
    var leftmenuid=$("#leftmenuid").val();
    $(".main-nav").find("li").each(function (index, element) {
        if ($(this).attr("menuid") == leftmenuid) {
            $(this).css("background", "url(/resource/images/img/pfs_nav_hover.png)");
        }
    });
    $("#channelType").on("change", function () {
        if ($("#signChannel").is(":hidden")) {
            $("#signChannel").show();
            $("#signChannelList").show();
            $("#signChannelList").find("input").attr("disabled",false);
            $("#nonChannel").hide();
            $("#nonChannelList").hide();
            $("#nonChannelList").find("input").attr("disabled",true);
            
            $("#orderCompanyName").attr("disabled", true);
            $("#orderCompany").removeAttr("disabled");
          	//加载渠道商联系人信息
			loadAgentInfo();
        } else {
            $("#signChannel").hide();
            $("#signChannelList").hide();
            $("#signChannelList").find("input").attr("disabled",true);
            $("#nonChannel").show();
            $("#nonChannelList").show();
            $("#nonChannelList").find("input").attr("disabled",false);
            
            $("#orderCompanyName").removeAttr("disabled");
            $("#orderCompany").attr("disabled", true);
        }
    });


    $("div.house_house_sel").on('click', 'i.price_sale_house_01', function () {
        var $div = $("div.house_house_sel > div").has(this).clone();
        $div.find("i.price_sale_house_01").removeClass('price_sale_house_01').addClass('price_sale_house_02');
        $("div.house_house_sel").append($div);
        $div.find('select.houseTypeSel').change();
    }).on('click', 'i.price_sale_house_02', function () {
        $("div.house_house_sel > div").has(this).remove();
    });

    // 联系人模板
    var $contactsTemp = $("#orderpersonMesdtail ul:first").clone();
    $("#orderpersonMesdtail").on("click", "span.yd1AddPeople", function () {
        // 添加联系人
        var $newContacts = $contactsTemp.clone();
        $newContacts.find('span.yd1AddPeople').replaceWith('<span class="ydbz_x yd1DelPeople gray">删除联系人</span>');
        $newContacts.find("input").val("");
        var $container = $("#signChannelList, #nonChannelList").has(this);
        $container.append($newContacts);
    }).on("click", "span.yd1DelPeople", function () {
        // 删除联系人
        $(this).parent().parent().remove();
    });
    $("#passengerInfoTable").on('click', 'i.price_sale_house_01', function () {
        // 添加签证
        var tempP = $(this).parent().clone();
        tempP.find('i.price_sale_house_01').replaceWith('<i class="price_sale_house_02"></i>');
        $(this).parent().parent().append(tempP);
    }).on('click', 'i.price_sale_house_02', function () {
        // 删除签证
        $(this).parent().remove();
    }).on('click', "a.delLink", function () {
        //删除游客信息
        $('#passengerInfoTable tr').has(this).remove();
        updateSequence();
    });

    $("#costTable").on("change", "input:text, select", function () {
        // 同行价、 人员改变
        getTradePrice();
        peopleCountChange();
    }).on('click', 'i.price_sale_house_01', function () {
        // 添加同行价
        var tempP = $(this).parent().clone();
        tempP.find("input").val('');
        tempP.find('i.price_sale_house_01').replaceWith('<i class="price_sale_house_02"></i>');
        $(this).parent().parent().append(tempP);
    }).on('click', 'i.price_sale_house_02', function () {
        // 删除同行价
        $(this).parent().remove();
        getTradePrice();
        peopleCountChange();
    });
    // 费用调整
    $("#adjustCost").on("change", "input:text.price, input:radio, select", function () {
        offersChange();
    });

    //添加游客信息
    $("#addpassengerInfo").on('click', function () {
        if ($('#passengerInfoTable tbody tr').length > orderInfo.totalCount) return;
        $('#passengerInfoTable').show();
        var html = '<tr>' + $('#add_tours_obj_tr').html() + '</tr>';
        $('#passengerInfoTable tbody').append(html);
        updateSequence();
    });

    // 团号选择
    $('#selNo').on('change', function () {
        var val = $(this).val();
        if (val == 1) {
           /*  $('.yes_edit_msg').show();
            $('.group_number_orders').hide();
            $('.group_orders_number').show();
            $('.no_edit_msg').hide();
            $('#costTable').find("tbody").eq(0).hide();
            $('#costTable').find("tbody").eq(1).show(); */
            
            $('.yes_edit_msg').show();
            $('.group_orders_number').show();
            $('.group_number_orders').hide();
            $('#groupSelect1').attr("disabled",false);
            $('#groupSelect2').attr("disabled",true);
            $('.no_edit_msg').hide();
            $('#costTable tbody:eq(0)').hide();
            $('#costTable tbody:eq(1)').show();
            
            $('.yes_edit_msg').find("input").attr("disabled",false);
            $('.no_edit_msg').find("input").attr("disabled",true);
            
            $('#no_edit_tbody').find("input").attr("disabled",true);
            $('#yes_edit_tbody').find("input").attr("disabled",false);
            $('#yes_edit_tbody').find("select").attr("disabled",false);
            
            //$("#signChannelList").find("input").removeAttr("disabled");
        	//$("#nonChannelList").find("input").attr("disabled",true);
            /* orderInfo = {
        	    	traveller: [
        	            	<c:forEach items="${travellerList }" var="groupPrice" varStatus="status">
        	                	{ type: "${groupPrice.uuid}", name: "${groupPrice.name}",
        	                		cost: ""},
        	            	</c:forEach>
        	            ],
        		    // 订单总额
        			totalCost: {},
        		    // 应收金额 accounts: {},
        		    // 已收金额
        			receipted: {},
        		    // 总人数
        			totalCount: 0
        		} */
        } else {
            /* $('.yes_edit_msg').hide();
            $('.no_edit_msg').show();
            $('.group_orders_number').hide();
            $('.group_number_orders').show();
            $('#costTable').find("tbody").eq(0).show();
            $('#costTable').find("tbody").eq(1).hide(); */
            
            $('.yes_edit_msg').hide();
            $('.no_edit_msg').show();
            $('.group_orders_number').hide();
            $('.group_number_orders').show();
            $('#groupSelect1').attr("disabled",true);
			$('#groupSelect2').attr("disabled",false);
            $('#costTable tbody:eq(0)').show();
            $('#costTable tbody:eq(1)').hide();
            
            $('.yes_edit_msg').find("input").attr("disabled",true);
            $('.no_edit_msg').find("input").attr("disabled",false);
            
            $('#no_edit_tbody').find("input").attr("disabled",false);
            $('#yes_edit_tbody').find("input").attr("disabled",true);
            $('#yes_edit_tbody').find("select").attr("disabled",true);
            
            orderInfo = {
            	    /* traveller: [
            	        { type: "adult", name: "成人" },
            	        { type: "baby", name: "儿童" },
            	        { type: "baby1", name: "儿童占床" }
            	    ], */
                	traveller: [
                            	<c:forEach items="${priceList }" var="groupPrice" varStatus="status">
                                	{ type: "${groupPrice.type}", name: "<trekiz:autoId2Name4Table tableName='traveler_type' sourceColumnName='uuid' srcColumnName='name' value='${groupPrice.type}'/>",
                                		cost: { '<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groupPrice.currencyId}"/>': "${groupPrice.price}"}},
                            	</c:forEach>
                        ],
            	    // 订单总额
                    totalCost: {},
            	    // 应收金额 accounts: {},
            	    // 已收金额
            		receipted: {},
            	    // 总人数
            		totalCount: 0
            	}
        }
        var v_hotelUuid = '${activityHotel.hotelUuid}';
        if(v_hotelUuid){
	        writeTravelSel(v_hotelUuid);
        }
        getTradePrice();
        peopleCountChange();
    }).change();
});

function getTradePrice() {
    /* var $tbody = $("#costTable>tbody:visible");
    if ($("#costTable>tbody:first").is(":visible")) {
        for (var l = orderInfo.traveller.length; l--;) {
            var traveller = orderInfo.traveller[l];
            var cost = $tbody.find("tr#" + traveller.type).find("td:eq(1) span").attr("data-cost");
            if (cost) {
                var costs = cost.split(";");
                traveller.cost = {};
                for (var i = costs.length; i--;) {
                    var arr = costs[i].split(":");
                    (+arr[1]) && (traveller.cost[arr[0]] = (+arr[1]));
                }
            }
        }
    } else {
        for (var l = orderInfo.traveller.length; l--;) {
            var traveller = orderInfo.traveller[l];
            var $p = $tbody.find("tr#" + traveller.type).find("td:eq(1) p");
            traveller.cost = {};
            var cost = (+$p.find("input").val());
            if (cost) {
                var currency = $p.find("select option:selected").attr("data-currency");
                traveller.cost[currency] = (traveller.cost[currency] || 0) + cost;
            }
        }
    } */
    var $tbody = $("#costTable tbody:visible");
    if ($("#costTable tbody:first").is(":visible")) {
        for (var l = orderInfo.traveller.length; l--;) {
            var traveller = orderInfo.traveller[l];
            //var cost = $tbody.find("tr #" + traveller.type).find("td:eq(1) span").attr("data-cost");
            var cost = $tbody.find("tr#" + traveller.type).find("td:eq(1) span").attr("data-cost");
            if (cost) {
                var costs = cost.split(";");
                traveller.cost = {};
                for (var i = costs.length; i--;) {
                    var arr = costs[i].split(":");
                    (+arr[1]) && (traveller.cost[arr[0]] = (+arr[1]));
                }
            }
        }
    } else {
        for (var l = orderInfo.traveller.length; l--;) {
            var traveller = orderInfo.traveller[l];
            var $p = $tbody.find("tr#" + traveller.type).find("td:eq(1) p");
            traveller.cost = {};
            var cost = (+$p.find("input").val());
            if (cost) {
                var currency = $p.find("select option:selected").attr("data-currency");
                traveller.cost[currency] = (traveller.cost[currency] || 0) + cost;
            }
        }
    }
}

//费用调整添加其他费用
var ot_id=0;
function add_other_charges() {
	ot_id+=1;
	var html = $('.add_other_charges').clone();
	html.find('td.tr_other_u').replaceWith('<td class="tr"><input name="other_u'+ot_id+'" type="radio" class="dis_inlineblock" id="u138_input" value="radio" checked="checked" data-label="增加" /><label for="u138_input">增加</label> <input id="u138_input2" value="radio" data-label="减少" name="other_u'+ot_id+'" type="radio" /><label for="u140_input">减少：</label></td>');
	html.attr('class', 'clone_other_charges calc_amount_tr').show();
	$('#add_other_charges_table').append(html);
}
function del_other_charges(obj) {
	$(obj).parent().parent().remove();
	offersChange();
}

function peopleCountChange() {
 	// 总人数
    var totalCount = 0;
    // 总费用
    var totalCost = {};
    var $tbody = $("#costTable tbody:visible");
    for (var l = orderInfo.traveller.length; l--;) {
        var traveller = orderInfo.traveller[l];
        var count = parseInt($tbody.find("tr#" + traveller.type).find("td:eq(2) input").val());
        if (count) {
            var cost = {};
            for (var k in traveller.cost) {
                totalCost[k] || (totalCost[k] = 0);
                cost[k] = traveller.cost[k] * count;
                totalCost[k] += cost[k];
            }
            $tbody.find("tr#" + traveller.type).find("td:last").text(formatCost(cost));
            if(traveller.cost[k]>0){
	            totalCount += count;
            }
        }else{
        	$tbody.find("tr#" + traveller.type).find("td:last").text('');
        }
    }
    orderInfo.totalCost = totalCost;
    orderInfo.totalCount = totalCount;
    $("span.totalPeopleCount").text(totalCount);
    $("#orderPersonNum").val(totalCount);
    showCostMsg();
    $tbody = $("#passengerInfoTable > tbody");
    var subCount = totalCount - $tbody.children("tr:visible").length;
    if (subCount < 0) {
        $tbody.children("tr:gt(" + (totalCount - 1) + ")").remove();
    }
}

function offersChange() {
    var offersCost = {};
    $("#adjustCost tr:visible").each(function (i) {
        var $this = $(this);
        var currency, money;
        if (i < 3) {
            if ($this.find("input:text:first").is(".price")) {
                currency = $this.find("select option:selected").attr("data-currency");
                money = parseFloat($this.find("input:text:first").val());
            }
        } else {
            currency = $this.find("select option:selected").attr("data-currency");
            money = parseFloat($this.find("input:text:eq(1)").val());
            if ($this.find("input:radio").prop("checked") && money) {
                money = 0 - money;
            }
        }
        if (currency && money) {
            offersCost[currency] || (offersCost[currency] = 0);
            offersCost[currency] += money;
        }
    });
    orderInfo.offersCost = offersCost;
    showCostMsg();
}

function showCostMsg() {
    orderInfo.accounts = subCost(orderInfo.totalCost, orderInfo.offersCost);
    orderInfo.unReceipted = subCost(orderInfo.accounts, orderInfo.receipted);
    // 订单总额
    $("span.totalCost").text(formatCost(orderInfo.totalCost));
    // 结算总额
    $("span.accounts").text(formatCost(orderInfo.accounts));
    // 未收总额
    $("span.unReceipted").text(formatCost(orderInfo.unReceipted));
}

function formatCost(cost) {
    var str = []
    for (var k in cost) {
        if (cost[k]) {
            if (cost[k] > 0 && str.length) {
                str.push(" + ");
            }
            else if (cost[k] < 0) {
                str.push(" - ");
            }
            str.push(k + Math.abs(cost[k]).toFixed(2));
        }
    }
    return str.join('');
}

function subCost(cost1, cost2) {
    var cost = $.extend({}, cost1);
    if (!cost2) return cost;
    for (var k in cost2) {
        cost[k] = (cost[k] || 0) - cost2[k];
    }
    return cost;
}

$(function () {
    $('.closeNotice').click(function () {
        var par = $(this).parent().parent();
        par.hide();
        par.prev().removeClass('border-bottom');
        par.prev().find('.notice-date').show();
    });
    $('.showNotice').click(function () {
        $(this).parent().hide();
        var par = $(this).parent().parent();
        par.addClass('border-bottom');
        par.next().show();
    });
});
$(function () {
    $('.main-nav li').click(function () {
        $(this).addClass('select').siblings().removeClass('select');
    })
});
String.prototype.formatNumberMoney = function (pattern) {
    var strarr = this ? this.toString().split('.') : ['0'];
    var fmtarr = pattern ? pattern.split('.') : [''];
    var retstr = '';
    var str = strarr[0];
    var fmt = fmtarr[0];
    var i = str.length - 1;
    var comma = false;
    for (var f = fmt.length - 1; f >= 0; f--) {
        switch (fmt.substr(f, 1)) {
            case '#':
                if (i >= 0) retstr = str.substr(i--, 1) + retstr;
                break;
            case '0':
                if (i >= 0) retstr = str.substr(i--, 1) + retstr;
                else retstr = '0' + retstr;
                break;
            case ',':
                comma = true;
                retstr = ',' + retstr;
                break;
        }
    }
    if (i >= 0) {
        if (comma) {
            var l = str.length;
            for (; i >= 0; i--) {
                retstr = str.substr(i, 1) + retstr;
                if (i > 0 && ((l - i) % 3) == 0) retstr = ',' + retstr;
            }
        } else retstr = str.substr(0, i + 1) + retstr;
    }
    retstr = retstr + '.';
    str = strarr.length > 1 ? strarr[1] : '';
    fmt = fmtarr.length > 1 ? fmtarr[1] : '';
    i = 0;
    for (var f = 0; f < fmt.length; f++) {
        switch (fmt.substr(f, 1)) {
            case '#':
                if (i < str.length) retstr += str.substr(i++, 1);
                break;
            case '0':
                if (i < str.length) retstr += str.substr(i++, 1);
                else retstr += '0';
                break;
        }
    }
    return retstr.replace(/^,+/, '').replace(/\.$/, '');
}
String.prototype.replaceSpecialChars = function (regEx) {
    if (!regEx) {
        regEx = /[\`\"\~\!\@#$\%\^\&\*\(\)\+\=\|\{\}\'\:\;\'\,\/\/\[\/\/\]\.\<\>\/\?\~\！\@#\¥\%\…\…\&\*\（\）\—\—\+\|\{\}\【\】\‘\；\：\”\“\’\。\，\、\？]/g;
    }
    return this.replace(regEx, '');
};

//切换 日期列表 与 酒店列表
$(function () {
    $('div.filterbox').on('click', '.date2', function () {
        $('div.house_list_out').show();
        $('div.house_list_out02').hide();
        $(this).removeClass().addClass('date');
        $("i.hotel").removeClass().addClass("hotel2");
    });
    $('div.filterbox').on('click', '.hotel2', function () {
        $('div.house_list_out').hide();
        $('div.house_list_out02').show();
        $(this).removeClass().addClass('hotel');
        $("i.date").removeClass().addClass("date2");
    });
});
</script></body>
</html>