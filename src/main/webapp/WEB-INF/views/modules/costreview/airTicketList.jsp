<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
	<title>机票成本审核</title>
    <meta name="decorator" content="wholesaler"/>	
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script  type="text/javascript">
    	var contextPath = '${ctx}';
    </script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>

	<script type="text/javascript">
	
		var activityIds = "";
		$(function() {

         $("div.message_box div.message_box_li").hover(function(){
        $("div.message_box_li_hover",this).show();
    },function(){
          $("div.message_box_li_hover",this).hide();
    });


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
						$(searchFormselect[i]).children("option:selected").val() != "100" &&
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
			
			var _$review = $("#review").val();
            if(_$review==""){
               $("#isRecord").addClass("select");
            }else{
                $("#isRecord"+_$review).addClass("select"); 
            }

            var _$orderBy = $("#orderBy").val();
            if(_$orderBy==""){
                _$orderBy="id DESC";
                $("#orderBy").val("id");
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
           
           $("a[name=cost-del-name]").click(function(){
       		var gid = $(this).attr("groupid");
       		deleteCost(gid,this);       		
       		});
		});
		
	    function expand(child,obj,srcActivityId){
            if($(child).is(":hidden")){
                
                if("${userType}"=="1") {
                    $.ajax({
                        type:"POST",
                        url:"${ctx}/stock/review/payReservePosition",
                        data:{
                            srcActivityId:srcActivityId
                        },
                        success:function(msg) {
                        	$(obj).html("关闭查看");
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
                	$(obj).html("关闭成本");
                	$(child).show();
                    $(obj).parents("td").attr("class","td-extend");
                }
            }else{
                if(!$(child).is(":hidden")){           
                	$(child).hide();
                    $(obj).parents("td").attr("class","");
                    $(obj).html("查看成本");
                }
            }
        }
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/cost/review/airTicketList/${reviewLevel}");
			$("#searchForm").submit();
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
        
        var deleteCost = function(id,$this){
            $.jBox.confirm("确定要删除所有的成本数据吗？", "提示", function(v, h, f) {
                if (v == 'ok') {
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/cost/review/delByGroup",
                        cache:false,
                        async:false,
                        data:{gid : id},
                        success: function(e){
                            if(e == 'true'){
                               $($this).parent("td[name=cost-manager-name]").find("a[name=cost-add-name]").html("审核");
                               $($this).parent("td[name=cost-manager-name]").children(":not(a[name=cost-add-name])").remove();
                               window.location.reload();
                            }else{
                                top.$.jBox.tip('请求失败。','error');
                                return false;
                            }
                            
                        },
                        error : function(e){
                            top.$.jBox.tip('请求失败。','error');
                            return false;
                        }
                     });
                }
            });
            
          }
        
      function cancelOp(costRecordId,orderType,nowLevel){
	    $.jBox.confirm("确定要撤销此审核吗？","提示",function(v,h,f){
		if(v=='ok'){
			$.ajax({
				type:"POST",
				url:"${ctx}/cost/review/cancelOp",
				data:{costRecordId:costRecordId,orderType:orderType,nowLevel:nowLevel},
					success:function(data){
                        $.jBox.tip('操作成功', 'success');
						$("#searchForm").submit();
					
				   },
                    error : function(e){					  
                        top.$.jBox.tip('请求失败。','error');
                        return false;
                    }
			})
		}
	})
	}  
      function  changeIsRecord(itemRecord){
    	  $("#review").val(itemRecord);
    	  $(".supplierLine .select").removeClass("select");
    	  $("#isRecord"+itemRecord).addClass("select");
    	  $("#searchForm").submit();
      }
      
	//全选&反选操作
	function checkall(obj){
	    if($(obj).attr("checked")){
	        $('#contentTable input[type="checkbox"]').attr("checked",'true');
	        $("input[name='allChk']").attr("checked",'true');
	    }else{
	        $('#contentTable input[type="checkbox"]').removeAttr("checked");
	        $("input[name='allChk']").removeAttr("checked");
	    }
	}
	
	function checkreverse(obj){
	    var $contentTable = $('#contentTable');
	    $contentTable.find('input[type="checkbox"]').each(function(){
	        var $checkbox = $(this);
	        if($checkbox.is(':checked')){
	            $checkbox.removeAttr('checked');
	        }else{
	            $checkbox.attr('checked',true);
	        }
			 $checkbox.trigger('change');
	    });
	}
	
	function batchApproval(ctx,checkBox) {
		var tmp=0;
		var count=0;
	    $("input[name='"+checkBox+"']").each(function(){ 
	    	if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
	           tmp=tmp +","+$(this).attr('value');
	           count++;
	    	}
	    });    
	    if(tmp=="0"){
	        alert("请选择审批记录");
	        return;
	    }
	    $('#tip').text($('#tip').text().replace('num', count));
		$.jBox($("#batch-verify-list").html(), {
			title: "批量审批", buttons: { '取消': 0,'驳回': 1, '通过': 2 }, submit: function (v, h, f) {
				var reviewComment = f.reviewComment;
				if (v == "1") {
					$.ajax({
						type:"POST",
						url:"${ctx}/cost/review/batchDeny",
						data:{code:tmp,reviewComment:reviewComment},
						success:function(data){
							$("#searchForm").submit();
						},
						error:function(){
							alert('返回数据失败');
						}
					});
				} else if (v == "2") {
					$.ajax({
						type:"POST",
						url:"${ctx}/cost/review/batchPass",
						data:{code:tmp,reviewComment:reviewComment},
						success:function(data){
							$("#searchForm").submit();
						},
						error:function(){
							alert('返回数据失败');
						}
					});
				}
			}, height: 250, width: 350
		});
		inquiryCheckBOX();
	}
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
    <c:if test="${reviewLevel==1}">                   
      <page:applyDecorator name="cost_review_head">
      <page:param name="current">airticket</page:param>
      </page:applyDecorator>
    </c:if>
    <c:if test="${reviewLevel>=2}">                   
      <page:applyDecorator name="cost_review_manager">
      <page:param name="current">airticket</page:param>
      </page:applyDecorator>
    </c:if>


    <!--右侧内容部分开始-->
   <!-- <div class="rolelist_btn">
        <c:forEach items="${userJobs}" var="userJob">
            <c:if test="${userJobId == userJob.id}">
                <a class="ydbz_x" href="${ctx}/cost/review/airTicketList/${reviewLevel}?userJobId=${userJob.id}">${userJob.deptName}_${userJob.jobName}</a>
            </c:if> 
            <c:if test="${userJobId != userJob.id}">
                <a class="ydbz_x gray" href="${ctx}/cost/review/airTicketList/${reviewLevel}?userJobId=${userJob.id}">${userJob.deptName}_${userJob.jobName}</a>
            </c:if> 
        </c:forEach>
    </div>   -->
 <div class="message_box">
          <c:forEach items="${userJobs}" var="role">
            <div class="message_box_li">
              <c:choose>
                <c:when test="${userJobId==role.id}">
                      <a  href="${ctx}/cost/review/airTicketList/${reviewLevel}?userJobId=${role.id}">
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
                  <a  href="${ctx}/cost/review/airTicketList/${reviewLevel}?userJobId=${role.id}">
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
	 <form:form id="searchForm" modelAttribute="activityAirTicket" action="${ctx}/cost/review/airTicketList/${reviewLevel}" method="post">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy"  type="hidden"  value="${page.orderBy}"/>
		<input id="review" name="review" type="hidden" value="${review}">
        <input id="reviewLevel"  type="hidden"  name="reviewLevel"  value="${reviewLevel}">
        <input id="userJobId" name="userJobId"  type="hidden"  value="${userJobId}">  
        <div class="activitylist_bodyer_right_team_co">
            <div class="activitylist_bodyer_right_team_co2 pr wpr20">
                <label>出发城市：</label>
                <input name="departureCity" type="text" class="inputTxt" value="${departureCity}" style="width:130px;">
            </div>
            <div class="activitylist_bodyer_right_team_co2 pr wpr20">
                <label>到达城市：</label><input  value="${arrivedCity}" name="arrivedCity" class="inputTxt" style="width:130px;">
            </div>
            <div class="form_submit">
                <input type="submit" value="搜索" class="btn btn-primary">
            </div>
            <a class="zksx">展开筛选</a>
            <div class="ydxbd" style="display: hidden;">
                <!--
                <div class="activitylist_bodyer_right_team_co2">
                    <div class="activitylist_team_co3_text">切位渠道：</div>
                    <select name="agentId">
                        <option value="">不限</option>
                        <c:forEach items="${agentinfoList}" var="agentInfo">
                            <option value="${agentInfo.id}" ${agentInfo.id==agentId?"selected":''}>${agentInfo.agentName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="activitylist_bodyer_right_team_co3">
                    <div class="activitylist_team_co3_text">切位部门：</div>
                    <select name="department">
                        <option value="">不限</option>
                        <c:forEach items="${departmentList}" var="department">
                            <option value="${department.id}" ${departmentId==department.id?"selected":''}>${department.name}</option>
                        </c:forEach>

                    </select>
                </div>
                 -->
                <div class="activitylist_bodyer_right_team_co3">
                    <div class="activitylist_team_co3_text">机票类型：</div>
                    <select name="airType">
                        <option value="">不限</option>
                        <option value="3" ${activityAirTicket.airType=='3'?"selected":''}>单程</option>
                        <option value="1" ${activityAirTicket.airType=='1'?"selected":''}>多段</option>
                        <option value="2" ${activityAirTicket.airType=='2'?"selected":''}>往返</option>
                    </select>
                </div>
                <div class="kong"></div>
                <!-- <div class="activitylist_bodyer_right_team_co3">
                    <div class="activitylist_team_co3_text">航空公司：</div>
                    <select name="airlines">
                        <option value="">不限</option>
                        <c:forEach items="${traffic_namelist}" var="trafficName">
                            <option value="${trafficName.value}" ${trafficName.value==activityAirTicket.airlines?"selected":''}>${trafficName.label} ${trafficName.description}</option>
                        </c:forEach>
                    </select>
                </div> -->
                <!--
                <div class="activitylist_bodyer_right_team_co2">
                    <label>价格范围：</label><input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" value="${settlementAdultPriceStart}" name="settlementAdultPriceStart" class="rmb inputTxt" maxlength="7" id="settlementAdultPriceStart">
                    <span> 至 </span>
                    <input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" value="${settlementAdultPriceEnd}" name="settlementAdultPriceEnd" class="rmb inputTxt" maxlength="7" id="settlementAdultPriceEnd">
                </div> -->
                <div class="activitylist_bodyer_right_team_co2">
                    <label>起飞时间：</label>
                    <input readonly="" onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" style="margin-left: -3px;" value="${startingDate}" name="groupOpenDate" class="inputTxt dateinput" id="groupOpenDate">
                    <span> 至 </span>
                    <input readonly="" onclick="WdatePicker()" value="${returnDate}" name="groupCloseDate" class="inputTxt dateinput" id="groupCloseDate">
                </div>
            </div>
            <div class="kong"></div>
        </div>
	</form:form>
	
	
    <div class="activitylist_bodyer_right_team_co_paixu">
        <div class="activitylist_paixu">
          <div class="activitylist_paixu_left">
             <ul>
            <li style="width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
            <li class="activitylist_paixu_left_biankuang liid"><a onClick="sortby('id',this)">创建时间</a></li>
            <li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
            <li class="activitylist_paixu_left_biankuang lisettlementAdultPrice"><a onClick="sortby('settlementAdultPrice',this)">同行价格</a></li> 
            <!--
            <li class="activitylist_paixu_left_biankuang lisuggestAdultPrice"><a onClick="sortby('suggestAdultPrice',this)">直客价格</a></li>
            <li class="activitylist_paixu_left_biankuang ligroupOpenDate"><a onClick="sortby('groupOpenDate',this)">出团日期</a></li> -->
           
            </ul>
      </div>
          <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
          <div class="kong"></div>
        </div>
    </div>
    <div class="supplierLine">
        <a href="javascript:void(0)" onclick="changeIsRecord(11)" id="isRecord11">全部</a>
        <a href="javascript:void(0)" onclick="changeIsRecord('')" id="isRecord">待本人审核</a>
        <a href="javascript:void(0)" onclick="changeIsRecord(1)" id="isRecord1">审核中</a>
        <a href="javascript:void(0)" onclick="changeIsRecord(2)" id="isRecord2">已通过</a>
        <a href="javascript:void(0)" onclick="changeIsRecord(0)" id="isRecord0">未通过</a>
          <a href="javascript:void(0)" onclick="changeIsRecord(5)" id="isRecord5">已取消</a>

    </div>
	<form id="groupform" name="groupform" action="" method="post" >
	<table id="contentTable" class="table activitylist_bodyer_table" >
		<thead >
			<tr>
				<th width="5%">序号</th>
                <th width="8%">成本名称</th>   
                <th width="8%">渠道商/地接社</th>   
                <th width="6%">金额</th> 
				<!--<th width="12%">航空公司</th> -->
				<th width="6%">机票类型</th>
				<!--<th width="6%">舱位</th>  -->
				<th width="7%">出发城市</th>
				<th width="7%">到达城市</th>
				<th width="8%">起飞日期</th>
				<th width="8%">到达日期</th>
				<th width="6%">应收价格</th>
				<th width="4%">预收</th>
				<th width="4%">机位<br>余位</th>
				<th width="4%">切位数</th>               
                <th width="6%">审核状态</th>
                <th width="5%">审核</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="airticket" varStatus="s">
				<tr id="parent${s.count}">
					 <td class="tc"><c:if test="${empty review }"><input type="checkbox" name="checkBox" id="checkBox" value="${airticket.id}_${reviewLevel}_${reviewCompanyId}_7" /></c:if> ${s.count}</td>
                     <td class="tc">${airticket.name}</td>
                     <td class="tc">${airticket.supplyName}</td>
                     <td class="tc">${airticket.currencyMark} ${airticket.totalPrice}</td>
					<!--<td>
					<c:forEach items="${airticket.flightInfos}" var="fi" varStatus="s">
								 <c:forEach items="${traffic_namelist}" var="tn" varStatus="s">
								    <c:if test="${tn.value == fi.airlines}">
								      ${tn.label} ${tn.description} <br>
								    </c:if>
								</c:forEach>
							</c:forEach>
					</td> -->
					<!-- 机票类型 -->
					 <td>  <c:choose>
                    <c:when test="${airticket.airType == 3}">
                        单程
                    </c:when>
                    <c:when test="${airticket.airType == 2}">
                        往返
                    </c:when>
                    <c:otherwise>
                        多段
                    </c:otherwise>
                </c:choose></td>

                <td>${airticket.departureCityLabel()}</td>
                 <td>
                    <c:forEach items="${areas}" var="arrivedcity">
                    <c:if test="${arrivedcity.id eq airticket.arrivedCity}">
                     ${arrivedcity.name}
                    </c:if>
                   </c:forEach>
               </td>
              	<!--	<td>
              		<c:forEach items="${airticket.flightInfos}" var="fi" varStatus="s">
					<c:forEach var="dic" items="${airspacelist}">
                                            <c:if test="${dic.value eq fi.airspace}">
                                                 ${dic.label} 
                                                 </c:if>
                                        </c:forEach><BR>
				</c:forEach>
				
				</td> -->
              		<!-- 出发机场 -->

					<!-- <td>
					 <c:forEach items="${airticket.flightInfos}" var="fi" varStatus="s">
					  <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq fi.leaveAirport}">
                            ${airportlist.airportName}<BR>
                            </c:if>
                          </c:forEach>
				</c:forEach>
              
            </td>
                     <td>  -->
                     <!-- 到达机场 -->
                   <!--  <c:forEach items="${airticket.flightInfos}" var="fi" varStatus="s">
					 <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq fi.destinationAirpost}">
                            ${airportlist.airportName}<BR>
                            </c:if>
                          </c:forEach>
            </c:forEach>
                </td> -->
                    <!-- <td>
                    <c:forEach items="${airticket.flightInfos}" var="fi" varStatus="s">
                    <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${fi.startTime }"/><BR>
					 </c:forEach>
					 </td>
                    <td><c:forEach items="${airticket.flightInfos}" var="fi" varStatus="s">
                          <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${fi.arrivalTime }"/><BR>                  
					 </c:forEach></td>  -->
                      <td><fmt:formatDate pattern="yyyy-MM-dd" value="${airticket.startingDate }"/></td>
                    <td><fmt:formatDate pattern="yyyy-MM-dd" value="${airticket.returnDate }"/></td>


                    <td>${airticket.currencyMark}<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${airticket.settlementAdultPrice}"/></td>
                    <td>${airticket.reservationsNum}</td>
                    <td>${airticket.freePosition}</td>
                    <td>${airticket.payReservePosition}</td>                  
                    <td>
                        ${fns:getNextCostReview(airticket.id)}             
                    </td>

                    <td  class="tc">
                    <c:if test="${airticket.nowLevel == reviewLevel && airticket.review==1}"> 
                      <a href="${ctx}/cost/review/airTicketPreRecord/${airticket.airId}/${reviewLevel}/${reviewCompanyId}" target="_blank">审核</a>
                    </c:if> 
                   <c:if test="${ fns:getUser().id eq airticket.updateBy.id    
					&& airticket.review eq 1 && airticket.nowLevel ne 1  && airticket.nowLevel-1 eq reviewLevel}">    
					<a href="#" onclick="cancelOp('${airticket.id}','7','${airticket.nowLevel }')">撤销</a> 
				</c:if> 
				
                    </td>
                    <td>  
                    <a href="${ctx}/cost/review/airTicketRead/${airticket.airId}/${reviewLevel}" target="_blank">查看</a>&nbsp;
			        <a href="${ctx }/cost/manager/forcastList/${airticket.airId}/7" target="_blank">预报单</a>&nbsp;
					<a href="${ctx }/cost/manager/settleList/${airticket.airId}/7" target="_blank">结算单</a>  
			        </td>					
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</form>
	</div>
	   <div class="page">
	   	<c:if test="${empty review }">
			<div class="pagination">
				<dl>
					<dt>
						<input name="allChk" onclick="checkall(this)" type="checkbox">
						全选
					</dt>
					<dt>
						<input name="reverseChk" onclick="checkreverse(this)"
							type="checkbox"> 反选
					</dt>
					<dd>
						<a onclick="batchApproval('${ctx }','checkBox');">批量审批</a>
					</dd>
				</dl>
			</div>
		</c:if>
		<div class="pagination">
				<div class="endPage">${page}</div>
				<div style="clear:both;"></div>
			</div>	
		</div>
		<br/>
	<div class="batch-verify-list" id="batch-verify-list"
		style="padding:20px;">
		<table width="100%"
			style="padding:10px !important; border-collapse: separate;">
			<tr>
				<td></td>
			</tr>
			<tr>
				<td><p id="tip">您好，当前您提交了num个审核项目，是否执行批量操作？</p>
				</td>
			</tr>
			<tr>
				<td><p>备注：</p>
				</td>
			</tr>
			<tr>
				<td><label> <textarea name="reviewComment"
							id="reviewComment" style="width: 290px;"></textarea> </label>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>