<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>基础信息维护-酒店房型管理-详情</title>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<%@include file="/WEB-INF/views/include/treetable.jsp"%>
<link type="text/css" rel="stylesheet"
	href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<!--基础信息维护模块的脚本-->
<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">房型基本信息</div>
	<form method="post" action="" class="form-horizontal" id=""
		novalidate="">
		<div class="maintain_add">
			<p>
				<label>房型名称：</label> <span>${hotelRoom.roomName}</span>
			</p>
			<p>
				<label>床型名称：</label> <span><trekiz:defineDict name="bed"
						type="hotel_bed_type" defaultValue="${hotelRoom.bed }"
						readonly="true" /></span>
			</p>
			<p>
				<label>关联餐型：</label> <span> <c:forEach items="${mealList }"
						var="ml" varStatus="meal">
						<c:if test="${!meal.last }">${ml.mealName }、</c:if>
						<c:if test="${meal.last }">${ml.mealName }</c:if>
					</c:forEach>
				</span>
			</p>
			<p>
				<label>建筑面积：</label> <span>${hotelRoom.roomArea}</span>
			</p>

			<p class="maintain_kong"></p>
			<!-- 			<p> -->
			<!-- 				<label>可加床数量：</label> <span>${hotelRoom.extraBedNum}</span> -->
			<!-- 			</p> -->
			<!-- 			<p> -->
			<!-- 				<label>加床费用：</label> <span>${hotelRoom.extraBedCost}</span>元/人 -->
			<!-- 			</p> -->
			<!-- 			<p> -->
			<!-- 				<label>加床顾客类型：</label> <span><trekiz:autoId2Name4Class classzName="TravelerType" sourceProName="uuid" srcProName="name" value="${hotelRoom.extraBedCustomer}"/></span> -->
			<!-- 			</p> -->
			<p class="maintain_kong"></p>
			<p>
				<label>容住率：</label>
							${hotelRoomOccuRates[0].occupancyRate }
							<br>
							<c:forEach items="${hotelRoomOccuRates }" var="hro" begin="1">
									  <label></label>
									  <span >${hro.occupancyRate } </span>
               						  <br>
							</c:forEach>
						
			</p>
			<p>
				<label style="float:left;">容住率备注：</label>
							
							<span title="${hotelRoomOccuRates[0].remark }" style="display:inline-block;width: 180px;text-align: left;cursor: text;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">${hotelRoomOccuRates[0].remark }</span>
							<br>
							<c:forEach items="${hotelRoomOccuRates }" var="hro" begin="1">
									<label></label>
									<span title="${hro.remark }" style="display:inline-block;width: 180px;text-align: left;cursor: text;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">${hro.remark } </span>
               						<br>
							</c:forEach>
						
			</p>
			<p>
				<label>可住人数：</label>
						<span >${roomNum} </span>
						
			</p>
			<p>
				<label>排序：</label> <span>${hotelRoom.sort}</span>
			</p>
			<p class="maintain_pfull">
				<label>房型特色：</label> <span class="checkboxdiv"> <trekiz:defineDict
						name="roomFeatures" type="room_feature" input="checkbox"
						defaultValue="${hotelRoom.roomFeatures}" readonly="true" />
				</span>
			</p>
			<p class="maintain_kong"></p>
			<p>
				<label>住客类型：</label> <span> <span> <c:forEach
							items="${relationlist }" var="typeRelation" varStatus="relation">
							<c:if test="${!relation.last }">${typeRelation.hotelGuestTypeName }、</c:if>
							<c:if test="${relation.last }">${typeRelation.hotelGuestTypeName }</c:if>
						</c:forEach>
				</span>
				</span>
			</p>
				<p>
				<label>间数：</label> 
				<span>
					<span> 
						${hotelRoom.roomNumb }
					</span>
				</span>
			</p>
			<p class="maintain_kong"></p>
			<p class="maintain_pfull">
				<label>备注：</label> 
				<span>
					<textarea  readonly="readonly" style="width: 35%;height: 200px"  name="remark"  maxlength="2000" >${hotelRoom.remark}</textarea>
				</span>
			</p>
			<p class="maintain_kong"></p>
			<p class="maintain_btn">
				<label>&nbsp;</label> <label>&nbsp;</label> <input type="button"
					value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray"
					onclick="window.close();" />
			</p>
		</div>
	</form>
	<!--右侧内容部分结束-->
</body>
</html>
