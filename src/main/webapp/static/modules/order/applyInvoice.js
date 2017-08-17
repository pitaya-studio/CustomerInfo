var contextPath;
$(function(){
	contextPath = $("#ctx").val();
		$('#sea').on("click",'.btn-submit',function(){
		formSubmit();
		});
		$('#sea').on("click",'.btn-back',function(){
		formBack();
		});
	//各块信息展开与收起
	boxCloseOn();
	invoiceOrder();
	//滑过显示汇率
	exchangerateDiv();
	//开票总金额
	totalinvoice();
	//申请发票开票方式
	invoiceTypeChg();

    if("${param.saveinvoiceMSG}" =="1") {
		top.$.jBox.tip('操作已成功!','success');	
	}
    $(".qtip").tooltip({
        track: true
    });
    
    $(document).scrollLeft(0);
	$("#targetAreaId").val("${travelActivity.targetAreaIds}");
	$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
    
			
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

   //申请发票-0539所有产品线申请发票可以输入负值  modify by wangyang 2016.10.20
	function invoiceInputin(obj){
    	 var first = obj.value.charAt(0).replace(/[^\-\d\.]/,"");
    	 var ms = "";
    	 if(first == "-"){
    		 ms = first + obj.value.substring(1).replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
    	 }else{
    		 ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
    	 }
		
    	 // 只有当检测到不合法字符时才进行替换，因为实时替换会导致光标乱窜
		if (ms != obj.value) {
			var txt = ms.split(".");
			obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
		}
		 totalinvoice();
	}
   
   function totalinvoice(){
	   var total = 0;
	   var totalone = 0;
	   $(".invoicediv").each(function(index, element) {
	   	   var b=this.style.display;
	     if(b!="none"){
	     var invoiceIn=$(this).find(".invoicetd");
		    invoiceIn.each(function(index, element) {
			 var vals=$(this).val();
			if(vals==""){vals=0;}else{totalone += parseFloat(vals);}
        });
		total+=totalone;
		//$(this).find(".totalInvoiceOne").html(milliFormat(totalone,'1'));
		totalone=0;
		$(".totalinvoice").attr("value",milliFormat(total,'1'));
	     }
           
    }); 
   }
   
   function exchangerateDiv(){
	    $("td[name='exchangeratetd']").hover(function(){
			$(".exchangerate_mouse").show();
			$(this).css("cursor","pointer");
		    var left = $(this).offset().left;
		    var top = $(this).parents("tr").offset().top;
		    $(".exchangerate_mouse").css({"left":left+10,"top":top+30}).show();
		},function(){
			$(".exchangerate_mouse").hide();
		});  
	   }
//申请发票合开发票
//开票方式为合并类型时，显示添加合开订单按钮
function invoiceTypeChg(){
	 var value=$(".invoiceTypeChg option:selected").index();
	 //切换开票方式
//	 alert($(("input[name='totalInvoice']")).val());
	 $(".invoicetd").val("");
	 $(".totalinvoice").val("");
	 
	 $(".invoicemain").each(function(index, element) {
      if(value==index){
		  $(this).show();
		  }else{
			  $(this).hide();
			  }
    });
             
	}     
//删除合开订单
function invoiceOrderDel(obj){
	$(obj).parent().parent().remove();
	totalinvoice();
}
//各块信息展开与收起
function  boxCloseOn(){
$(".closeOrExpand").click(function(){
	var obj_this = $(this);
	if(obj_this.attr("class").match("ydClose")) {
		obj_this.removeClass("ydClose");
		obj_this.parent().nextAll("[flag=messageDiv]").eq(0).show();
	} else {
		obj_this.addClass("ydClose");
		obj_this.parent().nextAll("[flag=messageDiv]").eq(0).hide();
	}
});
}

// 0539所有产品线申请发票可以输入负值  modify by wangyang 2016.10.20
function refundInputs(obj){
   objs=obj.value;
   if(objs.charAt(0)=="-"){
	   objs=objs.replace(/^(\-\d*)$/,"$1.");
   }else{
	   objs=objs.replace(/^(\d*)$/,"$1.");
   }
   objs=(objs+"00").replace(/(\d*\.\d\d)\d*/,"$1");
   objs= objs.replace(/^\./,"0.");
   $(obj).val(objs)
   $(obj).next("span").hide()
   
   }

//添加合开订单
function invoiceOrder(){
		$('.invoiceAdd').delegate(".ydbz_x","click",function(){
			    var obj=$(this);
				var html = '<div class="add_allactivity"><label>订单号：</label>';
	html += '<input type="text" class="invoiceOrderInput"/>';
	html += '</div>';
	$.jBox(html, { title: "添加合开订单", submit:function(v, h, f){
		if(v=='ok'){
			var values=h.find('.invoiceOrderInput').val();
			if(values==""){
				 h.find('.nothisPro').hide('fast', function () { $(this).remove(); });
			     //如果没订单
			     $('<p class="nothisPro" style="display: none;">没有这个订单</p>').appendTo(h).show('slow');
			     return false;
				}else{
				var checkFlag=0;
				var ordersId=$(obj).parent().parent().find("input[name='myOrderNum']");
	                    ordersId.each(function(index, element) {
					        var si = $(ordersId[index]);
        					var s = si.val();
        					if(s!=undefined && s!=""){         					
        						if(values==s){		    
			     					h.find('.nothisPro').hide('fast', function () { $(this).remove(); });
			     					//如果订单已存在
			     					top.$.jBox.tip('订单已存在！', 'error', { focusId: 'name' });
			     					checkFlag=1;
			   						return false;
        						}
        					}
					    });
				if(checkFlag==0){
					$.ajax({
	                type: "POST",
	                url: contextPath+"/orderInvoice/manage/getOrderOpenInvoiceInfo",
	                data: {
	                
	                    orderNum:values,
	                    salerId:$("#salerId").val(),
	                    orderType:"${orderType}"
	                },
	                success: function(msg){
	                    if(msg==null||msg==undefined ||msg==""){
	                    	h.find('.nothisPro').hide('fast', function () { $(this).remove(); });
						     //如果没订单
						     top.$.jBox.tip('订单号错误！', 'error', { focusId: 'name' });
	                    }else{
	                    //根据订单内容加载信息
	                    if(null == msg.errorMessage || undefined == msg.errorMessage || "" == msg.errorMessage){
	                    	addOrderInvoiceInfo(obj,msg);
	                    }else{
	                    	top.$.jBox.tip(msg.errorMessage, 'error', { focusId: 'name' });
	                    }
	                    
	                    	                    	                    
		                    }
		                }
		            });
				
				}
					
				
				
					
					}
			}	
	},height:180,width:500});
		
	
		});
	 }
function addOrderInvoiceInfo(obj,msg){
	if(msg.orderType =='6'){
		addVisaOrderInvoiceInfo(obj,msg);
	}else if(msg.orderType =='7'){
		addAirOrderInvoiceInfo(obj,msg);
	}else{
		addSingleOrderInvoiceInfo(obj,msg)
	}
	
}
function addSingleOrderInvoiceInfo(obj,msg){
	//拼接添加订单显示信息
	var html='<div class="invoicediv">';
                        html+='<div class="ydbz_tit orderdetails_titpr">订单<a class="ydbz_x" onclick="invoiceOrderDel(this)">删除</a></div>';
                        html+='<table border="0"  width="100%" style="margin-bottom:10px"';
                        html+=' <tbody>';
                        html+=' <tr>';
                        html+='  <td class="mod_details2_d1_five">团号：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.groupCode+'</td>';
                        html+=' <td class="mod_details2_d1_five">出团日期：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.groupOpenDate+'</td>';
                        html+=' <td class="mod_details2_d1_five">订单号：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.orderNum+'</td>';
                        html+='<td class="mod_details2_d1_five">预定日期：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.orderTime+'</td>';
                        html+='<td class="mod_details2_d1_five">订单类型：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.orderTypeName+'</td>';
                        html+='</tr>';
                        html+='<tr>';
                        html+='<td class="mod_details2_d1_five">人数：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.orderPersonNum+'</td>';
                        html+='<td class="mod_details2_d1_five">应收金额：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.orderyTotal+'</td>';
                        html+=' <td class="mod_details2_d1_five">财务到账：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.totalAsAcount+'</td>';
                        html+='<td class="mod_details2_d1_five">已开发票：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.invoiceAmount+'</td>';
                        html+='</tr>';
                        html+=' </tbody>';
                        html+='</table>';
                        html+='<input type="hidden" name="myGroupCode" value="'+msg.groupCode+'" >';
                        html+='<input type="hidden" name="myOrderId" value="'+msg.id+'" >';
                        html+='<input type="hidden" name="myOrderNum" value="'+msg.orderNum+'" >';
                        html+='<input type="hidden" name="myOrderType" value="'+msg.orderType+'" >';
                        html+='<table class="table table-striped table-bordered">';
                        html+='<thead>';
                        html+=' <tr>';
                        html+='<th width="10%">付款金额</th>';
                        html+='<th width="10%">达账金额</th>';
                        html+='<th width="10%">退款金额</th>';
                        html+='<th width="10%">已开票金额</th>';
                        html+='<th width="10%">可开票金额</th>';
                        html+='<th width="10%">本次开票金额</th>';
                        html+='</tr>';
                        html+=' </thead>';
                        html+='<tbody>';
                        html+=' <tr>';
                        html+=' <td class="tr">'+msg.alreadyPaid+'</td>';
                        html+='<td class="tr " name="exchangeratetd">'+msg.totalAsAcount+'</td>';
                        html+=' <td class="tr">'+msg.refundTotalStr+'</td>';
                        html+=' <td class="tr">'+msg.invoiceAmount+'</td>';
                        html+='<td class="tr">'+msg.refundableAmount+'</td>';
                        html+='<td class="tc"><input type="text" name="orderInvoiceAmount" maxlength="8" class="rmb invoicetd" onblur="refundInputs(this)" onafterpaste="invoiceInputin(this)" onkeyup="invoiceInputin(this)"></td>';
                        html+='</tr>';
                        html+='</tbody>';
                        html+=' </table>';
                        html+=' </div>';
	
	$(obj).parent().before(html);
}


//验证前台页面提交数据  
function formCheck(){

	return true;
}
function addAirOrderInvoiceInfo(obj,msg){
	//拼接添加订单显示信息
	var html='<div class="invoicediv">';
                        html+='<div class="ydbz_tit orderdetails_titpr">订单<a class="ydbz_x" onclick="invoiceOrderDel(this)">删除</a></div>';
                        html+='<table border="0"  width="100%" style="margin-bottom:10px"';
                        html+=' <tbody>';
                        html+=' <tr>';
                        html+='  <td class="mod_details2_d1_five">团号：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.groupCode+'</td>';
                        html+=' <td class="mod_details2_d1_five">乘机日期：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.startTime+'</td>';
                        html+=' <td class="mod_details2_d1_five">订单号：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.orderNum+'</td>';
                        html+='<td class="mod_details2_d1_five">预定日期：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.createDate+'</td>';
                        html+='<td class="mod_details2_d1_five">订单类型：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.orderTypeName+'</td>';
                        html+='</tr>';
                        html+='<tr>';
                        html+='<td class="mod_details2_d1_five">人数：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.person_num+'</td>';
                        html+='<td class="mod_details2_d1_five">应收金额：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.orderyTotal+'</td>';
                        html+=' <td class="mod_details2_d1_five">财务到账：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.totalAsAcount+'</td>';
                        html+='<td class="mod_details2_d1_five">已开发票：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.invoiceAmount+'</td>';
                        html+='</tr>';
                        html+=' </tbody>';
                        html+='</table>';
                        html+='<input type="hidden" name="myGroupCode" value="'+msg.groupCode+'" >';
                        html+='<input type="hidden" name="myOrderId" value="'+msg.id+'" >';
                        html+='<input type="hidden" name="myOrderNum" value="'+msg.orderNum+'" >';
                        html+='<input type="hidden" name="myOrderType" value="'+msg.orderType+'" >';
                        html+='<table class="table table-striped table-bordered">';
                        html+='<thead>';
                        html+=' <tr>';
                        html+='<th width="10%">付款金额</th>';
                        html+='<th width="10%">达账金额</th>';
                        html+='<th width="10%">退款金额</th>';
                        html+='<th width="10%">已开票金额</th>';
                        html+='<th width="10%">可开票金额</th>';
                        html+='<th width="10%">本次开票金额</th>';
                        html+='</tr>';
                        html+=' </thead>';
                        html+='<tbody>';
                        html+=' <tr>';
                        html+=' <td class="tr">'+msg.alreadyPaid+'</td>';
                        html+='<td class="tr " name="exchangeratetd">'+msg.totalAsAcount+'</td>';
                        html+=' <td class="tr">'+msg.refundTotalStr+'</td>';
                        html+=' <td class="tr">'+msg.invoiceAmount+'</td>';
                        html+='<td class="tr">'+msg.refundableAmount+'</td>';
                        html+='<td class="tc"><input type="text" name="orderInvoiceAmount" class="rmb invoicetd" maxlength="8" onblur="refundInputs(this)" onafterpaste="invoiceInputin(this)" onkeyup="invoiceInputin(this)"></td>';
                        html+='</tr>';
                        html+='</tbody>';
                        html+=' </table>';
                        html+=' </div>';
	
	$(obj).parent().before(html);
}

function addVisaOrderInvoiceInfo(obj,msg){
	//拼接添加订单显示信息
	var html='<div class="invoicediv">';
                        html+='<div class="ydbz_tit orderdetails_titpr">订单<a class="ydbz_x" onclick="invoiceOrderDel(this)">删除</a></div>';
                        html+='<table border="0"  width="100%" style="margin-bottom:10px"';
                        html+=' <tbody>';
                        html+=' <tr>';
                        html+='  <td class="mod_details2_d1_five">团号：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.groupCode+'</td>';
                        html+=' <td class="mod_details2_d1_five">订单号：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.orderNum+'</td>';
                        html+='<td class="mod_details2_d1_five">预定日期：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.createDate+'</td>';
                        html+='<td class="mod_details2_d1_five">订单类型：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.orderTypeName+'</td>';
                        html+='</tr>';
                        html+='<tr>';
                        html+='<td class="mod_details2_d1_five">人数：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.person_num+'</td>';
                        html+='<td class="mod_details2_d1_five">应收金额：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.orderyTotal+'</td>';
                        html+=' <td class="mod_details2_d1_five">财务到账：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.totalAsAcount+'</td>';
                        html+='<td class="mod_details2_d1_five">已开发票：</td>';
                        html+='<td class="mod_details2_d2_five">'+msg.invoiceAmount+'</td>';
                        html+='</tr>';
                        html+=' </tbody>';
                        html+='</table>';
                        html+='<input type="hidden" name="myGroupCode" value="'+msg.groupCode+'" >';
                        html+='<input type="hidden" name="myOrderId" value="'+msg.id+'" >';
                        html+='<input type="hidden" name="myOrderNum" value="'+msg.orderNum+'" >';
                        html+='<input type="hidden" name="myOrderType" value="'+msg.orderType+'" >';
                        html+='<table class="table table-striped table-bordered">';
                        html+='<thead>';
                        html+=' <tr>';
                        html+='<th width="10%">付款金额</th>';
                        html+='<th width="10%">达账金额</th>';
                        html+='<th width="10%">退款金额</th>';
                        html+='<th width="10%">已开票金额</th>';
                        html+='<th width="10%">可开票金额</th>';
                        html+='<th width="10%">本次开票金额</th>';
                        html+='</tr>';
                        html+=' </thead>';
                        html+='<tbody>';
                        html+=' <tr>';
                        html+=' <td class="tr">'+msg.alreadyPaid+'</td>';
                        html+='<td class="tr " name="exchangeratetd">'+msg.totalAsAcount+'</td>';
                        html+=' <td class="tr">'+msg.refundTotalStr+'</td>';
                        html+=' <td class="tr">'+msg.invoiceAmount+'</td>';
                        html+='<td class="tr">'+msg.refundableAmount+'</td>';
                        html+='<td class="tc"><input type="text" name="orderInvoiceAmount" class="rmb invoicetd" maxlength="8" onblur="refundInputs(this)" onafterpaste="invoiceInputin(this)" onkeyup="invoiceInputin(this)"></td>';
                        html+='</tr>';
                        html+='</tbody>';
                        html+=' </table>';
                        html+=' </div>';
	
	$(obj).parent().before(html);
}