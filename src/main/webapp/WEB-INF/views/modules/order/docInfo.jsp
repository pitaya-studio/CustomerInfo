<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
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
    	function downloads(docid){
	         window.open("${ctx}/sys/docinfo/download/"+docid);
  		}
    </script>
</head>
<body>
	<!-- 下载浮框开始-->
    <!--<div name="downloadWin" class="display-none">-->
        <div class="pay_record_new">
            <ul>
               <c:forEach items="${docInfo }" var="doc">
                   <li>
                    <label>${doc.docName }</label>
                    <em class="ico-download" title="下载" onclick="downloads(${doc.id})"></em>
                  </li>
               </c:forEach>
            </ul>
        </div>
    <!--</div>-->
    <!-- 下载浮框结束-->
</body>
</html>