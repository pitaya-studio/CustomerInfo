
var addNum=10;
//新增航段
function inquiryFlights3Add(obj){
	var id=$(obj).parent().parent().find('.addFlights3Div').length+1;
	//alert(id);
	var cloneDiv = $(".addFlights3None").clone(true);
	//重置label与input的id、name、for
	var $lable = cloneDiv.find(".title_samil .seach_check label");
	$lable.each(function(index, element) {
		var str_flag = "-" + (index+1);
        $(element).attr("for","radio3" + id+1 + str_flag);
		var $input = $(element).find("input[type=radio]");
		$input.attr("id","radio3" + id + str_flag).attr("name","flightArea" + id);
    });

	cloneDiv.appendTo($(obj).parent().parent());
	//cloneDiv.appendTo($("#addDiv"));
	cloneDiv.show().removeClass('addFlights3None').removeAttr("id").find('em').text(id);
	
	cloneDiv.find("input[name=leaveAirport]").attr("id","mleaveAirport"+addNum+"Id");
	cloneDiv.find("input[name=leaveAirportName]").attr("id","mleaveAirport"+addNum+"Name");
	cloneDiv.find("input[name=desAirpost]").attr("id","mdesAirpost"+addNum+"Id");
	cloneDiv.find("input[name=desAirpostName]").attr("id","mdesAirpost"+addNum+"Name");
	cloneDiv.find("a[name=leaveHref]").attr("id","mleaveAirport"+addNum);
	cloneDiv.find("a[name=desHref]").attr("id","mdesAirpost"+addNum);
	
	$("#moreSenum").val(id);//设置段数
	addNum++;
}

//删除航段
function inquiryFlights3Del(obj){
	$(obj).parent().parent().parent().remove();
	$('.addFlights3Div').each(function(index, element){
		$(this).find('em').text(index+1);
		//重置label与input的id、name、for
		if(2 < index){
			
			var num_i = index;
			var $lable = $(element).find(".title_samil .seach_check label");
			$lable.each(function(index, element) {
				//alert(num_i);
				var str_flag = "-" + (index+1);
				$(element).attr("for","radio3" + num_i + str_flag);
				var $input = $(element).find("input[type=radio]");
				//$input.attr("id","radio3" + num_i + str_flag).attr("name","flightArea" + (num_i+1));
				$input.attr("id","radio3" + num_i + str_flag).attr("name","flightArea" + (num_i));
			});
		}
	});
	
	var sn=$("#moreSenum").val()-1;
	$("#moreSenum").val(sn);//设置段数
}

function firstToSecond(){
	
	
  $.ajax({
                        type:"POST",
                        url:root+"/stock/manager/payReservePosition",
                        data:{
                            srcActivityId:srcActivityId,
                            agentId:agentId
                        },
                        success:function(msg) {
                        	     }
         });
              
}

function selectairline(airlineCode,obj){
	//alert(ctx+"/airTicket/getspaceLevelList");
	$.ajax( {
		type : "POST",
		url :root+"/airTicket/getspaceLevelList.htm",
		data : {
			airlineCode : airlineCode
		},
		success : function(msg) {
			 var dataObj=eval("("+msg+")");
			 var spaceGrade=$(obj).parent().parent().next().children("p").eq(1).children("select");
			 //alert($(obj).parent().parent().next().children("p").eq(1).html());
			 $(spaceGrade).empty();
			 $(spaceGrade).append("<option value=''>不限</option>");
			 $.each(dataObj,function(key,value){
	         $(spaceGrade).append("<option value='"+key+"'>"+value+"</option>");
	         
	         });
			 $(spaceGrade).trigger("change");  
		}
	});
}

function selectspaceGrade(spacegrade,obj){
	
	var spaceLevel=$(obj).parent().parent().prev().children("p").eq(1).children("select");
	//alert($(spaceLevel).val());
	$.ajax( {
		type : "POST",
		url : root+"/airTicket/getspaceList",
		data : {
			airlineCode : $(spaceLevel).val(),
			spaceLevel :spacegrade
		},
		success : function(msg) {
		  var dataObj=eval("("+msg+")");
		  var airspace=$(obj).parent().parent().next().children("p").eq(1).children("select");
          $(airspace).empty();
          $(airspace).append("<option value=''>不限</option>");
	      $.each(dataObj,function(key,value){
	       $(airspace).append("<option value='"+key+"'>"+value+"</option>");
	      })
		}
	});
}

//下一步
function next(){
	var airTypeVal=$("input[name='airType_radio']:checked").val();
	//批发商uuid
	var companyUuid = $("#companyUUID").val();
	var parForm;

	if(airTypeVal==1){//多段
		$("#hiddenFilght").remove();//多段提交删除隐藏域
		parForm="frm1";
	}else if(airTypeVal==2){//往返
		parForm="frm2";
	}else{//单程
		parForm="frm3";
	}

	var departureCity = $("#"+parForm).find("select[name=departureCity]").val();
	var arrivedCity =   $("#"+parForm).find("select[name='arrivedCity']").val();
	var reservationsNum = $("#"+parForm).find("input[name='reservationsNum']").val();
	//当前机票产品切位人数
	var airticketReserveNum = $("#airticketReserveNum").val();
	//离境口岸
	var $outArea = $("#"+parForm).find("select[name=outArea]");

	if(parseInt(airticketReserveNum)>=parseInt(reservationsNum)){
		top.$.jBox.tip("预收人数不能小于切位人数!", 'warnning');
		 return false;
	}

	if(departureCity=="-1"){
	   top.$.jBox.tip("请选择出发城市!", 'warnning');
	    return false;
    }
	if(arrivedCity=="-1"){
	   top.$.jBox.tip("请选择到达城市!", 'warnning');
	    return false;
    }
	if(reservationsNum==""){
	   top.$.jBox.tip("请选择预收人数!", 'warnning');
	    return false;
    }

	//离境口岸必填提示
	if($outArea.is(":visible") && ($outArea.val() == "-1")){
		top.$.jBox.tip("请选择离境口岸!", 'warnning');
	     return false;
	}



	//验证出发时刻不能小于当前时间
	var CurrentDate = "";
	var yy =0;
	var mon =0;
	var da =0;
	var dat = new Date();
	var hh = dat.getHours();
	var mm = dat.getMinutes();
	var ss = dat.getTime() % 60000;
	ss= (ss - (ss % 1000)) / 1000;
	var clock = hh+':';
	if (mm < 10) clock += '0';
	clock += mm;
	var startTime = $("#"+parForm).find("input[name='startTime']").val();
	yy = dat.getFullYear();
	mon = dat.getMonth()+1;
	da = dat.getDate();
	CurrentDate += yy + "-";
    if (mon >= 10 )
    {
     CurrentDate += mon + "-";
    }
    else
    {
     CurrentDate += "0" + mon + "-";
    }
    if (da >= 10 )
    {
     CurrentDate += da ;
    }
    else
    {
     CurrentDate += "0" + da;
    }
	var nowDate = CurrentDate+" "+clock;
	if(startTime<nowDate){
		//0246 懿洋假期取消该校验 changed by ang.gao 2016/4/20
		if(companyUuid != 'f5c8969ee6b845bcbeb5c2b40bac3a23'){
			//增加需求0470by2016-06-27
			if(companyUuid != 'dfafad3ebab448bea81ca13b2eb0673e') {
				top.$.jBox.tip("出发时刻不能小于当前时间!", 'warnning');
				return false;
			}
		}
	}

	if($("#companyUUID").val()=='7a81b21a77a811e5bc1e000c29cf2586'){
		var airticketId = $("#"+parForm).find("input[name='txt_ticketId']").val();
		if(airticketId == null || airticketId ==''){
			var flag=false;
			var groupDate=startTime.substring(0,10);
			$.ajax({
				type:"POST",
				async : false,
				url:root+"/activity/manager/getGroupNum",
				data:{
					deptId : "DX",
					groupOpenDate : groupDate
				},
				success:function(result){
					if(result==""){
						top.$.jBox.tip("此日期已发布999条产品，请重新选择出发时刻!", 'warnning');
					}else{
						flag=true;
					}
				}
			});
			if(!flag){
				 return false;
			}
		}
	}

	//出票日期
	var outTicketTime = $("#"+parForm).find("input[name='outTicketTime']").val();
	var startTime2 = startTime.substring(0,10);
	if(outTicketTime>startTime2){
		//增加需求0470by2016-06-27
		if(companyUuid != 'dfafad3ebab448bea81ca13b2eb0673e') {
			top.$.jBox.tip("出票日期不能晚于出发时刻!", 'warnning');
			return false;
		}
	}else if(outTicketTime<nowDate){
		//0246 懿洋假期取消该校验 changed by ang.gao 2016/4/20
		if(companyUuid != 'f5c8969ee6b845bcbeb5c2b40bac3a23'){
			//增加需求0470by2016-06-27
			if(companyUuid != 'dfafad3ebab448bea81ca13b2eb0673e') {
				top.$.jBox.tip("出票日期不能早于当前时间!", 'warnning');
				return false;
			}
		}
	}

	////出发时刻不能晚于到达时刻
	//var arrivalTime = $("#"+parForm).find("input[name='arrivalTime']").val();
	//if(startTime>arrivalTime){
	//	top.$.jBox.tip("出发时刻不能晚于到达时刻!", 'warnning');
	//	 return false;
	//}
	//
	//if(airTypeVal==2){
	//	var wfstartTime  = $("#wfstartTime").val();
	//	if(wfstartTime<startTime){
	//		top.$.jBox.tip("返程的出发时刻不能早于去程的出发时刻!", 'warnning');
	//		 return false;
	//	}
	//}
	//if(airTypeVal==1){
	//	var ddstarttime  = $("#ddstarttime").val();
	//	if(ddstarttime<startTime){
	//		top.$.jBox.tip("第二段的出发时刻不能早于第一段的出发时刻!", 'warnning');
	//		 return false;
	//	}
	//}
    //时间比较 1多段


	//机场
	$("#"+parForm).find("input[name='leaveAirport']").each(
		function(){
			if($(this).attr("id")!="mleaveAirportxId"){
				if($(this).val()==""||$(this).val().indexOf("air")!=0){//机场ID前缀以air开头，非air不是机场
					top.$.jBox.tip("请选择出发机场!", 'warnning');
					throw "请选择出发机场！";
				}
			}
		}
	);
	//机场
	$("#"+parForm).find("input[name='desAirpost']").each(
		function(){
			if($(this).attr("id")!="mdesAirpostxId"){
				if($(this).val()==""||$(this).val().indexOf("air")!=0){//机场ID前缀以air开头，非air不是机场
					top.$.jBox.tip("请选择到达机场!", 'warnning');
					throw "请选择到达机场！";
				}
			}
		}
	);

	var intermodalCity="";//联运城市
	var intermodalCurrency="";//联运价格
	var intermodalPrice="";//联运价格

	var intermodalType=$("#"+parForm).find("select[name='intermodalType']").val();//联运类型
	if("1"==intermodalType){//全国联运
		intermodalCity="全国联运";
		intermodalCurrency=$("#"+parForm).find("div[id='nationalTrans']").find("select[name='selectCurrency']").val();
		intermodalPrice=$("#"+parForm).find("div[id='nationalTrans']").find("input[name='intermodalAllPrice']").val();
		if(intermodalPrice==""){
			top.$.jBox.tip("请填写全国联运价格!", 'warnning');
			    return false;
		}


	}else if("2"==intermodalType){//分区联运
		$("#"+parForm).find("div[id='groupTrans']").find("input[name='intermodalGroupPart']").each(  //联运城市
			function(){
				if($(this).val()==""){
					top.$.jBox.tip("请选择分区联运!", 'warnning');
					throw "请选择分区联运！";
				}else{
					intermodalCity+=$(this).val()+",";
				}
			}
		);
		$("#"+parForm).find("div[id='groupTrans']").find("select[name='selectCurrency']").each(  //联运币种
			function(){
				intermodalCurrency+=$(this).val()+",";
			}
		);
		$("#"+parForm).find("div[id='groupTrans']").find("input[name='intermodalGroupPrice']").each(  //联运价格
			function(){
				if($(this).val()==""){
					top.$.jBox.tip("请选择分区联运价格!", 'warnning');
					throw "请选择分区联运价格！";
				}else{
					intermodalPrice+=$(this).val()+",";
				}
			}
		);
	}else{//无联运
		intermodalCity="";
		intermodalCurrency="";
		intermodalPrice="";
	}
	//alert(intermodalCity+intermodalCurrency+intermodalPrice);
	$("#"+parForm).find("input[name='intermodalInfo']").val(intermodalCity+"#"+intermodalCurrency+"#"+intermodalPrice);//设置联运信息

	var payModeDeposit = $("#"+parForm).find("input[name='payMode_deposit']:checked").val();
	var payModeAdvance = $("#"+parForm).find("input[name='payMode_advance']:checked").val();
	var payModeFull = $("#"+parForm).find("input[name='payMode_full']:checked").val();
	var payModeOp = $("#"+parForm).find("input[name='payMode_op']:checked").val();
	var payModeCw = $("#"+parForm).find("input[name='payMode_cw']:checked").val();
	var remainDaysAdvance = $("#"+parForm).find("input[name='remainDays_advance']").val();
	var remainDaysAdvanceHour = $("#"+parForm).find("input[name='remainDays_advance_hour']").val();
	var remainDaysAdvanceMin = $("#"+parForm).find("input[name='remainDays_advance_fen']").val();

	var remainDays = $("#"+parForm).find("input[name='remainDays_deposit']").val();
	var remainDaysHour = $("#"+parForm).find("input[name='remainDays_deposit_hour']").val();
	var remainDaysMin = $("#"+parForm).find("input[name='remainDays_deposit_fen']").val();

	if(!payModeDeposit && !payModeAdvance && !payModeFull && !payModeOp && !payModeCw){
	   top.$.jBox.tip("请选择付款方式!", 'warnning');
	    return false;
    }

	if(remainDays=="") remainDays=0;
	if(remainDaysHour=="") remainDaysHour=0;
	if(remainDaysMin=="") remainDaysMin=0;
	if(remainDaysAdvance=="") remainDaysAdvance=0;
	if(remainDaysAdvanceHour=="") remainDaysAdvanceHour=0;
	if(remainDaysAdvanceMin=="") remainDaysAdvanceMin=0;

	if(payModeDeposit && remainDays==0 && remainDaysHour==0 && remainDaysMin==0){
		   top.$.jBox.tip("请选择订金占位保留时限!", 'warnning');
		    return false;
	 }

	if(payModeAdvance && remainDaysAdvance==0 && remainDaysAdvanceHour==0 && remainDaysAdvanceMin==0){
	   top.$.jBox.tip("请选择预占位保留时限!", 'warnning');
	    return false;
    }

	if(payModeDeposit && parseInt(remainDaysHour)>23){
		top.$.jBox.tip("请输入正确的订金占位保留小时数! 为0~23的正整数", 'warnning');
		 return false;
	}
	if(payModeDeposit && parseInt(remainDaysMin)>59){
		top.$.jBox.tip("请输入正确的订金占位保留分钟数! 为0~59的正整数", 'warnning');
		 return false;
	}
	if(payModeDeposit && parseInt(remainDaysAdvanceHour)>23){
		top.$.jBox.tip("请输入正确的预占位保留小时数! 为0~23的正整数", 'warnning');
		 return false;
	}
	if(payModeDeposit && parseInt(remainDaysAdvanceMin)>59){
		top.$.jBox.tip("请输入正确的预占位保留分钟数! 为0~59的正整数", 'warnning');
		return false;
	}

	//var jsonData={"xlid":"cxh","xldigitid":123456,"topscore":2000,"topplaytime":"2009-08-20"};

	//var airType=window.location.href = "../airTicket/secondform.htm";
}

//订单占位
function paychg(obj) {
	
	
	if($(obj).prop("checked")){
		$(obj).next().next().find("span").css("display","inline");
		//$(obj).next().next().next().find("input[name^='remainDays']").rules("remove");
    	$(obj).next().next().next().find("input[name^='remainDays']").prop("disabled",false);
    	$(obj).next().next().next().next().find("input[name^='remainDays']").prop("disabled",false);
    	$(obj).next().next().next().next().next().find("input[name^='remainDays']").prop("disabled",false);
		//$(obj).next().next().next().find("input[name^='remainDays']").rules("add",{required:true,digits:true,nonzero:true,messages:{required:"必填信息",digits:"请输入正确的数字",nonzero:"输入不能为0"}});
	}else{
		$(obj).next().next().find("span").css("display","none");
		//$(obj).next().next().next().find("input[name^='remainDays']").rules("remove");
    	$(obj).next().next().next().find("input[name^='remainDays']").val("");
    	$(obj).next().next().next().next().find("input[name^='remainDays']").val("");
    	$(obj).next().next().next().next().next().find("input[name^='remainDays']").val("");
    	
    	$(obj).next().next().next().find("input[name^='remainDays']").prop("disabled",true);
    	$(obj).next().next().next().next().find("input[name^='remainDays']").prop("disabled",true);
    	$(obj).next().next().next().next().next().find("input[name^='remainDays']").prop("disabled",true);
	}
}

//更改联运城市为离境口岸 
function transportCity(){
	var airTypeVal=$("input[name='airType_radio']:checked").val();
	var parForm;
	if(airTypeVal==1){//多段
		parForm="frm1";
	}else if(airTypeVal==2){//往返 
		parForm="frm2";
	}else{//单程
		parForm="frm3";
	}
	if("0"!=$("#"+parForm).find("select[name=intermodalType]").val())//为非联运时，出发城市改为离境口岸
		$("#"+parForm).find("select[name=departureCity]").val($("#"+parForm).find("select[name=outArea]").val());
}

function outAreachg(){
//	var airTypeVal=$("input[name='airType_radio']:checked").val();
//	var parForm;
//	if(airTypeVal==1){//多段
//		parForm="frm1";
//	}else if(airTypeVal==2){//往返 
//		parForm="frm2";
//	}else{//单程
//		parForm="frm3";
//	}
//	if("0"!=$("#"+parForm).find("select[name=intermodalType]").val())//为非联运时，出发城市改为离境口岸
//		$("#"+parForm).find("select[name=departureCity]").val($("#"+parForm).find("select[name=outArea]").val());
}



//联运
function transportchgAir(obj){
//	var objval = obj.value;
//	var inquiry_radio_flights1 = $("#inquiry_radio_flights1").val(); //往返
//	var inquiry_radio_flights2 = $("#inquiry_radio_flights2").val(); //单程
//	var inquiry_radio_flights3 =  $("#inquiry_radio_flights3").val(); //多段
//	if(objval==1 || objval==2 && inquiry_radio_flights1==2){
//		$("#outAreadiv").show();
//	}else{
//		$("#outAreadiv").hide();
//	}
//	if(objval==1 || objval==2 && inquiry_radio_flights3==1){
//		$("#outAreadiv2").show();
//	}else{
//		$("#outAreadiv2").hide();
//	}
//	if(objval==1 || objval==2 && inquiry_radio_flights2==3){
//		$("#outAreadiv1").show();
//	}else{
//		$("#outAreadiv1").hide();
//	}

	
	var value=$(obj).find("option:selected").attr("id");
	//全国联运
	var $nationalTrans = $(obj).parent().next();
	//分区联运
	var $groupTrans = $(obj).parent().next().next();
	//离境口岸
	var $outAreaMust = $(obj).parent().parent().prev().prev();
	//var $outAreaMust = $("#outArea").prev("p").children(".xing");
	if("none" == value){
		$nationalTrans.hide();
		$groupTrans.hide();
		$outAreaMust.hide();
	} else if ("group" == value){
		$nationalTrans.hide();
		$groupTrans.show();
		$outAreaMust.show();
	} else if("national" == value){
		$nationalTrans.show();
		$groupTrans.hide();
		$outAreaMust.show();
	}else{
		$nationalTrans.hide();
		$groupTrans.hide();
		$outAreaMust.hide();
	}
	
	//transportCity();
}

//页面加载，表单元素与相关内容的显示隐藏初始化
function initCreateAirForm(){
	//刷新页面，根据“联运类型”select，设置“离境口岸”和联运显示内容
	$("[name='intermodalType']").each(function(index,obj){
		var value=$(obj).find("option:selected").attr("id");
		//全国联运
		var $nationalTrans = $(obj).parent().next();
		//分区联运
		var $groupTrans = $(obj).parent().next().next();
		//离境口岸
		var $outAreaMust = $(obj).parent().parent().prev().prev();
		if("none" == value){
			$nationalTrans.hide();
			$groupTrans.hide();
			$outAreaMust.hide();
		} else if ("group" == value){
			$nationalTrans.hide();
			$groupTrans.show();
			$outAreaMust.show();
		} else if("national" == value){
			$nationalTrans.show();
			$groupTrans.hide();
			$outAreaMust.show();
		}else{
			$nationalTrans.hide();
			$groupTrans.hide();
			$outAreaMust.hide();
		}
	});
	//付款方式
	$(".add-paytype input[type='checkbox']").each(function(index,element){
		//保留天数必填项标志
		var $xing = $(element).next().next().children(".xing");
		//保留天数文本框
		var $txt = $(element).next().next().next().find(".ui-spinner-input");
		if($(element).is(":checked")){
			$xing.show();
			$txt.prop("disabled",false);
		}else{
			$xing.hide();
			$txt.prop("disabled",true);
		}
	});
}
//新行者团号生成规则 addby jiachen
function getMaxCount(ctx) {
	var groupNum = '';
	//新行者团号生成规则
	if($("#groupCodeRule").length != 0) {
		var groupOpenDate = "";
		
		if($("#groupCodeRule").children("option:selected").text() == 'BJ-SP' || $("#secondStepEnd").find("[name='groupCode'][value*='BJ-DT'],[value*='BJ-DB'],[value*='BJ-ZM']").length == 0) {		
			$.ajax({
				type : "POST",
				async : false,
				url : ctx + "/activity/manager/getCurrentDateMaxGroupCode?groupOpenDate=" + groupOpenDate,
				success : function(result){
					groupNum = result;
				}
			});
		//如果是新行者的规则再次添加团期，则需要在本地累加，不从数据库获取。
		}else{
			var countArr = groupNum.split("-");
			var count = Number(countArr[countArr.length - 1]) + 1;
			if(1 == count.toString().length){
				countArr[countArr.length - 1] = "000" + count;
			}else if(2 == count.toString().length){
				countArr[countArr.length - 1] = "00" + count;
			}else if(3 == count.toString().length){
				countArr[countArr.length - 1] = "0" + count;
			}else{
				countArr[countArr.length - 1] = count;
			}
			groupNum = countArr.join("-");
		}
		$("#groupCode").val( $("#groupCodeRule option:selected").text()+"-"+groupNum );
	}
}

//是否符合金额规则
function isMoney(obj){
			var rr = $(obj).val();
			var rule = /^[^0-9|\.]$/;
			if(rr.length>0){
				var newStr='';
				//过滤掉非字（不过滤小数点）
				for(var i=0;i<rr.length;i++){
					var c = rr.substr(i,1);
					if(!rule.test(c)){
						newStr+=c;
					}
				}
				if(newStr!=''){
					//只能有一个小数点，并去掉多余的0
					var szfds = newStr.split('.');
					var zs = '';
					var xs = '';
					if(szfds.length>1){
						zs=szfds[0];
						xs=szfds[1];
						for(var i=1;i<zs.length;i++){
							var zs_char = zs.substr(0,1);
							if(zs_char=='0'){
								zs = zs.substring(1,zs.length);
							}
						}
						//保留两位小数
						if(xs.length>2){
							xs = xs.substring(0,2);
						}
						newStr = zs+'.'+xs;
					}else{
						zs=szfds[0];
						for(var i=1;i<zs.length;i++){
							var zs_char = zs.substr(0,1);
							if(zs_char=='0'){
								newStr = zs.substring(1,zs.length);
							}
						}
					}
											
					//'.'之前没有数字会自动补0
					if(newStr.indexOf('.')==0){
						newStr='0'+newStr;
					}
				}
				
				
				$(obj).val(newStr);//asdf090980123
			}
		}
/**
 * 是否要继续的提示框
 */
function tipFunction(parForm,msg)
{
    $.jBox.confirm(msg, "系统提示", function(v, h, f){
        if (v == 'ok') {
        	//alert('继续！');
            getMaxCount(root);
            $("#"+parForm).submit();
        }else if (v == 'cancel'){
        	//alert('返回修改！');
        }
    },{ buttons: {'继续': 'ok', '返回修改': 'cancel'}});//20160118 modified by wxw
}

function  validateDate()
{
    if(false ==next())
    return false;
    var airTypeVal=$("input[name='airType_radio']:checked").val();
    var parForm;
    if(airTypeVal==1){//多段
        $("#hiddenFilght").remove();//多段提交删除隐藏域
        parForm="frm1";
    }else if(airTypeVal==2){//往返
        parForm="frm2";
    }else{//单程
        parForm="frm3";
    }
    var starTimeArray =  new Array();
    var arriveTimeArray = new Array;
    if(1 == airTypeVal)
    {
        $("#frm1").find("input[name=startTime]").each(function(index,element){
            starTimeArray.push($(element).val());
            arriveTimeArray.push($(element).parent().parent().next().find("input[name=arrivalTime]").val());
        });
        //开始和开始比
        for(var  i=0;i<$(starTimeArray).length-1;i++)
        {
            if(starTimeArray[i] >starTimeArray[i+1])
            {
                tipFunction(parForm,"第"+(i+2)+"段的出发时刻早于第"+(i+1)+"段的出发时刻");
                return false;
            }
        }
        //开始和到达比
        for(var  i=0;i<$(starTimeArray).length;i++)
        {
            if(starTimeArray[i] >arriveTimeArray[i])
            {
                tipFunction(parForm,"第"+(i+1)+"段的到达时刻早于出发时刻");
                return false;
            }
        }
    }
    //2往返
    else if (2 == airTypeVal)
    {
        $("#frm2").find("input[name=startTime]").each(function(index,element){
            starTimeArray.push($(element).val());
            arriveTimeArray.push($(element).parent().parent().next().find("input[name=arrivalTime]").val());
        });

        //开始和开始比
        for(var  i=0;i<$(starTimeArray).length-1;i++)
        {
            if(starTimeArray[i] >starTimeArray[i+1])
            {
                tipFunction(parForm,"返程的出发时刻早于去程的出发时刻!");
                return false;
            }
        }
        //开始和到达比  
        //debugger;
        //for(var  i=0;i<$(starTimeArray).length-1;i++)
        for(var  i=0;i<$(starTimeArray).length;i++)
        {
            if(starTimeArray[i] >arriveTimeArray[i])
            {
            	//20160118 modified by wxw
            	if(i==0){
            		tipFunction(parForm,"去程的到达时刻早于出发时刻!");
            	}else if(i==1){
            		tipFunction(parForm,"返程的到达时刻早于出发时刻!");
            	}
                
                return false;
            }
        }
        
    }
    else  //单程
    {
        var start_time =$("#frm3").find("input[name=startTime]").val();
        var arrive_time =  $($("#frm3").find("input[name=startTime]")).parent().parent().next().find("input[name=arrivalTime]").val();
        if(start_time > arrive_time)
        {
            tipFunction(parForm,"到达时刻早于出发时刻！");
            return false;
        }

    }
    $("#"+parForm).submit();
}