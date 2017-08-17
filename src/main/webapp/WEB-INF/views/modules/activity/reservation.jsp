<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>


<html>
<head>
<meta/>

<link href="${ctxStatic}/forTTS/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/forTTS/css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="${ctxStatic}/forTTS/js/bootstrap-ie.min.js"></script>
<![endif]-->
<link rel="stylesheet" href="${ctxStatic}/forTTS/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/forTTS/css/jh-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/forTTS/css/huanqiu-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/forTTS/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/forTTS/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/forTTS/js/common.js"></script>

<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.spinner.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>

<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
<link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/common/mustache.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/trekiz.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/vendor.service_mode1.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js" type="text/javascript"></script>
<script src="${ctxStatic }/json/json2.js" type="text/javascript" ></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>


<script type="text/javascript">
$(function(){
	//展开筛选
	launch();
	//文本框提示信息显示隐藏
	inputTips();
});
</script>

<script type="text/javascript">
	
		$(document).ready(function() {
			//$.jBox('',{ bottomText: '这是底部文字' });
			
			$( ".spinner" ).spinner({
				spin: function( event, ui ) {
				if ( ui.value > 365 ) {
					$( this ).spinner( "value", 1 );
					return false;
				} else if ( ui.value < 0 ) {
					$( this ).spinner( "value", 365 );
					return false;
				}
				}
			});
			
			
			$(".qtip").tooltip({
				track: true
			});
			
			var _$orderBy = $("#orderBy").val();
			if(_$orderBy==""){
				_$orderBy="groupOpenDate DESC";
			}
			var orderBy = _$orderBy.split(" ");
			$(".activitylist_paixu_left li").each(function(){
                if ($(this).hasClass("li"+orderBy[0])){
                    orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
                    $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
                    $(this).attr("class","activitylist_paixu_moren");
                }
            });
			
			var selectdata = $("#agentIdSel").children("option:selected");
			if(selectdata.length!=0 && $("#tempUserName").val() != "lmelsguest"){
				var options = $("#agentIdSel option");
	            if(selectdata.val()==null||selectdata.val()==""){
	            	var _select = $("#agentIdSel").clone();
	            	_select.attr("id","agentIdIn");
	            	_select.attr("name","agentIdIn");
	            	_select.unbind();
		             var $div = $("<div id='chooseQd' class=\"tanchukuang\"></div>")
					.append($('<div class="add_allactivity choseAgent"></div>')
					.append($('<p style="line-height:60px;text-align:center;">我们目前为您提供'+(options.length-1)+'家渠道的预订，请根据您服务的对象选择渠道名称</p>'))
					    .append($("<label>渠道选择：</label>")).append(_select))
		            .append('<div class="ydBtn"><div class="btn btn-primary ydbz_x" onclick="choseAgent()">开始预订</div>');
		             var html = $div.html();
		             $.jBox(html, { title: "预订-选择渠道", buttons:{},height:220,width:550});
		            letDivCenter("#jbox");
				}
			}
			if( $("#tempUserName").val() == "lmelsguest") {
				$(".lmels-ts").show();
				$("#agentIdSel").attr("disabled",true);
				
			}
			
            $.fn.datepicker=function(option){
                var opt = {}||option;
                this.click(function(){
                   WdatePicker(option);            
                });
            };
            //滚动条	
            $('.team_top').find('.table_activity_scroll').each(function(index, element) {
			 var _gg=$(this).find('tr').length;
            if(_gg>=20){
            $(this).prev().wrap("<div class='group_h_scroll_top'></div>");
            $(this).addClass("group_h_scroll");}
		  });
            //产品名称获得焦点显示隐藏
			$("#wholeSalerKey").focusin(function(){
				var obj_this = $(this);
					obj_this.next("span").hide();
			}).focusout(function(){
				var obj_this = $(this);
				if(obj_this.val()!="") {
				obj_this.next("span").hide();
			}else
				obj_this.next("span").show();
			});
			if($("#wholeSalerKey").val()!="") {
				$("#wholeSalerKey").next("span").hide();
			}
			$("#groupOpenDate").datepicker({
				dateFormat:"yy-mm-dd",
			   dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
		       closeText:"关闭", 
		       prevText:"前一月", 
		       nextText:"后一月",
		       monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
		       });
			$("#groupCloseDate").datepicker({
				dateFormat:"yy-mm-dd",
				   dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
			       closeText:"关闭", 
			       prevText:"前一月", 
			       nextText:"后一月",
			       monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
			       });
			$("#targetAreaId").val("${travelActivity.targetAreaIds}");
			$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
			$("#agentIdSel").change(function(){
				var selectdata = $("#agentIdSel option:selected");
				$("#agentId").val($(selectdata).val());
				$("#seachbutton").click();
			});
			
			//展开筛选按钮
			$('.zksx').click(function() {
					
				if($('.ydxbd').is(":hidden")==true) {
					$('.ydxbd').show();
					$(this).text('收起筛选');
					$(this).addClass('zksx-on');
				}else{
					$('.ydxbd').hide();
					$(this).text('展开筛选');
					$(this).removeClass('zksx-on');
				}
			});
			//如果展开部分有查询条件的话，默认展开，否则收起	
			var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][id!='wholeSalerKey']");
			var searchFormselect = $("#searchForm").find("select");
			var inputRequest = false;
			var selectRequest = false;
			for(var i = 0; i<searchFormInput.length; i++) {
				if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
					inputRequest = true;
				}
			}
			for(var i = 0; i<searchFormselect.length; i++) {
				if($(searchFormselect[i]).children("option:selected").val() != "" && $(searchFormselect[i]).children("option:selected").val() != null) {
					selectRequest = true;
				}
			}
			if(inputRequest||selectRequest) {
				$('.zksx').click();
			}
				
				
				
		});
		
		   function expand(child, obj, srcActivityId, payMode_deposit, payMode_advance, payMode_full, payMode_op, payMode_cw, payMode_data, payMode_guarantee, payMode_express, showType) {
			
            if($(child).is(":hidden")){
                var selectdata = $("#agentIdSel").length ? $("#agentIdSel option:selected") : $("input[name='agentId']");
               
                var agentId = selectdata.val();
                if(agentId!=null&&agentId!="") {
                    $.ajax({
                        type:"POST",
                        url:"${ctx}/stock/manager/payReservePosition",
                        data:{
                            srcActivityId:srcActivityId,
                            agentId:agentId
                        },
                        success:function(msg) {
                        	if(showType=='1'){
                        	$(obj).html("关闭团期预定");
                        	}else{
                        	$(obj).html("关闭团期预报名");
                        	}
                        	$(obj).next().hide();
                            $(child).css("display","table-row");
                            $(obj).parents("td").attr("class","td-extend");
                        	if(msg.length>0){
                                $(child+" [class^='soldPayPosition']").show();
                        	}
                        	$.each(msg,function(keyin,valuein){
                                $("td .soldPayPosition"+(valuein.activityGroupId)).find("span").text(valuein.leftpayReservePosition);
                                //如果切位余位数大于0
                                if(valuein.leftpayReservePosition>0) {
                                    //三种支付方式
                                	$("td .aPayforModePrice"+(valuein.activityGroupId)).each(function(index, obj) {
                                		if(!$(this).hasClass("canClick")) {
                       
    	                                	$(this).css("color","").unbind().click(function() {
    		                                	if($.trim($(this).text()) == '订金占位' && payMode_deposit == 1) {
    		                                		occupied(valuein.activityGroupId,srcActivityId,1,this);
    		                                		
    			                                } else if($.trim($(this).text()) == '预占位' && payMode_advance == 1) {
    		                                		occupied(valuein.activityGroupId,srcActivityId,2,this);
    		                                			
    			                                } else if($.trim($(this).text()) == '付全款' && payMode_full == 1) {
    		                                		occupied(valuein.activityGroupId,srcActivityId,3,this);
    			                                } else if($.trim($(this).text()) == '计调确认占位' && payMode_op == 1) {
    		                                		occupied(valuein.activityGroupId,srcActivityId,7,this);
    			                                } else if($.trim($(this).text()) == '财务确认占位' && payMode_cw == 1) {
    		                                		occupied(valuein.activityGroupId,srcActivityId,8,this);
    			                                } else if($.trim($(this).text()) == '资料占位' && payMode_data == 1) {
    		                                		occupied(valuein.activityGroupId,srcActivityId,4,this);
    			                                } else if($.trim($(this).text()) == '担保占位' && payMode_guarantee == 1) {
    		                                		occupied(valuein.activityGroupId,srcActivityId,5,this);
    			                                } else if($.trim($(this).text()) == '确认单占位' && payMode_express == 1) {
    		                                		occupied(valuein.activityGroupId,srcActivityId,6,this);
    			                                }
    	                                	});
    	                                	$("td .aPayforModePrice" + (valuein.activityGroupId) + ":last").next("input").removeClass("gray").addClass("btn-primary");
    	                                	if(showType=='1'){
    	                                		
    	                                	  $("td .aPayforModePrice" + (valuein.activityGroupId) + ":last").next("input").unbind().click(function() {
    	                                		 orderType(this)
    		                                   });
    	                                	}else{
    	                                		
    	                                		$("td .aPayforModePrice" + (valuein.activityGroupId) + ":last").next("input").unbind().click(function() {
    	                                		  preOrderApply(srcActivityId,valuein.activityGroupId,this)
    		                                  });
    	                                	}
                                    	}
                                    });
                                }
                        	});
                        }
                    });
                }else{
                	if(showType=='1'){
                        	$(obj).html("关闭团期预定");
                        	}else{
                        	$(obj).html("关闭团期预报名");
                        	}
                	
                    $(child).show();
                    $(obj).parents("td").addClass("td-extend");
                    //$(obj).next().hide();
                }
            }else{
                if(!$(child).is(":hidden")){
                    $(child).hide();
                    $(obj).parents("td").removeClass("td-extend");
                  
                    if(showType=='1'){
                        	  $(obj).html("展开团期预定");
                        	}else{
                        	  $(obj).html("展开团期预报名");
                        	}
                }
            }
        }
		
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			//var showType=$("#showType").val();;
			$("#searchForm").attr("action","${ctx}/activity/managerforCustomizedOrder/list/"+1);
			$("#searchForm").submit();
	    }
	    
	    function downloads(docid){
	    	window.location.href = "${ctx}/sys/docinfo/download/"+docid;
	    }
	    
	    //占位
	    function occupied(id,srcActivityId,payMode,_this){
	    	showQwOrYw("预订",srcActivityId,id,payMode,_this);
	    	return false;
        }
	    //预订
	    function reserveOrder(id,srcActivityId,_this){
	    
	    	showQwOrYw("预订",srcActivityId,id,payMode,_this);
	    	
	    	var selectdata = $("#agentIdSel option:selected");
	    	if(selectdata.length==0){
                selectdata= $(".inputagentId");
            }
	    	//if(selectdata.val()==null||selectdata.val()==""){
	    		//top.$.jBox.tip('请选择预订的渠道','error');
	    		//return false;
	    	//}
            var param = "&agentId="+selectdata.val();
	    	//渠道id
	    	//window.open("${ctx}/orderCommon/manage/showforModify?payMode=" + payMode + "&type=1&productId=" + srcActivityId + "&productGroupId=5261&agentId=101");// + id + param);
        }
	    
	    $(function(){
			$('.team_a_click').toggle(function(){
				$(this).addClass('team_a_click2')
			},function(){
				$(this).removeClass('team_a_click2')
			});
		 });
		 
		 function takeOpenDate(obj){
		 	
			var groupOD = $(obj).parent().prev().children("input[name='groupOpenDate']").val();
//			alert(groupOD);
			return groupOD;
		}
	    
		function takeVisaDate(obj) {
			var groupOD = $(obj).parent().parent().children().first().children("input[name='groupOpenDate']").val();
//			alert(groupOD);
			return groupOD;
		}
		 
		function getOpenDate(obj){
			var groupOD = $(obj).parent().prev().children("input[name='groupOpenDate']").val();
			var strArrOD = groupOD.split("-");
			var thisMouth = parseInt(strArrOD[1])+1;
			var thisYear = parseInt(strArrOD[0]);
			if(thisMouth==13){
				thisYear+=1;
				thisMouth=1;
			}
			var thisDate = thisYear+"-"+thisMouth+"-"+strArrOD[2];
			return thisDate;
		}
		
		function doClick(productId,productGroupId,payMode){
			var selectType = $("input[name='flytype']:checked").val();
			var selectdata = $("#agentIdSel option:selected");
			if(selectdata.length==0){
				selectdata= $(".inputagentId");
            }
			
            if(selectdata.val()==null||selectdata.val()==""){
                top.$.jBox.tip('请选择预订的渠道','error');
                return false;
            }
            var selectIntermodalValue = $('#intermodalAreaSelect').children('select').val();
            var param = "&agentId="+selectdata.val()+"&placeHolderType="+selectType;
            if(selectIntermodalValue){
                param += "&intermodalType=" + selectIntermodalValue;
            }
            if(payMode=="1"){
                //dingj zhanwei 
                window.open("${ctx}/orderCommon/manage/showforModify?payMode=1&type=2&productId="+productId+"&productGroupId="+productGroupId+param);
            }else if(payMode=="2"){
                //zanwei
                window.open("${ctx}/orderCommon/manage/showforModify?payMode=2&type=3&productId="+productId+"&productGroupId="+productGroupId+param);
            }else if(payMode=="3"){
                window.open("${ctx}/orderCommon/manage/showforModify?payMode=3&type=1&productId="+productId+"&productGroupId="+productGroupId+param);
            }else if(payMode=="4"){
                window.open("${ctx}/orderCommon/manage/showforModify?payMode=4&type=3&productId="+productId+"&productGroupId="+productGroupId+param);
            }else if(payMode=="5"){
                window.open("${ctx}/orderCommon/manage/showforModify?payMode=5&type=3&productId="+productId+"&productGroupId="+productGroupId+param);
            }else if(payMode=="6"){
                window.open("${ctx}/orderCommon/manage/showforModify?payMode=6&type=3&productId="+productId+"&productGroupId="+productGroupId+param);
            }
		}
		function orderType(obj) {
			
			var _selectType = $(obj).next().clone();
            var intermodalType = $(obj).next().next().clone();
            var intermodalFlag = false;
            if(intermodalType.length > 0){
                intermodalFlag = true;
                intermodalType.show();
            }
            
            
			$(obj).addClass("sign");
			$(_selectType).addClass("typeSelected");
			$(_selectType).show();
			var $div = $("<div class=\"tanchukuang\"></div>")
						.append($('<div class="add_allactivity"><label>付款方式：</label>').append(_selectType));
            if(intermodalFlag){
                $div.append($('<div id="intermodalAreaSelect" class="add_intermodalType"><label>游客出发区域：</label>').append(intermodalType));
            }
            $div.append("<br><div class='ydBtn' id='order_pay_button'><input class='btn btn-primary' type='button' onClick='orderPay()' value='预 定'/></div>");
			var html = $div.html();
			
		
			
			$.jBox(html, { title: "预订-选择付款方式",buttons:{},height:230});
		
			letDivCenter("#jbox");
			
		}

        function preOrderApply(productId,productGroupId,obj){
            var intermodalType = $(obj).next().next().clone();
            var intermodalFlag = false;
            if(intermodalType.length > 0){
                intermodalFlag = true;
                intermodalType.show();
                $(obj).addClass("sign");
                var $div = $("<div class=\"tanchukuang\"></div>");
                if(intermodalFlag){
                    $div.append($('<div id="intermodalAreaSelect" class="add_intermodalType"><label>游客出发区域：</label>').append(intermodalType));
                }
                $div.append("<br><div class='ydBtn'><input class='btn btn-primary' type='button' onClick='apply("+productId+","+productGroupId+")' value='预报名'/></div>");
                var html = $div.html();
                $.jBox(html, { title: "预报名-选择联运方式",buttons:{},height:230});
                letDivCenter("#jbox");
            }else{
                apply(productId,productGroupId);
            }


        }

		//报名  
		function apply(productId,productGroupId) {
			
           //渠道id
            var selectdata = $("#agentIdSel option:selected");
			if(selectdata.length==0){
				selectdata= $(".inputagentId");
			}
            if(selectdata.val()==null||selectdata.val()==""){
                top.$.jBox.tip('请选择预报名的渠道','error');
                return false;
            }
            var agentId = selectdata.val();
            var selectIntermodalValue = $('#intermodalAreaSelect').children('select').val();

            var param ="productId=" + productId + "&productGroupId=" + productGroupId+"&agentId="+selectdata.val();
            if(selectIntermodalValue){
                param += "&intermodalType=" + selectIntermodalValue;
            }
	    	window.open("${ctx}/orderCommon/manage/applyshowforModify?" + param);

		}
		function orderPay() {
			var selectType = $(".typeSelected option:selected").val();
			if (selectType == 3) {
				$(".sign").prevAll(".normalPayType").click();
			} else if(selectType == 1) {
				$(".sign").prevAll(".dingjin_PayType").click();
			} else if(selectType == 2) {
				$(".sign").prevAll(".yuzhan_PayType").click();
			} else if(selectType == 4) {
				$(".sign").prevAll(".data_PayType").click();
			} else if(selectType == 5) {
				$(".sign").prevAll(".guarantee_PayType").click();
			} else if(selectType == 6) {
				$(".sign").prevAll(".express_PayType").click();
			}
			
			$(".sign").removeClass("sign");
		}
		function showQwOrYw(title,productId,productGroupId,type,_this){
			var selectdata = $("#agentIdSel option:selected");
			if(selectdata.length==0){
				selectdata= $(".inputagentId");
			}
			
            //if(selectdata.val()==null||selectdata.val()==""){
                //top.$.jBox.tip('请选择预订的渠道','error');
                //return false;
            //}
			var _td = $(_this).closest("tr").find("td[class^='soldPayPosition']");
			var qws=Number(_td.text());
			var yws=Number(_td.prev().text());
			var flag = false;
			var selectType = 0;
			if(yws<=0&&qws<=0){
				top.$.jBox.tip('余位数不足，不能预订','error');
                return false;		
			}else if(yws<=0){
				//直接走切位
				selectType=1;
				flag = true;
			}else if(qws<=0){
				//直接走余位
				flag = true;
				selectType=0;
			}
			if(flag){
                var selectIntermodalValue = $('#intermodalAreaSelect').children('select').val();
                var param = "&agentId="+selectdata.val()+"&placeHolderType="+selectType;
                if(selectIntermodalValue){
                    param += "&intermodalType=" + selectIntermodalValue;
                }
	            if(type=="1"){
	                //dingj zhanwei 
	                window.open("${ctx}/orderCommon/manage/showforModify?payMode=1&type=2&productId="+productId+"&productGroupId=5261&agentId=101&placeHolderType=0");
	            }else if(type=="2"){
	                //zanwei
	                window.open("${ctx}/orderCommon/manage/showforModify?payMode=2&type=3&productId="+productId+"&productGroupId=5261&agentId=101&placeHolderType=0");//"+productGroupId+param);
	            }else if(type=="3"){
	                window.open("${ctx}/orderCommon/manage/showforModify?payMode=3&type=1&productId="+productId+"&productGroupId=5261&agentId=101&placeHolderType=0");//"+productGroupId+param);
	            }else if(type=="4"){
	                window.open("${ctx}/orderCommon/manage/showforModify?payMode=4&type=3&productId="+productId+"&productGroupId=5261&agentId=101&placeHolderType=0");//"+productGroupId+param);
	            }else if(type=="5"){
	                window.open("${ctx}/orderCommon/manage/showforModify?payMode=5&type=3&productId="+productId+"&productGroupId="+productGroupId+param);
	            }else if(type=="6"){
	                window.open("${ctx}/orderCommon/manage/showforModify?payMode=6&type=3&productId="+productId+"&productGroupId="+productGroupId+param);
	            }
	            $(".jbox-close").click();
	            return false;
			}
			var $div = $("<div class=\"tanchukuang\"></div>")
            .append('<label>订单类型：</label><p><span><input type="radio" name="flytype" checked="checked" value="1" class="radio">切位订单，剩余' + qws + '</span><span><input type="radio" name="flytype" value="0" class="radio">余位订单，剩余' + yws + '</span></p>');
            $(".add_allactivity").after('<div class="ydBtn"><div class="ydbz_x" style="width: 57px;" onclick="doClick(' + productId+',' + productGroupId+',' + type+')">' + title + '</div>');
            var html = $div.html();
            $(".add_allactivity").find("label").text("付款方式：" + $(".typeSelected option:selected").text());
            $(".typeSelected").nextAll().remove();
            $(".typeSelected").parent().append($div);
            $(".tanchukuang").prev().remove();
            $(".add_allactivity").next().next().next().next().remove();
            $("#intermodalAreaSelect").hide();
            $("#order_pay_button").hide();
		}
		
	   function choseAgent(){
		   var selectdata = $("#agentIdIn option:selected");
		  $("#agentIdSel").val(selectdata.val());
		  $("#agentId").val(selectdata.val());
		  $("#seachbutton").click();
		  //$("#agentIdSel").change();
		  $(".jbox-close").click();
	   }
	   
	   
	   function letDivCenter(divName){
           var top = ($(window).height() - $(divName).height())/2; 
           $(divName).css( { 'top' : top } ).show();
        }
	   
	   function sortby(sortBy,obj){
		   var temporderBy = $("#orderBy").val();
		   if(temporderBy.match(sortBy)){
			   sortBy = temporderBy;
			   if(sortBy.match(/ASC/g)){
				   sortBy = sortBy.replace(/ASC/g,"");
			   }else{
				   sortBy = $.trim(sortBy)+" ASC";
			   }
		   }
		   
           $("#orderBy").val(sortBy);
           $("#searchForm").submit();
       }

	</script>
</head>
<body>
<div id="sea">
	<header>
		<div class="hedear">
      		<div class="hedear-left">
                <div class="hedear-logo"></div>
                <div class="clear"></div>
      		</div>
            <div class="hedear-right">
                <ul class="hedear-nav">
                    <li class="head-home"><a href="#">后台首页</a></li>
                    <li class="head-logout"><a href="#">退出</a></li>
                    <div class="clear"></div>
                </ul>
				<p class="header-user"><em>王涛</em>，您好，登录时间 <em>2014-5-12 12:36:52</em><span class="header-userspan">
			接口销售：郭晓勤</span><span>销售人电话：13666984589</span></p>
            </div>
      		<div class="clear"></div>
		</div>
    </header>
	<div class="main">
		<div class="main-left">
            <h2 class="mainMenu"><span class="iconMenu iconMenu-10"></span>询价<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">询价记录</a></li>
            </ul>
            <h2 class="mainMenu mainMenu-on"><span class="iconMenu iconMenu-1"></span>预定<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul" style="display:block;">
                <li class="mainMenu-ul-on"><a href="#" target="_self">单团</a></li>
                <li><a href="#" target="_self">散拼</a></li>
                <li><a href="#" target="_self">游学</a></li>
                <li><a href="#" target="_self">大客户</a></li>
                <li><a href="#" target="_self">自由行</a></li>
                <li><a href="#" target="_self">签证</a></li>
                <li><a href="#" target="_self">机票</a></li>
            </ul>
            <!--订单开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-2"></span>订单<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">单团订单</a></li>
                <li><a href="#" target="_self">销售订单</a></li>
                <li><a href="#" target="_self">计调订单</a></li>
                <li><a href="#" target="_self">签证订单</a></li>
                <li><a href="#" target="_self">机票订单</a></li>
            </ul>
            <!--订单结束-->
            <!--渠道商开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-3"></span>渠道商<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">渠道商查询</a></li>
                <li><a href="#" target="_self">渠道商添加</a></li>
                <li><a href="#" target="_self">定价策略查询</a></li>
                <li><a href="#" target="_self">定价策略添加</a></li>
            </ul>
            <!--渠道商结束-->
            <!--财务(渠)开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-4"></span>财务(渠)<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self"><!--渠道商-->发票申请</a></li>
                <li><a href="#" target="_self"><!--渠道商-->发票审核记录查询</a></li>
            </ul>
            <!--财务(渠)结束-->
            <!--财务(供)开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-4"></span>财务(供)<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self"><!--渠道商-->发票申请</a></li>
                <li><a href="#" target="_self"><!--渠道商-->结算管理</a></li>
                <li><a href="#" target="_self"><!--渠道商-->成本管理</a></li>
            </ul>
            <!--财务(供)结束-->
            <!--产品开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-5"></span>产品<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">单团</a></li>
                <li><a href="#" target="_self">散拼</a></li>
                <li><a href="#" target="_self">游学</a></li>
                <li><a href="#" target="_self">大客户</a></li>
                <li><a href="#" target="_self">自由行</a></li>
                <li><a href="#" target="_self">签证</a></li>
                <li><a href="#" target="_self">机票</a></li>
            </ul>
            <!--产品结束-->
            <!--运控开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-6"></span>运控<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">库存切位</a></li>
                <li><a href="#" target="_self">产品成本录入</a></li>
            </ul>
            <!--运控结束-->
            <!--系统设置开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-7"></span>系统设置<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">个人信息</a></li>
                <li><a href="#" target="_self">修改密码</a></li>
                <li><a href="#" target="_self">账号管理</a></li>
                <li><a href="#" target="_self">账号添加</a></li>
            </ul>
            <!--系统设置结束-->
            <!--基础信息开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-8"></span>基础信息<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">旅游类型</a></li>
                <li><a href="#" target="_self">产品系列</a></li>
                <li><a href="#" target="_self">产品类型</a></li>
                <li><a href="#" target="_self">交通方式</a></li>
                <li><a href="#" target="_self">出发城市</a></li>
                <li><a href="#" target="_self">目的地区域</a></li>
                <li><a href="#" target="_self">航空公司</a></li>
            </ul>
            <!--基础信息结束-->
            <!--模块说明开始-->
            <h2 class="mainMenu mainMenu-last"><span class="iconMenu iconMenu-9"></span>模块说明<!--<i class="iconMenu-arrow"></i>--></h2>
            <!--模块说明结束-->
		</div>
		
		
		
		
		<!-- 查询UI -->
        <div class="main-right">
            <content tag="three_level_menu"></content>
            <div class="bgMainRight">
            	<!--右侧内容部分开始-->
                <form method="post" action="/trekiz_wholesaler_tts/a/activity/managerforCustomizedOrder/list/1" id="searchForm">
					<div class="activitylist_bodyer_right_team_co">
						<div class="activitylist_bodyer_right_team_co2 pr wpr20">
							<label>产品名称：</label><input value="" name="wholeSalerKey" id="wholeSalerKey" class="txtPro inputTxt" style="width:260px" flag="istips" />
							<span style="display: block;" class="ipt-tips">销售姓名或线路国家</span>
						</div>
							
						<div class="form_submit">
							<input type="submit" value="搜索" id="seachbutton" class="btn btn-primary ydbz_x">
						</div>
				
					<div class="zksx zksx-on">收起筛选</div>
							
					  <div class="ydxbd">      
					  
				  
							
					    
     <div class="activitylist_bodyer_right_team_co3">
            <div class="activitylist_team_co3_text">行程天数：</div>
              <input id="activityDuration" class="spinner" maxlength="5" name="activityDuration" value="${activityDuration }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
            </div>
						
									
	
            
            
					  <div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">出发地：</div>
							<select name="fromArea" id="fromArea">
									<option value="">不限</option>
									<option value="3">上海</option><option value="1">北京</option><option value="19">合肥</option><option value="4">广州</option><option value="17">哈尔滨</option><option value="18">沈阳</option><option value="9">武汉</option><option value="13">石家庄</option><option value="11">济南</option>
							</select>
							
						</div>
					 
						
						  
						<div class="activitylist_bodyer_right_team_co2">
								<label>出团日期：</label><input readonly="" onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" style=" width: 122px;" value="" name="groupOpenDate" class="inputTxt dateinput" id="groupOpenDate">
									<span style="font-size:12px; font-family:'宋体';"> 至 </span><input readonly="" onclick="WdatePicker()" style="width: 122px;" value="" name="groupCloseDate" class="inputTxt dateinput" id="groupCloseDate">
							</div>
						<div class="kong"></div>
						
						<!-- div class="activitylist_bodyer_right_team_co1">  
							<div class="activitylist_team_co3_text">目的地：</div>
							<div class="input-append">
								<input type="hidden" value="" class="" name="targetAreaIdList" id="targetAreaId">
								<input type="text" style="" class="" value="" readonly="readonly" name="targetAreaNameList" id="targetAreaName"><a style="_padding-top:6px;" class="btn " href="javascript:" id="targetAreaButton">&nbsp;选择&nbsp;</a>
							</div>
							<script type="text/javascript">
								$("#targetAreaButton").click(function(){
									// 是否限制选择，如果限制，设置为disabled
									//if ("" == "disabled"){
										//return true;
									//}
									// 正常打开	
									top.$.jBox.open("iframe:/trekiz_wholesaler_tts/a/tag/treeselect?url="+encodeURIComponent("/activity/manager/filterTreeData")+"&amp;module=&amp;checked=true&amp;extId=&amp;selectIds="+$("#targetAreaId").val(), "选择区域", 300, 420,{
										buttons:{"确定":"ok", "关闭":true}, submit:function(v, h, f){
											if (v=="ok"){
												var tree = h.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
												var ids = [], names = [], nodes = [];
												if ("true" == "true"){
													nodes = tree.getCheckedNodes(true);
												}else{
													nodes = tree.getSelectedNodes();
												}
												for(var i=0; i<nodes.length; i++) {//
													if (nodes[i].isParent){
														continue; // 如果为复选框选择，则过滤掉父节点
													}//
													ids.push(nodes[i].id);
													names.push(nodes[i].name);//
												}
												$("#targetAreaId").val(ids);
												$("#targetAreaName").val(names);
												$("#targetAreaName").focus();
												$("#targetAreaName").blur();
											}//
										}, loaded:function(h){
											$(".jbox-content", top.document).css("overflow-y","hidden");
										},persistent:true
									});
								});
							</script>
						</div>  -->
						
						 <div class="activitylist_bodyer_right_team_co1">  
                  <div class="activitylist_team_co3_text">目的地：</div>
                 <tags:treeselect id="targetArea" name="targetAreaIdList" value="${targetAreaIds}" labelName="targetAreaNameList"  labelValue="${targetAreaNames}"
                     title="区域" url="/activity/manager/filterTreeData" checked="true"/>
            </div>
						
						
						<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">航空公司：</div><select name="trafficName" id="trafficName">      
						   <option selected="selected" value="">不限</option>
						   <option value="816">UN</option><option value="317">EK</option><option value="22">2S</option><option value="330">EY</option><option value="848">VN</option><option value="274">D7</option><option value="11">2D</option><option value="3">MU</option><option value="21">2P</option><option value="239">C2</option><option value="1">CA</option><option value="10">2B</option><option value="7">0V</option><option value="6">0P</option><option value="4">UL</option><option value="9">2A</option><option value="8">1X</option><option value="692">QR</option>
						</select>
						</div>
						
						<div class="activitylist_bodyer_right_team_co2">
							<label>成本价格：</label><input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" value="" name="settlementAdultPriceStart" class="rmb inputTxt" maxlength="8" id="settlementAdultPriceStart">
								<span style="font-size:12px;font-family:'宋体';"> 至</span>
							<input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" value="" maxlength="8" name="settlementAdultPriceEnd" class="rmb inputTxt" id="settlementAdultPriceEnd">
						</div>
					 <div class="kong"></div>
					 
					  </div>
					  <div class="kong"></div>
					</div>
					
				</form>
				
				
				<!-- 排序 UI -->
				<div class="activitylist_bodyer_right_team_co_paixu">
                    <div class="activitylist_paixu">
                      <div class="activitylist_paixu_left">
                        <ul>
                            <li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
                            <li class="activitylist_paixu_left_biankuang"><a onClick="sortby('id',this)">创建时间</a></li>
                            <li class="activitylist_paixu_left_biankuang"><a onclick="">更新时间</a></li>
                            <li class="activitylist_paixu_moren"><a onclick="">出团日期<i class="icon icon-arrow-down"></i></a></li>
                        </ul>
                      </div>
                       <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
                      <div class="kong"></div>
                    </div>
                </div>
                
                
                <!-- 搜索结果UI -->
				<form id="groupform" name="groupform" action="" method="post">
					<table id="contentTable" class="table activitylist_bodyer_table">
						<thead style="background:#403738">
							<tr>
								<th width="10%">序号</th>
								<th width="20%">产品名称</th>
								<th width="15%">出发地</th>
								<th width="15%">航空</th>
								<th width="15%">成本价格</th>
								<th width="25%">最近出团日期</th>
							</tr>
						</thead>
						
						<tbody>			
						
						
						<c:forEach items="${page.list}" var="activity" varStatus="s">
            <c:set var="groupsize" value="${fn:length(activity.activityGroups)}"></c:set>
            	<c:set var="freePositions" value="0"></c:set>
	            <c:forEach items="${activity.activityGroups}" var="group" varStatus="s2">
		            <c:if test="${activity.groupOpenDate eq group.groupOpenDate}">
		            	<c:set var="freePositions" value="${freePositions + group.freePosition }"></c:set>
		            </c:if>
	            </c:forEach>
	            
            <tr id="parent${s.count}">
                <td>${s.count}<br/><br/></td>
                
                <td class="activity_name_td" >
                    <a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${activity.id}')">${activity.acitivityName}</a>
                    <c:choose>
                        <c:when test="${activity.intermodalStrategies[0].type == 1}">
                            <span class="lianyun_name">全国联运</span>
                        </c:when>
                        <c:when test="${activity.intermodalStrategies[0].type == 2}">
                            <span class="lianyun_name">分区联运</span>
                        </c:when>
                        <c:otherwise>
                            <span class="lianyun_name">无联运</span>
                        </c:otherwise>
                    </c:choose>
                </td>
               
               
                <td>
                    <c:choose>
                        <c:when test="${activity.intermodalStrategies[0].type == 1}">
                            <span class="lianyun_name">全国联运</span>
                        </c:when>
                        <c:when test="${activity.intermodalStrategies[0].type == 2}">
                            <span class="lianyun_name">分区联运</span>
                        </c:when>
                        <c:otherwise>
                            ${activity.fromAreaName}
                        </c:otherwise>
                    </c:choose>
                </td>
                
                
                
                <td>
                    <label class="qtip" title="${activity.trafficNameDesc}">${activity.trafficNameLabel}</label>
                </td>
               
               
                
                
                <td class="tr"><c:if test="${activity.settlementAdultPrice==0}">价格待定</c:if><c:if test="${activity.settlementAdultPrice>0}">¥<span class="tdred fbold"><fmt:formatNumber value="${activity.settlementAdultPrice}"  type="currency" pattern="#,##0.00"/></span>起</c:if></td>
                 <td>
                    <c:if test="${groupsize ne 0 }">
                    	<c:choose>
							<c:when test="${activity.groupOpenDate eq activity.groupCloseDate}">${activity.groupOpenDate}</c:when>
							<c:otherwise>${activity.groupOpenDate}</c:otherwise>
						</c:choose>
						(余位：${freePositions})
	                <br/>
	                <c:if test="${showType=='1'}">
	                <a id="close${s.count}" href="javascript:void(0)" class="team_a_click" onClick="expand('#child${s.count}',this,${activity.id },${activity.payMode_deposit},${activity.payMode_advance},${activity.payMode_full},${activity.payMode_op},${activity.payMode_cw},${activity.payMode_data},${activity.payMode_guarantee},${activity.payMode_express},${showType})" onMouseenter="if($(this).html()=='团期预定'){$(this).html('展开团期预定')}" onMouseleave="if($(this).html()=='展开团期预定'){$(this).html('团期预定')}">团期预定</a>
                    </c:if>
                    <c:if test="${showType=='2'}">
	                <a id="close${s.count}" href="javascript:void(0)" class="team_a_click" onClick="expand('#child${s.count}',this,${activity.id },${activity.payMode_deposit},${activity.payMode_advance},${activity.payMode_full},${activity.payMode_op},${activity.payMode_cw},${activity.payMode_data},${activity.payMode_guarantee},${activity.payMode_express},${showType})" onMouseenter="if($(this).html()=='团期预报名'){$(this).html('展开团期预报名')}" onMouseleave="if($(this).html()=='展开团期预报名'){$(this).html('团期预报名')}">团期预报名</a>
                    </c:if>
                    </c:if>
                    <c:if test="${groupsize == 0 }">
                                                      日期待定
                    </c:if> 
                </td>
            </tr>
            
                <tr id="child${s.count}" style="display:none"  class="activity_team_top1">
									  <td colspan="6" class="team_top" style="background-color:#d1e5f5;">
										<table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;">
											<thead>
												<tr>
													<th width="10%" rowspan="2">团号</th>
													<th width="8%" rowspan="2">询价客户</th>
													<th width="8%" rowspan="2">操作员</th>
													<th width="10%" rowspan="2">出/截团日期</th>
													<th width="7%" rowspan="2">签证国家</th>
													<th width="9%" rowspan="2">资料截止日期</th>
													<th width="24%" colspan="3" class="t-th2">成本价/人</th>
													<th width="6%" rowspan="2">订金/人</th>
													<th width="8%" rowspan="2">单房差/间夜</th>
													<th width="4%" rowspan="2" style="display:none" class="soldPayPosition">切位</th>
													<th width="6%" rowspan="2">操作</th>
													
												</tr>
												<tr>
													<th width="8%">成人</th>
													<th width="8%">儿童</th>
													<th width="8%">特殊人群</th>
												</tr>
											</thead></table>
										<div class="table_activity_scroll">
										<table class="table activitylist_bodyer_table table-mod2-group">
									<tbody>
									
									
									  <tr>
													<td width="10%">wwww1111</td>
													<td width="8%" class="tc">张三</td>
													<td width="8%" class="tc" title="QQ:12345 电话：130-665774">张思</td>
													<td width="10%" class="p0">
														<div class="out-date">2014-09-10</div>
														<div class="close-date">2014-08-26</div>
													</td>
													 <td width="7%">
														
													</td>
													<td width="9%" class="tc">
														<span>2014-08-28</span>
													</td>
													<td width="8%" class="tr">
														¥<span class="tdred">20</span>
													</td>
													<td width="8%" class="tr">
														¥<span class="tdred">20</span>
													</td>
					
													<td width="8%" class="tr">
														¥<span class="tdred">20</span>
													</td>
													<td width="6%" class="tr">
														¥<span class="tdorange">20</span>
													</td>
													<td width="8%" class="tr">
														¥<span class="tdred">20</span>
													</td>

													  <td width="4%" style="display:none;" class="soldPayPosition2212">
																	<span class="tdred">0</span>
													  </td>
													  
													  <td width="6%" class="tc">
													
														<a style="display:none;" href="javascript:void(0)" class="normalPayType aPayforModePrice2212  
														 canClick " onclick="occupied(2212,1059,3,this)">
															付全款
														</a>
													
													
														<a style="display:none;" href="javascript:void(0)" class="dingjin_PayType aPayforModePrice2212  
														 canClick " onclick="occupied(2212,1059,1,this)">
															订金占位
														</a>
													
													
														<a style="display:none;" href="javascript:void(0)" class="yuzhan_PayType aPayforModePrice2212  
														 canClick " onclick="occupied(2212,1059,2,this)">
															预占位
														</a>
													 
														<input type="button" class="btn btn-primary" value="预 定" onclick="orderType(this)">
													
													
													
													 
													<select style="display:none;">
														
															<option value="3">全款支付</option>
														
														
															<option value="1">订金占位</option>
														
														
															<option value="2">预占位</option>
														
														
														
														
													</select>
													  
												  </td>
													
									  </tr>
									
									
									
									
									</tbody>
								</table>
								</div>
							 </td>
							</tr>
            </c:forEach>		
						
						
						
							
							

							</tbody>
					</table>
					
					
					<c:set var="userinfo" value="${fns:getUser()}"/>
					<input type="hidden" id="tempUserName" value="${userinfo.loginName }"/>
					</form>
					
					
					
					
					
					
					
					
					
					
					
					<div class="pagination clearFix">${page}
					</div>

	 
</div>
                <!--右侧内容部分结束-->
            </div>
        </div>
	</div>
    <!--footer-->
    <div class="bs-footer">
        <p>客服电话：010-85718666  <br/>Copyright © 2012-${fns:getConfig('copyrightYear')} 接待社交易管理后台</p>
        <div class="footer-by">Powered By Trekiz Technology</div>
    </div>
    <!--footer***end-->
</div>
</body>
</html>
