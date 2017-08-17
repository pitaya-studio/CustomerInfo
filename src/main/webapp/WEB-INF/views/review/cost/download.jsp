<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head lang="en">
    <meta charset="UTF-8">
    <title>附件下载框</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <link href="${ctxStatic}/css/bootstrap.css" type="text/css" rel="stylesheet"/>
    <link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet"/>
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/jh-style.css" type="text/css" rel="stylesheet"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/huanqiu-style.css"/>

    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>

    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
    
    <script type="text/javascript">
    	function download(id){
	         window.open("${ctx}/sys/docinfo/download/"+id);
  		}
    </script>
    <style type="text/css">
        .pay_record_new ul li label.docName{
            max-width:400px;
            overflow:hidden;
            text-overflow:ellipsis;
            white-space:nowrap;
            word-break:break-all;
            margin-bottom:-5px;
            text-align:left;
        }
        .pay_record_new ul li label{
            width:auto
        }
    </style>
</head>
<body>
    <!-- 下载浮框开始-->
    <!--<div name="downloadWin" class="display-none">-->
        <div class="pay_record_new">
            <ul>
            	<c:forEach items="${docInfoList }" var="docInfo" varStatus="s">
	                <li>
	                    <label class="docName" title="${docInfo.docName }">${docInfo.docName }</label>
	                    <em class="ico-download" title="下载" onclick="download(${docInfo.id})"></em>
	                    <c:if test="${userId eq docInfo.createBy.id and delFlag eq 1 }">
<!-- 	                    	<em class="ico-del" title="删除" onclick="deleteAttachment('${ctx}',this,${docInfo.id },${costId })"></em> -->
	                    </c:if>
	                </li>
                </c:forEach>
            </ul>
        </div>
    <!--</div>-->
    <!-- 下载浮框结束-->
</body>
</html>