<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler"/>
<title>基础信息-地理区域-详情</title>
<link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
<link type="text/css" rel="stylesheet" href="jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.css"><!--树形插件的样式-->
<link type="text/css" rel="stylesheet" href="css/jquery.mCustomScrollbar.css" /><!--滚动条插件样式-->
<script type="text/javascript" src="js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.js"></script><!--树形插件的脚本-->
<script type="text/javascript" src="jquery-ztree/3.5.12/js/jquery.ztree.excheck-3.5.js"></script><!--树形插件的脚本-->
<script type="text/javascript" src="js/jquery.mousewheel.min.js"></script><!--滚动条插件脚本-->
<script type="text/javascript" src="js/jquery.mCustomScrollbar.js"></script><!--滚动条插件脚本-->
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript">
</script>
</head>
<body>
        <page:applyDecorator name="sys_regionDetail" >
  		    <page:param name="current">${lable}</page:param>
	    </page:applyDecorator>
	    <input type="hidden" id="labelVal" name="lable" value="${lable}"/>
				<!--右侧内容部分开始-->
                <form id="inputForm" class="form-horizontal" action="#" method="post">
                    
					<div class="sysdiv sysdiv_coupon">
                    	<p>
                            <label>状态：</label>
                            <span>
                              <c:if test="${sysRegion.status==0}">启用</c:if> 
                              <c:if test="${sysRegion.status==1}">停用</c:if> 
                            </span>
                        </p>
                    	<p>
                            <label>区域名称：</label>
                            <span>
                               ${sysRegion.name}
                            </span>
                        </p>
                        <p>
                            <label>覆盖范围：</label>
                            <span>
                                 ${sysRegion.nameCns}
                            </span>
                        </p>
                       	<div style="width:100%; height:1px;"></div>
                        <p class="maintain_pfull">
                            <label>区域描述：</label>
                            <span>
                                 ${sysRegion.description} 
                            </span>
                         </p>
                    </div>
                    <div class="release_next_add">
                        <input type="button" class="btn btn-primary gray"  onclick="javascript:window.close();"value="关闭" />
                    </div>
              	</form>
                <!--右侧内容部分结束-->     
</body>
</html>
