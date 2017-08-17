
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>产品-签证产品发布-上传资料</title>
<script type="text/javascript" src="${ctxStatic}/modules/activity/activity.js"></script>
<script type="text/javascript" src="${ctxStatic}/modules/visa/visaProductsUpload.js" ></script>
<script type="text/javascript">
function ThirdToSecond(){
	var pay = "${visaProduct.currencyId}" + ";" + "${visaProduct.visaPrice}" + ";" + "${visaProduct.visaPay}" + ";" + "${visaProduct.otherCost}";
	//$("#addForm").attr("action","../../visa/visaProducts/editVisaPrice.htm");
	$("#addForm").attr("action","../../visa/visaProducts/editVisaPrice.htm?pay="+pay);
	
    $("#addForm").submit();
}
</script>

<style type="text/css">
	.disableCss{
				pointer-events:none;
				color:#afafaf;
				cursor:default
				}
    /*added by ruiqi.zhang 修改按钮样式bug  2017-3-31 10:56:40*/
    a.btn-primary {
        background: #3781d6;
        border: 1px solid #3781d6;
        color: #fff;
    }

</style>
</head>
<body>
    <!--右侧内容部分开始-->
    <div class="produceDiv">
        <div style="width:100%; height:20px;"></div>
        <div class="visa_num visa_num3" id="dh"></div>
        <form enctype="multipart/form-data" method="post" action="${ctx}/visa/visaProducts/save" class="form-search" id="addForm" novalidate="novalidate">
            <input type="hidden"  value="${visaProduct.id}"  name="id" id="visaProductId">
            <input id="sysCountryId" type="hidden" name="sysCountryId" value="${visaProduct.sysCountryId }"/>
            <input id="visaType" type="hidden" name="visaType" value="${visaProduct.visaType}"/>
            <input id="deptId" type="hidden" name="deptId" value="${visaProduct.deptId}"/>
            <input id="groupCode" type="hidden" name="groupCode" value="${visaProduct.groupCode }" />
            <input id="productName" type="hidden" name="productName" value="${visaProduct.productName }"/>
            <input id="contactPerson" type="hidden" name="contactPerson" value="${visaProduct.contactPerson }"/>
            <input id="reserveMethod" type="hidden" name="reserveMethod" value="${visaProduct.reserveMethod}"/>
            <input id="stayTime" type="hidden" name="stayTime" value="${visaProduct.stayTime }"/>
            <input id="collarZoning" type="hidden" name="collarZoning" value="${visaProduct.collarZoning }"/>
            <input id="currencyId" type="hidden" name="currencyId" value="${visaProduct.currencyId }"/>
            <input id="visaPrice" type="hidden" name="visaPrice" value="${visaProduct.visaPrice }"/>
            <input id="visaPay" type="hidden" name="visaPay" value="${visaProduct.visaPay }"/>
            <input id="invoiceQZ" type="hidden" name="invoiceQZ" value="${visaProduct.invoiceQZ }"/>
            <input id="otherCost" type="hidden" name="otherCost" value="${visaProduct.otherCost }"/>
            <input id="remark" type="hidden" name="remark" value="${visaProduct.remark }"/>
            <input id="productStatus" type="hidden" name="productStatus" value="${visaProducts.productStatus}"/>
			<input id="payableDate" type="hidden" name="payableDate" value="<fmt:formatDate value="${visaProduct.payableDate}" pattern="yyyy-MM-dd" />"/>

            <div class="mod_information_dzhan" id="oneStepDiv">

                <div class="kongr"></div>
                <div class="kongr"></div>
                <div class="mod_information_d7"></div>
                <div class="kongr"></div>
                <div class="kongr"></div>
                <div class="mod_information_dzhan_d error_add1 iptToTxt" id="oneStepContent">
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
                    <label>领区联系人：</label>
                          <span class="disabledshowspan">${visaProduct.contactPerson}</span>
                    <div class="mod_information_d2">
                        <label>签证类型：</label>
                        <span class="disabledshowspan">
                        	${visaType}
                        </span>
                    </div>
                    <div class="kongr"></div>
                    <div class="mod_information_d2">
                        <label>部门：</label>
                        <span class="disabledshowspan">
                        	${deptName}
                        </span>
                    </div>
                <!-- C460 -->
				<c:if test="${fns:getUser().company.groupCodeRuleQZ eq 0}"><!-- 0:手动输入 -->
                    <div class="mod_information_d2">
                        <label>团号：</label>
                        <span class="disabledshowspan">
                        	${groupCode}
                        </span>
                    </div>
				</c:if>                   
                    <div class="mod_information_d2  add-paytype">
                        <label>预定方式：</label>
                        <font style="" id="payModeText"> 
	                        <c:choose>
	                            <c:when test="${visaProduct.reserveMethod eq '1'}">
	                          		预定（保留${visaProduct.stayTime}天）
	                            </c:when>
	                            <c:when test="${visaProduct.reserveMethod eq '2'}">
	                          		付全款
	                            </c:when>
	                            <c:otherwise>
	                            	预定（保留${visaProduct.stayTime}天）, 付全款
	                            </c:otherwise>
	                        </c:choose>
                        </font>
                    </div>
                    <div class="kongr"></div>
                    <!--备注开始-->
                    <div style="width: 100%;" class="mod_information_d2 pro-marks">
                        <label>备注：</label>
                        <span class="disabledshowspan" style="word-break: break-all">
                        	${visaProduct.remark }
                    	</span>
                    </div>
                    <!--备注结束-->
                    <div class="kongr"></div>
                </div>
            </div>

            <div style="" class="mod_information" id="secondStepDiv">
                <div class="mod_information_d" id="secondStepTitle"><span style=" font-weight:bold; padding-left:20px;float:left">签证费用</span></div>
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
                                <td class="add2_nei_table_typetext">
                                
                                <fmt:formatDate value="${visaProduct.payableDate}" pattern="yyyy-MM-dd" />
                               </td>
                            </tr>
                            <!-- 0258-qyl -->
                            <c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23'}">
                             <tr>
                                <td class="add2_nei_table">发票税：</td>
                                <c:if test="${!empty visaProduct.invoiceQZ}"><!-- 发票税不为空 -->
                                <td class="add2_nei_table_typetext"><fmt:formatNumber type="number" value="${visaProduct.invoiceQZ}" pattern="#0.00" />&nbsp;%</td> 
                                </c:if>
                                 <c:if test="${empty visaProduct.invoiceQZ}"><!-- 发票税为空 -->
                                <td class="add2_nei_table_typetext">0.00&nbsp;%</td> 
                                </c:if>
                            </tr>
                            </c:if>
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
					</ol>
                    <!--
                        <div class="">
                            <table border="0" style="vertical-align:middle;margin-top:10px;" name="company_logo">
                                <tbody><tr>
                                    <td><label>上传报名表：</label></td>
                                    <td><input type="text" style="width:160px;" readonly="readonly" name="fileLogo" class="valid"></td>
                                    <td>
                                        <input type="file" onchange="inFileName(this)" hidefocus="" size="1" style="position:absolute;filter:alpha(opacity=0);-moz-opacity:0;opacity:0;margin-left:0px; width:82px;" name="costagreement" id="costagreement">
                                        <input type="button" class="mod_infoinformation3_file" value="选择文件">
                                    </td>
                                </tr>
                            </tbody></table>
                            <input type="hidden" value="自费补充协议" name="costagreement_name" id="icostagreement_name">
                        </div>
                        <div class="mod_information_d7"></div>
                        <div style="margin-top:8px;" id="otherflag">
                            <label>上传其他：</label><a onclick="addfile(this)" href="javascript:void(0)"><img src="images/add_11.jpg"></a>
                        </div>
                        -->
                </div>
                <div class="mod_information_d"><span style=" font-weight:bold; padding-left:20px">需提供办签资料</span></div>
                <!--***********************197-start  -->
                <div class="mod_information_3">
                    <div id="otherflag" style="margin-top:8px;">
                        <label>需提供原件项目：</label>
              <span class="seach_check">
              <input type="checkbox" name="original_Project_Type" value="0" id=""><label for="">护照</label>
              </span>
              <span class="seach_check">
              <input type="checkbox" name="original_Project_Type" value="1" id=""><label for="">身份证</label>
              </span>
              <span class="seach_check">
              <input type="checkbox" name="original_Project_Type" value="3" id=""><label for="">电子照片</label>
              </span>
              <span class="seach_check">
              <input type="checkbox" name="original_Project_Type" value="4" id=""><label for="">申请表格</label>
              </span>
              <span class="seach_check">
              <input type="checkbox" name="original_Project_Type" value="5" id=""><label for="">户口本</label>
              </span>
              <span class="seach_check">
              <input type="checkbox" name="original_Project_Type" value="6" id=""><label for="">房产证</label>
              </span>
              <span class="seach_check">
              <input type="checkbox" name="original_Project_Type" value="2" id="originalProjectType4Others"><label for="">其他</label><input type="text" name="original_Project_Name" value="" class="input-mini" maxlength="30">
              </span>
                    </div>
                    <div id="otherflag" style="margin-top:8px;">
                        <label>需提供复印件项目：</label>
              <span class="seach_check">
              <input type="checkbox" name="copy_Project_Type" value="3" id=""><label for="">护照</label>
              </span>
              <span class="seach_check">
              <input type="checkbox" name="copy_Project_Type" value="4" id=""><label for="">身份证</label>
              </span>
              <span class="seach_check">
              <input type="checkbox" name="copy_Project_Type" value="5" id=""><label for="">电子照片</label>
              </span>
              <span class="seach_check">
              <input type="checkbox" name="copy_Project_Type" value="6" id=""><label for="">申请表格</label>
              </span>
              <span class="seach_check">
              <input type="checkbox" name="copy_Project_Type" value="0" id=""><label for="">户口本</label>
              </span>
              <span class="seach_check">
              <input type="checkbox" name="copy_Project_Type" value="1" id=""><label for="">房产证</label>
              </span>
              <span class="seach_check">
              <input type="checkbox" name="copy_Project_Type" value="2" id="copyProjectType4Others"><label for="">其他</label><input type="text" name="copy_Project_Name" value="" class="input-mini" maxlength="30"></span>
                    </div>
                </div>
                <!--***********************197-end  -->
                <div class="release_next_add">
                    <!--  <input type="button" value="上一步" onclick="ThirdToSecond()" class="btn btn-primary"> -->
                    <input type="button" value="放&nbsp;&nbsp;弃" onclick="javascript:window.location.href='${ctx}/visa/visaProducts/list/2'" class="btn btn-primary gray">
<%--                    <input type="button" value="保存草稿" onclick="submitForm(3);" class="btn btn-primary">--%>

                    <!-- 重复提交修改 -->
                    <!-- 
                    <input type="button" value="提交发布" onclick="submitForm(2);" class="btn btn-primary">
                     -->
                    <a id="" class="btn btn-primary" style="height: 18px;" onclick="submitForm(2,this);" href="javascript:void(0)">提交发布</a>
                </div>
            </div>
        </form>
        <!--
        <div class="mod_information_d6" style="display:none;" id="othertemplate">
            <table border="0" style="vertical-align:middle;margin-top:10px;" name="company_logo">
                <tbody><tr>
                    <td><label>其他文件：</label></td>
                    <td><input type="text" style="width:160px;" readonly="readonly" name="fileLogo" class="valid"></td>
                    <td>
                        <input type="file" onchange="inFileName(this)" hidefocus="" size="1" style="position:absolute;filter:alpha(opacity=0);-moz-opacity:0;opacity:0;margin-left:0px; width:82px;" name="otherfile" id="otherfile">
                        <input type="button" class="mod_infoinformation3_file" value="选择文件">
                    </td>
                    <td>
                        <a onclick="removefile('确定删除该文件吗', this)" class="mod_infoinformation3_del" href="javascript:void(0)">删除</a>
                    </td>
                </tr>
            </tbody></table>
            <input type="hidden" value="其他文件" name="otherfile_name" class="otherfile_name">
        </div>-->
        <div style="height:2px; width:100%; clear:both;"></div>
    </div>
    <!--右侧内容部分结束-->
</body>
</html>