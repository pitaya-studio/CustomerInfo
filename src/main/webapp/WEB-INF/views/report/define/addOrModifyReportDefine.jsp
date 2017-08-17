<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
  <title>添加/修改报表定义</title>

  <meta name="decorator" content="wholesaler" />
  <%--<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>--%>
  <%--<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>--%>
  <%--<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>--%>
  <%--<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>--%>
  <%--<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>--%>
  <%--<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>--%>
  <%--<script src="${ctxStatic}/common/jquery.disabled.js" type="text/javascript"></script>--%>

  <style type="text/css">
    input[disabled],select[disabled],textarea[disabled],input[readonly],select[readonly],textarea[readonly],
    input[disabled]:focus,select[disabled]:focus,textarea[disabled]:focus,input[readonly]:focus, select[readonly]:focus,textarea[readonly]:focus{
      cursor:auto;
      background:transparent;
      border:0px;
      box-shadow:inset 0 0px 0px rgba(0,0,0,0.075)
    }
  </style>

  <script type="text/javascript">
    $(document).ready(function() {
      // 提交表单
      $("#issue").on("click",function(){
        $("#issue").attr("disabled","disabled");
//        valiParam(1);
        window.setTimeout(function(){
          $("#issue").removeAttr("disabled");
        },3000);
      });

      $("#preview").on("click",function() {
        var sqls = $("#sql").val();
        var href = $("#preview").attr("href");
        $("#preview").attr("href", href + "?sqls=" + sqls);
      });
    });

    // 校验不合格参数
    function valiParam(type){
      var content = $("#content").val();
      $("#index").val(content);
      var title = $("input[name=title]").val(); // 获取标题值
      if(title && content){
        var backTitle = $.trim(title);
        var backContent = $.trim(content);
        var bool = true;
        if(backTitle.length>20 || backTitle.length<1){
          $.jBox.tip("提交失败，公告标题不要超过20个字","提示");
          bool = false;
        }
        if(bool && (backContent.length>10000 || backContent.length<1)){
          $.jBox.tip("提交失败，公告内容不要超过10000个字","提示");
          bool = false;
        }
        if(bool && (backTitle.length>0 && backTitle.length<20 && backContent.length>0 && backContent.length<10000)){
          subForm(type);
          $.jBox.tip("发布成功。", 'success');
          cleanForm();
        }
      }else{
        $.jBox.tip("提交失败，请检查您输入的数据","提示");
      }
    }
    // 提交表单
    function subForm(type){

      var the_param = $("#addMessage").serialize();
      the_param += "&saveStatus="+type,
              $.ajax({
                type : "POST",
                url :  "${ctx}/message/addAJaxMsg",
                data : the_param,
                dataType : "text"
              });
    }
    // 清空表单
    function cleanForm(){
      $("input[name=title]").val("");
      $("#index").val(""); // 干掉正文预览
      $('#content').val(""); // 干掉上传正文
    }

    function preview(){
      var sqls = $("#sql").val();
      window.location.href = "${ctx}/report/define/preview?sqls =" + sqls;
      <%--$.ajax({--%>
        <%--type: "POST",--%>
        <%--url: "${ctx}/report/define/preview",--%>
        <%--cache:false,--%>
        <%--async:false,--%>
        <%--data:{sqls : sqls},--%>
        <%--success: function(data){--%>
<%--//          top.$.jBox.tip(data.message, data.result);--%>
<%--//          window.location.reload();--%>
        <%--},--%>
        <%--error : function(data){--%>
<%--//          top.$.jBox.tip(date.message, data.result);--%>
<%--//          return false;--%>
        <%--}--%>
      <%--});--%>
    }
  </script>
</head>
<body>

<div class="sysdiv">
  <form id="addMessage" class="form-horizontal" action="${ctx}/report/define/saveOrUpdateReportDefine">
    <input type="hidden"  name="msgType"  value="4"/>
    <div class="control-group">
      <label class="control-label">名称：</label>
      <div class="controls">
        <input id="name" name="name" class="input-xxlarge required measure-input"  type="text"  maxlength="200" value="${reportDefine.name}"/>
      </div>
    </div>
    <div class="control-group">
      <label class="control-label">描述：</label>
      <div class="controls">
        <textarea id="description" name="description"  class="input-xxlarge">${reportDefine.description}</textarea>
      </div>
    </div>
    <div class="control-group">
      <label class="control-label">模板：</label>
      <div class="controls">
        <select id="templateId" name="templateId" >
          <option value="0">请选择</option>
          <c:forEach items="${templates }" var="template">
            <option value="${template.id }" <c:if test="${reportDefine.templateId eq template.id }">selected="selected"</c:if>>${template.agentName }</option>
          </c:forEach>
        </select>
      </div>
    </div>
    <div class="control-group">
      <label class="control-label">SQL语句：</label>
      <div class="controls">
        <textarea id="sql" name="sql" class="input-xxlarge">${reportDefine.reportQuery}</textarea>
        <br>
        （多个sql请使用&nbsp;<font color="red">;</font>&nbsp;分隔）
      </div>
    </div>
    <div class="release_next_add" style="width:905px;">
      <a id="preview" style="height: 20px; width: auto" class="btn btn-primary" href="${ctx }/report/define/preview" target="_blank">预&nbsp;&nbsp;&nbsp;览</a>&nbsp;&nbsp;
      <input id="issue" class="btn btn-primary" type="submit" value="保&nbsp;&nbsp;&nbsp;存"/>&nbsp;
      <input id="cancel" class="btn btn-primary gray" type="button" value="关&nbsp;&nbsp;&nbsp;闭" onclick="window.close();" />
    </div>
  </form>
</div>

</body>
</html>
