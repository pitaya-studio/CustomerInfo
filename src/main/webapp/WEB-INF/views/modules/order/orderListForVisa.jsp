<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>签证押金收款</title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/payedConfirm.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<script type="text/javascript">
//如果展开部分有查询条件的话，默认展开，否则收起	
function closeOrExpand(){
	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='groupCode']");
	var searchFormselect = $("#searchForm").find("select");
	var inputRequest = false;
	var selectRequest = false;
	for(var i=0;i<searchFormInput.length;i++) {
		var inputValue = $(searchFormInput[i]).val();
		if(inputValue != "" && inputValue != null) {
			inputRequest = true;
			break;
		}
	}
	for(var i=0;i<searchFormselect.length;i++) {
		var selectValue = $(searchFormselect[i]).children("option:selected").val();
		if(selectValue != "" && selectValue != null && selectValue != 0) {
			selectRequest = true;
			break;
		}
	}
	if(inputRequest||selectRequest) {
		$('.zksx').click();
	}else{
		$('.zksx').text('筛选');
	}
}
function payedConfirmForVisa(ctx,orderid,agentid,obj){
	var ht = ($(window).height())*0.7;
	$.jBox("iframe:"+ctx+"/orderPay/getOrderPayInfo1?orderid="+orderid+"&agentid="+agentid,{		
		    title: "收款确认",
			width:830,
	   		height: ht,
	   		buttons:{'取消': 0,'确认收款':1},
	   		persistent:true,
	   		loaded: function (h) {
	   			$(".jbox-content").css("overflow","hidden");
	   		},
	   		submit: function(v,h,f){
	   			if(v==1){
	   				//禁止操作  所有收款确认列的链接
	   				$("table a").each(function(){
	   						$(this).addClass("disableCss");
	   				});
	   				  var payerName =  $(h.find("iframe")[0].contentWindow.payerName).val();
				     
				      var payType = $(h.find("iframe")[0].contentWindow.payType).val();
				      var bankName= $(h.find("iframe")[0].contentWindow.bankName).val();
				      var bankAccount =$(h.find("iframe")[0].contentWindow.bankAccount).val();
				      var toBankNname = $(h.find("iframe")[0].contentWindow.toBankNname).val();		
				      var toBankAccount=$(h.find("iframe")[0].contentWindow.toBankAccount).val();
				      var orderPayId = $(h.find("iframe")[0].contentWindow.orderPayId).val();
				      var DocInfoIds = $(h.find("iframe")[0].contentWindow.DocInfoIds).parent().find("div[class=uploadPath] input[name=DocInfoIds]").val();
				      var checkNumber =  $(h.find("iframe")[0].contentWindow.checkNumber).val();
				      var remarks= $(h.find("iframe")[0].contentWindow.remarks).val();
				      var accountDate = $(h.find("iframe")[0].contentWindow.accountDate).val();
				      // 因公支付宝
				      var fromAlipayName = $(h.find("iframe")[0].contentWindow.fromAlipayName).val();
				      var fromAlipayAccount = $(h.find("iframe")[0].contentWindow.fromAlipayAccount).val();
				      var toAlipayName = $(h.find("iframe")[0].contentWindow.toAlipayName).val();
				      var toAlipayAccount = $(h.find("iframe")[0].contentWindow.toAlipayAccount).val();
				      var comeOfficeName = $(h.find("iframe")[0].contentWindow.comeOfficeName).val();
			         dataparam={ 
			        	payerName:payerName,
						bankName:bankName,
						bankAccount:bankAccount,
						toBankNname:toBankNname,
						toBankAccount:toBankAccount,
						payType:payType,
						orderPayId:orderPayId,
						DocInfoIds:DocInfoIds,
						checkNumber:checkNumber,
						accountDate:accountDate,
						remarks:remarks,
						// invoiceDate:invoiceDate
						fromAlipayName:fromAlipayName,
						fromAlipayAccount:fromAlipayAccount,
						toAlipayName:toAlipayName,
						toAlipayAccount:toAlipayAccount,
						comeOfficeName:comeOfficeName
					}
			      if(accountDate == ""){
			          $.jBox.tip("银行到账日期不能为空","WARM");
			          return false;
			      }else{
			          $.ajax({ 
				          type:"POST",
				          url:ctx+"/orderPay/confirmPayInfoForVisa",
				          dataType:"json",
				          data:dataparam,
				          success:function(data){
				        	
				              $('#searchForm').submit();
				             
				          }
				      });
			      }
			     
	   			}
	   			
	   		}
	});
}

$(function(){
	//展开、收起筛选
	launch();
	//如果展开部分有查询条件的话，默认展开，否则收起
	closeOrExpand();
	//操作浮框
	operateHandler();
	//renderSelects(selectQuery());
	//计调模糊匹配
	$("[name=jd]").comboboxSingle();
	//销售模糊匹配
	$("[name=saler]").comboboxSingle();
	$("[name=creator]").comboboxSingle();
	//渠道改为可输入的select
    $("#modifyAgentInfo").comboboxInquiry();
    //收款银行改为可输入的select
     $("[name='toBankNname']").comboboxInquiry();
	
    $(document).delegate(".downloadzfpz","click",function(){
        window.open ("${ctx}/sys/docinfo/download/"+$(this).attr("lang"));
    }); 
        
	    $("#contentTable").delegate("ul.caption > li","click",function(){
	        var iIndex = $(this).index();/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
	        $(this).addClass("on").siblings().removeClass('on');
	        $(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
	    });
	
	    $("#contentTable").delegate(".saler","click",function(){
	        $(this).addClass("on").siblings().removeClass('on');
	        $('.creator_cen').removeClass('onshow');
	        $('.saler_cen').addClass('onshow');
	    });
	    
	    	
	    $("#contentTable").delegate(".creator","click",function(){
	         $(this).addClass("on").siblings().removeClass('on');
	         $('.saler_cen').removeClass('onshow');
	         $('.creator_cen').addClass('onshow');
	        
	    });
	    $("#contentTable").delegate(".tuanhao","click",function(){
	        $(this).addClass("on").siblings().removeClass('on');
	        $('.chanpin_cen').removeClass('onshow');
	        $('.tuanhao_cen').addClass('onshow');
	    });
	    
	    	
	    $("#contentTable").delegate(".chanpin","click",function(){
	         $(this).addClass("on").siblings().removeClass('on');
	         $('.tuanhao_cen').removeClass('onshow');
	         $('.chanpin_cen').addClass('onshow');
	        
	    });
        
        var _$orderBy = $("#orderBy").val();
        if(_$orderBy==""){
            _$orderBy="createDate DESC";
        }
        var orderBy = _$orderBy.split(" ");
        $(".activitylist_paixu_left li").each(function(){
            if ($(this).hasClass("li"+orderBy[0])){
                orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
                $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
                $(this).attr("class","activitylist_paixu_moren");
            }
        });

$(document).scrollLeft(0);
$("#targetAreaId").val("${travelActivity.targetAreaIds}");
$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
});
$().ready(function(){
    //产品名称获得焦点显示隐藏
            $("#wholeSalerKey").focusin(function(){
                var obj_this = $(this);
                    obj_this.next("span").hide();
            }).focusout(function(){
                var obj_this = $(this);
                if(obj_this.val()!="") {
                obj_this.next("span").hide();
            }else
                obj_this.next("span").show();
            });
            if($("#wholeSalerKey").val()!="") {
                $("#wholeSalerKey").next("span").hide();
            }
});

function changeGroup(orderId){
    window.open("${ctx}/orderCommon/manage/changGroup/"+orderId,"_blank");
}

function orderDetail(orderId){
    window.open("${ctx}/stock/manager/apartGroup/detail?id=" + orderId + "&agentId=&activityGroupId=","_blank");
}

function page(n,s){
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    $("#searchForm").submit();
    return false;
}

$(function(){
    $.fn.datepicker=function(option){
        var opt = {}||option;
        this.click(function(){
           WdatePicker(option);            
        });
    }
    
    $("#groupOpenDate").datepicker({
        dateFormat:"yy-mm-dd",
       dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
       closeText:"关闭", 
       prevText:"前一月", 
       nextText:"后一月",
       monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
       });
    
    $("#groupCloseDate").datepicker({
        dateFormat:"yy-mm-dd",
           dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
           closeText:"关闭", 
           prevText:"前一月", 
           nextText:"后一月",
           monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
           });
    //首次打印提醒20150824
	$(".uiPrint").hover(function(){
		$(this).find("span").show();
	},function(){
		$(this).find("span").hide();
	})
    
});

$(function(){
    $('.nav-tabs li').hover(function(){
        $('.nav-tabs li').removeClass('current');
        $(this).parent().removeClass('nav_current');
         if($(this).hasClass('ernav'))
         {
             if(!$(this).hasClass('current')){
                $(this).addClass('current');
                $(this).parent().addClass('nav_current');
             }
         }
        },function(){
            $('.nav-tabs li').removeClass('current');
            $(this).parent().removeClass('nav_current');
            var _active = $(".totalnav .active").eq(0);
            if(_active.hasClass('ernav')){
                _active.addClass('current');
                $(this).parent().addClass('nav_current');
            }
        });
});

function sortby(sortBy,obj){
    var temporderBy = $("#orderBy").val();
    if(temporderBy.match(sortBy)){
        sortBy = temporderBy;
        if(sortBy.match(/ASC/g)){
            sortBy = $.trim(sortBy.replace(/ASC/gi,""))+" DESC";
        }else{
            sortBy = $.trim(sortBy.replace(/DESC/gi,""))+" ASC";
        }
    }else{
        sortBy = sortBy+" DESC";
    }
    
    $("#orderBy").val(sortBy);
    $("#searchForm").submit();
}
function query() {
	$('#searchForm').submit();
}

function cancelConfirm(payId,payType,_this){
	 var $this = $(_this);
		$.jBox.confirm("确定撤销吗？", "系统提示", function(v, h, f){
			if (v == 'ok') {
				$.ajax({
			        type: "POST",
			        url: "${ctx}/orderPay/cancelVisaOper",
			        async:false,
			        cache:false,
			        data: {
			        	payId:payId,
			        	payType:payType
			        },
			        success: function(data){
			        	top.$.jBox.tip('确定成功','success');                   
	                    $this.remove();
	                    $("#searchForm").submit();
			        }
		     	}); 
			}else if (v == 'cancel'){
				
			}
	});
}
/**
 * 表单重置，不能改成 $('#searchForm')[0].reset() 
 * 输入条件点击查询之后，再点击条件重置按钮，此方法在Google Chrome 下失效 
 * @author shijun.liu
 */
function resetForm(){
	var inputArray = $('#searchForm').find("input[type!='hidden'][type!='submit'][type!='button']");
	var selectArray = $('#searchForm').find("select");
	for(var i=0;i<inputArray.length;i++){
		if($(inputArray[i]).val()){
			$(inputArray[i]).val('');
		}
	}
	for(var i=0;i<selectArray.length;i++){
		var selectOption = $(selectArray[i]).children("option");
		$(selectOption[0]).attr("selected","selected");
	}
}

function downLoad(ctx){
    var args = $('#searchForm').serialize();//查询条件参数
    window.open(ctx + "/orderCommon/manage/exportOrderListForVisa?" + args);
}

// 批量打印- wangyang 2016.10.25
$(document).on('change','#contentTable,#contentTable_foot, [type="checkbox"]',function(){
	var $this = $(this);
    var $contentContainer = $("#contentTable,#contentTable_foot");
    var $all =$contentContainer.find('[check-action="All"]');
    var $reverse = $contentContainer.find('[check-action="Reverse"]');
    var $chks=$contentContainer.find('[check-action="Normal"]:enabled');
    
    if($this.is('[check-action="All"]')){
    	if($this.is(':checked')){
        	$chks.attr('checked',true);
        }else{
        	$chks.removeAttr('checked');
        }
    }
    
    if($this.is('[check-action="Reverse"]')){
    	$chks.each(function(){
        	var $chk = $(this);
            if($chk.is(':checked')){
            	$chk.removeAttr('checked');
            }else{
            	$chk.attr('checked',true);
            }
        });
    }
    
    if($chks.length && ($chks.length ==$chks.filter(':checked').length)){
    	$all.attr('checked',true);
    }else{
    	$all.removeAttr('checked');
    }
});

function batchPrint() {
	var checks = $(".box:checked");
	var payids = new Array();
	
	if (checks.length == 0) {
		top.$.jBox.tip('请选择数据', 'warnning');
		return ;
	}
	
	for (var i = 0; i < checks.length; i++) {
		payids.push(checks[i].value);
	}
	
	var $div = $("#batchPrintDiv");
	$div.find("#payids").val(payids.join(","));
	
	$form = $div.find("#batchPrintForm");
	$form.submit();
}
</script>

<style type="text/css">
	a{display: inline-block;}
	label{ cursor:inherit;}
	.disableCss{
pointer-events:none;
color:#afafaf;
cursor:default
} 
a{
    display: inline-block;
}
label{ cursor:inherit;}
input[type="checkbox"] {margin: 0;}
</style>
</head>
<body>
<%--<page:applyDecorator name="finance_op_head" >--%>
    <%--<page:param name="current"><c:choose><c:when test="${param.option eq 'visa'}">visaList</c:when></c:choose></page:param>--%>
<%--</page:applyDecorator>--%>
<c:choose><c:when test="${param.option eq 'visa'}"><c:set var ="current" value="visaList" /></c:when></c:choose>
<%@ include file="/WEB-INF/views/head/financeHead.jsp"%>
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
 <form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/finance/manage/showFinanceList/${showType}/${orderS}.htm?option=visa" method="post" >
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
        <input id="showType" name="showType" type="hidden" value="${showType}" />
     	 <div class="activitylist_bodyer_right_team_co">
            <div class="activitylist_bodyer_right_team_co2">
	            <input id="groupCode"  name="groupCode" class="inputTxt inputTxtlong searchInput" value="${groupCode }"/>
            </div>
			<div class="zksx">筛选</div>
            <div class="form_submit">
                 <input class="btn btn-primary ydbz_x" type="button" onclick="query()" value="搜索" placeholder="请输入团号"/>
                 <input class="btn ydbz_x" type="button" onclick="resetForm();" value="清空所有条件" />
				 <input class="btn ydbz_x " onclick="downLoad('${ctx}')" value="导出Excel" type="button">
            </div>
            <div class="ydxbd">
				<span></span>
				<div class="activitylist_bodyer_right_team_co1" >
					<label class="activitylist_team_co3_text" >订单号：</label>
					<input id="orderNum" name="orderNum" class="inputTxt inputTxtlong" value="${orderNum }"/>
				</div>
				<div class="activitylist_bodyer_right_team_co1" >
					<label class="activitylist_team_co3_text" >收款银行：</label>
					<select name="toBankNname">
						<option value="">全部</option>
						<c:forEach var="toBank" items="${toBank }">
							<option value="${toBank.text }" <c:if test="${toBankNname==toBank.text}">selected="selected"</c:if>>${toBank.text }</option>
						</c:forEach>
					</select>
				</div>
           		<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">银行到账日期：</label>
					<input id="accountDateBegin" name="accountDateBegin" class="inputTxt dateinput" value="${accountDateBegin }"
											readonly onClick="WdatePicker()"/> 至 
					<input id="accountDateEnd" name="accountDateEnd" value="${accountDateEnd }" readonly onClick="WdatePicker()" class="inputTxt dateinput"/>
				</div>
				<!-- 增加计调查询条件  -->
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">计调：</label>
					<select name="jd">
						<option value="">全部</option>
						<c:forEach var="jd" items="${agentJd }">
							<option value="${jd.id }" <c:if test="${jds==jd.id}">selected="selected"</c:if>>${jd.name }</option>
						</c:forEach>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co1" >
					<label class="activitylist_team_co3_text">销售：</label>
					<select name="saler" style="width:44%;">
						<option value="">全部</option>
						<c:forEach var="salers" items="${agentSalers }">
							<option value="${salers.key }" <c:if test="${saler==salers.key}">selected="selected"</c:if>>${salers.value }</option>
						</c:forEach>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">收款日期：</label>
					<input id="createDateBegin" name="createDateBegin" class="inputTxt dateinput" value="${createDateBegin }"
											readonly onClick="WdatePicker()"/> 至 
					<input id="createDateEnd" name="createDateEnd" value="${createDateEnd }" readonly onClick="WdatePicker()" class="inputTxt dateinput"/>
				</div>
				<div class="activitylist_bodyer_right_team_co1" >
					<label class="activitylist_team_co3_text">下单人：</label>
					<select name="creator">
						<option value="">全部</option>
						<c:forEach var="creators" items="${creatorList }">
						<option value="${creators.id }" <c:if test="${creator==creators.id}">selected="selected"</c:if>>${creators.name }</option>
						</c:forEach>
					</select>
				 </div>
          		 <c:if test="${fns:getUser().userType ==3}">
					  <div class="activitylist_bodyer_right_team_co1">
							<label class="activitylist_team_co3_text">渠道选择：</label>
							<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
							<select id="modifyAgentInfo" name="agentId">
							<option value="">全部</option>
							<c:choose>
								<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' }"><option value="-1">未签</option></c:when>
								 <c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' }"><option value="-1">直客</option></c:when>
								<c:otherwise><option value="-1">非签约渠道</option></c:otherwise>
							</c:choose>
							<c:forEach var="agentinfo" items="${agentinfoList }">
							<c:choose>
		                       	<c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' && agentinfo.agentName=='非签约渠道' }">
		                       		<option value="${agentinfo.id }" <c:if test="${agentId==agentinfo.id}">selected="selected"</c:if>>直客</option>
		                       	</c:when>
		                       	<c:otherwise>
		                       		<option value="${agentinfo.id }" <c:if test="${agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName }</option>
		                       </c:otherwise>
		                      </c:choose>
							</c:forEach>
							</select>
					 </div>
				 </c:if>
				 <c:if test="${RXGG eq companyUuid}">
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">出团日期：</label>
						<input id="groupDateBegin" name="groupDateBegin" class="inputTxt dateinput" value="${groupDateBegin }"
																					  readonly onClick="WdatePicker()"/> 至
						<input id="groupDateEnd" name="groupDateEnd" value="${groupDateEnd }" readonly onClick="WdatePicker()" class="inputTxt dateinput"/>
					</div>
				</c:if>
            	<!-- 增加是否到账的查询条件  -->
            	<div class="activitylist_bodyer_right_team_co1" >
                	<label class="activitylist_team_co3_text">是否到账：</label>
					<div class="selectStyle">
						<select name="isAccounted">
						   <option value="">全部</option>
						   <option value="Y" <c:if test="${isAccounted eq 'Y'}">selected="selected"</c:if>>已到账</option>
						   <option value="N" <c:if test="${isAccounted eq 'N'}">selected="selected"</c:if>>未到账</option>
						   <option value="C" <c:if test="${isAccounted eq 'C'}">selected="selected"</c:if>>已驳回</option>
						</select>
					</div>
            	</div>
            	<div class="activitylist_bodyer_right_team_co1" >
					<label class="activitylist_team_co3_text" >来款单位：</label>
					<input id="payerName" name="payerName" class="inputTxt inputTxtlong" value="${payerName }"/>
				</div>
            	<!-- 增加打印状态查询条件20150824 -->
            	<div class="activitylist_bodyer_right_team_co1">
               		<label class="activitylist_team_co3_text">打印状态：</label>
					<div class="selectStyle">
						<select name="printFlag">
							<option value="">全部</option>
							<option value="1" <c:if test="${printFlag eq '1'}">selected="selected"</c:if>>已打印</option>
							<option value="0" <c:if test="${printFlag eq '0'}">selected="selected"</c:if>>未打印</option>
						</select>
					</div>
                </div>
            	<!--支付方式查询条件，sys_dict字典表有支付方式的数据，但是各系统平台维护的有点乱，所以在此直接写死了  -->
            	<div class="activitylist_bodyer_right_team_co1">
               		<label class="activitylist_team_co3_text">收款方式：</label>
					<div class="selectStyle">
						<select name="payType">
							<option value="">全部</option>
							<option value="1" <c:if test="${payType eq '1'}">selected="selected"</c:if>>支票</option>
							<option value="3" <c:if test="${payType eq '3' or payType eq '5'}">selected="selected"</c:if>>现金</option>
							<option value="4" <c:if test="${payType eq '4'}">selected="selected"</c:if>>汇款</option>
							<option value="6" <c:if test="${payType eq '6'}">selected="selected"</c:if>>转账</option>
							<option value="7" <c:if test="${payType eq '7'}">selected="selected"</c:if>>汇票</option>
							<option value="8" <c:if test="${payType eq '8'}">selected="selected"</c:if>>POS</option>
							<c:if test="${isLMT }">
								<option value="9" <c:if test="${payType eq '9'}">selected="selected"</c:if>>因公支付宝</option>
							</c:if>
						</select>
					</div>
            	</div>
            	<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">收款确认日期：</label>
					<input id="receiptConfirmationDateBegin" name="receiptConfirmationDateBegin" class="inputTxt dateinput" value="${receiptConfirmationDateBegin }"
											readonly onClick="WdatePicker()"/> 至 
					<input id="receiptConfirmationDateEnd" name="receiptConfirmationDateEnd" value="${receiptConfirmationDateEnd }" readonly onClick="WdatePicker()" class="inputTxt dateinput"/>
				</div>
            </div>
		 </div>
    </form:form>
    
    <div class="activitylist_bodyer_right_team_co_paixu">
        <div class="activitylist_paixu">
          	<div class="activitylist_paixu_left">
				<ul>
					<li class="activitylist_paixu_left_biankuang licreateDate"><a onClick="sortby('createDate',this)">创建时间</a></li>
					<li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
				</ul>
      		</div>
          	<div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
        </div>
    </div>

	<table id="contentTable" class="table mainTable table-bordered table-condensed activitylist_bodyer_table">
        <thead style="background:#403738;">
        <tr>
            <th width="5%">序号</th>
            <th width="6%">收款日期<br>银行到账日期</th>
            <th width="6%">收款确认日期</th>     <!-- 405需求，此列不需要加入导出的excel -->
			<th width="6%">订单号</th>
			<c:choose>
				<c:when test="${isQHJQ }">
					<th width="4%">团号</th>
					<th width="5%">产品名称</th>
				</c:when>
				<c:otherwise>
					<th width="9%">
						<span class="tuanhao on">团号</span>/
						<span class="chanpin">产品名称</span>
					</th>
				</c:otherwise>
			</c:choose>
			<c:if test="${RXGG eq companyUuid}"><th width="8%">出团日期</th></c:if>
			<th width="5%">游客姓名</th>
			<th width="5%">计调</th>
			<th width="6%">
				<c:choose>
					<c:when test="${isQHJQ }">
						<span>销售</span>
					</c:when>
					<c:otherwise>
						<span class="saler on">销售</span>/
						<span class="creator">下单人</span>
					</c:otherwise>
				</c:choose>
	        </th>
			<th width="7%">渠道名称</th>
			<th width="8%">来款单位</th>
			<th width="8%">收款银行</th>
			<th width="8%">押金金额</th>
			<th width="9%">已收金额<br>到账金额</th>
			<th width="6%">收款确认</th>
			<th width="6%">打印确认</th>
			<th width="5%">操作</th>
        </tr>
        </thead>
        <tbody>
        
        <c:if test="${fn:length(page.list) <= 0 }">
                 <tr class="toptr" >
                 <td class="tc" colspan="16">
                                      暂无搜索结果
                 </td>
                 </tr>
        </c:if>
        
        
        <c:forEach items="${page.list }" var="orders" varStatus="s">
                <tr class="toptr">
                    <td>
                    	<input type="checkbox" check-action="Normal" class="box" value="${orders.payid }" />
                    	${s.count }
                    </td>
                     <td style="padding: 0px;">
						<div class="out-date"><fmt:formatDate value="${orders.orderpaycreatedate }" pattern="yyyy-MM-dd"/></div>
						<div class="close-date">
						 <c:if test="${not empty orders.accountDate }"><c:if test="${orders.is_accounted eq '1'}"><fmt:formatDate value="${orders.accountDate}" pattern="yyyy-MM-dd"/></c:if></c:if></div> 						
					</td>
					<td><fmt:formatDate value="${orders.receiptConfirmationDate }" pattern="yyyy-MM-dd"/></td>      <!-- 收款确认日期 -->
                    <td>${orders.orderNum }</td>
					<c:choose>
						<c:when test="${isQHJQ }">
							<td><div title="${orders.groupCode}">${orders.groupCode}</div></td>
							<td>
								<div title="${orders.productName}">
	               		 			<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/visa/visaProducts/visaProductsDetail/${orders.productId}')">
										${orders.productName}
	               		 			</a>
               					</div>
							</td>
						</c:when>
						<c:otherwise>
							<td>
                    			<div class="tuanhao_cen onshow" title="${orders.groupCode}"> 
                    				${orders.groupCode}</div>
								<div class="chanpin_cen qtip" title="${orders.productName}">
	               		 			<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/visa/visaProducts/visaProductsDetail/${orders.productId}')">
										${orders.productName}
	               		 			</a>
               					</div>
               				</td>
						</c:otherwise>
					</c:choose>
					<c:if test="${RXGG eq companyUuid}"><td>${orders.start_out}</td></c:if>
               		<td>
                       ${orders.travlerName }
                    </td>
                    <c:choose>
               			<c:when test="${orders.createBy != null && orders.createBy != ''}">
               				<td>${fns:getUserById(orders.createBy).name}</td>
               			</c:when>
               			<c:otherwise>
               				<td> </td>
               			</c:otherwise>
               		</c:choose>
                    <td>
                    	<c:choose>
                    		<c:when test="${isQHJQ }">
                    			<div title="${orders.saler}">${orders.saler}</div>
                    		</c:when>
                    		<c:otherwise>
                    			<div class="saler_cen onshow" title="${orders.saler}">${orders.saler}</div>
                        		<div class="creator_cen qtip" title="${fns:getUserById(orders.creator).name}">${fns:getUserById(orders.creator).name}</div>
                    		</c:otherwise>
                    	</c:choose>
                    </td>
                    <td><!-- 315需求,针对越柬行踪，将非签约渠道改为直客 -->
                    	<c:choose>
                    		<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and orders.agentName eq '非签约渠道' }">未签</c:when>
                    		<c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && orders.agentName=='非签约渠道'}"> 
						      	 直客
						   	</c:when>
                    		<c:otherwise>${orders.agentName }</c:otherwise>
                    	</c:choose>
                    </td>
                    <td>${orders.payerName }</td>
                    <td>${orders.toBankNname }</td>
                    <td class="tr">
                      <span class="fbold"><c:if test="${not empty orders.total_money}">${orders.total_money}</c:if></span>
                    </td>
                    <td class="p0 tr pr">	
                        <c:choose>
		            	  <c:when test="${orders.payType == 1}"><p><span class="icon-mode-of-payment-check"></span></p></c:when>
		            	  <c:when test="${orders.payType == 3 or orders.payType==5}"><p><span class="icon-mode-of-payment-cash"> </span></p></c:when>
		            	  <c:when test="${orders.payType == 4 }"><p><span class="icon-mode-of-payment-remit"> </span></p></c:when>
		            	  <c:when test="${orders.payType == 6}"><p><span class="icon-mode-of-payment-transfer"> </span></p></c:when>
		            	  <c:when test="${orders.payType == 7}"><p><span class="icon-mode-of-payment-bill"> </span></p></c:when>
		            	  <c:when test="${orders.payType == 8 or orders.payType==2}"><span class="icon-mode-of-payment-pos"> </span></p></c:when>
		            	  <c:when test="${orders.payType == 9}"><p><span class="icon-mode-of-payment-alipay"> </span></p></c:when>
		            	</c:choose>
                        <div class="yfje_dd">
                     		<span class="fbold"><c:if test="${not empty orders.payed_money}">${orders.payed_money}</c:if></span>
                       </div>
                    	<div class="dzje_dd">
	                      	<span class="fbold"><c:if test="${orders.is_accounted eq '1'}">${orders.payed_money}</c:if></span>
	                    </div>
                    </td>
					<td>
					<c:choose>
					<c:when test="${empty orders.is_accounted || orders.is_accounted == 0 && not empty orders.payed_money}">
						<a href="javascript:void(0)" onClick="javascript:payedConfirmForVisa('${ctx}',${orders.payid},${orders.agentid},this);">收款确认 </a>&nbsp;
						<a href="javascript:void(0)" onClick="javascript:payedConfirmReject('${ctx}',${orders.payid},${orders.id},' ','1',this);">驳回</a>
					</c:when>
					<c:otherwise>
					<c:choose>
						<c:when test="${orders.is_accounted eq '1' }">已确认<br>
						<shiro:hasPermission name="visacash:operation:revoke">
						<a href="javascript:void(0)" onclick="javascript:cancelConfirm(${orders.payid},1,this)">撤销</a>
						</shiro:hasPermission>
						</c:when>
						<c:when test="${orders.is_accounted eq '2'}">已驳回</c:when>
						<c:otherwise></c:otherwise>
					</c:choose>
					</c:otherwise>
					</c:choose>
					</td>
					<!-- 打印状态和首次打印时间显示20150824 -->
					<td class="invoice_yes" id="print_${orders.payid}_6">
		            	<c:if test="${empty orders.printFlag || orders.printFlag == 0 }">未打印</c:if>
		            	<c:if test="${orders.printFlag == 1 }"><p class="uiPrint">已打印<span style="display: none;">首次打印时间<br><fmt:formatDate value="${orders.printTime}" pattern="yyyy-MM-dd HH:mm:ss" /></span></p></c:if>
		            </td>	
                    <td>
<%--              			<shiro:hasPermission name="looseOrder:operation:view"> --%>
                        <div class="orderList_line"> 
                        	<a target="_blank" href="${ctx}/visa/order/goUpdateVisaOrder?visaOrderId=${orders.id }&mainOrderCode=&details=1">查看详情</a>
                        	<a target="_blank" href="${ctx}/visa/order/visaCashPrint/${orders.payid}">打印</a>
                        </div>
<%--                         </shiro:hasPermission> --%>
                    </td>
                </tr>
            </c:forEach>
       
        </tbody> 
    </table>
    <%--<div id="contentTable_foot">--%>
        <%--<label><input check-action="All" type="checkbox"> 全选</label>--%>
        <%--<label><input check-action="Reverse" type="checkbox"> 反选</label>--%>
        <%--<input type="button" class="btn btn-primary" onclick="batchPrint();" value="批量打印">--%>
    <%--</div>--%>
    <div id="batchPrintDiv">
    	<form action="${ctx }/visa/order/visaCashBatchPrint" id="batchPrintForm" method="post" target="_blank">
    		<input type="hidden" name="payids" id="payids" value=""  />
    	</form>
    </div>
</div>

<div class="page" id="contentTable_foot">
		<div class="pagination">
			<dl>
				<dt><input check-action="All" type="checkbox">全选</dt>
				<dt><input check-action="Reverse" type="checkbox">反选</dt>
				<dd>
					<input type="button" class="btn ydbz_x " onclick="batchPrint();" value="批量打印">
				</dd>
			</dl>
		</div>

	<div class="pagination clearFix">
		${page}
	</div>
</div>


 <style type="text/css">
.orderList_dosome{ text-align:left; margin-left:11px;}
.orderList_line{ height:100%; width:50px;float:left; }
 </style>  
</body>
</html>