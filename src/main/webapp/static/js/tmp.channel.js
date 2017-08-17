/*!
 * JQuery for the module of channel(渠道模块的脚本)
 *
 * Depends:common.js
 * the public functions used by this module:
 *
 * Date: 2015-01-13
 */

/*=============渠道管理-新增第1步-基本信息 begin===============*/
//添加联系人
function shopPeopleAdd(obj){
	var id=$(obj).parent().parent().find('p').length;
	var cloneDiv = $(".shopPeopleNone").clone(true);
	cloneDiv.appendTo($(obj).parent().parent());
	cloneDiv.show().removeClass('shopPeopleNone').addClass("shopPeopleP").find('em').text(id);
}
//删除联系人
function shopPeopleDel(obj){
	$(obj).parent().remove();
	$('.shopPeopleP').each(function(index, element){
		$(this).find('em').text(index+1);
	});
}
//新增渠道，结款方式
function agentpayfor(obj){
	var sysselect_s=$(".agentpayfor option:selected").index();
	var indexs=$(obj).find("option:selected").index();
	if(indexs==1||sysselect_s==1){
		$(obj).parent().next().show();
		$(".agentpayfor").next().show();
	}else{
		$(obj).next().hide(); 
		$(".agentpayfor").next().hide();
	}
}
/*=============渠道管理-新增第1步-基本信息 end===============*/

/*=============渠道管理-新增银行账户信息 begin===============*/
//渠道 银行账户
function account_tj(){
	//去掉第一个account 的边框线
	$('.account').eq(0).addClass('borderNone');	
	//删除
	$('.account dt em').live('click',function(){$(this).parents('.account').remove();});
	//新增
	$('.account_tj').click(function(){
		var ykhtml=$(this).parent().next('.account').html();
		$(this).parent().before('<dl class="account">'+ykhtml+'</dl>');
	});
}
/*=============渠道管理-新增银行账户信息 end===============*/

/*=============渠道管理-渠道资质 begin===============*/
//其他文件
function qdzz_add(){
	//删除
	$('.qdgl-cen').on('click','.qdzz-add-remove',function(){$(this).parent().remove();});
	//添加其他文件
	var html='<div class="batch" style="margin-top:10px;">';
        html+='<label class="batch-label">其他文件：</label>';
		html+='<div class="pr fl">';
		html+='<input value="" class="inputTxt inputTxtlong" name="orderNum" id="orderNum" flag="istips"> ';
        html+='<span class="ipt-tips">文件名称</span>';
		html+='</div>';
        html+='<input type="button" class="mod_infoinformation3_file" value="上传文件"><div class="ydbz_s qdzz-add-remove gray">删除</div>';
        html+='<ol class="batch-ol">';
        html+='<li><span>新建 Microsoft Office Word 文档.docx</span><a onclick="deleteFile(this,4433)" href="javascript:void(0)" class="batchDel">删除</a></li>';
        html+='</ol>';
        html+='</div>';
	$('.qdzz-add').click(function(){
		$(this).parent().after(html);
		inputTips();
		addClickEvent($(this).parent().next().find("input[type='file']"));
	});
}
/*=============渠道管理-渠道资质 end===============*/