<%--
  Created by IntelliJ IDEA.
  User: ZhengZiyu
  Date: 2014/9/25
  Time: 16:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<script type="text/javascript">
    $(function(){
        var activeMark = '<sitemesh:getProperty property="current" />';
        $('#' + activeMark).addClass('active');
    });

    function jump(href){
        var _m = '${_m}';
        var _mc = '${_mc}';
        href = appendParam(href, {_m : _m, _mc : _mc});
        window.location.href = href;
    }
</script>