/*!
 * JQuery for the module of finance(财务模块的脚本)
 *
 * Depends:common.js
 * the public functions used by this module:CurrencyMoney、validNum、validNumFinally、sumToStrCost
 *
 * Date: 2015-01-12
 */
 
/*=============成本管理模块-成本录入-散拼、机票、海岛、签证、酒店成本录入 begin===============*/
//运控-成本录入-添加项目
function jbox_tjxm(){
	var html = '<div class="costBox">';
	var $templateClone = $("#addItem").clone(false);
	//重置表单元素的id和name值
	$templateClone.find("[id]").each(function(index, element) {
        $(element).attr("id",$(element).attr("id").replace("0","")).attr("name",$(element).attr("name").replace("0",""));
    });
	//重置表单的供应商、渠道商label的for
	$templateClone.find("label[for]").each(function(index, element) {
        $(element).attr("for",$(element).attr("for").replace("0",""));
    });
	html += $templateClone.html();
	html += '</div>';
	$.jBox(html, { title: "成本录入",buttons:{'提交': true,'取消': false}, submit:function(v, h, f){
		if (v==true){
			var container = "";
			var tbodyFirstTdTxt = "";
			var tfootFirstTdTxt = "";
			var name = $.trim(f.name);
            var price = $.trim(f.price);
			var account = $.trim(f.account);
            var comment = f.comment;
			var sorc; //供应商或渠道商的名字
			
			if(1 == f.customer){
				sorc = $("#supply option[value=" + f.supply + "]").text();
			}else{
				sorc = $("#channels option[value=" + f.channels + "]").text();
			}

			//判断是境内还是境外明细
			if("1" == f.detailType){
				container = "#costDomestic";
				firstTrTdTxt = "境内付款明细";
				tfootFirstTdTxt = "境内小计";
			}else{
				container = "#costForeign";
				firstTrTdTxt = "境外付款明细";
				tfootFirstTdTxt = "境外小计";
			}
			//为空判断
			if(name==""){
                top.$.jBox.tip('项目名称不能为空', 'error', { focusId: 'name' }); return false;
            }else if(price==""){
                top.$.jBox.tip('单价不能为空', 'error', { focusId: 'price' }); return false;
            }else if(comment.length > 200){
                top.$.jBox.tip('项目备注不能大于200字','error' ,{ focusId: 'comment' }); return false;
            }else if(account==""){
                top.$.jBox.tip('数量不能为空', 'error', { focusId: 'account' }); return false;
            }else {
				//开发编写保存数据
				//var jsonRes = {name : name, price : price, comment : comment, activityGroupId : groupId};
				//$.ajax({
//					type: "POST",
//					url: "/trekiz_wholesaler/a/cost/manager/save",
//					cache:false,
//					dataType:"json",
//					async:false,
//					data:{value:JSON.stringify(jsonRes),
//						type : classType},
//					success: function(array){
//						for(var i = 0; i < array.length; i++){
//							var index = parseInt($List.find("td:first-child").last().html());
//							if(!index){
//								index = 0;
//							}
//							$List.append('<tr id=' + classType + "_" + array[i].id + '> <td>' + (index + i + 1) + '</td><td>'+array[i].name+'</td><td price-sum-bind="' + classType + '">'+array[i].price+'</td><td>'+array[i].comment+'</td><td><a href="javascript:void(0)" onclick="modifyCost('+ array[i].id +', \'' + classType + '\')">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="return deleteCost('+array[i].id+', \'' + classType + '\');" class="button_delcb"></a></td></tr>');
//						}
//						eventHandler('operatorBudget', $('#contentTable'));
//					},
//					error : function(e){
//						top.$.jBox.tip('请求失败。','error' ,{ focusId: 'price' });
//						return false;
//					}
//				});

				//向table中添加数据
				var html_tr = '<tr value="{10}">'.replace("{10}",f.detailType);
				if($(container).find("tbody tr").length){
					//行合并
					var $firstTd = $(container).find("tbody tr:first td:first")
					var rowspan_count = $firstTd.attr("rowspan") ? Number($firstTd.attr("rowspan")) : 1;
					$firstTd.attr("rowspan",rowspan_count + 1);
				}else{
					html_tr += '<td rowspan="1">{6}</td>'.replace("{6}",firstTrTdTxt);
					$(container).find("tfoot").append('<tr><td>{0}</td><td colspan="7"></td></tr>'.replace("{0}",tfootFirstTdTxt));
				}
				html_tr += '<td width="17%" name="tdName">{0}</td><td class="tr" width="10%" name="tdAccount">{1}</td><td width="15%" name="tdSupply" tovalue="{21}">{2}</td><td width="10%" name="tdCurrencyName">{3}</td><td class="tr" width="10%" name="tdPrice">{4}</td><td width="20%" name="tdComment">{5}</td><td width="8%" class="tc"><a href="javascript:void(0)" onclick="modifyCost(this)">修改</a>&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="deleteCost(this)">删除</a></td></tr>'.replace("{0}",name).replace("{1}",account).replace("{2}",sorc).replace("{3}",$("#currencyType option[value=" + f.currencyType + "]").text()).replace("{4}",milliFormat(price,'1')).replace("{5}",comment).replace("{21}",f.customer);
				$(container).find("tbody").append(html_tr);
				costSum($(container));
				return true;
            }
		}
	},height:470,width:450});
	switchCustomer();
}
//选择客户类别
function switchCustomer(){
	$(".costBox input[name=customer]").change(function(){
		var flag_showDiv = $(this).val();
		if("1" == flag_showDiv){
			$(".costBox").find("[flag=customer1]").show();
			$(".costBox").find("[flag=customer2]").hide();
		}else{
			$(".costBox").find("[flag=customer2]").show();
			$(".costBox").find("[flag=customer1]").hide();
		}
	});
}
//运控-成本录入-添加项目--小计（境内、境外）
function costSum(obj){
	var objMoney = {};
	obj.find("tbody tr").each(function(index, element) {
		var currencyName = $(element).find("td[name='tdCurrencyName']").text();
		var thisAccount = $(element).find("td[name='tdAccount']").text();
		var thisPrice = $(element).find("td[name='tdPrice']").text();
		if(typeof objMoney[currencyName] == "undefined"){
			objMoney[currencyName] = parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(",",""),10);
		}else{
			objMoney[currencyName] += parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(",",""),10);
		}
	});
	//输出结果
	var strCurrency = "";
	var sign = " + ";
	var $footTd = obj.find("tfoot tr:first td:last");
	for(var i in objMoney){
		var isNegative = /^\-/.test(objMoney[i]);
		if(isNegative){
			sign = " - ";
		}
		if(strCurrency != '' || (strCurrency == '' && isNegative)){
			strCurrency += sign;
		}
		strCurrency += i + milliFormat(objMoney[i].toString().replace(/^\-/g,''),'1');
	}
	if($footTd.length){
		$footTd.html(strCurrency);
	}
}
//运控-成本录入-修改项目
function modifyCost(dom){
	//清除正在编辑的状态
	$("tr[ismodify='1']").each(function(index, element) {
        $(element).removeAttr("ismodify");
    });
	//设置正在编辑项目tr
	var $thisTr = $(dom).parents("tr");
	$thisTr.attr("ismodify","1");
	//弹出框中加载数据
	var thisDetailType = $thisTr.attr("value");/*境内、境外*/
	var thisSupply = $thisTr.find("td[name='tdSupply']").text();/*供应商*/
	var sorcValue = $thisTr.find("td[name='tdSupply']").attr("tovalue");/*用来判断是供应商or渠道商的value值*/
	var thisName = $thisTr.find("td[name='tdName']").text();/*项目名称*/
	var thisCurrencyType = $thisTr.find("td[name='tdCurrencyName']").text();/*币种*/
	var thisPrice = $thisTr.find("td[name='tdPrice']").text();/*单价*/
	var thisAccount = $thisTr.find("td[name='tdAccount']").text();/*数量*/
	var thisComment = $thisTr.find("td[name='tdComment']").text();/*备注*/
	//绑定数据
	var $templateClone = $("#addItem").clone(false);
	//重置表单元素的id和name值
	$templateClone.find("[id]").each(function(index, element) {
        $(element).attr("id",$(element).attr("id").replace("0","")).attr("name",$(element).attr("name").replace("0",""));
    });
	//重置表单的供应商、渠道商label的for
	$templateClone.find("label[for]").each(function(index, element) {
        $(element).attr("for",$(element).attr("for").replace("0",""));
    });
	
	//去除select的选项
	$templateClone.find("option:selected").removeAttr("selected");
	//境内、境外
	$templateClone.find("#detailType option[value='" + thisDetailType + "']").attr("selected","selected");
	
	//供应商or渠道商
	var $sorcSelect;
	if("1" == sorcValue){
		$sorcSelect = $templateClone.find("#supply option");
		
	}else{
		$sorcSelect = $templateClone.find("#channels option");
	}
	$sorcSelect.each(function(index, element) {
        if($(element).text() == thisSupply){
			$(element).attr("selected","selected");
			return;
		}
    });
	//定位供应商or渠道商的radio
	$templateClone.find("input[name='customer']").removeAttr("checked");
	$templateClone.find("input[name='customer'][value='" + sorcValue + "']").attr("checked","checked");;
	//显示隐藏对应的供应商or渠道商
	$templateClone.find("[flag^='customer']").hide();
	$templateClone.find("[flag='customer" + sorcValue + "']").show();
	
	//项目名称
	$templateClone.find("#name").attr("value",thisName);
	//币种
	$templateClone.find("#currencyType option").each(function(index, element) {
        if($(element).text() == thisCurrencyType){
			$(element).attr("selected","selected");
			return;
		}
    });
	//单价
	$templateClone.find("#price").attr("value",thisPrice.replace(/,/g,''));
	//数量
	$templateClone.find("#account").attr("value",thisAccount);
	//备注
	$templateClone.find("#comment").text(thisComment);
	$.jBox('<div class="costBox">'+$templateClone.html()+'</div>', { title: "成本修改",buttons:{'提交': true,'取消': false}, submit:function(v, h, f){
		if (v==true){
			var $containerTr = $("tr[ismodify='1']");
			var name = $.trim(f.name);
            var price = $.trim(f.price);
			var account = $.trim(f.account);
            var comment = f.comment;
			var sorc; //供应商或渠道商的名字
			
			if(1 == f.customer){
				sorc = $("#supply option[value=" + f.supply + "]").text();
			}else{
				sorc = $("#channels option[value=" + f.channels + "]").text();
			}
			
			//为空判断
			if(name==""){
                top.$.jBox.tip('项目名称不能为空', 'error', { focusId: 'name' }); return false;
            }else if(price==""){
                top.$.jBox.tip('单价不能为空', 'error', { focusId: 'price' }); return false;
            }else if(comment.length > 200){
                top.$.jBox.tip('项目备注不能大于200字','error' ,{ focusId: 'comment' }); return false;
            }else if(account==""){
                top.$.jBox.tip('数量不能为空', 'error', { focusId: 'account' }); return false;
            }else {
				//修改table中的数据
				$containerTr.find("td[name='tdSupply']").text(sorc).attr("tovalue",f.customer);
				$containerTr.find("td[name='tdName']").text(name);
				$containerTr.find("td[name='tdCurrencyName']").text($("#currencyType option[value=" + f.currencyType + "]").text());
				$containerTr.find("td[name='tdPrice']").text(milliFormat(price,'1'));
				$containerTr.find("td[name='tdAccount']").text(account);
				$containerTr.find("td[name='tdComment']").text(comment);
				
				var containerFrom;
				var containerTo;
				var tbodyFirstTdTxtTo = "";
				var tfootFirstTdTxtTo = "";
				
				//判断是境内还是境外明细
				if("1" == f.detailType){
					containerTo = "#costDomestic";
					firstTrTdTxtTo = "境内付款明细";
					tfootFirstTdTxtTo = "境内小计";
					containerFrom = "#costForeign";
				}else{
					containerTo = "#costForeign";
					firstTrTdTxtTo = "境外付款明细";
					tfootFirstTdTxtTo = "境外小计";
					containerFrom = "#costDomestic";
				}
				//若修改境内境外类型
				if(thisDetailType != f.detailType){					
					var tdFromTxt = "";
					//被移除行是否是第一行
					if(!$containerTr.index()){
						tdFromTxt = $containerTr.find("td:first").text();
					}
					//设置境内境外标志
					$containerTr.attr("value",f.detailType).appendTo($(containerTo).find("tbody"));
					$containerTr = $(containerTo).find("tbody tr:last");
					//移入行操作
					if(1 == $(containerTo).find("tbody tr").length){
						if(7 == $containerTr.find("td").length){
							$containerTr.prepend('<td width="10%" rowspan="{0}">{1}</td>'.replace("{0}","1").replace("{1}",firstTrTdTxtTo));
						}else{
							$containerTr.find("td:first").text(firstTrTdTxtTo).attr("rowspan","1");
						}
						$(containerTo).find("tfoot").append('<tr><td width="10%">{0}</td><td colspan="7"></td></tr>'.replace("{0}",tfootFirstTdTxtTo));
					}else{
						if(8 == $containerTr.find("td").length){
							$containerTr.find("td:first").remove();
						}
						//合并行
						var $firstTd = $(containerTo).find("tbody tr:first td:first")
						var rowspan_count = $firstTd.attr("rowspan") ? Number($firstTd.attr("rowspan")) : 1;
						$firstTd.attr("rowspan",rowspan_count + 1);
					}
					
					//被移出行操作
					if(0 == $(containerFrom).find("tbody tr").length){
						$(containerFrom).find("tbody").empty();
						$(containerFrom).find("tfoot").empty();
					}else{
						if("" != tdFromTxt){
							$(containerFrom).find("tbody tr:first").prepend('<td width="10%" rowspan="{0}">{1}</td>'.replace("{0}",$(containerFrom).find("tbody tr").length).replace("{1}",tdFromTxt));
						}else{
							$(containerFrom).find("tbody tr:first td:first").attr("rowspan",$(containerFrom).find("tbody tr").length);
						}
					}
					costSum($(containerFrom));				
				}
				costSum($(containerTo));
				return true;
            }
		}
	},height:470,width:450});
	switchCustomer();
}
//删除成本录入
function deleteCost(dom){
	var $thisTr = $(dom).parents("tr");
	var tableName = "#" + $(dom).parents("table").attr("id");
	var $firstTd = $(tableName).find("tr:first td:first");
	if(1 == $(tableName).find("tbody tr").length){
		$(tableName).find("tbody").empty();
		$(tableName).find("tfoot").empty();
	}else{
		if($thisTr.index()){
			$firstTd.attr("rowspan",Number($firstTd.attr("rowspan"))-1);
		}else{
			$thisTr.next().prepend('<td width="10%" rowspan="{0}">{1}</td>'.replace("{0}",$(tableName).find("tbody tr").length-1).replace("{1}",$firstTd.text()));
		}
		$thisTr.remove();
	}
	costSum($(tableName));
}

//预计总毛利初始化
function calculateProfit(){
	var $tmpCurrencyType = $("#currencyType0");
	//成本
	var money_cost = new CurrencyMoney($tmpCurrencyType.find("option"));
	//收入
	var money_income = new CurrencyMoney($tmpCurrencyType.find("option"));
	$("ul[data-total='cost']").find("li").each(function(index,element){
		if(0 != index){
			var currencyType = $(element).find("font").text().replace(/ /g,"");
			var currencyValue = isFloat($(element).find("[data-money]").attr("data-money").replace(/ /g,""));
			money_cost[currencyType] = currencyValue;
		}
	});
	$("ul[data-total='income']").find("li").each(function(index,element){
		if(0 != index){
			var currencyType = $(element).find("font").text().replace(/ /g,"");
			var currencyValue = isFloat($(element).find("[data-money]").attr("data-money").replace(/ /g,""));
			money_income[currencyType] = currencyValue;
		}
	});
	//毛利的html
	var html_profit = '';
	for(var p in money_cost){
		html_profit = sumToStrCost(html_profit,p,money_income[p] - money_cost[p]);
	};
	if('' == html_profit){
		html_profit = '<li>' + $("ul[data-total='cost']").find("li").eq(1).html() + '</li>';
	}
	var $gProfit = $("ul[data-total='profit']");
	html_profit = '<li>' + $gProfit.find("li:first").text() + '</li>' + html_profit;
	$gProfit.html(html_profit);
}
/*=============成本管理模块-成本录入-散拼、机票、海岛、签证、酒店成本录入 end===============*/


/*===============================================================================
 *财务审核模块-借款审批、退款审批、返佣审批、转款审批、改价审批、转团审批、退团审批；
 *成本审核（散拼、单团等）、发票管理-发票审核 begin
=================================================================================*/
//驳回按钮对应的弹出框
function jbox_bohui(){
	var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
	html += '<textarea style="width:80%; margin:10px auto;" name="" cols="" rows="" ></textarea>';
	html += '</div>';
	$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
			
		}
	},height:250,width:500});

}
/*===============================================================================
 *财务审核模块-借款审批、退款审批、返佣审批、转款审批、改价审批、转团审批、退团审批；
 *成本审核（散拼、单团等）、发票管理-发票审核 begin
=================================================================================*/


/*=============结算管理模块-订单收款确认、押金收款确认、切位收款确认 begin===============*/
//收款确认弹出框
function payforOk(obj){
	var html = '<div class="jbox_type jbox_col2">' +
			   	'<p><label>付款时间：</label>2014-09-02 11:40:03</p><p><label>付款方式：</label>快速支付</p>' +
				'<p><label>支付款类型：</label>支付切位订金</p><p><label>付款金额：</label>￥11111.00</p>' +
				'<p><label>支付凭证：</label><a>下载</a><a style="margin-left:10px;">预览</a></p>' +
				'</div>';
	$.jBox(html, {title: "收款确认",buttons:{"取 消":"0","提 交":"1"},submit:function(v, h, f){
		if (v=="0"){
			//取消的操作
		}else if(v=="1"){
			//重新申请的操作
		}
	},height: 230,width:500,});
}
/*=============结算管理模块-订单收款确认、押金收款确认、切位收款确认 end===============*/