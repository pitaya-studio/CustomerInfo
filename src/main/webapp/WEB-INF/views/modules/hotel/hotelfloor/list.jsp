<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>基础信息维护-酒店楼层管理</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>

	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
	
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
	<script type="text/javascript">
	function show(uuid){
		window.open( "${ctx}/hotelFloor/show/" + uuid) ;
	}
	function edit(uuid){
		window.open("${ctx}/hotelFloor/edit/" + uuid) ;
	}
	function del(uuid){
		$.jBox.confirm("确定要删除数据吗？", "系统提示", function(v, h, f) {
			if (v == 'ok') {
				$.jBox.tip("正在删除数据...", 'loading');
				$.post("${ctx}/hotelFloor/delete", {"uuid":uuid},
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
		if($(".sort").length>0){
			$.jBox.confirm("确定要修改排序吗？", "系统提示", function(v, h, f) {
				if (v == 'ok') {
					$.jBox.tip("正在修改排序...", 'loading');
					var uuidAndSorts = [];
					$(".sort").each(function(){
						var uuid = $(this).find("input[name=uuid]").val();
						var sort = $(this).find(".maintain_sort").val();
						uuidAndSorts.push(uuid+","+sort);
					});
					$.post("${ctx}/hotelFloor/updateOrder", {"uuidAndSortsStr":uuidAndSorts.join(";")},
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
	</script>	
</head>
<body>
	<div>
		<!--右侧内容部分开始-->
		<div class="filter_btn">
			<form id="searchForm" action="${ctx}/hotelFloor/list/${hotelUuid}/${hotelRoomUuid}" method="post">
				
			</form>
			<a class="btn btn-primary" href="${ctx}/hotelFloor/form/${hotelUuid}/${hotelRoomUuid}" target="_blank">添加楼层</a>
		</div>
              <table class="t-type t-type100 tablemt50">
                  <thead>
                      <tr>
                      	<th width="10%">排序</th>
                          <th width="">楼层</th>
                          <th width="">描述</th>
                          <th width="15%">操作</th>
                      </tr>
                  </thead>
                  <tbody>
                  	<c:forEach var="hotelFloor" items="${hotelFloors}" varStatus="v">
	                  	  <tr>
	                          <td class="sort">
	                  	  	  	<input name="uuid" type="hidden" value="<c:out value="${hotelFloor.uuid}" />"/>
	                          	<input type="text" value="${hotelFloor.sort}" class="maintain_sort" maxlength="4"  onblur="this.value=this.value.replace(/\D/g,'');"  onkeyup="this.value=this.value.replace(/\D/g,'');"   onafterpaste="this.value=this.value.replace(/\D/g,'');" disabled="disabled"/>
	                          </td>
	                          <td class="tc">${hotelFloor.floorName}</td>
	                          <td>${hotelFloor.description}</td>
	                          <td class="tda tc">
	                              <a href="javascript:void(0)" onclick="edit('${hotelFloor.uuid}')">修改</a>
	                              <a href="javascript:void(0)" onclick="del('${hotelFloor.uuid}')">删除</a>
	                              <a href="javascript:void(0)" onclick="show('${hotelFloor.uuid}')">详情</a>
	                          </td>
	                      </tr>
                  	</c:forEach>
                  </tbody>
              </table>
          	<div class="page">
                  <div class="pagination">
                      <dl>
                          <a href="javascript:void(0)" onclick="updateOrder(this);" class="ydbz_x">修改排序</a>
                      </dl>
                      <div style="clear:both;"></div>
                  </div>	
		</div>
        <!--右侧内容部分结束-->
	</div>
</body>
</html>
<script type="text/javascript">


</script>
