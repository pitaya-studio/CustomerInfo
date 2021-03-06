<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>渠道价格策略列表</title>
	<meta name="decorator" content="wholesaler"/>
	<style type="text/css">
	.sort{color:#0663A2;cursor:pointer;}
	
	/*0071需求样式 */
    label.myerror {
    color: #ea5200;
    font-weight: bold;
    margin-left: 0px;
    padding-bottom: 2px;
    padding-left: 0px;
}
	</style>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/jquery.number.js" type="text/javascript"></script>
	
	<style type="text/css">
	
        #contentTable th {
            height: 40px;
            border-top: 1px solid #CCC;
            }
        #teamTable{
            border:1px solid #CCC;
            }
            .groupNoteTipImg {
                display: inline-block;
                width: 12px;
                height: 12px;
                background-image: url("${ctxStatic}/images/order_s3.png");
                background-repeat: no-repeat;
                background-position: 0px center;
                margin: 4px 0px 0px 5px;
                line-height: 8px;
                vertical-align: top;
            }
	</style>
	<style type="text/css">
        input[disabled], select[disabled], textarea[disabled], input[readonly], select[readonly], textarea[readonly],
        input[disabled]:focus, select[disabled]:focus, textarea[disabled]:focus, input[readonly]:focus, select[readonly]:focus, textarea[readonly]:focus {
            cursor: auto;
            background: transparent;
            border: 0px;
            box-shadow: inset 0 0px 0px rgba(0, 0, 0, 0.075)
        }

        .tourist-info1 li {
            float: left;
            width: 33%;
            height: 30px;
            padding-top: 12px;
        }
        .chzn-select{
            width:130px;

        }
        .chosen-choices{
            border-radius: 4px;
        }
        .chosen-container-multi .chosen-choices li.search-choice{
            background-image: none;
            border-radius: 0;
            border: none;
        }
    </style>
	<!-- 需求223 -->
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css"/>
	 <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css" rel="stylesheet" type="text/css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/quauq.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/chosen.min.css" />
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/quauq.base64.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.plus.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/boxScroll.js"></script>
    <script type="text/javascript" src="${ctxStatic}/modules/agentToOffice/pricingStrategyEdit.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
     <script src="${ctxStatic}/js/chosen.jquery.min.js" type="text/javascript"></script>
	<!-- 需求223 -->
</head>
<body>
<c:if test="${priceStrategy }">
	<input type="hidden" value="${ctx}/pricingStrategy/manager" id="sysUrl">
	<input type="hidden" value="${priceStrategyId }" id="updateId">
    <div class="ydbzbox fs">
                    <div class="ydbz_tit">产品条件</div>
                    <ul class="ydbz_width">
                        <li class="titled">
                            <span class="margin">出发城市：</span>

                        </li>
                        <li class="quanXuan">
                            <em class="allChecked " onclick="allcheckBoxClick(this,'fromAreas')"></em>
                            <em class="notAllChecked " onclick="allcheckBoxClick(this,'fromAreas')"></em>
                            <em class="notChecked" onclick="allcheckBoxClick(this,'fromAreas')"></em>全选
                        </li>
                        <li class="ydbz_width_right">
                            <ul id="fromAreas">
                              <c:if test="${fromAreas != null }">
                              <c:forEach items="${fromAreas }" var="beginCity" varStatus="status">
                               <c:if test="${status.index < 14 }">
                                <li>
                                    <span>
                                    <em class="allChecked " onclick="checkBoxClick(this,'fromAreas',${beginCity.key },'${beginCity.value.itemName }')"></em>
                                    <em class="notAllChecked" onclick="checkBoxClick(this,'fromAreas',${beginCity.key },'${beginCity.value.itemName }')"></em>
                                    <em class="notChecked" ${beginCity.value.checkedFlag?"name='initChecken'":"" } onclick="checkBoxClick(this,'fromAreas',${beginCity.key },'${beginCity.value.itemName }')"></em>
                                    <input type="hidden" value="${beginCity.key }">
                                    <font>${beginCity.value.itemName }</font>
                                    </span>
                                </li>
                                </c:if>
                                
                                   <c:if test="${status.index >= 14 }">
	                                <li class="lookMore" id="fromAreasMore">  
	                                 <span>
                                    <em class="allChecked " onclick="checkBoxClick(this,'fromAreas',${beginCity.key },'${beginCity.value.itemName }')"></em>
                                    <em class="notAllChecked" onclick="checkBoxClick(this,'fromAreas',${beginCity.key },'${beginCity.value.itemName }')"></em>
                                    <em class="notChecked" ${beginCity.value.checkedFlag?"name='initChecken'":"" } onclick="checkBoxClick(this,'fromAreas',${beginCity.key },'${beginCity.value.itemName }')"></em>
                                    <input type="hidden" value="${beginCity.key }">
                                    <font>${beginCity.value.itemName }</font>
                                    </span>
                                </li></c:if>
                                </c:forEach></c:if>
                            </ul>
                        </li>
                         <c:if test="${fromAreas != null && fn:length(fromAreas) >= 14}">
                                <ul class="moreLess">
                                    <a class="more" id="fromAreasMoreM" onclick="more('fromAreasMore')">更多></a>
                                    <a class="less " id="fromAreasMoreL" onclick="less('fromAreasMore')">收起></a>
                          </ul></c:if>
                    </ul>
                    <ul>
                        <li class="relative">
                            <ul class="ydbz_width">
                                <li class="titled">
                                    <span class="margin">线路：</span>

                                </li>
                                <li class="quanXuan">
                                    <em class="allChecked " onclick="allcheckBoxClick(this,'targetAreas')"></em>
                                    <em class="notAllChecked " onclick="allcheckBoxClick(this,'targetAreas')"></em>
                                    <em class="notChecked" onclick="allcheckBoxClick(this,'targetAreas')"></em> 全选
                                </li>
                               <li class="ydbz_width_right">
                                    <ul id="targetAreas">
                                    <c:if test="${targetAreaIds != null }">
                     				<c:forEach items="${targetAreaIds }" var="targetArea" varStatus="status">
                                       <c:if test="${status.index < 14 }"><li>
                                            <span>
                                                <em class="allChecked " onclick="checkBoxClick(this,'targetAreas',${targetArea.key },'${targetArea.value.itemName }')"></em>
                                                <em class="notAllChecked " onclick="checkBoxClick(this,'targetAreas',${targetArea.key },'${targetArea.value.itemName }')"></em>
                                                <em class="notChecked" ${targetArea.value.checkedFlag?"name='initChecken'":"" } onclick="checkBoxClick(this,'targetAreas',${targetArea.key },'${targetArea.value.itemName }')"></em>
                                                <input type="hidden" value="${targetArea.key }">
                                                <font>${targetArea.value.itemName }</font>
                                            </span>
                                        </li></c:if> 
                                        <c:if test="${status.index >= 14 }"><li class="lookMore" id="targetAreasMore">  <span>
                                                <em class="allChecked " onclick="checkBoxClick(this,'targetAreas',${targetArea.key },'${targetArea.value.itemName }')"></em>
                                                <em class="notAllChecked " onclick="checkBoxClick(this,'targetAreas',${targetArea.key },'${targetArea.value.itemName }')"></em>
                                                <em class="notChecked" ${targetArea.value.checkedFlag?"name='initChecken'":"" } onclick="checkBoxClick(this,'targetAreas',${targetArea.key },'${targetArea.value.itemName }')"></em>
                                                <input type="hidden" value="${targetArea.key }">
                                                <font>${targetArea.value.itemName }</font>
                                            </span></li></c:if></c:forEach></c:if>
                                    </ul>
                                </li> 
                                <c:if test="${targetAreaIds != null && fn:length(targetAreaIds) >= 14}">
                                <ul class="moreLess">
                                      <a class="more" id="targetAreasMoreM" onclick="more('targetAreasMore')">更多></a>
                                    <a class="less " id="targetAreasMoreL"  onclick="less('targetAreasMore')">收起></a>
                                </ul></c:if>
                            </ul>
                        </li>
                    </ul>
                    <ul class="ydbz_width">
                        <li class="titled">
                            <span class="margin">旅游类型：</span>

                        </li>
                        <li class="quanXuan">
                            <em class="allChecked " onclick="allcheckBoxClick(this,'travelTypes')"></em>
                            <em class="notAllChecked " onclick="allcheckBoxClick(this,'travelTypes')"></em>
                            <em class="notChecked" onclick="allcheckBoxClick(this,'travelTypes')"></em>全选
                        </li>
                       <li class="ydbz_width_right">
                            <ul id="travelTypes">
                            <c:if test="${travelTypes != null }">
                     		<c:forEach items="${travelTypes }" var="travelType" varStatus="status">
                     		<c:if test="${status.index < 14 }">
                                <li>
                                    <span>
                                    <em class="allChecked " onclick="checkBoxClick(this,'travelTypes',${travelType.key },'${travelType.value.itemName }')"></em>
                                    <em class="notAllChecked " onclick="checkBoxClick(this,'travelTypes',${travelType.key },'${travelType.value.itemName }')"></em>
                                    <em class="notChecked" ${travelType.value.checkedFlag?"name='initChecken'":"" }  onclick="checkBoxClick(this,'travelTypes',${travelType.key },'${travelType.value.itemName }')"></em>
                                    <input type="hidden" value="${travelType.key }">
                                    <font>${travelType.value.itemName }</font>
                                    </span>
                                </li></c:if>
                                 <c:if test="${status.index >= 14 }"><li class="lookMore" id="travelTypeMore">  <span>
                                  <em class="allChecked " onclick="checkBoxClick(this,'travelTypes',${travelType.key },'${travelType.value.itemName }')"></em>
                                    <em class="notAllChecked " onclick="checkBoxClick(this,'travelTypes',${travelType.key },'${travelType.value.itemName }')"></em>
                                    <em class="notChecked" ${travelType.value.checkedFlag?"name='initChecken'":"" }  onclick="checkBoxClick(this,'travelTypes',${travelType.key },'${travelType.value.itemName }')"></em>
                                    <input type="hidden" value="${travelType.key }">
                                    <font>${travelType.value.itemName }</font>
                                    </span>
                                </li></c:if>
                                </c:forEach></c:if>
                            </ul>
                        </li>
                        <c:if test="${travelTypes != null && fn:length(travelTypes) >= 14}">
                                <ul class="moreLess">
                                    <a class="more" id="travelTypeMoreM" onclick="more('travelTypeMore')">更多></a>
                                    <a class="less " id="travelTypeMoreL"  onclick="less('travelTypeMore')">收起></a>
                                </ul></c:if>
                    </ul>
                    <ul class="ydbz_width">
                        <li class="titled">
                            <span class="margin">产品类型：</span>

                        </li>
                        <li class="quanXuan">
                            <em class="allChecked " onclick="allcheckBoxClick(this,'productTypes')"></em>
                            <em class="notAllChecked " onclick="allcheckBoxClick(this,'productTypes')"></em>
                            <em class="notChecked" onclick="allcheckBoxClick(this,'productTypes')"></em>全选
                        </li>
                       <li class="ydbz_width_right">
                            <ul id="productTypes">
                            <c:if test="${productTypes != null }">
                     		<c:forEach items="${productTypes }" var="productType" varStatus="status">
                     		<c:if test="${status.index < 14 }">
                                <li>
                                    <span>
                                    <em class="allChecked " onclick="checkBoxClick(this,'productTypes',${productType.key },'${productType.value.itemName }')"></em>
                                    <em class="notAllChecked " onclick="checkBoxClick(this,'productTypes',${productType.key },'${productType.value.itemName }')"></em>
                                    <em class="notChecked" ${productType.value.checkedFlag?"name='initChecken'":"" } onclick="checkBoxClick(this,'productTypes',${productType.key },'${productType.value.itemName }')"></em>
                                     <input type="hidden" value="${productType.key }">
                                  <font>${productType.value.itemName }</font>
                                    </span>
                                </li></c:if>
                                 <c:if test="${status.index >= 14 }"><li class="lookMore" id="productTypeMore">  <span>
                                  <em class="allChecked " onclick="checkBoxClick(this,'productTypes',${productType.key },'${productType.value.itemName }')"></em>
                                    <em class="notAllChecked " onclick="checkBoxClick(this,'productTypes',${productType.key },'${productType.value.itemName }')"></em>
                                    <em class="notChecked" ${productType.value.checkedFlag?"name='initChecken'":"" } onclick="checkBoxClick(this,'productTypes',${productType.key },'${productType.value.itemName }')"></em>
                                     <input type="hidden" value="${productType.key }">
                                  <font>${productType.value.itemName }</font>
                                    </span>
                                </li></c:if>
                                </c:forEach></c:if>
                            </ul>
                        </li>
                        <c:if test="${productTypes != null && fn:length(productTypes) >= 14}">
                                <ul class="moreLess">
                                    <a class="more" id="productTypeMoreM" onclick="more('productTypeMore')">更多></a>
                                    <a class="less " id="productTypeMoreL"  onclick="less('productTypeMore')">收起></a>
                                </ul></c:if>
                    </ul>
                    <ul class="ydbz_width">
                        <li class="titled">
                            <span class="margin">产品系列：</span>

                        </li>
                        <li class="quanXuan">
                            <em class="allChecked " onclick="allcheckBoxClick(this,'productLevels')"></em>
                            <em class="notAllChecked " onclick="allcheckBoxClick(this,'productLevels')"></em>
                            <em class="notChecked" onclick="allcheckBoxClick(this,'productLevels')"></em>  全选
                        </li>
                        <li class="ydbz_width_right">
                            <ul id="productLevels">
                            <c:if test="${productLevels != null }">
                     	<c:forEach items="${productLevels }" var="productLevel" varStatus="status">
                     		<c:if test="${status.index < 14 }">
                                <li>
                                    <span>
                                    <em class="allChecked " onclick="checkBoxClick(this,'productLevels',${productLevel.key },'${productLevel.value.itemName }')"></em>
                                    <em class="notAllChecked " onclick="checkBoxClick(this,'productLevels',${productLevel.key },'${productLevel.value.itemName }')"></em>
                                    <em class="notChecked"  ${productLevel.value.checkedFlag?"name='initChecken'":"" } onclick="checkBoxClick(this,'productLevels',${productLevel.key },'${productLevel.value.itemName }')"></em>
                                    <input type="hidden" value="${productLevel.key }">
                                    <font>${productLevel.value.itemName }</font>
                                    </span>
                                </li></c:if>
                                  <c:if test="${status.index >= 14 }"><li class="lookMore" id="productLevelsMore">  <span>
                                   <em class="allChecked " onclick="checkBoxClick(this,'productLevels',${productLevel.key },'${productLevel.value.itemName }')"></em>
                                    <em class="notAllChecked " onclick="checkBoxClick(this,'productLevels',${productLevel.key },'${productLevel.value.itemName }')"></em>
                                    <em class="notChecked"  ${productLevel.value.checkedFlag?"name='initChecken'":"" } onclick="checkBoxClick(this,'productLevels',${productLevel.key },'${productLevel.value.itemName }')"></em>
                                    <input type="hidden" value="${productLevel.key }">
                                    <font>${productLevel.value.itemName }</font>
                                    </span>
                                </li></c:if>
                                </c:forEach></c:if>
                            </ul>
                        </li> 
                          <c:if test="${productLevels != null && fn:length(productLevels) >= 14}">
                                <ul class="moreLess">
                                    <a class="more" id="productLevelsMoreM"  onclick="more('productLevelsMore')">更多></a>
                                    <a class="less " id="productLevelsMoreL"  onclick="less('productLevelsMore')">收起></a>
                                </ul></c:if>
                    </ul>
                    <div class="newTrench">
                        <p>
                            <span class="ydbz_x" href="javascript:void(0);" onclick="newTrench()">+新增渠道优惠</span>
                        </p>
                    </div>
                    <c:forEach items="${priceDetail }" var="priceDetailSingle">
                   
                    <div id="priceTrategyMsgs">
                        <table class="trenchBox" >
                         <input name="angentPriceStrategyId" type="hidden" value="${priceDetailSingle.key }">
                            <thead class="trenchBoxChildrenOne">
                            <tr>
                                <th class="trenchCondition">渠道条件</th>
                                <th class="privileges">优惠内容
                                    <em class="remove-selected allDelete" onclick="deleteTrench(this)" ></em>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>
                                     <span class="marginLeft">渠道类型：</span>
                                    <select data-placeholder="请选择..."  class="chzn-select" multiple>
                                        <option value=""></option>
                                      	<c:forEach items="${priceDetailSingle.value.agentTypes }" var="agentType">
                                        	<option value="${agentType.key }" ${agentType.value.checkedFlag?" selected='selected'":"" }>${agentType.value.itemName }</option>
                                        </c:forEach>
                                    </select>
                                    <!--<a onclick="chose_mult_set_ini(‘#dl_chose2‘,‘1,3,5,8‘);" href="javascript:;;">Set a,c,e,h to Chose Mult-Select</a>-->
                                    <span class="marginLeft">渠道等级：</span>
                                    <select data-placeholder="请选择..."  class="chzn-select" multiple>
                                        <option value=""></option>
                                        <c:forEach  items="${priceDetailSingle.value.agentLevels }" var="agentLevel" >
                                        	<option value="${agentLevel.key }" ${agentLevel.value.checkedFlag?" selected='selected'":"" }>${agentLevel.value.itemName }</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td> <c:forEach items="${priceDetailSingle.value.adultPriceStrategys }" var="adultPriceStrategySingle" varStatus="status">
                                    <p name="selected" class="adult">
                                        <span class="privilegesW" ${status.index!=0?"style='visibility: hidden;'":"" }>成人：</span>
                                        <span>
                                            <select name="adultValues" id="" class="selectW" onchange="zheKou(this)">
                                               <!--  <option value="1" ${adultPriceStrategySingle.value.itemName==1?'selected=selected':'' }>加价</option> -->
                                                <option value="2" ${adultPriceStrategySingle.value.itemName==2?'selected=selected':'' }>直减</option>
                                                <option value="3" ${adultPriceStrategySingle.value.itemName==3?'selected=selected':'' }">折扣</option>
                                              <!--   <option value="4" ${adultPriceStrategySingle.value.itemName==4?'selected=selected':'' }">提价</option> -->
                                            </select>
                                        </span>
                                        <span class="relative">
                                            <input type="text" class="heightInput" onkeyup="changeClearPriceSum(this)" onafterpaste="changeClearPriceSum(this)" value="${adultPriceStrategySingle.value.itemValue }"/>
                                            <span class="absolute zheKou" style="right:8px;top:0;display:none;position:absolute">%</span>
                                        </span>
                                        <span>
                                            <em class="add-select" onclick="add_select(this)"></em>
                                            <em class="remove-selected" onclick="remove_selected(this)" ></em>
                                        </span>
                                    </p></c:forEach>
                                    <c:forEach items="${priceDetailSingle.value.childrenPriceStrategys }" var="childPriceStrategy" varStatus="status">
                                    <p name="selected" class="children">
                                        <span class="privilegesW" ${status.index!=0?"style='visibility: hidden;'":"" }>儿童：</span>
                                        <span>
                                            <select name="childrenValues" id="" class="selectW" onchange="zheKou(this)">
                                               <!--  <option value="1" $childPriceStrategy.value.itemName==1?'selected=selected':'' }>加价</option> -->
                                                <option value="2" ${childPriceStrategy.value.itemName==2?'selected=selected':'' }>直减</option>
                                                <option value="3" ${childPriceStrategy.value.itemName==3?'selected=selected':'' }">折扣</option>
                                              <!--   <option value="4" ${childPriceStrategy.value.itemName==4?'selected=selected':'' }">提价</option> -->
                                            </select>
                                        </span>
                                        <span class="relative">
                                            <input type="text" class="heightInput" onkeyup="changeClearPriceSum(this)" onafterpaste="changeClearPriceSum(this)" value="${childPriceStrategy.value.itemValue }"/>
                                            <span class="absolute zheKou" style="right:8px;top:0;display:none">%</span>
                                        </span>
                                        <span>
                                            <em class="add-select" onclick="add_select(this)"></em>
                                            <em class="remove-selected" onclick="remove_selected(this)" ></em>
                                        </span>
                                    </p></c:forEach>
                                    <c:if test="${priceDetailSingle.value.childrenPriceStrategys == null }">
                                    <p name="selected" class="children">
                                        <span class="privilegesW">儿童：</span>
                                        <span>
                                            <select name="childrenValues" id="" class="selectW" onchange="zheKou(this)">
                                              <!--   <option value="1" >加价</option> -->
                                                <option value="2" >直减</option>
                                                <option value="3" >折扣</option>
                                              <!--   <option value="4" >提价</option> -->
                                            </select>
                                        </span>
                                        <span class="relative">
                                            <input type="text" class="heightInput" onkeyup="changeClearPriceSum(this)" onafterpaste="changeClearPriceSum(this)" />
                                            <span class="absolute zheKou" style="right:8px;top:0;display:none">%</span>
                                        </span>
                                        <span>
                                            <em class="add-select" onclick="add_select(this)"></em>
                                            <em class="remove-selected" onclick="remove_selected(this)" ></em>
                                        </span>
                                    </p>
                                    </c:if>
                                    <c:if test="${priceDetailSingle.value.specialPriceStrategys == null}">
                                    <p name="selected" class="specialCrowd">
                                        <span class="privilegesW" >特殊人群：</span>
                                        <span>
                                            <select name="specialCrowdValues" id="" class="selectW" onchange="zheKou(this)">
                                             <!--  <option value="1" >加价</option> -->
                                              <option value="2" >直减</option>
                                              <option value="3" >折扣</option>
                                            <!--   <option value="4" >提价</option> -->
                                            </select>
                                        </span>
                                        <span class="relative">
                                            <input type="text" class="heightInput" onkeyup="changeClearPriceSum(this)" onafterpaste="changeClearPriceSum(this)" />
                                            <span class="absolute zheKou" style="right:8px;top:0;display:none">%</span>
                                        </span>
                                        <span>
                                            <em class="add-select" onclick="add_select(this)"></em>
                                            <em class="remove-selected" onclick="remove_selected(this)" ></em>
                                        </span>
                                    </p>
                                    </c:if>
                                      <c:forEach items="${priceDetailSingle.value.specialPriceStrategys }" var="specialPriceStrategy" varStatus="status">
                                    <p name="selected" class="specialCrowd">
                                        <span class="privilegesW"  ${status.index!=0?"style='visibility: hidden;'":"" }>特殊人群：</span>
                                        <span>
                                            <select name="specialCrowdValues" id="" class="selectW" onchange="zheKou(this)">
                                             <!--  <option value="1" ${specialPriceStrategy.value.itemName==1?'selected=selected':'' }>加价</option> -->
                                              <option value="2" ${specialPriceStrategy.value.itemName==2?'selected=selected':'' }>直减</option>
                                              <option value="3" ${specialPriceStrategy.value.itemName==3?'selected=selected':'' }">折扣</option>
                                            <!--   <option value="4" ${specialPriceStrategy.value.itemName==4?'selected=selected':'' }">提价</option> -->
                                            </select>
                                        </span>
                                        <span class="relative">
                                            <input type="text" class="heightInput" onkeyup="changeClearPriceSum(this)" onafterpaste="changeClearPriceSum(this)" value="${specialPriceStrategy.value.itemValue }"/>
                                            <span class="absolute zheKou" style="right:8px;top:0;display:none">%</span>
                                        </span>
                                        <span>
                                            <em class="add-select" onclick="add_select(this)"></em>
                                            <em class="remove-selected" onclick="remove_selected(this)" ></em>
                                        </span>
                                    </p></c:forEach>
                                </td>
                            </tr>
                            </tbody>
                        </table>

                        </p>
                    </div></c:forEach>
                    <c:if test="${priceStrategyId != null }"><input type="button" class="ydbz_x" value="保存" onclick="addPriceTrategy('${ctx}/pricingStrategy/manager',${priceStrategyId })"></c:if>
                    <input type="button" class="ydbz_x" value="取消" onclick="cancleSubmit('${ctx}/pricingStrategy/manager')">
	</div>               
</c:if>
</body>

</html>