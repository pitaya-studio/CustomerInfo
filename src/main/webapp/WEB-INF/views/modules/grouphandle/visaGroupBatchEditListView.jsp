<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>

	<title>订单-批量操作记录</title>
	<!--[if lte IE 6]>
	<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
	<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
	<![endif]-->
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
	<script type="text/javascript" src="${ctxStatic}/js/little_logo.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-ui.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script type="text/javascript">
        $(function () {
        	g_context_url = "${ctx}";
            //搜索条件筛选
            launch();
			//收款确认提醒
            //147全选返选
            




		$(".mainMenu-ul_new").hover(function(){
			$(this).next().find("span").show();
		},function(){
			$(this).next().find("span").hide();
		});
		$(document).on(" click","#contentTable.orderlist-list tbody tr",function(){
					var $tr=$(this);
					$tr.find(".new-tips").hide();
				});
			
            //操作浮框
            operateHandler();
            //团号和产品切换
            switchNumAndPro();
            //首次打印提醒
            inputTips();
            $(".uiPrint").hover(function () {
                $(this).find("span").show();
            }, function () {
                $(this).find("span").hide();
            });

            $('.team_a_click').toggle(function () {
                $(this).addClass('team_a_click2')
            }, function () {
                $(this).removeClass('team_a_click2')
            });
        });
        function expand(child, obj) {
            if($(child).is(":hidden")) {
                $(obj).html("收起");
                $(child).show();
                $(obj).parents("td").addClass("td-extend");
            } else {
                if(!$(child).is(":hidden")) {
                    $(child).hide();
                    $(obj).parents("td").removeClass("td-extend");
                    $(obj).html("展开");
                }
            }
        }
        /**
        	条件重置
        
        **/
        var resetSearchParams = function(){
        	$(':input','#searchForm')
        	 .not(':button, :submit, :reset, :hidden')
        	 .val('')
        	 .removeAttr('checked')
        	 .removeAttr('selected');

        	$('#salerId').val('');
        	$('#travelName').val('');
        	$('#commonCode').val('');
        	$('#visaCountryId').val('');
        	$('#visaTypeId').val('');
        	$('#agentinfoId').val('');
        	$('#aboutSigningTimeStart').val('');
        	$('#aboutSigningTimeEnd').val('');
        	$('#orderPayStatus').val('');
        	$('#signingTimeStart').val('');
        	$('#signingTimeEnd').val('');
        	$('#activityProductKind').val('');
        	$('#visaStatus').val('');
        }
        
      //排序
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
        	    $("#searchForm").attr("action","${ctx}/grouphandle/visagroupBatchEditListNew");
        	    $("#orderBy").val(sortBy);
        	    $("#searchForm").submit();
        }
        
        function orderOrGroupList(type, elem) {
            if (type == "group") {
                $('#contentTable_order').hide();
                $('#contentTable').show();
                $(elem).addClass('select');
                $(elem).siblings().removeClass('select');
            } if(type=="order") {
                $('#contentTable_order').show();
                $('#contentTable').hide();
                $(elem).addClass('select');
                $(elem).siblings().removeClass('select');
            }
        }

        /**
         * 支付记录
         *
         * param orderId
         * param obj
         */
        function showOrderPay(orderId, obj){
            var sbrtr = $(obj).parents("tr").next();
            var sbrtd = sbrtr.children().eq(0);
            var table = sbrtr.find("table[id=table_orderPay]");
            if(table.length<=0){
                $.ajax({
                    type: "POST",
                    url: contextPath + "/orderCommon/manage/getPayList",
                    data: {
                        orderId:orderId
                    },
                    success: function(msg){
                        var $table = $("<table class=\"table activitylist_bodyer_table\" style=\"margin:0 auto;\"></table>").append("<thead style=\"background:#62AFE7\"><tr><th>付款方式</th><th>金额</th><th>日期</th><th>支付款类型</th><th>是否已确认达账</th><th>支付凭证</th><th>操作</th></tr></thead>");

                        $.each(msg.orderList,function(key,value){
                            var payTypeName = value.payTypeName;
                            var payPriceType = value.payPriceType;
                            var isAsAccount = value.isAsAccount;

                            if(payTypeName==null||payTypeName==undefined){
                                payTypeName="";
                            }
                            if(payPriceType=="1"){
                                payPriceType="支付全款";
                            }else if(payPriceType=="2"){
                                payPriceType="交订金";
                            }if(payPriceType=="3"){
                                payPriceType="支付尾款";
                            }

                            if(isAsAccount=="0"||isAsAccount=="null"||isAsAccount==undefined){
                                isAsAccount="否";
                            }else if(isAsAccount=="1"){
                                isAsAccount="是";
                            }

                            var payvoucher = "<td><a class=\"downloadzfpz\" lang=\""+value.payVoucher+"\">支付凭证ee</a></td>";

                            if(payvoucher==""||payvoucher==undefined||payvoucher==null){
                                payvoucher = "<td>暂无支付凭证</td>";
                            }

                            var paypricechange="<td><a class=\"changepayPrice\" lang=\""+value.payVoucher+"\""+"payprice=\""+value.payPrice+"\">修改</a></td>"
                            $table.append($("<tr></tr>")
                                            .append("<td>"+payTypeName+"</td>")
                                            .append("<td>"+value.payPrice+"</td>")
                                            .append("<td>"+value.createDate+"</td>")
                                            .append("<td>"+payPriceType+"</td>")
                                            .append("<td>"+isAsAccount+"</td>")
                                            .append(payvoucher)
                                            .append(paypricechange)
                            )
                        });
                        sbrtd.append($table);
                    }
                });
            }
            if($(obj).hasClass("jtk")){
                var td = $(obj).closest("td");
                if(sbrtr.is(":hidden")){
                    sbrtr.show();
                    td.addClass("td-extend");
                }else{
                    sbrtr.hide();
                    td.removeClass("td-extend");
                }
            }
        }
        function page(n,s){
        	$("#pageNo").val(n);
        	$("#pageSize").val(s);
        	$("#searchForm").attr("action","${ctx}/grouphandle/visagroupBatchEditListNew");
        	$("#searchForm").submit();
        }
    </script>
    <style type="text/css">
    .timestyle{
        width:85px;
        text-align:right;
        margin-right:5px;
        }</style>
    </head>

    <body>
    <!--  <div class="main-right">
        <ul class="nav-tabs">
        <li id="all"> <a href="javascript:void(0)" onClick="getOrderList(0, 2, 'order');">单办签列表</a> </li>
        <li id="paid" class="ernav"><a href="javascript:void(0);">团签列表</a></li>     
      </ul>-->
          <div class="bgMainRight"> 

        <script type="application/javascript">
                    $(function(){
                        var showType = '0';
                        var orderStatus = '10';
                        var mark = '';
                        var markTail = '';
                        switch (showType)
                        {
                            case '0' :
                                mark = 'all';
                                break;
                            case '1' :
                                mark = 'notPaid';
                                markTail = 'notPaidAll';
                                break;
                            case '2' :
                                mark = 'notPaid';
                                markTail = 'notPaidHead';
                                break;
                            case '3' :
                                mark = 'occupied';
                                break;
                            case '4' :
                                mark = 'paid';
                                markTail = 'paidHead';
                                break;
                            case '5' :
                                mark = 'paid';
                                markTail = 'paidAll';
                                break;
                            case '99' :
                                mark = 'canceled';
                                break;
                            case '111' :
                                mark = 'deleted';
                                break;
                            case '1000' :
                                mark = 'preOrder';
                                break;
                            case 'statistics' :
                                mark = 'statistics';
                                break;
                        }
                        if(mark != ''){
                            $('#' + mark).addClass('active');
                        }
                        if(markTail != '' && mark != ''){
                            $('#' + markTail).children('a').addClass('active');
                            $('#' + mark).addClass('current');
                            $('#' + mark).parent('ul').addClass('hasNav');
                        }

                    });
                </script>
        <div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%"> 
            <!-- 搜索查询 -->
            <form id="searchForm" name="searchForm" action="${ctx}/grouphandle/visagroupBatchEditListNew" method="post">
            <input id="ctx" type="hidden" value="${ctx}" />
            <input id="orderStatus" type="hidden" value="10" />
            <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
            <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
            <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
            <input id="departmentId" name="departmentId" type="hidden" value="${departmentId}" />
            <input id="showType" name="showType" type="hidden" value="0" />
            <input id="orderOrGroup" name="orderOrGroup" type="hidden" value="group" />
            <input value="youke" type="hidden" id="showList" name="showList"/> 
            
            <!-- 订单查询DIV -->
            <div class="activitylist_bodyer_right_team_co">
                  <div class="activitylist_bodyer_right_team_co2 wpr20 pr">
                <div class="activitylist_team_co3_text">搜索：</div>
                <input id="commonCode" name="commonCode"
                                       class="inputTxt inputTxtlong" value="${searchForm.commonCode}"
                                       flag="istips" onKeyUp="this.value=this.value.replaceColonChars()"  onafterpaste="this.value=this.value.replaceColonChars()"/>
                <span class="ipt-tips" style="display: block;">团号、产品名称</span> </div>
                  <div class="form_submit">
                <input id="btn_search" class="btn btn-primary ydbz_x" type="submit"
                                       onclick="query(0)" value="搜索" />
                <input
                                    class="btn ydbz_x" type="button"
                                    onclick="resetSearchParams()" value="清空所有条件" />
              </div>
                  <div class="zksx">筛选</div>
                  <div class="ydxbd">
                     <div class="activitylist_bodyer_right_team_co1">
                                <label class="activitylist_team_co3_text">销售：</label>
                                    <select id="salerId" name="salerId">
                                        <option value="">全部</option>
										<c:forEach items="${createByList}" var="createBy">
											<option value="${createBy}" <c:if test="${ createBy eq searchForm.salerId}"> selected="selected"</c:if>>${createBy}</option>
										</c:forEach>
                                    </select> 
                                </div>
                                  <div class="activitylist_bodyer_right_team_co1">
                                    <div class="activitylist_team_co3_text">游客姓名：</div>
                                    <input value="${searchForm.travelerName}" class="inputTxt inputTxtlong" name="travelerName" id="travelName">
                                </div>
                                <div class="activitylist_bodyer_right_team_co2" style="width:37%;">
                                    <label class="activitylist_team_co3_text" style="width:85px;">预计约签时间：</label>
                                    <input readonly="" onclick="WdatePicker()" value="${searchForm.aboutSigningTimeStart}" name="aboutSigningTimeStart" class="inputTxt dateinput" id="aboutSigningTimeStart"> 
                                    <span> 至 </span>  
                                    <input readonly="" onclick="WdatePicker()" value="${searchForm.aboutSigningTimeEnd}" name="aboutSigningTimeEnd" class="inputTxt dateinput" id="aboutSigningTimeEnd">
                                </div>
                                <div class="kong"></div>
                                 <div class="activitylist_bodyer_right_team_co1">
                                <label class="activitylist_team_co3_text">渠道：</label>
                                    <select id="agentinfoId" name="agentinfoId">
                                        <option value="">全部</option>
										<c:choose>
											<c:when test="${ searchForm.agentinfoId eq '-1' }">
												<option value="-1" selected="selected">非签约渠道</option>
											</c:when>
											<c:otherwise>
												<option value="-1">非签约渠道</option>
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
                                <div class="activitylist_bodyer_right_team_co1">
                                    <div class="activitylist_team_co3_text">签证国家：</div>
                                    <select id="visaCountryId" name="visaCountryId">
                                        <option value="">全部</option>
                                            <c:forEach items="${countryList}" var="country">
                                                <option value="${country.id }" <c:if test="${country.id eq searchForm.visaCountryId }">selected="selected"</c:if>>${country.countryName_cn }</option>
                                            </c:forEach>
                                    </select> 
                                </div>
                                <div class="activitylist_bodyer_right_team_co2" style="width:37%;">
                                    <label class="activitylist_team_co3_text" style="width:85px;">实际约签日期：</label>
                                    <input readonly="" onclick="WdatePicker()" value="${searchForm.signingTimeStart}" name="signingTimeStart" class="inputTxt dateinput" id="signingTimeStart"> 
                                    <span> 至 </span>  
                                    <input readonly="" onclick="WdatePicker()" value="${searchForm.signingTimeEnd}" name="signingTimeEnd" class="inputTxt dateinput" id="signingTimeEnd">
                                </div>
                                <div class="kong"></div>
                                 <div class="activitylist_bodyer_right_team_co1">
                                    <div class="activitylist_team_co3_text">团队类型：</div>
                                    <select id="activityProductKind" name="activityProductKind">
                                        <option value="">全部</option>
                                     
												<option value="1" <c:if test="${'1' eq searchForm.activityProductKind}">selected="selected"</c:if>>单团</option>
												<option value="2" <c:if test="${'2' eq searchForm.activityProductKind}">selected="selected"</c:if>>散拼</option>
												<option value="3" <c:if test="${'3' eq searchForm.activityProductKind}">selected="selected"</c:if>>游学</option>
												<option value="4" <c:if test="${'4' eq searchForm.activityProductKind}">selected="selected"</c:if>>大客户</option>
												<option value="5" <c:if test="${'5' eq searchForm.activityProductKind}">selected="selected"</c:if>>自由行</option>
												<option value="6" <c:if test="${'6' eq searchForm.activityProductKind}">selected="selected"</c:if>>单办</option>
												<option value="10" <c:if test="${'10' eq searchForm.activityProductKind}">selected="selected"</c:if>>游轮</option>
												
                                    </select> 
                                </div>
                                
                                <div class="activitylist_bodyer_right_team_co1">
                                    <div class="activitylist_team_co3_text">签证类型：</div>
                                    <select id="visaTypeId" name="visaTypeId">
                                        <option value="">全部</option>
                                            <c:forEach items="${visaTypeList}" var="visaType">
                                                    <option value="${visaType.value}" <c:if test="${visaType.value eq searchForm.visaTypeId}">selected="selected"</c:if>>${visaType.label }</option>
                                            
                                            </c:forEach>   
                                    </select> 
                                </div>
                                    <div class="activitylist_bodyer_right_team_co1" style="margin-left:60px">
                                    <div class="activitylist_team_co3_text">签证状态：</div>
                                    <select id="visaStauts" name="visaStauts">
                                        <option value="">全部</option>
                                        <c:forEach items="${visaStatusList}" var="status">
                                                    <option value="${status.value}" <c:if test="${status.value eq searchForm.visaStauts}">selected="selected"</c:if>>${status.label}</option>
                                        </c:forEach>
                                    </select> 
                                </div>

                <div class="kong"></div>
               
               
              </div>
                  <div class="kong"></div>
                </div>
          </form>
              
              <!-- 下载 -->
              <form id="exportForm" action="/a/orderCommon/manage/downloadData" method="post">
            <input type="hidden" id="orderId" name="orderId">
            <input type="hidden" name="orderType" value="10">
            <input type="hidden" id="downloadType" name="downloadType">
            <input type="hidden" value="" id="orderNum" name="orderNum">
          </form>
              <!-- 导出 -->
              <form id="exportTravelesForm" action="/a/activity/manager/exportExcel" method="post">
            <input type="hidden" id="groupId" name="groupId">
            <input type="hidden" name="orderType" value="10">
            <input type="hidden" id="groupCode" name="groupCode">
          </form>
              <!-- 部门分区 --> 
              
              <!-- 部门分区 --> 
              
              <!-- 订单列表、团期列表 --> 
              
              <!-- 订单列表、团期列表 -->
             
              
              <!-- 订单排序 --> 
              
              <!-- 排序 -->
              <div class="activitylist_bodyer_right_team_co_paixu">
            <div class="activitylist_paixu">
                  <div class="activitylist_paixu_left">
                <ul>
                      <li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
												<c:choose>
												<c:when test="${page.orderBy == '' || page.orderBy == null }">
													<li class="activitylist_paixu_left_biankuang lipro.updateDate">
														<a onclick="sortby('v.createDate',this)"> 创建时间 </a>
													</li>
													<li class="activitylist_paixu_left_biankuang lipro.updateDate">
														<a onclick="sortby('v.updateDate',this)"> 更新时间 </a>
													</li>
												</c:when>
												<c:when test="${page.orderBy == 'v.createDate DESC'}">
													<li class="activitylist_paixu_moren">
						                    			<a onclick="sortby('v.createDate',this)">
							                    		创建时间
														<i class="icon icon-arrow-down"></i>
						                    			</a>
													</li>
												</c:when>
												<c:when test="${page.orderBy == 'v.createDate ASC'}">
													<li class="activitylist_paixu_moren">
						                    			<a onclick="sortby('v.createDate',this)">
							                    		创建时间
														<i class="icon icon-arrow-up"></i>
						                    			</a>
													</li>
												</c:when> 
												<c:otherwise>
													<li class="activitylist_paixu_left_biankuang  lipro.id selected">
						                    			<a onclick="sortby('v.createDate',this)">
							                    		创建时间
						                    			</a>
													</li>
												</c:otherwise>
											</c:choose>
											
											<c:choose>
												<c:when test="${page.orderBy == 'v.updateDate DESC'}">
													<li class="activitylist_paixu_moren">
						                    			<a onclick="sortby('v.updateDate',this)">
							                    		更新时间
														<i class="icon icon-arrow-down"></i>
						                    			</a>
													</li>
												</c:when>
												<c:when test="${page.orderBy == 'v.updateDate ASC'}">
													<li class="activitylist_paixu_moren">
						                    			<a onclick="youkesortby('v.updateDate',this)">
							                    		更新时间
														<i class="icon icon-arrow-up"></i>
						                    			</a>
													</li>
												</c:when> 
												<c:otherwise>
													<li class="activitylist_paixu_left_biankuang  lipro.updateDate">
						                    			<a onclick="sortby('v.updateDate',this)">
							                    		更新时间
						                    			</a>
													</li>
												</c:otherwise>
											</c:choose>
                    </ul>
              </div>
                  <div class="activitylist_paixu_right">
						<c:choose>
							<c:when test="${page.count >0}">
						查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
						</c:when>
							<c:otherwise>
						查询结果&nbsp;&nbsp;<strong>0</strong>&nbsp;条
						</c:otherwise>
						</c:choose>
					</div>
                  <div class="kong"></div>
                </div>
          </div>
         <!--S 147添加全选反选，批量操作签证，批量操作护照，批量设置时间-->
            <!--<div class="pagination">
            <dl>
              <dt>
                <input name="allChk" onclick="checkall(this)" type="checkbox">
                全选</dt>
              <dt>
                <input name="reverseChk" onclick="checkreverse(this)" type="checkbox">
                反选</dt>
              <dd style="width:500px"> <a style="width:80px">批量操作签证</a><a style="width:80px">批量操作护照</a> <a style="width:80px"onclick="jbox__edittime()">批量设置时间</a></dd>
              
            </dl>
          </div>-->
          <table class="activitylist_bodyer_table">
        <tbody>
            <tr class="checkalltd" height="50">
            <td colspan="16" class="tl">
                <label><input type="checkbox" name="allChk" onclick="checkall(this)">全选</label>
                <label><input name="reverseChk" onclick="checkreverse(this)" type="checkbox">反选</label>
                <a onclick="batchUpdateVisaStatus(null)">批量操作签证</a>
                <a onclick="batchUpdatePassportStatus(null)">批量操作护照</a>
                <a href="javascript:void(0)"onclick="batchUpdatetime(null)">批量设置时间</a>
               
            </td>
            </tr>
        </tbody>
    </table>
          <!--E 147添加全选反选，批量操作签证，批量操作护照，批量设置时间-->
              
              <!-- 订单列表：showType为1000时，是预报名订单 订单列表-->
              
             
              <table id="contentTable_order" class="activitylist_bodyer_table " >
            
            <!-- 订单标题 --> 
            
            <!-- 订单标题 -->
            <thead style="background:#403738;" id="orderOrGroup_order_thead">
                  <tr>
               
                <th class="tc" width="5%">序号</th>
                <th class="tc"width="6%">销售</th>
                <!--添加渠道-->
                <th class="tc"width="6%">渠道</th>
                <th class="tc"width="10%">团号</th>
                <th class="tc" width="6%">姓名</th>
                <th class="tc"width="10%">护照号</th>
                <th class="tc"width="6%">签证国家</th>
                <th class="tc"width="6%">签证类型</th>
                <th class="tc"width="6%">签证领区</th>
                <th class="tc"width="6%">办签单位</th>
                <th class="tc"width="10%">签证状态</th>
                <th class="tc" width="10%">护照状态</th>
                <!--<th class="tc"width="12%">担保</th>
                <th class="tc"width="6%">应收押金<br>达帐押金</th>-->
                <th class="tc"width="6%">预计约签时间<br>实际约签时间</th>
                <th class="tc" width="6%">送签时间</th>
                <th class="tc" width="6%">出签时间</th>
                 <th class="tc" width="6%">补资料时间</th>
                <th class="tc"width="6%">保存<br>修改</th>
               
                
              </tr>
                </thead>
            <!-- 订单列表 --> 
            
            <!-- 订单查询列表显示 -->
            <tbody id="orderOrGroup_order_tbody">
                  <!-- 无查询结果 --> 
                  <c:if test="${fn:length(visaGroupList) <= 0 }">
							<tr class="toptr" >
								<td colspan="16" style="text-align: center;color:green">没有符合条件的记录</td>
							</tr>
				  </c:if>
                  <!-- 查询结果订单列表循环显示 -->
                  <c:forEach items="${visaGroupList}" var="group" varStatus="count">
              <%--     <tr>
										<td width="4%" class="p0">
										<c:choose>
																<c:when test="${!empty traveler.paymentType}">
																	<div class="ycq yj" style="margin-top:1px;">
																		${traveler.paymentType}
																	</div>
																</c:when>
															</c:choose> --%>
															
                  <tr class="toptr">
                	<td><span id="traveler_travelerName" class="sqcq-fj">
                	<c:choose>
                		<c:when test="${group.delFlag==3||group.delFlag==5 }">
                			<input  type="checkbox" class="tdCheckBox" travelerId="${group.id }" id="checkbox4" visaId="${group.vId}" travelername="${group.travelerName }" value="${group.id}@${group.gId}" name="activityId" onclick="t_idcheckchg(this)" style="margin-top: 5px;" disabled="disabled">${count.count}
                		</c:when>
                		<c:otherwise>
                			<input type="checkbox" class="tdCheckBox" travelerId="${group.id }" id="checkbox4" visaId="${group.vId}" travelername="${group.travelerName }" value="${group.id}@${group.gId}" name="activityId" onclick="t_idcheckchg(this)" style="margin-top: 5px;">${count.count}
                		</c:otherwise>
                	</c:choose>
                	</span></td>
<%--                 <input type="checkbox" class="tdCheckBox" travelerId="${group.id }" visaId="${traveler.visaId}" travelerName="${traveler.travelerName }" value="${traveler.id }@${traveler.visaorderId}" name="activityId" onclick="t_idcheckchg(this)"/>${traveler.travelerName}</span>
 --%>				<input type="hidden" value="${group.agentinfoId }" id="activityId_agentId_${group.id}">
                <td class="tc">${group.salerName}</td>
                <td class="tc">${group.agentinfoName}</td>
                <td> ${group.activityGroupCode}</td>
                <td class="tc">${group.travelerName}</td>
                <td>${group.passportNum }</td>
                <td class="tc">${group.visaCountryName}</td>
                <td class="tc">${group.visaTypeName}</td>
                <td class="tc">${group.visaConsulardistricName}</td>
                <td class="tc">
                <c:choose>
                	<c:when test="${group.delFlag==3||group.delFlag==5 }">
                		<input value="${group.visaHandleUnit}" class="inputTxt inputTxtlong" id="visa_handleUnit_${group.vId}" disabled="disabled">
                	</c:when>
                	<c:otherwise>
                		<input value="${group.visaHandleUnit}" class="inputTxt inputTxtlong" id="visa_handleUnit_${group.vId}" >
                	</c:otherwise>
                </c:choose>
                </td>
                <td class="tc">
                <c:choose>
                	<c:when test="${group.delFlag==3||group.delFlag==5 }">
                		<select id="traveler_visaStatus_${group.vId}" name="" style="width:100%;" disabled="disabled">
                    									<option value="-1" <c:if test="${group.visaStauts==-1 }">selected="selected"</c:if>>请选择</option>
														<option value="0" <c:if test="${group.visaStauts==0 }">selected="selected"</c:if>>未送签</option>
														<option value="1" <c:if test="${group.visaStauts==1 }">selected="selected"</c:if>>送签</option>
														<option value="2" <c:if test="${group.visaStauts==2 }">selected="selected"</c:if>>约签</option>
														<option value="3" <c:if test="${group.visaStauts==3 }">selected="selected"</c:if>>出签</option>
														<option value="4" <c:if test="${group.visaStauts==4 }">selected="selected"</c:if>>未约签</option>
														<!--  <option value="4" <c:if test="${traveler.visa.visaStauts==4 }">selected="selected"</c:if>>申请撤签</option> -->
														<option value="5" <c:if test="${group.visaStauts==5 }">selected="selected"</c:if>>已撤签</option>
														<!--<option value="6" <c:if test="${traveler.visa.visaStauts==6 }">selected="selected"</c:if>>撤签失败</option> -->
														<option value="7" <c:if test="${group.visaStauts==7 }">selected="selected"</c:if>>拒签</option>
														<option value="8" <c:if test="${group.visaStauts==8 }">selected="selected"</c:if>>调查</option>
														<option value="9" <c:if test="${group.visaStauts==9 }">selected="selected"</c:if>>续补资料</option>
                     	</select>	
                	</c:when>
                	<c:otherwise>
                		 <select id="traveler_visaStatus_${group.vId}" name="" style="width:100%;">
                    									<option value="-1" <c:if test="${group.visaStauts==-1 }">selected="selected"</c:if>>请选择</option>
														<option value="0" <c:if test="${group.visaStauts==0 }">selected="selected"</c:if>>未送签</option>
														<option value="1" <c:if test="${group.visaStauts==1 }">selected="selected"</c:if>>送签</option>
														<option value="2" <c:if test="${group.visaStauts==2 }">selected="selected"</c:if>>约签</option>
														<option value="3" <c:if test="${group.visaStauts==3 }">selected="selected"</c:if>>出签</option>
														<option value="4" <c:if test="${group.visaStauts==4 }">selected="selected"</c:if>>未约签</option>
														<!--  <option value="4" <c:if test="${traveler.visa.visaStauts==4 }">selected="selected"</c:if>>申请撤签</option> -->
														<option value="5" <c:if test="${group.visaStauts==5 }">selected="selected"</c:if>>已撤签</option>
														<!--<option value="6" <c:if test="${traveler.visa.visaStauts==6 }">selected="selected"</c:if>>撤签失败</option> -->
														<option value="7" <c:if test="${group.visaStauts==7 }">selected="selected"</c:if>>拒签</option>
														<option value="8" <c:if test="${group.visaStauts==8 }">selected="selected"</c:if>>调查</option>
														<option value="9" <c:if test="${group.visaStauts==9 }">selected="selected"</c:if>>续补资料</option>
                     </select>	
                	</c:otherwise>
                </c:choose> 
                </td>
                 <td class="tc">
                 	<c:choose>
                 		<c:when test="${group.delFlag==3||group.delFlag==5 }">
                 			<select id="passportStatus_${group.vId}" name="" style="width:100%;" disabled="disabled">
                                                             <option value="0" <c:if test="${group.passportStatus==0 }">selected="selected"</c:if>>请选择</option>
                                                             <option value="1"<c:if test="${group.passportStatus==1}">selected="selected"</c:if>>借出</option>
                                                             <option  value="2" <c:if test="${group.passportStatus==2 }">selected="selected"</c:if>>销售已领取</option>
                                							 <option value="4" <c:if test="${group.passportStatus==4 }">selected="selected"</c:if>>已还</option>
                                                             <option value="5" <c:if test="${group.passportStatus==5 }">selected="selected"</c:if>>已取出</option>
                                                             <option value="6" <c:if test="${group.passportStatus==6}">selected="selected"</c:if>>未取出</option>
                                                             <option value="8" <c:if test="${group.passportStatus==8}">selected="selected"</c:if>>计调领取</option>
                             </select>
                 		</c:when>
                 		<c:otherwise>
                 			<select id="passportStatus_${group.vId}" name="" style="width:100%;">
                                                             <option value="0" <c:if test="${group.passportStatus==0 }">selected="selected"</c:if>>请选择</option>
                                                             <option value="1"<c:if test="${group.passportStatus==1}">selected="selected"</c:if>>借出</option>
                                                             <option  value="2" <c:if test="${group.passportStatus==2 }">selected="selected"</c:if>>销售已领取</option>
                                							 <option value="4" <c:if test="${group.passportStatus==4 }">selected="selected"</c:if>>已还</option>
                                                             <option value="5" <c:if test="${group.passportStatus==5 }">selected="selected"</c:if>>已取出</option>
                                                             <option value="6" <c:if test="${group.passportStatus==6}">selected="selected"</c:if>>未取出</option>
                                                             <option value="8" <c:if test="${group.passportStatus==8}">selected="selected"</c:if>>计调领取</option>
                            </select>
                 		</c:otherwise>
                 	</c:choose>
                </td>
                 <!--<td class="tc">
                    <select  name="" style="width:100%;">
                        <option value="-1" selected="selected">请选择</option>
                    </select>
                </td>
                
                <td class="tc"><div class="yfje_dd">   
                                <span class="fbold">€100.00</span>
                            </div>
                            <div class="dzje_dd">
                                 <span class="fbold">€100.00</span>
                            </div></td>-->
                <td class="tc"><span class="fbold"style="color:#eb0301"><fmt:formatDate pattern="yyyy-MM-dd " value="${group.aboutSigningTime }"/></span><br>
                <c:choose>
                	<c:when test="${group.delFlag==3||group.delFlag==5 }">
                		 <input id="signingTime_${group.vId}" readonly="" onclick="WdatePicker()" value="<fmt:formatDate pattern="yyyy-MM-dd " value="${group.signingTime }"/>"  class="inputTxt dateinput" disabled="disabled"> 
                	</c:when>
                	<c:otherwise>
                		 <input id="signingTime_${group.vId}" readonly="" onclick="WdatePicker()" value="<fmt:formatDate pattern="yyyy-MM-dd " value="${group.signingTime }"/>"  class="inputTxt dateinput" > 
                	</c:otherwise>
                </c:choose>
                </td>
                <td class="tc">
                <c:choose>
                	<c:when test="${group.delFlag==3||group.delFlag==5 }">
                		<input id="visaDeliveryTime_${group.vId}" readonly="" onclick="WdatePicker()" value="<fmt:formatDate pattern="yyyy-MM-dd " value="${group.visaDeliveryTime }"/>"  class="inputTxt dateinput" disabled="disabled" >
                	</c:when>
                	<c:otherwise>
               			<input id="visaDeliveryTime_${group.vId}" readonly="" onclick="WdatePicker()" value="<fmt:formatDate pattern="yyyy-MM-dd " value="${group.visaDeliveryTime }"/>"  class="inputTxt dateinput" >
                	</c:otherwise>
                </c:choose>
                </td>
                <td class="tc">
                <c:choose>
                	<c:when test="${group.delFlag==3||group.delFlag==5 }">
                		<input id="visaGotTime_${group.vId}" readonly="" onclick="WdatePicker()" value="<fmt:formatDate pattern="yyyy-MM-dd " value="${group.visaGotTime }"/>"  class="inputTxt dateinput" disabled="disabled"> 
                	</c:when>
                	<c:otherwise>
                		<input id="visaGotTime_${group.vId}" readonly="" onclick="WdatePicker()" value="<fmt:formatDate pattern="yyyy-MM-dd " value="${group.visaGotTime }"/>"  class="inputTxt dateinput" > 
                	</c:otherwise>
                </c:choose>
                </td>
                <td class="tc">
                <c:choose>
                	<c:when test="${group.delFlag==3||group.delFlag==5 }">
                		<input id="supplementaryinfoTime_${group.vId}" readonly="" onclick="WdatePicker()" value="<fmt:formatDate pattern="yyyy-MM-dd " value="${group.supplementaryinfoTime }"/>"  class="inputTxt dateinput" disabled="disabled" > 
                	</c:when>
                	<c:otherwise>
               			<input id="supplementaryinfoTime_${group.vId}" readonly="" onclick="WdatePicker()" value="<fmt:formatDate pattern="yyyy-MM-dd " value="${group.supplementaryinfoTime }"/>"  class="inputTxt dateinput" > 
                	</c:otherwise>
                </c:choose>
                </td>
                <td class="tc" width="5%">
                <c:choose>
                	<c:when test="${group.delFlag==3||group.delFlag==5 }">
                		<input class="btn btn-primary" value="保存" onclick="updateTraveler_qianwu('${group.vId}','visa_handleUnit_${group.vId}','traveler_visaStatus_${group.vId}','passportStatus_${group.vId}','signingTime_${group.vId}','visaDeliveryTime_${group.vId}','visaGotTime_${group.vId}','supplementaryinfoTime_${group.vId}','${group.id}')" type="button" disabled="disabled">
                	</c:when>
                	<c:otherwise>
                		<input class="btn btn-primary" value="保存" onclick="updateTraveler_qianwu('${group.vId}','visa_handleUnit_${group.vId}','traveler_visaStatus_${group.vId}','passportStatus_${group.vId}','signingTime_${group.vId}','visaDeliveryTime_${group.vId}','visaGotTime_${group.vId}','supplementaryinfoTime_${group.vId}','${group.id}')" type="button">
                	</c:otherwise>
                </c:choose>
                </td> 
              </tr>	
                  
            </c:forEach>
                 
                
                
                <!-- 订单状态描述不为空或订单同步数据出错表示订单为正向同步来订单 --> 
                
                <!-- 操作 -->
                <!--<td class="p0"><dl class="handle">
                    <dt><img title="操作" src="images/handle_cz.png"></dt>
                    <dd class="">
                      <p> <span></span> <a href="javascript:void(0)" onClick="javascript:orderDetail(1447);">详情 </a> 
                        
                        
                        
                        <a href="/a/orderCommon/manage/getOderInfoById?productorderById=1447" target="_blank">修改</a> <a href="/a/activityUpProces/list?orderId=1447&amp;productType=10&amp;flowType=10"> 改价</a> 
                        
                        
                        
                        <a href="javascript:rebatesInfo.rebatesOrder(1447,10);">返佣</a> 
                        
                        
                        
                        <a href="javascript:lockOrder(1447);">锁死</a> 
                        
                       
                        
                        <a href="javascript:void(0)" onClick="uploadData('traveler', '1447');">上传资料</a> 
                        
                 
                        
                        <a href="javascript:void(0)" onClick="uploadData('confirmation', '1447');">上传确认单</a> 
                        
                
             
                        
                        <a href="javascript:viewGroupRefund('1447','10')">退款</a> 
                        
                        
                      </p>
                    </dd>
                  </dl></td>-->
               
               
       <%--        </tr>
                  
                  <tr class="toptr">
                <td><span id="traveler_travelerName" class="sqcq-fj">
	<input type="checkbox" class="tdCheckBox" travelerid="30538" id="checkbox4" visaid="11186" travelername="11" value="30538@8493" name="activityId" onclick="t_idcheckchg(this)" style="margin-top: 5px;">1</span></td>
                
            --%>
              <tr class="checkalltd">
                <td colspan="18" class="tl">
                    <label><input type="checkbox" name="allChk" onclick="checkall(this)">全选</label>
                <label><input name="reverseChk" onclick="checkreverse(this)" type="checkbox">反选</label>
                
                
                
                <a onclick="batchUpdateVisaStatus(null)">批量操作签证</a>
                <a onclick="batchUpdatePassportStatus(null)">批量操作护照</a>
                <a href="javascript:void(0)"onclick="batchUpdatetime(null)">批量设置时间</a>
                
                
                
                <!-- 
                (签证借款--》<a href="/a/activitytest/createModelId" target="_blank">获取流程定义Id</a>
                <a href="/a/activitytest/modelDesign?mid=120001" target="_blank">根据获取Id进行流程定义</a>
                <a href="/a/activitytest/export?modelId=120001" target="_blank">导出加部署</a>)
                
                (还签证收据--》
                <a href="/a/activitytest/modelDesign?mid=395001" target="_blank">根据获取Id进行流程定义</a>
                <a href="/a/activitytest/export?modelId=395001" target="_blank">导出加部署</a>)
                 -->
                </td>
            </tr>
                 
                
                
                </tbody>
          </table>
            </div>
           <div class="pagination clearFix">${page}</div>
    
<!-- 
<SCRIPT LANGUAGE="JavaScript" src=http://float2006.tq.cn/floatcard?adminid=9557094&sort=0 ></SCRIPT>
 -->

 <!--147 批量设置时间弹窗-->

    <script type="text/javascript">
    $(function () {
    $("input[name='ids']").click(function () {
                    if ($(this).attr("checked")) {

                    } else {
                        $("input[name='allChk']").removeAttr("checked");
                    }
                });

                var resetSearchParams = function () {
                    $(':input', '#searchForm')
                            .not(':button, :submit, :reset, :hidden')
                            .val('')
                            .removeAttr('checked')
                            .removeAttr('selected');
                    $('#country').val("");
                }
                $('#contentTable').on('change', 'input[type="checkbox"]', function () {
                    if ($('#contentTable input[type="checkbox"]').length == $('#contentTable input[type="checkbox"]:checked').length) {
                        $('[name="allChk" ]').attr('checked', true);
                    } else {
                        $('[name="allChk" ]').removeAttr('checked');
                    }
                });
            });

	/* function checkall(obj) {
                if ($(obj).attr("checked")) {
                    $('#contentTable_order input[type="checkbox"]').attr("checked", 'true');
                    $("input[name='allChk']").attr("checked", 'true');
                } else {
                    $('#contentTable_order input[type="checkbox"]').removeAttr("checked");
                    $("input[name='allChk']").removeAttr("checked");
                }
            }

            function checkreverse(obj) {
                var $contentTable = $('#contentTable_order');
                $contentTable.find('input[type="checkbox"]').each(function () {
                    var $checkbox = $(this);
                    if ($checkbox.is(':checked')) {
                        $checkbox.removeAttr('checked');
                    } else {
                        $checkbox.attr('checked', true);
                    }
                });
            } */
            
          //全选
          //var activityIds = "";
          function checkall(obj){
          //debugger;
          	if(obj.checked){ 
          		$("input[name='activityId']").not("input:checked").each(function(){
          			if(this.disabled==false){
          				this.checked=true;
          			}
          		}); 
          		$("input[name='allChk']").not("input:checked").each(function(){this.checked=true;});
          	}else{ 
          		$("input[name='activityId']:checked").each(function(){this.checked=false;}); 
          		$("input[name='allChk']:checked").each(function(){this.checked=false;});	
          	} 
          }
          function checkreverse(obj){
          	$("input[name='activityId']").each(function () { 
          		if(this.disabled==false){
          				$(this).attr("checked", !$(this).attr("checked"));   
          			}  
          	}); 
          	allchk();
          }
          //每行中的复选框
          function idcheckchg(obj){
          	if(obj.checked){
          		if($("input[name='activityId']").not("input:checked").length == 0){
          			$("input[name='allChk']").not("input:checked").each(function(){this.checked=true;});
          		}
          	}else{
          		$("input[name='allChk']:checked").each(function(){
          			this.checked=false;	
          		})
          	}
          }
          function allchk(){ 
          	var chknum = $("input[name='activityId']").size(); 
          	var chk = 0; 
          	$("input[name='activityId']").each(function () {   
          		if($(this).attr("checked")==true){ 
          			chk++; 
          		} 
          	}); 
          	if(chknum==chk){//全选 
          		$("input[name='allChk']").attr("checked",true); 
          	}else{//不全选 
          		$("input[name='allChk']").attr("checked",false); 
          	} 
          }
            </script>

            <script type="text/javascript">
            
           
            //批量操作签证状态
            function batchUpdateVisaStatus(orderId){
            //标志位 判断是否有选中
            var travelerIds ="";
            var visaIds ="";
            //游客界面
            if(orderId==null){
                $("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
                    if($(this).attr("checked")){
                        var travelerId = $(this).attr("travelerId");
                        var visaId = $(this).attr("visaId");
                        	travelerIds+=travelerId+",";
                            visaIds+=visaId+",";
                    }
                });
            }else{
                $("#child_"+orderId).find("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
                    if($(this).attr("checked")){
                        var travelerId = $(this).attr("travelerId");
                        var visaId = $(this).attr("visaId");
                        	travelerIds+=travelerId+",";
                            visaIds+=visaId+",";
                    }
                });
            }
            if(travelerIds==""){
                top.$.jBox.tip('请选择游客！');
                return;
            } else {
                visaIds = visaIds.substring(0,visaIds.length-1)
            }
            batchUpdateVisaStatus1(orderId,visaIds);
        }

        function batchUpdateVisaStatus1(orderId,visaIds){
        	var html = '<div style="margin-top:20px;padding:0 10px" id="batchUpdateVisaStatus">';
			html += '<label class="jbox-label">选择签证状态：</label>';
			html += '<select id="visaStatus" name="visaStatus">';
			html += '<option value="-1">请选择</option>';
			html += '<c:forEach items="${visaStatusList}" var="visaStatus">';

			html += '<c:if test="${visaStatus.value == 2}">';
			html += '<option value="2">约签</option>';
			html += '</c:if>';

			html += '<c:if test="${visaStatus.value == 3}">';
			html += '<option value="3">出签</option>';
			html += '</c:if>';
			html += '<c:if test="${visaStatus.value != 3 and visaStatus.value != 2}">';
			html += '<option value="${visaStatus.value}">${visaStatus.label}</option>';
			html += '</c:if>';
			html += '</c:forEach>';
			html += '<select>';
			html += '</div>';

            $.jBox(html, { title: "批量操作签证状态",buttons:{'提交': 1,'取消':0}, submit:function(v, h, f){
            if (v=="1"){
            	var visaStatus=$("#batchUpdateVisaStatus").find("select[id=visaStatus]").val();
				$.ajax({
					cache: true,
					type: "POST",
					url:g_context_url+ "/grouphandle/batchUpdateVisaStatus",
					data:{ 
						"visaStatus":visaStatus,
						"visaIds":visaIds},
						async: false,
						success: function(msg){
						if(msg.msg!=null&&msg.msg!=""){
							top.$.jBox.tip(msg.msg,'warning');
						}else{
							top.$.jBox.tip("操作成功",'warning');
							showTraveler();	
						}
					}
				});
                return true;
            }
            },height:160,width:420});
        }

function clearNoNum(obj){
    obj.value = obj.value.replace(/[^-?\d.\d\d+$]/g,""); //清除"数字""-"和"."以外的字符
    obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字而不是
    obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的
    obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
    obj.value = obj.value.replace(/\-{2,}/g,"-"); //只保留第一个.- 清除多余的
    obj.value = obj.value.replace("-","$#$").replace(/\-/g,"").replace("$#$","-");
    obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
    if (obj.value.length > 1) {
        obj.value = obj.value.replace(/\-$/g,"");
    }
}

//批量操作护照状态
    function batchUpdatePassportStatus(orderId){
        //标志位 判断是否有选中
        var travelerIds ="";
        var visaIds ="";
        //游客界面
        if(orderId==null){
            $("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
                if($(this).attr("checked")){
                    var travelerId = $(this).attr("travelerId");
                    var visaId = $(this).attr("visaId");
                    travelerIds+=travelerId+",";
                        visaIds+=visaId+",";
                }
            });
        }else{
            $("#child_"+orderId).find("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
                if($(this).attr("checked")){
                    var travelerId = $(this).attr("travelerId");
                    var visaId = $(this).attr("visaId");
                    travelerIds+=travelerId+",";
                        visaIds+=visaId+",";
                }
            });
        }
        if(travelerIds==""){
            top.$.jBox.tip('请选择游客！');
            return;
        }
        //travelerIds = travelerIds.substring(0,visaIds.length-1);
        travelerIds = travelerIds;
        batchUpdatePassportStatus1(travelerIds);
    }
            
        function batchUpdatePassportStatus1(travelerIds){
        var html = '<div style="margin-top:20px;padding:0 10px" id="batchUpdatePassportStatus">';
        html += '<label class="jbox-label">选择护照状态：</label>';
        html += '<select id="passportStatus" name="passportStatus">';
        html += '<option value="0" selected="selected">请选择</option>';
        html += '<option value="1">借出</option>';
        html += '<option value="2">销售已领取</option>';
        html += '<option value="4">已还</option>';
        html += '<option value="5">已取出</option>';
        html += '<option value="6">未取出</option>';
        html += '<option value="8">计调领取</option>';
        html += '</select>';
        html += '</div>';

        $.jBox(html, { title: "批量操作护照状态",buttons:{'提交': 1,'取消':0}, submit:function(v, h, f){
            if (v=="1"){
            	var passportStatus=$("#batchUpdatePassportStatus").find("select[id=passportStatus]").val();
				$.ajax({
					cache: true,
					type: "POST",
					url:g_context_url+ "/grouphandle/batchUpdatePassportStatus",
					data:{ 
						"passportStatus":passportStatus,
						"travelerIds":travelerIds
					},
						async: false,
						success: function(msg){
						if(msg.msg!=null&&msg.msg!=""){
							top.$.jBox.tip(msg.msg,'warning');
						}else{
							top.$.jBox.tip("操作成功",'warning');
							showTraveler();
						}
					}
				});
                return true;
            }
        },height:160,width:420});
    }
        
//批量设置时间
function batchUpdatetime(orderId){
        //标志位 判断是否有选中
        var travelerIds ="";
        var visaIds ="";
        //游客界面
        if(orderId==null){
            $("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
                if($(this).attr("checked")){
                    var travelerId = $(this).attr("travelerId");
                    var visaId = $(this).attr("visaId");
                    travelerIds+=travelerId+",";
                        visaIds+=visaId+",";
                }
            });
        }else{
            $("#child_"+orderId).find("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
                if($(this).attr("checked")){
                    var travelerId = $(this).attr("travelerId");
                    var visaId = $(this).attr("visaId");
                    travelerIds+=travelerId+",";
                        visaIds+=visaId+",";
                }
            });
        }
        if(travelerIds==""){
            top.$.jBox.tip('请选择游客！');
            return;
        }else {
            visaIds = visaIds.substring(0,visaIds.length-1)
        }
        //travelerIds = travelerIds.substring(0,visaIds.length-1);
        batchUpdatetime1(orderId,visaIds);
    }
            
        function batchUpdatetime1(orderId,visaIds){
        var html = '<div style="margin:20px;padding:0 10px" id="batchUpdatetime">';
        html += '<label class="timestyle">实际签约时间：</label>';
        html += ' <input readonly="" onclick="WdatePicker()" value="" name="signingTime" class="inputTxt dateinput" >';
        html += '<label class="timestyle">送签时间：</label>';
        html += ' <input readonly="" onclick="WdatePicker()" value="" name="visaDeliveryTime" class="inputTxt dateinput" >';
        html += '<br><br/>';
        html += ' <label class="timestyle">出签时间：</label>';
        html += '<input readonly="" onclick="WdatePicker()" value=""  name="visaGotTime"  class="inputTxt dateinput" >';
       
        html += '<label class="timestyle">补资料时间：</label>';
        html += ' <input readonly="" onclick="WdatePicker()" value="" name="supplementaryinfoTime"  class="inputTxt dateinput" > ';
        html += '</div>';
       
        $.jBox(html, { title: "批量设置时间",buttons:{'提交': 1,'取消':0}, submit:function(v, h, f){
            if (v=="1"){
            	var signingTime = $("#batchUpdatetime").find("input[name=signingTime]").val();
            	var visaDeliveryTime =$("#batchUpdatetime").find("input[name=visaDeliveryTime]").val();
            	var visaGotTime = $("#batchUpdatetime").find("input[name=visaGotTime]").val();
                var supplementaryinfoTime = $("#batchUpdatetime").find("input[name=supplementaryinfoTime]").val();
                $.ajax({
                	cache:true,
                	type:"POST",
                	url:g_context_url+ "/grouphandle/batchUpdateTime",
                	data:{ 
						"visaIds" : visaIds,
						"signingTime" : signingTime,
						"visaDeliveryTime" : visaDeliveryTime,
						"visaGotTime" : visaGotTime,
						"supplementaryInfoTime" : supplementaryinfoTime
					},
						async: false,
						success: function(msg){
						if(msg.msg!=null&&msg.msg!=""){
							top.$.jBox.tip(msg.msg,'warning');
						}else{
							top.$.jBox.tip("操作成功",'warning');
							showTraveler();
						}
					}
                });
                return true;
            }
        },height:'400',width:600});
    }
     //保存按钮
     function updateTraveler_qianwu(groupId,visa_handleUnitId,traveler_visaStatusId,passportStatusId,signingTimeId,visaDeliveryTimeId,visaGotTimeId,supplementaryinfoTimeId,travlerId){
    	 var visa_handleUnitIds=$("#"+visa_handleUnitId).val();//办签单位
    	 var traveler_visaStatusIds=$("#"+traveler_visaStatusId).val();//签证状态
    	 var passportStatusIds = $("#"+passportStatusId).val();//护照状态
    	 var signingTimeIds = $("#"+signingTimeId).val();//实际约签时间
    	 var visaDeliveryTimeIds =$("#"+visaDeliveryTimeId).val();//送签时间
    	 var visaGotTimeIds = $("#"+visaGotTimeId).val();//出签时间
    	 var supplementaryinfoTimeIds = $("#"+supplementaryinfoTimeId).val();//补资料时间
    	 $.ajax({
         	cache:true,
         	type:"POST",
         	url:g_context_url+ "/grouphandle/saveGroupControlVisa",
         	data:{ 
					"visaHandleUnitId" : visa_handleUnitIds,
					"visaStatusIds" : traveler_visaStatusIds,
					"passportStatus" : passportStatusIds,
					"signingTime" : signingTimeIds,
					"visaDeliveryTime" : visaDeliveryTimeIds,
					"visaGotTime" : visaGotTimeIds,
					"supplementaryInfoTime" : supplementaryinfoTimeIds,
					"travlerId" : travlerId,
					"groupId" : groupId
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

     function showTraveler(){
          	 $("#showList").val("youke");
     
          	 $("#pageNo").val(1);
          	 $("#pageSize").val(10);
          	 $("#searchForm").submit();
          	}

        </script>
</body>
</html>
