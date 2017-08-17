<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.trekiz.admin.modules.sys.utils.UserUtils" %>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>

<%
String flag= "";
if (UserUtils.getUser().getCompany().getIsNeedAttention() == 1) 
flag = "1";
else
	flag="0";
%>


<head>
<meta name="decorator" content="wholesaler"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单-销售签证订单</title>
	<%--t2改版 去掉重复引用的样式 modified by Tlw--%>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css" rel="stylesheet" type="text/css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript" src="${ctxStatic}/modules/visa/visaOrderList.js"></script>

<script type="text/javascript">
function selectQuery(){
	$("#sysCountryId" ).comboboxInquiry();
    $("#agentinfoId" ).comboboxInquiry(); 
    $("#createBy" ).comboboxInquiry();
	$("#salerId" ).comboboxInquiry();
}
$(function(){
g_context_url = "${ctx}";
	launch();
	//操作浮框
	operateHandler();
	inputTips();
	//产品名称团号切换
	switchNumAndPro();
	//产品销售和下单人切换
	switchSalerAndPicker();
	//订单应收金额和未收余额切换
	switchYingshouAndWeishou();
	
	selectQuery();
	show('${flag}','${showList}');
	//达账和撤销提示
	$(".notice_price").hover(function(){
		$(this).find("span").show();
	},function(){
		$(this).find("span").hide();
	});
	//渠道联系人增加"..."
	sliceAgentContacts();
	
	    $("#visa").hover(function () 
		{  
	    	$("#tm").val(new Date().getTime());
	    },   
        function () 
	    {  
	    	var tm2  = new Date().getTime();
	    	var tm1= $("#tm").val();
	    	//alert(tm2 - tm1); 执行更新fangfa
        });
});

function exportExcelUserList(){
	var sysCtx = $("#sysCtx").val();
	$('#searchForm').attr("action",  sysCtx + "/visa/order/exportExcelUserList");
	$("#searchForm").submit();
}

function searchOrderList(){
	// TODO
    // 通过比较tab页显示权限和参与搜索的订单状态条件，得出需要回显的tab页
	var showType = fixShowType($("#orderPayStatus").val());
	$("#showType").val(showType);
	// 组织表单提交
	var sysCtx = $("#sysCtx").val();
	$('#searchForm').attr("action",  sysCtx + "/visa/order/searchxs");
	$("#searchForm").submit();
}

//点击批量支付付款按钮
function batchPay(aId,checkName){
	var str=document.getElementsByName(checkName);
	var objarray=str.length;
	var chestr="";
	var agentId ="";
	var xuanzhongfukuanName ="";
	//不符合标准的个数
	var errorCount =0;
	var xuanzhongCount=0;
	for (var i=0;i<objarray;i++){
	  if(str[i].checked == true)
	  {
	   xuanzhongCount++;
	   var tid=str[i].value.split("@")[0];
	   var xuanzhongAgentId = document.getElementById("activityId_agentId_"+tid).value;
	   if(agentId != ""){
		   if(agentId != xuanzhongAgentId){
			   top.$.jBox.tip('不能选择多个渠道,游客姓名:'+str[i].nextSibling.nodeValue+"与其他游客不属于同一个渠道");
			   return false;
		   }
	   }else{
		  	agentId = xuanzhongAgentId;
	   }
	   
	   chestr+=str[i].value+",";
	   var xuanzhongfukuanFlag = document.getElementById("fukuan_flag_"+tid).value;
	   if("fasle" == xuanzhongfukuanFlag){
	   	xuanzhongfukuanName = xuanzhongfukuanName + str[i].nextSibling.nodeValue +" ";
	   	errorCount++;
	   }
	  }
	}
	
	if("" == chestr || chestr.length ==0){
		top.$.jBox.tip('至少选择一位游客');
        return false;
	}else{
		//筛选批量付款游客
		getPayTravelerInfo(chestr);

	}
		
	
} 

//点击批量退押金
function batchtuiyajin(aId,checkName){
	var str=document.getElementsByName(checkName);
	var objarray=str.length;
	//游客id列表
	var tidArray="";
	//订单id
	var visaOrderId ="";
	

	for (var i = 0; i < objarray; i++) {
			if (str[i].checked == true) {
				var vid = str[i].value.split("@")[1];
				//如果选中的游客不属于一个订单,清空订单id
				if (visaOrderId != "") {
					if (visaOrderId != vid) {
						visaOrderId = "";
						break;
					}
				} else {
					visaOrderId = vid;
				}
			}
		}

		for (i = 0; i < objarray; i++) {
			if (str[i].checked == true) {
				//选中游客的id
				var tid = str[i].value.split("@")[0];
				tidArray += tid + ",";
			}
		}

		if ("" == tidArray || tidArray.length == 0) {
			top.$.jBox.tip('至少选择一位游客');
			return false;
		} else {
			batchHsj(visaOrderId,tidArray);
		}
	}

	//单个游客点击付款按钮
	function pay(tid, visaId, aId) {
		if ("" == tid || tid.length == 0 || "" == visaId || visaId.length == 0
				|| "" == aId || aId.length == 0) {
			top.$.jBox.tip('至少选择一位游客');
			return false;
		} else {
			var chestr = tid + "@" + visaId;
			var obj = document.getElementById(aId);
			obj.href = "${ctx}/visa/order/batchPay?parString=" + chestr;
		}
	}

	function show(flag, showList) {
		if ("zhankai" == flag) {
			document.getElementById("showFlag").value = "zhankai";
			if ($('.ydxbd').is(":hidden") == true) {
				document.getElementById("showFlag").value = "zhankai";
				$('.ydxbd').show();
//				$('.zksx').text('收起筛选');
				$('.zksx').addClass('zksx-on');
			}
		}
		if ("youke" == showList) {
			$("#showList").val("youke");
			document.getElementById("dingdanliebiao").className = "";
			document.getElementById("youkeliebiao").className = "select";
			document.getElementById("youke").style.display = 'block';
			document.getElementById("dingdan").style.display = 'none';
		} else {
			$("#showList").val("dingdan");
			document.getElementById("dingdanliebiao").className = "select";
			document.getElementById("youkeliebiao").className = "";
			document.getElementById("dingdan").style.display = 'block';
			document.getElementById("youke").style.display = 'none';
		}
	}

	function showOrder() {

		$("#showList").val("dingdan");
		document.getElementById("dingdanliebiao").className = "select";
		document.getElementById("youkeliebiao").className = "";
		document.getElementById("dingdan").style.display = 'block';
		document.getElementById("youke").style.display = 'none';
		$("#pageNo").val(1);
		$("#pageSize").val(10);
		$("#youkePageNo").val(1);
		$("#youkePageSize").val(10);
		$("#searchForm").submit();
	}

	function showTraveler() {

		$("#showList").val("youke");
		document.getElementById("dingdanliebiao").className = "";
		document.getElementById("youkeliebiao").className = "select";
		document.getElementById("youke").style.display = 'block';
		document.getElementById("dingdan").style.display = 'none';
		$("#pageNo").val(1);
		$("#pageSize").val(10);
		$("#youkePageNo").val(1);
		$("#youkePageSize").val(10);
		$("#searchForm").submit();
	}

	//展开筛选按钮
	function launch() {
		$('.zksx').click(function() {
			if ($('.ydxbd').is(":hidden") == true) {
				document.getElementById("showFlag").value = "zhankai";
				$('.ydxbd').show();
//				$('.zksx').text('收起筛选');
				$('.zksx').addClass('zksx-on');
			} else {
				document.getElementById("showFlag").value = "shouqi";
				$('.ydxbd').hide();
//				$('.zksx').text('展开筛选');
				$('.zksx').removeClass('zksx-on');
			}
		});
	}
	
	/*订单列表-下载-确认单*/
	function downloadData(orderId) {
		window.open(encodeURI(encodeURI(g_context_url + "/sys/docinfo/download/" + orderId)));
	}
	
	function expand(order,orderId,obj,lockStatus,activityLockStatus,main_orderId) {
		//供应商唯一标识uuid
		var companyUuid="${fns:getUser().company.uuid }";
		
		if($(order).is(":hidden")){
				$(obj).html("收起客户");
				$(obj).parents('tr').addClass('tr-hover');
				$(order).show();
				$(obj).parents("td").addClass("td-extend");
				$.ajax({
					cache:true,
					type: "POST",
					url: "${ctx}/visa/order/searchTravelerByOrderId",
					data:{
						orderId:orderId,
						shenfen:"xiaoshou"
					},
					async:true,
					success: function(traveler){
							var html="";
							
							for(var i = 0,len = traveler.length; i<len; i++){
								html += '<tr>';
								html += '<td class="p0">';
								if(traveler[i].paymentType != '' && traveler[i].paymentType != null) {
									html += '<div class="ycq yj" style="margin-top:1px;">';
									html += '${traveler.paymentType}';
									html += '</div>';
								}
								html += '<span id="traveler_travelerName" class="sqcq-fj">';
								html += '<input type="checkbox" class="tdCheckBox" travelerid="'+traveler[i].id+'" orderId="'+orderId+'" visaId="'+traveler[i].visaId+'" trallerName="'+traveler[i].travelerName+'" value="'+traveler[i].id+'@'+orderId+'" name="activityId'+orderId+'" onclick="idcheckchg(this,'+orderId+')"/>';
								html += traveler[i].travelerName;
								html += '</span>';
								html += '<input type="hidden" value="'+traveler[i].agentinfoId+'" id="activityId_agentId_'+traveler[i].id+'">';
								html += '<input type="hidden" value="'+traveler[i].fukuanButtonFlag+'" id="fukuan_flag_'+traveler[i].id+'">';
								html += '</td>';
								html += '<td class="tc">';
								html += '<span id="traveler_passportId">'+traveler[i].passportId+'</span>';
								html += '</td>';
								html += '<td class="tc">';
								html += '<span id="traveler_visaArea">'+traveler[i].collarZoning+'</span>';
								html += '</td>';
								html += '<td class="tc">';
								html += '<span id="traveler_mainOrderNum">'+traveler[i].mainOrderNum+'</span>';
								html += '</td>';
								html += '<td class="tc">';
								html += '<span id="traveler_cantuantuanhao">'+traveler[i].cantuantuanhao+'</span>';
								html += '</td>';
								html += '<td class="tc">';
								html += '<span>'+traveler[i].groupType+'</span>';
								html += '</td>';
								html += '<td class="tc">';
								html += '<div class="yfje_dd">';
								html += '<span class="fbold">';
								html += '<span id="traveler_forecastStartOut">'+traveler[i].forecastStartOut+'</span>';
								html += '</span>';
								html += '</div>';
								html += '<div class="dzje_dd">';
								html += '<span class="fbold" >';
								html += '<span id="traveler_startOut">'+traveler[i].startOut+'</span>';
								html += '</span>';
								html += '</div>';
								html += '</td>';
								html += '<td class="tc">';
								html += '<div class="yfje_dd">';
								html += '<span class="fbold">';
								html += '<span id="traveler_forecastContract">'+traveler[i].forecastContract+'</span>';
								html += '</span>';
								html += '</div>';
								/* c482懿洋假期美国签证的游客列表和订单列表 */
								if(companyUuid=='f5c8969ee6b845bcbeb5c2b40bac3a23' && traveler[i].visaCountry=='美国'){
								/*隐藏实际约签时间  */
								html += '<div class="dzje_dd" style="display:none">';
								}else{
								/*显示实际约签时间  */
								html += '<div class="dzje_dd">';
								}
								html += '<span class="fbold" >';
								html += '<span id="traveler_contract">'+traveler[i].contract+'</span>';
								html += '</span>';
								html += '</div>';
								html += '</td>';
								//说明会时间 begin
								html += '<td>';
								html += '<span class="fbold">';
								html += '<span id="traveler_explanationMeeting" style="color:green">'+traveler[i].explanationTime+'</span>';
								html += '</span>';
								html += '</td>';
								//说明会时间 end
								html +='<td class="tc">';
								html +='<c:forEach items="${visaStatusList}" var="visaStatus">';
								if("${visaStatus.value }" == 2){
									if("${visaStatus.value }" == traveler[i].visaStatus){
										html +='已约签';
									}
								}else if("${visaStatus.value}" == 3){
									if("${visaStatus.value }" == traveler[i].visaStatus){
										html +='通过';
									}
								}else{
									if("${visaStatus.value }" == traveler[i].visaStatus){
										html +='${visaStatus.label}';
									}
								}
								html +='</c:forEach>';
								html +='</td>';
								html +='<td class="tc">';
								if('1'==traveler[i].passportStatus){
									html +='借出';
								}
								if('2'==traveler[i].passportStatus){
									html +='销售已领取';
								}
								if('4'==traveler[i].passportStatus){
									html +='已还';
								}
								if('5'==traveler[i].passportStatus){
									html +='已取出';
								}
								if('6'==traveler[i].passportStatus){
									html +='未取出';
								}
								if('8'==traveler[i].passportStatus){
									html +='计调领取';
								}
								html +='</td>';
								html +='<td class="tc">';
								if('1'==traveler[i].guaranteeStatus){
									html +='担保';
								}
								if('2'==traveler[i].guaranteeStatus){
									html +='担保+押金';
								}
								if('3'==traveler[i].guaranteeStatus){
									html +='押金';
								}
								if('4'==traveler[i].guaranteeStatus){
									html +='无需担保';
								}
								html +='</td>';
								var totalDeposit = "";
								if('2'==traveler[i].guaranteeStatus||'3'==traveler[i].guaranteeStatus){
									totalDeposit = traveler[i].totalDeposit;
								}
								html +='<td class="tr">'+totalDeposit+'</td>';
								html +='<td class="p0 tr">';
								html +='<div class="yfje_dd">';
								html +='<span class="fbold" id="traveleryingshouyajin_'+traveler[i].visaId+traveler[i].id+'">';
								//if('3'==traveler[i].guaranteeStatus || '2'==traveler[i].guaranteeStatus){
									html +=traveler[i].payedDeposit;
								//}
								html +='</span>';
								html +='</div>';
								html +='<div class="dzje_dd">';
								html +='<span class="fbold" id="accountedDeposit">';
								html +=traveler[i].accountedDeposit;
								html +='</span>';
								html +='</div>';
								html +='</td>';
								html +='<td class="tr">';
								html +='<span class="tdorange fbold">';
								html +=traveler[i].payPriceSerialNum;
								html +='</span>';
								html +='</td>';
							   // <!-- 0615需求,新增是否需要押金,是否需要上传资料限定为批发商大洋国旅 --> 
								/*html +='<c:if test="${fns:getUser().company.uuid eq \'7a81a03577a811e5bc1e000c29cf2586\'}"><td class="tc"><span><p align="center">'+(traveler[i].visa_deposit==true?"是":"否")+'</p></span></td>';
								html +='<td class="tc"><span><p align="center">'+(traveler[i].visa_datum==true?"是":"否")+'</p></span></td></c:if>';*/
								
								html +='<td class="p0 tr">';
								html +='<div class="yfje_dd">';
								html +='<span class="fbold">';
								html +=traveler[i].payedMoneySerialNum;
								html +='</span>';
								html +='</div>';
								html +='<div class="dzje_dd">';
								html +='<span class="fbold">';
								html +=traveler[i].accountedMoney;
								html +='</span>';
								html +='</div>';
								html +='</td>';
								html +='<td class="tc"><span><p align="center">'+traveler[i].jiekuanStatus+'</p></span></td>';
								html +='<td class="p0">';
								html +='<dl class="handle">';
								html +='<dt><img src="${ctxStatic}/images/handle_cz_rebuild.png" title="操作"></dt>';
								html +='<dd>';
								html +='<p>';
								html +='<span></span>';
								if(lockStatus != 1 && activityLockStatus !=1){
									if(traveler[i].mainOrderId == '' || traveler[i].mainOrderId == null){
										html+='<c:if test="${companyUuid ne \'7a81a26b77a811e5bc1e000c29cf2586\' }">'
										html +='<a href="${ctx}/order/manager/visa/joinGroup/joinGroupForm?orderId='+orderId+'&travelerId='+traveler[i].id+'">参团</a>';
										html+='</c:if>';
										html+='<c:if test="${companyUuid eq \'7a81a26b77a811e5bc1e000c29cf2586\' }">'
											if(traveler[i].main_orderId == '' || traveler[i].main_orderId == null)
											html +='<a href="${ctx}/order/manager/visa/joinGroup/joinGroupForm?orderId='+orderId+'&travelerId='+traveler[i].id+'">参团</a>';
											html+='</c:if>';		
					      	}
									html+='<c:if test="${companyUuid ne \'7a81a26b77a811e5bc1e000c29cf2586\' }">'
// 									html +='<a href="${ctx}/visaUpProces/list?orderId='+orderId+'&productType=6&flowType=10" >改价</a>';
									html +='<a href="${ctx}/changeprice/visa/list?orderId='+orderId+'&productType=6&flowType=10" >改价</a>';
									html+='</c:if>';
									
									
									html+='<c:if test="${companyUuid eq \'7a81a26b77a811e5bc1e000c29cf2586\' }">'
										if(traveler[i].main_orderId == '' || traveler[i].main_orderId == null)
// 										html +='<a href="${ctx}/visaUpProces/list?orderId='+orderId+'&productType=6&flowType=10" >改价</a>';
										html +='<a href="${ctx}/changeprice/visa/list?orderId='+orderId+'&productType=6&flowType=10" >改价</a>';
										html+='</c:if>';
									
									
									
									
									
								//	if('3'==traveler[i].guaranteeStatus){
								//		html +='<a href="${ctx}/order/manager/visa/warrantList?proId='+orderId+'">押金转担保</a>';
								//		html +='<a href="${ctx}/order/manager/visaNew/warrantListNew?orderId='+orderId+'">New押金转担保</a>';
								//	}
									
									html +='<a target="_blank" id="piliang" onclick="deleteOneTraveler('+traveler[i].id+');" >删除</a>';
								}
								html +='<a target="_blank" href="${ctx}/guaranteeMod/list/'+orderId+'">担保变更</a>';
								html +='</p>';
								html +='</dd>';
								html +='</dl>';
								html +='</td>';
								html +='<td class="p0">';
								html +='<dl class="handle">';
								html +='<dt><img title="财务" src="${ctxStatic}/images/handle_fk_rebuild.png"></dt>';
								html +='<dd>';
								html +='<p>';
								html +='<span></span>';
								if(lockStatus != 1 && activityLockStatus !=1){
									if('true' == traveler[i].fukuanButtonFlag && 'F' != traveler[i].showFlag){
										html+='<c:if test="${companyUuid ne \'7a81a26b77a811e5bc1e000c29cf2586\'}">';
										html +='<a target="_blank" id="pay_'+traveler[i].id+'" onclick="pay(\''+traveler[i].id+'\',\''+orderId+'\',\'pay_'+traveler[i].id+'\');">收款</a>';
										html+='</c:if>';
										
										
										html+='<c:if test="${companyUuid eq \'7a81a26b77a811e5bc1e000c29cf2586\'}">';
										if(traveler[i].main_orderId == '' || traveler[i].main_orderId == null)
										html +='<a target="_blank" id="pay_'+traveler[i].id+'" onclick="pay(\''+traveler[i].id+'\',\''+orderId+'\',\'pay_'+traveler[i].id+'\');">收款</a>';
										html+='</c:if>';
										
									}
									if('true' == traveler[i].payButtonFlag && ('3' == traveler[i].guaranteeStatus || '2' == traveler[i].guaranteeStatus)){
										html +='<a target="_blank" href="${ctx}/orderPay/pay?orderId='+orderId+'&orderNum='+traveler[i].visaorderNo+'&payPriceType=7&businessType=2&orderType=6&agentId='+traveler[i].agentinfoId+'&paramCurrencyId='+traveler[i].yajinHuobiId+'&paramCurrencyPrice='+traveler[i].yajinJine+'&visaId='+traveler[i].visaId+'&orderDetailUrl=${ctx}/visa/order/goUpdateVisaOrderForSales?visaOrderId='+orderId+'&mainOrderCode='+traveler[i].mainOrderId+'&details=1&paramTotalCurrencyId='+traveler[i].totalYajinHuobi+'&paramTotalCurrencyPrice='+traveler[i].totalYajinJine+'">收押金</a>';
									}
									if("${tuiYaJinFlag}"=='true'){
									//	html +='<a href="${ctx}/order/manager/visaDeposit/refundList?proId='+orderId+'">退押金</a>';
									}
									if("${huanYaJinShouJuFlag}"=='true'){
									 //	html +='<a href="javascript:void(0)" onclick="jbox_hyjsj(\''+traveler[i].id+'\');">还押金收据</a>';
									 //	html +='<a href="javascript:void(0)" onclick="jbox_hyjsj_new(\''+traveler[i].id+'\');">New还押金收据</a>';
									}
								}
								html +='</p>';
								html +='</dd>';
								html +='</dl>';
								html +='</td>';
								html +='</tr>';
							}
							html +='<tr class="checkalltd">';
							html +='<td colspan="19" class="tl">';
							html +='<label>';
							html +='<input type="checkbox" name="allChk'+orderId+'" onclick="checkall(this,'+orderId+')">全选';
							html +='</label>';
							html +='<label>';
							html +='<input type="checkbox" name="allChkNo" onclick="checkallNo(this,'+orderId+')">反选';
							html +='</label>';
							if(lockStatus != 1 && activityLockStatus !=1){
								
								html +='<c:if test="${companyUuid ne '7a81a26b77a811e5bc1e000c29cf2586'}">';
								html +='<a target="_blank" id="piliang_o_'+orderId+'" onclick="batchPay(\'piliang_o_'+orderId+'\',\'activityId'+orderId+'\');" >批量收款</a>';
								html +='</c:if>';
								
								
								html +='<c:if test="${companyUuid eq '7a81a26b77a811e5bc1e000c29cf2586'}">';
								if(main_orderId == '' || main_orderId == null)
								html +='<a target="_blank" id="piliang_o_'+orderId+'" onclick="batchPay(\'piliang_o_'+orderId+'\',\'activityId'+orderId+'\');" >批量收款</a>';
								html +='</c:if>';
								
								
								
								
								
								
								
//								if("${tuiYaJinFlag}"=='true'){
//									html +='<a id="piliang_o_tuiyajin_'+orderId+'" onclick="batchtuiyajin(\'piliang_o_tuiyajin_'+orderId+'\',\'activityId'+orderId+'\');">批量退押金</a>';
//								}
								html +='<a target="_blank" id="piliang" onclick="batchDeleteTraveler('+orderId+');" >批量删除游客</a>';
							}
							html +='</td>';
							html +='</tr>';
							
							$("#traveler_"+orderId).empty();
							$("#traveler_"+orderId).append(html);
					}
				});
		}else{
			if(!$(order).is(":hidden")){
				$(obj).parents('tr').removeClass('tr-hover');
				$(order).hide();
				$(obj).parents("td").removeClass("td-extend");
				$(obj).html("展开客户");
			}
		}
	}

	//订单列表全选
	//var activityIds = "";
	function checkall(obj, orderId) {
		if (obj.checked) {
			$("input[name='activityId" + orderId + "']").not("input:checked")
					.each(function() {
						this.checked = true;
					});
			$("input[name='allChk" + orderId + "']").not("input:checked").each(
					function() {
						this.checked = true;
					});
		} else {
			$("input[name='activityId" + orderId + "']:checked").each(
					function() {
						this.checked = false;
					});
			$("input[name='allChk" + orderId + "']:checked").each(function() {
				this.checked = false;
			});
		}
	}
	function checkallNo(obj, orderId) {
		$("input[name='activityId" + orderId + "']").each(function() {
			$(this).attr("checked", !$(this).attr("checked"));
		});
		allchk(orderId);
	}
	//每行中的复选框
	function idcheckchg(obj, orderId) {
		/*	// debugger;  
		debugger
		//  $('input[type=checkbox][name=items]').attr("checked", false );	
		if (obj.checked) {
			if ($("input[name='activityId" + orderId + "']").not("input:checked").length === 1) {
				
				$("input[name='allChk" + orderId + "']").not("input:checked")
						.each(function() {
							this.checked = true;
						});
			}
		} else {
			$("input[name='allChk" + orderId + "']:checked").each(function() {
				this.checked = false;
			})
		}
		*/
		if ($("input[name='activityId" + orderId + "']").not("input:checked").length) {
			$("input[name='allChk" + orderId + "']").attr("checked",false);
		}else{

			$("input[name='allChk" + orderId + "']").attr("checked",true);
		}
	}
	function allchk(orderId) {
		var chknum = $("input[name='activityId" + orderId + "']").size();
		var chk = 0;
		$("input[name='activityId" + orderId + "']").each(function() {
			if ($(this).attr("checked") == true) {
				chk++;
			}
		});
		if (chknum == chk) {//全选 
			$("input[name='allChk" + orderId + "']").attr("checked", true);
		} else {//不全选 
			$("input[name='allChk" + orderId + "']").attr("checked", false);
		}
	}

	//游客列表全选
	//var activityIds = "";
	function t_checkall(obj) {
		if (obj.checked) {
			$("input[name='activityId']").not("input:checked").each(function() {
				this.checked = true;
			});
			$("input[name='allChk']").not("input:checked").each(function() {
				this.checked = true;
			});
		} else {
			$("input[name='activityId']:checked").each(function() {
				this.checked = false;
			});
			$("input[name='allChk']:checked").each(function() {
				this.checked = false;
			});
		}
	}
	function t_checkallNo(obj) {
		$("input[name='activityId']").each(function() {
			$(this).attr("checked", !$(this).attr("checked"));
		});
		t_allchk();
	}
	//每行中的复选框
	function t_idcheckchg(obj) {
		
		
		if (obj.checked) {
			
			
			if ($("input[name='activityId']").not("input:checked").length == 0) {
				$("input[name='allChk']").not("input:checked").each(function() {
					this.checked = true;
				});
			}
		} else {
			$("input[name='allChk']:checked").each(function() {
				this.checked = false;
			});
		}
	}
	function t_allchk() {
		var chknum = $("input[name='activityId']").size();
		var chk = 0;
		$("input[name='activityId']").each(function() {
			if ($(this).attr("checked") == true) {
				chk++;
			}
		});
		if (chknum == chk) {//全选 
			$("input[name='allChk']").attr("checked", true);
		} else {//不全选 
			$("input[name='allChk']").attr("checked", false);
		}
	}

	function sortby(sortBy, obj) {
		var temporderBy = $("#orderBy").val();
		if (temporderBy.match(sortBy)) {
			sortBy = temporderBy;
			if (sortBy.match(/ASC/g)) {
				sortBy = $.trim(sortBy.replace(/ASC/gi, "")) + " DESC";
			} else {
				sortBy = $.trim(sortBy.replace(/DESC/gi, "")) + " ASC";
			}
		} else {
			sortBy = sortBy + " DESC";
		}
		$("#searchForm").attr("action", "${ctx}/visa/order/searchxs");
		$("#orderBy").val(sortBy);
		$("#searchForm").submit();
	}

	function youkesortby(sortBy, obj) {
		var temporderBy = $("#youkeOrderBy").val();
		if (temporderBy.match(sortBy)) {
			sortBy = temporderBy;
			if (sortBy.match(/ASC/g)) {
				sortBy = $.trim(sortBy.replace(/ASC/gi, "")) + " DESC";
			} else {
				sortBy = $.trim(sortBy.replace(/DESC/gi, "")) + " ASC";
			}
		} else {
			sortBy = sortBy + " DESC";
		}
		$("#searchForm").attr("action", "${ctx}/visa/order/searchxs");
		$("#youkeOrderBy").val(sortBy);
		$("#searchForm").submit();
	}

	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);

		$("#youkePageNo").val(n);
		$("#youkePageSize").val(s);
		$("#searchForm").submit();
	}

	//订单-销售身份订单-还押金收据----wxw added----
	function jbox_hyjsj(travelerID) {
		//alert("还押金收据！");
		//alert(travelerID);
		//收据金额: depositReceiptAmount
		//还收据人: depositReceiptor
		//归还时间: depositReceiptReturnTime
		var html = '<div style="margin-top:20px; padding-left:20px;">';
		html += '<label class="jbox-label">收据金额：</label><input name="depositReceiptAmount"  type="text" /><br /><label class="jbox-label">还收据人：</label><input name="depositReceiptor" type="text" /><br /><label class="jbox-label">归还时间：</label><input name="depositReceiptReturnTime" onclick="WdatePicker()" class="inputTxt dateinput"  type="text" />';
		html += '</div>';
		$
				.jBox(
						html,
						{
							title : "还押金收据",
							buttons : {
								'还收据' : 1
							},
							submit : function(v, h, f) {

								if (!f.depositReceiptAmount) {
									top.$.jBox.tip("收据金额为必填项！");
									return false;
								}

								var text1 = (f.depositReceiptor).replace(
										/^\s+/, "").replace(/\s+$/, "");
								if (!f.depositReceiptor || text1.length < 1) {
									top.$.jBox.tip("还收据人为必填项！");
									return false;
								}

								if (!f.depositReceiptReturnTime) {
									top.$.jBox.tip("归还时间为必填项！");
									return false;
								}

								if (isNumber(f.depositReceiptAmount)) {
									$
											.ajax({
												type : "POST",
												url : "${ctx}/visa/workflow/returndepositreceipt/createVisaHYJSJ",
												dataType : "json",
												data : {
													"travelerID" : travelerID,
													"depositReceiptAmount" : f.depositReceiptAmount,
													"depositReceiptor" : f.depositReceiptor,
													"depositReceiptReturnTime" : f.depositReceiptReturnTime
												},
												async : false,
												success : function(msg) {
													top.$.jBox
															.tip(msg.visaHYJSHreply);
												}
											});
								} else {
									top.$.jBox.tip("收据金额必须为数字或小数！");
									return false;
								}
							},
							height : 220,
							width : 380
						});
	}
	
	//(新审核)订单-销售身份订单-还押金收据----jyang added by 2015年11月26日 18:26:02----
	//收据金额: depositReceiptAmount
	//还收据人: depositReceiptor
	//归还时间: depositReceiptReturnTime
	function jbox_hyjsj_new(travelerID) {
		var html = '<div style="margin-top:20px; padding-left:20px;">';
		html += '<label class="jbox-label">收据金额：</label><input name="depositReceiptAmount"  type="text" /><br /><label class="jbox-label">还收据人：</label><input name="depositReceiptor" type="text" /><br /><label class="jbox-label">备注：</label><input name="remark" type="text" /><br /><label class="jbox-label">归还时间：</label><input name="depositReceiptReturnTime" onclick="WdatePicker()" class="inputTxt dateinput"  type="text" />';
		html += '</div>';
		$.jBox(html,{
				title : "还押金收据",
				buttons : {'还收据' : 1},
				submit : function(v, h, f) {
					if (!f.depositReceiptAmount) {
						top.$.jBox.tip("收据金额为必填项！");
						return false;
					}
					var text1 = (f.depositReceiptor).replace(/^\s+/, "").replace(/\s+$/, "");
					if (!f.depositReceiptor || text1.length < 1) {
						top.$.jBox.tip("还收据人为必填项！");
						return false;
					}
					if (!f.depositReceiptReturnTime) {
						top.$.jBox.tip("归还时间为必填项！");
						return false;
					}
					if (isNumber(f.depositReceiptAmount)) {
						$.ajax({
							type : "POST",
							url : "${ctx}/newreviewvisa/returndepositreceipt/createVisaHYJSJ",
							dataType : "json",
							data : {
								"travelerID" : travelerID,
								"depositReceiptAmount" : f.depositReceiptAmount,
								"depositReceiptor" : f.depositReceiptor,
								"remark" : f.remark,
								"depositReceiptReturnTime" : f.depositReceiptReturnTime
							},
							async : false,
							success : function(result) {
								if(result.code==0){
									top.$.jBox.tip(result.message);
								}else{
									top.$.jBox.tip(result.message);
								}
							}
						});
					} else {
						top.$.jBox.tip("收据金额必须为数字或小数！");
						return false;
					}
				},
				height : 220,
				width : 380
			});
	}

	/**
	 * 必须为数字或小数点的校验
	 * 必填校验
	 */
	function isNumber(oNum) {
		if (!oNum)
			return false;
		var strP = /^\d+(\.\d+)?$/;
		if (!strP.test(oNum))
			return false;
		try {
			if (parseFloat(oNum) != oNum)
				return false;
		} catch (ex) {
			return false;
		}
		return true;
	}

	/**
	 * 订单页面查看订单发票信息列表
	 * param orderNum   订单号
	 * author     chenry
	 * createDate 2014-11-14
	 */
	function viewInvoiceInfo(orderNum, orderId, activityId) {
		window.open("${ctx}/orderInvoice/manage/getInvoiceListByOrderNum?orderNum="+ orderNum + "&orderId=" + orderId + "&activityId=" + activityId + "&orderType=6");
	}
	
	function viewReceiptInfo(orderNum, orderId, activityId) {
		window.open("${ctx}/orderInvoice/manage/supplyreceiptlist?orderNum="+ orderNum + "&orderId=" + orderId + "&activityId=" + activityId + "&orderType=6");
		
		
		//window.open("${ctx}/orderInvoice/manage/getInvoiceListByOrderNum?orderNum="+orderNum+"&orderId="+orderId+"&orderType=6");
	}
	

	/**
	 * 取消订单
	 * 
	 * param orderId
	 */
	function cancelOrder(orderId) {
		var status = true;
		$.ajax({
			type : "POST",
			url : "${ctx}/visa/order/checkOrderStatus",
			async : false,
			data : {
				orderId : orderId
			},
			success : function(msg) {
				if (msg=='已开发票') {
					top.$.jBox.tip('此订单已有开发票记录,不能取消。', 'warning');
					top.$('.jbox-body .jbox-icon').css('top', '55px');
					status=false;
				} else if (msg == '已开收据') {
					top.$.jBox.tip('此订单已有开收据记录,不能取消。', 'warning');
					top.$('.jbox-body .jbox-icon').css('top', '55px');
					status=false;
				} else {
					status=true;
				}
			}
		});
		if(status!=true){
			return;
		}
		$.jBox.confirm("确定要取消订单吗？", "系统提示", function (v, h, f) {
			if (v == 'ok') {
				$.ajax({
					type : "POST",
					url : "${ctx}/visa/order/cancelOrder",
					data : {
						orderId : orderId
					},
					success : function(msg) {
						if (msg == 'fail') {
							top.$.jBox.tip('订单不能取消', 'warning');
							top.$('.jbox-body .jbox-icon').css('top', '55px');
						} else if (msg == 'ok') {
							top.$.jBox.tip('订单已取消', 'warning');
							top.$('.jbox-body .jbox-icon').css('top', '55px');
							$("#btn_search").click();
						} else {
							top.$.jBox.tip(msg, 'warning');
							top.$('.jbox-body .jbox-icon').css('top', '55px');
						}
					}
				});
			} else if (v == 'cancel') {
				
			}
		});
	}

	/**
	 * 订单激活
	 *
	 * param orderId
	 */
	function invokeOrder(orderId) {
		$.ajax({
			type : "POST",
			url : "${ctx}/visa/order/invokeOrder",
			data : {
				orderId : orderId
			},
			success : function(msg) {
				if (msg == 'fail') {
					top.$.jBox.tip('激活失败', 'warning');
					top.$('.jbox-body .jbox-icon').css('top', '55px');
				} else if (msg == 'success') {
					top.$.jBox.tip('激活成功', 'warning');
					top.$('.jbox-body .jbox-icon').css('top', '55px');
					$("#btn_search").click();
				} else {
					top.$.jBox.tip(msg, 'warning');
					top.$('.jbox-body .jbox-icon').css('top', '55px');
				}
			}
		});
	}

	var resetSearchParams = function() {
		$(':input', '#searchForm').not(':button, :submit, :reset, :hidden')
				.val('').removeAttr('checked').removeAttr('selected');
		$('#targetAreaId').val('');
		$('#orderShowType').val('${showType}');
		$('#commonCode').val('');
		$('#sysCountryId').val('');
		$('#visaType').val('');
		$('#agentinfoId').val('');
		$('#AACode').val('');
		$('#createBy').val('');
		$('#orderPayStatus').val('');
		$('#visaType').val('');
		$('#visaStatus').val('');
		$('#salerId').val('');
	};
	
//批量还收据  orderId:订单号
function batchHsj(orderId,tidArray){

	 $.ajax({                 
			cache: true,                 
			type: "POST",                 
			url: "${ctx}" + "/order/manager/visaDeposit/checkDepositRefund",                 
			data:{ 
			    "travelerIds":tidArray
				},                
			async: false,                 
			 success: function(msg){
				 if(msg.msg!=null&&msg.msg!=""){
					 top.$.jBox.tip(msg.msg,'warning');
				 }else{
					 batchHsj1(msg);
				 }
	        }   
		});
	
}


function batchHsj1(msg){
	var rightList =msg.rightList;
	var errList =msg.errList;
	var html = '<div style="margin-top:20px;padding:0 10px;overflow:scroll;height:500px;" id="borrowWin">';
	html += '<p>不满足条件用户：</p>';
	if(errList.length==0){
		html += ' (无)';
	}else{
		html +='<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th>游客</th><th>订单编号</th><th>币种</th><th>押金金额</th><th>押金状态</th><th>不满足原因</th></tr></thead><tbody>';
		for(var i = 0,len = errList.length; i<len; i++){
			html += '<tr><td>'+errList[i].name+'</td><td>'+errList[i].code+'</td><td>'+errList[i].totalCurrency+'</td><td>'+errList[i].total+'</td><td>'+errList[i].moneyStatus+'</td><td>'+errList[i].message+'</td></tr>';
		}
		html+='</tbody></table>';
	}
	html += '<p>满足条件用户：</p>';
	if(rightList.length==0){
		html += ' (无)';
		html += '</div>';
		$.jBox(html, { title: "批量退押金",buttons:{'取消':0}, submit:function(v, h, f){
		},height:'auto',width:780});
		
	}else{
		html += '<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th width="15%">游客</th><th  width="20%">订单编号</th><th width="10%">币种</th><th width="10%">押金金额</th><th width="10%">达账押金</th><th width="10%">退款时间</th><th width="10%">申请金额</th><th width="15%">备注</th></tr></thead><tbody>';
		//html += '<tr><td><input type="checkbox"/> 张三</td><td>2323555</td><td>未借</td><td><input type="text" value="2015-03-03"/></td><td><input type="text" value="李四"/></td><td><input type="text" /></td></tr>';
		for(var i = 0,len = rightList.length; i<len; i++){
			html += '<tr><td><input name="checkedTraveler" travelerId ="'+rightList[i].id+'" travelerName="'+rightList[i].name+'" type="checkbox" checked="checked"/>'+rightList[i].name+'</td><td>'+rightList[i].code+'</td><td id="' + rightList[i].totalCurrencyId + '">'+rightList[i].totalCurrency+'</td><td>'+rightList[i].total+'</td><td>'+rightList[i].accountedMoney+'</td><td><input type="text" onclick="WdatePicker()" id="applyDate'+rightList[i].id+'"/></td><td><input type="text"  id="applyMoney'+rightList[i].id+'" value=""/></td><td><input id="applyMark'+rightList[i].id+'" type="text" /></td></tr>';
		}
		html += '<input name="visaId" value="' + rightList[0].visaId + '" style="display: none;"/></tbody></table>';
		html += '</div>';
		$.jBox(html, { title: "批量退押金",buttons:{'确定': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			var travelerIds = [];
			var travelerNames = [];
			var priceCurrency = [];
			var price = [];
			var totalPrice = [];
			var applyDates = [];
			var applyPrice = [];
			var applyMark = [];
			var t = true;
			 $("[name='checkedTraveler']:checked").each(function(){
				  if($(this).attr("checked")){
					 var travelerId = $(this).attr("travelerId");
					 var name = $(this).attr("travelerName");
					 travelerIds.push(travelerId);
					 if($("#applyDate"+travelerId).val()==null||$("#applyDate"+travelerId).val()==""){
						top.$.jBox.tip("游客"+name+"对应的退款日期不能为空",'warning');
						t=  false;
					 }
					 applyDates.push($("#applyDate"+travelerId).val());
					 if($("#applyMoney"+travelerId).val()==null||$("#applyMoney"+travelerId).val()==""){
							top.$.jBox.tip("游客"+name+"对应的申请金额不能为空",'warning');
							t=  false;
						}
					 travelerNames.push($(this).attr("travelername"));
					 priceCurrency.push($(this).parent().parent().find("td").eq(2).attr("id"));
					 price.push($(this).parent().parent().find("td").eq(3).html());
					 totalPrice.push($(this).parent().parent().find("td").eq(4).html());
					 applyPrice.push($("#applyMoney"+travelerId).val());
					 applyMark.push($("#applyMark"+travelerId).val());
				  }
			});
			 
			 if(0 == travelerIds.length){
				 top.$.jBox.tip("请勾选对应的游客",'warning');
				 return false;
			 }
			 
			 if(!t){
				 return false;
			 }
			 $.ajax({                 
					cache: true,                 
					type: "POST",                 
					url: "${ctx}" + "/order/manager/visaDeposit/refundApply",                 
					data:{ 
						proId : $("[name='visaId']").val(),
						travelerIds : travelerIds.join(),
						travelerName : travelerNames.join(),
						priceCurrency : priceCurrency.join(),
						price : price.join(),
						totalPrice : totalPrice.join(),
					    applyDates : applyDates.join(),
					    applyPrice : applyPrice.join(),
					    reasonMark : applyMark.join(),
					    ajax : 'true'
						},                
				    	async: false,                 
					    success: function(msg){
						if(msg!=null&&msg!=""){
							top.$.jBox.tip(msg,'warning');
						}else{
							top.$.jBox.tip("操作成功",'warning');
						}
			        }   
				});
			return true;
			}
		},height:'auto',width:780});
	}
}

/**
*渠道选择的时候,选择一次以后就获取不到空的值,于是就用这个方法控制
*/
function cleanAgent(){
		  var sel=document.getElementById('agentinfoId'); 
		  var str = sel.options[sel.selectedIndex].text;
		  if(null == str || 0==str.length){
		    sel.options[sel.selectedIndex].value ="";
		}
}

/**
 * 按部门查询订单
 * 
 * param departmentId
 */
function getDepartment(departmentId) {
	$("#departmentId").val(departmentId);
	$("#searchForm").submit();
}


//批量借款  orderId:订单号
function batchJk(orderId){

	//标志位 判断是否有选中
	var travellerIds ="";
	var visaIds ="";
	//游客界面
	if(orderId==null){
		$("input[type=checkbox][name=activityId]").each(function(){
			  if($(this).attr("checked")){
				  var trallerId =   $(this).attr("travelerId");
				  var visaId =   $(this).attr("visaId");
				  var trallerName =   $(this).attr("trallerName");
					 travellerIds+=trallerId+",";
					 visaIds+=visaId+",";
			  }
		});
	}else{
		$("#child_"+orderId).find("input[type=checkbox][class=tdCheckBox]").each(function(){
			  if($(this).attr("checked")){
				  var trallerId =   $(this).attr("travelerId");
				  var visaId =   $(this).attr("visaId");
				  var trallerName =   $(this).attr("trallerName");
				 var passPortStatus1 =  $("#hidden_passportStatus_"+trallerId).val();
					 travellerIds+=trallerId+",";
					 visaIds+=visaId+",";
			  }
		});
	}
	if(travellerIds==""){
		top.$.jBox.tip("请至少选择一名游客",'warning');
		return;
	}
	 $.ajax({                 
			cache: true,                 
			type: "POST",                 
			url:g_context_url+ "/visa/order/checkBatchJk",                 
			data:{ 
				"orderId":orderId,
			    "travellerIds":travellerIds,
			    "visaIds":visaIds
				},                
			async: false,                 
			 success: function(msg){
				 if(msg.msg!=null&&msg.msg!=""){
					 top.$.jBox.tip(msg.msg,'warning');
				 }else{
					 batchJk1(msg);
				 }
	        }   
		});
	
}

function batchJk1(msg){
	var rightList =msg.rightList;
	var errList =msg.errList;
	var html = '<div style="margin-top:20px;padding:0 10px;overflow:scroll;height:500px;" id="borrowWin">';
	html += '<p>不满足条件用户：</p>';
	if(errList.length==0){
		html += ' (无)';
	}else{
		html +='<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th>游客</th><th>护照号</th><th>签证费用</th><th>借款状态</th><th>原因</th></tr></thead><tbody>';
		for(var i = 0,len = errList.length; i<len; i++){
			html += '<tr><td>'+errList[i].tname+'</td><td>'+errList[i].passportCode+'</td><td>'+errList[i].visaFee+'</td><td>'+errList[i].jiekuanStatus+'</td><td>'+errList[i].errMsg+'</td></tr>';
		}
		html+='</tbody></table>';
	}
	html += '<p>满足条件用户：</p>';
	if(rightList.length==0){
		html += ' (无)';
		html += '</div>';
		$.jBox(html, { title: "批量借款",buttons:{'取消':0}, submit:function(v, h, f){
		},height:'auto',width:680});
		
	}else{
		html +='<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th width="8%">游客</th><th  width="10%">护照号</th><th width="10%">签证费用</th><th width="12%">借款日期</th><th width="10%">借款人</th><th width="10%">借款金额</th><th width="8%">借款状态</th><th width="10%">备注</th></tr></thead><tbody>';
		//html += '<tr><td><input type="checkbox"/> 张三</td><td>2323555</td><td>未借</td><td><input type="text" value="2015-03-03"/></td><td><input type="text" value="李四"/></td><td><input type="text" /></td></tr>';
		for(var i = 0,len = rightList.length; i<len; i++){
			html += '<tr><td><input visaId="'+rightList[i].visaId+'" trallerId ="'+rightList[i].tid+'"  trallerName="'+rightList[i].tname+'" type="checkbox" checked="checked"/>'+rightList[i].tname+'</td>';
			html += '<td>'+rightList[i].passportCode+'</td><td>'+rightList[i].visaFee+'</td><td><input type="text" onclick="WdatePicker()" id="borrowWinDate_'+rightList[i].visaId+'" value="'+rightList[i].curDate+'" disabled/></td>';
			html +='<td><input type="text"  id="borrowWinPerson_'+rightList[i].visaId+'" value="'+rightList[i].borrowPerson+'" disabled /></td><td><input type="text" onkeyup="clearNoNum(this)" id="borrowWinMoney_'+rightList[i].visaId+'" value="" /></td><td>'+rightList[i].jiekuanStatus+'</td><td><input id="borrowWinOther_'+rightList[i].visaId+'" type="text" /></td></tr>';
		}
		html += '</tbody></table>';
		html += '</div>';
		$.jBox(html, { title: "批量借款",buttons:{'确定': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			var travellerIds="";
			var  visaIds="";
			var dates="";
			var persons="";
			var others ="";
			var moneys="";
			var t = true;
			 $("#borrowWin").find("input[type=checkbox]").each(function(){
				  if($(this).attr("checked")){
					  var trallerId =   $(this).attr("trallerId");
					  var visaId =   $(this).attr("visaId");
					  var  name=   $(this).attr("trallerName");
					 travellerIds+=trallerId+",";
					 visaIds+=visaId+",";
					 if($("#borrowWinDate_"+visaId).val()==null||$("#borrowWinDate_"+visaId).val()==""){
						top.$.jBox.tip(name+"对应的借款日期不能为空",'warning');
						t=  false;
					}
					 dates+=$("#borrowWinDate_"+visaId).val()+",";
					 if($("#borrowWinPerson_"+visaId).val()==null||$("#borrowWinPerson_"+visaId).val()==""){
							top.$.jBox.tip(name+"对应的借款人不能为空",'warning');
							t=  false;
						}
					  persons+=$("#borrowWinPerson_"+visaId).val()+",";
					
					 if($("#borrowWinMoney_"+visaId).val()==null||$("#borrowWinMoney_"+visaId).val()==""){
							top.$.jBox.tip(name+"对应的借款金额不能为空",'warning');
							t=  false;
						}
					 moneys+=$("#borrowWinMoney_"+visaId).val()+",";
					 others+=$("#borrowWinOther_"+visaId).val()+" "+",";
				  }
			});
			 
			 if(travellerIds==""){
				 top.$.jBox.tip("请勾选对应的游客",'warning');
				 return false;
			 }
			 
			 if(!t){
				 return false;
			 }
			 $.ajax({                 
					cache: true,                 
					type: "POST",                 
					//url:g_context_url+ "/visa/order/batchJk",    
					url:g_context_url+ "/visa/workflow/borrowmoney/createBatchVisaJK",
					data:{ 
					    "travellerIds":travellerIds,
					    "visaIds":visaIds,
						"passportOperator":persons,
						"passportOperateTime":dates,
						"moneys":moneys,
						"passportOperateRemark":others},                
				    	"async": false,                 
					   success: function(msg){
						 if(msg.msg!=null&&msg.msg!=""){
							 top.$.jBox.tip(msg.msg,'warning');
							 return false;
						 }else{
							top.$.jBox.tip("操作成功",'warning');
							
							var travellArray = travellerIds.split(",");
							for(var i =0;i<travellArray.length;i++){
								 var travelerId = travellArray[i];
								 if(travelerId!=null&&travelerId!=""){
									$("#hidden_passportStatus_"+travelerId).val(1);
									 $("#passportStatus_"+travelerId+" option").eq(0).attr("selected",true);
								 }
							}
							
							window.location.reload();
						 }
			        }   
				});
			return true;
		}
		}});
		
	}
	
	
}


function clearNoNum(obj){
	obj.value = obj.value.replace(/[^-?\d.\d\d+$]/g,""); //清除"数字""-"和"."以外的字符
	obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字而不是
	obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
	obj.value = obj.value.replace(/\-{2,}/g,"-"); //只保留第一个.- 清除多余的
	obj.value = obj.value.replace("-","$#$").replace(/\-/g,"").replace("$#$","-");
	obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
	if (obj.value.length > 1) {
		obj.value = obj.value.replace(/\-$/g,"");
	}
}


function getPayTravelerInfo(travlerIds) {
	var result = false;
	var html = '<div class="" id="certificate_of_receipts">' +
		'<p id="certificate_of_receipts_p">您需要收款的最终金额为： <span class="payeeAmouont">' +
		'TM' +
		'</span> </p><table class="table activitylist_bodyer_table paymentTable" style="margin:0 auto;">' +
		'<thead><tr>' +
		'<th class="tc" width="10%">序号</th><th class="tc" width="20%">姓名</th>' +
		'<th class="tc" width="30%">应收金额</th><th class="tc" width="30%">收款金额</th><th class="tc" width="10%">操作</th>' +
		'</tr></thead><tbody>' +
		'TLI' +
		'</tbody></table><p id="certificate_of_receipts_p">您总计收款的金额为：<span class="paymentAmount">' +
		'FM' +
		'</span></p></div>';
	$.ajax({
		type:"POST",                 
		url:g_context_url+ "/visa/order/payTravelerInfo",                 
		data:{
			travlerIds:travlerIds
		},
		async: false,
	 	success: function(data){
				var totleMoney = data.totleMoney;
				var innerHtml = "";
				var pri =  data.prices.split(',');
				var cur =  data.currencyIds.split(',');
				var priceIndex = 0;
				var priceIndex1 = 0;
				for(var i = 1; i <= data.travelerList.length; i++) {
					var traveler = data.travelerList[i-1];
					
					var payPrice = traveler.payPriceSerialNum;
					var currency = payPrice.match(/[^\d,.\+,\s]+/g);
					for(var c = 0; c < currency.length; c++) {
						currency[c] = currency[c].replace('-','');
					}
					var price = payPrice.replace(currency, '');
					
					innerHtml += '<tr><td class="tc">' + i + '</td>' +
						'<td class="tc">' + traveler.name + '</td>' 
						+'<td class="tc">';
						for(var j = 0; j <= currency.length-1; j++){
							innerHtml +='<p data-currency="'+currency[j] +'" data-money="'+pri[priceIndex1]+'">'+currency[j]+' '+pri[priceIndex1]+'</p>' ;
							priceIndex1 = priceIndex1+1;
						}
						innerHtml +='</td>';
					
						
						innerHtml += '<td class="tl">';
					    //如果是环球行，则不可更改付款金额
 						if(68 == data.companyId) {
 							for(var j = 0; j <= currency.length-1; j++){
 								var currencyOne = currency[j];
 								innerHtml +=' <p class="tc"><label for="textfield4">' + currencyOne + '</label>' + pri[priceIndex] + '<input name="textfield3" type="text" class="w50_30 clear_input_padding_margin" data-currency="' + currencyOne + '" value="' + pri[priceIndex] + '" currencyId="'+ cur[priceIndex]+'" orderId="' + traveler.orderId + '" travelerId="'+ traveler.id +'" style="display: none;" /></p>';
 								priceIndex=priceIndex+1;
 							}
 						}else{
 							for(var j = 0; j <= currency.length-1; j++){
 								var currencyOne = currency[j];
 								innerHtml +=' <p class="tc" style="vertical-align:middle; margin-bottom:0px;"> <label for="textfield4">' + currencyOne + '</label>';
 								innerHtml +='<input name="textfield3" type="text" class="w50_30 clear_input_padding_margin" data-currency="' + currencyOne + '" value="' + pri[priceIndex] + '" currencyId="'+ cur[priceIndex]+'" orderId="' + traveler.orderId + '" travelerId="'+ traveler.id +'" /> </p>';
 								priceIndex=priceIndex+1;
 							}
 						}
						innerHtml +='</td>';
						innerHtml += '<td class="tc"><a class="delLink">删除</a></td></tr>';
				}
				html = html.replace('TM',totleMoney).replace('TLI',innerHtml).replace('FM',totleMoney);
 	  	  }
	});
	//弹窗
	$.jBox(html, {
		title: "批量收款", buttons: { '取消': 1, '确定': 2 }, submit: function (v, h, f) {
            if (v == "2") {
            	var travelerList=[];
            	$("#certificate_of_receipts").find("tbody tr").each(function(){
            		var $tr= $(this);
            		var travelerid = $tr.find('[travelerid]:first').attr('travelerid');
            		var orderId = $tr.find('[travelerid]:first').attr('orderId');
            		var currencyAmounts=[];
            		$tr.find('[travelerid]').each(function(){
            			var $input = $(this);
            			var currencyid = $input.attr('currencyid');
            			var price=$input.val();
            			currencyAmounts.push({
            				currencyid:currencyid,
            				price:price
            			});
								
            		});
            		travelerList.push({
            			orderId:orderId,
            			travelerid:travelerid,
            			currencyAmounts:currencyAmounts
            		});
            	});
            	if(travelerList.length) {          		
	    			window.location.href="${ctx}/visa/order/batchPay?parString="+JSON.stringify(travelerList); 
            	}else{
            		top.$.jBox.tip("请选择游客",'warning');
            		return true;
            	}
            	
            }else if(v == "1"){
                return true;
            }

        }, height: 634, width: 800

    });
	
}

//删除单个游客
function deleteOneTraveler(travelerId){
	var tid = travelerId+",";
	checkTravelers(tid);
}

//批量删除游客
function batchDeleteTraveler(orderId){
	var travelerIds ="";
	if(orderId != null){//订单列表批量删除只选取订单下游客
		$("input[type=checkbox][class=tdCheckBox]").each(function(){
			if($(this).attr("checked") && $(this).attr("orderId")==orderId){
				var travelerId = $(this).attr("travelerid");
				travelerIds += travelerId+",";
			}
		});
	}else{//游客列表
		$("input[type=checkbox][class=tdCheckBox2]").each(function(){
			if($(this).attr("checked")){
				var travelerId = $(this).attr("trallerId");
				travelerIds += travelerId+",";
			}
		});
	}
	
	if(travelerIds==""){
		top.$.jBox.tip('请选择游客！');
		return;
	}
	checkTravelers(travelerIds);//检查游客是否满足删除条件
}
//检查游客是否有审核流程 
function checkTravelers(travelerIds){
	$.ajax({
		cache : true,
		type : "POST",
		url : g_context_url+ "/visa/order/check4DeleteTravelers",
		data:{ 
		    "travelerIds":travelerIds
			}, 
		async: false,
		success: function(msg){
			if(msg.msg!=null && msg.msg!=""){
				 top.$.jBox.tip(msg.msg,'warning');
			 }else{
				if(msg.errMsg==""){
					cancelReviewAndDelete(travelerIds);
				}else{
					var html = '<div style="margin-top:20px;padding:0 10px;overflow:hidden;" id="borrowWin">';
					html += '<p style="font-size:18px;">'+msg.errMsg+'</p></div>';
					$.jBox(html, { title: "所选游客不满足删除条件",buttons:{'取消':0}, submit:function(v, h, f){
						return true;
					},height:350,width:500});
				}
				 
			 }
		}
	});
}


function applyInvoice(orderNum,orderId,orderType){
    $.ajax({
    	type : "post",
		url : "${ctx}/orderInvoice/manage/validateOrder",
		data:{
				orderNum : orderNum,
				orderId: orderId,
				orderType:orderType
		},
		success : function(msg) 
		{
			if("success"== msg.msg)
			window.location.href ="${ctx}/orderInvoice/manage/applyInvoice?orderNum=" +orderNum+"&orderId="+orderId+"&orderType="+orderType;  
			//${orderNum}&orderId=${orderId}&orderType=${orderType}";
			else if("canOrDel"==msg.msg)
				top.$.jBox.tip('已取消或已删除订单不能开发票！','success');
			else
				top.$.jBox.tip('申请失败，订单创建人和申请人不一致！','success'); 
		}
    });
	
}


function applyReceipt(orderNum,orderId,orderType){
    $.ajax({
    	type : "post",
		url : "${ctx}/orderInvoice/manage/validateOrder",
		data:{
				orderNum : orderNum,
				orderId: orderId,
				orderType:orderType
		},
		success : function(msg) 
		{
			if("success"== msg.msg)
				window.location.href ="${ctx}/orderReceipt/manage/applyReceipt?orderNum=" +orderNum+"&orderId="+orderId+"&orderType="+orderType; 
			else if("canOrDel"==msg.msg)
				top.$.jBox.tip('已取消或已删除订单不能开收据！','success');
			else
				top.$.jBox.tip('申请失败，订单创建人和申请人不一致！','success'); 
		}
    });
	
}


//取消正在审核中的流程并删除游客
function cancelReviewAndDelete(travelerIds){
	$.ajax({
		cache : true,
		type : "POST",
		url : g_context_url+ "/visa/order/cancelReviewAndDelete",
		data:{ 
		    "travelerIds":travelerIds
			}, 
		async: false,
		success: function(msg){
			if(msg.msg!=null&&msg.msg!=""){
				 top.$.jBox.tip(msg.msg,'warning');
			 }else{
				 var tid = msg.tid;
				 if(tid ==""){//无审核中的流程，直接删除游客
					 var html = '<div style="margin-top:20px;padding:0 10px" >';
					 html += '<p class="tc" style="font-size:16px;">'+msg.waringMsg+'</p></div>';
					 $.jBox(html, { title: "删除",buttons:{'确定': 1,'取消':0}, submit:function(v, h, f){
						 if (v!="0"){
							 $.ajax({
									cache: false,
									type: "POST",
									url:g_context_url + "/visa/order/doDelete",
									data:{
										"travelerIds":travelerIds,
										"tids":tid
										},
									async: false,
									success: function(msg){
										if(msg.msg!=null&&msg.msg!=""){
											top.$.jBox.tip(msg.msg,'warning');
										}else{
											$("#searchForm").submit();
										}
									}
								});
								return true;							 
						 }
					 },height:160,width:420});	 
				 }else{//将审核中的流程取消后删除游客
					 var html = '<div style="margin-top:20px;padding:0 10px;overflow:scroll;height:500px;" id="borrowWin">';
					 html += '<p style="font-size:18px;">'+msg.waringMsg+'</p></div>';
					 $.jBox(html, { title: "删除",buttons:{'确定': 1,'取消':0}, submit:function(v, h, f){
						 if (v!="0"){
							 $.ajax({
									cache: false,
									type: "POST",
									url:g_context_url + "/visa/order/doDelete",
									data:{
										"travelerIds":travelerIds,
										"tids":tid
										},
									async: false,
									success: function(msg){
										if(msg.msg!=null&&msg.msg!=""){
											top.$.jBox.tip(msg.msg,'warning');
										}else{
											$("#searchForm").submit();
										}
									}
								});
								return true;
						 }
					 },height:350,width:500});
				 }
				 
			 }
		}
	});
}

function changeCount(obj,orderCode)
{
	// 如果不是日信观光用户，则不执行此方法
	var sysCompanyUuid = $("#sysCompanyUuid").val();
	if (sysCompanyUuid != '58a27feeab3944378b266aff05b627d2') {
		return false;
	}
	if($("#childMenu_580").find("span").text() !=0 && $(obj).find(".new-tips").css("display") =="inline-block")
		{
			if(($("#childMenu_580").find("span").text() -1)==0)
				$("#childMenu_580").find("em").remove();
			else
			$("#childMenu_580").find("span").text($("#childMenu_580").find("span").text() -1);
			$(obj).find(".new-tips").hide();
			
			$.ajax({
		        type: "POST",
		        url: "${ctx}/visa/preorder/updateCount",
		        data: {
		        	'orderCode':orderCode
		        }
	     	}); 
		}
}

function expandPaidRecord(child,orderId,obj){
	if($(child).is(":hidden")){
		$(obj).parents('tr').addClass('tr-hover');
		$(child).show();
		$(obj).parents("td").addClass("td-extend");
		$.ajax({
			cache:true,
			type: "POST",
			url: "${ctx}/visa/order/paidRecords",
			data:{
				orderId:orderId
			},
			async:true,
			success: function(list){
				if(list.length == 0){
					$(obj).parents('tr').removeClass('tr-hover');
					$(child).hide();
					$(obj).parents("td").removeClass("td-extend");
					$.jBox.tip("没有收款信息!");
					return;
				}
				var html="";
				for(var i = 0,len = list.length; i<len; i++){
					html += '<tr><td class="tc">'+list[i][10]+'</td><td class="tc">';
					if(list[i][0]==1){
						html += '支票支付';
					}else if(list[i][0]==2){
						html += 'POS支付';
					}else if(list[i][0]==3){
						html += '现金支付';
					}else if(list[i][0]==4){
						html += '汇款支付';
					}else if(list[i][0]==5){
						html += '快速支付';
					}else if(list[i][0]==6){
						html += '银行转账';
					}else if(list[i][0]==7){
						html +='汇票'
					}else if(list[i][0]==8){
						html +='POS机刷卡'
					}else if(list[i][0]==9){
						html +='因公支付宝'
					}else{
						html += ' ';
					}
					html += '</td><td class="tc">'+list[i][2]+'</td>';
					html += '<td class="tc">'+list[i][3]+'</td><td class="tc">';
					if(list[i][5]==1){
						html += '收全款';
					}else if(list[i][5]==2){
						html += '收尾款';
					}else if(list[i][5]==3){
						html += '收订金';
					}else if(list[i][5]==7){
						html += '收押金';
					}else if(list[i][5]==16){
						html += '收押金';
					}else{
						html += ' ';
					}
					html += '</td><td class="tc"><div  class="pr xuanfudiv">';
					if(list[i][4]==1){
						html += '已达账';
					}else if(list[i][4]==0){
						html += '已撤销';
					}else if(list[i][4]==2){
						html += '已驳回';
					}else{
						html += '未达账';
					}
					if(null!=list[i][9] && ""!=list[i][9]){
						html += '<div class="ycq xuanfu" style="width: 24px;">备注</div>';
						html += '<div class="hover-title team_top hide" id="hoverWindow">'+list[i][9]+'</div>';
					}

					html += '</td><td class="tc"><a  class="showpayVoucher" onclick="downloads(\''+list[i][8]+'\',\'收款凭证\')" >收款凭证</a></td>';
					html += '<td class="tc">'+'<a href=\"javascript:void(0)\" onClick=\"changepayVoucher('+list[i][6]+','+list[i][7]+')\">改收款凭证</a>'+'</td></tr>';
				}
				$("#recordInfo_"+orderId).empty();
				$("#recordInfo_"+orderId).append(html);

			}
		});
	}else{
		if(!$(child).is(":hidden")){
			$(obj).parents('tr').removeClass('tr-hover');
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
		}
	}


}
/**
 * 修改支付凭证
 *
 * param payId
 * param orderId
 */
function changepayVoucher(payId, orderId){
	window.open ("${ctx}/visa/order/modifypayVoucher/" + payId + "/" + orderId);
}
function downloads(docIds,fileName){

	var doc = docIds.replace(/[ ]/g,"");
	if(doc==null || doc==""){
		$.jBox.tip("文件不存在");
		return;
	}
	var fileUrl =encodeURI("${ctx}/sys/docinfo/fileExists/" + doc + "/"+fileName);

	$.ajax({ url:encodeURI(fileUrl) ,async:false, success: function(result){

		if("文件存在"==result){
			window.open(encodeURI(encodeURI("${ctx}/sys/docinfo/zipdownload/" + doc + "/"+fileName)));
		} else{
			$.jBox.tip("文件不存在");
		}
	}});

}

	var $node;
	function relationInvoiceList(orderId) {
		var $pop = $.jBox($("#relationInvoice").html(), {
	    	title    : "发票列表", buttons: {'提交':1 ,'关闭':0}, submit: function (v, h, f) {
	        	if (v == "0") {
	        	}else if(v == "1"){
	        		var checks = $("input[name='invoiceName']:checked");
	        		var invoiceIds = "";
	        		checks.each(function(i,element){
	        			invoiceIds += $(element).val();
	        			invoiceIds += ",";
	        		})
	        		invoiceIds = invoiceIds.slice(0,-1);
	        		
	        		if(invoiceIds == ""){
        			$.jBox.tip("请先选择要关联的发票!");
        				return false;
        			}
	        		$.ajax({
	        			type: "POST",
		        		url: sysCtx + "/orderPay/changeInvoiceReceivedPayStatus",
		        		data: {
		        			invoiceIds : invoiceIds
		        		},
	        			success: function(result){
	        				if(result.flag == 'success'){
	        					$.jBox.tip("发票关联成功！"); 
	        				}
	        			}
        			});
	        	}
			},loaded:function(h, f){
				$node = h.find("#relationInvoiceTable");
				searchRelationInvoice(orderId);
			}, height: 350, width: 700}
		);
	}
	
	// ajax进入后台查询，返回json再解析
	function searchRelationInvoice(orderId){
		$.ajax({
			type: "POST",
	        url: sysCtx + "/orderPay/relationInvoiceList",
	        data: {
	        	orderId : orderId
	        },
			success: function(result){
				var html = "";
				if(result == undefined || result == null){
					top.$.jBox.tip("获取关联发票记录失败！");
				}
				if(result == ''){
					$node.empty().append(html);
				}
				var json = eval(result);
				// json数组个数
				var jsonLength = json.length;
				// 判断为空
				if (jsonLength && jsonLength != 0) {
					// 循环获取html组合
					for (var i = 0; i < jsonLength; i++) {
						// 序列值
						var indexVal = i + 1;
						html += "<tr>";
						// 选择
						html += "<td><input name='invoiceName' type='checkbox' value='" + json[i][0] + "'></td>";
						// 发票号
						html += "<td name='operatorName' class='tc'><span>" + json[i][1] + "</span></td>";
						// 团号
						html += "<td name='operation' class='tc'><span>" + json[i][2] + "</span></td>";
						// 申请人
						html += "<td name='operationTime' class='tc'><span>" + json[i][5] + "</span></td>";
						// 开票状态
						html += "<td name='mainContext' class='tc'><span>" + json[i][3] + "</span></td>";
						// 开票金额
						html += "<td name='mainContext' class='tr'><span>¥ " + json[i][4] + "</span></td>";
						html += "</tr>";
					}
					$node.empty().append(html);
				}
			}
		});
	}
</script>
<style>
    #certificate_of_receipts_p {
        margin: 10px !important;
        width: 800px;
        text-align: center !important;
    }
    
    .w50_30 {
        width: 80px !important;
        height: 30px;
        line-height: 30px;
        text-align: right;
    }
</style>
</head>
<% 
 String mainOrderId = request.getParameter("mainOrderId");
%>
<body>
<!-- 顶部菜单列表 -->
<page:applyDecorator name="visaXS_order_head">
	<page:param name="showType">${showType}</page:param>
</page:applyDecorator>
<input type="hidden" id="ctx" value="${ctx}">
<input id="tm" type="hidden" value="">
            	<!--右侧内容部分开始-->
				<div style="float:left;width: 100%" class="activitylist_bodyer_right_team_co_bgcolor">
					<form method="post" action="${ctx}/visa/order/searchxs" id="searchForm" onsubmit="cleanAgent()" > 
					
					<input id="mainOrderId" name="mainOrderIdYouKe" type="hidden" value="<%=mainOrderId %>" />
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
					
					<input id="youkePageNo" name="youkePageNo" type="hidden" value="${travelPage.pageNo}"/>
					<input id="youkePageSize" name="youkePageSize" type="hidden" value="${travelPage.pageSize}"/>
					<input id="youkeOrderBy" name="youkeOrderBy" type="hidden" value="${travelPage.orderBy}"/>
					
					<input value="" type="hidden" id="showList" name="showList"/>     
					<input value="" type="hidden" id="showFlag" name="showFlag"/>
					<input type="hidden" id="showType" name="showType" value="${showType }" />
					<shiro:hasPermission name="visaOrderXS:list:cancelorder">
						<c:set var="cancelOrderPermission" value="1"></c:set>
					</shiro:hasPermission>
					<input type="hidden" id="cancelOrderPermission" name="cancelOrderPermission" value="${cancelOrderPermission }" />


						<div class="activitylist_bodyer_right_team_co">
							<div class="activitylist_bodyer_right_team_co2">
								<input type="text" value="${visaOrderForm.orderNo}" class="inputTxt searchInput inputTxtlong"
									   name="orderNo" id="orderNo" placeholder="请输入订单号">
							</div>
							<a class="zksx">筛选</a>
							<div class="form_submit">
								 <input type="submit" id="btn_search" value="搜索" onclick="searchOrderList();"  class="btn btn-primary ydbz_x">
								 <input type="button" value="清空所有条件" onclick="resetSearchParams()" class="btn ydbz_x">
							</div>
							<shiro:hasPermission name="visaOrderForSale:operation:borrow">
								<p class="main-right-topbutt"><a class="primary" href="${ctx }/visa/order/visaBatchEditListNew?details=1" target="_blank" id="piliangcaozuo">批量操作</a></p>
							</shiro:hasPermission>
							<div class="ydxbd" style="display: none;">
								<span></span>
								<c:if test="${showList eq 'dingdan' || showList eq null }">
									<div class="activitylist_bodyer_right_team_co1">
										<div class="activitylist_team_co3_text">销售：</div>
										<select name="salerId" id="salerId" >
											<option value="">不限</option>
											<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
												<!-- 用户类型  1 代表销售 -->
												<option value="${userinfo.id }" <c:if test="${userinfo.id eq visaOrderForm.salerId}">selected="selected"</c:if>>${userinfo.name }</option>
											</c:forEach>
										</select>
									</div>
									<div class="activitylist_bodyer_right_team_co1">
										<div class="activitylist_team_co3_text">发票状态：</div>
										<div class="selectStyle">
											<select id="invoiceStatus" name="invoiceStatus">
												<option value="">不限</option>
												<option value="1" <c:if test="${visaOrderForm.invoiceStatus eq '1'}">selected="selected"</c:if>>已开发票</option>
												<option value="2" <c:if test="${visaOrderForm.invoiceStatus eq '2'}">selected="selected"</c:if>>未开发票</option>
											</select>
										</div>
									</div>
									<div class="activitylist_bodyer_right_team_co1">
										<div class="activitylist_team_co3_text">收据状态：</div>
										<div class="selectStyle">
											<select id="receiptStatus" name="receiptStatus">
												<option value="">不限</option>
												<option value="1" <c:if test="${visaOrderForm.receiptStatus eq '1'}">selected="selected"</c:if>>已开收据</option>
												<option value="2" <c:if test="${visaOrderForm.receiptStatus eq '2'}">selected="selected"</c:if>>未开收据</option>
											</select>
										</div>
									</div>
									<!-- 0067新增说明会时间 -->
									<div class="activitylist_bodyer_right_team_co1">
										<label class="activitylist_team_co3_text" >说明会时间：</label>
										<input readonly="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${visaOrderForm.explanationMeetingStart}" name="explanationMeetingStart" class="inputTxt dateinput" id="explanationMeetingStart"> 
										<span> 至 </span>  
										<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${visaOrderForm.explanationMeetingEnd}" name="explanationMeetingEnd" class="inputTxt dateinput" id="explanationMeetingEnd">
									</div>
								</c:if>
								<c:if test="${!(showList eq 'dingdan' || showList eq null )}">
									<!-- 0067新增说明会时间 -->
									<div class="activitylist_bodyer_right_team_co1">
										<label class="activitylist_team_co3_text">说明会时间：</label>
										<input readonly="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${visaOrderForm.explanationMeetingStart}" name="explanationMeetingStart" class="inputTxt dateinput" id="explanationMeetingStart"> 
										<span> 至 </span>  
										<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${visaOrderForm.explanationMeetingEnd}" name="explanationMeetingEnd" class="inputTxt dateinput" id="explanationMeetingEnd">
									</div>
								</c:if>
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">签证国家：</div>
									<select id="sysCountryId" name="sysCountryId">
										<option value="">不限</option>
										<c:forEach items="${countryList}" var="country">
											<c:choose>
												<c:when test="${ country.id eq visaOrderForm.sysCountryId}">
													<option value="${country.id}" selected="selected">${country.countryName_cn}</option>
												</c:when>
												<c:otherwise>
													<option value="${country.id}">${country.countryName_cn}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select> 
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">签证类型：</div>
									<div class="selectStyle">
										<select id="visaType" name="visaType">
											<option value="">不限</option>
											<c:forEach items="${visaTypeList}" var="visaType">
												<c:choose>
													<c:when test="${ visaType.value eq visaOrderForm.visaType}">
														<option value="${visaType.value}" selected="selected">${visaType.label}</option>
													</c:when>
													<c:otherwise>
														<option value="${visaType.value}">${visaType.label}</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">签证状态：</div>
									<div class="selectStyle">
									<select id="visaStatus" name="visaStatus">
											<option value="">不限</option>
											<c:forEach items="${visaStatusList}" var="visaStatus">
												
													<c:choose>
													<c:when test="${visaStatus.value == 2}">
															<c:choose>
																<c:when test="${visaStatus.value eq visaOrderForm.visaStatus}">
																	<option selected="selected" value="2">已约签</option>
																</c:when>
																<c:otherwise>
																	<option value="2">已约签</option>
																</c:otherwise>
															</c:choose>
														</c:when>
														<c:when test="${visaStatus.value == 3}">
															<c:choose>
																<c:when test="${visaStatus.value eq visaOrderForm.visaStatus}">
																	<option selected="selected" value="3">通过</option>
																</c:when>
																<c:otherwise>
																	<option value="3">通过</option>
																</c:otherwise>
															</c:choose>
														</c:when>
														<c:otherwise>
															<c:choose>
																<c:when test="${visaStatus.value eq visaOrderForm.visaStatus}">
																	<option selected="selected" value="${visaStatus.value}">${visaStatus.label}</option>
																</c:when>
																<c:otherwise>
																	<option value="${visaStatus.value}">${visaStatus.label}</option>
																</c:otherwise>
															</c:choose>
														</c:otherwise>
													</c:choose>
												
										</c:forEach>
										</select>
									</div>
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">预计出团日期：</label>
									<input readonly="" onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('forecastStartOutEnd').value==''){$dp.$('forecastStartOutEnd').value=vvv;}}})" value="${visaOrderForm.forecastStartOutStart}" name="forecastStartOutStart" class="inputTxt dateinput" id="forecastStartOutStart"> 
									<span> 至 </span>  
									<input readonly="" onclick="WdatePicker()" value="${visaOrderForm.forecastStartOutEnd }" name="forecastStartOutEnd" class="inputTxt dateinput" id="forecastStartOutEnd">
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">产品编号：</div>
									<input type="text" value="${visaOrderForm.visaProductId }" class="inputTxt" name="visaProductId" id="visaProductId">
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">订单状态：</div>
									<div class="selectStyle">
										<select id="orderPayStatus" name="orderPayStatus">
											<option value="">全部订单状态</option>
											<c:choose>
												<c:when test="${'1' eq visaOrderForm.orderPayStatus}">
													<option value="1" selected="selected">未收款</option>
												</c:when>
												<c:otherwise>
													<option value="1" >未收款</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '5' eq visaOrderForm.orderPayStatus}">
													<option value="5" selected="selected">已收款</option>
												</c:when>
												<c:otherwise>
													<option value="5">已收款</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '3' eq visaOrderForm.orderPayStatus}">
													<option value="3" selected="selected">预定</option>
												</c:when>
												<c:otherwise>
													<option value="3">预定</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '99' eq visaOrderForm.orderPayStatus}">
													<option value="99" selected="selected">已取消</option>
												</c:when>
												<c:otherwise>
													<option value="99">已取消</option>
												</c:otherwise>
											</c:choose>
										</select>
									</div>
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">渠道选择：</label>
									<!-- 315需求 针对越柬行踪 将非签约渠道改为直客 -->
									<select id="agentinfoId" name="agentinfoId">
										<option value="">全部</option>
										<c:choose>
											<c:when test="${ visaOrderForm.agentinfoId eq '-1' }">
												<c:choose><c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' }"><option value="-1" selected="selected">未签</option></c:when><c:otherwise>
												<c:if test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' }">
													<option value="-1" selected="selected">直客</option>
												</c:if>
												<c:if test="${fns:getUser().company.uuid ne  '7a81b21a77a811e5bc1e000c29cf2586' }">
													<option value="-1" selected="selected">非签约渠道</option>
												</c:if>
												</c:otherwise></c:choose>
											</c:when>
											<c:otherwise>
												<c:choose><c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' }"><option value="-1">未签</option></c:when><c:otherwise>
												<c:if test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' }">
													<option value="-1">直客</option>
												</c:if>
												<c:if test="${fns:getUser().company.uuid ne  '7a81b21a77a811e5bc1e000c29cf2586' }">
													<option value="-1">非签约渠道</option>
												</c:if>
												</c:otherwise></c:choose>
											</c:otherwise>
										</c:choose>
										<c:forEach items="${agentinfoList}" var="agentinfo">
											<c:choose>
												<c:when test="${ agentinfo.id eq visaOrderForm.agentinfoId}">
													<option value="${agentinfo.id}" selected="selected">${agentinfo.agentName}</option>
												</c:when>
												<c:otherwise>
													<option value="${agentinfo.id}">${agentinfo.agentName}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select> 
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">预计约签日期：</label>
									<input onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('forecastContractEnd').value==''){$dp.$('forecastContractEnd').value=vvv;}}})" value="${visaOrderForm.forecastContractStart}" name="forecastContractStart" class="inputTxt dateinput" id="forecastContractStart"> 
									<span> 至 </span>  
									<input  onclick="WdatePicker()" value="${visaOrderForm.forecastContractEnd}" name="forecastContractEnd" class="inputTxt dateinput" id="forecastContractEnd">
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">下单人：</div>
								<select id="createBy" name="createBy" >
										<option value="">请选择</option>
										<c:forEach items="${createByList}" var="createBy">
											<c:choose>
												<c:when test="${createBy eq visaOrderForm.createBy}">
													<option value="${createBy}" selected="selected">${createBy}</option>
												</c:when>
												<c:otherwise>
													<option value="${createBy}">${createBy}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select> 
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">参团类型：</div>
									<div class="selectStyle">
										<select id="orderType" name="orderType">
											<option value="">不限</option>
											<c:choose>
												<c:when test="${'1' eq visaOrderForm.orderType}">
													<option value="1" selected="selected">单团</option>
												</c:when>
												<c:otherwise>
													<option value="1" >单团</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '2' eq visaOrderForm.orderType}">
													<option value="2" selected="selected">散拼</option>
												</c:when>
												<c:otherwise>
													<option value="2">散拼</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '3' eq visaOrderForm.orderType}">
													<option value="3" selected="selected">游学</option>
												</c:when>
												<c:otherwise>
													<option value="3">游学</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '4' eq visaOrderForm.orderType}">
													<option value="4" selected="selected">大客户</option>
												</c:when>
												<c:otherwise>
													<option value="4" >大客户</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '5' eq visaOrderForm.orderType}">
													<option value="5" selected="selected">自由行</option>
												</c:when>
												<c:otherwise>
													<option value="5">自由行</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '6' eq visaOrderForm.orderType}">
													<option value="6" selected="selected">单办签</option>
												</c:when>
												<c:otherwise>
													<option value="6">单办签</option>
												</c:otherwise>
											</c:choose>
										</select>
									</div>
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">游客：</div>
									<input type="text" value="${visaOrderForm.travelName}" class="inputTxt inputTxtlong" name="travelName" id="travelName">
									
								</div> 
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">下单日期：</label>
									<input readonly="" onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('createDateEnd').value==''){$dp.$('createDateEnd').value=vvv;}}})" value="${visaOrderForm.createDateStart }" name="createDateStart" class="inputTxt dateinput" id="createDateStart"> 
									<span> 至 </span>  
									<input readonly="" onclick="WdatePicker()" value="${visaOrderForm.createDateEnd }" name="createDateEnd" class="inputTxt dateinput" id="createDateEnd">
								</div>
								<div class="activitylist_bodyer_right_team_co1">
								 	<div class="activitylist_team_co3_text">借款状态：</div>
									<div class="selectStyle">
										<select id="jiekuanStatus" name="jiekuanStatus">
										<option value="">不限</option>
										<c:choose>
											<c:when test="${'1' eq visaOrderForm.jiekuanStatus}">
												<option value="1" selected="selected">审批中</option>
											</c:when>
											<c:otherwise>
												<option value="1" >审批中</option>
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${ '2' eq visaOrderForm.jiekuanStatus}">
												<option value="2" selected="selected">已借</option>
											</c:when>
											<c:otherwise>
												<option value="2">已借</option>
											</c:otherwise>
										</c:choose>	
										<c:choose>
											<c:when test="${ '3' eq visaOrderForm.jiekuanStatus}">
												<option value="3" selected="selected">未借</option>
											</c:when>
											<c:otherwise>
												<option value="3">未借</option>
											</c:otherwise>
										</c:choose>
									</select>
									</div>
								</div>
								<%-- <div class="activitylist_bodyer_right_team_co1">
										<div class="activitylist_team_co3_text" style="width: 85px;">渠道结算方式：</div>
										<select name="paymentType" id="paymentType" >
											<option value="">不限</option>
											
											<c:forEach items="${fns:findAllPaymentType()}" var="pType">
												<!-- 用户类型  1 代表销售 -->
												<option value="${pType[0]}" <c:if test="${pType[0] eq visaOrderForm.paymentType}">selected="selected"</c:if>>${pType[1] }</option>
											</c:forEach> 
											
										</select>
									</div> --%>
								<!-- 新增护照状态 -->
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">护照状态：</div>
									<div class="selectStyle">
										<select id="passportStatus" name="passportStatus">
										<option value="">请选择</option>
										<c:choose>
											<c:when test="${'1' eq visaOrderForm.passportStatus}">
												<option value="1" selected="selected">借出</option>
											</c:when>
											<c:otherwise>
												<option value="1" >借出</option>
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${ '2' eq visaOrderForm.passportStatus}">
												<option value="2" selected="selected">销售已领取</option>
											</c:when>
											<c:otherwise>
												<option value="2">销售已领取</option>
											</c:otherwise>
										</c:choose>	
										<c:choose>
											<c:when test="${ '4' eq visaOrderForm.passportStatus}">
												<option value="4" selected="selected">已还</option>
											</c:when>
											<c:otherwise>
												<option value="4" >已还</option>
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${ '5' eq visaOrderForm.passportStatus}">
												<option value="5" selected="selected">已取出</option>
											</c:when>
											<c:otherwise>
												<option value="5">已取出</option>
											</c:otherwise>
										</c:choose>	
										<c:choose>
											<c:when test="${ '6' eq visaOrderForm.passportStatus}">
												<option value="6" selected="selected">未取出</option>
											</c:when>
											<c:otherwise>
												<option value="6">未取出</option>
											</c:otherwise>
										</c:choose>
										<c:choose>
											<c:when test="${ '8' eq visaOrderForm.passportStatus}">
												<option value="8" selected="selected">计调领取</option>
											</c:when>
											<c:otherwise>
												<option value="8">计调领取</option>
											</c:otherwise>
										</c:choose>
									</select>
									</div>
								</div>
								<%-- <div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text" style="width: 85px;">渠道结算方式：</div>
											<select name="paymentType" id="paymentType" >
												<option value="">不限</option>
												
												<c:forEach items="${fns:findAllPaymentType()}" var="pType">
													<!-- 用户类型  1 代表销售 -->
													<option value="${pType[0]}" <c:if test="${pType[0] eq visaOrderForm.paymentType}">selected="selected"</c:if>>${pType[1] }</option>
												</c:forEach> 
												
											</select>
								</div> --%><!-- 占位 -->
								
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">实际出团日期：</label>
									<input readonly="" onfocus="WdatePicker()" value="${visaOrderForm.startOutStart}" name="startOutStart" class="inputTxt dateinput" id="startOutStart"> 
									<span> 至 </span>  
									<input readonly="" onclick="WdatePicker()" value="${visaOrderForm.startOutEnd}" name="startOutEnd" class="inputTxt dateinput" id="startOutEnd">
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">实际约签日期：</label>
									<input readonly="" onfocus="WdatePicker()" value="${visaOrderForm.contractStart}" name="contractStart" class="inputTxt dateinput" id="contractStart"> 
									<span> 至 </span>  
									<input readonly="" onclick="WdatePicker()" value="${visaOrderForm.contractEnd}" name="contractEnd" class="inputTxt dateinput" id="contractEnd">
								</div>
							</div>
						</div>
					</form>

					<!-- 产品线路分区 -->
					<div class="supplierLine">
						<a onclick="showOrder();" href="javascript:void(0)" id="dingdanliebiao">订单列表</a>
						<a onclick="showTraveler();" href="javascript:void(0)" id="youkeliebiao">游客列表</a>
						<shiro:hasPermission name="visaOrderForSale:operation:borrow">
							<a href="${ctx }/visa/order/visaBatchEditRecordList?jumpFlag=1" target="_blank" id="piliangcaozuojilu">批量操作记录</a>
						</shiro:hasPermission>
					</div>
					<div id="dingdan">
						<div  class="activitylist_bodyer_right_team_co_paixu">
							<div class="activitylist_paixu">
								<div class="activitylist_paixu_left">
									<ul>

										
										 <c:choose>
	                    	<c:when test="${page.orderBy == 'vo.create_date DESC'}">
	                    		<li class="activitylist_paixu_moren">
	                    			<a onclick="sortby('vo.create_date',this)">
		                    			创建时间
		                    			<i class="icon icon-arrow-down"></i>
	                    			</a>
	                    		</li>
								<li class="activitylist_paixu_left_biankuang lipro.updateDate">
									<a onclick="sortby('vo.update_Date',this)">
										更新时间
									</a>
								</li>
	                    	</c:when>
	                    	
	                    	<c:when test="${page.orderBy == 'vo.create_date ASC'}">
	                    		<li class="activitylist_paixu_moren">
	                    			<a onclick="sortby('vo.create_date',this)">
		                    			创建时间
		                    			<i class="icon icon-arrow-up"></i>
	                    			</a>
	                    		</li>
								<li class="activitylist_paixu_left_biankuang lipro.updateDate">
									<a onclick="sortby('vo.update_Date',this)">
										更新时间
									</a>
								</li>
	                    	</c:when>
	                    	
	                    	<c:when test="${page.orderBy == 'vo.update_Date DESC'}">
	                    		<li class="activitylist_paixu_left_biankuang lipro.updateDate">
	                    			<a onclick="sortby('vo.create_date',this)">
		                    			创建时间
	                    			</a>
	                    		</li>
								<li class="activitylist_paixu_moren">
									<a onclick="sortby('vo.update_Date',this)">
										更新时间
		                    			<i class="icon icon-arrow-down"></i>
										
									</a>
								</li>
	                    	</c:when>
	                    	
	                    	<c:when test="${page.orderBy == 'vo.update_Date ASC'}">
	                    		<li class="activitylist_paixu_left_biankuang lipro.updateDate">
	                    			<a onclick="sortby('vo.create_date',this)">
		                    			创建时间
	                    			</a>
	                    		</li>
								<li class="activitylist_paixu_moren">
									<a onclick="sortby('vo.update_Date',this)">
										更新时间
		                    			<i class="icon icon-arrow-up"></i>
									</a>
								</li>
	                    	</c:when>
	                    	<c:otherwise>
	                    		<li class="activitylist_paixu_left_biankuang lipro.updateDate">
	                    			<a onclick="sortby('vo.create_date',this)">
		                    			创建时间
	                    			</a>
	                    		</li>
								<li class="activitylist_paixu_left_biankuang lipro.updateDate">
									<a onclick="sortby('vo.update_Date',this)">
										更新时间
									</a>
								</li>
	                    	</c:otherwise>
	                    </c:choose>
									</ul>
								</div>
							  <div class="activitylist_paixu_right">
							<c:choose>
							 	<c:when test="${page.count >0}">
							 	<c:if test="${companyUuid eq '58a27feeab3944378b266aff05b627d2' }">
							 	<button onclick="exportExcelUserList();" class="btn btn-primary" style="width:auto;">导出游客</button></c:if>
							 	查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
							 	</c:when>
							 	<c:otherwise>
							 	查询结果&nbsp;&nbsp;<strong>0</strong>&nbsp;条
							 	</c:otherwise>
							 </c:choose>
							</div>
							  <div class="kong"></div>
							</div>
						</div>
						<table class="table mainTable activitylist_bodyer_table" id="contentTable">
							<thead>
								<tr>
									<th width="4%">序号</th>
									<th width="6%">预订渠道</th>
									<!-- 200 针对优加，销售签证订单 订单列表加入"渠道联系人" -->
									<c:if test="${companyUuid eq '7a81c5d777a811e5bc1e000c29cf2586'}">
										<th width="8%">渠道联系人</th>
									</c:if>
									<c:choose>
										<c:when test="${dayangCompanyUuid==companyUuid}">
											<!-- 隐藏此列 -->
										</c:when>
										<c:otherwise>
											<th width="6%">订单编号</th>
										</c:otherwise>
									</c:choose>

									<th width="9%">产品名称</th>
									<!-- C460V3 所有批发商团号取产品团号 -->
									<!-- C460V5 所有批发商团号取产品团号  环球行显示订单二字 -->
									<c:if test="${fns:getUser().company.uuid eq '7a816f5077a811e5bc1e000c29cf2586'}">
									   <th width="6%">订单团号</th>
									</c:if>
									<c:if test="${fns:getUser().company.uuid ne '7a816f5077a811e5bc1e000c29cf2586'}">
									   <th width="6%">团号</th>
									</c:if>
									<th width="6%">签证类别</th>
									<th width="6%">签证国家</th>
									<!--  <th width="6%">领区联系人</th>-->
									<th width="6%">订单状态</th>
									<th width="6%"><span class="order-saler-title on">销售</span>/<span class="order-picker-title">下单人</span></th>
									<th width="9%">下单时间</th>
									<th width="4%">人数</th>
									<th width="6%">发票/收据</th>
									<th width="6%">
										<c:if test="${fns:getUser().company.uuid ne '7a45838277a811e5bc1e000c29cf2586'}">
											应收金额
										</c:if>
										<c:if test="${fns:getUser().company.uuid eq '7a45838277a811e5bc1e000c29cf2586'}">
											<span class="order-yingshou-title on">应收金额</span>/<span class="order-weishou-title">未收余额</span>
										</c:if>
									</th>
									<th width="6%">已收总金额<br>达账总金额</th>
									<th width="4%">操作</th>
									<th width="4%">下载</th>
									<th width="4%">财务</th>
								</tr>
							</thead>
							<tbody>
							<c:if test="${ empty resultFormList}">	
								<tr >
									<td colspan="16" >
										<center>暂无搜索结果</center>
									</td>
								</tr>
							</c:if>
							<c:if test="${!empty resultFormList}">
							<c:forEach items="${resultFormList}" var="result" varStatus="s">
								<tr id="visa" onclick="changeCount(this,'${result.orderCode}');">
									<td>					
										<span class="sqcq-fj">
											<input type="checkbox" class="tdCheckBox" value="${result.orderCode}@<c:if test="${not empty result.confirmationFileId}">${result.orderId}</c:if>" name="visaOrderId" onclick="visaOrderCheckchg()" />
											${(page.pageNo-1)*page.pageSize + s.count}
										</span>
									</td>
									<td>
									<c:choose>
									<c:when test="${!empty result.paymentType}">
										<div class="ycq yj" style="margin-top:1px;">
											${result.paymentType}
										</div>
									</c:when>
								</c:choose>
								<!-- 315需求 针对越柬行踪 将非签约渠道改为直客 -->
								<c:choose>
									<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and result.agentinfoName eq '非签约渠道' }">未签</c:when>
									<c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' and result.agentinfoName eq '非签约渠道' }">直客</c:when>
									<c:otherwise>${result.agentinfoName }</c:otherwise>
								</c:choose>
								</td>
									<!-- 200 针对优加，签务签证订单 订单列表加入"渠道联系人" -->
									<c:if test="${companyUuid eq '7a81c5d777a811e5bc1e000c29cf2586'}">
										<td>
											<span class="agentContactsNameSpan">${result.contactsName}</span>
											<input class="agentContactsName" value="${result.contactsName}" style="display: none"/>
										</td>
									</c:if>
									<c:choose>
										<c:when test="${dayangCompanyUuid==companyUuid}">
											<!-- 隐藏此列 -->
										</c:when>
										<c:otherwise>
											<td class="que_parent">${result.orderCode }<c:if test="${result.confirmFlag }"><i class="que"></i></c:if>
												<% if("1".equals(flag)) { %>
												<c:if test="${null != result.isRead}">
													<c:if test="${result.isRead eq '0' && companyUuid eq '58a27feeab3944378b266aff05b627d2'}">
														<span class="new-tips" style="display:true;"></span>
													</c:if>	
												</c:if>
												<% } %>
											</td>
										</c:otherwise>
									</c:choose>
									
									<td>${result.productName }</td>
									
									<%-- <td>${result.orderTuanhao }</td> --%>
									<!--  C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-s-订单->销售签证订单->列表 -->
                                    <c:if test="${fns:getUser().company.uuid eq '7a816f5077a811e5bc1e000c29cf2586'}"><!-- 环球行 -->
		  					        <td>${result.orderTuanhaoFromVOTable }</td>
							        </c:if>
							        <c:if test="${fns:getUser().company.uuid ne'7a816f5077a811e5bc1e000c29cf2586'}"><!-- 非环球行 -->
		  					        <td>${result.orderTuanhao }</td>
							        </c:if>
                                    <!--  C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-e -->
									
									<!-- 签证类别 -->
									<td>${fns:getDictLabel(result.visaType,'new_visa_type','')}</td>
									<!-- 签证国家 -->
									<td>${result.visaCountry }</td>
									<!-- 领区联系人 -->
									<!--  <td>${result.contactPerson }</td>-->
									<td>
										<c:choose>
											<c:when test="${'1' eq result.payStatus}">
												未收款
											</c:when>
											<c:when test="${ '3' eq result.payStatus}">
												预定
											</c:when>
											<c:when test="${ '5' eq result.payStatus}">
												已收款
											</c:when>
											<c:when test="${ '99' eq result.payStatus}">
												已取消
											</c:when>
										</c:choose>
									</td>
									
									<td><span class="order-saler onshow">${result.salerName }</span><span class="order-picker">${result.creatUser }</span></td>
									<td class="tc"><fmt:formatDate value="${result.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>${result.travelerCount }</td>
									<td class="tc">
										${result.invoiceStatus}
										<br/>
										${result.receiptStatus}
									</td>
									<td class="tr pr">
										<c:if test="${result.yujiRebates != '' || result.shijiRebates != '' }">
											<span class="icon-rebate">
												<span>
													<c:if test="${result.yujiRebates != '' }">
														   <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
													       <c:choose>
													      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
																	预计宣传费 ${result.yujiRebates }<br>
															</c:when>
													         <c:otherwise>
													         		预计返佣 ${result.yujiRebates }<br>
													          </c:otherwise>
		   											  </c:choose>   
													</c:if>
													<c:if test="${result.shijiRebates != '' }">
														 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
													       <c:choose>
													      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
																	实际宣传费 ${result.shijiRebates }
															</c:when>
													         <c:otherwise>
													         		实际返佣 ${result.shijiRebates }
													          </c:otherwise>
		   											  </c:choose>   
													</c:if>
												</span>
											</span>
										</c:if>
										<c:if test="${fns:getUser().company.uuid ne '7a45838277a811e5bc1e000c29cf2586'}">
											<span class="tdorange fbold ">
												${fns:getMoneyAmountBySerialNum(result.visaPay,2)}
											</span>
										</c:if>
										<c:if test="${fns:getUser().company.uuid eq '7a45838277a811e5bc1e000c29cf2586'}">
											<span class="order-yingshou onshow">
												<span class="tdorange fbold ">
													${fns:getMoneyAmountBySerialNum(result.visaPay,2)}
												</span>
											</span>
											<span class="order-weishou">
												<span class="tdorange fbold ">
													<c:if test="${'1' ne result.isAsAccount }">
														<c:if test="${'1' eq result.payStatus || '3' eq result.payStatus}">
															${fns:getMoneyAmountBySerialNum(result.visaPay,2)}
														</c:if>
														<c:if test="${'5' eq result.payStatus}">
															${fns:getDecrease(result.visaPay,result.payedMoney)}
														</c:if>
													</c:if>
													<c:if test="${'1' eq result.isAsAccount }">
														${fns:getDecrease(result.visaPay,result.accountedMoney)}
													</c:if>
												</span>
											</span>
										</c:if>
									</td>
									<td class="p0 tr">
										<c:if test="${not empty result.promptStr}">
											<div class="notice_price"><span>${result.promptStr }</span></div>
										</c:if>
										<div class="yfje_dd">	
											<span class="fbold">
												<c:if test="${'1' eq result.visaOrderStatus }">
													${fns:getMoneyAmountBySerialNum(result.payedMoney,2)}
												</c:if>
											</span>
										</div>
										<div class="dzje_dd">
												<span class="fbold">
													<c:if test="${'0' eq result.isAsAccount || '1' eq result.isAsAccount || '2' eq result.isAsAccount }">
														${fns:getMoneyAmountBySerialNum(result.accountedMoney,2)}
													</c:if>
												</span>
										</div>
									</td>
									<td class="p0">
										<dl class="handle">
											<dt><img src="${ctxStatic}/images/handle_cz_rebuild.png" title="操作"></dt>
											<dd>
												<p>
													<span></span>
													<!-- 改列表只显示 签证订单的详情 不显示主订单的 -->
													<a target="_blank" href="${ctx}/visa/order/goUpdateVisaOrderForSales?visaOrderId=${result.orderId}&details=1">详情 </a>
													<!-- 产品锁定或订单锁定后下面操作被禁止 -->
													<c:if test="${result.lockStatus != 1 && result.activityLockStatus != 1}">
														<a href="${ctx}/visa/order/goUpdateVisaOrderForSales?visaOrderId=${result.orderId}"  target="_blank">修改</a>
														
														<c:if test="${companyUuid ne '7a81a26b77a811e5bc1e000c29cf2586' || (companyUuid eq '7a81a26b77a811e5bc1e000c29cf2586' &&  empty result.main_orderId)}">
														<a href="${ctx}/order/manager/visa/joinGroup/joinGroupForm?orderId=${result.orderId}">参团</a>
														</c:if>

														<!-- 未锁定状态或单办签时允许取消激活操作 -->
														<c:if test="${result.lockStatus != 1 }">
														   <shiro:hasPermission name="visaOrderForSale:operation:cancel">
																<c:if test="${result.payStatus==1||result.payStatus==2||result.payStatus==3}">
																	<a href="javascript:cancelOrder('${result.orderId}');">取消</a>
																</c:if>
															</shiro:hasPermission>
															<shiro:hasPermission name="visaOrderForSale:operation:invoke">
															<c:if test="${result.payStatus==99 || result.payStatus==2 || result.payStatus==3}">
																	<a href="javascript:void(0)" onClick="invokeOrder(${result.orderId})">激活</a>
																</c:if>
															</shiro:hasPermission>
														
														</c:if>
														<c:if test="${companyUuid ne '7a81a26b77a811e5bc1e000c29cf2586' || (companyUuid eq '7a81a26b77a811e5bc1e000c29cf2586' &&  empty result.main_orderId)}">
<%-- 	                                                    <a href="${ctx}/visaUpProces/list?orderId=${result.orderId}&productType=6&flowType=10"> 改价</a> --%>
	                                                    <a href="${ctx}/changeprice/visa/list?orderId=${result.orderId}&productType=6&flowType=10"> 改价</a>
	                                                    <c:if test="${'0' eq result.isAsAccount || '1' eq result.isAsAccount || '2' eq result.isAsAccount }">
<%-- 	                                                    	<a href="${ctx}/order/manager/visaRefund/refundList?proId=${result.orderId}">退款</a> --%>
	                                                    	<a href="${ctx}/newRefund/visa/refundList?proId=${result.orderId}">退款</a>
	                                                    </c:if>
<!-- 	                                                    <a href="${ctx}/visa/order/showRebatesList?orderId=${result.orderId}" target="_blank">返佣</a> update by zhanghao20151216 屏蔽老申请记录入口-->
												            <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
													       <c:choose>
													      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
																<a href="${ctx}/visa/rebate/visaRebateList?orderId=${result.orderId}" target="_blank">宣传费</a>
															</c:when>
													         <c:otherwise>
													         	<a href="${ctx}/visa/rebate/visaRebateList?orderId=${result.orderId}" target="_blank">返佣</a>
													          </c:otherwise>
		   											  </c:choose>   
	                                                    </c:if>
														<a href="javascript:applyInvoice('${result.orderCode}',${result.orderId},6)">申请发票</a>
														<a href="javascript:applyReceipt('${result.orderCode}',${result.orderId},6)">申请收据</a>
													</c:if>
													<a href="${ctx}/visa/order/orderTable?orderId=${result.orderId}" target="_blank">预约表</a>
													
													<!-- 上传确认单  -->
													<c:if test="${result.payStatus!=99 }"><!-- 已取消订单不能上传 -->
														<shiro:hasPermission name="visaOrderForSale:upload:confirmation">
															<a  href="javascript:void(0)"  onclick="uploadConfirmFiles('confirmFiles',this,'${result.orderId}','/visa/order/visaUploadConfirmation');">上传确认单</a>
														</shiro:hasPermission>
													</c:if>
													
													<a onclick="expand('#child_${result.orderId }','${result.orderId }',this,'${result.lockStatus }','${result.activityLockStatus }','${result.main_orderId }') " href="javascript:void(0)">展开客户</a>
											   </p>
											</dd>
										</dl>
									</td>
									<td class="p0">
										<dl class="handle">
											<dt><img src="${ctxStatic}/images/handle_xz_rebuild.png" title="下载"></dt>
											<dd>
												<p>
													<span></span>   
													 <a target="_blank" href="${ctx}/visa/order/downloadTraveler?visaOrderId=${result.orderId}&agentId=${result.agentId}&groupCode=${result.cantuanTuanhao}">游客资料</a>
													 
													 <%-- <a target="_blank" href="${ctx}/visa/order/interviewNotice?type=order&id=${result.orderId}">面签通知</a> --%>
													 <!-- 0214新增面签通知附件上传,下载上传文件-s -->
												     <a target="_blank" href="${ctx}/visa/order/interviewNoticeAll?type=order&id=${result.orderId}">面签通知</a>
												     <!-- 0214新增面签通知附件上传,下载上传文件-e -->
													<!---461需求判断环球行与非环球行批发商的护照领取单下载模板 -->
													<c:choose>
														<c:when test="${fns:getUser().company.uuid eq '7a816f5077a811e5bc1e000c29cf2586'}">
															<a target="_blank" href="${ctx}/order/download/download?fileName=hzlqd.rar">护照领取单</a>
														</c:when>
														<c:otherwise>
															<a target="_blank" href="${ctx}/order/download/download?fileName=hzlqd_other.rar">护照领取单</a>
														</c:otherwise>
													</c:choose>

													
													<shiro:hasPermission name="visaOrderForSale:download:confirmation"> 
														<c:if test="${not empty result.confirmationFileId}">
															<a href="javascript:void(0)"   onClick="downloadConfirm('${result.orderId}','/visa/order/downloadConfirmFiles')">确认单</a>
														</c:if>
													</shiro:hasPermission>
												</p>
											</dd>
										</dl>
									</td>
									<td class="p0">
										<dl class="handle">
											<dt><img src="${ctxStatic}/images/handle_fk_rebuild.png" title="财务"></dt>
											<dd>
												<p>
													<span></span>
													<a onclick="expandPaidRecord('#payRecord_${result.orderId}','${result.orderId}',this);"  href="javascript:void(0)">收款记录</a>
													<!-- 0444需求 -->
													<c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
														<c:if test="${1 eq result.applyInvoiceWay }">
														<a href="javascript:relationInvoiceList('${result.orderId}')" class="jtk">关联发票</a>
														</c:if>
													</c:if>
													<!-- 0444需求 -->
													<a onclick="" href="javascript:viewInvoiceInfo('${result.orderCode }','${result.orderId }','${result.productId}')">发票明细</a> <br>
													<a onclick="" href="javascript:viewReceiptInfo('${result.orderCode }','${result.orderId }','${result.productId}')">收据明细</a>
														<shiro:hasPermission name="visaOrderForSale:list:costpayl">
														<c:if test="${result.productId != null }">
															<a href="${ctx }/cost/manager/settleList/${result.productId}/6" target="_blank">结算单</a>
														</c:if></shiro:hasPermission>
													
											   </p>
											</dd>
											
										</dl>
									</td>
								</tr>

								<!-- 20160629增加0467需求，支付记录 -->
								<tr id="payRecord_${result.orderId}" name="subtr" style="display: none;" class="activity_team_top1">
									<td colspan="17" class="team_top" style="background-color:#dde7ef;">
										<table  class="table activitylist_bodyer_table" style="margin:0 auto;" >
											<thead>
											<tr>
												<th>游客姓名</th>
												<th>收款方式</th>
												<th>金额</th>
												<th>日期</th>
												<th>收款类型</th>
												<th>是否已确认达账</th>
												<th>收款凭证</th>
												<th colspan=2>操作</th>
											</tr>
											</thead>
											<tbody id="recordInfo_${result.orderId}">

											</tbody>
										</table>
									</td>
								</tr>
								
								<tr id="child_${result.orderId}" class="activity_team_top1" style="display:none">
									<td colspan="19" class="team_top">
										<table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;">
											<thead>
												<tr>
													<th width="5%" class="tc">姓名</th>
													<th width="9%" class="tc">护照号</th>
													<!--<th width="6%">AA码</th> -->
													<!-- <th width="4%">签证类别</th>  -->
													<!-- <th width="4%">签证国家</th> -->
													<th width="4%" class="tc">领区</th>
													<th width="6%" class="tc">参团订单编号</th>
													<th width="4%" class="tc">参团团号</th>
													<th width="4%" class="tc">团队类型</th>
													<th width="7%" class="tc">预计出团时间<br/>实际出团时间</th>
													<th width="7%" class="tc">预计约签时间
													<!-- 懿洋假期的美国签证隐藏 C482-->
													<c:if test="${!(fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' && result.visaCountry=='美国')}">
														<br/>实际约签时间
													</c:if>
													<!-- 懿洋假期的美国签证隐藏 C482-->
													</th>
													<th width="6%" class="tc">说明会时间</th>
													<th width="4%" class="tc">签证状态</th>
													<th width="4%" class="tc">护照状态</th>
													<th width="4%" class="tc">担保类型</th>
													<th width="6%" class="tr">应收押金</th>
													<th width="6%" class="tr">已收押金<br />达账押金</th>
													<th width="6%" class="tr">应收金额</th>
													 <!-- 0615需求,新增是否需要押金,是否需要上传资料限定为批发商大洋国际 --> 
								                    <%-- <c:if test="${fns:getUser().company.uuid eq \'7a81a03577a811e5bc1e000c29cf2586\'}">
													<th width="5%" class="tr">是否需要押金</th>
													<th width="5%" class="tr">是否上交资料</th>
													</c:if>--%>
													<th width="6%" class="tr">已收金额<br />达账金额</th>
													<th class="tc" width="4%" class="tc">借款状态</th>
													<th width="5%" class="tc">操作</th>	
													<th width="5%" class="tc">财务</th>					
												</tr>
											</thead>
											<tbody id="traveler_${result.orderId}">
										
											</tbody>
										</table>
									</td>
								</tr>
								</c:forEach>
								<tr class="checkalltd">
									<td colspan='19' class="t1">
										<label> <input type="checkbox" name="visa_orderAllChk" onclick="visa_orderAllChecked(this)" /> 全选 </label> 	
										<label> <input type="checkbox" name="visa_orderAllChkNo" onclick="visa_orderAllNoCheck()" /> 反选 </label>
										<shiro:hasPermission name="visaOrderForSale:download:confirmation">
											<input type="button" class="btn" value="批量下载确认单" onclick="visa_orderBatchDownload('visaOrderId')">
										</shiro:hasPermission>	
										<shiro:lacksPermission name="visaOrderForSale:download:confirmation">
											<input type="button" class="btn gray" value="批量下载确认单" disabled="disabled">
										</shiro:lacksPermission>												
									</td>
								</tr>
								</c:if>
							</tbody>
						</table>
				<div class="pagination clearFix">${page}</div>
					</div>


					<div id="youke">
						<div class="activitylist_bodyer_right_team_co_paixu">
							<div class="activitylist_paixu">
								<div class="activitylist_paixu_left">
									<ul>
										<c:choose>
										<c:when test="${travelPage.orderBy == '' || travelPage.orderBy == null }">
	                    		<li class="activitylist_paixu_left_biankuang lipro.updateDate">
	                    			<a onclick="youkesortby('ag.groupOpenDate',this)">
		                    			出团日期
	                    			</a>
	                    		</li>
								<li class="activitylist_paixu_left_biankuang lipro.updateDate">
									<a onclick="youkesortby('ag.groupCloseDate',this)">
										截团日期
									</a>
								</li>
	                    	</c:when>
					                    	<c:when test="${travelPage.orderBy == 'ag.groupOpenDate DESC'}">
					                    		<li class="activitylist_paixu_moren">
					                    			<a onclick="youkesortby('ag.groupOpenDate',this)">
						                    			出团日期
						                    			<i class="icon icon-arrow-down"></i>
					                    			</a>
					                    		</li>
												<li class="activitylist_paixu_left_biankuang lipro.updateDate">
													<a onclick="youkesortby('ag.groupCloseDate',this)">
														截团日期
													</a>
												</li>
					                    	</c:when>
					                    	
					                    	<c:when test="${travelPage.orderBy == 'ag.groupOpenDate ASC'}">
					                    		<li class="activitylist_paixu_moren">
					                    			<a onclick="youkesortby('ag.groupOpenDate',this)">
						                    			出团日期
						                    			<i class="icon icon-arrow-up"></i>
					                    			</a>
					                    		</li>
												<li class="activitylist_paixu_left_biankuang lipro.updateDate">
													<a onclick="youkesortby('ag.groupCloseDate',this)">
														截团日期
													</a>
												</li>
					                    	</c:when>
					                    	
					                    	<c:when test="${travelPage.orderBy == 'ag.groupCloseDate DESC'}">
					                    		<li class="activitylist_paixu_left_biankuang lipro.updateDate">
					                    			<a onclick="youkesortby('ag.groupOpenDate',this)">
						                    			出团日期
					                    			</a>
					                    		</li>
												<li class="activitylist_paixu_moren">
													<a onclick="youkesortby('ag.groupCloseDate',this)">
														截团日期
						                    			<i class="icon icon-arrow-down"></i>
														
													</a>
												</li>
					                    	</c:when>
					                    	
					                    	<c:when test="${travelPage.orderBy == 'ag.groupCloseDate ASC'}">
					                    		<li class="activitylist_paixu_left_biankuang lipro.updateDate">
					                    			<a onclick="youkesortby('ag.groupOpenDate',this)">
						                    			出团日期
					                    			</a>
					                    		</li>
												<li class="activitylist_paixu_moren">
													<a onclick="youkesortby('ag.groupCloseDate',this)">
														截团日期
						                    			<i class="icon icon-arrow-up"></i>
													</a>
												</li>
					                    	</c:when>
					                    	<c:otherwise>
					                    		<li class="activitylist_paixu_left_biankuang lipro.updateDate">
					                    			<a onclick="youkesortby('ag.groupOpenDate',this)">
						                    			出团日期
					                    			</a>
					                    		</li>
												<li class="activitylist_paixu_left_biankuang lipro.updateDate">
													<a onclick="youkesortby('ag.groupCloseDate',this)">
														截团日期
													</a>
												</li>
					                    	</c:otherwise>
					                    </c:choose>
									</ul>
								</div>
							 <div class="activitylist_paixu_right">
							 <c:choose>
							 	<c:when test="${travelPage.count >0}">
							 	查询结果&nbsp;&nbsp;<strong>${travelPage.count}</strong>&nbsp;条
							 	</c:when>
							 	<c:otherwise>
							 	查询结果&nbsp;&nbsp;<strong>0</strong>&nbsp;条
							 	</c:otherwise>
							 </c:choose>
							</div>
							  <div class="kong"></div>
							</div>
						</div>
						<table id="teamTable" class="table mainTable activitylist_bodyer_table" style="margin:0 auto;">
											<thead>
												<tr>
													<th width="5%">姓名</th>
													<th width="7%">护照号</th>
													<!-- <th width="7%">AA码</th> -->
													<th width="4%">签证类别</th>
													<th width="4%">签证国家</th>
													<th width="4%">领区</th>
													<th width="6%">参团订单编号</th>
													<th width="4%">参团团号</th>
													<th width="4%">团队类型</th>
													<th width="6%">预计出团时间<br/>实际出团时间</th>
													<th width="6%">预计约签时间<br/>实际约签时间</th>
													<th width="7%">说明会时间</th>
													<th width="4%">签证状态</th>
													<th width="4%">护照状态</th>
													<th width="5%">担保类型</th>
													<th width="4%">借款状态</th>
													 <c:if test="${fns:getUser().company.uuid eq \'7a81a03577a811e5bc1e000c29cf2586\'}">
													<th width="5%" >是否需要押金</th>
													<th width="5%">是否上交资料</th>
													</c:if>
													<th width="7%">应收押金</th>
													<th width="7%">已收押金<br />达账押金</th>
													<th width="4%">操作</th>	
													<th width="4%">下载</th>	
													<th width="4%">财务</th>					
												</tr>
											</thead>
											<tbody>
											<c:forEach items="${travelList}" var="traveler">
												<tr>
													<td class="p0">
													<c:choose>
																<c:when test="${!empty traveler.paymentType}">
																	<div class="ycq yj" style="margin-top:1px;">
																		${traveler.paymentType}
																	</div>
																</c:when>
															</c:choose>
														<span id="traveler_travelerName" class="sqcq-fj">
														<c:if test="${companyUuid ne '7a81a26b77a811e5bc1e000c29cf2586' || (companyUuid eq '7a81a26b77a811e5bc1e000c29cf2586' &&  empty traveler.main_orderId)}">
														<input type="checkbox" value="${traveler.id }@${traveler.visaorderId }" class="tdCheckBox2" name="activityId" onclick="t_idcheckchg(this)" trallerId="${traveler.id }" visaId="${traveler.visaId}"  trallerName="${traveler.travelerName }"/>
														</c:if>
														${traveler.travelerName}</span>
														
													<input type="hidden" value="${traveler.agentinfoId }" id="activityId_agentId_${traveler.id}">
													<input type="hidden" value="${traveler.fukuanButtonFlag }" id="fukuan_flag_${traveler.id}">		
													</td>
													<td>
														<span id="traveler_passportId">${traveler.passportId}</span>
													</td>
													<!-- <td>${traveler.AACode}</td> -->
													<td>${traveler.visaType}</td>
													<td>${traveler.visaCountry}</td>
													<td>${traveler.collarZoning}</td> 
													<td>${traveler.mainOrderNum}</td>
													<td>${traveler.cantuantuanhao}</td>
													<td>${traveler.groupType}</td>
													<td>
													<div class="yfje_dd">	
															<span class="fbold">
															${traveler.forecastStartOut}
															</span>
														</div>
														<div class="dzje_dd">
															<span class="fbold" >
																${traveler.startOut}
															</span>
														</div>
													</td>
													<td>
													
													<div class="yfje_dd">	
															<span class="fbold">
															${traveler.forecastContract}
															</span>
														</div>
														<div class="dzje_dd">
															<span class="fbold" >
																${traveler.contract}
															</span>
														</div>
														 <div class="dzje_dd">
															<span class="fbold" > 																						     
																<input id="traveler_contract_${traveler.id }" value="${traveler.contract}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" name="" type="text" />			
															</span>
														</div> 
														<!-- 懿洋假期的美国签证隐藏 实际约签时间C482-->
                                     					<%--<c:if test="${!(fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' && traveler.visaCountry=='美国')}">										
														<div class="dzje_dd">
															<span class="fbold" >
																${traveler.contract}
															</span>
														</div>
														 <div class="dzje_dd">
															<span class="fbold" > 																						     
																<input id="traveler_contract_${traveler.id }" value="${traveler.contract}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" name="" type="text" />			
															</span>
														</div> 
														</c:if> 
														<c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' && traveler.visaCountry=='美国'}">										
														<div class="dzje_dd" style="display:none">
															<span class="fbold" >
																${traveler.contract}
															</span>
														</div>
														 <div class="dzje_dd" style="display:none">
															<span class="fbold" > 																						     
																<input id="traveler_contract_${traveler.id }" value="${traveler.contract}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" name="" type="text" />			
															</span>
														</div>
														</c:if>--%>
														<!-- 懿洋假期的美国签证隐藏 实际约签时间 C482-->
														</td>
													<!-- 说明会时间 -->
													<td>
														<span style="color:green">${traveler.explanationTime}</span>
													</td>
													<td>
														<c:forEach items="${visaStatusList}" var="visaStatus">
															<c:if test="${visaStatus.value == 2}">
																	<c:if test="${visaStatus.value eq traveler.visaStatus}">已约签</c:if>
																</c:if>
																<c:if test="${visaStatus.value == 3}">
																	<c:if test="${visaStatus.value eq traveler.visaStatus}">通过</c:if>
																</c:if>
																<c:if test="${visaStatus.value != 3 and visaStatus.value !=2}">
																	<c:if test="${visaStatus.value eq traveler.visaStatus}">${visaStatus.label}</c:if>
																</c:if>
															
														</c:forEach>
													</td>
													<td>
														<c:if test="${ '1' eq traveler.passportStatus}">借出</c:if>
														<c:if test="${ '2' eq traveler.passportStatus}">销售已领取</c:if>
														<!--<c:if test="${ '3' eq traveler.passportStatus}">未签收</c:if>
														<c:if test="${ '4' eq traveler.passportStatus}">已签收</c:if>
														-->
														<c:if test="${ '4' eq traveler.passportStatus}">已还</c:if>
														<c:if test="${ '5' eq traveler.passportStatus}">已取出</c:if>
														<c:if test="${ '6' eq traveler.passportStatus}">未取出</c:if>
														<c:if test="${ '8' eq traveler.passportStatus}">计调领取</c:if>
													</td>
													<td class="tr">
														<c:if test="${ '1' eq traveler.guaranteeStatus}">担保</c:if>
														<c:if test="${ '2' eq traveler.guaranteeStatus}">担保+押金</c:if>
														<c:if test="${ '3' eq traveler.guaranteeStatus}">押金</c:if>
														<c:if test="${ '4' eq traveler.guaranteeStatus}">无需担保</c:if>
													</td>
													<!-- 借款状态 -->
													<td class="tc"><span><p align="center"> ${traveler.jiekuanStatus} </p></span></td>								
													
														 <!-- 0615需求,新增是否需要押金,是否需要上传资料限定为批发商大洋国际 --> 
										          <%--<c:if test="${fns:getUser().company.uuid eq '7a81a03577a811e5bc1e000c29cf2586'}">
													<td class="tc">
													<c:if test="${traveler.visaDeposit eq true}">是</c:if>
													<c:if test="${traveler.visaDeposit eq false}">否</c:if>
													</td>
													<td class="tc">
													<c:if test="${traveler.visaDatum eq 'true'}">是</c:if>
													<c:if test="${traveler.visaDatum eq 'false'}">否</c:if>
													</td>
													</c:if>--%>
													<td class="p0 tr"><c:if test="${traveler.guaranteeStatus eq 2 or traveler.guaranteeStatus eq 3}">${traveler.totalDeposit}</c:if></td>
													<td class="p0 tr">	
														<div class="yfje_dd">	
															<span class="fbold" id="traveleryingshouyajin${traveler.visaId}${traveler.id}">
															<!-- 
															<c:if test="${ '3' eq traveler.guaranteeStatus || '2' eq traveler.guaranteeStatus}">
															</c:if>
															 -->
															${traveler.payedDeposit}
															</span>
														</div>
														<div class="dzje_dd">
															<span class="fbold" id="accountedDeposit">
																${traveler.accountedDeposit}
															</span>
														</div>
													</td>											
													<td class="p0">
														<dl class="handle">
															<dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
															<dd class="">
																<p>
																	<span></span>
																	<c:if test="${traveler.orderStatus != '1' && traveler.activityLockStatus != '1'}">
																		<c:if test="${ empty traveler.mainOrderId}">
																		<c:if test="${companyUuid ne '7a81a26b77a811e5bc1e000c29cf2586' || (companyUuid eq '7a81a26b77a811e5bc1e000c29cf2586' &&  empty traveler.main_orderId)}">
																			<a href="${ctx}/order/manager/visa/joinGroup/joinGroupForm?orderId=${traveler.visaorderId}&travelerId=${traveler.id}">参团</a>
																		</c:if>
																		
<%-- 																		<a href="${ctx}/visaUpProces/list?orderId=${traveler.visaorderId}&productType=6&flowType=10"> 改价</a> --%>
																		<a href="${ctx}/changeprice/visa/list?orderId=${traveler.visaorderId}&productType=6&flowType=10"> 改价</a>
																		</c:if>
																		<!--  
																		<c:if test="${ '3' eq traveler.guaranteeStatus}">
																		<a href="${ctx}/order/manager/visa/warrantForm?proId=${traveler.visaorderId}">押金转担保</a>
																		</c:if>
																		-->
																		<a target="_blank" id="piliang" onclick="deleteOneTraveler(${traveler.id});" >删除</a>
																	</c:if>
																	<a target="_blank" href="${ctx}/guaranteeMod/list/${traveler.visaorderId}">担保变更</a>
															   </p>
															</dd>
														</dl>
													</td>
													
													<td class="p0">
														<dl class="handle">
															<dt><img src="${ctxStatic}/images/handle_xz_rebuild.png" title="下载"></dt>
															<dd>
																<p>
																	<span></span>   
																	 <a target="_blank" href="${ctx}/visa/order/downloadInterviewNotice?travelerId=${traveler.id}&orderId=${traveler.visaorderId}">面签通知</a>
																</p>
															</dd>
														</dl>
													</td>
									
													<td class="p0">
														<dl class="handle">
															<dt><img title="财务" src="${ctxStatic}/images/handle_fk_rebuild.png"></dt>
															<dd>
																<p>
																	<span></span>
																	<c:if test="${traveler.orderStatus != '1' && traveler.activityLockStatus != '1'}">
																		<c:if test="${ '3' eq traveler.guaranteeStatus}">
																		<!--  
	<a target="_blank" href="${ctx}/orderPay/pay?orderId=${traveler.visaorderId}&orderNum=${traveler.visaorderNo}&payPriceType=7&businessType=2&orderType=${traveler.groupTypeId}&agentId=${traveler.agentinfoId}&paramCurrencyId=${traveler.yajinHuobiId}&paramCurrencyPrice=${traveler.yajinJine}&visaId=${traveler.visaId}&paramTotalCurrencyId=${traveler.totalYajinHuobi}&paramTotalCurrencyPrice=${traveler.totalYajinJine}">交押金</a>
																		-->
																		</c:if>
																		<c:if test="${ 'true' == traveler.fukuanButtonFlag && 'F' != traveler.showFlag}">
																		<c:if test="${companyUuid ne '7a81a26b77a811e5bc1e000c29cf2586'}">
																			<a target="_blank" id="pay_t_${traveler.id }" onclick="pay('${traveler.id }','${traveler.visaorderId }','pay_t_${traveler.id }');">收款</a>
																		</c:if>
																		<c:if test="${companyUuid eq '7a81a26b77a811e5bc1e000c29cf2586' &&  empty traveler.main_orderId}">
																			<a target="_blank" id="pay_t_${traveler.id }" onclick="pay('${traveler.id }','${traveler.visaorderId }','pay_t_${traveler.id }');">收款</a>
																		</c:if>
																		</c:if>
																		<c:if test="${tuiYaJinFlag}">
																		<!--  
																			<a href="${ctx}/order/manager/visaDeposit/refundList?proId=${traveler.visaorderId}">退
</a>
																		-->
																		</c:if>
																		<c:if test="${huanYaJinShouJuFlag}">
																		<!--  
																			<a href="javascript:void(0)" onclick="jbox_hyjsj('${traveler.id}');">还押金收据</a>
																			<a href="javascript:void(0)" onclick="jbox_hyjsj_new('${traveler.id}');">New还押金收据</a>
																			-->
																		</c:if>
																		 <!--  
																		<c:if test="${jieKuanFlag == false}">
																			 <a href="javascript:void(0)" onclick="jbox_jk_qianwu('${traveler.id}');">
																				借款
																			 </a>
																		 </c:if>
																		 -->
																	</c:if> 
															   </p>
															</dd>
														</dl>
													</td>
												</tr>
												</c:forEach>
												<tr class="checkalltd">
													<td colspan='20' class="tl">
														<label>
															<input type="checkbox" name="allChk" onclick="t_checkall(this)">
															全选
														</label>
														<label>
															<input type="checkbox" name="allChkNo" onclick="t_checkallNo(this)">
															反选
														</label>
														<c:if test="${traveler.orderStatus != '1' && traveler.activityLockStatus != '1'}">
															<a target="_blank" id="piliang" onclick="batchPay('piliang','activityId');" >批量付款</a>
															<!--
															<c:if test="${tuiYaJinFlag}">
																<a id="piliang_t_tuiyajin" onclick="batchtuiyajin('piliang_t_tuiyajin','activityId');">批量退押金</a>
															</c:if>
															<c:if test="${jieKuanFlag}">
																<a onclick="batchJk(null);">批量借款</a>
															</c:if>
															-->
														</c:if>
														<a target="_blank" id="piliang" onclick="batchDeleteTraveler(null);" >批量删除游客</a>
													</td>
												</tr>
											</tbody>
										</table>
						<div class="pagination clearFix">${travelPage}</div>
					</div>
				</div>
				<!--右侧内容部分结束-->
				<!-- 0444需求 -->
			<div id="relationInvoice" class="display-none">
				<div class="select_account_pop" style="padding:20px">
					<div >
						<table class="activitylist_bodyer_table">
							<thead>
								<tr>
									<th>选择</th>
									<th>发票号</th>  
									<th>团号</th>
									<th>申请人</th>
									<th>开票状态</th>
									<th>开票金额</th>
								</tr>
							</thead>
							<tbody id="relationInvoiceTable">
								<tr>
									<td name='logId'  class='tc' value=''><input id="" type="checkbox" value=""></td>
									<td name='operation' class='tc'><span></span></td>
									<td name='operation' class='tc'><span></span></td>
									<td name='operationTime' class='tc'><span></span></td>
									<td name='mainContext' class='tl'><span></span></td>
									<td name='mainContext' class='tl'><span></span></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<%--T2改版 清除浮动 added by Tlw--%>
			<div class="clear"></div>
				
				
	<script type="text/javascript">
        $(document).ready(function () {
            var isEdit = true;
            $("table.paymentTable").find("tr td:nth-child(" + (isEdit ? 4 : 5) + ")").remove();

            $(document).on('click', 'a.delLink', function () {
                $("#jbox-content table.paymentTable tr").has(this).remove();
                getPayeeAmount();
                getPaymentAmount();
            }).on('keyup', 'table.paymentTable input:text', function () {
                getPaymentAmount();
            });



            function getPayeeAmount() {
                var amount = {};
                var $ps = $("#jbox-content table.paymentTable tbody tr td:nth-child(3) p");
                $.each($ps, function () {
                    var currency = $(this).attr("data-currency");
                    var money = (+$(this).attr("data-money"));
                    if (money) {
                        amount[currency] = (amount[currency] || 0) + money;
                    }
                });
                $("span.payeeAmouont").text(formatAmount(amount));
            }

            function getPaymentAmount() {
                var amount = {};
                var $ps = $("#jbox-content table.paymentTable tbody tr td:nth-child(4) p");
                $.each($ps, function () {
                    var currency, money;
                    if (isEdit) {
                        currency = $(this).find("input:text").attr("data-currency");
                        money = (+$(this).find("input:text").val());
                    } else {
                        currency = $(this).attr("data-currency");
                        money = (+$(this).attr("data-money"));
                    }
                    if (money) {
                        amount[currency] = (amount[currency] || 0) + money;
                    }
                });
                $("span.paymentAmount").text(formatAmount(amount));
            }

            function formatAmount(amount) {
                var str = [];
                for (var k in amount) {
                    if (amount[k]) {
                        if (amount[k] > 0 && str.length) {
                            str.push(" + ");
                        }
                        else if (amount[k] < 0) {
                            str.push(" - ");
                        }
                        str.push(k + Math.abs(amount[k]));
                    }
                }
                return str.join('');
            }
        });
    </script>
</body>
</html>

