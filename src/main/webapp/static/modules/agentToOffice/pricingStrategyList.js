$(document).ready(function() {var $ctx = '${ctx}';});

/**
 * 搜索条件重置
 * @returns
 */
var resetSearchParams = function () {
            $(':input', '#searchForm')
                    .not(':button, :submit, :reset, :hidden')
                    .val('')
                    .removeAttr('checked')
                    .removeAttr('selected');
        }

/**
 * 禁用启用价格策略
 * @param pricingStrategyId
 * @param url
 * @param msg
 * @param inputId
 * @returns
 */
var updateState = function(pricingStrategyId,url,msg,inputId){
	if($("#"+inputId).attr("checked") == "checked"){
		msg = "启用";
	}else{
		msg = "禁用";
	}
	$.jBox.confirm("是否"+msg+"该策略？","提示",function(v,h,f){
		if(v=='ok'){
			$.ajax({
				type:"POST",
				url:url+"/updateState",
				data:{priceStrategyId:pricingStrategyId},
				success:function(data){
					if("true" == data.flag){
					}else{
					}
				}
			})
		}
		if(v=='cancel'){
			if($("#"+inputId).attr("checked") == "checked"){
				$("#"+inputId).prop("checked",false);
			}else{
				$("#"+inputId).prop("checked",true);
			}
			return;
		}
	})
};

/**
 * 删除价格策略
 * @param pricingStrategyId
 * @param url
 * @param state
 * @returns
 */
var deletePriceStrate =  function(delIndex,pricingStrategyId,url,state){
	state = $("#"+delIndex+" td:first-child input").attr("checked");
	if(state && state == "checked"){
		//启用状态不能删除
		$.jBox.confirm("无法删除该策略.","提示",function(v,h,f){
			if(v=='ok'){
				return;
			}
			if(v=='cancel'){
				return;
			}
		})
	}else{
		$.jBox.confirm("确定要删除吗？","提示",function(v,h,f){
			if(v=='ok'){
				$.ajax({
					type:"POST",
					url:url+"/delete",
					data:{priceStrategyId:pricingStrategyId},
					success:function(data){
						if("true" == data.flag){
							window.location.href = url + "/list";
						}else{
							
						}
					}
				})
			}
			if(v=='cancel'){
				return;
			}
		});
	}
};

/**
 * 翻页
 * n 为页数
 * s 为条数
 */
function page(n, s) {
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    $("#seachbutton").click();
    return false;
}