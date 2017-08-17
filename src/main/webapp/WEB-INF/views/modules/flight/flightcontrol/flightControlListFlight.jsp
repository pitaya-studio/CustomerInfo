<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler" />
<title>机票库存表--航班列表</title>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<script type="text/javascript" src="${ctxStatic}/js/little_logo.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>	
<script type="text/javascript">
$(function(){
	//搜索条件筛选
	launch();
	//产品名称文本框提示信息
	inputTips();
	//操作浮框
	operateHandler();
    g_context_url="${ctx}";
   //取消一个checkbox 就要联动取消 全选的选中状态
	$("input[name='ids']").click(function(){
		if($(this).attr("checked")){
			
		}else{
			$("input[name='allChk']").removeAttr("checked");
		}
	});
});
//条件重置
var resetSearchParams = function(){
	$(':input','#searchForm')
	 .not(':button, :submit, :reset, :hidden')
	 .val('')
	 .removeAttr('checked')
	 .removeAttr('selected');
	$("#country").val('');
}	 
//分页
function page(n,s){
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    $("#searchForm").submit();
    return false;
}
//排序
function cantuansortby(sortBy,obj){
	var status = ${activityStatus};
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
    $("#searchForm").attr("action","${ctx}/flightControl/flightControlList?showType=2&activityStatus="+status);
    $("#orderBy").val(sortBy);
    $("#searchForm").submit();
}
function changToDate(){
	//var status = ${activityStatus};
	//window.open(g_context_url+"/hotelControl/hotelControlList?showType=1&activityStatus="+status,'_self');
	window.open(g_context_url+"/flightControl/flightControlList?showType=1&activityStatus=3",'_self');
}
//改变status的状态值
function del(id){
	var ids = [];
	ids.push(id);
	v_deleteItems(ids);
}
//
function checkall(obj){
	if($(obj).attr("checked")){
		$("input[name='ids']").attr("checked",'true');
		$("input[name='allChk']").attr("checked",'true');
		$("input[sign='uuids']").attr("checked",'true');
	}else{
		$("input[name='ids']").removeAttr("checked");
		$("input[name='allChk']").removeAttr("checked");
		$("input[sign='uuids']").removeAttr("checked");
	}
}
function subcheckall(obj){
	if($(obj).attr("checked")){
		$("input[name="+$(obj).val()+"]").attr("checked",'true');
		$(obj).attr("checked",'true');
	}else{
		$("input[name="+$(obj).val()+"]").removeAttr("checked");
		$(obj).removeAttr("checked");
	}
}
//级联取消父级选中状态
function canparechecon(obj){
	$("#"+$(obj).attr("name")).removeAttr("checked");
	$("input[name='allChk']").removeAttr("checked");
}
//批量删除按钮调用的方法
function alldel(){
	if($("[sign=uuids]:checkbox:checked").length>0){
		var ids = [];
		$("[sign=uuids]:checkbox:checked").each(function(){ids.push($(this).val())});
		v_deleteItems(ids);
	}else{
		top.$.jBox.tip('请选择后进行删除操作','warning');
	}
}
//删除按钮处调用的方法
function suballdel(obj){
	if($("[name="+$(obj).attr('name')+"]:checkbox:checked").length>0){
		var ids = [];
		$("[name="+$(obj).attr('name')+"]:checkbox:checked").each(function(){ids.push($(this).val())});
		v_deleteItems(ids);
	}else{
		top.$.jBox.tip('请选择后进行删除操作','warning');
	}
}
//根据传入的flight_control_detail的uuid的值，处理status字段的状态
function v_deleteItems(ids){
	$.jBox.confirm("确定要删除数据吗？", "系统提示", function(v, h, f) {
		if (v == 'ok') {
			$.jBox.tip("正在删除数据...", 'loading');
			$.post( "${ctx}/flightControl/delete", {"uuids":ids.join(",")}, 
				function(data){
					if(data.result=="1"){
						$.jBox.prompt("删除成功!", 'success', 'info', { closed: function () { $("#searchForm").submit(); } });
					}else{
						top.$.jBox.tip(data.message,'warning');
					}
				}
			);
			
		} else if (v == 'cancel') {
                
		}
	});
}
//清空方法
function delFlag(uuid){
	$.jBox.confirm("确定要清空数据吗？", "系统提示", function(v, h, f) {
		if (v == 'ok') {
			$.jBox.tip("正在清空数据...", 'loading');
			$.post( "${ctx}/flightControl/deleteFlag", {"uuid":uuid}, 
				function(data){
					if(data.result=="1"){
						$.jBox.prompt("清空成功!", 'success', 'info', { closed: function () { $("#searchForm").submit(); } });
					}else{
						top.$.jBox.tip(data.message,'warning');
					}
				}
			);
			
		} else if (v == 'cancel') {
                
		}
	});
}
//发布方法
function publishControl(detailUuid){
	$.jBox.confirm("确定要发布数据吗？", "系统提示", function(v, h, f) {
		if (v == 'ok') {
			$.jBox.tip("正在发布数据...", 'loading');
			$.post( "${ctx}/flightControl/flightControlDetailRelease/"+detailUuid,  
				function(data){
					if(data.result=="success"){
						$.jBox.prompt("发布成功!", 'success', 'info', { closed: function () { $("#searchForm").submit(); } });
					}else{
						top.$.jBox.tip(data.message,'warning');
					}
				}
			);
			
		} else if (v == 'cancel') {
                
		}
	});
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
<style type="text/css">
.custom-combobox-toggle {
	height: 26px;
	margin:0 0 0 -1px;
	padding: 0;
	/* support: IE7 */
	*height: 1.7em;
	*top: 0.1em;
}
.custom-combobox-input {
	margin: 0;
	padding: 0.3em;width:166px;
}
.ui-autocomplete{height:200px;overflow:auto;}
.sort{color:#0663A2;cursor:pointer;}
.custom-combobox input[type="text"]{height:26px;width:166px;}
#house_h35{margin-bottom: 0px;}
#house_h35 th{ height:35px; padding:0px 8px 0px 8px; text-align:left; background-color:#edf0f1;}
#house_h35 th p{  line-height:35px; float:right; display:inline-block; font-weight:normal; color:#00a0e9;}
#house_h35 th p a:link{ color: #00a0e9; text-decoration: none; }
#house_h35 th p a:hover{ color: #00a0e9;}
#house_h35 th p a:visited{ color: #00a0e9; text-decoration: none; }
#house_h35 input[type="checkbox"]{ padding:0px; margin:0px 8px 0px 0px;}
.house_h35_checkbox02{ padding:0px; margin:0px 8px 0px 0px !important;}
.activitylist_bodyer_table{ width:100%; margin-top:10px;  border-right:1px solid #cccccc; *border-bottom:1px solid #ddd;border-top:1px solid #ddd;}
.hotel_list_n_all{ display:inline-block; line-height:35px; float:left; font-weight:normal;}
.hotel_list_n_all td{ padding:0px; margin:0px;}
.hotel_list_n{padding-right:20px;}
#pad_mar_none td{ padding:0px; margin:0px;}

.list_date_hotel{font-weight: bold;
padding: 0px 5px;
line-height: 30px;
float:left;}
.list_date_hotel .date{ width: 18px; height: 18px; float: left; margin-top: 6px; background-attachment: scroll; background-color: transparent; background-image: url(${ctxStatic}/images/ico_list01.png); background-repeat: no-repeat; background-position: 0px 0px; display: inline-block; cursor: pointer; }
.list_date_hotel .date2{ width: 18px; height: 18px; float: left; margin-top: 6px; background-attachment: scroll; background-color: transparent; background-image: url(${ctxStatic}/images/ico_list01.png); background-repeat: no-repeat; background-position: -25px 0px; display: inline-block; cursor: pointer; }
.list_date_hotel .hotel{ width: 18px; height: 18px; float: left; margin-top:10px; background-attachment: scroll; background-color: transparent; background-image: url(${ctxStatic}/images/ico_list01.png); background-repeat: repeat; background-position: 0px -29px; display: inline-block;  cursor:pointer; }
.list_date_hotel .hotel2{ width: 18px; height: 18px; float: left; margin-top:10px; background-attachment: scroll; background-color: transparent; background-image: url(${ctxStatic}/images/ico_list01.png); background-repeat: repeat; background-position: -25px -29px; display: inline-block;  cursor:pointer; }
			.maintain_add p{min-width:300px;}
/*.t_table_nohover{width: 100%;
margin-top: 10px;
border-right: 1px solid #CCC;
border-top: 1px solid #DDD;}*/
</style>
</head>
<body>      
			  <!-- <script type="text/javascript">
				$("select[name='country']" ).combobox();
				$("select[name='islands']" ).combobox();
				$("select[name='tways']" ).combobox();
				$("select[name='flights']" ).combobox();
				$("select[name='roomtype']" ).combobox();
				$("select[name='roomnum']" ).combobox();
				$("select[name='hoteljituan']" ).combobox();
				$("select[name='hotelname']" ).combobox();
				$("select[name='foodtype']" ).combobox();		
			 </script> -->
			   <page:applyDecorator name="flight_control" ></page:applyDecorator>
                <!--列表状态开始-->
                <%@ include file="/WEB-INF/views/modules/flight/flightcontrol/searchBarInclude.jsp"%>
                 <!--查询结果筛选条件排序开始-->
                    <div class="pagination">
                        <dl>
                            <dt><input name="allChk" onclick="checkall(this)" type="checkbox">全选</dt>
                            <dd style="width:80px;">
                                <a onclick="alldel()">批量删除</a>
                            </dd>
                        </dl>
                        <div class="filter_btn fl mar_l20"><a class="btn btn-primary" target="_blank" href="#">新增机票库存</a> </div>
                    </div>	
				<div class="filterbox">
                    <div class="list_date_hotel"><i class="date2" onclick="changToDate()"></i>日期列表</div>
                    <div class="list_date_hotel"><i class="hotel"></i>航班列表</div>
					<div class="filter_num">查询结果<strong>${count}</strong>条</div>
					<div class="filter_sort"><span>表单排序：</span>
						<c:choose>
					<c:when test="${orderBy=='' || orderBy==null}">
						<a onclick="cantuansortby('fc.id',this)">创建时间<i
							class="i_sort "></i></a>
						<a onclick="cantuansortby('fc.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('fcd.price',this)">价格<i
							class="i_sort"></i></a>	
						<a onclick="cantuansortby('fcd.stock',this)">库存<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='fc.id DESC'}">
						<a onclick="cantuansortby('fc.id',this)">创建时间<i
							class="i_sort  i_sort_down"></i></a>
						<a onclick="cantuansortby('fc.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('fcd.price',this)">价格<i
							class="i_sort"></i></a>	
						<a onclick="cantuansortby('fcd.stock',this)">库存<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='fc.id ASC'}">
						<a onclick="cantuansortby('fc.id',this)">创建时间<i
							class="i_sort  i_sort_up"></i></a>
						<a onclick="cantuansortby('fc.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('fcd.price',this)">价格<i
							class="i_sort"></i></a>	
						<a onclick="cantuansortby('fcd.stock',this)">库存<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='fc.updateDate DESC'}">
						<a onclick="cantuansortby('fc.id',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('fc.updateDate',this)">更新时间<i
							class="i_sort  i_sort_down"></i></a>
						<a onclick="cantuansortby('fcd.price',this)">价格<i
							class="i_sort"></i></a>	
						<a onclick="cantuansortby('fcd.stock',this)">库存<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='fc.updateDate ASC'}">
						<a onclick="cantuansortby('fc.id',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('fc.updateDate',this)">更新时间<i
							class="i_sort  i_sort_up"></i></a>
							<a onclick="cantuansortby('fcd.price',this)">价格<i
							class="i_sort"></i></a>	
						<a onclick="cantuansortby('fcd.stock',this)">库存<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='fcd.stock DESC'}">
						<a onclick="cantuansortby('fc.id',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('fc.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('fcd.price',this)">价格<i
							class="i_sort"></i></a>	
						<a onclick="cantuansortby('fcd.stock',this)">库存<i
							class="i_sort  i_sort_down"></i></a>
					</c:when>
					<c:when test="${orderBy=='fcd.stock ASC'}">
						<a onclick="cantuansortby('fc.id',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('fc.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('fcd.price',this)">价格<i
							class="i_sort"></i></a>	
						<a onclick="cantuansortby('fcd.stock',this)">库存<i
							class="i_sort  i_sort_up"></i></a>
					</c:when>
						<c:when test="${orderBy=='fcd.price ASC'}">
						<a onclick="cantuansortby('fc.id',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('fc.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('fcd.price',this)">价格<i
							class="i_sort  i_sort_up"></i></a>	
						<a onclick="cantuansortby('fcd.stock',this)">库存<i
							class="i_sort"></i></a>
					</c:when>
						<c:when test="${orderBy=='fcd.price DESC'}">
						<a onclick="cantuansortby('fc.id',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('fc.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('fcd.price',this)">价格<i
							class="i_sort  i_sort_down"></i></a>	
						<a onclick="cantuansortby('fcd.stock',this)">库存<i
							class="i_sort"></i></a>
					</c:when>
				</c:choose>
					</div>
				</div>
				<!--查询结果筛选条件排序结束-->	
                <div class="house_list_out">
                <!--日期列表开始-->              
                <!--日期列表结束-->
                <!--酒店列表开始-->
              <div class="house_list_out02" >
                <table class="table activitylist_bodyer_table t_table_nohover"  id="contentTable pad_mar_none">
                    <thead>
                        <tr>
                            <th width="4%">序列</th>
                            <th width="6%">日期</th>
                            <th width="10%">舱位等级</th>
                            <th width="7%">价格</th>
                            <th width="8%">总税</th>
                            <th width="8%">库存</th>
							<th width="16%">参考酒店</th>
                            <th width="6%">状态</th>
                            <th width="6%">发布人</th>
                            <th width="6%">更新人</th>
                            <th width="11%">创建时间<br />更新时间</th>
                            <th width="5%">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                     <c:if test="${empty page.list}">
						<tr class="toptr">
							<td colspan="12" style="text-align: center;">暂无相关信息</td>
						</tr>
			      	</c:if>
                    </tbody>
                </table>
                <c:forEach items="${page.list }" var="record" varStatus = "s">
                <table   class="table activitylist_bodyer_table house_35" id="house_h35">
                    <thead>
                        <tr>
                            <th  colspan="12">
                            <div class="hotel_list_n_all">
								<input type="checkbox" onclick="subcheckall(this)" name="ids" value="${record.uuid}" id="${record.uuid}">
								<span class="hotel_list_n">航空公司：<em>${fns:getAirlineNameByAirlineCode(record.airline)}	</em></span>
								<span class="hotel_list_n"> 国家：<em><trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${record.country}"/></em></span>
                                <span class="hotel_list_n">岛屿名称：<em><trekiz:autoId2Name4Table tableName="island" sourceColumnName="uuid" srcColumnName="island_name" value="${record.island}"/></em></span>
                                <span class="hotel_list_n">控票名称：<em>${record.name}</em></span>
							</div> 
                            <p><a href="#">新增日期</a> | <a href="#">详情</a> | <a href="#">修改</a> | <a href="#">查看价单</a> | <a href="#" name="${record.uuid}" onclick="suballdel(this)">删除</a></p></th>
                            </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${flightList[s.index]}" var="entry" >
                              <tr>
                                <td width="4%" class="tc">
                                <input type="checkbox" onclick="canparechecon(this)" name="${record.uuid}" value="${entry.uuid}" sign="uuids" />
                                ${s.index+1}</td>
                                <td width="6%" class="tc">
                                <fmt:formatDate value="${entry.departureDate}" pattern="yyyy-MM-dd" />
                                </td>
                                <td width="10%"  class="tc">
                                    ${fns:getDictLabel(entry.spaceGradeType,"spaceGrade_Type" , "无")}
                                </td>
                                <td  width="7%" class="tc">
	                                <p><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${entry.priceId}"/>
	                                 ${entry.price}</p>
                                </td>
                                <td width="8%" class="tc">
	                                 <p> <trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${entry.taxesId}"/>
	                                  ${entry.taxesPrice}</p>
                                </td>
                                <td width="8%" class="tc">${entry.stock}</td>
                                <td width="16%" class="tc">
                                    <c:set value="${entry.hotelUuid}" var="hotelNames" /> 
                                    <c:forEach items="${fn:split(hotelNames, ',')}" var="hotel">
                                       <p><trekiz:autoId2Name4Table tableName="hotel" sourceColumnName="uuid" srcColumnName="name_cn" value="${fn:trim(hotel)}"/></p>
                                    </c:forEach>
                                </td>
                                <td width="6%" class="tc 
                                <c:if test="${entry.status=='0'}">tdgreen</c:if>
                                <c:if test="${entry.status=='2'}">tdred</c:if>">
                                    <c:if test="${entry.status=='0'}">已提交</c:if>
									<c:if test="${entry.status=='1'}">草稿</c:if>
									<c:if test="${entry.status=='2'}">已删除</c:if>
                                </td>
                                <td width="6%" class="tc">${fns:getUserById(entry.createBy).name}</td>
                                <td width="6%" class="tc">${fns:getUserById(entry.updateBy).name}</td>
                                <td width="11%" class="tc"><fmt:formatDate value="${entry.createDate}"
									pattern="yyyy-MM-dd HH:mm" /><br />
                                 <fmt:formatDate
									value="${entry.updateDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                                <td width="5%" class="p0">
                                <dl class="handle">
                                    <dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
                                    <dd class="">
                                    <p>
                                        <span></span>
                                        <a href="#" target="_blank">详情</a>
                                        <c:if test="${entry.status!=2}"><a href="#" target="_blank">修改</a></c:if>
                                        <c:if test="${entry.status!=2}"><a onclick="del('${entry.uuid}')" href="javascript:void(0)">删除</a></c:if>
                                        <c:if test="${entry.status==1}"><a onclick="publishControl('${entry.uuid}')" href="javascript:void(0)">发布</a></c:if>
                                        <c:if test="${entry.status==2}"><a onclick="delFlag('${entry.uuid}')" href="javascript:void(0)">清空</a></c:if>
                                    </p>
                                    </dd>
                                </dl>
                                </td>
                              </tr>
                         </c:forEach>
                  </tbody>
                  </table>
                  </c:forEach>
               </div>
                <!--酒店列表结束-->
                  <div class="pagination">
                        <dl>
                            <dt><input name="allChk" onclick="checkall(this)" type="checkbox">全选</dt>
                            <dd>
                                <a onclick="alldel()">批量删除</a>
                            </dd>
                        </dl>
                  </div>
					<!--分页部分开始-->
				<div class="pagination clearFix">${pageStr}</div>
					<!--分页部分结束-->
                <!--列表状态结束-->
               
                <!--右侧内容部分结束-->
</div>
</body>
</html>
