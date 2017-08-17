<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/> 
<link rel="stylesheet" type="text/css" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css">
<link type="text/css"  rel="stylesheet" href="${ctxStatic}/css/channelPricing.css" />
<link type="text/css"  rel="stylesheet" href="${ctxStatic}/css/changeList.css" />
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/modules/agentToOffice/pricingStrategyFrame.js"></script>
<div class="wrap">
	<c:if test="${empty page || fn:length(page) eq 0}">
	没有修改记录
	</c:if>
	<c:if test="${fn:length(page) ne 0 }">
    <div class="op-table">
            <table>
                <thead>
                <tr class="table-lists-header">
                    <th style="width:160px;">操作时间</th>
                    <th style="width:80px;">操作人</th>
                    <th style="width:380px;"   class="td-last">操作内容</th>
                </tr>
                </thead>
                <c:forEach items="${page }" var="entity">
                <tbody>
                <tr>
                    <td> <fmt:formatDate value="${entity.create_date }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>${fns:getUserById(entity.create_by).name }</td>
                    <td>
                        <div><label></label><span>${entity.content }</span></div>
                    </td>
                </tr>
                </tbody></c:forEach>
            </table>
        </div>
    </div></c:if>
</div>