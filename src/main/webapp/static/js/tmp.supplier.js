/*!
 * JQuery for the module of supplier(供应商模块的脚本)
 *
 * Depends:common.js
 * the public functions used by this module:
 *
 * Date: 2015-01-13
 */

/*=============供应商管理-新增第1步-基本信息 begin===============*/
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
/*=============供应商管理-新增第1步-基本信息 end===============*/

/*=============供应商管理-新增第3步-银行账户信息 begin===============*/
//银行账户
function account_tj(){
	//去掉第一个account 的边框线
	$('.account').eq(0).addClass('borderNone');	
	//删除
	$('.account dt em').live('click',function(){
		$(this).parents('.account').remove();
		delBank();
	});
	//新增
	$('.account_tj').click(function(){
		var ykhtml=$(this).parent().next('.account').html();
		$(this).parent().before('<dl class="account">'+ykhtml+'</dl>');
	});
}
/*=============供应商管理-新增第3步-银行账户信息 end===============*/

/*=============供应商管理-新增第4步-资质 begin===============*/
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
/*=============供应商管理-新增第4步-资质 end===============*/

//供应商管理添加地区
// function shopAddressAdd(obj){
	// var html=$(obj).parent().next("p").clone(true);
	// $(obj).parent().after(html);
	// html.show();
// }
// function shopAddressDel(obj){
	// $(obj).parent().remove();
// }
/*==============供应商详情页begin===============*/
//切换卡
function switchingCard(){
	$('.supplierLine > a').hover(function() {
       var iIndex = $(this).index();
       $(this).addClass("select").siblings().removeClass('select');
       $(this).parent().siblings('.switch-cen').addClass('no').eq(iIndex).removeClass('no');

   });
}
/*=============供应商详情页end===============================*/
/*=============文件下载begin===============================*/
function downloads(docid){
	window.open("${ctx}/sys/docinfo/download/"+docid);
}
/*=============文件下载end===============================*/