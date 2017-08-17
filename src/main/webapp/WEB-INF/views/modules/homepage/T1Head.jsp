<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%--<link rel="stylesheet" href="${ctxStatic}/css/t1t2.css"/>--%>
<link rel="stylesheet" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.min.css"/>
<style>
	/* *{
		-webkit-box-sizing:initial; 
   		-moz-box-sizing:initial;
     	box-sizing:initial;
	} */
</style>
<script type="text/javascript" src="${ctxStatic}/js/t1ForHuiTeng.js"></script>
<script type="text/javascript">
	//用于百度统计的js代码
	var _hmt = _hmt || [];
	(function() {
		var hm = document.createElement("script");
		hm.src = "//hm.baidu.com/hm.js?09b70cb643d83d26f997900c24c7ab59";
		var s = document.getElementsByTagName("script")[0];
		s.parentNode.insertBefore(hm, s);
	})();
	$(function(){
		$.ajax({
			type:"POST",
			url:"${ctx}/t1/orderList/manage/countHasSeen",
			data:{},
			success:function(data){
				if(data.hasSeenCount && data.hasSeenCount > 0){
					$(".hint").css('display','block');
					$(".hint").html(data.hasSeenCount);
				}else{
					$(".hint").css('display','none');
				}
			}
		});
	});


	function targetToOrderList(){
		$.ajax({
			type:"POST",
			url:"${ctx}/t1/orderList/manage/updateHasSeen",
			data:{},
			success:function(data){
				if('ok' == data.flag){
					window.location.href = "${ctx}/t1/orderList/manage/showOrderList/2.htm";
				}else{

				}
			}
		});
	}
	function targetToRecordOrder(){
				window.location.href = "${ctx}/t1/preOrder/manage/showT1OrderList";
	}

	//回到首页函数
	function backHomePage(){
		//modify by wlj at 2016.11.24 for huiteng-start
		var domain=window.location.host;
		if(domain.indexOf("demo.quauqsystem.com.cn")==-1){
			location.href =  "${ctx}/t1/newHome";
		}else{
			location.href =  "${ctx}/t1/jumpParam";
		}
		//modify by wlj at 2016.11.24 for huiteng-end
	}

	//个人信息中心
	function targetToUserInfo(){
		window.location.href="${ctx}/person/info/getAgentInfo";//个人中心
	}

	//修改密码
	function change_password(){
		$.jBox("iframe:" + "${ctx}/t1/password/manage/modifyPwd", {
			title : "修改密码",
			width : 470,
			height : 330,
			persistent : true,
			buttons : false
		});
	}
	//批发商认证
	function targetToOfficeList(){
		window.location.href="${ctx}/wholesalers/certification/getOfficeList"
	}
	function showNoticeList(){
		if($("#userCenterList").hasClass("expended")){
			$(".user_management").css("background-position"," -100px -129px");
			$("#userCenterList").animate({
				height: "0px"
			}, function () {
				$("#userCenterList").removeClass('expended');
			});
		}else{
			$(".user_management").css("background-position","-97px -102px");
			$("#userCenterList").animate({
				height: "70px"
			}, function () {
				$("#userCenterList").addClass('expended');
			});
		}
	}
</script>
<input id="ctx" type="hidden" value="${ctx}" />
<%@ include file="/WEB-INF/views/modules/order/orderProgressTracking/orderProgressTrackingList.jsp"%>
<div class="header" <c:if test="${ctxs==1 }">style="background:#ffffff;box-shadow: 0 1px 8px #CFCFCF;height:45px;"</c:if><c:if test="${ctxs!=1 }">style="height:30px;"</c:if>>
	<div class="header_child">
	<c:choose>
		<c:when test="${ctxs==1 }">
			<div class="hedear-left">
		         <div id="logo_t1Head" class="" onclick="backHomePage()" style="cursor:pointer;"></div>
		         <div class="clear"></div>
		     </div>
		</c:when>
		<c:otherwise>
			<span id="gray_t1Head" class="gray" style="margin-top:1px">旅游交易预订系统</span>
		</c:otherwise>
	</c:choose>
		<span class="float_right header_child_span" <c:if test="${ctxs==1 }">style="margin-top:5px;"</c:if> >
			<div class="relative header_child_div">
				<span onclick="showNoticeList()">${fns:getUser().name}<em class="t1_2 user_management all_management"></em></span>
				<div class="userCenterList"  id="userCenterList" style="height:0;">
					<a class="passwordChange" href="javascript:change_password();"><em></em>修改密码</a>
					<a class="logout" href="${ctx }/logout"><em></em>退出登录</a>
					<div class="kong"></div>
				</div>
			</div>
			<!-- <span><em class="order-track-btn"></em><a id="orderTracking_btn" href="javascript:void(0)" style="text-decoration:none;">订单跟踪</a></span> -->
			<span id="wholesalers_t1Head" style="display: none"><a class="text-decoration_none relative" href="javascript:targetToOfficeList();"><i class="fa fa-vimeo" style="color: #666666;font-size: 14px;vertical-align: text-bottom;"></i> 批发商认证</a></span>
			<span><a class="text-decoration_none relative" href="javascript:targetToOrderList();"><em class="t1_2 order_management all_management"></em>订单管理<i class="hint"></i></a></span>
			<c:if test="${fns:getUser().differenceRights eq 1}">
				<span><a class="text-decoration_none relative" href="javascript:targetToRecordOrder();"><em class="t1_2 order_record all_management"></em>下单记录<%--<i class="hint"></i>--%></a></span>
			</c:if>
			<span>
				<a class="text-decoration_none" href="javascript:targetToUserInfo();">
					<em class="t1_2 account_management all_management"></em>账户管理
					<b class="notice" id="hasSeenCountR" style="display: none;">
						<i></i>
					</b>
				</a>
			</span>
		</span>
	</div>
</div>
