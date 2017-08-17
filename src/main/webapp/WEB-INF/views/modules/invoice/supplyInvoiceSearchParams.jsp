<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<div class="activitylist_bodyer_right_team_co">
	<%-- 发票记录     start --%>
	<c:if test="${param.verifyStatus eq ''}">
		<div class="activitylist_bodyer_right_team_co2">
			<input value="${invoiceNum }" class="inputTxt searchInput" name="invoiceNum" id="invoiceNum" type="text" placeholder="请输入发票号"/>
        </div>
		<div class="zksx">筛选</div>
        <div class="form_submit">
			<input class="btn btn-primary ydbz_x" type="button" onclick="query()" value="搜索"/>
      		<input class="btn ydbz_x" type="button" onclick="resetSearchParams()" value="清空所有条件"/>
        	<input class="btn ydbz_x" type="button" onclick="exportToExcel('${ctx}')" value="导出Excel"/>
            </div>

            <div class="ydxbd">
				<span></span>
            
            <!-- 0453需求,开票类型替换成开票项目-->
        	<c:choose>
	        	<c:when test="${fns:getUser().company.uuid eq '5c05dfc65cd24c239cd1528e03965021'}"> 
	            	<div class="activitylist_bodyer_right_team_co1" >
						<label class="activitylist_team_co3_text">开票项目：</label>
						<div class="selectStyle">
						<select name="invoiceSubject" id="invoiceSubject">
							<option value="" <c:if test="${empty invoiceSubject}">selected</c:if>>不限</option>
							<c:forEach var="invoices" items="${invoiceSubject_qhjq }">
								<option value="${invoices.value }" <c:if test="${invoiceSubject==invoices.value}">selected="selected"</c:if>>${invoices.label }</option>
							</c:forEach>
						</select>
						</div>
					</div>
		        </c:when>
		       	<c:otherwise> 
		       		 <div class="activitylist_bodyer_right_team_co1" >
	                      <label class="activitylist_team_co3_text">开票类型：</label>
						 <div class="selectStyle">
	                      <select name="invoiceType" id="invoiceType">
		                      <option value="" <c:if test="${empty invoiceType}">selected</c:if>>全部</option>
		                      <c:forEach var="invoice" items="${invoiceTypes }">
		                      		<option value="${invoice.value }" <c:if test="${invoiceType==invoice.value}">selected="selected"</c:if>>${invoice.label }</option>
		                      </c:forEach>
	                      </select>
						 </div>
		            </div> 
		       	 </c:otherwise>
        	</c:choose> 
        	<!-- 0453需求,开票类型替换成开票项目-->
	       	
	        <div class="activitylist_bodyer_right_team_co1">
	        	<div class="activitylist_team_co3_text">审核状态：</div>
				<div class="selectStyle">
	            <select id="selectVerifyStatus" name="selectVerifyStatus">
					<option value="" <c:if test="${empty selectVerifyStatus}">selected</c:if>>全部</option>
					<option value="0" <c:if test="${selectVerifyStatus eq '0'}">selected</c:if>>未审核</option>
					<option value="1" <c:if test="${selectVerifyStatus eq '1'}">selected</c:if>>审核通过</option>
					<option value="2" <c:if test="${selectVerifyStatus eq '2'}">selected</c:if>>被驳回</option>
				</select>
				</div>
            </div>
            <div class="activitylist_bodyer_right_team_co1" >
            	<label class="activitylist_team_co3_text">开票方式：</label>
				<div class="selectStyle">
                <select name="invoiceMode" id="invoiceMode">
	            	<option value="" <c:if test="${empty invoiceMode}">selected</c:if>>全部</option>
	                <c:forEach var="invoice" items="${invoiceModes }">
	                	<option value="${invoice.value }" <c:if test="${invoiceMode==invoice.value}">selected="selected"</c:if>>${invoice.label }</option>
	                </c:forEach>
                </select>
				</div>
	        </div>
	        <div class="activitylist_bodyer_right_team_co1">
            	<label class="activitylist_team_co3_text">开票日期：</label>
	            <input id="invoiceTimeBegin" class="inputTxt dateinput" name="invoiceTimeBegin" value="${invoiceTimeBegin }" 
	            	onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('invoiceTimeBegin').value==''){$dp.$('invoiceTimeBegin').value=vvv;}}})" readonly/> 
	            <span style="font-size:12px; font-family:'宋体';"> 至</span>  
	            <input id="invoiceTimeEnd" class="inputTxt dateinput" name="invoiceTimeEnd" value="${invoiceTimeEnd }"  onClick="WdatePicker()" readonly/>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
	        	<div class="activitylist_team_co3_text">发票抬头：</div>
	            <input value="${invoiceHead }" class="inputTxt" name="invoiceHead" id="invoiceHead" type="text" />
            </div>
            <div class="activitylist_bodyer_right_team_co1">
	        	<div class="activitylist_team_co3_text">申请人：</div>
	        	<input value="${createName }" class="inputTxt" name="createName" id="createName" type="text" />
            </div>
            <div class="activitylist_bodyer_right_team_co1">
	        	<div class="activitylist_team_co3_text">开票客户：</div>
	        	<input value="${invoiceCustomer }" class="inputTxt" name="invoiceCustomer" id="invoiceCustomer" type="text" />
            </div>
            <div class="activitylist_bodyer_right_team_co1">
            	<label class="activitylist_team_co3_text">申请日期：</label>
	        	<input id="orderTimeBegin" class="inputTxt dateinput" name="applyInvoiceBegin" value="${applyInvoiceBegin }"  onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('orderTimeBegin').value==''){$dp.$('orderTimeBegin').value=vvv;}}})" readonly/> 
	        	<span style="font-size:12px; font-family:'宋体';"> 至</span>  
	        	<input id="orderTimeEnd" class="inputTxt dateinput" name="applyInvoiceEnd" value="${applyInvoiceEnd }"  onClick="WdatePicker()" readonly/>
            </div>
            <%--越谏行踪隐藏开票项目，需求0411，yudong.xu 0453针对起航假期也需隐藏--%>
			<c:if test="${!isYJXZ and fns:getUser().company.uuid ne '5c05dfc65cd24c239cd1528e03965021'}">
                <div class="activitylist_bodyer_right_team_co1" >
					<label class="activitylist_team_co3_text">开票项目：</label>
					<div class="selectStyle">
					<select name="invoiceSubject" id="invoiceSubject">
						<option value="" <c:if test="${empty invoiceSubject}">selected</c:if>>不限</option>
						<c:forEach var="invoices" items="${invoiceSubjects }">
							<option value="${invoices.value }" <c:if test="${invoiceSubject==invoices.value}">selected="selected"</c:if>>${invoices.label }</option>
						</c:forEach>
					</select>
					</div>
				</div>
			</c:if>
            <!-- 加入来款单位筛选条件 -->
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">来款单位：</div>
				<input value="${invoiceComingUnit}" class="inputTxt" name="invoiceComingUnit" id="invoiceComingUnit" type="text" />
	        </div>
			<!-- 加入申请类型筛选条件   0444需求-->
			<c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">申请类型：</div>
					<div class="selectStyle">
					<select name="applyInvoiceWay" id="applyInvoiceWay">
						<option value=""  <c:if test="${empty applyInvoiceWay}">selected</c:if>>请选择</option>
						<option value="0" <c:if test="${applyInvoiceWay=='0'}">selected="selected"</c:if>>正常申请</option>
						<option value="1" <c:if test="${applyInvoiceWay=='1'}">selected="selected"</c:if>>预开发票</option>
					</select>
					</div>
	            </div>
	        </c:if>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">发票金额：</label>
                <span class="pr" style="display:inline-block;">
                <input value="${invoiceMoneyBegin }" name="invoiceMoneyBegin" class="inputTxt" flag="istips"
                	onkeyup="validNum(this)" onafterpaste="validNum(this)" placeholder="输入金额"/>
                </span>
				<%--<span class="ipt-tips ipt-tips2">输入金额</span>--%>
                <span>至</span>
                <span class="pr" style="display:inline-block;">
                <input value="${invoiceMoneyEnd }" name="invoiceMoneyEnd" class="inputTxt" flag="istips"
                	onkeyup="validNum(this)" onafterpaste="validNum(this)"placeholder="输入金额" />
                </span>
            </div>
	        </div>
		</c:if><%-- 发票记录    end  --%>

		<%-- 待审核发票     start --%>
		<c:if test="${param.verifyStatus eq '0'}">
            <div class="activitylist_bodyer_right_team_co2 ">
	            <input id="orderNum" name="orderNum" class="inputTxt inputTxtlong searchInput" value="${orderNum }" placeholder="请输入预订单号"/>
            </div>
			<div class="zksx">筛选</div>
            <div class="form_submit">
            	<input class="btn btn-primary ydbz_x" type="button" onclick="query()" value="搜索"/>
            	<input class="btn ydbz_x" type="button" onclick="resetSearchParams()" value="清空所有条件"/>
            	<input class="btn ydbz_x" type="button" onclick="exportToExcel('${ctx}')" value="导出Excel"/>
            </div>

            <div class="ydxbd">
				<span></span>
            	<div class="activitylist_bodyer_right_team_co1">
	                <div class="activitylist_team_co3_text">发票抬头：</div>
	                <input value="${invoiceHead }" class="inputTxt" name="invoiceHead" id="invoiceHead" type="text" />
                </div>
                <div class="activitylist_bodyer_right_team_co1">
		            <div class="activitylist_team_co3_text" >团号：</div>
		            <input id="groupCode" name="groupCode" class="inputTxt inputTxtlong" value="${groupCode }"/> 
            	</div>
            	<div class="activitylist_bodyer_right_team_co1">
                	<label class="activitylist_team_co3_text">下单日期：</label>
	                <input id="orderTimeBegin" class="inputTxt dateinput" name="orderTimeBegin" value="${orderTimeBegin }" 
	                	onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('orderTimeBegin').value==''){$dp.$('orderTimeBegin').value=vvv;}}})" readonly/> 
	                <span style="font-size:12px; font-family:'宋体';"> 至</span>  
	                <input id="orderTimeEnd" class="inputTxt dateinput" name="orderTimeEnd" value="${orderTimeEnd }"  onClick="WdatePicker()" readonly/>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
	                <div class="activitylist_team_co3_text">开票客户：</div>
	                <input value="${invoiceCustomer }" class="inputTxt" name="invoiceCustomer" id="invoiceCustomer" type="text" />
                </div>
                <!-- 加入来款单位筛选条件 -->
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">来款单位：</div>
					<input value="${invoiceComingUnit}" class="inputTxt" name="invoiceComingUnit" id="invoiceComingUnit" type="text" />
            	</div>
                <div class="activitylist_bodyer_right_team_co1">
                	<label class="activitylist_team_co3_text">申请日期：</label>
	                <input id="applyInvoiceBegin" class="inputTxt dateinput" name="applyInvoiceBegin" value="${applyInvoiceBegin }"  onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('applyInvoiceBegin').value==''){$dp.$('applyInvoiceBegin').value=vvv;}}})" readonly/> 
	                <span style="font-size:12px; font-family:'宋体';"> 至</span>  
	                <input id="applyInvoiceEnd" class="inputTxt dateinput" name="applyInvoiceEnd" value="${applyInvoiceEnd }"  onClick="WdatePicker()" readonly/>
                </div>
             	<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">开票方式：</div>
					<div class="selectStyle">
                    <select name="invoiceMode" id="invoiceMode">
                     	<option value="" <c:if test="${empty invoiceMode}">selected</c:if>>全部</option>
                     	<c:forEach var="invoice" items="${invoiceModes }">
                     		<option value="${invoice.value }" <c:if test="${invoiceMode==invoice.value}">selected="selected"</c:if>>${invoice.label }</option>
                     	</c:forEach>
                    </select>
					</div>
           		</div>
           		<!-- 0453需求,开票类型替换成开票项目-->
       	   		<c:choose>
	           		<c:when test="${fns:getUser().company.uuid eq '5c05dfc65cd24c239cd1528e03965021'}"> 
		           		<div class="activitylist_bodyer_right_team_co1">
							<label class="activitylist_team_co3_text">开票项目：</label>
							<div class="selectStyle">
							<select name="invoiceSubject" id="invoiceSubject">
								<option value="" <c:if test="${empty invoiceSubject}">selected</c:if>>不限</option>
								<c:forEach var="invoices" items="${invoiceSubject_qhjq }">
									<option value="${invoices.value }" <c:if test="${invoiceSubject==invoices.value}">selected="selected"</c:if>>${invoices.label }</option>
								</c:forEach>
							</select>
							</div>
						</div>
		        	</c:when>
		       		<c:otherwise>
		       			<div class="activitylist_bodyer_right_team_co1">
					  		<label class="activitylist_team_co3_text">开票类型：</label>
							<div class="selectStyle">
					  		<select name="invoiceType" id="invoiceType">
						  		<option value="" <c:if test="${empty invoiceType}">selected</c:if>>全部</option>
						  		<c:forEach var="invoice" items="${invoiceTypes }">
									<option value="${invoice.value }" <c:if test="${invoiceType==invoice.value}">selected="selected"</c:if>>${invoice.label }</option>
						  		</c:forEach>
					  		</select>
							</div>
			        	</div>
		       		</c:otherwise>
	        	</c:choose>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">发票金额：</label>
					<span class="pr" style="display:inline-block;">
					<input value="${invoiceMoneyBegin }" name="invoiceMoneyBegin" class="inputTxt" flag="istips"
						onkeyup="validNum(this)" onafterpaste="validNum(this)">
					<span class="ipt-tips ipt-tips2">输入金额</span></span>
					<span>至</span>
					<span class="pr" style="display:inline-block;">
					<input value="${invoiceMoneyEnd }" name="invoiceMoneyEnd" class="inputTxt" flag="istips"
						onkeyup="validNum(this)" onafterpaste="validNum(this)">
					<span class="ipt-tips ipt-tips2">输入金额</span></span>
             	</div>
             	<div class="activitylist_bodyer_right_team_co1">
	                <div class="activitylist_team_co3_text">申请人：</div>
	                <input value="${createName }" class="inputTxt" name="createName" id="createName" type="text" />
             	</div>
            	<%--越谏行踪隐藏开票项目，需求0411，yudong.xu 0453起航假期也需隐藏--%>
				<c:if test="${!isYJXZ and fns:getUser().company.uuid ne '5c05dfc65cd24c239cd1528e03965021'}">
          			<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">开票项目：</label>
						<div class="selectStyle">
					<select name="invoiceSubject" id="invoiceSubject">
						<option value="" <c:if test="${empty invoiceSubject}">selected</c:if>>不限</option>
						<c:forEach var="invoices" items="${invoiceSubjects }">
							<option value="${invoices.value }" <c:if test="${invoiceSubject==invoices.value}">selected="selected"</c:if>>${invoices.label }</option>
						</c:forEach>
					</select>
						</div>
					</div>
				</c:if>
			
				<!-- 加入申请类型筛选条件  0444需求-->
				<c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">申请类型：</div>
						<div class="selectStyle">
						<select name="applyInvoiceWay" id="applyInvoiceWay">
							<option value=""  <c:if test="${empty applyInvoiceWay}">selected</c:if>>请选择</option>
							<option value="0" <c:if test="${applyInvoiceWay=='0'}">selected="selected"</c:if>>正常申请</option>
							<option value="1" <c:if test="${applyInvoiceWay=='1'}">selected="selected"</c:if>>预开发票</option>
						</select>
						</div>
            		</div>
            	</c:if>
            </div>
        </c:if><%-- 待审核发票     end --%>
       
        <%-- 已审核发票     start --%>
        <c:if test="${param.verifyStatus eq 'ne0' }">
        	<div class="activitylist_bodyer_right_team_co2">
	            <input id="groupCode" name="groupCode" class="inputTxt inputTxtlong searchInput" value="${groupCode }"placeholder="请输入团号"/>
            </div>
			<div class="zksx">筛选</div>
            <div class="form_submit">
                <input class="btn btn-primary ydbz_x" type="button" onclick="query()" value="搜索"/>
                <input class="btn ydbz_x" type="button" onclick="resetSearchParams()" value="清空所有条件"/>
                <input class="btn ydbz_x" type="button" onclick="exportToExcel('${ctx}')" value="导出Excel"/>
            </div>

            <div class="ydxbd">
				<span></span>
            	<div class="activitylist_bodyer_right_team_co1">
	            	<div class="activitylist_team_co3_text" >订单号：</div>
	            	<input id="orderNum" name="orderNum" class="inputTxt inputTxtlong" value="${orderNum }"/> 
            	</div>
	            <div class="activitylist_bodyer_right_team_co1">
	                	<div class="activitylist_team_co3_text">开票客户：</div>
	                	<input value="${invoiceCustomer }" class="inputTxt" name="invoiceCustomer" id="invoiceCustomer" type="text" />
                	</div>
                <div class="activitylist_bodyer_right_team_co1">
                		<label class="activitylist_team_co3_text">发票金额：</label>
                		<span class="pr" style="display:inline-block;">
                			<input value="${invoiceMoneyBegin }" name="invoiceMoneyBegin" class="inputTxt" flag="istips"
                				onkeyup="validNum(this)" onafterpaste="validNum(this)">
                			<span class="ipt-tips ipt-tips2">输入金额</span>
                		</span>
                		<span>至</span>
                		<span class="pr" style="display:inline-block;">
                			<input value="${invoiceMoneyEnd }" name="invoiceMoneyEnd" class="inputTxt" flag="istips"
                				onkeyup="validNum(this)" onafterpaste="validNum(this)">
                			<span class="ipt-tips ipt-tips2">输入金额</span>
                		</span>
            		</div>
            	<div class="activitylist_bodyer_right_team_co1">
                    	<label class="activitylist_team_co3_text">开票方式：</label>
						<div class="selectStyle">
                    	<select name="invoiceMode" id="invoiceMode">
                     		<option value="" <c:if test="${empty invoiceMode}">selected</c:if>>全部</option>
                     		<c:forEach var="invoice" items="${invoiceModes }">
                     			<option value="${invoice.value }" <c:if test="${invoiceMode==invoice.value}">selected="selected"</c:if>>${invoice.label }</option>
                     		</c:forEach>
                    	</select>
						</div>
           			</div>
           		<div class="activitylist_bodyer_right_team_co1">
	                	<div class="activitylist_team_co3_text">审核状态：</div>
						<div class="selectStyle">
	                	<select id="selectVerifyStatus" name="selectVerifyStatus">
							<option value="" <c:if test="${empty selectVerifyStatus}">selected</c:if>>全部</option>
							<option value="1" <c:if test="${selectVerifyStatus eq '1'}">selected</c:if>>审核通过</option>
							<option value="2" <c:if test="${selectVerifyStatus eq '2'}">selected</c:if>>被驳回</option>
						</select>
						</div>
	            	</div>
                <div class="activitylist_bodyer_right_team_co1">
                		<label class="activitylist_team_co3_text">下单日期：</label>
	                	<input id="orderTimeBegin" class="inputTxt dateinput" name="orderTimeBegin" value="${orderTimeBegin }" 
	                		onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('orderTimeBegin').value==''){$dp.$('orderTimeBegin').value=vvv;}}})" readonly/> 
	                	<span style="font-size:12px; font-family:'宋体';"> 至</span>  
	                	<input id="orderTimeEnd" class="inputTxt dateinput" name="orderTimeEnd" value="${orderTimeEnd }"  onClick="WdatePicker()" readonly/>
               	 	</div>
                	<!-- 0453需求,开票类型替换成开票项目-->
        			<c:choose>
            			<c:when test="${fns:getUser().company.uuid eq '5c05dfc65cd24c239cd1528e03965021'}">
            				<div class="activitylist_bodyer_right_team_co1">
	                      		<label class="activitylist_team_co3_text">开票项目：</label>
								<div class="selectStyle">
	                      		<select name="invoiceSubject" id="invoiceSubject">
		                      		<option value="" <c:if test="${empty invoiceSubject}">selected</c:if>>全部</option>
		                      		<c:forEach var="ins" items="${invoiceSubject_qhjq }">
		                      			<option value="${ins.value }" <c:if test="${invoiceSubject==ins.value}">selected="selected"</c:if>>${ins.label }</option>
		                      		</c:forEach>
	                      		</select>
								</div>
		        			</div>
                		</c:when>
       					<c:otherwise>
	                		<div class="activitylist_bodyer_right_team_co1">
	                      		<label class="activitylist_team_co3_text">开票类型：</label>
								<div class="selectStyle">
	                      		<select name="invoiceType" id="invoiceType">
		                      		<option value="" <c:if test="${empty invoiceType}">selected</c:if>>全部</option>
		                      		<c:forEach var="invoice" items="${invoiceTypes }">
		                      			<option value="${invoice.value }" <c:if test="${invoiceType==invoice.value}">selected="selected"</c:if>>${invoice.label }</option>
		                      		</c:forEach>
	                      		</select>
								</div>
		        			</div>
	        			</c:otherwise>
		        	</c:choose>
		        	<!-- 0453需求,开票类型替换成开票项目-->
	        		<div class="activitylist_bodyer_right_team_co1">
	                	<div class="activitylist_team_co3_text">申请人：</div>
	                	<input value="${createName }" class="inputTxt" name="createName" id="createName" type="text" />
             		</div>
	            	<div class="activitylist_bodyer_right_team_co1">
                		<label class="activitylist_team_co3_text">申请日期：</label>
	                	<input id="applyInvoiceBegin" class="inputTxt dateinput" name="applyInvoiceBegin" value="${applyInvoiceBegin }" 
	                		onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('applyInvoiceBegin').value==''){$dp.$('applyInvoiceBegin').value=vvv;}}})" readonly/> 
	                	<span style="font-size:12px; font-family:'宋体';"> 至</span>  
	                	<input id="applyInvoiceEnd" class="inputTxt dateinput" name="applyInvoiceEnd" value="${applyInvoiceEnd }"  onClick="WdatePicker()" readonly/>
                	</div>
	             	<div class="activitylist_bodyer_right_team_co1">
	                	<div class="activitylist_team_co3_text">发票抬头：</div>
	                	<input value="${invoiceHead }" class="inputTxt" name="invoiceHead" id="invoiceHead" type="text" />
               	 	</div>
                	<!-- 加入来款单位筛选条件 -->
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">来款单位：</div>
						<input value="${invoiceComingUnit}" class="inputTxt" name="invoiceComingUnit" id="invoiceComingUnit" type="text" />
            		</div>
            
            		<!-- 加入申请类型筛选条件 0444需求-->
            		<c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
						<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">申请类型：</div>
							<div class="selectStyle">
							<select name="applyInvoiceWay" id="applyInvoiceWay">
								<option value=""  <c:if test="${empty applyInvoiceWay}">selected</c:if>>请选择</option>
								<option value="0" <c:if test="${applyInvoiceWay=='0'}">selected="selected"</c:if>>正常申请</option>
								<option value="1" <c:if test="${applyInvoiceWay=='1'}">selected="selected"</c:if>>预开发票</option>
							</select>
							</div>
            			</div>
            		</c:if>
            </div>
        </c:if><%-- 已审核发票   end --%>
        
        <%-- 开票    start  --%>
        <c:if test="${param.verifyStatus eq '1'}">
        	<div class="activitylist_bodyer_right_team_co2">
	            <input id="groupCode" name="groupCode" class="inputTxt searchInput inputTxtlong" value="${groupCode }" placeholder="请输入团号"/>
            </div>
			<div class="zksx">筛选</div>
            <div class="form_submit">
                <input class="btn btn-primary ydbz_x" type="button" onclick="query()" value="搜索"/>
                <input class="btn ydbz_x" type="button" onclick="resetSearchParams()" value="清空所有条件"/>
                <input class="btn ydbz_x" type="button" onclick="exportToExcel('${ctx}')" value="导出Excel"/>
            </div>
	        <div class="ydxbd">
				<span></span>
	        	<div class="activitylist_bodyer_right_team_co1">
	                <div class="activitylist_team_co3_text">开票状态：</div>
					<div class="selectStyle">
	                <select id="createStatus" name="createStatus">
						<option value="" <c:if test="${empty createStatus}">selected</c:if>>全部</option>
						<option value="0" <c:if test="${createStatus eq '0'}">selected</c:if>>待开票</option>
						<option value="1" <c:if test="${createStatus eq '1'}">selected</c:if>>已开票</option>
					</select>
					</div>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
                	<label class="activitylist_team_co3_text">下单日期：</label>
	                <input id="orderTimeBegin" class="inputTxt dateinput" name="orderTimeBegin" value="${orderTimeBegin }"  
	                	onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('orderTimeBegin').value==''){$dp.$('orderTimeBegin').value=vvv;}}})" readonly/> 
	                <span style="font-size:12px; font-family:'宋体';"> 至</span>  
	                <input id="orderTimeEnd" class="inputTxt dateinput" name="orderTimeEnd" value="${orderTimeEnd }"  onClick="WdatePicker()" readonly/>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                	<label class="activitylist_team_co3_text">发票金额：</label>
                	<span class="pr" style="display:inline-block;">
                		<input value="${invoiceMoneyBegin }" name="invoiceMoneyBegin" class="inputTxt" flag="istips"
                			onkeyup="validNum(this)" onafterpaste="validNum(this)">
                		<span class="ipt-tips ipt-tips2">输入金额</span>
                	</span>
                	<span>至</span>
                	<span class="pr" style="display:inline-block;">
                		<input value="${invoiceMoneyEnd }" name="invoiceMoneyEnd" class="inputTxt" flag="istips"
                			onkeyup="validNum(this)" onafterpaste="validNum(this)">
                		<span class="ipt-tips ipt-tips2">输入金额</span>
                	</span>
            	</div>
				<div class="activitylist_bodyer_right_team_co1">
	                <div class="activitylist_team_co3_text">发票抬头：</div>
	                <input value="${invoiceHead }" class="inputTxt" name="invoiceHead" id="invoiceHead" type="text" />
                </div>
                <!-- 0453需求,开票类型替换成开票项目-->
		        <c:choose>
		            <c:when test="${fns:getUser().company.uuid eq '5c05dfc65cd24c239cd1528e03965021'}">
		            	<div class="activitylist_bodyer_right_team_co1">
		                    <label class="activitylist_team_co3_text">开票项目：</label>
		                    <select name="invoiceSubject" id="invoiceSubject">
		                        <option value="" <c:if test="${empty invoiceSubject}">selected</c:if>>不限</option>
		                       	<c:forEach var="invoices" items="${invoiceSubject_qhjq }">
		                       		<option value="${invoices.value }" <c:if test="${invoiceSubject==invoices.value}">selected="selected"</c:if>>${invoices.label }</option>
		                       	</c:forEach>
		                    </select>
		           		</div>
		           		<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">来款单位：</div>
							<input value="${invoiceComingUnit}" class="inputTxt" name="invoiceComingUnit" id="invoiceComingUnit" type="text" />
			            </div>
		           	</c:when>
	       			<c:otherwise>
	       				<div class="activitylist_bodyer_right_team_co1">
		                      <label class="activitylist_team_co3_text">开票类型：</label>
							<div class="selectStyle">
		                      <select name="invoiceType" id="invoiceType">
			                      <option value="" <c:if test="${empty invoiceType}">selected</c:if>>全部</option>
			                      <c:forEach var="invoice" items="${invoiceTypes }">
			                      		<option value="${invoice.value }" <c:if test="${invoiceType==invoice.value}">selected="selected"</c:if>>${invoice.label }</option>
			                      </c:forEach>
		                      </select>
							</div>
			        	</div>
			        	<div class="activitylist_bodyer_right_team_co1">
		                    <label class="activitylist_team_co3_text">开票方式：</label>
							<div class="selectStyle">
		                    <select name="invoiceMode" id="invoiceMode">
		                     <option value="" <c:if test="${empty invoiceMode}">selected</c:if>>全部</option>
		                     <c:forEach var="invoice" items="${invoiceModes }">
		                     		<option value="${invoice.value }" <c:if test="${invoiceMode==invoice.value}">selected="selected"</c:if>>${invoice.label }</option>
		                     </c:forEach>
		                    </select>
							</div>
		           		</div>
	       			</c:otherwise>
        		</c:choose>
       	 		<!-- 0453需求,开票类型替换成开票项目  加入来款单位筛选条件-->
	        	<div class="activitylist_bodyer_right_team_co1">
	                <div class="activitylist_team_co3_text">申请人：</div>
	                <input value="${createName }" class="inputTxt" name="createName" id="createName" type="text" />
             	</div>
             	<div class="activitylist_bodyer_right_team_co1">
	                <div class="activitylist_team_co3_text">开票客户：</div>
	                <input value="${invoiceCustomer }" class="inputTxt" name="invoiceCustomer" id="invoiceCustomer" type="text" />
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                	<label class="activitylist_team_co3_text">开票日期：</label>
	                <input id="invoiceTimeBegin" class="inputTxt dateinput" name="invoiceTimeBegin" value="${invoiceTimeBegin }"  onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('invoiceTimeBegin').value==''){$dp.$('invoiceTimeBegin').value=vvv;}}})" readonly/> 
	                <span style="font-size:12px; font-family:'宋体';"> 至</span>  
	                <input id="invoiceTimeEnd" class="inputTxt dateinput" name="invoiceTimeEnd" value="${invoiceTimeEnd }"  onClick="WdatePicker()" readonly/>
              	</div>
              	<div class="activitylist_bodyer_right_team_co1">
              		<div class="activitylist_team_co3_text" >订单号：</div>
	            	<input id="orderNum" name="orderNum" class="inputTxt inputTxtlong" value="${orderNum }"/> 
            	</div>
            	<div class="activitylist_bodyer_right_team_co1">
              		<div class="activitylist_team_co3_text" >发票号：</div>
	            	<input value="${invoiceNum }" class="inputTxt" name="invoiceNum" id="invoiceNum" type="text" /> 
            	</div>
              	<div class="activitylist_bodyer_right_team_co1">
                	<label class="activitylist_team_co3_text">申请日期：</label>
	                <input id="applyInvoiceBegin" class="inputTxt dateinput" name="applyInvoiceBegin" value="${applyInvoiceBegin }"  onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('applyInvoiceBegin').value==''){$dp.$('applyInvoiceBegin').value=vvv;}}})" readonly/> 
	                <span style="font-size:12px; font-family:'宋体';"> 至</span>  
	                <input id="applyInvoiceEnd" class="inputTxt dateinput" name="applyInvoiceEnd" value="${applyInvoiceEnd }"  onClick="WdatePicker()" readonly/>
                </div>
                <c:if test="${fns:getUser().company.uuid eq '5c05dfc65cd24c239cd1528e03965021'}">
	                <div class="activitylist_bodyer_right_team_co1">
	                    <label class="activitylist_team_co3_text">开票方式：</label>
						<div class="selectStyle">
	                    <select name="invoiceMode" id="invoiceMode">
	                     <option value="" <c:if test="${empty invoiceMode}">selected</c:if>>全部</option>
	                     <c:forEach var="invoice" items="${invoiceModes }">
	                     		<option value="${invoice.value }" <c:if test="${invoiceMode==invoice.value}">selected="selected"</c:if>>${invoice.label }</option>
	                     </c:forEach>
	                    </select>
						</div>
	           		</div>
           		</c:if>
                <%--越谏行踪隐藏开票项目，需求0411，yudong.xu 0453起航假期也需隐藏--%>
				<c:if test="${!isYJXZ and fns:getUser().company.uuid ne '5c05dfc65cd24c239cd1528e03965021'}">
                	<div class="activitylist_bodyer_right_team_co1">
                		<label class="activitylist_team_co3_text">开票项目：</label>
						<div class="selectStyle">
                    	<select name="invoiceSubject" id="invoiceSubject">
                        	<option value="" <c:if test="${empty invoiceSubject}">selected</c:if>>不限</option>
                       		<c:forEach var="invoices" items="${invoiceSubjects }">
                       			<option value="${invoices.value }" <c:if test="${invoiceSubject==invoices.value}">selected="selected"</c:if>>${invoices.label }</option>
                       		</c:forEach>
                    	</select>
						</div>
					</div>
				</c:if>
				
			    <!-- 加入来款单位筛选条件  0444需求-->
			    <c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">来款单位：</div>
						<input value="${invoiceComingUnit}" class="inputTxt" name="invoiceComingUnit" id="invoiceComingUnit" type="text" />
	            	</div>
				
					<!-- 加入申请类型筛选条件 0444需求-->
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">申请类型：</div>
						<div class="selectStyle">
						<select name="applyInvoiceWay" id="applyInvoiceWay">
							<option value=""  <c:if test="${empty applyInvoiceWay}">selected</c:if>>请选择</option>
							<option value="0" <c:if test="${applyInvoiceWay=='0'}">selected="selected"</c:if>>正常申请</option>
							<option value="1" <c:if test="${applyInvoiceWay=='1'}">selected="selected"</c:if>>预开发票</option>
						</select>
						</div>
	            	</div>
                </c:if>            
	        </div>       
       	</c:if>
       	</div>
