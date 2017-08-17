<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>订单管理-上传确认单</title>
<meta name="decorator" content="wholesaler" />

    
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/jquery.file.filter.js" type="text/javascript"></script>
<script type="text/javascript">

String.prototype.endWith=function(s){
	  if(s==null||s==""||this.length==0||s.length>this.length)
	     return false;
	  if(this.substring(this.length-s.length)==s)
	     return true;
	  else
	     return false;
	  return true;
}

$(function(){
	$(document).scrollLeft(0);
	$("#offlineform_5").validate({});
	jQuery.extend(jQuery.validator.messages, {
		required: "必填信息"
	});
});

var submit_times = 0;
$(function(){

	$("#submitbtn").click(function(){
	
		var filename = $(".payDiv>form:visible").find("input[name='confirmationFile']").val();
		var flag = false;
		
		// 判断当前活动DIV
		var payDivId; // 活动ID
		$(".payDiv").each(function(){
			var block = $(this).css('display');
			if(block=='block'){
				payDivId = $(this).attr('id');
			}
		})
		// 判断offlineform_5 表单的file是否已经填写
		var bool = true;
		$("#"+payDivId+" input[name='confirmationFile']").each(function(){
				var val = $(this).val();
				if(!val){
					$(this).next().css('display','inline');
					bool = false;
				}else{
					$(this).next().css('display','none');
				}
		})
		
		if(!$(".payDiv>form:visible").validate({}).form()){
			return false;
		}
	
		 if(!bool){
			 return bool;
		 }
		if(submit_times == 0) {
		
			submit_times++;
			var $div = $('<div class=\"tanchukuang\"></div>').append('<div class="msg-orderCancel"><div class="msg-orderCancel-t">提示 :当前操作会替换您之前上传的确认单,请确认是否继续上传！</div></div>');
            var html = $div.html();
            var submit = function(v){
             if(v === 0){
            	  submit_times--;
                  return true;
               }else if(v === 1){
            	   $("#offlineform_5").submit();
               }
              return false;
              };
           $.jBox(html, {title: "确认重新上传确认单", buttons:{'确定' : 1, '取消' : 0 }, submit: submit});
		}
	});

})


</script>
</head>
<body>
<page:applyDecorator name="show_head">
    <page:param name="desc">上传确认单</page:param>
</page:applyDecorator>
<div class="ydbzbox fs">
	<div class="payforDiv"> 
		<div class="payforchose">上传确认单</div>
		<div id="offline_paybox" class="pay_clearfix" style="clear: both;"><!--去掉width=750-->
			<div id="payorderbgcolor" style="display: block; z-index: 2;height:30px; position:relative;">
				<font color="red"  style="font-weight:bold;">温馨提示:当前操作会替换您之前上传的确认单 !</font>
			</div>
			<div class="payORDER"
				style="clear:both; padding:20px; border:1px #cccccc solid; margin-top:-2px; margin-top:-1px;background-color:#FFF;">
		
				<div id="offlinebox_5" class="payDiv">
					<form id="offlineform_5" method="post" enctype="multipart/form-data" action="${ctx}/orderCommon/manage/uploadConfirmation" style="margin:0px; padding:0px;">
						<input type="hidden" name="orderId" value="${productorder.id}">
						<table width="100%" cellpadding="5" cellspacing="0" border="0">
							<tbody>
									<tr class="trVoucher_5">
									<td class="trtextaling" width=""><span style="color:#f00;">*</span>确认单：</td>
									<td><input type="file" style="cursor:pointer;" name="confirmationFile"/><label style="display:none;font-size: 12px;font-weight: normal;padding-left: 0;margin-left: 10px;padding-bottom: 2px;width: 60px;color: #ea5200">必填信息</label></td>
								</tr>
							</tbody>
						</table>
						<div>
						
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
			
	<div style="overflow:hidden">
		<div class="kongr"></div>
	</div>
	<div class="ydbz_sxb ydbz_button">
		<a class="ydbz_s" href="${ctx}/orderCommon/manage/orderDetail/${orderId}">查看订单</a>
		<a class="ydbz_x" id="submitbtn" >上传确认单</a>
	</div>
</div>
</body>
</html>