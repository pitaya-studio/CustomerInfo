<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>基础信息维护-地域区域管理</title>
<link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/trekiz.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
$(function(){
	g_context_url = "${ctx}";
	})
//删除弹框
function del(id){
	if(!confirm("您确信要删除吗？")){
		return false;
	}
	 $.ajax({                 
			cache: true,                 
			type: "POST",                 
			url:g_context_url+ "/region/delete",                 
			data:{ 
				"id":id
				},                
			 async: false,                 
			 success: function(result){
			/*  if(result!=null&&result!=""){
				 top.$.jBox.tip(result);
				 return false;
			 } */
			 top.$.jBox.tip("删除成功");
			 window.location.href=g_context_url+"/region/regionList?lable="+$("#lable").val();
		}
	 });
}
function page(n,s){
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    $("#searchForm").submit();
    return ;
}
</script>
</head>
<body>
       <page:applyDecorator name="sys_region" >
  		    <page:param name="current">${lable}</page:param>
	   </page:applyDecorator>
	   <form  id="searchForm" >
	   <input type="hidden" id="lable" name="lable" value="${lable}"/>
	   <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
	   <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	     </form> 
            	<!--右侧内容部分开始-->
				<div class="filter_btn">
                    <%--<a href="${ctx}/region/addRegion?lable=${lable}" class="btn btn-primary" target="_self">添加区域</a>--%>
                    <input class="btn btn-primary" onclick="window.open('${ctx}/region/addRegion?lable=${lable}')"value="添加区域" type="button">
                </div>
                <table class="t-type t-type100 tablemt50 mainTable">
                    <thead>
                        <tr>
                        	<th width="18%">区域名称</th>
                            <th width="">区域范围</th>
                            <th width="8%">状态</th>
                            <th width="12%">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                     <c:forEach var="entry" items="${page.list}" varStatus="v">
                    	<tr>
                        	<td>${entry.name }</td>
                            <td>${entry.nameCns }</td>
                            <td class="tc">
                            <c:if test="${entry.status==1}"><i class="basic_off"></i></c:if>
                            <c:if test="${entry.status==0}"><i class="basic_on"></i></c:if>
                            </td>
                            <td class="tc">
                            <a href="${ctx}/region/modifyRegion?lable=${lable}&id=${entry.id}" target="_self">修改</a>&#12288;
                            <a  onclick="del(${entry.id })">删除</a>&#12288;
                            <a href="${ctx}/region/showRegion?lable=${lable}&id=${entry.id}" target="_blank">详情</a>
                            </td>
                            <input id="region" name="id" value="${entry.id }" type="hidden"/>
                        </tr>
                      </c:forEach>
                    </tbody>
                </table>
             <!--右侧内容部分结束-->
        	<div class="pagination clearFix">${page}</div>
        	
</body>
</html>
