
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>签证参团</title>
<meta name="decorator" content="wholesaler" />
    

<link href="css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
var applyNum = 0;
//************获得申请游客人数的方法,通过游客参团表格的tbody中的选中复选框的数目来计数-tgy-20151225*****************//
function getApplicationTouristNums(){
	applyNum = $(".activitylist_bodyer_table").children("tbody").find("input:checked").length;
}
//*******************************************************************************************//
$(function(){
	//申请游客人数
	applyNum = $(".activitylist_bodyer_table").children("tbody").find("tr").length;
});
	
	function jbox_chaxu(){
		var chaxu=$('#chaxun-tk').html();
		var html = '<div class="add_allactivity" style=" padding:20px;">';
		html += chaxu;
		html += '</div>';
		$.jBox(html, { title: "",buttons:{'提交': 1}, submit:function(v, h, f){
			if (v=="1"){
				var value="";
				var radio=document.getElementsByName("danxuan");
				for(var i=0;i<radio.length;i++){
					if(radio[i].checked==true){
						value=radio[i].value;
						break;
					}
				}
				$("#joinGroupCode").val(value);
				return true;
			}
		},height:350,width:520});
	}

	function getJoinGroupInfo(viewObj) {
		var groupOpenDate = $(viewObj).parent().parent().find("input[name=groupOpenDate]").val();
		var groupCode = $(viewObj).parent().parent().find("input[name=groupCode]").val();
		var orderType = $(viewObj).parent().parent().find("select[name=orderType]").val();
		if (groupOpenDate!='' && groupOpenDate != null) {
			$.ajax({
				type: "POST",
				dataType:"json",
				url: "${ctx}/order/manager/visa/joinGroup/findGroupListByCondition",
				data: {
				groupOpenDate : groupOpenDate,
				groupCode : groupCode,
				orderType : orderType
				},
				success: function(msg){
					var html = "<table class='table table-striped table-bordered table-condensed '>"+
						"<thead>"+
							"<tr>"+
								"<th width='10%'>序号</th>"+
								"<th width='15%'>团号</th>"+
								"<th width='20%'>出团日期</th>"+
								"<th width='15%'>产品类型</th>"+
								"<th width='15%'>计调</th>"+
								"<th width='15%'>余位</th>"+
							"</tr>"+
						"</thead>"+
						"<tbody>";

					if (msg.length > 0) {
						$.each(msg,function(index, obj){
							html+="<tr>"+
							"<td class='tc'><input type='radio' style='margin:0;' value='"+ obj.groupCode +"' name='danxuan'></td>"+
							"<td class='tc'>" + obj.groupCode +"</td>"+
							"<td class='tc'>" + obj.groupOpenDate +"</td>"+
							"<td class='tc'>" + obj.type +"</td>"+
							"<td class='tc'>" + obj.createBy +"</td>"+
							"<td class='tc'>" + obj.freePosition +"</td>"+
						"</tr>";
						});
					} else {
						html += "<tr class='toptr'>"+
							"<td colspan='15' style='text-align: center;'>没有符合条件的记录</td>"+
						"</tr>";
					}

					html += "</tbody>"+
					"</table>";

					$(viewObj).parent().parent().next().html(html);
				}
			});
		} else {
			$.jBox.info("请选择出团日期！", "信息");
		}
	}

	function getGroupInfo() {
		var groupCode = $("#joinGroupCode").val();
		if('' != groupCode) {
			
	       	$.ajax({
	       		type: "POST",
	            url: "${ctx}/order/manager/visa/joinGroup/getGroupInfo",
	            data: {
	            	groupCode : groupCode,
	           		orderAgentId : "${visaOrder.agentinfoId}"
	            },
	           	//async:false,
                success: function(result){
               	    if(null != result && "" != result) {
               	    	//参团游客
               	    	var joinGroupTravelers = "";
               	    	$(".travelerName").each(function(index, obj) {
               	    		joinGroupTravelers += $(obj).text() + ",";
               	    	});
               	    	//截取最后一个逗号
               	    	if('' != joinGroupTravelers) {
               	    		joinGroupTravelers = joinGroupTravelers.substring(0,joinGroupTravelers.length - 1);
               	    	}
               	    	//拼接html
               	    	var html = $('<dd></dd>')
               	    		.append('<div class="ydbz_tit">参团信息</div>' +
               	    		'<div class="orderdetails1">' +
               	    		'<table width="98%" border="0" style="margin-left: 25px"><tbody>' +
               	    		'<tr>' +
               	    			'<input type="hidden" id="groupId" value="' + result.groupId + '" />' +
	               	    		'<td class="mod_details2_d1">参团游客：</td>' +
								'<td id="joinTravelerName" class="mod_details2_d2"></td>' +
								'<td class="mod_details2_d1">团队类型：</td>' +
								'<td class="mod_details2_d2">' + result.activityKind + '</td>' +
								'<td class="mod_details2_d1">团号：</td>' + 
								'<td class="mod_details2_d2">' + result.groupCode + '</td>' +
							'</tr>' +
							'<tr>' +
								'<td class="mod_details2_d1">余位：</td>' +
								'<td class="mod_details2_d2 freePosition">' + result.freePosition + '</td>' +
								'<td class="mod_details2_d1">参团计调：</td>' +
								'<td class="mod_details2_d2">' + result.createBy + '</td>' +
								'<td class="mod_details2_d1">产品名称：</td>' +
								'<td class="mod_details2_d2">' + result.activityName + '</td>' +
							'</tr></tbody></table></div>');
               	    	if(0 != $(".xt-qzct").find("dd").length) {
               	    		$(".xt-qzct").find("dd").remove();
               	    	}
               	    	$(".xt-qzct").append(html);
               	    	$(".xt-qzct").find("dd").show();
               	    	//占位方式
               	    	var payModeText = ['','订金占位','预占位','全款支付','资料占位','担保占位','确认单占位','计调确认占位','财务确认占位'];
               	    	var payModes = result.payMode;
          	    		if(0 != $("#payModeSelect").children("option").length) {
          	    			$("#payModeSelect").children("option").remove();
          	    		}
               	    	$(payModes.split(",")).each(function(index, obj) {
               	    		$("#payModeSelect").append('<option value="' + obj + '">' + payModeText[obj] + '</option>')
               	    	});
               	    	
               	    	//切位情况
               	    	if(null != result.reserve && applyNum <= result.reserve) {
               	    		$("#reserveSelect").append('<option value="1">占位</option>');
               	    		$("#reserveSelect").append('<option value="2">切位</option>');
               	    	}
               	    	//添加参团游客
               	    	getCheckedTName();
               	    }else{
               	    	$.jBox.info("未找到对应的团期！", "信息");
               	    }
           		}
	       	});
		}else{
			$.jBox.info("请输入团号！", "信息");
		}
	}
	//提交参团申请
	function joinGroupSub() {
		var freePosition = $(".freePosition").text();
		if('' != freePosition && undefined != freePosition) {
			//验证是否有选中的游客
			var isClicked = false;
			if(0 < $("[name='travelerIds']:checked").length || 1==$(".checkedId").length) {
				isClicked = true;
			}else{
				$.jBox.info("请选择参团游客！", "信息");
				return false;
			}
			//如果有足够的余位
			if(parseInt(freePosition) >= applyNum && isClicked) {
				
				var agentIdSelect = $("#agentIdSelect").clone();
				var payModeSelect = $("#payModeSelect").clone();
				var reserveSelect = $("#reserveSelect").find("option").length == 0?"":$("<p><label>占位方式：</label><select name='reserve'>" + $('#reserveSelect').clone().html() + "</select></p>");
				
				var reserve = 0;
				if("" != reserveSelect) {
					reserve = reserveSelect.val();
				}
				
				var payModeWindow_con = $('<div class="add_allactivity" style="padding-top:15px;"></div>')
					.append('<p><label>&nbsp;&nbsp;渠道：</label><select name="agentId">' + agentIdSelect.html() +'</select></p>')
					.append('<p><label>付款方式：</label><select name="paytype">' + payModeSelect.html() +'</select></p>')
					.append(reserveSelect)
				var payModeWindow_btn = $('<div class="tc" style="padding-top:15px;">' + 
							'<input class="btn btn-primary" type="button" value="参团" onclick="joinGroup(this)" /></div>');
				var payModeWindow = $('<div></div>').append(payModeWindow_con).append(payModeWindow_btn);
				
				if("" != reserveSelect) {
					reserveSelect.show();
				}
				
				//弹窗
				$.jBox(payModeWindow.prop("outerHTML"), { title: "预定_付款方式", buttons:{}, height:190,width:410});
			}else{
				$.jBox.info("余位数不足！", "信息");
			}
		}else{
			$.jBox.info("请输入团号！", "信息");
		}
	}
	
	//提交签证参团
	function joinGroup(dom) {
		var tmp =   ${visaOrder.id};
		var $agentId = $(dom).parent().parent().find("[name='agentId']");
		var $paytype = $(dom).parent().parent().find("[name='paytype']");
		var $reserve = $(dom).parent().parent().find("[name='reserve']");
		var groupId = $("#groupId").val();
		var travelerIdArr = [];
		$("[name='travelerIds']:checked").each(function(index, obj) {
			travelerIdArr.push($(obj).val());
		});
		if(1 == $(".checkedId").length) {
			travelerIdArr.push($(".checkedId").val());
		}
		$.ajax({
       		type: "POST",
            url: "${ctx}/order/manager/visa/joinGroup/joinGroupApply?visaOrderId="+tmp,
            data: {
            	agentId : $agentId.val(),
            	payMode : $paytype.val(),
           		reserve : $reserve.val(),
           		groupId : groupId,
           		travelerIdArr : travelerIdArr.join(',')
            },
            success: function(result){
            	if(null != result.errorMsg && "" != result.errorMsg) {
            		$.jBox.info(result.errorMsg + "！", "信息");
            	}else{
            		var orderId = result.orderId;
            		var travelerIds = result.travelerIds;
            		$(".jbox-close").click();
            		top.$.jBox.tip.mess = null;
            		top.$.jBox.tip("正在处理，请稍候。",'loading',{opacity:0});
            		window.location.href = "${ctx}/orderCommon/manage/getOderInfoById?productorderById=" +orderId;
            	}
             }
		});
	}
	//选择游客
	function checkall(obj){
		if($(obj).attr("checked")){
			$("input[name='allChk']").attr("checked",'true');
			$("input[name='travelerIds']").attr("checked",'true');
		}				
		else{
			$("input[name='allChk']").removeAttr("checked");
			$("input[name='travelerIds']").removeAttr("checked");
		}
		getCheckedTName();
	}

	//获取选中的游客名称
	function getCheckedTName() {
		$("#joinTravelerName").html("");
		var checkedTNames = "";
		$("input[name='travelerIds']:checked").each(function(index, obj) {
			checkedTNames += $(obj).parent().parent().find(".travelerName").html() + ",";
		});
		checkedTNames = checkedTNames.substring(0, checkedTNames.length-1);
		var travelerNameOne = "";
		if(1 == $(".checkedId").length) {
			travelerNameOne = $(".activitylist_bodyer_table").find(".travelerName").html();
		}
		$("#joinTravelerName").html(travelerNameOne + checkedTNames);
	}

	
</script>

</head>
<body>
	<div class="mod_nav">订单 > 签证 > 参团</div>
	<div class="ydbz_tit">订单详情</div>
		<ul class="ydbz_info">
			<li>
				<span>销售：</span>${visaOrder.salerName}
			</li>
			<li>
				<span>下单人：</span>${visaOrder.createBy.name}
			</li>
			<li>
				<span>下单时间：</span><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaOrder.createDate}"/>
			</li>
			<li>
				<span>收客人：</span>${visaOrder.createBy.name}
			</li>
			<li>
				<!-- <span>操作人：</span>${fns:getUser().name} //以前用这个-->
				<span>操作人：</span>${visaProducts.createBy.name}
			</li>
			<li>
				<span>订单编号：</span>${visaOrder.orderNo}
			</li>
			<li>
				<!-- C460V3 团号取自产品团号-->
				<c:if test="${fns:getUser().company.uuid eq '7a816f5077a811e5bc1e000c29cf2586'}"><!-- 环球行 -->
				   <span>订单团号：</span>${visaOrder.groupCode}
				</c:if>
				
				<!-- C460V3 团号取自产品团号-->
				<c:if test="${fns:getUser().company.uuid ne '7a816f5077a811e5bc1e000c29cf2586'}"><!-- 环球行 -->
				   <span>团号：</span>${visaProducts.groupCode}
				</c:if>
				
				
			</li>
		</ul>
     <div class="ydbz_tit">产品信息</div>                
        <ul class="ydbz_info">
			<li>
				<span>产品编号：</span>${visaProducts.productCode}
			</li>
			<li>
				<span>签证国家：</span><c:if test="${not empty visaProducts.sysCountryId and visaProducts.sysCountryId ne ''}">${fns:getCountryName(visaProducts.sysCountryId)}</c:if>
			</li>
			<li>
				<span>签证类别：</span>${fns:getDictLabel(visaProducts.visaType,'new_visa_type','')}
			</li>
			<li>
				<span>领区：</span><c:if test="${not empty visaProducts.collarZoning }">${fns:getDictLabel(visaProducts.collarZoning,'from_area','')}</c:if>
			</li>
			
            <c:if test="${visaCostPriceFlag eq 1}">
            <li><span>成本价格：</span>${currency.currencyMark }<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${visaProducts.visaPrice}" />/人</li>
            </c:if>
			<li><span>应收价格：</span>${currency.currencyMark }<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${visaProducts.visaPay}" />/人</li>
			
			<li>
				<span>创建时间：</span><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaProducts.createDate}"/>
			</li>
		</ul>
    <div class="ydbz_tit">游客参团</div>
    <div class="orderVisaDep">
		<table class="activitylist_bodyer_table">
			<thead>
				<tr>
					<c:if test="${empty param.travelerId and fn:length(travelerInfoList) gt 1}">
						<th width="5%">全选</br><input type="checkbox" onclick="checkall(this)" name="allChk" /></th>
					</c:if>
					<th width="10%">姓名</th>
					<th width="15%">签证状态</th>
					<th width="15%">实际约签时间</th>
					<th width="15%">押金金额</th>
					<th width="15%">护照号</th>
					<th width="25%">护照有效期</th>
				</tr>
			</thead>
			<tbody>
			
				<c:forEach items="${travelerInfoList}" var="travelerInfo">
					<tr>
						<input type="hidden" value="${travelerInfo[0]}" name="travelerid"/>
						
							<c:choose>
								<c:when test="${empty param.travelerId and fn:length(travelerInfoList) gt 1}">
									<td><input type="checkbox" name="travelerIds" value="${travelerInfo[0]}" onClick="getCheckedTName();" /></td>
								</c:when>
								<c:otherwise>
									<input type="text" name="travelerIds" value="${travelerInfo[0]}" style="display: none" class="checkedId"/>
								</c:otherwise>
							</c:choose>
						<td class="travelerName">${travelerInfo[1]}</td>
						<td>
							<c:choose>
								<c:when test="${travelerInfo[2] eq '0'}">未送签</c:when>
								<c:when test="${travelerInfo[2] eq '1'}">送签</c:when>
								<c:when test="${travelerInfo[2] eq '2'}">约签</c:when>
								<c:when test="${travelerInfo[2] eq '3'}">出签</c:when>
								<c:when test="${travelerInfo[2] eq '5'}">已撤签</c:when>
								<c:when test="${travelerInfo[2] eq '7'}">拒签</c:when>
								<c:when test="${travelerInfo[2] eq '8'}">调查</c:when>
								<c:when test="${travelerInfo[2] eq '9'}">续补资料</c:when>
							</c:choose>
						</td>
						<td>
							<c:if test="${not empty travelerInfo[3]}"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${travelerInfo[3]}"/></c:if>
						</td>
						<td class="tr tdred">${travelerInfo[4]}</td>
						<td>${travelerInfo[5]}</td>
						<td>
							<c:if test="${not empty travelerInfo[6]}"><fmt:formatDate pattern="yyyy-MM-dd" value="${travelerInfo[6]}"/></c:if>
						</td>
					</tr>
				</c:forEach>
				
				<c:if test="${empty travelerInfoList }">
					<tr>
						<td colspan="6" style="text-align: center;">暂无符合参团条件的游客</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
	<dl class="xt-qzct">
		<dt>
			<label class="fl">转入团号:</label>
			<input class="inputTxt fl" type="text" id="joinGroupCode">
			<div class="ydbz_x" onclick="jbox_chaxu()" >查询</div>
			<a class="ydbz_x" href="javascript:void(0)" onclick="getGroupInfo();">确定</a>
		</dt>
	</dl>
	<div class="dbaniu">
		<select id="agentIdSelect" style="display: none;" class="typeSelected">
			<!--  
		    <c:if test="${loginuserDeptid!=68}">
		       <option value="-1" <c:if test="${visaOrder.agentinfoId eq -1 }">selected="selected"</c:if> >非签约渠道</option>
		    </c:if>
			-->
			<c:forEach items="${agentInfoList }" var="agentInfo">
				<option value="${agentInfo.id }" <c:if test="${visaOrder.agentinfoId eq agentInfo.id }">selected="selected"</c:if> >${agentInfo.agentName }</option>
			</c:forEach>
		</select>
		<select id="payModeSelect" style="display: none;" class="typeSelected"></select>
		<select id="reserveSelect" style="display: none;" class="typeSelected"></select>
		<a class="ydbz_x gray" onclick="history.go(-1)">取消</a>
		<a class="ydbz_x" onclick="getApplicationTouristNums();joinGroupSub();" href="javascript:void(0)">申请参团</a>
	</div>
	<!--查询弹框 begin-->
	<div class="add_allactivity no" id="chaxun-tk" style="margin-top:20px; padding-left:20px;">
		<dl class="chaxun-tankuang">
			<dt>
				<label><em class="xing">*</em>出团日期：</label><input onclick="WdatePicker()" id="groupOpenDate" name="groupOpenDate" value="" class="inputTxt dateinput fl">
				<p><label>团号：</label><input id="groupCode" name="groupCode" value="" class="inputTxt fl"></p>
			</dt>
			<div class="kong"></div>
			<dt>
				<label><em class="xing">&nbsp;&nbsp;</em>产品类型：</label>
				<select id="orderType" name="orderType">
					<option value="">请选择</option>
					<c:forEach items="${fns:getDictList('order_type')}" var="orderType">
						<!-- 产品类型 (不包含签证和机票)-->
						<c:if test="${orderType.value <= 5}">
							<option value="${orderType.value}">${orderType.label}</option>
						</c:if>
					</c:forEach>
				</select>
			</dt>
			<dt><span class="ydbz_x" onclick="getJoinGroupInfo(this);">查询</span></dt>
		</dl>
		<div id="groupHtml" style="clear: both;">
		</div>
	</div>
	<!--查询弹框 end-->
</body>
</html>