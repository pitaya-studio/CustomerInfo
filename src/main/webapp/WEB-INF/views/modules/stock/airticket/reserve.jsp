<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机票产品切位</title>
<meta name="decorator" content="wholesaler"/>
<style type="text/css">
.sort {
	color: #0663A2;
	cursor: pointer;
}
.ui-autocomplete.ui-menu.ui-front {
	z-index: 10000;
}
</style>

	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script src="${ctxStatic}/common/jquery.date.format.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactorStock.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
    <style type="text/css">
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
           $('.handle').hover(function() {
            if(0 != $(this).find('a').length){
              $(this).addClass('handle-on');
              $(this).find('dd').addClass('block');
            }
           },function(){$(this).removeClass('handle-on');$(this).find('dd').removeClass('block');});

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


		   function agendChoose() {
        	if(${isRequested}){
				var _select = $("#agentId").clone();
		            	_select.attr("id","agentIdIn");
		            	_select.attr("name","agentIdIn");
		            	_select.unbind();			
                var $select=$('<p></p>').append(_select);
			    var html= '<div id="chooseQd" class="tanchukuang"><div class="add_allactivity choseAgents"><p style="line-height:60px;text-align:center;">共有'+${fn:length(agentinfoList) }+'家渠道为您服务，请选择渠道为其切位</p><p>'+"<label>渠道选择：</label>"+  $select.html()+'</p></div></div>'

			    $.jBox(html, { title: "切位-选择渠道", buttons:{'取消':0, '提交': 1},submit: mysubmit,height:350,width:400,persistent:true});
			     $(".jbox-close").hide();
			}else{
				choseAgent(${agentId});
			}
        }

        var mysubmit = function (v, h, f) {
           if(v==1) {
              choseAgent(); 
           } else {
               window.location.href = '${ctx}/stock/manager/airticket/?agentId=';
              //history.go(-1);
           }         
        }

        function quitAgent(){
             $(".jbox-close").click();  
		}
        

		function agentIdChange(selectdata){
				window.location.href = '${ctx}/stock/manager/airticket/reserve?id=${airticketId}&agentId='+selectdata;
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
            var iframe = "iframe:${ctx}/stock/manager/airticket/uploadform?srcActivityId=${airticketId}&agentId="+$('#agentId').val()+"&activityGroupId="+activityGroupId;
            $.jBox(iframe, {
                    title: "支付凭证",
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
                  //window.open("${ctx}/sys/docinfo/zipdownload/119,118/AA");
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
        
	function checkMoney(v,obj)
    {
       var a=/^[0-9]*(\.[0-9]{1,2})?$/;
       if(!a.test(v))
       {
	       	top.$.jBox.tip('金额格式不正确','warning');
	        $("#"+obj+"").val('');
	        return false;
		} 
    }
       
     
        function formSbumit() {
             var payType= $("#payType").val(); 
            if (payType==null || payType==""){ 
              alert("请选择支付方式");
              return;
            }
        	var agentId = $("#agentId").val();
        if(agentId!=null&&agentId!=""){             
                        
           /*  var form = $("#modForm");
            $.ajax({
             url: form.attr('action'),
             type: "POST",
             data: form.serialize(),
          success: function (data) {  
          
          },
           error: function (jqXhr, textStatus, errorThrown) {
          },
       
           }); */

       var frontMoney= $("#frontMoney").val();
       var reservation= $("#reservation").val();
       var remark= $("#remark").val();
       var isBreak = false;
       $("input[name=frontMoney]").each(function(index, element){
           if($(element).val().length > 7) {
           		$.jBox.tip('定金不能大于7位数','error');
           		$(element).val('');
           		isBreak = true;
           		return;
           } 
       });
       // 如果条件不满足，则阻断提交
       	if (isBreak) {
			return;
		}
       if(reservation.length > 10) {alert("预订人姓名过长");return false;} 
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

        function returnReserve(mess, airticketId,leftFontMoney,freePosition) {
               
                 requestActivityGroupId=airticketId;              
                 var option = '<div style="margin-top:20px; padding-left:0px;">';
                 option += '<p>当前余位数量：'+ freePosition +'</p><dl style="overflow:hidden; padding-right:5px;"><dd style=" margin:0; float:left; width:100%;"><table class="table activitylist_bodyer_table"><thead><tr><th width="25%">序号</th><th width="25%">经销商</th><th width="25%">切位</th><th width="25%">已售出切位</th></tr></thead>'; 
                 $.ajax({
                        type: "POST",
                        async: false,
                        url: "${ctx}/stock/manager/airticket/getReserveByGroupId?dom=" + Math.random(),
                        data: {
                            airticketId : airticketId
                        },
                        success: function(msg) {                          
                        activityGroupList=msg;
                        for(var i=0;i<msg.length;i++){
                            option=option+'<tr><td><input name="radioid"  id="radioid" type="radio" value="'+msg[i].agentId+"~"+msg[i].leftpayReservePosition+"~"+msg[i].soldPayPosition+'" style="margin:0;" />'+(i+1)+'</td><td> '+msg[i].agentName+'</td><td> '+msg[i].leftpayReservePosition+'</td><td> '+msg[i].soldPayPosition+'</td></tr>';
                         }                   
                        }
                    });
                    option+="</table></div>";           
            
            var mess= '<div class="msg" style="margin-left:10px"><div class="field">'+option+
            '</div>'+
            '<p>归还切位数量：</p><div class="field"><input type="text" style="width:150px" id="reserveBackAmount" name="reserveBackAmount" value="" /></div>  ' +  
            '<div class="errorBlock" style="display: none;color:#FF0000"></div><BR> '+ 
             '<p>请填写您的还位原因：</p><div class="field"><textarea name="inputtext"'+
             ' id="inputtext"  rows="3" cols="20"></textarea></div>  ' +             
            '</div>'
          $.jBox(mess, { title: "切位归还",  buttons:{'提交': 1,'取消':0},submit: submit,width:400,persistent:true});
            
        }

         var submit = function (v, h, f) {
          if(v==1){ 
             var New=document.getElementsByName("radioid");
             //var tt=f.radioid;             
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
                     strs=strNew.split("~"); //字符分割                       
                     requestAgentId=strs[0]; 
                     maxReserveNum =strs[1];   
                     soldPayPosition =strs[2];                        
                     find=true;
                    break;
               }
             } 
             if(find==false) {
              $(".errorBlock").html("请选择切位记录").show();
              return false;
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
                    url: "${ctx}/stock/manager/airticket/returnReserve?dom=" + Math.random(),
                    data: {
                        airticketId : requestActivityGroupId,
                        agentId : requestAgentId,
                        reserveBackAmount:reserveBackAmount,
                        fontMoneyBackAmount:1, //fontMoneyBackAmount
                        returnRemark:inputtext                        
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
           }
	</script>
	
	<script type="text/javascript">
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
            <page:param name="current">flightStock</page:param>
        </page:applyDecorator>

        <div class="ydbzbox fs">
	<form id="modForm" action="${ctx}/stock/manager/airticket/doreserve" method="post" class="form-search">
	<input type="hidden" id="id" name="id" value="${airticketId }"/>
<%--	<div class="mod_nav departure_title">库存切位</div>--%>
    <div class="orderdetails_tit"><span>1</span>机票产品信息</div>
       
      <div class="mod_information_dzhan_d mod_details2_d" style="overflow:hidden">

        <p class="ydbz_mc"> 产品编号: ${activityAirTicket.productCode}, ${activityAirTicket.departureCityLabel() }&mdash;<c:forEach items="${areas}" var="arrivedcity">
                    <c:if test="${arrivedcity.id eq activityAirTicket.arrivedCity}">
                     ${arrivedcity.name}：
                    </c:if>
                   </c:forEach>  
            <c:if test="${ activityAirTicket.airType ==1}">多段</c:if>
            <c:if test="${ activityAirTicket.airType ==2}">往返</c:if>
            <c:if test="${ activityAirTicket.airType ==3}">单程</c:if>
               </p>
    <span class="agentInfo"></span> &nbsp;<a href="javascript:void(0)" onclick="window.location.hash='view';$('.table-toggel').show();$('.closeOrExpand').attr('class','ydExpand closeOrExpand')">查看全部产品占位</a>   
<c:if test="${ activityAirTicket.airType ==1}">
       <c:forEach items="${activityAirTicket.flightInfos}" var="flightInfos" varStatus="s">
            <div class="title_samil">第${flightInfos.number}段：<c:if test="${flightInfo.ticket_area_type eq '3'}">内陆</c:if>
                            <c:if test="${flightInfo.ticket_area_type eq '2'}">国际</c:if>
                            <c:if test="${flightInfo.ticket_area_type eq '1'}">内陆+国际</c:if></div>
            <table border="0" width="90%">
                                <tbody><tr>
                                    <td class="mod_details2_d1">出发机场：</td>
                                    <td class="mod_details2_d2">
                            <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq flightInfos.leaveAirport}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                                      </td>
                                    <td class="mod_details2_d1">到达机场：</td>
                                    <td class="mod_details2_d2">
                            <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq flightInfos.destinationAirpost}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                               </td>
                                 </tr>
                                 <tr>
                                    <td class="mod_details2_d1">出发时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${flightInfos.startTime }"/></td>
                                    <td class="mod_details2_d1">到达时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${flightInfos.arrivalTime }"/></td>
                                    <td class="mod_details2_d1">航班号：</td>
                                    <td class="mod_details2_d2">${flightInfos.flightNumber }</td>               
                                   </tr>
                                    <tr>
                                    <td class="mod_details2_d1">航空公司：</td>
                                    <td class="mod_details2_d2">${flightInfos.airlinesLabel() eq '' ? '不限' : flightInfos.airlinesLabel()}</td>
                                    <td class="mod_details2_d1">舱位等级：</td>
                                    <td class="mod_details2_d2">${flightInfos.spaceGradeLabel() }</td>		
                                    <td class="mod_details2_d1">舱位：</td>
                                    <td class="mod_details2_d2">${flightInfos.airspaceLabel() }</td>
                                 </tr>
            </table>
       </c:forEach>
    </c:if>


<c:if test="${ activityAirTicket.airType ==2}">
       <div class="title_samil">去程：</div>
              <table border="0" width="90%">
                                <tbody><tr>
                                       <td class="mod_details2_d1">出发机场：</td>
                                    <td class="mod_details2_d2">
                            <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq activityAirTicket.flightInfos[0].leaveAirport}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                                      </td>
                                    <td class="mod_details2_d1">到达机场：</td>
                                    <td class="mod_details2_d2">
                            <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq activityAirTicket.flightInfos[0].destinationAirpost}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                               </td>
                           </tr>
                                 <tr>
                                    <td class="mod_details2_d1">出发时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[0].startTime }"/></td>
                                    <td class="mod_details2_d1">到达时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[0].arrivalTime }"/></td>
                                    <td class="mod_details2_d1">航班号：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].flightNumber }</td>               
                                   </tr>
                                    <tr>
                                    <td class="mod_details2_d1">航空公司：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].airlinesLabel() eq '' ? '不限' : activityAirTicket.flightInfos[0].airlinesLabel()}</td>
                                    <td class="mod_details2_d1">舱位等级：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].spaceGradeLabel() }</td>		
                                    <td class="mod_details2_d1">舱位：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].airspaceLabel() }</td>
                                 </tr>
                           </table>
                                 <div class="title_samil">返程：</div>
                                  <table border="0" width="90%">
                                 <tbody><tr>
                                    <td class="mod_details2_d1">出发机场：</td>
                                    <td class="mod_details2_d2">
                             <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq activityAirTicket.flightInfos[1].leaveAirport}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                              </td>
                             <td class="mod_details2_d1">到达机场：</td>
                             <td class="mod_details2_d2">
                             <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq activityAirTicket.flightInfos[1].leaveAirport}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                                     </td>
                                 </tr>
                                 <tr>
                                    <td class="mod_details2_d1">出发时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[1].startTime }"/></td>
                                    <td class="mod_details2_d1">到达时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[1].arrivalTime }"/></td>
                                    <td class="mod_details2_d1">航班号：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[1].flightNumber }</td>               
                                   </tr>
                                    <tr>
                                    <td class="mod_details2_d1">航空公司：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].airlinesLabel() eq '' ? '不限' : activityAirTicket.flightInfos[0].airlinesLabel() }</td>
                                    <td class="mod_details2_d1">舱位等级：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[1].spaceGradeLabel() }</td>		
                                    <td class="mod_details2_d1">舱位：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[1].airspaceLabel() }</td>
                                 </tr>
                    </table>
  </c:if>


<c:if test="${ activityAirTicket.airType ==3}">
       <table border="0" width="90%">
                                <tbody><tr>
                                <td class="mod_details2_d1">出发机场：</td>
                                <td class="mod_details2_d2">
                                <c:forEach items="${airportlist}" var="airportlist">
                                <c:if test="${airportlist.id eq activityAirTicket.flightInfos[0].leaveAirport}">
                                ${airportlist.airportName}
                                </c:if>
                                </c:forEach>
                                   </td>
                                    <td class="mod_details2_d1">到达机场：</td>
                                    <td class="mod_details2_d2">
                                   <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq activityAirTicket.flightInfos[0].destinationAirpost}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                                 </td> 
                                 </tr>
                                 <tr>
                                    <td class="mod_details2_d1">出发时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[0].startTime }"/></td>
                                    <td class="mod_details2_d1">到达时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[0].arrivalTime }"/></td>
                                    <td class="mod_details2_d1">航班号：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].flightNumber }</td>               
                                   </tr>
                                    <tr>
                                    <td class="mod_details2_d1">航空公司：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].airlinesLabel() eq '' ? '不限' : activityAirTicket.flightInfos[0].airlinesLabel()}</td>
                                    <td class="mod_details2_d1">舱位等级：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].spaceGradeLabel() }</td>		
                                    <td class="mod_details2_d1">舱位：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].airspaceLabel() }</td>
                                 </tr>
                    </table>
  </c:if>
  </div>
            
           <!-- <a href="javascript:void(0)" onclick="window.location.hash='view';$('.table-toggel').show();$('.closeOrExpand').attr('class','ydExpand closeOrExpand')">查看全部产品占位</a>  -->
          
       <div class="orderdetails_tit">
			<span>2</span>
			申请切位
			</div>
            <textarea name="dateList" id="dateList" style="display: none;"></textarea>
       
        <div class="orderdetails1_bt">
			<div class="add2_line_text">填写机票价格信息：</div>
		</div>
 		<jsp:useBean id="now" class="java.util.Date"></jsp:useBean>

		<table class="table table-striped table-bordered table-condensed table-mod2-group">
		<thead>
			<tr>    
				    <th width="7%" rowspan="2">出发日期</th>
					<th width="6%" rowspan="2">订金(元)</th>
					<th width="6%" rowspan="2">切位人数</th>
					<th width="4%" rowspan="2">余位</th>
					<th width="6%" rowspan="2">预订人</th>
					<th width="18%" colspan="3" class="t-th2">同行价</th>
					<th width="6%" rowspan="2">预收</th>					
					<th width="8%" rowspan="2">收款时间</th>
					<th width="6%" rowspan="2">收款方式</th>
					<!--<th width="5%" rowspan="2">支付凭证</th> -->
					<th width="6%" rowspan="2">备注</th>
				</tr>
				<tr>
					<th width="6%">成人</th>
					<th width="6%">儿童</th>
					<th width="6%">特殊人群</th>
			</tr>
		</thead>
		<tbody>
		<%--<c:if test="${group.activityGroupReserve==null }"> --%>
      
			<tr  class="grouprow" id="grouprow1">
              
                <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${activityAirTicket.startingDate}"/></td>   
                 <td>
                <input type="hidden" id="id" name="id" value="${airticketId }" >
                <input type="hidden" id="reserveid" name="reserveid" value="${airticketReserve.id }" > 
                <input type="text" id="frontMoney${status.count}" name="frontMoney" onblur="this.value=this.value.replace(/\D/g,'');checkMoney(this.value, 'frontMoney${status.count}');" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
				 </td>
                 <td>
                 <input type="text" id="payReservePosition" name="payReservePosition"  class="required intChange isZero duration" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
                 </td>
                 <td class="tr">${activityAirTicket.freePosition}</td>
                 <td>                
                 <input type="text" id="reservation" name="reservation" onkeyup="value=value.replace(/[^\a-zA-Z\u4E00-\u9FA5]/g,'')" />                 
                </td>
                 <td class="tr">${fns:getCurrencyNameOrFlag(activityAirTicket.currency_id, '0')}${activityAirTicket.settlementAdultPrice}</td>
                 <td class="tr">${fns:getCurrencyNameOrFlag(activityAirTicket.currency_id, '0')}${activityAirTicket.settlementcChildPrice}</td>
                 <td class="tr">${fns:getCurrencyNameOrFlag(activityAirTicket.currency_id, '0')}${activityAirTicket.settlementSpecialPrice}</td>
                 <td class="tr">${activityAirTicket.reservationsNum}</td>
                
                 <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${now}"/></td>
                 <td class="departure_td_width">             
                 <select id="payType" name="payType">
                 	<option value="">请选择</option>
                 	<c:forEach items="${fns:getDictList('offlineorder_pay_type')}" var="dict">
                 		<option value="${dict.value }">${dict.label }</option>
                 	</c:forEach>
                 	<c:if test="${fns:getUser().company.uuid eq '7a81a26b77a811e5bc1e000c29cf2586'}">
                 		<option value="9">因公支付宝</option>
                 	</c:if>
                 </select>                
                 </td>
                 <!--<td>
                 <a href="javascript:void(0);" onclick="javascript:return upload(${airticketId });">上传</a>
                 </td> -->
                 <td>
                    <input type="text" name="remark" id="remark"/>              
                 </td>             
			</tr>
			
		
		</tbody>
	</table>




    <!-- 上传文件 -->
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


    <!-- 上传文件 -->




		<div class="release_next_add">
           <input id="btnSubmit" class="btn btn-primary" type="button" onClick="formSbumit()" value="提交申请"  />&nbsp;
	</div>

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
                    <th width="6%" rowspan="2">操作</th> 
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
        <c:forEach items="${airticketReserveList}" var="reserve" varStatus="status">
            <c:if test="${reserve.agentId == agentId }">
            <c:set var="exsit" value="false"></c:set>
            <tr>
            	 <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${reserve.startingDate}"/></td>              
                <td class="tc">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${reserve.orderMoney}" /></td>
                <td class="tc">${reserve.payReservePosition }</td>
                
                <td class="tc">${reserve.reservation }</td>
                <td class="tc"><c:if test="${not empty reserve.settlementAdultPrice}">${fns:getCurrencyNameOrFlag(activityAirTicket.currency_id, '0')}</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${reserve.settlementAdultPrice}" /></td>
                <td class="tc"><c:if test="${not empty reserve.settlementcChildPrice}">${fns:getCurrencyNameOrFlag(activityAirTicket.currency_id, '0')}</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${reserve.settlementcChildPrice}" /></td>
                <td class="tc"><c:if test="${not empty reserve.settlementSpecialPrice}">${fns:getCurrencyNameOrFlag(activityAirTicket.currency_id, '0')}</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${reserve.settlementSpecialPrice}" /></td>
                <td class="tc">${reserve.reservationsNum}</td>
              
                <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${reserve.createDate}"/></td>
                <td class="tc">${reserve.label}</td>
                <td class="tc"> 
                  <c:if test="${!empty  reserve.filelist }">
                    <a href="javascript:void(0)" onClick="downloads('${reserve.filelist }','ticket${airticketId}','${agentId }','',true)">下载</a>
                </c:if>               
                    
                </td>
                <td>${reserve.remark }</td>
                 <td>                 

                 <dl class="handle">
                        <dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
                        <dd>
                            <p>
                                <span></span>                             
                              <a href="javascript:void(0)" onclick="return returnReserve('确认归还切位？', '${activityAirTicket.id }','${activityAirTicket.payReservePosition - activityAirTicket.soldPayPosition}','${activityAirTicket.freePosition}');">归还切位</a>
                                <a target="_blank" href="${ctx}/stock/manager/airticket/detail/${activityAirTicket.id}" >库存详情</a></p>                      
                        </dd>
                    </dl>            
                    </td>           
            </tr>
            </c:if>  
        </c:forEach>
         <c:if test="${exsit}">
            <tr><td colspan="14"><div class="wtjqw">未添加已切位团期</div></td></tr>
         </c:if>
        </tbody>
    </table>
       <span  class="departure_td_width" style="display: none;">渠道选择：
            <select id="agentId" name="agentId" class="agentId">
               <c:forEach var="agentinfo" items="${agentinfoList }">
               <option value="${agentinfo.id }" >${agentinfo.agentName }</option>
               </c:forEach>
            </select>
            </span>

	</form>
	</div>
</body>
</html>
