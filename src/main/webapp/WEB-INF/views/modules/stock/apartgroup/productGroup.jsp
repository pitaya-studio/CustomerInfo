<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>产品团期列表</title>
    <meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <link href="${ctxStatic}/css/bootstrap.css" type="text/css" rel="stylesheet"/>
    <link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet"/>
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/jh-style.css" type="text/css" rel="stylesheet"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/huanqiu-style.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
    <style>
        .p00 .handle a{
            width: 50px;
            margin-left: auto;
            margin-right: auto
        }
    </style>
    <script type="text/javascript">
	   $(function() { 
		   
		     var _$orderBy = $("#orderBy").val();
	         var orderBy = _$orderBy.split(" ");
	         $(".activitylist_paixu_left li").each(function(){
	             if ($(this).hasClass("li"+orderBy[0])){
	                 orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
	                 $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
	                 $(this).attr("class","activitylist_paixu_moren");
	             }
	         });
	         
	        $("#wholeSalerKey").focusin(function(){
	 			var obj_this = $(this);
	 			obj_this.next("span").hide();
	 		}).focusout(function(){var obj_this = $(this);
	 		if(obj_this.val()!="") {
	 			obj_this.next("span").hide();
	 		}else
	 			obj_this.next("span").show();
	 		});
	 		if($("#wholeSalerKey").val()!="") {
	 			$("#wholeSalerKey").next("span").hide();
	 		}; 
	 		//目的地查询条件显示
	 		$("#targetAreaId").val($("#tempTargetAreaIds").val());
	 		$("#targetAreaName").val($("#tempTargetAreaNamess").val());
	 		
	 		//初始时已选择团期置灰
            setGroupState();
	   });
     
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
            $("#groupform").submit();
 	    };
 	    
        function CurentTime(){
            return new Date();
        }
        $(document).on('change','[name="checkAll"]',function(){
            if($(this).attr('checked')){
                $(this).parents('#contentTable').find('input[type="checkbox"]:enabled').attr('checked',true);
            }else{
                $(this).parents('#contentTable').find('input[type="checkbox"]:enabled').attr('checked',false);
            }
        });
        //重置查询
        var resetSearchParams = function(){
            $(':input','#groupform')
        		.not(':button, :submit, :reset, :hidden')
        		.val('')
        		.removeAttr('checked')
        		.removeAttr('selected');
            $('#targetAreaId').val('');
            $('#activityCreate').val('');
            $('#proCreateBy').val('');
            $('#orderShowType').val('${showType}');
        }
        
		function setGroupState(){
            //若列表中数据已被添加,则勾选并置灰
            var selectedProductArr = '${selectedProducts}';
            if(!selectedProductArr){
                return ;
            }
            var selectedProducts = selectedProductArr.split(',');
            $('[name="groupRow"]').each(function(){
                for(var i= 0,len=selectedProducts.length;i<len;i++){
                    var $tds = $(this).children('td');
                    if(selectedProducts[i]==$tds.eq(9).find("input[name=id]").val()){
                        $tds.eq(0).children('input').attr('checked','true').attr('disabled','disabled');
                    }
                }
            });
        }
        
    </script>
</head>
<body>
<form novalidate="novalidate" id="groupform" name="groupform" action="${ctx}/stock/manager/apartGroup/getProductLooseList" method="post">
<div class="activitylist_bodyer_right_team_co">
	<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
	<input type="hidden" name="selectedProducts" value="${selectedProducts }" />
	<input type="hidden" name="source" value="${source }" />
	<input type="hidden" name="agentId" value="${agentId }">
    <div class="activitylist_bodyer_right_team_co2 pr wpr20">
        <label>搜索：</label>
        <input style="width:260px;" class="txtPro inputTxt inquiry_left_text" id="wholeSalerKey" name="wholeSalerKey" value="${travelActivity.acitivityName }">
        <span class="ipt-tips" style="display: block;">输入产品名称、团号，支持模糊匹配</span>
    </div>
    <div class="form_submit">
        <input class="btn btn-primary" value="搜索" type="submit" name="search">&nbsp;
        <input class="btn btn-primary" value="重置查询" onclick="resetSearchParams();" type="button">
    </div>
    <div style="display: block;" class="ydxbd">
        <div class="activitylist_bodyer_right_team_co2">
            <label style="width: 60px">出团日期：</label>
            <input id="groupOpenDateStart" class="inputTxt dateinput" name="groupOpenDateStart" value="${searchParam.groupOpenDateStart }" style="margin-left: -3px; width: 122px;" onfocus="WdatePicker()" readonly="">
            <span style="font-size:12px; font-family:'宋体';"> 至</span>
            <input id="groupOpenDateEnd" class="inputTxt dateinput" name="groupOpenDateEnd" value="${searchParam.groupOpenDateEnd }" style="width: 122px;" onclick="WdatePicker()" readonly="">
        </div>
        <div class="activitylist_bodyer_right_team_co2">
            <label>截团日期：</label>
            <input id="groupCloseDateStart" class="inputTxt dateinput" name="groupCloseDateStart" value="${searchParam.groupCloseDateStart }" style="margin-left: -3px; width: 122px;" onfocus="WdatePicker()" readonly="">
            <span style="font-size:12px; font-family:'宋体';"> 至</span>
            <input id="groupCloseDateEnd" class="inputTxt dateinput" name="groupCloseDateEnd" value="${searchParam.groupCloseDateEnd }" style="width: 122px;" onclick="WdatePicker()" readonly="">
        </div>
        <div class="kong" style="margin:5px 0px"></div>
        <div class="activitylist_bodyer_right_team_co2">
            <label style="width: 60px">价格范围：</label>
            <input id="settlementAdultPriceStart" maxlength="7" class="rmb inputTxt" name="settlementAdultPriceStart" value="${searchParam.settlementAdultPriceStart }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
            <span style="font-size:12px;font-family:'宋体';"> 至</span>
            <input id="settlementAdultPriceEnd" maxlength="7" class="rmb inputTxt" name="settlementAdultPriceEnd" value="${searchParam.settlementAdultPriceEnd }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
        </div>
        <div class="activitylist_bodyer_right_team_co2">
        	<!-- 切位时取余位数，归还切位时取切位数 -->
            <label><c:if test="${'isReserve' == source }">余位数</c:if><c:if test="${'isReturn' == source }">切位数</c:if>：</label>
            <input id="freePositionStart" maxlength="7" class="inputTxt" name="freePositionStart" value="${searchParam.freePositionStart }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
            <span style="font-size:12px;font-family:'宋体';"> 至</span>
            <input id="freePositionEnd" maxlength="7" class="inputTxt" name="freePositionEnd" value="${searchParam.freePositionEnd }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
        </div>
        <div class="mod_information_d2">
       		<label>目的地：</label>
       		<%-- <input type="hidden" value="${travelActivity.targetAreaIds}" id="targetAreaIds">
			<input type="hidden" value="${travelActivity.targetAreaNamess}" id="targetAreaNames">
            <tags:treeselect id="targetArea" name="targetAreaIdList" value="${targetAreaIds}"  labelName="targetAreaNameList" labelValue="${targetAreaNames}"  
               title="区域" url="/activity/manager/filterTreeData" checked="true"/> --%>
            <input type="hidden" value="${travelActivity.targetAreaIds}" id="tempTargetAreaIds">
			<input type="hidden" value="${travelActivity.targetAreaNamess}" id="tempTargetAreaNamess">
			<tags:treeselect id="targetArea" name="targetAreaIdList" value="${targetAreaIds}" labelName="targetAreaNameList"  labelValue="${targetAreaNames}"
                     title="区域" url="/activity/manager/filterTreeData" checked="true"/>
        </div>
    </div>
</div>
</form>
<c:choose>
<c:when test="${empty page }">
</c:when>
<c:when test="${empty page.list }">
	<div class="activitylist_bodyer_right_team_co_paixu" >
		    <div class="activitylist_paixu">
		        <div class="activitylist_paixu_left">
		            <ul>
		                <li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
		                <li class="activitylist_paixu_left_biankuang licreateDate">
		                    <a onclick="sortby('createDate',this)">创建时间</a>
		                </li>
		                <li class="activitylist_paixu_left_biankuang liupdateDate">
		                    <a onclick="sortby('updateDate',this)">更新时间</a>
		                </li>
		                <li class="activitylist_paixu_left_biankuang lisettlementAdultPrice">
		                    <a onclick="sortby('settlementAdultPrice',this)">同行价格</a>
		                </li>
		                <li class="activitylist_paixu_left_biankuang lisuggestAdultPrice">
		                    <a onclick="sortby('suggestAdultPrice',this)">直客价格</a>
		                </li>
		                <li class="activitylist_paixu_left_biankuang ligroupOpenDate">
		                    <a onclick="sortby('groupOpenDate',this)">出团日期</a>
		                </li>
		            </ul>
		        </div>
		        <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${fn:length(page.list)}</strong>&nbsp;条</div>
		        <div class="kong"></div>
		    </div>
		</div>
	    <table id="contentTable" class="table activitylist_bodyer_table" >
	        <thead>
	        <tr>
	            <th width="9%"><input name="checkAll" value="" type="checkbox" style="width: auto;">序号</th>
	            <th width="10%">团号</th>
	            <th width="9%">产品名称</th>
	            <th width="9%">目的地</th>
	            <th width="9%">出团日期</th>
	            <th width="9%">截团日期</th>
	            <th width="9%">余位数</th>
	            <th width="9%">切位数</th>
	            <th width="9%">预收数</th>
	            <th width="9%">操作</th>
	        </tr>
	        </thead>
	        <tbody>
		       <tr class="toptr">
					<td colspan="10" style="text-align: center;">暂无相关信息</td>
				</tr>
	        </tbody>
	    </table>
</c:when>
<c:otherwise>
	<div class="activitylist_bodyer_right_team_co_paixu" >
	    <div class="activitylist_paixu">
	        <div class="activitylist_paixu_left">
	            <ul>
	                <li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
	                <li class="activitylist_paixu_left_biankuang licreateDate">
	                    <a onclick="sortby('createDate',this)">创建时间</a>
	                </li>
	                <li class="activitylist_paixu_left_biankuang liupdateDate">
	                    <a onclick="sortby('updateDate',this)">更新时间</a>
	                </li>
	                <li class="activitylist_paixu_left_biankuang lisettlementAdultPrice">
	                    <a onclick="sortby('settlementAdultPrice',this)">同行价格</a>
	                </li>
	                <li class="activitylist_paixu_left_biankuang lisuggestAdultPrice">
	                    <a onclick="sortby('suggestAdultPrice',this)">直客价格</a>
	                </li>
	                <li class="activitylist_paixu_left_biankuang ligroupOpenDate">
	                    <a onclick="sortby('groupOpenDate',this)">出团日期</a>
	                </li>
	            </ul>
	        </div>
	        <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${fn:length(page.list)}</strong>&nbsp;条</div>
	        <div class="kong"></div>
	    </div>
	</div>
    <table id="contentTable" class="table activitylist_bodyer_table" >
        <thead>
        <tr>
            <th width="9%"><input name="checkAll" value="" type="checkbox" style="width: auto;">序号</th>
            <th width="10%">团号</th>
            <th width="9%">产品名称</th>
            <th width="9%">目的地</th>
            <th width="9%">出团日期</th>
            <th width="9%">截团日期</th>
            <th width="9%">余位数</th>
            <th width="9%">切位数</th>
            <th width="9%">预收数</th>
            <th width="9%">操作</th>
        </tr>
        </thead>
        <tbody>
	        <c:forEach items="${page.list}" var="activity" varStatus="s">
		        <tr id="parent${s.count}" name="groupRow">
		            <td class="tc"><input type="checkbox">${s.count}</td>
		            <td class="tc">${activity.groupCode}</td>
		            <td class="tc">${activity.acitivityName}</td>
		            <td>
		            	<c:forEach items="${fns:findTargetAreaById(activity.srcActivityId)}" var="targetArea" varStatus="t">
                      <c:if test="${t.count==1}">                      
                        <c:set var="areaName" value="${targetArea.name} "></c:set>                     
                     </c:if>
                      <c:if test="${t.count>1}">                      
                        <c:set var="areaName" value="${areaName }, ${targetArea.name} "></c:set>                        
                     </c:if>
                      </c:forEach>                     
                     <div id="area${s.count}" 
                     title="${areaName }"
                     style="overflow: hidden;text-overflow: ellipsis;white-space: nowrap;max-width: 150px;">
                     <c:forEach items="${fns:findTargetAreaById(activity.srcActivityId)}" var="targetArea" varStatus="t">
                      <c:if test="${ t.count==1}">                      
                        ${targetArea.name}                     
                     </c:if>
                      <c:if test="${  t.count>1 && t.count<=3 }">                      
                        , ${targetArea.name}                       
                     </c:if>
                      <c:if test="${t.count==4}">&nbsp;.......</c:if>
                      </c:forEach>                
                     </div>                 
		            </td>
		            <td><fmt:formatDate value="${activity.groupOpenDate}" pattern="yyyy-MM-dd"/></td>
		            <td><fmt:formatDate value="${activity.groupCloseDate}" pattern="yyyy-MM-dd" /></td>
		             <td>${activity.freePosition}</td>
                    <td>${activity.payReservePosition}</td>
                    <td>${activity.planPosition}</td>
		            <td class="p00">
		                <dl class="handle">
		                    <a href="${ctx}/stock/manager/apartGroup/detail?id=${activity.srcActivityId}&agentId=${param.agentId }&activityGroupId=${activity.id}&showReserve=0" target="_blank">库存详情</a>
		                </dl>
		                <input type="hidden" value="${activity.id }" name="id" />
		                <input type="hidden" value="${fns:getCurrencyInfo(activity.currencyType,0,'mark')}<fmt:formatNumber value="${activity.agpsettlementAdultPrice }" pattern="#,##0.00"/>" name="agpsettlementAdultPrice" />
		                <input type="hidden" value="${fns:getCurrencyInfo(activity.currencyType,1,'mark')}<fmt:formatNumber value="${activity.agpsettlementcChildPrice }" pattern="#,##0.00"/>" name="agpsettlementcChildPrice" />
		                <input type="hidden" value="${fns:getCurrencyInfo(activity.currencyType,2,'mark')}<fmt:formatNumber value="${activity.agpsettlementSpecialPrice }" pattern="#,##0.00"/>" name="agpsettlementSpecialPrice" />
		                <input type="hidden" value="${fns:getCurrencyInfo(activity.currencyType,7,'mark')}<fmt:formatNumber value="${activity.singleDiff }" pattern="#,##0.00"/>" name="singleDiff" />
		                <input type="hidden" value="${activity.soldPayPosition }" name="soldPayPosition">
		            </td>
		        </tr>
	        </c:forEach>
        </tbody>
    </table>
    </c:otherwise>
</c:choose>	
</body>
</html>