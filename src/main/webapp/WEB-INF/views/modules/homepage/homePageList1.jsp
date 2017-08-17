<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta http-equiv="X-UA-Compatible"content="IE=8">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>首页</title>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/bootstrap.min.css" />
    <!--[if lte IE 6]>
    <link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css"/>
    <script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
    <![endif]-->
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
    <link rel="stylesheet" href="${ctxStatic}/css/t1t2.css"/>

    <script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script> 
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script src="${ctxStatic}/js/t1t2.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/js/validationRules.js"></script>
   <style type="text/css">
     /*指定分页插件,鼠标悬浮时的背景色*/
     .pagination ul>li>a:hover, .pagination ul>li>a:focus, .pagination ul>.active>a, .pagination ul>.active>span{
        background-color: #ff7d27;
     }
     /*调整分页插件输入框的宽度*/
     .pagination input {
       width:39px;
     }
     /*调整列表页当某些表格单元长度过长时*/
     .groupHomeOrderChildren>span{
    text-overflow:ellipsis;
    overflow: hidden;
    white-space: nowrap;
}
.homeLook {
    width: 15px;
    height: 15px;
    display: none;
    cursor: pointer;
    margin-bottom: 16px;//改成16px;
}
    /*消除首页筛选条件:出发城市的样式问题*/
   .custom-combobox-input{
   	width:178px;
   	border-right:none;
   	background:#fff;
   }
	.ui-button{
	height:28px;
	}
	/*去除目的地文本框的小图标*/
	input[readonly] {
	  background:url("") #fff;
	}
	/*解决详情弹窗的联系人浮框显示问题*/
	.POPUP {
	  width: 280px;
	}
	/*详情弹窗的悬浮标题样式*/
	.hover-container .hover-title {
	display: none;
	width: 350px;
	max-height: 220px;
	overflow-y: auto;
	padding: 5px;
	position: absolute;
	top: 100%;
	left: 0px;
	background-color: #ffffff;
	border: 1px solid #dddddd;
	z-index: 100;
}

.hover-container:hover .hover-title {
	display: block;
}
.pop_xx {
   
     overflow: visible; 
}
/*调节目的地文本框上外边距和高度*/
input[readonly] {
    margin-top: 10px;
    height:33px;
}
/*调节树形控件的"选择"按钮的外边距*/
.input-append .add-on, .input-append .btn, .input-append .btn-group {
    margin-top: 10px;
}

/*数字提醒内嵌样式*/
.noticeParent {
  background:red;
  border-radius:8px;
  width:auto;
  right: 104px;
  top: 8px;
  /*padding: 0 4px;*/
}
.notice{
  background:red;
  border-radius:8px;
  width:auto;
  /*padding: 0 4px;*/
}
.notice i, .noticeParent i {
    color: #fff;
    height: 16px;
    transform: scale(0.7);
    line-height: 15px;
    font-size: 10px;
    font-style: normal;
    margin:0;
    width:auto;
    background:none;
}
.userCenterList{
	width:130px;
	right:72px;
}
   </style>
</head>
<script type="text/javascript">
    //将普通下拉框变成可输入的文本框
       $(function(){
    	   //搜索条件:出发地变成combobox
    	   $("#fromArea").comboboxInquiry();
           //搜索条件:供应商变成combobox
    	   $("#supplier").comboboxInquiry();
           //加载目的地的上一次取值以及相应的选中状态
    		$("#targetAreaId").val("${travelActivity.targetAreaIds}");
			$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
       })
    //给搜索条件绑定事件
    $(function(){
    	//给出团日期绑定focus事件
    	$("#groupOpenDate,#groupCloseDate").on('click',function(){
    		$("#prompt4GroupOpenDate").text("");
    	})
    	//给行程天数绑定focus事件
    	$("#activityDurationFrom,#activityDurationTo").on('click',function(){
    		$("#prompt4ActivityDuration").text("");
    	})
    	//给余位绑定focus事件
    	$("#freePositionFrom,#freePositionTo").on('click',function(){
    		$("#prompt4FreePosition").text("");
    	})
    });
    
    /*
      function:searchForm
      usage:searching according to specified conditions and check validation	  
     */
    function searchForm(){
    	    var patt=/\D/g;//正则:查找非数字
    	   /*
    	           功能:校验选择的搜索条件的出团日期
    	           规则:出团日期的起始时间不能大于结束时间且结束时间可以为空    
    	          说明:这些校验可以不要          
    	    */
    	   var elGroupOpenDateBegin=$("[name='groupOpenDateBegin']").val();//出团日期起始时间
    	   var elGroupOpenDateEnd=$("[name='groupOpenDateEnd']").val();//出团日期结束时间
    	  /*  if((elGroupOpenDateBegin>elGroupOpenDateEnd)&&(elGroupOpenDateEnd!="")){ //出团日期起始时间大于出团日期结束时间
    		   $("#prompt4GroupOpenDate").text("起始时间不能大于结束时间!");
    	       return false;
    	   } */
    	   /*
    	            功能:校验选择的搜索条件的行程天数
    	            规则:行程天数的起始天数不能大于行程天数的截止天数且截止天数可以为空     
    	            说明:这些校验可以不要           
    	   */
    	   var elActivityDurationFrom=$("[name='activityDurationFrom']").val();//线程天数的起始天数
    	   var elActivityDurationTo=$("[name='activityDurationTo']").val();//线程天数的起始天数
    		  /*  if((parseInt(elActivityDurationFrom)>parseInt(elActivityDurationTo))&&(elActivityDurationTo.length>0)){ //出团日期起始时间大于出团日期结束时间
        		   $("#prompt4ActivityDuration").text("起始天数不能大于截止天数!");
        	       return false;
    	       } */
    	   /*
                                   功能:校验选择的搜索条件的余位
                                  规则:余位的起始数不能结束数且结束余位可以为空
                                  说明:这些校验可以不要                       
           */
           var elFreePositionFrom=$("#freePositionFrom").val();//行程天数的起始天数
           var elFreePositionTo=$("#freePositionTo").val();//行程天数的起始天数
        	  /*  if((parseInt(elFreePositionFrom)>parseInt(elFreePositionTo))&&(elFreePositionTo.length>0)){ //出团日期起始时间大于出团日期结束时间
      	         $("#prompt4FreePosition").text("起始余位不能大于结束余位!");
                 return false;
                 } */
           //校验通过后,提交form.
           $("#homeSearchingForm").submit();
    }
    
    //根据供应商查询列表信息
    function searchBySupplier(obj){
    	var tempSoId=$(obj).attr('id');
    	clearForm();
        window.location.href='${ctx}/activity/manager/homepagelist?supplier='+tempSoId;
    }
    //下载行程单:这个方法拷贝自产品->散拼->详情里的产品行程介绍下载
    function downloads(docIds){
    	if(null==docIds||''==docIds||'undefined'==docIds){ //判断文档id是否为空
    		top.$.jBox.tip("没有行程单可供下载!");
    		return false;
    	}
    	//将产品行程介绍打包下载
		window.open("${ctx}/sys/docinfo/zipdownload/"+docIds+"/introduction");
    }
    
</script>

<script>
    var nt = !1;
    $(window).bind("scroll",
            function() {
                var st = $(document).scrollTop();//往下滚的高度
                nt = nt ? nt: $(".J_m_nav").offset().top;
                var sel=$(".J_m_nav");
                if (nt < st) {
                    sel.addClass("nav_fixed_home");
                } else {
                    sel.removeClass("nav_fixed_home");
                }
            });
    
  //-----------t1t2需求-----------s--//
    //form条件重置函数
	 function clearForm(){
	  $(':input', '#homeSearchingForm').not(':button, :submit, :reset, :hidden')
			.val('').removeAttr('checked').removeAttr('selected');//去除已选条件的selected属性
	 // $("#homeSearchingForm").trigger("reset");//这么写条件重置,存在一定的问题
	  //条件重置搜索条件值清空
	  $("#keywordSearching").val("");
	  $("#fromArea").removeAttr('selected');
	  $("#fromArea").val('');
	  $("#targetAreaId").val("${travelActivity.targetAreaIds}");
	  $("#targetAreaName").val("${travelActivity.targetAreaNamess}");
	  $("#supplier").val('');
	  $("#groupOpenDate").val("");
	  $("#groupCloseDate").val("");
	  $("#activityDurationFrom").val("");
	  $("#activityDurationTo").val("");
	  $("#freePositionFrom").val("");
	  $("#freePositionTo").val("");
	  //条件重置,错误提示信息也清空
	  $("#prompt4GroupOpenDate").text("");
	  $("#prompt4ActivityDuration").text("");
	  $("#prompt4FreePosition").text("");
  }
  //翻页函数
  function page(n,s){
	 $("#hidden4PageNo").val(n);
	 $("#hidden4PageSize").val(s);
	 $("#homeSearchingForm").submit();
  }
  
  //-----------t1t2需求-----------e--//

    function details(){
        var iframe = "iframe:${ctx}/activity/manager/viewDetail/KCE00410";
        $.jBox(iframe, {
            title: "详情",
            width: 880,
            height: 630,
            persistent: true,
            buttons:false
        });
    }
</script>
<style>
    .page{
        margin: 30px auto 0 auto;
        width:1336px;
    }
</style>
<body>
<!--header start-->
<%@ include file="T1Head.jsp"%>
<!--header end-->
<div class="sea">
    <!--main start-->
    <div class="main">
        <div class="mainHomePage">
            <div class="contentHomePage">

                <div class="bread breadLeft breadHome"><i></i>您的位置：首页 <!-- > 订单管理 --></div>
                <div id="group">
                    <div class="groupHomeSearch">
                   <form id="homeSearchingForm" action="${ctx}/activity/manager/homepagelist" method="post"> 
                        <!-- 用户记录当前页码和每页记录大小-s -->
                          <input type="hidden" name="hidden4PageNo" id="hidden4PageNo" value="${page.pageNo}"/>
                          <input type="hidden" name="hidden4PageSize" id="hidden4PageSize" value="${page.pageSize}"/>
                        <!-- 用户记录当前页码和每页记录大小-e -->
                        <div>
                            <span class="groupSearchSpan">
                                <span>搜索：</span>
                                <input type="text" placeholder="产品名称、供应商、团号" id="keywordSearching" name="keywordSearching" value="${keywordSearching}"/>
                            </span>
                            <span class="groupSearchSpan">
                                <span>出发城市：</span>
                                 <!-- <span class="sysselect_s">
                                        <select style="display: none;" id="agentSalerUser" name="agentinfo.agentSalerUser.id">
                                            <option value="">全部</option>
                                        </select>
                                        <span class="custom-combobox">
                                            <span class="ui-helper-hidden-accessible" aria-live="polite" role="status">34 results are available, use up and down arrow keys to navigate.</span>
                                            <input autocomplete="off" class="input-t-width custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left ui-autocomplete-input" title="请选择" style="background: #ffffff"><a aria-disabled="false" role="button" class="a-t-height ui-button ui-widget ui-state-default ui-button-icon-only custom-combobox-toggle ui-corner-right" title="选择" tabindex="-1">
                                            <span class="ui-button-icon-primary ui-icon ui-icon-triangle-1-s"></span>
                                            <span class="ui-button-text"></span>
                                        </a>
                                        </span>
                                    </span>  -->
                                    <span class="sysselect_s">
                                        <select  id="fromArea" name="fromArea">
                                            <option value=" ">全部</option><!-- 不可以给默认value赋值,否则搜索会出问题! -->
                                            <c:forEach items="${fromAreas}" var="fromArea">
                                                 <option value="${fromArea.value}" <c:if test="${fromArea.value eq fromArea4Form}">selected="selected"</c:if>>${fromArea.label}</option>
                                            </c:forEach>
                                        </select>
                                    </span>
                            </span>
                            <span class="groupSearchSpan">
                                <span>目的地：</span>
                                <!-- <span class="destination">
                                    <input  type="text" ><a id="targetAreaButton" href="javascript:" class="btn " style="_padding-top:6px;" onclick="chooseDestination()">&nbsp;选择&nbsp;</a>
                                </span> -->
                                 <span class="destination">
                                 <!-- T1需求,目的地要求展示的是散拼下的所有基础信息维护中的所有目的地,不仅限于勾选启用的 -->
                                 <tags:treeselect id="targetArea" name="targetAreaIdList" value="${targetAreaIds}" labelName="targetAreaNameList"  labelValue="${targetAreaNames}"
                                 title="区域" url="/activity/manager/filterTreeData4T1?kind=2" checked="true"/>
                                 </span>
                              
                            </span>
                        </div>

                        <div style="margin-left:10px;">
                            <span class="groupSearchSpan">
                                <span>供应商：</span><!-- TODO 需求与彭凯迪确认后再做 -->
                                <!-- <span class="custom-combobox">
                                            <span class="ui-helper-hidden-accessible" aria-live="polite" role="status">34 results are available, use up and down arrow keys to navigate.</span>
                                            <input autocomplete="off" class="input-t-width custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left ui-autocomplete-input" title="请选择" style="background: #ffffff"><a aria-disabled="false" role="button" class="a-t-height ui-button ui-widget ui-state-default ui-button-icon-only custom-combobox-toggle ui-corner-right" title="选择" tabindex="-1">
                                    <span class="ui-button-icon-primary ui-icon ui-icon-triangle-1-s"></span>
                                    <span class="ui-button-text"></span>
                                </a>
                                </span> -->
                                <span class="sysselect_s">
                                        <%-- <select  id="supplier" name="supplier">
                                            <option value=" ">全部</option><!-- 供应商的默认值不能赋值,否则搜索查询会出问题 -->
                                            <c:forEach items="${supplierInfos}" var="supplierInfo">
                                            <option value="${supplierInfo.so_id}" <c:if test="${supplierInfo.so_id eq supplier4Form}">selected="selected"</c:if>>${supplierInfo.so_name}</option>
                                            </c:forEach>
                                        </select> --%>
                                        <select  id="supplier" name="supplier">
                                            <option value=" ">全部</option><!-- 供应商的默认值不能赋值,否则搜索查询会出问题 -->
                                            <c:forEach items="${supplierInfos}" var="supplierInfo">
                                            <option value="${supplierInfo.id}" <c:if test="${supplierInfo.id eq supplier4Form}">selected="selected"</c:if>>${supplierInfo.name}</option>
                                            </c:forEach>
                                        </select>
                                    </span>
                            </span>
                            <span class="groupSearchSpan">
                                <span>出团日期：</span>
                                <!-- <input id="groupOpenDate" class="inputTxt dateinput" name="groupOpenDateBegin" value=""
                                       onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})"
                                       readonly=""> -->
                                <input id="groupOpenDate" class="inputTxt dateinput" name="groupOpenDateBegin" value="${groupOpenDateBegin4Form}" onclick="WdatePicker()" readonly="" style="margin-top:1px;">
                                       &nbsp—&nbsp
                                <input id="groupCloseDate" class="inputTxt dateinput" name="groupOpenDateEnd" value="${groupOpenDateEnd4Form}" onclick="WdatePicker()" readonly="" style="margin-top:1px;">
                              </span>
                            <span id="prompt4GroupOpenDate" style="color:red;font-size:12px;"></span>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <span class="groupSearchSpan">
                                <span>行程天数：</span>
                                <input class="inputSmall" value="${activityDurationFrom4Form }" name="activityDurationFrom" id="activityDurationFrom" onkeyup="checkValue(this);" onafterpaste="checkValue(this);" onfocus="checkValue(this);"/>&nbsp—&nbsp
                                <input class="inputSmall" value="${activityDurationTo4Form }" name="activityDurationTo" id="activityDurationTo" onkeyup="checkValue(this);" onafterpaste="checkValue(this);" onfocus="checkValue(this);"/>
                            </span>
                            <span id="prompt4ActivityDuration" style="color:red;font-size:12px;"></span>
                        </div>

                        <div style="margin-left:-7px;margin-top:10px;">
                            <span class="groupSearchSpan">
                                <span>余位：</span>
                                <input class="inputSmall" value="${freePositionFrom4Form }" id="freePositionFrom" name="freePositionFrom" onkeyup="checkValue(this);" onafterpaste="checkValue(this);" onfocus="checkValue(this);"/>&nbsp—&nbsp
                                <input class="inputSmall" value="${freePostionTo4Form }" id="freePositionTo" name="freePostionTo" onkeyup="checkValue(this);" onafterpaste="checkValue(this);" onfocus="checkValue(this);"/> 
                            </span>
                            <span id="prompt4FreePosition" style="color:red;font-size:12px;"></span>
                        </div>

                        <div class="search">
                            <span onclick="searchForm();">搜&nbsp&nbsp&nbsp索</span>
                             <span class="reset" onclick="clearForm();">条件重置</span> 
                            <!-- <span onclick="clearForm();">条件重置</span> -->
                            <!-- 此reset元素不显示,用于条件重置,不可删除! -->
                            <input type="reset" id="resetBtn" name="resetBtn" style="display:none;"/>
                        </div>

                       </form> 
                    </div>
                    <div class="groupOrder J_m_nav groupHomerOrder" id="J_m_nav">
                        <span class="groupHomeOne">出团日期</span>
                        <span class="groupHomeTwo">天数</span>
                        <span class="groupHomeThree">产品名称</span>
                        <span class="groupHomeFour">余位</span>
                        <span class="groupHomeFive">QUAUQ价</span>
                        <span class="groupHomeSix">建议直客价</span>
                        <span class="groupHomeSeven">出发城市</span>
                        <span class="groupHomeEight">供应商</span>
                        <!-- <span class="groupHomeNine">服务费</span> -->
                        <span class="groupHomeTen">查看</span>
                    </div>
                    <!-- 分页开始,查询无数据的情况s -->
                    <c:if test="${fn:length(page.list) eq 0}">
                      <div class="groupHomeOrderChildren">
                                                                              经搜索暂无数据，请尝试改变搜索条件再次搜索
                      </div>
                    </c:if>
                    <!-- 分页开始,查询无数据的情况e -->
                    <!-- 分页开始,查询有数据的情况s -->
                    <c:if test="${fn:length(page.list) ne 0}">
                    <c:forEach items="${page.list}" var="infoList" >
                    <div class="groupHomeOrderChildren">
                        <span class="homeSpanOne">${infoList.groupOpenDate}</span>
                        <span class="homeSpanTwo">${infoList.activityDuration}</span>
                        <span class="homeSpanThree" title="${infoList.acitivityName}">${infoList.acitivityName}</span>
                        <span class="homeSpanFour">${infoList.freePosition}</span>
                        <!-- quauq价,索引0表示quauq价,取值同产品->散拼发布和修改时相同 -->
                        <span class="homeSpanFive">
                        <c:if test="${infoList.quauqPrice>0}"><!-- 当quauq价有值时,才显示币种符号和具体金额数 -->
                         <%-- ${fns:getCurrencyInfo(infoList.currencyids,0,'mark')}${infoList.quauqPrice} --%>
                         ${fns:getCurrencyInfo(infoList.currencyids,0,'mark')}<fmt:formatNumber type="currency" pattern="#,##0.00" value="${infoList.quauqPrice}" />
                        </c:if>
                        </span><!-- TODOunfinished -->
                        <!-- 建议直客价,索引3表示成人直客价 -->
                        <span class="homeSpanSix">
                         <c:if test="${infoList.suggestAdultPrice >0}"><!-- 当直客价有值的时候,才显示币种符号和具体金额数目 -->
                             <%--  ${fns:getCurrencyInfo(infoList.currencyids,3,'mark')}${infoList.suggestAdultPrice} --%>
                         ${fns:getCurrencyInfo(infoList.currencyids,3,'mark')} <fmt:formatNumber type="currency" pattern="#,##0.00" value="${infoList.suggestAdultPrice}" /> 
                         </c:if>
                         </span><!-- TODOunfinished -->
                        
                        <span class="homeSpanSeven">
                        <c:forEach items="${fromAreas}" var="fromArea">
                          <c:if test="${fromArea.value eq infoList.fromArea }">
                           <%-- ${infoList.fromArea} --%>
                           ${fromArea.label}
                          </c:if>
                        </c:forEach>   
                        </span>
                        <!-- TODOunfinished供应商 -->
                        <span class="homeSpanEight" title="${infoList.supplierName}">
                        <a onclick="searchBySupplier(this);" id='${infoList.sys_office_id }' style="cursor:hand;">
                        ${infoList.supplierName}
                        </a>
                        </span>
                        <!-- <span class="homeSpanNine">￥6666/人</span> -->
                        <span class="homeSpanTen" onclick="showDetails('${ctxStatic}','${infoList.activitygroup_id }','${infoList.travelactivity_id }','${infoList.sys_office_id}','${infoList.supplierName }')"><em title="详情" class="homeLook" style="display: none;"></em></span>
                        <a href="javascript:void(0)" onclick="details()">【铂金泰-精典】曼谷+沙美+芭堤雅5晚7日游（HU直飞，）沙美最的旅佳</a>
                    </div >
                    </c:forEach>
                    <div class="page">
                        <div class="pagination">
                        <div class="endPage">${page}</div>
			            <div style="clear:both;"></div>
                            <!-- <div class="endPage">
                                <ul>
                                    <li>
                                            <span class="total">
                                                共100条
                                            </span>
                                    </li>
                                    <li class="disabled">
                                    <span href="javascript:" style="padding: 0;border: 0;">
                                        <em class="fa fa-angle-left " style="margin: 0;">
                                        </em>
                                    </span>
                                    </li>
                                    <li class="active">
                                        <span href="javascript:">1</span>
                                    </li>
                                    <li>
                                        <span href="javascript:page(2,10);">2</span>
                                    </li>
                                    <li>
                                        <span href="javascript:page(3,10);">3</span>
                                    </li>
                                    <li>
                                        <span href="javascript:page(4,10);">4</span>
                                    </li>
                                    <li>
                                        <span href="javascript:page(5,10);">5</span>
                                    </li>
                                    <li>
                                        <span href="javascript:page(6,10);">6</span>
                                    </li>
                                    <li>
                                        <span href="javascript:page(7,10);">7</span>
                                    </li>
                                    <li>
                                        <span href="javascript:page(8,10);">8</span>
                                    </li>
                                    <li class="">
                                        <span class="ellipsis" href="javascript:">...</span>
                                    </li>
                                    <li>
                                        <span href="javascript:page(25,10);">25</span>
                                    </li>
                                    <li>
                                            <span href="javascript:page(2,10);" style="padding: 0;border: 0;">
                                                <em class="fa fa-angle-right " style="margin: 0;"></em>
                                            </span>
                                    </li>
                                    <li>
                                            <span class="skip" href="javascript:page(2,10);">
                                                跳至 <input type="text"/>页
                                            </span>
                                    </li>
                                    <li>
                                            <span class="skip" href="javascript:void(0);">
                                                每页 <input type="text"/>条
                                            </span>
                                    </li>
                                </ul>
                                <div style="clear:both;"></div>
                            </div> -->
                            
                        </div>
                    </div>
                    </c:if>
                     <!-- 分页结束e -->
                </div>
            </div>
        </div>
        <!--main end-->

    </div>

    <!--footer start-->

    <div class="footer">
        Copyright © 2012-2016 接待社交易管理后台　　客服电话：010-85718666
    </div>


    <!--footer end-->
   <!-- ---------------------------------------------首页的详情弹窗函数s------------------------------------------------ -->
   <script type="text/javascript">
   function showDetails(ctx,activitygroupId,travelactivityId,sys_officeId,sys_officeName){
	    //发送ajax请求,查询详情页面所需要的数据
	   $.ajax({
				type:"POST",
				url:"${ctx}/activity/manager/viewDetails",
				async:false,
				data:{
					"activitygroupId":activitygroupId,
					"travelactivityId":travelactivityId,
					"sys_officeId":sys_officeId
				},
				success:function(data){
		    //拼接详情弹窗-------------------------------s//
		    var userInfos=data.userList;//拿到所有联系人的信息
		    debugger;
		    var html='';
		    html+='<div class="pop_content">';
		    html+=' <div class="pop_content_th">团号：'+data.groupCode+'</div>';
		    html+='<div title="下载行程单" class="download" onclick="downloads(\''+data.docIds+'\')"></div>';
		    html+='<h2>'+data.acitivityName+'</h2>';
		    html+='<div class="relative">';
		    html+='<div class="pop_link"><a style="cursor:hand;" onclick="searchBySupplier(this);" id="'+sys_officeId+'"><span title="供应商：'+sys_officeName+'">供应商：'+sys_officeName+'</span></a>';
		    
		    //拼接联系人的姓名和电话-------------------s//
		    //分两种情况拼接:1.userInfos.length<=1;2.length>1
		    if(userInfos.length<=1){
		    	html+='<dl>';
			    html+='<dt style="margin-left: 6px;"><img src="'+ctx+'/images/t1t2/icon_03.png"></dt>';
			    html+='<dd>'+userInfos[0].contactName+'</dd>';
			    html+='</dl>';
			    html+=' <dl>';
			    html+='<dt><img src="'+ctx+'/images/t1t2/icon_05.png"></dt>';
			    html+='<dd>'+userInfos[0].contactPhone+'</dd>';
			    html+='</dl>';
			    html+='</div>';
		    }else{
		    	for(var i=0;i<userInfos.length;i++){
		    		if(i==0){
		    			html+='<dl>';
					    html+='<dt style="margin-left: 6px;"><img src="'+ctx+'/images/t1t2/icon_03.png"></dt>';
					    html+='<dd>'+userInfos[0].contactName+'</dd>';
					    html+='</dl>';
					    html+=' <dl>';
					    html+='<dt><img src="'+ctx+'/images/t1t2/icon_05.png"></dt>';
					    html+='<dd>'+userInfos[0].contactPhone+'</dd>';
					    html+='</dl>';
					    html+='<em class="ArrowRight"><img src="'+ctx+'/images/t1t2/icon_21.png"></em>';
					    html+='</div>';
					    html+='<div class="absolute POPUP">';
					    html+='<img class="absoluteArrow" src="'+ctx+'/images/t1t2/arrow.png" alt=""/>';
		    		}else{
		    			html+='<p>';
		    		    html+='<span><img src="'+ctx+'/images/t1t2/icon_03.png"></span>';
		    		    html+='<span>'+userInfos[i].contactName+'</span>';
		    		    html+='<span class="mL"><img src="'+ctx+'/images/t1t2/icon_05.png"></span>';
		    		    html+='<span>'+userInfos[i].contactPhone+'</span>';
		    		    html+='</p>';
		    		}
		    	}
		    	html+='</div>'
		    } 
		    
		    //拼接联系人的姓名和电话-------------------e//
		   /*  html+='<dl>';
		    html+='<dt style="margin-left: 6px;"><img src="'+ctx+'/images/t1t2/icon_03.png"></dt>';
		    html+='<dd>张三疯</dd>';
		    html+='</dl>';
		    html+=' <dl>';
		    html+='<dt><img src="'+ctx+'/images/t1t2/icon_05.png"></dt>';
		    html+='<dd>15811260382</dd>';
		    html+='</dl>';
		    
		    html+='<em class="ArrowRight"><img src="'+ctx+'/images/t1t2/icon_21.png"></em>';
		    html+='</div>';
		    html+='<div class="absolute POPUP">';
		    html+='<img class="absoluteArrow" src="'+ctx+'/images/t1t2/arrow.png" alt=""/>';
		    html+='<p>';
		    html+='<span><img src="'+ctx+'/images/t1t2/icon_03.png"></span>';
		    html+='<span>张三疯1</span>';
		    html+='<span class="mL"><img src="'+ctx+'/images/t1t2/icon_05.png"></span>';
		    html+='<span>15811260382</span>';
		    html+='</p>';
		    
		    html+='</div>'; */
		    html+='</div>';
		    
		    html+=' <div class="pop_xx">';
		    html+='<table>';
		    html+='<tr>';
		    html+=' <td style="text-align:right">';
		    html+=' <img title="出发地" src="'+ctx+'/images/t1t2/icon_13.png" height="22">';
		    html+='</td>';
		    html+='<td valign="middle" style="width: 28px;white-space: nowrap;">'+data.fromAreaName+'</td>';
		    html+='<td rowspan="2"><img  src="'+ctx+'/images/t1t2/icon_08.png"></td>';
		    html+='<td class="tr">出团日期：</td>';
		    html+='<td>'+data.groupOpenDate+'</td>';
		    html+='<td class="tr">截团日期：</td>';
		    html+='<td>'+data.groupCloseDate+'</td>';
		    html+='<td class="tr">行程天数：</td>';
		    html+='<td>'+data.activityDuration+'</td>';
		    html+="</tr>";
		    html+="<tr>";
		    html+='<td valign="middle" style="text-align:right"><img title="目的地" src="'+ctx+'/images/t1t2/icon_15.png"></td>';
		    
		    /* html+='<td valign="middle" style="width: 28px;white-space: nowrap;">'+data.targetAreaNames+'</td>'; */
		    html+='<td valign="middle" style="width: 28px;white-space: nowrap;position:relative;white-space: pre-line;" class="hover-container">';
		       html+='<div style=" display:block; width:100px; text-overflow:ellipsis; overflow:hidden; white-space:nowrap;">';
		       html+=''+data.targetAreaNames+'';
		       html+='</div>';
		       html+='<div class="hover-title">';
		       html+=''+data.targetAreaNames+'';
		       html+='</div>';
		    html+='</td>'
		    
		    html+='<td class="tr">余位：</td>';
		    html+=' <td>'+data.freePosition+'</td>';
		    html+='<td class="tr">交通工具：</td>';
		    html+='<td title="飞机">';
		    html+=''+data.trafficModeName+'';
		    html+='</td>';
		    html+=" </tr>";
		    html+=" </table>";
		    html+="</div>";
		    html+='<div style="clear: both;"></div>';
		    
		    html+='<div class="pop_sales">';
		    html+='<table>';
		    html+='  <tbody>';
		    html+='<tr class="firsttr">';
		    html+=' <td class="quauq-price tr">QUAUQ价：</td>';
		    html+='<td>成人：</td>';
		    if(Number(data.quauqAdultPrice)>0){//价格有值时,才展示币种符号和"/人"
		    html+=' <td><span class="orange">'+data.quauqAdultPriceMark+'</span><span class="orange orange-price">'+data.quauqAdultPrice+'</span></td>';
		    html+='<td>/人</td>';
		    }else{
		    	html+='<td></td><td></td>';
		    }
		    html+='<td class="secondtd">儿童：</td>';
		    if(Number(data.quauqChildPrice)>0){//价格有值时,才展示币种符号和"/人"
		    html+='<td><span class="orange">'+data.quauqChildPriceMark+'</span><span class="orange orange-price">'+data.quauqChildPrice+'</span></td>';
		    html+='<td>/人</td>';
		    }else{
		    	html+='<td></td><td></td>';
		    }
		    html+='<td class="secondtd">特殊人群：</td>';
		    if(Number(data.quauqSpecialPrice)>0){//价格有值时,才展示币种符号和"/人"
		    html+='<td><span class="orange">'+data.quauqSpecialPriceMark+'</span><span class="orange orange-price">'+data.quauqSpecialPrice+'</span></td>';
		    html+='<td>/人</td>';
		    }else{
		    	html+='<td></td><td></td>';
		    }
		    html+='</tr>';
		    html+=' <tr>';
		    html+='<td class="tr">建议直客价：</td>';
		    html+=' <td>成人：</td>';
		    if(Number(data.suggestAdultPrice)>0){//价格有值时,才展示币种符号和"/人"
		    html+='<td>'+data.suggestAdultPriceMark+''+data.suggestAdultPrice+'/人</td>';
		    html+='<td></td>';
		    }else{
		    	html+='<td></td><td></td>';
		    }
		    html+=' <td class="secondtd">儿童：</td>';
		    if(Number(data.suggestChildPrice)>0){//价格有值时,才展示币种符号和"/人"
		    html+='<td>'+data.suggestChildPriceMark+''+data.suggestChildPrice+'/人</td>';
		    html+=' <td></td>';
		    }else{
		    	html+='<td></td><td></td>';
		    }
		    html+='<td class="secondtd">特殊人群：</td>';
		    if(Number(data.suggestSpecialPrice)>0){//价格有值时,才展示币种符号和"/人"
		    html+='<td>'+data.suggestSpecialPriceMark+''+data.suggestSpecialPrice+'/人</td>';
		    html+='<td></td>';
		    }else{
		    	html+='<td></td><td></td>';
		    }
		    html+='</tr>';
		    html+='<tr>';
		    html+=' <td class="tr">服务费：</td>';
		    html+='<td>成人：</td>';
		    if(Number(data.quauqAdultPriceProfit)>0){//价格有值时,才展示币种符号和"/人"
		    html+=' <td>'+data.quauqAdultPriceMark+''+data.quauqAdultPriceProfit+'/人</td>';
		    html+='<td></td>';
		    }else{
		    	html+='<td></td><td></td>';
		    }
		    html+='<td class="secondtd">儿童：</td>';
		    if(Number(data.quauqChildPriceProfit)>0){//价格有值时,才展示币种符号和"/人"
		    html+=' <td>'+data.quauqChildPriceMark+''+data.quauqChildPriceProfit+'/人</td>';
		    html+='<td></td>';
		    }else{
		    	html+='<td></td><td></td>';
		    }
		    html+='<td class="secondtd">特殊人群：</td>';
		    if(Number(data.quauqSpecialPriceProfit)>0){//价格有值时,才展示币种符号和"/人"
		    html+=' <td>'+data.quauqSpecialPriceMark+''+data.quauqSpecialPriceProfit+'/人</td>';
		    html+='<td></td>';
		    }else{
		    	html+='<td></td><td></td>';
		    }
		    html+='</tr>';
		    html+='</tbody>';
		    html+='</table>';
		    html+='</div>';
		    html+='</div>';
		$.jBox(html, {
			title: "详情",
			width: 836,
			height: 500,
			persistent: true,
			buttons:false
		});
		//详情弹窗内联系人小图标绑定鼠标悬浮函数
		$(".ArrowRight").hover(function(){
			$(".POPUP").show();
		},function(){
			$(".POPUP").hide();
		});
	  //拼接详情弹窗-------------------------------e//
				}
			});
	}
   </script>
   <!-- ---------------------------------------------首页的详情弹窗函数e------------------------------------------------ -->
</body>

</html>
