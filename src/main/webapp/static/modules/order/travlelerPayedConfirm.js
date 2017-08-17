function payedConfirm(serialNum,travelerid){
	$.jBox("iframe:"+"/trekiz_wholesaler_tts/a/visaOrderPayLog/manage/orderPayView?serialNum="+serialNum+"&travelerid="+travelerid,{		
		    title: "收款确认",
			width: 900,
	   		height: 600,
	   		buttons:{'取消': 0,'确认收款':1},
	   		persistent:true,
	   		loaded: function (h) {},
	   		submit: function(v,h,f){
	   			if(v==1){
	   				   var payerName =  $(h.find("iframe")[0].contentWindow.payerName).val();
				     
				      var payType = $(h.find("iframe")[0].contentWindow.payType).val();
				      var bankName= $(h.find("iframe")[0].contentWindow.bankName).val();
				      var bankAccount =$(h.find("iframe")[0].contentWindow.bankAccount).val();
				      var toBankNname = $(h.find("iframe")[0].contentWindow.toBankName).val();		

				      var toBankAccount=$(h.find("iframe")[0].contentWindow.tobankAccount).val();
				      var orderPayId = $(h.find("iframe")[0].contentWindow.orderPayId).val();
				      var DocInfoIds = $(h.find("iframe")[0].contentWindow.DocInfoIds).parent().find("div[class=uploadPath] input[name=DocInfoIds]").val();
				      var checkNumber =  $(h.find("iframe")[0].contentWindow.checkNumber).val();
				      var remarks= $(h.find("iframe")[0].contentWindow.remarks).val();
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
										      remarks:remarks
										     // invoiceDate:invoiceDate
							     }
			            
			      
			      $.ajax({
			          type:"POST",
			          url:"/trekiz_wholesaler_tts/a/orderPay/confirmPayInfo",
			          dataType:"json",
			          data:dataparam,
			          success:function(data){
			              //window.close();
			          }
			      });
	   			}
	   			
	   		}
	});
}