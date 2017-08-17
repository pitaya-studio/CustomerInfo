<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title><c:if test="${type eq '1' }">机票-日期机票库存详情</c:if><c:if test="${type eq '2' }">机票-机票库存详情</c:if></title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
	<script type="text/javascript">
		//下载文件
	function downloads(docid){
		window.open("${ctx}/sys/docinfo/download/"+docid);
    }
	</script>
	
</head>
<body>
						<!--右侧内容部分开始-->
						<div class="ydbz_tit pl20"><c:if test="${type eq '1' }">机票-日期机票库存详情</c:if><c:if test="${type eq '2' }">机票-机票库存详情</c:if></div>
						<form method="post" action="" class="form-horizontal" id="" novalidate="">
							<div class="maintain_add">
								<p>
									<label>
										<span class="xing">*</span>控票名称：</label>
										<span>
											${flightControl.name}
										</span>
									
								</p>
								<p>
									<label>
										<span class="xing">*</span>国家：</label>
										<span>
											<trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${flightControl.country}"/>
										</span>
								</p>
								<p>
									<label>
										<span class="xing">*</span>岛屿名称：</label>
										<span>
											<trekiz:autoId2Name4Table tableName="island" sourceColumnName="uuid" srcColumnName="island_name" value="${flightControl.islandUuid}"/>
										</span>
									
								</p>
								<p class="maintain_kong"></p>
								<p>
									<label>
								  <span class="xing">*</span>航空公司：</label>
								  <span>
								  	${fns:getAirlineNameByAirlineCode(flightControl.airline)}
								  </span>
								</p>
								<p>
									<label>
										<span class="xing">*</span>币种选择：</label>
										<span>
											<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_name" value="${flightControl.currencyId}"/>
										</span>
									
								</p>
								<p class="maintain_kong"></p>
							</div>
						</form>
						<table id="contentTable" class="table activitylist_bodyer_table">
							<thead>
								<tr>
									<th width="8%">日期</th>
									<th width="10%">舱位等级</th>
									<th width="10%">价格</th>
									<th width="8%">总税</th>
									<th width="6%">库存</th>
									<th width="6%">参考酒店</th>
									<th width="6%">备注</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${flightControl.flightControlDetails }" var="entry">
								<tr>
								  <td class="tc"><fmt:formatDate value="${entry.departureDate }" pattern="yyyy-MM-dd"/></td>
								  <td class="tc"><p>
								  	${fns:getDictLabel(entry.spaceGradeType,"spaceGrade_Type" , "无")}
								  </p></td>
								  <td class="tc"><p>
								  <trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${entry.priceCurrencyId }"/>
								  ${entry.price }</p></td>
								  <td class="tc">
								  	<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${entry.taxesCurrencyId }"/>
								  	${entry.taxesPrice }</td>
								  <td class="tc">${entry.stock }</td>
								  <td class="tc">
								  <span>
								  <c:forEach items="${entry.flightControlHotelDetails }" var="fchd" varStatus="v">
								  	<trekiz:autoId2Name4Table tableName="hotel" sourceColumnName="uuid" srcColumnName="short_name_cn" value="${fchd.hotelUuid }"/>
								  <c:if test="${v.last==false}"><br/></c:if>
								  </c:forEach>
								  </span><a class="addHouseType" href="javascript:;"></a></td>
								  <td class="tc"><span>${entry.memo }</span><a class="addHouseType" href="javascript:;"></a></td>
							  	</tr>
							  	</c:forEach>
							</tbody>
						</table>
						<!--右侧内容部分结束-->
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
											<a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(null,'hotelAnnexDocId',this)">删除</a>
										</li>
									</c:forEach>
								</ol>
							</p>
							<p class="maintain_pfull new_kfang">
								<label>备注：</label>
                                ${flightControl.memo }
								
							</p>
						</div>
						<div class="release_next_add">
						  <input type="button" class="btn btn-primary" value="关闭" onclick="window.close()">
						</div>
					</div>
</body>
</html>
