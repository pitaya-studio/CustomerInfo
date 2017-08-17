<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>旅游区域</title>
    <meta name="decorator" content="wholesaler" />
	<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet"/>
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css" rel="stylesheet" type="text/css"/>
    <script src="${ctxStatic}/js/jquery-1.9.1.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
    <link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet"/>
    <script src="${ctxStatic}/common/mustache.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/trekiz.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/vendor.service_mode1.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>

    <script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/placeholder.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
    <script type="text/javascript">
		function addType() {
			var iframe = "iframe:${ctx}/sys/district/form";
			$.jBox(iframe,{
				title: "新增旅游区域",
       		 	width: 350,
       			height: 365,
       			persistent: true,
       			buttons: {}
       		});
            $("#jbox-content").css("overflow","hidden");
			$("#jbox-content").css("overflow-y","hidden");
        }

		function deleteThis(id,label){
			$.jBox.confirm("是否删除该旅游区域？", "系统提示", function(v, h, f) {
				if(v=='ok'){
//					var dis = checkDis(id);
//					if(dis){
//						top.$.jBox.tip("此区域已被使用，无法删除！");
//						return ;
//					}
					$.ajax({
						type:"post",
						url:"${ctx}/sys/district/deleteDistrict",
						data:{
							id:id
						},
						success:function(result){
							if(result){
								top.$.jBox.tip("删除成功");
								location.href="${ctx}/sys/district";
							}else{
								top.$.jBox.tip("删除成功");
							}
						}
					});
				}
			});
			
		}

		function checkDis(id){
			var dis = false;
			$.ajax({
				type:"post",
				url:"${ctx}/sys/district/checkDistrict",
				async:false,
				data:{
					id:id
				},
				success:function(result){
					if(result.isUsed){
						dis = true;
					}else{
						dis = false;
					}
				}
			});
			return dis;
		}
		
		// 线路修改
		function modifyThis(id, destinationIds) {
			var iframe = "iframe:${ctx}/sys/district/form?id="+id+"&destinationIds="+destinationIds;
            $.jBox(iframe,{
       			title: "修改旅游区域",
       		 	width: 340,
       			height: 365,
       			persistent: true,
       			buttons: {}
       		});
            $("#jbox-content").css("overflow","hidden");
			$("#jbox-content").css("overflow-y","hidden");
		}
		
		// 搜索
		function query() {
			$("#searchForm").submit();
		}
		
		// 重置条件
		function resetForm(){
			var inputArray = $('#searchForm').find("input[type!='hidden'][type!='submit'][type!='button']");
			var selectArray = $('#searchForm').find("select");
			for(var i=0;i<inputArray.length;i++){
				if($(inputArray[i]).val()){
					$(inputArray[i]).val('');
				}
			}
			for(var i=0;i<selectArray.length;i++){
				var selectOption = $(selectArray[i]).children("option");
				$(selectOption[0]).attr("selected","selected");
			}
			tipToggle();
		}
		
		function page(n,s){
    		$("#pageNo").val(n);
    		$("#pageSize").val(s);
   			$("#searchForm").submit();
    		return false;
		}

		$(function() {
			//展开筛选按钮
			//bug17514 去掉该jsp原有展开筛选方法，替换为 common.js中的launch方法 by tlw
			launch();
//    		$('.zksx').click(function(){
//				if($('.ydxbd').is(":hidden")==true)
//				{
//					$('.ydxbd').show();
//					$(this).text('收起筛选');
//					$(this).addClass('zksx-on');
//				}else
//				{
//					$('.ydxbd').hide();
//					$(this).text('展开筛选');
//					$(this).removeClass('zksx-on');
//				}
//			});

			//如果展开部分有查询条件的话，默认展开，否则收起
			var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
			var searchFormselect = $("#searchForm").find("select");
			var inputRequest = false;
			var selectRequest = false;
			for(var i = 0; i<searchFormInput.length; i++) {
				if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
					inputRequest = true;
				}
			}
			for(var i = 0; i<searchFormselect.length; i++) {
				if($(searchFormselect[i]).children("option:selected").val() != "" &&
						$(searchFormselect[i]).children("option:selected").val() != null) {
					selectRequest = true;
				}
			}
			if(selectRequest) {
				$('.zksx').click();
			}

			tipToggle();
		});
		
		function tipToggle() {
			var val = $("input[name*=name]").val();
			if (val == "") {
				$(".ipt-tips").css("display", "block");
			} else {
				$(".ipt-tips").css("display", "none");
			}
		}
    </script>
    <style type="text/css">
		.flight .t-type-add {
			float: right;
		}
		.relative{position:relative;}
    </style>

</head>

<body>
	<div class="ydbzbox fs">
  		<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
  			<form id="searchForm" action="${ctx }/sys/district" method="post">
  				<%--<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>--%>
        		<%--<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>--%>
        		<%-- <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/> --%>
  				<div class="activitylist_bodyer_right_team_co " >
  					<div class="activitylist_bodyer_right_team_co2 relative">
	            		<input id="name" name="name" class="inputTxt inputTxtlong searchInput" placeholder="旅游区域" value="${name }"
	            			onblur="tipToggle()" onfocus="$('.ipt-tips').css('display', 'none');"/>
            		</div>
					<a class="zksx" >筛选</a>
            		<div class="form_submit">
                 		<input class="btn btn-primary ydbz_x" type="button" onclick="query()" value="搜索"/>
                 		<input class="btn ydbz_x" type="button" onclick="resetForm();" value="清空所有条件" />
            		</div>
					<p class="main-right-topbutt"><a class="primary" onclick="addType()">添加</a></p>
					<div class="ydxbd">
						<span></span>
						<div class="activitylist_bodyer_right_team_co1" >
							<label class="activitylist_team_co3_text">归属分类：</label>
							<div class="selectStyle">
							<select name="inout">
								<option value="" <c:if test="${empty inout}">selected</c:if>>全部</option>
								<option value="100000" <c:if test="${inout eq 100000}">selected</c:if>>出境游</option>
								<option value="200000" <c:if test="${inout eq 200000}">selected</c:if>>国内游</option>
							</select>
							</div>
						</div>
					</div>
  				</div>
  			</form>
  		</div>
		<div>
			<table id="contentTable" class="t-type mainTable">
				<thead class="destination_title">
					<tr>
                       <th width="5%">序号</th>
                       <th width="15%">旅游区域</th>
                       <th width="25%">归属分类</th>
                       <th width="10%">操作</th>
                   </tr>
				</thead>
				<tbody>
					<c:if test="${fn:length(list) <= 0 }">
						<tr>
							<td colspan="4" style="text-align: center;">暂无搜索结果</td>
						</tr>
					</c:if>
					
					<c:forEach items="${list }" var="district" varStatus="status">
						<tr>
							<td>${status.count }</td>
		                    <td>${district.name }</td>
		                    <td>${district.tourInOutName }</td>
		                    <td class="tc">
								<a href="javascript:void(0);"
									onclick="modifyThis('${district.id}', '${district.destinationIds }')">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;
								<a href="javascript:void(0);"
									onclick="deleteThis('${district.id}','${district.name }')">删除</a>
							</td>
	                   </tr>
                   </c:forEach>
				</tbody>
			</table>
		</div>
		<%--<div class="pagination clearFix">${page}</div>--%>
	</div>
</body>
</html>
