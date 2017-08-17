<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
	<title>应付账款</title>
	<meta name="decorator" content="wholesaler"/>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	  <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
   <style type="text/css">
   </style>
	<script type="text/javascript">
	//如果展开部分有查询条件的话，默认展开，否则收起	
	function closeOrExpand(){
		var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='groupCode']");
		var searchFormselect = $("#searchForm").find("select");
		var inputRequest = false;
		var selectRequest = false;
		for(var i=0;i<searchFormInput.length;i++) {
			var inputValue = $(searchFormInput[i]).val();
			if(inputValue != "" && inputValue != null) {
				inputRequest = true;
				break;
			}
		}
		for(var i=0;i<searchFormselect.length;i++) {
			var selectValue = $(searchFormselect[i]).children("option:selected").val();
			if(selectValue != "" && selectValue != null && selectValue != 0) {
				selectRequest = true;
				break;
			}
		}
		if(inputRequest||selectRequest) {
			$('.zksx').click();
		}
	}
	$(function () {
      //搜索条件筛选
      launch();
      closeOrExpand();
      //操作浮框
	  operateHandler();
	  $("[name=jd]").comboboxSingle();
	 // $("[name=deptId]").comboboxSingle();
     });
     //展开收起
     function expand(child, obj) {
         if ($(child).is(":hidden")) {
             $(child).show();
             $(obj).parents("td").addClass("td-extend");
             $(obj).parents("tr").addClass("tr-hover");

         } else {
             $(child).hide();
             $(obj).parents("td").removeClass("td-extend");
             $(obj).parents("tr").removeClass("tr-hover");

         }
     }
            
    function page(n,s){
	    $("#pageNo").val(n);
	    $("#pageSize").val(s);
	    $("#searchForm").submit();
        return false;
    }
    //条件重置
    function resetForm(){
		var inputArray = $('#searchForm').find("input[type!='hidden'][type!='submit'][type!='button']");
		var selectArray = $('#searchForm').find("select");
		for(var i=0;i<inputArray.length;i++){
			if($(inputArray[i]).val()){
				$(inputArray[i]).val('');
			}
		}
		for(var i=0;i<selectArray.length;i++){
			var selectOption = $(selectArray[i]).children("option");
			$(selectOption[0]).attr("selected","selected");
		}
    }
    function downLoadPayList(ctx){
        var args = $('#searchForm').serialize();//查询条件参数
        window.open(ctx + "/receivepay/manager/downLoadPayList?flag=1&" + args);
    }
	</script>
</head>
 <body>
   <form:form method="post" action="${ctx }/receivepay/manager/getPayList" id="searchForm">
      <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
      <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
      <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
      <div class="activitylist_bodyer_right_team_co">
          <div class="activitylist_bodyer_right_team_co2">
              <input class="txtPro inputTxt searchInput" id="groupCode" name="groupCode" value="${groupCode }" placeholder="请输入团号">
          </div>
          <div class="zksx">筛选</div>
          <div class="form_submit">
              <input type="submit" value="搜索" id="seachbutton" class="btn btn-primary ydbz_x">
              <input class="btn ydbz_x" onclick="resetForm()" value="清空所有条件" type="button">
              <input class="btn ydbz_x" onclick="downLoadPayList('${ctx}')" value="导出Excel" type="button">
          </div>
          <div style="display:none;" class="ydxbd">
              <span></span>
<!--                                     <div class="activitylist_bodyer_right_team_co3"> -->
<!--                                         <div class="activitylist_team_co3_text">类型选择：</div> -->
<!--                                         <select> -->
<!--                                             <option value="单团">单团</option> -->
<!--                                             <option value="散拼">散拼</option> -->
<!--                                             <option value="游学">游学</option> -->
<!--                                             <option value="大客户">大客户</option> -->
<!--                                             <option value="自由行">自由行</option> -->
<!--                                             <option value="签证" selected="">签证</option> -->
<!--                                             <option value="机票">机票</option> -->
<!--                                             <option value="游轮">游轮</option> -->
<!--                                             <option value="全部" selected="selected">全部</option> -->
<!--                                         </select> -->
<!--                                     </div> -->
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">计调：</div>
                    <select name="jd">
				<option value="" selected="selected">全部</option>
				<c:forEach var="jd" items="${agentJd }">
				<option value="${jd.key }" <c:if test="${jds==jd.key}">selected="selected"</c:if>>${jd.value }</option>
				</c:forEach>
				</select>
                 </div>
                 <div class="activitylist_bodyer_right_team_co1">
                     <div class="activitylist_team_co3_text">部门：</div>
                     <div class="selectStyle">
                         <select name="deptId">
                             <option value="" >全部</option>
                             <c:forEach var="dept" items="${dept }">
                              <option value="${dept.id }" <c:if test="${deptId==dept.id}">selected="selected"</c:if>>${dept.name }</option>
                          </c:forEach>
                         </select>
                    </div>
              </div>

             
              <div class="activitylist_bodyer_right_team_co1">
                  <label>收款单位：</label>
                  <input class="txtPro inputTxt" id="payee" name="payee" value="${payee }" onkeyup="value=value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5\ ]/g,'')">
              </div>
          </div>

      </div>
              
          <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
      
      </form:form>
      <table class="table mainTable activitylist_bodyer_table" id="contentTable">
          <thead>
              <tr>
                  <th width="5%">序号</th>
<!--                   <th width="4%">团队类型</th> -->
                  <th width="8%">部门</th>
                  <th width="5%">计调</th>
                  <th width="5%">团号</th>
                  <th width="4%">收款单位</th>
                  <th width="5%">应付总额</th>
                  <th width="5%">已付总额</th>
                  <th width="5%">应付余额</th>
                  <th width="6%">应付账期</th>
                  <th width="6%">未到期应付</th>
              </tr>
          </thead>
          <tbody>
              <c:forEach items="${page.list }" var="data" varStatus="s">
                <tr id="parent${s.count}">
                    <td class="tc">${s.count }</td>
<!--                     <td class="tc">${fns:getDictLabel(data.orderType,"order_type", "")}</td> -->
                    <td class="tc">${fns:getDeptNameById(data.deptId)}</td>
                    <td class="tc">${data.jd }</td>
                    <td class="tc">${data.groupCode }</td>
                    <td class="tc">${data.payee }</td>
                     <td class="tc">¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${data.total }"/></td>
                     <c:choose>
                        <c:when test="${data.payed == null }">
                           <td class="tc">¥0.00</td>
                        </c:when>
                        <c:otherwise><td class="tc">¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${data.payed }"/></td></c:otherwise>
                     </c:choose>
                     
                     
                     
                     <td class="tc">
                        <c:choose>
                            <c:when test="${data.sub == null }">¥0.00</c:when>
                            <c:otherwise>¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${data.sub }"/></c:otherwise>
                        </c:choose>
                    </td>
                     
                    
                        
                      
                     
                    <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${data.paymentDay }"/></td>
                    <td class="tc">
                        <c:choose>
                            <c:when test="${data.paymentDay<date }">¥0.00</c:when>
                            <c:otherwise><c:choose>
                            <c:when test="${data.sub == null }">¥0.00</c:when>
                            <c:otherwise>¥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${data.sub }"/></c:otherwise>
                        </c:choose></c:otherwise>
                        </c:choose>
                    </td>
                </tr>
              </c:forEach>
          </tbody>
      </table>
   <div class="pagination">${page}
<div style="clear:both;"></div>
</div>
     
    </body>
     
</html>