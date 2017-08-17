<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>签证押金转担保</title>
    <meta name="decorator" content="wholesaler"/>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(function () {
            //搜索条件筛选
            launch();
            //操作浮框
            operateHandler();
            //团号和产品切换
            switchNumAndPro();
            //取消一个checkbox 就要联动取消 全选的选中状态
            $("input[name='ids']").click(function () {
                if ($(this).attr("checked")) {

                } else {
                    $("input[name='allChk']").removeAttr("checked");
                }
            });


            $('#contentTable').on('change', 'input[type="checkbox"]', function () {
                if ($('#contentTable input[type="checkbox"]').length == $('#contentTable input[type="checkbox"]:checked').length) {
                    $('[name="allChk" ]').attr('checked', true);
                } else {
                    $('[name="allChk" ]').removeAttr('checked');
                }
            });


            var _$orderBy = $("#orderBy").val();
            if(_$orderBy==""){
                _$orderBy="rn.update_date DESC";
            }
            var orderBy = _$orderBy.split(" ");   //
            $(".activitylist_paixu_left li").each(function(){
                $(this).className
                if ($(this).hasClass(orderBy[0])){
                    orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="DESC"?"down":"up";
                    $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
                    $(this).attr("class","activitylist_paixu_moren");
                }
            });
            
            $("select[name='agent']").comboboxInquiry();
            

        });

        //展开收起
        function expand(child, obj) {
            if ($(child).is(":hidden")) {
                $(child).show();
                $(obj).parents("td").addClass("td-extend");
                $(obj).parents("tr").addClass("tr-hover");

            } else {
                $(child).hide();
                $(obj).parents("td").removeClass("td-extend");
                $(obj).parents("tr").removeClass("tr-hover");

            }
        }
        function checkall(obj) {
            if ($(obj).attr("checked")) {
                $('#contentTable input[type="checkbox"]').attr("checked", 'true');
                $("input[name='allChk']").attr("checked", 'true');
            } else {
                $('#contentTable input[type="checkbox"]').removeAttr("checked");
                $("input[name='allChk']").removeAttr("checked");
            }
        }

        function checkreverse(obj) {
            var $contentTable = $('#contentTable');
            $contentTable.find('input[type="checkbox"]').each(function () {
                var $checkbox = $(this);
                if ($checkbox.is(':checked')) {
                    $checkbox.removeAttr('checked');
                } else {
                    $checkbox.attr('checked', true);
                }
            });
        }
        function page(n,s){
            contextPath = "${ctx}";
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            var timstamp = (new Date()).valueOf();
            $('#searchForm').attr("action",contextPath + "/order/manager/visaNew/showDepositToWarrantList?timestamp="+timstamp);
            $("#searchForm").submit();
            return false;
        }
        function sortby(sortBy,obj){
            var temporderBy = $("#orderBy").val();
            if(temporderBy.match(sortBy)){
                sortBy = temporderBy;
                if(sortBy.match(/DESC/g)){
                    sortBy = sortBy.replace(/DESC/g,"");
                }else{
                    sortBy = $.trim(sortBy)+" DESC";
                }
            }
            $("#orderBy").val(sortBy);
            $("#searchForm").submit();
        }
        /**
         * 根据记录的不同审批状态展示数据。
         * @param flag
         */
        function submitForm(flag)
        {
            $("#appliType").val(flag);
            $("#searchForm").submit();
        }
        
        
      //批量审核按钮 操作
        function multiReviewDepositToWarrant(ctx,orderid,agentid,obj){
          var str="";
        	$('[name=ids]:checkbox:checked').each(function(){
        		str+=$(this).val()+",";
        	});
        	
        	var chosenNum = 0;
        	if("" == str){
        		$.jBox.tip("请选择需要审批的记录！"); 
        		return false;
        	}else{
        		chosenNum = str.split(",").length-1;
        	}
        			
        	$.jBox("iframe:"+ctx+"/orderPay/returnMoneyConfirm?orderid="+orderid+"&agentid="+agentid+"&chosenNum="+chosenNum,{		
        		    title: "批量审批",
        			width:830,
        	   		height: 300,
        	   		buttons:{'取消': 2,'驳回':0,'通过':1},
        	   		persistent:true,
        	   		loaded: function (h) {},
        	   		submit: function(v,h,f){
        	   			if(v==1 || v==0){
        	   			 var remarks= $(h.find("iframe")[0].contentWindow.remarks).val();
        			     dataparam={ 
              		 				  revids:str,
        						      remarks:remarks,
        						      result:v
        						  };
        			            
        			     $.ajax({ 
        			          type:"POST",
        			          url:ctx+"/review/deposit/multiReviewDepositToWarrant",
        			          dataType:"json",
        			          data:dataparam,
        			          success:function(data){
        			               $("#searchForm").submit();
        			          }
        			      });
        	   			}
        	   			
        	   		}
        	});
        }

    </script>
    <style type="text/css">
        .text-more-new .activitylist_team_co3_text, .text-more-new .activitylist_bodyer_right_team_co2 label {
            width: 90px;
            text-align: right;
        }

        .text-more-new .activitylist_bodyer_right_team_co1, .text-more-new .activitylist_bodyer_right_team_co3 {
            min-width: 200px;
        }

        .text-more-new .activitylist_bodyer_right_team_co2 {
            min-width: 400px;
        }
        .activitylist_paixu_right {
        	margin-top: -20px;
        }
    </style>
</head>
<body>
<div id="sea">
    <!--右侧内容部分开始-->
    <form:form id="searchForm" action="${ctx}/order/manager/visaNew/showDepositToWarrantList" method="post">

        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>


        <div class="activitylist_bodyer_right_team_co">
            <div class="activitylist_bodyer_right_team_co2 pr">
                <input class="txtPro inputTxt searchInput" id="orderNum" name="orderNum" value="" type="text" placeholder="请输入订单号"/>
                <%--<span style="display: block;" class="ipt-tips">订单号</span>--%>
            </div>
            <a class="zksx">筛选</a>
            <div class="form_submit">
                <input class="btn btn-primary" value="搜索" type="submit">
            </div>

            <div class="ydxbd "><!--text-more-new-->
                <span></span>
                <div class="activitylist_bodyer_right_team_co3">
                    <label class="activitylist_team_co3_text">审批发起人：</label>
                    <div class="selectStyle">
                        <select name="applyPromoter">
                                <option value="" selected="selected">请选择</option>
                                <c:forEach items="${appStarterList}" var="appStarter">
                                    <option value="${appStarter.appId}" <c:if test="${applyPromoter==appStarter.appId}">selected="selected"</c:if> > ${appStarter.appName}  </option>
                                </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <label class="activitylist_team_co3_text">渠道选择：</label>
                    <select name="agent" id="agent">
                        <option value="">全部</option>
                        <c:if test="${not empty fns:getAgentListOrderByFirstLetter() }">
                            <c:forEach items="${fns:getAgentListOrderByFirstLetter()}" var="agentinfo">
                                <option value="${agentinfo.id }" <c:if test="${agentinfo.id eq agent }">selected="selected"</c:if> >${agentinfo.agentName}</option>
                            </c:forEach>
                        </c:if>
                    </select>
                </div>
                
               <div class="activitylist_bodyer_right_team_co1">
                    <label class="activitylist_team_co3_text">游客：</label>
                   <div class="selectStyle">
                        <select name="travellerName">
                                <option value="" selected="selected">请选择</option>
                                <c:forEach items="${travellerList}" var="travellName">
                                    <option value="${travellName.traveller_id}" <c:if test="${travellerId==travellName.traveller_id}">selected="selected"</c:if> > ${travellName.traveller_name}  </option>
                                </c:forEach>
                        </select>
                   </div>
                </div>
              <%--   <div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text" style="width:85px;">渠道结算方式：</label>
						<select name="paymentType">
							<option value="">不限</option>
							<c:forEach var="pType" items="${fns:findAllPaymentType()}">
								<option value="${pType[0] }"
									<c:if test="${paymentType == pType[0]}">selected="selected"</c:if>>${pType[1]}</option>
							</c:forEach>
						</select>
				</div> --%>
                <div class="activitylist_bodyer_right_team_co1">
                    <label class="activitylist_team_co3_text">审批状态：</label>
                    <div class="selectStyle">
                        <select name="status">
                            <option value="" selected="selected">请选择</option>
                            <option value="0">审批驳回</option>
                            <option value="1">审批中</option>
                            <option value="2">审批通过</option>
                            <option value="3">取消申请</option>
                        </select>
                    </div>
                </div>
                 <div class="activitylist_bodyer_right_team_co1">
                    <label class="activitylist_team_co3_text">申请日期：</label>
                    <input id="timeBegin" class="inputTxt dateinput" name="timeBegin" value="" onclick="WdatePicker()" readonly="readonly"/>
                    <span> 至 </span>
                    <input id="timeEnd" class="inputTxt dateinput" name="timeEnd" value="" onclick="WdatePicker()" readonly="readonly"/>
                </div>
        </div>
          </div>

        <input type="hidden" name="appliType" id="appliType" value="">
    </form:form>
    <div class="activitylist_bodyer_right_team_co_paixu">
        <div class="activitylist_paixu">
            <div class="activitylist_paixu_left">
                <ul>
                    <%--<li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>--%>
                    <li class="activitylist_paixu_left_biankuang rn.create_date"><a onClick="sortby('rn.create_date',this)">创建时间</a></li>
                    <li class="activitylist_paixu_left_biankuang rn.update_date"><a onClick="sortby('rn.update_date',this)">更新时间</a></li>
                </ul>
            </div>
            <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
            <div class="kong"></div>
        </div>
    </div>
    <!--状态开始-->
    <div class="supplierLine">
        <a onclick="submitForm(0)" id="all" <c:if test="${appliFlag eq 0}">class="select" </c:if> >全部</a>

        <a onclick="submitForm(1)" id="todo" <c:if test="${appliFlag eq 1}">class="select" </c:if> >待本人审核</a>

        <a onclick="submitForm(2)" id="todo"<c:if test="${appliFlag eq 2}">class="select" </c:if>>本人已审核</a>

        <a onclick="submitForm(3)" id="todoing" <c:if test="${appliFlag eq 3}">class="select" </c:if>>非本人审核</a>
    </div>

    <!--状态结束-->
    <table id="contentTable" class="table activitylist_bodyer_table mainTable">
        <thead>
        <tr>
            <th width="4%">序号</th>
            <th width="8%">订单号</th>
            <th width="8%">
            <span class="tuanhao on">
               <c:if test="${fns:getUser().company.uuid eq '7a816f5077a811e5bc1e000c29cf2586'}">订单团号</c:if>
               <c:if test="${fns:getUser().company.uuid ne '7a816f5077a811e5bc1e000c29cf2586'}">团号</c:if>
            </span> / <span class="chanpin">产品名称</span>
            </th>
            <th width="5%">申请时间</th>
            <th width="7%">审批发起人</th>
            <th width="6%">渠道商</th>
            <th width="6%">下单人</th>
            <th width="6%" class="tc">游客</th>
            <th width="7%">担保金额</th>
            <th width="6%">上一环节审批人</th>
            <th width="7%">审批状态</th>
            <th width="6%">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list }" var="orders" varStatus="s">
            <tr>
                <td><input type="checkbox" name="ids" value=""/>${s.count}</td>
                <td>
                        ${orders.order_no}
                </td>
                <td>
                    <div title="${orders.group_code}" class="tuanhao_cen onshow">${orders.group_code}</div>
                    <div title="${orders.product_name}" class="chanpin_cen qtip"><a href="#" target="_blank">${orders.product_name}</a></div>
                </td>
                <td class="p0">
                    <div class="out-date"> <fmt:formatDate pattern="yyyy-MM-dd" value="${orders.create_date}" /> </div>
                    <div class="close-date time"> <fmt:formatDate pattern="HH:mm:ss" value="${orders.create_date}" /> </div>
                </td>
                <td class="tc">${orders.create_by}</td>
                <td class="tc">${orders.extend_5}</td>
                <td class="tc">${orders.order_creator_name}</td>
                <td class="tc">${orders.traveller_name}</td>
                <td class="tr">${orders.extend_4}<span class="fbold tdred">${orders.extend_3}</span></td>
                <td class="tc">${fns:getUserById(orders.last_reviewer).getName()}</td>
                <td class="invoice_yes tc">
                <c:if test="${orders.status eq 0}">审批驳回</c:if> 
                <c:if test="${orders.status eq 1}">审批中</c:if> 
                <c:if test="${orders.status eq 2}">审批通过</c:if> 
                <c:if test="${orders.status eq 3}">取消申请</c:if> 
                </td>
                <td class="p0">
                    <dl class="handle">
                        <dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"/></dt>
                        <dd class="">
                            <p><span></span>
                                <a href="javascript:void(0)">查看</a>
                                <a href="javascript:void(0)">撤销</a>
                            </p>
                        </dd>
                    </dl>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="page">
        <div class="pagination">
            <dl>
                <dt><input name="allChk" onclick="checkall(this)" type="checkbox">全选</dt>
                <dt><input name="reverseChk" onclick="checkreverse(this)" type="checkbox">反选</dt>
                <dd>
                    <%--<a target="_blank" id="piliang_o_${result.orderId}" onclick="javascript:multiReviewDepositToWarrant('${ctx}',1,1,this);" >批量审批</a>--%>
                    <input id="piliang_o_${result.orderId}"  onclick="javascript:multiReviewDepositToWarrant('${ctx}',1,1,this);"class="btn btn-primary" value="批量审批" type="button">
                </dd>
            </dl>
        </div>
        <div class="pagination">
            <div class="pagination clearFix">
                ${page}</div>
            <div style="clear:both;"></div>
        </div>
    </div>
    <!--右侧内容部分结束-->
</div>

<!--S批量审核操作弹出层-->

<div class="batch-verify-list" id="batch-verify-list" style="padding:20px;">
    <table width="100%" style="padding:10px !important; border-collapse: separate;">
        <tr>
            <td></td>
        </tr>
        <tr>
            <td><p>您好，当前您提交了23个审核项目，是否执行批量操作？</p></td>
        </tr>
        <tr>
            <td><p>备注：</p></td>
        </tr>
        <tr>
            <td><label>
                <textarea name="textfield" id="textfield" style="width: 290px;"></textarea>
            </label></td>
        </tr>
    </table>
</div>


</body>
</html>