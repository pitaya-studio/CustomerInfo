
<%@ page contentType="text/html;charset=utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="wholesaler" />
<title>产品-机票产品及发布-基础信息</title>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/modules/forTTS/airticket/airticetForm.js"></script>
<script type="text/javascript">
    var root = "${ctx}";
   
    $(function(){

	if($('#airtypeHidden').val()!=''){
	    var ids = '#inquiry_radio_flights' + $('#airtypeHidden').val();
	//	$('#inquiry_radio_flights2').attr('checked',"checked");
	    $(ids).attr('checked',"checked");
	}
    
	inquiry_radio_flights();
	selectairline();//机场联动
	transportSelect();
	clearValue();
	//可输入select
	
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
		
		/*开启[保留天数]输入框*/
		//$("input[name^='remainDays']").prop("disabled",false);
		
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
//判断，如果支付方式没有被选中，则清空等待时间
function clearValue(){
	$("input[name='payMode']").each(function(){
		if($(this).prop("checked")){
			$(this).next().next().find("span.xing").show();
		}else{
			$(this).next().next().next().find("input[name^='remainDays']").val("");
			/*注释不可写样式*/
        	$(this).next().next().next().find("input[name^='remainDays']").attr("disabled","disabled");
        	$(this).next().next().find("span.xing").hide();
		}
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
				<span class="seach_check"><label for="inquiry_radio_flights2"><input name="airType_radio" id="inquiry_radio_flights2" type="radio"
						value="3" onclick="inquiry_radio_flights()"> 单程</label> </span> 
				<span class="seach_check"><label for="inquiry_radio_flights1"><input name="airType_radio" id="inquiry_radio_flights1" type="radio"
						value="2" checked="checked" onclick="inquiry_radio_flights()">往返</label> </span> 
				<span class="seach_check"><label for="inquiry_radio_flights3">
						<input name="airType_radio" id="inquiry_radio_flights3" type="radio" value="1"
						onclick="inquiry_radio_flights()"> 多段</label> </span> 
						
				<span class="pl25"><a target="_blank" href="${ctx}/eprice/manager/project/erecordtrafficlist">询价记录</a> </span>
			</div>

			<input type="hidden" value="${fns:getUser().company.uuid }" id="companyUUID"/>
			<!-- 新建页面时 -->
			<!-- 单程 -->
			<form action="${ctx}/airTicket/secondform" method="post" id="frm3">
				<input type="hidden" name="airType" value="3" /> 
				<input type="hidden" name="sectionNum" value="1" />
				<input type="hidden" name="intermodalInfo" value="" />
				<input type="hidden" name="recordId" value="${recordId}" />
				<input type="hidden" id="airtypeHidden" name="airtypeHidden" value="${airtype}" />

				<div class="inquiry_flights2">
					<div class="seach25 seach100">
						<span class="seach_check"><label for="radio24"><input name="flightArea" id="radio24" type="radio" value="4">国内</label>
						</span>
						<span class="seach_check"><label for="radio22"><input name="flightArea" id="radio22" type="radio" value="2">国际</label>
						</span> 
						<span class="seach_check"><label for="radio21"><input name="flightArea" id="radio21" type="radio" value="1">内陆</label>
						</span> 				
						<span class="seach_check"><label for="radio23"><input name="flightArea" id="radio23"  type="radio" value="3">国际+内陆</label>
						</span>
					</div>
					<div class="seach25">
						<p>
							<span class="xing">*</span>出发城市：
							
						</p>
						<p class="seach_r">
						
						
						<!-- 		<input type="text" id="departureCity"   class="auto1" name="departureCity"/>-->
					<select class="selectinput" id="departureCity" name="departureCity">
								<option selected="selected" value="-1">请选择</option>
								<c:forEach var="dics" items="${from_areaslist}">
									<option value="${dics.value}">${dics.label}</option>
								</c:forEach>
							</select> 
						</p>
					</div>
					<div class="seach25">
						<p>
							<span class="xing">*</span>到达城市：
						</p>
						<p class="seach_r">
						<!-- <input type="text" id="arrivedCity" class="auto2"  name="arrivedCity"/>-->
							<select class="selectinput" name="arrivedCity">
							<option selected="selected" value="-1">请选择</option>
								<c:forEach var="area" items="${arrivedCitys}">
									<option value="${area.id}">${area.name}</option>
								</c:forEach>
							</select> 
						</p>
					</div>
					<div class="seach25">
						<p><span class="xing">*</span>预收人数 ：</p>
						<p class="seach_r">
							<input type="text" id="reservationsNum" name="reservationsNum" value=""  maxlength="9" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
						</p>
					</div>
					<div class="kong"></div>
					<div class="seach25">
						<p>
							<span class="xing">*</span>出发机场：
						</p>
						<div class="input-append">
								<input id="leaveAirportId" name="leaveAirport" type="hidden" value="" class=""/> 
								<input id="leaveAirportName" name="leaveAirportName" readonly="readonly" type="text" value="" class="appendtext"/>
								<a id="leaveAirport" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
						</div>
					</div>
					<div class="seach25">
						<p>
							<span class="xing">*</span>到达机场：
						</p>
						<div class="input-append">
								<input id="desAirpostId" name="desAirpost" type="hidden" value="" /> 
								<input id="desAirpostName" name="desAirpostName" readonly="readonly" type="text" value="" class="appendtext"/> 
								<a id="desAirpost" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
						</div>
					</div>
					<div class="kong"></div>
					<div class="seach25">
						<p>
							<!--0470需求-->
							<c:if test="${companyUUID ne 'dfafad3ebab448bea81ca13b2eb0673e'}">
							<span class="xing">*</span>
							</c:if>
							出发时刻：</p>
						<p class="seach_r">
							<input type="text"  class="dateinput" name="startTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'2015-01-01',maxDate:'2020-12-31'})">
						</p>
					</div>
					<div class="seach25">
						<p>
							<!--0470需求-->
							<c:if test="${companyUUID ne 'dfafad3ebab448bea81ca13b2eb0673e'}">
							<span class="xing">*</span>
							</c:if>
							到达时刻：</p>
						<p class="seach_r">
							<input type="text" class="dateinput" name="arrivalTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})">
						</p>
					</div>
					<div class="seach25">
						<p>航班号：</p>
						<p class="seach_r">
							<input type="text" name="flightNumber" value=""  maxlength="20">
						</p>
					</div>
					<div class="kong"></div>
					<div class="seach25">
						<p>航空公司：</p>
						<p class="seach_r">
							<select name="airlines" id="airlines"
								onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
								<option value="-1">不限</option>
								<c:forEach var="airlines" items="${airlines_list}">
									<option value="${airlines.airlineCode}">${airlines.airlineName}</option>
								</c:forEach>
							</select>
						</p>
					</div>
					<div class="seach25">
						<p>舱位等级：</p>
						<p class="seach_r">
							<select name="spaceGrade"
								onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
								<option value="-1">不限</option>
							</select>
						</p>
					</div>
					<div class="seach25">
						<p>舱位：</p>
						<p class="seach_r">
							<select name="airSpace">
								<option value="-1">不限</option>
							</select>
						</p>
					</div>
					<div class="kong"></div>

					<div class="kongr"></div>
					
					<!--出票日期开始-->
					<div class="seach25">
						<p class="fbold f14">
							<!--0470需求-->
							<c:if test="${companyUUID ne 'dfafad3ebab448bea81ca13b2eb0673e'}">
							<span class="xing">*</span>
							</c:if>
								出票日期：</p>
						<div><input type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="" name="outTicketTime" class="dateinput"></div>
					</div>
					<div class="kongr"></div>
					<c:if test="${fn:length(groupCodeRule) ne 0}">
						<div class="seach25">
							<p class="fbold f14">团号类别：</p>
							<div>
								<select id="groupCodeRule">
									<c:forEach items="${groupCodeRule}" var="codeRule">
										<option value="${codeRule[0]}">${codeRule[1]}</option>
									</c:forEach>
								</select>
							</div>
	                     	<input type="hidden" id="groupCode" name="groupCode"/>
						</div>
					</c:if>
					<!--出票日期结束-->
					<!--离境口岸开始-->
                      <div class="seach25" style="display: none;" id="outAreadiv1">
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
<!--                                 <input type="text" value="0" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="8" name="intermodalAllPrice" id="intermodalAllPrice" /><span class="currency">元</span> -->
                                <input type="text" value="0"  onkeyup="isMoney(this)" onafterpaste="isMoney(this)"  maxlength="16" name="intermodalAllPrice" id="intermodalAllPrice" />
                                <span class="currency">
                                    <c:forEach items="${curlist}" var="currency" begin="0" end="0">
                                      
                                      <!-- 如下语句是对人民币的特殊显示处理,由于下拉列表展示的是'人民币'-元 -->
                                      <c:if test="${currency.currencyName=='人民币'}">
                                                                                                                              元
                                      </c:if>
                                      <!-- 如下语句是其他币种的显示处理,直接按照其在下拉列表中展示的值进行展示-如美元,日元等 -->
                                      <c:if test="${currency.currencyName!='人民币'}">
                                          ${currency.currencyName}                                                                                    
                                      </c:if>
                                      
                                  	</c:forEach>                                              
                                </span>
                            </div>
                            
       
                            <div id="groupTrans" class="transport_city" style="display: none;">
                                <p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart" name="intermodalGroupPart" maxlength="50" type="text"><label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  	</c:forEach>
                                </select>&nbsp;<input id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="16" type="text" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" />
                                
                                <span class="currency">
                                    <c:forEach items="${curlist}" var="currency" begin="0" end="0">
                                      
                                      <!-- 如下语句是对人民币的特殊显示处理,由于下拉列表展示的是'人民币'-元 -->
                                      <c:if test="${currency.currencyName=='人民币'}">
                                                                                                                              元
                                      </c:if>
                                      <!-- 如下语句是其他币种的显示处理,直接按照其在下拉列表中展示的值进行展示-如美元,日元等 -->
                                      <c:if test="${currency.currencyName!='人民币'}">
                                          ${currency.currencyName}                                                                                    
                                      </c:if>
                                      
                                  	</c:forEach>                                              
                                </span>
                                
                                <a class="ydbz_s transportAdd" onclick="javascript:transportAdd(this, 0);">增加</a></p>
                            </div>
                            
                        </div>
                        <!--联运结束-->
						<!--币种开始-->
                        <select id="templateCurrency" name="selectCurrency" class="sel-currency" style="display:none;">
                            <c:forEach items="${curlist}" var="currency">
                                <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                            </c:forEach>
                        </select>
                        <!--币种结束-->
					<div class="kong"></div>

					<!--占位方式开始-->
					<div class="seach25 seach100">
						<p class="fbold f14">
							<span class="xing">*</span>付款方式：
						</p>
						<p class="seach_r add-paytype">
							<input type="checkbox" class="ckb_mod error" id="payMode_deposit" name="payMode_deposit" value="1" onclick="paychg(this)"><font>订金占位</font>&#12288;&#12288;
							<label for="spinner" class="txt2" id="label_deposit"><span class="xing" id="deposit_xing" style="display: none;">*</span>保留时限：</label>
							<span id="payMode_deposit_span"> 
								<input disabled="disabled" id="activityDuration" class="spinner" name="remainDays_deposit" maxlength="3" value="" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" /> 
							<span style="padding-left:5px;">天</span> 
							</span> 
							
							<span id="payMode_deposit_span"> 
								<input disabled="disabled" id="activityDuration_hour" class="spinner_hour" name="remainDays_deposit_hour" maxlength="2" value="" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" /> 
							<span style="padding-left:5px;">时</span> 
							</span> 
							
							<span id="payMode_deposit_span"> 
								<input disabled="disabled" id="activityDuration_fen" class="spinner_fen" name="remainDays_deposit_fen" maxlength="2" value="" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" /> 
							<span style="padding-left:5px;">分</span> 
							</span> 
							
							<br /> 
							
							<input type="checkbox" class="ckb_mod valid" id="payMode_advance" name="payMode_advance" value="1" onclick="paychg(this)"><font>预占位</font>&#12288;&#12288;&#12288;
							
							<label for="spinner" class="txt2" id="label_advance"><span class="xing" id="advance_xing" style="display: none;">*</span>保留时限：</label>
							
							<span id="payMode_advance_span"> 
								<input disabled="disabled" id="activityDuration" class="spinner" name="remainDays_advance" maxlength="3" value="" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" disabled="disabled" /> 
								<span style="padding-left:5px;">天</span>
							</span> 
							
							<span id="payMode_advance_span"> 
								<input disabled="disabled" id="activityDuration_hour" class="spinner_hour" name="remainDays_advance_hour" maxlength="2" value="" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"  /> 
								<span style="padding-left:5px;">时</span>
							</span>
							
							<span id="payMode_advance_span"> 
								<input disabled="disabled" id="activityDuration_fen" class="spinner_fen" name="remainDays_advance_fen" maxlength="2" value="" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"  /> 
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
				<input type="hidden" name="recordId" value="${recordId}" />
				<div class="inquiry_flights1">
					<div class="seach25 seach100">
						<span class="seach_check"><label for="radio14"><input name="flightArea" id="radio14" type="radio" value="4">国内</label>
						</span>
						<span class="seach_check"><label for="radio12"><input name="flightArea" id=" radio12" type="radio" value="2">国际</label>
						</span>
						<span class="seach_check"><label for="radio11"><input name="flightArea" id="radio11" type="radio" value="1">内陆</label>
						</span> 
						 <span class="seach_check"><label for="radio13"><input name="flightArea" id=" radio13" type="radio" value="3">国际+内陆</label>
						</span>
					</div>

					<div class="seach25">
						<p><span class="xing">*</span>出发城市：</p>
						<p class="seach_r">
						
						<!-- <input type="text" id="departureCity"  class="auto3" name="departureCity"/>-->
						
							<select class="selectinput" id="departureCity"
								name="departureCity">
								<option selected="selected" value="-1">请选择</option>
								<c:forEach var="dic" items="${from_areaslist}">
									<option value="${dic.value}">${dic.label}</option>
								</c:forEach>
							</select>
							
						</p>
					</div>
					<div class="seach25">
						<p><span class="xing">*</span>到达城市：</p>
						<p class="seach_r">
						<!--	<input type="text" id="arrivedCity" class="auto4"   name="arrivedCity"/>
						 -->
							<select class="selectinput" id="arrivedCity" name="arrivedCity">
							<option selected="selected" value="-1">请选择</option>
								<c:forEach var="area" items="${arrivedCitys}">
									<option value="${area.id}">${area.name}</option>
								</c:forEach>
							</select>
						</p>
					</div>
					<div class="seach25">
						<p><span class="xing">*</span>预收人数 ：</p>
						<p class="seach_r">
							<input type="text" id="reservationsNum" name="reservationsNum" value=""   maxlength="9"  onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
						</p>
					</div>
					<div class="title_samil">去程：</div>
					<div class="kong"></div>
					<div class="seach25">
						<p><span class="xing">*</span>出发机场：</p>
						<div class="input-append">
								<input id="leaveAirport1Id" name="leaveAirport" type="hidden" value="" /> 
								<input id="leaveAirport1Name" name="leaveAirportName" readonly="readonly" type="text" value=""  class="appendtext"/> 
								<a id="leaveAirport1" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
						</div>
					</div>
					<div class="seach25">
						<p>
							<!--0470需求-->
							<c:if test="${companyUUID ne 'dfafad3ebab448bea81ca13b2eb0673e'}">
							<span class="xing">*</span>
							</c:if>
							到达机场：
						</p>
						<div class="input-append">
								<input id="desAirpost1Id" name="desAirpost" type="hidden" value="" /> 
								<input id="desAirpost1Name" name="targetAreaNameList" readonly="readonly" type="text" value="" class="appendtext"/> 
								<a id="desAirpost1" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
						</div>
					</div>
					<div class="kong"></div>
					<div class="seach25">
						<p>
							<!--0470需求-->
							<c:if test="${companyUUID ne 'dfafad3ebab448bea81ca13b2eb0673e'}">
							<span class="xing">*</span>
							</c:if>
							出发时刻：</p>
						<p class="seach_r">
							<input type="text" class="dateinput"  name="startTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'2015-01-01',maxDate:'2020-12-31'})">
						</p>
					</div>
					<div class="seach25">
						<p>
							<!--0470需求-->
							<c:if test="${companyUUID ne 'dfafad3ebab448bea81ca13b2eb0673e'}">
							<span class="xing">*</span>
							</c:if>
							到达时刻：</p>
						<p class="seach_r">
							<input type="text" class="dateinput" name="arrivalTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})">
						</p>
					</div>
					<div class="seach25">
						<p>航班号：</p>
						<p class="seach_r">
							<input type="text" name="flightNumber"  maxlength="20" value="">
						</p>
					</div>

					<div class="kong"></div>
					<div class="seach25">
						<p>航空公司：</p>
						<p class="seach_r">
							<select name="airlines" id="airlines"
								onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
								<option value="-1">不限</option>
								<c:forEach var="airlines" items="${airlines_list}">
									<option value="${airlines.airlineCode}">${airlines.airlineName}</option>
								</c:forEach>
							</select>
						</p>
					</div>
					<div class="seach25">
						<p>舱位等级：</p>
						<p class="seach_r">
							<select name="spaceGrade"
								onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
								<option value="-1">不限</option>
							</select>
						</p>
					</div>
					<div class="seach25">
						<p>舱位：</p>
						<p class="seach_r">
							<select name="airSpace" id="">
								<option value="-1">不限</option>
							</select>
						</p>
					</div>
					<div class="kong"></div>

					<div class="title_samil">返程：</div>
					<div class="seach25">
						<p>
							<span class="xing">*</span>出发机场：
						</p>
						<div class="input-append">
								<input id="leaveAirport2Id" name="leaveAirport" type="hidden" value="" /> 
								<input id="leaveAirport2Name" name="leaveAirportName" readonly="readonly" type="text" value=""  class="appendtext"/> 
								<a id="leaveAirport2" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>

						</div>
					</div>
					<div class="seach25">
						<p>
							<span class="xing">*</span>到达机场：
						</p>
						<div class="input-append">
								<input id="desAirpost2Id" name="desAirpost" type="hidden" value="" /> 
								<input id="desAirpost2Name" name="targetAreaNameList" readonly="readonly" type="text" value=""  class="appendtext"/> 
								<a id="desAirpost2" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
						</div>
					</div>
					<div class="kong"></div>
					<div class="seach25">
						<p>
							<!--0470需求-->
							<c:if test="${companyUUID ne 'dfafad3ebab448bea81ca13b2eb0673e'}">
							<span class="xing">*</span>
							</c:if>
							出发时刻：</p>
						<p class="seach_r">
							<input type="text" class="dateinput" id="wfstartTime" name="startTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'2015-01-01',maxDate:'2020-12-31'})">
						</p>
					</div>
					<div class="seach25">
						<p>
							<!--0470需求-->
							<c:if test="${companyUUID ne 'dfafad3ebab448bea81ca13b2eb0673e'}">
							<span class="xing">*</span>
							</c:if>
							到达时刻：</p>
						<p class="seach_r">
							<input type="text" class="dateinput" name="arrivalTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})">
						</p>
					</div>
					<div class="seach25">
						<p>航班号：</p>
						<p class="seach_r">
							<input type="text" name="flightNumber"  maxlength="20"  value="">
						</p>
					</div>
					<div class="kong"></div>
					<div class="seach25">
						<p>航空公司：</p>
						<p class="seach_r">
							<select name="airlines" id="airlines"
								onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
								<option value="-1">不限</option>
								<c:forEach var="airlines" items="${airlines_list}">
									<option value="${airlines.airlineCode}">${airlines.airlineName}</option>
								</c:forEach>
							</select>
						</p>
					</div>
					<div class="seach25">
						<p>舱位等级：</p>
						<p class="seach_r">
							<select name="spaceGrade"
								onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
								<option value="-1">不限</option>
							</select>
						</p>
					</div>
					<div class="seach25">
						<p>舱位：</p>
						<p class="seach_r">
							<select name="airSpace" id="">
								<option value="-1">不限</option>
							</select>
						</p>
					</div>
					<div class="kong"></div>
					<div class="kongr"></div>
					
					<!--出票日期开始-->
						<div class="seach25">
						<p class="fbold f14">
							<!--0470需求-->
							<c:if test="${companyUUID ne 'dfafad3ebab448bea81ca13b2eb0673e'}">
							<span class="xing">*</span></c:if>出票日期：
						</p>
						<div><input type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="" name="outTicketTime" class="dateinput"></div>
						</div>
					<div class="kongr"></div>
					<c:if test="${fn:length(groupCodeRule) ne 0}">
						<div class="seach25">
							<p class="fbold f14">团号类别：</p>
							<div>
								<select id="groupCodeRule">
									<c:forEach items="${groupCodeRule}" var="codeRule">
										<option value="${codeRule[0]}">${codeRule[1]}</option>
									</c:forEach>
								</select>
							</div>
	                     	<input type="hidden" id="groupCode" name="groupCode"/>
						</div>
					</c:if>
						<!--出票日期结束-->
						<!--离境口岸开始-->
                        <div class="seach25" style="display: none;" id="outAreadiv">
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
                                <input type="text" onafterpaste="isMoney(this)" onkeyup="isMoney(this)" maxlength="16" name="intermodalAllPrice" id="intermodalAllPrice">
                                
                                <span class="currency">
                                    <c:forEach items="${curlist}" var="currency" begin="0" end="0">
                                      
                                      <!-- 如下语句是对人民币的特殊显示处理,由于下拉列表展示的是'人民币'-元 -->
                                      <c:if test="${currency.currencyName=='人民币'}">
                                                                                                                              元
                                      </c:if>
                                      <!-- 如下语句是其他币种的显示处理,直接按照其在下拉列表中展示的值进行展示-如美元,日元等 -->
                                      <c:if test="${currency.currencyName!='人民币'}">
                                          ${currency.currencyName}                                                                                    
                                      </c:if>
                                      
                                  	</c:forEach>                                              
                                </span>
                                
                            </div>
                            <div id="groupTrans" class="transport_city" style="display: none;">
                                <p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart" name="intermodalGroupPart" maxlength="50" type="text"><label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  	</c:forEach>
                                </select>&nbsp;<input id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="16" type="text" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" />
                                
                                <span class="currency">
                                    <c:forEach items="${curlist}" var="currency" begin="0" end="0">
                                      
                                      <!-- 如下语句是对人民币的特殊显示处理,由于下拉列表展示的是'人民币'-元 -->
                                      <c:if test="${currency.currencyName=='人民币'}">
                                                                                                                              元
                                      </c:if>
                                      <!-- 如下语句是其他币种的显示处理,直接按照其在下拉列表中展示的值进行展示-如美元,日元等 -->
                                      <c:if test="${currency.currencyName!='人民币'}">
                                          ${currency.currencyName}                                                                                    
                                      </c:if>
                                      
                                  	</c:forEach>                                              
                                </span>
                                
                                <a class="ydbz_s transportAdd" onclick="javascript:transportAdd(this, 0);">增加</a></p>
                            </div>
                        </div>
                        <!--联运结束-->
						
					<div class="kong"></div>
					<!--占位方式开始-->
					<div class="seach25 seach100">
						<p class="fbold f14">
							<span class="xing">*</span>付款方式：
						</p>
						<p class="seach_r add-paytype">
							<input type="checkbox" class="ckb_mod error" id="payMode_deposit" name="payMode_deposit" value="1" onclick="paychg(this)"><font>订金占位</font>&#12288;&#12288;
							<label for="spinner" class="txt2" id="label_deposit"><span class="xing" id="deposit_xing" style="display: none;">*</span>保留时限：</label>
							<span id="payMode_deposit_span"> <input disabled="disabled" id="activityDuration" class="spinner" name="remainDays_deposit" maxlength="3" value=""
								onafterpaste="this.value=this.value.replace(/\D/g,'')"
								onkeyup="this.value=this.value.replace(/\D/g,'')" /> <span
								style="padding-left:5px;">天</span> 
							</span> 
							
							<span id="payMode_deposit_span"> <input disabled="disabled" id="activityDuration_hour" class="spinner_hour" name="remainDays_deposit_hour" maxlength="2" value=""
								onafterpaste="this.value=this.value.replace(/\D/g,'')"
								onkeyup="this.value=this.value.replace(/\D/g,'')" /> <span
								style="padding-left:5px;">时</span> 
							</span>
							
							<span id="payMode_deposit_span"> <input disabled="disabled" id="activityDuration_fen" class="spinner_fen" name="remainDays_deposit_fen" maxlength="2" value=""
								onafterpaste="this.value=this.value.replace(/\D/g,'')"
								onkeyup="this.value=this.value.replace(/\D/g,'')" /> <span
								style="padding-left:5px;">分</span> 
							</span>
							
							<br /> 
							
							<input type="checkbox" class="ckb_mod valid" id="payMode_advance" name="payMode_advance" value="1" onclick="paychg(this)" /><font>预占位</font>&#12288;&#12288;&#12288;
							<label for="spinner" class="txt2" id="label_advance"><span
								class="xing" id="advance_xing" style="display: none;">*</span>保留时限：</label>
							<span id="payMode_advance_span"> 
								<input disabled="disabled" id="activityDuration" class="spinner" name="remainDays_advance" maxlength="3" value=""
								onafterpaste="this.value=this.value.replace(/\D/g,'')"
								onkeyup="this.value=this.value.replace(/\D/g,'')" /> 
							<span style="padding-left:5px;">天</span> 
							</span>
							
							<span id="payMode_advance_span"> 
								<input disabled="disabled" id="activityDuration_hour" class="spinner_hour" name="remainDays_advance_hour" maxlength="2" value=""
								onafterpaste="this.value=this.value.replace(/\D/g,'')"
								onkeyup="this.value=this.value.replace(/\D/g,'')" /> 
							<span style="padding-left:5px;">时</span> 
							</span>
							
							<span id="payMode_advance_span"> 
								<input disabled="disabled" id="activityDuration_fen" class="spinner_fen" name="remainDays_advance_fen" maxlength="2" value=""
								onafterpaste="this.value=this.value.replace(/\D/g,'')"
								onkeyup="this.value=this.value.replace(/\D/g,'')" /> 
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

					
						
						<div class="kong"></div>
						
						
						
						<!--占位方式结束-->
					</div>
			</form>
			<!-- 多段 -->
			<form action="${ctx}/airTicket/secondform" method="post" id="frm1">
				<input type="hidden" name="airType" value="1" /> 
				<input type="hidden" id="moreSenum" name="sectionNum" value="2" />
				<input type="hidden" name="intermodalInfo" value="" />
				<input type="hidden" name="recordId" value="${recordId }" />

				<div class="inquiry_flights3">
					<div>
						<p class="main-right-topbutt">
							<%--bug17551 添加主按钮样式--%>
							<a class="primary" href="javascript:void(0);" onclick="inquiryFlights3Add(this)">新增航段</a>
						</p>
						<div class="seach25">
							<p>
								<span class="xing">*</span>出发城市：
							</p>
							<p class="seach_r">
								<!-- 	<input type="text" id="departureCity" class='auto5'  name="departureCity"/>
							-->
								<select class="selectinput" id="departureCity"
									name="departureCity">
									<option selected="selected" value="-1">请选择</option>
									<c:forEach var="dic" items="${from_areaslist}">
										<option value="${dic.value}">${dic.label}</option>
									</c:forEach>
								</select>
							</p>
						</div>
						<div class="seach25">
							<p><span class="xing">*</span>到达城市：</p>
							<p class="seach_r">
								<!--<input type="text" id="arrivedCity" class='auto6'   name="arrivedCity"/>-->
								 <select class="selectinput" name="arrivedCity">
								<option selected="selected" value="-1">请选择</option>
									<c:forEach var="area" items="${arrivedCitys}">
										<option value="${area.id}">${area.name}</option>
									</c:forEach>
								</select> 
							</p>
						</div>
						<div class="seach25">
							<p><span class="xing">*</span>预收人数 ：</p>
							<p class="seach_r">
								<input type="text" id="reservationsNum" name="reservationsNum"
									value=""  maxlength="9" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
							</p>
						</div>
						<div class="kong"></div>
						<div class="addFlights3Div">
							<div class="title_samil">
								第1段： <span class="seach_check"><label for="radio31-4"><input
										name="flightArea1" type="radio" value="4"  id="radio31-4">
										国内</label> </span>
								<span class="seach_check"><label for="radio31-2"><input
										name="flightArea1" type="radio" value="2" id="radio31-2"> 国际</label>
								</span> 
								<span class="seach_check"><label for="radio31-1"><input
										name="flightArea1" type="radio" value="1" id="radio31-1">
										内陆</label>
								</span> 
								<span class="seach_check"><label for="radio13-3"><input
										name="flightArea1" type="radio" value="3" id="radio13-3"> 国际+内陆</label>
								</span>
							</div>
							<div class="seach25">
								<p>
									<span class="xing">*</span>出发机场：
								</p>
								<div class="input-append">
										<input id="mleaveAirport1Id" name="leaveAirport" type="hidden" value="" /> 
										<input id="mleaveAirport1Name" name="leaveAirportName" readonly="readonly" type="text" value=""  class="appendtext"/> 
										<a id="mleaveAirport1" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div>
							</div>
							<div class="seach25">
								<p>
									<span class="xing">*</span>到达机场：
								</p>
								<div class="input-append">
									<input id="mdesAirpost1Id" name="desAirpost" type="hidden" value="" /> 
									<input id="mdesAirpost1Name" name="desAirpostName" readonly="readonly" type="text" value=""  class="appendtext"/> 
									<a id="mdesAirpost1" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div>
							</div>
							<div class="kong"></div>
							<div class="seach25">
								<p>
									<!--0470需求-->
									<c:if test="${companyUUID ne 'dfafad3ebab448bea81ca13b2eb0673e'}">
									<span class="xing">*</span>
									</c:if>
									出发时刻：</p>
								<p class="seach_r">
									<input type="text" class="dateinput" name="startTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm' ,minDate:'2015-01-01',maxDate:'2020-12-31'})">
								</p>
							</div>
							<div class="seach25">
								<p>
									<!--0470需求-->
									<c:if test="${companyUUID ne 'dfafad3ebab448bea81ca13b2eb0673e'}">
									<span class="xing">*</span>
									</c:if>
									到达时刻：</p>
								<p class="seach_r">
									<input type="text" class="dateinput" name="arrivalTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})">
								</p>
							</div>
							<div class="seach25">
								<p>航班号：</p>
								<p class="seach_r">
									<input type="text" name="flightNumber"  maxlength="20" value="">
								</p>
							</div>
							<div class="kong"></div>
							<div class="seach25">
								<p>航空公司：</p>
								<p class="seach_r">
									<select name="airlines" id="airlines"
										onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
										<option value="-1">不限</option>
										<c:forEach var="airlines" items="${airlines_list}">
											<option value="${airlines.airlineCode}">${airlines.airlineName}</option>
										</c:forEach>
									</select>
								</p>
							</div>
							<div class="seach25">
								<p>舱位等级：</p>
								<p class="seach_r">
									<select name="spaceGrade"
										onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
										<option value="-1">不限</option>
									</select>
								</p>
							</div>
							<div class="seach25">
								<p>舱位：</p>
								<p class="seach_r">
									<select name="airSpace">
										<option value="-1">不限</option>
									</select>
								</p>
							</div>
							<div class="kong"></div>
						</div>
						<div class="addFlights3Div">
							<div class="title_samil">
								第2段： <span class="seach_check"><label for="radio32-4"><input
										name="flightArea2" id="radio32-4" type="radio" value="4"
										> 国内</label>
								</span>
								<span class="seach_check"><label for="radio32-2"><input
										name="flightArea2" id="radio32-2" type="radio" value="2">
										国际</label>
								</span>
								<span class="seach_check"><label for="radio32-1"><input
										name="flightArea2" id="radio32-1" type="radio" value="1"
										> 内陆</label>
								</span> 
								 <span class="seach_check"><label for="radio32-3"><input
										name="flightArea2" id="radio32-3" type="radio" value="3">
										国际+内陆</label>
								</span>
							</div>
							<div class="seach25">
								<p>
									<span class="xing">*</span>出发机场：
								</p>
								<div class="input-append">
										<input id="mleaveAirport2Id" name="leaveAirport" type="hidden" value="" /> 
										<input id="mleaveAirport2Name" name="leaveAirportName" readonly="readonly" type="text" value=""  class="appendtext"/> 
										<a id="mleaveAirport2" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div>
							</div>
							<div class="seach25">
								<p>
									<span class="xing">*</span>到达机场：
								</p>
								<div class="input-append">
										<input id="mdesAirpost2Id" name="desAirpost" type="hidden" value="" /> 
										<input id="mdesAirpost2Name" name="desAirpostName" readonly="readonly" type="text" value=""  class="appendtext"/> 
										<a id="mdesAirpost2" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div>
							</div>
							<div class="kong"></div>
							<div class="seach25">
								<p>
									<!--0470需求-->
									<c:if test="${companyUUID ne 'dfafad3ebab448bea81ca13b2eb0673e'}">
									<span class="xing">*</span>
									</c:if>
									出发时刻：</p>
								<p class="seach_r">
									<input type="text" id="ddstarttime" class="dateinput" name="startTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})">
								</p>
							</div>
							<div class="seach25">
								<p>
									<!--0470需求-->
									<c:if test="${companyUUID ne 'dfafad3ebab448bea81ca13b2eb0673e'}">
									<span class="xing">*</span>
									</c:if>
									到达时刻：</p>
								<p class="seach_r">
									<input type="text" class="dateinput" name="arrivalTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})">
								</p>
							</div>
							<div class="seach25">
								<p>航班号：</p>
								<p class="seach_r">
									<input type="text" name="flightNumber"  maxlength="20" value="">
								</p>
							</div>
							<div class="kong"></div>
							<div class="seach25">
								<p>航空公司：</p>
								<p class="seach_r">
									<select name="airlines" id="airlines"
										onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
										<option value="-1">不限</option>
										<c:forEach var="airlines" items="${airlines_list}">
											<option value="${airlines.airlineCode}">${airlines.airlineName}</option>
										</c:forEach>
									</select>
								</p>
							</div>
							<div class="seach25">
								<p>舱位等级：</p>
								<p class="seach_r">
									<select name="spaceGrade"
										onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
										<option value="-1">不限</option>

									</select>
								</p>
							</div>
							<div class="seach25">
								<p>舱位：</p>
								<p class="seach_r">
									<select name="airSpace" id="">
										<option value="-1">不限</option>
									</select>
								</p>
							</div>
							<div class="kong"></div>
						</div>
						<!-- <div id="addDiv">
                            </div> -->
					</div>
					<div class="kongr"></div>

					<!--出票日期开始-->
					<div class="seach25">
						<p class="fbold f14">
							<!--0470需求-->
							<c:if test="${companyUUID ne 'dfafad3ebab448bea81ca13b2eb0673e'}">
							<span class="xing">*</span>
							</c:if>
								出票日期：</p>
						<div>
							<input type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="" name="outTicketTime" class="dateinput"></div>
					</div>
					<div class="kongr"></div>
					<c:if test="${fn:length(groupCodeRule) ne 0}">
						<div class="seach25">
							<p class="fbold f14">团号类别：</p>
							<div>
								<select id="groupCodeRule">
									<c:forEach items="${groupCodeRule}" var="codeRule">
										<option value="${codeRule[0]}">${codeRule[1]}</option>
									</c:forEach>
								</select>
							</div>
	                     	<input type="hidden" id="groupCode" name="groupCode"/>
						</div>
					</c:if>
						<!--出票日期结束-->
						<!--离境口岸开始-->
                        <div class="seach25" style="display: none;" id="outAreadiv2">
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
                                <input type="text" onafterpaste="isMoney(this)" onkeyup="isMoney(this)" maxlength="16" name="intermodalAllPrice" id="intermodalAllPrice">
                                
                               <span class="currency">
                                    <c:forEach items="${curlist}" var="currency" begin="0" end="0">
                                      
                                      <!-- 如下语句是对人民币的特殊显示处理,由于下拉列表展示的是'人民币'-元 -->
                                      <c:if test="${currency.currencyName=='人民币'}">
                                                                                                                              元
                                      </c:if>
                                      <!-- 如下语句是其他币种的显示处理,直接按照其在下拉列表中展示的值进行展示-如美元,日元等 -->
                                      <c:if test="${currency.currencyName!='人民币'}">
                                          ${currency.currencyName}                                                                                    
                                      </c:if>
                                      
                                  	</c:forEach>                                              
                                </span>
                                
                            </div>
                            <div id="groupTrans" class="transport_city" style="display: none;">
                                <p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart" name="intermodalGroupPart" maxlength="50" type="text"><label>联运价格：</label>
                                <select name="selectCurrency" class="sel-currency">
                                    <c:forEach items="${curlist}" var="currency">
                                      <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                                  	</c:forEach>
                                </select>&nbsp;<input id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="16" type="text" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" />
                                
                                <span class="currency">
                                    <c:forEach items="${curlist}" var="currency" begin="0" end="0">
                                      
                                      <!-- 如下语句是对人民币的特殊显示处理,由于下拉列表展示的是'人民币'-元 -->
                                      <c:if test="${currency.currencyName=='人民币'}">
                                                                                                                              元
                                      </c:if>
                                      <!-- 如下语句是其他币种的显示处理,直接按照其在下拉列表中展示的值进行展示-如美元,日元等 -->
                                      <c:if test="${currency.currencyName!='人民币'}">
                                          ${currency.currencyName}                                                                                    
                                      </c:if>
                                      
                                  	</c:forEach>                                              
                                </span>
                                
                                <a class="ydbz_s transportAdd" onclick="javascript:transportAdd(this, 0);">增加</a></p>
                            </div>
                        </div>
                        <!--联运结束-->


						<div class="kong"></div>
						<!--占位方式开始-->
						<div class="seach25 seach100">
							<p class="fbold f14">
								<span class="xing">*</span>付款方式：
							</p>
							<p class="seach_r add-paytype">
								<input type="checkbox" class="ckb_mod error" id="payMode_deposit" name="payMode_deposit" value="1" onclick="paychg(this)" /><font>订金占位</font>&#12288;&#12288;
								<label
									for="spinner" class="txt2" id="label_deposit"><span
									class="xing" id="deposit_xing" style="display: none;">*</span>保留时限：</label>
								
								<span id="payMode_deposit_span"> 
									<input disabled="disabled" id="activityDuration" class="spinner" name="remainDays_deposit" maxlength="3" value=""
									onafterpaste="this.value=this.value.replace(/\D/g,'')"
									onkeyup="this.value=this.value.replace(/\D/g,'')" /> 
								<span style="padding-left:5px;">天</span> 
								</span>
								
								<span id="payMode_deposit_span"> 
									<input disabled="disabled" id="activityDuration_hour" class="spinner_hour" name="remainDays_deposit_hour" maxlength="2" value=""
									onafterpaste="this.value=this.value.replace(/\D/g,'')"
									onkeyup="this.value=this.value.replace(/\D/g,'')" /> 
								<span style="padding-left:5px;">时</span> 
								</span>
								
								<span id="payMode_deposit_span"> 
									<input disabled="disabled" id="activityDuration_fen" class="spinner_fen" name="remainDays_deposit_fen" maxlength="2" value=""
									onafterpaste="this.value=this.value.replace(/\D/g,'')"
									onkeyup="this.value=this.value.replace(/\D/g,'')" /> 
								<span style="padding-left:5px;">分</span> 
								</span> 
								
								<br /> 
								
								<input type="checkbox" class="ckb_mod valid" id="payMode_advance" name="payMode_advance" value="1" onclick="paychg(this)"><font>预占位</font>&#12288;&#12288;&#12288;
								<label for="spinner" class="txt2" id="label_advance"><span class="xing" id="advance_xing" style="display: none;">*</span>保留时限：</label>
								<span id="payMode_advance_span"> 
									<input disabled="disabled" id="activityDuration" class="spinner" name="remainDays_advance" maxlength="3" value=""
									onafterpaste="this.value=this.value.replace(/\D/g,'')"
									onkeyup="this.value=this.value.replace(/\D/g,'')" /> 
								<span style="padding-left:5px;">天</span> 
								</span>
								
								<span id="payMode_advance_span"> 
									<input disabled="disabled" id="activityDuration_hour" class="spinner_hour" name="remainDays_advance_hour" maxlength="2" value=""
									onafterpaste="this.value=this.value.replace(/\D/g,'')"
									onkeyup="this.value=this.value.replace(/\D/g,'')" /> 
								<span style="padding-left:5px;">时</span> 
								</span>
								
								<span id="payMode_advance_span"> 
									<input disabled="disabled" id="activityDuration_fen" class="spinner_fen" name="remainDays_advance_fen" maxlength="2" value=""
									onafterpaste="this.value=this.value.replace(/\D/g,'')"
									onkeyup="this.value=this.value.replace(/\D/g,'')" /> 
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
							<div class="title_samil">
								第<em>3</em>段：<span class="seach_check"><label><input type="radio" name="flightAreax" value="4"  />国内</label>
								</span> 
								<span class="seach_check"><label><input type="radio" name="flightAreax" value="2" /> 国际</label>
								</span>
								<span class="seach_check"><label><input type="radio" name="flightAreax" value="1"  />内陆</label>
								</span>  
								<span class="seach_check"><label><input type="radio" name="flightAreax" value="3" /> 国际+内陆</label>
								</span>
								<%--bug17551 调整按钮样式--%>
								<p class="main-right-topbutt">
									<a href="javascript:void(0);" style="margin-right: 25px;"
										onclick="inquiryFlights3Del(this);">删除航段</a>
								</p>
							</div>
							<div class="seach25">
								<p>
									<span class="xing">*</span>出发机场：
								</p>
								<div class="input-append">
										<input id="mleaveAirportxId" name="leaveAirport" type="hidden" value="" /> 
										<input id="mleaveAirportxName" name="leaveAirportName" readonly="readonly" type="text" value=""  class="appendtext"/> 
										<a id="mleaveAirportx" name="leaveHref" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div>
							</div>
							<div class="seach25">
								<p>
									<span class="xing">*</span>到达机场：
								</p>
								<div class="input-append">
										<input id="mdesAirpostxId" name="desAirpost" type="hidden" value="" /> 
										<input id="mdesAirpostxName" name="desAirpostName" readonly="readonly" type="text" value=""  class="appendtext"/> 
										<a id="desAirpostx" name="desHref" class="btn appendbtn" onclick="airportClick(this)" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
								</div>
							</div>
							<div class="kong"></div>
							<div class="seach25">
								<p>
									<!--0470需求-->
									<c:if test="${companyUUID ne 'dfafad3ebab448bea81ca13b2eb0673e'}">
									<span class="xing">*</span>
									</c:if>
									出发时刻：</p>
								<p class="seach_r">
									<input type="text" class="dateinput" name="startTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})">
								</p>
							</div>
							<div class="seach25">
								<p>
									<!--0470需求-->
									<c:if test="${companyUUID ne 'dfafad3ebab448bea81ca13b2eb0673e'}">
									<span class="xing">*</span>
									</c:if>
									到达时刻：</p>
								<p class="seach_r">
									<input type="text" class="dateinput" name="arrivalTime" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})">
								</p>
							</div>
							<div class="seach25">
								<p>航班号：</p>
								<p class="seach_r">
									<input type="text" name="flightNumber"  maxlength="20" value="">
								</p>
							</div>
							<div class="kong"></div>
							<div class="seach25">
								<p>航空公司：</p>
								<p class="seach_r">
									<select name="airlines" id="airlines"
										onchange="selectairline(this.options[this.options.selectedIndex].value,this)">
										<option value="-1">不限</option>
										<c:forEach var="airlines" items="${airlines_list}">
											<option value="${airlines.airlineCode}">${airlines.airlineName}</option>
										</c:forEach>
									</select>
								</p>
							</div>
							<div class="seach25">
								<p>舱位等级：</p>
								<p class="seach_r">
									<select name="spaceGrade"
										onchange="selectspaceGrade(this.options[this.options.selectedIndex].value,this)">
										<option value="-1">不限</option>

									</select>
								</p>
							</div>
							<div class="seach25">
								<p>舱位：</p>
								<p class="seach_r">
									<select name="airSpace">
										<option value="-1">不限</option>
									</select>
								</p>
							</div>
							<div class="kong"></div>
						</div>



			<div class="kong"></div>
			<div class="seach_btnbox">
				<input type="button" value="下一步" onclick="validateDate()"
					class="btn btn-primary" />
			</div>
		</div>

	</div>

	<!--右侧内容部分结束-->
</body>
</html>