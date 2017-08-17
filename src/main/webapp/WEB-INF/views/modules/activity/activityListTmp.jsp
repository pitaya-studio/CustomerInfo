<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>草稿中产品</title>
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
		$(function() {
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
		var activityIds = "";
		$(document).ready(function() {
		
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
			var searchFormselect = $("#searchForm").find("select");
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
			$("#searchForm").attr("action","${ctx}/activity/manager/list/${travelActivity.activityStatus }?newflag=newflag");
			$("#searchForm").submit();
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
	    	$(obj).parent().parent().find("span").hide();
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
					if(activityIds.indexOf(a.value) < 0){					
						activityIds = activityIds + a.value+",";
					}
  				});
			}				
			else{
				$("input[name='allChk']").removeAttr("checked");
				$("input[name='activityId']").removeAttr("checked");
				$("input[name='activityId']").each(function(i,a){
					if(activityIds.indexOf(a.value) >= 0){					
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
		
		
		function onLineProduct(activityId){
            $.jBox.confirm("确定要上架该产品吗？", "提示", function(v, h, f){
                if (v == 'ok') {
                       $("#searchForm").attr("action","${ctx}/activity/manager/batchreleaseTmp/"+activityId + "/${activityKind}");
                       $("#searchForm").submit();
                        }else if (v == 'cancel'){
                            
                        }
                    });
        }

		function getDepartment(departmentId) {
			$("#departmentId").val(departmentId);
			$("#searchForm").submit();
		}
	</script>
</head>
<body>
    <page:applyDecorator name="activity_op_head">
        <page:param name="current">tmp</page:param>
    </page:applyDecorator>
    <!-- 签证公告展示 -->
	<%@ include file="/WEB-INF/views/modules/visa/visaPublicBulletinMarquee.jsp"%>
    <p class="main-right-topbutt"><a class="primary" href="${ctx}/activity/manager/form">发布新产品</a></p>
  <div class="activitylist_bodyer_right_team_co_bgcolor">
<!--   <div class="activitylist_bodyer_right_team_co_bgcolor">-->
	<form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/activity/manager/list/${travelActivity.activityStatus }?newflag=newflag" method="post" >
		<input id="activityStatus" type="hidden" name="activityStatus" value="${travelActivity.activityStatus }"/>
		<input id="activityIds" type="hidden" name="activityIds" value="${activityIds }"/>
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<input id="departmentId" name="departmentId" type="hidden" value="${departmentId }" />
		
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr wpr20">
			<label>搜索：</label><input style="width:260px" class="txtPro inputTxt inquiry_left_text" id="wholeSalerKey" name="wholeSalerKey" value="${travelActivity.acitivityName }"/>
	         	<span class="ipt-tips" style="display: block;">输入产品名称、团号，支持模糊匹配</span>
         	</div>
	      <div class="form_submit">
				<input class="btn btn-primary" type="submit" value="搜索"/>
		</div>
		<div class="zksx">筛选</div>
			<div class="ydxbd" >
			
			<div class="activitylist_bodyer_right_team_co3">
	            <div class="activitylist_team_co3_text">销售：</div>
	            <input id="estimatePriceRecordUserName" name="estimatePriceRecordUserName" class="inputTxt"  value="${travelActivity.estimatePriceRecord.userName }"> 
	        </div>
			<div class="activitylist_bodyer_right_team_co1">
			    <div class="activitylist_team_co3_text">出发地：</div>
			    <form:select id="fromArea" path="fromArea" itemValue="key" itemLabel="value" >      
	                <form:option value="" >不限</form:option>
	                <form:options items="${fromAreas}"/>
            	</form:select>
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
			<div class="activitylist_bodyer_right_team_co3">
	            <div class="activitylist_team_co3_text">计调：</div>
	            <input id="createName" name="createName" class="inputTxt"  value="${travelActivity.createBy.name }"> 
	        </div>
			<div class="activitylist_bodyer_right_team_co3">
	            <div class="activitylist_team_co3_text">领队：</div>
	            <input id="groupLead" name="groupLead" class="inputTxt"  value="${travelActivity.groupLead }"> 
	        </div>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">航空公司：</div>
				<form:select id="trafficName" path="trafficName" itemValue="key" itemLabel="value" >      
	                <form:option value="" >不限</form:option>
	                <form:options items="${trafficNames}"/>
            	</form:select>
            </div>
			<div class="activitylist_bodyer_right_team_co2">
				<label>同行价格：</label><input id="settlementAdultPriceStart" class="rmb inputTxt" name="settlementAdultPriceStart" value="${settlementAdultPriceStart }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
				<span style="font-size:12px;font-family:'宋体';"> 至</span>
				<input id="settlementAdultPriceEnd" class="rmb inputTxt" name="settlementAdultPriceEnd" value="${settlementAdultPriceEnd }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
         	</div>
         	<div class="kong"></div>
			<div class="activitylist_bodyer_right_team_co3">
         		<label for="spinner"  class="fl" style="line-height:28px;">行程天数：</label>
				<input id="activityDuration" class="spinner" maxlength="3" name="activityDuration" value="${param.activityDuration }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
			</div>
			
<%--不使用的筛选条件====================			--%>
<%--                <div class="activitylist_bodyer_right_team_co3">--%>
<%--                    <div class="activitylist_team_co3_text">旅游类型：</div><form:select path="travelTypeId" itemValue="key" itemLabel="value">--%>
<%--               <form:option value="">不限</form:option>--%>
<%--               <form:options items="${travelTypes}" />--%>
<%--            </form:select>--%>
<%--          </div>--%>
<%--    		--%>
<%--              --%>
<%--      		--%>
<%--              <div class="activitylist_bodyer_right_team_co3">--%>
<%--            <div class="activitylist_team_co3_text">产品系列：</div><form:select path="activityLevelId" itemValue="key" itemLabel="value">--%>
<%--               <form:option value="">不限</form:option>--%>
<%--               <form:options items="${productLevels}" />--%>
<%--            </form:select>--%>
<%--            </div>--%>
<%----%>
<%--            </div>--%>
<%--			<div class="activitylist_bodyer_right_team_co1">--%>
<%--         	<div class="activitylist_team_co3_text">产品类型：</div><form:select path="activityTypeId" itemValue="key" itemLabel="value">--%>
<%--               <form:option value="">不限</form:option>--%>
<%--               <form:options items="${productTypes}" />--%>
<%--            </form:select>--%>
<%--            </div>--%>
<%--   		<div class="activitylist_bodyer_right_team_co3">--%>
<%--            <div class="activitylist_team_co3_text">产品编号：</div>--%>
<%--           <input id="activitySerNum" name="activitySerNum" class="inputTxt"  value="${travelActivity.activitySerNum }"> --%>
<%--        </div>--%>
<%--	      <div class="kong"></div>--%>
<%----%>
<%--============================--%>
			</div>
	      <div class="kong"></div>
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
			            <c:if test="${activityKind eq '2'}">
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
				<th class="table_borderLeftN" width="4%">全选<br/><input name="allChk" type="checkbox" onclick="checkall(this)"/></th>
<%--				<th width="10%">产品编号</th>--%>
				<th width="14%">产品名称</th>
<%--				<th width="8%">产品系列</th>--%>
				<th width="8%">出发地</th>
				<th width="6%">航空</th>
				<th width="8%">签证</th>
				<th width="16%">最近出团日期</th>
				<th width="10%">成人同行价</th>
				<c:if test="${activityKind eq '2'}">
					<th width="10%">成人直客价</th>
				</c:if>
				<th width="6%">操作</th>
			</tr>
		</thead>
		<tbody>
		
			<c:forEach items="${page.list}" var="activity" varStatus="s">
			<c:set var="groupsize" value="${fn:length(activity.activityGroups)}"></c:set>
				<tr id="parent${s.count}">
					<td class="table_borderLeftN"><input type="checkbox" name="activityId" value="${activity.id }" <c:if test="${fn:contains(activityIds,fn:trim(activity.id))}">checked="checked"</c:if> onclick="idcheckchg(this)"/><br/><br/></td>
<%--					<td>--%>
<%--						<c:choose> --%>
<%--						     <c:when test="${fn:length(activity.activitySerNum) > 20}"> --%>
<%--						   		<a style="text-decoration: none; color:inherit; cursor:default;" title="${activity.activitySerNum}"><c:out value="${fn:substring(activity.activitySerNum, 0, 20)}......" /></a> --%>
<%--						     </c:when> --%>
<%--						     <c:otherwise> --%>
<%--						      	<c:out value="${activity.activitySerNum}" /> --%>
<%--						     </c:otherwise>--%>
<%--						</c:choose>--%>
<%--					</td>--%>
					<td class="activity_name_td">
                        <a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${activity.id}')">${activity.acitivityName}</a>
<%--                        <c:choose>--%>
<%--                            <c:when test="${not empty activity.activityAirTicket and activity.activityAirTicket.intermodalStrategies[0].type == 1}">--%>
<%--                                <span class="lianyun_name">全国联运</span>--%>
<%--                            </c:when>--%>
<%--                            <c:when test="${not empty activity.activityAirTicket and activity.activityAirTicket.intermodalStrategies[0].type == 1}">--%>
<%--                                <span class="lianyun_name">分区联运</span>--%>
<%--                            </c:when>--%>
<%--                            <c:otherwise>--%>
<%--                                <span class="lianyun_name">无联运</span>--%>
<%--                            </c:otherwise>--%>
<%--                        </c:choose>--%>
                    </td>
<%--					<td>${activity.activityLevelName}</td>--%>
					<td>
                        <c:choose>
                            <c:when test="${not empty activity.activityAirTicket and activity.activityAirTicket.intermodalStrategies[0].type == 1}">
                                <span class="lianyun_name">全国联运</span>
                            </c:when>
                            <c:when test="${not empty activity.activityAirTicket and activity.activityAirTicket.intermodalStrategies[0].type == 2}">
                                <span class="lianyun_name">分区联运</span>
                            </c:when>
                            <c:otherwise>
                                ${activity.fromAreaName}
                            </c:otherwise>
                        </c:choose>
					</td>
<%--               		<td>${activity.targetAreaNames}</td>--%>
                    <td><label class="qtip" title="${activity.trafficNameDesc}">${activity.activityAirTicket.airlines}</label></td>
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
					<td id="groupdate${activity.id }" align="center" >	
					<div id="truedate" <c:if test="${groupsize ne 0 }">style="display:block;"</c:if><c:if test="${groupsize == 0 }">style="display:none;"</c:if>>
						<span>${activity.groupOpenDate}至${activity.groupCloseDate}</span><br>
						<a id="close${s.count}" href="javascript:void(0)" class="team_a_click" onClick="expand('#child${s.count}',this,${activity.id })" onMouseenter="if($(this).html()=='全部团期'){$(this).html('展开全部团期')}" onMouseleave="if($(this).html()=='展开全部团期'){$(this).html('全部团期')}">全部团期</a>
					</div>
					
					<div id="falsedate" <c:if test="${groupsize ne 0 }">style="display:none;"</c:if><c:if test="${groupsize == 0 }">style="display:block;"</c:if>>					
						日期待定
					</div>				                       
					</td>
					<td id="settleadultprice${activity.id }" class="tr"><c:if test="${activity.settlementAdultPrice==0}">价格待定</c:if><c:if test="${activity.settlementAdultPrice>0}">¥<span class="tdred fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${activity.settlementAdultPrice}" /></span>起</c:if></td>
					<c:if test="${activityKind eq '2'}">
						<td id="suggestadultprice${activity.id }" class="tr"><c:if test="${activity.suggestAdultPrice==0}">价格待定</c:if><c:if test="${activity.suggestAdultPrice>0}">¥<span class="tdblue fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${activity.suggestAdultPrice}" /></span>起</c:if></td>
					</c:if>
					<td class="p0">
                    	<dl class="handle">
                    		<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
                    		<dd class="">
               				<p>
               					<span></span>
								<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${activity.id}')">详情</a><br/>
								<shiro:hasPermission name="product:manager:edit"><a href="javascript:void(0)" onClick="productModify(${activity.id})">修改</a></shiro:hasPermission><br/>
								<a href="javascript:void(0)" onClick="onLineProduct(${activity.id})">上架</a><br/>
								<shiro:hasPermission name="product:manager:delete"><a href="javascript:void(0)" onClick="return confirmxDel('要删除该产品吗？', ${activity.id})">删除</a></shiro:hasPermission><br/>
							</p>
							</dd>
						</dl>
					</td>
										
				</tr>
				<tr id="child${s.count}" style="display:none" class="activity_team_top1">
                      <c:if test="${activityKind eq '2'}">
						<td colspan="10" class="team_top" style="background-color:#d1e5f5;">
					  </c:if>
					  <c:if test="${activityKind ne '2'}">
						<td colspan="9" class="team_top" style="background-color:#d1e5f5;">
					  </c:if>	
                      <form id="childform${s.count}"  style="margin: 0px">
						<table id="teamTable" class="table activitylist_bodyer_table table-mod2-group" style="margin:0 auto;">
							<thead>
								<tr>
									<th rowspan="2" width="9%">团号</th>
									<th rowspan="2" width="8%">出团日期</th>
							        <th rowspan="2" width="8%">截团日期</th>
									<th rowspan="2" width="7%">签证国家</th>
									<th rowspan="2" width="8%" class="p0">资料截止日期</th>
									<th colspan="3" class="t-th2" width="15%">同行价</th>
									<th colspan="3" class="t-th2" width="15%">直客价</th>
									<th rowspan="2" width="6%">订金</th>
									<th rowspan="2" width="5%">单房差</th>
									<th rowspan="2" width="4%">预收</th>
									<th rowspan="2" width="4%">余位</th>
									<th rowspan="2" style="display:none" class="soldPayPosition"  width="4%">切位</th>
									<th rowspan="2" width="7%">操作</th>
								</tr>
								<tr>
									<th>成&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;人</th>
									<th>儿&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;童</th>
									<th>特殊人群</th>
									<th>成&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;人</th>
									<th>儿&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;童</th>
									<th>特殊人群</th>
								</tr>
							</thead>
							</table>
						  <div class="table_activity_scroll">
							<table class="table activitylist_bodyer_table table-mod2-group">
							<tbody>
							<c:forEach items="${activity.activityGroups}" var="group" varStatus="s2">												
								<tr id="childtr${s.count}${s2.count}">
								
									<td width="9%"><span>
										<%--<c:choose> 
										     <c:when test="${fn:length(group.groupCode) > 10}"> 
										    	 <span title="${group.groupCode}"><c:out value="${fn:substring(group.groupCode, 0, 10)}......" /></span> 
										     </c:when> 
										     <c:otherwise> 
										      	<c:out value="${group.groupCode}" /> 
										     </c:otherwise>
										</c:choose>
										--%>${group.groupCode}</span>
										<input style="display:none;" disabled="disabled" type="text" id="groupCode${s.count}${s2.count}" name="groupCode" value="${group.groupCode}" onafterpaste="this.value=this.value.replace(/[^\u4e00-\u9fa5\w\(\)\（\）\-]/g,'')" onKeyUp="this.value=this.value.replace(/[^\u4e00-\u9fa5\w\(\)\（\）\-]/g,'')"/>
									</td>
									<td width="8%" class="tc">
										<span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/></span>
										<input type="text" id="groupid${s.count}${s2.count}" name="groupid" value="${group.id}" style="display:none;" disabled="disabled"/>
										<input style="display:none;" disabled="disabled" type="text" id="groupOpenDate${s.count}${s2.count}" name="groupOpenDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/>" onClick="WdatePicker({minDate:getCurDate()})" class="inputTxt"/>
									</td>
									<td width="8%" class="tc">
										<span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupCloseDate }"/></span>
										<input style="display:none;" disabled="disabled" type="text" id="groupCloseDate${s.count}${s2.count}" name="groupCloseDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupCloseDate}" />" onClick="WdatePicker({maxDate:takeOrderOpenDate('groupCloseDate${s.count}${s2.count}'),minDate:getCurDate()})"  class="inputTxt"/>
									</td>
									<td width="7%">
										<span>${group.visaCountry }</span>
										<input style="display:none;" disabled="disabled" type="text" id="visaCountry${s.count}${s2.count}" name="visaCountry" value="${group.visaCountry}" />
									</td>
									<td width="8%" class="tc">
										<span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate }"/></span>
										<input style="display:none;" disabled="disabled" type="text" id="visaDate${s.count}${s2.count}" name="visaDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate }"/>" onClick="WdatePicker({maxDate:takeModVisaDate('visaDate${s.count}${s2.count}'),minDate:getCurDate()})"/>
									</td>									
									<td width="5%" class="tr">
										<c:if test="${group.settlementAdultPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,0,'mark')}</span><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementAdultPrice }" /></c:if></span>
										<input style="display:none;" disabled="disabled" type="text" id="settlementAdultPrice${s.count}${s2.count}" name="settlementAdultPrice" value="<c:if test="${group.settlementAdultPrice eq 0}"></c:if><c:if test="${group.settlementAdultPrice ne 0}">${group.settlementAdultPrice}</c:if>" maxlength="6" onafterpaste="this.value=this.value.replace(/\D/g,'')" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
									</td>
									<td width="5%" class="tr">
										<c:if test="${group.settlementcChildPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,1,'mark')}</span><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementcChildPrice }" /></c:if></span>
										<input style="display:none;" disabled="disabled" type="text" id="settlementcChildPrice${s.count}${s2.count}" name="settlementcChildPrice" value="<c:if test="${group.settlementcChildPrice eq 0}"></c:if><c:if test="${group.settlementcChildPrice ne 0}">${group.settlementcChildPrice}</c:if>" maxlength="6" onafterpaste="this.value=this.value.replace(/\D/g,'')" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
									</td>
									<td width="5%" class="tr">
										<c:if test="${group.settlementSpecialPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,2,'mark')}</span><span class="tdred" title="${activity.specialRemark }"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementSpecialPrice }" /></c:if></span>
										<input style="display:none;" disabled="disabled" type="text" id="settlementSpecialPrice${s.count}${s2.count}" name="settlementSpecialPrice" value="<c:if test="${group.settlementSpecialPrice eq 0}"></c:if><c:if test="${group.settlementSpecialPrice ne 0}">${group.settlementSpecialPrice}</c:if>" maxlength="6" onafterpaste="this.value=this.value.replace(/\D/g,'')" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
									</td>
									<c:if test="${activityKind eq '2'}">
										<td width="5%" class="tr">
											<c:if test="${group.suggestAdultPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,3,'mark')}</span><span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestAdultPrice }" /></c:if></span>
											<input style="display:none;" disabled="disabled" type="text" id="suggestAdultPrice${s.count}${s2.count}" name="suggestAdultPrice" value="<c:if test="${group.suggestAdultPrice eq 0}"></c:if><c:if test="${group.suggestAdultPrice ne 0}">${group.suggestAdultPrice}</c:if>" maxlength="6" onafterpaste="this.value=this.value.replace(/\D/g,'')" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
										</td>
										<td width="5%" class="tr">
											<c:if test="${group.suggestChildPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,4,'mark')}</span><span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestChildPrice }" /></c:if></span>
											<input style="display:none;" disabled="disabled" type="text" id="suggestChildPrice${s.count}${s2.count}" name="suggestChildPrice" value="<c:if test="${group.suggestChildPrice eq 0}"></c:if><c:if test="${group.suggestChildPrice ne 0}">${group.suggestChildPrice}</c:if>" maxlength="6" onafterpaste="this.value=this.value.replace(/\D/g,'')" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
										</td>
										<td width="5%" class="tr">
											<c:if test="${group.suggestSpecialPrice ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,5,'mark')}</span><span class="tdblue" title="${activity.specialRemark }"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestSpecialPrice }" /></c:if></span>
											<input style="display:none;" disabled="disabled" type="text" id="suggestSpecialPrice${s.count}${s2.count}" name="suggestSpecialPrice" value="<c:if test="${group.suggestSpecialPrice eq 0}"></c:if><c:if test="${group.suggestSpecialPrice ne 0}">${group.suggestSpecialPrice}</c:if>" maxlength="6" onafterpaste="this.value=this.value.replace(/\D/g,'')" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
										</td>
										<td width="6%" class="tr">
											<c:if test="${group.payDeposit ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,6,'mark')}</span><span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.payDeposit }" /></c:if></span>
											<input style="display:none;" disabled="disabled" type="text" id="payDeposit${s.count}${s2.count}" name="payDeposit" value="<c:if test="${group.payDeposit eq 0}"></c:if><c:if test="${group.payDeposit ne 0}">${group.payDeposit}</c:if>" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="6" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
										</td>
										<td width="5%" class="tr">
											<c:if test="${group.singleDiff ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,7,'mark')}</span><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.singleDiff }" /></c:if></span>
											<input style="display:none;" disabled="disabled" type="text" id="singleDiff${s.count}${s2.count}" name="singleDiff" value="<c:if test="${group.singleDiff eq 0}"></c:if><c:if test="${group.singleDiff ne 0}">${group.singleDiff}</c:if>" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="6" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
										</td>
									</c:if>
									<c:if test="${activityKind ne '2'}">
										<td width="6%" class="tr">
											<c:if test="${group.payDeposit ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,3,'mark')}</span><span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.payDeposit }" /></c:if></span>
											<input style="display:none;" disabled="disabled" type="text" id="payDeposit${s.count}${s2.count}" name="payDeposit" value="<c:if test="${group.payDeposit eq 0}"></c:if><c:if test="${group.payDeposit ne 0}">${group.payDeposit}</c:if>" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="6" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
										</td>
										<td width="5%" class="tr">
											<c:if test="${group.singleDiff ne 0}"><span class="rm">${fns:getCurrencyInfo(group.currencyType,4,'mark')}</span><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.singleDiff }" /></c:if></span>
											<input style="display:none;" disabled="disabled" type="text" id="singleDiff${s.count}${s2.count}" name="singleDiff" value="<c:if test="${group.singleDiff eq 0}"></c:if><c:if test="${group.singleDiff ne 0}">${group.singleDiff}</c:if>" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="6" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
										</td>
									</c:if>
									<td width="4%" class="tr">
										<span>${group.planPosition }</span>
										<input style="display:none;" disabled="disabled" type="text" id="planPosition${s.count}${s2.count}" name="planPosition" value="${group.planPosition}"  onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="3" onKeyUp="this.value=this.value.replace(/\D/g,'')"  onChange="comparePositionMod(this)"/>
									</td>
									<td width="4%" class="tr">
										<span class="tdred">${group.freePosition }</span>
										<input style="display:none;" disabled="disabled" type="text" id="freePosition${s.count}${s2.count}" name="freePosition" value="${group.freePosition}" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="3" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
									</td>
									<td width="4%" style="display:none;" class="soldPayPosition${group.id}">
										<span style="color:#eb0205" >0</span>
										<input style="display:none;" disabled="disabled" type="text" name="soldPayPosition" value="${group.soldPayPosition}" class="required digits modposition" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="3" onKeyUp="this.value=this.value.replace(/\D/g,'')"/>
									</td>									
									<td width="7%" class="tnwrap">										
										<shiro:hasPermission name="group:manager:edit"><a href="javascript:void(0)"  id="modbtn${s.count}${s2.count}" style="" 
											onClick="modgroup('#groupid${s.count}${s2.count}','#savebtn${s.count}${s2.count}','#delbtn${s.count}${s2.count}','#cancelbtn${s.count}${s2.count}',this)">修改</a></shiro:hasPermission>
										<shiro:hasPermission name="group:manager:edit"><a href="javascript:void(0)"  id="savebtn${s.count}${s2.count}" style="display:none;" 
											onClick="savegroup(${activity.id},'#modbtn${s.count}${s2.count}','#delbtn${s.count}${s2.count}','#cancelbtn${s.count}${s2.count}',this,'#childform${s.count}','#childtr${s.count}${s2.count}','${ctx}')">确认</a></shiro:hasPermission>										
										<shiro:hasPermission name="group:manager:delete"><a href="javascript:void(0)" id="delbtn${s.count}${s2.count}" onClick="return confirmxCopy('要删除该产品的此团期吗？',${group.id},${activity.id },this,'child${s.count}')">删除</a></shiro:hasPermission>
										<shiro:hasPermission name="group:manager:edit"><a style="color:#ec0203;display:none;" id="cancelbtn${s.count}${s2.count}" href="javascript:void(0)" 
											onClick="cancelgroup('#modbtn${s.count}${s2.count}','#savebtn${s.count}${s2.count}','#delbtn${s.count}${s2.count}',this)">取消</a></shiro:hasPermission>
										
										
									</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
						</div>
						</form>
						<div style="text-align: right;">
<%--							<c:if test="${s2.count==1 }">--%>
							<div class="kong"></div><div class="kong"></div><div class="kong"></div><div class="kong"></div><div class="kong"></div>
								<input class="btn btn-primary"  style="text-align: right;" type="submit" onClick="return releaseProduct('确定发布该产品吗',${activity.id})" value="重新确认并发布"/>
<%--							</c:if>	--%>
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
				<c:if test="${travelActivity.activityStatus == 2 }">
					<a onClick="confirmBatchIsNull('需要将选择的产品下架吗','off')">批量下架</a>
				</c:if>
				<c:if test="${travelActivity.activityStatus == 1 or travelActivity.activityStatus == 3}">
					<a onClick="confirmBatchIsNull('需要将选择的产品上架吗','')">批量上架</a>
				</c:if>
					<a onClick="confirmBatchIsNull('删除所有选择的团期吗','del')">批量删除</a>
				</dd>
				
				</dl>
			<div class="endPage">${page}</div>
			</div>
			<br/>
			</c:if>
			<c:if test="${fn:length(page.list) eq 0}">
				<table id="contentTable" class="table activitylist_bodyer_table" >
				<thead>
					<tr>
						<th class="table_borderLeftN" width="4%">全选<input name="allChk" type="checkbox" onclick="checkall(this)"/></th>
<%--					<th width="10%">产品编号</th>--%>
						<th width="14%">产品名称</th>
<%--					<th width="8%">产品系列</th>--%>
						<th width="8%">出发地</th>
						<th width="8%">航空</th>
						<th width="8%">签证</th>
						<th width="16%">最近出团日期</th>
						<th width="10%">成人同行价</th>
						<c:if test="${activityKind eq '2'}">
							<th width="10%">成人直客价</th>
						</c:if>
						<th width="6%">操作</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<c:if test="${activityKind eq '2'}">
							<td colspan="9" style="text-align: center;">经搜索暂无数据，请尝试改变搜索条件再次搜索</td>
						</c:if>
						<c:if test="${activityKind ne '2'}">
							<td colspan="8" style="text-align: center;">经搜索暂无数据，请尝试改变搜索条件再次搜索</td>
						</c:if>						
					</tr>
				</tbody>
			</table>
		</c:if>
	</div>
   
</body>
</html>