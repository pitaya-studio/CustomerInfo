<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/include/head-wholesaler.jsp" %>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<div class="cwxt-qbdd-ellipsis activitylist_paixu_left">
			<span> <label>应收总额: </label>
				${totalMoney }
			</span>
			<span><label>应付总额: </label>
				${costTotalStr }
			</span>
			<span><label>利润总额:</label>
				${profitTotal }
			</span>
  <span></span>
</div>
<div class="cwxt-qbdd-ellipsis activitylist_paixu_left">
			<span><label>实收总额: </label>
				${realReceiveMoney }
			</span>
			<span><label>实付总额:</label>
				${realPayedMoney }
			</span>
			<span><label>实际总利润: </label>
				${realProfit }
			</span>
		    <span><label>总人数:</label>
		    	${totalPerson }
		    </span>
</div>