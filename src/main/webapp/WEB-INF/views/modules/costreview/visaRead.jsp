<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>${head}</title>  
    <meta name="decorator" content="wholesaler"/>  
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactor.js" type="text/javascript"></script>
    <script src="${ctxStatic}/json/jquery.json.ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>    
    <script src="${ctxStatic}/jquery-other/jquery-combobox_prop.js" type="text/javascript"></script>
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
        });
        
        $("#contentTable").delegate(".chanpin","click",function(){
             $(this).addClass("on").siblings().removeClass('on');
             $('.tuanhao_cen').removeClass('onshow');
             $('.chanpin_cen').addClass('onshow');
            
        });        

        var ids = "";
        var names = "";
        <c:forEach var="data" items="${travelActivity.targetAreaList}" varStatus="d">
            ids = ids + "${data.id}"+",";
            names = names +"${data.name}"+",";
        </c:forEach>
        $("#targetAreaName").text(names.toString().substring(0,12)+'...');
        $("#targetAreaName").attr('title',names.toString().substring(0,names.length-1));
        eventHandler('operatorBudget', $('#contentTable'));
    });
    
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
            url: "${ctx}/cost/manager/getCosts/" + classType + "/" + groupId,
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
    
    var addCost = function addCost(text, classType, $List,budgetType) {           

           var _agentSelect = $("#agentId").clone();           
           var _currencyId = $("#currency").clone();
           var _first = $("#first").clone();        
           var groupId = '${visaProduct.id}';

           var _chengBenText = $(text).next().text();
           var $firstselect=$('<p></p>').append(_first);        
           var $select=$('<p></p>').append(_agentSelect);
           var $currencyId=$('<p></p>').append(_currencyId);          
           
           var html='<div id="moneyId" style=" margin:10px;" >'+  
           '<table border="0" width="96%">'+ 
           ' <tr><td width="200px" align="right">'+ 
           '境内外项目选择：</td><td>'+ 
           '<select name="overseas" id="overseas" style="width:180px">'+ 
           '<option value=""selected="selected">请选择</option>'+ 
           '<option value="0">境内明细</option>'+ 
           '<option value="1">境外明细</option></select>'+ 
           '</td></tr><tr><td align="right">客户类别：</td><td>'+ 
           '<input type="radio" name="supplytype" value="0" checked="checked" onClick="tosupply()" />批发商 '+ 
           '&nbsp;&nbsp;<input type="radio" name="supplytype" value="1"  onClick="tosales()"/>渠道商</td></tr>'+ 
           '<tr id="sales" style="display:none"><td align="right">渠道商选择：</td><td>'+ 
             $select.html() +
           '</td></tr><tr id="supplyid" ><td align="right">'+ 
           ' 地接社类型：</td><td>'+ 
            $firstselect.html() +
           '</td></tr>'+ 
           '<tr id="supply"><td align="right">'+ 
           '批发商选择：</td><td>'+ 
           '<span id="second"> '+ 
           '<select id="supplier" name="supplier"  style="width:180px" >'+ 
           '<option value="0" selected>请选择</option>  '+ 
           '</select> </span></td></tr>'+ 
           '<tr><td align="right">项目名称：</td><td>'+ 
           '<input type="text" name="itemname"  id="itemname"  style="width:180px" />'+ 
           ' </td></tr><tr><td align="right">单价：</td><td>'+ 
             $currencyId.html() +   
           ' </td></tr><tr><td align="right">数量：</td>'+ 
           '<td><input type="text" name="quantity"  id="quantity"  style="width:180px"/>'+
           '</td></tr> <tr><td align="right">项目备注：</td><td>'+
           '<textarea rows="3" cols="20" id="comment" name="comment"  style="width:180px"></textarea></td></tr>'+
           '</table></div> ';      
     
     var submit = function (v, h, f) { 
            
            var overseas =f.overseas;
            var supplytype = f.supplytype;
            var agentId =f.agentId;
            var first=f.first;
            var supplier=f.supplier;
            var supplyName;         

            var itemname= $.trim(f.itemname); 
            var currencyId =f.currencyId;
            var quantity = $.trim(f.quantity);
            var price = $.trim(f.price);

            var comment = f.comment;
            var commentLength = comment.length;

            //var reg = /^[\-\+]?\d+$/;
            var reg =/^[0-9]*(\.[0-9]{1,2})?$/; 
            if(v === 0){
                return true;
            }            
            if(overseas==""){
                top.$.jBox.tip('请选择境内外项目', 'error', { focusId: 'agentId' }); return false;
              }

             if(supplytype=="1"){                
                 if(agentId=="" ){
                top.$.jBox.tip('请选择渠道商', 'error', { focusId: 'agentId' }); return false;
              }else {
                supplyName=$(f.agentId).find("option:selected").text();               
                supplier=agentId;
              }
            }
            
            if(supplytype=="0" ){                          
                if(first=="0" ){
                top.$.jBox.tip('请选择地接社类型', 'error', { focusId: 'fisrt' }); return false;
                } else if (supplier=="0") {
                 top.$.jBox.tip('请选择地接社', 'error', { focusId: 'second' }); return false;
                } else {
                supplyName=$("#supplier").find("option:selected").text();               
                }
            }
          

            if(itemname==""){
                top.$.jBox.tip('项目名称不能为空', 'error', { focusId: 'itemname' }); return false;
            }

            if(currencyId=="0"){
                top.$.jBox.tip('请选择单价币种', 'error', { focusId: 'itemname' }); return false;
            }

            if(price==""){
                top.$.jBox.tip('请输入单价', 'error', { focusId: 'itemname' }); return false;
            }

            if(quantity==""){
                top.$.jBox.tip('请输入数量', 'error', { focusId: 'itemname' }); return false;
            }


            if(commentLength > 200){
                top.$.jBox.tip('项目备注不能大于200字','error' ,{ focusId: 'comment' }); return false;
            }       
            else {
                if(!reg.test(price)){
                    top.$.jBox.tip('项目金额最多有2位小数,不能有字母','error' ,{ focusId: 'price' });
                    return false;
                }else{
                    
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/cost/visa/save",
                        cache:false,
                        dataType:"json",
                        async:false,
                        data:{ 
                            overseas: overseas, supplytype :supplytype, agentId : agentId, supplyId :supplier,name : itemname, currencyId :currencyId, price : price,quantity : quantity, comment : comment, activityId : groupId,supplyName:supplyName,supplyType:supplytype, budgetType :budgetType},
                        success: function(array){
                            window.location.reload();                          
                        },
                        error : function(e){
                            top.$.jBox.tip('请求失败。'+e,'error' ,{ focusId: 'price' });
                            return false;
                        }
                     });
                }
            }
        };
        $.jBox(html, { title: '成本录入', buttons:{ '确定' : 1,'取消' : 0 }, submit: submit, height:440});
    };  
    
  
    var deleteCost = function(id, classType){
        $.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f) {
            if (v == 'ok') {
                $.ajax({
                    type: "POST",
                    url: "${ctx}/cost/manager/delete",
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
            url: "${ctx}/cost/manager/flow/start/save/" + groupId,
            data : {types : "operator,operatorBudget"},
            async:false,
            success: function(e){
                if(e == "true"){
                    location.href = '${ctx}/cost/manager/list/operatorPre';
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
            url: "${ctx}/cost/manager/flow/start/commit/" + groupId,
            data : {types : "operator,operatorBudget"},
            async:false,
            success: function(e){
                if(e == "true"){
                    location.href = '${ctx}/cost/manager/list/operatorPre';
                }else{
                    top.$.jBox.tip('请求失败。','error');
                    return false;
                }
            }
         });
    };





    var denyVisaCost = function denyVisaCost(id) {        

           var html='<div id="moneyId" style=" margin:10px;" >'+  
           '<table border="0" width="96%">'+          
           '<tr><td align="right">项目备注：</td><td>'+
           '<textarea rows="3" cols="20" id="remark" name="remark"  style="width:180px"></textarea></td></tr>'+
           '</table></div> ';      
     
     var submit = function (v, h, f) {            
                     
            var remark = f.remark;
            var remarkLength = remark.length;     
            if(remarkLength > 200){
                top.$.jBox.tip('项目备注不能大于200字','error' ,{ focusId: 'comment' }); return false;
            }       
            else {  
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/cost/review/denyvisacost",
                        cache:false,
                        dataType:"json",
                        async:false,
                        data:{ 
                            remark : remark,id: id},
                        success: function(array){
                            window.location.href = "${ctx}/cost/review/visaList/${reviewLevel}";
                            //window.location.reload();                          
                        },
                        error : function(e){
                            top.$.jBox.tip('请求失败。'+e,'error' ,{ focusId: 'price' });
                            return false;
                        }
                     });
                
            }
        };
        $.jBox(html, { title: '驳回备注', buttons:{ '确定' : 1,'取消' : 0 }, submit: submit, height:160});
    };  


//成本审核-通过
function passVisaCost(dom,id,review){

  $.ajax({
                     type: "POST",                      
                     url: "${ctx}/cost/review/passvisacost",                      
                     cache:false,
                     dataType:"json",
                     async:false,
                     data:{  format:"json",id : id },
                     success: function (data){ 
                      window.location.href = "${ctx}/cost/review/visaList/${reviewLevel}";
                     // window.location.reload();
                     },
                     error: function (){
                     alert('返回数据失败');
                       }
                }); 
}


//成本审核-拒绝某条成本
function denyCost(dom,id,review){

  $.ajax({
                     type: "POST",                      
                     url: "${ctx}/cost/review/deny",                      
                     cache:false,
                     dataType:"json",
                     async:false,
                     data:{  format:"json",id : id,review : review },
                     success: function (data){ window.location.reload();
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
  $.getJSON("${ctx}/cost/manager/supplylist/"+parentId,{"noCache":noCache},function(myJSON){ 
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
 function jumpto(){
    window.location.href="${ctx}/cost/review/visaList/${reviewLevel}"; 
  }
  
function expand(child,obj,id) {
        $.ajax({
           url:"${ctx}/cost/manager/payedRecord/",
           type:"POST",
           data:{id:id},
           success:function(data){
             var htmlstr=""
             var num = data.length;
             if(num>0){
                 var str1='';
                 for(var i =0;i<num;i++){
                   var str = data[i].payvoucher.split("|")
                   var idstr = data[i].ids.split("|")
                   var index = str.length;
                   if(index>0){
                      for(var a=0;a<index;a++){
                          str1+="<a class=\"downloadzfpz\" lang=\""+idstr[a]+"\">"+str[a]+"</a><br/>"
                      }
                   }
                   htmlstr+="<tr><td class='tc'>"+data[i].payTypeName+"</td><td class='tc'>"+data[i].currency_mark+parseFloat(data[i].amount).toFixed(2)+"</td><td class='tc'>"+data[i].createDate+"</td><td class='tc'>"+"其它收入"+
                    "</td><td class='tc'>";
                    if(data[i].isAsAccount == null) {
                    	htmlstr+="待收款";
                    }else if(data[i].isAsAccount == 1) {
                    	htmlstr+="已达账";
                    }else if(data[i].isAsAccount == 101) {
                    	htmlstr+="已撤销";
                    }else if(data[i].isAsAccount == 102) {
                    	htmlstr+="已驳回";
                    }
                    //if(data[i].isAsAccount == 1) {
                    	//htmlstr+="是";
                    //}else{
                    	//htmlstr+="否";
                    //}
					htmlstr+="</td><td class='tc'>"+str1+"</td>"+"</tr>";
                   str1='';
                 }
             }
             $("#rpi").html(htmlstr);
           }
        });
		if ($(child).is(":hidden")) {
			$(obj).html("收起收款记录");
			$(obj).parents("tr").addClass("tr-hover");
			$(child).show();
			$(obj).parents("td").addClass("td-extend");
		} else {
			if (!$(child).is(":hidden")) {
				$(obj).parents("tr").removeClass("tr-hover");
				$(child).hide();
				$(obj).parents("td").removeClass("td-extend");
				$(obj).html("收款记录");
			}
		}
	}

</script>
</head>
<body>
    <c:if test="${reviewLevel==1}">
		<c:choose>
     		<c:when test="${flag eq '1' }">
     			<page:applyDecorator name="new_cost_review_head">
			    	<page:param name="showType">visa</page:param>
			    </page:applyDecorator>
     		</c:when>
     		<c:otherwise>
     			<page:applyDecorator name="cost_review_head">
					<page:param name="current">visa</page:param>
				</page:applyDecorator>
     		</c:otherwise>
     	</c:choose>
    </c:if>
    <c:if test="${reviewLevel>=2}">                   
      <page:applyDecorator name="cost_review_manager">
      <page:param name="current">visa</page:param>
      </page:applyDecorator> 
    </c:if>

     <div class="mod_nav"><c:if test="${empty title}">财务 > 财务审核 > 成本审核 > 签证录入审核</c:if><c:if test="${!empty title}">${title}</c:if></div>

    <div class="produceDiv">
        <div class="mod_information">
            <div class="mod_information_d">
                <div class="ydbz_tit">产品基本信息</div>
            </div>
        </div>      
        <div class="mod_information_dzhan">
            <div class="mod_information_dzhan_d mod_details2_d">
                <span style="color: #3a7851; font-size: 16px; font-weight: bold;">${visaProduct.productName}</span>
                <div class="mod_information_d7"></div>
                 <table width="90%" border="0">
                        <tbody><tr>
                           <td class="mod_details2_d1">产品编号：</td>
                            <td class="mod_details2_d2">${visaProduct.productCode}  </td>
                            <td class="mod_details2_d1">签证国家：</td>
                            <td class="mod_details2_d2"><c:forEach items="${visaCountryList}" var="country">
                                <c:if test="${country.id eq visaProduct.sysCountryId}">
                                    ${country.countryName_cn}
                                </c:if>
                            </c:forEach></td>
                            <td class="mod_details2_d1">签证类型：</td>
                            <td class="mod_details2_d2">
                                ${visaType}
                                 </td>
                            <td class="mod_details2_d1">签证领区：</td>
                            <td class="mod_details2_d2">
                        <c:if test="${not empty visaProduct.collarZoning }">
                           ${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}
                        </c:if>
                               </td>
                            <td colspan="2"></td>
                        </tr>
                       
                        <tr>
                             <td class="mod_details2_d1">应收价格：</td>
                            <td class="mod_details2_d2">${currencyMark}&nbsp;<fmt:formatNumber pattern="#.00" value="${visaProduct.visaPay}" /></td>
                              <td class="mod_details2_d1">创建时间：</td>
                            <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaProduct.createDate}"/></td>
                            <td class="mod_details2_d1">团号：</td>
                            <td class="mod_details2_d2">${visaProduct.groupCode}  </td>
                            <td colspan="2"></td>
                        </tr>
                        </tbody></table>

                <div class="kong"></div>
            </div>
        </div>


        <div class="mod_information">
          <div class="mod_information_d">
            <div class="ydbz_tit">订单列表</div>
          </div>
        </div>
        <table style="border-top: 1px solid #dddddd" class="activitylist_bodyer_table mod_information_dzhan" id="contentTable">
          <thead style="background: #403738;">
            <tr>
                          <th width="4%">序号</th>
                            <th width="7%">预定渠道</th>
                            <th width="11%">订单号</th>
                            <th width="11%">产品编号</th>
                            <th width="11%">参团类型</th>
                            <th width="8%">销售</th>
                            <th width="8%">下单时间</th>
                            <th width="5%">人数</th>
                            <th width="8%">应收总额</th>
                            <th width="8%">已付金额<br/>到账金额</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${orderList}" var="visaOrder" varStatus="s">
            <tr>
              <td>${s.count}</td>
              <td>${visaOrder.agentName}</td>             
              <td>${visaOrder.orderNo}</td>
              <td class="tc">  ${visaProduct.productCode}</td>
              <td class="tc">
                <c:if test="${! empty visaOrder.activityKind}">
                  <c:forEach items="${ordertype}" var="ordertype">
                    <c:if test="${ordertype.value==1}">
                             ${ordertype.label}
                    </c:if>
                  </c:forEach>               
               </c:if>
               <c:if test="${empty visaOrder.activityKind}">单办签</c:if>
              </td>
              <td class="tc">${visaOrder.orderUserName}</td>
              <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaOrder.createDate }"/></td>
              <td class="tc">${visaOrder.travel_num}</td>                     
              <td class="tc"><span class="tdorange fbold">${visaOrder.totalMoney}</span></td>
              <td class="p0 tr">  
                <div class="yfje_dd"> 
                  <span class="fbold">${visaOrder.payedMoney}</span>
                </div>
                <div class="dzje_dd">
                  <span class="fbold">${visaOrder.accountedMoney}</span>
                </div>
              </td>            
            </tr>
            </c:forEach>
          </tbody>
        </table>    
        
		        <div class="costSum  clearfix"
			style=" width: 98%; margin: 0px auto; padding:10px 0px 0px 0px;">
			<ul class="cost-ul" data-total="cost">
				<ul class="cost-ul" data-total="income">
					<li>订单总收入：&nbsp;￥<fmt:formatNumber type="currency"
							pattern="#,##0.00"
							value="${fns:getSum(incomeList,'totalMoney') }" /></li>
				</ul>
				<li>订单总人数：&nbsp;${fns:getSum(orderList,'travel_num') }</li>
			</ul>
         </div>

        <div class="mod_information">
                    <div class="mod_information_d">
                        <div class="ydbz_tit">
                            	其它收入                           
                    </div>
                </div>
             <table class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan">
                    <thead>
                        <tr>
                            <th width="7%">境内/外项目</th>
                            <th width="10%">项目名称</th>
                            <th width="6%">数量</th>
                            <th width="10%">批发商/地接社</th>
                            <th width="7%">转换前<br>币种</th>
                            <th width="7%">转换前<br>单价</th>
                            <th width="7%">汇率</th>
                            <th width="7%">转换后<br>币种</th>
                            <th width="7%">转换后<br>总价</th>
                            <th width="9%">已收金额<br>达账金额</th>
                            <th width="7%">备注</th>                          
                            <th width="7%">录入人</th>
                            <th width="9%">操作</th>   
                        </tr>
                    </thead>
            
                        <c:forEach items="${otherCostList}" var="otherCost" varStatus="status">  
                        <tr class="otherCost">
                        <c:if test="${status.count==1}">
                         <td rowspan="${fn:length(otherCostList)}">收入明细</td>
                         </c:if>
                        <td  class="tc" name="tdName">${otherCost.name}</td>
                        <td  class="tc" name="tdAccount">${otherCost.quantity}</td>
                        <td class="tc" name="tdSupply">
                        ${otherCost.supplyName}
                         </td>
                        <td class="tc" width="7%" name="tdCurrencyName">
                        <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==otherCost.currencyId}">
                             ${currency.currencyName}
                          </c:if>
                        </c:forEach>
                        </td>
                        <td  class="tc" name="tdPrice">
                         <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==otherCost.currencyId}">
                             ${currency.currencyMark}
                          </c:if>
                        </c:forEach><fmt:formatNumber type="currency" pattern="#,##0.00" value="${otherCost.price }"/>
                      </td>
                          <td class="tc">${otherCost.rate}</td>
                       <td  class="tc" name="tdPrice">
                       <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==otherCost.currencyAfter}">
                             ${currency.currencyName}
                          </c:if>
                        </c:forEach>
                       </td>
                        <td class="tc">
                            <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==otherCost.currencyAfter}">
                             ${currency.currencyMark}
                          </c:if>
                        </c:forEach><fmt:formatNumber type="currency" pattern="#,##0.00" value="${otherCost.priceAfter }"/>                       
                        </td>
                        <td class="p0 tr">
                        	<div class="yfje_dd"> 
			                	<span class="fbold">
			                		<c:if test="${otherCost.payedMoney != '0.00' }">
										<c:forEach items="${curlist}" var="currency">
				                          <c:if test="${currency.id==otherCost.currencyId}">
				                             ${currency.currencyMark}
				                          </c:if>
				                        </c:forEach><fmt:formatNumber type="currency" pattern="#,##0.00" value="${otherCost.payedMoney }"/>
			                        </c:if>
			                    </span>
			                </div>
			                <div class="dzje_dd">
			                	<span class="fbold">
			                		<c:if test="${otherCost.confirmMoney != '0.00' }">
										<c:forEach items="${curlist}" var="currency">
				                          <c:if test="${currency.id==otherCost.currencyId}">
				                             ${currency.currencyMark}
				                          </c:if>
				                        </c:forEach><fmt:formatNumber type="currency" pattern="#,##0.00" value="${otherCost.confirmMoney }"/>
				                    </c:if>
				                </span>
			                </div>
						</td>
                        <td name="tdComment">${otherCost.comment}</td>                        
                        <td class="tc">                       
                             ${otherCost.createBy.name}                        
                       </td>
                        <td class="tc">
                          <a onclick="expand('#child1',this,${otherCost.id})" href="javascript:void(0)">收款记录</a>                  
                        </td>
                                                   
                       </tr>
                        </c:forEach>              
                    <c:if test="${! empty otherCostList}">
                    <tr>
                        <td>小计</td>
                        <td colspan="12">&nbsp;<span id="otherCostShow" name="otherCostShow"></span></td>
                    </tr></c:if>                 
             
                    </tfoot>
                </table>

			<div id="child1" class="activity_team_top1"
							style="display:none">
							<td colspan="15" class="team_top">
								<table id="teamTable" class="table activitylist_bodyer_table"
									style="margin:0 auto;">
									<thead>
										<tr>
											<th class="tc" width="10%">付款方式</th>
											<th class="tc" width="10%">金额</th>
											<th class="tc" width="7%">日期</th>
											<th class="tc" width="5%">支付款类型</th>
											<th class="tc" width="10%">状态确认</th>
											<th class="tc" width="8%">支付凭证</th>
										</tr> 
									</thead>
									<tbody id='rpi'>
		
									</tbody>
								</table></td>
						</div>  


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
                            <th width="17%">项目名称</th>
                            <th width="10%">数量</th>
                            <th width="15%">渠道商/地接社</th>
                            <th width="10%">币种</th>
                            <th width="10%">金额</th>
                            <th width="12%">备注</th>
                            <c:if test="${costAutoPass==0 }"><th width="8%">成本<br>审核状态</th></c:if>
                            <th width="8%">操作</th>
                        </tr>
                    </thead>              
                        <c:forEach items="${budgetInList}" var="budgetIn" varStatus="status">
                        <tr  class="budgetInCost">
                        <c:if test="${status.count==1}">
                         <td rowspan="${fn:length(budgetInList)}">境内付款明细</td>
                         </c:if>
                        <td  name="tdName">${budgetIn.name}</td>
                        <td  class="tr" name="tdAccount">${budgetIn.quantity}</td>
                        <td  name="tdSupply">
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
                        <td  name="tdComment">${budgetIn.comment}</td>
                        <c:if test="${costAutoPass==0 }">
	                        <td  class="tc" width="8%">
	                         <c:if test="${budgetIn.reviewType==0}">
	                        ${fns:getNextCostReview(budgetIn.id)}
	                        </c:if>
	                         <c:if test="${budgetIn.reviewType!=0}">
	                         ${budgetIn.reviewStatus}
	                         </c:if>
	                        </td>
                        </c:if>
                        <td class="tc">                          
                        </td>
                        </tr>
                        </c:forEach>
                  
                    <c:if test="${! empty budgetInList}">
                    <tr>
                        <td>境内小计</td>
                        <td colspan="8">&nbsp;<span id="budgetInShow" name="budgetInShow"></span></td>
                    </tr></c:if>
                    <c:forEach items="${budgetOutList}" var="budgetOut" varStatus="status">
                        <tr class="budgetOutCost">
                        <c:if test="${status.count==1}">
                         <td width="10%" rowspan="${fn:length(budgetOutList)}">境外付款明细</td>
                         </c:if>
                        <td width="17%" name="tdName">${budgetOut.name}</td>
                        <td width="10%" class="tr" name="tdAccount">${budgetOut.quantity}</td>
                        <td width="15%" name="tdSupply">
                       ${budgetOut.supplyName}
                         </td>
                        <td width="10%" name="tdCurrencyName">
                        <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==budgetOut.currencyId}">
                             ${currency.currencyName}
                          </c:if>
                        </c:forEach>
                        </td>
                        <td width="10%" class="tr" name="tdPrice"> <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==budgetOut.currencyId}">
                             ${currency.currencyMark}${budgetOut.price}
                          </c:if>
                        </c:forEach></td>
                        <td width="12%" name="tdComment">${budgetOut.comment}</td>
                        <c:if test="${costAutoPass==0 }">
	                        <td  class="tc" width="8%">
	                        <c:if test="${budgetOut.reviewType==0}">
	                        ${fns:getNextCostReview(budgetOut.id)}
	                        </c:if>
	                         <c:if test="${budgetOut.reviewType!=0}">
	                         ${budgetOut.reviewStatus}
	                         </c:if>
	                        </td>
                        </c:if>
                        <td class="tc">                         
                        </td>
                       </tr>
                        </c:forEach>                  
                    <c:if test="${! empty budgetOutList}">
                    <tr>
                        <td>境外小计</td>
                        <td colspan="8">&nbsp;<span id="budgetOutShow" name="budgetOutShow"></td>
                    </tr></c:if>
                    </tfoot>
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
                  		<ul class="cost-ul" data-total="income">             
							<li>预计退款合计：￥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${budgetrefund }"/>
							</li>
						</ul>
                      <li>预计总毛利：￥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${fns:getSum(incomeList,'totalMoney')-fns:getSum(budgetCost,'cost')-budgetrefund }"/>
</li>                      
                     </ul> 
        </div>    

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
                            <th width="13%">项目名称</th>
                            <th width="10%">数量</th>
                            <th width="12%">渠道商/地接社</th>
                            <th width="10%">币种</th>
                            <th width="10%">金额</th>
                            <th width="11%">备注</th>
                            <c:if test="${costAutoPass==0 }"><th width="7%">成本<br>审核状态</th></c:if>
                            <th width="7%">付款<br>审核状态</th>
                            <th width="5%">操作</th>
                        </tr>
                    </thead>               
                        <c:forEach items="${actualInList}" var="actualIn" varStatus="status">
                        <tr class="actualInCost">
                        <c:if test="${status.count==1}">
                         <td  rowspan="${fn:length(actualInList)}">境内付款明细</td>
                         </c:if>
                        <td  name="tdName">${actualIn.name}</td>
                        <td  class="tr" name="tdAccount">${actualIn.quantity}</td>
                        <td  name="tdSupply">
                          ${actualIn.supplyName}
                         </td>
                        <td  name="tdCurrencyName">
                        <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualIn.currencyId}">
                             ${currency.currencyName}
                          </c:if>
                        </c:forEach>
                        </td>
                        <td  class="tr" name="tdPrice"> <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualIn.currencyId}">
                             ${currency.currencyMark}${actualIn.price}
                          </c:if>
                        </c:forEach></td>
                        <td  name="tdComment">${actualIn.comment}</td>
                        <c:if test="${costAutoPass==0 }">
	                        <td  class="tc" width="8%">
	                        <c:if test="${actualIn.reviewType==0}">
	                        ${fns:getNextCostReview(actualIn.id)}
	                        </c:if>
	                         <c:if test="${actualIn.reviewType!=0}">
	                         <font color="green">${actualIn.reviewStatus}</font>
	                         </c:if>
	                        </td>
                        </c:if>
                        <td class="tc">
                         	<c:choose>
                         		<c:when test="${actualIn.reviewType==0}">
                         			${fns:getNextPayReview(actualIn.id)}
                         		</c:when>
                         		<c:when test="${actualIn.reviewType==2}">
			                        ${actualIn.reviewStatus}
                         		</c:when>
                         		<c:otherwise>
				                                                  不需审核
                         		</c:otherwise>
                         	</c:choose>
                        </td>
                        <td class="tc">                          
                        </td>
                        </tr>
                        </c:forEach>                 
                    <c:if test="${! empty actualInList}">
                    <tr>
                        <td>境内小计</td>
                        <td colspan="9">&nbsp;<span id="actualInShow" name="actualInShow"></td>
                    </tr></c:if>
                  
                    <c:forEach items="${actualOutList}" var="actualOut" varStatus="status">
                        <tr class="actualOutCost">
                        <c:if test="${status.count==1}">
                         <td  rowspan="${fn:length(actualOutList)}">境外付款明细</td>
                         </c:if>
                        <td name="tdName">${actualOut.name}</td>
                        <td class="tr" name="tdAccount">${actualOut.quantity}</td>
                        <td name="tdSupply">
                       ${actualOut.supplyName}
                         </td>
                        <td  name="tdCurrencyName">
                        <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualOut.currencyId}">
                             ${currency.currencyName}
                          </c:if>
                        </c:forEach>
                        </td>
                        <td  class="tr" name="tdPrice"> <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualOut.currencyId}">
                             ${currency.currencyMark}${actualOut.price}
                          </c:if>
                        </c:forEach></td>
                        <td  name="tdComment">${actualOut.comment}</td>
                        <c:if test="${costAutoPass==0 }">
	                        <td  class="tc">
	                            <c:if test="${actualOut.reviewType==0}">
	                           ${fns:getNextCostReview(actualOut.id)}
	                        </c:if>
	                         <c:if test="${actualOut.reviewType!=0}">
	                         <font color="green">${actualOut.reviewStatus}</font>
	                         </c:if>
	                        </td>
                        </c:if>
                        <td class="tc">
                         	<c:choose>
                         		<c:when test="${actualOut.reviewType==0}">
                         			${fns:getNextPayReview(actualOut.id)}
                         		</c:when>
                         		<c:when test="${actualOut.reviewType==2}">
			                        ${actualOut.reviewStatus}
                         		</c:when>
                         		<c:otherwise>
				                                                  不需审核
                         		</c:otherwise>
                         	</c:choose>
                        </td>
                        <td class="tc">                        
                        </td>
                        </tr>
                        </c:forEach>                    
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
                     </div>   
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

                <div class="release_next_add">                       
               <!-- <input class="btn btn-primary" type="button" value="返 回"  onclick="jumpto()" />  -->   
            </div>

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
	costSums($('tr.otherCost'),$('#otherCostShow'));
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
