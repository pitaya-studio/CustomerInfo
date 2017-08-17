<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>
		<c:if test="${empty id}">
			添加提醒
		</c:if>
		<c:if test="${not empty id}">
			修改提醒
		</c:if>
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
// 		var allInfos = new Array();  // 存储所有类型产品信息， 每次加载新的选项卡存入，切换回旧卡取出。
// 		var activityInfo = new Array();  // 产品信息
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
    		tabClickBind();
    		// TODO 填充全部团期（涉及）
    		if ('${remind.id}') {
	    		fillSrcActivityInfo(${remind.id});
			}
		});
		
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
			// 更新参数
			$("#tabIndex").val(tabIndex);
			$("#selectedRemindOrderType").val(tabIndex);
// 			showActivityInfo4Choose(activityInfo, tabIndex);
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
			showActivityInfo(activityInfo, activityType);
			showActivityInfo4JBox(activityInfo, activityType);
			showActivityInfo4Choose(activityInfo, activityType);
			if (${remind.selectedRemindType == 1 }) {
				$("#allGroup_" + activityType).click();
			} else if (${remind.selectedRemindType == 2 }) {
// 				$("#specifyGroup_" + activityType).click();
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
		
		/**
		 * 弹窗搜索
		 */
		function searchActivityInfo(activityType){
			var activityInfo = getAllActivityInfo(activityType);
			var tabIndex = Number($("#tabIndex").val());
			// 搜索之后显示新结果
			showActivityInfo4JBox(activityInfo, tabIndex);
		}
		
		/**
		 * TODO
		 * 展示产品信息到面板上（弹出框初始展示、搜索展示）
		 * @param activityInfo 产品信息
		 * @param tabIndex 产品类型
		 */
		function showActivityInfo4JBox(activityInfo, tabIndex){
			var $targetbody4JBox = $("#jbox-states").find("tbody[name=activityMainInfo]");  // 产品信息面板着陆点
			var html4JBox = "";
			// 如果没有信息， 或者没有产品信息集合
			if (activityInfo == undefined || activityInfo == null || activityInfo.length == 0) {
				html4JBox += "<tr><td colspan='6' class='tc'>无产品信息</td></tr>";
				$targetbody4JBox.empty();
				$targetbody4JBox.append(html4JBox);
				return;
			}
			// 解析组织html
			for ( var int = 0; int < activityInfo.length; int++) {
				html4JBox += "<tr>";
				html4JBox += "<td class='tc'>" + (int + 1) + "&nbsp;<input class='onceChk' type='checkbox' id='onceChk_" + activityInfo[int].groupId + "' name='onceChk' onclick=''></td>";  // 序号
				html4JBox += "<td class='tc'><input type='hidden' name='groupId' value='" + activityInfo[int].groupId + "'><span groupid='" + activityInfo[int].groupId + "'>" + activityInfo[int].groupCode + "</span></td>";  // 团号
				html4JBox += "<td class='tc'><input type='hidden' name='activityId' value='" + activityInfo[int].activityId + "'>" + activityInfo[int].acitivityName + "</td>";  // 产品名称
				html4JBox += "<td class='tc'>" + activityInfo[int].groupOpenDate + "</td>";  // 出团日期
				html4JBox += "<td class='tc'>" + activityInfo[int].groupCloseDate + "</td>";  // 截团日期
				html4JBox += "<td class='tc'><input type='hidden' name='activityCreatorId' value='" + activityInfo[int].activityCreatorId + "'>" + activityInfo[int].activityCreator + "</td>";  // 发布人
				html4JBox += "</tr>";
			}
			// 填充html
			$targetbody4JBox.empty();  // 必须先清空
			$targetbody4JBox.append(html4JBox);
		}
		
		/**
		 * TODO
		 * 展示产品信息到面板上（全部团期）
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
			if ($("#jbox_resubmit_o_" + tabIndex).size() == 0) {  
		    	var $jboxMain = $("#jbox_resubmit_o").clone();  // 弹框主体
		    	$jboxMain.attr("id","jbox_resubmit_o_" + tabIndex);  // 添加id
		    	$("#offlinebox_" + tabIndex).append($jboxMain);  // 追加到页面
		    }
			var $targetbodyPreJBox = $("#jbox_resubmit_o_" + tabIndex).find("tbody[name=activityMainInfo]");  // 为弹出框做准备的信息面板着陆点
			// 解析组织html
			for ( var int = 0; int < activityInfo.length; int++) {
				html += "<tr>";
				html += "<td class='tc'>" + (int + 1) + "</td>";  // 序号
				html += "<td class='tc'><input type='hidden' name='groupId' value='" + activityInfo[int].groupId + "'><span groupid='" + activityInfo[int].groupId + "'>" + activityInfo[int].groupCode + "</span></td>";  // 团号
				html += "<td class='tc'><input type='hidden' name='activityId' value='" + activityInfo[int].activityId + "'>" + activityInfo[int].acitivityName + "</td>";  // 产品名称
				html += "<td class='tc'>" + activityInfo[int].groupOpenDate + "</td>";  // 出团日期
				html += "<td class='tc'>" + activityInfo[int].groupCloseDate + "</td>";  // 截团日期
				html += "<td class='tc'><input type='hidden' name='activityCreatorId' value='" + activityInfo[int].activityCreatorId + "'>" + activityInfo[int].activityCreator + "</td>";  // 发布人
				html += "</tr>";
			}
			// 填充html
			$targetbody.append(html);
			$targetbodyPreJBox.append(html);
		}
		
		/**
		 * TODO
		 * 展示产品信息到面板上 (已经选中的选择团期)
		 * @param activityInfo 产品信息
		 * @param tabIndex 产品类型
		 */
		function showActivityInfo4Choose(activityInfo, tabIndex){
			var $targetbody4Choose = $("#specifyGroupList_" + tabIndex).find("tbody[name=activityMainInfo]");  // 产品信息面板着陆点
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
				html4Choose += "<td class='tc'>" + (int + 1) + "&nbsp;<input class='onceChk' type='checkbox' id='onceChk_" + activityInfo[int].groupId + "' name='onceChk' onclick=''><a href='javascript:void(0);' onclick='delChosedInfo(this)'>删除</a></td>";  // 序号
				html4Choose += "<td class='tc'><input type='hidden' name='groupId' value='" + activityInfo[int].groupId + "'><span groupid='" + activityInfo[int].groupId + "'>" + activityInfo[int].groupCode + "</span></td>";  // 团号
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
				$("#specifyGroupList_" + tabIndex).find("input[class=onceChk]").hide();
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
    	
    	//204 start 选择团期
		function selectGroup(obj){
		    var $alreadyhas = $(obj).next().next().find(".specifyGroupList tbody");
		    var activityType = Number($("#selectedRemindOrderType").val());
		    var checkall =0;
		    //弹出框
		    var $pop = $.jBox($("#jbox_resubmit_o").html(), {
		        title    : "选择团期", buttons: {'确定': 1, '取消': 2}, submit: function (v, h, f) {
		            if (v == "1") {
// 		            	$alreadyhas.empty();
		                if($pop.find('input:checked').length > 0 ){//checkbox有选中
		                    $pop.find('input:checked').not("#checkAll").each(function(){
		                        var alh = 0;
		                        var $srctr = $(this).parent().parent();
		                        $(this).hide();
		                        var $trhtml= $srctr.clone();
		                        $trhtml.find("input[name=onceChk]").after("<a href='javascript:void(0);' onclick='delChosedInfo(this)'>删除</a>");
		                        $alreadyhas.find("tr span").each(function(){
		                            if($(this).attr("groupid")== $trhtml.find("span").attr("groupid")){
		                                alh+=1;
		                            }
		                        });
		
		                        if(alh==0){
		                            $alreadyhas.append($trhtml);
		                        }
		                    });
		                }else{
		                    $alreadyhas.html("");
		                }
		                return true; 
		            }
		        }, height: '600', width: 1300
		    });
		
		    $(obj).next().next().find(".specifyGroupList tr").find("span").each(function(){
		        var currentId = $(this);
		        $pop.find("td span").each(function(){
		            if(currentId.attr("groupid")==$(this).attr("groupId")){
		                $(this).parent().prev().find("input").attr("checked","checked");
		                checkall=checkall+1;
		            }
		        });
		        if(checkall==$pop.find("#contentTable tbody").children().length){
		            //$pop.find("#checkAll").attr("checked", 'checked');  //bug:13421
		        }
		    });
		
		    $pop.find("#checkAll").click(function(){
		        if($(this).attr("checked")){
		            $pop.find(".onceChk").attr("checked", 'checked');
		        }else{
		            $pop.find(".onceChk").removeAttr("checked");
		        }
		    });
		    
		    $pop.find(".onceChk").click(function(){
		
		        if($(this).attr("checked")){
		           
		        }else{
		            $pop.find("#checkAll").removeAttr("checked");
		        }
		        if($pop.find("#contentTable tbody").children().length == $pop.find('input:checked').not("#checkAll").length){
		            $pop.find("#checkAll").attr("checked", 'checked');
		        }
		
		    });
		    
		    inquiryCheckBOX();
		}
		//204 end 选择团期
		
		/**
		 * 删除已经选中的信息
		 */
		function delChosedInfo(obj){
			$(obj).parent().parent().remove();
		}
    	
    	/**
    	 * 保存提醒
    	 */
    	function saveRemind(){
			// 保存之前的校验
			var flag = true;
			flag = validationOfRemind();
			if(!flag){
				return;
			}
    		// 保存之前清除其他页签  (或可以不用清空，而且浪费时间)
    		var tabIndex = $("#tabIndex").val();
    		$("div[id^=offlinebox_]").each(function(index, element){
    			var boxId = "offlinebox_" + tabIndex;
   				// 如果是最后编辑的tab页，则将其中remind信息写入表单
    			if($(element).attr("id") == boxId){
    				$("#inputForm").append($("#userId").clone().hide());  // 提醒人
    				var isVisible4ReviewerValue = $("#isVisible4Reviewer").val();
    				if ($("#isVisible4Reviewer").is(":checked")) {
						isVisible4ReviewerValue = 1;
					} else {
						isVisible4ReviewerValue = 0;
					}
    				$("#inputForm").append("<input type='hidden' name='isVisible4Reviewer' id='isVisible4Reviewer' value= " + isVisible4ReviewerValue + " >");  // 审批人员是否接收提醒
    				$("#inputForm").append($(element).find("#selectedRemindType").clone());  //  选团方式
    				var startRemindStatusValue = Number($(element).find("#startRemindStatus").val());
    				$("#inputForm").append("<input type='hidden' name='startRemindStatus' id='startRemindStatus' value=" + startRemindStatusValue + " >");  //  提醒日期
    				
    				var startRemindDaysValue = Number($(element).find("#startRemindDays").val());
    				$("#inputForm").append("<input type='hidden' name='startRemindDays' id='startRemindDays' value=" + startRemindDaysValue + " >");  //  提醒日期
    				
    				var endRemindStatusValue = Number($(element).find("#endRemindStatus").val());
    				$("#inputForm").append("<input type='hidden' name='endRemindStatus' id='endRemindStatus' value=" + endRemindStatusValue + " >");  //  提醒日期
    				
    				var endRemindDaysValue = Number($(element).find("#endRemindDays").val());
    				$("#inputForm").append("<input type='hidden' name='endRemindDays' id='endRemindDays' value=" + endRemindDaysValue + " >");  //  提醒日期
    			}
    		});

    		// 组织 groupId, activityId, userId
    		organization4Field(tabIndex);
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
    	}
    	
    	/**
    	 * 保存之前的校验
		 * @date 2016年4月12日
    	 */
    	function validationOfRemind () {
    		// 获取要提交的tab页节点
    		var tabIndex = $("#tabIndex").val();
    		var $tabNode = $("#offlinebox_" + tabIndex);
			if(!$("#remindName").val()){		//名称
				top.$.jBox.tip("请填写名称", "warning");
				return false;
			}
			if(!$("#remindType").val()){		//提醒类型
				top.$.jBox.tip("请选择提醒类型", "warning");
				return false;
			}
			if (!$tabNode.find("#selectedRemindType").val() || Number($tabNode.find("#selectedRemindType").val()) == 0) {
				top.$.jBox.tip("请选择团期/产品的选中方式", "warning");
				return false;
			}
			out:
			if($tabNode.find("#startRemindDays").val() == "" || $tabNode.find("#endRemindDays").val() == "" ){	//还款日期
				top.$.jBox.tip("提醒起始时间及过期时间必填！");
				return false;
			}else{
				var startRemindStatus = Number($tabNode.find("#startRemindStatus").val());	//-1：前 1：后
				var endRemindStatus = Number($tabNode.find("#endRemindStatus").val());
				if(startRemindStatus < endRemindStatus){
					break out;
				}else if(startRemindStatus > endRemindStatus){
					top.$.jBox.tip("过期时间小于提醒起始时间，请重新设置！", "warning");
					return false;
				}else{
					var startRemindDays = Number($tabNode.find("#startRemindDays").val());
					var endRemindDays = Number($tabNode.find("#endRemindDays").val());
					if (startRemindStatus == -1 && (startRemindDays<=endRemindDays) ){
						top.$.jBox.tip("过期时间小于提醒起始时间，请重新设置！", "warning");
						return false;
					}else if(startRemindStatus==1 && (startRemindDays>=endRemindDays) ){
						top.$.jBox.tip("过期时间小于提醒起始时间，请重新设置！", "warning");
						return false;
					}
				}
			}
// 			if(!$("input[name=userNameList]").val()){		//提醒接收人
// 				top.$.jBox.tip("请选择提醒接收人", "warning");
// 				return false;
// 			}
			return true;
    	}
		
    </script>
	
	<style type="text/css"></style>
	<style type="text/css">.input-append{margin-left:50px}</style>
</head>
<body>
	
	<form:form id="inputForm" modelAttribute="remind" action="${ctx}/sys/remind/save">
		<input name="tabIndex" id="tabIndex" type="hidden" value="${remind.selectedRemindOrderType }" >
		<form:input path="selectedRemindOrderType" type="hidden" />
		<form:input path="activityGroupIds" type="hidden"/>
        <form:input path="productIds" type="hidden"/>
		<form:hidden path="id"/>
		<table>
			<tr>
				<td style="text-align:right;width: 84px;"><span class="xing">*</span>名称：</td>
				<td>
					<form:input path="remindName" maxlength="20"/>
				</td>
			</tr>
			<tr>
				<td style="text-align:right;width: 84px;"><span class="xing">*</span>提醒类型：</td>
				<td>
					<form:select path="remindType">
						<form:option value="1">还款提醒</form:option>
					</form:select>
				</td>
			</tr>
		</table>
	</form:form>
	
	
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
			</br><label style="margin-left:50px">审批人员：</label><input type="checkbox" name="isVisible4Reviewer" id="isVisible4Reviewer" value="1" <c:if test='${remind.isVisible4Reviewer == 1 }'>checked=checked</c:if> >
			</br><label style="margin-left:50px">其他人员：（注意：选中的人员可以看到本规则包含的所有团期相关的还款提醒）</label>
			</br><tags:treeselect id="user" name="userIdList" value="${remind.userIds}"  labelName="userNameList" labelValue="${remind.userNames}"
                      title="提醒接收人" url="/activity/manager/filterOpUserTreeData" checked="true"/>
		</div>
		
		<div class="dbaniu">
			<a href="${ctx}/sys/remind/list" class="ydbz_s gray">取消</a>
			<input type="submit" class="btn btn-primary" onclick="saveRemind();return false;" value="保存">
		</div>

<div id="remindGroupTemplate" style="display: none;">
	<span class="red">友情提示：此处产品线只可单选，只有选中的选项卡会被保存。</span></br>
	<select name="selectedRemindType" id="selectedRemindType" style="display: none;" value="${remind.selectedRemindType }">
		<option value="0" selected="selected">未选择</option>
		<option value="1" class="prev">全部团期</option>
		<option value="2" class="after">指定团期</option>
	</select>
	<input type="radio" name="groupSelect" id="allGroup_" <c:if test="${remind.selectedRemindType eq 1 }">checked="checked"</c:if>><label>全部团期(包含未来生成的团期)</label>
	<input type="radio" name="groupSelect" id="specifyGroup_" style="margin-left:60px" <c:if test="${remind.selectedRemindType eq 2 }">checked="checked"</c:if>><label>指定团期</label>
	<div class="set-batch-op-butn-new" id="addSpecifyGroup_" onclick="selectGroup(this);" style="display:none">选择团期</div>

             		<div class="groupRequirement">
             			<div class="specifyGroupdate">
             				<table>
             					<tr>
             						<td><label>提醒起始时间：</label></td>
             						<td>
             							<span class="editDeadline" style="">
             								<span class="xing">*</span><span>还款日期</span>
             								<select name="startRemindStatus" id="startRemindStatus" class="bg-prev-after" style="width:50px;margin-bottom:0" >
												<option value="-1" class="prev" <c:if test="${remind.startRemindStatus eq -1 }">selected='selected'</c:if> >前</option>
												<option value="1" class="after" <c:if test="${remind.startRemindStatus eq 1 }">selected='selected'</c:if> >后</option>
											<select>
										<input name="startRemindDays" id="startRemindDays" class="txtPro inputTxt" flag="istips" maxlength="20" value="${remind.startRemindDays }"
													onafterpaste="this.value=this.value.replace(/\D/g,'')"
													onkeyup="this.value=this.value.replace(/\D/g,'')"/>天
             							</span>
             						</td>
             						<td><label style="text-align:right;width: 84px;">过期时间：</label></td>
             						<td>
             							<span class="editDeadline" style="">
             								<span class="xing">*</span><span>还款日期</span>
             								<select name="endRemindStatus" id="endRemindStatus" class="gq-prev-after" style="width:50px;margin-bottom:0" >
												<option value="-1" class="prev" <c:if test="${remind.endRemindStatus eq -1 }">selected='selected'</c:if> >前</option>
												<option value="1" class="after" <c:if test="${remind.endRemindStatus eq 1 }">selected='selected'</c:if> >后</option>
											</select>
										<input name="endRemindDays" id="endRemindDays" class="txtPro inputTxt" flag="istips" maxlength="20" value="${remind.endRemindDays }"
													onafterpaste="this.value=this.value.replace(/\D/g,'')"
													onkeyup="this.value=this.value.replace(/\D/g,'')"/>天
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
                               
                               <div>
                               	<ul id="choosedRemindUsers">
                               		
                               	</ul>
                               </div>
             				
             			</div>
                 	</div>
                 	
                   <div class="approve-bill-list abl"></div>
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

</body>
</html>
