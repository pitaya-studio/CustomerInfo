<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>基础信息维护-地域城市-城市维护</title>
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
	//搜索出发城市
	$("input[name='menuIds']" ).comboboxInquiry({
		"afterInvalid":function(event,data){
			//被选择项的value值
			var selectValue = $(this).val();
			var Array_default = new Array("搜索计调名");
			if(-1 == $.inArray(data,Array_default)){
				var isIncluded = 0;
				$("#startCityShow a").each(function(index, element) {
					if(data == $(element).text()){
						isIncluded = 1;
						return;
					}
				});
				if(isIncluded){
					jBox.tip("您已选择");
				}else{
					$("#startCityShow").append('<a value="{1}">{0}</a>'.replace("{0}",data).replace("{1}",selectValue));
				}
			}
		}
	});
	//搜索到达城市
	$("select[name='transCityShow']" ).comboboxInquiry({
		"afterInvalid":function(event,data){
			//被选择项的value值
			var selectValue = $(this).val();
			var Array_default = new Array("搜索计调名");
			if(-1 == $.inArray(data,Array_default)){
				var isIncluded = 0;
				$("#transCityShow a").each(function(index, element) {
					if(data == $(element).text()){
						isIncluded = 1;
						return;
					}
				});
				if(isIncluded){
					jBox.tip("您已选择");
				}else{
					$("#transCityShow").append('<a value="{1}">{0}</a>'.replace("{0}",data).replace("{1}",selectValue));
				}
			}
		}
	});
	//搜索离境城市
	$("select[name='leaveCity']" ).comboboxInquiry({
		"afterInvalid":function(event,data){
			//被选择项的value值
			var selectValue = $(this).val();
			var Array_default = new Array("搜索计调名");
			if(-1 == $.inArray(data,Array_default)){
				var isIncluded = 0;
				$("#leaveCityShow a").each(function(index, element) {
					if(data == $(element).text()){
						isIncluded = 1;
						return;
					}
				});
				if(isIncluded){
					jBox.tip("您已选择");
				}else{
					$("#leaveCityShow").append('<a value="{1}">{0}</a>'.replace("{0}",data).replace("{1}",selectValue));
				}
			}
		}
	});
	//搜索联运城市
	$("select[name='transCity']" ).comboboxInquiry({
		"afterInvalid":function(event,data){
			//被选择项的value值
			var selectValue = $(this).val();
			var Array_default = new Array("搜索计调名");
			if(-1 == $.inArray(data,Array_default)){
				var isIncluded = 0;
				$("#interCityShow a").each(function(index, element) {
					if(data == $(element).text()){
						isIncluded = 1;
						return;
					}
				});
				if(isIncluded){
					jBox.tip("您已选择");
				}else{
					$("#interCityShow").append('<a value="{1}">{0}</a>'.replace("{0}",data).replace("{1}",selectValue));
				}
			}
		}
	});
	//删除选择的城市
	inquiryCheckBOXLocal();
});
function  addcity(tarObj,type){
	var paraname=$(tarObj).parent().next().attr("id");
	//alert(paraname.attr("id"));
    // 是否限制选择，如果限制，设置为disabled
    if ("" == "disabled"){
        return true;
    }
    // 正常打开	
    top.$.jBox.open("iframe:"+g_context_url+"/sysGeoCity/addcity?searchId="+paraname, "选择区域", 615, 540,{
        buttons:{"确定":"ok", "关闭":true}, submit:function(v, h, f){
            if (v=="ok"){
                var tree = h.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
                var ids = [], names = [], nodes = [];
                if ("true" == "true"){
                    nodes = tree.getCheckedNodes(true);
                }else{
                    nodes = tree.getSelectedNodes();
                }
                var html ="";
                for(var i=0; i<nodes.length; i++) {//
                	
                    if (nodes[i].isParent){
                        continue; // 如果为复选框选择，则过滤掉父节点
                    }//
                    ids.push(nodes[i].id);
                    //names.push(nodes[i].name);//
                    html+='  <input type="hidden" name="'+type+'" value="'+nodes[i].id+'"  >';
                    html +="<a  value="+nodes[i].id+">"+nodes[i].name+"</a>";
				//	$("#"+paraname).append("<a  value="+nodes[i].id+">"+nodes[i].name+"</a>");
                }
                $("#"+paraname).html(html);
            //    $("#targetAreaId").val(ids);
            }//
        }, loaded:function(h){
            $(".jbox-content", top.document).css("overflow-y","hidden");
        },persistent:true
    });
};


function updateAreas(){
	
	$("#submitButton").attr("disabled", true);
	
	 $.ajax({                 
			cache: true,                 
			type: "POST",                 
			url:g_context_url+ "/sysGeoCity/updateAreas",       
			
			data:$("#areaForm").serialize(),                
			async: false,                 
			error: function(request) {                     
				top.$.jBox.tip('操作失败');
			},                 
			success: function(data) {    
				if(data!=null&&data!=""){
					top.$.jBox.tip(data);
					$("#submitButton").attr("disabled", false);
					return false;
				}                
				top.$.jBox.tip("操作成功！");
				window.location.href=g_context_url+"/sysGeoCity/cityList";
			}             
		});
}

function  inquiryCheckBOXLocal(){
	$(".seach_checkbox").on("click","em",function(){
		$(this).parent().prev().remove();
		$(this).parent().remove();
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
		 <page:applyDecorator name="sys_cityList" ></page:applyDecorator>  
				<!--右侧内容部分开始-->
            <form id="areaForm" method="post" action="#">
              <div class="seach25 seach100">
                	<p>出发城市：</p>
                  	<p class="seach_r">
                        <span class="seach_check">
                            <a class="city btn" href="javascript:"  onclick="addcity(this,'startCityId')">&nbsp;选择&nbsp;</a>
                        </span>
                        <span id="startCityShow" class="seach_checkbox">
                        <c:forEach var="entry" items="${fromArea}" >
                        <input type="hidden" name="startCityId" value="${entry.id}" >
                        <a value="${entry.id}">${entry.name}</a>
                        </c:forEach>
                        </span>
                   </p>
                </div>
                <div class="seach25 seach100">
                	<p>到达城市：</p>
                  	<p class="seach_r">
                    	<span class="seach_check">
                            <a class="city btn" href="javascript:" onclick="addcity(this,'transCityId')">&nbsp;选择&nbsp;</a>
                        </span>
                        <span id="transCityShow" class="seach_checkbox">
                       <c:forEach var="entry" items="${arrivalsArea}" >
                        <input type="hidden" name="transCityId" value="${entry.id}" >
                        <a  value="${entry.id}">${entry.name}</a>
                       </c:forEach>
                        </span>
                   </p>
                </div>  
                <div class="seach25 seach100">
                	<p>离境城市：</p>
                  	<p class="seach_r">
                    	<span class="seach_check">
                           <a class="city btn" href="javascript:" onclick="addcity(this,'leaveCityId')">&nbsp;选择&nbsp;</a>
                        </span>
                        <span id="leaveCityShow" class="seach_checkbox">
                        <c:forEach var="entry" items="${outArea}" >
                           <input type="hidden" name="leaveCityId" value="${entry.id}" >
                        <a  value="${entry.id}">${entry.name}</a>
                       </c:forEach>
                        </span>
                   </p>
                </div>  
                <div class="seach25 seach100">
                	<p>联运城市：</p>
                  	<p class="seach_r">
                    	<span class="seach_check">
                           <a class="city btn" href="javascript:" onclick="addcity(this,'interCityId')">&nbsp;选择&nbsp;</a>
                        </span>
                        <span id="interCityShow" class="seach_checkbox">
                         <c:forEach var="entry" items="${IntermodalArea}" >
                          <input type="hidden" name="interCityId" value="${entry.id}" >
                        <a   value="${entry.id}">${entry.name}</a>
                       </c:forEach>
                        </span>
                   </p>
                </div>
			<div style="margin:50px 0 0 100px">
                <a href="javascript:history.go(-1);" target="_self"><input type="button" class="btn" value="返回" ></a>
                <input id="submitButton" type="button" onclick="updateAreas()" class="btn btn-primary" value="提交">
            </div>
                </form>
				<!--右侧内容部分结束-->
</body>
</html>
