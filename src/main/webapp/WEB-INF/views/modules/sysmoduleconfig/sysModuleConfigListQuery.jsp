<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>后台维护-后台查询</title>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>

<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script src="${ctxStatic}/common/jquery.number.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>

<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript">
var fmoduleId = "";
$(function(){
	launch();
	
	//排序样式
	var _$orderBy = $("#orderBy").val();
    if(_$orderBy==""){
        _$orderBy="groupOpenDate DESC";
    }
    var orderBy = _$orderBy.split(" ");
    $(".filter_sort li").each(function(){
       if ($(this).hasClass("li"+orderBy[0])){
           orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
           $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
           $(this).attr("class","activitylist_paixu_moren");
       }
    });
    
    //如果展开部分有查询条件的话，默认展开，否则收起	
	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][type!='checkbox'][type!='text']");
	var searchFormselect = $("#searchForm").find("select");
	var inputRequest = false;
	var selectRequest = false;
	/*for(var i = 0; i<searchFormInput.length; i++) {
		if($(searchFormInput[i]).val() != " " && $(searchFormInput[i]).val() != null
				&& $(searchFormInput[i]).val() != "") {
			inputRequest = true;
		}
	}*/
	for(var i = 0; i<searchFormselect.length; i++) {
		if($(searchFormselect[i]).children("option:selected").val() != "" && $(searchFormselect[i]).children("option:selected").val() != " " &&
				$(searchFormselect[i]).children("option:selected").val() != null) {
			selectRequest = true;
		}
	}
	if(inputRequest||selectRequest) {
		$('.zksx').click();
	}
	
});

		function sort(element){

			var _this = $(element);

			//按钮高亮
			_this.parent("li").attr("class","activitylist_paixu_moren");

			//原先高亮的同级元素置灰
			_this.parent("li").siblings(".activitylist_paixu_moren").attr("class","activitylist_paixu_left_biankuang");

			//高亮按钮隐藏input赋值
			_this.next().val("activitylist_paixu_moren");

			//原先高亮按钮隐藏input值清空
			_this.parent("li").siblings(".activitylist_paixu_moren").children("a").next().val("");

			var sortFlag = _this.children().attr("class");
			//降序
			if(sortFlag == "icon icon-arrow-up"){

				//改变箭头的方向
				_this.children().attr("class","icon icon-arrow-down");

				//降序
				_this.prev().val("desc");
			}
			//降序
			else if(sortFlag == "icon icon-arrow-down"){
				//改变箭头方向
				_this.children().attr("class","icon icon-arrow-up");

				//shengx
				_this.prev().val("asc");
			}

			$("#searchForm").submit();

			return false;
		}

function getModules(obj) {
	var fmoduleId=$(obj).find("option:selected").val();
	if(fmoduleId != '') {
		$.ajax({
			type:"POST",
			url:"${ctx}/modules/sysmoduleconfig/getSonModulesFromJson?fmoduleId="+fmoduleId,
			async : false,
			success:function(data){
				$("#moduleId").empty().append('<option value="">请选择</option>');
				if(data != null && data.length != 0) {
					for(var index = 0; index < data.length; index++) {
						$("#moduleId").append('<option value=\'' + data[index][0] + '\'>' + data[index][1] + '</option>');
					}
				}
			}
		});
	}else if(fmoduleId == ''){
		$("#moduleId").empty().append('<option value=" ">请选择</option>');
	}
}

var resetSearchParams = function(){
    $(':input','#searchForm')
		.not(':button, :submit, :reset, :hidden')
		.val('')
		.removeAttr('checked')
		.removeAttr('selected');
    $('#targetAreaId').val('');
    $('#orderShowType').val('${showType}');
};

//删除产品的提示信息
function confirmxDel(id,_this){
	 var $this = $(_this);
		$.jBox.confirm("确定删除吗？", "系统提示", function(v, h, f){
			if (v == 'ok') {
				$.ajax({
			        type: "POST",
			        url: "${ctx}/modules/sysmoduleconfig/delSysModuleConfig",
			        async:false,
			        cache:false,
			        data: {
			        	id:id
			        	//payType:"1"
			        },
			        success: function(data){
			        	 var delsuccess = data.delsuccess;
			        	  if(null == delsuccess){
			        		  return false;
			        	  }
			        	  top.$.jBox.tip(data.delsuccess,'success');  
			        	  $this.parents('#searchForm').submit();
			        }
		     	}); 
			}else if (v == 'cancel'){
				
			}
	});
}
</script>
</head>
<body>
<form:form id="searchForm" modelAttribute="sysMOduleConfig" action="${ctx}/modules/sysmoduleconfig/queryModuleConfigList" method="post" class="form-search">
	<div class="activitylist_bodyer_right_team_co_bgcolor">
	
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
        <p class="main-right-topbutt"><a  href='${ctx}/modules/sysmoduleconfig/toSaveSysModuleConfig' class="btn btn-primary">添加</a></p>
        
        <div class="activitylist_bodyer_right_team_co">
          <div class="activitylist_bodyer_right_team_co3">
              <div class="activitylist_team_co3_text">模块：</div>
                  <select id="fmoduleId" name="fmoduleId" onchange="getModules(this)">
					<option value="">请选择</option>
					  <c:forEach items="${fmodulelist}" var="module">
	                	<c:choose>
							<c:when test="${fmoduleId==module[0]}"><option value="${module[0]}"  selected="selected">${module[1]}</option></c:when>
							<c:otherwise><option value="${module[0]}">${module[1]}</option></c:otherwise>	                	
	                	</c:choose>
	                  </c:forEach>
				 </select> 
		   </div>
				<div class="activitylist_bodyer_right_team_co3">
				  <div class="activitylist_team_co3_text">子模块：</div>
                     <select id="moduleId" name="moduleId">
						<option value="">请选择</option>
					      <c:forEach items="${modulelist}" var="smodule">
	                	    <c:choose>
							  <c:when test="${moduleId==smodule[0]}"><option value="${smodule[0]}"  selected="selected">${smodule[1]}</option></c:when>
							  <c:otherwise><option value="${smodule[0]}">${smodule[1]}</option></c:otherwise>	                	
	                	    </c:choose>
	                      </c:forEach>
					  </select> 
                   </div>
                <div class="form_submit">
								 <input type="submit" id="btn_search" value="搜索"  class="btn btn-primary ydbz_x">
								 <input type="button" value="清空所有条件" onclick="resetSearchParams()" class="btn ydbz_x">
				</div>
				<a class="zksx">筛选</a>
                <div class="ydxbd">
						<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">批发商 :</div>
							<select id="companyId" name="companyId">
								<option value="">请选择</option>
						      		<c:forEach items="${officelist}" var="office">
			                	    	<c:choose>
									  		<c:when test="${companyId==office.id}"><option value="${office.id}"  selected="selected">${office.name}</option></c:when>
									  		<c:otherwise><option value="${office.id}">${office.name}</option></c:otherwise>	                	
			                	    	</c:choose>
		                      		</c:forEach>
							</select> 
						</div>
						<div class="activitylist_bodyer_right_team_co1">
							<label>页面名称：</label><input type="text" value="" name="" id="" class="txtPro inputTxt">
						</div>
						<div class="activitylist_bodyer_right_team_co1">
							<label>页面路径：</label><input type="text" value="" name="" id="" class="txtPro inputTxt">
						</div>        
                    </div>
                    <div class="kong"></div>
            </div>
                   
			<div class="activitylist_bodyer_right_team_co_paixu">
			<div class="activitylist_paixu">
				<div class="activitylist_paixu_left">
					<ul>
						<li style="width: 50px; border: none; background: none; height: 28px; line-height: 28px;">排序 </li> 
						
						
						<c:choose>
				                    <c:when test="${conditionsMap.sysModuleIdCss == 'activitylist_paixu_moren' }">
				                    <li class="activitylist_paixu_moren">
				                    </c:when>
				                    <c:otherwise>
				                    <li class="activitylist_paixu_left_biankuang">
				                    </c:otherwise>
				                    </c:choose>
				                    	<input type="hidden" value="" name="sysModuleIdSort">
				                    	<a onclick="sort(this)">模块
				                    		<c:choose>
				                    			<c:when test="${conditionsMap.sysModuleIdSort == 'desc' }">
				                    			<i class="icon icon-arrow-down"></i>
				                    			</c:when>
				                    			<c:otherwise>
				                    			<i class="icon icon-arrow-up"></i>
				                    			</c:otherwise>
				                    		</c:choose> 
				                    	</a>
				                    	<input type="hidden" value="" name="sysModuleIdCss">
				                    </li>       
				        
				         <c:choose>
				                    <c:when test="${conditionsMap.sysModuleSonIdCss == 'activitylist_paixu_moren' }">
				                    <li class="activitylist_paixu_moren">
				                    </c:when>
				                    <c:otherwise>
				                    <li class="activitylist_paixu_left_biankuang">
				                    </c:otherwise>
				                    </c:choose>
				                    	<input type="hidden" value="" name="sysModuleSonIdSort">
				                    	<a onclick="sort(this)">子模块
				                    		<c:choose>
				                    			<c:when test="${conditionsMap.sysModuleSonIdSort == 'desc' }">
				                    			<i class="icon icon-arrow-down"></i>
				                    			</c:when>
				                    			<c:otherwise>
				                    			<i class="icon icon-arrow-up"></i>
				                    			</c:otherwise>
				                    		</c:choose> 
				                    	</a>
				                    	<input type="hidden" value="" name="sysModuleSonIdCss">
				                    </li>       
						     
						     
						 <c:choose>
				                    <c:when test="${conditionsMap.sysOfficeCss == 'activitylist_paixu_moren' }">
				                    <li class="activitylist_paixu_moren">
				                    </c:when>
				                    <c:otherwise>
				                    <li class="activitylist_paixu_left_biankuang">
				                    </c:otherwise>
				                    </c:choose>
				                    	<input type="hidden" value="" name="sysOfficeSort">
				                    	<a onclick="sort(this)">批发商
				                    		<c:choose>
				                    			<c:when test="${conditionsMap.sysOfficeSort == 'desc' }">
				                    			<i class="icon icon-arrow-down"></i>
				                    			</c:when>
				                    			<c:otherwise>
				                    			<i class="icon icon-arrow-up"></i>
				                    			</c:otherwise>
				                    		</c:choose> 
				                    	</a>
				                    	<input type="hidden" value="" name="sysOfficeCss">
				                    </li>    
						
					</ul>
				</div>
				<div class="activitylist_paixu_right">
					查询结果&nbsp;&nbsp;<strong id="eprice-list-count-id">${page.count}</strong>&nbsp;条
				</div>
				<div class="kong"></div>
			</div>
		</div>		
					
					
					<!--查询结果筛选条件排序结束-->
                        <table class="table activitylist_bodyer_table" id="contentTable">
                          <thead>
                              <tr>
                                  <th style="width:10%">批发商</th>
                                  <th style="width:10%">模块</th>
                                  <th style="width:10%">子模块</th>
                                  <th style="width:10%">页面名称</th>
                                  <th style="width:10%">页面路径</th>
                                  <th style="width:10%">预览路径</th>
                                  <th style="width:10%">操作</th>
                              </tr>
                          </thead>
                          <tbody>
                          	<c:if test="${fn:length(page.list) <= 0 }">
				                 <tr class="toptr" >
				                 	<td colspan="15" style="text-align: center;">没有符合条件的记录</td>
				                 </tr>
			        		</c:if>
			        		<c:if test="${fn:length(page.list) ne 0}">
	                        	<c:forEach items="${page.list}" var="sysModule" varStatus="s">
		                            <tr>
		                                <!-- <td>${s.count}</td> -->
		                                <td>${sysModule.companyname}</td>
		                                <td>${sysModule.fmodulename}</td>
										<td class="tr">${sysModule.modulename}</td>
										<td class="tr">${sysModule.pagename}</td>
										<td class="tc">${sysModule.path}</td>
		                                <td class="tc">${sysModule.prepath}</td>
		                                <td class="tc">
		                                
		                            <a href="${ctx}/modules/sysmoduleconfig/toUpdateSysModuleConfig?id=${sysModule.id}">修改</a>&nbsp;&nbsp;
                                   	<a href="javascript:void(0)" onclick="javascript:confirmxDel('${sysModule.id}',this)">删除</a>&nbsp;&nbsp;
                                   	<a id = "querydetail"  href="${ctx}/modules/sysmoduleconfig/querySysModuleConfigById?id=${sysModule.id}">详  情</a>&nbsp;&nbsp;
                                   	<!-- <a target="_blank" href="后台维护-后台详情.html">详情</a>  -->
                                   	<a href="${trekizpath}/acs/dispatch.action?id=${sysModule.id}">配置页面</a>
		                    				<!-- 
		                    				<dl class="handle">
						                    	<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt> 
						                    	<a href="javascript:void(0)" onClick="suppliermod()">修改</a>
												<a href="${ctx}/supplier/deleteSupplierInfo?id=" onClick="return confirm('您确认要删除该地接社吗？', this.href)">删除</a>
					                   		</dl>
					                   		-->
					                   </td>
		                            </tr>
	                            </c:forEach>
                            </c:if>	
                         </tbody>
                      </table>
                    
          
	    </div>
	</form:form>
	<div class="pagination clearFix">${page}</div>
</body>
</html>
