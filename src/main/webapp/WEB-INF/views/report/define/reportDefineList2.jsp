<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
  <title>报表定义列表</title>
  <meta name="decorator" content="wholesaler"/>
  <script type="text/javascript">

    $(function() {
      //加载操作按键
      $('.handle').hover(function() {
        if(0 != $(this).find('a').length){
          $(this).addClass('handle-on');
          $(this).find('dd').addClass('block');
        }
      },function(){$(this).removeClass('handle-on');$(this).find('dd').removeClass('block');});

      $(".qtip").tooltip({
        track: true
      });

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
                $(searchFormselect[i]).children("option:selected").val() != null) {
          selectRequest = true;
        }
      }
      if(inputRequest||selectRequest) {
        $('.zksx').click();
      }

      jQuery.extend(jQuery.validator.messages, {
        required: "必填信息"
      });
      $('.nav-tabs li').hover(function(){
        $('.nav-tabs li').removeClass('current');
        $(this).parent().removeClass('nav_current');
        if($(this).hasClass('ernav')){
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

      var _$orderBy = $("#orderBy").val();
      if(_$orderBy==""){
        _$orderBy="createDate DESC";
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

    function page(n,s){
      $("#pageNo").val(n);
      $("#pageSize").val(s);
      $("#searchForm").submit();
    }

    function sortby(sortBy,obj){
      var temporderBy = $("#orderBy").val();
      if(temporderBy.match(sortBy)){
        sortBy = temporderBy;
        if(sortBy.match(/ASC/g)){
          sortBy = $.trim(sortBy.replace(/ASC/g,"")) + " DESC";
        }else{
          sortBy = $.trim(sortBy.replace(/DESC/g,""))+" ASC";
        }
      }else{
        sortBy = sortBy+" DESC";
      }

      $("#orderBy").val(sortBy);
      $("#searchForm").submit();
    }

  </script>
</head>
<body>

  <div class="activitylist_bodyer_right_team_co_paixu">
    <div class="activitylist_paixu">
      <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
      <div class="kong"></div>
    </div>
  </div>

  <div class="activitylist_bodyer_right_team_co_bgcolor inventory_management">
    <table id="contentTable" class="table activitylist_bodyer_table">
      <thead>
        <tr>
          <th width="5%">序号</th>
          <th width="5%">名称</th>
          <th width="9%">描述</th>
          <th width="9%">SQL</th>
          <th width="6%">创建人</th>
          <th width="8%">创建时间</th>
          <th width="10%">操作</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${page.list}" var="reportDefine" varStatus="s">
          <tr>
            <td class="tc">${s.count}</td>
            <td class="tc word-wrap">${reportDefine.name}</td>
            <td class="tc">${reportDefine.description}</td>
            <td class="tc">${reportDefine.reportQuery}</td>
            <td class="tc">${reportDefine.createBy}</td>
            <td class="tc"><fmt:formatDate value="${reportDefine.createDate}" pattern="yyyy-MM-dd" /></td>
            <td class="p00">
              <dl class="handle">
                <a href="${ctx}/cost/manager/forcastList/${activity.id}/${typeId}" target="_blank">修改</a>&nbsp;
                <a href="${ctx}/cost/manager/settleList/${activity.id}/${typeId}" target="_blank">删除</a>
              </dl>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>

  <div class="page">
    <div class="pagination">
      <div class="endPage">${page}</div>
      <div style="clear:both;"></div>
    </div>
  </div>

</body>
</html>