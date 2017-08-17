<!-- 
author:chenry
describe:订单详情页，订单操作中 团签 功能跳转页面,适用于单团订单，散拼订单，游学订单，大客户订单，自由行订单功能列表
createDate：2014-11-03
 -->
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
<title>团签
</title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">

$(function(){
    if("${param.saveinvoiceMSG}" =="1") {
		top.$.jBox.tip('操作已成功!','success');	
	}
    $(".qtip").tooltip({
        track: true
    });
    
    $(document).scrollLeft(0);
	$("#targetAreaId").val("${travelActivity.targetAreaIds}");
	$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
    
	<%-- 前端js效果部分 --%>
			
    $("#contentTable").delegate("ul.caption > li","click",function() {
        var iIndex = $(this).index();/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
        $(this).addClass("on").siblings().removeClass('on');
        $(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
    });
		
    $('.handle').hover(function() {
    	if(0 != $(this).find('a').length){
    		$(this).addClass('handle-on');
			$(this).find('dd').addClass('block');
    	}
	},function(){$(this).removeClass('handle-on');$(this).find('dd').removeClass('block');});
  		
});

function applyLeague(orderId){
    
    var $div = $('<div class=\"tanchukuang\"></div>')
    .append('<div class="msg-orderCancel"><div class="msg-orderCancel-t">请输入退团原因（输入字数为100字以内）：</div><textarea cols="30" rows="3" name="description"></textarea></div>');
    var html = $div.html();
    var submit = function(v, h, f) {
        if(v === 0){
            return true;
        }
        if(f.description.length > 100) {
            top.$.jBox.tip('输入字数为100字以内。','warning');
            return false;
        }
        else if(v === 1) {
            $.ajax({
                type: "POST",
                url: "${ctx}/orderCommon/manage/applyLeague",
                data: {
                    orderId:orderId,
                    description : f.description
                },
                success: function(msg){
                    location.reload();
                }
            });
        }
        return false;
    };
    $.jBox(html, {title: "退团原因", buttons:{'确定' : 1, '取消' : 0 }, submit: submit});
    
   }

function myTest() {
	
	window.open("${ctx}/orderCommon/manage/viewGroupVisa?id="+1);
}

</script>

<style type="text/css">
a{
    display: inline-block;
}

*{ margin:0px; padding:0px;}
body{ background:#fff; margin:0px auto;}
.pop_gj{ padding:10px 24px; margin:0px; border-bottom:#b3b3b3 1px dashed; overflow:hidden;}
.pop_gj dt{ float:left; width:100%;}
.pop_gj dt span{ float:left; width:80px; text-align:right; color:#333; font-size:12px; overflow:hidden; height:25px; line-height:180%;}
.pop_gj dt p{ float:left; width:300px;color:#000; font-size:12px;line-height:180%;}
.pop_xg{ padding:10px 4px; margin:0px; overflow:hidden;}
.pop_xg dt{ float:left; width:100%; margin-top:10px; height:30px;}
.pop_xg dt span{ float:left; width:100px; text-align:right; color:#333; font-size:12px; overflow:hidden; height:30px; line-height:30px;}
.pop_xg dt p{ float:left; width:110px;color:#333; font-size:12px;height:30px; line-height:30px;overflow:hidden; position:relative;}
.pop_xg dt p font{ color:#e60012; font-size:12px;}
.pop_xg dt p input{width:60px; height:28px; line-height:28px; padding:0px 5px 0px 18px; color:#403738; font-size:12px; position:relative; z-index:3; }
.pop_xg dt p i{ position:absolute; height:28px; top:2px; width:10px; text-align:center; left:5px; z-index:5; font-style:normal; line-height:28px;}
.release_next_add button{ cursor:pointer; border-radius:4px;}
label{ cursor:inherit;}
</style>

</head>

<body>
<div id="sea">

	<!-- 顶部参数 -->
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
    
 <!--右侧内容部分开始-->
         <div class="ydbzbox fs">
            <div class="orderdetails">

               <div class="orderdetails_tit"><span>1</span>订单信息</div>
               <div class="orderdetails1">
               		<table border="0" width="96%" style="margin-left:0;">
						<tbody>
							<tr>
								<td class="mod_details2_d1">下单人：</td>
						        <td class="mod_details2_d2">管理员</td>
								<td class="mod_details2_d1">下单时间：</td>
								<td class="mod_details2_d2">2014-09-12 18:05:34</td>  
                                <td class="mod_details2_d1">团队类型：</td>
						        <td class="mod_details2_d2">参团游</td>	
                                <td class="mod_details2_d1">订单团号：</td>
								<td class="mod_details2_d2">TES347943553</td>	        
							</tr>
							<tr> 
								<td class="mod_details2_d1">收客人：</td>
						        <td class="mod_details2_d2">李四</td>
                                <td class="mod_details2_d1">订单编号：</td>
								<td class="mod_details2_d2">JHG140912049</td>
								<td class="mod_details2_d1">参团订单编号：</td>
								<td class="mod_details2_d2">TES347943553</td>
								<td class="mod_details2_d1">参团团号：</td>
						        <td class="mod_details2_d2">TES347943553</td>
                                </tr>
						</tbody>
					</table>
               </div>
               <div class="orderdetails_tit orderdetails_titpr"><span>2</span>游客列表</div>
               <div class="orderdetails3">
                  <table class="activitylist_bodyer_table" id="contentTable">
				   <thead>
					  <tr>
						 <th width="5%">姓名</th>
						 <th width="8%">护照号</th>
						 <th width="6%">AA码</th>
						 <th width="6%">签证类别</th>
						 <th width="6%">签证国家</th>
						 <th width="7%">预计出团时间</th>
						 <th width="7%">预计约签时间</th>
                         <th width="7%">实际出团时间</th>
						 <th width="7%">实际约签时间</th>
						 <th width="6%">签证状态</th>
                         <th width="6%">护照状态</th>
                         <th width="6%">担保状态</th>
                         <th width="6%">应收金额<br/>达账金额</th>
                         <th width="6%">应收押金</th>
                         <th width="3%">操作</th>
                         <th width="3%">下载</th>
                         <th width="3%">财务</th>
					  </tr>
				   </thead>
				   <tbody>
					  <tr>
						 <td>张三三</td>
						 <td>HOU059234<span class="lianyun_name">即将过期</span></td>
						 <td>AA23456</td>
						 <td>个签</td>
						 <td>美国</td>
						 <td class="tc">2014-08-29</td>
                         <td class="tc">2014-08-29</td>
                         <td class="tc">2014-08-29</td>
                         <td class="tc">2014-08-29</td>
						 <td>出签</td>
                         <td>已借出</td>
                          <td>担保</td>
                          <td>无</td>
                         <td class="p0 tr">	
						<div class="yfje_dd">	
							¥<span class="fbold">15</span>
						</div>
						<div class="dzje_dd">
							¥<span class="fbold">15</span>
						</div>
					</td>
                           <td class="p0">
							<dl class="handle">
								<dt><img src="${ctxStatic}/images/handle_cz.png" title="操作"></dt>
								<dd class="">
									<p>
										<span></span> 
										<a>详情</a>
										<a href="">撤签</a>
								   </p>
								</dd>
							</dl>
							
						</td>
						<td class="p0">
							<dl class="handle">
								<dt><img src="${ctxStatic}/images/handle_xz.png"></dt>
								<dd class="">
									<p>
										<span></span> 
										<a>个人预约表</a>
								   </p>
								</dd>
							</dl>
						</td>
						 <td class="p0">
							<dl class="handle">
								<dt><img src="${ctxStatic}/images/handle_fk.png"></dt>
								<dd class="">
									<p>
										<span></span> 
										<a onclick="" href="javascript:void(0)">付款</a>
										<a target="_blank" href="">交押金</a>
								   </p>
								</dd>
							</dl>
						</td>
					  </tr>
				   </tbody>
				</table>
               </div>
            </div>
            
    </div>
    <!--右侧内容部分结束--> 
    
</div>
</div>
</body>
</html>
