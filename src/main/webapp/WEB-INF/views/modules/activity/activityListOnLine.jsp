<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>已上架产品</title>
	<meta name="decorator" content="wholesaler"/>
	<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<style type="text/css">
	.sort{color:#0663A2;cursor:pointer;}
	
	/*0071需求样式 */
    label.myerror {
    color: #ea5200;
    font-weight: bold;
    margin-left: 0px;
    padding-bottom: 2px;
    padding-left: 0px;
}
	</style>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/activity/dynamic.group.validator.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/jquery.number.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/validationRules.js"></script><!-- 0258需求校验引入文件 --> 
	<script type="text/javascript">
	
	/*
	 *4 t1t2 打通：
	 *散拼产品列表  点击修改时获取成人、儿童、特殊人群的 价格策略。
	 *在方法  function addQuauqPrice()中  为
	 *成人、儿童、特殊人群的 价格策略 赋值
	 */
	/* var adultPricingStrategy; //成人价策略      
	var childrenPricingStrategy;//儿童价策略     
	var specialPricingStrategy;//特殊人群价策略     
	var quauqAdultPrice; //成人
	var quauqChildPrice;//儿童
	var quauqSpecialPrice;//特殊人群 */
	var paramKind = '${activityKind}';

	var isOnlinePage = "0";
	
    //  对应需求  c460
	var groupCodeRuleDT = '${groupCodeRuleDT}';
	
	//备注右边的提示是否显示
	function remarkVisible(){
	    $('.groupNoteContent').each(function () {
	        if ($(this).html() == '') {
	            $(this).parent().parent().prev().find('.groupNoteTipImg').hide();
	        }
	        else {
	            $(this).parent().parent().prev().find('.groupNoteTipImg').show()
	        }
	    })
	}
    
    //批量操作对应的产品ID   	
	var activityIds = "";
	
	//页面加载执行
	$(function() {	
		g_context_url = "${ctx}";
		   $(document).on('click', '.expandNotes', function () {
	       var $this = $(this);
	       //判断是否展开备注
	       if ($this.parents('tr:first').next().css('display') == 'none') {
	           $this.text('收起备注');
	           $this.parents('tr:first').next().show();
	       }
	       else {
	           $this.text('展开备注');
	           $this.parents('tr:first').next().hide();
	       }
	   });
	   //设置备注是否显示
	   remarkVisible();
			   
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
		
		//设置默认排序方式
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
			// 	bug 12814
			$(this).addClass("group_h_scroll_top");
	           $(this).prev().wrap("<div class='group_h_scroll_top'></div>");
	           }
		  });
		  
  		//上架产品被选中
		var activityStatusValue = '${activityStatusValue}';
		if(activityStatusValue == 'online') {
			$("#online").addClass('active');
		}
		
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
		
		//初始化表单校验信息提示语
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
		
		$('.team_a_click').toggle(function(){
				$(this).addClass('team_a_click2');
			},function(){
				$(this).removeClass('team_a_click2');
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
		
	/*
	 *  展开产品团期
	 */
	function expand(child,obj,srcActivityId){
		$(".groupNoteCol").hide();
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
				   if("${shiroType}" == "loose"){
						//debugger;
						$.ajax({
							type:"post",
							url:"${ctx}/pricingStrategy/manager/findPricingStrategy",
							data:{
								srcActivityId:srcActivityId
							},
							success:function(msg){
								debugger;
							}
						});
				   }
				   $(obj).html("关闭全部团期");
				   //修改bug16272，每次展开全部团期时都显示展开备注
				   $(obj).parents("tr:first").next().find('a.expandNotes').html('展开备注');
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
	/**
		分页方法
	*/
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
									$("#settleadultprice"+proId).html(result.settlementAdultPriceCMark+"<span class=\"tdred fbold\">"+result.settlementAdultPrice+"</span>起");
								else
									$("#settleadultprice"+proId).html("价格待定");
								
								if(result.suggestAdultPrice)
									$("#suggestadultprice"+proId).html(result.suggestAdultPriceCMark+"<span class=\"tdblue fbold\">"+result.suggestAdultPrice+"</span>起");
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
	
	/**
		判断批量操作是否勾选数据
	*/
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
	
	//删除某团期
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
	    
	    //修改产品,匹配quauq策略,对旧数据的quauq价进行处理-----t1t2--点击修改按钮后,对quauq价和供应价的出路--djw------------------
	    function modgroup(groupId,activityId,groupid,savebtn,delbtn,cancelbtn,obj){
	    	//debugger;
		   /*  var activityKind = '${activityKind}';
		  	//获取产品基本策略匹配参数
		    if(activityKind == '2'){
				//var fromArea = $(obj).parent().parent().find("input[name='fromAreaNum']").val(); //出发城市
				//var targetAreaId = $(obj).parent().parent().find("input[name='targetAreaIds']").val(); //目的地
				//var travelTypeId = $(obj).parent().parent().find("input[name='travelTypeId']").val(); //旅游类型
				//var activityTypeId = $(obj).parent().parent().find("input[name='activityTypeId']").val(); //产品类型
				//var activityLevelId = $(obj).parent().parent().find("input[name='activityLevelId']").val(); //产品系列
		    	//var flag = false;
					
				$.ajax({
						type: "POST",
						async:false,
						url: "${ctx}/activity/manager/checkQuauqPrice",
						data: {"groupid":groupId,"activityId":activityId},
						success: function(data){
							//debugger;
							//if(data.result=="0"){
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
					/*          //flag = true; 
								//adultQuauqPriceStrategy = null; //成人价策略    
						        //childrenQuauqPriceStrategy = null;//儿童价策略 
						        // spicalQuauqPriceStratety = null;
						        //debugger;
						        quauqAdultPrice_temp = data.quauqAdultPrice; 
						        quauqChildPrice_temp = data.quauqChildPrice;
						        quauqSpecialPrice_temp = data.quauqSpecialPrice;
						        //alert(quauqAdultPrice_temp);
								updateQuauqPrice(obj);
//								var settlementAdultPrice = $(obj).parents('tr:first').find("input[name='settlementAdultPrice']").val();
//								var settlementChildPrice = $(obj).parents('tr:first').find("input[name='settlementcChildPrice']").val();
//								var settlementSpecalPrice = $(obj).parents('tr:first').find("input[name='settlementSpecialPrice']").val();
//								$(obj).parents('tr:first').find("input[name='quauqAdultPrice']").val(settlementAdultPrice);
//								$(obj).parents('tr:first').find("input[name='quauqChildPrice']").val(settlementChildPrice);
//								$(obj).parents('tr:first').find("input[name='quauqSpecialPrice']").val(settlementSpecalPrice);
//								$(obj).parents('tr:first').find("input[name='quauqAdultPrice']").val(quauqAdultPrice);
//								$(obj).parents('tr:first').find("input[name='quauqChildPrice']").val(quauqChildPrice);
//								$(obj).parents('tr:first').find("input[name='quauqSpecialPrice']").val(quauqSpecialPrice);
								
							//}else{
								 //quauqPrice4Adult##2:12#2:12,3:2#2:13
								 //quauqPrice4Child##2:13#2:1#2:14
								 //quauqPrice4SpicalPerson##2:15
							//} 
						}
				});
				
				//if(flag == false){
					//return;
	    			//updateQuauqPrice(obj);
				//}
				
				//获取策略
				$.ajax({
					type: "POST",
					async:false,
					url: "${ctx}/activity/manager/checkActivityPriceStrategy",
					data: {"groupid":groupId,"activityId":activityId},
					success: function(data){
						
					        //debugger;
					        adultPricingStrategy = data.adultPricingStrategy; 
					        childrenPricingStrategy = data.childrenPricingStrategy;
					        specialPricingStrategy = data.specialPricingStrategy;
					        //alert(adultPricingStrategy);

					}
			});
		} */
		  	
		  	
	    	
	    	
	    	//debugger; 
	    	//alert("--开始修改了");
	    	//要修改的元素的团号span
	    	var span4Modified=$("#groupId"+$(groupid).val());
	    	//要修改的元素的input文本框
	    	var text44Modified=$("#"+$(groupid).val());
	    	//批发商的uuid是否为优加
	    	var uuidTemp='${uuid4ManualModifyGroupcode}';
	    	// 对应需求 c460 添加   groupCodeRuleDT==0
	    	if(uuidTemp=='f5c8969ee6b845bcbeb5c2b40bac3a23' || uuidTemp=='7a81c5d777a811e5bc1e000c29cf2586' || uuidTemp=='5c05dfc65cd24c239cd1528e03965021' || (groupCodeRuleDT==0 && uuidTemp != '7a8177e377a811e5bc1e000c29cf2586' && uuidTemp != 'ed88f3507ba0422b859e6d7e62161b00' && uuidTemp != 'f5c8969ee6b845bcbeb5c2b40bac3a23' && uuidTemp != '58a27feeab3944378b266aff05b627d2' && uuidTemp != '7a81a26b77a811e5bc1e000c29cf2586'&& uuidTemp != '7a45838277a811e5bc1e000c29cf2586')){ //为优加、起航假期 则可手输    
	    	   //隐藏团号span,显示团号文本框
	    	   span4Modified.hide();
	    	   text44Modified.show();
	    	} 
	    	
	    	$(obj).parent().parent().find("span").hide();
	    	$(obj).parent().parent().find("span").eq(0).show();
	    	$(obj).parent().parent().find("span[class='rm']").show();
	    	$(obj).parent().parent().find("span[class='houseAndType']").show();
	    	$(obj).parent().parent().find("input[type='text']").css("display","inline-block");
	    	//*0258需求,发票税:针对懿洋假期-tgy-s,显示文本框后的%号(如果table结构发生变化,则也需要相应改变),单团,*//
	    	$(obj).parent().prev().prev().find("input").next().css("display","inline-block");
	    	//*258需求,发票税:针对懿洋假期-tgy-e*//
	    	$(obj).parent().parent().find("input[type='text']").attr("disabled",false);
	    	$(obj).parent().parent().find("input[type='checkbox'][flag*='mod']").css("display","block");
	    	$(obj).parent().parent().find("input[type='checkbox'][flag*='show']").css("display","none");
	    	$(obj).parent().parent().find("input[type='checkbox'][flag*='mod']").attr("disabled",false);
	    	var checkedFlag = $(obj).parent().parent().find("input[type='checkbox'][flag*='show']").attr("checked");
	    	if(checkedFlag == undefined | checkedFlag == null | checkedFlag == ''){
	    		$(obj).parent().parent().find("input[type='checkbox'][flag*='mod']").attr("checked", false);
	    	}else if (checkedFlag == 'checked'){
		    	$(obj).parent().parent().find("input[type='checkbox'][flag*='mod']").attr("checked", "checked");
	    	}
	    	$(groupid).hide();
	    	
	    	$(groupid).attr("disabled",false);
	    	$(savebtn).show();
	    	$(delbtn).hide();
	    	$(cancelbtn).show();
	    	$(obj).hide();
			//展开备注
		    $(obj).parent().parent().find('.expandNotes').hide();
		    $(obj).parent().parent().find('.groupNoteTipImg').hide();
		    
		    $(obj).parent().parent().next().show();
		    //设置备注框
		    var remark = $(obj).parent().parent().next().find('[name="groupNoteContent"]').text();
		    $(obj).parent().parent().next().find('[name="groupNote"]').val(remark);
		    $(obj).parent().parent().next().find('div:first').hide().next().show();
		   
		    //对应需求 c460 添加   groupCodeRuleDT==0
		   if(uuidTemp=='f5c8969ee6b845bcbeb5c2b40bac3a23' || uuidTemp=='7a81c5d777a811e5bc1e000c29cf2586' || uuidTemp=='5c05dfc65cd24c239cd1528e03965021' || (groupCodeRuleDT==0 && uuidTemp != '7a8177e377a811e5bc1e000c29cf2586' && uuidTemp != 'ed88f3507ba0422b859e6d7e62161b00' && uuidTemp != '58a27feeab3944378b266aff05b627d2' && uuidTemp != '7a81a26b77a811e5bc1e000c29cf2586'&& uuidTemp != '7a45838277a811e5bc1e000c29cf2586')){//为优加、起航假期 则可手输       对应需求 c460 添加   groupCodeRuleDT==0
		    //再次隐藏团号span
		    span4Modified.hide();
		    }
		    //对应需求 c460 添加   groupCodeRuleDT==0
		    if(uuidTemp!='7a81c5d777a811e5bc1e000c29cf2586' && uuidTemp!='5c05dfc65cd24c239cd1528e03965021' && (groupCodeRuleDT!=0 || uuidTemp=='7a8177e377a811e5bc1e000c29cf2586' || uuidTemp=='ed88f3507ba0422b859e6d7e62161b00' || uuidTemp=='58a27feeab3944378b266aff05b627d2'|| uuidTemp=='7a81a26b77a811e5bc1e000c29cf2586'|| uuidTemp=='7a45838277a811e5bc1e000c29cf2586')){ //不为优加、起航假期,需要隐藏团号文本框     对应需求 c460 添加   groupCodeRuleDT==1
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
	    	$(obj).parent().parent().find("input[type='checkbox'][flag*='mod']").css("display","none");
	    	$(obj).parent().parent().find("input[type='checkbox'][flag*='mod']").attr("disabled",true);
	    	$(obj).parent().parent().find("input[type='checkbox'][flag*='show']").css("display","block");
//	    	$(obj).parent().parent().find("label",".error").remove();
	    	 //设置备注状态
	    	$(obj).parent().parent().find('.expandNotes').text('展开备注');
	    	$(obj).parent().parent().find('.expandNotes').show();
	    	var remark = $(obj).parent().parent().next().find('.groupNoteContent').text();
	    	if (remark == null || remark == 'undefined' || remark == '') {
					$(obj).parent().parent().find('.groupNoteTipImg').hide();
				}else{
					$(obj).parent().parent().find('.groupNoteTipImg').show();
				}
    		$(obj).parent().parent().next().hide();
    		$(obj).parent().parent().next().find('div:first').show().next().hide();
	    }
	    
	    //下载文件
	    function downloads(docids,zipname,acitivityName,iszip){
	    	if(iszip){
	    		//var zipname = activitySerNum;
	    		window.open(encodeURI(encodeURI("${ctx}/sys/docinfo/zipdownload/"+docids+"/"+zipname)));
	    	}
	    		
	    	else
	    		window.open("${ctx}/sys/docinfo/download/"+docid);
	    }
	    
	    //产品修改,t1t2-v2增加修改链接区分
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
		
		//全选操作
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
		
		//排序
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
		
	//下架产品	
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

	//--071需求校验签证国家的字符串长度函数
	function checkVisaCountrylen(v1,v2){
		//0071-进来先清空提示span-防止bug-s
        $("[name='span4visaCountry']").empty();
       //0071-进来先清空提示span-防止bug-e
		var arrVisaCountry=$("#visaCountry"+(v1+"".concat(v2)));
	    if(arrVisaCountry.val().length>50){
			$("#visaCountry"+v1+"".concat(v2)).after('<label name="span4visaCountry" style="display:block"  class="myerror">请输入一个长度最多是50的字符串</label>');
			 top.$.jBox.info("请先修改完错误再提交", "警告");
             top.$('.jbox-body .jbox-icon').css('top','55px');
             return;
		}
	}
	
// 	 function groupNolength(obj){//团号超过50字提醒
//     if( $(obj).val().length>50){
//         top.$.jBox.tip("团号超过50个字符，请修改", 'warnning');
//         return false;
//     }
//   }
	 
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
	 
	 //189  优加
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
							//$(obj).parent().parent().
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
//		if(""!=thisvalue){
//			supplyvalue = thisvalue * rate + parseFloat(thisvalue);
//		}
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
	         			//$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").removeAttr("readonly");
	     				var adultQuauqPrice = getQuauqPrice(groupid,activityId,inputName,thisvalue);
						//20160713修改同行价，如果有策略则显示quauq价和供应价，反之则无显示。
						if("0.001" != adultQuauqPrice) {
							$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").val(adultQuauqPrice);
							//增加供应价成人
							$(dom).parents('tr:first').find("input[name='supplyAdultPrice']").val(xround(adultQuauqPrice*rate,2));
						}else {
							$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").val("");
							//增加供应价成人
							$(dom).parents('tr:first').find("input[name='supplyAdultPrice']").val("");
						}
	         			//$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").val(adultQuauqPrice);
						//增加供应价成人
						//$(dom).parents('tr:first').find("input[name='supplyAdultPrice']").val(xround(adultQuauqPrice*rate,2));
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
	     			    //$(dom).parents('tr:first').find("input[name='quauqChildPrice']").removeAttr("readonly");
	     			    var quauqChildPrice = getQuauqPrice(groupid,activityId,inputName,thisvalue);
						//20160713修改同行价，如果有策略则显示quauq价和供应价，反之则无显示。
						if("0.001" != quauqChildPrice) {
							$(dom).parents('tr:first').find("input[name='quauqChildPrice']").val(quauqChildPrice);
							//增加供应价儿童
							$(dom).parents('tr:first').find("input[name='supplyChildPrice']").val(xround(quauqChildPrice*rate,2));
						}else {
							$(dom).parents('tr:first').find("input[name='quauqChildPrice']").val("");
							//增加供应价儿童
							$(dom).parents('tr:first').find("input[name='supplyChildPrice']").val("");
						}
	     			    //$(dom).parents('tr:first').find("input[name='quauqChildPrice']").val(quauqChildPrice);
						//增加供应价儿童
						//$(dom).parents('tr:first').find("input[name='supplyChildPrice']").val(xround(quauqChildPrice*rate,2));
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
	     			    //$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").removeAttr("readonly");
	     			    var quauqSpecialPrice = getQuauqPrice(groupid,activityId,inputName,thisvalue);
						//20160713修改同行价，如果有策略则显示quauq价和供应价，反之则无显示。
						if("0.001" != quauqSpecialPrice) {
							$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").val(quauqSpecialPrice);
							//增加供应价特殊人群
							$(dom).parents('tr:first').find("input[name='supplySpecialPrice']").val(xround(quauqSpecialPrice*rate,2));
						}else {
							$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").val("");
							//增加供应价特殊人群
							$(dom).parents('tr:first').find("input[name='supplySpecialPrice']").val("");
						}
	     			    //$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").val(quauqSpecialPrice);
						//增加供应价特殊人群
						//$(dom).parents('tr:first').find("input[name='supplySpecialPrice']").val(xround(quauqSpecialPrice*rate,2));
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
    			var quauqAdultPrice = getQuauqPrice(groupid,activityId,"settlementAdultPrice",settlementAdultPrice);
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
    						
	    					top.$.jBox.info("QUAUQ价应低于或等于渠道最低价，请重新修改！", "提示");
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
					var quauqChildPrice = getQuauqPrice(groupid,activityId,"settlementcChildPrice",settlementcChildPrice);
				//	if(null != childrenQuauqPriceStrategy){
    			//		childrenQuauqPrice = getQuauqPrice(settlementcChildPrice,childrenQuauqPriceStrategy);
				//	}else{
				//		childrenQuauqPrice = settlementcChildPrice;
				//	}
    				if(new Number(thisvalue)>new Number(quauqChildPrice)){
    					if(settlementcChildPrice == quauqChildPrice){
	    					top.$.jBox.info("QUAUQ价应低于或等于同行价，请重新修改！", "提示");
    					}else{
    						
	    					top.$.jBox.info("QUAUQ价应低于或等于渠道最低价，请重新修改！", "提示");
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
					var quauqSpecialPrice = getQuauqPrice(groupid,activityId,"settlementSpecialPrice",settlementSpecialPrice);;
				//	if(null != spicalQuauqPriceStratety){
    			//		spicalQuauqPrice = getQuauqPrice(settlementSpecialPrice,spicalQuauqPriceStratety);
				//	}else{
				//		spicalQuauqPrice = settlementSpecialPrice;
				//	}
    				if(new Number(thisvalue)>new Number(quauqSpecialPrice)){

    					if(settlementSpecialPrice == quauqSpecialPrice){
	    					top.$.jBox.info("QUAUQ价应低于或等于同行价，请重新修改！", "提示");
    					}else{
    						
	    					top.$.jBox.info("QUAUQ价应低于或等于渠道最低价，请重新修改！", "提示");
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
	 function updateQuauqPrice(obj){
		//alert(${group.quauqAdultPrice});
		//adultQuauqPrice,childrenQuauqPrice,spicalQuauqPrice,
		//if(paramKind == '2'){//只有散拼产品才做如下操作
			var quauqAdultPrice=$(obj).parent().parent().find("input[name='quauqAdultPrice']").val();
			var quauqChildPrice=$(obj).parent().parent().find("input[name='quauqChildPrice']").val();
			var quauqSpecialPrice=$(obj).parent().parent().find("input[name='quauqSpecialPrice']").val();
			//debugger;
			var settlementAdultPrice = $(obj).parents('tr:first').find("input[name='settlementAdultPrice']").val();
			var settlementChildPrice = $(obj).parents('tr:first').find("input[name='settlementcChildPrice']").val();
			var settlementSpecalPrice = $(obj).parents('tr:first').find("input[name='settlementSpecialPrice']").val();
//			var adultQuauqPrice_temp = getQuauqPrice(settlementAdultPrice, adultQuauqPriceStrategy);
//			var childQuauqPrice_temp = getQuauqPrice(settlementChildPrice, childrenQuauqPriceStrategy);
//			var specalQuauqPrice_temp = getQuauqPrice(settlementSpecalPrice, spicalQuauqPriceStratety);
			/* if(adultQuauqPrice == null || adultQuauqPrice <= adultQuauqPrice_temp){
				return;
			} */
			
			//由于策略修改,或者页面修改 导致的quauq价变动,quauq价取计算出的quauq价和库中的quauq价的最小值
			//首先判断是否存在策略  
//			if(adultQuauqPriceStrategy.length != 0){   //同行价策略不为空时
//				if(settlementAdultPrice != null && settlementAdultPrice != ""){
					if(new Number(quauqAdultPrice) >= new Number(quauqAdultPrice_temp)){
						quauqAdultPrice = quauqAdultPrice_temp;
				//		$(obj).parents('tr:first').find("input[name='quauqAdultPrice']").val(adultQuauqPrice);
					}
				//	else{
						$(obj).parents('tr:first').find("input[name='quauqAdultPrice']").val(quauqAdultPrice);
				//	}
				//}else{
				//	adultQuauqPrice = "";
				//	$(obj).parent().parent().find("input[name='quauqAdultPrice']").parent().find("span[class!='rm']").html('');
				//	$(obj).parents('tr:first').find("input[name='quauqAdultPrice']").val('');
				//	$(obj).parents('tr:first').find("input[name='quauqAdultPrice']").attr("readonly","readonly");
				//}
			//}else{
			//	adultQuauqPrice = "";
			//	$(obj).parent().parent().find("input[name='quauqAdultPrice']").parent().find("span[class!='rm']").html('');
			//	$(obj).parents('tr:first').find("input[name='quauqAdultPrice']").val('');
			//	$(obj).parents('tr:first').find("input[name='quauqAdultPrice']").attr("readonly","readonly");
			//}
			
			//儿童策略不为空时
			//if(childrenQuauqPriceStrategy.length != 0){
			//	if(settlementChildPrice != null && settlementChildPrice != ""){
					if(new Number(quauqChildPrice) >= new Number(quauqChildPrice_temp)){
						quauqChildPrice = childQuauqPrice_temp;
			//			$(obj).parents('tr:first').find("input[name='quauqChildPrice']").val(childrenQuauqPrice);
					}
			//		else{
					$(obj).parents('tr:first').find("input[name='quauqChildPrice']").val(quauqChildPrice);
			//		}
			//	}else{
			//		childrenQuauqPrice = "";
			//		$(obj).parent().parent().find("input[name='quauqChildPrice']").parent().find("span[class!='rm']").html('');
			//		$(obj).parents('tr:first').find("input[name='quauqChildPrice']").val('');
			//		$(obj).parents('tr:first').find("input[name='quauqChildPrice']").attr("readonly","readonly");
			//	}
			//}else{
			//	childrenQuauqPrice = "";
			//	$(obj).parent().parent().find("input[name='quauqChildPrice']").parent().find("span[class!='rm']").html('');
			//	$(obj).parents('tr:first').find("input[name='quauqChildPrice']").val('');
			//	$(obj).parents('tr:first').find("input[name='quauqChildPrice']").attr("readonly","readonly");
			//}
			
			//特殊人群策略不为空时
			//if(spicalQuauqPriceStratety != 0){
			//	if(settlementSpecalPrice != null && settlementSpecalPrice != ""){
					if(new Number(quauqSpecialPrice) >= new Number(quauqSpecialPrice_temp)){
						quauqSpecialPrice = quauqSpecialPrice_temp;
			//			$(obj).parents('tr:first').find("input[name='quauqSpecialPrice']").val(spicalQuauqPrice);
					}
			//		else{
						$(obj).parents('tr:first').find("input[name='quauqSpecialPrice']").val(quauqSpecialPrice);
			//		}
			//	}else{
			//		spicalQuauqPrice = "";
			//		$(obj).parent().parent().find("input[name='quauqSpecialPrice']").parent().find("span[class!='rm']").html('');
			//		$(obj).parents('tr:first').find("input[name='quauqSpecialPrice']").val('');
			//		$(obj).parents('tr:first').find("input[name='quauqSpecialPrice']").attr("readonly","readonly");
			//	}
			//}else{
			//	spicalQuauqPrice = "";
			//	$(obj).parent().parent().find("input[name='quauqSpecialPrice']").parent().find("span[class!='rm']").html('');
			//	$(obj).parents('tr:first').find("input[name='quauqSpecialPrice']").val('');
			//	$(obj).parents('tr:first').find("input[name='quauqSpecialPrice']").attr("readonly","readonly");
			//}
		//}
		
	}
	
	
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
//	    	var settlementAdultPrice = $(obj).parents('tr:first').find("input[name='settlementAdultPrice']").val();
//			var settlementChildPrice = $(obj).parents('tr:first').find("input[name='settlementcChildPrice']").val();
//			var settlementSpecalPrice = $(obj).parents('tr:first').find("input[name='settlementSpecialPrice']").val();
	    	
		
//			$(obj).parents('tr:first').find("input[name='quauqAdultPrice']").val(settlementAdultPrice);
//			$(obj).parents('tr:first').find("input[name='quauqChildPrice']").val(settlementChildPrice);
//			$(obj).parents('tr:first').find("input[name='quauqSpecialPrice']").val(settlementSpecalPrice);
//			$(obj).parents('tr:first').find("input[name='quauqAdultPrice']").val(quauqAdultPrice);
//			$(obj).parents('tr:first').find("input[name='quauqChildPrice']").val(quauqChildPrice);
//			$(obj).parents('tr:first').find("input[name='quauqSpecialPrice']").val(quauqSpecialPrice);
    	
	
		//srcPriceStrategy 为空  直接 返回 "";
//		if(srcPriceStrategy == ""){
//			return srcPrice;
//		}
		
		/* if(strategy.getPersonType() == 0){
			if(strategy.getFavorableType()==2 && adultPrice != null && adultPrice.compareTo(BigDecimal.ZERO) > 0){
				adultPrice = adultPrice.subtract(strategy.getFavorableNum());
			}else if(strategy.getFavorableType()==3){
				adultPrice = adultPrice.multiply(strategy.getFavorableNum().divide(new BigDecimal("100")));
			}
		}else if(strategy.getPersonType() == 1 && childrenPrice!= null && childrenPrice.compareTo(BigDecimal.ZERO) > 0){
			if(strategy.getFavorableType()==2){
				childrenPrice = childrenPrice.subtract(strategy.getFavorableNum());
			}else if(strategy.getFavorableType()==3){
				childrenPrice = childrenPrice.multiply(strategy.getFavorableNum().divide(new BigDecimal("100")));
			}
		}else if(strategy.getPersonType() == 2 && specialPrice!=null && specialPrice.compareTo(BigDecimal.ZERO) > 0){
			if(strategy.getFavorableType()==2){
				specialPrice = specialPrice.subtract(strategy.getFavorableNum());
			}else if(strategy.getFavorableType()==3){
				specialPrice = specialPrice.multiply(strategy.getFavorableNum().divide(new BigDecimal("100")));
			}
		} */
		//debugger;
		/* var srcPriceStrategyArray = srcPriceStrategy.split("#");
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
		return  xround(minQuauqPrice,2); */
		
	}
    
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
	
	<style type="text/css">
	
		
        #contentTable th {
            height: 40px;
            border-top: 1px solid #CCC;
            }
        #teamTable{
            border:1px solid #CCC;
            }
            .groupNoteTipImg {
                display: inline-block;
                width: 12px;
                height: 12px;
                background-image: url("${ctxStatic}/images/order_s3.png");
                background-repeat: no-repeat;
                background-position: 0px center;
                margin: 4px 0px 0px 5px;
                line-height: 8px;
                vertical-align: top;
            }
</style>
<!-- 需求223 -->
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/quauq.css" />
    <script type="text/javascript">
    	var $ctx = '${ctx}';
    </script>
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/quauq.base64.js"></script>
<%-- 	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script> --%><!-- 该js和页面中方法冲突，放到上面引入 -->
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.plus.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/boxScroll.js"></script>
    <script type="text/javascript" src="${ctxStatic}/modules/store/billboard.js"></script>
    <script type="text/javascript">
    	$(document).ready(function() {
    		var $ctx = '${ctx}';
    	});
    	function save(){
    		alert();
    	}
    </script>
<!-- 需求223 -->
</head>
<body>
	<page:applyDecorator name="activity_op_head" >
		<page:param name="current">online</page:param>
	</page:applyDecorator>
	
	
		
	
	<!-- 签证公告展示 -->
	<%@ include file="/WEB-INF/views/modules/visa/visaPublicBulletinMarquee.jsp"%>
	<!-- 需求223 看板 -->
	<shiro:hasPermission name="cruiseshipStockList:stock:board">
		<c:if test="${activityKind eq '10'}">
			<%@ include file="/WEB-INF/views/modules/cruiseship/cruiseshipboard/cruiseshipboard.jsp"%>
		</c:if>
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
	         	<input class="txtPro inputTxt searchInput"
					   id="wholeSalerKey" name="wholeSalerKey" value="${travelActivity.acitivityName }" placeholder="输入产品名称、团号，支持模糊匹配"/>
         	</div>
			 <a class="zksx">筛选</a>
       		<div class="form_submit">
					<input class="btn btn-primary ydbz_x" type="submit" value="搜索"/>
					<input type="button" value="清空所有条件" onclick="resetSearchParams()" class="btn ydbz_x" />
				    <!--  团期产品  非常国际、优加 、起航假期  添加团号库 -->
				    <!--  对应需求号   c460   添加    fns:getUser().company.groupCodeRuleDT eq 1 -->
				    <c:if test="${fns:getUser().company.uuid eq '7a81c5d777a811e5bc1e000c29cf2586' || fns:getUser().company.uuid eq '1d4462b514a84ee2893c551a355a82d2' || fns:getUser().company.uuid eq '5c05dfc65cd24c239cd1528e03965021' || fns:getUser().company.groupCodeRuleDT == 0}">
						<c:if test="${fns:getUser().company.isNeedGroupCode eq 1}"> 
					    	<input class="btn btn-primary " type="button" onclick="openGroupLibPage()" value="团号库"/>
				    	</c:if>
				    </c:if>
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
				<div class="activitylist_bodyer_right_team_co3">
	         		<div for="spinner"  class="fl activitylist_team_co3_text" style="line-height:28px;">行程天数：</div>
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
				<input  type="text" id="settlementAdultPriceStart" class="inputTxt" name="settlementAdultPriceStart" value="${settlementAdultPriceStart }" />
				<span style="font-size:12px;font-family:'宋体';"> 至</span>
				<input type="text" id="settlementAdultPriceEnd" class="inputTxt" name="settlementAdultPriceEnd" value="${settlementAdultPriceEnd }" />
         	</div>
         	<c:if test="${activityKind ne 10 }">
			<div class="activitylist_bodyer_right_team_co1">
         		<div for="spinner"  class="fl activitylist_team_co3_text" style="line-height:28px;">行程天数：</div>
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
				<label class="activitylist_team_co3_text">产品类型：</label>
				<div class="selectStyle">
					<form:select path="activityTypeId" itemValue="key" itemLabel="value" id="productType">
						<form:option value="">不限</form:option>
						<form:options items="${productTypes}" />
					</form:select>
				</div>
           </div>
           </c:if>
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
			<div class="activitylist_bodyer_right_team_co_paixu">
				<div class="activitylist_paixu">
					<div class="activitylist_paixu_left">
						<ul>
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
						<td align="center">
							<label class="qtip" title="${activity.trafficNameDesc}">
								<c:set var="fligthInfoStr" value=""></c:set>
								<c:forEach items="${activity.activityAirTicket.flightInfos }" var="fligthInfo">
									<c:set var="fligthInfoStr" value="${fligthInfoStr }${fn:replace(fligthInfo.airlines,'-1','-')},"></c:set>
								</c:forEach>
								${fligthInfoStr }
							</label>
						</td>
						<td align="center">
							<c:if test="${!empty visaMapList}">
								<c:forEach items="${visaMapList}" var="visas">
										<c:if test="${activity.id eq visas.srcActivityId}"> 
										<a href="javascript:void(0)" onClick="downloads('${visas.docInfoId}',
										'${fns:getCountryName(visas.countryId)}',null,true)">${fns:getCountryName(visas.countryId)}</a>
										</c:if>
								
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
					<c:if test="${activityKind eq '2' or activityKind eq '10'}">
						<td id="suggestadultprice${activity.id }" class="tr"><c:if test="${activity.suggestAdultPrice==0}">价格待定</c:if><c:if test="${activity.suggestAdultPrice>0}">${fns:getCurrencyInfo(activity.currencyType,1,'mark')}<span class="tdblue fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${activity.suggestAdultPrice}" /></span>起</c:if></td>
					</c:if>
					<td class="p0">
                    	<dl class="handle">
                    		<dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
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
									<th rowspan="2" width="6%">团号</th>
									<c:if test="${activityKind eq '2'}">
										<shiro:hasPermission name="calendarLoose:book:order">
											<th rowspan="2" width="4%">推荐</th>
										</shiro:hasPermission>
									</c:if>
									<th class="tc" rowspan="2" width="6%">出团日期</th>
							        <th class="tc" rowspan="2" width="6%">截团日期</th>
									<th rowspan="2" width="3%">签证国家</th>
									<th rowspan="2" width="6%" class="tc p0">资料截止日期</th>
									<shiro:hasPermission name="price:project">
										<c:if test="${travelActivity.activityKind ne 6 and travelActivity.activityKind ne 7 and travelActivity.activityKind ne 10 }">
											<th class="tc" rowspan="2" width="8%">酒店房型</th>
										</c:if>
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
									<!-- t1t2  列表中增加quauq策略和供应价  start  djw -->
									<%--<c:if test="${flagList[s.index] == 1}">--%>
									<c:if test="${activityKind eq '2' and activity.isT1 == 1 }">
									<th colspan="3" class="t-th2" width="266px">QUAUQ策略</th>

									<th colspan="3" class="t-th2" width="266px">供应价（含服务费）</th>
									</c:if>
									<%--</c:if>--%>
									<!-- t1t2  列表中增加quauq价和供应价  end  djw -->
									<c:if test="${activityKind ne '10' }">
										<th class="tr" rowspan="2" width="4%">
											儿童最高人数
										</th>
										<th class="tr" rowspan="2" width="4%">
											特殊人群最高人数
										</th>
									</c:if>
									<th class="tr" rowspan="2" width="4%">
										订金<c:if test="${activityKind eq '10' }">/人</c:if>
									</th>
									<th class="tr" rowspan="2" width="4%">
										单房差<c:if test="${activityKind eq '10' }">/人</c:if>
									</th>
									<th class="tc" rowspan="2" width="3%">
										预收<c:if test="${activityKind eq '10' }">/间</c:if>
									</th>
									<th class="tc" rowspan="2" width="3%">
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
											<%--<c:if test="${flagList[s.index] == 1}">--%>
											<c:if test="${activityKind eq '2' and activity.isT1 == 1 }">
											<!-- quauq策略    djw -->
												<th class="tc">成人</th>
												<th class="tc">儿童</th>
												<th class="tc">特殊人群</th>
												<!-- 供应价     djw-->
												<th class="tc">成人</th>
												<th class="tc">儿童</th>
												<th class="tc">特殊人群</th>
											</c:if>
											<%--</c:if>--%>
										</c:otherwise>
									</c:choose>
								</tr>
							</thead> <!-- </table><div class="table_activity_scroll">
						<table class="table activitylist_bodyer_table table-mod2-group">  --><!-- 因bug#13372,列表页不对齐,解决方案-s  -->
							<c:forEach items="${activity.activityGroupList}" var="group" varStatus="s2">
							<tbody>
								<tr id="childtr${s.count}${s2.count}">
									<td  rowspan="1" width="6%"><span style="word-break: break-all;display: block;word-wrap: break-word;"id="groupId${group.id}">${group.groupCode}</span>
										<%--<c:choose> 
										     <c:when test="${fn:length(group.groupCode) > 10}"> 
										    	 <span title="${group.groupCode}"><c:out value="${fn:substring(group.groupCode, 0, 10)}......" /></span> 
										     </c:when> 
										     <c:otherwise> 
										      	<c:out value="${group.groupCode}" /> 
										     </c:otherwise>
										</c:choose>
										--%>
										<!--将团号变成可编辑的模式 -s-tgyTODO:这的正则有点问题-->
										<input type="text" id="${group.id}" maxlength="50" name="groupCode" value="${group.groupCode}"  src="${group.id}" onafterpaste="replaceStr(this)" onkeyup="validateLong(this)" style="display:none;"/>
										<!--将团号变成可编辑的模式 -e-tgy-->
										<input type="hidden" class="srcActivityId" value="${group.srcActivityId}"/>
									</td>
									<c:if test="${activityKind eq '2'}">
										<shiro:hasPermission name="calendarLoose:book:order">
										<td width="4%">
											<span>
											<input type="checkbox" disabled="disabled" name="recommend" id="recommend${s2.count }show" flag="show" <c:if test="${not empty group.recommend and group.recommend == 1 }">checked="checked"</c:if> >
											</span>
											<input type="checkbox" disabled="disabled" name="recommend" id="recommend${s2.count }mod" flag="mod" style="display:none;" <c:if test="${not empty group.recommend and group.recommend == 1 }">checked="checked"</c:if> >
										</td>
										</shiro:hasPermission>
									</c:if>
									<td width="5%" class="tc">
										<span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/></span>
										<input type="text" id="groupid${s.count}${s2.count}" name="groupid" value="${group.id}" style="display:none;" disabled="disabled"/>
										<input style="display:none;" disabled="disabled" type="text" id="groupOpenDate${s.count}${s2.count}" name="groupOpenDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/>" onClick="WdatePicker()" class="inputTxt"/>
									</td>
									<%--<input type="hidden" class="srcActivityId" value="${group.srcActivityId}"/>--%>
									<td width="5%" class="tc">
										<span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupCloseDate }"/></span>
										<input style="display:none;" disabled="disabled" type="text" id="groupCloseDate${s.count}${s2.count}" name="groupCloseDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupCloseDate}" />" onClick="WdatePicker({maxDate:takeOrderOpenDate('groupCloseDate${s.count}${s2.count}')})"  class="inputTxt"/>
									</td>
									<td width="3%">
										<span>${group.visaCountry }</span>
										<input style="display:none;" disabled="disabled" type="text" id="visaCountry${s.count}${s2.count}" name="visaCountry" value="${group.visaCountry}" />
									</td>
									<td width="5%" class="tc">
										<span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate }"/></span>
										<input style="display:none;" disabled="disabled" type="text" id="visaDate${s.count}${s2.count}" name="visaDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate }"/>" onClick="WdatePicker({maxDate:takeModVisaDate('visaDate${s.count}${s2.count}')})"/>
									</td>
									<!-- 299v2 酒店房型 -->
									<shiro:hasPermission name="price:project">
										<c:if test="${travelActivity.activityKind ne 6 and travelActivity.activityKind ne 7 and travelActivity.activityKind ne 10 }">
											<td class="tc hotelAndHouse" width="8%">
												<input type="hidden" name="groupHotel" value="${group.groupHotel}" />
												<input type="hidden" name="groupHouseType" value="${group.groupHouseType}" />
											</td>
										</c:if>
									</shiro:hasPermission>
									<c:if test="${activityKind eq '10' }">
										<td width="4%">
											<span>${fns:getDictLabel(group.spaceType, "cruise_type", "-") }</span>
										</td>
									</c:if>	
									<!-- 同行价开始 -->	
									<!-- 成人价 -->						
									<td width="${priceWidth}%" class="tr tdCurrency" <c:if test="${not empty activity.activityAirTicket }">title="包含${activity.activityAirTicket.settlementAdultPrice }机票价"</c:if> >
										<c:if test="${group.settlementAdultPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,0,'mark')}</span><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementAdultPrice }" /></c:if></span>
										<input style="display:none;" disabled="disabled" type="text" id="settlementAdultPrice${s.count}${s2.count}" name="settlementAdultPrice" 
											value="<c:if test="${group.settlementAdultPrice eq 0}"></c:if><c:if test="${group.settlementAdultPrice ne 0}"><fmt:formatNumber value="${group.settlementAdultPrice}" pattern="##0.00" /></c:if>" maxlength="14"    
											onkeyup="validNum(this,'${group.id}','${activity.id}')" onafterpaste="validNum(this,'${group.id}','${activity.id}')"  onblur="validNum(this,'${group.id}','${activity.id}')"/>
<!-- 										onkeyup="validatorFloat(this)" onafterpaste="validatorFloat(this)" -->
									</td>
									<!-- 儿童价 -->
									<td width="${priceWidth}%" class="tr tdCurrency" <c:if test="${not empty activity.activityAirTicket }">title="包含${activity.activityAirTicket.settlementcChildPrice }机票价"</c:if> >
										<c:if test="${group.settlementcChildPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,1,'mark')}</span><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementcChildPrice }" /></c:if></span>
										<input style="display:none;" disabled="disabled"  type="text" id="settlementcChildPrice${s.count}${s2.count}" name="settlementcChildPrice" 
											value="<c:if test="${group.settlementcChildPrice eq 0}"></c:if><c:if test="${group.settlementcChildPrice ne 0}"><fmt:formatNumber value="${group.settlementcChildPrice}" pattern="##0.00" /></c:if>" maxlength="14"  
											onkeyup="validNum(this,'${group.id}','${activity.id}')" onafterpaste="validNum(this,'${group.id}','${activity.id}')"  onblur="validNum(this,'${group.id}','${activity.id}')" />
									</td>
									<c:if test="${activityKind ne '10' }">
										<!-- 特殊人群价 -->
										<td width="${priceWidth}%" class="tr tdCurrency" <c:if test="${not empty activity.activityAirTicket }">title="包含${activity.activityAirTicket.settlementSpecialPrice }机票价"</c:if> >
											<c:if test="${group.settlementSpecialPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,2,'mark')}</span><span class="tdred" title="${activity.specialRemark }"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementSpecialPrice }" /></c:if></span>
											<input style="display:none;" disabled="disabled" type="text" id="settlementSpecialPrice${s.count}${s2.count}" name="settlementSpecialPrice" 
												value="<c:if test="${group.settlementSpecialPrice eq 0}"></c:if><c:if test="${group.settlementSpecialPrice ne 0}"><fmt:formatNumber value="${group.settlementSpecialPrice}" pattern="##0.00" /></c:if>" maxlength="14"  
												onkeyup="validNum(this,'${group.id}','${activity.id}')" onafterpaste="validNum(this,'${group.id}','${activity.id}')"  onblur="validNum(this,'${group.id}','${activity.id}')" />
										</td>
									</c:if>
									<!-- 同行价结束 -->
		<!-- 直客价开始 -->
		<c:if test="${activityKind eq '2' or activityKind eq '10'}">
		    <!-- wxw  added 20160107  处理 币种符号串位问题   -->
		    <c:choose>
		         <c:when test="${activityKind eq '2'}">
					   	<td width="${priceWidth}%" class="tr tdCurrency">
							<c:if test="${group.suggestAdultPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,3,'mark')}</span><span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestAdultPrice }" /></c:if></span>
							<input style="display:none;" disabled="disabled" type="text" id="suggestAdultPrice${s.count}${s2.count}" name="suggestAdultPrice" value="<c:if test="${group.suggestAdultPrice eq 0}"></c:if><c:if test="${group.suggestAdultPrice ne 0}"><fmt:formatNumber value="${group.suggestAdultPrice}" pattern="##0.00" /></c:if>" maxlength="14"  />
						</td>
						<td width="${priceWidth}%" class="tr tdCurrency">
							<c:if test="${group.suggestChildPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,4,'mark')}</span><span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestChildPrice }" /></c:if></span>
							<input style="display:none;" disabled="disabled" type="text" id="suggestChildPrice${s.count}${s2.count}" name="suggestChildPrice" value="<c:if test="${group.suggestChildPrice eq 0}"></c:if><c:if test="${group.suggestChildPrice ne 0}"><fmt:formatNumber value="${group.suggestChildPrice}" pattern="##0.00" /></c:if>" maxlength="14"  />
						</td>
		         </c:when>
		         <c:otherwise>
		                <td width="${priceWidth}%" class="tr tdCurrency">
							<c:if test="${group.suggestAdultPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,2,'mark')}</span><span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestAdultPrice }" /></c:if></span>
							<input style="display:none;" disabled="disabled" type="text" id="suggestAdultPrice${s.count}${s2.count}" name="suggestAdultPrice" value="<c:if test="${group.suggestAdultPrice eq 0}"></c:if><c:if test="${group.suggestAdultPrice ne 0}"><fmt:formatNumber value="${group.suggestAdultPrice}" pattern="##0.00" /></c:if>" maxlength="14"  />
						</td>
						<td width="${priceWidth}%" class="tr tdCurrency">
							<c:if test="${group.suggestChildPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,3,'mark')}</span><span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestChildPrice }" /></c:if></span>
							<input style="display:none;" disabled="disabled" type="text" id="suggestChildPrice${s.count}${s2.count}" name="suggestChildPrice" value="<c:if test="${group.suggestChildPrice eq 0}"></c:if><c:if test="${group.suggestChildPrice ne 0}"><fmt:formatNumber value="${group.suggestChildPrice}" pattern="##0.00" /></c:if>" maxlength="14"  />
						</td>
		         </c:otherwise>
		    </c:choose>
			<c:if test="${activityKind ne '10' }">
				<!-- 特殊人群 -->
				<td width="4%" class="tr tdCurrency">
					<c:if test="${group.suggestSpecialPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,5,'mark')}</span><span class="tdblue" title="${activity.specialRemark }"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestSpecialPrice }" /></c:if></span>
					<input style="display:none;" disabled="disabled" type="text" id="suggestSpecialPrice${s.count}${s2.count}" name="suggestSpecialPrice" value="<c:if test="${group.suggestSpecialPrice eq 0}"></c:if><c:if test="${group.suggestSpecialPrice ne 0}"><fmt:formatNumber value="${group.suggestSpecialPrice}" pattern="##0.00" /></c:if>" maxlength="14"  />
				</td>
			</c:if>
			<!-- 直客价结束 -->
			<%--<c:if test="${flagList[s.index] == 1}">--%>
			<!-- quauq -->
			<c:if test="${activityKind eq '2' and activity.isT1 == 1}">
			<!-- 成人价策略 -->
			<td width="4.0%" class="tr tdCurrency">
				<c:choose>
					<c:when test="${group.isT1 == 1 and not empty group.pricingStrategy.adultPricingStrategy }">${group.pricingStrategy.adultPricingStrategy }</c:when>
					<c:otherwise>-</c:otherwise>
				</c:choose>
			</td>
			<!-- 儿童价策略 -->
			<td width="4.0%" class="tr tdCurrency">
				<c:choose>
					<c:when test="${group.isT1 == 1 and not empty group.pricingStrategy.childrenPricingStrategy }">${group.pricingStrategy.childrenPricingStrategy }</c:when>
					<c:otherwise>-</c:otherwise>
				</c:choose>
			</td>
			<!-- 特殊人群价策略 -->
			<td width="4%" class="tr tdCurrency">
				<c:choose>
					<c:when test="${group.isT1 == 1 and not empty group.pricingStrategy.specialPricingStrategy }">${group.pricingStrategy.specialPricingStrategy }</c:when>
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
			<c:if test="${activityKind eq '2' and activity.isT1 == 1 }">
			<!-- 成人价 -->
			<td width="4.0%" class="tr tdCurrency">
				<%--<c:if test="${group.quauqAdultPrice ne 0}">--%>
				<c:choose>
					<c:when test="${group.isT1 == 1 and not empty group.adultRetailPrice }">
						<span class="rm">${fns:getCurrencyInfo(group.currencyType,0,'mark')}</span>
						<span class="tdblue"><fmt:formatNumber value="${group.adultRetailPrice }" pattern="#,##0.00" /></span>
					</c:when>
					<c:otherwise>-</c:otherwise>
				</c:choose>
				<%--</c:if>--%>
				
				<input style="display:none;" disabled="disabled" type="text" readonly="readonly"
					 id="supplyAdultPrice${s.count}${s2.count}" name="supplyAdultPrice"
					 value=<c:choose>
							   <c:when test="${group.quauqAdultPrice == '' or group.quauqAdultPrice == null}">""</c:when>
								 <c:otherwise>
									<fmt:formatNumber value="${group.adultRetailPrice }" pattern="##0.00" />
								 </c:otherwise>
							 </c:choose>
					maxlength="14" onkeyup="validNum(this)" onafterpaste="validNum(this)"  onblur="isMoney(this)"/>
			</td>
			<!-- 儿童价 -->
			<td width="4.0%" class="tr tdCurrency">
				<%--<c:if test="${group.quauqChildPrice ne 0}">--%>
				<c:choose>
					<c:when test="${group.isT1 == 1 and not empty group.childRetailPrice }">
						<span class="rm">${fns:getCurrencyInfo(group.currencyType,1,'mark')}</span>
						<span class="tdblue">
						<fmt:formatNumber value="${group.childRetailPrice }" pattern="#,##0.00" /></span>
					</c:when>
					<c:otherwise>-</c:otherwise>
				</c:choose>
				<%--</c:if>--%>
				<input style="display:none;" disabled="disabled" type="text"  readonly="readonly"
					id="supplyChildPrice${s.count}${s2.count}" name="supplyChildPrice"  
					value=<c:choose>
					   			<c:when test="${group.quauqChildPrice == '' or group.quauqChildPrice == null}">""</c:when>
							 	<c:otherwise>
							 		<fmt:formatNumber value="${group.childRetailPrice }" pattern="##0.00" />
							 	</c:otherwise>
							</c:choose>
					maxlength="14" onkeyup="validNum(this)" onafterpaste="validNum(this)"  onblur="isMoney(this)"/></input>
			</td>
			<!-- 特殊人群价 -->
			<td width="4%" class="tr tdCurrency">
				<%--<c:if test="${group.quauqSpecialPrice ne 0}">--%>
				<c:choose>
					<c:when test="${group.isT1 == 1 and not empty group.specialRetailPrice }">
						<span class="rm">${fns:getCurrencyInfo(group.currencyType,2,'mark')}</span>
						<span class="tdblue" title="特殊人群备注"><fmt:formatNumber value="${group.specialRetailPrice }" pattern="#,##0.00" /></span>
					</c:when>
					<c:otherwise>-</c:otherwise>

					<%--</c:if>--%>
				</c:choose>
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
			<%--</c:if>--%>
			
			<c:if test="${activityKind ne '10' }">
				<!-- 儿童最高人数 -->
				<td width="3%" class="tr tdCurrency">
					<span class="tdorange"><c:if test="${group.maxChildrenCount ne 0}"> ${group.maxChildrenCount } </c:if></span>
					<input style="display:none;" disabled="disabled" type="text" id="maxChildrenCount${s.count}${s2.count}" name="maxChildrenCount" value="<c:if test="${group.maxChildrenCount eq 0}"></c:if><c:if test="${group.maxChildrenCount ne 0}">${group.maxChildrenCount}</c:if>" onafterpaste="this.value.replace(/\D|^0.+/g,'')" maxlength="8" onKeyUp="this.value.replace(/\D|^0.+/g,'')"/>
				</td>
				<!-- 特殊人群数 -->
				<td width="3%" class="tr tdCurrency">
					<span class="tdorange"><c:if test="${group.maxPeopleCount ne 0}"> ${group.maxPeopleCount } </c:if></span>
					<input style="display:none;" disabled="disabled" type="text" id="maxPeopleCount${s.count}${s2.count}" name="maxPeopleCount" value="<c:if test="${group.maxPeopleCount eq 0}"></c:if><c:if test="${group.maxPeopleCount ne 0}">${group.maxPeopleCount}</c:if>" onafterpaste="this.value.replace(/\D/g,'')" maxlength="8" onKeyUp="this.value.replace(/\D/g,'')"/>
				</td>
				<!-- 定金 -->
				<c:if test="${fn:length(fn:split(group.currencyType,',')) gt 8 }">
				<td width="4%" class="tr tdCurrency">
					<c:if test="${group.payDeposit ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,12,'mark')}</span><span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.payDeposit }" /></c:if></span>
					<input style="display:none;" disabled="disabled" type="text" id="payDeposit${s.count}${s2.count}" name="payDeposit" value="<c:if test="${group.payDeposit eq 0}"></c:if><c:if test="${group.payDeposit ne 0}"><fmt:formatNumber value="${group.payDeposit}" pattern="#.##" /></c:if>" onafterpaste="this.value.replace(/\D/g,'')" maxlength="14" onKeyUp="this.value.replace(/\D/g,'')"/>
				</td>
				<!-- 单房差 -->
				<td width="4%" class="tr tdCurrency">
					<c:if test="${group.singleDiff ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,13,'mark')}</span><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.singleDiff }" /></c:if></span>
					<input style="display:none;" disabled="disabled" type="text" id="singleDiff${s.count}${s2.count}" name="singleDiff" value="<c:if test="${group.singleDiff eq 0}"></c:if><c:if test="${group.singleDiff ne 0}"><fmt:formatNumber value="${group.singleDiff}" pattern="#.##" /></c:if>" onafterpaste="this.value.replace(/\D/g,'')" maxlength="14" onKeyUp="this.value.replace(/\D/g,'')"/>
				</td>
				</c:if>
				<!-- 定金 -->
				<c:if test="${fn:length(fn:split(group.currencyType,',')) le 8 }">
				<td width="4%" class="tr tdCurrency">
					<c:if test="${group.payDeposit ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,6,'mark')}</span><span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.payDeposit }" /></c:if></span>
					<input style="display:none;" disabled="disabled" type="text" id="payDeposit${s.count}${s2.count}" name="payDeposit" value="<c:if test="${group.payDeposit eq 0}"></c:if><c:if test="${group.payDeposit ne 0}"><fmt:formatNumber value="${group.payDeposit}" pattern="#.##" /></c:if>" onafterpaste="this.value.replace(/\D/g,'')" maxlength="14" onKeyUp="this.value.replace(/\D/g,'')"/>
				</td>
				<!-- 单房差 -->
				<td width="4%" class="tr tdCurrency">
					<c:if test="${group.singleDiff ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,7,'mark')}</span><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.singleDiff }" /></c:if></span>
					<input style="display:none;" disabled="disabled" type="text" id="singleDiff${s.count}${s2.count}" name="singleDiff" value="<c:if test="${group.singleDiff eq 0}"></c:if><c:if test="${group.singleDiff ne 0}"><fmt:formatNumber value="${group.singleDiff}" pattern="#.##" /></c:if>" onafterpaste="this.value.replace(/\D/g,'')" maxlength="14" onKeyUp="this.value.replace(/\D/g,'')"/>
				</td>
				</c:if>
			</c:if>
			<c:if test="${activityKind eq '10' }">
			<!-- 游轮不显示特殊人群数 -->
<!-- 			    <td width="5%" class="tr tdCurrency"> -->
<%-- 					<span class="tdorange"><c:if test="${group.maxPeopleCount ne 0}">${group.payDeposit }</c:if></span> --%>
<%-- 					<input style="display:none;" disabled="disabled" type="text" id="maxPeopleCount${s.count}${s2.count}" name="maxPeopleCount" value="<c:if test="${group.maxPeopleCount eq 0}"></c:if><c:if test="${group.maxPeopleCount ne 0}">${group.maxPeopleCount}</c:if>" onafterpaste="this.value.replace(/\D/g,'')" maxlength="8" onKeyUp="this.value.replace(/\D/g,'')"/> --%>
<!-- 				</td> -->
				<!-- 定金 -->
				<td width="5%" class="tr tdCurrency">
					<c:if test="${group.payDeposit ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,4,'mark')}</span><span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.payDeposit }" /></c:if></span>
					<input style="display:none;" disabled="disabled" type="text" id="payDeposit${s.count}${s2.count}" name="payDeposit" value="<c:if test="${group.payDeposit eq 0}"></c:if><c:if test="${group.payDeposit ne 0}"><fmt:formatNumber value="${group.payDeposit}" pattern="#.##" /></c:if>" onafterpaste="this.value.replace(/\D/g,'')" maxlength="14" onKeyUp="this.value.replace(/\D/g,'')"/>
				</td>
				<!-- 单房差 -->
				<td width="5%" class="tr tdCurrency">
					<c:if test="${group.singleDiff ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,5,'mark')}</span><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.singleDiff }" /></c:if></span>
					<input style="display:none;" disabled="disabled" type="text" id="singleDiff${s.count}${s2.count}" name="singleDiff" value="<c:if test="${group.singleDiff eq 0}"></c:if><c:if test="${group.singleDiff ne 0}"><fmt:formatNumber value="${group.singleDiff}" pattern="#.##" /></c:if>" onafterpaste="this.value.replace(/\D/g,'')" maxlength="14" onKeyUp="this.value.replace(/\D/g,'')"/>
				</td>
			</c:if>
		</c:if>
									<c:if test="${activityKind ne '2' and activityKind ne '10'}">
										<!-- 儿童最高人数 -->
										<td width="5%" class="tr tdCurrency">
											<span class="tdorange"><c:if test="${group.maxChildrenCount ne 0}">${group.maxChildrenCount } </c:if></span>
											<input style="display:none;" disabled="disabled" type="text" id="maxChildrenCount${s.count}${s2.count}" onafterpaste="this.value=this.value.replace(/\D|^0.+/g,'')" onkeyup="this.value=this.value.replace(/\D|^0.+/g,'')" name="maxChildrenCount" value="<c:if test="${group.maxChildrenCount eq 0}"></c:if><c:if test="${group.maxChildrenCount ne 0}">${group.maxChildrenCount}</c:if>" maxlength="8" />
										</td>
										<!-- 特殊人群数 -->
										<td width="5%" class="tr tdCurrency">
											<span class="tdorange"><c:if test="${group.maxPeopleCount ne 0}">${group.maxPeopleCount } </c:if></span>
											<input style="display:none;" disabled="disabled" type="text" id="maxPeopleCount${s.count}${s2.count}" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" name="maxPeopleCount" value="<c:if test="${group.maxPeopleCount eq 0}"></c:if><c:if test="${group.maxPeopleCount ne 0}">${group.maxPeopleCount}</c:if>" maxlength="8" />
										</td>
										<!-- 定金 -->
										<td width="5%" class="tr tdCurrency">
											<c:if test="${group.payDeposit ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,3,'mark')}</span><span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.payDeposit }" /></c:if></span>
											<input style="display:none;" disabled="disabled" type="text" id="payDeposit${s.count}${s2.count}" name="payDeposit" value="<c:if test="${group.payDeposit eq 0}"></c:if><c:if test="${group.payDeposit ne 0}"><fmt:formatNumber value="${group.payDeposit}" pattern="#.##" /></c:if>" maxlength="14" />
										</td>
										<!-- 单房差 -->
										<td width="5%" class="tr tdCurrency">
											<c:if test="${group.singleDiff ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,4,'mark')}</span><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.singleDiff }" /></c:if></span>
											<input style="display:none;" disabled="disabled" type="text" id="singleDiff${s.count}${s2.count}" name="singleDiff" value="<c:if test="${group.singleDiff eq 0}"></c:if><c:if test="${group.singleDiff ne 0}"><fmt:formatNumber value="${group.singleDiff}" pattern="#.##" /></c:if>" maxlength="14" />
										</td>
									</c:if>
									<!-- 预收 -->
									<td width="3%" class="tr">
										<span>${group.planPosition }</span>
										<input style="display:none;" disabled="disabled" type="text" id="planPosition${s.count}${s2.count}" name="planPosition" value="${group.planPosition}"  onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="3" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
									</td>
									<!-- 余位 -->
									<td width="3%" class="tr">
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
										 <%--<shiro:hasPermission name="${shiroType}Product:operation:groupEdit">--%>
											<%--<a href="javascript:void(0)"  id="modbtn${s.count}${s2.count}" --%>
											<%--onClick="modgroup(${group.id},${activity.id},'#groupid${s.count}${s2.count}','#savebtn${s.count}${s2.count}','#delbtn${s.count}${s2.count}','#cancelbtn${s.count}${s2.count}',this)">修改</a>--%>
										<%--</shiro:hasPermission>--%>
										<shiro:hasPermission name="${shiroType}Product:operation:groupEdit">
											<a href="javascript:void(0)"  id="savebtn${s.count}${s2.count}" style="display:none;" 
											onClick="checkVisaCountrylen(${s.count},${s2.count});savegroup(${activity.id},'#modbtn${s.count}${s2.count}','#delbtn${s.count}${s2.count}','#cancelbtn${s.count}${s2.count}',this,'#childform${s.count}','#childtr${s.count}${s2.count}','${ctx}','${chargeRate}')">确认</a>
										</shiro:hasPermission>										
										<shiro:hasPermission name="${shiroType}Product:operation:groupDelete">
											<a href="javascript:void(0)" id="delbtn${s.count}${s2.count}" onClick="return confirmxCopy('要删除该产品的此团期吗？',${group.id},${activity.id },this,'child${s.count}')" ><span>删除</span></a>
										</shiro:hasPermission>
										<shiro:hasPermission name="${shiroType}Product:operation:groupEdit">												
											<a style="color:#ec0203;display:none;" id="cancelbtn${s.count}${s2.count}" href="javascript:void(0)" 
											onClick="cancelgroup('#modbtn${s.count}${s2.count}','#savebtn${s.count}${s2.count}','#delbtn${s.count}${s2.count}',this)">取消</a>
										</shiro:hasPermission>
										<!-- add by lvwenchao 2016-1-7 21:33:41 -->  			
                                    	<c:choose>
											<c:when test="${not empty group.groupRemark }">
												<a class="expandNotes" href="javascript:void(0)"> 展开备注 </a>
												<em class="groupNoteTipImg"></em>
											</c:when>	
											<c:otherwise>
												<a class="expandNotes" href="javascript:void(0)"> 展开备注 </a> 
												<em class="groupNoteTipImg" style="display:none;"></em>
											</c:otherwise>
										</c:choose>
										<shiro:hasPermission name="price:project">
											<c:if test="${not empty group.priceJson and activityKind != 10}">
												<a class="expandPriceJson" href="javascript:void(0)" onclick="expandPriceJson(this, ${activityKind})"
												   data='${group.priceJson}'> 展开价格方案 </a>
											</c:if>
										</shiro:hasPermission>
									</td>
								</tr>
								<tr class="groupNoteCol">
								   <!-- 懿洋假期多一列发票税 -->
                                	<td 
                                	<c:if test="${fns:getUser().company.uuid eq 'f5c8969ee6b845bcbeb5c2b40bac3a23' or fns:getUser().company.id==69}">
                                		colspan="25"
                                	</c:if>
                                	<c:if test="${fns:getUser().company.uuid ne 'f5c8969ee6b845bcbeb5c2b40bac3a23' and fns:getUser().company.id !=69}">
                                		colspan="25"
                                	</c:if>
                                	>
	                                   	<div>
	                                    	备注:<span class="groupNoteContent" id="groupNoteContent${s.count}${s2.count}" name="groupNoteContent">${group.groupRemark}</span>
	                                    </div>                          
	                                    <div class="remarks-containers display-none">备注:<input type="text" class="groupNotes padding-none" name="groupNote"/>
                                        </div>
                                   </td>
                               </tr>

								<!--299-产品模块-单团产品列表-start-->
								<tr class="pricePlanContainer" style="display:none;">
									<td colspan="25">
										<table name="pricePlanTable" id="pricePlanTable"
											   class="table activitylist_bodyer_table border-table-spread"
											   style="margin: 0 auto">
											<thead>
											<tr>
												<th rowspan="2" class="tc" style="width: 50px;">序号</th>
												<th rowspan="2" class="tc" style="width: 500px">
													价格方案
												</th>
												<th colspan="3" class="tc t-th2">同行价</th>
												<c:if test="${activityKind == '2'}">
													<th colspan="3" class="tc t-th2">直客价</th>
												</c:if>
												<th rowspan="2" class="tc">备注</th>
											</tr>
											<tr>
												<th class="tc">成人</th>
												<th class="tc">儿童</th>
												<th class="tc">特殊人群</th>
												<c:if test="${activityKind == '2'}">
													<th class="tc">成人</th>
													<th class="tc">儿童</th>
													<th class="tc">特殊人群</th>
												</c:if>
												<!-- 修复bug15206时，注释一下代码 -->
												<!-- <th class="tc">成人</th>
												<th class="tc">儿童</th>
												<th class="tc">特殊人群</th> -->
											</tr>
											</thead>
											<tbody>

											</tbody>
										</table>
									</td>
								</tr>
								<!--299-产品模块-单团产品列表-end-->
							</tbody>
							</c:forEach>
						</table><!-- </div> --> <!-- 因bug#13372,列表页不对齐,解决方案-e  -->
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
			</div>
		</c:if>
	<form id="exportForm" action="${ctx}/activity/manager/exportExcel" method="post">
		<input type="hidden" id="groupId" name="groupId">
		<input type="hidden" id="groupCode" name="groupCode">
	</form>   
	</body>
</html>