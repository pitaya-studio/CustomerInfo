<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<% String path = request.getContextPath();%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	    <title>订单-改价申请</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<!-- 页面左边和上边的装饰 -->
		<meta name="decorator" content="wholesaler" />
		<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
		<script type="text/javascript" src="${ctxStatic}/review/changePrice/singleGroup/changePrice.js"></script>
        <script type="text/javascript" src="${ctxStatic}/review/changePrice/es5-shim.js"></script>
		<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/changePrice.css" />
		<script type="text/javascript" src="${ctxStatic}/jquery/jquery.nicescroll.min.js"></script>
		<script type="text/javascript">

            var currencyArr = [];
            <c:forEach items="${currencylist}" var="currency">
                var currency = new Object();
                currency.currencyId = ${currency.id};
                currency.currencyName = '${currency.currencyName}';
                currency.currencyMark = '${currency.currencyMark}';
                currency.convertLowest = '${currency.convertLowest}';
                currencyArr.push(currency);
            </c:forEach>

            //混动条
            $(document).ready(function() {
                Object.assignObj=function(target, firstSource) {
                    "use strict";
                    if (target === undefined || target === null)
                        throw new TypeError("Cannot convert first argument to object");
                    var to = Object(target);
                    for (var i = 1; i < arguments.length; i++) {
                        var nextSource = arguments[i];
                        if (nextSource === undefined || nextSource === null) continue;
                        var keysArray = Object.keys(Object(nextSource));
                        for (var nextIndex = 0, len = keysArray.length; nextIndex < len; nextIndex++) {
                            var nextKey = keysArray[nextIndex];
                            var desc = nextSource.hasOwnProperty(nextKey);
                            var flag=to.hasOwnProperty(nextKey);
                            if (desc)
                                if(flag){
                                    to[nextKey] += nextSource[nextKey];
                                }else{
                                    to[nextKey] = nextSource[nextKey];
                                }

                        }
                    }
                    return to;
                }
                Object.minusObj=function(target, firstSource) {
                    "use strict";
                    if (target === undefined || target === null)
                        throw new TypeError("Cannot convert first argument to object");
                    var to = Object(target);
                    for (var i = 1; i < arguments.length; i++) {
                        var nextSource = arguments[i];
                        if (nextSource === undefined || nextSource === null) continue;
                        var keysArray = Object.keys(Object(nextSource));
                        for (var nextIndex = 0, len = keysArray.length; nextIndex < len; nextIndex++) {
                            var nextKey = keysArray[nextIndex];
                            var desc = Object.hasOwnProperty(nextSource, nextKey);
                            var flag=to.hasOwnProperty(nextKey);
                            if (desc !== undefined && desc.enumerable)
                                if(flag){
                                    to[nextKey] -= nextSource[nextKey];
                                    if(!to[nextKey]){
                                        var x=nextKey;
                                        var x= delete to[nextKey.toString()];
                                    }

                                }else{
                                    to[nextKey] = -nextSource[nextKey];
                                }
                        }
                    }
                    return to;
                }
                var testObj={};
                //改后应收价
                (function countTo(){
					var $tableTr=$("#toursChangePrice>tr");
					var length=$tableTr.length;
					var parentThis="";
					$tableTr.each(function(i,a){
						if(i==length-1) return;
						var nowPr=[];//币种
						var nowIce=[];//金额
						var returnObj={};
						var nowPriceObj={};//当前应收价格
						if($(this).find("div.countMoney").length>0){
							$(this).find("div.countMoney").each(function(){
								var changePriceObj={}; //改变的幅度
								changePriceObj[$(this).attr("data-curmark")]=Number($(this).attr("data-pricost"));
								returnObj=Object.assignObj(returnObj,changePriceObj);
								parentThis=$(this).parents(".sale_off_parent");//悬浮框所在的TD
								var oldThis=parentThis .prev();//当前应收价所在的TD
								//如果nowPriceObj没有取值（第一次循环）那么取值，否则，不再重复取值
								oldThis.find("span:even").each(function(){
									nowPr.push($(this).text());
								});
								oldThis.find("span:odd").each(function(){
									nowIce.push(Number($(this).text()));
								})
								$.each(nowPr,function(i,v){
									nowPriceObj[v]=nowIce[i];
								})
							})
							returnObj=Object.assignObj(nowPriceObj,returnObj);
							var innerHtml="";
							for(var _a in returnObj){
								innerHtml+='<span>'+_a+'</span><span class="red_c fbold"  flag="original">'+returnObj[_a].toFixed(2)+'</span>+';
							}
							innerHtml=innerHtml.slice(0,-1);
							if(parentThis.length>0)  parentThis.find("div.parent_div").empty().append(innerHtml);
						}
					})
                })();

                // 原始应收总价
                (function countOriginal(){
                    var changePriceObj={}; //改变的幅度
                    var innerHtml="";
                    var returnObj={};
                    var self=$("#toursChangePrice");
                    $("#toursChangePrice").find("tr").each(function(i,v){
                        var nowPriceObj={};//当前应收价格
                        var nowPr=[];//币种
                        var nowIce=[];//金额
                        var oldThis = $(v).find("td").eq(3);
                        oldThis.find("span:even").each(function(){
                            nowPr.push($(this).text());
                        });
                        oldThis.find("span:odd").each(function(){
                            nowIce.push(Number($(this).text()));
                        })
                        $.each(nowPr,function(i,v){
                            nowPriceObj[v]=nowIce[i];
                        })
                        returnObj=Object.assignObj(returnObj,nowPriceObj);
                        var innerHtml="";

                    })
                    for(var _a in returnObj){
                        innerHtml+='<span>'+_a+'</span> <span class="red_c fbold"  flag="original">'+returnObj[_a].toFixed(2)+'</span>+';
                    }
                    innerHtml=innerHtml.slice(0,-1);
                    $("#totalOriginal").empty().append(innerHtml);
                })();

				// 当前应收总价
				(function countOriginal(){
					var changePriceObj={}; //改变的幅度
					var innerHtml="";
					var returnObj={};
					var self=$("#toursChangePrice");
					$("#toursChangePrice").find("tr").each(function(i,v){
						var nowPriceObj={};//当前应收价格
						var nowPr=[];//币种
						var nowIce=[];//金额
						var oldThis = $(v).find("td").eq(4);
						oldThis.find("span:even").each(function(){
							nowPr.push($(this).text());
						});
						oldThis.find("span:odd").each(function(){
							nowIce.push(Number($(this).text()));
						})
						$.each(nowPr,function(i,v){
							nowPriceObj[v]=nowIce[i];
						})
						returnObj=Object.assignObj(returnObj,nowPriceObj);
						var innerHtml="";

					})
					for(var _a in returnObj){
						innerHtml+='<span>'+_a+'</span> <span class="red_c fbold"  flag="original">'+returnObj[_a].toFixed(2)+'</span>+';
					}
					innerHtml=innerHtml.slice(0,-1);
					$("#totalNowtime").empty().append(innerHtml);
				})();

				// 改后应收总价
				(function countOriginal(){
					var changePriceObj={}; //改变的幅度
					var innerHtml="";
					var returnObj={};
					var self=$("#toursChangePrice");
					$("#toursChangePrice").find("tr").each(function(i,v){
						var nowPriceObj={};//改后应收价格
						var nowPr=[];//币种
						var nowIce=[];//金额
						var oldThis = $(v).find("td").eq(5);
						oldThis.find("span:even").each(function(){
							nowPr.push($(this).text());
						});
						oldThis.find("span:odd").each(function(){
							nowIce.push(Number($(this).text()));
						})
						$.each(nowPr,function(i,v){
							nowPriceObj[v]=nowIce[i];
						})
						returnObj=Object.assignObj(returnObj,nowPriceObj);
						var innerHtml="";

					})
					for(var _a in returnObj){
						innerHtml+='<span>'+_a+'</span> <span class="red_c fbold"  flag="original">'+returnObj[_a].toFixed(2)+'</span>+';
					}
					innerHtml=innerHtml.slice(0,-1);
					$("#totalFuture").empty().append(innerHtml);
				})();

				$("#totalBefore").html($("#totalNowtime").html().replace("当前应收总价：",""));
				$("#totalAfter").html($("#totalFuture").html());


                $(".sale_off_parent").hover(function () {
                            var $this = $(this).children(".sale_off");
                            if ($this.text().trim() == "") {
                                $this.hide();
                                $(this).children(".arrow").hide();
                            } else {
                                $this.show();
                                $(this).children(".arrow").show();
                                $(".sale_off").getNiceScroll().resize();
                            }
                        }, function () {
                            $(this).children(".sale_off").hide();
                            $(this).children(".arrow").hide();
                            $(this).children(".sale_off").children(".arrow").remove();
                            $(".sale_off").getNiceScroll().resize();
                        }
                );
                $(".sale_off ").niceScroll({
                    cursorcolor: "#ccc",//#CC0071 光标颜色
                    cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
                    touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
                    cursorwidth: "5px", //像素光标的宽度
                    cursorborder: "5px", //     游标边框css定义
                    cursorborderradius: "5px",//以像素为光标边界半径
                    autohidemode: false, //是否隐藏滚动条
                    zindex:100
                });

            })
			function jbox_add_other_cost_pop(obj,travelerId) {
				//添加弹出页面要展示出以前添加的数据
				$("#contentTable").find("tbody");
				var oldHtml="";//添加弹出页面要展示出以前添加的数据
				var $this = $(obj);
				var initValue=$this.parent().siblings(".relative").find("div.data_value");
//				var initValue=$this.parent().siblings(".relative").children(".sale_off").children();
				var oldObj={};
				if(initValue.length>0){
					initValue.each(function(index,data){
						var _this= $(this);
						var data_name=_this.attr("data-name");
						var data_number=_this.attr("data-number");
						var data_price=_this.attr("data-price");
						var data_currency=_this.attr("data-currency");
						var data_currencyId=_this.attr("data-currencyId");
						var data_result=_this.attr("data-result");

						var oldTempObj={};
						var _obj=$(this).text();
						var _currency=_obj.slice(_obj.indexOf(":")+1,_obj.indexOf(":")+2);
						var _currencyCount=_obj.slice(_obj.indexOf(":")+2);
						oldHtml+='<tr class="tr_add">';
						oldHtml+='<td class="tc">';
						oldHtml+='<input type="text" maxlength="50"  class="name_length"  value="'+data_name+'"  onkeyup="nospace(this)"></td>';
						oldHtml+='<td class="tc"><select class="currencyId jbox-width100" onclick="changeCur(this)">';
                        for (var i = 0; i < currencyArr.length; i++) {
                            var c = currencyArr[i];
                            oldHtml += '<option value="'+c.currencyId+'"';
                            if (c.currencyId == data_currencyId) {
                                oldHtml += 'selected="selected"';
                            } else {

                            }
                            oldHtml += '>' + c.currencyName + '</option>';
                        }
                        oldHtml	+= '</select>'
								+ '</td>';
						oldHtml+='<td class="tc">'
								+'<input class="dataChange number_input" name="num" type="text" onafterpaste="calculateCost(this)" maxlength="5" onkeyup="calculateCost(this)" value="'+data_number+'"></td>'
								+'<td class="tc">'
								+'<input class="dataChange price_input"  name="price" type="text" onkeyup="calculateCost(this)" onafterpaste="calculateCost(this)" maxlength="12" value="'+data_price+'"></td>'
								+'<td class="tr" name="result">'
								+'<em class="currency">'+data_currency+'</em><em class="result">'+data_result+'</em>'
								+'</td>'
								+'<td class="tc">';
						if(index==0){
							oldHtml+='<a href="javascript:void(0)" onclick="addone_img(this)"><em class="add_img_green"></em></a>' +
									'&nbsp;&nbsp;<a href="javascript:void(0)" onclick="deletethis(this)"><em class="add_img_red"></em></a>' +
									'</td></tr>';
						}else{
							oldHtml+='<a href="javascript:void(0)" onclick="deletethis(this)"><em class="add_img_red"></em></a></td></tr>';
						}
					})
					$("#contentTable").find("tbody").empty().append(oldHtml);
//            $("#contentTable").find("tbody tr:first td").eq(5).html('<a href="javascript:void(0)" onclick="addone_img(this)"><em class="add_img_green"></em></a>');
				}else{
					var initHtml='<tr class="tr_add"><td class="tc"><input type="text" maxlength="50"  class="name_length" onkeyup="nospace(this)"></td>'+
							'<td class="tc">'+
							'<select class="currencyId jbox-width100" onclick="changeCur(this)">'+
							'<c:forEach items="${currencylist}" var="currency">'+
							'<option value="${currency.id}">${currency.currencyName}</option>'+
							'</c:forEach>'+
							'</select>'+
							'</td>'+
							'<td class="tc">'+
							'<input class="dataChange number_input" name="num" type="text" onafterpaste="calculateCost(this)" maxlength="5" onkeyup="calculateCost(this)" value="1">'+
							'</td>'+
							'<td class="tc">'+
							'<input class="dataChange price_input"  name="price" type="text" onkeyup="calculateCost(this)" onafterpaste="calculateCost(this)"maxlength="12"> </td>'+
							'<td class="tr" name="result">'+
							'<em class="currency">￥</em><em class="result">0.00</em></td>'+
							'<td class="tc">'+
							'<a href="javascript:void(0)" onclick="addone_img(this)"><em class="add_img_green"></em></a>'+
							'&nbsp;&nbsp;<a href="javascript:void(0)" onclick="deletethis(this)"><em class="add_img_red"></em></a>'+'</td></tr>';
					//
					$("#contentTable").find("tbody").empty().append(initHtml);
				}

				$pop = $.jBox($("#add_other_cost").html(), {
					title    : "添加", buttons: {'取消': 0,'确认':1}, submit: function (v, h, f) {
						if (v == "1") {
							$("#contentTable").find("tbody");
							var oldHtml="";//添加弹出页面要展示出以前添加的数据
							var $this = $(obj);//添加按钮

							var DQYSJ=$this.parent().siblings(".relative").prev().find("span:even");
							var initValue=$this.parent().siblings(".relative").children(".sale_off").children();
							var oldObj={};
							var tr_length = $pop.find(".tr_add").length;
							var name = $pop.find(".name_length");
							var currency = $pop.find(".currency");
							var currencyId = $pop.find(".currencyId");
							var result = $pop.find(".result");
							var number= $pop.find(".number_input");
							var price= $pop.find(".price_input");
							var parent_div=$this.parent().siblings(".relative").children(".parent_div");
							var div;
							var str=[];
							var innerHtml1="";
							var innerHtml2="";
							if(tr_length > 0) {
								innerHtml2+='<div class="payfor-otherDiv"><table width="100%" class="table_fixed border_t">'
										+'<thead class="thead_border_bottom"><tr>'
										+'<td width="17%" class="tl">'
										+'<div style="margin:0 5px 0 10px;word-break: break-all;">序号</div></td>'
										+'<td width="25%" class="tl">'
										+'<div style="margin:0 5px 0 10px;word-break: break-all;">费用名称</div></td>'
										+'<td width="13%" class="tl">'
										+'<div style="margin:0 5px;word-break: break-all;" >数量</div></td>'
										+'<td width="20%" class="tr">'
										+'<div style="margin:0 5px;word-break: break-all;">单价</div></td>'
										+'<td width="30%" class="tr">'
										+'<div style="margin:0 10px;word-break: break-all;">总计</div></td></tr></thead><tbody class="new_tbody">';
								for (var i = 0; i < tr_length; i++) {
									if(name.eq(i).val()==""||number.eq(i).val()==""||price.eq(i).val()==""){
										$.jBox.tip("请输入完整信息！", 'error');
										return false;
									}else if(price.eq(i).val().replace(/\./g,"").replace(/-/g,"")==""){
										$.jBox.tip("请输入正确数字！", 'error');
										return false;
									}else if(number.eq(i).val().replace(/0/g,"")=="" || number.eq(i).val().charAt(0) == '0'){
										$.jBox.tip("请重新输入数量！", 'error');
										return false;
									}else if(price.eq(i).val().replace(/0/g,"")=="" || (price.eq(i).val().charAt(0) == '0'&&price.eq(i).val().charAt(1) != '.')){
										$.jBox.tip("请重新输入单价！", 'error');
										return false;
									}if(result.eq(i).text() > 10000000.00) {
										$.jBox.tip("总计值过大！", 'error');
										return false;
									}else{
										currency.eq(i).text()//币种符号
										result.eq(i).text()//数值
										var oldTempObj={};
										var preAddValue = Number(result.eq(i).text());
										var afterAddValue = getTravelerChargeRate(currency.eq(i).text()=='￥'?"¥":currency.eq(i).text(), preAddValue);
										oldTempObj[currency.eq(i).text()=='￥'?"¥":currency.eq(i).text()]=afterAddValue;
										oldObj=Object.assignObj(oldObj,oldTempObj);

										innerHtml2+='<tr><td class="t1 SortId"><div style="margin:0 5px 0 10px;word-break: break-all;">'+(i+1)+'</div></td><td class="t1"><div style="margin:0 5px 0 10px;word-break: break-all;">'+name.eq(i).val()+'</div></td><td class="t1"><div style="margin:0 5px;word-break: break-all;">'+number.eq(i).val()+'</div></td><td class="tr"><div style="margin:0 5px;word-break: break-all;">'+currency.eq(i).text()+price.eq(i).val()+'</div></td><td class="tr"><div style="margin:0 10px;word-break: break-all;">'+ currency.eq(i).text() + result.eq(i).text()+'</div></td></tr>';
										innerHtml2+='<div class="data_value" style="display:none" data-travelerId="'+travelerId+'" data-name="'+name.eq(i).val()+'"'+'data-number="'+number.eq(i).val()+'"data-price="'+price.eq(i).val()+'"data-currency="'+currency.eq(i).text()+'"data-currencyId="'+currencyId.eq(i).val()+'"data-result="'+result.eq(i).text()+'">'+name.eq(i).val()+':'+ currency.eq(i).text() + result.eq(i).text()+'</div>';
									}
								}
								innerHtml2+='</tbody>';
								//oldObj  此时为修改的浮动
								//oldPrice  当前应收价
								var oldPrice={};
								DQYSJ.each(function(){
									oldPrice[$(this).text()]=parseFloat($(this).next().text());
								})


								oldPrice=Object.assignObj(oldPrice,oldObj);
								for(var _a in oldPrice){
									innerHtml1+='<span>'+_a+'</span><span class="red_c fbold"  flag="original">'+oldPrice[_a].toFixed(2)+'</span>+';
								}
//                                innerHtml1=innerHtml1.slice(0,-1);
//                                parent_div.empty().append(innerHtml1);
//                                parent_div.next().empty().append(innerHtml2);
//
//								var totalObj={};
//								//算计总价并显示  $this 是当前点击的添加或者修改按钮
//								countToatl($this,totalObj,"totalFuture");
//
//								$("#totalAfter").html($("#totalFuture").html());
								$this.html("修改");
								$("#flag").html("修改");
							}

							innerHtml1=innerHtml1.slice(0,-1);
							parent_div.empty().append(innerHtml1);
							parent_div.next().empty().append(innerHtml2);

                            var totalObj={};
                            //算计总价并显示  $this 是当前点击的添加或者修改按钮
                            countToatl($this,totalObj,"totalFuture");

//                            $("#totalAfter").html($("#totalFuture").html());
							return true;
						}
					},
					width:950,
					loaded:function(){
						$(".jbox-content").css("max-height","400px");
						var partten = /^[0-9]+$/;
						$(document).ready(function() {
							$('input[name=num]').keyup(function () {
								if (!partten.test($(this).val())) {
									var a = $(this).val();
									var b = a.replace(/[^0-9]+/gi, '');
									$(this).val(b)
								}
							});
						});

						//验证只能输出数字和小数点后两位
						$(document).ready(function() {
							$('input[name=price]').keyup(function () {
								var $this = this;
								var t = $this.value.charAt(0);
								$this.value = $this.value.replace(/[^\d.]/g, ""); //清除"数字"和"."以外的字符
								$this.value = $this.value.replace(/^\./g, ""); //验证第一个字符是数字而不是
								$this.value = $this.value.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的
								$this.value = $this.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
								$this.value = $this.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //只能输入两个小数
								if (t == '-') {
									$this.value = '-' + $this.value;
								}
							});
						});
					},
					persistent: true

				});

			}

			//输入信息时计算结果
			function calculateCost(obj){

//				if($(obj).attr("name")=="num"){
//					refundInput(obj);
//				}else{
//					refundInput_minus(obj);
//				}

				var $tr = $(obj).parent().parent();
				var $num = $tr.find("[name='num']").val();
				var $price = $tr.find("[name='price']").val();
				var $result = $num*$price;
				var $currency = $tr.find(".currencyId").val();
				var $em = $tr.find(".currency");

                if(isNaN($result)) {
                    $tr.find(".result").text();
                }else{
                    var sum = String($result);
                    if(sum.indexOf(".") != -1) {
                        $tr.find(".result").text(sum.substring(0, sum.indexOf(".") + 3));
                    }else{
                        $tr.find(".result").text(sum);
                    }

                }
			}

			//换币种时 变换符号
			function changeCur(obj){
				var $em = $(obj).parent().parent().find(".currency");
				var $currency = $(obj).val();

				var currencyList = '${currencyString}';
				var currencyJson = JSON.parse(currencyList)
				for (var i = 0; i < currencyJson.length; i++) {
					var currency = currencyJson[i];
					if($currency == currency["currencyId"]) {
						$em.html(currency["currencyMark"]);
					}
				}
			}

			//增加
			function addone_img(obj){
				var html ="";
				html +="<tr class=\"tr_add\"><td class=\"tc\"><input type=\"text\" maxlength=\"50\" class=\"name_length\" onkeyup=\"nospace(this)\"></td>";
				html +="<td class=\"tc\"><select onclick=\"changeCur(this)\" class=\"currencyId jbox-width100\">" +
						"<c:forEach items='${currencylist}' var='currency'>"+
						"<option value=\"${currency.id}\">${currency.currencyName}</option>"+
						"</c:forEach>"+
						"</select></td>";
				html +="<td class=\"tc\"><input class=\"dataChange number_input\" name=\"num\" type=\"text\" maxlength=\"5\" onkeyup=\"calculateCost(this)\" onafterpaste=\"calculateCost(this)\"  value=\"1\"></td>";
				html +="<td class=\"tc\"><input class=\"dataChange price_input\"  name=\"price\" type=\"text\" onafterpaste=\"calculateCost(this)\"  onkeyup=\"calculateCost(this)\"  maxlength=\"14\"></td>";
				html +="<td class=\"tr\" name=\"result\"><em class=\"currency\">￥</em><em class=\"result\">0.00</em></td>";
				html +="<td class=\"tc\"><a href=\"javascript:void(0)\" onclick=\"deletethis(this)\"><em class=\"add_img_red\"></em></a></td></tr>";

				$(obj).parents("#contentTable").append(html);

                var partten = /^[0-9]+$/;
                $('input[name=num]').keyup(function(){
                    if(!partten.test($(this).val())){
                        var a= $(this).val();
                        var b=a.replace(/[^0-9]+/gi,'');
                        $(this).val(b)
                    }
                });

                //验证只能输出数字和小数点后两位
                $('input[name=price]').keyup(function(){
                    var $this=this;
                    var t = $this.value.charAt(0);
                    $this.value = $this.value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
                    $this.value = $this.value.replace(/^\./g,""); //验证第一个字符是数字而不是
                    $this.value = $this.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的
                    $this.value = $this.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
                    $this.value = $this.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
                    if (t == '-') {
                        $this.value = '-' + $this.value;
                    }
                });

			}

			//删除
			function deletethis(obj){
			    var flag = $("#flag").html();
//				if($(obj).parent().parent().parent().find("tr").length==1){
//					$.jBox.tip("不能删除！", 'error');
//				}else{
                    if($(obj).parent().parent().prev().length){

                    }else{
                        $(obj).parent().parent().parent().children(":first").next().find("td:last-child").empty().append(
                                '<a href="javascript:void(0)" onclick="addone_img(this)"><em class="add_img_green"></em></a>&nbsp;&nbsp;' +
                                '<a href="javascript:void(0)" onclick="deletethis(this)"><em class="add_img_red"></em></a>');
                    }
                    $(obj).parent().parent().remove(); //添加
//				}
			}
			//不可输入空格
			function nospace(obj){
				obj.value=obj.value.replace(/(^\s+)|(\s+$)/g,"");
			}
			//可输入负号
			function refundInput_minus(obj) {
				var ms = obj.value.replace(/[^\d\-\.]/g, "").replace(/(\.\d{2}).+$/, "$1").replace(/^0+([1-9])/, "$1").replace(/^0+$/, "0");
				var txt = ms.split(".");
				obj.value = txt[0] + (txt.length > 1 ? "." + txt[1] : "");
				if (obj.value == '.') {
					return;
				}
				totalRefund();
			}

			function countToatl($this,totalObj,id){
				var innerHtml1='';
				var returnObj={};
				$this.parent().parent().parent().find("div.parent_div").each(function(){
					var tempObj={};
					if($(this).find("span").length>0){
						var tempAttr=[];
						var tempValue=[];
						$(this).find("span:even").each(function () {
							tempAttr.push($(this).text());
						});
						$(this).find("span:odd").each(function () {
							tempValue.push($(this).text())
						});
						$.each(tempAttr,function(i,v){
							tempObj[v]=Number(tempValue[i]);
						})
					}
					returnObj=Object.assignObj(returnObj,tempObj);
				})
				for(var _a in returnObj){
					innerHtml1+='<span>'+_a+'</span><span class="font_16 fbold red_c">'+Number(returnObj[_a]).toFixed(2)+'</span>+';
				}
				innerHtml1=innerHtml1.slice(0,-1);
				$("#"+id).empty().html(innerHtml1);
			}
			
			/**
			 * 获取游客单一币种其他费用
			 * @param travlerPrice
			 */
			function getTravelerChargeRate(currencyMark, travlerPrice) {
				// 如果单笔金额为负值或0，则不添加服务费
				if (Number(travlerPrice) < 0) {
					var tempPrice = 0;
					var quauqOtherChargeType = $("#quauqOtherChargeType").val();
					var quauqOtherChargeRate = $("#quauqOtherChargeRate").val();
					var partnerOtherChargeType = $("#partnerOtherChargeType").val();
					var partnerOtherChargeRate = $("#partnerOtherChargeRate").val();
					var cutChargeType = $("#cutChargeType").val();
					var cutChargeRate = $("#cutChargeRate").val();
					if (quauqOtherChargeType == 0) {
						tempPrice = Number(travlerPrice) * Number(quauqOtherChargeRate);
					} else {
						for(var j = 0; j < currencyArr.length; j++) {
							var currency = currencyArr[j];
							if (currencyMark == currency.currencyMark) {
								tempPrice = (Number(quauqOtherChargeRate) / Number(currency.convertLowest)).toFixed(2);
							}
						}
					}
					if (partnerOtherChargeType == 0) {
						tempPrice = Number(tempPrice) + Number(travlerPrice) * Number(partnerOtherChargeRate);
					} else {
						for(var j = 0; j < currencyArr.length; j++) {
							var currency = currencyArr[j];
							if (currencyMark == currency.currencyMark) {
								tempPrice = Number(tempPrice) + Number((Number(partnerOtherChargeRate) / Number(currency.convertLowest)).toFixed(2));
							}
						}
					}
					return Number(travlerPrice) + Number(tempPrice);
				} else {
					return Number(travlerPrice);
				}
			}

			<!--505需求end-->
		</script>
		<style>
			a.ydbz_s:hover{
				color: #3781d6 !important;
			}
		</style>
	</head>
  
	<body>
  
		<!--币种模板开始-->
		<select name="currencyTemplate" id="currencyTemplate" style="display:none;">
			<c:forEach items="${currencyTemplate}" var="currency">
				<option value="${currency.id}">${currency.currencyName}</option>
			</c:forEach>
		</select>
		<!--币种模板结束-->
		
		<div class="mod_nav">订单 > ${orderStatusStr}</div>
		<%@ include file="/WEB-INF/views/modules/order/rebates/rebatesBaseInfo.jsp"%>
                               
		<div class="ydbz_tit"><span class="fl">游客改价</span></div>
		<form:form action="${ctx}/newChangePrice/applyChangePrice" method="post" id="form1">
			<table class="activitylist_bodyer_table modifyPrice-table">
				<thead>
					<tr>
						<th width="7%" class="">全选</br><input type="checkbox" onclick="" id="checkedAllBox" name=""></th>
						<th width="7%">姓名</th>	
						<%--<th width="13%">币种</th>--%>
						<th width="15%">原始应收价</th>
						<th width="15%">当前应收价</th>
						<th width="15%">改后应收价</th>
						<th width="25%">备注</th>
						<th width="15%">操作</th>
					</tr>
				</thead>
				<tbody id="toursChangePrice">
					<c:choose>
						<c:when test="${empty travelerList }">
							<tr>
								<td colspan="7" style="text-align: center;">暂无游客...</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var='traveler' items='${travelers}' varStatus='statusTraveler'>

								<tr group='travler${statusTraveler.count}'>
									<%--<c:if test="${status.count==1}">--%>
									<td class="table_borderLeftN">
										<input type="checkbox" name="travelerId" value="${traveler.id}">
									</td>
									<td>${traveler.travelerName} </td>
									<%--</c:if>--%>
									<td style="display: none">
										<input type='hidden' name='travelerids' value='${traveler.id}'/>
										<%--<input type='hidden' name='gaijiaCurency' value='${moneyInfo.currencyId}'/>--%>

										<%--<span name='gaijiaCurency'>${currencyNameMap[tempCurrencyId]}</span>--%>
									</td>

									<td class='tr'>
										<c:forEach var="moneyInfo" items='${traveler.originalMoneyList}' varStatus='status'>
											<c:set var="tempCurrencyId" value="${moneyInfo.currencyId}" scope="page"></c:set>
											<c:set var="tempCurrencyMark" value="${currencyMarkMap[tempCurrencyId]}" scope="page"></c:set>
											<c:set var="tempCurrencyName" value="${currencyNameMap[tempCurrencyId]}" scope="page"></c:set>
											<c:choose>
												<c:when test="${!status.last}">
													<span>${tempCurrencyMark}</span><span class='tdorange fbold' flag='original'> ${traveler.originalMoneyList[status.index].amount}</span> +
												</c:when>
												<c:otherwise>
													<span>${tempCurrencyMark}</span><span class='tdorange fbold' flag='original'> ${traveler.originalMoneyList[status.index].amount}</span>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</td>
									<td class='tr'>
                                        <c:forEach items="${fns:getMoneyAfterCP(traveler.id)}" var="money" varStatus="count">
                                            <c:choose>
                                                <c:when test="${not count.last}">
                                                    <span>${fns:getCurrencyNameOrFlag(money.key, "0")}</span> <span class='tdorange fbold' flag='original'>${money.value}</span> +
                                                </c:when>
                                                <c:otherwise>
                                                    <span>${fns:getCurrencyNameOrFlag(money.key, "0")}</span> <span class='tdorange fbold' flag='original'>${money.value}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
										<%--<c:choose>--%>
											<%--<c:when test="${not empty fns:getCosts(traveler.id, 2)}">--%>
												<%--<c:forEach items="${fns:getCosts(traveler.id, 2)}" var="money" varStatus="count">--%>
													<%--<c:choose>--%>
														<%--<c:when test="${not count.last}">--%>
															<%--<span>${fns:getCurrencyNameOrFlag(money.currencyId, "0")}</span> <span class='tdorange fbold' flag='original'>${money.sum}</span> +--%>
														<%--</c:when>--%>
														<%--<c:otherwise>--%>
															<%--<span>${fns:getCurrencyNameOrFlag(money.currencyId, "0")}</span> <span class='tdorange fbold' flag='original'>${money.sum}</span>--%>
														<%--</c:otherwise>--%>
													<%--</c:choose>--%>
												<%--</c:forEach>--%>
											<%--</c:when>--%>
											<%--<c:otherwise>--%>
												<%--<c:forEach var="moneyInfo" items='${traveler.payMoneyList}' varStatus='status'>--%>
													<%--<c:set var="tempCurrencyId" value="${moneyInfo.currencyId}" scope="page"></c:set>--%>
													<%--<c:set var="tempCurrencyMark" value="${currencyMarkMap[tempCurrencyId]}" scope="page"></c:set>--%>
													<%--<c:set var="tempCurrencyName" value="${currencyNameMap[tempCurrencyId]}" scope="page"></c:set>--%>
													<%--<c:choose>--%>
														<%--<c:when test="${!status.last}">--%>
															<%--<span>${tempCurrencyMark}</span><span class='tdorange fbold' flag='beforeys'> ${moneyInfo.amount }</span> +--%>
														<%--</c:when>--%>
														<%--<c:otherwise>--%>
															<%--<span>${tempCurrencyMark}</span><span class='tdorange fbold' flag='beforeys'> ${moneyInfo.amount }</span>--%>
														<%--</c:otherwise>--%>
													<%--</c:choose>--%>
												<%--</c:forEach>--%>
											<%--</c:otherwise>--%>
										<%--</c:choose>--%>
									</td>
									<%--<td class='tc'>--%>
										<%--<dl class='huanjia'>--%>
											<%--<dt>--%>
												<%--<input name='plusys' type='text' class='' value='${moneyInfo.amount}' onkeyup='validNum(this)' onafterpaste='validNum(this)' />--%>
												<%--<input name='plusysTrue' type='hidden' value='0.00' defaultValue='0.00' >--%>
											<%--</dt>--%>
											<%--<dd>--%>
												<%--<div class='ydbz_x' flag='appAll'>应用全部</div>--%>
												<%--<div class='ydbz_x gray' flag='reset'>还原</div>--%>
											<%--</dd>--%>
										<%--</dl>--%>
									<%--</td>--%>
									<td class='tr relative sale_off_parent'>
                                        <em class="arrow"></em>
										<div class="max_width_200 parent_div"></div>
										<div class="sale_off absolute" id="abc">
											<c:if test="${traveler.isReviewing eq 1}">
												<div class="payfor-otherDiv">
													<div class="data_value" style="display:none" data-travelerid="3621" data-name="1" data-number="1" data-price="1" data-currency="￥" data-currencyid="37" data-result="1.00">1:￥1.00</div>
													<table width="100%" class="table_fixed border_t">
														<thead class="thead_border_bottom">
															<tr>
																<td width="17%" class="tl">
																	<div style="margin:0 5px 0 10px;word-break: break-all;">序号</div>
																</td>
																<td width="25%" class="tl">
																	<div style="margin:0 5px 0 10px;word-break: break-all;">费用名称</div>
																</td>
																<td width="13%" class="tl">
																	<div style="margin:0 5px;word-break: break-all; " >数量</div>
																</td>
																<td width="20%" class="tr">
																	<div style="margin:0 5px;word-break: break-all;">单价</div>
																</td>
																<td width="30%" class="tr">
																	<div style="margin:0 10px;word-break: break-all;">总计</div>
																</td>
															</tr>
														</thead>
														<tbody class="new_tbody">
															<c:forEach items="${traveler.costchangeList}" var="costchange" varStatus="s">
																<tr>
																	<td class="t1 SortId">
																		<div style="margin:0 5px 0 10px;word-break: break-all;">${s.count}</div>
																	</td>
																	<td class="t1">
																		<div style="margin:0 5px 0 10px;word-break: break-all;">${costchange.costName}</div>
																	</td>
																	<td class="t1">
																		<div style="margin:0 5px;word-break: break-all;">${costchange.costNum}</div>
																	</td>
																	<td class="tr">
																		<div style="margin:0 5px;word-break: break-all;">${costchange.priceCurrency.currencyMark}${costchange.costPrice}</div>
																	</td>
																	<td class="tr">
																		<div class="countMoney" data-curMark="${costchange.priceCurrency.currencyMark}" data-priCost="${costchange.costSum}" style="margin:0 10px;word-break: break-all;">${costchange.priceCurrency.currencyMark}${costchange.costSum}</div>
																	</td>
																</tr>
															</c:forEach>
														</tbody>
													</table>
												</div>
											</c:if>
										</div>
									</td>

									<td class='tc'>
										<textarea id="travelerremark" name='travelerremark' cols='180' rows='1' onclick="this.innerHTML=''"></textarea>
									</td>
									<td class="tc">
										<c:if test="${traveler.isReviewing ne 1}">
											<a id="addButton" href="javascript:void(0)" onclick="jbox_add_other_cost_pop(this,${traveler.id});">添加</a>
										</c:if>
									</td>
								</tr>

							</c:forEach>
							<tr>
								<td colspan='2'>总价</td>
								<td id='totalOriginal' class="tr"></td>
								<td id='totalNowtime' class="tr"></td>
								<td id='totalFuture' class="tr"></td>
								<td></td>
								<td></td>
							</tr>
						</c:otherwise>					  		
					</c:choose>
				</tbody>
			</table>
			<%--<div class="ydbz_foot" style="margin-top:10px; padding-bottom:10px;">--%>
				<%--<div class="ydbz_x fl re-storeall">全部还原</div>--%>
			<%--</div>--%>
			<%--<div class="allzj tr f18">--%>
				<%--当前金额：<span id="totalBefore"><font class="f14" flag="bz" value=""></font></span><br/>--%>
				<%--<div class="all-money">改后总额：<span id="totalAfter"></span></div>--%>
			<%--</div>--%>
			<div class="dbaniu" style="width:150px;">
				<a class="ydbz_s gray" href="javascript:history.go(-1);" onclick="return confirm('是否确认取消该申请？');">取消</a>
				<input type="button" value="申请改价" class="btn btn-primary" onclick="check_activity_uppricess();">
			</div>
			<input type="hidden" id="ctx" value="${ctx}"/>
			<input type="hidden" id="orderId" name="orderId" value="${productOrder.id}"/>
			<input type="hidden" id="productType" name="productType" value="${productOrder.orderStatus}"/>
			<input type="hidden" id="quauqOtherChargeType" value="${productOrder.quauqOtherChargeType}"/>
			<input type="hidden" id="quauqOtherChargeRate" value="${productOrder.quauqOtherChargeRate}"/>
			<input type="hidden" id="partnerOtherChargeType" value="${productOrder.partnerOtherChargeType}"/>
			<input type="hidden" id="partnerOtherChargeRate" value="${productOrder.partnerOtherChargeRate}"/>
			<input type="hidden" id="cutChargeType" name="cutChargeType" value="${productorder.cutChargeType}">
			<input type="hidden" id="cutChargeRate" name="cutChargeRate" value="${productorder.cutChargeRate}">
		</form:form>

		<div id="add_other_cost" style="display: none">

			<div style="margin:10px"class="add_other_cost">

				<table id="contentTable" class="table activitylist_bodyer_table">
					<thead>
					<tr>
						<th width="10%">费用名称</th>
						<th width="1%">币种</th>
						<th width="8%">数量</th>
						<th width="8%">单价</th>
						<th width="10%">总计</th>
						<th width="3%">操作</th>
                        <th id="flag" style="display: none">添加</th>
					</tr>
					</thead>
					<tbody>
					<tr class="tr_add">
						<td class="tc">
							<input type="text" maxlength="50"  class="name_length" onkeyup="nospace(this)">
						</td>
						<td class="tc">
							<select class="currencyId jbox-width100" onclick="changeCur(this)">
								<option value="33">人民币</option>
								<option value="34">美元</option>

							</select>
						</td>
						<td class="tc">
							<input class="dataChange number_input" name="num" type="text" onafterpaste="calculateCost(this)" onkeyup="calculateCost(this)" value="1" maxlength="5">
						</td>
						<td class="tc">
							<input class="dataChange price_input"  name="price" type="text" onkeyup="calculateCost(this)" onafterpaste="calculateCost(this)" maxlength="12">
						</td>
						<td class="tr" name="result">
							<em class="currency">￥</em><em class="result">0.00</em>
						</td>

						<td class="tc">
							<a href="javascript:void(0)" onclick="addone_img(this)"><em class="add_img_green"></em></a>

						</td>
					</tr>
					</tbody>

				</table>
			</div>

		</div>

	</body>
</html>
