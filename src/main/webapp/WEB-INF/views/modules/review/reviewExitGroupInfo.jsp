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
<title>退团申请审核
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
	
  	$(".btn-primary").click(function(){
		
	});	
});
//驳回
function jbox_bohui(rid,roleId){
	var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
	html += '<textarea style="width:80%; margin:10px auto;" id="denyReason" cols="" rows="" ></textarea>';
	html += '</div>';
	$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
			var htm=$("#denyReason").val();
			$("input[name='denyReason']").val(htm);
			$.ajax({
		            type: "POST",
		            url: "${ctx}/orderReview/manage/reviewExitGroupInfo",
		            data: {
		            rid:rid,
		            roleId:roleId,
		            result:0,
		            userLevel:"${userLevel}",
		            denyReason:$("#denyReason").val()		            
		            },
		            success: function(msg){
		            	if(msg.flag == 1){
		            		top.$.jBox.tip('操作成功！');
		            		window.opener.location.href = window.opener.location.href;
	           				window.close();
		            	}else{
		            		top.$.jBox.tip(msg.message, 'error', { focusId: 'name' });
		            	}
		            }
		        });
		}
	},height:250,width:500});
	
}

function reviewPass(rid,roleId){
$("input[name='denyReason']").val("");
	$.ajax({
		            type: "POST",
		            url: "${ctx}/orderReview/manage/reviewExitGroupInfo",
		            data: {
		            rid:rid,
		            roleId:roleId,
		            result:1,
		            userLevel:"${userLevel}",
		            denyReason:""		            
		            },
		            success: function(msg){
		            	if(msg.flag == 1){
		            		top.$.jBox.tip('操作成功！');
		            		window.opener.location.href = window.opener.location.href;
	           				window.close();
		            	}else{
		            		top.$.jBox.tip(msg.message, 'error', { focusId: 'name' });
		            	}
		            }
		        });
}
function cancel(){

	alert("关闭");
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
.wpr20 label{ width:60px; text-align:right;}
.fl{float:left;}
.fr{float:right;}
.ydbz_tit{
	background-color:#f3f3f3;
	line-height:33px;color:#333333;
	font-weight:bold;padding-left:32px;margin-top:10px;margin-bottom:10px;
	position:relative; *height:33px;
	font-size:14px; border-radius:1px;
	overflow:hidden;
}
label{ cursor:inherit;}
</style>

</head>

<body>
<div class="bgMainRight">
				<!--右侧内容部分开始-->
				<div class="mod_nav">审核 > 退团申请 > 退团审批</div>
				<%@ include file="/WEB-INF/views/modules/order/rebates/rebatesBaseInfo.jsp"%>
				<div class="ydbz_tit">
					<span class="fl">游客退团</span><span class="fr wpr20">报批日期：
					<fmt:formatDate value="${orderInfo.reviewInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					 </span>
        		</div>
				<table id="contentTable" class="table activitylist_bodyer_table">
                    <thead>
                        <tr>
                            
                            <th width="10%">游客</th>
                            <th width="15%">下单时间</th>
                            <th width="15%">应收金额</th>
                            <th width="15%">退团原因</th>
                        </tr>
                    </thead>
                    
                    <tbody>                    
						<tr>
							<td>${orderInfo.travelerInfo.name}</td>
							<td>
							<fmt:formatDate value="${orderInfo.travelerInfo.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</td>
							<td class="tr"><span class="tdred">${orderInfo.travelerInfo.payPrice}</span></td>							
							<td>${orderInfo.reviewInfo.createReason}
							<input name="travelerId"value="${orderInfo.travelerInfo.travelerId}" type="hidden">
							<input name="travelerName"value="${orderInfo.travelerInfo.name}" type="hidden">
							</td>					
						</tr>
                    </tbody>                    
                </table>
                <%@ include file="/WEB-INF/views/modules/order/transferMoney/transferMoneyReviewInfo.jsp"%>
				<div style="margin-top:20px;"></div>				
				<div class="dbaniu">
					<a class="ydbz_s gray" href="javascript:window.opener=null;window.close();">关闭</a>
					<a class="ydbz_s" onclick="jbox_bohui('${rid}','${roleId }');">驳回</a>
					<input name="denyReason" type="hidden"/>
					<input type="submit" value="审核通过" class="btn btn-primary" onclick="reviewPass('${rid}','${roleId }');">
				</div>
				<!--右侧内容部分结束-->
			</div>
</body>
</html>
