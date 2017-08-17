<%--
  Created by Changying huo IDEA.
  User: quauq
  Date: 2017/1/24
  Time: 17:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="decorator" content="wholesaler" />
    <title>微信账号确认匹配</title>
    <link rel="stylesheet" href="${ctxStatic }/css/wechatMatching.css"></head>

<body>
<page:applyDecorator name="agent_op_head">
</page:applyDecorator>
<input type="hidden" value="${ctx}" id="ctx">
<div class="wechat_table wechat_top">
    <table>
        <thead>
        <tr>
            <th>微信记录信息</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><span class="width_80">手机号：</span><span id="phone" class="adopt_use">${userInfo.wxMobile}</span></td>
            <td><span class="use_btn" data-id="0">采用</span></td>
        </tr>
        <tr>
            <td><span class="width_80">姓名：</span><span id="name" class="adopt_use">${userInfo.wxName}</span></td>
            <td><span class="use_btn" data-id="1">采用</span></td>
        </tr>
        <tr>
            <td><span class="width_80">渠道名称：</span><span id="company" class="adopt_use">${userInfo.wxAgentName}</span></td>
            <td><span class="use_btn" data-id="2">采用</span></td>
        </tr>
        <tr>
            <td><span class="width_80">微信号：</span><span id="wechatNum" class="adopt_use">${userInfo.wxChatCode}</span></td>
            <td><span class="use_btn" data-id="3">采用</span></td>
        </tr>
        <tr class="no_border">
            <td><span class="width_80">座机号：</span><span id="telephone" class="adopt_use">${userInfo.wxPhone}</span></td>
            <td><span class="use_btn" data-id="4">采用</span></td>
        </tr>
        </tbody>
    </table>
    <table>
        <thead>
        <tr>
            <th>渠道账号信息</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><span class="width_80">手机号：</span><span id="channel_phone" class="adopt_use">${userInfo.agentMobile}</span></td>
            <td><span class="use_btn" data-id="0">采用</span></td>
        </tr>
        <tr>
            <td><span class="width_80">姓名：</span><span id="channel_name" class="adopt_use">${userInfo.agentContactName}</span></td>
            <td><span class="use_btn" data-id="1">采用</span></td>
        </tr>
        <tr>
            <td><span class="width_80">渠道名称：</span><span id="channel_company" class="adopt_use">${userInfo.agentName}</span></td>
            <td><span class="use_btn" data-id="2">采用</span></td>
        </tr>
        <tr>
            <td><span class="width_80">微信号：</span><span id="channel_wechatNum" class="adopt_use">${userInfo.agentWeixin}</span></td>
            <td><span class="use_btn" data-id="3">采用</span></td>
        </tr>
        <tr class="no_border">
            <td><span class="width_80">座机号：</span><span id="channel_telephone" class="adopt_use">${userInfo.agentPhone}</span></td>
            <td><span class="use_btn" data-id="4">采用</span></td>
        </tr>
        </tbody>
    </table>
</div>
<div class="wechat_table">
    <form method="post" action="${ctx}/mobileUser/confirmCorrelation" id="form">
        <input name="mobileUserId" type="hidden" value="${userInfo.mobileUserId}">
        <input name="userId" type="hidden" value="${userInfo.userId}">
        <input name="agentId" type="hidden" value="${userInfo.agentId}">
        <table>
            <thead>
            <tr>
                <th>确认合并信息</th>
            </tr>
            </thead>
            <tbody>
            <tr class="height_20">
                <td></td>
            </tr>
            <tr>
                <td><span class="width_80"><i class="red">*</i> 手机号：</span>
                    <input class="checkout_use " type="text" name="agentContactMobile" maxlength="20">
                    <span class="red checkout_text">必填项</span>
                </td>
            </tr>
            <tr>
                <td><span class="width_80"><i class="red">*</i> 姓名：</span>
                    <input class="checkout_use" type="text" name="agentContact" maxlength="9">
                    <span class="red checkout_text">必填项</span>
                </td>
            </tr>
            <tr>
                <td><span class="width_80"><i class="red">*</i> 渠道名称：</span>
                    <input class="checkout_use" type="text" name="agentName" maxlength="50">
                    <span class="red checkout_text">必填项</span>
                </td>
            </tr>
            <tr>
                <td><span class="width_80"><i class="red">*</i> 微信号：</span>
                    <input class="checkout_use" type="text" name="wxCode" maxlength="20">
                    <span class="red checkout_text">必填项</span>
                </td>
            </tr>
            <tr>
                <td><span class="width_80">座机号：</span>
                    <input class="input_use_left" type="text" name="leftAgentContactTel" maxlength="20">
                    -
                    <%--需求调整，座机号码允许输入20位 ymx Start--%>
                    <input class="input_use_right" type="text" name="rightAgentContactTel" maxlength="20">
                    <%--需求调整，座机号码允许输入20位 ymx End--%>
                </td>
            </tr>
            <tr>
                <td><span class="width_80"><i class="red">*</i> 渠道品牌：</span>
                    <input class="checkout_use" type="text" name="agentBrand" value="${userInfo.agentBrand}" maxlength="50">
                    <span class="red checkout_text">必填项</span>
                </td>
            </tr>
            <tr>
                <td id="area_use"><span class="width_80"><i class="red">*</i> 地址：</span>
                    <select name="agentAddress" id="country" class="checkout_use_select">
                        <option value="">国家</option>
                        <c:forEach  items="${areaMap}" var="area">
                            <option value="${area.key}" <c:if test="${agentinfo.agentAddress eq area.key }">selected='selected'</c:if> >${area.value}</option>
                        </c:forEach>
                    </select>
                    <select name="agentAddressProvince" id="province">
                        <option value="">省(直辖市)</option>
                        <c:forEach items="${addressProvinceMap}" var="prvc">
                            <option value="${prvc.key}" <c:if test="${agentinfo.agentAddressProvince eq prvc.key }">selected='selected'</c:if> >${prvc.value}</option>
                        </c:forEach>
                    </select>
                    <select name="agentAddressCity" id="city">
                        <option value="">市(区)</option>
                        <c:forEach items="${addressCityMap}" var="city">
                            <option value="${city.key}" <c:if test="${agentinfo.agentAddressCity eq city.key }">selected='selected'</c:if> >${city.value}</option>
                        </c:forEach>
                    </select>
                    <input type="text" name="agentAddressStreet" maxlength="100" value="${agentinfo.agentAddressStreet }">
                    <span class="red checkout_text">必填项</span>
                </td>
            </tr>
            <tr class="height_20">
                <td></td>
            </tr>
            </tbody>
        </table>
    </form>
</div>
<div class="btn_use">
    <span id="checkout">确认合并</span>
    <span id="cancel1" onclick="history.go(-1)">取消</span>
</div>
<script type="text/javascript" src="${ctxStatic }/js/wechatMatching.js"></script>
</body>
</html>
