<%@ page contentType="text/html;charset=UTF-8"%>

<script type="text/javascript">

$(function(){
	initSuggest({});
	showSearchPanel();
	
	getAjaxSelect('island',$('#country'),"${hotelControlQuery.island}");
	
	//可输入select
	 $("#island").comboboxInquiry().on('comboboxinquiryselect', function(event, obj){
		getAjaxSelect('islandway',$("#island")[0]);
	}); 
	$("#hotel").comboboxInquiry().on('comboboxinquiryselect', function(event, obj){
		getAjaxSelect('roomtype',$("#hotel")[0]);
	});
	$("#roomtype").comboboxInquiry().on('comboboxinquiryselect', function(event, obj){
		getAjaxSelect('foodtype',$("#roomtype")[0]);
	});
	$("#foodtype").comboboxInquiry().on('comboboxinquiryselect', function(event, obj){
		
	});
	$("#islandway").comboboxInquiry().on('comboboxinquiryselect', function(event, obj){
		
	}); 
});

//级联查询
//type：下级 级联对象名字 ；obj：当前控件对象；value 下级选中默认值  value2上岛方式和酒店一起调用时使用
function getAjaxSelect(type,obj,value,value2){
	$.ajax({
		type: "POST",
	   	url: "${ctx}/hotelControl/ajaxCheck",
	   	data: {
				"type":type,
				"uuid":$(obj).val()
			  },
		dataType: "json",
	   	success: function(data){
	   		if(type != "islandway"){
		   		$("#"+type).empty();
		   		$("#"+type).append("<option value=''>不限</option>");
	   		}else{
	   			$("#islandway").empty();
	   			$("#hotel").empty();
	   			$("#roomtype").empty();
	   			$("#foodtype").empty();
		   		$("#islandway").append("<option value=''>不限</option>");
		   		$("#hotel").append("<option value=''>不限</option>");
		   		$("#roomtype").append("<option value=''>不限</option>");
		   		$("#foodtype").append("<option value=''>不限</option>");
	   		}
	   		if(data){
	   			if(type=="hotel"){
	   				if(data.hotelList){
			   			$.each(data.hotelList,function(i,n){
			   				$("#"+type).append($("<option/>").text(n.nameCn).attr("value",n.uuid));
			   			});
	   				}
		   			if(value){
		   				$("#"+type).val(value);
	   					getAjaxSelect(tranferObj(type),$("#"+type),tranferValue(type));
		   			}
		   			$("#hotel").comboboxInquiry('reset');
		   			$("#hotel").comboboxInquiry().trigger('comboboxinquiryselect');
	   			}else if(type=="roomtype"){
	   				if(data.roomtype){
		   				$.each(data.roomtype,function(i,n){
		   					$("#"+type).append($("<option/>").text(n.roomName).attr("value",n.uuid));
		   				});
	   				}
	   				
	   				if(value){
		   				$("#"+type).val(value);
		   				getAjaxSelect(tranferObj(type),$("#"+type),tranferValue(type));
	   				}else{
	   					$("#roomtype").comboboxInquiry().trigger('comboboxinquiryselect');	
	   				}
	   				$("#roomtype").comboboxInquiry('reset');
	   				//$("#foodtype").comboboxInquiry('reset');
	   			}else if(type=="foodtype"){
	   				if(data.roomMeals){
		   				$.each(data.roomMeals,function(i,n){
		   					$("#"+type).append($("<option/>").text(n.mealName).attr("value",n.uuid));
		   				});
	   				}
	   				if(value){
	   					$("#"+type).val(value);
		   				//getAjaxSelect(tranferObj(type),$("#"+tranferObj(type)),tranferValue(type));
	   				}
	   				$("#foodtype").comboboxInquiry('reset');
	   			}else if(type=="islandway"){
	   				if(data.listIslandWay){
		   				$.each(data.listIslandWay,function(i,n){
		   					$("#"+type).append($("<option/>").text(n.label).attr("value",n.uuid));
		   				});
	   				}
	   				if(data.hotelList){
		   				$.each(data.hotelList,function(i,n){
	 	   					$("#hotel").append($("<option/>").text(n.nameCn).attr("value",n.uuid));
		   				});
	   				}
	   				if(value){
	   					$("#islandway").val(value);
	   				
	   				}
	   				if(value2){
	   					$("#hotel").val("${hotelControlQuery.hotel}");
	   					getAjaxSelect(tranferObj("hotel"),$("#hotel"),"${hotelControlQuery.roomtype}");
	   				}
	   				else{
	   					$("#hotel").comboboxInquiry().trigger('comboboxinquiryselect');
	   				}
	   				$("#hotel").comboboxInquiry('reset');
	   				$("#islandway").comboboxInquiry('reset');
	   			
	   			}else if(type=="island"){
	   				if(data.islandList){
		   				$.each(data.islandList,function(i,n){
		   					$("#island").append($("<option/>").text(n.islandName).attr("value",n.uuid));
		   				});
	   				}
	   				if(value){
	   					$("#"+type).val(value);
		   				getAjaxSelect(tranferObj(type),$("#"+type),"${hotelControlQuery.islandway}","${hotelControlQuery.hotel}");
	   				}else{
	   					$("#island").comboboxInquiry().trigger('comboboxinquiryselect');
	   				}
	   				$("#island").comboboxInquiry('reset');
	   			 	
	   			}
	   		}
	   	}
	});
}

function tranferObj(obj){
	if(obj=='country'){
		return 'island';
	}else if(obj=='island'){
		return 'islandway';
	}else if(obj=='hotel'){
		return 'roomtype';
	}else if(obj=='roomtype'){
		return 'foodtype';
	}
	return '';
}
function tranferValue(obj){
	if(obj=='country'){
		return '${hotelControlQuery.island}';
	}else if(obj=='island'){
		return '${hotelControlQuery.islandway}';
	}else if(obj=='hotel'){
		return '${hotelControlQuery.roomtype}';
	}else if(obj=='roomtype'){
		return '${hotelControlQuery.foodtype}';
	}
	return '';
}
//有查询条件的时候，DIV不隐藏
function showSearchPanel(){
	var island = "${hotelControlQuery.island}";
	var hotel = "${hotelControlQuery.hotel}";
	var roomtype = "${hotelControlQuery.roomtype}";
	var foodtype = "${hotelControlQuery.foodtype}";
	var islandway = "${hotelControlQuery.islandway}";
	var hoteljituan = "${hotelControlQuery.hoteljituan}";
	var roomnum = "${hotelControlQuery.roomnum}";
	var flights = "${hotelControlQuery.flights}";
	var groupOpenDate = "${hotelControlQuery.groupOpenDate}";
	var groupCloseDate = "${hotelControlQuery.groupCloseDate}";
	var publishperson = "${hotelControlQuery.publishperson}";
	var controlname = "${hotelControlQuery.controlname}";

	if(isNotEmpty(island) || isNotEmpty(hotel) || isNotEmpty(roomtype) || isNotEmpty(foodtype)|| isNotEmpty(islandway)
			|| isNotEmpty(hoteljituan)|| isNotEmpty(publishperson)|| isNotEmpty(controlname)||isNotEmpty(roomnum) 
			|| isNotEmpty(flights)|| isNotEmpty(groupOpenDate)|| isNotEmpty(groupCloseDate)){
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
</script>
<form id="searchForm" action="${ctx}/hotelControl/hotelControlList?showType=${showType}&activityStatus=${activityStatus}" method="post">
	<input id="activityStatus" name="activityStatus" value="${activityStatus}" type="hidden"> 
	<input id="pageNo" name="pageNo" value="${pageNo}" type="hidden"> 
	<input id="pageSize" name="pageSize" value="${pageSize}" type="hidden">
	<input id="orderBy" name="orderBy" value="${orderBy}" type="hidden">
	<input id="showType" name="showType" value="${showType}" type="hidden"> 
	<div class="activitylist_bodyer_right_team_co">
		<div class="activitylist_bodyer_right_team_co2">
			<%--<label>国家：</label>--%>
	        <trekiz:suggest name="country" style="width:150px;border-radius: 4px 0 0 4px;width: 315px;"
				defaultValue="${hotelControlQuery.country}" callback="getAjaxSelect('island',$('#country'))"  displayValue="${countryName}"
				ajaxUrl="${ctx}/geography/getAllConListAjax" />
		</div>
		<a class="zksx">筛选</a>
		<div class="form_submit">
			<input class="btn btn-primary" value="搜索" type="submit">
			<input class="btn conditionalReset" onclick="resetSearchParams()" value="清空所有条件" type="button">
		</div>
		<p class="main-right-topbutt">
			<a class="primary" target="_blank" href="${ctx}/hotelControl/tosavehotelcontrol">新增控房</a>
		</p>
		<%--<span class="conditionalReset"	onclick="resetSearchParams()">条件重置</span>--%>
		<div class="ydxbd">
			<span></span>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">日期：</label>
				<input type="text" id="groupOpenDate" class="inputTxt dateinput"
					name="groupOpenDate" value="${hotelControlQuery.groupOpenDate}"
					onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}},maxDate:'#F{$dp.$D(\'groupCloseDate\')}'})"
					readonly="readonly" /> <span> 至 </span> <input type="text"
					id="groupCloseDate" class="inputTxt dateinput"
					name="groupCloseDate" value="${hotelControlQuery.groupCloseDate}"
					onclick="WdatePicker({minDate:'#F{$dp.$D(\'groupOpenDate\')}'})"
					readonly="readonly" />
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">岛屿名称：</label>
				<select name="island" id="island"
					onchange="getAjaxSelect('islandway',this);">
					<option value="" selected="selected">不限</option>
				</select>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">酒店名称：</label>
				<select name="hotel" id="hotel"
					onchange="getAjaxSelect('roomtype',this);">
					<option value="" selected="selected">不限</option>
				</select>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">房型：</label>
				<select name="roomtype" id="roomtype"
					onchange="getAjaxSelect('foodtype',this);">
					<option value="" selected="selected">不限</option>
				</select>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">餐型：</label>
				<select name="foodtype" id="foodtype">
					<option value="" selected="selected">不限</option>
				</select>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">上岛方式：</label>
				<select class="sel-w1" name="islandway" id="islandway"
					onchange="getAjaxSelect('',this);">
					<option value="" selected="selected">不限</option>
				</select>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">发布人：</label>
				<input type="text" class="inputTxt" name="publishperson" value="${hotelControlQuery.publishperson }" maxlength="50" />
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">酒店集团：</label>
				<div class="selectStyle">
					<trekiz:defineDict id="hoteljituan" name="hoteljituan" type="hotel_group" defaultValue="${hotelControlQuery.hoteljituan}" />
				</div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">现有库存：</label>
				<div class="selectStyle">
					<select name="roomnum">
						<option value="" >不限</option>
						<option value="&lt;5" <c:if test="${hotelControlQuery.roomnum=='<5'}"> selected="selected"</c:if> >少于5间</option>
						<option value="BETWEEN 5 AND 9 " <c:if test="${hotelControlQuery.roomnum=='BETWEEN 5 AND 9'}"> selected="selected"</c:if> >5到10间</option>
						<option value="&gt;10" <c:if test="${hotelControlQuery.roomnum=='>10'}"> selected="selected"</c:if> >大于10间</option>
					</select>
				</div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">航班：</label>
				<input type="text" name="flights" value="${hotelControlQuery.flights }" class="inputTxt" maxlength="122">
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">控房名称：</label>
				<input type="text" class="inputTxt" name="controlname" value="${hotelControlQuery.controlname }" maxlength="16"/>
			</div>
		</div>
		<div class="kong"></div>
	</div>
</form>
