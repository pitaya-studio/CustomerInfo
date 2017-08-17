<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="decorator" content="wholesaler"/>
    <title>库存-库存列表页</title>
    <%--<link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />--%>
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
    <%--<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/quauq.css" />--%>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/modules/stock/stock-list.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script type="text/javascript">
        $(function () {
            //搜索条件筛选
            launch();
            //操作浮框
            operateHandler();
            //团号和产品切换
            switchNumAndPro();
            //首次打印提醒
            inputTips();
            $(".uiPrint").hover(function () {
                $(this).find("span").show();
            }, function () {
                $(this).find("span").hide();
            });

            $('.team_a_click').toggle(function () {
                $(this).addClass('team_a_click2')
            }, function () {
                $(this).removeClass('team_a_click2')
            });
        });
        var $ctx="${ctx}"; 
        //展开收起
        function expand(child, obj, showType) {
            if ($(child).is(":hidden")) {
                if (showType == '1') {
                    $(obj).html("关闭团期预定");
                } else {
                    $(obj).html("关闭团期预报名");
                }
                $(child).show();
                $(obj).parents("td").addClass("td-extend");
            } else {
                if (!$(child).is(":hidden")) {
                    $(child).hide();
                    $(obj).parents("td").removeClass("td-extend");
                    if (showType == '1') {
                        $(obj).html("展开团期预定");
                    } else {
                        $(obj).html("展开团期预报名");
                    }
                }
            }
        }
    </script>
    <script type="text/javascript">
        function reserve() {
            var $popReserve = $.jBox($('#popReserve').html(),
                    {
                        title: "渠道和付款方式选择：",
                        buttons: {}
                    }
            );
            $popReserve.on('click', '#btnNext', function () {
                var status = $popReserve.data("status");
                if (status == 'payment') {
                    $popReserve.data('status', 'orderType');
                    $popReserve.trigger('pop.status.change');
                } else {
                    //@todo 需要开发继续处理
                }
            });
            $popReserve.on('click', '#btnPre', function () {
                var status = $popReserve.data("status");
                if (status == 'payment') {
                    $.jBox.close();
                } else {
                    $popReserve.data('status', 'payment');
                    $popReserve.trigger('pop.status.change');
                }
            });
            $popReserve.on('pop.status.change', function () {
                var status = $popReserve.data("status");
                var $btnPre = $popReserve.find('#btnPre');
                var $reservePayment = $popReserve.find("#reservePayment");
                var $reserveOrderType = $popReserve.find("#reserveOrderType");
                if (status == 'payment') {
                    $btnPre.text('取消');
                    $popReserve.find('.jbox-title').text('渠道和付款方式选择');
                    $reservePayment.show();
                    $reserveOrderType.hide();
                } else {
                    $btnPre.text('返回');
                    $popReserve.find('.jbox-title').text('订单类型选择');
                    $reservePayment.hide();
                    $reserveOrderType.show();
                }
            });
            $popReserve.data('status', 'payment');
        }
        function checkall(obj){
            if($(obj).attr("checked")){
                $('#contentTable input[type="checkbox"]').attr("checked",'true');
                $("input[name='allChk']").attr("checked",'true');
            }else{
                $('#contentTable input[type="checkbox"]').removeAttr("checked");
                $("input[name='allChk']").removeAttr("checked");
            }
        }

        function checkreverse(obj){
            var $contentTable = $('#contentTable');
            $contentTable.find('input[type="checkbox"]').each(function(){
                var $checkbox = $(this);
                if($checkbox.is(':checked')){
                    $checkbox.removeAttr('checked');
                }else{
                    $checkbox.attr('checked',true);
                }
            });
        }
        //分页查询函数
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
        function jbox_relevance_record_pop(uvalue) {
			$.ajax({                 
				cache: true,                 
				type: "POST",                 
				url:"${ctx}/cruiseshipStock/cruiseshipStockGroupRelList",                    
				data:{
					stockUuid:uvalue
				}, 
				error: function(request) {                     
					top.$.jBox.tip('操作失败');
				},   
				dataType: "json",
				success: function(data) {
					$("#stockgltable").children().remove();
				  var html = "";
				 html="<tr><th class=\"tc\" width=\"30%\">关联时间</th><th class=\"tc\" width=\"50%\">关联团号</th><th class=\"tc\" width=\"20%\">操作人</th></tr>"
					  $.each(data,function(i,n){
						  	var date=new Date(parseInt(n.createDate))
		     				 var year=date.getFullYear();
		     				 var month=date.getMonth()+1;
		     				 var day=date.getDate();
		     				 var formatDate=year+"-"+month+"-"+day
	  				  	  html+="<tr>";
	  				      html+="<td  class='tc'>"+formatDate+"</td>";
	  				      html+="<td  class='tc'>"+n.groupCode+"</td>"; 
	  				      html+="<td  class='tc'>"+n.name+"</td>";
	  				      html+="</tr>";  
					  });
				  $("#stockgltable").append(html);
	              $.jBox($("#relevance-record-pop").html(), {
		               title: "关联记录",submit: function (v, h, f) {
		                   if (v == "1") {
		                       return true;
		                   }
		               }, height: 300, width: 500,
		               persistent: true
		               
		          });
		          inquiryCheckBOX();
				}             
			});
        }
    function delStock(uuid,obj){
    	top.$.jBox.confirm('确认要删除么？','系统提示',function(v){
    		if(v=="ok"){
    			$.ajax({
    	    		type:"POST",
    	    		url:$ctx+"/cruiseshipStock/cruiseshipStockGroupRelBatchDelete",
    	    		data:{
    	    			uuids:uuid
    	    		},
    	    		success:function(data){
    	    			if(data=="库存已关联，删除失败"){
    	    				top.$.jBox.tip("库存已关联，删除失败!","error");
    	    			}else{
    	    				top.$.jBox.tip("删除成功","success");
    	    				$(obj).parent().parent().parent().parent().parent().remove();
    	    			}
    	    		}
    	    	});
    		}
    	});
    }
        
//         function jbox_relevance_record_pop() {
//             $.jBox($("#relevance-record-pop").html(), {
//                 title: "关联记录",submit: function (v, h, f) {
//                     if (v == "1") {
//                         return true;
//                     }
//                 }, height: 300, width: 500,
//                 persistent: true
//             });
//             inquiryCheckBOX();
//         }
    </script>
</head>
<body>
<div id="sea">
    <!--右侧内容部分开始-->
    <form id="searchForm" action="${ctx}/cruiseshipStock/cruiseshipStockList" method="post">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />

        <div class="activitylist_bodyer_right_team_co">
            <div class="activitylist_bodyer_right_team_co2 pr">
                <%--<label>游轮名称：</label>--%>
                <input name="curiseshipName"class="txtPro inputTxt searchInput" value="${parameters.curiseshipName}"
                       type="text" flag="istips" placeholder="仅支持游轮名称模糊检索"/>
                <%--<span style="display: block;" class="ipt-tips">仅支持游轮名称模糊检索</span>--%>
            </div>
            <a class="zksx">筛选</a>
            <div class="form_submit">
                <input class="btn btn-primary" value="搜索" type="submit" />
            </div>
            <p class="main-right-topbutt">
                <%--<a class="primary" target="_blank" href="${ctx }/flightControl/tosaveflightcontrol">新增机票库存</a>--%>
                <a class="primary" href="${ctx}/cruiseshipStock/cruiseshipStockform" target = "_blank">添加库存</a>
            </p>
            <div class="ydxbd">
                <span></span>
                <div class="activitylist_bodyer_right_team_co1">
                    <label class="activitylist_team_co3_text">船期：</label>
                    <input id="groupOpenDate" class="inputTxt dateinput" name="shipDateBegin"
                           value="${parameters.shipDateBegin}"
                           onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})"
                           readonly="" />
                    <span style="font-size:12px; font-family:'宋体';"> 至</span>
                    <input id="groupCloseDate" class="inputTxt dateinput" name="shipDateEnd"
                           value="${parameters.shipDateEnd}" onclick="WdatePicker()" readonly="" />
                </div>
            </div>
        </div>
    </form>
    <div class="activitylist_bodyer_right_team_co_paixu">
        <div class="activitylist_paixu">
            <div class="activitylist_paixu_left">
            </div>
            <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
        </div>
    </div>
    <!--状态开始-->
    <%--<div class="table-order-list-container" id="table-order-list">--%>
        <%--<div class="supplierLine">--%>
            <%--<p class="main-right-topbutt upgrade">--%>
                <%--<a href="${ctx}/cruiseshipStock/cruiseshipStockform" target = "_blank">添加库存</a>--%>
            <%--</p>--%>
        <%--</div>--%>
    <%--</div>--%>
    <!--状态结束-->
    <!--S--223需求库存表格-->
    <table id="contentTable" class="table activitylist_bodyer_table mainTable">
        <thead>
        <tr>
            <th width="4%">序号</th>
            <th width="20%">游轮名称</th>
            <th width="10%"> 船期</th>
            <th width="5%">关联状态</th>
            <th width="6%">操作</th>
        </tr>
        </thead>
        <tbody>

        <c:if test="${fn:length(page.list) <= 0 }">
            <tr class="toptr" >
                <td colspan="6" style="text-align: center;">
                    暂无搜索结果
                </td>
            </tr>
        </c:if>

        <c:forEach items="${page.list }" var="list" varStatus="s">
            <tr>
                <td class="tc">
                    <input type="checkbox" name="ids" value="${list.uuid }" />${s.count}
                </td>
                <td class="tl">${list.name}</td>
                <td class="tc">${list.shipDate}</td>
                <td class="tc">
                	<c:choose>
	                	<c:when test="${list.status eq '0'}">已关联</c:when>
	                	<c:otherwise>未关联</c:otherwise>
                	</c:choose>
                </td>
                <td class="p0">
                    <dl class="handle">
                        <dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png" /></dt>
                        <dd class="">
                            <p>
                                <span></span>
                                <a href="${ctx }/cruiseshipStock/cruiseshipStockDetail?detailUuid=${list.uuid}" target="_blank">详情</a>
                                <shiro:hasPermission name="cruiseshipStockList:stock:edit">
                           		  	<a href="${ctx }/cruiseshipStock/cruiseshipStockedit?detailUuid=${list.uuid}" target = "_blank">修改</a>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="cruiseshipStockList:stock:delete">
	                                <a href="javascript:void(0)" onclick="delStock('${list.uuid}',this)">删除</a>
                                </shiro:hasPermission>
                                <a onclick="jbox_relevance_record_pop('${list.uuid}');">关联记录</a>
                            </p>
                        </dd>
                    </dl>
                </td>
            </tr>
        </c:forEach>

        </tbody>
    </table>
    <!--E--223需求库存表格-->
    <!--S--分页-->
    <div class="page">
        <div class="pagination">
            <dl>
                <dt><input name="allChk" onclick="checkall(this)" type="checkbox" />全选</dt>
                <dt><input name="reverseChk" onclick="checkreverse(this)" type="checkbox" />反选</dt>
                <dd>
                    <%--UG_V2 按钮样式统一--%>
                    <%--<a onclick="jbox_relevance_record_del_pop();">批量删除</a>--%>
                    <input class="btn ydbz_x" type="button" value="批量删除" onclick="jbox_relevance_record_del_pop();">

                </dd>
            </dl>
        </div>
        <div class="pagination">
            <div class="pagination clearFix">${page}</div>
        </div>
    </div>
    <!--E--分页-->
<!--右侧内容部分结束-->
</div>

<!--S--关联记录弹窗-->
<div id="relevance-record-pop" class="display-none">
    <div class="relevance-record-pop">
        <table class="table table-striped table-bordered "  id="stockgltable">
            <tr>
                <th width="30%" class="tc">关联时间</th>
                <th width="50%" class="tc">关联团号</th>
                <th width="20%" class="tc">操作人</th>
            </tr>
        </table>
    </div>
</div>

<!--E--关联记录弹窗-->
</body>
</html>
