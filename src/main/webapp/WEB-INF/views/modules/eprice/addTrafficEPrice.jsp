<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
	<title>新增机票询价</title>
	<meta name="decorator" content="wholesaler"/>
	<%@ include file="commonVar.jsp"%>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/addTrafficEPrice.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/eprice/epriceCommon.js" type="text/javascript"></script>
	
		<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
    <link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
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
		
		inquiryCheckBOX();
		$("#eprice-c-div-id").find(".selectinput" ).comboboxInquiry({
			"afterInvalid":function(event,data){
				$(this).trigger("change");
			}
		});
		$("#customerAgentId").comboboxInquiry().on('comboboxinquiryselect', function (event, obj) {
			fillContactByAgent(this);
		});
		$("#ticketOperator").comboboxInquiry();
	});
	</script>
	<style type="text/css">
		.custom-combobox .custom-combobox-input, .produceDiv .seach_r .custom-combobox {
			width:80px;
		}
	</style>
</head>
<body>
	<page:applyDecorator name="eprice_traffic_create" >
    </page:applyDecorator>
      <!--右侧内容部分开始-->
        <input type="hidden" name="onceAgain" id="id_onceAgain" value="0"/>
           <input type="hidden" name="projectId" id="id_projectId"  />
        
      <div class="inquiry_num inquiry_num_ticket"></div>
        <!--第一步-->
        <div class="ydbz_tit">
			<span class="ydExpand closeOrExpand"></span>基本信息
		</div>
        <div class="messageDiv inquiry_box1" flag="messageDiv" style="width:1040px">
          <form id="eprice-a-form-id">
          	<input type="hidden" name="type" value="${type}"/>
          	<input type="hidden" name="emode" value="${emode}"/>
            <!-- 机票相关
            <div class="seach25 seach100">
              <p>机票计调：</p>
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
            </div>-->
            
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
            
            <div class="seach25 seach100">
              <p>询价客户类型：</p>
              <p class="seach_r" >
              	<label for="inquiry_radio_people">
              		<input name="customerType" refshow="inquiry_radio_people1" id="customer-type-radio-id-1" type="radio" checked="checked"  value="1">直客
              	</label>
              	<label for="inquiry_radio_people2">
              		<input name="customerType" refshow="inquiry_radio_people2" id="customer-type-radio-id-2" type="radio" value="2" >同行
              	</label>
              	 <input type="hidden" name="teamType" value="${teamType}"/>
              	<!-- 
              	<label for="inquiry_radio_people3">
              		<input name="customerType" refshow="inquiry_radio_people3"  id="customer-type-radio-id-3" type="radio"  value="0">其它
              	</label> -->
              </p>
           </div>
           
           <!--询价客户类型  直客选择触发  -->
           <div class="inquiry_radio_people1" name="inquiry_radio_people">
           		<div class="seach25">
		              <p><span class="xing">*</span>询价客户：</p>
		              <p class="seach_r"><input type="text"  name="customerName" maxlength="16"/></p>
            	</div>
            	<div class="seach25">
		              <p><span class="xing">*</span>联系人：</p>
		              <p class="seach_r"><input type="text" name="contactPerson"  maxlength="16"/></p>
            	</div>
	            <div class="seach25">
		              <p><span class="xing">*</span>电话：</p>
		              <p class="seach_r"><input type="text"  name="contactMobile" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" maxlength="16"/></p>
	            </div>
	            <div class="seach25">
		              <p>其他联系方式：</p>
		              <p class="seach_r"><input type="text" name="otherContactWay"  maxlength="32"  value=""/></p>
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
		              <p class="seach_r"><input id="togetherman"   type="text" name="contactPersontogether" maxlength="16"/></p>
	            </div>
	            <div class="seach25">
		              <p><span class="xing">*</span>电话：</p>
		              <p class="seach_r"><input id="togetherphone"  type="text" name="contactMobiletogether" maxlength="16"/></p>
	            </div>
	            <div class="seach25">
		              <p>其他联系方式：</p>
		              <p class="seach_r"><input type="text"  id="togetherOtherContactWay"  name="otherContactWay"  maxlength="32" value="" /></p>
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
	              <p class="seach_r"><input type="text" id="base_allPersonSum" name="allPersonSum" value="1" maxlength="4"></p>
            </div>
              <div class="seach25">
       	       <p  class="seach_r" style="float:left;"><a id="expand_detailsSum" name="expand_detailsSum"  href="javascript:void(0)">展开</a></p>
            </div>
            
             <div class="kong"></div>
            <div id="ifShow_detailsSum" style="display:none">
	            <div class="seach25">
	              <p>成人人数：</p>
	              <p class="seach_r"><input type="text"  id="base_adultSum"  name="adultSum" value="1" maxlength="4"></p>
	            </div>
	            <div class="seach25 pr jd-xs">
	              <p>儿童人数：</p>
	              <p class="seach_r"><input type="text"  id="base_childSum" name="childSum" title="12岁以内" flag="istips" maxlength="4"/></p>
	            </div>
	            <div class="seach25">
	              <p>特殊人群：</p>
	              <p class="seach_r"> <input type="text"  id="base_specialPersonSum"  name="specialPersonSum" value="0" maxlength="4"></p>
	            </div>
		              <div class="seach25"  >
		              <p style="width:106px">特殊人群说明：</p>
		              <p class="seach_r"> <input type="text"  id="base_specialRemark" name="specialRemark" value="" maxlength="30"></p>
		            </div>
           		 <div class="kong"></div>
           		</div>
            <div class="seach_btnbox">
              <input type="button" id="eprice-a-form-submit-btn-id"  value="下一步" class="btn btn-primary" />
            </div>
          </form>
        </div>
        <!--第二步-->
       
        
        
        <!--第三步-->
        <div id="eprice-c-div-p-id" class="messageDiv inquiry_box3">
        	<div class="seach25 seach100" style="display: none">
        		<!-- 
        		<input type="checkbox" id="is-app-flight-id" name="inquiry_box3_check" />
        		 -->
        		<input   type="checkbox" id="is-app-flight-id" name="isAppFlight" checked="checked"/>
          		<label for="is-app-flight-id">申请机票</label>
          	</div>
          	
          	<div class="inquiry_box3_check" id="eprice-c-div-id">
          		<form id="eprice-c-form-0-id">
	            	<div class="title_con">临时机票申请</div>
	            	<!-- 机票相关-->
		            <div class="seach25 seach100">
			              <p><span class="xing">*</span>机票计调：</p>
			              <p class="seach_r">
			              	 <select name="ticketOperator" id="ticketOperator">
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
	            		<div class="seach25" style="min-width:240px">
	              			<p><span class="xing">*</span>出发城市：</p>
	              			<p class="seach_r">
	              				<select name="startCityId" class="selectinput">
		              					<option value="0">选择城市</option>
		              					<c:forEach items="${fromareaslist }"  var = "city">
		              						<option value="${city.id }" >${city.label }</option>
		              					</c:forEach>
		              				</select>
		              				<input type="hidden" name="startCityName" value=""/>
	              			</p>
	            		</div>
	            		<div class="seach25"  style="min-width:240px">
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
	            		<div class="seach25 seach50" >
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
	            		<div class="seach25"  style="min-width:240px">
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
		            		<div class="seach25"  style="min-width:240px">
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
	              			<p class="seach_r"><input type="text" class="dateinput" readonly="readonly" name="startDate"  onclick="WdatePicker({minDate:'%y-%M-%d'})" /></p>
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
            		<input type="hidden" name="no" value="1"/>
	             			<div class="title_samil" name="area-type-div">第一段：
	            				<span class="seach_check">
		            				<label for="area-type-id-00"><input name="areaType0" myname="areaType" id="area-type-id-00" type="radio" value="1" checked="checked" />内陆</label>
		            			</span>
		              			<span class="seach_check">
		              				<label for="area-type-id-10"><input  name="areaType0" myname="areaType" id="area-type-id-10" type="radio" value="2" />国际</label>
		              			</span>
		              			<input type="hidden" name="areaType"  value="1"/>
	            		
	            		</div>
		            	<div class="seach25"  style="min-width:240px">
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
		            		<div class="seach25"  style="min-width:240px">
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
		            		<div class="seach25"  style="min-width:240px">
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
		            		<div class="seach25" style="min-width:240px">
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
		             		<div class="seach25  seach50">
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
		              			<p class="main-right-topbutt" style="display: none;"><a style="cursor: pointer;" name="del-traffic-line-name"  class="gray">删除航段</a></p>
		              		</div>
		            		<div class="seach25" style="min-width:240px">
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
		            		<div class="seach25"  style="min-width:240px">
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
	            		
	            		
            		</form>
            	</div>
            	<div class="kong"></div>
            	
            	<form id="eprice-c-form-4-id">
	            	<div class="title_con">申请基本信息</div>
	            	<div class="seach25">
	              		<p class="seachlongp" style="margin-left: -22px;"><span class="xing">*</span>机票申请总人数：</p>
	              		<input type="text"  readonly="readonly" id="traffic_allPersonSum" name="allPersonSum" value="1"/>
	            	</div>
	            	<div class="seach25">
	              		<p>成人人数：</p>
	              		<input type="text" readonly="readonly"  id="traffic_adultSum" name="adultSum" value="1"/>
	            	</div>
	           		<div class="seach25">
	              		<p>儿童人数：</p>
	              		<input type="text"  readonly="readonly"  id="traffic_childSum"   flag="istips" name="childSum"  />
	                </div> 
	            	<div class="seach25">
	              		<p>特殊人群人数：</p>
	              		<input type="text"   readonly="readonly" id="traffic_specialPersonSum"  name="specialPersonSum" value="0"/>
	            	</div>
	            	<div class="kong"></div>
	            	
	            	<div class="seach25 seach100">
	              		<p>特殊要求：</p>
	              		<p class="seach_r"><textarea id="traffic_specialDescn" name="specialDescn"></textarea></p>
	            	</div>
            	</form>
          </div>
          <div class="seach_btnbox">
          	<input type="button" value="取消" class="btn btn-primary gray" name="history-back" />
          	<input type="button" value="上一步" class="btn btn-primary" onclick="ThreeToOne()">
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
         		<div class="seach25" style="min-width:240px">
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
         		<div class="seach25" style="min-width:240px">
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