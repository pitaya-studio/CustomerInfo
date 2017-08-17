<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>机票库存表--日期列表</title>
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
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

});
//删除产品的提示信息
function confirmxDel(mess,proId){
	top.$.jBox.confirm(mess,'系统提示',function(v){
	if(v=='ok'){
		//loading('正在提交，请稍等...');
		}
	},{buttonsFocus:1});
	return false;
}
//全选
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
//改变status的状态值
function del(id){
	var ids = [];
	ids.push(id);
	v_deleteItems(ids);
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
//排序
function flightContrlListDateSorts(sortBy,obj){
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
	    $("#searchForm").attr("action","${ctx}/flightControl/flightControlList?showType=1&activityStatus="+status);
	    $("#orderBy").val(sortBy);
	    $("#searchForm").submit();
}
//条件重置
var resetSearchParams = function(){
	$(':input','#searchForm')
	 .not(':button, :submit, :reset, :hidden')
	 .val('')
	 .removeAttr('checked')
	 .removeAttr('selected');
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
.filterbox{
	margin-top:56px;
	margin-bottom:10px;
}
</style>

</head>
<body>
 	<page:applyDecorator name="flight_control" />
  	<%@ include file="/WEB-INF/views/modules/flight/flightcontrol/searchBarInclude.jsp"%>
  <script type="text/javascript">
				
				$("select[name='country']" ).combobox();
				$("select[name='islands']" ).combobox();
				$("select[name='tways']" ).combobox();
				$("select[name='flights']" ).combobox();
				$("select[name='roomtype']" ).combobox();
				$("select[name='roomnum']" ).combobox();
				$("select[name='hoteljituan']" ).combobox();
				$("select[name='hotelname']" ).combobox();
				$("select[name='foodtype']" ).combobox();
				
				//切换 日期列表 与 酒店列表
				function changeToHotel(){
					window.open("${ctx}/flightControl/flightControlList?showType=2&activityStatus=3",'_self');
				}
	</script>
	 <!--列表状态开始-->
                
                 <!--查询结果筛选条件排序开始-->
                    <div class="pagination">
                        <dl>
                            <dt><input name="allChk" onclick="checkall(this)" type="checkbox">全选</dt>
                            <dd>
								<%--UG_V2 按钮样式统一--%>

							<%--<a onclick="alldel()">批量删除</a>--%>
								<input class="btn ydbz_x" type="button" value="批量删除" onclick="alldel()">
                            </dd>
                        </dl>
                        <%--<div class="filter_btn fl mar_l20"><a class="btn btn-primary" target="_blank" href="${ctx }/flightControl/tosaveflightcontrol">新增机票库存</a> </div>--%>
                    </div>	
				<div class="filterbox">
                    <div class="list_date_hotel"><i class="date"></i>日期列表</div>
                    <div class="list_date_hotel"><i class="hotel2" onclick="changeToHotel()"></i>航班列表</div>
					<div class="filter_num">查询结果<strong>${count }</strong>条</div>
					<div class="filter_sort"><span>表单排序：</span>
					<c:choose>
						<c:when test="${orderBy=='' || orderBy==null}">
							<a onclick="flightContrlListDateSorts('fcd.id',this)">创建时间<i 
								class="i_sort"></i></a>
							<a onclick="flightContrlListDateSorts('fcd.updateDate',this)">更新时间<i 
								class="i_sort"></i></a>
	                        <a onclick="flightContrlListDateSorts('fcd.price',this)">价格<i 
	                        	class="i_sort"></i></a>
							<a onclick="flightContrlListDateSorts('fcd.stock',this)">库存<i 
								class="i_sort"></i></a>
						</c:when>
						<c:when test="${orderBy=='fcd.id DESC'}">
							<a onclick="flightContrlListDateSorts('fcd.id',this)">创建时间<i class="i_sort i_sort_down"></i></a>
							<a onclick="flightContrlListDateSorts('fcd.updateDate',this)">更新时间<i class="i_sort"></i></a>
	                        <a onclick="flightContrlListDateSorts('fcd.price',this)">价格<i class="i_sort"></i></a>
							<a onclick="flightContrlListDateSorts('fcd.stock',this)">库存<i class="i_sort"></i></a>
						</c:when>
						<c:when test="${orderBy=='fcd.id ASC'}">
							<a onclick="flightContrlListDateSorts('fcd.id',this)">创建时间<i class="i_sort i_sort_up"></i></a>
							<a onclick="flightContrlListDateSorts('fcd.updateDate',this)">更新时间<i class="i_sort"></i></a>
	                        <a onclick="flightContrlListDateSorts('fcd.price',this)">价格<i class="i_sort"></i></a>
							<a onclick="flightContrlListDateSorts('fcd.stock',this)">库存<i class="i_sort"></i></a>
						</c:when>
						<c:when test="${orderBy=='fcd.updateDate DESC'}">
							<a onclick="flightContrlListDateSorts('fcd.id',this)">创建时间<i class="i_sort"></i></a>
							<a onclick="flightContrlListDateSorts('fcd.updateDate',this)">更新时间<i class="i_sort i_sort_down"></i></a>
	                        <a onclick="flightContrlListDateSorts('fcd.price',this)">价格<i class="i_sort"></i></a>
							<a onclick="flightContrlListDateSorts('fcd.stock',this)">库存<i class="i_sort"></i></a>
						</c:when>
						<c:when test="${orderBy=='fcd.updateDate ASC'}">
							<a onclick="flightContrlListDateSorts('fcd.id',this)">创建时间<i class="i_sort"></i></a>
							<a onclick="flightContrlListDateSorts('fcd.updateDate',this)">更新时间<i class="i_sort i_sort_up"></i></a>
	                        <a onclick="flightContrlListDateSorts('fcd.price',this)">价格<i class="i_sort"></i></a>
							<a onclick="flightContrlListDateSorts('fcd.stock',this)">库存<i class="i_sort"></i></a>
						</c:when>
						<c:when test="${orderBy=='fcd.price DESC'}">
							<a onclick="flightContrlListDateSorts('fcd.id',this)">创建时间<i class="i_sort"></i></a>
							<a onclick="flightContrlListDateSorts('fcd.updateDate',this)">更新时间<i class="i_sort"></i></a>
	                        <a onclick="flightContrlListDateSorts('fcd.price',this)">价格<i class="i_sort i_sort_down"></i></a>
							<a onclick="flightContrlListDateSorts('fcd.stock',this)">库存<i class="i_sort"></i></a>
						</c:when>
						<c:when test="${orderBy=='fcd.price ASC'}">
							<a onclick="flightContrlListDateSorts('fcd.id',this)">创建时间<i class="i_sort"></i></a>
							<a onclick="flightContrlListDateSorts('fcd.updateDate',this)">更新时间<i class="i_sort"></i></a>
	                        <a onclick="flightContrlListDateSorts('fcd.price',this)">价格<i class="i_sort i_sort_up"></i></a>
							<a onclick="flightContrlListDateSorts('fcd.stock',this)">库存<i class="i_sort"></i></a>
						</c:when>
						<c:when test="${orderBy=='fcd.stock DESC'}">
							<a onclick="flightContrlListDateSorts('fcd.id',this)">创建时间<i class="i_sort"></i></a>
							<a onclick="flightContrlListDateSorts('fcd.updateDate',this)">更新时间<i class="i_sort"></i></a>
	                        <a onclick="flightContrlListDateSorts('fcd.price',this)">价格<i class="i_sort"></i></a>
							<a onclick="flightContrlListDateSorts('fcd.stock',this)">库存<i class="i_sort i_sort_down"></i></a>
						</c:when>
						<c:when test="${orderBy=='fcd.stock ASC'}">
							<a onclick="flightContrlListDateSorts('fcd.id',this)">创建时间<i class="i_sort"></i></a>
							<a onclick="flightContrlListDateSorts('fcd.updateDate',this)">更新时间<i class="i_sort"></i></a>
	                        <a onclick="flightContrlListDateSorts('fcd.price',this)">价格<i class="i_sort"></i></a>
							<a onclick="flightContrlListDateSorts('fcd.stock',this)">库存<i class="i_sort i_sort_up"></i></a>
						</c:when>
					</c:choose>
						
						<!--<a>房间库存<i class="i_sort"></i></a>
						<a>岛屿名称<i class="i_sort"></i></a>
						<a>酒店名称<i class="i_sort"></i></a>-->
						<!--正序 class="i_sort i_sort_up"-->
						<!--倒序 class="i_sort i_sort_down"-->
					</div>
				</div>
				<!--查询结果筛选条件排序结束-->	
                <div class="house_list_out">
                <!--日期列表开始-->
                <table id="contentTable house_h35" class="table activitylist_bodyer_table mainTable " >
                    <thead>
                        <tr>
                            <th width="4%">序列</th>
                            <th width="6%">日期</th>
                            <th width="8%">国家</th>
                            <th width="7%">航空公司</th>
                            <th width="7%">舱位等级</th>
                            <th width="7%">价格</th>
                            <th width="6%">总税</th>
                            <th width="6%">库存</th>
							<th width="5%">参考酒店</th>
						 	<th width="5%">状态</th>
                            <th width="6%">发布人</th>
                            <th width="6%">更新人</th>
                            <th width="11%">创建时间<br />更新时间</th>
                            <th width="5%">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                    	<c:forEach items="${page.list }" var="record" varStatus="vStatus">
						<tr>
                        	<td class="tc">
                        		<input class="house_h35_checkbox02" type="checkbox" onclick="idcheckchg(this)" name="activityId" value="${record.uuid }" sign="uuids">
                        		${vStatus.index+1 }
                        	</td>
                            <td class="tc">
                            	<fmt:formatDate value="${record.departureDate }" pattern="yyyy-MM-dd" />
                            </td>
                            <td class="tc"><trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${record.country }"/></td>
                            <td class="tc">
                            	${fns:getAirlineNameByAirlineCode(record.airline)}
                            </td>
                            <td class="tc">
                            	${fns:getDictLabel(record.spaceGradeType,"spaceGrade_Type" , "无")}
                            </td>
                            <td class="tc">
                            	<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${record.priceCurrencyId}"/>
                            	${record.price }</td>
                            <td class="tc">${record.taxesCurrencyId } ${record.taxesPrice }</td>
							<td class="tc">${record.stock }</td>
                            <td class="tc">
                            	<c:forEach items="${fn:split(record.hotelUuid,',') }" var="str">
                            		<trekiz:autoId2Name4Table tableName="hotel" sourceColumnName="uuid" srcColumnName="short_name_cn" value="${str }"/><br/>
                            	</c:forEach>
                            </td>
                            <td class="tc tdgreen">
                            	<c:if test="${record.STATUS=='0'}">已提交</c:if>
								<c:if test="${record.STATUS=='1'}">保存草稿</c:if>
								<c:if test="${record.STATUS=='2'}">已删除</c:if>
                            </td>
                            <td class="tc">${record.createBy }</td>
                            <td class="tc">${record.updateBy }</td>
                            <td class="tc">
                            	<fmt:formatDate value="${record.createDate }" pattern="yyyy-MM-dd hh:mm"/><br />
                            	<fmt:formatDate value="${record.updateDate }" pattern="yyyy-MM-dd hh:mm"/>
                            </td>
                            <td class="p0">
                                <dl class="handle">
                                    <dt><img title="操作" src="${ctxStatic }/images/handle_cz_rebuild.png"></dt>
                                    <dd class="">
                                    <p>
                                        <span></span>
                                        <a href="${ctx }/flightControl/edit/${record.uuid }" target="_blank">修改</a>
                                        <a href="${ctx }/flightControl/show/${record.uuid }/1" target="_blank">详情</a>
                                        <!--<a href="#" target="_blank">预报名</a>-->
                                         <a onclick="del('${record.uuid}')" href="javascript:void(0)">删除</a>
                                    </p>
                                    </dd>
                                </dl>
                            </td>
                        </tr>
                        </c:forEach>
                    </tbody>
                </table>
                </div>	
                <!--日期列表结束-->
                 <!--酒店列表结束-->
                    <div class="pagination">
                        <dl>
                            <dt><input name="allChk" onclick="checkall(this)" type="checkbox">全选</dt>
                            <dd>
                              	<%--<a onclick="alldel()">批量删除</a>--%>
								<input class="btn ydbz_x" type="button" value="批量删除" onclick="alldel()">

							</dd>
                        </dl>
                  </div>
              
					<!--分页部分开始-->
					<div class="pagination clearFix">${pageStr}</div>
					<!--分页部分结束-->
                <!--列表状态结束-->
                
</body>
</html>

