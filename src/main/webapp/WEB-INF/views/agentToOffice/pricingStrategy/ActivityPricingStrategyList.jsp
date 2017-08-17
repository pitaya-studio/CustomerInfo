<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>渠道价格策略列表</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/jquery.number.js" type="text/javascript"></script>
	
	<!-- 需求223 -->
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<link type="text/css"  rel="stylesheet" href="${ctxStatic}/css/channelPricing.css" />
	<link type="text/css"  rel="stylesheet" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.min.css" /> <!--渠道定价css-->
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/quauq.css" />
    <!-- 522 需求新添加css -->
    <%-- <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/common.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/policySettingNew.css" /> --%>
    
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/quauq.base64.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.plus.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/boxScroll.js"></script>
    <script type="text/javascript" src="${ctxStatic}/modules/agentToOffice/channelPricing.js"></script>
	<!-- 需求223 -->
	<!-- 522需求添加js -->
	<%-- <script type="text/javascript" src="${ctxStatic}/modules/agentToOffice/policySetting.js"></script> --%>

    <!--[if lte IE 9]>
    <style>

        .withdraw-relative{
            width:inherit;
        }

    </style>
    <![endif]-->

    <style>
        .items-content {
            min-height:49px;
            line-height:49px;
        }
        .more-cities{
            box-sizing:border-box;
            -webkit-box-sizing: border-box;
            height: 99%;
            height: 96%\9;
        }
        .items-title{
            height: 51px;
            height: 50px\9;
        }
        .items-content{
            width: 90%;
        }
        .show-items{
            line-height: 12px;
        }
        .criteria-hide .items-title{

        }

    </style>

    <script type="text/javascript" src="${ctxStatic}/js/jquery.placeholder.min.js"></script>
    <script>
        $(function () {
            //input内添加placeholder属性
            $('#nameCode').placeholder();
        })
    </script>

</head>
<body>
		 <content tag="three_level_menu" id="threeLevelMenu">
		<%-- 	<shiro:hasPermission name="supplier:manager:view"> --%>
				<c:if test="${allActivity == null }"> <li id="allActivity" class="active current" ></c:if>
				<c:if test="${allActivity != null }"> <li id="allActivity" ></c:if>
				<a href="javascript:void(0)" onclick="threeLevelSearch(this);">所有产品(${allCount })</a></li>
			<%-- </shiro:hasPermission>
			<shiro:hasPermission name="supplier:manager:add"> --%>
			<c:if test="${allActivity != null }"> <li id="someActivity" class="active current" ></c:if>
				<c:if test="${allActivity == null }"> <li id="someActivity" ></c:if>
				<a href="javascript:void(0)" onclick="threeLevelSearch(this,true);">未设置(${someCount })</a></li>
			<%-- </shiro:hasPermission> --%>
		</content>
           <!--顶部tab页-->
          <!--   <ul class="nav-tabs">
                <li class="active"><a href="#">所有产品<span  class="pro_quantity">600</span></a></li>
                <li><a href="#">未设置产品<span class="pro_quantity">200</span></a></li>
            </ul> -->
            <!--搜索框开始-->
            <form id="queryList" method="post" action="${ctx }/pricingStrategy/manager/pricingStrategyList" style="display: none;" accept-charset="utf-8">
            	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
				<input id="allActivity" name="allActivity" type="hidden" value="${allActivity }">
				<div>
				<input type="hidden" name="nameCode" value="${activityName }"/>
            	<input type="hidden" name="contentWrap1" value="${contentWrap1 }"/><!-- 出发城市 -->
    			<input type="hidden" name="contentWrap2" value="${contentWrap2 }"/><!-- 线路 -->
    			<input type="hidden" name="travelType" value="${travelType }"/>
    			<input type="hidden" name="activityType" value="${activityType }"/>
    			<input type="hidden" name="activityLevel" value="${activityLevel }"/>
    			<input type="hidden" name="onlineState" value="${onlineState }"/>
    			<input type="hidden" name="orderByQuauqPrice" value="${orderByQuauqPrice }"/>
    			<input type="hidden" name="orderByIndustryPrice" value="${orderByIndustryPrice }"/>
    			<input type="hidden" name="orderByDate" value="${orderByDate }"/>
    			<input type="hidden" name="departureDate" value="${departureDate }"/>
    			<input type="hidden" name="startDate" value="${startDate }"/>
    			<input type="hidden" name="endDate" value="${endDate }"/></div>
            </form>
            <input type="hidden" id="ctx" value="${ctx }">
           <div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%"> 
            <div class="search-box">
                <div class="search-container">
                    <input id="nameCode" type="text" value="${activityName }" class="search-text" placeholder="请输入产品名称、团号" onblur="initNameCode(this)"/>
                    <div class="search-btn-container" onclick="searchSqActivityStrategy();">
                        <%--<em class="search-icon"></em>--%>
                            <input type="button" class="btn-primary search-btn-text" value="搜索" />
                            <%--<div class="search-btn-text" >搜索</div>--%>
                    </div>
                </div>
                <div class="clear-container">
                    <input type="button" class="clear-text" onclick="clearAllCon();" value="清空所有条件" />
                    <%--<span class="clear-text" onclick="clearAllCon();">清空所有条件</span>--%>
                </div>
            </div><!--搜索框结束-->
            <!--搜索条件开始-->
            <div class="search-criteria">
                <div class="criteria-show">
                    <c:if test="${fn:length(fromAreas) >= 20 }">
                    <div class="show-items item-wrap"  style="position:relative;">
                         <div class="more-cities"  onclick="showMoreCities(this,1);">
                            <span>更多</span>
                            <em class="criteria-btn-icon"></em>
                        </div></c:if>
                          <c:if test="${fn:length(fromAreas) < 20 }"><div class="show-items"> </c:if>
                        <div class="items-title" style="height:52px\9">出发城市</div>
                        <div class="items-content  content-back" id="fromArea">
                        <c:if test="${fn:length(fromAreas) >= 20 &&  contentWrap1 != null && contentWrap1 != ''}">
                            <div class="content-wrap expand-content"  id="contentWrap1"></c:if>
                             <c:if test="${fn:length(fromAreas) < 20 ||  contentWrap1 == null || contentWrap1 == ''}">
                            <div class="content-wrap"  id="contentWrap1"></c:if>
                                 <c:if test="${not empty fromAreas}">
                                 	<span class="content-item selected">全部</span>
	                     			<c:forEach items="${fromAreas }" var="fromArea">
	                                <span class="content-item">
	                                <input type="hidden" value="${fromArea.fromArea }">
	                               ${fromArea.label }</span></c:forEach>
                                </c:if>
                            </div>    
                        </div>
                    </div>
                       <c:if test="${fn:length(targetAreas) >= 20 }">
                    <div class="show-items item-wrap"  style="position:relative;">
                    <div class="more-cities"  onclick="showMoreCities(this,2);">
                           <%--  <c:if test="${fn:length(targetAreas) >= 20 && contentWrap2 != null && contentWrap2 != ''}">
                        	<span>收起</span>
                            <em class="criteria-btn-icon icon-up"></em></c:if> --%>
                        	<%-- <c:if test="${fn:length(targetAreas) < 20 || contentWrap2 == null || contentWrap2 ==''}"> --%>
                        	 <span>更多</span>
                            <em class="criteria-btn-icon"></em><%-- </c:if> --%>
                        </div></c:if>
                         <c:if test="${fn:length(targetAreas) < 20 }"><div class="show-items"> </c:if>
                        <span class="items-title">目的地</span>
                        <div class="items-content  content-back" id="targetArea">
                       <%--  <c:if test="${fn:length(targetAreas) >= 20 && contentWrap2 != null && contentWrap2 != ''}">
                        	<div class="content-wrap expand-content"  id="contentWrap2"></c:if> --%>
                        	  <%--  <c:if test="${fn:length(targetAreas) < 20 || contentWrap2 == null || contentWrap2 ==''}"> --%>
                        	<div class="content-wrap"  id="contentWrap2"><%-- </c:if> --%>
                             <c:if test="${not empty targetAreas}">
                             	<span class="content-item selected">全部</span>
	                     			<c:forEach items="${targetAreas }" var="targetArea">
                            <span class="content-item">
                            <input type="hidden" value="${targetArea.targetAreaId }">
                            ${targetArea.name }</span></c:forEach></c:if>
                            </div>
                        </div>
                    </div>
                    <div class="show-items">
                        <span class="items-title">出团日期</span>
                        <div class="items-content border-none" id="departureDate" id="groupOpenDate">
                            <span class="content-item selected" id="allDate">全部</span>
                            <span class="content-item"><input type="hidden" value="1">1月</span>
                            <span class="content-item"><input type="hidden" value="2">2月</span>
                            <span class="content-item"><input type="hidden" value="3">3月</span>
                            <span class="content-item"><input type="hidden" value="4">4月</span>
                            <span class="content-item"><input type="hidden" value="5">5月</span>
                            <span class="content-item"><input type="hidden" value="6">6月</span>
                            <span class="content-item"><input type="hidden" value="7">7月</span>
                            <span class="content-item"><input type="hidden" value="8">8月</span>
                            <span class="content-item"><input type="hidden" value="9">9月</span>
                            <span class="content-item"><input type="hidden" value="10">10月</span>
                            <span class="content-item"><input type="hidden" value="11">11月</span>
                            <span class="content-item"><input type="hidden" value="12">12月</span>
                            <input type="text" id="startDate" value="${startDate }" class="start-date  dateinput" onclick="WdatePicker();" onchange="changeDate(this,'startDate')"/>
                            至
                            <input type="text" id="endDate" value="${endDate }" class="start-date  dateinput" onclick="WdatePicker();" onchange="changeDate(this,'endDate')"/>
                        </div>
                    </div>
                </div>
                <c:choose>
				    <c:when test="${(travelType != null ||  activityType != null || activityLevel != null || onlineState != null)&&( travelType != '' ||  activityType != '' || activityLevel != '' || onlineState != '')}"> 
				           <div class="criteria-hide" style="display: block;">
				   </c:when>
				   <c:otherwise>
				       <div class="criteria-hide" >
				   </c:otherwise>
				</c:choose>
                    <div class="show-items">
                        <span class="items-title">旅游类型</span>
                        <div class="items-content" id="travelType">
                            <span class="content-item selected">全部</span>
                             <c:if test="${travelTypes != null }">
	                     			<c:forEach items="${travelTypes }" var="travelType">
                            <span class="content-item">
                            <input type="hidden" value=" ${travelType.traveltypeid }">
                           ${travelType.traveltypename }</span></c:forEach></c:if>
                            <span class="content-item">
                            <input type="hidden" value=" -1">
                          	无</span>
                        </div>
                    </div>
                    <div class="show-items">
                        <span class="items-title">产品类型</span>
                        <div class="items-content" id="activityType">
                            <span class="content-item selected">全部</span>
                            <c:if test="${activityTypes != null }">
	                     			<c:forEach items="${activityTypes }" var="activityType">
                            <span class="content-item">
                            <input type="hidden" value="${activityType.activitytypeid }">${activityType.activitytypename }</span>
                           </c:forEach></c:if>
                           <span class="content-item">
                            <input type="hidden" value=" -1">
                          	无</span>
                        </div>
                    </div>
                    <div class="show-items">
                        <span class="items-title">产品系列</span>
                        <div class="items-content"  style="border-bottom:none;" id="activityLevel">
                            <span class="content-item selected">全部</span>
                             <c:if test="${activityLevels != null }">
	                     			<c:forEach items="${activityLevels }" var="activityLevel">
                            <span class="content-item">
                            <input type="hidden" value="${activityLevel.activityLevelId }">${activityLevel.activityLevelName }</span>
                            </c:forEach></c:if>
                            <span class="content-item">
                            <input type="hidden" value=" -1">
                          	无</span>
                        </div>
                    </div>
                  <c:if test="${shelfRightsStatus eq 0 }">
                    <div class="show-items">
                        <span class="items-title">平台上架状态</span>
                        <div class="items-content"  style="border-bottom:none;" id="onlineState">
                            <span class="content-item selected">全部</span>
                             <c:if test="${groupOnlineStates != null }">
	                     			<c:forEach items="${groupOnlineStates }" var="groupOnlineState">
                            <span class="content-item">
                            <input type="hidden" value="${groupOnlineState.key }">${groupOnlineState.value }</span>
                            </c:forEach></c:if>
                        </div>
                    </div>
                    </c:if>
                </div>
                <c:choose>
				   <c:when test="${(travelType != null ||  activityType != null || activityLevel != null )&&( travelType != '' ||  activityType != '' || activityLevel != '')}"> 
				         <div style="position:relative;">
                    <div class="criteria-btn" onclick="showQueryMore(this);">
                        <span>收起条件</span>
                        <em class="criteria-btn-icon icon-up"></em>
                    </div>
                </div> 
				   </c:when>
				   <c:otherwise>
				    <div style="position:relative;">
                    <div class="criteria-btn"  onclick="showQueryMore(this);">
                        <span>更多条件</span>
                        <em class="criteria-btn-icon"></em>
                    </div>
                </div>
				   </c:otherwise>
				</c:choose>
            </div><!--搜索条件结束-->
            <!--搜索按钮开始-->
           <!--  <div class="search-button">
                <button class="screening-opera" onclick="searchSqActivityStrategy();">开始筛选</button>
            </div> --><!--搜索按钮结束-->
            <!--表头排序开始-->
            <div class="table-sort">
                <span class="sort-items  no-select"><input id="checkAll" type="checkbox" class="all-check-icon"  onclick="checkAll(this);" />全选</span>
                <span class="sort-items" onclick="checkReverse();">反选</span>
                <input type="button" class="sort-items batch-setting" onclick="setBatchPricingStrategy();" value="批量设置策略"/>
                <div class="sort-page align-right" style="margin-right:10px;">
                <c:if test="${page.pageNo == 1 }"><em class="page-icon-left"></em></c:if>
                <c:if test="${page.pageNo != 1}"><em class="page-icon-leftFirst" onclick="javascript:page(${page.prev },${page.pageSize });"></em></c:if>
                <span id="current-page">${currentPage }</span>/<span id="all-pages">${totalPage }</span>
                <c:if test="${page.pageNo == totalPage}"><em class="page-icon-rightLast"></em></c:if>
                <c:if test="${page.pageNo != totalPage }"><em class="page-icon-right" onclick="javascript:page(${page.next },${page.pageSize });"></em></c:if>
                </div>
               <%-- <div class="sort-page align-right"  style="margin-right:10px;"><em class="page-icon-left"></em><span id="current-page">${page.pageNo }</span> /<span id="all-pages">${(page.count%page.pageSize==0)?(page.count/page.pageSize):(page.count/page.pageSize+1) }</span> <em class="page-icon-right"></em></div>--%>
                <span class="sort-items align-right no-select">查询结果<strong  style="margin:0 5px;">${page.count }</strong>条</span>

                <%-- T2 改版 ymx 2017/3/10 添加了not_choose 在没有选中的时候的span样式 Start--%>
                <span onclick="sortTable(this,'orderByQuauqPrice');"  class="sort-items align-right not_choose"  style="margin-right:25px;">QUAUQ价
                	<c:if test="${orderByQuauqPrice =='desc' }"><em id="orderByQuauqPrice" class="sort-icon sort-icon-down"></em></c:if>
                 	<c:if test="${orderByQuauqPrice =='asc' }"><em id="orderByQuauqPrice" class="sort-icon sort-icon-up"></em></c:if>
                  	<c:if test="${orderByQuauqPrice ==null || orderByQuauqPrice == '' }"><em id="orderByQuauqPrice" class="sort-icon"></em></c:if>
                </span>
                <span onclick="sortTable(this,'orderByIndustryPrice');"  class="sort-items align-right not_choose">同行价
                	<c:if test="${orderByIndustryPrice =='desc' }"><em id="orderByIndustryPrice" class="sort-icon sort-icon-down"></em></c:if>
                 	<c:if test="${orderByIndustryPrice =='asc' }"><em id="orderByIndustryPrice" class="sort-icon sort-icon-up"></em></c:if>
                  	<c:if test="${orderByIndustryPrice ==null || orderByIndustryPrice == '' }"><em id="orderByIndustryPrice" class="sort-icon"></em></c:if></span>
                <span onclick="sortTable(this,'orderByDate');"  class="sort-items align-right not_choose">出团日期
                	<c:if test="${orderByDate =='desc' }"><em id="orderByDate" class="sort-icon sort-icon-down"></em></c:if>
                 	<c:if test="${orderByDate =='asc' }"><em id="orderByDate" class="sort-icon sort-icon-up"></em></c:if>
                  	<c:if test="${orderByDate ==null || orderByDate == '' }"><em id="orderByDate" class="sort-icon"></em></c:if>
                </span>
                <%-- T2 改版 ymx 2017/3/10 添加了not_choose 在没有选中的时候的span样式 End--%>

                 <%--  <div class="shelf-status">
                    <span>全部状态</span><i class="fa fa-angle-down" aria-hidden="true"></i>
                    <ul style="display: none;">
                        <li value="0">全部状态</li>
                        <c:forEach items="${groupOnlineStates }" var="groupOnlineState">
                        <li value="${groupOnlineState.key }">${groupOnlineState.value }</li>
                        </c:forEach>
                    </ul>
                </div> --%>
            </div><!--表头排序结束-->
            <!--列表开始-->
            <div class="table-lists">
                <table class="activitylist_bodyer_table mainTable"  style="width:100%;"  id="ProTab">
                    <thead>
                        <tr class="table-lists-header">
                            <th style="min-width:70px;width:7%;"  class="th-first">序号</th>
                            <th style="min-width:160px;width:21%;">产品名称</th>
                            <th style="min-width:130px;width:10%;">团号</th>
                            <th style="min-width:100px;width:9%;">出团日期</th>
                            <th style="min-width:180px;width:15%;">同行价</th>
                            <th style="min-width:180px;width:15%;">定价策略</th>
                            <th style="min-width:180px;width:15%;">QUAUQ价</th>
                            <th style="min-width:100px;width:8%;"   class="td-last">操作</th>
                        </tr>
                    </thead>
                    <tbody id="wantSet">
                      <c:if test="${page.list != null }">
	                     			<c:forEach items="${page.list }" var="activityGroup" varStatus="status">
                        <tr>
                            <c:choose>  
                            	<c:when test="${activityGroup.isT1 == 1 && shelfRightsStatus eq 0}">
                            		<td class="th-first withdraw-relative">
                            		<em class="grounding-hover grounding-one" style="display: none;">已上架旅游交易系统</em>
									<em class="g-w grounding" style="background-image: url(&quot;${ctxStatic}/images/g-w.png&quot;);"></em>
                            	</c:when>
                            	<c:when test="${activityGroup.isT1 == 0 && activityGroup.isT12 ne '' && shelfRightsStatus eq 0}">
	                            	 <td class="th-first withdraw-relative">
		                            	 <em class="withdraw-hover grounding-one" style="display: none;">已下架旅游交易系统</em>
										<em class="g-w withdraw" style="background-image: url(&quot;${ctxStatic}/images/g-w.png&quot;);"></em>
                            	</c:when>
                            	<c:otherwise> <td  class="th-first"></c:otherwise>
                            </c:choose>
                            <input value="${activityGroup.id}_${ activityGroup.srcActivityId}_${activityGroup.alertFlag }"  type="checkbox" class="all-check-icon" onclick="changeColor(this);"/>${status.index+1 }</td>
                            <td><div class="pro-name">${activityGroup.acitivityName }</div></td>
                            <td><div class="pro-name">${activityGroup.groupCode }</div></td>
                            <td><div class="pro-name">${activityGroup.groupOpenDate }</div></td>
                            <td>
                                <div class="pro-name">
                                    <div>
                                        <span class="lists-title">成人:</span>${activityGroup.settlementAdultPrice }
                                    </div>
                                    <div>
                                        <span class="lists-title">儿童:</span>${activityGroup.settlementcChildPrice }
                                    </div>
                                    <div>
                                        <span class="lists-title">特殊人群:</span>${activityGroup.settlementSpecialPrice }
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="pro-name">
                                    <div>
                                        <span class="lists-title">成人:</span>
                                        <c:set value="${ fn:split(activityGroup.adultPricingStrategy, ',') }" var="adultPricingStrategys" />
                                        <c:forEach items="${adultPricingStrategys}" var="adultPricingStrategy" varStatus="status">
                                        	<c:if test="${status.index eq 0}">
                                        		${adultPricingStrategy eq ''?'-':adultPricingStrategy}
                                        	</c:if>
                                        	<c:if test="${status.index ne 0}">
                                        		<br/><span class="lists-title"></span>&nbsp;${adultPricingStrategy eq ''?'-':adultPricingStrategy}
                                        	</c:if>
										</c:forEach>
                                    </div>
                                    <div>
                                        <span class="lists-title">儿童:</span>
                                        <c:set value="${ fn:split(activityGroup.childrenPricingStrategy, ',') }" var="childrenPricingStrategys" />
                                        <c:forEach items="${childrenPricingStrategys}" var="childrenPricingStrategy" varStatus="status">
                                        	<c:if test="${status.index eq 0}">
                                        		${childrenPricingStrategy eq ''?'-':childrenPricingStrategy}
                                        	</c:if>
                                        	<c:if test="${status.index ne 0}">
                                        		<br/><span class="lists-title"></span>&nbsp;${childrenPricingStrategy eq ''?'-':childrenPricingStrategy}
                                        	</c:if>
										</c:forEach>
                                    </div>
                                    <div>
                                        <span class="lists-title">特殊人群:</span>
                                        <c:set value="${ fn:split(activityGroup.specialPricingStrategy, ',') }" var="specialPricingStrategys" />
                                        <c:forEach items="${specialPricingStrategys}" var="specialPricingStrategy" varStatus="status">
                                        	<c:if test="${status.index eq 0}">
                                        		${specialPricingStrategy eq ''?'-':specialPricingStrategy}
                                        	</c:if>
                                        	<c:if test="${status.index ne 0}">
                                        		<br/><span class="lists-title"></span>&nbsp;${specialPricingStrategy eq ''?'-':specialPricingStrategy}
                                        	</c:if>
										</c:forEach>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="pro-name">
                                    <div>
                                        <span class="lists-title">成人:</span>${activityGroup.quauqAdultPrice }
                                    </div>
                                    <div>
                                        <span class="lists-title">儿童:</span>${activityGroup.quauqChildPrice }
                                    </div>
                                    <div>
                                        <span class="lists-title">特殊人群:</span>${activityGroup.quauqSpecialPrice }
                                    </div>
                                </div>
                            </td>
                            <td  class="td-last">
                                <span class="opera-text" onclick="setPricingStrategy(this,${activityGroup.id},${ activityGroup.srcActivityId},'{${activityGroup.acitivityName}}',${activityGroup.alertFlag })">设置策略
	                                <c:if test="${activityGroup.hasEyelessAgents eq 1}">
	                                <i class="fa fa-exclamation-circle remind-icon color_red" aria-hidden="true"></i>
                                   <!--  <div class="remind-container first-remind" style="display: none;">
                                        <em class="remind-triangle"></em>
                                        <div class="remind-info">温馨提示：目前部分门店无法查看该产品。</div>
                                    </div> -->
                                    </c:if>
                                 </span>
                                <br>
                                <span class="opera-text" onclick="changeList(${activityGroup.id},${ activityGroup.srcActivityId})">变更记录
                                <c:choose>
                                	<c:when test="${fns:hasChanged(activityGroup.id,activityGroup.srcActivityId) == 1}">
                                		<i onmouseover="showRemind(this)" onmouseout="hideRemind(this)" class="fa fa-exclamation-circle remind-icon" aria-hidden="true"></i>
	                                    <div class="remind-container first-remind" style="display: none;">
	                                        <em class="remind-triangle"></em>
	                                        <div class="remind-info">同行价和直客价有新调整需要重设置策略</div>
	                                    </div>
                                	</c:when>
                                	<c:when test="${fns:hasChanged(activityGroup.id,activityGroup.srcActivityId) == 2}">
                                		<i onmouseover="showRemind(this)" onmouseout="hideRemind(this)" class="fa fa-exclamation-circle remind-icon" aria-hidden="true"></i>
	                                    <div class="remind-container first-remind" style="display: none;">
	                                        <em class="remind-triangle"></em>
	                                        <div class="remind-info">同行价有新调整需要重设置策略</div>
	                                    </div>
                                	</c:when>
                                	<c:when test="${fns:hasChanged(activityGroup.id,activityGroup.srcActivityId) == 3}"> 
                                		<i onmouseover="showRemind(this)" onmouseout="hideRemind(this)" class="fa fa-exclamation-circle remind-icon" aria-hidden="true"></i>
	                                    <div class="remind-container first-remind" style="display: none;">
	                                        <em class="remind-triangle"></em>
	                                        <div class="remind-info">直客价有新调整需要重设置策略</div>
	                                    </div>
                                	</c:when>
                                	<c:otherwise></c:otherwise>
                                </c:choose>
                                </span>
                            </td>
                        </tr>
                        <%-- <tr class="warniniglx tr_child height_30" style="height: 20px;"><td colspan="8" class="tr paddingR"> <i class="fa fa-exclamation-circle reminder-i color_red" aria-hidden="true"></i> 提示：目前有<i class="count color_red">30</i>家门店无法看到该产品。<button onclick="setPlolcy(0.1,this,${activityGroup.id},${ activityGroup.srcActivityId},'${activityGroup.acitivityName }',${activityGroup.alertFlag })">设置策略</button></td></tr> --%>
                        </c:forEach></c:if>
                    </tbody>
                </table>
            </div><!--列表结束-->
            <!--分页开始-->
	<div class="pagination clearFix" style="margin-right: 35px;">
		${page}
	</div>
   </div><!--分页结束-->
   
</body>
</html>