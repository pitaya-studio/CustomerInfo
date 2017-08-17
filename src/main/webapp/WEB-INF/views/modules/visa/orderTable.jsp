<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>预约表</title>
<meta name="decorator" content="wholesaler" />
    
</head>
	<body>
		<div class="mod_nav">订单 > 签证 > 预约表</div>
		<div class="ydbz_tit orderdetails_titpr">
			预约表
		</div>
		
		<table id="contentTable" class="table activitylist_bodyer_table">
			<thead>
				<tr>
					<th width="7%">姓名</th>
					<th width="9%">英文名</th>
					<th width="5%">性别</th>
					<th width="8%">约签日期</th>
					<th width="8%">约签时间</th>
					<th width="7%">约签国家</th>
					<th width="8%">签证类型</th>
					<th width="5%">领区</th>
					<th width="5%">证件</th>
					<th width="8%">护照号</th>
					<th width="8%">出生日期</th>
					<th width="5%">销售</th>
					<th width="7%">签证结果</th>
					<th width="10%">AA码</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${orderTables}" var="orderTable">
					<tr>
						<td>${orderTable[0]}</td>
						<td>${orderTable[1]}</td>
						<td>
							<c:if test="${not empty orderTable[2]}">
								<c:choose>
									<c:when test="${orderTable[2] eq '1'}">男</c:when>
									<c:otherwise>女</c:otherwise>
								</c:choose>
							</c:if>
						</td>
						<td>${orderTable[3][0]}</td>
						<td>${fn:substring(orderTable[3][1],0,8) }</td>
						<td>${orderTable[4]}</td>
						<td>${orderTable[5]}</td>
						<td>${orderTable[6]}</td>
						<td>护照</td>
						<td>${orderTable[7]}</td>
						<td>${orderTable[8]}</td>
						<td>${orderTable[9]}</td>
						<td></td>
						<td>${orderTable[11]}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</body>
</html>