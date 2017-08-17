
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>产品-签证产品修改-上传资料</title>

<script type="text/javascript" src="${ctxStatic}/modules/visa/visaProductsModUpload.js"></script>
<script type="text/javascript" src="${ctxStatic}/modules/activity/activity.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript">
		//文件下载
		function downloads(docid,activitySerNum,acitivityName,iszip){
	    	if(iszip){
	    		var zipname = activitySerNum;
	    		window.open("${ctx}/sys/docinfo/zipdownload/"+docid+"/"+zipname);
	    	}
	    		
	    	else
	    		window.open("${ctx}/sys/docinfo/download/"+docid);
	    }
	
	</script>
	
</head>
<body>
<page:applyDecorator name="show_head">
    <page:param name="desc">修改签证产品</page:param>
</page:applyDecorator>
    <!--右侧内容部分开始-->
    <div class="produceDiv">
        <div style="width:100%; height:20px;"></div>
        <div class="visa_num visa_num3" id="dh"></div>
        <form enctype="multipart/form-data" method="post" modelAttribute="visaProduct" action="${ctx}/visa/visaProducts/save" class="form-search" id="addForm" novalidate="novalidate">
            <input id="productId" type="hidden" name="id" value="${visaProduct.id }"/>
            <input id="sysCountryId" type="hidden" name="sysCountryId" value="${visaProduct.sysCountryId }"/>
            <input id="visaType" type="hidden" name="visaType" value="${visaProduct.visaType}"/>
            <input id="productName" type="hidden" name="productName" value="${visaProduct.productName }"/>
            <input id="contactPerson" type="hidden" name="contactPerson" value="${visaProduct.contactPerson }"/>
            <input id="reserveMethod" type="hidden" name="reserveMethod" value="${visaProduct.reserveMethod}"/>
            <input id="stayTime" type="hidden" name="stayTime" value="${visaProduct.stayTime }"/>
            <input id="collarZoning" type="hidden" name="collarZoning" value="${visaProduct.collarZoning }"/>
            <input id="currencyId" type="hidden" name="currencyId" value="${visaProduct.currencyId }"/>
            <input id="visaPrice" type="hidden" name="visaPrice" value="${visaProduct.visaPrice }"/>
            <input id="visaPay" type="hidden" name="visaPay" value="${visaProduct.visaPay }"/>
            <!-- 0258-qyl-begin -->
            <input id="invoiceQZ" type="hidden" name="invoiceQZ" value="${visaProduct.invoiceQZ }"/>
            <!-- 0258-end -->
            <input id="otherCost" type="hidden" name="otherCost" value="${visaProduct.otherCost }"/>
            <input id="remark" type="hidden" name="remark" value="${visaProduct.remark }"/>
            <input id="productStatus" type="hidden" name="productStatus" value="${visaProducts.productStatus}"/>
            <input id="payableDate" type="hidden" name="payableDate" value="${payableDate}"/>
            <input id="deptId" type="hidden" name="deptId" value="${visaProduct.deptId }"/>
            <input id="groupCode" type="hidden" name="groupCode" value="${visaProduct.groupCode }"/>

            <div class="mod_information_dzhan" id="oneStepDiv">

                <div class="kongr"></div>
                <div class="kongr"></div>
                <div class="mod_information_d7"></div>
                <div class="kongr"></div>
                <div class="kongr"></div>
                <div class="mod_information_dzhan_d error_add1" id="oneStepContent">
                    <div class="mod_information_d1">
                        <label><span class="xing displayClick">*</span>产品名称：</label>
                        <span class="disabledshowspan">${visaProduct.productName }</span>
                    </div>
                    <div class="kongr"></div>

                    <div class="mod_information_d2">
                        <label>签证国家：</label>
                        <span class="disabledshowspan">
                          ${sysCountry}
                        </span>
                    </div>
                    <div class="mod_information_d2">
                        <label>签证领区：</label>
                        <span class="disabledshowspan">
                           <c:if test="${not empty visaProduct.collarZoning }">
	                           ${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}
                        	</c:if>
                        </span>
                    </div>
                    <div class="mod_information_d2">
                        <label>签证类型：</label>
                        <span class="disabledshowspan">
                        	${visaType}
                        </span>
                    </div>
                    <label>领区联系人：</label>
                          <span class="disabledshowspan">${visaProduct.contactPerson}</span>
                    <div class="kongr"></div>
                    
                    <div class="mod_information_d2">
                        <label>部门：</label>
                        <span>${deptName }</span>
                    </div>
                    <!-- C460 -->
                    <c:if test="${fns:getUser().company.groupCodeRuleQZ eq 0}"><!-- 0:手动输入 --> 
                    	<div class="mod_information_d2">
                    		<label>团号：</label>
                        	<span class="disabledshowspan">${groupCode}</span>
                   		</div>
                    </c:if>
                    <!-- C460V3 不分自动还是手动，均显示团号-->
                    <c:if test="${fns:getUser().company.groupCodeRuleQZ eq 1}"><!-- 1:自动生成 --> 
                    	<div class="mod_information_d2">
                    		<label>团号：</label>
                        	<span class="disabledshowspan">${groupCode}</span>
                   		</div>
                    </c:if>
                    
                    <div class="kongr"></div>
                    <div class="mod_information_d2  add-paytype">
                        <label>预定方式：</label>
                        <font style="" id="payModeText"> 
	                        <c:choose>
	                            <c:when test="${visaProduct.reserveMethod eq '1'}">
	                          		预定<!-- （保留${visaProduct.stayTime}天） -->
	                            </c:when>
	                            <c:when test="${visaProduct.reserveMethod eq '2'}">
	                          		付全款
	                            </c:when>
	                            <c:otherwise>
	                            	预定<!-- （保留${visaProduct.stayTime}天） -->,付全款
	                            </c:otherwise>
	                        </c:choose>
                        </font>
                    </div>
                    <div class="kongr"></div>
                    <!--备注开始-->
                    <div style="width: 100%;" class="mod_information_d2 pro-marks">
                        <label>备注：</label>
                                    <span class="disabledshowspan" style="word-break: break-all">${visaProduct.remark }
                                	</span>
                    </div>
                    <!--备注结束-->
                    <div class="kongr"></div>
                </div>
            </div>

            <div style="" class="mod_information" id="secondStepDiv">
                <div class="mod_information_d" id="secondStepTitle"><span style=" font-weight:bold; padding-left:20px;float:left">添加价格</span></div>
                <div id="secondStepEnd">
                    <div style="width:100%; height:10px;"></div>
                    <div class="add2_nei">
                        <table class="table-mod2-group planeTick-table">
                            <tbody>
                            <tr>
                                <td class="add2_nei_table">成本价格：</td>
                                <td class="add2_nei_table_typetext"><span class="disabledshowspan"><c:if test="${not empty visaProduct.visaPrice and visaProduct.visaPrice ne 0}">${currency.currencyMark}&nbsp;&nbsp;</c:if><fmt:formatNumber pattern="0.00" value="${visaProduct.visaPrice}" /></span></td>
                                <td class="add2_nei_table">应收价格：</td>
                                <td class="add2_nei_table_typetext"><span class="disabledshowspan"><c:if test="${not empty visaProduct.visaPay and visaProduct.visaPay ne 0}">${currency.currencyMark}&nbsp;&nbsp;</c:if><fmt:formatNumber pattern="0.00" value="${visaProduct.visaPay }" /></span></td>
                                <td class="add2_nei_table">应付账期：</td>
                                <td class="add2_nei_table_typetext"><span class="disabledshowspan">${payableDate}</span></td>
                                <!-- 0258-begin -->
                              <c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23'}">
                                <td class="add2_nei_table">发票税：</td>
                                <td class="add2_nei_table_typetext"><c:if test="${not empty visaProduct.invoiceQZ and visaProduct.invoiceQZ ne 0}">&nbsp;</c:if><fmt:formatNumber pattern="0.00" value="${visaProduct.invoiceQZ }" />&nbsp;%</td>
                              </c:if>
                                <!-- 0258-end -->
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="kong"></div>
            </div>
            <div style="clear:none;" class="kong"></div>
            <div id="thirdStepDiv">
                <!-- 上传文件 -->
                <div class="mod_information_d"><span style=" font-weight:bold; padding-left:20px">签证资料</span></div>
               	<div class="mod_information_3">
	               	<input type="button" name="otherfile" value="选择文件" class="mod_infoinformation3_file" onClick="uploadFiles('${ctx}',null,this)"/>
	               	<ol class="batch-ol" style="margin-left:40px;">
						<c:forEach items="${docInfoList}" var="docInfo">
							<li>
								<input type="hidden" value="${docInfo.id}" name="otherfile">
								<span>${docInfo.docName}</span>
								<a class="batchDl" onclick="downloads(${docInfo.id},'','',false)">下载</a>&nbsp;&nbsp;
								<a class="batchDel" onclick="deleteFileInfo(null,null,this);" href="javascript:void(0)">删除</a>
							</li>
						</c:forEach>
					</ol>
				</div>
                <div class="mod_information_d"><span style=" font-weight:bold; padding-left:20px">需提供办签资料</span></div>
                <div class="mod_information_3">
                    <div id="otherflag" style="margin-top:8px;">
                        <label>需提供原件项目：</label><span class="seach_check">
			              <input type="checkbox" name="original_Project_Type" value="0" <c:if test="${fn:contains(visaProduct.original_Project_Type,'0')}">checked="checked"</c:if> id=""><label for="">护照</label></span><span class="seach_check">
			              <input type="checkbox" name="original_Project_Type" value="1" <c:if test="${fn:contains(visaProduct.original_Project_Type,'1')}">checked="checked"</c:if> id=""><label for="">身份证</label></span><span class="seach_check">
			              <!-- C197-start -->
			              <input type="checkbox" name="original_Project_Type" value="3" <c:if test="${fn:contains(visaProduct.original_Project_Type,'3')}">checked="checked"</c:if> id=""><label for="">电子照片</label></span><span class="seach_check">
			              <input type="checkbox" name="original_Project_Type" value="4" <c:if test="${fn:contains(visaProduct.original_Project_Type,'4')}">checked="checked"</c:if> id=""><label for="">申请表格</label></span><span class="seach_check">
			              <input type="checkbox" name="original_Project_Type" value="5" <c:if test="${fn:contains(visaProduct.original_Project_Type,'5')}">checked="checked"</c:if> id=""><label for="">户口本</label></span><span class="seach_check">
			              <input type="checkbox" name="original_Project_Type" value="6" <c:if test="${fn:contains(visaProduct.original_Project_Type,'6')}">checked="checked"</c:if> id=""><label for="">房产证</label></span><span class="seach_check">
			              <!-- C197-end -->
			              <span class="seach_check">
				              <input type="checkbox" name="original_Project_Type" value="2" <c:if test="${fn:contains(visaProduct.original_Project_Type,'2')}">checked="checked"</c:if>  id="originalProjectType4Others"><label for="">其他</label>
				              <input type="text" name="original_Project_Name" value="${visaProduct.original_Project_Name}" class="input-mini" maxlength="30"/>
			              </span>
                    </div>
                    <div id="otherflag" style="margin-top:8px;">
                        <label>需提供复印件项目：</label><span class="seach_check">
			              <!-- 197-start -->
			              <input type="checkbox" name="copy_Project_Type" value="3" <c:if test="${fn:contains(visaProduct.copy_Project_Type,'3')}">checked="checked"</c:if>  /><label for="">护照</label></span><span class="seach_check">
			              <input type="checkbox" name="copy_Project_Type" value="4" <c:if test="${fn:contains(visaProduct.copy_Project_Type,'4')}">checked="checked"</c:if> id=""><label for="">身份证</label></span><span class="seach_check">
			              <input type="checkbox" name="copy_Project_Type" value="5" <c:if test="${fn:contains(visaProduct.copy_Project_Type,'5')}">checked="checked"</c:if>  /><label for="">电子照片</label></span><span class="seach_check">
			              <input type="checkbox" name="copy_Project_Type" value="6" <c:if test="${fn:contains(visaProduct.copy_Project_Type,'6')}">checked="checked"</c:if> id=""><label for="">申请表格</label></span><span class="seach_check">
			              <input type="checkbox" name="copy_Project_Type" value="0" <c:if test="${fn:contains(visaProduct.copy_Project_Type,'0')}">checked="checked"</c:if>  /><label for="">户口本</label></span><span class="seach_check">
			              <input type="checkbox" name="copy_Project_Type" value="1" <c:if test="${fn:contains(visaProduct.copy_Project_Type,'1')}">checked="checked"</c:if> id=""><label for="">房产证</label></span><span class="seach_check">
			              <!-- 197-end -->
			              <span class="seach_check">
				              <input type="checkbox" name="copy_Project_Type"  value="2" <c:if test="${fn:contains(visaProduct.copy_Project_Type,'2')}">checked="checked"</c:if> id="copyProjectType4Others"><label for="">其他</label>
				              <input type="text" name="copy_Project_Name" value="${visaProduct.copy_Project_Name}" class="input-mini" maxlength="30"/>
			              </span>
                    </div>
                </div>
                <!-- C460 签证修改记录 -->
                <!-- 当批发商具有团号库权限时,才进行修改记录的展示  -->
                <c:if test="${is_need_groupCode eq '1' }">
					<c:if
						test="${fns:getUser().company.groupCodeRuleQZ==0}">
						<div class="mod_information">
							<div class="mod_information_d">
								<span style="font-weight: bold; padding-left: 20px; float: left">修改记录</span>
							</div>
						</div>

						<c:forEach items="${groupcodeModifiedRecords}"
							var="modifiedRecord" varStatus="listIndex">
							<div class="mod_information_dzhan">
								<span class="modifyTime" style="margin-left: 30px"><fmt:formatDate
										value="${modifiedRecord.createDate}"
										pattern="yyyy-MM-dd HH:mm:ss" /></span> 【<span class="modifyType">团号</span>
								】 由【<span class="exGroupNo">${modifiedRecord.groupcodeOld}</span>
								】修改成【<span class="groupNo">${modifiedRecord.groupcodeNew}</span>】
								by【<span class="modifyUser">${modifiedRecord.updateByName}</span>】
							</div>
						</c:forEach>
					</c:if>
				</c:if>
                <div class="release_next_add">
                    <input type="button" value="上一步" onclick="ThirdToSecond()" class="btn btn-primary">
                    <input type="button" value="放&nbsp;&nbsp;弃" onclick="javascript:window.location.href='${ctx}/visa/visaProducts/list/2'" class="btn btn-primary gray">
<%--                    <!--  --><input type="button" value="保存草稿" onclick="submitForm(1);" class="btn btn-primary">--%>
                    <input type="button" value="提交保存" onclick="submitForm(2);" class="btn btn-primary">
                </div>
            </div>
        </form>
        <div style="height:2px; width:100%; clear:both;"></div>
    </div>
    <!--右侧内容部分结束-->
</body>
</html>