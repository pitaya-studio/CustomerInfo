<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<c:set var="companyUuid" value="${fns:getCompanyUuid()}"/>
<!-- 针对C147andC109 奢华和优加：优惠，和申请办签信息的展示 控制开关 -->
<c:if test="${companyUuid == '75895555346a4db9a96ba9237eae96a5' or companyUuid == '7a81c5d777a811e5bc1e000c29cf2586'}">
    <input type="hidden" name="isForYouJia" id="isForYouJia" value="true">
    <c:set var="isForYouJia" value="true"/>
</c:if>
<c:if test="${!(companyUuid == '75895555346a4db9a96ba9237eae96a5' or companyUuid == '7a81c5d777a811e5bc1e000c29cf2586')}">
    <input type="hidden" name="isForYouJia" id="isForYouJia" value="false">
    <c:set var="isForYouJia" value="false"/>
</c:if>
<!-- 针对C109 奢华和优加：是否是散拼产品 控制开关 -->
<c:if test="${activityKind == '2'}">
    <c:set var="isLoose" value="true"/>
    <input type="hidden" name="isLoose" id="isLoose" value="true">
</c:if>
<c:if test="${activityKind != '2'}">
    <c:set var="isLoose" value="false"/>
    <input type="hidden" name="isLoose" id="isLoose" value="false">
</c:if>
<!-- 针对C109 奢华和优加：是否是同行价报名 控制开关 -->
<c:if test="${priceType == 0}">
    <c:set var="isSettlement" value="true"/>
    <input type="hidden" name="isSettlement" id="isSettlement" value="true">
</c:if>
<c:if test="${priceType != 0}">
    <c:set var="isSettlement" value="false"/>
    <input type="hidden" name="isSettlement" id="isSettlement" value="false">
</c:if>
<c:forEach items="${travelers}" var="travelerone" varStatus="s">
    <form class="travelerTable" name="travelerForm">
        <input type="hidden" name="travelerOrderId" value="${travelerone.traveler.orderId}">
        <input type="hidden" value="${singleDiffCurrencyId}" name="singleDiffCurrencyId">
        <input type="hidden" name="travelerId" value="${travelerone.traveler.id}">
        <input type="hidden" name="changeGroupFlag" value="${travelerone.traveler.delFlag}">

        <div class="tourist">
            <!--转团信息开始-->
            <c:if test="${not empty travelerone.changeGroupCode}">
                <div class="tourist-t tourist-zt">
                    <b class="tourist-t-onb">已转团</b>

                    <div class="tourist-t-r">
                        <label>团号：${travelerone.changeGroupCode}</label>
                        <label>${travelerone.changeProductType}</label>
                        <label>${travelerone.changeProductName}</label>
                        <label>操作人：${travelerone.changeCreateName}</label>
                    </div>
                </div>
            </c:if>
            <!--转团信息结束-->

            <!-- 退团信息开始 -->
            <c:if test="${travelerone.traveler.delFlag == '3'}">
                <div class="tourist-t tourist-zt">
                    <b class="tourist-t-onb">已退团</b>
                </div>
            </c:if>
            <!-- 退团信息结束 -->
            <div class="tourist-t">
                <span class="add_seachcheck" onclick="travelerBoxCloseOnAdd(this)"></span>
                <label class="ydLable">游客<em class="travelerIndex" style="vertical-align: baseline;"></em>:</label>

                <div class="tourist-t-off">
					<span class="fr" <c:if test="${priceType eq 2}">style="display: none;"</c:if>>
						<c:if test="${not empty travelerone.visaDeposit}">
                            押金：<span class="ydFont2">${travelerone.visaDeposit}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        </c:if>
						<c:if test="${priceType eq 2}">QUAUQ价</c:if><c:if test="${priceType eq 1}">直客价</c:if><c:if test="${priceType ne 1 and priceType ne 2 }">同行价</c:if>：<span name="jsPrice" class="ydFont2"></span>
						结算价：<span name="travelerClearPrice" class="ydFont2"></span>
					</span>
					<em style="vertical-align: baseline;"><span name="tName">${travelerone.traveler.name }</span></em>
					<label>
						<c:if test="${not empty travelerone.visaOrderIdStr}">
							<a target="_blank" class="ydbz_xwt" href="${ctx}/order/download/exportExcel?orderId=${travelerone.visaOrderIdStr}">下载预约表</a>
						</c:if>
					</label>
				</div>
				<div class="tourist-t-on">
					<c:choose>
						<c:when test="${productorder.orderStatus == '10'}">
							<label><input type="radio" class="traveler" <c:if test="${travelerone.traveler.personType=='1' }">checked="checked"</c:if> disabled="disabled"/>1/2<c:if test="${priceType eq 1}">直客</c:if><c:if test="${priceType ne 1 }">同行</c:if></label>
							<label><input type="radio" class="traveler" <c:if test="${travelerone.traveler.personType=='2' }">checked="checked"</c:if> disabled="disabled"/>3/4<c:if test="${priceType eq 1}">直客</c:if><c:if test="${priceType ne 1 }">同行</c:if></label>
						</c:when>
						<c:otherwise>
							<label><input type="radio" class="traveler" <c:if test="${travelerone.traveler.delFlag=='3' or travelerone.traveler.delFlag=='5' }">adultflag</c:if> <c:if test="${travelerone.traveler.personType=='1' }">checked="checked"</c:if> disabled="disabled"/>成人</label>
							<label><input type="radio" class="traveler" <c:if test="${travelerone.traveler.delFlag=='3' or travelerone.traveler.delFlag=='5' }">childflag</c:if> <c:if test="${travelerone.traveler.personType=='2' }">checked="checked"</c:if> disabled="disabled"/>儿童</label>
							<label><input type="radio" class="traveler" <c:if test="${travelerone.traveler.delFlag=='3' or travelerone.traveler.delFlag=='5' }">specialflag</c:if> <c:if test="${travelerone.traveler.personType=='3' }">checked="checked"</c:if> disabled="disabled"/>特殊人群</label>
						</c:otherwise>
					</c:choose>
					<c:choose>
					<c:when test="${travelerone.traveler.personType=='1'}">
	              		<input type="radio" class="temp" name="personType" value="1" checked="checked" style="display: none"/>
	              	</c:when>
	              	<c:when test="${travelerone.traveler.personType=='2'}">
	              		<input type="radio" class="temp" name="personType" value="2" checked="checked" style="display: none"/>
	              	</c:when>
	              	<c:when test="${travelerone.traveler.personType=='3'}">
	              		<input type="radio" class="temp" name="personType" value="3" checked="checked" style="display: none"/>
	              	</c:when>
	              </c:choose>
	              <c:if test="${fn:length(intermodalList) > 0}">
	                  <div class="tourist-t-r">
	                                                              是否联运：
	                      <label>
	                        <input type="radio" name="travelerIntermodalType" value="0" <c:if test="${travelerone.traveler.intermodalType=='0' }">checked="checked"</c:if> onclick="isIntermodal(this)"/>
	                                                                     不需要
	                      </label>
	                      <label>
	                        <input type="radio" name="travelerIntermodalType" value="1" <c:if test="${travelerone.traveler.intermodalType=='1' }">checked="checked"</c:if> onclick="isIntermodal(this)"/>
	                                                                     需要
	                      </label>
	                      <span>
	                         <select onchange="setIntermodal($(this))" name="intermodalStrategy">
                                 <c:forEach items="${intermodalList}" var="intermodal">
                                     <option value="${intermodal.price}" intermodalId="${intermodal.id}"
                                             priceCurrencyId="${intermodal.priceCurrency.id}"
                                             priceCurrency="${intermodal.priceCurrency.currencyName}"
                                             <c:if test="${travelerone.traveler.intermodalId==intermodal.id}">selected="selected"</c:if>>${intermodal.groupPart}</option>
                                 </c:forEach>
                             </select>
	                                                                  币种：<label
                                  name="priceCurrency">${travelerone.intermodal.priceCurrency.currencyName}</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                                                                  联运价格：<label
                                  name="intermodalPrice">${travelerone.intermodal.price}</label>
	                         <input type="hidden" name="intermodalId" value="${travelerone.traveler.intermodalId}"/>
	                      </span>
                        </div>
                    </c:if>
                </div>
            </div>

            <div flag="messageDiv">
                <div class="tourist-con">
                	<!-- 左侧 -->
                    <c:choose>
                        <c:when test="${isForYouJia eq true}">
                            <!--游客信息左侧开始-->
                            <div class="tourist-left">
                                <!-- 基本信息 -->
                                <div class="ydbz_tit ydbz_tit_child"><em class="ydExpand closeOrExpand"
                                                                         onclick="boxCloseOnAdd(this)"></em>基本信息
                                </div>
                                <c:forEach items="${travelerone.visaList}" var="tvisa">
                                    <c:if test="${tvisa.zbqType==1}">
                                        <c:set var="zbqType" value="${tvisa.zbqType}"></c:set>
                                    </c:if>
                                </c:forEach>
                                <ul class="tourist-info1 clearfix" flag="messageDiv">
                                    <li>
                                        <label class="ydLable"><span class="xing">*</span>姓名：</label>
                                        <input type="text" maxlength="25" name="travelerName"
                                               value="${travelerone.traveler.name }" class="traveler required"
                                               onkeyup="this.value=this.value.replaceSpecialChars()"
                                               onafterpaste="this.value=this.value.replaceSpecialChars()">
                                    </li>
                                    <li>
                                        <label class="ydLable">英文／拼音：</label>
                                        <input type="text" maxlength="100" name="travelerPinyin"
                                               value="${travelerone.traveler.nameSpell }" class="traveler">
                                    </li>
                                    <li>
                                        <label class="ydLable">性别：</label>
                                        <select name="sex" class="selSex required">
                                            <option value="1"
                                                    <c:if test="${travelerone.traveler.sex=='1' }">selected="selected"</c:if>>
                                                男
                                            </option>
                                            <option value="2"
                                                    <c:if test="${travelerone.traveler.sex=='2' }">selected="selected"</c:if>>
                                                女
                                            </option>
                                        </select>
                                    </li>
                                    <li>
                                        <label class="ydLable">出生日期：</label>
                                        <input type="text" name="birthDay"
                                               value='<fmt:formatDate value="${travelerone.traveler.birthDay}" pattern="dd/MM/yyyy" />' onclick="WdatePicker({dateFmt:'dd/MM/yyyy'})"
                                               class="traveler traveler2 dateinput"/>
                                    </li>
                                    <li>
                                        <label class="ydLable"><span class="xing"></span>联系电话：</label>
                                        <input type="text" name="telephone" class="traveler"
                                               value="${travelerone.traveler.telephone}" maxlength="50"
                                               onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"
                                               onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')">
                                    </li>
                                    <li>
                                        <label class="ydLable"><span class="xing"></span>护照类型：</label>
                                        <select name="passportType" class="selCountry">
                                            <c:forEach items="${passportTypeList}" var="passportType">
                                                <option value="${passportType.key}"
                                                        <c:if test="${travelerone.traveler.passportType == passportType.key}">selected="selected"</c:if>>
                                                        ${passportType.value}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </li>
                                    <li>
                                        <label class="ydLable">护照号：</label>
                                        <input type="text" name="passportCode"
                                               value="${travelerone.traveler.passportCode}"
                                               class="traveler" maxlength="30"
                                               onkeyup="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')"
                                               onafterpaste="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')">
                                    </li>
                                    <li>
                                        <label class="ydLable">发证日期：</label>
                                        <input type="text"
                                               value='<fmt:formatDate value="${travelerone.traveler.issuePlace }" pattern="dd/MM/yyyy" />' onclick="WdatePicker({dateFmt:'dd/MM/yyyy'})"
                                               name="issuePlace" class="traveler traveler2 dateinput">
                                    </li>
                                    <li>
                                        <label class="ydLable">护照有效期：</label>
                                        <input type="text" name="passportValidity"
                                               value='<fmt:formatDate value="${travelerone.traveler.passportValidity}" pattern="dd/MM/yyyy" />' onclick="WdatePicker({dateFmt:'dd/MM/yyyy'})"
                                               class="traveler traveler2 dateinput">
                                    </li>
                                    <li>
	                                    <label class="ydLable">护照签发地：</label>
	                                    <input type="text" name="passportPlace" class="traveler" value="${travelerone.traveler.passportPlace }">
                                	</li>
                                    <li>
                                        <label class="ydLable"><span class="xing"
                                                                     <c:if test="${zbqType!=1}">style="display: none;"</c:if>></span>身份证号：</label>
                                        <input type="text" name="idCard" value="${travelerone.traveler.idCard}"
                                               style="width:128px;padding-left:1px;padding-right:1px;" class="traveler"
                                               maxlength="18"
                                               onkeyup="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')"
                                               onafterpaste="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')"
                                               onblur="getBirthday(this);">
                                    </li>
                                    <div class="kong"></div>
                                    <li style="width: 402px;margin-left:10px;">
                                        <label class="ydLable" style="width:120px">职务（中文/英文）：</label>
                                        <input type="text" name="positionCn"
                                               style="width:128px;padding-left:1px;padding-right:1px;"
                                               value="${travelerone.traveler.positionCn}" class="traveler"> /
                                        <input type="text" name="positionEn"
                                               style="width:128px;padding-left:1px;padding-right:1px;"
                                               value="${travelerone.traveler.positionEn}" class="traveler">
                                    </li>
                                </ul>
                                <!-- 签证信息 -->
                                <div class="ydbz_tit ydbz_tit_child"><em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>签证信息
                                </div>
                                <ul flag="messageDiv" class="zbqinfo">
                                    <div class="ydbz_tit ydbz_tit_child">申请办签</div>
                                    <div class="ydbz_scleft">
                                        <table class="table-visa">
                                            <thead>
                                            <tr>
                                                <th width="15%">申请国家<font color="red">*</font></th>
                                                <th width="15%">领区<font color="red">*</font></th>
                                                <th width="15%">签证类型<font color="red">*</font></th>
                                                <th width="10%">预计出团时间</th>
                                                <th width="15%">预计约签时间</th>
                                                <th width="15%" name="adddel">操作</th>
                                            </tr>
                                            </thead>
                                            <c:forEach items="${fns:obtainTravelerAllVisaList(travelerone.traveler.orderId,travelerone.traveler.id)}" var="tvisa" varStatus="varStatus">
                                            <tbody id="qztemplate">
                                                <tr class="${tvisa.countryName}" name="visainfo">
                                                    <input type="hidden" value="${tvisa.id}" name="orgVisaId">
                                                    <td>
                                                        <select name="countrySelect">
                                                            <option value="-1" <c:if test="${'-1' eq tvisa.countryId}">selected="selected"</c:if>>请选择</option>
                                                            <c:forEach items="${countryList}" var="country">
                                                                <option value="${country.id}" <c:if test="${country.id eq tvisa.countryId}">selected="selected"</c:if>>${country.countryName_cn}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </td>
                                                    <td>
                                                        <select name="manor">
                                                            <option value="-1" <c:if test="${'-1' eq tvisa.manorId}">selected="selected"</c:if>>请选择</option>
                                                            <c:forEach items="${fns:getDictByType('from_area')}" var="dict">
                                                                <option value="${dict.value}" <c:if test="${dict.value eq tvisa.manorId}">selected="selected"</c:if>>${dict.label}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </td>
                                                    <td>
                                                    	<input type="hidden" value="${tvisa.visaType}" name="orgVisaType">
                                                        <select name="visaType">
                                                            <option value="-1" <c:if test="${'-1' eq tvisa.visaType}">selected="selected"</c:if>>请选择</option>
                                                            <c:forEach items="${fns:getDictByType('new_visa_type')}" var="dict">
                                                                <option value="${dict.value}" <c:if test="${dict.value eq tvisa.visaType}">selected="selected"</c:if>>${dict.label}</option>
                                                            </c:forEach>
                                                            <option value="0" <c:if test="${'0' eq tvisa.visaType}">selected="selected"</c:if>>自备签证</option>
                                                        </select>
                                                    </td>
                                                    <td>
                                                        <fmt:formatDate value="${productGroup.groupOpenDate}" pattern="dd/MM/yyyy"/>
                                                    </td>
                                                    <td>
                                                        <input type="text" name="contractDate" value="<fmt:formatDate value="${tvisa.contractDate}" pattern="dd/MM/yyyy"/>" onclick="WdatePicker({dateFmt:'dd/MM/yyyy'})" class="inputTxt dateinput">
                                                    </td>
                                                    <c:if test="${varStatus.count eq 1 }">
	                                                    <td name="adddel" id="adddel">
	                                                        <a class="add" href="javascript:void(0)" onclick="addQz(this)">+</a>
	                                                    </td>
                                                    </c:if>
                                                    <c:if test="${varStatus.count ne 1 }">
	                                                    <td name="adddel" id="adddel">
	                                                        <a class="add" href="javascript:void(0)" onclick="addQz(this)">+</a>
	                                                        <a class="del" href="javascript:void(0)" onclick="delQz(this)">-</a>
	                                                    </td>
                                                    </c:if>
                                                </tr>
                                            </tbody>
                                            </c:forEach>
                                        </table>
                                    </div>
                                    <div class="ydbz_tit ydbz_tit_child">上传资料
                                        <c:if test="${not empty travelerone.docIds}">
                                            <a class="ydbz_x downloadFiles"
                                               onclick="downloadFiles('${travelerone.docIds}');"
                                               style="display: none;">全部下载</a>
                                        </c:if>
                                    </div>
                                    <ul name="visafiles" class="ydbz_2uploadfile ydbz_scleft">
                                    	<input type="hidden" name="tobeDelFiles" value="">
                                        <li class="seach25 seach33">
                                        	<p>护照首页：</p>
                                        	<input type="text" readonly="readonly">
                                        	<input name="passportfile" type="hidden" value="${travelerone.passportfileNameStr }">
                                            <input type="button" name="passport" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','passportfile',this,'false');"/>
                                            <c:forEach items="${travelerone.passportfile}" var="travelerFile">
                                                <span class="seach_r" style="position:relative;margin-left:67px">
                                                    <b>${travelerFile.fileName}</b>
                                                    <input type="hidden" name="passportdocID" value="${travelerFile.srcDocId }">
	                                            	<input type="hidden" name="passportdocName" value="${travelerFile.fileName }">
	                                            	<input type="hidden" name="passportdocPath" value="">
                                                    <a class="deleteicon" style="margin-left:10px;position: absolute;top:9px;" href="javascript:void(0)" onclick="deleteFiles('',this)">x</a>
                                                </span>
                                            </c:forEach>
                                            <span class="fileLogo"></span>
                                        </li>
                                        <li class="seach25 seach33">
                                        	<p>个人资料表：</p>
                                        	<input type="text" readonly="readonly">
                                        	<input name="idcardfrontfile" type="hidden" value="${travelerone.idcardfrontfileNameStr }">
                                            <input type="button" name="idcardfront" class="btn btn-primary" value="上传"
                                                   onclick="uploadFiles('${ctx}','idcardfrontfile',this,'false');">
                                            <c:forEach items="${travelerone.idcardfrontfile}" var="travelerFile">
                                                <span class="seach_r" style="position:relative;margin-left:67px">
                                                    <b>${travelerFile.fileName}</b>
                                                    <input type="hidden" name="idcardfrontdocID" value="${travelerFile.srcDocId }">
	                                            	<input type="hidden" name="idcardfrontdocName" value="${travelerFile.fileName }">
	                                            	<input type="hidden" name="idcardfrontdocPath" value="">
                                                    <a class="deleteicon"
                                                       style="margin-left:10px;position: absolute;top:9px;"
                                                       href="javascript:void(0)" onclick="deleteFiles('',this)">x</a>
                                                </span>
                                            </c:forEach>
                                            <span class="fileLogo"></span>
                                        </li>
                                        <li class="seach25 seach33">
                                        	<p>担保书：</p>
                                        	<input type="text" readonly="readonly">
                                        	<input name="entryformfile" type="hidden" value="${travelerone.entryformfileNameStr }">
                                            <input type="button" name="entryform" class="btn btn-primary" value="上传"
                                                   onclick="uploadFiles('${ctx}','entryformfile',this,'false');">
                                            <c:forEach items="${travelerone.entryformfile}" var="travelerFile">
                                                <span class="seach_r" style="position:relative;margin-left:67px">
                                                    <b>${travelerFile.fileName}</b>
                                                    <input type="hidden" name="entryformdocID" value="${travelerFile.srcDocId }">
	                                            	<input type="hidden" name="entryformdocName" value="${travelerFile.fileName }">
	                                            	<input type="hidden" name="entryformdocPath" value="">
                                                    <a class="deleteicon"
                                                       style="margin-left:10px;position: absolute;top:9px;"
                                                       href="javascript:void(0)" onclick="deleteFiles('',this)">x</a>
                                                </span>
                                            </c:forEach>
                                            <span class="fileLogo"></span>
                                        </li>
                                        <p class="kong"></p>
                                        <li class="seach25 seach33">
                                        	<p>参团告知书：</p>
	                                        <input type="text" readonly="readonly">
	                                        <input name="photofile" type="hidden" value="${travelerone.photofileNameStr }">
                                            <input type="button" name="photo" class="btn btn-primary" value="上传"
                                                   onclick="uploadFiles('${ctx}','photofile',this,'false');">
                                            <c:forEach items="${travelerone.photofile}" var="travelerFile">
                                                <span class="seach_r" style="position:relative;margin-left:67px">
                                                    <b>${travelerFile.fileName}</b>
                                                    <input type="hidden" name="photodocID" value="${travelerFile.srcDocId }">
	                                            	<input type="hidden" name="photodocName" value="${travelerFile.fileName }">
	                                            	<input type="hidden" name="photodocPath" value="">
                                                    <a class="deleteicon"
                                                       style="margin-left:10px;position: absolute;top:9px;"
                                                       href="javascript:void(0)" onclick="deleteFiles('',this)">x</a>
                                                </span>
                                            </c:forEach>
                                            <span class="fileLogo"></span>
                                        </li>
                                        <li class="seach25 seach33">
                                        	<p>健康承诺书：</p>
                                        	<input type="text" readonly="readonly">
                                        	<input name="idcardbackfile" type="hidden" value="${travelerone.idcardbackfileNameStr }">
                                            <input type="button" name="idcardback" class="btn btn-primary" value="上传"
                                                   onclick="uploadFiles('${ctx}','idcardbackfile',this,'false');">
                                            <c:forEach items="${travelerone.idcardbackfile}" var="travelerFile">
                                                <span class="seach_r" style="position:relative;margin-left:67px">
                                                    <b>${travelerFile.fileName}</b>
                                                    <input type="hidden" name="idcardbackdocID" value="${travelerFile.srcDocId }">
	                                            	<input type="hidden" name="idcardbackdocName" value="${travelerFile.fileName }">
	                                            	<input type="hidden" name="idcardbackdocPath" value="">
                                                    <a class="deleteicon"
                                                       style="margin-left:10px;position: absolute;top:9px;"
                                                       href="javascript:void(0)" onclick="deleteFiles('',this)">x</a>
                                                </span>
                                            </c:forEach>
                                            <span class="fileLogo"></span>
                                        </li>
                                        <li class="seach25 seach33">
                                            <p>其　它：</p>
                                            <input type="text" readonly="readonly">
                                            <input name="otherfile" type="hidden" value="${travelerone.otherfileNameStr }">
                                            <input type="button" name="other" class="btn btn-primary" value="上传"
                                                   onclick="uploadFiles('${ctx}','otherfile',this,'false');">
                                            <c:forEach items="${travelerone.otherfile}" var="travelerFile">
                                                <span class="seach_r" style="position:relative;margin-left:67px">
                                                    <b>${travelerFile.fileName}</b>
                                                    <input type="hidden" name="otherdocID" value="${travelerFile.srcDocId }">
	                                            	<input type="hidden" name="otherdocName" value="${travelerFile.fileName }">
	                                            	<input type="hidden" name="otherdocPath" value="">
                                                    <a class="deleteicon"
                                                       style="margin-left:10px;position: absolute;top:9px;"
                                                       href="javascript:void(0)" onclick="deleteFiles('',this)">x</a>
                                                </span>
                                            </c:forEach>
                                            <span class="fileLogo"></span>
                                        </li>
                                        <p class="kong"></p>
                                        <li class="seach25 seach33">
                                            <p>签证附件：</p>
                                            <input type="text" readonly="readonly">
                                            <input name="visaannexfile" type="hidden" value="${travelerone.visaannexfileNameStr }">
                                            <input type="button" name="visaannex" class="btn btn-primary" value="上传"
                                                   onclick="uploadFiles('${ctx}','visaannexfile',this,'false');">
                                            <c:forEach items="${travelerone.visaannexfile}" var="travelerFile">
                                                <span class="seach_r" style="position:relative;margin-left:67px">
                                                    <b>${travelerFile.fileName}</b>
                                                    <input type="hidden" name="visaannexdocID" value="${travelerFile.srcDocId }">
	                                            	<input type="hidden" name="visaannexdocName" value="${travelerFile.fileName }">
	                                            	<input type="hidden" name="visaannexdocPath" value="">
                                                    <a class="deleteicon"
                                                       style="margin-left:10px;position: absolute;top:9px;"
                                                       href="javascript:void(0)" onclick="deleteFiles('',this)">x</a>
                                                </span>
                                            </c:forEach>
                                            <span class="fileLogo"></span>
                                        </li>

                                    </ul>
                                </ul>
                                <div class="ydbz_tit ydbz_tit_child"><span style="display:inline-block">备注：</span>
                                <textarea name="remark" class="textarea_long"
                                          maxlength="200">${travelerone.traveler.remark}</textarea>
                                </div>
                            </div>
                            <!--游客信息左侧结束-->
                        </c:when>
                        <c:otherwise>
                            <!--游客信息左侧开始-->
                            <div class="tourist-left">
                                <!-- 基本信息 -->
                                <div class="ydbz_tit ydbz_tit_child">
                                	<em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>基本信息
                                </div>
                                <c:forEach items="${travelerone.visaList}" var="tvisa">
                                    <c:if test="${tvisa.zbqType==1}">
                                        <c:set var="zbqType" value="${tvisa.zbqType}"></c:set>
                                    </c:if>
                                </c:forEach>
                                <ul class="tourist-info1 clearfix" flag="messageDiv">
                                    <li>
                                        <label class="ydLable"><span class="xing">*</span>姓名：</label>
                                        <input type="text" maxlength="25" name="travelerName"
                                               value="${travelerone.traveler.name }" class="traveler required"
                                               onkeyup="this.value=this.value.replaceSpecialChars()"
                                               onafterpaste="this.value=this.value.replaceSpecialChars()">
                                    </li>
                                    <li>
                                        <label class="ydLable">英文／拼音：</label>
                                        <input type="text" maxlength="100" name="travelerPinyin"
                                               value="${travelerone.traveler.nameSpell }" class="traveler">
                                    </li>
                                    <li>
                                        <label class="ydLable">性别：</label>
                                        <select name="sex" class="selSex required">
                                            <option value="1"
                                                    <c:if test="${travelerone.traveler.sex=='1' }">selected="selected"</c:if>>
                                                男
                                            </option>
                                            <option value="2"
                                                    <c:if test="${travelerone.traveler.sex=='2' }">selected="selected"</c:if>>
                                                女
                                            </option>
                                        </select>
                                    </li>
                                    <li>
                                        <label class="ydLable">国籍：</label>
                                        <select name="nationality" class="selCountry">
                                            <c:forEach items="${countryList}" var="country">
                                                <option value="${country.id}"
                                                        <c:if test="${travelerone.traveler.nationality==country.id }">selected="selected"</c:if>>
                                                        ${country.countryName_cn}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </li>
                                    <li>
                                        <label class="ydLable">出生日期：</label>
                                        <input type="text" name="birthDay"
                                               value='<fmt:formatDate value="${travelerone.traveler.birthDay}" pattern="yyyy-MM-dd"/>'
                                               class="traveler traveler2 dateinput"/>
                                    </li>
                                    <li>
                                        <label class="ydLable"><span class="xing"></span>联系电话：</label>
                                        <input type="text" name="telephone" class="traveler"
                                               value="${travelerone.traveler.telephone}" maxlength="50"
                                               onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"
                                               onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')">
                                    </li>
                                    <li>
                                        <label class="ydLable">护照号：</label>
                                        <input type="text" name="passportCode"
                                               value="${travelerone.traveler.passportCode}" class="traveler"
                                               maxlength="30"
                                               onkeyup="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')"
                                               onafterpaste="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')">
                                    </li>
                                     <c:if test="${fns:getUser().company.uuid eq '980e4c74b7684136afd89df7f89b2bee'}"> 
                                      <li>
	                                    <label class="ydLable">出生地：</label>
	                                    <input type="text" name="hometown" value="${travelerone.traveler.hometown }" class="traveler" maxlength="30"
	                                           onblur="this.value=this.value.replace(/[^\u4e00-\u9fa5]+/g,'')"
	                                           onafterpaste="this.value=this.value.replace(/[^\u4e00-\u9fa5]+/g,'')">
	                                </li>
	                                <li>
	                                    <label class="ydLable">签发地：</label>
	                                    <input type="text" name="issuePlace1" value="${travelerone.traveler.issuePlace1 }" class="traveler" maxlength="10"
	                                            onblur="this.value=this.value.replace(/[^\u4e00-\u9fa5]+/g,'')"
                                           onafterpaste="this.value=this.value.replace(/[^\u4e00-\u9fa5]+/g,'')">
	                                </li>
	                                 <li>
	                                    <label class="ydLable">签发日期：</label>
	                                    <input type="text" name="issueDate" value="<fmt:formatDate value="${travelerone.traveler.issueDate }" pattern="yyyy-MM-dd" />" class="traveler traveler2 dateinput" onclick="WdatePicker()">
	                                </li>
                                 </c:if>  
                                    <li>
                                        <label class="ydLable">发证日期：</label>
                                        <input type="text"
                                               value='<fmt:formatDate value="${travelerone.traveler.issuePlace }" pattern="yyyy-MM-dd"/>'
                                               name="issuePlace" class="traveler traveler2 dateinput">
                                    </li>
                                    <li>
                                        <label class="ydLable">护照有效期：</label>
                                        <input type="text" name="passportValidity"
                                               value='<fmt:formatDate value="${travelerone.traveler.passportValidity}" pattern="yyyy-MM-dd"/>'
                                               class="traveler traveler2 dateinput">

                                    </li>
                                    <li>
                                        <label class="ydLable"><span class="xing"
                                                                     <c:if test="${zbqType!=1}">style="display: none;"</c:if>></span>身份证号：</label>
                                        <input type="text" name="idCard" value="${travelerone.traveler.idCard}"
                                               style="width:128px;padding-left:1px;padding-right:1px;"
                                               class="traveler" maxlength="18"
                                               onkeyup="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')"
                                               onafterpaste="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')"
                                               onblur="getBirthday(this);">
                                    </li>
                                    <li>
                                        <label class="ydLable"><span class="xing"></span>护照类型：</label>
                                        <select name="passportType" class="selCountry">
                                            <c:forEach items="${passportTypeList}" var="passportType">
                                                <option value="${passportType.key}"
                                                        <c:if test="${travelerone.traveler.passportType == passportType.key}">selected="selected"</c:if>>
                                                        ${passportType.value}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </li>
                                </ul>
                                <!-- 签证信息 -->
                                <div class="ydbz_tit ydbz_tit_child">
                                	<em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>签证信息
                                </div>
                                <ul flag="messageDiv" class="zbqinfo">
                                    <div class="ydbz_tit ydbz_tit_child">自备签
                                        <p class="tourist-ckb">
                                            <label class="ydLable">自备签国家：</label>
                                            <c:forEach items="${travelerone.visaList}" var="tvisa" varStatus="s">
                                                <input type="checkbox" class="traveler" name="zibeiqian" value="${tvisa.applyCountry.name}" <c:if test="${tvisa.zbqType==1 }">checked="checked"</c:if>>
                                                <label class="ckb-txt">自备${tvisa.applyCountry.name}签证</label>
                                            </c:forEach>
                                        </p>
                                    </div>
                                    <div class="zjlx-tips zibei-tips" <c:if test="${tvisa.zbqType==1}">style="display: block;"</c:if>>
                                        <i class="arrow1">&nbsp;</i>
                                        <c:forEach items="${travelerone.visaList}" var="tvisa">
                                            <ul class="tourist-type clearfix"
                                                <c:if test="${tvisa.zbqType==0 }">style="display: none;" </c:if>
                                                <c:if test="${tvisa.zbqType==1 }">style="display: block;"</c:if>>
                                                <li>
                                                    <label class="ydLable">${tvisa.applyCountry.name}签证有效期：</label>
                                                    <input type="text" name="zbqVisaDate"
                                                           value="<fmt:formatDate value="${tvisa.visaDate}" pattern="yyyy-MM-dd"/>"
                                                           onclick="WdatePicker()" class="dateinput">
                                                </li>
                                            </ul>
                                        </c:forEach>
                                    </div>
                                    <div class="ydbz_tit ydbz_tit_child notzbq-uploadfile" >申请办签</div>
                                    <div class="ydbz_scleft notzbq-uploadfile">
                                        <table class="table-visa">
                                            <thead>
                                            <tr>
                                                <th width="15%">申请国家</th>
                                                <th width="15%">领区</th>
                                                <th width="15%">签证类别</th>
                                                <th width="10%">预计出团时间</th>
                                                <th width="15%">预计约签时间</th>
                                                <!-- 0211需求,新增预计回团时间,限定为批发商星徽四海-s -->
                                                <%-- <c:if test="${fns:getUser().company.uuid eq '0e19ac500f78483d8a9f4bb768608629'}">
	                                                <th width="10%"><span style="color:red;">*</span>预计回团时间</th>
												</c:if> --%>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach items="${travelerone.visaList}" var="tvisa">
                                                <tr class="${tvisa.applyCountry.name}" name="visainfo"
                                                    <c:if test="${tvisa.zbqType==1}">style="display: none;"</c:if>>
                                                    <td>${tvisa.applyCountry.name}
                                                        <input type="hidden" value="${tvisa.applyCountry.id}"
                                                               name="visaCountryId">
                                                        <input type="hidden" value="${tvisa.sysCountryId}"
                                                               name="sysCountryId">
                                                    </td>
                                                    <td>
                                                    	<%-- <select name="manor" onchange="changeManor(this)"> --%>
                                                        <select name="manor">
                                                            <c:forEach items="${tvisa.manorList}" var="manor">
                                                                <option value="${manor.value}"
                                                                        <c:if test="${manor.value==tvisa.manorId}">selected="selected"</c:if>>${manor.label}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </td>
                                                    <td>
                                                        <select name="visaType">
                                                            <c:forEach items="${tvisa.visaTypeList}" var="visaType">
                                                                <option value="${visaType.value}"
                                                                        <c:if test="${visaType.value==tvisa.visaTypeId}">selected="selected"</c:if>>${visaType.label}</option>
                                                            </c:forEach>
                                                        </select>
                                                        <input type="hidden" name="tempVisaType"
                                                               value="${tvisa.visaTypeId}">
                                                    </td>
                                                    <%-- <td><fmt:formatDate value="${tvisa.groupOpenDate}"
                                                                        pattern="yyyy-MM-dd"/></td> --%>
                                                    <td><input type="text" name="groupOpenDate" 
													       value="<fmt:formatDate value="${tvisa.groupOpenDate}" pattern="yyyy-MM-dd"/>"
													       onclick="WdatePicker()" class="inputTxt dateinput">
													</td>
                                                    <td><input type="text" name="contractDate"
                                                               value="<fmt:formatDate value="${tvisa.contractDate}" pattern="yyyy-MM-dd"/>"
                                                               onclick="WdatePicker()" class="inputTxt dateinput">
                                                    </td>
                                                    <!-- 0211需求,新增预计回团时间,限定为批发商星徽四海-s -->
                                                    <%-- <c:if test="${fns:getUser().company.uuid eq '0e19ac500f78483d8a9f4bb768608629'}">
		                                                <td><input type="text" name="groupBackDate" 
														       value="<fmt:formatDate value="${tvisa.groupBackDate}" pattern="yyyy-MM-dd"/>"
														       onclick="WdatePicker()" class="inputTxt dateinput">
														</td>
													</c:if> --%>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="ydbz_tit ydbz_tit_child">上传资料
                                        <c:if test="${not empty travelerone.docIds}">
                                            <a class="ydbz_x downloadFiles"
                                               onclick="downloadFiles('${travelerone.docIds}');"
                                               style="display: none;">全部下载</a>
                                        </c:if>
                                    </div>
                                    <ul class="ydbz_2uploadfile ydbz_scleft certificate">
                                        <li class="seach25 seach33">
                                        	<p>护照首页：</p>
	                                        <input name="passportfile" value="<c:if test="${not empty travelerone.passportfile and fn:length(travelerone.passportfile) gt 0}">${travelerone.passportfile[0].fileName}</c:if>"
	                                              type="text" readonly="readonly">
											<input type="button" name="passport" class="btn btn-primary" value="上传"
	                                                onclick="uploadFiles('${ctx}','passportfile',this,'true');"/>
											<span class="fileLogo"></span>
										</li>
                                        <li class="seach25 seach33">
                                        	<p>身份证正面：</p>
	                                        <input name="idcardfrontfile" value="<c:if test="${not empty travelerone.idcardfrontfile and fn:length(travelerone.idcardfrontfile) gt 0}">${travelerone.idcardfrontfile[0].fileName}</c:if>"
													type="text" readonly="readonly">
											<input type="button" name="idcardfront" class="btn btn-primary" value="上传"
	                                                onclick="uploadFiles('${ctx}','idcardfrontfile',this,'true');">
											<span class="fileLogo"></span>
										</li>
                                        <li class="seach25 seach33">
                                        	<p>申请表格：</p>
											<input name="entryformfile" value="<c:if test="${not empty travelerone.entryformfile and fn:length(travelerone.entryformfile) gt 0}">${travelerone.entryformfile[0].fileName}</c:if>"
												type="text" readonly="readonly">
											<input type="button" name="entryform" class="btn btn-primary" value="上传"
                                                onclick="uploadFiles('${ctx}','entryformfile',this,'true');">
											<span class="fileLogo"></span>
										</li>
										<p class="kong"></p>
                                        <li class="seach25 seach33">
                                        	<p>房产证：</p>
	                                        <input name="housefile"	value="<c:if test="${not empty travelerone.housefile and fn:length(travelerone.housefile) gt 0}">${travelerone.housefile[0].fileName}</c:if>"
												type="text" readonly="readonly">
											<input type="button" name="house" class="btn btn-primary" value="上传"
	                                                onclick="uploadFiles('${ctx}','housefile',this,'true');">
											<span class="fileLogo"></span>
										</li>
                                        <li class="seach25 seach33">
                                        	<p>电子照片：</p>
                                        	<input name="photofile" value="<c:if test="${not empty travelerone.photofile  and fn:length(travelerone.photofile) gt 0}">${travelerone.photofile[0].fileName}</c:if>"
												type="text" readonly="readonly">
											<input type="button" name="photo" class="btn btn-primary" value="上传"
                                                onclick="uploadFiles('${ctx}','photofile',this,'true');">
											<span class="fileLogo"></span>
										</li>
                                        <li class="seach25 seach33">
                                        	<p>身份证反面：</p>
                                        	<input name="idcardbackfile" value="<c:if test="${not empty travelerone.idcardbackfile and fn:length(travelerone.idcardbackfile) gt 0}">${travelerone.idcardbackfile[0].fileName}</c:if>"
												type="text" readonly="readonly">
											<input type="button" name="idcardback" class="btn btn-primary" value="上传"
                                                onclick="uploadFiles('${ctx}','idcardbackfile',this,'true');">
											<span class="fileLogo"></span>
										</li>
										<p class="kong"></p>
                                        <li class="seach25 seach33">
                                        	<p>户口本：</p>
											<input name="residencefile" value="<c:if test="${not empty travelerone.residencefile and fn:length(travelerone.residencefile) gt 0}">${travelerone.residencefile[0].fileName}</c:if>"
												type="text" readonly="readonly">
											<input type="button" name="residence" class="btn btn-primary" value="上传"
                                                onclick="uploadFiles('${ctx}','residencefile',this,'true');">
											<span class="fileLogo"></span>
										</li>
                                        <li class="seach25 seach33">
                                        	<p>其　它：</p>
                                        	<input name="otherfile" value="<c:if test="${not empty travelerone.otherfile  and fn:length(travelerone.otherfile) gt 0}">${travelerone.otherfile[0].fileName}</c:if>"
												type="text" readonly="readonly">
											<input type="button" name="other" class="btn btn-primary" value="上传"
                                                onclick="uploadFiles('${ctx}','otherfile',this,'true');">
											<span class="fileLogo"></span>
										</li>
                                        <li class="seach25 seach33">
                                        	<p>签证附件：</p>
                                        	<input name="visaannexfile" value="<c:if test="${not empty travelerone.visaannexfile  and fn:length(travelerone.visaannexfile) gt 0}">${travelerone.visaannexfile[0].fileName}</c:if>"
												type="text" readonly="readonly">
											<input type="button" name="visaannex" class="btn btn-primary" value="上传"
                                                onclick="uploadFiles('${ctx}','visaannexfile',this,'true');">
											<span class="fileLogo"></span>
										</li>
                                        <p class="kong"></p>

                                    </ul>
                                    <!-- 办证资料 -->
                                    <div class="ydbz_tit ydbz_tit_child">办证资料</div>
                                    <ul class="seach25 seach100 ydbz_2uploadfile">
                                        <p>原件：</p> ${original}<br/>

                                        <p>复印件：</p>${copyoriginal}
                                    </ul>
                                </ul>
                                <div class="ydbz_tit ydbz_tit_child"><span
                                        style="display:inline-block">备注：</span><textarea name="remark"
                                                                                         class="textarea_long"
                                                                                         maxlength="200">${travelerone.traveler.remark}</textarea>
                                </div>
                            </div>
                            <!--游客信息左侧结束-->
                        </c:otherwise>
                    </c:choose>

                    <%--游客信息右侧信息--%>
                    <c:choose>
                        <c:when test="${isForYouJia eq true and isLoose eq true and isSettlement eq true}">
                            <!--游客信息右侧开始-->
                            <div class="tourist-right">
                                <div class="bj-info">
                                    <div class="ydbz_tit ydbz_tit_child">报价</div>
                                    <div class="clearfix">
                                        <ul class="tourist-info2">
                                            <li class="tourist-info2-first">
                                                <label class="ydLable2 ydColor1">住房要求：</label>
                                                <select class="selZF" name="hotelDemand">
                                                    <option value="2" <c:if test="${travelerone.traveler.hotelDemand=='2'}">selected="selected"</c:if>>双人间</option>
                                                    <option value="1" <c:if test="${travelerone.traveler.hotelDemand=='1'}">selected="selected"</c:if>>单人间</option>
                                                </select>
                                            </li>
                                            <li>
                                                <label>单人房差：</label>
                                                <c:if test="${travelerone.traveler.hotelDemand=='2'}">
                                                    <span>0</span>
                                                    <input type="hidden" name="singleDiff" class="traveler" value="0">
                                                </c:if>
                                                <c:if test="${travelerone.traveler.hotelDemand=='1'}">
                                                    <span>${travelerone.traveler.singleDiff}</span>
                                                    <input type="hidden" name="singleDiff" class="traveler" value="${travelerone.traveler.singleDiff}">
                                                </c:if>
                                                /间 x
                                                <input type="text" class="ipt4" name="sumNight" maxlength="3" value="${travelerone.traveler.singleDiffNight}" onkeyup="changeSumNight(this)" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')">
												晚
                                            </li>
                                        </ul>
                                    </div>
                                    <div class="yd-line"></div>
                                    <div class="clearfix">
                                        <!-- 修改订单 -->
                                        <c:if test="${not empty update}">
                                            <div class="clearfix">
                                                <div class="traveler-rebatesDiv">
                                                		<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
                                                	   <c:choose>
												         	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
																<label class="ydLable2 ydColor1">预计个人宣传费：</label>
															</c:when>
											            <c:otherwise>
											                <label class="ydLable2 ydColor1">预计个人返佣：</label>
											             </c:otherwise>
											         </c:choose>   
                                                    <select name="rebatesCurrency">
                                                        <c:if test="${not empty currencyList }">
                                                            <c:forEach items="${currencyList}" var="curren">
                                                                <option value="${curren.id }"
                                                                        <c:if test="${not empty fns:getScheduleCurrencyID(travelerone.traveler.rebatesMoneySerialNum,curren.id) }">selected="selected"</c:if>>${curren.currencyName }</option>
                                                            </c:forEach>
                                                        </c:if>
                                                    </select>
                                                    <input type="text"
                                                           value="${fns:getMoneyAmountNUM(travelerone.traveler.rebatesMoneySerialNum) }"
                                                           class="ipt-rebates" name="rebatesMoney" maxlength="9"
                                                           onafterpaste="checkRebatesValue(this)"
                                                           onkeyup="checkRebatesValue(this)"/>
                                                </div>
                                            </div>
                                        </c:if>
                                        <!-- 订单详情 -->
                                        <c:if test="${empty update}">
                                            <div class="traveler-rebatesDiv">
                                            	<!-- 265需求，针对鼎鸿假期，将返佣字段改为宣传费 -->
                                                	   <c:choose>
												         	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
																<label class="ydLable2 ydColor1">预计个人宣传费：</label>
															</c:when>
												            <c:otherwise>
												                <label class="ydLable2 ydColor1">预计个人返佣：</label>
												             </c:otherwise>
												         </c:choose>   
                                            <span>
                                            ${travelerone.rebatesCurrency.currencyMark}
                                            <c:if test="${not empty travelerone.rebatesMoney.amount }">${travelerone.rebatesMoney.amount }</c:if>
                                            <c:if test="${empty travelerone.rebatesMoney.amount }">-</c:if>
                                            </span>
                                                <input type="hidden" name="rebatesMoney"
                                                       value="${travelerone.rebatesMoney.amount }"/>
                                                <select name="rebatesCurrency" style="display:none">
                                                    <option value="${travelerone.rebatesMoney.currencyId }"></option>
                                                </select>
                                            </div>
                                        </c:if>
                                    </div>
                                    <div class="yd-line"></div>
                                    <!--S--C109--优惠额度、优惠金额部分-->
                                    <div class="clearfix">
                                        <div class="traveler-rebatesDiv-discount-limit">
                                            <label class="ydLable2 ydColor1">产品优惠额度：</label>
                                                ${travelerone.traveler.srcPriceCurrency.currencyMark }&nbsp;${travelerone.traveler.orgDiscountPrice }
                                            <input type="hidden" name="orgDiscountPrice" value="${travelerone.traveler.orgDiscountPrice }">
                                        </div>
                                        <div class="traveler-rebatesDiv-discount-limit">
                                            <label class="ydLable2 ydColor1">优惠金额：</label>
                                            <input class="discount-limit" name="discount"
                                                   <c:if test='${not travelerone.traveler.isAllowModifyDiscount}'>disabled="disabled"</c:if>
                                                   maxlength="9" type="text"
                                                   value="${travelerone.traveler.fixedDiscountPrice }"
                                                   onblur="changeInnerDiscount(this)" onafterpaste="changeInnerDiscount(this)">
                                            <input type="hidden" name="disCurrencyMark" value="${travelerone.traveler.srcPriceCurrency.currencyMark }">
                                            <input type="hidden" name="disCurrencyName" value="${travelerone.traveler.srcPriceCurrency.currencyName }">
                                            <input type="hidden" name="disCurrencyId" value="${travelerone.traveler.srcPriceCurrency.id }">
                                            <input type="hidden" name="reviewedDiscount" value="${travelerone.traveler.reviewedDiscountPrice }">
                                            <input type="hidden" name="fixedDiscount" value="${travelerone.traveler.fixedDiscountPrice }">
                                            <input type="hidden" name="org_fixedDiscount" value="${travelerone.traveler.fixedDiscountPrice }">
                                        </div>
                                    </div>
                                    <div class="yd-line"></div>
									<div class="clearfix">
                                    	
                                    	<c:set var="otherCostFlag" value="0"></c:set>
                                    
                                        <c:if test="${not empty travelerone.costChangeList and fn:length(travelerone.costChangeList) > 0}">
                                        	<c:set var="otherCostFlag" value="1"></c:set>
                                        </c:if>
                                        
                                        <c:if test="${otherCostFlag eq 0}">
                                        	 <shiro:hasPermission name="${shiroType }:order:otherExpenses">
                                        	<a name="addcost" class="btn-addBlue">添加其他费用</a>
                                        	</shiro:hasPermission>
                                        </c:if>
                                        <c:if test="${otherCostFlag eq 1}">
                                         <shiro:hasPermission name="${shiroType }:order:otherExpenses">
                                        	<a name="addcost" class="btn-addBlue">编辑其他费用</a>
                                        	</shiro:hasPermission>
                                        </c:if>
                                        

                                        <div class="payfor-otherDiv" <c:if test="${otherCostFlag eq 0}">style="display: none"</c:if>>
											<table width=100% class="table_fixed border_t">
												<thead class="thead_border_bottom">
													<tr>
														<td width=40% class="tl">
															<div style="margin:0 5px 0 10px;word-break: break-all;">费用名称</div>
														</td>
														<td width=15% class="tl">
															<div style="margin:0 5px;word-break: break-all;">数量</div>
														</td>
														<td width=20% class="tr">
															<div style="margin:0 5px;word-break: break-all;">单价</div>
														</td>
														<td width=25% class="tr">
															<div style="margin:0 10px;word-break: break-all;">总计</div>
														</td>
													</tr>
												</thead>
												<tbody>
													<c:set var="costNames" value=""></c:set>
													<c:set var="costCurrencyIds" value=""></c:set>
													<c:set var="costNums" value=""></c:set>
													<c:set var="costPrices" value=""></c:set>
													<c:set var="costTotals" value=""></c:set>
													<c:set var="businessTypes" value=""></c:set>
													<c:forEach items="${travelerone.costChangeList}" var="costChange" varStatus="status">
														<tr businessType="0">
															<td class="tl">
																<div style="margin:0 5px 0 10px;word-break: break-all;">${costChange.costName}</div>
															</td>
															<td class="tl">
																<div style="margin:0 5px;word-break: break-all;">${costChange.costNum}</div>
															</td>
															<c:set var="currencyMark" value=""></c:set>
															<c:forEach items="${currencyList}" var="currency">
																<c:if test="${costChange.priceCurrency.id==currency.id}">
																	<c:set var="currencyMark" value="${currency.currencyMark}"></c:set>
																</c:if>
															</c:forEach>
															<td class="tr">
																<div style="margin:0 5px;word-break: break-all;">${currencyMark}${costChange.costPrice}</div>
															</td>
															<td class="tr">
																<div style="margin:0 10px;word-break: break-all;">${currencyMark}${costChange.costSum}</div>
															</td>
															
															<c:choose>
																<c:when test="${not status.first}">
																	<c:set var="costNames" value="${costNames};:;${costChange.costName}"></c:set>
																	<c:set var="costCurrencyIds" value="${costCurrencyIds};:;${costChange.priceCurrency.id}"></c:set>
																	<c:set var="costNums" value="${costNums};:;${costChange.costNum}"></c:set>
																	<c:set var="costPrices" value="${costPrices};:;${costChange.costPrice}"></c:set>
																	<c:set var="costTotals" value="${costTotals};:;${costChange.costSum}"></c:set>
																	<c:set var="businessTypes" value="${businessTypes};:;${costChange.businessType}"></c:set>
																</c:when>
																<c:otherwise>
																	<c:set var="costNames" value="${costChange.costName}"></c:set>
																	<c:set var="costCurrencyIds" value="${costChange.priceCurrency.id}"></c:set>
																	<c:set var="costNums" value="${costChange.costNum}"></c:set>
																	<c:set var="costPrices" value="${costChange.costPrice}"></c:set>
																	<c:set var="costTotals" value="${costChange.costSum}"></c:set>
																	<c:set var="businessTypes" value="${costChange.businessType}"></c:set>
																</c:otherwise>
															</c:choose>
															
														</tr>
													</c:forEach>
												</tbody>
											</table>
											<input type="hidden" name="costNames" value="${costNames}">
											<input type="hidden" name="costCurrencyIds" value="${costCurrencyIds}">
											<input type="hidden" name="costNums" value="${costNums}">
											<input type="hidden" name="costPrices" value="${costPrices}">
											<input type="hidden" name="costTotals" value="${costTotals}">
											<input type="hidden" name="businessTypes" value="${businessTypes}">
										</div>
									</div>
                                	<div class="yd-line"></div>
                                    <div class="clearfix">
                                        <div class="traveler-rebatesDiv-discount-limit">
                                            <p>
                                            	<label for="">同行价：</label>
                                            	<em><span name="settlementPrice">${travelerone.traveler.srcPriceCurrency.currencyMark}${travelerone.traveler.srcPrice }</span></em>
                                            </p>
                                            <p class="ydLable2">
                                                <label>单房差小计：</label>${singleDiffCurrencyMark}
                                                <span name="singleDiffSubtotal"><em>${travelerone.traveler.singleDiff * travelerone.traveler.singleDiffNight}</em></span>
                                            </p>
                                            <p class="ydLable2">
                                            	<label for="">同行结算价：</label>
                                            	<em><span name="settlementClearPrice">${travelerone.traveler.srcPriceCurrency.currencyMark}${travelerone.settleClearPrice }</span></em>
                                            	<em>（优惠减<span name="totalDiscount">${travelerone.totalDiscountPrice }</span>）</em>
                                            </p>
                                            <p class="ydLable2">
                                            	<label for="">其他费用总计：</label>
                                            	<em><span name="totalOtherCostPrice">${travelerone.traveler.srcPriceCurrency.currencyMark}0.00</span></em>
                                            </p>
                                            <input type="hidden" name="srcPrice" class="traveler" value="${travelerone.traveler.srcPrice}">
                                            <input type="hidden" name="srcPriceCurrency" class="traveler" value="${travelerone.traveler.srcPriceCurrency.id}">
                                            <input type="hidden" name="srcPriceCurrencyMark" class="traveler" value="${travelerone.traveler.srcPriceCurrency.currencyMark}">
                                            <input type="hidden" name="srcPriceCurrencyName" class="traveler" value="${travelerone.traveler.srcPriceCurrency.currencyName}">
                                            <input type="hidden" name="jsPrice" class="traveler" value="">
                                            <input type="hidden" name="jsPriceJson" class="traveler" value='${travelerone.costPriceJson}'>
                                        </div>
                                    </div>
                                    <div class="yd-line"></div>
                                    <!--E--C109--费用展示部分-->
                                    <div class="yd-line"></div>
                                    <div class="yd-total clearfix">
                                    	<!--  
                                        <a name="bjyyqb" class="ydbz_x" onclick="useAllPrice(this)">报价应用全部</a>
                                        -->
                                    </div>
                                    <div class="yd-total clearfix">
                                        <div class="traveler-rebatesDiv-discount-limit">
                                            <p class="ydLable2 total">
                                                <label for="">结算价总计：</label>
                                                <em><span name="clearPrice" class="ydLable2"></span></em>
                                                <input type="hidden" name="travelerClearPrice" class="traveler" value=''>
                                                <input type="hidden" name="travelerClearPriceJson" class="traveler" value='${travelerone.payPriceJson}'>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <!--游客信息右侧开始-->
                            <div class="tourist-right">
                                <div class="bj-info">
                                    <div class="ydbz_tit ydbz_tit_child font_16">报价
                                    	<!--  
										<a name="bjyyqb" class="ydbz_x float_right margin_tb" onclick="useAllPrice(this)">报价应用全部</a>
										-->
                                    </div>
                                    <div class="yd-line"></div>

                                    <div class="clearfix">
                                        <ul class="tourist-info2">
                                            <li class="tourist-info2-first">
                                                <label class="ydLable2 ydColor1">住房要求：</label>
                                                <select class="selZF" name="hotelDemand">
                                                    <option value="2"
                                                            <c:if test="${travelerone.traveler.hotelDemand=='2'}">selected="selected"</c:if>>
                                                        	双人间
                                                    </option>
                                                    <option value="1"
                                                            <c:if test="${travelerone.traveler.hotelDemand=='1'}">selected="selected"</c:if>>
                                                        	单人间
                                                    </option>
                                                </select>
                                            </li>
                                            <li>
                                            	<label>单人房差：</label>${singleDiffCurrencyMark}
                                            	<span>
	                          						<c:if test="${travelerone.traveler.hotelDemand=='2'}">0
	                          							</span><input type="hidden" name="singleDiff" class="traveler" value="0">
	                          						</c:if>
                                                	<c:if test="${travelerone.traveler.hotelDemand=='1'}">${travelerone.traveler.singleDiff}</span>
                                                	<input type="hidden" name="singleDiff" class="traveler"
                                                       value="${travelerone.traveler.singleDiff}"></c:if>/间 x<input
                                                        type="text" class="ipt4" name="sumNight" maxlength="3"
                                                        value="${travelerone.traveler.singleDiffNight}"
                                                        onkeyup="changeSumNight(this)"
                                                        onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')">晚
                                            </li>
                                            <li><label class="ydLable2">单房差小计：</label>${singleDiffCurrencyMark}<span
                                                    class="ydFont1">${travelerone.traveler.singleDiff * travelerone.traveler.singleDiffNight}</span>
                                            </li>
                                        </ul>
                                    </div>
                                    <div class="yd-line"></div>

                                    <div class="clearfix">
                                        <!-- 修改订单 -->
                                        <c:if test="${not empty update}">
                                            <div class="clearfix">
                                                <div class="traveler-rebatesDiv">
                                                		<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
												             <c:choose>
													         	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
																	<label class="ydLable2 ydColor1">预计个人宣传费：</label>
																</c:when>
													            <c:otherwise>
													                 <label class="ydLable2 ydColor1">预计个人返佣：</label>
													             </c:otherwise>
												         </c:choose>   
                                                    <select name="rebatesCurrency">
                                                        <c:if test="${not empty currencyList }">
                                                            <c:forEach items="${currencyList}" var="curren">
                                                                <option value="${curren.id }"
                                                                        <c:if test="${not empty fns:getScheduleCurrencyID(travelerone.traveler.rebatesMoneySerialNum,curren.id) }">selected="selected"</c:if>>${curren.currencyName }</option>
                                                            </c:forEach>
                                                        </c:if>
                                                    </select>
                                                    <input type="text"
                                                           value="${fns:getMoneyAmountNUM(travelerone.traveler.rebatesMoneySerialNum) }"
                                                           class="ipt-rebates" name="rebatesMoney" maxlength="9"
                                                           onafterpaste="checkRebatesValue(this)"
                                                           onkeyup="checkRebatesValue(this)"/>
                                                </div>
                                            </div>
                                        </c:if>
                                        <!-- 订单详情 -->
                                        <c:if test="${empty update}">
                                            <div class="traveler-rebatesDiv">
                                            		<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
										             <c:choose>
											         	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
															<label class="ydLable2 ydColor1">预计个人宣传费：</label>
														</c:when>
											            <c:otherwise>
											                  <label class="ydLable2 ydColor1">预计个人返佣：</label>
											             </c:otherwise>
										         </c:choose>   
									<span>
										${travelerone.rebatesCurrency.currencyMark}
										<c:if test="${not empty travelerone.rebatesMoney.amount }">${travelerone.rebatesMoney.amount }</c:if>
										<c:if test="${empty travelerone.rebatesMoney.amount }">-</c:if>
									</span>
                                                <input type="hidden" name="rebatesMoney"
                                                       value="${travelerone.rebatesMoney.amount }"/>
                                                <select name="rebatesCurrency" style="display:none">
                                                    <option value="${travelerone.rebatesMoney.currencyId }"></option>
                                                </select>
                                            </div>
                                        </c:if>

                                    </div>
                                    <div class="yd-line"></div>

                                    <div class="clearfix">
                                    	
                                    	<c:set var="otherCostFlag" value="0"></c:set>
                                    
                                        <c:if test="${not empty travelerone.costChangeList and fn:length(travelerone.costChangeList) > 0}">
                                        	<c:set var="otherCostFlag" value="1"></c:set>
                                        </c:if>
                                        
                                        <c:if test="${otherCostFlag eq 0}">
                                         <shiro:hasPermission name="${shiroType }:order:otherExpenses">
                                        	<a name="addcost" class="btn-addBlue">添加其他费用</a>
                                        	</shiro:hasPermission>
                                        </c:if>
                                        <c:if test="${otherCostFlag eq 1}">
                                         <shiro:hasPermission name="${shiroType }:order:otherExpenses">
                                        	<a name="addcost" class="btn-addBlue">编辑其他费用</a>
                                        	</shiro:hasPermission>
                                        </c:if>
                                        

                                        <div class="payfor-otherDiv" <c:if test="${otherCostFlag eq 0}">style="display: none"</c:if>>
											<table width=100% class="table_fixed border_t">
												<thead class="thead_border_bottom">
													<tr>
														<td width=40% class="tl">
															<div style="margin:0 5px 0 10px;word-break: break-all;">费用名称</div>
														</td>
														<td width=15% class="tl">
															<div style="margin:0 5px;word-break: break-all;">数量</div>
														</td>
														<td width=20% class="tr">
															<div style="margin:0 5px;word-break: break-all;">单价</div>
														</td>
														<td width=25% class="tr">
															<div style="margin:0 10px;word-break: break-all;">总计</div>
														</td>
													</tr>
												</thead>
												<tbody>
													<c:set var="costNames" value=""></c:set>
													<c:set var="costCurrencyIds" value=""></c:set>
													<c:set var="costNums" value=""></c:set>
													<c:set var="costPrices" value=""></c:set>
													<c:set var="costTotals" value=""></c:set>
													<c:set var="businessTypes" value=""></c:set>
													<c:forEach items="${travelerone.costChangeList}" var="costChange" varStatus="status">
														<tr businessType="0">
															<td class="tl">
																<div style="margin:0 5px 0 10px;word-break: break-all;">${costChange.costName}</div>
															</td>
															<td class="tl">
																<div style="margin:0 5px;word-break: break-all;">${costChange.costNum}</div>
															</td>
															<c:set var="currencyMark" value=""></c:set>
															<c:forEach items="${currencyList}" var="currency">
																<c:if test="${costChange.priceCurrency.id==currency.id}">
																	<c:set var="currencyMark" value="${currency.currencyMark}"></c:set>
																</c:if>
															</c:forEach>
															<td class="tr">
																<div style="margin:0 5px;word-break: break-all;">${currencyMark}${costChange.costPrice}</div>
															</td>
															<td class="tr">
																<div style="margin:0 10px;word-break: break-all;">${currencyMark}${costChange.costSum}</div>
															</td>
															
															<c:choose>
																<c:when test="${not status.first}">
																	<c:set var="costNames" value="${costNames};:;${costChange.costName}"></c:set>
																	<c:set var="costCurrencyIds" value="${costCurrencyIds};:;${costChange.priceCurrency.id}"></c:set>
																	<c:set var="costNums" value="${costNums};:;${costChange.costNum}"></c:set>
																	<c:set var="costPrices" value="${costPrices};:;${costChange.costPrice}"></c:set>
																	<c:set var="costTotals" value="${costTotals};:;${costChange.costSum}"></c:set>
																	<c:set var="businessTypes" value="${businessTypes};:;${costChange.businessType}"></c:set>
																</c:when>
																<c:otherwise>
																	<c:set var="costNames" value="${costChange.costName}"></c:set>
																	<c:set var="costCurrencyIds" value="${costChange.priceCurrency.id}"></c:set>
																	<c:set var="costNums" value="${costChange.costNum}"></c:set>
																	<c:set var="costPrices" value="${costChange.costPrice}"></c:set>
																	<c:set var="costTotals" value="${costChange.costSum}"></c:set>
																	<c:set var="businessTypes" value="${costChange.businessType}"></c:set>
																</c:otherwise>
															</c:choose>
															
														</tr>
													</c:forEach>
												</tbody>
											</table>
											<input type="hidden" name="costNames" value="${costNames}">
											<input type="hidden" name="costCurrencyIds" value="${costCurrencyIds}">
											<input type="hidden" name="costNums" value="${costNums}">
											<input type="hidden" name="costPrices" value="${costPrices}">
											<input type="hidden" name="costTotals" value="${costTotals}">
											<input type="hidden" name="businessTypes" value="${businessTypes}">
										</div>
									</div>
                                </div>
                                <div class="yd-line"></div>

                                <div class="yd-total clearfix">
                                    <div class="fr" style="white-space:nowrap;<c:if test="${priceType eq 2}">display: none</c:if>">
                                        <label class="ydLable2">
                                        	<c:if test="${priceType eq 2}">QUAUQ价</c:if><c:if test="${priceType eq 1}">直客价</c:if><c:if test="${priceType ne 1 and priceType ne 2 }">同行价</c:if>：
                                        </label>
                                        <span name="innerJsPrice" class="ydFont2"></span>
                                        <input type="hidden" name="srcPrice" class="traveler" value="${travelerone.traveler.srcPrice}">
                                        <input type="hidden" name="srcPriceCurrency" class="traveler" value="${travelerone.traveler.srcPriceCurrency.id}">
                                        <input type="hidden" name="jsPrice" class="traveler" value="">
                                        <input type="hidden" name="jsPriceJson" class="traveler" value='${travelerone.costPriceJson}'>
                                    </div>

                                    <!--299 v2 -start-->
                                    <shiro:hasPermission name="price:project">
                                        <c:if test="${productorder.orderStatus ne '10' and productorder.orderStatus ne '6' and productorder.orderStatus ne '7'}">
                                            <div style="clear:both">
                                                <c:forEach var="hotelhouse" items="${productGroup.hotelHouseList }" varStatus="status">
                                                    <div>
                                                        <label>(</label>
                                                        <label>酒店：</label>
                                                        <span class="">${hotelhouse.hotel }</span>&nbsp;
                                                        <label>房型：</label>
                                                        <span class="">${hotelhouse.houseType }</span>
                                                        <label>)</label>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </c:if>
                                    </shiro:hasPermission>
                                    <!--299 v2 -end-->

                                    <div class="fr" style="white-space:nowrap">
                                        <label class="ydLable2">结算价：</label><span name="clearPrice" class="ydFont2"></span>
                                        <input type="hidden" name="travelerClearPrice" class="traveler" value=''>
                                        <input type="hidden" name="travelerClearPriceJson" class="traveler" value='${travelerone.payPriceJson}'>
                                    </div>
                                </div>
                                <!--299-start-->
                                <shiro:hasPermission name="price:project">
                                <c:if test="${productorder.orderStatus ne 10 }">
	                                <div class="yd-total clearfix">
	                                    <a name="jgfa" class="ydbz_x" onclick="pricePlanPop(this)">查看价格方案</a>
	                                </div>
	                                <div>
	                                    <span>价格备注：</span>
	                                    <textarea rows="4" cols="10" style="width: 160px" name="priceRemark" onafterpaste="checkMaxLength(this, 50)" onKeyUp="checkMaxLength(this, 50)">${travelerone.traveler.priceRemark }</textarea>
	                                </div>
                                </c:if>
                                </shiro:hasPermission>
                                <!--299-end-->
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <!--保存按钮开始-->
            <div class="rightBtn">
                <input type="button" name="saveBtn" class="btn btn-primary"
                       onclick="saveTraveler(this,this.form,${productorder.orderStatus})" value="保存">
                <input type="button" name="editBtn" class="btn btn-primary"
                       onclick="saveTravelerAfter(this,this.form,'edit')" style="display:none" value="修改">
            </div>
            <!--保存按钮结束-->
        </div>
        
		<!-- T2报名 添加其他费用弹窗 START-->
		<div class="add_other_cost" style="display: none;">
			<div style="margin:10px"class="add_other_cost">
				<table class="table activitylist_bodyer_table">
					<thead>
						<tr>
							<th width="10%">费用名称</th>
							<th width="5%">币种</th>
							<th width="8%">数量</th>
							<th width="8%">单价</th>
							<th width="8%">总计</th>
							<th width="8%">操作</th>
						</tr>
					</thead>
					<tbody>
						<tr businessType="0">
							<td class="tc">
								<input type="text" maxlength="50" value="">
							</td>
							<td class="tc">
								<select name="currency" onchange="changeOtherCostCurrency(this)">
									${currencyOptions}
								</select>
							</td>
							<td class="tc">
								<input class="dataChange" name="num" type="text" maxlength="14" value="1"
										onblur="changeOtherCostNum(this)" onpaste="changeOtherCostNum(this)" onkeyup="changeOtherCostNum(this)">
							</td>
							<td class="tc">
								<input class="dataChange"  name="price" type="text" maxlength="14" value=""
										onblur="changeOtherCostPrice(this)" onpaste="changeOtherCostPrice(this)" onkeyup="changeOtherCostPrice(this)">
							</td>
							<td class="tc" name="result">
								<em class="currency"></em><em class="result">0</em>
							</td>
							<td class="tc">
								<a href="javascript:void(0)" onclick="addone(this)">增加</a>
								<a href="javascript:void(0)" onclick="deletethis(this)">删除</a>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<!-- T2报名 添加其他费用弹窗 END -->
		
    </form>
</c:forEach>
	<shiro:hasPermission name="price:project">
		<div class="pricePlan_container" style="display: none">
            <table id="pricePlanTable" name="pricePlanTable" class="table activitylist_bodyer_table border-table-spread" style="margin: 0 auto">
                <thead>
                    <tr>
                    	<th rowspan="2" class="tc" style="width: 50px">
                           	 序号
                        </th>
                        <th rowspan="2" class="tc" style="width: 500px">
                            	价格方案
                        </th>
                        <th colspan="3" class="tc t-th2">同行价</th>
                        <c:if test="${productorder.orderStatus eq 2 }">
                        	<th colspan="3" class="tc t-th2">直客价</th>
                        </c:if>
                        <th rowspan="2" class="tc">备注</th>
                    </tr>
                    <tr>
                        <th class="tc">成人</th>
                        <th class="tc">儿童</th>
                        <th class="tc" style="border-right: 0px">特殊人群</th>
                        <c:if test="${productorder.orderStatus eq 2 }">
	                        <th class="tc">成人</th>
	                        <th class="tc">儿童</th>
	                        <th class="tc" style="border-right: 0px">特殊人群</th>
                        </c:if>
                    </tr>
                </thead>
                <tbody>
                
                </tbody>
            </table>
        </div>
	</shiro:hasPermission>