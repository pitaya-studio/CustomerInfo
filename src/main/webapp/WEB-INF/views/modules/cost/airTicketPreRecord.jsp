<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>运控-成本录入-机票成本录入</title>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>

<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link rel="stylesheet" href="css/jbox.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript">
   $(function(){
       $('.zksxs').click(function(){         
        if($('.ydxbds').is(":hidden")==true)
        {
          $('.ydxbds').show();
          $(this).text('收起筛选');
          //$(this).addClass('zksx-on');
        }else
        {
          $('.ydxbds').hide();
          $(this).text('展开筛选');
         // $(this).removeClass('zksx-on');
        }
      });

    });
    
    $(document).delegate(".downloadzfpz","click",function(){
        window.open ("${ctx}/sys/docinfo/download/"+$(this).attr("lang"));
    });
    

  var addCost = function addCost(text, classType, $List,budgetType) {           

           var _agentSelect = $("#agentId").clone();           
           var _currencyId = $("#currency").clone();
           var _first = $("#first").clone();        
           var groupId = '${activityAirTicket.id}';

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
           ' 供用商类型：</td><td>'+ 
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

           selectchange('1'); //获得默认批发商列表      
     
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
                top.$.jBox.tip('请选择供用商类型', 'error', { focusId: 'fisrt' }); return false;
                } else if (supplier=="0") {
                 top.$.jBox.tip('请选择供用商', 'error', { focusId: 'second' }); return false;
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


            if(commentLength > 999){
                top.$.jBox.tip('项目备注不能大于1000 字符','error' ,{ focusId: 'comment' }); return false;
            }       
            else {
                if(!reg.test(price)){
                    top.$.jBox.tip('项目金额最多有2位小数,不能有字母','error' ,{ focusId: 'price' });
                    return false;
                }else{
                    
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/cost/manager/saveticket",
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
        $.jBox(html, { title: '成本录入', buttons:{'取消' : 0, '确定' : 1 }, submit: submit, height:440});
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
                        type : classType,groupId : "", orderType : "",visaId : ""},
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


    var cancelCost = function(id, classType){
        $.jBox.confirm("确定要撤回成本审核吗？", "提示", function(v, h, f) {
            if (v == 'ok') {
                $.ajax({
                    type: "POST",
                    url: "${ctx}/cost/manager/cancel",
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


function trimStr(str){return str.replace(/(^\s*)|(\s*$)/g,"");}

//运控-成本录入-修改项目
function modifyCost(dom,id,budgetType){  
    var itemId=$(dom).parents("tr").find("#itemId").val();
    //清除正在编辑的状态
    $("tr[ismodify='1']").each(function(index, element) {
        $(element).removeAttr("ismodify");
    });
    //设置正在编辑项目tr
    var $thisTr = $(dom).parents("tr");
    $thisTr.attr("ismodify","1");
    //弹出框中加载数据
    var thisDetailType = $thisTr.attr("value");/*境内、境外*/
    var thisSupply = $thisTr.find("td[name='tdSupply']").text();/*批发商*/
    var thisName = $thisTr.find("td[name='tdName']").text();/*项目名称*/
    //var thisCurrencyType = $thisTr.find("td[name='tdCurrencyName']").text();/*币种*/
    var thisPrice = $thisTr.find("td[name='tdPrice']").text();/*单价*/
    var thisAccount = $thisTr.find("td[name='tdAccount']").text();/*数量*/
    var thisComment = $thisTr.find("td[name='tdComment']").text();/*备注*/
    //绑定数据
    var $templateClone = $("#addItem").clone(false);
    //重置表单元素的id和name值
    $templateClone.find("[id]").each(function(index, element) {
        $(element).attr("id",$(element).attr("id").replace("0","")).attr("name",$(element).attr("name").replace("0",""));
    });
    //去除select的选项
    $templateClone.find("option:selected").removeAttr("selected");
    //境内、境外
    $templateClone.find("#detailType option[value='" + thisDetailType + "']").attr("selected","selected");
    //批发商
    $templateClone.find("#supply option").each(function(index, element) {
        if($(element).text() == thisSupply){
            $(element).attr("selected","selected");
            return;
        }
    });
    //项目名称
   $templateClone.find("#name").attr("value",thisName);
   $templateClone.find("#supply").attr("value",thisSupply);
   var border;
     thisPrice=thisPrice.replace(/(^\s*)|(\s*$)/g, "");
        //找到金额中第一个数字位置
        for(var i=0;i<thisPrice.length;i++){
           if(thisPrice.substring(i,i+1).match(/^[0-9].*$/)){
           border=i;          
           break;
          }
        }

     var curId=trimStr(thisPrice.substring(0,border));
          //币种
    $templateClone.find("#currencyType option").each(function(index, element) {
        if(trimStr($(element).text())== curId){   
            $(element).attr("selected","selected");
            return;
        }
    });
    
    //单价    
    $templateClone.find("#price").attr("value",thisPrice.substring(border));
    //数量
    $templateClone.find("#account").attr("value",thisAccount);
    //备注
    $templateClone.find("#comment").text(thisComment);
   
    $.jBox('<div class="costBox">'+$templateClone.html()+'</div>', { title: "成本修改",buttons:{'取消': false,'提交': true}, submit:function(v, h, f){
        if (v==true){
            var $containerTr = $("tr[ismodify='1']");
            var name = $.trim(f.name);
            var price = $.trim(f.price);
            var account = $.trim(f.account);
            var comment = f.comment;
            var currencyId=f.currencyType;
            var company=f.supply;
            var overseas = f.detailType;            
          
            if(name==""){
                top.$.jBox.tip('项目名称不能为空', 'error', { focusId: 'name' }); return false;
            }else if(price==""){
                top.$.jBox.tip('单价不能为空', 'error', { focusId: 'price' }); return false;
            }else if(comment.length > 1000){
                top.$.jBox.tip('项目备注不能大于1000字','error' ,{ focusId: 'comment' }); return false;
            }else if(account==""){
                top.$.jBox.tip('数量不能为空', 'error', { focusId: 'account' }); return false;
            }else {             
                
                $.ajax({
                     type: "POST",                      
                     url: "${ctx}/cost/manager/modify",                      
                     cache:false,
                     dataType:"json",
                     async:false,
                     data:{  format:"json",id : id, name : name, quantity: account,price : price,currencyId : currencyId, comment : comment,overseas : overseas,budgetType : budgetType },
                     success: function (data){  window.location.reload();
                     },
                     error: function (){
                     alert('返回数据失败');
                     }
                }); 
               
                return true;
            }
        }
    },height:400,width:450});
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
    options="<option value=''>==没有记录==</option>"; 
    $("#supplier").html(options); 
    $("#second").show(); 
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

//提交成本
function submitform(id){
  $.ajax({
                     type: "POST",                      
                     url: "${ctx}/cost/manager/submitair",                      
                     cache:false,
                     dataType:"json",
                     async:false,
                     data:{  format:"json",id : id },
                     success: function (data){ 
                        alert('提交成功'); 
                        window.location.href="${ctx}/cost/manager/airTicketList";
                     },
                     error: function (){
                     alert('返回数据失败');
                       }
                }); 
}

function addCurrency(activityId,groupId,typeId) {      

      var iframe = "iframe:${ctx}/cost/manager/startCurrency?activityId="+activityId+"&groupId="+groupId+"&typeId="+typeId;
     
         $.jBox(iframe, {
            title: "录入总成本/总收入",
            width: 440,
            height: 480,
            buttons: {}
      });
  } 

    function addCostHQX(budgetType) {
      var groupId = '${activityAirTicket.id}';
      var iframe = "iframe:${ctx}/cost/manager/addCostHQX/"+ '${activityAirTicket.id}' +"/"+ '${activityAirTicket.id}' +"/"+ budgetType  +"/7/"+ '${deptId}';     

      $.jBox(iframe, {
            title: "成本录入",
            width: 380,
            height: 640,
			persistent:true,
            buttons: {}
      });
  } 
  
  function addOtherCostHQX(budgetType) {
      var groupId = '${activityAirTicket.id}';
      var iframe = "iframe:${ctx}/cost/manager/addCostHQX/"+ '${activityAirTicket.id}' +"/"+ '${activityAirTicket.id}' +"/"+ budgetType  +"/7" +"/"+ '${deptId}';     

      $.jBox(iframe, {
            title: "其它收入录入",
            width: 380,
            height: 655,
            persistent:true,
            buttons: {}
      });
  }

  function updateCostHQX(costid) {
      var groupId = '${activityAirTicket.id}';
      var iframe = "iframe:${ctx}/cost/manager/updateCostHQX/"+ '${activityAirTicket.id}' +"/"+ '${activityAirTicket.id}' +"/"+ costid  +"/7/"+ '${deptId}';     

      $.jBox(iframe, {
            title: "成本修改",
            width: 380,
            height: 620,
			persistent:true,
            buttons: {}
      });
  } 
  
  function updateOtherCostHQX(costid) {
      var groupId = '${activityAirTicket.id}';
      var iframe = "iframe:${ctx}/cost/manager/updateCostHQX/"+ '${activityAirTicket.id}' +"/"+ '${activityAirTicket.id}' +"/"+ costid  +"/7" +"/"+ '${deptId}';     

      $.jBox(iframe, {
            title: "其它收入修改",
            width: 380,
            height: 620,
			persistent:true,
            buttons: {}
      });
  }

    function saveCheckBox(id,budgetType){   
    var tmp=0;
    $("input[name='"+id+"']").each(function(){ 
    if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
           tmp=tmp +","+$(this).attr('value')
       }
    });    
    if(tmp=="0"){
        if (budgetType=='0')  alert("请选择预算成本");
        else   alert("请选择实际成本");
        return;
    }
    $.ajax({
                        type: "POST",
                        url: "${ctx}/cost/manager/saveCostList",
                        cache:false,
                        dataType:"json",
                        async:false,
                        data:{ 
                            costList:tmp,visaIds:"",groupId:"",orderType:""  },
                        success: function(array){ 
                            window.location.reload();       

                        },
                        error : function(e){
                            alert('请求失败。');                         
                            return false;
                        }
                     });
 }  

    function payCheckBox(id,budgetType){   
    	var tmp= '';
        $("input[name='"+id+"']").each(function(){
    	    if ($(this).is(":checked")){
    	    	if(!tmp){
    	    		tmp = $(this).val();
    	    	}else{
    	    		tmp = tmp + "&" + $(this).val();
    	    	}
    		}
        });
        $.ajax({
    		type: "POST",
    		url: "${ctx}/review/airticket/payment/apply",
    		cache:false,
    		dataType:"json",
    		async:false,
    		data:{items:tmp},
    		success: function(data){
    			if(data.flag){
    				$.jBox.tip('申请付款成功', 'success');
    				window.location.reload();
    			}else{
    				$.jBox.tip('付款申请失败，' + data.msg, 'error');
    				return false;
    			}
    		},
    		error : function(e){
    			$.jBox.tip('申请付款失败', 'error');
    		    return false;
    		}
    	});
    }

    var cancelPayCost = function(reviewId){
    	$.jBox.confirm("确定要取消付款审批吗？", "提示", function(v, h, f) {
    		if (v == 'ok') {
    			$.ajax({
    				type: "POST",
    				url: "${ctx}/review/payment/web/cancelReview",
    				cache:false,
    				async:false,
    				data:{reviewId:reviewId},
    				success: function(data){
    					if(data.flag){
    						$.jBox.tip('取消成功', 'success');
    						window.location.reload();
    					}else{
    						$.jBox.tip('取消失败，' + data.msg, 'error');
    						return false;
    					}
    				},
    				error : function(e){
    					top.$.jBox.tip('请求失败。','error');
    					return false;
    				}
    			});
    		}
    	});
    }
    
function milliFormat(s){//添加千位符
	if(/[^0-9\.\-]/.test(s)) return "invalid value";
	s=s.replace(/^(\d*)$/,"$1.");
	s=(s+"00").replace(/(\d*\.\d\d)\d*/,"$1");
	s=s.replace(".",",");
	var re=/(\d)(\d{3},)/;
	while(re.test(s)){
		s=s.replace(re,"$1,$2");
	}
	s=s.replace(/,(\d\d)$/,".$1");
	return s.replace(/^\./,"0.")
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
                   htmlstr+="<tr><td class='tc'>"+data[i].payTypeName+"</td><td class='tc'>"+data[i].currency_mark+milliFormat(parseFloat(data[i].amount).toFixed(2))+"</td><td class='tc'>"+data[i].createDate+"</td><td class='tc'>"+"其它收入"+
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
	
	var deletePassCost = function(id, classType, groupId, visaId){
		$.jBox.confirm("确定要删除成本吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				$.ajax({
					type: "POST",
					url: "${ctx}/cost/manager/delete",
					cache:false,
					async:false,
					data:{id : id, type : classType, groupId : groupId, orderType : '${typeId}',visaId : visaId, deptId : '${deptId}'},
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
</script>
<style type="text/css">
	.word-wrap{
		word-wrap:break-word;
	}
</style>
</head>
<body>
      <page:applyDecorator name="cost_input_head">
      <page:param name="current">airticket</page:param>
      </page:applyDecorator>
<input type="hidden" name="airTicketId" id="airTicketId" value="${airticket.id}"/>
<input type="hidden" name="budgetType" id="budgetType" value="1"/>
		<!--右侧内容部分开始-->
			<div class="mod_nav">运控 > 产品成本录入 > 机票录入详情页</div>
			
			<div class="produceDiv"> 
				 <div class="orderdetails_tit"><span>1</span>机票产品信息</div>
     
      <div class="mod_information_dzhan_d mod_details2_d" style="overflow:hidden">

        <p class="ydbz_mc">产品编号: ${activityAirTicket.productCode}, 
            ${activityAirTicket.departureCityLabel() }    
           &mdash;
            <c:forEach items="${areas}" var="tn">
						    <c:if test="${tn.id == activityAirTicket.arrivedCity}">
						      ${tn.name}
						    </c:if>

						</c:forEach>：
            <c:if test="${ activityAirTicket.airType ==1}">多段</c:if>
            <c:if test="${ activityAirTicket.airType ==2}">往返</c:if>
            <c:if test="${ activityAirTicket.airType ==3}">单程</c:if>
               </p>
       
<c:if test="${ activityAirTicket.airType ==1}">
       <c:forEach items="${activityAirTicket.flightInfos}" var="flightInfos" varStatus="s">
            <div class="title_samil">第${flightInfos.number}段：<c:if test="${flightInfo.ticket_area_type eq '3'}">内陆</c:if>
                            <c:if test="${flightInfo.ticket_area_type eq '2'}">国际</c:if>
                            <c:if test="${flightInfo.ticket_area_type eq '1'}">内陆+国际</c:if></div>
            <table border="0" width="90%">
                                <tbody><tr>
                                    <td class="mod_details2_d1">出发机场：</td>
                                    <td class="mod_details2_d2">
                            <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq flightInfos.leaveAirport}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                                      </td>
                                    <td class="mod_details2_d1">到达机场：</td>
                                    <td class="mod_details2_d2">
                            <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq flightInfos.destinationAirpost}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                               </td>
                                 </tr>
                                 <tr>
                                    <td class="mod_details2_d1">出发时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${flightInfos.startTime }"/></td>
                                    <td class="mod_details2_d1">到达时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${flightInfos.arrivalTime }"/></td>
                                    <td class="mod_details2_d1">航班号：</td>
                                    <td class="mod_details2_d2">${flightInfos.flightNumber }</td>               
                                   </tr>
                                    <tr>
                                    <td class="mod_details2_d1">航空公司：</td>
                                    <td class="mod_details2_d2">${flightInfos.airlinesLabel() }</td>
                                    <td class="mod_details2_d1">舱位等级：</td>
                                    <td class="mod_details2_d2">${flightInfos.spaceGradeLabel() }</td>		
                                    <td class="mod_details2_d1">舱位：</td>
                                    <td class="mod_details2_d2">${flightInfos.airspaceLabel() }</td>
                                 </tr>
            </table>
       </c:forEach>
    </c:if>


<c:if test="${ activityAirTicket.airType ==2}">
       <div class="title_samil">去程：</div>
              <table border="0" width="90%">
                                <tbody><tr>
                                       <td class="mod_details2_d1">出发机场：</td>
                                    <td class="mod_details2_d2">
                            <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq activityAirTicket.flightInfos[0].leaveAirport}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                                      </td>
                                    <td class="mod_details2_d1">到达机场：</td>
                                    <td class="mod_details2_d2">
                            <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq activityAirTicket.flightInfos[0].destinationAirpost}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                               </td>
                           </tr>
                                 <tr>
                                    <td class="mod_details2_d1">出发时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[0].startTime }"/></td>
                                    <td class="mod_details2_d1">到达时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[0].arrivalTime }"/></td>
                                    <td class="mod_details2_d1">航班号：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].flightNumber }</td>               
                                   </tr>
                                    <tr>
                                    <td class="mod_details2_d1">航空公司：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].airlinesLabel() }</td>
                                    <td class="mod_details2_d1">舱位等级：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].spaceGradeLabel() }</td>		
                                    <td class="mod_details2_d1">舱位：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].airspaceLabel() }</td>
                                 </tr>
                           </table>
                                 <div class="title_samil">返程：</div>
                                  <table border="0" width="90%">
                                 <tbody><tr>
                                    <td class="mod_details2_d1">出发机场：</td>
                                    <td class="mod_details2_d2">
                             <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq activityAirTicket.flightInfos[1].leaveAirport}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                              </td>
                             <td class="mod_details2_d1">到达机场：</td>
                             <td class="mod_details2_d2">
                             <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq activityAirTicket.flightInfos[1].leaveAirport}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                                     </td>
                                 </tr>
                                 <tr>
                                    <td class="mod_details2_d1">出发时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[1].startTime }"/></td>
                                    <td class="mod_details2_d1">到达时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[1].arrivalTime }"/></td>
                                    <td class="mod_details2_d1">航班号：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[1].flightNumber }</td>               
                                   </tr>
                                    <tr>
                                    <td class="mod_details2_d1">航空公司：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[1].airlinesLabel() }</td>
                                    <td class="mod_details2_d1">舱位等级：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[1].spaceGradeLabel() }</td>		
                                    <td class="mod_details2_d1">舱位：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[1].airspaceLabel() }</td>
                                 </tr>
                    </table>
  </c:if>


<c:if test="${ activityAirTicket.airType ==3}">
       <table border="0" width="90%">
                                <tbody><tr>
                                    <td class="mod_details2_d1">出发机场：</td>
                                    <td class="mod_details2_d2">
                                <c:forEach items="${airportlist}" var="airportlist">
                                <c:if test="${airportlist.id eq activityAirTicket.flightInfos[0].leaveAirport}">
                                ${airportlist.airportName}
                                </c:if>
                                </c:forEach>
                                   </td>
                                    <td class="mod_details2_d1">到达机场：</td>
                                    <td class="mod_details2_d2">
                                   <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq activityAirTicket.flightInfos[0].destinationAirpost}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                                 </td>                                    
                                 </tr>
                                 <tr>
                                    <td class="mod_details2_d1">出发时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[0].startTime }"/></td>
                                    <td class="mod_details2_d1">到达时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[0].arrivalTime }"/></td>
                                    <td class="mod_details2_d1">航班号：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].flightNumber }</td>               
                                   </tr>
                                    <tr>
                                    <td class="mod_details2_d1">航空公司：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].airlinesLabel() }</td>
                                    <td class="mod_details2_d1">舱位等级：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].spaceGradeLabel() }</td>		
                                    <td class="mod_details2_d1">舱位：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].airspaceLabel() }</td>
                                 </tr>
                    </table>
  </c:if>
  </div>



				
				<div class="mod_information">
					<div class="mod_information_d">
						<div class="ydbz_tit">订单列表  &nbsp; <a class="zksxs">收起筛选</a></div>

					</div>
				</div>
        <div class="ydxbds">
				<table class="activitylist_bodyer_table mod_information_dzhan" id="contentTable">
					<thead>
						<tr>
				                <th width="8%">预定渠道</th>
				                <th width="8%">订单号</th>
				                <th width="7%">销售</th>
				                <th width="13%">预订/剩余时间</th>
				                <th width="9%">出/截团日期</th>
				                <th width="7%">机票类型</th>
				                <th width="4%">人数</th>
				                <th width="8%">订单状态</th>
				                <th width="7%">订单总额</th>
				                <th width="8%">已付金额<br/>到账金额</th>
				                <th width="8%">备注</th>
				                <th width="5%">操作</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${airTicketOrderList}" var="airTicketOrder">
						<tr >
							<td>
								<c:choose>
									<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and airTicketOrder.agentName eq '非签约渠道' }">未签</c:when>
									<c:otherwise>${airTicketOrder.agentName}</c:otherwise>
								</c:choose>
							</td>
							<td>${airTicketOrder.orderNo}</td>
							<td class="tc">${airTicketOrder.orderUserName}</td>
							<td class="p0">
								<div class="out-date"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${airTicketOrder.createDate }"/></div>
								<div class="close-date">${airTicketOrder.leftDays }</div>
							</td>
							<td style="padding: 0px;">
								<div class="out-date">${airTicketOrder.groupOpenDate}</div>
								<div class="close-date">${airTicketOrder.groupCloseDate}</div>
							</td>
							<td>
							<c:if test="${airTicketOrder.airType=='3'}">单程</c:if>
							<c:if test="${airTicketOrder.airType=='1'}">多段</c:if>
							<c:if test="${airTicketOrder.airType=='2'}">往返</c:if>
							</td>
							<td class="tr">${airTicketOrder.personNum}</td>
							<td>${fns:getDictLabel(airTicketOrder.order_state, "order_pay_status", "")}</td>
							<td class="tr"><span class="tdorange fbold">${airTicketOrder.totalMoney} </span></td>
							<td class="p0 tr">	
								<div class="yfje_dd">	
									<span class="fbold">${airTicketOrder.payedMoney}</span>
								</div>
								<div class="dzje_dd">
									<span class="fbold">${airTicketOrder.accountedMoney}</span>
								</div>
							</td>
							<td class="word-wrap">${airTicketOrder.comments}</td>
							<td class="tc"><a target="_blank" href="${ctx}/order/manage/airticketOrderDetail?orderId=${airTicketOrder.orderId}" onclick="">详情</a></td>
						</tr>
						</c:forEach>
					</tbody>
				</table>				
	  </div>
	  <div class="costSum  clearfix"
			style=" width: 98%; margin: 0px auto; padding:10px 0px 0px 0px;">
			<ul class="cost-ul" data-total="cost">
				<ul class="cost-ul" data-total="income">
					<li>订单总收入：&nbsp;￥<fmt:formatNumber type="currency"
							pattern="#,##0.00"
							value="${fns:getSum(incomeList,'totalMoney') }" /></li>
				</ul>
				<li>订单总人数：&nbsp;${fns:getSum(airTicketOrderList,'personNum') }</li>
			</ul>

		</div>


     <div class="mod_information">
                    <div class="mod_information_d">
                        <div class="ydbz_tit">
                            其它收入录入<c:if test="${activityAirTicket.lockStatus==1}">(已经锁定)</c:if>
                            <div class="button_addcb wpr20 xtjxm">
                                 <c:if test="${empty activityAirTicket.lockStatus || activityAirTicket.lockStatus==0}">
                                  <c:if test="${companyId!=71 || companyId==71 && isOperator==1}">
                                    <a class="button_addcb_a" onclick="addOtherCostHQX('2')">添加项目</a>
                                  </c:if>
                                 </c:if>
                        </div>
                    </div>
                </div>

             <table class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan">
                    <thead>
                        <tr>
                            <th width="7%">境内/外项目</th>
                            <th width="10%">项目名称</th>
                            <th width="10%">地接社/渠道商</th>
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
                        <td class="tc" name="tdSupply">
                        	<c:choose>
                        		<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and otherCost.supplyName eq '非签约渠道' }">未签</c:when>
                        		<c:otherwise>${otherCost.supplyName}</c:otherwise>
                        	</c:choose>
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
                        <td name="tdComment" class="td-remark">${fn:escapeXml(otherCost.comment) }</td>                        
                        <td class="tc">                       
                             ${otherCost.createBy.name}                        
                       </td>
                        <td class="tc">
                         <c:if test="${activityAirTicket.lockStatus==0 }">
                         <c:if test="${companyId!=71 || companyId==71 && isOpt!=1 }">                        
                          <a href="javascript:void(0)" onclick="updateOtherCostHQX('${otherCost.id}')">修改</a>&nbsp;&nbsp;<a href="javascript:void(0)"  onclick="return deleteCost('${otherCost.id}','operator')">删除</a>                                               
                         </c:if>                        
                         </c:if>
                          <c:if test="${activityAirTicket.lockStatus==1}">
                          已经锁定
                          </c:if> 
                          <br><a href="${ctx}/cost/manager/paymentConfirm/${otherCost.id}/${otherCost.orderType}?payType=3&groupId=${otherCost.activityId }&orderType=${otherCost.orderType}" target="_blank">收款</a>&nbsp;&nbsp;
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
							
								<table id="teamTable" class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan"
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
								</table>
						</div>

    </div>

      <div class="mod_information">
                    <div class="mod_information_d">
                        <div class="ydbz_tit">
                            预算成本录入<c:if test="${activityAirTicket.lockStatus==1}">(已经锁定)</c:if>
                            <div class="button_addcb wpr20 xtjxm">
                                 <c:if test="${empty activityAirTicket.lockStatus || activityAirTicket.lockStatus==0}">
                                  <c:if test="${companyId!=71 || companyId==71 && isOperator==1}">
                                    <a class="button_addcb_a" onclick="addCostHQX('0')">添加项目</a>
                                  </c:if>
                                 </c:if>
                        </div>
                    </div>
                </div>
             <table class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan">
                    <thead>
                        <tr>
                            <th width="7%">境内/外项目</th>
                            <th width="9%">项目名称</th>
                            <th width="5%">数量</th>
                            <th width="10%">地接社/渠道商</th>
                            <th width="6%">转换前<br>币种</th>
                            <th width="6%">转换前<br>单价</th>
                            <th width="6%">汇率</th>
                            <th width="6%">转换后<br>币种</th>
                            <th width="6%">转换后<br>总价</th>
                            <th width="7%">备注</th>
                            <th width="6%">成本审<br>核状态</th>
                            <th width="6%">录入人</th>
                            <th width="8%">操作</th>
                            <th width="5%">选择</th>
                        </tr>
                    </thead>
            
                        <c:forEach items="${budgetInList}" var="budgetIn" varStatus="status">  
                        <tr class="budgetInCost">
                        <c:if test="${status.count==1}">
                         <td rowspan="${fn:length(budgetInList)}">境内付款明细</td>
                         </c:if>
                        <td  class="tc" name="tdName">${budgetIn.name}</td>
                        <td  class="tc" name="tdAccount">${budgetIn.quantity}</td>
                        <td class="tc" name="tdSupply">
                        	<c:choose>
                        		<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and budgetIn.supplyName eq '非签约渠道' }">未签</c:when>
                        		<c:otherwise>${budgetIn.supplyName}</c:otherwise>
                        	</c:choose>
                         </td>
                        <td class="tc" width="7%" name="tdCurrencyName">
                        	<c:choose>
                        		<c:when test="${budgetIn.name eq '其他'}">-</c:when>
                        		<c:otherwise>
                        			<c:forEach items="${curlist}" var="currency">
                          				<c:if test="${currency.id==budgetIn.currencyId}">
                             				${currency.currencyName}
                          				</c:if>
                        			</c:forEach>
                        		</c:otherwise>
                        	</c:choose>
                        </td>
                        <td  class="tc" name="tdPrice">
                         <c:choose>
                        		<c:when test="${budgetIn.name eq '其他'}">-</c:when>
                        		<c:otherwise>
                        			<c:forEach items="${curlist}" var="currency">
                          				<c:if test="${currency.id==budgetIn.currencyId}">
                             				${currency.currencyMark}
                          				</c:if>
                        			</c:forEach>${budgetIn.price}
                        		</c:otherwise>
                        	</c:choose>
                      </td>
                          <td class="tc">${budgetIn.rate}</td>
                       <td  class="tc" name="tdPrice">
                       <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==budgetIn.currencyAfter}">
                             ${currency.currencyName}
                          </c:if>
                        </c:forEach>
                       </td>
                        <td class="tc">
                            <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==budgetIn.currencyAfter}">
                             ${currency.currencyMark}
                          </c:if>
                        </c:forEach>${budgetIn.priceAfter}                       
                        </td>
                        <td name="tdComment" class="td-remark">${budgetIn.comment}</td>
	                        <td class="tc"  name="tdReview">
	                        <c:if test="${budgetIn.reviewType==0}">
	                        ${fns:getNextCostReview(budgetIn.id)} 
	                        </c:if>
	                         <c:if test="${budgetIn.reviewType!=0}">
	                         ${budgetIn.reviewStatus}
	                         </c:if>
	                       </td>
                        <td class="tc">                       
                             ${budgetIn.createBy.name}                        
                       </td>
                        <td class="tc">
                         <c:if test="${activityAirTicket.lockStatus==0 &&  budgetIn.reviewType==0}">

                        <c:if test="${companyId!=71 || companyId==71 && isOperator==1 }">
                          <c:if test="${ budgetIn.review==4 || budgetIn.review==0|| budgetIn.review==5}">
                          <a href="javascript:void(0)" onclick="updateCostHQX('${budgetIn.id}')">修改</a>&nbsp;&nbsp;<a href="javascript:void(0)"  onclick="return deleteCost('${budgetIn.id}','operator')">删除</a>
                         </c:if>
                         <c:if test="${budgetIn.review==1 }"><a href="javascript:void(0)"  onclick="return cancelCost('${budgetIn.id}','operator')">撤回</a> 
                         </c:if>
                         </c:if>                          

                         </c:if>
                          <c:if test="${activityAirTicket.lockStatus==1}">
                          已经锁定
                          </c:if>
                        <input type="hidden" id="itemId" value="${budgetIn.id}"/>
                        <c:if test="${budgetIn.reviewType eq 0 and budgetIn.review eq 2 }">
							<shiro:hasPermission name="cost:delete">
								<a href="javascript:void(0)" onclick="return deletePassCost('${budgetIn.id}','operator','','')">删除</a>
							</shiro:hasPermission>
						</c:if>
                        </td>
                        <td>
                        <c:if test="${ budgetIn.reviewType==0 && (budgetIn.review==4 || budgetIn.review==5) && activityAirTicket.lockStatus==0}">  
                          <input type="checkbox" value="${budgetIn.id}" id="" name="budgetId"/>
                        </c:if> 
                        </td>                       
                       </tr>
                        </c:forEach>
              
                    <c:if test="${! empty budgetInList}">
                    <tr>
                        <td>境内小计</td>
                        <td colspan="13">&nbsp;<span id="budgetInShow" name="budgetInShow"></span></td>
                    </tr></c:if>
                   
             
                        <c:forEach items="${budgetOutList}" var="budgetOut" varStatus="status">
                        <tr class="budgetOutCost">
                        <c:if test="${status.count==1}">
                         <td rowspan="${fn:length(budgetOutList)}">境外付款明细</td>
                         </c:if>
                        <td class="tc" name="tdName">${budgetOut.name}</td>
                        <td class="tc" name="tdAccount">${budgetOut.quantity}</td>
                        <td class="tc" name="tdSupply">
                        	<c:choose>
                        		<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and budgetOut.supplyName eq '非签约渠道' }">未签</c:when>
                        		<c:otherwise>${budgetOut.supplyName}</c:otherwise>
                        	</c:choose>
                         </td>
                        <td class="tc" name="tdCurrencyName">
                        	<c:choose>
                        		<c:when test="${budgetOut.name eq '其他'}">-</c:when>
                        		<c:otherwise>
                        			<c:forEach items="${curlist}" var="currency">
                          				<c:if test="${currency.id==budgetOut.currencyId}">
                             				${currency.currencyName}
                          				</c:if>
                        			</c:forEach>
                        		</c:otherwise>
                        	</c:choose>
                        </td>
                        <td class="tc"  name="tdPrice">
                          <c:choose>
    							<c:when test="${budgetOut.name eq '其他'}">-</c:when>
    							<c:otherwise>
    								<c:forEach items="${curlist}" var="currency">
			                          <c:if test="${currency.id==budgetOut.currencyId}">
			                             ${currency.currencyMark}
			                          </c:if>
			                        </c:forEach>${budgetOut.price}
    							</c:otherwise>
    						</c:choose>
                      </td>
                       <td class="tc">${budgetOut.rate}</td>
                       <td  class="tc" name="tdPrice">                     
                       <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==budgetOut.currencyAfter}">
                             ${currency.currencyName}
                          </c:if>
                        </c:forEach>
                       </td>
                        <td class="tc">
                            <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==budgetOut.currencyAfter}">
                             ${currency.currencyMark}
                          </c:if>
                        </c:forEach>${budgetOut.priceAfter}                       
                        </td>              
                        <td  name="tdComment" class="td-remark">${budgetOut.comment}</td>
	                          <td class="tc" name="tdReview">
	                         <c:if test="${budgetOut.reviewType==0}">
	                        ${fns:getNextCostReview(budgetOut.id)} 
	                        </c:if>
	                         <c:if test="${budgetOut.reviewType!=0}">
	                         ${budgetOut.reviewStatus}
	                         </c:if>
	                      </td>
                       <td class="tc">                       
                             ${budgetOut.createBy.name}                        
                       </td>
                        <td class="tc">
                          <c:if test="${activityAirTicket.lockStatus==0 && budgetOut.reviewType==0}">
                          <c:if test="${companyId!=71 || companyId==71 && isOperator==1}">
                          <c:if test="${ budgetOut.review==4 || budgetOut.review==0|| budgetOut.review==5}">
                          <a href="javascript:void(0)" onclick="updateCostHQX('${budgetOut.id}')">修改</a>&nbsp;&nbsp;<a href="javascript:void(0)"  onclick="return deleteCost('${budgetOut.id}','operator')">删除</a>
                         </c:if>
                         <c:if test="${budgetOut.review==1 }"><a href="javascript:void(0)"  onclick="return cancelCost('${budgetOut.id}','operator')">撤回</a>  
                         </c:if>
                         </c:if>
                         </c:if>
                          <c:if test="${activityAirTicket.lockStatus==1}">
                          已经锁定
                          </c:if>
                           <input type="hidden" id="itemId" value="${budgetOut.id}"/>
                           <c:if test="${budgetOut.reviewType eq 0 and budgetOut.review eq 2 }">
								<shiro:hasPermission name="cost:delete">
									<a href="javascript:void(0)" onclick="return deletePassCost('${budgetOut.id}','operator','','')">删除</a>
								</shiro:hasPermission>
							</c:if>
                        </td>
                       <td>
                       <c:if test="${ budgetOut.reviewType==0 && (budgetOut.review==4 || budgetOut.review==5) && activityAirTicket.lockStatus==0 }">
                         <input type="checkbox" value="${budgetOut.id}" id="" name="budgetId"/>
                       </c:if>
                        </td>
                       </tr>
                        </c:forEach>                        
               
                    <c:if test="${! empty budgetOutList}">
                    <tr>
                        <td>境外小计</td>
                        <td colspan="13">&nbsp;<span id="budgetOutShow" name="budgetOutShow"></td>
                    </tr></c:if>
                    </tfoot>
                </table>
        </div>

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
                 <!--  <c:if test="${ activityAirTicket.lockStatus ==0  && companyId !=68}">
                     <span onclick="addCurrency('${activityAirTicket.id}','${activityAirTicket.id}','7')" class="btn btn-primary">录入成本与收入</span>
                    </c:if> -->              
                    <ul style="margin: 0px;float: right;">
                    <c:if test="${activityAirTicket.lockStatus==0}"><input class="btn-primary" type="button" value="提交审核"  onclick="saveCheckBox('budgetId','0')" /></c:if>                      
                    </ul> 
               
        </div>

        <c:if test="${companyId!=71 || companyId==71 && isOpt==1}"> 
            <div class="mod_information">
                    <div class="mod_information_d">
                        <div class="ydbz_tit">
                            实际成本录入<c:if test="${activityAirTicket.lockStatus==1}">(已经锁定)</c:if>
                            <div class="button_addcb wpr20 xtjxm">
                             <c:if test="${ activityAirTicket.lockStatus ==0}">
                                <a class="button_addcb_a" onclick="addCostHQX('1')">添加项目</a>
                            </c:if>
                            </div>
                        </div>
                    </div>
             
       
               
    <table class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan">
                    <thead>
                        <tr>
                            <th width="7%">境内/外项目</th>
                            <th width="8%">项目名称</th>
                            <th width="5%">数量</th>
                            <th width="7%">地接社/渠道商</th>
                            <th width="5%">转换前<br>币种</th>
                            <th width="5%">转换前<br>单价</th>
                            <th width="6%">汇率</th>
                            <th width="6%">转换后<br>币种</th>
                            <th width="6%">转换后<br>总价</th>
                            <th width="7%">备注</th>
                            <c:if test="${costAutoPass==0 }"><th width="7%">成本审<br>核状态</th></c:if>
                            <th width="6%">录入人</th>
                            <th width="8%">操作</th>
                            <c:if test="${costAutoPass==0}"> <th width="5%">成本审<br>核申请</th></c:if>
                            <th width="7%">付款审<br>核状态</th>
                            <th width="6%">付款<br>申请</th>
                        </tr>
                    </thead>
                     <tbody>
                        <c:forEach items="${actualInList}" var="actualIn" varStatus="status">
                        <tr class="actualInCost">
                        <c:if test="${status.count==1}">
                         <td  rowspan="${fn:length(actualInList)}">境内付款明细</td>
                         </c:if>
                        <td class="tc" name="tdName">${actualIn.name}</td>
                        <td class="tc" name="tdAccount">${actualIn.quantity}</td>
                        <td  class="tc" name="tdSupply">
                        	<c:choose>
                        		<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and actualIn.supplyName eq '非签约渠道' }">未签</c:when>
                        		<c:otherwise>${actualIn.supplyName}</c:otherwise>
                        	</c:choose>
                         </td>
                        <td class="tc" name="tdCurrencyName">
                        	<c:choose>
    							<c:when test="${actualIn.name eq '其他'}">-</c:when>
    							<c:otherwise>
    								<c:forEach items="${curlist}" var="currency">
                          				<c:if test="${currency.id==actualIn.currencyId}">
                             				${currency.currencyName}
                          				</c:if>
                        			</c:forEach>
    							</c:otherwise>
    						</c:choose>
                        </td>
                        <td  class="tc" name="tdPrice">
                          <c:choose>
    							<c:when test="${actualIn.name eq '其他'}">-</c:when>
    							<c:otherwise>
    								<c:forEach items="${curlist}" var="currency">
			                          <c:if test="${currency.id==actualIn.currencyId}">
			                             ${currency.currencyMark}
			                          </c:if>
			                        </c:forEach>${actualIn.price}
    							</c:otherwise>
    						</c:choose>
                      </td>
                       <td class="tc">${actualIn.rate}</td>
                     <td  class="tc" name="tdPrice">
                       <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualIn.currencyAfter}">
                             ${currency.currencyName}
                          </c:if>
                        </c:forEach>
                       </td>
                        <td class="tc">
                            <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualIn.currencyAfter}">
                             ${currency.currencyMark}
                          </c:if>
                        </c:forEach>${actualIn.priceAfter}                       
                        </td>
                      <td  name="tdComment" class="td-remark">${actualIn.comment}</td>
                      <c:if test="${costAutoPass==0 }">
	                         <td class="tc" name="tdReview">
	                         <c:if test="${actualIn.reviewType==0}">
	                        ${fns:getNextCostReview(actualIn.id)} 
	                        </c:if>
	                         <c:if test="${actualIn.reviewType!=0}">
	                         ${actualIn.reviewStatus}
	                         </c:if>
	                        </td>
                        </c:if>
                         <td class="tc">                       
                             ${actualIn.createBy.name}                        
                       </td>
                        <td class="tc">
                          <c:if test="${activityAirTicket.lockStatus==0 &&  actualIn.reviewType==0}">
                          <c:if test="${ actualIn.review==4  || actualIn.review==0 || actualIn.review==5  }">
                           <a href="javascript:void(0)" onclick="updateCostHQX('${actualIn.id}')">修改</a>&nbsp;&nbsp;<a href="javascript:void(0)"  onclick="return deleteCost('${actualIn.id}','operator')">删除</a>
                         </c:if>

                            <c:if test="${ actualIn.review==2 && ( actualIn.payReview==4 || actualIn.payReview==0|| actualIn.payReview==5)}">
                          <a href="javascript:void(0)" onclick="updateCostHQX('${actualIn.id}')">修改</a>&nbsp;&nbsp;<a href="javascript:void(0)"  onclick="return deleteCost('${actualIn.id}','operator')">删除</a>
                         </c:if>
                         
                          <c:if test="${actualIn.review==1 }">
                           <a href="javascript:void(0)"  onclick="return cancelCost('${actualIn.id}','operator')">撤回成本申请</a>
                         </c:if> 
                           <a href="javascript:void(0)"  onclick="return cancelPayCost('${actualIn.payReviewUuid}')">取消付款申请</a>
                         </c:if>
              <c:if test="${activityAirTicket.lockStatus==1}">已经锁定</c:if>
                          <input type="hidden" id=" " value="${actualIn.id}"/>
                          <c:if test="${actualIn.reviewType eq 0 and actualIn.review eq 2 and (actualIn.payReview eq 1 or actualIn.payReview eq 2 or actualIn.payReview eq 3)}">
							<shiro:hasPermission name="cost:delete">
								<a href="javascript:void(0)" onclick="return deletePassCost('${actualIn.id}','operator','','')">删除</a>
							</shiro:hasPermission>
						</c:if>
                        </td> 
                       
                        <c:if test="${costAutoPass==0}"> 
                        <td>
                        <c:if test="${actualIn.reviewType==0 &&( actualIn.review==4 || actualIn.review==5)  && activityAirTicket.lockStatus==0}">  
                          <input type="checkbox" value="${actualIn.id}" id="" name="actualId"/>
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
                         <td>
                       <c:if test="${actualIn.review==2 && (actualIn.payReview==4 || actualIn.payReview==5) && actualIn.reviewType==0}">  
                          <input type="checkbox" value="${actualIn.activityId},${actualIn.orderType},${actualIn.uuid}" id="" name="payId"/>
                        </c:if> 
                        </td>                            
                       </tr>
                    </c:forEach>
            
                    <c:if test="${! empty actualInList}">
                    <tr>
                        <td>境内小计</td>
                        <td colspan=<c:if test="${costAutoPass==0}">"15"</c:if><c:if test="${costAutoPass==1}">"14"</c:if>>&nbsp;<span id="actualInShow" name="actualInShow"></td>
                    </tr></c:if>
              
                    
                        <c:forEach items="${actualOutList}" var="actualOut" varStatus="status">
                        <tr class="actualOutCost">
                        <c:if test="${status.count==1}">
                         <td  rowspan="${fn:length(actualOutList)}">境外付款明细</td>
                         </c:if>
                        <td  class="tc"name="tdName">${actualOut.name}</td>
                        <td  class="tc" name="tdAccount">${actualOut.quantity}</td>
                        <td  class="tc" name="tdSupply">
                        	<c:choose>
                        		<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and actualOut.supplyName eq '非签约渠道' }">未签</c:when>
                        		<c:otherwise>${actualOut.supplyName}</c:otherwise>
                        	</c:choose>
                         </td>
                        <td  class="tc" name="tdCurrencyName">
                        	<c:choose>
    							<c:when test="${actualOut.name eq '其他'}">-</c:when>
    							<c:otherwise>
    								<c:forEach items="${curlist}" var="currency">
                          				<c:if test="${currency.id==actualOut.currencyId}">
                             				${currency.currencyName}
                          				</c:if>
                        			</c:forEach>
    							</c:otherwise>
    						</c:choose>
                        </td>                        
                        <td  class="tc" name="tdPrice">
                          	<c:choose>
    							<c:when test="${actualOut.name eq '其他'}">-</c:when>
    							<c:otherwise>
    								<c:forEach items="${curlist}" var="currency">
    									<c:if test="${currency.id==actualOut.currencyId}">
                             				${currency.currencyMark}
                          				</c:if>
                        			</c:forEach>${actualOut.price}
    							</c:otherwise>
    						</c:choose>
                       </td>
                         <td class="tc">${actualOut.rate}</td>
                        <td  class="tc" name="tdPrice">
                       <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualOut.currencyAfter}">
                             ${currency.currencyName}
                          </c:if>
                        </c:forEach>
                       </td>
                        <td class="tc">
                            <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualOut.currencyAfter}">
                             ${currency.currencyMark}
                          </c:if>
                        </c:forEach>${actualOut.priceAfter}                       
                        </td>
                        <td  name="tdComment" class="td-remark">${actualOut.comment}</td>
                        <c:if test="${costAutoPass==0 }">
                         <td class="tc" name="tdReview">
	                         <c:if test="${actualOut.reviewType==0}">
	                        ${fns:getNextCostReview(actualOut.id)} 
	                        </c:if>
	                         <c:if test="${actualOut.reviewType!=0}">
	                         ${actualOut.reviewStatus}
	                         </c:if>
	                      </td>
                      </c:if>
                      <td class="tc">                       
                             ${actualOut.createBy.name}                        
                       </td>
                        <td class="tc"> <c:if test="${activityAirTicket.lockStatus==0 && actualOut.reviewType==0}">
                         <c:if test="${ actualOut.review==4  || actualOut.review==0 || actualOut.review==5 }">
                          <a href="javascript:void(0)" onclick="updateCostHQX('${actualOut.id}')">修改</a>&nbsp;&nbsp;<a href="javascript:void(0)"  onclick="return deleteCost('${actualOut.id}','operator')">删除</a>
                         </c:if>

                            <c:if test="${ actualOut.review==2 && ( actualOut.payReview==4 || actualOut.payReview==0|| actualOut.payReview==5)}">
                          <a href="javascript:void(0)" onclick="updateCostHQX('${actualOut.id}')">修改</a>&nbsp;&nbsp;<a href="javascript:void(0)"  onclick="return deleteCost('${actualOut.id}','operator')">删除</a>
                         </c:if>
                         
                          <c:if test="${actualOut.review==1 }">
                           <a href="javascript:void(0)"  onclick="return cancelCost('${actualOut.id}','operator')">撤回成本申请</a>
                         </c:if> 
                           <a href="javascript:void(0)"  onclick="return cancelPayCost('${actualOut.payReviewUuid}')">取消付款申请</a>
                         </c:if>
                          <c:if test="${activityAirTicket.lockStatus==1}">已经锁定</c:if>
                           <input type="hidden" id="itemId" value="${actualOut.id}"/>
                           <c:if test="${actualOut.reviewType eq 0 and actualOut.review eq 2 and (actualOut.payReview eq 1 or actualOut.payReview eq 2 or actualOut.payReview eq 3)}">
								<shiro:hasPermission name="cost:delete">
									<a href="javascript:void(0)" onclick="return deletePassCost('${actualOut.id}','operator','','')">删除</a>
								</shiro:hasPermission>
							</c:if>
                         </td> 

                        <c:if test="${costAutoPass==0}"> 
                        <td>
                        <c:if test="${actualOut.reviewType==0 && (actualOut.review==4 || actualOut.review==5 ) && activityAirTicket.lockStatus==0 }">  
                          <input type="checkbox" value="${actualOut.id}" id="" name="actualId"/>
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
                          <td>
                   <c:if test="${actualOut.review==2 &&(actualOut.payReview==4||actualOut.payReview==5) && actualOut.reviewType==0}">                        
                          <input type="checkbox" value="${actualOut.activityId},${actualOut.orderType},${actualOut.uuid}" id="" name="payId"/>
                      </c:if> 
                        </td> 
                       </tr>
                        </c:forEach>                           
               
                    <c:if test="${! empty actualOutList}">
                    <tr>
                        <td>境外小计</td>
                        <td colspan=<c:if test="${costAutoPass==0 }">"15"</c:if><c:if test="${costAutoPass==1}">"14"</c:if>>&nbsp;<span id="actualOutShow" name="actualOutShow"></td>
                    </tr></c:if>
                    </tbody>
                </table> 
        </div>
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
                <c:if test="${costAutoPass==0&& activityAirTicket.lockStatus==0 }"><input  class="btn-primary" type="button" value="成本审核"  onclick="saveCheckBox('actualId','1')" /></c:if>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;<c:if test="${activityAirTicket.lockStatus==0}"><input  class="btn-primary" type="button" value="付款申请"  onclick="payCheckBox('payId','1')" /></c:if>                       
                    </ul> 
       </div>
      
      </c:if>


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

                <!--
          <div class="release_next_add"> 
                <c:if test="${empty  activityAirTicket.lockStatus || activityAirTicket.lockStatus==0}">          
                <input class="btn btn-primary" type="button" value="提 交"  onclick="submitform('${activityAirTicket.id}')" /> 
                </c:if>  
                 <c:if test="${activityAirTicket.lockStatus==1}">          
                <input class="btn btn-primary" type="button" value="提 交"  onclick="javascript:alert('已经锁定,不能提交')" /> 
                </c:if>                 
            </div> -->
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
                <label>批发商：</label><input type="text" id="supply0" name="supply0" readonly="true" value="" /> <!--<select id="supply0" name="supply0">
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

         


<script type="text/javascript">
function trimStr(str){return str.replace(/(^\s*)|(\s*$)/g,"");}
//运控-成本录入-添加项目--小计
function costSums(obj,objshow,ordertype){    
  var objMoney = {}; 
     obj.each(function(index, element) { 
    //var currencyName = $(element).find("td[name='tdCurrencyName']").text();
    var thisAccount = $(element).find("td[name='tdAccount']").text();
    if(thisAccount == '') {
      thisAccount = 1;
    }
    var thisPrice = $(element).find("td[name='tdPrice']").text();   
    if(thisPrice.indexOf('-')!=-1) thisPrice = $(element).find("td[name='tdPrice']").next().next().next().text();
    var thisReview = $(element).find("td[name='tdReview']").text();   
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
        var currencyName =thisPrice.substring(0,border).trim();
    //金额去掉第一个字符(币种)
    thisPrice=thisPrice.substring(border);  
  if(ordertype==2 || (ordertype==0 && trimStr(thisReview) != '已取消')|| (ordertype==1 && trimStr(thisReview) != '已取消' && (trimStr(thisReview) != '已驳回'&&trimStr(thisReview) != '审核失败(驳回)') )){        
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
    costSums($('tr.otherCost'),$('#otherCostShow'),2);
    //实际成本录入-境内小计
    costSums($('tr.budgetInCost'),$('#budgetInShow'),0);
    //实际成本录入-境外小计  
    costSums($('tr.budgetOutCost'),$("#budgetOutShow"),0);

    costSums($('tr.actualInCost'),$("#actualInShow"),1);
    
    costSums($('tr.actualOutCost'),$("#actualOutShow"),1); 

});
</script>
    
</body>
</html>