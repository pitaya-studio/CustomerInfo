<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
    <title>财务成本录入</title>
    <meta name="decorator" content="wholesaler"/>
    <style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
    <script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
  
    <script type="text/javascript">
    
        var activityIds = "";
        $(function() {
            $( ".spinner" ).spinner({
                spin: function( event, ui ) {
                if ( ui.value > 365 ) {
                    $( this ).spinner( "value", 1 );
                    return false;
                } else if ( ui.value < 0 ) {
                    $( this ).spinner( "value", 365 );
                    return false;
                }
                }
            });
            $(".qtip").tooltip({
                track: true
            });
            
            $("#targetAreaId").val("${travelActivity.targetAreaIds}");
            $("#targetAreaName").val("${travelActivity.targetAreaNamess}");
            
            //展开筛选按钮
			$('.zksx').click(function(){
				if($('.ydxbd').is(":hidden")==true)
				{
					$('.ydxbd').show();
					$(this).text('收起筛选');
					$(this).addClass('zksx-on');
				}else
				{
					$('.ydxbd').hide();
					$(this).text('展开筛选');
					$(this).removeClass('zksx-on');
				}
			});
			//如果展开部分有查询条件的话，默认展开，否则收起	
			var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
			var searchFormselect = $("#searchForm").find("select");
			var inputRequest = false;
			var selectRequest = false;
			for(var i = 0; i<searchFormInput.length; i++) {
				if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
					inputRequest = true;
				}
			}
			for(var i = 0; i<searchFormselect.length; i++) {
				if($(searchFormselect[i]).children("option:selected").val() != "" && 
						$(searchFormselect[i]).children("option:selected").val() != "100" &&
						$(searchFormselect[i]).children("option:selected").val() != null) {
					selectRequest = true;
				}
			}
			if(inputRequest||selectRequest) {
				$('.zksx').click();
			}
            
            $("#cost").attr("class", "active");
            activityIds = "${activityIds}";
            $("#groupform").validate({});
            jQuery.extend(jQuery.validator.messages, {
                required: "必填信息"
            });
            
            //产品名称获得焦点显示隐藏
            $("#wholeSalerKey").focusin(function(){
                var obj_this = $(this);
                    obj_this.next("span").hide();
            }).focusout(function(){
                var obj_this = $(this);
                if(obj_this.val()!="") {
                obj_this.next("span").hide();
            }else
                obj_this.next("span").show();
            });
            if($("#wholeSalerKey").val()!="") {
                $("#wholeSalerKey").next("span").hide();
            }
            
            
            var _$orderBy = $("#orderBy").val();
            if(_$orderBy==""){
                _$orderBy="id DESC";
            }
            var orderBy = _$orderBy.split(" ");
            $(".activitylist_paixu_left li").each(function(){
                if ($(this).hasClass("li"+orderBy[0])){
                    orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
                    $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
                    $(this).attr("class","activitylist_paixu_moren");
                }
            });
           $('.team_top').find('.table_activity_scroll').each(function(index, element) {
             var _gg=$(this).find('tr').length;
            if(_gg>=20){
            $(this).addClass("group_h_scroll");}
          });
           
           
           $('.nav-tabs li').hover(function(){
        	    $('.nav-tabs li').removeClass('current');
        	    $(this).parent().removeClass('nav_current');
        	     if($(this).hasClass('ernav'))
        	     {
        	         if(!$(this).hasClass('current')){
        	            $(this).addClass('current');
        	            $(this).parent().addClass('nav_current');
        	         }
        	     }
        	    },function(){
        	        $('.nav-tabs li').removeClass('current');
        	        $(this).parent().removeClass('nav_current');
        	        var _active = $(".totalnav .active").eq(0);
        	        if(_active.hasClass('ernav')){
        	            _active.addClass('current');
        	            $(this).parent().addClass('nav_current');
        	        }
        	    });
           
           $("a[name=cost-del-name]").click(function(){
				var gid = $(this).attr("groupid");
				deleteCost(gid,this);
				return false;
			});
        });
        
        function expand(child,obj,srcActivityId){
            if($(child).is(":hidden")){
                
                if("${userType}"=="1") {
                    $.ajax({
                        type:"POST",
                        url:"${ctx}/stock/manager/payReservePosition",
                        data:{
                            srcActivityId:srcActivityId
                        },
                        success:function(msg) {
                            $(obj).html("关闭查看");
                            $(child).show();
                            $(obj).parents("td").attr("class","td-extend");
                            if(msg.length>0){
                                $(child+" [class^='soldPayPosition']").show();
                            }
                            $.each(msg,function(keyin,valuein){
                                $("td .soldPayPosition"+(valuein.activityGroupId)).find("span").text(valuein.leftpayReservePosition);
                            });
                        }
                    });
                }else{
                    $(obj).html("关闭成本");
                    $(child).show();
                    $(obj).parents("td").attr("class","td-extend");
                }
            }else{
                if(!$(child).is(":hidden")){           
                    $(child).hide();
                    $(obj).parents("td").attr("class","");
                    $(obj).html("查看成本");
                }
            }
        }
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").attr("action","${ctx}/cost/manager/list/${type}");
            $("#searchForm").submit();
        }

        $(function(){
            $('.team_a_click').toggle(function(){
                $(this).addClass('team_a_click2');
            },function(){
                $(this).removeClass('team_a_click2');
            }); 
         });
        
        function getCurDate(){
            var curDate = new Date();
            return curDate.getFullYear()+"-"+(curDate.getMonth()+1)+"-"+(curDate.getDate()+1);
        }
      //控制截团时间  
        function takeOrderOpenDate(obj){
            var groupOD = $(obj).parent().prev().children("input[name='groupOpenDate']").val();
            return groupOD;
        }
        function takeModVisaDate(obj) {
            var groupOD = $(obj).parent().parent().children().first().children("input[name='groupOpenDate']").val();
            return groupOD;
        }
        function comparePositionMod(obj){
            var plan = $(obj).val();
            $(obj).parent().next().find("input").val(plan);
            $(obj).parent().next().find("input").focus();
            $(obj).parent().next().find("input").blur();
        }
        
        function sortby(sortBy,obj){
            var temporderBy = $("#orderBy").val();
            if(temporderBy.match(sortBy)){
                sortBy = temporderBy;
                if(sortBy.match(/ASC/g)){
                    sortBy = sortBy.replace(/ASC/g,"");
                }else{
                    sortBy = $.trim(sortBy)+" ASC";
                }
            }
            
            $("#orderBy").val(sortBy);
            $("#searchForm").submit();
        }

        function groupDetail(url) {
        	window.open(encodeURI(encodeURI(url)));
        }
        
        var deleteCost = function(id,$this){
            $.jBox.confirm("确定要删除所有的成本数据吗？", "提示", function(v, h, f) {
                if (v == 'ok') {
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/cost/manager/delByGroup",
                        cache:false,
                        async:false,
                        data:{gid : id},
                        success: function(e){
                            if(e == 'true'){
                               $($this).parent("td[name=cost-manager-name]").find("a[name=cost-add-name]").html("成本录入");
                               $($this).parent("td[name=cost-manager-name]").children(":not(a[name=cost-add-name])").remove();
                               window.location.reload();
                            }else{
                                top.$.jBox.tip('请求失败。','error');
                                return false;
                            }
                            
                        },
                        error : function(e){
                            top.$.jBox.tip('请求失败。','error');
                            return false;
                        }
                     });
                }
            });
            
          }
    </script>

</head>
<body>
    <%@ include file="head_finance.jsp" %>
    <div class="activitylist_bodyer_right_team_co_bgcolor inventory_management">
    <form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/cost/manager/list/${type}" method="post" >
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
         <div class="activitylist_bodyer_right_team_co">
         
        	<div class="activitylist_bodyer_right_team_co2 pr wpr20">
	            <label>产品名称：</label><input style="width:260px" class="txtPro inputTxt" id="wholeSalerKey" name="wholeSalerKey" value="${travelActivity.acitivityName }"/>
	            <span class="ipt-tips" style="display: block;">支持产品名称、目的地全称、简称的搜索</span>
         	</div>
            
      		<div class="form_submit">
				<input class="btn btn-primary" type="submit" value="搜索"/>
			</div>
			<div class="zksx">筛选</div>
			<div class="ydxbd" >
              <div class="activitylist_bodyer_right_team_co3">
            <div class="activitylist_team_co3_text">产品系列：</div><form:select path="activityLevelId" itemValue="key" itemLabel="value">
               <form:option value="">不限</form:option>
               <form:options items="${productLevels}" />
            </form:select>
            </div>
            <div class="activitylist_bodyer_right_team_co3">  
                  <div class="activitylist_team_co3_text">目的地：</div>
                 <tags:treeselect id="targetArea" name="targetAreaIdList" value="${targetAreaIds}" labelName="targetAreaNameList"  labelValue="${targetAreaNames}"
                     title="区域" url="/activity/manager/filterTreeData" checked="true"/>
            </div>
            <div class="activitylist_bodyer_right_team_co2">
                <label>价格范围：</label><input id="settlementAdultPriceStart" maxlength="7" class="rmb inputTxt" name="settlementAdultPriceStart" value="${settlementAdultPriceStart }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
                <span style="font-size:12px;font-family:'宋体';"> 至</span> 
                <input id="settlementAdultPriceEnd" maxlength="7" class="rmb inputTxt" name="settlementAdultPriceEnd" value="${settlementAdultPriceEnd }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
            </div>
             <div class="kong"></div>
              <div class="activitylist_bodyer_right_team_co3">
            <div class="activitylist_team_co3_text">产品类型：</div><form:select path="activityTypeId" itemValue="key" itemLabel="value">
               <form:option value="">不限</form:option>
               <form:options items="${productTypes}" />
            </form:select>
            </div>
                <div class="activitylist_bodyer_right_team_co3">
                    <div class="activitylist_team_co3_text">旅游类型：</div><form:select path="travelTypeId" itemValue="key" itemLabel="value">
               <form:option value="">不限</form:option>
               <form:options items="${travelTypes}" />
            </form:select>
          </div>
        	<div class="activitylist_bodyer_right_team_co2">
                <label>出团日期：</label>
                <input id="groupOpenDate" class="inputTxt dateinput" name="groupOpenDateString" value='<fmt:formatDate value="${travelActivity.groupOpenDate }" pattern="yyyy-MM-dd"/>' style="margin-left: -3px; width: 122px;" onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" readonly /> 
                <span style="font-size:12px; font-family:'宋体';"> 至</span>  
                <input id="groupCloseDate" class="inputTxt dateinput" name="groupCloseDateString" value='<fmt:formatDate value="${travelActivity.groupCloseDate }" pattern="yyyy-MM-dd"/>' style="width: 122px;" onClick="WdatePicker()" readonly/>
            </div>
            </div>
          <div class="kong"></div>
         </div>
    </form:form>
    <div class="activitylist_bodyer_right_team_co_paixu">
        <div class="activitylist_paixu">
          <div class="activitylist_paixu_left">
             <ul>
            <li style="width:50px;border:none; background-color:#f5f5f5; height:28px; line-height:28px;">排序</li>
            <li class="activitylist_paixu_left_biankuang liid"><a onClick="sortby('id',this)">创建时间</a></li>
            <li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
            <li class="activitylist_paixu_left_biankuang lisettlementAdultPrice"><a onClick="sortby('settlementAdultPrice',this)">同行价格</a></li>
            <li class="activitylist_paixu_left_biankuang lisuggestAdultPrice"><a onClick="sortby('suggestAdultPrice',this)">直客价格</a></li>
            <li class="activitylist_paixu_left_biankuang ligroupOpenDate"><a onClick="sortby('groupOpenDate',this)">出团日期</a></li>
            </ul>
      </div>
          <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
          <div class="kong"></div>
        </div>
      </div>
    <form id="groupform" name="groupform" action="" method="post" >
    <table id="contentTable" class="table activitylist_bodyer_table" >
        <thead >
            <tr>
                <th width="5%">序号</th>
                <th width="8%">产品系列</th>
                <th width="8%">出发地</th>
                <th width="8%">目的地</th>
                <th width="15%">产品名称</th>
                <th width="8%">切位总数</th>
                <th width="8%">余位总数</th>
                <th width="8%">预收总数</th>
                <th width="16%">操作</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${page.list}" var="activity" varStatus="s">
            <c:set var="groupsize" value="${fn:length(activity.activityGroups)}"></c:set>
                <tr id="parent${s.count}">
                    <td>${s.count}<br /><br /></td>
                    <td>${activity.activityLevelName}</td>
                    <td>${activity.fromAreaName}</td>
                    <td>${activity.targetAreaNames}</td>
                    <td style="color:#3a7850;"><a href="${ctx}/activity/manager/detail/${activity.id}" target="_blank">${activity.acitivityName}</a></td>

                    <td class="tr">${activity.reservePosition}</td>
                    <td class="tr">${activity.freePosition}</td>
                    <td class="tr">${activity.planPosition}</td>
                    <td id="groupdate${activity.id }" align="center" class="">  
                    <c:if test="${groupsize ne 0 }">
                        <span>${activity.groupOpenDate}至${activity.groupCloseDate}</span><br>
                        <a id="close${s.count}" href="javascript:void(0)" class="team_a_click" onClick="expand('#child${s.count}',this,${activity.id })">查看成本</a>
                    </c:if>
                    <c:if test="${groupsize == 0 }">
                                                                            日期待定
                    </c:if>                                        
                    </td>             
                </tr>
                <tr id="child${s.count}" style="display:none" class="activity_team_top1">
                      <td colspan="9" class="team_top" style="background-color:#dde7ef;">
                        <table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;">
                            <thead>
                                <tr>
                                    <th width="10%">团号</th>
                                    <th width="8%">出/截团日期</th>
                                    <th width="8%">预收</th>
                                    <th width="8%">余位</th>
                                    <th width="8%">操作</th>
                                </tr>
                            </thead>
                        </table>
                        <div class="table_activity_scroll">
                            <table class="table activitylist_bodyer_table ">
                                <tbody>
                                    <c:forEach items="${activity.activityGroups}" var="group" varStatus="s2">
                                        <tr>
                                            <td width="10%"><a target="_blank" href="${ctx}/stock/manager/groupDetail?groupCode=${group.groupCode }&srcActivityId=${group.srcActivityId }">${group.groupCode}</a></td>
                                            <td width="8%" style="padding: 0px;">
                                            	<div class="out-date">
	                                                <span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/></span>
	                                                <input type="text" id="groupid${s.count}${s2.count}" name="groupid" value="${group.id}" style="display:none;margin-bottom:0;" disabled="disabled"/>
	                                                <input style="display:none;width:75px;margin-bottom:0" disabled="disabled" type="text" name="groupOpenDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/>" onClick="WdatePicker({minDate:getCurDate()})" class="required inputTxt"/>
	                                                <input type="hidden" class="srcActivityId" value="${group.srcActivityId}"/>
												</div>
                                            	<div class="close-date">
                                            		<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupCloseDate }"/></span>
                                           		</div>
                                           	</td>
                                            <td width="8%" class="tr"><span>${group.planPosition } </span></td>
                                            <td width="8%" class="tr"><span>${group.freePosition }</span></td>
                                            <td width="8%">
                                              <shiro:hasPermission name="cost:finance:edit">
                                                  <c:choose>
                                                     <c:when test="${group.costStatus eq fnc:getState(\"start\")}">
                                                     	 <a target="_blank" href="${ctx}/cost/manager/flow/financeCostDetail/${activity.id}/${group.id}?from=${type}">详情</a><span style="color:#696969;">&nbsp;&nbsp;（${fnc:getStateDescByNum(group.costStatus)}）</span>                                                         
                                                     </c:when>
                                                     <c:when test="${group.costStatus eq fnc:getState(\"financeAccepted\")}">
                                                     	<a target="_blank" href="${ctx}/cost/manager/flow/financeAccepted/${activity.id}/${group.id}?from=${type}">成本录入</a>
                                                     </c:when>                                                                                                         
                                                     <c:otherwise>
                                                         <shiro:hasPermission name="cost:finance:view"> 
                                                         	<a target="_blank" href="${ctx}/cost/manager/flow/financeCostDetail/${activity.id}/${group.id}?from=${type}">详情</a><span style="color:#696969;">&nbsp;&nbsp;（${fnc:getStateDescByNum(group.costStatus)}）</span><br>
                                                              <c:choose>
	                                                              <c:when test="${group.costStatus eq fnc:getState(\"financeRollback\") || group.costStatus eq fnc:getState(\"operatorFinanceRollback\")}">
			                                                         <a target="_blank" name="cost-edit-name"  href="${ctx}/cost/manager/flow/financeRollback/${activity.id}/${group.id}?from=${type}" >重新修改</a><span style="color:#eb0205;">（已驳回）</span>
			                                                     </c:when>
			                                                     <c:when test="${group.costStatus eq fnc:getState(\"financeAcceptedInOperatorFinanceRollback\")}">
			                                                         <a target="_blank" name="cost-edit-name"  href="${ctx}/cost/manager/flow/financeRollback/${activity.id}/${group.id}?from=${type}" >重新修改</a><span style="color:#eb0205;">（已驳回）</span>
			                                                     </c:when>
			                                                      <c:when test="${group.costStatus eq fnc:getState(\"financeAcceptedInOperatorFinanceRollback\")}">
			                                                      </c:when>
			                                                     <c:otherwise>
			                                                     	<c:if test="${group.costStatus != fnc:getState(\"directorCommited\") }">
		                                                         		<a target="_blank" name="cost-edit-name" href="${ctx}/cost/manager/flow/financeAccepted/${activity.id}/${group.id}?from=${type}">修改</a>
		                                                         	</c:if>
		                                                         </c:otherwise>
	                                                         </c:choose>
	                                                         <c:if test="${group.costStatus eq fnc:getState(\"directorCommited\") }">
	                                                         	<a target="_blank" name="cost-add-name" href="${ctx}/cost/manager/flow/financeAccepted/${activity.id}/${group.id}?from=${type}">2次录入</a>
	                                                         </c:if>	                                                         
	                                                         <a name="cost-del-name" groupid="${group.id}">删除</a>
                                                         	
                                                         </shiro:hasPermission>
                                                     </c:otherwise>
                                                  </c:choose>
                                              </shiro:hasPermission>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                             </table>
                         </div>
                     </td>
                 </tr>
            </c:forEach>
        </tbody>
    </table>
    </form>
    </div>
       <div class="page">
            <div class="pagination">
                <div class="endPage">${page}</div>
                <div style="clear:both;"></div>
            </div>  
        </div>
        <br/>
    </body>
</html>