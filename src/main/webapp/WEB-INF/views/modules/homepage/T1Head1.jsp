<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<script type="text/javascript">

//-->

	$(function(){
		$.ajax({
				type:"POST",
				url:"${ctx}/t1/orderList/manage/countHasSeen",
				data:{},
				success:function(data){
					if(data.hasSeenCount && data.hasSeenCount >0){
						$("#hasSeenCountM").css('display','block'); 
						$("#hasSeenCountM").children().text(data.hasSeenCount);
						$("#hasSeenCountR").css('display','block'); 
						$("#hasSeenCountR").children().text(data.hasSeenCount);
					}else{
						
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
						window.location.href = "${ctx}/t1/orderList/manage/showOrderList/0";
					}else{
						
					}
				}
			});
	  }

	  //回到首页函数
	    function backHomePage(){
	    	window.location.href="${ctx}/activity/manager/homepagelist";//回到首页
	    }
	    
	    //个人信息中心
	    function targetToUserInfo(){
	    	window.location.href="${ctx}/person/info/getAgentInfo";//个人中心
	    }
	    
	    //修改密码
	    function targetToPswMng(){
	    	window.location.href="${ctx}/t1/password/manage/modifyPwd";//个人中心
	    }
</script>

 <div class="hedear">
     <div class="hedear-left">
         <div class="hedear-logo"></div>
         <div class="clear"></div>
     </div>
     <c:if test="${updateShow != 1 }">
     <div class="hedear-right">
         <ul class="hedear-nav">
             <li class="user-center">
             <!-- <em></em><a href="javascript:void(0)"><span>个人中心</span></a> -->
              <em onclick="showNoticeList()"></em>
                     <a onclick="showNoticeList()"href="javascript:void(0)" >
                     	<span>个人中心
                     	<b class="noticeParent" id="hasSeenCountM" style="display: none;">
                     	<i></i>
                     	</b>
                     	</span>
                     </a>
                     <div class="userCenterList"  id="userCenterList" style="height:0;">
                                 <a class="order" href="javascript:targetToOrderList();">
                                 <em></em>订单管理
                                 <b class="notice" id="hasSeenCountR" style="display: none;">
                                 <i></i>
                                 </b>
                                 </a>
                                 <a class="userInfo" href="javascript:targetToUserInfo();"><em></em>个人信息</a>
                                 <a class="passwordChange" href="javascript:targetToPswMng();"><em></em>修改密码</a>
                              <div class="kong"></div>
                     </div>
             </li>
             <li class="head-home" style="cursor:hand;" onclick="backHomePage();"><em></em><a>返回首页</a></li>
             <div class="clear"></div>
         </ul>
     </div>
    </c:if>
     <div class="clear"></div>
 </div>
