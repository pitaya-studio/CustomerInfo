<%@ page contentType="text/html;charset=UTF-8" %>
<!-- 游客模板 -->
<!-- 针对C147andC109 奢华和优加：优惠，和申请办签信息的展示     控制开关 -->
<c:if test="${companyUuid == '75895555346a4db9a96ba9237eae96a5' or companyUuid == '7a81c5d777a811e5bc1e000c29cf2586'}">
    <input type="hidden" name="isForYouJia" value="true">
    <c:set var="isForYouJia" value="true"/>
</c:if>
<c:if test="${!(companyUuid == '75895555346a4db9a96ba9237eae96a5' or companyUuid == '7a81c5d777a811e5bc1e000c29cf2586')}">
    <input type="hidden" name="isForYouJia" value="false">
    <c:set var="isForYouJia" value="false"/>
</c:if>
<!-- 针对C109 奢华和优加：是否是散拼产品     控制开关 -->
<c:if test="${activityKind == '2'}">
    <c:set var="isLoose" value="true"/>
</c:if>
<c:if test="${activityKind != '2'}">
    <c:set var="isLoose" value="false"/>
</c:if>

<div id="travelerTemplate" style="display: none;">
    <form name="travelerForm" class="form-search">
        <input type="hidden" name="travelerOrderId" value="${productorder.id}">
        <input type="hidden" value="${singleDiffCurrencyId}" name="singleDiffCurrencyId">
        <input type="hidden" name="travelerId">
        <div class="tourist">
            <div class="tourist-t">
                <a class="btn-del" style="cursor:pointer;" name="deleteTraveler">删除</a>
                <span class="add_seachcheck" onclick="travelerBoxCloseOnAdd(this)"></span>
                <label class="ydLable">游客<em class="travelerIndex" style="vertical-align: baseline;"></em>:</label>
                <div class="tourist-t-off">
                <span class="fr">
                	<b style="font-size:18px"><span name="labelSPGPrice"></span>：</b><span name="jsPrice" class="ydFont2"></span> 
                	<b style="font-size:18px">结算价：</b><span name="travelerClearPrice" class="ydFont2"></span>
                </span>
                    <span name="tName"></span>
                </div>
                <div class="tourist-t-on">
                    <c:choose>
                        <c:when test="${activityKind == '10'}">
                            <label><input type="radio" class="traveler" name="personType" value="1"/>1/2<span name="labelSPG"></span></label>
                            <label><input type="radio" class="traveler" name="personType" value="2"/>3/4<span name="labelSPG"></span></label>
                        </c:when>
                        <c:otherwise>
                            <label><input type="radio" class="traveler" name="personType" value="1"/>成人</label>
                            <label><input type="radio" class="traveler" name="personType" value="2"/>儿童</label>
                            <label><input type="radio" class="traveler" name="personType" value="3"/>特殊人群</label>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${fn:length(intermodalList) > 0}">
                        <div class="tourist-t-r">
                            是否联运：
                            <label>
                                <input type="radio" name="travelerIntermodalType" value="0" checked="checked" onclick="isIntermodal(this)"/>
                                不需要
                            </label>
                            <label>
                                <input type="radio" name="travelerIntermodalType" value="1" onclick="isIntermodal(this)"/>
                                需要
                            </label>
                        <span>
                            <select onchange="setIntermodal($(this))" name="intermodalStrategy">
                                <c:forEach items="${intermodalList}" var="intermodal">
                                    <option value="${intermodal.price}" intermodalId="${intermodal.id}"
                                            priceCurrencyId="${intermodal.priceCurrency.id}"
                                            priceCurrency="${intermodal.priceCurrency.currencyName}">
                                            ${intermodal.groupPart}
                                    </option>
                               </c:forEach>
                            </select>
                            币种：<label name="priceCurrency"></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            联运价格：<label name="intermodalPrice"></label>
                            <input type="hidden" name="intermodalId"/>
                        </span>
                        </div>
                    </c:if>
                </div>
            </div>
            <div flag="messageDiv">
                <div class="tourist-con">
                    <!-- 如果不为奢华和优加 -->
                    <c:if test="${!isForYouJia}">
                        <div class="tourist-left">
                            <!-- 基本信息 -->
                            <div class="ydbz_tit ydbz_tit_child"><em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>基本信息</div>
                            <ul class="tourist-info1 clearfix" flag="messageDiv">
                                <li>
                                    <label class="ydLable"><span class="xing">*</span>姓名：</label>
                                    <input type="text" maxlength="25" name="travelerName" class="traveler required"
                                           onkeyup="this.value=this.value.replaceSpecialChars()"
                                           onafterpaste="this.value=this.value.replaceSpecialChars()">
                                </li>
                                <li>
                                    <label class="ydLable">英文／拼音：</label>
                                    <input type="text" maxlength="100" name="travelerPinyin" class="traveler">
                                </li>
                                <li>
                                    <label class="ydLable">性别：</label>
                                    <select name="sex" class="selSex">
                                        <option value="1" selected="selected">男</option>
                                        <option value="2">女</option>
                                    </select>
                                </li>
                                
                                <li>
                                    <label class="ydLable">出生日期：</label>
                                    <input type="text" name="birthDay" class="traveler traveler2 dateinput">
                                </li>
                                <c:if test="${fns:getUser().company.uuid eq '980e4c74b7684136afd89df7f89b2bee'}">
	                                <li>
	                                    <label class="ydLable">出生地：</label>
	                                    <input type="text" name="hometown" class="traveler" maxlength="30"
	                                           onblur="this.value=this.value.replace(/[^\u4e00-\u9fa5]+/g,'')"
	                                           onafterpaste="this.value=this.value.replace(/[^\u4e00-\u9fa5]+/g,'')">
	                                </li>
                                </c:if>
                                <li>
                                    <label class="ydLable">护照号：</label>
                                    <input type="text" name="passportCode" class="traveler" maxlength="30"
                                           onkeyup="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')"
                                           onafterpaste="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')">
                                </li>
                               <c:if test="${fns:getUser().company.uuid eq '980e4c74b7684136afd89df7f89b2bee'}">  
                                <li>
                                    <label class="ydLable">签发地：</label>
                                    <input type="text" name="issuePlace1" class="traveler" maxlength="10"
                                           onblur="this.value=this.value.replace(/[^\u4e00-\u9fa5]+/g,'')"
                                           onafterpaste="this.value=this.value.replace(/[^\u4e00-\u9fa5]+/g,'')">
                                </li>
                                 <li>
                                    <label class="ydLable">签发日期：</label>
                                    <input type="text" name="issueDate" class="traveler traveler2 dateinput" onclick="WdatePicker()">
                                </li>
                               </c:if>
                                <li>
                                    <label class="ydLable">护照有效期：</label>
                                    <input type="text" name="passportValidity" class="traveler traveler2 dateinput">
                                </li>
                                 <li>
                                    <label class="ydLable"><span class="xing"></span>联系电话：</label>
                                    <input type="text" name="telephone" class="traveler" maxlength="50"
                                           onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"
                                           onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')">
                                </li>
                                <li>
                                    <label class="ydLable">国籍：</label>
                                    <select name="nationality" class="selCountry">
                                        <c:forEach items="${countryList}" var="country">
                                            <option value="${country.id}"
                                                    <c:if test="${country.id==461}">selected="selected"</c:if>>
                                                    ${country.countryName_cn}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </li> 
                                <li>
                                    <label class="ydLable">身份证号：</label>
                                    <input type="text" name="idCard" style="width:128px;padding-left:1px;padding-right:1px;"
                                           class="traveler" maxlength="18"
                                           onkeyup="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')"
                                           onafterpaste="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')" onblur="getBirthday(this);">
                                </li>
                                <li>
                                    <label class="ydLable"><span class="xing"></span>护照类型：</label>
                                    <select name="passportType" class="selCountry">
                                        <c:forEach items="${passportTypeList}" var="passportType">
                                            <option value="${passportType.key}">${passportType.value}</option>
                                        </c:forEach>
                                    </select>
                                </li>
                                <li>
                                    <label class="ydLable">发证日期：</label>
                                    <input type="text" name="issuePlace" class="traveler traveler2 dateinput">
                                </li>
                            </ul>
                            <!-- 签证信息 -->
                            <div class="ydbz_tit ydbz_tit_child"><em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>签证信息</div>
                            <ul flag="messageDiv" class="zbqinfo">
                                <div class="ydbz_tit ydbz_tit_child">自备签
                                    <p class="tourist-ckb">
                                        <label class="ydLable">自备签国家：</label>
                                        <c:forEach items="${targetForeignCountry}" var="tfc" varStatus="s">
                                            <input type="checkbox" class="traveler" value="${tfc.applyCountryName}" name="zibeiqian"><label
                                                class="ckb-txt">自备${tfc.applyCountryName}签证</label>
                                        </c:forEach>
                                    </p>
                                </div>
                                <div class="zjlx-tips  zibei-tips" style="display: none;">
                                    <i class="arrow1">&nbsp;</i>
                                    <c:forEach items="${targetForeignCountry}" var="tfc">
                                        <ul class="tourist-type clearfix" style="display: none;">
                                            <li>
                                                <label class="ydLable">${tfc.applyCountryName}签证有效期：</label>
                                                <input type="text" class="dateinput" name="zbqVisaDate" onclick="WdatePicker()">
                                            </li>
                                        </ul>
                                    </c:forEach>
                                </div>
                                <div class="ydbz_tit ydbz_tit_child">申请办签</div>
                                <div class="ydbz_scleft">
                                    <table class="table-visa">
                                        <thead>
                                        <tr>
                                            <th width="15%">申请国家</th>
                                            <th width="15%">领区</th>
                                            <th width="15%">签证类别</th>
                                            <th width="10%">预计出团时间</th>
                                            <th width="15%">预计约签时间</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${targetForeignCountry}" var="tfc">
                                            <tr class="${tfc.applyCountryName}" name="visainfo">
                                                <td class="tl">${tfc.applyCountryName}
                                                    <input type="hidden" value="${tfc.applyCountryId}" name="visaCountryId">
                                                    <input type="hidden" value="${tfc.sysCountryId}" name="sysCountryId">
                                                </td>
                                                <td>
                                                    <%-- <select name="manor" onchange="changeManor(this)"> --%>
                                                    <select name="manor">
                                                        <c:forEach items="${tfc.manorList}" var="manor">
                                                            <option value="${manor.value}">${manor.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                                <td>
                                                    <select name="visaType">
                                                        <c:forEach items="${tfc.visaTypeList}" var="visaType">
                                                            <option value="${visaType.value}">${visaType.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                                <td><fmt:formatDate value="${productGroup.groupOpenDate}" pattern="yyyy-MM-dd"/></td>
                                                <td><input type="text" onclick="WdatePicker()" name="contractDate" class="inputTxt dateinput"></td>
                                            </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="ydbz_tit ydbz_tit_child">上传资料</div>
                                <ul class="ydbz_2uploadfile ydbz_scleft certificate">
                                    <li class="seach25 seach33"><p>护照首页：</p><input name="passportfile" type="text" readonly="readonly">
                                        <input type="button" name="passport" class="btn btn-primary" value="上传"
                                               onclick="uploadFiles('${ctx}','passportfile',this,'true');"/>
                                        <span class="fileLogo"></span>
                                    </li>
                                    <li class="seach25 seach33"><p>身份证正面：</p><input name="idcardfrontfile" type="text" readonly="readonly">
                                        <input type="button" name="idcardfront" class="btn btn-primary" value="上传"
                                               onclick="uploadFiles('${ctx}','idcardfrontfile',this,'true');">
                                        <span class="fileLogo"></span></li>
                                    <li class="seach25 seach33"><p>申请表格：</p><input name="entryformfile" type="text" readonly="readonly">
                                        <input type="button" name="entryform" class="btn btn-primary" value="上传"
                                               onclick="uploadFiles('${ctx}','entryformfile',this,'true');">
                                        <span class="fileLogo"></span>
                                    </li>
                                    <p class="kong"></p>
                                    <li class="seach25 seach33"><p>房产证：</p><input name="housefile" type="text" readonly="readonly">
                                        <input type="button" name="house" class="btn btn-primary" value="上传"
                                               onclick="uploadFiles('${ctx}','housefile',this,'true');">
                                        <span class="fileLogo"></span>
                                    </li>
                                    
                                    <li class="seach25 seach33"><p>电子照片：</p><input name="photofile" type="text" readonly="readonly">
                                        <input type="button" name="photo" class="btn btn-primary" value="上传"
                                               onclick="uploadFiles('${ctx}','photofile',this,'true');">
                                        <span class="fileLogo"></span>
                                    </li>
                                    <li class="seach25 seach33"><p>身份证反面：</p><input name="idcardbackfile" type="text" readonly="readonly">
                                        <input type="button" name="idcardback" class="btn btn-primary" value="上传"
                                               onclick="uploadFiles('${ctx}','idcardbackfile',this,'true');">
                                        <span class="fileLogo"></span>
                                    </li>
                                    <p class="kong"></p>
                                    <li class="seach25 seach33"><p>户口本：</p><input name="residencefile" type="text" readonly="readonly">
                                        <input type="button" name="residence" class="btn btn-primary" value="上传"
                                               onclick="uploadFiles('${ctx}','residencefile',this,'true');">
                                        <span class="fileLogo"></span>
                                    </li>
                                    <li class="seach25 seach33"><p>其　它：</p><input name="otherfile" type="text" readonly="readonly">
                                        <input type="button" name="other" class="btn btn-primary" value="上传"
                                               onclick="uploadFiles('${ctx}','otherfile',this,'true');">
                                        <span class="fileLogo"></span>
                                    </li>
                                    <li class="seach25 seach33"><p>签证附件：</p><input name="visaannexfile" type="text" readonly="readonly">
                                        <input type="button" name="visaannex" class="btn btn-primary" value="上传"
                                               onclick="uploadFiles('${ctx}','visaAnnexFile', this,'true');">
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
                            <div class="ydbz_tit ydbz_tit_child"><span style="display:inline-block">备注：</span>
                                <textarea name="remark" class="textarea_long" maxlength="200"></textarea>
                            </div>
                        </div>
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
                                                <option value="2">双人间</option>
                                                <option value="1">单人间</option>
                                            </select>
                                        </li>
                                        <li><label>单人房差：</label>${singleDiffCurrencyMark}<span>0</span>
                                            <input type="hidden" name="singleDiff" class="traveler" value="0">/间 x
                                            <input type="text" class="ipt4" name="sumNight" maxlength="3" onkeyup="changeSumNight(this)"
                                                   onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')">晚
                                        </li>
                                        <li><label class="ydLable2">单房差小计：</label>${singleDiffCurrencyMark}<span class="ydFont1"></span></li>
                                    </ul>
                                </div>
                                <div class="yd-line"></div>

                                <div class="clearfix">
                                    <div class="traveler-rebatesDiv">
                                    <!-- 265需求，针对鼎鸿假期，将所有返佣字段改为宣传费 -->
                                    <c:choose>
                                    	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
											<label class="ydLable2 ydColor1">预计个人宣传费：</label>
										</c:when>
                                    	<c:otherwise>
                                        <label class="ydLable2 ydColor1">预计个人返佣：</label>
                                        </c:otherwise>
                                     </c:choose>   
                                        <select name="rebatesCurrency">
                                            ${currencyOptions}
                                        </select>
                                        <input type="text" class="ipt-rebates" name="rebatesMoney" maxlength="9" onafterpaste="checkRebatesValue(this)"
                                               onkeyup="checkRebatesValue(this)"/>
                                    </div>
                                </div>

                                <div class="yd-line"></div>
                                 <shiro:hasPermission name="${shiroType }:order:otherExpenses">
                                <div class="clearfix">
                                    <a name="addcost" class="btn-addBlue">添加其他费用</a>
                                    <div class="payfor-otherDiv" style="display: none">
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
	                                    	</tbody>
	                                    </table>
	                                    <input type="hidden" name="costNames" value="">
	                                    <input type="hidden" name="costCurrencyIds" value="">
	                                    <input type="hidden" name="costNums" value="">
	                                    <input type="hidden" name="costPrices" value="">
	                                    <input type="hidden" name="costTotals" value="">
	                                    <input type="hidden" name="businessTypes" value="">
                                   </div>
                                </div>
                                </shiro:hasPermission>
                            </div>
                            <div class="yd-line"></div>
                            <div class="yd-total clearfix">
                                <div class="fr">
                                    <label class="ydLable2"><span name="labelSPGPrice"></span>：</label><span name="innerJsPrice" class="ydFont2"></span>
                                    <input type="hidden" name="srcPrice" class="traveler">
                                    <input type="hidden" name="srcPriceCurrency" class="traveler">
                                    <input type="hidden" name="jsPrice" class="traveler">
                                </div>

                                <shiro:hasPermission name="price:project">
                                    <c:if test="${activityKind ne '10' and activityKind ne '6' and activityKind ne '7'}">
                                        <div style="clear:both">
                                            <c:forEach var="hotelhouse" items="${activityGroup.hotelHouseList }" varStatus="status">
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

                                <div class="fr">
                                    <label class="ydLable2">结算价：</label><span name="clearPrice" class="ydFont2"></span>
                                    <input type="hidden" name="travelerClearPrice" class="traveler">
                                </div>
                            </div>
                            <!--299-start-->
                            <shiro:hasPermission name="price:project">
                            <c:if test="${priceJson != '' and activityKind != 10}">
                                <div class="yd-total clearfix">
                                    <a name="bjyyqb" class="ydbz_x" onclick="pricePlanPop(this)">查看价格方案
                                    </a>
                                </div>
                                <div>
                                    <span>价格备注：</span>
                                    <textarea name="priceRemark" rows="4" cols="10" style="width: 160px" onblur="checkMaxLength(this, 50)"></textarea>
                                </div>
                            </c:if>
                            </shiro:hasPermission>
                            <!--299-end-->
                        </div>
                    </c:if>

                    <!-- 如果为 -->
                    <c:if test="${isForYouJia}">
                        <div class="tourist-left" >
                            <!-- 基本信息 -->
                            <div class="ydbz_tit ydbz_tit_child"><em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>基本信息</div>
                            <ul class="tourist-info1 clearfix" flag="messageDiv">
                                <li>
                                    <label class="ydLable"><span class="xing">*</span>姓名：</label>
                                    <input type="text" maxlength="25" name="travelerName" class="traveler required"
                                           onkeyup="this.value=this.value.replaceSpecialChars()" onafterpaste="this.value=this.value.replaceSpecialChars()">
                                </li>
                                <li>
                                    <label class="ydLable">英文／拼音：</label>
                                    <input type="text" maxlength="100" name="travelerPinyin" class="traveler">
                                </li>
                                <li>
                                    <label class="ydLable">性别：</label>
                                    <select name="sex" class="selSex">
                                        <option value="1" selected="selected">男</option>
                                        <option value="2">女</option>
                                    </select>
                                </li>
                                <li>
                                    <label class="ydLable">出生日期：</label>
                                    <input type="text" name="birthDay" class="traveler traveler2 dateinput">
                                </li>
                                <li>
                                    <label class="ydLable"><span class="xing"></span>联系电话：</label>
                                    <input type="text" name="telephone" class="traveler" maxlength="50"
                                           onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"
                                           onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')">
                                </li>
                                <li>
                                    <label class="ydLable"><span class="xing"></span>护照类型：</label>
                                    <select name="passportType" class="selCountry">
                                        <c:forEach items="${passportTypeList}" var="passportType">
                                            <option value="${passportType.key}">${passportType.value}</option>
                                        </c:forEach>
                                    </select>
                                </li>
                                <li>
                                    <label class="ydLable">护照号：</label>
                                    <input type="text" name="passportCode" class="traveler" maxlength="30"
                                           onkeyup="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')"
                                           onafterpaste="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')">
                                </li>
                                <li>
                                    <label class="ydLable">护照签发日期：</label>
                                    <input type="text" name="issuePlace" class="traveler traveler2 dateinput">
                                </li>
                                <li>
                                    <label class="ydLable">护照有效期：</label>
                                    <input type="text" name="passportValidity" class="traveler traveler2 dateinput">
                                </li>
                                <li>
                                    <label class="ydLable">护照签发地：</label>
                                    <input type="text" name="passportPlace" class="traveler">
                                </li>
                                <li>
                                    <label class="ydLable">身份证号：</label>
                                    <input type="text" name="idCard" style="width:128px;padding-left:1px;padding-right:1px;" class="traveler" maxlength="18"
                                           onkeyup="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')"
                                           onafterpaste="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')" onblur="getBirthday(this);">
                                </li>
                                <div class="kong"></div>
                                <li style="width: 402px;margin-left:10px;">
                                    <label class="ydLable2" style="width:120px">职务（中文/英文）：</label>
                                    <input type="text" name="positionCn" style="width:128px;padding-left:1px;padding-right:1px;" class="traveler"> /
                                    <input type="text" name="positionEn" style="width:128px;padding-left:1px;padding-right:1px;" class="traveler">
                                </li>
                            </ul>
                            <!-- 签证信息 -->
                            <div class="ydbz_tit ydbz_tit_child"><em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>签证信息</div>
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
                                        <tbody id="qztemplate">
                                        <tr class="country" name="visainfo">
                                            <td class="tl">
                                                <select name="countrySelect">
                                                    <option value="-1">请选择</option>
                                                    <c:forEach items="${countryList}" var="country">
                                                        <option value="${country.id}">${country.countryName_cn}</option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                            <td>
                                                <select name="manor">
                                                    <option value="-1">请选择</option>
                                                    <c:forEach items="${fns:getDictByType('from_area')}" var="dict">
                                                        <option value="${dict.value}">${dict.label}</option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                            <td>
                                                <select name="visaType">
                                                    <option value="-1">请选择</option>
                                                    <c:forEach items="${fns:getDictByType('new_visa_type')}" var="dict">
                                                        <option value="${dict.value}">${dict.label}</option>
                                                    </c:forEach>
                                                    <option value="0">自备签证</option>
                                                </select>
                                            </td>
                                            <td><fmt:formatDate value="${productGroup.groupOpenDate}" pattern="dd/MM/yyyy"/></td>
                                            <td>
                                                <input type="text" onclick="WdatePicker({dateFmt:'dd/MM/yyyy'})" name="contractDate" class="inputTxt dateinput">
                                            </td>
                                            <td name="adddel" id="adddel">
                                                <a class="add" href="javascript:void(0)" onclick="addQz(this)">+</a>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="ydbz_tit ydbz_tit_child">上传资料</div>
                                <ul class="ydbz_2uploadfile ydbz_scleft">
                                    <li class="seach25 seach33"><p>护照首页：</p><input name="passportfile" type="text" readonly="readonly">
                                        <input type="button" name="passport" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','passportfile',this);"/>
                                        <span class="fileLogo"></span>
                                    </li>
                                    <li class="seach25 seach33"><p>个人资料表：</p><input name="idcardfrontfile" type="text" readonly="readonly">
                                        <input type="button" name="idcardfront" class="btn btn-primary" value="上传"
                                               onclick="uploadFiles('${ctx}','idcardfrontfile',this);">
                                        <span class="fileLogo"></span></li>
                                    <li class="seach25 seach33"><p>担保书：</p><input name="entryformfile" type="text" readonly="readonly">
                                        <input type="button" name="entryform" class="btn btn-primary" value="上传"
                                               onclick="uploadFiles('${ctx}','entryformfile',this);">
                                        <span class="fileLogo"></span></li>
                                    <p class="kong"></p>
                                    <li class="seach25 seach33"><p>参团告知书：</p><input name="photofile" type="text" readonly="readonly">
                                        <input type="button" name="photo" class="btn btn-primary" value="上传"
                                               onclick="uploadFiles('${ctx}','photofile',this);">
                                        <span class="fileLogo"></span></li>
                                    <li class="seach25 seach33"><p>健康承诺书：</p><input name="idcardbackfile" type="text" readonly="readonly">
                                        <input type="button" name="idcardback" class="btn btn-primary" value="上传"
                                               onclick="uploadFiles('${ctx}','idcardbackfile',this);">
                                        <span class="fileLogo"></span></li>
                                    <li class="seach25 seach33"><p>其　它：</p><input name="otherfile" type="text" readonly="readonly">
                                        <input type="button" name="other" class="btn btn-primary" value="上传"
                                               onclick="uploadFiles('${ctx}','otherfile',this);">
                                        <span class="fileLogo"></span></li>
                                    <p class="kong"></p>
                                    <li class="seach25 each33"><p>签证附件：</p><input name="visaannexfile" type="text" readonly="readonly">
                                        <input type="button" name="visaannex" class="btn btn-primary" value="上传"
                                               onclick="uploadFiles('${ctx}','visaAnnexFile', this);">
                                        <span class="fileLogo"></span>
                                    </li>
                                    <p class="kong"></p>

                                </ul>
                            </ul>
                            <div class="ydbz_tit ydbz_tit_child"><span style="display:inline-block">备注：</span>
                                <textarea name="remark" class="textarea_long" maxlength="200"></textarea>
                            </div>
                        </div>
                        <c:if test="${isLoose}">
                            <div class="tourist-right peer">
                                <div class="bj-info">
                                    <div class="ydbz_tit ydbz_tit_child" style="margin: auto">报价</div>
                                    <div class="clearfix">
                                        <ul class="tourist-info2">
                                            <li class="tourist-info2-first">
                                                <label class="ydLable2 ydColor1">住房要求：</label>
                                                <select class="selZF" name="hotelDemand">
                                                    <option value="2">双人间</option>
                                                    <option value="1">单人间</option>
                                                </select>
                                            </li>
                                            <li><label>单人房差：</label>${singleDiffCurrencyMark}<span>0</span>
                                                <input type="hidden" name="singleDiff" class="traveler" value="0">/间 x
                                                <input type="text" class="ipt4" name="sumNight" maxlength="3" onkeyup="changeSumNight(this)"
                                                       onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')">晚
                                            </li>
                                        </ul>
                                    </div>
                                    <div class="yd-line"></div>
                                    <div class="clearfix">
                                        <div class="traveler-rebatesDiv">
                                            <label class="ydColor1 ydLable2">预计个人返佣：</label><select name="rebatesCurrency">
                                                ${currencyOptions}
                                        </select>
                                            <input type="text" class="ipt-rebates" name="rebatesMoney" maxlength="9" onafterpaste="checkRebatesValue(this)"
                                                   onkeyup="checkRebatesValue(this)"/>
                                        </div>
                                    </div>
                                    <div class="yd-line"></div>
                                    <div class="clearfix">
                                        <div class="traveler-rebatesDiv-discount-limit">
                                            <label class="ydLable2 ydColor1">产品优惠额度：</label>
                                            <span class="discount">
                                                <span class="adultDiscount" style="display: none">
                                                    <span class="activityDiscountCurrencyMark">
                                                        ${fns:getCurrencyNameOrFlag(adultDiscountCurrencyId, '0')}
                                                    </span>
                                                    <input type="hidden" name="activityDiscountCurrencyId" value="${adultDiscountCurrencyId}"/>
                                                    <span class="activityDiscountAmount">
                                                        ${adultDiscountPrice}
                                                    </span>
                                                </span>
                                                <span class="childDiscount" style="display: none">
                                                    <span class="activityDiscountCurrencyMark">
                                                        ${fns:getCurrencyNameOrFlag(childDiscountCurrencyId, '0')}
                                                    </span>
                                                    <input type="hidden" name="activityDiscountCurrencyId" value="${childDiscountCurrencyId}"/>
                                                    <span class="activityDiscountAmount">
                                                        ${childDiscountPrice}
                                                    </span>
                                                </span>
                                                <span class="specialDiscount" style="display: none">
                                                    <span class="activityDiscountCurrencyMark">
                                                        ${fns:getCurrencyNameOrFlag(specialDiscountCurrencyId, '0')}
                                                    </span>
                                                    <input type="hidden" name="activityDiscountCurrencyId" value="${specialDiscountCurrencyId}"/>
                                                    <span class="activityDiscountAmount">
                                                        ${specialDiscountPrice}
                                                    </span>
                                                </span>
                                            </span>
                                        </div>
                                        <div class="traveler-rebatesDiv-discount-limit">
                                            <label class="ydLable2 ydColor1">优惠金额：</label>
                                            <input class="discount-limit" name="discountPrice" maxlength="9" type="text"
                                                   onafterpaste="changeDiscountPrice(this)" onkeyup="changeDiscountPrice(this)">
                                        </div>
                                    </div>
                                    <div class="yd-line"></div>
                                     <shiro:hasPermission name="${shiroType }:order:otherExpenses">
                                    <div class="clearfix">
                                        <a name="addcost" class="btn-addBlue">添加其他费用</a>
										<div class="payfor-otherDiv" style="display: none">
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
		                                    	</tbody>
		                                    </table>
		                                    <input type="hidden" name="costNames" value="">
		                                    <input type="hidden" name="costCurrencyIds" value="">
		                                    <input type="hidden" name="costNums" value="">
		                                    <input type="hidden" name="costPrices" value="">
		                                    <input type="hidden" name="costTotals" value="">
		                                    <input type="hidden" name="businessTypes" value="">
	                                   </div>
                                    </div>
                                    </shiro:hasPermission>
                                    <div class="yd-line"></div>
                                    <div class="clearfix">
                                        <div class="traveler-rebatesDiv-discount-limit">
                                            <p><label>同行价：</label>
                                                <span name="innerJsPrice"></span>
                                                <input type="hidden" name="srcPrice" class="traveler">
                                                <input type="hidden" name="srcPriceCurrency" class="traveler">
                                                <input type="hidden" name="jsPrice" class="traveler">
                                            </p>

                                            <p class="ydLable2"><label>单房差小计：</label>${singleDiffCurrencyMark}<span class="forPeer"></span></p>

                                            <p class="ydLable2"><label>同行结算价：</label><span class="peerJsPrice"></span></p>

                                            <p class="ydLable2"><label>其他费用总计：</label><span class="otherPrice"></span></p>
                                        </div>
                                    </div>
                                </div>
                                <div class="yd-line"></div>
                                <div class="yd-total clearfix">
                                	<!--  
									<a name="bjyyqb" class="ydbz_x" onclick="useAllPrice(this)">报价应用全部</a>
									-->
                                </div>
                                <div class="yd-total clearfix">
                                    <div class="fr">
                                        <div class="traveler-rebatesDiv-discount-limit">
                                            <p class="ydLable2 total"><label> 结算价总计：</label><span name="clearPrice"></span></p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="tourist-right notPeer" style="display: none;">
                                <div class="bj-info">
                                    <div class="ydbz_tit ydbz_tit_child">报价</div>
                                    <div class="clearfix">
                                        <ul class="tourist-info2">
                                            <li class="tourist-info2-first">
                                                <label class="ydLable2 ydColor1">住房要求：</label>
                                                <select class="selZF" name="hotelDemand">
                                                    <option value="2">双人间</option>
                                                    <option value="1">单人间</option>
                                                </select>
                                            </li>
                                            <li><label>单人房差：</label>${singleDiffCurrencyMark}<span>0</span>
                                                <input type="hidden" name="singleDiff" class="traveler" value="0">/间 x
                                                <input type="text" class="ipt4" name="sumNight" maxlength="3" onkeyup="changeSumNight(this)"
                                                       onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')">晚
                                            </li>
                                            <li><label class="ydLable2">单房差小计：</label>${singleDiffCurrencyMark}<span class="ydFont1"></span></li>
                                        </ul>
                                    </div>
                                    <div class="yd-line"></div>

                                    <div class="clearfix">
                                        <div class="traveler-rebatesDiv">
                                            <label class="ydLable2 ydColor1">预计个人返佣：</label>
                                            <select name="rebatesCurrency">
                                                    ${currencyOptions}
                                            </select>
                                            <input type="text" class="ipt-rebates" name="rebatesMoney" maxlength="9" onafterpaste="checkRebatesValue(this)"
                                                   onkeyup="checkRebatesValue(this)"/>
                                        </div>
                                    </div>

                                    <div class="yd-line"></div>
                                     <shiro:hasPermission name="${shiroType }:order:otherExpenses">
                                    <div class="clearfix">
                                        <a name="addcost" class="btn-addBlue">添加其他费用</a>
	                                    <div class="payfor-otherDiv" style="display: none">
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
		                                    	</tbody>
		                                    </table>
		                                    <input type="hidden" name="costNames" value="">
		                                    <input type="hidden" name="costCurrencyIds" value="">
		                                    <input type="hidden" name="costNums" value="">
		                                    <input type="hidden" name="costPrices" value="">
		                                    <input type="hidden" name="costTotals" value="">
		                                    <input type="hidden" name="businessTypes" value="">
	                                   </div>
	                                </div>
	                                </shiro:hasPermission>
                                </div>
                                <div class="yd-line"></div>
                                <div class="yd-total clearfix">
                                	<!--  
                                	<a name="bjyyqb" class="ydbz_x" onclick="useAllPrice(this)">报价应用全部</a>
                                	-->
                                </div>
                                <div class="yd-total clearfix">
                                    <div class="fr">
                                        <label class="ydLable2"><span name="labelSPGPrice"></span>：</label><span name="innerJsPrice" class="ydFont2"></span>
                                        <input type="hidden" name="srcPrice" class="traveler">
                                        <input type="hidden" name="srcPriceCurrency" class="traveler">
                                        <input type="hidden" name="jsPrice" class="traveler">
                                    </div>
                                    <div class="fr">
                                        <label class="ydLable2">结算价：</label><span name="clearPrice" class="ydFont2"></span>
                                        <input type="hidden" name="travelerClearPrice" class="traveler">
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${!isLoose}">
                            <div class="tourist-right">
                                <div class="bj-info">
                                    <div class="ydbz_tit ydbz_tit_child">报价</div>
                                    <div class="clearfix">
                                        <ul class="tourist-info2">
                                            <li class="tourist-info2-first">
                                                <label class="ydLable2 ydColor1">住房要求：</label>
                                                <select class="selZF" name="hotelDemand">
                                                    <option value="2">双人间</option>
                                                    <option value="1">单人间</option>
                                                </select>
                                            </li>
                                            <li><label>单人房差：</label>${singleDiffCurrencyMark}<span>0</span>
                                                <input type="hidden" name="singleDiff" class="traveler" value="0">/间 x
                                                <input type="text" class="ipt4" name="sumNight" maxlength="3" onkeyup="changeSumNight(this)"
                                                       onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')">晚
                                            </li>
                                            <li><label class="ydLable2">单房差小计：</label>${singleDiffCurrencyMark}<span class="ydFont1"></span></li>
                                        </ul>
                                    </div>
                                    <div class="yd-line"></div>

                                    <div class="clearfix">
                                        <div class="traveler-rebatesDiv">
                                            <label class="ydLable2 ydColor1">预计个人返佣：</label>
                                            <select name="rebatesCurrency">
                                                    ${currencyOptions}
                                            </select>
                                            <input type="text" class="ipt-rebates" name="rebatesMoney" maxlength="9" onafterpaste="checkRebatesValue(this)"
                                                   onkeyup="checkRebatesValue(this)"/>
                                        </div>
                                    </div>

                                    <div class="yd-line"></div>
                                     <shiro:hasPermission name="${shiroType }:order:otherExpenses">
                                    <div class="clearfix">
                                        <a name="addcost" class="btn-addBlue">添加其他费用</a>
	                                    <div class="payfor-otherDiv" style="display: none">
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
		                                    	</tbody>
		                                    </table>
		                                    <input type="hidden" name="costNames" value="">
		                                    <input type="hidden" name="costCurrencyIds" value="">
		                                    <input type="hidden" name="costNums" value="">
		                                    <input type="hidden" name="costPrices" value="">
		                                    <input type="hidden" name="costTotals" value="">
		                                    <input type="hidden" name="businessTypes" value="">
	                                   </div>
	                                 </div>
	                                 </shiro:hasPermission>
                                </div>
                                <div class="yd-line"></div>
                                <div class="yd-total clearfix">
                                	<!--  
                                	<a name="bjyyqb" class="ydbz_x" onclick="useAllPrice(this)">报价应用全部</a>
                                	-->
                                </div>
                                <div class="yd-total clearfix">
                                    <div class="fr">
                                        <label class="ydLable2"><span name="labelSPGPrice"></span>：</label><span name="innerJsPrice" class="ydFont2"></span>
                                        <input type="hidden" name="srcPrice" class="traveler">
                                        <input type="hidden" name="srcPriceCurrency" class="traveler">
                                        <input type="hidden" name="jsPrice" class="traveler">
                                    </div>
                                    <div class="fr">
                                        <label class="ydLable2">结算价：</label><span name="clearPrice" class="ydFont2"></span>
                                        <input type="hidden" name="travelerClearPrice" class="traveler">
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </c:if>
                </div>
            </div>
            <!--保存按钮开始-->
            <c:if test="${!isForYouJia}">
            <div class="rightBtn">
                <input type="button" name="saveBtn" class="btn btn-primary" onclick="saveTraveler(this,this.form,${travelerKind})" value="保存">
                <input type="button" name="editBtn" class="btn btn-primary" onclick="saveTravelerAfter(this,this.form,'edit')" style="display:none" value="修改">
            </div>
            </c:if>
            <c:if test="${isForYouJia}">
            <div class="rightBtn">
                <input type="button" name="saveBtn" class="btn btn-primary" onclick="saveTraveler4YouJia(this,this.form,${travelerKind})" value="保存">
                <input type="button" name="editBtn" class="btn btn-primary" onclick="saveTravelerAfter(this,this.form,'edit')" style="display:none"
                       value="修改">
            </div>
            </c:if>
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
						<tr>
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
										onblur="changeOtherCostNum(this)" onafterpaste="changeOtherCostNum(this)" onkeyup="changeOtherCostNum(this)">
							</td>
							<td class="tc">
								<input class="dataChange"  name="price" type="text" maxlength="14" value=""
										onblur="changeOtherCostPrice(this)" onpaste="changeOtherCostPrice(this)" onkeyup="changeOtherCostPrice(this)">
							</td>
							<td class="tc" name="result">
								<em class="currency"></em><em class="result"></em>
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
</div>