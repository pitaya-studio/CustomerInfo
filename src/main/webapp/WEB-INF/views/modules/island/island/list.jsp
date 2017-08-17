<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>基础信息维护-岛屿管理</title>
	<meta name="decorator" content="wholesaler"/>
	<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	
	<script type="text/javascript">
		$(function(){
			//搜索条件筛选
			launch();
			//产品名称文本框提示信息
			inputTips();
			//初始化所属国家下拉列表框
			changePosition($("#position"));
			
			var selectRequest = false;

			if(($("#position").val() != "") || ($("#type").val() != "") || ($("#country").val() != "")) {
				selectRequest = true;
			}
			
			if(selectRequest) {
				$('.zksx').click();
			}
			
		});
		function show(uuid){
			window.open( "${ctx}/island/show/" + uuid) ;
		}
		function edit(uuid){
			window.open("${ctx}/island/edit/" + uuid) ;
		}
		function del(uuid){
			$.jBox.confirm("确定要删除数据吗？", "系统提示", function(v, h, f) {
				if (v == 'ok') {
					$.jBox.tip("正在删除数据...", 'loading');
					$.post("${ctx}/island/delete", {"uuid":uuid},
						function(data){
							if(data.result=="1"){
								top.$.jBox.tip('删除成功!');
								$("#searchForm").submit();
							}else{
								top.$.jBox.tip(data.message,'warning');
							}
						}
					);
				}
			});
		}
		
		function orderSubmitCallBack(){
			if($(".sort").length>0){
				$.jBox.confirm("确定要修改排序吗？", "系统提示", function(v, h, f) {
					if (v == 'ok') {
						$.jBox.tip("正在修改排序...", 'loading');
						var uuidAndSorts = [];
						$(".sort").each(function(){
							var uuid = $(this).find("input[name=uuid]").val();
							var sort = $(this).find(".maintain_sort").val();
							uuidAndSorts.push(uuid+"-"+sort);
						});
						$.post("${ctx}/island/updateOrder", {"uuidAndSortsStr":uuidAndSorts.join(",")},
							function(data){
								if(data.result=="1"){
									top.$.jBox.tip("修改排序成功!");
									$("#searchForm").submit();
								}else{
									top.$.jBox.tip(data.message,'warning');
								}
							}
						);
						
					} else if (v == 'cancel') {
			             $("#searchForm").submit();
					}
				});
			}
		}
		
		function submitForm(obj) {
			$("#pageSize").val($(obj).val());
			$("#searchForm").submit();
		}
		
		//境内境外 改变地址展现形式
		function changePosition(){
			initSuggest({"type":$("#position").val()});
		}
		
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/island/list");
			$("#searchForm").submit();
	    }
		
		//排序
		function updateOrder(obj){
			var txt=$(obj).text();
			if(txt=="修改排序"){
				$(obj).text("提交");
				$("input[class='maintain_sort']").removeAttr("disabled");
			}else{
				$(obj).text("修改排序");
				$("input[class='maintain_sort']").attr("disabled","disabled");
				//提交修改排序后的回调函数
				orderSubmitCallBack();
			}
		}
	</script>
</head>
<body>
<page:applyDecorator name="sys_menu_head" >
    <page:param name="showType">${type}</page:param>
</page:applyDecorator>
	<div>
		<!--右侧内容部分开始-->
		<div class="activitylist_bodyer_right_team_co_bgcolor">
			<form:form method="post" modelAttribute="island" action="${ctx}/island/list" class="form-horizontal" id="searchForm" novalidate="">
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				<input type="hidden" name="pageSize" id="pageSize" value="${page.pageSize}"/>
				<div class="activitylist_bodyer_right_team_co">
					<div class="activitylist_bodyer_right_team_co2 pr">
						<input class="txtPro inputTxt searchInput" id="islandName" name="islandName" value="${island.islandName }" type="text"
							   placeholder="请输入岛屿名称" />
					</div>
					<a class="zksx">筛选</a>
					<div class="form_submit">
						<input class="btn btn-primary" value="搜索" type="submit" />
					</div>
					<p class="main-right-topbutt">
						<a class="btn btn-primary" href="${ctx}/island/form" target="_blank">添加岛屿</a>
					</p>
					<div class="ydxbd">
						<span></span>
						<div class="activitylist_bodyer_right_team_co1">
							<label class="activitylist_team_co3_text">位置属性：</label>
							<div class="selectStyle">
								<form:select path="position" class="domesticOverseas required" onchange="changePosition(this);">
									<form:option value="">请选择</form:option>
									<form:option value="1">境内岛屿</form:option>
									<form:option value="2">境外岛屿</form:option>
								</form:select>
							</div>
						</div>
						<div class="activitylist_bodyer_right_team_co1">
							<label class="activitylist_team_co3_text">岛屿类型：</label>
							<div class="selectStyle">
								<trekiz:defineDict name="type" type="islands_type" defaultValue="${island.type}" />
							</div>
						</div>
						<div>
							<label class="activitylist_team_co3_text">所属国家：</label>
							<trekiz:suggest name="country" style="width:150px;" defaultValue="${island.country}" displayValue="${countryName}" ajaxUrl="${ctx}/geography/getGeoListAjax" />
						</div>
					</div>
				</div>
			</form:form>
		</div>
		<table class="activitylist_bodyer_table mainTable">
                    <thead>
                        <tr>
                        	<th width="10%">排序</th>
                            <th width="">岛屿名称</th>
                            <th width="">属性位置</th>
                            <th width="">国家</th>
                            <th width="">岛屿类型</th>
                            <th width="15%">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                    	<c:forEach var="entity" items="${page.list}" varStatus="v">
                    	<tr>
                            <td class="tc sort">
                            	<input type="hidden" name="uuid" value="${entity.uuid }" />
                            	 <input type="text" value="${entity.sort }" maxlength="4" class="maintain_sort" onblur="this.value=this.value.replace(/\D/g,'');"  onkeyup="this.value=this.value.replace(/\D/g,'');"   onafterpaste="this.value=this.value.replace(/\D/g,'');" disabled="disabled"/>
                            	<%-- <input type="text" value="${entity.sort }" maxlength="4"  class="maintain_sort" onblur="this.value=this.value.replace(/\D/g,'');"  onkeyup="this.value=this.value.replace(/\D/g,'');"   onafterpaste="this.value=this.value.replace(/\D/g,'');" disabled="disabled"/> --%>
                            </td>
                            <td class="tc">${entity.islandName }</td>
                            <td>
                            	<c:choose>
                            		<c:when test="${entity.position == 1 }">
                            			境内
                            		</c:when>
                            		<c:when test="${entity.position == 2 }">
                            			境外
                            		</c:when>
                            	</c:choose>
                            </td>
                            <td><trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${entity.country }"/></td>
                            <td><trekiz:defineDict name="type" type="islands_type" defaultValue="${entity.type}" readonly="true"/></td>
                            <td class="tda tc">
                                <a href="javascript:void(0);" onclick="edit('${entity.uuid}')" >修改</a>
                                <a href="javascript:void(0);" onclick="del('${entity.uuid}')" >删除</a>
                                <a href="javascript:void(0);" onclick="show('${entity.uuid}')" >详情</a>
                            </td>
                        </tr>
                        </c:forEach>
                    </tbody>
                </table>
            	<div class="page">
                    <div class="pagination">
                        <dd>
                            <%--<a onclick="updateOrder(this)" class="ydbz_x">修改排序</a>--%>
							<input class="btn btn-primary" onclick="updateOrder(this)" value="修改排序" type="button" />

						</dd>
						<span class="activitylist_page_num">
						
						</span>
						<div class="endPage">
							 ${page}
						</div>
                        <div style="clear:both;"></div>
                    </div>	
				</div>
                <!--右侧内容部分结束-->
	</div>
</body>
</html>
<script type="text/javascript">


</script>
