<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>酒店星级维护</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	

	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
	<script type="text/javascript">
		function show(uuid){
			window.open( "${ctx}/hotelStar/show/" + uuid) ;
		}
		function edit(uuid){
			window.open("${ctx}/hotelStar/edit/" + uuid) ;
		}
		function del(uuid){
			$.jBox.confirm("确定要删除数据吗？", "系统提示", function(v, h, f) {
				if (v == 'ok') {
					$.jBox.tip("正在删除数据...", 'loading');
					$.post("${ctx}/hotelStar/delete", {"uuid":uuid},
						function(data){
							if(data.result=="1"){
								top.$.jBox.tip("删除成功!");
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
			if($(".tc").length>0){
				$.jBox.confirm("确定要修改排序吗？", "系统提示", function(v, h, f) {
					if (v == 'ok') {
						$.jBox.tip("正在修改排序...", 'loading');
						var uuidAndSorts = [];
						$(".tc").each(function(){
							var uuid = $(this).find("input[name=uuid]").val();
							var sort = $(this).find(".maintain_sort").val();
							uuidAndSorts.push(uuid+","+sort);
						});
						$.post("${ctx}/hotelStar/updateOrder", {"uuidAndSortsStr":uuidAndSorts.join(";")},
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
				}, {persistent: true});
			}
		}
		
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/sysCompanyDictView/list");
			$("#searchForm").submit();
	    }
	</script>	
</head>
<body>
<page:applyDecorator name="sys_menu_head" >
    <page:param name="showType">${param.type}</page:param>
</page:applyDecorator>
	<div>
		<!--右侧内容部分开始-->
		<div class="filter_btn">
			<form id="searchForm" action="${ctx}/sysCompanyDictView/list" method="post">
				<input id="type" name="type" type="hidden" value="hotel_star" />
				<input type="hidden" name="pageNo" id="pageNo" value="${page.pageNo}"/>
				<input type="hidden" name="pageSize" id="pageSize" value="${page.pageSize}"/>
			</form>
			<%--<a class="btn btn-primary" href="${ctx}/hotelStar/form" target="_blank">添加酒店星级</a>--%>
			<input class="btn btn-primary ydbz_x" onclick="window.open('${ctx}/hotelStar/form')" value="添加酒店星级" type="button">

		</div>
              <table class="activitylist_bodyer_table mainTable">
                  <thead>
                      <tr>
                      	<th width="10%">排序</th>
                          <th width="">酒店星级</th>
                          <th width="">星级类型</th>
                          <th width="">描述</th>
                          <th width="15%">操作</th>
                      </tr>
                  </thead>
                  <tbody>
                  	<c:forEach var="hotelStar" items="${page.list}" varStatus="v">
	                  	  <tr>
	                          <td class="tc">
	                  	  	  	<input name="uuid" type="hidden" value="<c:out value="${hotelStar.uuid}" />"/>
	                          	<input type="text" value="${hotelStar.sort}" class="maintain_sort" maxlength="4" onblur="this.value=this.value.replace(/\D/g,'');"  onkeyup="this.value=this.value.replace(/\D/g,'');"   onafterpaste="this.value=this.value.replace(/\D/g,'');" disabled="disabled"/>
	                          </td>
	                          <td>${hotelStar.label}</td>
	                          <td>${hotelStar.sysCompanyDictView.label}</td>
	                          <td>${hotelStar.description}</td>
	                          <td class="tda">
	                          	  <c:if test="${hotelStar.wholesalerId == wholesalerId}">
		                              <a href="javascript:void(0)" onclick="edit('${hotelStar.uuid}')">修改</a>
		                              <a href="javascript:void(0)" onclick="del('${hotelStar.uuid}')">删除</a>
	                              </c:if>
	                              <a href="javascript:void(0)" onclick="show('${hotelStar.uuid}')">详情</a>
	                          </td>
	                      </tr>
                  	</c:forEach>
                  </tbody>
              </table>
          	<div class="page">
                  <div class="pagination">
                      <dd>
                          <%--<a href="javascript:void(0)" onclick="updateOrder(this);" class="ydbz_x">修改排序</a>--%>
						  <input class="btn btn-primary ydbz_x" onclick="updateOrder(this);" value="修改排序" type="button">

					  </dd>
                      <div class="paginations">
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
