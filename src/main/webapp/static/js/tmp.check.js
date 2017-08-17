/*!
 * JQuery for the module of check(审核模块的脚本)
 *
 * Depends:common.js
 * the public functions used by this module:CurrencyMoney、sumToStrCost
 *
 * Date: 2015-01-12
 */
 
/*===============================================================================
 *借款审批、退款审批、返佣审批、转款审批、改价审批、转团审批、退团审批、应付款审批；
 *成本审核（散拼、单团等）、机票退票、房费付款审批、撤签审批-签证撤签
 *改签审批-单办机票改签 begin
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
 *借款审批、退款审批、返佣审批、转款审批、改价审批、转团审批、退团审批、应付款审批；
 *成本审核（散拼、单团等）、机票退票、房费付款审批、撤签审批-签证撤签
 *改签审批-单办机票改签 end
=================================================================================*/


/*=============成本审核管理模块-散拼、机票、海岛、签证、酒店录入审批 begin===============*/
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
/*=============成本审核管理模块-散拼、机票、海岛、签证、酒店录入审批 end===============*/
/*=============审核列表页切换角色 begin===============*/
function changeRoles(){
	$(".change_roles span").hover(function(){
		$(".change_rolelist").show();
	},function(){
		$(".change_rolelist").hide();
	})
	$(".change_rolelist li").hover(function(){
		var rolemore=$(this).find("li");
		if(rolemore.length>0){
			var theleft=$(this).width()+14;
			var thetop=$(this).find("ul").height()/2;
			$(this).addClass("select");	
			$(this).find("ul").css({'left':theleft,'top':-thetop});
			$(".change_rolelist a").click(function(){
				var thisTxt=$(this).text();
				$(".change_rolea").html(thisTxt);
				var theleft=$(this).parent().parent().parent().width()+14;
				$(this).parent().parent().css("left",theleft);
			})
		}
	},function(){
		$(this).removeClass("select")
	})
}
/*=============审核列表页切换角色 end===============*/