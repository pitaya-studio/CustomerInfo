<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>报表定义列表</title>
  <meta name="decorator" content="wholesaler"/>
  <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/huanqiu-style.css"/>
  <script type="text/javascript">
    $(function(){

      //如果展开部分有查询条件的话，默认展开，否则收起
      var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
      var searchFormselect = $("#searchForm").find("select");
      var inputRequest = false;
      var selectRequest = false;
      for(var i = 0; i<searchFormInput.length; i++) {
        if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null && $(searchFormInput[i]).val() != "全部") {
          inputRequest = true;
        }
      }
      for(var i = 0; i<searchFormselect.length; i++) {
        if($(searchFormselect[i]).children("option:selected").val() != "" &&
                $(searchFormselect[i]).children("option:selected").val() != "0" &&
                $(searchFormselect[i]).children("option:selected").val() != null) {
          selectRequest = true;
        }
      }
      if(inputRequest||selectRequest) {
        $('.zksx').click();
      }

      var _$orderBy = $("#orderBy").val();
      if(_$orderBy==""){
        _$orderBy="createDate DESC";
        $("#orderBy").val("createDate DESC");
      }
      var orderBy = _$orderBy.split(" ");
      $(".activitylist_paixu_left li").each(function(){
        if ($(this).hasClass("li"+orderBy[0])){
          orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
          $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
          $(this).attr("class","activitylist_paixu_moren");
        }
      });
    });



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

      $("#orderBy").val(sortBy);
      $("#searchForm").submit();
    }

    function page(n,s){
      $("#pageNo").val(n);
      $("#pageSize").val(s);
      $("#searchForm").attr("action","${ctx}/report/define/list");
      $("#searchForm").submit();
    }

    function deleteReportDefine(id){
      $.jBox.confirm("确定要删除报表定义吗？", "提示", function(v, h, f) {
        if (v == 'ok') {
          $.ajax({
            type: "POST",
            url: "${ctx}/report/define/delete",
            cache:false,
            async:false,
            data:{id : id},
            success: function(data){
              top.$.jBox.tip(data.message, data.result);
              window.location.reload();
            },
            error : function(data){
              top.$.jBox.tip(date.message, data.result);
              return false;
            }
          });
        }
      });
    }

  </script>

</head>
<body>

<!--右侧内容部分开始-->
<form id="searchForm" action="${ctx}/report/define/list" method="post">
  <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
  <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
  <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
  <input id="reviewer" name="reviewer" type="hidden"  value="${costParam.reviewer}"/>
  <input id="budgetType" name="budgetType" type="hidden" value="${budgetType}"/>
  <div class="activitylist_bodyer_right_team_co">
    <div class="activitylist_bodyer_right_team_co2 pr wpr20">
      <label><!-- 团号： --></label>
      <input style="width:260px" class="txtPro inputTxt inquiry_left_text" id="groupCode" name="groupCode" value="${costParam.groupCode }" type="text" flag="istips" />
      <span style="display: block;" class="ipt-tips">团号/产品名称</span>
    </div>
    <div class="form_submit">
      <input class="btn btn-primary" value="搜索" type="submit">
    </div>
    <a class="zksx">展开筛选</a>
    <div class="ydxbd text-more-new">
      <div class="activitylist_bodyer_right_team_co1">
        <div class="activitylist_team_co3_text">产品类型：</div>
        <select name="productType">
          <option value="0">全部</option>
          <c:forEach items="${productTypes }" var="productType">
            <option value="${productType.productType }" <c:if test="${costParam.productType eq productType.productType }">selected="selected"</c:if>>${productType.getProductName() }</option>
          </c:forEach>
        </select>
      </div>
      <div class="activitylist_bodyer_right_team_co2">
        <div class="activitylist_team_co3_text">地接社：</div>
        <select id="supplyId" name="supplyId" >
          <option value="0">全部</option>
          <c:forEach items="${supplierList }" var="supplierInfo">
            <option value="${supplierInfo.id }" <c:if test="${costParam.supplyId eq supplierInfo.id }">selected="selected"</c:if>>${supplierInfo.supplierName }</option>
          </c:forEach>
        </select>
      </div>
      <div class="activitylist_bodyer_right_team_co2">
        <label>申请日期：</label>
        <input id="createDateStart" class="inputTxt dateinput" name="createDateStart" value="${costParam.createDateStart }" onclick="WdatePicker()" readonly="readonly" />
        <span> 至 </span>
        <input id="createDateEnd" class="inputTxt dateinput" name="createDateEnd" value="${costParam.createDateEnd }" onclick="WdatePicker()" readonly="readonly" />
      </div>
      <div class="kong"></div>
      <div class="activitylist_bodyer_right_team_co1">
        <div class="activitylist_team_co3_text">审批发起人：</div>
        <select id="createBy" name="createBy" >
          <option value="0">全部</option>
          <!-- 用户类型  1 代表销售 -->
          <c:forEach items="${fns:getSaleUserList('1')}" var="user">
            <option value="${user.id }" <c:if test="${costParam.createBy==user.id }">selected="selected"</c:if>>${user.name}</option>
          </c:forEach>
        </select>
      </div>
      <div class="activitylist_bodyer_right_team_co2">
        <div class="activitylist_team_co3_text">渠道选择：</div>
        <select id="agentId" name="agentId" >
          <option value="">全部</option>
          <!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
          <c:choose>
            <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}">
              <option value="-1">直客</option>
            </c:when>
            <c:otherwise>
              <option value="-1">非签约渠道</option>
            </c:otherwise>
          </c:choose>
          <c:forEach items="${agentList }" var="agent">
            <option value="${agent.id }" <c:if test="${costParam.agentId eq agent.id }">selected="selected"</c:if>>${agent.agentName }</option>
          </c:forEach>
        </select>
      </div>
      <div class="activitylist_bodyer_right_team_co2">
        <label>出团日期：</label>
        <input id="groupOpenDateStart" class="inputTxt dateinput" name="groupOpenDateStart" value="${costParam.groupOpenDateStart }" onclick="WdatePicker()" readonly="readonly" />
        <span> 至 </span>
        <input id="groupOpenDateEnd" class="inputTxt dateinput" name="groupOpenDateEnd" value="${costParam.groupOpenDateEnd }" onclick="WdatePicker()" readonly="readonly" />
      </div>

      <div class="kong"></div>
      <div class="activitylist_bodyer_right_team_co1">
        <div class="activitylist_team_co3_text">审批状态：</div>
        <select name="status">
          <option value="" selected="selected">全部</option>
          <option value="1" <c:if test="${costParam.status eq 1 }">selected="selected"</c:if>>审批中</option>
          <option value="2" <c:if test="${costParam.status eq 2 }">selected="selected"</c:if>>审批通过</option>
          <option value="0" <c:if test="${costParam.status eq 0 }">selected="selected"</c:if>>审批驳回</option>
          <option value="3" <c:if test="${costParam.status eq 3 }">selected="selected"</c:if>>取消申请</option>
        </select>
      </div>
      <div class="activitylist_bodyer_right_team_co2">
        <div class="activitylist_team_co3_text"><c:if test="${budgetType eq 0 }">预算</c:if><c:if test="${budgetType eq 1 }">成本</c:if>金额：</div>
        <input id="priceStart" name="priceStart" class="inputTxt" value="${costParam.priceStart }" />
        <span> 至 </span>
        <input id="priceEnd" name="priceEnd" class="inputTxt" value="${costParam.priceEnd }" />
      </div>

      <div class="kong"></div>

    </div>
    <div class="kong"></div>
  </div>
</form>
<div class="activitylist_bodyer_right_team_co_paixu">
  <div class="activitylist_paixu">
    <div class="activitylist_paixu_left">
      <ul>
        <li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
        <li class="activitylist_paixu_left_biankuang licreateDate"><a onclick="sortby('createDate',this)">创建时间</a></li>
        <li class="activitylist_paixu_left_biankuang liupdateDate"><a onclick="sortby('updateDate',this)">更新时间</a></li>
      </ul>
    </div>
    <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count }</strong>&nbsp;条</div>
    <div class="kong"></div>
  </div>
</div>

<div class="supplierLine">
  <a id="all" name="reviewer" href="javascript:void(0)" onclick="reviewer(0)" <c:if test="${costParam.reviewer eq 0 or empty costParam.reviewer}">class="select"</c:if>>全部</a>
  <%--<a id="todo" name="reviewer" href="javascript:void(0)" onclick="reviewer(1)" <c:if test="${costParam.reviewer eq 1 }">class="select"</c:if>>待本人审批</a>--%>
  <%--<a id="done" name="reviewer" href="javascript:void(0)" onclick="reviewer(2)" <c:if test="${costParam.reviewer eq 2 }">class="select"</c:if>>本人已审批</a>--%>
  <%--<a id="notdo" name="reviewer" href="javascript:void(0)" onclick="reviewer(3)" <c:if test="${costParam.reviewer eq 3 }">class="select"</c:if>>非本人审批</a>--%>
  <p class="main-right-topbutt">
    <a style="font-size:14px;padding:10px;margin:-20px;margin-right: 10px;" href="${ctx }/report/define/addOrModifyReportDefine" target="_blank" onclick="">添加报表定义</a>
  </p>
</div>

<table id="contentTable" class="table activitylist_bodyer_table">
  <thead>
    <tr>
      <th width="3%">序号</th>
      <th width="8%">名称</th>
      <th width="9%">描述</th>
      <th width="9%">SQL</th>
      <th width="6%">创建人</th>
      <th width="8%">创建时间</th>
      <th width="5%">操作</th>
    </tr>
  </thead>
  <tbody>
    <c:if test="${fn:length(page.list) <= 0 }">
      <tr>
        <td class="tc" colspan="7">暂无报表定义记录</td>
      </tr>
    </c:if>
    <c:forEach items="${page.list}" var="reportDefine" varStatus="s">
      <tr>
        <td class="tc">${s.count}</td>
        <td class="tc">${reportDefine.name}</td>
        <td class="tc">${reportDefine.description}</td>
        <td class="tc">${reportDefine.reportQuery}</td>
        <td class="tc">${fns:getUserById(reportDefine.createBy).name}</td>
        <td class="tc"><fmt:formatDate value="${reportDefine.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
        <td class="p00">
          <dl class="handle">
            <a href="${ctx}/report/define/addOrModifyReportDefine?id=${reportDefine.id}" target="_blank">修改</a>&nbsp;
            <a href="javascript:void(0)"  onclick="deleteReportDefine(${reportDefine.id})">删除</a>
          </dl>
        </td>
      </tr>
    </c:forEach>
  </tbody>
</table>
<div class="page">
  <div class="pagination">
    <div class="endPage">${page}</div>
    <div style="clear:both;"></div>
  </div>
</div>

</body>
</html>
