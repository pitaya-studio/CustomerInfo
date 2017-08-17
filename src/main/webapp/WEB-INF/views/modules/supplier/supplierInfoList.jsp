<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>地接社管理-列表</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<!--[if lte IE 6]>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/bootstrap-ie6.min.css" />
	<script type="text/javascript" src="${ctxStatic}/js/bootstrap-ie.min.js"></script>
	<![endif]-->
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
	
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery/jquery-migrate-1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/2.3/jquery.jBox-2.3.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
	
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript">
		$(function(){
			// 国家地区联动下拉框
			$("span.countryCityAddress select").change(function(){
				var parentId = $(this).val();
				var url = "${ctx}"+"/supplier/getAreaById/"+parentId;
				var currentClass = $(this).attr('class');
				if(parentId.length>0){
					$.ajax({
							type: "POST",
							url: url,
							dataType:"json",
							success: function(msg){
								if(currentClass=="country"){
									$("span.countryCityAddress .area").empty();
									$("span.countryCityAddress .area").append("<option value=''>请选择</option>");
									$.each(msg,function(i, n){
										var optionStr = "<option value='"+n.id+"'>"+n.name+"</option>";
										$("span.countryCityAddress .area").append(optionStr);
									});
								}
							}
						});
				}else{
			     	if(currentClass=="country"){
		 		  		$("span.countryCityAddress .area").empty();
		 		  		$("span.countryCityAddress .area").append("<option value=''>请选择</option>");
			     	}
				}
			});
			//展开筛选按钮
			launch();
			//操作浮框
			operateHandler();
			//产品名称文本框提示信息
			inputTips();
			//如果展开部分有查询条件的话，默认展开，否则收起	
			var searchFormInput1 = $("#searchForm").find("input[id='minCreateDate']");
			var searchFormInput2 = $("#searchForm").find("input[id='maxCreateDate']");
			var searchFormselect = $("#searchForm").find("select");
			var inputRequest1 = false;
			var inputRequest2 = false;
			var selectRequest = false;
			for(var i = 0; i<searchFormInput1.length; i++) {
				if($(searchFormInput1[i]).val() != "" && $(searchFormInput1[i]).val() != null) {
					inputRequest1 = true;
				}
			}
			for(var i = 0; i<searchFormInput2.length; i++) {
				if($(searchFormInput2[i]).val() != "" && $(searchFormInput2[i]).val() != null) {
					inputRequest2 = true;
				}
			}
			for(var i = 0; i<searchFormselect.length; i++) {
				if($(searchFormselect[i]).children("option:selected").val() != "" && 
						$(searchFormselect[i]).children("option:selected").val() != null) {
					selectRequest = true;
				}
			}
			if(inputRequest1||inputRequest2||selectRequest) {
				$('.zksx').click();
			}
		});

		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/supplier/supplierInfoList");
			$("#searchForm").submit();
	    	return false;
	    }

	    function strategydetail(supplierid){
	    	window.open("${ctx}/supplier/supplierDetail?supplierId="+supplierid);
	    }
	    function suppliermod(supplierid){
	    	window.location.href = "${ctx}/supplier/supplierFirstForm?supplierId="+supplierid;
	    }
		
		//排序
		function sort(element){

			var _this = $(element);

			//按钮高亮
			_this.parent("li").attr("class","activitylist_paixu_moren");

			//原先高亮的同级元素置灰
			_this.parent("li").siblings(".activitylist_paixu_moren").attr("class","activitylist_paixu_left_biankuang");

			//高亮按钮隐藏input赋值
			_this.next().val("activitylist_paixu_moren");

			//原先高亮按钮隐藏input值清空
			_this.parent("li").siblings(".activitylist_paixu_moren").children("a").next().val("");

			var sortFlag = _this.children().attr("class");
			//降序
			if(sortFlag == "icon icon-arrow-up"){

				//改变箭头的方向
				_this.children().attr("class","icon icon-arrow-down");

				//降序
				_this.prev().val("desc");
			}
			//降序
			else if(sortFlag == "icon icon-arrow-down"){
				//改变箭头方向
				_this.children().attr("class","icon icon-arrow-up");

				//shengx
				_this.prev().val("asc");
			}

			$("#searchForm").submit();

			return false;
		}

	</script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
</head>
<body>
	<page:applyDecorator name="supplier_op_head">
		<page:param name="current">supplierList</page:param>
	</page:applyDecorator>
	<form:form id="searchForm" modelAttribute="supplierinfo" action="${ctx}/supplier/supplierInfoList" method="post" class="form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
            	<!--右侧内容部分开始-->

                    <div class="activitylist_bodyer_right_team_co">
					<div class="activitylist_bodyer_right_team_co2 pr">
							<input class="inputTxt searchInput inputTxtlong" name="supplierName" value="${conditionsMap.supplierName}" placeholder="输入地接社名称">
						</div>
					<a class="zksx">筛选</a>
					<div class="form_submit">
						 <input name="search" type="submit" value="搜索"  class="btn btn-primary ydbz_x">
					</div>
					<p class="main-right-topbutt"><a href="${ctx}/supplier/supplierFirstForm">新增地接社</a></p>
					<div class="ydxbd" style="display: none;">
						<span></span>
						<div class="activitylist_bodyer_right_team_co">
							<div class="activitylist_bodyer_right_team_co1">
								<label class="activitylist_team_co3_text">创建时间：</label>
								<input readonly="" id="minCreateDate" name="minCreateDate" value="${conditionsMap.minCreateDate}" onclick="WdatePicker()" class="inputTxt dateinput">
								<span>至 </span>
								<input readonly="" id="maxCreateDate" name="maxCreateDate" value="${conditionsMap.maxCreateDate}" onclick="WdatePicker()" class="inputTxt dateinput">
							</div>
							<div class="activitylist_bodyer_right_team_co1">
								<label class="activitylist_team_co3_text">地接社类别：</label>
								<div class="selectStyle">
									<select name="supplierType">
										<option value="">请选择</option>
										<c:choose>
											<c:when test="${companyId != '68' }">
												<!-- mod by jiangyang 把函数由从字典取值改成了从视图里取值，原函数名称getDictList -->
												<c:forEach items="${fns:getSysCompanyDictViewListByCmpId('travel_agency_type', companyId)}" var="suptypeinfo">
												<!-- 地接社类型 -->
													<c:if test="${suptypeinfo.label !='餐厅' }">
														<option value="${suptypeinfo.value}" <c:if test="${conditionsMap.supplierType==suptypeinfo.value}">selected="selected"</c:if>>${suptypeinfo.label}</option>
													</c:if>
												</c:forEach>
											</c:when>
										<c:otherwise>
											<c:forEach items="${fns:getSysCompanyDictViewListByCmpId('travel_agency_type', companyId)}" var="suptypeinfo">
											<!-- 地接社类型 -->
												<c:if test="${suptypeinfo.label !='散拼' && suptypeinfo.label !='签证' && suptypeinfo.label !='领队'}">
													<option value="${suptypeinfo.value}" <c:if test="${conditionsMap.supplierType==suptypeinfo.value}">selected="selected"</c:if>>${suptypeinfo.label}</option>
												</c:if>
											</c:forEach>
										</c:otherwise>
										</c:choose>
									</select>
								</div>
							</div>
							<span class="sysselect_s countryCityAddress">
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">国家：</label>
									<div class="selectStyle">
										<select class="country" id="country" name="country">
											<option value="">请选择</option>
											<c:forEach items="${areaMap}" var="aa">
												<option value="${aa.key}" <c:if test="${aa.key == conditionsMap.country}">selected="selected"</c:if>>${aa.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">地区：</label>
									<div class="selectStyle">
										<select class="area" id="area" name="area">
											<option value="">请选择</option>
											<c:forEach items="${conditionsMap.areaMap}" var="aa">
												<option value="${aa.key}" <c:if test="${aa.key == conditionsMap.area}">selected="selected"</c:if>>${aa.value}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</span>
							<!-- <div class="kong"></div>
							<div class="activitylist_bodyer_right_team_co2">
								<label class="activitylist_team_co3_text">营业额：</label><input id="" class="rmb inputTxt" name="" value="" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
								<span>至 </span>
								<input id="" class="rmb inputTxt" name="" value="" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
							</div> -->
						</div>
					</div>
					</div>
                    <!-- 产品线路分区 -->
					<div class="supplierLine">
						<a class="select" onclick="">国内</a>
						<!--先不做国外 <a href="javascript:void(0)" onclick="">国外</a> -->
					</div>
                    <div class="activitylist_bodyer_right_team_co_paixu">
                        <div class="activitylist_paixu">
                            <div class="activitylist_paixu_left">
                                <ul>
                                    <c:choose>
				                    <c:when test="${conditionsMap.orderCreateDateCss == 'activitylist_paixu_moren' }">
				                    <li class="activitylist_paixu_moren">
				                    </c:when>
				                    <c:otherwise>
				                    <li class="activitylist_paixu_left_biankuang">
				                    </c:otherwise>
				                    </c:choose>
				                    	<input type="hidden" value="" name="orderCreateDateSort">
				                    	<a onclick="sort(this)">创建日期
				                    		<c:choose>
				                    			<c:when test="${conditionsMap.orderCreateDateSort == 'desc' }">
				                    			<i class="icon icon-arrow-down"></i>
				                    			</c:when>
				                    			<c:otherwise>
				                    			<i class="icon icon-arrow-up"></i>
				                    			</c:otherwise>
				                    		</c:choose> 
				                    	</a>
				                    	<input type="hidden" value="" name="orderCreateDateCss">
				                    </li>
				                    
				                    <c:choose>
				                    <c:when test="${conditionsMap.orderUpdateDateCss == 'activitylist_paixu_moren' }">
				                    <li class="activitylist_paixu_moren">
				                    </c:when>
				                    <c:otherwise>
				                    <li class="activitylist_paixu_left_biankuang">
				                    </c:otherwise>
				                    </c:choose>
				                    	<input type="hidden" value="" name="orderUpdateDateSort">
				                    	<a onclick="sort(this)">更新日期
				                    		<c:choose>
				                    			<c:when test="${conditionsMap.orderUpdateDateSort == 'desc' }">
				                    			<i class="icon icon-arrow-down"></i>
				                    			</c:when>
				                    			<c:otherwise>
				                    			<i class="icon icon-arrow-up"></i>
				                    			</c:otherwise>
				                    		</c:choose>
				                    	</a>
				                    	<input type="hidden" value="" name="orderUpdateDateCss">
				                    </li>
                                    <!-- <li class="activitylist_paixu_left_biankuang orderCreateDateSort"><a onclick="sortby('ordercreateDateSort',this)">创建时间</a></li>
                        			<li class="activitylist_paixu_left_biankuang orderUpdateDateSort"><a onclick="sortby('orderUpdateDateSort',this)">更新时间</a></li> -->
                                </ul>
                            </div>
                            <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
                            <div class="kong"></div>
                        </div>
                    </div>
     
                    <table class="table mainTable activitylist_bodyer_table" id="contentTable">
                        <thead>
                        <tr>
                            <th width="4%">序号</th>
                            <th width="10%">地接社名称</th>
                            <th width="10%">地接社等级/分类</th>
							<th width="10%">营业额</th>
                            <th width="10%">游客数(人)</th>
							<th width="10%">定价等级</th>
							<th width="10%">合作状态</th>
                            <th width="16%">操作</th>
                        </tr>
                        </thead>
                        <tbody>
	                        <c:if test="${fn:length(page.list) <= 0 }">
				                 <tr class="toptr" >
				                 	<td colspan="15" style="text-align: center;">没有符合条件的记录</td>
				                 </tr>
			        		</c:if>
                        	<c:forEach items="${page.list}" var="supplier" varStatus="s">
	                            <tr>
	                                <td>${s.count}</td>
	                                <td>${supplier.supplierName}</td>
	                                <td><c:if test="${empty supplier.supplierType or supplier.supplierType == ''}">——</c:if>${supplier.supplierType}</td>
									<td class="tr">${supplier.bussinessUUID}</td>
									<td class="tr"></td>
									<td class="tc"></td>
	                                <td></td>
	                                <td class="p0">
	                    				<dl class="handle">
					                    	<dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
					                    	<dd style="left:47%;">
					                			<p>
					                				<span></span>
													<a href="javascript:void(0)" onClick="strategydetail(${supplier.id})">详情</a><br/>
													<a href="javascript:void(0)" onClick="suppliermod(${supplier.id})">修改</a>
													<a href="${ctx}/supplier/deleteSupplierInfo?id=${supplier.id}" onClick="return confirm('您确认要删除该地接社吗？', this.href)">删除</a>
												</p>
				                   			</dd>
				                   		</dl>
				                   </td>
	                            </tr>
                            </c:forEach>
                        </tbody>
                    </table>
				<!-- 分页部分 -->
	<div class="pagination clearFix">${page}</div>
	<!--右侧内容部分结束-->
        </form:form>
</body>
</html>
