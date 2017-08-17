<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <title>基础信息维护-地域城市-目的地维护</title>
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
$(function(){
	 g_context_url ="${ctx}";
	//搜索目的地
	$("select[name='target']" ).comboboxInquiry({
		"afterInvalid":function(event,data){
			//被选择项的value值
			var selectValue = $(this).val();
			var Array_default = new Array("搜索计调名");
			if(-1 == $.inArray(data,Array_default)){
				var isIncluded = 0;
				$("#targetShow a").each(function(index, element) {
					if(data == $(element).text()){
						isIncluded = 1;
						return;
					}
				});
				if(isIncluded){
					jBox.tip("您已选择");
				}else{
					$("#targetShow").append('<a value="{1}">{0}</a>'.replace("{0}",data).replace("{1}",selectValue));
					
					$.ajax({                 
						cache: true,                 
						type: "POST",                 
						url:g_context_url+ "/sysGeoCity/addDest",    						
						data:{id:selectValue},                
						async: false,                 
						error: function(request) {                     
							top.$.jBox.tip('操作失败');
						},                 
						success: function(data) {    
							if(data!=null&&data!=""){
								top.$.jBox.tip(data);
								return false;
							}                
							top.$.jBox.tip("保存成功！");
							window.location.href=g_context_url+"/sysGeoCity/destinationList";
						}             
					});
					
				}
			}
		}
	});
	//删除选择的城市
	//inquiryCheckBOX();
	inquiryCheckBOXLocal()

});

function  inquiryCheckBOXLocal(){
	$(".seach_checkbox").on("click","em",function(){
		//alert($(this).parent().attr("value"));
		$(this).parent().remove();
		var id = $(this).parent().attr("id");
		$.ajax({                 
			cache: true,                 
			type: "POST",                 
			url:g_context_url+ "/sysGeoCity/delDest",    						
			data:{id:id},                
			async: false,                 
			error: function(request) {                     
				top.$.jBox.tip('操作失败');
			},                 
			success: function(data) {    
				if(data!=null&&data!=""){
					top.$.jBox.tip(data);
					return false;
				} 
				top.$.jBox.tip("保存成功！");
				window.location.href=g_context_url+"/sysGeoCity/destinationList";
			}             
		});
		
		
	})
	$(".seach_checkbox").on("hover","a",function(){
		$(this).append("<em></em>")
	})
	$(".seach_checkbox").on("mouseleave","a",function(){
		$(this).parent().find('em').remove();
	})
	}
</script>
</head>
<body>
	 <page:applyDecorator name="sys_destinationList" ></page:applyDecorator> 
				<!--右侧内容部分开始-->
                <form method="post" action="#"  id="destinationForm" >
                <!--目的地开始-->
                <div class="seach25 seach100">
                	<p>目的地：</p>
                  	<p class="seach_r">
                        <span class="seach_check">
                            <select name="target">
                              <option selected="selected" value="0">选择出发城市</option>
                               <c:forEach  var="entry"  items="${selectList}">
                                 <option value="${entry.id}">${entry.name}</option>
                              </c:forEach>
                            </select>
                        </span>
                        <span id="targetShow" class="seach_checkbox">
                           <c:forEach var="entry" items="${list}">
                            <a value="${entry.id}" id="${entry.udefid}" >${entry.name}</a>
                            </c:forEach>
                        </span>
                   </p>
                </div>
                <!--目的地结束-->
                </form>
				<!--右侧内容部分结束-->
</body>
</html>
