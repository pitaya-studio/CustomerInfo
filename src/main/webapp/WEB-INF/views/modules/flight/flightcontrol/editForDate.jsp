<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>机票-日期机票库存修改</title>
	<meta name="decorator" content="wholesaler"/>
	<link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/js/jquery-migrate-1.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/default.validator.js"  type="text/javascript"></script>
	<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/tmp.basicInfo.js" type="text/javascript" ></script>
		<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				rules:{
					
				},
				submitHandler: function(form){
					var url="${ctx}/flightControl/updateForFlight";
					$("#btnSubmit").attr("disabled", "disabled");
					$.post(url,$("#inputForm").serialize(),function(data){
						if(data.message=="2"){
							$.jBox.tip("修改成功!");
						}else if(data.message=="3"){
							$.jBox.tip(data.error,'warning');
							$("#btnSubmit").attr("disabled", false);
						}else{
							$.jBox.tip('系统异常，请重新操作!','warning');
							$("#btnSubmit").attr("disabled", false);
						}
					});
				}
			});
		});
	</script>
	<script>
	$(function() {
		//搜索条件筛选
		launch();
		//产品名称文本框提示信息
		inputTips();
		//操作浮框
		operateHandler();
		//初始化国家的标签
		initSuggest({});
		
		var $contentTable = $("#contentTable");
		$("select[name='country']").combobox();
		$("select[name='islands']").combobox();
		$("select[name='hotelname']").combobox();
		$("select[name='currencyId']").on('change',function(){
			$contentTable.find('span.currency').text($("select[name='currencyId']").val());
			$contentTable.find('select.currency').val($("select[name='currencyId']").val());
		});
		
		var $templateTr = $contentTable.find("tbody tr:first").clone();
		$templateTr.find('a.saveAndNew').after('<span class="jiange_li">|</span><a data-delTag="tr" href="javascript:void(0)">删除</a>')
		$contentTable.on("click", "a[data-delTag]", function() {
			// 删除
			var $this = $(this);
			var delTag = $this.attr("data-delTag");
			$contentTable.find(delTag).has($this).remove();
		}).on('click', 'a.saveAndNew', function() {
			// 保存并新增
			var $tr = $(this).parent().parent();
			$tr.after(saveTr($tr));
		}).on('click', 'a.new', function() {
			// 新增
			var $tr = $(this).parent().parent();
			var $newTr = editTr($tr);
			$tr.after($newTr);
		}).on('click', 'a.save', function() {
			// 保存
			var $tr = $(this).parent().parent();
			$tr.replaceWith(saveTr($tr));
		}).on('click', 'a.edit', function() {
			// 编辑
			var $tr = $(this).parent().parent();
			var $newTr = editTr($tr).data("source", $tr);
			$newTr.find('td:last').html('<a class="save" href="javascript:void(0)">保存</a><span class="jiange_li">|</span><a class="cancel" href="javascript:void(0)">取消</a>');
			$tr.replaceWith($newTr);
		}).on('click', 'a.cancel', function() {
			// 取消
			var $tr = $(this).parent().parent();
			var $source = $tr.data("source");
			$tr.replaceWith($source);
		}).on('click', 'a.addHotel', function() {
			//增加参考酒店
			addHotel($(this).parent().parent());
		});
		// 保存
		function saveTr($tr) {
			var $newTr = $templateTr.clone();
			var $newTds = $newTr.find("td").empty();
			var $oldTds = $tr.find("td");
			var ci = 0;
			// 出发日期
			$newTds.eq(ci).text($oldTds.eq(ci).find("input").val());
			//add by wangxk
			$newTds.eq(ci).append("<input type=hidden name='departureDates'>");
			$newTds.eq(ci).find("input[name=departureDates]").val($newTds.eq(ci).text());
			ci++;
			// 舱位等级
			$newTds.eq(ci).text($oldTds.eq(ci).find("option:selected").text());
			// add by wangxk
			$newTds.eq(ci).append("<input type=hidden name='spaceGradeTypes'>");
			$newTds.eq(ci).find("input[name=spaceGradeTypes]").val($oldTds.eq(ci).find("select").val());
			
			ci++;
			// 总价格
			$newTds.eq(ci).html('<span class="currency">' + $oldTds.eq(ci).find("option:selected").text() + '</span><span>' + $oldTds.eq(ci).find("input").val() + '</span>');
			//add by wangxk  币种id+总额 
			$newTds.eq(ci).append("<input type=hidden name='priceCurrencyIds'>");
			$newTds.eq(ci).find("input[name=priceCurrencyIds]").val($oldTds.eq(ci).find("select").val());
			$newTds.eq(ci).append("<input type=hidden name='prices'>");
			$newTds.eq(ci).find("input[name=prices]").val($oldTds.eq(ci).find("input").val());
			
			ci++;
			// 总税
			$newTds.eq(ci).html('<span class="currency">' + $oldTds.eq(ci).find("option:selected").text() + '</span><span>' + $oldTds.eq(ci).find("input").val() + '</span>');
			//add by wangxk  总税币种id+总额 
			$newTds.eq(ci).append("<input type=hidden name='taxesCurrencyIds'>");
			$newTds.eq(ci).find("input[name=taxesCurrencyIds]").val($oldTds.eq(ci).find("select").val());
			$newTds.eq(ci).append("<input type=hidden name='taxesPrices'>");
			$newTds.eq(ci).find("input[name=taxesPrices]").val($oldTds.eq(ci).find("input").val());
			
			ci++;
			// 库存
			$newTds.eq(ci).text($oldTds.eq(ci).find("input").val());
			//add by wangxk
			$newTds.eq(ci).append("<input type=hidden name='stocks'>");
			$newTds.eq(ci).find("input[name=stocks]").val($oldTds.eq(ci).find("input").val());
			ci++;
		    // 参考酒店
			//add by wangxk
			var hotelUuid=[];
			$.each($oldTds.eq(ci).find("p"), function() {
				var type = $(this).find("select").val();
				var text = $(this).find("option:selected").text();
				$newTds.eq(ci).append('<p><span> ' + text + ' </span> ');
				hotelUuid.push(type);
			});
			
			$newTds.eq(ci).append("<input type=hidden name='hotelUuids'>");
			$newTds.eq(ci).find("input[name=hotelUuids]").val(hotelUuid.join(";"));
			ci++;
			// 备注
			$newTds.eq(ci).text($oldTds.eq(ci).find("input").val());
			//add by wangxk
			$newTds.eq(ci).append("<input type=hidden name='memos'>");
			$newTds.eq(ci).find("input[name=memos]").val($oldTds.eq(ci).find("input").val());
			ci++;
			// 操作
			$newTds.eq(ci).html('<a class="new" href="javascript:void(0)">新增</a><span class="jiange_li">|</span><a class="edit" href="javascript:void(0)">修改</a><span class="jiange_li">|</span><a data-delTag="tr" href="javascript:void(0)">删除</a>');
			return $newTr;
		}
		
		// 编辑
		function editTr($tr) {
				var $newTr = $templateTr.clone();
				var $newTds = $newTr.find("td");
				var $oldTds = $tr.find("td");
				var ci = 0;
				// 入住日期
				$newTds.eq(ci).find("input.dateinput").val($oldTds.eq(ci).text());
				ci++;
				// 舱位等级
				$newTds.eq(ci).find("select").val($oldTds.eq(ci).text());
				ci++;
			    // 价格
				$newTds.eq(ci).find("select").val($oldTds.eq(ci).find('span').eq(0).text());
				$newTds.eq(ci).find("input").val($oldTds.eq(ci).find('span').eq(1).text());
				ci++;
				// 总税
				$newTds.eq(ci).find("select").val($oldTds.eq(ci).find('span').eq(0).text());
				$newTds.eq(ci).find("input").val($oldTds.eq(ci).find('span').eq(1).text());
				ci++;
			    // 库存
				$newTds.eq(ci).find("input").val($oldTds.eq(ci).text());
				ci++;
				// 参考酒店
				$.each($oldTds.eq(ci).find("p"), function(i) {
					if (i == 0) {
						$newTds.eq(ci).find('p select').val($(this).text());
					} else {
						addHotel($newTds.eq(ci), $(this).text());
					}
				});
				ci++;
			    // 备注
				$newTds.eq(ci).find("input").val($oldTds.eq(ci).text());
				return $newTr;
			}
			
			/// 增加参考酒店
		function addHotel($td, val) {
				var $newP = $td.find("p:first").clone();
				$newP.find("a").replaceWith('<a href="javascript:;" data-delTag="p">删除</a>');
				if (val) {
					$newP.find("input.wtext").val(val);
				}
				$td.append($newP);
			}
});
	</script>
	
	<script>
	//级联查询
	function getAjaxSelect(type,obj){
		$.ajax({
			type: "POST",
		   	url: "${ctx}/hotelControl/ajaxCheck",
		   	data: {
					"type":type,
					"uuid":$(obj).val()
				  },
			dataType: "json",
		   	success: function(data){
		   		if(type == "islandway"){
		   			$("select[name=islandWays]").empty();
		   			$("#hotelUuid").empty();
			   		$("select[name=islandWays]").append("<option value=''>不限</option>");
			   		$("#hotelUuid").append("<option value=''>不限</option>");
		   		} else if(type == "roomtype"){
		   			$("select[name=roomType]").empty();
			   		$("select[name=roomType]").append("<option value=''>不限</option>");
		   		} else if(type == "island"){
		   			$("select[name=islandUuid]").empty();
			   		$("select[name=islandUuid]").append("<option value=''>不限</option>");
			   		
		   			$("#hotelUuid").empty();
			   		$("#hotelUuid").append("<option value=''>不限</option>");
			   		
		   			$("select[name=islandWays]").empty();
			   		$("select[name=islandWays]").append("<option value=''>不限</option>");
			   		
		   			$("select[name=roomType]").empty();
			   		$("select[name=roomType]").append("<option value=''>不限</option>");
			   		
		   		}
		   		if(data){
		   			if(type=="hotel"){
			   			$.each(data.hotelList,function(i,n){
			   					 $("#"+type).append($("<option/>").text(n.nameCn).attr("value",n.uuid));
			   			});
		   			}else if(type=="roomtype"){
		   				$.each(data.roomtype,function(i,n){
		   					 var arr = $("select[name=roomType]");
		   					 for(var j=0; j<arr.length; j++) {
		   					 	$(arr[j]).append($("<option/>").text(n.roomName).attr("value",n.uuid));
		   					 }
		   				});
		   			}else if(type=="islandway"){
		   				$.each(data.listIslandWay,function(i,n){
		   					 var arr = $("select[name=islandWays]");
		   					 for(var j=0; j<arr.length; j++) {
		   					 	$(arr[j]).append($("<option/>").text(n.label).attr("value",n.uuid));
		   					 }
		   				});
		   				$.each(data.hotelList,function(i,n){
		   					 $("#hotelUuid").append($("<option/>").text(n.nameCn).attr("value",n.uuid));
		   				});
		   			}else if(type=="island"){
		   				$.each(data.islandList,function(i,n){
		   					 $("#islandUuid").append($("<option/>").text(n.islandName).attr("value",n.uuid));
		   				});
		   			}
		   		}
		   	}
		});
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
<div>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">日期机票修改</div>
	<form:form method="post" modelAttribute="flightControlInput" action="" class="form-horizontal" id="inputForm" novalidate="">
							<div class="maintain_add">
								<p>
									<label>
										<span class="xing">*</span>控票名称：</label>
									 	<form:input path="name" htmlEscape="false" maxlength="49"/>
								</p>
								<p>
									<label>
										<span class="xing">*</span>国家：</label>
										<form:hidden path="country" />
										<trekiz:autoId2Name4Table tableName='sys_geography' sourceColumnName='uuid' srcColumnName='name_cn' value='${flightControlInput.country}'/>
								</p>
								<p>
									<label>
										<span class="xing">*</span>岛屿名称：</label>
										<form:hidden path="islandUuid" />
										<trekiz:autoId2Name4Class classzName="Island" sourceProName="uuid" srcProName="islandName" value="${flightControlInput.islandUuid }"/>
								</p>
								<p class="maintain_kong"></p>
								<p>
									<label>
								  <span class="xing">*</span>航空公司：</label>
								  ${fns:getAirlineNameByAirlineCode(flightControlInput.airline)}					  
								</p>
								<p>
									<label>
										<span class="xing">*</span>币种选择：</label>
											<form:hidden path="currencyId" />
											<c:forEach items="${currencyList}" var="item">
												<c:if test="${flightControlInput.currencyId==item.id}">${item.currencyName}</c:if>
											</c:forEach>
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
								<c:forEach items="${flightControlInput.flightControlDetails }" var="ff">
									<tr>
										<td class="tc">
											<fmt:formatDate value="${ff.createDate }" pattern="yyyy.MM.dd"/> 
										</td>
										<td class="tc">
											<p>
												<c:forEach items="${spaceGradelist }"  var="sg">
														<c:if test="${sg.value==ff.spaceGradeType }">
															${sg.label}
														</c:if>
												</c:forEach>											
											</p>
										</td>
										<td class="tc">
											<p>
												<select name="detailCurrencys" class="wnum2 currency">
													<c:forEach items="${currencyList}" var="item">
													    <option value="${item.id}" 
													    	<c:choose>
													    		<c:when test="${ff.priceCurrencyId==item.id}">selected="selected"</c:when>
													    		<c:when test="${ff.priceCurrencyId == null && item.currencyName == '美元'}">selected="selected"</c:when>
													    	</c:choose>>
													    	${item.currencyMark}
													    </option>
													</c:forEach>
												</select>
						                        <input type="text" class="wnum2"  value="${ff.price }"/>
											 </p>
										</td>
										<td class="tc">
											<select name="detailCurrencys" class="wnum2 currency">
													<c:forEach items="${currencyList}" var="item">
													    <option value="${item.id}" 
													    	<c:choose>
													    		<c:when test="${ff.taxesCurrencyId==item.id}">selected="selected"</c:when>
													    		<c:when test="${ff.taxesCurrencyId == null && item.currencyName == '美元'}">selected="selected"</c:when>
													    	</c:choose>>
													    	${item.currencyMark}
													    </option>
													</c:forEach>
												</select>
										  <input type="text" class="wnum2" value="${ff.taxesPrice }" />
										</td>
										<td class="tc"><input type="text" class="wtext" value="${ff.stock }"/></td>
										<td class="tc">
											<p>
											    <select name="select3" class="wtext">
											   		   <option>希尔顿</option>
											    </select>
			                                    <a class="addHotel" href="javascript:void(0);">新增</a>
		                                    </p>
	                                    </td>
										<td class="tc">
											<input type="text" class="wtext" value="${ff.memo }"/>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
				<div class="release_next_add">
					<input type="button" value="取消" class="btn btn-primary gray" onclick="window.close();" />
					<input type="submit" value="保存修改" class="btn btn-primary" id="btnSubmit"/>
				</div>
		</form:form>
	<!--右侧内容部分结束-->
	</div>
</body>
</html>
