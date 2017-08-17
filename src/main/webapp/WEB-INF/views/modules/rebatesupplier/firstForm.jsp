<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>供应商管理-新增基本信息</title>

<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>

<script type="text/javascript">

// $(document).ready(function() {
// 	$("#inputForm").validate({
// 		//-------校验开始--------
// 		onfocusout:function(element){ //失去焦点就进行校验
// 			$(element).valid();
// 		},rules:{  //校验规则
//     		agentBrand : {
//     			required : true
//     		}
//     	},messages:{ //校验的提示信息
//     		agentBrand :{
//     			required:'公告标题为必填项!'
//     		}
//     	}
//     	//--------校验结束--------
//     });
// });


	function submitForm(){
		var brandVal = $("#supplierBrand").val();
		var supplierName = $("#supplierName").val();
		var operatorId= $("select[name='operatorId']:visible").val();
		var b = true;
		if(brandVal==""||brandVal==null){
			b = false;
			top.$.jBox.info("供应商品牌没有填写", "警告");
			return;
		}
		if(supplierName==""||supplierName==null){
			b = false;
			top.$.jBox.info("供应商名称没有填写", "警告");
			return;
		}
		if(operatorId==""||operatorId==null){
			b = false;
			top.$.jBox.info("跟进计调为必填项", "警告");
			return;
		}
		if(b){
			$("#inputForm").submit();
		}
	}
</script>
<style type="text/css">
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
</head>
<body>
<style type="text/css">label{ cursor:inherit;}</style>
<page:applyDecorator name="agent_op_head" >

</page:applyDecorator>
<!-- <div id="sea"> -->
<!--   <div class="main"> -->
<!--     <div class="main-right"> -->
<!--       <ul class="nav nav-tabs"> -->
<!--       </ul> -->
<!--       <div class="bgMainRight">  -->
        <!--右侧内容部分开始-->
		<div class="supplierLine">
			<a href="javascript:void(0)" class="select">基本信息填写</a>
			<a href="javascript:void(0)">银行账户</a>
		</div>
        <div class="sysdiv sysdiv_coupon">

          <form  method="post" action="${ctx}/rebatesupplier/manager/saveFirstForm" class="form-horizontal" id="inputForm" name="inputForm" >
			<c:choose>
				<c:when test="${not empty supplier}">
					<input type="hidden" name="id" value="${supplier.id }"/>
					<p>
						<label><em class="xing">*</em>供应商品牌：</label>
						<span><input id="supplierBrand" name="brand" type="text" maxlength="50"  value="${supplier.brand}"></span>
					</p>
					<p>
						<label><em class="xing">*</em>供应商名称：</label>
						<span><input id="supplierName" name="name" type="text" maxlength="50" value="${supplier.name}"></span>
					</p>
					<p>
						<label>英文名称：</label>
					    <span><input name="enName" type="text" maxlength="50" value="${supplier.enName}"></span>
					</p>
					<p>
						<label>公司地址：</label>
					    <span><input name="address" type="text" maxlength="50" value="${supplier.address}"></span>
					</p>
					<p>
						<label>负责人：</label>
					    <span><input  name="personInCharge" type="text" maxlength="50" value="${supplier.personInCharge}"></span>
					</p>
					<p>
						<label><em class="xing">*</em>跟进计调人员：</label>
						  <span class="sysselect_s">
							<select name="operatorId" style="width: 220px">
								<option value="">请选择</option>
								<c:forEach items="${supplierOperators}" var="as">
									<option value="${as.key}" <c:if test="${as.key eq supplier.operatorId}">selected='selected'</c:if>>${as.value}</option>
								</c:forEach>
							</select>
							<a class="ydbz_x" href="javascript:void(0);" onclick="javascript:window.location.href='${ctx}/sys/user/form'">+新增计调员</a>
						  </span>
					</p>
					<p>
						<label>邮政编码：</label>
						<span><input name="postcode" maxlength="20"  type="text" value="${supplier.postcode}"></span>
					</p>
					<p>
						<label>电话：</label>
						<span><input name="telephone" maxlength="20" type="text"  value="${supplier.telephone}"></span>
					</p>
					<p>
						<label>传真：</label>
						<span><input name="fax" type="text" maxlength="20"  value="${supplier.fax}"></span>
					</p>
					<p>
						<label>邮箱：</label>
						<span><input name="email" type="text" maxlength="50"  value="${supplier.email}"></span>
					</p>
					<p>
						<label>描述：</label>
					    <span>
					    <textarea name="description" rows="3" class="input-xlarge" maxlength="200" >${supplier.description}</textarea>
					    </span>
					</p>
				</c:when>
				<c:otherwise>
					<p>
					  <label><em class="xing">*</em>供应商品牌：</label>
					  <span><input id="supplierBrand" name="brand" type="text" maxlength="50"></span>
					</p>
					<p>
					  <label><em class="xing">*</em>供应商名称：</label>
					  <span><input id="supplierName" name="name" type="text" maxlength="50"></span>
					</p>
					<p>
					  <label>英文名称：</label>
					  <span><input name="enName" type="text" maxlength="50"></span>
					</p>
					<p>
					  <label>公司地址：</label>
					  <span><input name="address" type="text" maxlength="50"></span>
					</p>
					<p>
					  <label>负责人：</label>
					  <span><input  name="personInCharge" type="text" maxlength="50"></span>
					</p>
					<p>
					  <label><em class="xing">*</em>跟进计调人员：</label>
					  <span class="sysselect_s">
						<select name="operatorId" style="width: 220px">
							<option value="">请选择</option>
							<c:forEach items="${supplierOperators}" var="as">
								<option value="${as.key}" <c:if test="${as.key eq userId}">selected='selected'</c:if>>${as.value}</option>
							</c:forEach>
						</select>
						<a class="ydbz_x" href="javascript:void(0);" onclick="javascript:window.location.href='${ctx}/sys/user/form'">+新增计调员</a>
					  </span>
					</p>
					<p>
					  <label>邮政编码：</label>
					  <span><input name="postcode" type="text" maxlength="20"></span>
					</p>
					<p>
					  <label>电话：</label>
					  <span><input name="telephone" type="text" maxlength="20"></span>
					</p>
					<p>
					  <label>传真：</label>
					 <span><input name="fax" type="text" maxlength="20" ></span>
					</p>
					<p>
						<label>邮箱：</label>
						<span><input name="email" type="text" maxlength="50" ></span>
					</p>
					<p>
					  <label>描述：</label>
					  <span>
					  <textarea name="description" rows="3" class="input-xlarge" maxlength="200"></textarea>
					  </span>
					</p>
				</c:otherwise>
			</c:choose>
            <p>
              <label>&nbsp;</label>
              <span>
                <a class="ydbz_x submit" href="javascript:void(0);" onclick="submitForm();">下一步</a>
              </span>
			</p>
          </form>
        </div>
        <!--右侧内容部分结束-->
</body>
</html>
