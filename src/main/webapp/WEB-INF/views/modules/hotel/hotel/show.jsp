<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>基础信息维护-酒店-基础信息</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <%--t2改版 去掉重复引用的样式 modified by Tlw--%>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">Hotel信息</div>
	<div class="maintain_add">
		
		<p>
			<label>酒店名称：</label> 
			<span>
				${hotel.nameCn}
			</span>
		</p>
		<p>
			<label>中文缩写：</label> 
			<span>
				${hotel.shortNameCn}
			</span>
		</p>
		<p>
			<label>英文名称：</label> 
			<span>
				${hotel.nameEn}
			</span>
		</p>
		<p>
			<label>英文缩写：</label> 
			<span>
				${hotel.shortNameEn}
			</span>
		</p>
		<p>
			<label>全拼：</label> 
			<span>
				${hotel.spelling}
			</span>
		</p>
		<p>
			<label>全拼缩写：</label> 
			<span>
				${hotel.shortSpelling}
			</span>
		</p>
		<p>
			<label>位置：</label> 
			<span>
				${hotel.position=="1"?"境内":"境外"}
			</span>
		</p>
		<p>
			<label>国家：</label> 
			<span>
				<trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${hotel.country}"/>
			</span>
		</p>
		<p>
			<label>省：</label> 
			<span>
				<trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${hotel.province}"/>
			</span>
		</p>
		<p>
			<label>市：</label> 
			<span>
				<trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${hotel.city}"/>
			</span>
		</p>
		<p>
			<label>区：</label> 
			<span>
				<trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${hotel.district}"/>
			</span>
		</p>
		<p>
			<label>详细地址：</label> 
			<span>
				${hotel.shortAddress}
			</span>
		</p>
		<p>
			<label>酒店类型：</label> 
			<span>
				${hotel.areaType=="1"?"内陆":"海岛"}
			</span>
		</p>
		
		<p>
			<label>酒店品牌：</label> 
			<span>
				${hotel.brand}
			</span>
		</p>
		<p>
			<label>酒店星级：</label> 
			<span>
				<trekiz:autoId2Name4Class classzName="HotelStar" sourceProName="uuid" srcProName="label" value="${hotel.star}"/>
			</span>
		</p>
		<p>
			<label>层高：</label> 
			<span>
				${hotel.floor}
			</span>
		</p>
		<p>
			<label>开业日期：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${hotel.openingDate}"/>
			</span>
		</p>
		<p>
			<label>最后装修日期：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${hotel.lastDecoDate}"/>
			</span>
		</p>
		<p>
			<label>酒店主题：</label> 
			<span>
				<trekiz:defineDict name="type" readonly="true" input="checkbox" type="hotel_topic" defaultValue="${hotel.topic}" />
			</span>
		</p>
		<p>
			<label>酒店设施：</label> 
			<span>
				<trekiz:defineDict name="type" readonly="true" input="checkbox" type="hotel_facilities" defaultValue="${hotel.facilities}" />
			</span>
		</p>
		<p>
			<label>酒店特色：</label> 
			<span>
				<trekiz:autoId2Name4Class classzName="HotelFeature" sourceProName="uuid" srcProName="name" value="${hotel.feature}"/>
			</span>
		</p>
		<p>
			<label>关联游客类型：</label> 
			<span>
			<c:forEach items="${httrList }" var="hotelTraveltypeRel" varStatus="relation">
					<c:if test="${!relation.last }">${hotelTraveltypeRel.travelerTypeName}、</c:if>
					<c:if test="${relation.last }">${hotelTraveltypeRel.travelerTypeName}</c:if>
				</c:forEach>
			</span>
		</p>
		<p>
			<label>地址：</label> 
			<span>
				${hotel.address}
			</span>
		</p>
		<p>
			<label>网址：</label> 
			<span>
				${hotel.website}
			</span>
		</p>
		<p>
			<label>电话：</label> 
			<span>
				${hotel.telephone}
			</span>
		</p>
		<p>
			<label>传真：</label> 
			<span>
				${hotel.fax}
			</span>
		</p>
		<p>
			<label>负责人：</label> 
			<span>
				${hotel.chargePerson}
			</span>
		</p>
		<p>
			<label>手机：</label> 
			<span>
				${hotel.chargeMobile}
			</span>
		</p>
		<p>
			<label>电话：</label> 
			<span>
				${hotel.chargeTelephone}
			</span>
		</p>
		<p>
			<label>传真：</label> 
			<span>
				${hotel.chargeFax}
			</span>
		</p>
		<p>
			<label>邮箱：</label> 
			<span>
				${hotel.chargeEmail}
			</span>
		</p>
		<p>
			<label>摘要：</label> 
			<span>
				${hotel.remark}
			</span>
		</p>
		<p>
			<label>描述：</label> 
			<span>
				${hotel.description}
			</span>
		</p>
		<p>
			<label>入住日期：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${hotel.inDate}"/>
			</span>
		</p>
		<p>
			<label>离店日期：</label> 
			<span>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${hotel.outDate}"/>
			</span>
		</p>
		<c:if test="${hotel.areaType==2 }">
		<p>
			<label>岛屿：</label> 
			<span>
				<trekiz:autoId2Name4Table tableName="island" sourceColumnName="uuid" srcColumnName="island_name" value="${hotel.islandUuid}"/>
			</span>
		</p>
		<p>
			<label>上岛方式：</label> 
			<span>
				<trekiz:defineDict name="type" readonly="true" input="checkbox" type="islands_way" defaultValue="${hotel.islandWay}" />
			</span>
		</p>
		</c:if>
		
		<p class="maintain_btn">
			<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
		</p>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
