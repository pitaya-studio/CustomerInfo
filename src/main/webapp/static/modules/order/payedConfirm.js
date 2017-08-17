//增加订单类型orderType参数20150616
function payedConfirm(ctx,orderid,agentid,orderType,obj){
	var ht = ($(window).height())*0.7;
	$.jBox("iframe:"+ctx+"/orderPay/getOrderPayInfo1?orderid="+orderid+"&agentid="+agentid+"&orderType="+orderType,{		
		title: "收款确认",
		width:830,
		height: ht,
		buttons:{'取消': 0,'确认收款':1},
		persistent:true,
		loaded: function (h) {},
		submit: function(v,h,f){
			$('.jbox-button[value=1]').attr('disabled','disabled');
			if(v==1){
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
				//添加海岛游支付uuid20150616
				var payProductOrderUuid = $(h.find("iframe")[0].contentWindow.payProductOrderUuid).val();
				//添加海岛游支付收款确认--确认收款按钮功能20150616
				var url = ctx + "/orderPay/confirmPayInfo";
				if(orderType > 10) {
					url = ctx + "/orderPay/confirmPayInfoForProduct";
				}
				//添加汇票支付方式的汇票到期日
				var draftAccountedDate = $(h.find("iframe")[0].contentWindow.draftAccountedDate).val();
				// 防重复提交 add by shijun.liu 2016.04.05
				var token = $(h.find("iframe")[0].contentWindow.token).val();
				// 添加因公支付宝支付
				var fromAlipayAccount = $(h.find("iframe")[0].contentWindow.fromAlipayAccount).val();//支付宝账号（来款）
				var fromAlipayName = $(h.find("iframe")[0].contentWindow.fromAlipayName).val();//支付宝名称（来款）
				var toAlipayName = $(h.find("iframe")[0].contentWindow.toAlipayName).val();//支付宝名称（收款）
				var toAlipayAccount = $(h.find("iframe")[0].contentWindow.toAlipayAccount).val();//支付宝账号（收款）
				var comeOfficeName = $(h.find("iframe")[0].contentWindow.comeOfficeName).val();//收款单位
				
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
					payProductOrderUuid:payProductOrderUuid,
					orderType:orderType,
					draftAccountedDate:draftAccountedDate,
					token:token,
					// invoiceDate:invoiceDate
					fromAlipayAccount:fromAlipayAccount,
					fromAlipayName:fromAlipayName,
					toAlipayName:toAlipayName,
					toAlipayAccount:toAlipayAccount,
					comeOfficeName:comeOfficeName
				}
				if(accountDate == ""){
					$("table a").each(function(){
						$(this).removeClass("disableCss");
					});
					$('.jbox-button[value=1]').removeAttr('disabled');
					$.jBox.tip('银行到帐日期不能为空','error');
					return false;
				}else{
					$.ajax({
						type:"POST",
						url:url,
						dataType:"json",
						data:dataparam,
						success:function(data){
							if('ok'==data.flag){
								$('#searchForm').submit();
							}else if('false'==data.flag){
								$.jBox.tip(data.msg,'error');
								$("table a").each(function(){
									$(this).removeClass("disableCss");
								});
							}
						}
					});
				}
			}
		}
	});
}

/**
 * author:wuqiang
 * 收款确认-驳回操作
 */
function payedConfirmReject(ctx,payId,orderId,ordertype,sign,payStatus,obj){
	//选择财务确认占位或已撤销占位的订单，在订单收款中，点击“驳回”，隐藏“驳回确认”弹出框，因为财务驳回时订单还没有占位扣位，不需要确认
	if(payStatus == 8 || payStatus == 9) {
		$.ajax({ 
	          type:"POST",
	          url:ctx+"/orderPay/getOrderPayForRejectTwo",
	          dataType:"json",
	          //data:dataparam,
	          data: {
		        	payId:payId,
		        	//payType:payType,
		        	rejectRadio:0,
		        	productOrderId:orderId,
		        	ordertype:ordertype,
		        	sign:sign
		        },
	          success:function(data){
	        	 // var rejectMark = data.rejectMark;
	        	 // if(null == rejectMark){
	        	//	  return false;
	        	 // }
	        	 // top.$.jBox.tip(data.rejectMark,'success');  
	              $('#searchForm').submit();
	          }
	      });
		return false;
		}
	$.jBox("iframe:"+ctx+"/orderPay/getOrderPayForRejectOne?payId="+payId+"&sign="+sign+"&orderType="+ordertype+"&orderId="+orderId,{
		    title: "驳回确认",
			width:500,
	   		height: 330,
	   		buttons:{'取消': 0,'确认':1},
	   		persistent:true,
	   		loaded: function (h) {},
	   		submit: function(v,h,f){
	   			if(v==1){
	   				$("table a").each(function(){
   						$(this).addClass("disableCss");
   				    });
	   				  var payType = $(h.find("iframe")[0].contentWindow.payType).val();
				      var orderPayId = $(h.find("iframe")[0].contentWindow.orderPayId).val();
				      
				      //0-保持占位，1-退回占位
				      var rejectRadio = $(h.find("iframe")[0].contentWindow.innerTable).find("[name=rejectRadio]:checked").val();
				      
				    //sign为驳回类型标识，0-订单收款驳回操作，1-签证押金收款驳回操作
				      if(sign == "0"){
				    	  if(null == rejectRadio){
					    	  top.$.jBox.tip("请选择驳回占位方式！",'success');  
					    	  return false;
					      }
				      }

					//驳回备注
					var reason = $(h.find("iframe")[0].contentWindow.reject).find("[name=reason]").val();

			      $.ajax({ 
			          type:"POST",
			          url:ctx+"/orderPay/getOrderPayForRejectTwo",
			          dataType:"json",
			          //data:dataparam,
			          data: {
				        	payId:orderPayId,
				        	payType:payType,
				        	rejectRadio:rejectRadio,
				        	productOrderId:orderId,
				        	ordertype:ordertype,
				        	sign:sign,
						  reason:reason
				        },
			          success:function(data){
			        	  var rejectMark = data.rejectMark;
			        	  if(null == rejectMark){
			        		  return false;
			        	  }
			        	  top.$.jBox.tip(data.rejectMark,'success');  
			              $('#searchForm').submit();
			             
			          }
			      });
	   			}
	   			
	   		}
	});
	$("#jbox-content").css("overflow-y","hidden");
}

/**
 * 团期收款确认
 */
function recepitsConfim(ctx,payGroupId,obj){
	var ht=($(window).height())*0.7;
	$.jBox("iframe:"+ctx+"/payGroup/receiptsMoney?payGroupId="+payGroupId,{		
	    title: "收款",
		width:830,
   		height: ht,
   		buttons:{'取消': 0,'确认收款':1},
   		persistent:true,
   		loaded: function (h) {
   			$(".jbox-content").css("overflow","hidden");
   		},
   		submit: function(v,h,f){
   			if(v==1){
   				$("table a").each(function(){
						$(this).addClass("disableCss");
				    });
   				//payGroupID
   				var payGroupId = $(h.find("iframe")[0].contentWindow.payGroupId).val();
   				//付款单位
			    var payerName =  $(h.find("iframe")[0].contentWindow.payerName).val();
			    //来款行名称
		        var bankName= $(h.find("iframe")[0].contentWindow.bankName).val();
		        //来款账户
		        var bankAccount =$(h.find("iframe")[0].contentWindow.bankAccount).val();
		        //支票号
		        var checkNumber =  $(h.find("iframe")[0].contentWindow.checkNumber).val();
		        //银行到账日期
		        var accountDate = $(h.find("iframe")[0].contentWindow.accountDate).val();
		        //备注
		        var remarks= $(h.find("iframe")[0].contentWindow.remarks).val();
		        //支付宝名称（来款）
		        var fromAlipayName = $(h.find("iframe")[0].contentWindow.fromAlipayName).val();
		        //支付宝账号（来款）
		        var fromAlipayAccount = $(h.find("iframe")[0].contentWindow.fromAlipayAccount).val();
		        //支付宝名称（收款）
		        var toAlipayName = $(h.find("iframe")[0].contentWindow.toAlipayName).val();
		        //支付宝账号（收款）
		        var toAlipayAccount = $(h.find("iframe")[0].contentWindow.toAlipayAccount).val();
		        //收款单位
		        var comeOfficeName = $(h.find("iframe")[0].contentWindow.comeOfficeName).val();
		        
		        dataparam={
		        		payGroupId:payGroupId,
		        		payerName:payerName,
					    bankName:bankName,
					    bankAccount:bankAccount,
					    checkNumber:checkNumber,
					    accountDate:accountDate,
					    remarks:remarks,
					    fromAlipayName:fromAlipayName,
					    fromAlipayAccount:fromAlipayAccount,
					    toAlipayName:toAlipayName,
					    toAlipayAccount:toAlipayAccount,
					    comeOfficeName:comeOfficeName
					}
		        
		      if(accountDate == ""){
		    	  $.jBox.tip('银行到帐日期不能为空','error');
		    	  return false;
		      }else{
		    	  $.ajax({ 
			          type:"POST",
			          url:ctx+"/payGroup/confirmRecepitsMoney",
			          dataType:"json",
			          data:dataparam,
			          success:function(data){
			        	if('success'==data.flag){
			              $(obj).parents('table').siblings('#searchForm').submit();
			        	}
			          }
			      });  
		      }
		     
   			}
   		}
    });
}

/**
 * 团期撤销收款
 */
function cancelRecepits(ctx,payGroupId,obj){
	var submit = function (v, h, f) {
	    if (v == 'ok') {
	    	$("table a").each(function(){
				$(this).addClass("disableCss");
		    });
	    	$.jBox.tip("正在撤销数据...", 'loading');
	    	$.ajax({ 
		          type:"POST",
		          url:ctx+"/payGroup/cancelRecepitsMoney",
		          dataType:"json",
		          data:{payGroupId:payGroupId},
		          success:function(data){
		        	if('success'==data.flag){
		        		$.jBox.tip('撤销成功', 'success');
		        		$(obj).parents('table').siblings('#searchForm').submit();
		        	}
		          }
		      });
	    }
	    return true; //close
	};
	$.jBox.confirm("确定要撤销该收款吗？", "提示", submit);
}

/**
 * 团期收款驳回 
 */
function recepitsReject(ctx,payGroupId,obj){
	var submit = function (v, h, f) {
		if (v == 'ok') {
			$.jBox.tip("正在驳回数据...", 'loading');
			var reason = f.reason;
			$.ajax({
				type:"POST",
				url:ctx+"/payGroup/rejectRecepitsMoney",
				dataType:"json",
				data:{payGroupId:payGroupId,reason:reason},
				success:function(data){
					if('success'==data.flag){
						$.jBox.tip('驳回成功', 'success');
						$(obj).parents('table').siblings('#searchForm').submit();
					}
				}
			});
		}
		return true; //close
	};
	//111 添加备注
	$.jBox.confirm('确定要驳回该收款吗？<form class="contactTable" style="margin-top:20px;margin-left:-25px"><div class="textarea pr wpr20"><label style="width: 57px;">备注：</label><textarea style="width:200px;resize: none;" id="reason" name="reason"  class="textarea_long" rows="3" cols="30" onkeyup="this.value=this.value.replaceForChar()" onafterpaste="this.value=this.value.replaceForChar()"></textarea></div></form>', "提示", submit);
}

String.prototype.replaceForChar = function (regEx) {
	if (!regEx) {
		regEx = /[\.\`\"\'\'\‘\”\“\’\<\>\\]/g;
	}
	return this.replace(regEx, '');

};
