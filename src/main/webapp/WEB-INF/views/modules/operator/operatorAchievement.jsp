<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <title>财务-操作人员业绩</title>
        <meta name="decorator" content="wholesaler"/>
        <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
        <!-- <link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon"/> -->
        <!--[if lte IE 6]>
        <link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css"/>
        <script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
        <![endif]-->
        <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
        <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css"/>
        <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
        <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
        <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
        <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
        <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
        <script type="text/javascript">
            $(function () {
                //展开收起搜索条件
                launch();
                //table中团号、产品名称切换
               // switchNumAndPro();
                //switchSalerAndCreator();

                var span=$(".dept");
                for(var i=0;i<span.length;i++){
                	if($(span[i]).text().indexOf(",")>-1){
                		var str=$(span[i]).text().split(",")[0]+"...";
                    	$(span[i]).text(str);
                	}
                }
                
                //去掉总毛利和总人数
               /*$.ajax({
                	type:"post",
                	url:"${ctx}/operator/achievement/getZongJi",
                	data:$("#searchForm").serialize(),
                	success:function(result){
                		var split=result.split("-");
                		$("#profitSumAll").text("￥"+split[1]);
                		$("#personAll").text(split[0]);
                	}
                });*/
            }); 
            function page(n,s){
				$("#pageNo").val(n);
				$("#pageSize").val(s);
				$("#searchForm").attr("action","${ctx }/operator/achievement/getList");
				$("#searchForm").submit();
			}
            function resetParams(){
            	$("#departmentId").val("");
            	$("#salerId").val("")
            	$("#productName").val("")
            	$("#groupOpenDate").val("");
            	$("#groupCloseDate").val("");
            	$("#groupCode").val("");
            }
            function exportToExcel(){
            	$("#searchForm").attr("action","${ctx }/operator/achievement/exportToExcel");
            	$("#searchForm").submit();
            	$("#searchForm").attr("action","${ctx }/operator/achievement/getList");
            }
        </script>
    </head>
    <body>
          <!--右侧内容部分开始-->
              <form method="post" action="${ctx }/operator/achievement/getList" id="searchForm">
                  <input type="hidden" value="${page.pageNo}" name="pageNo" id="pageNo">
                  <input type="hidden" value="${page.pageSize}" name="pageSize" id="pageSize">
                  <div class="activitylist_bodyer_right_team_co">
                      <div class="activitylist_bodyer_right_team_co2">
                          <input class="txtPro inputTxt searchInput" id="groupCode" name="groupCode" value="${groupCode }" placeholder="请输入团号">
                      </div>
                      <div class="zksx">筛选</div>
                      <div class="form_submit">
                          <input type="submit" value="搜索" id="seachbutton" class="btn btn-primary ydbz_x">
                          <input class="btn ydbz_x" type="button" onclick="resetParams()"  value="清空所有条件">
                          <input class="btn ydbz_x " onclick="exportToExcel()" value="导出Excel" type="button">
                      </div>
                      <div style="display:none;" class="ydxbd">
                          <span></span>
                          <div class="activitylist_bodyer_right_team_co1">
                              <div class="activitylist_team_co3_text">部门：</div>
                              <div class="selectStyle">
                                  <select name="departmentId" id="departmentId">
                                        <option value="">全部</option>
                                        <c:forEach items="${departments }" var="d">
                                            <option value="${d.id }" <c:if test="${d.id==departmentId }">selected</c:if> >${d.text }</option>
                                         </c:forEach>
                                  </select>
                              </div>
                          </div>
                          <div class="activitylist_bodyer_right_team_co1">
                              <div class="activitylist_team_co3_text">操作员：</div>
                              <div class="selectStyle">
                                  <select name="salerId" id="salerId">
                                    <option value="">全部</option>
                                    <c:forEach items="${users }" var="u">
                                      <option value="${u.id }" <c:if test="${u.id==salerId }">selected</c:if>>${u.name }</option>
                                    </c:forEach>
                                  </select>
                              </div>
                          </div>
                          <div class="activitylist_bodyer_right_team_co1">
                              <label class="activitylist_team_co3_text">产品名称：</label>
                              <input id="productName" name="productName" class="inputTxt inputTxtlong" value="${productName }">
                          </div>
                          <div class="activitylist_bodyer_right_team_co1">
                              <label class="activitylist_team_co3_text">汇总月份：</label>
                              <%-- <input readonly=""
                                     onfocus="WdatePicker({dateFmt:'yyyy年MM月',onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})"
                                     value="${groupOpenDate }" name="groupOpenDate" class="inputTxt dateinput"
                                     id="groupOpenDate"> --%>
                              <%-- <span>至 </span>--%>
                              <input readonly="" onclick="WdatePicker({dateFmt:'yyyy年MM月'})" value="${groupOpenDate }" name="groupOpenDate"
                                     class="inputTxt dateinput" id="groupOpenDate"> 
                          </div>
                      </div>
                  </div>
              </form>
              <!--查询结果筛选条件排序开始-->
   <%--<div class="activitylist_paixu_left">
      			<div class="cwxt-qbdd">
			          总毛利：
			          <span id="profitSumAll"></span>
			          总人数：
			          <span id="personAll"></span>
			   </div> 
			</div>--%>
<div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count }</strong>&nbsp;条</div>
                  <!--查询结果筛选条件排序结束-->
              <table class="table mainTable activitylist_bodyer_table" id="contentTable">
                  <thead>
                      <tr>
                          <th width="2%" >序号</th>
                          <th width="6%" >部门</th>
                          <th width="3%" >操作员</th>
                          <th width="6%" >产品名称</th>
                          <th width="6%" >团号</th>
                          <th width="4%" >人数</th>
                          <th width="8%" >毛利</th>
                      </tr>
                  </thead>
                  <tbody>
                  	<c:forEach items="${page.list }" var="list" varStatus="status">
                      <tr>
                         <td class="tc">${status.count }</td>
                         <td class="tc " title="${list.departmentName }"> <span class="dept">${list.departmentName }</span>	</td>
                         <td class="tc">${fns:getUserById(list.createBy).name}</td>
                         <td class="tc">${list.acitivityName }</td>
                         <td class="tc">${list.groupCode }</td>
                         <td class="tc">${list.personNum }</td>
                         <td class="tr">
                         	<c:if test="${list.profitSum!='0.00' }">
                         		￥${list.profitSum }
                         	</c:if>
                         </td>
                      </tr>
                      </c:forEach>
                  </tbody>
              </table>
              <div class="page">
				<div class="pagination">
						<div class="endPage">${page }</div>
				</div>
			  </div>	
                    <!--右侧内容部分结束-->
    </body>
</html>
