<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
	<title>付款审核</title>
	<meta name="decorator" content="wholesaler"/>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
   <style type="text/css">
   </style>
	<script type="text/javascript">
	
		var activityIds = "";

	$(function() {
      $("div.message_box div.message_box_li").hover(function(){
        $("div.message_box_li_hover",this).show();
    },function(){
          $("div.message_box_li_hover",this).hide();
    });

    //加载操作按键
    $('.handle').hover(function() {
        if(0 != $(this).find('a').length){
            $(this).addClass('handle-on');
            $(this).find('dd').addClass('block');
        }
    },function(){$(this).removeClass('handle-on');$(this).find('dd').removeClass('block');});
  
   var _$review = $("#review").val();   
     if(_$review==""){
         $("#isRecord").addClass("select");
      }else{
                $("#isRecord"+_$review).addClass("select"); 
       }

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
			
			$('.team_top').find('.table_activity_scroll').each(function(index, element) {
				var _gg=$(this).find('tr').length;
	            if(_gg>=20){
	            $(this).addClass("group_h_scroll");
	            $(this).prev().wrap("<div class='group_h_scroll_top'></div>");
	            }

		  });
			
			$("#targetAreaId").val("${travelActivity.targetAreaIds}");
			$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
			
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
						$(searchFormselect[i]).children("option:selected").val() != null) {
					selectRequest = true;
				}
			}
			if(inputRequest||selectRequest) {
				$('.zksx').click();
			}
			
			activityIds = "${activityIds}";
		 	$("#groupform").validate({});
		 	jQuery.extend(jQuery.validator.messages, {
		  		required: "必填信息"
   			});
            $('.nav-tabs li').hover(function(){
                $('.nav-tabs li').removeClass('current');
                $(this).parent().removeClass('nav_current');
                 if($(this).hasClass('ernav'))
                 {
                     if(!$(this).hasClass('current')){
                        $(this).addClass('current');
                        $(this).parent().addClass('nav_current');
                     }
                 }
                },function(){
                    $('.nav-tabs li').removeClass('current');
                    $(this).parent().removeClass('nav_current');
                    var _active = $(".totalnav .active").eq(0);
                    if(_active.hasClass('ernav')){
                        _active.addClass('current');
                        $(this).parent().addClass('nav_current');
                    }
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
			
			
            var _$orderBy = $("#orderBy").val();
            if(_$orderBy==""){
                _$orderBy="id DESC";
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
            $(this).addClass("group_h_scroll");}
          });
		});
		
function expand(child,obj,srcActivityId){
            if($(child).is(":hidden")){
                
                if("${userType}"=="1") {
                    $.ajax({
                        type:"POST",
                        url:"${ctx}/stock/manager/apartGroup/payReservePosition",
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
                if(!$(child).is(":hidden")){           
                	$(child).hide();
                    $(obj).parents("td").attr("class","");
                    $(obj).html("展开全部团期");
                }
            }
        }
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/payment/review/island/${reviewLevel}");
			$("#searchForm").submit();
	    }
		function confirmBatchIsNull(mess,sta) {
			if(activityIds != ""){
				if(sta=='off'){
					confirmBatchOff(mess);
				}else if(sta=='del'){
					confirmBatchDel(mess);
				}
			}else{
				$.jBox.error('未选择产品','系统提示');
			}
		}
	    function getSoldPayPosition(obj) {
	    	
	    }
	    function modgroup(groupid,savebtn,delbtn,cancelbtn,obj){    	
	    	$(obj).parent().parent().find("span").css("display","none");
	    	$(obj).parent().parent().find("input[type='text']").css("display","inline-block");
	    	$(obj).parent().parent().find("input[type='text']").attr("disabled",false);
	    	$(groupid).css("display","none");
	    	$(groupid).attr("disabled",false);
	    	$(savebtn).css("display","block");
	    	$(delbtn).css("display","none");
	    	$(cancelbtn).css("display","block");
	    	$(obj).css("display","none");
	    }
		function cancelgroup(modbtn,savebtn,delbtn,obj){
			$(modbtn).css("display","block");
	    	$(savebtn).css("display","none");
	    	$(delbtn).css("display","block");
	    	$(obj).css("display","none");
			$(obj).parent().parent().find("span").css("display","block");
	    	$(obj).parent().parent().find("input[type='text']").css("display","none");
	    	$(obj).parent().parent().find("input[type='text']").attr("disabled",true);
	    }
	    function downloads(docid){
	    	window.open("${ctx}/sys/docinfo/download/"+docid);
	    	//window.location.href = "${ctx}/sys/docinfo/download/"+docid;
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
			var groupOD = $(obj).parent().prev().children("input[name='groupOpenDate']").val();
			return groupOD;
		}
		function takeModVisaDate(obj) {
			var groupOD = $(obj).parent().parent().children().first().children("input[name='groupOpenDate']").val();
//			alert(groupOD);
			return groupOD;
		}
		function comparePositionMod(obj){
    		var plan = $(obj).val();
    		$(obj).parent().next().find("input").val(plan);
    		$(obj).parent().next().find("input").focus();
    		$(obj).parent().next().find("input").blur();
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

        function groupDetail(url) {
			window.open(encodeURI(encodeURI(url)));
        }
        
        var requestAgentId;
        var requestActivityGroupId;
        var maxReserveNum;
        var activityGroupList;
        var maxFontMoneyBack;
        function returnReserve(mess, activityGroupId,leftFontMoney,freePosition) {
     			
                 requestActivityGroupId=activityGroupId;              
                 var option = '<div style="margin-top:20px; padding-left:0px;">';
                 option += '<p>当前余位数量：'+ freePosition +'</p><dl style="overflow:hidden; padding-right:5px;"><dd style=" margin:0; float:left; width:100%;"><table class="table activitylist_bodyer_table"><thead><tr><th width="25%">序号</th><th width="25%">经销商</th><th width="25%">切位</th><th width="25%">已售出切位</th></tr></thead>'; 
       			 $.ajax({
    	                type: "POST",
    	                async: false,
    	                url: "${ctx}/stock/manager/apartGroup/getReserveByGroupId?dom=" + Math.random(),
    	                data: {
    	                	activityGroupId : activityGroupId
    	                },
    	                success: function(msg) {                          
    	                activityGroupList=msg;
    	                for(var i=0;i<msg.length;i++){
    	                	option=option+'<tr><td><input name="radioid" type="radio" value="'+msg[i].agentId+"-"+msg[i].leftpayReservePosition+"-"+msg[i].soldPayPosition+'" style="margin:0;" />'+(i+1)+'</td><td> '+msg[i].agentName+'</td><td> '+msg[i].leftpayReservePosition+'</td><td> '+msg[i].soldPayPosition+'</td></tr></thead>';
    	                 	
    	                }    	             
    	                }
    	            });
    	            option+="</table>";        	
        	
        	var mess= '<div class="msg" style="margin-left:10px"><div class="field">'+option+
            '</div>'+
            '<p>归还切位数量：</p><div class="field"><input type="text" style="width:150px" id="reserveBackAmount" name="reserveBackAmount" value="" /></div>' +  
            '<div class="errorBlock" style="display: none;color:#FF0000"></div><BR>'+ 
                '<p>请填写您的还位原因：</p><div class="field"><textarea name="inputtext"'+
                ' id="inputtext"  rows="3" cols="20"></textarea></div>' +             
            '<div class="msg" style="margin-left:110px"><div class="btn btn-primary ydbz_x" onclick="confirmReserve()">确定</div>&nbsp;</div><br>';        
           
           $.jBox(mess, { title: "切位归还", buttons:{},width:400});
       
        }
   function confirmReserve(){                 
             var New=document.getElementsByName("radioid");
             var  find=false;
             var strNew;
             var maxReserveNum;
             var requestAgentId;
             var soldPayPosition;
             var reserveBackAmount=$("#reserveBackAmount").val();
             var inputtext=$("#inputtext").val();            
             for(var i=0;i<New.length;i++){
                 if(New.item(i).checked){
                     strNew=New.item(i).getAttribute("value"); 
                     var strs= new Array(); //定义一数组 
                     strs=strNew.split("-"); //字符分割                       
                     requestAgentId=strs[0]; 
                     maxReserveNum =strs[1];   
                     soldPayPosition =strs[2];                        
                     find=true;
                    break;
               }
             }
             if(find==false) {
              $(".errorBlock").html("请选择切位记录").show();
              return;
            }
            var repos = /^[1-9]+[0-9]*]*$/;
            var re = /^[0-9]+[0-9]*]*$/;
            var reserveBackAmount=$("#reserveBackAmount").val();

            if(!reserveBackAmount.match(repos)||parseInt(reserveBackAmount)>(parseInt(maxReserveNum) -parseInt(soldPayPosition))|| parseInt(reserveBackAmount)<0){             
                $(".errorBlock").html("人数请输入正整数1到"+(parseInt(maxReserveNum) -parseInt(soldPayPosition))+"之间的整数").show();
                return false; 
                /*}else   if(fontMoneyBackAmount==null||fontMoneyBackAmount==''||(!fontMoneyBackAmount.match(re))||
                    parseInt(fontMoneyBackAmount)>parseInt(maxFontMoneyBack) || parseInt(maxFontMoneyBack)<0){
                //alert("金额请输入整数");
                $(".errorBlock").html("金额请输入正整数0到"+maxFontMoneyBack+"之间的整数").show();
                return false; */       
            }else{  
                $(".errorBlock").hide();
                $.ajax({
                    type: "POST",
                    url: "${ctx}/stock/manager/apartGroup/returnReserve?dom=" + Math.random(),
                    data: {
                        activityGroupId : requestActivityGroupId,
                        agentId : requestAgentId,
                        reserveBackAmount:reserveBackAmount,
                        fontMoneyBackAmount:1, //fontMoneyBackAmount
                        inputText:inputtext                        
                    },
                    success: function(msg) {
                        if(msg == 'fail') {
                            top.$.jBox.tip('归还失败','warning');
                            top.$('.jbox-body .jbox-icon').css('top','55px');
                        } else if(msg == 'success') {
                            top.$.jBox.tip('归还成功','warning');
                            top.$('.jbox-body .jbox-icon').css('top','55px');
                            location.reload();
                        } else {
                            top.$.jBox.tip(msg,'warning');
                            top.$('.jbox-body .jbox-icon').css('top','55px');
                        }
                    }
                });
                top.$('.jbox-body .jbox-icon').css('top','55px');
                return false;
            }
        }


        
        function agentIdChange(){
        requestAgentId=$("#agentId").val();
        for(var i=0;i<activityGroupList.length;i++){
        	if(activityGroupList[i].agentId==requestAgentId){
        		$("#maxReserveNum").html(activityGroupList[i].leftpayReservePosition);
        		maxReserveNum=activityGroupList[i].leftpayReservePosition;
        		$("#maxFontMoneyBack").html(activityGroupList[i].leftFontMoney);
        		maxFontMoneyBack=activityGroupList[i].leftFontMoney;
        	}
        }
        }
        
   

        function getDepartment(departmentId) {
    		$("#departmentId").val(departmentId);
    		$("#searchForm").submit();
    	}

        function changeIsRecord(itemRecord){
          $("#review").val(itemRecord);
          $(".supplierLine .select").removeClass("select");
          $("#isRecord"+itemRecord).addClass("select");
          $("#searchForm").submit();
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
		
		function batchAuditing(){
			var activityIds = $("#activityIds").val();
			if(activityIds == ""){
				$.jBox.info('至少选择一记录进行操作','系统提示');
				return;
			}
			
			$.ajax({
	              type: "POST",
	              url: "",
	              data: {
	                  activityIds : $("#activityIds").val()
	              },
	              success: function(msg) {
	                  // 页面刷新处理
	              }
	          });
		}

		function revokePayAudit(id, nowLevel, orderType){
        $.jBox.confirm("确定要撤销付款审核吗？", "提示", function(v, h, f) {
            if (v == 'ok') {
                $.ajax({
                    type: "POST",
                    url: "${ctx}/pay/review/revokePayAudit",
                    cache:false,
                    async:false,
                    data:{id : id, nowLevel : nowLevel, orderType : orderType},
                    success: function(e){
                          window.location.reload();                     
                        
                    },
                    error : function(e){
                        top.$.jBox.tip('请求失败。','error');
                        return false;
                    }
                 });
            }
        });
    };
	</script>
  <style type="text/css">

.message_box{width:100%;padding:0px 0 40px 0;}
.message_box_li { width:246px;height:33px;float:left;margin:5px 20px 5px 0;}
.message_box_li_a{max-width:240px;padding:0 5px;margin-top:9px;height:24px;line-height:24px;background:#a8b9d3;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px;color:#ffffff;float:left;position:relative;cursor:pointer;}
.message_box_li .curret{background:#62afe7;}
.message_box_li_em{background:#ff3333;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px;color:#ffffff;padding:2px;height:10px;min-width:14px;line-height:10px;text-align:center;float:left;position:absolute;z-index:4;right:-12px;top:-9px;font-size:12px; }
.message_box_li_a span{float:left;}
.message_box_li_hover{width:auto;line-height:24px;color:#5f7795;font-size:12px;border:1px solid #cccccc;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px; -webkit-box-shadow:0 0 2px #b2b0b1; -moz-box-shadow:0 0 2px #b2b0b1; box-shadow:0 0 2px #b2b0b1;position:absolute;z-index:999;background:#ffffff;cursor:pointer;left:-5px;padding:0 5px;display:none; }
</style>

</head>
<body> 
                       
       <page:applyDecorator name="payment_review_head">
             <page:param name="current">island</page:param>
       </page:applyDecorator>  


    <!--右侧内容部分开始-->   

      <div class="message_box">
          <c:forEach items="${userJobs}" var="role">
            <div class="message_box_li">
              <c:choose>
                <c:when test="${userJobId==role.id}">
                      <a  href="${ctx}/payment/review/island/${reviewLevel}?userJobId=${role.id}">
                      <div class="message_box_li_a curret">
                <span>
                ${fns:abbrs(role.deptName,role.jobName,40)}
                </span>
                <c:if test ="${role.count gt 0}">
                  <div class="message_box_li_em" >
                  ${role.count}               
                  </div>
                </c:if>
                <c:if test="${fns:getStringLength(role.deptName,role.jobName) gt 37}">
                  <div class="message_box_li_hover">
                    ${role.deptName }_${role.jobName }
                  </div>
                </c:if>
              </div>
                      </a>
                  </c:when>
                <c:otherwise>
                  <a  href="${ctx}/payment/review/island/${reviewLevel}?userJobId=${role.id}">
                      <div class="message_box_li_a">
                <span>${fns:abbrs(role.deptName,role.jobName,40)}</span>
                <c:if test ="${role.count gt 0}">
                  <div class="message_box_li_em" >
                  ${role.count}         
                  </div>
                </c:if>
                <c:if test="${fns:getStringLength(role.deptName,role.jobName) gt 37}">
                  <div class="message_box_li_hover">
                    ${role.deptName }_${role.jobName }
                  </div>
                </c:if>
              </div>
                      </a>
                  </c:otherwise>
              </c:choose>
              
            </div>
          </c:forEach>  
        </div>


	 <div class="activitylist_bodyer_right_team_co_bgcolor inventory_management">
<!--   <div class="activitylist_bodyer_right_team_co_bgcolor">-->
	 <form:form id="searchForm" modelAttribute="activityIslandGroup" action="${ctx}/payment/review/island/${reviewLevel}" method="post" >
        <input type="hidden" id="typeId" value="${typeId}"/>
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<input id="departmentId" name="departmentId" type="hidden" value="${departmentId }" />
		<input id="activityIds" type="hidden" name="activityIds"/>
    <input id="review" name="review"  type="hidden"  value="${review}">    
    <input id="reviewLevel"  type="hidden"  name="reviewLevel"  value="${reviewLevel}">
    <input id="userJobId" name="userJobId"  type="hidden"  value="${userJobId}"> 
     <div class="activitylist_bodyer_right_team_co">    	 
    	 <div class="activitylist_bodyer_right_team_co2 pr wpr20">
         	<label>团号：</label><input class="txtPro inputTxt inquiry_left_text" id="wholeSalerKey" name="groupCode" value="${groupCode }"/>
         	<span class="ipt-tips" style="display: block;"></span>
         	</div>
	      <div class="form_submit">
			<input class="btn btn-primary" type="submit" value="搜索">
		</div>
		<a class="zksx" >筛选</a>
		<div class="ydxbd">     	
    	 <div class="activitylist_bodyer_right_team_co3">
		      <div class="activitylist_team_co3_text">地接社：</div>
         <select name="supplierId">
                     <option value="">不限</option>
                     <c:forEach   items="${supplierList }" var="supplier">
                     <option value="${supplier.id }" <c:if test="${param.supplierId==supplier.id}">selected="selected"</c:if>>${supplier.supplierName }</option>
                     </c:forEach>
                 </select>
      </div> 
      	<div class="activitylist_bodyer_right_team_co2">
				<label>出团日期：</label>
				<input id="groupOpenDate" class="inputTxt dateinput" name="groupOpenDate" value='<fmt:formatDate value="${activityIslandGroup.groupOpenDate }" pattern="yyyy-MM-dd"/>' style="margin-left: -3px; width: 122px;" onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" readonly/> 
				<span style="font-size:12px; font-family:'宋体';"> 至</span>  
				<input id="groupEndDate" class="inputTxt dateinput" name="groupEndDate" value='<fmt:formatDate value="${activityIslandGroup.groupEndDate }" pattern="yyyy-MM-dd"/>' style="width: 122px;" onClick="WdatePicker()" readonly/>
        	</div>
         		<div class="activitylist_bodyer_right_team_co3">
           		 <div class="activitylist_team_co3_text" >发起人：</div>
           		<input id="createByName" name="createByName"  class="txtPro inputTxt inquiry_left_text" value="${param.createByName }" /> 
          	</div>
      <div class="kong"></div>
		
              
    		<div class="activitylist_bodyer_right_team_co3">  
                  <div class="activitylist_team_co3_text">产品名称：</div>
                  <input class="txtPro inputTxt inquiry_left_text" id="activityName" name="activityName" value="${param.activityName }"/>
         	</div>
         	<div class="activitylist_bodyer_right_team_co3">
				<label>岛屿名称：</label>	<select name="island" id="island">
							 <option value="">不限</option>
                     <c:forEach   items="${islandList }" var="myisland">
                     <option value="${myisland.islandName }" <c:if test="${param.island==myisland.islandName}">selected="selected"</c:if>>${myisland.islandName }</option>
                     </c:forEach>
						</select>        
     	  	</div>

	        <div class="activitylist_bodyer_right_team_co3">
				<label>酒店名称：</label>	<select name="hotel" id="hotel">
							 <option value="">不限</option>
                     <c:forEach   items="${hotelList }" var="myhotel">
                     <option value="${myhotel.nameCn }" <c:if test="${param.hotel==myhotel.nameCn}">selected="selected"</c:if>>${myhotel.nameCn }</option>
                     </c:forEach>
						</select>          
         </div>

<div class="activitylist_bodyer_right_team_co1" style="width: 38%">
                                    <div class="activitylist_team_co3_text" style="width: 80px;" >付款金额：</div>
                                    <select name="currencyId" id="currencyId">
								 <option value="">不限</option>
                     <c:forEach   items="${currencyList }" var="mycurrency">
                     <option value="${mycurrency.id }" <c:if test="${param.currencyId==mycurrency.id}">selected="selected"</c:if>>${mycurrency.currencyName }</option>
                     </c:forEach>
						        </select>          
                                    <input type="text" class="inputtxt" style="width: 50px"  placeholder="输入金额" name="startCurrency" id="startCurrency" value="${param.startCurrency}"/>~<input type="text" class="inputtxt" style="width: 50px" placeholder="输入金额"  name="endCurrency" id="endCurrency" value="${param.endCurrency}"/>
                                </div>
	     </div>
      </div>
	</form:form>
<div class="activitylist_bodyer_right_team_co_paixu">
        <div class="activitylist_paixu">
          <div class="activitylist_paixu_left">
             <ul>
            <li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
            <li class="activitylist_paixu_left_biankuang liid"><a onClick="sortby('id',this)">创建时间</a></li>
            <li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
            <!--  <li class="activitylist_paixu_left_biankuang lisettlementAdultPrice"><a onClick="sortby('settlementAdultPrice',this)">同行价格</a></li> -->
            <!-- 解决Bug 4462 -->
            <li class="activitylist_paixu_left_biankuang litotalPrice"><a onClick="sortby('totalPrice',this)">同行价格</a></li>
            <c:if test="${companyId!=68}">
            <li class="activitylist_paixu_left_biankuang lisuggestAdultPrice"><a onClick="sortby('suggestAdultPrice',this)">直客价格</a></li>
            </c:if>
            <li class="activitylist_paixu_left_biankuang ligroupOpenDate"><a onClick="sortby('groupOpenDate',this)">出团日期</a></li>
            </ul>
      </div>
          <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong  id="total">${page.count}</strong>&nbsp;条</div>
          <div class="kong"></div>
        </div>
      </div>

    <div class="supplierLine">
        <a href="javascript:void(0)" onclick="changeIsRecord(11)" id="isRecord11">全部</a>
        <a href="javascript:void(0)" onclick="changeIsRecord('')" id="isRecord">待本人审核</a>
        <a href="javascript:void(0)" onclick="changeIsRecord(22)" id="isRecord22">本人审核通过</a>
        <a href="javascript:void(0)" onclick="changeIsRecord(1)" id="isRecord1">审核中</a>
        <a href="javascript:void(0)" onclick="changeIsRecord(2)" id="isRecord2">已通过</a>
        <a href="javascript:void(0)" onclick="changeIsRecord(0)" id="isRecord0">未通过</a>
        <a href="javascript:void(0)" onclick="changeIsRecord(5)" id="isRecord5">已取消</a>
    </div>    
  
	<table id="contentTable" class="table activitylist_bodyer_table" >
		<thead >		
              <tr>              
                                <th width="4%">序号</th>
                                <th width="7%">成本名称</th>   
                                <th width="7%">渠道商/地接社</th>   
                                <th width="5%">金额</th>   
                                <th width="6%">团号</th>
                                <th width="7%">产品名称</th>
								<th width="6%">出团日期</th>
                                <th width="6%">截团日期</th>
								<th width="4%">参考航班</th>  
								<th width="6%">岛屿名称</th>
                                <th width="6%">酒店名称</th>
                                <th width="5%">房型*晚数</th>
								<th width="5%">余位数/预收数</th>
								<th width="6%">审核发起<br>时间</th> 
								<th width="5%">审核发起人</th>
                                <th width="5%">审核状态</th>
                                <th width="5%">审核</th>
                                <th width="6%">操作</th>
           </tr>
		</thead>
		<tbody>
		  
			<c:forEach items="${page.list}" var="activity" varStatus="s">
			<c:if test="${empty review}"> 
			 	<tr id="review${activity.id}">	
			</c:if> 
            <c:if test="${not empty review }"> 
			 	<tr id="review${activity.id}_${review}">	
			</c:if> 		
		  <td class="tc">${s.count} </td>
          <td class="tc">${activity.name}</td>
          <td class="tc">${activity.supplyName}</td>
          <td class="tc">${activity.currencyMark} ${activity.totalPrice}</td>
          <td class="tc">${activity.groupCode}</td>
                  <td class="tc">${activity.activityName}</td>
                 
                    <td class="tc"><fmt:formatDate value="${activity.groupOpenDate}" pattern="yyyy-MM-dd"/></td>
                     <td class="tc"><fmt:formatDate value="${activity.groupEndDate}" pattern="yyyy-MM-dd" /></td>
                   
                          <td class="tc">航班</td>
                   
                    <td class="tc">${activity.islandName}</td>
                    <td class="tc">${activity.hotelName}</td>
                    <td class="tc">${fns:getIslandRoom(activity.activityUuid)}</td>
                    <td class="tc">${fns:getAirRemNumber(activity.agpUuid)}/${activity.advNumber }</td>
					<td><fmt:formatDate pattern="yyyy-MM-dd" value="${activity.updateDate}"/></td>
					<td>${activity.createByName }</td>
                    <td class="tc" id="result${activity.id}">
                    ${fns:getNextPayIslandReview(activity.id)}   
              </td> 
			 
              <td class="tc" id="change${activity.id}">
                 <c:if test="${ activity.payNowLevel == reviewLevel && activity.payReview==1}"> 
              <a target="_blank" href="${ctx}/payment/review/islandDetail/${activity.productUuid}/${activity.activityUuid}/${reviewLevel}/${reviewCompanyId}?from=operatorPre">审核</a>               
         </c:if>   
              </td>           
              <td class="p00">            
          <a target="_blank" href="${ctx}/payment/review/islandRead/${activity.productUuid}/${activity.activityUuid}/${reviewLevel}?from=operatorPre">查看</a>&nbsp;
        <br><a href="${ctx }/cost/manager/forcastList/${activity.activityUuid}/${typeId}" target="_blank">预报单</a>&nbsp;
     <br><a href="${ctx }/cost/manager/settleList/${activity.activityUuid}/${typeId}" target="_blank">结算单</a>    
	 <br><c:if test="${activity.payUpdateBy eq fns:getUser().id and activity.payNowLevel ne 1 and activity.payNowLevel-1 ne reviewCompany.topLevel and activity.payReview ne 0 }">
		 <a href="javascript:void(0)" onClick="revokePayAudit('${activity.id}','${activity.payNowLevel}','${activity.product_type_id }')">撤销</a></c:if>                           

              </td>
             </tr>
				
		
			</c:forEach>
		</tbody>
	</table>
	
	</div>
	   <div class="page">
			<div class="pagination">
			<c:if test="${fn:length(page.list) ne 0}">
		  <!-- 	<dl>
				<dt>&nbsp;&nbsp;<input name="allChk" type="checkbox" onclick="checkall(this)"/>全选</dt>
				<dd>
					<a onClick="batchAuditing()">批量审核</a>
				</dd>
			</dl> -->
			</c:if>
			<div class="endPage">${page}</div>
			<div style="clear:both;"></div>
			</div>	
			</div>
			<br/>
<script type="text/javascript">
$(document).ready(function(){  
  //目的地去掉最后的逗号
 var total=$("#pageSize").val();
 for(var i=1;i<=total;i++){
  if($("#area"+i).length>0){
   var str=$("#area"+i).html();  
   $("#area"+i).html(str.substring(0,str.length-2));
  }
 }
});
</script>
	</body>
</html>