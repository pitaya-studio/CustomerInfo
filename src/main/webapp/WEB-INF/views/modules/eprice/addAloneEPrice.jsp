<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>新增${teamTypeShow}询价</title>
	<meta name="decorator" content="wholesaler"/>
	<%@ include file="commonVar.jsp"%>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/travelRequirementsData.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/addAloneEPrice.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/epriceCommon.js" type="text/javascript"></script>
	<script type="text/javascript">
	$(function(){
		//展开筛选
		launch();
		//文本框提示信息显示隐藏
		//inputTips();
		//新增第二步增加删除询价要求
		//inquiryTwoText();
		//新增第二步添加复选条件
		//inquiryAddSeachcheck();
		//上传动作
		//btfile();
		//询价客户类型
		//inquiry_radio_peoples();
		//
		//inquiry2AddIpt();
		
		//新增询价多选计调和线路国家
		inquiryCheckBOX();
		
		$("#eprice-c-div-id").find(".selectinput" ).comboboxInquiry({
			"afterInvalid":function(event,data){
				$(this).trigger("change");
			}
		});
		// 计算总人数
		$("input.blurSum").blur(function(){
			personSum();
		});
		$("#customerAgentId").comboboxInquiry().on('comboboxinquiryselect', function (event, obj) {
			fillContactByAgent(this);
		});
		$("#vendorOperator").comboboxInquiry();
	});
	
	jQuery.extend(jQuery.validator.messages, {
    	required: "必填信息"
    });
    
	// 上传文件信息处理
	/**
     * 附件上传回调方法
     * @param {Object} obj button对象
     * @param {Object} fileIDList  文件表id
     * @param {Object} fileNameList 文件原名称
     * @param {Object} filePathList 文件url
     */
    function commenFunction(obj,fileIDList,fileNameList,filePathList){
    	//var name = obj.name;
   // 	$("#upfileShow").append("<p class='seach_r'><span  class='seach_checkbox'  id='"+obj.name+"'></span></p>");
     	if(fileIDList){
     		var arrID = new Array();
     		arrID = fileIDList.split(';');
     		var arrName = new Array();
     		arrName = fileNameList.split(';');
     		var arrPath = new Array();
     		arrPath = filePathList.split(';');
     		for(var n=0;n<arrID.length;n++){
     			if(arrID[n]){
     				var $a = $('<a style="padding:0px 20px 0px 15px;position:relative; ">'+arrName[n]+'<span style="position:absolute;right:3px;top:4px;cursor: pointer;" class="deleteicon" href="javascript:void(0)" onclick="deleteSelected(this)">x</span></a>');
     				$a.append("<input type='hidden' name='salerTripFileId' value='"+arrID[n]+"'/>");
     				$a.append("<input type='hidden' name='salerTripFileName' value='"+arrName[n]+"'/>");
     				$a.append("<input type='hidden' name='salerTipFilePath' value='"+arrPath[n]+"'/>");
     				$("#upfileShow").append($a);
     			}
     		}
     	}
     }
     
     // 总人数计算
     function personSum(){
    	 var adult =parseInt($("#traffic_adultSum").val(),10);
    	 var child = parseInt($("#traffic_childSum").val(),10);
    	 var special = parseInt($("#traffic_specialPersonSum").val(),10);
    	 adult = isNaN(adult)?0:adult;
    	 child = isNaN(child)?0:child;
    	 special = isNaN(special)?0:special;
    	 var  allPersonSum = adult+child+special;
    	 
    	 $("#traffic_allPersonSum").val(allPersonSum);
     }

	</script>
	<style type="text/css">
		.custom-combobox .custom-combobox-input, .produceDiv .seach_r .custom-combobox {
			width:80px;
		}
	</style>
</head>
<body>
	
	<page:applyDecorator name="eprice_admit_create" >
    </page:applyDecorator>
        <!--第一步-->
        <div class="messageDiv inquiry_box1"  style="width:1040px">
          <form id="eprice-a-form-id">
            
            <div class="seach25">
              <p>销售姓名：</p>
              <p class="seach_r">${saler.name}<input type="hidden" name="salerId" value="${saler.id}" /></p>
            </div>
            
            <div class="seach25">
              	<p>电话：</p>
             	<p class="seach_r">${saler.mobile}</p>
            </div>
            
            <div class="seach25"> 
            	<p>邮箱：</p>
              	<p class="seach_r">${saler.email}</p>
            </div>
            
            <div class="kong"></div>
            
            <div class="seach25">
              <p>询价客户类型：</p>
              <p class="seach_r" >
              	<label for="inquiry_radio_people">
              		<input name="customerType" refshow="inquiry_radio_people1" id="customer-type-radio-id-1" type="radio" checked="checked"  value="1">直客
              	</label>
              	<label for="inquiry_radio_people2">
              		<input name="customerType" refshow="inquiry_radio_people2" id="customer-type-radio-id-2" type="radio" value="2" >同行
              	</label>
              	<!-- 
              	<label for="inquiry_radio_people3">
              		<input name="customerType" refshow="inquiry_radio_people3"  id="customer-type-radio-id-3" type="radio"  value="0">其它
              	</label> -->
              </p>
           </div>
           
            <div class="seach25">
              <p>团队类型：</p>
              <p class="seach_r">
               <input type="text"  readonly="readonly" name="teamTypeShow" value="${teamTypeShow}"/>
              </p>
               <input type="hidden" name="teamType" value="${teamType}"/>
              <input type="hidden" name="type" value="${type}"/>
              <input type="hidden" name="emode" value="${emode}"/>
            </div>
            
            <div class="kong"></div>
            
           <!--询价客户类型  直客选择触发  -->
           <div class="inquiry_radio_people1" name="inquiry_radio_people">
           		<div class="seach25">
		              <p><span class="xing">*</span>询价客户：</p>
		              <p class="seach_r"><input type="text"  name="customerName"  maxlength="16"/></p>
            	</div>
            	<div class="seach25">
		              <p><span class="xing">*</span>联系人：</p>
		              <p class="seach_r"><input type="text" name="contactPerson" maxlength="16"/></p>
            	</div>
	            <div class="seach25">
		              <p><span class="xing">*</span>电话：</p>
		              <p class="seach_r"><input type="text"  name="contactMobile" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="16" /></p>
	            </div>
	            <div class="seach25">
		              <p>其他联系方式：</p>
		              <p class="seach_r"><input type="text"  name="otherContactWay" maxlength="32" /></p>
	            </div>
            </div>
            
            <!--询价客户类型  同行选择触发  -->
            <div class="inquiry_radio_people2" name="inquiry_radio_people">
            	<div class="seach25">
		              <p><span class="xing">*</span>询价客户：</p>
		              <p class="seach_r">
			              <select name="customerAgentId" id="customerAgentId">
			              	  <option value="-1">不限</option>
				              <c:forEach items="${customerAgents}" var="au" varStatus="auindex">
					             <option value="${au.id}">${au.agentName }</option>
			              	  </c:forEach>
			              </select>
			              <c:forEach items="${customerAgents}" var="au" varStatus="auindex">
				              <input type="hidden" id='hidename${au.id}' value="${au.agentContact }"/>
				              <input type="hidden" id='hidephone${au.id}' value="${au.agentTelAreaCode }-${au.agentTel }"/>
				               <input type="hidden" id='hideOther${au.id}' value="${au.agentContactMobile }"/>
			              </c:forEach>
		              </p>
            	</div>
	            <div class="seach25">
		              <p><span class="xing">*</span>联系人：</p>
		              <p class="seach_r"><input id="togetherman"  type="text" name="contactPersontogether" maxlength="16"/></p>
	            </div>
	            <div class="seach25">
		              <p><span class="xing">*</span>电话：</p>
		              <p class="seach_r"><input id="togetherphone"   type="text" name="contactMobiletogether"  maxlength="16" onkeyup="value=value.replace(/[^\d]/g,'')"/></p>
	            </div>
	            <div class="seach25">
		              <p>其他联系方式：</p>
		              <p class="seach_r"><input type="text"  id="togetherOtherContactWay"  name="otherContactWay"  maxlength="20"/></p>
	            </div>
             </div>
             
             <!--询价客户类型  其他选择触发  -->
             <div class="inquiry_radio_people3" name="inquiry_radio_people">
                 
                  <div class="seach25">
		              <p><span class="xing">*</span>询价客户：</p>
		              <p class="seach_r"><input type="text"  name="otherCustomerName"  maxlength="16"/></p>
            	 </div>
            	 <div class="seach25">
		              <p><span class="xing">*</span>联系人：</p>
		              <p class="seach_r"><input type="text" name="otherContactPerson"  maxlength="16"/></p>
            	 </div>
	             <div class="seach25">
		              <p><span class="xing">*</span>电话：</p>
		              <p class="seach_r"><input type="text"  name="otherContactMobile"  maxlength="16" onkeyup="value=value.replace(/[^\d]/g,'')"/></p>
	             </div>
	             <div class="seach25">
		              <p>其他联系方式：</p>
		              <p class="seach_r"><input type="text" name="otherContactWay" maxlength="20"/></p>
	             </div>
            </div>
            
            <div class="kong"></div>
            
            
            <div class="seach25">
		              <p>客户预算：</p>
		              <p class="seach_r">
		              	<select name="budgetType" id="inquiry_people3_rad">
		              		<option value="1">按人报价</option>
		              		<option value="2">按团报价</option>
		              	</select>
		              </p>
		             <!-- <p class="seach_r"><input type="text" name="budget" /></p> --> 
             </div>
             <div class="seach25">
             		<p>预算金额：</p>
		             <p class="seach_r"> <input type="text" name="budget" maxlength="9"/></p> 
             </div>
             <div class="seach25 pr">
             	  <p>预算币种：</p>
	              <p class="seach_r">
	              	<select name="budgetPayTypeId" id="budgetPayTypeId">
	              		<option value="1">人民币</option>
	              	</select>
	              </p>
	         </div>
             
             <div class="seach25 pr">
             	  <p>预算备注：</p>
	              <p class="seach_r"><input name="budgetRemark" type="text" title="建议30字以内" flag="istips" maxlength="30"></p>
	         </div>
            
            <div class="kong"></div>
            
            <div class="seach25" >
	              <p><span class="xing">*</span>申请总人数：</p>
	              <p class="seach_r"><input type="text" name="allPersonSum"  id="base_allPersonSum"  onkeyup="value=value.replace(/[^\d]/g,'')"  maxlength="8" value="1"></p>
              </div>
            <div class="seach25">
       	       <p  class="seach_r" style="float:left;"><a id="expand_detailsSum" name="expand_detailsSum"  href="javascript:void(0)">展开</a></p>
            </div>
        
            <div class="kong"></div>
            <div id="ifShow_detailsSum" style="display:none">
	            <div class="seach25" >
	              <p><span class="xing">*</span>成人人数：</p>
	              <p class="seach_r"><input type="text" name="adultSum"  id="base_adultSum" onkeyup="value=value.replace(/[^\d]/g,'')"  value="1" maxlength="5"/></p>
	            </div>
	            <div class="seach25 pr jd-xs">
	              <p><span class="xing">*</span>儿童人数：</p>
	              <p class="seach_r"><input type="text" name="childSum"   id="base_childSum" onkeyup="value=value.replace(/[^\d]/g,'')"  title="12岁以内" flag="istips"  maxlength="5"/></p>
	            </div>
	            <div class="seach25">
	              <p><span class="xing">*</span>特殊人群：</p>
	              <p class="seach_r"> <input type="text"    id="base_specialPersonSum" onkeyup="value=value.replace(/[^\d]/g,'')"  name="specialPersonSum" value="0"  maxlength="5"/></p>
	            </div>
	            <div class="seach25"  >
	              <p style="width:106px">特殊人群说明：</p>
	              <p class="seach_r"> <input type="text"  name="specialRemark" value=""  title="建议20字以内"  maxlength="20"/></p>
	            </div>
            </div>
            
            <div class="kong"></div>
            <div class="seach_btnbox">
              <input type="button" id="eprice-a-form-submit-btn-id" value="下一步" class="btn btn-primary" /><!-- onclick="oneToTwo()" -->
            </div>
          </form>
        </div>
        <!--第二步-->
        <div class="messageDiv inquiry_box2"  style="width:1040px">
          <form id="eprice-b-form-id">
          	<div class="seach25 seach100">
	              <p><span class="xing">*</span>接待社计调：</p>
	              <p class="seach_r">
	              <!-- 
	              	<c:forEach items="${aoperators}" var="au" varStatus="auindex">
	              		<span class="seach_check">
			                <input type="checkbox" id="aoperator-user-id-${auindex.count}" name="aoperatorUserId" value="${au.id}"><label for="aoperator-user-id-${auindex.count}">${au.name}</label>
		                </span>
	              	</c:forEach>
	              	 -->
	              	<span class="seach_check">
		                <select name="vendorOperator" id="vendorOperator">
		                  <option value="0" selected="selected">请搜索计调</option>
		                  	<c:forEach items="${aoperators }" var="au">
		                  		<option value="${au.id }">${au.name}</option>
		                  	</c:forEach>
		                </select>
	                </span>
	                <span id="vendorOperatorShow" class="seach_checkbox"></span>
	              </p>
            </div>
            
            <!-- 线路国家 -->
            <div class="seach25 seach100">
              <p><span class="xing">*</span>线路国家：</p>
              <p class="seach_r">
              	<span class="seach_check">
              		<select name="country">
	                  <option value="0" selected="selected">搜索国家</option>
	                  	<c:forEach items="${areamapList }" var="au">
	                 		<option value="${au.id }">${au.name}</option>
	                 	</c:forEach>
	                </select>
              	</span>
                <span id="countryShow" class="seach_checkbox"></span>
              </p>
            </div>
            
            
            <div class="seach25" >
              <p style="width:106px"><span class="xing">*</span>预计出团日期：</p>
              <!--     <p class="seach_r"><input type="text" readonly="readonly" id="dgroup-out-date-id" name="dgroupOutDate" class="dateinput required" value="" onClick="WdatePicker()"></p> -->  
              <p class="seach_r"><input type="text" readonly="readonly" id="dgroup-out-date-id" name="dgroupOutDate" class="dateinput required" value="" onclick="WdatePicker({minDate:'%y-%M-%d'})"></p>
            </div>
            <div class="seach25">
              <p>出境口岸：</p>
              
              	<p class="seach_r">
	              				<select name="outAreaId">
		              					<option value="0" selected="selected">选择城市</option>
		              					<c:forEach items="${outAreaList }"  var = "city">
		              						<option value="${city.value }" >${city.label }</option>
		              					</c:forEach>
		              				</select>
		              				<input type="hidden" name="outAreaName" value=""/>
	              			</p>
            </div>
            
            <div class="seach25">
              <p>境外停留：</p>
              <p class="seach_r">
                 <input type="text" class="seach_shortinput" name="outsideDaySum" value="1"  maxlength="4"/>天，
                 <input type="text" class="seach_shortinput" name="outsideNightSum"  value="0" maxlength="4"/>晚
              </p>
            </div>
            <div class="kong" name="kong-name"></div>
            
            <!-- 询价接待要求 -->
            <div id="eprice-travel-require-div-id">
            
             </div>
            <div class="seach25 seach100"><a class="ydbz_x inquiry2AddIpt">添加要求</a></div>
            <div class="seach25 seach100">
              <p>询价要求：</p>
              <p class="seach_r"><span class="inquiry_zxtext">
              <textarea name="epriceRequirement"></textarea>
              <a class="inquiry_zxadd">添加</a></span></p> </div>
            <div class="seach25 seach100">
				<p><c:if test="${fns:getCompanyUuid() eq '049984365af44db592d1cd529f3008c3' }"><span class="xing">*</span></c:if>上传行程：</p>
				<input type="button" name="passport"  id="uploadMoreFile" class="btn btn-primary" value="上传"  onclick="uploadFiles(' ${ctx}','passportfile',this,'false');"/>
				<span id="upfileShow" class="seach_checkbox"></span>
				<span class="fileLogo"></span>
            </div>
            <div class="seach_btnbox">
              <input type="button" value="上一步" class="btn btn-primary" onclick="TwoToOne()" />
              <!-- 机票相关 -->
              <input type="button" id="eprice-b-form-submit-btn-id" value="下一步" class="btn btn-primary" />
              <!-- 临时增加提交询价
              <input type="button" id="eprice-b-form-submit-btn-id" value="提交询价" class="btn btn-primary" /> -->
            </div>
          </form>
        </div>
        
        
        <!--第三步-->
        <div id="eprice-c-div-p-id" class="messageDiv inquiry_box3">
        	<div class="seach25 seach100">
        		<!-- 
        		<input type="checkbox" id="is-app-flight-id" name="inquiry_box3_check" />
        		 -->
        		<input type="checkbox" id="is-app-flight-id" name="isAppFlight" />
          		<label for="is-app-flight-id">申请机票</label>
          	</div>
          	
          	<div class="inquiry_box3_check" id="eprice-c-div-id">
          		<form id="eprice-c-form-0-id">
	            	<div class="title_con">临时机票申请</div>
	            	<!-- 机票相关-->
		            <div class="seach25 seach100">
			              <p><span class="xing">*</span>机票计调：</p>
			              <p class="seach_r">
			              	 <span class="seach_check">
				                <select name="ticketOperator">
				                  <option value="0" selected="selected">请搜索计调</option>
				                  	<c:forEach items="${toperators }" var="au">
				                  		<option value="${au.id }">${au.name}</option>
				                  	</c:forEach>
				                </select>
			                </span>
			                <span id="ticketOperatorShow" class="seach_checkbox"></span>
			              </p>
		            </div>
	            	<div class="seach25 seach100">
	              		<span class="seach_check">
	              			<label for="inquiry_radio_flights1">
	                			<input name="trafficLineType" value="1" id="inquiry_radio_flights1"  type="radio" checked="checked" refshow="inquiry_flight1"/>往返
	                		</label>
	                	</span>
	              		<span class="seach_check">
	              			<label for="inquiry_radio_flights2">
	                			<input  name="trafficLineType" value="2"  id="inquiry_radio_flights2" type="radio" refshow="inquiry_flights2"/>单程
	                		</label>
	                	</span>
						<span class="seach_check">
							<label for="inquiry_radio_flights3">
	                			<input  name="trafficLineType" value="3"  id="inquiry_radio_flights3" type="radio" refshow="inquiry_flights3"/>多段
	                		</label>
	                	</span>
	              	</div>
	              
	            </form>
	            
	            <!-- 往返  -->
            	<div class="inquiry_flight1" name="inquiry-flights-div">
            		<form id="eprice-c-form-1-id">
	            		<div class="title_samil" name="area-type-div">去程：
	            				<span class="seach_check">
		            				<label for="area-type-id-00"><input name="areaType0" myname="areaType" id="area-type-id-00" type="radio" value="1" checked="checked" />内陆</label>
		            			</span>
		              			<span class="seach_check">
		              				<label for="area-type-id-10"><input  name="areaType0" myname="areaType" id="area-type-id-10" type="radio" value="2" />国际</label>
		              			</span>
		              			<input type="hidden" name="areaType"  value="1"/>
	            		
	            		</div><input type="hidden" name="no" value="1"/>
	            		<div class="seach25">
	              			<p><span class="xing">*</span>出发城市：</p>
	              			<p class="seach_r">
	              				<select name="startCityId" class="selectinput">
		              					<option value="0" selected="selected">选择城市</option>
		              					<c:forEach items="${fromareaslist }"  var = "city">
		              						<option value="${city.id }" >${city.label }</option>
		              					</c:forEach>
		              				</select>
		              				<input type="hidden" name="startCityName" value=""/>
	              			</p>
	            		</div>
	            		<div class="seach25">
	              			<p><span class="xing">*</span>到达城市：</p>
	              			<p class="seach_r">
	              				<select name="endCityId" class="selectinput">
		              					<option value="0" selected="selected">选择城市</option>
					                  	<c:forEach items="${airportCityList }" var="au">
					                  		<option value="${au.id }">${au.name}</option>
					                  	</c:forEach>
		              				</select>
		              				<input type="hidden" name="endCityName" value=""/>
	              			</p>
	            		</div>
	            		<div class="kong"></div>
	            		
	            		<div class="seach25">
	              			<p><span class="xing">*</span>出发日期：</p>
	              			<p class="seach_r"><input type="text"   readonly="readonly" name="startDate"  class="dateinput required" value="" onclick="WdatePicker({minDate:'%y-%M-%d'})" /></p>
	            		</div>
	            		<div class="seach25">
	              			<p>出发时刻：</p>
	             			<p class="seach_r">
	             				<select name="startTimeType">
	                				<option value="1">早</option>
	                				<option value="2">中</option>
	                				<option value="3">晚</option>
	              				</select>
	              			</p>
	            		</div>
	            		<div class="seach25 seach50">
			            	<p>时间区间：</p>
			              	<p class="seach_r">
			              		<select class="seach_shortselect" name="startTime1">
					                
			              		</select>至 
			              		<select  class="seach_shortselect" name="startTime2">
			                		
			              		</select>
			              	</p>
			            </div>
	            		<div class="kong"></div>
	            		
	            		<div class="seach25">
	              			<p>舱位等级：</p>
	              			<p class="seach_r">
	              				<select name="aircraftSpaceLevel" >
	              						<option value="0">不限</option>
						                <option value="1">头等</option>
						                <option value="2">公务</option>
						                <option value="3">经济</option>
		              			</select>
	              			</p>
	            		</div>
	            		<div class="seach25">
	              			<p>舱位：</p>
	              			<p class="seach_r">
	              				<select name="aircraftSpace" >
	              					<option value="0">不限</option>
					                <option value="Y">Y舱</option>
					                <option value="K">K舱</option>
				              	</select>
	              			</p>
	            		</div>
	            		<div class="kong"></div>
	            		
	            		<div class="title_samil" name="area-type-div">返程：
	            				<span class="seach_check">
		            				<label for="area-type-id-01"><input name="areaType1" myname="areaType" id="area-type-id-01" type="radio" value="1" checked="checked" />内陆</label>
		            			</span>
		              			<span class="seach_check">
		              				<label for="area-type-id-11"><input  name="areaType1" myname="areaType" id="area-type-id-11" type="radio" value="2" />国际</label>
		              			</span>
		              			<input type="hidden" name="areaType"  value="1"/>
	            		</div><input type="hidden" name="no" value="2"/>
	            		<div class="seach25">
		              			<p><span class="xing">*</span>出发城市：</p>
		              			<p class="seach_r">
		              				<select name="startCityId" class="selectinput">
		              					<option value="0" selected="selected">选择城市</option>
		              					<c:forEach items="${airportCityList }"  var = "city">
		              						<option value="${city.id }" >${city.name }</option>
		              					</c:forEach>
		              				</select>
		              				<input type="hidden" name="startCityName" value=""/>
		              			</p>
		            		</div>
		            		<div class="seach25">
		              			<p><span class="xing">*</span>到达城市：</p>
		              			<p class="seach_r">
		              				<select name="endCityId" class="selectinput">
		              					<option value="0" selected="selected">选择城市</option>
					                  	<c:forEach items="${fromareaslist }" var="au">
					                  		<option value="${au.id }">${au.label}</option>
					                  	</c:forEach>
		              				</select>
		              				<input type="hidden" name="endCityName" value=""/>
		              			</p>
		            		</div>
	            		<div class="kong"></div>
	            		
	            		<div class="seach25">
	              			<p><span class="xing">*</span>出发日期：</p>
	              			<p class="seach_r"><input type="text" class="dateinput" readonly="readonly" name="startDate" onclick="WdatePicker({minDate:'%y-%M-%d'})" /></p>
	            		</div>
	            		<div class="seach25">
	              			<p>出发时刻：</p>
	              			<p class="seach_r">
	              				<select name="startTimeType" >
	                				<option value="1">早</option>
	                				<option value="2">中</option>
	                				<option value="3">晚</option>
	              				</select>
	              			</p>
	            		</div>
	            		<div class="seach25 seach50">
			              	<p>时间区间：</p>
			              	<p class="seach_r">
			              		<select class="seach_shortselect" name="startTime1" >
					               
			              		</select> 至 <select  class="seach_shortselect" name="startTime2">
					                
			              		</select>
			              	</p>
			            </div>
	            		<div class="kong"></div>
	            		
	            		<div class="seach25">
	              			<p>舱位等级：</p>
	              			<p class="seach_r">
	              				<select name="aircraftSpaceLevel" >
	              						<option value="0">不限</option>
						                <option value="1">头等</option>
						                <option value="2">公务</option>
						                <option value="3">经济</option>
		              			</select>
	              			</p>
	            		</div>
	            		<div class="seach25">
	              			<p>舱位：</p>
	             			<p class="seach_r">
								<select name="aircraftSpace" >
										<option value="0">不限</option>
						                <option value="Y">Y舱</option>
						                <option value="K">K舱</option>
					            </select>
	              			</p>
	            		</div>
            		</form>
            	</div>
            	
            	<!-- 单程  -->
            	<div class="inquiry_flights2" name="inquiry-flights-div">
            		<form id="eprice-c-form-2-id">
            		<div class="title_samil" name="area-type-div">第一段：
	            				<span class="seach_check">
		            				<label for="area-type-id-00"><input name="areaType0" myname="areaType" id="area-type-id-00" type="radio" value="1" checked="checked" />内陆</label>
		            			</span>
		              			<span class="seach_check">
		              				<label for="area-type-id-10"><input  name="areaType0" myname="areaType" id="area-type-id-10" type="radio" value="2" />国际</label>
		              			</span>
		              			<input type="hidden" name="areaType"  value="1"/>
	            		
	            		</div>
            		<input type="hidden" name="no" value="1"/>
	             		
		            	<div class="seach25">
		              			<p><span class="xing">*</span>出发城市：</p>
		              			<p class="seach_r">
		              				<select name="startCityId" class="selectinput">
		              					<option value="0" selected="selected">选择城市</option>
		              					<c:forEach items="${fromareaslist }"  var = "city">
		              						<option value="${city.id }" >${city.label }</option>
		              					</c:forEach>
		              				</select>
		              				<input type="hidden" name="startCityName" value=""/>
		              			</p>
		            		</div>
		            		<div class="seach25">
		              			<p><span class="xing">*</span>到达城市：</p>
		              			<p class="seach_r">
		              				<select name="endCityId" class="selectinput">
		              					<option value="0" selected="selected">选择城市</option>
					                  	<c:forEach items="${airportCityList }" var="au">
					                  		<option value="${au.id }">${au.name}</option>
					                  	</c:forEach>
		              				</select>
		              				<input type="hidden" name="endCityName" value=""/>
		              			</p>
		            		</div>
		            	<div class="kong"></div>
		            	
		            	<div class="seach25">
		              		<p><span class="xing">*</span>出发日期：</p>
		              		<p class="seach_r"><input type="text" class="dateinput" readonly="readonly" name="startDate" onclick="WdatePicker({minDate:'%y-%M-%d'})" /></p>
		            	</div>
		            	<div class="seach25">
		              		<p>出发时刻：</p>
		              		<p class="seach_r">
		              			<select name="startTimeType">
	                				<option value="1">早</option>
	                				<option value="2">中</option>
	                				<option value="3">晚</option>
	              				</select>
		              		</p>
		            	</div>
		             	<div class="seach25 seach50">
		              		<p>时间区间：</p>
		              		<p class="seach_r">
		              			<select class="seach_shortselect" name="startTime1">
					                
		              			</select> 至 <select  class="seach_shortselect" name="startTime2">
					                
		              			</select>
		              		</p>
		            	</div>
		            	<div class="kong"></div>
		            	
		            	<div class="seach25">
		              		<p>舱位等级：</p>
		             		<p class="seach_r">
		             			<select name="aircraftSpaceLevel" >
		             					<option value="0">不限</option>
						                <option value="1">头等</option>
						                <option value="2">公务</option>
						                <option value="3">经济</option>
		              			</select>
		              		</p>
		            	</div>
		            	<div class="seach25">
		              		<p>舱位：</p>
		              		<p class="seach_r">
		              			<select name="aircraftSpace" >
		              					<option value="0">不限</option>
						                <option value="Y">Y舱</option>
						                <option value="K">K舱</option>
					            </select>
		              		</p>
		            	</div>
	            	</form>
            	</div>
            	
            	<!-- 多段 -->
            	<div class="inquiry_flights3" name="inquiry-flights-div">
            		<form id="eprice-c-form-3-id">
	            		<p class="main-right-topbutt"><a style="cursor: pointer;" id="add-traffic-line-id">新增航段</a></p>
	            		<div name="traffic-line-div-name">
		            		<div class="title_samil" name="area-type-div"><span name="traffic-line-no">第一段：</span><input type="hidden" name="no" value="0"/>
		            			<span class="seach_check">
		            				<label for="area-type-id-00"><input name="areaType0" myname="areaType" id="area-type-id-00" type="radio" value="1" checked="checked" />内陆</label>
		            			</span>
		              			<span class="seach_check">
		              				<label for="area-type-id-10"><input  name="areaType0" myname="areaType" id="area-type-id-10" type="radio" value="2" />国际</label>
		              			</span>
		              			<input type="hidden" name="areaType"  value="1"/>
		              		</div>
		            		<div class="seach25">
		              			<p><span class="xing">*</span>出发城市：</p>
		              			<p class="seach_r">
		              				<select name="startCityId" class="selectinput">
		              					<option value="0" selected="selected">选择城市</option>
		              					<c:forEach items="${fromareaslist }"  var = "city">
		              						<option value="${city.id }" >${city.label }</option>
		              					</c:forEach>
		              				</select>
		              				<input type="hidden" name="startCityName" value=""/>
		              			</p>
		            		</div>
		            		<div class="seach25">
		              			<p><span class="xing">*</span>到达城市：</p>
		              			<p class="seach_r">
		              				<select name="endCityId" class="selectinput">
		              					<option value="0" selected="selected">选择城市</option>
					                  	<c:forEach items="${airportCityList }" var="au">
					                  		<option value="${au.id }">${au.name}</option>
					                  	</c:forEach>
		              				</select>
		              				<input type="hidden" name="endCityName" value=""/>
		              			</p>
		            		</div>
		            		<div class="seach25">
		              			<p>人数：</p>
		              			<p class="seach_r"><input type="text"  name="lineAllPersonSum" value="1"></p>
		            		</div>
		            		<div class="kong"></div>
		            		<div class="seach25">
		              			<p><span class="xing">*</span>出发日期：</p>
		              			<p class="seach_r"><input type="text" class="dateinput" readonly="readonly" name="startDate"  onclick="WdatePicker({minDate:'%y-%M-%d'})" /></p>
		            		</div>
		            		<div class="seach25">
		              			<p>出发时刻：</p>
		              			<p class="seach_r">
		              				<select name="startTimeType">
		                				<option value="1">早</option>
		                				<option value="2">中</option>
		                				<option value="3">晚</option>
		              				</select>
		              			</p>
		            		</div>
		             		<div class="seach25 seach50">
		              			<p>时间区间：</p>
		              			<p class="seach_r">
		              				<select class="seach_shortselect" name="startTime1">
						               
		              				</select> 至 <select  class="seach_shortselect" name="startTime2">
						                
		              				</select>
		              			</p>
		            		</div>
		            		<div class="kong"></div>
		            		
		            		<div class="seach25">
		              			<p>舱位等级：</p>
		             			<p class="seach_r">
		             				<select name="aircraftSpaceLevel" >
		             					<option value="0">不限</option>
						                <option value="1">头等</option>
						                <option value="2">公务</option>
						                <option value="3">经济</option>
		              				</select>
		              			</p>
		            		</div>
		            		<div class="seach25">
					              <p>舱位：</p>
					              <p class="seach_r">
					              	<select name="aircraftSpace" >
					              		<option value="0">不限</option>
						                <option value="Y">Y舱</option>
						                <option value="K">K舱</option>
					              	</select>
					              </p>
		            		</div>
	            		</div>
	            		
	            		<div id="traffic-line-2-div-id" name="traffic-line-div-name">
		              		<div class="title_samil" name="area-type-div"><span name="traffic-line-no">第二段：</span><input type="hidden" name="no" value="1"/>
		              			<span class="seach_check" name="area-type-span">
		              				<label for="area-type-id-01"><input name="areaType1" myname="areaType" id="area-type-id-01" type="radio" value="1" checked="checked" />内陆</label>
		              			</span>
		              			<span class="seach_check" name="area-type-span">
		              				<label for="area-type-id-11"><input  name="areaType1" myname="areaType" id="area-type-id-11" value="2" type="radio"/>国际</label>
		              			</span>
		              			<input type="hidden" name="areaType"  value="1"/>
		              			<p class="main-right-topbutt" style="display: none;"><a style="cursor: pointer;" name="del-traffic-line-name" class="gray">删除航段</a></p>
		              		</div>
		            		<div class="seach25">
		              			<p><span class="xing">*</span>出发城市：</p>
		              			<p class="seach_r">
		              				<select name="startCityId" class="selectinput">
		              					<option value="0" selected="selected">选择城市</option>
					                  	<c:forEach items="${airportCityList }" var="au">
					                  		<option value="${au.id }">${au.name}</option>
					                  	</c:forEach>
		              				</select>
		              				<input type="hidden" name="startCityName" value=""/>
		              				<!-- <input type="text" id="flight1-start-city-id" name="startCityName" /><input type="hidden"  name="startCityId" /> -->
		              			</p>
		            		</div>
		            		<div class="seach25">
		              			<p><span class="xing">*</span>到达城市：</p>
		              			<p class="seach_r">
		              				<select name="endCityId" class="selectinput">
		              					<option value="0" selected="selected">选择城市</option>
					                  	<c:forEach items="${airportCityList }" var="au">
					                  		<option value="${au.id }">${au.name}</option>
					                  	</c:forEach>
		              				</select>
		              				<input type="hidden" name="endCityName" value=""/>
		              			<!-- <input type="text" id="flight1-end-city-id" name="endCityName" /><input type="hidden"  name="endCityId" /> -->
		              			</p>
		            		</div>
		            		<div class="seach25">
		              			<p>人数：</p>
		              			<p class="seach_r"><input type="text" name="lineAllPersonSum" value="1"></p>
		            		</div>
		            		<div class="kong"></div>
		            		
		            		
		            		<div class="seach25">
		              			<p><span class="xing">*</span>出发日期：</p>
		              			<p class="seach_r"><input type="text" class="dateinput" readonly="readonly" name="startDate"  onclick="WdatePicker({minDate:'%y-%M-%d'})" /></p>
		            		</div>
		            		<div class="seach25">
		              			<p>出发时刻：</p>
		              			<p class="seach_r">
		              				<select name="startTimeType">
		                				<option value="1">早</option>
		                				<option value="2">中</option>
		                				<option value="3">晚</option>
		              				</select>
		              			</p>
		            		</div>
		             		<div class="seach25 seach50">
				              <p>时间区间：</p>
				              <p class="seach_r">
				              	<select class="seach_shortselect" name="startTime1">
					                
				              	</select> 至 <select  class="seach_shortselect" name="startTime2">
					                
				              	</select></p>
		           	 		</div>
		            		<div class="kong"></div>
		            		
		            		<div class="seach25">
		              			<p>舱位等级：</p>
		             			<p class="seach_r">
		             				<select name="aircraftSpaceLevel" >
		             					<option value="0">不限</option>
						                <option value="1">头等</option>
						                <option value="2">公务</option>
						                <option value="3">经济</option>
		              				</select>
		              			</p>
		            		</div>
		            		<div class="seach25">
		              			<p>舱位：</p>
		              			<p class="seach_r">
		              				<select name="aircraftSpace">
		              					<option value="0">不限</option>
						                <option value="Y">Y舱</option>
						                <option value="K">K舱</option>
		              				</select>
		              			</p>
		            		</div>
	            		</div>
	            		
	            		
            		</form>
            	</div>
            	<div class="kong"></div>
            	
            	<form id="eprice-c-form-4-id">
	            	<div class="title_con">申请基本信息</div>
	            	<div class="seach25">
	              		<p class="seachlongp"><span class="xing">*</span>机票申请总人数：</p>
	              		<input type="text"   id="traffic_allPersonSum" name="allPersonSum" value="1" readonly = "readonly"/>
	            	</div>
	            	<div class="seach25">
	              		<p>成人人数：</p>
	              		<input type="text"  id="traffic_adultSum" name="adultSum" onkeyup="value=value.replace(/[^\d]/g,'')"  value="1" class="blurSum"/>
	            	</div>
	           		<div class="seach25 pr jd-xs">
	              		<p>儿童人数：</p>
	              		<input type="text" flag="istips"  id="traffic_childSum" name="childSum" onkeyup="value=value.replace(/[^\d]/g,'')"  title="12岁以内" class="blurSum"/>
	                </div>
	            	<div class="seach25 pr jd-xs">
	              		<p>特殊人群人数：</p>
	              		<input type="text"  id="traffic_specialPersonSum" name="specialPersonSum" onkeyup="value=value.replace(/[^\d]/g,'')"  value="0" class="blurSum"/>
	            	</div>
	            	<div class="kong"></div>
	            	
	            	<div class="seach25 seach100">
	              		<p>特殊要求：</p>
	              		<p class="seach_r"><textarea name="specialDescn"></textarea></p>
	            	</div>
            	</form>
          </div>
          <div class="seach_btnbox">
          	<input type="button" value="取消" class="btn btn-primary gray" name="history-back" />
          	<input type="button" value="上一步" class="btn btn-primary" onclick="ThreeToTwo()">
          	<input type="button" value="提交询价" class="btn btn-primary" id="eprice-c-form-submit-btn-id">
          </div>
        </div>
             <!-- 新增航段模板开始 --> 
       <div id="trafficTemplate" style="display:none">
        <div name="traffic-line-div-name">
           		<div class="title_samil" name="area-type-div"><span name="traffic-line-no">第二段：</span><input type="hidden" name="no" value="1"/>
           			<span class="seach_check" name="area-type-span">
           				<label for="area-type-id-001"><input name="areaType01" myname="areaType" id="area-type-id-001" type="radio" value="1" checked="checked" />内陆</label>
           			</span>
           			<span class="seach_check" name="area-type-span">
           				<label for="area-type-id-011"><input  name="areaType01" myname="areaType" id="area-type-id-011" value="2" type="radio"/>国际</label>
           			</span>
           			<input type="hidden" name="areaType"  value="1"/>
           			<p class="main-right-topbutt"><a style="cursor: pointer;" name="del-traffic-line-name"  class="gray">删除航段</a></p>
           		</div>
         		<div class="seach25">
           			<p><span class="xing">*</span>出发城市：</p>
           			<p class="seach_r">
           				<select name="startCityId" class="selectinput">
           					<option value="0" selected="selected">选择城市</option>
                  	<c:forEach items="${airportCityList }" var="au">
                  		<option value="${au.id }">${au.name}</option>
                  	</c:forEach>
           				</select>
           				<input type="hidden" name="startCityName" value=""/>
           				<!-- <input type="text" id="flight1-start-city-id" name="startCityName" /><input type="hidden"  name="startCityId" /> -->
           			</p>
         		</div>
         		<div class="seach25">
           			<p><span class="xing">*</span>到达城市：</p>
           			<p class="seach_r">
           				<select name="endCityId"  class="selectinput">
           					<option value="0" selected="selected">选择城市</option>
                  	<c:forEach items="${airportCityList }" var="au">
                  		<option value="${au.id }">${au.name}</option>
                  	</c:forEach>
           				</select>
           				<input type="hidden" name="endCityName" value=""/>
           			<!-- <input type="text" id="flight1-end-city-id" name="endCityName" /><input type="hidden"  name="endCityId" /> -->
           			</p>
         		</div>
         		<div class="kong"></div>
         		
         		
         		<div class="seach25">
           			<p>出发日期：</p>
           			<p class="seach_r"><input type="text" class="dateinput" readonly="readonly" name="startDate"  onclick="WdatePicker({minDate:'%y-%M-%d'})"  /></p>
         		</div>
         		<div class="seach25">
           			<p>出发时刻：</p>
           			<p class="seach_r">
           				<select name="startTimeType">
             				<option value="1">早</option>
             				<option value="2">中</option>
             				<option value="3">晚</option>
           				</select>
           			</p>
         		</div>
          		<div class="seach25 seach50">
             <p>时间区间：</p>
             <p class="seach_r">
             	<select class="seach_shortselect" name="startTime1">
                
             	</select> 至 <select  class="seach_shortselect" name="startTime2">
                
             	</select></p>
        	 		</div>
         		<div class="kong"></div>
         		
         		<div class="seach25">
           			<p>舱位等级：</p>
          			<p class="seach_r">
          				<select name="aircraftSpaceLevel" >
          					<option value="0">不限</option>
	                <option value="1">头等</option>
	                <option value="2">公务</option>
	                <option value="3">经济</option>
           				</select>
           			</p>
         		</div>
         		<div class="seach25">
           			<p>舱位：</p>
           			<p class="seach_r">
           				<select name="aircraftSpace">
           					<option value="0">不限</option>
	                <option value="Y">Y舱</option>
	                <option value="K">K舱</option>
           				</select>
           			</p>
         		</div>
        		</div>
        </div>
        <!-- 新增航段模板结束 --> 
      <!--右侧内容部分结束--> 
      
   
	
</body>
</html>