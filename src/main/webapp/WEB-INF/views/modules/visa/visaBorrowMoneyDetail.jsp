<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>订单-签证-借款详情</title>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>

<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="js/common.js"></script>
</head>
<body>
<page:applyDecorator name="show_head">
        <page:param name="desc">借款详情</page:param>
</page:applyDecorator>
				<!--右侧内容部分开始-->
                <div class="mod_nav">订单 > 签证 > 借款记录 > 借款详情</div>
                <div class="ydbz_tit">订单信息</div>
                <div class="mod_information_dzhan_d mod_details2_d">
               		<table border="0" style="margin-left: 25px" width="90%">
						<tbody>
							<tr>
								<td class="mod_details2_d1">下单人：</td>
						        <td class="mod_details2_d2">${visaOrder.createBy.name }</td>
								<td class="mod_details2_d1">下单时间：</td>
								<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaOrder.createDate }"/></td> 
                                <td class="mod_details2_d1">团队类型：</td>
						        <td class="mod_details2_d2">
						        	<c:if test="${orderStatus==null }">单办签</c:if>
									<c:if test="${orderStatus!=null }">
										<c:if test="${orderStatus==1 }">单团</c:if>
										<c:if test="${orderStatus==2 }">散拼</c:if>
										<c:if test="${orderStatus==3 }">游学</c:if>
										<c:if test="${orderStatus==4 }">大客户</c:if>
										<c:if test="${orderStatus==5 }">自由行</c:if>
										<c:if test="${orderStatus==6 }">签证</c:if>
										<c:if test="${orderStatus==7 }">机票</c:if> 
									</c:if> 
						        </td>	
                                <td class="mod_details2_d1">收客人：</td>
						        <td class="mod_details2_d2">${visaOrder.createBy.name }</td>
                                       
							</tr>
							<tr> 
                                <td class="mod_details2_d1">订单编号：</td>
								<td class="mod_details2_d2">${visaOrder.orderNo }</td>
                                <td class="mod_details2_d1">订单团号：</td>
								<td class="mod_details2_d2">${visaOrder.groupCode }</td>
                                <td class="mod_details2_d1">订单总额：</td>
								<td class="mod_details2_d2"><em class="tdred">${totalMoney }</em></td>
                                <td class="mod_details2_d1">订单状态：</td>
								<td class="mod_details2_d2">
									<c:if test="${visaOrder.payStatus==1 }">未收款</c:if>
									<c:if test="${visaOrder.payStatus==3 }">预定</c:if>
									<c:if test="${visaOrder.payStatus==5 }">已收款</c:if>
									<c:if test="${visaOrder.payStatus==99 }">已取消</c:if>
								</td>	 
                                </tr>
							<tr>
								<td class="mod_details2_d1">操作人：</td>
						        <td class="mod_details2_d2">${visaProduct.createBy.name }</td>     
								<td class="mod_details2_d1">办签人数：</td>
						        <td class="mod_details2_d2">${visaOrder.travelNum } &nbsp;人</td>     
							</tr>
						</tbody>
					</table>
               </div>
               <div class="ydbz_tit">产品信息</div>
				<div class="mod_information_dzhan_d mod_details2_d" style="overflow:hidden;">
                        <!--<p class="ydbz_mc">美国个签（北京领区）</p>  -->
						<p class="ydbz_mc">${visaProduct.productName } &nbsp;(<c:if test="${not empty visaProduct.collarZoning }">${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}</c:if>领区)</p>
							 <table style="margin-left: 25px" width="90%" border="0">
							<tbody><tr>
								<td class="mod_details2_d1">产品编号：</td>
								<td class="mod_details2_d2">${visaProduct.productCode }</td>
								<td class="mod_details2_d1">签证国家：</td>
								<td class="mod_details2_d2">${country.countryName_cn }</td>
								<td class="mod_details2_d1">签证类别：</td>
								<td class="mod_details2_d2">${visaType.label }</td>
								<td class="mod_details2_d1">领区：</td>
								<td class="mod_details2_d2"><c:if test="${not empty visaProduct.collarZoning }">${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}</c:if></td>
							 </tr>
							 <tr>
								<td class="mod_details2_d1">应收价格：</td>
								<td class="mod_details2_d2">${currency.currencyMark }<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${visaOrder.proOriginVisaPay}" />/人</td>
                                <td class="mod_details2_d1">创建时间：</td>
                                <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaProduct.createDate }"/></td>
								<td class="mod_details2_d1">&nbsp;</td>
								<td class="mod_details2_d2">&nbsp;</td>
							 </tr>
							</tbody></table>
                        
				</div>
				<div class="ydbz_tit">
					<span class="fl">借款</span>
        		</div>
				<span style="padding-left:30px;">报批日期：</span>2014-12-23
				<table id="contentTable" class="activitylist_bodyer_table">
				   <thead>
					  <tr>
						 <th width="8%">姓名</th>
                         <th width="12%">币种</th>
                         <th width="11%">游客结算价</th>
						 <th width="12%">借款金额</th>
						 <th width="20%">备注</th>
					  </tr>
				   </thead>
				   <tbody>
					  <c:forEach items="${travelers }" var="traveler" >
					  <tr>
						 <td class="tc">${traveler.name }</td>
                         <td class="tc">${currency.currencyName }</td>
						 <td class="tr">${traveler.payPriceSerialNumInfo}</td>
						 <td class="tr red">${traveler.borrowMoney}</td>
						 <td>备注</td>
					  </tr>
					  </c:forEach>
				   </tbody>
				</table>
                
				<div class="ydbz_tit">
					<span class="fl">团队借款</span>
        		</div>
				<table id="contentTable" class="activitylist_bodyer_table">
				   <thead>
					  <tr>
						 <th width="8%">费用名称</th>
                         <th width="12%">币种</th>
                         <th width="11%">订单金额</th>
						 <th width="12%">借款金额</th>
						 <th width="20%">备注</th>
					  </tr>
				   </thead>
				   <tbody>
					  <tr>
						 <td class="tc" rowspan="2">张三</td>
                         <td class="tc">人民币</td>
						 <td class="tr">100</td>
						 <td class="tr red">3000</td>
						 <td>增加车费</td>
					  </tr>
				   </tbody>
				</table>
                
                <!--<div class="ydbz_tit"><span class="fl">备注</span></div>
				<dl class="gai-price-tex">
                    <dd>文字文字</dd>
                </dl> -->
				<div  class="ydbz_foot">
                    <div class="fr f14 all-money" style="font-size:18px;font-weight:bold;">借款金额：<span style="font-size:12px;">￥</span><span class="red" >800</span><span style="color:green;">+</span><span style="font-size:12px;">$</span><span class="red" >800</span></div>
				</div>

				<div class="ydbz_tit">
					<span class="fl">审核动态</span>
        		</div>
				<ul class="spdtai">
					<li>2014-07-30 20:31:56 【销售_张三】申请改价</li>
					<li>2014-07-30 20:31:56 【销售主管_李四】审批通过，发送给【计调_王五】</li>
					<li>2014-07-30 20:31:56 【销售_张三】申请改价</li>
					<li>2014-07-30 20:31:56 【销售主管_李四】审批通过，发送给【计调_王五】</li>
					<li>2014-07-30 20:31:56 【销售_张三】申请改价</li>
					<li>2014-07-30 20:31:56 【销售主管_李四】审批通过，发送给【计调_王五】</li>
				</ul>
                <!--<div class="ydbz_tit">
					<span class="fl">驳回理由</span>
        		</div>
                <ul class="spdtai">
					<li>费用太高</li>
				</ul>-->
				<div class="dbaniu" style="width:230px;">
					<input type="button" value="返回" class="ydbz_x gray" onclick="javascript:history.back();"/>
					<!--<a class="ydbz_s" onclick="reApply();">重新申请</a>-->
					<a href="javaScript:window.close()" class="ydbz_x blue">关闭</a> 
				</div>
				<!--右侧内容部分结束-->
</body>
</html>
