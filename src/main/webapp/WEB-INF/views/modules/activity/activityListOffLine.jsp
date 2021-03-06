<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>已下架产品</title>
	<meta name="decorator" content="wholesaler"/>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/activity/dynamic.group.validator.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/jquery.number.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>
   <style type="text/css">
   </style>
	<script type="text/javascript">
		var activityIds = "";
		var isOnlinePage = "";
		
	    //   对应需求  c460
		var groupCodeRuleDT = '${groupCodeRuleDT}';
		
		
		$(function() {
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
					//$(this).text('收起筛选');
					$(this).addClass('zksx-on');
				}else
				{
					$('.ydxbd').hide();
					//$(this).text('展开筛选');
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
			
			
			$("#settlementAdultPriceStart").keyup(function(){
		        $(this).val($(this).val().replace(/\D|^0/g,''));  
		    }).bind("paste",function(){ 
		        $(this).val($(this).val().replace(/\D|^0/g,''));  
		    }).css("ime-mode", "disabled");
			
			$("#settlementAdultPriceEnd").keyup(function(){
		        $(this).val($(this).val().replace(/\D|^0/g,''));  
		    }).bind("paste",function(){ 
		        $(this).val($(this).val().replace(/\D|^0/g,''));  
		    }).css("ime-mode", "disabled");
			
			$("#activityDuration").keyup(function(){
		        $(this).val($(this).val().replace(/\D|^0/g,''));  
		    }).bind("paste",function(){ 
		        $(this).val($(this).val().replace(/\D|^0/g,''));  
		    }).css("ime-mode", "disabled");
			
			$("#activityDuration").keyup(function(){
		        $(this).val($(this).val().replace(/\D|^0/g,''));  
		    }).bind("paste",function(){ 
		        $(this).val($(this).val().replace(/\D|^0/g,''));  
		    }).css("ime-mode", "disabled");
		});
		function expand(child,obj,srcActivityId){
            if($(child).css("display")=="none"){
                
                
                if("${userType}"=="3") {
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
                        	if(msg.length>0){
                                $(".soldPayPosition").show();
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
                }
            }else{
                    $(child).hide();
                    $(obj).parents("td").attr("class","");
                    $(obj).html("展开全部团期");
            }
        }
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/activity/manager/list/${travelActivity.activityStatus }/${activityKind}?newflag=newflag");
			$("#searchForm").submit();
	    }
	    //是否选中产品
		function confirmBatchIsNull(mess,sta) {
			if(activityIds != ""){
				if(sta=='off'){
					confirmBatchOff(mess);
				}else if(sta=='del'){
					confirmBatchDel(mess);
				}else{
					confirmBatchRelease(mess);
				}
			}else{
				$.jBox.error('未选择产品','系统提示');
			}
		}
	    // 团期删除确认对话框
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
									alert("删除失败,请联系管理员");
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
				$("#searchForm").attr("action","${ctx}/activity/manager/batchoff/"+activityIds+"/{activityKind}");
				$("#searchForm").submit();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
			return false;
		}
		// 批量上架确认对话框
		function confirmBatchRelease(mess){
			top.$.jBox.confirm(mess,'系统提示',function(v){
			if(v=='ok'){
				loading('正在提交，请稍等...');
				$("#searchForm").attr("action","${ctx}/activity/manager/batchrelease/"+activityIds+"/${activityKind}");
				$("#searchForm").submit();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
			return false;
		}
		//产品删除
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
		//单个产品上架
		function releaseProduct(mess,proId){
			top.$.jBox.confirm(mess,'系统提示',function(v){
				if(v=='ok'){
					loading('正在提交，请稍等...');
					$("#searchForm").attr("action","${ctx}/activity/manager/release/"+proId + "/${activityKind}");
					$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
				return false;
		}
	    
	    function modgroup(groupid,savebtn,delbtn,cancelbtn,obj){
	        //要修改的元素的团号span
	    	var span4Modified=$("#groupId"+$(groupid).val());
	    	//要修改的元素的input文本框
	    	var text44Modified=$("#"+$(groupid).val());
	    	//批发商的uuid是否为优加
	    	var uuidTemp='${uuid4ManualModifyGroupcode}';
	    	if(uuidTemp=='7a81c5d777a811e5bc1e000c29cf2586' || uuidTemp=='5c05dfc65cd24c239cd1528e03965021'|| groupCodeRuleDT==0){ //为优加、起航假期 则可手输    对应需求 c460 修改
	    	   //隐藏团号span,显示团号文本框
	    	   span4Modified.hide();
	    	   text44Modified.show();
	    	}     	
	    	$(obj).parent().parent().find("span").hide();
	    	$(obj).parent().parent().find("span").eq(0).show();
	    	$(obj).parent().parent().find("span[class='rm']").show();
	    	$(obj).parent().parent().find("input[type='text']").css("display","inline-block");
	    	//*0258需求,发票税:针对懿洋假期-tgy-s,显示文本框后的%号(如果table结构发生变化,则也需要相应改变),单团,*//
	    	$(obj).parent().prev().prev().find("input").next().css("display","inline-block");
	    	//*258需求,发票税:针对懿洋假期-tgy-e*//
	    	$(obj).parent().parent().find("input[type='text']").attr("disabled",false);
	    	$(groupid).hide();
	    	$(groupid).attr("disabled",false);
	    	$(savebtn).show();
	    	$(delbtn).hide();
	    	$(cancelbtn).show();
	    	$(obj).hide();
	    	 if(uuidTemp=='7a81c5d777a811e5bc1e000c29cf2586' || uuidTemp=='5c05dfc65cd24c239cd1528e03965021' || groupCodeRuleDT==0){//为优加/起航假期 则可手输   对应需求 c460 修改
		    //再次隐藏团号span
		    span4Modified.hide();
		    }
		    if(uuidTemp!='7a81c5d777a811e5bc1e000c29cf2586' && uuidTemp!='5c05dfc65cd24c239cd1528e03965021' && groupCodeRuleDT==1){ //不为优加/起航假期,需要隐藏团号文本框   c460 修改
		       text44Modified.hide();
		    }
	    }
		function cancelgroup(modbtn,savebtn,delbtn,obj){
			$(modbtn).show();
	    	$(savebtn).hide();
	    	$(delbtn).show();
	    	$(obj).hide();
			$(obj).parent().parent().find("span").show();
	    	$(obj).parent().parent().find("input[type='text']").css("display","none");
	    	//*0258需求,发票税:针对懿洋假期-tgy-s,隐藏文本框后的%号(如果table结构发生变化,则也需要相应改变),单团,*//
	    	$(obj).parent().prev().prev().find("input").next().css("display","none");
	    	//TODO:修改了文本框的值,然后点击取消了,下一次文本框带出上次修改的值.当前这种逻辑处理,个人觉得有问题,暂且未处理-tgy
	    	//*258需求,发票税:针对懿洋假期-tgy-e*//
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
	    	
	    function chgDuration(type){
	    	var duration = $("#activityDuration").val();
    		if(type=="-"){
    			if(duration==1 || duration == "")
    				$("#activityDuration").val(1);
    			else
    				$("#activityDuration").val(parseInt(duration)-1);
    		}
    		if(type=="+"){
    			if(duration == "")
    				$("#activityDuration").val(1);
    			else{
    				$("#activityDuration").val(parseInt(duration)+1);
    			}
    		}
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
					sortBy = $.trim(sortBy.replace(/ASC/gi,""))+" DESC";
				}else{
					sortBy = $.trim(sortBy.replace(/DESC/gi,""))+" ASC";
				}
			}else{
				sortBy = sortBy+" DESC";
			}
			$("#orderBy").val(sortBy);
			$("#searchForm").submit();
		}
		
		function onLineProduct(activityId){
	        $.jBox.confirm("确定要上架该产品吗？", "提示", function(v, h, f){
	            if (v == 'ok') {
	            	   $("#searchForm").attr("action","${ctx}/activity/manager/batchrelease/"+activityId + "/${activityKind}");
	            	   $("#searchForm").submit();
	                    }else if (v == 'cancel'){
	                        
	                    }
	                });
	    }
		//导出团期中关于游客信息
		function exportExcel(groupId, status, obj) {
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
		/**
		  * 团号超过50字提醒
		  * @param {} obj
		  */
		 var flag=10;
		 function validateLong(obj)
		 {
		 	replaceStr(obj);
		 	if($(obj).val().length<=49){
		 		flag = 10;
		 	}
		 	if($(obj).val().length>=50)
		 		{ 
		 			if($(obj).val().length==50 && flag==10){
		 				//$.jBox.tip("团号只能输入50个字符","true");
		 				flag++;
		 			}else{
		 				$.jBox.tip("团号超过50个字符，请修改","error");  
		 			}
		 			return false;  
		 		}
		 }
	</script>
</head>
<body>
    <page:applyDecorator name="activity_op_head" >
        <page:param name="current">offline</page:param>
    </page:applyDecorator>
    
    <!-- 签证公告展示 -->
	<%@ include file="/WEB-INF/views/modules/visa/visaPublicBulletinMarquee.jsp"%>
    <div class="activitylist_bodyer_right_team_co_bgcolor">
	<form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/activity/manager/list/${travelActivity.activityStatus }/${activityKind}?newflag=newflag" method="post" >
		<input id="activityStatus" type="hidden" name="activityStatus" value="${travelActivity.activityStatus }"/>
		<input id="activityIds" type="hidden" name="activityIds" value="${activityIds }"/>
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<input id="departmentId" name="departmentId" type="hidden" value="${departmentId }" />
		
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr ">
				<input class="txtPro inputTxt searchInput"
				   		id="wholeSalerKey" name="wholeSalerKey" value="${travelActivity.acitivityName }" placeholder="输入产品名称、团号，支持模糊匹配"/>
         	</div>
			<a class="zksx">筛选</a>
	        <div class="form_submit">
				<input class="btn btn-primary ydbz_x" type="submit" value="搜索"/>
				<input type="button" value="清空所有条件" onclick="resetSearchParams()" class="btn ydbz_x" />
			</div>
			<shiro:hasPermission name="${shiroType}Product:operation:form">
				<p class="main-right-topbutt"><a class="primary" onclick="javascript:window.location.href='${ctx}/activity/manager/form?kind=${activityKind}'">发布新产品</a></p>
			</shiro:hasPermission>

			<div class="ydxbd" >
				<span></span>
			
			<div class="activitylist_bodyer_right_team_co1">
	            <div class="activitylist_team_co3_text">销售：</div>
	            <input type="text" id="estimatePriceRecordUserName" name="estimatePriceRecordUserName" class="inputTxt"  value="${travelActivity.estimatePriceRecord.userName }">
	        </div>
			<div class="activitylist_bodyer_right_team_co1">
			    <div class="activitylist_team_co3_text">出发地：</div>
				<div class="selectStyle">
					<form:select id="fromArea" path="fromArea" itemValue="key" itemLabel="value" >
						<form:option value="" >不限</form:option>
						<form:options items="${fromAreas}"/>
					</form:select>
				</div>
            </div>
			<div class="activitylist_bodyer_right_team_co1">  
                 <div class="activitylist_team_co3_text">目的地：</div>
                 <tags:treeselect id="targetArea" name="targetAreaIdList" value="${targetAreaIds}" labelName="targetAreaNameList"  labelValue="${targetAreaNames}"
                     title="区域" url="/activity/manager/filterTreeData1?kind=${activityKind}" checked="true"/>
         	</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">出团日期：</label><input id="groupOpenDate" class="inputTxt dateinput" name="groupOpenDate" value='<fmt:formatDate value="${travelActivity.groupOpenDate }" pattern="yyyy-MM-dd"/>' onFocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" readonly/>
				<span style="font-size:12px; font-family:'宋体';"> 至</span>  
				<input id="groupCloseDate" class="inputTxt dateinput" name="groupCloseDate" value='<fmt:formatDate value="${travelActivity.groupCloseDate }" pattern="yyyy-MM-dd"/>' onClick="WdatePicker()" readonly/>
        	</div>
         	<c:if test="${activityKind ne 10 }">
			<div class="activitylist_bodyer_right_team_co1">
	            <div class="activitylist_team_co3_text">计调：</div>
	            <input type="text" id="createName" name="createName" class="inputTxt"  value="${travelActivity.createBy.name }">
	        </div>
			<div class="activitylist_bodyer_right_team_co1">
	            <div class="activitylist_team_co3_text">领队：</div>
	            <input type="text" id="groupLead" name="groupLead" class="inputTxt"  value="${travelActivity.groupLead }">
	        </div>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">航空公司：</div>
				<div class="selectStyle">
				<form:select id="trafficName" path="activityAirTicket.airlines" >      
	                <form:option value="" >不限</form:option>
	                <form:options items="${airlines}" itemValue="airlineCode" itemLabel="airlineName"/>
            	</form:select>
				</div>
            </div>
            </c:if>
            <c:if test="${activityKind eq 10 }">
            	<div class="activitylist_bodyer_right_team_co1">
			    <div class="activitylist_team_co3_text">返回城市：</div>
					<div class="selectStyle">
						<form:select path="backArea" itemValue="key" itemLabel="value" id="backArea" >
							<form:option value="" >不限</form:option>
							<form:options items="${fromAreas}"/>
						</form:select>
					</div>
              </div>
			  <div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">产品系列：</div>
				  <div class="selectStyle">
						<form:select path="activityLevelId" itemValue="key"
							itemLabel="value" id="activityLevelId">
							<form:option value="">不限</form:option>
							<form:options items="${productLevels}" />
						</form:select>
				  </div>
			  </div>
				<div class="activitylist_bodyer_right_team_co3">
	         		<label class="activitylist_team_co3_text" for="spinner"  class="fl">行程天数：</label>
					<input id="activityDuration" class="spinner" maxlength="3" name="activityDuration" value="${param.activityDuration }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
				</div>
            </c:if>
			<div class="activitylist_bodyer_right_team_co4 sCurrency">
				<label class="activitylist_team_co3_text">同行价格：</label>
				<div class="selectStyle">
				<select id="selectCurrencyType" name="currencyType">
<!-- 					<option value="1">人民币</option> -->
					<c:forEach items="${currencyList}" var="currency" varStatus="s">
						<c:if test="${currency.id != '1'}">
						<option value="${currency.id}">${currency.currencyName}</option>
						</c:if>
	                </c:forEach>
				</select>
				</div>
				<input type="text" id="settlementAdultPriceStart" class="inputTxt" name="settlementAdultPriceStart" value="${settlementAdultPriceStart }" />
				<span style="font-size:12px;font-family:'宋体';"> 至</span>
				<input type="text" id="settlementAdultPriceEnd" class="inputTxt" name="settlementAdultPriceEnd" value="${settlementAdultPriceEnd }" />
         	</div>
         	<c:if test="${activityKind ne 10 }">
			<div class="activitylist_bodyer_right_team_co1">
         		<label class="activitylist_team_co3_text" for="spinner"  class="fl" >行程天数：</label>
				<input id="activityDuration" class="spinner" maxlength="3" name="activityDuration" value="${param.activityDuration }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
			</div>
            <div class="activitylist_bodyer_right_team_co1">
                   <div class="activitylist_team_co3_text">旅游类型：</div>
				   <div class="selectStyle">
					   <form:select path="travelTypeId" itemValue="key" itemLabel="value" id="travelTypeId">
						   <form:option value="">不限</form:option>
						   <form:options items="${travelTypes}" />
					   </form:select>
				   </div>
         		</div>
			<div class="activitylist_bodyer_right_team_co1">
            	<div class="activitylist_team_co3_text">产品系列：</div>
				<div class="selectStyle">
					<form:select path="activityLevelId" itemValue="key" itemLabel="value" id="productser">
						<form:option value="">不限</form:option>
						<form:options items="${productLevels}" />
					</form:select>
				</div>
           	</div>
			<div class="activitylist_bodyer_right_team_co1">
        	<div class="activitylist_team_co3_text">产品类型：</div>
				<div class="selectStyle">
					<form:select path="activityTypeId" itemValue="key" itemLabel="value" id="productType">
						<form:option value="">不限</form:option>
						<form:options items="${productTypes}" />
					</form:select>
				</div>
           </div>
           </c:if>
<%--不使用的筛选条件====================			--%>           
<%--   		<div class="activitylist_bodyer_right_team_co3">--%>
<%--            <div class="activitylist_team_co3_text">产品编号：</div>--%>
<%--           <input id="activitySerNum" name="activitySerNum" class="inputTxt"  value="${travelActivity.activitySerNum }"> --%>
<%--        </div>--%>
<%--	      <div class="kong"></div>--%>
<%----%>
<%--============================--%>
			</div>
         </div>
	</form:form>
	
	<c:if test="${fn:length(page.list) ne 0}">
		<div class="activitylist_bodyer_right_team_co_paixu">
	        <div class="activitylist_paixu">
				<div class="activitylist_paixu_left">
					<ul>
			            <li class="activitylist_paixu_left_biankuang liid"><a onClick="sortby('id',this)">创建时间</a></li>
			            <li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
			            <li class="activitylist_paixu_left_biankuang lisettlementAdultPrice"><a onClick="sortby('settlementAdultPrice',this)">同行价格</a></li>
			            <c:if test="${activityKind eq '2' or activitykind eq '10'}">
			            	<li class="activitylist_paixu_left_biankuang lisuggestAdultPrice"><a onClick="sortby('suggestAdultPrice',this)">直客价格</a></li>
			            </c:if>
			            <li class="activitylist_paixu_left_biankuang ligroupOpenDate"><a onClick="sortby('groupOpenDate',this)">出团日期</a></li>
	            	</ul>
				</div>
	          <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
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
	<table id="contentTable" class="table mainTable activitylist_bodyer_table" >
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
				<c:if test="${activityKind eq '2' or activityKind eq '10'}">
					<th width="10%">成人直客价</th>
				</c:if>
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
<%--						     	<a style="text-decoration: none; color:inherit; cursor:default;" title="${activity.activitySerNum}"><c:out value="${fn:substring(activity.activitySerNum, 0, 20)}......" /></a> --%>
<%--						     </c:when> --%>
<%--						     <c:otherwise> --%>
<%--						      	<c:out value="${activity.activitySerNum}" /> --%>
<%--						     </c:otherwise>--%>
<%--						</c:choose>--%>
<%--					</td>--%>
					<td class="activity_name_td" style="color:#3a7850;">
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
										<c:set var="visaMapIds" value="${map.value }"></c:set>
									</c:forEach>
								</c:forEach>
								<a href="javascript:void(0)" onClick="downloads(${visaMapIds},'签证文件',null,true)">签证文件</a>
							</c:if>
						</td>
					</c:if>
					<td id="groupdate${activity.id }" align="center">	
					
					<div id="truedate" <c:if test="${groupsize ne 0 }">style="display:block;"</c:if><c:if test="${groupsize == 0 }">style="display:none;"</c:if>>
						<span>${activity.groupOpenDate}至${activity.groupCloseDate}</span><br>
						<a id="close${s.count}" href="javascript:void(0)" class="team_a_click" onClick="expand('#child${s.count}',this,${activity.id })" onMouseenter="if($(this).html()=='全部团期'){$(this).html('展开全部团期')}" onMouseleave="if($(this).html()=='展开全部团期'){$(this).html('全部团期')}">全部团期</a>						
					</div>
					
					<div id="falsedate" <c:if test="${groupsize ne 0 }">style="display:none;"</c:if><c:if test="${groupsize == 0 }">style="display:block;"</c:if>>					
						日期待定
					</div>				                       
					</td>
					<td id="settleadultprice${activity.id }" class="tr"><c:if test="${activity.settlementAdultPrice==0}">价格待定</c:if><c:if test="${activity.settlementAdultPrice>0}">${fns:getCurrencyInfo(activity.currencyType,0,'mark')}<span class="tdred fbold"><fmt:formatNumber type="currency" pattern="#,##00.00" value="${activity.settlementAdultPrice}" /></span>起</c:if></td>
					<c:if test="${activityKind eq '2' or activityKind eq '10'}">
						<td id="suggestadultprice${activity.id }" class="tr"><c:if test="${activity.suggestAdultPrice==0}">价格待定</c:if><c:if test="${activity.suggestAdultPrice>0}">${fns:getCurrencyInfo(activity.currencyType,1,'mark')}<span class="tdblue fbold"><fmt:formatNumber type="currency" pattern="#,##00.00" value="${activity.suggestAdultPrice}" /></span>起</c:if></td>
					</c:if>
					<td class="p0">
                    	<dl class="handle">
                    		<dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
                    		<dd class="">
                 				<p>
                 					<span></span>
									<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${activity.id}')">详情</a>
									<shiro:hasPermission name="${shiroType}Product:operation:edit"><br/><a href="javascript:void(0)" onClick="productModify(${activity.id})">修改</a><br/></shiro:hasPermission>
									<a href="javascript:void(0)" onClick="onLineProduct(${activity.id})">上架</a>
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
                      <form id="childform${s.count}" style="margin: 0px">
						<table id="teamTable" class="table activitylist_bodyer_table table-mod2-group" style="margin:0 auto;">
							<c:set var="colspanNum" value="3"></c:set>
							<thead>
								<tr>
									<th rowspan="2" width="9%">团号</th>
									<th class="tc" rowspan="2" width="8%">出团日期</th>
							        <th class="tc" rowspan="2" width="8%">截团日期</th>
									<th rowspan="2" width="5%">签证国家</th>
									<th rowspan="2" width="7%" class="p0 tc">资料截止日期</th>
									<shiro:hasPermission name="price:project">
										<th rowspan="2" width="12%">酒店房型</th>
									</shiro:hasPermission>
									<c:if test="${activityKind eq '10' }">
										<th rowspan="2" width="4%">舱型</th>
										<c:set var="colspanNum" value="2"></c:set>
									</c:if>
									<c:set var="priceWidth" value="${12/colspanNum }"></c:set>
									<th colspan="${colspanNum }" class="t-th2" width="12%">同行价</th>
									<c:if test="${activityKind eq '2' or activityKind eq '10'}">
										<th colspan="${colspanNum }" class="t-th2" width="12%">直客价</th>
									</c:if>
									<th class="tc" rowspan="2" width="5%">
										订金<c:if test="${activityKind eq '10' }">/人</c:if>
									</th>
									<th class="tc" rowspan="2" width="5%">
										单房差<c:if test="${activityKind eq '10' }">/人</c:if>
									</th>
									<th class="tc" rowspan="2" width="4%">
										预收<c:if test="${activityKind eq '10' }">/间</c:if>
									</th>
									<th class="tc" rowspan="2" width="4%">
										余位<c:if test="${activityKind eq '10' }">/间</c:if>
									</th>
									
									 <!-- 0258需求,发票税:针对懿洋假期-tgy-s-列表:单团,-->
									   <c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' }"> 
									      <th class="tc" rowspan="2" width="4%">发票税</th>
									   </c:if>
 									 <!-- 0258需求,发票税:针对懿洋假期-tgy-e -->
									
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
										</c:otherwise>
									</c:choose>
								</tr>
							</thead>
							<!-- /table><div class="table_activity_scroll ">
							<table class="table activitylist_bodyer_table table-mod2-group"> --><!-- 因bug#13372,列表页不对齐,解决方案-s  -->
							<tbody>
							<c:forEach items="${activity.activityGroupList}" var="group" varStatus="s2">
								<tr id="childtr${s.count}${s2.count}">
								
									<td width="9%"><span id="groupId${group.id}">
										<%--<c:choose> 
										     <c:when test="${fn:length(group.groupCode) > 10}"> 
										    	 <span title="${group.groupCode}"><c:out value="${fn:substring(group.groupCode, 0, 10)}......" /></span> 
										     </c:when> 
										     <c:otherwise> 
										      	<c:out value="${group.groupCode}" /> 
										     </c:otherwise>
										</c:choose>
										--%>${group.groupCode}</span>
										<input type="text" maxlength="50" id="${group.id}" name="groupCode" value="${group.groupCode}"  src="${group.id}"  onKeyUp="validateLong(this)" onafterpaste="replaceStr(this)" style="display:none;"/>
									</td>
									<td width="8%" class="tc">
										<span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/></span>
										<input type="text" id="groupid${s.count}${s2.count}" name="groupid" value="${group.id}" style="display:none;" disabled="disabled"/>
										<input style="display:none;" disabled="disabled" type="text" id="groupOpenDate${s.count}${s2.count}" name="groupOpenDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/>" onClick="WdatePicker()" class="inputTxt"/>
									</td>
									<td width="8%" class="tc">
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
									<!-- 299v2 酒店房型 -->
									<shiro:hasPermission name="price:project">
										<td class="tc hotelAndHouse" width="12%">
											<input type="hidden" name="groupHotel" value="${group.groupHotel}" />
											<input type="hidden" name="groupHouseType" value="${group.groupHouseType}" />
										</td>
									</shiro:hasPermission>
									<c:if test="${activityKind eq '10' }">
										<td width="4%">
											<span>${fns:getDictLabel(group.spaceType, "cruise_type", "-") }</span>
										</td>
									</c:if>									
									<td width="${priceWidth}%" class="tr tdCurrency" <c:if test="${not empty activity.activityAirTicket }">title="包含${activity.activityAirTicket.settlementAdultPrice }机票价"</c:if> >
										<c:if test="${group.settlementAdultPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,0,'mark')}</span><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementAdultPrice }" /></c:if></span>
										<input style="display:none;" disabled="disabled" type="text" id="settlementAdultPrice${s.count}${s2.count}" name="settlementAdultPrice" value="<c:if test="${group.settlementAdultPrice eq 0}"></c:if><c:if test="${group.settlementAdultPrice ne 0}">${group.settlementAdultPrice}</c:if>" maxlength="9"  onkeyup="isMoney(this)" onafterpaste="isMoney(this)"  onblur="isMoney(this)"/>
<!-- 										onkeyup="validatorFloat(this)" onafterpaste="validatorFloat(this)" -->
									</td>
									<td width="${priceWidth}%" class="tr tdCurrency" <c:if test="${not empty activity.activityAirTicket }">title="包含${activity.activityAirTicket.settlementcChildPrice }机票价"</c:if> >
										<c:if test="${group.settlementcChildPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,1,'mark')}</span><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementcChildPrice }" /></c:if></span>
										<input style="display:none;" disabled="disabled"  type="text" id="settlementcChildPrice${s.count}${s2.count}" name="settlementcChildPrice" value="<c:if test="${group.settlementcChildPrice eq 0}"></c:if><c:if test="${group.settlementcChildPrice ne 0}">${group.settlementcChildPrice}</c:if>" maxlength="9"  onkeyup="isMoney(this)" onafterpaste="isMoney(this)"  onblur="isMoney(this)"/>
									</td>
									<c:if test="${activityKind ne '10' }">
										<td width="${priceWidth}%" class="tr tdCurrency" <c:if test="${not empty activity.activityAirTicket }">title="包含${activity.activityAirTicket.settlementSpecialPrice }机票价"</c:if> >
											<c:if test="${group.settlementSpecialPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,2,'mark')}</span><span class="tdred" title="${activity.specialRemark }"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementSpecialPrice }" /></c:if></span>
											<input style="display:none;" disabled="disabled" type="text" id="settlementSpecialPrice${s.count}${s2.count}" name="settlementSpecialPrice" value="<c:if test="${group.settlementSpecialPrice eq 0}"></c:if><c:if test="${group.settlementSpecialPrice ne 0}">${group.settlementSpecialPrice}</c:if>" maxlength="9"  onkeyup="isMoney(this)" onafterpaste="isMoney(this)"  onblur="isMoney(this)"/>
										</td>
									</c:if>
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
										<input style="display:none;" disabled="disabled" type="text" id="planPosition${s.count}${s2.count}" name="planPosition" value="${group.planPosition}"  onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="3" onKeyUp="this.value=this.value.replace(/\D/g,'')" />
									</td>
									<td width="4%" class="tr">
										<span class="tdred">${group.freePosition }</span>
										<input style="display:none;" disabled="disabled" type="text" id="freePosition${s.count}${s2.count}" name="freePosition" value="${group.freePosition}" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="3" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
									</td>
									
									 <!-- 0258需求,发票税:针对懿洋假期-tgy-s-列表: -->
									 <c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' }"> 
									 <td width="6%" class="tr">
										<span class="">${group.invoiceTax==null?0:group.invoiceTax}&nbsp;%</span>
										<input style="display:none;width:75%;" disabled="disabled" type="text" id="invoiceTax${s.count}${s2.count}" name="invoiceTax" value="${group.invoiceTax==null?0:group.invoiceTax}" onafterpaste="checkValue(this)" onKeyUp="checkValue(this)" onfocus="checkValue(this)"/><span style="display:none">%</span>
									 </td>
									 </c:if>
 									 <!-- 0258需求,发票税:针对懿洋假期-tgy-e -->
									
									<td width="4%" style="display:none;" class="soldPayPosition${group.id}">
										<span style="color:#eb0205" >0</span>
									</td>
									<td width="7%" class="tnwrap tc">
										<shiro:hasPermission name="${shiroType}Product:operation:groupEdit">
											<%--<a href="javascript:void(0)"  id="modbtn${s.count}${s2.count}"--%>
											<%--onClick="modgroup('#groupid${s.count}${s2.count}','#savebtn${s.count}${s2.count}','#delbtn${s.count}${s2.count}','#cancelbtn${s.count}${s2.count}',this)">修改</a>--%>
										</shiro:hasPermission>
										<shiro:hasPermission name="${shiroType}Product:operation:groupEdit">
											<a href="javascript:void(0)"  id="savebtn${s.count}${s2.count}" style="display:none;" 
											onClick="savegroup(${activity.id},'#modbtn${s.count}${s2.count}','#delbtn${s.count}${s2.count}','#cancelbtn${s.count}${s2.count}',this,'#childform${s.count}','#childtr${s.count}${s2.count}','${ctx}')">确认</a>
										</shiro:hasPermission>										
										<shiro:hasPermission name="${shiroType}Product:operation:groupDelete">
											<a href="javascript:void(0)" id="delbtn${s.count}${s2.count}" onClick="return confirmxCopy('要删除该产品的此团期吗？',${group.id},${activity.id },this,'child${s.count}')">删除</a>
										</shiro:hasPermission>
<%--										<shiro:hasPermission name="group:manager:exportExcel"><br/><a href="javascript:void(0)" id="exportExcelbtn${s.count}${s2.count}" onClick="exportExcel('${group.id}', 'customer', '${group.groupCode}')"><span>导出游客信息</span></a></shiro:hasPermission>--%>
										<shiro:hasPermission name="${shiroType}Product:operation:groupEdit">
											<a style="color:#ec0203;display:none;" id="cancelbtn${s.count}${s2.count}" href="javascript:void(0)" 
											onClick="cancelgroup('#modbtn${s.count}${s2.count}','#savebtn${s.count}${s2.count}','#delbtn${s.count}${s2.count}',this)">取消</a>
										</shiro:hasPermission>
									</td>	
								</tr>
							</c:forEach>
							</tbody>
						</table>
						<!-- </div> --><!-- 因bug#13372,列表页不对齐,解决方案-e  -->
						</form>

						<div style="text-align: right;">
							<div class="kong"></div><div class="kong"></div><div class="kong"></div>
								<input class="btn btn-primary"  style="text-align: right;" type="submit" onClick="return releaseProduct('确定发布该产品吗',${activity.id})" value="重新确认并发布"/>
						</div>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
		<div class="page"></div>
	<div class="pagination">
			<dl>
				<dt><input name="allChk" type="checkbox" onclick="checkall(this)"/>全选</dt>
				<dd>
					<shiro:hasPermission name="${shiroType}Product:operation:edit">	
						<a onClick="confirmBatchIsNull('需要将选择的产品上架吗','on')">批量上架</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${shiroType}Product:operation:delete">	
						<a onClick="confirmBatchIsNull('删除所有选择的团期吗','del')">批量删除</a>
					</shiro:hasPermission>
				</dd>
				
				</dl>
			<div class="endPage">${page}</div>
			</div>
			<br/>
		</c:if>
		<c:if test="${fn:length(page.list) eq 0}">
				<table id="contentTable" class="table mainTable activitylist_bodyer_table" >
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
							<c:if test="${activityKind eq '2' or activityKind eq '10'}">
								<th width="10%">成人直客价</th>
							</c:if>
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
   </div>
   <form id="exportForm" action="${ctx}/activity/manager/exportExcel" method="post">
		<input type="hidden" id="groupId" name="groupId">
		<input type="hidden" id="groupCode" name="groupCode">
	</form>  
</body>
</html>