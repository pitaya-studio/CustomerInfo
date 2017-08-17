function checkNum(dom){
	 var thisvalue = $(dom).val();

	    if(thisvalue.length >15){

	        top.$.jBox.info("改价金额位数不合法", "提示");


	        thisvalue = '0.00';
	    }

	    var minusSign = false;
	    if(thisvalue){
	        if(/^\-/.test(thisvalue)){
	            minusSign = true;
	            thisvalue = thisvalue.substring(1);
	        }
	        thisvalue = thisvalue.replace(/[^\d\.]/g,"").replace(/(\.\d{4}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
	        var txt = thisvalue.split(".");
	        thisvalue = txt[0]+(txt.length>1?"."+txt[1]:"");
	        if(minusSign){
	            thisvalue = '-' + thisvalue;
	        }
	        if(thisvalue < 0){
	        	thisvalue = "";
	        }
	        $(dom).val(thisvalue);
	    }
}

Date.prototype.Format = function(fmt)   
{ //author: meizz   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}  

function showPayDetailInfo(ctx,payId,payType,callback,orderType,obj){
	$.jBox("iframe:"+ ctx +"/orderPayMore/showPayDetailInfo?payId=" + payId + "&payType=" + payType+"&orderType="+orderType, {
	    title: "支付记录",
		width: 470,
   		height: 350,
   		buttons: {'关闭':9,},
   		persistent:true,
   		loaded: function (h) {},
   		submit: function (v, h, f) {
   			if(v==1){
   				var refundId =  $(h.find("iframe")[0].contentWindow.payId).val();
  		        var recordId =  $(h.find("iframe")[0].contentWindow.recordId).val();
  		      /*$.ajax({
  		            type:"GET",
  		            url:ctx+"/refund/manager/undoRefundPayInfo",
  		            dataType:"json",
  		            data:{refundId:refundId,recordId:recordId},
  		            success:function(data){
  		              if(data.flag=='ok'){
  		                 $(obj).parents("table[id=contentTable]").siblings('#searchForm').submit();
  		              }
  		            }
  		         });*/
   				callback(ctx,refundId,recordId,obj);
   			}
   		}
	}).find("#jbox-content").css("overflow", "hidden");
}

function confirmOrCannePay(ctx,id,type,status,flag,orderType){
	var msg='';
	if(0==flag){
		msg="确认付款吗？"
	}else{
		msg="取消付款确认吗？"
	}
	$.jBox.confirm(msg,"提示",function(v,h,f){
		if(v=='ok'){
			$.ajax({
				type:"POST",
				url:ctx+"/orderCommon/manage/confirmPay",
				data:{id:id,type:type,status:status,orderType:orderType},
				success:function(data){
					if('ok' == data.flag){
						 $("#searchForm").submit();
					}else{
						
					}
				}
			})
		}
		if(v=='cancel'){
			
		}
	})
}

//137 start 成本付款 确认付款弹窗
function jbox_paymentconfirmLx(currencyId,index,amount,rateInt,ctx,id,type,status,flag,orderType) {
    var $pop = $.jBox($("#payment_confirm_pop_o").html(), {
        title    : "付款确认", buttons: {'确认': 1, '取消': 2}, submit: function (v, h, f) {
            if (v == "1") {
            	
                if ($pop.find('.rate').val().trim() == "") {
                    top.$.jBox.info("汇率不能为空", "提示");
                    return false;
                }

                if ($pop.find('.rate').val() == 0 || $pop.find('.rate').val().trim() == "0") {
                    top.$.jBox.info("汇率不能为0", "提示");
                    return false;
                }
                var rates = $pop.find("input[name='rate']").val();
                if($pop.find("font[id='moneyFlag']").text()=="¥" && rates != 1){
                	top.$.jBox.info("人民币汇率默认为1，且不允许修改", "提示");
                    return false;
                }
                
                var rateS = $pop.find("input[name='rate']").val();
				var confirmCashierDate = $pop.find("input[name='confirmCashierDate']").val();
				if ($pop.find("input[name='confirmCashierDate']").val().trim() == "") {
					top.$.jBox.info("出纳时间为必填信息！", "提示");
					return false;
				}
              //付款确认
                $.ajax({
    				type:"POST",
    				url:ctx+"/orderCommon/manage/confirmPay",
    				data:{id:id,type:type,status:status,orderType:orderType,rate:rateS,afterAmount:amount*rateS,confirmCashierDate:confirmCashierDate},
    				success:function(data){
    					if('ok' == data.flag){
    						 $("#searchForm").submit();
    						 return true;
    					}else{
    						return false;
    					}
    				}
    			})
            }
        }, height: '500', width: 500
    });
	$("#jbox-content").css("overflow-y","hidden");
    //初始化弹出框
    $pop.find("input[name='rate']").val(parseFloat(rateInt).toFixed(4));
    $pop.find("span[name='payment']").html(parseFloat(amount).toFixed(2));
    var moneyFlag = "#moneyFlag"+index;
    var moneyFlag = $(moneyFlag).text();
    $pop.find("font[id='moneyFlag']").html(moneyFlag);
    var afterexchange1 = amount * rateInt;
    $pop.find("span[name='afterexchange']").html(parseFloat(afterexchange1).toFixed(2));

    if($pop.find("font[id='moneyFlag']").text()=="¥"){
    	$pop.find("input[id='singleRate']").attr("readonly","readonly");
       }

    if(currencyId == 37){
    	$pop.find("input[name='rate']").attr("readonly","readonly");
    }
    

    $pop.find("input[name='rate']").keyup(function () {
        var rate = $pop.find("input[name='rate']").val();
        var payment = $pop.find("span[name='payment']").html();
        var afterexchange = payment * rate;
        $pop.find("span[name='afterexchange']").html(parseFloat(afterexchange).toFixed(2));
    });
    inquiryCheckBOX();

//137 END 确认付款弹窗
}

//新增询价多选计调和线路国家
function inquiryCheckBOX() {
    $(".seach_checkbox").on("click", "em", function () {
        $(this).parent().remove();
    })
    $(".seach_checkbox").on("hover", "a", function () {
        $(this).append("<em></em>")
    })
    $(".seach_checkbox").on("mouseleave", "a", function () {
        $(this).parent().find('em').remove();
    })
}


/**
 * 成本付款、退款付款、返佣付款、借款付款批量付款确认
 * @param ctx
 * @param id
 * @param type  1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款；
 *              7：新审核成本录入付款；8：新审核退款付款；9：新审核返佣付款；10：新审核借款付款；
 * @param status 0表示未付，1表示已付
 * */
function batchConfirmOrCannePay(ctx,id,type,status){
	var tmp = '';
    $("input[name='"+id+"']").each(function(){ 
    if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
           tmp=tmp+$(this).attr('value')+",";
       }
    });    
    if(tmp==""){
        alert("请选择付款记录");
        return;
    }
	var msg="确认付款吗？";
	$.jBox.confirm(msg,"提示",function(v,h,f){
		if(v=='ok'){
			$.ajax({
				type:"POST",
				url:ctx+"/orderCommon/manage/batchConfirmPay",
				data:{id:tmp,type:type,status:status},
				success:function(data){
					if('ok' == data.flag){
						 $("#searchForm").submit();
					}else{
						
					}
				}
			})
		}
		if(v=='cancel'){
			
		}
	})
}

//137 START 成本付款 确认付款  批量弹窗
function paymentconfirmallLx(ctx,id,type,status) {
	var tmp = '';
	var flag = false;
	$("#confirmallList").empty();
    $("input[name='"+id+"']").each(function(){ 
    if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
    	 var varll = $(this).attr('value');
    	 var varl = varll.split("_");
    	 if(varl[9] == "0"){
    		 tmp=tmp+varll.substring(0,varll.lastIndexOf("_"))+",";
    	 }
           //初始化已选列表
           if(varl[9] == "0"){
        	   var count = $(this).parent().next().text();
        	   count = "#moneyFlag"+count;
        	   //new Date(varl[8]).Format("yyyy-MM-dd hh:mm:ss");
        	   var dateStr = varl[8].substring(0,varl[8].length-2);
        	   var collumnStr = "<tr ><td class='tc'>"+dateStr+"</td><td class='tc'>"+varl[6]+"</td><td class='tc'>"+varl[7]+"</td><td class='tc payment'>"
        	   +$(count).text()+"<span name='payment'>"+parseFloat(varl[4]).toFixed(2)+"</span></td><td class='tc'>" +
					   " <input name='rateinput' class='rate inputTxt inputTxtlong' value='"+parseFloat(varl[3]).toFixed(4)+"'";
        	   if($(count).text() == "¥"){
        		   collumnStr = collumnStr+" readOnly='true'"
        	   }
        	   collumnStr = collumnStr+" onkeyup='checkNum(this)' onafterpaste='checkNum(this)' ><input type='hidden' value='"+varl[5]+"'/></td><td class='afterexchange tc'>￥<span name='afterexchange'>"+parseFloat(varl[3]*varl[4]).toFixed(2)+"</span></td>" +
					   "<td class='tc'>" +
					   "<input  name='confirmCashierDate' class='pay inputTxt dateinput' value='"+ new Date().Format("yyyy-MM-dd") +"' onClick='WdatePicker()'/></td></tr>";
        	   $("#confirmallList").append(collumnStr);
           }
           if($(this).parent().parent().find("font[class='noPay']").text()=='已付'){
        	   flag = true;
           } 
       }
    }); 
    if(flag){
    	top.$.jBox.tip('已有数据付款，请重新选择！');
    	return;
    }
    if(tmp==""){
    	top.$.jBox.tip('请选择数据！');
        return;
    } else {
        var $pop = $.jBox($("#payment_confirmall_pop_o").html(), {
            title    : "付款确认", buttons: {'确认': 1, '取消': 2}, submit: function (v, h, f) {
                if (v == "1") {
                	var submit = true;
                	var errMsg = "";
                	$pop.find('.rate').each(function(){
                		 if ($(this).val().trim() == "" && errMsg == "") {
                			 errMsg="汇率不能为空";
                             submit = false;
                             return false;
                         }
                	});
					$pop.find('.pay').each(function(){
						if ($(this).val().trim() == "" && errMsg == "") {
							errMsg="出纳时间为必填信息！";
							submit = false;
							return false;
						}
					});
                	$pop.find('.rate').each(function(){
                		if (($(this).val() == 0 || $(this).val().trim() == "0")&& errMsg == "" ){
                			 errMsg="汇率不能为0";
                			submit = false;
                			return false;
                		}
                	});
                    
                    //检查汇率是否合法
                    var rates = "";
                    var afterAmounts = "";
                    $("#jbox-content input[name='rateinput']").each(function(){
//                       if($(this).next().val() == 37 && $(this).val() != 1 &&  errMsg== ""){
//                    	    errMsg="人民币汇率默认为1，且不允许修改";
//                    	   submit = false;
//                           return false;
//                       }else{
                    	  rates =rates+ $(this).val()+"_";
                    	  afterAmounts = afterAmounts+$(this).parent().prev().children('span:first-child').text()*$(this).val()+"_";
//                       }
                    });
					var confirmCashierDates = "";
					$("#jbox-content input[name='confirmCashierDate']").each(function() {
						confirmCashierDates = confirmCashierDates + $(this).val() + "@";
					});
                    //提交
                    if(submit){
                    	$.ajax({
                    		type:"POST",
                    		url:ctx+"/orderCommon/manage/batchConfirmPay",
                    		data:{id:tmp,type:type,status:status,rates:rates,afterAmounts:afterAmounts,confirmCashierDates:confirmCashierDates},
                    		success:function(data){
                    			if('ok' == data.flag){
                    				$("#searchForm").submit();
                    			}else{
                    				
                    			}
                    		}
                    	});
                    }else{
                    	top.$.jBox.info(errMsg, "提示");
                    	return false;
                    }

                }
            }, height: '500', width: 700
        });
        //复制汇率
        $pop.find(".rateCopy").on("click", function () {
            var ratecopy = $pop.find("#rateCopy").val();
            if (ratecopy != "") {
//            	input[name='rateinput']:not([readOnly])
                $pop.find("input[name='rateinput']:not([readOnly])").val(ratecopy);
            }
            $("#jbox-content input[name='rateinput']").each(function(){
//            	if($(this).next().val() != 37){
            		var rateC = $(this).val();
            		var paymentC = $(this).parent().prev().children('span:first-child').text();
            		var afterexchangeC = paymentC * rateC;
            		$(this).parent().next().html("￥"+parseFloat(afterexchangeC).toFixed(2));
//            	}
             });

        });
		$pop.find(".dateCopy").on("click", function () {
			var confirmCashierDatee = $pop.find("#confirmCashierDatee").val();
			if (confirmCashierDatee != "") {
				$pop.find("input[name='confirmCashierDate']").val(confirmCashierDatee);
			}
		});

        $pop.find("input[name='rateinput']").keyup(function () {
        	var rate = $(this).val();
            var payment = $(this).parent().prev().children('span:first-child').text();
            var afterexchange = payment * rate;
            $(this).parent().next().html("￥"+parseFloat(afterexchange).toFixed(2));
        });
    }

}