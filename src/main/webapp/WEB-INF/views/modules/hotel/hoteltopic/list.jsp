<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>酒店主题维护</title>
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
		$(function(){
			//产品名称文本框提示信息
			inputTips();
		});
		
		function show(uuid){
			window.open("${ctx}/sysCompanyDictView/show/" + uuid +"?type=${type}") ;
		}
		
		//排序
		function updateOrder(obj){
			var txt=$(obj).text();
			if(txt=="修改排序"){
				$(obj).text("提交");
				$("input[class='maintain_sort']").each(function(index,element){
					if($(element).val() != '') {
						$(element).removeAttr("disabled");
					}
				});
			}else{
				$(obj).text("修改排序");
				$("input[class='maintain_sort']").attr("disabled","disabled");
				//提交修改排序后的回调函数
				orderSubmitCallBack();
			}
		}
		
		function orderSubmitCallBack(){
			if($(".tc").length>0){
				$.jBox.confirm("确定要修改排序吗？", "系统提示", function(v, h, f) {
					if (v == 'ok') {
						$.jBox.tip("正在修改排序...", 'loading');
						var uuidAndSorts = [];
						$(".sort").each(function(){
							var uuid = $(this).find("input[name=uuid]").val();
							var sort = $(this).find(".maintain_sort").val();
							if(sort != '') {
								uuidAndSorts.push(uuid+","+sort);
							}
						});
						$.post("${ctx}/hotelTopic/updateOrder", {"uuidAndSortsStr":uuidAndSorts.join(";")},
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
		
		//数据状态(新增，删除，默认显示)
		function getVal(obj) {
			if($(obj).attr("class")){
				if($(obj).attr("class")=="selected"){
					$(obj).attr("class",'cancel');
				}else if($(obj).attr("class")=="cancel"){
					$(obj).attr("class",'selected');
				}else{
					$(obj).attr("class",'');
				}
			}else{
				$(obj).attr("class",'checked');
			}
		}
		
		//数据状态Id	
		function getIdArr() {
			var checkedUuids = "";
			var cancelUuids = "";
			$("[class='checked']").each(function(index,element){
				checkedUuids += $(element).val()+",";
			});
			$("[class='cancel']").each(function(index,element){
				cancelUuids += $(element).val()+",";
			});
			
			$("#checkedUuids").val(checkedUuids);
			$("#cancelUuids").val(cancelUuids);
		}

		//修改操作
		function updateHotelTopic() {
			getIdArr();
			$.post("${ctx}/hotelTopic/updateHotelTopic", {"checkedUuids":$("#checkedUuids").val(), "cancelUuids":$("#cancelUuids").val()},
				function(data){
					if(data.result=="1"){
						top.$.jBox.tip('保存成功!','成功');
						setTimeout(function(){
							$("#searchForm").submit();
						}, 1000);
					}else{
						top.$.jBox.tip(data.message,'warning');
					}
				}
			);
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
    <page:param name="showType">hotel_topic</page:param>
</page:applyDecorator>
	<div>
		<!--右侧内容部分开始-->
		<div class="activitylist_bodyer_right_team_co_bgcolor">
              <form id="searchForm" action="${ctx}/hotelTopic/list" method="post">
              	   <input type="hidden" name="type" value="hotel_topic" />
				   <input type="hidden" name="pageNo" id="pageNo" value="${page.pageNo}"/>
				   <input type="hidden" name="pageSize" id="pageSize" value="${page.pageSize}"/>
                   <div class="activitylist_bodyer_right_team_co">
                      <div class="activitylist_bodyer_right_team_co2 pr wpr20">
                          <input class="txtPro inputTxt" id="topicName" name="topicName"
								 value="${topicName}" type="text" placeholder="请输入主题名称" />
                      </div>
                      <div class="form_submit">
                          <input class="btn btn-primary" value="搜索" type="submit" />
                      </div>
                   </div>
              </form>
              <input type="hidden" name="checkedUuids" id="checkedUuids" />
              <input type="hidden" name="cancelUuids" id="cancelUuids" />
              
              <table class="activitylist_bodyer_table mainTable">
                  <thead>
                      <tr>
                      	<th width="10%">排序</th>
                          <th width="">酒店主题</th>
                          <th width="">描述</th>
                          <th width="7%">启用</th>
					<th width="15%">操作</th>
                      </tr>
                  </thead>
                  <tbody>
                  	<c:forEach var="dictView" items="${page.list}" varStatus="v">
	                  	<tr>
	                        <td class="sort">
	                 	  		<input name="uuid" type="hidden" value="<c:out value="${dictView.hotelTopic.uuid}" />"/>
	                        	<input type="text" value="<c:out value="${dictView.hotelTopic.sort}" />" class="maintain_sort" maxlength="4" onblur="this.value=this.value.replace(/\D/g,'');"  onkeyup="this.value=this.value.replace(/\D/g,'');"   onafterpaste="this.value=this.value.replace(/\D/g,'');" disabled="disabled"/>
	                        </td>
	                        <td class="tc">${dictView.label}</td>
	                        <td>${dictView.description}</td>
	                        <td class="tc">
	                            <input type="checkbox" class="<c:if test='${dictView.hotelTopic != null }'>selected</c:if>" name="checkItem" value="${dictView.uuid}" <c:if test="${dictView.hotelTopic != null }">checked="checked"</c:if> onclick="getVal(this)" />
	                        </td>
					  		<td class="tda tc">
	                            <a href="javascript:void();" onclick="show('${dictView.uuid}')">详情</a>
	                        </td>
	                      </tr>
                      </c:forEach>
                  </tbody>
              </table>
          	<div class="page">
            	<div class="pagination">
					<dd>
						<%--<a onclick="updateOrder(this)" class="ydbz_x">修改排序</a>--%>
						<input class="btn btn-primary ydbz_x" onclick="updateOrder(this)" value="修改排序" type="button" />


					</dd>
					<div class="endPage">
                        ${page}
                    </div>
                </div>
             </div>
		<div class="ydBtn ydBtn2">
                   <%--<a onclick="updateHotelTopic()" class="ydbz_s">保&nbsp;&nbsp;&nbsp;存</a>--%>
			<input class="btn btn-primary ydbz_x" onclick="updateHotelTopic()" value="保存" type="button" />

		</div>
        </div>
              <!--右侧内容部分结束-->
        
	</div>
</body>
</html>
<script type="text/javascript">


</script>
