/*!
 * JQuery for the module of operation control(运控模块的脚本)
 *
 * Depends:common.js
 * the public functions used by this module:CurrencyMoney、validNum、validNumFinally、qiewei、sumToStrCost
 *
 * Date: 2015-01-09
 */

/*=============成本录入-散拼、机票、海岛、签证、酒店录入详情页（实际成本、预算成本） begin===============*/
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
/*=============成本录入-散拼、机票、海岛、签证、酒店录入详情页（实际成本、预算成本） end===============*/


/*=============成本录入-散拼、机票、海岛、签证、酒店录入详情页（预算成本） begin===============*/
//录入成本与收入弹框
/****************************************
@title:弹出框的标题
@obj:this对象
****************************************/
function costAndIncome1(title,obj){
	var $parent = $(obj).parents(".costSum");
	//成本
	var $cost = $parent.find("ul[data-total='cost']");
	//毛利
	var $gProfit = $parent.find("ul[data-total='profit']");
	//收入
	var $income = $parent.find("ul[data-total='income']");
	//jbox弹出的html
	var str_html = ' <div class="msg_div_tmcost1">';
	//成本字符串
	var str_cost = '';
	//成本币种、值对
	var obj_cost = {};
	$cost.find("li").each(function(index,element){
		if(0 != index){
			var currencyType = $(element).find("font").text().replace(/ /g,"");
			var currencyValue = isFloat($(element).find("[data-money]").attr("data-money").replace(/ /g,""));
			obj_cost[currencyType] = currencyValue;
		}
	});
	//币种
	var $currencyType = $("#currencyType0").clone(false);
	//币种名称与value键值对
	var obj_currency = {};
	$currencyType.find("option").each(function(index,element){
		var text = $(element).text().replace(/ /g,"");
		$(element).removeAttr("selected");
		if(undefined != obj_cost[text]){
			obj_currency[text] = $(element).val();
			$(element).remove();
		}
	});
	//成本的“新增”按钮是否显示
	var numWritten = 0;
	var str_hidden = '';
	for(var p in obj_cost){ numWritten++;}
	if(numWritten == $("#currencyType0 option").length){
		str_hidden = 'style="display:none;"';
	}
	//成本字符串拼接
	var i = 1;
	for(var p in obj_cost){
		str_cost += '<p><label for="costCurrencyType' + i +'">币种：</label>';
		var $currencyType_1 = $currencyType.clone();
		$currencyType_1.prepend('<option value="' + obj_currency[p] +'" selected="selected">' + p + '</option>');
		str_cost += '<select style="width:75px; margin-right:20px;" id="costCurrencyType' + i +'" name="costCurrencyType' + i +'" nowValue=' + obj_currency[p] + ' onchange="changeCurrencyType(this)">' + $currencyType_1.html() + '</select>';
		str_cost += '<label for="jboxCost' + i +'">金额：</label><input type="text" onkeyup="validNum(this)" onafterpaste="validNum(this))" onblur="validNumFinally(this)" value="' + obj_cost[p] + '" id="jboxCost' + i +'" name="jboxCost' + i +'" />';
		if(i == 1){
			str_cost += '&#12288;<span class="linkAir-spn" onclick="addCost(this)" ' + str_hidden +'>+新增</span>';
		}else{
			str_cost += '&#12288;<span class="tdred" onclick="delCost(this)">删除</span>';
		}
		str_cost += '</p>';
		i++;
	}
	
	//收入
	var obj_income = {};
	$income.find("li").each(function(index,element){
		if(0 != index){
			var currencyType = $(element).find("font").text().replace(/ /g,"");
			var currencyValue = isFloat($(element).find("[data-money]").attr("data-money").replace(/ /g,""));
			obj_income[currencyType] = currencyValue;
		}
	});
	//币种
	$currencyType = $("#currencyType0").clone(false);
	//币种名称与value键值对
	obj_currency = {};
	$currencyType.find("option").each(function(index,element){
		var text = $(element).text().replace(/ /g,"");
		$(element).removeAttr("selected");
		if(undefined != obj_income[text]){
			obj_currency[text] = $(element).val();
			$(element).remove();
		}
	});
	//成本的“新增”按钮是否显示
	numWritten = 0;
	str_hidden = '';
	for(var p in obj_income){ numWritten++;}
	if(numWritten == $("#currencyType0 option").length){
		str_hidden = 'style="display:none;"';
	}
	//收入字符串拼接
	var j = 1;
	//收入字符串
	var str_income = '';
	for(var p in obj_income){
		str_income += '<p><label for="incomeCurrencyType' + j +'">币种：</label>';
		var $currencyType_1 = $currencyType.clone();
		$currencyType_1.prepend('<option value="' + obj_currency[p] +'" selected="selected">' + p + '</option>');
		str_income += '<select style="width:75px; margin-right:20px;" id="incomeCurrencyType' + j +'" name="incomeCurrencyType' + j +'" nowValue=' + obj_currency[p] + ' onchange="changeCurrencyType(this)">' + $currencyType_1.html() + '</select>';
		str_income += '<label for="jboxIncome' + j +'">金额：</label><input type="text" onkeyup="validNum(this)" onafterpaste="validNum(this))" onblur="validNumFinally(this)" value="' + obj_income[p] + '" id="jboxIncome' + j +'" name="jboxIncome' + j +'" />';
		if(j == 1){
			str_income += '&#12288;<span class="linkAir-spn" onclick="addCost(this)" ' + str_hidden +'>+新增</span>';
		}else{
			str_income += '&#12288;<span class="tdred" onclick="delCost(this)">删除</span>';
		}
		str_income += '</p>';
		j++;
	}
	//拼接弹出框中的html
	str_html += '<div class="pl15"><h3>总成本：</h3>' + str_cost + '</div>';
	str_html += '<div class="pl15"><h3>总收入：</h3>' + str_income + '</div>';
	str_html += '</div>';	
	
	$.jBox(str_html,{
		title:title,
		buttons:{'取消' : 0, '提交' : 1 },
		submit:function (v, h, f) {
			//提交数据
			if(v === 1){
				var $tmpCurrencyType = $("#currencyType0");
				//成本
				var money_cost = new CurrencyMoney($tmpCurrencyType.find("option"));
				//收入
				var money_income = new CurrencyMoney($tmpCurrencyType.find("option"));
				for(var p in f){
					var currencyName = $tmpCurrencyType.find("option[value='" + f[p] + "']").text();
					var flagID = p.match(/\d+$/);
					if("-1" != p.indexOf("costCurrencyType")){
						money_cost[currencyName] += isFloat(f["jboxCost" + flagID]);
					}else if("-1" != p.indexOf("incomeCurrencyType")){
						money_income[currencyName] += isFloat(f["jboxIncome" + flagID]);
					}
				}
				
				//拼接字符串
				var html_cost = '';
				var html_income = '';
				var html_profit = '';
				for(var p in money_cost){
					html_cost = sumToStrCost(html_cost,p,money_cost[p]);
					html_income = sumToStrCost(html_income,p,money_income[p]);
					html_profit = sumToStrCost(html_profit,p,money_income[p] - money_cost[p]);
				};
				if('' == html_cost){
					html_cost = '<li><font class="gray14">人民币</font><span data-money="0" class="tdred">0</span></li>';
				}
				if('' == html_income){
					html_income = '<li><font class="gray14">人民币</font><span data-money="0" class="tdred">0</span></li>';
				}
				if('' == html_profit){
					html_profit = '<li><font class="gray14">人民币</font><span data-money="0" class="tdred">0</span></li>';
				}
				html_cost = '<li>' + $cost.find("li:first").text() + '</li>' + html_cost;
				html_income = '<li>' + $income.find("li:first").text() + '</li>' + html_income;
				html_profit = '<li>' + $gProfit.find("li:first").text() + '</li>' + html_profit;
				$cost.html(html_cost);
				$income.html(html_income);
				$gProfit.html(html_profit);
			}
		},
		height:300,
		width:400
	});
}
//新增成本或收入 @dom:this;
function addCost(dom){
	var $this = $(dom);
	var $parent = $this.parent().parent();
	var $copy = $parent.find("p:last").clone(false);
	//序号
	var ordernum = $parent.find("p").length + 1;
	//id和name重置
	$copy.find('label').each(function(index,element){
		var thisFor = $(element).attr("for");
		$(element).attr("for",thisFor.replace(/\d+$/g,ordernum));
	});
	$copy.find('select,input[type="text"]').each(function(index,element){
		var thisID = $(element).attr("id");
		var thisName = $(element).attr("name");
		$(element).attr("id",thisID.replace(/\d+$/,ordernum));
		$(element).attr("name",thisName.replace(/\d+$/,ordernum));
	});
	//剔除已存在的币种
	var theValue = $parent.find("p:last").find("select[id*='CurrencyType']").val();
	var $newSelect = $copy.find("select[id*='CurrencyType']");
	$newSelect.find("option[value='" + theValue +"']").remove();
	$newSelect.attr("nowValue",$newSelect.find("option:first").val()).find("option:first").prop("selected",true);
	$parent.find("select[id*='CurrencyType'] option[value='" + $newSelect.val() +"']").remove();
	//金额重置为0
	$copy.find("input[id^='jbox']").val(0);
	//新增按钮改为删除
	if(2 == ordernum){
		$copy.find(".linkAir-spn").replaceWith('<span class="tdred" onclick="delCost(this)">删除</span>');
	}
	$copy.appendTo($parent);
	//新增按钮的显示与隐藏
	if($parent.children("p").length == $("#currencyType0 option").length){
		$this.hide();
	}
}
//删除成本或收入
function delCost(dom){
	var $this = $(dom);
	var $parent = $this.parent().parent();
	var $thisRow = $this.parent();
	//删除币种的vale和text
	var optionValue = $thisRow.find("select[id*='CurrencyType']").val();
	var optionText = $thisRow.find("select[id*='CurrencyType']").find("option[value='" + optionValue +"']").text();
	//删除币种行
	$thisRow.remove();
	//重置for、id、name
	$parent.find("p").each(function(i,obj){
		$(obj).find('label').each(function(index,element){
			var thisFor = $(element).attr("for");
			$(element).attr("for",thisFor.replace(/\d+$/g,(i+1)));
		});
		$(obj).find('select,input[type="text"]').each(function(index,element){
			var thisID = $(element).attr("id");
			var thisName = $(element).attr("name");
			$(element).attr("id",thisID.replace(/\d+$/,(i+1)));
			$(element).attr("name",thisName.replace(/\d+$/,(i+1)));
		});
		//select中添加删除的币种
		$(obj).find("select[id*='CurrencyType']").append('<option value="' + optionValue + '">' + optionText +'</option>');
	});
	//新增按钮的显示与隐藏
	$parent.children("p:first").find(".linkAir-spn").show();
}
//更改币种
function changeCurrencyType(dom){
	var $thisSelect = $(dom);
	var $parent = $thisSelect.parent().parent();
	//币种变化
	var oldValue = $thisSelect.attr("nowValue");
	var $addOption = $("#currencyType0 option[value='" + oldValue + "']");
	$thisSelect.attr("nowValue",$thisSelect.val());
	//更改其他的币种select
	$parent.find("p").each(function(index, element) {
		var $select = $(element).find("select[id*='CurrencyType']");
		if($select.find("option[value='" + oldValue +"']").length == 0){
			$select.append($addOption.clone().removeAttr("selected"));
			$select.find("option[value='" + $thisSelect.val() +"']").remove();
		}
	});
}
/*=============成本录入-散拼、机票、海岛、签证、酒店录入详情页（预算成本） end===============*/


/*=============运控-机票、散拼、酒店库存切位  切位对象选择弹框 begin===============*/
function jbox_qw() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<p>您好，请选择需要切位对象  ！</p><dl style="width:300px;"><dt style="height:30px; float:left; width:100%; display:none;"><span style="width:50%; display:inline-block;"><input name="qiewei" type="radio" style="margin:0;" value="" checked /> 内部切位</span><span style="width:50%; display:inline-block;"><input name="qiewei" style="margin:0;" type="radio" value="" /> 渠道切位</span></dt><dd style="display:none; margin:0; float:left; width:100%;"><select name="" ><option>全部</option><option>大客户部</option><option>自由行部</option><option>游学部</option></select></dd><dd style="display:block; margin:0; float:left; width:100%;"><label class="jbox-label" style="width:auto;">渠道门店：</label><select name="" style="width:120px; float:left; margin-left:10px; display:inline;"><option>飞扬假期</option><option>大洋国际</option></select></dd></dl>';
	html += '</div>';
	$.jBox(html, { title: "切位对象选择",buttons:{'取消': 1,'提交':0}, submit:function(v, h, f){
		if (v=="1"){
			return true;
		}else{
			return true;
		}
	},height:180,width:380});
}
/*=============运控-机票、散拼、酒店库存切位  切位对象选择弹框 end===============*/


/*=============运控-机票库存切位  归还切位弹框 begin===============*/
function jbox_ghqwFj() {
	var html = '<div style="margin-top:20px; padding:0 15px 0 20px;">';
	html += '<p><label style="width:auto;" class="jbox-label">舱位等级：</label><select style="width:120px;margin:0 10px; display:inline;" name=""><option>头等舱</option><option>公务舱</option><option>经济舱</option></select>当前余位数量：30</p><dl style="padding-right:5px;"><dt style="height:30px; float:left; width:100%;"><span style="width:50%; display:inline-block;"><input name="qiewei" style="margin:0;" type="radio" value="" checked/> 渠道切位</span></dt><dd style="display:block; margin:0; float:left; width:100%;"><table class="table table-striped table-bordered table-condensed"><thead><tr><th width="25%">序号</th><th width="25%">部门</th><th width="25%">切位</th><th width="25%">已售出切位</th></tr></thead>	<tbody><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 1</td><td class="tc">飞扬假期</td><td class="tc">10</td><td class="tc">0</td></tr><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 2</td><td class="tc">大洋国际</td><td class="tc">10</td><td class="tc">0</td></tr></tbody></table></dd></dl>';
	html += '<label class="jbox-label">归还还位数量：</label>';
	html += '<input type="text" /><br /><label style="width:100%; line-height:30px;">请填写您的还位原因！</label><textarea name="" cols="" rows="" style="width:90%;"></textarea>';
	html += '</div>';
	$.jBox(html, { title: "归还切位录入框",buttons:{'取消': 1,'提交':0}, submit:function(v, h, f){
		if (v=="1"){
			return true;
		}else{
			return true;
		}
	},height:450,width:500});
}
/*=============运控-机票库存切位  归还切位弹框 end===============*/


/*=============运控-酒店库存切位  归还切位弹框 begin===============*/
function jbox_ghqwHost() {
	var html = '<div style="margin-top:20px; padding:0 15px 0 20px;">';
	html += '<p><label style="width:auto;" class="jbox-label">房型选择：</label><select style="width:120px;margin:0 10px; display:inline;" name=""><option>沙滩房</option><option>水沙房</option></select>当前余位数量：30</p><dl style="padding-right:5px;"><dt style="height:30px; float:left; width:100%;"><span style="width:50%; display:inline-block;"><input name="qiewei" style="margin:0;" type="radio" value="" checked/> 渠道切位</span></dt><dd style="display:block; margin:0; float:left; width:100%;"><table class="table table-striped table-bordered table-condensed"><thead><tr><th width="25%">序号</th><th width="25%">部门</th><th width="25%">切位</th><th width="25%">已售出切位</th></tr></thead>	<tbody><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 1</td><td class="tc">飞扬假期</td><td class="tc">10</td><td class="tc">0</td></tr><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 2</td><td class="tc">大洋国际</td><td class="tc">10</td><td class="tc">0</td></tr></tbody></table></dd></dl>';
	html += '<label class="jbox-label">归还还位数量：</label>';
	html += '<input type="text" /><br /><label style="width:100%; line-height:30px;">请填写您的还位原因！</label><textarea name="" cols="" rows="" style="width:90%;"></textarea>';
	html += '</div>';
	$.jBox(html, { title: "归还切位录入框",buttons:{'取消': 1,'提交':0}, submit:function(v, h, f){
		if (v=="1"){
			return true;
		}else{
			return true;
		}
	},height:450,width:500});
}
/*=============运控-酒店库存切位  归还切位弹框 end===============*/


/*=============运控-散拼库存切位  归还切位弹框 begin===============*/
function jbox_ghqw() {
	var html = '<div style="margin-top:20px; padding:0 15px 0 20px;">';
	html += '<p>当前余位数量：30</p><dl style="padding-right:5px;"><dt style="height:30px; float:left; width:100%;"><span style="width:50%; display:none;"><input name="qiewei" type="radio" style="margin:0;" value="" /> 内部切位</span><span style="width:50%; display:inline-block;"><input name="qiewei" style="margin:0;" type="radio" value="" checked/> 渠道切位</span></dt><dd style="display:none; margin:0; float:left; width:100%;"><table class="table table-striped table-bordered table-condensed"><thead><tr><th width="25%">序号</th><th width="25%">部门</th><th width="25%">切位</th><th width="25%">已售出切位</th></tr></thead>	<tbody><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 1</td><td class="tc">自由行部</td><td class="tc">10</td><td class="tc">0</td></tr><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 2</td><td class="tc">自由行部</td><td class="tc">10</td><td class="tc">0</td></tr><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 3</td><td class="tc">自由行部</td><td class="tc">10</td><td class="tc">0</td></tr></tbody></table></dd><dd style="display:block; margin:0; float:left; width:100%;"><table class="table table-striped table-bordered table-condensed"><thead><tr><th width="25%">序号</th><th width="25%">部门</th><th width="25%">切位</th><th width="25%">已售出切位</th></tr></thead>	<tbody><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 1</td><td class="tc">飞扬假期</td><td class="tc">10</td><td class="tc">0</td></tr><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 2</td><td class="tc">大洋国际</td><td class="tc">10</td><td class="tc">0</td></tr></tbody></table></dd></dl>';
	html += '<label style="width:90px; text-align:right; line-height:30px; height:30px; float:left;">归还还位数量：</label>';
	html += '<input type="text" /><br /><label style="width:100%; line-height:30px;">请填写您的还位原因！</label><textarea name="" cols="" rows="" style="width:90%;"></textarea>';
	html += '</div>';
	$.jBox(html, { title: "归还切位录入框",buttons:{'取消': 1,'提交':0}, submit:function(v, h, f){
		if (v=="1"){
			return true;
		}else{
			return true;
		}
	},height:420,width:500});
}
/*=============运控-散拼库存切位  归还切位弹框 end===============*/

/*=============运控-散拼库存切位-散拼切位、运控-酒店库存切位-酒店切位、运控-机票库存切位-机票切位  归还切位弹框 begin===============*/
//运控-散拼库存切位-归还切位 归还一个
function jbox_ghqwone() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<p>当前余位数量：30</p><dl style="overflow:hidden; padding-right:5px;"><dt style="height:30px; float:left; width:100%;"><span style="width:50%; display:inline-block;"><input name="qiewei" style="margin:0;" type="radio" value="" checked/> 渠道切位</span></dt><dd style="margin:0; float:left; width:100%;"><table class="table table-striped table-bordered table-condensed"><thead><tr><th width="25%">序号</th><th width="25%">部门</th><th width="25%">切位</th><th width="25%">已售出切位</th></tr></thead>	<tbody><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 1</td><td class="tc">飞扬假期</td><td class="tc">10</td><td class="tc">0</td></tr></tbody></table></dd></dl>';
	html += '<label style="width:100%; line-height:30px;">请填写您的还位原因！</label><textarea name="" cols="" rows="" style="width:90%;"></textarea>';
	html += '</div>';
	$.jBox(html, { title: "归还切位录入框",buttons:{'提交': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			return true;
		}else{
			return true;
		}
	},height:420,width:480});
	qiewei();
}
/*=============运控-散拼库存切位-散拼切位、运控-酒店库存切位-酒店切位、运控-机票库存切位-机票切位  归还切位弹框 end===============*/