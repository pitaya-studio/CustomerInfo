<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<head>
<meta name="decorator" content="wholesaler" />
<title>批发商管理-批发商详情页</title>
<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"  type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/ckeditor/ckeditor.js"></script>
<script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>
<!--批发商模块的脚本-->
<script type="text/javascript" src="${ctxStatic}/js/tmp.supplier.js"></script>
<script type="text/javascript">
$(function(){
	//上传动作
	btfile();
	//切换卡
	switchingCard();
	// 返回
	$("#back").click(function(){
			location.href=contextPath + "/manage/saler/salerlist";
		});
	});
	</script>
</head>
<body>
        <!--右侧内容部分开始-->
		<div class="supplierLine">
			<a href="javascript:void(0)" class="select">基本信息填写</a>
			<a href="javascript:void(0)">网站信息</a>
			<a href="javascript:void(0)">银行账户</a>
			<a href="javascript:void(0)">资质上传</a>
		</div>
		<div class="switch-cen ">
            <div class="sysdiv sysdiv_coupon">
                <p>
                  <label>上级节点：</label>
                  <span>${office.parent.name }</span>
                </p>
                <p>
                  <label>批发商类型：</label>
                  <span class="checkboxdiv">${office.supplierTypeNames }</span>
                </p>
                <p>
                  <label>状态：</label>
                  <c:if test="${office.loginStatus==1 }"><span>启用</span></c:if>
                  <c:if test="${office.loginStatus==2 }"><span>停用</span></c:if>
                </p>
                <p>
                  <label>供应品牌：</label>
                  <span>${office.supplierBrand }</span>
                </p>
                <p>
                  <label>公司名称：</label>
                  <span>${office.companyName }</span>
                </p>
                <p>
                  <label>批发商名称：</label>
                  <span>${office.name }</span>
                </p>
                <p>
                  <label>批发商编码：</label>
                  <span>${office.code }</span>
                </p>
                <p>
                  <label>英文名称：</label>
                  <span>${office.enname }</span>
                </p>
                <p>
                  <label>所属地区(国内)：</label>
                  <span>${areaInternals }</span>
                </p>
                <p>
                  <label>所属地区(国际)：</label>
                  <span>${areaoverseas }</span>
                </p>
                <p>
                  <label>公司地址：</label>
                  <c:if test="${office.frontier==1 }">
                  	<span>
	                  	<trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${office.countryId}"/>
	                  	<trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${office.provinceId}"/>
	                  	<trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${office.cityId}"/>
	                  	<trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${office.districtId}"/>
	                  	${office.address }
	                  </span>
                  </c:if>
                  <c:if test="${office.frontier==2 }">
                  	<span>
	                  	<trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${office.countryId}"/>
	                  	<trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${office.provinceId}"/>
	                  	<trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${office.cityId}"/>
	                  	${office.address }
	                  </span>
                  </c:if>
                </p>
                <p>
                  <label>电话：</label>
                  <span>${office.districtCode }-${office.phone }</span>
                </p>
                <p>
                  <label>传真：</label>
                  <span>${office.districtCode }-${office.fax }</span>
                </p>
                <div>
                	<c:if test="${not empty supplyContacts }">
                		<c:forEach items="${supplyContacts }"  var="contacts"  varStatus="status">
	                		<p class="shopPeopleP">
	                			<label>联系人<em>${status.index+1 }</em>：</label>
			                   <span>${contacts.contactName }</span>
			                   <label>手机：</label>
			                   <span>${contacts.contactMobile }</span>
			                   <span class="kongr20"></span>
			                   <label>固定电话：</label>
			                   <span>${contacts.contactPhone }</span>
			                   <label>传真：</label>
			                   <span>${contacts.contactFax }</span>
			                   <span class="kongr20"></span>
			                   <label>Email：</label>
			                   <span>${contacts.contactEmail }</span>
			                   <label>QQ：</label>
			                   <span>${contacts.contactQQ }</span>
			                 </p>
                		</c:forEach>
                	</c:if>
                </div>
                <p>
                  <label>描述：</label>
                  <span>${office.remarks }</span>
                </p>
            </div>
        </div>
		<!--第二步-->
		<div class="switch-cen no">
            <div class="sysdiv sysdiv_coupon">
                <p>
                  <label>网站状态：</label>
                  <c:if test="${office.loginStatus==1 }">
                  	<span class="checkboxdiv">启用</span>
                  </c:if>
                  <c:if test="${office.loginStatus==2 }">
                  	<span class="checkboxdiv">停用</span>
                  </c:if>
                </p>
                <p>
                  <label>浏览权限：</label>
                  <c:if test="${office.loginShow==1 }">
                  	<span class="checkboxdiv">开放浏览</span>
                  </c:if>
                  <c:if test="${office.loginShow==2 }">
                  	<span class="checkboxdiv">关闭浏览</span>
                  </c:if>
                </p>
                <p>
                  <label>网站名称：</label>
                  <span>${office.loginName }</span>
                </p>
                <p>
                  <label>域名/网址：</label>
                  <span>${office.loginName }</span>
                </p>
                <p>
                  <label>网站LOGO：</label>
                  <span>${office.loginLogoName }<a style="margin-left:10px;" href="${ctx}/sys/docinfo/download/${office.logo }">下载</a></span>
                </p>
                <p>
                  <label>网站负责人：</label>
                 <span>${office.loginMaster }</span>
                </p>
                <p>
                  <label>网站负责人电话：</label>
                 <span>${office.loginMPhone }</span>
                </p>
                <p>
                  <label>网站客服电话：</label>
                 <span>${office.loginSPhone }</span>
                </p>
                <p>
                  <label>网站管理员邮箱：</label>
                 <span>${office.loginAMail }</span>
                </p>
                <p>
                  <label>QQ在线客服号码：</label>
                 <span>${office.loginSQQ }</span>
                </p>
            </div>
        </div>
        <!--第三步-->
        <div class="switch-cen no">
        	<div class="qdgl-cen">
        		<c:if test="${not empty banklist }">
        			<c:forEach items="${banklist }" var = "bank">
        				<!--第一个dl的类名加acount_nbor,剩余的不加此类名-->
        				<c:if test="${bank.defaultFlag==0 }">
        					<dl class="account account-text acount_nbor">
	        					<dt><b>默认账户</b></dt>
			                    <dd><span>账户名：</span><p>${bank.accountName }</p></dd>
			                    <dd><span>开户行名称：</span><p>${bank.bankName }</p></dd>
			                    <dd><span>开户行地址：</span><p>${bank.bankAddr }</p></dd>
			                    <dd><span>账户号码：</span><p>${bank.bankAccountCode }</p></dd>
			                    <dd><span>备注：</span><p>${bank.remarks }</p></dd>
			                </dl>
        				</c:if>
		                <c:if test="${bank.defaultFlag==1 }">
		                	<dl class="account account-text">
			                    <dd><span>账户名：</span><p>${bank.accountName }</p></dd>
			                    <dd><span>开户行名称：</span><p>${bank.bankName }</p></dd>
			                    <dd><span>开户行地址：</span><p>${bank.bankAddr }</p></dd>
			                    <dd><span>账户号码：</span><p>${bank.bankAccountCode }</p></dd>
			                    <dd><span>备注：</span><p>${bank.remarks }</p></dd>
			                </dl>
		                </c:if>
        			</c:forEach>
        		</c:if>
            </div>
        </div>
        <!--第四步-->
        <div class="switch-cen no">
            <div class="qdgl-cen">
                <dl class="wbyu-bot wbyu-bot2">
                    <dt>
                        <label>营业执照：</label>
                        <c:if test="${not empty qual1 }">
                       	 	${qual1.docInfoName }<a style="margin-left:10px;" href="${ctx}/sys/docinfo/download/${qual1.docInfoId }">下载</a>
                        </c:if>
                    </dt>
                    <dt>
                        <label>经营许可证：</label>
                        <c:if test="${not empty qual2 }">
                       	 	${qual2.docInfoName }<a style="margin-left:10px;" href="${ctx}/sys/docinfo/download/${qual2.docInfoId }">下载</a>
                        </c:if>
                    </dt>
                    <dt>
                        <label>税务登记证：</label>
                        <c:if  test="${not empty qual3 }">
                        	${qual3.docInfoName }<a style="margin-left:10px;" href="${ctx}/sys/docinfo/download/${qual3.docInfoId }">下载</a>
                        </c:if>
                    </dt>
                    <dt>
                        <label>组织机构代码证：</label>
                        <c:if test="${not empty qual4 }">
                        	${qual4.docInfoName }<a style="margin-left:10px;" href="${ctx}/sys/docinfo/download/${qual4.docInfoId }">下载</a>
                        </c:if>
                    </dt>
                    <dt>
                        <label>公司法人身份证（正反面在一起）：</label>
                        <c:if test="${not empty qual5 }">
                        	${qual5.docInfoName }<a style="margin-left:10px;" href="${ctx}/sys/docinfo/download/${qual5.docInfoId }">下载</a>
                    	</c:if>
                    </dt>
                    <dt>
                        <label>公司银行开户许可证：</label>
                        <c:if test="${not empty qual6 }">
                        	${qual6.docInfoName }<a style="margin-left:10px;" href="${ctx}/sys/docinfo/download/${qual6.docInfoId }">下载</a>
                    	</c:if>
                    </dt>
                    <dt>
                        <label>旅游业资质：</label>
                        <c:if test="${not empty qual7 }">
                       		 ${qual7.docInfoName }<a style="margin-left:10px;" href="${ctx}/sys/docinfo/download/${qual7.docInfoId }">下载</a>
                    	</c:if>
                    </dt>
                   	<c:if test="${not empty qualList }">
                   		<c:forEach items="${qualList }"  var ="qu">
                    		<dt>
		                    	<label>其他文件：</label>
		                        <c:if test="${not empty qu }">
			                        <span>${qu.title } :</span>
			                        ${qu.docInfoName }<a style="margin-left:10px;" href="${ctx}/sys/docinfo/download/${qu.docInfoId }">下载</a>
		                        </c:if>
		                    </dt>
                   		</c:forEach>
                   	</c:if>
                </dl>
            </div>
		</div>
        <div class="dbaniu" style=" margin-left:100px;"><a class="ydbz_s gray" id="back">返回</a></div>
		<!--右侧内容部分结束--> 
</body>
</html>
