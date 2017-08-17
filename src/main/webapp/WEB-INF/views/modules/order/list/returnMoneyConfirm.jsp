<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<link href="${ctxStatic}/bootstrap/2.3.1/css_default/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jh-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/huanqiu-style.css" />
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>

<script src="${ctxStatic}/modules/order/payedConfirm.js" type="text/javascript"></script>

  
<script type="text/javascript">

$(document).ready(function(){
	$("#offline_paybox  [flag='select']").each(function(index, element) {
                        $(element).jQSelect({});
                    });
      var payType= $("#payType").val();
      if(payType==1){//支票
            $("#payorderbgcolor div[class=patorder_a1]").hide();
		    $("#payorderbgcolor div[class=patorder_a3]").hide();
		    $("#payorderbgcolor div[class=patorder_a2]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
		    $("#offlinebox tr[class=huikuan]").hide();
	        $("#offlinebox tr[class=invoice]").show();
      }else if(payType==3 || payType==5){//现金
           $("#payorderbgcolor div[class=patorder_a2]").hide();
	        $("#payorderbgcolor div[class=patorder_a1]").hide();
	        $("#payorderbgcolor div[class=patorder_a3]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
            $("#offlinebox tr[class=huikuan]").hide();
	        $("#offlinebox tr[class=invoice]").hide(); 
	        $("#cashPayDisplay").hide();
      }else if(payType==4){//汇款
            $("#payorderbgcolor div[class=patorder_a2]").hide();
	        $("#payorderbgcolor div[class=patorder_a3]").hide();
	        $("#payorderbgcolor div[class=patorder_a1]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
	        $("#offlinebox tr[class=invoice]").hide();
	        $("#offlinebox tr[class=huikuan]").show();
      }
});
function tabshow(obj,str){
        $(obj).css({"color":"#3A7851","backgroundColor":"#FFF"}).siblings().css({"color":"#000","backgroundColor":""});
		$(obj).parent().siblings().children('#'+str).show().siblings().hide();
}

function downloads(docid){
		window.open("${ctx}/sys/docinfo/download/"+docid);
    }

function isnull(){
     var accountDate = $("#accountDate").val();
     if(accountDate==""){
        alert("到账日期不能为空");
     }
}

function changeBank(supplierId, bankName, eleId) {
		bankName = encodeURI(encodeURI(bankName));
		if (bankName != -1) {
			$.ajax({
				type : "POST",
				url : "${ctx}/orderPayMore/getBankAccount/" + supplierId,
				cache : false,
				async : false,
				data : "bankName=" + bankName,
				dataType : "json",//返回的数据类型  
				success : function(data) {
					var options = '';
					if (data != null) {
						for ( var i = 0; i < data.length; i++) {
							options += '<option value="'+data[i][5]+'">'
									+ data[i][5] + '</option>';
						}
					}
					$("#" + eleId).html('');
					$("#" + eleId).append(options);
				},
				error : function(e) {
					alert(e.responseText);
				}
			});
		}
}

</script>
<style>

input,input[type="text"]{height:25px;line-height:25px;padding:4px 6px;font-size:12px;}
.trtextaling,.list{font-size:12px;}
</style>
<div class="ydbzbox" style="height:300px;">
<div class="payforDiv" style="margin-left:0">
	<div id="offline_paybox" class="pay_clearfix" style="clear: both;">
                   
		<table width="100%" cellpadding="5" cellspacing="0" border="0">
			<tbody>
				<tr>
				      <td class="trtextaling" style="text-align:left;padding-left: 24px;" colspan="2">
				         您好，您选择了${chosenNum}个项目，请确认需要执行的批量操作？
				      </td>
				</tr>
				<tr>
					<td class="trtextaling" style="">备注信息：</td>
					<td class="trtextalingi" style="vertical-align:top" >
						<c:choose>
							<c:when test="${type eq '1' }"><%-- 548 549需求涉及页面 备注添加100字符限制 --%>
								<textarea name="remarks" maxlength="100" style="width:500px; resize:none;" id="remarks" 
									oninput="this.value=this.value.substring(0,100)"></textarea>
							</c:when>
							<c:otherwise>
								<textarea name="remarks" maxlength="254" style="width:500px; resize:none;" id="remarks"></textarea>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</tbody>
		</table>
		
	</div>
</div>
</div>
