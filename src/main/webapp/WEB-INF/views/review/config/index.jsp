<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>审批配置</title>
<meta name="decorator" content="wholesaler" />
<link rel="stylesheet" href="${ctx}/static/css/jbox.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
<link type="text/css" rel="stylesheet"	href="${ctxStatic}/css/jquery.validate.min.css" />

<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>


<!--<script type="text/javascript" src="jquery-ztree/3.5.12/js/jquery-1.4.4.min.js"></script>-->
<script type="text/javascript"	src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript"	src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.excheck-3.5.js"></script>

<script type="text/javascript">
	$(function() {

		//搜索条件筛选
		launch();
		//操作浮框
		operateHandler();

		$("#channel").comboboxInquiry();

		//取消一个checkbox 就要联动取消 全选的选中状态
		$("input[name='ids']").click(function() {
			if ($(this).attr("checked")) {

			} else {
				$("input[name='allChk']").removeAttr("checked");
			}
		});
		$(document).ready(function(){
			$('#contentTable tr').mouseover(function(){
				$(this).find(".attribute-display .n-limited,.attribute-display .n-necessary").show();
			});
			$('#contentTable tr').mouseout(function(){
				$(this).find(".attribute-display .n-limited,.attribute-display .n-necessary").hide();
			});
		});

		//注册select可输入可下拉
//		$("#deptId" ).comboboxInquiry();

		var resetSearchParams = function() {
			$(':input', '#searchForm').not(':button, :submit, :reset, :hidden')
					.val('').removeAttr('checked').removeAttr('selected');
			$('#country').val("");
		}
		$('#contentTable')
				.on(
						'change',
						'input[type="checkbox"]',
						function() {
							if ($('#contentTable input[type="checkbox"]').length == $('#contentTable input[type="checkbox"]:checked').length) {
								$('[name="allChk" ]').attr('checked', true);
							} else {
								$('[name="allChk" ]').removeAttr('checked');
							}
						});

		$(document).on(
				'click',
				'#contentTable tbody input[type="checkbox"]',
				function() {
					var $checkbox=$(this);
					if ($(this).is(':checked')) {

						var modelId=$(this).val();
						var processKey=$(this).siblings(":hidden").val();
						var serialNumber=$(this).attr("name");
						//记录当前checkbox
						$.ajax({
							type: "POST",
							url: "${ctx}/sys/review/configuration/deploy",
							dataType:"json",
							data: {
								"modelId" :modelId,
								"processKey" : processKey,
								"serialNumber":serialNumber
							},
							success: function(data){
								if (data.code==0){
									$checkbox.parent().parent().find('a[name="delete"]')
											.attr('disabled', 'disabled');
									$.jBox.tip("启用成功！");
//									$.jBox($('div[name="startupTip"]').html(), {
//										title : "",
//										buttons : {
//											'确定' : 1
//										},
//										height : 220,
//										width : 380
//									});
								}else{
									//勾选状态取消
									$checkbox.removeAttr("checked");
									$('div[name="commonTip"] div span').html(data.msg);
									$.jBox($('div[name="commonTip"]').html(), {
										title : "",
										buttons : {
											'确定' : 1
										},
										height : 220,
										width : 380
									});
								}
							}
						});
					} else {

						var processKey=$(this).siblings(":hidden").val();
						var serialNumber=$(this).attr("name");
						$.jBox($('div[name="blockupTip"]').html(), {
							title : "",
							buttons : {
								'停用' : 1,
								'取消' : 2
							},
							submit : function(v, h, f) {
								if (v == "1") {
									$.ajax({
										type: "POST",
										url: "${ctx}/sys/review/configuration/undeploy",
										dataType:"json",
										data: {
											"processKey" :processKey,
											"serialNumber":serialNumber
										},
										success: function(data){
											if (data.code==0){
												$checkbox.parent().parent().find('a[name="delete"]')
														.removeAttr('disabled');
											}else{
												$('div[name="commonTip"] div span').html(data.msg);
												$.jBox($('div[name="commonTip"]').html(), {
													title : "",
													buttons : {
														'确定' : 1
													},
													height : 220,
													width : 380
												});
											}
										}
									});
								}else if(v == '2'){
//									$('#'+serialNumber).attr('checked','checked');
									$checkbox.attr('checked','checked');
								}
							},
							height : 220,
							width : 380
						});
					}
				});
		$(document).on(
				'click',
				'#contentTable tbody  [name=delete]',
				function() {
					if (!$(this).parents('tr:first').find(
							'input[type="checkbox"]').is(':checked')) {
						var contextPath ="${ctx}";
						var $checkbox=$(this).parents('tr:first').find('input[type="checkbox"]');
						var processKey=$checkbox.siblings(":hidden").val();
						var serialNumber=$checkbox.attr("name");
						//先验证是否有再审数据
						$.ajax({
							type: "POST",
							url: "${ctx}/sys/review/configuration/delete/validation",
							dataType:"json",
							data: {
								"processKey" : processKey,
								"serialNumber":serialNumber
							},
							success: function(data){
								if (data.code==0){
									$.jBox($('div[name="deleteTip"]').html(), {
										title : "",
										buttons : {
											'确认' : 1,
											'取消' : 2
										},
										submit : function(v, h, f) {
											if (v == '1') {
												$.ajax({
													type: "POST",
													url: "${ctx}/sys/review/configuration/delete",
													dataType:"json",
													data: {
														"processKey" : processKey,
														"serialNumber":serialNumber
													},
													success: function(data){
														if (data.code==0){
															window.open (contextPath + "/sys/review/configuration/index","_self");
														}else{
															alert( data.msg );
														}
													}
												});
											}
										},
										height : 220,
										width : 380
									});
								}else{
									$.jBox($('div[name="deleteForbiddenTip"]').html(), {
										title : "",
										buttons : {
											'返回' : 1
										},
										height : 220,
										width : 380
									});
								}
							}
						});
					}
				});

	});

	//展开收起
	function expand(child, obj) {
		if ($(child).is(":hidden")) {
			$(child).show();
			$(obj).parents("td").addClass("td-extend");
			$(obj).parents("tr").addClass("tr-hover");

		} else {
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).parents("tr").removeClass("tr-hover");

		}
	}
	function checkall(obj) {
		if ($(obj).attr("checked")) {
			$('#contentTable input[type="checkbox"]').attr("checked", 'true');
			$("input[name='allChk']").attr("checked", 'true');
		} else {
			$('#contentTable input[type="checkbox"]').removeAttr("checked");
			$("input[name='allChk']").removeAttr("checked");
		}
	}

	function checkreverse(obj) {
		var $contentTable = $('#contentTable');
		$contentTable.find('input[type="checkbox"]').each(function() {
			var $checkbox = $(this);
			if ($checkbox.is(':checked')) {
				$checkbox.removeAttr('checked');
			} else {
				$checkbox.attr('checked', true);
			}
		});
	}

	$(document).on('click', '.delRow', function() {
		$(this).parents('tr').remove();
	});

	/**
	 * 表单重置，不能改成 $('#searchForm')[0].reset()
	 * 输入条件点击查询之后，再点击条件重置按钮，此方法在Google Chrome 下失效
	 * @author shijun.liu
	 */
	function resetForm(){
		var inputArray = $('#searchForm').find("input[type!='hidden'][type!='submit'][type!='button']");
		var selectArray = $('#searchForm').find("select");
		for(var i=0;i<inputArray.length;i++){
			if($(inputArray[i]).val()){
				$(inputArray[i]).val('');
			}
		}
		for(var i=0;i<selectArray.length;i++){
			var selectOption = $(selectArray[i]).children("option");
			$(selectOption[0]).attr("selected","selected");
		}
	}

</script>
</head>

<body>
<!--右侧内容部分开始-->
<%--added for UG_V2 添加tab at 20170227 by tlw start.--%>
<content tag="three_level_menu">
	<li class="active"><a href="javascript:void(0)">审批配置</a></li>
</content>
<%--added for UG_V2 添加tab at 20170227 by tlw end.--%>
                        <form id="searchForm" action="" method="post">
							<div class="activitylist_bodyer_right_team_co">
								<div class="activitylist_bodyer_right_team_co2">
									<label class="activitylist_team_co2_text">部门：</label>
										<select id="channel" name="deptId">
											<option value="">全部</option>
											<c:forEach var="dept" items="${departments}">
												<option value="${dept.key}" <c:if test="${ (not empty deptId) and (dept.key eq deptId) }">selected</c:if>>${dept.value}</option>
											</c:forEach>
										</select>
								</div>
								<div class="zksx filterButton_solo">筛选</div>
								<div class="form_submit">
									<input id="btn_search" value="搜索" class="btn btn-primary ydbz_x" type="submit">
									<input class="btn ydbz_x" type="button" onclick="resetForm();" value="清空所有条件">
								</div>
								<p class="main-right-topbutt"><a href="${ctx}/sys/review/configuration/add" target="_blank" id="all" class="select primary" style="float:right">新增审批流程</a></p>
								<div class="ydxbd" style="display: block;">
									<span></span>
									<%--<div class="activitylist_bodyer_right_team_co1">--%>
										<%--<div class="activitylist_team_co3_text" id="searchDept">部门：</div>--%>
										<%--<select id="channel" name="deptId">--%>
											<%--<option value="">全部</option>--%>
											<%--<c:forEach var="dept" items="${departments}">--%>
												<%--<option value="${dept.key}" <c:if test="${ (not empty deptId) and (dept.key eq deptId) }">selected</c:if>>${dept.value}</option>--%>
											<%--</c:forEach>--%>
										<%--</select>--%>
									<%--</div>--%>
									<div class="activitylist_bodyer_right_team_co1">
										<label class="activitylist_team_co3_text" id="searchProductType">产品类型：</label>
										<div class="selectStyle">
											<select name="productType">
												<option value="">全部</option>
												<c:forEach var="productType" items="${productTypes}">
													<option value="${productType.key}" <c:if test="${ (not empty productTypeId) and (productType.key eq productTypeId) }">selected</c:if>>${productType.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="activitylist_bodyer_right_team_co1">
										<label class="activitylist_team_co3_text" id="searchProcessType">流程名称：</label>
										<div class="selectStyle">
											<select name="processType">
												<option value="">全部</option>
												<c:forEach var="processType" items="${processTypes}">
													<option value="${processType.key}" <c:if test="${ (not empty processTypeId) and (processType.key eq processTypeId) }">selected</c:if> >${processType.value}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								</div>
							</div>
                        </form>
                        <!--状态结束-->
                        <table id="contentTable" class="table activitylist_bodyer_table mainTable">
                            <thead>
                                <tr>
                                    <th width="5%">状态</th>
                                    <th width="5%">序号</th>
                                    <th width="35%">部门</th>
                                    <th width="20%">产品类型</th>
                                    <th width="20%">流程名称</th>
                                    <th width="10%">操作</th>
                                </tr>
                            </thead>
                            <c:forEach var="record" items="${records}" varStatus="status">
                                <tbody>
                                <tr id="vertical-align-top">
                                    <td class="tc">
                                        <input type="checkbox" id="${record.serialNumber}" name="${record.serialNumber}" value="${record.modelId}" <c:if test="${record.enable eq 1}">checked</c:if> >
										<input name="processKey" type="hidden" value="${record.processKey}"/>
                                        <label for="checkbox">启用</label>
                                    </td>
                                    <td class="tc"><label for="checkbox"></label>
                                        ${status.index+1}
                                    </td>
                                    <td class="tl  vertical-align-top">
                                        <span>
                                        <c:forEach var="dept" items="${record.departments}">
                                            <p>${dept}</p>
                                        </c:forEach>
                                       </span>
                                    </td>
                                    <td class="tl vertical-align-top">
                                        <%--<span>--%>
                                        <c:forEach var="product" items="${record.productTypes}">
                                            <%--<p>${product}</p>--%>
                                            <span>${product}</span>
                                        </c:forEach>
                                        <%--</span>--%>
                                    </td>
                                    <td>
										<c:if test="${record.processKey eq needNoReviewProcessKey}">
										<p class="attribute-display">
											<em class="n-necessary">无需审批</em>
										</p>
										</c:if>
                                        <%--<span>--%>
                                        <c:forEach var="process" items="${record.processTypes}" varStatus="status">
                                            <%--<p>${process}</p>--%>
											<c:if test="${status.last}"><span>${process}</span></c:if>
											<c:if test="${! status.last}"><span>${process}、</span></c:if>

                                        </c:forEach>
                                        <%--</span>--%>
                                    </td>
                                    <td class="tl main-configure-operator">
                                        <a href="${ctx}/sys/review/configuration/modifyInfo?serialNumber=${record.serialNumber}" target="_blank">修改</a>
                                        <a href="javascript:void(0);" name="preview"
												<c:choose>
													<c:when test="${record.processKey eq needNoReviewProcessKey}">disabled="disabled" </c:when>
													<c:otherwise>onclick="jbox_approve_view_img_pop('${ctx}/sys/review/configuration/model/diagram/${record.modelId}');"</c:otherwise>
												</c:choose> >预览</a>
                                        <%--<a href="javascript:void(0);">复制并新增</a>--%>
                                        <a href="javascript:void(0);"  name="delete" <c:if test="${record.enable eq 1}">disabled="true" </c:if> >删除</a>
                                    </td>
                                </tr>
                                </tbody>
                            </c:forEach>
                        </table>
						<div name="startupTip" style="display: none" >
							<div class="blockupTip">
								<span>启用成功！</span>
							</div>
						</div>
						<div name="commonTip" style="display: none" >
							<div class="blockupTip">
								<span></span>
							</div>
						</div>
						<div name="blockupTip" style="display: none" >
							<div class="blockupTip">
								<span>停用后，新发起的申请将无法执行此流程</span>
								<p>未审批完成的数据将继续执行此流程</p>
							</div>
						</div>
						<div name="deleteTip" style="display: none" >
							<div class="deleteTip">
								<span>确认删除此流程！</span>
							</div>
						</div>
						<div name="deleteForbiddenTip" style="display: none" >
							<div class="deleteTip">
								<span>此流程尚有未审批完成的数据，暂时无法删除！</span>
							</div>
						</div>
                        <!--右侧内容部分结束-->

<!--S流程图片预览-->
<div id="approve_view_img_pop" style="display:none">
	<div class="approve_view_img_pop">
		<img src="" onerror="javascript:this.src='${ctxStatic}/images/configure-flow-no-pic.jpg';"/>
	</div>
</div>
<!--E流程图片预览-->
</body>
</html>