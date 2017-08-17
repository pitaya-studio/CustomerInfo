<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title><c:if test="${type eq '1' }">控房-新增控房-控房详情</c:if><c:if test="${type eq '2' }">控房-新增日期-日期控房详情</c:if></title>
	<meta name="decorator" content="wholesaler"/>
	<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/default.validator.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
		<script type="text/javascript">
		//下载文件
	function downloads(docid){
		window.open("${ctx}/sys/docinfo/download/"+docid);
    }
	</script>
	
	<style type="text/css">
			.custom-combobox-toggle {
				height: 26px;
				margin: 0 0 0 -1px;
				padding: 0;
				/* support: IE7 */
				*height: 1.7em;
				*top: 0.1em;
			}
			.custom-combobox-input {
				margin: 0;
				padding: 0.3em;
				width: 166px;
			}
			.ui-autocomplete {
				height: 200px;
				overflow: auto;
			}
			.sort {
				color: #0663A2;
				cursor: pointer;
			}
			.custom-combobox input[type="text"] {
				height: 26px;
				width: 166px;
			}
			.activitylist_bodyer_table .wdate {
				width: 80px !important;
			}
			.activitylist_bodyer_table .wtext {
				vertical-align: middle;
				margin: 0;
				width: 80px !important;
			}
			.activitylist_bodyer_table .wnum1 {
				width: 30px !important;
				margin: 0;
			}
			.activitylist_bodyer_table .wnum2 {
				width: 50px !important;
				margin: 0;
			}
			.qdgl-cen .batch-label {
				width: 100px;
				cursor: text
			}
			.new_kfang label {
				width: 100px !important;
			}
			.jiange_li {
				color: #08c;
				padding-left: 3px;
				padding-right: 3px;
			}
			.maintain_add p{min-width:300px;}
		</style>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20"><c:if test="${type eq '1' }">控房详情</c:if><c:if test="${type eq '2' }">日期控房详情</c:if></div>
	<div class="maintain_add">
		<p>
			<label>控房名称：</label> ${hotelControl.name }
		</p>
		<p>
			<label>地接供应商：</label>
			<trekiz:autoId2Name4Table tableName='supplier_info' sourceColumnName='id' srcColumnName='supplierName' value='${hotelControl.groundSupplier }'/>
		</p>
		<p>
			<label>采购类型：</label>
				<c:choose>
					<c:when test="${hotelControl.purchaseType==0}">内采</c:when>
					<c:when test="${hotelControl.purchaseType==1}">外采</c:when>
				</c:choose>
		</p>
		<p>
			<label>国家：</label><trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${hotelControl.country}"/>
		</p>
		<p>
			<label>岛屿名称：</label>  <trekiz:autoId2Name4Table tableName="island" sourceColumnName="uuid" srcColumnName="island_name" value="${hotelControl.islandUuid }"/>
		</p>
		<!-- <p class="maintain_kong"></p> -->
		<p>
			<label>酒店集团：</label> 
			<trekiz:defineDict  name="hotelGroup" type="hotel_group" defaultValue="${hotelControl.hotelGroup}" readonly="true" />
		</p>
		<p>
			<label>酒店名称：</label> <trekiz:autoId2Name4Table tableName="hotel" sourceColumnName="uuid" srcColumnName="name_cn" value="${hotelControl.hotelUuid }"/>
		</p>
		<p>
			<label>币种选择：</label> <trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_name" value="${hotelControl.currencyId }"/>
		</p>
		<!-- <p class="maintain_kong"></p> -->
	</div>
	<table id="contentTable" class="table activitylist_bodyer_table">
		<thead>
			<tr>
				<th width="8%">入住日期</th>
				<th width="10%">航班</th>
				<th width="15%">房型 * 晚数<!--/单价-->
				</th>
				<th width="7%">餐型</th>
				<th width="6%">上岛方式</th>
				<th width="8%">房间总价</th>
				<th width="6%">现有库存（间）</th>
				<th width="6%">备注</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${hotelControl.detailList }" var="entry">
				<tr>
					<td class="tc" name="indate" rowspan="${fn:length(entry.roomList) }">
						<fmt:formatDate value="${entry.inDate}" pattern="yyyy-MM-dd" />
					</td>
					<td class="tc" name="flightNo" rowspan="${fn:length(entry.roomList) }"><p>${entry.flightList[0].airline }</p>
					</td>
					
						<c:choose>
							<c:when test="${fn:length(entry.roomList)==0}">
								<td></td><td></td>
							</c:when>
							<c:otherwise>
								<c:forEach items="${entry.roomList }" begin="0" end="0" varStatus="status" var="room">
										<td class="tc" name="houseType">
											<span>
												<trekiz:autoId2Name4Table tableName="hotel_room" sourceColumnName="uuid" srcColumnName="room_name" value="${room.roomUuid}"/> 
											</span>
											 * <span> ${room.night} </span> 晚 
										</td>
										<td class="tc" name="mealType">
											<c:forEach items="${fn:split(room.hotelMeals,';')}" var="meal">
												<trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${meal}" /><br/>
											</c:forEach>
										</td>
								</c:forEach>
							</c:otherwise>
						</c:choose>
						
					
					<td class="tc" name="islandWay" rowspan="${fn:length(entry.roomList) }">
						<%--这种方式不好使 <trekiz:defineDict  name="islandWay" type="island_way" defaultValue="${entry.islandWay}" readonly="true" /> --%>
						<trekiz:autoId2Name4Table tableName="sys_company_dict_view" sourceColumnName="uuid" srcColumnName="label" value="${fn:trim(entry.islandWay)}"/>
					</td>
					<td class="tc" name="housePrice" rowspan="${fn:length(entry.roomList) }">
					    <p><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${entry.currencyId}"/>
                        <fmt:formatNumber  type="currency" pattern="#,##0.00" value="${entry.totalPrice}" />
					</td>
					<td class="tc" name="stockQty" rowspan="${fn:length(entry.roomList) }">${entry.stock }</td>
					<td class="tc" name="memo" rowspan="${fn:length(entry.roomList) }">${entry.memo }</td>
				</tr>
				<c:forEach items="${entry.roomList }" begin="1" varStatus="status" var="room">
					<tr>
						<td class="tc" name="houseType">
							<span>
								<trekiz:autoId2Name4Table tableName="hotel_room" sourceColumnName="uuid" srcColumnName="room_name" value="${room.roomUuid}"/>
							</span>
							 * <span> ${room.night} </span> 晚 <!--<span class="pad5">￥700/间</span>-->
						</td>
					
						<td class="tc" name="mealType">
						
							<c:forEach items="${fn:split(room.hotelMeals,';')}" var="meal" >
								<!-- <trekiz:defineDict name="mealType" type="hotel_meal_type" defaultValue="${fn:split(room.hotelMeals,';')}" readonly="true"/></br> -->
								<trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${meal}" /><br/> 
							</c:forEach>
						 
						</td>
					</tr>
				</c:forEach>
			</c:forEach>
		</tbody>
	</table>
		<div class="sysdiv sysdiv_coupon">
				<p class="maintain_pfull new_kfang">
					<label>上传附件：</label>
					<ol class="batch-ol">
						<c:forEach items="${haList}" var="file" varStatus="s1">
							<li>
								<span>${file.docName}</span><a style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${file.docId})">下载</a>
								<input type="hidden" name="hotelAnnexDocId" value="${file.docId}"/>
								<input type="hidden" name="docOriName" value="${file.docName}"/>
								<input type="hidden" name="docPath" value="${file.docPath}"/>
								
							</li>
						</c:forEach>
					</ol>
				</p>
				<p class="maintain_pfull new_kfang">
					<label>备注：</label>
                             ${hotelControl.memo }
				</p>
		</div>
	<div class="release_next_add">
		<input type="button" class="btn btn-primary gray" value="关闭" onclick="window.close();">
	</div>
	<!--右侧内容部分结束--> 
</body>
</html>
