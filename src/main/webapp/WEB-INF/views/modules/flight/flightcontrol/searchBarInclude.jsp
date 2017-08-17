<%@ page contentType="text/html;charset=UTF-8"%>
<script type="text/javascript">
$(function(){
	initSuggest({});
	
	showSearchPanel();
	//岛屿、酒店的默认选中方法
    getAjaxSelect('island',$('#country'),"${flightControlQuery.islandUuid}");
   // 舱位等级的默认选中
    selectairline($('#trafficName').val(),$('#trafficName'),"${flightControlQuery.spaceGradeType}");
   
});
//有查询条件的时候，DIV不隐藏
function showSearchPanel(){
	var startDepartureDate = "${flightControlQuery.startDepartureDate}";//开始日期
	var endDepartureDate = "${flightControlQuery.endDepartureDate}";//结束日期
	var name = "${flightControlQuery.name}";//控票名称
	var islandUuid = "${flightControlQuery.islandUuid}";//岛屿名称
	var airline = "${flightControlQuery.airline}";//航空公司
	var spaceGradeType = "${flightControlQuery.spaceGradeType}";//舱位等级
	var startPrice = "${flightControlQuery.startPrice}";//最低价格
	var endPrice = "${flightControlQuery.endPrice}";//最高价格
	var startStock = "${flightControlQuery.startStock}";//最少库存
	var endStock = "${flightControlQuery.endStock}";//最多库存
	var hotelUuid = "${flightControlQuery.hotelUuid}";//参考酒店
	var status = "${flightControlQuery.status}";//状态
	var createUser = "${flightControlQuery.createUser}";//发布人
	var updateUser = "${flightControlQuery.updateUser}";//更新人

	if(isNotEmpty(startDepartureDate) || isNotEmpty(endDepartureDate) || isNotEmpty(name) || isNotEmpty(islandUuid)|| isNotEmpty(airline)
			|| isNotEmpty(spaceGradeType)|| isNotEmpty(startPrice)|| isNotEmpty(endPrice)||isNotEmpty(startStock) 
			|| isNotEmpty(endStock)|| isNotEmpty(hotelUuid)|| isNotEmpty(status)|| isNotEmpty(createUser)|| isNotEmpty(updateUser)){
		$('.zksx').click();
	}
}
// 判定不为空值
function isNotEmpty(str){
	if(str != ""&&str !="0"&&str !=null){
		return true;
	}
	return false;
} 

//级联查询
//type：下级 级联对象名字 ；obj：当前控件对象；value 下级选中默认值
function getAjaxSelect(type,obj,value){
	$.ajax({
		type: "POST",
	   	url: "${ctx}/hotelControl/ajaxCheck",
	   	data: {
				"type":type,
				"uuid":$(obj).val()
			  },
		dataType: "json",
	   	success: function(data){
	   		if(type=="island"){
		   		$("#islandUuid").empty();
		   		$("#hotel").empty();
		   		$("#islandUuid").append("<option value=''>不限</option>");
		   		$("#hotel").append("<option value=''>不限</option>");
	   		}else if(type=="hotel"){
	   			$("#hotel").empty();
	   			$("#hotel").append("<option value=''>不限</option>");
	   		}
	   		if(data){
	   			if(type=="hotel"){
	   				$.each(data.hotelList,function(i,n){
	   					$("#hotel").append($("<option/>").text(n.nameCn).attr("value",n.uuid));
	   				});
	   				 if(value){
	   					$("#hotel").val(value);	   					
	   				} 
	   			}else if(type=="island"){
	   				$.each(data.islandList,function(i,n){
	   					$("#islandUuid").append($("<option/>").text(n.islandName).attr("value",n.uuid));
	   				});
	   				  if(value){
	   					$("#islandUuid").val(value);
		   				getAjaxSelect('hotel',$("#islandUuid"),"${flightControlQuery.hotelUuid}");
	   				} 
	   			}
	   		}
	   	}
	});
}

//航空公司级联及默认选中舱位等级  airlineCode：航空公司code，obj，value：舱位等级的值
 function selectairline(airlineCode,obj,value){
   $.ajax( {
		type : "POST",
		url :"${ctx}/airTicket/getspaceLevelList.htm",
		data : {
			airlineCode : airlineCode
		},
		success : function(msg) {
			var dataObj = eval('(' + msg + ')'); 
			$("#spaceGradeType").empty();  
            $("#spaceGradeType").append("<option value=''>不限</option>");  
            $.each(dataObj, function(key, value) {  
                $("#spaceGradeType").append("<option value=" + key + ">" + value+ "</option>");  
            });  
            if(value){
            	$("#spaceGradeType").val(value);
            }
		}
	}); 
}
</script>
<form:form id="searchForm" action="${ctx}/flightControl/flightControlList?showType=${showType}&activityStatus=${activityStatus}" method="post" modelAttribute="flightControlQuery" >
	<input id="activityStatus" name="activityStatus" value="${activityStatus}" type="hidden"> 
	<input id="pageNo" name="pageNo" value="${pageNo}" type="hidden"> 
	<input id="pageSize" name="pageSize" value="${pageSize}" type="hidden">
	<input id="orderBy" name="orderBy" value="${orderBy}" type="hidden">
	<input id="showType" name="showType" value="${showType}" type="hidden">
	<div class="activitylist_bodyer_right_team_co">
		<div class="activitylist_bodyer_right_team_co2">
			<%--<label>国家：</label>--%>
			<trekiz:suggest name="country" style="width:150px;border-radius: 4px 0 0 4px;width: 315px;"
				defaultValue="${flightControlQuery.country}"  callback="getAjaxSelect('island',$('#country'))"
				displayValue="${countryName}" 
				ajaxUrl="${ctx}/geography/getAllConListAjax" />
		</div>
		<a class="zksx">筛选</a>
		<div class="form_submit">
			<input class="btn btn-primary" value="搜索" type="submit">
			<input class="btn " value="清空所有条件" type="button" onclick="resetSearchParams()">
		</div>
		<p class="main-right-topbutt">
			<a class="primary" target="_blank" href="${ctx }/flightControl/tosaveflightcontrol">新增机票库存</a>
		</p>
		 <%--<span class="conditionalReset"--%>
			<%--onclick="resetSearchParams()">条件重置</span>--%>
		<div class="ydxbd">
			<span></span>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">岛屿名称：</label>
				<div class="selectStyle">
					<select name="islandUuid" id="islandUuid"
						onchange="getAjaxSelect('hotel',this);">
						<option value="" selected="selected">不限</option>
					</select>
				</div>
			</div>
			
			<div class="activitylist_bodyer_right_team_co1">
				<lable class="activitylist_team_co3_text">参考酒店：</lable>
				<div class="selectStyle">
					<select name="hotelUuid" id="hotel">
						<option value="" >不限</option>
					</select>
				</div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<!-- <div class="activitylist_team_co3_text">航空公司：</div>
				 <form:select name="airline" id="trafficName" path="airline"  onchange="selectairline(this.options[this.options.selectedIndex].value,this);"   >      
	               <form:option value="" >不限</form:option>
	               <form:options items="${traffic_namelist}" itemValue="airlineCode" itemLabel="airlineName"/>
	            </form:select>
				-->
			 
			   <label class="activitylist_team_co3_text">航空公司：</label>
				<div class="selectStyle">
					<form:select name="airline" id="trafficName" path="airline"  onchange="selectairline(this.options[this.options.selectedIndex].value,this);"   >
					   <form:option value="" >不限</form:option>
					   <form:options items="${traffic_namelist}" itemValue="airlineCode" itemLabel="airlineName"/>
					</form:select>
				</div>
				
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">舱位等级：</label>
				<div class="selectStyle">
					<select class="sel-w1" name="spaceGradeType" id="spaceGradeType">
						<option value="" selected="">不限</option>
					</select>
				</div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">日期：</label>
				<input type="text" id="groupOpenDate" class="inputTxt dateinput"
					name="startDepartureDate"
					value='<fmt:formatDate value="${flightControlQuery.startDepartureDate}" pattern="yyyy-MM-dd"/>'
					onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}},maxDate:'#F{$dp.$D(\'groupCloseDate\')}'})"
					readonly="readonly" /> <span> 至 </span> <input type="text"
					id="groupCloseDate" class="inputTxt dateinput"
					name="endDepartureDate"
					value='<fmt:formatDate value="${flightControlQuery.endDepartureDate}" pattern="yyyy-MM-dd"/>'
					onclick="WdatePicker({minDate:'#F{$dp.$D(\'groupOpenDate\')}'})"
					readonly="readonly" />
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">价格：</label>
				<input type="text" name="startPrice" class="rmb inputTxt"
					maxlength="7" id="settlementAdultPriceStart"
					value="${flightControlQuery.startPrice}"
					onblur="this.value=this.value.replace(/\D/g,'');"   onkeyup="this.value=this.value.replace(/\D/g,'');" 
					onafterpaste="this.value=this.value.replace(/\D/g,'');"> <span> 至 </span>
				<input type="text" name="endPrice" class="rmb  inputTxt"
					maxlength="7" id="settlementAdultPriceEnd"
					value="${flightControlQuery.endPrice}"
					onblur="this.value=this.value.replace(/\D/g,'');"   onkeyup="this.value=this.value.replace(/\D/g,'');" 
					onafterpaste="this.value=this.value.replace(/\D/g,'');">
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">库存：</label>
				<input type="text" name="startStock" class="inputTxt" maxlength="7"
					id="settlementAdultPriceStart"
					value="${flightControlQuery.startStock}"
					onblur="this.value=this.value.replace(/\D/g,'');"   onkeyup="this.value=this.value.replace(/\D/g,'');" 
					onafterpaste="this.value=this.value.replace(/\D/g,'');"> <span> 至 </span>
				<input type="text" name="endStock" class="inputTxt" maxlength="7"
					id="settlementAdultPriceEnd" value="${flightControlQuery.endStock}"
					onblur="this.value=this.value.replace(/\D/g,'');"   onkeyup="this.value=this.value.replace(/\D/g,'');" 
					onafterpaste="this.value=this.value.replace(/\D/g,'');">
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">状态：</label>
				<div class="selectStyle">
					<form:select path="status">
						<form:option value="">不限</form:option>
						<form:option value="0">已提交</form:option>
						<form:option value="1">保存草稿</form:option>
						<form:option value="2">已删除</form:option>
					</form:select>
				</div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">发布人：</label>
				<input type="text" class="inputTxt" name="createUser"
					value="${flightControlQuery.createUser}" />
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">更新人：</label>
				<input type="text" class="inputTxt" name="updateUser"
					value="${flightControlQuery.updateUser}" />
			</div>
			<label class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">控票名称：</div>
				<input type="text" class="inputTxt" name="name"
					value="${flightControlQuery.name}" />
			</label>
		</div>
		<div class="kong"></div>
	</div>
</form:form>
