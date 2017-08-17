<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>产品切位</title>
<meta name="decorator" content="wholesaler"/>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
<%-- 	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script> --%>
	<script src="${ctxStatic}/common/jquery.date.format.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactorStock.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<style type="text/css">
.ydBtn {
	PADDING-BOTTOM: 10px; MARGIN-TOP: 50px; WIDTH: 278px; HEIGHT: 28px; MARGIN-LEFT: auto; MARGIN-RIGHT: auto
}
.ui-autocomplete.ui-menu.ui-front {
    z-index: 10000;
}
			.draftbox-disable {
                background-color: #cccccc;
            }

            .ydtExpand {
                background-position: 0px 0px;
                display: block;
                width: 12px;
                height: 12px;
                position: absolute;
                top: 11px;
                left: 12px;
                cursor: pointer;
                background-image: url("${ctxStatic}/images/yd-close-target.png");
            }

            .ydtClose {
                background-position: 0px -18px;
                display: block;
                width: 12px;
                height: 12px;
                position: absolute;
                top: 11px;
                left: 12px;
                cursor: pointer;
                background-image: url("${ctxStatic}/images/yd-close-target.png");
            }

            .pop-channel-container {
                margin-top: 20px;;
            }

            .pop-channel-container .header label {
                width: 100px;
                text-align: right;
            }

            .pop-channel-container .selected-channel {
                padding: 10px;
            }

            .pop-channel-container .selected-channel .channel-text {
                display: inline-block;
                padding: 0 3px;
                margin-right: 6px;
                margin-bottom: 6px;
                line-height: 25px;
                border: 1px solid #DDDDDD;
                background-color: #ededed;
            }

            .pop-channel-container .selected-channel .channel-remove {
                margin-left: 10px;
                cursor: pointer;
            }

            .ui-autocomplete.ui-menu.ui-front {
                z-index: 10000;
            }

            .file-upload {
                width: 55px;
                height: 25px;
                border: 1px solid #D9D9D9;
                color: #403938;
                font-size: 12px;
                background: transparent -moz-linear-gradient(center top, #FFF, #F9F9F9) repeat scroll 0% 0%;
            }
</style>
	<script type="text/javascript">
	
	    function showAll(){
	        $(".grouprow").each(function(){
	            $(this).show();
	        });
	    }
		$(document).ready(function() {
			$("#btnSubmit").removeAttr("disabled");
			agendChoose();
			$("#agentIdIn").comboboxInquiry();
			var datepicker= $(".groupDate").datepickerRefactor(
					{dateFormat: "yy-mm-dd",
						target:"#dateList",
						numberOfMonths: 3,
						isChickArr:getChickList(),
						defaultDate:'${travelActivity.groupOpenDate }'
						},"#groupOpenDate","#groupCloseDate");
			//表单验证	
			$("#modForm").validate({
                rules :{
                	agentId:"required"
                },
                messages: {
                	agentId:"请选择一个切位渠道"
                },
	 			errorPlacement: function(error, element) { 
			    if ( element.is(":radio") ) 
			        error.appendTo ( element.parent() ); 
			    else if ( element.is(":checkbox") ) 
			        error.appendTo ( element.parent() ); 
			    else if ( element.is("input") ) 
			        error.appendTo ( element.parent() ); 
			    else 
			        error.insertAfter(element); 
				} 
			});
			
			$(".closeOrExpand").click(function(){
        		if($(".closeOrExpand").attr("class")=="ydClose closeOrExpand"){
	        		$(".table-toggel").show();
	        		$(".closeOrExpand").attr("class","ydExpand closeOrExpand");
        		}else{
	        		$(".table-toggel").hide();
	        		$(".closeOrExpand").attr("class","ydClose closeOrExpand");
	        	}
	        });	
			
			$(".selectGroupId").click(function(){
				if($(this).text()=='选择全部'){
	        		$('.cancelGroupId').attr('checked','true');
	        		$(this).text('取消全部');
				}else{
        			$('.cancelGroupId').removeAttr('checked');
        			$(this).text('选择全部');
				}
			});
			
			//附件删除
            inquiryCheckBOXLocal();
		});
			
		jQuery.extend(jQuery.validator.messages, {
		  		required: "必填信息",
		  		digits:"请输入正确的数字",
		  		number : "请输入正确的数字价格"
   			});
		function agentIdChange(selectdata){
				window.location.href = '${ctx}/stock/manager/apartGroup/reserve?id=${param.id}&agentId='+selectdata+'&activityGroupId=${param.activityGroupId }';
			}
		//查询所选择的渠道信息
		function choseAgent(agentId){

			   var selectdata = null;
				   if(agentId){
					   selectdata = agentId;
				   }else{
					   selectdata = $("#agentIdIn option:selected").val();
					   agentIdChange(selectdata);
				   }
			   $.ajax({
			        type: "POST",
			        url: "${ctx}/stock/manager/apartGroup/agentInfo",
			        data: {
			        	agentId:selectdata
			        },
			         success: function(msg){
			        	$(msg).each(function(index1,obj1){
			        		var salerSelect = document.createElement("select");  // 创建销售下拉框
			        		var salerUsersJson = obj1.agentSalerUser;
			                var salerUsers = eval(eval('("' + salerUsersJson + '")'));
			                salerSelect.options.add(new Option("请选择", ''));
			                for ( var j = 0; j < salerUsers.length; j++) {
								var newOpt = new Option(salerUsers[j].name, salerUsers[j].id);
								$(newOpt).attr("salerMobile", salerUsers[j].mobile);
								salerSelect.options.add(newOpt);
							}
							$(salerSelect).attr("id", "salerSelect");
							$(salerSelect).attr("onchange", "fillMobile(this)");
							var _seperate = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
							var _agentName = "<label>渠道商名称：</label>"+ obj1.agentName + _seperate + "<label>接口销售员：</label>";
							var _salerMobile = _seperate + "<label>联系电话：</label><span name='salerMobile' style='padding-left:0px; margin-right:10px;'></span>";
				        	$(".agentInfo").append(_agentName).append(salerSelect).append(_salerMobile); 
			        	});
			         }
			        });
			  $(".jbox-close").click();
			  $(".agentId").val(selectdata);
		   }
		
		/**
		 * 选择销售，填充电话
		 */
		function fillMobile(obj) {
			var mobile = $(obj).find("option:selected").attr("salermobile");
            $("span[name=salerMobile]").empty().text(mobile);
		}
		
		function quitAgent(){
             $(".jbox-close").click();  
		}
		function delGroupDate(obj){
			$(document).delGroup1(obj);
			
			var divobj = $(obj).parent().parent();
			//var input = $(divobj).find("td").first().find("input")[0];
			$(divobj).remove();
		}
		function delgroup(obj){
	    	$(obj).parent().parent().remove();
	    }
	  	function maxVali(obj){
		  		if($("#payReservePosition").val() != null && $("#payReservePosition").val() != "") {
		  			$("#btnSubmit").removeAttr("disabled");
	        	}
				var old = $(obj).parent().prev().val();
				var newVal = $(obj).val();
				var freePosition = $(obj).parent().next().text();
				if(newVal-old>freePosition){
					return false;
				}else{
				return true;
				}
		}
		jQuery.validator.addMethod("intChange", function(value, element) {       
		    return this.optional(element) || maxVali(element);
		 }, "余位不足");   
		jQuery.validator.addMethod("isZero", function(value, element) {   
		    return this.optional(element) || !($(element).val() == 0)
		}, "无切位人数");  
		
		function getChickList(){
			var d = ${fn:length(travelActivity.activityGroups)};
			var dateArr = new Array();
			for(var i=1; i<=d; i++){
				var beforDate = $("#grouprow"+i).find(".leftdays").val();
				if(beforDate<0){
					var date = ($("#grouprow"+i+" td").eq(1).text()).split("-");
					dateArr.push(new Date(date[0],date[1]-1,date[2]));
				}
			}
			return dateArr;
		}
	    function upload(activityGroupId) {
	    	if($('#agentId').val()==''){
	    		$.jBox.tip('请先选择切位渠道！','error');
	    		return false;
	    	}
            var iframe = "iframe:${ctx}/stock/manager/apartGroup/uploadform?srcActivityId=${param.id}&agentId="+$('#agentId').val()+"&activityGroupId="+activityGroupId;
            $.jBox(iframe, {
                    title: "收款凭证",
                    width: 580,
                    height: 460,
                    buttons: {}
          });
          return false;
    }   
        function downloads(docid,activitySerNum,groupCode,acitivityName,iszip){
            if(iszip){
                var zipname = activitySerNum+'-'+groupCode;
                window.open("${ctx}/sys/docinfo/zipdownload/"+docid+"/"+zipname);
            }
                
            else
                window.open("${ctx}/sys/docinfo/download/"+docid);
        }
        
        function cancelGroup() {
        	$(".cancelGroupId").each(function(){
        		if($(this).attr("checked")){
	            	$(this).parent().parent().hide();
	            	$(this).removeAttr("checked");
	            	
        		}
	        });
        	$(".selectGroupId").text('选择全部');
        }
       
        function agendChoose() {
        	if(${isRequested}){
				var _select = $("#agentId").clone();
		            	_select.attr("id","agentIdIn");
		            	_select.attr("name","agentIdIn");
		            	_select.unbind();
		       var $select=$('<p></p>').append(_select);     	
			   var html= '<div id="chooseQd" class="tanchukuang"><div class="add_allactivity choseAgents"><p style="line-height:60px;text-align:center;">共有'+${fn:length(agentinfoList) }+'家渠道为您服务，请选择渠道为其切位</p><p>'+"<label>渠道选择：</label>"+  $select.html()+'</p></div></div>'

			            $.jBox(html, { title: "切位-选择渠道", buttons:{'取消':0,'提交': 1},submit: submit,height:350,width:400,persistent:true});
			     $(".jbox-close").hide();
			}else{
				choseAgent(${agentId});
			}
        }
        
        var submit = function (v, h, f) {         
           if(v==1) {
              choseAgent(); 
           } else {
              history.go(-1);
           }                         
        }

        function formSubmit() {
            var payType= $("#payType").val();                
            if (payType==null || payType=="-1"){ 
              alert("请选择收款方式");
              return;
            }
        	var agentId = $("#agentId").val();
        	if(agentId!=null&&agentId!=""){
        	var isBreak = false;
            $("input[name=frontMoney]").each(function(index, element){
	           if($(element).val().length > 8) {
	           		$.jBox.tip('定金不能大于8位数','error');
	           		$(element).val('');
	           		isBreak = true;
	           		return;
	           } 
            });
            // 如果条件不满足，则阻断提交
        	if (isBreak) {
				return;
			}
            var reservation= $("#reservation").val();
            var remark= $("#remark").val();
           if(reservation.length > 26) {alert("预订人姓名过长");return false;} 
           if(remark.length > 200) {alert("备注不能超过200个字符");return false;} 
           if(!$("#salerSelect").val() || $("#salerSelect").val() == "0") {$.jBox.tip('请选择接口销售员！','error');return false;}
        		$("#modForm").submit();
        	}else{
        		agendChoose();
        	}
        	if($("#payReservePosition").val() != null && $("#payReservePosition").val() != "") {
        		$("#btnSubmit").attr({"disabled":"disabled"});
        	}
        }


    function checkMoney(v,obj)
    {
       var a=/^[0-9]*(\.[0-9]{1,2})?$/;
       if(!a.test(v))
       {
	       	top.$.jBox.tip('金额格式不正确','warning');
	        $("#"+obj+"").val('');
	        $("#user_id").val("");
	        return false;
		} 
    }

     function clean(id)
    { 
      $("#frontMoney"+id).val("");
      $("#payReservePosition").val("");
      $("#reservation").val("");
      $("#remark").val("");
      //清空支付方式
      $("#payType option[value='-1']").attr("selected",true);
      $("#btnSubmit").removeAttr("disabled");
      return true;
    }
     function resetBtn(){
    	 $("#btnSubmit").removeAttr("disabled");
     }

    //下载文件
 	function downloads(docid){
 		window.open("${ctx}/sys/docinfo/download/"+docid);
     }
     //上传文件后的名称回显
     function commenFunction(obj,fileIDList,fileNameList,filePathList) {
 		var fileIdArr = fileIDList.split(";");
 		var fileNameArr = fileNameList.split(";");
 		var filePathArr = filePathList.split(";");
 		for(var i=0; i<fileIdArr.length-1; i++) {
				//<span><a href="javascript:void(0);" title="点击下载附件">0001.jpg</a></span>
    			var html = [];
    			html.push('<span>');
    			html.push('<a href="javascript:downloads('+ fileIdArr[i] +');" title="点击下载附件">'+ fileNameArr[i] +'</a>');
    			html.push('<input type="hidden" name="docId"  value="' + fileIdArr[i] + '" />');
    			html.push('<input type="hidden" name="docName" value="'+ fileNameArr[i] +'"/>');
    			html.push('<input type="hidden" name="docPath" value="'+ filePathArr[i] +'"/>');
    			html.push('</span>');
 			$(obj).parent().find("span.seach_checkbox_user").append(html.join(''));
 		}
     }
     
	</script>
</head>
<body>
        <page:applyDecorator name="stock_op_head">
            <page:param name="current">activityStock</page:param>
        </page:applyDecorator>
        <div class="ydbzbox fs">
	<form:form id="modForm" modelAttribute="travelActivity" action="${ctx}/stock/manager/apartGroup/doreserve" method="post" class="form-search">
	<input type="hidden" id="id" name="id" value="${travelActivity.id }"/>
    <c:if test="${not empty param.activityGroupId}">
      <input type="hidden" id="id" name="groupId" value="${param.activityGroupId }"/>
    </c:if>

<%--	<div class="mod_nav departure_title">库存切位</div>--%>
    <div class="orderdetails_tit">
			<span>1</span>
			产品信息
		</div>
            <p class="ydbz_mc">${travelActivity.acitivityName }</p>
            <div class="orderdetails1">                   
                   <span>出发地：${travelActivity.fromAreaName }</span>
                   <span>产品编号：${travelActivity.activitySerNum }</span>
                   <span>行程天数：${travelActivity.activityDuration }</span>
            <span  class="departure_td_width" style="display: none;">切位渠道：
            <select id="agentId" name="agentId" class="agentId">
               <c:forEach var="agentinfo" items="${agentinfoList }">
               <option value="${agentinfo.id }" >${agentinfo.agentName }</option>
               </c:forEach>
            </select>
            </span>
            <span class="agentInfo"></span>
            
            <a href="javascript:void(0)" onclick="window.location.hash='view';$('.table-toggel').show();$('.closeOrExpand').attr('class','ydExpand closeOrExpand')">查看全部产品占位</a>
            </div>
       <div class="orderdetails_tit">
			<span>2</span>
			申请切位
			</div>
            <textarea name="dateList" id="dateList" style="display: none;"></textarea>
            <c:if test="${empty param.activityGroupId}">
           	<div class="orderdetails1_bt"><div class="add2_line_text">请选择出团日期月份：</div></div>
           	<input id="groupOpenDate" class="inputTxt inputGroupDate" name="groupOpenDateBegin" readonly value="<fmt:formatDate pattern="yyyy-MM-dd" value="${travelActivity.groupOpenDate }"/>"/> 至 <input id="groupCloseDate" class="inputTxt inputGroupDate" name="groupCloseDateEnd" readonly value="<fmt:formatDate pattern="yyyy-MM-dd" value="${travelActivity.groupCloseDate }"/>"/>
            <a class="groupDate">选择团期</a>&nbsp;<a class="departure_add" href="javascript:void(0);" onclick="javascript:showAll();">显示全部</a>
            </c:if>
       
        <div class="orderdetails1_bt">
			<div class="add2_line_text">填写团期价格信息：</div>
		</div>
 		<jsp:useBean id="now" class="java.util.Date"></jsp:useBean>
		<table class="table table-striped table-bordered table-condensed table-mod2-group">
		<thead>
			<tr>
				<!--<th width="4%" rowspan="2"></th> -->
				<th width="7%" rowspan="2">出团日期</th>
					<th width="6%" rowspan="2">订金(元)</th>
					<th width="6%" rowspan="2">切位人数</th>
					<th width="4%" rowspan="2">余位</th>
					<th width="6%" rowspan="2">预订人</th>
					<th width="18%" colspan="3" class="t-th2">同行价</th>
					<th width="6%" rowspan="2">单房差</th>
					<th width="6%" rowspan="2">预收</th>					
					<th width="8%" rowspan="2">收款时间</th>
					<th width="6%" rowspan="2">收款方式</th>
					<!--<th width="5%" rowspan="2">支付凭证</th> -->
					<th width="6%" rowspan="2">备注</th>
					<th width="5%" rowspan="2">操作</th>
				</tr>
				<tr>
					<th width="6%">成人</th>
					<th width="6%">儿童</th>
					<th width="6%">特殊人群</th>
			</tr>
		</thead>
		<tbody>
		 <c:set var="ex" value="false"></c:set>
		<c:forEach items="${travelActivity.activityGroups}" var="group" varStatus="status">
		<c:set var="leftDay" value="${group.leftdays}"></c:set>
		<%--<c:if test="${group.activityGroupReserve==null }"> --%> 
			<c:if test="${group.id==param.activityGroupId or empty param.activityGroupId}">

			<c:set var="ex" value="true"></c:set>
			<tr class="grouprow" id="grouprow${status.count }" >
				<!--<td ><c:if test="${group.leftdays < 0 }"><input type="checkbox" name="" class="cancelGroupId" value="${group.id }"/></c:if></td>-->
				<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/></td>
                 <td class="tc">
                 <input type="hidden" value="${group.leftdays}" class="leftdays"/>
                 <c:if test="${group.leftdays > 0 }">
                   <input type="hidden" name="reserveid" value="${group.activityGroupReserve.id }" ><input type="hidden" id="groupid" name="activityGroupId" value="${group.id }" >               
                   <input type="text" id="frontMoney${status.count}" name="frontMoney" maxlength="10"  onblur="checkMoney(this.value, 'frontMoney${status.count}');"/>		
				  </c:if>
                 <c:if test="${group.leftdays <= 0 }">
                 <label title="已过期，无法进行相关操作" style="color:#eb0205">已过期</label>
                 </c:if>
                 </td>
                 <td class="tc">
                 <c:if test="${group.leftdays > 0 }">
<%--                 <a href="javascript:void(0)" class="attend_down">‐</a>--%>
                 <input type="text" id="payReservePosition" name="payReservePosition" class="required intChange isZero duration" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
<%--                 <a href="javascript:void(0)" class="attend_up">+</a>--%>
                 </c:if>
                 <c:if test="${group.leftdays <= 0 }">
                 <label title="已过期，无法进行相关操作" style="color:#eb0205">已过期</label>
                 </c:if>
                 </td>
                 <td class="tc">${group.freePosition}</td>
                 <td class="tc">
                 <c:if test="${group.leftdays > 0 }">                
                   <input name="reservation" type="text" id="reservation" maxlength="10"  onkeyup="value=value.replace(/[^\a-zA-Z\u4E00-\u9FA5]/g,'')"/>              
                 </c:if>
                 <c:if test="${group.leftdays <= 0 }">
				 <label title="已过期，无法进行相关操作" style="color:#eb0205">已过期</label>
				 </c:if>
                 </td>
                 <td class="tc">${fns:getCurFlagByIndexAndCurArray(group.currencyType,0,0)}${group.settlementAdultPrice}</td>
                 <td class="tc">${fns:getCurFlagByIndexAndCurArray(group.currencyType,1,0)}${group.settlementcChildPrice}</td>
                 <td class="tc">${fns:getCurFlagByIndexAndCurArray(group.currencyType,2,0)}${group.settlementSpecialPrice}</td>
                 <td class="tc">${fns:getCurFlagByIndexAndCurArray(group.currencyType,6,0)}${group.singleDiff}</td>
                 <td class="tc">${group.planPosition}</td>
               
                 <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${now}"/></td>
                 <td class="tc">
                 <c:if test="${group.leftdays > 0 }">                
                 <select name="payType" id="payType">
                 	<option value="-1" selected>请选择</option>
                 	<c:forEach items="${fns:getDictList('offlineorder_pay_type')}" var="dict">
                 		<option value="${dict.value }">${dict.label }</option>
                 	</c:forEach>
                 	<c:if test="${fns:getUser().company.uuid eq '7a81a26b77a811e5bc1e000c29cf2586'}">
                 		<option value="9">因公支付宝</option>
                 	</c:if>
                 </select>               
                 </c:if>
                 <c:if test="${group.leftdays <= 0 }">
				 <label title="已过期，无法进行相关操作" style="color:#eb0205">已过期</label>
				 </c:if>
                 </td>
                 <!--
                 <td class="tc">
                 <c:if test="${group.leftdays < 0 }"><a href="javascript:void();" onclick="javascript:return upload(${group.id });">上传</a>
                 </c:if>
                 <c:if test="${group.leftdays >= 0 }">
				 <label title="已过期，无法进行相关操作" style="color:#eb0205">已过期</label>
				 </c:if>
                 </td> -->

                  <td class="tc">
                 <c:if test="${group.leftdays > 0 }">                
                 <input type="text" name="remark" id="remark"/>            
                 </c:if>
                 <c:if test="${group.leftdays <= 0 }">
                 <label title="已过期，无法进行相关操作" style="color:#eb0205">已过期</label>
                 </c:if>
                 </td>
                 <td class="tc">
                 <a href="javascript:void(0)" onclick="javascript:clean(${status.count })">清空</a>
                 </td>
			</tr>
			</c:if> 
			
			<%--</c:if> --%>
		</c:forEach>
		<c:if test="${not ex }">
			<tr><td colspan="16"><div class="wtjqw">此渠道对于该产品已有切位记录</div></td></tr>
			</c:if>
		</tbody>
	</table>
	<c:if test="${ex }">
		<!--<a class="departure_add selectGroupId" href="javascript:void(0)" >选择全部</a>
		<a class="departure_add" href="javascript:void(0)" onclick="cancelGroup()">删除勾选</a>  --> 




    <!-- 上传文件 -->
                                <div class="">
									<div class="orderdetails_tit">
										<span>3</span>上传切位收款凭证
									</div>
			
									<div style="text-align: left">
										<strong>上传切位收款凭证：</strong>
										<input type="button" name="passport" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}','',this,'false');"> 
										<span class="fileLogo"></span> 
										<span class="seach_checkbox_user">
											
										</span>
									</div>
									<input  type="hidden" name="srcActivityId" value="${srcActivityId }"/>
						            <input  type="hidden" name="activityGroupId" value="${activityGroupId }"/>
						            <input  type="hidden" name="agentId" value="${agentId }"/>
						            <input  type="hidden" name="reserveOrderId" value="${reserveOrderId }"/>
                                </div>


    <!-- 上传文件 -->

	<div class="release_next_add">
<%--		<input id="btnCancel" class="btn btn-primary" type="button" value="取消申请" onClick="javascript:location.reload();"/>&nbsp;--%>
            
				 <c:if test="${leftDay > 0 }"> 
                   <input id="btnSubmit" class="btn btn-primary" type="button" onClick="formSubmit();" value="提交申请"  />&nbsp;
                 </c:if>
                 <c:if test="${leftDay <= 0 }">
                   	已经过期,不能切位了
                 </c:if>
	</div>
	</c:if>
		<a id="view"></a>
		<div id="secondStepTitle" style="margin:10px 0 0 0%;"class=" ydbz_tit">
		<span><span class="ydClose closeOrExpand"></span></span>
		<span>渠道已切位团期</span>
		</div>
        <table class="table table-striped table-bordered table-condensed table-mod2-group table-toggel" style="margin-top:10px; display: none;">
        <thead>
        <tr>
            	    <th width="8%" rowspan="2">出发日期</th>
					<th width="8%" rowspan="2">订金(元)</th>
					<th width="8%" rowspan="2">切位人数</th>
					<!--<th width="7%" rowspan="2">余位</th> -->
					<th width="8%" rowspan="2">预订人</th>
					<th width="18%" colspan="3" class="t-th2">同行价</th>
					<th width="8%" rowspan="2">预收</th>					
					<th width="12%" rowspan="2">收款时间</th>
					<th width="7%" rowspan="2">收款方式</th>
					<th width="7%" rowspan="2">收款凭证</th>
					<th width="6%" rowspan="2">备注</th>
                    <!-- <th width="6%" rowspan="2">操作</th> -->
				</tr>
				<tr>
					<th width="6%">成人</th>
					<th width="6%">儿童</th>
					<th width="6%">特殊人群</th>
			</tr>
            </tr>
        </thead>
        <tbody><fmt:formatNumber type="currency" pattern="#,##0.00" value="" />
        <c:set var="exsit" value="true"></c:set>
        <c:forEach items="${reserveList}" var="reserve" varStatus="status">        
            <c:set var="exsit" value="false"></c:set>
            <tr>
                <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${reserve.groupOpenDate}"/></td>
                <td class="tc">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${reserve.orderMoney}" /></td>
                <td class="tc">${reserve.payReservePosition }</td>
                
                <td class="tc">${reserve.reservation }</td>
                <td class="tc"><c:if test="${not empty reserve.settlementAdultPrice}">${fns:getCurFlagByIndexAndCurArray(reserve.currencys,0,0)}</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${reserve.settlementAdultPrice}" /></td>
                <td class="tc"><c:if test="${not empty reserve.settlementcChildPrice}">${fns:getCurFlagByIndexAndCurArray(reserve.currencys,1,0)}</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${reserve.settlementcChildPrice}" /></td>
                <td class="tc"><c:if test="${not empty reserve.settlementSpecialPrice}">${fns:getCurFlagByIndexAndCurArray(reserve.currencys,2,0)}</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${reserve.settlementSpecialPrice}" /></td>
                <td class="tc">${reserve.planPosition}</td>
              
                <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${reserve.createDate}"/></td>
                <td>${reserve.label}</td>
                <td class="tc">
                	 <c:if test="${!empty  reserve.filelist }">
                    <a href="javascript:void(0)" onClick="downloads('${reserve.filelist }','stock${activityGroupId}','${agentId }','',true)">下载</a>
                </c:if>  
                </td>
                <td>${reserve.remark }</td>
            </tr>            
        </c:forEach>
         <c:if test="${exsit}">
            <tr><td colspan="14"><div class="wtjqw">未添加已切位团期</div></td></tr>
         </c:if>
        </tbody>
    </table>
      <br><br>
      <!--
      <div style="color:#333; font-size:16px;font-weight:bold;margin:10px 0 0 0;">
			&nbsp;&nbsp;切位动态：
			</div>
    <table style="margin-top:10px;">   
     <c:if test="${not empty listReserveFull}">
     <c:forEach  var="list" items="${listReserveFull}" varStatus="status" >          	
              <tr style=" height:30px">
                     <td><span><fmt:formatDate value="${list.logReserve.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>【${list.agentName}_${list.createByName}】对${list.groupCode} 团 ${list.logReserve.detail}</span></td>               
		      </tr>
    </c:forEach>
   </c:if>
 </table>  -->
</form:form>	
	</div>
</body>
</html>
