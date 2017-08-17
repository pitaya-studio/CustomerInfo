<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>询价-酒店-销售询房-询房列表</title>
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
$(function(){
	//搜索条件筛选
	launch();
	//操作浮框
	operateHandler();
	//天数插件
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
	//modify for 显示排序 by tlw at 20170223 start.
	showSort();
	//modify for 显示排序 by tlw at 20170223 end.
	//初始化询价对象框
	initSuggest({});
	//销售员默认选中
	getAjaxDepSelect($("#wholesalerId"),"${hotelQuoteQuery.userId}");
	//选择条件默认选中
	getAjaxSelect('island',$('#country'),"${hotelQuoteQuery.islandUuid}");
	//判定是否展开搜索
	$(".ydxbd select,#quoteObject").each(function (){
		if($(this).val()!=''){
			$(".zksx").click();
			return false;
		}
	})
	//为酒店名称添加title
	addHotelTitle ();
	//为酒店集团添加title
	addHotelGroupTitle();
});
//展开、关闭
function expand(child,obj,srcActivityId){
	if($(child).css("display")=="none"){
		$(obj).html("收起");
		$(child).show();
		$(obj).addClass('team_a_click2');
		$(obj).parents("td").addClass("td-extend").parent("tr").addClass("tr-hover");
	}else{
		$(child).hide();
		$(obj).removeClass('team_a_click2');
		$(obj).parents("td").removeClass("td-extend").parent("tr").removeClass("tr-hover");
		$(obj).html("展开");
	}
}
//删除产品的提示信息
function confirmxDel(mess,proId){
	top.$.jBox.confirm(mess,'系统提示',function(v){
	if(v=='ok'){
		//loading('正在提交，请稍等...');
		}
	},{buttonsFocus:1});
	return false;
}
//下架产品
function downProduct(activityId){
	$.jBox.confirm("确定要下架该产品吗？", "提示", function(v, h, f){
		if (v == 'ok') {
			//$("#searchForm").attr("action","/a/activity/manager/batchoff/"+activityId);
			//$("#searchForm").submit();
		}else if (v == 'cancel'){ 
		}
	});		
}
function confirmBatchIsNull(mess,sta) {
	if(activityIds != ""){
		if(sta=='off'){
			confirmBatchOff(mess);
		}else if(sta=='del'){
			confirmBatchDel(mess);
		}
	}else{
		$.jBox.info('未选择产品','系统提示');
	}
}
//批量删除确认对话框
function confirmBatchDel(mess){
	top.$.jBox.confirm(mess,'系统提示',function(v){
		if(v=='ok'){
			//loading('正在提交，请稍等...');
			//$("#searchForm").attr("action","/a/activity/manager/batchdel/"+activityIds);
			//$("#searchForm").submit();
		}
	},{buttonsFocus:1});
	return false;
}
// 批量下架确认对话框
function confirmBatchOff(mess){
	top.$.jBox.confirm(mess,'系统提示',function(v){
		if(v=='ok'){
			//loading('正在提交，请稍等...');
			//$("#searchForm").attr("action","/a/activity/manager/batchoff/"+activityIds);
			//$("#searchForm").submit();
		}
	},{buttonsFocus:1});
	return false;
}
//排序
/*function sorts(sortBy,element){
	var _this = $(element);
	_this.parent().siblings("a").children("i").attr("class","i_sort");
	var sortFlag = _this.attr("class");
	//降序
	if(sortFlag == "i_sort i_sort_up"){
		_this.attr("class","i_sort i_sort_down");
	}
	//升序
	else if(sortFlag == "i_sort i_sort_down"){
		_this.attr("class","i_sort i_sort_up");
	}else{
		_this.attr("class","i_sort i_sort_up");
	}
}*/
//分页
function page(n,s){
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").submit();
	return false;
}
//排序
/*function cantuansortby(sortBy,obj){
	var temporderBy = $("#orderBy").val();
	if(temporderBy.match(sortBy)){
		sortBy = temporderBy;
		if(sortBy.match(/ASC/g)){
			sortBy = $.trim(sortBy.replace(/ASC/gi,""))+" DESC";
		}else{
			sortBy = $.trim(sortBy.replace(/DESC/gi,""))+" ASC";
		}
	}else{
		sortBy = sortBy+" DESC";
	}
	//$("#searchForm").attr("action","${ctx}/hotelQuote/hotelQuoteList");
	$("#orderBy").val(sortBy);
	$("#searchForm").submit();
}*/
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
		async: false,
	   	success: function(data){
	   	if(type == "island"){
	   	   	//清空岛屿、酒店的选项
	   		$("#"+type).empty();
	   		$("#hotel").empty();
	   		$("#"+type).append("<option value=''>不限</option>");
	   		$("#hotel").append("<option value=''>不限</option>");
	   	}else if(type=="hotel"){
	   		//清空酒店选项
   			$("#hotel").empty();
	   		$("#hotel").append("<option value=''>不限</option>");
	   	}else{
	   		/* $("#hotelGroup").empty();
	   		$("#roomtype").append("<option value=''>不限</option>"); */
	   	}
	   		if(data){
	   			if(type=="hotel"){
		   			$.each(data.hotelList,function(i,n){
		   				$("#"+type).append($("<option/>").text(n.nameCn).attr("value",n.uuid));
		   			});
		   			if(value){
		   				$("#"+type).val(value);	   				
		   			} 
		   			$("#island").attr("title", $("#island").find("option:selected").text());
	   			}else if(type=="island"){
	   				$.each(data.islandList,function(i,n){
	   					$("#island").append($("<option/>").text(n.islandName).attr("value",n.uuid));
	   				});
	   				 if(value){
	   					$("#"+type).val(value);
		   				getAjaxSelect("hotel",$("#"+type),"${hotelQuoteQuery.hotel}");
	   				} 
	   			}else{
	   				$.each(data.hotelGroups,function(i,n){
	   					$("#hotelGroup").append($("<option/>").text(n.islandName).attr("value",n.uuid));
	   				});
	   			}
	   		}
	   	}
	});
}

//modify for UG_V2排序方法 by tlw at 20170223 start.
function sortby(sortBy,obj){
	var temporderBy = $("#orderBy").val();
	if(temporderBy.match(sortBy)){
		sortBy = temporderBy;
		if(sortBy.match(/ASC/g)){
			sortBy = $.trim(sortBy.replace(/ASC/gi,""))+" DESC";
		}else{
			sortBy = $.trim(sortBy.replace(/DESC/gi,""))+" ASC";
		}
	}else{
		sortBy = sortBy+" DESC";
	}
	$("#orderBy").val(sortBy);
	$("#searchForm").submit();
}
function showSort(){
	var _$orderBy = $("#orderBy").val();
	if (!_$orderBy || _$orderBy == "") {
		//默认按出团日期降序排序
		_$orderBy = "groupOpenDate DESC";
	}
	var orderBy = _$orderBy.split(" ");
	$(".activitylist_paixu_left li").each(function() {

		if ($(this).hasClass(orderBy[0])) {
			_$orderBy = orderBy[1].toUpperCase() == "ASC"? "up" : "down";
			var arrow = "<i class=\"icon icon-arrow-" + _$orderBy + "\"></i>"
			$(this).find("a").eq(0).html($(this).find("a").eq(0).html() + arrow);
			$(this).addClass("activitylist_paixu_moren").removeClass("activitylist_paixu_left_biankuang");
		}
	});
	var isOrder = $("#isOrder").val();
	if ("true" == isOrder) {
		$("#orderLabel").addClass("select")
	} else {
		$("#groupLabel").addClass("select")
	}
}

//modify for UG_V2排序 by tlw at 20170223 end.

function addHotelTitle(){
	$("#hotel").attr("title",$("#hotel").find("option:selected").text());
}
function addHotelGroupTitle(){
	$("#hotelGroupUuid").attr("title",$("#hotelGroupUuid").find("option:selected").text());
}
//动态获取部门下的人员
function getAjaxDepSelect(obj,value){
	$.ajax({
		type: "POST",
	   	url: "${ctx}/sys/user/getUserByDepartment",
	   	data: {	"departmentId":$(obj).val() },
		dataType: "json",
		async: false,
	   	success: function(data){
	   		$("#userId").empty();
	   		$("#userId").append("<option value=''>不限</option>");
   		    $.each(data,function(i,n){
				$("#userId").append($("<option/>").text(n.name).attr("value",n.id));
			});
   		    if(value){
   		    	$("#userId").val(value);
   		    }
	   	}
	});
}

function resetSearchParams() {
	$(':input', '#searchForm')
			.not(':button, :submit, :reset, :hidden')
			.val('')
			.removeAttr('checked')
			.removeAttr('selected');
	$('#searchForm').find('.custom-combobox').each(function(){
		// var text = $(this).prev().find("option:eq(0)").text();
		// $(this).find("input").attr("title",text);
		$(this).prev().val("");
	})
	$("#quoteObject").val("");

}
</script>
<script type="text/javascript">
(function( $ ) {
	$.widget( "custom.combobox", {
		_create: function() {
			this.wrapper = $( "<span>" ).addClass( "custom-combobox" ).insertAfter( this.element );
			this.element.hide();
			this._createAutocomplete();
			this._createShowAllButton();
		},
   
		_createAutocomplete: function() {
		    var selected = this.element.children( ":selected" ),
			value = selected.val() ? selected.text() : "";
			
			this.input = $("<input>").appendTo( this.wrapper ).val( value ).attr( "title", "" ).addClass( "custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left" ).autocomplete({
				delay: 0,
				minLength: 0,
				source: $.proxy( this, "_source" )
			}).tooltip({
			 	tooltipClass: "ui-state-highlight"
			});
			
			this._on( this.input, {
				autocompleteselect: function( event, ui ) {
			  		ui.item.option.selected = true;
			  		this._trigger( "select", event, {
						item: ui.item.option
			  		});
				},autocompletechange: "_removeIfInvalid"
		  	});
		},
   
		_createShowAllButton: function() {
			var input = this.input,
			wasOpen = false;
			
			$( "<a>" ).attr( "tabIndex", -1 ).attr( "title", "选择" ).tooltip().appendTo( this.wrapper ).button({
				icons: {primary: "ui-icon-triangle-1-s"},
			  	text: false
			}).removeClass( "ui-corner-all" ).addClass( "custom-combobox-toggle ui-corner-right" ).mousedown(function() {
			  	wasOpen = input.autocomplete( "widget" ).is( ":visible" );
			}).click(function() {
				input.focus();
				
				// Close if already visible
				if ( wasOpen ) {
				  return;
				}
   
				// Pass empty string as value to search for, displaying all results
				input.autocomplete( "search", "" );
			});
		},
   
		_source: function( request, response ) {
			var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
			response( this.element.children( "option" ).map(function() {
				var text = $( this ).text();
				if ( this.value && ( !request.term || matcher.test(text) ) )
			  	return {
					label: text,
					value: text,
					option: this
			  	};
		  	}));
		},
   
		_removeIfInvalid: function( event, ui ) {
   
			// Selected an item, nothing to do
			if ( ui.item ) {//console.log(ui.item);
				this._trigger("afterInvalid",null,ui.item.value);
				return;
			}
   
			// Search for a match (case-insensitive)
			var value = this.input.val(),
				valueLowerCase = value.toLowerCase(),
				valid = false;
			this.element.children( "option" ).each(function() {
				if ( $( this ).text().toLowerCase() === valueLowerCase ) {
					this.selected = valid = true;
					return false;
				}
			});
   
			// Found a match, nothing to do
			if ( valid ) {
				this._trigger("afterInvalid",null,value);
				return;
			}
   
			// Remove invalid value
			this.input.val( "" ).attr( "title", value + "" ).tooltip( "open" );
			this.element.val( "" );
			this._delay(function() {
				this.input.tooltip( "close" ).attr( "title", "" );
			}, 2500 );
			this.input.data( "ui-autocomplete" ).term = "";
		},
   
		_destroy: function() {
			this.wrapper.remove();
			this.element.show();
		}
	});
})( jQuery );
</script>
</head>
<body>
<%--added for UG_V2 添加tab at 20170223 by tlw start.--%>
<content tag="three_level_menu">
	<li class="active">
		<a href="javascript:void(0)">报价列表</a>
	</li>
</content>
<%--added for UG_V2 添加tab at 20170223 by tlw end.--%>
            	<!--右侧内容部分开始-->
                <div class="activitylist_bodyer_right_team_co_bgcolor">
                    <form id="searchForm" action="${ctx}/hotelQuote/hotelQuoteList" method="post">
                       <input id="activityIds" type="hidden" name="activityIds" value=""/>
                       <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				       <input type="hidden" name="pageSize" id="pageSize" value="${page.pageSize}"/>
                       <input id="orderBy" name="orderBy" value="${hotelQuoteQuery.orderBy}" type="hidden">
                       <div class="activitylist_bodyer_right_team_co">
                            <div class="activitylist_bodyer_right_team_co2 pr">
                            		<label>报价日期：</label><input type="text" id="fromDate" name="fromDate" value="${hotelQuoteQuery.fromDate }" class="inputTxt dateinput" onclick="WdatePicker()" readonly="readonly"> 
                           			 <span> 至 </span>  
                            		<input type="text" id="endDate"  name="endDate" value="${hotelQuoteQuery.endDate }" class="inputTxt dateinput" onclick="WdatePicker()" readonly="readonly">
							</div>
						   <a class="zksx filterButton_solo">筛选</a>
                        	<div class="form_submit">
                                <input class="btn ydbz_x btn-primary" value="搜索" type="submit">
                                <%--bug17533 type=reset换成type=button，添加点击事件resetSearchParams--%>
								<input class="btn ydbz_x" type="button" onclick="resetSearchParams()" value="清空所有条件"/>
                           </div>
						   <p class="main-right-topbutt"><a class="primary" href="${ctx}/autoQuotedPrice/quotedPriceForm">报价器</a></p>
                        	<div class="ydxbd">
								<span></span>
                                <div class="activitylist_bodyer_right_team_co3">
                            		<label class="activitylist_team_co3_text">分公司：</label>
									<div class="selectStyle">
										<select name="wholesalerId" id="wholesalerId" onchange="getAjaxDepSelect(this);">
											 <option value="">不限</option>
											 <c:forEach items="${departmentList}" var="department" >
												<option value="${department.id}" <c:if test="${hotelQuoteQuery.wholesalerId == department.id}">selected="selected"</c:if> >${department.name}</option>
											</c:forEach>
										</select>
									</div>
                       			</div>
								<div class="activitylist_bodyer_right_team_co3">
									<%-- <div class="activitylist_team_co3_text">询价客户：</div> --%>
									<label class="activitylist_team_co3_text">询价客户：</label>
									<trekiz:suggestQuote name="quoteObject"
														 defaultValue="${hotelQuoteQuery.quoteObject}"  displayValue="" ajaxUrl="${ctx }/agent/manager/findAllAgentinfo" />
								</div>
								<div class="activitylist_bodyer_right_team_co3">
									<label class="activitylist_team_co3_text">酒店集团：</label>
									<div class="selectStyle">
										<trekiz:defineDict id="hotelGroupUuid" name="hotelGroupUuid" type="hotel_group" defaultValue="${hotelQuoteQuery.hotelGroupUuid}" onchange="addHotelGroupTitle()"/>
									</div>
								</div>
								<div class="activitylist_bodyer_right_team_co3">
									<label class="activitylist_team_co3_text">酒店名称：</label>
									<div class="selectStyle">
										<select name="hotel" id="hotel"	onchange="addHotelTitle()">
											<option value="" >不限</option>
										</select>
									</div>
								</div>
								<div class="activitylist_bodyer_right_team_co3">
									<label class="activitylist_team_co3_text">岛屿名称：</label>
									<div class="selectStyle">
										<select name="islandUuid" id="island" onchange="getAjaxSelect('hotel',this);">
											<option value="">不限</option>
										</select>
									</div>
								</div>
								<div class="activitylist_bodyer_right_team_co1">
                            		 <label class="activitylist_team_co3_text">销售员：</label>
									 <div class="selectStyle">
										 <select name="userId" id="userId">
											 <option value="">不限</option>
										 </select>
									 </div>
                       			</div>
                                <div class="activitylist_bodyer_right_team_co1"  style="display:none">
                                <!--目前是固定值马尔代夫，待升级时更改  -->
									<label>国家：</label>
									<input  id="coutry" value="80415d01488c4d789494a67b638f8a37" disabled="disabled"/>
							        <%--  <trekiz:suggest name="country" style="width:150px;"
										defaultValue="${hotelControlQuery.country}" callback="getAjaxSelect('island',$('#country'))"  displayValue="${countryName}"
										ajaxUrl="${ctx}/geography/getAllConListAjax" /> --%>
								</div>
							</div>
                      		<div class="kong"></div>
                     	</div>
                    </form>
				<!--查询结果筛选条件排序开始-->
				<div class="activitylist_bodyer_right_team_co_paixu">
					<div class="activitylist_paixu">
						<div class="activitylist_paixu_left">
							<ul>

								<li class="activitylist_paixu_left_biankuang hqc.createDate">
									<a onclick="sortby('hqc.createDate',this)">报价日期</a>
								</li>
							</ul>
						</div>
						<div class="activitylist_paixu_right">

							查询结果&nbsp;<strong>${count}</strong>&nbsp;条
						</div>
						<div class="kong"></div>
					</div>
				</div>

				<!--查询结果筛选条件排序结束-->	
                <table id="contentTable house_h35" class="table activitylist_bodyer_table mainTable" >
                    <thead>
                        <tr>
                            <th width="5%">序号</th>
                            <th width="11%">分公司</th>
                            <th width="10%">销售员</th>
                            <th width="10%">询价客户</th>
                            <th width="12%">客户联系人</th>
                            <th width="9%">岛屿名称</th>
                            <th width="10%">酒店名称</th>
                            <th width="10%">酒店集团</th>
                            <th width="10%">房型</th>
                            <th width="8%">报价日期</th>
                            <th width="5%">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                      <c:if test="${empty page.list}">
						<tr class="toptr">
							<td colspan="11" style="text-align: center;">暂无相关信息</td>
						</tr>
			       	  </c:if>
			       	  <c:forEach items="${page.list }" var="record" varStatus="v">
							<tr>
	                        	<td class="tc">${v.count }</td>
	                            <td class="tc">${fns:getOfficeById(record.wholesalerId)}</td>
	                            <td class="tc">${fns:getUserNameById(record.userId)}</td>
	                            <td class="tc">
	                               <c:choose>
										<c:when test="${record.quoteType==2}">
											${fns:getAgentName(record.quoteObject)}	
										</c:when>
										<c:otherwise >
											${record.quoteObject}
										</c:otherwise>
									</c:choose>
	                            </td>
	                            <td class="tc">${record.linkName}</td>
	                            <td class="tc">
	                            <trekiz:autoId2Name4Table tableName="island" sourceColumnName="uuid" srcColumnName="island_name" value="${record.islandUuid}"/>
	                            </td>
	                            <td class="tc"><trekiz:autoId2Name4Table tableName="hotel" sourceColumnName="uuid" srcColumnName="name_cn" value="${record.hotelUuid}"/></td>
	                            <td class="tc">${record.hotelGroup}</td>
	                            <td class="tc">
	                            <c:forEach items="${subList[v.index]}" var="variable" >
	                               <div class="out-date">
	                               <trekiz:autoId2Name4Table tableName="hotel_room" sourceColumnName="uuid" srcColumnName="room_name" value="${variable.hotelRoomUuid }"/>
	                              *${record.roomNum}</div>
	                            </c:forEach>
	                            </td>
								<td class="tc"><fmt:formatDate value="${record.createDate}" pattern="yyyy-MM-dd" /></td>   
	                            <td class="p0">
	                                <dl class="handle">
	                                    <dt><img title="操作" src="${ctxStatic }/images/handle_cz_rebuild.png"></dt>
	                                    <dd class="">
	                                    <p style="width: 80px;">
	                                        <span></span>
	                                        <a href="${ctx }/hotelQuote/showDetail/${record.uuid}" target="_blank">详情</a>
	                                        <a href="${ctx }/hotelPlSpeedGenOrder/page?uuid=${record.uuid}" target="_blank">快速生成订单</a>
	                                       <!--暂不做  <a href="#" target="_blank">询房</a> -->
	                                    </p>
	                                    </dd>
	                                </dl>
	                            </td>
	                        </tr>
                        </c:forEach>
                    </tbody>
                </table>
				</div>
				<div class="pagination clearFix">${pageStr}</div>
                <!--右侧内容部分结束-->
</body>
</html>