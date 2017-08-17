<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>渠道价格策略列表</title>
	<meta name="decorator" content="wholesaler"/>
	<style type="text/css">
	.sort{color:#0663A2;cursor:pointer;}
	
	/*0071需求样式 */
    label.myerror {
    color: #ea5200;
    font-weight: bold;
    margin-left: 0px;
    padding-bottom: 2px;
    padding-left: 0px;
}
	</style>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/jquery.number.js" type="text/javascript"></script>
	
	<style type="text/css">
	
        #contentTable th {
            height: 40px;
            border-top: 1px solid #CCC;
            }
        #teamTable{
            border:1px solid #CCC;
            }
            .groupNoteTipImg {
                display: inline-block;
                width: 12px;
                height: 12px;
                background-image: url("${ctxStatic}/images/order_s3.png");
                background-repeat: no-repeat;
                background-position: 0px center;
                margin: 4px 0px 0px 5px;
                line-height: 8px;
                vertical-align: top;
            }
	</style>
	<!-- 需求223 -->
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/quauq.css" />
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/quauq.base64.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.plus.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/boxScroll.js"></script>
    <script type="text/javascript" src="${ctxStatic}/modules/agentToOffice/pricingStrategyList.js"></script>
	<!-- 需求223 -->
</head>
<body>
     <!--右侧内容部分开始-->
     <form id="searchForm"  action="${ctx}/pricingStrategy/manager/list" method="post">
     	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
         <div style="display: block;" class="ydxbd">
             <div class="activitylist_bodyer_right_team_co1" style="width:206px;">
                 <label class="activitylist_team_co3_text" style="width:90px;">出发城市：</label>
                 <select name="fromArea" id="fromArea">
                     <option value="" selected="">全部</option>
                     <c:if test="${fromAreas != null }">
                     	<c:forEach items="${fromAreas }" var="beginCity">
                     		<option value="${beginCity.key }" ${searchParm.fromArea==beginCity.key?"selected='selected'":"" } >${beginCity.value }</option>
                     	</c:forEach>
                     </c:if>
                 </select>
             </div>
             <div class="activitylist_bodyer_right_team_co1" style="width:206px;">
                 <div class="activitylist_team_co3_text" style="width:90px;">线路：</div>
                 <select name="targetArea" id="targetArea">
                     <option value="" selected="">全部</option>
                    <c:if test="${targetAreaIds != null }">
                     	<c:forEach items="${targetAreaIds }" var="targetArea">
                     		<option value="${targetArea.id }" ${searchParm.targetArea==targetArea.id?"selected='selected'":"" }>${targetArea.lineName }</option>
                     	</c:forEach>
                     </c:if>
                 </select>
             </div>
             <div class="activitylist_bodyer_right_team_co1" style="width:206px;">
                 <div class="activitylist_team_co3_text" style="width:90px;">渠道类型：</div>
                 <select name="agentType" id="agentType">
                     <option value="" selected="">全部</option>
                      <c:if test="${agentTypes != null }">
                     	<c:forEach items="${agentTypes }" var="agentType">
                     		<option value="${agentType.id }"  ${searchParm.agentType==agentType.id?"selected='selected'":"" }>${agentType.label }</option>
                     	</c:forEach>
                     </c:if>
                 </select>
             </div>
             <div class="activitylist_bodyer_right_team_co1" style="width:206px;">
                 <div class="activitylist_team_co3_text" style="width:90px;">渠道等级：</div>
                 <select name="agentLevel" id="agentLevel">
                     <option value="" selected="">全部</option>
                     <c:if test="${agentLevels != null }">
                     	<c:forEach items="${agentLevels }" var="agentLevel">
                     		<option value="${agentLevel.id }"  ${searchParm.agentLevel==agentLevel.id?"selected='selected'":"" }>${agentLevel.label }</option>
                     	</c:forEach>
                     </c:if>
                 </select>
             </div>
             <div class="activitylist_bodyer_right_team_co1" style="width:206px;">
                 <input value="确定" id="seachbutton" class="btn btn-primary ydbz_x" type="submit"> <input
                     value="条件重置" onclick="resetSearchParams()" class="btn btn-primary ydbz_x" type="button">
             </div>
             <div class="kong"></div>
             <div class="activitylist_bodyer_right_team_co1" style="width:206px;">
                 <label class="activitylist_team_co3_text" style="width:90px;">旅游类型：</label>
                 <select name="travelType" id="travelType">
                     <option value="" selected="">全部</option>
                     <option value="-1" >空</option>
                      <c:if test="${travelTypes != null }">
                     	<c:forEach items="${travelTypes }" var="travelType">
                     		<option value="${travelType.key }" ${searchParm.travelType==travelType.key?"selected='selected'":"" }>${travelType.value }</option>
                     	</c:forEach>
                     </c:if>
                 </select>
             </div>
             <div class="activitylist_bodyer_right_team_co1" style="width:206px;">
                 <div class="activitylist_team_co3_text" style="width:90px;">产品类型：</div>
                 <select name="productType" id="productType">
                     <option value="" selected="">全部</option>
                     <option value="-1" >空</option>
                     <c:if test="${productTypes != null }">
                     	<c:forEach items="${productTypes }" var="productType">
                     		<option value="${productType.key }" ${searchParm.productType==productType.key?"selected='selected'":"" }>${productType.value }</option>
                     	</c:forEach>
                     </c:if>
                 </select>
             </div>
             <div class="activitylist_bodyer_right_team_co1" style="width:206px;">
                 <div class="activitylist_team_co3_text" style="width:90px;">产品系列：</div>
                 <select name="productLevel" id="productLevel">
                     <option value="" selected="">全部</option>
                     <option value="-1" >空</option>
                      <c:if test="${productLevels != null }">
                     	<c:forEach items="${productLevels }" var="productLevel">
                     		<option value="${productLevel.key }">${productLevel.value }</option>
                     	</c:forEach>
                     </c:if>
                 </select>
             </div>
             <div class="activitylist_bodyer_right_team_co1" style="width:206px;">
                 <div class="activitylist_team_co3_text" style="width:90px;">优惠类型：</div>
                 <select name="favorableType" id="favorableType">
                     <option value="" selected="">全部</option>
                     <!-- <option value="1" >加价</option> -->
                     <option value="2" >直减</option>
                     <option value="3" >折扣</option>
                    <!--  <option value="4" >提价</option> -->
                 </select>
             </div>
             <div class="kong"></div>
         </div>
     </form>

     <!-- 产品线路分区 -->
   <shiro:hasPermission name="price:add"> 
     <div style="height:60px">
         <p class="main-right-topbutt">
             <a href="${ctx}/pricingStrategy/manager/addt">新增策略</a>
         </p>
     </div>
    </shiro:hasPermission> 
     <table class="activitylist_bodyer_table" id="contentTable_quauq">
         <thead>
         <tr>
             <th width="4%">状态</th>
             <th width="4%">序号</th>
             <th width="8%">出发城市</th>
             <th width="8%">线路</th>
             <th width="8%">旅游类型</th>
             <th width="8%">产品类型</th>
             <th width="8%">产品系列</th>
             <th width="8%">渠道类型</th>
             <th width="8%">渠道等级</th>
             <th width="16%">优惠</th>
             <th width="8%">操作</th>
             <!-- 如果是非签约渠道，则没有操作项 -->

         </tr>
         </thead>
         <tbody class="orderOrGroup_group_tbody">
         <c:forEach items="${page.list }" var="priceStrategy" varStatus="status">
         <tr rowspan="${fn:length(priceStrategy.agentPriceStrategySet)}" id="index${status.index }">
             <td class="tc" rowspan="${fn:length(priceStrategy.agentPriceStrategySet)}" >
             	<c:if test="${priceStrategy.state == 1 }">
                 <input id="updateState${status.index }" type="checkbox" checked="checked" onclick="updateState(${priceStrategy.id},'${ctx}/pricingStrategy/manager/','禁用','updateState${status.index }')"/><font>启用</font>
                 </c:if>
                 <c:if test="${priceStrategy.state == 2 }">
                 	 <input id="updateState${status.index }" type="checkbox"  onclick="updateState(${priceStrategy.id},'${ctx}/pricingStrategy/manager/','启用','updateState${status.index }')"/><font>启用</font>
                 </c:if>
             </td>
             <td class="tc" rowspan="${fn:length(priceStrategy.agentPriceStrategySet)}" >${status.index+1 }</td>
             <td class="tc" rowspan="${fn:length(priceStrategy.agentPriceStrategySet)}" >${priceStrategy.fromAreaNames }</td>
             <td class="tc" rowspan="${fn:length(priceStrategy.agentPriceStrategySet)}" >${priceStrategy.targetAreaNames }</td>
             <td class="tc" rowspan="${fn:length(priceStrategy.agentPriceStrategySet)}" >${priceStrategy.travelTypeNames }</td>
             <td class="tc" rowspan="${fn:length(priceStrategy.agentPriceStrategySet)}" >${priceStrategy.activityTypeIdNames }</td>
             <td class="tc" rowspan="${fn:length(priceStrategy.agentPriceStrategySet)}" >${priceStrategy.productLevelNames }</td>
             <c:forEach items="${priceStrategy.agentPriceStrategySet }" var="agentPrice" varStatus="secondStatus">
             <c:if test="${0 ==  secondStatus.index}">
             <td class="tc">${agentPrice.agentTypeNames }</td>
             <td class="tc">${agentPrice.agentLevelNames }</td>
             <td class="tc">${agentPrice.priceStrategyDesc }</td>
             <td class="tc" rowspan="${fn:length(priceStrategy.agentPriceStrategySet)}">
             	<shiro:hasPermission name="price:update">  
                 	<a href="${ctx}/pricingStrategy/manager/edit?priceStrategyId=${priceStrategy.id}">修改</a>
                  </shiro:hasPermission>  
                 <a href="javascript:void(0)" onclick="deletePriceStrate('index${status.index }',${priceStrategy.id},'${ctx}/pricingStrategy/manager/','${priceStrategy.state }')">删除</a>
             </td>
             </tr></c:if>
             <c:if test="${0 !=  secondStatus.index}"><tr>
              <td class="tc">${agentPrice.agentTypeNames }</td>
             <td class="tc">${agentPrice.agentLevelNames }</td>
             <td class="tc">${agentPrice.priceStrategyDesc }</td>
             </tr></c:if>
             </c:forEach>
         </c:forEach>
         </tbody>
     </table>
     <!--quauq渠道列表-->
     <div class="page">
			<div class="pagination">
			<div class="endPage">${page}</div>
			<div style="clear:both;"></div>
			</div>	
		</div>
     <!--右侧内容部分结束-->
</body>
</html>