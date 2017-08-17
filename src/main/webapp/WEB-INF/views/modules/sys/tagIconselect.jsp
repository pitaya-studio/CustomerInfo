<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html style="overflow-x:hidden;overflow-y:auto;">
<head>
    <title>图标选择</title>
	<%@include file="/WEB-INF/views/include/head.jsp" %>
    <style type="text/css">
		.the-icons {padding:25px 10px 15px;list-style:none;}
		.the-icons li {float:left;width:22%;line-height:25px;margin:2px 5px;cursor:pointer;}
		.the-icons i {margin:1px 5px;} .the-icons li:hover {background-color:#5f7795; color:#ffffff;}
        .the-icons li.active {background-color:#0088CC;color:#ffffff;}
    </style>
    <script type="text/javascript">
	    $(document).ready(function(){
	    	$("#icons li").click(function(){
	    		$("#icons li").removeClass("active");
	    		$("#icons li i").removeClass("icon-white");
	    		$(this).addClass("active");
	    		$(this).children("i").addClass("icon-white");
	    		$("#icon").val($(this).text());
	    	});
	    	$("#icons li").each(function(){
	    		if ($(this).text()=="${value}"){
	    			$(this).click();
	    		}
	    	});
	    	$("#icons li").dblclick(function(){
	    		top.$.jBox.getBox().find("button[value='ok']").trigger("click");
	    	});
	    });
    </script>
</head>
<body>
<input type="hidden" id="icon" value="${value}" />
<ul class="the-icons clearfix the-icons-modifiy" id="icons">
    <li><i class="icon-menu1"></i>iconMenu-1</li>
    <li><i class="icon-menu2"></i>iconMenu-2</li>
    <li><i class="icon-menu3"></i>iconMenu-3</li>
    <li><i class="icon-menu4"></i>iconMenu-4</li>
    <li><i class="icon-menu5"></i>iconMenu-5</li>
    <li><i class="icon-menu6"></i>iconMenu-6</li>
    <li><i class="icon-menu7"></i>iconMenu-7</li>
    <li><i class="icon-menu8"></i>iconMenu-8</li>
    <li><i class="icon-menu9"></i>iconMenu-9</li>
    <li><i class="icon-menu10"></i>iconMenu-10</li>
    <li><i class="icon-menu11"></i>iconMenu-11</li>
    <li><i class="icon-menu12"></i>iconMenu-12</li>
    <li><i class="icon-menu13"></i>iconMenu-13</li>
    <li><i class="icon-menu14"></i>iconMenu-14</li>
    <li><i class="icon-menu15"></i>iconMenu-15</li>
    <li><i class="icon-menu16"></i>iconMenu-16</li>
    <li><i class="icon-menu17"></i>iconMenu-17</li>
    <li><i class="icon-menu18"></i>iconMenu-18</li>
    <li><i class="icon-menu19"></i>iconMenu-19</li>
    <li><i class="icon-menu20"></i>iconMenu-20</li>
    <li><i class="icon-menu21"></i>iconMenu-21</li>
    <li><i class="icon-menu22"></i>iconMenu-22</li>
    <li><i class="icon-menu23"></i>iconMenu-23</li>
    <li><i class="icon-menu24"></i>iconMenu-24</li>
</ul>
</body>