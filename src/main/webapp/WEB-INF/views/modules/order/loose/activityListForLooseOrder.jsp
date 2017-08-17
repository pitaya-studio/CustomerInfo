<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<shiro:hasPermission name="looseOrder:book">
		<c:if test="${showType==1}"><title>预订-散拼</title></c:if>
		<c:if test="${showType==2}"><title>预报名-散拼</title></c:if>
	</shiro:hasPermission>
	<meta name="decorator" content="wholesaler"/>

	<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/priceList.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/inputToSelect.css">
	<script type="text/javascript" src="${ctxStatic}/js/jquery.placeholder.min.js"></script>

	<script src="${ctxStatic}/jquery-validation/1.11.0/lib/jquery.form.js" type="text/javascript"></script>
	<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
    <%--t2改版 去掉重复引用的样式 modified by Tlw--%>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/order/single/activityListForOrder.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/order/single/preOrderForLooseOrder.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/PriceList.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/inputToSelect.js"></script>
	<c:set var="officeShelfRightsStatus" value="${officeShelfRightsStatus }"></c:set>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.css" />

	<style type="text/css">
        <%--bug17231--%>
        .handle dd {
            left: 42%;
        }
        #contentTable{
            min-width:1030px;
        }
		@media only screen and (max-width: 1430px){
			.price-list {
				margin-left: 5%;
			}
			span.price-title {
				width: 45%;
			}
            .handle dd {
                left: 35%;
            }
            .handle dd span {
                margin-left: 31px;
            }
		}
		@media only screen and (max-width:1300px){
			#groupform{
				overflow-x:auto;
                width: 100%;
			}
            #sea {
                min-width: auto;
            }
            .main-right {

                min-width: auto;
            }
		}
		.ui-front {z-index: 2100;}
		.sort{color:#0663A2;cursor:pointer;}
		#contentTable th {
			height: 40px;
			/*border-top: 1px solid #CCC;*/   /*根据bug#12722注释掉*/
		}
		#teamTable{
			border:1px solid #CCC;
		}
		/* 备注浮框 */
		.ico-remarks-td {
			height: 16px;
			width: 31px;
			background-image: url("${ctxStatic}/images/ico-remarks-td.png");
			background-repeat: no-repeat;
			position: absolute;
			top: -1px;
			left: 0px;
			cursor: pointer;
		}

        #quauqAgentInput{
            width:300px;
            margin-bottom: 10px;
        }
	</style>

	<script type="text/javascript">
	
		var groupIds="";	
		function selectQuery(){
			$("#activityCreate" ).comboboxInquiry();
		}
		$(function(){
			$("#targetAreaId").val("${travelActivity.targetAreaIds}");
			$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
			$("#activityCreate").comboboxInquiry();
			//renderSelects(selectQuery());
		});
	   
		// 全选
		function checkall(obj) {
			if ($(obj).attr("checked")) {
				$("input[name='allChk']").attr("checked",'true');
				$("input[name='groupid']").attr("checked",'true');
				$("input[name='activityId']").attr("checked",'true');
				$("input[name='groupid']:checked").each(function(i,a){
					var arr = groupIds.split(",");
					if(arr.indexOf(a.value) < 0){
						groupIds = groupIds + a.value+",";
					}
  				});
  				// 不管反选框选中了没有，去掉被选中
  				$("input[name='allChkRev']").removeAttr("checked");
			} else {
				$("input[name='allChk']").removeAttr("checked");
				$("input[name='groupid']").removeAttr("checked");
				$("input[name='activityId']").removeAttr("checked");
				$("input[name='groupid']").each(function(i,a){
					var arr = groupIds.split(",");
					if(arr.indexOf(a.value) >= 0){					
						groupIds = groupIds.replace(a.value+",","");
					}
  				});
			}
			$("#groupIds").val(groupIds);			
		}
		// 反选
		function checkallReverse(obj) {
			$("input[name=groupid]").each(function(){
				$(this).attr("checked",!$(this).attr("checked"));
			});
			var ids = "";
			$("input[name=groupid]").each(function(i,a){
				var arr = ids.split(",");
				if($(this).attr("checked") == "checked"){
					if(arr.indexOf(a.value) < 0){
						ids = ids + a.value+",";
					}					
				} else {
					if(arr.indexOf(a.value) >= 0){
						ids = ids.replace(a.value+",","");
					}
				}
			});
			$("#groupIds").val(ids);
			t_allchk();
			choseActivity();
		}
		// 如变成全选，则需要勾选全选框
		function t_allchk() {
			var chknum = $("input[name='groupid']").size();
			var chk = 0;
			$("input[name=groupid]").each(function() {
				if ($(this).attr("checked") == "checked") {
					chk++;
				}
			});
			if (chknum == chk) {//全选 
				$("input[name='allChk']").attr("checked", true);
			} else {//不全选 
				$("input[name='allChk']").attr("checked", false);
			}
		}
		//验证groupid的值的改变来处理activity的选择事件
		function choseActivity() {
			$("input[name='groupid']").each(function() {
				idcheckchg($(this));
			});
		}
		//点击某一团期选择
		function idcheckchg(obj) {
			var arr = groupIds.split(",");
			if($(obj).attr("checked")) {
				if(arr.indexOf($(obj).val()) < 0){
					groupIds = groupIds+$(obj).val()+",";
				}
			} else {
				if ($("input[name='allChk']").attr("checked"))
					$("input[name='allChk']").removeAttr("checked");
				if (arr.indexOf($(obj).val()) >= 0) {					
					groupIds = groupIds.replace($(obj).val()+",","");
				}
			}
			$("#groupIds").val(groupIds);
			//判断所在tr=childxx中所有的input[name=groupid]是否都已经cheched
			var trChild = new Array();
			trChild = $(obj).parents("tr[id^=child]").find("input[name=groupid]");
			var checkedFlag = true;//表示都checked
			//遍历子项，如果任意一项未选中，则不选中对应产品选择框
			trChild.each(function(index){
				if($(this).attr("checked") == undefined || $(this).attr("checked") == false){
					checkedFlag = false;
				}
			});
			if(checkedFlag){
				$(obj).parents("tr[id^=child]").prev().find("input[name=activityId]").attr("checked",true);
			}else{
				$(obj).parents("tr[id^=child]").prev().find("input[name=activityId]").attr("checked",false);
			}
			t_allchk();
		}
		//点击某一团选择
		function acidcheckchg(obj, count) {
			var acId = $(obj).val();
			var grId = "";
			if($(obj).attr("checked")){
				$(obj).parents("tr[id=parent" + count + "]").next().find("input[name=groupid]").attr("checked", true);
			} else {
				$(obj).parents("tr[id=parent" + count + "]").next().find("input[name=groupid]").attr("checked", false);
			}
			t_allchk();
		}
		
	</script>
</head>
<body>
    <content tag="three_level_menu">
    	<shiro:hasPermission name="loose:book:order">
    		<li <c:if test="${showType==1}"> class='active' </c:if>><a href="${ctx}/activity/managerforOrder/list/1/2">预订</a></li>
    		
    		<c:choose>
    			<c:when test="${youjiaCompanyUuid==companyUuid}">
    				<!-- 如果是优加，则隐藏 -->
    			</c:when>
    			<c:otherwise>
    				<!-- 隐藏预报名
    				<li <c:if test="${showType==2}"> class='active' </c:if>><a href="${ctx}/activity/managerforOrder/list/2/2">预报名</a></li>
    				-->
    			</c:otherwise>
    		</c:choose>
    		
    	</shiro:hasPermission>
    </content>
    <!-- 签证公告展示 -->
	<%@ include file="/WEB-INF/views/modules/visa/visaPublicBulletinMarquee.jsp"%>
	<div class="xt-activitylist" style="display:none;">
		<c:if test="${activityKind == 2}">
	        <select name="agentSourceType" id="agentSourceTypeTemp" class="typeSelected" onchange="changeAgentSource(this);">
	        	<option value="1" selected="selected">非实时渠道</option>
	        	<option value="2">实时连通渠道</option>
	        </select>
	        <select name="quauqAgent" id="quauqAgentTemp" class="typeSelected">
	        	<c:forEach var="agentinfo" items="${quauqAgentinfoList }">
	                <option value="${agentinfo.id }" <c:if test="${agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName }</option>
	            </c:forEach>
	        </select>
	    </c:if>
	    <c:if test="${fns:getUser().userType ==3}">
	        <select name="agentId" id="agentIdSel" class="typeSelected" onchange="getSalerByAgentId(this);">
	        	<c:if test="${fns:getUser().company.id ne 68 }">
	            	<option value="-1" <c:if test="${agentId==-1}">selected="selected"</c:if>><c:choose><c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' }">未签</c:when><c:otherwise>非签约渠道</c:otherwise></c:choose></option>
	            </c:if>

	            <c:forEach var="agentinfo" items="${agentinfoList }">
	                <option value="${agentinfo.id }" <c:if test="${agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName }</option>
	            </c:forEach>
	        </select>
	    </c:if>
	</div>
	<%--<input id="ctx" type="hidden" value="${companyUuid}" />--%>
	<input id="ctx" type="hidden" value="${ctx}"/>
	<input id="companyUuid" type="hidden" value="${companyUuid}" />
	<input id="showType" name="showType" type="hidden" value="${showType}" />
	<input id="activityKind" name="activityKind" type="hidden" value="${activityKind}" />
	<input id="orderTypeStr" name="orderTypeStr" type="hidden" value="${orderTypeStr}" />
	<input id="quauqBookOrderPermission" name="quauqBookOrderPermission" type="hidden" value="${user.quauqBookOrderPermission }" />
	<input id="officeShelfRightsStatus" name="officeShelfRightsStatus" type="hidden" value="${officeShelfRightsStatus }" />
	<shiro:hasPermission name="${orderTypeStr}:book:addAgent">
		<input id="isAddAgent" name="isAddAgent" type="hidden" value="1" />
	</shiro:hasPermission>
	<!-- 补位权限 -->
	<c:set var="groupCoverFlag" value="0"></c:set>
	<shiro:hasPermission name="looseActivity:cover">
		<c:set var="groupCoverFlag" value="1"></c:set>
	</shiro:hasPermission>
	<input id="groupCoverFlag" name="groupCoverFlag" type="hidden" value="${groupCoverFlag}"/>
	
	<input id="groupIds" type="hidden" name="groupIds" value=""/>
	<div class="activitylist_bodyer_right_team_co_bgcolor" >
	
		<!-- 搜索查询 -->
		<%@ include file="/WEB-INF/views/modules/order/forOrder/form.jsp"%>
		<!-- 产品系列 -->
		<%@ include file="/WEB-INF/views/modules/order/forOrder/productType.jsp"%>
		<!-- 部门分区 -->
		<%@ include file="/WEB-INF/views/common/departmentDiv.jsp"%>
		<!-- 产品列表、团期列表 -->
		<%@ include file="/WEB-INF/views/modules/order/forOrder/swith.jsp"%>
		<!-- 排序 -->
		<%@ include file="/WEB-INF/views/modules/order/forOrder/orderByDiv.jsp"%>
		<c:choose>
			<c:when test="${productOrGroup == 'product'}">
				<!-- 产品列表 -->
				<%@ include file="/WEB-INF/views/modules/order/forOrder/looseProductForm.jsp"%>
			</c:when>
			<c:otherwise>
				<!-- 团期列表 -->	  <!-- 304 针对优加，调整团期列表 -->
				<c:if test="${companyUuid ne youjiaCompanyUuid}">
				    <%@ include file="/WEB-INF/views/modules/order/forOrder/looseGroupForm.jsp"%>
				</c:if>
				<c:if test="${companyUuid eq youjiaCompanyUuid}">
					<%@ include file="/WEB-INF/views/modules/order/forOrder/looseGroupFormForYoujia.jsp"%>
				</c:if>
			</c:otherwise>
		</c:choose>
		
	</div>
	<div class="page"></div>
	<div class="pagination">
		<dl>
			<dt><input name="allChk" type="checkbox" style="vertical-align: middle !important" onclick="checkall(this)"/>全选</dt>
			<dt><input name="allChkRev" type="checkbox" style="vertical-align: middle !important" onclick="checkallReverse(this)"/>反选</dt>
			<dd>
				<input type="button"class="btn ydbz_x" value="下载余位表" onclick="partsOfYWDownload2($('#groupIds').val())" style="width: auto;height: 28px;">
			</dd>
		</dl>
		<div class="pagination clearFix">${page}</div>
		<div style="clear:both;"></div>
	</div>
	
	<!-- 下载余位 -->
	<form id="exportForm" action="${ctx}/activity/managerforOrder/downloadYw" method="post">
		<input type="hidden" id="groupId" name="groupId">
	</form>
	
	<form id="partsForm" action="${ctx}/activity/managerforOrder/downloadYwChosed" method="post">
		<input type="hidden" name="paramGroupIds" id="paramGroupIds">
	</form>

</body>
</html>