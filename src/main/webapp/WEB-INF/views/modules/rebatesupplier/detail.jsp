<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>供应商管理-详细信息</title>

<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>

</head>
<body>
<style type="text/css">
	label{ cursor:inherit;}
	.input_supplier{
		width: 150px;
	}
	.sysdiv p label,.coverarea-label{
		float:left;
		text-align: right;
		line-height:25px;
		margin:0;
		cursor:text;
		width:115px;
	}
</style>
<page:applyDecorator name="agent_op_head" >

</page:applyDecorator>
	<c:if test="${not empty supplier}">
         <div class="sysdiv sysdiv_coupon">
			 <p>
				 <label><em class="xing">*</em>供应商品牌：</label>
				 <span><input id="supplierBrand" name="brand" type="text" maxlength="50" readonly="readonly" value="${supplier.brand}"></span>
			 </p>
			 <p>
				 <label><em class="xing">*</em>供应商名称：</label>
				 <span><input id="supplierName" name="name" type="text" maxlength="50" readonly="readonly" value="${supplier.name}"></span>
			 </p>
			 <p>
				 <label>英文名称：</label>
				 <span><input name="enName" type="text" maxlength="50" readonly="readonly" value="${supplier.enName}"></span>
			 </p>
			 <p>
				 <label>公司地址：</label>
				 <span><input name="address" type="text" maxlength="50" readonly="readonly" value="${supplier.address}"></span>
			 </p>
			 <p>
				 <label>负责人：</label>
				 <span><input  name="personInCharge" type="text" maxlength="50" readonly="readonly" value="${supplier.personInCharge}"></span>
			 </p>
			 <p>
				 <label>跟进计调人员：</label>
				 <span><input  name="operatorName" type="text" maxlength="50" readonly="readonly" value="${supplier.operatorName}"></span>
			 </p>
			 <p>
				 <label>邮政编码：</label>
				 <span><input name="postcode" type="text" class="input_supplier" readonly="readonly" value="${supplier.postcode}"></span>
			 </p>
			 <p>
				 <label>电话：</label>
				 <span><input name="telephone" type="text" class="input_supplier" readonly="readonly" value="${supplier.telephone}"></span>
			 </p>
			 <p>
				 <label>传真：</label>
				 <span><input name="fax" type="text" class="input_supplier" readonly="readonly" value="${supplier.fax}"></span>
			 </p>
			 <p>
				 <label>邮箱：</label>
				 <span><input name="email" type="text" class="input_supplier" readonly="readonly" value="${supplier.email}"></span>
			 </p>
			 <p>
				 <label>描述：</label>
              <span>
              <textarea name="description" rows="3" class="input-xlarge" maxlength="200" readonly="readonly" value="${supplier.description}"></textarea>
              </span>
			 </p>
        </div>
	</c:if>
		<%--银行信息--%>
		<div class="qdgl-cen"><%--境内账户--%>
			<c:if test="${(not empty domesticBanks) and fn:length(domesticBanks)>0}">
				<h5>境内账户</h5>
				<c:forEach items="${domesticBanks }" var="bank" varStatus="status">
					<dl class="account">
						<dt><span><input name="domesticDefaultFlag" type="radio" disabled="disabled" altattr="radio"  <c:if test="${bank.defaultFlag == 0 }">checked="checked"</c:if>  value="${bank.defaultFlag}" /></span><b>默认账户</b></dt>
						<dd><span>账户名：</span><input name="accountName" type="text" value="${bank.accountName}" readonly="readonly"/></dd>
						<dd><span>开户行名称：</span><input name="bankName" type="text" value="${bank.bankName}" readonly="readonly"/></dd>
						<dd><span>开户行地址：</span><input name="bankAddr" type="text" value="${bank.bankAddr}" readonly="readonly"/></dd>
						<dd><span>账户号码：</span><input name="bankAccountCode" type="text" value="${bank.bankAccountCode}" readonly="readonly"/></dd>
						<dd><span>备注：</span><textarea name="remarks" cols="" rows="" readonly="readonly">${bank.remarks}</textarea></dd>
					</dl>
				</c:forEach>
			</c:if>
<%--		</div>
		<div class="qdgl-cen">&lt;%&ndash;境外账户&ndash;%&gt;--%>
		  <c:if test="${(not empty overseasBanks) and fn:length(overseasBanks)>0}">
			  <h5>境外账户</h5>
			  <c:forEach items="${overseasBanks }" var="bank" varStatus="status">
				<dl class="account">
					<dt><span><input name="overseasDefaultFlag" type="radio" disabled="disabled" altattr="radio"  <c:if test="${bank.defaultFlag == 4 }">checked="checked"</c:if>  value="${bank.defaultFlag}" /></span><b>默认账户</b></dt>
					<dd><span>账户名：</span><input name="accountName" type="text" value="${bank.accountName}" readonly="readonly"/></dd>
					<dd><span>开户行名称：</span><input name="bankName" type="text" value="${bank.bankName}" readonly="readonly"/></dd>
					<dd><span>开户行地址：</span><input name="bankAddr" type="text" value="${bank.bankAddr}" readonly="readonly"/></dd>
					<dd><span>账户号码：</span><input name="bankAccountCode" type="text" value="${bank.bankAccountCode}" readonly="readonly"/></dd>
					<dd><span>Rounting：</span><input  name="rounting" type="text" value="${bank.rounting}" readonly="readonly"/></dd>
					<dd><span>Swift Number：</span><input name="swiftNum" type="text" value="${bank.swiftNum}" readonly="readonly"/></dd>
					<dd><span>Phone Number：</span><input  name="phoneNum" type="text" value="${bank.phoneNum}" readonly="readonly"/></dd>
					<dd><span>备注：</span><textarea cols="" rows="" readonly="readonly">${bank.remarks}</textarea></dd>
				</dl>
			  </c:forEach>
		  </c:if>
		</div>

		<div class="dbaniu " style=" margin-left:100px;"><a class="ydbz_s" href="javascript:void(0)" onclick="window.opener=null;window.close();">关闭</a></div>

  <!--footer***end--> 
<!-- </div> -->
</body>
</html>
