$(function(){
	$(document).click(function(){
		$(".price-strategy").each(function(){
			$(this).hide();
		});
	});
});
	function showTrial(obj){
		var em = $(obj).children().last();
		var clsName = em.attr('class');
		if(clsName.indexOf("trial-icon-up") == -1){
			$(em).addClass("trial-icon-up");
			$(".price-container").show();
		}else{
			$(em).removeClass("trial-icon-up");
			$(".price-container").hide();
		}
	}
	 function getEvent(){
	     if(window.event) {return window.event;}
	     func=getEvent.caller;
	     while(func!=null){
	         var arg0=func.arguments[0];
	         if(arg0){
	             if((arg0.constructor==Event || arg0.constructor ==MouseEvent
	                || arg0.constructor==KeyboardEvent)
	                ||(typeof(arg0)=="object" && arg0.preventDefault
	                && arg0.stopPropagation)){
	                 return arg0;
	             }
	         }
	         func=func.caller;
	     }
	     return null;
	     }
	     function cancelBubble()
		{
		    var e=getEvent();
		    if(window.event){
		        //e.returnValue=false;//阻止自身行为
		        e.cancelBubble=true;//阻止冒泡
		     }else if(e.preventDefault){
		        //e.preventDefault();//阻止自身行为
		        e.stopPropagation();//阻止冒泡
		     }
		} 
	function getStrage(obj){
		cancelBubble();
		$(obj).children(".price-strategy").toggle();
//		changeClearPriceSum($(obj).next().get(0));
	}
	function showOperaIcon(obj){
//		$(obj).find(".opera-icon").show();
	}
	function hideOperaIcon(obj){
//		$(obj).find(".opera-icon").hide();
	}
	function selectTrial(obj,i){
		var selected = $(obj).html();
		if(selected == '折扣'){
			$(obj).parent().parent().next().next().show();
		}else{
			$(obj).parent().parent().next().next().hide();
		}
		$(obj).parent().parent().children("span").html(selected);
		$(obj).parent().hide();
		var node = $(obj).parent().parent().next();
		changeClearPriceSum(node.get(0));
		calculatePrice(node,i);
		cancelBubble();
	}
	function addItems(obj,i,cantAdd){
		if(cantAdd)return;
		var htmlEle ="<div class='items' onmouseover='showOperaIcon(this);' onmouseout='hideOperaIcon(this);'><span class='price-title'  onclick='getStrage(this);'><span name='opera"+i+"'>直减</span><em class='title-icon dynamic'></em><ul class='price-strategy'><li onclick='selectTrial(this,"+i+");'>直减</li><li onclick='selectTrial(this,"+i+");'>折扣</li></ul></span><input name='priceValue"+i+"' type='text' class='price-input' onkeyup='calculatePrice(this,"+i+");'><span class='percentage'>%</span><span class='opera-icon'><em class='plus' onclick='addItems(this ,"+i+");'></em><em class='minus' onclick='removeItems(this ,"+i+");'></em></span></div>" ;
		$(obj).parent().parent().parent().append(htmlEle);
	}
	function removeItems(obj,i){
		var items = $(obj).parent().parent().parent().children("div");
		if(items.length<=1){
			return false;
		}else{
			var mainCon = $(obj).parent().parent().parent();
			$(obj).parent().parent().remove();
			if( $(".clickBtn",window.parent.document).parent().find("#hiddenParm").find("#isBatch").length > 0){
				batchSet(mainCon.children(".items"),i);
			}else{
				calculatePrice2(mainCon.children(".items"),i);
			}
		}
	}
	
	/**
     * 判断价格格式是否正确
     * @param obj
     */
    function changeClearPriceSum(obj){
        var money = obj.value;
        if(money && money != ""){
            if(money >= 0){
                var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
                var txt = ms.split(".");
                if( txt[0] &&  txt[0].length > 9 && ($(obj).next().css("display")=='none' || $(obj).attr('class').indexOf('price-edit') != -1)){
                	txt[0] = txt[0].substring(0,9);
                }else if(txt[0] &&  txt[0].length > 2 && $(obj).next().css("display")!='none' && $(obj).attr('class').indexOf('price-edit') == -1){
                	if(txt[0] >100){
                		txt[0] = txt[0].substring(0,3);
                	}
                	if(txt[0] >100){
                		txt[0] = txt[0].substring(0,2);
                	}
                	if(txt[0] == 100 && txt.length>1){
                		txt[1] = 0;
                	}
                }
                obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
//                alert(obj.value);
            }else{
                obj.value = '';
            }
        }
//        changeClearPriceByInputChange($(obj).closest("form"));
    }
	
	function calculatePrice(obj,i){
		changeClearPriceSum(obj);
//		$(obj).val($(obj).val().replace(/[^\d.]/g,''));
		var mainCon = $(obj).parent().parent().children(".items");
		if( $(".clickBtn",window.parent.document).parent().find("#hiddenParm").find("#isBatch").val() == "true"){
			batchSet($(obj).parent().parent().children(".items"),i,obj);
		}else{
			var sid = "standard"+i;
			var prevResult = $("#"+sid).val();
			if(prevResult == null || prevResult.trim() == ""){
				$(obj).val("");
				return;
			}
			calculatePrice2($(obj).parent().parent().children(".items"),i,obj);
		}
	}
	
	
		    		
	function calculatePrice2(mainCon,i,obj){
		var tempValue = "";
		var divConTemp ;
		if(i == 1){
			divConTemp = $(".clickBtn",window.parent.document).parent().find("#hiddenParm").find("#adultPri");
		} else if(i == 2){
			divConTemp = $(".clickBtn",window.parent.document).parent().find("#hiddenParm").find("#childrenPri");
		} else if(i == 3){
			divConTemp = $(".clickBtn",window.parent.document).parent().find("#hiddenParm").find("#specialPri");
		}
		var rid = "result"+i;
		var sid = "standard"+i;
		var prevResult = $("#"+sid).val();
		if(!prevResult){
			return;
		}
        for(var i=0;i<mainCon.length;i++){
        	var ops = $(mainCon[i]).children(".price-title").children("span").html();
        	var value = $(mainCon[i]).children("input").val();
        	if(value!="" && value != null){
				if(ops == "直减"){
					/*if(obj && parseFloat(prevResult)<parseFloat(value)){
						$(mainCon[i]).children("input").val("");
					}else{*/
						prevResult = prevResult-value;
						tempValue = tempValue+"2:"+value+",";
						prevResult = prevResult.toFixed(2);
					/*}*/
				}else if(ops == "折扣"){
				    prevResult = prevResult*accAdd(100,-value)/100;
				    tempValue = tempValue+"3:"+value+",";
				}
			}
        }
		//(prevResult);
        //$("#"+rid).html(prevResult.toFixed(2));
        //$("#"+rid).html(new Number(prevResult).toFixed(2));
		$("#"+rid).html(prevResult);
        divConTemp.val(tempValue);
        
	}
	
	function accAdd(arg1,arg2){ 
	    var r1,r2,m; 
	    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0} 
	    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0} 
	    m=Math.pow(10,Math.max(r1,r2)) 
	    return (arg1*m+arg2*m)/m 
	}
	
	//批量设置计算
	function  batchSet(mainCon,i){
		var tempValue = "";
		var divConTemp ;
		if(i == 1){
			divConTemp = $(".clickBtn",window.parent.document).parent().find("#hiddenParm").find("#adultPri");
		} else if(i == 2){
			divConTemp = $(".clickBtn",window.parent.document).parent().find("#hiddenParm").find("#childrenPri");
		} else if(i == 3){
			divConTemp = $(".clickBtn",window.parent.document).parent().find("#hiddenParm").find("#specialPri");
		}
		
        for(var i=0;i<mainCon.length;i++){
        	var ops = $(mainCon[i]).children(".price-title").children("span").html();
        	var value = $(mainCon[i]).children("input").val();
        	if(value!="" && value != null){
				if(ops == "直减"){
					tempValue = tempValue+"2:"+value+",";
				}else if(ops == "折扣"){
				    tempValue = tempValue+"3:"+value+",";
				}
			}
        }
        divConTemp.val(tempValue);
	}
	
	$(function(){
		var divCon = $(".clickBtn",window.parent.document).parent().find("#hiddenParm");
		divCon.append("<input type='hidden' id='adultPri' value='' />")
			    		.append("<input type='hidden' id='childrenPri' value='' />")
			    		.append("<input type='hidden' id='specialPri' value='' />");
		calculatePrice2($("#adultPricingStrategy").children(".items"),1);
		calculatePrice2($("#childPricingStrategy").children(".items"),2);
		calculatePrice2($("#specialPricingStrategy").children(".items"),3);
		}); 
	
	function judgeInput(obj,inputType,i){
//		$(obj).val($(obj).val().replace(/[^\d.]/g,''));
		var mainCon = $("#"+inputType).children(".items");
		var prevResult = $(obj).val();
		if(prevResult == null || prevResult.trim() == ""){
			$(obj).val("");
			return;
		}
		calculatePrice2(mainCon,i,obj);
	}
	
	function changeResult(obj,i){
		$(obj).val($(obj).val().replace(/[^\d.]/g,''));
		var thisVal = $(obj).val();
		var oid="opera"+i;
		var oList = $("span[name='"+oid+"']");
		var rid = "result"+i;
		var prevResult = $(obj).val();
		for(var i=0;i<oList.length;i++){
			var ops = $(oList[i]).html();
        	var value = $(oList[i]).parent().next().val();
        	if(value!="" && value != null){
				if(ops == "直减"){
					prevResult = prevResult-value;
				}else if(ops == "折扣"){
				    prevResult = prevResult*value/100;
				}
			}
		}
		$("#"+rid).html(prevResult);
	}

String.prototype.trim = function () {
	return this .replace(/^\s\s*/, '' ).replace(/\s\s*$/, '' );
}