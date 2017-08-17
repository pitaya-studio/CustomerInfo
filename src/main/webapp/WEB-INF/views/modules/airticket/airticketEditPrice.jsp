
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
   <title>产品-机票产品及发布-填写价格</title>
       <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/modules/forTTS/airticket/airticketEditPrice.js"></script>
	<!-- <style type="text/css">
		#settlementcChildPrice,#settlementAdultPrice,#settlementSpecialPrice,#taxamt,#depositamt{
			padding-left: 20px;
		}
	</style> -->
	<!-- 0258-tgy-校验规则引入的文件 -->
	<script type="text/javascript" src="${ctxStatic}/js/validationRules.js"></script>
    <script type="text/javascript">
    $(function(){
        g_context_url = "${ctx}";
        //选择币种
        selectCurrencyVisa();
        
        inputTips();
        //是否分段报价
		flyDivCheck();
         //是否含税
		isTaxation();
         
         
		//------------------------------------------------------------------- 
	    $("#airticketremark").keyup(function(){
		    var objval = $(this).val();
		    //alert(objval);
		    //先将全角转换成半角(全角括号除外)
	       /*  var tmp = "";
	        for(var i=0;i<objval.length;i++) {
	            if(objval.charCodeAt(i)>65248&&objval.charCodeAt(i)<65375&&objval.charCodeAt(i)!=65288&&objval.charCodeAt(i)!=65289) {
	                tmp += String.fromCharCode(objval.charCodeAt(i)-65248);
	            }else{
	                tmp += String.fromCharCode(objval.charCodeAt(i));
	            }
	        }
	        objval=tmp; */
	        //删除掉规定外的字符
	        objval=objval.replace(/[^\u4e00-\u9fa5\w\(\)\（\）\-\+\\/\—.。，,!！？?“”‘’]/g,'');
	        $(this).val(objval);
		   
	  });
         
    	$('.flyMoreDiv').find('[name="more_currency_id"]').children('option:selected').each(function(){
    		var currency = $(this).attr('addclass'); 
    		$(this).parents('tr:first').next().find('span').each(function(index,obj){
    			if(index ==0 || index ==1 ||index ==2)
    			$(obj).text(currency);
    		})
    		
    	})
    	
    	
    	
    	$('.mod_information').find('[name="currency_id"]').children('option:selected').each(function(){
    		var currency = $(this).attr('addclass'); 
    		$($($(this).parents('tr:first').children()[3]).find('span')[0]).text(currency);
    		$(this).parents('tbody').children().eq(2).find('span').text(currency);
    		$(this).parents('tr:first').next().find('span:not(.xing)').text(currency);
    		
    	})
    	
    	$(".flyMoreDiv").find("tbody").each(function()
    	{
    	    var mark= $('.mod_information').find('[name="currency_id"]').children('option:selected').attr('addclass')
    		$(this).find("tr>td").eq(3).find("span").eq(0).text(mark);
    	}
    	)
    	
    	
		$(".flyMoreDiv").find("tbody>tr>td").eq(3).find("span").eq(0).text()
		
    });
    
    //是否分段报价
	function flyDivCheck(){
		if($("input[name='flyDivInput']").prop("checked")){
		$('.flyMoreDiv').show();
		}else{
		$('.flyMoreDiv').hide();
		}
	}
	//是否符合金额规则
	function isMoney(obj){
				var rr = $(obj).val();
				var rule = /^[^0-9|\.]$/;
				if(rr.length>0){
					var newStr='';
					//过滤掉非字（不过滤小数点）
					for(var i=0;i<rr.length;i++){
						var c = rr.substr(i,1);
						if(!rule.test(c)){
							newStr+=c;
						}
					}
					if(newStr!=''){
						//只能有一个小数点，并去掉多余的0
						var szfds = newStr.split('.');
						var zs = '';
						var xs = '';
						if(szfds.length>1){
							zs=szfds[0];
							xs=szfds[1];
							for(var i=1;i<zs.length;i++){
								var zs_char = zs.substr(0,1);
								if(zs_char=='0'){
									zs = zs.substring(1,zs.length);
								}
							}
							//保留两位小数
							if(xs.length>2){
								xs = xs.substring(0,2);
							}
							newStr = zs+'.'+xs;
						}else{
							zs=szfds[0];
							for(var i=1;i<zs.length;i++){
								var zs_char = zs.substr(0,1);
								if(zs_char=='0'){
									newStr = zs.substring(1,zs.length);
								}
							}
						}
												
						//'.'之前没有数字会自动补0
						if(newStr.indexOf('.')==0){
							newStr='0'+newStr;
						}
					}
					
					
					$(obj).val(newStr);//asdf090980123
				}
			}
    function removeCodeCss(obj){
        $(obj).removeAttr("style");
    }
    function replaceStr(obj) {
    	var selectionStart = obj.selectionStart;
        //先将全角转换成半角(全角括号除外)
        var tmp = "";
        for (var i = 0; i < obj.value.length; i++) {
            if (obj.value.charCodeAt(i) > 65248 && obj.value.charCodeAt(i) < 65375 && obj.value.charCodeAt(i) != 65288 && obj.value.charCodeAt(i) != 65289) {
                tmp += String.fromCharCode(obj.value.charCodeAt(i) - 65248);
            } else {
                tmp += String.fromCharCode(obj.value.charCodeAt(i));
            }
        }
        obj.value = tmp;
        //删除掉规定外的字符
        obj.value = obj.value.replace(/[^\u4e00-\u9fa5\w\(\)\（\）\-\+\\/\—\\]/g, '');
        //设置光标的位置
        if(obj.setSelectionRange)
        {
            obj.focus();
            obj.setSelectionRange(selectionStart,selectionStart);
        }
        else if (obj.createTextRange) {
            var range = obj.createTextRange();
            range.collapse(true);
            range.moveEnd('character', selectionStart);
            range.moveStart('character', selectionStart);
            range.select();
        }


        $("#groupCode").attr("title", $("#groupCode").val());
    }
    
    
    /**
     * 
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
                    $(obj).val($(obj).val().slice(0, 50));
				}
				return false;  
    		}
    }
    
    //C460 下一步前给出修改团号提示 
    function beforeSecondToThird(){
    	var groupCode = $("#groupCode").val();//当前团号
    	var groupCodeOld = $("#groupCodeOld").val();//原有团号
    	var currentChildrenNum = $("#orderPersonNumChild").val();
    	var currentPeopleNum = $("#orderPersonNumSpecial").val();
    	var wangtNum = $("#reservationsNum").val();
    	var maxPeopleCount = $("#maxPeopleCount").val();
    	var maxChildrenCount = $("#maxChildrenCount").val();
    	if(parseInt(maxPeopleCount)<parseInt(currentPeopleNum)){
   		 	top.$.jBox.info("\"特殊人群最高人数\"不能小于\"已占位特殊人群人数\""+currentPeopleNum+"", "警告");
       	   	return;
    	}else if(parseInt(maxChildrenCount)<parseInt(currentChildrenNum)){
    		top.$.jBox.info("\"儿童最高人数\"不能小于\"已占位儿童人数\""+currentChildrenNum+"", "警告");
       	   	return;
    	}else if((parseInt(maxPeopleCount)+parseInt(maxChildrenCount))>parseInt(wangtNum)){
    		top.$.jBox.info("\"儿童与特殊人群最高人数之和\"不能大于\"预收\"", "警告");
       	   	return;
    	}else if(groupCodeOld=='' || groupCode==groupCodeOld){ //产品发布 或 未修改团号 
    		secondToThird();
    	}else{
    		$.jBox.confirm("团号修改后该团期下订单数据、财务数据、审批数据对应的团号会相应变化，确认修改？", "提示", function(v, h, f){
    	        if (v == 'ok') {
    	        	secondToThird();
    	        }else if (v == 'cancel'){
                    
                }
            });
    	}
    }

    </script>
</head>
<body>
<page:applyDecorator name="show_head">
    <page:param name="desc">发布机票产品</page:param>
</page:applyDecorator>
<!--右侧内容部分开始-->
                <div class="produceDiv">
					<div style="width:100%; height:20px;"></div>
					<div class="visa_num visa_num2"></div>
		  <form class="form-search" action="${ctx}/airTicket/lastform" id="form1" method="post">
		  <input type="hidden" id="txt_ticketId" name="txt_ticketId" value="${airticket.id}"/>
		  <input type="hidden" name="recordId" value="${airticket.recordId}" />
		  <input type="hidden" id="txt_jsonObj" name="txt_jsonObj" value='${jsonObj}'/>
		  <input type="hidden" id="companyId" name="companyId" value="${fns:getUser().company.id }" />
		  <input type="hidden" id="companyUUID" name="companyUUID" value="${fns:getUser().company.uuid }" />
		  <input type="hidden" id="groupCodeRuleJP" value="${fns:getUser().company.groupCodeRuleJP }" />
		  <input type="hidden" id="groupCodeOld" value="${airticket.groupCode }"/>
		  <input type="hidden" id="orderPersonNumChild" value="${orderPersonNumChild }">
		  <input type="hidden" id="orderPersonNumSpecial" value="${orderPersonNumSpecial }">
		  <input type="hidden" id="reservationsNum" value="${airticket.reservationsNum }">
		  
          <div class="messageDiv">
          	<div class="kongr"></div>
            <div class="kongr"></div>
            <div class="mod_information_d7"></div>
            <div class="kongr"></div>
            <!--往返开始-->
            <div class="seach25 seach100">
            	<span class="seach_check"><label for="inquiry_radio_flights2"><input id="inquiry_radio_flights2" type="radio" onclick="inquiry_radio_flights()" <c:if test="${airticket.airType eq '3'}">checked="checked"</c:if> disabled="disabled" /> 单程</label></span>
                <span class="seach_check"><label for="inquiry_radio_flights1"><input id="inquiry_radio_flights1" type="radio" onclick="inquiry_radio_flights()" <c:if test="${airticket.airType eq '2'}">checked="checked"</c:if> disabled="disabled" /> 往返</label></span>
                <span class="seach_check"><label for="inquiry_radio_flights3"><input id="inquiry_radio_flights3" type="radio" onclick="inquiry_radio_flights()" <c:if test="${airticket.airType eq '1'}">checked="checked"</c:if> disabled="disabled" /> 多段</label></span>
                <input name="flightType" type="hidden" value="${airticket.airType}" />
            </div>


                <c:choose>
                    <c:when test="${airticket.airType eq '3'}"> <!--单程 -->
                        <div class="inquiry_flights2" style="display: block;">
                            <c:forEach items="${airticket.flightInfos}" var="list" varStatus="listIndex">
	                            <div class="seach25 seach100">
	                            	<span class="seach_check"><label for="radio14"><input type="radio" <c:if test="${list.ticket_area_type eq '4'}">checked="checked"</c:if> disabled="disabled" /> 国内</label></span>
	                                <span class="seach_check"><label for="radio12"><input type="radio" <c:if test="${list.ticket_area_type eq '2'}">checked="checked"</c:if> disabled="disabled" /> 国际</label></span>
	                                <span class="seach_check"><label for="radio11"><input type="radio" <c:if test="${list.ticket_area_type eq '1'}">checked="checked"</c:if> disabled="disabled" /> 内陆</label></span>
	                                <span class="seach_check"><label for="radio13"><input type="radio" <c:if test="${list.ticket_area_type eq '3'}">checked="checked"</c:if> disabled="disabled" /> 国际+内陆</label></span>
	                            </div>
                                <div class="seach25">
                                    <p><span class="xing">*</span>出发城市：</p>
                                    
                                    <p class="seach_r"><span class="disabledshowspan">
                             	  ${fns:getDictLabel(airticket.departureCity, 'from_area', '')}
                                    </span></p>
                                </div>
                                <div class="seach25">
                                    <p><span class="xing">*</span>到达城市：</p>
                                    <p class="seach_r"><span class="disabledshowspan">
                                   <c:forEach items="${arrivedCitys}" var="arrivedCitys">
			                       		<c:if test="${arrivedCitys.id == airticket.arrivedCity}">
			                       		${arrivedCitys.name}
			                       		</c:if>
		                     	   </c:forEach>
                                    </span></p>
                                </div>
                                <div class="seach25">
                                    <p>预收人数：</p>
                                    <p class="seach_r"><span id="yushourenshu" class="disabledshowspan">${airticket.reservationsNum}人</span></p>
                                </div>
                                <div class="kong"></div>
                                <div class="seach25">
					                <p><span class="xing">*</span>出发机场：</p>
					                <p class="seach_r"><span class="disabledshowspan">${list.paraMap.leaveAirport}</span></p>
					              </div>
					              <div class="seach25">
					                <p><span class="xing">*</span>到达机场：</p>
					               <p class="seach_r"><span class="disabledshowspan">${list.paraMap.destinationAirpost}</span></p>
					              </div>
					              <div class="kong"></div>
                                
                                <div class="seach25">
                                    <p>出发时刻：</p>
                                    <p class="seach_r"><span class="disabledshowspan"><fmt:formatDate value="${list.startTime}" pattern="yyyy-MM-dd HH:mm"/></span></p>
                                </div>
                                <div class="seach25">
                                    <p>到达时刻：</p>
                                    <p class="seach_r"><span class="disabledshowspan"><fmt:formatDate value="${list.arrivalTime}" pattern="yyyy-MM-dd HH:mm"/></span></p>
                                </div>
								<div class="seach25">
					                <p>航班号：</p>
					                <p class="seach_r"><span class="disabledshowspan">${list.flightNumber}</span></p>
					            </div>
                                <div class="kong"></div>
                                <div class="seach25">
                                    <p>航空公司：</p>
                                    <p class="seach_r"><span class="disabledshowspan">
                                    ${fns:getAirlineNameByAirlineCode(list.airlines)}
                                    </span></p>
                                </div>
                                <div class="seach25">
                                    <p>舱位等级：</p>
                                    <p class="seach_r"><span class="disabledshowspan">${fns:getDictLabel(list.spaceGrade, "spaceGrade_Type", "")}</span></p>
                                </div>
                                <div class="seach25">
                                    <p>舱位：</p>
                                    <p class="seach_r"><span class="disabledshowspan">${fns:getDictLabel(list.airspace, "airspace_Type", "")}</span></p>
                                </div>
                                <div class="kong"></div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:when test="${airticket.airType eq '2'}"> <!--往返 -->
                        <div class="inquiry_flights1">
                            
                            <c:forEach items="${airticket.flightInfos}" var="list" varStatus="listIndex">
                            	<c:if test="${list.number==1}">
	                            <div class="seach25 seach100">
	                            	<span class="seach_check"><label for="radio14"><input id="radio14" type="radio" <c:if test="${list.ticket_area_type eq '4'}">checked="checked"</c:if> disabled="disabled" /> 国内</label></span>
	                                <span class="seach_check"><label for="radio12"><input id="radio12" type="radio" <c:if test="${list.ticket_area_type eq '2'}">checked="checked"</c:if> disabled="disabled" /> 国际</label></span>
	                                <span class="seach_check"><label for="radio11"><input id="radio11" type="radio" <c:if test="${list.ticket_area_type eq '1'}">checked="checked"</c:if> disabled="disabled" /> 内陆</label></span>                               
	                                <span class="seach_check"><label for="radio13"><input id="radio13" type="radio" <c:if test="${list.ticket_area_type eq '3'}">checked="checked"</c:if> disabled="disabled" /> 国际+内陆</label></span>
	                            </div>
	                            
	                            <div class="seach25">
                                    <p><span class="xing">*</span>出发城市：</p>
                                    <p class="seach_r"><span class="disabledshowspan">
                                 ${fns:getDictLabel(airticket.departureCity, 'from_area', '')}

                                    </span></p>
                                </div>
                                <div class="seach25">
                                    <p><span class="xing">*</span>到达城市：</p>
                                    <p class="seach_r"><span class="disabledshowspan">
                                    <c:forEach items="${arrivedCitys}" var="arrivedCitys">
			                       		<c:if test="${arrivedCitys.id == airticket.arrivedCity}">
			                       		${arrivedCitys.name}
			                       		</c:if>
		                     	   </c:forEach>
                                    </span></p>
                                </div>
                                <div class="seach25">
                                    <p>预收人数：</p>
                                    <p class="seach_r"><span class="disabledshowspan">${airticket.reservationsNum}人</span></p>
                                </div>

                                <div class="kong"></div>
                            	</c:if>
                                <div class="title_samil">
                                    <c:choose>
                                        <c:when test="${listIndex.index == 0}">
                                            去程：
                                        </c:when>
                                        <c:when test="${listIndex.index == 1}">
                                            返程：
                                        </c:when>
                                    </c:choose>
                                </div>
                                
                                
                                <div class="seach25">
					                <p><span class="xing">*</span>出发机场：</p>
					                <p class="seach_r"><span class="disabledshowspan">${list.paraMap.leaveAirport}</span></p>
					              </div>
					              <div class="seach25">
					                <p><span class="xing">*</span>到达机场：</p>
					               <p class="seach_r"><span class="disabledshowspan">${list.paraMap.destinationAirpost}</span></p>
					              </div>
					              <div class="kong"></div>
                                <div class="seach25">
			                         <p>出发时刻：</p>
			                         <p class="seach_r"><span class="disabledshowspan"><fmt:formatDate value="${list.startTime}" pattern="yyyy-MM-dd HH:mm"/></span></p>
			                     </div>
			                     <div class="seach25">
			                         <p>到达时刻：</p>
			                         <p class="seach_r"><span class="disabledshowspan"><fmt:formatDate value="${list.arrivalTime}" pattern="yyyy-MM-dd HH:mm"/></span></p>
			                     </div>
								<div class="seach25">
					                <p>航班号：</p>
					                <p class="seach_r"><span class="disabledshowspan">${list.flightNumber}</span></p>
					            </div>
                                <div class="kong"></div>
                                <div class="seach25">
                                    <p>航空公司：</p>
                                    <p class="seach_r"><span class="disabledshowspan">
                                   ${fns:getAirlineNameByAirlineCode(list.airlines)}
                                    </span></p>
                                </div>
                                <div class="seach25">
                                    <p>舱位等级：</p>
                                    <p class="seach_r"><span class="disabledshowspan">${fns:getDictLabel(list.spaceGrade, "spaceGrade_Type", "")}</span></p>
                                </div>
                                <div class="seach25">
                                    <p>舱位：</p>
                                    <p class="seach_r"><span class="disabledshowspan">${fns:getDictLabel(list.airspace, "airspace_Type", "")}</span></p>
                                </div>
                                <div class="kong"></div>
                            </c:forEach>
                        </div>
                        <!--往返结束-->
                    </c:when>
                    <c:when test="${airticket.airType eq '1'}">  <!--多段 -->
                        <div class="inquiry_flights3" style="display: block;">
                        	<div class="seach25">
                                    <p><span class="xing">*</span>出发城市：</p>
                                    <p class="seach_r"><span class="disabledshowspan">
                                   ${fns:getDictLabel(airticket.departureCity, 'from_area', '')}
                                    </span></p>
                                </div>
                                <div class="seach25">
                                    <p><span class="xing">*</span>到达城市：</p>
                                    <p class="seach_r"><span class="disabledshowspan">
                                    <c:forEach items="${arrivedCitys}" var="arrivedCitys">
			                       		<c:if test="${arrivedCitys.id == airticket.arrivedCity}">
			                       		${arrivedCitys.name}
			                       		</c:if>
		                     	   </c:forEach>
                                    
                                    </span></p>
                                </div>
                                <div class="seach25">
                                    <p>预收人数：</p>
                                    <p class="seach_r"><span class="disabledshowspan">${airticket.reservationsNum}人</span></p>
                                </div>
                            <c:forEach items="${airticket.flightInfos}" var="list" varStatus="listIndex">
                                <div class="addFlights3Div">
                                    <div class="title_samil">
                                        第${listIndex.index + 1}段：
                                        <span class="seach_check"><label for="radio34"><input id="radio34" type="radio" <c:if test="${list.ticket_area_type eq '4'}">checked="checked"</c:if> disabled="disabled"> 国内</label></span>
                                        <span class="seach_check"><label for="radio32"><input id="radio32" type="radio" <c:if test="${list.ticket_area_type eq '2'}">checked="checked"</c:if> disabled="disabled"> 国际</label></span>
                                        <span class="seach_check"><label for="radio31"><input id="radio31" type="radio" <c:if test="${list.ticket_area_type eq '1'}">checked="checked"</c:if> disabled="disabled"> 内陆</label></span>                
                                        <span class="seach_check"><label for="radio13"><input id="radio33" type="radio" <c:if test="${list.ticket_area_type eq '3'}">checked="checked"</c:if> disabled="disabled" /> 国际+内陆</label></span>
                                    </div>
                                </div>
                                

                                <div class="kong"></div>
                                
                                <div class="seach25">
					                <p><span class="xing">*</span>出发机场：</p>
					                <p class="seach_r"><span class="disabledshowspan">${list.paraMap.leaveAirport}</span></p>
					              </div>
					              <div class="seach25">
					                <p><span class="xing">*</span>到达机场：</p>
					               <p class="seach_r"><span class="disabledshowspan">${list.paraMap.destinationAirpost}</span></p>
					              </div>
					              <div class="kong"></div>
                                <div class="seach25">
			                         <p>出发时刻：</p>
			                         <p class="seach_r"><span class="disabledshowspan"><fmt:formatDate value="${list.startTime}" pattern="yyyy-MM-dd HH:mm"/></span></p>
			                     </div>
			                     <div class="seach25">
			                         <p>到达时刻：</p>
			                         <p class="seach_r"><span class="disabledshowspan"><fmt:formatDate value="${list.arrivalTime}" pattern="yyyy-MM-dd HH:mm"/></span></p>
			                     </div>
								<div class="seach25">
					                <p>航班号：</p>
					                <p class="seach_r"><span class="disabledshowspan">${list.flightNumber}</span></p>
					            </div>
                                <div class="kong"></div>
                                <div class="seach25">
                                    <p>航空公司：</p>
                                    <p class="seach_r"><span class="disabledshowspan">
                                   ${fns:getAirlineNameByAirlineCode(list.airlines)}
                                    </span></p>
                                </div>
                                <div class="seach25">
                                    <p>舱位等级：</p>
                                    <p class="seach_r"><span class="disabledshowspan">${fns:getDictLabel(list.spaceGrade, "spaceGrade_Type", "")}</span></p>
                                </div>
                                <div class="seach25">
                                    <p>舱位：</p>
                                    <p class="seach_r"><span class="disabledshowspan">${fns:getDictLabel(list.airspace, "airspace_Type", "")}</span></p>
                                </div>
                                <div class="kong"></div>
                            </c:forEach>
                        </div>
                    </c:when>
                </c:choose>

            <!--备注和有效期开始-->
            <div class="kongr"></div>
            
            <!--出票日期开始-->
            <div class="seach25">
                <p class="fbold f14">出票日期：</p>
                <div><span class="disabledshowspan"><fmt:formatDate value="${airticket.outTicketTime}" pattern="yyyy-MM-dd"/></span></div>
            </div>
            <!--出票日期结束-->
            <div class="seach25">
                  <p class="fbold f14"><span class="xing" style="display:none;">*</span>离境口岸：</p>
                  <span class="disabledshowspan">
                   <c:forEach items="${out_areas}" var="from_areas">
			                       		<c:if test="${from_areas.id == airticket.outArea}">
			                       		${from_areas.label}
			                       		</c:if>
		                     	   </c:forEach>
                  
                  </span>
                  
                  
            </div>
            <!--青岛凯撒，大洋-->
            <c:if test="${fns:getUser().company.uuid != '7a8177e377a811e5bc1e000c29cf2586' && fns:getUser().company.uuid != '7a81a03577a811e5bc1e000c29cf2586' && fns:getUser().company.uuid != 'ed88f3507ba0422b859e6d7e62161b00' && fns:getUser().company.uuid != 'f5c8969ee6b845bcbeb5c2b40bac3a23' && fns:getUser().company.uuid != '1d4462b514a84ee2893c551a355a82d2' && fns:getUser().company.uuid != '58a27feeab3944378b266aff05b627d2' && fns:getUser().company.uuid != '7a81c5d777a811e5bc1e000c29cf2586' && fns:getUser().company.uuid != '5c05dfc65cd24c239cd1528e03965021' && fns:getUser().company.groupCodeRuleJP !=0}">
                <input type="hidden" name="groupCode" id="groupCode" value="${airticket.groupCode }"/>
            </c:if>
            <div class="kong"></div>
			<!--分区联运开始-->
            <div class="mod_information_d2 lianyun">
            	<div class="lianyun_select">
                    <label>联运类型：</label>
                    <span class="disabledshowspan">${airticket.paraMap.intermodalType}</span>
                </div>
                <div id="nationalTrans" class="transport_city" style="display: none;">
                    <label>联运价格：</label>
                    <span class="disabledshowspan">555</span>
                </div>
                <div id="groupTrans" class="transport_city">
	                <c:forEach items="${airticket.intermodalStrategies}" var="list" varStatus="listIndex">
	                    <p>
							<c:if test="${airticket.intermodalType==2}">
								<label class="transport_city_label">联运城市分区：</label>
							</c:if>
							<input class="valid" id="intermodalGroupPart" name="intermodalGroupPart" maxlength="50" type="text" style="display:none;">
							<%--因bug16751，注释下面代码--%>
							<%--<span class="disabledshowspan">${list.groupPart}</span>--%>
							<label>联运价格：</label>
							<span class="currency">${list.priceCurrency.currencyMark}
								<span class="disabledshowspan">
									<fmt:formatNumber value="${list.price}" pattern="#,##0.##" />
								</span>
							</span>
						</p>
	                 </c:forEach>   
                </div>
            </div>
            <!--分区联运结束-->            
            
            
            <div class="kong"></div>
            <!--占位方式开始-->
            <div class="seach25 seach100">
                <p class="fbold f14">付款方式：</p>
                <p class="seach_r add-paytype">
                    <font id="payModeText"><c:if test="${airticket.payMode_deposit==1}">订金占位 
                   		（保留
                   		<c:choose>
                   			<c:when test="${empty airticket.remainDays_deposit }">0天</c:when>
                   			<c:otherwise>${airticket.remainDays_deposit}天</c:otherwise>
                   		</c:choose>
                   		<c:choose>
                   			<c:when test="${empty airticket.remainDays_deposit_hour }">0时</c:when>
                   			<c:otherwise>${airticket.remainDays_deposit_hour}时</c:otherwise>
                   		</c:choose>
                   		<c:choose>
                   			<c:when test="${empty airticket.remainDays_deposit_fen }">0分</c:when>
                   			<c:otherwise>${airticket.remainDays_deposit_fen}分</c:otherwise>
                   		</c:choose>
                   		）
                   	</c:if>
                   &nbsp;&nbsp;
                   <c:if test="${airticket.payMode_advance==1}">预占位   
                  		（保留
                  		<c:choose>
                   			<c:when test="${empty airticket.remainDays_advance }">0天</c:when>
                   			<c:otherwise>${airticket.remainDays_advance}天</c:otherwise>
                   		</c:choose>
                   		<c:choose>
                   			<c:when test="${empty airticket.remainDays_advance_hour }">0时</c:when>
                   			<c:otherwise>${airticket.remainDays_advance_hour}时</c:otherwise>
                   		</c:choose>
                   		<c:choose>
                   			<c:when test="${empty airticket.remainDays_advance_fen }">0分</c:when>
                   			<c:otherwise>${airticket.remainDays_advance_fen}分</c:otherwise>
                   		</c:choose>
                  		）	
                   </c:if>
                   &nbsp;&nbsp;
                   <c:if test="${airticket.payMode_full==1}">全款支付</c:if>
                   &nbsp;&nbsp;
                   <c:if test="${airticket.payMode_op==1}">计调确认占位</c:if>
                   
                   <c:if test="${airticket.payMode_cw==1}">财务确认占位</c:if>
                   </font>
                </p>
                <div class="kongr"></div>
            </div>
            <!--占位方式结束-->
          </div>
          <!--填写价格开始-->
          <div class="mod_information" id="secondStepDiv">   
              <c:if test="${fns:getUser().company.uuid == '7a8177e377a811e5bc1e000c29cf2586' || fns:getUser().company.uuid == '7a81a03577a811e5bc1e000c29cf2586' || fns:getUser().company.uuid == 'ed88f3507ba0422b859e6d7e62161b00' || fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' || fns:getUser().company.uuid == '1d4462b514a84ee2893c551a355a82d2' || fns:getUser().company.uuid == '58a27feeab3944378b266aff05b627d2' || fns:getUser().company.uuid == '7a81c5d777a811e5bc1e000c29cf2586' || fns:getUser().company.uuid == '5c05dfc65cd24c239cd1528e03965021' || fns:getUser().company.groupCodeRuleJP==0}">
                  <div class="mod_information_d" id="secondStepTitle"><span style=" font-weight:bold; padding-left:20px;float:left">添加团号及价格</span></div>
              </c:if>
              <c:if test="${fns:getUser().company.uuid != '7a8177e377a811e5bc1e000c29cf2586' && fns:getUser().company.uuid != '7a81a03577a811e5bc1e000c29cf2586' && fns:getUser().company.uuid != 'ed88f3507ba0422b859e6d7e62161b00' && fns:getUser().company.uuid != 'f5c8969ee6b845bcbeb5c2b40bac3a23' && fns:getUser().company.uuid != '1d4462b514a84ee2893c551a355a82d2' && fns:getUser().company.uuid != '58a27feeab3944378b266aff05b627d2' && fns:getUser().company.uuid != '7a81c5d777a811e5bc1e000c29cf2586' && fns:getUser().company.uuid != '5c05dfc65cd24c239cd1528e03965021' && fns:getUser().company.groupCodeRuleJP !=0}">
                  <div class="mod_information_d" id="secondStepTitle"><span style=" font-weight:bold; padding-left:20px;float:left">添加价格</span></div>
              </c:if>
              <div id="secondStepContent">
                  <div style="width:100%; height:10px;"></div>
                  <div class="add2_nei">
                  <!-- 青岛凯撒、诚品旅游、日信观光   团号不可修改-->
                  <c:if test="${fns:getUser().company.uuid == '7a8177e377a811e5bc1e000c29cf2586'  || fns:getUser().company.uuid == 'ed88f3507ba0422b859e6d7e62161b00' }">

                      <div class="kongr"></div>
                      <div class="seach25 seach100 pro-marks1">
                          <p class="fbold f14" style="width:auto;margin-right:5px;"><span class="xing">*</span>团号：</p>
                          <p class="seach_r">
                              <input <c:if test="${airticket.groupCode != null && airticket.groupCode != '' }">readonly="readonly"</c:if> id="groupCode" name="groupCode" value="${airticket.groupCode }" type="text" maxlength="50" title="${airticket.groupCode }" onblur="removeCodeCss(this)" <c:if test="${airticket.groupCode == null || airticket.groupCode == '' }">onafterpaste="replaceStr(this)" onkeyup="validateLong(this)"</c:if> />
                          </p>
                      </div>
                      <div class="kong"></div>
                  </c:if>
                  <!--大洋、 非常国际、优加、起航、懿洋假期手动配置且不包含上面三家批发商  团号可修改 --><!-- 其余批发商不显示团号 -->
                  <c:if test="${fns:getUser().company.uuid == '7a81a03577a811e5bc1e000c29cf2586' || fns:getUser().company.uuid == '1d4462b514a84ee2893c551a355a82d2' || fns:getUser().company.uuid == '7a81c5d777a811e5bc1e000c29cf2586' || fns:getUser().company.uuid == '5c05dfc65cd24c239cd1528e03965021' || fns:getUser().company.uuid == '58a27feeab3944378b266aff05b627d2' || (fns:getUser().company.groupCodeRuleJP==0 && fns:getUser().company.uuid != '7a8177e377a811e5bc1e000c29cf2586' && fns:getUser().company.uuid != 'ed88f3507ba0422b859e6d7e62161b00' && fns:getUser().company.uuid != '58a27feeab3944378b266aff05b627d2')}">
                      <div class="kongr"></div>
                      <div class="seach25 seach100 pro-marks1">
                          <p class="fbold f14" style="width:auto;margin-right:5px;"><span class="xing">*</span>团号：</p>
                          <p class="seach_r">
                              <input id="groupCode" name="groupCode" value="${airticket.groupCode }" type="text" maxlength="50" title="${airticket.groupCode }" onblur="removeCodeCss(this)" onafterpaste="replaceStr(this)" onkeyup="validateLong(this)"/>
                          </p>
                      </div>
                      <div class="kong"></div>
                  </c:if>
                  <div class="title_samil">整体报价</div>
                  <div class="clear"></div>
                  <table class="table-mod2-group planeTick-table">
                      <tbody>
                      <tr>
                          <td class="add2_nei_table">币种选择：</td>
                          <td class="add2_nei_table_typetext">
                            
                              
                              <c:if test="${fns:getUser().company.uuid == '7a8177e377a811e5bc1e000c29cf2586' }"> 
                              <select id="selectCurrency" name="currency_id" class="sel-currency">
                                  <c:forEach items="${curlist}" var="currency">
                                      <option addclass="${currency.currencyMark}" value="${currency.id}"  <c:if test="${currency.currencyStyle eq 'rmb'}"> selected="selected" </c:if> >${currency.currencyName}</option>
                                  </c:forEach>
                              </select>
                              </c:if>
                              <c:if test="${fns:getUser().company.uuid != '7a8177e377a811e5bc1e000c29cf2586' }"> 
                              <select id="selectCurrency" name="currency_id" class="sel-currency">
                                  <c:forEach items="${curlist}" var="currency">
                                      <option addclass="${currency.currencyMark}" value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  </c:forEach>
                              </select>
                              </c:if>
                              
                          </td>
                          <td class="add2_nei_table"><input name="istax" type="checkbox" value="1" class="ckb-tax"  onc  onkeydown="onlyNum();" <c:if test="${airticket.istax==1}">checked="checked"</c:if>/>税费：</td>
                          <td class="add2_nei_table_typetext pr"><span>${curlist.size()>0?curlist[0].currencyMark:"￥"}</span><input type="text" maxlength="7" id="taxamt" name="taxamt" value="${airticket.taxamt}" class="ipt-currency valid"  onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" flag="istips"><span class="ipt-tips" style="left:20px;top:10px;">已含</span></td>
                          
                          <td class="add2_nei_table">应付账期：</td>
                          <td class="add2_nei_table_typetext wpr20"><input type="text" maxlength="20" id="payableDate" name="payableDate" value="<fmt:formatDate value="${airticket.payableDate}" pattern="yyyy-MM-dd "/>" class="dateinput valid" onfocus="WdatePicker()"></td>
                      </tr>
                      <tr>
						  <td class="add2_nei_table"><span class="xing">*</span>成人同行价：</td>
                          <td class="add2_nei_table_typetext"><span>${curlist.size()>0?curlist[0].currencyMark:"￥"}</span><input type="text" maxlength="16" id="settlementAdultPrice" value="${airticket.settlementAdultPrice}" name="settlementAdultPrice" class="ipt-currency valid" onkeyup="isMoney(this)" onafterpaste="isMoney(this)"></td>
                      
                          <td class="add2_nei_table"><span class="xing">*</span>儿童同行价：</td>
                          <td class="add2_nei_table_typetext"><span>${curlist.size()>0?curlist[0].currencyMark:"￥"}</span><input type="text" maxlength="16" value="${airticket.settlementcChildPrice}" id="settlementcChildPrice" name="settlementcChildPrice" class="ipt-currency valid" onkeyup="isMoney(this)" onafterpaste="isMoney(this)"></td>
                          <td class="add2_nei_table"><span class="xing">*</span>特殊人群同行价：</td>
                          <td class="add2_nei_table_typetext"><span>${curlist.size()>0?curlist[0].currencyMark:"￥"}</span><input type="text" maxlength="16" value="${airticket.settlementSpecialPrice}" id="settlementSpecialPrice" name="settlementSpecialPrice" class="ipt-currency valid" onkeyup="isMoney(this)" onafterpaste="isMoney(this)"></td>
                      </tr>
                      <!-- 发票税:0258需求,限定为懿洋假期-tgy-s -->
                      <c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' }"> 
                       <tr>
						  <td class="add2_nei_table">发票税：</td>
						  <!-- TODO:发票税文本的校验 -->
                          <td class="add2_nei_table_typetext"><input type="text"  id="invoiceTax" value="${airticket.invoiceTax}" name="invoiceTax" class="ipt-currency valid" onkeyup="checkValue(this);" onafterpaste="checkValue(this);" onfocus="checkValue(this);">%</td>
                      </tr>
                      </c:if>
                      <!-- 发票税:0258需求,限定为懿洋假期-tgy-e -->
                      <tr>
                          <td class="add2_nei_table">订金：</td>
                          <td class="add2_nei_table_typetext"><span>${curlist.size()>0?curlist[0].currencyMark:"￥"}</span><input type="text" maxlength="16" id="depositamt" name="depositamt" value="${airticket.depositamt}" class="ipt-currency valid" onkeyup="isMoney(this)" onafterpaste="isMoney(this)"></td>
                          <td class="add2_nei_table">订金时限：</td>
                          <td class="add2_nei_table_typetext wpr20"><input type="text" maxlength="20" id="depositTime" name="depositTime" value="<fmt:formatDate value="${airticket.depositTime}" pattern="yyyy-MM-dd "/>" class="dateinput valid" onfocus="WdatePicker()"></td>
                          <td class="add2_nei_table">取消时限：</td>
                          <td class="add2_nei_table_typetext"><input type="text" maxlength="20" id="cancelTimeLimit" name="cancelTimeLimit" value="<fmt:formatDate value="${airticket.cancelTimeLimit}" pattern="yyyy-MM-dd "/>" class="dateinput valid" onfocus="WdatePicker()"></td>
                      </tr>
                      <tr>
<%--                       	<label class="label-dw">备注：</label><input type="text" class="valid" id="remark" name="remark" maxlength="20" value="${airticket.specialremark}" flag="istips"/><span class="ipt-tips">特殊人群备注</span> --%>
                      	<td class="add2_nei_table">备注：</td>
                        <td class="add2_nei_table_typetext wpr20">
                         <div class="marks-people">
                        <input type="text" class="valid" id="remark" name="remark" maxlength="20" value="${airticket.specialremark}" flag="istips"/>
                        <span class="ipt-tips" style="text-indent:-1.2cm;">特殊人群备注</span>
                        </div>
                        </td>
                      	<td class="add2_nei_table"><label class="label-dw">特殊人群最高人数：   </label> </td>
                        <td class="add2_nei_table_typetext">                      
		                    	<input id="maxPeopleCount" type="text"  name="maxPeopleCount" maxlength="8"  value="${airticket.maxPeopleCount}"  style="width:60px;"
		                    	onafterpaste="this.value=this.value.replace(/\D|^0.+/g,'')" onkeyup="this.value=this.value.replace(/\D|^0.+/g,'')"/>
		                    	<span style="padding-left:5px;">人</span>                    	
	               		</td>
	               		<td class="add2_nei_table"><label class="label-dw">儿童最高人数：   </label> </td>
                        <td class="add2_nei_table_typetext">                      
		                    	<input id="maxChildrenCount" name="maxChildrenCount" type="text"  maxlength="8"  value="${airticket.maxChildrenCount}"  style="width:60px;"
		                    	onafterpaste="this.value=this.value.replace(/\D|^0.+/g,'')" onkeyup="this.value=this.value.replace(/\D|^0.+/g,'')"/>
		                    	<span style="padding-left:5px;">人</span>                    	
	               		</td>
                      </tr>
                      </tbody>
                  </table>
                      <c:choose>
                          <c:when test="${airticket.airType eq '1'}">
                              <div class="title_samil"><input type="checkbox" name="flyDivInput" value="1" <c:if test="${airticket.isSection==1}">checked="checked"</c:if> onclick="flyDivCheck()"> 分段报价</div>
                              <input type="hidden" id="txt_isMore" name="txt_isMore" value="0" />
                              <input type="hidden" id="txt_istax" name="txt_istax" value="" />
                              <div class="flyMoreDiv">
                              <c:forEach items="${airticket.flightInfos}" varStatus="partIndex" var="list">
                                  <div class="title_samil">第${partIndex.index+1}段：${list.paraMap.leaveAirport}-${list.paraMap.destinationAirpost}</div>
                                  <table class="table-mod2-group planeTick-table">
                                  <tbody><tr>
                                  <td class="add2_nei_table">币种选择：</td>
                                  <td class="add2_nei_table_typetext">
                                       <select id="selectCurrency" name="more_currency_id" class="sel-currency">
                                          <c:forEach items="${curlist}" var="currency">
                                          	<option addclass="${currency.currencyMark}" value="${currency.id}" <c:if test="${list.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                          </c:forEach>
                                      </select>
                                  </td>
                                  <td class="add2_nei_table"><input name="more_istax" type="checkbox" value="1" class="ckb-tax"  <c:if test="${airticket.istax==1}">checked="checked"</c:if>/>税费：</td>
                                  <td class="add2_nei_table_typetext pr" colspan='3'><span>${curlist.size()>0?curlist[0].currencyMark:"￥"}</span><input type="text" maxlength="20" id="taxamt" name="more_taxamt" value="${list.taxamt}" class="ipt-currency valid"  onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" flag="istips" ><span class="ipt-tips" style="left:20px;top:10px;">已含</span></td>
                                  </tr>
                                  <tr>
								  <td class="add2_nei_table">成人同行价：</td>
                                  <td class="add2_nei_table_typetext"><span>${curlist.size()>0?curlist[0].currencyMark:"￥"}</span><input type="text" maxlength="20" id="settlementAdultPrice" name="more_settlementAdultPrice" value="${list.settlementAdultPrice}" class="ipt-currency valid" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"></td>
                                  <td class="add2_nei_table">儿童同行价：</td>
                                  <td class="add2_nei_table_typetext"><span>${curlist.size()>0?curlist[0].currencyMark:"￥"}</span><input type="text" maxlength="20" id="settlementcChildPrice" name="more_settlementcChildPrice" value="${list.settlementcChildPrice}" class="ipt-currency valid" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"></td>
                                  <td class="add2_nei_table">特殊人群同行价：</td>
                                  <td class="add2_nei_table_typetext"><span>${curlist.size()>0?curlist[0].currencyMark:"￥"}</span><input type="text" maxlength="20" id="settlementSpecialPrice" name="more_settlementSpecialPrice" value="${list.settlementSpecialPrice}" class="ipt-currency valid" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"><div class="marks-people"><label class="label-dw">备注：</label><input type="text" class="valid" id="more_remark" name="more_remark" maxlength="200" value="${list.remark}"  flag="istips"/><span class="ipt-tips">特殊人群备注</span></div></td>
                                  
                                  </tr>

                                  </tbody>
                                  </table>
                              </c:forEach>
                              </div>
                          </c:when>
                          <c:otherwise>
                          </c:otherwise>
                      </c:choose>
                   <div class="kongr"></div>
                        <div class="seach25 seach100 pro-marks1">
							<p class="fbold f14" style="width:auto;margin-right:5px;">备注：</p>
							<p class="seach_r">
								<textarea id="airticketremark" name="airticket.remark"  >${airticket.remark}</textarea>
							</p>
						</div>
					<div class="kong"></div>
                </div>
              </div>
              <!--填写价格结束-->
              <div class="mod_information_dzhan_d" id="secondStepBtn">
                  <div class="release_next_add">
                           <!--  input type="button" value="上一步" onclick="secondToOne()" class="btn btn-primary"-->
<!--                            <input type="button" value="下一步" onclick="secondToThird()" class="btn btn-primary"> -->
								<input type="button" value="下一步" onclick="beforeSecondToThird()" class="btn btn-primary"><!-- C460 修改团号时给出提示 -->
                  </div>
              </div>
          </div>
		  </form>
        </div>
				<!--右侧内容部分结束-->
</body>
</html>