<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>库存详情</title>
    <meta name="decorator" content="wholesaler"/>
    <style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/flexbox-0.9.6/FlexBox/css/jquery.flexbox.css" />
    <script src="${ctxStatic}/jquery-other/jquery-ui-1.8.10.custom.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactorStock.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/flexbox-0.9.6/FlexBox/js/jquery.flexbox.js"></script>
	<script type="text/javascript">
    function showAll(){
        $(".grouprow").each(function(){
        	$(this).show();
        });
    }
    function getChickList(){
        var dateArr = new Array();
        var date;
        var index=0;
         <c:forEach items="${travelActivity.activityGroups}" var="group" varStatus="status">
            <c:if test="${group.leftdays < 0 }">
                date = '<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/>'.split("-");
                dateArr[index] = new Date(date[0],date[1]-1,date[2]);
                index++;
            </c:if>
        </c:forEach>
        return dateArr;
    }
	  $().ready(function(){
		  
		   if("${activityGroupId}"){
              $(".team_a_click").hide();
              $(".team_a_click").next("img").hide();    
              var cloneTbody = $("#contentTable").clone();
              $("#contentTableclone").append(cloneTbody);
              $("#contentTableclone").attr("width","100%");
              $("#contentTableclone").find("tr [class!='1']").remove();
              $("#contentTableclone").find("thead").remove();
              $("#contentTableclone").find("#contentTable").remove();
              $("#contentTableclone").find("tbody").remove();
          }
		  
          var datepicker= $(".groupDate").datepickerRefactor({dateFormat: "yy-mm-dd",target:"#dateList",numberOfMonths: 3,isChickArr:getChickList(),defaultDate:'${travelActivity.groupOpenDate }'},"#groupOpenDate","#groupCloseDate");
        	var thisClass = "";
        	$(".team_a_click").click(function(){
        		thisClass = $(this).attr("class").substring(12,19);
        		if($(this).parent().hasClass('td-extend')){
                    $(this).parent().attr("class",thisClass + " tr");
                    $(this).next().show();
                }else{
                    var objs=$(this).parents("tr").find("[class^='td-extend']");
                    if(objs.length>0){
                        objs.removeClass('td-extend');
                        objs.find(".team_a_click").next().show();
                    }
                    $(this).parent().attr("class","td-extend tr "+thisClass);
                    $(this).next().hide();
                }
        	});
        })
		function expand(current,obj,child,child1,child2){
			$(child).hide();
           	$(child1).hide();
           	$(child2).hide();
			if("${param.activityGroupId}"){
					$(current).show();        			
        		}else{
        			$(current).toggle();
        		}
           //	if("${param.activityGroupId}"){
				$("#contentTableclone").append($(current));  
				$(".cDetail-ul").find(".cDetail-ul-active").attr("class","");
				$(obj).parent().attr("class","cDetail-ul-active");
			//}
		}
        //ajax
        //ajax
        function soldNopayPosition(current,obj,child,child1,child2,productGroupId){
            $(child).hide();
            $(child1).hide();
            $(child2).hide();
            var trs = $(current).find("tbody").find("tr");     
            if(trs.length!=0){
                if(productGroupId){
                    $(current).show();                  
                }else{
                    $(current).toggle();
                }
            }           
            else{
                var noCache = Date();
                $.ajaxSettings.async = false; 
                $.getJSON("${ctx}/stock/manager/apartGroup/soldNopayPosition/"+productGroupId,{"noCache":noCache},
                    function(result){
                        if(result.length==0){
                        	$(current).find("tbody").empty();
                            var tr = $("<tr></tr>");
                            $(tr).append("<td class='tc' colspan=\"10\">没有记录</td>");
                            if(productGroupId){
                                $("#contentTableclone").append($(current));  
                            }
                            $(current).find("tbody").append(tr);
                            $(current).show();
                        }else{
                            var numFlag = "";
                            var numCount = 0;
                            var tr;
                            $(current).find("tbody").empty();
                            $(result).each(function(index,obj){
                                if(numFlag == "")
                                    numFlag = obj.orderCompany;
                                if(obj.orderCompany != numFlag){
                                    var str = $(current).find("tbody").find("tr")[index-numCount];
                                    var td1 = $(str).find("td")[0];
                                    var td2 = $(str).find("td")[1];
                                    var td3 = $(str).find("td:eq(2)");
                                    $(td1).attr("rowspan",numCount);
                                    $(td2).attr("rowspan",numCount);
                                    $(td3).attr("rowspan",numCount);
                                    numFlag = obj.orderCompany;
                                    numCount = 0;
                                }
                                tr = $("<tr></tr>");
                                if(numCount==0){
                                    $(tr).append("<td class='tc'>"+obj.agentName+"</td>")
                                        .append("<td  class='tc'>"+obj.payReservePosition+"</td>")
                                        .append("<td  class='tc'>"+obj.leftpayReservePosition+"</td>");
                                }
                                var ps = obj.orderState==null?"":obj.orderState;
                                //$(tr).append("<td class='tc'>"+obj.createUserName+"</td>")
                                $(tr).append("<td class='tc'> <span class='order-picker onshow'>" + obj.createUserName + "</span>" + "<span class='order-saler'>" + obj.salerName + "</span>")
                                .append("<td class='tc'>"+obj.orderNo+"</td>")
                                .append("<td class='tr'>"+obj.totalMoney+"</td>")
                                .append("<td class='tr'>"+obj.payMoney+"</td>")
                                .append("<td class='tc'>"+obj.personNum+"</td>")
                                .append("<td class='tc'>"+obj.payStatus+"</td>");
                                if(obj.orderRemark == null){
                                	 $(tr).append("<td class='tl'>" + ""+ "</td>");
                                }else{
                                	  $(tr).append("<td class='tl'>" + obj.orderRemark + "</td>");
                                }
                                numCount++; 
                                $(current).find("tbody").append(tr);
                                if(index==result.length-1){
                                    var str = $(current).find("tbody").find("tr")[index+1-numCount];
                                    var td1 = $(str).find("td")[0];
                                    var td2 = $(str).find("td")[1];
                                    var td3 = $(str).find("td:eq(2)");
                                    $(td1).attr("rowspan",numCount);
                                    $(td2).attr("rowspan",numCount);
                                    $(td3).attr("rowspan",numCount);
                                }
                            });
                            if(productGroupId){
                                $("#contentTableclone").append($(current));  
                            }
                            $(current).show();
                        }
                    }
                );
            }
            if(productGroupId){
                $(".cDetail-ul").find(".cDetail-ul-active").attr("class","");
                $(obj).parent().attr("class","cDetail-ul-active");
            }
        }
        
        //ajax
	   function orderPersonNum(current,obj,child,child1,child2,productGroupId,status){
            $(child).hide();
            $(child1).hide();
            $(child2).hide();
            if(productGroupId){
                $(current).show();                  
            }else{
                $(current).toggle();
            }
            var trs = $(current).find("tbody").find("tr"); 
            /*if(trs.length!=0){
                if(productGroupId){
                    $(current).show();                  
                }else{
                    $(current).toggle();
                }
            }          
            else*/ 
            if(trs.length == 0){ 
                $.ajax({
                    type: "POST",
                    url: "${ctx}/stock/manager/apartGroup/productGroupOrdersDetail",
                    data: {
                        productGroupId:productGroupId,
                        status:status
                    },
                     success: function(msg){
                        if(msg==""){
                        	$(current).find("tbody").empty();
                            var tr = $("<tr></tr>");
                            var colspanNum = status=="nopay"?5:6;
                            $(tr).append("<td class='tc' colspan=\""+colspanNum+"\">没有记录</td>");
                            $(current).find("tbody").append(tr);
                            //$(current).show();
                        }else{
                            var personNum = 0;
                            var agentName = null;
                            var tbody = null;
                            var tbody1 = null;
                            var tbody2 = null;
                            var count = 0;
                            $(current).find("tbody").empty();
                        $(msg).each(function(index1,obj1){
                            tbody1 = "<td class='tc' rowspan="+ obj1.length+">"+obj1[0].agentName+"</td>";
                            $(obj1).each(function(index2,obj2){
                                    if(status==""){
                                        //tbody2 = "<td class='tc'>"+obj2.createUserName+"</td><td class='tc'>"+obj2.orderNo+"</td><td class='tr'>"+obj2.totalMoney+"元</td><td class='tc'>"+obj2.orderPersonNum+"</td>";
                                    	 tbody2 = "<td class='tc'> <span class='order-picker onshow'>" + obj2.createUserName + "</span>" 
                                         + "<span class='order-saler'>" + obj2.salerName + "</span>"
                                         +"</td><td class='tc'>"+obj2.orderNo+"</td><td class='tr'>"+obj2.totalMoney+"元</td><td class='tc'>"+obj2.orderPersonNum+"</td>";
                                    }else{
                                        var payStatus =null;                                                                             
                                        switch (obj2.payStatus)
                                        {
                                           
                                            case 1:
                                                payStatus = "未收全款";
                                            break;
                                            case 2:
                                                payStatus = '未收订金';
                                            break;
                                            case 3:
                                                payStatus = "已占位";
                                            break;
                                            case 4:
                                                payStatus = "已收订金";
                                            break;
                                            case 5:
                                                payStatus = "已收全款";
                                            break;
                                            case 99:
                                                payStatus = '<span style="color:#eb0205">已取消</span>';
                                            break;
                                            case 111:
                                                payStatus = '<span style="color:#eb0205">已删除</span>';
                                            break;
                                            default:
                                                payStatus = '';
                                            break;
                                        }                                        
                                      
                                        var orderPersonNumColor = " ";
                                        if(payStatus==99|| payStatus==111)
                                            orderPersonNumColor = " style='color:#eb0205'";
                                        //tbody2 = "<td class='tc'>"+obj2.createUserName+"</td><td class='tc' "+orderPersonNumColor+">"+obj2.orderPersonNum+"</td><td class='tc'>"+payStatus +  "</td>";
                                        tbody2 = "<td class='tc'> <span class='order-picker onshow'>" + obj2.createUserName + "</span>" 
                                                 + "<span class='order-saler'>" + obj2.salerName + "</span>"
                                                 + "</td><td class='tc' "+orderPersonNumColor+">"+obj2.orderPersonNum+"</td><td class='tc'>"+payStatus +  "</td>";
                                    }
                                if(count == 0){
                                    $(current).find("tbody")
                                    .append($("<tr></tr>")
                                    .append(tbody1)
                                    .append("<td class=\"orderCompanyNum\" rowspan="+obj1.length+">"+personNum+"</td>")
                                    .append(tbody2));
                                }else{
                                    $(current).find("tbody")
                                    .append($("<tr></tr>")
                                    .append(tbody2));
                                    }
                                if (obj2.payStatus != 99 && obj2.payStatus != 111) {
                                	personNum+=obj2.orderPersonNum;
                                }
                                count++;
                            });
                            $(current).find(".orderCompanyNum").text(personNum);
                            $(current).find(".orderCompanyNum").attr("class","tc");
                            personNum = 0;
                            count = 0;
                        });
                    }
                }
            });
                if(productGroupId){
                    $("#contentTableclone").append($(current));  
                    }
            $(current).show();
            }
            if(productGroupId){
                $(".cDetail-ul").find(".cDetail-ul-active").attr("class","");
                $(obj).parent().attr("class","cDetail-ul-active");
            }
        }       
	   // 删除确认对话框
        function confirmxDel(mess, href,proId){
            top.$.jBox.confirm(mess,'系统提示',function(v){
                if(v=='ok'){
                    loading('正在提交，请稍等...');
                    window.location="${ctx}/stock/manager/apartGroup/delete?id=${param.id}&agentId=${param.agentId }&groupreserveId="+proId;
                }
            },{buttonsFocus:1});
            top.$('.jbox-body .jbox-icon').css('top','55px');
            return false;
        }

       function tolist(){
          window.location.href = "${ctx}/stock/manager/apartGroup/?agentId=";
      }
       
       $(function () {
           //table中团号、产品名称切换
			switchSalerAndPicker();
           switchNumAndPro();

       });
   function switchSalerAndPicker() {
	//点击团号
	$(document).on("click", ".order-saler-title", function () {
	   $(this).addClass("on").siblings().removeClass('on');
	   var $table = $(this).parents('table:first');
	   $table.find('.order-saler').addClass('onshow');
	   $table.find('.order-picker').removeClass('onshow');
	});
	//点击产品
	$(document).on("click", ".order-picker-title", function () {
	   $(this).addClass("on").siblings().removeClass('on');
	   var $table = $(this).parents('table:first');
	   $table.find('.order-saler').removeClass('onshow');
	   $table.find('.order-picker').addClass('onshow');
	});
   }
        
</script>
<style type="text/css">
body{ font-size:12px;}
.activity_team_top1 .team_top table {
    border-width: 0;
    border-style: solid;
}
</style>
</head>
<body>       
        <page:applyDecorator name="stock_op_head">
            <page:param name="current">activityStock</page:param>
        </page:applyDecorator>
            <div class="ydbzbox fs">
<%--            <div class="mod_nav departure_title">库存详情</div>--%>
            <div class="orderdetails_tit"><span>1</span>产品信息</div>
            <div style="margin-top:8px;" class="team_con   show-grid">
			<p class="ydbz_mc">${travelActivity.acitivityName }</p>
               <div class="orderdetails1">
                   <span>旅游类型：${travelActivity.travelTypeName }</span>
                   <span>出发地：${travelActivity.fromAreaName }</span>
                   <span>产品编号：${travelActivity.activitySerNum }</span>
                   <span>行程天数：${travelActivity.activityDuration }</span>
            </div>
<%--                    <label>航空：</label>${travelActivity.trafficNameDesc}--%>
<%--                    <label>产品类型：</label>${travelActivity.activityTypeName }--%>
<%--            <div style="margin-top:8px;" class="team_con   show-grid">--%>
<%--                <div class="span4">--%>
<%--                    <label>切位渠道：</label>--%>
<%--                    <c:if test="${agentName == null}">全部渠道</c:if>--%>
<%--                    <c:if test="${agentName != null}">${agentName }</c:if>--%>
<%--                </div>--%>
<%--            </div>--%>
			<div class="orderdetails_tit">
				<span>2</span>团期信息
			</div>
	
    <table id="contentTable" class="table table-stock table-bordered table-condensed psf_table_son table-mod2-group activitylist_bodyer_table">
            <thead style="background:#403738">
            <tr>
                <th rowspan="2">出团日期</th>
                <th rowspan="2">团号</th>
                <th rowspan="2">预收</th>
                <th colspan="2">已分配人数</th>
                <th colspan="2">已售出人数</th>
                <th rowspan="2">余位</th>
                <th rowspan="2">团期剩余天数</th>
                <th rowspan="2">操作</th>
            </tr>
            <tr>
                <th>占位人数</th>
                <th>切位人数</th>
                <th>售出占位</th>
                <th>售出切位</th>
            </tr>
        </thead>
                    
        <tbody>
          <c:forEach items="${travelActivity.activityGroups}" var="group" varStatus="status" >

          	  <c:if test="${ group.id==param.activityGroupId }">
          	  <c:set var="groupId" value="${group.id}"></c:set>
          	  <c:set var="groupCount" value="${status.count}"></c:set>
              <tr class="grouprow" id="grouprow${status.count}">
                  <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/></td>
                  <td style="display: none"><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/></td>
                  <td class="tc"><span>${group.groupCode }</span></td>
                  <td  ><span>${group.planPosition } </span></td>
<%--                  <td class="blue"><span>${group.nopayReservePosition + group.payReservePosition } </span>&nbsp;&nbsp;&nbsp;--%>
                  
                  <td class="tc"><span>${group.nopayReservePosition } </span><!--<a id="close0${status.count}" class="team_a_click purple" href="javascript:void(0)" onClick="orderPersonNum('#noPayReservePosition${status.count}',this,'#reservePosition${status.count}','#soldNopayPosition${status.count}','#soldPosition${status.count}',${group.id },'nopay')">明细</a><img src="${ctxStatic}/images/team_san_purple.png" alt="">--></td></td>
                  <td class="tc"><span>${group.payReservePosition } </span><!--<a id="close1${status.count}" class="team_a_click blue" href="javascript:void(0)" onClick="expand('#reservePosition${status.count}',this,'#soldPosition${status.count}','#soldNopayPosition${status.count}','#noPayReservePosition${status.count}')">明细</a><img src="${ctxStatic}/images/team_san.png" alt="">--></td></td>
<%--                  <td><span>${group.soldNopayPosition+group.soldNopayPosition }</span> </td>--%>
                  <td class="tc"><span>${group.soldNopayPosition }</span><!--&nbsp;&nbsp;&nbsp;
                  <a id="close2${status.count}" href="javascript:void(0)" class="team_a_click green" onClick="orderPersonNum('#soldPosition${status.count}',this,'#reservePosition${status.count}','#soldNopayPosition${status.count}','#noPayReservePosition${status.count}',${group.id },'')">明细</a>
                  <img src="${ctxStatic}/images/team_san_green.png" alt="">-->
				  </td>
                  <td class="tc"><span>${group.soldPayPosition }</span><!--&nbsp;&nbsp;&nbsp;
                  <a id="close3${status.count}" class="team_a_click orange" href="javascript:void(0)" onClick="soldNopayPosition('#soldNopayPosition${status.count}',this,'#soldPosition${status.count}','#reservePosition${status.count}','#noPayReservePosition${status.count}',${group.id })">明细</a>
				  <img src="${ctxStatic}/images/team_san_orange.png" alt=""> -->
				  </td>
                  <td class="tc"><span>${group.freePosition }</span></td>
                  <td class="tc"><c:if test="${group.leftdays <= 0 }"><span style="color:#eb0205">已过期</span></c:if>
                      <c:if test="${group.leftdays > 0 }"><span>${group.leftdays}天</span></c:if></td>
                  <td class="tc"><c:if test="${group.leftdays > 0 }"><a href="${ctx}/stock/manager/apartGroup/reserve?id=${travelActivity.id}&agentId=${param.agentId }&activityGroupId=${group.id }"><c:if test="${showReserve!=0 }">申请切位</c:if></a></c:if></td>
              </tr>
          

                <tr id="reservePosition1" style="display:none"  class="1 activity_team_top1">
                    <td class="team_top" colspan="10">
                        <table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;" >
                            <thead>
                                <tr>
                                    <th>渠道名称</th>
                                    <th>切位总数</th>
                                    <th>预订人</th>
                                    <th>备注</th>
<%--                                     <th>操作</th>--%>
                                 </tr>
                            </thead>
                            <tbody>
                        <!--表单内容-->
	                        <c:if test="${empty group.activityGroupReserveList}"><tr><td class="tc" colspan="4">没有记录</td></tr></c:if>
                            <c:forEach items="${group.activityGroupReserveList}" var="groupReserve">
                                <tr>
                                    <td class='tc'>${groupReserve.agentName }</td>
                                    <td class='tc'>${groupReserve.payReservePosition }</td>
                                    <td class='tc'>${groupReserve.reservation }</td>
                                    <td class='tl'>${groupReserve.remark }</td>
<%--                                    <td><a href="${ctx}/stock/manager/revise?id=${param.id }&groupreserveId=${groupReserve.id }&agentId=${groupReserve.agentId }">修改切位</a>&nbsp;&nbsp;&nbsp;
                                    <a href="javascript:void(0)" onClick="return confirmxDel('要删除该切位记录吗？', this.href ,${groupReserve.id })">删除切位</a></td>--%>
                                </tr>                                
                            </c:forEach>
                            </tbody>
                        </table>
                    </td>
                </tr>
              </c:if>
            </c:forEach>  

              <tr id="noPayReservePosition1" style="display:none"  class="1 table-Second activity_team_top1 activity_team_top1_purple">
                    <td class="team_top" colspan="10">
                        <table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;" >
                            <thead>
                                <tr>
                                    <th>渠道名称</th>
                                    <th>占位总数</th>
                                    <th>
                                    <span class="order-picker-title on">下单人</span>/
									<span class="order-saler-title">销售</span>
                                    </th>
                                    <th>占位人数</th>
                                    <th>订单状态</th>
<%--                                    <th>备注</th>--%>
<%--                                     <th>操作</th>--%>
                                 </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr id="soldPosition1"style="display:none"  class="1 table-Second activity_team_top1 activity_team_top1_green">
                    <td class="team_top" colspan="10">
                        <table id="teamTable" class="table table-striped table-bordered table-condensed table-Second" style="margin-bottom: 1px;">
                            <thead>
                                <tr>
                                    <th>渠道名称</th>
                                    <th>占位总数</th>
                                    <th> <span class="order-picker-title on">下单人</span>/
									<span class="order-saler-title">销售</span></th>
                                    <th>订单号</th>
                                    <th>应收团款金额</th>
                                    <th>占位人数</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </td>
                </tr>
            <tr id="soldNopayPosition1" style="display:none" class="1 activity_team_top1  activity_team_top1_orange">
                    <td class="team_top" colspan="10">
                        <table id="teamTable" class="table table-striped table-bordered table-condensed" style="margin-bottom: 1px;">
                            <thead>
                                <tr>
                                    <th>渠道名称</th>
                                    <th>切位总数</th>
                                    <th>切位空余数</th>
                                    <th> <span class="order-picker-title on">下单人</span>/
									<span class="order-saler-title">销售</span></th>
                                    <th>订单号</th>
                                    <th>应收团款金额</th>
                                    <th>已收金额</th>
                                    <th>已售出切位</th>
                                    <th>状态</th>
                                    <th>备注</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </td>
             </tr>
           	
        </tbody>
    </table>   			
	        
            <div class="cDetail">
                <ul class="cDetail-ul">
                    <li class="">
                    <a id="close0" class="purple" href="javascript:void(0)" onClick="orderPersonNum('#noPayReservePosition1',this,'#reservePosition1','#soldNopayPosition1','#soldPosition1',${groupId },'nopay')">已占位明细</a>
                      <em></em>
                      </li>
                      <li class="">
                      <a id="close1" class="blue" href="javascript:void(0)" onClick="expand('#reservePosition1',this,'#soldPosition1','#soldNopayPosition1','#noPayReservePosition1')">已切位明细</a>
                      <em></em>
                      </li>
                      <li class="">
                      <a id="close2" href="javascript:void(0)" class="green"  onClick="orderPersonNum('#soldPosition1',this,'#reservePosition1','#soldNopayPosition1','#noPayReservePosition1',${groupId },'')">售出占位明细</a>
                      <em></em>
                      </li>
                      <li class="">
                      <a id="close3" class="orange" href="javascript:void(0)" onClick="soldNopayPosition('#soldNopayPosition1',this,'#soldPosition1','#reservePosition1','#noPayReservePosition1',${groupId })">售出切位明细</a>
                        </li>
                </ul>
            </div>
          

 
		        <table id="contentTableclone" style="width:100%">
		        </table>
            <div class="release_next_add">
               <input id="btnSubmit"  class="btn btn-primary" type="button" value="返 回" onClick="tolist()" />
            </div>
   </div>
  </div>
</body>
</html>