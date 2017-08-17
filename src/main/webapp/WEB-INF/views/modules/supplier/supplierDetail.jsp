<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>地接社管理-地接社详情页</title>
<meta name="decorator" content="wholesaler"/>

<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
$(function(){
	// 	上传动作
	btfile();
	// 	切换卡
	switchingCard();
});
function switchingCard(){
	$(".supplierLine > a").hover(function() {
		var iIndex = $(this).index();
		$(this).addClass("select").siblings().removeClass("select");
		$(this).parent().siblings(".switch-cen").addClass("no").eq(iIndex).removeClass("no");
	});
}
//文件下载
function downloads(docid,activitySerNum,acitivityName,iszip){
	window.open("${ctx}/sys/docinfo/download/"+docid);
}
</script>
</head>
<body>
<!--右侧内容部分开始-->
		<form method="post" action="${ctx}/supplier/supplierDetail?supplierId=" class="form-horizontal" id="inputForm">
		<div class="supplierLine">
			<a href="javascript:void(0)" class="select">基本信息填写</a>
			<a href="javascript:void(0)">网站信息</a>
			<a href="javascript:void(0)">银行账户</a>
			<a href="javascript:void(0)">资质上传</a>
		</div>
		<div class="switch-cen ">
            <div class="sysdiv sysdiv_coupon">
                <p>
                  <label>地接社类型：</label>
                  <span class="checkboxdiv">${supplierType}
                  </span>
                </p>                
                <p>
                  <label>地接社等级：</label>
                  <span class="checkboxdiv">${supplierLevel}
                  </span>
                </p>                
                <p>
                  <label>地接社品牌：</label>
                  <span>${supplierInfo.supplierBrand}</span>
                </p>
                <p>
                  <label>公司名称：</label>
                  <span>${supplierInfo.supplierName}</span>
                </p>
                <p>
                  <label>英文名称：</label>
                  <span>${supplierInfo.companyEnName}</span>
                </p>
                <c:choose>
				<c:when test="${belongAreaList != null}">
				<c:forEach items="${belongAreaList}" var="aa" varStatus="status">
                <p>
                  	<c:choose>
	               	<c:when test="${status.index==0 }"><label>所属地区：</label></c:when>
	               	<c:otherwise><label>&nbsp;</label></c:otherwise>
	          		</c:choose>
                  <span>${belongAreaList[status.index]}</span>
                </p>
                </c:forEach>
                </c:when>
                <c:otherwise>
                <p>
                <label>所属地区：</label>
                <span></span>
                </p>
                </c:otherwise>
                </c:choose>
                <c:choose>
				<c:when test="${companyAddr != null}">
                <p>
                  <label>公司地址：</label>
                  <span>${companyAddr }</span>
                </p>
                </c:when>
                <c:otherwise>
                <p>
                  <label>公司地址：</label>
                  <span></span>
                </p>
                </c:otherwise>
                </c:choose>
                <p>
                  <label>电话：</label>
                  <span>${phone }</span>
                </p>
                <p>
                  <label>传真：</label>
                 <span>${fax }</span>
                </p>
                <div>
                <c:choose>
				<c:when test="${supplierContactsList != null}">
                <c:forEach items="${supplierContactsList }" var="contacts" varStatus="status">
                 <p class="shopPeopleP"><label>联系人<em>${status.index +1}</em>：</label>
                   <span>${contacts.contactName }</span>
                   <label>手机：</label>
                   <span>${contacts.contactMobile }</span>
                   <span class="kongr20"></span>
                   <label>固定电话：</label>
                   <span>${contacts.contactPhone }</span>
                   <label>传真：</label>
                   <span>${contacts.contactPhone }</span>
                   <span class="kongr20"></span>
                   <label>Email：</label>
                   <span>${contacts.contactEmail }</span>
                   <label>QQ：</label>
                   <span>${contacts.contactQQ }</span>
                 </p>
                 </c:forEach>
                 </c:when>
                 <c:otherwise>
                 <p class="shopPeopleP"><label>联系人<em>1</em>：</label>
                   <span></span>
                   <label>手机：</label>
                   <span></span>
                   <span class="kongr20"></span>
                   <label>固定电话：</label>
                   <span></span>
                   <label>传真：</label>
                   <span></span>
                   <span class="kongr20"></span>
                   <label>Email：</label>
                   <span></span>
                   <label>QQ：</label>
                   <span></span>
                 </p>
             	</c:otherwise>
             </c:choose>
                </div>
                <p>
                  <label>描述：</label>
                  <span>${supplierInfo.description}
                  </span>
                </p>
                <p>
                  <label>公司LOGO：</label>
                <c:choose>
				<c:when test="${docInfo != null}">
                  <span>
                  <b>${docInfo.docName}</b>
				<input type="hidden" name="docInfo" value="${docInfo.id}">
				<input type="hidden" name="fileName" value="crebas.sql">
				<input type="hidden" name="filePath" value="upload\2014\12\30\6b479d04-298b-4a4d-9ce3-2dd6e038bd61.sql">
				<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${docInfo.id}','${docInfo.docName}',1,true);">下载</a>
				</span>
				</c:when>
                </c:choose>
                </p>
            </div>
        </div>
		<!--第二步-->
		<div class="switch-cen no">
            <div class="sysdiv sysdiv_coupon">
                <p>
                  <label>网站状态：</label>
                  <c:choose>
                  <c:when test="${supplierWebsiteInfo.siteStatus == '1' }">
                      <span class="checkboxdiv">启用</span>
                  </c:when>
                  <c:otherwise>
                      <span class="checkboxdiv">未启用</span>
                  </c:otherwise>
                  </c:choose>
                </p>
                <p>
                  <label>浏览权限：</label>
                  <c:choose>
                  <c:when test="${supplierWebsiteInfo.scanAuthority == '1' }">
                      <span>开放浏览</span>
                  </c:when>
                  <c:otherwise>
                      <span>登陆后浏览</span>
                  </c:otherwise>
                  </c:choose>
                </p>
                <p>
                  <label>登陆账号：</label>
                  <span>${supplierWebsiteInfo.loginAccount }</span>
                </p>
                <p>
                  <label>登录密码：</label>
                  <span>${supplierWebsiteInfo.loginPwd }</span>
                </p>
                <p>
                  <label>网站标题：</label>
                  <span>${supplierWebsiteInfo.siteTitle }</span>
                </p>
                <p>
                  <label>网站关键字：</label>
                  <span>${supplierWebsiteInfo.siteKeyWord }</span>
                </p>
                <p>
                  <label>网站描述：</label>
                  <span>${supplierWebsiteInfo.siteDescription }</span>
                </p>
                <p>
                  <label>网站地址：</label>
                  <span>${supplierWebsiteInfo.siteUrl }</span>
                </p>
                <p>
                  <label>网站负责人：</label>
                 <span>${supplierWebsiteInfo.siteManager }</span>
                </p>
                <p>
                  <label>网站负责人电话：</label>
                 <span>${supplierWebsiteInfo.siteManagerPhone }</span>
                </p>
                <p>
                  <label>网站客服电话：</label>
                 <span>${supplierWebsiteInfo.siteCustomerServicePhone }</span>
                </p>
                <p>
                  <label>网站管理员邮箱：</label>
                 <span>${supplierWebsiteInfo.siteAdminEmail }</span>
                </p>
                <p>
                  <label>QQ在线客服号码：</label>
                 <span>${supplierWebsiteInfo.qqCustomerService }</span>
                </p>
            </div>
        </div>
        <!--第三步-->
        <div class="switch-cen no">
        	<div class="qdgl-cen">
            	<!--第一个dl的类名加acount_nbor,剩余的不加此类名-->
            	<c:choose>
            	<c:when test="${banks != null }">
            	<c:forEach items="${banks }" var="bank" varStatus="status">
            	<input type="hidden" value="${bank[0]}"/>
            	<c:choose>
            		<c:when test="${bank[1] == '0' }">
            			<dl class="account account-text acount_nbor">
            			<dt><b>默认账户</b></dt>
            			<dd><span>账户名：</span><p>${bank[2] }</p></dd>
                    	<dd><span>开户行名称：</span><p>${bank[3] }</p></dd>
                    	<dd><span>开户行地址：</span><p>${bank[4] }</p></dd>
                    	<dd><span>账户号码：</span><p>${bank[5] }</p></dd>
                    	<dd><span>备注：</span><p>${bank[6] }</p></dd>
                    	</dl>
            		</c:when>
            		<c:otherwise>
            			<dl class="account account-text">
            			<dd><span>账户名：</span><p>${bank[2] }</p></dd>
                    	<dd><span>开户行名称：</span><p>${bank[3] }</p></dd>
                    	<dd><span>开户行地址：</span><p>${bank[4] }</p></dd>
                    	<dd><span>账户号码：</span><p>${bank[5] }</p></dd>
                    	<dd><span>备注：</span><p>${bank[6] }</p></dd>
                    	</dl>
            		</c:otherwise>
            	</c:choose>
            	</c:forEach>
            	</c:when>
            	<c:otherwise>
            	        <dl class="account account-text acount_nbor">
            			<dt><b>默认账户</b></dt>
            			<dd><span>账户名：</span><p></p></dd>
                    	<dd><span>开户行名称：</span><p></p></dd>
                    	<dd><span>开户行地址：</span><p></p></dd>
                    	<dd><span>账户号码：</span><p></p></dd>
                    	<dd><span>备注：</span><p></p></dd>
                    	</dl>
            	</c:otherwise>
            	</c:choose>
            </div>
        </div>
        <!--第四步-->
        <div class="switch-cen no">
            <div class="qdgl-cen">
                <dl class="wbyu-bot wbyu-bot2">
                    <dt>
                        <label>营业执照：</label>
                        <c:choose>
						<c:when test="${businessLicense != null}">
                        <b>${businessLicense.docName }</b>
						<input type="hidden" name="supplierInfo.businessLicense" value="${businessLicense.id}">
						<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${businessLicense.id}','${businessLicense.docName}',1,true)">下载</a>
						</c:when>
						</c:choose>
                    </dt>
                    <dt>
                        <label>经营许可证：</label>
                        <c:choose>
						<c:when test="${businessCertificate != null}">
						<b>${businessCertificate.docName }</b>
						<input type="hidden" name="supplierInfo.businessCertificate" value="${businessCertificate.id}">
						<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${businessCertificate.id}','${businessCertificate.docName}',1,true)">下载</a>
						</c:when>
						</c:choose>
                    </dt>
                    <dt>
                        <label>税务登记证：</label>
                        <c:choose>
						<c:when test="${taxCertificate != null}">
						<b>${taxCertificate.docName }</b>
						<input type="hidden" name="supplierInfo.taxCertificate" value="${taxCertificate.id}">
						<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${taxCertificate.id}','${taxCertificate.docName}',1,true)">下载</a>
						</c:when>
						</c:choose>
                    </dt>
                    <dt>
                        <label>组织机构代码证：</label>
                        <c:choose>
						<c:when test="${organizeCertificate != null}">
						<b>${organizeCertificate.docName }</b>
						<input type="hidden" name="supplierInfo.organizeCertificate" value="${organizeCertificate.id}">
						<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${organizeCertificate.id}','${organizeCertificate.docName}',1,true)">下载</a>
						</c:when>
						</c:choose>
                    </dt>
                    <dt>
                        <label>公司法人身份证（正反面在一起）：</label>
                        <c:choose>
						<c:when test="${idCard != null}">
						<b>${idCard.docName }</b>
						<input type="hidden" name="supplierInfo.idCard" value="${idCard.id}">
						<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${idCard.id}','${idCard.docName}',1,true)">下载</a>
						</c:when>
						</c:choose>
                    </dt>
                    <dt>
                        <label>公司银行开户许可证：</label>
                        <c:choose>
						<c:when test="${bankOpenLicense != null}">
						<b>${bankOpenLicense.docName }</b>
						<input type="hidden" name="supplierInfo.bankOpenLicense" value="${bankOpenLicense.id}">
						<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${bankOpenLicense.id}','${bankOpenLicense.docName}',1,true)">下载</a>
						</c:when>
						</c:choose>
                    </dt>
                    <dt>
                        <label>旅游业资质：</label>
                        <c:choose>
						<c:when test="${travelAptitudes != null}">
						<b>${travelAptitudes.docName }</b>
						<input type="hidden" name="supplierInfo.travelAptitudes" value="${travelAptitudes.id}">
						<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${travelAptitudes.id}','${travelAptitudes.docName}',1,true)">下载</a>
						</c:when>
						</c:choose>
                    </dt>
                    <c:choose>
					<c:when test="${elseFileList != null}">
                    <c:forEach varStatus="status" items="${elseFileList }" var="docinfo">
                    <dt>
	               	<label>其他文件：</label>
	               	<div class="pr fl">
						<input value="${docinfo.elseFileName }" class="inputTxt inputTxtlong" name="elseFileName[${status.index }]"  flag="istips" readonly="readonly"> 
<!-- 	                	<span class="ipt-tips">文件名称</span> -->
					</div>
                        <p class="fl">
          			   		<span class="seach_checkbox_2">
          			   		<b>${ docinfo.docName}</b>
          			   		<input type="hidden" name="elseFileId[${status.index }]" value="${ docinfo.id}">
							<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${ docinfo.id}','${ docinfo.docName}',1,true)">下载</a> 
          			   		</span>
						</p>
                    </dt>
					</c:forEach>
					</c:when>
					<c:otherwise>
					<dt>
                    <label>其他文件：</label>
                    </dt>
					</c:otherwise>
					</c:choose>
                </dl>
            </div>
		</div>
        </form>
        <div class="dbaniu" style=" margin-left:100px;">
        <a class="ydbz_s" href="javascript:void(0)" onclick="window.opener=null;window.close();">关闭</a>
        </div>
		<!--右侧内容部分结束--> 
</body>
</html>
