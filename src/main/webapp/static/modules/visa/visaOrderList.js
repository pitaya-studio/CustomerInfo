//订单-签务身份订单-修改押金
function jbox_xgyj_qianwu(visaId,uuid,travelerId,orderId,yingshouId) {
	$.ajax({
			type: "POST",
			url: g_context_url+"/visa/order/searchDeposit",
			data:{
             	visaId:visaId
                  },
			success: function(msg){
				if(msg.data != "true"){
				top.$.jBox.tip("未设置押金,不能进行修改");
					}else{
			
				var fuhao = msg.currency_mark;
 					var html = '<div style="margin-top:20px; padding-left:20px;">';
						html += '<label class="jbox-label">'+msg.currency_name+'</label><input id ="searchTotalDeposit" type="text" class="jbox-width100" value="'+msg.amount +'"> ';
						html += '</div>';
						$.jBox(html, { title: "修改押金金额",buttons:{'确定': 1,'取消':0}, submit:function(v, h, f){
							if (v=="1"){
								
								var totalDeposit = document.getElementById("searchTotalDeposit").value;
								totalDeposit = formatnumber(totalDeposit,2);
								
								 if (!totalDeposit) {
						            top.$.jBox.tip('不能为空');
						            return false;
						        } else if (isNaN(totalDeposit)) {
						            top.$.jBox.tip('包含非数字');
						            return false;
						        }
								
								$.ajax({
									type: "POST",
									url: g_context_url+"/visa/order/updateDeposit",
									data:{
										visaId:visaId,
						             	totalDeposit:totalDeposit,
						             	uuid:uuid
						                  },
									success: function(msg){
										document.getElementById("traveleryingshouyajin_"+travelerId).innerHTML =  fuhao+" "+totalDeposit;																
						 				top.$.jBox.tip(msg.message);
						 				}
									});
							}
						},height:150,width:380});
						}
 				}
			});
}


//订单-签务身份订单-撤销押金
function jbox_cxyj_qianwu(visaId,uuid,travelerId,orderId) {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<p style="padding:20px; line-height:180%;">此游客正在押金转担保审核中，修改押金金额将撤销申请，是否继续修改押金？';
	html += '</div>';
	$.jBox(html, { title: "撤销押金转担保申请？",buttons:{'确定': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
		
		//点击确认后,执行取消押金转担保的操作
			$.ajax({
			type: "POST",
			url: g_context_url+"/visa/order/cancelApply",
			data:{
             	visaId:visaId,
             	travelerId:travelerId,
             	orderId:orderId
                  },
			success: function(msg){
				top.$.jBox.tip(msg.message);
				if("押金转担保取消成功" ==msg.message){
					jbox_xgyj_qianwu(visaId,uuid,travelerId,orderId);
				}
			}
			});
		
			return true;
		}
	},height:220,width:380});

	
}
//更新游客信息
function updateTraveler_qianwu(orderStatus,AAid,visaStatusId,passstatusId,guaranteeStatusId,startOutId,contractId,travelerId,visaId,deliveryTimeId,orderId){
	//var aa = document.getElementById(AAid).value;
	
 	var visaStatus = document.getElementById(visaStatusId).value;
 	var passstatus = document.getElementById(passstatusId).value;
 	var guaranteeStatus = document.getElementById(guaranteeStatusId).value;
 	var startOut = document.getElementById(startOutId).value;
 	var contract = document.getElementById(contractId).value;
 	var deliveryTime = document.getElementById(deliveryTimeId).value;
 	if("1" == orderStatus){
 		top.$.jBox.tip("此订单被锁定,不能修改游客信息");
		   return;
 	}
	$.ajax({
		type: "POST",
		url: g_context_url+"/guaranteeMod/isReviewing",
		data:{
			travelerId:travelerId,
			orderId:orderId
		},
		success: function(msg){
			if("yes" == msg){
				top.$.jBox.tip("此游客正在担保变更审批中，无法变更担保类型");
				return false;
			}else {
				$.ajax({
					type: "POST",
					url: g_context_url+"/visa/order/updateTraveler",
					data:{
						//aa:aa,
						visaStatus:visaStatus,
						passstatus:passstatus,
						guaranteeStatus:guaranteeStatus,
						travelerId:travelerId,
						visaId:visaId ,
						startOut:startOut,
						contract:contract,
						deliveryTime:deliveryTime
					},
					success: function(msg){
						if('guarantee_type' == guaranteeStatus && '3' == guaranteeStatus){
						}else{
							top.$.jBox.tip(msg.message);
						}
						$("#hidden_passportStatus_"+travelerId).val(passstatus);

					}
				});
			}
		}
	});
}


function updateTraveler_qianwu1(orderStatus,AAid,visaStatusId,passstatusId,guaranteeStatusId,startOutId,contractId,travelerId,visaId,orderId){
 	var visaStatus = document.getElementById(visaStatusId).value;
 	var passstatus = document.getElementById(passstatusId).value;
 	var guaranteeStatus = document.getElementById(guaranteeStatusId).value;
 	var startOut = document.getElementById(startOutId).value;
 	var contract = document.getElementById(contractId).value;
 	if("1" == orderStatus){
 		top.$.jBox.tip("此订单被锁定,不能修改游客信息");
		   return;
 	}
    $.ajax({
        type: "POST",
        url: g_context_url + "/guaranteeMod/isReviewing",
        data: {
            travelerId: travelerId,
			orderId:orderId
        },
        success: function (msg) {
            if ("yes" == msg) {
                top.$.jBox.tip("此游客正在担保变更审批中，无法变更担保类型");
                return false;
            } else {
                $.ajax({
                    type: "POST",
                    url: g_context_url+"/visa/order/updateTraveler",
                    data:{
                        visaStatus:visaStatus,
                        passstatus:passstatus,
                        guaranteeStatus:guaranteeStatus,
                        travelerId:travelerId,
                        visaId:visaId ,
                        startOut:startOut,
                        contract:contract
                    },
                    success: function(msg){
                        if('guarantee_type' == guaranteeStatus && '3' == guaranteeStatus){
                        }else{
                            top.$.jBox.tip(msg.message);
                        }
                        $("#hidden_passportStatus_"+travelerId).val(passstatus);

                    }
                });
            }
        }
    });
}

/**
 * 必须为数字或小数点的校验可带负号
 */ 
function isNumber4JK(oNum){
		if(!oNum) return false;
		var strP=/^(-)?\d+(\.\d+)?$/;
		if(!strP.test(oNum)) return false;
		try{
			if(parseFloat(oNum)!=oNum) return false;
		}catch(ex){
		    return false;
		}
		return true;
} 

/**
 * 必须为数字或小数点的校验
 */ 
function isNumber(oNum){
		if(!oNum) return false;
		var strP=/^\d+(\.\d+)?$/;
		if(!strP.test(oNum)) return false;
		try{
			if(parseFloat(oNum)!=oNum) return false;
		}catch(ex){
		    return false;
		}
		return true;
} 


//订单-签务身份订单-借款-----------wxw added---------------
//v:buttons的值，h为该jBox对象的属性和方法，f为html中的个输入框的值
function jbox_jk_qianwu(travelerID,obj) {
	var html = '<div style="margin-top:20px;"><label class="jbox-label">借款金额：</label>';
	html += '<input name="borrowAmount" type="text" /><br /><label class="jbox-label">借款原因：</label><textarea name="borrowRemark" cols="" rows="" ></textarea><br /><label class="jbox-label">借款日期：</label><input name="borrowTime" onclick="WdatePicker()" class="inputTxt dateinput"  type="text" />';
	html += '</div>';
	
	$.jBox(html, { title: "借款",buttons:{'申请借款': true}, submit:function(v, h, f){
		
		  if(!f.borrowAmount){
			   top.$.jBox.tip("借款金额为必填项！");
			   return false;
		   }
		   
		   var text1=(f.borrowRemark).replace( /^\s+/, "" ).replace( /\s+$/, "" ); 
		   if(!f.borrowRemark||text1.length<1){
			   top.$.jBox.tip("借款原因为必填项！");
			   return false;
		   }
		   if(isNumber4JK(f.borrowAmount)){
				$.ajax({
					  type: "POST",
					  url:  g_context_url+"/visa/workflow/borrowmoney/createVisaJK",
					  dataType: "json",
					  data : {
							"travelerID" : travelerID,
			                "borrowAmount" : f.borrowAmount,
			                "borrowRemark" : f.borrowRemark,
			                "borrowTime" : f.borrowTime},
					  async: false,
					  success:function(msg){
						  if(obj){
							  $(obj).parent().parent().parent().parent().parent().children().eq(13).html(msg.borrowStatus);
						  }
						  top.$.jBox.tip(msg.visaJKreply);
					  }
					});
		   }else{
			  top.$.jBox.tip("借款金额必须为数字！");
			   return false;
		   }
	},height:260,width:380});
} 


//订单-签务身份订单-借款-----------wxw added---------------
//v:buttons的值，h为该jBox对象的属性和方法，f为html中的个输入框的值
function jbox_jk_qianwu_new(travelerID,obj) {
	var html = '<div style="margin-top:20px;"><label class="jbox-label">借款金额：</label>';
	html += '<input name="borrowAmount" type="text" /><br /><label class="jbox-label">借款原因：</label><textarea name="borrowRemark" cols="" rows="" ></textarea><br /><label class="jbox-label">借款日期：</label><input name="borrowTime" onclick="WdatePicker()" class="inputTxt dateinput"  type="text" />';
	html += '</div>';
	
	$.jBox(html, { title: "借款",buttons:{'申请借款': true}, submit:function(v, h, f){
		
		  if(!f.borrowAmount){
			   top.$.jBox.tip("借款金额为必填项！");
			   return false;
		   }
		   
		   var text1=(f.borrowRemark).replace( /^\s+/, "" ).replace( /\s+$/, "" ); 
		   if(!f.borrowRemark||text1.length<1){
			   top.$.jBox.tip("借款原因为必填项！");
			   return false;
		   }
		
		   if( isNumber(f.borrowAmount)){
				$.ajax({
					  type: "POST",
					  url:  g_context_url+"/visa/workflow/borrowmoney/createVisaJK",
					  dataType: "json",
					  data : {
							"travelerID" : travelerID,
			                "borrowAmount" : f.borrowAmount,
			                "borrowRemark" : f.borrowRemark,
			                "borrowTime" : f.borrowTime},
					  async: false,
					  success:function(msg){
					  	obj.parent().parent().parent().parent().parent().children().eq(11).html(msg.borrowStatus)
						  top.$.jBox.tip(msg.visaJKreply);
					  }
					});
		   }else{
			   top.$.jBox.tip("借款金额必须为数字！");
			   return false;
		   }
	},height:260,width:380});
}

//订单-签务身份订单-办签资料
function jbox_qszl_qianwu(visaProductId,visaId) {
 	$.ajax({
				type: "POST",
				url: g_context_url+"/visa/order/searchDatum",
				 data:{
                         visaProductId:visaProductId,
                         visaId:visaId
                     },
				success: function(msg){
				
				var html = '<div style="margin-top:20px; padding-right:20px;" class="jbox_qszl">';
				html += '<div><label>资料原件：</label>';
				
				var html_yuanjian ='';
				for(var i=0;i<msg.yuanjian.length;i++){
					html_yuanjian +='<span>';
					if(msg.yuanjian[i].signFlag == "1"){
						html_yuanjian +='<input name="yuanjian" checked="true" type="checkbox" value="'+msg.yuanjian[i].id+'"  />'+msg.yuanjian[i].name;
					}else{
						html_yuanjian +='<input name="yuanjian" type="checkbox" value="'+msg.yuanjian[i].id+'" />'+msg.yuanjian[i].name;
					}
					html_yuanjian +='</span>';
				}
					html_yuanjian +='</div>';
					html =html +html_yuanjian;
					var html_fuyinjian ='';
					if(msg.fuyinjian.length>0) {
					html_fuyinjian +='<div><label>复印件：</label>';
					for(var i=0;i<msg.fuyinjian.length;i++){
						html_fuyinjian +='<span>';
						if('1' == msg.fuyinjian[i].signFlag){
							html_fuyinjian +='<input name="fuyinjian" checked="true" type="checkbox" value="'+msg.fuyinjian[i].id+'" style="margin:0; margin-right:5px;"/>'+msg.fuyinjian[i].name;
						}else{
							html_fuyinjian +='<input name="fuyinjian" type="checkbox" value="'+msg.fuyinjian[i].id+'" style="margin:0; margin-right:5px;"/>'+msg.fuyinjian[i].name;
						}
						html_fuyinjian +='</span>';
					}
					html_fuyinjian += '</div>';
					}
					html_fuyinjian += '</div>';
					html = html+html_fuyinjian;
				$.jBox(html, { title: "办签资料",buttons:{'确定': 1}, submit:function(v, h, f){
				 	if (v=="1"){
				 		
				 		var yuanjian_value="";
				 		if(msg.yuanjian.length>0) {
				 		var str=document.getElementsByName("yuanjian");
				 		var objarray=str.length;
				 		for (i=0;i<objarray;i++)
				 		{
				 		  if(str[i].checked == true)
				 		  {
				 			 yuanjian_value+=str[i].value+",";
				 		  }
				 		}
				 		}
				 		
				 		var fuyinjian_value="";
				 		if(msg.fuyinjian.length>0) {
				 		var str2=document.getElementsByName("fuyinjian");
				 		var objarray2=str2.length;
				 		for (i=0;i<objarray2;i++)//复印件对应的数组长度,应当为objarray2-tgy,这是造成弹窗签收复印件设置失败的原因
				 		{
				 		  if(str2[i].checked == true)
				 		  {
				 			 fuyinjian_value+=str2[i].value+",";
				 		  }
				 		}
				 		}
				 		
						 $.ajax({
							type: "POST",
							url: g_context_url+"/visa/order/signDatum",
							 data:{
							 		visaId:visaId,
							 		yuanValue:yuanjian_value,
							 		fuyinValue:fuyinjian_value
			                     },
							success: function(msg){
			 				top.$.jBox.tip(msg.message);
			 				}
						});
						 return true;
					}
				 },height:220,width:400});
 				}
			});
}
//修改押金
function xiugaiyajin_qianwu(visaId,uuid,travelerId,orderId,yingshouId){
    $.ajax({
        type: "POST",
        url: g_context_url + "/guaranteeMod/isReviewing",
        data: {
            travelerId: travelerId,
			orderId:orderId
        },
        success: function (msg) {
            if ("yes" == msg) {
                top.$.jBox.tip("此游客正在担保变更审批中，无法修改押金");
                return false;
            } else {
                $.ajax({
                    type: "POST",
                    url: g_context_url+"/visa/order/searchApply",
                    data:{
                        visaId:visaId,
                        travelerId:travelerId,
                        orderId:orderId
                    },
                    success: function(msg){
                        if("show" == msg.message){
                            jbox_cxyj_qianwu(visaId,uuid,travelerId,orderId,yingshouId);
                        }else if("no" == msg.message){
                            jbox_xgyj_qianwu(visaId,uuid,travelerId,orderId,yingshouId);
                        }else{
                            top.$.jBox.tip(msg.message);
                        }
                    }
                });
            }
        }
    });


}
//订单-签务身份订单-借款明细
function jbox_jkmx_qianwu(travelerName,passportNo,orderCreateUser,createTime,groupType,shoukeUser,orderNo,jiekuanCreateUser,amount,remarks) {
	var html = '<div style="margin-top:20px;text-align:center;">';
	html += '<p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">';
	html += '姓名:'+travelerName;
	html += '</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">';
	html += '护照号:'+passportNo;
	html += '</span></p><p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">';
	html += '下单人:'+orderCreateUser;
	html += '</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">';
	html += '下单时间:'+createTime;
	html += '</span></p><p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">';
	html += '团队类型:'+groupType;
	html += '</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">';
	html += '收客人:'+shoukeUser;
	html += '</span></p><p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">';
	html += '订单编号:'+orderNo;
	html += '</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">';
	html += '操作人:'+jiekuanCreateUser;
	html += '</span></p><p><span style="width:90%; display:inline-block; text-align:left;overflow:hidden; height:28px;">';
	html += '借款金额:'+amount;
	html += '</span></p><p ><span style="width:90%; display:inline-block; text-align:left;overflow:hidden; height:auto;">';
	html += '备注:'+remarks;
	html += '</span></p></div>';
	$.jBox(html, { title: "借款明细",buttons:{'关闭': true}, submit:function(v, h, f){
		if (v==true){
			
			 return true;
		}
	},height:330,width:550});				
}
function xuanze(visaId,travelerId,id,yingshouId,orderId){
	var value = document.getElementById(id).value;
	$.ajax({
		type: "POST",
		url: g_context_url+"/guaranteeMod/isReviewing",
		data:{
			travelerId:travelerId,
			orderId:orderId
		},
		success: function(msg){
			if("yes" == msg){
				top.$.jBox.tip("此游客正在担保变更审批中，无法变更担保类型");
				return false;
			}else if("3"== value || "2"== value){
				jbox_jyj_qianwu(visaId,travelerId,yingshouId,value);
			}
		}
	});
	//else{
		//document.getElementById(yingshouId).innerHTML = "";
	//}
}
//还签证收据单个游客-----------wxw added----------------
//2015-03-30 添加单个还收据时的备注信息
function jbox_hsj_qianwu(travelerID) {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<label class="jbox-label">收据金额：</label><input name="receiptAmount" type="text" /><br /><label class="jbox-label">还领取人：</label><input name="receiptor" type="text" /><br /><label class="jbox-label">归还时间：</label><input name="returnTime" onclick="WdatePicker()" class="inputTxt dateinput"  type="text" /><br/><label class="jbox-label">备注：</label><input name="returnReceiptRemark" type="text" />';
	html += '</div>';
	$.jBox(html, { title: "还收据",buttons:{'还收据': 1}, submit:function(v, h, f){
		
		  if(!f.receiptAmount){
			   top.$.jBox.tip("收据金额为必填项！");
			   return false;
		   }
		  
		   var text1=(f.receiptor).replace( /^\s+/, "" ).replace( /\s+$/, "" ); 
		   if(!f.receiptor||text1.length<1){
			   top.$.jBox.tip("还领取人为必填项！");
			   return false;
		   }
		   
		   if(!f.returnTime){
			   top.$.jBox.tip("归还时间为必填项！");
			   return false;
		   }
		
		   if( isNumber(f.receiptAmount)){
			    $.ajax({
					  type: "POST",
					  url:  g_context_url+"/visa/workflow/returnreceipt/createVisaHSJ",
					  dataType: "json",
					  data : {
							"travelerID" : travelerID,
			                "receiptAmount" : f.receiptAmount,
			                "receiptor" : f.receiptor,
			                "returnTime" : f.returnTime,
			                "returnReceiptRemark" : f.returnReceiptRemark},
					  async: false,
					  success:function(msg){
						    top.$.jBox.tip(msg.visaHSJreply);
					  }
					});
		   }else{
			   top.$.jBox.tip("收据金额必须为数字或小数！");
			   return false;
		   }
	},height:260,width:380});
}

/**
 * 按部门查询订单
 * 
 * param departmentId
 */
function getDepartment(departmentId) {
	$("#departmentId").val(departmentId);
	$("#searchForm").submit();
}

/**
 * 200 渠道联系人显示20个字符，其余的用"..."代替
 */
function sliceAgentContacts(){
	$("input[class=agentContactsName]").each(function(index) {
		var agentContactsName = $(this).val();
		$(this).siblings().attr("title", agentContactsName);
		if(agentContactsName!=null && agentContactsName!=undefined && agentContactsName.length>=20){
			agentContactsName = agentContactsName.substring(0,20) + "...";
		}
		$(this).siblings().html(agentContactsName);
	});
}

/*  批量下载签证类确认单
 * @param checkName checkbox标签的name属性值，用于获取checkbox标签
 * add by xianglei.dong  2016-03-23
 * updated by xianglei.dong 2016-03-30
 */
function visa_orderBatchDownload(checkName){
	var check = document.getElementsByName(checkName);
	var objarray = check.length;
	var orderIds = "";
	var docIds = "";
	//记录选中的个数
	var checkedCount = 0;
	for (var i=0; i<objarray; i++) {
	  if(check[i].checked == true) {
		  checkedCount++;
		  var order_doc_id = check[i].value.split("@");
		  if(order_doc_id[1]=='' || order_doc_id[1]==null || order_doc_id[1]==undefined) {
			  continue;
		  }
		  orderIds = orderIds + "," + order_doc_id[0];
		  docIds = docIds + "," + order_doc_id[1];
	  }
	}
	
	if(checkedCount ==0){
		top.$.jBox.tip('请选择订单');
        return false;
	}else if(docIds == '') {
		top.$.jBox.tip('选择的订单中没有可下载的确认单，请先上传确认单');
		return false;
	}else{
		docIds = docIds.substr(1, docIds.length);
		window.open(encodeURI(encodeURI(g_context_url+"/visa/order/zipconfirmdownload/" + docIds )));
		location.reload(true);
	}		
}

/* 签证类订单列表全选
 * @param obj 全选checkbox标签的对象
 * add by xianglei.dong
 * 2016-03-23
 */
function visa_orderAllChecked(obj) {
	if (obj.checked) {
		$("input[name='visaOrderId']").not("input:checked").each(function() {
			this.checked = true;
		});
		$("input[name='visa_orderAllChk']").not("input:checked").each(function() {
			this.checked = true;
		});
	} else {
		$("input[name='visaOrderId']:checked").each(function() {
			this.checked = false;
		});
		$("input[name='visa_orderAllChk']:checked").each(function() {
			this.checked = false;
		});
	}
}

/* 签证类订单列表反选
 * add by xianglei.dong
 * 2016-03-23
 */
function visa_orderAllNoCheck() {
	$("input[name='visaOrderId']").each(function() {
		$(this).attr("checked", !$(this).attr("checked"));
	}); 
		
	pr_allchk();
}

function pr_allchk() {
	var chk = 0;
	var chknum = $("input[name='visaOrderId']").size();		
	$("input[name='visaOrderId']").each(function() {
		if ($(this).attr("checked") == 'checked') {
			chk++;
		}
	});
	if (chknum == chk) {//全选 
		$("input[name='visa_orderAllChk']").attr("checked", true);
	}else {//不全选 
		$("input[name='visa_orderAllChk']").attr("checked", false);
	}
}

/* 每行中的复选框
 * add by xianglei.dong
 * 2016-03-23
 */
function visaOrderCheckchg() {
	if ($("input[name='visaOrderId']").not("input:checked").length) {
		$("input[name='visa_orderAllChk']").attr("checked",false);
	}else {
		$("input[name='visa_orderAllChk']").attr("checked",true);
	}
}

function focustext(obj){
	$(obj).siblings().filter(".delanduseall").show();
}

function blurtext(obj){
	$(obj).siblings().filter(".delanduseall").hide();
}

function delThis(obj){
	$(obj).parent().prev().val("");
	$(obj).parent().hide();
}

function useAll(obj){
	var $value = $(obj).parent().prev().val();
	$(".selectGuarantee").each(function(){
		if($(this).val()!=="1"){
			$(this).parent().parent().find("#yingshouyajin").val($value);
		}
	});
	$(obj).parent().hide();
}



/**
 * 销售签证/签务签证订单3级菜单搜索订单列表
 * @param payStatus 订单状态（支付状态）
 * @param showList 显示订单列表？显示游客列表？
 */
function getVisaOrderList(payStatus, showList) {
	resetSearchParams();
	// 组织表单数据
	$("#orderPayStatus").val(payStatus);
	$("#showList").val(showList);
	if ($("#cancelOrderPermission") && $("#cancelOrderPermission").val() == "1" && payStatus == "99") {
		$("#showType").val("99");
	} else {
		$("#showType").val("0");
	}
	$("#searchForm").submit();
}

/**
 * 查询条件重置
 */
var resetSearchParams = function () {
	$(':input', '#searchForm')
		.not(':button, :submit, :reset, :hidden')
		.val('')
		.removeAttr('checked')
		.removeAttr('selected');
}
