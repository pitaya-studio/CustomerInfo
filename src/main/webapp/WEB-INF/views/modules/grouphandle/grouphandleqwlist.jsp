<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

   <head>
	<title>签务团控列表</title>
	<meta name="decorator" content="wholesaler"/>
   <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
	
	
	<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-ui.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/default.validator.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script> 
    
    <script type="text/javascript">
        $(function () {
        	g_context_url = "${ctx}"
        	//给输入订单号,团号提示框信息添加事件-s
        	$("#orderNumOrGroupCode").focus(function(){
        		$('.ipt-tips').hide();
        	});
        	$("#orderNumOrGroupCode").blur(function(){
        		if($("#orderNumOrGroupCode").val().length==0){
        			$('.ipt-tips').show();
        		}else{
        			$('.ipt-tips').hide();
        		}
        	});
        	$('.ipt-tips').click(function(){
        		$("#orderNumOrGroupCode").focus();
        	});
        	//给输入订单号,团号提示框信息添加事件-e
        	//debugger;
        	
	        //搜索条件筛选
	        launch();
	        
	        //默认展开筛选
	        <%--if("zhankai" == "${flag}"){--%>
	    		<%--document.getElementById("showFlag").value="zhankai";--%>
	    		<%--if ($('.ydxbd').is(":hidden") == true) {--%>
	    			<%--document.getElementById("showFlag").value="zhankai";--%>
		            <%--$('.ydxbd').show();--%>
		            <%--$('.zksx').siblings('.conditionalReset').show();--%>
		            <%--$('.zksx').text('收起筛选');--%>
		            <%--$('.zksx').addClass('zksx-on');--%>
		        <%--} --%>
	    	<%--}--%>
	        
	        //操作浮框
            operateHandler();
	        
	        //列表选择样式处理
	        orderOrGroupListInit('${searchForm.showList}');
	        
	        $("#salerId" ).comboboxInquiry();
	        $("#agentinfoId" ).comboboxInquiry();
	        $("#visaCountryId" ).comboboxInquiry();
	         
            
        });
        
        
        function expand(child, obj) {
        	
        	//debugger;
        	
            if($(child).is(":hidden")) {
                $(obj).text("收起客户");
                $(child).show();
                $(obj).parent().parent().parent().parent().parent().addClass("tr-hover");
                //$(obj).parents("td").addClass("td-extend");
            } else {
                if(!$(child).is(":hidden")) {
                    $(child).hide();
                    //$(obj).parents("td").removeClass("td-extend");
                    $(obj).text("展开客户");
                     $(obj).parent().parent().parent().parent().parent().removeClass("tr-hover");
                }
            }
        }
        
        
        function orderOrGroupListInit(type) {
        	
        	// debugger;
        	
            if (type == "traveler") {
            	
                $('#contentTable_order').show();
                $('#contentTable').hide();
                $('#travelerliebiao').addClass('select');
                $('#travelerliebiao').siblings().removeClass('select');
                
            	//区分显示的数据列表  
            	$("#showList").val("traveler");
                
            } else {
            	
                $('#contentTable_order').hide();
                $('#contentTable').show();
                $('#groupliebiao').addClass('select');
                $('#groupliebiao').siblings().removeClass('select');
                
                //区分显示的数据列表
            	$("#showList").val("group");
            }
        }
        
        function orderOrGroupList(type, elem) {
            if (type == "group") {
            	
                $('#contentTable_order').hide();
                $('#contentTable').show();
                $(elem).addClass('select');
                $(elem).siblings().removeClass('select');
                
                //区分显示的数据列表
            	$("#showList").val("group");
                
                $("#searchForm").submit();
                
            } if(type=="traveler") {
            	
            	
                $('#contentTable_order').show();
                $('#contentTable').hide();
                $(elem).addClass('select');
                $(elem).siblings().removeClass('select');
                
            	//区分显示的数据列表  
            	$("#showList").val("traveler");
            	$("#searchForm").submit();
            }
        }

 
        
    	//展开筛选按钮
		<%--function launch(){--%>
		    <%--$('.ydxbd').each(function (index, element) {--%>
		        <%--if ($(element).is(":hidden") == true) {--%>
		            <%--$(element).siblings('.conditionalReset').hide();--%>
		        <%--} else {--%>
		            <%--$(element).siblings('.conditionalReset').show();--%>
		        <%--}--%>
		    <%--});--%>
		    <%----%>
		    <%--$('.zksx').click(function () {--%>
		    	<%----%>
		        <%--if ($('.ydxbd').is(":hidden") == true) {--%>
		        	<%--document.getElementById("showFlag").value="zhankai";--%>
		            <%--$('.ydxbd').show();--%>
		            <%--$(this).siblings('.conditionalReset').show();--%>
		            <%--$(this).text('收起筛选');--%>
		            <%--$(this).addClass('zksx-on');--%>
		        <%--} else {--%>
		        	<%--document.getElementById("showFlag").value="shouqi";--%>
		            <%--$('.ydxbd').hide();--%>
		            <%--$(this).siblings('.conditionalReset').hide();--%>
		            <%--$(this).text('展开筛选');--%>
		            <%--$(this).removeClass('zksx-on');--%>
		        <%--}--%>
		        <%----%>
		    <%--});--%>
		    <%----%>
		    <%--//处理团号  和  订单  号  查询--%>
		    <%--if(''!='${searchForm.commonCode}'){--%>
		    	<%--$("#orderNumOrGroupCode").next().text('');--%>
		    <%--}--%>
		    <%----%>
		<%--}--%>
    	
    	
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#youkePageNo").val(n);
			$("#youkePageSize").val(s);
			$("#searchForm").attr("action","${ctx}/grouphandle/grouphandleqwlist");
			$("#searchForm").submit();
	    }
		 function youkesortby(sortBy,obj){
			    var temporderBy = $("#youkeOrderBy").val();
			    if(temporderBy.match(sortBy)){
			        sortBy = temporderBy;
			        if(sortBy.match(/ASC/g)){
			            sortBy = $.trim(sortBy.replace(/ASC/gi,""))+" DESC";
			        }else{
			            sortBy = $.trim(sortBy.replace(/DESC/gi,""))+" ASC";
			        }
			    }else{
			        sortBy = sortBy+" DESC";
			    }
			    $("#searchForm").attr("action","${ctx}/grouphandle/grouphandleqwlist");
			    $("#youkeOrderBy").val(sortBy);
			    $("#searchForm").submit();
		}
    	
		var resetSearchParams = function(){
		    $(':input','#searchForm')
				.not(':button, :submit, :reset, :hidden')
				.val('')
				.removeAttr('checked')
				.removeAttr('selected');
		    $('#salerId').val('');
		    $('#travelerName').val('${showType}');
		    $('#aboutSigningTimeStart').val('');
		    $('#aboutSigningTimeEnd').val('');
		    $('#agentinfoId').val('');
		    $('#visaCountryId').val('');
		    $('#signingTimeStart').val('');
		    $('#signingTimeEnd').val('');
		    $('#activityProductKind').val('');
		    $('#visaTypeId').val('');
		    $('#visaStauts').val('');
		};
		
		
		function sortby(sortBy,obj){
		    var temporderBy = $("#orderBy").val();
		
		    if(temporderBy.match(sortBy)){
		        sortBy = temporderBy;
		        if(sortBy.match(/ASC/g)){
		            sortBy = $.trim(sortBy.replace(/ASC/gi,""))+" DESC";
		        }else{
		            sortBy = $.trim(sortBy.replace(/DESC/gi,""))+" ASC";
		        }
		    }else{
		        sortBy = sortBy+" DESC";
		    }
		    $("#searchForm").attr("action","${ctx}/grouphandle/grouphandleqwlist");
		    $("#orderBy").val(sortBy);
		    $("#searchForm").submit();
		}
        
		
		 //保存按钮
	     function updateTraveler_qianwu(groupId,passportStatusId,travlerId){
	    	
	    	 var passportStatusIds = $("#"+passportStatusId).val();//护照状态
	    	 $.ajax({
	         	cache:true,
	         	type:"POST",
	         	url:g_context_url+ "/grouphandle/savePassportStatus",
	         	data:{ 
						
						"passportStatus" : passportStatusIds,
						
						"travelerIds" : travlerId,

					},
						async: false,
						success: function(msg){
						if(msg.msg!=null&&msg.msg!=""){
							top.$.jBox.tip(msg.msg,'warning');
						}else{
							top.$.jBox.tip("保存成功",'warning');
							showTraveler();
						}
					}
	         });
	     }
    </script>
    </head>

    <body>
      
        <div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%"> 
        
          <!-- 搜索查询 --> 
          <!-- 查询form -->
          <form id="searchForm" name="searchForm" action="${ctx}/grouphandle/grouphandleqwlist"  method="post" >  
           
             <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
			 <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			 <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			 
			 <input id="departmentId" name="departmentId" type="hidden" value="${departmentId}" />
			 
			 <input id="youkePageNo" name="youkePageNo" type="hidden" value="${travelPage.pageNo}"/>
			 <input id="youkePageSize" name="youkePageSize" type="hidden" value="${travelPage.pageSize}"/>
			 <input id="youkeOrderBy" name="youkeOrderBy" type="hidden" value="${travelPage.orderBy}"/>
		
			 <input value="" type="hidden" id="showList" name="showList"/>     
			 <input value="" type="hidden" id="showFlag" name="showFlag"/> 
            
            <!-- 订单查询DIV -->
            <div class="activitylist_bodyer_right_team_co">
                <div class="activitylist_bodyer_right_team_co2 pr">
	                <input id="orderNumOrGroupCode" name="commonCode"
						   class="inputTxt searchInput inputTxtlong" value="${searchForm.commonCode}"
						   onKeyUp="this.value=this.value.replaceColonChars()"  onafterpaste="this.value=this.value.replaceColonChars()"placeholder="输入团号、产品名称"/>
                </div>
				<a class="zksx">筛选</a>
                
				<div class="form_submit">
					<input id="btn_search" type="submit" class="btn btn-primary ydbz_x" type="button" onclick="query(0)" value="搜索" />
					<input class="btn ydbz_x" type="button"  onclick="resetSearchParams()" value="清空所有条件" />
				</div>
				<p class="main-right-topbutt"><a class="primary" href="${ctx}/grouphandle/visagroupBatchEditListNew" target="_blank" id="piliangcaozuo">批量操作</a></p>
				<div class="ydxbd">
					<span></span>
                     <div class="activitylist_bodyer_right_team_co1">
                                <label class="activitylist_team_co3_text">销售：</label>
                                    <select name="salerId" id="salerId">
											<option value="">不限</option>
											<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
												<!-- 用户类型  1 代表销售 -->
												<option value="${userinfo.id }" <c:if test="${userinfo.id eq searchForm.salerId}">selected="selected"</c:if>>${userinfo.name }</option>
											</c:forEach>
										</select>
                                </div>
					 <div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">游客姓名：</div>
						<input value="${searchForm.travelerName }" class="inputTxt inputTxtlong" name="travelerName" id="travelerName">
					</div>
					 <div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">预计约签时间：</label>
						<input readonly="" onclick="WdatePicker()" value="${searchForm.aboutSigningTimeStart }" name="aboutSigningTimeStart" class="inputTxt dateinput" id="aboutSigningTimeStart">
						<span>至</span>
						<input readonly="" onclick="WdatePicker()" value="${searchForm.aboutSigningTimeEnd }" name="aboutSigningTimeEnd" class="inputTxt dateinput" id="aboutSigningTimeEnd">
					</div>
					 <div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">渠道：</label>
						<select id="agentinfoId" name="agentinfoId">
							<option value="">全部</option>
							<c:choose>
								<c:when test="${ searchForm.agentinfoId eq '-1' }">
									<option value="-1" selected="selected"><c:choose><c:when test="${companyUUid eq '7a81a03577a811e5bc1e000c29cf2586' }">未签</c:when><c:otherwise>非签约渠道</c:otherwise></c:choose></option>
								</c:when>
								<c:otherwise>
									<option value="-1"><c:choose><c:when test="${companyUUid eq '7a81a03577a811e5bc1e000c29cf2586' }">未签</c:when><c:otherwise>非签约渠道</c:otherwise></c:choose></option>
								</c:otherwise>
							</c:choose>
							<c:forEach items="${agentinfoList}" var="agentinfo">
								<c:choose>
									<c:when test="${ agentinfo.id eq searchForm.agentinfoId}">
										<option value="${agentinfo.id}" selected="selected">${agentinfo.agentName}</option>
									</c:when>
									<c:otherwise>
										<option value="${agentinfo.id}">${agentinfo.agentName}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</div>
					 <div  class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">签证国家：</label>
						<select id="visaCountryId" name="visaCountryId">
						   <option value="">不限</option>
							<c:forEach items="${countryList}" var="country">
								<c:choose>
									<c:when test="${ country.id eq searchForm.visaCountryId}">
										<option value="${country.id}" selected="selected">${country.countryName_cn}</option>
									</c:when>
									<c:otherwise>
										<option value="${country.id}">${country.countryName_cn}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</div>
					 <div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">实际约签日期：</label>
						<input readonly="" onclick="WdatePicker()" value="${searchForm.signingTimeStart }" name="signingTimeStart" class="inputTxt dateinput" id="signingTimeStart">
						<span>至</span>
						<input readonly="" onclick="WdatePicker()" value="${searchForm.signingTimeEnd }" name="signingTimeEnd" class="inputTxt dateinput" id="signingTimeEnd">
					</div>
					 <div class="activitylist_bodyer_right_team_co1">
					 <label class="activitylist_team_co3_text">团队类型：</label>
						<div class="selectStyle">
							<select id="activityProductKind" name="activityProductKind">
								<option value="">不限</option>
								<c:choose>
									<c:when test="${'1' eq searchForm.activityProductKind}">
										<option value="1" selected="selected">单团</option>
									</c:when>
									<c:otherwise>
										<option value="1" >单团</option>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${ '2' eq searchForm.activityProductKind}">
										<option value="2" selected="selected">散拼</option>
									</c:when>
									<c:otherwise>
										<option value="2">散拼</option>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${ '3' eq searchForm.activityProductKind}">
										<option value="3" selected="selected">游学</option>
									</c:when>
									<c:otherwise>
										<option value="3">游学</option>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${ '4' eq searchForm.activityProductKind}">
										<option value="4" selected="selected">大客户</option>
									</c:when>
									<c:otherwise>
										<option value="4" >大客户</option>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${ '5' eq searchForm.activityProductKind}">
										<option value="5" selected="selected">自由行</option>
									</c:when>
									<c:otherwise>
										<option value="5">自由行</option>
									</c:otherwise>
								</c:choose>
								<!--因为签务团控不涉及单办签,所以注释掉.
								<c:choose>
									<c:when test="${ '6' eq searchForm.activityProductKind}">
										<option value="6" selected="selected">单办签</option>
									</c:when>
									<c:otherwise>
										<option value="6">单办签</option>
									</c:otherwise>
								</c:choose>
								 -->
								 <!-- 团队类型,需要添加游轮选项 -->
								 <c:choose>
									<c:when test="${ '10' eq searchForm.activityProductKind}">
										<option value="10" selected="selected">游轮</option>
									</c:when>
									<c:otherwise>
										<option value="10">游轮</option>
									</c:otherwise>
								</c:choose>
							</select>
						</div>
					</div>
					 <div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">签证类型：</label>
						<div class="selectStyle">
							<select id="visaTypeId" name="visaTypeId">
								<option value="">不限</option>
								<c:forEach items="${visaTypeList}" var="visaType">
									<c:choose>
										<c:when test="${ visaType.value eq searchForm.visaTypeId}">
											<option value="${visaType.value}" selected="selected">${visaType.label}</option>
										</c:when>
										<c:otherwise>
											<option value="${visaType.value}">${visaType.label}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</div>
					</div>
					 <div class="activitylist_bodyer_right_team_co1">
						 <label class="activitylist_team_co3_text">签证状态：</label>
						 <div class="selectStyle">
							<select id="visaStauts" name="visaStauts">
								<option value="">不限</option>
								<c:forEach items="${visaStatusList}" var="visaStatus">

										<c:choose>
											<c:when test="${visaStatus.value == 3}">
												<c:choose>
													<c:when test="${visaStatus.value eq searchForm.visaStauts}">
														<option selected="selected" value="3">通过</option>
													</c:when>
													<c:otherwise>
														<option value="3">通过</option>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:when test="${visaStatus.value == 2}">
												<c:choose>
													<c:when test="${visaStatus.value eq searchForm.visaStauts}">
														<option selected="selected" value="2">已约签</option>
													</c:when>
													<c:otherwise>
														<option value="2">已约签</option>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${visaStatus.value eq searchForm.visaStauts}">
														<option selected="selected" value="${visaStatus.value}">${visaStatus.label}</option>
													</c:when>
													<c:otherwise>
														<option value="${visaStatus.value}">${visaStatus.label}</option>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>

							</c:forEach>
							</select>
						 </div>
					</div>
                 </div>
			</div>
          </form>
              
              <!-- 订单列表、团期列表 -->
              <div class="supplierLine"> 
                        <a class="select" onclick="orderOrGroupList('group', this)" href="javascript:void(0)" id="groupliebiao">团队列表</a> 
                        <a class="" onclick="orderOrGroupList('traveler', this)" href="javascript:void(0)" id="travelerliebiao">游客列表</a>
              </div>

              
              <!-- 排序 -->
              <div class="activitylist_bodyer_right_team_co_paixu">
              <div class="activitylist_paixu">
              <div class="activitylist_paixu_left">
                   <ul>
<!--                       <li class="activitylist_paixu_left_biankuang lipro.id selected"> <a onClick="sortby('pro.id',this)">创建时间</a></li> -->
<!--                       <li class="activitylist_paixu_left_biankuang lipro.updateDate"> <a onClick="sortby('pro.updateDate',this)">更新时间</a></li> -->
					<c:if test="${searchForm.showList eq 'traveler' }">
							<c:choose>
								<c:when test="${travelPage.orderBy == 'gc.createDate DESC'}">
									<li class="activitylist_paixu_moren">
										<a onclick="youkesortby('gc.createDate',this)">
										创建时间
										<i class="icon icon-arrow-down"></i>
										</a>
									</li>
								</c:when>
								<c:when test="${travelPage.orderBy == 'gc.createDate ASC'}">
									<li class="activitylist_paixu_moren">
										<a onclick="youkesortby('gc.createDate',this)">
										创建时间
										<i class="icon icon-arrow-up"></i>
										</a>
									</li>
								</c:when>
								<c:otherwise>
									<li class="activitylist_paixu_left_biankuang  lipro.updateDate">
										<a onclick="youkesortby('gc.createDate',this)">
										创建时间
										</a>
									</li>
								</c:otherwise>
							</c:choose>

							<c:choose>
								<c:when test="${travelPage.orderBy == 'gc.updateDate DESC'}">
									<li class="activitylist_paixu_moren">
										<a onclick="youkesortby('gc.updateDate',this)">
										更新时间
										<i class="icon icon-arrow-down"></i>
										</a>
									</li>
								</c:when>
								<c:when test="${travelPage.orderBy == 'gc.updateDate ASC'}">
									<li class="activitylist_paixu_moren">
										<a onclick="youkesortby('gc.updateDate',this)">
										更新时间
										<i class="icon icon-arrow-up"></i>
										</a>
									</li>
								</c:when>
								<c:otherwise>
									<li class="activitylist_paixu_left_biankuang  lipro.updateDate">
										<a onclick="youkesortby('gc.updateDate',this)">
										更新时间
										</a>
									</li>
								</c:otherwise>
							</c:choose>
					</c:if>
				<c:if test="${empty searchForm.showList or searchForm.showList eq 'group' }">                                            
                      <c:choose>
	                    	<c:when test="${page.orderBy == '' || page.orderBy == null }">
	                    		<li class="activitylist_paixu_left_biankuang lipro.updateDate">
	                    			<a onclick="sortby('gc.createDate',this)">
		                    			创建时间
	                    			</a>
	                    		</li>
								<li class="activitylist_paixu_left_biankuang lipro.updateDate">
									<a onclick="sortby('gc.updateDate',this)">
										更新时间
									</a>
								</li>
	                    	</c:when>
	                    	
	                    	<c:when test="${page.orderBy == 'gc.createDate DESC'}">
	                    		<li class="activitylist_paixu_moren">
	                    			<a onclick="sortby('gc.createDate',this)">
		                    			创建时间
		                    			<i class="icon icon-arrow-down"></i>
	                    			</a>
	                    		</li>
								<li class="activitylist_paixu_left_biankuang lipro.updateDate">
									<a onclick="sortby('gc.updateDate',this)">
										更新时间
									</a>
								</li>
	                    	</c:when>
	                    	
	                    	<c:when test="${page.orderBy == 'gc.createDate ASC'}">
	                    		<li class="activitylist_paixu_moren">
	                    			<a onclick="sortby('gc.createDate',this)">
		                    			创建时间
		                    			<i class="icon icon-arrow-up"></i>
	                    			</a>
	                    		</li>
								<li class="activitylist_paixu_left_biankuang lipro.updateDate">
									<a onclick="sortby('gc.updateDate',this)">
										更新时间
									</a>
								</li>
	                    	</c:when>
	                    	
	                    	<c:when test="${page.orderBy == 'gc.updateDate DESC'}">
	                    		<li class="activitylist_paixu_left_biankuang lipro.updateDate">
	                    			<a onclick="sortby('gc.createDate',this)">
		                    			创建时间
	                    			</a>
	                    		</li>
								<li class="activitylist_paixu_moren">
									<a onclick="sortby('gc.updateDate',this)">
										更新时间
		                    			<i class="icon icon-arrow-down"></i>
										
									</a>
								</li>
	                    	</c:when>
	                    	
	                    	<c:when test="${page.orderBy == 'gc.updateDate ASC'}">
	                    		<li class="activitylist_paixu_left_biankuang lipro.updateDate">
	                    			<a onclick="sortby(gc.createDate',this)">
		                    			创建时间
	                    			</a>
	                    		</li>
								<li class="activitylist_paixu_moren">
									<a onclick="sortby('gc.updateDate',this)">
										更新时间
		                    			<i class="icon icon-arrow-up"></i>
									</a>
								</li>
	                    	</c:when>
	                    </c:choose>
					</c:if>
                  </ul>
              </div>
              
                <c:if test="${empty searchForm.showList or searchForm.showList eq 'group' }">
                	<div class="activitylist_paixu_right" > 查询结果&nbsp;&nbsp;<strong>${page.count }</strong>&nbsp;条 </div>
                </c:if>
                <c:if test="${searchForm.showList eq 'traveler' }">
                	<div class="activitylist_paixu_right" > 查询结果&nbsp;&nbsp;<strong>${travelPage.count }</strong>&nbsp;条 </div>
                </c:if>
               </div>
          </div>
          <!-- 排序结束 -->
              
             <!-- 订单列表：showType为1000时，团控列表-->  
             <!-- 订单列表：showType为1000时，团控列表--> 
             <!-- 订单列表：showType为1000时，团控列表-->  
            <table class="table mainTable activitylist_bodyer_table orderlist-list" id="contentTable" >
            <thead>
                  <tr>
                    <!--147添加序号-->
                    <th width="3%">序号</th>
                    <!--<th width="12%">订单号</th>-->
                    <th width="12%">团号</th>
                    <!--  <th width="6%">领区联系人</th>-->
                    <th width="12%">产品名称</th>
                    <th width="6%">计调</th>
                    <!--<th width="6%">订单状态</th>-->
                    <th width="6%">出团日期<br>截团日期</th>
                    <!--<th width="8%">下单时间</th>-->
                    <th width="4%">团队类型</th>
                    <th width="5%">人数</th>
                    <th width="4%">操作</th>
                  </tr>
            </thead>
            <tbody>
                <c:if test="${ empty groupHandleList}">	
						<tr >
						   <td colspan="8" ><center>暂无搜索结果</center></td>
						</tr>
				 </c:if>		
				
            
                <c:if test="${ !empty groupHandleList}">	
				  <c:forEach items="${groupHandleList}" var="result" varStatus="status">
            
                  <!-- 第一条记录  开始  -->
                  <tr class="behovered">
	                    <td class="tc">${status.index + 1}</td>
	                <!-- 订单团号-->
	                    <td>${result.activity_group_code}</td>
	                <!-- 领区联系人 --> 
	                <!--  <td></td>-->
	                    <td>${result.activity_product_name}</td>
	                    <td class="tc"> ${result.op_name}</td>
	                    <!--<td>预订</td>-->
	                    <td class="tc">${result.groupOpenDate}<br>${result.groupCloseDate}</td>
	                    <!--<td>2015-07-31<br>03:18:26 </td>-->
	                    <td class="tc">
	                        <c:if test="${result.activity_product_kind eq 1}">单团</c:if>
	                        <c:if test="${result.activity_product_kind eq 2}">散拼</c:if>
	                        <c:if test="${result.activity_product_kind eq 3}">游学</c:if>
	                        <c:if test="${result.activity_product_kind eq 4}">大客户</c:if>
	                        <c:if test="${result.activity_product_kind eq 5}">自由行</c:if>
	                        <c:if test="${result.activity_product_kind eq 6}">单办签</c:if>
	                        <c:if test="${result.activity_product_kind eq 10}">游轮</c:if>
	                    </td>
	                    <td class="tc">${(result.grouptravle_num==null)?"0":result.grouptravle_num}</td>
	               
	                   <td class="p0"><dl class="handle">
	                    <dt><img src="${ctxStatic}/images/handle_cz_rebuild.png" title="操作"></dt>
	                    <dd>
	                      <p> 
	                         <span></span> 
	                         <a onclick="expand('#${result.activity_group_code}',this)" href="javascript:void(0)">展开客户</a>
	                        </p>
	                    </dd>
	                  </dl></td>
	              </tr>
                  
                  <!-- 第一条记录  下的展开结构  -->
	              <tr id="${result.activity_group_code}" class="activity_team_top1" style="display:none;">
	                <td colspan="17" class="team_top" style="background-color:#d1e5f5;">
	                    <table class="table activitylist_bodyer_table table_th_no" id="tbbd" style="margin:0 auto;">
	                        <thead>
	                            <tr>
		                        <!--S 147-->
		                                <th class="tc" width="3%">序号</th>
		                                <th class="tc"width="5%">销售</th>
		                                <th class="tc" width="5%">渠道</th>
		                                <th class="tc"width="6%">姓名</th>
		                                <th class="tc"width="6%">性别</th>
		                                <th class="tc"width="7%">护照号</th>
		                                <th class="tc" width="13%">签证国家</th>
		                                <th class="tc"width="10%">护照状态</th>
		                                <!-- <th class="tc"width="7%">付款状态</th> -->
		                                <th class="tc"width="7%">保存修改</th>
		                        <!--E 147-->
		                        </tr>
		                     </thead>
		                     <tbody>
		                     
		                         <!-- 处理展开结果为空的情况 -->
		                         <c:if test="${ empty result.groupHandleTravellers}">	
										<tr >
										   <td colspan="8" ><center>暂无搜索结果</center></td>
										</tr>
								 </c:if>
		                     
		                      <c:forEach items="${result.groupHandleTravellers}" var="groupHandleTraveller" varStatus="statusindex">
		                      
		                        <!-- 处理销售分组  -->
		                        <c:set var="salerId" value="${groupHandleTraveller.saler_id }"/>
		                        <c:if test="${salerId!=oldSalerId }">
									<c:set var="index" value="1"/>
								</c:if>
								
			                    <tr>
			                       <td class="tc">${statusindex.index + 1}</td>
			                       
			                       <c:if test="${index==1 }">
			                          <td class="tc" rowspan="${groupHandleTraveller.salerTravelRowCount}" >${groupHandleTraveller.saler_name}</td>
			                       </c:if>
			                       
			                       <td class="tc">${groupHandleTraveller.agentinfo_name}</td>
			                       <td class="name tc">${groupHandleTraveller.tname}</td>
			                       <td class="tc">
			                       		<c:if test="${groupHandleTraveller.sex==1}">M</c:if>
			                       		<c:if test="${groupHandleTraveller.sex==2}">F</c:if>
			                       </td>
			                       <td class="passportnum tc">${groupHandleTraveller.passport_num}</td>
			                       
			                        <!-- 签证国家 -->
			                       <td class="tc" >
				                       <div  class="pr xuanfudiv">
				                                ${groupHandleTraveller.country_names}
					                       <div class="ycq xuanfu">more</div>
					                       <div class="hover-title team_top hide" id="hoverWindow" >
							                       <table class="table activitylist_bodyer_table orderlist-list" style="margin:0 auto;">
									                        <thead id="orderOrGroup_order_thead">
									                            <tr>
									                        <!--S 147-->
									                                <th class="tc" width="14%">国家</th>
									                                <th class="tc" width="10%">类型</th>
									                                <th class="tc"width="10%">领区</th>
									                                <th class="tc"width="13%">办签单位</th>
									                                <th class="tc"width="9%">签证状态</th>
									                                <th class="tc"width="11%">预计约签时间<br>实际约签时间</th>
									                                <th class="tc"width="11%">送签时间</th>
									                                <th class="tc"width="11%">出签时间</th>
									                                <th class="tc"width="18%">补资料时间</th>
 									                             </tr>
									                        </thead>
									                        <tbody>
										                        <c:forEach items="${groupHandleTraveller.groupHandleTravllerVisas}" var="groupHandleTravllerVisa">
											                        <tr gvcid = "${groupHandleTravllerVisa.id}">
											                            <td class="tc">${groupHandleTravllerVisa.visa_country_name}</td>
											                            <td class="tc">${groupHandleTravllerVisa.visa_type_name}</td>
											                            <td class="tc">${groupHandleTravllerVisa.visa_consulardistric_name}</td>
											                            <td class="tc">${groupHandleTravllerVisa.visa_handle_unit}</td>
											                            <td class="tc">
											                            	 <c:forEach items="${visaStatusList}" var="visaStatus">
											                            	       <c:if test="${groupHandleTravllerVisa.visa_stauts ==visaStatus.value }">${visaStatus.label}</c:if>
																			 </c:forEach>
											                            </td>
											                            <td class="tc">
											                                 <div class="fbold" style="color:#eb0301">
											                                     <fmt:formatDate value="${groupHandleTravllerVisa.about_signing_time}" pattern="yyyy-MM-dd"/>
											                                 </div><br>
											                                 <div>
											                                     <fmt:formatDate value="${groupHandleTravllerVisa.signing_time}" pattern="yyyy-MM-dd"/>
											                                 </div>
											                            </td>
											                            <td class="tc">
											                                 <fmt:formatDate value="${groupHandleTravllerVisa.visa_delivery_time}" pattern="yyyy-MM-dd"/>
											                            </td>
											                            <td class="tc">
											                                 <fmt:formatDate value="${groupHandleTravllerVisa.visa_got_time}" pattern="yyyy-MM-dd"/>
											                            </td>
											                            <td class="tc">
											                                 <fmt:formatDate value="${groupHandleTravllerVisa.supplementaryinfo_time}" pattern="yyyy-MM-dd"/>
											                            </td>
											                        </tr>
										                        </c:forEach> 
									                        </tbody>
							                     </table>
					                           <%-- <input class="btn btn-primary" value="编辑" type="button" style="float:right" onclick="jbox__editvisa(this,'${ctx}')"> --%>
					                           <c:if test="${groupHandleTraveller.delFlag!=0}">
					                               <input class="btn btn-primary" value="编辑" type="button" style="float:right" onclick="jbox__editvisa(this,'${ctx}')" disabled="disabled">
					                           </c:if>
					                           <c:if test="${groupHandleTraveller.delFlag==0}">
					                              <input class="btn btn-primary" value="编辑" type="button" style="float:right" onclick="jbox__editvisa(this,'${ctx}')">
					                           </c:if>
					
					                       </div>
				                       
				                        </div>
			                        </td>
			                        
			                        
			                        <td class="tc">
			                            <c:if test="${groupHandleTraveller.delFlag!=0}">
			                               <select  id="passportStatus_${groupHandleTraveller.traveler_id}" name="passportStatus" style="width:100%;" disabled="disabled">
			                           </c:if>
			                           <c:if test="${groupHandleTraveller.delFlag==0}">
			                              <select  id="passportStatus_${groupHandleTraveller.traveler_id}" name="passportStatus" style="width:100%;">
			                           </c:if>
				                            <option value="0">请选择</option>
											<option value="1"  ${groupHandleTraveller.passportStatus == '1'?'selected':''}>借出</option>
											<option value="2"  ${groupHandleTraveller.passportStatus == '2'?'selected':''}>销售已领取</option>
											<option value="4"  ${groupHandleTraveller.passportStatus == '4'?'selected':''}>已还</option>
											<option value="5"  ${groupHandleTraveller.passportStatus == '5'?'selected':''}>已取出</option>
											<option value="6"  ${groupHandleTraveller.passportStatus == '6'?'selected':''}>未取出</option>
											<option value="8"  ${groupHandleTraveller.passportStatus == '8'?'selected':''}>计调领取</option>
				                          </select>
			                          </td>
			                       
			
				                      <!-- <td class="name tc">未付</td> -->
				                      <td class="tc" width="5%">
				                         <c:if test="${groupHandleTraveller.delFlag!=0}">
			                                   <input class="btn btn-primary" value="保存" onclick="updateTraveler_qianwu('','passportStatus_${groupHandleTraveller.traveler_id}','${groupHandleTraveller.traveler_id}')" type="button" disabled="disabled">
			                             </c:if>
			                             <c:if test="${groupHandleTraveller.delFlag==0}">
			                                  <input class="btn btn-primary" value="保存" onclick="updateTraveler_qianwu('','passportStatus_${groupHandleTraveller.traveler_id}','${groupHandleTraveller.traveler_id}')" type="button">
			                             </c:if>
				                      </td>
				                      
				                    </tr>
				                    
				                     <!-- 处理销售分组  -->
				                     <c:set var="oldSalerId" value="${groupHandleTraveller.saler_id }"/>
					                 <c:set var="index" value="${index+1 }"/>
					                
				                  </c:forEach> 
				                   
				                  <!-- 处理销售分组  -->
				                  <c:set var="index" value="1"/>
			                    </tbody>
		                </table>
		            </td>
		        </tr>
		        
		       </c:forEach>
			</c:if>
		       <!-- 第一条记录  结束  -->
		       
		      
          </tbody>
        </table>


         <!-- 147 游客列表-->
         <!-- 147 游客列表-->
         <!-- 147 游客列表-->
        <table id="contentTable_order" class="activitylist_bodyer_table hide" >
            
            <thead style="background:#403738;" id="orderOrGroup_order_thead">
	               <tr>
	                <th class="tc" width="2%">序号</th>
	                <!--添加渠道-->
	                 <th class="tc"width="4%">销售</th>
	                <th class="tc" width="5%">渠道</th>
	                <th class="tc"width="8%">团号</th>
	                <!--添加团队类型-->
	                <th class="tc"width="4%">团队类型</th>
	                <th class="tc" width="4%">姓名</th>
	                <th class="tc" width="4%">性别</th>
	                <th class="tc"width="6%">护照号</th>
	                <th class="tc"width="10%">签证国家</th>
	                <th class="tc" width="8%">护照状态</th>
	               <!--  <th class="tc"width="7%">付款状态</th> -->
	                <th class="tc"width="7%">保存<br>修改</th>
	              </tr>
            </thead>
            
            <tbody id="orderOrGroup_order_tbody">
	            <c:if test="${ empty travelPageList}">	
						<tr ><td colspan="10" >
						<center>暂无搜索结果</center>
						</td>
				 </c:if>
	         <c:if test="${ !empty travelPageList}">
	         <c:forEach items="${travelPageList}" var="travelList" varStatus="status">	   
	            <tr class="toptr">
	                <td class="tc">${status.count}</td>
	                <td class="tc">${travelList.salerName }</td>
	                <td class="tc">${travelList.agentInfoName}</td>
	                <td> ${travelList.groupCode} </td>
	                <td class="tc">${travelList.orderTypeName }</td>
	                <td class="tc">${travelList.travellerName }</td>
	                <td class="tc">
	                	<c:if test="${travelList.sex==1 }">M</c:if>
	                	<c:if test="${travelList.sex==2 }">F</c:if>
	                </td>
	                <td class="tc">${travelList.passportNum }</td>
	                <!--S 147悬浮-->
	                <td class="tc" >
	                        <div  class="pr xuanfudiv ">
	                                  ${travelList.countryName }
	                            <div class="ycq xuanfu">more</div>
	                            
	                            <div class="hover-title team_top bdbg hide" id="hoverWindow" >
	                                <table class="table activitylist_bodyer_table orderlist-list" style="margin:0 auto;">
				                        <thead id="orderOrGroup_order_thead">
				                           <tr>
				                                <!--S 147-->
				                                <th class="tc" width="5%">国家</th>
				                                <th class="tc" width="8%">类型</th>
				                                <th class="tc"width="7%">领区</th>
				                                <th class="tc"width="7%">办签单位</th>
				                                <th class="tc"width="7%">签证状态</th>
				                                <th class="tc"width="10%">预计约签时间<br>实际约签时间</th>
				                                <th class="tc"width="7%">送签时间</th>
				                                <th class="tc"width="7%">出签时间</th>
				                                <th class="tc"width="7%">补资料时间</th>
				                           </tr>
				                        </thead>
					                    <tbody>
					                    	<c:forEach items="${travelList.groupHandleTravllerVisas}" var="groupHandleTravllerVisa">
					                           <tr gvcid = "${groupHandleTravllerVisa.id}">
											      <td class="tc">${groupHandleTravllerVisa.visa_country_name}</td>
											      <td class="tc">${groupHandleTravllerVisa.visa_type_name}</td>
											      <td class="tc">${groupHandleTravllerVisa.visa_consulardistric_name}</td>
											      <td class="tc">${groupHandleTravllerVisa.visa_handle_unit}</td>
											      <td class="tc">
											      		<c:forEach items="${visaStatusList}" var="visaStatus">
											                  <c:if test="${groupHandleTravllerVisa.visa_stauts ==visaStatus.value }">${visaStatus.label}</c:if>
												  		</c:forEach>
												  </td>
											      <td class="tc">
											           <div class="fbold" style="color:#eb0301">
											                 <fmt:formatDate value="${groupHandleTravllerVisa.about_signing_time}" pattern="yyyy-MM-dd"/>
											           </div><br>
											           <div>
											                 <fmt:formatDate value="${groupHandleTravllerVisa.signing_time}" pattern="yyyy-MM-dd"/>
											           </div>
											       </td>
											       <td class="tc"><fmt:formatDate value="${groupHandleTravllerVisa.visa_delivery_time}" pattern="yyyy-MM-dd"/></td>
											       <td class="tc"><fmt:formatDate value="${groupHandleTravllerVisa.visa_got_time}" pattern="yyyy-MM-dd"/></td>
											       <td class="tc"><fmt:formatDate value="${groupHandleTravllerVisa.supplementaryinfo_time}" pattern="yyyy-MM-dd"/></td>
											   </tr>
					                        </c:forEach>
					                    </tbody>
				                   </table>
				                   <%-- <input class="btn btn-primary" value="编辑" type="button" style="float:right" onclick="jbox__editvisa(this,'${ctx}')"> --%>
				                      <c:if test="${travelList.delFlag!=0}">
							             <input class="btn btn-primary" value="编辑" type="button" style="float:right" onclick="jbox__editvisa(this,'${ctx}')" disabled="disabled">
							          </c:if>
							          <c:if test="${travelList.delFlag==0}">
							              <input class="btn btn-primary" value="编辑" type="button" style="float:right" onclick="jbox__editvisa(this,'${ctx}')">
							          </c:if>
	
	                            </div>
	                        </div>
	                 </td>
	                        <!--E 147悬浮-->
	                <td class="tc">
<%-- 	                    <select id="passportStatus_${travelList.traveler_id}" name="passportStatus" style="width:100%;"> --%>
							<c:if test="${travelList.delFlag!=0}">
			                      <select  id="passportStatus_${travelList.traveler_id}" name="passportStatus" style="width:100%;" disabled="disabled">
			                </c:if>
			                <c:if test="${travelList.delFlag==0}">
			                    <select  id="passportStatus_${travelList.traveler_id}" name="passportStatus" style="width:100%;">
			                </c:if>
				            <option value="0">请选择</option>
							<option value="1"  ${travelList.passportStatus == '1'?'selected':''}>借出</option>
							<option value="2"  ${travelList.passportStatus == '2'?'selected':''}>销售已领取</option>
							<option value="4"  ${travelList.passportStatus == '4'?'selected':''}>已还</option>
							<option value="5"  ${travelList.passportStatus == '5'?'selected':''}>已取出</option>
							<option value="6"  ${travelList.passportStatus == '6'?'selected':''}>未取出</option>
							<option value="8"  ${travelList.passportStatus == '8'?'selected':''}>计调领取</option>
				        </select>
	                </td>
	                <!-- <td class="tc">未付</td> -->
	                <td class="tc" width="5%">
<%-- 	                <input class="btn btn-primary" value="保存" onclick="updateTraveler_qianwu('','passportStatus_${travelList.traveler_id}','${travelList.traveler_id}')" type="button"> --%>
	                 <c:if test="${travelList.delFlag!=0}">
			             <input class="btn btn-primary" value="保存" onclick="updateTraveler_qianwu('','passportStatus_${travelList.traveler_id}','${travelList.traveler_id}')" type="button" disabled="disabled">
			          </c:if>
			          <c:if test="${travelList.delFlag==0}">
			              <input class="btn btn-primary" value="保存" onclick="updateTraveler_qianwu('','passportStatus_${travelList.traveler_id}','${travelList.traveler_id}')" type="button">
			          </c:if>
	                </td>
<!-- 	                <td class="tc" width="5%"><input class="btn btn-primary" value="保存" onclick="updateTraveler_qianwu('','traveler_AACode_10563','traveler_visaStatus_10563','passportStatus_10563','traveler_guaranteeStatus_10563','traveler_startOut_10563','traveler_contract_10563','10563','5031','actual_delivery_time_10563')" type="button"></td>
 -->	         </tr>
	         
	         </c:forEach>
             </c:if>
              </tbody>
          </table>
        </div>
       
       <c:if test="${empty searchForm.showList or searchForm.showList eq 'group' }">
			 <div class="pagination clearFix">${page}</div>
       </c:if>
       <c:if test="${searchForm.showList eq 'traveler' }">
       		 <div class="pagination clearFix">${travelPage}</div>
       </c:if>
         
            


  
 <!--147 编辑签证弹窗-->
 <!-- <div id="edit_visa_o" class="display-none">
        <div class="edit_visa" >
               <table class="table activitylist_bodyer_table orderlist-list" style="margin:0 auto;">
                        <thead>
                            <tr>
                        S 147
                                <th class="tc" width="7%">游客</th>
                                <th class="tc" width="7%">护照号</th>
                                <th class="tc"width="7%">国家</th>
                                <th class="tc"width="7%">类型</th>
                                <th class="tc"width="7%">领区</th>
                                <th class="tc"width="7%">办签单位</th>
                                <th class="tc"width="7%">签证状态</th>
                                <th class="tc"width="7%">预计约签时间<br>实际约签时间</th>
                                <th class="tc"width="7%">送签时间</th>
                                <th class="tc"width="7%">出签时间</th>
                                <th class="tc"width="7%">补资料时间</th>
                       
                               </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td rowspan="3" class="tc">王某</td>
                            <td rowspan="3" class="tc">001</td>
                            <td class="tc">巴西</td>
                            <td class="tc">个签</td>
                            <td class="tc">北京</td>
                            <td class="tc"><input value="" class="inputTxt inputTxtlong" ></td>
                            <td class="tc"><select id="traveler_visaStatus_10563" name="" style="width:100%;">
                            <option value="-1" selected="selected">请选择</option>
                            <option value="0">未送签</option>
                            <option value="1">送签</option>
                            <option value="2">已约签</option>
                            <option value="3">通过</option>
                            <option value="4">未约签</option>
                            <option value="5">已撤签</option>
                            <option value="7">拒签</option>
                            <option value="8">调查</option>
                            <option  value="9">续补资料</option>
                          </select></td>
                            <td class="tc"><span class="fbold"style="color:#eb0301">2016-01-01</span><br><input readonly="" onclick="WdatePicker()" value="" class="inputTxt dateinput" > </td>
                            <td class="tc"><input readonly="" onclick="WdatePicker()" value="" class="inputTxt dateinput" ></td>
                            <td class="tc"><input readonly="" onclick="WdatePicker()" value="" class="inputTxt dateinput" ></td>
                            <td class="tc"><input readonly="" onclick="WdatePicker()" value="" class="inputTxt dateinput" ></td>
                        </tr>
                        <tr>
                             <td class="tc">阿根廷</td>
                            <td class="tc">个签</td>
                            <td class="tc">北京</td>
                            <td class="tc"><input value="" class="inputTxt inputTxtlong" ></td>
                            <td class="tc"><select id="traveler_visaStatus_10563" name="" style="width:100%;">
                            <option value="-1" selected="selected">请选择</option>
                            <option value="0">未送签</option>
                            <option value="1">送签</option>
                            <option value="2">已约签</option>
                            <option value="3">通过</option>
                            <option value="4">未约签</option>
                            <option value="5">已撤签</option>
                            <option value="7">拒签</option>
                            <option value="8">调查</option>
                            <option  value="9">续补资料</option>
                          </select></td>
                            <td class="tc"><span class="fbold"style="color:#eb0301">2016-01-01</span><br><input readonly="" onclick="WdatePicker()" value="" class="inputTxt dateinput" > </td>
                            <td class="tc"><input readonly="" onclick="WdatePicker()" value="" class="inputTxt dateinput" ></td>
                            <td class="tc"><input readonly="" onclick="WdatePicker()" value="" class="inputTxt dateinput" ></td>
                            <td class="tc"><input readonly="" onclick="WdatePicker()" value="" class="inputTxt dateinput" ></td>
                        </tr>
                        <tr>
                             <td class="tc">秘鲁</td>
                            <td class="tc">个签</td>
                            <td class="tc">北京</td>
                            <td class="tc"><input value="" class="inputTxt inputTxtlong" ></td>
                            <td class="tc"><select id="traveler_visaStatus_10563" name="" style="width:100%;">
                            <option value="-1" selected="selected">请选择</option>
                            <option value="0">未送签</option>
                            <option value="1">送签</option>
                            <option value="2">已约签</option>
                            <option value="3">通过</option>
                            <option value="4">未约签</option>
                            <option value="5">已撤签</option>
                            <option value="7">拒签</option>
                            <option value="8">调查</option>
                            <option  value="9">续补资料</option>
                          </select></td>
                            <td class="tc"><span class="fbold" style="color:#eb0301">2016-01-01</span><br><input readonly="" onclick="WdatePicker()" value="" class="inputTxt dateinput" > </td>
                            <td class="tc"><input readonly="" onclick="WdatePicker()" value="" class="inputTxt dateinput" ></td>
                            <td class="tc"><input readonly="" onclick="WdatePicker()" value="" class="inputTxt dateinput" ></td>
                            <td class="tc"><input readonly="" onclick="WdatePicker()" value="" class="inputTxt dateinput" ></td>
                        </tr>
                        </tbody>
                </table>
         </div>
    </div> -->
        
  </body>
</html>
    