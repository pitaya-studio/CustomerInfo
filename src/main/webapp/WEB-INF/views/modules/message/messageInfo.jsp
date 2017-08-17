<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>公告详情</title>
<meta name="decorator" content="wholesaler" />
<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"  type="text/javascript"></script>
<script type="text/javascript">
	function closeWin(){
		window.close();
	}
</script>
</head>
<body>
<!--右侧内容部分开始-->
   <div class="noticeDetails">
       <a onclick="closeWin()" class="ydbz_s gray">返　回</a>
       <div class="noticeDetails-title">${msg.title } </div>
       <div class="noticeDetails-nav">
       	<span>发布者：${user.name }</span>
       	<c:if test="${msg.msgType!=4 }">
	       	<span>公告类型：
	       		<c:if test="${msg.msgNoticeType==1 }">单团</c:if>
	       		<c:if test="${msg.msgNoticeType==2 }">散拼</c:if>
	       		<c:if test="${msg.msgNoticeType==3 }">游学</c:if>
	       		<c:if test="${msg.msgNoticeType==4 }">大客户</c:if>
	       		<c:if test="${msg.msgNoticeType==5 }">自由行</c:if>
	       		<c:if test="${msg.msgNoticeType==6 }">签证</c:if>
	       		<c:if test="${msg.msgNoticeType==7 }">机票</c:if>
	       		<c:if test="${msg.msgNoticeType==8 }">其他</c:if>
	       	</span>
       	</c:if>
       	<span>公告范围：
			<c:if test="${msg.msgType==1}">全站公告</c:if>
			<c:if test="${msg.msgType==2 }">部门公告</c:if>
			<c:if test="${msg.msgType==3 }">渠道公告</c:if>
			<c:if test="${msg.msgType==4 }">约签公告</c:if>
			<c:if test="${msg.msgType==5 }">消息</c:if>
		</span>
       	<span>发布时间：<fmt:formatDate value="${msg.createDate }"  pattern="yyyy-MM-dd"/></span>
       	
       	<span>过期时间：
	       	<c:if test="${not empty msg.overTime }">
	       		<fmt:formatDate value="${msg.overTime }"  pattern="yyyy-MM-dd"/>
	       	</c:if>
	       	<c:if test="${empty msg.overTime }">
	       		不限
	       	</c:if>
       	</span>
       </div>
       <div class="noticeDetails-cen">
       		<div>${msg.content }</div>
           <div class="noticeDetails-foot">
         	<ul>
         		<c:forEach items="${docList }"  var="doc">
         			<li><span>${doc.docName}</span>
         			<a  href="${ctx}/sys/docinfo/zipdownload/${doc.id}/${doc.docName}" >[下载]</a></li>
         		</c:forEach>
		    </ul>
           </div>
       </div>
   </div>
   <!--右侧内容部分结束-->
</body>
</html>
