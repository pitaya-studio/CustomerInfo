<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>付款审核</title>
    <meta name="decorator" content="wholesaler"/>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactor.js" type="text/javascript"></script>
    <script src="${ctxStatic}/json/jquery.json.ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>    
    <script src="${ctxStatic}/jquery-other/jquery-combobox_prop.js" type="text/javascript"></script>
    <script type="text/javascript">
   $(function(){
         $('.zksx').click(function(){
        if($('.ydxbd').is(":hidden")==true)
        {
          $('.ydxbd').show();
          $(this).text('收起筛选');
          $(this).addClass('zksx-on');
        }else
        {
          $('.ydxbd').hide();
          $(this).text('展开筛选');
          $(this).removeClass('zksx-on');
        }
      });

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
        
        /*
        if(!${isShow}){
            $("#shenhetongguo").attr("disabled","disabled")
        } */
       
        $("#contentTable").delegate(".chanpin","click",function(){
             $(this).addClass("on").siblings().removeClass('on');
             $('.tuanhao_cen').removeClass('onshow');
             $('.chanpin_cen').addClass('onshow');
             $("div.groupCode").hide();
             $("div.acitivityName").show();
        });        
        var ids = "";
        var names = "";
        <c:forEach var="data" items="${travelActivity.targetAreaList}" varStatus="d">
            ids = ids + "${data.id}"+",";
            names = names +"${data.name}"+",";
        </c:forEach>
       
        //$("#targetAreaName").text(names.toString());
         $("#targetAreaName").text(names.toString().substring(0,names.toString().length-1));
        $("#targetAreaName").attr('title',names.toString().substring(0,names.length-1));
        eventHandler('operatorBudget', $('#contentTable'));
    });
    
    function disButton(obj){
    
     //  alert(obj[0].name+obj[1]+obj[2]+obj[3]+obj[4]+obj[5]+obj[6]+obj[7]+"--")
    }
    
    
    function orderDetail(orderId){
        window.open("${ctx}/orderCommon/manage/orderDetail/"+orderId,"_blank");
    }
    var eventHandler = function(mark, $Table){
        var sum = 0;
        var income = 0;
        var gain = 0;
        var datas = $('[price-sum-bind=\'' + mark + '\']');
        
        for(var i = 0; i < datas.length; i++){
            sum += parseInt(datas[i].innerHTML);
        }
        var orders = $Table.find('td:nth-child(8) span');
        for(var i = 0; i < orders.length; i++){
            var orderPrice = orders[i].innerHTML.replace(new RegExp(',', 'gm'), "");
            gain += parseInt(orderPrice);
        }
        income = gain - sum;
        $('[price-sum-all=\'' + mark + '\']').html(sum);
        $('[price-sum-gain=\'' + mark + '\']').html(gain);
        $('[price-sum-income=\'' + mark + '\']').html(income);
    };
    
    /*
    * 按类型获取成本
    */
    /*
    var getCost = function(classType, $List){
       
        var groupId = '${activityGroup.id}';
        $.ajax({
            type: "GET",
            url: "${ctx}/payment/manager/getCosts/" + classType + "/" + groupId,
            dataType:"json",
            async:false,
            cache:false,
            data:{
                states : "start"
            },
            success: function(array){
                var htmldoc = "";
                for(var i = 0; i < array.length; i++){
                    htmldoc += '<tr id=' + classType + "_" + array[i].id + '> <td>' + (i + 1) + '</td><td>'+array[i].name+'</td><td price-sum-bind="' + classType + '">'+array[i].price+'</td><td>'+array[i].comment+'</td><td><a href="javascript:void(0)" onclick="modifyCost('+ array[i].id +', \'' + classType + '\')">修改s</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="return deleteCost('+array[i].id+', \'' + classType + '\');" class="button_delcb"></a></td></tr>';
                }
                $List.append(htmldoc);
                eventHandler('operatorBudget', $('#contentTable'));
            }
         });
    }; */
    
    
  
    var deleteCost = function(id, classType){
        $.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f) {
            if (v == 'ok') {
                $.ajax({
                    type: "POST",
                    url: "${ctx}/payment/manager/delete",
                    cache:false,
                    async:false,
                    data:{id : id,
                        type : classType},
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
    };
    
    var save = function(){
        var groupId = '${activityGroup.id}';
        $.ajax({
            type: "POST",
            url: "${ctx}/payment/manager/flow/start/save/" + groupId,
            data : {types : "operator,operatorBudget"},
            async:false,
            success: function(e){
                if(e == "true"){
                    location.href = '${ctx}/payment/manager/list/operatorPre';
                }else{
                    top.$.jBox.tip('请求失败。','error');
                    return false;
                }
            }
         });
    };
    
    var submit = function(){
        var groupId = '${activityGroup.id}';
        $.ajax({
            type: "POST",
            url: "${ctx}/payment/manager/flow/start/commit/" + groupId,
            data : {types : "operator,operatorBudget"},
            async:false,
            success: function(e){
                if(e == "true"){
                    location.href = '${ctx}/payment/manager/list/operatorPre';
                }else{
                    top.$.jBox.tip('请求失败。','error');
                    return false;
                }
            }
         });
    };



    var denyActivityCost = function denyActivityCost(id) {        

           var html='<div id="moneyId" style=" margin:10px;" >'+  
           '<table border="0" width="96%">'+          
           '<tr><td align="right">项目备注：</td><td>'+
           '<textarea rows="3" cols="20" id="remark" name="remark"  style="width:180px"></textarea></td></tr>'+
           '</table></div> ';      
     
     var submit = function (v, h, f) {            
            if(v==0) {
              return true;
            }         
            var remark = f.remark;
            var remarkLength = remark.length;     
            if(remarkLength > 200){
                top.$.jBox.tip('项目备注不能大于200字','error' ,{ focusId: 'comment' }); return false;
            }       
            else {  
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/payment/review/denyactivitycost",
                        cache:false,
                        dataType:"json",
                        async:false,
                        data:{ 
                            remark : remark,id: id,reviewLevel:${reviewLevel},typeId:${typeId} },
                        success: function(array){
                            window.location.href = "${ctx}/payment/review/list/${typeId}/${reviewLevel}";
                           // window.location.reload();                          
                        },
                        error : function(e){
                            top.$.jBox.tip('请求失败。'+e,'error' ,{ focusId: 'price' });
                            return false;
                        }
                     });
                
            }
        };
        $.jBox(html, { title: '驳回备注', buttons:{ '取消' : 0,'确定' : 1 }, submit: submit, height:160});
    };  


//付款审核-通过
function passActivityCost(dom,id,review){ 
  $.ajax({
                     type: "POST",                      
                     url: "${ctx}/payment/review/passactivitycost",                      
                     cache:false,
                     dataType:"json",
                     async:false,
                     data:{  format:"json",id : id,typeId:${typeId},reviewLevel:${reviewLevel} },
                     success: function (data){
                      window.location.href = "${ctx}/payment/review/list/${typeId}/${reviewLevel}";
                      //window.location.reload();
                     },
                     error: function (){
                     alert('返回数据失败');
                       }
                }); 
}



//成本审核-拒绝某条成本
function denyCost(dom,id,reviewLevel){

  $.ajax({
                     type: "POST",                      
                     url: "${ctx}/payment/review/denyIsland",                      
                     cache:false,
                     dataType:"json",
                     async:false,
                     data:{  format:"json",id :id,reviewLevel:reviewLevel, reviewCompanyId :${reviewCompanyId},orderType:${typeId}  },
                     success: function (data){
						window.opener.$("#searchForm").submit(); //刷新父窗口
						/*var doc = window.opener.document; 
						var review=doc.getElementById("review"+id);
						if(review!=null){
							//待本人审核移除刚审核通过的列
                            review.style.display='none';
							var $td = $(doc.getElementById("total")); 
						    $td.html($td.html()-1) ;
 						}else{
                            //更新审核状态
						    var $td = $(doc.getElementById("result"+id));
                            $td.html(data[0].result);
                            var $ch = $(doc.getElementById("change"+id));
                            $ch.html("");
						} */
						window.location.reload();
                     },
                     error: function (){
                     alert('返回数据失败');
                       }
                }); 
}

  var passCosts = function(id, classType){
       
            //if (v == 'ok') {
                $.ajax({
                    type: "POST",
                    url: "${ctx}/payment/manager/passCosts",
                    cache:false,
                    async:false,
                    data:{id : id,
                        type : classType},
                    success: function(e){
                          window.location.reload();                     
                        
                    },
                    error : function(e){
                        top.$.jBox.tip('请求失败。','error');
                        return false;
                    }
                 });
           // }       
    };

//成本审核-通过某条成本
function passCost(dom,id,reviewLevel){  	
  $.ajax({
                     type: "POST",                      
                     url: "${ctx}/payment/review/passIsland",                      
                     cache:false,
                     dataType:"json",
                     async:false,
                     data:{  format:"json",id : id, reviewLevel:reviewLevel, reviewCompanyId :${reviewCompanyId},orderType:${typeId} },
                     success: function (data){ 
						window.opener.$("#searchForm").submit(); //刷新父窗口
						/*var doc = window.opener.document; 
						var review=doc.getElementById("review"+id);
						if(review!=null){
							//待本人审核移除刚审核通过的列
                            review.style.display='none';
							var $td = $(doc.getElementById("total")); 
						    $td.html($td.html()-1) ;
 						}else{
                            //更新审核状态
						    var $td = $(doc.getElementById("result"+id));
                            $td.html(data[0].result);
                            var $ch = $(doc.getElementById("change"+id));
                            $ch.html("");
						} */
						window.location.reload();
                     },
                     error: function (){
                     alert('返回数据失败');
                       }
                }); 
}

function selectchange(parentId)
{ 
if(null!= parentId && ""!=parentId){  
  var noCache = Date(); 
  $.getJSON("${ctx}/payment/manager/supplylist/"+parentId,{"noCache":noCache},function(myJSON){ 
  var options=""; 
  if(myJSON.length>0){ 
  options+="<option value=''>==请选择类型==</option>"; 
  for(var i=0;i<myJSON.length;i++){ 
   options+="<option value="+myJSON[i].supplierid+">"+myJSON[i].suppliername+"</option>"; 
   } 
  $("#supplier").html(options); 
  $("#second").show(); 
  }  
   else if(myJSON.length<=0){ 
   //$("#second").hide(); 
   } 
  });
  }
}

function tosales()
 {  
   $("#sales").show(); 
   $("#supply").hide();
   $("#supplyid").hide();  
 }

 function tosupply(){
   $("#sales").hide(); 
   $("#supply").show();
   $("#supplyid").show();    
}

 function saveCheckBox(id){   
    var tmp=0;
    $("input[name='"+id+"']").each(function(){ 
    if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
           tmp=tmp +","+$(this).attr('value')
       }
    });    
    if(tmp=="0"){
        alert("请选择成本");
         return;
    }
   
    $.ajax({
                        type: "POST",
                        url: "${ctx}/payment/review/saveCostIslandList",
                        cache:false,
                        dataType:"json",
                        async:false,
                        data:{ 
                            costList:tmp, reviewCompanyId :${reviewCompanyId},orderType:${typeId}   },
                        success: function(data){
                             window.opener.$("#searchForm").submit(); //刷新父窗口
							  /* var doc = window.opener.document;
                              for(var i = 0; i < data.length; i++){
								  var review=doc.getElementById("review"+data[i].costid);								 
								  if(review!=null){	
									  //待本人审核移除刚审核通过的列									 
                                    review.style.display='none';
							        var $td = $(doc.getElementById("total")); 
						            $td.html($td.html()-1) ;                           
						         }else{
                                      //更新审核状态
                                    var $td = $(doc.getElementById("result"+id));
                                    $td.html(data[0].result);
                                    var $ch = $(doc.getElementById("change"+id));
                                    $ch.html("");
                                 }
                              } */
					          window.location.reload();      

                        },
                        error : function(e){
                            alert('请求失败。');                         
                            return false;
                        }
                     });

 }  

</script>
</head>
<body>

                    
       <page:applyDecorator name="payment_review_head">
             <page:param name="current">island</page:param>
       </page:applyDecorator>
      
     <div class="mod_nav">财务 > 财务审核 > 付款审核 > 海岛付款审核</div>
    <div class="produceDiv">
        <div class="mod_information">
            <div class="mod_information_d">
                <div class="ydbz_tit">产品基本信息</div>
            </div>
        </div>
           <div class="mod_information_dzhan">
            <div class="mod_information_dzhan_d mod_details2_d">
                <span style="color: #3a7851; font-size: 16px; font-weight: bold;">${activityIsland.activityName}</span>
                <div class="mod_information_d7"></div>
                <table border="0" width="90%">
                       <tr>
                        <td class="mod_details2_d1">团号：</td>
                        <td class="mod_details2_d2">${activityGroup.groupCode }</td>                <td class="mod_details2_d1">计调员：</td>
                        <td class="mod_details2_d2">${createByName}</td>
						<td></td>
						<td></td>
                    </tr>
                    <tr>
                        <td class="mod_details2_d1">国家：</td>
                        <td class="mod_details2_d2">${countryName }</td>
                        <td class="mod_details2_d1">岛屿：</td>
                        <td class="mod_details2_d2">${island.islandName }</td>
                        <td class="mod_details2_d1">酒店名称：</td>
                        <td class="mod_details2_d2">${hotel.nameCn}</td>
                        <td class="mod_details2_d1">酒店星级：</td>
                        <td class="mod_details2_d2">
						<span class="y_xing"> 
								<c:forEach begin="1" end="${star}">
									★
								</c:forEach>
							</span>
						</td>
                    </tr>
                    <tr>
					    <td class="mod_details2_d1">日期：</td>
                        <td class="mod_details2_d2"><fmt:formatDate value="${activityIslandGroupAirline.departureTime }" /></td>
                       <td class="mod_details2_d1">上岛方式：</td>
                        <td class="mod_details2_d2"><trekiz:autoId2Name4Table tableName="sysdefinedict" sourceColumnName="uuid" srcColumnName="label" value="${activityGroup.islandWay}"/> </td>
                        <td class="mod_details2_d1">航空公司：</td>
                        <td class="mod_details2_d2"><trekiz:autoId2Name4Table tableName="sys_airline_info" sourceColumnName="airline_code" srcColumnName="airline_name" value="${activityIslandGroupAirline.airline}"/></td>					  
                        <td class="mod_details2_d1">参考航班：</td>
                        <td class="mod_details2_d2">${activityIslandGroupAirline.flightNumber }</td>
                     </tr>
					 <tr>
                        <td class="mod_details2_d1">起飞时间：</td>
                        <td class="mod_details2_d2"><fmt:formatDate value="${activityIslandGroupAirline.departureTime }" pattern="yyyy/MM/dd HH:mm:ss" /></td>
                        <td class="mod_details2_d1">到达时间：</td>
                        <td class="mod_details2_d2"><fmt:formatDate value="${activityIslandGroupAirline.arriveTime }" pattern="HH:mm" />+${activityIslandGroupAirline.dayNum }天</td>
					    <td class="mod_details2_d1">余位：</td>
                        <td class="mod_details2_d2">${activityIslandGroupAirline.remNumber } </td>
					    <td class="mod_details2_d1">预收：</td>
                        <td class="mod_details2_d2">${activityGroup.advNumber }</td>             </tr> 
					  <tr>  
					    <td class="mod_details2_d1">预报名：</td>
                        <td class="mod_details2_d2">${bookingPersonNum }</td>
				    </tr>
                </table>
               <table   class="table activitylist_bodyer_table_new contentTable_preventive"  style="width:60% !important;">
					<thead>
						<tr>
							<th width="30%">房型&晚数</th>
							<th width="30%">基础餐型</th>
							<th>升级餐型&升级价格</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${rms}" var="rm">
							<tr>
								<td>${rm.room_name } ${rm.nights }</td><td>${rm.mlabel }</td><td>${rm.mrlabel } ${fns:getCurrencyNameOrFlag(rm.currency_id, '0') }${rm.price }/人</td>
							</tr>
						</c:forEach>
					</tbody>
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
              <th width="8%">已付金额<br>到账金额</th> 
              <th width="6%">操作</th>            
            </tr>
          </thead>
          <tbody>
          <c:forEach items="${orderList}" var="groupOrder">
            <tr >
              <td>${fns:getAgentName(groupOrder.orderCompany)}</td>
              <td>${groupOrder.orderNum}</td>
              <td class="tc"><div class="groupCode">${groupOrder.groupCode}</div>  <div  style="display:none;" class="acitivityName">${groupOrder.acitivityName}</div></td>
              <td class="tc">              
                  <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${groupOrder.orderTime }"/>                
              </td>
              <td style="padding: 0px;">
                <div class="out-date">${groupOrder.groupOpenDate}</div>
                <div class="close-date">${groupOrder.groupCloseDate}</div>
              </td>
              <td>
             ${groupOrder.orderPersonNum}
              </td>
              <td>${fns:getDictLabel(groupOrder.payStatus, "order_pay_status", "")}</td>
              <td class="tr"><span class="tdorange fbold">${groupOrder.totalMoney} </span></td>
              <td class="p0 tr">  
                <div class="yfje_dd"> 
                  <span class="fbold">${groupOrder.payedMoney}</span>
                </div>
                <div class="dzje_dd">
                  <span class="fbold">${groupOrder.accountedMoney}</span>
                </div>
              </td>              
              <td class="tc">
              	<a href="${ctx}/islandOrder/islandOrderDetail/${groupOrder.orderUuid }" target="_blank">详情</a>
              </td>
            </tr>
            </c:forEach>
          </tbody>
        </table>      
            <!--
            <div class="mod_information">
                    <div class="mod_information_d">
                        <div class="ydbz_tit">
                            预算成本
                    </div>
                </div>
                <table class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan">
                    <thead>
                        <tr>
                            <th width="10%">境内/外项目</th>

                            <th width="15%">项目名称</th>
                            <th width="8%">数量</th>
                            <th width="11%">批发商/地接社</th>

                            <th width="10%">币种</th>
                            <th width="10%">金额</th>
                            <th width="12%">备注</th>
                            <th width="7%">状态</th>
                            <th width="8%">审核操作</th>
                            <th width="5%">选择</th>
                        </tr>
                    </thead>             
                <tbody>
                        <c:forEach items="${budgetInList}" var="budgetIn" varStatus="status">
                        <tr class="budgetInCost">
                        <c:if test="${status.count==1}">
                         <td rowspan="${fn:length(budgetInList)}">境内付款明细</td>
                         </c:if>
                        <td name="tdName">${budgetIn.name}</td>
                        <td  class="tr" name="tdAccount">${budgetIn.quantity}</td>
                        <td name="tdSupply">
                        ${budgetIn.supplyName}
                         </td>
                        <td  name="tdCurrencyName">
                        <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==budgetIn.currencyId}">
                             ${currency.currencyName}
                          </c:if>
                        </c:forEach>
                        </td>
                        <td  class="tr" name="tdPrice">
                         <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==budgetIn.currencyId}">
                             ${currency.currencyMark}${budgetIn.price}
                          </c:if>
                        </c:forEach></td>
                          <td name="tdComment">${budgetIn.comment}</td>
                         <td  class="tc">
                         <c:if test="${budgetIn.reviewType==0}">
                         <c:forEach items="${review_cost}" var="review_cost">
                          <c:if test="${review_cost.value==budgetIn.review}">
                             ${review_cost.label}
                          </c:if>
                        </c:forEach>
                        </c:if>
                         <c:if test="${budgetIn.reviewType!=0}">
                         ${budgetIn.reviewStatus}
                         </c:if>
                        </td>
                        <td class="tc">                         
                          <c:if test="${budgetIn.review ==1 }">
                           <c:forEach items="${levelList}" var="levelList">
                           <c:if test="${levelList==budgetIn.nowLevel}">
                               <a href="javascript:void(0)" onclick="passCost(this,'${budgetIn.id}','${budgetIn.nowLevel}')">通过</a>&nbsp;<a href="javascript:void(0)" onclick="denyCost(this,'${budgetIn.id}','0')">拒绝</a>
                             </c:if>
                          </c:forEach>
                         </c:if>
                        <input type="hidden" id="itemId" value="${budgetIn.id}"/>
                        </td>
                        <td>                         
                          <c:if test="${budgetIn.review ==1 }">
                           <c:forEach items="${levelList}" var="levelList">
                           <c:if test="${levelList==budgetIn.nowLevel}">  
                          <input type="checkbox" value="${budgetIn.id}"  name="budgetId"/>
                             </c:if>
                          </c:forEach>
                         </c:if>
                        </td>                          
                       </tr>
                        </c:forEach>                   
                    <c:if test="${! empty budgetInList}">
                    <tr>
                        <td>境内小计</td>
                        <td colspan="9">&nbsp;<span id="budgetInShow" name="budgetInShow"></span></td>
                    </tr></c:if>
                     <c:forEach items="${budgetOutList}" var="budgetOut" varStatus="status">
                        <tr class="budgetOutCost">
                        <c:if test="${status.count==1}">
                         <td rowspan="${fn:length(budgetOutList)}">境外付款明细</td>
                         </c:if>
                        <td name="tdName">${budgetOut.name}</td>
                        <td class="tr" name="tdAccount">${budgetOut.quantity}</td>
                        <td name="tdSupply">
                       ${budgetOut.supplyName}
                         </td>
                        <td name="tdCurrencyName">
                        <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==budgetOut.currencyId}">
                             ${currency.currencyName}
                          </c:if>
                        </c:forEach>
                        </td>
                        <td  class="tr" name="tdPrice"> <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==budgetOut.currencyId}">
                             ${currency.currencyMark}${budgetOut.price}
                          </c:if>
                        </c:forEach></td>
                         <td  name="tdComment">${budgetOut.comment}</td>
                         <td  class="tc">
                         <c:if test="${budgetOut.reviewType==0}">
                         <c:forEach items="${review_cost}" var="review_cost">
                          <c:if test="${review_cost.value==budgetOut.review}">
                             ${review_cost.label}
                          </c:if>
                        </c:forEach>
                        </c:if>
                         <c:if test="${budgetOut.reviewType!=0}">
                         ${budgetOut.reviewStatus}
                         </c:if>
                        </td>
                        <td class="tc">
                          <c:if test="${budgetOut.review ==1 }">
                           <c:forEach items="${levelList}" var="levelList">
                           <c:if test="${levelList==budgetOut.nowLevel}">
                                 <a href="javascript:void(0)" onclick="passCost(this,'${budgetOut.id}','${budgetOut.nowLevel}')">通过</a>&nbsp;<a href="javascript:void(0)" onclick="denyCost(this,'${budgetOut.id}','0')">拒绝</a>
                             </c:if>
                          </c:forEach>
                         </c:if>       
                           <input type="hidden" id="itemId" value="${budgetOut.id}"/>
                        </td>
                         <td>
                         <c:if test="${budgetOut.review ==1 }">
                           <c:forEach items="${levelList}" var="levelList">
                           <c:if test="${levelList==budgetOut.nowLevel}">  
                          <input type="checkbox" value="${budgetOut.id}"  name="budgetId"/>
                             </c:if>
                          </c:forEach>
                         </c:if>
                        </td>   
                       </tr>
                        </c:forEach>                        
                   
                    <c:if test="${! empty budgetOutList}">
                    <tr>
                        <td>境外小计</td>
                        <td colspan="9">&nbsp;<span id="budgetOutShow" name="budgetOutShow"></td>
                    </tr></c:if>
                    </tbody>
                </table>


             <div class="costSum  clearfix" style=" width: 98%; margin: 0px auto; padding:10px 0px 0px 0px;">
          <ul class="cost-ul" data-total="cost">
          <ul class="cost-ul" data-total="income">
                      <li>预计总收入：&nbsp;￥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${fns:getSum(incomeList,'totalMoney') }"/>
</li>                   
                    </ul>
                      <li>预计总成本：&nbsp;￥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${fns:getSum(budgetCost,'cost')}"/>
</li>               
                     </ul>
                  <ul class="cost-ul" data-total="profit">
                      <li>预计总毛利：￥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${fns:getSum(incomeList,'totalMoney')-fns:getSum(budgetCost,'cost') }"/>
</li>                      
                     </ul> 
                  <ul style="margin: 0px;float: right;">
                    <input class="btn-primary" type="button" value="审核通过"  onclick="saveCheckBox('budgetId')" />                        
                    </ul>               
        </div>
            -->
            <div class="mod_information">
                    <div class="mod_information_d">
                        <div class="ydbz_tit">
                              实际成本
                        </div>
                    </div>
                </div>
                <table class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan">
                    <thead>
                        <tr>
                            <th width="10%">境内/外项目</th>

                            <th width="15%">项目名称</th>
                            <th width="8%">数量</th>
                            <th width="11%">批发商/地接社</th>
                            <th width="10%">币种</th>
                            <th width="10%">金额</th>
                            <th width="12%">备注</th>
                            <th width="7%">付款审核状态</th>
                            <th width="8%">审核操作</th>
                            <th width="5%">选择</th>
                        </tr>
                
                        <c:forEach items="${actualInList}" var="actualIn" varStatus="status">
                        <tr class="actualInCost">
                        <c:if test="${status.count==1}">
                         <td rowspan="${fn:length(actualInList)}">境内付款明细</td>
                         </c:if>
                        <td name="tdName">${actualIn.name}</td>
                        <td class="tr" name="tdAccount">${actualIn.quantity}</td>
                        <td name="tdSupply">
                          ${actualIn.supplyName}
                         </td>
                        <td name="tdCurrencyName">
                        <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualIn.currencyId}">
                             ${currency.currencyName}
                          </c:if>
                        </c:forEach>
                        </td>
                        <td class="tr" name="tdPrice"> <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualIn.currencyId}">
                             ${currency.currencyMark}${actualIn.price}
                          </c:if>
                        </c:forEach></td>
                        <td name="tdComment">${actualIn.comment}</td>
                        <td  class="tc">
                        <c:if test="${actualIn.reviewType==0}">
                         ${fns:getNextPayIslandReview(actualIn.id)} 
                        </c:if>
                         <c:if test="${actualIn.reviewType!=0}">
                         <font color="green">已通过</font>
                         </c:if>
                        </td>
                        <td class="tc">                        
                         <c:if test="${actualIn.payReview ==1 }">
                           <c:forEach items="${levelList}" var="levelList">
                           <c:if test="${levelList==actualIn.payNowLevel}">
                               <a href="javascript:void(0)" onclick="passCost(this,'${actualIn.id}','${actualIn.payNowLevel}')">通过</a>&nbsp;<a href="javascript:void(0)" onclick="denyCost(this,'${actualIn.id}','0')">拒绝</a>
                             </c:if>
                          </c:forEach>
                         </c:if>  
                        </td>
                        <td>
                         <c:if test="${actualIn.payReview ==1 }">
                           <c:forEach items="${levelList}" var="levelList">
                           <c:if test="${levelList==actualIn.payNowLevel}">  
                          <input type="checkbox" value="${actualIn.id}"  name="actualId"/>
                             </c:if>
                          </c:forEach>
                         </c:if>
                        </td>                          
                       </tr>
                        </c:forEach>                  
                    <c:if test="${! empty actualInList}">
                    <tr>
                        <td>境内小计</td>
                        <td colspan="9">&nbsp;<span id="actualInShow" name="actualInShow"></td>
                    </tr>
                  </c:if>
                     <c:forEach items="${actualOutList}" var="actualOut" varStatus="status">
                        <tr class="actualOutCost">
                        <c:if test="${status.count==1}">
                         <td rowspan="${fn:length(actualOutList)}">境外付款明细</td>
                         </c:if>
                        <td name="tdName">${actualOut.name}</td>
                        <td class="tr" name="tdAccount">${actualOut.quantity}</td>
                        <td name="tdSupply">
                       ${actualOut.supplyName}
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
                         <td name="tdComment">${actualOut.comment}</td>
                         <td  class="tc" width="8%">
                         <c:if test="${actualOut.reviewType==0}">
                        ${fns:getNextPayIslandReview(actualOut.id)}
                        </c:if>
                         <c:if test="${actualOut.reviewType!=0}">
                         <font color="green">已通过</font>
                         </c:if>
                        </td>
                      <td class="tc">                         
                          <c:if test="${actualOut.payReview ==1 }">
                           <c:forEach items="${levelList}" var="levelList">
                           <c:if test="${levelList==actualOut.payNowLevel}">
                               <a href="javascript:void(0)" onclick="passCost(this,'${actualOut.id}','${actualOut.payNowLevel}')">通过</a>&nbsp;<a href="javascript:void(0)" onclick="denyCost(this,'${actualOut.id}','0')">拒绝</a>
                             </c:if>
                          </c:forEach>
                         </c:if>
                        </td>
                          <td>
                         <c:if test="${actualOut.payReview ==1 }">
                           <c:forEach items="${levelList}" var="levelList">
                           <c:if test="${levelList==actualOut.payNowLevel}">  
                          <input type="checkbox" value="${actualOut.id}"  name="actualId"/>
                             </c:if>
                          </c:forEach>
                         </c:if>
                        </td>                         
                       </tr>
                        </c:forEach>                        
                    </tbody>
                    <tfoot>
                    <c:if test="${! empty actualOutList}">
                    <tr>
                        <td>境外小计</td>
                        <td colspan="9">&nbsp;<span id="actualOutShow" name="actualOutShow"></td>
                    </tr></c:if>
                    </tfoot>
                </table>  
      
                  <div class="costSum  clearfix" style=" width: 98%; margin: 0px auto; padding:10px 0px 0px 0px;">
          <ul class="cost-ul" data-total="cost">
          <ul class="cost-ul" data-total="income">
                                  <li>实际总收入：&nbsp;￥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${fns:getSum(incomeList,'accountedMoney') }"/>
</li>                   
                    </ul>
                      <li>实际总成本：&nbsp;￥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${fns:getSum(actualCost,'cost')}"/>
</li>               
                     </ul>
                  <ul class="cost-ul" data-total="profit">
                  		<ul class="cost-ul" data-total="income">             
							<li>退款合计：￥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${actualrefund }"/>
							</li>
						</ul>
                      <li>实际总毛利：￥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${fns:getSum(incomeList,'accountedMoney')-fns:getSum(actualCost,'cost')-actualrefund }"/>
</li>                      
                     </ul> 
                        <ul style="margin: 0px;float: right;">
                    <input class="btn-primary" type="button" value="审核通过"  onclick="saveCheckBox('actualId')" />                        
                    </ul>  
               </div>   


         <div class="mod_information">
          <div class="mod_information_d">
            <div class="ydbz_tit">审核日志</div>
          </div>
        </div>    
        <div style="margin:0 auto; width:98%;">
                    <ul class="spdtai">
                        <c:forEach items="${costLog}" var="log" varStatus="status">
                     <li>   <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${log.createDate}"/>&#12288;【${log.costName}】<c:if test="${log.result==-1}"><span class="invoice_back">审核已撤销</span></c:if><c:if test="${log.result==0}"><span class="invoice_back">审核未通过</span></c:if><c:if test="${log.result==1}"><span class="invoice_yes">审核通过</span></c:if>&#12288;【${log.name}】&#12288; <c:if test="${!empty log.remark}"><font color="red">批注:</font>&nbsp;${log.remark} </c:if></li>
                         </c:forEach>
                     </ul>
                </div>

             <div class="release_next_add">
               <input class="btn btn-primary" type="button" value="关 闭"  onclick="javascript:window.opener=null;window.open('','_self');window.close();" /> 
            </div>
 
         <!--
         <div class="release_next_add">
            <c:if test="${reviewLevel > 0}">
                <input class="btn btn-primary" type="button" value="驳 回"  onclick="denyActivityCost('${activityGroup.id}')" />  <input class="btn btn-primary" type="button" value="审核通过"  onclick="passActivityCost(this,'${activityGroup.id}','0')"  id="shenhetongguo"/>
           </c:if>
            <c:if test="${reviewLevel <=0}">
                没有审核权限
          </c:if>
            </div>  -->

         <div id="agent" style="display:none">
         <select name="agentId" id="agentId" style="width:180px">
         <option value="" >===请选择===</option>
                <option value="-1" >非签约渠道</option>
                <c:forEach var="agentinfo" items="${agentinfoList }"> 
                <option value="${agentinfo.id }" <c:if
                test="${agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName }</option>
                </c:forEach>
            </select>          
            <div id="currency">
            <select style="width:75px; margin-right:5px;" id="currencyId" name="currencyId">
                    <c:forEach items="${curlist}" var="currency">
                        <option value="${currency.id}">
                               ${currency.currencyName} 
                        </option>
                    </c:forEach>
                </select><input type="text" name="price" id="price"  style="width:100px"/>
                </div>
                <select style="width:180px" onchange="selectchange(this.value)" id="first" name="first">
                    <c:forEach items="${supplytypelist}" var="dict">
                        <option value="${dict.value}">
                               ${dict.label} 
                        </option>
                    </c:forEach>
                </select> 
                  
       </div>
       
 
    <!--修改项目模板开始-->
            <div id="addItem" style="display:none;">
                <label>境内外项目选择：</label><select id="detailType0" name="detailType0">
                    <option value="0" selected="selected">境内明细</option>
                    <option value="1">境外明细</option>
                </select><br />
               <!-- <label>批发商类别选择：</label><select id="supplyClassify0" name="supplyClassify0">
                    <option value="1" selected="selected">签约渠道</option>
                    <option value="0">非签约渠道</option>
                </select><br />-->
                <label>批发商：</label> 批发商不能改 <!--<select id="supply0" name="supply0">
                <c:forEach items="${companyList}" var="company">
                <option value="${company.id}">${company.name}</option>
                </c:forEach>
                </select> --> <br />
                <label>项目名称：</label><input type="text" id="name0" name="name0" value="" /><br />
                <label>单价：</label><select style="width:75px; margin-right:20px;" id="currencyType0" name="currencyType0">
                    <c:forEach items="${curlist}" var="currency">
                        <option value="${currency.id}">
                               ${currency.currencyMark} 
                        </option>
                    </c:forEach>
                </select><input type="text" style="width:110px;" id="price0" name="price0" onblur="validNumFinally(this)" onafterpaste="validNum(this))" onkeyup="validNum(this)" /><br />
                <label>数量：</label><input type="text" class="inputTxt" style="width:60px;" id="account0" name="account0" onkeyup="this.value=this.value.replace(/\D/,'')" onafterpast="this.value=this.value.replace(/\D/,'')" /><br />
                <label>项目备注：</label><textarea rows="" cols="" id="comment0" name="comment0"></textarea>
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
    if(typeof objMoney[currencyName] == "undefined"){
      objMoney[currencyName] = parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(",",""),10);
    }else{
      objMoney[currencyName] += parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(",",""),10);
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
    costSums($('tr.budgetInCost'),$('#budgetInShow'));
    //实际成本录入-境外小计  
    costSums($('tr.budgetOutCost'),$("#budgetOutShow"));

    costSums($('tr.actualInCost'),$("#actualInShow"));
    
    costSums($('tr.actualOutCost'),$("#actualOutShow")); 

});
</script>
    
</body>
</html>
