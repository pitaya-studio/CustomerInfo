<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>产品添加</title>

		<style>
.custom-combobox-toggle {
	height: 26px;
	margin-left: -1px;
	padding: 0;
	/* support: IE7 */ *
	height: 1.7em; *
	top: 0.1em;
}

.custom-combobox-input {
	margin: 0;
	padding: 0.3em;
	width: 100px;
}

.ui-autocomplete {
	height: 200px;
	overflow: auto;
}
/* 可见用户*/
.mod_information_d2.user_show > div{
	display:inline-block;
	width:200px;
	overflow:hidden;
	text-overflow:ellipsis;
}
/*0071需求样式 */
label.myerror {
    color: #ea5200;
    font-weight: bold;
    margin-left: 0px;
    padding-bottom: 2px;
    padding-left: 0px;
}
.batch-ol li {
	overflow: hidden;
}
</style>


	<meta name="decorator" content="wholesaler" />
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<style type="c">.sort{color:#0663A2;cursor:pointer;}</style>
	<!--   -->
		<!-- 微信 需求 -->
		<%--<script src="${ctxStatic}/js/picView/bootstrap.min.js"></script>--%>
		<script src="${ctxStatic}/js/dist/cropper.js"></script>
		<script src="${ctxStatic}/js/dist/main.js"></script>
		<script src="${ctxStatic}/js/dist/weChat_saveSelf.js"></script>

		<script type="text/javascript" src="${ctxStatic}/js/validationRules.js"></script>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactor2.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/i18n/jquery-ui-i18n.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-other/jquery-combobox_prop.js" type="text/javascript"></script>
    <script src="${ctxStatic}/common/jquery.disabled.js" type="text/javascript"></script>
    <script src="${ctxStatic}/common/jquery.date.format.js" type="text/javascript"></script>
    <script src="${ctxStatic}/common/jquery.file.filter.js" type="text/javascript"></script>
    <script src="${ctxStatic}/modules/activity/dynamic.validator.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <shiro:hasPermission name="price:project">
		<script src="${ctxStatic}/modules/activity/groupPrice.js" type="text/javascript"></script>
	</shiro:hasPermission>
    <script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>
    <script src="${ctxStatic}/modules/product/discount-setting.js" type="text/javascript"></script>
    
    <!-- 233 需求 -->
     <script type="text/javascript" src="${ctxStatic}/modules/stock/stock-list.js"></script> 
     <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/quauq.css"/>

	<!-- 微信 需求 -->
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/wechatList.css"/>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/dist/cropper.css"/>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/dist/main.css"/>


    <script type="text/javascript">
		function regisFun(){
			$(".gc").bind('paste', function(e) {
				var el = $(this);
				setTimeout(function() {
					replaceStr(el[0]);
				}, 100);
			});
		}

	var ctxStatic = '${ctxStatic}';
	//  对应需求  223
	var isneedCruiseGroupControl = '${isneedCruiseGroupControl}';
	//   对应需求  c460
	var groupCodeRuleDT = '${groupCodeRuleDT}';
	
	
	/*
	 *4 t1t2 打通：
	 *散拼产品发布  点击下一步是时获取成人、儿童、特殊人群的 价格策略。
	 *在方法  function oneToSecond()中  为
	 *成人、儿童、特殊人群的 价格策略 赋值
	 */
	var adultQuauqPriceStrategy; //成人价策略      结构'1:200,2:10'
	var childrenQuauqPriceStrategy;//儿童价策略     结构'1:100,2:10'
	var spicalQuauqPriceStratety;//特殊人群价策略     结构'2:200,3:15'
	var paramKind = '${param.kind}';
	
	
    
    $(function() {
    	
    	// 无信息的时候，合并单元格个数设置
    	$("td[name=countLineNum]").each(function(index, obj) {
    		var countLineNum = $(this).parents("tbody").parent().children("thead:eq(0)").find("th").length;
    		$(this).attr("colspan", countLineNum);
    	});
    	
    	
    	/**
    	 *wangxinwei  2016-01-12 added 
    	 *c463为团期添加备注   处理   表格的合并与拆分
    	 *
    	 **/
        $(document).on('click', '.groupNote', function () {
        	//debugger;
        	var kind = $("#activityKind").val();
        	if(2==kind){//散拼前两个都要合并
        		
        		if($(this).parents('tr:first').find("input[name=ids]").parent().size() > 0){//全部列， 优加 奢华  ‘全部’ 点备注时 rowspan 处理
        			$(this).parents('tr:first').find("input[name=ids]").parent().attr("rowspan","2");
        		}
        		
        		if($(this).parents('tr:first').find("input[name=recommend]").parent().size() > 0){
        			$(this).parents('tr:first').find("input[name=recommend]").parent().attr("rowspan","2");
        		}
        		if($(this).parents('tr:first').find("input[name=groupOpenDate]").parent().size() > 0){
        			$(this).parents('tr:first').find("input[name=groupOpenDate]").parent().attr("rowspan","2");
        		}
        		if($(this).parents('tr:first').find("input[name=groupCloseDate]").parent().size() > 0){
        			$(this).parents('tr:first').find("input[name=groupCloseDate]").parent().attr("rowspan","2");
        		}
        	}else{
        		$(this).parents('tr:first').children().eq(0).attr("rowspan","2");
        		$(this).parents('tr:first').children().eq(1).attr("rowspan","2");
        	}
        	
            $(this).parents('tr:first').next().show();
        });
    	
        /*c463为团期取消备注--start*/
     /*    $(document).on('click', '.unSaveNotes', function () {
        	debugger;
        	$(this).parents('tr:first').prev().children().eq(0).attr("rowspan","1");
            $(this).parents('tr:first').hide();
        }); */
        
        /**
    	 *wangxinwei  2016-01-12 added 
    	 *c463取消
    	 *
    	 **/
        $(document).on('click', '.unSaveNotes', function () { //散拼前两个都要合并
        	//debugger;
        	var kind = $("#activityKind").val();
        	if(2==kind){//散拼前两个都要合并
        		
        		if($(this).parents('tr:first').prev().find("input[name=ids]").parent().size() > 0){
        			$(this).parents('tr:first').prev().find("input[name=ids]").parent().attr("rowspan","1");
        		}
        		
        		if($(this).parents('tr:first').prev().find("input[name=recommend]").parent().size() > 0){
        			$(this).parents('tr:first').prev().find("input[name=recommend]").parent().attr("rowspan","1");
        		}
        		if($(this).parents('tr:first').prev().find("input[name=groupOpenDate]").parent().size() > 0){
        			$(this).parents('tr:first').prev().find("input[name=groupOpenDate]").parent().attr("rowspan","1");
        		}
        		if($(this).parents('tr:first').prev().find("input[name=groupCloseDate]").parent().size() > 0){
        			$(this).parents('tr:first').prev().find("input[name=groupCloseDate]").parent().attr("rowspan","1");
        		}
        		
//         		$(this).parents('tr:first').prev().children().eq(0).attr("rowspan","1");
//         		$(this).parents('tr:first').prev().children().eq(1).attr("rowspan","1");
//              $(this).parents('tr:first').prev().children().eq(2).attr("rowspan","1");
        	}else{
        		$(this).parents('tr:first').prev().children().eq(0).attr("rowspan","1");
        		$(this).parents('tr:first').prev().children().eq(1).attr("rowspan","1");
        	}
            
            $(this).parents('tr:first').find("input").val("");
            $(this).parents('tr:first').hide();
            $(this).parent().parent().prev().find(".groupNote").children().remove();
        });
        
        
        /**
    	 *wangxinwei  2016-01-12 added 
    	 *c463保存
    	 *
    	 **/
        $(document).on('click', '.saveNotes', function () {
        	var kind = $("#activityKind").val();
        	if(2==kind){//散拼前两个都要合并
        		
        		if($(this).parents('tr:first').prev().find("input[name=ids]").parent().size() > 0){
        			$(this).parents('tr:first').prev().find("input[name=ids]").parent().attr("rowspan","1");
        		}
        		
        		if($(this).parents('tr:first').prev().find("input[name=recommend]").parent().size() > 0){
        			$(this).parents('tr:first').prev().find("input[name=recommend]").parent().attr("rowspan","1");
        		}
        		if($(this).parents('tr:first').prev().find("input[name=groupOpenDate]").parent().size() > 0){
        			$(this).parents('tr:first').prev().find("input[name=groupOpenDate]").parent().attr("rowspan","1");
        		}
        		if($(this).parents('tr:first').prev().find("input[name=groupCloseDate]").parent().size() > 0){
        			$(this).parents('tr:first').prev().find("input[name=groupCloseDate]").parent().attr("rowspan","1");
        		}
        		
//         		$(this).parents('tr:first').prev().children().eq(0).attr("rowspan","1");
//         		$(this).parents('tr:first').prev().children().eq(1).attr("rowspan","1");
//              $(this).parents('tr:first').prev().children().eq(2).attr("rowspan","1");
        	}else{
        		$(this).parents('tr:first').prev().children().eq(0).attr("rowspan","1");
        		$(this).parents('tr:first').prev().children().eq(1).attr("rowspan","1");
        	}
            
            $(this).parents('tr:first').hide();
            if($.trim($(this).parents('tr:first').find("input").val()) != ""){
            	$(this).parent().parent().prev().find(".groupNote").children().remove();
            	$(this).parent().parent().prev().find(".groupNote").append('<em class="groupNoteTipImg"></em>');
            }else{
            	$(this).parent().parent().prev().find(".groupNote").children().remove();
            	$(this).parent().parent().prev().find(".groupNote").append('<em class=""></em>');
            }
        });
    	
    	
    	operateHandler();
    	
    	
    	$(".selectinput" ).comboboxInquiry();
    	$("#fromArea").comboboxInquiry();
    	$("#backArea").comboboxInquiry();
		$(".custom-combobox-input").css("width","90px");
    	
            $( ".spinner" ).spinner({
                spin: function( event, ui ) {
                if ( ui.value > 365 ) {
                    $( this ).spinner( "value", 1 );
                    return false;
                } else if ( ui.value <= 0 ) {
                    $( this ).spinner( "value", 365 );
                    return false;
                }
                }
            });
            
            $( ".spinner02" ).spinner({
                spin: function( event, ui ) {
//                 if(ui.value==0 && ($("#remainDays_deposit_hour").attr('aria-valuenow')==null || $("#remainDays_deposit_hour").attr('aria-valuenow')=='0')){
//                 	$("#remainDays_deposit_hour").spinner("value",1);
//                 }
                if ( ui.value > 365 ) {
                    $( this ).spinner( "value", 0 );
                    return false;
                } else if ( ui.value < 0 ) {
                    $( this ).spinner( "value", 365 );
                    return false;
                }
                }
            });
            
            $( ".spinner01" ).spinner({
                spin: function( event, ui ) {
//                 if(ui.value==0 && ($("#remainDays_deposit").attr('aria-valuenow')==null || $("#remainDays_deposit").attr('aria-valuenow')=='0')){
//                     $("#remainDays_deposit").spinner("value",1);
//                 }
                if ( ui.value > 23 ) {
                    $( this ).spinner( "value", 0 );
                    return false;
                } else if ( ui.value < 0 ) {
                    $( this ).spinner( "value", 23 );
                    return false;
                }
                }
            });
            
            $( ".spinner_fen" ).spinner({
                spin: function( event, ui ) {
                if ( ui.value > 59 ) {
                    $( this ).spinner( "value", 0 );
                    return false;
                } else if ( ui.value < 0 ) {
                    $( this ).spinner( "value", 59 );
                    return false;
                }
                }
            });
            
            $(".transport_city").delegate(".transportDel","click",function(){
                $(this).parent().remove();
            });
            
            //前端js
	            //文本框内的提示信息
				inputTips();
				//币种选择
				selectCurrency();
				transportSelect();
				
				
				$(document).on('click','.departmentButton',function(){
					var $currentClick=$(this);
						var url = "/sys/department/treeData?officeId=" + ${deptId};
						// 正常打开	
						top.$.jBox.open("iframe:${ctx}/tag/treeselect?url="+encodeURIComponent(url)+"&module=&checked=&extId=&selectIds="+$currentClick.prev().prev().val(), "选择部门", 300, 420,{
							buttons:{"确定":"ok", "关闭":true}, submit:function(v, h, f){
								if (v=="ok"){
									var tree = h.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
									var ids = [], names = [], nodes541 = [];
									if ("" == "true"){
										nodes = tree.getCheckedNodes(true);
									}else{
										nodes = tree.getSelectedNodes();
									}
									for(var i=0; i<nodes.length; i++) {//
										ids.push(nodes[i].id);
										names.push(nodes[i].name);//
										break; // 如果为非复选框选择，则返回第一个选择  
									}
									$currentClick.prev().val(names);
									$currentClick.prev().prev().val(ids);
//				 					$("#departmentId").val(ids);
//				 					$("#departmentName").val(names);
									$("#departmentName").focus();
									$("#departmentName").blur();
								}
							}, loaded:function(h){
								$(".jbox-content", top.document).css("overflow-y","hidden");
							},persistent:true
						});
					});
				
        });
    
    
  //发布单团产品--添加团期和价格--币种选择
    function selectCurrency(){
    	//单个设置币种
    	$(".choose-currency").hover(function(){
    		$(this).addClass("choose-currency-on");
    	},function(){
    		$(this).removeClass("choose-currency-on");
    	}).on("click","dd p",function(){
    		var $this = $(this);
    		if(!$this.hasClass("p-checked")){
    			var oldCurrency = $this.siblings("p.p-checked").text();
    			var oldClass = $this.siblings("p.p-checked").attr("name");
    			$this.addClass("p-checked").siblings("p").removeClass("p-checked");
    			//设置币种/人
    			var $currency = $this.parents(".add2_nei_table").next("td").find(".currency").eq(0);
    			if(0 != $currency.length){
    				var txt_currency = $this.text() == "人民币" ? "元" : $this.text();
    				if("人民币" == oldCurrency){oldCurrency = "元";}//alert(oldCurrency);
    				$currency.html($currency.html().replace(oldCurrency,txt_currency));
    			}
    			//设置币种图标
    			var $input_currency = $this.parents(".add2_nei_table").next("td").find(".ipt-currency").eq(0);
    			$input_currency.prev("span").remove().end().before("<span>"+$this.attr('addClass')+"</span>");
    			//设置对应的select的选中项
    			var $select = $this.parents("td.add2_nei_table").find("select");
    			$select.find("option:selected").removeAttr("selected");
    			$select.children("option[value=" + $this.attr("value") + "]").attr("selected",true);
    		}
    	});
    	//统一设置币种
    	$("#selectCurrency").change(function(){
    		var theValue = $(this).val();
//    		$(this).next("span").html("汇率：" + $(this).children("option:selected").attr("var"));
    		$.each($(".choose-currency"),function(index,element){
    			var $this_dl = $(element);
    			$this_dl.find("dd p[id=" + theValue + "]").click();
    		});
    	});
    }
    
    
    
    var index = 0;
    var res;
    var validator1;
    var errorArray = new Array();
    var closeBeforeVal = "";
    var visaBeforeVal = "";
    $(document).ready(function() {
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
                if($(element).attr("id")=="introduction" || $(element).attr("id")=="departmentName")
                    error.appendTo (element.parent().parent());
                else if($(element).hasClass("spinner"))
                    error.appendTo (element.parent().parent());
                else if ( element.is(":radio") )
                    error.appendTo ( element.parent() );
                else if ( element.is(":checkbox") )
                    error.appendTo ( element.parent() );
                else if ($(element).attr("id")=="targetAreaName")
                    error.appendTo ( element.parent().parent() );
                else if ($(element).attr("id")=="opUserName")
                    error.appendTo ( element.parent().parent() );
                else if ( element.is("input") )
                    error.appendTo ( element.parent() );
                else if($(element).attr("id")=="fromArea" || $(element).attr("id")=="backArea")
                	error.appendTo ( element.parent() );
                else
                   error.insertAfter(element);
            },
            showErrors:function(errorMap,errorList){
                this.defaultShowErrors();
                errorArray = errorArray.concat(errorList);
            },
            ignore:":hidden[id!='fromArea'][id!='backArea']"
        });

		$("#fromArea" ).comboboxInquiry({
			"afterInvalid":function(event,data){
				$(this).trigger("click");
			}
		});
		$("#backArea" ).comboboxInquiry({
			"afterInvalid":function(event,data){
				$(this).trigger("click");
			}
		});

        //防止选择了团期再刷新页面后产生两个团期，进页面时清除
			$("#groupOpenDate").val("");
			$("#dateList").val("");

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

            },
            invalidHandler: function(form, validator) {
                alert("请先修改错误");
                return false;
            }
        });--%>

        //

        var datepicker= $(".groupDate").datepickerRefactor({
	        	 dateFormat: "yy-mm-dd",
		         target:"#dateList",
		         dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
		         closeText:"关闭",
		         prevText:"前一月",
		         nextText:"后一月",
		         monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"],
// 		         minDate:getCurDate(),
 		         minDate:'2015-01-01',
 		         maxDate:'2020-12-31',
		         numberOfMonths:3,
		         closeBeforeDays:"#closeBeforeDays",
		         visaBeforeDays:"#visaBeforeDays",
		         visaCountryCopy:"#visaCountryCopy",
		         visaCopyBtn:"#visaCopyBtn",
		         planPositionCopy:"#planPositionCopy",
		         planPositionBtn:"#planPositionBtn",
		         freePositionCopy:"#freePositionCopy",
		         freePositionBtn:"#freePositionBtn",
		         secondStepContent:"#secondStepContent",
		         beforeShow: function(input) {
        		 	if($(".datepicked").length != 0) {
        		 		return false
        		 	}
		         }
             },
              "#groupOpenDate","#groupCloseDate","#groupTable");


<%--        $("#activityDuration").val(1);          --%>
       /* $("#targetAreaName").change(function(){
			alert("3244");

            $("#targetAreaName").trigger("onblur");
        });*/
		function ttsa(){
			alert("AS");
		}
		document.getElementById("targetAreaId").addEventListener("oninput",ttsa)

        $("#targetAreaId").change(function(){

			alert("324");
        });
        $("#targetAreaId").val("${travelActivity.targetAreaIds}");
        $("#targetAreaName").val("${travelActivity.targetAreaNamess}");
        //可见用户
		$("#opUserName").change(function(){
			$("#opUserName").trigger("onblur");
        });
        $("#opUserId").val("${travelActivity.opUserIds}");
        $("#opUserName").val("${travelActivity.opUserNamess}");
        $("#secondStepDiv").hide();
        $("#thirdStepDiv").hide();
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
//                 minDate:getCurDate(),
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
        	
        	var inputCount = $("[name='otherFileDiv']").length;
        	var tempEle = $("#othertemplate").clone();
        	
        	tempEle.find("td").eq(1).find("input").attr("id","otherfile" + inputCount);
        	tempEle.find("td").eq(2).append('<input type="button" name="otherfile" value="选择文件" class="mod_infoinformation3_file" onClick="uploadFiles(\'${ctx}\',\'' + "otherfile" + inputCount + '\',this)"></input>');
        	
            var file = "<div name=\"otherFileDiv\" style=\"margin-top:8px;\">"+
                        tempEle.html()+                                 
                        "</div>";
            
            $(obj).parent().parent().append(file);
        }
        function addvisafile(obj){
        	
        	var inputCount = $(obj).attr("class");
        	
        	var tempEle = $("[name='signtemplate']").clone();
        	tempEle.find("td").eq(5).find("input").attr("id","signmaterial" + inputCount);
            tempEle.find("td").eq(6).append('<input type="button" name="signmaterial' + inputCount + '" value="选择文件" class="mod_infoinformation3_file" onClick="uploadFiles(\'${ctx}\',\'' + "signmaterial" + inputCount + '\',this)"></input>');
            tempEle.find("td").eq(6).append('<input type="hidden" name="fileGroup" value="' + inputCount + '" />');
            
            var html = 
            "<div id=\"visafile\" class=\"mod_information_d8_2\" style=\"margin-top:5px;\">"+tempEle.html()+"</div>";
            
            
            
            $("#visaData").append(html);
			
            
            $("#thirdStepDiv .mod_information_d8_2 select[name='country']").combobox();
            
            add_visaFileValidator();
        }
        
        
         function trafficchg(){
            var value=$("#trafficMode option:selected").val();
            if("${relevanceFlagId}"!="" && "${relevanceFlagId}".split(",").contains(value)) 
         //  if(value == "飞机")
            {
<%--                $("#trafficName").css("display","inline-block");--%>
<%--                $("#flightInfo").css("display","inline-block");--%>
                $(".ml25").css("display","inline-block");
            }else {
<%--                $("#trafficName").css("display","none");--%>
<%--                $("#trafficName option[value='']").attr("selected", true);--%>
<%--                $("#flightInfo").css("display","none");--%>
<%--                $("#flightInfo option[value='']").attr("selected", true);--%>
                $(".ml25").css("display","none");
            }
        }
        
        
        
        
        
        
        
        
        
        
        
        
        
        function findTrafficAct(actNum) {
        	$.ajax({
        		type: "POST",
                url: "${ctx}/activity/manager/getTrafficAct",
                data: actNum,
            	async:false,
                success: function(result){
                	if(result != null)
                		return false;
             		else
             			return true;
                	
                }
        	})
        }
        //79需求订金占位设置非必填项--2016/02/29--wenchao.lv--需求暂停暂时屏蔽
        //為避免衝突公用方法問題
        function paychgsubsription(obj) {
            if($(obj).prop("checked")){
                //$(obj).next().next().find("span").css("display","inline");
                $(obj).next().next().next().find("input[name^='remainDays']").rules("remove");
                $(obj).next().next().next().find("input[name^='remainDays']").removeAttr("disabled");
                //改为自定义校验规则 changed for C405 2015.11.10
                //$(obj).next().next().next().find("input[name^='remainDays']").rules("add",{required:true,digits:true,nonzero:true,messages:{required:"必填信息",digits:"请输入正确的数字",nonzero:"输入不能为0"}});
            }else{
                //$(obj).next().next().find("span").css("display","none");
                $(obj).next().next().next().find("input[name^='remainDays']").rules("remove");
                $(obj).next().next().next().find("input[name^='remainDays']").val("");
                $(obj).next().next().next().next().find("input[name^='remainDays']").val("");
                $(obj).next().next().next().next().next().find("input[name^='remainDays']").val("");
                /*注释不可写样式*/
                //$(obj).next().next().next().find("input[name^='remainDays']").attr("disabled","disabled");
            }
        }
        //79需求订金占位设置非必填项--2016/02/29--wenchao.lv
        function paychg(obj) {
        	if($(obj).prop("checked")){
        		$(obj).next().next().find("span").css("display","inline");
        		$(obj).next().next().next().find("input[name^='remainDays']").rules("remove");
            	$(obj).next().next().next().find("input[name^='remainDays']").removeAttr("disabled");
        		//改为自定义校验规则 changed for C405 2015.11.10
            	//$(obj).next().next().next().find("input[name^='remainDays']").rules("add",{required:true,digits:true,nonzero:true,messages:{required:"必填信息",digits:"请输入正确的数字",nonzero:"输入不能为0"}});
        	}else{
        		$(obj).next().next().find("span").css("display","none");
        		$(obj).next().next().next().find("input[name^='remainDays']").rules("remove");
            	$(obj).next().next().next().find("input[name^='remainDays']").val("");
            	$(obj).next().next().next().next().find("input[name^='remainDays']").val("");
            	$(obj).next().next().next().next().next().find("input[name^='remainDays']").val("");
            	/*注释不可写样式*/
            	//$(obj).next().next().next().find("input[name^='remainDays']").attr("disabled","disabled");
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
        	
        	//debugger;
        	var temCols=$("#contentTable").find("tbody").find("td").size()-2;//-2为调试$("#contentTable").find("tbody").find("td")所得
        	var kind = $("#activityKind").val();
        	var tuijian = $("#tuijian").val();
        	
            $(document).delGroup1(obj);
            
            if($(".datepicked").length != 0) {
	            $(".datepicked").removeClass("datepicked");
            }
            /*复原样式*/
            var col = "${param.kind}";
            if(col == "1"){
            	 //$("#emptygroup td").attr("colspan",15);//因为bug #13153,"删除"造成的样式问题
            	$("#emptygroup td").attr("colspan",temCols); 
            }else{
            	if(kind == 2 & tuijian == 'true'){
	            	//$("#emptygroup td").attr("colspan",18);//因为bug #13153,"删除"造成的样式问题
	            	//计算删除之前表体具有多少列
	            	
	            	$("#emptygroup td").attr("colspan",temCols);
            	}else{
            		$("#emptygroup td").attr("colspan",temCols);
            	}
            }
           
            
        }
        
        
        /**
		 * 
		 * c463 仓型校验要做特殊处理：
		 * planPosition   freePosition 要赋予新的id  
		 * 
		 */
		var cloneIdNum = 100;
		function addNewCabintype(thisObj){
			
        	//debugger;
			
			//$(this).parent().parent().after($(this).parent().parent().clone());
			
			//$(this).parent().parent().next().after($(this).parent().parent().next().clone()).after($(this).parent().parent().clone());
			
			
			//var srcTR = $(thisObj).parents("tr:first").clone()
			
			//处理新增舱型的校验
			var srcTR = $(thisObj).parents("tr:first").clone().find("input[name='planPosition']").attr("id","planPosition_"+cloneIdNum).parents("tr:first")
			                                                  .find("input[name='freePosition']").attr("id","freePosition_"+cloneIdNum).parents("tr:first");
			var groupReMarkTR = $(thisObj).parents("tr:first").next().clone();
			$(thisObj).parents("tr:first").next().after(groupReMarkTR).after(srcTR);
			
			cloneIdNum = cloneIdNum+1;
			
			// 对应需求号 223  新增舱型时   不复制 原有的团控关联
			$(thisObj).parents("tr:first").next().next().find("input[name='cruiseGroupControlId']").val("");
			
			
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
            //debugger;
            var getGroupCD = $(obj).attr("id");
            if(undefined != getGroupCD){
            	var index = getGroupCD.substring(14,getGroupCD.length);
	            var strGroupOD = "#groupOpenDate";
	            var groupOD = strGroupOD.concat(index);
	            var maxDate = $(groupOD).val();
	            return maxDate;
            }
        }
        

        
        
        
        
        
        function getCurDate(){
            var curDate = new Date();
            var times = curDate.getTime();
            times += 24*3600*1000;
            curDate.setTime(times);
            var dateStr = curDate.getFullYear()+"-"+(curDate.getMonth()+1)+"-"+curDate.getDate();
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
           // add_filevalidator();
            var flagVisa = visaValidator();
            //如果是单团产品，则不需要验证产品编号
            //if($("#suggestAdultPriceDefine").length == 0)
            //remove_serNumValidator();
            //团期酒店房型拼接,同一团期内使用逗号隔开
            assembleGroupHotelAndHouseType();
            
            // QU-SDP-微信分销模块start 调用微信上传广告图片验证信息 yang.gao 2017-01-06
			var requiredStraightPrice = ${requiredStraightPrice}; // 是否拥有计调权限
			var kind = '${param.kind}'; // 产品类型
			// 散拼并有计调权限
			if (kind == 2 && requiredStraightPrice) {
				
				//var validImg = $("input[name='distributionAdImg']").next(".batch-ol").children().eq(0).html(); // 判断是否有上传图片
				// 如果没有上传图片做信息提示
				// if (validImg == "") {
				//	$.jBox.confirm("尚未添加微信分销广告图片，确认发布吗?", "提示", function(v, h, f) {
				//		if (v == 'ok') {
				//			submitFormFun(flagVisa, status);
				//		} else if (v == 'cancel') {}
				//	});
				//} else {
				//	submitFormFun(flagVisa, status);
				//}
				
				
				// 图片上传剪裁插件公共部分调用
				var base64_img = uploadPosterPicCommon();
				// 如果出现图片规格验证错误则返回false
				if (base64_img == "false") {
					return false;
				}
				
				
				// 判断是否上传为微信广告图片
				if($(".cropper-view-box").children().attr("src").indexOf("images/wechat") != -1) {
					$.jBox.confirm("尚未添加微信分销广告图片，确认发布吗?", "提示", function(v, h, f) {
						if (v == 'ok') {
							// 进行提交操作带微信图片
							submitFormFunWithWeiXin(flagVisa, status, base64_img);
						} else if (v == 'cancel') {
							// 去掉提交按钮禁用效果
							$("#submitAndSave").attr("class","ydbz_x");
						}
					});
				} else {
					// 进行提交操作带微信图片
					submitFormFunWithWeiXin(flagVisa, status, base64_img);
				}
			} else {
				// 进行提交不带微信图片
				submitFormFun(flagVisa, status);
			}
			// QU-SDP-微信分销模块end yang.gao 2017-01-06
        }
        
        // QU-SDP-微信分销模块start yang.gao 2017-01-06
		// 表单数据提交不带微信广告图片
        function submitFormFun(flagVisa, status) {
			if(flagVisa) {
				if($("#addForm").validate({}).form()) {
					if(submit_times == 0) {
						submit_times++;
						var groupPriceFlag = $("#groupPriceFlag").val();
						if (groupPriceFlag  == "true") {
							groupPriceJosn();
						}
						$("#addForm").attr("action","${ctx}/activity/manager/save?activityStatus="+status);
						$("#addForm").submit();
					}
				}else{
					top.$.jBox.info("请先修改完错误再提交", "警告");
					top.$('.jbox-body .jbox-icon').css('top','55px');
				}
			}
		}
		
		// 表单数据提交带微信广告图片
		function submitFormFunWithWeiXin (flagVisa, status, base64_img) {
			var _ctx=$("#ctx").val();
			// 上传微信图片并保存到数据库
			$.ajax({
				url: _ctx+"/activity/manager/uploadImgByBaseCode",
				type: 'POST',
				data: {image:base64_img},
				timeout : 10000, //超时时间设置，单位毫秒
				async: true,
				success: function (result) {
					if (result.msg == "success") {
						// 验证成功后提交表单
						if(flagVisa) {
							if($("#addForm").validate({}).form()) {
								if(submit_times == 0) {
									submit_times++;
									var groupPriceFlag = $("#groupPriceFlag").val();
									if (groupPriceFlag  == "true") {
										groupPriceJosn();
									}
									$("#addForm").attr("action","${ctx}/activity/manager/save?activityStatus="+status+"&docId="+result.data);
									$("#addForm").submit();
								}
							}else{
								top.$.jBox.info("请先修改完错误再提交", "警告");
								top.$('.jbox-body .jbox-icon').css('top','55px');
							}
						}
					} else {
						// 后台验证：如果为false-2 提示图片大小不能超过2M 
						if (result.msg == "false-2") {
							top.$.jBox.info("请上传规定的格式图片并且大小不超过2M.", "警告");
							$("#submitAndSave").attr("class","ydbz_x");
						// 后台验证：验证失败提示
						} else {
							top.$.jBox.info("上传微信广告图片有误", "警告");
							$("#submitAndSave").attr("class","ydbz_x");
						}
					}
				},
				error: function (returndata) {
					
				}
			});	
		}
		// QU-SDP-微信分销模块end yang.gao 2017-01-06
        
        function visaValidator() {
    		var flag = true;
    		$(".mod_information_3 [name='visaType']").each(function(index, obj) {
    			var visaVal = $(obj).val();
    			var countryVal = $(obj).parent().parent().children().children("[name='country']").val();
    			if(countryVal == "" && visaVal != "") {
    				flag = false;
    				top.$.jBox.info("请选择签证类型对应签证国家", "警告");
    				return false;
    			}
    			if(countryVal != "" && visaVal == "") {
    				flag = false;
    				top.$.jBox.info("请选择签证国家对应签证类型", "警告");
    				return false;
    			}
    		});
    		return flag;
    	}
    	
    	/**
    	 * 组装团期酒店房型
    	 */
    	function assembleGroupHotelAndHouseType(){
    		$("#contentTable").children("tbody").find("tr[class!=noteTr]").each(function(index, element){
    			if($(element).find("td[name=hotelhouse]").size() > 0){
    				//组装酒店字符串
    				var groupHotelStr = "";
    				$(element).find("td[name=hotelhouse]").find("input[name=groupHotel]").each(function(index2, element2){
    					if(index2 > 0){
    						groupHotelStr += ",";
    					}
    					groupHotelStr += $(element2).val();
    				});
    				//设置隐藏域的值，如果没有则追加
    				if($(element).find("input[name=groupHotelStr]").size() > 0){
    					$(element).find("input[name=groupHotelStr]").val(groupHotelStr);
    				} else {
    					var hotelHtml = "<input type='hidden' name='groupHotelStr' value='" + groupHotelStr + "'>";
    					$(element).find("td[name=hotelhouse]").append(hotelHtml);
    				}
    				//组装房型字符串
    				var groupHouseTypeStr = "";
    				$(element).find("td[name=hotelhouse]").find("input[name=groupHouseType]").each(function(index2, element2){
    					if(index2 > 0){
    						groupHouseTypeStr += ",";
    					}
    					groupHouseTypeStr += $(element2).val();
    				});
    				//设置隐藏域的值，如果没有则追加
    				if($(element).find("input[name=groupHouseTypeStr]").size() > 0){
    					$(element).find("input[name=groupHouseTypeStr]").val(groupHouseTypeStr);
    				} else {
    					var houseTypeHtml = "<input type='hidden' name='groupHouseTypeStr' value='" + groupHouseTypeStr + "'>";
    					$(element).find("td[name=hotelhouse]").append(houseTypeHtml);
    				}
    			}
    		});
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
    				/*注释不可写样式*/
    	        	//$(this).next().next().next().find("input[name^='remainDays']").attr("disabled","disabled");
    	        	$(this).next().next().find("span.xing").hide();
    			}
    		});
    	}
        // 下一步
        function oneToSecond(){
        	
        	debugger;
        	var activityKind = '${param.kind}';
        	
	        /*
	         *涉及表：agent_price_strategy，price_strategy
	         *
	         *对应需求号 0426 t1t2 打通 
	         *产品发布前根据 出发城市，目的地（线路），旅游类型，产品类型，产品系列  校验  是否有可匹配的价格策略
	         *并根据策略计算出    一套  quauq 价，返回到页面：具体的匹配规则如下：
	         *1.var adultQuauqPriceStrategy; //成人价策略      结构'1:200,2:10'  加200 减 10，
	         *2.var childrenQuauqPriceStrategy;//儿童价策略     结构'1:100,2:10'  加100减10
	         *3.var spicalQuauqPriceStratety;//特殊人群价策略     结构'2:200,3:15'  减200，打15折 
	         *4.1代表加，2代表减，3代表乘%
	         *
	         *
	         */
//			if("2"==activityKind){

				//获取产品基本策略匹配参数
//				var fromArea = $("#fromArea").val(); //出发城市
//				var targetAreaId = $("#targetAreaId").val(); //目的地
//				var travelTypeId = $("#travelTypeId").val(); //旅游类型
//				var activityTypeId = $("#activityTypeId").val(); //产品类型
//				var activityLevelId = $("#activityLevelId").val(); //产品系列

//				var flag = false;
//				$.ajax({
//					type: "POST",
//					async:false,
					<%--url: "${ctx}/activity/manager/checkActivityPriceStrategy",--%>
//					data: {"fromArea":fromArea,"targetAreaId":targetAreaId,"travelTypeId":travelTypeId,"activityTypeId":activityTypeId,"activityLevelId":activityLevelId},
//					success: function(data){
//						debugger;
//						if(data.result=="0"){
//							top.$.jBox.info("未匹配到相关渠道价格策略，请配置相关策略。", "警告",{
//								buttons: {"取消": "0", "新建策略": "1"},
//								submit:function(v,h,f){
//									if(v=='1'){
										<%--window.location.href ="${ctx}/pricingStrategy/manager/addt";--%>
										//window.open("${ctx}/pricingStrategy/manager/addt");
//									}else{
										//alert("000");
//									}
//								}
//							});
//							flag = true;
//						}else{
							 //quauqPrice4Adult##2:12#2:12,3:2#2:13
							 //quauqPrice4Child##2:13#2:1#2:14
							 //quauqPrice4SpicalPerson##2:15
//							 adultQuauqPriceStrategy = data.quauqPrice4Adult; //成人价策略      结构'1:200,2:10#1:100,2:10'  加200 减 10，
//					         childrenQuauqPriceStrategy = data.quauqPrice4Child;//儿童价策略     结构'1:100,2:10'  加100减10
//					         spicalQuauqPriceStratety = data.quauqPrice4SpicalPerson;//特殊人群价策略     结构'2:200,3:15'  减200，打15折
//						}
//					}
//				});
//				if(flag){
//					return;
//				}
//			}
        	
	        
        	
        	
	        if("" == $("#deptId").val() || undefined == $("#deptId").val()) {
	        	top.$.jBox.info("该用户没有配置发布产品相关的部门，请确认后再试！", "警告");
	        	return;
	        }
	        //青岛凯撒验证用户是否填写了团号姓氏缩写
			if("1"==$("#deptCodeFlag").val()){
				var flag = false;
				$.ajax({
					type: "POST",
					async:false,
					url: "${ctx}/activity/manager/checkGroupeSurname",
					data: {},
					success: function(data){
						if(data.result=="0"){
							top.$.jBox.info("该用户没有配置团号姓氏缩写，请确认后再试！", "警告");
							flag = true;
						}
					}
				});
				if(flag){
					return;
				}
			}
	        
	        //验证订金占位保留时限不能全部为空或为零
            //79需求设置订金占位为非必填项--2016/2/29-wenchao.lv--需求暂停暂时屏蔽
           /*  if($("#payMode_deposit").prop("checked") && ($("#remainDays_deposit_hour").val()=="0" && $("#remainDays_deposit").val()=="0" && $("#remainDays_deposit_fen").val()=="0")) {
                top.$.jBox.info("订金占位 保留时限不能全部为零!", "警告");
                return;
            } */
            //79需求设置订金占位为非必填项--2016/2/29-wenchao.lv

	       if($("#payMode_deposit").prop("checked") && ($("#remainDays_deposit_hour").val()=="" ||$("#remainDays_deposit_hour").val()=="0") && ($("#remainDays_deposit").val()=="" ||$("#remainDays_deposit").val()=="0") && ($("#remainDays_deposit_fen").val()=="" ||$("#remainDays_deposit_fen").val()=="0")){
	        	top.$.jBox.info("订金占位 保留时限不能全部为空或零!", "警告");
	        	return;
	        }
	        //验证预占位时限不能全部为空或为零
	        if($("#payMode_advance").prop("checked") && ($("#remainDays_advance").val()=="" ||$("#remainDays_advance").val()=="0") && ($("#remainDays_advance_hour").val()=="" ||$("#remainDays_advance_hour").val()=="0") && ($("#remainDays_advance_fen").val()=="" ||$("#remainDays_advance_fen").val()=="0")){
	        	top.$.jBox.info("预占位 保留时限不能全部为空或零!", "警告");
	        	return;
	        }
	        //验证资料占位时限不能全部为空或为零
	        if($("#payMode_data").prop("checked") && ($("#remainDays_data").val()=="" ||$("#remainDays_data").val()=="0") && ($("#remainDays_data_hour").val()=="" ||$("#remainDays_data_hour").val()=="0") && ($("#remainDays_data_fen").val()=="" ||$("#remainDays_data_fen").val()=="0")){
	        	top.$.jBox.info("资料占位 保留时限不能全部为空或零!", "警告");
	        	return;
	        }
	        //验证担保占位时限不能全部为空或为零
	        if($("#payMode_guarantee").prop("checked") && ($("#remainDays_guarantee").val()=="" ||$("#remainDays_guarantee").val()=="0") && ($("#remainDays_guarantee_hour").val()=="" ||$("#remainDays_guarantee_hour").val()=="0") && ($("#remainDays_guarantee_fen").val()=="" ||$("#remainDays_guarantee_fen").val()=="0")){
	        	top.$.jBox.info("担保占位 保留时限不能全部为空或零!", "警告");
	        	return;
	        }
	        //验证确认单占位时限不能全部为空或为零
	        if($("#payMode_express").prop("checked") && ($("#remainDays_express").val()=="" ||$("#remainDays_express").val()=="0") && ($("#remainDays_express_hour").val()=="" ||$("#remainDays_express_hour").val()=="0") && ($("#remainDays_express_fen").val()=="" ||$("#remainDays_express_fen").val()=="0")){
	        	top.$.jBox.info("确认单占位 保留时限不能全部为空或零!", "警告");
	        	return;
	        }
	        
	        var remainDays_deposit = $("#remainDays_deposit").val();
	        var remainDays_deposit_hour = $("#remainDays_deposit_hour").val();
	        var remainDays_deposit_fen = $("#remainDays_deposit_fen").val();
	        
	        var remainDays_advance = $("#remainDays_advance").val();
	        var remainDays_advance_hour = $("#remainDays_advance_hour").val();
	        var remainDays_advance_fen = $("#remainDays_advance_fen").val();
	        
	        var remainDays_data = $("#remainDays_data").val();
	        var remainDays_data_hour = $("#remainDays_data_hour").val();
	        var remainDays_data_fen = $("#remainDays_data_fen").val();
	        
	        var remainDays_guarantee = $("#remainDays_guarantee").val();
	        var remainDays_guarantee_hour = $("#remainDays_guarantee_hour").val();
	        var remainDays_guarantee_fen = $("#remainDays_guarantee_fen").val();
	        
	        var remainDays_express = $("#remainDays_express").val();
	        var remainDays_express_hour = $("#remainDays_express_hour").val();
	        var remainDays_express_fen = $("#remainDays_express_fen").val();
	        
	        if(remainDays_deposit=="") remainDays_deposit=0;
	        if(remainDays_deposit_hour=="") remainDays_deposit_hour=0;
	        if(remainDays_deposit_fen=="") remainDays_deposit_fen=0;
	        if(remainDays_advance=="") remainDays_advance=0;
	        if(remainDays_advance_hour=="") remainDays_advance_hour=0;
	        if(remainDays_advance_fen=="") remainDays_advance_fen=0;
	        if(remainDays_data=="") remainDays_data=0;
	        if(remainDays_data_hour=="") remainDays_data_hour=0;
	        if(remainDays_data_fen=="") remainDays_data_fen=0;
	        if(remainDays_guarantee=="") remainDays_guarantee=0;
	        if(remainDays_guarantee_hour=="") remainDays_guarantee_hour=0;
	        if(remainDays_guarantee_fen=="") remainDays_guarantee_fen=0;
	        if(remainDays_express=="") remainDays_express=0;
	        if(remainDays_express_hour=="") remainDays_express_hour=0;
	        if(remainDays_express_fen=="") remainDays_express_fen=0;
	        
	        if($("#payMode_deposit").prop("checked") && parseInt(remainDays_deposit)>365){
	        	top.$.jBox.info("订金占位 保留天数需为0~365的正整数!", "警告");
	        	return;
	        }
	        if($("#payMode_deposit").prop("checked") && parseInt(remainDays_deposit_hour)>23){
	        	top.$.jBox.info("订金占位 保留小时数需为0~23的正整数!", "警告");
	        	return;
	        }
	        if($("#payMode_deposit").prop("checked") && parseInt(remainDays_deposit_fen)>59){
	        	top.$.jBox.info("订金占位 保留分钟数需为0~59的正整数!", "警告");
	        	return;
	        }
	        
	        if($("#payMode_advance").prop("checked") && parseInt(remainDays_advance)>365){
	        	top.$.jBox.info("预占位 保留天数需为0~365的正整数!", "警告");
	        	return;
	        }
	        if($("#payMode_advance").prop("checked") && parseInt(remainDays_advance_hour)>23){
	        	top.$.jBox.info("预占位 保留小时数需为0~23的正整数!", "警告");
	        	return;
	        }
	        if($("#payMode_advance").prop("checked") && parseInt(remainDays_advance_fen)>59){
	        	top.$.jBox.info("预占位 保留分钟数需为0~59的正整数!", "警告");
	        	return;
	        }
	        
	        if($("#payMode_data").prop("checked") && parseInt(remainDays_data)>365){
	        	top.$.jBox.info("资料占位 保留小时数需为0~365的正整数!", "警告");
	        	return;
	        }
	        if($("#payMode_data").prop("checked") && parseInt(remainDays_data_hour)>23){
	        	top.$.jBox.info("资料占位 保留小时数需为0~23的正整数!", "警告");
	        	return;
	        }
	        if($("#payMode_data").prop("checked") && parseInt(remainDays_data_fen)>59){
	        	top.$.jBox.info("资料占位 保留分钟数需为0~59的正整数!", "警告");
	        	return;
	        }
	        
	        if($("#payMode_guarantee").prop("checked") && parseInt(remainDays_guarantee)>365){
	        	top.$.jBox.info("担保占位 保留小时数需为0~365的正整数!", "警告");
	        	return;
	        }
	        if($("#payMode_guarantee").prop("checked") && parseInt(remainDays_guarantee_hour)>23){
	        	top.$.jBox.info("担保占位 保留小时数需为0~23的正整数!", "警告");
	        	return;
	        }
	        if($("#payMode_guarantee").prop("checked") && parseInt(remainDays_guarantee_fen)>59){
	        	top.$.jBox.info("担保占位 保留分钟数需为0~59的正整数!", "警告");
	        	return;
	        }
	        
	        if($("#payMode_express").prop("checked") && parseInt(remainDays_express)>365){
	        	top.$.jBox.info("确认单占位 保留小时数需为0~365的正整数!", "警告");
	        	return;
	        }
	        if($("#payMode_express").prop("checked") && parseInt(remainDays_express_hour)>23){
	        	top.$.jBox.info("确认单占位 保留小时数需为0~23的正整数!", "警告");
	        	return;
	        }
	        if($("#payMode_express").prop("checked") && parseInt(remainDays_express_fen)>59){
	        	top.$.jBox.info("确认单占位 保留分钟数需为0~59的正整数!", "警告");
	        	return;
	        }
        //联运部分已经去除====================
<%--            var priceInput = document.getElementsByName('intermodalGroupPrice');--%>
<%--            var priceInputAll = document.getElementsByName('intermodalAllPrice');--%>
<%--            for(var i = 0; i < priceInput.length; i++){--%>
<%--                if($.trim(priceInput[i].value) == ''){--%>
<%--                    priceInput[i].value = 0;--%>
<%--                }--%>
<%--            }--%>
<%--            if($.trim(priceInputAll[0].value) == ''){--%>
<%--                priceInputAll[0].value = 0;--%>
<%--            }--%>
		//联运部分已经去除=====================
<%--            remove_filevalidator();--%>
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
                $("#oneStepDiv").disableContainer({blankText : "",tipTarget:["opUserName"]});
                disabledcheckbox();
                $("#oneStepBtn").hide();
                $(".acitivityNameSizeSpan").hide();
                $("#dh").removeClass("add_img").addClass("add2_img");
                $("#secondStepDiv").show();
                $("#thirdStepDiv").hide();
                $(".displayClick").show();
                if($("#showErrorDiv").length!=0) {
               		$("#showErrorDiv").remove();
                }
                subCode(true,false);
                //如果之前关联了机票产品，但是后来又更改了，并且更改的方式不关联机票产品，则需要去对掉机票产品的关联。
				var value = $("#trafficMode option:selected").val();
			    if($(".airInfo").find("div").length != 0 && !"${relevanceFlagId}".split(",").contains(value)) {
					$(".activityAirTicketId").val("");
					$(".airInfo-tit1").remove();
					$(".airInfo-con").remove();
				}
                if($(".airInfo").find(".airInfo-con").length != 0) {
                	$(".linkAir").show();
                }
            }else{
                showErrorDiv();
                
            }
            //填充部门信息
            if("" == $("#treeDeptId").val() || $("#treeDeptId").val() == undefined) {
            	$("#treeDeptId").val($("#deptId").val());
            }
            
            //增加可见用户hover事件提示框效果
            var temp = $("label:contains('可见用户')").next('div').find("span").text();
            var temparray = temp.split(',');
            if(temparray != null && temparray !=""){
            	var result = "";
                for(var i=0;i<temparray.length;i++){
                	if(result.indexOf(temparray[i]) >= 0 ){
                		continue;
                	}
                	result = result+temparray[i]+",";      
                }
                result = result.substring(0,result.length-1);
                $("label:contains('可见用户')").next('div').find("span").attr('title',result);
                
                if(temparray.length==1){
                	$("label:contains('可见用户')").next('div').find("span").text(temparray[0]);
                }else if(temparray.length==2){
                	 $("label:contains('可见用户')").next('div').find("span").text(temparray[0]+","+temparray[1]);
                }else{
                	$("label:contains('可见用户')").next('div').find("span").text(temparray[0]+","+temparray[1]+",...");
                }
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
                    	if($(this).val() != 3 && $(this).val() != 7 && $(this).val() != 8) {
                    		var remainDays = $("[name$=" + $(this).attr("id").split("_")[1] + "]").val();
                    		var remainHours = $("[name$=" + $(this).attr("id").split("_")[1] + "_hour]").val();
                    		var remainMin = $("[name$=" + $(this).attr("id").split("_")[1] + "_fen]").val();
                    		
                    		if(remainDays==""){
                    			remainDays=0;
                    		}
                    		if(remainHours==""){
                    			remainHours=0;
                    		}
                    		if(remainMin==""){
                    			remainMin=0;
                    		}
                    		
                    		if(remainDays == 0 && remainHours==0 && remainMin==0) {
                                //79需求订金占位设置非必填项，當订金输入为空时默认保留永久不得自动删除须手动删除--2016/02/29--wenchao.lv-需求暂停暂时屏蔽
                                //payModeHtml += ",&nbsp;&nbsp;" + $(this).next("font").text().replace(/(^\s*)|(\s*$)/g,'')+"(保留永久)";
                    			payModeHtml += ",&nbsp;&nbsp;" + $(this).next("font").text().replace(/(^\s*)|(\s*$)/g,'');
                        	} else {
                        		payModeHtml += ",&nbsp;&nbsp;" + $(this).next("font").text().replace(/(^\s*)|(\s*$)/g,'') + "（保留" +  remainDays + "天"+remainHours+"时"+remainMin+"分）";
                            }
                        } else {
                        	payModeHtml += ",&nbsp;&nbsp;" + $(this).next("font").text();
                        }
                	} else {
                		if($(this).val() != 3 && $(this).val() != 7 && $(this).val() != 8) {
                    		var remainDays = $("[name$=" + $(this).attr("id").split("_")[1] + "]").val();
                    		var remainHours = $("[name$=" + $(this).attr("id").split("_")[1] + "_hour]").val();
                    		var remainMin = $("[name$=" + $(this).attr("id").split("_")[1] + "_fen]").val();
                    		if(remainDays==""){
                    			remainDays=0;
                    		}
                    		if(remainHours==""){
                    			remainHours=0;
                    		}
                    		if(remainMin==""){
                    			remainMin=0;
                    		}
                    		if(remainDays == 0 && remainHours==0 && remainMin==0) {
                                //79需求订金占位设置非必填项，當订金输入为空时默认保留永久不得自动删除须手动删除--2016/02/29--wenchao.lv
                    			//payModeHtml = $(this).next("font").text().replace(/(^\s*)|(\s*$)/g,'')+"(保留永久)";
                    			payModeHtml = $(this).next("font").text().replace(/(^\s*)|(\s*$)/g,'');
                        	} else {
                        		payModeHtml = $(this).next("font").text().replace(/(^\s*)|(\s*$)/g,'') + "（保留" +  remainDays + "天"+remainHours+"时"+remainMin+"分）";
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
            	$("#payMode_deposit_span").show();
            	$(".payModeSpan").show();
            	$(this).show();
            	$(this).next("font").show();
//            	$(this).next().next("[id^='label']").show();
            	$("[id^='label']").show();
            	$(this).next().next().next().show();
            	if($(this).prop("checked")){
            		$(this).next().next().find("span.xing").show();
            	}else{
            		$(this).next().next().find("span.xing").hide();
            	}
            	if(!$(obj).prop("checked")){//没有选中的占位方式若输入了时间，点击下一步后再返回时清空输入的值 
            		$(obj).next().next().next().next().find("input[name^='remainDays']").val("");
            		$(obj).next().next().next().next().next().find("input[name^='remainDays']").val("");
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
        
        //获取当前金额的币种，用于显示在输入框下面的span中。
		function getCurrencyMark(obj) {
			return $(".choose-currency").find("p[id='"+$(obj).prev().attr("var")+"']").attr("addclass");
		}        
        
        function secondToThird(){
        	
        	// 酒店房型添加按钮隐藏
        	//groupPriceOpHide();
        	// debugger;
            $(".handle").hide();
        	 
        	//c463  团期
        	var groupRemarkAfterX = $("#contentTable").find("input[name='groupRemark']").each(function(index,obj){
        			
        		  //debugger;
        		  //处理备注信息过多  的样式问题
        		  $(obj).parent("div:first").removeClass("remarks-containers");
        		  
        		  //点下一步时    去掉团期的清空按钮  XX
        		  $(obj).next().removeClass("clearNotes");
        		  
        		  
        		  //点下一步时收起    所有团期的备注，并对 表格的合并与才分做处理
        		  var kind = $("#activityKind").val();
	              if(2==kind){//散拼前两个都要合并
	            	  $(obj).parents('tr:first').prev().children().eq(0).attr("rowspan","1");
	            	  $(obj).parents('tr:first').prev().children().eq(1).attr("rowspan","1");
	            	  $(obj).parents('tr:first').prev().children().eq(2).attr("rowspan","1");
	              }else{
	            	  $(obj).parents('tr:first').prev().children().eq(0).attr("rowspan","1");
	            	  $(obj).parents('tr:first').prev().children().eq(1).attr("rowspan","1");
	              }
                  
	              //备注不为  空的添加样式   ！
                  if($.trim($(obj).parents('tr:first').find("input").val()) != ""){
                	  $(obj).parent().parent().prev().find(".groupNote").children().remove();
                	  //$(obj).parents("tr:first").prev().find(".groupNote").append('<em class="groupNoteTipImg"></em>')
                	  $(obj).parents("tr:first").prev().find(".groupNote").find("em").eq(0).addClass('groupNoteTipImg');
                  }
                  $(obj).parents('tr:first').hide();
        		  
            });
        	
        	
        	/*
        	 *对应需求号   0258
        	 *懿洋假期添加发票税
        	 *产品发布时：在提交也   如果 invoiceTax 为空  或 0,0.00 统一显示为 0.00
        	 */
        	$("#contentTable").find("input[name='invoiceTax']").each(function(index,obj){
        		
        		var invoiceTax  = $(obj).val();
        		if(null==invoiceTax||""==invoiceTax||"0"==invoiceTax||"0.0"==invoiceTax){
        			$(obj).val("0.00");
        		}
        		
        	});
        	
        	// 微信分销系统 直客价必填权限验证 王洋 2017.1.6
        	if (${requiredStraightPrice}) {
        		var info = false;
        		$("#contentTable").find("input[name='suggestAdultPrice']").each(function(index,obj){
        			var suggestAdultPrice = $(obj).val();
        			if (null == suggestAdultPrice || "" == suggestAdultPrice || "0" == suggestAdultPrice || "0.0" == suggestAdultPrice) {
        				info = true;
        			}
        		});
        		
        		if (info) {
        			top.$.jBox.info("请填写成人直客价，否则不能进行微信分销", "警告");
        			return;
        		}
        	}
        	
        	
        	
        	//给checkbox不为on的 赋值
//         	var recommendEles=$("input[type='checkbox'][name='recommend']:checked");  
// 			$(recommendEles).each(function(){
// 				var idVal = $(this).attr("id");  
// 			    $(this).attr("value", idVal);  
// 			});
           //0071-进来先清空提示span-防止bug-s
            $("[name='span4visaCountry']").empty();
           //0071-进来先清空提示span-防止bug-e

            remove_productinfo();
            remove_filevalidator();
            errorArray = new Array();    
        	add_groupsvalidator();
            var flag = validator1.form();
            //**************0071-签证国家长度限定为50字-s**********
            var arrVisaCountries=$('[name="visaCountry"]');
            var flag4VisaCountry=false;
            for(var i=0;i<arrVisaCountries.length;i++){
            	if(arrVisaCountries[i].value.length>50){
                 $("#visaCountry"+i).after('<label name="span4visaCountry" style="display:block"  class="myerror">请输入一个长度最多是50的字符串</label>');
                 flag4VisaCountry=true;
            	}	
            }
            if(flag4VisaCountry==true){
            	 top.$.jBox.info("请先修改完错误再提交", "警告");
                 top.$('.jbox-body .jbox-icon').css('top','55px');
                 return;
            }
            //**************0071-签证国家长度限定为50字-e**********
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
                                tips += "第"+(i+1)+"团号发生重复，请重新录入<br>";
                            }
                        });
                        
                        
                        
                        if(!submitflag){
                            top.$.jBox.info(tips, "警告", { width: 250, showType:"slide", icon:     "info",draggable: "true" });
                           top.$('.jbox-body .jbox-icon').css('top','55px');
                           return;
                       }    
                       
                        var obj=  $("#contentTable").find("input[name='maxPeopleCount']")
                        var obj1=  $("#contentTable").find("input[name='maxChildrenCount']")
                        var maxCountFlag = true; 
                      	var maxChildrenCount = true;
                        var planPositionFlag = true; 
                        var sumPositionFlag = true;
                        var planPositionFlagChildren = true; 
                        $.each(obj, function(i,item){ 
                        	 var obj2 = $("#contentTable").find("input[name='planPosition']");
                        	 var objTemp = $("#contentTable").find("input[name='maxChildrenCount']");
                        	/* if(!$.trim($("#maxPeopleCountDefine").val()) =="")
                        		{
	                        		if(parseInt(item.value) >parseInt($("#maxPeopleCountDefine").val()))
	                        		{
	                        		 	maxCountFlag=false;
	                        			return false;
	                        		}
                        		
                        		} */
                        	
                        	if(parseInt(item.value) >parseInt(obj2[i].value))
                        		{
                        		planPositionFlag=false;
                    				return false;
                        		}
                        		
                        	if((parseInt(item.value)+parseInt(objTemp[i].value)) >parseInt(obj2[i].value))
                        		{
                        		sumPositionFlag=false;
                    				return false;
                        		}
                        	
                           });
                        
                        $.each(obj1, function(i,item){ 
                        	 var obj2 = $("#contentTable").find("input[name='planPosition']");
                        	  var objTemp = $("#contentTable").find("input[name='maxPeopleCount']");
                        	/* if(!$.trim($("#maxChildrenCountDefine").val()) =="")
                        		{
	                        		if(parseInt(item.value) >parseInt($("#maxChildrenCountDefine").val()))
	                        		{
	                        		alert("1");
	                        		 	maxChildrenCount=false;
	                        			return false;
	                        		}
                        		
                        		} */
                        	
                        	if(parseInt(item.value) >parseInt(obj2[i].value))
                        		{
                        		planPositionFlagChildren=false;
                    				return false;
                        		}
                        	
                        	if((parseInt(item.value)+parseInt(objTemp[i].value)) >parseInt(obj2[i].value))
                        		{
                        		sumPositionFlag=false;
                    				return false;
                        		}
                        	
                           });
                        
                        
                        if(flag && submitflag  && planPositionFlag  && planPositionFlagChildren && sumPositionFlag){
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
                            $("#secondStepEnd").find("input[name=groupCode]").next().attr("style","word-break: break-all;display: block;word-wrap: break-word;");
                            $("#secondStepBtn").hide();
                            $("#dh").removeClass("add2_img").addClass("add3_img");
                            $("#thirdStepDiv").show();
							//bug16482
							$("#contentTable .add-select").hide();
							$("#contentTable .remove-selected").hide();
                            //判断产品类型
							var suggestBoo = $("#suggestAdultPriceDefine").length>0?true:false;
                            $("#contentTable tbody").children().find("td:eq(5),td:eq(6),td:eq(7),td:eq(8),td:eq(9)").find("span").each(function(index,obj){
                            	if ($("select",obj).length == 0){
                            		$(obj).text($(obj).text());
                            	}
                            });
	                        if(suggestBoo) {
	                            $("#contentTable tbody").children().find("td:eq(10)").find("span").each(function(index,obj){$(obj).text($(obj).text());});
	                            $("#contentTable tbody").children().find("td:eq(11)").find("span").each(function(index,obj){$(obj).text($(obj).text());});
	                            $("#contentTable tbody").children().find("td:eq(12)").find("span").each(function(index,obj){$(obj).text($(obj).text());});
                            }
	                        $(".handle").hide();//隐藏操作按钮 
                            
                            // from freeposition to invoiceTax for   0258  wxw modified
	                        $("#secondStepEnd tbody").children().find("[name='invoiceTax']").each(function(index, obj){
	                        	
	                        	 //c463  团期的展示
	                        	 var oprationHtml  = '<dl class="handle"><dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt><dd class=""><p style="width: 75px;">';
	                        	     oprationHtml += '<a id="" name="uploadGroupFile" class="uploadGroupFileClass" onclick="uploadGroupFile(\'${ctx}\', this)" href="javascript:void(0)">上传出团通知</a>';
	                        	     
	                        	     if($(obj).parent().parent().next().attr('class') == 'noteTr'&&""!= $.trim($(obj).parent().parent().next().find('input').val())){
	                        	    	 
	                        	    	 oprationHtml += '<a id="" name="uploadGroupFile" class="uploadGroupFileClass" onclick="showGroupRemark(this)" href="javascript:void(0)">展开备注<em class=\"groupNoteTipImg\"></em></a>';
	                        	    	 
	                        	     }else{
	                        	    	 
	                        	    	 oprationHtml += '<a id="" name="uploadGroupFile" class="uploadGroupFileClass" onclick="showGroupRemark(this)" href="javascript:void(0)">展开备注</a>';
	                        	     
	                        	     }
	                        	     var groupPriceFlag = $("#groupPriceFlag").val();
	                        	     if (groupPriceFlag  == "true") {
	                        	     	if ($("[name=expandPricing]").length > 0) {
	                        	     		if ($("[name=expandPricing]").text() == "展开价格方案") {
	                        	     			oprationHtml += '<a name="expandPricing" haspricing=\"true\" class="">展开价格方案</a>';
	                        	     		} else {
	                        	     			oprationHtml += '<a name="expandPricing" haspricing=\"true\" class="">收起价格方案</a>';
	                        	     		}
	                        	     	}
	                        	     }
	                        	     oprationHtml += '</dd></dl>';
	                        	     
	                        	
	                        	 $(obj).parent().next().append(oprationHtml);
	                        });
	                        
	                      
	                        
                            //subCode(false,true);
                            subCode(false,false);
                            //生成币种Id串，并记录在每个团期后面
                            var len = $("#contentTable tbody").find("input").length;
							if(len != 0){
                            	//createCurrencyIdStr();以前用这个函数，在IE8浏览器下不管用，故注释掉
                            	//生成币种Id串，并记录在每个团期后面,开始
                            	// c463 wangxinwei  modified 2016-01-19
                            	//$("#contentTable").children("tbody").find("tr").each(function(index,obj){
                            	$("#contentTable").children("tbody").find("tr[class*='-']").each(function(index,obj){
                            		
								var currencyIdStr = "";
								$(obj).find("input").each(function(index1,obj1) {
								
									if($(obj1).hasClass("ipt-currency")) {
						    			currencyIdStr += $(obj1).attr("var") + ",";
						    			}
						    		});
						   			currencyIdStr = currencyIdStr.substring(0,currencyIdStr.length-1);
						   			if($(obj).find("[name='groupCurrencyType']").length != 0) {
						   				$(obj).find("[name='groupCurrencyType']").remove();
						   			}
						    		$(obj).append("<input type='hidden' name='groupCurrencyType' value='"+currencyIdStr+"' />");
						    		
						    		
						   	 	});
						   	 	//生成币种Id串，并记录在每个团期后面,结束
                            }
							//批量操作团期项目的数值隐藏
							$("#contentTable").children("thead").find(".disabledshowspan").hide();
                        }else{  
                        	/* if(maxCountFlag == false)
                        		top.$.jBox.info("\"特殊人群人数\"不能大于\"特殊人群最高人数\"", "警告");
                       		else if(maxChildrenCount == false)
                        		top.$.jBox.info("\"儿童人数\"不能大于\"儿童最高人数\"", "警告");
                       		else */ if(planPositionFlagChildren == false)
                        		top.$.jBox.info("\"儿童最高人数\"不能大于\"预收\"", "警告");
                        	else if(planPositionFlag == false)
                        		top.$.jBox.info("\"特殊人群最高人数\"不能大于\"预收\"", "警告");
                        	else if(sumPositionFlag == false)
                        		top.$.jBox.info("\"儿童与特殊人群最高人数之和\"不能大于\"预收\"", "警告");
                        	else
                            top.$.jBox.info("请先修改完错误再提交", "警告");
                            top.$('.jbox-body .jbox-icon').css('top','55px');
                        }
                    }
                 });
            }    
            /*隐藏标题中的input*/
        	//$("span .disabledshowspan").hide();      
           
            // 隐藏操作按钮
           //groupPriceOpHide();
            
        }
        function secondToOne(){
        	// 显示酒店房型操作按钮
        	//groupPriceOpShow();
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
            $(".custom-combobox a").attr("style","");           
          	$("#opUserButton").removeAttr("style");             
            $("#dh").removeClass("add2_img").addClass("add_img");
            $("#oneStepDiv").show();
            $("#oneStepBtn").show();
            $("#secondStepDiv").hide();
            $("#thirdStepDiv").hide();
            $(".acitivityNameSizeSpan").show();
        }
        function ThirdToSecond(){
        	//
        	//debugger;
        	//c463  团期    : 当返回上一步时    添加备注的   清空  按钮 XX
        	var groupRemarkAfterX = $("#contentTable").find("input[name='groupRemark']").each(function(index,obj){
        		//debugger;
      		  $(obj).next().next().addClass("clearNotes");
      		  
      		  //处理备注信息过多  的样式问题
      		  $(obj).parent("div:first").addClass("remarks-containers");
      		  
            });
        	
        	
        	//debugger;
        	// 对应需求223      为修改bug:12888
        	$("#contentTable").find("a:contains('生成团控表')").each(function(index,obj){
        		//debugger;
        		$(obj).parents("dl:first").show();
        		$(obj).parents("dl:first").next().hide();
        	});
        	


            $("#contentTable").find("input[name='groupOpenDate']").each(function(index,obj){
                $(obj).parents("tr:first").children("td:last").find("dl:eq(0)").show();
                 $(obj).parents("tr:first").children("td:last").find("dl:gt(0)").remove();
            });
     
      		  
      		 
        	
        	
            $("#secondStepEnd").undisableContainer();
        	$("#secondStepEnd").show();
            $("#secondStepTitle span:eq(1)").remove();
            $("#secondStepContent").show();
            $("#secondStepBtn").show();
            $("#dh").removeClass("add3_img").addClass("add2_img");
            $("#thirdStepDiv").hide();
            
            /*返回第二步时，删除操作中的上传出团通知<a>标签*/
            $("#secondStepEnd tbody").children().find("[name='openDateFiles']").each(function(index, obj){
            	$(obj).parent().find("dl[class='handle']").remove();
            });
            
            // 显示团期价格操作按钮
            //groupPriceOpShow();
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
                case "groupNoMark":
                    name = "团号标识";
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
                case "fromArea":
                    name = "出发城市";
                    break;
                case "backArea":
                    name = "返回城市";
                    break;
                case "departmentName":
                    name = "部门";
                    break;
                case "outArea":
                    name = "离境口岸";
                    break;
                case "trafficMode":
                    name = "交通方式";
                    break;
                case "activityDuration":
                    name = "行程天数";
                    break;
                case "remainDays_deposit":
                    name = "订金占位保留时限";
                    break;
                case "remainDays_advance":
                    name = "预占位保留时限";
                    break;
                case "remainDays_data":
                    name = "资料占位保留时限";
                    break;
                case "remainDays_guarantee":
                    name = "担保占位保留时限";
                    break;
                case "remainDays_express":
                    name = "确认单占位保留时限";
                    break;
                case "payMode":
                    name = "付款方式";
                    break;
                case "groupLead":
                	name = "领队";
                	break;
                case "opUserName":
                	name = "可见用户";
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
        /*	单团产品页面已经去除联运*/
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


	//发布单团产品--基础信息填写--联运选择
	function transportAdd(element, index){
		var $selectCurrency = $("#templateCurrency").clone();
		var html_selectCurrency = $selectCurrency.removeAttr("id").removeAttr("style")[0].outerHTML;
		var middle = 'onkeyup="this.value=this.value.replace(/[^\\d\\+\\-]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^\\d\\+\\-]/g,\'\')"';
		$(element).parent().parent().append('<p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart' + (index+1) + '" name="intermodalGroupPart" maxlength="50" type="text"><label>联运价格：</label>&nbsp;' + html_selectCurrency +'&nbsp;<input class="valid rmb" id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="8" type="text" ' + middle + ' /><a class="ydbz_s gray transportDel">删除</a></p>');
		$('.transportAdd').attr('onclick', 'transportAdd(this, ' + (index + 1) + ')');
	}
        
    function changeFromType(){
        var typeValue = $('#intermodalType').val();
        var $outArea = $("#outArea");
        if(typeValue != 1 && typeValue != 2){
            $outArea.rules("remove");
            $outArea.prev("label").children().remove();
            $('#outArea').val("");
        }else{
            $outArea.prev("label").children().remove();
            $("#outArea").rules("add",{required:true,messages:{required:"必填信息"}});
            $outArea.prev("label").prepend("<span class=\"xing\">*</span>");
        }

    }
    
    //新行者团号规则
    function changeGroupRule(obj) {
    	var oldCode = $("#contentTable").find("[name='groupCode']").val();
    	var oldCodeArr = oldCode.split("-");
    	var oldTitle = oldCodeArr[0] + "-" + oldCodeArr[1];
    	var newTitle = $(obj).find("option:selected").text();
    	var newCode = oldCode.replace(oldTitle, newTitle);
    	$("#contentTable").find("[name='groupCode']").val(newCode);
    }
    
    
  //团号标识
    function addReady(fn){
        if(document.addEventListener){
            document.addEventListener('DOMContentLoaded',fn,false);
        }else{
            document.attachEvent('onreadystatechange',function(){
                if(document.readyState == 'complete'){
                    fn();
                }
            });
        }
    };
    addReady(function(){
        var oGroupNoMark = document.getElementById('groupNoMark');
        var oGroupNoName = document.getElementById('groupNoName');
        oGroupNoMark.onchange = function(){
            if(oGroupNoMark.value==""){
                oGroupNoName.style.display = 'none';
                oGroupNoName.value = "";
            }else{
                oGroupNoName.style.display = 'block';
                oGroupNoName.value = this.value;
            }
        };
    });


    
    function forbidUnlowCharacter(obj){ var $obj = $(obj);  if (/["<>.'\\]/.test($obj.val())){
        $obj.val($obj.data("oldvalue"));  }
}
$(document).on('keyup','#specialRemark',function(){ forbidUnlowCharacter(this); }) 
$(document).on('keypress','#specialRemark',function(){ forbidUnlowCharacter(this); })
    
    /**
     *  wangxinwei 2016-01-12 added
     *  c463  团期添加备注  提交前  的 备注展示
     *  1.处理备注开关的切换
     *  2.处理开关备注时 表格的拆分与合并
     **/
    function showGroupRemark(thisObj){
    	
    	//debugger;
    	
    	//处理备注的    展开与关闭的切换
    	if("展开备注"==$(thisObj).text()){
    		
    		if($(thisObj).find("em").attr("class")){
    			$(thisObj).html('关闭备注<em class=\"groupNoteTipImg\">');
    		}else{
    			$(thisObj).html('关闭备注');
    		}
    	}else{
    		if($(thisObj).find("em").attr("class")){
    			$(thisObj).html('展开备注<em class=\"groupNoteTipImg\">');
    		}else{
    			$(thisObj).html('展开备注');
    		}
    	}
    	
    	//处理备注开关时单元格的合并 与 拆分
    	$(thisObj).parents('tr:first').next().toggle();
    	if($(thisObj).parents('tr:first').next().attr("style")=='display: none;'){
    		var kind = $("#activityKind").val();
        	if(2==kind){//散拼做特殊处理
        		if($(thisObj).parents('tr:first').find("input[name=recommend]").parent().size() > 0){
        			$(thisObj).parents('tr:first').find("input[name=recommend]").parent().attr("rowspan","1");
        		}
        		if($(thisObj).parents('tr:first').find("input[name=groupOpenDate]").parent().size() > 0){
        			$(thisObj).parents('tr:first').find("input[name=groupOpenDate]").parent().attr("rowspan","1");
        		}
        		if($(thisObj).parents('tr:first').find("input[name=groupCloseDate]").parent().size() > 0){
        			$(thisObj).parents('tr:first').find("input[name=groupCloseDate]").parent().attr("rowspan","1");
        		}
        		
//         		$(thisObj).parents('tr:first').children().eq(0).attr("rowspan","1");
//         		$(thisObj).parents('tr:first').children().eq(1).attr("rowspan","1");
//         		$(thisObj).parents('tr:first').children().eq(2).attr("rowspan","1");
        	}else{
        		$(thisObj).parents('tr:first').children().eq(0).attr("rowspan","1");
        		$(thisObj).parents('tr:first').children().eq(1).attr("rowspan","1");
        	}
    		
    	}else{
    		var kind = $("#activityKind").val();
    		if(2==kind){
    			if($(thisObj).parents('tr:first').find("input[name=recommend]").parent().size() > 0){
        			$(thisObj).parents('tr:first').find("input[name=recommend]").parent().attr("rowspan","2");
        		}
        		if($(thisObj).parents('tr:first').find("input[name=groupOpenDate]").parent().size() > 0){
        			$(thisObj).parents('tr:first').find("input[name=groupOpenDate]").parent().attr("rowspan","2");
        		}
        		if($(thisObj).parents('tr:first').find("input[name=groupCloseDate]").parent().size() > 0){
        			$(thisObj).parents('tr:first').find("input[name=groupCloseDate]").parent().attr("rowspan","2");
        		}
        		
//     			$(thisObj).parents('tr:first').children().eq(0).attr("rowspan","2");
//     			$(thisObj).parents('tr:first').children().eq(1).attr("rowspan","2");
//     			$(thisObj).parents('tr:first').children().eq(2).attr("rowspan","2");
    		}else{
    			$(thisObj).parents('tr:first').children().eq(0).attr("rowspan","2");
    			$(thisObj).parents('tr:first').children().eq(1).attr("rowspan","2");
    		}
    		
    	}
    	
    }
    
    
    
    


    function checkall(obj){
        if($(obj).attr("checked")){
            $('#contentTable input[type="checkbox"]').attr("checked",'true');
            $("input[name='allChk']").attr("checked",'true');
        }else{
            $('#contentTable input[type="checkbox"]').removeAttr("checked");
            $("input[name='allChk']").removeAttr("checked");
        }
    }






    function reserve() {
        var $popReserve = $.jBox($('#popReserve').html(),
                {
                    title: "渠道和付款方式选择：",
                    buttons: {}
                }
        );
        $popReserve.on('click', '#btnNext', function () {
            var status = $popReserve.data("status");
            if (status == 'payment') {
                $popReserve.data('status', 'orderType');
                $popReserve.trigger('pop.status.change');
            } else {
                //@todo 需要开发继续处理
            }
        });
        $popReserve.on('click', '#btnPre', function () {
            var status = $popReserve.data("status");
            if (status == 'payment') {
                $.jBox.close();
            } else {
                $popReserve.data('status', 'payment');
                $popReserve.trigger('pop.status.change');
            }
        });
        $popReserve.on('pop.status.change', function () {
            var status = $popReserve.data("status");
            var $btnPre = $popReserve.find('#btnPre');
            var $reservePayment = $popReserve.find("#reservePayment");
            var $reserveOrderType = $popReserve.find("#reserveOrderType");
            if (status == 'payment') {
                $btnPre.text('取消');
                $popReserve.find('.jbox-title').text('渠道和付款方式选择');
                $reservePayment.show();
                $reserveOrderType.hide();
            } else {
                $btnPre.text('返回');
                $popReserve.find('.jbox-title').text('订单类型选择');
                $reservePayment.hide();
                $reserveOrderType.show();
            }
        });
        $popReserve.data('status', 'payment');
    }
    function checkall_new(obj){
        if($(obj).attr("checked")){
            $('#contentTable input[name="ids"]').attr("checked",'true');
            $("input[name='allChk']").attr("checked",'true');
        }else{
            $('#contentTable input[name="ids"]').removeAttr("checked");
            $("input[name='allChk']").removeAttr("checked");
        }
    }

    function checkreverse(obj){
        var $contentTable = $('#contentTable');
        $contentTable.find('input[type="checkbox"]').each(function(){
            var $checkbox = $(this);
            if($checkbox.is(':checked')){
                $checkbox.removeAttr('checked');
            }else{
                $checkbox.attr('checked',true);
            }
        });
    }
   
    $(document).on('click','.control-display',function(){
        if($(this).text()!="收起"){
            $(this).text('收起')
            $(this).parent().css('height','auto');
        }else{
            $(this).text('展开所选择的团')
            $(this).parent().css('height','30px');
        }

    });
  //---223--游轮名称onchange事件--s--//
  /*   function changeCruiseshipName(obj){
	   //移除游轮名称下拉项的属性,去除缓存影响.-s--//
	   $("#selectedCruiseshipName").find("option").removeAttr("selected");
	   //移除游轮名称下拉项的属性,去除缓存影响.-e--//
	   //清除变更之前的船期和船期下的具体信息
	    $("#selectedCruiseshipDate").empty();
	    $("#stockInfo").find("tbody").empty();
	   //获得选中的游轮名称
        var cruiseshipUuid=$(obj).val();
        var path='${ctx}/activity/manager/getShipDateByCruiseshipUuid?uuid='+cruiseshipUuid;
        //alert(path);
       $.ajax({
    	   url:path,
    	   type:"post",
    	   success:function(result){
    		 var cruiseshipUUid=result.cruiseshipUUid;
    		 var shipdateList=result.shipdateList;
    		 var shipStockDetailInfos=result.shipStockDetailInfo;
    		// debugger;
    		//---拼出某游轮的船期下拉列表--223-s//
             var html="";
    		 //清空下拉列表的缓存
    		 $("#selectedCruiseshipDate").empty();
    		 if("none"!=shipdateList){ //游轮名称不为请选择时,才要进行船期下拉项的拼接
    		 for(var i=0;i<shipdateList.length;i++){
    			// alert(shipdateList[i].ship_date);
    			 html+=' <option value="'+shipdateList[i].ship_date+'">'+shipdateList[i].ship_date+'</option>';
    		 }
    			 $("#selectedCruiseshipDate").append(html); 
    		 } 
    			 //debugger;
    			 //解决弹窗刷新造成的游轮名称下拉列表项被重置默认的问题
    			   var sel=$("#selectedCruiseshipName").find("option");
    			   sel.each(function(i,n){
    				  //alert(this.value) 
    				  if(n.value==cruiseshipUUid){
    					 $(n).attr("selected","selected");
    				  } 
    				 // alert(n.value+"--"+(n.value==cruiseshipUUid));
    			   });   
    			   //拼接第一次带出的船期库存的信息.--s
       			  // var tbodyElt=$("#stockInfo").find("tbody");
       			   var htmlStock="";
       			   if("none"!=shipStockDetailInfos){ //游轮名称不为默认请选择时,才进行船期具体信息的拼接
       			   for(var j=0;j<shipStockDetailInfos.length;j++){
       				   htmlStock+='<tr>';
       				   htmlStock+='<td class="tc" cruiseGroupControlId="'+shipStockDetailInfos[j].detailId+'">'+(j+1)+'</td>';
       				   htmlStock+='<td  class="tc">'+shipStockDetailInfos[j].cabin_name+'</td>'
       				   htmlStock+='<td  class="tc">'+shipStockDetailInfos[j].stock_amount+'</td>';
    				   htmlStock+='<td  class="tc">'+shipStockDetailInfos[j].free_positon+'</td>';
       				   htmlStock+='</tr>';
       			   }
       			   $("#stockInfo").find("tbody").append(htmlStock);
       			   //拼接第一次带出的船期库存的信息.--e
       			   }
    			   jbox_create_group_control_pop(this);//每次更新船期的下拉列表需要重新刷新jBox弹窗
                   										//否则append内容不生效
    												
    		  //---拼出某游轮的船期下拉列表--223-e//
    	   }
    	   });
       } */
      //---223-tgy-游轮名称onchange事件--e--//
      
      //----223-tgy--船期onchange事件--s--//
     /*  function changeCruiseshipDate(obj){
    	  //移除游轮名称下拉项的属性,去除缓存影响.-s--//
   	   	  $("#selectedCruiseshipDate").find("option").removeAttr("selected");
    	  var selCruiseshipDateElt=$(obj).val();
    	  var selCruiseshipNameElt=$("#selectedCruiseshipName option:selected").val(); 
    	  //发送ajax查询选中游轮选中船期的船期信息
    	  $.ajax({
    		url:"${ctx}/activity/manager/getInfoByUuidDate?uuid="+selCruiseshipNameElt+"&shipdate="+selCruiseshipDateElt,
    		type:"post",
    		success:function(res){
    			var infos=res.detailInfo;
    			//拼接查询到船期的具体信息
    			$("#stockInfo").find("tbody").empty();
    			var htmlAdd="";
    			for(var j=0;j<infos.length;j++){
    				   htmlAdd+='<tr>';
    				   htmlAdd+='<td class="tc" cruiseGroupControlId="'+infos[j].detailId+'">'+(j+1)+'</td>';
    				   htmlAdd+='<td  class="tc">'+infos[j].cabin_name+'</td>'
    				   htmlAdd+='<td  class="tc">'+infos[j].stock_amount+'</td>';
    				   htmlAdd+='<td  class="tc">'+infos[j].free_positon+'</td>';
    				   htmlAdd+='</tr>';
    			   }
    			   $("#stockInfo").find("tbody").append(htmlAdd);
    			   //解决弹窗刷新造成的船期名称下拉列表项被重置默认的问题
    			   var sel=$("#selectedCruiseshipDate").find("option");
    			   sel.each(function(i,n){
    				  //alert(this.value) 
    				  if(n.value==selCruiseshipDateElt){
    					 $(n).attr("selected","selected");
    				  } 
    				 // alert(n.value+"--"+(n.value==cruiseshipUUid));
    			   });   
    			   jbox_create_group_control_pop(this);
    		}
    	  });
      } */
       //----223-tgy--船期onchage事件--e--//
       
       
    //====================t1t2 begin======================
    /**
     * t1t2 打通 
     * 产品发布时团期的   同行价  的keyup 事件要做特殊处理   需要 同步 修改  相应的 quauq 价
     * 正负数字验证
     */
   function validNum(dom){
    	
    	debugger; 
    	var activityKind = '${param.kind}';  
    	var thisvalue = $(dom).val();
		//t1t2增加供应价服务费计算，在QUAUQ价基础上增加1%的交易服务费
		var rate = '${chargeRate}',supplyvalue = '';
		if(""!=thisvalue){
			supplyvalue = thisvalue * rate + parseFloat(thisvalue);
		}
    	if(thisvalue.length >15){
    		top.$.jBox.info("改价金额位数不合法", "提示");
    		thisvalue = '0.00';
    	}

    	var minusSign = false;
    	if(thisvalue){
    		if(/^\-/.test(thisvalue)){
    			minusSign = true;
    			thisvalue = thisvalue.substring(1);
    		}
    		thisvalue = thisvalue.replace(/[^\d\.]/g,"").replace(/(\.\d{3}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
    		var txt = thisvalue.split(".");
    		thisvalue = txt[0]+(txt.length>1?"."+txt[1]:"");
    		if(minusSign){
    			thisvalue = thisvalue;
    		}
    		$(dom).val(thisvalue);
    	}
    	
    	
    	debugger;
    	if(activityKind=='2'){//只有散拼产品才做如下操作
    		
    		var inputName = $(dom).attr("name");

    		/*
    		 *同行价发布时修改处理
    		 *1.如同行价修改后的值不为空，则要重新计算相应的quauq价
    	     *2.如同行价修改后的值为空，把相应的quauq价置空，且变为只读状态
    	     *
    		 */
    		if(inputName == "settlementAdultPrice"){//同行成人
    			
    			if(""!=thisvalue){
    				//var adultQuauqPrice = getQuauqPrice(thisvalue,adultQuauqPriceStrategy);
        			$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").val("");
        			//同行不为空后，相应的quauq价  要 变得  可 编辑
        			//$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").removeAttr("readonly");
					//增加供应价成人
					$(dom).parents('tr:first').find("input[name='supplyAdultPrice']").val("");
    			}else{
    				$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").val("");
    				$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").attr("readonly","readonly");
					$(dom).parents('tr:first').find("input[name='supplyAdultPrice']").val("");
    			}
    			    			
    		}else if(inputName == "settlementcChildPrice"){//同行儿童
    			
    			if(""!=thisvalue){
    			    //var childrenQuauqPrice = getQuauqPrice(thisvalue,childrenQuauqPriceStrategy);
    			    $(dom).parents('tr:first').find("input[name='quauqChildPrice']").val("");
    			    //同行不为空后，相应的quauq价  要 变得  可 编辑
    			    //$(dom).parents('tr:first').find("input[name='quauqChildPrice']").removeAttr("readonly");
					//增加供应价儿童
					$(dom).parents('tr:first').find("input[name='supplyChildPrice']").val("");
    			}else{
    				$(dom).parents('tr:first').find("input[name='quauqChildPrice']").val("");
    				//$(dom).parents('tr:first').find("input[name='quauqChildPrice']").attr("readonly","readonly");
					$(dom).parents('tr:first').find("input[name='supplyChildPrice']").val("");
    			}
    			
    		}else if(inputName == "settlementSpecialPrice"){//同行特殊人群
    			
    			if(""!=thisvalue){
    			    //var spicalQuauqPrice = getQuauqPrice(thisvalue,spicalQuauqPriceStratety);
    			    $(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").val("");
    			    //同行不为空后，相应的quauq价  要 变得  可 编辑
    			    //$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").removeAttr("readonly");
					//增加供应价特殊人群
					$(dom).parents('tr:first').find("input[name='supplySpecialPrice']").val("");
    			}else{
    				$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").val("");
    				//$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").attr("readonly","readonly");
					$(dom).parents('tr:first').find("input[name='supplySpecialPrice']").val("");
    			}
    			
    		}
    		/*
    	     *quauq价发布时修改处理
    	     *1.如quauq价修改时高 于 quauq 价，要给出提示信息，告知quauq价不能高于多少多少
    	     *2.如低改不做任何处理
    	     *3.如低改空    是否置为0
    	     *4.
    		 */
    		else if(inputName == "quauqAdultPrice"){//quauq成人
    			
    			if(""!=thisvalue){
    				
    				var settlementAdultPrice = $(dom).parents('tr:first').find("input[name='settlementAdultPrice']").val();
    				//var adultQuauqPrice = getQuauqPrice(settlementAdultPrice,adultQuauqPriceStrategy);
    				if(new Number(thisvalue)>new Number(settlementAdultPrice)){
    					//top.$.jBox.info("不能高于quauq策略价"+adultQuauqPrice, "提示");
						top.$.jBox.info("QUAUQ价应低于或等于同行价，请重新修改！", "提示");
    					$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").val(settlementAdultPrice);
    				}else {
						//增加供应价成人变化
						$(dom).parents('tr:first').find("input[name='supplyAdultPrice']").val(supplyvalue);
					}
    				
    			}else{
//    				var settlementAdultPrice = $(dom).parents('tr:first').find("input[name='settlementAdultPrice']").val();
//    				if(""!=settlementAdultPrice){
//	    				$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").val("0");
//    				}else{
//    					$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").val("");
//    				}
					$(dom).parents('tr:first').find("input[name='quauqAdultPrice']").val("");
					$(dom).parents('tr:first').find("input[name='supplyAdultPrice']").val("");
    			}
    			
    		}else if(inputName == "quauqChildPrice"){//quauq儿童
				if(""!=thisvalue){
    				
					var settlementcChildPrice = $(dom).parents('tr:first').find("input[name='settlementcChildPrice']").val();
    				//var childrenQuauqPrice = getQuauqPrice(settlementcChildPrice,childrenQuauqPriceStrategy);
    				if(new Number(thisvalue)>new Number(settlementcChildPrice)){
    					top.$.jBox.info("QUAUQ价应低于或等于同行价，请重新修改！", "提示");
    					$(dom).parents('tr:first').find("input[name='quauqChildPrice']").val(settlementcChildPrice);
    				}else {
						//增加供应价儿童变化
						$(dom).parents('tr:first').find("input[name='supplyChildPrice']").val(supplyvalue);
					}
					
    			}else{
//    				var settlementcChildPrice = $(dom).parents('tr:first').find("input[name='settlementcChildPrice']").val();
//    				if(""!=settlementcChildPrice){
//    					$(dom).parents('tr:first').find("input[name='quauqChildPrice']").val("0");
//    				}else{
//    				    $(dom).parents('tr:first').find("input[name='quauqChildPrice']").val("");
//    				}
					$(dom).parents('tr:first').find("input[name='quauqChildPrice']").val("");
					$(dom).parents('tr:first').find("input[name='supplyChildPrice']").val("");
    			}
    			
    		}else if(inputName == "quauqSpecialPrice"){//quauq特殊人群
				if(""!=thisvalue){
    				
					var settlementSpecialPrice = $(dom).parents('tr:first').find("input[name='settlementSpecialPrice']").val();
    				//var spicalQuauqPrice = getQuauqPrice(settlementSpecialPrice,spicalQuauqPriceStratety);
    				if(new Number(thisvalue)>new Number(settlementSpecialPrice)){
    					top.$.jBox.info("QUAUQ价应低于或等于同行价，请重新修改！", "提示");
    					$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").val(settlementSpecialPrice);
    				}else {
						//增加供应价特殊人群变化
						$(dom).parents('tr:first').find("input[name='supplySpecialPrice']").val(supplyvalue);
					}
					
    			}else{
    				
//    				var settlementSpecialPrice = $(dom).parents('tr:first').find("input[name='settlementSpecialPrice']").val();
//    				if(""!=settlementSpecialPrice){
//    					$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").val("0");
//    				}else{
//    					$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").val("");
//    				}
					$(dom).parents('tr:first').find("input[name='quauqSpecialPrice']").val("");
					$(dom).parents('tr:first').find("input[name='supplySpecialPrice']").val("");
    			}
    			
    		}
    	
    	}
    	
    }
      
      
    /**
	 * 点击团期生成  团期记录后处理
	 * 1.如果同行价的值不为空  则  根据 匹配到的价格策略中的方案进行进行计算，得出quauq价（价格方案中的最低价）。
	 * 2.如果同行价没有进行填写，则quauq 价 不得进行修改，改为只读
	 * 3.修改quauq价，不得低高于，价格方案中的最低价，在点下一步（ 2-》3 步时进行校验）
	 * 4.价格策略的结构如下：
	 * adultQuauqPriceStrategy = data.quauqPrice4Adult; //成人价策略      结构'1:200,2:10#1:100,2:10'  加200 减 10，
	 * childrenQuauqPriceStrategy = data.quauqPrice4Child;//儿童价策略     结构'1:100,2:10'  加100减10
	 * spicalQuauqPriceStratety = data.quauqPrice4SpicalPerson;//特殊人群价策略     结构'2:200,3:15'  减200，打15折
	 * 
	 */
	function afterClicka(adultQuauqPriceStrategy,childrenQuauqPriceStrategy,spicalQuauqPriceStratety){
		
		//1.获取新增行的  团期的  成了价    儿童价    特殊人群价
		var settlementAdultPrice = $("#contentTable").children("tbody").find("tr[class!=noteTr]").last().find("input[name='settlementAdultPrice']").val();
		var settlementcChildPrice = $("#contentTable").children("tbody").find("tr[class!=noteTr]").last().find("input[name='settlementcChildPrice']").val();
		var settlementSpecialPrice = $("#contentTable").children("tbody").find("tr[class!=noteTr]").last().find("input[name='settlementSpecialPrice']").val();
		
		//成人
		if(""==settlementAdultPrice){
			$("#contentTable").children("tbody").find("tr[class!=noteTr]").last().find("input[name='quauqAdultPrice']").attr("readonly","readonly");
		}else{
			var adultQuauqPrice = getQuauqPrice(settlementAdultPrice,adultQuauqPriceStrategy);
			$("#contentTable").children("tbody").find("tr[class!=noteTr]").last().find("input[name='quauqAdultPrice']").val(settlementAdultPrice);
		}
		
		//儿童
		if(""==settlementcChildPrice){
			$("#contentTable").children("tbody").find("tr[class!=noteTr]").last().find("input[name='quauqChildPrice']").attr("readonly","readonly");
		}else{
			var childrenQuauqPrice = getQuauqPrice(settlementcChildPrice,childrenQuauqPriceStrategy);
			$("#contentTable").children("tbody").find("tr[class!=noteTr]").last().find("input[name='quauqChildPrice']").val(settlementcChildPrice);
		}
		
		//特殊人群
		if(""==settlementSpecialPrice){
			$("#contentTable").children("tbody").find("tr[class!=noteTr]").last().find("input[name='quauqSpecialPrice']").attr("readonly","readonly");
		}else{
			var spicalQuauqPrice = getQuauqPrice(settlementSpecialPrice,spicalQuauqPriceStratety);
			$("#contentTable").children("tbody").find("tr[class!=noteTr]").last().find("input[name='quauqSpecialPrice']").val(settlementSpecialPrice);
		}
		
	}
	
	/**
	 * 根据价格方案获取策略价：最低quauq价
	 * 
	 */
     function getQuauqPrice(srcPrice,srcPriceStrategy){
		
		//srcPriceStrategy 为空  直接 返回 """;
		if(!srcPriceStrategy){
			return "";
		}
		
		//debugger;
		var srcPriceStrategyArray = srcPriceStrategy.split("#");
		var quauqPriceArray = new Array();
		for(var i = 0;i < srcPriceStrategyArray.length; i++) {
			var quauqPrice = srcPrice;//
			var priceStrategyArray =  srcPriceStrategyArray[i].split(",");
			for(var j = 0;j < priceStrategyArray.length; j++) {
				var  srcPriceStrategyItem = priceStrategyArray[j].split(":");
				if("1" == srcPriceStrategyItem[0]){
					quauqPrice = new Number(quauqPrice) + new Number(srcPriceStrategyItem[1]);
				}else if("2" == srcPriceStrategyItem[0]){
					quauqPrice = new Number(quauqPrice) - new Number(srcPriceStrategyItem[1]);
				}else if("3" == srcPriceStrategyItem[0]){
					quauqPrice = new Number(quauqPrice)*new Number(100-srcPriceStrategyItem[1])/100;
				}
			}
			if(quauqPrice<0){
				quauqPrice = 0;
			}
			quauqPriceArray.push(quauqPrice);
		}
		var minQuauqPrice = getMaxMinNum(quauqPriceArray,"min");
		//var maxQuauqPrice = getMaxMinNum(quauqPriceArray,"max");
		return  xround(minQuauqPrice,2);
		
	}
	
	/**
	 *
	 * @param arr:array operated
	 * @param type:expected max,min
	 * @returns get max/min value in specified array or cosole log error
	 */
    function getMaxMinNum(arr,type){
        if(type==''||type==null||type=='undefined'){
            //console.log("Type is undefined.Please specified!");
            return false;
        }
        if('max'==type){
           return Math.max.apply(null,arr);
        }
        if('min'==type){
            return Math.min.apply(null,arr);
        }
    }
	
	
	/**
	 *四舍五入，保留位数为 scale
	 *num:要格式化的数字
	 *scale: 保留的位数
	 */
	function xround(num,scale){
		var resultTemp;
		if(scale>0){
			resultTemp = Math.round(num * Math.pow(10, scale)) / Math.pow(10, scale);
		}
		return resultTemp.toFixed(2);
	}

	// 选择目标区域执行的操作，原先使用tags:treeselect标签，现在需要和旅游线路联动，所以单独拿出来进行处理。
	function selectTargetArea(){
		top.$.jBox.open("iframe:${ctx}/tag/treeselect?url="+encodeURIComponent("/activity/manager/filterTreeData1?kind=${param.kind}")
				+"&checked=true&selectIds="+$("#targetAreaId").val(),"选择区域", 320, 420,{
			buttons:{"确定":"ok","关闭":true}, submit:function(v, h, f){
				if (v=="ok"){
					var tree = h.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
					var ids = [], names = [], nodes = [];
					nodes = tree.getCheckedNodes(true);

					//青岛凯撒目的地选择出境游时显示activityForm.jsp中的部门团号缩写div
					var companyUUID="${fns:getUser().company.uuid }";
					if(companyUUID=='7a8177e377a811e5bc1e000c29cf2586'){
						if(nodes.length>0){
							for(var i=0; i<nodes.length; i++) {
								if(nodes[0].id=='1' && nodes[0].name=='目的地'){
									if(nodes[i].id=='100000' && nodes[i].name=='出境游'){
										//选择了出境游时显示
										$("#deptCodeDiv").show();
										$("#deptCodeFlag").val('1');
										break;
									}else{
										//未选择出境游时隐藏
										$("#deptCodeDiv").hide();
										$("#deptCodeFlag").val('0');
									}
								}
							}
						}else{
							//未选择出境游时隐藏
							$("#deptCodeDiv").hide();
							$("#deptCodeFlag").val('0');
						}
					}
					for(var i=0; i<nodes.length; i++) {//
						if (nodes[i].isParent){
							continue; // 如果为复选框选择，则过滤掉父节点
						}
						ids.push(nodes[i].id);
						names.push(nodes[i].name);
					}
					$("#targetAreaId").val(ids);
					$("#targetAreaName").val(names);
					$("#targetAreaName").focus();
					$("#targetAreaName").blur();
					// 如果存在线路玩法，发送异步请求，进行联动操作
					var $touristLineId = $("#targetAreaId").parent().parent().next().find("#touristLineId");
					if ($touristLineId[0]){
						$.ajax({
							type: "POST",
							async:false,
							url: "${ctx}/activity/manager/getMatchLine",
							data: {areaIds:JSON.stringify(ids)},
							success: function(data){
								var optStr = "";
								if (data.length > 0){
									for (var i = 0; i < data.length; i++){
										optStr += "<option value= '" + data[i].touristLineId + "'>" + data[i].touristLineName + "</option>";
									}
									optStr += "<option value= '0'>其他</option>";
								}else {
									optStr += "<option value='0' selected='selected'>其他</option>";
								}
								$("#touristLineId").html(optStr);
							}
						});

					}
				}
			},loaded:function(h){
				$(".jbox-content", top.document).css("overflow-y","hidden");
			},persistent:true
		});
	}
      
      
    //====================t1t2 begin======================   
      
       
       
       
</script>   

   <style type="text/css"> 
    label{cursor:inherit;}
    .mydivclass{float:left;}
    </style>
</head>
<body>

<%--     <div style=" float:left;">--%>
<content tag="three_level_menu">
    <li class="active"><a href="${ctx}/activity/manager/form?kind=${param.kind}">发布新产品</a></li>         
</content>
<div class="produceDiv">
<div style="width:100%; height:20px;"></div>
<div id="dh" class="add_img"></div>
	<input type="hidden" id="ctx" value="${ctx}"/>
    <form:form id="addForm" modelAttribute="travelActivity" action="${ctx}/activity/manager/save" method="post"
            class=" form-search" enctype="multipart/form-data">
        <tags:message content="${message}" />           
		
	<shiro:hasPermission name="calendarLoose:book:order">
		<c:set var="tuijian" value="true"></c:set>
		<input type="hidden" id="tuijian" value="${tuijian }">
	</shiro:hasPermission>
	
	<c:set var="groupPriceFlag" value="false"></c:set>
	<shiro:hasPermission name="price:project">
		<c:if test="${param.kind ne 6 and param.kind ne 7 and param.kind ne 10 }">
		<c:set var="groupPriceFlag" value="true"></c:set>
		</c:if>
	</shiro:hasPermission>
	<input type="hidden" id="groupPriceFlag" value="${groupPriceFlag }">
        
        <input type="hidden" id="activityKind" name="activityKind" value="${param.kind}"/>
        <div id="oneStepDiv" class="mod_information_dzhan">
<%--            <div id="oneStepTitle" class="mod_information_d"><span style="padding-left:20px; font-weight:bold;">填写产品基本信息</span></div>--%>
            <div class="kongr"></div>
            <div class="kongr"></div>
                <div class="mod_information_d7"></div>
                <div class="kongr"></div>
                <div class="kongr"></div>
            <div id="oneStepContent" class="mod_information_dzhan_d error_add1">
                <div class="mod_information_d1">
                    <div class="fl">
                    <label><span class="xing">*</span>产品名称：</label>
                    <form:input path="acitivityName" cssClass="inputTxt" maxlength="50"/>
                    <span class="acitivityNameSizeSpan" style="color:#b2b2b2">还可输入<span class="acitivityNameSize">50</span>个字</span>
                 	<c:if test="${not empty param.recordId}">
                		<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/eprice/manager/project/ePriceInfoReadOnly/${param.recordId}/1')">询价记录</a>
                		<input type="hidden" name="estimatePriceRecordId" value="${param.recordId}"/>
                 	</c:if>&nbsp;
                 	</div>
                    <c:if test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}">
                        <c:if test="${param.kind ne '5'}">
                            <div class="mod_information_d_tb fl">
                                <label><span class="xing">*</span>团号标识：</label>
                                <form:select path="groupNoMark" itemValue="key" itemLabel="value" class="valid" style="width:150px;">
                                    <form:option value="" >请选择</form:option>
                                    <form:options items="${groupNoMarks}" />
                                </form:select>
                            </div>
                             <div style="padding-top:2px;">
                                 <input id="groupNoName" class="valid" name="groupNoName" value="" maxlength="" type="text" style="display: none;width:50px;" readonly="readonly" />
                             </div>
                         </c:if>
                         <!-- 名扬国际自由行产品 -->
                         <c:if test="${param.kind eq '5'}">
                             <div class="mod_information_d_tb fl">
                                 <label><span class="xing">*</span>团号标识：</label>
                                 <select id="groupNoMark" name="groupNoMark" disabled="disabled">
                                     <option value="ZYX" selected="selected">自由行产品</option>
                                 </select>
                             </div>
                             <div style="padding-top:2px;">
                                 <input id="groupNoName" class="valid" name="groupNoName" value="ZYX" maxlength="" type="text" style="width:50px;" readonly="readonly" />
                             </div>
                         </c:if>
                    </c:if>
                </div>
                <div class="kongr"></div>
                
                <div class="mod_information_d2">
                     <label><span class="xing">*</span>出发城市：</label>
                     <form:select path="fromArea" itemValue="key" itemLabel="value">
                        <form:option value=" ">请选择</form:option>
                        <form:options items="${fromAreas}" />
                    </form:select>
                </div>
                <div class="mod_information_d2 ">
                     <label><span class="xing">*</span>目的地：</label>
					<div class="input-append">
						<input id="targetAreaId" name="targetAreaIdList" type="hidden" value="${targetAreaIds}"/>
						<input id="targetAreaName" name="targetAreaNameList" readonly="readonly" type="text" value="${targetAreaNames}"/>
						<a id="targetAreaButton" href="javascript:" class="btn" style="_padding-top:6px;" onclick="selectTargetArea()">&nbsp;选择&nbsp;</a>
					</div>
                </div>
				<c:if test="${param.kind eq '2'}">
					<div class="mod_information_d2 ">
						<label><span class="xing">*</span>游玩线路：</label>
						<form:select path="touristLineId" itemValue="key" itemLabel="value"  >
							<form:option value="">请选择</form:option>
							<form:option value="0">其他</form:option>
						</form:select>
					</div>
					<div class="kongr"></div>
				</c:if>
                <c:if test="${param.kind eq '10'}">
	                <div class="mod_information_d2">
	                     <label><span class="xing">*</span>返回城市：</label>
	                     <form:select path="backArea" itemValue="key" itemLabel="value">
	                        <form:option value=" ">请选择</form:option>
	                        <form:options items="${fromAreas}" />
	                    </form:select>
	                </div>
	                <div class="kongr"></div>
                </c:if>
                <c:if test="${param.kind ne '10' and param.kind ne '2'}">
	                <div class="mod_information_d2 mod_information_d2jt">
	                      <label><span class="xing">*</span>交通方式：</label>
	                    <form:select path="trafficMode" itemValue="key" itemLabel="value" onchange="trafficchg()" >
	                        <form:option value="">请选择</form:option>
	                        <form:options items="${trafficModes}" />
	                    </form:select>
	                    <a target="_self" href="javascript:;" class="ml25" onclick="linkAirTicket1('${ctx}')" style="display:none;">关联机票产品</a>
	                    <span class="linkAir" style="display: none">
								<span onclick="showAirInfo(this)" class="linkAir-spn">展开关联机票产品信息</span>
							<div class="airInfo-arrow" style="display: block;">
								<i></i>
							</div>
						</span>
	                 </div>
	                 <div class="airInfo" style="display: none;">
	                 	<input type="hidden" class="activityAirTicketId" name="airTicketId" value=""/>
	                 </div>
	                <div class="kongr"></div>
					<div class="mod_information_d2">
	                    <label>旅游类型：</label>
	                     <form:select path="travelTypeId" itemValue="key" itemLabel="value">
	                       <form:option value="">请选择</form:option>
	                       <form:options items="${travelTypes}" />
	                   </form:select>
	                </div>
					<div class="mod_information_d2">
	                     <label>产品类型：</label>
	                     <form:select id="activityTypeId" path="activityTypeId" itemValue="key" itemLabel="value" >
	                       <form:option value="">请选择</form:option>
	                       <form:options items="${productTypes}" />
	                   </form:select>
					</div>
                </c:if>
				 <%--散拼的增加了线路玩法，为了排版单独拎出来了--%>
				<c:if test="${param.kind eq '2'}">
					<div class="mod_information_d2">
						<label>旅游类型：</label>
						<form:select path="travelTypeId" itemValue="key" itemLabel="value">
							<form:option value="">请选择</form:option>
							<form:options items="${travelTypes}" />
						</form:select>
					</div>
					<div class="mod_information_d2">
						<label>产品类型：</label>
						<form:select id="activityTypeId" path="activityTypeId" itemValue="key" itemLabel="value" >
							<form:option value="">请选择</form:option>
							<form:options items="${productTypes}" />
						</form:select>
					</div>
					<div class="mod_information_d2 mod_information_d2jt">
						<label><span class="xing">*</span>交通方式：</label>
						<form:select path="trafficMode" itemValue="key" itemLabel="value" onchange="trafficchg()" >
							<form:option value="">请选择</form:option>
							<form:options items="${trafficModes}" />
						</form:select>
						<a target="_self" href="javascript:;" class="ml25" onclick="linkAirTicket1('${ctx}')" style="display:none;">关联机票产品</a>
	                    <span class="linkAir" style="display: none">
								<span onclick="showAirInfo(this)" class="linkAir-spn">展开关联机票产品信息</span>
							<div class="airInfo-arrow" style="display: block;">
								<i></i>
							</div>
						</span>
					</div>
					<div class="airInfo" style="display: none;">
						<input type="hidden" class="activityAirTicketId" name="airTicketId" value=""/>
					</div>
					<div class="kongr"></div>
				</c:if>

                <div class="mod_information_d2">
                      <label>产品系列：</label>
                      <form:select path="activityLevelId" itemValue="key" itemLabel="value" >
                           <form:option value="">请选择</form:option>
                           <form:options items="${productLevels}" />
                       </form:select>
                </div>
				<c:if test="${param.kind ne '10' and param.kind ne '2'}">
              		<div class="kongr"></div>
           		</c:if>
                <div class="mod_information_d2">
                    <div class="divDurationDays">
                        <label for="spinner"  class="txt2 fl"><span class="xing">*</span>行程天数：</label>
                        <input id="activityDuration" class="spinner" name="activityDuration" maxlength="3" value="" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
                        <span style="padding-left:5px;">天</span>
                    </div>
                </div>
               
<%--                <div id="outAreaLabel" class="mod_information_d2">--%>
<%--                    <label>离境口岸：</label>--%>
<%--                    <form:select path="outArea" itemValue="key" itemLabel="value">--%>
<%--                        <form:option value="">请选择</form:option>--%>
<%--                        <form:options items="${outAreas}" />--%>
<%--                    </form:select>--%>
<%--                </div>--%>
                
               <div class="mod_information_d2">
               		<!-- <label><span class="xing">*</span>领队：</label> -->
                	<label>领队：</label>
                	<form:input path="groupLead" maxlength="20"/>
               </div>
				<c:if test="${param.kind eq '2' or param.kind eq '10'}">
					<div class="kongr"></div>
				</c:if>
               
               <!-- 如果是单团产品，则选择谁可看见此产品并预定 -->
				<div class="mod_information_d2 no-wrap user_show">
					<label>可见用户：</label>
					<tags:treeselect id="opUser" name="opUserIdList" value="${opUserIds}"  labelName="opUserNameList" labelValue="${opUserNames}"  
                        title="用户" url="/activity/manager/filterOpUserTreeData" checked="true"/>
				</div>
               
<%--                <shiro:hasPermission name="activity:deparment:choose"> --%>
<%-- 	               <c:if test="${fns:getUser().company.id eq 68 }"> --%>
<!-- 		               <div class="kongr"></div> -->
<!-- 			       		<div class="mod_information_d2"> -->
<!-- 							<label class="control-label">部门：</label> -->
<%-- 			                <tags:treeselect id="treeDept" name="deptId" value="${department.parent.id}" labelName="parent.name" labelValue="${department.parent.name}" --%>
<%-- 		 						title="部门" url="/sys/department/treeData" extId="${department.id}" /> --%>
<!-- 						</div> -->
<%-- 	               </c:if> --%>
<%--                 </shiro:hasPermission> --%>
<%--                	<c:set var="departmentSet" value="${fns:getDepartmentByJob() }"/> --%>
<%-- 	    		<c:choose> --%>
<%-- 	    			<c:when test="${fn:length(departmentSet) gt 1}"> --%>
<!-- 				       <div class="mod_information_d2"> -->
<!-- 				      		<label><span class="xing">*</span>部门：</label> -->
<%-- 				      		<form:select path="deptId"> --%>
<%-- 				      			<form:option value="">请选择</form:option> --%>
<%-- 				               	<c:forEach items="${departmentSet }" var="department"> --%>
<%-- 				               		<form:option title="${department.name }" value="${department.id }">${department.name }</form:option> --%>
<%-- 				               	</c:forEach> --%>
<%-- 				    		</form:select> --%>
<!-- 				        </div>		 -->
<%-- 	    			</c:when> --%>
<%-- 	    			<c:otherwise> --%>
<%-- 	    				<c:forEach items="${departmentSet }" var="department"> --%>
<%-- 		    				<form:hidden path="deptId" value="${department.id }"/> --%>
<%-- 		               	</c:forEach> --%>
<%-- 	    			</c:otherwise> --%>
<%-- 	    		</c:choose> --%>
				<div class="kongr"></div>

            

               <div class="mod_information_d2 no-wrap">

					<label class="control-label"><span class="xing">*</span>部门：</label>
					<div class="input-append"><!-- 160727 选择按钮与输入框分离修正 添加类input-append -->
						<input id="deptId" name="deptId" type="hidden" value="${!empty deptMap ? deptMap.dept_id : '' }">
						<input id="departmentName" name="departmentName" readonly="readonly" type="text" value="${!empty deptMap ? deptMap.deptName : '' }" style="">
						<a id="departmentButton" href="javascript:" class="btn departmentButton" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
					</div>
				</div>
               
               
               <div class="kongr"></div>
               
<%--                <div class="mod_information_d2">--%>
<%--                     <label><span class="xing">*</span>产品分类：</label>--%>
<%--                      <form:select path="overseasFlag" itemValue="key" >--%>
<%--                        <form:option value="0">国内</form:option>--%>
<%--                        <form:option value="1">国外</form:option>--%>
<%--                    </form:select>--%>
<%--                </div>--%>
<%--                <div class="mod_information_d2">--%>
<%--                     <label><span class="xing">*</span>旅游类型：</label>--%>
<%--                      <form:select path="travelTypeId" itemValue="key" itemLabel="value">--%>
<%--                        <form:option value="">请选择</form:option>--%>
<%--                        <form:options items="${travelTypes}" />--%>
<%--                    </form:select>--%>
<%--                </div>--%>
                 
<%--                 <div class="mod_information_d2">--%>
<%--                       <label><span class="xing">*</span>产品系列：</label>--%>
<%--                       <form:select path="activityLevelId" itemValue="key" itemLabel="value" >--%>
<%--                            <form:option value="">请选择</form:option>--%>
<%--                            <form:options items="${productLevels}" />--%>
<%--                        </form:select>--%>
<%--                 </div>--%>
<%--				<div class="mod_information_d2">--%>
<%--                      <label><span class="xing">*</span>产品类型：</label>--%>
<%--                      <form:select path="activityTypeId" itemValue="key" itemLabel="value" >--%>
<%--                        <form:option value="">请选择</form:option>--%>
<%--                        <form:options items="${productTypes}" />--%>
<%--                    </form:select>--%>
<%--				</div>--%>
                 
                <input type="hidden" id="activitySerNum" name="activitySerNum" maxlength="500" value="${travelActivity.activitySerNum}" onblur="activitySerNumEmpty()"
                onafterpaste="this.value=this.value.replace(/[^\u4e00-\u9fa5\w\(\)\（\）\-]/g,'')" onKeyUp="this.value=this.value.replace(/[^\u4e00-\u9fa5\w\(\)\（\）\-\-]/g,'')"/>
	                
				<div class="mod_information_d2 add-paytype mydivclass" >
					<label><span class="xing">*</span>付款类型：</label><font id="payModeText" style="display: none"></font>
					<input type="checkbox" class="ckb_mod" id="payMode_deposit" name="payMode" value="1" onclick="paychg(this)"/><font>订金占位&#12288;&#12288;</font>
                    <label for="spinner" class="txt2" id="label_advance"><span class="xing" id="advance_xing" style="display: none;">*</span>保留时限：</label>
                   
                    <span id="payMode_deposit_span" class="payModeSpan">
                    	<input id="remainDays_deposit" class="spinner02" name="remainDays_deposit" maxlength="3" value="" 
                    	onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'');">
                    	<span style="padding-left:5px;">天</span>
                    </span>
                    
                    <span id="payMode_deposit_span" class="payModeSpan">
                    	<input id="remainDays_deposit_hour" class="spinner01" name="remainDays_deposit_hour" maxlength="2" value="" 
                    	onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
                    	<span style="padding-left:5px;">时</span>
                    </span>
                    
                    <span id="payMode_deposit_span" class="payModeSpan">
                    	<input id="remainDays_deposit_fen" class="spinner_fen" name="remainDays_deposit_fen" maxlength="2" value="" 
                    	onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
                    	<span style="padding-left:5px;">分</span>
                    </span>
                    
                    <br id="label_n" />
                    <label id="label_n" >&nbsp;</label>
                    <input type="checkbox" class="ckb_mod" id="payMode_advance" name="payMode" value="2" onclick="paychg(this)"/><font>预占位&#12288;&#12288;&#12288;</font>
                    <label for="spinner" class="txt2" id="label_advance"><span class="xing" id="advance_xing" style="display: none;">*</span>保留时限：</label>
                    <span id="payMode_advance_span" class="payModeSpan">
                    	<input id="remainDays_advance" class="spinner02" name="remainDays_advance" maxlength="3" value="" 
                    	onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
                    	<span style="padding-left:5px;">天</span>
                    </span>
                    
                     <span id="payMode_advance_span" class="payModeSpan">
                    	<input id="remainDays_advance_hour" class="spinner01" name="remainDays_advance_hour" maxlength="2" value="" 
                    	onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
                    	<span style="padding-left:5px;">时</span>
                    </span>
                    
                    <span id="payMode_advance_span" class="payModeSpan">
                    	<input id="remainDays_advance_fen" class="spinner_fen" name="remainDays_advance_fen" maxlength="2" value="" 
                    	onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
                    	<span style="padding-left:5px;">分</span>
                    </span>
                    
                    <c:if test="${param.kind eq '2' or param.kind eq '10'}">
	                    <br id="label_n" /><label id="label_n" >&nbsp;</label>
	                    <input type="checkbox" class="ckb_mod" id="payMode_data" name="payMode" value="4" onclick="paychg(this)"/><font>资料占位&#12288;&#12288;</font>
	                    <label for="spinner" class="txt2" id="label_data"><span class="xing" id="data_xing" style="display: none;">*</span>保留时限：</label>
	                    <span id="payMode_data_span" class="payModeSpan">
	                    	<input id="remainDays_data" class="spinner02" name="remainDays_data" maxlength="3" value="" 
	                    	onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
	                    	<span style="padding-left:5px;">天</span>
	                    </span>
	                    
	                    <span id="payMode_data_span" class="payModeSpan">
                    		<input id="remainDays_data_hour" class="spinner01" name="remainDays_data_hour" maxlength="2" value="" 
                    		onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
                    	<span style="padding-left:5px;">时</span>
                    	</span>
                    
                    	<span id="payMode_data_span" class="payModeSpan">
                    		<input id="remainDays_data_fen" class="spinner_fen" name="remainDays_data_fen" maxlength="2" value="" 
                    		onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
                    	<span style="padding-left:5px;">分</span>
                    	</span>
	                    
	                    <br id="label_n" /><label id="label_n" >&nbsp;</label>
	                    <input type="checkbox" class="ckb_mod" id="payMode_guarantee" name="payMode" value="5" onclick="paychg(this)"/><font>担保占位&#12288;&#12288;</font>
	                    <label for="spinner" class="txt2" id="label_guarantee"><span class="xing" id="guarantee_xing" style="display: none;">*</span>保留时限：</label>
	                    <span id="payMode_guarantee_span" class="payModeSpan">
	                    	<input id="remainDays_guarantee" class="spinner02" name="remainDays_guarantee" maxlength="3" value="" 
	                    	onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
	                    	<span style="padding-left:5px;">天</span>
	                    </span>
	                    
	                    <span id="payMode_guarantee_span" class="payModeSpan">
                    		<input id="remainDays_guarantee_hour" class="spinner01" name="remainDays_guarantee_hour" maxlength="2" value="" 
                    		onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
                    	<span style="padding-left:5px;">时</span>
                    	</span>
                    
                    	<span id="payMode_guarantee_span" class="payModeSpan">
                    		<input id="remainDays_guarantee_fen" class="spinner_fen" name="remainDays_guarantee_fen" maxlength="2" value="" 
                    		onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
                    	<span style="padding-left:5px;">分</span>
                    	</span>
	                    
	                    <br id="label_n" /><label id="label_n">&nbsp;</label>
	                    <input type="checkbox" class="ckb_mod" id="payMode_express" name="payMode" value="6" onclick="paychg(this)"/><font>确认单占位&#12288;</font>
	                    <label for="spinner" class="txt2" id="label_express"><span class="xing" id="express_xing" style="display: none;">*</span>保留时限：</label>
	                    <span id="payMode_express_span" class="payModeSpan">
	                    	<input id="remainDays_express" class="spinner02" name="remainDays_express" maxlength="3" value="" 
	                    	onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
	                    	<span style="padding-left:5px;">天</span>
	                    </span>
	                    
	                    <span id="payMode_express_span" class="payModeSpan">
                    		<input id="remainDays_express_hour" class="spinner01" name="remainDays_express_hour" maxlength="2" value="" 
                    		onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
                    	<span style="padding-left:5px;">时</span>
                    	</span>
                    
                    	<span id="payMode_express_span" class="payModeSpan">
                    		<input id="remainDays_express_fen" class="spinner_fen" name="remainDays_express_fen" maxlength="2" value="" 
                    		onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
                    	<span style="padding-left:5px;">分</span>
                    	</span>
	                    
                    </c:if>
                    
                    <br id="label_n"/>
                    <label id="label_n" >&nbsp;</label>
                    <input type="checkbox" class="ckb_mod" id="payMode_full" name="payMode" value="3"/><font>全款支付</font>
                    
                    <br id="label_n" />
                    <label id="label_n" >&nbsp;</label>
                    <input type="checkbox" class="ckb_mod" id="payMode_op" name="payMode" value="7"/><font>计调确认占位</font>
                    
					<c:if test="${fns:getUser().company.orderPayMode eq 1}">
						<br id="label_n" />
	                    <label id="label_n" >&nbsp;</label>
	                    <input type="checkbox" class="ckb_mod" id="payMode_cw" name="payMode" value="8"/><font>财务确认占位</font>
					</c:if>
				</div>
				<c:if test="${fns:getUser().company.uuid eq '7a8177e377a811e5bc1e000c29cf2586' }">
					<c:if test="${param.kind eq '1' or param.kind eq '2'}">
						<input type="hidden" value="0" id="deptCodeFlag"/>
						<div id="deptCodeDiv" class="mydivclass" style="margin-left:160px;display:none;">
							<c:set var="crtsdeptCodeSet" value="${fns:getCrtsdeptCodeSet() }"/>
							<label>团号部门缩写：</label>
							<select id="deptCode" name="deptCode" style="width:125px;">
								<c:forEach items="${crtsdeptCodeSet }" var="department">
									<option value="${department.code }">${department.name }${department.code }</option>
								</c:forEach>
							</select>
						</div>
					</c:if>
				</c:if>
                <div class="kongr"></div>

<%--                <div class="mod_information_d2 lianyun">--%>
<%--	                <div class="lianyun_select">--%>
<%--	                    <label>联运类型：</label>--%>
<%--	                    <select id="intermodalType" name="intermodalType" onchange="transportchg()">--%>
<%--	                        <option id="national" value="1">全国联运</option><option selected="selected" id="none" value="0">无联运</option><option id="group" value="2">分区联运</option>--%>
<%--	                    </select>--%>
<%--	                </div>--%>
<%--	                <div id="nationalTrans" class="transport_city" style="display:none">--%>
<%--	                    <label>联运价格：</label>--%>
<%--	                    <select id="templateCurrency" class="sel-currency" nowclass="rmb" name="templateCurrency">--%>
<%--	                    	<c:forEach items="${currencyList}" var="currency">--%>
<%--							    <option value="${currency.id}" addclass="${currency.currencyStyle}">${currency.currencyName}</option>--%>
<%--							</c:forEach>--%>
<%--	                    </select>--%>
<%--	                    <input class="valid rmb" id="intermodalAllPrice" name="intermodalAllPrice" maxlength="8" type="text" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" />--%>
<%--	                </div>--%>
<%--	                <div id="groupTrans" class="transport_city" style="display:none">--%>
<%--	                    <p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart0" name="intermodalGroupPart" maxlength="50" type="text" /><label>联运价格：</label>--%>
<%--	                    <select class="sel-currency" nowclass="rmb" name="templateCurrency">--%>
<%--	                    	<c:forEach items="${currencyList}" var="currency">--%>
<%--							    <option value="${currency.id}" addclass="${currency.currencyStyle}">${currency.currencyName}</option>--%>
<%--							</c:forEach>--%>
<%--	                    </select>--%>
<%--	                    <input class="valid rmb" id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="8" type="text" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" /><a class="ydbz_s transportAdd" onclick="javascript:transportAdd(this, 0);">增加</a></p>--%>
<%--	                </div>--%>
<%--	            </div>--%>
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
                <div class="add2_line add_alone_price_top">
                	<div class="add2_line_text">填写团期价格信息：</div>
               		<div class="add2-money">
               			<label><span class="xing"></span>币种选择：</label>
						<select id="selectCurrency" name="selectCurrency" >
							<c:forEach items="${currencyList}" var="currency">
							<option value="${currency.id}" <c:if test="${currency.currencyStyle eq 'rmb'}"> selected="selected" </c:if>  var="${currency.convertLowest}" id="${currency.currencyMark}">${currency.currencyName}</option>
							</c:forEach>
						</select>
						<span></span>
               		</div>
               	</div>
                <div style="width:100%; height:20px;"></div>
                <div class="add2_nei add_alone_price_tb">
                   <table class="table-mod2-group">
						<!--区分供应商 -->
						<input type="hidden" value="${fns:getUser().company.id}" id="companyId"/>
						<input type="hidden" value="${fns:getUser().company.uuid}" id="companyUUID"/>
                   		<div class="choCur" style="display: none;">
                   			<dl class="choose-currency">
                            	<dt>选择币种</dt>
                                <dd>
	                   			<c:forEach items="${currencyList}" var="currency" varStatus="s">
	                   				<p id="${currency.id}" class="<c:if test="${s.index eq '0'}">p-checked</c:if>" name="${currency.currencyStyle}" addclass="${currency.currencyMark}">${currency.currencyName}</p>
	                   			</c:forEach>
	                   			</dd>
                   			</dl>
                   		</div>
                <c:choose>
				<%--    -----游轮产品----- 	 --%>
                   	<c:when test="${param.kind eq '10'}">
                   		<select id="spaceType" name="spaceType" style="display:none">
                   			<c:forEach items="${fns:getDictList('cruise_type')}" var="dict">
	                   			<option value="${dict.value }">${dict.label }</option>
                   			</c:forEach>
                   		</select>
                   		<tr>
                   		   	<td class="add2_nei_table"><span class="fr">1/2人同行价：</span></td>
	                       	<td class="add2_nei_table_typetext">
	                       		<input id="settlementAdultPriceDefine" type="text" maxlength="8" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="validNum(this)" onafterpaste="validNum(this)"/>
	                	   	</td>
	                       	<td class="add2_nei_table"><span class="fr">3/4人同行价：</span></td>
	                       	<td class="add2_nei_table_typetext">
	                       		<input  id="settlementcChildPriceDefine" type="text" maxlength="8" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="validNum(this)" onafterpaste="validNum(this)"  />
                       	   	</td>
                   		</tr>
                   		<tr>
                   		   	<td class="add2_nei_table"><span class="fr">1/2人直客价：</span></td>
	                       	<td class="add2_nei_table_typetext">
	                       		<input id="suggestAdultPriceDefine" type="text" maxlength="8" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="validNum(this)" onafterpaste="validNum(this)"/>
                      	   	</td>
	                       	<td class="add2_nei_table"><span class="fr">3/4人直客价：</span></td>
	                       	<td class="add2_nei_table_typetext">
	                       		<input id="suggestChildPriceDefine" type="text" maxlength="8" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="validNum(this)" onafterpaste="validNum(this)"/>
                       		</td>
                   		</tr>
                   	</c:when>
                <%--    -----游轮产品----- 	 --%>
                   	<c:otherwise>
                     <tr>
                       <td class="add2_nei_table"><span class="fr">成人同行价：</span></td>
                       <td class="add2_nei_table_typetext">
                       		<input id="settlementAdultPriceDefine" type="text" maxlength="14" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="validNum(this)" onafterpaste="validNum(this)"/>
                	   </td>
                       <td class="add2_nei_table"><span class="fr">儿童同行价：</span></td>
                       <td class="add2_nei_table_typetext"><input  id="settlementcChildPriceDefine" type="text" maxlength="14" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="validNum(this)" onafterpaste="validNum(this)"  /></td>
                       <td class="add2_nei_table"><span class="fr">特殊人群同行价：</span></td>
                       <td class="add2_nei_table_typetext"><input id="settlementSpecialPriceDefine" type="text" maxlength="14" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="validNum(this)" onafterpaste="validNum(this)"/></td>
                       <td class="add2_nei_table"><label class="label-dw">特殊人群最高人数：   </label> </td>
                        <td class="add2_nei_table_typetext">                      
		                    	<input id="maxPeopleCountDefine"  name="maxPeopleCountDefine" type="input" class="inputTxt" maxlength="8" value=""  style="width:60px;height:18px;"
		                    	onafterpaste="this.value=this.value.replace(/\D|^0.+/g,'')" onkeyup="this.value=this.value.replace(/\D|^0.+/g,'')"/>
		                    	<span style="padding-left:5px;">人</span>
	               		</td>
                       <td class="add2_nei_table_typetext"><div class="marks-people"><label class="label-dw">备注：</label>
                       <form:input path="specialRemark" cssClass="ipt-otherPeople" flag="istips" maxlength="50"/>
                       <span class="ipt-tips">特殊人群备注</span></div></td>

                     </tr>
                     <c:if test="${param.kind eq '2'}">
	                     <tr>
	                       <td class="add2_nei_table"><span class="fr">成人直客价：</span></td>
	                       <td class="add2_nei_table_typetext"><input id="suggestAdultPriceDefine" type="text" maxlength="14" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="validNum(this)" onafterpaste="validNum(this)"/></td>
	                       <td class="add2_nei_table"><span class="fr">儿童直客价：</span></td>
	                       <td class="add2_nei_table_typetext"><input id="suggestChildPriceDefine" type="text" maxlength="14" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="validNum(this)" onafterpaste="validNum(this)"/></td>
	                       <td class="add2_nei_table"><span class="fr">特殊人群直客价：</span></td>
	                       <td class="add2_nei_table_typetext"><input id="suggestSpecialPriceDefine" type="text" maxlength="14" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="validNum(this)" onafterpaste="validNum(this)"/></td>
	                     </tr>
                     </c:if>
                  	</c:otherwise>
                  </c:choose>
                  <tr>
                    <td class="add2_nei_table"><span class="fr">需交订金：</span></td>
                    <td class="add2_nei_table_typetext"><input id="payDepositDefine" type="text" maxlength="8" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="validNum(this)" onafterpaste="validNum(this)"/></td>
                    <td class="add2_nei_table"><span class="fr">单房差：</span></td>
                    <td class="add2_nei_table_typetext"><input id="singleDiffDefine" type="text" maxlength="8" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="validNum(this)" onafterpaste="validNum(this)"/></td>
                    <c:if test="${param.kind ne '10'}">
                    	<td class="add2_nei_table_typetext" colspan="2"><label class="units-style label-dw">单位：</label><form:input path="singleDiffUnit" value="间 / 夜" cssClass="ipt-fjc" maxlength="50" /></td>
                  
                  	<td class="add2_nei_table"><label class="label-dw">儿童最高人数：   </label> </td>
                    <td class="add2_nei_table_typetext">                      
                    	<input id="maxChildrenCountDefine"  name="maxChildrenCountDefine" type="input" class="inputTxt" maxlength="8" value=""  style="width:60px;height:18px;"
                    	onafterpaste="this.value=this.value.replace(/\D|^0.+/g,'')" onkeyup="this.value=this.value.replace(/\D|^0.+/g,'')"/>
                    	<span style="padding-left:5px;">人</span>
	               	</td>	</c:if>
                  </tr>
                     <c:if test="${fn:length(groupCodeRule) ne 0}">
	                     <tr>
	                     	<td class="add2_nei_table">团号类别：</td>
	                     	<td>
								<!-- onChange="changeGroupRule(this)" -->
								<select id="groupCodeRule" style="width: 77px;">
									<c:forEach items="${groupCodeRule}" var="codeRule">
										<option value="${codeRule[0]}">${codeRule[1]}</option>
									</c:forEach>
								</select>
	                     	</td>
	                     </tr>
                     </c:if>
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
                        <a class="groupDate" onclick="regisFun()">选择日期</a>
                        <input class="inputTxt selectedDate" style="background-color:#CCCCCC;width:80px; display: none;" readonly="">
                        <input id="groupOpenDate" class="inputTxt" name="groupOpenDateBegin" readonly style="display: none;"/> 
                  		<input id="groupCloseDate" class="inputTxt" name="groupCloseDateEnd" readonly  style="display: none;"/>&nbsp;&nbsp;                   
                </div>  
              
<%--             <div class="release_next_add">--%>
<%--                 <input class="btn btn-primary" type="button" onclick="writeGroupDate()" value="提交团期"/>--%>
<%--               </div>           --%>
            </div>
            <div id="secondStepEnd">
                <div style="width:100%; height:30px;"></div>
                <div style="width:100%; height:10px;"></div>
					<div id="groupTable" style="margin-top:8px;">
                        <!--S--109需求增加设置优惠额度-->
                         <c:if test="${param.kind eq '2' && (fns:getUser().company.uuid eq '7a81c5d777a811e5bc1e000c29cf2586' 
                         || fns:getUser().company.uuid eq '75895555346a4db9a96ba9237eae96a5') }">
	                        <div class="discount-setting-container tr">
	                            <input class="btn btn-primary" onclick="jbox__discount_setting_pop_fab();" value="设置优惠额度" type="button">
	                            <!--仅为展示效果，后端需要适当判断进行提示，-->
	                            <!--<input class="btn btn-primary" onclick="jbox__nosel_group_discount_setting_pop_pop_fab();" value="未选择团期设置优惠额度" type="button">-->
	                        </div>
                        </c:if>
						<table id="contentTable" class="table table-striped table-bordered table-condensed table-mod2-group psf_table_son change_hint_sty" style="width:98%;margin:10px auto">
							<thead>
								<tr>
                                    
									<c:if test="${param.kind == 2}">
									<c:if test="${fns:getUser().company.uuid eq '7a81c5d777a811e5bc1e000c29cf2586' || fns:getUser().company.uuid eq '75895555346a4db9a96ba9237eae96a5' }">
									<th width="4%" rowspan="2">全选
                                        <input class="none-height-input" name="allChk" onclick="checkall_new(this)" type="checkbox"></th>
                                    </c:if>
									<shiro:hasPermission name="calendarLoose:book:order">
										<%--<th rowspan="2" width="2%">推荐</th>--%>
									</shiro:hasPermission>
									</c:if>
									<th rowspan="2" width="5%">出团日期</th>
									<th rowspan="2" width="5%">截团日期<span><br>提前天数<br/><select id="closeBeforeDays" name="closeBeforeDays" disabled="disabled"></select></span></th>
									<th rowspan="2" width="4%">应付账期</th>
									<%-- <c:if test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' }">
										<th rowspan="2" width="7%" style="display:none;">团号</th>
									</c:if>
									<c:if test="${fns:getUser().company.uuid ne '7a81b21a77a811e5bc1e000c29cf2586' }">
										<th rowspan="2" width="7%"><span class="xing">*</span>团号</th>
									</c:if> --%>
									<!-- 由于bug13135,产品决定放开条件限制,应付账期和团号都展示 -->
									<th rowspan="2" width="5%"><span class="xing">*</span>团号</th>
									<th rowspan="2" width="5%">签证国家<span><br><input id="visaCountryCopy" type="text" name="visaCountryCopy" class="visacountrycopy_input" disabled="disabled"/><br/><input id="visaCopyBtn" type="button" value="复制" onclick="visaCopy()" class="visa_copy" disabled="disabled"/></span></th>
									<th rowspan="2" width="5%">资料截止<span><br>提前天数<br/><select id="visaBeforeDays" name="visaBeforeDays" disabled="disabled"></select></span></th>
									<c:set var="colspanNum" value="3"></c:set>
									<c:if test="${param.kind eq '10'}">
										<th rowspan="2" width="4%">舱型</th>
										<c:set var="colspanNum" value="2"></c:set>
									</c:if>
									<shiro:hasPermission name="price:project">
										<c:if test="${param.kind ne 6 and param.kind ne 7 and param.kind ne 10 }">
										<th rowspan="2" width="4%">酒店房型</th>
										</c:if>
									</shiro:hasPermission>
									<th width="11%" class="t-th2" colspan="${colspanNum }">同行价</th>
									<c:if test="${param.kind eq '2' or param.kind eq '10'}">
										<th width="11%" class="t-th2" colspan="${colspanNum }">
											<shiro:hasPermission name="looseProduct:operation:requiredStraightPrice">
												<span class="xing">*</span>
											</shiro:hasPermission>
											直客价
										</th>
									</c:if>
									
									<!-- 对应需求号  0426  t1t2 打通  -->
									<%-- <c:if test="${param.kind eq '2'}">
										<th width="11%" class="t-th2" colspan="${colspanNum }">QUAUQ价</th>
										<th width="11%" class="t-th2" colspan="${colspanNum }">供应价<br>（含服务费）</th>
									</c:if> --%>
									
									<c:if test="${param.kind ne '10'}">
									<th width="3%" rowspan="2">儿童最</br>高人数</th>
									<th width="3%" rowspan="2">特殊人</br>群最高人数</th>
									</c:if>
									<th width="4%" rowspan="2">需交</br>订金</th>
									<th width="4%" rowspan="2">单房差</th>
									<th width="4%" rowspan="2">预收<c:if test="${param.kind eq '10'}">/间</c:if><span><br><input id="planPositionCopy" name="planPositionCopy" class="visacountrycopy_input" maxlength="3" disabled="disabled"/><br/><input id="planPositionBtn" type="button" value="复制" onclick="positionCopy(this)" class="visa_copy" disabled="disabled"/></span></th>
									<th width="4%" rowspan="2">余位<c:if test="${param.kind eq '10'}">/间</c:if><span><br><input id="freePositionCopy" name="freePositionCopy" class="visacountrycopy_input" maxlength="3" disabled="true"/><br/><input id="freePositionBtn" type="button" value="复制" onclick="positionCopy(this)" class="visa_copy" disabled="true"/></span></th>
									
									<!-- 懿洋假期   显示发票税一项    对应需求号   0258  -->
									<c:if test="${fns:getUser().company.uuid eq 'f5c8969ee6b845bcbeb5c2b40bac3a23'}">
									    <th width="2%" rowspan="2">发票税<span><br><input id="invoiceTaxCopy" name="invoiceTaxCopy" class="visacountrycopy_input" style="width: 19px;" onafterpaste="checkValue(this)" onKeyUp="checkValue(this)" onfocus="checkValue(this)" disabled="true"/>%<br/><input id="invoiceTaxBtn" type="button" value="复制" onclick="positionCopy4InvoiTax(this)" class="visa_copy" disabled="true"/></span></th>
                                    </c:if>
									
									<th width="3%" rowspan="10">操作<span><br></span>
									<a href="javascript:void(0)" onclick="delAllGroupDate()">全部<br>删除</a>
								</tr>
								<tr>
									<c:choose>
										<c:when test="${param.kind eq '10' }">
											<th>1/2人</th>
											<th>3/4人</th>
											<th>1/2人</th>
											<th>3/4人</th>
										</c:when>
										<c:otherwise>
											<th>成人</th>
											<th>儿童</th>
											<th>特殊人群</th>
											<c:if test="${param.kind eq '2'}">
												<th>成人</th>
												<th>儿童</th>
												<th>特殊人群</th>
											</c:if>
											<!-- 对应需求号  0426  t1t2 打通  -->
											<%-- <c:if test="${param.kind eq '2'}">
												<th>成人</th>
												<th>儿童</th>
												<th>特殊人群</th>
												<th>成人</th>
												<th>儿童</th>
												<th>特殊人群</th>
											</c:if> --%>
										</c:otherwise>
									</c:choose>
								</tr>
							</thead>
							<tbody>
								<tr id="emptygroup">
									<td name="countLineNum">暂无价格信息，请选择日期</td>
								</tr>
							</tbody>
						</table>
					</div>
                <div id="secondStepBtn" class="mod_information_dzhan_d">
                    <div class="release_next_add">
                             <input class="btn btn-primary" type="button" onclick="secondToOne()" value="上一步"/>
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
                <div class="batch"  style="margin-top:10px;">
                	<label class="batch-label company_logo_pos">
                	<!-- 139大洋国旅产品非必填 -->
                	<c:choose>
                		<c:when test="${fns:getUser().company.uuid ne '7a81a03577a811e5bc1e000c29cf2586' }">
                			产品行程介绍：
                		</c:when>
                		<c:otherwise>产品行程介绍：</c:otherwise>
                	</c:choose> 
                	</label>
					<input type="button" name="introduction" value="选择文件" class="mod_infoinformation3_file" onClick="uploadFiles('${ctx}',null,this)"/>
					<ol class="batch-ol">
					<input type="hidden" name="introduction_name" value="产品行程介绍"/>
					</ol>
					<c:if test="${fns:getUser().company.uuid ne '7a81a03577a811e5bc1e000c29cf2586' }">
						<input type="text" value="" id="introductionVaildator" style="width:1px; height:1px; margin:0; padding:0; border:none; position:absolute; z-index:-1;" />
					</c:if>
					<!-- 139大洋国旅产品非必填 -->
                </div>
                <div class="mod_information_d7"></div>
                <div class="batch" style="margin-top:10px;">
                	<label class="batch-label company_logo_pos">自费补充协议：</label>
					<input type="button" name="costagreement" value="选择文件" class="mod_infoinformation3_file" onClick="uploadFiles('${ctx}',null,this)"/>
					<ol class="batch-ol">
					<input type="hidden" name="costagreement_name" value="自费补充协议"/>
					</ol>
                </div>
                <div class="mod_information_d7"></div>
                <div class="batch" style="margin-top:10px;">
                	<label class="batch-label company_logo_pos">其他补充协议：</label>
					<input type="button" name="otheragreement" value="选择文件" class="mod_infoinformation3_file" onClick="uploadFiles('${ctx}',null,this)"/>
					<ol class="batch-ol">
					<input type="hidden" name="otheragreement_name" value="其他补充协议"/>
					</ol>
                </div>
                <div class="mod_information_d7"></div>
                <div class="batch" style="margin-top:10px;">
                	<label class="batch-label company_logo_pos">其他文件：</label>
					<input type="button" name="otherfile" value="选择文件" class="mod_infoinformation3_file" onClick="uploadFiles('${ctx}',null,this)"/>
					<ol class="batch-ol">
					<input type="hidden" name="otherfile_name" value="其他文件"/>
					</ol>
                </div>
                <c:if test="${param.kind ne '10' }">
	                <div class="mod_information_d7"></div>
	                <div id="visaData">
		                <div class="batch" id="visaflag" style="margin-top:8px;margin-bottom:8px;">
		                    <label>签证资料文件：</label><input type="button" class="mod_infoinformation3_file" value="+添加" onclick="addvisafile(this)">                  
		                </div>
	                </div>
                </c:if>

				<c:if test="${param.kind eq '2'}">
					<shiro:hasPermission name="looseProduct:operation:requiredStraightPrice">
					<!--微信新文件上传 Strat-->
					<div class="mod_information_d7"></div>
					<div class="we_chat_update">
						<label class="batch-label company_logo_pos">上传微信图片：</label>
						<span class="hint_span"> 建议上传JPG、JPEG、GIF、PNG格式，大小不超过2M </span>
						<div class="preview_content">
							<div class="preview_con_left">
								<div class="col-md-9">
									<div class="img-container">
										<img id="defaultImgId" src="${ctxStatic}/images/wechat/weixindefaultpic.png" alt="Picture" class="aaa">
									</div>
								</div>
								<div class="control_img">
									<div class="btn-group">
										<label class="btn btn-primary btn-upload" for="inputImage">
											<input class="sr-only" id="inputImage" name="file" type="file">
											<span class="docs-tooltip" onclick="uploadImage();" data-toggle="tooltip">
                                              <i class="fa fa-upload"></i> 上传图片
                                            </span>
										</label>
										<button class="btn btn-primary" data-method="setDragMode" data-option="crop" type="button" title="Crop">
                                            <span class="docs-tooltip" data-toggle="tooltip" title="裁剪图片">
                                              <span class="icon icon-crop"></span>
                                            </span>
										</button>
										<button class="btn btn-primary" data-method="clear" type="button" title="Clear">
                                            <span class="docs-tooltip" data-toggle="tooltip" title="取消裁剪">
                                              <span class="icon icon-remove"></span>
                                            </span>
										</button>
										<button class="btn btn-primary" data-method="zoom" data-option="0.2" type="button" title="Zoom In">
											<span class="docs-tooltip" data-toggle="tooltip" title="放大图片">
											  <span class="icon icon-zoom-in"></span>
											</span>
										</button>
										<button class="btn btn-primary" data-method="zoom" data-option="-0.2" type="button" title="Zoom Out">
											<span class="docs-tooltip" data-toggle="tooltip" title="缩小图片">
											  <span class="icon icon-zoom-out"></span>
											</span>
										</button>
										<button class="btn btn-primary" data-method="reset" type="button" title="Reset">
											<span class="docs-tooltip" data-toggle="tooltip" title="重置">
											  <span class="icon icon-refresh"></span>
											</span>
										</button>
									</div>
								</div>

                                <%--删去--%>
                                <%--<div class="container">--%>
                                <%--<div class="row">--%>
								<div class="col-md-9 disable_btn">
									<div class="btn-group btn-group-crop">
										<button class="btn btn-primary" data-method="getCroppedCanvas" type="button">
											<span class="docs-tooltip" data-toggle="tooltip"></span>
										</button>
									</div>
									<!-- Show the cropped image in modal -->
									<div class="modal fade docs-cropped" id="getCroppedCanvasModal" aria-hidden="true" aria-labelledby="getCroppedCanvasTitle" role="dialog" tabindex="-1">
										<div class="modal-dialog">
											<div class="modal-content">
												<div class="modal-header">
													<button class="close" data-dismiss="modal" type="button" aria-hidden="true">&times;</button>
													<h4 class="modal-title" id="getCroppedCanvasTitle">Cropped</h4>
												</div>
												<div class="modal-body"></div>
												<div class="modal-footer">
													<button class="btn btn-primary" data-dismiss="modal" type="button">上传</button>
													<button class="btn btn-primary" type="button">Close</button>
												</div>
											</div>
										</div>
									</div>
								</div>
									<%--</div>--%>
									<%--</div>--%>
									<%--删去--%>
							</div>

							<div class="preview_img list_previw">
								<ul>
									<li><div class="img-preview preview-sm"></div></li>
								</ul>
							</div>
							<div class="preview_img detail_previw">
								<div class="img-preview preview-md"></div>
							</div>
						</div>
					</div>
					<!--微信新文件上传 End-->
					</shiro:hasPermission>
				</c:if>

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
			        
	<div name="signtemplate" style="display:none;" class="mod_information_d6">
        <table border="0"  name="company_logo" style="vertical-align:middle;">
            <tr>                        
                <td><label style="width: auto;">国家/地区：</label></td>
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
                <td>
               	</td>
                <td>
                </td>
                <td>
                    <a href="javascript:void(0)" class="mod_infoinformation3_del" onclick="removefile('确定删除该文件吗',this)">删除</a>
                </td>
            </tr>
        </table>
        <ol class="batch-ol"></ol>
        <input class="signmaterial_name" name="signmaterial_name" value="签证资料" type="hidden" />
    </div>
    
    <!-- QU-SDP-微信分销模块start 为散拼产品添加上传微信广告图片功能  yang.gao 2017-01-06 -->
     <c:if test="${param.kind eq '2'}">
     	<shiro:hasPermission name="looseProduct:operation:requiredStraightPrice">
	    	<!--div class="mod_information_d7"></div>
	        <div class="batch" style="margin-top:10px;">
	            <label class="batch-label company_logo_pos">微信分销广告图片：</label>
				<input type="button" name="distributionAdImg" value="选择文件" class="mod_infoinformation3_file" onClick="uploadSingleFileByParam('${ctx}',null,this,'.jpg;*.jpeg;*.png;*.gif','2MB')"/>
				<ol class="batch-ol">
					<input type="hidden" name="distributionAdImg_name" value="微信分销广告图片"/>
				</ol>
	    	</div-->
	    </shiro:hasPermission>
     </c:if>
     <!-- QU-SDP-微信分销模块end -->
                <div class="mod_information_d7"></div>
                <c:if test="${param.kind eq '10' }">
					<div class="seach25 seach100 pro-marks1">
						<p class="fbold f14">备注：</p>
						<p class="seach_r">
						<textarea name="remarks" maxlength="22167"></textarea>
						</p>
					</div>
                </c:if>
       		 </div>     	
                 <div class="release_next_add">
                      <input class="btn btn-primary" type="button" onclick="ThirdToSecond()" value="上一步"/>
                      <input class="btn btn-primary gray" type="button" onclick="history.go(-1)" value="放&nbsp;&nbsp;弃"/>
<%--                      <input class="btn btn-primary" type="button" onclick="submitForm(1);" value="保存草稿"/>--%>
                      <input class="btn btn-primary" type="button" onclick="submitForm(2);" value="提交发布"/>
                  </div>
       		</div>     
 	 	</form:form>
    <div style="height:2px; width:100%; clear:both;"></div>
    <style type="text/css">

        #product_level{ top:404px !important; left:268px !important; width:125px;}
        #product_type{ top:300px !important; left:1100px !important; width:125px;}
    </style>    
</div>





<!--E关联机票-分配弹窗-->
<!--S--C109--设置优惠额度弹窗-->

<div id="discount-setting-pop" class="display-none">
    <div class="discount-setting-pop">
        <div class="first-part">
            <div class="control-display">展示所选择的团</div>
            <ul>
            </ul>
        </div>
        <div class="mod_details2_tabletype">
            <table class="table" style="width:100%;">
                <thead>
                <tr>
                    <th colspan="3">同行价优惠额度</th>
                </tr>
                <tr>
                    <th class="tc" width="12%">成人</th>
                    <th class="tc" width="9%">儿童</th>
                    <th class="tc" width="9%">特殊人群</th>
                </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
    </div>
</div>

<!--S--C109--设置优惠额度弹窗-->
<!--S--C109--查看优惠额度弹窗-->

<div id="view-discount-setting-pop"  class="display-none">
    <div class="discount-setting-pop">
        <div class="mod_details2_tabletype">
            <table class="table" style="width:100%;">
                <thead>
                <tr>
                    <th colspan="3">同行价优惠额度</th>
                </tr>
                <tr>
                    <th class="tc" width="12%">成人</th>
                    <th class="tc" width="9%">儿童</th>
                    <th class="tc" width="9%">特殊人群</th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
</div>

<!--S--C109--查看优惠额度弹窗-->
<!--S--C109--修改优惠额度弹窗-->

<div id="modify-discount-setting-pop"  class="display-none">
    <div class="discount-setting-pop">
        <div class="mod_details2_tabletype">
            <table class="table" style="width:100%;">
                <thead>
                <tr>
                    <th colspan="3">同行价优惠额度</th>
                </tr>
                <tr>
                    <th class="tc" width="12%">成人</th>
                    <th class="tc" width="9%">儿童</th>
                    <th class="tc" width="9%">特殊人群</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="tc">￥<input type="text" name="discount" value="1000"/></td>
                    <td class="tc">￥<input type="text" name="discount" value="1000"/></td>
                    <td class="tc">￥<input type="text" name="discount" value="1000"/></td>
                </tr>
                <tr>
                    <td class="tc">￥<input type="text" name="discount" value="1000"/></td>
                    <td class="tc">￥<input type="text" name="discount" value="1000"/></td>
                    <td class="tc">￥<input type="text" name="discount" value="1000"/></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

<!--S--C109--修改优惠额度弹窗-->
<!--S--C109--未选择团期设置优惠提示-->
<div id="nosel-group-discount-setting-pop" class="display-none">
    <div class="nosel-group-discount-setting-pop">
        请选择团！
    </div>
</div>
<!--E--C109--未选择团期设置优惠提示-->
</div>

	<div id="pricingTableTem" class="display-none">
		<table id="pricePlanTable" name="pricePlanTable" class="table table-striped table-bordered table-condensed table-mod2-group psf_table_son hoverstyle">
			<thead>
				<tr>
					<th width="5%" rowspan="2">序号</th>
					<th width="30%" class="tc" rowspan="2">价格方案</th>
					<th width="15%" class="tc" colspan="3">同行价</th>
					<c:if test="${param.kind == 2}">
						<th width="15%" class="tc" colspan="3">直客价</th>
					</c:if>
					<th width="25%" rowspan="2">备注</th>
					<th width="5%" rowspan="2">操作</th>
				</tr>
				<tr>
					<th>成人</th>
					<th>儿童</th>
					<th>特殊人群</th>
					<c:if test="${param.kind == 2}">
						<th>成人</th>
						<th>儿童</th>
						<th>特殊人群</th>
					</c:if>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td name="index">1</td>
					<td class="tc">
						<p>
							<span>
								<label>酒店：</label>
							</span>
							<input width="2%" type="text" name="hotel" class="pricing-scheme" maxlength="50">
							<span>
								<label>房型：</label>
							</span>
							<input width="2%" type="text" name="houseType" class="pricing-scheme" maxlength="50">
							
							<label class="addAndRemove"><em class="add-select" name="addPricing"></em>
							<em class="remove-selected" name="deletePricing"></em></label>
						</p>
					</td>
					<td class="tr tdCurrency">
						<span name="currencyMark">¥</span>
						<input width="4%" type="text" name="thcr" class="ipt-currency" data-type="amount" maxlength="8">
					</td>
					<td class="tr tdCurrency">
						<span name="currencyMark">¥</span>
						<input width="4%" type="text" name="thet" class="ipt-currency" data-type="amount" maxlength="8">
					</td>
					<td class="tr tdCurrency">
						<span name="currencyMark">¥</span>
						<input width="4%" type="text" name="thts" class="ipt-currency" data-type="amount" maxlength="8">
					</td>
					<c:if test="${param.kind == 2}">
						<td class="tr tdCurrency">
							<span name="currencyMark">¥</span>
							<input width="4%" type="text" name="zkcr" class="ipt-currency" data-type="amount" maxlength="8">
						</td>
						<td class="tr tdCurrency">
							<span name="currencyMark">¥</span>
							<input width="4%" type="text" name="zket" class="ipt-currency" data-type="amount" maxlength="8">
						</td>
						<td class="tr tdCurrency">
							<span name="currencyMark">¥</span>
							<input width="4%" type="text" name="zkts" class="ipt-currency" data-type="amount" maxlength="8">
						</td>
					</c:if>
					<td class="tc">
						<input type="text" name="remark" class="nopadding" maxlength="50"/>
					</td>
					<td>
						<em class="add-select" name="addPricingRow"></em>
						<em class="remove-selected" name="deletePricingRow"></em>
					</td>
				</tr>
			</tbody>
		</table>
	</div>



</body>
</html>
