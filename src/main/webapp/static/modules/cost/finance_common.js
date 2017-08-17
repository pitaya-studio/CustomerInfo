/**
 * 确认付款，取消付款
 * @param ctx			网站路径
 * @param review_uuid	审批ID
 * @param status		付款状态，0：未付，1：已付
 * @param flag			0：确认付款，1：撤销付款
 * @author shijun.liu
 */
function confirmOrCannelPay(ctx, review_uuid, status, flag){
	var msg = "";
	if(0 == flag){
		msg = "确定要执行付款吗？";
	}else{
		msg = "确定要撤销付款吗？";
	}
	$.jBox.confirm(msg,"提示",function(v,h,f){
		if(v=='ok'){
			$.ajax({
				type:"POST",
				url:ctx+"/review/common/web/confimOrCancelPay",
				data:{reviewId:review_uuid,status:status},
				success:function(data){
					if(data.flag){
						$.jBox.tip('操作成功', 'success');
						$("#searchForm").submit();
					}else{
						$.jBox.tip('操作失败,原因:' + data.msg, 'error');
						return false;
					}
				}
			})
		}
		if(v=='cancel'){
			return true;
		}
	})
}

/**
 * 确认已收发票，取消已收发票   217   只针对奢华之旅  成本付款 退款付款
 * @param ctx			网站路径
 * @param review_uuid	审批ID
 * @param status		发票状态，0：未收，1：已收
 * @param flag			0：确认已收发票，1：撤销已收发票
 * @author jinxin.gao
 */
function confirmOrCannelInvoice(ctx, review_uuid, status, flag){
	var msg = "";
	if(0 == flag){
		msg = "是否确认收到发票？";
	}else{
		msg = "是否确认撤销收到发票？";
	}
	$.jBox.confirm(msg,"提示",function(v,h,f){
		if(v=='ok'){
			$.ajax({
				type:"POST",
				url:ctx+"/review/common/web/confimOrCancelInvoice",
				data:{reviewId:review_uuid,status:status},
				success:function(data){
					if(data.flag){
						$.jBox.tip('操作成功', 'success');
						$("#searchForm").submit();
					}else{
						$.jBox.tip('操作失败,原因:' + data.msg, 'error');
						return false;
					}
				}
			})
		}
		if(v=='cancel'){
			return true;
		}
	})
}

/**
 * 批量确认发票   217   只针对奢华之旅  成本付款 退款付款
 * @param ctx			网站路径
 * @param review_uuid	审批ID
 * @param status		发票状态，0：未收，1：已收
 * @param flag			0：确认已收发票，1：撤销已收发票
 * @author jinxin.gao
 */
function confirmOrCannelInvoiceValues(ctx, review_uuids, status){
	var tmp = '';
	$("input[name='"+review_uuids+"']").each(function(){
		if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
			tmp=tmp+$(this).attr('value')+",";
		}
	});
	if(tmp==""){
		alert("请选择至少一条记录");
		return;
	}
	$.jBox.confirm("是否批量确认收到发票？","提示",function(v,h,f){
		if(v=='ok'){
			$.ajax({
				type:"POST",
				url:ctx+"/review/common/web/confimOrCancelInvoice",
				data:{reviewId:tmp,status:status},
				success:function(data){
					if(data.flag){
						$.jBox.tip('操作成功', 'success');
						$("#searchForm").submit();
					}else{
						$.jBox.tip('操作失败,原因:' + data.msg, 'error');
						return false;
					}
				}
			})
		}
		if(v=='cancel'){
			return true;
		}
	})
}

/**
 * 环球行确认付款，取消付款操作
 * @param ctx			地址
 * @param batchId		批次号
 * @param payStatus		付款状态，0：未付，1：已付
 * @param flag			标识，0，表示执行确认付款，1,表示执行取消付款
 * @author	shijun.liu
 * @date	2015.12.05
 */
function confirmOrCancelPayForTTS(ctx, batchId, payStatus, flag){
	var msg='';
	if(0==flag){
		msg="确定要执行付款吗？"
	}else{
		msg="确定要撤销付款吗？"
	}
	$.jBox.confirm(msg,"提示",function(v,h,f){
		if(v=='ok'){
			$.ajax({
				type:"POST",
				url:ctx+"/costNew/payManager/confimOrCancelTTSQZPay",
				data:{batchId:batchId,payStatus:payStatus},
				success:function(data){
					if('success' == data.flag){
						$.jBox.tip('操作成功', 'success');
						$("#searchForm").submit();
					}else{
						$.jBox.tip('操作失败', 'error');
					}
				}
			})
		}
	})
}