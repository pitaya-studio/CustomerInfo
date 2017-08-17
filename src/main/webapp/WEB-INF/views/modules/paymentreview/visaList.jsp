<%@page import="com.trekiz.admin.modules.visa.entity.VisaPublicBulletin"%>
<%@page import="com.trekiz.admin.common.persistence.Page"%>
<%@page import="com.trekiz.admin.modules.visa.service.VisaPublicBulletinService"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="wholesaler"/>
	<title>签证付款审核</title>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/modules/visa/visaProductsList.js"></script>
	<script type="text/javascript">
	$(function(){
		$("div.message_box div.message_box_li").hover(function(){
			$("div.message_box_li_hover",this).show();
		},function(){
			$("div.message_box_li_hover",this).hide();
		});
		//搜索条件筛选
		launch();
		//产品名称文本框提示信息
		inputTips();
		//操作浮框
		operateHandler();

		var _$orderBy = $("#orderBy").val() || "id DESC";
		$("#orderBy").val(_$orderBy)
		var orderBy = _$orderBy.split(" ");
		$(".activitylist_paixu_left li").each(function(){
			if ($(this).hasClass("li"+orderBy[0])){
				orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
				$(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
				$(this).attr("class","activitylist_paixu_moren");
			}
		});

		//审核页签切换
		var _$review = $("#review").val() || '';
		$("#isRecord"+_$review).addClass("select"); 

		//如果展开部分有查询条件的话，默认展开，否则收起   
		var inputRequest = false;
		var selectRequest = false;
		$("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='sysCountryName']").each(function(){
			if($(this).val()){
				inputRequest = true;
				return false;
			}
		})
		$("#searchForm").find("select").each(function(){
			if($(this).children("option:selected").val()){
				selectRequest = true;
				return false;
			}
		})
		
		if(inputRequest||selectRequest){
			$('.zksx').click();
		}else{
			$('.zksx').text('展开筛选');
		}
	});

	function sortby(sortBy,obj){
		var temporderBy = $("#orderBy").val();
		if(temporderBy.match(sortBy)){
			sortBy = temporderBy;
			if(sortBy.match(/ASC/g)){
				sortBy = $.trim(sortBy.replace(/ASC/gi,"")) + " DESC";
			}else{
				sortBy = $.trim(sortBy.replace(/DESC/gi,""))+" ASC";
			}
		}else{
			sortBy = sortBy + " DESC";
		}
		$("#orderBy").val(sortBy);
		$("#searchForm").submit();
	}

	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
	}

	function changeIsRecord(itemRecord){
		$("#review").val(itemRecord);
		$(".supplierLine .select").removeClass("select");
		$("#isRecord"+itemRecord).addClass("select");
		$("#searchForm").submit();
	}

	function revokePayAudit(id, nowLevel, orderType){
		$.jBox.confirm("确定要撤销付款审核吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				$.ajax({
					type: "POST",
					url: "${ctx}/pay/review/revokePayAudit",
					cache:false,
					async:false,
					data:{id : id, nowLevel : nowLevel, orderType : orderType},
					success: function(e){
						window.location.reload();
					},
					error : function(e){
						top.$.jBox.tip('请求失败。','error');
						return false;
					}
				});
			}
		});
	}
	
	function jbox__shoukuanqueren_chexiao_fab(){
		var $contentTable = $('#contentTable');
		var items_val = '';
		var count = 0;
		$contentTable.find('input[name="batchItem"]').each(function(){
			if ($(this).is(":checked")){
				var temp = $(this).val();
				if(items_val == ''){
					items_val = temp;
				}else{
					items_val += '&&' + temp;
				}
				count++;
			}
		})
		if(count == 0){
			$.jBox.tip("请选择需要审核的项目", 'error');
			return false;
		}
		$('#batch-verify-list').find('p:first').text('您好，当前您提交了'+count+'个审核项目，是否执行批量操作？');
		$.jBox($("#batch-verify-list").html(),{
			title: "批量审批", buttons: { '取消': 0,'驳回': 1, '通过': 2 }, submit: function (v, h, f) {
				var token = $('#token').val() || "";
				if (v == "1") {
					var comment = f.comment_val;
					return _denyCost(items_val,comment,token);
				}else if(v == "2"){
					var comment = f.comment_val;
					return _passCost(items_val,comment,token);
				}
			}, height: 250, width: 350
		});
	}
	
	//付款审核-通过某条成本
	function _passCost(items_val,comment,token){
		$.ajax({
			type: "POST",
			url: "${ctx}/payment/review/batchPass",
			cache:false,
			dataType:"json",
			async:false,
			data:{items:items_val,comment:comment,token:token},
			success:function (data){
				if(data.flag){
					$.jBox.tip('审核通过', 'success');
					window.location.reload();
					return true;
				}else{
					$.jBox.tip(data.msg, 'error');
					return false;
				}
			},
			error: function (){
				$.jBox.tip("审核失败", 'error');
				return false;
			}
		});
	}
	
	//付款审核-拒绝某条成本
	function _denyCost(items_val,comment,token){
		$.ajax({
			type: "POST",
			url: "${ctx}/payment/review/batchDeny",
			cache:false,
			dataType:"json",
			async:false,
			data:{items:items_val,comment:comment,token:token},
			success:function (data){
				if(data.flag){
					$.jBox.tip('驳回成功', 'success');
					window.location.reload();
					return true;
				}else{
					$.jBox.tip(data.msg, 'error');
					return false;
				}
			},
			error: function (){
				$.jBox.tip("驳回失败", 'error');
				return false;
			}
		});
	}
	//全选&反选操作
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
			$checkbox.trigger('change');
		});
	}
	</script>
<style type="text/css">
	.message_box{width:100%;padding:0px 0 40px 0;}
	.message_box_li { width:246px;height:33px;float:left;margin:5px 20px 5px 0;}
	.message_box_li_a{max-width:240px;padding:0 5px;margin-top:9px;height:24px;line-height:24px;background:#a8b9d3;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px;color:#ffffff;float:left;position:relative;cursor:pointer;}
	.message_box_li .curret{background:#62afe7;}
	.message_box_li_em{background:#ff3333;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px;color:#ffffff;padding:2px;height:10px;min-width:14px;line-height:10px;text-align:center;float:left;position:absolute;z-index:4;right:-12px;top:-9px;font-size:12px; }
	.message_box_li_a span{float:left;}
	.message_box_li_hover{width:auto;line-height:24px;color:#5f7795;font-size:12px;border:1px solid #cccccc;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px; -webkit-box-shadow:0 0 2px #b2b0b1; -moz-box-shadow:0 0 2px #b2b0b1; box-shadow:0 0 2px #b2b0b1;position:absolute;z-index:999;background:#ffffff;cursor:pointer;left:-5px;padding:0 5px;display:none; }
</style>

</head>
<body>
<div class="message_box">
<c:forEach items="${userJobs}" var="role">
	<div class="message_box_li">
	<c:choose>
		<c:when test="${userJobId==role.id}">
			<a href="${ctx}/payment/review/visaList/${reviewLevel}?userJobId=${role.id}">
				<div class="message_box_li_a curret">
				<span>${fns:abbrs(role.deptName,role.jobName,40)}</span>
				<c:if test ="${role.count gt 0}">
				<div class="message_box_li_em" >${role.count}</div>
				</c:if>
				<c:if test="${fns:getStringLength(role.deptName,role.jobName) gt 37}">
				<div class="message_box_li_hover">${role.deptName}_${role.jobName}</div>
				</c:if>
				</div>
			</a>
		</c:when>
		<c:otherwise>
			<a href="${ctx}/payment/review/visaList/${reviewLevel}?userJobId=${role.id}">
				<div class="message_box_li_a">
				<span>${fns:abbrs(role.deptName,role.jobName,40)}</span>
				<c:if test ="${role.count gt 0}">
				<div class="message_box_li_em">${role.count}</div>
				</c:if>
				<c:if test="${fns:getStringLength(role.deptName,role.jobName) gt 37}">
				<div class="message_box_li_hover">${role.deptName}_${role.jobName}</div>
				</c:if>
				</div>
			</a>
		</c:otherwise>
	</c:choose>
	</div>
</c:forEach>  
</div>

<page:applyDecorator name="payment_review_head">
	<page:param name="current">visa</page:param>
</page:applyDecorator>
<div class="activitylist_bodyer_right_team_co_bgcolor">
	<form:form id="searchForm" modelAttribute="VisaProducts" action="${ctx}/payment/review/visaList/${reviewLevel}" method="post">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<input id="visaProductIds" type="hidden" name="visaProductIds" value="${visaProductIds}"/>
		<input id="review" name="review" type="hidden" value="${review}">
		<input id="reviewLevel" type="hidden" name="reviewLevel" value="${reviewLevel}">
		<input id="userJobId" name="userJobId"  type="hidden"  value="${userJobId}">
		<input id="token" name="token" type="hidden" value="${token}">
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr wpr20">
				<label>签证国家：</label>
				<!-- 添加签证国家/地区查询条件 -->
				<input style="width:260px" class="txtPro inputTxt inquiry_left_text" id="sysCountryName" name="sysCountryName" value="${sysCountryName}" type="text" flag="istips" />
			</div>
			<div class="form_submit">
				<input class="btn btn-primary" value="搜索" type="submit">
			</div>
			<a class="zksx">展开筛选</a>
			<div class="ydxbd">
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">地接社：</div>
					<select  name="supplierCompanyId" id="supplierCompanyId">
						<option value="">所有</option>
						<c:forEach items="${supplierList}" var="supplierlist">
							<option value="${supplierlist.id}" <c:if test="${supplierlist.id==supplierCompanyId}">selected="selected"</c:if>>${supplierlist.supplierName}</option>
						</c:forEach>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co3">
					<div class="activitylist_team_co3_text">领区：</div>
					<select name="collarZoning" id="collarZoning">
						<option value="">所有</option>
						<c:forEach items="${visaDistrictList}" var="visaDistrict">
							<option value="${visaDistrict.key}" <c:if test="${collarZoning eq visaDistrict.key}">selected</c:if>>
							<c:out value="${visaDistrict.value}" />
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">签证类型：</div>
					<select name="visaType" id="visaType">
						<option value="" >所有</option>
						<c:forEach items="${visaTypeList}" var="visaType">
							<option value="${visaType.key}"  <c:if test="${visaTypeId eq visaType.key}">selected</c:if>>
							<c:out value="${visaType.value}" />
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co1" >
					<div class="activitylist_team_co4_text" >审核发起人：</div>
					<input id="createByName" type="text" name="createByName" class="inputTxt inputTxtlong" value="${createByName }"/> 
				</div>
			</div>
		</div>
	</form:form>
	<div class="activitylist_bodyer_right_team_co_paixu">
		<div class="activitylist_paixu">
			<div class="activitylist_paixu_left">
				<ul>
					<li style="width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li><li class="activitylist_paixu_left_biankuang liid"><a onclick="sortby('id',this)">创建时间</a></li>
					<li class="activitylist_paixu_left_biankuang liupdateDate"><a onclick="sortby('updateDate',this)">更新时间</a></li>
					<li class="activitylist_paixu_left_biankuang livisaPay"><a onClick="sortby('visaPay',this)">应收价</a></li>
				</ul>
			</div>
			<div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong id="total">${page.count}</strong>&nbsp;条</div>
			<div class="kong"></div>
		</div>
	</div>

	<div class="supplierLine">
		<a href="javascript:void(0)" onclick="changeIsRecord(11)" id="isRecord11">全部</a>
		<a href="javascript:void(0)" onclick="changeIsRecord('')" id="isRecord">待本人审核</a>
		<a href="javascript:void(0)" onclick="changeIsRecord(22)" id="isRecord22">本人审核通过</a>
		<a href="javascript:void(0)" onclick="changeIsRecord(1)" id="isRecord1">审核中</a>
		<a href="javascript:void(0)" onclick="changeIsRecord(2)" id="isRecord2">已通过</a>
		<a href="javascript:void(0)" onclick="changeIsRecord(0)" id="isRecord0">未通过</a>
		<a href="javascript:void(0)" onclick="changeIsRecord(5)" id="isRecord5">已取消</a>
	</div>

	<table id="contentTable" class="table activitylist_bodyer_table">
		<thead>
			<tr>
				<th width="4%">序号</th>
				<th width="8%">成本名称</th>   
				<th width="8%">渠道商/地接社</th>  
				<th width="7%">产品名称</th> 
				<th width="6%">签证国家</th>
				<th width="6%">签证类型</th>
				<th width="6%">签证领区</th>
				<th width="6%">成本价格</th>
				<th width="6%">应收价格</th>
				<th width="6%">金额</th>
				<th width="7%">已付金额</th>
				<th width="6%">审核发起<br>时间</th>
				<th width="6%">审核发起<br>人</th>
				<th width="8%">审核状态</th>
				<th width="5%">审核</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="visaProduct" varStatus="s">
			<c:if test="${empty review}">
			<tr id="review${visaProduct.id}">
			</c:if>
			<c:if test="${not empty review}">
			<tr id="review${visaProduct.id}_${review}">
			</c:if>
				<td class="tc">
					<c:if test="${empty review}">
						<input type="checkbox" name="batchItem" value="${visaProduct.id},${reviewLevel},${reviewCompanyId},6" />
					</c:if>${s.count}
				</td>
				<td class="tc">${visaProduct.name}</td>
				<td class="tc">${visaProduct.supplyName}</td>
				<td class="activity_name_td">
				<a href="javascript:void(0)"  onclick="javascript:window.open('${ctx}/visa/preorder/visaProductsDetail/${visaProduct.visaId}')">
					${visaProduct.productName}</a>
				</td>
				<td>
				<c:forEach items="${visaCountryList}" var="country">
					<c:if test="${country.id eq visaProduct.sysCountryId}">${country.countryName_cn}</c:if>
				</c:forEach>
				</td>
				<td>
				<c:forEach items="${visaTypeList}" var="visas">
					<c:if test="${visas.key eq visaProduct.visaType}">${visas.value}</c:if>
				</c:forEach>
				</td>
				<td>
				<c:if test="${not empty visaProduct.collarZoning }">
					${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}
				</c:if>
				</td>
				<td class="tr">
				<c:forEach items="${curlist}" var="currency">
					<c:if test="${currency.id eq visaProduct.currencyId}">${currency.currencyMark}</c:if>
				</c:forEach>
				<font color="red"><fmt:formatNumber pattern="#.00" value="${visaProduct.visaPrice}" /></font>起
				</td>
				<td class="tr">
				<c:forEach items="${curlist}" var="currency">
					<c:if test="${currency.id eq visaProduct.currencyId}">${currency.currencyMark}</c:if>
				</c:forEach>
				<font color="green"><fmt:formatNumber pattern="#.00" value="${visaProduct.visaPay}" /></font>起
				</td>
				<td class="tc">${visaProduct.currencyMark} ${visaProduct.totalPrice}</td>
				<td class="tc">${fns:getPayCost(activity.id,typeId,1)}</td>
				<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${visaProduct.updateDate}"/></td>
				<td class="tc">${fns:getUserNameById(visaProduct.costCreateBy)}</td>
				<td id="result${visaProduct.id}">${fns:getNextPayReview(visaProduct.id)}</td>
				<td class="tc" id="change${visaProduct.id}">
				<c:if test="${ visaProduct.payNowLevel == reviewLevel && visaProduct.payReview==1 }">
					<a href="${ctx}/payment/review/visa/${visaProduct.visaId}/${reviewLevel}/${reviewCompanyId}" target="_blank">审核</a>
				</c:if>
				</td>
				<td class="p0">
					<a href="${ctx}/payment/review/visaRead/${visaProduct.visaId}/${reviewLevel}" target="_blank">查看</a>&nbsp;
					<br><a href="${ctx}/cost/manager/forcastList/${visaProduct.visaId}/6" target="_blank">预报单</a>&nbsp;
					<br><a href="${ctx}/cost/manager/settleList/${visaProduct.visaId}/6" target="_blank">结算单</a>&nbsp;  
					<c:if test="${visaProduct.payUpdateBy eq fns:getUser().id and visaProduct.payNowLevel ne 1 and visaProduct.payNowLevel-1 ne reviewCompany.topLevel and visaProduct.payReview ne 0 }">
						<a href="javascript:void(0)" onClick="revokePayAudit('${visaProduct.id}','${visaProduct.payNowLevel}','6')">撤销</a></c:if>  
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="page">
		<c:if test="${empty review}">
			<div class="pagination">
				<dl>
					<dt><input name="allChk" onclick="checkall(this)" type="checkbox">全选</dt>
					<dt><input name="reverseChk" onclick="checkreverse(this)" type="checkbox">反选</dt>
					<dd><a onclick="jbox__shoukuanqueren_chexiao_fab();">批量审批</a></dd>
				</dl>
			</div>
		</c:if>
		<div class="pagination">
			<div class="endPage">${page}</div>
			<div style="clear:both;"></div>
		</div>
	</div>
</div>
<div class="batch-verify-list" id="batch-verify-list" style="padding:20px;">
	<table width="100%" style="padding:10px !important; border-collapse: separate;">
		<tr>
			<td> </td>
		</tr>
		<tr>
			<td><p> </p></td>
		</tr>
		<tr>
			<td><p>备注：</p></td>
		</tr>
		<tr>
			<td>
				<label>
					<textarea name="comment_val" id="comment_val" style="width: 290px;"></textarea>
				</label>
			</td>
		</tr>
	</table>
</div>
</body>
</html>