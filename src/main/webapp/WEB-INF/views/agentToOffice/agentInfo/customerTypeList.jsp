<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html><head>
    <title>客户类型维护</title>
    <meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.min.css">
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>  
    
    <script type="text/javascript">
    //505 添加客户类型弹窗 S
	function jbox_add_client_type_pop(ctx) {
    	$pop = $.jBox($("#add_client_pop").html(), {
        	title : "客户类型", 
        	buttons : {'取消': 1,'确认':0}, 
        	width : 350, 
        	height : 260,
        	submit : function (v, h, f) {
            	if (v == "0") {
                	if($pop.find("#clientType").val()==""){
                   	 	$pop.find("#add_client div").show();
                    	$pop.find("#clientType").focus().addClass("inputError");
                    	return false;
                	} else {
                		
                		var name = $pop.find("input[name=name]").val();
                		var remark = $pop.find("textarea[name=remark]").val();
                		var url = ctx + "/customerType/addCustomerType";
                		
                		dataparam = {
                			name : name,
                			remark : remark
                		}
                		
                		$.ajax({
                			type:"POST",
							url:url,
							dataType:"json",
							data:dataparam,
							success:function(data){
								if (data.flag) {
									$("#groupform").submit();
								} else {
									$.jBox.tip(data.msg, 'error');
								}
							}
                		});
                	}
           	 	}
        	}
    	});

    	$pop.find("#clientType").keyup(function(){

        	if($pop.find("#clientType").val()!==""){
            	$pop.find("#add_client div").hide();
            	$pop.find("#clientType").focus().removeClass("inputError");
        	}
    	});
	}

	//点击删除
	function confirmDel(ctx, agentId) {
    	$.jBox.confirm("确定要删除吗？", "提示", function (v) {
        	if (v == 'ok') {
            	
            	dataparam = {
            		agentId : agentId
            	}
            
            	$.ajax({
            		type : "POST",
            		url : ctx + "/customerType/delCustomerType",
            		dataType : "json",
            		data : dataparam,
            		success : function(data) {
            			if (data.flag) {
            				$('#groupform').submit();
            			} else {
            				$.jBox.tip(data.msg, 'error');
            			}
            		}
            	});
        	}
    	});
	}
	//505 添加客户类型弹窗 E
    
    function checkRepeat(obj, ctx) {
    	var name = obj.value;
    	$.ajax({
    		type : "POST",
    		url : ctx + "/customerType/checkRepeat/" + name,
    		dataType : "json",
    		data : {},
    		success : function(data) {
    			if (data.flag) {
    				
    			} else {
    				obj.value = "";
    				$.jBox.tip(data.msg, 'error');
    			}
    		}
    	});
    }
    </script>
</head>
<body>
	<!-- 505客户分类维护 S --> 
<%--     <p class="add-client-topbutt">
		<a onclick="jbox_add_client_type_pop('${ctx}')"><i class="fa fa-plus" aria-hidden="true"></i>添加客户类型</a>
    </p>  --%>
    
    <form name="groupform" id="groupform" action="${ctx }/customerType/getCustomerTypeList">
    	<table id="contentTable" class="table activitylist_bodyer_table mainTable">
			<thead>
            	<tr>
                	<th width="5%">序号</th>
                    <th width="20%">客户类型</th>
                    <th width="54%">描述</th>
                    <th width="8%">创建人</th>
                    <th width="8%">创建时间</th>
                    <%-- <th width="5%">操作</th> --%>  
                </tr>
            </thead>
            <tbody>
                <c:if test="${fn:length(page.list) <= 0 }">
					<tr class="toptr" >
						<td colspan="17" style="text-align: center;">暂无搜索结果</td>
					</tr>
        		</c:if>
        		
        		<c:forEach items="${page.list }" var="customer" varStatus="c">
        			<tr>
        				<td>${c.count }</td><%-- 序号 --%>
        				<td>${customer.name }</td><%-- 类别名称 --%>
        				<td>${customer.remark }</td><%-- 描述 --%>
        				<td>${customer.createBy }</td><%-- 创建人 --%>
        				<td><%-- 创建时间 --%>
        					<fmt:formatDate value="${customer.createDate }" pattern="yyyy-MM-dd"/>
        				</td>
        				<%-- <td class="tc">
							<a href="javascript:void();" onclick="confirmDel('${ctx}', '${customer.id}')">删除</a>        						
        				</td>  --%>
        			</tr>
        		</c:forEach>
             </tbody>
		</table>
	</form>
    
    <div class="pagination clearFix">${page}</div>

	<!-- 添加客户类型 -->
    <div id="add_client_pop" style="display:none">
		<div id="add_client">
			<p>
				<span ><font>*</font>客户类型：</span>
                <input id="clientType" class="inputTxt" name="name" maxlength="20" type="text" onblur="checkRepeat(this, '${ctx}')">
                <div class="red hide">请输入客户类型</div>
            </p>
            <p>
                <span>描述：</span>
                <textarea maxlength="50" name="remark" id="remark"></textarea>
            </p>
         </div>
     </div>
</body>

</html>