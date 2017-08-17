<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<% String path = request.getContextPath();%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	    <title>报名-申请详情</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<!-- 页面左边和上边的装饰 -->
		<meta name="decorator" content="wholesaler" />
		<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	</head>
  
	<body>
		<!-- 产品与团期信息 -->
		<%@ include file="/WEB-INF/views/modules/groupcover/groupInfo.jsp"%>
                               
		<div class="ydbz_tit"><span class="fl">补位申请</span></div>
		
		<div class="content">

                 <table id="contentTable" class="table activitylist_bodyer_table">
                     <thead>
                     <tr>
                         <th width="10%">补位号</th>
                         <th width="10%">补位人数</th>
                         <th width="10%">状态</th>
                         <th width="10%">申请提交时间</th>
                         <th width="10%">补位申请人</th>
                         <th width="10%">备注</th>
                     </tr>
                     </thead>
                     
                     <tbody>
                     <tr>
                         <td class="tc">${groupCover.coverCode}</td>
                         <td class="tc">${groupCover.coverPosition}</td>
                         <td class="tc" style="color:#eb0301">${groupCover.coverStatusName}</td>
                         <td class="tc"><fmt:formatDate value="${groupCover.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                         <td class="tc">${groupCover.createBy.name}</td>
                         <td class="tc">${groupCover.remarks}</td>
                     </tr>
                     </tbody>
                 </table>
        </div>
		
			<%-- <table style="margin-left:40px">
				<tbody>
					<tr>
						<td>补位人数：</td>
						<td>
							${groupCover.coverPosition}
						</td>
						<td width="100px" class="tr">备注：</td>
						<td>${groupCover.remarks}</td>
					</tr>
				</tbody>
			</table> --%>
			<div class="dbaniu" style="width:150px;">
				<a class="ydbz_s gray"onClick="window.close();">关闭</a>
			</div>
	</body>
</html>
