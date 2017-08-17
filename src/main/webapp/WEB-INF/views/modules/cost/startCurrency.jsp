<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
  <title>类型字典维护</title>
  <link href="${ctxStatic}/bootstrap/2.3.1/css_default/bootstrap.min.css" type="text/css" rel="stylesheet" />
  <script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript" ></script>
  <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
  <link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
    <link href="${ctxStatic}/css/jh-style.css" type="text/css" rel="stylesheet" />
    <link href="${ctxStatic}/css/huanqiu-style.css" type="text/css" rel="stylesheet" />
    <style>
    .msg_div_tmcost1  p.incomePeopleIncome,.msg_div_tmcost1 p.shopPeopleP{padding-left:0px;margin-top:10px;margin-bottom:0;}
    .msg_div_tmcost1 input,.incomePeopleIncome input{width:120px;}
    .tdred{cursor:pointer;}
    </style>

  <script type="text/javascript">
    var addCostNum = ${costTotal};
    var delCostNum =  ${costTotal};    
    var currencyNum=${curTotal};

    var addIncomeNum = ${incomeTotal};
    var delIncomeNum =  ${incomeTotal};

    function add(o){  
      if(addCostNum>=currencyNum) { alert("币种选择完毕"); return false;}
        addCostNum++;
       $("#addCostNum").val(addCostNum);
        var num = addCostNum;
        var cloneDiv = $("p.shopPeopleNone").clone(true);
        cloneDiv.appendTo($(o).parent());         
        cloneDiv.show().removeClass('shopPeopleNone').addClass("shopPeopleP");
        
        if ( cloneDiv.html().indexOf("id=costid") > 0 ){
          var valueHtml = cloneDiv.html().replace("name=costid","name=\"costid"+num+"\"");
          valueHtml =valueHtml.replace("id=costid","id=\"costid"+num+"\"");
          valueHtml =valueHtml.replace("name=costsum","name=\"costsum"+num+"\"");
          valueHtml =valueHtml.replace("id=costsum","id=\"costsum"+num+"\"");
          cloneDiv.html(valueHtml);   
        }else{
          var valueHtml = cloneDiv.html().replace("name=\"costid\"","name=\"costid"+num+"\"");
          valueHtml =valueHtml.replace("id=\"costid\"","id=\"costid"+num+"\"");
          valueHtml =valueHtml.replace("name=\"costsum\"","name=\"costsum"+num+"\"");
          valueHtml =valueHtml.replace("id=\"costsum\"","id=\"costsum"+num+"\"");
          cloneDiv.html(valueHtml);
        }
    } 
    
    function deleteDiv(o){
      $(o).parent().remove();
      var i = delCostNum +1;      
      $('.shopPeopleP').each(function(index, element){
        $(element).find("select").attr("name","costid"+i);
        $(element).find("select").attr("id","costid"+i);
        $(element).find("input").attr("name","costsum"+i);      
        $(element).find("input").attr("id","costsum"+i);
        i++;
      });
       addCostNum--;
      $("#addCostNum").val(addCostNum);

    }


   
    function addIncome(o){    

      if(addIncomeNum>=currencyNum) { alert("币种选择完毕"); return false;}
        addIncomeNum++;
        $("#addIncomeNum").val(addIncomeNum);
        var num = addIncomeNum;
        var cloneDiv = $("p.incomePeopleNone").clone(true);
         cloneDiv.appendTo($(o).parent());         
         cloneDiv.show().removeClass('incomePeopleNone').addClass("incomePeopleIncome");

      
      if ( cloneDiv.html().indexOf("id=incomeid") > 0 ){
           var valueHtml = cloneDiv.html().replace("name=incomeid","name=\"incomeid"+num+"\"");
           valueHtml =valueHtml.replace("id=incomeid","id=\"incomeid"+num+"\"");
           valueHtml =valueHtml.replace("name=incomesum","name=\"incomesum"+num+"\"");
           valueHtml =valueHtml.replace("id=incomesum","id=\"incomesum"+num+"\"");
           cloneDiv.html(valueHtml);
      }else{
            var valueHtml = cloneDiv.html().replace("name=\"incomeid\"","name=\"incomeid"+num+"\"");
            valueHtml =valueHtml.replace("id=\"incomeid\"","id=\"incomeid"+num+"\"");
            valueHtml =valueHtml.replace("name=\"incomesum\"","name=\"incomesum"+num+"\"");
            valueHtml =valueHtml.replace("id=\"incomesum\"","id=\"incomesum"+num+"\"");
            cloneDiv.html(valueHtml);
      }

        
    } 
    
    function deleteIncome(o){
      $(o).parent().remove();
      var i = delIncomeNum+1;
      $('.incomePeopleIncome').each(function(index, element){
        $(element).find("select").attr("name","income"+i);
        $(element).find("input").attr("name","income"+i);
        i++;
      });
       addIncomeNum--;
      $("#addIncomeNum").val(addIncomeNum);

    }



    jQuery.extend(jQuery.validator.messages, {
          required: "必填信息",
          digits:"输入有误"
        }); 

     function formSubmit() {       
            var reg =/^[0-9]*(\.[0-9]{1,2})?$/;
            var x=$("#addCostNum").val();           

            for(i=1;i<=x;i++)
             {  if ($("#costid"+i).val()=="") { alert("请确认币种都已经选择"); return false; };               
                $('#costid'+i).removeAttr("disabled");
                if ($("#costsum"+i).val()=="") {  alert("请确认金额都已经输入");  return false; };             
                if (!reg.test($("#costsum"+i).val())){ alert("金额不能有字母");  return false;  }
              };

            x=$("#addIncomeNum").val();
            for(i=1;i<=x;i++)
             {  if ($("#incomeid"+i).val()=="") { alert("请确认币种都已经选择"); return false; };
                $("#incomeid"+i).removeAttr("disabled");
                if ($("#incomesum"+i).val()=="") {  alert("请确认金额都已经输入");  return false; };             
                if (!reg.test($("#incomesum"+i).val())){ alert("金额不能有字母");  return false;  }
              };
         $("#companyDictForm").submit();          
        }


  </script>


  </head>
  
  <body>
  <form:form id="companyDictForm" modelAttribute="sysdefinedict" action="${ctx}/cost/manager/saveCurrency" target="_parent" method="post">
    	<input type="hidden" id="activityId" name="activityId" value="${activityId}" />
    	<input type="hidden" id="groupId" name="groupId" value="${groupId}" />
   		 <input type="hidden" id="typeId" name="typeId" value="${typeId}" />
  
      <div>
        <input type="hidden"  type="text" id="curTotal" name="curTotal" value="${curTotal}" />
        <input type="hidden" type="text" id="addCostNum" name="addCostNum" value=" ${costTotal}" />   
        <input type="hidden"  type="text" id="addIncomeNum" name="addIncomeNum" value=" ${incomeTotal}" />        
      </div>
		<div class="msg_div_tmcost1">
			<div style="margin-left: 20px; margin-top: 20px;">
				<p>
					<div style="font-size: 15px; margin-bottom: 20px">总成本:</div>
				</p>

  <!--加一个隐藏不用的select 解决火狐下 select diable 不工作问题-->
  <select name="test" disabled="disabled"  style=" display:none;">
    <option value="abc">Black</option>
  </select>


				<c:forEach items="${costList}" var="currency" varStatus="s">
					<p class="shopPeopleK">
						<label>币种：</label> 
						<span> 
               <select disabled="disabled" name="costid${s.count}" id="costid${s.count}"  style="width: 75px; margin-right: 5px;">								
									<c:forEach items="${curlist}" var="curlist">
										<c:if test="${ curlist.id eq currency.currency_id}">
											<option value="${curlist.id}" selected>${curlist.currencyName}</option>
										</c:if>
										<c:if test="${ curlist.id != currency.currency_id}">
											<option value="${curlist.id}">${curlist.currencyName}</option>
										</c:if>
									</c:forEach>
							</select>
						</span> 
						<label>金额：</label> 
						<span>
							<input name="costsum${s.count}" style="height: 25px" id="costsum${s.count}" style="height:25px" value="${currency.amount}" type="text">
							 <c:if test="${s.count eq costTotal}">
								<a class="linkAir-spn" onclick="add(this)">+新增</a>
							</c:if> 
						</span>
					</p>
				</c:forEach>
  				<c:if test="${costTotal==0}"> 
  					<p>  
 						<a class="linkAir-spn" onclick="add(this)">+新增</a>  
 			     </p> 
 		         </c:if> 
 		         </div>
			<p style="display: none" class="shopPeopleNone">
				<label>币种：</label>
				<span> 
					<select name="costid" id="costid" 	  style="width: 75px; margin-right: 5px;">							
							<c:forEach items="${curlist}" var="currency">
								<option value="${currency.id}">${currency.currencyName}
								</option>
							</c:forEach>
					</select>
				</span> 
				<label>金额：</label>
				 <span>
				 	<input name="costsum" id="costsum" type="text" style="height: 25px">
				 </span> 
				 <span class="tdred" onclick="deleteDiv(this)">删除</span>
			</p>
		</div>

		<div class="msg_div_tmcost1">

			<div style="margin-left: 20px; margin-top: 20px;">
				<p>
				<div style="font-size: 15px; margin-bottom: 20px">总收入:</div>
				</p>
				<c:forEach items="${incomeList}" var="currency" varStatus="s">
					<p class="shopPeopleMyIncome tt">
						<label>币种：</label> <span> <select disabled="disabled" name="incomeid${s.count}" 
							id="incomeid${s.count}" style="width: 75px; margin-right: 5px;">
								<option value="">请选择</option>
								<c:forEach items="${curlist}" var="curlist">
									<c:if test="${ curlist.id eq currency.currency_id}">
										<option value="${curlist.id}" selected>
											${curlist.currencyName}</option>
									</c:if>
									<c:if test="${ curlist.id != currency.currency_id}">
										<option value="${curlist.id}">
											${curlist.currencyName}</option>
									</c:if>
								</c:forEach>
						</select>

						</span> <label>金额：</label> <span><input name="incomesum${s.count}"
							id="incomesum${s.count}" style="height: 25px"
							value="${currency.amount}" type="text"> <c:if
								test="${s.count eq incomeTotal}">
								<a class="linkAir-spn" onclick="addIncome(this)">+新增</a>
							</c:if> </span>
					</p>
				</c:forEach>
				<c:if test="${incomeTotal==0}">
					<p>
						<a class="linkAir-spn" onclick="addIncome(this)">+新增</a>
			
			</p>

			</c:if>
			</div>

			<p style="display: none" class="incomePeopleNone">
				<label>币种：</label> 
				<span> 
					<select name="incomeid" id="incomeid" style="width: 75px; margin-right: 5px;">
							<option value="">请选择</option>
							<c:forEach items="${curlist}" var="currency">
								<option value="${currency.id}">${currency.currencyName}
								</option>
							</c:forEach>
					</select>
				</span> 
				<label>金额：</label>
				<span>
					<input name="incomesum" id="incomesum" type="text" style="height: 25px">
				</span>
				<span class="tdred" onclick="deleteIncome(this)">删除</span>
			</p>
		</div>
		</div>
		</div>

		<div class="release_next_add">
			<input class="btn btn-primary gray" type="button" value="取消"
				onClick="window.parent.window.jBox.close()" /> <input type="button"
				class="btn btn-primary" onClick="formSubmit()" value="确定" />
		</div>
		</div>

	</form:form>
</body>
</html>
