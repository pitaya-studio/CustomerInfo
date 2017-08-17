$(function(){
	//搜索条件筛选
    launch();
    //操作浮框
    operateHandler();
    //团号和产品切换
    switchNumAndPro();
    //产品名称团号切换
    switchNumAndPro();
    //产品销售和下单人切换
    switchSalerAndPicker();
    $('[name=orderedId]').comboboxSingle();
    //下拉模糊搜索
    $("[name=salerId]").comboboxSingle();
    $("[name=operatorId]").comboboxSingle();

  	//如果展开部分有查询条件的话，默认展开，否则收起
	var inputRequest = false;
	var selectRequest = false;
	$("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][title!='全部']").each(function(){
		if($(this).val()){
			inputRequest = true;
			return false;
		}
	})
	$("#searchForm").find("select").each(function(){
		var val = $(this).children("option:selected").val();
		if(val && 0 != val){
			selectRequest = true;
			return false;
		}
	})
	if(inputRequest||selectRequest){
		$('.zksx').click();
	}
})

/**
 * 重置按钮函数
 */
function resetSearchParams() {
    $(':input', '#searchForm','select')
            .not(':button, :submit, :reset ')
            .val('')
            .removeAttr('checked')
            .removeAttr('selected');
}
//分页执行函数
function page(n,s){
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    $("#searchForm").submit();
    return false;
}
/**
 * 应收账款账龄数据导出Excel文件
 * @author shijun.liu
 */
function exportToExcel(ctx){
	//表单所有参数
	var args = $('#searchForm').serialize();
	window.open(ctx + "/receivepay/manager/downloadReceive?1=1&" + args);
}
