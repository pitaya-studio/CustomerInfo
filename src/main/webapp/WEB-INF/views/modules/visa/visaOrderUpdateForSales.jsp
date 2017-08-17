<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单-签证-销售身份-单办签订单修改</title>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="${ctxStatic }/css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="${ctxStatic }/js/bootstrap-ie.min.js"></script>
<![endif]-->
	<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%><script src="${ctxStatic }/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic }/modules/visa/common.js" type="text/javascript" ></script>
<script src="${ctxStatic}/modules/visa/visaOrder4orderupdate.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/visa/visapreordercommon4orderupdate.js" type="text/javascript"></script>

<style type="text/css">
input[disabled],select[disabled],textarea[disabled],input[readonly],select[readonly],textarea[readonly],
input[disabled]:focus,select[disabled]:focus,textarea[disabled]:focus,input[readonly]:focus, select[readonly]:focus,textarea[readonly]:focus{
    cursor:auto;
    background:transparent;
    border:0px;
    box-shadow:inset 0 0px 0px rgba(0,0,0,0.075)
}

.ipt3, input[type="text"].ipt3 {
 width: 38px;
  margin-left: 10px;
  margin-right: 0;
  padding-left: 17px;
 }
	.uploadlong {
		display: block;
		width: 6cm;
		overflow: hidden;
		white-space: nowrap;
		text-overflow: ellipsis;
	}
.orderdetails6 span {
	width: 14.2%;
	float: left;
}
.ellipsis {
	overflow: hidden;
	white-space: nowrap;
	text-overflow: ellipsis;
	padding-right:10px;
}
</style>

<script type="text/javascript">

g_context_url = "${ctx}";
var currencyList = "";
$(function(){
	
	/***************************************************************/
	/*112需求-特殊备注的提示展示										   */
	/***************************************************************/
        $("#specialremark").focus(function(){
            $(".ipt-tips").hide();
        });

        $("#specialremark").blur(function(){
            if($("#specialremark").val()==''){
                $(".ipt-tips").show();
            }else{
            $(".ipt-tips").hide();
        }
        });
	
	//自备签类型
	ydbz2zibeiqian();
	
	jQuery.extend(jQuery.validator.messages, {
        required: "必填信息"
    });
	recountIndexTraveler();
	beforePrice();
	var value = $("#specialremark").text().trim();
	$("#specialremark").text(value);
});


/*订单修改预加载*/
function  beforePrice(){
$("#traveler form").each(function(index, element) {
    var _travelerForm=$(element);
	//默认添加游客信息时，判断什么游客类型
		var selFlag = false;
		var selJsPrice = 0;
		var selSrcPriceCurrency;
		var selSrcPriceCurrencyMark;
		var selSrcPriceCurrencyName;
		var personType = 0;
		if ($("#orderPersonelNum").val() > countAdult()){
			selFlag = true;
			selJsPrice = $('#crj').val();
			selSrcPriceCurrency = $('#crbz').val();
			selSrcPriceCurrencyMark  = $('#crbzm').val();
			selSrcPriceCurrencyName = $('#crbmc').val();
			personType = 0;
		}
		if( undefined == selSrcPriceCurrencyName){
		selSrcPriceCurrencyName = "";
		}
		//填充游客内部显示的结算价
		$("span[name=innerJsPrice]",_travelerForm).html(selSrcPriceCurrencyName + selJsPrice.toString().formatNumberMoney('#,##0.00'));
		$("input[name=payPrice]",_travelerForm).val(selJsPrice);
		//存放不同类型游客的同行价
		$("input[name=srcPrice]",_travelerForm).val(selJsPrice);
		//存放不同类型游客的同行价币种
		$("input[name=srcPriceCurrency]",_travelerForm).val(selSrcPriceCurrency);
		//填充单个游客信息收起显示的结算价格
		$("span[name=jsPrice]",_travelerForm).html(selSrcPriceCurrencyName + selJsPrice.toString().formatNumberMoney('#,##0.00'));
		//组装每种游客同行价的对象
		var priceObj = new Object();
		priceObj.currencyId = selSrcPriceCurrency;
		priceObj.price =  selJsPrice;
		var priceObjArr = new Array();
		priceObjArr.push(priceObj);
		var travelerJsPrice = new Object();
		travelerJsPrice.jsPrice = priceObjArr;
		travelerTotalPriceArr.push(travelerJsPrice);
		//changePayPriceByCostChange($(element));
});

}
function viewdetail(uuid,verifyStatus){
	verifyStatus = '-2';
	window.open("${ctx}/invoice/limit/supplyviewrecorddetail/" + uuid + "/" + verifyStatus);
}
function viewdetail4Receipt(uuid,verifyStatus,orderType){
	verifyStatus = '-2';
	window.open("${ctx}/receipt/limit/supplyviewrecorddetail/" + uuid + "/" + verifyStatus + "/" + orderType);
}

//得到焦点事件：隐藏填写费用名称提示
function payforotherIn(doc) {
	var obj = $(doc);
	obj.siblings(".ipt-tips2").hide();
}

//失去焦点事件：如果输入框中没有值，则提示填写费用名称
function payforotherOut(doc){
	var obj = $(doc);
	if(!obj.val()){
		obj.siblings(".ipt-tips2").show();
	}
}

//特殊需求备注 保存 
function doUpdateVisaOrderRemark(obj,orderId){
	var value = $(obj).parent().parent().find("textarea").val();
	$.ajax({    
		cache : true,
		type: "POST",   
		url:g_context_url+"/visa/order/doUpdateVisaOrderRemark",                 
		data:{
			"remark":value,
			"orderId":orderId
		},
		async: false,
		success: function(data) {  
			if(data=="true"){
				$.jBox.tip("保存成功！", "success"); 
			}
		}             
	});
}

//点击提示错误信息中 "修改" 后错误输入框得到焦点
function focusIpt(doc){
	$(doc).parent().find('input[type=text].ipt2').trigger("focus");
}

function commenFunction(obj,fileIDList,fileNameList,filePathList){
	if(fileIDList.indexOf(",") > 0){
		fileIDList = fileIDList.substring(0,fileIDList.length-1);
	}
	
	$(obj).prev().val(fileIDList);
	var html = '<a onclick="downloadDocs(\''+fileIDList+'\')" >'+fileNameList+'</a>';
	$(obj).parent().find("p").find("span").html(html);
}

function updateTraveler(){
	$(obj).text("保存");
	$(obj).parent().prev().show();
	$(obj).parent().parent().find('.tourist-t-off').css("display","none");
	$(obj).parent().parent().find('.tourist-t-on').show();
	$(obj).parent().parent().find('.add_seachcheck').removeClass('boxCloseOnAdd');
}

//预定保存游客信息
function mySaveTraveler(obj){
	if($(obj).text()=="保存"){//按钮的字面值为"保存"
		if($(obj).parent().parent().parent().validate({}).form()){
			//游客的id
			var tempTravelerId=$(obj).parent().parent().parent().find("input[name='travelerId']").val();
			//$(obj).parent().parent().parent().find("input[name='name']")
			//0318新增是否允许修改销售签证订单下的游客信息--s-校验游客姓名是否为空规则:非环球行批发商需要校验-------------------//
			var companyUuid='${fns:getUser().company.uuid}';//登录用户的所属批发商的uuid
			var tempTravelerName=$(obj).parent().parent().parent().find("input[name='name']").val();//修改后的游客姓名
			var checkFlag=null;//校验是否通过的标志,主要用于当游客为有借款,送签时,将游客的基本信息还原为修改前的值
			if('7a816f5077a811e5bc1e000c29cf2586'!=companyUuid){ //非环球行需要校验游客姓名不能为空,并且还要是配置为可修改(这里取巧用可编辑时为文本框来代替)
			   if(!(undefined==tempTravelerName)){//姓名为文本框时,则是可配置的
			   if(tempTravelerName.trim().length==0||tempTravelerName==''||tempTravelerName==null){
				   $.jBox.tip("请填写姓名", "error");
				   return false;
			   }
			   }
			}
			//0318新增是否允许修改销售签证订单下的游客信息--e-校验游客姓名是否为空规则:非环球行批发商需要校验--------------------//
			
			//var rebatesMoney = $("input[name=inputClearPrice]",travelerForm).val();	//返佣费用金额
			var data = $(obj).parent().parent().parent().serialize();
			var $UUid= $(obj).parent().parent().parent();
			var data2 = data.substring(data.indexOf("start")+7,data.indexOf("end")-1);
			var payPriceSerialNum =$UUid.find("#payPriceSerialNum").val();//$("#payPriceSerialNum").val();
			data = data.replace("srcPriceCurrency","srcPriceCur");
			var orderId=$UUid.find("#traveler_OrderId").val();
			
			 //wxw added 20150817
			 var visaTravelerRebateCurrencyId = $(obj).parent().parent().find("#visaTravelerRebateCurrencyId").val();
			 var visaTravelerRebateAmount = $(obj).parent().parent().find("#visaTravelerRebateAmount").val();
			 //debugger;
			 //alert(visaTravelerRebateCurrencyId);
			 //alert(visaTravelerRebateAmount);
			 
			 data.rebatesCurrencyID = visaTravelerRebateCurrencyId;
			 data.rebatesAmount =visaTravelerRebateAmount;
			 
			 // 0615需求,新增是否需要押金,是否需要上传资料限定为批发商大洋国旅
			/* var depositValue=$(obj).parent().parent().find("input[name='depositValue']:checked").val();
			 if(depositValue!=undefined){
			 data.depositValue=depositValue;
			 }
			 var datumValue =$(obj).parent().parent().find("input[name='datumValue']:checked").val();
			 if(datumValue!=undefined){
			    data.datumValue=datumValue;
			 }*/
			 //0615需求,新增是否需要押金,是否需要上传资料限定为批发商大洋国旅
			 
			//---0211需求,新增预计回团时间,限定为批发商星徽四海-s--//
				 //对预计归团时间进行必填校验
				 var tempForecastBackDate=$(obj).parent().parent().parent().find("input[name='forecastBackDate']").val();
			     //alert(tempForecastBackDate=="");
			     if(tempForecastBackDate==''){ //非空校验
			    	 $.jBox.tip("请填写预计回团时间", "error");
			    	 return false;
			     }
			//---0211需求,新增预计回团时间,限定为批发商星徽四海-e--//
			   //0318新增是否允许修改销售签证订单下的游客信息--s----校验游客是否是有借款,有送签,环球行批发商不受此限制---------------------//
					$.ajax({
						type:"post",
						async:false,
						url:"${ctx}/visa/order/checkTraveler",
						data:{
							"travelerId":tempTravelerId
						},
					    success:function(msg){//msg一个对象的集合,因为这里只会查出一个游客的信息,所以只要取索引0即可
					    	var travelerVisaStatus=msg[0].visaStatus;
					        var travelerJieKuanStatus=msg[0].jiekuanStatus;
					    	//根据0318的需求,当游客为有借款(借款状态为"审批中"),有送签(签证状态为送签时--"送签"的值这里取1,),阻止该游客修改基本信息--针对的是非环球行用户
					    	//按照当前系统已有代码无法正向判断审批中,所以反向判断(A=A,A=!!A),当然如果定义了新的借款状态,这里的条件需要作出相应的变化-TODO
					    	if('7a816f5077a811e5bc1e000c29cf2586'!=companyUuid){//非环球行用户,需要校验
					    		if('1'==travelerVisaStatus||(!(('未借'==travelerJieKuanStatus)||('审批通过'==travelerJieKuanStatus)||('审批驳回'==travelerJieKuanStatus)))){//借款状态为:审批中,签证状态为:送签(取值:1),校验不通过,不进行保存
					    			top.$.jBox.info("此游客信息不可修改", "警告");
					    			checkFlag=true;
					    	        return false;
					    		}else{ //校验通过,进行游客信息的保存
					    			 $.ajax({                        
					    					type: "post",   
					    					url:"${ctx}/visa/order/saveOrUpdateTraveler?orderId="+orderId,                 
					    					data:  data,// {data:data,data2:data2},// 你的form  
					    					error: function(request) {                     
					    						$.jBox.tip("保存失败！", "error"); 
					    					},                 
					    					success: function(data) {  
					    						if("false"==data){
					    							$.jBox.tip("更新预计回团时间失败！", "error"); 
					    							return false;
					    						}
					    						$.ajax({   
					    							type: "post",   // visaOrderPayLog/manage
					    							url:"${ctx}/visaOrderPayLog/manage/showsss",                 
					    							data:   {data2:data2,payPriceSerialNum:payPriceSerialNum,orderId:orderId},// 你的form  
					    							error: function(request) {                     
					    								$.jBox.tip("保存失败！", "error"); 
					    							},success: function(data) {                     
					    								$.jBox.tip("保存成功！", "success"); 
					    							}
					    					});
					    					}             
					    				}); 
					    		}
					    	}else{ //环球行用户走原有逻辑
					    		$.ajax({                        
									type: "post",   
									url:"${ctx}/visa/order/saveOrUpdateTraveler?orderId="+orderId,                 
									data:  data,// {data:data,data2:data2},// 你的form  
									error: function(request) {                     
										$.jBox.tip("保存失败！", "error"); 
									},                 
									success: function(data) {  
										if("false"==data){
											$.jBox.tip("更新预计回团时间失败！", "error"); 
											return false;
										}
										$.ajax({   
											type: "post",   // visaOrderPayLog/manage
											url:"${ctx}/visaOrderPayLog/manage/showsss",                 
											data:   {data2:data2,payPriceSerialNum:payPriceSerialNum,orderId:orderId},// 你的form  
											error: function(request) {                     
												$.jBox.tip("保存失败！", "error"); 
											},success: function(data) {                     
												$.jBox.tip("保存成功！", "success"); 
											}
									});
									}             
								});
					    	}
					    }
					});
		      //0318新增是否允许修改销售签证订单下的游客信息--e-----------------------------------------------------//
			/* $.ajax({                        
				type: "post",   
				url:"${ctx}/visa/order/saveOrUpdateTraveler?orderId="+orderId,                 
				data:  data,// {data:data,data2:data2},// 你的form  
				error: function(request) {                     
					$.jBox.tip("保存失败！", "error"); 
				},                 
				success: function(data) {  
					if("false"==data){
						$.jBox.tip("更新预计回团时间失败！", "error"); 
						return false;
					}
					$.ajax({   
						type: "post",   // visaOrderPayLog/manage
						url:"${ctx}/visaOrderPayLog/manage/showsss",                 
						data:   {data2:data2,payPriceSerialNum:payPriceSerialNum,orderId:orderId},// 你的form  
						error: function(request) {                     
							$.jBox.tip("保存失败！", "error"); 
						},success: function(data) {                     
							$.jBox.tip("保存成功！", "success"); 
						}
				});
				}             
			}); */
			
			//---如果游客为有借款(借款状态为审批中),有送签(签证状态为送签),则将游客的基本信息还原成修改前的值--s--主要是从用户体验角度而言//
			    if(checkFlag){ //游客基本信息不能修改时,该校验标志会为true
			      var beforeModifiedName=$(obj).parent().parent().find("[name='oldName']").val();//修改前游客的姓名
			      $(obj).parent().parent().find("[name='name']").val(beforeModifiedName);//还原成修改前的游客姓名
			      //div收缩后,游客标题后的游客姓名也要还原成修改前的值
			      $(obj).parent().parent().find('.tourist-t-off').find("[name='tName']").text(beforeModifiedName);
			      
			      var beforeModifiedNameSpell=$(obj).parent().parent().find("[name='oldNameSpell']").val();//修改前的性别
			      $(obj).parent().parent().find("[name='nameSpell']").val(beforeModifiedNameSpell);//还原成修改前的姓名拼音
			      
			      var beforeModifiedSex=$(obj).parent().parent().find("[name='oldSex']").val();//修改前性别
			      $(obj).parent().parent().find("[name='sex'] option").each(function(i,n){ //还原成修改前的选中的性别
			    	 if($(n).val()==beforeModifiedSex){
			    		 $(n).attr("selected","selected");
			    	 }  
			      });
			      
			      var beforeModifiedBirthday=$(obj).parent().parent().find("[name='oldBirthday']").val();//修改前出生日期
			      $(obj).parent().parent().find("[name='birthDay']").val(beforeModifiedBirthday);//还原成修改前出生日期
			      
			      var beforeModifiedTel=$(obj).parent().parent().find("[name='oldTelephone']").val();//修改前联系电话
			      $(obj).parent().parent().find("[name='telephone']").val(beforeModifiedTel);//还原成修改前联系电话
			      
			      var beforeModifiedPassportCode=$(obj).parent().parent().find("[name='oldPassportCode']").val();//修改前护照号
			      $(obj).parent().parent().find("[name='passportCode']").val(beforeModifiedPassportCode);//还原成修改前护照号
			      
			      var beforeModifiedIssuePlace=$(obj).parent().parent().find("[name='oldIssuePlace']").val();//修改前护照签发日期
			      $(obj).parent().parent().find("[name='issuePlace']").val(beforeModifiedIssuePlace);//还原成修改前护照签发日期
			      
			      var beforeModifiedPassportValidity=$(obj).parent().parent().find("[name='oldPassportValidity']").val();//修改前的护照有效期
			      $(obj).parent().parent().find("[name='passportValidity']").val(beforeModifiedPassportValidity);//还原成修改前的护照有效期
			      
			      var beforeModifiedPassortType=$(obj).parent().parent().find("[name='oldPassportType']").val();
			      $(obj).parent().parent().find("[name='passportType'] option").each(function(i,n){
			    	  if($(n).val()==beforeModifiedPassortType){
			    		  $(n).attr("selected","selected");
			    	  }
			      });
			     //游客结算价要还原-解决bug#14200 
			      var beforeInputClearPrice=$(obj).parent().parent().find("[name='hidden4InputClearPrice']").text();
			      $(obj).parent().parent().find("[name='inputClearPrice']").val(beforeInputClearPrice);
			      var temp=$(obj).parent().parent().find("[name='inputClearPrice']");
			      changeClearPriceSum(temp[0]);//订单总结算价还原
			    }
			//---如果游客为有借款(借款状态为审批中),有送签(签证状态为送签),则将游客的基本信息还原成修改前的值--e--主要是从用户体验角度而言//
			//--0318---给游客基本信息添加readonly,disabled属性--s//
			$(obj).parent().parent().find("[name='name']").attr("readonly","readonly");//姓名
			$(obj).parent().parent().find("[name='nameSpell']").attr("readonly","readonly");//拼音
			$(obj).parent().parent().find("[name='sex']").attr("disabled","disabled");//性别
			$(obj).parent().parent().find("[name='birthDay']").attr("disabled","disabled");//出生日期
			$(obj).parent().parent().find("[name='telephone']").attr("readonly","readonly");//联系电话
			$(obj).parent().parent().find("[name='passportCode']").attr("readonly","readonly");//护照号
			$(obj).parent().parent().find("[name='issuePlace']").attr("disabled","disabled");//护照签发日期
			$(obj).parent().parent().find("[name='passportValidity']").attr("disabled","disabled");//护照有效期
			$(obj).parent().parent().find("[name='passportType']").attr("disabled","disabled");//护照类型
			
			if($(obj).parent().parent().find("[name='issuePlace1']")){
				$(obj).parent().parent().find("[name='issuePlace1']").attr("readonly","readonly");
			}
			//--0318---给游客基本信息添加readonly,disabled属性--//
		    $(obj).text("修改");
		  //---0211需求,新增预计回团时间,限定为批发商星徽四海-s-已保存时的显示样式--//
		  //同时更新div中的回团时间的值
			 $(obj).parent().parent().parent().find("input[name='forecastBackDate']").next().text($(obj).parent().parent().parent().find("input[name='forecastBackDate']").val());
			 $(obj).parent().parent().parent().find("input[name='forecastBackDate']").css("display","none");
			 $(obj).parent().parent().parent().find("input[name='forecastBackDate']").next().css("display","inline-block");
			 
		  //---0211需求,新增预计回团时间,限定为批发商星徽四海-e--//
		   
		    
			$(obj).parent().prev().hide();
			$(obj).parent().parent().find('.tourist-t-off').css("display","inline");
			$(obj).parent().parent().find('.tourist-t-on').hide();
			$(obj).parent().parent().find('.add_seachcheck').addClass('boxCloseOnAdd')
			var money="";
			var array = $(obj).parent().parent().find("[name='inputClearPrice']");
			$.each(array,function(n,value) { 
				if(array.length != n+1)
					money=money+ value.id+value.value+"+";
					else
						money=money+value.id+value.value;
		           }); 
			
			// jsjyd
			$(obj).parent().parent().find("#jsjyd").html(money);
			
			
		}else{
			$.jBox.tip("请仔细检查表单信息！", "error");
		}
		}else if($(obj).text()=="修改"){//按钮的字面值为"修改"
			
			//--0318---给游客基本信息去除readonly,disabled属性--s//
			if(!($(obj).parent().parent().find("[name='flag4HQX']").attr("rState")=='true')){//环球行批发商,游客姓名是否只读是有条件的,非只读的才进行去除样式变成可修改
			$(obj).parent().parent().find("[name='name']").removeAttr("readonly","readonly");//姓名
			}
			$(obj).parent().parent().find("[name='nameSpell']").removeAttr("readonly","readonly");//拼音
			$(obj).parent().parent().find("[name='sex']").removeAttr("disabled");//性别
			$(obj).parent().parent().find("[name='birthDay']").removeAttr("disabled");//出生日期
			$(obj).parent().parent().find("[name='telephone']").removeAttr("readonly");//联系电话
			$(obj).parent().parent().find("[name='passportCode']").removeAttr("readonly");//护照号
			$(obj).parent().parent().find("[name='issuePlace']").removeAttr("disabled");//护照签发日期
			$(obj).parent().parent().find("[name='passportValidity']").removeAttr("disabled");//护照有效期
			$(obj).parent().parent().find("[name='passportType']").removeAttr("disabled");//护照类型
			//--0318---给游客基本信息去除readonly,disabled属性--e//
			
			//544
			if($(obj).parent().parent().find("[name='issuePlace1']")){
				$(obj).parent().parent().find("[name='issuePlace1']").removeAttr("readonly","readonly");
			}
			
			$(obj).text("保存");
			//---0211需求,新增预计回团时间,限定为批发商星徽四海-s-未保存时的显示--//
			 $(obj).parent().parent().parent().find("input[name='forecastBackDate']").css("display","inline-block");
			 $(obj).parent().parent().parent().find("input[name='forecastBackDate']").next().css("display","none");
		  //---0211需求,新增预计回团时间,限定为批发商星徽四海-e--//
			$(obj).parent().prev().show();
			$(obj).parent().parent().find('.tourist-t-off').css("display","none");
			$(obj).parent().parent().find('.tourist-t-on').show();
			$(obj).parent().parent().find('.add_seachcheck').removeClass('boxCloseOnAdd')
			}
	//添加费用
	var addcost=$(obj).parent().parent().find(".btn-addBlue");
	if($(addcost).css("display")=="none") {
			$(addcost).show();
				}else{
			$(addcost).hide();
				}
	//删除			
	var deleltecost=$(obj).parent().parent().find("a[name='deleltecost']");
	if($(deleltecost).css("display")=="none") {
			$(deleltecost).show();
				}else{
			$(deleltecost).hide();
				}
	//应用全部			
	var useall=$(obj).parent().parent().find(".yd-total a");
	if($(useall).css("display")=="none") {
			$(useall).show();
				}else{
			$(useall).hide();
				} 
	
	}


$(function(){
    //加载币种数据源
    
    currencyList = eval('${currencyListJsonArray}');
    //添加其他费用时触发
    var addcostindex = 0;
    $("#traveler").delegate("a[name='addcost']","click",function() {
        var $this = $(this);
        var $table = $this.next();
        var travelerIndex = $this.closest("form").find(
                ".travelerIndex");
        addcostindex++;
        //var _div = $('<div class="payfor-other cost costrmb"><input type="hidden" name="id">'
        		var _div = $('<div class="payfor-other cost costrmb">'
                + '<select name="currency" onchange="changeCostCurrency(this)">'
                + '${currencyList}'
                +'</select>'
                + '<input type="text" name="cosName" onfocus="payforotherIn(this)" maxlength="50" onblur="payforotherOut(this)" id="costname'
                + travelerIndex.text()
                + addcostindex
                + '" class="required ipt2" />'
                + '<span class="ipt-tips2" onclick="focusIpt(this)">费用名称</span>'
                + '<input type="text"  id="costvalue'
                + travelerIndex.text()
                + addcostindex
                + '" name="sum" style="" value="0"  maxlength="15" class="required number ipt3" onafterpaste="changeSum(this)" onkeyup="changeSum(this)">'
                + '<a name="deleltecost" class="btn-del1"></a>'
                + '</div>');
        $table.append(_div);
        changePayPriceByCostChange($this.closest("form"));
    });
	//recountIndexTraveler();
	//beforePrice();
});


function downloadDocs(docIds){
	window.open(encodeURI(encodeURI("${ctx}/sys/docinfo/zipdownload/" + docIds + "/download")));
}

function errorMessage(){
	$.jBox.tip("未上传收款凭证!", "error");
}






function updateAgentnew(obj) {
var flag ;
   
$("input[name=contactsName]").each(function(index,obj){

if(obj.value.length == 0)
	{	
		flag = false;
	}
})   
if(flag  == false)
{
	$.jBox.tip("渠道联系人不能为空!", "error");
	return false;
}


var  contactsTel_flag ;
$("input[name=contactsTel]").each(function(index,obj){

if(obj.value.length == 0)
	{	
		contactsTel_flag = false;
	}
}) 


if(contactsTel_flag  == false)
{
	$.jBox.tip("渠道联系人电话不能为空!", "error");
	return false;
}


	var orderId = $("[name='orderId']").val();
	
	var channel1 = ${agentInfo.id}
	
	var agentId;
	if(channel1 == -1)
	agentId =-1
	else
	agentId =$("#modifyAgentInfo").val().split(",")[0]
	
	var data = $('#orderpersonMesdtail').serialize().toString();
	var agent_name;
	if(agentId == -1)
	agent_name = $("input[name='agenet_Name']").val();
	else
	agent_name = $("#modifyAgentInfo").find("option:selected").text(); 
	$.ajax({
		   type: "POST",
		   url: g_context_url + "/visa/order/updateOrderContacts?uud"+new Date().getTime(),
		   cache: false,
		   data:{
			   orderId:orderId,
			   agentId:agentId,
			   agent_name:agent_name,
			   data:data
		   },
		   success: function(msg){
			   $.jBox.tip("保存成功！", "success");
			    window.location.reload();
		   }
	});
}

//上传文件时，点击后弹窗进行上传文件(多文件上传)
//方法参数：${ctx}、显示上传文件名称input的id、当前点击元素
function uploadFilesvisa(ctx, inputId, obj, isSimple) {
	var fls=flashChecker();
	var s="";
	if(fls.f) {
//		alert("您安装了flash,当前flash版本为: "+fls.v+".x");
	} else {
		alert("请您退出当前浏览器,并安装当前浏览器的flash player软件,然后才能上传文件"); 
		return;
	}
	
	//新建一个隐藏的div，用来保存文件上传后返回的数据
	if($(obj).parent().find(".uploadPath").length == 0)
		$(obj).parent().append('<div class="uploadPath" style="display: none" id="uploadPathDiv"></div>');
	
	$(obj).addClass("clickBtn");
	
	//默认为多文件上传
	if(isSimple == null) {
		isSimple = "false";
	}
	
	$.jBox("iframe:"+ ctx +"/MulUploadFile/uploadFilesPage?isSimple=" + isSimple, {
	//$.jBox("iframe:"+ ctx, {
	    title: "文件上传",
		width: 340,
 		height: 365,
 		buttons: {'完成上传':true},
 		persistent:true,
 		loaded: function (h) {},
 		submit: function (v, h, f) {
			$(h.find("iframe")[0].contentWindow.file_upload).next("p").find(".successUpload").click();
			//这里拼接本次上传文件的原名称
			var fileIDList = "";
			var fileNameList = "";
			var filePathList = "";
			//
			if($(obj).parent().find("[name='docID']").length != 0) {
				$(obj).parent().find("[name='docID']").each(function(index, obj) {
					if(null != isSimple && "false"!=isSimple) {
						fileIDList = $(obj).val();
					}else{
						fileIDList +=$(obj).val()+",";
					}
				});
			}
			if($(obj).parent().find("[name='docOriName']").length != 0) {
				$(obj).parent().find("[name='docOriName']").each(function(index, obj) {
					if(null != isSimple && "false"!=isSimple) {
						fileNameList = $(obj).val();
					}else{
						fileNameList +=$(obj).val()+";" ;
					}
				});
			}
			if($(obj).parent().find("[name='docPath']").length != 0) {
				$(obj).parent().find("[name='docPath']").each(function(index, obj) {
					if(null != isSimple && "false"!=isSimple) {
						filePathList = $(obj).val();
					}else{
						filePathList += $(obj).val()+";";
					}
				});
			}
			//在这里将原名称写入到指定id的input中
			//if(inputId)
			//	$("#" + inputId).val(fileNameList);
			//该函数各自业务jsp都写一个，里面的内容根据自身页面要求自我实现
			commenFunction(obj,fileIDList,fileNameList,filePathList);
			$("#uploadPathDiv").remove();
			$(".clickBtn",window.parent.document).removeClass("clickBtn");
			fileNameList = "";
 		}
	});
	$(".jbox-close").hide();
}

</script>


</head>

<body>


<input type="hidden" class="traveler" value="${visaOrder.id }" name="orderId" id="orderid"/>
<input type="hidden" value="${fns:getUser().company.uuid }" id="companyUuid" name="companyUuid">
<!-- c333, 为页面计算 结算价  和  成本价用 -->
<input type="hidden"  value="${totalMoney}" name="totalMoneyName" id="totalMoney4addTraveler">
<input type="hidden"  value="${costTotalMoney}" name="costTotalMoneyName" id="costTotalMoney4addTraveler">
<input type="hidden"  value="${currency.currencyMark}${visaOrder.proOriginVisaPay}" name="productCostName" id="productCost4addTraveler">

<page:applyDecorator name="show_head">
        <page:param name="desc">签证订单修改</page:param>
</page:applyDecorator>
<!-- 应收价格 -->
<input type="hidden" value="${visaProduct.visaPay}" name="settlementAdultPrice" id="crj">
<input type="hidden" value="${currency.id }" id="crbz" name="adultCurrencyId">
<input type="hidden" value="${currency.currencyMark }"  id="crbzm" name="adultCurrencyMark">
<input type="hidden" value="${currency.currencyName }"  id="crbmc" name="adultCurrencyName">
<input type="hidden" value="" id="singleDiffCurrencyId" name="singleDiffCurrencyId"> 
<!--添加游客模板开始-->
<!-- 游客模板 -->
<div id="travelerTemplate" style="display: none;">
<form name="travelerForm"  id="searchForm" >
<%--     <input type="hidden" name="travelerOrderId">
    <input type="hidden" value="<fmt:formatDate value="${productGroup.groupOpenDate}" pattern="yyyy-MM-dd"/>" name="groupOpenDate">
    <input type="hidden" value="${singleDiffCurrencyId}" name="singleDiffCurrencyId">
    <input type="hidden" name="travelerId">
 --%> 
   
    <!-- 把游客模板替换为新增时的模板，对应需求号  c333--> 
    <input type="hidden" name ="id" value=""  class="traveler" >
    <input type="hidden" class="traveler" value="${visaOrder.id }" name="orderId">
    <input type="hidden" class="traveler" value="6" name="orderType">
    
	<input type="hidden" name="travelerOrderId">
    <input type="hidden" value="<fmt:formatDate value="${productGroup.groupOpenDate}" pattern="yyyy-MM-dd"/>" name="groupOpenDate">
    <input type="hidden" value="${singleDiffCurrencyId}" name="singleDiffCurrencyId">
    <input type="hidden" name="travelerId">
    

	<div class="tourist">
		<div class="tourist-t">
			<a class="btn-del" style="cursor:pointer;" name="deleteTraveler" >删除</a>
			<input type="hidden" name ="id" value=""  class="traveler" >
			<span class="add_seachcheck" onclick="travelerBoxCloseOnAdd(this)"></span>
            <label class="ydLable">游客<em class="travelerIndex" style="vertical-align: baseline;"></em>:</label>
            <div class="tourist-t-off">
                <!-- 
                <span class="fr">结算价：<span name="jsPrice" class="ydFont2"></span></span>
                 -->
                 <span class="fr">应收价格：<span name="jsPrice" class="ydFont2" style="margin-right:10px;"></span>结算价：<span name="travelerClearPrice" class="ydFont2"></span></span>
                <span name="tName"></span>       
            </div>
			<div class="tourist-t-on">
		            <!--<label><input type="radio" class="traveler" name="personType" value="1" checked="checked"/>在职</label>
					<label><input type="radio" class="traveler" name="personType" value="2" />退休</label>
					<label><input type="radio" class="traveler" name="personType" value="3" />学生</label> -->
             </div>
		</div>
		
		<div class="tourist-con" flag="messageDiv">
			<!--游客信息左侧开始-->
			<div class="tourist-left">
            <div class="ydbz_tit ydbz_tit_child"><em class="ydExpand" onclick="boxCloseOnAdd(this)"></em>基本信息</div>
				<ul class="tourist-info1 clearfix"  flag="messageDiv">
	                <li>
	                	<label class="ydLable"><span class="xing">*</span>姓名：</label>
	                	<c:if test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}"> 
                  			<input type="text" maxlength="30" name="travelerName"  loginName="${fns:getUser().company.uuid }"  value="" class="traveler required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()">
                  		</c:if>
						<c:if test="${fns:getUser().company.uuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
		                	<input type="text" maxlength="30" name="travelerName" loginName="${fns:getUser().company.uuid }" class="traveler required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()">
						</c:if>
	                </li>
	                <li>
	                 	<label class="ydLable">英文／拼音：</label>
                        <input type="text" maxlength="30" name="travelerPinyin" class="traveler">
	               	</li>
	               	<li>
	                    <label class="ydLable"><span class="xing"></span>性别：</label>
                         <select name="travelerSex" class="selSex required">
                            <option value="1" selected="selected">男</option>
                            <option value="2" >女</option>
                        </select> 
	                </li>                        
                        <li>
                            <label class="ydLable">出生日期：</label>
                             <input type="text" name="birthDay" class="traveler traveler2 dateinput" onclick="WdatePicker()">
                           
                        </li>
                        <li>
                            <label class="ydLable"><span class="xing"></span>联系电话：</label>
                            <input type="text" name="telephone" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
                        </li>
                            <li>
							<label class="ydLable"><span class="xing"></span>护照类型：</label>
	                        <select name="passportType" class="selCountry">
	                            <c:forEach items="${passportTypeList}" var="passportType">
	                                <option value="${passportType.key}">${passportType.value}</option>
	                            </c:forEach>
	                        </select>
						</li> 
                        <li>
                             <!-- 
                            <label class="ydLable"><span class="xing">*</span>护照号：</label>
                            <input type="text" name="passportCode" class="traveler" maxlength="50"  >
                             -->
                            <!-- 去掉必填* 和 required  -->
                            <label class="ydLable"><span class="xing"></span>护照号：</label>
                            <input type="text" name="passportCode" class="traveler" maxlength="50"  >
                        </li>
                         <li>
                             
                             <!-- 
                            <label class="ydLable"><span class="xing">*</span>护照签发日期:</label>
                            <input type="text" name="issuePlace" class="traveler traveler2 dateinput required" >
                             -->
                             <!-- 去掉必填* 和 required  -->
                            <label class="ydLable"><span class="xing"></span>护照签发日期:</label>
                            <input type="text" name="issuePlace" class="traveler traveler2 dateinput" >
                        </li>
                        <li>
                        	<!--
                            <label class="ydLable"><span class="xing">*</span>有效期至：</label>
                            <input type="text" name="passportValidity" class="traveler traveler2 dateinput required" onclick="WdatePicker({minDate:getCurDate()})">
                             -->
                            <!-- 去掉必填* 和 required  -->
                            <label class="ydLable"><span class="xing"></span>有效期至：</label>
                            <input type="text" name="passportValidity" class="traveler traveler2 dateinput" onclick="WdatePicker({minDate:getCurDate()})">
                        </li>
	                	<c:if test="${fns:getUser().company.uuid eq '980e4c74b7684136afd89df7f89b2bee'}"> <li><label class="ydLable">签发地：</label><span class="fArial">
			<input type="text" maxlength="10" onblur="this.value=this.value.replace(/[^\u4e00-\u9fa5]+/g,'')" onafterpaste="this.value=this.value.replace(/[^\u4e00-\u9fa5]+/g,'')" name="issuePlace1" class="inputTxt" value="${traveler.issuePlace1 }"/></span></li></c:if>
                    </ul>
                    <div class="ydbz_tit ydbz_tit_child"><em class="ydExpand" onclick="boxCloseOnAdd(this)"></em>申请办签</div>
                    <ul flag="messageDiv">
                        <div class="ydbz_scleft">
                            <table class="table-visa">
                                <thead><tr>
                                    <th width="15%">申请国家</th><!-- 订单中添加的游客 -->
                                    <th width="15%">领区</th>
                                    <th width="15%">签证类别</th>
                                    <th width="10%">
                                        <c:if test="${fns:getUser().company.uuid eq '0e19ac500f78483d8a9f4bb768608629'}">
                                        <!-- 0211需求,新增预计回团时间,限定为批发商星徽四海-s,对于星徽四海预计出团时间需要加上必填标识 -->
                                    	<span style="color:red;">*</span>
                                    	<!-- 0211需求,新增预计回团时间,限定为批发商星徽四海-e -->
                                    	</c:if>
                                    	预计出团时间
                                    </th>
                                    <th width="15%">预计约签时间</th>
                                    <!-- 0211需求,新增预计回团时间,限定为批发商星徽四海-s -->
									   <c:if test="${fns:getUser().company.uuid eq '0e19ac500f78483d8a9f4bb768608629'}">
									   <th width="13%"><span style="color:red;">*</span>预计回团时间</th>
									   </c:if>
										 <!-- 0615需求,新增是否需要押金,是否需要上传资料限定为批发商大洋国旅 -->
										  <%--<c:if test="${fns:getUser().company.uuid eq '7a81a03577a811e5bc1e000c29cf2586'}">--%>
									<%--<th width="8%">是否需要押金</th>
									<th width="8%">是否上传资料</th>--%>
									<%--</c:if>--%>
									 <!-- 0615需求,新增是否需要押金,是否需要上传资料限定为批发商大洋国旅 -->
                                </tr></thead>
                                <tbody><tr>
                                    <td class="tl">${country.countryName_cn}</td>
                                    <td>
	                                    <c:if test="${not empty visaProduct.collarZoning }">
				                           ${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}
			                        	</c:if>
	                               </td>
                                    <td>
	                                    ${visaType.label}
                                    </td>
                                    <td><input type="text" onclick="WdatePicker()" name="forecastStartOut"  class="inputTxt dateinput"  id=""></td>
                                    <td><input type="text" onclick="WdatePicker()" name="forecastContract"  class="inputTxt dateinput"  id=""></td>
                                    <!-- 0211需求,新增预计回团时间,限定为批发商星徽四海-s-新增游客 -->
                                       <c:if test="${fns:getUser().company.uuid eq '0e19ac500f78483d8a9f4bb768608629'}">
                                       <td>
										  <input type="text" onclick="WdatePicker()" name="forecastBackDate"  class="inputTxt dateinput"  id="" value='<fmt:formatDate value="${traveler.visa.forecastBackDate}" pattern="yyyy-MM-dd"/>'/>
										  <input type="hidden" name="companyUuid" value="${fns:getUser().company.uuid}"/>
									   </td>
									   </c:if>
									 <!-- 0211需求,新增预计回团时间,限定为批发商星徽四海-s-新增游客 -->
									 
									 
									 <!-- 0615需求,新增是否需要押金,是否需要上传资料限定为批发商大洋国旅-->  
									  <%--<c:if test="${fns:getUser().company.uuid eq '7a81a03577a811e5bc1e000c29cf2586'}">
									  <td>
						
									 <label><input type="radio" style="width:13px" name="depositValue" value="1" checked="checked">是</label><label><input type="radio" style="width:13px;margin-left: 5px" name="depositValue" value="0">否</label>
									
									 </td>  
									  <td>
									 <label><input type="radio" name="datumValue" value="1" checked="checked" style="width: 13px">是</label><label><input type="radio" name="datumValue" value="0" style="width: 13px;margin-left: 5px">否</label>
							          </td> 
							          </c:if> --%>
							          <!-- 0615需求,新增是否需要押金,是否需要上传资料限定为批发商大洋国旅-->    
                                </tr></tbody>
                            </table>
                        </div>
                        <!-- 上传资料部分 -->

					  <div class="ydbz_tit ydbz_tit_child">上传资料</div>
		                <ul flag="messageDiv" class="ydbz_2uploadfile ydbz_scleft">
		                    <li class="seach25 seach33"><p>护照首页：</p>
			                    <input name="passportfile" type="text" style="display:none;" disabled="disabled">
			                    <input type="button" name="passport" class="btn btn-primary" value="上传" onclick="uploadFiles2('${ctx}','passportfile',this,'true');"/>
			                    <span class="fileLogo"></span>
		                    </li>
		                    <li class="seach25 seach33"><p>身份证正面：</p><input name="idcardfrontfile" type="text" style="display:none;" disabled="disabled"><input type="button" name="idcardfront" class="btn btn-primary" value="上传" onclick="uploadFiles2('${ctx}','idcardfrontfile',this,'true');"><span class="fileLogo"></span></li>
		                    <li class="seach25 seach33"><p>申请表格：</p><input name="entryformfile" type="text"  style="display:none;" disabled="disabled"><input type="button" name="entry_form" class="btn btn-primary" value="上传" onclick="uploadFiles2('${ctx}','entryformfile',this,'true');"><span class="fileLogo"></span></li>
		                    <p class="kong"></p>
		                    <li class="seach25 seach33"><p>电子照片：</p><input name="photofile" type="text"  style="display:none;" disabled="disabled"><input type="button" name="photo" class="btn btn-primary" value="上传" onclick="uploadFiles2('${ctx}','photofile',this,'true');"><span class="fileLogo"></span></li>
		                    <li class="seach25 seach33"><p>身份证反面：</p><input name="idcardbackfile" type="text"  style="display:none;" disabled="disabled"><input type="button" name="idcardback" class="btn btn-primary" value="上传" onclick="uploadFiles2('${ctx}','idcardbackfile',this,'true');"><span class="fileLogo"></span></li>
		                    <li class="seach25 seach33"><p>签证附件：</p><input name="visaannexfile" type="text"  style="display:none;" disabled="disabled"><input type="button" name="visa_annex" class="btn btn-primary" value="上传" onclick="uploadFiles2('${ctx}','visaannexfile',this,'false');"><span class="fileLogo"></span></li> 
		                    <p class="kong"></p>
		                    <li class="seach25 seach33"><p>户口本：</p><input name="familyRegisterfile" type="text"  style="display:none;" disabled="disabled"><input type="button" name="familyRegister" class="btn btn-primary" value="上传" onclick="uploadFiles2('${ctx}','familyRegisterfile',this,'true');"><span class="fileLogo"></span></li>
		                    <li class="seach25 seach33"><p>房产证：</p><input name="houseEvidencefile" type="text"  style="display:none;" disabled="disabled"><input type="button" name="houseEvidence" class="btn btn-primary" value="上传" onclick="uploadFiles2('${ctx}','houseEvidencefile',this,'true');"><span class="fileLogo"></span></li>
 							<li class="seach25 seach33"><p>其　它：</p><input name="otherfile" type="text"  style="display:none;" disabled="disabled"><input type="button" name="other" class="btn btn-primary" value="上传" onclick="uploadFiles2('${ctx}','otherfile',this,'true');"><span class="fileLogo"></span></li> 
		                </ul>
			   
			   
                <div class="ydbz_tit ydbz_tit_child">需提交办签资料</div>
                <ul class="seach25 seach100 ydbz_2uploadfile">
                                     <!--***************197-start -->
                                     <p>资料原件：</p>              
                                     <input type="checkbox" name="original_Project_Type" value="0" <c:if test="${fn:contains(originalProjectType,'0')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">护照</label></span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="1" <c:if test="${fn:contains(originalProjectType,'1')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">身份证</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="3" <c:if test="${fn:contains(originalProjectType,'3')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">电子照片</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="4" <c:if test="${fn:contains(originalProjectType,'4')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">申请表格</label>
                                     </span>
                                      <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="5" <c:if test="${fn:contains(originalProjectType,'5')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">户口本</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="6" <c:if test="${fn:contains(originalProjectType,'6')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">房产证</label>
                                     </span>
                                     <span class="seach_check">
                                      <input type="checkbox" name="original_Project_Type" value="2" <c:if test="${fn:contains(originalProjectType,'2')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">其他</label><input type="text" name="original_Project_Name" value="${originalProjectName}" class="input-mini"  disabled="disabled" ></span><br/>
                                     <p>复印件：</p> 
                                     <input type="checkbox" name="copy_Project_Type" value="3"   <c:if test="${fn:contains(copyProjectType,'3')}">checked="checked"</c:if> id=""   disabled="disabled"  ><label for="">护照</label></span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="4"   <c:if test="${fn:contains(copyProjectType,'4')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">身份证</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="5"   <c:if test="${fn:contains(copyProjectType,'5')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">电子照片</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="6"   <c:if test="${fn:contains(copyProjectType,'6')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">申请表格</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="0"   <c:if test="${fn:contains(copyProjectType,'0')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">户口本</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="1"   <c:if test="${fn:contains(copyProjectType,'1')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">房产证</label>
                                     </span>
                                     <span class="seach_check">
                                      <input type="checkbox" name="copy_Project_Type"  value="2" <c:if test="${fn:contains(copyProjectType,'2')}">checked="checked"</c:if> id=""  disabled="disabled" ><label for="">其他</label><input type="text" name="copy_Project_Name" value="${copyProjectName}" class="input-mini"  disabled="disabled" ></span></ul>
                                      <!--***************197-end -->
                </ul>
               
                <div class="ydbz_tit ydbz_tit_child"></em>备注: <textarea class="textarea_long"  name="remark"></textarea></div>
                
			</div>
            <!--游客信息左侧结束-->
            <!--游客信息右侧开始-->
			<div class="tourist-right">
			  <div class="bj-info">
                    <div class="ydbz_tit ydbz_tit_child">报价</div>
			    <!-- C225将游客信息中的“应收价格”删除,将“成本价”改为“应收价格”,取签证产品发布时的“应收价格”,“订单总成本价”改为“应收总计”,取游客应收价格之和
			     	 changed by 2015-10-12
			    <div class="clearfix"> 
			        <ul class="tourist-info2">
			            <li><label class="ydLable2">应收价格：</label> ${currency.currencyMark }<span class="ydFont1"><fmt:formatNumber pattern="#.00" value="${visaPay}" /></span><input type="hidden" name="singleDiff"  class="traveler" value="0" > </li>
			        </ul>			      
			    </div>			    
			    <div class="yd-line"></div>
			    -->
                    <div class="clearfix">
                        <a name="addcost" class="btn-addBlue">添加其他费用</a>
                        <div class="payfor-otherDiv"> 
                        </div>
                    </div>
                 </div>
			    <div class="yd-line"></div>
                <div class="yd-total clearfix"><a name="bjyyqb" class="ydbz_x" onclick="useAllPrice(this)">报价应用全部</a></div>
                <div class="yd-total clearfix">
                  <div class="fr">
                    <!-- c333 显示应收价  -->
                    <label class="ydLable2">应收价格：</label><span name="innerJsPrice" class="ydFont2">${currency.currencyName }<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${visaOrder.proOriginVisaPay}" /></span>
                    <input type="hidden" name="srcPrice" class="traveler">
                    <input type="hidden" name="srcPriceCurrency" class="traveler">
                    <input type="hidden" name="jsPrice" class="traveler">
                    <input type="hidden" name="payPrice" class="traveler">
                  </div>
                </div>
                <div class="yd-total clearfix">
					<div class="fr">
					<!-- c333 显示结算价  -->
                  	<label class="ydLable2">结算价：</label><span name="clearPrice" class="ydFont2"></span>
                  	
                  	<input type="hidden" name="travelerClearPrice" class="traveler">
                  </div>
				</div>
				<!--     0820上  -->
			
				<div class="clearfix1">
						<div class="traveler-rebatesDiv" >
							<label class="ydLable2 ydColor1">预计个人返佣：</label>
							<select name="rebatesCurrency">
	               				 <c:forEach items="${currencyList4Rebates}" var="cu">
	                                <option value="${cu.id}">${cu.currencyName}</option>
	                            </c:forEach>
	               			</select>
	               			<input type="text"  maxlength="9" name="rebatesMoney" style="display: inline-block;width: 40px;"  onafterpaste="validNum(this)" onkeyup="validNum(this)">
						</div>
			     </div>
			
			<!--     0820上  	-->	
			<div >
	               	<input type="hidden"   maxlength="9" name="rebatesCurrency" class="ipt-rebates" value="33">
	               	<input type="hidden"  maxlength="9" name="rebatesMoney" class="ipt-rebates"  value="0">
			</div>	
			
				
            </div>
          </div>
          <!--保存、取消按钮开始-->
            <div class="rightBtn"><a class="btn" onclick="SavePeopleTableData4AddTraveler(this)"/>保存</a></div>
           <!--保存、取消按钮结束-->
           
        </div>
        
</form>
</div>

<shiro:hasPermission name="visaOrderForSale:agentinfo:visibility">
	<input type="hidden" value="1" id="agentinfo_visibility">
	<c:set var="agentinfo_visibility" value="1"></c:set>
</shiro:hasPermission>
<!--添加游客模板结束-->
<div class="mod_nav">订单 > 签证 > 订单修改</div>
	<div class="ydbzbox fs">
		<!--<div class="tr">
			<a href="${ctxStatic }/" class="dyzx-add">下载出团通知书</a>
			<a href="${ctxStatic }/" class="dyzx-add">下载确认单</a>
			
		</div>-->
		<div class="ydbz_tit">订单详情</div>
		<ul class="ydbz_info ydbz_infoli25">
			<li>
				<span>销售：</span>${visaOrder.salerName }
			</li>
			<li><span>下单时间：</span><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaOrder.createDate }"/></li>
			<li><span>团队类型：</span>
				<c:if test="${productOrder==null }">单办签</c:if>
				<c:if test="${productOrder!=null }">
					<c:if test="${productOrder.orderStatus==1 }">单团</c:if>
					<c:if test="${productOrder.orderStatus==2 }">散拼</c:if>
					<c:if test="${productOrder.orderStatus==3 }">游学</c:if>
					<c:if test="${productOrder.orderStatus==4 }">大客户</c:if>
					<c:if test="${productOrder.orderStatus==5 }">自由行</c:if>
					<c:if test="${productOrder.orderStatus==6 }">签证</c:if>
					<c:if test="${productOrder.orderStatus==7 }">机票</c:if>
				</c:if>
			</li>
			<li><span>收客人：</span>${visaOrder.createBy.name }</li>
			<li><span>订单编号：</span>${visaOrder.orderNo }</li>
			<!-- C460V3 所有批发商团号取产品团号 -->
			<%-- <li><span>团号：</span>${visaProduct.groupCode }</li> --%>
			<!-- C460V3 所有批发商团号取自产品团号 -->
			<!--  C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-s-订单->销售签证订单->修改页 -->
			<c:if test="${fns:getUser().company.uuid eq '7a816f5077a811e5bc1e000c29cf2586'}"><!-- 环球行 -->
		  	<li><span>订单团号：</span>${visaOrder.groupCode }</li>
			</c:if>
			<c:if test="${fns:getUser().company.uuid ne'7a816f5077a811e5bc1e000c29cf2586'}"><!-- 非环球行 -->
		  	<li><span>团号：</span>${visaProduct.groupCode }</li>
			</c:if>
            <!--  C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-e -->
			
			<c:if test="${productOrder!=null }">
			<li><span>参团订单编号：</span>${visaOrder.activityCode }</li>
			<li><span>参团团号：</span>${activityGroup.groupCode }</li>
			</c:if>
			<li><span>订单总额：</span>${totalMoney }</li>
			<li><span>订单状态：</span>
				<c:if test="${visaOrder.visaOrderStatus==0 }">未收款</c:if>
				<c:if test="${visaOrder.visaOrderStatus==1 }">已收款</c:if>
				<c:if test="${visaOrder.visaOrderStatus==2 }">已取消</c:if>
				<c:if test="${visaOrder.visaOrderStatus==100 }">订单创建中</c:if>
			</li>
			<li><span>操作人：</span>${visaProduct.createBy.name }</li>
			<li><span>办签人数：</span>${visaOrder.travelNum }人</li>
			<li>
				<span>下单人：</span>${visaOrder.createBy.name }
			</li>
		</ul>
		<p class="ydbz_mc"></p>
		<ul class="ydbz_info ydbz_infoli25">
			<li style="height:auto">
				<span style="vertical-align: top;">收据号：</span>
				<span><c:forEach items="${receiptList }" var="receipt" ><div><a onClick="viewdetail4Receipt('${receipt.uuid}','-2','${receipt.orderType}')">${receipt.invoiceNum }</a></div></c:forEach></span>
			</li>
			<li style="height:auto">
				<span style="vertical-align: top;">发票号：</span>
				<span><c:forEach items="${invoiceList }" var="invoice" ><div><a onClick="viewdetail('${invoice.uuid}','-2')">${invoice.invoiceNum }</a></div></c:forEach></span>
			</li>
		</ul>	
		<div class="ydbz_tit">产品信息</div>
               <div class="orderdetails2">
                  <p class="ydbz_mc">${visaProduct.productName }</p>
             	  <ul class="ydbz_info">
                    <li><span>产品编号：</span>${visaProduct.productCode }</li>
                    <li><span>签证国家：</span>${country.countryName_cn }</li>
                    <li><span>签证类别：</span>${visaType.label }</li>
                    <li><span>领区：</span>	<c:if test="${not empty visaProduct.collarZoning }">
				                           ${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}
			                        	</c:if></li>
                    <c:if test="${visaCostPriceFlag eq 1}">
                     <li><span>成本价格：</span>${currency.currencyMark }<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${visaProduct.visaPrice}" />/人</li>
                     </c:if>
                    <li><span>应收价格：</span>${currency.currencyMark }<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${visaOrder.proOriginVisaPay}" />/人</li>
	    			<li><span>创建时间：</span><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaProduct.createDate }"/></li>
                  	<li style="display:none">
		                <span>办签人数：</span>
		                <input type="text" id="orderPersonNumAdult" value="${visaOrder.travelNum }"> 人
		            </li>
		            <li style="display: none;">
		                <input type="text" class="required" value="${visaOrder.travelNum }" id="orderPersonelNum">
		            </li>
                  </ul>
                </div>
			<div class="ydbz_tit">预订人信息</div>
			<div flag="messageDiv">
<form id="orderpersonMesdtail">
	   <p class="ydbz_qdmc">预订渠道：
	   		<c:choose>
	   			<c:when test="${agentInfo.id == -1 }" >
							   <c:if test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}"> 
							       直客
							   </c:if>
							   <c:if test="${fns:getUser().company.uuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
							    非签约渠道 
							   </c:if>
	   			<br>
	   			渠道名称：<input name="agenet_Name" value="${visaOrder.agentinfoName}" style="border:1px solid #ccc;box-shadow:inset 0 1px 1px rgba(0,0,0,0.075);">  
	   			</c:when>
	   			<c:otherwise>	   				
	   				<select id="modifyAgentInfo" onchange="getAllContactsByAgentId(this)">	   						   				
						<c:forEach items="${agentList }" var="agentinfo">
						
							<option value="${agentinfo.id},${agentinfo.agentName},${agentinfo.agentContact},
							${agentinfo.agentContactMobile},${agentinfo.agentTel},
							${agentinfo.agentAddress},${agentinfo.agentContactFax},
							${agentinfo.agentContactQQ},${agentinfo.agentContactEmail},
							${agentinfo.agentPostcode} "
							
							${agentinfo.agentName eq agentInfo.agentName?'selected':'' }  >
							${agentinfo.agentName}
							</option>
						</c:forEach>
					</select>
	   			</c:otherwise>
	   		</c:choose>
	   </p>
	   
	   <div id="ordercontact" <c:if test="${fns:getUser().id ne visaOrder.createBy.id and fns:getUser().id ne visaOrder.salerId and (empty agentinfo_visibility or agentinfo_visibility ne 1) }">style="visibility:hidden;"</c:if>>	    
			<c:choose>
	   			<c:when test="${agentInfo.id == -1 }" >
	   				<c:forEach items="${orderContactsSrc }" var="orderContact" varStatus="s1">
	   				<ul class="ydbz_qd" name="orderpersonMes" varStatus="s2">
		   				<li>
							<label><span class="xing">*</span>渠道联系人<span name="contactNo">1</span>：</label>
							<input maxlength="45" type="text" name="contactsName" value="${orderContact.contactsName}" class="required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()"/>
						</li>
						<li class="ydbz_qd_lilong">
							<label><span class="xing">*</span>渠道联系人电话：</label>
							<input maxlength="20" type="text"  name="contactsTel" id="contactsTel${s2.count}" value="${orderContact.contactsTel}" class="required" onkeyup="this.value=this.value.replace(/[^\\d\+\\-]/g,\\)"  onafterpaste="this.value=this.value.replace(/[^\\d\+\\-]/g,\\)"/><div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div>
							<c:if test="${allowAddAgentInfo == 1 and s1.count == 1 }">
				           		<span class="ydbz_x yd1AddPeople" onclick="addAgentContactNew(this)">添加联系人</span>
			           		</c:if>
			           		<c:if test="${s1.count != 1 }">
			           			<span class="ydbz_x gray" onclick="yd1DelPeople(this)">删除联系人</span>
			           		</c:if>
		           		</li>
						<li flag="messageDiv" style="display:none" class="ydbz_qd_close">
							<ul>
								<li hidden><label>id：</label><input maxlength="200" type="text" name="contactsId" id="contactsId${s2.count}" value="${orderContact.id}"/></li>
								<li><label>固定电话：</label><input maxlength="20" type="text" name="contactsTixedTel" id="contactsTixedTel${s2.count}" value="${orderContact.contactsTixedTel}" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/></li>
								<li><label>渠道地址：</label><input maxlength="200" type="text" name="contactsAddress" id="contactsAddress${s2.count}" value="${address}"/></li>
						        <li><label>传真：</label><input maxlength="20" type="text" name="contactsFax" id="contactsFax${s2.count}" value="${orderContact.contactsFax}"/></li>
						        <li><label>QQ：</label><input maxlength="20" type="text" name="contactsQQ" id="contactsQQ${s2.count}" value="${orderContact.contactsQQ}"/></li>
						        <li><label>Email：</label><input maxlength="50" type="text" name="contactsEmail" id="contactsEmail${s2.count}" value="${orderContact.contactsEmail}"/></li>
						        <li><label>渠道邮编：</label><input maxlength="20" type="text" name="contactsZipCode" id="contactsZipCode${s2.count}" value="${orderContact.contactsZipCode}"/></li>
						        <li><label>其他：</label><input maxlength="200" type="text" name="remark" id="remark${s2.count}" value="${orderContact.remark}"/></li>
					        </ul>
				        </li>
			        </ul>
			        </c:forEach>
	   			</c:when>
	   			<c:otherwise>
		     		<c:set var="orderContact" value="${orderContacts[0] }"></c:set>
			         <ul class="ydbz_qd min-height" name="orderpersonMes" varStatus="s2">
	 					<li>
							<label><span class="xing">*</span>渠道联系人<span name="contactNo">1</span>：</label>
							
								<c:if test="${agentInfo.id == -1}">
							<input maxlength="45" name="fqy" style="border-radius:4px;border:1px solid #ccc;" value="${orderContact.contactsName}" />
							</c:if>
							<c:if test="${agentInfo.id != -1}">
									<span name="channelConcat"></span>
							</c:if>
							
						</li>
						<li class="ydbz_qd_lilong">
							<label><span class="xing">*</span>渠道联系人电话：</label>
							<input maxlength="20" type="text" name="contactsTel" id="contactsTel${s2.count}" value="${orderContact.contactsTel}" class="required" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/><div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div>
				           	<c:if test="${allowAddAgentInfo == 1}">
				           		 	<span class="ydbz_x yd1AddPeople" name="addContactButton" onclick="addAgentContactNew(this)">添加联系人</span>
			           		</c:if>
				           	
				          
		           		</li>
						<li flag="messageDiv" style="display:none" class="ydbz_qd_close">
							<ul>
								<li hidden><label>id：</label><input maxlength="200" type="text" name="contactsId" id="contactsId${s2.count}" value="${orderContact.id}"/></li>
								<li><label>固定电话：</label><input maxlength="20" type="text" name="contactsTixedTel" id="contactsTixedTel${s2.count}" value="${orderContact.contactsTixedTel}" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/></li>
								<li><label>渠道地址：</label><input maxlength="200" type="text" name="contactsAddress" id="contactsAddress${s2.count}" value="${orderContact.contactsAddress}"/></li>
						        <li><label>传真：</label><input maxlength="20" type="text" name="contactsFax" id="contactsFax${s2.count}" value="${orderContact.contactsFax}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></li>
						        <li><label>QQ：</label><input maxlength="20" type="text" name="contactsQQ" id="contactsQQ${s2.count}" value="${orderContact.contactsQQ}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></li>
						        <li><label>Email：</label><input maxlength="50" type="text" name="contactsEmail" id="contactsEmail${s2.count}" value="${orderContact.contactsEmail}"/></li>
						        <li><label>渠道邮编：</label><input maxlength="20" type="text" name="contactsZipCode" id="contactsZipCode${s2.count}" value="${orderContact.contactsZipCode}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/></li>
						        <li><label>其他：</label><input maxlength="200" type="text" name="remark" id="remark${s2.count}" value="${orderContact.remark}"/></li>
					        </ul>
				        </li>
				     </ul>
				      
				</c:otherwise>
			
			</c:choose>
	    </div>
	    <div class="rightBtn">
	     <a class="btn" onclick="updateAgentnew(this);">保存</a>
	    </div>
</form>
	    <!-- 112需求新增修改特殊需求的功能 -->
	    <!-- S 特殊需求112 -->
                    <div id="manageOrder_m">
                        <div id="contact">
                           <div class="ydbz_tit">特殊需求</div>
                            <div class="ydbz2_lxr" flag="messageDiv">
                                <form class="contactTable">
                                   <div class="textarea pr wpr20">
                                    
                                    <label style="vertical-align:top">特殊需求：</label><input type="hidden" name ="id" value="">
                                    <textarea name="remark" style="margin: 0px 0px 10px; max-width: 1265px; max-height: 436px;" flag="istips" class="textarea_long" maxlength="500" rows="3" cols="50" onkeyup="this.value=this.value.replaceSpecialChars4SpecialRemark()" onafterpaste="this.value=this.value.replaceSpecialChars4SpecialRemark()" id="specialremark">
                                     	${visaOrder.remark}
                                    </textarea>
                                    <c:if test="${empty visaOrder.remark}">
                                    	<span id="promptSpan" class="ipt-tips" style="text-indent:1cm;">最多输入500字</span> 
                                    </c:if>   
                                	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="rightBtn"><a class="btn" onclick="doUpdateVisaOrderRemark(this,'${visaOrder.id}')"/>保存</a></span>
                                    </div>
                                </form>
                            </div>
                        </div>
                        
                    </div>
                    
       <!--E 112-->


		</div>
		<div  id="manageOrder_new" >
			<div class="ydbz_tit orderdetails_titpr">游客信息<a class="ydbz_x" href="${ctx}/visa/order/downloadTraveler?visaOrderId=${visaOrder.id}&agentId=${agentInfo.id }&groupCode=${visaOrder.groupCode }" >下载游客信息</a></div>
			<!--<div class="warningtravelerNum">暂无游客信息</div>-->
			<div id="traveler">
			<c:forEach items="${travelers }" var="traveler" varStatus="status">
				<form class="travelerTable" name="travelerForm">
				<input type="hidden" class="traveler" value="${traveler.id }" name="id">
				
				<!-- 游客返佣先关信息 -->
				<input type="hidden" class="traveler" value="${traveler.rebatesMoneySerialNum}" name="rebatesMoneySerialNum">
		
				
				
				<input id="payPriceSerialNum" type="hidden" class="traveler" value="${traveler.payPriceSerialNum }" name="payPriceSerialNum">
	            <div class="tourist">
	              <div class="tourist-t"> 
				    <span class="add_seachcheck" onclick="travelerBoxCloseOnAdd(this)"></span>
	                <label class="ydLable">游客<em class="travelerIndex" style="vertical-align: baseline;"></em>:</label>
	                <div class="tourist-t-off"> 
	                    <!-- 处理bug	12246-s -->
	                   <span class="fr">结算价：
	                         <span id="jsjyd" class="ydFont2">
	                         ${currency.currencyMark}<!-- 币种符号 -->
	                         <c:forEach items="${traveler.currencies }" var="currency" varStatus="status">
								${currency.convertCash}<!-- 结算金额 --> 
							</c:forEach>   
	                     </span>
	                   </span> 
	                    <!-- 处理bug	12246-e -->
	                   <span name="tName">${traveler.name }</span>
	                </div>
	                <div class="tourist-t-on"> 
	                </div>
	              </div>
	              <div class="tourist-con" flag="messageDiv"> 
	                <!--游客信息左侧开始-->
	                <div class="tourist-left"><!-- 环球行用户,其销售签证订单下游客基本信息不受0318需求的可修改配置的影响,保持当前现有逻辑 -->
	                <c:choose>
	                <c:when test="${companyId == 68 }">
	                  <c:choose>
                        <c:when test="${traveler.borrowMoney!=''&&traveler.borrowMoney!=null}">
                           <div class="ydbz_tit ydbz_tit_child"><em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>基本信息</div>
							<ul class="tourist-info1 clearfix" flag="messageDiv">
							<input type="hidden" name="travelerId" value="${traveler.id}">
								<li><label class="ydLable">姓名：</label>${traveler.name }</li><!-- aaaa -->
								 <input type="hidden" maxlength="30" name="name" value="${traveler.name }">
								<li><label class="ydLable">英文／拼音：</label><span class="fArial">${traveler.nameSpell }</span></li>
								<input type="hidden" maxlength="30" name="nameSpell" value="${traveler.nameSpell }">
								<li><label class="ydLable">性别：</label><c:if test="${traveler.sex==1 }">男</c:if><c:if test="${traveler.sex==2 }">女</c:if></li>
								<input type="hidden" maxlength="30" name="sex" value="${traveler.sex }">
								<li><label class="ydLable">出生日期：</label><span class="fArial"><fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.birthDay }"/></span></li>
								<input type="hidden" maxlength="30" name="birthDay" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.birthDay }"/>">
								<li><label class="ydLable">联系电话：</label><span class="fArial">${traveler.telephone }</span></li>
								<input type="hidden" maxlength="30" name="telephone" value="${traveler.telephone }">
								<li><label class="ydLable">护照号：</label><span class="fArial">${traveler.passportCode }</span></li>
								<input type="hidden" maxlength="30" name="passportCode" value="${traveler.passportCode }">
								<li><label class="ydLable">护照签发日期：</label><span class="fArial"><fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.issuePlace }"/></span></li>
								<input type="hidden" maxlength="30" name="issuePlace"   value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.issuePlace }"/>" >
								<li><label class="ydLable">护照有效期：</label><span class="fArial"><fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.passportValidity }"/></span></li>
								<input type="hidden" maxlength="30" name="passportValidity"   value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.passportValidity }"/>" >
							<!--	<li><label class="ydLable">身份证号：</label><span class="fArial">${traveler.idCard }</span></li>--> 
                                <li><label class="ydLable">护照类型：</label><c:if test="${traveler.passportType==1 }">因公护照</c:if><c:if test="${traveler.passportType==2 }">因私护照</c:if></li>
								<c:if test="${traveler.passportType==1 }"> 
								<input type="hidden" maxlength="30" name="passportType" value="1">
								</c:if>
								<c:if test="${traveler.passportType==2 }"> 
								<input type="hidden" maxlength="30" name="passportType" value="2">
								</c:if>
							</ul>	    
	   </c:when>
	   	<c:otherwise>
			<div class="ydbz_tit ydbz_tit_child"><em class="ydExpand" onclick="boxCloseOnAdd(this)"></em>基本信息</div>
	                  <ul class="tourist-info1 clearfix" flag="messageDiv">
	                  <input type="hidden" name="travelerId" value="${traveler.id}">
	                    <li>
	                      <!--rState:存放环球行批发商,姓名为是否为只读  -->
	                      <label class="ydLable" name="flag4HQX" rState="${traveler.visa.visaStauts != -1 && traveler.visa.visaStauts != 0 }">姓名：</label><!-- guanliyuan dddd -->
	                      <input type="text" maxlength="30" name="name" value="${traveler.name }" <c:if test="${traveler.visa.visaStauts != -1 && traveler.visa.visaStauts != 0 }">readonly="readonly"</c:if> class="traveler" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()">
	                    </li>
	                    <li>
	                      <label class="ydLable">英文／拼音：</label>
	                      <input type="text" maxlength="30" name="nameSpell" value="${traveler.nameSpell }" class="traveler" >
	                    </li>
	                    <li>
	                      <label class="ydLable">性别：</label>
	                      <select name="sex" class="selSex" >
								<option value="1" <c:if test="${traveler.sex==1 }">selected="selected"</c:if> >男</option>
								<option value="2" <c:if test="${traveler.sex==2 }">selected="selected"</c:if> >女</option>
						  </select> 
	                    </li>
	                <!--    <li>
							<label class="ydLable">国籍：</label>
							
							<select name="nationality" class="selCountry" >
								<c:forEach items="${countrys }" var="con" varStatus="sta">
									<option value="${con.id }" <c:if test="${con.id==traveler.nationality }">selected="selected"</c:if> >${con.countryName_cn }</option>
								</c:forEach>
							</select>
						</li>  -->
	                    <li>
	                      <label class="ydLable">出生日期：</label>
	                      <input type="text" maxlength="" name="birthDay" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.birthDay }"/>" class="traveler dateinput" >
	                    </li>
	                    <li>
							<label class="ydLable">联系电话：</label>
							<input type="text" name="telephone" value="${traveler.telephone }" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
						</li>
						<li>
							<label class="ydLable">护照号：</label>
							<input type="text" name="passportCode" value="${traveler.passportCode }" class="traveler" maxlength="50"  >
						</li>
						<li>
							<label class="ydLable">护照发日期：</label>
							<input type="text" maxlength="" name="issuePlace"  onclick="WdatePicker()" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.issuePlace }"/>" class="traveler dateinput " >
						</li>
						<li>
							<label class="ydLable">护照有效期：</label>
							<input type="text" maxlength="" name="passportValidity"  onclick="WdatePicker()" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.passportValidity }"/>" class="traveler dateinput" >
						</li>
						<!--<li>
							<label class="ydLable"><span class="xing">*</span>身份证号：</label>
							<input type="text" name="idCard" value="${traveler.idCard }" readonly="readonly required" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')">
						</li>-->
						<li>
                            <label class="ydLable">护照类型：</label>
                            <select name="passportType" class="selCountry" >
                                    <option <c:if test="${traveler.passportType==1 }">selected="selected"</c:if> value="1">因公护照</option>
                                    <option <c:if test="${traveler.passportType==2 }">selected="selected"</c:if> value="2" >因私护照</option>
                            </select>
                        </li>
	                  </ul>	
			</c:otherwise>               
</c:choose>	
</c:when>
<c:when test="${companyId == 71 }">
	<div class="ydbz_tit ydbz_tit_child"><em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>基本信息</div>
		<ul class="tourist-info1 clearfix" flag="messageDiv">
		<input type="hidden" name="travelerId" value="${traveler.id}">
		<!-- 0318新增是否允许修改销售签证订单下的游客信息--s -->
			 <li>
				<c:if test="${isAllowModifyXSVisaOrder  eq '0' }"><!-- 修改销售签证订单游客信息:0-否,1-是 -->
				<label class="ydLable">姓名：</label>
				${traveler.name }
				</c:if>
				<c:if test="${isAllowModifyXSVisaOrder  eq '1' }"><!-- 修改销售签证订单游客信息:0-否,1-是 -->
				     <label class="ydLable"><span class="xing">*</span>姓名：</label><!-- bbbb -->
				     <c:if test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}"> 
					   <input type="text" maxlength="30" name="travelerName"  loginName="${fns:getUser().company.uuid }"  value="${traveler.name }" class="traveler required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()">
				     </c:if>
					 <c:if test="${fns:getUser().company.uuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
					   <input type="text" maxlength="30" name="travelerName" loginName="${fns:getUser().company.uuid }"   value="${traveler.name }" class="traveler required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()">
					 </c:if>
				</c:if>
		    </li>
			<input type="hidden" maxlength="30" name="oldName" value="${traveler.name }"> 
			 
			<li>
			<label class="ydLable">英文／拼音：</label>
				<c:if test="${isAllowModifyXSVisaOrder  eq '0' }">
				<span class="fArial">${traveler.nameSpell }</span>
				</c:if>
				<c:if test="${isAllowModifyXSVisaOrder  eq '1' }">
				<input type="text" maxlength="30" name="travelerPinyin" class="traveler">
				</c:if>
		   </li>
		   <input type="hidden" maxlength="30" name="oldNameSpell" value="${traveler.nameSpell }"> 
			
			<li>
				<c:if test="${isAllowModifyXSVisaOrder  eq '0' }">
				<label class="ydLable">性别：</label>
				<c:if test="${traveler.sex==1 }">男</c:if><c:if test="${traveler.sex==2 }">女</c:if>
				</c:if>
			   <c:if test="${isAllowModifyXSVisaOrder  eq '1' }">
			       <label class="ydLable"><span class="xing"></span>性别：</label>
		           <select name="travelerSex" class="selSex required">
		              <option value="1" <c:if test="${traveler.sex eq '1' }">selected</c:if>>男</option>
		              <option value="2" <c:if test="${traveler.sex eq '2' }">selected</c:if> >女</option>
		           </select> 
			   </c:if>
		   </li>
			 <input type="hidden" maxlength="30" name="oldSex" value="${traveler.sex }"> 
		<!--	<li><label class="ydLable">国籍：</label> 
			<c:forEach items="${countrys }" var="con" varStatus="sta">
				<c:if test="${con.id==traveler.nationality }">${con.countryName_cn }</c:if>
			</c:forEach>
			</li>  -->
			<li>
			  <label class="ydLable">出生日期：</label>
			  <c:if test="${isAllowModifyXSVisaOrder  eq '0' }">
			  <span class="fArial"><fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.birthDay }"/></span>
			  </c:if>
			  <c:if test="${isAllowModifyXSVisaOrder  eq '1' }">
			  <input type="text" name="birthDay" class="traveler traveler2 dateinput" onclick="WdatePicker()" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.birthDay }"/>">
			  </c:if>
			</li>
		    <input type="hidden" maxlength="30" name="oldBirthday" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.birthDay }"/>"> 
			
			<li>
			<label class="ydLable">联系电话：</label>
			<c:if test="${isAllowModifyXSVisaOrder  eq '0' }">
			<span class="fArial">${traveler.telephone }</span>
		    </c:if>
		    <c:if test="${isAllowModifyXSVisaOrder  eq '1' }">
		    <input value="${traveler.telephone }" type="text" name="telephone" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
		    </c:if>
			</li>
		    <input type="hidden" maxlength="30" name="oldTelephone" value="${traveler.telephone }"> 
			
			<li>
			<label class="ydLable">护照号：</label>
			<c:if test="${isAllowModifyXSVisaOrder  eq '0' }">
			<span class="fArial">${traveler.passportCode }</span>
			</c:if>
			<c:if test="${isAllowModifyXSVisaOrder  eq '1' }">
			 <input type="text" name="passportCode" class="traveler" maxlength="50" value="${traveler.passportCode }" >
			</c:if>
			</li>
			<input type="hidden" maxlength="30" name="oldPassportCode" value="${traveler.passportCode }"> 
			
			<li><label class="ydLable">护照签发日期：</label>
			<c:if test="${isAllowModifyXSVisaOrder  eq '0' }">
			<span class="fArial"><fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.issuePlace }"/></span>
			</c:if>
			<c:if test="${isAllowModifyXSVisaOrder  eq '1' }">
			 <input type="text" name="issuePlace" class="traveler traveler2 dateinput" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.issuePlace }"/>">
			</c:if>
			</li>
			 <input type="hidden" maxlength="30" name="oldIssuePlace"   value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.issuePlace }"/>" > 
			
			<li><label class="ydLable">护照有效期：</label>
			<c:if test="${isAllowModifyXSVisaOrder  eq '0' }">
			<span class="fArial"><fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.passportValidity }"/></span>
			</c:if>
			<c:if test="${isAllowModifyXSVisaOrder  eq '1' }">
			<input type="text" name="passportValidity" class="traveler traveler2 dateinput" onclick="WdatePicker({minDate:getCurDate()})" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.passportValidity }"/>">
			</c:if>
			</li>
			<input type="hidden" maxlength="30" name="oldPassportValidity"   value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.passportValidity }"/>" > 
		<!--	<li><label class="ydLable">身份证号：</label><span class="fArial">${traveler.idCard }</span></li>--> 
           <li><label class="ydLable">护照类型：</label>
	        <c:if test="${isAllowModifyXSVisaOrder  eq '0' }">
	        <c:if test="${traveler.passportType==1 }">因公护照</c:if><c:if test="${traveler.passportType==2 }">因私护照</c:if>
	        </c:if>
	        <c:if test="${isAllowModifyXSVisaOrder  eq '1' }">
	          <select name="passportType" class="selCountry">
			    <c:forEach items="${passportTypeList}" var="passportType">
				<option value="${passportType.key}"<c:if test="${passportType.key eq  traveler.passportType}">selected</c:if>>${passportType.value}</option>
			    </c:forEach>
			</select>
	        </c:if>
        	</li>
		    <c:if test="${traveler.passportType==1 }"> 
			<input type="hidden" maxlength="30" name="oldPassportType" value="1">
			</c:if>
			<c:if test="${traveler.passportType==2 }"> 
			<input type="hidden" maxlength="30" name="oldPassportType" value="2">
			</c:if> 
			<!-- 0318新增是否允许修改销售签证订单下的游客信息--e -->
		</ul>
	</c:when>
<c:otherwise>
	<div class="ydbz_tit ydbz_tit_child"><em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>基本信息</div>
	<ul class="tourist-info1 clearfix" flag="messageDiv">
	   <input type="hidden" name="travelerId" value="${traveler.id}">
	   <!-- 0318新增是否允许修改销售签证订单下的游客信息--s -->
	    <!-- 游客姓名 -->
		<li>
		<c:if test="${isAllowModifyXSVisaOrder  eq '0' }"><!-- 修改销售签证订单游客信息:0-否,1-是 -->
		<label class="ydLable">姓名：</label><!-- cccc -->
		<input type="text" name="name" value="${traveler.name }" readonly></input>
		</c:if>
		<c:if test="${isAllowModifyXSVisaOrder  eq '1' }"><!-- 修改销售签证订单游客信息:0-否,1-是 -->
		     <label class="ydLable"><span class="xing">*</span>姓名：</label><!-- eeee -->
		     <c:if test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}"> 
			   <input type="text" maxlength="30" name="name"  loginName="${fns:getUser().company.uuid }"  value="${traveler.name }" class="traveler" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()">
		     </c:if>
			 <c:if test="${fns:getUser().company.uuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
			   <input type="text" maxlength="30" name="name" loginName="${fns:getUser().company.uuid }" <c:if test="${fns:isReviewing(traveler.id,traveler.orderId) eq 'yes'}">readonly</c:if> value="${traveler.name }" class="traveler" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()">
			 </c:if>
		</c:if>
		</li>
	    <input type="hidden" maxlength="30" name="oldName" value="${traveler.name }"><!-- 用于保存修改前的值 -->
		 <!-- 游客姓名英文拼音 -->
		<li>
		<label class="ydLable">英文／拼音：</label>
			<c:if test="${isAllowModifyXSVisaOrder  eq '0' }">
			<span class="fArial"><input type="text" name="nameSpell" value="${traveler.nameSpell }" readonly></input></span>
			</c:if>
			<c:if test="${isAllowModifyXSVisaOrder  eq '1' }">
			<input type="text" maxlength="30" name="nameSpell" class="traveler" value="${traveler.nameSpell }">
			</c:if>
		</li>
		<input type="hidden" maxlength="30" name="oldNameSpell" value="${traveler.nameSpell }"> 
		
		<!--游客性别  -->
		<li>
			<c:if test="${isAllowModifyXSVisaOrder  eq '0' }">
			<label class="ydLable">性别：</label>
			<c:if test="${traveler.sex==1 }"><input type="hidden" name="sex" value="1"></input>男</c:if>
				<c:if test="${traveler.sex==2 }"><input type="hidden" name="sex" value="2"></input>女</c:if>
			</c:if>
		   <c:if test="${isAllowModifyXSVisaOrder  eq '1' }">
		       <label class="ydLable"><span class="xing"></span>性别：</label>
	           <select name="sex" class="selSex required">
	              <option value="1" <c:if test="${traveler.sex eq '1' }">selected</c:if>>男</option>
	              <option value="2" <c:if test="${traveler.sex eq '2' }">selected</c:if> >女</option>
	           </select> 
		   </c:if>
		</li>
		<input type="hidden" maxlength="30" name="oldSex" value="${traveler.sex }"> 
	<!--	<li><label class="ydLable">国籍：</label> 
		<c:forEach items="${countrys }" var="con" varStatus="sta">
			<c:if test="${con.id==traveler.nationality }">${con.countryName_cn }</c:if>
		</c:forEach>
		</li>  -->
		<!-- 出生日期 -->
		<li>
		  <label class="ydLable">出生日期：</label>
		  <c:if test="${isAllowModifyXSVisaOrder  eq '0' }">
		  <span class="fArial"><input type="text" name="birthDay" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.birthDay }"/>" readonly></input></span>
		  </c:if>
		  <c:if test="${isAllowModifyXSVisaOrder  eq '1' }">
		  <input type="text" name="birthDay" class="traveler traveler2 dateinput" onclick="WdatePicker()" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.birthDay }"/>">
		  </c:if>
		</li>
		<input type="hidden" maxlength="30" name="oldBirthday" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.birthDay }"/>"> 
		<!-- 联系电话 -->
		<li>
		<label class="ydLable">联系电话：</label>
		<c:if test="${isAllowModifyXSVisaOrder  eq '0' }">
		<span class="fArial"><input type="text" name="telephone" value="${traveler.telephone }" readonly></input></span>
	    </c:if>
	    <c:if test="${isAllowModifyXSVisaOrder  eq '1' }">
	    <input value="${traveler.telephone }" type="text" name="telephone" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
	    </c:if>
		</li>
		<input type="hidden" maxlength="30" name="oldTelephone" value="${traveler.telephone }">
		<!-- 护照号 -->
		<li>
		<label class="ydLable">护照号：</label>
		<c:if test="${isAllowModifyXSVisaOrder  eq '0' }">
		<span class="fArial"><input type="text" name="passportCode" value="${traveler.passportCode }" readonly></input></span>
		</c:if>
		<c:if test="${isAllowModifyXSVisaOrder  eq '1' }">
		 <input type="text" name="passportCode" class="traveler" maxlength="50" value="${traveler.passportCode }" >
		</c:if>
		</li>
		<input type="hidden" maxlength="30" name="oldPassportCode" value="${traveler.passportCode }"> 
		<!-- 护照签发日期 -->
		<li><label class="ydLable">护照签发日期：</label>
		<c:if test="${isAllowModifyXSVisaOrder  eq '0' }">
		<span class="fArial"><input type="text" name="issuePlace" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.issuePlace }"/>" readonly></input></span>
		</c:if>
		<c:if test="${isAllowModifyXSVisaOrder  eq '1' }">
		 <input onclick="WdatePicker()" type="text" name="issuePlace" class="traveler traveler2 dateinput" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.issuePlace }"/>">
		</c:if>
		</li>
		<input type="hidden" maxlength="30" name="oldIssuePlace"   value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.issuePlace }"/>" > 
		<!-- 护照有效期 -->
		<li><label class="ydLable">护照有效期：</label>
		<c:if test="${isAllowModifyXSVisaOrder  eq '0' }">
		<span class="fArial"><input type="text" name="passportValidity" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.passportValidity }"/>" readonly></input></span>
		</c:if>
		<c:if test="${isAllowModifyXSVisaOrder  eq '1' }">
		<input type="text" name="passportValidity" class="traveler traveler2 dateinput" onclick="WdatePicker()" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.passportValidity }"/>">
		</c:if>
		</li>
		<input type="hidden" maxlength="30" name="oldPassportValidity"   value="<fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.passportValidity }"/>" > 
	<!--	<li><label class="ydLable">身份证号：</label><span class="fArial">${traveler.idCard }</span></li>--> 
        <li><label class="ydLable">护照类型：</label>
        <c:if test="${isAllowModifyXSVisaOrder  eq '0' }">
        <c:if test="${traveler.passportType==1 }"><input type="hidden" name="passportType" value="1"></input>因公护照</c:if>
			<c:if test="${traveler.passportType==2 }"><input type="hidden" name="passportType" value="2"></input>因私护照</c:if>
        </c:if>
        <c:if test="${isAllowModifyXSVisaOrder  eq '1' }">
          <select name="passportType" class="selCountry">
		    <c:forEach items="${passportTypeList}" var="passportType">
			<option value="${passportType.key}"<c:if test="${passportType.key eq  traveler.passportType}">selected</c:if>>${passportType.value}</option>
		    </c:forEach>
		</select>
        </c:if>
        </li>
	   <c:if test="${traveler.passportType==1 }"> 
		<input type="hidden" maxlength="30" name="oldPassportType" value="1">
		</c:if>
		<c:if test="${traveler.passportType==2 }"> 
		<input type="hidden" maxlength="30" name="oldPassportType" value="2">
		</c:if> 
		<!-- 0318新增是否允许修改销售签证订单下的游客信息--e -->
		<!-- 544需求 -->
			<c:if test="${fns:getUser().company.uuid eq '980e4c74b7684136afd89df7f89b2bee'}"> <li><label class="ydLable">签发地：</label><span class="fArial">
			<c:if test="${isAllowModifyXSVisaOrder  eq '0' }"><input type="text" name="issuePlace1" maxlength="10"  onblur="this.value=this.value.replace(/[^\u4e00-\u9fa5]+/g,'')" onafterpaste="this.value=this.value.replace(/[^\u4e00-\u9fa5]+/g,'')" value="${traveler.issuePlace1 }" readonly></input></c:if>
			<c:if test="${isAllowModifyXSVisaOrder  eq '1' }"><input type="text" name="issuePlace1" maxlength="10"  onblur="this.value=this.value.replace(/[^\u4e00-\u9fa5]+/g,'')" onafterpaste="this.value=this.value.replace(/[^\u4e00-\u9fa5]+/g,'')" class="inputTxt" value="${traveler.issuePlace1 }"/></</c:if></span></li></c:if>
	</ul>
</c:otherwise>
</c:choose>
	                  <div class="ydbz_tit ydbz_tit_child"><em class="ydExpand" onclick="boxCloseOnAdd(this)"></em>申请办签</div>
	                  <ul flag="messageDiv">
	                    <div class="ydbz_scleft">
	                      <table class="table-visa">
	                        <thead>
	                          <tr>
									<th width="10%">申请国家</th><!-- 订单中已有游客 -->
									<th width="10%">领区</th>
									<th width="10%">签证类别</th>
									<th width="10%">签证状态</th>
									<th width="13%">担保</th>
									<th width="15%">预计出团时间<br>预计约签时间</th>
									<th width="15%">
											实际出团时间
										<!-- 懿洋假期的美国签证隐藏 C482-->
										<c:if test="${!(fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' && country.countryName_cn=='美国')}">	
											<br>实际约签时间
										</c:if>
									</th>
									<!-- 0211需求,新增预计回团时间,限定为批发商星徽四海-s -->
									    <c:if test="${fns:getUser().company.uuid eq '0e19ac500f78483d8a9f4bb768608629'}">
									   <th width="13%"><span style="color:red;">*</span>预计回团时间</th>
									   </c:if>
									<!-- 0615需求,新增是否需要押金,是否需要上传资料限定为批发商大洋国旅--> 
									<%--<c:if test="${fns:getUser().company.uuid eq '7a81a03577a811e5bc1e000c29cf2586'}">
									<th width="8%">是否需要押金</th>
									<th width="8%">是否上传资料</th>
									</c:if>--%>
								  <!-- 0615需求,新增是否需要押金,是否需要上传资料限定为批发商大洋国旅--> 
								</tr>
	                        </thead>
	                        <tbody>
								<tr>
									<td class="tl">${country.countryName_cn }</td>
									<td class="tl">	<c:if test="${not empty visaProduct.collarZoning }">
				                           ${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}
			                        	</c:if></td>
									<td class="tl">${visaType.label }</td>
									<td class="tl"><c:if test="${traveler.visa.visaStauts==-1 }">无</c:if>
														<c:if test="${traveler.visa.visaStauts==null}">无</c:if>
														<c:if test="${traveler.visa.visaStauts==0 }">未送签</c:if>
														<c:if test="${traveler.visa.visaStauts==1 }">送签</c:if>
														<c:if test="${traveler.visa.visaStauts==2 }">已约签</c:if>
														<c:if test="${traveler.visa.visaStauts==3 }">通过</c:if>
														<c:if test="${traveler.visa.visaStauts==4 }">未约签</c:if>
														<c:if test="${traveler.visa.visaStauts==5 }">已撤签</c:if>
														<c:if test="${traveler.visa.visaStauts==7 }">拒签</c:if>
														<c:if test="${traveler.visa.visaStauts==8 }">调查</c:if>
														<c:if test="${traveler.visa.visaStauts==9 }">续补资料</c:if>
									</td>
									<td class="tl">
										<c:if test="${traveler.visa.guaranteeStatus==1 }">担保</c:if>
										<c:if test="${traveler.visa.guaranteeStatus==2 }">担保+押金</c:if>
										<c:if test="${traveler.visa.guaranteeStatus==3 }">押金</c:if>
									</td>
									<td class="p0">
                                                             <div class="out-date"><fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.visa.forecastStartOut }"/></div>
                                                             <div class="close-date"><fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.visa.forecastContract }"/></div>
                                                         </td>
                                                         
									<td class="p0">
                                                             <div class="out-date"><fmt:formatDate pattern="yyyy-MM-dd" value="${traveler.visa.startOut }"/></div>
                                                             <!-- 懿洋假期的美国签证隐藏 C482-->
															 <c:if test="${!(fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' && country.countryName_cn=='美国')}">	
                                                             	<div class="close-date"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${traveler.visa.contract }"/></div>
                                                         	 </c:if>
                                                         </td>
                                     <!-- 0211需求,新增预计回团时间,限定为批发商星徽四海-s-已有游客 -->
                                       <c:if test="${fns:getUser().company.uuid eq '0e19ac500f78483d8a9f4bb768608629'}">
                                       <td class="t0">
										  <input type="text" onclick="WdatePicker()" name="forecastBackDate"  class="inputTxt dateinput"  id="" value='<fmt:formatDate value="${traveler.visa.forecastBackDate}" pattern="yyyy-MM-dd" />' style="display:inline-block;"/>
										  <div id="span4ForecastBackDate" style="display:none;"><fmt:formatDate value="${traveler.visa.forecastBackDate}" pattern="yyyy-MM-dd"/></div>
									   </td>
									   </c:if>
										 <!-- 0615需求,新增是否需要押金,是否需要上传资料限定为批发商大洋国旅--> 
									<%--<c:if test="${fns:getUser().company.uuid eq '7a81a03577a811e5bc1e000c29cf2586'}">
									 <td>
									 <c:if test="${traveler.visa.depositValue==1 }">
									 <label><input type="radio" style="width:13px" name="depositValue" value="1" checked="checked">是</label><label><input type="radio" style="width:13px;margin-left: 5px" name="depositValue" value="0">否</label>
									 </c:if>
									 <c:if test="${traveler.visa.depositValue!=1 }">
									 <label><input type="radio" name="depositValue" value="1" style="width:13px">是</label><label><input type="radio" name="depositValue" value="0" checked="checked" style="width:13px;margin-left: 5px">否</label>
									 </c:if>--%>
									  <!-- 0615需求,新增是否需要押金,是否需要上传资料限定为批发商大洋国旅-->
									 </td>  
									  <%--<td>--%>
									 <%--<c:if test="${traveler.visa.datumValue==1}">--%>
									 <%--<label><input type="radio" name="datumValue" value="1" checked="checked" style="width: 13px">是</label><label><input type="radio" name="datumValue" value="0" style="width: 13px;margin-left: 5px">否</label>--%>
									 <%--</c:if>--%>
									 <%--<c:if test="${traveler.visa.datumValue!=1 }">--%>
									 <%--<label><input type="radio" name="datumValue" value="1" style="width: 13px">是</label><label><input type="radio" name="datumValue" value="0" checked="checked" style="width: 13px;margin-left: 5px">否</label>--%>
									 <%--</c:if>--%>
									  <%--</td> --%>
									  <%--</c:if>--%>
								</tr>
							</tbody>
	                      </table>
	                    </div>
	                    <!-- 上传资料部分 -->
	                    
	                    <div class="ydbz_tit ydbz_tit_child">上传资料</div>
	                    <ul class="ydbz_2uploadfile ydbz_scleft">
							<li class="seach25 seach33">
								<p>护照首页：<span class="uploadlong"><a onclick="downloadDocs('${traveler.visa.passportPhoto.id }')" title="${traveler.visa.passportPhoto.docName }" >${traveler.visa.passportPhoto.docName }</a></span></p>
								<input type="text" name="passportPhotoId" id="passportPhotoId"  style="display:none;"  value="${traveler.visa.passportPhotoId }" >
								<input type="button" name="passportPhoto" onclick="uploadFiles('${ctx}','',this,'true');" class="btn btn-primary" value="上传">
								<span class="fileLogo"></span>
							</li>
							<li class="seach25 seach33">
								<p>身份证正面：<span class="uploadlong"><a onclick="downloadDocs('${traveler.visa.identityFrontPhoto.id }')" title="${traveler.visa.identityFrontPhoto.docName }" >${traveler.visa.identityFrontPhoto.docName }</a></span></p>
								<input type="text" name="identityFrontPhotoId" id="identityFrontPhotoId"  style="display:none;" value="${traveler.visa.identityFrontPhotoId }" >
								<input type="button" name="identityFrontPhoto" onclick="uploadFiles('${ctx}','',this,'true');" class="btn btn-primary" value="上传">
								<span class="fileLogo"></span>
							</li>
							<li class="seach25 seach33">
								<p>　申请表格：<span class="uploadlong"><a onclick="downloadDocs('${traveler.visa.tablePhoto.id }')" title="${traveler.visa.tablePhoto.docName }" >${traveler.visa.tablePhoto.docName }</a></span></p>
								<input type="text" name="tablePhotoId" id="tablePhotoId"  style="display:none;"  value="${traveler.visa.tablePhotoId }" >
								<input type="button" name="tablePhoto" onclick="uploadFiles('${ctx}','',this,'true');" class="btn btn-primary" value="上传">
								<span class="fileLogo"></span>
							</li>
					   		<p class="kong"></p>
							<li class="seach25 seach33">
								<p>电子照片：<span class="uploadlong"><a onclick="downloadDocs('${traveler.visa.personPhoto.id }')" title="${traveler.visa.personPhoto.docName }" >${traveler.visa.personPhoto.docName }</a></span></p>
								<input type="text" name="personPhotoId" id="personPhotoId"  style="display:none;"  value="${traveler.visa.personPhotoId }" >
								<input type="button" name="personPhoto" onclick="uploadFiles('${ctx}','',this,'true');" class="btn btn-primary" value="上传">
								<span class="fileLogo"></span>
							</li>
							<li class="seach25 seach33">
								<p>身份证反面：<span class="uploadlong"><a onclick="downloadDocs('${traveler.visa.identityBackPhoto.id }')" title="${traveler.visa.identityBackPhoto.docName }" >${traveler.visa.identityBackPhoto.docName }</a></span></p>
								<input type="text" name="identityBackPhotoId" id="identityBackPhotoId"  style="display:none;"  value="${traveler.visa.identityBackPhotoId }" >
								<input type="button" onclick="uploadFiles('${ctx}','',this,'true');" class="btn btn-primary" value="上传">
								<span class="fileLogo"></span>
							</li>
							<li class="seach25 seach33">
							   <!-- 签证附件 -->
								<p>签证附件：
								<span class="uploadlong"><a onclick="downloadDocs('${traveler.visa.docIds }')" >
								<c:forEach items="${traveler.visa.docs}" var="docs">
									<a onclick="downloadDocs('${docs.id}')" > ${docs.docName};</a>
								</c:forEach></a></span>
								</p>
								<input type="text" name="docIds" id="docIds"  style="display:none;"  value="${traveler.visa.docIds }" >
								<input type="button" name="docs" onclick="uploadFilesvisa('${ctx}','',this,'false');" class="btn btn-primary" value="上传">
								<span class="fileLogo"></span>
								<!-- 签证附件 -->
							</li>
							<p class="kong"></p>
							<li class="seach25 seach33">
								<p>户口本：<span class="uploadlong"><a onclick="downloadDocs('${traveler.visa.familyRegisterPhoto.id }')" title="${traveler.visa.familyRegisterPhoto.docName }" >${traveler.visa.familyRegisterPhoto.docName }</a></span></p>
								<input type="text" name="familyRegisterPhotoId" id="familyRegisterPhotoId"  style="display:none;"  value="${traveler.visa.familyRegisterPhotoId }" >
								<input type="button" name="familyRegisterPhoto" onclick="uploadFiles('${ctx}','',this,'true');" class="btn btn-primary" value="上传">
								<span class="fileLogo"></span>
							</li>
							<li class="seach25 seach33">
								<p>房产证：<span class="uploadlong"><a onclick="downloadDocs('${traveler.visa.houseEvidencePhoto.id }')" title="${traveler.visa.houseEvidencePhoto.docName }" >${traveler.visa.houseEvidencePhoto.docName }</a></span></p>
								<input type="text" name="houseEvidencePhotoId" id="houseEvidencePhotoId"  style="display:none;"  value="${traveler.visa.houseEvidencePhotoId }" >
								<input type="button" name="houseEvidencePhoto" onclick="uploadFiles('${ctx}','',this,'true');" class="btn btn-primary" value="上传">
								<span class="fileLogo"></span>
							</li>
							
							<li class="seach25 seach33">
								<p>其它：<span class="uploadlong"><a onclick="downloadDocs('${traveler.visa.otherPhoto.id }')" title="${traveler.visa.otherPhoto.docName }" >${traveler.visa.otherPhoto.docName }</a></span></p>
								<input type="text" name="otherPhotoId" id="otherPhotoId"  style="display:none;"  value="${traveler.visa.otherPhotoId }" >
								<input type="button" name="otherPhoto" onclick="uploadFiles('${ctx}','',this,'true');" class="btn btn-primary" value="上传">
								<span class="fileLogo"></span>
							</li>
							
						</ul>
	                    <div class="ydbz_tit ydbz_tit_child">需提交办签资料</div>
						<ul class="seach25 seach100 ydbz_2uploadfile">
						            <!-- **********197-start -->
                                     <p>原件：</p>              
                                     <input type="checkbox" name="original_Project_Type" value="0" <c:if test="${fn:contains(visaProduct.original_Project_Type,'0')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">护照</label></span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="1" <c:if test="${fn:contains(visaProduct.original_Project_Type,'1')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">身份证</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="3" <c:if test="${fn:contains(visaProduct.original_Project_Type,'3')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">电子照片</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="4" <c:if test="${fn:contains(visaProduct.original_Project_Type,'4')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">申请表格</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="5" <c:if test="${fn:contains(visaProduct.original_Project_Type,'5')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">户口本</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="original_Project_Type" value="6" <c:if test="${fn:contains(visaProduct.original_Project_Type,'6')}">checked="checked"</c:if> id=""   disabled="disabled" /><label for="">房产证</label>
                                     </span>
                                     <span class="seach_check">
                                      <input type="checkbox" name="original_Project_Type" value="2" <c:if test="${fn:contains(visaProduct.original_Project_Type,'2')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">其他</label><input type="text" name="original_Project_Name" value="${visaProduct.original_Project_Name}" class="input-mini"  disabled="disabled" ></span><br/>
                                     <p>复印件：</p> 
                                     <input type="checkbox" name="copy_Project_Type" value="3"   <c:if test="${fn:contains(visaProduct.copy_Project_Type,'3')}">checked="checked"</c:if> id=""   disabled="disabled"  ><label for="">护照</label></span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="4"   <c:if test="${fn:contains(visaProduct.copy_Project_Type,'4')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">身份证</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="5"   <c:if test="${fn:contains(visaProduct.copy_Project_Type,'5')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">电子照片</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="6"   <c:if test="${fn:contains(visaProduct.copy_Project_Type,'6')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">申请表格</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="0"   <c:if test="${fn:contains(visaProduct.copy_Project_Type,'0')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">户口本</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type" value="1"   <c:if test="${fn:contains(visaProduct.copy_Project_Type,'1')}">checked="checked"</c:if>  id=""  disabled="disabled" ><label for="">房产证</label>
                                     </span>
                                     <span class="seach_check">
                                     <input type="checkbox" name="copy_Project_Type"  value="2" <c:if test="${fn:contains(visaProduct.copy_Project_Type,'2')}">checked="checked"</c:if> id=""  disabled="disabled" ><label for="">其他</label><input type="text" name="copy_Project_Name" value="${visaProduct.copy_Project_Name}" class="input-mini"  disabled="disabled" ></span></ul>
                					<!-- **********197-end -->
                		</ul>
						
	                  <div class="ydbz_tit ydbz_tit_child"><em class="ydExpand"></em>备注：
	                    <textarea style="max-width: 948px;" class="textarea_long"  name="remark">${traveler.remark}</textarea>
	                  </div>
	                </div>
	                <!--游客信息左侧结束--> 
	                <!--游客信息右侧开始-->
	                <div class="tourist-right">
	                  <div class="bj-info">
	                    <div class="ydbz_tit ydbz_tit_child">报价</div>
	                    <div class="clearfix">
	                      <ul class="tourist-info2">
	                        <li>
	                          <label class="ydLable2">应收价格：</label>
	                          ${currency.currencyMark }<span class="ydFont1"><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${visaOrder.proOriginVisaPay}" /></span>
	                          <input type="hidden" name="singleDiff"  class="traveler" value="0" >
	                        </li>
	                      </ul>
	                    </div>
	                    <!-- <div class="yd-line"></div> -->
	                    <div class="clearfix"> 
	                    <!-- a name="addcost" class="btn-addBlue">添加其他费用</a -->
	                      <div class="payfor-otherDiv">
	                            <!-- 
	                      		<c:forEach items="${traveler.costChange }" var="costChange" varStatus="status">
	                     		<div class="payfor-other cost costrmb" style="display: none;">
	                     		
	                     			<select onchange="changeCostCurrency(this)" name="currency">
	                     				<option value="1" <c:if test="${costChange.priceCurrency.id==1}">selected="selected"</c:if>>人民币</option>
	                     				<option value="2" <c:if test="${costChange.priceCurrency.id==2}">selected="selected"</c:if>>美元</option>
	                     				<option value="3" <c:if test="${costChange.priceCurrency.id==3}">selected="selected"</c:if>>欧元</option>
	                     				<option value="4" <c:if test="${costChange.priceCurrency.id==4}">selected="selected"</c:if>>英镑</option>
	                     				<option value="5" <c:if test="${costChange.priceCurrency.id==5}">selected="selected"</c:if>>日元</option>
	                     			</select><input type="text" class="required ipt2" id="costname11" value="${costChange.costName}" onblur="payforotherOut(this)" maxlength="50" onfocus="payforotherIn(this)" name="cosName"><span onclick="focusIpt(this)" style="display: none;" class="ipt-tips2">费用名称</span><input type="text" onkeyup="changeSum(this)" onafterpaste="changeSum(this)" class="required number ipt3" maxlength="15" value="${costChange.costSum}" style="" name="sum" id="costvalue11">
	                     			<a class="btn-del1" name="deleltecost"></a>
	                     			</div>
	                     		</c:forEach> 
	                     		<ul class="tourist-info2">
									<c:forEach items="${traveler.costChange }" var="costChange" varStatus="status">
									<li class="tourist-info2-first">
										<label class="ydLable2 ydColor1">${costChange.costName}：</label>${costChange.priceCurrency.currencyMark} <fmt:formatNumber  type="currency" pattern="#,##0.00" value="${costChange.costSum}" />
									</li>
									</c:forEach>
								</ul>
								 -->
	                      </div>
	                    </div>
	                  </div>
	                  <div class="yd-line"></div>
	                  <div class="yd-total clearfix"><!-- <a name="bjyyqb" class="ydbz_x" onclick="useAllPrice(this)">报价应用全部</a> --></div>
						<div class="yd-total clearfix">
							<div class="fr">
							<!-- 返佣费用  -->
								<div class="clearfix1">
									<div class="traveler-rebatesDiv" >
										 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
								       <c:choose>
								      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
												<label class="ydLable2 ydColor1">预计个人宣传费：</label>
										</c:when>
								         <c:otherwise>
								         		<label class="ydLable2 ydColor1">预计个人返佣：</label>
								          </c:otherwise>
										 </c:choose>   
										<select name="rebatesCurrencyID" id="visaTravelerRebateCurrencyId">
											<c:forEach items="${currencyList4Rebates}" var="cu">
												<option value="${cu.id}"  <c:if test="${cu.id==traveler.rebatesCurrencyID}">selected="selected"</c:if> >${cu.currencyName}</option>
											</c:forEach>
										</select>
										<input type="text"  maxlength="9" style="width: 40px;" name="rebatesAmount" class="ipt-rebates" value="${(traveler.rebatesAmount eq null||'' eq traveler.rebatesAmount)?0:traveler.rebatesAmount}" id="visaTravelerRebateAmount" onafterpaste="validNum(this)" onkeyup="validNum(this)"/>
									</div>
								</div>
							</div>
						</div>
						<div class="yd-total clearfix">
							<div class="fr">
							<!--
							<label class="ydLable2">结算价：</label>
							<span class="ydFont2">${traveler.payPriceSerialNumInfo}</span>
							<input type="hidden" name="srcPrice" class="traveler">
							<input type="hidden" name="srcPriceCurrency" class="traveler">
							<input type="hidden" name="jsPrice" class="traveler">
							<input type="hidden" name="payPrice" class="traveler">
							-->
							<label class="ydLable2">结算价：</label>
							<span name="clearPrice" class="ydFont2">
								<input type="hidden"   name="start">
								<c:forEach items="${traveler.currencies }" var="currency" varStatus="status">
									 <div name="inputClearPriceDiv">
									${currency.currencyName}:
									<input type="hidden" value="${currency.id}" alt="${currency.currencyName}" name="inputCurreyId">
									<input id="${currency.currencyMark}" class="required ipt3" 
									type="text"  onkeyup="changeClearPriceSum(this)" 
									onafterpaste="changeClearPriceSum(this)" 
									value="${currency.convertCash}" maxlength="15" 
									name="inputClearPrice" style="margin-right:0px;"><!-- ttt游客结算价 -->
									<span name="hidden4InputClearPrice" style="display:none">${currency.convertCash}</span><!-- 解决bug14200 添加 -->
									</div> 
							<br>
	                          </c:forEach>
							<input type="hidden"  name="end">
							
	                       </span>
	                       <input type="hidden" name="travelerClearPrice" class="traveler">
	                       <input type="hidden" id="traveler_OrderId"    value="${traveler.orderId}" >
	                    </div>
	                  </div>
	                </div>
	              </div>
	             
	              <!--保存、取消按钮开始-->
	              <div class="rightBtn"><a class="btn" onclick="mySaveTraveler(this)">修改</a></div>
	              
	              <!-- 0211需求,新增预计回团时间,限定为批发商星徽四海-s -->
	              <input type="hidden" name="travelerVisaId" value="${traveler.visa.id}" />
	              <!-- 0211需求,新增预计回团时间,限定为批发商星徽四海-e -->
	              
	              <!--
	            <div class="rightBtn">
	              <input type="button" name="saveBtn" class="btn btn-primary" onclick="saveTraveler(this,this.form,)" value="保存">
	              <input type="button" name="editBtn" class="btn btn-primary" onclick="saveTravelerAfter(this,this.form,'edit')" style="display:none" value="修改">
	          </div>
	            --> 
	              <!--保存、取消按钮结束--> 
	            </div>
	          </form>
			</c:forEach>
			</div>
			<!-- 把游客模板替换为新增时的模板，对应需求号  c333--> 
            <!--添加游客按钮开始  -->
            <c:if test="${issubject == 0}">
                <div class="touristBtn"><a class="btn-addGrey" id="addTraveler">添加游客</a></div> 
            </c:if>
            
            <!--添加游客按钮结束-->
			<div class="clearfix2">
				<div class="traveler-rebatesDiv" >
					<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
			       <c:choose>
			      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
							<label class="ydLable2 ydColor1" style="width: 100px;">预计团队宣传费：</label>
					</c:when>
			         <c:otherwise>
			         		<label class="ydLable2 ydColor1" style="width: 100px;">预计团队返佣：</label>
			          </c:otherwise>
					 </c:choose>   
					
					<select id="groupRebatesCurrency" name="groupRebatesCurrency">
						<c:forEach items="${currencyList4Rebates }" var="cu">
							<option value="${cu.id}" <c:if test="${cu.id==groupRebatesCurrency}">selected="selected"</c:if>>${cu.currencyName}</option>
						</c:forEach>
					</select>
					<input type="text" class="required ipt-rebates" maxlength="15" id="groupRebatesMoney" name="groupRebatesMoney" value="${groupRebatesMoney }" onafterpaste="validNum(this)" onkeyup="validNum(this)">
					<a class="btn" onclick="updateGroupRebates()">保存</a>
				</div>
			</div>
			<div class="ydbz_tit">收款信息</div>
			<c:forEach items="${orderPays }" var="orderPay" varStatus="status">
			<p class="orderdetails6">
				<span style="width: 16%" class="ellipsis" title="${orderPay.remarks}">游客姓名：${orderPay.remarks}</span> <%-- 使用orderpay的备注字段remarks来显示游客名称--%>
			   <span style="width: 14%">收款方式：<c:if test="${orderPay.payType==1 }">支票支付</c:if>
			   <c:if test="${orderPay.payType==2 }">POS支付</c:if>
			   <c:if test="${orderPay.payType==3 }">现金支付</c:if>
			   <c:if test="${orderPay.payType==4 }">汇款支付</c:if>
			   <c:if test="${orderPay.payType==5 }">快速支付</c:if>
			   </span>
			   <span style="width: 14%">收款类型：<c:if test="${orderPay.payPriceType==1 }">收全款</c:if>
			   			<c:if test="${orderPay.payPriceType==2 }">收尾款</c:if>
			   			<c:if test="${orderPay.payPriceType==3 }">收订金</c:if>
			   			<c:if test="${orderPay.payPriceType==7 }">收押金</c:if>
			   			<c:if test="${orderPay.payPriceType==16 }">收押金</c:if>
			   </span>
			   <span style="width: 18%">收款金额：${orderPay.moneyAmount }</span>
			   <span style="width: 20%">收款时间： <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${orderPay.createDate }"/></span>
			   <c:choose> 
					<c:when test="${orderPay.payVoucher != null && orderPay.payVoucher !='' }">
			   		<span style="width: 14%">收款凭证：<a lang="4453" class="showpayVoucher" onclick="downloadDocs('${orderPay.payVoucher }')" >收款凭证</a></span>
			   		</c:when>
					<c:otherwise>
					<span style="width: 14%">收款凭证：<a lang="4453" class="showpayVoucher" onclick="errorMessage()" >收款凭证</a></span>
					</c:otherwise>
			   	</c:choose>
			</p>
			</c:forEach>

			<div class="payment_information">
				<%-- <p class="orderdetails6">
					<c:forEach items="${costs }" var="cost" varStatus="status">
					<span>${cost[1] }：${cost[3] }${cost[2] }/成人x${cost[0] }人</span>
					</c:forEach>
				</p> --%>
					<div style="text-align:right; font-size:12px; margin-top:20px;  padding-top:10px; padding-right:10px;">
						<b style="font-size:18px">订单总结算价：</b>
						<!-- c333:保留原始值 -->
						<!--
        	    <span id="travelerSumClearPrice" class="tdred f20">${totalFinalPirce}</span>
        	    -->
						<span id="travelerSumClearPrice" class="tdred f20">${totalMoney}</span><!-- ttt订单总结算价 -->
					</div>
				<div class="ordermoney ordermoney2">
				<!-- 应收总计：<span id="travelerSumPrice" class="tdred f20"></span><br /> -->
				应收总计：<em class="gray14"></em>
				<!-- c333 ：保留原始值-->
				<!--  
				<span id="travelerSumPrice">${totalMoney }</span><br />
				-->
				<span id="travelerSumPrice">${costTotalMoney}</span><br />
				
				达账金额：<em class="gray14"></em><span><c:if test="${accountedMoney=='¥ 0.00' }">¥0.00</c:if><c:if test="${accountedMoney!='¥ 0.00' }">${accountedMoney}</c:if></span><br />
				签证押金：<span><c:if test="${totalDeposit=='¥ 0.00' }">¥0.00</c:if><c:if test="${totalDeposit!='¥ 0.00' }">${totalDeposit}</c:if></span></div>
			</div>
                     
			<div class="ydbz_sxb" id="secondDiv" style="display: block;">
				<div class="ydBtn ydBtn2">
					<!-- <a class="ydbz_s gray">取消</a><input type="button" class="btn btn-primary fl" value="确定"> -->
				</div>
			</div>
		</div>
	</div>


<script type="text/javascript">
$(document).ready(function(e) {
    var leftmenuid = $("#leftmenuid").val();
    $(".main-nav").find("li").each(function(index, element) {
        if($(this).attr("menuid")==leftmenuid){
            $(this).css("background", "url(/resource/images/img/pfs_nav_hover.png)");
        }
    });
    $("#agentInfoId").comboboxInquiry();
    
	$("#agentInfoId").next().find(".custom-combobox-input").blur(function(){
		getAgentinfo();
	});
});
$(function(){
    $('.closeNotice').click(function(){
        var par = $(this).parent().parent();
        par.hide();
        par.prev().removeClass('border-bottom');
        par.prev().find('.notice-date').show();
    });
    $('.showNotice').click(function(){
        $(this).parent().hide();
        var par = $(this).parent().parent();
        par.addClass('border-bottom');
        par.next().show();
    });
});
$(function(){
	$('.main-nav li').click(function(){
		$(this).addClass('select').siblings().removeClass('select');
	})
})

String.prototype.formatNumberMoney= function(pattern){
	  var strarr = this?this.toString().split('.'):['0'];   
	  var fmtarr = pattern?pattern.split('.'):[''];   
	  var retstr='';   
	  var str = strarr[0];
	  var fmt = fmtarr[0];
	  var i = str.length-1;     
	  var comma = false;   
	  for(var f=fmt.length-1;f>=0;f--){   
	    switch(fmt.substr(f,1)){   
	      case '#':   
	        if(i>=0 ) retstr = str.substr(i--,1) + retstr;   
	        break;   
	      case '0':   
	        if(i>=0) retstr = str.substr(i--,1) + retstr;   
	        else retstr = '0' + retstr;   
	        break;   
	      case ',':   
	         comma = true;   
	         retstr=','+retstr;   
	        break;   
	     }   
	   }   
	  if(i>=0){   
	    if(comma){   
	      var l = str.length;   
	      for(;i>=0;i--){   
	         retstr = str.substr(i,1) + retstr;   
	        if(i>0 && ((l-i)%3)==0) retstr = ',' + retstr;   
	       }   
	     }   
	    else retstr = str.substr(0,i+1) + retstr;   
	   }   
	  
	   retstr = retstr+'.';   
	   
	   str=strarr.length>1?strarr[1]:'';   
	   fmt=fmtarr.length>1?fmtarr[1]:'';   
	   i=0;   
	  for(var f=0;f<fmt.length;f++){   
	    switch(fmt.substr(f,1)){   
	      case '#':   
	        if(i<str.length) retstr+=str.substr(i++,1);   
	        break;   
	      case '0':   
	        if(i<str.length) retstr+= str.substr(i++,1);   
	        else retstr+='0';   
	        break;   
	     }   
	   }   
	  return retstr.replace(/^,+/,'').replace(/\.$/,'');   
}

String.prototype.replaceSpecialChars=function(regEx){
	if(!regEx){
		regEx = /[\`\"\~\!\@#$\%\^\&\*\(\)\+\=\|\{\}\'\:\;\'\,\/\/\[\/\/\]\.\<\>\/\?\~\！\@#\¥\%\…\…\&\*\（\）\—\—\+\|\{\}\【\】\‘\；\：\”\“\’\。\，\、\？]/g;
	}
	return this.replace(regEx,'');
	
};

//游客信息 onblur 事件的处理	  游客名称失去焦点时触发
$("#traveler").delegate("input[name='name']","blur",function() {
	var srcname = $(this).val();
	
	var name = $(this).closest("form").find(".tourist-t-off .name").text(srcname);
	
	if ($.trim(srcname).length <= 0) {
		return false;
	}

	var pinying = $(this).closest("form").find(
			"input[name='nameSpell']").eq(0);
	var tName = $(this).closest("form").find("span[name='tName']").eq(0).html(srcname);
	$.ajax( {
		type : "POST",
		url : g_context_url+"/orderCommon/manage/getPingying",
		data : {
			srcname : $.trim(srcname)
		},
		success : function(msg) {
			pinying.val(msg);
		}
	});
});

/******************************************************/
/*112需求:特殊需求过滤字符:"<",">","\"," "" "," '' "的函数方法      */
/******************************************************/
String.prototype.replaceSpecialChars4SpecialRemark= function (regEx) {//112-my
   if (!regEx){
     regEx = /[\`\"\'\'\‘\”\“\’\<\>\\]/g;
     }
   return this.replace(regEx, '');
    };   
</script>
<script>


var dataArrayStr2 = '${contactArray}';
// var dataStr = "[" + '${contactsJsonStr}' + "]";
var dataStr = '${contactsJsonStr}';
var tempFirstContactName = '${agentinfo.agentContact}';

var dataArrayStr = '${contactArray }';  //conta转换的jsonArray
var dataArray = eval(dataArrayStr);
var agentinfoId = '${agentinfo.id }';
var address = '${address }';
$(function(){

  
    var allowModifyAgentInfo =true;  ;
    if(${allowModifyAgentInfo} != 1)
    allowModifyAgentInfo = false;
    
    //初始加载时，订单的联系人
     var orderContactsList = eval(${orderContactsListJsonArray});
        var orderContactsNum = orderContactsList.length;        
        //
        var allowModifyAgentInfo = ${allowModifyAgentInfo };
		var allowAddAgentInfo = ${allowAddAgentInfo };
		//初始化控件
	//    if(allowModifyAgentInfo == 0){
	//		//不可修改时，把联系人input置为只读（'其他'除外）
	//		setContactInfoReadonly();
	//    }
	
	//    var dataStr = '${contactsJsonStr}';  //自己组织的data
	    if(${agentInfo.id} == -1){
	    	var $channel = $('[name="channelConcat"]');
	    	$channel.after('<input name="fqy" style="border-radius:4px;border:1px solid #ccc;"/>');
	    	$channel.remove();
	    }else{
	    //debugger;
	    	    $('[name="channelConcat"]').combox({datas:dataStr,
	// 	        回调函数,可在此函数中获取选择的联系人的uuid,但只对原始的联系人框有效,对于新增的联系人在第28行获取选中项信息
	        onSelect:function(obj){	        	
	        	//选择联系人填充其他信息
				for(var i=0; i<dataArray.length; i++){
					var tempData = dataArray[i];
					if(tempData.uuid == $(obj).find('a').attr('uuid')){
	            		fillContactInfo(obj, tempData);
		        	}
				}
	        },editable:allowModifyAgentInfo   // true 可以修改
	    });
	    }

// 	    为添加的联系人绑定事件,因第一个已添加,所以过滤掉
    $(document).on('click','[name="channelConcat"]:not(:first) em',function(){
        var $ul = $(this).next();
        if(!$ul.is(':visible')){
            $ul.show();
        }else{
            $ul.hide();
        }
    });
// 	    为下拉项添加点击事件,同样过滤掉第一个
    $(document).on('click','[name="channelConcat"]:not(:first) ul li',function(){
// 	        根据当前选中的li为文本框赋值
        $(this).parents('.combox_border:first').children(':text').val($(this).text()).attr('uuid',$(this).find('a').attr('uuid'));
        $(this).parent().hide();
// 	        获取选中项信息
		//选择联系人填充其他信息						
		for(var i=0; i<dataArray.length; i++){
			var tempData = dataArray[i];
			if(tempData.uuid == $(this).find('a').attr('uuid')){
        		fillContactInfo(this, tempData);
        	}
		}
    });
// 	    过滤特殊字符
    $(document).on('keyup','[name="channelConcat"] input',function(){
        $(this).val(replaceSpecialChars.apply($(this).val()));
    });
    $(document).on('afterpaste','[name="channelConcat"] input',function(){
        $(this).val(replaceSpecialChars.apply($(this).val()));
    });
    
    for(var i=1; i<orderContactsList.length; i++){
	    var js = {};
	    // 比如属性名为name
	    orderContactsList[i];
	    addAgentContactNew($("span[name=addContactButton]"));
	}		
	//初始化时为ordercontact联系人填充信息
    $("#ordercontact").find("ul[name=orderpersonMes]").each(function(index, element){
    	initialContactInfo(element, orderContactsList[index]);
    });
    //如果不是可输入修改，disabled下拉和除了“其他”之外的input
    if(allowModifyAgentInfo == 0){
    	if(${agentInfo.id} !=-1){
    	$("input[name=contactsName]").attr("readonly", true);
	    setContactInfoReadonly();
    	}
    }
    //如果不是可添加，隐藏掉添加联系人按钮
    if(allowAddAgentInfo == 0){
    	$("span[name=addContactButton]").hide();
    }
});

/**
* 初始化填充对应信息（对应可修改）
*/
function initialContactInfo(obj, tempData){
if($(obj).attr("name") == "orderpersonMes"){
	$(obj).find("[name=contactsName]").val(tempData.contactsName);
	$(obj).find("[name=contactsTel]").val(tempData.contactsTel);
	$(obj).find("[name=contactsTixedTel]").val(tempData.contactsTixedTel);
	$(obj).find("[name=contactsFax]").val(tempData.contactsFax);
	$(obj).find("[name=contactsQQ]").val(tempData.contactsQQ);
	$(obj).find("[name=contactsEmail]").val(tempData.contactsEmail);
	$(obj).find("[name=contactsId]").val(tempData.id);
	if(tempData.contactsAddress=="null"){
		$(obj).find("[name=contactsAddress]").val("");
	}else{
		$(obj).find("[name=contactsAddress]").val(tempData.contactsAddress);
	}
	return;
}
}

/**
* 不可修改时，把联系人input置为只读（'其他'除外）,其实是在其后添加了一个span用于展示
*/    
function setContactInfoReadonly(){

var $mesDom = $("ul[name=orderpersonMes]");
$mesDom.find("[name=contactsTel]").attr("readonly",true);
$mesDom.find("[name=contactsTixedTel]").attr("readonly",true);
$mesDom.find("[name=contactsFax]").attr("readonly",true);
$mesDom.find("[name=contactsQQ]").attr("readonly",true);
$mesDom.find("[name=contactsEmail]").attr("readonly",true);
$mesDom.find("[name=contactsAddress]").attr("readonly",true);
$mesDom.find("[name=contactsZipCode]").attr("readonly",true);


}



/**
* 选择联系人，填充对应信息（对应可修改）
*/
function fillContactInfo(obj, tempData){
if($(obj).attr("name") == "orderpersonMes"){
	$(obj).find("[name=contactsName]").val(tempData.text);
	$(obj).find("[name=contactsTel]").val(tempData.contactMobile);
	$(obj).find("[name=contactsTixedTel]").val(tempData.contactPhone);
	$(obj).find("[name=contactsFax]").val(tempData.contactFax);
	$(obj).find("[name=contactsQQ]").val(tempData.contactQQ);
	$(obj).find("[name=contactsEmail]").val(tempData.contactEmail);
	$(obj).find("[name=contactsAddress]").val(tempData.agentAddressFull);
	$(obj).find("[name=contactsZipCode]").val("");
	$(obj).find("[name=remark]").val("");
	$(obj).find("[name=contactsId]").val(tempData.id);
	return;
}
$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsName]").val(tempData.text);
$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsTel]").val(tempData.contactMobile);
$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsTixedTel]").val(tempData.contactPhone);
$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsFax]").val(tempData.contactFax);
$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsQQ]").val(tempData.contactQQ);
$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsEmail]").val(tempData.contactEmail);
$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsAddress]").val(tempData.agentAddressFull);
$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsZipCode]").val("");
$(obj).parents("ul[name=orderpersonMes]").find("[name=remark]").val("");
	$(obj).parents("ul[name=orderpersonMes]").find("[name=contactsId]").val(tempData.id);
}

/**
* 获取当前渠道的所有联系人信息
*/
function getAllContactsByAgentId(obj){

//debugger;
contactNo = 1;
var tempAgentId = $(obj).val().split(",")[0];
$.ajax({
	type : "POST",
	url : "../../orderCommon/manage/getAllAgentContactInfo",
	data : {agentId : tempAgentId},
	success : function(myData) {
	//debugger;
		dataArray = eval(myData);
		address = dataArray[0].agentAddressFull;
		$("ul[name=orderpersonMes]:not(:first)").remove();

	    var $span = $('span[name="channelConcat"]');
	    $span.after('<span name="channelConcat"></span>');
	    $span.remove();
	    //变更下拉列表内容
		$('span[name="channelConcat"]').combox({datas:eval(myData),
			//点击
			onSelect:function(obj){
	        	//选择联系人填充其他信息
				for(var i=0; i<eval(myData).length; i++){
					var tempData = eval(myData)[i];
					if(tempData.uuid == $(obj).find('a').attr('uuid')){
	            		fillContactInfo(obj, tempData);
		        	}
				}
	        }
	    });
	    //给第一联系人填充信息
	    fillContactInfo($("ul[name=orderpersonMes]:first"), eval(myData)[0]);
	}		
});
}

/**
* 可下拉可修改
*/
$.fn.combox = function(options) {
var defaults = {
    borderCss: "combox_border",
    inputCss: "combox_input required",
    buttonCss: "combox_button",
    selectCss: "combox_select",
    firstContactName : tempFirstContactName,
    datas:[],
    onSelect:null,
    editable:true
};
var options = $.extend(defaults, options);

function _initBorder($border) {//初始化外框CSS
    $border.css({'display':'inline-block', 'position':'relative'}).addClass(options.borderCss);
    return $border;
}

function _initInput($border){//初始化输入框
	if(options.editable){
		$border.append('<input maxlength="45" type="text" name="contactsName" value="' + options.firstContactName + '" class="'+options.inputCss+'"/>');
	}else{
		$border.append('<input maxlength="45" type="text" name="contactsName" value="' + options.firstContactName + '" class="'+options.inputCss+'" readonly="readonly"/>');
	}
    
    $border.append('<em></em>');
   //绑定下拉特效
    $border.delegate('em', 'click', function() {
        var $ul = $border.children('ul');
        if($ul.css('display') == 'none') {
            $ul.slideDown('fast');
        }else {
            $ul.slideUp('fast');
        }
    });
    return $border;//IE6需要返回值
}

function _initSelect($border) {//初始化下拉列表
    $border.append('<ul style="position:absolute;left:-1px;display:none;z-index:999" class="'+options.selectCss+'">');
    var $ul = $border.children('ul');
    $ul.css('top',$border.height()+1);
    var length = options.datas.length;
    for(var i=0; i<length ;i++)
        $ul.append('<li><a href="javascript:void(0)" uuid="'+options.datas[i].uuid+'">'+options.datas[i].text+'</a></li>');
    $ul.delegate('li', 'click', function() {
        $border.children(':text').val($(this).text()).attr('uuid',$(this).find('a').attr('uuid'));
        $ul.hide();
        options.onSelect(this);
    });
    return $border;
}

this.each(function() {
    var _this = $(this);
    _this = _initBorder(_this);//初始化外框CSS
    _this = _initInput(_this);//初始化输入框
    _initSelect(_this);//初始化下拉列表
});
};

var contactNo = 1;  //联系人临时编号
/**
* 添加渠道联系人(新有控件)
*/
function addAgentContactNew(obj){		
contactNo++;
var contactPeopleNum = $("ul[name=orderpersonMes]").length;
var $currentUl = $(obj).parents("ul[name=orderpersonMes]");
var $newUl = $currentUl.clone();
//修改联系人序号
$newUl.find("span[name=contactNo]").html(contactNo);
$newUl.find('input').val('');
$newUl.find('select').append('<option value="" selected="selected"></option>');
$newUl.children('li').eq(0).find('label font').html(parseInt(contactPeopleNum) + 1);
$newUl.children('li').eq(1).find('span.yd1AddPeople').remove();
$newUl.children('li').eq(1).append('<span class="ydbz_x gray" name="delContactButton" onclick="yd1DelPeople(this)">删除联系人</span>');
$currentUl.parent().append($newUl);
$currentUl.parent().append();
}

/**
* 删除联系人
*/
function yd1DelPeople(obj) {
contactNo--;
$(obj).parent().parent().remove();
//重置联系人序号
//if ($('#orderpersonss option:selected').val() == '1'|| $(obj).parents().find('#orderpersonss').length==0) {
$("ul[name=orderpersonMes]").each(function (index, element) {
    $(element).children("li").find("span[name=contactNo]").text(index + 1);
});
}


</script>
</body>
</html>
