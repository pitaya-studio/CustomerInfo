<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>
		查看提醒
	</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.excheck-3.5.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.all-3.5.js"></script>
	<script type="text/javascript">
		var tabIndex = 1;
		$(function () {
            inputTips();
            //下拉可选
            $("#GroupNoId").comboboxInquiry();
            $('.relateAirComp').mouseenter(function () {
                $('.relateAirCompDetail').show();
            })
            $('.relateAirComp').mouseleave(function () {
                $('.relateAirCompDetail').hide();
            })
    		// 初始设置产品tab显示
    		initialTabs();
    		// 初始给第一个产品线标签页填充内容
    		fillActivityTab(getFirstTab());
    		// tab标签页点击（切换tab，显隐而非删除）
//     		tabClickBind();
    		// TODO 填充全部团期（涉及）
    		if ('${remind.id}') {
	    		fillSrcActivityInfo(${remind.id});
			}
			// 详情页面 只读
			readOnly4Detail();
		});
		
		/**
		 * 详情页面只读
		 */
		function readOnly4Detail(){
			$("#remindName").attr("disabled", true);
			$("#remindType").attr("disabled", true);
			$("input[name^=groupSelect]").attr("disabled", true);
			$("input").attr("disabled", true);
			$("select").attr("disabled", true);
			$("div[id^=addSpecifyGroup]").hide();
			$("#submit").hide();
		}
			
		/**
		 * 初始设置产品tab显示
		 */
		function initialTabs(){
			// TODO
		}
		
		/**
		 * 给tab绑定点击事件
		 */
		function tabClickBind(){
			$("#payorderbgcolor").children().each(function(index, element){
				$(element).click(function(){
					// 获取activityType的index
					var tabIdStr = $(this).attr("id");
					var tabIndexStr = tabIdStr.substring(tabIdStr.indexOf("_a") + 2, tabIdStr.length);  // 截取tab的下标
					
					if (tabIndexStr != undefined && tabIndexStr != null && tabIndexStr != "") {
						tabIndex = Number(tabIndexStr);
					}
					$("#tabIndex").val(tabIndex);
					$("#selectedRemindOrderType").val(tabIndex);
					// 调用填充方法
					fillActivityTab(tabIndex);
				});
			});
		}
		
		/**
		 * 点击产品tab页，填充内容
		 * @param tabIndex tab页标识序号
		 */
		function fillActivityTab(tabIndex){
			// TODO
			// 判断内容节点是否存在内容
			var $targetTab =  $("#offlinebox_"+tabIndex);
			// 获取该类型所有的产品/团期信息
			var activityInfo = getAllActivityInfo(tabIndex);
			// 不存在子节点，则由模板生成并填充
			if ($targetTab.children().size() == 0) {
				var $template = $("#remindGroupTemplate");
				var _remindGroup = $template.clone();
				_remindGroup.find("input[id^=allGroup_]").attr("id", "allGroup_" + tabIndex).attr("name", "groupSelect" + tabIndex);  // 全团
				_remindGroup.find("input[id^=specifyGroup_]").attr("id", "specifyGroup_" + tabIndex).attr("name", "groupSelect" + tabIndex);  // 选团
				_remindGroup.find("div[id^=addSpecifyGroup_]").attr("id", "addSpecifyGroup_" + tabIndex);  // 进入选择团
				_remindGroup.find("table[id^=selectedGroup_]").attr("id", "selectedGroup_" + tabIndex);  // 已选中团
				_remindGroup.find("table[id^=specifyGroupList_]").attr("id", "specifyGroupList_" + tabIndex);  // 已选中团
				$targetTab.append(_remindGroup.children());
				// 展示产品/团期信息
				showActivityInfo(activityInfo, tabIndex);
			}
			showActivityInfo4Choose(activityInfo, tabIndex);
			// 处理样式
			$("#patorder_a"+tabIndex).addClass("patorderNewSelected").siblings().removeClass("patorderNewSelected");
			// 处理显示
			$("#patorder_a"+tabIndex).parent().siblings().children('#offlinebox_'+tabIndex).show().siblings().hide();
			
		}
		
		/**
		 * TODO
		 * 获取某类型下所有订单的产品团期信息
		 @param activityType 产品类型
		 */
		function getAllActivityInfo(activityType){
			// 如果参数不满足条件，则默认查询第一个产品类型
			if (activityType == undefined || activityType == null || activityType == "" || isNaN(activityType)) {
				activityType = Number($("#tabIndex").val());
			}
			//
			var activityInfo = new Array();
			// 组织参数
			var groupCode = $("div[id=jbox-content]").find("#groupCode").val();
			var activityName = $("div[id=jbox-content]").find("#activityName").val();
			var groupOpenDate = $("div[id=jbox-content]").find("#groupOpenDate").val();
			var groupCloseDate = $("div[id=jbox-content]").find("#groupCloseDate").val();
			var creator = $("div[id=jbox-content]").find("#creator").val();
			$.ajax({
				type : "POST",
				url : "../../activity/manager/getAllActivityInfo",
				async: false,
				data : {
					activityType : activityType,
					groupCode : groupCode,
					activityName : activityName,
					groupOpenDate : groupOpenDate,
					groupCloseDate : groupCloseDate,
					creator : creator
				},
				success : function(resultMap) {
					if(resultMap){
						if(resultMap.flag == "success"){
							activityInfo = resultMap.data;
							fillCreator(resultMap.creator);
						} else {
							top.$.jBox.tip('获取产品信息失败!详情:' + resultMap.message, 'error');
						}
					} else {
						top.$.jBox.tip('获取产品信息失败!', 'error');
					}
				}
			});
			return activityInfo;
		}
		
		/**
		 * 修改初始加载时，填充产品团期信息
		 */
		function fillSrcActivityInfo(remindId){
			var activityInfo = new Array();
			$.ajax({
				type : "POST",
				url : "../../activity/manager/getActivityInfoByRemind",
				async: false,
				data : {
					remindId : remindId
				},
				success : function(resultMap) {
					if(resultMap){
						if(resultMap.flag == "success"){
							activityInfo = resultMap.data;
							fillCreator(resultMap.creator);
						} else {
							top.$.jBox.tip('获取产品信息失败!详情:' + resultMap.message, 'error');
						}
					} else {
						top.$.jBox.tip('获取产品信息失败!', 'error');
					}
				}
			});
			var activityType = '${remind.selectedRemindOrderType}';
			if (activityType != '') {
				activityType = Number(activityType);
			} else {
				return;
			}
			
			// 填充
			if (${remind.selectedRemindType == 1 }) {
				$("#allGroup_" + activityType).click();
				showActivityInfo(activityInfo, activityType);
			} else if (${remind.selectedRemindType == 2 }) {
				$("#specifyGroup_" + activityType).click();
				showActivityInfo4JBox(activityInfo, activityType);
				// 取消checkbox
				$("#specifyGroupList_" + activityType).find("input[class=onceChk]").hide();
			}
			
		}
		
		/**
		 * 填充联系人
		 */
		function fillCreator(creator) {
			var creatorSel = $("#creator");
			var creatorSel = document.createElement("select");
			creatorSel.options.add(new Option("请选择", ""));
			for (var i = 0; i < creator.length; i++) {
				var cname = creator[i].name;
				var cid = creator[i].id;
				var newOpt = new Option(cname, cid);
				creatorSel.options.add(newOpt);
			}
			var $creatorList = $(creatorSel).clone();
			$creatorList.attr("id", "creator");
			$creatorList.attr("name", "creator");
			
			$("#creator").after($creatorList);
			$("#creator").remove();
		}
		
		function searchActivityInfo(activityType){
			var activityInfo = getAllActivityInfo(activityType);
			var tabIndex = Number($("#tabIndex").val());
			// 搜索之后显示新结果
			showActivityInfo4JBox(activityInfo, tabIndex);
		}
		
		/**
		 * TODO
		 * 展示产品信息到面板上
		 * @param activityInfo 产品信息
		 * @param tabIndex 产品类型
		 */
		function showActivityInfo4JBox(activityInfo, tabIndex){
			var $targetbody4JBox = $("#specifyGroupList_" + tabIndex).find("tbody[name=activityMainInfo]");  // 产品信息面板着陆点
			var html4JBox = "";
			// 如果没有信息， 或者没有产品信息集合
			if (activityInfo == undefined || activityInfo == null || activityInfo.length == 0) {
				html4JBox += "<tr><td colspan='6' class='tc'>无产品信息</td></tr>";
				$targetbody4JBox.empty();
				$targetbody4JBox.append(html4Choose);
				return;
			}
			
			// 解析组织html
			for ( var int = 0; int < activityInfo.length; int++) {
				html4JBox += "<tr>";
				html4JBox += "<td class='tc'>" + (int + 1) + "&nbsp;<input class='onceChk' type='checkbox' id='onceChk_" + activityInfo[int].groupId + "' name='onceChk' onclick=''></td>";  // 序号
				html4JBox += "<td class='tc'><input type='hidden' name='groupId' value='" + activityInfo[int].groupId + "'><span groupid='" + activityInfo[int].groupId + "'>" + activityInfo[int].groupCode + "</td>";  // 团号
				html4JBox += "<td class='tc'><input type='hidden' name='activityId' value='" + activityInfo[int].activityId + "'>" + activityInfo[int].acitivityName + "</td>";  // 产品名称
				html4JBox += "<td class='tc'>" + activityInfo[int].groupOpenDate + "</td>";  // 出团日期
				html4JBox += "<td class='tc'>" + activityInfo[int].groupCloseDate + "</td>";  // 截团日期
				html4JBox += "<td class='tc'><input type='hidden' name='activityCreatorId' value='" + activityInfo[int].activityCreatorId + "'>" + activityInfo[int].activityCreator + "</td>";  // 发布人
				html4JBox += "</tr>";
			}
			// 填充html
			$targetbody4JBox.empty();
			$targetbody4JBox.append(html4JBox);
		}
		
		/**
		 * TODO
		 * 展示产品信息到面板上
		 * @param activityInfo 产品信息
		 * @param tabIndex 产品类型
		 */
		function showActivityInfo(activityInfo, tabIndex){
			var $targetbody = $("#selectedGroup_" + tabIndex).find("tbody[name=activityMainInfo]");  // 产品信息面板着陆点
			var html = "";
			// 如果没有信息， 或者没有产品信息集合
			if (activityInfo == undefined || activityInfo == null || activityInfo.length == 0) {
				html += "<tr><td colspan='6' class='tc'>无产品信息</td></tr>";
				$targetbody.append(html);
				return;
			}
				
			// 解析组织html
			for ( var int = 0; int < activityInfo.length; int++) {
				html += "<tr>";
				html += "<td class='tc'>" + (int + 1) + "</td>";  // 序号
				html += "<td class='tc'><input type='hidden' name='groupId' value='" + activityInfo[int].groupId + "'>" + activityInfo[int].groupCode + "</td>";  // 团号
				html += "<td class='tc'><input type='hidden' name='activityId' value='" + activityInfo[int].activityId + "'>" + activityInfo[int].acitivityName + "</td>";  // 产品名称
				html += "<td class='tc'>" + activityInfo[int].groupOpenDate + "</td>";  // 出团日期
				html += "<td class='tc'>" + activityInfo[int].groupCloseDate + "</td>";  // 截团日期
				html += "<td class='tc'><input type='hidden' name='activityCreatorId' value='" + activityInfo[int].activityCreatorId + "'>" + activityInfo[int].activityCreator + "</td>";  // 发布人
				html += "</tr>";
			}
			// 填充html
			$targetbody.append(html);
		}
		
		/**
		 * TODO
		 * 展示产品信息到面板上
		 * @param activityInfo 产品信息
		 * @param tabIndex 产品类型
		 */
		function showActivityInfo4Choose(activityInfo, tabIndex){
			var $targetbody4Choose = $("#jbox_resubmit_o").find("tbody[name=activityMainInfo]");  // 产品信息面板着陆点
			var html4Choose = "";
			// 如果没有信息， 或者没有产品信息集合
			if (activityInfo == undefined || activityInfo == null || activityInfo.length == 0) {
				html4Choose += "<tr><td colspan='6' class='tc'>无产品信息</td></tr>";
				$targetbody4Choose.empty();
				$targetbody4Choose.append(html4Choose);
				return;
			}
			
			// 解析组织html
			for ( var int = 0; int < activityInfo.length; int++) {
				html4Choose += "<tr>";
				html4Choose += "<td class='tc'>" + (int + 1) + "&nbsp;<input class='onceChk' type='checkbox' id='onceChk_" + activityInfo[int].groupId + "' name='onceChk' onclick=''></td>";  // 序号
				html4Choose += "<td class='tc'><input type='hidden' name='groupId' value='" + activityInfo[int].groupId + "'><span groupid='" + activityInfo[int].groupId + "'>" + activityInfo[int].groupCode + "</td>";  // 团号
				html4Choose += "<td class='tc'><input type='hidden' name='activityId' value='" + activityInfo[int].activityId + "'>" + activityInfo[int].acitivityName + "</td>";  // 产品名称
				html4Choose += "<td class='tc'>" + activityInfo[int].groupOpenDate + "</td>";  // 出团日期
				html4Choose += "<td class='tc'>" + activityInfo[int].groupCloseDate + "</td>";  // 截团日期
				html4Choose += "<td class='tc'><input type='hidden' name='activityCreatorId' value='" + activityInfo[int].activityCreatorId + "'>" + activityInfo[int].activityCreator + "</td>";  // 发布人
				html4Choose += "</tr>";
			}
			// 填充html
			$targetbody4Choose.empty();
			$targetbody4Choose.append(html4Choose);
		}
		
		/**
		 * 获取批发商默认第一产品线
		 */
		function getFirstTab(){
			var srcIndex = '${remind.selectedRemindOrderType}';
			if(srcIndex != undefined && srcIndex != null && srcIndex != ""){
				return Number(srcIndex);
			} else {
				var firstIndex = 1;
				// 获取批发商产品线集合
				var activityTypeStr = $("#activityTypeStr").val();
				if (activityTypeStr != undefined && activityTypeStr != null && activityTypeStr != "") {
					var typeArray = new Array();
					typeArray = activityTypeStr.split(",");
					if (typeArray[0] != undefined && typeArray[0] != null) {
						firstIndex = typeArray[0];
					}
				}
				return firstIndex;
			}
		}
		
		/**
		 * 选择可见人(暂时废弃)
		 */
		function selectReciever(obj){
	        if ("" == "disabled"){
	            return true;
	        }
	        top.$.jBox.open("iframe:${ctx}/sys/remind/receiverPage", "选择提醒接收人", 320, 420,{
	            buttons:{"确定":"ok", "关闭":true}, submit:function(v, h, f){
	                if (v=="ok"){
	                    var tree = h.find("iframe")[0].contentWindow.receivers;
	                    var ids = [], names = [], nodes = [];
	                    if ("true" == "true"){
	                        nodes = tree.getCheckedNodes(true);
	                    }else{
	                        nodes = tree.getSelectedNodes();
	                    }
	                    for(var i=0; i<nodes.length; i++) {
	                        if (nodes[i].isParent){
	                            continue;
	                        }
	                        ids.push(nodes[i].id);
	                        names.push(nodes[i].name);
	                    }
	                }//
	            }, loaded:function(h){
	                $(".jbox-content", top.document).css("overflow-y","hidden");
	            },persistent:true
	        });
	    }
	    
	    /**
	     * 切换全部/选择团期
	     */
    	$("input[type=radio]").live('click', function(){
    		// 获取tabIndex
	    	var tabIndex = Number($(this).attr("id").substring($(this).attr("id").indexOf("_") + 1, $(this).attr("id").length));
			$(this).parent().find(".tablescroll").prev().show();
			
			if ($(this).attr("id")== ('allGroup_' + tabIndex)) {
				// 改变form:select 的值
				$(this).parent().children("select[name=selectedRemindType]").find("option[value=1]").attr("selected", true);
				$(this).parent().children("select[name=selectedRemindType]").find("option[value!=1]").attr("selected", false);
				//
				$("#selectedGroup_" + tabIndex).show();
				$("#specifyGroupList_" + tabIndex).hide();
				$("#addSpecifyGroup_" + tabIndex).hide();				
			} else {
				// 改变form:select 的值
				$(this).parent().children("select[name=selectedRemindType]").find("option[value=2]").attr("selected", true);
				$(this).parent().children("select[name=selectedRemindType]").find("option[value!=2]").attr("selected", false);
				//
				$("#selectedGroup_" + tabIndex).hide();
				$("#specifyGroupList_" + tabIndex).show();
				var html = "";
				html += "<div class='set-batch-op-butn-new' id='addSpecifyGroup_"+tabIndex+"' onclick='selectGroup(this);' style='display:none'>选择团期</div>";
				if($(this).parent().find("div[id^=addSpecifyGroup_]").length == 0){
					$(this).next().after(html);
				} else {
					$(this).parent().find("div[id^=addSpecifyGroup_]").attr("id", "addSpecifyGroup_" + tabIndex);
					$("#addSpecifyGroup_" + tabIndex).show();
				}
			}
    	});
    	
    	/**
    	 * 保存提醒
    	 */
    	function saveRemind(){
    		// 保存之前清除其他页签
    		var tabIndex = $("#tabIndex").val();
    		$("div[id^=offlinebox_]").each(function(index, element){
    			var boxId = "offlinebox_" + tabIndex;
    			if($(element).attr("id") != boxId){
    				$(element).empty();
    			}
    		});
    		// 组织 groupId, activityId, userId
    		organization4Field(tabIndex);
    		// 保存之前的校验
    		validationOfRemind();
    		
    		$("#inputForm").submit();
    	}
    	
    	/**
    	 * 组织 groupId, activityId, userId
    	 */
    	function organization4Field(tabIndex){
    		// 选择团期的方式
    		var selectedRemindType = $("#selectedRemindType").val();
    		// 团期id
    		var groupIdStr = "";
    		var $activityInfoBase;
    		if (selectedRemindType == 1) {  // 全部团期
				$activityInfoBase = $("#selectedGroup_" + tabIndex);
			} else {  // 选择团期
				$activityInfoBase = $("#specifyGroupList_" + tabIndex);
			}
			$activityInfoBase.find("input[name=groupId]").each(function(index, element){
				if (index > 0 && $(element).val() != "") {					
					groupIdStr += ",";
				}
				groupIdStr += $(element).val(); 
			});
    		$("#activityGroupIds").val(groupIdStr);
    		
    		// 产品id
    		var productIdStr = "";
    		$activityInfoBase.find("input[name=activityId]").each(function(index, element){
    			if (index > 0 && $(element).val() != "") {
					productIdStr += ",";
				}
				productIdStr += $(element).val();
			});
    		$("#productIds").val(productIdStr);
    		
    		// 团期id   TODO
    		var userIdStr = "";
    		userIdStr = $("#remindUserId").val();
    		var userIdArray = new Array();
    		userIdArray = userIdStr.split(",");
    		for ( var int = 0; int < userIdArray.length; int++) {
				if (userIdStr != "") {
					userIdStr += ",";
				}
				userIdStr += userIdArray[int];
			}
    		$("#userStr").val(userIdStr);
    		
    	}
    	
    	/**
    	 * 保存之前的校验
    	 */
    	function validationOfRemind () {
    		// TODO
    	}
		
    </script>
	
	<style type="text/css"></style>
</head>
<body>

	
	<form:form id="inputForm" modelAttribute="remind" action="${ctx}/sys/remind/save">
		<input name="tabIndex" id="tabIndex" type="hidden" value="${remind.selectedRemindOrderType }" >
		<form:input path="selectedRemindOrderType" type="hidden" />
		<form:hidden path="id"/>
		<table>
			<tr>
				<td style="text-align:right;width: 84px;">名称：</td>
				<td>
					<form:input path="remindName"/>
				</td>
			</tr>
			<tr>
				<td style="text-align:right;width: 84px;">提醒类型：</td>
				<td>
					<form:select path="remindType">
						<form:option value="1">还款提醒</form:option>
					</form:select>
				</td>
			</tr>
		</table>
		
		<table style="margin-top:20px">
			<tr>
				<td class="alignTop">选择团期提醒：</td>
				<td>
					<div id="offline_paybox" class="pay_clearfix" >
						<div id="payorderbgcolor">
                            <div id="patorder_a1" class="patordernew patorderNewSelected" >单团</div>
                            <div id="patorder_a2" class="patordernew">散拼</div>
                            <div id="patorder_a3" class="patordernew">游学</div>
                            <div id="patorder_a4" class="patordernew">大客户</div>
                            <div id="patorder_a5" class="patordernew">自由行</div>
                            <div id="patorder_a6" class="patordernew">签证</div>
                            <div id="patorder_a7" class="patordernew">机票</div>
                            <div id="patorder_a10" class="patordernew">游轮</div>
                       	</div>
                       	
                       	<div class="payORDER">
                       		<!-- 单团 -->
                           	<div id="offlinebox_1"  style="display:block"></div>
                           	<!--204 s 散拼 -->
                           	<div id="offlinebox_2" style="display:none;" class="groupDiv"></div>
                           	<!--204 s 游学-->
                           	<div id="offlinebox_3" style="display: none;" class="groupDiv"></div>
                           	<!--204 s 自由行-->
                           	<div id="offlinebox_4" style="display: none;" class="groupDiv"></div>
                           	<!--204 s 自由行 -->
                           	<div id="offlinebox_5" style="display: none;" class="groupDiv"></div>
                           	<!--204 s 签证-->
                           	<div id="offlinebox_6" style="display: none;" class="groupDiv"></div>
                           	<!--204 s 机票-->
                           	<div id="offlinebox_7" style="display: none;" class="groupDiv"></div>
                           	<!--204 s 游轮 -->
                           	<div id="offlinebox_10" style="display: none;" class="groupDiv"></div>
                           	
                       	</div>
					</div>
				</td>
			</tr>
		</table>
		<div style="height:20px"></div>
		<div style="margin-left: 100px;">
			<label>提醒接收人：</label>
			</br><label style="margin-left:50px">审批人员：</label><input type="checkbox" name="isVisible4Reviewer" id="isVisible4Reviewer" value="1" disabled="disabled" <c:if test='${remind.isVisible4Reviewer == 1 }'>checked=checked</c:if> >
			</br><label style="margin-left:50px">其他人员：（注意：选中的人员可以看到本规则包含的所有团期相关的还款提醒）</label>
			</br><tags:treeselect id="user" name="userIdList" value="${remind.userIds}"  labelName="userNameList" labelValue="${remind.userNames}"
                      title="提醒接收人" url="/activity/manager/filterOpUserTreeData" checked="true"/>
		</div>
		
		<div class="dbaniu">
			<a href="${ctx}/sys/remind/list" class="ydbz_s gray">取消</a>
			<input type="submit" id="submit" class="btn btn-primary" onclick="saveRemind();" value="保存">
		</div>
	
	
<!--204 start 选择团期弹窗内容-->
<div id="jbox_resubmit_o" class="hide">
    <div class="jbox_resubmit" style="padding-bottom:10px">
        <div style="margin:20px;margin-left:190px">
            <label>团号：</label>
            <input name="groupCode" id="groupCode" class="inputTxt">
            <label>产品名称：</label>
            <input name="activityName" id="activityName" class="inputTxt">
            <label>出团日期：</label>
            <input id="groupOpenDate" class="inputTxt dateinput" name="groupOpenDate" onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" readonly="">
            <span style="font-size:12px; font-family:'宋体';"> 至</span>
            <input id="groupCloseDate" class="inputTxt dateinput" name="groupCloseDate" onclick="WdatePicker()" readonly="">
            <label>发布人：</label>
            <select style="width:100px" id="creator" name="creator">
            	<option value="">请选择</option>
                <c:forEach items="${creator }" var="creator">
                	<option value="${creator.id }">${creator.name }</option>
                </c:forEach>
            </select>
            <input type="button" value="搜索" id="search-form-btn-id" class="btn btn-primary" onclick="searchActivityInfo()">
        </div>
        <div class="tablescroll" style="max-height:383px; margin:10px;">
            <table id="contentTable" class="activitylist_bodyer_table">
                <thead>
                    <tr>
                        <th width="10%">序号</th>
                        <th width="15%">团号</th>
                        <th width="15%">产品名称</th>
                        <th width="14%">出团日期</th>
                        <th width="14%">截团日期</th>
                        <th width="8%">发布人</th>
                    </tr>
                </thead>
            	<tbody name="activityMainInfo">
            	
	            </tbody>
	        </table>
	    </div>
	    <div style="margin-left:74px">
	        <dt>
	            <input name="allChk" id="checkAll" type="checkbox">全选
	        </dt>
	    </div>
	</div>
</div>
<!--240 end 选择团期弹窗内容-->

<div id="remindGroupTemplate" style="display: none;">
		<form:select path="selectedRemindType" style="display: none;" value="${remind.selectedRemindType }">
			<form:option value="1" class="prev" selected="selected">全部团期</form:option>
			<form:option value="2" class="after">指定团期</form:option>
		</form:select>
		<input type="radio" name="groupSelect" id="allGroup_" <c:if test="${remind.selectedRemindType ne 2 }">checked="checked"</c:if>><label>全部团期(包含未来生成的团期)</label>
		<input type="radio" name="groupSelect" id="specifyGroup_" style="margin-left:60px" <c:if test="${remind.selectedRemindType eq 2 }">checked="checked"</c:if>><label>指定团期</label>
		<div class="set-batch-op-butn-new" id="addSpecifyGroup_" onclick="selectGroup(this);" style="display:none">选择团期</div>

              		<div class="groupRequirement">
              			<div class="specifyGroupdate">
              				<table>
              					<tr>
              						<td><label>提醒起始时间：</label></td>
              						<td>
              							<span class="editDeadline" style="">
              								<span>还款日期</span>
              								<form:select path="startRemindStatus" class="bg-prev-after" style="width:50px;margin-bottom:0">
												<form:option value="-1" class="prev">前</form:option>
												<form:option value="1" class="after">后</form:option>
											</form:select>
											<form:input path="startRemindDays" class="txtPro inputTxt" flag="istips" maxlength="20"/>天
              							</span>
              						</td>
              						<td><label style="text-align:right;width: 84px;">过期时间：</label></td>
              						<td>
              							<span class="editDeadline" style="">
              								<span>还款日期</span>
              								<form:select path="endRemindStatus" class="gq-prev-after" style="width:50px;margin-bottom:0">
												<form:option value="-1" class="prev">前</form:option>
												<form:option value="1" class="after">后</form:option>
											</form:select>
											<form:input path="endRemindDays" class="txtPro inputTxt" flag="istips" maxlength="20"/>天
              							</span>
              						</td>
              					</tr>
              				</table>
              			</div>
              		</div>
              		
              		<div style="margin-top:10px">
              			<p class="hide">已选团期：</p>
              			<div class="tablescroll">
              				
              					<table class="table activitylist_bodyer_table selectedGroup" id="selectedGroup_">
									<thead style="background: #403738">
										<tr>
											<th width="5%">序号</th>
	                                        <th width="5%">团号</th>
	                                        <th width="10%">产品名称</th>
	                                        <th width="10%">出团日期</th>
	                                        <th width="8%">截团日期</th>
	                                        <th width="10%">发布人</th>
										</tr>
									</thead>
									<tbody name="activityMainInfo">
                                      
									</tbody>
								</table>
								
								<table class="table activitylist_bodyer_table hide specifyGroupList selectedGroup" id="specifyGroupList_">
                                	<thead style="background: #403738">
                                        <tr>
                                        	<th width="5%">序号</th>
                                            <th width="5%">团号</th>
                                            <th width="10%">产品名称</th>
                                            <th width="10%">出团日期</th>
                                            <th width="8%">截团日期</th>
                                            <th width="10%">发布人</th>
                                        </tr>
                                    </thead>
                                    <tbody name="activityMainInfo">

                                	</tbody>
                                </table>
                                
                                <form:input path="activityGroupIds" type="hidden"/>
                                <form:input path="productIds" type="hidden"/>
                                <div>
                                	<ul id="choosedRemindUsers">
                                		
                                	</ul>
                                </div>
                              	<form:input path="userStr" type="hidden"/>
              				
              			</div>
                  	</div>
                  	
                    <div class="approve-bill-list abl"></div>
</div>
</form:form>
</body>
</html>
