<%@ page contentType="text/html;charset=UTF-8" %>

		<form:form id="searchForm" modelAttribute="travelActivity" 
			action="${ctx }/weixin/distribution/productList?newflag=newflag" method="post">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        	<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        	<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
        	<input id="productOrGroup" name="productOrGroup" type="hidden" value="${productOrGroup}" />
        	<input id="activityIds" type="hidden" name="activityIds" value=""/>
        	
        	<div class="activitylist_bodyer_right_team_co">
        		<div class="activitylist_bodyer_right_team_co2 pr">
                	<%--<label>搜索：</label>--%>
                	<input class="txtPro inputTxt searchInput" id="wholeSalerKey"
                		name="wholeSalerKey" value="${wholeSalerKey}" placeholder="输入产品名称、团号，支持模糊匹配"/>
                	<%--<span id="sousuo" class="ipt-tips" style="display: block;">输入产品名称、团号，支持模糊匹配</span>--%>
            	</div>
				<a class="zksx">筛选</a>
				<div class="form_submit">
            		<input class="btn btn-primary ydbz_x" type="submit" value="搜索"/>
            		<input type="button" value="清空所有条件" onclick="resetSearchParams()" class="btn ydbz_x"/>
            	</div>
             	<div class="ydxbd">
					<span></span>
            		<div class="activitylist_bodyer_right_team_co1">
                    	<label class="activitylist_team_co3_text">出发地：</label>
						<div class="selectStyle">
							<form:select id="fromArea" path="fromArea" itemValue="key" itemLabel="value">
								<form:option value="">不限</form:option>
								<form:options items="${fromAreas}"/>
							</form:select>
						</div>
                	</div>
                	<div class="activitylist_bodyer_right_team_co1">
                    	<label class="activitylist_team_co3_text">目的地：</label>
                    	<tags:treeselect id="targetArea" name="targetAreaIdList" value="${travelActivity.targetAreaIds}"
                                     labelName="targetAreaNameList" labelValue="${targetAreaNames}"
                                     title="区域" url="/activity/manager/filterTreeData1?kind=${activityKind}"
                                     checked="true"/>
                	</div>
                	<div class="activitylist_bodyer_right_team_co1">
                    	<label class="activitylist_team_co3_text">计调：</label>
                    	<input id="createName" name="createName" class="inputTxt" value="${createName}">
                	</div>
                	<div class="activitylist_bodyer_right_team_co1">
                    	<label class="activitylist_team_co3_text">出团日期：</label>
                    	<input id="groupOpenDate" class="inputTxt dateinput" name="groupOpenDate" 
                        	value='<fmt:formatDate value="${travelActivity.groupOpenDate }" pattern="yyyy-MM-dd"/>'
                            onFocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})"
                            readonly/>
                    	<span style="font-size:12px; font-family:'宋体';"> 至</span>
                    	<input id="groupCloseDate" class="inputTxt dateinput" name="groupCloseDate"
                           value='<fmt:formatDate value="${travelActivity.groupCloseDate }" pattern="yyyy-MM-dd"/>'
                           onClick="WdatePicker()" readonly/>
                	</div>
                	<div class="activitylist_bodyer_right_team_co1">
                    	<div class="activitylist_team_co3_text" for="spinner" class="fl">行程天数：</div>
                    	<input id="activityDuration" class="spinner" maxlength="3" name="activityDuration"
                           value="${param.activityDuration }" onafterpaste="this.value=this.value.replace(/\D/g,'')"
                           onkeyup="this.value=this.value.replace(/\D/g,'')">
                	</div>
                	<div class="activitylist_bodyer_right_team_co1">
                    	<label class="activitylist_team_co3_text">领队：</label>
                    	<input id="groupLead" name="groupLead" class="inputTxt" value="${travelActivity.groupLead }">
                	</div>
                	<div class="activitylist_bodyer_right_team_co1">
                    	<label class="activitylist_team_co3_text">航空公司：</label>
						<div class="selectStyle">
							<form:select id="trafficName" path="activityAirTicket.airlines">
								<form:option value="">不限</form:option>
								<form:options items="${trafficNames}" itemValue="airlineCode" itemLabel="airlineName"/>
							</form:select>
						</div>
                	</div>
                	<div class="activitylist_bodyer_right_team_co4 sCurrency">
                    	<label class="activitylist_team_co3_text">同行价格：</label>
						<div class="selectStyle">
							<select id="selectCurrencyType" name="currencyType">
								<c:forEach items="${currencyList}" var="currency" varStatus="s">
									<c:if test="${currency.id != '1'}">
										<option value="${currency.id}" <c:if test="${currencyType==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
									</c:if>
								</c:forEach>
							</select>
						</div>
                    	<input type="text" id="settlementAdultPriceStart" class="inputTxt" name="settlementAdultPriceStart"
                        	value="${settlementAdultPriceStart }"/>
                    	<span> 至</span>
                    	<input type="text" id="settlementAdultPriceEnd" class="inputTxt" name="settlementAdultPriceEnd"
                           	value="${settlementAdultPriceEnd }"/>
                	</div>
                	<%-- 隐藏列表刷选条件 --%>
                	<%-- <div class="kong"></div>
                	<div class="activitylist_bodyer_right_team_co3">
                    	<div class="activitylist_team_co3_text">旅游类型：</div>
                    	<form:select path="travelTypeId" itemValue="key" itemLabel="value" id="travelTypeId">
                        	<form:option value="">不限</form:option>
                        	<form:options items="${travelTypes}"/>
                    	</form:select>
                	</div>
                	<div class="activitylist_bodyer_right_team_co3">
                    	<div class="activitylist_team_co3_text">产品系列：</div>
                    	<form:select path="activityLevelId" itemValue="key" itemLabel="value" id="productser">
                        	<form:option value="">不限</form:option>
                        	<form:options items="${productLevels}"/>
                    	</form:select>
                	</div>
                	<div class="activitylist_bodyer_right_team_co3" style="margin-left: 52px;">
                    	<label>产品类型：</label>
                    	<form:select path="activityTypeId" itemValue="key" itemLabel="value" id="productType" style="margin-left:-4px;">
                        	<form:option value="">不限</form:option>
                        	<form:options items="${productTypes}"/>
                    	</form:select>
                	</div> --%>
            	</div> 
        	</div>
		</form:form>