<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>产品-机票产品及发布-基础信息</title>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/modules/forTTS/airticket/airticetForm.js"></script>
<script type="text/javascript">
var root = "${ctx}";
function inquiry_radio_flights1(){
    if($("#inquiry_radio_flights1").prop("checked")){
        $('.inquiry_flights1').show();
        $('.inquiry_flights2').hide(); $('.inquiry_flights3').hide();
    }else if($("#inquiry_radio_flights2").prop("checked")){
        $('.inquiry_flights2').show();
        $('.inquiry_flights1').hide(); $('.inquiry_flights3').hide();
    }else if($("#inquiry_radio_flights3").prop("checked")){
        $('.inquiry_flights3').show();
        $('.inquiry_flights1').hide(); $('.inquiry_flights2').hide();
    }
}
$(function(){
	//inquiry_radio_peoples();
	//inquiry_box3_check();
	inquiry_radio_flights1();
	selectairline();//机场联动
	transportSelect();
	//可输入select
	//$(".selectinput" ).comboboxSingle(); 
	$(".selectinput" ).comboboxInquiry(); 
	//天数插件
	$( ".spinner" ).spinner({
		spin: function( event, ui ) {
		if ( ui.value > 365 ) {
			$( this ).spinner( "value", 0 );
			return false;
		} else if ( ui.value < 0 ) {
			$( this ).spinner( "value", 365 );
			return false;
		}
		}
	});
	//小时
	$( ".spinner_hour" ).spinner({
		spin: function( event, ui ) {
		if ( ui.value > 23 ) {
			$( this ).spinner( "value", 0 );
			return false;
		} else if ( ui.value < 0 ) {
			$( this ).spinner( "value", 23 );
			return false;
		}
		}
	});
	//分钟
	$( ".spinner_fen" ).spinner({
		spin: function( event, ui ) {
		if ( ui.value > 59 ) {
			$( this ).spinner( "value", 0 );
			return false;
		} else if ( ui.value < 0 ) {
			$( this ).spinner( "value", 59 );
			return false;
		}
		}
	});
	
	//联运类型、付款方式、离境口岸初始化
	initCreateAirForm();
});
    
	//机场选择
function airportClick(tarObj){
	var paraname=$(tarObj).attr("id");
	// 是否限制选择，如果限制，设置为disabled
	if ("${disabled}" == "disabled"){
		return true;
	}
	// 正常打开	
	top.$.jBox.open("iframe:"+root+"/tag/treeselect?url="+encodeURIComponent("/airTicket/filterAirportInfoData")+"&module=&checked=&extId=&selectIds="+$("#"+paraname+"Id").val(), "选择机场", 300, 420,{
		buttons:{"确定":"ok", "关闭":true}, submit:function(v, h, f){
			if (v=="ok"){
				var tree = h.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
				var ids = [], names = [], nodes = [];
				if ("${checked}" == "true"){
					nodes = tree.getCheckedNodes(true);
				}else{
					nodes = tree.getSelectedNodes();
				}
				for(var i=0; i<nodes.length; i++) {//<c:if test="${checked}">
					if (nodes[i].isParent){
						continue; // 如果为复选框选择，则过滤掉父节点
					}//</c:if><c:if test="${notAllowSelectRoot}">
					if (nodes[i].level == 0){
						top.$.jBox.tip("不能选择根节点（"+nodes[i].name+"）请重新选择。");
						return false;
					}//</c:if><c:if test="${notAllowSelectParent}">
					if (nodes[i].isParent){
						top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
						return false;
					}//</c:if><c:if test="${not empty module && selectScopeModule}">
					if (nodes[i].module == ""){
						top.$.jBox.tip("不能选择公共模型（"+nodes[i].name+"）请重新选择。");
						return false;
					}else if (nodes[i].module != "${module}"){
						top.$.jBox.tip("不能选择当前栏目以外的栏目模型，请重新选择。");
						return false;
					}//</c:if>
					ids.push(nodes[i].id);
					names.push(nodes[i].name);//<c:if test="${!checked}">
					break; // 如果为非复选框选择，则返回第一个选择  </c:if>
				}
				$("#"+paraname+"Id").val(ids);
				$("#"+paraname+"Name").val(names);
				$("#"+paraname+"Name").focus();
				$("#"+paraname+"Name").blur();
			}//<c:if test="${allowClear}">
			else if (v=="clear"){
				$("#"+paraname+"Id").val("");
				$("#"+paraname+"Name").val("");
				
            }//</c:if>
		}, loaded:function(h){
			$(".jbox-content", top.document).css("overflow-y","hidden");
		},persistent:true
	});
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
					<div class="visa_num"></div>
					 
                    <div class="mod_information_dzhan">
                    	<div class="kongr"></div>
                      	<div class="kongr"></div>
                      	<div class="mod_information_d7"></div>
                      	<div class="kongr"></div>
                      	<div class="seach25 seach100">
                        	<span class="seach_check"><label for="inquiry_radio_flights2"><input  name="airType_radio" id="inquiry_radio_flights2" type="radio" value="3"  <c:if test="${airticket.airType eq '3'}">checked="checked"</c:if> onclick="inquiry_radio_flights()"> 
                        	单程
                        	<input type="hidden" id="airticketReserveNum" name="" value="${airticketReserveNum }" />    
                        	</label></span>
                          	<span class="seach_check"><label for="inquiry_radio_flights1"><input name="airType_radio" id="inquiry_radio_flights1" type="radio" value="2" <c:if test="${airticket.airType eq '2' || airticket.airType==null}">checked="checked"</c:if> onclick="inquiry_radio_flights()"> 往返</label></span>
                          	<span class="seach_check"><label for="inquiry_radio_flights3"><input  name="airType_radio" id="inquiry_radio_flights3" type="radio" value="1" <c:if test="${airticket.airType eq '1'}">checked="checked"</c:if> onclick="inquiry_radio_flights()"> 多段</label></span>
                            <span class="pl25"><a target="_blank" href="${ctx}/eprice/manager/project/erecordtrafficlist">询价记录</a></span>
                        </div>
                        
						<input type="hidden" value="${fns:getUser().company.uuid }" id="companyUUID"/>
                        <c:choose>
                        
                        <c:when test="${airticket.airType eq '2'}">
                        <!-- 修改打开-往返 -->
                        <!-- 单程 -->
                        <form action="${ctx}/airTicket/secondform" method="post" id="frm3">
                        <input type="hidden" name="airType" value="3" />
                        <input type="hidden" name="sectionNum" value="1" />
                        <input type="hidden" name="intermodalInfo" value="" />
                        <input type="hidden" name="txt_ticketId" value="${airticket.id}" />
                        <c:if test="${fns:getUser().company.uuid == '7a8177e377a811e5bc1e000c29cf2586' || fns:getUser().company.uuid == '7a81a03577a811e5bc1e000c29cf2586' 
                        || fns:getUser().company.uuid == 'ed88f3507ba0422b859e6d7e62161b00' || fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23'
                         || fns:getUser().company.uuid == '1d4462b514a84ee2893c551a355a82d2' || fns:getUser().company.uuid == '58a27feeab3944378b266aff05b627d2' 
                         || fns:getUser().company.uuid == '7a81c5d777a811e5bc1e000c29cf2586' || fns:getUser().company.uuid == '5c05dfc65cd24c239cd1528e03965021' 
                         || fns:getUser().company.uuid == '049984365af44db592d1cd529f3008c3' || fns:getUser().company.groupCodeRuleJP==0}">
                            <input type="hidden" id="groupCode" name="groupCode" value="${airticket.groupCode }" />
                        </c:if>
                      	<div class="inquiry_flights2">
                        	<div class="seach25 seach100">
                        		<span class="seach_check"><label for="radio24"><input name="flightArea" id="radio24" type="radio" value="4"> 国内</label></span>
                        		<span class="seach_check"><label for="radio22"><input  name="flightArea"id="radio22" type="radio" value="2"> 国际</label></span>
                        		<span class="seach_check"><label for="radio21"><input name="flightArea" id="radio21" type="radio" value="1"> 内陆</label></span>                          
                                <span class="seach_check"><label for="radio23"><input  name="flightArea"id="radio23" type="radio" value="3"> 国际+内陆</label></span>
                            </div>
                             <div class="seach25">
                              	<p><span class="xing">*</span>出发城市：</p>
                              	<p class="seach_r">
                              	<select class="selectinput" id="departureCity" name="departureCity">      
                                    <c:forEach var="dic" items="${from_areaslist}">
                                            <option value="${dic.value}" <c:if test="${airticket.departureCity==dic.value}">selected="selected"</c:if>>${dic.label}</option>
                                        </c:forEach>
                                </select></p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达城市：</p>
                                <p class="seach_r"><select class="selectinput" name="arrivedCity">      
                                   <c:forEach var="area" items="${arrivedareas}">
                                            <option value="${area.id}" <c:if test="${airticket.arrivedCity==area.id}">selected="selected"</c:if>>${area.name}</option>
                                        </c:forEach>
                                </select></p>
                            </div>
                             <div class="seach25">
                                <p><span class="xing">*</span>预收人数 ：</p>
                                <p class="seach_r"><input type="text" id="reservationsNum"  maxlength="10" name="reservationsNum" value=""></p>
                            </div> 
                            <div class="kong"></div>
                            <div class="seach25">
                              	<p><span class="xing">*</span>出发机场：</p>
                              	<p class="seach_r">
                              	<div class="input-append">
                              		<input id="sin_leaveAirportId" name="leaveAirport" type="hidden" value="" class=""/> 
									<input id="sin_leaveAirportName" name="leaveAirportName" readonly="readonly" type="text" value="" class="appendtext"/>
									<a id="sin_leaveAirport" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div>
								</p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达机场：</p>
                                <p class="seach_r">
                                <div class="input-append">
				                    <input id="sin_desAirpostId" name="desAirpost" type="hidden" value="" /> 
									<input id="sin_desAirpostName" name="desAirpostName" readonly="readonly" type="text" value="" class="appendtext"/> 
									<a id="sin_desAirpost" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
							</div></p>
                            </div>
                            <div class="kong"></div>
                            <div class="seach25">
                                <p>出发时刻：</p>
                                <p class="seach_r"><input type="text" class="dateinput"  name="startTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                            </div>
                            <div class="seach25">
                                <p>到达时刻：</p>
                                <p class="seach_r"><input type="text" class="dateinput"  name="arrivalTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                            </div>
                            <div class="seach25">
                            	<p>航班号：</p>
                              	<p class="seach_r"><input type="text" name="flightNumber" value="" maxlength="20"></p>
                            </div>
                      		<div class="kong"></div>
                            <div class="seach25">
                                <p>航空公司：</p>
                                <p class="seach_r"> <select name="airlines" id="airlines" onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
                                	<option value="">不限</option>
                                    <c:forEach var="airlines" items="${airlines_list}">
                                            <option value="${airlines.airlineCode}">${airlines.airlineName}</option>
                                        </c:forEach>
                                </select></p>
                            </div>
                            <div class="seach25">
                                <p>舱位等级：</p>
                                <p class="seach_r"> 
                                <select name="spaceGrade" onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
                                  <option value="-1">不限</option>
                                </select></p>
                            </div>
                            <div class="seach25">
                                <p>舱位：</p>
                                <p class="seach_r"><select name="airSpace">
                                 <option value="-1">不限</option>
                                </select></p>
                            </div>
                            <div class="kong"></div>
                            
                             <div class="kongr"></div>
                             
                             <!--出票日期开始-->
					<div class="seach25">
						<p class="fbold f14">出票日期：</p>
						<div><input type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="" name="outTicketTime" class="dateinput"></div>
					</div>
					<!--出票日期结束-->
                    <!--离境口岸开始-->
                      <div class="seach25">
                           <p class="fbold f14"><span class="xing">*</span>离境口岸：</p>
                           <select id="outArea" name="outArea" class="" style="display: inline-block;" onchange="outAreachg()">
                           <option selected="selected" value="-1">请选择</option>
                                <c:forEach var="dic" items="${out_areaslist}">
								<option value="${dic.id}">${dic.label}</option>
								</c:forEach>
                           </select>
                      </div>
                        <!--离境口岸结束-->
                        <div class="kongr"></div>
                        <!--联运开始-->
                        <div class="mod_information_d2 lianyun">
                        	<div class="lianyun_select">
                                <label><span class="xing" style="display:none;">*</span>联运类型：</label>
                                <select id="intermodalType" name="intermodalType" onchange="transportchgAir(this)" class="" style="display: inline-block;">
                                    <option id="national" value="1">全国联运</option>
                                    <option id="group" value="2">分区联运</option>
                                    <option selected="selected" id="none" value="0">无联运</option>
                                </select>
                            </div>
                            <div id="nationalTrans" class="transport_city" style="display: none;">
                                <label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  </c:forEach>
                                </select>
                                <input type="text" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="8" name="intermodalAllPrice" id="intermodalAllPrice" /><span class="currency">元</span>
                            </div>
                            <div id="groupTrans" class="transport_city" style="display: none;">
                                <p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart" name="intermodalGroupPart" maxlength="50" type="text"><label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  	</c:forEach>
                                </select>&nbsp;<input id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="8" type="text" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" /><span class="currency">元</span><a class="ydbz_s transportAdd" onclick="javascript:transportAdd(this, 0);">增加</a></p>
                            </div>
                        </div>
                        <!--联运结束-->         
                        <!--币种开始-->
                        <select id="templateCurrency" name="selectCurrency" class="sel-currency" style="display:none;">
                            <c:forEach items="${curlist}" var="currency">
                                <option value="${currency.id}">${currency.currencyName}</option>
                            </c:forEach>
                        </select>
                        <!--币种结束-->   
                             
					<div class="kong"></div>
                              <!--占位方式开始-->
                        <div class="seach25 seach100">
                            <p class="fbold f14"><span class="xing">*</span>付款方式：</p>
                            <p class="seach_r add-paytype">
                                <input type="checkbox" class="ckb_mod error" id="payMode_deposit" name="payMode_deposit" value="1" onclick="paychg(this)"><font>订金占位</font>&#12288;&#12288;
                                <label for="spinner" class="txt2" id="label_deposit"><span class="xing" id="deposit_xing" style="display: none;">*</span>保留时限：</label>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration" class="spinner" name="remainDays_deposit" maxlength="3" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">天</span>
                                </span>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration_hour" class="spinner_hour" name="remainDays_deposit_hour" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">时</span>
                                </span>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration_fen" class="spinner_fen" name="remainDays_deposit_fen" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">分</span>
                                </span>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_advance" name="payMode_advance" value="1" onclick="paychg(this)"><font>预占位</font>&#12288;&#12288;&#12288;
                                <label for="spinner" class="txt2" id="label_advance"><span class="xing" id="advance_xing" style="display: none;">*</span>保留时限：</label>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration" class="spinner" name="remainDays_advance" maxlength="3" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">天</span>
                                </span>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration_hour" class="spinner_hour" name="remainDays_advance_hour" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">时</span>
                                </span>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration_fen" class="spinner_fen" name="remainDays_advance_fen" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">分</span>
                                </span>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_full" name="payMode_full" value="1"><font>全款占位</font>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_op" name="payMode_op" value="1"><font>计调确认占位</font>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_cw" name="payMode_cw" value="1"><font>财务确认占位</font>
                            </p>
                            <div class="kongr"></div>
                        </div>
                        <!--占位方式结束-->
                        </div>
                        </form>
                        
                        <!-- 往返 -->
                   		<form action="${ctx}/airTicket/secondform" method="post" id="frm2">
                            <input type="hidden" name="airType" value="2" />
                            <input type="hidden" name="sectionNum" value="2" />
                            <input type="hidden" name="intermodalInfo" value="" />
                            <input type="hidden" name="txt_ticketId" value="${airticket.id}" />
                        	<div class="inquiry_flights1">
                        	
                        	<c:forEach items="${airticket.flightInfos}" var="list" varStatus="listIndex">
                        	<c:if test="${list.number==1}">
                        	<div class="seach25 seach100">
                        		<span class="seach_check"><label for="radio14"><input name="flightArea" id="radio14" type="radio" value="4" <c:if test="${list.ticket_area_type eq '4'}">checked="checked"</c:if> > 国内</label></span>
                        		<span class="seach_check"><label for="radio12"><input name="flightArea" id="radio12" type="radio" value="2" <c:if test="${list.ticket_area_type eq '2'}">checked="checked"</c:if>> 国际</label></span>
                        		<span class="seach_check"><label for="radio11"><input name="flightArea" id="radio11" type="radio" value="1" <c:if test="${list.ticket_area_type eq '1'}">checked="checked"</c:if> > 内陆</label></span>                      		
                                <span class="seach_check"><label for="radio13"><input name="flightArea" id="radio13" type="radio" value="3" <c:if test="${list.ticket_area_type eq '3'}">checked="checked"</c:if>> 国际+内陆</label></span>
                        	</div>
                        		<div class="seach25">
                              	<p><span class="xing">*</span>出发城市：</p>
                              	<p class="seach_r"><select class="selectinput" id="departureCity" name="departureCity">      
                                    <c:forEach var="dic" items="${from_areaslist}">
                                            <option value="${dic.value}" <c:if test="${airticket.departureCity==dic.value}">selected="selected"</c:if>>${dic.label}</option>
                                        </c:forEach>
                                </select></p>
	                            </div>  
	                            <div class="seach25">
	                                <p><span class="xing">*</span>到达城市：</p>
	                                <p class="seach_r"><select class="selectinput" id="arrivedCity" name="arrivedCity">      
	                                   <c:forEach var="area" items="${arrivedareas}">
	                                            <option value="${area.id}" <c:if test="${airticket.arrivedCity==area.id}">selected="selected"</c:if>>${area.name}</option>
	                                        </c:forEach>
	                                </select></p>
	                            </div> 
	                             <div class="seach25">
	                                <p><span class="xing">*</span>预收人数 ：</p>
	                                <p class="seach_r"><input type="text" id="reservationsNum" name="reservationsNum"  maxlength="10" value="${airticket.reservationsNum}"  onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></p>
	                            </div> 
	                            
                                <div class="kong"></div>
                       
							</c:if>
                            
                      		<div class="title_samil">
								<c:choose>
                                    <c:when test="${list.number == 1}">去程：</c:when>
                                    <c:when test="${list.number == 2}">返程：</c:when>
                                </c:choose>
							</div>
                            <div class="kong"></div>
                            <div class="seach25">
                              	<p><span class="xing">*</span>出发机场：</p>
                              	<p class="seach_r">
	                              	<div class="input-append">
	                              		<input id="wf_leaveAirport${listIndex.index}Id" name="leaveAirport" type="hidden" value="air${list.leaveAirport}"/> 
										<input id="wf_leaveAirport${listIndex.index}Name" name="leaveAirportName" readonly="readonly" type="text" value="${list.paraMap.leaveAirport}" class="appendtext"/>
										<a id="wf_leaveAirport${listIndex.index}" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
									</div>
								</p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达机场：</p>
                                <p class="seach_r">
                                <div class="input-append">
                                	<input id="wf_desAirpost${listIndex.index}Id" name="desAirpost" type="hidden" value="air${list.destinationAirpost}" /> 
									<input id="wf_desAirpost${listIndex.index}Name" name="desAirpostName" readonly="readonly" type="text" value="${list.paraMap.destinationAirpost}" class="appendtext"/> 
									<a id="wf_desAirpost${listIndex.index}" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
	                 				
                     			</div>
                     			</p> 
                            </div>
                            <div class="kong"></div>
                      		<div class="seach25">
                              	<p>出发时刻：</p>
                              	<p class="seach_r"><input type="text" class="dateinput" name="startTime" value="<fmt:formatDate value="${list.startTime}" pattern="yyyy-MM-dd HH:mm"/>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"  ></p>
                            </div>
                            <div class="seach25">
                            	<p>到达时刻：</p>
                              	<p class="seach_r"><input type="text" class="dateinput" name="arrivalTime" value="<fmt:formatDate value="${list.arrivalTime}" pattern="yyyy-MM-dd HH:mm"/>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                            </div>
                            <div class="seach25">
                            	<p>航班号：</p>
                              	<p class="seach_r"><input type="text" name="flightNumber"  maxlength="20" value="${list.flightNumber}"></p>
                            </div>
                            
                            <div class="kong"></div>
                            <div class="seach25">
                        		<p>航空公司：</p>
                       			<p class="seach_r"> <select name="airlines" id="airlines"  onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
                       			<option value="">不限</option>
                                   <c:forEach var="airlines" items="${airlines_list}">
                                       <option value="${airlines.airlineCode}" <c:if test="${list.airlines==airlines.airlineCode}">selected="selected"</c:if>>${airlines.airlineName}</option>
                                   </c:forEach>
                                </select></p>
                      		</div>
                      		<div class="seach25">
                        		<p>舱位等级：</p>
                       			<p class="seach_r"> <select name="spaceGrade" onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
                                 <option value="-1">不限</option>
                                  <c:forEach var="obj" items="${list.paraMap.airLevel}">
                                       <option value="${obj.levelCode}" <c:if test="${list.spaceGrade eq obj.levelCode}">selected="selected"</c:if>>${obj.levelName}</option>
                                   </c:forEach>
                                </select></p>
                      		</div>
                          	<div class="seach25">
                            	<p>舱位：</p>
                            	<p class="seach_r"><select name="airSpace" id="">
                            	     <option value="-1">不限</option>
                            		<c:forEach var="obj" items="${list.paraMap.airSpace}">
                                       <option value="${obj.spaceCode}" <c:if test="${list.airspace eq obj.spaceCode}">selected="selected"</c:if>>${obj.spaceName}</option>
                                   </c:forEach>
                                </select></p>
                            </div>
                            <div class="kong"></div>
                            </c:forEach>
                            
                             <div class="kongr"></div>
                             
                             <!--出票日期开始-->
					<div class="seach25">
						<p class="fbold f14">出票日期：</p>
						<div><input type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="<fmt:formatDate value="${airticket.outTicketTime}" pattern="yyyy-MM-dd"/>" name="outTicketTime" class="dateinput"></div>
					</div>
					<!--出票日期结束-->
					<!--离境口岸开始-->
                      <div class="seach25">
                           <p class="fbold f14"><span class="xing">*</span>离境口岸：</p>
                           <select id="outArea" name="outArea" class="" style="display: inline-block;" onchange="outAreachg()">
                           <option selected="selected" value="-1">请选择</option>
                                <c:forEach var="dic" items="${out_areaslist}">
								<option value="${dic.id}" <c:if test="${airticket.outArea eq dic.id}">selected="selected"</c:if>>${dic.label}</option>
								</c:forEach>
                           </select>
                      </div>
                        <!--离境口岸结束-->
                        <div class="kongr"></div>
                        <!--联运开始-->
                        <div class="mod_information_d2 lianyun">
                        	<div class="lianyun_select">
                                <label><span class="xing" style="display:none;">*</span>联运类型：</label>
                                <select id="intermodalType" name="intermodalType" onchange="transportchgAir(this)" class="" style="display: inline-block;">
                                    <option id="national" value="1" <c:if test="${airticket.intermodalType==1}">selected="selected"</c:if>>全国联运</option>
                                    <option id="group" value="2" <c:if test="${airticket.intermodalType==2}">selected="selected"</c:if>>分区联运</option>
                                    <option id="none" value="0" <c:if test="${airticket.intermodalType==0}">selected="selected"</c:if>>无联运</option>
                                </select>
                            </div>
                            <div id="nationalTrans" class="transport_city" style="display: <c:if test="${airticket.intermodalType!=1}">none</c:if>;">
                                <label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  </c:forEach>
                                </select>
                                <input type="text" value="<fmt:formatNumber value='${airticket.intermodalStrategies.size()>0?airticket.intermodalStrategies.get(0).price:null}' pattern='#.##' />" onafterpaste="isMoney(this)" onkeyup="isMoney(this)" maxlength="8" name="intermodalAllPrice" id="intermodalAllPrice" /><span class="currency">元</span>
                            </div>
                            <div id="groupTrans" class="transport_city" style="display: <c:if test="${airticket.intermodalType!=2}">none</c:if>;">
                                
                                <c:if test="${airticket.intermodalStrategies.size()==0}">
                                <p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart" name="intermodalGroupPart" maxlength="50" type="text"><label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  	</c:forEach>
                                </select>&nbsp;<input id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="8" type="text" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" /><span class="currency">元</span><a class="ydbz_s transportAdd" onclick="javascript:transportAdd(this, 0);">增加</a></p>
                                </c:if>
                                
                                <c:forEach items="${airticket.intermodalStrategies}" var="intermodalInfo" varStatus="status">
                                <p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart" name="intermodalGroupPart" value="${intermodalInfo.groupPart}" maxlength="50" type="text"><label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${intermodalInfo.priceCurrency.id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  	</c:forEach>
                                </select>&nbsp;<input id="intermodalGroupPrice" name="intermodalGroupPrice" value="${intermodalInfo.price}" maxlength="8" type="text" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" /><span class="currency">元</span>
                                <c:if test="${status.index==0}">
                                <a class="ydbz_s transportAdd" onclick="javascript:transportAdd(this, 0);">增加</a>
                                </c:if>
                                <c:if test="${status.index!=0}">
                                <a class="ydbz_s gray transportDel">删除</a>
                                </c:if>
                                </p>
                                </c:forEach>
                            </div>
                        </div>
                        <!--联运结束-->
                        <select id="templateCurrency" name="selectCurrency" class="sel-currency" style="display:none;">
                            <c:forEach items="${curlist}" var="currency">
                                <option value="${currency.id}">${currency.currencyName}</option>
                            </c:forEach>
                        </select>


                             
					<div class="kong"></div>
                            <!--占位方式开始-->
                        <div class="seach25 seach100">
                            <p class="fbold f14"><span class="xing">*</span>付款方式：</p>
                            <p class="seach_r add-paytype">
                                <input type="checkbox" class="ckb_mod error" id="payMode_deposit" name="payMode_deposit" value="1" onclick="paychg(this)" <c:if test="${airticket.payMode_deposit==1}">checked="checked"</c:if>   ><font>订金占位</font>&#12288;&#12288;
                                <label for="spinner" class="txt2" id="label_deposit"><span class="xing" id="deposit_xing" style="display: none;">*</span>保留时限：</label>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration" class="spinner" name="remainDays_deposit" maxlength="3" <c:if test="${airticket.payMode_deposit!=1}">value="" disabled="disabled"</c:if><c:if test="${airticket.payMode_deposit==1}">value="${airticket.remainDays_deposit}"</c:if> onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">天</span>
                                </span>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration_hour" class="spinner_hour" name="remainDays_deposit_hour" maxlength="2" <c:if test="${airticket.payMode_deposit!=1}">value="" disabled="disabled"</c:if><c:if test="${airticket.payMode_deposit==1}">value="${airticket.remainDays_deposit_hour}"</c:if> onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">时</span> 
                                </span>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration_fen" class="spinner_fen" name="remainDays_deposit_fen" maxlength="2" <c:if test="${airticket.payMode_deposit!=1}">value="" disabled="disabled"</c:if><c:if test="${airticket.payMode_deposit==1}">value="${airticket.remainDays_deposit_fen}"</c:if> onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">分</span>
                                </span>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_advance" name="payMode_advance" value="1" onclick="paychg(this)" <c:if test="${airticket.payMode_advance==1}">checked="checked"</c:if>><font>预占位</font>&#12288;&#12288;&#12288;
                                <label for="spinner" class="txt2" id="label_advance"><span class="xing" id="advance_xing" style="display: none;">*</span>保留时限：</label>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration" class="spinner" name="remainDays_advance" maxlength="3" <c:if test='${airticket.payMode_advance!=1}'>value="" disabled="disabled"</c:if><c:if test='${airticket.payMode_advance==1}'>value="${airticket.remainDays_advance}"</c:if> onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">天</span>
                                </span>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration_hour" class="spinner_hour" name="remainDays_advance_hour" maxlength="2" <c:if test='${airticket.payMode_advance!=1}'>value="" disabled="disabled"</c:if><c:if test='${airticket.payMode_advance==1}'>value="${airticket.remainDays_advance_hour}"</c:if> onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">时</span>
                                </span>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration_fen" class="spinner_fen" name="remainDays_advance_fen" maxlength="2" <c:if test='${airticket.payMode_advance!=1}'>value="" disabled="disabled"</c:if><c:if test='${airticket.payMode_advance==1}'>value="${airticket.remainDays_advance_fen}"</c:if> onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">分</span>
                                </span>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_full" name="payMode_full" value="1" <c:if test="${airticket.payMode_full==1}">checked="checked"</c:if>><font>全款占位</font>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_op" name="payMode_op" value="1" <c:if test="${airticket.payMode_op==1}">checked="checked"</c:if>><font>计调确认占位</font>
                                
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_cw" name="payMode_cw" value="1" <c:if test="${airticket.payMode_cw==1}">checked="checked"</c:if>><font>财务确认占位</font>
                            </p>
                            <div class="kongr"></div>
                        </div>
                        <!--占位方式结束-->
                        </div>
                        </form>
                        
                        <!-- 多段 -->
                        <form action="${ctx}/airTicket/secondform" method="post"  id="frm1">
                                <input type="hidden" name="airType" value="1" />
                                <input type="hidden" id="moreSenum" name="sectionNum" value="2" />
                                <input type="hidden" name="intermodalInfo" value="" />
                                <input type="hidden" name="txt_ticketId" value="${airticket.id}" />
                           
                      		<div class="inquiry_flights3">
                      		 <div>
                        	<p class="main-right-topbutt"><a class="primary" href="javascript:void(0);" onclick="inquiryFlights3Add(this)">新增航段</a></p>
                            <div class="seach25">
                              	<p><span class="xing">*</span>出发城市：</p>
                              	<p class="seach_r"><select class="selectinput" id="departureCity" name="departureCity">      
                                    <c:forEach var="dic" items="${from_areaslist}">
                                            <option value="${dic.value}" <c:if test="${airticket.departureCity==dic.value}">selected="selected"</c:if>>${dic.label}</option>
                                        </c:forEach>
                                </select></p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达城市：</p>
                                <p class="seach_r"><select class="selectinput" name="arrivedCity">      
                                  <c:forEach var="area" items="${arrivedareas}">
                                            <option value="${area.id}" <c:if test="${airticket.arrivedCity==area.id}">selected="selected"</c:if>>${area.name}</option>
                                        </c:forEach>
                                </select></p>
                            </div>
                             <div class="seach25">
                                <p><span class="xing">*</span>预收人数 ：</p>
                                <p class="seach_r"><input type="text" id="reservationsNum"  maxlength="10"  name="reservationsNum" value=""></p>
                            </div> 
                            <div class="kong"></div>
                            <div class="addFlights3Div">
                                <div class="title_samil">第1段：
                                	<span class="seach_check"><label for="radio31-4"><input name="flightArea1" type="radio" value="4"> 国内</label></span>
                                	<span class="seach_check"><label for="radio31-2"><input name="flightArea1" type="radio" value="2"> 国际</label></span>
                                	<span class="seach_check"><label for="radio31-1"><input name="flightArea1" type="radio" value="1"> 内陆</label></span>                                    
                                    <span class="seach_check"><label for="radio13-3"><input name="flightArea1" type="radio" value="3"> 国际+内陆</label></span>
                                </div>
                            <div class="seach25">
                              	<p><span class="xing">*</span>出发机场：</p>
                              	<p class="seach_r"><div class="input-append">
	                            <div class="input-append">
									<input id="mleaveAirport1Id" name="leaveAirport" type="hidden" value=""/>
									<input id="mleaveAirport1Name" name="leaveAirportName" readonly="readonly" type="text" value="" class="appendtext"/>
									<a id="mleaveAirport1" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div> 
</div></p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达机场：</p>
                                <p class="seach_r"><div class="input-append">
                                
                                <div class="input-append">
									<input id="mdesAirpost1Id" name="desAirpost" type="hidden" value=""/>
									<input id="mdesAirpost1Name" name="desAirpostName" readonly="readonly" type="text" value="" class="appendtext"/>
									<a id="mdesAirpost1" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div> 
</div></p>
                            </div>
                            <div class="kong"></div>
                                <div class="seach25">
                                    <p>出发时刻：</p>
                                    <p class="seach_r"><input type="text" class="dateinput"  name="startTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                                </div>
                                <div class="seach25">
                                    <p>到达时刻：</p>
                                    <p class="seach_r"><input type="text" class="dateinput" name="arrivalTime" value=""  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                                </div>
                                <div class="seach25">
	                            	<p>航班号：</p>
	                              	<p class="seach_r"><input type="text" name="flightNumber"  maxlength="20" value=""></p>
	                            </div>
                                <div class="kong"></div>
                                <div class="seach25">
                                    <p>航空公司：</p>
                                    <p class="seach_r"> <select name="airlines" id="airlines" onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
                                    	<option value="">不限</option>
                                      <c:forEach var="airlines" items="${airlines_list}">
                                            <option value="${airlines.airlineCode}">${airlines.airlineName}</option>
                                        </c:forEach>
                                    </select></p>
                                </div>
                                <div class="seach25">
                                    <p>舱位等级：</p>
                                    <p class="seach_r"> <select name="spaceGrade" onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
                                      <option value="-1">不限</option>
                                    </select></p>
                                </div>
                                <div class="seach25">
                                  <p>舱位：</p>
                                  <p class="seach_r"><select name="airSpace">
                                  <option value="-1">不限</option>
                                  </select></p>
                                </div>
                                <div class="kong"></div>
                            </div>
                            <div class="addFlights3Div">
                                <div class="title_samil">第2段：
                                	<span class="seach_check"><label for="radio32-4"><input name="flightArea2" id="flightArea3" type="radio" value="4"> 国内</label></span>
                                    <span class="seach_check"><label for="radio32-2"><input name="flightArea2" id="radio32-2" type="radio" value="2"> 国际</label></span>
                                    <span class="seach_check"><label for="radio32-1"><input name="flightArea2" id="flightArea2" type="radio" value="1"> 内陆</label></span>                           
                                    <span class="seach_check"><label for="radio32-3"><input  name="flightArea2"id="radio13" type="radio" value="3"> 国际+内陆</label></span>
                                </div>
                            <div class="seach25">
                              	<p><span class="xing">*</span>出发机场：</p>
                              	<p class="seach_r"><div class="input-append">
                              	<div class="input-append">
									<input id="mleaveAirport2Id" name="leaveAirport" type="hidden" value=""/>
									<input id="mleaveAirport2Name" name="leaveAirportName" readonly="readonly" type="text" value="" class="appendtext"/>
									<a id="mleaveAirport2" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div> 
								</div></p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达机场：</p>
                                <p class="seach_r"><div class="input-append">
                                
                                <div class="input-append">
									<input id="mdesAirpost2Id" name="desAirpost" type="hidden" value=""/>
									<input id="mdesAirpost2Name" name="desAirpostName" readonly="readonly" type="text" value="" class="appendtext"/>
									<a id="mdesAirpost2" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div> 
                                 </div>
                            	</p>
                            </div>
                            <div class="kong"></div>
                                <div class="seach25">
                                    <p>出发时刻：</p>
                                    <p class="seach_r"><input type="text" class="dateinput" name="startTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                                </div>
                                <div class="seach25">
                                    <p>到达时刻：</p>
                                    <p class="seach_r"><input type="text" class="dateinput" name="arrivalTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                                </div>
                                <div class="seach25">
	                            	<p>航班号：</p>
	                              	<p class="seach_r"><input type="text"  maxlength="20" name="flightNumber" value=""></p>
	                            </div>
                                <div class="kong"></div>
                                <div class="seach25">
                                    <p>航空公司：</p>
                                    <p class="seach_r"> <select name="airlines" id="airlines" onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
                                    <option value="">不限</option>
                                    <c:forEach var="airlines" items="${airlines_list}">
                                            <option value="${airlines.airlineCode}">${airlines.airlineName}</option>
                                        </c:forEach>
                                    </select></p>
                                </div>
                                <div class="seach25">
                                    <p>舱位等级：</p>
                                    <p class="seach_r"> <select name="spaceGrade" onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
                                      <option value="-1">不限</option>
                                    
                                    </select></p>
                                </div>
                                <div class="seach25">
                                    <p>舱位：</p>
                                    <p class="seach_r"><select name="airSpace" id="">
                                     <option value="-1">不限</option>
                                    </select></p>
                                </div>
                                <div class="kong"></div>
                            </div>
                           
                            
                            </div>
                            <div class="kongr"></div>
                            
                            <!--出票日期开始-->
					<div class="seach25">
						<p class="fbold f14">出票日期：</p>
						<div><input type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="" name="outTicketTime" class="dateinput"></div>
					</div>
					<!--出票日期结束-->
					<!--离境口岸开始-->
                      <div class="seach25">
                           <p class="fbold f14"><span class="xing">*</span>离境口岸：</p>
                           <select id="outArea" name="outArea" class="" style="display: inline-block;" onchange="outAreachg()">
                           <option selected="selected" value="-1">请选择</option>
                                <c:forEach var="dic" items="${out_areaslist}">
								<option value="${dic.id}">${dic.label}</option>
								</c:forEach>
                           </select>
                      </div>
                        <!--离境口岸结束-->
                        <div class="kongr"></div>
                        <!--联运开始-->
                        <div class="mod_information_d2 lianyun">
                        	<div class="lianyun_select">
                                <label><span class="xing" style="display:none;">*</span>联运类型：</label>
                                <select id="intermodalType" name="intermodalType" onchange="transportchgAir(this)" class="" style="display: inline-block;">
                                    <option id="national" value="1">全国联运</option>
                                    <option id="group" value="2">分区联运</option>
                                    <option selected="selected" id="none" value="0">无联运</option>
                                </select>
                            </div>
                            <div id="nationalTrans" class="transport_city" style="display: none;">
                                <label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  </c:forEach>
                                </select>
                                <input type="text" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="8" name="intermodalAllPrice" id="intermodalAllPrice" /><span class="currency">元</span>
                            </div>
                            <div id="groupTrans" class="transport_city" style="display: none;">
                                <p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart" name="intermodalGroupPart" maxlength="50" type="text"><label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  	</c:forEach>
                                </select>&nbsp;<input id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="8" type="text" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" /><span class="currency">元</span><a class="ydbz_s transportAdd" onclick="javascript:transportAdd(this, 0);">增加</a></p>
                            </div>
                        </div>
                        <!--联运结束-->
                        <select id="templateCurrency" name="selectCurrency" class="sel-currency" style="display:none;">
                            <c:forEach items="${curlist}" var="currency">
                                <option value="${currency.id}">${currency.currencyName}</option>
                            </c:forEach>
                        </select>



					<div class="kong"></div>
                             <!--占位方式开始-->
                        <div class="seach25 seach100">
                            <p class="fbold f14"><span class="xing">*</span>付款方式：</p>
                            <p class="seach_r add-paytype">
                                <input type="checkbox" class="ckb_mod error" id="payMode_deposit" name="payMode_deposit" value="1" onclick="paychg(this)"><font>订金占位</font>&#12288;&#12288;
                                <label for="spinner" class="txt2" id="label_deposit"><span class="xing" id="deposit_xing" style="display: none;">*</span>保留时限：</label>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration" class="spinner" name="remainDays_deposit" maxlength="3" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">天</span>
                                </span>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration_hour" class="spinner_hour" name="remainDays_deposit_hour" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">时</span> 
                                </span>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration_fen" class="spinner_fen" name="remainDays_deposit_fen" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">分</span>
                                </span>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_advance" name="payMode_advance" value="1" onclick="paychg(this)"><font>预占位</font>&#12288;&#12288;&#12288;
                                <label for="spinner" class="txt2" id="label_advance"><span class="xing" id="advance_xing" style="display: none;">*</span>保留时限：</label>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration" class="spinner" name="remainDays_advance" maxlength="3" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">天</span>
                                </span>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration_hour" class="spinner_hour" name="remainDays_advance_hour" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">时</span>
                                </span>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration_fen" class="spinner_fen" name="remainDays_advance_fen" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">分</span>
                                </span>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_full" name="payMode_full" value="1"><font>全款占位</font>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_op" name="payMode_op" value="1"><font>计调确认占位</font>
                                 <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_cw" name="payMode_cw" value="1"><font>财务确认占位</font>
                            </p>
                            <div class="kongr"></div>
                        </div>
                        <!--占位方式结束-->
                            </div>
                      	</form>
                      	
                      	<div class="addFlights3None addFlights3Div" style="display:none;" id="hiddenFilght">
                                <div class="title_samil">第<em>3</em>段：
                                	<span class="seach_check"><label><input type="radio" name="flightAreax" value="4" /> 国内</label></span>
                                	<span class="seach_check"><label><input type="radio" name="flightAreax" value="2"/> 国际</label></span>
                                	<span class="seach_check"><label><input type="radio" name="flightAreax" value="1" /> 内陆</label></span>            
                                    <span class="seach_check"><label><input type="radio" name="flightAreax" value="3"/> 国际+内陆</label></span>
                                    <p class="main-right-topbutt"><a  href="javascript:void(0);" onclick="inquiryFlights3Del(this);" style="margin-right:40px;">删除路段</a></p>
                                </div>
                            <div class="seach25">
                              	<p><span class="xing">*</span>出发机场：</p>
                              	<p class="seach_r"><div class="input-append">
                              	<div class="input-append">
									<input id="mleaveAirportxId" name="leaveAirport" type="hidden" value=""/>
									<input id="mleaveAirportxName" name="leaveAirportName" readonly="readonly" type="text" value="" class="appendtext"/>
									<a id="mleaveAirportx" name="leaveHref" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div> 
								</div></p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达机场：</p>
                                <p class="seach_r"><div class="input-append">
                                
                                <div class="input-append">
									<input id="mdesAirpostxId" name="desAirpost" type="hidden" value=""/>
									<input id="mdesAirpostxName" name="desAirpostName" readonly="readonly" type="text" value="" class="appendtext"/>
									<a id="desAirpostx" name="desHref" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div> 
                                 </div>
                            	</p>
                            </div>
                            <div class="kong"></div>
                                <div class="seach25">
                                    <p>出发时刻：</p>
                                    <p class="seach_r"><input type="text" class="dateinput" name="startTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                                </div>
                                <div class="seach25">
                                    <p>到达时刻：</p>
                                    <p class="seach_r"><input type="text" class="dateinput" name="arrivalTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                                </div>
                                <div class="seach25">
	                            	<p>航班号：</p>
	                              	<p class="seach_r"><input type="text"  maxlength="20" name="flightNumber" value=""></p>
	                            </div>
                                <div class="kong"></div>
                                <div class="seach25">
                                    <p>航空公司：</p>
                                    <p class="seach_r"> <select name="airlines" id="airlines" onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
                                    <option value="">不限</option>
                                     <c:forEach var="airlines" items="${airlines_list}">
                                            <option value="${airlines.airlineCode}">${airlines.airlineName}</option>
                                        </c:forEach>
                                    </select></p>
                                </div>
                                <div class="seach25">
                                    <p>舱位等级：</p>
                                    <p class="seach_r"> <select name="spaceGrade"  onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
                                      <option value="-1">不限</option>
                                    
                                    </select></p>
                                </div>
                                <div class="seach25">
                                    <p>舱位：</p>
                                    <p class="seach_r"><select name="airSpace">
                                    <option value="-1">不限</option>
                                    </select></p>
                                </div>
                                <div class="kong"></div>
                            </div>
                        </c:when>
                        
                        <c:when test="${airticket.airType eq '3'}">
                        <!-- 单程情况 -->
                        
                        <!-- 单程 -->
                        <form action="${ctx}/airTicket/secondform" method="post" id="frm3">
                        <input type="hidden" name="airType" value="3" />
                        <input type="hidden" name="sectionNum" value="1" />
                        <input type="hidden" name="intermodalInfo" value="" />
                        <input type="hidden" name="txt_ticketId" value="${airticket.id}" />        
                      	<div class="inquiry_flights2">
                      	<c:forEach items="${airticket.flightInfos}" var="list" varStatus="listIndex">
                        	<div class="seach25 seach100">
                        		<span class="seach_check"><label for="radio14"><input name="flightArea" id="radio14" type="radio" value="4" <c:if test="${list.ticket_area_type eq '4'}">checked="checked"</c:if> > 国内</label></span>
                        		<span class="seach_check"><label for="radio12"><input name="flightArea" id="radio12" type="radio" value="2" <c:if test="${list.ticket_area_type eq '2'}">checked="checked"</c:if>> 国际</label></span>
                        		<span class="seach_check"><label for="radio11"><input name="flightArea" id="radio11" type="radio" value="1" <c:if test="${list.ticket_area_type eq '1'}">checked="checked"</c:if> > 内陆</label></span>       		
                                <span class="seach_check"><label for="radio13"><input name="flightArea" id="radio13" type="radio" value="3" <c:if test="${list.ticket_area_type eq '3'}">checked="checked"</c:if>> 国际+内陆</label></span>
                        	</div>
                       		<div class="seach25">
                             	<p><span class="xing">*</span>出发城市：</p>
                             	<p class="seach_r"><select class="selectinput" id="departureCity" name="departureCity">      
                                   <c:forEach var="dic" items="${from_areaslist}">
                                           <option value="${dic.value}" <c:if test="${airticket.departureCity==dic.value}">selected="selected"</c:if>>${dic.label}</option>
                                       </c:forEach>
                               </select></p>
                            </div>  
                            <div class="seach25">
                                <p><span class="xing">*</span>到达城市：</p>
                                <p class="seach_r"><select class="selectinput" id="arrivedCity" name="arrivedCity">      
                                   <c:forEach var="area" items="${arrivedareas}">
                                            <option value="${area.id}" <c:if test="${airticket.arrivedCity==area.id}">selected="selected"</c:if>>${area.name}</option>
                                        </c:forEach>
                                </select></p>
                            </div> 
                             <div class="seach25">
                                <p><span class="xing">*</span>预收人数 ：</p>
                                <p class="seach_r"><input type="text" id="reservationsNum" name="reservationsNum"  maxlength="10" value="${airticket.reservationsNum}"  onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></p>
                            </div> 
                            <div class="kong"></div>
                            
                            <div class="seach25">
                              	<p><span class="xing">*</span>出发机场：</p>
                              	<p class="seach_r"><div class="input-append">
			                       	<input id="leaveAirportId" name="leaveAirport" type="hidden" value="air${list.leaveAirport}"/> 
									<input id="leaveAirportName" name="leaveAirportName" readonly="readonly" type="text" value="${list.paraMap.leaveAirport}" class="appendtext"/>
									<a id="leaveAirport" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
							</div></p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达机场：</p>
                                <p class="seach_r"><div class="input-append">
				                     <input id="desAirpostId" name="desAirpost" type="hidden" value="air${list.destinationAirpost}" /> 
									<input id="desAirpostName" name="desAirpostName" readonly="readonly" type="text" value="${list.paraMap.destinationAirpost}" class="appendtext"/> 
									<a id="desAirpost" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
							</div></p>
                            </div>
                            <div class="kong"></div>
                            <div class="seach25">
                                <p>出发时刻：</p>
                                <p class="seach_r"><input type="text" class="dateinput"  name="startTime" value="<fmt:formatDate value="${list.startTime}" pattern="yyyy-MM-dd HH:mm"/>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                            </div>
                            <div class="seach25">
                                <p>到达时刻：</p>
                                <p class="seach_r"><input type="text" class="dateinput"  name="arrivalTime" value="<fmt:formatDate value="${list.arrivalTime}" pattern="yyyy-MM-dd HH:mm"/>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                            </div>
                            <div class="seach25">
                            	<p>航班号：</p>
                              	<p class="seach_r"><input type="text" name="flightNumber"  maxlength="20"  value="${list.flightNumber}"></p>
                            </div>
                      		<div class="kong"></div>
                            <div class="seach25">
                                <p>航空公司：</p>
                                <p class="seach_r"> <select name="airlines" id="airlines" onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
                                	<option value="">不限</option>
                                    <c:forEach var="airlines" items="${airlines_list}">
                                            <option value="${airlines.airlineCode}" <c:if test="${list.airlines==airlines.airlineCode}">selected="selected"</c:if>>${airlines.airlineName}</option>
                                        </c:forEach>
                                </select></p>
                            </div>
                            <div class="seach25">
                        		<p>舱位等级：</p>
                       			<p class="seach_r"> <select name="spaceGrade" onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
                                  <option value="-1">不限</option>
                                  <c:forEach var="obj" items="${list.paraMap.airLevel}">
                                       <option value="${obj.levelCode}" <c:if test="${list.spaceGrade eq obj.levelCode}">selected="selected"</c:if>>${obj.levelName}</option>
                                   </c:forEach>
                                </select></p>
                      		</div>
                          	<div class="seach25">
                            	<p>舱位：</p>
                            	<p class="seach_r"><select name="airSpace" id="">
                            		<option value="-1">不限</option>
                            		<c:forEach var="obj" items="${list.paraMap.airSpace}">
                                       <option value="${obj.spaceCode}" <c:if test="${list.airspace eq obj.spaceCode}">selected="selected"</c:if>>${obj.spaceName}</option>
                                   </c:forEach>
                                </select></p>
                            </div>
                            <div class="kong"></div>
                            </c:forEach>
                            
                            <div class="kongr"></div>
                            
                            <!--出票日期开始-->
					<div class="seach25">
						<p class="fbold f14">出票日期：</p>
						<div><input type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="<fmt:formatDate value="${airticket.outTicketTime}" pattern="yyyy-MM-dd"/>" name="outTicketTime" class="dateinput"></div>
					</div>
					<!--离境口岸开始-->
                      <div class="seach25">
                           <p class="fbold f14"><span class="xing">*</span>离境口岸：</p>
                           <select id="outArea" name="outArea" class="" style="display: inline-block;" onchange="outAreachg()">
                           <option selected="selected" value="-1">请选择</option>
                                <c:forEach var="dic" items="${out_areaslist}">
								<option value="${dic.id}" <c:if test="${airticket.outArea eq dic.id}">selected="selected"</c:if>>${dic.label}</option>
								</c:forEach>
                           </select>
                      </div>
                        <!--离境口岸结束-->
                        <div class="kongr"></div>
                        <!--联运开始-->
                        <div class="mod_information_d2 lianyun">
                        	<div class="lianyun_select">
                                <label><span class="xing" style="display:none;">*</span>联运类型：</label>
                                <select id="intermodalType" name="intermodalType" onchange="transportchgAir(this)" class="" style="display: inline-block;">
                                    <option id="national" value="1" <c:if test="${airticket.intermodalType==1}">selected="selected"</c:if>>全国联运</option>
                                    <option id="group" value="2" <c:if test="${airticket.intermodalType==2}">selected="selected"</c:if>>分区联运</option>
                                    <option id="none" value="0" <c:if test="${airticket.intermodalType==0}">selected="selected"</c:if>>无联运</option>
                                </select>
                            </div>
                            <div id="nationalTrans" class="transport_city" style="display: <c:if test="${airticket.intermodalType!=1}">none</c:if>;">
                                <label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  </c:forEach>
                                </select>
                                <input type="text" value="<fmt:formatNumber value='${airticket.intermodalStrategies.size()>0?airticket.intermodalStrategies.get(0).price:null}' pattern='#.##' />" onafterpaste="isMoney(this)" onkeyup="isMoney(this)" maxlength="8" name="intermodalAllPrice" id="intermodalAllPrice" /><span class="currency">元</span>
                            </div>
                            <div id="groupTrans" class="transport_city" style="display: <c:if test="${airticket.intermodalType!=2}">none</c:if>;">
                                <c:if test="${airticket.intermodalStrategies.size()==0}">
                                <p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart" name="intermodalGroupPart" maxlength="50" type="text"><label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  	</c:forEach>
                                </select>&nbsp;<input id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="8" type="text" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" /><span class="currency">元</span><a class="ydbz_s transportAdd" onclick="javascript:transportAdd(this, 0);">增加</a></p>
                                </c:if>
                                
                                <c:forEach items="${airticket.intermodalStrategies}" var="intermodalInfo" varStatus="status">
                                <p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart" name="intermodalGroupPart" value="${intermodalInfo.groupPart}" maxlength="50" type="text"><label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${intermodalInfo.priceCurrency.id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  	</c:forEach>
                                </select>&nbsp;<input id="intermodalGroupPrice" name="intermodalGroupPrice" value="${intermodalInfo.price}" maxlength="8" type="text" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" /><span class="currency">元</span>
                                <c:if test="${status.index==0}">
                                <a class="ydbz_s transportAdd" onclick="javascript:transportAdd(this, 0);">增加</a>
                                </c:if>
                                <c:if test="${status.index!=0}">
                                <a class="ydbz_s gray transportDel">删除</a>
                                </c:if>
                                </p>
                                </c:forEach>
                            </div>
                        </div>
                        <!--联运结束-->
                        <select id="templateCurrency" name="selectCurrency" class="sel-currency" style="display:none;">
                            <c:forEach items="${curlist}" var="currency">
                                <option value="${currency.id}">${currency.currencyName}</option>
                            </c:forEach>
                        </select>
					
					
					<div class="kong"></div>
					<!--出票日期结束-->
                              <!--占位方式开始-->
                        <div class="seach25 seach100">
                            <p class="fbold f14"><span class="xing">*</span>付款方式：</p>
                            <p class="seach_r add-paytype">
                                <input type="checkbox" class="ckb_mod error" id="payMode_deposit" name="payMode_deposit" value="1" onclick="paychg(this)" <c:if test="${airticket.payMode_deposit==1}">checked="checked"</c:if>   ><font>订金占位</font>&#12288;&#12288;
                                <label for="spinner" class="txt2" id="label_deposit"><span class="xing" id="deposit_xing" style="display: none;">*</span>保留时限：</label>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration" class="spinner" name="remainDays_deposit" maxlength="3" <c:if test="${airticket.payMode_deposit!=1}">value="" disabled="disabled"</c:if><c:if test="${airticket.payMode_deposit==1}">value="${airticket.remainDays_deposit}"</c:if> onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">天</span>
                                </span>
                                <span id="payMode_deposit_span"> 
                                    <input id="activityDuration_hour" class="spinner_hour" name="remainDays_deposit_hour" maxlength="2" <c:if test="${airticket.payMode_deposit!=1}">value="" disabled="disabled"</c:if><c:if test="${airticket.payMode_deposit==1}">value="${airticket.remainDays_deposit_hour}"</c:if> onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">时</span>
                                </span>
                                <span id="payMode_deposit_span"> 
                                    <input id="activityDuration_fen" class="spinner_fen" name="remainDays_deposit_fen" maxlength="2" <c:if test="${airticket.payMode_deposit!=1}">value="" disabled="disabled"</c:if><c:if test="${airticket.payMode_deposit==1}">value="${airticket.remainDays_deposit_fen}"</c:if> onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">分</span>
                                </span>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_advance" name="payMode_advance" value="1" onclick="paychg(this)" <c:if test="${airticket.payMode_advance==1}">checked="checked"</c:if>><font>预占位</font>&#12288;&#12288;&#12288;
                                <label for="spinner" class="txt2" id="label_advance"><span class="xing" id="advance_xing" style="display: ${airticket.payMode_advance==1?'':'none'}">*</span>保留时限：</label>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration" class="spinner" name="remainDays_advance" maxlength="3" <c:if test='${airticket.payMode_advance!=1}'>value="" disabled="disabled"</c:if><c:if test='${airticket.payMode_advance==1}'>value="${airticket.remainDays_advance}"</c:if> onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">天</span>
                                </span>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration_hour" class="spinner_hour" name="remainDays_advance_hour" maxlength="2" <c:if test='${airticket.payMode_advance!=1}'>value="" disabled="disabled"</c:if><c:if test='${airticket.payMode_advance==1}'>value="${airticket.remainDays_advance_hour}"</c:if> onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">时</span>
                                </span>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration_fen" class="spinner_fen" name="remainDays_advance_fen" maxlength="2" <c:if test='${airticket.payMode_advance!=1}'>value="" disabled="disabled"</c:if><c:if test='${airticket.payMode_advance==1}'>value="${airticket.remainDays_advance_fen}"</c:if> onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">分</span>
                                </span>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_full" name="payMode_full" value="1" <c:if test="${airticket.payMode_full==1}">checked="checked"</c:if>><font>全款占位</font>
                                
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_op" name="payMode_op" value="1" <c:if test="${airticket.payMode_op==1}">checked="checked"</c:if>><font>计调确认占位</font>
                             <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_cw" name="payMode_cw" value="1" <c:if test="${airticket.payMode_cw==1}">checked="checked"</c:if>><font>财务确认占位</font>
                            
                            
                            
                            </p>
                            <div class="kongr"></div>
                        </div>
                        <!--占位方式结束-->
                        </div>
                        </form>
                        <!-- 往返 -->
                        
                       <!-- 往返 -->
                   		<form action="${ctx}/airTicket/secondform" method="post" id="frm2">
                                <input type="hidden" name="airType" value="2" />
                                <input type="hidden" name="sectionNum" value="2" />
                                <input type="hidden" name="intermodalInfo" value="" />
                                <input type="hidden" name="txt_ticketId" value="${airticket.id}" />
                        	<div class="inquiry_flights1">
                        	<div class="seach25 seach100">
                        		<span class="seach_check"><label for="radio14"><input name="flightArea" id="radio14" type="radio" value="4"> 国内</label></span>
                        		<span class="seach_check"><label for="radio12"><input  name="flightArea" id=" radio12" type="radio" value="2"> 国际</label></span>
                        		<span class="seach_check"><label for="radio11"><input name="flightArea" id="radio11" type="radio" value="1"> 内陆</label></span>
                                <span class="seach_check"><label for="radio13"><input  name="flightArea" id=" radio13" type="radio" value="3"> 国际+内陆</label></span>
                        	</div>

                         	<div class="seach25">
                              	<p><span class="xing">*</span>出发城市：</p>
                              	<p class="seach_r"><select class="selectinput" id="departureCity" name="departureCity">      
                                    <c:forEach var="dic" items="${from_areaslist}">
                                            <option value="${dic.value}" <c:if test="${airticket.departureCity==dic.value}">selected="selected"</c:if>>${dic.label}</option>
                                        </c:forEach>
                                </select></p>
                            </div>  
                            <div class="seach25">
                                <p><span class="xing">*</span>到达城市：</p>
                                <p class="seach_r"><select class="selectinput" id="arrivedCity" name="arrivedCity">      
                                   <c:forEach var="area" items="${arrivedareas}">
                                            <option value="${area.id}" <c:if test="${airticket.arrivedCity==area.id}">selected="selected"</c:if>>${area.name}</option>
                                        </c:forEach>
                                </select></p>
                            </div> 
                             <div class="seach25">
                                <p><span class="xing">*</span>预收人数 ：</p>
                                <p class="seach_r"><input type="text" id="reservationsNum" name="reservationsNum" value="" maxlength="10"  onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></p>
                            </div> 
                      		<div class="title_samil">去程：</div>
                            <div class="kong"></div>
                            <div class="seach25">
                              	<p><span class="xing">*</span>出发机场：</p>
                              	<p class="seach_r">
	                              	<div class="input-append">
		 								<div class="input-append">
											<input id="leaveAirport1Id" name="leaveAirport" type="hidden" value=""/>
											<input id="leaveAirport1Name" name="leaveAirportName" readonly="readonly" type="text" value="" class="appendtext"/>
											<a id="leaveAirport1" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
										</div> 
									</div>
								</p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达机场：</p>
                                <p class="seach_r">
                                <div class="input-append">
                                	<div class="input-append">
										<input id="desAirpost1Id" name="desAirpost" type="hidden" value=""/>
										<input id="desAirpost1Name" name="targetAreaNameList" readonly="readonly" type="text" value="" class="appendtext"/>
										<a id="desAirpost1" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
									</div> 
                     			</div>
                     			</p> 
                            </div>
                            <div class="kong"></div>
                      		<div class="seach25">
                              	<p>出发时刻：</p>
                              	<p class="seach_r"><input type="text" class="dateinput" name="startTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"  ></p>
                            </div>
                            <div class="seach25">
                            	<p>到达时刻：</p>
                              	<p class="seach_r"><input type="text" class="dateinput" name="arrivalTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                            </div>
                            <div class="seach25">
                            	<p>航班号：</p>
                              	<p class="seach_r"><input type="text" name="flightNumber"  maxlength="20"  value=""></p>
                            </div>
                            <div class="kong"></div>
                            <div class="seach25">
                        		<p>航空公司：</p>
                       			<p class="seach_r"> <select name="airlines" id="airlines"  onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
                       			<option value="">不限</option>
                                   <c:forEach var="airlines" items="${airlines_list}">
                                            <option value="${airlines.airlineCode}" >${airlines.airlineName}</option>
                                        </c:forEach>
                                </select></p>
                      		</div>
                      		<div class="seach25">
                        		<p>舱位等级：</p>
                       			<p class="seach_r"> <select name="spaceGrade" onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
                                  <option value="-1">不限</option>
                                </select></p>
                      		</div>
                          	<div class="seach25">
                            	<p>舱位：</p>
                            	<p class="seach_r"><select name="airSpace" id="">
                            	<option value="-1">不限</option>
                                </select></p>
                            </div>
                            <div class="kong"></div>
                            
                      		<div class="title_samil">返程：</div>
                            <div class="seach25">
                              	<p><span class="xing">*</span>出发机场：</p>
                              	<p class="seach_r"><div class="input-append">
	                 			<div class="input-append">
									<input id="leaveAirport2Id" name="leaveAirport" type="hidden" value=""/>
									<input id="leaveAirport2Name" name="leaveAirportName" readonly="readonly" type="text" value="" class="appendtext"/>
									<a id="leaveAirport2" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div> 
                     
                     		</div></p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达机场：</p>
                                <p class="seach_r"><div class="input-append">
	                 				<div class="input-append">
										<input id="desAirpost2Id" name="desAirpost" type="hidden" value=""/>
										<input id="desAirpost2Name" name="targetAreaNameList" readonly="readonly" type="text" value="" class="appendtext"/>
										<a id="desAirpost2" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
									</div> 
                     		</div></p>
                            </div>
                            <div class="kong"></div>
                            <div class="seach25">
                                <p>出发时刻：</p>
                                <p class="seach_r"><input type="text" class="dateinput"  name="startTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                            </div>
                            <div class="seach25">
                                <p>到达时刻：</p>
                                <p class="seach_r"><input type="text" class="dateinput" name="arrivalTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                            </div>
                            <div class="seach25">
                            	<p>航班号：</p>
                              	<p class="seach_r"><input type="text" name="flightNumber"  maxlength="20"  value=""></p>
                            </div>
                            <div class="kong"></div>
                            <div class="seach25">
                              	<p>航空公司：</p>
                             	<p class="seach_r"> <select name="airlines" id="airlines" onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
                             	<option value="">不限</option>
                                  <c:forEach var="airlines" items="${airlines_list}">
                                            <option value="${airlines.airlineCode}">${airlines.airlineName}</option>
                                        </c:forEach>
                                </select></p>
                            </div>
                            <div class="seach25">
                                <p>舱位等级：</p>
                                <p class="seach_r"> <select name="spaceGrade" onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
                                  <option value="-1">不限</option>
                                </select></p>
                            </div>
                      		<div class="seach25">
                        		<p>舱位：</p>
                        		<p class="seach_r"><select name="airSpace" id="">
                        		<option value="-1">不限</option>
                                </select></p>
                      		</div>
                            <div class="kong"></div>
                            
                             <div class="kongr"></div>
                             
                             <!--出票日期开始-->
					<div class="seach25">
						<p class="fbold f14">出票日期：</p>
						<div><input type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="" name="outTicketTime" class="dateinput"></div>
					</div>
					<!--出票日期结束-->
					<!--离境口岸开始-->
                      <div class="seach25">
                           <p class="fbold f14"><span class="xing">*</span>离境口岸：</p>
                           <select id="outArea" name="outArea" class="" style="display: inline-block;" onchange="outAreachg()">
                           <option selected="selected" value="-1">请选择</option>
                                <c:forEach var="dic" items="${out_areaslist}">
								<option value="${dic.id}">${dic.label}</option>
								</c:forEach>
                           </select>
                      </div>
                        <!--离境口岸结束-->
                        <div class="kongr"></div>
                        <!--联运开始-->
                        <div class="mod_information_d2 lianyun">
                        	<div class="lianyun_select">
                                <label><span class="xing" style="display:none;">*</span>联运类型：</label>
                                <select id="intermodalType" name="intermodalType" onchange="transportchgAir(this)" class="" style="display: inline-block;">
                                    <option id="national" value="1">全国联运</option>
                                    <option id="group" value="2">分区联运</option>
                                    <option selected="selected" id="none" value="0">无联运</option>
                                </select>
                            </div>
                            <div id="nationalTrans" class="transport_city" style="display: none;">
                                <label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  </c:forEach>
                                </select>
                                <input type="text" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="8" name="intermodalAllPrice" id="intermodalAllPrice" /><span class="currency">元</span>
                            </div>
                            <div id="groupTrans" class="transport_city" style="display: none;">
                                <p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart" name="intermodalGroupPart" maxlength="50" type="text"><label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  	</c:forEach>
                                </select>&nbsp;<input id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="8" type="text" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" /><span class="currency">元</span><a class="ydbz_s transportAdd" onclick="javascript:transportAdd(this, 0);">增加</a></p>
                            </div>
                        </div>
                        <!--联运结束-->
                        <select id="templateCurrency" name="selectCurrency" class="sel-currency" style="display:none;">
                            <c:forEach items="${curlist}" var="currency">
                                <option value="${currency.id}">${currency.currencyName}</option>
                            </c:forEach>
                        </select>



					<div class="kong"></div>
                              <!--占位方式开始-->
                        <div class="seach25 seach100">
                            <p class="fbold f14"><span class="xing">*</span>付款方式：</p>
                            <p class="seach_r add-paytype">
                                <input type="checkbox" class="ckb_mod error" id="payMode_deposit" name="payMode_deposit" value="1" onclick="paychg(this)"><font>订金占位</font>&#12288;&#12288;
                                <label for="spinner" class="txt2" id="label_deposit"><span class="xing" id="deposit_xing" style="display: none;">*</span>保留时限：</label>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration" class="spinner" name="remainDays_deposit" maxlength="3" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">天</span>
                                </span>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration_hour" class="spinner_hour" name="remainDays_deposit_hour" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">时</span> 
                                </span>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration_fen" class="spinner_fen" name="remainDays_deposit_fen" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">分</span>
                                </span>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_advance" name="payMode_advance" value="1" onclick="paychg(this)"><font>预占位</font>&#12288;&#12288;&#12288;
                                <label for="spinner" class="txt2" id="label_advance"><span class="xing" id="advance_xing" style="display: none;">*</span>保留时限：</label>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration" class="spinner" name="remainDays_advance" maxlength="3" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">天</span>
                                </span>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration_hour" class="spinner_hour" name="remainDays_advance_hour" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">时</span>
                                </span>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration_fen" class="spinner_fen" name="remainDays_advance_fen" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">分</span>
                                </span>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_full" name="payMode_full" value="1"><font>全款占位</font>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_op" name="payMode_op" value="1"><font>计调确认占位</font>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_cw" name="payMode_cw" value="1"><font>财务确认占位</font>
                            </p>
                            <div class="kongr"></div>
                        </div>
                        <!--占位方式结束-->
                        </div>
                        </form>
                        <!-- 多段 -->
                        <form action="${ctx}/airTicket/secondform" method="post"  id="frm1">
                            <input type="hidden" name="airType" value="1" />
                            <input type="hidden" id="moreSenum" name="sectionNum" value="2" />
                            <input type="hidden" name="intermodalInfo" value="" />
                            <input type="hidden" name="txt_ticketId" value="${airticket.id}" />
                      	<div class="inquiry_flights3">
                      		<div>
                        	<p class="main-right-topbutt"><a class="primary" href="javascript:void(0);" onclick="inquiryFlights3Add(this)">新增航段</a></p>
                            <div class="seach25">
                              	<p><span class="xing">*</span>出发城市：</p>
                              	<p class="seach_r"><select class="selectinput" id="departureCity" name="departureCity">      
                                    <c:forEach var="dic" items="${from_areaslist}">
                                            <option value="${dic.value}" <c:if test="${airticket.departureCity==dic.value}">selected="selected"</c:if>>${dic.label}</option>
                                        </c:forEach>
                                </select></p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达城市：</p>
                                <p class="seach_r"><select class="selectinput" name="arrivedCity">      
                                  <c:forEach var="area" items="${arrivedareas}">
                                            <option value="${area.id}" <c:if test="${airticket.arrivedCity==area.id}">selected="selected"</c:if>>${area.name}</option>
                                        </c:forEach>
                                </select></p>
                            </div>
                             <div class="seach25">
                                <p><span class="xing">*</span>预收人数 ：</p>
                                <p class="seach_r"><input type="text" id="reservationsNum" maxlength="10" name="reservationsNum" value=""></p>
                            </div> 
                            <div class="kong"></div>
                            <div class="addFlights3Div">
                                <div class="title_samil">第1段：
                                	<span class="seach_check"><label for="radio31-4"><input name="flightArea1" type="radio" value="4"> 国内</label></span>
                                	<span class="seach_check"><label for="radio31-2"><input name="flightArea1" type="radio" value="2"> 国际</label></span>
                                	<span class="seach_check"><label for="radio31-1"><input name="flightArea1" type="radio" value="1"> 内陆</label></span>                            
                                    <span class="seach_check"><label for="radio13-3"><input name="flightArea1" type="radio" value="3"> 国际+内陆</label></span>
                                </div>
                            <div class="seach25">
                              	<p><span class="xing">*</span>出发机场：</p>
                              	<p class="seach_r"><div class="input-append">
	                            <div class="input-append">
									<input id="mleaveAirport1Id" name="leaveAirport" type="hidden" value=""/>
									<input id="mleaveAirport1Name" name="leaveAirportName" readonly="readonly" type="text" value="" class="appendtext"/>
									<a id="mleaveAirport1" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div> 
</div></p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达机场：</p>
                                <p class="seach_r"><div class="input-append">
                                
                                <div class="input-append">
									<input id="mdesAirpost1Id" name="desAirpost" type="hidden" value=""/>
									<input id="mdesAirpost1Name" name="desAirpostName" readonly="readonly" type="text" value="" class="appendtext"/>
									<a id="mdesAirpost1" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div> 
</div></p>
                            </div>
                            <div class="kong"></div>
                                <div class="seach25">
                                    <p>出发时刻：</p>
                                    <p class="seach_r"><input type="text" class="dateinput"  name="startTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                                </div>
                                <div class="seach25">
                                    <p>到达时刻：</p>
                                    <p class="seach_r"><input type="text" class="dateinput" name="arrivalTime" value=""  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                                </div>
                                <div class="seach25">
                            	<p>航班号：</p>
                              	<p class="seach_r"><input type="text" name="flightNumber"  maxlength="20" value=""></p>
                            </div>
                                <div class="kong"></div>
                                <div class="seach25">
                                    <p>航空公司：</p>
                                    <p class="seach_r"> <select name="airlines" id="airlines" onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
                                    	<option value="">不限</option>
                                      <c:forEach var="airlines" items="${airlines_list}">
                                            <option value="${airlines.airlineCode}">${airlines.airlineName}</option>
                                        </c:forEach>
                                    </select></p>
                                </div>
                                <div class="seach25">
                                    <p>舱位等级：</p>
                                    <p class="seach_r"> <select name="spaceGrade" onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
                                      <option value="-1">不限</option>
                                    </select></p>
                                </div>
                                <div class="seach25">
                                  <p>舱位：</p>
                                  <p class="seach_r"><select name="airSpace">
                                  <option value="-1">不限</option>
                                  </select></p>
                                </div>
                                <div class="kong"></div>
                            </div>
                            <div class="addFlights3Div">
                                <div class="title_samil">第2段：
                                	<span class="seach_check"><label for="radio32-4"><input name="flightArea2" id="flightArea4" type="radio" value="4"> 国内</label></span>
                                    <span class="seach_check"><label for="radio32-2"><input name="flightArea2" id="radio32-2" type="radio" value="2"> 国际</label></span>
                                    <span class="seach_check"><label for="radio32-1"><input name="flightArea2" id="flightArea2" type="radio" value="1"> 内陆</label></span>                   
                                    <span class="seach_check"><label for="radio32-3"><input  name="flightArea2"id="radio13" type="radio" value="3"> 国际+内陆</label></span>
                                </div>
                            <div class="seach25">
                              	<p><span class="xing">*</span>出发机场：</p>
                              	<p class="seach_r"><div class="input-append">
                              	<div class="input-append">
									<input id="mleaveAirport2Id" name="leaveAirport" type="hidden" value=""/>
									<input id="mleaveAirport2Name" name="leaveAirportName" readonly="readonly" type="text" value="" class="appendtext"/>
									<a id="mleaveAirport2" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div> 
								</div></p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达机场：</p>
                                <p class="seach_r"><div class="input-append">
                                
                                <div class="input-append">
									<input id="mdesAirpost2Id" name="desAirpost" type="hidden" value=""/>
									<input id="mdesAirpost2Name" name="desAirpostName" readonly="readonly" type="text" value="" class="appendtext"/>
									<a id="mdesAirpost2" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div> 
                                 </div>
                            	</p>
                            </div>
                            <div class="kong"></div>
                                <div class="seach25">
                                    <p>出发时刻：</p>
                                    <p class="seach_r"><input type="text" class="dateinput" name="startTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                                </div>
                                <div class="seach25">
                                    <p>到达时刻：</p>
                                    <p class="seach_r"><input type="text" class="dateinput" name="arrivalTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                                </div>
                                <div class="seach25">
	                            	<p>航班号：</p>
	                              	<p class="seach_r"><input type="text" name="flightNumber"  maxlength="20" value=""></p>
	                            </div>
                                <div class="kong"></div>
                                <div class="seach25">
                                    <p>航空公司：</p>
                                    <p class="seach_r"> <select name="airlines" id="airlines" onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
                                    <option value="">不限</option>
                                    <c:forEach var="airlines" items="${airlines_list}">
                                            <option value="${airlines.airlineCode}">${airlines.airlineName}</option>
                                        </c:forEach>
                                    </select></p>
                                </div>
                                <div class="seach25">
                                    <p>舱位等级：</p>
                                    <p class="seach_r"> <select name="spaceGrade" onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
                                      <option value="-1">不限</option>
                                    
                                    </select></p>
                                </div>
                                <div class="seach25">
                                    <p>舱位：</p>
                                    <p class="seach_r"><select name="airSpace" id="">
                                    <option value="-1">不限</option>
                                    </select></p>
                                </div>
                                <div class="kong"></div>
                            </div>
                          
                            
                            </div>
                             <div class="kongr"></div>
                             
                             <!--出票日期开始-->
					<div class="seach25">
						<p class="fbold f14">出票日期：</p>
						<div><input type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="" name="outTicketTime" class="dateinput"></div>
					</div>
					<!--出票日期结束-->
					<!--离境口岸开始-->
                      <div class="seach25">
                           <p class="fbold f14"><span class="xing">*</span>离境口岸：</p>
                           <select id="outArea" name="outArea" class="" style="display: inline-block;" onchange="outAreachg()">
                           <option selected="selected" value="-1">请选择</option>
                                <c:forEach var="dic" items="${out_areaslist}">
								<option value="${dic.id}">${dic.label}</option>
								</c:forEach>
                           </select>
                      </div>
                        <!--离境口岸结束-->
                        <div class="kongr"></div>
                        <!--联运开始-->
                        <div class="mod_information_d2 lianyun">
                        	<div class="lianyun_select">
                                <label><span class="xing" style="display:none;">*</span>联运类型：</label>
                                <select id="intermodalType" name="intermodalType" onchange="transportchgAir(this)" class="" style="display: inline-block;">
                                    <option id="national" value="1">全国联运</option>
                                    <option id="group" value="2">分区联运</option>
                                    <option selected="selected" id="none" value="0">无联运</option>
                                </select>
                            </div>
                            <div id="nationalTrans" class="transport_city" style="display: none;">
                                <label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  </c:forEach>
                                </select>
                                <input type="text" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="8" name="intermodalAllPrice" id="intermodalAllPrice" /><span class="currency">元</span>
                            </div>
                            <div id="groupTrans" class="transport_city" style="display: none;">
                                <p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart" name="intermodalGroupPart" maxlength="50" type="text"><label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  	</c:forEach>
                                </select>&nbsp;<input id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="8" type="text" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" /><span class="currency">元</span><a class="ydbz_s transportAdd" onclick="javascript:transportAdd(this, 0);">增加</a></p>
                            </div>
                        </div>
                        <!--联运结束-->
                        <select id="templateCurrency" name="selectCurrency" class="sel-currency" style="display:none;">
                            <c:forEach items="${curlist}" var="currency">
                                <option value="${currency.id}">${currency.currencyName}</option>
                            </c:forEach>
                        </select>


					<div class="kong"></div>
                             <!--占位方式开始-->
                        <div class="seach25 seach100">
                            <p class="fbold f14"><span class="xing">*</span>付款方式：</p>
                            <p class="seach_r add-paytype">
                                <input type="checkbox" class="ckb_mod error" id="payMode_deposit" name="payMode_deposit" value="1" onclick="paychg(this)"><font>订金占位</font>&#12288;&#12288;
                                <label for="spinner" class="txt2" id="label_deposit"><span class="xing" id="deposit_xing" style="display: none;">*</span>保留时限：</label>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration" class="spinner" name="remainDays_deposit" maxlength="3" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">天</span>
                                </span>
                                <span id="payMode_deposit_span"> 
                                    <input id="activityDuration_hour" class="spinner_hour" name="remainDays_deposit_hour" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">时</span>
                                </span>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration_fen" class="spinner_fen" name="remainDays_deposit_fen" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">分</span>
                                </span>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_advance" name="payMode_advance" value="1" onclick="paychg(this)"><font>预占位</font>&#12288;&#12288;&#12288;
                                <label for="spinner" class="txt2" id="label_advance"><span class="xing" id="advance_xing" style="display: none;">*</span>保留时限：</label>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration" class="spinner" name="remainDays_advance" maxlength="3" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">天</span>
                                </span>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration_hour" class="spinner_hour" name="remainDays_advance_hour" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">时</span>
                                </span>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration_fen" class="spinner_fen" name="remainDays_advance_fen" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">分</span>
                                </span>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_full" name="payMode_full" value="1"><font>全款占位</font>
                                 <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_op" name="payMode_op" value="1"><font>计调确认占位</font>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_cw" name="payMode_cw" value="1"><font>财务确认占位</font>
                            </p>
                            <div class="kongr"></div>
                        </div>
                        <!--占位方式结束-->
                            </div>
                      	</form>
                      	
                      	<div class="addFlights3None addFlights3Div" style="display:none;" id="hiddenFilght">
                                <div class="title_samil">第<em>3</em>段：
                                	<span class="seach_check"><label><input type="radio" name="flightAreax" value="4" /> 国内</label></span>
                                	<span class="seach_check"><label><input type="radio" name="flightAreax" value="2"/> 国际</label></span>
                                	<span class="seach_check"><label><input type="radio" name="flightAreax" value="1" /> 内陆</label></span>
                                    <span class="seach_check"><label><input type="radio" name="flightAreax" value="3"/> 国际+内陆</label></span>
                                    <p class="main-right-topbutt"><a class="" href="javascript:void(0);" onclick="inquiryFlights3Del(this);"style="margin-right:40px;">删除路段</a></p>
                                </div>
                            <div class="seach25">
                              	<p><span class="xing">*</span>出发机场：</p>
                              	<p class="seach_r"><div class="input-append">
                              	<div class="input-append">
									<input id="mleaveAirportxId" name="leaveAirport" type="hidden" value=""/>
									<input id="mleaveAirportxName" name="leaveAirportName" readonly="readonly" type="text" value="" class="appendtext"/>
									<a id="mleaveAirportx" name="leaveHref" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div> 
								</div></p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达机场：</p>
                                <p class="seach_r"><div class="input-append">
                                
                                <div class="input-append">
									<input id="mdesAirpostxId" name="desAirpost" type="hidden" value=""/>
									<input id="mdesAirpostxName" name="desAirpostName" readonly="readonly" type="text" value="" class="appendtext"/>
									<a id="desAirpostx" name="desHref" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div> 
                                 </div>
                            	</p>
                            </div>
                            <div class="kong"></div>
                                <div class="seach25">
                                    <p>出发时刻：</p>
                                    <p class="seach_r"><input type="text" class="dateinput" name="startTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                                </div>
                                <div class="seach25">
                                    <p>到达时刻：</p>
                                    <p class="seach_r"><input type="text" class="dateinput" name="arrivalTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                                </div>
                                <div class="seach25">
	                            	<p>航班号：</p>
	                              	<p class="seach_r"><input type="text" name="flightNumber"  maxlength="20" value=""></p>
	                            </div>
                                <div class="kong"></div>
                                <div class="seach25">
                                    <p>航空公司：</p>
                                    <p class="seach_r"> <select name="airlines" id="airlines" onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
                                    <option value="">不限</option>
                                     <c:forEach var="airlines" items="${airlines_list}">
                                            <option value="${airlines.airlineCode}">${airlines.airlineName}</option>
                                        </c:forEach>
                                    </select></p>
                                </div>
                                <div class="seach25">
                                    <p>舱位等级：</p>
                                    <p class="seach_r"> <select name="spaceGrade"  onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
                                      <option value="-1">不限</option>
                                    
                                    </select></p>
                                </div>
                                <div class="seach25">
                                    <p>舱位：</p>
                                    <p class="seach_r"><select name="airSpace">
                                    <option value="-1">不限</option>
                                    </select></p>
                                </div>
                                <div class="kong"></div>
                            	</div>
                      	
                        </c:when>
                        
                        <c:when test="${airticket.airType eq '1'}">
                        <!-- 多段情况 -->
                        <!-- 单程 -->
                        <form action="${ctx}/airTicket/secondform" method="post" id="frm3">
                        <input type="hidden" name="airType" value="3" />
                        <input type="hidden" name="sectionNum" value="1" />
                        <input type="hidden" name="intermodalInfo" value="" />
                        <input type="hidden" name="txt_ticketId" value="${airticket.id}" />
                      	<div class="inquiry_flights2">
                        	<div class="seach25 seach100">
                        		<span class="seach_check"><label for="radio24"><input name="flightArea" id="radio24" type="radio" value="4" > 国内</label></span>
                        		<span class="seach_check"><label for="radio22"><input  name="flightArea"id="radio22" type="radio" value="2"> 国际</label></span>
                        		<span class="seach_check"><label for="radio21"><input name="flightArea" id="radio21" type="radio" value="1" > 内陆</label></span>   
                                <span class="seach_check"><label for="radio23"><input  name="flightArea"id="radio23" type="radio" value="3"> 国际+内陆</label></span>
                            </div>
                             <div class="seach25">
                              	<p><span class="xing">*</span>出发城市：</p>
                              	<p class="seach_r"><select class="selectinput" id="departureCity" name="departureCity">      
                                    <c:forEach var="dic" items="${from_areaslist}">
                                            <option value="${dic.value}" <c:if test="${airticket.departureCity==dic.value}">selected="selected"</c:if>>${dic.label}</option>
                                        </c:forEach>
                                </select></p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达城市：</p>
                                <p class="seach_r"><select class="selectinput" name="arrivedCity">      
                                   <c:forEach var="area" items="${arrivedareas}">
                                            <option value="${area.id}" <c:if test="${airticket.arrivedCity==area.id}">selected="selected"</c:if>>${area.name}</option>
                                        </c:forEach>
                                </select></p>
                            </div>
                             <div class="seach25">
                                <p><span class="xing">*</span>预收人数 ：</p>
                                <p class="seach_r"><input type="text" id="reservationsNum" maxlength="10" name="reservationsNum" value=""></p>
                            </div> 
                            <div class="kong"></div>
                            <div class="seach25">
                              	<p><span class="xing">*</span>出发机场：</p>
                              	<p class="seach_r"><div class="input-append">
									<div class="input-append">
										<input id="leaveAirportId" name="leaveAirport" type="hidden" value=""/>
										<input id="leaveAirportName" name="leaveAirportName" readonly="readonly" type="text" value="" class="appendtext"/>
										<a id="leaveAirport" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
									</div>                              	
							</div></p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达机场：</p>
                                <p class="seach_r"><div class="input-append">
                                	<div class="input-append">
										<input id="desAirpostId" name="desAirpost" type="hidden" value=""/>
										<input id="desAirpostName" name="desAirpostName" readonly="readonly" type="text" value="" class="appendtext"/>
										<a id="desAirpost" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
									</div> 
				                    
							</div></p>
                            </div>
                            <div class="kong"></div>
                            <div class="seach25">
                                <p>出发时刻：</p>
                                <p class="seach_r"><input type="text" class="dateinput"  name="startTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                            </div>
                            <div class="seach25">
                                <p>到达时刻：</p>
                                <p class="seach_r"><input type="text" class="dateinput"  name="arrivalTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                            </div>
                            <div class="seach25">
                            	<p>航班号：</p>
                              	<p class="seach_r"><input type="text" name="flightNumber"  maxlength="20"  value=""></p>
                            </div>
                      		<div class="kong"></div>
                            <div class="seach25">
                                <p>航空公司：</p>
                                <p class="seach_r"> <select name="airlines" id="airlines" onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
                                	<option value="">不限</option>
                                    <c:forEach var="airlines" items="${airlines_list}">
                                            <option value="${airlines.airlineCode}">${airlines.airlineName}</option>
                                        </c:forEach>
                                </select></p>
                            </div>
                            <div class="seach25">
                                <p>舱位等级：</p>
                                <p class="seach_r"> 
                                <select name="spaceGrade" onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
                                  <option value="-1">不限</option>
                                </select></p>
                            </div>
                            <div class="seach25">
                                <p>舱位：</p>
                                <p class="seach_r"><select name="airSpace">
                                 <option value="-1">不限</option>
                                </select></p>
                            </div>
                            <div class="kong"></div>
                            
                             <div class="kongr"></div>
                             
                             <!--出票日期开始-->
					<div class="seach25">
						<p class="fbold f14">出票日期：</p>
						<div><input type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="" name="outTicketTime" class="dateinput"></div>
					</div>
					<!--离境口岸开始-->
                      <div class="seach25">
                           <p class="fbold f14"><span class="xing">*</span>离境口岸：</p>
                           <select id="outArea" name="outArea" class="" style="display: inline-block;" onchange="outAreachg()">
                           <option selected="selected" value="-1">请选择</option>
                                <c:forEach var="dic" items="${out_areaslist}">
								<option value="${dic.id}">${dic.label}</option>
								</c:forEach>
                           </select>
                      </div>
                        <!--离境口岸结束-->
                        <div class="kongr"></div>
                        <!--联运开始-->
                        <div class="mod_information_d2 lianyun">
                        	<div class="lianyun_select">
                                <label><span class="xing" style="display:none;">*</span>联运类型：</label>
                                <select id="intermodalType" name="intermodalType" onchange="transportchgAir(this)" class="" style="display: inline-block;">
                                    <option id="national" value="1">全国联运</option>
                                    <option id="group" value="2">分区联运</option>
                                    <option selected="selected" id="none" value="0">无联运</option>
                                </select>
                            </div>
                            <div id="nationalTrans" class="transport_city" style="display: none;">
                                <label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  </c:forEach>
                                </select>
                                <input type="text" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="8" name="intermodalAllPrice" id="intermodalAllPrice" /><span class="currency">元</span>
                            </div>
                            <div id="groupTrans" class="transport_city" style="display: none;">
                                <p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart" name="intermodalGroupPart" maxlength="50" type="text"><label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  	</c:forEach>
                                </select>&nbsp;<input id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="8" type="text" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" /><span class="currency">元</span><a class="ydbz_s transportAdd" onclick="javascript:transportAdd(this, 0);">增加</a></p>
                            </div>
                        </div>
                        <!--联运结束-->
                        <select id="templateCurrency" name="selectCurrency" class="sel-currency" style="display:none;">
                            <c:forEach items="${curlist}" var="currency">
                                <option value="${currency.id}">${currency.currencyName}</option>
                            </c:forEach>
                        </select>


					<!--出票日期结束-->
					<div class="kong"></div>
                             <!--占位方式开始-->
                        <div class="seach25 seach100">
                            <p class="fbold f14"><span class="xing">*</span>付款方式：</p>
                            <p class="seach_r add-paytype">
                                <input type="checkbox" class="ckb_mod error" id="payMode_deposit" name="payMode_deposit" value="1" onclick="paychg(this)"><font>订金占位</font>&#12288;&#12288;
                                <label for="spinner" class="txt2" id="label_deposit"><span class="xing" id="deposit_xing" style="display: none;">*</span>保留时限：</label>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration" class="spinner" name="remainDays_deposit" maxlength="3" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">天</span>
                                </span>
                                <span id="payMode_deposit_span"> 
                                    <input id="activityDuration_hour" class="spinner_hour" name="remainDays_deposit_hour" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">时</span>
                                </span>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration_fen" class="spinner_fen" name="remainDays_deposit_fen" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">分</span>
                                </span>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_advance" name="payMode_advance" value="1" onclick="paychg(this)"><font>预占位</font>&#12288;&#12288;&#12288;
                                <label for="spinner" class="txt2" id="label_advance"><span class="xing" id="advance_xing" style="display: none;">*</span>保留时限：</label>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration" class="spinner" name="remainDays_advance" maxlength="3" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">天</span>
                                </span>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration_hour" class="spinner_hour" name="remainDays_advance_hour" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">时</span>
                                </span>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration_fen" class="spinner_fen" name="remainDays_advance_fen" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">分</span>
                                </span>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_full" name="payMode_full" value="1"><font>全款占位</font>
                                 <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_op" name="payMode_op" value="1"><font>计调确认占位</font>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_cw" name="payMode_cw" value="1"><font>财务确认占位</font>
                            </p>
                            <div class="kongr"></div>
                        </div>
                        <!--占位方式结束-->
                        </div>
                        </form>
                        <!-- 往返 -->
                   		<form action="${ctx}/airTicket/secondform" method="post" id="frm2">
                                <input type="hidden" name="airType" value="2" />
                                <input type="hidden" name="sectionNum" value="2" />
                                <input type="hidden" name="intermodalInfo" value="" />
                                <input type="hidden" name="txt_ticketId" value="${airticket.id}" />
                        	<div class="inquiry_flights1">
                        	<div class="seach25 seach100">
                        		<span class="seach_check"><label for="radio14"><input name="flightArea" id="radio14" type="radio" value="4"> 国内</label></span>
                        		<span class="seach_check"><label for="radio12"><input  name="flightArea" id=" radio12" type="radio" value="2"> 国际</label></span>
                        		<span class="seach_check"><label for="radio11"><input name="flightArea" id="radio11" type="radio" value="1"> 内陆</label></span>
                                <span class="seach_check"><label for="radio13"><input  name="flightArea" id=" radio13" type="radio" value="3"> 国际+内陆</label></span>
                        	</div>

                         	<div class="seach25">
                              	<p><span class="xing">*</span>出发城市：</p>
                              	<p class="seach_r"><select class="selectinput" id="departureCity" name="departureCity">      
                                    <c:forEach var="dic" items="${from_areaslist}">
                                            <option value="${dic.value}" <c:if test="${airticket.departureCity==dic.value}">selected="selected"</c:if>>${dic.label}</option>
                                        </c:forEach>
                                </select></p>
                            </div>  
                            <div class="seach25">
                                <p><span class="xing">*</span>到达城市：</p>
                                <p class="seach_r"><select class="selectinput" id="arrivedCity" name="arrivedCity">      
                                   <c:forEach var="area" items="${arrivedareas}">
                                            <option value="${area.id}" <c:if test="${airticket.arrivedCity==area.id}">selected="selected"</c:if>>${area.name}</option>
                                        </c:forEach>
                                </select></p>
                            </div> 
                             <div class="seach25">
                                <p><span class="xing">*</span>预收人数 ：</p>
                                <p class="seach_r"><input type="text" id="reservationsNum" name="reservationsNum" value=""  maxlength="10" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></p>
                            </div> 
                      		<div class="title_samil">去程：</div>
                            <div class="kong"></div>
                            <div class="seach25">
                              	<p><span class="xing">*</span>出发机场：</p>
                              	<p class="seach_r">
	                              	<div class="input-append">
		 								<div class="input-append">
											<input id="leaveAirport1Id" name="leaveAirport" type="hidden" value=""/>
											<input id="leaveAirport1Name" name="leaveAirportName" readonly="readonly" type="text" value="" class="appendtext"/>
											<a id="leaveAirport1" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
										</div> 
									</div>
								</p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达机场：</p>
                                <p class="seach_r">
                                <div class="input-append">
                                	<div class="input-append">
										<input id="desAirpost1Id" name="desAirpost" type="hidden" value=""/>
										<input id="desAirpost1Name" name="targetAreaNameList" readonly="readonly" type="text" value="" class="appendtext"/>
										<a id="desAirpost1" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
									</div> 
                     			</div>
                     			</p> 
                            </div>
                            <div class="kong"></div>
                      		<div class="seach25">
                              	<p>出发时刻：</p>
                              	<p class="seach_r"><input type="text" class="dateinput" name="startTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"  ></p>
                            </div>
                            <div class="seach25">
                            	<p>到达时刻：</p>
                              	<p class="seach_r"><input type="text" class="dateinput" name="arrivalTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                            </div>
                            <div class="seach25">
                            	<p>航班号：</p>
                              	<p class="seach_r"><input type="text" name="flightNumber"  maxlength="20" value=""></p>
                            </div>
                            
                            <div class="kong"></div>
                            <div class="seach25">
                        		<p>航空公司：</p>
                       			<p class="seach_r"> <select name="airlines" id="airlines"  onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
                       			<option value="">不限</option>
                                   <c:forEach var="airlines" items="${airlines_list}">
                                            <option value="${airlines.airlineCode}" >${airlines.airlineName}</option>
                                        </c:forEach>
                                </select></p>
                      		</div>
                      		<div class="seach25">
                        		<p>舱位等级：</p>
                       			<p class="seach_r"> <select name="spaceGrade" onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
                                  <option value="-1">不限</option>
                                </select></p>
                      		</div>
                          	<div class="seach25">
                            	<p>舱位：</p>
                            	<p class="seach_r"><select name="airSpace" id="">
                            	<option value="-1">不限</option>
                                </select></p>
                            </div>
                            <div class="kong"></div>
                            
                      		<div class="title_samil">返程：</div>
                            <div class="seach25">
                              	<p><span class="xing">*</span>出发机场：</p>
                              	<p class="seach_r"><div class="input-append">
	                 			<div class="input-append">
									<input id="leaveAirport2Id" name="leaveAirport" type="hidden" value=""/>
									<input id="leaveAirport2Name" name="leaveAirportName" readonly="readonly" type="text" value="" class="appendtext"/>
									<a id="leaveAirport2" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div> 
                     
                     		</div></p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达机场：</p>
                                <p class="seach_r"><div class="input-append">
	                 				<div class="input-append">
										<input id="desAirpost2Id" name="desAirpost" type="hidden" value=""/>
										<input id="desAirpost2Name" name="targetAreaNameList" readonly="readonly" type="text" value="" class="appendtext"/>
										<a id="desAirpost2" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
									</div> 
                     		</div></p>
                            </div>
                            <div class="kong"></div>
                            <div class="seach25">
                                <p>出发时刻：</p>
                                <p class="seach_r"><input type="text" class="dateinput"  name="startTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                            </div>
                            <div class="seach25">
                                <p>到达时刻：</p>
                                <p class="seach_r"><input type="text" class="dateinput" name="arrivalTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                            </div>
                            <div class="seach25">
                            	<p>航班号：</p>
                              	<p class="seach_r"><input type="text" name="flightNumber"  maxlength="20"  value=""></p>
                            </div>
                            <div class="kong"></div>
                            <div class="seach25">
                              	<p>航空公司：</p>
                             	<p class="seach_r"> <select name="airlines" id="airlines" onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
                             	<option value="">不限</option>
                                  <c:forEach var="airlines" items="${airlines_list}">
                                            <option value="${airlines.airlineCode}">${airlines.airlineName}</option>
                                        </c:forEach>
                                </select></p>
                            </div>
                            <div class="seach25">
                                <p>舱位等级：</p>
                                <p class="seach_r"> <select name="spaceGrade" onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
                                  <option value="-1">不限</option>
                                </select></p>
                            </div>
                      		<div class="seach25">
                        		<p>舱位：</p>
                        		<p class="seach_r"><select name="airSpace" id="">
                        		<option value="-1">不限</option>
                                </select></p>
                      		</div>
                            <div class="kong"></div>
                             <div class="kongr"></div>
                             
                             <!--出票日期开始-->
					<div class="seach25">
						<p class="fbold f14">出票日期：</p>
						<div><input type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="" name="outTicketTime" class="dateinput"></div>
					</div>
					<!--出票日期结束-->
					<!--离境口岸开始-->
                      <div class="seach25">
                           <p class="fbold f14"><span class="xing">*</span>离境口岸：</p>
                           <select id="outArea" name="outArea" class="" style="display: inline-block;" onchange="outAreachg()">
                           <option selected="selected" value="-1">请选择</option>
                                <c:forEach var="dic" items="${out_areaslist}">
								<option value="${dic.id}">${dic.label}</option>
								</c:forEach>
                           </select>
                      </div>
                        <!--离境口岸结束-->
                        <div class="kongr"></div>
                        <!--联运开始-->
                        <div class="mod_information_d2 lianyun">
                        	<div class="lianyun_select">
                                <label><span class="xing" style="display:none;">*</span>联运类型：</label>
                                <select id="intermodalType" name="intermodalType" onchange="transportchgAir(this)" class="" style="display: inline-block;">
                                    <option id="national" value="1">全国联运</option>
                                    <option id="group" value="2">分区联运</option>
                                    <option selected="selected" id="none" value="0">无联运</option>
                                </select>
                            </div>
                            <div id="nationalTrans" class="transport_city" style="display: none;">
                                <label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option addclass="${currency.currencyStyle}" value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  </c:forEach>
                                </select>
                                <input type="text" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="8" name="intermodalAllPrice" id="intermodalAllPrice" /><span class="currency">元</span>
                            </div>
                            <div id="groupTrans" class="transport_city" style="display: none;">
                                <p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart" name="intermodalGroupPart" maxlength="50" type="text"><label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  	</c:forEach>
                                </select>&nbsp;<input id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="8" type="text" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" /><span class="currency">元</span><a class="ydbz_s transportAdd" onclick="javascript:transportAdd(this, 0);">增加</a></p>
                            </div>
                        </div>
                        <!--联运结束-->
                        <select id="templateCurrency" name="selectCurrency" class="sel-currency" style="display:none;">
                            <c:forEach items="${curlist}" var="currency">
                                <option value="${currency.id}">${currency.currencyName}</option>
                            </c:forEach>
                        </select>


					<div class="kong"></div>
							 <!--占位方式开始-->
                        <div class="seach25 seach100">
                            <p class="fbold f14"><span class="xing">*</span>付款方式：</p>
                            <p class="seach_r add-paytype">
                                <input type="checkbox" class="ckb_mod error" id="payMode_deposit" name="payMode_deposit" value="1" onclick="paychg(this)"><font>订金占位</font>&#12288;&#12288;
                                <label for="spinner" class="txt2" id="label_deposit"><span class="xing" id="deposit_xing" style="display: none;">*</span>保留时限：</label>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration" class="spinner" name="remainDays_deposit" maxlength="3" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">天</span>
                                </span>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration_hour" class="spinner_hour" name="remainDays_deposit_hour" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">时</span> 
                                </span>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration_fen" class="spinner_fen" name="remainDays_deposit_fen" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">分</span>
                                </span>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_advance" name="payMode_advance" value="1" onclick="paychg(this)"><font>预占位</font>&#12288;&#12288;&#12288;
                                <label for="spinner" class="txt2" id="label_advance"><span class="xing" id="advance_xing" style="display: none;">*</span>保留时限：</label>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration" class="spinner" name="remainDays_advance" maxlength="3" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">天</span>
                                </span>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration_hour" class="spinner_hour" name="remainDays_advance_hour" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">时</span>
                                </span>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration_fen" class="spinner_fen" name="remainDays_advance_fen" maxlength="2" value="" disabled="disabled" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">分</span>
                                </span>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_full" name="payMode_full" value="1"><font>全款占位</font>
                                 <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_op" name="payMode_op" value="1"><font>计调确认占位</font>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_cw" name="payMode_cw" value="1"><font>财务确认占位</font>
                            </p>
                            <div class="kongr"></div>
                        </div>
                        <!--占位方式结束-->
                        </div>
                        </form>
                        <!-- 多段 -->
                        <form action="${ctx}/airTicket/secondform" method="post"  id="frm1">
                                <input type="hidden" name="airType" value="1" />
                                <input type="hidden" id="moreSenum" name="sectionNum" value="${airticket.flightInfos.size()}" />
                                <input type="hidden" name="intermodalInfo" value="" />
                        		<input type="hidden" name="txt_ticketId" value="${airticket.id}" />
                      	<div class="inquiry_flights3">
                      	 <div>
                        	<p class="main-right-topbutt"><a class="primary" href="javascript:void(0);" onclick="inquiryFlights3Add(this)">新增航段</a></p>
                            <div class="seach25">
                             	<p><span class="xing">*</span>出发城市：</p>
                             	<p class="seach_r"><select class="selectinput" id="departureCity" name="departureCity">      
                                   <c:forEach var="dic" items="${from_areaslist}">
                                           <option value="${dic.value}" <c:if test="${airticket.departureCity==dic.value}">selected="selected"</c:if>>${dic.label}</option>
                                       </c:forEach>
                               </select></p>
                            </div>  
                            <div class="seach25">
                                <p><span class="xing">*</span>到达城市：</p>
                                <p class="seach_r"><select class="selectinput" id="arrivedCity" name="arrivedCity">      
                                   <c:forEach var="area" items="${arrivedareas}">
                                            <option value="${area.id}" <c:if test="${airticket.arrivedCity== area.id}">selected="selected"</c:if>>${area.name}</option>
                                        </c:forEach>
                                </select></p>
                            </div> 
                             <div class="seach25">
                                <p><span class="xing">*</span>预收人数 ：</p>
                                <p class="seach_r"><input type="text" id="reservationsNum" name="reservationsNum"  maxlength="10" value="${airticket.reservationsNum}"  onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></p>
                            </div> 
                            <div class="kong"></div>
                            
                            <c:forEach items="${airticket.flightInfos}" var="list" varStatus="listIndex">
                            <div class="addFlights3Div">
                               <div class="title_samil">第${list.number}段：
                                <span class="seach_check"><label for="radio31-4"><input name="flightArea${list.number}" id="radio14" type="radio" value="4" <c:if test="${list.ticket_area_type eq '4'}">checked="checked"</c:if> > 国内</label></span>
                                <span class="seach_check"><label for="radio31-2"><input name="flightArea${list.number}" id="radio12" type="radio" value="2" <c:if test="${list.ticket_area_type eq '2'}">checked="checked"</c:if>> 国际</label></span>
                                <span class="seach_check"><label for="radio31-1"><input name="flightArea${list.number}" id="radio11" type="radio" value="1" <c:if test="${list.ticket_area_type eq '1'}">checked="checked"</c:if> > 内陆</label></span>
                                <span class="seach_check"><label for="radio13-3"><input name="flightArea${list.number}" id="radio13" type="radio" value="3" <c:if test="${list.ticket_area_type eq '3'}">checked="checked"</c:if>> 国际+内陆</label></span>
                               </div>
                            <div class="seach25">
                              	<p><span class="xing">*</span>出发机场：</p>
                              	<p class="seach_r">
	                              	<div class="input-append">
		 								<input id="mleaveAirport${list.number}Id" name="leaveAirport" type="hidden" value="air${list.leaveAirport}"/>
										<input id="mleaveAirport${list.number}Name" name="leaveAirportName" readonly="readonly" type="text" value="${list.paraMap.leaveAirport}" class="appendtext"/>
										<a id="mleaveAirport${list.number}" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
									</div>
								</p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达机场：</p>
                                <p class="seach_r">
                                <div class="input-append">
                                	<input id="mdesAirpost${list.number}Id" name="desAirpost" type="hidden" value="air${list.destinationAirpost}"/>
									<input id="mdesAirpost${list.number}Name" name="desAirpostName" readonly="readonly" type="text" value="${list.paraMap.destinationAirpost}" class="appendtext"/>
									<a id="mdesAirpost${list.number}" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
                                
                                	
                     			</div>
                     			</p> 
                            </div>
                            <div class="kong"></div>
                      		<div class="seach25">
                              	<p>出发时刻：</p>
                              	<p class="seach_r"><input type="text" class="dateinput" name="startTime" value="<fmt:formatDate value="${list.startTime}" pattern="yyyy-MM-dd HH:mm"/>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"  ></p>
                            </div>
                            <div class="seach25">
                            	<p>到达时刻：</p>
                              	<p class="seach_r"><input type="text" class="dateinput" name="arrivalTime" value="<fmt:formatDate value="${list.arrivalTime}" pattern="yyyy-MM-dd HH:mm"/>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                            </div>
                            <div class="seach25">
                            	<p>航班号：</p>
                              	<p class="seach_r"><input type="text" name="flightNumber"  maxlength="20" value="${list.flightNumber}"></p>
                            </div>
                                <div class="kong"></div>
                                <div class="seach25">
                                    <p>航空公司：</p>
                                    <p class="seach_r"> <select name="airlines" id="airlines" onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
                                    	<option value="">不限</option>
                                      <c:forEach var="airlines" items="${airlines_list}">
                                            <option value="${airlines.airlineCode}" <c:if test="${list.airlines==airlines.airlineCode}">selected="selected"</c:if>>${airlines.airlineName}</option>
                                        </c:forEach>
                                    </select></p>
                                </div>
                                <div class="seach25">
                                    <p>舱位等级：</p>
                                    <p class="seach_r"> <select name="spaceGrade" onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
                                      <option value="-1">不限</option>
                                       <c:forEach var="obj" items="${list.paraMap.airLevel}">
                                       <option value="${obj.levelCode}" <c:if test="${list.spaceGrade eq obj.levelCode}">selected="selected"</c:if>>${obj.levelName}</option>
                                   </c:forEach>
                                    </select></p>
                                </div>
                                <div class="seach25">
                                  <p>舱位：</p>
                                  <p class="seach_r"><select name="airSpace">
                                  <option value="-1">不限</option>
                                   <c:forEach var="obj" items="${list.paraMap.airSpace}">
                                       <option value="${obj.spaceCode}" <c:if test="${list.airspace eq obj.spaceCode}">selected="selected"</c:if>>${obj.spaceName}</option>
                                   </c:forEach>
                                  </select></p>
                                </div>
                                <div class="kong"></div>
                            </div>
                            </c:forEach>
                            
                            
                            </div>
	                            
	                        <div class="kongr"></div>
	                        
	                        <!--出票日期开始-->
					<div class="seach25">
						<p class="fbold f14">出票日期：</p>
						<div><input type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="<fmt:formatDate value="${airticket.outTicketTime}" pattern="yyyy-MM-dd"/>" name="outTicketTime" class="dateinput"></div>
					</div>
					<!--出票日期结束-->
					<!--离境口岸开始-->
                      <div class="seach25">
                           <p class="fbold f14"><span class="xing">*</span>离境口岸：</p>
                           <select id="outArea" name="outArea" class="" style="display: inline-block;" onchange="outAreachg()">
                           <option selected="selected" value="-1">请选择</option>
                                <c:forEach var="dic" items="${out_areaslist}">
								<option value="${dic.id}" <c:if test="${airticket.outArea eq dic.id}">selected="selected"</c:if>>${dic.label}</option>
								</c:forEach>
                           </select>
                      </div>
                        <!--离境口岸结束-->
                        <div class="kongr"></div>
                        <!--联运开始-->
                        <div class="mod_information_d2 lianyun">
                        	<div class="lianyun_select">
                                <label><span class="xing" style="display:none;">*</span>联运类型：</label>
                                <select id="intermodalType" name="intermodalType" onchange="transportchgAir(this)" class="" style="display: inline-block;">
                                    <option id="national" value="1" <c:if test="${airticket.intermodalType==1}">selected="selected"</c:if>>全国联运</option>
                                    <option id="group" value="2" <c:if test="${airticket.intermodalType==2}">selected="selected"</c:if>>分区联运</option>
                                    <option id="none" value="0" <c:if test="${airticket.intermodalType==0}">selected="selected"</c:if>>无联运</option>
                                </select>
                            </div>
                            <div id="nationalTrans" class="transport_city" style="display: <c:if test="${airticket.intermodalType!=1}">none</c:if>;">
                                <label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  </c:forEach>
                                </select>
                                <input type="text" value="<fmt:formatNumber value='${airticket.intermodalStrategies.size()>0?airticket.intermodalStrategies.get(0).price:null}' pattern='#.##' />" onafterpaste="isMoney(this)" onkeyup="isMoney(this)" maxlength="8" name="intermodalAllPrice" id="intermodalAllPrice" /><span class="currency">元</span>
                            </div>
                            <div id="groupTrans" class="transport_city" style="display: <c:if test="${airticket.intermodalType!=2}">none</c:if>;">
                                <c:if test="${airticket.intermodalStrategies.size()==0}">
                                <p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart" name="intermodalGroupPart" maxlength="50" type="text"><label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  	</c:forEach>
                                </select>&nbsp;<input id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="8" type="text" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" /><span class="currency">元</span><a class="ydbz_s transportAdd" onclick="javascript:transportAdd(this, 0);">增加</a></p>
                                </c:if>
                                
                                <c:forEach items="${airticket.intermodalStrategies}" var="intermodalInfo" varStatus="status">
                                <p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart" name="intermodalGroupPart" value="${intermodalInfo.groupPart}" maxlength="50" type="text"><label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${intermodalInfo.priceCurrency.id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  	</c:forEach>
                                </select>&nbsp;<input id="intermodalGroupPrice" name="intermodalGroupPrice" value="${intermodalInfo.price}" maxlength="8" type="text" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" /><span class="currency">元</span>
                                <c:if test="${status.index==0}">
                                <a class="ydbz_s transportAdd" onclick="javascript:transportAdd(this, 0);">增加</a>
                                </c:if>
                                <c:if test="${status.index!=0}">
                                <a class="ydbz_s gray transportDel">删除</a>
                                </c:if>
                                </p>
                                </c:forEach>
                            </div>
                        </div>
                        <!--联运结束-->
                        <select id="templateCurrency" name="selectCurrency" class="sel-currency" style="display:none;">
                            <c:forEach items="${curlist}" var="currency">
                                <option value="${currency.id}">${currency.currencyName}</option>
                            </c:forEach>
                        </select>

					<div class="kong"></div>
	                          <!--占位方式开始-->
                        <div class="seach25 seach100">
                            <p class="fbold f14"><span class="xing">*</span>付款方式：</p>
                            <p class="seach_r add-paytype">
                                <input type="checkbox" class="ckb_mod error" id="payMode_deposit" name="payMode_deposit" value="1" onclick="paychg(this)" <c:if test="${airticket.payMode_deposit==1}">checked="checked"</c:if>   ><font>订金占位</font>&#12288;&#12288;
                                <label for="spinner" class="txt2" id="label_deposit"><span class="xing" id="deposit_xing" style="display: none;">*</span>保留时限：</label>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration" class="spinner" name="remainDays_deposit" maxlength="3" <c:if test="${airticket.payMode_deposit!=1}">value="" disabled="disabled"</c:if><c:if test="${airticket.payMode_deposit==1}">value="${airticket.remainDays_deposit}"</c:if> onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">天</span>
                                </span>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration_hour" class="spinner_hour" name="remainDays_deposit_hour" maxlength="2" <c:if test="${airticket.payMode_deposit!=1}">value="" disabled="disabled"</c:if><c:if test="${airticket.payMode_deposit==1}">value="${airticket.remainDays_deposit_hour}"</c:if> onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">时</span> 
                                </span>
                                <span id="payMode_deposit_span">
                                    <input id="activityDuration_fen" class="spinner_fen" name="remainDays_deposit_fen" maxlength="2" <c:if test="${airticket.payMode_deposit!=1}">value="" disabled="disabled"</c:if><c:if test="${airticket.payMode_deposit==1}">value="${airticket.remainDays_deposit_fen}"</c:if> onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">分</span>
                                </span>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_advance" name="payMode_advance" value="1" onclick="paychg(this)" <c:if test="${airticket.payMode_advance==1}">checked="checked"</c:if>><font>预占位</font>&#12288;&#12288;&#12288;
                                <label for="spinner" class="txt2" id="label_advance"><span class="xing" id="advance_xing" style="display: none;">*</span>保留时限：</label>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration" class="spinner" name="remainDays_advance" maxlength="3" <c:if test='${airticket.payMode_advance!=1}'>value="" disabled="disabled"</c:if><c:if test='${airticket.payMode_advance==1}'>value="${airticket.remainDays_advance}"</c:if> onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">天</span>
                                </span>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration_hour" class="spinner_hour" name="remainDays_advance_hour" maxlength="2" <c:if test='${airticket.payMode_advance!=1}'>value="" disabled="disabled"</c:if><c:if test='${airticket.payMode_advance==1}'>value="${airticket.remainDays_advance_hour}"</c:if> onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">时</span>
                                </span>
                                <span id="payMode_advance_span">
                                    <input id="activityDuration_fen" class="spinner_fen" name="remainDays_advance_fen" maxlength="2" <c:if test='${airticket.payMode_advance!=1}'>value="" disabled="disabled"</c:if><c:if test='${airticket.payMode_advance==1}'>value="${airticket.remainDays_advance_fen}"</c:if> onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" />
                                    <span style="padding-left:5px;">分</span>
                                </span>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_full" name="payMode_full" value="1" <c:if test="${airticket.payMode_full==1}">checked="checked"</c:if>><font>全款占位</font>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_op" name="payMode_op" value="1" <c:if test="${airticket.payMode_op==1}">checked="checked"</c:if>><font>计调确认占位</font>
                                <br />
                                <input type="checkbox" class="ckb_mod valid" id="payMode_cw" name="payMode_cw" value="1" <c:if test="${airticket.payMode_cw==1}">checked="checked"</c:if>><font>财务确认占位</font>
                            </p>
                            <div class="kongr"></div>
                        </div>
                        <!--占位方式结束-->
                            </div>
                      	</form>
                      	
                      	<div class="addFlights3None addFlights3Div" style="display:none;" id="hiddenFilght">
                            <div class="title_samil">第<em>3</em>段：
                            	<span class="seach_check"><label><input type="radio" name="flightAreax" value="4" /> 国内</label></span>
                            	<span class="seach_check"><label><input type="radio" name="flightAreax" value="2"/> 国际</label></span>
                            	<span class="seach_check"><label><input type="radio" name="flightAreax" value="1" /> 内陆</label></span>                            
                                <span class="seach_check"><label><input type="radio" name="flightAreax" value="3"/> 国际+内陆</label></span>
                                <p class="main-right-topbutt"><a class="" href="javascript:void(0);" onclick="inquiryFlights3Del(this);" style="margin-right:40px;">删除路段</a></p>
                            </div>
                            <div class="seach25">
                              	<p><span class="xing">*</span>出发机场：</p>
                              	<p class="seach_r"><div class="input-append">
                              	<div class="input-append">
									<input id="mleaveAirportxId" name="leaveAirport" type="hidden" value=""/>
									<input id="mleaveAirportxName" name="leaveAirportName" readonly="readonly" type="text" value="" class="appendtext"/>
									<a id="mleaveAirportx" name="leaveHref" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div> 
								</div></p>
                            </div>
                            <div class="seach25">
                                <p><span class="xing">*</span>到达机场：</p>
                                <p class="seach_r"><div class="input-append">
                                
                                <div class="input-append">
									<input id="mdesAirpostxId" name="desAirpost" type="hidden" value=""/>
									<input id="mdesAirpostxName" name="desAirpostName" readonly="readonly" type="text" value="" class="appendtext"/>
									<a id="desAirpostx" name="desHref" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div> 
                                 </div>
                            	</p>
                            </div>
                            <div class="kong"></div>
                                <div class="seach25">
                                    <p>出发时刻：</p>
                                    <p class="seach_r"><input type="text" class="dateinput" name="startTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                                </div>
                                <div class="seach25">
                                    <p>到达时刻：</p>
                                    <p class="seach_r"><input type="text" class="dateinput" name="arrivalTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" ></p>
                                </div>
                                <div class="seach25">
	                            	<p>航班号：</p>
	                              	<p class="seach_r"><input type="text"  maxlength="20" name="flightNumber" value=""></p>
	                            </div>
                                <div class="kong"></div>
                                <div class="seach25">
                                    <p>航空公司：</p>
                                    <p class="seach_r"> <select name="airlines" id="airlines" onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
                                    <option value="">不限</option>
                                     <c:forEach var="airlines" items="${airlines_list}">
                                            <option value="${airlines.airlineCode}">${airlines.airlineName}</option>
                                        </c:forEach>
                                    </select></p>
                                </div>
                                <div class="seach25">
                                    <p>舱位等级：</p>
                                    <p class="seach_r"> <select name="spaceGrade"  onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
                                      <option value="-1">不限</option>
                                    
                                    </select></p>
                                </div>
                                <div class="seach25">
                                    <p>舱位：</p>
                                    <p class="seach_r"><select name="airSpace">
                                    <option value="-1">不限</option>
                                    </select></p>
                                </div>
                                <div class="kong"></div>
                            	</div>
                        </c:when>
                        
                        </c:choose>
                      	<div class="kong"></div>
                        <div class="seach_btnbox"><input type="button" value="下一步" onclick="validateDate()" class="btn btn-primary"  /></div>
					</div>
			
                </div>
				
				<!--右侧内容部分结束-->
</body>
</html>