<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>
<c:if test="${shenfen=='qianwu' }">
	订单-签证-签证订单详情
</c:if>
<c:if test="${shenfen=='xiaoshou' }">
	订单-签证-销售订单详情
</c:if>
</title>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 modified by Tlw--%>
<link type="text/css" rel="stylesheet" href="${ctxStatic }/jqueryUI/themes/base/jquery.ui.all.css" />
<link href="${ctxStatic }/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css" rel="stylesheet" type="text/css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic }/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic }/css/jquery.validate.min.css" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic }/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic }/js/jquery-migrate-1.js"></script>
<script src="${ctxStatic }/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic }/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic }/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic }/js/manageorder.js"></script>
<script type="text/javascript" src="${ctxStatic }/js/common.js"></script>

<style type="text/css">
input[disabled],select[disabled],textarea[disabled],input[readonly],select[readonly],textarea[readonly],
input[disabled]:focus,select[disabled]:focus,textarea[disabled]:focus,input[readonly]:focus, select[readonly]:focus,textarea[readonly]:focus{
    cursor:auto;
    background:transparent;
    border:0px;
    box-shadow:inset 0 0px 0px rgba(0,0,0,0.075)
}
.uploadlong {
	display: block;
	width: 6cm;
	overflow: hidden;
	white-space: nowrap;
	text-overflow: ellipsis;
}

.ellipsis {
	overflow: hidden;
	white-space: nowrap;
	text-overflow: ellipsis;
	padding-right:10px;
}

</style>

<script type="text/javascript">

	function downloadInterviewNotice(visaOrderId,travelerId){
		var href = "${ctx}/visa/order/downloadInterviewNotice?orderId="+visaOrderId+"&travelerId="+travelerId;
		window.open(href);
	}
	
function downloads(docIds,fileName){
	 
	 var doc = docIds.replace(/[ ]/g,"");
		if(doc==null || doc==""){
			$.jBox.tip("文件不存在");
			return;
		}
		var fileUrl =encodeURI("${ctx}/sys/docinfo/fileExists/" + doc + "/"+fileName);
		
		$.ajax({ url:encodeURI(fileUrl) ,async:false, success: function(result){
			
			if("文件存在"==result){
				  window.open(encodeURI(encodeURI("${ctx}/sys/docinfo/zipdownload/" + doc + "/"+fileName)));
			  } else{
				  $.jBox.tip("文件不存在");
			  }
	      }});
		
	}
	
	function viewdetail(uuid,verifyStatus){
		verifyStatus = '-2';
		window.open("${ctx}/invoice/limit/supplyviewrecorddetail/" + uuid + "/" + verifyStatus);
	}
	function viewdetail4Receipt(uuid,verifyStatus,orderType){
		verifyStatus = '-2';
		window.open("${ctx}/receipt/limit/supplyviewrecorddetail/" + uuid + "/" + verifyStatus + "/" + orderType);
	}
	
	$(function(){
		//AA码
		AAHover();
	
		//各块信息展开与收起
		$(".ydClose").click(function(){
			var obj_this = $(this);
			if(obj_this.attr("class").match("ydExpand")) {
				obj_this.removeClass("ydExpand");
				obj_this.parent().nextAll("[flag=messageDiv]").eq(0).hide();
			} else {
				obj_this.addClass("ydExpand");
				obj_this.parent().nextAll("[flag=messageDiv]").eq(0).show();
			}
		});
		//证件类型
		$("input[name=papersType]").live("click",function(){			
			var $this = $(this);
			var $siblingsCkb = $this.parents(".tourist-ckb").children("input[type=checkbox]");
			var thisIndex = $siblingsCkb.index($this);
			var $tips = $this.parents(".tourist-info1").siblings(".zjlx-tips").eq(0);
			if($this.attr('checked')) {
				if(!$tips.is(":visible")) {
					$tips.show();
				}
				$tips.children("ul").eq(thisIndex).show();
			} else {
				$tips.children("ul").eq(thisIndex).hide(500,function() {
					var isshow = 0;
					$tips.children("ul").each(function(index, element) {
	                    if($(element).is(":visible")){
							isshow++;
						}
	                });
					if(0 == isshow) {
						$tips.hide();
					}
				});
				
			}
		});
		
		$("input[name='tempOfPapersType']").each(function(index, obj) {
			var papersType = $(this).val();
			$("[name='papersType']", $(this).parent()).each(function() {
				if(papersType && papersType.indexOf($(this).val()) != -1) {
					$(this).attr("checked", true);
					$(this).trigger("click").trigger("click");
				}
			});
			var idCard = $(this).next("input").val();
			if(idCard) {
				var value = idCard.split(",");
				$("input[name='idCard']:visible", $(this).parent().parent().next("div")).each(function(index, obj) {
					$(this).val(value[index]);
				});
			}
		});
	});

	//得到焦点事件：隐藏填写费用名称提示
	function payforotherIn(doc) {
	    var obj = $(doc);
	    obj.siblings(".ipt-tips2").hide();
	}
	
	//失去焦点事件：如果输入框中没有值，则提示填写费用名称
	function payforotherOut(doc){
	    var obj = $(doc);
	    if(!obj.val()){
	        obj.siblings(".ipt-tips2").show();
	    }
	}
	
	//点击提示错误信息中 "修改" 后错误输入框得到焦点
	function focusIpt(doc){
	    $(doc).parent().find('input[type=text].ipt2').trigger("focus");
	}
	
	
	//签证借款申请 ---------wxw added--------
	function jbox_sqjk() {
		var html = '<div style="margin-top:20px;"><label class="jbox-label">借款金额：</label>';
		html += '<input name="borrowAmount" type="text" /><br /><label class="jbox-label">备注：</label><textarea name="borrowRemark" cols="" rows="" ></textarea>';
		html += '</div>';
		$.jBox(html, { title: "借款",buttons:{'申请借款': true}, submit:function(v, h, f){
			$.ajax({
				  type: "POST",
				  url:  "${ctx}/visa/workflow/borrowmoney/createVisaJK",
				  dataType: "json",
				  data : {
						"travelerID" : travelerID,
		                "borrowAmount" : f.borrowAmount,
		                "borrowRemark" : f.borrowRemark},
				  async: false,
				  success:function(msg){
					  top.$.jBox.tip(msg.visaJKreply);
				  }
				});
		},height:220,width:380});
	}

	function modifyDetail(visaOrderId) {
		window.location.href = "${ctx}/log/order/list?orderId="+visaOrderId;
	}
	

</script>


</head>

<body>


<page:applyDecorator name="show_head">
        <page:param name="desc">签证订单详情</page:param>
</page:applyDecorator>

<c:if test="${shenfen=='qianwu' }">
	<shiro:hasPermission name="visaOrderForOp:agentinfo:visibility">
		<input type="hidden" value="1" id="agentinfo_visibility">
		<c:set var="agentinfo_visibility" value="1"></c:set>
	</shiro:hasPermission>
</c:if>
<c:if test="${shenfen=='xiaoshou' }">
	<shiro:hasPermission name="visaOrderForSale:agentinfo:visibility">
		<input type="hidden" value="1" id="agentinfo_visibility">
		<c:set var="agentinfo_visibility" value="1"></c:set>
	</shiro:hasPermission>
</c:if>

<div class="mod_nav">订单 > 签证 > 订单详情</div>
<div class="ydbzbox fs">
	<div class="tr">
		<a href="#" class="dyzx-add" onclick="modifyDetail(${visaOrder.id })">修改记录</a>
		<c:if test="${productOrder!=null }">
			<a href="#" class="dyzx-add" onclick="downloads('${activityGroup.openDateFile }','出团通知书')">下载出团通知书</a>
			<a href="#" class="dyzx-add" onclick="downloads('${productOrder.confirmationFileId}','确认单')">下载确认单</a> 
			
		</c:if>
	</div>
	<div class="ydbz_tit">订单详情</div>
	<ul class="ydbz_info ydbz_infoli25">
		<li>
			<span>销售：</span>${visaOrder.salerName }
		</li>
		<li><span>下单时间：</span><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaOrder.createDate }"/></li>
		<li><span>团队类型：</span>
			<c:if test="${productOrder==null }">单办签</c:if>
			<c:if test="${productOrder!=null }">
				<c:if test="${productOrder.orderStatus==1 }">单团</c:if>
				<c:if test="${productOrder.orderStatus==2 }">散拼</c:if>
				<c:if test="${productOrder.orderStatus==3 }">游学</c:if>
				<c:if test="${productOrder.orderStatus==4 }">大客户</c:if>
				<c:if test="${productOrder.orderStatus==5 }">自由行</c:if>
				<c:if test="${productOrder.orderStatus==6 }">签证</c:if>
				<c:if test="${productOrder.orderStatus==7 }">机票</c:if>
			</c:if>
		</li>
		<li><span>收客人：</span>${visaOrder.createBy.name }</li>
		<li><span>订单编号：</span>${visaOrder.orderNo }</li>
		<!-- C460V3 所有批发商团号取自产品团号 -->
		<!--  C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-s-订单->签务签证订单/销售签证订单->详情页 -->
		<c:if test="${fns:getUser().company.uuid eq '7a816f5077a811e5bc1e000c29cf2586'}"><!-- 环球行 -->
		  <li><span>订单团号：</span>${visaOrder.groupCode }</li>
		</c:if>
		<c:if test="${fns:getUser().company.uuid ne'7a816f5077a811e5bc1e000c29cf2586'}"><!-- 非环球行 -->
		  <li><span>团号：</span>${visaProduct.groupCode }</li>
		</c:if>
        <!--  C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-e -->
		
		<c:if test="${productOrder!=null }">
		<li><span>参团订单编号：</span>${visaOrder.activityCode }</li>
		<li><span>参团团号：</span>${activityGroup.groupCode }</li>
		</c:if>
		<li><span>订单总额：</span>${totalMoney }</li>
		<li><span>订单状态：</span>
			<c:if test="${visaOrder.payStatus==1 }">未收款</c:if>
			<c:if test="${visaOrder.payStatus==3 }">预定</c:if>
			<c:if test="${visaOrder.payStatus==5 }">已收款</c:if>
			<c:if test="${visaOrder.payStatus==99 }">已取消</c:if>
		</li>
		<li><span>操作人：</span>${visaProduct.createBy.name }</li>
		<li><span>办签人数：</span>${visaOrder.travelNum }人</li>
		<li>
			<span>下单人：</span>${visaOrder.createBy.name }
		</li>
	</ul>
	<!-- C482 begin -->
	<p class="ydbz_mc"></p>
	<ul class="ydbz_info ydbz_infoli25">
		<li style="height:auto">
			<span style="vertical-align: top;">收据号：</span>
			<span><c:forEach items="${receiptList }" var="receipt" ><div><a onClick="viewdetail4Receipt('${receipt.uuid}','-2','${receipt.orderType}')">${receipt.invoiceNum }</a></div></c:forEach></span>
		</li>
		<li style="height:auto">
			<span style="vertical-align: top;">发票号：</span>
			<span><c:forEach items="${invoiceList }" var="invoice" ><div><a onClick="viewdetail('${invoice.uuid}','-2')">${invoice.invoiceNum }</a></div></c:forEach></span>
		</li>
	</ul>
	<!-- C482 end -->	
   <div class="ydbz_tit">产品信息</div>
               <div class="orderdetails2">
                  <p class="ydbz_mc">${visaProduct.productName }</p>
             	  <ul class="ydbz_info">
                    <li><span>产品编号：</span>${visaProduct.productCode }</li>
                    <li><span>签证国家：</span>${country.countryName_cn }</li>
                    <li><span>签证类别：</span>${visaType.label }</li>
                    <li><span>领区：</span>	<c:if test="${not empty visaProduct.collarZoning }">
				                           ${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}
			                        	</c:if></li>
                     <c:if test="${visaCostPriceFlag eq 1}">
                     <li><span>成本价格：</span>${currency.currencyMark }<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${visaProduct.visaPrice}" />/人</li>
                     </c:if>
                     <li><span>应收价格：</span>${currency.currencyMark }<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${visaProduct.visaPay}" />/人</li>
	    			<li><span>创建时间：</span><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaProduct.createDate }"/></li>
                  </ul>
                </div>
	<div class="ydbz_tit">预订人信息</div>
	<div flag="messageDiv">
		<p class="ydbz_qdmc">预订渠道：${agentInfo.agentName }</p>
			<c:if test="${fns:getUser().id eq visaOrder.createBy.id or fns:getUser().id eq visaOrder.salerId or (not empty agentinfo_visibility and agentinfo_visibility eq 1) }">
			<c:forEach items="${contacts }" var="contact" varStatus="status">
                    <ul class="ydbz_qd">
                      <li><label>渠道联系人<font>${status.index+1 }</font>：</label>${contact.contactsName}</li>
                      <li class="ydbz_qd_lilong"><label>渠道联系人电话：</label>${contact.contactsTel}<div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div></li>
                      <li flag="messageDiv" class="ydbz_qd_close">
                       <ul>
                         <li><label>固定电话：</label>${contact.contactsTixedTel }</li>
                         <li><label>渠道地址：</label>${contact.contactsAddress }</li>
                         <li><label>传真：</label>${contact.contactsFax }</li>
                         <li><label>QQ：</label>${contact.contactsQQ }</li>
                         <li><label>Email：</label>${contact.contactsEmail }</li>
                         <li><label>渠道邮编：</label>${contact.contactsZipCode }</li>
                         <li><label>其他：</label>${contact.remark }</li>
                       </ul>
                      </li>
                    </ul>
              </c:forEach>
              </c:if>
	</div>
	
	<!-- 112展示备注 -->
	   <div id="manageOrder_m">
                        <div id="contact">
                            <div class="ydbz_tit">特殊需求</div>
                            <div class="ydbz2_lxr" flag="messageDiv">
                                <form class="contactTable">
                                    <table width="80%">
                                        <tr>
                                        <td width="90px"style="vertical-align:top;text-align:right">特殊需求：</td>
                                        <!--<textarea style="display: none;" class="disabledClass" id="specialDemand"
                                                  name="specialDemand" maxlength="100">需要接机</textarea>-->
                                                  <td width="80%"
                                            class="disabledshowspan"style="word-break:break-all">${visaOrder.remark }</td>
                                             </tr>
                                    </table>
                                </form>
                            </div>
                        </div>
         </div>
	
	
	<div  id="manageOrder_new" >
		<div class="ydbz_tit orderdetails_titpr">游客信息<a class="ydbz_x" href="${ctx}/visa/order/downloadTraveler?visaOrderId=${visaOrder.id}&agentId=${agentInfo.id }&groupCode=${visaOrder.groupCode }">下载游客信息</a></div>
		<!--<div class="warningtravelerNum">暂无游客信息</div>-->
		<c:forEach items="${travelers }" var="traveler" varStatus="status">
		<div id="traveler">
			<form class="travelerTable">
				<div class="tourist">
					<!--转团信息开始--><%--
                                <div class="tourist-t tourist-zt">
                                	<b class="tourist-t-onb">已转团</b>
                                	<div class="tourist-t-r">
                                    	<label>团号：dt690725</label>
                                        <label>单团</label>
                                        <label>美国纽约一地6天五晚</label>
                                        <label>操作人：王海</label>
                                    </div>
                                </div>
                                --%><!--转团信息结束-->
                    
					<div class="tourist-t">
						<!--<a name="deleteTraveler" style="cursor:pointer;" class="btn-del">删除</a>-->
                                    	
						<input type="hidden" class="traveler" value="${traveler.visa.id }" name="id">
						<input type="hidden" class="traveler" value="${traveler.id }" name="travelerId">
						<span onclick="boxCloseOnAdd(this,'1')" class="add_seachcheck"></span>
                               <c:if test="${traveler.mainOrderId!=null }"><b class="tourist-t-onb">已参团</b> </c:if>    <label class="ydLable">游客<em class="travelerIndex" style="vertical-align: baseline;">${status.index+1 }</em>:</label>
						<div class="tourist-t-off">
							<span class="fr">押金：<span class="gray14"></span><span class="ydFont2">${traveler.visa.totalDepositMoney }</span>　　
							<c:choose>
								<c:when test="${empty traveler.payPriceSerialNumInfo}">
									  结算价：<span name="clearPrice" class="ydFont2">
	                          		<c:forEach items="${traveler.currencies }" var="currency" varStatus="status1">	
	                          		<c:choose>
	                          			<c:when test="${status1.last }">
	                          				${currency.currencyName}:${currency.convertCash}
	                          			</c:when>
	                          			<c:otherwise>
	                          				${currency.currencyName}:${currency.convertCash}&nbsp;+&nbsp;
	                          			</c:otherwise>
	                          		</c:choose>													
	                          		</c:forEach>                     			 
	                      			 </span> 
	                      		</c:when>
	                      		<c:otherwise>
	                      			结算价：<span id="jsjyd" class="ydFont2">${traveler.payPriceSerialNumInfo}</span>
	                      		</c:otherwise>
	                      	</c:choose> 
	                      			 </span>
							<em>${traveler.name }</em><label>签证状态：
														<c:if test="${traveler.visa.visaStauts==-1 }">无</c:if>
														<c:if test="${traveler.visa.visaStauts==null}">无</c:if>
														<c:if test="${traveler.visa.visaStauts==0 }">未送签</c:if>
														<c:if test="${traveler.visa.visaStauts==1 }">送签</c:if>
														<c:if test="${traveler.visa.visaStauts==2 }">已约签</c:if>
														<c:if test="${traveler.visa.visaStauts==3 }">通过</c:if>
														<c:if test="${traveler.visa.visaStauts==4 }">未约签</c:if>
														<c:if test="${traveler.visa.visaStauts==5 }">已撤签</c:if>
														<c:if test="${traveler.visa.visaStauts==7 }">拒签</c:if>
														<c:if test="${traveler.visa.visaStauts==8 }">调查</c:if>
														<c:if test="${traveler.visa.visaStauts==9 }">续补资料</c:if>
													<a onclick="downloadInterviewNotice('${visaOrder.id }','${traveler.id }')" class="ydbz_xwt">下载面签通知</a></label>
						</div>
                                    <div class="tourist-t-on">
								<label>
									<c:if test="${traveler.personType==1 }">成人</c:if>
									<c:if test="${traveler.personType==2 }">儿童</c:if>
									<c:if test="${traveler.personType==3 }">特殊人群</c:if>
								</label>
							</div>
					</div>
					
					<div class="tourist-con" flag="messageDiv">
						<!--游客信息左侧开始-->
						<div class="tourist-left">
						<div class="ydbz_tit ydbz_tit_child"><em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>基本信息</div>
							<ul class="tourist-info1 clearfix" flag="messageDiv">
								<li><label class="ydLable">姓名：</label>${traveler.name }</li>
								<li><label class="ydLable">英文／拼音：</label><span class="fArial">${traveler.nameSpell }</span></li>
								<li><label class="ydLable">性别：</label><c:if test="${traveler.sex==1 }">男</c:if><c:if test="${traveler.sex==2 }">女</c:if></li>
								<!--  <li><label class="ydLable">国籍：</label>-->
								<!--<c:forEach items="${countrys }" var="con" varStatus="sta">
									<c:if test="${con.id==traveler.nationality }">${con.countryName_cn }</c:if>
								</c:forEach>
								</li>
								-->
								<li><label class="ydLable">出生日期：</label><span class="fArial"><fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.birthDay }"/></span></li>
								<li><label class="ydLable">联系电话：</label><span class="fArial">${traveler.telephone }</span></li>
								<li><label class="ydLable">护照号：</label><span class="fArial">${traveler.passportCode }</span></li>
								<li><label class="ydLable">护照签发日期：</label><span class="fArial"><fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.issuePlace }"/></span></li>
								<li><label class="ydLable">护照有效期：</label><span class="fArial"><fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.passportValidity }"/></span></li>
								<!--  <li><label class="ydLable">身份证号：</label><span class="fArial">${traveler.idCard }</span></li> -->
                                <li><label class="ydLable">护照类型：</label><c:if test="${traveler.passportType==1 }">因公护照</c:if><c:if test="${traveler.passportType==2 }">因私护照</c:if></li>
                              <c:if test="${fns:getUser().company.uuid eq '980e4c74b7684136afd89df7f89b2bee'}">   <li><label class="ydLable">签发地：</label><span class="fArial">${traveler.issuePlace1 }</span></li></c:if>
							</ul>
							<div class="ydbz_tit ydbz_tit_child"><em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>签证信息</div>
							<ul flag="messageDiv">
								<div class="ydbz_tit ydbz_tit_child">申请办签</div>
								<div class="ydbz_scleft">
									<table style="width:98%; line-height:40px;margin-left: 32px; margin-bottom:50px;" cellspacing="10" cellpadding="10">
										<tbody>
											<tr> 
												<td>产品编号：${visaProduct.productCode }</td>
												<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;领区：
												<c:if test="${not empty visaProduct.collarZoning }">
				                           ${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}
			                        	</c:if>
												</td>
												<td>&nbsp;&nbsp;&nbsp;签证国家：${country.countryName_cn }</td>
											</tr>
											<tr> 
												<td>签证类别：${visaType.label }</td>
												<td colspan="1">签证状态：   
														<c:if test="${traveler.visa.visaStauts==null }">无</c:if>
												        <c:if test="${traveler.visa.visaStauts==-1 }">无</c:if>
														<c:if test="${traveler.visa.visaStauts==0 }">未送签</c:if>
														<c:if test="${traveler.visa.visaStauts==1 }">送签</c:if>
														<c:if test="${traveler.visa.visaStauts==2 }">已约签</c:if>
														<c:if test="${traveler.visa.visaStauts==3 }">通过</c:if>
														<c:if test="${traveler.visa.visaStauts==4 }">未约签</c:if>
														<c:if test="${traveler.visa.visaStauts==5 }">已撤签</c:if>
														<c:if test="${traveler.visa.visaStauts==7 }">拒签</c:if>
														<c:if test="${traveler.visa.visaStauts==8 }">调查</c:if>
														<c:if test="${traveler.visa.visaStauts==9 }">续补资料</c:if> 
												    <!--<c:if test="${traveler.visa.visaStauts==1 }">送签</c:if>
														<c:if test="${traveler.visa.visaStauts==2 }">约签</c:if>
														<c:if test="${traveler.visa.visaStauts==3 }">出签</c:if>
														<c:if test="${traveler.visa.visaStauts==4 }">申请撤签</c:if>
														<c:if test="${traveler.visa.visaStauts==5 }">撤签成功</c:if>
														<c:if test="${traveler.visa.visaStauts==6 }">撤签失败</c:if>
														<c:if test="${traveler.visa.visaStauts==7 }">拒签</c:if>
														<c:if test="${traveler.visa.visaStauts==8 }">送签中</c:if>
														<c:if test="${traveler.visa.visaStauts==9 }">销签</c:if>-->
													<a class="ydbz_x fn" onclick="downloadInterviewNotice('${visaOrder.id }','${traveler.id }')">下载面签通知</a>
												</td>
												<td>
										       <!-- 懿洋假期的美国签证隐藏 制表人姓名-s-->
										    	<c:if test="${!(fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' && country.countryName_cn=='美国')}"> 
													制表人姓名：${traveler.visa.makeTable }
												</c:if>
											    <!-- 懿洋假期的美国签证隐藏 制表人姓名-e-->
											    </td>
											</tr>
											<tr><td colspan="3"><div class="mod_information_d7"></div></td></tr>
											<tr> 
												<td>预计出团时间：<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.visa.forecastStartOut }"/></td>
												<td>预计约签时间：<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.visa.forecastContract }"/></td>
												<!-- 0211-qyl-begin-新增回团日期的显示,只有星徽四海才会显示 -->
												 <c:if test="${fns:getUser().company.uuid == '0e19ac500f78483d8a9f4bb768608629'}">
												<td>预计回团时间：<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.visa.forecastBackDate }"/></td>
												 </c:if>
												<!-- 0211-qyl-end -->
												
											</tr>
											<tr> 
												<td class="touristNeed"><!-- <span class="xing">*</span> -->&nbsp;&nbsp;&nbsp;实际出团时间：<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.visa.startOut }"/></td>
												<!-- 懿洋假期的美国签证隐藏实际约签时间-s-->
												<c:if test="${!(fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' && country.countryName_cn=='美国')}">
												<td  class="touristNeed"><!-- <span class="xing">*</span> -->&nbsp;&nbsp;&nbsp;实际约签时间：<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${traveler.visa.contract }"/> </td>
											    </c:if>
											    <!-- 懿洋假期的美国签证隐藏实际约签时间-e-->
											    
											</tr>
											<tr> 
												<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;AA码：${traveler.visa.AACode }</td>
												<%-- <td>制表人姓名：${traveler.visa.makeTable }</td> --%>
												<!-- c482需求新增UID编号-s-->
												<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;UID编号：${traveler.visa.UIDCode }</td>
												<!-- c482需求新增UID编号-e-->
											</tr>
										</tbody>
									</table>
								</div>
                                            <div class="ydbz_tit ydbz_tit_child">办签资料</div>
							
									<ul class="seach25 seach100 ydbz_2uploadfile">
									<!-- ****************197-start -->
                                     <p>资料原件：</p>              
                                     <input type="checkbox" name="original_Project_Type" value="0" <c:if test="${fn:contains(visaProduct.original_Project_Type,'0')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">护照</label></span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="1" <c:if test="${fn:contains(visaProduct.original_Project_Type,'1')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">身份证</label>
                                     </span>
                                      <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="3" <c:if test="${fn:contains(visaProduct.original_Project_Type,'3')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">电子照片</label>
                                     </span>
                                      <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="4" <c:if test="${fn:contains(visaProduct.original_Project_Type,'4')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">申请表格</label>
                                     </span>
                                      <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="5" <c:if test="${fn:contains(visaProduct.original_Project_Type,'5')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">户口本</label>
                                     </span>
                                      <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="6" <c:if test="${fn:contains(visaProduct.original_Project_Type,'6')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">房产证</label>
                                     </span>
                                     <span class="seach_check">
                                      <input type="checkbox" name="original_Project_Type" value="2" <c:if test="${fn:contains(visaProduct.original_Project_Type,'2')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">其他</label><input type="text" name="original_Project_Name" value="${visaProduct.original_Project_Name}" class="input-mini"  disabled="disabled" ></span><br/>
                                     <p>复印件：</p> 
                                     <input type="checkbox" name="copy_Project_Type" value="3"   <c:if test="${fn:contains(visaProduct.copy_Project_Type,'3')}">checked="checked"</c:if> id=""   disabled="disabled"  ><label for="">护照</label></span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="4"   <c:if test="${fn:contains(visaProduct.copy_Project_Type,'4')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">身份证</label>
                                     </span>
                                      <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="5"   <c:if test="${fn:contains(visaProduct.copy_Project_Type,'5')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">电子照片</label>
                                     </span>
                                      <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="6"   <c:if test="${fn:contains(visaProduct.copy_Project_Type,'6')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">申请表格</label>
                                     </span>
                                      <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="0"   <c:if test="${fn:contains(visaProduct.copy_Project_Type,'0')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">户口本</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="1"   <c:if test="${fn:contains(visaProduct.copy_Project_Type,'1')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">房产证</label>
                                     </span>
                                     <span class="seach_check">
                                      <input type="checkbox" name="copy_Project_Type"  value="2" <c:if test="${fn:contains(visaProduct.copy_Project_Type,'2')}">checked="checked"</c:if> id=""  disabled="disabled" ><label for="">其他</label><input type="text" name="copy_Project_Name" value="${visaProduct.copy_Project_Name}" class="input-mini"  disabled="disabled" ></span></ul>
                                    <!-- ****************197-end -->
                              </ul>

 

							<div class="ydbz_tit ydbz_tit_child">
							<!-- 
								<p class="tourist-ckb">
									<label class="ydLable">资料原件：</label>
									<c:if test="${fn:contains(visaProduct.original_Project_Type, '0') }">
									<c:if test="${fn:contains(traveler.visa.signOriginalProjectType, '0') }">护照</c:if>
									</c:if>
									<c:if test="${fn:contains(visaProduct.original_Project_Type, '1') }">
									<c:if test="${fn:contains(traveler.visa.signOriginalProjectType, '1') }">身份证</c:if>
									</c:if>
									<c:if test="${fn:contains(visaProduct.original_Project_Type, '2') }">
									<c:if test="${fn:contains(traveler.visa.signOriginalProjectType, '2') }">${visaProduct.original_Project_Name }</c:if>

									</c:if>
								</p>
								<p class="tourist-ckb">
									<label class="ydLable">复印件：</label>
									<c:if test="${fn:contains(visaProduct.copy_Project_Type, '0') }">
									<c:if test="${fn:contains(traveler.visa.signCopyProjectType, '0') }">户口本</c:if>
									</c:if>
									<c:if test="${fn:contains(visaProduct.copy_Project_Type, '1') }">
									<c:if test="${fn:contains(traveler.visa.signCopyProjectType, '1') }">房产证</c:if> 
									</c:if>
									<c:if test="${fn:contains(visaProduct.copy_Project_Type, '2') }">
									<c:if test="${fn:contains(traveler.visa.signCopyProjectType, '2') }">${visaProduct.copy_Project_Name }</c:if> 
									</c:if>
								</p>
								-->
								<p class="tourist-ckb">
									<label class="ydLable">护照状态：</label>
									    <c:if test="${traveler.passportStatus==0}">无</c:if>
								        <c:if test="${traveler.passportStatus==null}">无</c:if>
									    <c:if test="${traveler.passportStatus==1}">借出</c:if>
										<c:if test="${traveler.passportStatus==2}">销售已领取</c:if>
										<!--
										<c:if test="${traveler.passportStatus==3}">未签收</c:if>
										<c:if test="${traveler.passportStatus==4}">已签收</c:if>
										-->
										<c:if test="${traveler.passportStatus==4}">已还</c:if>
										<c:if test="${traveler.passportStatus==5}">已取出</c:if>
										<c:if test="${traveler.passportStatus==6}">未取出</c:if>
										<!--<c:if test="${traveler.passportStatus==7}">走团</c:if>-->
										<c:if test="${traveler.passportStatus==8}">计调领取</c:if>
									
								</p>
								
							</div>
							
							<c:if test="${shenfen=='qianwu' }">
								<c:if test="${traveler.visa.docIds!=null && traveler.visa.docIds!=''}">
									<div class="ydbz_tit ydbz_tit_child">下载资料<a class="ydbz_x" onclick="downloads('${traveler.visa.docIds }','全部下载')" >全部下载</a></div>
									<ul class="ydbz_2uploadfile ydbz_scleft lh180">
									<c:forEach items="${traveler.visa.docs }" var="doc" varStatus="status">
										<li class="seach25 seach33"><p class="uploadlong" title="${doc.docName}">${doc.docName}</p></li>
									</c:forEach>
									</ul>
								</c:if>
							</c:if>
							
							
							<!-- test position-tgy -->
							<div class="ydbz_tit ydbz_tit_child"><em class="ydExpand"></em>备注：</div>
							<ul class="textareaulpad">
								<%-- ${traveler.remark } --%>
								${traveler.visa.passportOperateRemark }
  								</ul>
							
						</div>
						<!--游客信息左侧结束-->
						
						<!--游客信息右侧开始-->
						<div class="tourist-right">
							<div class="clearfix">
								<ul class="tourist-info2">
									<c:forEach items="${traveler.costChange }" var="costChange" varStatus="status">
									<li class="tourist-info2-first">
										<label class="ydLable2 ydColor1">${costChange.costName}：</label>${costChange.priceCurrency.currencyMark} <fmt:formatNumber  type="currency" pattern="#,##0.00" value="${costChange.costSum}" />
									</li>
									</c:forEach>
								</ul>
							</div>
							<div class="yd-line"></div>
							<div class="clearfix">
								<div class="yd-w90 yd-total tr fr">
									<label class="ydLable2">结算价：</label>
									<c:choose>
										<c:when test="${empty traveler.payPriceSerialNumInfo}">
											<span name="clearPrice" class="ydFont2">
	                        				<input type="hidden"   name="start">
	                          				<c:forEach items="${traveler.currencies }" var="currency" varStatus="status">
	                          				<div name="inputClearPriceDiv">
												${currency.currencyName}:${currency.convertCash}
											<input type="hidden" value="${currency.id}" alt="${currency.currencyName}" name="xxxyy" />
											<input type="hidden" value="${currency.convertCash}"  name="inputClearPrice" />
							    			</div><br>
	                          				</c:forEach>
											<input type="hidden"  name="end">
	                      			 		</span>
										</c:when>
										<c:otherwise>
											<span class="ydFont2">${traveler.payPriceSerialNumInfo}</span>
										</c:otherwise>
									</c:choose>
										                       <c:if test="${traveler.rebatesMoneySerialNum != null&&''!=traveler.rebatesMoneySerialNum }">
		                       <div class="clearfix1">
									<div class="traveler-rebatesDiv" >
									 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
								       <c:choose>
								      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
												<label class="ydLable2 ydColor1">预计个人宣传费：</label>
										</c:when>
								         <c:otherwise>
								         		<label class="ydLable2 ydColor1">预计个人返佣：</label>
								          </c:otherwise>
										 </c:choose>   
										
										
										

         				 <c:forEach items="${currencyList4Rebates}" var="cu">
									   <c:if test="${cu.id==traveler.rebatesCurrencyID}">
									    <lable>${cu.currencyName}</label>
									   </c:if> 
						</c:forEach>
	<input type="text" readonly="readonly" maxlength="9" style="width: 40px;" name="rebatesAmount" class="ipt-rebates" 
				               			value="${traveler.rebatesAmount}" id="visaTravelerRebateAmount"/>
									</div>
								</div>
	                       </c:if> 
	                      			 
								</div>
								
								
								<div class="yd-w90 yd-total fr">
								<c:if test="${traveler.borrowMoney!=null}">
						            <label class="ydLable2">
						                    <c:if test="${traveler.borrowMoneyCheckStatus==0}">
						                    	已驳回
						                    </c:if>
						                    <c:if test="${traveler.borrowMoneyCheckStatus==1}">
						                        借款审批中
						                    </c:if>
						                    <c:if test="${traveler.borrowMoneyCheckStatus==2}">
						                        借款审核通过
						                    </c:if>
						            </label><br />
						            
									<label class="ydLable2">借款金额：</label>¥<span class="ydFont2">${traveler.borrowMoney}</span><br />
							   </c:if>

									<label class="ydLable2">备注：</label>
									<div class="yd-w90 tl fr">${traveler.visa.remark }</div><br />
									<label class="ydLable2">押金：</label>${traveler.visa.totalDepositMoney }
								</div>
							</div>
						</div>
						
						<!--游客信息右侧结束-->
						
					</div>
				</div>
			</form>
		</div>
		</c:forEach>
		<div class="clearfix2">
			<div class="traveler-rebatesDiv" >
				<label class="ydLable2 ydColor1" style="width: 100px;">
				 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
		       <c:choose>
		      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
						<label class="ydLable2 ydColor1">	预计团队宣传费：</label>
				</c:when>
		         <c:otherwise>
		         		<label class="ydLable2 ydColor1">预计团队返佣：</label>
		          </c:otherwise>
				 </c:choose>   
				
			
				
				</label>
				
					<c:forEach items="${currencyList4Rebates }" var="cu">
						<c:if test="${cu.id==groupRebatesCurrency}">
									    <lable>${cu.currencyName}</label>
						</c:if> 
					</c:forEach>

				<input type="text" readonly="readonly" class="required ipt-rebates" maxlength="15" id="groupRebatesMoney" name="groupRebatesMoney" value="${groupRebatesMoney }" >
				
			</div>
		</div>
		<div class="ydbz_tit">收款信息</div>
		<c:forEach items="${orderPays }" var="orderPay" varStatus="status">
		<p class="orderdetails6">
		   <span style="width: 14%" class="ellipsis" title="${orderPay.remarks}">游客姓名：${orderPay.remarks}</span> <%-- 使用orderpay的备注字段remarks来显示游客名称--%>
		   <span style="width: 14%">收款方式：<c:if test="${orderPay.payType==1 }">支票支付</c:if>
		   <c:if test="${orderPay.payType==2 }">POS支付</c:if>
		   <c:if test="${orderPay.payType==3 }">现金支付</c:if>
		   <c:if test="${orderPay.payType==4 }">汇款支付</c:if>
		   <c:if test="${orderPay.payType==5 }">快速支付</c:if>
		   <c:if test="${orderPay.payType==6 }">银行转账</c:if>
		   <c:if test="${orderPay.payType==7 }">汇票</c:if>
		   <c:if test="${orderPay.payType==8 }">POS机刷卡</c:if>
		   <c:if test="${orderPay.payType==9 }">因公支付宝</c:if>
		   </span>
		   <span style="width: 14%">收款类型：
		            <c:if test="${orderPay.payPriceType==1 }">收全款</c:if>
		   			<c:if test="${orderPay.payPriceType==2 }">收尾款</c:if>
		   			<c:if test="${orderPay.payPriceType==3 }">收订金</c:if>
		   			<c:if test="${orderPay.payPriceType==7 }">收押金</c:if>
		   			<c:if test="${orderPay.payPriceType==16 }">收押金</c:if>
		   </span>
		   <span style="width: 14%">收款金额：${orderPay.moneyAmount}</span>
		   <span style="width: 14%">收款时间： <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${orderPay.createDate }"/></span>
		   <span style="width: 14%">收款凭证：<a lang="4453" class="showpayVoucher" onclick="downloads('${orderPay.payVoucher }','收款凭证')" >收款凭证</a></span>
		   <span style="width: 14%">是否到账：
							  <c:choose>
							    <c:when test="${empty orderPay.isAsAccount}">
									未到账
							    </c:when>
							    <c:when test="${orderPay.isAsAccount == 0}">
									未到账
							    </c:when> 
							     <c:when test="${orderPay.isAsAccount == 99}">
									未到账
							    </c:when>  
							    <c:when test="${orderPay.isAsAccount == 2}">
									已驳回
							    </c:when>  
							   <c:otherwise>  
							   		已到账
							   </c:otherwise>
							  </c:choose>
						</span>
		</p>
		</c:forEach>
		<div class="payment_information">
			<p class="orderdetails6">
				<c:forEach items="${costs }" var="cost" varStatus="status">
				<span>${cost[1] }：${cost[3] }${cost[2] }/成人x${cost[0] }人</span>
				</c:forEach>
			</p>
			<div class="ordermoney ordermoney2">应收总计：<em class="gray14"></em><span>${totalMoney }</span><br/>达账金额：<em class="gray14"></em><span>${accountedMoney }</span><br />签证押金：<span>${totalDeposit}</span></div>
		</div>

		<div class="ydbz_sxb" id="secondDiv" >
			<div class="ydBtn ydBtn2">
				<a class="ydbz_x gray" onClick="window.close();">关闭</a>
				
			</div>
		</div>
	</div>
</div>


<script type="text/javascript">
$(document).ready(function(e) {
    var leftmenuid = $("#leftmenuid").val();
    $(".main-nav").find("li").each(function(index, element) {
        if($(this).attr("menuid")==leftmenuid){
            $(this).css("background", "url(/resource/images/img/pfs_nav_hover.png)");
        }
    });
});
$(function(){
    $('.closeNotice').click(function(){
        var par = $(this).parent().parent();
        par.hide();
        par.prev().removeClass('border-bottom');
        par.prev().find('.notice-date').show();
    });
    $('.showNotice').click(function(){
        $(this).parent().hide();
        var par = $(this).parent().parent();
        par.addClass('border-bottom');
        par.next().show();
    });
});
$(function(){
	$('.main-nav li').click(function(){
		$(this).addClass('select').siblings().removeClass('select');
	})
})

String.prototype.formatNumberMoney= function(pattern){
	  var strarr = this?this.toString().split('.'):['0'];   
	  var fmtarr = pattern?pattern.split('.'):[''];   
	  var retstr='';   
	  var str = strarr[0];
	  var fmt = fmtarr[0];
	  var i = str.length-1;     
	  var comma = false;   
	  for(var f=fmt.length-1;f>=0;f--){   
	    switch(fmt.substr(f,1)){   
	      case '#':   
	        if(i>=0 ) retstr = str.substr(i--,1) + retstr;   
	        break;   
	      case '0':   
	        if(i>=0) retstr = str.substr(i--,1) + retstr;   
	        else retstr = '0' + retstr;   
	        break;   
	      case ',':   
	         comma = true;   
	         retstr=','+retstr;   
	        break;   
	     }   
	   }   
	  if(i>=0){   
	    if(comma){   
	      var l = str.length;   
	      for(;i>=0;i--){   
	         retstr = str.substr(i,1) + retstr;   
	        if(i>0 && ((l-i)%3)==0) retstr = ',' + retstr;   
	       }   
	     }   
	    else retstr = str.substr(0,i+1) + retstr;   
	   }   
	  
	   retstr = retstr+'.';   
	   
	   str=strarr.length>1?strarr[1]:'';   
	   fmt=fmtarr.length>1?fmtarr[1]:'';   
	   i=0;   
	  for(var f=0;f<fmt.length;f++){   
	    switch(fmt.substr(f,1)){   
	      case '#':   
	        if(i<str.length) retstr+=str.substr(i++,1);   
	        break;   
	      case '0':   
	        if(i<str.length) retstr+= str.substr(i++,1);   
	        else retstr+='0';   
	        break;   
	     }   
	   }   
	  return retstr.replace(/^,+/,'').replace(/\.$/,'');   
}

String.prototype.replaceSpecialChars=function(regEx){
	if(!regEx){
		regEx = /[\`\"\~\!\@#$\%\^\&\*\(\)\+\=\|\{\}\'\:\;\'\,\/\/\[\/\/\]\.\<\>\/\?\~\！\@#\¥\%\…\…\&\*\（\）\—\—\+\|\{\}\【\】\‘\；\：\”\“\’\。\，\、\？]/g;
	}
	return this.replace(regEx,'');
	
};
</script>
</body>
</html>
