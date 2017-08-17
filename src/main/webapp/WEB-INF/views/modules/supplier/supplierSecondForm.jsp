<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>地接社管理-新增第2步-网站信息</title>
<meta name="decorator" content="wholesaler"/>
<!-- 页面左边和上边的装饰 -->
<link type="text/css" rel="stylesheet" href="${ctxStatic}jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}css/jquery.validate.min.css" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
function submit(){
	$("#inputForm").submit();
}

function check(){
	if (document.getElementById("supplierId").value != null && document.getElementById("supplierId").value != "") {
		top.$.jBox.tip('第二步保存成功', 'success');
		return true;
	} else {
		top.$.jBox.tip('请先填写基本信息', 'error');
		return false;
	}
}

function checkFirstForm(){
	top.$.jBox.tip('请先填写基本信息', 'error');
}
</script>
</head>
<body>
	<page:applyDecorator name="supplier_op_head">
		<page:param name="current">supplierAdd</page:param>
	</page:applyDecorator>
        <!--右侧内容部分开始-->
		<div class="supplierLine">
			<c:choose>
				<c:when test="${supplierId!=null && supplierId!='' }">
					<a href="${ctx}/supplier/supplierFirstForm?supplierId=${supplierId}">基本信息填写</a>
					<a href="${ctx}/supplier/supplierSecondForm?supplierId=${supplierId}" class="select">网站信息</a>
					<a href="${ctx}/supplier/supplierThirdForm?supplierId=${supplierId}">银行账户</a>
					<a href="${ctx}/supplier/supplierFourthForm?supplierId=${supplierId}">资质上传</a>
				</c:when>
				<c:otherwise>
					<a href="${ctx}/supplier/supplierFirstForm">基本信息填写</a>
					<a href="${ctx}/supplier/supplierSecondForm" class="select">网站信息</a>
					<a href="javascript:void(0)" onclick="checkFirstForm()">银行账户</a>
					<a href="javascript:void(0)" onclick="checkFirstForm()">资质上传</a>
				</c:otherwise>
			</c:choose>
		</div>
        <div class="sysdiv sysdiv_coupon">
        <form method="post" action="${ctx}/supplier/saveSecondForm" class="form-horizontal" id="inputForm" onsubmit="return check()">
        <input type="hidden" name="supplierId" <c:if test="${supplierId!=null }">value="${supplierId }"</c:if><c:if test="${supplierId==null }">value=""</c:if>/>
            <p>
              <label>网站状态：</label>
              <span class="checkboxdiv">
              <input type="checkbox" name="siteStatus" id="siteStatus" <c:if test="${websiteInfo.siteStatus == '1'}">checked="checked"</c:if>><label for="siteStatus">启用</label>
              </span>
			</p>
            <p>
              <label>浏览权限：</label>
              <span>
             <select name="scanAuthority">
             <option <c:if test="${websiteInfo.scanAuthority == '1'}">selected="selected"</c:if> value="1" >开放浏览</option>
             <option <c:if test="${websiteInfo.scanAuthority == '2'}">selected="selected"</c:if> value="2">登陆后浏览</option>
             </select>
              </span>
			</p>
            <p>
              <label>登陆账号：</label>
              <span>
              <input id="loginAccount" name="loginAccount" type="text" maxlength="50" value="${websiteInfo.loginAccount }">
              </span>
			</p>
            <p>
              <label>登录密码：</label>
              <span>
              <input name="loginPwd" type="text" maxlength="50" value="${websiteInfo.loginPwd }">
              </span>如果不填，默认密码将设为：111111
			</p>
            <p>
              <label>网站标题：</label>
              <span>
              <input name="siteTitle" type="text" maxlength="50" value="${websiteInfo.siteTitle }">
              </span>浏览器窗口上方显示内容
			</p>
			<p>
              <label>网站关键字：</label>
              <span>
              <textarea name="siteKeyWord" rows="3" maxlength="200">${websiteInfo.siteKeyWord }</textarea>
              </span>
			</p>
            <p>
              <label>网站描述：</label>
              <span>
              <textarea name="siteDescription" rows="3" maxlength="200">${websiteInfo.siteDescription }</textarea>
              </span>
			</p>
            <p>
              <label>网站地址：</label>
              <span><input id="siteUrl" name="siteUrl" type="text" maxlength="50" value="${websiteInfo.siteUrl }"></span>
			</p>
            <p>
              <label>网站负责人：</label>
             <span><input name="siteManager" type="text" maxlength="50" value="${websiteInfo.siteManager }"></span>
			</p>
            <p>
              <label>网站负责人电话：</label>
             <span><input name="siteManagerPhone" type="text" maxlength="20" value="${websiteInfo.siteManagerPhone }"></span>旅行社网站负责人电话
			</p>
            <p>
              <label>网站客服电话：</label>
             <span><input name="siteCustomerServicePhone" type="text" maxlength="20" value="${websiteInfo.siteCustomerServicePhone }"></span>旅行社网站客服电话
			</p>
            <p>
              <label>网站管理员邮箱：</label>
             <span><input name="siteAdminEmail" type="text" maxlength="50" value="${websiteInfo.siteAdminEmail }"></span>旅行社网站负责人邮箱
			</p>
            <p>
              <label>QQ在线客服号码：</label>
             <span><input name="qqCustomerService" type="text" maxlength="20" value="${websiteInfo.qqCustomerService }"></span>
			</p>
            <p>
              <label>&nbsp;</label>
              <span>
                <%--<a class="ydbz_x gray" href="${ctx}/supplier/supplierInfoList">返&nbsp;&nbsp;&nbsp;回</a>--%>
                <%--<a class="ydbz_x" href="${ctx}/supplier/supplierFirstForm?supplierId=${supplierId}">上一步</a>--%>
                <input type="button" value="返&nbsp;&nbsp;&nbsp;回" class="ydbz_x btn" onclick="location.href='${ctx}/supplier/supplierInfoList'">
                <input type="button" value="上一步" class="ydbz_x btn" onclick="location.href='${ctx}/supplier/supplierFirstForm?supplierId=${supplierId}'">
                <input type="submit" value="下一步" class="ydbz_x btn btn-primary submit">
              </span>
			</p>
          </form>
        </div>
        <!--右侧内容部分结束-->
</body>
</html>

