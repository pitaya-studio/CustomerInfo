<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单-签证-签务身份-签证订单修改</title>
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
<%-- <script type="text/javascript" src="${ctxStatic }/js/manageorder.js"></script> --%>
<script type="text/javascript" src="${ctxStatic }/js/common.js"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>

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
	g_context_url = "${ctx}";
	function downloadInterviewNotice(visaOrderId,travelerId){
		var href = "${ctx}/visa/order/downloadInterviewNotice?orderId="+visaOrderId+"&travelerId="+travelerId;
		window.open(href);
	}
	function viewdetail(uuid,verifyStatus){
		verifyStatus = '-2';
		window.open("${ctx}/invoice/limit/supplyviewrecorddetail/" + uuid + "/" + verifyStatus);
	}
	function viewdetail4Receipt(uuid,verifyStatus,orderType){
		verifyStatus = '-2';
		window.open("${ctx}/receipt/limit/supplyviewrecorddetail/" + uuid + "/" + verifyStatus + "/" + orderType);
	}

	function downloadDocs(docIds,fileName){
		 var doc = docIds.replace(/[ ]/g,"");
			if(doc == null || doc ==""){
				$.jBox.tip("文件不存在");
				return;
			}
			var fileUrl =encodeURI("${ctx}/sys/docinfo/fileExists/" + doc + "/"+fileName);
			$.ajax({ url: encodeURI(fileUrl),async:false, success: function(result){
				 if("文件存在"==result){
					  window.open(encodeURI(encodeURI("${ctx}/sys/docinfo/zipdownload/" + doc + "/"+fileName)));
				  } else{
					  top.$.jBox.tip('文件不存在');
					
				  }
			}});
		
	}
	$(function(){
		/***************************************************************/
		/*112需求-特殊备注的提示展示										   */
		/***************************************************************/
            $("#specialremark").focus(function(){
                $(".ipt-tips").hide();
            });

            $("#specialremark").blur(function(){
                if($("#specialremark").val()==''){
                    $(".ipt-tips").show();
                }else{
                $(".ipt-tips").hide();
            }
            });
		
		
		jQuery.extend(jQuery.validator.messages, {
            required: "必填信息"
        });
		
		
		
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
		
		//112特殊处理 
		var value = $("#specialremark").text().trim();
		$("#specialremark").text(value);
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
	
	//特殊需求备注 保存 
	function doUpdateVisaOrderRemark(obj,orderId){
		var value = $(obj).parent().parent().find("textarea").val();
		$.ajax({    
			cache : true,
			type: "POST",   
			url:g_context_url+"/visa/order/doUpdateVisaOrderRemark",                 
			data:{
				"remark":value,
				"orderId":orderId
			},
			async: false,
			success: function(data) {  
				if(data=="true"){
					$.jBox.tip("保存成功！", "success"); 
				}
			}             
		});
	}
	
	
	//签证借款申请 ---------wxw added--------
	function jbox_sqjk(travelerID) {
		var html = '<div style="margin-top:20px;"><label class="jbox-label">借款金额：</label>';
		html += '<input name="borrowAmount" type="text" /><br /><label class="jbox-label">借款原因：</label><textarea name="borrowRemark" cols="" rows="" ></textarea>';
		html += '</div>';
		$.jBox(html, { title: "借款",buttons:{'申请借款': true}, submit:function(v, h, f){
			
			  if(!f.borrowAmount){
				   top.$.jBox.tip("借款金额为必填项！");
				   return false;
			   }
			   
			   var text1=(f.borrowRemark).replace( /^\s+/, "" ).replace( /\s+$/, "" ); 
			   if(!f.borrowRemark||text1.length<1){
				   top.$.jBox.tip("借款原因为必填项！");
				   return false;
			   }
			
			   if( isNumber( f.borrowAmount)){
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
			   }else{
				   top.$.jBox.tip("借款金额必须为数字！");
				   return false;
			   }
			   
		},height:220,width:380});
	}
	
	/**
	 * 必须为数字或小数点的校验
	 */
	function isNumber(oNum)
	{
			if(!oNum) return false;
			var strP=/^\d+(\.\d+)?$/;
			if(!strP.test(oNum)) return false;
			try{
				if(parseFloat(oNum)!=oNum) return false;
			}catch(ex){
			    return false;
			}
			return true;
	} 
</script>


</head>

<body>
<input type="hidden" value="${fns:getUser().company.uuid }" id="companyUuid" name="companyUuid">

<page:applyDecorator name="show_head">
        <page:param name="desc">签证订单修改</page:param>
</page:applyDecorator>

<shiro:hasPermission name="visaOrderForOp:agentinfo:visibility">
	<input type="hidden" value="1" id="agentinfo_visibility">
	<c:set var="agentinfo_visibility" value="1"></c:set>
</shiro:hasPermission>
<div class="mod_nav">订单 > 签证 > 订单修改</div>
<div class="ydbzbox fs">
	<div class="tr">
		<a href="#" class="dyzx-add" onclick="downloadDocs('${activityGroup.openDateFile }','出团通知书')">下载出团通知书</a>
		<a href="#" class="dyzx-add" onclick="downloadDocs('${productOrder.confirmationFileId}','确认单')">下载确认单</a>
		
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
		<%-- <li><span>订单团号：</span>${visaOrder.groupCode }</li> --%>
		<!-- 对应需求号    C460V3  -->
		<!-- C460V3 所有批发商团号取自产品团号 -->
		<!--  C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-s-订单->签务签证订单->修改页 -->
		<c:if test="${fns:getUser().company.uuid eq '7a816f5077a811e5bc1e000c29cf2586'}"><!-- 环球行 -->
		  <li><span>订单团号：</span>${visaOrder.groupCode }</li>
		</c:if>
		<c:if test="${fns:getUser().company.uuid ne'7a816f5077a811e5bc1e000c29cf2586'}"><!-- 非环球行 -->
		  <li><span>团号：</span>${visaProduct.groupCode }</li>
		</c:if>
        <!--  C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-e -->
		<%-- <li><span>团号：</span>${visaProduct.groupCode}</li> --%>
		<c:if test="${productOrder!=null }">
		<li><span>参团订单编号：</span>${visaOrder.activityCode }</li>
		<li><span>参团团号：</span>${activityGroup.groupCode }</li>
		</c:if>
		<li><span>订单总额：</span>${totalMoney }</li>
		<li><span>订单状态：</span>
			<c:if test="${visaOrder.visaOrderStatus==0 }">未收款</c:if>
			<c:if test="${visaOrder.visaOrderStatus==1 }">已收款</c:if>
			<c:if test="${visaOrder.visaOrderStatus==2 }">已取消</c:if>
			<c:if test="${visaOrder.visaOrderStatus==100 }">订单创建中</c:if>
		</li>
		<li><span>操作人：</span>${visaProduct.createBy.name }</li>
		<li><span>办签人数：</span>${visaOrder.travelNum }人</li>
		<li>
			<span>下单人：</span>${visaOrder.createBy.name }
		</li>
	</ul>
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
                    <li><span>应收价格：</span>${currency.currencyMark }<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${visaProduct.visaPay}" />人</li>
                    
	    			<li><span>创建时间：</span><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaProduct.createDate }"/></li>
                  </ul>
                </div>
	<div class="ydbz_tit">预订人信息</div>
	<div flag="messageDiv">
		<p class="ydbz_qdmc">预订渠道：${agentInfo.agentName }</p>
			<c:forEach items="${contacts }" var="contact" varStatus="status">
                    <ul class="ydbz_qd" <c:if test="${fns:getUser().id ne visaOrder.createBy.id and fns:getUser().id ne visaOrder.salerId and (empty agentinfo_visibility or agentinfo_visibility ne 1) }">style="visibility:hidden;"</c:if>>
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
	</div>
	<!-- 112需求新增修改特殊需求的功能 -->
	<!-- S 特殊需求112 -->
                  <%--  <div id="manageOrder_m">
                        <div id="contact">
                           <div class="ydbz_tit">特殊需求</div>
                            <div class="ydbz2_lxr" flag="messageDiv">
                                <form class="contactTable">
                                   <div class="textarea pr wpr20">
                                    
                                    <label style="vertical-align:top">特殊需求：</label><input type="hidden" name ="id" value="">
                                    <textarea name="remark" flag="istips" class="textarea_long" maxlength="500" rows="3" cols="50" onkeyup="this.value=this.value.replaceSpecialChars4SpecialRemark()" onafterpaste="this.value=this.value.replaceSpecialChars4SpecialRemark()" id="specialremark">
                                     	${visaOrder.remark}
                                    </textarea>
                                    <c:if test="${empty visaOrder.remark}">
                                    	<span id="promptSpan" class="ipt-tips" style="text-indent:1cm;">最多输入500字</span> 
                                    </c:if>   
                                	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="rightBtn"><a class="btn" onclick="doUpdateVisaOrderRemark(this,'${visaOrder.id}')"/>保存</a></span>
                                    </div>
                                </form>
                            </div>
                        </div>
                        
                    </div> --%>
                    
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
                    
                    <!--E 112-->
	<div  id="manageOrder_new" >
		<div class="ydbz_tit orderdetails_titpr">游客信息<a class="ydbz_x" href="${ctx}/visa/order/downloadTraveler?visaOrderId=${visaOrder.id}&agentId=${agentInfo.id }&groupCode=${visaOrder.groupCode }">下载游客信息</a></div>
		<!--<div class="warningtravelerNum">暂无游客信息</div>-->
		<c:forEach items="${travelers }" var="traveler" varStatus="status">
		<div id="traveler">
			<form class="travelerTable" id="subForm">
				<input type="hidden" name="orderId" value="${visaOrder.id}"/>
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
                        <label class="ydLable">游客<em class="travelerIndex" style="vertical-align: baseline;">${status.index+1 }</em>:</label>
						<div class="tourist-t-off">
							<span class="fr">押金：<span class="gray14"></span><span class="ydFont2">${traveler.visa.totalDepositMoney }</span>　　结算价：<span class="gray14"></span><span class="ydFont2">${traveler.payPriceSerialNumInfo}</span></span>
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
								<li><label class="ydLable">国籍：</label>
								<c:forEach items="${countrys }" var="con" varStatus="sta">
									<c:if test="${con.id==traveler.nationality }">${con.countryName_cn }</c:if>
								</c:forEach>
								</li>
								<li><label class="ydLable">出生日期：</label><span class="fArial"><fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.birthDay }"/></span></li>
								<li><label class="ydLable">联系电话：</label><span class="fArial">${traveler.telephone }</span></li>
								<li><label class="ydLable">护照号：</label><span class="fArial">${traveler.passportCode }</span></li>
								<li><label class="ydLable">护照签发日期：</label><span class="fArial"><fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.issuePlace }"/></span></li>
								<li><label class="ydLable">护照有效期：</label><span class="fArial"><fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.passportValidity }"/></span></li>
								<li><label class="ydLable">身份证号：</label><span class="fArial">${traveler.idCard }</span></li>
                                <li><label class="ydLable">护照类型：</label><c:if test="${traveler.passportType==1 }">因公护照</c:if><c:if test="${traveler.passportType==2 }">因私护照</c:if></li>
							<c:if test="${fns:getUser().company.uuid eq '980e4c74b7684136afd89df7f89b2bee'}"> <li><label class="ydLable">签发地：</label><span class="fArial">${traveler.issuePlace1 }</span></li></c:if>
							</ul>
							<div class="ydbz_tit ydbz_tit_child"><em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>签证信息</div>
							<ul flag="messageDiv">
								<div class="ydbz_tit ydbz_tit_child">申请办签</div>
								<div class="ydbz_scleft">
									<table style="width:98%; line-height:40px;margin-left: 32px; margin-bottom:50px;" cellspacing="10" cellpadding="10">
										<tbody>
											<tr> 
												<td>产品编号：${visaProduct.productCode }</td>
												<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;领区：	<c:if test="${not empty visaProduct.collarZoning }">
				                           ${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}
			                        	</c:if></td>
			                        			<td></td><!-- 占位 -->
												<td>&nbsp;&nbsp;&nbsp;签证国家：${country.countryName_cn }</td>
											</tr>
											<tr> 
												<td>签证类别：${visaType.label }</td>
												<td colspan="2">签证状态：
													<select name="visaStauts" class="selZF">
														<option value='-1'>请选择</option>
														<option value="0" <c:if test="${traveler.visa.visaStauts==0 }">selected="selected"</c:if>>未送签</option>
														<option value="1" <c:if test="${traveler.visa.visaStauts==1 }">selected="selected"</c:if>>送签</option>
														<option value="2" <c:if test="${traveler.visa.visaStauts==2 }">selected="selected"</c:if>>已约签</option>
														<option value="3" <c:if test="${traveler.visa.visaStauts==3 }">selected="selected"</c:if>>通过</option>
														<option value="4" <c:if test="${traveler.visa.visaStauts==4 }">selected="selected"</c:if>>未约签</option>
														<!--  <option value="4" <c:if test="${traveler.visa.visaStauts==4 }">selected="selected"</c:if>>申请撤签</option> -->
														<option value="5" <c:if test="${traveler.visa.visaStauts==5 }">selected="selected"</c:if>>已撤签</option>
														<!--<option value="6" <c:if test="${traveler.visa.visaStauts==6 }">selected="selected"</c:if>>撤签失败</option> -->
														<option value="7" <c:if test="${traveler.visa.visaStauts==7 }">selected="selected"</c:if>>拒签</option>
														<option value="8" <c:if test="${traveler.visa.visaStauts==8 }">selected="selected"</c:if>>调查</option>
														<option value="9" <c:if test="${traveler.visa.visaStauts==9 }">selected="selected"</c:if>>续补资料</option>
													</select>
													<a class="ydbz_x fn" onclick="downloadInterviewNotice('${visaOrder.id }','${traveler.id }')">下载面签通知</a>
												</td>
												<!-- 懿洋假期的美国签证隐藏 C482-->
													<c:if test="${!(fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' && country.countryName_cn=='美国')}">
														<td>制表人姓名：<input type="text" class="inputTxt" name="makeTable" value="${traveler.visa.makeTable }"></td>
													</c:if>
											</tr>
											<tr><td colspan="4"><div class="mod_information_d7"></div></td></tr>
											<tr> 
												<td>预计出团时间：<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.visa.forecastStartOut }"/></td>
												<td>预计约签时间：<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.visa.forecastContract }"/></td>
												<!-- 0211-qyl-begin-新增回团日期,只有星徽四海才会显示-->
												 <c:if test="${fns:getUser().company.uuid == '0e19ac500f78483d8a9f4bb768608629'}">
												<td>预计回团时间：<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.visa.forecastBackDate }"/></td>
                                                 </c:if> 
												<!--0211-qyl-end- -->
												</tr>
											<tr> 
												<td class="touristNeed"><!-- <span class="xing">*</span> -->&nbsp;&nbsp;&nbsp;实际出团时间：<input type="text" id="" name="startOut" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.visa.startOut }"/>' onclick="WdatePicker()"><!-- <input type="text" id="" class="inputTxt dateinput required" name="startOut" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.visa.startOut }"/>' onclick="WdatePicker()"> --></td>
												<!-- 懿洋假期的美国签证隐藏 C482-->
												<c:if test="${!(fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' && country.countryName_cn=='美国')}">
													<td colspan="2" class="touristNeed"><!-- <span class="xing">*</span> -->&nbsp;&nbsp;&nbsp;实际约签时间：<input type="text" id="" name="contract" value='<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${traveler.visa.contract }"/>' onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"> <!-- <input type="text" id="" class="inputTxt dateinput required" name="contract" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.visa.contract }"/>' onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})">  --></td>
												</c:if>
											</tr>
											<tr> 
												<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;AA码：<input name="AACode" type="text" value="${traveler.visa.AACode }" class="inputTxt "/></td>
												<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;UID编号：<input type="text" class="inputTxt" name="UIDCode" value="${traveler.visa.UIDCode }"></td>
<!-- 												<td>制表人姓名：<input type="text" class="inputTxt" name="makeTable" value="${traveler.visa.makeTable }"></td> -->
											</tr>
										</tbody>
									</table>
								</div>
                                            <div class="ydbz_tit ydbz_tit_child">办签资料</div>
							<ul class="seach25 seach100 ydbz_2uploadfile"></ul>
							<div class="ydbz_tit ydbz_tit_child">
							    <!-- ***************197-start -->
								<p class="tourist-ckb">
									<label class="ydLable">资料原件：</label>
									<c:if test="${fn:contains(visaProduct.original_Project_Type, '0') }">
									<input type="checkbox" name="signOriginalProjectType" <c:if test="${fn:contains(traveler.visa.signOriginalProjectType, '0') }">checked="checked"</c:if> value="0" class="traveler"><label class="ckb-txt">护照</label>
									</c:if>
									<c:if test="${fn:contains(visaProduct.original_Project_Type, '1') }">
									<input type="checkbox" name="signOriginalProjectType" <c:if test="${fn:contains(traveler.visa.signOriginalProjectType, '1') }">checked="checked"</c:if> value="1" class="traveler"><label class="ckb-txt">身份证</label>
									</c:if>
									<c:if test="${fn:contains(visaProduct.original_Project_Type, '3') }">
									<input type="checkbox" name="signOriginalProjectType" <c:if test="${fn:contains(traveler.visa.signOriginalProjectType, '3') }">checked="checked"</c:if> value="3" class="traveler"><label class="ckb-txt">电子照片</label>
									</c:if>
									<c:if test="${fn:contains(visaProduct.original_Project_Type, '4') }">
									<input type="checkbox" name="signOriginalProjectType" <c:if test="${fn:contains(traveler.visa.signOriginalProjectType, '4') }">checked="checked"</c:if> value="4" class="traveler"><label class="ckb-txt">申请表格</label>
									</c:if>
									<c:if test="${fn:contains(visaProduct.original_Project_Type, '5') }">
									<input type="checkbox" name="signOriginalProjectType" <c:if test="${fn:contains(traveler.visa.signOriginalProjectType, '5') }">checked="checked"</c:if> value="5" class="traveler"><label class="ckb-txt">户口本</label>
									</c:if>
									<c:if test="${fn:contains(visaProduct.original_Project_Type, '6') }">
									<input type="checkbox" name="signOriginalProjectType" <c:if test="${fn:contains(traveler.visa.signOriginalProjectType, '6') }">checked="checked"</c:if> value="6" class="traveler"><label class="ckb-txt">房产证</label>
									</c:if>
									<c:if test="${fn:contains(visaProduct.original_Project_Type, '2') }">
									<input type="checkbox" name="signOriginalProjectType" <c:if test="${fn:contains(traveler.visa.signOriginalProjectType, '2') }">checked="checked"</c:if> value="2" class="traveler"><label class="ckb-txt">其他&nbsp;&nbsp;&nbsp;${visaProduct.original_Project_Name }</label>
									</c:if>
								</p>
								<p class="tourist-ckb">
									<label class="ydLable">复印件：</label>
									<c:if test="${fn:contains(visaProduct.copy_Project_Type, '3') }">
									<input type="checkbox" name="signCopyProjectType" <c:if test="${fn:contains(traveler.visa.signCopyProjectType, '3') }">checked="checked"</c:if> value="3" class="traveler"><label class="ckb-txt">护照</label>
									</c:if>
									<c:if test="${fn:contains(visaProduct.copy_Project_Type, '4') }">
									<input type="checkbox" name="signCopyProjectType" <c:if test="${fn:contains(traveler.visa.signCopyProjectType, '4') }">checked="checked"</c:if> value="4" class="traveler"><label class="ckb-txt">身份证</label>
									</c:if>
									<c:if test="${fn:contains(visaProduct.copy_Project_Type, '5') }">
									<input type="checkbox" name="signCopyProjectType" <c:if test="${fn:contains(traveler.visa.signCopyProjectType, '5') }">checked="checked"</c:if> value="5" class="traveler"><label class="ckb-txt">电子照片</label>
									</c:if>
									<c:if test="${fn:contains(visaProduct.copy_Project_Type, '6') }">
									<input type="checkbox" name="signCopyProjectType" <c:if test="${fn:contains(traveler.visa.signCopyProjectType, '6') }">checked="checked"</c:if> value="6" class="traveler"><label class="ckb-txt">申请表格</label>
									</c:if>
									<c:if test="${fn:contains(visaProduct.copy_Project_Type, '0') }">
									<input type="checkbox" name="signCopyProjectType" <c:if test="${fn:contains(traveler.visa.signCopyProjectType, '0') }">checked="checked"</c:if> value="0" class="traveler"><label class="ckb-txt">户口本</label>
									</c:if>
									<c:if test="${fn:contains(visaProduct.copy_Project_Type, '1') }">
									<input type="checkbox" name="signCopyProjectType" <c:if test="${fn:contains(traveler.visa.signCopyProjectType, '1') }">checked="checked"</c:if> value="1" class="traveler"><label class="ckb-txt">房产证</label>
									</c:if>
									<c:if test="${fn:contains(visaProduct.copy_Project_Type, '2') }">
									<input type="checkbox" name="signCopyProjectType" <c:if test="${fn:contains(traveler.visa.signCopyProjectType, '2') }">checked="checked"</c:if> value="2" class="traveler"><label class="ckb-txt">其他&nbsp;&nbsp;&nbsp;${visaProduct.copy_Project_Name }</label>
									</c:if>
								</p>
								<!-- ***************197-end -->
								<p class="tourist-ckb">
									<label class="ydLable">护照状态：</label>
									<select id="passportStatus" id="passportStatus${visa.id}" name="passportStatus" class="selZF">
									    <option <c:if test="${traveler.passportStatus==0}">selected="selected"</c:if>>请选择</option>
										<option <c:if test="${traveler.passportStatus==1}">selected="selected"</c:if>>借出</option>
										<option <c:if test="${traveler.passportStatus==2}">selected="selected"</c:if>>销售已领取</option>
										<!--  
										<option <c:if test="${traveler.passportStatus==3}">selected="selected"</c:if>>未签收</option>
										<option <c:if test="${traveler.passportStatus==4}">selected="selected"</c:if>>已签收</option>
										-->
										<option <c:if test="${traveler.passportStatus==4}">selected="selected"</c:if>>已还</option>
										<option <c:if test="${traveler.passportStatus==5}">selected="selected"</c:if>>已取出</option>
										<option <c:if test="${traveler.passportStatus==6}">selected="selected"</c:if>>未取出</option>
										<!--  <option <c:if test="${traveler.passportStatus==7}">selected="selected"</c:if>>走团</option>-->
										<option <c:if test="${traveler.passportStatus==8}">selected="selected"</c:if>>计调领取</option>
									</select>
									<c:if test="${traveler.passportStatus==1}">
										<a class="ydbz_x" onclick="jbox_hhz('${traveler.id}','${visa.id}');" href="javascript:void();">还护照</a>
									</c:if>
									<c:if test="${traveler.passportStatus!=1}">
										<a class="ydbz_x" onclick="jbox_jhz1('${traveler.id}','${visa.id}');" href="javascript:void();">借护照</a>
										<a class="ydbz_x" onclick="jbox_hhz('${traveler.id}','${visa.id}');" href="javascript:void();">还护照</a>
									</c:if>
								</p>
								
							</div>
							<c:if test="${traveler.visa.docIds!=null && traveler.visa.docIds!=''}">
							<div class="ydbz_tit ydbz_tit_child">下载资料<a class="ydbz_x" onclick="downloadDocs('${traveler.visa.docIds }','全部下载')" >全部下载</a></div>
							</c:if>
							<ul class="ydbz_2uploadfile ydbz_scleft lh180">
								<c:forEach items="${traveler.visa.docs }" var="doc" varStatus="status">
								<li class="seach25 seach33"><p class="uploadlong" title="${doc.docName}">${doc.docName}</p></li>
								</c:forEach>
							</ul>
							</ul>
							
							
							<div class="ydbz_tit ydbz_tit_child"><em class="ydExpand"></em>备注：</div>
							<ul class="textareaulpad">
							<textarea style="max-width: 1327px;height: 56px;" name="passportOperateRemark" >${traveler.visa.passportOperateRemark }</textarea>
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
									<label class="ydLable2">结算价：</label><span class="gray14"></span><span class="ydFont2">${traveler.payPriceSerialNumInfo}</span>
								</div>
								
								<c:if test="${traveler.borrowMoney==null}">
								       <!--  新审核中屏蔽  单申请入口  -->
								       <!-- 
						               <div class="fr"><a class="ydbz_x" href="javascript:void(0)" onclick="jbox_sqjk('${traveler.id}');">申请借款</a></div>
						               -->                                              
							   </c:if>
								
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
					<div class="rightBtn"><a onclick="SavePeopleTable(this)" class="btn">保存</a></div>
				</div>
			</form>
		</div>
		</c:forEach>
		
		<div class="ydbz_tit">收款信息</div>
		<c:forEach items="${orderPays }" var="orderPay" varStatus="status">
		<p class="orderdetails6">
		   <span style="width: 16%" class="ellipsis" title="${orderPay.remarks}">游客姓名：${orderPay.remarks}</span> <%-- 使用orderpay的备注字段remarks来显示游客名称--%>
		   <span style="width: 16%">收款方式：<c:if test="${orderPay.payType==1 }">支票支付</c:if>
		   <c:if test="${orderPay.payType==2 }">POS支付</c:if>
		   <c:if test="${orderPay.payType==3 }">现金支付</c:if>
		   <c:if test="${orderPay.payType==4 }">汇款支付</c:if>
		   <c:if test="${orderPay.payType==5 }">快速支付</c:if>
		   </span>
		   <span style="width: 16%">收款类型：<c:if test="${orderPay.payPriceType==1 }">收全款</c:if>
		   			<c:if test="${orderPay.payPriceType==2 }">收尾款</c:if>
		   			<c:if test="${orderPay.payPriceType==3 }">收订金</c:if>
		   			<c:if test="${orderPay.payPriceType==7 }">收押金</c:if>
		   			<c:if test="${orderPay.payPriceType==16 }">收押金</c:if>
		   </span>
		   <span style="width: 16%">收款金额：${orderPay.moneyAmount }</span>
		   <span style="width: 16%">收款时间： <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${orderPay.createDate }"/></span>
		   <span style="width: 16%">收款凭证：<a lang="4453" class="showpayVoucher" onclick="downloadDocs('${orderPay.payVoucher }','收款凭证')" >收款凭证</a></span>
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

		<div class="ydbz_sxb" id="secondDiv" style="display: block;">
			<div class="ydBtn ydBtn2">
				<!-- <a class="ydbz_x gray" href="">取消</a>
				<input type="button" class="btn btn-primary" value="确定"> -->
			</div>
		</div>
	</div>
</div>
<a  style="display: none;" id="huzhaolingqu" href="${ctx}/order/download/download?fileName=hzlqd.rar">护照领取单</a>

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

/******************************************************/
/*112需求:特殊需求过滤字符:"<",">","\"," "" "," '' "的函数方法      */
/******************************************************/
String.prototype.replaceSpecialChars4SpecialRemark= function (regEx) {//112-my
   if (!regEx){
     regEx = /[\`\"\'\'\‘\”\“\’\<\>\\]/g;
     }
   return this.replace(regEx, '');
    };   
   
</script>
</body>
</html>
