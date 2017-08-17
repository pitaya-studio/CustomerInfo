<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>已上架产品</title>
	<meta name="decorator" content="wholesaler"/>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/activity/dynamic.group.validator.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/jquery.number.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>
	<script type="text/javascript">
	
		/*
		 *4 t1t2 打通：
		 *散拼产品发布  点击修改时获取成人、儿童、特殊人群的 价格策略。
		 *在方法  function addQuauqPrice()中  为
		 *成人、儿童、特殊人群的 价格策略 赋值
		 */
		/* var adultQuauqPriceStrategy; //成人价策略      结构'1:200,2:10'
		var childrenQuauqPriceStrategy;//儿童价策略     结构'1:100,2:10'
		var spicalQuauqPriceStratety;//特殊人群价策略     结构'2:200,3:15' */
		var paramKind = '${activityKind}';
	
	
	
		var activityIds = "";
		$(function() {
			g_context_url = "${ctx}";
			
			
			$( ".spinner" ).spinner({
				spin: function( event, ui ) {
				if ( ui.value > 365 ) {
					$( this ).spinner( "value", 1 );
					return false;
				} else if ( ui.value <= 0 ) {
					$( this ).spinner( "value", 365 );
					return false;
				}
				}
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
       	   $('.team_top').find('.table_activity_scroll').each(function(index, element) {
			 var _gg=$(this).find('tr').length;
            if(_gg>=20){
            $(this).addClass("group_h_scroll");
            $(this).prev().wrap("<div class='group_h_scroll_top'></div>");
            }
		  });
			
		});
		
		//条件重置
		var resetSearchParams = function() {
			$(':input', '#searchForm').not(':button, :submit, :reset, :hidden')
					.val('').removeAttr('checked').removeAttr('selected');
			$('#wholeSalerKey').val('');
			$('#estimatePriceRecordUserName').val('');
			$('#fromArea').val('');
			$('#targetAreaId').val('');			
			$('#groupOpenDate').val('');			
			$('#groupCloseDate').val('');			
			$('#createName').val('');			
			$('#groupLead').val('');			
			$('#trafficName').val('');			
			$('#backArea').val('');			
			$('#activityLevelId').val('');			
			$('#activityDuration').val('');			
			$('#selectCurrencyType').val('1');			
			$('#settlementAdultPriceStart').val('');			
			$('#settlementAdultPriceEnd').val('');			
			$('#activityDuration').val('');			
			$('#travelTypeId').val('');			
			$('#productser').val('');		
			$('#productType').val('');
			$('#sousuo').show();
		};
		
		
		$(document).ready(function() {
			
			//保存筛选选中的币种信息
			var selectCurrencyType = "${travelActivity.currencyType}";
			if(null != selectCurrencyType || "" != selectCurrencyType){
				$("#selectCurrencyType option").each(function(){
		    		var txt = $(this).val();
		    		if(txt == selectCurrencyType){
		    			$(this).attr("selected","true");
		    		}
		    	});
			}
		
			activityIds = "${activityIds}";
		 	//$("#groupform").validate({});
		 	jQuery.extend(jQuery.validator.messages, {
		  		required: "必填信息",
		  		digits:"请输入正确的数字",
		  		number : "请输入正确的数字价格"
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
			
			
			$("#targetAreaId").val("${travelActivity.targetAreaIds}");
			$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
			
			$('.handle').hover(function() {
				if(0 != $(this).find('a').length){
					$(this).addClass('handle-on');
					$(this).find('dd').addClass('block');
		    	}
			},function(){$(this).removeClass('handle-on');$(this).find('dd').removeClass('block');});
			//展开筛选按钮
			$('.zksx').click(function(){
				if($('.ydxbd').is(":hidden")==true)
				{
					$('.ydxbd').show();
					$(this).text('收起筛选');
					$(this).addClass('zksx-on');
				}else
				{
					$('.ydxbd').hide();
					$(this).text('展开筛选');
					$(this).removeClass('zksx-on');
				}
			});
			//如果展开部分有查询条件的话，默认展开，否则收起	
			var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
			var searchFormselect = $("#searchForm").find("select").not("#selectCurrencyType");
			var inputRequest = false;
			var selectRequest = false;
			for(var i = 0; i<searchFormInput.length; i++) {
				if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
					inputRequest = true;
				}
			}
			for(var i = 0; i<searchFormselect.length; i++) {
				if($(searchFormselect[i]).children("option:selected").val() != "" && 
						$(searchFormselect[i]).children("option:selected").val() != "100" &&
						$(searchFormselect[i]).children("option:selected").val() != null) {
					selectRequest = true;
				}
			}
			if(inputRequest||selectRequest) {
				$('.zksx').click();
			}
			
		});
		
		function expand(child,obj,srcActivityId){
			if($(child).css("display")=="none"){ 
                if("${userType}"=="1") {
                    $.ajax({
                        type:"POST",
                        url:"${ctx}/stock/manager/payReservePosition",
                        data:{
                            srcActivityId:srcActivityId
                        },
                        success:function(msg) {
                        	$(obj).html("关闭全部团期");
                            $(child).show();
                            $(obj).parents("td").attr("class","td-extend");
                            $(obj).parents("td").addClass("td-extend").parent("tr").addClass("tr-hover");
                        	if(msg.length>0){
                                $(child+" [class^='soldPayPosition']").show();
                        	}
                        	$.each(msg,function(keyin,valuein){
                                $("td .soldPayPosition"+(valuein.activityGroupId)).find("span").text(valuein.leftpayReservePosition);
                        	});
                        }
                    });
                }else{
                	$(obj).html("关闭全部团期");
                    $(child).show();
                    $(obj).parents("td").attr("class","td-extend");
                    $(obj).parents("td").addClass("td-extend").parent("tr").addClass("tr-hover");
                }
            }else{
                    $(child).hide();
                    $(obj).parents("td").attr("class","");
                    $(obj).html("展开全部团期");
                    $(obj).parents("td").removeClass("td-extend").parent("tr").removeClass("tr-hover");
            }
        }
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/activity/manager/list/${travelActivity.activityStatus }/${activityKind}?newflag=newflag");
			$("#searchForm").submit();
	    }
	    // 删除确认对话框
		function confirmxCopy(mess,id,proId,obj,child){
			top.$.jBox.confirm(mess,'系统提示',function(v){
				if(v=='ok'){
				$.ajax({
						type: "POST",
						async:false,
						url: "${ctx}/activity/manager/hasOrder",
						data: {"groupId":id},
						success: function(result){
							if(result.data=="true" || result.data== true){
								top.$.jBox.info("团期已存在占位，不能删除", "警告");
								return;
							}else{
								$.getJSON("${ctx}/activity/manager/delgroup2/"+$("#activityStatus").val()+"?id="+id+"&proId="+proId,
								function(result){
									if("success"==result.flag){
										if(result.settlementAdultPrice)
											$("#settleadultprice"+proId).html("¥"+"<span class=\"tdred fbold\">"+result.settlementAdultPrice+"</span>起");
										else
											$("#settleadultprice"+proId).html("价格待定");
										
										if(result.suggestAdultPrice)
											$("#suggestadultprice"+proId).html("¥"+"<span class=\"tdblue fbold\">"+result.suggestAdultPrice+"</span>起");
										else
											$("#suggestadultprice"+proId).html("价格待定");
										
										if(result.groupOpenDate && result.groupCloseDate){
											if(result.groupOpenDate == result.groupCloseDate)
												$("#groupdate"+proId).find("span").html(result.groupOpenDate);
												else
												$("#groupdate"+proId).find("span").html(result.groupOpenDate+"至"+result.groupCloseDate);
										}else
											$("#groupdate"+proId).html("日期待定");
											
										$(obj).parent().parent().remove();
										if($("#"+child+" tbody").find("tr").length==0){
											$("#"+child).hide();
											$("#groupdate"+proId).removeClass("td-extend");
										}
									}								
									else
									$.jBox.info("删除失败,请联系管理员",'系统提示');
									}
								);
							}
						}
						});			
				
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
			return false;
		}
		function confirmBatchIsNull(mess,sta) {
			if($("#contentTable").find("input[name='activityId']:checked").length != 0){
				if(sta=='off'){
					confirmBatchOff(mess);
				}else if(sta=='del'){
					confirmBatchDel(mess);
				}
			}else{
				$.jBox.info('未选择产品','系统提示');
			}
		}
		// 批量删除确认对话框
		function confirmBatchDel(mess){
			top.$.jBox.confirm(mess,'系统提示',function(v){
			if(v=='ok'){
				loading('正在提交，请稍等...');
				$("#searchForm").attr("action","${ctx}/activity/manager/batchdel/"+activityIds+"/${activityKind}");
				$("#searchForm").submit();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
			return false;
		}
		// 批量下架确认对话框
		function confirmBatchOff(mess){
			top.$.jBox.confirm(mess,'系统提示',function(v){
			if(v=='ok'){
				loading('正在提交，请稍等...');
				$("#searchForm").attr("action","${ctx}/activity/manager/batchoff/"+activityIds+"/${activityKind}");
				$("#searchForm").submit();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
			return false;
		}
		function confirmxDel(mess,proId){
			top.$.jBox.confirm(mess,'系统提示',function(v){
			if(v=='ok'){
				loading('正在提交，请稍等...');
				$("#searchForm").attr("action","${ctx}/activity/manager/del/" + proId + "/1/${activityKind}");
				$("#searchForm").submit();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
			return false;
		}
	    
	    function getSoldPayPosition(obj) {
	    	
	    }
	    
	    
	  //修改产品,匹配quauq策略,对旧数据的quauq价进行处理-----t1t2--点击修改按钮后,对quauq价和供应价的出路--djw------------------
	    function modgroup(groupid,savebtn,delbtn,cancelbtn,obj){
	    	debugger;
		    /* var activityKind = '${activityKind}';
		  	//获取产品基本策略匹配参数
		    if(activityKind == '2'){
				var fromArea = $(obj).parent().parent().find("input[name='fromAreaNum']").val(); //出发城市
				var targetAreaId = $(obj).parent().parent().find("input[name='targetAreaIds']").val(); //目的地
				var travelTypeId = $(obj).parent().parent().find("input[name='travelTypeId']").val(); //旅游类型
				var activityTypeId = $(obj).parent().parent().find("input[name='activityTypeId']").val(); //产品类型
				var activityLevelId = $(obj).parent().parent().find("input[name='activityLevelId']").val(); //产品系列
		    	var flag = false;
					
				$.ajax({
						type: "POST",
						async:false,
						url: "${ctx}/activity/manager/checkActivityPriceStrategy",
						data: {"fromArea":fromArea,"targetAreaId":targetAreaId,"travelTypeId":travelTypeId,"activityTypeId":activityTypeId,"activityLevelId":activityLevelId},
						success: function(data){
							//debugger;
							if(data.result=="0"){
								/* top.$.jBox.info("未匹配到相关渠道价格策略，请配置相关策略。", "警告",{
									buttons: {"取消": "0", "新建策略": "1"},
									submit:function(v,h,f){
										if(v=='1'){
											window.location.href ="${ctx}/pricingStrategy/manager/addt";  //新开页面
											//window.open("${ctx}/pricingStrategy/manager/addt");    //不新开页面
										}else{
											//alert("000");
										}
									}
								}); */
							/*	flag = true; 
								adultQuauqPriceStrategy = null; //成人价策略    
						         childrenQuauqPriceStrategy = null;//儿童价策略 
						         spicalQuauqPriceStratety = null;
								var settlementAdultPrice = $(obj).parents('tr:first').find("input[name='settlementAdultPrice']").val();
								var settlementChildPrice = $(obj).parents('tr:first').find("input[name='settlementcChildPrice']").val();
								var settlementSpecalPrice = $(obj).parents('tr:first').find("input[name='settlementSpecialPrice']").val();
								$(obj).parents('tr:first').find("input[name='quauqAdultPrice']").val(settlementAdultPrice);
								$(obj).parents('tr:first').find("input[name='quauqChildPrice']").val(settlementChildPrice);
								$(obj).parents('tr:first').find("input[name='quauqSpecialPrice']").val(settlementSpecalPrice);
								
								
							}else{
								 //quauqPrice4Adult##2:12#2:12,3:2#2:13
								 //quauqPrice4Child##2:13#2:1#2:14
								 //quauqPrice4SpicalPerson##2:15
								 adultQuauqPriceStrategy = data.quauqPrice4Adult; //成人价策略      结构'1:200,2:10#1:100,2:10'  加200 减 10，
						         childrenQuauqPriceStrategy = data.quauqPrice4Child;//儿童价策略     结构'1:100,2:10'  加100减10
						         spicalQuauqPriceStratety = data.quauqPrice4SpicalPerson;//特殊人群价策略     结构'2:200,3:15'  减200，打15折 
							} 
						}
				});
				
				if(flag == false){
					//return;
	    			updateQuauqPrice(obj);
				}
			}    	 */
	    	
	    	
	    	$(obj).parent().parent().find("span").hide();
	    	$(obj).parent().parent().find("span").eq(0).show();
	    	$(obj).parent().parent().find("span[class='rm']").show();
	    	$(obj).parent().parent().find("input[type='text']").css("display","inline-block");
	    	$(obj).parent().parent().find("input[type='text']").attr("disabled",false);
	    	$(groupid).hide();
	    	$(groupid).attr("disabled",false);
	    	$(savebtn).show();
	    	$(delbtn).hide();
	    	$(cancelbtn).show();
	    	$(obj).hide();
	    }
		function cancelgroup(modbtn,savebtn,delbtn,obj){
			$(modbtn).show();
	    	$(savebtn).hide();
	    	$(delbtn).show();
	    	$(obj).hide();
			$(obj).parent().parent().find("span").show();
	    	$(obj).parent().parent().find("input[type='text']").css("display","none");
	    	$(obj).parent().parent().find("input[type='text']").attr("disabled",true);
	    	$(obj).parent().parent().find("label",".error").remove();
	    }
	    function downloads(docid,activitySerNum,acitivityName,iszip){
	    	if(iszip){
	    		var zipname = activitySerNum;
	    		window.open(encodeURI(encodeURI("${ctx}/sys/docinfo/zipdownload/"+docid+"/"+zipname)));
	    	}
	    		
	    	else
	    		window.open("${ctx}/sys/docinfo/download/"+docid);
	    }
	    //产品修改
	    function productModify(proId){
	    	$("#searchForm").attr("action","${ctx}/activity/manager/mod/"+proId+"/0");
			$("#searchForm").submit();
	    }	
	    
	    //占位
	    function occupied(id,srcActivityId,payMode){
	    	
	    	if(payMode=="1"){
                //dingj zhanwei 
                window.open("${ctx}/orderCommon/manage/showforModify?type=2&productId="+srcActivityId+"&productGroupId="+id);
            }else if(payMode=="2"){
                //zanwei
                window.open("${ctx}/orderCommon/manage/showforModify?type=3&productId="+srcActivityId+"&productGroupId="+id);
            }
        }
	    //预订
	    function reserveOrder(id,srcActivityId){
	    	window.open("${ctx}/orderCommon/manage/showforModify?type=1&productId="+srcActivityId+"&productGroupId="+id);
        }
    	
	    $(function(){
			$('.team_a_click').toggle(function(){
				$(this).addClass('team_a_click2');
			},function(){
				$(this).removeClass('team_a_click2');
			});	
		 });
	    
	    function getCurDate(){
			var curDate = new Date();
			return curDate.getFullYear()+"-"+(curDate.getMonth()+1)+"-"+(curDate.getDate()+1);
		}
	  //控制截团时间	
		function takeOrderOpenDate(obj){
			var groupOD = $('#'+obj).parent().parent().find("input[name='groupOpenDate']").eq(0).val();
			return groupOD;
		}
		function takeModVisaDate(obj) {
			var groupOD = $('#'+obj).parent().parent().find("input[name='groupOpenDate']").eq(0).val();
			return groupOD;
		}
		function comparePositionMod(obj){
    		var plan = $(obj).val();
    		$(obj).parent().next().find("input").val(plan);
    		$(obj).parent().next().find("input").focus();
    		$(obj).parent().next().find("input").blur();
    	}
		
		function checkall(obj){
			if($(obj).attr("checked")){
				$("input[name='allChk']").attr("checked",'true');
				$("input[name='activityId']").attr("checked",'true');
				$("input[name='activityId']:checked").each(function(i,a){
					var arr = activityIds.split(",");
					if(arr.indexOf(a.value) < 0){
						activityIds = activityIds + a.value+",";
					}
  				});
			}
			else{
				$("input[name='allChk']").removeAttr("checked");
				$("input[name='activityId']").removeAttr("checked");
				$("input[name='activityId']").each(function(i,a){
					var arr = activityIds.split(",");
					if(arr.indexOf(a.value) >= 0){					
						activityIds = activityIds.replace(a.value+",","");
					}
  				});
			}
			$("#activityIds").val(activityIds);
		}
		function idcheckchg(obj){
			var value = $(obj).val();
			if($(obj).attr("checked")){
				if(activityIds.indexOf($(obj).val()) < 0){
					activityIds = activityIds+$(obj).val()+",";
				}
			}			
			else{
				if($("input[name='allChk']").attr("checked"))
					$("input[name='allChk']").removeAttr("checked");
				if(activityIds.indexOf($(obj).val()) >= 0){
					
					activityIds = activityIds.replace($(obj).val()+",","");
				}
			}
			$("#activityIds").val(activityIds);
				
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
		
		
	function downProduct(activityId){
		
		$.jBox.confirm("确定要下架该产品吗？", "提示", function(v, h, f){
            if (v == 'ok') {
		            	$("#searchForm").attr("action","${ctx}/activity/manager/batchoff/"+activityId+"/${activityKind}");
		                $("#searchForm").submit();
                    }else if (v == 'cancel'){
                        
                    }
                });
		top.$('.jbox-body .jbox-icon').css('top','55px');
		
	}
		
	
	//导出团期中关于游客信息
	function exportExcel(groupId, status) {
		var group_id = "#groupId" + groupId;
		var groupCode = $(group_id).html();
		$.ajax({
	        type: "POST",
	        url: "${ctx}/activity/manager/existExportData",
	        dataType:"json",
	        cache:false,
	        data:{groupId : groupId, status : status},
	        success : function(result){
	        	var data = eval(result);
	        	if(data && data[0].flag == "true") {
	        		$("#groupId").val(groupId);
	        		$("#groupCode").val(groupCode);
					$("#exportForm").submit();
	        	} else {
	        		var tips = data[0].warning;
	        		top.$.jBox.info(tips, "警告", { width: 250, showType:"slide", icon: "info",draggable: "true" });
					top.$('.jbox-body .jbox-icon').css('top','55px');
	        	}
			}
		});
	}	

	function getDepartment(departmentId) {
		$("#departmentId").val(departmentId);
		$("#searchForm").submit();
	}
	
	function chooseActivityKinds() {
		var _select = $("<select id='activityKinds'></select>")
				.append($('<option value=\"2"\>散拼</option><option value=\"1"\>单团</option><option value=\"3"\>游学</option><option value=\"4"\>大客户</option><option value=\"5"\>自由行</option>'));
		
		var $div = $("<div id='chooseQd' class=\"tanchukuang\"></div>")
				.append($('<div class="add_allactivity choseAgent"></div>')
			    .append($("<label>类型选择：</label>")).append(_select))
	            .append('<div class="ydBtn"><div class="btn btn-primary ydbz_x" onclick="javascript:window.location.href=\'${ctx}/activity/manager/form?kind=\'+$(\'#activityKinds\').val();$(\'.jbox-close\').click();">开始发布</div>');
	             var html = $div.html();
	             $.jBox(html, { title: "发布-产品类型选择", buttons:{},height:220,width:550});
        $("#activityKinds").children("[value='${activityKind}']").attr("selected",true);
	             
	}
	
	
	
	//c452,c453  大洋87
	function openGroupLibPage(){
		//debugger;
		//groupcodelibtype = 0 为机票
	    $.jBox("iframe:"+g_context_url+"/activity/groupcodelibrary/toGroupcodeLibraryBox?groupcodelibtype="+${activityKind},{  //groupcodelibtype =7 为机票
	        title:"团号库",buttons:{'关闭':1},height:680,width:680,persistent:true
	    }).find("#jbox-content").css("overflow","hidden");
	}
	
	//-----------t1t2-----begin-----------------------------------------------------------------------------------------------    
	/* function addQuauqPrice(obj){
		//debugger;
	    var activityKind = '${activityKind}';
	  	//获取产品基本策略匹配参数
	    if(activityKind == '2'){
			var fromArea = $(obj).parent().parent().find("input[name='fromAreaNum']").val(); //出发城市
			var targetAreaId = $(obj).parent().parent().find("input[name='targetAreaIds']").val(); //目的地
			var travelTypeId = $(obj).parent().parent().find("input[name='travelTypeId']").val(); //旅游类型
			var activityTypeId = $(obj).parent().parent().find("input[name='activityTypeId']").val(); //产品类型
			var activityLevelId = $(obj).parent().parent().find("input[name='activityLevelId']").val(); //产品系列
	    	var flag = false;
				
			$.ajax({
					type: "POST",
					async:false,
					url: "${ctx}/activity/manager/checkActivityPriceStrategy",
					data: {"fromArea":fromArea,"targetAreaId":targetAreaId,"travelTypeId":travelTypeId,"activityTypeId":activityTypeId,"activityLevelId":activityLevelId},
					success: function(data){
						//debugger;
						if(data.result=="0"){
							top.$.jBox.info("未匹配到相关渠道价格策略，请配置相关策略。", "警告",{
								buttons: {"取消": "0", "新建策略": "1"},
								submit:function(v,h,f){
									if(v=='1'){
										window.location.href ="${ctx}/pricingStrategy/manager/addt";  //新开页面
										//window.open("${ctx}/pricingStrategy/manager/addt");    //不新开页面
									}else{
										//alert("000");
									}
								}
							});
							flag = true;
						}else{
							 //quauqPrice4Adult##2:12#2:12,3:2#2:13
							 //quauqPrice4Child##2:13#2:1#2:14
							 //quauqPrice4SpicalPerson##2:15
							 adultQuauqPriceStrategy = data.quauqPrice4Adult; //成人价策略      结构'1:200,2:10#1:100,2:10'  加200 减 10，
					         childrenQuauqPriceStrategy = data.quauqPrice4Child;//儿童价策略     结构'1:100,2:10'  加100减10
					         spicalQuauqPriceStratety = data.quauqPrice4SpicalPerson;//特殊人群价策略     结构'2:200,3:15'  减200，打15折 
						} 
					}
			});
		}		    	
	} */
	
	
	/**
     * t1t2 打通 
     * 产品发布时团期的   同行价  的keyup 事件要做特殊处理   需要 同步 修改  相应的 quauq 价
     * 正负数字验证
     */
     function validNum(dom,groupid,activityId){
      	
      	//debugger;
      	var activityKind = paramKind;  
      	var thisvalue = $(dom).val();
 		//t1t2增加供应价服务费计算，在QUAUQ价基础上增加1%的交易服务费
 		var rate = new Number('${chargeRate}')+1;
// 		if(""!=thisvalue){
// 			supplyvalue = thisvalue * rate + parseFloat(thisvalue);
// 		}
      	if(thisvalue.length >15){
      		top.$.jBox.info("改价金额位数不合法", "提示");
      		thisvalue = '0.00';
      	}

      	var minusSign = false;
      	if(thisvalue){
      		if(/^\-/.test(thisvalue)){
      			minusSign = true;
      			thisvalue = thisvalue.substring(1);
      		}
      		thisvalue = thisvalue.replace(/[^\d\.]/g,"").replace(/(\.\d{3}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
      		var txt = thisvalue.split(".");
      		thisvalue = txt[0]+(txt.length>1?"."+txt[1]:"");
      		if(minusSign){
      			thisvalue = thisvalue;
      		}
      		$(dom).val(thisvalue);
      	}
      	
      	
      	//debugger;
      	if(activityKind=='2'){//只有散拼产品才做如下操作
      		
      		var inputName = $(dom).attr("name");
      		
      		/*
      		 *同行价发布时修改处理
      		 *1.如同行价修改后的值不为空，则要重新计算相应的quauq价
      	     *2.如同行价修改后的值为空，把相应的quauq价置空，且变为只读状态
      	     *
      		 */
      		if(inputName == "settlementAdultPrice"){//同行成人
      			
      			
      			if(""!=thisvalue){
      				//if(adultQuauqPriceStrategy != null){
 	         			//同行不为空后，相应的quauq价  要 变得  可 编辑
 	         			$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").removeAttr("readonly");
 	     				var adultQuauqPrice = getQuauqPrice(groupid,activityId,inputName,thisvalue);
 	         			$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").val(adultQuauqPrice);
 						//增加供应价成人
 						$(dom).parents('tr:first').find("input[name='supplyAdultPrice']").val(xround(adultQuauqPrice*rate,2));
      				//}else{
      				//	$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").val(thisvalue);
          				//$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").attr("readonly","readonly");
 					//	$(dom).parents('tr:first').find("input[name='supplyAdultPrice']").val(supplyvalue);
      				//}
      			}else{
      				$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").val("");
      				$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").attr("readonly","readonly");
      				//增加供应价成人
 					$(dom).parents('tr:first').find("input[name='supplyAdultPrice']").val("");
      			}
      			    			
      		}else if(inputName == "settlementcChildPrice"){//同行儿童(请注意:settlementcChildPrice中多添加了一个c)
      			
      			if(""!=thisvalue){
      				//if(childrenQuauqPriceStrategy != null){
 	     			   // var childrenQuauqPrice = getQuauqPrice(thisvalue,childrenQuauqPriceStrategy);
 	     			    //同行不为空后，相应的quauq价  要 变得  可 编辑
 	     			    $(dom).parents('tr:first').find("input[name='quauqChildPrice']").removeAttr("readonly");
 	     			    var quauqChildPrice = getQuauqPrice(groupid,activityId,inputName,thisvalue);
 	     			    $(dom).parents('tr:first').find("input[name='quauqChildPrice']").val(quauqChildPrice);
 						//增加供应价儿童
 						$(dom).parents('tr:first').find("input[name='supplyChildPrice']").val(xround(quauqChildPrice*rate,2));
      				//}else{
      				//	$(dom).parents('tr:first').find("input[name='quauqChildPrice']").val(thisvalue);
          				//$(dom).parents('tr:first').find("input[name='quauqChildPrice']").attr("readonly","readonly");
 					//	$(dom).parents('tr:first').find("input[name='supplyChildPrice']").val(supplyvalue);
      				//}
      			}else{
      				$(dom).parents('tr:first').find("input[name='quauqChildPrice']").val("");
      				$(dom).parents('tr:first').find("input[name='quauqChildPrice']").attr("readonly","readonly");
      				//增加供应价儿童
 					$(dom).parents('tr:first').find("input[name='supplyChildPrice']").val("");
      			}
      			
      		}else if(inputName == "settlementSpecialPrice"){//同行特殊人群
      			
      			if(""!=thisvalue){
      				//if(spicalQuauqPriceStratety != null){
 	     			    //var spicalQuauqPrice = getQuauqPrice(thisvalue,spicalQuauqPriceStratety);
 	     			    //同行不为空后，相应的quauq价  要 变得  可 编辑
 	     			    $(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").removeAttr("readonly");
 	     			    var quauqSpecialPrice = getQuauqPrice(groupid,activityId,inputName,thisvalue);
 	     			    $(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").val(quauqSpecialPrice);
 						//增加供应价特殊人群
 						$(dom).parents('tr:first').find("input[name='supplySpecialPrice']").val(xround(quauqSpecialPrice*rate,2));
      				//}else{
      				//	$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").val(thisvalue);
          				//$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").attr("readonly","readonly");
 					//	$(dom).parents('tr:first').find("input[name='supplySpecialPrice']").val(supplyvalue);
      				//}
      			}else{
      				$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").val("");
      				$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").attr("readonly","readonly");
      				//增加供应价特殊人群
 					$(dom).parents('tr:first').find("input[name='supplySpecialPrice']").val("");
      			}
      			
      		}
      		/*
      	     *quauq价发布时修改处理
      	     *1.如quauq价修改时高 于 quauq 价，要给出提示信息，告知quauq价不能高于多少多少
      	     *2.如低改不做任何处理
      	     *3.如低改空    是否置为0
      	     *4.
      		 */
 			else if(inputName == "quauqAdultPrice"){//quauq成人
     			var settlementAdultPrice = $(dom).parents('tr:first').find("input[name='settlementAdultPrice']").val();
     			var quauqAdultPrice = getQuauqPrice(groupid,activityId,inputName,settlementAdultPrice);
     			if(""!=thisvalue){
     				//获取同行价,由同行价计算出现有策略下的quauq价,与输入的quauq价对比
     			//	var adultQuauqPrice;
     			//	if(null != adultQuauqPriceStrategy){
     			//		adultQuauqPrice = getQuauqPrice(settlementAdultPrice,adultQuauqPriceStrategy);
     			//	}else{
     			//		adultQuauqPrice = settlementAdultPrice;
     			//	}
     				if(new Number(thisvalue)>new Number(quauqAdultPrice)){
     					if(settlementAdultPrice == quauqAdultPrice){
 	    					top.$.jBox.info("QUAUQ价应低于或等于同行价，请重新修改！", "提示");
     					}else{
     						
 	    					top.$.jBox.info("QUAUQ价低于渠道最低价，请重新修改！", "提示");
     					}
     					$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").val(quauqAdultPrice);
 						$(dom).parents('tr:first').find("input[name='supplyAdultPrice']").val(xround(quauqAdultPrice*rate,2));
     				}else {
 						//增加供应价成人变化
 						$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").val(thisvalue);
 						$(dom).parents('tr:first').find("input[name='supplyAdultPrice']").val(xround(new Number(thisvalue)*rate,2));
 					}
     				
     			}else{
     				//增加供应价成人变化
 					$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").val("");
 					$(dom).parents('tr:first').find("input[name='supplyAdultPrice']").val("");
     			//	var settlementAdultPrice = $(dom).parents('tr:first').find("input[name='settlementAdultPrice']").val();
     			//	if(""!=settlementAdultPrice && null != adultQuauqPriceStrategy){
 	    		//		$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").val("0");
     			//	}else{
     			//		$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").val("");
     			//	} 
     			}
     			
     		}else if(inputName == "quauqChildPrice"){//quauq儿童
 				if(""!=thisvalue){
     				
 					var settlementcChildPrice = $(dom).parents('tr:first').find("input[name='settlementcChildPrice']").val();
 					var quauqChildPrice = getQuauqPrice(groupid,activityId,inputName,settlementcChildPrice);
 				//	if(null != childrenQuauqPriceStrategy){
     			//		childrenQuauqPrice = getQuauqPrice(settlementcChildPrice,childrenQuauqPriceStrategy);
 				//	}else{
 				//		childrenQuauqPrice = settlementcChildPrice;
 				//	}
     				if(new Number(thisvalue)>new Number(quauqChildPrice)){
     					if(settlementcChildPrice == quauqChildPrice){
 	    					top.$.jBox.info("QUAUQ价应低于或等于同行价，请重新修改！", "提示");
     					}else{
     						
 	    					top.$.jBox.info("QUAUQ价低于渠道最低价，请重新修改！", "提示");
     					}
     					$(dom).parents('tr:first').find("input[name='quauqChildPrice']").val(quauqChildPrice);
 						$(dom).parents('tr:first').find("input[name='supplyChildPrice']").val(quauqChildPrice*rate);
     				}else {
 						//增加供应价儿童变化
 						$(dom).parents('tr:first').find("input[name='quauqChildPrice']").val(thisvalue);
 						$(dom).parents('tr:first').find("input[name='supplyChildPrice']").val(xround(new Number(thisvalue)*rate,2));
 					}
 					
     			}else{
     				//增加供应价儿童变化
 					$(dom).parents('tr:first').find("input[name='quauqChildPrice']").val("");
 					$(dom).parents('tr:first').find("input[name='supplyChildPrice']").val("");
     			//	var settlementcChildPrice = $(dom).parents('tr:first').find("input[name='settlementcChildPrice']").val();
     			//	if(""!=settlementcChildPrice && null != childrenQuauqPriceStrategy){
     			//		$(dom).parents('tr:first').find("input[name='quauqChildPrice']").val("0");
     			//	}else{
     			//	    $(dom).parents('tr:first').find("input[name='quauqChildPrice']").val("");
     			//	}
     			}
     			
     		}else if(inputName == "quauqSpecialPrice"){//quauq特殊人群
 				if(""!=thisvalue){
     				
 					var settlementSpecialPrice = $(dom).parents('tr:first').find("input[name='settlementSpecialPrice']").val();
 					var quauqSpecialPrice = getQuauqPrice(groupid,activityId,inputName,settlementSpecialPrice);;
 				//	if(null != spicalQuauqPriceStratety){
     			//		spicalQuauqPrice = getQuauqPrice(settlementSpecialPrice,spicalQuauqPriceStratety);
 				//	}else{
 				//		spicalQuauqPrice = settlementSpecialPrice;
 				//	}
     				if(new Number(thisvalue)>new Number(quauqSpecialPrice)){
     					if(settlementSpecialPrice == quauqSpecialPrice){
 	    					top.$.jBox.info("QUAUQ价应低于或等于同行价，请重新修改！", "提示");
     					}else{
     						
 	    					top.$.jBox.info("QUAUQ价低于渠道最低价，请重新修改！", "提示");
     					}
     					$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").val(quauqSpecialPrice);
 						$(dom).parents('tr:first').find("input[name='supplySpecialPrice']").val(quauqSpecialPrice*rate);
     				}else {
 						//增加供应价特殊人群变化
 						$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").val(thisvalue);
 						$(dom).parents('tr:first').find("input[name='supplySpecialPrice']").val(xround(new Number(thisvalue)*rate,2));
 					}
 					
     			}else{
     				//增加供应价特殊人群变化
 					$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").val("");
 					$(dom).parents('tr:first').find("input[name='supplySpecialPrice']").val("");
     			//	var settlementSpecialPrice = $(dom).parents('tr:first').find("input[name='settlementSpecialPrice']").val();
     			//	if(""!=settlementSpecialPrice && null != spicalQuauqPriceStratety){
     			//		$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").val("0");
     			//	}else{
     			//		$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").val("");
     			//	}
     				
     			}
      			
      		}
      	
      	}
      	
      }
	
	/**
	 * 点击修改时,修改老数据的quauq价
	 * 
	 */
	/*  function updateQuauqPrice(obj){
		//alert(${group.quauqAdultPrice});
		//adultQuauqPrice,childrenQuauqPrice,spicalQuauqPrice,
		var adultQuauqPrice=$(obj).parent().parent().find("input[name='quauqAdultPrice']").val();
		var childrenQuauqPrice=$(obj).parent().parent().find("input[name='childrenQuauqPrice']").val();
		var spicalQuauqPrice=$(obj).parent().parent().find("input[name='spicalQuauqPrice']").val();
		if(paramKind == '2'){//只有散拼产品才做如下操作
			debugger;
			var settlementAdultPrice = $(obj).parents('tr:first').find("input[name='settlementAdultPrice']").val();
			var settlementChildPrice = $(obj).parents('tr:first').find("input[name='settlementcChildPrice']").val();
			var settlementSpecalPrice = $(obj).parents('tr:first').find("input[name='settlementSpecialPrice']").val();
			var adultQuauqPrice_temp = getQuauqPrice(settlementAdultPrice, adultQuauqPriceStrategy);
			var childQuauqPrice_temp = getQuauqPrice(settlementChildPrice, childrenQuauqPriceStrategy);
			var specalQuauqPrice_temp = getQuauqPrice(settlementSpecalPrice, spicalQuauqPriceStratety); */
			/* if(adultQuauqPrice == null || adultQuauqPrice <= adultQuauqPrice_temp){
				return;
			} */
			
			//由于策略修改,或者页面修改 导致的quauq价变动,quauq价取计算出的quauq价和库中的quauq价的最小值
			//首先判断是否存在策略  
			/* if(adultQuauqPriceStrategy.length != 0){   //同行价策略不为空时
				if(settlementAdultPrice != null && settlementAdultPrice != ""){
					if(new Number(adultQuauqPrice) >= new Number(adultQuauqPrice_temp) || adultQuauqPrice == null || adultQuauqPrice == ""){
						adultQuauqPrice = adultQuauqPrice_temp;
						$(obj).parents('tr:first').find("input[name='quauqAdultPrice']").val(xround(adultQuauqPrice,2));
					}else{
						$(obj).parents('tr:first').find("input[name='quauqAdultPrice']").val(xround(adultQuauqPrice,2));
					}
				}else{
					adultQuauqPrice = "";
					$(obj).parent().parent().find("input[name='quauqAdultPrice']").parent().find("span[class!='rm']").html('');
					$(obj).parents('tr:first').find("input[name='quauqAdultPrice']").val('');
					$(obj).parents('tr:first').find("input[name='quauqAdultPrice']").attr("readonly","readonly");
				}
			}else{
				adultQuauqPrice = "";
				$(obj).parent().parent().find("input[name='quauqAdultPrice']").parent().find("span[class!='rm']").html('');
				$(obj).parents('tr:first').find("input[name='quauqAdultPrice']").val('');
				$(obj).parents('tr:first').find("input[name='quauqAdultPrice']").attr("readonly","readonly");
			}
			
			//儿童策略不为空时
			if(childrenQuauqPriceStrategy.length != 0){
				if(settlementChildPrice != null && settlementChildPrice != ""){
					if(new Number(childrenQuauqPrice) >= new Number(childQuauqPrice_temp) || childrenQuauqPrice == null || childrenQuauqPrice == ""){
						childrenQuauqPrice = childQuauqPrice_temp;
						$(obj).parents('tr:first').find("input[name='quauqChildPrice']").val(xround(childrenQuauqPrice,2));
					}else{
						$(obj).parents('tr:first').find("input[name='quauqChildPrice']").val(xround(childrenQuauqPrice,2));
					}
				}else{
					childrenQuauqPrice = "";
					$(obj).parent().parent().find("input[name='quauqChildPrice']").parent().find("span[class!='rm']").html('');
					$(obj).parents('tr:first').find("input[name='quauqChildPrice']").val('');
					$(obj).parents('tr:first').find("input[name='quauqChildPrice']").attr("readonly","readonly");
				}
			}else{
				childrenQuauqPrice = "";
				$(obj).parent().parent().find("input[name='quauqChildPrice']").parent().find("span[class!='rm']").html('');
				$(obj).parents('tr:first').find("input[name='quauqChildPrice']").val('');
				$(obj).parents('tr:first').find("input[name='quauqChildPrice']").attr("readonly","readonly");
			}
			
			//特殊人群策略不为空时
			if(spicalQuauqPriceStratety != 0){
				if(settlementSpecalPrice != null && settlementSpecalPrice != ""){
					if(new Number(spicalQuauqPrice) >= new Number(specalQuauqPrice_temp) || spicalQuauqPrice == null || spicalQuauqPrice == ""){
						spicalQuauqPrice = specalQuauqPrice_temp;
						$(obj).parents('tr:first').find("input[name='quauqSpecialPrice']").val(xround(spicalQuauqPrice,2));
					}else{
						$(obj).parents('tr:first').find("input[name='quauqSpecialPrice']").val(xround(spicalQuauqPrice,2));
					}
				}else{
					spicalQuauqPrice = "";
					$(obj).parent().parent().find("input[name='quauqSpecialPrice']").parent().find("span[class!='rm']").html('');
					$(obj).parents('tr:first').find("input[name='quauqSpecialPrice']").val('');
					$(obj).parents('tr:first').find("input[name='quauqSpecialPrice']").attr("readonly","readonly");
				}
			}else{
				spicalQuauqPrice = "";
				$(obj).parent().parent().find("input[name='quauqSpecialPrice']").parent().find("span[class!='rm']").html('');
				$(obj).parents('tr:first').find("input[name='quauqSpecialPrice']").val('');
				$(obj).parents('tr:first').find("input[name='quauqSpecialPrice']").attr("readonly","readonly");
			}
		} 
		
	}*/
	
	
    /**
	 * 根据价格方案获取策略价：最低quauq价
	 * 
	 */
	 function getQuauqPrice(groupid,activityId,inputName,srcPrice){
 		debugger;
 	    var quauqPrice;
			$.ajax({
				type:"post",
				async:false,
				dataType:"json",
				url:"${ctx}/activity/manager/getQuauqPrice",
				data:{"groupid":groupid,"activityId":activityId,"inputName":inputName,"srcPrice":srcPrice},
				success:function(result){
					quauqPrice = result;
				}
			});
 		return quauqPrice; 
	}
     /* function getQuauqPrice(srcPrice,srcPriceStrategy){
		
		//srcPriceStrategy 为空  直接 返回 """;
		if(!srcPriceStrategy){
			return "";
		}
		
		//debugger;
		var srcPriceStrategyArray = srcPriceStrategy.split("#");
		var quauqPriceArray = new Array();
		for(var i = 0;i < srcPriceStrategyArray.length; i++) {
			var quauqPrice = srcPrice;//
			var priceStrategyArray =  srcPriceStrategyArray[i].split(",");
			for(var j = 0;j < priceStrategyArray.length; j++) {
				var  srcPriceStrategyItem = priceStrategyArray[j].split(":");
				if("1" == srcPriceStrategyItem[0]){
					quauqPrice = new Number(quauqPrice) + new Number(srcPriceStrategyItem[1]);
				}else if("2" == srcPriceStrategyItem[0]){
					quauqPrice = new Number(quauqPrice) - new Number(srcPriceStrategyItem[1]);
				}else if("3" == srcPriceStrategyItem[0]){
					quauqPrice = new Number(quauqPrice)*new Number(100-new Number(srcPriceStrategyItem[1]))/100;
				}
			}
			if(quauqPrice<0){
				quauqPrice = 0;
			}
			quauqPriceArray.push(quauqPrice);
		}
		var minQuauqPrice = getMaxMinNum(quauqPriceArray,"min");
		//var maxQuauqPrice = getMaxMinNum(quauqPriceArray,"max");
		return  xround(minQuauqPrice,2);
		
	} */
    
	/**
	 *四舍五入，保留位数为
	 *num:要格式化的数字 
	 *scale: 保留的位数
	 */
	function xround(num,scale){
	    var resultTemp = Math.round(num * Math.pow(10, scale)) / Math.pow(10, scale);
	    return resultTemp.toFixed(2);
	}
    
    
	/**
	 *
	 * @param arr:array operated
	 * @param type:expected max,min
	 * @returns get max/min value in specified array or cosole log error
	 */
   function getMaxMinNum(arr,type){
       if(type==''||type==null||type=='undefined'){
           //console.log("Type is undefined.Please specified!");
           return false;
       }
       if('max'==type){
          return Math.max.apply(null,arr);
       }
       if('min'==type){
           return Math.min.apply(null,arr);
       }
   }
	
	
	
	//-------------t1t2------end--------------------------------------------------------------------------------------------	    
	

	</script>
</head>
<body>
	<page:applyDecorator name="activity_op_head" >
		<page:param name="current">online</page:param>
	</page:applyDecorator>
	
	<!-- 签证公告展示 -->
	<%@ include file="/WEB-INF/views/modules/visa/visaPublicBulletinMarquee.jsp"%>

	<shiro:hasPermission name="${shiroType}Product:operation:form">
		<p class="main-right-topbutt"><a class="primary"onclick="javascript:window.location.href='${ctx}/activity/manager/form?kind=${activityKind}'">发布新产品</a></p>
	</shiro:hasPermission>
	
	<div class="activitylist_bodyer_right_team_co_bgcolor">
	<form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/activity/manager/list/${travelActivity.activityStatus }/${activityKind}?newflag=newflag" method="post" >
		<input id="activityStatus" type="hidden" name="activityStatus" value="${travelActivity.activityStatus }"/>
		<input id="activityIds" type="hidden" name="activityIds" value="${activityIds }"/>
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<input id="departmentId" name="departmentId" type="hidden" value="${departmentId }" />
    	 <div class="activitylist_bodyer_right_team_co">
    	 
    	 	<div class="activitylist_bodyer_right_team_co2 pr">
	         	<input type="text" class="txtPro inputTxt searchInput"
					   id="wholeSalerKey" name="wholeSalerKey" value="${travelActivity.acitivityName }"
				placeholder="输入产品名称、团号，支持模糊匹配"/>
         	</div>
       		<div class="form_submit">
					<input class="btn btn-primary" type="submit" value="搜索"/>
					<input type="button" value="条件重置" onclick="resetSearchParams()" class="btn btn-primary" />
					<!-- c451,c453 团期产品  大洋  添加团号库 -->
					<c:if test="${fns:getUser().company.isNeedGroupCode eq 1}"> 
					    <input class="btn btn-primary " type="button" onclick="openGroupLibPage()" value="团号库"/>
					</c:if>
			</div>
			<div class="zksx">筛选</div>
		<div class="ydxbd" >
			
			<div class="activitylist_bodyer_right_team_co3">
	            <div class="activitylist_team_co3_text">销售：</div>
	            <input id="estimatePriceRecordUserName" name="estimatePriceRecordUserName" class="inputTxt"  value="${travelActivity.estimatePriceRecord.userName }"> 
	        </div>
			
			<div class="activitylist_bodyer_right_team_co1">  
                 <div class="activitylist_team_co3_text">目的地：</div>
                 <tags:treeselect id="targetArea" name="targetAreaIdList" value="${targetAreaIds}" labelName="targetAreaNameList"  labelValue="${targetAreaNames}"
                     title="区域" url="/activity/manager/filterTreeData" checked="true"/>
         	</div>
			<div class="activitylist_bodyer_right_team_co2">
				<label>出团日期：</label><input id="groupOpenDate" class="inputTxt dateinput" name="groupOpenDate" value='<fmt:formatDate value="${travelActivity.groupOpenDate }" pattern="yyyy-MM-dd"/>' onFocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" readonly/> 
				<span style="font-size:12px; font-family:'宋体';"> 至</span>  
				<input id="groupCloseDate" class="inputTxt dateinput" name="groupCloseDate" value='<fmt:formatDate value="${travelActivity.groupCloseDate }" pattern="yyyy-MM-dd"/>' onClick="WdatePicker()" readonly/>
        	</div>
         	<div class="kong"></div>
         	<c:if test="${activityKind ne 10 }">
				<div class="activitylist_bodyer_right_team_co3">
		            <div class="activitylist_team_co3_text">计调：</div>
		            <input id="createName" name="createName" class="inputTxt"  value="${travelActivity.createBy.name }"> 
		        </div>
				
				
           </c:if>
           <c:if test="${activityKind eq 10 }">
           	  <div class="activitylist_bodyer_right_team_co1">
			    <div class="activitylist_team_co3_text">返回城市：</div>
			    <form:select path="backArea" itemValue="key" itemLabel="value" id="backArea">      
	                <form:option value="" >不限</form:option>
	                <form:options items="${fromAreas}"/>
            	</form:select>
              </div>
			  <div class="activitylist_bodyer_right_team_co3">
				<div class="activitylist_team_co3_text">产品系列：</div>
				<form:select path="activityLevelId" itemValue="key"
					itemLabel="value" id="activityLevelId">
					<form:option value="">不限</form:option>
					<form:options items="${productLevels}" />
				</form:select>
			  </div>
				
			</c:if>
			<div class="activitylist_bodyer_right_team_co2 sCurrency">
				<label>同行价格：</label><select id="selectCurrencyType" name="currencyType">
<!-- 					<option value="1">人民币</option> -->
					<c:forEach items="${currencyList}" var="currency" varStatus="s">
						<c:if test="${currency.id != '1'}">
						<option value="${currency.id}">${currency.currencyName}</option>
						</c:if>
	                </c:forEach>
				</select>
				<input id="settlementAdultPriceStart" class="inputTxt" name="settlementAdultPriceStart" value="${settlementAdultPriceStart }" />
				<span style="font-size:12px;font-family:'宋体';"> 至</span>
				<input id="settlementAdultPriceEnd" class="inputTxt" name="settlementAdultPriceEnd" value="${settlementAdultPriceEnd }" />
         	</div>
         	
          </div>
          
<%--不使用的筛选条件====================			--%>
<%--   		<div class="activitylist_bodyer_right_team_co3">--%>
<%--            <div class="activitylist_team_co3_text">产品编号：</div>--%>
<%--           <input id="activitySerNum" name="activitySerNum" class="inputTxt"  value="${travelActivity.activitySerNum }"> --%>
<%--        </div>--%>
<%--	      <div class="kong"></div>--%>
<%----%>
<%--============================--%>
         </div>
	</form:form>
		<c:if test="${fn:length(page.list) ne 0}">
			<div class="activitylist_bodyer_right_team_co_paixu">
		        <div class="activitylist_paixu">
		          <div class="activitylist_paixu_left">
		             <ul>
		            	<li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
		            	<li class="activitylist_paixu_left_biankuang liid"><a onClick="sortby('id',this)">创建时间</a></li>
		            	<li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
		            	<li class="activitylist_paixu_left_biankuang lisettlementAdultPrice"><a onClick="sortby('settlementAdultPrice',this)">同行价格</a></li>
		            	<c:if test="${activityKind eq '2' or activityKind eq '10'}">
		            		<li class="activitylist_paixu_left_biankuang lisuggestAdultPrice"><a onClick="sortby('suggestAdultPrice',this)">直客价格</a></li>
		            	</c:if>
		            	<li class="activitylist_paixu_left_biankuang ligroupOpenDate"><a onClick="sortby('groupOpenDate',this)">出团日期</a></li>
		             </ul>
		      	</div>
		          <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
		          <div class="kong"></div>
		        </div>
		    </div>
		</c:if>
	
	
	<!-- 部门分区 -->
	<div class="supplierLine" <c:if test="${empty showAreaList || fn:length(showAreaList) == 1}">style="display: none;"</c:if>>
		<%--<a <c:if test="${empty departmentId}">class="select"</c:if> href="javascript:void(0)" onclick="getDepartment('');">全部产品</a>--%>
		<c:forEach var="department" items="${showAreaList}" varStatus="status">
			<c:choose>
				<c:when test="${(department.id == departmentId) || (empty departmentId && status.index == 0)}">
					<a class="select" href="javascript:void(0)" onclick="getDepartment('${department.id}');">${department.name}</a>
				</c:when>
				<c:otherwise>
					<a href="javascript:void(0)" onclick="getDepartment('${department.id}');">${department.name}</a>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</div>

	<c:if test="${fn:length(page.list) ne 0}">
	<table id="contentTable" class="table activitylist_bodyer_table" >
		<thead >
			<tr>
				<th width="4%">序号</th>
				<th class="table_borderLeftN" width="4%">全选<input name="allChk" type="checkbox" onclick="checkall(this)"/></th>
<%--			<th width="10%">产品编号</th>--%>
				<th width="14%">产品名称</th>
				<th width="8%">计调</th>
				<th width="8%">出发城市</th>
				<c:if test="${activityKind ne '10' }">
					<th width="6%">航空</th>
					<th width="8%">签证</th>
				</c:if>
				<th width="16%">最近出团日期</th>
				<th width="10%">成人同行价</th>
				
				<th width="4%">操作</th>
			</tr>
		</thead>
		<tbody>
		
			<c:forEach items="${page.list}" var="activity" varStatus="s">
			<c:set var="groupsize" value="${fn:length(activity.activityGroups)}"></c:set>
				<tr id="parent${s.count}">
					<td>${s.count}</td>
					<td class="table_borderLeftN"><input type="checkbox" name="activityId" value="${activity.id }" <c:if test="${fn:contains(activityIds,fn:trim(activity.id))}">checked="checked"</c:if> onclick="idcheckchg(this)"/><br/><br/></td>
<%--					<td>--%>
<%--						<c:choose> --%>
<%--						     <c:when test="${fn:length(activity.activitySerNum) > 20}"> --%>
<%--						     	<a style="text-decoration:none; color:inherit; cursor:default;" title="${activity.activitySerNum}"><c:out value="${fn:substring(activity.activitySerNum, 0, 20)}......" /></a> --%>
<%--						     </c:when> --%>
<%--						     <c:otherwise> --%>
<%--						      	<c:out value="${activity.activitySerNum}" /> --%>
<%--						     </c:otherwise>--%>
<%--						</c:choose>--%>
<%--					</td>--%>
					<td class="activity_name_td">
                        <a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${activity.id}')">${activity.acitivityName}</a>
                       <c:choose>
                            <c:when test="${not empty activity.activityAirTicket and activity.activityAirTicket.intermodalType == 1}">
                                <span class="lianyun_name">全国联运</span>
                            </c:when>
                            <c:when test="${not empty activity.activityAirTicket and activity.activityAirTicket.intermodalType == 2}">
                                <span class="lianyun_name">分区联运</span>
                            </c:when>
                        </c:choose>
                    </td>
					<td>${activity.createBy.name}</td>
					<td>
               			${activity.fromAreaName}
					</td>
					<c:if test="${activityKind ne '10' }">
						<td>
							<label class="qtip" title="${activity.trafficNameDesc}">
								<c:set var="fligthInfoStr" value=""></c:set>
								<c:forEach items="${activity.activityAirTicket.flightInfos }" var="fligthInfo">
									<c:set var="fligthInfoStr" value="${fligthInfoStr }${fn:replace(fligthInfo.airlines,'-1','-')},"></c:set>
								</c:forEach>
								${fligthInfoStr }
							</label>
						</td>
						<td>
							<c:if test="${!empty visaMap[activity.id]}">
								<c:forEach items="${visaMap[activity.id] }" var="visas">
									<c:forEach items="${visas }" var="map">
										<c:set var="countryId" value="${fn:split(map.key,'/')[0]}"></c:set>
										<c:forEach items="${map.value }" var="docInfo">
											<c:set var="visaMapIds" value="${visaMapIds },${docInfo.id }"></c:set>
										</c:forEach>
										<a href="javascript:void(0)" onClick="downloads('${visaMapIds}','${fns:getCountryName(countryId)}',null,true)">${fns:getCountryName(countryId)}</a>
										<c:set var="visaMapIds" value=""></c:set>
									</c:forEach>
								</c:forEach>
							</c:if>
						</td>
					</c:if>
					<td id="groupdate${activity.id }" align="center" class="">	
					<div id="truedate" <c:if test="${groupsize ne 0 }">style="display:block;"</c:if><c:if test="${groupsize == 0 }">style="display:none;"</c:if>>
						<span>
						<c:choose>
							<c:when test="${activity.groupOpenDate eq activity.groupCloseDate}">${activity.groupOpenDate}</c:when>
							<c:when test="${empty activity.groupCloseDate}">${activity.groupOpenDate}</c:when>
							<c:otherwise>${activity.groupOpenDate}至${activity.groupCloseDate}</c:otherwise>
						</c:choose>
						</span><br>
						<a id="close${s.count}" href="javascript:void(0)" class="team_a_click" onClick="expand('#child${s.count}',this,${activity.id });" onMouseenter="if($(this).html()=='全部团期'){$(this).html('展开全部团期')}" onMouseleave="if($(this).html()=='展开全部团期'){$(this).html('全部团期')}">全部团期</a>						
					</div>
					
					<div id="falsedate" <c:if test="${groupsize ne 0 }">style="display:none;"</c:if><c:if test="${groupsize == 0 }">style="display:block;"</c:if>>					
						日期待定
					</div>					                       
					</td>
					<td id="settleadultprice${activity.id }" class="tr"><c:if test="${activity.settlementAdultPrice==0}">价格待定</c:if><c:if test="${activity.settlementAdultPrice>0}">${fns:getCurrencyInfo(activity.currencyType,0,'mark')}<span class="tdred fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${activity.settlementAdultPrice}" /></span>起</c:if></td>
					<td class="p0">
                    	<dl class="handle">
                    		<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
                    		<dd class="">
               				<p>
               					<span></span>
								<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${activity.id}')">详情</a>
								<shiro:hasPermission name="${shiroType}Product:operation:edit"><br/><a href="javascript:void(0)" onClick="productModify(${activity.id})">修改</a></shiro:hasPermission>
								<a href="javascript:void(0)" onClick="downProduct(${activity.id})">下架</a>
								<shiro:hasPermission name="${shiroType}Product:operation:delete"><br/><a href="javascript:void(0)" onClick="return confirmxDel('要删除该产品吗？', ${activity.id})">删除</a></shiro:hasPermission>
							</p>
							</dd>
						</dl>
					</td>
										
				</tr>
				<tr id="child${s.count}" style="display:none" class="activity_team_top1">
					<c:if test="${activityKind eq '2'}">
						<td colspan="11" class="team_top" style="background-color:#d1e5f5;">
					</c:if>
					<c:if test="${activityKind ne '2'}">
						<td colspan="10" class="team_top" style="background-color:#d1e5f5;">
					</c:if>	
                     <form id="childform${s.count}">
						<table id="teamTable" class="table activitylist_bodyer_table table-mod2-group" style="margin:0 auto;">
							<c:set var="colspanNum" value="3"></c:set>
							<thead>
								<tr>
									<th rowspan="2" width="9%">团号</th>
									<th class="tc" rowspan="2" width="6%">出团日期</th>
							        <th class="tc" rowspan="2" width="6%">截团日期</th>
									<th rowspan="2" width="5%">签证国家</th>
									<th rowspan="2" width="7%" class="tc p0">资料截止日期</th>
									<c:if test="${activityKind eq '10' }">
										<th rowspan="2" width="4%">舱型</th>
										<c:set var="colspanNum" value="2"></c:set>
									</c:if>
									<c:set var="priceWidth" value="${12/colspanNum }"></c:set>
									<th colspan="${colspanNum }" class="t-th2" width="12%">同行价</th>
									<c:if test="${activityKind eq '2' or activityKind eq '10'}">
										<th colspan="${colspanNum }" class="t-th2" width="12%">直客价</th>
									</c:if>
									
									<!-- t1t2  列表中增加quauq策略和供应价  start  djw -->
									<c:if test="${flagList[s.index] == 1}">
									<c:if test="${activityKind eq '2'}">
									<th colspan="3" class="t-th2" width="266px">QUAUQ策略</th>
									</c:if>  
									
									<c:if test="${activityKind eq '2'}">
									<th colspan="3" class="t-th2" width="266px">供应价（含服务费）</th>
									</c:if>
									</c:if>
									<!-- t1t2  列表中增加quauq策略和供应价  end  djw -->
									
									<th class="tr" rowspan="2" width="5%">
										订金<c:if test="${activityKind eq '10' }">/人</c:if>
									</th>
									<th class="tr" rowspan="2" width="5%">
										单房差<c:if test="${activityKind eq '10' }">/人</c:if>
									</th>
									<th class="tr" rowspan="2" width="4%">
										预收<c:if test="${activityKind eq '10' }">/间</c:if>
									</th>
									<th class="tr" rowspan="2" width="4%">
										余位<c:if test="${activityKind eq '10' }">/间</c:if>
									</th>
									<th rowspan="2" style="display:none" class="soldPayPosition"  width="4%">切位</th>
									<th class="tc" rowspan="2" width="7%">操作</th>
								</tr>
								<tr>
									<c:choose>
										<c:when test="${activityKind eq '10' }">
											<th class="tr">1/2人</th>
											<th class="tr">3/4人</th>
											<th class="tr">1/2人</th>
											<th class="tr">3/4人</th>
										</c:when>
										<c:otherwise>
											<th class="tr" width="${priceWidth}%">成人</th>
											<th class="tr" width="${priceWidth}%">儿童</th>
											<th class="tr" width="${priceWidth}%">特殊人群</th>
											<c:if test="${activityKind eq '2'}">
												<th class="tr" width="${priceWidth}%">成人</th>
												<th class="tr" width="${priceWidth}%">儿童</th>
												<th class="tr" width="${priceWidth}%">特殊人群</th>
											</c:if>
											<!-- quauq策略    djw -->
											<c:if test="${flagList[s.index] == 1}">
											<c:if test="${activityKind eq '2'}">
												<th class="tc">成人</th>
												<th class="tc">儿童</th>
												<th class="tc">特殊人群</th>
											</c:if>
											
											<!-- 供应价     djw-->
											<c:if test="${activityKind eq '2'}">
												<th class="tc">成人</th>
												<th class="tc">儿童</th>
												<th class="tc">特殊人群</th>
											</c:if>
											</c:if>
										</c:otherwise>
									</c:choose>
								</tr>
							</thead><!-- </table><div class="table_activity_scroll">
						<table class="table activitylist_bodyer_table table-mod2-group"> -->
							<tbody>
							<c:forEach items="${activity.activityGroups}" var="group" varStatus="s2">												
								<tr id="childtr${s.count}${s2.count}">
									<input type="hidden" class="srcActivityId" value="${group.srcActivityId}"/>
									<td width="9%"><span id="groupId${group.id}">${group.groupCode}</span>
										<%--<c:choose> 
										     <c:when test="${fn:length(group.groupCode) > 10}"> 
										    	 <span title="${group.groupCode}"><c:out value="${fn:substring(group.groupCode, 0, 10)}......" /></span> 
										     </c:when> 
										     <c:otherwise> 
										      	<c:out value="${group.groupCode}" /> 
										     </c:otherwise>
										</c:choose>
										--%>
										<!-- 不打开注释无法  修改 
										<input style="display:none;" disabled="disabled" type="text" id="groupCode${s.count}${s2.count}" name="groupCode" value="${group.groupCode}" onafterpaste="this.value=this.value.replace(/[^\u4e00-\u9fa5\w\(\)\（\）\-]/g,'')" onKeyUp="this.value=this.value.replace(/[^\u4e00-\u9fa5\w\(\)\（\）\-]/g,'')"/>
										-->	
										<!--  c460 解决bug 13137  -->
										<input type="hidden" name="groupCode" class="srcActivityId" value="${group.groupCode}"/>								
									</td>
									<td width="6%" class="tc">
										<span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/></span>
										<input type="text" id="groupid${s.count}${s2.count}" name="groupid" value="${group.id}" style="display:none;" disabled="disabled"/>
										<input style="display:none;" disabled="disabled" type="text" id="groupOpenDate${s.count}${s2.count}" name="groupOpenDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/>" onClick="WdatePicker()" class="inputTxt"/>
									</td>
									<input type="hidden" class="srcActivityId" value="${group.srcActivityId}"/>
									<td width="6%" class="tc">
										<span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupCloseDate }"/></span>
										<input style="display:none;" disabled="disabled" type="text" id="groupCloseDate${s.count}${s2.count}" name="groupCloseDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupCloseDate}" />" onClick="WdatePicker({maxDate:takeOrderOpenDate('groupCloseDate${s.count}${s2.count}')})"  class="inputTxt"/>
									</td>
									<td width="5%">
										<span>${group.visaCountry }</span>
										<input style="display:none;" disabled="disabled" type="text" id="visaCountry${s.count}${s2.count}" name="visaCountry" value="${group.visaCountry}" />
									</td>
									<td width="7%" class="tc">
										<span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate }"/></span>
										<input style="display:none;" disabled="disabled" type="text" id="visaDate${s.count}${s2.count}" name="visaDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate }"/>" onClick="WdatePicker({maxDate:takeModVisaDate('visaDate${s.count}${s2.count}')})"/>
									</td>	
									<c:if test="${activityKind eq '10' }">
										<td width="4%">
											<span>${fns:getDictLabel(group.spaceType, "cruise_type", "-") }</span>
										</td>
									</c:if>	
									<!-- 同行价 -->							
									<td width="${priceWidth}%" class="tr tdCurrency" <c:if test="${not empty activity.activityAirTicket }">title="包含${activity.activityAirTicket.settlementAdultPrice }机票价"</c:if> >
										<c:if test="${group.settlementAdultPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,0,'mark')}</span><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementAdultPrice }" /></c:if></span>
										<input style="display:none;" disabled="disabled" type="text" id="settlementAdultPrice${s.count}${s2.count}" name="settlementAdultPrice" value="<c:if test="${group.settlementAdultPrice eq 0}"></c:if><c:if test="${group.settlementAdultPrice ne 0}">${group.settlementAdultPrice}</c:if>" maxlength="9" onkeyup="validNum(this,'${group.id}','${activity.id}')" onafterpaste="validNum(this,'${group.id}','${activity.id}')"  onblur="validNum(this,'${group.id}','${activity.id}')"/>
<!-- 										onkeyup="validatorFloat(this)" onafterpaste="validatorFloat(this)" -->
									</td>
									<td width="${priceWidth}%" class="tr tdCurrency" <c:if test="${not empty activity.activityAirTicket }">title="包含${activity.activityAirTicket.settlementcChildPrice }机票价"</c:if> >
										<c:if test="${group.settlementcChildPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,1,'mark')}</span><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementcChildPrice }" /></c:if></span>
										<input style="display:none;" disabled="disabled"  type="text" id="settlementcChildPrice${s.count}${s2.count}" name="settlementcChildPrice" value="<c:if test="${group.settlementcChildPrice eq 0}"></c:if><c:if test="${group.settlementcChildPrice ne 0}">${group.settlementcChildPrice}</c:if>" maxlength="9"  onkeyup="validNum(this,'${group.id}','${activity.id}')" onafterpaste="validNum(this,'${group.id}','${activity.id}')"  onblur="validNum(this,'${group.id}','${activity.id}')"/>
									</td>
									<c:if test="${activityKind ne '10' }">
										<td width="${priceWidth}%" class="tr tdCurrency" <c:if test="${not empty activity.activityAirTicket }">title="包含${activity.activityAirTicket.settlementSpecialPrice }机票价"</c:if> >
											<c:if test="${group.settlementSpecialPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,2,'mark')}</span><span class="tdred" title="${activity.specialRemark }"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementSpecialPrice }" /></c:if></span>
											<input style="display:none;" disabled="disabled" type="text" id="settlementSpecialPrice${s.count}${s2.count}" name="settlementSpecialPrice" value="<c:if test="${group.settlementSpecialPrice eq 0}"></c:if><c:if test="${group.settlementSpecialPrice ne 0}">${group.settlementSpecialPrice}</c:if>" maxlength="9"  onkeyup="validNum(this,'${group.id}','${activity.id}')" onafterpaste="validNum(this,'${group.id}','${activity.id}')"  onblur="validNum(this,'${group.id}','${activity.id}')"/>
										</td>
									</c:if>
									<!-- 直客价 -->
									<c:if test="${activityKind eq '2' or activityKind eq '10'}">
										<td width="${priceWidth}%" class="tr tdCurrency">
											<c:if test="${group.suggestAdultPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,3,'mark')}</span><span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestAdultPrice }" /></c:if></span>
											<input style="display:none;" disabled="disabled" type="text" id="suggestAdultPrice${s.count}${s2.count}" name="suggestAdultPrice" value="<c:if test="${group.suggestAdultPrice eq 0}"></c:if><c:if test="${group.suggestAdultPrice ne 0}">${group.suggestAdultPrice}</c:if>" maxlength="9"  />
										</td>
										<td width="${priceWidth}%" class="tr tdCurrency">
											<c:if test="${group.suggestChildPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,4,'mark')}</span><span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestChildPrice }" /></c:if></span>
											<input style="display:none;" disabled="disabled" type="text" id="suggestChildPrice${s.count}${s2.count}" name="suggestChildPrice" value="<c:if test="${group.suggestChildPrice eq 0}"></c:if><c:if test="${group.suggestChildPrice ne 0}">${group.suggestChildPrice}</c:if>" maxlength="9"  />
										</td>
										<c:if test="${activityKind ne '10' }">
											<td width="4%" class="tr tdCurrency">
												<c:if test="${group.suggestSpecialPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,5,'mark')}</span><span class="tdblue" title="${activity.specialRemark }"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestSpecialPrice }" /></c:if></span>
												<input style="display:none;" disabled="disabled" type="text" id="suggestSpecialPrice${s.count}${s2.count}" name="suggestSpecialPrice" value="<c:if test="${group.suggestSpecialPrice eq 0}"></c:if><c:if test="${group.suggestSpecialPrice ne 0}">${group.suggestSpecialPrice}</c:if>" maxlength="9"  />
											</td>
										</c:if>
										<!-- quauq价开始 -->
										<c:if test="${flagList[s.index] == 1}">
										<!-- quauq -->
										<c:if test="${activityKind eq '2' and activity.activityStatus == 2}">
										<!-- 成人价策略 -->
										<td width="4.0%" class="tr tdCurrency">
											<c:choose>
												<c:when test="${group.pricingStrategy.adultPricingStrategy ne null and group.pricingStrategy.adultPricingStrategy ne '' }">${group.pricingStrategy.adultPricingStrategy }</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
											
										</td>
										<!-- 儿童价策略 -->
										<td width="4.0%" class="tr tdCurrency">
											<c:choose>
												<c:when test="${group.pricingStrategy.childrenPricingStrategy ne null and group.pricingStrategy.childrenPricingStrategy ne '' }">${group.pricingStrategy.childrenPricingStrategy }</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
											
										</td>
										<!-- 特殊人群价策略 -->
										<td width="4%" class="tr tdCurrency">
											<c:choose>
												<c:when test="${group.pricingStrategy.specialPricingStrategy ne null and group.pricingStrategy.specialPricingStrategy ne '' }">${group.pricingStrategy.specialPricingStrategy }</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
											
										</td> 
										<%-- <!-- 成人价 -->
										<td width="4.0%" class="tr tdCurrency"  style="display:none;" >
											<c:if test="${group.quauqAdultPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,0,'mark')}</span><span class="tdblue"><fmt:formatNumber value="${group.quauqAdultPrice}" pattern="#,##0.00" /></c:if></span>
											<input style="display:none;" disabled="disabled" type="text" readonly="readonly"
												 id="quauqAdultPrice${s.count}${s2.count}" name="quauqAdultPrice" value="${group.quauqAdultPrice}"
												maxlength="14" onkeyup="validNum(this,'${group.id}','${activity.id}')" onafterpaste="validNum(this,'${group.id}','${activity.id}')"  onblur="isMoney(this)"/> 
										</td>
										<!-- 儿童价 -->
										<td width="4.0%" class="tr tdCurrency" style="display:none;" >
											<c:if test="${group.quauqChildPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,1,'mark')}</span><span class="tdblue"><fmt:formatNumber value="${group.quauqChildPrice}" pattern="#,##0.00" /></c:if></span>
											<input style="display:none;" disabled="disabled" type="text" readonly="readonly"
												id="quauqChildPrice${s.count}${s2.count}" name="quauqChildPrice"  value="${group.quauqChildPrice}" 
												maxlength="14" onkeyup="validNum(this,'${group.id}','${activity.id}')" onafterpaste="validNum(this,'${group.id}','${activity.id}')"  onblur="isMoney(this)"/></input>
										</td>
										<!-- 特殊人群价 -->
										<td width="4%" class="tr tdCurrency" style="display:none;" >
											<c:if test="${group.quauqSpecialPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,2,'mark')}</span><span class="tdblue" title="特殊人群备注"><fmt:formatNumber value="${group.quauqSpecialPrice}" pattern="#,##0.00" /></c:if></span>
											<input style="display:none;" disabled="disabled" type="text" readonly="readonly"
												id="quauqSpecialPrice${s.count}${s2.count}" name="quauqSpecialPrice"
												value="<c:if test="${group.quauqSpecialPrice eq 0}"></c:if><c:if test="${group.quauqSpecialPrice ne 0}">${group.quauqSpecialPrice}</c:if>"
												maxlength="14" onkeyup="validNum(this,'${group.id}','${activity.id}')" onafterpaste="validNum(this,'${group.id}','${activity.id}')"  onblur="isMoney(this)"/>
										</td>  --%>
										
										</c:if>
										<!-- quauq -->
										
										<!-- 供应价价开始 -->
										<c:if test="${activityKind eq '2' and activity.activityStatus eq '2'}">
										<!-- 成人价 -->
										<td width="4.0%" class="tr tdCurrency">
											<c:if test="${group.quauqAdultPrice ne 0}">
												<c:if test="${group.quauqAdultPrice ne '' and group.quauqAdultPrice ne null }">
													<span class="rm">${fns:getCurrencyInfo(group.currencyType,0,'mark')}</span>
													<span class="tdblue">
													<fmt:formatNumber value="${group.adultRetailPrice }" pattern="#,##0.00" />
												</c:if>
												<c:if test="${group.quauqAdultPrice == '' or group.quauqAdultPrice == null }">-</c:if>
												</span>
											</c:if>
											
											<input style="display:none;" disabled="disabled" type="text" readonly="readonly"
												 id="supplyAdultPrice${s.count}${s2.count}" name="supplyAdultPrice"
												 value=<c:choose><c:when test="${group.quauqAdultPrice == '' or group.quauqAdultPrice == null}">
														 	""
														 </c:when> 
														 <c:otherwise>
														 	<fmt:formatNumber value="${group.adultRetailPrice }" pattern="##0.00" />
														 	
														 </c:otherwise> 
														 </c:choose>
												maxlength="14" onkeyup="validNum(this)" onafterpaste="validNum(this)"  onblur="isMoney(this)"/>
										</td>
										<!-- 儿童价 -->
										<td width="4.0%" class="tr tdCurrency">
											<c:if test="${group.quauqChildPrice ne 0}">
												<c:if test="${group.quauqChildPrice ne '' and group.quauqChildPrice ne null  }">
													<span class="rm">${fns:getCurrencyInfo(group.currencyType,1,'mark')}</span>
													<span class="tdblue">
													<fmt:formatNumber value="${group.childRetailPrice }" pattern="#,##0.00" />
												</c:if>
												<c:if test="${group.quauqChildPrice == '' or group.quauqChildPrice == null }">-</c:if>
											</span>
											</c:if>
											<input style="display:none;" disabled="disabled" type="text"  readonly="readonly"
												id="supplyChildPrice${s.count}${s2.count}" name="supplyChildPrice"  
												value=<c:choose><c:when test="${group.quauqChildPrice == '' or group.quauqChildPrice == null}">
														 	""
														 </c:when> 
														 <c:otherwise>
														 	<fmt:formatNumber value="${group.childRetailPrice }" pattern="##0.00" />
														 </c:otherwise> 
														 </c:choose>
												maxlength="14" onkeyup="validNum(this)" onafterpaste="validNum(this)"  onblur="isMoney(this)"/></input>
										</td>
										<!-- 特殊人群价 -->
										<td width="4%" class="tr tdCurrency">
											<c:if test="${group.quauqSpecialPrice ne 0}">
											<c:if test="${group.quauqSpecialPrice ne '' and group.quauqSpecialPrice ne null }">
												<span class="rm">${fns:getCurrencyInfo(group.currencyType,2,'mark')}</span>
												<span class="tdblue" title="特殊人群备注"><fmt:formatNumber value="${group.specialRetailPrice }" pattern="#,##0.00" />
											</c:if>
											<c:if test="${group.quauqSpecialPrice == '' or group.quauqSpecialPrice == null }">-</c:if>
											</span>
											</c:if>
											<input style="display:none;" disabled="disabled" type="text" readonly="readonly"
												id="supplySpecialPrice${s.count}${s2.count}" name="supplySpecialPrice"
												value=<c:choose><c:when test="${group.quauqSpecialPrice == '' or group.quauqSpecialPrice == null}">
														 	""
														 </c:when> 
														 <c:otherwise>
														 	<fmt:formatNumber value="${group.specialRetailPrice }" pattern="##0.00" />
														 </c:otherwise> 
														 </c:choose>
												maxlength="14" onkeyup="validNum(this)" onafterpaste="validNum(this)"  onblur="isMoney(this)"/>
										</td> 
										</c:if>
										</c:if>
										<!-- 供应价结束 -->
										<input type="hidden" name="fromAreaNum" value="${activity.fromArea }"></input>											
										<input type="hidden" name="targetAreaIds" value="${activity.targetAreaIds }"></input>											
										<input type="hidden" name="travelTypeId" value="${activity.travelTypeId }"></input>											
										<input type="hidden" name="activityTypeId" value="${activity.activityTypeId }"></input>											
										<input type="hidden" name="activityLevelId" value="${activity.activityLevelId }"></input>
			
										<c:if test="${activityKind ne '10' }">
											<td width="5%" class="tr tdCurrency">
												<c:if test="${group.payDeposit ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,6,'mark')}</span><span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.payDeposit }" /></c:if></span>
												<input style="display:none;" disabled="disabled" type="text" id="payDeposit${s.count}${s2.count}" name="payDeposit" value="<c:if test="${group.payDeposit eq 0}"></c:if><c:if test="${group.payDeposit ne 0}">${group.payDeposit}</c:if>" onafterpaste="this.value.replace(/\D/g,'')" maxlength="9" onKeyUp="this.value.replace(/\D/g,'')"/>
											</td>
											<td width="5%" class="tr tdCurrency">
												<c:if test="${group.singleDiff ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,7,'mark')}</span><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.singleDiff }" /></c:if></span>
												<input style="display:none;" disabled="disabled" type="text" id="singleDiff${s.count}${s2.count}" name="singleDiff" value="<c:if test="${group.singleDiff eq 0}"></c:if><c:if test="${group.singleDiff ne 0}">${group.singleDiff}</c:if>" onafterpaste="this.value.replace(/\D/g,'')" maxlength="9" onKeyUp="this.value.replace(/\D/g,'')"/>
											</td>
										</c:if>
										<c:if test="${activityKind eq '10' }">
											<td width="5%" class="tr tdCurrency">
												<c:if test="${group.payDeposit ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,4,'mark')}</span><span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.payDeposit }" /></c:if></span>
												<input style="display:none;" disabled="disabled" type="text" id="payDeposit${s.count}${s2.count}" name="payDeposit" value="<c:if test="${group.payDeposit eq 0}"></c:if><c:if test="${group.payDeposit ne 0}">${group.payDeposit}</c:if>" onafterpaste="this.value.replace(/\D/g,'')" maxlength="9" onKeyUp="this.value.replace(/\D/g,'')"/>
											</td>
											<td width="5%" class="tr tdCurrency">
												<c:if test="${group.singleDiff ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,5,'mark')}</span><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.singleDiff }" /></c:if></span>
												<input style="display:none;" disabled="disabled" type="text" id="singleDiff${s.count}${s2.count}" name="singleDiff" value="<c:if test="${group.singleDiff eq 0}"></c:if><c:if test="${group.singleDiff ne 0}">${group.singleDiff}</c:if>" onafterpaste="this.value.replace(/\D/g,'')" maxlength="9" onKeyUp="this.value.replace(/\D/g,'')"/>
											</td>
										</c:if>
									</c:if>
									<c:if test="${activityKind ne '2' and activityKind ne '10'}">
										<td width="5%" class="tr tdCurrency">
											<c:if test="${group.payDeposit ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,3,'mark')}</span><span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.payDeposit }" /></c:if></span>
											<input style="display:none;" disabled="disabled" type="text" id="payDeposit${s.count}${s2.count}" name="payDeposit" value="<c:if test="${group.payDeposit eq 0}"></c:if><c:if test="${group.payDeposit ne 0}">${group.payDeposit}</c:if>" maxlength="9" />
										</td>
										<td width="5%" class="tr tdCurrency">
											<c:if test="${group.singleDiff ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,4,'mark')}</span><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.singleDiff }" /></c:if></span>
											<input style="display:none;" disabled="disabled" type="text" id="singleDiff${s.count}${s2.count}" name="singleDiff" value="<c:if test="${group.singleDiff eq 0}"></c:if><c:if test="${group.singleDiff ne 0}">${group.singleDiff}</c:if>" maxlength="9" />
										</td>
									</c:if>
									<td width="4%" class="tr">
										<span>${group.planPosition }</span>
										<input style="display:none;" disabled="disabled" type="text" id="planPosition${s.count}${s2.count}" name="planPosition" value="${group.planPosition}"  onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="3" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
									</td>
									<td width="4%" class="tr">
										<span class="tdred">${group.freePosition }</span>
										<input style="display:none;" disabled="disabled" type="text" id="freePosition${s.count}${s2.count}" name="freePosition" value="${group.freePosition}" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="3" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
									</td>
									<td width="4%" style="display:none;" class="soldPayPosition${group.id}">
										<span style="color:#eb0205" >0</span>
									</td>
									<td width="7%" class="tnwrap tc">
										<shiro:hasPermission name="${shiroType}Product:operation:groupEdit">
											<a href="javascript:void(0)"  id="modbtn${s.count}${s2.count}" 
											onClick="modgroup('#groupid${s.count}${s2.count}','#savebtn${s.count}${s2.count}','#delbtn${s.count}${s2.count}','#cancelbtn${s.count}${s2.count}',this)">修改</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="${shiroType}Product:operation:groupEdit">
											<a href="javascript:void(0)"  id="savebtn${s.count}${s2.count}" style="display:none;" 
											onClick="savegroup(${activity.id},'#modbtn${s.count}${s2.count}','#delbtn${s.count}${s2.count}','#cancelbtn${s.count}${s2.count}',this,'#childform${s.count}','#childtr${s.count}${s2.count}','${ctx}')">确认</a>
										</shiro:hasPermission>										
										<shiro:hasPermission name="${shiroType}Product:operation:groupDelete">
											<a href="javascript:void(0)" id="delbtn${s.count}${s2.count}" onClick="return confirmxCopy('要删除该产品的此团期吗？',${group.id},${activity.id },this,'child${s.count}')" ><span>删除</span></a>
										</shiro:hasPermission>
<%--										<shiro:hasPermission name="group:manager:exportExcel"><br/><a href="javascript:void(0)" id="exportExcelbtn${s.count}${s2.count}" onClick="exportExcel('${group.id}', 'customer', '${group.groupCode}')" ><span>导出游客信息</span></a></shiro:hasPermission>--%>
										<shiro:hasPermission name="${shiroType}Product:operation:groupEdit">
											<a style="color:#ec0203;display:none;" id="cancelbtn${s.count}${s2.count}" href="javascript:void(0)" 
											onClick="cancelgroup('#modbtn${s.count}${s2.count}','#savebtn${s.count}${s2.count}','#delbtn${s.count}${s2.count}',this)">取消</a>
										</shiro:hasPermission>
									</td>
																	
								</tr>										
							</c:forEach>
							</tbody>
						</table></div>
						</form>
				</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
		</div>
		<div class="page">
			<div class="pagination">
				<dl>
					<dt><input name="allChk" type="checkbox" onclick="checkall(this)"/>全选</dt>
					<dd>
						<shiro:hasPermission name="${shiroType}Product:operation:edit">	
							<a onClick="confirmBatchIsNull('需要将选择的产品下架吗','off')">批量下架</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${shiroType}Product:operation:delete">	
							<a onClick="confirmBatchIsNull('删除所有选择的团期吗','del')">批量删除</a>
						</shiro:hasPermission>
					</dd>
				</dl>
			<div class="endPage">${page}</div>
			<div style="clear:both;"></div>
			</div>	
		</div>
		</c:if>
		<c:if test="${fn:length(page.list) eq 0}">
			<table id="contentTable" class="table activitylist_bodyer_table" >
				<thead>
					<tr>
						<th width="4%">序号</th>
						<th class="table_borderLeftN" width="4%">全选<input name="allChk" type="checkbox" onclick="checkall(this)"/></th>
		<%--			<th width="10%">产品编号</th>--%>
						<th width="14%">产品名称</th>
						<th width="8%">计调</th>
						<th width="8%">出发城市</th>
						<c:if test="${activityKind ne '10' }">
							<th width="6%">航空</th>
						</c:if>
						<th width="8%">签证</th>
						<th width="16%">最近出团日期</th>
						<th width="10%">成人同行价</th>
						
						<th width="4%">操作</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<c:if test="${activityKind eq '2' or activityKind eq '10'}">
							<td colspan="11" style="text-align: center;">经搜索暂无数据，请尝试改变搜索条件再次搜索</td>
						</c:if>
						<c:if test="${activityKind ne '2' and activityKind ne '10'}">
							<td colspan="10" style="text-align: center;">经搜索暂无数据，请尝试改变搜索条件再次搜索</td>
						</c:if>						
					</tr>
				</tbody>
			</table>
		</c:if>
	<form id="exportForm" action="${ctx}/activity/manager/exportExcel" method="post">
		<input type="hidden" id="groupId" name="groupId">
		<input type="hidden" id="groupCode" name="groupCode">
	</form>   
	</body>
</html>