<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>产品添加</title>
    
       <style>
          .custom-combobox-toggle {
            height: 26px;
            margin-left: -1px;
            padding: 0;
            /* support: IE7 */
            *height: 1.7em;
            *top: 0.1em;
          }
          .custom-combobox-input {
            margin: 0;
            padding: 0.3em;width:100px;
          }
          .ui-autocomplete{height:200px;overflow:auto;}
  </style>
    
    
    <meta name="decorator" content="wholesaler"/>
    <style type="c">.sort{color:#0663A2;cursor:pointer;}</style>
    <script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactor2.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/i18n/jquery-ui-i18n.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-other/jquery-combobox_prop.js" type="text/javascript"></script>
    <script src="${ctxStatic}/common/jquery.disabled.js" type="text/javascript"></script>
    <script src="${ctxStatic}/modules/activity/dynamic.validator.js" type="text/javascript"></script>
    <script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>
    <script src="${ctxStatic}/common/jquery.date.format.js" type="text/javascript"></script>
    <script src="${ctxStatic}/common/jquery.file.filter.js" type="text/javascript"></script>
    
    <script type="text/javascript">
    
    $(function() {
            $( ".spinner" ).spinner({
                spin: function( event, ui ) {
                if ( ui.value > 365 ) {
                    $( this ).spinner( "value", 1 );
                    return false;
                } else if ( ui.value < 0 ) {
                    $( this ).spinner( "value", 365 );
                    return false;
                }
                }
            });
        });
    var index = 0;
    var res;
    var validator1;
    var errorArray = new Array();
    var closeBeforeVal = "";
    var visaBeforeVal = "";
    $(document).ready(function() {
        $(".transport_city").delegate(".transportAdd","click",function(){
            $(this).parent().parent().append('<p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart" name="intermodalGroupPart" maxlength="50" type="text"><label>联运价格：</label><input class="valid rmbp17" id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="8" type="text"><a class="ydbz_s transportAdd">增加</a><a class="ydbz_s transportDel">删除</a></p>')
        });
        $(".transport_city").delegate(".transportDel","click",function(){
            $(this).parent().remove();
        });
        jQuery.extend(jQuery.validator.messages, {
            required: "必填信息",
            digits:"请输入正确的数字",
            number : "请输入正确的数字价格"
        });
        validator1 = $("#addForm").validate({
            rules:{
                activitySerNum:{
                    remote: {
                        type: "POST",
                        url: "${ctx}/activity/manager/serNumRepeat"
                            }
                    }
            },
            messages:{
                activitySerNum:{remote:"编号已存在"}
            },
            errorPlacement: function(error, element) {
                if($(element).attr("id")=="introduction")
                    error.appendTo (element.parent().parent());
                else if($(element).hasClass("spinner"))
                    error.appendTo (element.parent().parent());
                else if ( element.is(":radio") ) 
                    error.appendTo ( element.parent() ); 
                else if ( element.is(":checkbox") ) 
                    error.appendTo ( element.parent() );
                else if ($(element).attr("id")=="targetAreaName")
                    error.appendTo ( element.parent().parent() );
                else if ( element.is("input") ) 
                    error.appendTo ( element.parent() ); 
                else 
                   error.insertAfter(element); 
            },
            showErrors:function(errorMap,errorList){
                this.defaultShowErrors();
                errorArray = errorArray.concat(errorList);
            }
        });
        <%--remove_productinfo();
        add_productinfo();
        $("#addForm").validate({            
            onsubmit:true,          
            errorPlacement: function(error, element) { 
                if ( element.is(":radio") ) 
                    error.appendTo ( element.parent() ); 
                else if ( element.is(":checkbox") ) 
                    error.appendTo ( element.parent() ); 
                else if ( element.is("input") ) 
                    error.appendTo ( element.parent() ); 
                else 
                   error.insertAfter(element); 
            },
            submitHandler: function(form) {
                alert();
                
            }, 
            invalidHandler: function(form, validator) {
                alert("请先修改错误"); 
                return false; 
            }               
        });--%>
        
        var datepicker= $(".groupDate").datepickerRefactor({dateFormat: "yy-mm-dd",
                                                            target:"#dateList",
                                                            dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
                                                            closeText:"关闭", 
                                                            prevText:"前一月", 
                                                            nextText:"后一月",
                                                            monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"],
                                                            minDate:getCurDate(),
                                                            numberOfMonths:3,
                                                            closeBeforeDays:"#closeBeforeDays",
                                                            visaBeforeDays:"#visaBeforeDays",
                                                            visaCountryCopy:"#visaCountryCopy",
                                                            visaCopyBtn:"#visaCopyBtn",
                                                            planPositionCopy:"#planPositionCopy",
                                                            planPositionBtn:"#planPositionBtn",
                                                            freePositionCopy:"#freePositionCopy",
                                                            freePositionBtn:"#freePositionBtn",
                                                            secondStepContent:"#secondStepContent"
                                                            },
                                            "#groupOpenDate","#groupCloseDate","#groupTable");
        
<%--        $("#activityDuration").val(1);          --%>
        $("#targetAreaName").change(function(){
            $("#targetAreaName").trigger("onblur");
        });
        $("#targetAreaId").val("${travelActivity.targetAreaIds}");
        $("#targetAreaName").val("${travelActivity.targetAreaNamess}");         
<%--        $("#activityLevelId").combobox({"title": "显示所有","target":"product_level"});--%>
<%--        $("#activityTypeId").combobox({"title": "显示所有","target":"product_type"});--%>
        $("#secondStepDiv").css("display","none");
        $("#thirdStepDiv").css("display","none");
        $("input[name='product_level']").css("line-height","25px");
        $("input[name='product_type']").css("line-height","25px");
        
        //产品名称输入长度  
        $("#acitivityName").live("keyup",function(){
            getAcitivityNameLength();
        });
        $("#acitivityName").live("blur",function(){
            getAcitivityNameLength();
        });
        clearValue();
        function getAcitivityNameLength() {
            var acitivityNameLength = 50-($("#acitivityName").val().length);
            if(acitivityNameLength>=0){
                $(".acitivityNameSize").text(acitivityNameLength);
            }
        }
    });
    
        function showGroupDate(){
            $(".groupDate").datepickerRefactor({dateFormat: "yy-mm-dd",
                target:"#dateList",
                dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
                closeText:"关闭", 
                prevText:"前一月", 
                nextText:"后一月",
                monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"],
                minDate:getCurDate(),
                closeBeforeDays:"#closeBeforeDays",
                visaBeforeDays:"#visaBeforeDays",
                visaCountryCopy:"#visaCountryCopy",
                visaCopyBtn:"#visaCopyBtn",
                planPositionCopy:"#planPositionCopy",
                planPositionBtn:"#planPositionBtn",
                secondStepContent:"#secondStepContent"
                },
                "#groupOpenDate","#groupCloseDate","#groupTable");
        }
        function addfile(obj){
            var file = "<div style=\"margin-top:8px;\">"+
                        $("#othertemplate").clone().html()+                                 
                        "</div>";
            
            $(obj).parent().parent().append(file);
        }
        function addvisafile(obj){
            var html = 
            "<div id=\"visafile\" class=\"mod_information_d8_2\" style=\"margin-top:5px;\">"+$("#signtemplate").clone().html()+"</div>";
            $("#otherflag").prev().prev().after(html);

            $("#thirdStepDiv .mod_information_d8_2 select[name='country']").combobox();
        }
        function trafficchg(){
            var value=$("#trafficMode option:selected").val();
            if("${relevanceFlagId}".indexOf(value)>=0&&value!="")
                $("#trafficName").css("display","inline-block");
            else
                $("#trafficName").css("display","none");
        }

        function paychg(obj) {
        	/*
        	if($(obj).prop("checked")) {
        		$("#advance_xing").show();
        		$("#remainDays_advance").rules("remove"); 
        		$("#remainDays_advance").rules("add",{required:true,digits:true,nonzero:true,messages:{required:"必填信息",digits:"请输入正确的数字",nonzero:"输入不能为0"}});
        	} else {
        		$("#advance_xing").hide();
        		$("#remainDays_advance").rules("remove"); 
        	} */
        	if($(obj).prop("checked")){
        		$(obj).next().next().find("span").css("display","inline");
        		$(obj).next().next().next().find("input[name^='remainDays']").rules("remove");
            	$(obj).next().next().next().find("input[name^='remainDays']").removeAttr("disabled");
        		$(obj).next().next().next().find("input[name^='remainDays']").rules("add",{required:true,digits:true,nonzero:true,messages:{required:"必填信息",digits:"请输入正确的数字",nonzero:"输入不能为0"}});
        	}else{
        		$(obj).next().next().find("span").css("display","none");
        		$(obj).next().next().next().find("input[name^='remainDays']").rules("remove");
            	$(obj).next().next().next().find("input[name^='remainDays']").val("");
            	$(obj).next().next().next().find("input[name^='remainDays']").attr("disabled","disabled");
        	}
        }
        function removefile(msg,obj){
            top.$.jBox.confirm(msg,'系统提示',function(v){
                if(v=='ok'){
                    var divobj = $(obj).parent().parent().parent().parent().parent();
                    $(divobj).remove();
                }
                },{buttonsFocus:1});
                top.$('.jbox-body .jbox-icon').css('top','55px');
                return false;           
        }
        //删除团期
        function delGroupDate(obj){
            $(document).delGroup1(obj);
        }
        function writeGroupDate(){
            $(document).writeDate();
        }
        
        //控制材料截止日期
        function takeVisaDate(obj){
            var getGroupCD = $(obj).attr("id");
            var index = getGroupCD.substring(8,getGroupCD.length);
            var strGroupOD = "#groupOpenDate";
            var groupOD = strGroupOD.concat(index);
            var OpenDate = $(groupOD).val();
            return OpenDate;
        }
        //验证日期合法        
        function getMinDate(obj){
            
            var getGroupCD = $(obj).attr("id");
            var index = getGroupCD.substring(14,getGroupCD.length);
            var strGroupOD = "#groupOpenDate";
            var groupOD = strGroupOD.concat(index);
            var maxDate = $(groupOD).val();
            return maxDate;
        
        }
        function getCurDate(){
            var dateStr = '1970-01-01';
            return dateStr;
        }
            
        function getDateOVId(obj){
         var getGroupCD = $(obj).attr("id");
         var index = getGroupCD.substring(8,getGroupCD.length);
         var strGroupOD = "#groupOpenDate";
         var groupOD = strGroupOD.concat(index);
         var groupVD = strGroupOD.charAt(0).concat(getGroupCD);
         
       
        }
        function selfDefine(obj,type,soure){
            if($("#"+type).css("display")=="none"){
                $(obj).html("取消");
                $("#"+type).css("display","inline-block");
                $("#"+type).attr("disabled",false);
                $("#"+soure).css("display","none");
                $("#"+soure).attr("disabled",true);
            }else{
                $(obj).html("自定义");
                $("#"+type).css("display","none");
                $("#"+type).attr("disabled",true);
                $("#"+soure).css("display","inline-block");
                $("#"+soure).attr("disabled",false);
            }
            
        }
        //空位数量初始等于预收人数
        function comparePosition(obj){
            var plan = $(obj).val();
            $(obj).parent().next().find("input").val(plan);
            $(obj).parent().next().find("input").focus();
            $(obj).parent().next().find("input").blur();
        }
        function inFileName(obj){
            
            var flag = $(obj).fileInclude({includes:[".doc",".docx"]});
            var dest = $(obj).parent().parent().find("input[name='fileLogo']")[0];
            if(flag){
                var res = $(obj).val();             
                $(dest).val(res);
            }else{
                $(obj).val("");
                $(dest).val("");
                top.$.jBox.info("文件上传仅支持带有.doc,.docx后缀名的文件", "警告");
                top.$('.jbox-body .jbox-icon').css('top','55px');
            }
            
        }
        
        var submit_times = 0;
        function submitForm(status) {
            remove_productinfo();
            remove_groupsvalidator();
            add_filevalidator();
            var flag = validator1.form();
            if(flag) {
                if(submit_times == 0) {
                	submit_times++;
                    $("#addForm").attr("action","${ctx}/activity/manager/saveTemp?activityStatus="+status);
                    $("#addForm").submit();
                }
            } else {
                top.$.jBox.info("请先修改完错误再提交", "警告");
                top.$('.jbox-body .jbox-icon').css('top','55px');
            }
        }
        
        function removeCodeCss(obj){
            $(obj).removeAttr("style");
        }
        function showErrorDiv(){
            if(errorArray.length!=0){
                if($("#showErrorDiv").length!=0)
                    $("#showErrorDiv").remove();
                var html = $("<div id=\"showErrorDiv\" class=\"show_m_div\"></div>");
                var innerhtml = $("<div class=\"show_m_div_title\">提示信息</div>");
                var closespn = $("<span title=\"关闭\" class=\"show_m_div_close\" >&times;</span>");
                $(closespn).bind("click",function(){
                    $("#showErrorDiv").remove();
                });
                $(innerhtml).append(closespn);
                $(html).append(innerhtml);
                var ul = $("<ul></ul>");
                var checkbox = 1;
                $(errorArray).each(function(index,obj) {
                    if(obj.element.type == 'checkbox' && checkbox == 1) {
                    	var msg = obj.message;
                    	var btn = $("<input type='button' value='修改' />");
                        btn.click(function(){
                            $(obj.element).focus();
                        });
                        var li = $("<li></li>").append("<em>*" + msg + "</em>").append(btn);
                        $(ul).append(li);
                        checkbox++;
                    } else if(obj.element.type != 'checkbox') {
                    	var id = obj.element.id;
                        var msg = obj.message;
                        var name = getErrorName(id);
                        var btn = $("<input type='button' value='修改' />");
                        btn.click(function(){
                            $(obj.element).focus();
                        });
                        var li = $("<li></li>").append("<em>*" + name + " 为" + msg + "</em>").append(btn);
                        $(ul).append(li);
                    }
                });
                $(html).append(ul);
                html.appendTo(document.body);
            }
        }
    	// 判断，如果支付方式没有被选中，则清空等待时间
    	function clearValue(){
    		$("input[name='payMode']").each(function(){
    			if($(this).prop("checked")){
    				$(this).next().next().find("span.xing").show();
    			}else{
    				$(this).next().next().next().find("input[name^='remainDays']").val("");
    	        	$(this).next().next().next().find("input[name^='remainDays']").attr("disabled","disabled");
    	        	$(this).next().next().find("span.xing").hide();
    			}
    		});
    	}
        function oneToSecond(){
            var priceInput = document.getElementsByName('intermodalGroupPrice');
            var priceInputAll = document.getElementsByName('intermodalAllPrice');
            for(var i = 0; i < priceInput.length; i++){
                if($.trim(priceInput[i].value) == ''){
                    priceInput[i].value = 0;
                }
            }
            if($.trim(priceInputAll[0].value) == ''){
                priceInputAll[0].value = 0;
            }
            remove_filevalidator();
            remove_groupsvalidator();
            errorArray = new Array();
            add_productinfo();
            var flag = validator1.form();           
            if(flag){
            	clearValue();            
                var spn = $("<span style=\"float: right;padding-right: 10px;\">关闭</span>");
                $("#oneStepTitle").append(spn); 
                spn.toggle(
                        function(){
                            $(this).text("展开");
                            $("#oneStepContent").hide();
                        },
                        function(){
                            $(this).text("关闭");
                            $("#oneStepContent").show();
                        });
                $(".divDurationDays").children("span").removeClass();
                $(".divRemainDaysDeposit").children("span").removeClass();
                $(".divRemainDaysAdvance").children("span").removeClass();           
                $("#oneStepDiv").disableContainer({blankText : "",tipTarget:["targetAreaName"]});
                disabledcheckbox();
                $("#oneStepBtn").hide();
                $(".acitivityNameSizeSpan").hide();
                $("#dh").removeClass("add_img").addClass("add2_img");
                $("#secondStepDiv").show();
                $("#thirdStepDiv").hide();
                if($("#showErrorDiv").length!=0)
                    $("#showErrorDiv").remove();
                subCode(true,false);
                
            }else{
                showErrorDiv();
                
            }
        }
        
      //产品基本信息-->添加团期和价格（隐藏并修改样式）
        function disabledcheckbox() {
        	$("#payModeText").html("");
            $(":checkbox[name='payMode']").each(function(index, obj) {
//            	$(this).hide();
//            	$(this).next("font").hide();
//            	$("[id^=label]").hide();
//            	$("#payMode_deposit_span").hide();
//            	$(this).next().next().next("span").hide();
                $(this).hide();
                $(this).next("font").hide();
                $("[id^=label]").hide();
                $("span.payModeSpan").css("display","none");
                $(this).next().next().next("span").hide();
                if($(this).prop("checked")) {
                	var payModeHtml = $("#payModeText").html();
                	if(payModeHtml != "") {
                    	if($(this).val() != 3 && $(this).val() != 7) {
                    		var remainDays = $("[name$=" + $(this).attr("id").split("_")[1] + "]").val();
                    		if(remainDays != '') {
                    			payModeHtml += ",&nbsp;&nbsp;" + $(this).next("font").text().replace(/(^\s*)|(\s*$)/g,'') + "（保留" +  remainDays + "天）";
                        	} else {
                        		payModeHtml += ",&nbsp;&nbsp;" + $(this).next("font").text().replace(/(^\s*)|(\s*$)/g,'');
                            }
                        } else {
                        	payModeHtml += ",&nbsp;&nbsp;" + $(this).next("font").text();
                        }
                	} else {
                		if($(this).val() != 3 && $(this).val() != 7) {
                    		var remainDays = $("[name$=" + $(this).attr("id").split("_")[1] + "]").val();
                    		if(remainDays != '') {
                    			payModeHtml = $(this).next("font").text().replace(/(^\s*)|(\s*$)/g,'') + "（保留" +  remainDays + "天）";
                        	} else {
                        		payModeHtml = $(this).next("font").text().replace(/(^\s*)|(\s*$)/g,'');
                            }
                        } else {
                        	payModeHtml = $(this).next("font").text().replace(/(^\s*)|(\s*$)/g,'');
                        }
                    }
                	$("#payModeText").html(payModeHtml).show();
                }
            });
        }


        function removedisabledcheckbox() {
        	$("#payModeText").hide();
            $(":checkbox[name='payMode']").each(function(index, obj) {
            	/*
            	$(this).show();
            	$(this).next("font").show();
            	$("[id^=label]").show();
            	$("#payMode_deposit_span").show();
            	$("#payMode_advance_span").show();
            	*/
            	$("#payMode_deposit_span").show();
            	$(this).show();
            	$(this).next("font").show();
                $("[id^='label']").show();
            	$(this).next().next().next().show();
            	if($(this).prop("checked")){
            		$(this).next().next().find("span.xing").show();
            	}else{
            		$(this).next().next().find("span.xing").hide();
            	}
            });
        }
        
        //判断产品编号和团号是否需要截取：超过10位的要截取
	    function subCode(activitySerNum,groupCode) {
	        if(activitySerNum) {
	            //判断产品编号：超过20位的截取然后加冒号
	            var activitySerNum = $("#activitySerNum").siblings("span").html();
	            if(activitySerNum && activitySerNum.length >20) {
	                activitySerNum_temp = activitySerNum.substring(0,20) + "......";
	                $("#activitySerNum").siblings("span").html(activitySerNum_temp);
	                $("#activitySerNum").siblings("span").wrapInner("<a href='###' style='text-decoration: none; color:inherit; cursor:default;' title='" + activitySerNum + "'></a>");
	             }
	        }
	        if(groupCode) {
	            //判断团期编号：超过10位的截取然后加冒号
	            var groupCodeArr = $("[name='groupCode']").siblings(".disabledshowspan");
	            $(groupCodeArr).each(function() {
	                var groupCode = $(this).html();
	                if(groupCode && groupCode.length >10) {
	                    groupCode_temp = groupCode.substring(0,10) + "......";
	                    $(this).html(groupCode_temp);
	                    $(this).wrapInner("<a href='###' style='text-decoration: none; color:inherit; cursor:default;' title='" + groupCode + "'></a>");
	                 }
	            });
	        }
	    }       
        
        function secondToThird(){
            remove_productinfo();
            remove_filevalidator();
            errorArray = new Array();           
            add_groupsvalidator();
            var flag = validator1.form();
                    
            var groupCodes = $("#contentTable").find("input[name='groupCode']");
            var submitflag = true;
            var jsonRes = new Array();
            if(groupCodes){
                $(groupCodes).each(function(index,obj){
                    var id = $(obj).attr("id");
                    var input = $(obj).val();
                    if(input!="" && input!=null && input != undefined)
                        jsonRes.push({id:id,value:input});
                });
                $.ajax({
                    type: "POST",
                    url: "${ctx}/activity/manager/groupsplit",
                    dataType:"json",
                    async:false,
                    data:{jsonresult:JSON.stringify(jsonRes)},
                    success: function(result){
                        var tips = "";
                        $(result).each(function(i,obj){
                            var id = obj.id;
                            var flag = obj.flag;
                            if(flag=="false"){
                                submitflag = false;
                                $("#"+id).css("border-color","red");
                                tips += "第"+(i+1)+"行团号发生重复，请重新录入<br>";
                            }
                        });
                        if(!submitflag){
                            top.$.jBox.info(tips, "警告", { width: 250, showType:"slide", icon: "info",draggable: "true" });
                            top.$('.jbox-body .jbox-icon').css('top','55px');
                        }                       
                        if(flag && submitflag){
                            var spn = $("<span style=\"float: right;padding-right: 10px;\">收起</span>");
                            $("#secondStepTitle").append(spn);
                            $("#secondStepContent").hide();
                            spn.toggle(
                                    function(){
                                        $(this).text("展开");
                                        $("#secondStepEnd").hide();
                                    },
                                    function(){
                                        $(this).text("收起");
                                        $("#secondStepEnd").show();
                                    });
                                        
                            $("#secondStepEnd").disableContainer({blankText : ""});
                            $("#secondStepBtn").hide();
                            $("#dh").removeClass("add2_img").addClass("add3_img");
                            $("#thirdStepDiv").show();
                            $("#contentTable tbody").children().find("td:eq(5)").find("span").each(function(index,obj){$(obj).text("¥"+$(obj).text());});
                            $("#contentTable tbody").children().find("td:eq(6)").find("span").each(function(index,obj){$(obj).text("¥"+$(obj).text());});
                            $("#contentTable tbody").children().find("td:eq(7)").find("span").each(function(index,obj){$(obj).text("¥"+$(obj).text());});
                            $("#contentTable tbody").children().find("td:eq(8)").find("span").each(function(index,obj){$(obj).text("¥"+$(obj).text());});
                            $("#contentTable tbody").children().find("td:eq(9)").find("span").each(function(index,obj){$(obj).text("¥"+$(obj).text());});
                            $("#contentTable tbody").children().find("td:eq(10)").find("span").each(function(index,obj){$(obj).text("¥"+$(obj).text());});
                            //subCode(false,true);
                            subCode(false,false);
                        }else{  
                            top.$.jBox.info("请先修改完错误再提交", "警告");
                            top.$('.jbox-body .jbox-icon').css('top','55px');
                        }
                    }
                 });
            }               
            
        }
        function secondToOne(){
            $("#oneStepTitle span:eq(1)").remove();
            $("#oneStepDiv").undisableContainer();
            removedisabledcheckbox();
            $(".divDurationDays").children("span").addClass("ui-spinner ui-widget ui-widget-content ui-corner-all");
            $(".divDurationDays").children("span:last").removeClass();
            $(".divRemainDays").children("span").addClass("ui-spinner ui-widget ui-widget-content ui-corner-all");
            $(".divRemainDays").children("span:last").removeClass();
            $(".divRemainDaysDeposit").children("span").addClass("ui-spinner ui-widget ui-widget-content ui-corner-all");
            $(".divRemainDaysDeposit").children("span:last").removeClass();
            $(".divRemainDaysAdvance").children("span").addClass("ui-spinner ui-widget ui-widget-content ui-corner-all");
            $(".divRemainDaysAdvance").children("span:last").removeClass();
            $("#secondStepEnd").undisableContainer();
            $("#secondStepTitle span:eq(1)").remove();
            $("#secondStepContent").show();
            
            $("#dh").removeClass("add2_img").addClass("add_img");
            $("#oneStepDiv").show();
            $("#oneStepBtn").show();
            $("#secondStepDiv").hide();
            $("#thirdStepDiv").hide();
            $(".acitivityNameSizeSpan").show();
        }
        function ThirdToSecond(){
            $("#secondStepEnd").undisableContainer();
            $("#secondStepEnd").show();
            $("#secondStepTitle span:eq(1)").remove();
            $("#secondStepContent").show();
            $("#secondStepBtn").show();
            $("#dh").removeClass("add3_img").addClass("add2_img");
            $("#thirdStepDiv").hide();
        }
        function getErrorName(id){
            var name = "";
            var reg=new RegExp("^intermodalGroupPart");
            if(reg.test(id)){
                return "联运城市";
            }
            switch(id){
                case "travelTypeId":
                    name = "旅游类型";
                    break;
                case "activitySerNum":
                    name = "产品编号";
                    break;
                case "acitivityName":
                    name = "产品名称";
                    break;
                case "targetAreaName":
                    name = "目的地";
                    break;
                case "activityLevelId":
                    name = "产品系列";
                    break;
                case "activityTypeId":
                    name = "产品类型";
                    break;
                case "outArea":
                    name = "离境口岸";
                    break;
                case "fromArea":
                    name = "出发城市";
                    break;
                case "trafficMode":
                    name = "交通方式";
                    break;
                case "activityDuration":
                    name = "行程天数";
                    break;
                case "remainDays_advance":
                    name = "预占位保留天数";
                    break;
                case "remainDays_data":
                    name = "资料占位保留天数";
                    break;
                case "remainDays_guarantee":
                    name = "担保占位保留天数";
                    break;
                case "remainDays_express":
                    name = "确认单占位保留天数";
                    break;
                case "payMode":
                    name = "付款方式";
                    break;
                default:
                    name = "未知";
                    break;
            }
            return name;
        }
    
        
        
        
          (function( $ ) {
                $.widget( "custom.combobox", {
                  _create: function() {
                    this.wrapper = $( "<span>" )
                      .addClass( "custom-combobox" )
                      .insertAfter( this.element );
             
                    this.element.hide();
                    this._createAutocomplete();
                    this._createShowAllButton();
                  },
             
                  _createAutocomplete: function() {
                    var selected = this.element.children( ":selected" ),
                      value = selected.val() ? selected.text() : "";
             
                    this.input = $( "<input>" )
                      .appendTo( this.wrapper )
                      .val( value )
                      .attr( "title", "" )
                      .addClass( "custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left" )
                      .autocomplete({
                        delay: 0,
                        minLength: 0,
                        source: $.proxy( this, "_source" )
                      })
             
                    this._on( this.input, {
                      autocompleteselect: function( event, ui ) {
                        ui.item.option.selected = true;
                        this._trigger( "select", event, {
                          item: ui.item.option
                        });
                      },
             
                      autocompletechange: "_removeIfInvalid"
                    });
                  },
             
                  _createShowAllButton: function() {
                    var input = this.input,
                      wasOpen = false;
             
                    $( "<a>" )
                      .attr( "tabIndex", -1 )
                      .attr( "title", "选择" )
                      .tooltip()
                      .appendTo( this.wrapper )
                      .button({
                        icons: {
                          primary: "ui-icon-triangle-1-s"
                        },
                        text: false
                      })
                      .removeClass( "ui-corner-all" )
                      .addClass( "custom-combobox-toggle ui-corner-right" )
                      .mousedown(function() {
                        wasOpen = input.autocomplete( "widget" ).is( ":visible" );
                      })
                      .click(function() {
                        input.focus();
             
                        // Close if already visible
                        if ( wasOpen ) {
                          return;
                        }
             
                        // Pass empty string as value to search for, displaying all results
                        input.autocomplete( "search", "" );
                      });
                  },
             
                  _source: function( request, response ) {
                    var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
                    response( this.element.children( "option" ).map(function() {
                      var text = $( this ).text();
                      if ( this.value && ( !request.term || matcher.test(text) ) )
                        return {
                          label: text,
                          value: text,
                          option: this
                        };
                    }) );
                  },
             
                  _removeIfInvalid: function( event, ui ) {
             
                    // Selected an item, nothing to do
                    if ( ui.item ) {
                      return;
                    }
             
                    // Search for a match (case-insensitive)
                    var value = this.input.val(),
                      valueLowerCase = value.toLowerCase(),
                      valid = false;
                    this.element.children( "option" ).each(function() {
                      if ( $( this ).text().toLowerCase() === valueLowerCase ) {
                        this.selected = valid = true;
                        return false;
                      }
                    });
             
                    // Found a match, nothing to do
                    if ( valid ) {
                      return;
                    }
             
                    // Remove invalid value
                    this.input
                      .val( "" )
                      .attr( "title", value + "" )
                      .tooltip( "open" );
                    this.element.val( "" );
                    this._delay(function() {
                      this.input.tooltip( "close" ).attr( "title", "" );
                    }, 2500 );
                    this.input.data( "ui-autocomplete" ).term = "";
                  },
             
                  _destroy: function() {
                    this.wrapper.remove();
                    this.element.show();
                  }
                });
              })( jQuery );
             
        
          $(function() {
                $("#visafile select[name='country']" ).combobox();
              });
          function activitySerNumEmpty() {
              if($("#activitySerNum").val()=="")
                  $("#activitySerNum").val($(".activitySerNumHid").val());
          }
        /*联运*/
        function transportchg(){
            changeFromType();
            var value=$("#intermodalType option:selected").attr("id");
            if("none" == value){
                $('#nationalTrans').hide();
                $('#groupTrans').hide();
    //                $('#intermodalType').parent().next().show();
    //                $('#intermodalType').parent().next().next().hide();
            } else if ("group" == value){
                $('#nationalTrans').hide();
                $('#groupTrans').show();
    //                $('#intermodalType').parent().next().hide();
    //                $('#intermodalType').parent().next().next().show();
            } else if("national" == value){
                $('#nationalTrans').show();
                $('#groupTrans').hide();
    //                $('#intermodalType').parent().next().hide();
    //                $('#intermodalType').parent().next().next().hide();
            }else{
                $('#nationalTrans').hide();
                $('#groupTrans').hide();
            }
        }

    function transportAdd(element, index){
        var middle = 'onkeyup="this.value=this.value.replace(/[^\\d\\+\\-]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^\\d\\+\\-]/g,\'\')"';
        $(element).parent().parent().append('<p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart' + (index+1) + '" name="intermodalGroupPart" maxlength="50" type="text"><label>联运价格：</label><input class="valid rmbp17" id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="8" type="text" ' + middle + ' /><a class="ydbz_s transportAdd" onclick="javascript:transportAdd(this,' + (index+1) + ');">增加</a><a class="ydbz_s transportDel">删除</a></p>');
        $('.transportAdd').attr('onclick', 'transportAdd(this, ' + (index + 1) + ')');
    }
    function changeFromType(){
        var typeValue = $('#intermodalType').val();
        var $outArea = $("#outArea");
        if(typeValue != 1 && typeValue != 2){
            $outArea.rules("remove");
            $outArea.prev("label").children().remove();
            $('#outArea').val("");
            $('#outAreaLabel').hide();
        }else{
            $outArea.prev("label").children().remove();
            $("#outArea").rules("add",{required:true,messages:{required:"必填信息"}});
            $outArea.prev("label").prepend("<span class=\"xing\">*</span>");
            $('#outAreaLabel').show();
        }

    }


    </script>   
   <style type="text/css"> 
    label{cursor:inherit;}
    </style>
</head>
<body>
<page:applyDecorator name="show_head">
    <page:param name="desc">产品补单</page:param>
</page:applyDecorator>
<div class="produceDiv">
<div style="width:100%; height:20px;"></div>
<div id="dh" class="add_img"></div>
    <form:form id="addForm" modelAttribute="travelActivity" action="${ctx}/activity/manager/saveTemp" method="post"
            class=" form-search" enctype="multipart/form-data">
        <tags:message content="${message}" />           
            
        <div id="oneStepDiv" class="mod_information_dzhan">
<%--            <div id="oneStepTitle" class="mod_information_d"><span style="padding-left:20px; font-weight:bold;">填写产品基本信息</span></div>--%>
            <div class="kongr"></div>
            <div class="kongr"></div>
                <div class="mod_information_d7"></div>
                <div class="kongr"></div>
                <div class="kongr"></div>
            <div id="oneStepContent" class="mod_information_dzhan_d error_add1">
                <div class="mod_information_d1">
                    <label><span class="xing">*</span>产品名称：</label>
                    <form:input path="acitivityName" cssClass="inputTxt" maxlength="50"/>
                    <span class="acitivityNameSizeSpan" style="color:#b2b2b2">还可输入<span class="acitivityNameSize">50</span>个字</span>
                </div>
                <div class="kongr"></div>
                <div class="mod_information_d2">
                      <label>产品编号：</label>
                      <input id="activitySerNum" name="activitySerNum" maxlength="500" value="${travelActivity.activitySerNum}" onblur="activitySerNumEmpty()"/>
                      <input type="hidden" class="activitySerNumHid" value="${travelActivity.activitySerNum}"/>
                </div>
                <div class="mod_information_d2">
                     <label><span class="xing">*</span>产品分类：</label>
                      <form:select path="overseasFlag" itemValue="key" >
                        <form:option value="0">国内</form:option>
                        <form:option value="1">国外</form:option>
                    </form:select>
                </div>
                <div class="mod_information_d2">
                     <label><span class="xing">*</span>旅游类型：</label>
                      <form:select path="travelTypeId" itemValue="key" itemLabel="value">
                        <form:option value="">请选择</form:option>
                        <form:options items="${travelTypes}" />
                    </form:select>
                </div>
                
                
                <div class="kongr"></div>
               
                <div class="mod_information_d2">
                     <label><span class="xing">*</span>出发城市：</label>
                     <form:select path="fromArea" itemValue="key" itemLabel="value">
                        <form:option value="">请选择</form:option>
                        <form:options items="${fromAreas}" />
                    </form:select>
                </div>
                <div class="mod_information_d2 ">
                     <label><span class="xing">*</span>目的地：</label>
                     <tags:treeselect id="targetArea" name="targetAreaIdList" value="${targetAreaIds}"  labelName="targetAreaNameList" labelValue="${targetAreaNames}"  
                        title="区域" url="/activity/manager/filterTreeData" checked="true"/>
                </div>
                <div class="mod_information_d2 mod_information_d2jt">
                      <label><span class="xing">*</span>交通方式：</label>
                    <form:select path="trafficMode" itemValue="key" itemLabel="value" onchange="trafficchg()" >
                        <form:option value="">请选择</form:option>
                        <form:options items="${trafficModes}" />
                    </form:select>
                    <form:select id="trafficName" path="trafficName" itemValue="key" itemLabel="value" cssStyle="display:none;width:83px;padding-left:0px;padding-right:0px;">
                       <form:option value="">请选择</form:option>
                        <form:options items="${trafficNames}" />
                    </form:select>
                 </div>
                
                 
                 <div class="kongr"></div>
                 <div class="mod_information_d2">
                       <label><span class="xing">*</span>产品系列：</label>
                       <form:select path="activityLevelId" itemValue="key" itemLabel="value" >
                            <form:option value="">请选择</form:option>
                            <form:options items="${productLevels}" />
                        </form:select>
                 </div>
                  <div class="mod_information_d2">
                      <label><span class="xing">*</span>产品类型：</label>
                      <form:select path="activityTypeId" itemValue="key" itemLabel="value" >
                        <form:option value="">请选择</form:option>
                        <form:options items="${productTypes}" />
                    </form:select>
                </div>
                 
                 <div class="mod_information_d2">
                    <div class="divDurationDays">
                        <label for="spinner"  class="txt2 fl"><span class="xing">*</span>行程天数：</label>
                        <input id="activityDuration" class="spinner" name="activityDuration" maxlength="3" value="" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
                        <span style="padding-left:5px;">天</span>
                    </div>
                 </div>
                 
                 <div class="kongr"></div>

                <div class="mod_information_d2 add-paytype">
                    <label><span class="xing">*</span>付款方式：</label>&nbsp;
                    <input type="checkbox" class="ckb_mod" id="payMode_deposit" name="payMode" value="1" onclick="paychg(this)"/><font>订金占位</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <font id="payModeText" style="display: none"></font>
                    <label for="spinner" class="txt2" id="label_deposit"><span class="xing" id="deposit_xing" style="display: none;"></span>保留天数：</label>
                    <span id="payMode_deposit_span" class="payModeSpan">
                    	<input id="remainDays_deposit" class="spinner" name="remainDays_deposit" maxlength="3" value=""
                               onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
                    	<span style="padding-left:5px;">天</span>
                    </span>
                    <br id="label_n" />
                    <label id="label_n" >&nbsp;</label>
                    <input type="checkbox" class="ckb_mod" id="payMode_advance" name="payMode" value="2" onclick="paychg(this)"/><font>预占位</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <label for="spinner" class="txt2" id="label_advance"><span class="xing" id="advance_xing" style="display: none;">*</span>保留天数：</label>
                    <span id="payMode_advance_span" class="payModeSpan">
                    	<input id="remainDays_advance" class="spinner" name="remainDays_advance" maxlength="3" value=""
                               onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
                    	<span style="padding-left:5px;">天</span>
                    </span>
                    <br id="label_n" /><label id="label_n" >&nbsp;</label>
                    <input type="checkbox" class="ckb_mod" id="payMode_data" name="payMode" value="4" onclick="paychg(this)"/><font>资料占位</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <label for="spinner" class="txt2" id="label_data"><span class="xing" id="data_xing" style="display: none;">*</span>保留天数：</label>
                    <span id="payMode_data_span" class="payModeSpan">
                    	<input id="remainDays_data" class="spinner" name="remainDays_data" maxlength="3" value=""
                               onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
                    	<span style="padding-left:5px;">天</span>
                    </span>
                    <br id="label_n" /><label id="label_n" >&nbsp;</label>
                    <input type="checkbox" class="ckb_mod" id="payMode_guarantee" name="payMode" value="5" onclick="paychg(this)"/><font>担保占位</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <label for="spinner" class="txt2" id="label_guarantee"><span class="xing" id="guarantee_xing" style="display: none;">*</span>保留天数：</label>
                    <span id="payMode_guarantee_span" class="payModeSpan">
                    	<input id="remainDays_guarantee" class="spinner" name="remainDays_guarantee" maxlength="3" value=""
                               onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
                    	<span style="padding-left:5px;">天</span>
                    </span>
                    <br id="label_n" /><label id="label_n">&nbsp;</label>
                    <input type="checkbox" class="ckb_mod" id="payMode_express" name="payMode" value="6" onclick="paychg(this)"/><font>确认单占位</font>
                    <label for="spinner" class="txt2" id="label_express"><span class="xing" id="express_xing" style="display: none;">*</span>保留天数：</label>
                    <span id="payMode_express_span" class="payModeSpan">
                    	<input id="remainDays_express" class="spinner" name="remainDays_express" maxlength="3" value=""
                               onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
                    	<span style="padding-left:5px;">天</span>
                    </span>
                    <br id="label_n" />
                    <label id="label_n" >&nbsp;</label>
                    <input type="checkbox" class="ckb_mod" id="payMode_full" name="payMode" value="3"/><font>全款支付　　</font>
                    <br id="label_n" />
                    <label id="label_n" >&nbsp;</label>
                    <input type="checkbox" class="ckb_mod" id="payMode_op" name="payMode" value="7"/><font>计调确认占位　　</font>
                </div>

                 <div class="mod_information_d2">
                        <label for="spinner" class="txt2">创建时间：</label>
                        <input id="createDate" name="createDate" type="text" onclick="WdatePicker()" class="valid">
                 </div>
                 <div class="kongr"></div>

                <div id="outAreaLabel" class="mod_information_d2" style="display: none;">
                    <label>离境口岸：</label>
                    <form:select path="outArea" itemValue="key" itemLabel="value" >
                        <form:option value="">请选择</form:option>
                        <form:options items="${outAreas}" />
                    </form:select>
                </div>
                <div class="mod_information_d2">
                    <label>联运类型：</label>
                    <select id="intermodalType" name="intermodalType" onchange="transportchg()">
                        <option id="national" value="1">全国联运</option><option id="none" value="0" selected="selected">无联运</option><option id="group" value="2">分区联运</option>
                    </select>
                </div>
                <div id="nationalTrans" class="mod_information_d2" style="display:none">
                    <label>联运价格：</label>
                    <input class="valid rmbp17" id="intermodalAllPrice" name="intermodalAllPrice" maxlength="8" type="text" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" />
                </div>
                <div id="groupTrans" class="mod_information_d2 transport_city" style="display:none">
                    <p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart0" name="intermodalGroupPart" maxlength="50" type="text" /><label>联运价格：</label><input class="valid rmbp17" id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="8" type="text" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" /><a class="ydbz_s transportAdd" onclick="javascript:transportAdd(this, 0);">增加</a></p>
                </div>
                <div class="kongr"></div>
            </div>
        </div>
                       <div id="oneStepBtn" class="ydBtn">
                           <input class="btn btn-primary" type="button" onclick="oneToSecond()" value="下一步"/>
                       </div>
        
        <div id="secondStepDiv" class="mod_information" style="display: none;">   
            <div id="secondStepTitle"  class="mod_information_d"><span style=" font-weight:bold; padding-left:20px;float:left">添加团期和价格</span></div>
            <div id="secondStepContent" class="add2_information_dzhan">
                <div style="width:100%; height:20px;"></div>
<%--                <div class="team_bill"><span style="padding-left:20px; color:#FFF;">填写团期价格信息</span></div>--%>
                <div class="add2_line"><div class="add2_line_text">填写团期价格信息：</div></div>
                <div style="width:100%; height:20px;"></div>
                <div class="add2_nei">
                   <table class="table-mod2-group">
                     <tr>
                       <td class="add2_nei_table">成人同行价：</td>
                       <td class="add2_nei_table_typetext"><input class="rmb_input" id="settlementAdultPriceDefine" type="text" maxlength="6"/></td>
                       <td class="add2_nei_table">儿童同行价：</td>
                       <td class="add2_nei_table_typetext"><input class="rmb_input" id="settlementcChildPriceDefine" type="text" maxlength="6"/></td>
                       <td class="add2_nei_table">特殊人群同行价：</td>
                       <td class="add2_nei_table_typetext"><input class="rmb_input" id="settlementSpecialPriceDefine" type="text" maxlength="6"/></td>
                       <td class="add2_nei_table">需交订金：</td>
                       <td class="add2_nei_table_typetext"><input class="rmb_input" id="payDepositDefine" type="text" maxlength="6"/></td>
                     </tr>
                     <tr>
                       <td class="add2_nei_table">成人直客价：</td>
                       <td class="add2_nei_table_typetext"><input class="rmb_input" id="suggestAdultPriceDefine" type="text" maxlength="6"/></td>
                       <td class="add2_nei_table">儿童直客价：</td>
                       <td class="add2_nei_table_typetext"><input class="rmb_input" id="suggestChildPriceDefine" type="text" maxlength="6"/></td>
                       <td class="add2_nei_table">特殊人群直客价：</td>
                       <td class="add2_nei_table_typetext"><input class="rmb_input" id="suggestSpecialPriceDefine" type="text" maxlength="6"/></td>
                       <td class="add2_nei_table">单房差：</td>
                       <td class="add2_nei_table_typetext"><input class="rmb_input" id="singleDiffDefine" type="text" maxlength="6"/></td>
                     </tr>
                   </table>
                 <div class="kong"></div>
               </div>
               
               <div class="add2_nei add2_nei_line">
                    <div class="add2_nei_a">选择出团日期：</div>
                    <div class="add2_nei_b">（温馨提示：日历框中选择出团日期，按shift键同时移动鼠标多选日期）</div>
                    <div class="kong"></div>
                </div>
                <div class="add2_nei add2_nei_chosedate">
                        <textarea name="dateList" id="dateList" style="width:500px;height:200px;display: none;"></textarea>
                        <label>出团日期：</label>
                        <a class="groupDate">选择日期</a>
                        <input id="groupOpenDate" class="inputTxt" name="groupOpenDateBegin" readonly style="background-color:#CCCCCC;width:80px;"/> 至 
                        <input id="groupCloseDate" class="inputTxt" name="groupCloseDateEnd" readonly  style="background-color:#CCCCCC;width:80px;"/>&nbsp;&nbsp;                   
                </div>  
              
<%--             <div class="release_next_add">--%>
<%--                 <input class="btn btn-primary" type="button" onclick="writeGroupDate()" value="提交团期"/>--%>
<%--               </div>           --%>
            </div>
            <div id="secondStepEnd">
                <div style="width:100%; height:30px;"></div>
                <div style="width:100%; height:10px;"></div>
                <div id="groupTable" style="margin-top:8px;">
						<table id="contentTable" class="table table-striped table-bordered table-condensed table-mod2-group psf_table_son" style="width:98%;margin:10px auto"><thead>
					<tr><th rowspan="2" width="9%">出团日期</th>
					<th rowspan="2" width="9%">截团日期</th>
					<th rowspan="2" width="11%">团号</th>
					<th rowspan="2" width="9%">签证国家<span><br><input id="visaCountryCopy" type="text" name="visaCountryCopy" class="visacountrycopy_input" disabled="disabled"/><br/><input id="visaCopyBtn" type="button" value="复制" onclick="visaCopy()" class="visa_copy" disabled="disabled"/></span></th>
					<th rowspan="2" width="9%">资料截止</th>
					<th width="13%" class="t-th2" colspan="3">同行价</th>
					<%--<th width="12%" class="t-th2" colspan="2">trekiz价</th>
					--%><th width="13%" class="t-th2" colspan="3">直客价</th>
					<th width="7%" rowspan="2">需交订金</th>
					<th width="7%" rowspan="2">单房差</th>
					<th width="4%" rowspan="2">预收<span><br><input id="planPositionCopy" name="planPositionCopy" class="visacountrycopy_input" disabled="disabled"/><br/><input id="planPositionBtn" type="button" value="复制" onclick="positionCopy(this)" class="visa_copy" disabled="disabled"/></span></th>
					<th width="4%" rowspan="2">余位<span><br><input id="freePositionCopy" name="freePositionCopy" class="visacountrycopy_input" disabled="true"/><br/><input id="freePositionBtn" type="button" value="复制" onclick="positionCopy(this)" class="visa_copy" disabled="true"/></span></th>
					<th width="4%" rowspan="2">操作<span><br><a href="javascript:void(0)" onclick="delAllGroupDate()">全部<br>删除</a></span>
					</tr>
					<tr><%--<th>成人</th><th>儿童</th>--%><th>成人</th><th>儿童</th><th>特殊人群</th><th>成人</th><th>儿童</th><th>特殊人群</th></tr></thead><tbody><tr id="emptygroup"><td colspan="16">暂无价格信息，请选择日期</td></tr></tbody></table>
					</div>
                <div id="secondStepBtn" class="mod_information_dzhan_d">
                    <div class="release_next_add">
                             <input class="btn btn-primary gray" type="button" onclick="secondToOne()" value="上一步"/>
                             <input class="btn btn-primary" type="button" onclick="secondToThird()" value="下一步"/>
                    </div>
                </div>
                <div class="kong"></div>
            </div>
        </div>          
        <div class="kong" style="clear:none;"></div>
        
        
        <div id="thirdStepDiv" style="display: none;">
            <!-- 上传文件 -->
            <div class="mod_information_d"><span style=" font-weight:bold; padding-left:20px">上传资料</span></div>
            <div class="mod_information_3">
            
                <div class="">              
                    <table border="0"  name="company_logo" style="vertical-align:middle;margin-top:10px;">
                        <tr>
                            <td><label class="company_logo_pos"><span>*</span>产品行程介绍：</label></td>
                            <td><input type="text" class="valid" name="fileLogo" readonly="readonly"  style="width:160px;"  /></td>
                            <td>
                                
                                <input type="file" id="introduction" name="introduction" style="position:absolute;filter:alpha(opacity=0);-moz-opacity:0;opacity:0;margin-left:0px; width:82px;"size="1" hidefocus onchange="inFileName(this)"></input> 
                                <input type="button"  value="选择文件" class="mod_infoinformation3_file"></input>
                            </td>
                        </tr>
                    </table>            
                    <input id="introduction_name" name="introduction_name" value="产品行程介绍" type="hidden" />
                </div>
                <div class="mod_information_d7"></div>
                <div class="">                  
                    <table  border="0"  name="company_logo" style="vertical-align:middle;margin-top:10px;">
                        <tr>
                            <td><label>自费补充协议：</label></td>
                            <td><input type="text" class="valid" name="fileLogo" readonly="readonly"  style="width:160px;"  /></td>
                            <td>
                                <input type="file" id="costagreement" name="costagreement" style="position:absolute;filter:alpha(opacity=0);-moz-opacity:0;opacity:0;margin-left:0px; width:82px;"size="1" hidefocus onchange="inFileName(this)"></input> 
                                <input type="button"  value="选择文件" class="mod_infoinformation3_file"></input>
                            </td>
                        </tr>
                    </table>        
                    <input id="icostagreement_name" name="costagreement_name" value="自费补充协议" type="hidden" />
                </div>
                <div class="mod_information_d7"></div>
                <div class="">                  
                    <table border="0"  name="company_logo" style="vertical-align:middle;margin-top:10px;">
                        <tr>
                            <td><label>其他补充协议：</label></td>
                            <td><input type="text" class="valid" name="fileLogo" readonly="readonly"  style="width:160px;"  /></td>
                            <td>
                                <input type="file" id="otheragreement" name="otheragreement" style="position:absolute;filter:alpha(opacity=0);-moz-opacity:0;opacity:0;margin-left:0px; width:82px;"size="1" hidefocus onchange="inFileName(this)"></input> 
                                <input type="button"  value="选择文件" class="mod_infoinformation3_file"></input>
                            </td>
                        </tr>
                    </table>
                    <input id="otheragreement_name" name="otheragreement_name" value="其他补充协议" type="hidden" />
                </div>
                <div class="mod_information_d7"></div>
                <div style="margin-top:8px;margin-bottom:8px;">
                    <label>签证资料文件：</label><a href="javascript:void(0)" onclick="addvisafile(this)"><img src="${ctxStatic}/images/add_11.jpg" /></a>                  
                </div>
                <div id="visafile" class="mod_information_d8_2">                
                    <table border="0"  name="company_logo" style="vertical-align:middle;">
                        <tr>                        
                            <td><label style="width:auto;">国家：</label></td>
                            <td>
                            
                                <select name="country">
                                    <option value="">请选择</option>
                                    <c:forEach items="${countryList }" var="country">
                                        <option value="${country.id }">${country.countryName_cn}</option>
                                    </c:forEach>
                                </select>
                                
                            </td>
                            <td><label style="width: auto;">签证类型：</label></td>
                            <td>
                                <select name="visaType">
                                    <option value="">请选择</option>
                                    <c:forEach items="${visaTypes }" var="visaType">
                                        <option value="${visaType.value }">${visaType.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td><label style="padding-top:4px;width: auto;">签证资料：</label></td>
                            <td><input type="text" class="valid" name="fileLogo" readonly="readonly"  style="width:160px;"  /></td>
                            <td>
                                <input type="file" id="signmaterial" name="signmaterial" style="position:absolute;filter:alpha(opacity=0);-moz-opacity:0;opacity:0;margin-left:0px; width:82px;"size="1" hidefocus onchange="inFileName(this)"></input> 
                                <input type="button"  value="选择文件" class="mod_infoinformation3_file"></input>
                            </td>
                        </tr>
                    </table>
                    <input class="signmaterial_name" name="signmaterial_name" value="签证资料" type="hidden" />
                </div>
                <div class="mod_information_d7"></div>
                <div id="otherflag" style="margin-top:8px;">
                    <label>其他文件：</label><a href="javascript:void(0)" onclick="addfile(this)"><img src="${ctxStatic}/images/add_11.jpg" /></a>                  
                </div>
            </div>
                 <div class="release_next_add">
                      <input class="btn btn-primary gray" type="button" onclick="ThirdToSecond()" value="上一步"/>
                      <input class="btn btn-primary gray" type="button" onclick="javascript:window.location.href='${ctx}/activity/manager/list/2'" value="放&nbsp;&nbsp;弃"/>
                      <input class="btn btn-primary" type="button" onclick="submitForm(1);" value="保存草稿"/>
                      <input class="btn btn-primary" type="button" onclick="submitForm(2);" value="提交发布"/>
                  </div>
        </div>          
    </form:form>
        
    <div id="template" style="display:none;">
        <select name="country">
            <option value="">请选择</option>
            <c:forEach items="${countryList }" var="country">
                <option value="${country.id }">${country.countryName_cn}</option>
            </c:forEach>
        </select>
        <select name="visaType">
            <option value="">请选择</option>
            <c:forEach items="${visaTypes }" var="visaType">
                <option value="${visaType.value }">${visaType.label}</option>
            </c:forEach>
        </select>
    </div>
        
    <div id="signtemplate" style="display:none;" class="mod_information_d6">
        <table border="0"  name="company_logo" style="vertical-align:middle;">
            <tr>                        
                <td><label style="width: auto;">国家：</label></td>
                <td><select name="country">
                        <option value="">请选择</option>
                        <c:forEach items="${countryList }" var="country">
                            <option value="${country.id }">${country.countryName_cn}</option>
                        </c:forEach>
                    </select>
                </td>
                <td><label style="width: auto;">签证类型：</label></td>
                <td>
                    <select name="visaType">
                        <option value="">请选择</option>
                        <c:forEach items="${visaTypes }" var="visaType">
                            <option value="${visaType.value }">${visaType.label}</option>
                        </c:forEach>
                    </select>
                </td>
                <td><label style="padding-top:4px;width: auto;">签证资料：</label></td>
                <td><input type="text" class="valid" name="fileLogo" readonly="readonly"  style="width:160px;"  /></td>
                <td>
                    <input type="file" id="signmaterial" name="signmaterial" style="position:absolute;filter:alpha(opacity=0);-moz-opacity:0;opacity:0;margin-left:0px; width:82px;"size="1" hidefocus onchange="inFileName(this)"></input> 
                    <input type="button"  value="选择文件" class="mod_infoinformation3_file"></input>
                </td>
                <td>
<%--                    <input type="text" name="companyLogo" readonly="readonly"  style="width:343px;height:22px;border:1px solid #ccc; margin-left:5px; color:#333;border:1px solid #666" />--%>
                    <a href="javascript:void(0)" class="mod_infoinformation3_del" onclick="removefile('确定删除该文件吗',this)">删除</a>
                </td>
            </tr>
        </table>
        <input class="signmaterial_name" name="signmaterial_name" value="签证资料" type="hidden" />
    </div>
        
    <div id="othertemplate" style="display:none;" class="mod_information_d6">
        <table  border="0"  name="company_logo" style="vertical-align:middle;margin-top:10px;">
            <tr>
                <td><label >其他文件：</label></td>
                <td><input type="text" class="valid" name="fileLogo" readonly="readonly"  style="width:160px;"  /></td>
                <td >
                    <input type="file" id="otherfile" name="otherfile" style="position:absolute;filter:alpha(opacity=0);-moz-opacity:0;opacity:0;margin-left:0px; width:82px;"size="1" hidefocus onchange="inFileName(this)"></input> 
                    <input type="button"  value="选择文件" class="mod_infoinformation3_file"></input>
                </td>
                <td>
<%--                    <input type="text" name="companyLogo" readonly="readonly"  style="width:343px;height:22px;border:1px solid #ccc; margin-left:5px; color:#333;border:1px solid #666" />--%>
                    <a href="javascript:void(0)" class="mod_infoinformation3_del" onclick="removefile('确定删除该文件吗',this)">删除</a>
                </td>
            </tr>
        </table>
        <input class="otherfile_name"  name="otherfile_name" value="其他文件" type="hidden"/>
    </div>
    <div style="height:2px; width:100%; clear:both;"></div>
    <style type="text/css">

        #product_level{ top:404px !important; left:268px !important; width:125px;}
        #product_type{ top:300px !important; left:1100px !important; width:125px;}
    </style>    
</div>
</body>
</html>
