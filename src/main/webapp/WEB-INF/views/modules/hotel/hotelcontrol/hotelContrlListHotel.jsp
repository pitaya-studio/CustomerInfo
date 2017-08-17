<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>控房表--酒店列表</title>
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
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
	 //项目目录
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
	 $('#country').val("");
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
    $("#searchForm").attr("action","${ctx}/hotelControl/hotelControlList?showType=2&activityStatus="+status);
    $("#orderBy").val(sortBy);
    $("#searchForm").submit();
}
function changToDate(){
	//var status = ${activityStatus};
	//window.open(g_context_url+"/hotelControl/hotelControlList?showType=1&activityStatus="+status,'_self');
	window.open(g_context_url+"/hotelControl/hotelControlList?showType=1&activityStatus=3",'_self');
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
	var ids = [];
	$("[name="+$(obj).attr('name')+"]").each(function(){ids.push($(this).val())});
	v_deleteItems(ids);	
	//删除按钮的删除前确认
	/* if($("[name="+$(obj).attr('name')+"]:checkbox:checked").length>0){
		var ids = [];
		$("[name="+$(obj).attr('name')+"]:checkbox:checked").each(function(){ids.push($(this).val())});
		v_deleteItems(ids);
	}else{
		top.$.jBox.tip('请选择后进行删除操作','warning');
	} */
}
//根据传入的hotel_control_detail的uuid的值，处理status字段的状态
function v_deleteItems(ids){
	$.jBox.confirm("确定要删除数据吗？", "系统提示", function(v, h, f) {
		if (v == 'ok') {
			$.jBox.tip("正在删除数据...", 'loading');
			$.post( "${ctx}/hotelControl/delete", {"uuids":ids.join(",")}, 
				function(data){
					if(data.result=="1"){
// 						$.jBox.prompt("删除成功!", 'success', 'info', { closed: function () { $("#searchForm").submit(); } });
						$("#searchForm").submit();
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
			$.post( "${ctx}/hotelControl/deleteFlag", {"uuid":uuid}, 
				function(data){
					if(data.result=="1"){
						//$.jBox.prompt("清空成功!", 'success', 'info', { closed: function () { $("#searchForm").submit(); } });
						$("#searchForm").submit();
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
			$.post( "${ctx}/hotelControl/hotelControlDetailRelease/"+detailUuid,  
				function(data){
					if(data.result=="success"){
						//$.jBox.prompt("发布成功!", 'success', 'info', { closed: function () { $("#searchForm").submit(); } });
						 $("#searchForm").submit();
					}else{
						top.$.jBox.tip(data.message,'warning');
					}
				}
			);
			
		} else if (v == 'cancel') {
                
		}
	});
}

//停用   add by hhx 2015-08-03
function updateControlStatus(detailUuid,status){
    var msg = "";
    if(status=='2'){
    	msg = "恢复";
    }else if(status=="4"){
    	msg = "停用";
    }
	$.jBox.confirm("确定要"+msg+"吗？", "系统提示", function(v, h, f) {
		if (v == 'ok') {
			$.jBox.tip("正在"+msg+"数据...", 'loading');
			$.post( "${ctx}/hotelControl/updateHotelControlDetailStatus/"+detailUuid,{status:status},  
				function(data){
					if(data.result=="success"){
						//$.jBox.prompt(msg+"成功!", 'success', 'info', { closed: function () { $("#searchForm").submit(); } });
						$("#searchForm").submit();
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
.activitylist_bodyer_table{ width:100%; margin-top:10px;  border-right:1px solid #cccccc; *border-bottom:1px solid #ddd;border-top:1px solid #ddd;}
.hotel_list_n_all{ display:inline-block; line-height:35px; float:left; font-weight:normal;}
.hotel_list_n_all td{ padding:0px; margin:0px;}
.hotel_list_n{padding-right:20px;}
#pad_mar_none td{ padding:0px; margin:0px;}

.list_date_hotel{font-weight: bold;
padding: 0px 5px;
line-height: 30px;
float:left;}
.list_date_hotel .date{ width: 18px; height: 18px; float: left; margin-top: 6px; background-attachment: scroll; background-color: transparent; background-image: url(${ctxStatic }/images/ico_list01.png); background-repeat: no-repeat; background-position: 0px 0px; display: inline-block; cursor: pointer; }
.list_date_hotel .date2{ width: 18px; height: 18px; float: left; margin-top: 6px; background-attachment: scroll; background-color: transparent; background-image: url(${ctxStatic }/images/ico_list01.png); background-repeat: no-repeat; background-position: -25px 0px; display: inline-block; cursor: pointer; }
.list_date_hotel .hotel{ width: 18px; height: 18px; float: left; margin-top:10px; background-attachment: scroll; background-color: transparent; background-image: url(${ctxStatic }/images/ico_list01.png); background-repeat: repeat; background-position: 0px -29px; display: inline-block;  cursor:pointer; }
.list_date_hotel .hotel2{ width: 18px; height: 18px; float: left; margin-top:10px; background-attachment: scroll; background-color: transparent; background-image: url(${ctxStatic }/images/ico_list01.png); background-repeat: repeat; background-position: -25px -29px; display: inline-block;  cursor:pointer; }
/*.t_table_nohover{width: 100%;
margin-top: 10px;
border-right: 1px solid #CCC;
border-top: 1px solid #DDD;}*/

</style>

</head>
<body>
                <page:applyDecorator name="hotel_control" ></page:applyDecorator>
            	<!--右侧内容部分开始-->
               	<%@ include file="/WEB-INF/views/modules/hotel/hotelcontrol/searchBarInclude.jsp"%>
                <!--列表状态开始-->
                 <!--查询结果筛选条件排序开始-->
                    <div class="pagination">
                        <dl>
                            <dt><input name="allChk" onclick="checkall(this)" type="checkbox">全选</dt>
                            <dd>
                                <c:choose>
                            		<c:when test="${activityStatus == 2 }">
                                		<a onclick="allClear()">批量清空</a>
                            		</c:when>
                            		<c:when test="${activityStatus == 0 }">
                                		<a onclick="allStop('4')">批量停用</a>
                            		</c:when>
                            		<c:otherwise>
                            			<a onclick="alldel()">批量删除</a>
                            		</c:otherwise>
                            	</c:choose>
                            </dd>
                        </dl>
                        <div class="filter_btn fl mar_l20"><a class="btn btn-primary" target="_blank" href="${ctx}/hotelControl/tosavehotelcontrol">新增控房</a> </div>  
                    </div>	
				<div class="filterbox">
                    <div class="list_date_hotel"><i class="date2" onclick="changToDate()"></i>日期列表</div>
                    <div class="list_date_hotel"><i class="hotel" ></i>酒店列表</div>
					<div class="filter_num">查询结果<strong>${count }</strong>条</div>
					<div class="filter_sort"><span>表单排序：</span>
						<c:choose>
					<c:when test="${orderBy=='' || orderBy==null}">
						<a onclick="cantuansortby('hc.id',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('hc.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('stock',this)">现有库存<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='hc.id DESC'}">
						<a onclick="cantuansortby('hc.id',this)">创建时间<i
							class="i_sort  i_sort_down"></i></a>
						<a onclick="cantuansortby('hc.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('stock',this)">现有库存<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='hc.id ASC'}">
						<a onclick="cantuansortby('hc.id',this)">创建时间<i
							class="i_sort  i_sort_up"></i></a>
						<a onclick="cantuansortby('hc.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('stock',this)">现有库存<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='hc.updateDate DESC'}">
						<a onclick="cantuansortby('hc.id',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('hc.updateDate',this)">更新时间<i
							class="i_sort  i_sort_down"></i></a>
						<a onclick="cantuansortby('stock',this)">现有库存<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='hc.updateDate ASC'}">
						<a onclick="cantuansortby('hc.id',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('hc.updateDate',this)">更新时间<i
							class="i_sort  i_sort_up"></i></a>
						<a onclick="cantuansortby('stock',this)">现有库存<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='stock DESC'}">
						<a onclick="cantuansortby('hc.id',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('hc.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('stock',this)">现有库存<i
							class="i_sort  i_sort_down"></i></a>
					</c:when>
					<c:when test="${orderBy=='stock ASC'}">
						<a onclick="cantuansortby('hc.id',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('hc.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('stock',this)">现有库存<i
							class="i_sort  i_sort_up"></i></a>
					</c:when>
				</c:choose>
					</div>
				</div>
				<!--查询结果筛选条件排序结束-->	
                <div class="house_list_out"></div>	
                <!--日期列表结束-->
                <!--酒店列表开始-->
                <div class="house_list_out02" >
                   <table class="table activitylist_bodyer_table t_table_nohover"  id="contentTable pad_mar_none">
                    <thead>
                        <tr>
                            <th width="4%">序列</th>
                            <th width="6%">入住日期</th>
                            <th width="6%">地接供应商</th>
                            <th width="6%">采购类型</th>
                            <th width="9%">航班</th>
                            <th width="9%">房型 * 晚数</th>
                            <th width="8%">餐型</th>
                            <th width="8%">上岛方式</th>
							<th width="6%">现有库存</th>
						 	<th width="8%">房间总价</th>
                            <th width="6%">状态</th>
                            <th width="6%">发布人</th>
                            <th width="6%">更新人</th>
                            <th width="7%">创建时间<br />更新时间</th>
                            <th width="7%">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                      <c:if test="${empty page.list}">
						<tr class="toptr">
							<td colspan="15" style="text-align: center;">暂无相关信息</td>
						</tr>
			      	  </c:if>
                    </tbody>
                   </table>
                    <c:forEach items="${page.list }" var="record" varStatus = "s">
                    <table   class="table activitylist_bodyer_table house_35" id="house_h35">
                          <thead>
	                            <tr>
		                            <th  colspan="15">
		                            <div class="hotel_list_n_all">
										<input type="checkbox" onclick="subcheckall(this)" name="ids" value="${record.uuid}" id="${record.uuid}">
										<span class="hotel_list_n">酒店：<em><trekiz:autoId2Name4Table tableName="hotel" sourceColumnName="uuid" srcColumnName="name_cn" value="${record.hotelUuid}"/></em></span>
										<span class="hotel_list_n"> 岛屿：<em><trekiz:autoId2Name4Table tableName="island" sourceColumnName="uuid" srcColumnName="island_name" value="${record.islandUuid}"/></em></span>
										<span class="hotel_list_n"> 控房名称：<em>${record.name}</em></span>
									</div> 
		                            <p><a href="${ctx}/hotelControl/tosavehotelcontrol?flag=1&uuid=${record.uuid}" target="_blank">新增日期</a> | <a href="${ctx}/hotelControl/show/${record.uuid}/1" target="_blank">详情</a> | <a href="${ctx}/hotelControl/editHotelControl/editForHotel/${record.uuid}" target="_blank">修改</a> | <a href="${ctx}/hotelPl/list?hotelUuid=${record.hotelUuid}" target="_blank">查看价单</a> | <a href="javascript:void(0)" name="${record.uuid}" onclick="suballdel(this)">删除</a></p>
		                            </th>
	                            </tr>
                         </thead>
                    <tbody>
                          <c:forEach items="${hotelList[s.index]}" var="entry" varStatus = "vs">
		                          <c:set var="rowspanNum" value="1" />
		                          <c:set var="total" value="${entry.room}" />
									<c:if test="${total != null && fn:length(fn:split(total,',')) > 0}">
										<c:set var="rowspanNum" value="${fn:length(fn:split(total,','))}" />
									</c:if>
                              <tr>
                                <td width="4%" class="tc" rowspan="${rowspanNum}">
                                	<input type="checkbox" onclick="canparechecon(this)" name="${record.uuid}" value="${entry.uuid}" sign="uuids" />
                                 	${vs.index+1}
                                 </td>
                                <td width="6%" class="tc" rowspan="${rowspanNum}">
	                                <fmt:formatDate value="${entry.inDate}"
									pattern="yyyy-MM-dd" />
								</td>
								<td width="6%" class="tc" rowspan="${rowspanNum}">
								<trekiz:autoId2Name4Table tableName='supplier_info' sourceColumnName='id' srcColumnName='supplierName' value='${record.ground }'/>

								</td>
								<td width="6%" class="tc" rowspan="${rowspanNum}">
									<c:choose>
										<c:when test="${record.purchase==0 }">内采</c:when>
										<c:when test="${record.purchase==1 }">外采</c:when>
									</c:choose> 
								</td>
                                <td width="9%"  class="tc" rowspan="${rowspanNum}">
	                                <c:set value=" ${entry.airLine}" var="airlineNames" />
									<c:forEach items="${fn:split(airlineNames, ';')}" var="air" >
									    ${air}<br />
									</c:forEach>
                                </td>
                                <%-- 房型*晚数 --%>
                                <td width="9%" class="tc">
                                    <c:set value=" ${entry.room}" var="roomTypes" />
                                    <c:set value=" ${entry.night}" var="nightNo" />
                                    <c:forEach items="${fn:split(roomTypes, ',')}" var="room" varStatus="i" end="0">
	                                  <p>
		                                  <trekiz:autoId2Name4Table tableName="hotel_room" sourceColumnName="uuid" srcColumnName="room_name" value="${fn:trim(room)}"/>
		                                  *${fn:split(nightNo, ',')[i.index]}晚
	                                  </p>
	                                </c:forEach>
                                </td>
                                <%-- 餐型  --%>
                                <td width="8%" class="tc">
                                <c:set value="${entry.hotelMeals}" var="meals" />
                                <c:set value="${fn:split(meals, ',')}" var="roommeals" />
	                                <c:forEach items="${fn:split(roommeals[0], ';')}" var="roommeal" >
		                                <p>
		                                <trekiz:autoId2Name4Table tableName="hotel_meal" sourceColumnName="uuid" srcColumnName="meal_name" value="${roommeal}"/>
		                                </p>
	                                </c:forEach>
                                </td>
                                <td width="8%" class="tc" rowspan="${rowspanNum}">
                                <trekiz:autoId2Name4Table tableName="sys_company_dict_view" sourceColumnName="uuid" srcColumnName="label" value="${fn:trim(entry.islandWay)}"/>
                                </td>
                                <td width="6%" class="tc" rowspan="${rowspanNum}"
                                  <c:if test="${entry.stock<=2}">tdred</c:if>
                                >${entry.stock}</td>
                             	<td width="8%" class="tc" rowspan="${rowspanNum}">
								    <p><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${entry.currencyId}"/>
                             		<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${entry.totalPrice}" />
                             	</td>
                                
                                <td width="6%" class="tc" rowspan="${rowspanNum}">
                                	<c:if test="${entry.status=='0'}">已生效</c:if>
									<c:if test="${entry.status=='1'}">保存草稿</c:if>
									<c:if test="${entry.status=='2'}">已删除</c:if>
									<c:if test="${entry.status=='4'}">已停用</c:if>
                                </td>
                                <td width="6%" class="tc" rowspan="${rowspanNum}">${fns:getUserById(entry.createBy).name}</td>
                                <td width="6%" class="tc" rowspan="${rowspanNum}">${fns:getUserById(entry.updateBy).name}</td>
                                <td width="7%" class="tc" rowspan="${rowspanNum}"><fmt:formatDate value="${entry.createDate}"
									pattern="yyyy-MM-dd HH:mm" /><br />
                                 <fmt:formatDate
									value="${entry.updateDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                                <td width="5%" class="p0" rowspan="${rowspanNum}">
                                <dl class="handle">
                                    <dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
                                    <dd class="">
                                    <p>
                                        <span></span>
                                        <c:if test="${entry.status!=2}"><a href="${ctx}/hotelControl/editHotelControl/editForDate/${entry.uuid}" target="_blank">修改</a></c:if>                                           
                                        <c:if test="${entry.status!=2&&entry.status!=0}"><a onclick="del('${entry.uuid}')" href="javascript:void(0)">删除</a></c:if>           
                                        <c:if test="${entry.status==1}"><a onclick="publishControl('${entry.uuid}')" href="javascript:void(0)">发布</a></c:if>
                                        <c:if test="${entry.status==2}"><a onclick="delFlag('${entry.uuid}')" href="javascript:void(0)">清空</a></c:if>  
                                        <!-- add by hhx 新增停用状态4:删除状态恢复为停用状态，停用状态发布为提交状态    -->  
                                        <c:if test="${entry.status==0}"><a onclick="updateControlStatus('${entry.uuid}','4')" href="javascript:void(0)">停用</a></c:if>  
                                        <c:if test="${entry.status==2}"><a onclick="updateControlStatus('${entry.uuid}','2')" href="javascript:void(0)">恢复</a></c:if>  
                                        <c:if test="${entry.status==4}"><a onclick="publishControl('${entry.uuid}')" href="javascript:void(0)">发布</a></c:if>
                                         
                                        <a href="${ctx}/hotelControl/show/${entry.uuid}/2" target="_blank">详情</a>
                                    </p>
                                    </dd>
                                </dl>
                                </td>
                              </tr>
                             <c:forEach items="${fn:split(roomTypes, ',')}" var="room" begin="1"  varStatus = "v">
								 <tr>
		                            <td class="tc">
		                            	<p>
		                                  <trekiz:autoId2Name4Table tableName="hotel_room" sourceColumnName="uuid" srcColumnName="room_name" value="${fn:trim(room)}"/>
		                                  *${fn:split(nightNo, ',')[v.index]}晚
	                                  </p>
		                            </td>
		                            <td class="tc">
		                                <c:forEach items="${fn:split(roommeals[v.index], ';')}" var="hotelmeals" >
											<p><trekiz:autoId2Name4Table tableName="hotel_meal" sourceColumnName="uuid" srcColumnName="meal_name" value="${fn:trim(hotelmeals)}"/></p>
										</c:forEach>
		                            </td>
		                        </tr>
					        </c:forEach>
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
                                <c:choose>
                            		<c:when test="${activityStatus == 2 }">
                                		<a onclick="allClear()">批量清空</a>
                            		</c:when>
                            		<c:when test="${activityStatus == 0 }">
                                		<a onclick="allStop('4')">批量停用</a>
                            		</c:when>
                            		<c:otherwise>
                            			<a onclick="alldel()">批量删除</a>
                            		</c:otherwise>
                            	</c:choose>
                            </dd>
                        </dl>
                  </div>
              
					<!--分页部分开始-->
					<div class="pagination clearFix">${pageStr}</div>
					<!--分页部分结束-->
                <!--列表状态结束-->
               
                <!--右侧内容部分结束-->
</body>
</html>
