<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>预定-签证填写下单信息</title>

<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="X-UA-Compatible" content="IE=7,IE=9,IE=10" />
<link href="${ctxStatic}/bootstrap/2.3.1/css_default/bootstrap.min.css"
	type="text/css" rel="stylesheet" />
<!--[if lte IE 6]><link href="/trekiz_wholesaler/static/bootstrap/bsie/css/bootstrap-ie6.min.css" type="text/css" rel="stylesheet" />
<script src="/trekiz_wholesaler/static/bootstrap/bsie/js/bootstrap-ie.min.js" type="text/javascript"></script><![endif]-->
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css"
	rel="stylesheet" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css"
	rel="stylesheet" type="text/css" />
<link
	href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css"
	rel="stylesheet" type="text/css" />
<link href="${ctxStatic}/css/jh-style.css" type="text/css"
	rel="stylesheet" />
<link href="${ctxStatic}/css/huanqiu-style.css" type="text/css"
	rel="stylesheet" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"
	type="text/javascript"></script>
<link
	href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css"
	type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/common/mustache.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/trekiz.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/vendor.service_mode1.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js"
	type="text/javascript"></script>
<!-- 
<script
	src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js"
	type="text/javascript"></script>
 -->
<script src="${ctxStatic}/json/json2.js" type="text/javascript"></script>
<!-- 
<script src="${ctxStatic}/modules/activity/dynamic.validator.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
 -->
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/common/jquery.disabled.js"
	type="text/javascript"></script>
<style type="text/css">
input[disabled],select[disabled],textarea[disabled],input[readonly],select[readonly],textarea[readonly],input[disabled]:focus,select[disabled]:focus,textarea[disabled]:focus,input[readonly]:focus,select[readonly]:focus,textarea[readonly]:focus
	{
	cursor: auto;
	background: transparent;
	border: 0px;
	box-shadow: inset 0 0px 0px rgba(0, 0, 0, 0.075)
}
</style>

<script type="text/javascript">
	
	$(document).ready(function(){
		// 添加游客
		$("#addTraveler").click(function(){
			$(".warningtravelerNum").hide();
			travelerTemplate();
		});
		// 绑定删除游客信息框事件
		$("input[name='delbutton']").live("click",function(){
			//console.log($(this).attr('name'));
			delTravel($(this));
		});
		
		// 绑定增加其他费用信息框事件
		$("input[name='addcost']").live('click',function(){
			alert("1");
			addothercost($(this));
			alert("2");
		});
		
		// 绑定删除其他费用事件
		$("input[name='delPay']").live('click',function(){
			delPay($(this));
		});
	});
	
	// 根据模板生成游客信息框
	function travelerTemplate() {
		var $table = $("#travelerTemplate").children();
		var _travelerForm = $table.clone().addClass("travelerTable");
		$("#traveler").append(_travelerForm);
		
	}
	
	// 删除游客资料框
	function delTravel(obj){
		console.log(obj.attr('name'));
		console.log(obj.parent().parent().attr('class'));
		obj.parent().parent().parent().remove();
	}
	
	// 将游客姓名增加到右侧
	function addTravelName(){
		
	}
	
	// 增加其他费用信息框
	function addothercost(obj){
		var $table = $("#otherPayTemplate").children();
		var _otherdiv = $table.clone().addClass("otherPay");
		console.log(_otherdiv);
		obj.after(_otherdiv);
	}
	
	// 删除该费用
	function delPay(obj){
		obj.parent().remove();
	}
	
	// 上传文件
	function ydbz2interfile(obj){
		var dest = $(obj).parent().find("span");
		var res = $(obj).val();      		
    	$(dest).html(res);
    	var tabName = $(obj).parent().find("input.tabName");
    	$(tabName).val($(tabName).attr('name'));
    	console.log($(tabName).attr('name'));
    }
	
	
</script>

</head>

<body>

	<div class="main-right">


		<ul class="nav nav-tabs">
			<li class="active"><a href="#">预定</a></li>
		</ul>

		<div class="bgMainRight">
			<div class="ydbzbox fs">
				<div class="ydbz yd-step1 " id="stepbar">&nbsp;</div>
				<div class="ydbz_tit">订单基本信息</div>

				<!--<p class="ydbz_mc">JHGJLXS5535--预报名--001</p>-->
				<ul class="ydbz_info">
					<li><span>有效期：</span> <em class="fArial">${product.valid_period }
							${product.valid_period_unit }</em></li>
					<li><span>签证国家：</span>${country.countryName_cn }</li>
					<li><span>签证类别：</span> ${visaType }</li>
					<li><span>是否面试：</span>${needSpotAudition }</li>
					<li><span>预计工作日：</span>${forecastWorkingTime } 天</li>
					<li><span>入境次数：</span> ${visaProduct.enterNum } 次</li>
					<li><span>最多停留：</span> <em class="fArial"> ${visaProduct.stayTime }
							${visaProduct.stayTimeUnit } </em></li>
					<li><span>应收价格：</span>${currency.currencyMark} ${visaProduct.visaPay } ${currency.currencyName}</li>
					<li><span>办签人数：</span> ${visaOrder.travelNum }</li>
				</ul>

				<div class="ydbz_tit">
					<span class="ydExpand closeOrExpand"></span>填写预订人信息
				</div>
				<div flag="messageDiv">
					<p class="ydbz_qdmc">预订渠道： ${agentInfo.agentName }</p>
					<div class="contract">
						<ul class="ydbz_qd">
							<li><label><span class="xing">*</span>渠道联系人：<span
									id="agentContact">${agentInfo.agentContact }</span></label></li>
							<li><label><span class="xing">*</span>渠道联系人电话：<span
									id="agentTel">${agentInfo.agentTel }</span></label></li>
							<li><label>固定电话：<span id="agentFixedLine"></span></label> <!-- <span class="ydbz_x yd1AddPeople" onclick="yd1AddPeople(this)">展开</span> -->
							</li>
						</ul>
						<ul class="ydbz_qd">
							<li><label>通讯地址：<span id="agentAddress">${agentInfo.agentAddress }</span></label></li>
							<li><label>传真号码：<span id="agentFax">${agentInfo.agentFax }</span></label></li>
							<li><label>网络邮箱：<span id="agentEmail">${agentInfo.agentEmail }</span></label></li>
						</ul>

						<ul class="ydbz_qd">
							<li><label>QQ号码：<span id="agentQQ">${agentInfo.agentQQ }</span></label></li>
						</ul>
					</div>
				</div>
				<div id="manageOrder_m">
					<div id="contact">
						<div class="ydbz_tit">
							<span class="ydExpand closeOrExpand"></span> 特殊需求
						</div>
						<div class="ydbz2_lxr" flag="messageDiv">
							<form id="firstStepForm"
								action="${ctx}/visa/preorder/getOrderFirst" method="post">
								<!-- step 判断下单进行到第几步 -->
								<input type="hidden" id="step" name="step" value="2" />
								<!-- theagentinfo 判断渠道商ID -->
								<input type="hidden" id="getAgentinfoId" name="getAgentinfoId"
									value="${agentinfo.id }" />
								<!-- 签证订单类型，在本界面下达的订单均为单签订单 -->
								<input type="hidden" name="visaOrderType" id="visaOrderType"
									value="0" />
								<!-- 签证产品ID -->
								<input type="hidden" name="visaProductId" id="visaProductId"
									value="${productId }" />
								<p>
									<label style="vertical-align: top">特殊需求：</label>
									<textarea id="remark" name="remark" readonly>${visaOrder.remark }</textarea>
								</p>
							</form>
						</div>
					</div>
				</div>

				<div class="ydbz_tit">请填写游客信息</div>

				<div class="warningtravelerNum">暂无游客信息</div>

				<div id="traveler"></div>

				<!--添加游客按钮开始-->
				<div class="touristBtn">
					<a class="btn-addGrey" id="addTraveler">添加游客</a>
				</div>
				<!--添加游客按钮结束-->
				<div class="ydBtn ydBtn2">
					<a class="ydbz_s" id="secondToOneStepDiv">上一步</a> 
					<a class="ydbz_x" id="secondToThirdStepDiv">下一步</a>
				</div>
			</div>
		</div>

	</div>



	<div id="travelerTemplate" style="display: none;">
		<form action="${ctx}/visa/preorder/saveTravel" enctype="multipart/form-data" method="post">
			<input type="hidden" name="orderID" value="${visaOrder.id }"/>
			<div class="tourist">
				<div class="tourist-t">
				<!-- <a class="btn-del" style="cursor: pointer;" onclick="delTravel()">删除</a> -->	
					<input name="delbutton" type="button" class="btn-del" style="cursor: pointer;" value="删除"/>
					<label class="ydLable">游客<em class="travelerIndex"></em>:<span class="travelName"></span></label>
					<!-- <div class="tourist-t-off">
						<span class="fr">结算价：
							<span class="gray14">￥</span>
							<span class="ydFont2">1,600</span>
						</span>
					</div> -->	
					<div class="tourist-t-on">
						<label><input type="radio" class="traveler"
							name="personTypeinner" value="1" checked="checked" />在职</label> <label><input
							type="radio" class="traveler" name="personTypeinner" value="2" />退休</label>
						<label><input type="radio" class="traveler"
							name="personTypeinner" value="3" />学生</label>
					</div>
				</div>

				<div class="tourist-con" flag="messageDiv">
					<!--游客信息左侧开始-->
					<div class="tourist-left">
						<div class="ydbz_tit ydbz_tit_child">
							<em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>基本信息
						</div>
						<ul class="tourist-info1 clearfix" flag="messageDiv">
							<li><label class="ydLable"><span class="xing">*</span>姓名：</label>
								<input type="text" maxlength="30" name="travelerName"
								class="traveler"
								onkeyup="this.value=this.value.replaceSpecialChars()"
								onafterpaste="this.value=this.value.replaceSpecialChars()">
							</li>

							<li><label class="ydLable">英文／拼音：</label>
								<input type="text" maxlength="30" name="travelerSpell" class="traveler traveler2">
							</li>

							<li><label class="ydLable"><span class="xing">*</span>性别：</label>
								<select name="travelerSex" class="selSex">
									<option value="1" selected="selected">男</option>
									<option value="2">女</option>
								</select>
							</li>
							<li><label class="ydLable">出生日期：</label><input type="text"
								maxlength="" name="brithday" class="traveler traveler2 dateinput">
							</li>

							<li><label class="ydLable"><span class="xing"></span>联系电话：</label>
								<input type="text" name="telephone" class="traveler"
								maxlength="50"
								onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"
								onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')">
							</li>
							<li><label class="ydLable"><span class="xing">*</span>护照号：</label>
								<input type="text" name="passportCode" class="traveler"
								maxlength="50"
								onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"
								onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')">
							</li>
							<li><label class="ydLable"><span class="xing">*</span>护照有效期：</label><input
								type="text" maxlength="" name="passportValidity"
								class="traveler traveler2 dateinput"></li>
							<li><label class="ydLable"><span class="xing"></span>身份证号：</label>
								<input type="text" name="idCard" class="traveler"
								maxlength="50"
								onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"
								onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')">
							</li>
						</ul>
						<div class="ydbz_tit ydbz_tit_child">
							<em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>签证信息
						</div>
						<ul flag="messageDiv">
							
							<div class="ydbz_tit ydbz_tit_child">申请办签</div>
							<ul class="ydbz_2uploadfile">
								<li class="seach25 seach33"><p class="seachlongp">预计出团日期：</p>
									<input name="forecastStartOut" type="text" class="dateinput"/></li>
								<li class="seach25 seach33"><p class="seachlongp">签证类别：</p> ${visaType }</li>
								<p class="kong"></p>
							</ul>
						</ul>
						<div class="ydbz_tit ydbz_tit_child">
							<em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>上传资料
						</div>
						<ul flag="messageDiv" class="ydbz_2uploadfile">
							<li class="seach25 seach33"><p>护照首页：</p> 
								<input type="file" onchange="ydbz2interfile(this)" name="fileInfo" 
								class="ydbz_2filebtn">
								<input type="button" class="btn btn-primary" value="上传">
								<span class="fileLogo"></span>
								<input type="hidden" name="passportPhotoId" class="tabName"/></li>
							<li class="seach25 seach33"><p>身份证正面：</p> 
								<input type="file" onchange="ydbz2interfile(this)" name="fileInfo" 
								class="ydbz_2filebtn">
								<input type="button" class="btn btn-primary" value="上传">
								<span class="fileLogo"></span>
								<input type="hidden" name="identityFrontPhotoId" class="tabName"/></li>
							<li class="seach25 seach33"><p>报名表：</p> 
								<input type="file" onchange="ydbz2interfile(this)" name="fileInfo" 
								class="ydbz_2filebtn">
								<input type="button" class="btn btn-primary" value="上传">
								<span class="fileLogo"></span>
								<input type="hidden" name="tablePhotoId" class="tabName"/></li>
							<p class="kong"></p>
							<li class="seach25 seach33"><p>照片：</p> 
								<input type="file" onchange="ydbz2interfile(this)" name="fileInfo" 
								class="ydbz_2filebtn">
								<input type="button" class="btn btn-primary" value="上传">
								<span class="fileLogo"></span>
								<input type="hidden" name="personPhotoId" class="tabName"/></li>
							<li class="seach25 seach33"><p>身份证反面：</p> 
								<input type="file" onchange="ydbz2interfile(this)" name="fileInfo" 
								class="ydbz_2filebtn">
								<input type="button" class="btn btn-primary" value="上传">
								<span class="fileLogo"></span>
								<input type="hidden" name="identityBackPhotoId" class="tabName"/></li>
							<li class="seach25 seach33"><p>其它：</p> 
								<input type="file" onchange="ydbz2interfile(this)" name="fileInfo" 
								class="ydbz_2filebtn">
								<input type="button" class="btn btn-primary" value="上传">
								<span class="fileLogo"></span>
								<input type="hidden" name="otherPhotoId" class="tabName"/></li>
							<p class="kong"></p>
						</ul>
						<div class="ydbz_tit ydbz_tit_child">
							<em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>办证资料
						</div>
						<ul flag="messageDiv" class="seach25 seach100 ydbz_2uploadfile">
							<p>原件：</p>
							护照已领取、房产证未领取
							<br />
							<p>复印件：</p>
							户口本未领取、房产证未领取
						</ul>
						
						<div class="ydbz_tit ydbz_tit_child">
							<em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>办证资料
						</div>
						<ul flag="messageDiv" class="ydbz_2uploadfile">
							<li><input name="addcost" type="button" class="btn-addBlue"  value="增加其它费用"/></li>
						</ul>
						
						<div class="clearfix"></div>
						<div class="ydbz_tit ydbz_tit_child">
							备注：
							<textarea name="travelRemark" class="textarea_long"></textarea>
						</div>

					</div>
					<!--游客信息左侧结束-->

					<!--游客信息右侧开始-->
					<div class="tourist-right">
						<div class="clearfix">
							<ul class="tourist-info2">
								<li class="tourist-info2-first"><label
									class="ydLable2 ydColor1">应收价格：</label><span class="rightPay">${currency.currencyMark } ${visaProduct.visaPay } ${currency.currencyName }</span></li>
							</ul>
						</div>
						<div class="yd-line"></div>
						<div class="payfor-otherDiv">
								
						<div class="yd-line"></div>
						<div class="yd-total clearfix">
							<div class="fr">
								<label class="ydLable2">结算价：</label>${currency.currencyMark }<span class="ydFont2">${currency.currencyMark } ${visaProduct.visaPay } ${currency.currencyName }</span>
								<input type="hidden" name="payPrice" class="traveler"
									value="1600">
							</div>
						</div>
					</div>
					<!--游客信息右侧结束-->
				</div>
			</div>
			<input type="submit" value="提交"/>
		</form>
	</div>
	
	<!-- 其他费用模板 -->
	<div id="otherPayTemplate" style="display: none;">
		<li>
			<label class="ydLable">填写费用名称：</label><input type="text" name="otherPayName" value=""/>
			<select name="otherPayCurrency">
				<option value="1" selected="selected">人民币</option>
				<option value="2">美元</option>
				<option value="3">加元</option>
				<option value="4">欧元</option>
			</select><label class="ydLable">报价：</label><input type="text" name="otherPaynum" value=""/>
			<input name="delPay" type="button" class="btn-addBlue" value="删除此费用"/>
		</li>
	</div>
</body>
</html>
