<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>订单管理-收款凭证修改</title>
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

var fiterFile = new Array();
fiterFile.push(".gif");
fiterFile.push(".doc");
fiterFile.push(".jpg");
fiterFile.push(".jpeg");
fiterFile.push(".gif");
fiterFile.push(".png");
fiterFile.push(".pdf");
fiterFile.push(".tif");

$(function(){
	
$(document).scrollLeft(0);
})

//表单验证
	$().ready(function() {
	 
	 $("#offlineform_5").validate({
	 	});
	 	jQuery.extend(jQuery.validator.messages, {
		  		required: "必填信息"
		  	
	  		});
});



		function inFileName(obj){
			var flag = $(obj).fileInclude({includes:[".jpg",".jpeg",".gif",".png",".pdf",".tif"]});
    		if(!flag){
    			// IE
    			if($.browser.msie){
    				obj.outerHTML+='';
    			}
    			// 其他浏览器
    			else{
    				$(obj).val("");
    			}
    			top.$.jBox.info("文件上传仅支持带有jpg，jpeg，gif，png，pdf，tif后缀名的文件", "警告");
				top.$('.jbox-body .jbox-icon').css('top','55px');
    		}
    	}

var submit_times = 0;
$(function(){

	$("#submitbtn").click(function(){
	
		var filename = $(".payDiv>form:visible").find("input[name='payVoucher']").val();
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
		$("#"+payDivId+" input[name='payVoucher']").each(function(){
				var val = $(this).val();
				if(!val){
					$(this).next().css('display','inline');
					bool = false;
				}else{
					$(this).next().css('display','none');
				}
		})
		
		$.each(fiterFile,function(key,value){
			if(filename.endWith(value)){
				flag = true;
			}
		});
		if(!$(".payDiv>form:visible").validate({}).form()){
			return false;
		}
		if(!flag){
			top.$.jBox.tip('文件格式不合法','error');
			return false;
		}
	
		 if(!bool){
			 return bool;
		 }
		if(submit_times == 0) {
		
			submit_times++;
			var $div = $('<div class=\"tanchukuang\"></div>').append('<div class="msg-orderCancel"><div class="msg-orderCancel-t">提示 :当前操作会替换您之前上传的凭证,请确认是否继续上传！</div></div>');
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
           $.jBox(html, {title: "确认重新上传凭证", buttons:{'确定' : 1, '取消' : 0 }, submit: submit});
			//$("#offlineform_5").submit();
		}
	});


	
	
	/* 增加支付凭证*/
	$("input[name='offlinebox_5_payVoucherAdd']").click(function(){
		var trVou5 = $("#offlineform_5 tr[class='trVoucher_5']:eq(0)").clone(true);
		$(trVou5).find("input[name='offlinebox_5_payVoucherAdd']").val("删除凭证");
		//按照浏览器分类，清空新增input file的value
		// IE
		if($.browser.msie){
			$(trVou5).find("input[name='payVoucher']").outHTML+='';
		}
		// 其他浏览器
		else{
			$(trVou5).find("input[name='payVoucher']").val("");
		}
		var trV = $("#offlineform_5 input[name='offlinebox_5_payVoucherAdd']").index(this);
		if(trV==0){
			$("#offlineform_5 .trVoucher_5:last").after($(trVou5).clone(true));
		}else{
			$(this).parent().parent().remove();
		}
	});

})


</script>
</head>
<body>
<div class="ydbzbox fs">

<div class="payforDiv"> 


<div class="payforprice">收款凭证金额为：<span><i>${orderpay.moneySerialNum}</i></span>
订单总额为：<span><i>${productorder.totalMoney}</i></span><c:if test="${productorder.orderState eq 1 or productorder.orderState eq 2 }">&nbsp;&nbsp;&nbsp;<span><i>（尚未占位）</i></span></c:if>
</div>

<div class="payforchose">重新上传收款凭证</div>

	<div id="offline_paybox" class="pay_clearfix" style="clear: both;"><!--去掉width=750-->
     
		<div id="payorderbgcolor" style="display: block; z-index: 2;height:30px; position:relative;">
		<font color="red"  style="font-weight:bold;">温馨提示:当前操作会替换您之前上传的凭证 !</font>
		</div>
		<div class="payORDER"
			style="clear:both; padding:20px; border:1px #cccccc solid; margin-top:-2px; margin-top:-1px;background-color:#FFF;">
	
			<div id="offlinebox_5" class="payDiv">
				<form id="offlineform_5" method="post" enctype="multipart/form-data" action="${ctx}/order/manage/modifypayVoucherFile" style="margin:0px; padding:0px;">
					<input type="hidden" name="orderId" value="${productorder.id}">
					<input type="hidden" name="payId" value="${orderpay.id}">
					<table width="100%" cellpadding="5" cellspacing="0" border="0">
						<tbody>
								<tr class="trVoucher_5">
								<td class="trtextaling" width=""><span style="color:#f00;">*</span>收款凭证：</td>
								<td><input type="file" style="cursor:pointer;" name="payVoucher" onchange="inFileName(this)"/><label style="display:none;font-size: 12px;font-weight: normal;padding-left: 0;margin-left: 10px;padding-bottom: 2px;width: 60px;color: #ea5200">必填信息</label>（支持格式：jpg，jpeg，gif，png，pdf，tif）</td>
								<td><input type="button" name="offlinebox_5_payVoucherAdd" style='cursor:pointer;' class='required btn btn-primary' value="增加凭证"/></td>
							</tr>
							<tr>
								<td class="trtextaling" style="">备注信息：</td>
								<td class="trtextalingi" style="vertical-align:top"><textarea
										name="remarks" maxlength="254"
										style="width:500px;resize:none;"></textarea>
								</td>
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
					<a class="ydbz_s" href="${ctx}/order/manage/airticketOrderDetail?orderId=${orderId}">查看订单</a>
					<a class="ydbz_x" id="submitbtn" >上传凭证</a>

			</div>
</div>
</body>
</html>