<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<script type="application/javascript">
    $(function(){
        var activeMark = '<sitemesh:getProperty property="showType" />';
        $('#' + activeMark).addClass('active');
        
        var isLMT = '<sitemesh:getProperty property="isLMT" />';
        if(isLMT=='true'){
             $('#island').hide();
			 $('#hotel').hide();
        }
    });

     function jump(href){
         var _m = '${_m}';
         var _mc = '${_mc}';
         href = appendParam(href, {_m : _m, _mc : _mc});
         window.location.href = href;
     }
</script>
<content tag="three_level_menu">
	<shiro:hasPermission name="all:activity:newreview">
		<li id="all"><a href="${ctx}/cost/review/listGroup/0/1">全部（除签证）</a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="single:activity:newreview">
        <li id="single"><a href="${ctx}/cost/review/listGroup/1/1">单团产品</a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="loose:activity:newreview">
        <li id="loose"><a href="${ctx}/cost/review/listGroup/2/1">散拼产品</a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="study:activity:newreview">
        <li id="study"><a href="${ctx}/cost/review/listGroup/3/1">游学产品</a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="bigCustomer:activity:newreview">
         <li id="bigCustomer"><a href="${ctx}/cost/review/listGroup/4/1">大客户</a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="free:activity:newreview">
        <li id="free"><a href="${ctx}/cost/review/listGroup/5/1">自由行</a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="visa:activity:newreview">
       <li id="visa"><a href="${ctx}/cost/review/listGroup/6/1">签证产品</a></li>
 	</shiro:hasPermission>
 	<shiro:hasPermission name="airticket:activity:newreview">
        <li id="airticket"><a href="${ctx}/cost/review/listGroup/7/1">机票产品</a></li>  
    </shiro:hasPermission>   
    <shiro:hasPermission name="cruise:activity:newreview"> 
        <li id="cruise"><a href="${ctx}/cost/review/listGroup/10/1">游轮产品</a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="island:activity:newreview">
        <li id="island"><a href="${ctx}/cost/review/listGroup/12/1">海岛游产品</a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="hotel:activity:newreview">
        <li id="hotel"><a href="${ctx}/cost/review/listGroup/11/1">酒店产品</a></li>
    </shiro:hasPermission>

 
        
</content>

