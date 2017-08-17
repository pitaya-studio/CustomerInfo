$(function(){
	// 将地接计调和机票计调的选中报价写入 计调报价中的成本价
	var objA= $("input[name=chosePriceA]:checked");
	var objB= $("input[name=chosePriceB]:checked");
	if(objA.length>0){
		epriceInfo.service.aop(objA);
	}
	if(objB.length>0){
		epriceInfo.service.top(objB);
	}
	// 当该询价为机票询价时，不必做地接询价查询
	var recordType = $("#recordType").val();
	if(recordType!=7){
		epriceInfo.service.travelRequirementShow();
	}
	
	/**
	 * 选择不同报价，并且将报价放入“成本价”
	 * 
	 */
	epriceInfo.service.hooseReply();
	
	$("#out-price-id").blur(function(){
		//epriceInfo.service.countReply();
		// 20150518 换为直接将外报价写入隐藏表单
		var price = $("#out-price-id").val();
		var isAppFlight =$("#id_isAppFlight").val(); 
		var acceptTopId = $("#acceptTopId").val();
		var acceptAopId =$("#acceptAopId").val();
		var idRecordType = $("#id_recordType").val();
		var outPrice = parseFloat(price);
		$("#record-price-form-id input[name=outPrice]").val(outPrice);
		if(outPrice>0){
			$("#out-price-id").val(Math.round(outPrice*100)/100)
			if(idRecordType!=7 &&  isAppFlight==1 && acceptTopId && acceptAopId && parseFloat(outPrice)>0){
				$("#submitButton").show();
			}else if(idRecordType!=7 && isAppFlight!=1 && acceptAopId && parseFloat(outPrice)>0){
				$("#submitButton").show();
			}else if(idRecordType==7 && isAppFlight==1 && acceptTopId && parseFloat(outPrice)>0){
				$("#submitButton").show();
			}
		}else{
			$("#submitButton").hide();
			$("#out-price-id").val(0);
			//$.jBox.tip("外报价格式有误","提示");
		}
	});
	
	$("#out-price-id").click(function(){
		var out = $("#out-price-id").val();
		if(out){
			$("#out-price-id").val(rmoney(out));
		}
	});

	/**
	 * 提交确定询价
	 */
	$("#record-price-form-id").click(function(){
		 $.jBox.confirm("确认报价，您确定吗？","提示",function(v, h, f){
			 if(v=="ok"){
				 epriceInfo.ajax.replyPriceAjax();
			 }else{
				 
			 }
		 });
	});
	/**
	$("span[class=moneyFormat]").each(function(){
		var t = false;
		var value = $(this).html();
		var value1 = value.replace("¥", "") ;
		if(value!=value1){
			t = true;
		}
		value = milliFormat(getTrim(value1),2);
		if(t){
			value = "¥"+value;
		}
		$(this).html(value);
		
	});*/
	
	
	//渲染接待计调报价明细
	$("#admit-eprice-list-id span[name=reply-price-detail-name]").each(function(){
		//{"adult":{"price":"10","sum":0},"child":{"price":"20","sum":0},"specialPerson":{"price":"30","sum":0},"other":[{"title":"优惠价1","price":200,"sum":200}]}
		var json = $.parseJSON($(this).html());
		var html = "";
		html += '<div class="kong"></div>';
		html += '<span>成人总价：¥ '+milliFormat(json.adult.price,2)+"x"+json.adult.sum+'</span>';
		html += '<span>儿童总价：¥ '+milliFormat(json.child.price,2)+"x"+json.child.sum+'</span>';
		html += '<span>特殊人群总价：¥ '+milliFormat(json.specialPerson.price,2)+"x"+json.specialPerson.sum+'</span>';
		
		var other = json.other;
		var t;
		for (var i = 0,len = other.length; i < len; i++) {
			t = other[i];
			html += '<span>'+t.title+'：¥ '+t.price+"x"+t.sum+'</span>';
		}
		
		$(this).parent().append(html);
		
	});
	
	
	//渲染接待计调报价明细
	$("#traffic-eprice-list-id span[name=reply-price-detail-name]").each(function(){
		//{"adult":{"price":"10","sum":0},"child":{"price":"20","sum":0},"specialPerson":{"price":"30","sum":0},"other":[{"title":"优惠价1","price":200,"sum":200}]}
		var json = $.parseJSON($(this).html());
		var html = "";
		html += '<div class="kong"></div>';
		html += '<span>成人总价：¥ '+milliFormat(json.adult.price,2)+"x"+json.adult.sum+'</span>';
		html += '<span>儿童总价：¥ '+milliFormat(json.child.price,2)+"x"+json.child.sum+'</span>';
		html += '<span>特殊人群总价：¥ '+milliFormat(json.specialPerson.price,2)+"x"+json.specialPerson.sum+'</span>';
		
		
//		var other = json.other;
//		var t;
//		for (var i = 0,len = other.length; i < len; i++) {
//			t = other[i];
//			html += '<span>'+t.title+'：¥ '+t.price*t.sum+'</span>';
//		}
		
		$(this).parent().append(html);
		
	});
	
	$("div[name=reply-info-div]").each(function(){
		var st = $(this).attr("vstat");
		if(st<2){
			$(this).html('<span class="tdred">待回复</span>');
		};
	});
	
	epriceInfo.service.totalPricesShow();
	
	epriceInfo.service.onrefresh();
	
});

var epriceInfo = {
		travelRequirementsInputs :travelRequirements.formData,
		bigClass :travelRequirements.bigClass,
		
		travelRequirementsJson : null,
		
		dao : {
			/**
			 * 生成普通接待要求元素 html string，
			 * @author lihua.xu
			 * @时间 2014年9月28日
			 * @param {} m 接待要求内容
			 * @return {html string}
			 */
			travelRequirement : function(m,name){
				var html1 = '<div class="seach25 seach100"><p>&nbsp;&nbsp;&nbsp;&nbsp;'+name+'：</p><p class="seach_r">';
				 
				var msg = "(无)";
				if(m.option_0){
					html = epriceInfo.dao.travelRequirementOption(m.option_0);
					if(html!=""){ msg = ""; html1+=html;}
				}
				if(m.option_1){
					html = epriceInfo.dao.travelRequirementOption(m.option_1);
					if(html!=""){ msg = ""; html1+=html;}
				} 
				if(m.option_2){
					html = epriceInfo.dao.travelRequirementOption(m.option_2);
					if(html!=""){ msg = ""; html1+=html;}
				} 
				if(m.option_3){
					html = epriceInfo.dao.travelRequirementOption(m.option_3);
					if(html!=""){ msg = ""; html1+=html;}
				} 
				if(m.option_4){
					html = epriceInfo.dao.travelRequirementOption(m.option_4);
					if(html!=""){ msg = ""; html1+=html;}
				} 
				if(m.option_5){
					html = epriceInfo.dao.travelRequirementOption(m.option_5);
					if(html!=""){ msg = ""; html1+=html;}
				} 
				if(m.option_6){
					html = epriceInfo.dao.travelRequirementOption(m.option_6);
					if(html!=""){ msg = ""; html1+=html;}
				} 
				if(m.option_7){
					html = epriceInfo.dao.travelRequirementOption(m.option_7);
					if(html!=""){ msg = ""; html1+=html;}
				} 
				
				html1 += msg+'</p></div>';
				
				return html1;
			},
			
			travelRequirementOption : function(option){
				var htmls="";
				if(option.check=="unchecked"){
					return '';
				}
				
				if(option.title && option.title!="undefined"){
					htmls += '<span class="seach_check">'+option.title;
				}else{
					htmls += '<span class="seach_check">';
				}
				if(option.info){
					if(option.info.value){
						htmls += '-';
						if(option.info.prevname){
							htmls += option.info.prevname;
						}
						htmls += option.info.value;
						if(option.info.sufname){
							htmls += option.info.sufname;
						}
					}
				}
				htmls += '</span>';
				return htmls;
			},
			
			/**
			 * 生成其他接待要求元素 html string，
			 * @author lihua.xu
			 * @时间 2014年9月28日
			 * @param {} m 其他接待要求列表
			 * @return {html string}
			 */
			otherRequirements : function(m){
				if(m==null || m.length==0){
					return "";
				}
				var t = [];
				for (var i = 0,len=m.length; i < len; i++) {
					t[i] = m[i].value;
				}
				var html = '<div class="seach25 seach100"><p>其他要求：</p><p class="seach_r">';
					html += '<span class="seach_check">';
					html += t.join(",");
					html += '</span></p></div>';
				return html;
			
			},
			
			/**
			 * 生成询价要求元素 html string，
			 * @author lihua.xu
			 * @时间 2014年9月28日
			 * @param {} m 询价要求列表
			 * @return {html string}
			 */
			epriceRequirements : function(m){
				if(m==null || m.length==0){
					return "";
				}
				var t = [];
				for (var i = 0,len=m.length; i < len; i++) {
					t[i] = m[i].value;
				}
				var html = '<div class="seach25 seach100"><p>询价要求：</p><p class="seach_r">';
					html += '<span class="seach_check">';
					html += t.join(",");
					html += '</span></p></div>';
				return html;
			
			}
		},
		
		service : {
			
			/**
			 * 生成普通接待要求元素 jquery dhtml，
			 * @author lihua.xu
			 * @时间 2014年9月28日
			 * @param {} m 接待要求内容
			 * @return {jquery dhtml}
			 */
			travelRequirement : function(m,name){
				var $html = $(epriceInfo.dao.travelRequirement(m,name));
					
				return $html;
			},
			
			/**
			 * 生成其他接待要求元素 query dhtml，
			 * @author lihua.xu
			 * @时间 2014年10月8日
			 * @param {} m 其他接待要求列表
			 * @return {query dhtml}
			 */
			otherRequirements : function(m){
				var $html = $(epriceInfo.dao.otherRequirements(m));
					
				return $html;
			},
			
			/**
			 * 生成询价要求元素 query dhtml，
			 * @author lihua.xu
			 * @时间 2014年10月8日
			 * @param {} m 询价要求列表
			 * @return {query dhtml}
			 */
			epriceRequirements : function(m){
				var $html = $(epriceInfo.dao.epriceRequirements(m));
					
				return $html;
			},
			
			/**
			 * 初始化渲染询价接待要求
			 * @author lihua.xu
			 * @时间 2014年10月8日
			 */
			travelRequirementShow : function(){
//				alert(epriceInfo.travelRequirementsInputs);
//				alert(epriceInfo.bigClass);
				
				var j = $.trim($("#travel-requirements-span-id").html());
	
				epriceInfo.travelRequirementsJson = $.parseJSON(j);
				
				var json = epriceInfo.travelRequirementsJson;
				if(json==null){
					return;
				}
				
				
				var o;
				var $div = $("#travel-requirements-div-id");
				
				
				//其他接待要求
				var otherRequirements = json.otherRequirements;
				delete json.otherRequirements;
				//询价要求
				var	 epriceRequirements =  json.epriceRequirements;
				delete json.epriceRequirements;
				
				
				var jsonBigClass = epriceInfo.bigClass;
				var jsonRequestInputs = epriceInfo.travelRequirementsInputs;
				for(var i =0;i<jsonBigClass.length;i++){
					var t = true;
					var $bigClassDiv = $('<div name="'+jsonBigClass[i].code+'"><strong>'+jsonBigClass[i].name  +'</strong><div class="kong"></div></div>');
					for(var j=0; j<jsonRequestInputs.length;j++ ){
						if((jsonBigClass[i].code==""&&typeof(jsonRequestInputs[j].bigClass) == "undefined")
								||(jsonBigClass[i].code==jsonRequestInputs[j].bigClass)){
							o = json[jsonRequestInputs[j].code];
//							if(getJsonLength(o)>0){
//								t = true;
//							}
							if(o){
								o.code = jsonRequestInputs[j].code;
								var $html = epriceInfo.service.travelRequirement(o,jsonRequestInputs[j].name);
								$bigClassDiv.append($html);
							}
						}
					}
					if(t){
						$div.append($bigClassDiv);
					}
				}
				
				
				
				
//				for(var k in json){
//					o = json[k];
//					o.code = k;
//					$div.append(epriceInfo.service.travelRequirement(o));
//				}
//				
				$div.append(epriceInfo.service.otherRequirements(otherRequirements));
				$div.append(epriceInfo.service.epriceRequirements(epriceRequirements));
				
			},
			
			/**
			 * 展示询价交通路线中的一些类型信息，如：出发时刻、舱位等级、舱位
			 * @author lihua.xu
			 * @时间 2014年10月8日
			 */
			trafficLinesShow : function(){
				$("#epirce-traffic-line-list-id div[name=eprice-traffic-line-name]").each(function(){
					var $d = $(this).find("p[name=startTimeType]");
					var v = $d.attr("vtype");
					if(v==1){
						$d.html("早");
					}else if(v==2){
						$d.html("中");
					}else{
						$d.html("晚");
					}
					
					$d = $(this).find("p[name=aircraftSpaceLevel]");
					v = $d.attr("vtype");
					if(v==1){
						$d.html("头等舱");
					}else if(v==2){
						$d.html("公务舱");
					}else if(v==3){
						$d.html("经济舱");
					}else{
						$d.html("不限");
					}
					
					$d = $(this).find("p[name=aircraftSpace]");
					v = $d.attr("vtype");
					if(v==0){
						$d.html("不限");
					}
				});
			},
			
			/**
			 * 处理展示最终询价记录价格情况
			 * @author lihua.xu
			 * @时间 2014年10月8日
			 */
			totalPricesShow : function(){
				var namestr = null;
				if(record.type!=7){//单团询价
                    	if(record.isAppFlight==1){//申请机票
                    		if(record.aoperatorTotalPrice=='' && record.toperatorTotalPrice==''){//
                    			//namestr = '待报价';
                    			
                    		}else{
                    			namestr = record.operatorPrice;
                    		}
                    	}else{
                    		if(record.toperatorTotalPrice!=''){
                    			namestr = record.operatorPrice ;
                    		}else{
                    			//namestr = '<span class="tdred">待报价</span>';
                    		}
                    	
                    	}
                    
                }else{//机票询价
                	if(record.toperatorTotalPrice!=''){
                			namestr = record.toperatorTotalPrice;
                			
                	}else{
                			//namestr = '<span class="tdred">待报价</span>';
                	}
                	
                }
                
                $("#settle-price-span-id").html("待报价");
                if(namestr==null){
                	$("#operator-price-span-id").html("待报价");
                }else{
                	//$("#operatorPrice_id").val(namestr);
                	$("#operator-price-span-id").html("¥ "+milliFormat(namestr,2));
                	if(record.outPrice!=''){
                		$("#settle-price-span-id").html("¥ "+milliFormat((record.outPrice-namestr),2));
                	}
                }
                
			},
			
			/**
			 * 选择不同报价，并且将报价放入“成本价”
			 * 
			 *
			hooseReply : function(){
				$("#admit-eprice-list-id input[name=chosePriceA]").click(function(){
					//alert($(this).attr("name"));
					var textaopPrice = $(this).parent().next().find("em.red20").text();// 获取总计价格
					var aopPrice = textaopPrice==null||textaopPrice==""?"":rmoney(textaopPrice);
					$("#acceptAopId").val($(this).val());
					$("#acceptAopPrice").val(aopPrice);
					var isTraffic = $("#id_isAppFlight").val();
					if(isTraffic==1){
						var topPrice = $("#acceptTopPrice").val();
						if(topPrice!=null&&topPrice!=""&&aopPrice!=null&&aopPrice!=""){
							var sumPrice =Number((Number(aopPrice)+Number(topPrice)).toFixed(2));
							$("#operatorPrice").val(sumPrice);
							$("#operator-price-span-id").empty();
							$("#operator-price-span-id").text(milliFormat(sumPrice,2));
						}else{
							$("#operator-price-span-id").empty();
							$("#operator-price-span-id").text("待报价");
							$("#operatorPrice").val("");
						}
					}else{
						$("#operator-price-span-id").empty();
						$("#operator-price-span-id").text(aopPrice==null||aopPrice==""?"待报价":milliFormat(aopPrice,2));
						$("#operatorPrice").val(aopPrice);
				   }
					epriceInfo.service.countReply();
				}),
				$("#traffic-eprice-list-id input[name=chosePriceB]").click(function(){
					var texttopPrice = $(this).parent().next().find("em.red20").text();// 获取总计价格
					var type = 	$("#id_recordType").val();
					var topPrice = texttopPrice==null||texttopPrice==""?"":rmoney(texttopPrice);
					$("#acceptTopId").val($(this).val());
					$("#acceptTopPrice").val(topPrice);
//					$("#out-price-id").removeAttr("readonly");
					if(type==1||type==3||type==4||type==5){
						var aopPrice = $("#acceptAopPrice").val();
						if(topPrice!=null&&topPrice!=""&&aopPrice!=null&&aopPrice!=""){
							var sumPrice =Number(aopPrice)+Number(topPrice);
							$("#operatorPrice").val(sumPrice);
							$("#operator-price-span-id").empty();
							$("#operator-price-span-id").text(milliFormat(sumPrice,2));
						}else{
							$("#operator-price-span-id").empty();
							$("#operator-price-span-id").text("待报价");
							$("#operatorPrice").val("");
						}
					}else if(type==7){
						$("#operator-price-span-id").empty();
						$("#operator-price-span-id").text(topPrice==null||topPrice==""?"待报价":milliFormat(topPrice,2));
						$("#operatorPrice").val(topPrice);
					}
					epriceInfo.service.countReply();
				});
			},
			*/
			
			/**
			 * 20150518 新增 多币种成本价 计算
			 */	
			hooseReply : function(){
				$("#admit-eprice-list-id input[name=chosePriceA]").click(function(){
					epriceInfo.service.aop($(this));
				}),
				$("#traffic-eprice-list-id input[name=chosePriceB]").click(function(){
					epriceInfo.service.top($(this));
				});
			},
			/**
			 * 地接计调成本价计算
			 */
			aop: function(obj){
				var topprice = "";
				// 获取当前选项的成人报价
				var adultprice  = "";
				$(obj).parents("div.inquiry_choseprice").find("em.adultmark").each(function(){
					var mark  = $(this).html(); // 币种符号
					var price  = $(this).next().html(); // 报价
					adultprice=adultprice+" "+mark+" "+price;
				});
				if($.trim(adultprice)){
					topprice = topprice+"<br/>地接报价<br/>  成人 "+adultprice;
				}
				// 获取当前选项的儿童报价
				var childprice  = "";
				$(obj).parents("div.inquiry_choseprice").find("em.childmark").each(function(){
					var mark  = $(this).html(); // 币种符号
					var price  = $(this).next().html(); // 报价
					childprice=childprice+" "+mark+" "+price;
				});
				if($.trim(childprice)){
					topprice = topprice+"<br/>  儿童 "+childprice;
				}
				// 获取当前选项的特殊人群报价
				var specialprice  = "";
				$(obj).parents("div.inquiry_choseprice").find("em.specialmark").each(function(){
					var mark  = $(this).html(); // 币种符号
					var price  = $(this).next().html(); // 报价
					specialprice=specialprice+" "+mark+" "+price;
				});
				if($.trim(specialprice)){
					topprice = topprice+"<br/>  特殊人群 "+specialprice;
				}
				// 只有当计调已经完成报价时，才能选择
				if(topprice){
					$("#acceptAopId").val($(obj).val());
				}else{
					$.jBox.tip("该计调尚未报价","警告");
					$(obj).removeAttr("checked");
				}
				//var isTraffic = $("#id_isAppFlight").val();
				$("#operator-price-span-id").empty();
				$("#operator-price-span-id").html(topprice==null||topprice==""?"待报价":topprice);
			},
			/**
			 * 机票计调成本价计算
			 */
			top:function(obj){
				var topprice = "";
				// 获取当前选项的成人报价
				var adultprice  = "";
				$(obj).parents("div.inquiry_choseprice").find("em.adultmark").each(function(){
					var mark  = $(this).html(); // 币种符号
					var price  = $(this).next().html(); // 报价
					adultprice=adultprice+" "+mark+" "+price;
				});
				if($.trim(adultprice)){
					topprice = topprice+"<br/>机票报价<br/>  成人 "+adultprice;
				}
				// 获取当前选项的儿童报价
				var childprice  = "";
				$(obj).parents("div.inquiry_choseprice").find("em.childmark").each(function(){
					var mark  = $(this).html(); // 币种符号
					var price  = $(this).next().html(); // 报价
					childprice=childprice+" "+mark+" "+price;
				});
				if($.trim(childprice)){
					topprice = topprice+"<br/>  儿童 "+childprice;
				}
				// 获取当前选项的特殊人群报价
				var specialprice  = "";
				$(obj).parents("div.inquiry_choseprice").find("em.specialmark").each(function(){
					var mark  = $(this).html(); // 币种符号
					var price  = $(this).next().html(); // 报价
					specialprice=specialprice+" "+mark+" "+price;
				});
				if($.trim(specialprice)){
					topprice = topprice+"<br/>  特殊人群 "+specialprice;
				}
				//var type = $("#id_recordType").val();//类型 单团or 机票 
				// 只有当所选计调完成报价后才能选择
				if(topprice){
					$("#acceptTopId").val($(obj).val()); // 选中的 机票回复Id
				}else{
					$.jBox.tip("该计调尚未报价","警告");
					$(obj).removeAttr("checked");
				}
				$("#operator-price-top-span-id").empty();
				$("#operator-price-top-span-id").html(topprice==null||topprice==""?"待报价":topprice);
			},
			/**
			 * 通过“成本价”，“外包价”，计算“结算价”
			 * 20150518 因为出现多币种，结算价 = 外报价-成本价 这个公式无法计算，结算价暂时去除
			 
			countReply : function(){
				var str ;
				var out = $("#out-price-id").val();
				var all = 0;
				var op = $("#record-price-form-id input[name=operatorPrice]").val();
				if(out){
					out = rmoney(out);
					$("#out-price-id").val(milliFormat(out,2));
				}
				$("#record-price-form-id input[name=outPrice]").val(out);
				if(out&&op){
					all =  doubleSub(out,op);
//					all = Number(out,3)-Number(op,3);
					$("#settle-price-span-id").empty();
					str = '<em class="red20">¥'+milliFormat(all,2)+'</em>';
					// 将价格结果放入隐藏的“确定询价”表单
					$("#record-price-form-id input[name=operatorPrice]").val(op);
					$("#record-price-form-id input[name=outPrice]").val(out);
					$("#submitButton").show();
				}else{
					str="待报价";
					$("#submitButton").hide();
				}
				$("#settle-price-span-id").html(str);
//				$("#out-price-id").attr("readonly","readonly");
			},
			*/
			/**
			 * 刷新的时候用的，防止成本价不显示
			 */
			onrefresh : function(){
				var op = $("#record-price-form-id input[name=operatorPrice]").val();
				if(op){
					$("#operator-price-span-id").empty();
					$("#operator-price-span-id").text(milliFormat(op,2));
				}
				//epriceInfo.service.countReply();
			},
			
			
			
			/**
			 * 确定询价表单数据序列化成可提交形式
			 */
			replyPriceFormSerialize : function(){
				var param = "prid="+$("#record-price-form-id input[name=prid]").val();
				//param += "&operatorPrice="+$("#record-price-form-id input[name=operatorPrice]").val();
				param += "&outPrice="+$("#record-price-form-id input[name=outPrice]").val();
				param += "&status="+$("#record-price-form-id input[name=status]").val();
				param += "&acceptAopId="+$("#record-price-form-id input[name=acceptAopId]").val();
				param += "&acceptTopId="+$("#record-price-form-id input[name=acceptTopId]").val();
				
//				alert(param);
				return param;
			}
		},
		
		ajax : {
			/**
			 * 确定询价
			 */
			replyPriceAjax : function(){
				var the_param = epriceInfo.service.replyPriceFormSerialize();
				$.ajax({
					type : "POST",
					url : contextPath+"/eprice/manager/ajax/project/replyPrice",
					data : the_param,
					dataType : "text",
					success : function(html){
						var json;
						try{
							json = $.parseJSON(html);
						}catch(e){
							json = {res:"error"};							
						}
						
						if(json.res=="success"){
							jBox.tip("提交成功", 'info');
							history.back();
						}else if(json.res=="data_error"){
							jBox.tip("输入数据不正确", 'error');
						}else{
							jBox.tip(json.res, 'error');
						}
					}
				});
			}
		}
		
		
};

function getTrim(str){
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

function doubleSub(num1,num2){
	return Math.round((parseFloat(num1)*1000 - parseFloat(num2)*1000))/1000;
}

function doubleAdd(num1,num2){
	return Math.round((parseFloat(num1)*1000 + parseFloat(num2)*1000))/1000;
}


function getJsonLength(jsonData){
	var jsonLength = 0;
	for(var item in jsonData){
		jsonLength++;
	}
	return jsonLength;
	}