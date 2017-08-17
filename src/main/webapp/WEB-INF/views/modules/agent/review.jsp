<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>渠道管理-查看</title>

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
<style>
	.seach_checkbox a:hover em{
		display:none;
	}
</style>
<script type="text/javascript">

var salerIdStr = '${agentinfo.agentSalerId }';
var agentSalersJsonArrayStr = '${agentSalersJsonArray}';
var agentSalersJsonArray = eval(agentSalersJsonArrayStr);  // 批发商销售列表

$(function(){
	inquiryCheckBOX();
	initSalers();
	
	account_tj();
	//上传动作
	btfile();
	//inpout 活的焦点
	inputTips();
	//渠道资质添加
	qdzz_add();
	//渠道结款方式
	agentpayfor();
	//日期，点击
	$( ".spinner" ).spinner({
		spin: function( event, ui ) {
			var month=ui.value;
			if ( ui.value > 31 ) {
				$( this ).spinner( "value", 1 );
				$(this).parents("p").find("i").text("重复：每月1日");
				return false;
			} else if ( ui.value < 1 ) {
				$( this ).spinner( "value", 31 );
				$(this).parents("p").find("i").text("重复：每月最后一天");
				return false;
			} else if ( ui.value==31){
				$(this).parents("p").find("i").text("重复：每月最后一天");
			}else{
				$(this).parents("p").find("i").text("重复：每月"+month+"日");
			}
		}
	});
	//默认缓存判断
    var spinnerval=$(".spinner").val();
	if(spinnerval==31){
		$(".spinner").parents("p").find("i").text("重复：每月最后一天");
	}else{
		$(".spinner").parents("p").find("i").text("重复：每月"+spinnerval+"日");
	}
	
	$(".weekSettlementReview").change(function(){
		var weekText = $(this).find("option:selected").text();
		$(this).parents("span").find("i").text("重复：每周"+weekText);
	}).change();
});	

/**
 * 初始化加载显示销售
 */
function initSalers() {
	var oldSalerArray = new Array();
	var allSalerArray = agentSalersJsonArray;
	if(!salerIdStr) {
		return null;	
	} else {
// 		if(salerIdStr.indexOf(",") == 0) {
// 			salerIdStr += ",";
// 		}
		oldSalerArray = salerIdStr.split(",");
	}
	// 添加显示
	if (oldSalerArray && oldSalerArray.length > 0) {
		for ( var i = 0; i < oldSalerArray.length; i++) {
			for (var j = 0; j < allSalerArray.length; j++) {			
				if (oldSalerArray[i] == allSalerArray[j].key) {					
					$("#salerShow").append('<a>{0}<input type="hidden" name="agentinfo.salerIdArray" value="{1}"/><input type="hidden" name="agentinfo.salerNameArray" value="{2}"/></a>'
				                    	.replace("{0}",allSalerArray[j].value).replace("{1}",allSalerArray[j].key).replace("{2}",allSalerArray[j].value));
					break;	
				}
			}
		}
	}
	
}

function account_tjk(){
		var i = 1;
		$('.account dt em').live('click',function(){$(this).parents('.account').remove();});
		$('.account_tj').click(function(){
			var ykhtml=$(this).parent().next('.account').html();
			ykhtml = ykhtml.replace(/name=\"/g,"name=\"banks["+i+"].");
			$(this).parent().before('<dl class="account">'+ykhtml+'</dl>');
			alert(ykhtml);
			i++;
		});
	}
//输入判断
function spinnerInput(obj){
	var ms = obj.value.replace(/\D/g,'');
	if(obj.value>=31){
		$(".spinner").parents("p").find("i").text("重复：每月最后一天");
		if(obj.value > 31){
			obj.value = 31;
		}
	}else{
		$(".spinner").parents("p").find("i").text("重复：每月"+obj.value+"日");
	}
}
//文件下载
	 function downloads(docid,activitySerNum,acitivityName,iszip){
	    	if(iszip){
	    		var zipname = activitySerNum;
	    		window.open("${ctx}/sys/docinfo/zipdownload/"+docid+"/"+zipname);
	    	}
	    		
	    	else
	    		window.open("${ctx}/sys/docinfo/download/"+docid);
	    }
	//删除现有的文件
	function deleteFiles(id) {
		top.$.jBox.confirm('确定要删除该文件？','系统提示',function(v){
			if(v=='ok'){
				$("span.seach_checkbox a").remove();
			}
			},{buttonsFocus:1});
	}
</script>
</head>
<body>
<style type="text/css">label{ cursor:inherit;}</style>
<page:applyDecorator name="agent_op_head" >

</page:applyDecorator>
         <div class="sysdiv sysdiv_coupon">
         	<select id="oldSalerIds" name="oldSalerIds" style="display: none;">
				<c:forEach items="${agentinfo.agentSalerId }" var="oldId">
               		<option value="${oldId}">${oldId}</option>
               	</c:forEach>
			</select>
            <p>
              <label><em class="xing">*</em>渠道品牌：</label>
              <span>
              <input name="agentinfo.agentBrand" readonly="readonly" type="text" maxlength="50" value="${agentinfo.agentBrand }">
              </span>
			</p>
            <p>
              <label><em class="xing">*</em>渠道公司名称：</label>
              <span>
              <input name="agentinfo.agentName" readonly="readonly" type="text" maxlength="50" value="${agentinfo.agentName}">
              </span>
			</p>
			<p>
				<label><em class="xing">*</em>渠道名称首字母：</label> 
				<span><input id="agentFirstLetter" readonly="readonly" name="agentinfo.agentFirstLetter" type="text" maxlength="1" value="${agentinfo.agentFirstLetter}"></span>
			</p>
            <p>
              <label>英文名称：</label>
              <span>
              <input name="agentinfo.agentNameEn" disabled="disabled" type="text" maxlength="50" value="${agentinfo.agentNameEn }">
              </span>
              <c:if test="${agentNameShortFlag == 1}">
	              <label><em class="xing">*</em>简称：</label>
	              <span>
	              <input name="agentinfo.agentNameShort" disabled="disabled" id="agentNameShort" value="${agentinfo.agentNameShort }" type="text" maxlength="50">
	              </span>
              </c:if>
			</p>
            <p>
              <label>所属地区：</label>
              <span class="sysselect_s countryCityArea">
                <select class="country" disabled="disabled"  name="agentinfo.belongsArea">
                	<option value="">国家</option>
                	<c:forEach  items="${areaMap}" var="aa">
                		<option value="${aa.key}" <c:if test="${aa.key == agentinfo.belongsArea}">selected="selected"</c:if> >${aa.value}</option>
                	</c:forEach>
                </select>
                <select class="province" disabled="disabled" name="agentinfo.belongsAreaProvince">
                	<option>省(直辖市)</option>
                	<c:forEach  items="${belongsProvinceMap}" var="aa">
                		<option value="${aa.key}" <c:if test="${aa.key== agentinfo.belongsAreaProvince}">selected="selected"</c:if> >${aa.value}</option>
                	</c:forEach>
                </select>
                <select class="city" disabled="disabled" name="agentinfo.belongsAreaCity">
                 	<option>市(区)</option>
                 	<c:forEach  items="${belongsCityMap}" var="aa">
                		<option value="${aa.key}" <c:if test="${aa.key== agentinfo.belongsAreaCity}">selected="selected"</c:if> >${aa.value}</option>
                	</c:forEach>
                </select>
              </span>
			</p>
			<p>
				<label><em class="xing">*</em>跟进销售人员：</label>
				<span class="sysselect_s">
					<select id="agentSalerUser" name="agentinfo.agentSalerUser.id" style="display: none;">
	                	<option value="">请选择</option>
	                	<c:forEach items="${agentSalers}" var="as">
	                		<option value="${as.key}">${as.value}</option>
	                	</c:forEach>
	                </select>
					<a class="ydbz_x" href="javascript:void(0);" onclick="javascript:window.location.href='${ctx}/sys/user/form'">+新增销售员</a>
				</span>
				<div id="salerShow" class="seach_checkbox"></div>
			</p>
             <p>
              <label><em class="xing">*</em>结款方式：</label>
              <span class="sysselect_s agentpayfor">
                <select onchange="agentpayfor(this)" name="agentinfo.paymentType" disabled="disabled">
                	<c:forEach items="${paymentMap}" var="pm">
                		<option value="${pm[0]}" <c:if test="${pm[0]==agentinfo.paymentType}">selected="selected"</c:if>  >${pm[1]}</option>
                	</c:forEach>
                </select>
              </span>
              <span class="agentdatetips"  <c:if test="${agentinfo.paymentDay!=null}">style="display: inline;"</c:if> ><label>结款日期：</label><input readonly="readonly"  name="agentinfo.paymentDay" value="${agentinfo.paymentDay}" class="spinner" maxlength="2" onafterpaste="spinnerInput(this)" onkeyup="spinnerInput(this)" onblur="spinnerInput(this)">日<i>重复：每月1日</i></span>
              <!-- 周结算控制 -->
              <span class="agentdatetips"><label>结款日期：</label>
              		<select name="agentinfo.paymentDay" class="weekSettlementReview" style="width:100px;" disabled="disabled">
              			<option value="1" <c:if test="${agentinfo.paymentDay == 1}">selected</c:if>>周一</option>
              			<option value="2" <c:if test="${agentinfo.paymentDay == 2}">selected</c:if>>周二</option>
              			<option value="3" <c:if test="${agentinfo.paymentDay == 3}">selected</c:if>>周三</option>
              			<option value="4" <c:if test="${agentinfo.paymentDay == 4}">selected</c:if>>周四</option>
              			<option value="5" <c:if test="${agentinfo.paymentDay == 5}">selected</c:if>>周五</option>
              			<option value="6" <c:if test="${agentinfo.paymentDay == 6}">selected</c:if>>周六</option>
              			<option value="7" <c:if test="${agentinfo.paymentDay == 7}">selected</c:if>>周日</option>
              		</select>
              		<i>重复：每周周一</i>
              </span>
              <span> <label>门市名称：：</label>
	              <input name="agentinfo.salesRoom" disabled="disabled" id="salesRoom" value="${agentinfo.salesRoom }" type="text" maxlength="50">
	          </span>
			</p>
            <p>
              <label>公司地址：</label>
              <span class="sysselect_s countryCityAddress">
                 <select class="country" name="agentinfo.agentAddress" disabled="disabled">
                	<option value="">国家</option>
                	<c:forEach  items="${areaMap}" var="aa">
                		<option value="${aa.key}" <c:if test="${aa.key == agentinfo.agentAddress}">selected="selected"</c:if> >${aa.value}</option>
                	</c:forEach>
                </select>
                <select class="province" name="agentinfo.agentAddressProvince" disabled="disabled">
                	<option>省(直辖市)</option>
                	<c:forEach  items="${addressProvinceMap}" var="aa">
                		<option value="${aa.key}" <c:if test="${aa.key == agentinfo.agentAddressProvince}">selected="selected"</c:if> >${aa.value}</option>
                	</c:forEach>
                </select>
                <select class="city" name="agentinfo.agentAddressCity" disabled="disabled">
                 	<option>市(区)</option>
                 	<c:forEach  items="${addressCityMap}" var="aa">
                		<option value="${aa.key}" <c:if test="${aa.key == agentinfo.agentAddressCity}">selected="selected"</c:if> >${aa.value}</option>
                	</c:forEach>
                </select>
                <input name="agentinfo.agentAddressStreet" type="text" value="${agentinfo.agentAddressStreet }" class="" readonly="readonly">
              </span>
			</p>
			<p>
              <label>渠道邮编：</label>
             <span><input name="agentinfo.agentPostcode" readonly="readonly" type="text" value="${agentinfo.agentPostcode}" maxlength="20"></span>
			</p>
            <p>
              <label>电话：</label>
              <span><input name="agentinfo.agentTelAreaCode" readonly="readonly" type="text"  value="${agentinfo.agentTelAreaCode}" class="sysinput_s"><span class="sysinput_span">-</span><input name="agentinfo.agentTel" readonly="readonly" type="text" value="${ agentinfo.agentTel}" class="inputTxt">例如：010-87475943</span>
			</p>
            <p>
              <label>传真：</label>
             <span><input name="agentinfo.agentFaxAreaCode" readonly="readonly" type="text" value="${ agentinfo.agentFaxAreaCode}" class="sysinput_s"><span class="sysinput_span">-</span><input name="agentinfo.agentFax" readonly="readonly" type="text" value="${ agentinfo.agentFax}" class="inputTxt">例如：010-87475943</span>
			</p>
            <div>
             <p class="shopPeopleP"><label><a style="color:#f00;">*</a>&nbsp;联系人<em>1</em>：</label>
               <span><input name="agentinfo.agentContact" type="text" value="${agentinfo.agentContact }" readonly="readonly"></span>
               <label><a style="color:#f00;">*</a>&nbsp;手机：</label>
               <span><input name="agentinfo.agentContactMobile" type="text" value="${agentinfo.agentContactMobile }" readonly="readonly"></span>
               <span class="kongr20"></span>
               <label>固定电话：</label>
               <span><input name="agentinfo.agentContactTel" type="text" value="${agentinfo.agentContactTel }" readonly="readonly"></span>
               <label>传真：</label>
               <span><input name="agentinfo.agentContactFax" type="text" value="${ agentinfo.agentContactFax}" readonly="readonly"></span>
               <span class="kongr20"></span>
               <label>Email：</label>
               <span><input name="agentinfo.agentContactEmail" type="text" value="${agentinfo.agentContactEmail }" readonly="readonly"></span>
               <label>QQ：</label>
               <span><input name="agentinfo.agentContactQQ" type="text" value="${agentinfo.agentContactQQ }" readonly="readonly"></span>
             </p>
              
           	 <c:forEach items="${supplyContactsList }" var="contacts" varStatus="status">
	              <p <c:choose><c:when test="${status.index==0 }"> class="shopPeopleP tt"</c:when><c:otherwise>class="shopPeopleP"</c:otherwise></c:choose> >
	              	<label>联系人<em>${status.index +2}</em>：</label>    	
	               <input type="hidden" name="contacts.supplyContactses[${status.index}].id" value="${contacts.id}" />
	               <span><input name="contacts.supplyContactses[${status.index}].contactName" type="text" value="${contacts.contactName }" maxlength="9" readonly="readonly"></span>
	               <label>手机：</label>
	               <span>
	               		<input name="contacts.supplyContactses[${status.index}].contactMobile" type="text" value="${contacts.contactMobile }" readonly="readonly" maxlength="11">
	               </span>
	               <span class="kongr20"></span>
	               <label>固定电话：</label>
	               <span><input name="contacts.supplyContactses[${status.index}].contactPhone" type="text" value="${contacts.contactPhone }" readonly="readonly" maxlength="12"></span>
	               <label>传真：</label>
	               <span><input name="contacts.supplyContactses[${status.index}].contactFax" type="text" value="${contacts.contactFax }" readonly="readonly"></span>
	               <span class="kongr20"></span>
	               <label>Email：</label>
	               <span><input name="contacts.supplyContactses[${status.index}].contactEmail" type="text"  value="${contacts.contactEmail }" readonly="readonly"></span>
	               <label>QQ：</label>
	               <span><input name="contacts.supplyContactses[${status.index}].contactQQ"  type="text" value="${contacts.contactQQ }" readonly="readonly"></span>
	             </p>
             </c:forEach>
            </div>
			<p>
              <label>描述：</label>
              <span>
              <textarea name="agentinfo.remarks" rows="3" class="input-xlarge" maxlength="200" readonly="readonly">${agentinfo.remarks }</textarea>
              </span>
			</p>
			<p>
			  <label>公司LOGO：</label>
			  <span>	<!-- 
			  		   <input type="button" name="passport"  id="uploadMoreFile" class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','passportfile',this,1);"/>
			  		   -->
           			   <span id="upfileShow" class="seach_checkbox">
           			  		<a class="docname">
           			  			<b>${docInfo.docName}</b>
								<input type="hidden" name="agentinfo.logo" value="${docInfo.id}">
								<input type="hidden" name="fileName" value="crebas.sql">
								<input type="hidden" name="filePath" value="upload\2014\12\30\6b479d04-298b-4a4d-9ce3-2dd6e038bd61.sql">
							</a>
							<c:if test="${!empty docInfo.id }">
								<a class="download" href="javascript:void(0);" id="${docInfo.id }" onclick="downloads('${docInfo.id}','${docInfo.docName}',1,true);">下载</a>
							</c:if>
							<!-- 
							<a class="delete" href="javascript:void(0);" id="${docInfo.id }" onclick="deleteFiles('${docInfo.id}');">删除</a> -->
           			   </span>
           			   <span class="fileLogo"></span>
			  
			  </span><!-- 请上传公司LOGO文件 -->
			</p>
        </div>
        
<!-- second -->
		<!--右侧内容部分开始-->
<!-- 		<div class="supplierLine"> -->
			<!--<a href="javascript:void(0)">基本信息填写</a>-->
<!-- 			<a href="javascript:void(0)"  class="select">银行账户</a> -->
			<!--<a href="javascript:void(0)">资质上传</a>-->
<!-- 		</div> -->
		<div class="qdgl-cen">
				  <c:choose>
				  	<c:when test="${fn:length(banks)>0}">
				  		<c:forEach items="${banks }" var="bank" varStatus="status">
						<dl class="account"> 
							<dt><span><input name="banks[${status.index}].defaultFlag" type="radio" disabled="disabled" altattr="radio"  <c:if test="${bank.defaultFlag == 0 }">checked="checked"</c:if>  value="${bank.defaultFlag}" /></span><b>设为默认账户</b></dt>
							<dd><span>账户名：</span><input name="banks[${status.index}].accountName" type="text" value="${bank.accountName}" readonly="readonly"/></dd>
							<dd><span>开户行名称：</span><input name="banks[${status.index}].bankName" type="text" value="${bank.bankName}" readonly="readonly"/></dd>
							<dd><span>开户行地址：</span><input name="banks[${status.index}].bankAddr" type="text" value="${bank.bankAddr}" readonly="readonly"/></dd>
							<dd><span>账户号码：</span><input name="banks[${status.index}].bankAccountCode" type="text" value="${bank.bankAccountCode}" readonly="readonly"/></dd>
							<dd><span>备注：</span><textarea name="banks[${status.index}].remarks" cols="" rows="" readonly="readonly">${bank.remarks}</textarea></dd>
							<input type="hidden" name="banks[${status.index}].id" value="${bank.id}"/>
						</dl>
						</c:forEach>
				  	</c:when>
				  	<c:otherwise>
				  		<dl class="account"> 
							<dt><span><input name="banks[${status.index}].defaultFlag" type="radio" disabled="disabled" altattr="radio"  <c:if test="${bank.defaultFlag == 0 }">checked="checked"</c:if>  value="${bank.defaultFlag}" /></span><b>设为默认账户</b></dt>
							<dd><span>账户名：</span><input name="banks[${status.index}].accountName" type="text" value="${bank.accountName}" readonly="readonly"/></dd>
							<dd><span>开户行名称：</span><input name="banks[${status.index}].bankName" type="text" value="${bank.bankName}" readonly="readonly"/></dd>
							<dd><span>开户行地址：</span><input name="banks[${status.index}].bankAddr" type="text" value="${bank.bankAddr}" readonly="readonly"/></dd>
							<dd><span>账户号码：</span><input name="banks[${status.index}].bankAccountCode" type="text" value="${bank.bankAccountCode}" readonly="readonly"/></dd>
							<dd><span>备注：</span><textarea name="banks[${status.index}].remarks" cols="" rows="" readonly="readonly">${bank.remarks}</textarea></dd>
							<input type="hidden" name="banks[${status.index}].id" value="${bank.id}"/>
						</dl>
				  	</c:otherwise>
				  </c:choose>

<%-- 					<c:forEach items="${banks }" var="bank" varStatus="status"> --%>
<!-- 						<dl class="account"> -->
<!-- 							<dt> -->
<%-- 								<span><input name="banks[${status.index}].defaultFlag" --%>
<!-- 									type="radio" disabled="disabled" altattr="radio" -->
<%-- 									<c:if test="${bank.defaultFlag == 0 }">checked="checked"</c:if> --%>
<%-- 									value="${bank.defaultFlag}" /></span><b>设为默认账户</b> --%>
<!-- 							</dt> -->
<!-- 							<dd> -->
<!-- 								<span><i>*</i>账户名：</span><input -->
<%-- 									name="banks[${status.index}].accountName" type="text" --%>
<%-- 									value="${bank.accountName}" readonly="readonly" /> --%>
<!-- 							</dd> -->
<!-- 							<dd> -->
<!-- 								<span><i>*</i>开户行名称：</span><input -->
<%-- 									name="banks[${status.index}].bankName" type="text" --%>
<%-- 									value="${bank.bankName}" readonly="readonly" /> --%>
<!-- 							</dd> -->
<!-- 							<dd> -->
<!-- 								<span><i>*</i>开户行地址：</span><input -->
<%-- 									name="banks[${status.index}].bankAddr" type="text" --%>
<%-- 									value="${bank.bankAddr}" readonly="readonly" /> --%>
<!-- 							</dd> -->
<!-- 							<dd> -->
<!-- 								<span><i>*</i>账户号码：</span><input -->
<%-- 									name="banks[${status.index}].bankAccountCode" type="text" --%>
<%-- 									value="${bank.bankAccountCode}" readonly="readonly" /> --%>
<!-- 							</dd> -->
<!-- 							<dd> -->
<!-- 								<span>备注：</span> -->
<%-- 								<textarea name="banks[${status.index}].remarks" cols="" rows="" --%>
<%-- 									readonly="readonly">${bank.remarks}</textarea> --%>
<!-- 							</dd> -->
<%-- 							<input type="hidden" name="banks[${status.index}].id" --%>
<%-- 								value="${bank.id}" /> --%>
<!-- 						</dl> -->
<%-- 					</c:forEach> --%>

		<!-- 
					<div class="yh-account">
						<div class="ydbz_s account_tj">继续添加账户+</div>
					</div> 
					<dl class="account" style="display:none;">
						<dt><span><input name="defaultFlag" type="radio"   value="1"/></span><b>设为默认账户</b><em>X 删除</em></dt>
						<dd><span><i>*</i>账户名：</span><input name="accountName" type="text" value="" /></dd>
						<dd><span><i>*</i>开户行名称：</span><input name="bankName" type="text" value="" /></dd>
						<dd><span><i>*</i>开户行地址：</span><input name="bankAddr" type="text" value="" /></dd>
						<dd><span><i>*</i>账户号码：</span><input name="bankAccountCode" type="text" value="" /></dd>
						<dd><span>备注：</span><textarea name="remarks" cols="" rows=""></textarea></dd>
						<input type="hidden" name="id" />
					</dl>-->
		</div>
	<!--	<div class="dbaniu " style=" margin-left:100px;"><a class="ydbz_s gray">返回</a><a class="ydbz_s">上一步</a><a class="ydbz_s">下一步</a><a class="ydbz_s">提交</a></div>
				
 second end -->        
<!-- Third -->
		<!--右侧内容部分开始-->
<!-- 		<div class="supplierLine"> -->
			<!--<a href="javascript:void(0)">基本信息填写</a>
			<a href="javascript:void(0)" class="select">银行账户</a>-->
<!-- 			<a href="javascript:void(0)" class="select">资质上传</a> -->
<!-- 		</div> -->
		<div class="qdgl-cen">
					<dl class="wbyu-bot wbyu-bot2">
						<dt>
							<label>营业执照：</label>
							<!-- 
							<input type="button" name="agentinfo.businessLicense2" id="businessLicense"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','agentinfo.businessLicense',this,1);"/>
							 -->
							<c:if test="${!empty license }">
							<span class="seach_checkbox_2">
								<b>${ license.docName}</b>
								<input type="hidden" name="agentinfo.businessLicense2" value="${ license.id}">
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ license.id}','${ license.docName}',1,true)">下载</a> 
								<!--  
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${ license.id}',this)">删除</a>-->
							</span>
							</c:if>
						</dt>
						<dt>
							<label>经营许可证：</label>
								<!--  
							<input type="button" name="agentinfo.license2" id="license"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','agentinfo.license',this,1);"/>
           			   			 -->
           			   		<c:if test="${!empty business }">
           			   		<span class="seach_checkbox_2">
								<b>${ business.docName}</b>
								<input type="hidden" name="agentinfo.license2" value="${ business.id}">
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ business.id}','${ business.docName}',1,true)">下载</a> 
								<!-- <a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${ business.id}',this)">删除</a>-->
							</span>
           			   		</c:if>
						</dt>
						<dt>
							<label>税务登记证：</label>
							<!--  
							<input type="button" name="agentinfo.taxCertificate2" id="taxCertificate"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','agentinfo.license',this,1);"/>
							-->
							<c:if test="${!empty taxCertificate }">
							<span class="seach_checkbox_2">
								<b>${ taxCertificate.docName}</b>
								<input type="hidden" name="agentinfo.taxCertificate2" value="${ taxCertificate.id}">
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ taxCertificate.id}','${ taxCertificate.docName}',1,true)">下载</a> 
								<!-- <a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${ taxCertificate.id}',this)">删除</a>-->
							</span>
							</c:if>
						</dt>
						<dt>
							<label>组织机构代码证：</label>
							<!--
							 <input type="button" name="agentinfo.organizeCertificate2" id="organizeCertificate"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','agentinfo.license',this,1);"/>
							-->
							<c:if test="${!empty organizeCertificate}">
							<span class="seach_checkbox_2">
								<b>${ organizeCertificate.docName}</b>
								<input type="hidden" name="agentinfo.organizeCertificate2" value="${ organizeCertificate.id}">
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ organizeCertificate.id}','${ organizeCertificate.docName}',1,true)">下载</a> 
								<!-- <a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${ organizeCertificate.id}',this)">删除</a>-->
							</span>
							</c:if>
						</dt>
						<dt>
							<label>公司法人身份证（正反面在一起）：</label>
							<!-- 
							<input type="button" name="agentinfo.idCard2" id="idCard"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','agentinfo.license',this,1);"/>
							 -->
							 <c:if test="${!empty idCard }">
							<span class="seach_checkbox_2">
								<b>${ idCard.docName}</b>
								<input type="hidden" name="agentinfo.idCard2" value="${ idCard.id}">
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ idCard.id}','${ idCard.docName}',1,true)">下载</a> 
								<!-- <a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${ idCard.id}',this)">删除</a>-->
							</span>
							</c:if>
						</dt>
                        <dt>
							<label>公司银行开户许可证：</label>
							<!-- 
							<input type="button" name="agentinfo.bankOpenLicense2" id="bankOpenLicense"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','agentinfo.license',this,1);"/>
							 -->
							<c:if test="${!empty bankOpenLicense }">
							<span class="seach_checkbox_2">
								<b>${ bankOpenLicense.docName}</b>
								<input type="hidden" name="agentinfo.bankOpenLicense2" value="${ bankOpenLicense.id}">
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ bankOpenLicense.id}','${ bankOpenLicense.docName}',1,true)">下载</a> 
								<!-- <a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${ bankOpenLicense.id}',this)">删除</a> -->
							</span>
							</c:if>
						</dt>
						<dt>
							<label>旅游业资质：</label>
							<!-- 
							<input type="button" name="agentinfo.travelAptitudes2" id="travelAptitudes"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','agentinfo.license',this,1);"/>
							 -->
							 <c:if test="${!empty travelAptitudes }">
							<span class="seach_checkbox_2">
								<b>${ travelAptitudes.docName}</b>
								<input type="hidden" name="agentinfo.travelAptitudes2" value="${ travelAptitudes.id}">
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ travelAptitudes.id}','${ travelAptitudes.docName}',1,true)">下载</a> 
								<!-- <a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${ travelAptitudes.id}',this)">删除</a> -->
							</span>
							</c:if>
						</dt>
						<c:forEach varStatus="status" items="${elseFileList }" var="docinfo">	
							<dt class="elseFileAdd">
								<label>其他文件：</label>
								<div class="pr fl">
								<input value="${docinfo.elseFileName }" class="inputTxt inputTxtlong" name="elseFileName[${status.index }]"  flag="istips" readonly="readonly"> 
	                			<span class="ipt-tips">文件名称</span>
								</div>
								<p class="fl">
									<!-- 
									<input type="button" name="elseFileId[${status.index }]"   class="btn btn-primary fileUploadButton" value="上传"  onclick="uploadFiles('${ctx}','${docinfo.docName }',this,1);"/>
	           			   			 -->
	           			   			<span class="seach_checkbox_2">
	           			   				<b>${ docinfo.docName}</b>
	           			   				<input type="hidden" name="elseFileId[${status.index }]" value="${ docinfo.id}">
										<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ docinfo.id}','${ docinfo.docName}',1,true)">下载</a> 
										<!-- <a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${ docinfo.id}',this)">删除</a> -->
	           			   			</span>
								</p>
							</dt>
						</c:forEach>
					</dl>
				</div>
				<div class="dbaniu " style=" margin-left:100px;"><a class="ydbz_s" href="javascript:void(0)" onclick="window.opener=null;window.close();">关闭</a></div>
<!-- Third end -->        
        <!--右侧内容部分结束-->
<!--       </div> -->
<!--     </div> -->
<!--   </div> -->
  <!--footer
  <div class="bs-footer">
    <p>公司电话：010-85711691 | 技术支持电话：010-85711691-8006 | 客服电话：400-018-5090  | 传真：010-85711891<br/>
      Copyright &copy; 2012-2014 接待社交易管理后台</p>
    <div class="footer-by">Powered By Trekiz Technology</div>
  </div>-->
  <!--footer***end--> 
<!-- </div> -->
</body>

</html>
