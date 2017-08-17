<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>接待计调回复询价</title>
	<meta name="decorator" content="wholesaler"/>
	<%@ include file="commonVar.jsp"%>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/travelRequirementsData.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/replyEPriceInfo4Admit.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/epriceCommon.js" type="text/javascript"></script>
	<script type="text/javascript">
	
	/**
     * 附件上传回调方法
     * @param {Object} obj button对象
     * @param {Object} fileIDList  文件表id
     * @param {Object} fileNameList 文件原名称
     * @param {Object} filePathList 文件url
     */
    function commenFunction(obj,fileIDList,fileNameList,filePathList){
    	//var name = obj.name;
   // 	$("#upfileShow").append("<p class='seach_r'><span  class='seach_checkbox'  id='"+obj.name+"'></span></p>");
     	if(fileIDList){
     		var arrID = new Array();
     		arrID = fileIDList.split(';');
     		var arrName = new Array();
     		arrName = fileNameList.split(';');
     		var arrPath = new Array();
     		arrPath = filePathList.split(';');
     		for(var n=0;n<arrID.length;n++){
     			if(arrID[n]){
     				var $a = $("<a>"+arrName[n]+"</a>");
     				$a.append("<input type='hidden' name='salerTripFileId' value='"+arrID[n]+"'/>");
     				$a.append("<input type='hidden' name='salerTripFileName' value='"+arrName[n]+"'/>");
     				$a.append("<input type='hidden' name='salerTipFilePath' value='"+arrPath[n]+"'/>");
     				$("#upfileShow").append($a);
     			}
     		}
     	}
     }
	
	var record = {};
	record.id = '${record.id}';
	
	$(function(){
		//各块信息展开与收起
		boxCloseOn();
		//上传动作
		btfile();
	    //添加单价
		//tianjia();
	    inquiryCheckBOX();
	});
	</script>
<style type="text/css">
</style>
</head>
<body>
	<input type="hidden" id="companyUuid" value="${fns:getCompanyUuid()}">
	<!--右侧内容部分开始-->
		<!-- 询价基本信息 -- 不行，页面访问时会把非incude的内容给删除掉，不知道是什么机制-->
		<page:applyDecorator name="eprice_aop_repay" >
		</page:applyDecorator>		 
			
		<%@ include file="/WEB-INF/views/modules/eprice/ePriceDetailBaseInfo.jsp"%>
		
		<div class="ydbz_tit">
			<span class="ydExpand closeOrExpand"></span>接待社询价内容
		</div>			
		<div class="messageDiv" flag="messageDiv">
			<%@ include file="/WEB-INF/views/modules/eprice/ePriceDetail4Admit.jsp"%>
		</div>
				
                    <div class="xuxian"></div>
                    <div class="title_con">接待社计调回复</div>
                    <c:if test="${reply==null || reply.status<2 }">
	                    <form id="reply-eprice-form-id"  action="${ctx}/eprice/manager/project/replyAopEpriceForm" method="post">
	                    	<input type="hidden" name="replyId" value="${reply.id }"/>
	                    	<input type="hidden" name="replyId" value="${reply.id }"/>
		                    <div class="seach100 wbyu"  style="margin:0px 20px">
		                    	<textarea name="content" cols="" rows="" class=""></textarea>
		                    	<br/><br/>
		                    	<!-- 显示整体报价 -->
		                    	<input type="radio"  name="priceType"  value="1" checked="checked"/> 按游客类型整体报价
		                    	<!-- 显示细分报价-->
		                    	<input type="radio"  name="priceType"  value="2"/> 按游客类型细分报价
		                    	<br/><br/>
		                    	<!-- 整体报价 -->
		                    	<div  id="price-detaill-dl-all-id">
			                    	<ul class="adult-dt-price">
			                    		<li class="adult-dt-price" style = "display:none">
			                    			<label class="adultTitle">成&nbsp;&nbsp;人&nbsp;&nbsp;单&nbsp;&nbsp;价&nbsp;：</label>
			                    			<select name="adultCurrency"  style="width:150px" >
			                    				<option>请选择币种</option>
			                    				<c:forEach items="${currencyList }"  var="currency">
			                    					<option id="${currency.id }"  title="${currency.currencyMark }"  value="${currency.currencyExchangerate }">${currency.currencyName}</option>
			                    				</c:forEach>
			                    			</select>
			                    			&nbsp;&nbsp;
			                    			<label>汇率：</label>
			                    			<input type="text"  style="width:50px" class="exchangerate"   name="adultExchangerate"  maxlength="10" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" onblur="this.v();" value=""/>
			                    			&nbsp;&nbsp;
			                    			<label>价格：</label>
			                    			<span class="adultCurrencyMark"></span>
			                    			<input type="hidden" name="adultCurrencyId"/>
			                    			<input type="hidden" name="adultCurrencyName"/>
			                    			<input type="text"  style="width:150px" name="adultAmount"  maxlength="10" class="allNum"/>
			                    			<input type="button" class="btn btn-primary mar_xjia_add"  name="addNewPrice"  value="增加"/>
			                    		</li>
			                    		<li class="adult-dt-price">
			                    			<label class="adultTitle">成&nbsp;&nbsp;人&nbsp;&nbsp;单&nbsp;&nbsp;价&nbsp;：</label>
			                    			<select name="adultCurrency"  style="width:150px" >
			                    				<option>请选择币种</option>
			                    				<c:forEach items="${currencyList }"  var="currency">
			                    					<option id="${currency.id }"  title="${currency.currencyMark }"  value="${currency.currencyExchangerate }">${currency.currencyName}</option>
			                    				</c:forEach>
			                    			</select>
			                    			&nbsp;&nbsp;
			                    			<label>汇率：</label>
			                    			<input type="text"  style="width:50px"  class="exchangerate"  name="adultExchangerate" maxlength="10"  onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" onblur="this.v();"  value=""/>
			                    			&nbsp;&nbsp;
			                    			<label>价格：</label>
			                    			<span class="adultCurrencyMark"></span>
			                    			<input type="hidden" name="adultCurrencyId"/>
			                    			<input type="hidden" name="adultCurrencyName"/>
			                    			<input type="text"  style="width:150px" name="adultAmount"  maxlength="10"  class="allNum"/>
			                    			<input type="button" class="btn btn-primary mar_xjia_add"  name="addNewPrice"  value="增加"/>
			                    		</li>
			                    	</ul>
			                    	<ul class="child-dt-price">
			                    		<li class="child-dt-price" style="display:none">
			                    			<label  class="childTitle">儿&nbsp;&nbsp;童&nbsp;&nbsp;单&nbsp;&nbsp;价&nbsp;：</label>
			                    			<select name="childCurrency"  style="width:150px" >
			                    				<option>请选择币种</option>
			                    				<c:forEach items="${currencyList }"  var="currency">
			                    					<option id="${currency.id }"  title="${currency.currencyMark }"  value="${currency.currencyExchangerate }">${currency.currencyName}</option>
			                    				</c:forEach>
			                    			</select>
			                    			&nbsp;&nbsp;
			                    			<label>汇率：</label>
			                    			<input type="text"   style="width:50px"  class="exchangerate"    name="childExchangerate"  maxlength="10"  onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" onblur="this.v();"    value=""/>
			                    			&nbsp;&nbsp;
			                    			<label>价格：</label>
			                    			<span class="childCurrencyMark"></span>
			                    			<input type="hidden" name="childCurrencyId"/>
			                    			<input type="hidden" name="childCurrencyName"/>
			                    			<input type="text"   style="width:150px"   name="childAmount"  maxlength="10"   class="allNum"/>
			                    			<input type="button" class="btn btn-primary mar_xjia_add"  name="addNewPrice"  value="增加"/>
			                    		</li>
			                    		<li class="child-dt-price">
			                    			<label  class="childTitle">儿&nbsp;&nbsp;童&nbsp;&nbsp;单&nbsp;&nbsp;价&nbsp;：</label>
			                    			<select name="childCurrency"  style="width:150px" >
			                    				<option>请选择币种</option>
			                    				<c:forEach items="${currencyList }"  var="currency">
			                    					<option id="${currency.id }"  title="${currency.currencyMark }"  value="${currency.currencyExchangerate }">${currency.currencyName}</option>
			                    				</c:forEach>
			                    			</select>
			                    			&nbsp;&nbsp;
			                    			<label>汇率：</label>
			                    			<input type="text"   style="width:50px"  class="exchangerate"    name="childExchangerate" maxlength="10"  onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" onblur="this.v();"     value=""/>
			                    			&nbsp;&nbsp;
			                    			<label>价格：</label>
			                    			<span class="childCurrencyMark"></span>
			                    			<input type="hidden" name="childCurrencyId"/>
			                    			<input type="hidden" name="childCurrencyName"/>
			                    			<input type="text"   style="width:150px"   name="childAmount" maxlength="10"   class="allNum"/>
			                    			<input type="button" class="btn btn-primary mar_xjia_add"  name="addNewPrice"  value="增加"/>
			                    		</li>
			                    	</ul>
			                    	
			                    	<ul class="special-dt-price">
			                    		<li class="special-dt-price"   style="display:none">
			                    			<label  class="specialTitle">特殊人群单价：</label>
			                    			<select name="specialCurrency"  style="width:150px" >
			                    				<option>请选择币种</option>
			                    				<c:forEach items="${currencyList }"  var="currency">
			                    					<option id="${currency.id }"  title="${currency.currencyMark }"   value="${currency.currencyExchangerate }">${currency.currencyName}</option>
			                    				</c:forEach>
			                    			</select>
			                    			&nbsp;&nbsp;
			                    			<label>汇率：</label>
			                    			<input type="text"   style="width:50px"  class="exchangerate"    name="specialExchangerate"  maxlength="10"   onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" onblur="this.v();"   value=""/>
			                    			&nbsp;&nbsp;
			                    			<label>价格：</label>
			                    			<span class="specialCurrencyMark"></span>
			                    			<input type="hidden" name="specialCurrencyId"/>
			                    			<input type="hidden" name="specialCurrencyName"/>
			                    			<input type="text"   style="width:150px"  name="specialAmount"  maxlength="10"    class="allNum"/>
			                    			<input type="button" class="btn btn-primary mar_xjia_add"  name="addNewPrice"  value="增加"/>
			                    		</li>
			                    		<li class="special-dt-price">
			                    			<label  class="specialTitle">特殊人群单价：</label>
			                    			<select name="specialCurrency"  style="width:150px" >
			                    				<option>请选择币种</option>
			                    				<c:forEach items="${currencyList }"  var="currency">
			                    					<option id="${currency.id }"  title="${currency.currencyMark }"   value="${currency.currencyExchangerate }">${currency.currencyName}</option>
			                    				</c:forEach>
			                    			</select>
			                    			&nbsp;&nbsp;
			                    			<label>汇率：</label>
			                    			<input type="text"   style="width:50px"  class="exchangerate"    name="specialExchangerate"  maxlength="10"   onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" onblur="this.v();"   value=""/>
			                    			&nbsp;&nbsp;
			                    			<label>价格：</label>
			                    			<span class="specialCurrencyMark"></span>
			                    			<input type="hidden" name="specialCurrencyId"/>
			                    			<input type="hidden" name="specialCurrencyName"/>
			                    			<input type="text"   style="width:150px"  name="specialAmount"  maxlength="10"    class="allNum"/>
			                    			<input type="button" class="btn btn-primary mar_xjia_add"  name="addNewPrice"  value="增加"/>
			                    		</li>
			                    	</ul>
			                    	<div class="seach_allprice" id="price-all-div-id" >
			                    		<p>成人：${ record.baseInfo.adultSum } 人；儿童：${ record.baseInfo.childSum }  人；特殊人群：${ record.baseInfo.specialPersonSum }  人；共计：${record.baseInfo.adultSum+record.baseInfo.childSum+record.baseInfo.specialPersonSum } 人</p>
			                    		<p class="strAll"></p>
			                    	</div>
		                    	</div>
		                    	<input type="hidden"  id="adultSum"  name="adultSum"  value="${ record.baseInfo.adultSum }"/>
		                    	<input type="hidden"   id="childSum"  name="childSum"  value="${ record.baseInfo.childSum }"/>
		                    	<input type="hidden"   id="specialSum"  name="specialSum"  value="${ record.baseInfo.specialPersonSum }"/>
		                    	<!-- 细分报价 -->
		                    	<div id="price-detaill-dl-part-id"  style="display:none">
				                    	<ul class="adult-part-price">
				                    		<li class="adult-part-price" style="display:none">
				                    			<label class="adultTitle">成&nbsp;&nbsp;人&nbsp;&nbsp;单&nbsp;&nbsp;价&nbsp;：</label>
				                    			<select name="adultPartCurrency"  style="width:150px" >
				                    				<option>请选择币种</option>
				                    				<c:forEach items="${currencyList }"  var="currency">
				                    					<option id="${currency.id }"  title="${currency.currencyMark }"  value="${currency.currencyExchangerate }">${currency.currencyName}</option>
				                    				</c:forEach>
				                    			</select>
				                    			&nbsp;&nbsp;
				                    			<label>汇率：</label>
				                    			<input type="text"  style="width:50px"  class="exchangerate"   name="adultPartExchangerate"  maxlength="10"   onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" onblur="this.v();"  value=""/>
				                    			&nbsp;&nbsp;
				                    			<label>价格：</label>
				                    			<span class="adultPartCurrencyMark"></span>
				                    			<input type="hidden" name="adultPartCurrencyId"/>
				                    			<input type="hidden" name="adultPartCurrencyName"/>
				                    			<input type="hidden" name="adultPartNum"/>
				                    			<input type="text"  style="width:150px" name="adultPartAmount"  maxlength="10"    class="partNum"/>
				                    			&nbsp;&nbsp;
				                    			<label class="adultPartNum">人数：</label>
				                    			<!-- <input type="text"  style="width:50px" name="adultPersonNum"  class="personNum"  value="${ record.baseInfo.adultSum } "/>-->
				                    			<span class="adultPersonNum"   style="width:50px">${ record.baseInfo.adultSum }</span>
				                    			&nbsp;&nbsp;
				                    			<input type="button" class="btn btn-primary"  name="addNewpartPrice"  value="增加"/>
				                    		</li>
				                    		<li class="adult-part-price">
				                    			<label class="adultTitle">成&nbsp;&nbsp;人&nbsp;&nbsp;单&nbsp;&nbsp;价&nbsp;：</label>
				                    			<select name="adultPartCurrency"  style="width:150px" >
				                    				<option>请选择币种</option>
				                    				<c:forEach items="${currencyList }"  var="currency">
				                    					<option id="${currency.id }"  title="${currency.currencyMark }"  value="${currency.currencyExchangerate }">${currency.currencyName}</option>
				                    				</c:forEach>
				                    			</select>
				                    			&nbsp;&nbsp;
				                    			<label>汇率：</label>
				                    			<input type="text"  style="width:50px"  class="exchangerate"   name="adultPartExchangerate"  maxlength="10"   onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" onblur="this.v();"  value=""/>
				                    			&nbsp;&nbsp;
				                    			<label>价格：</label>
				                    			<span class="adultPartCurrencyMark"></span>
				                    			<input type="hidden" name="adultPartCurrencyId"/>
				                    			<input type="hidden" name="adultPartCurrencyName"/>
				                    			<input type="hidden" name="adultPartNum"/>
				                    			<input type="text"  style="width:150px" name="adultPartAmount"  maxlength="10"    class="partNum"/>
				                    			&nbsp;&nbsp;
				                    			<label class="adultPartNum">人数：</label>
				                    			<!-- <input type="text"  style="width:50px" name="adultPersonNum"  class="personNum"  value="${ record.baseInfo.adultSum } "/>-->
				                    			<span class="adultPersonNum"   style="width:50px">${ record.baseInfo.adultSum }</span>
				                    			&nbsp;&nbsp;
				                    			<input type="button" class="btn btn-primary"  name="addNewpartPrice"  value="增加"/>
				                    		</li>
				                    	</ul>
				                    	<ul class="child-part-price">
				                    		<li class="child-part-price"  style="display:none">
				                    			<label  class="childTitle">儿&nbsp;&nbsp;童&nbsp;&nbsp;单&nbsp;&nbsp;价&nbsp;：</label>
				                    			<select name="childPartCurrency"  style="width:150px" >
				                    				<option>请选择币种</option>
				                    				<c:forEach items="${currencyList }"  var="currency">
				                    					<option id="${currency.id }"  title="${currency.currencyMark }"  value="${currency.currencyExchangerate }">${currency.currencyName}</option>
				                    				</c:forEach>
				                    			</select>
				                    			&nbsp;&nbsp;
				                    			<label>汇率：</label>
				                    			<input type="text"   style="width:50px"  class="exchangerate"    name="childPartExchangerate"  maxlength="10"    onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" onblur="this.v();"  value=""/>
				                    			&nbsp;&nbsp;
				                    			<label>价格：</label>
				                    			<span class="childPartCurrencyMark"></span>
				                    			<input type="hidden" name="childPartCurrencyId"/>
				                    			<input type="hidden" name="childPartCurrencyName"/>
				                    			<input type="hidden" name="childPartNum"/>
				                    			<input type="text"   style="width:150px"   name="childPartAmount"  maxlength="10"   class="partNum"/>
				                    			&nbsp;&nbsp;
				                    			<label class="childPartNum">人数：</label>
				                    		<!-- <input type="text"  style="width:50px" name="childPersonNum"  class="personNum"   value="${ record.baseInfo.childSum } "/> -->	
				                    			<span class="childPersonNum"   style="width:50px">${ record.baseInfo.childSum }</span>
				                    			&nbsp;&nbsp;
				                    			<input type="button" class=""  name="addNewpartPrice"  value="增加"/>
				                    		</li>
				                    		<li class="child-part-price">
				                    			<label  class="childTitle">儿&nbsp;&nbsp;童&nbsp;&nbsp;单&nbsp;&nbsp;价&nbsp;：</label>
				                    			<select name="childPartCurrency"  style="width:150px" >
				                    				<option>请选择币种</option>
				                    				<c:forEach items="${currencyList }"  var="currency">
				                    					<option id="${currency.id }"  title="${currency.currencyMark }"  value="${currency.currencyExchangerate }">${currency.currencyName}</option>
				                    				</c:forEach>
				                    			</select>
				                    			&nbsp;&nbsp;
				                    			<label>汇率：</label>
				                    			<input type="text"   style="width:50px" class="exchangerate"  name="childPartExchangerate"  maxlength="10"  onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" onblur="this.v();"    value=""/>
				                    			&nbsp;&nbsp;
				                    			<label>价格：</label>
				                    			<span class="childPartCurrencyMark"></span>
				                    			<input type="hidden" name="childPartCurrencyId"/>
				                    			<input type="hidden" name="childPartCurrencyName"/>
				                    			<input type="hidden" name="childPartNum"/>
				                    			<input type="text"   style="width:150px"   name="childPartAmount" maxlength="10"    class="partNum"/>
				                    			&nbsp;&nbsp;
				                    			<label class="childPartNum">人数：</label>
				                    		<!-- <input type="text"  style="width:50px" name="childPersonNum"  class="personNum"   value="${ record.baseInfo.childSum } "/> -->	
				                    			<span class="childPersonNum"   style="width:50px">${ record.baseInfo.childSum }</span>
				                    			&nbsp;&nbsp;
				                    			<input type="button" class="btn btn-primary"  name="addNewpartPrice"  value="增加"/>
				                    		</li>
				                    	</ul>
				                    	
				                    	<ul class="special-part-price">
				                    		<li class="special-part-price" style="display : none">
				                    			<label  class="specialTitle">特殊人群单价：</label>
				                    			<select name="specialPartCurrency"  style="width:150px" >
				                    				<option>请选择币种</option>
				                    				<c:forEach items="${currencyList }"  var="currency">
				                    					<option id="${currency.id }"  title="${currency.currencyMark }"   value="${currency.currencyExchangerate }">${currency.currencyName}</option>
				                    				</c:forEach>
				                    			</select>
				                    			&nbsp;&nbsp;
				                    			<label>汇率：</label>
				                    			<input type="text"   style="width:50px" class="exchangerate"     name="specialPartExchangerate"  maxlength="10"   onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" onblur="this.v();"   value=""/>
				                    			&nbsp;&nbsp;
				                    			<label>价格：</label>
				                    			<span class="specialPartCurrencyMark"></span>
				                    			<input type="hidden" name="specialPartCurrencyId"/>
				                    			<input type="hidden" name="specialPartCurrencyName"/>
				                    			<input type="hidden" name="specialPartNum"/>
				                    			<input type="text"   style="width:150px"  name="specialPartAmount"  maxlength="10"  class="partNum"/>
				                    			&nbsp;&nbsp;
				                    			<label  class="specialPartNum">人数：</label>
				                    			<!-- <input type="text"  style="width:50px" name="specialPersonNum"  class="personNum"   value="${ record.baseInfo.specialPersonSum } "/> -->
				                    			<span class="specialPersonNum"   style="width:50px">${ record.baseInfo.specialPersonSum }</span>
				                    			&nbsp;&nbsp;
				                    			<input type="button" class="btn btn-primary"  name="addNewpartPrice"  value="增加"/>
				                    		</li>
				                    		<li class="special-part-price">
				                    			<label  class="specialTitle">特殊人群单价：</label>
				                    			<select name="specialPartCurrency"  style="width:150px" >
				                    				<option>请选择币种</option>
				                    				<c:forEach items="${currencyList }"  var="currency">
				                    					<option id="${currency.id }"  title="${currency.currencyMark }"   value="${currency.currencyExchangerate }">${currency.currencyName}</option>
				                    				</c:forEach>
				                    			</select>
				                    			&nbsp;&nbsp;
				                    			<label>汇率：</label>
				                    			<input type="text"   style="width:50px" class="exchangerate"     name="specialPartExchangerate"  maxlength="10"   onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)" onblur="this.v();"   value=""/>
				                    			&nbsp;&nbsp;
				                    			<label>价格：</label>
				                    			<span class="specialPartCurrencyMark"></span>
				                    			<input type="hidden" name="specialPartCurrencyId"/>
				                    			<input type="hidden" name="specialPartCurrencyName"/>
				                    			<input type="hidden" name="specialPartNum"/>
				                    			<input type="text"   style="width:150px"  name="specialPartAmount"  maxlength="10"  class="partNum"/>
				                    			&nbsp;&nbsp;
				                    			<label  class="specialPartNum">人数：</label>
				                    			<!-- <input type="text"  style="width:50px" name="specialPersonNum"  class="personNum"   value="${ record.baseInfo.specialPersonSum } "/> -->
				                    			<span class="specialPersonNum"   style="width:50px">${ record.baseInfo.specialPersonSum }</span>
				                    			&nbsp;&nbsp;
				                    			<input type="button" class="btn btn-primary"  name="addNewpartPrice"  value="增加"/>
				                    		</li>
				                    	</ul>
				                    	<div class="seach_allprice" id="price-part-div-id" >
				                    		<p>成人：${ record.baseInfo.adultSum } 人；儿童：${ record.baseInfo.childSum }  人；特殊人群：${ record.baseInfo.specialPersonSum }  人；共计：${record.baseInfo.adultSum+record.baseInfo.childSum+record.baseInfo.specialPersonSum } 人</p>
				                    		<p class="strPart"></p>
				                    	</div>
				                    </div>
		                    	<!-- 20150511 废弃，原报价代码
								<dl class="wbyu-bot" id="price-detaill-dl-id">
									<dt><label>成人单价：</label><input name="adultPrice" type="text" id="adult-price-input-id"  class="rmbp17"/>元/人 X ${record.baseInfo.adultSum }人
									<input type ="hidden" id = "adult-price-input-id-num" value="${record.baseInfo.adultSum }"/></dt>
									<dt><label>儿童单价：</label><input name="childPrice" type="text" id="child-price-input-id"   class="rmbp17"/>元/人  X ${record.baseInfo.childSum }人
									<input type ="hidden" id = "child-price-input-id-num" value="${record.baseInfo.childSum }"/></dt>
									<dt class="wbyu-bot-dtl"><label>特殊人群单价：</label><input name="specialPersonPrice" type="text" id="special-person-price-input-id" class="rmbp17" />元/人  X ${record.baseInfo.specialPersonSum }人<a class="ydbz_s" id="other-price-btn-id">增加</a>
									<input type ="hidden" id = "special-person-price-input-id-num" value="${record.baseInfo.specialPersonSum }"/></dt>
								</dl>
								<div class="seach_allprice" id="price-all-div-id">合计：<em class="gray14">¥</em><em class="red20">0</em></div>
								 -->
							</div>
							  <div class="seach25 seach100">
												<label><c:if test="${fns:getCompanyUuid() eq '049984365af44db592d1cd529f3008c3' }"><span class="xing">*</span></c:if>上传文件：</label>
									               <input type="button" name="passport"  id="uploadMoreFile" class="btn btn-primary" value="上传"  onclick="uploadFiles(' ${ctx}','passportfile',this,'false');"/>
							           			   <span id="upfileShow" class="seach_checkbox"></span>
							           			   <span class="fileLogo"></span>
						      </div>
						</form>
					</c:if>
					<%--  无用代码，因为报价不允许修改，因此已报价的项目不会再报价。所以status>=2的状态永远不会走到
					<c:if test="${reply!=null && reply.status>=2 }">
	                    <form id="reply-eprice-form-id">
		                    <div class="seach100 wbyu">
		                    	<textarea name="content" cols="" rows="" class="" readonly="readonly">${reply.content}</textarea>
		                    	<!-- 显示整体报价 -->
		                    	<input type="radio"  name="showType"  value="1"/>
		                    	<!-- 显示细分报价 -->
		                    	<input type="radio"  name="showType"  value="2"/>
								<dl class="wbyu-bot" id="price-detaill-dl-id">
									<dt><label>成人单价：</label><input name="adultPrice" type="text" id="adult-price-input-id" value="${priceDetailJson.adult.price}"  class="rmbp17" readonly="readonly"/>元/人 X ${record.baseInfo.adultSum }人
									<input type ="hidden" id = "adult-price-input-id-num" value="${record.baseInfo.adultSum }"/></dt>
									
									<dt><label>儿童单价：</label><input name="childPrice" type="text" id="child-price-input-id" value="${priceDetailJson.child.price}"    class="rmbp17" readonly="readonly"/>元/人  X ${record.baseInfo.childSum }人
									<input type ="hidden" id = "child-price-input-id-num" value="${record.baseInfo.childSum }"/></dt>
									
									<dt><label>特殊人群单价：</label><input name="specialPersonPrice" type="text" id="special-person-price-input-id" value="${priceDetailJson.specialPerson.price}"  class="rmbp17"  readonly="readonly"/>元/人  X ${record.baseInfo.specialPersonSum }人
									<input type ="hidden" id = "special-person-price-input-id-num" value="${record.baseInfo.specialPersonSum }"/></dt>
									
								</dl>
								
								<c:forEach items="${priceDetailJson.other}" var="o">
									<dl name="otherPrice" class="wbyu-bot">
										<dt><label>其它：</label><input type="text" name="title" readonly="readonly" value="${o.title}"/></dt>
										<dt><label>价格：</label><input type="text" name="price" class="rmbp17" readonly="readonly" value="${o.price}"/>元/人
										 <input type="text" name="sum" class="seach_shortinput"  value="${o.sum}" readonly="readonly"/>人</dt>
										
									</dl>
								</c:forEach>
								<div class="seach_allprice" id="price-all-div-id">合计：<em class="gray14">¥</em><em class="red20">${reply.operatorTotalPrice}</em></div>
							</div>
		                    <div class="seach25 seach100">
								<p>行程附件：</p><span class="fl"></span>
								<!-- <a class="ydbz_s">预览</a> -->	
								<c:if test="${upRelayFileList.size()>=1}">
								 	    	  <br/>
								 	    	  <c:forEach items="${upRelayFileList}" var="file">
										 	  <div><strong>&nbsp;&nbsp;&nbsp;	·</strong>${file.fileName}</div>
										 	   </c:forEach>
								 	   	     <a class="ydbz_s" href="${ctx}/sys/docinfo/zipdownload/${upRelayFileDocIds}/aditFiles">下载</a>
								 	   </c:if>
								 	  <c:if test="${upRelayFileList.size()==0}">
								 	   (无)
								 	   </c:if>
							
							</div>
						</form>
					</c:if>
				 --%>
				<div class="dbaniu">
					<a class="ydbz_s gray" name="history-back">返回</a>
					<c:if test="${reply==null || reply.status<2 }">
						<input type="button" class="btn btn-primary" id="reply-eprice-btn-id" value="报价" />
					</c:if>
				</div>
                <!--右侧内容部分结束-->
      
	
</body>
</html>