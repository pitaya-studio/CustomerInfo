<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>${title }</title>
    <meta name="decorator" content="wholesaler"/>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactor.js" type="text/javascript"></script>
    <script src="${ctxStatic}/json/jquery.json.ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>    
    <script src="${ctxStatic}/jquery-other/jquery-combobox_prop.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
    <script type="text/javascript">
	$(function(){
        //团号产品名称切换
        $("#contentTable").delegate("ul.caption > li","click",function(){
            var iIndex = $(this).index();/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
            $(this).addClass("on").siblings().removeClass('on');
            $(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
        });
    
        $("#contentTable").delegate(".tuanhao","click",function(){
            $(this).addClass("on").siblings().removeClass('on');
            $('.chanpin_cen').removeClass('onshow');
            $('.tuanhao_cen').addClass('onshow');
            $("div.groupCode").show();
            $("div.acitivityName").hide();
        });
       
        $("#contentTable").delegate(".chanpin","click",function(){
             $(this).addClass("on").siblings().removeClass('on');
             $('.tuanhao_cen').removeClass('onshow');
             $('.chanpin_cen').addClass('onshow');
             $("div.groupCode").hide();
             $("div.acitivityName").show();
        });


         <%--起航假期0425需求，用户点击的付款审批的条目，默认在实际成本table的第一行。后台已经把该item放到list的首位。
          默认显示顺序是境内境外，如果该审批条目是境外需要把境外挪到境内的上方。
          只有当用户是起航假期，并且是境外时才会overseas=1； yudong.xu 2016.6.1 --%>

        if ("${overseas}" == "1"){
            $(".inTr").insertAfter(".outTr:last");
        }
    });

	function _selectCheck(){
		var items = '';
		$("input[name='actualId']").each(function(){
			if ($(this).is(":checked")){
				var val = $(this).val();
				if(!items){
					items = val;
				}else{
					items += "," + val;
				}
			}
		});
		if(!items){
			$.jBox.tip("请选择需要审批的项目", 'error');
			return false;
		}
		return items;
	}

	/**
	 * 单个审批通过或者驳回
	 * author: shijun.liu
	 */
	function pass_deny(flag, obj){
		var $tr = $(obj).parents('tr:first');//选中当前行
		var val = $tr.find('input[name="actualId"]').val();
		var html = '<div class="add_allactivity"><label>请填写您的审批备注!</label>';
		html += '<textarea style="width:80%;margin:10px auto;" name="comment" cols="" rows="" maxlength="100" oninput="this.value=this.value.substring(0,100)"></textarea>';
		html += '</div>';
		$.jBox(html, { title: "备注",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v=="1"){
				var comment = f.comment || '';
				if(flag == 'pass'){
					_passCost(val, comment);
				}else if(flag == 'deny'){
					_denyCost(val, comment);
				}
				
			}
		},height:250,width:500});
	}
	
	/**
	 * 批量审批
	 * author: shijun.liu
	 */
	function batch_review(){
		var items = _selectCheck();
		if(!items){
			return false;
		}
		var html = '<div class="add_allactivity"><label>请填写您的审批备注!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" name="comment" cols="" rows="" maxlength="100" oninput="this.value=this.value.substring(0,100)"></textarea>';
		html += '</div>';
		$.jBox(html, { title: "备注",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v=="1"){
				var comment = f.comment || '';
				_passCost(items, comment)
			}
		},height:250,width:500});
	}

	/**
	 * 审批通过
	 * author:shijun.liu
	 */
	function _passCost(items, comment){
		$.ajax({
			type: "POST",
			url: "${ctx}/review/payment/web/batchApprove",
			cache:false,
			data:{items:items,comment:comment},
			success:function(data){
				if(data.flag){
					$.jBox.tip('审批通过', 'success');
					window.opener.$("#searchForm").submit(); //刷新父窗口
					window.location.reload();
				}else{
					$.jBox.tip(data.msg, 'error');
					return false;
				}
			},
			error : function(e){
				$.jBox.tip("提交失败", 'error');
				return false;
			}
		});
	}

	/**
	 * 审批驳回
	 * author:shijun.liu
	 */
	function _denyCost(items, comment){
		$.ajax({
			type: "POST",
			url: "${ctx}/review/payment/web/batchReject",
			cache:false,
			data:{items:items,comment:comment},
			success:function(data){
				if(data.flag){
					$.jBox.tip('驳回成功', 'success');
					window.opener.$("#searchForm").submit(); //刷新父窗口
					window.location.reload();
				}else{
					$.jBox.tip(data.msg, 'error');
					return false;
				}
			},
			error : function(e){
				$.jBox.tip("提交失败", 'error');
				return false;
			}
		});
	}
    function openDetail(obj){
        if($(obj).text()=="展开明细"){
            $(obj).parent().parent().next().show();
            $(obj).text("收起明细");
        }else{
            $(obj).parent().parent().next().hide();
            $(obj).text("展开明细");
        }
    }
</script>
</head>
<body>
	<!-- <page:applyDecorator name="pay_review_head">
		<page:param name="current"><c:choose><c:when test="${typeId==1}">single</c:when><c:when test="${typeId==2}">loose</c:when><c:when test="${typeId==3}">study</c:when><c:when test="${typeId==5}">free</c:when><c:when test="${typeId==4}">bigCustomer</c:when><c:when test="${typeId==10}">cruise</c:when></c:choose></page:param>
	</page:applyDecorator> -->
	<div class="mod_nav">财务 > 财务审批 > 付款审批 > ${typename}付款审批</div>
    <div class="produceDiv">
        <div class="mod_information">
            <div class="mod_information_d">
                <div class="ydbz_tit">产品基本信息</div>
            </div>
        </div>
        <div class="mod_information_dzhan">
            <div class="mod_information_dzhan_d mod_details2_d">
                <span style="color: #3a7851; font-size: 16px; font-weight: bold;">${travelActivity.acitivityName}</span>
                <div class="mod_information_d7"></div>
                <table border="0" width="90%">
                    <tr>
                        <td class="mod_details2_d1">产品编号：</td>
                        <td class="mod_details2_d2">
                            <c:choose> 
                                 <c:when test="${fn:length(travelActivity.activitySerNum) gt 20}"> 
                                    <a style="text-decoration: none; color:inherit; cursor:default;" title="${travelActivity.activitySerNum}"><c:out value="${fn:substring(travelActivity.activitySerNum, 0, 20)}..." /></a> 
                                 </c:when> 
                                 <c:otherwise> 
                                    <c:out value="${travelActivity.activitySerNum}" /> 
                                 </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="mod_details2_d1">产品分类：</td>
                        <td class="mod_details2_d2">
                            <c:choose>
                                <c:when test="${travelActivity.overseasFlag eq 1}">国外</c:when>
                                <c:otherwise>国内</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="mod_details2_d1">旅游类型：</td>
                        <td class="mod_details2_d2">${travelActivity.travelTypeName}</td>
                        <td class="mod_details2_d1">出发城市：</td>
                        <td class="mod_details2_d2">${travelActivity.fromAreaName}</td>
                    </tr>
                    <tr>
                        <td class="mod_details2_d1">目的地：</td>
                        <td class="mod_details2_d2" id="targetAreaName" title=""></td>
                        <td class="mod_details2_d1">交通方式：</td>
                        <td class="mod_details2_d2">
                            ${travelActivity.trafficModeName}
                            <c:if test="${not empty travelActivity.trafficName and fn:indexOf(relevanceFlagId,travelActivity.trafficMode)>=0 }">
                                &nbsp;&nbsp;|&nbsp;&nbsp;${travelActivity.trafficNameDesc}
                            </c:if>
                        </td>
                        <td class="mod_details2_d1">产品系列：</td>
                        <td class="mod_details2_d2">${travelActivity.activityLevelName}</td>
                        <td class="mod_details2_d1">团号：</td>
                        <td class="mod_details2_d2">${activityGroup.groupCode}</td>
                    </tr>
                    <tr>
                        <td class="mod_details2_d1">产品类型：</td>
                        <td class="mod_details2_d2">${travelActivity.activityTypeName}</td>
                        <td class="mod_details2_d1">行程天数：</td>
                        <td class="mod_details2_d2" id="activityDuration">${travelActivity.activityDuration}</td>
                        <td class="mod_details2_d1">付款方式：</td>
                        <td colspan="3" class="mod_details2_d2">
                            <c:if test="${travelActivity.payMode_deposit eq 1}">订金占位
                                <c:if test="${not empty travelActivity.remainDays_deposit}">（保留${travelActivity.remainDays_deposit}天）&nbsp;&nbsp;
                                </c:if>
                            </c:if>
                            <c:if test="${travelActivity.payMode_advance eq 1}">
                                                                         预占位（保留${travelActivity.remainDays_advance}天）&nbsp;&nbsp;
                            </c:if>
                            <c:if test="${travelActivity.payMode_full eq 1}">全款支付</c:if>
                            <c:if test="${travelActivity.payMode_op eq 1}">计调确认占位</c:if>
                        </td>
                    </tr>
                    <tr>
                        <td class="mod_details2_d1">计调人员：</td>
                        <td class="mod_details2_d2">${travelActivity.createBy.name}</td>
                        <td class="mod_details2_d1">创建时间：</td>
                        <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${travelActivity.createDate}"/></td>
                        <td class="mod_details2_d1">更新时间：</td>
                        <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${travelActivity.updateDate}"/></td>   
                        <td class="mod_details2_d1">出团日期：</td>
                        <td class="mod_details2_d2"><fmt:formatDate value="${activityGroup.groupOpenDate}" pattern="yyyy-MM-dd"/></td>   
                    </tr>
                </table>
                <div class="kong"></div>
            </div>
        </div>    

        <div class="mod_information">
			<div class="mod_information_d">
            	<div class="ydbz_tit">订单列表</div>
			</div>
        </div>
        <table class="activitylist_bodyer_table mod_information_dzhan" id="contentTable">
          <thead>
            <tr>
              <th width="7%">预定渠道</th>
              <th width="11%">订单号</th>
              <th width="13%"><span class="tuanhao on">团号</span>/<span class="chanpin">产品名称</span></th>
              <th width="11%">预订时间</th>
              <th width="8%">出/截团日期</th>
              <th width="5%">人数</th>
              <th width="8%">订单状态</th>
              <th width="8%">订单总额</th>
              <th width="8%">已收金额<br>到账金额</th>
              <th width="6%">操作</th>            
            </tr>
          </thead>
          <tbody>
          <c:forEach items="${orderList}" var="groupOrder">
            <tr >
              <td>
               <c:choose>
				   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && groupOrder.orderCompanyName=='非签约渠道'}"> 
				       直客
				   </c:when>
				   <c:otherwise>
				        ${groupOrder.orderCompanyName}
				   </c:otherwise>
			</c:choose> 
             </td>
              <td>${groupOrder.orderNum}</td>
              <td class="tc"><div class="groupCode">${groupOrder.groupCode}</div>  
              				<div style="display:none;" class="acitivityName">${groupOrder.acitivityName}</div></td>
              <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${groupOrder.orderTime }"/></td>
              <td style="padding:0px;">
                <div class="out-date">${groupOrder.groupOpenDate}</div>
                <div class="close-date">${groupOrder.groupCloseDate}</div>
              </td>
              <td>${groupOrder.orderPersonNum}</td>
              <td>${fns:getDictLabel(groupOrder.payStatus, "order_pay_status", "")}</td>
              <td class="tr"><span class="tdorange fbold">${groupOrder.totalMoney} </span></td>
              <td class="p0 tr">  
                <div class="yfje_dd"><span class="fbold">${groupOrder.payedMoney}</span></div>
                <div class="dzje_dd"><span class="fbold">${groupOrder.accountedMoney}</span></div>
              </td>              
              <td class="tc">
              	<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/orderCommon/manage/orderDetail/'+${groupOrder.id},'_blank')">详情 </a>
              </td>
            </tr>
            </c:forEach>
          </tbody>
        </table>      
            <div class="mod_information">
                    <div class="mod_information_d">
                        <div class="ydbz_tit">实际成本</div>
                    </div>
                </div>
<%-- <c:if test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}">

    <table class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan">

        <thead>
        <tr>
            <th width="20%">序号</th>
            <th width="25%">地接社/渠道商</th>
            <th width="25%">实际总成本</th>
            <th width="30%">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${actualList }" var="list" varStatus="status">
            <tr>
                <td class="tc">${status.count }</td>
                <td class="tc"><c:choose>
                    <c:when
                            test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && list.supplyName=='非签约渠道'}">
                        直客
                    </c:when>
                    <c:otherwise>
                        ${list.supplyName}
                    </c:otherwise>
                </c:choose></td>
                <td class="tc">￥${list.priceAfter }</td>
                <td class="tc"><a href="javascript:void(0)"
                                  onclick="openDetail(this)">展开明细</a></td>
            </tr>
            <tr style="display:none">
                <td colspan="4" class="team_top">
                    <table style="margin-left:0 auto;width:100%">
                        <thead>
                        <tr>
                            <th width="8%">境内/外项目</th>
                            <th width="15%">项目名称</th>
                            <th width="5%">数量</th>
                            <th width="8%">币种</th>
                            <th width="10%">单价</th>
                            <th width="10%">总价</th>
                            <th width="8%">备注</th>
                            <th width="7%">付款审批状态</th>
                            <th width="8%">操作</th>
                            <th width="5%">选择</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${list.InList }" var="actualIn"
                                   varStatus="statusList">
                            <tr>
                                <c:if test="${statusList.count==1}">
                                    <td class="tc" rowspan="${fn:length(list.InList)}">境内付款明细</td>
                                </c:if>
                                <td class="tc">${actualIn.name}</td>
                                <td class="tc">${actualIn.quantity}</td>
                                <td name="tdCurrencyName" class="tc">
                                    <c:forEach items="${curlist}" var="currency">
                                        <c:if test="${currency.id==actualIn.currencyId}">${currency.currencyName}</c:if>
                                    </c:forEach>
                                </td>
                                <td class="tr" name="tdPrice">
                                    <c:forEach items="${curlist}" var="currency">
                                        <c:if test="${currency.id==actualIn.currencyId}">
                                            ${currency.currencyMark}${actualIn.price}
                                        </c:if>
                                    </c:forEach></td>
                                <td class="tr">
                                    <c:forEach items="${curlist}" var="currency">
                                        <c:if test="${currency.id==actualIn.currencyId}">
                                            ${currency.currencyMark}${actualIn.price * actualIn.quantity}
                                        </c:if>
                                    </c:forEach></td>
                                <td name="tdComment">${actualIn.comment}</td>
                                <td class="tc" id="review" name="review">
                                    <!-- 成本付款审批状态 -->
                                    <c:if test="${actualIn.reviewType==0}">
                                        <!-- update by shijun.liu 2016.01.18 -->
                                        <!-- 旧审批的审批状态  -->
                                        <c:if test="${empty actualIn.payReviewUuid and actualIn.isNew ne '2' }">
                                            ${fns:getNextPayReview(actualIn.id)}
                                        </c:if>
                                        <!-- 新审批 但是没有提交付款申请  -->
                                        <c:if test="${empty actualIn.payReviewUuid and actualIn.isNew eq '2' }">
                                            待提交审批
                                        </c:if>
                                        <c:if test="${not empty actualIn.payReviewUuid }">
                                            <c:set value="${fns:getReviewNewByUuid(actualIn.payReviewUuid) }" var="reviewNew"></c:set>
                                            ${fns:getChineseReviewStatus(reviewNew.status,reviewNew.currentReviewer)}
                                        </c:if>
                                    </c:if>
                                    <!-- 退款返佣的审批状态 -->
                                    <c:if test="${actualIn.reviewType!=0}">
                                        <c:if test="${actualIn.isNew ne '2' }">
                                            ${actualIn.reviewStatus}
                                        </c:if>
                                        <c:if test="${actualIn.isNew eq '2' }">
                                            <c:set value="${fns:getReviewNewByUuid(actualIn.reviewUuid) }" var="reviewNew"></c:set>
                                            <font color="green">${fns:getChineseReviewStatus(reviewNew.status,reviewNew.currentReviewer)}</font>
                                        </c:if>
                                    </c:if>
                                </td>
                                <td class="tc">
                                    <c:if test="${fns:isCurrentReviewer(actualIn.payReviewUuid) and flag eq 'review'}">
                                        <a href="javascript:void(0)" onclick="pass_deny('pass', this)">通过</a>&nbsp;
                                        <a href="javascript:void(0)" onclick="pass_deny('deny', this)">驳回</a>
                                    </c:if>
                                    <a href="javascript:void(0)" onclick="showDownloadWin('${ctx}','${actualIn.costVoucher }',false)">下载附件</a>
                                </td>
                                <td>
                                    <c:if test="${fns:isCurrentReviewer(actualIn.payReviewUuid) and flag eq 'review'}">
                                        <input type="checkbox" value="${actualIn.payReviewUuid}" name="actualId"/>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${! empty list.InList}">
                            <tr>
                                <td>境内小计</td>
                                <td colspan="11">&nbsp;<span>${list.InSum}</span></td>
                            </tr>
                        </c:if>
                        <c:forEach items="${list.OutList }" var="actualOut"
                                   varStatus="statusList">
                            <tr>
                                <c:if test="${statusList.count==1}">
                                    <td class="tc" rowspan="${fn:length(list.OutList)}">境外付款明细</td>
                                </c:if>
                                <td class="tc">${actualOut.name}</td>
                                <td class="tc">${actualOut.quantity}</td>
                                <td name="tdCurrencyName" class="tc">
                                    <c:forEach items="${curlist}" var="currency">
                                        <c:if test="${currency.id==actualOut.currencyId}">
                                            ${currency.currencyName}
                                        </c:if>
                                    </c:forEach>
                                </td>
                                <td class="tr" name="tdPrice"> <c:forEach items="${curlist}" var="currency">
                                    <c:if test="${currency.id==actualOut.currencyId}">
                                        ${currency.currencyMark}${actualOut.price}
                                    </c:if>
                                </c:forEach></td>
                                <td class="tr"> <c:forEach items="${curlist}" var="currency">
                                    <c:if test="${currency.id==actualOut.currencyId}">
                                        ${currency.currencyMark}${actualOut.price * actualOut.quantity}
                                    </c:if>
                                </c:forEach></td>
                                <td name="tdComment">${actualOut.comment}</td>
                                <td class="tc" width="8%" id="review" name="review">
                                    <!-- 成本付款审批状态 -->
                                    <c:if test="${actualOut.reviewType==0}">
                                        <!-- update by shijun.liu 2016.01.18 -->
                                        <!-- 旧审批的审批状态 -->
                                        <c:if test="${empty actualOut.payReviewUuid and actualOut.isNew ne '2' }">
                                            ${fns:getNextPayReview(actualOut.id)}
                                        </c:if>
                                        <!-- 新审批 但是没有提交成本付款申请  -->
                                        <c:if test="${empty actualOut.payReviewUuid and actualOut.isNew eq '2' }">
                                            待提交审批
                                        </c:if>
                                        <c:if test="${not empty actualOut.payReviewUuid }">
                                            <c:set value="${fns:getReviewNewByUuid(actualOut.payReviewUuid) }" var="reviewNew"></c:set>
                                            ${fns:getChineseReviewStatus(reviewNew.status,reviewNew.currentReviewer)}
                                        </c:if>
                                    </c:if>
                                    <!-- 退款返佣的审批状态 -->
                                    <c:if test="${actualOut.reviewType!=0}">
                                        <c:if test="${actualOut.isNew ne '2' }">
                                            ${actualOut.reviewStatus}
                                        </c:if>
                                        <c:if test="${actualOut.isNew eq '2' }">
                                            <c:set value="${fns:getReviewNewByUuid(actualOut.reviewUuid) }" var="reviewNew"></c:set>
                                            <font color="green">${fns:getChineseReviewStatus(reviewNew.status,reviewNew.currentReviewer)}</font>
                                        </c:if>
                                    </c:if>
                                </td>
                                <td class="tc">
                                    <c:if test="${fns:isCurrentReviewer(actualOut.payReviewUuid) and flag eq 'review'}">
                                        <a href="javascript:void(0)" onclick="pass_deny('pass', this)">通过</a>&nbsp;
                                        <a href="javascript:void(0)" onclick="pass_deny('deny', this)">驳回</a>
                                    </c:if>
                                    <a href="javascript:void(0)" onclick="showDownloadWin('${ctx}','${actualOut.costVoucher }',false)">下载附件</a>
                                </td>
                                <td>
                                    <c:if test="${fns:isCurrentReviewer(actualOut.payReviewUuid) and flag eq 'review'}">
                                        <input type="checkbox" value="${actualOut.payReviewUuid}" name="actualId"/>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${! empty list.OutList}">
                            <tr>
                                <td>境外小计</td>
                                <td colspan="11">&nbsp;<span>${list.OutSum}</span></td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if> --%>
<%-- <c:if test="${fns:getUser().company.uuid ne '7a81b21a77a811e5bc1e000c29cf2586'}"> --%>
                <table class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan">
                    <thead>
                        <tr>
                            <th width="8%">境内/外项目</th>
                            <th width="15%">项目名称</th>
                            <th width="5%">数量</th>
                            <th width="11%">批发商/地接社</th>
                            <th width="8%">币种</th>
                            <th width="10%">单价</th>
                            <th width="10%">总价</th>
                            <th width="8%">备注</th>
                            <th width="7%">付款审批状态</th>
                            <th width="8%">操作</th>
                            <th width="5%">选择</th>
                        </tr>
                        <c:forEach items="${actualInList}" var="actualIn" varStatus="status">
                        <tr class="actualInCost inTr">
                        <c:if test="${status.count eq 1}">
                         <td rowspan="${fn:length(actualInList)}">境内付款明细</td>
                         </c:if>
                        <td name="tdName">${actualIn.name}</td>
                        <td class="tr" name="tdAccount">${actualIn.quantity}</td>
                        <td name="tdSupply">
                        <c:choose>
						   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && actualIn.supplyName=='非签约渠道'}"> 
						       直客
						   </c:when>
						   <c:otherwise>
						        ${actualIn.supplyName}
						   </c:otherwise>
					</c:choose> 
                        </td>
                        <td name="tdCurrencyName">
                        <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualIn.currencyId}">${currency.currencyName}</c:if>
                        </c:forEach>
                        </td>
                        <td class="tr" name="tdPrice">
                        <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualIn.currencyId}">
                             ${currency.currencyMark}<fmt:formatNumber value="${actualIn.price}" pattern="###,##0.00"></fmt:formatNumber>
                          </c:if>
                        </c:forEach></td>
                        <td class="tr">
                        <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualIn.currencyId}">
                             ${currency.currencyMark}<fmt:formatNumber value="${actualIn.price * actualIn.quantity}" pattern="###,##0.00"></fmt:formatNumber>
                          </c:if>
                        </c:forEach></td>
                        <td name="tdComment">${actualIn.comment}</td>
                        <td class="tc" id="review" name="review">
	                   <!-- 成本付款审批状态 -->
						<c:if test="${actualIn.reviewType==0}">
							<!-- update by shijun.liu 2016.01.18 -->
							<!-- 旧审批的审批状态  -->
							<c:if test="${empty actualIn.payReviewUuid and actualIn.isNew ne '2' }">
								${fns:getNextPayReview(actualIn.id)}
							</c:if>
							<!-- 新审批 但是没有提交付款申请  -->
							<c:if test="${empty actualIn.payReviewUuid and actualIn.isNew eq '2' }">
								待提交审批
							</c:if>
							<c:if test="${not empty actualIn.payReviewUuid }">
								<c:set value="${fns:getReviewNewByUuid(actualIn.payReviewUuid) }" var="reviewNew"></c:set>
                         		${fns:getChineseReviewStatus(reviewNew.status,reviewNew.currentReviewer)}
							</c:if>
						</c:if>
						<!-- 退款返佣的审批状态 -->
						<c:if test="${actualIn.reviewType!=0}">
							<c:if test="${actualIn.isNew ne '2' }">
								${actualIn.reviewStatus}
							</c:if>
							<c:if test="${actualIn.isNew eq '2' }">
								<c:set value="${fns:getReviewNewByUuid(actualIn.reviewUuid) }" var="reviewNew"></c:set>
								<font color="green">${fns:getChineseReviewStatus(reviewNew.status,reviewNew.currentReviewer)}</font>
							</c:if>
						</c:if>
                        </td>
                        <td class="tc">
                        	<c:if test="${fns:isCurrentReviewer(actualIn.payReviewUuid) and flag eq 'review'}">
                        		<a href="javascript:void(0)" onclick="pass_deny('pass', this)">通过</a>&nbsp;
                               	<a href="javascript:void(0)" onclick="pass_deny('deny', this)">驳回</a>
                        	</c:if>
                        	<a href="javascript:void(0)" onclick="showDownloadWin('${ctx}','${actualIn.costVoucher }',false)">下载附件</a>
                        </td>
                        <td>
                        	<c:if test="${fns:isCurrentReviewer(actualIn.payReviewUuid) and flag eq 'review'}">
                        		<input type="checkbox" value="${actualIn.payReviewUuid}" name="actualId"/>
                        	</c:if>
                        </td>                          
                       </tr>
                        </c:forEach>                  
                    <c:if test="${not empty actualInList}">
                    <tr class="inTr">
                        <td>境内小计</td>
                        <td colspan="10">&nbsp;<span id="actualInShow" name="actualInShow"/></td>
                    </tr>
                  </c:if>
                     <c:forEach items="${actualOutList}" var="actualOut" varStatus="status">
                        <tr class="actualOutCost outTr">
                        <c:if test="${status.count==1}">
                         <td rowspan="${fn:length(actualOutList)}">境外付款明细</td>
                         </c:if>
                        <td name="tdName">${actualOut.name}</td>
                        <td class="tr" name="tdAccount">${actualOut.quantity}</td>
                        <td name="tdSupply">
                        <c:choose>
						   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && actualOut.supplyName=='非签约渠道'}"> 
						       直客
						   </c:when>
						   <c:otherwise>
						        ${actualOut.supplyName}
						   </c:otherwise>
					</c:choose> 
                        </td>
                        <td name="tdCurrencyName">
                        <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualOut.currencyId}">
                             ${currency.currencyName}
                          </c:if>
                        </c:forEach>
                        </td>
                        <td class="tr" name="tdPrice"> <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualOut.currencyId}">
                             ${currency.currencyMark}${actualOut.price}
                          </c:if>
                        </c:forEach></td>
                        <td class="tr"> <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualOut.currencyId}">
                             ${currency.currencyMark}${actualOut.price * actualOut.quantity}
                          </c:if>
                        </c:forEach></td>
                         <td name="tdComment">${actualOut.comment}</td>
                         <td class="tc" width="8%" id="review" name="review">
                         <!-- 成本付款审批状态 -->
						<c:if test="${actualOut.reviewType==0}">
							<!-- update by shijun.liu 2016.01.18 -->
							<!-- 旧审批的审批状态 -->
							<c:if test="${empty actualOut.payReviewUuid and actualOut.isNew ne '2' }">
								${fns:getNextPayReview(actualOut.id)}
							</c:if>
							<!-- 新审批 但是没有提交成本付款申请  -->
							<c:if test="${empty actualOut.payReviewUuid and actualOut.isNew eq '2' }">
								待提交审批
							</c:if>
							<c:if test="${not empty actualOut.payReviewUuid }">
								<c:set value="${fns:getReviewNewByUuid(actualOut.payReviewUuid) }" var="reviewNew"></c:set>
                         		${fns:getChineseReviewStatus(reviewNew.status,reviewNew.currentReviewer)}
							</c:if>
						</c:if>
						<!-- 退款返佣的审批状态 -->
						<c:if test="${actualOut.reviewType!=0}">
							<c:if test="${actualOut.isNew ne '2' }">
								${actualOut.reviewStatus}
							</c:if>
							<c:if test="${actualOut.isNew eq '2' }">
								<c:set value="${fns:getReviewNewByUuid(actualOut.reviewUuid) }" var="reviewNew"></c:set>
								<font color="green">${fns:getChineseReviewStatus(reviewNew.status,reviewNew.currentReviewer)}</font>
							</c:if>
						</c:if>
                        </td>
                      <td class="tc">
                      	<c:if test="${fns:isCurrentReviewer(actualOut.payReviewUuid) and flag eq 'review'}">
                      		<a href="javascript:void(0)" onclick="pass_deny('pass', this)">通过</a>&nbsp;
                      		<a href="javascript:void(0)" onclick="pass_deny('deny', this)">驳回</a>
                      	</c:if>
                      	<a href="javascript:void(0)" onclick="showDownloadWin('${ctx}','${actualOut.costVoucher }',false)">下载附件</a>
                      </td>
                        <td>
                          	<c:if test="${fns:isCurrentReviewer(actualOut.payReviewUuid) and flag eq 'review'}">
                          		<input type="checkbox" value="${actualOut.payReviewUuid}" name="actualId"/>
                          	</c:if>
                        </td>                         
                       </tr>
                        </c:forEach>                        
                    </tbody>
                    <tfoot>
                    <c:if test="${not empty actualOutList}">
                    <tr class="outTr">
                        <td>境外小计</td>
                        <td colspan="10">&nbsp;<span id="actualOutShow" name="actualOutShow"/></td>
                    </tr></c:if>
                    </tfoot>
                </table>
<%-- </c:if> --%>
                  <div class="costSum clearfix" style=" width: 98%; margin: 0px auto; padding:10px 0px 0px 0px;">
          <ul class="cost-ul" data-total="cost">
          <ul class="cost-ul" data-total="income">
                      <li>实际总收入：&nbsp;¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${fns:getSum(incomeList,'accountedMoney') }"/></li>                   
                    </ul>
                      <li>实际总成本：&nbsp;¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${fns:getSum(actualCost,'cost')}"/></li>               
                     </ul>
                  <ul class="cost-ul" data-total="profit">
                      <li>实际总毛利：¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${fns:getSum(incomeList,'accountedMoney')-fns:getSum(actualCost,'cost') }"/></li>                      
                     </ul> 
                    <ul style="margin: 0px;float: right;">
                    <c:if test="${flag eq 'review' }">
                    	<input class="btn-primary" type="button" value="审批通过" onclick="batch_review()" />   
                    </c:if>
                    </ul>  
               </div>   
         <div class="mod_information">
          <div class="mod_information_d">
            <div class="ydbz_tit">审批日志</div>
          </div>
        </div>    
        <div style="margin:0 auto; width:98%;">
        	<%@ include file="/WEB-INF/views/review/common/cost_payment_review_log.jsp"%>
		</div>
        <div class="release_next_add">
           <input class="btn btn-primary" type="button" value="关 闭"  onclick="javascript:window.opener=null;window.open('','_self');window.close();" /> 
        </div>
    </div>
<script type="text/javascript">

//运控-成本录入-添加项目--小计
function costSums(obj,objshow){    
  var objMoney = {}; 
     obj.each(function(index, element) { 
    //var currencyName = $(element).find("td[name='tdCurrencyName']").text();
    var thisAccount = $(element).find("td[name='tdAccount']").text();

    var thisPrice = $(element).find("td[name='tdPrice']").text();

    var review = $(element).find("td[name='review']").text();
    var border=2;
    //去掉两边空格
    thisPrice=thisPrice.replace(/(^\s*)|(\s*$)/g, "");
    //找到金额中第一个数字位置
        for(var i=0;i<thisPrice.length;i++){
         if(thisPrice.substring(i,i+1).match(/^[0-9].*$/)){
           border=i;      
       break;
      }
    }
        var currencyName =thisPrice.substring(0,border);
    //金额去掉第一个字符(币种)
    thisPrice=thisPrice.substring(border);
         if(trimStr(review) != "取消申请") {
             if(typeof objMoney[currencyName] == "undefined"){
                 objMoney[currencyName] = parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(",",""),10);
             }else{
                 objMoney[currencyName] += parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(",",""),10);
             }
         }

  });
  //输出结果
  var strCurrency = "";
  var sign = " + "; 
  for(var i in objMoney){
    var isNegative = /^\-/.test(objMoney[i]);
    if(isNegative){
      sign = " - ";
    }
    if(strCurrency != '' || (strCurrency == '' && isNegative)){
      strCurrency += sign;
    }
    strCurrency += i + milliFormat(objMoney[i].toString().replace(/^\-/g,''),'1');
  }
  if(objshow.length>0) objshow.text("  "+strCurrency);
}

$(function(){
    //实际成本录入-境内小计
    costSums($('tr.actualInCost'),$("#actualInShow"));
  	//实际成本录入-境外小计  
    costSums($('tr.actualOutCost'),$("#actualOutShow")); 

});

function trimStr(str){return str.replace(/(^\s*)|(\s*$)/g,"");}
</script>
    
</body>
</html>
