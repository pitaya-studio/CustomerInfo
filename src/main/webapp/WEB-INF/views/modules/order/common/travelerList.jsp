<%@ page contentType="text/html;charset=UTF-8" %>
<c:forEach items="${travelers}" var="travelerone" varStatus="s">
	<form name="travelerForm"  class="travelerTable">
	  <input type="hidden" name="travelerOrderId" value="${travelerone.traveler.orderId}">
	  <input type="hidden" value="${singleDiffCurrencyId}" name="singleDiffCurrencyId">
	  <input type="hidden" name="travelerId" value="${travelerone.traveler.id}">
	  <div class="tourist">
	      <div class="tourist-t">
	          <a class="btn-del" style="cursor:pointer;" name="deleteTraveler">删除</a>
	          <span class="add_seachcheck" onclick="travelerBoxCloseOnAdd(this)"></span>
	          <label class="ydLable">游客<em class="travelerIndex"></em>:</label>
	          <div class="tourist-t-off">
	              <span class="fr">结算价：<span name="jsPrice" class="ydFont2"></span></span>
	              <span name="tName">${travelerone.traveler.name }</span>       
	          </div>
			<div class="tourist-t-on">
				<c:choose>
						<c:when test="${activityKind == '10'}">
							<label><input type="radio" class="traveler" name="personType" value="1" <c:if test="${travelerone.traveler.personType=='1' }">checked="checked"</c:if> />1/2同行</label>
	              			<label><input type="radio" class="traveler" name="personType" value="2" <c:if test="${travelerone.traveler.personType=='2' }">checked="checked"</c:if> />3/4同行</label>
						</c:when>
						<c:otherwise>
							<label><input type="radio" class="traveler" name="personType" value="1" <c:if test="${travelerone.traveler.personType=='1' }">checked="checked"</c:if> />成人</label>
	              			<label><input type="radio" class="traveler" name="personType" value="2" <c:if test="${travelerone.traveler.personType=='2' }">checked="checked"</c:if> />儿童</label>
	              			<label><input type="radio" class="traveler" name="personType" value="3" <c:if test="${travelerone.traveler.personType=='3' }">checked="checked"</c:if> />特殊人群</label>
						</c:otherwise>
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
	                                 <option value="${intermodal.price}" intermodalId="${intermodal.id}" priceCurrencyId="${intermodal.priceCurrency.id}" priceCurrency="${intermodal.priceCurrency.currencyName}" 
	                                 <c:if test="${travelerone.traveler.intermodalId==intermodal.id}">selected="selected"</c:if>>${intermodal.groupPart}</option>
	                              </c:forEach>
	                         </select>
	                                                                  币种：<label name="priceCurrency">${travelerone.intermodal.priceCurrency.currencyName}</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                                                                  联运价格：<label name="intermodalPrice">${travelerone.intermodal.price}</label>
	                         <input type="hidden" name="intermodalId" value="${travelerone.traveler.intermodalId}"/>
	                      </span>
	                  </div>
	              </c:if>
	          </div>
	      </div>
	      <div flag="messageDiv">
	        <div class="tourist-con">
	          <!--游客信息左侧开始-->
	          <div class="tourist-left">
	              <!-- 基本信息 -->
	              <div class="ydbz_tit ydbz_tit_child"><em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>基本信息</div>
	              <c:forEach items="${travelerone.visaList}" var="tvisa">
                  		<c:if test="${tvisa.zbqType==1}">
                  			<c:set var="zbqType" value="${tvisa.zbqType}"></c:set>
                  		</c:if>
                  </c:forEach>
	              <ul class="tourist-info1 clearfix" flag="messageDiv">
	                  <li>
	                      <label class="ydLable"><span class="xing">*</span>姓名：</label>
	                      <input type="text" maxlength="25" name="travelerName" value="${travelerone.traveler.name }" class="traveler required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()">
	                  </li>
	                  <li>
	                      <label class="ydLable">英文／拼音：</label>
	                      <input type="text" maxlength="100" name="travelerPinyin" value="${travelerone.traveler.nameSpell }" class="traveler">
	                  </li>
	                  <li>
	                      <label class="ydLable"><span class="xing">*</span>性别：</label>
	                      <select name="sex" class="selSex required">
	                          <option value="1" <c:if test="${travelerone.traveler.sex=='1' }">selected="selected"</c:if>>男</option>
	                          <option value="2" <c:if test="${travelerone.traveler.sex=='2' }">selected="selected"</c:if>>女</option>
	                      </select> 
	                  </li>
	                  <li>
	                      <label class="ydLable">国籍：</label>
	                      <select name="nationality" class="selCountry">
	                          <c:forEach items="${countryList}" var="country">
	                              <option value="${country.id}" <c:if test="${travelerone.traveler.nationality==country.id }">selected="selected"</c:if>>
	                              	${country.countryName_cn}
	                              </option>
	                          </c:forEach>
	                      </select>
	                  </li>
	                  <li>
	                      <label class="ydLable">出生日期：</label>
	                      <input type="text" name="birthDay" value='<fmt:formatDate value="${travelerone.traveler.birthDay}" pattern="yyyy-MM-dd"/>' class="traveler traveler2 dateinput"/>
	                  </li>
	                  <li>
	                      <label class="ydLable"><span class="xing"></span>联系电话：</label>
	                      <input type="text" name="telephone" class="traveler"  value="${travelerone.traveler.telephone}" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
	                  </li>
	                  <li>
	                      <label class="ydLable"><span class="xing">*</span>护照号：</label>
	                      <input type="text" name="passportCode" value="${travelerone.traveler.passportCode}" class="traveler required" maxlength="30" onkeyup="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')" >
	                  </li>
	                  <li>
	                  	<label class="ydLable"><span class="xing">*</span>发证日期：</label>
	                  	<input type="text" name="issuePlace" value='<fmt:formatDate value="${travelerone.traveler.issuePlace }" pattern="yyyy-MM-dd"/>' class="traveler traveler2 dateinput required">
	                  </li>
	                  <li>
	                      <label class="ydLable"><span class="xing">*</span>护照有效期：</label>
	                      <input type="text" name="passportValidity" value='<fmt:formatDate value="${travelerone.traveler.passportValidity}" pattern="yyyy-MM-dd"/>' class="traveler traveler2 dateinput required">
	                  </li>
	                  <li>
	                      <label class="ydLable"><span class="xing" <c:if test="${zbqType==1}">style="display: block;"</c:if>>*</span>身份证号：</label>
	                      <input type="text" name="idCard" value="${travelerone.traveler.idCard}" style="width:128px;padding-left:1px;padding-right:1px;" class="traveler" maxlength="18" onkeyup="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')" onblur="getBirthday(this);">
	                  </li>
	                  <li>
	                      <label class="ydLable"><span class="xing"></span>护照类型：</label>
	                      <select name="passportType" class="selCountry">
	                          <c:forEach items="${passportTypeList}" var="passportType">
	                              <option value="${passportType.key}" <c:if test="${travelerone.traveler.passportType == passportType.key}">selected="selected"</c:if>>
	                              	${passportType.value}
	                              </option>
	                          </c:forEach>
	                      </select>
	                  </li>
	              </ul>
	              <!-- 签证信息 -->
	              <div class="ydbz_tit ydbz_tit_child"><em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>签证信息</div>
	              <ul flag="messageDiv" class="zbqinfo">
	                  <div class="ydbz_tit ydbz_tit_child">自备签 
	                      <p class="tourist-ckb">
	                          <label class="ydLable">自备签国家：</label>
	                          <c:forEach items="${travelerone.visaList}" var="tvisa" varStatus="s">
	                              <input type="checkbox" class="traveler" value="${tvisa.applyCountry.name}" name="zibeiqian" <c:if test="${tvisa.zbqType==1 }">checked="checked"</c:if>><label class="ckb-txt">自备${tvisa.applyCountry.name}签证</label>
	                          </c:forEach>
	                      </p>
	                  </div>
	                  <div class="zjlx-tips  zibei-tips" <c:if test="${zbqType==1}">style="display: block;"</c:if>>
	                      <i class="arrow1">&nbsp;</i>
	                      <c:forEach items="${travelerone.visaList}" var="tvisa">
	                          <ul class="tourist-type clearfix" <c:if test="${tvisa.zbqType==0 }">style="display: none;"</c:if><c:if test="${tvisa.zbqType==1 }">style="display: block;"</c:if>>
	                              <li>
	                                  <label class="ydLable">${tvisa.applyCountry.name}签证有效期：</label>
	                                  <input type="text" name="zbqVisaDate" value="<fmt:formatDate value="${tvisa.visaDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker()" class="dateinput required">
	                              </li>
	                          </ul>
	                      </c:forEach>
	                  </div>
	                  <div class="ydbz_tit ydbz_tit_child">申请办签 </div>
	                  <div class="ydbz_scleft">
	                   <table class="table-visa">
	                       <thead><tr>
	                           <th width="15%">申请国家</th>
	                           <th width="15%">领区</th>
	                           <th width="15%">签证类别</th>
	                           <th width="10%">预计出团时间</th>
	                           <th width="15%">预计约签时间</th>
	                       </tr></thead>
	                       <tbody>
	                           <c:forEach items="${travelerone.visaList}" var="tvisa">
	                                   <tr class="${tvisa.applyCountry.name}" name="visainfo" <c:if test="${tvisa.zbqType==1}">style="display: none;"</c:if>>
	                                       <td class="tl">${tvisa.applyCountry.name}
		                                       <input type="hidden" value="${tvisa.applyCountry.id}" name="visaCountryId">
		                                       <input type="hidden" value="${tvisa.sysCountryId}" name="sysCountryId">
	                                       </td>
	                                       <td>
	                                           <select name="manor" onchange="changeManor(this)">
	                                               <c:forEach items="${tvisa.manorList}" var="manor">
	                                                 <option value="${manor.value}" <c:if test="${manor.value==tvisa.manorId}">selected="selected"</c:if>>${manor.label}</option>
	                                               </c:forEach>
	                                           </select>
	                                       </td>
	                                       <td>
	                                           <select name="visaType">
	                                               <c:forEach items="${tvisa.visaTypeList}" var="visaType">
	                                                 <option value="${visaType.value}" <c:if test="${visaType.value==tvisa.visaTypeId}">selected="selected"</c:if>>${visaType.label}</option>
	                                               </c:forEach>
	                                           </select>
	                                       </td>
	                                       <td><fmt:formatDate value="${tvisa.groupOpenDate}" pattern="yyyy-MM-dd"/></td>
	                                       <td><input type="text" name="contractDate" value="<fmt:formatDate value="${tvisa.contractDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker()" class="inputTxt dateinput"></td>
	                                   </tr>
	                           </c:forEach>
	                       </tbody>
	                   </table>
	                  </div>
	               <div class="ydbz_tit ydbz_tit_child">上传资料</div>
	               <ul class="ydbz_2uploadfile ydbz_scleft certificate">
	                   <li class="seach25 seach33"><p>护照首页：</p><input name="passportfile" value="<c:if test="${not empty travelerone.passportfile}">${travelerone.passportfile.fileName}</c:if>" type="text" readonly="readonly"><input type="button" name="passport" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','passportfile',this,'true');"/><span class="fileLogo"></span></li>
	                   <li class="seach25 seach33"><p>身份证正面：</p><input name="idcardfrontfile" value="<c:if test="${not empty travelerone.idcardfrontfile}">${travelerone.idcardfrontfile.fileName}</c:if>"  type="text" readonly="readonly"><input type="button" name="idcardfront" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','idcardfrontfile',this,'true');"><span class="fileLogo"></span></li>
	                   <li class="seach25 seach33"><p>申请表格：</p><input name="entryformfile" value="<c:if test="${not empty travelerone.entryformfile}">${travelerone.entryformfile.fileName}</c:if>"  type="text" readonly="readonly"><input type="button" name="entryform" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','entryformfile',this,'true');"><span class="fileLogo"></span></li>
	                   <p class="kong"></p>
	                   <li class="seach25 seach33"><p>房产证：</p><input name="housefile" value="<c:if test="${not empty travelerone.housefile}">${travelerone.housefile.fileName}</c:if>"  type="text" readonly="readonly"><input type="button" name="house" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','housefile',this,'true');"><span class="fileLogo"></span></li>
	                   <li class="seach25 seach33"><p>电子照片：</p><input name="photofile" value="<c:if test="${not empty travelerone.photofile}">${travelerone.photofile.fileName}</c:if>"  type="text" readonly="readonly"><input type="button" name="photo" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','photofile',this,'true');"><span class="fileLogo"></span></li>
	                   <li class="seach25 seach33"><p>身份证反面：</p><input name="idcardbackfile" value="<c:if test="${not empty travelerone.idcardbackfile}">${travelerone.idcardbackfile.fileName}</c:if>"  type="text" readonly="readonly"><input type="button" name="idcardback" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','idcardbackfile',this,'true');"><span class="fileLogo"></span></li>
	                   <p class="kong"></p>
	                   <li class="seach25 seach33"><p>户口本：</p><input name="residencefile" value="<c:if test="${not empty travelerone.residencefile}">${travelerone.residencefile.fileName}</c:if>"  type="text" readonly="readonly"><input type="button" name="residence" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','residencefile',this,'true');"><span class="fileLogo"></span></li>
	                   <li class="seach25 seach33"><p>其　它：</p><input name="otherfile" value="<c:if test="${not empty travelerone.otherfile}">${travelerone.otherfile.fileName}</c:if>"  type="text" readonly="readonly"><input type="button" name="other" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','otherfile',this,'true');"><span class="fileLogo"></span></li> 
	                   <p class="kong"></p>
	                   
	               </ul>
	               <!-- 办证资料 -->
	               <div class="ydbz_tit ydbz_tit_child">办证资料</div>
	               <ul class="seach25 seach100 ydbz_2uploadfile">
	                   <p>原件：</p> ${original}<br/> 
	                   <p>复印件：</p>${copyoriginal}
	               </ul>
	              </ul>
	              <div class="ydbz_tit ydbz_tit_child"><span style="display:inline-block">备注：</span><textarea name="remark" class="textarea_long" maxlength="200">${travelerone.traveler.remark}</textarea></div>
	          </div>
	          <!--游客信息左侧结束-->
	          
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
	                                  <option value="2" <c:if test="${travelerone.traveler.hotelDemand=='2'}">selected="selected"</c:if>>双人间</option>
	                                  <option value="1" <c:if test="${travelerone.traveler.hotelDemand=='1'}">selected="selected"</c:if>>单人间</option>
	                              </select>
	                          </li>
	                          <li><label>单人房差：</label>${singleDiffCurrencyMark}<span>
	                          	<c:if test="${travelerone.traveler.hotelDemand=='2'}">0</span><input type="hidden" name="singleDiff" class="traveler" value="0" ></c:if>
	                          	<c:if test="${travelerone.traveler.hotelDemand=='1'}">${travelerone.traveler.singleDiff}</span><input type="hidden" name="singleDiff" class="traveler" value="${travelerone.traveler.singleDiff}" ></c:if>/间 x<input type="text" class="ipt4" name="sumNight" maxlength="3" value="${travelerone.traveler.singleDiffNight}" onkeyup="changeSumNight(this)" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')">晚
	                          </li>
	                          <li><label class="ydLable2">单房差小计：</label>${singleDiffCurrencyMark}<span class="ydFont1">${travelerone.traveler.singleDiff * travelerone.traveler.singleDiffNight}</span>
	                          </li>
	                      </ul>
	                  </div>
	                  <div class="yd-line"></div>
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
							<input type="hidden" name="costNames" value="">
							<input type="hidden" name="costCurrencyIds" value="">
							<input type="hidden" name="costNums" value="">
							<input type="hidden" name="costPrices" value="">
							<input type="hidden" name="costTotals" value="">
							<input type="hidden" name="businessTypes" value="">
						</div>
					</div>
	              </div>
	              <div class="yd-line"></div>
	              <div class="yd-total clearfix">
	                <div class="fr">
	                  <label class="ydLable2">结算价：</label><span name="innerJsPrice" class="ydFont2"></span>
	                  <input type="hidden" name="srcPrice" class="traveler" value="${travelerone.traveler.srcPrice}">
	                  <input type="hidden" name="srcPriceCurrency" class="traveler" value="${travelerone.traveler.srcPriceCurrency.id}">
	                  <input type="hidden" name="jsPrice" class="traveler" value="${travelerone.payPrice}">
	                  <input type="hidden" name="jsPriceJson" class="traveler" value='${travelerone.payPriceJson}'>
	                </div>
	              </div>
	          </div>
	        </div>
	      </div>
	      <!--保存按钮开始-->
	      <div class="rightBtn">
	         <input type="button" name="saveBtn" class="btn btn-primary" onclick="saveTraveler(this,this.form,${travelerKind})" value="保存">
	         <input type="button" name="editBtn" class="btn btn-primary" onclick="saveTravelerAfter(this,this.form,'edit')" style="display:none" value="修改">
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
										onblur="changeOtherCostNum(this)" onafterpaste="changeOtherCostNum(this)" onkeyup="changeOtherCostNum(this)">
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