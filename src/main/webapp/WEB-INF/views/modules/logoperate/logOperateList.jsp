<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>操作日志查询</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>

	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/jquery.number.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>
	
	<script type="text/javascript">

	var ctx = "${ctx}";
	var countryId = "";
		$(function(){
			//搜索条件筛选
			launch();
			//产品名称文本框提示信息
			inputTips();
			//操作浮框
			operateHandler();
			
			//排序样式
			var _$orderBy = $("#orderBy").val();
		    if(_$orderBy==""){
		        _$orderBy="groupOpenDate DESC";
		    }
		    var orderBy = _$orderBy.split(" ");
		    $(".activitylist_paixu_left li").each(function(){
		       if ($(this).hasClass("li"+orderBy[0])){
		           orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
		           $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
		           $(this).attr("class","activitylist_paixu_moren");
		       }
		    });
			
		    $("#opeloginname" ).comboboxInquiry();
		    $("#opeloginname").next().find(".custom-combobox-input").blur(function(){
				if(countryId != $("#opeloginname").val()) {
					countryId = $("#opeloginname").val();
					getArea();
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
			
			var resetSearchParams = function(){
			    $(':input','#searchForm')
					.not(':button, :submit, :reset, :hidden')
					.val('')
					.removeAttr('checked')
					.removeAttr('selected');
			    $('#targetAreaId').val('');
			    $('#orderShowType').val('${showType}');
			};
			
			//$(".selectinput" ).comboboxInquiry(); 
		    //排序
		
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/modules/logoperate/logQueryList");
			$("#searchForm").submit();
	    	return false;
	    }
	    function queryOpeById(mid){
	    
	    	$("#querydetail").attr("action","${ctx}/modules/logoperate/queryDelLogDetail");
	    	$("#querydetail").submit();
	    }
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
		
		var activityIds = "";
		
function checkall(obj){
	if(obj.checked){ 
		$("input[name='logOpeId']").not("input:checked").each(function(){this.checked=true;}); 
		$("input[name='allChk']").not("input:checked").each(function(){this.checked=true;});
		$("input[name='logOpeId']:checked").each(function(i,a){
			if(logOpeIds.indexOf(a.value) < 0){					
				logOpeIds = logOpeIds + a.value+",";
			}
			
		});
		//alert("if activityIds: " + activityIds);
	}else{ 
		$("input[name='logOpeId']:checked").each(function(){this.checked=false;}); 
		$("input[name='allChk']:checked").each(function(){this.checked=false;});
		$("input[name='logOpeId']:checked").each(function(i,a){
			if(logOpeIds.indexOf(a.value) < 0){					
				logOpeIds = logOpeIds + a.value+",";
			}
		});	
		//alert("else activityIds: " + activityIds);	
	} 
}
//每行中的复选框
function idcheckchg(obj){
	if(obj.checked){
		if($("input[name='logOpeId']").not("input:checked").length == 0){
			$("input[name='allChk']").not("input:checked").each(function(){this.checked=true;});
		}
		if(logOpeIds.indexOf($(obj).val()) < 0){
			logOpeIds = logOpeIds+$(obj).val()+",";
		}
	}else{
		$("input[name='allChk']:checked").each(function(){
			this.checked=false;	
			if(logOpeIds.indexOf($(obj).val()) >= 0){
				logOpeIds = logOpeIds.replace($(obj).val()+",","");
			}	
		});
	$("#logOpeIds").val(logOpeIds);
	}
	alert($("#logOpeIds").val(logOpeIds));
}
		
	</script>
</head>
<body>
<form:form id="searchForm" modelAttribute="logOperate" action="${ctx}/modules/logoperate/logQueryList" method="post" class="form-search">
<div class="activitylist_bodyer_right_team_co_bgcolor">
	
	
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<input id="logOpeIds" type="hidden" name="logOpeIds" value="${logOpeIds }"/>
    	<div class="activitylist_bodyer_right_team_co">
    	 	<div class="activitylist_bodyer_right_team_co2 wpr20 pr">
			   <div class="activitylist_team_co3_text">模块：</div>
			    <select id="modular_id" name="modular_id" >      
	                <option value="">全部</option>
	                <c:forEach items="${morMap}" var="mro">
	                	<c:choose>
							<c:when test="${modular_id==mro.key}"><option value="${mro.key }"  selected="selected">${mro.value }</option></c:when>
							<c:otherwise><option value="${mro.key }">${mro.value }</option></c:otherwise>	                	
	                	</c:choose>
	                	
	                </c:forEach>
            	</select>
            </div>
       		<div class="form_submit">
				<input  class="btn btn-primary ydbz_x" type="submit" value="搜素"/>
				<input class="btn ydbz_x" type="button" value="清空所有条件" onclick="resetSearchParams()"/>
			</div>
			<div class="zksx">筛选</div>
			
			<div class="ydxbd" style="display: none;">
			<div class="activitylist_bodyer_right_team_co1">
	            <div class="activitylist_team_co3_text">操作项：</div>
				<select id="opetype" name="opetype">      
	              <option value="">全部</option>
	                <c:forEach items="${typeMap}" var="type">
	                	<c:choose>
	                		<c:when test="${opetype==type.key}"><option value="${type.key }" selected="selected">${type.value }</option></c:when>
	                		<c:otherwise><option value="${type.key }">${type.value }</option></c:otherwise>
	                	</c:choose>
	                </c:forEach>
            	</select>
	        </div>
			<div class="activitylist_bodyer_right_team_co1">
			
			    <div class="activitylist_team_co3_text">操作人：</div>
			    <select class="selectinput" name="opeloginname" id="opeloginname" style="width:200px">
			      <option value="">请选择</option>
	                <c:forEach items="${userlist}" var="ope">
	                	<c:choose>
	                		<c:when test="${opeloginname ==ope.loginName}"><option value="${ope.loginName }" selected="selected">${ope.name}  ${ope.loginName}</option></c:when>
	                		<c:otherwise><option value="${ope.loginName }">${ope.name}&nbsp;${ope.loginName}</option></c:otherwise>
	                	</c:choose>
	                	
	                </c:forEach>
	             </select>   
			     
            </div>
			
			<div class="activitylist_bodyer_right_team_co2">
				<label class="activitylist_team_co3_text">操作时间：</label>
				<input id="beginDate" class="inputTxt dateinput" name="beginDate" onClick="WdatePicker()" value="${beginDate}" readonly/> 
				<span style="font-size:12px; font-family:'宋体';"> 至</span>  
				<input id="endDate" class="inputTxt dateinput" name="endDate"  onClick="WdatePicker()" value="${endDate}" readonly/>
        	</div>
        	<div class="activitylist_bodyer_right_team_co3">
        	<label class="activitylist_team_co3_text">批发商：</label>
					<select id="opecomid" name="opecomid">      
		                <option value="">全部</option>
		                <c:forEach items="${officelist}" var="office">
		                	<c:choose>
		                		<c:when test="${opecomid==office.id}"><option value="${office.id}" selected="selected">${office.name}</option></c:when>
		                		<c:otherwise><option value="${office.id}">${office.name}</option></c:otherwise>
		                	</c:choose>
		                	
		                </c:forEach>
            		</select>
	        </div>
         	<!-- <div class="kong"></div> -->
			</div> 
       </div>
       
       <div class="activitylist_bodyer_right_team_co_paixu">
			<div class="activitylist_paixu">
				<div class="activitylist_paixu_left">
					<ul>
						<li style="width: 50px; border: none; background: none; height: 28px; line-height: 28px;">排序 </li> 
						
						
						<c:choose>
				                    <c:when test="${conditionsMap.orderModelarIdCss == 'activitylist_paixu_moren' }">
				                    <li class="activitylist_paixu_moren">
				                    </c:when>
				                    <c:otherwise>
				                    <li class="activitylist_paixu_left_biankuang">
				                    </c:otherwise>
				                    </c:choose>
				                    	<input type="hidden" value="" name="orderModelarIdSort">
				                    	<a onclick="sort(this)">模块
				                    		<c:choose>
				                    			<c:when test="${conditionsMap.orderModelarIdSort == 'desc' }">
				                    			<i class="icon icon-arrow-down"></i>
				                    			</c:when>
				                    			<c:otherwise>
				                    			<i class="icon icon-arrow-up"></i>
				                    			</c:otherwise>
				                    		</c:choose> 
				                    	</a>
				                    	<input type="hidden" value="" name="orderModelarIdCss">
				                    </li>       
				        
				         <c:choose>
				                    <c:when test="${conditionsMap.orderOpeLoginameCss == 'activitylist_paixu_moren' }">
				                    <li class="activitylist_paixu_moren">
				                    </c:when>
				                    <c:otherwise>
				                    <li class="activitylist_paixu_left_biankuang">
				                    </c:otherwise>
				                    </c:choose>
				                    	<input type="hidden" value="" name="orderOpeLoginameSort">
				                    	<a onclick="sort(this)">操作人
				                    		<c:choose>
				                    			<c:when test="${conditionsMap.orderOpeLoginameSort == 'desc' }">
				                    			<i class="icon icon-arrow-down"></i>
				                    			</c:when>
				                    			<c:otherwise>
				                    			<i class="icon icon-arrow-up"></i>
				                    			</c:otherwise>
				                    		</c:choose> 
				                    	</a>
				                    	<input type="hidden" value="" name="orderOpeLoginameCss">
				                    </li>       
						     
						     
						 <c:choose>
				                    <c:when test="${conditionsMap.orderCreateDateCss == 'activitylist_paixu_moren' }">
				                    <li class="activitylist_paixu_moren">
				                    </c:when>
				                    <c:otherwise>
				                    <li class="activitylist_paixu_left_biankuang">
				                    </c:otherwise>
				                    </c:choose>
				                    	<input type="hidden" value="" name="orderCreateDateSort">
				                    	<a onclick="sort(this)">操作时间 
				                    		<c:choose>
				                    			<c:when test="${conditionsMap.orderCreateDateSort == 'desc' }">
				                    			<i class="icon icon-arrow-down"></i>
				                    			</c:when>
				                    			<c:otherwise>
				                    			<i class="icon icon-arrow-up"></i>
				                    			</c:otherwise>
				                    		</c:choose> 
				                    	</a>
				                    	<input type="hidden" value="" name="orderCreateDateCss">
				                    </li>    
						<c:choose>
				                    <c:when test="${conditionsMap.orderOpeComIdCss == 'activitylist_paixu_moren' }">
				                    <li class="activitylist_paixu_moren">
				                    </c:when>
				                    <c:otherwise>
				                    <li class="activitylist_paixu_left_biankuang">
				                    </c:otherwise>
				                    </c:choose>
				                    	<input type="hidden" value="" name="orderOpeComIdSort">
				                    	<a onclick="sort(this)">批发商
				                    		<c:choose>
				                    			<c:when test="${conditionsMap.orderOpeComIdSort == 'desc' }">
				                    			<i class="icon icon-arrow-down"></i>
				                    			</c:when>
				                    			<c:otherwise>
				                    			<i class="icon icon-arrow-up"></i>
				                    			</c:otherwise>
				                    		</c:choose> 
				                    	</a>
				                    	<input type="hidden" value="" name="orderOpeComIdCss">
				                    </li>         
					</ul>
				</div>
				<div class="activitylist_paixu_right">
					查询结果&nbsp;&nbsp;<strong id="eprice-list-count-id">${page.count}</strong>&nbsp;条
				</div>
				<div class="kong"></div>
			</div>
		</div>
	
	</div>
	
	<table id="contentTable" class="table activitylist_bodyer_table" >
		<thead >
			<tr>
				<th width="10%">全选<input name="allChk" type="checkbox" onclick="checkall(this)"/></th>
				<th width="10%">模块</th>
				<th width="10%">操作人</th>
				<th width="8%">操作时间</th>
				<th width="6%">操作项</th>
				<th width="36%">操作内容</th>
				<th width="16%">批发商</th>
				<th width="14%">操作</th>
			</tr>
		</thead>
		<tbody>
	                        <c:if test="${fn:length(page.list) <= 0 }">
				                 <tr class="toptr" >
				                 	<td colspan="15" style="text-align: center;">没有符合条件的记录</td>
				                 </tr>
			        		</c:if>
			        		<c:if test="${fn:length(page.list) ne 0}">
	                        	<c:forEach items="${page.list}" var="logope" varStatus="s">
		                            <tr>
		                                <!-- <td>${s.count}</td> -->
		                                <td><input type="checkbox" name="logOpeId" value="${logope.id}" <c:if test="${fn:contains(logOpeIds,fn:trim(logope.id))}">checked="checked"</c:if> onclick="idcheckchg(this)"/></td>
		                                <td>${logope.modular_name}</td>
		                                <td>${logope.ope_loginname}</td>
										<td class="tr">${logope.create_date}</td>
										<td class="tr">${logope.ope_type}</td>
										<td class="tc">${logope.content}</td>
		                                <td class="tc">${logope.modular_name}</td>
		                                <td class="tc">
		                                <a id = "querydetail"  href="${ctx}/modules/logoperate/queryLogOperateById?id=${logope.id}&flag=${logope.ope_type}">详  情</a>
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
	</form:form>
	<div class="pagination clearFix">${page}</div>
	<!-- <br/>
	<a href="${ctx}/modules/logoperate/queryAddLogDetail">添加操作明细页面</a><br/>
	<a href="${ctx}/modules/logoperate/queryUpLogDetail">修改操作明细页面</a><br/>
	<a href="${ctx}/modules/logoperate/queryDelLogDetail">删除操作明细页面</a><br/>
	 -->
	
</body>
</html>