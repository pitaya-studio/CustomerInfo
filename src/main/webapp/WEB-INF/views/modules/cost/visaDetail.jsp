<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>产品成本录入</title>
    <meta name="decorator" content="wholesaler"/>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactor.js" type="text/javascript"></script>
    <script src="${ctxStatic}/json/jquery.json.ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>    
    <script src="${ctxStatic}/jquery-other/jquery-combobox_prop.js" type="text/javascript"></script>
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
      
      $(document).delegate(".downloadzfpz","click",function(){
        window.open ("${ctx}/sys/docinfo/download/"+$(this).attr("lang"));
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
       
        var groupId = '${visaProduct.id}';
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
           //初始化批发商下拉框的值
           var supplierFirst = $("#first").val();
           if(null!= supplierFirst && ""!=supplierFirst){  
        	   var noCache = Date(); 
        	   $.getJSON("${ctx}/cost/manager/supplylist/"+supplierFirst,{"noCache":noCache},function(myJSON){ 
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
           '</select> </span>'+ 
           '</td></tr>'+ 
           '<tr><td align="right">项目名称：</td><td>'+ 
           '<input type="text" name="itemname"  id="itemname"  style="width:180px" />'+ 
           ' </td></tr><tr><td align="right">单价：</td><td>'+ 
             $currencyId.html() +   
           ' </td></tr><tr><td align="right">数量：</td>'+ 
           '<td><input type="text" name="quantity"  id="quantity"  style="width:180px"/>'+
           '</td></tr> <tr><td align="right">项目备注：</td><td>'+
           '<textarea rows="3" cols="20" id="comment" name="comment"  style="width:180px"></textarea></td></tr>'+
           '</table></div> '; 

           selectchange('1');    
     
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
        $.jBox(html, { title: '成本录入', buttons:{ '取消' : 0,'确定' : 1 }, submit: submit, height:440});
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


    var save = function(){
        var groupId = '${visaProduct.id}';
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
        var groupId = '${visaProduct.id}';
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
   
    $.jBox('<div class="costBox">'+$templateClone.html()+'</div>', { title: "成本修改",buttons:{'取消': false, '提交': true}, submit:function(v, h, f){
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
                     url: "${ctx}/cost/visa/submitvisa",                      
                     cache:false,
                     dataType:"json",
                     async:false,
                     data:{  format:"json",id : id },
                     success: function (data){ 
                       alert('提交成功');
                       window.location.href="${ctx}/cost/visa/list";
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
      var groupId = '${visaProduct.id}';
      var iframe = "iframe:${ctx}/cost/manager/addCostHQX/"+ '${visaProduct.id}' +"/"+ '${visaProduct.id}' +"/"+ budgetType  +"/6/"+ '${deptId}';    

      $.jBox(iframe, {
            title: "成本录入",
            width: 380,
            height: 640,
			persistent:true,
            buttons: {}
      });
  } 
  
  function addOtherCostHQX(budgetType) {
      var groupId = '${visaProduct.id}';
      var iframe = "iframe:${ctx}/cost/manager/addCostHQX/"+ '${visaProduct.id}' +"/"+ '${visaProduct.id}' +"/"+ budgetType  +"/6/"+ '${deptId}';     

      $.jBox(iframe, {
            title: "其它收入录入",
            width: 380,
            height: 655,
			persistent:true,
            buttons: {}
      });
  }



      function updateCostHQX(costid) {
      var groupId = '${visaProduct.id}';
      var iframe = "iframe:${ctx}/cost/manager/updateCostHQX/"+ '${visaProduct.id}' +"/"+ '${visaProduct.id}' +"/"+ costid   +"/6/"+ '${deptId}';     

      $.jBox(iframe, {
            title: "成本修改",
            width: 380,
            height: 620,
			persistent:true,
            buttons: {}
      });
  }
  
  function updateOtherCostHQX(costid) {
      var groupId = '${visaProduct.id}';
      var iframe = "iframe:${ctx}/cost/manager/updateCostHQX/"+ '${visaProduct.id}' +"/"+ '${visaProduct.id}' +"/"+ costid  +"/6/"+ '${deptId}';     

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
                            costList:tmp,visaIds:"",groupId:"",orderType:"" },
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
    		url: "${ctx}/review/visa/payment/apply",
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
</script>
</head>
<body>
      <page:applyDecorator name="cost_input_head">
      <page:param name="current">visa</page:param>
      </page:applyDecorator>
    <div class="mod_nav">运控 > 产品成本录入 > 签证录入详情页</div>
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
                            <td colspan="4"></td>
                        </tr>
                        </tbody></table>

                <div class="kong"></div>
            </div>
        </div>


<div class="mod_information">
          <div class="mod_information_d">
            <div class="ydbz_tit">订单列表 &nbsp; <a class="zksxs">收起筛选</a></div>
          </div>
        </div>
        <div class="ydxbds">
        <table style="border-top: 1px solid #dddddd" class="activitylist_bodyer_table mod_information_dzhan" id="contentTable">
          <thead style="background: #403738;">
            <tr>
                          <th width="4%">序号</th>
                            <th width="7%">预定渠道</th>
                            <th width="11%">订单号</th>
                            <th width="11%">产品编号</th>
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
              <td>
				<c:choose>
					<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and visaOrder.agentName eq '非签约渠道' }">未签</c:when>
					<c:otherwise>${visaOrder.agentName}</c:otherwise>
				</c:choose>
              </td>             
              <td>${visaOrder.orderNo}</td>
              <td class="tc"> ${visaProduct.productCode}</td>
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
      </div>
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
                            其它收入录入<c:if test="${visaProduct.lockStatus==1}">(已经锁定)</c:if>
                            <div class="button_addcb wpr20 xtjxm">
                                 <c:if test="${empty visaProduct.lockStatus || visaProduct.lockStatus==0}">
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
                         <c:if test="${visaProduct.lockStatus==0 }">
                        <c:if test="${companyId!=71 || companyId==71 && isOpt!=1 }">                       
                          <a href="javascript:void(0)" onclick="updateOtherCostHQX('${otherCost.id}')">修改</a>&nbsp;&nbsp;<a href="javascript:void(0)"  onclick="return deleteCost('${otherCost.id}','operator')">删除</a>
                         </c:if>                        
                         </c:if>
                          <c:if test="${visaProduct.lockStatus==1}">
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

           <div class="mod_information">
                    <div class="mod_information_d">
                        <div class="ydbz_tit">
                            预算成本录入<c:if test="${visaProduct.lockStatus==1}">(已经锁定)</c:if>
                            <div class="button_addcb wpr20 xtjxm">
                               <c:if test="${empty visaProduct.lockStatus || visaProduct.lockStatus== 0}">
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
                            <th width="9%">备注</th>
                            <th width="8%">成本审<br>核状态</th>
                            <th width="6%">录入人</th>
                            <th width="8%">操作</th>
                            <th width="6%">选择</th>
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
	                        <td class="tc" name="tdReview">
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
                         <c:if test="${visaProduct.lockStatus==0 &&  budgetIn.reviewType==0}">
                        <c:if test="${companyId!=71 || companyId==71 && isOperator==1 }">
                        <c:if test="${ budgetIn.review==4 || budgetIn.review==0|| budgetIn.review==5}">
                          <a href="javascript:void(0)" onclick="updateCostHQX('${budgetIn.id}')">修改</a>&nbsp;&nbsp;<a href="javascript:void(0)"  onclick="return deleteCost('${budgetIn.id}','operator')">删除</a>
                         </c:if>
                        <c:if test="${budgetIn.review==1 }"><a href="javascript:void(0)"  onclick="return cancelCost('${budgetIn.id}','operator')">撤回</a>
                         </c:if>
                         </c:if>               
                         </c:if>
                          <c:if test="${visaProduct.lockStatus==1}">
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
                        <c:if test="${ budgetIn.reviewType==0 &&(budgetIn.review==4  || budgetIn.review==5)  &&  visaProduct.lockStatus==0 }">  
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
                          <c:if test="${visaProduct.lockStatus==0 &&  budgetOut.reviewType==0}">
                          <c:if test="${companyId!=71 || companyId==71 && isOperator==1}">
                          <c:if test="${ budgetOut.review==4 || budgetOut.review==0|| budgetOut.review==5}">
                          <a href="javascript:void(0)" onclick="updateCostHQX('${budgetOut.id}')">修改</a>&nbsp;&nbsp;<a href="javascript:void(0)"  onclick="return deleteCost('${budgetOut.id}','operator')">删除</a>
                         </c:if>
                        <c:if test="${budgetOut.review==1 }"><a href="javascript:void(0)"  onclick="return cancelCost('${budgetOut.id}','operator')">撤回</a> 
                         </c:if>
                         </c:if>
                         </c:if>
                          <c:if test="${visaProduct.lockStatus==1}">
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
                       <c:if test="${budgetOut.reviewType==0 && (budgetOut.review==4  || budgetOut.review==5)  &&  visaProduct.lockStatus==0}">
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


                    
                 <!-- <c:if test="${ visaProduct.lockStatus ==0  && companyId !=68 }">
                   <span onclick="addCurrency('${visaProduct.id}','${visaProduct.id}','6')" class="btn btn-primary">录入成本与收入</span>
                    </c:if> -->                 
                    <ul style="margin: 0px;float: right;">
                    <c:if test="${visaProduct.lockStatus==0}"><input class="btn-primary" type="button" value="提交审核"  onclick="saveCheckBox('budgetId','0')" /></c:if>                     
                    </ul> 
                  
        </div> 
<c:if test="${companyid!=71 || companyid==71 && isOpt==1}"> 
            <div class="mod_information">
                    <div class="mod_information_d">
                        <div class="ydbz_tit">
                            实际成本录入 <c:if test="${visaProduct.lockStatus==1}">(已经锁定)</c:if>
                            <div class="button_addcb wpr20 xtjxm">
                              <c:if test="${ visaProduct.lockStatus ==0}">
                               <a class="button_addcb_a" onclick="addCostHQX('1')">添加项目</a>
                            </c:if>
                            </div>
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
                            <c:if test="${costAutoPass==0}">  <th width="5%">成本审<br>核申请</th></c:if>
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
                        <c:if test="${visaProduct.lockStatus==0 &&  actualIn.reviewType==0}">
                         <c:if test="${ actualIn.review==4  || actualIn.review==0 || actualIn.review==5 }">
                           <a href="javascript:void(0)" onclick="updateCostHQX('${actualIn.id}')">修改</a>&nbsp;&nbsp;<a href="javascript:void(0)"  onclick="return deleteCost('${actualIn.id}','operator')">删除</a>
                         </c:if>

                            <c:if test="${ actualIn.review==2 && ( actualIn.payReview==4 || actualIn.payReview==0 || actualIn.payReview==5)}">
                          <a href="javascript:void(0)" onclick="updateCostHQX('${actualIn.id}')">修改</a>&nbsp;&nbsp;<a href="javascript:void(0)"  onclick="return deleteCost('${actualIn.id}','operator')">删除</a>
                         </c:if>
                         
                          <c:if test="${actualIn.review==1 }">
                           <a href="javascript:void(0)"  onclick="return cancelCost('${actualIn.id}','operator')">撤回成本申请</a>
                         </c:if> 
                           <a href="javascript:void(0)"  onclick="return cancelPayCost('${actualIn.payReviewUuid}')">取消付款申请</a>
                         </c:if>
                          <c:if test="${visaProduct.lockStatus==1}">
                          已经锁定
                          </c:if>
                          <input type="hidden" id=" " value="${actualIn.id}"/>
                          <c:if test="${actualIn.reviewType eq 0 and actualIn.review eq 2 and (actualIn.payReview eq 1 or actualIn.payReview eq 2 or actualIn.payReview eq 3)}">
								<shiro:hasPermission name="cost:delete">
									<a href="javascript:void(0)" onclick="return deletePassCost('${actualIn.id}','operator','','')">删除</a>
								</shiro:hasPermission>
							</c:if>
                        </td> 
                   
                       <c:if test="${costAutoPass==0}"> 
                        <td>
                         <c:if test="${actualIn.reviewType==0 && (actualIn.review==4 || actualIn.review==5)  &&  visaProduct.lockStatus==0}">  
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
                       <c:if test="${ actualIn.review==2 && (actualIn.payReview==4 || actualIn.payReview==5) && actualIn.reviewType==0 }">  
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
	                         <td class="tc"name="tdReview">
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
                        <td class="tc"> <c:if test="${visaProduct.lockStatus==0 && actualOut.reviewType==0}">
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
                          <c:if test="${visaProduct.lockStatus==1}">
                          已经锁定
                          </c:if>
                           <input type="hidden" id="itemId" value="${actualOut.id}"/>
                           <c:if test="${actualOut.reviewType eq 0 and actualOut.review eq 2 and (actualOut.payReview eq 1 or actualOut.payReview eq 2 or actualOut.payReview eq 3)}">
								<shiro:hasPermission name="cost:delete">
									<a href="javascript:void(0)" onclick="return deletePassCost('${actualOut.id}','operator','','')">删除</a>
								</shiro:hasPermission>
							</c:if>
                         </td> 

                       <c:if test="${costAutoPass==0}"> 
                         <td>
                          <c:if test="${actualOut.reviewType==0 && (actualOut.review==4 || actualOut.review==5) &&  visaProduct.lockStatus==0 }">  
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
              <c:if test="${actualOut.review==2 && (actualOut.payReview==4||actualOut.payReview==5) && actualOut.reviewType==0 }">                          
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
                   <!-- <tr>
                        <td>提交审核</td>
                        <td colspan="11"><td colspan="2">                       
                          <input  style="float: right;widht:70px" class="btn-primary" type="button" value="提交审核"  onclick="saveCheckBox('actualId','1')" />
                        </td>
                         <td colspan="2">                          
                          <input style="float: right;" class="btn-primary" type="button" value="付款申请"  onclick="payCheckBox('payId','1')" /> 
                        </td> 
                    </tr> -->
                </tbody>
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
                <c:if test="${costAutoPass==0 && visaProduct.lockStatus==0 }"><input  class="btn-primary" type="button" value="成本审核"  onclick="saveCheckBox('actualId','1')" /></c:if>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;<c:if test="${visaProduct.lockStatus==0}"><input  class="btn-primary" type="button" value="付款申请"  onclick="payCheckBox('payId','1')" /></c:if>                       
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
                <c:if test="${ empty visaProduct.lockStatus || visaProduct.lockStatus==0}">          
                <input class="btn btn-primary" type="button" value="提 交"  onclick="submitform('${visaProduct.id}')" /> 
                </c:if>               
                 <c:if test="${visaProduct.lockStatus==1}">          
                <input class="btn btn-primary" type="button" value="提 交"  onclick="javascript:alert('已经锁定,不能提交')" /> 
                </c:if>                  
                </div> -->

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
                <label>批发商：</label><input type="text" id="supply0" name="supply0" readonly="true" value="" /><!--<select id="supply0" name="supply0">
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
