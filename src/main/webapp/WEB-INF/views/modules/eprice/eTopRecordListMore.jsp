<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>更多询价记录列表</title>
	<meta name="decorator" content="wholesaler"/>
	<%@ include file="commonVar.jsp"%>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/eTopRecordListMore.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/epriceCommon.js" type="text/javascript"></script>
 	<script src="${ctxStatic}/js/common.js" type="text/javascript"></script> 	
	
	<script type="text/javascript">
	var project = {};
	project.id = '${project.id}';
	$(function(){
		//展开筛选
		launch();
		//文本框提示信息显示隐藏
		//inputTips();
		//操作浮框
		operateHandler();
		//成本价滑过显示具体内容
		inquiryPriceCon();
	});
	//展开收起
			   function expand(child, obj, srcActivityId, payMode_deposit, payMode_advance, payMode_full,payMode_op,payMode_cw,payMode_data,payMode_guarantee,payMode_express) {
	            if($(child).is(":hidden")){
	                var selectdata = $("#agentIdSel").length ? $("#agentIdSel option:selected") : $("input[name='agentId']");
	                var agentId = selectdata.val();
	                if(agentId!=null&&agentId!="") {
	                    $.ajax({
	                        
	                    });
	                }else{
	                	$(obj).html("收起");
	                    $(child).show();
	                    $(obj).parents("td").addClass("td-extend");
	                }
	            }else{
	                if(!$(child).is(":hidden")){
	                    $(child).hide();
	                    $(obj).parents("td").removeClass("td-extend");
	                    $(obj).html("展开");
	                }
	            }
	        }
	</script>
<style type="text/css">
</style>
</head>
<body>
	<!--右侧内容部分开始-->
    	
		
        <div class="ydbz_tit"><span class="ydExpand closeOrExpand"></span>基本信息</div>
        <div class="messageDiv" flag="messageDiv">
            <!-- 机票相关，临时去掉 -->
           	<div class="seach25 seach100" style="display:none">
              <p>机票计调：</p>
              <p class="seach_r">
              	<c:forEach items="${listt}" var="u">
              		<span uid="${u.id}" class="seach_check">${u.name}</span>
              	</c:forEach>
              </p>
            </div>
            <div class="seach25">
              <p>销售姓名：</p><p class="seach_r">${project.lastBaseInfo.salerName}</p>
            </div>
            <div class="seach25">
              <p>销售电话：</p><p class="seach_r">${project.lastBaseInfo.salerPhone}</p>
            </div>
            <div class="seach25">
            	<p>销售邮箱：</p><p class="seach_r">${project.lastBaseInfo.salerEmail}</p></div>
            <div class="kong"></div>
            <div class="seach25">
              <p>客户类型：</p>
              <p class="seach_r">
              	<c:choose>
		   			<c:when test="${project.lastBaseInfo.customerType==1}">直客</c:when>
		   			<c:when test="${project.lastBaseInfo.customerType==2}">同行</c:when>
		   			<c:otherwise>其他</c:otherwise>
				</c:choose>
              </p>
            </div>
            <div class="seach25">
              <p>询价客户：</p><p class="seach_r">${project.lastBaseInfo.customerName }</p>
            </div>
            <div class="seach25">
              <p>联系人：</p><p class="seach_r">${project.lastBaseInfo.contactPerson }</p>
            </div>
            <div class="seach25">
              <p>电话：</p><p class="seach_r">${project.lastBaseInfo.contactMobile }</p>
            </div>
            <div class="kong"></div>
        </div>
        <div class="activitylist_bodyer_right_team_co_paixu">
          <div class="activitylist_paixu">
            <div class="activitylist_paixu_left">
              <ul id="sort-list-ul-id">
                <li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
                <li class="activitylist_paixu_left_biankuang liid" sortc="createTime" sortv="1"><a >询价时间<i class="icon" style="display: none;"></i></a></li>
                <li class="activitylist_paixu_left_biankuang liupdateDate" sortc="lastToperatorPriceTime" sortv="1"><a >报价时间<i class="icon" style="display: none;"></i></a></li>
                <!-- 
                <li class="activitylist_paixu_left_biankuang lisettlementAdultPrice" sortc="lastCreateProductTime" sortv="1"><a >生成产品时间<i class="icon" style="display: none;"></i></a></li>
                <li class="activitylist_paixu_left_biankuang "  sortc="lastCancelTime" sortv="1"><a >取消时间<i class="icon" style="display: none;"></i></a></li> -->
              </ul>
              <form id="eprice-search-form-id"></form>
            </div>
            <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong id="eprice-list-count-id"></strong>&nbsp;条</div>
            <div class="kong"></div>
          </div>
        </div>
        <form method="post" name="groupform" id="groupform">
          <table class="table activitylist_bodyer_table" >
            <thead>
              <tr>
                <th width="4%">序号</th>
                        <th width="9%">询价日期</th>
                        <th width="7%">计调员</th>
                        <th width="9%">报价日期</th>
                        <th width="9%">出发日期</th>
                        <th width="9%">航班类型</th>
                        <th width="7%">出发城市</th>
                        <th width="7%">目的地城市</th>
                        <th width="7%">成本价</th>
                        <th width="7%">外报价</th>
                        <th width="7%">结算价</th>
                        <th width="8%">状态</th>
                        <th width="7%">操作</th>
              </tr>
            </thead>
            <tbody id="eprice-recode-list-id">
            	
            </tbody>
           </table>
        </form>
        <div class="pagination" id="eprice-record-list-page-id"></div>
      <!--右侧内容部分结束--> 
      
	
</body>
</html>