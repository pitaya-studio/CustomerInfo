<%@page import="com.trekiz.admin.common.persistence.Page"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
    <title>预定-签证产品预定列表</title>
    <style type='text/css'>
		.ui-front { z-index: 2100; }
	</style>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/modules/visa/visaProductsList.js"></script>
<!-- C366 -->
<%--bug16688 S--%>
    <style>
        .add_allactivity label{
            width: 60px;
            text-align: right;
        }
        .add_allactivity {
            padding: 22px 0 0 49px;
        }
        .add_allactivity .custom-combobox-input{
            margin-right: 0;
        }
    </style>
    <%--bug16688 E--%>

<script type="text/javascript">

	var countryId = ""; //有用不许删掉
	var yuejianxingzong = '7a81b21a77a811e5bc1e000c29cf2586';  // 越柬行踪 uuid

$(function(){
		//modified for UG_V2 搜索条件筛选 by tlw at 20170302
		launch();
		
		show('${flag}');
		//产品名称文本框提示信息
		inputTips();
		//操作浮框
		operateHandler();
		
	   $("#sysCountryId" ).comboboxInquiry();
	  
	   $("#collarZoning" ).comboboxInquiry();
	   $("#sysCountryId").next().find(".custom-combobox-input").blur(function(){
			if(countryId != $("#sysCountryId").val()) {
				countryId = $("#sysCountryId").val();
				getArea();
			}
		});
	
});
function show(flag){
	//alert(isSearchConditionNotUsed());
 	if("zhankai"== flag&&!isSearchConditionNotUsed()){
		document.getElementById("showFlag").value="zhankai";
		if($('.ydxbd').is(":hidden")==true) {
			document.getElementById("showFlag").value="zhankai";
			$('.ydxbd').show();
//			$('.zksx').text('收起筛选');
			$('.zksx').addClass('zksx-on');
		}
	} else{
		document.getElementById("showFlag").value="shouqi";
		$('.ydxbd').hide();
//		$('.zksx').text('展开筛选');
		$('.zksx').removeClass('zksx-on');
	}
}	

function isSearchConditionNotUsed(){
	//visaType
	//sysCountryId
	//collarZoning
	var visaType = $('#visaType').val();
	var sysCountryId = $('#sysCountryId').val();
	var collarZoning = $('#collarZoning').val();
	
	if(" "==visaType&&" "==sysCountryId&&" "==collarZoning){
		return true;
	}else{
		return false;
	}
}


//国家、领区联动
function getArea() {
	//alert("ddddddddddddd");
	if(countryId != '') {
		$.ajax({
			type:"POST",
			url:"${ctx}/visa/preorder/getArea?countryId="+countryId,
			async : false,
			success:function(result){
				$("#collarZoning").empty().append('<option value=" ">请选择</option>');
				if(result != null && result.length != 0) {
					for(var index = 0; index < result.length; index++) {
						$("#collarZoning").append('<option value=\'' + result[index][0] + '\'>' + result[index][1] + '</option>');
					}
				}
				$("#collarZoning" ).comboboxInquiry({
					"afterInvalid":function(event,data){
						$(this).trigger("click");
					}
				});
			}
		});
	}else if(countryId == ''){
		$("#collarZoning").empty().append('<option value=" ">请选择</option>');
	}
}



 var orderBy = null;
 
       	   
 function sortby(sortBy,obj){
	           var temporderBy = $("#orderBy").val();
	           if(temporderBy.match(sortBy)){
	               sortBy = temporderBy;
	               if(sortBy.match(/ASC/g)){
	                   sortBy = sortBy.replace(/ASC/g,"");
	               }else{
	                   sortBy = $.trim(sortBy)+" ASC";
	               }
	           }
	           
	           $("#orderBy").val(sortBy);
	           $("#searchForm").attr("action","${ctx}/visa/preorder/list.htm");
	           $("#searchForm").submit();
	       }
            

function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/visa/preorder/list.htm");
			$("#searchForm").submit();
}
		
var visaProductIds = "";
visaProductIds = "${visaProductIds}";
	
//增加渠道商链接
function addAgentinfo(){
	//移除弹窗（选择渠道和付款方式）
	window.parent.window.jBox.close();
	
	window.open(contextPath+"/agent/manager/firstForm");
}
	
//选择渠道弹出框
function agentType(objId,reserveMethods,companyUUid){
    //渠道选择
    var _agentSelect = $("#agentIdSel").clone();
     _agentSelect.attr("id", "agentIdSelCl");//C366
     $(_agentSelect).addClass("ui-front");//C366
    $(_agentSelect).addClass("agentSelected");
    _agentSelect.show();
    
    //给点击的按钮添加指定的标记，方便找到对应的触发按钮
    $("#banqian_"+objId).addClass("sign");
    //添加渠道商
	var addAgentinfoHtml = "<input class='btn btn-primary' type='button' onclick='addAgentinfo()' value='新增渠道' style='width:100px;height:30px;margin-left:20px'>";
    //弹出框
	var isAddAgent = $('#isAddAgent').val();
	if(isAddAgent == 1){
		var $div = $("<div class=\"tanchukuang\"></div>")
    .append($('<div class="add_allactivity"><label><span class="xing">*</span>渠道：</label>').append(_agentSelect).append(addAgentinfoHtml));
	}else{
		var $div = $("<div class=\"tanchukuang\"></div>")
    .append($('<div class="add_allactivity"><label><span class="xing">*</span>渠道：</label>').append(_agentSelect));
	}
    
    //处理渠道跟进销售
    var html = $div.html();	
    var channelSalerName = $("#agentIdSel").find("option:selected").attr("salerName");
	var channelSalerId = $("#agentIdSel").find("option:selected").attr("salerid");
    
    //跟进渠道名称
    if("-1"==$("#agentIdSel").val()){
    	html += '<div class="add_allactivity" id="channelSaler" style="display:none;">';
    }else{
    	html += '<div class="add_allactivity" id="channelSaler">';
    }
    html += '<label>销售：</label>' + '<select name="salerId"><option value="'+channelSalerId+'">'+channelSalerName+'</option></select>';
    html += '</div>';
    
    
    //非签销售选择
    var _salerIdSel = $("#salerIdSel").clone();
    $(_salerIdSel).addClass("salerIdSelected");
    if("-1"==$("#agentIdSel").val()){
    	 if(companyUUid && companyUUid == '33ab2de5fdc842caba057296b28f5bae'){
    	   	 $(_salerIdSel).attr("disabled","disabled"); 
    	 }
    	 html +='<div class="add_allactivity" id="notchannelSaler" >';
         html +='<label>销售：</label>';
//    	 $divsaler = _salerIdSel;
    	 html += _salerIdSel[0].outerHTML;
    	 html += '</div>';
    }else{
    	 html +='<div class="add_allactivity" id="notchannelSaler" style="display:none;">';
//    	 $divsaler = $('<label style="margin-left:68px;">销售：</label>').append(_salerIdSel);
//    	 html += $divsaler.html();
        html +='<label>销售：</label>';
        html += _salerIdSel[0].outerHTML;
    	 html += '</div>';
    }
    
	//alert(reserveMethods+"");
    //预定和全款   在选择预定时才显示占位方
	if(((reserveMethods+"").length>2)){
			html += '<div class="add_allactivity">';
			html += '<label >付款方式：</label>';
			html += '<select name="payType" class="typeSelected">';
			html += '<option value="1">全款</option>';
			html += '<option value="3">预订</option>';
			html += '</select></div>';
	}else{
		 if((reserveMethods+"")==2){
				html += '<div class="add_allactivity">';
				html += '<label>付款方式：</label>';
				html += '<select name="payType"  class="typeSelected">';
				html += '<option value="1"  >全款</option>';
				html += '</select></div>';
		  }else{
				html += '<div class="add_allactivity">';
				html += '<label>付款方式：</label>';
				html += '<select name="payType"  class="typeSelected">';
				html += '<option value="3">预订</option>';
				html += '</select></div>';
		  }
	}
	
	var $pop=$.jBox(html, { title: "选择渠道和付款方式",buttons:{
		'取消':0,'预定':1
	},submit: function (v, h, f) {
		if(v==1){
			var paramSalerId = "";
			if(f.agentId == "-1") {
				paramSalerId = h.find("#notchannelSaler").find("select[name=salerId]").val();
			} else {
				paramSalerId = h.find("#channelSaler").find("select[name=salerId]").val();
			}
			document.location ="${ctx}/visa/preorder/createVisaOrder/" + objId +"/" + f.agentId + "/" + f.payType + "/" + paramSalerId;
		}
	},
	loaded: function (h) { 
		h.find('#agentIdSelCl').comboboxInquiry('showTitle');
	}, 
	height:300,width:500});
	
 	$("#agentIdSelCl").comboboxInquiry().on('comboboxinquiryselect', function(event, obj){
 		getSelectedAgentValue(this);
 	});
 	// 渠道选择下拉框的可输入框中，失去焦点事件：1.根据渠道获取销售 2.回显非签约渠道（由于控件使用特性，在粘贴前是否失去焦点，直接影响粘贴渠道全称能否正确被控件识别出粘贴内容是属于下拉数据的某一条。如果未被识别，则渠道依旧是非签约渠道）
	$("#agentIdSelCl").next().find("input").blur(function(){
		if($("#agentIdSelCl").val() == '-1'){
			if($("#companyUuid").val() == yuejianxingzong){					
				$("#agentIdSelCl").next().find("input").val("直客");
			} else {					
				$("#agentIdSelCl").next().find("input").val("非签约渠道");
			}
		}
		getSelectedAgentValue($("#agentIdSelCl"));
	});
}

function getSelectedAgentValue(obj){
	var selectedVal = $(obj).val();
	if("-1"==selectedVal){ // （尚未签约的渠道，这里叫做“直客”）
		$("#notchannelSaler").show();
		$("#channelSaler").hide();
	}else{  // 签约渠道
		$("#notchannelSaler").hide();
		$("#channelSaler").show();
		// 选中的签约渠道的跟进销售ids
		var salerIdStr = $(obj).find("option:selected").attr("salerId");
		var salerNameStr = $(obj).find("option:selected").attr("salerName");
		var salerIdArr = new Array();
		var salerNameArr = new Array();
		if(salerIdStr) {
// 			salerIdStr.indexOf(",") > 0
			salerIdArr = salerIdStr.split(",");
		}
		if(salerNameStr) {
			salerNameArr = salerNameStr.split(",");
		}
		var channelSalerName = $(obj).find("option:selected").attr("salerName");
		var _channelSalers = document.createElement("select");
		for ( var i = 0; i < salerIdArr.length; i++) {
			var newOpt = new Option(salerNameArr[i], salerIdArr[i]);
			_channelSalers.options.add(newOpt);
		}
		$(_channelSalers).attr("name", "salerId");
		$("#channelSaler").empty().append("<label>销售：</label>").append($(_channelSalers));
	}
}



</script>
</head>
<body>

<!-- 签证公告展示 -->
<%@ include file="/WEB-INF/views/modules/visa/visaPublicBulletinMarquee.jsp"%>
                               
<div class="xt-activitylist" style="display:none;">
	<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
	<select name="agentId" id="agentIdSel" onchange="getSelectedAgentValue(this);" >
          <c:if test="${fns:getUser().company.id ne 68 }">
              <c:choose>
              <c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586'}">
              	<option value="-1">未签</option>
              </c:when>
              <c:otherwise>
	              <c:if test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}"> 
	       				<option value="-1">直客</option>
	  				 </c:if>
	              	<c:if test="${fns:getUser().company.uuid ne  '7a81b21a77a811e5bc1e000c29cf2586'}"> 
	       				<option value="-1">非签约渠道</option>
	  				 </c:if>
              </c:otherwise></c:choose>
          </c:if>
		<c:forEach var="agentinfo" items="${agentinfoList}">
			<c:set var="salerMap" value="${fns:getSalersFromIdStr(agentinfo.agentSalerId) }"></c:set>
			<c:choose>
				<c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'} && ${agentinfo.agentName eq '非签约渠道'}">
					<option value="${agentinfo.id}" salerId="${salerMap.salerIdStr }" salerName="${salerMap.salerNameStr }" >直客</option>
				</c:when>
				<c:otherwise>
					<option value="${agentinfo.id}" salerId="${salerMap.salerIdStr }" salerName="${salerMap.salerNameStr }" >${agentinfo.agentName }</option>
				</c:otherwise>
			</c:choose> 
		</c:forEach>
	</select>
</div>

<div class="xt-activitylist"  style="display:none;">
	<label>销售：</label>
	<select name="salerId"  class="typeSelected" id="salerIdSel">
		<c:forEach items="${fns:getVisaSaleUserList('1')}" var="userinfo">
			<!-- 用户类型  1 代表销售 -->
			<option value="${userinfo.id }"  <c:if test="${userinfo.id==userId}">selected="selected"</c:if>>${userinfo.name }</option>
		</c:forEach>
	</select>
</div>

<page:applyDecorator name="show_head">
    <page:param name="desc">签证产品</page:param>
</page:applyDecorator>
        <!--右侧内容部分开始-->
        <p class="main-right-topbutt"></p>
        <div class="activitylist_bodyer_right_team_co_bgcolor">
            <form:form id="searchForm" modelAttribute="visaProducts" action="${ctx}/visa/preorder/list.htm" method="post">

                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
                <input id="visaProductIds" type="hidden" name="visaProductIds" value="${visaProductIds}"/>
				<input id="departmentId" name="departmentId" type="hidden" value="${departmentId}" />                
                <input value=""  type="hidden" id="showFlag" name="showFlag"/>
                <shiro:hasPermission name="visa:book:addAgent">
					<input id="isAddAgent" name="isAddAgent" type="hidden" value="1" />
				</shiro:hasPermission>

                <div class="activitylist_bodyer_right_team_co">
                


                    <div class="activitylist_bodyer_right_team_co2 pr">
                        <input class="txtPro inputTxt searchInput"
                               id="productName"
                               name="productName"
                               value="${productName}"
                               type="text" placeholder="仅支持产品名称的搜索" />
                    </div>
                    <a class="zksx">筛选</a>
                    <div class="form_submit">
                        <input class="btn btn-primary ydbz_x" value="搜索" type="submit">
                        <%--<input type="reset" value="清空所有条件"class="btn ydbz_x" />--%>
                        <input class="btn ydbz_x" type="button"	onclick="resetSearchParams()" value="清空所有条件" />
                    </div>

                    <div class="ydxbd">
                        <span></span>
                    
                          <div class="activitylist_bodyer_right_team_co1">
                            <div class="activitylist_team_co3_text">签证国家：</div>
                            <select  name="sysCountryId" id="sysCountryId">
                                <option value=" ">所有</option>
                                <c:forEach items="${countryInfoList}" var="visaCountry">
                                    <option value="${visaCountry[0]}" <c:if test="${sysCountryId eq visaCountry[0]}"> selected="selected" </c:if> >${visaCountry[1]}</option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div class="activitylist_bodyer_right_team_co1">
                            <div class="activitylist_team_co3_text">领区：</div>
                            <select name="collarZoning"  id="collarZoning">
                                <option value=" ">所有</option>
                                <c:forEach items="${collarZoningList}" var="collarZonings">
                                    <option value="${collarZonings[0]}" <c:if test="${collarZoning eq collarZonings[0]}"> selected="selected" </c:if> >${collarZonings[1]}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="activitylist_bodyer_right_team_co1">
                            <div class="activitylist_team_co3_text">签证类型：</div>
                            <select  name="visaType" id="visaType">
                                <option value=" " >所有</option>
                                <c:forEach items="${visaTypeList}" var="visaTypevar">
                                    <option value="${visaTypevar.key}" 
                                      <c:if test="${visaType eq visaTypevar.key}">
                                        selected="selected"
                                    </c:if>  >
                                        <c:out value="${visaTypevar.value}" /></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="kong"></div>
                </div>
            </form:form>
            <c:if test="${fn:length(page.list) ne 0}">
                <div class="activitylist_bodyer_right_team_co_paixu">
                    <div class="activitylist_paixu">
                        <div class="activitylist_paixu_left">
                            <ul>
                                   <!--
                                <li class="activitylist_paixu_left_biankuang liid"><a onclick="sortby('id',this)">创建时间</a></li>
                                <li class="activitylist_paixu_left_biankuang liupdateDate"><a onclick="sortby('updateDate',this)">更新时间</a></li>
                                <li class="activitylist_paixu_left_biankuang livisaPrice"><a onclick="sortby('visaPrice',this)">成本价</a></li>
                             <li class="activitylist_paixu_moren"><a onclick="sortby('visaPay',this)">应收价<i class="icon icon-arrow-down"></i></a></li>-->
                                <li class="activitylist_paixu_left_biankuang livisaPay"><a onClick="sortby('visaPay',this)">应收价</a></li>

                            </ul>
                        </div>
                        <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
                        <div class="kong"></div>
                    </div>
                </div>
            </c:if>
        </div>

        <table id="contentTable" class="table mainTable activitylist_bodyer_table">
            <thead>
            <tr>
                <!-- 对应需求编号  C460V3 添加团号 -->
                <th width="10%">团号</th>
                <th width="20%">产品名称</th>
                <th width="10%">签证国家</th>
                <th width="10%">签证类型</th>
                <th width="8%">签证领区</th>
                <th width="10%">应收价格</th>
                <th width="9%">发布时间</th>
                <th width="15%">操作</th>
            </tr>
            </thead>
            <tbody>


            <c:forEach items="${page.list}" var="visaProduct" varStatus="s">

                <tr id="parent1">
                    <!-- 对应需求编号  C460V3 添加团号 -->
                    <td>${visaProduct.groupCode}</td>
                    <td class="activity_name_td">
                        <a href="javascript:void(0)"  onclick="javascript:window.open('${ctx}/visa/preorder/visaProductsDetail/${visaProduct.id}')">
                                ${visaProduct.productName}</a>
                    </td>
                    <td> ${fns:getCountryName(visaProduct.sysCountryId) }</td>
                    <td>
                        <c:forEach items="${visaTypeList}" var="visas">
                            <c:if test="${visas.key eq visaProduct.visaType}">
                                ${visas.value}
                            </c:if>
                        </c:forEach>    </td>
                    <td>
	                       <c:if test="${not empty visaProduct.collarZoning }">
	                           ${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}
	                       	</c:if>
                    </td>
                    <td class="tr">
                    <c:forEach items="${curlist}" var="currency">
                            <c:if test="${currency.id eq visaProduct.currencyId}">
                                ${currency.currencyMark}
                            </c:if>
                        </c:forEach><fmt:formatNumber pattern="#.00" value="${visaProduct.visaPay}" /> </span>起
                    </td>
                     <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${visaProduct.updateDate }"/></td>
                  <td class="p0">
                        <dl class="handle">
                            <input type="button" id="banqian_${visaProduct.id}" onclick="javascript:agentType('${visaProduct.id}','${visaProduct.reserveMethod}','${fns:getUser().company.uuid}')" value="办签" class="btn btn-primary">
                           <c:if test="${queryCommonOrderList=='1'}">
                                  <input type="button" openFlag="false" onClick="getProductOrderList(this,'${ctx}','${visaProduct.id}');" value="已收明细" class="btn btn-primary">
				            </c:if>
                            <!-- 
                            <input type="button" onclick="javascript:jbox_hsj('963');" value="还签证收据" class="btn btn-primary">
                            <input type="button" onclick="javascript:jbox_jk('964');" value="签证借款" class="btn btn-primary">
                            
                            <dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
                            <dd class="">
                                <p>
                                    <span></span>
                                    <a href="javascript:void(0)" onclick="javascript:window.open('${ctx}/visa/preorder/visaProductsDetail/${visaProduct.id}')">详情</a>
                                    <a href="javascript:void(0)" onclick="javascript:agentType(${visaProduct.id})">办签</a>
                                </p>
                            </dd>
                             -->
                        </dl>

                    </td>

                </tr>

            </c:forEach>

            </tbody>
        </table>
     	<div class="page">
                    <div class="pagination">

                <div class="endPage">
                    ${page}
                </div>
                <div style="clear:both;"></div>
            </div>
        </div>
</body>
</html></html>