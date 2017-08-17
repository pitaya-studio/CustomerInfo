/**
 * Created by quauq on 2016/10/25.
 */
function saveOldGroup(obj){
    var validatecount = 0;
    $tr = $(obj).parents("tr:first");
    $next_tr = $tr.next();
    $next_next_tr = $tr.next().next();
    //0071-进来先清空提示span-防止bug-s
    $("#modTable").find("[name='span4visaCountry']").empty();
    //0071-进来先清空提示span-防止bug-e
    /*C463备注超长处理 */
    //TODO：remarks-containers是什么意思？
    $("#modTable").find("input[name='groupNotes']").each(function(index,obj){
        $(obj).parent("div:first").removeClass("remarks-containers");
    });
    /*$("#contentTable").find("input[name='groupRemark']").each(function(index,obj){
        $(obj).parent("div:first").removeClass("remarks-containers");
    });
    $("#contentTable").find("input[name='groupNotes']").each(function(index,obj){
        $(obj).parent("div:first").removeClass("remarks-containers");
    });*/

    var activityKind = $("#activityKind").val();
    //var tuijian = $("#tuijian").val();
    //var hasTuijian = false;
   // if(orderType == 2 & tuijian == 'true'){
    //    hasTuijian = true;
   // }

    //add_groupsvalidator();
    add_modgroupsvalidator();

    //**************0071-签证国家长度限定为50字-s**********
    /*var arrCurrentVisaCountries=$('#secondStepMod [name="visaCountry"]');//现有签证国家
    //var arrAddVisaCountries=$('#groupTable [name="visaCountry"]');//新增签证国家
    var flag4VisaCountry=false;
    for(var i=0;i<arrCurrentVisaCountries.length;i++){
        if(arrCurrentVisaCountries[i].value.length>50){
            $("#secondStepMod #visaCountry"+(i+1)).after('<label name="span4visaCountry" style="display:block"  class="myerror">请输入一个长度最多是50的字符串</label>');
            flag4VisaCountry=true;
            validatecount++;
        }
    }*/
    //签证国家长度,不能超过50个字符串
    var arrCurrentVisaCountrie = $tr.find('input[name="visaCountry"]').val();
    if(arrCurrentVisaCountrie.length>50){
        $tr.find('input[name="visaCountry"]').after('<label name="span4visaCountry" style="display:block"  class="myerror">请输入一个长度最多是50的字符串</label>');
        //flag4VisaCountry=true;
        validatecount++;
        top.$.jBox.info("请先修改完错误再提交", "警告");
        top.$('.jbox-body .jbox-icon').css('top','55px');
        return validatecount;
    }


    /*for(var i=0;i<arrAddVisaCountries.length;i++){
        if(arrAddVisaCountries[i].value.length>50){
            $("#groupTable #visaCountry"+i).after('<label name="span4visaCountry" style="display:block"  class="myerror">请输入一个长度最多是50的字符串</label>');
            flag4VisaCountry=true;
            validatecount++;
        }
    }*/
   /* if(flag4VisaCountry==true){
        top.$.jBox.info("请先修改完错误再提交", "警告");
        top.$('.jbox-body .jbox-icon').css('top','55px');
        validatecount++;
        return validatecount;
    }*/
    //**************0071-签证国家长度限定为50字-e**********

    //**********************特殊人群数不能大于预收人数的校验-s*******//
    var flag4MaxPeopleCountPlanPosition=false;
    var flag4MaxChildrenCountPlanPosition=false;
    var flag4MaxCountPlanPosition=false;

    //当前占位
    var currentPositionNum = 0;
    var currentPositionPeopleNum = 0;
    var currentPositionChildrenFlag = false;
    var currentPositionPeopleFlag = false;
    var currentPositionElt = $tr.find("input[name='nopayPeoplePosition']");
    var currentChildrenPositionElt = $tr.find("input[name='nopayChildrenPosition']");
    //团号
    var groupCode = $tr.find("input[name='groupCode']").val();
    //最大特殊人群数
    var maxPeopleCountElt=$tr.find("input[name='maxPeopleCount']").val();
    //最大儿童数
    var maxChildrenCountElt=$tr.find("input[name='maxChildrenCount']").val();
    //预收
    var planPositionElt=$tr.find("input[name='planPosition']").val();
    //t1t2-v4 518bug-15889出团日期不能早于截团日期
    //出团日期
    var groupOpenDateElt = $tr.find("input[name='groupOpenDate']").val();
    //截团日期
    var groupCloseDateElt = $tr.find("input[name='groupCloseDate']").val();

    var currentDateFlag = false;
    //for(var i=0;i<maxChildrenCountElts.length;i++){
        if(groupOpenDateElt < groupCloseDateElt) {
            currentDateFlag = true;
            //break;
        }
        if(maxChildrenCountElt != "" && parseInt(maxChildrenCountElt)>parseInt(planPositionElt)){ //特殊人群数大于预收人数,则校验标志置为真
            flag4MaxChildrenCountPlanPosition=true;
            //break;//终止循环
        }
        if(currentChildrenPositionElt&& currentChildrenPositionElt !="" && maxChildrenCountElt != "" && parseInt(maxChildrenCountElt)<parseInt(currentChildrenPositionElt)){ //特殊人群数大于预收人数,则校验标志置为真
            currentPositionChildrenFlag=true;
            currentPositionNum = currentChildrenPositionElt;
            //break;//终止循环
        }
        if(maxChildrenCountElt != "" && maxPeopleCountElt != "" && (parseInt(maxChildrenCountElt)+parseInt(maxPeopleCountElt))>parseInt(planPositionElt)){ //特殊人群数大于预收人数,则校验标志置为真
            flag4MaxCountPlanPosition=true;
            //break;//终止循环
        }
    //}
    if(currentDateFlag){
        top.$.jBox.info("\"出团日期不能早于截团日期\"", "警告");
        validatecount++;
        return validatecount;
    }
    if(flag4MaxChildrenCountPlanPosition){
        top.$.jBox.info("\"儿童最高人数\"不能大于\"预收\"", "警告");
        validatecount++;
        return validatecount;
    }
    if(currentPositionChildrenFlag){
        validatecount++;
        top.$.jBox.info("\"儿童最高人数\"不能小于\"已占位儿童人数"+currentPositionNum+"\"", "警告");
        return validatecount;
    }
    //for(var i=0;i<maxPeopleCountElts.length;i++){
        if(maxPeopleCountElt != "" && parseInt(maxPeopleCountElt)>parseInt(planPositionElt)){ //特殊人群数大于预收人数,则校验标志置为真
            flag4MaxPeopleCountPlanPosition=true;
            //break;//终止循环
        }
        if(maxPeopleCountElt != "" && currentPositionElt && currentPositionElt != "" && currentPositionElt&&parseInt(maxPeopleCountElt)<parseInt(currentPositionElt)){ //特殊人群数大于预收人数,则校验标志置为真
            currentPositionPeopleFlag=true;
            currentPositionPeopleNum = currentPositionElt;
            //break;//终止循环
        }
        if(maxChildrenCountElt != "" && maxPeopleCountElt != ""  && (parseInt(maxChildrenCountElt)+parseInt(maxPeopleCountElt))>parseInt(planPositionElt)){ //特殊人群数大于预收人数,则校验标志置为真
            flag4MaxCountPlanPosition=true;
            //break;//终止循环
        }
    //}
    if(flag4MaxPeopleCountPlanPosition){
        validatecount++;
        top.$.jBox.info("\"特殊人群最高人数\"不能大于\"预收\"", "警告");
        return validatecount;
    }
    if(currentPositionPeopleFlag){
        validatecount++;
        top.$.jBox.info("\"特殊人群最高人数\"不能小于\"已占位特殊人群人数"+currentPositionPeopleNum+"\"", "警告");
        return validatecount;
    }

    if(flag4MaxCountPlanPosition){
        validatecount++;
        top.$.jBox.info("\"儿童与特殊人群最高人数之和\"不能大于\"预收\"", "警告");
        return validatecount;
    }

    //var keyVal= $("input[name^='groupCode']") ;  下面并没有使用此字段


    //var flag = validator1.form();
    var secondStepMod= $("#secondStepMod").validate({
        //var secondStepMod= $tr.parent().validate({
        rules:{
            activitySerNum:{
                remote: {
                    type: "POST",
                    url: "${ctx}/activity/manager/serNumRepeat?proId="+$("#proId").val()
                }
            }
        },
        messages:{
            activitySerNum:{remote:"编号已存在"}
        },
        errorPlacement: function(error, element) {
            if($(element).attr("id")=="introduction")
                error.appendTo (element.parent().parent());
            else if($(element).hasClass("spinner_day"))
                error.appendTo (element.parent().parent());
            else if ( element.is(":radio") )
                error.appendTo ( element.parent() );
            else if ( element.is(":checkbox") )
                error.appendTo ( element.parent() );
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
        ignore:":hidden[id!='fromArea'][id!='backArea'][id='secondStepEnd']"
    });
    var flag = secondStepMod.form();
    if(flag) {
        //现有团期的标题
        $("#secondStepTitle").find("span:eq(0)").addClass("ydExpand");
        secondToggle();
        //$("#secondStepEnd").disableContainer({blankText: ""});  //新团期的div
//					$("#secondStepEnd").find("input[name=groupCode]").next().attr("style","word-break: break-all;display: block;word-wrap: break-word;");
//				$("#secondStepMod").disableContainer({blankText : ""});
        //此处会将该团期隐藏
        /*var serialId = $(obj).parent().parent().find("input[name='serial']").val();
        $(obj).parent().parent().parent().find("#tr_" + serialId).hide();*/

        //$("#secondStepAdd").hide();  //填写价格信息部分
        //$("#secondModBtn").show();  //添加团期按钮
        //$("#secondSaveBtn").hide();   //添加团期下的保存按钮
//				$("#contentTable thead span").hide();
        /*if(hasTuijian){
            $("#modTable tbody").children().find("input[name=settlementAdultPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
            $("#modTable tbody").children().find("input[name=settlementcChildPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
            $("#modTable tbody").children().find("input[name=settlementSpecialPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
        }else{*/
            $tr.find("input[name=settlementAdultPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
            $tr.find("input[name=settlementcChildPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
            $tr.find("input[name=settlementSpecialPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
        //}
        if(activityKind == 2) {
           /* if(hasTuijian){
                $("#modTable tbody").children().find("input[name=suggestAdultPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
                $("#modTable tbody").children().find("input[name=suggestChildPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
                $("#modTable tbody").children().find("input[name=suggestSpecialPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
                $("#modTable tbody").children().find("input[name=payDeposit]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
                $("#modTable tbody").children().find("input[name=singleDiff]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
            }else{*/
                $tr.find("input[name=suggestAdultPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
                $tr.find("input[name=suggestChildPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
                $tr.find("input[name=suggestSpecialPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
                $tr.find("input[name=payDeposit]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
                $tr.find("input[name=singleDiff]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
            //}
        }/*else{
            $("#modTable tbody").children().find("input[name=payDeposit]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
            $("#modTable tbody").children().find("input[name=singleDiff]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
        }*/
        /*$("#contentTable tbody").children().find("td:eq(0)").attr("class","tc");
        $("#contentTable tbody").children().find("td:eq(1)").attr("class","tc");
        $("#contentTable tbody").children().find("td:eq(4)").attr("class","tc");*/
        //处理操作
        //$("#modTable tbody").children().find("p[name='targetHere']").children().remove();
        // 234需求，屏蔽上传出团通知
        //$("#modTable tbody").children().find("p[name='targetHere']").append('<a id="" name="uploadGroupFile" class="uploadGroupFileClass" onclick="uploadGroupFile(\'${ctx}\', this)" href="javascript:void(0)">上传出团通知</a>');
//	   		var groupPriceFlag = $("#groupPriceFlag").val();
//	    	if (groupPriceFlag  == "true") {
//	    		$("#modForm tbody [name=groupPriceTd]").each(function() {
//					if ($(this).is(":hidden")) {
//						$(this).prev().prev().prev().find("td[name='targetHere']").append('<a name=\"expandPricing\" show=\"true\">展开价格方案</a>');
//					} else {
//						$(this).prev().prev().prev().find("td[name='targetHere']").append('<a name=\"expandPricing\" show=\"true\">收起价格方案</a>');
//					}
//				});
//	    	}



        //C463现有団期列表备注
//	   		$("#modTable tbody tr[id^='groupRemark']").each(function(){
//	   			var $currNoteTr=$(this).find('input').val();
//	   			if($currNoteTr.trim()==""){
//	   				$(this).prev().prev().find("td[name='targetHere']").append('&nbsp;&nbsp;<a id="" name="uploadGroupFile" class="uploadGroupFileClass" onclick="expandRemark(this)" href="javascript:void(0)">备注</a>');
//	   			}else{
//	   				$(this).prev().prev().find("td[name='targetHere']").append('&nbsp;&nbsp;<a id="" name="uploadGroupFile" class="uploadGroupFileClass" onclick="expandRemark(this)" href="javascript:void(0)">备注<em class="groupNoteTipImg"></em></a>');
//	   			}
//	   			$(this).prev().hide();
//	   		})
        /*收起已有团期备注*/
        $next_next_tr.hide();
        $tr.find(".noteTr").hide();

        //C463新增団期列表备注
        if('f5c8969ee6b845bcbeb5c2b40bac3a23'=='${fns:getUser().company.uuid}'){//懿洋假期
            var nameStr = 'invoiceTax';
        }else{
            var nameStr = 'freePosition';
        }
        /*$("#contentTable tbody").children().find("[name="+nameStr+"]").each(function(index, obj){
            //修复：新增的団期保存后，再次点击修改，操作项消失的问题
            //$(obj).parent().next().empty();
            $(obj).parent().next().children().hide();
            // 234需求，屏蔽上传出团通知
            //$(obj).parent().next().append('<p><a id="" name="uploadGroupFile" onclick="uploadGroupFile(\'${ctx}\', this)" href="javascript:void(0)">上传出团通知</a></p>');
            var groupPriceFlag = $("#groupPriceFlag").val();
            if (groupPriceFlag  == "true") {
                $(this)
                if ($(this).is(":hidden")) {
                    $(obj).parent().next().append('<p style=\"margin-bottom:0\"><a id=\"expandPricing\" name=\"expandPricing\" show=\"true\">展开价格方案</a></p>');
                }
                else {
                    $(obj).parent().next().append('<p style=\"margin-bottom:0\"><a id=\"expandPricing\" name=\"expandPricing\" show=\"true\">收起价格方案</a></p>');
                }
            }
            if($(obj).parent().parent().next().find("input").val().trim() != ""){
                $(obj).parent().next().append('<p style=\"margin-bottom:0\"><a class="uploadGroupFileClass" name="uploadGroupFile" onclick="expandRemark4Add(this)">备注<em class="groupNoteTipImg"></em></a></p>');
            }else{
                $(obj).parent().next().append('<p style=\"margin-bottom:0\"><a class="uploadGroupFileClass" name="uploadGroupFile" onclick="expandRemark4Add(this)">备注</a></p>');
            }

            //散拼前三个都要合并
            if(2==orderType){
                $(obj).parent().parent().children().eq(0).attr("rowspan","1");
                $(obj).parent().parent().children().eq(1).attr("rowspan","1");
                if("${fns:getUser().company.uuid}"!='7a81a03577a811e5bc1e000c29cf2586'
                    && "${fns:getUser().company.uuid}"!='7a8175bc77a811e5bc1e000c29cf2586'
                    && "${fns:getUser().company.uuid}"!='7a8177e377a811e5bc1e000c29cf2586'
                    && "${fns:getUser().company.uuid}"!='7a81a26b77a811e5bc1e000c29cf2586'
                    && "${fns:getUser().company.uuid}"!='58a27feeab3944378b266aff05b627d2'
                    && "${fns:getUser().company.uuid}"!='5c05dfc65cd24c239cd1528e03965021'
                    && "${fns:getUser().company.uuid}"!='980e4c74b7684136afd89df7f89b2bee'
                ){//大洋、新行者、青岛凯撒、拉美途、日信观光、起航假期、骡子假期    2个
                    $(obj).parent().parent().children().eq(2).attr("rowspan","1");
                    if("${fns:getUser().company.uuid}"=='7a81c5d777a811e5bc1e000c29cf2586'){//优加国际 4个
                        $(obj).parent().parent().children().eq(3).attr("rowspan","1");
                    }
                }

            }/!*else{
                $(obj).parent().parent().children().eq(0).attr("rowspan","1");
                $(obj).parent().parent().children().eq(1).attr("rowspan","1");
            }*!/

            $(obj).parent().parent().next().hide();
        });*/


        // 对应需求号  223  点修改保存后  显示查看
        /*if(activityKind == 10) {
            //alert(isneedCruiseGroupControl)
            if(isneedCruiseGroupControl == '1'){
                $("#modTable tbody").children().find("p[name='targetHere']").append('' + '<a onClick="jbox_view_group_control_pop(this)" href="javascript:void(0)">查看关联团控表</a>' + '');
            }
        }*/

        //页面中已无uploadGroupFile
        /*$("#modTable tbody").children().find("[name='uploadGroupFile']").removeClass("displayClick");
        $("#modTable tbody").children().find("[name='uploadGroupFile']").show();*/

// 	   		for(var i = 5 ;i <= 12 ;i ++) {
// 	   			$("#contentTable tbody").children(":visible").find("td:eq("+i+")").attr("class","tr");
// 	   		}
        //remove_groupsvalidator();
        remove_modgroupsvalidator();
        //subCode(false,true);
        subCode(false,false);

        //生成币种Id串，并记录在每个团期后面
        /*var len = $("#contentTable tbody").find("input").length;
        if(len!=0){
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
        }*/

        //隐藏操作按钮
        $("#modTable .addAndRemove").hide();
        $("#modTable .add-select").hide();
        $("#modTable .remove-selected").hide();
        //$("#modForm .addAndRemove").hide();
       // $("#modForm .add-select").hide();
        //$("#modForm .remove-selected").hide();
        //$("#contentTable thead span").hide();
        //secondStepSaveFlag = true;
        saveOldGroupflag = true;
    }else{
        //c463
        /*$("#contentTable tbody").children().find("[name='freePosition']").each(function(index, obj){
            //散拼前三个都要合并
            if(2==orderType){
                $(obj).parent().parent().children().eq(0).attr("rowspan","1");
                $(obj).parent().parent().children().eq(1).attr("rowspan","1");
                if("${fns:getUser().company.uuid}"!='7a81a03577a811e5bc1e000c29cf2586'
                    && "${fns:getUser().company.uuid}"!='7a8175bc77a811e5bc1e000c29cf2586'
                    && "${fns:getUser().company.uuid}"!='7a8177e377a811e5bc1e000c29cf2586'
                    && "${fns:getUser().company.uuid}"!='7a81a26b77a811e5bc1e000c29cf2586'
                    && "${fns:getUser().company.uuid}"!='58a27feeab3944378b266aff05b627d2'
                    && "${fns:getUser().company.uuid}"!='5c05dfc65cd24c239cd1528e03965021'
                    && "${fns:getUser().company.uuid}"!='980e4c74b7684136afd89df7f89b2bee'
                ){//大洋、新行者、青岛凯撒、拉美途、日信观光、起航假期、骡子假期     2个
                    $(obj).parent().parent().children().eq(2).attr("rowspan","1");
                    if("${fns:getUser().company.uuid}"=='7a81c5d777a811e5bc1e000c29cf2586'){//优加国际 4个
                        $(obj).parent().parent().children().eq(3).attr("rowspan","1");
                    }
                }

            }/!*else{
                $(obj).parent().parent().children().eq(0).attr("rowspan","1");
                $(obj).parent().parent().children().eq(1).attr("rowspan","1");
            }*!/

            $(obj).parent().parent().next().hide();
            if($(obj).parent().parent().next().find("input").val().trim() != ""){
                $(obj).parent().next().find(".groupNote").children().remove();
                $(obj).parent().next().find(".groupNote").append('<em class="groupNoteTipImg"></em>');
            }
        });*/
        top.$.jBox.info("请先修改完错误再提交", "警告");
        top.$('.jbox-body .jbox-icon').css('top','55px');
        validatecount++;
        return validatecount;
    }
    return validatecount;
}


function saveSingleGroupSecond(obj){
    var activityKind = '${travelActivity.activityKind}';
    //团期tr
    $tr = $(obj).parents("tr:first");
    //备注span所在tr
    $next_tr = $tr.next();
    //备注input所在tr
    $next_next_tr = $tr.next().next();

    //团期的信息从input复制到span
    $tr.disableContainer({blankText : ""});
    //备注从input复制到span
    $next_tr.disableContainer({blankText : ""});
    $next_next_tr.disableContainer({blankText : ""});
    //价格方案的信息从input复制到span
    $next_next_tr.next().disableContainer({blankText : ""});
    //处理页面中的“+”号
    // TODO:
    $tr.find(".add-select").hide();
    $next_tr.find(".add-select").hide();
    $next_next_tr.find(".add-select").hide();
    $next_next_tr.next().find(".add-select").hide();
    //处理页面中的“x”号
    //TODO:
    $tr.find(".remove-selected").hide();
    $next_tr.find(".remove-selected").hide();
    $next_next_tr.find(".remove-selected").hide();
    $next_next_tr.next().find(".remove-selected").hide();
    //备注
    $tr.find("input[name='groupNotes']").parent("div:first").addClass("remarks-containers");
    $next_next_tr.css("display","none");


    //是否进行了保存的标志
    secondStepSaveFlag = false;

    $(obj).parent().children("span[name='hideBtn']").hide();
    $(obj).parent().children("span[name='deleteBtn']").show();
    $(obj).parent().children("span[name='expandPricing']").show();
    $(obj).parent().children("span").first().show();
    $(obj).parent().children("span.uploadGroupFileClass").show();

    //计算备注的长度
    var columnsNum = 0;
    $tr.find("th").each(function(){
        if($(this).text().trim() != '团号'){
            if($(this).attr('colspan')){
                columnsNum = columnsNum + parseInt($(this).attr('colspan'));
            }else{
                columnsNum = columnsNum + 1;
            }
        }else{
            columnsNum = columnsNum+1;
        }
    });
    $tr.find(".noteTr").find("td:eq(0)").attr("colspan",columnsNum);

}

/**
 * 保存新增团期
 * @returns {number}
 */
function saveNewGroup(obj){
    var validatecount = 0;
    //0071-进来先清空提示span-防止bug-s
    $("#contentTable").find("[name='span4visaCountry']").empty();
    //0071-进来先清空提示span-防止bug-e
    /*C463备注超长处理 */
    $("#contentTable").find("input[name='groupNotes']").each(function(index,obj){
        $(obj).parent("div:first").removeClass("remarks-containers");
    });
    $("#contentTable").find("input[name='groupRemark']").each(function(index,obj){
        $(obj).parent("div:first").removeClass("remarks-containers");
    });
    $("#contentTable").find("input[name='groupNotes']").each(function(index,obj){
        $(obj).parent("div:first").removeClass("remarks-containers");
    });

    var activityKind = $("#activityKind").val();
    add_groupsvalidator();
    //add_modgroupsvalidator();

    //**************0071-签证国家长度限定为50字-s**********
    //var arrCurrentVisaCountries=$('#secondStepMod [name="visaCountry"]');//现有签证国家
    var arrAddVisaCountries=$('#groupTable [name="visaCountry"]');//新增签证国家
    //var flag4VisaCountry=false;
    /*for(var i=0;i<arrCurrentVisaCountries.length;i++){
        if(arrCurrentVisaCountries[i].value.length>50){
            $("#secondStepMod #visaCountry"+(i+1)).after('<label name="span4visaCountry" style="display:block"  class="myerror">请输入一个长度最多是50的字符串</label>');
            flag4VisaCountry=true;
            validatecount++;
        }
    }*/
    for(var i=0;i<arrAddVisaCountries.length;i++){
        if(arrAddVisaCountries[i].value.length>50){
            $("#contentTable #visaCountry"+i).after('<label name="span4visaCountry" style="display:block"  class="myerror">请输入一个长度最多是50的字符串</label>');
            //flag4VisaCountry=true;
            //validatecount++;
            top.$.jBox.info("请先修改完错误再提交", "警告");
            top.$('.jbox-body .jbox-icon').css('top','55px');
        }
    }
    /*if(flag4VisaCountry==true){
        top.$.jBox.info("请先修改完错误再提交", "警告");
        top.$('.jbox-body .jbox-icon').css('top','55px');
        validatecount++;
        return validatecount;

        return
    }*/
    //**************0071-签证国家长度限定为50字-e**********

    //**********************特殊人群数不能大于预收人数的校验-s*******//
    var flag4MaxPeopleCountPlanPosition=false;
    var flag4MaxChildrenCountPlanPosition=false;
    var flag4MaxCountPlanPosition=false;

    
    // 微信分销系统 直客价必填权限验证 王洋 2017.1.6
	if (requiredStraightPrice == 'true') {
		var info = false;
		$("#contentTable").find("input[name='suggestAdultPrice']").each(function(index,obj){
			var suggestAdultPrice = $(obj).val();
			if (null == suggestAdultPrice || "" == suggestAdultPrice || "0" == suggestAdultPrice || "0.0" == suggestAdultPrice || "0.00" == suggestAdultPrice) {
				info = true;
			}
		});
		
		if (info) {
			top.$.jBox.info("请填写成人直客价，否则不能进行微信分销", "警告");
			return;
		}
	}
    //当前占位
    //var currentPositionNum = 0;
    //var currentPositionPeopleNum = 0;
    //var currentPositionChildrenFlag = false;
   // var currentPositionPeopleFlag = false;
    var currentPositionElts = $("input[name='nopayPeoplePosition']");
    //var currentChildrenPositionElts = $("input[name='nopayChildrenPosition']");
    var groupCodes = $("#contentTable").find("input[name='groupCode']");

    var maxPeopleCountElts=$("#contentTable").find("input[name='maxPeopleCount']");
    var maxChildrenCountElts=$("#contentTable").find("input[name='maxChildrenCount']");
    var planPositionElts=$("#contentTable").find("input[name='planPosition']");
    //t1t2-v4 518bug-15889出团日期不能早于截团日期
    var groupOpenDateElts = $("#contentTable").find("input[name='groupOpenDate']");
    var groupCloseDateElts = $("#contentTable").find("input[name='groupCloseDate']");
    var currentDateFlag = false;
    for(var i=0;i<maxChildrenCountElts.length;i++){
        if(groupOpenDateElts[i].value < groupCloseDateElts[i].value) {
            currentDateFlag = true;
            break;
        }
        if(maxChildrenCountElts[i].value != "" && parseInt(maxChildrenCountElts[i].value)>parseInt(planPositionElts[i].value)){ //特殊人群数大于预收人数,则校验标志置为真
            flag4MaxChildrenCountPlanPosition=true;
            break;//终止循环
        }
        /*if(currentChildrenPositionElts[i]&& currentChildrenPositionElts[i].value !="" && maxChildrenCountElts[i].value != "" && parseInt(maxChildrenCountElts[i].value)<parseInt(currentChildrenPositionElts[i].value)){ //特殊人群数大于预收人数,则校验标志置为真
            currentPositionChildrenFlag=true;
            currentPositionNum = currentChildrenPositionElts[i].value;
            break;//终止循环
        }*/
        if(maxChildrenCountElts[i].value != "" && maxPeopleCountElts[i].value != "" && (parseInt(maxChildrenCountElts[i].value)+parseInt(maxPeopleCountElts[i].value))>parseInt(planPositionElts[i].value)){ //特殊人群数大于预收人数,则校验标志置为真
            flag4MaxCountPlanPosition=true;
            break;//终止循环
        }
    }
    if(currentDateFlag){
        top.$.jBox.info("\"团号为"+groupCodes[i].value+"的出团日期不能早于截团日期\"", "警告");
        validatecount++;
        return validatecount;
    }
    if(flag4MaxChildrenCountPlanPosition){
        top.$.jBox.info("\"团号为"+groupCodes[i].value+"的儿童最高人数\"不能大于\"预收\"", "警告");
        validatecount++;
        return validatecount;
    }
    /*if(currentPositionChildrenFlag){
        validatecount++;
        top.$.jBox.info("\"团号为"+groupCodes[i].value+"的儿童最高人数\"不能小于\"已占位儿童人数"+currentPositionNum+"\"", "警告");
        return validatecount;
    }*/
    for(var i=0;i<maxPeopleCountElts.length;i++){
        if(maxPeopleCountElts[i].value != "" && parseInt(maxPeopleCountElts[i].value)>parseInt(planPositionElts[i].value)){ //特殊人群数大于预收人数,则校验标志置为真
            flag4MaxPeopleCountPlanPosition=true;
            break;//终止循环
        }
        if(maxPeopleCountElts[i].value != "" && currentPositionElts[i] && currentPositionElts[i].value != "" && currentPositionElts[i]&&parseInt(maxPeopleCountElts[i].value)<parseInt(currentPositionElts[i].value)){ //特殊人群数大于预收人数,则校验标志置为真
            currentPositionPeopleFlag=true;
            currentPositionPeopleNum = currentPositionElts[i].value;
            break;//终止循环
        }
        if(maxChildrenCountElts[i].value != "" && maxPeopleCountElts[i].value != ""  && (parseInt(maxChildrenCountElts[i].value)+parseInt(maxPeopleCountElts[i].value))>parseInt(planPositionElts[i].value)){ //特殊人群数大于预收人数,则校验标志置为真
            flag4MaxCountPlanPosition=true;
            break;//终止循环
        }
    }
    if(flag4MaxPeopleCountPlanPosition){
        validatecount++;
        top.$.jBox.info("\"团号为"+groupCodes[i].value+"的特殊人群最高人数\"不能大于\"预收\"", "警告");
        return validatecount;
    }
    /*if(currentPositionPeopleFlag){
        validatecount++;
        top.$.jBox.info("\"团号为"+groupCodes[i].value+"的特殊人群最高人数\"不能小于\"已占位特殊人群人数"+currentPositionPeopleNum+"\"", "警告");
        return validatecount;
    }*/

    if(flag4MaxCountPlanPosition){
        validatecount++;
        top.$.jBox.info("\"团号为"+groupCodes[i].value+"的儿童与特殊人群最高人数之和\"不能大于\"预收\"", "警告");
        return validatecount;
    }

    var keyVal= $("input[name^='groupCode']") ;

    var contentTable= $("#contentTable").validate({
        //var secondStepMod= $tr.parent().validate({
        rules:{
            activitySerNum:{
                remote: {
                    type: "POST",
                    url: "${ctx}/activity/manager/serNumRepeat?proId="+$("#proId").val()
                }
            }
        },
        messages:{
            activitySerNum:{remote:"编号已存在"}
        },
        errorPlacement: function(error, element) {
            if($(element).attr("id")=="introduction")
                error.appendTo (element.parent().parent());
            else if($(element).hasClass("spinner_day"))
                error.appendTo (element.parent().parent());
            else if ( element.is(":radio") )
                error.appendTo ( element.parent() );
            else if ( element.is(":checkbox") )
                error.appendTo ( element.parent() );
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
        ignore:":hidden[id!='fromArea'][id!='backArea'][id='secondStepEnd']"
    });
    var flag = contentTable.form();
    //var flag = validator1.form();
    if(flag) {
        $("#secondStepTitle").find("span:eq(0)").addClass("ydExpand");
        secondToggle();
        $("#secondStepEnd").disableContainer({blankText: ""});
//					$("#secondStepEnd").find("input[name=groupCode]").next().attr("style","word-break: break-all;display: block;word-wrap: break-word;");
//				$("#secondStepMod").disableContainer({blankText : ""});
        var serialId = $(obj).parent().parent().find("input[name='serial']").val();
        $(obj).parent().parent().parent().find("#tr_" + serialId).hide();

        //隐藏填写价格信息
        $("#secondStepAdd").hide();
        //显示添加团期按钮
        $("#secondModBtn").show();
        //隐藏保存按钮
        $("#secondSaveBtn").hide();
//				$("#contentTable thead span").hide();
       /* if(hasTuijian){
            $("#modTable tbody").children().find("input[name=settlementAdultPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
            $("#modTable tbody").children().find("input[name=settlementcChildPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
            $("#modTable tbody").children().find("input[name=settlementSpecialPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
        }else{
            $("#modTable tbody").children().find("input[name=settlementAdultPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
            $("#modTable tbody").children().find("input[name=settlementcChildPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
            $("#modTable tbody").children().find("input[name=settlementSpecialPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
        }*/
        /*if(activityKind == 2) {
            /!*if(hasTuijian){
                $("#modTable tbody").children().find("input[name=suggestAdultPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
                $("#modTable tbody").children().find("input[name=suggestChildPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
                $("#modTable tbody").children().find("input[name=suggestSpecialPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
                $("#modTable tbody").children().find("input[name=payDeposit]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
                $("#modTable tbody").children().find("input[name=singleDiff]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
            }else{
                $("#modTable tbody").children().find("input[name=suggestAdultPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
                $("#modTable tbody").children().find("input[name=suggestChildPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
                $("#modTable tbody").children().find("input[name=suggestSpecialPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
                $("#modTable tbody").children().find("input[name=payDeposit]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
                $("#modTable tbody").children().find("input[name=singleDiff]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
            }*!/
        }else{
            $("#modTable tbody").children().find("input[name=payDeposit]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
            $("#modTable tbody").children().find("input[name=singleDiff]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
        }*/
        $("#contentTable tbody").children().find("td:eq(0)").attr("class","tc");
        $("#contentTable tbody").children().find("td:eq(1)").attr("class","tc");
        $("#contentTable tbody").children().find("td:eq(4)").attr("class","tc");

        //$("#modTable tbody").children().find("p[name='targetHere']").children().remove();
        // 234需求，屏蔽上传出团通知
        //$("#modTable tbody").children().find("p[name='targetHere']").append('<a id="" name="uploadGroupFile" class="uploadGroupFileClass" onclick="uploadGroupFile(\'${ctx}\', this)" href="javascript:void(0)">上传出团通知</a>');
//	   		var groupPriceFlag = $("#groupPriceFlag").val();
//	    	if (groupPriceFlag  == "true") {
//	    		$("#modForm tbody [name=groupPriceTd]").each(function() {
//					if ($(this).is(":hidden")) {
//						$(this).prev().prev().prev().find("td[name='targetHere']").append('<a name=\"expandPricing\" show=\"true\">展开价格方案</a>');
//					} else {
//						$(this).prev().prev().prev().find("td[name='targetHere']").append('<a name=\"expandPricing\" show=\"true\">收起价格方案</a>');
//					}
//				});
//	    	}



        //C463现有団期列表备注
//	   		$("#modTable tbody tr[id^='groupRemark']").each(function(){
//	   			var $currNoteTr=$(this).find('input').val();
//	   			if($currNoteTr.trim()==""){
//	   				$(this).prev().prev().find("td[name='targetHere']").append('&nbsp;&nbsp;<a id="" name="uploadGroupFile" class="uploadGroupFileClass" onclick="expandRemark(this)" href="javascript:void(0)">备注</a>');
//	   			}else{
//	   				$(this).prev().prev().find("td[name='targetHere']").append('&nbsp;&nbsp;<a id="" name="uploadGroupFile" class="uploadGroupFileClass" onclick="expandRemark(this)" href="javascript:void(0)">备注<em class="groupNoteTipImg"></em></a>');
//	   			}
//	   			$(this).prev().hide();
//	   		})
        /*收起已有团期备注*/
        $("#contentTable").find(".noteTr").hide();

        //C463新增団期列表备注
        if('f5c8969ee6b845bcbeb5c2b40bac3a23'=='${fns:getUser().company.uuid}'){//懿洋假期
            var nameStr = 'invoiceTax';
        }else{
            var nameStr = 'freePosition';
        }
        $("#contentTable tbody").children().find("[name="+nameStr+"]").each(function(index, obj){
            //修复：新增的団期保存后，再次点击修改，操作项消失的问题
            //$(obj).parent().next().empty();
            $(obj).parent().next().children().hide();
            // 234需求，屏蔽上传出团通知
            //$(obj).parent().next().append('<p><a id="" name="uploadGroupFile" onclick="uploadGroupFile(\'${ctx}\', this)" href="javascript:void(0)">上传出团通知</a></p>');
            var groupPriceFlag = $("#groupPriceFlag").val();
            if (groupPriceFlag  == "true") {
                $(this)
                if ($(this).is(":hidden")) {
                    $(obj).parent().next().append('<p style=\"margin-bottom:0\"><a id=\"expandPricing\" name=\"expandPricing\" show=\"true\">展开价格方案</a></p>');
                }
                else {
                    $(obj).parent().next().append('<p style=\"margin-bottom:0\"><a id=\"expandPricing\" name=\"expandPricing\" show=\"true\">收起价格方案</a></p>');
                }
            }
            if($(obj).parent().parent().next().find("input").val().trim() != ""){
                $(obj).parent().next().append('<p style=\"margin-bottom:0\"><a class="uploadGroupFileClass" name="uploadGroupFile" onclick="expandRemark4Add(this)">备注<em class="groupNoteTipImg"></em></a></p>');
            }else{
                $(obj).parent().next().append('<p style=\"margin-bottom:0\"><a class="uploadGroupFileClass" name="uploadGroupFile" onclick="expandRemark4Add(this)">备注</a></p>');
            }

            //散拼前三个都要合并
            /*if(2==activityKind){*/
                $(obj).parent().parent().children().eq(0).attr("rowspan","1");
                $(obj).parent().parent().children().eq(1).attr("rowspan","1");
                if("${fns:getUser().company.uuid}"!='7a81a03577a811e5bc1e000c29cf2586'
                    && "${fns:getUser().company.uuid}"!='7a8175bc77a811e5bc1e000c29cf2586'
                    && "${fns:getUser().company.uuid}"!='7a8177e377a811e5bc1e000c29cf2586'
                    && "${fns:getUser().company.uuid}"!='7a81a26b77a811e5bc1e000c29cf2586'
                    && "${fns:getUser().company.uuid}"!='58a27feeab3944378b266aff05b627d2'
                    && "${fns:getUser().company.uuid}"!='5c05dfc65cd24c239cd1528e03965021'
                    && "${fns:getUser().company.uuid}"!='980e4c74b7684136afd89df7f89b2bee'
                ){//大洋、新行者、青岛凯撒、拉美途、日信观光、起航假期、骡子假期    2个
                    $(obj).parent().parent().children().eq(2).attr("rowspan","1");
                    if("${fns:getUser().company.uuid}"=='7a81c5d777a811e5bc1e000c29cf2586'){//优加国际 4个
                        $(obj).parent().parent().children().eq(3).attr("rowspan","1");
                    }
                }

            /*}else{
                $(obj).parent().parent().children().eq(0).attr("rowspan","1");
                $(obj).parent().parent().children().eq(1).attr("rowspan","1");
            }*/

            $(obj).parent().parent().next().hide();
        });


        // 对应需求号  223  点修改保存后  显示查看
        /*if(${travelActivity.activityKind} == 10) {
            //alert(isneedCruiseGroupControl)
            if(isneedCruiseGroupControl == '1'){
                $("#modTable tbody").children().find("p[name='targetHere']").append('' + '<a onClick="jbox_view_group_control_pop(this)" href="javascript:void(0)">查看关联团控表</a>' + '');
            }
        }*/


        /*$("#modTable tbody").children().find("[name='uploadGroupFile']").removeClass("displayClick");
         $("#modTable tbody").children().find("[name='uploadGroupFile']").show();*/

// 	   		for(var i = 5 ;i <= 12 ;i ++) {
// 	   			$("#contentTable tbody").children(":visible").find("td:eq("+i+")").attr("class","tr");
// 	   		}
        remove_groupsvalidator();
        //remove_modgroupsvalidator();
        //subCode(false,true);
        subCode(false,false);

        //生成币种Id串，并记录在每个团期后面
        var len = $("#contentTable tbody").find("input").length;
        if(len!=0){
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
        }

        //隐藏操作按钮
       /* $("#modTable .addAndRemove").hide();
        $("#modTable .add-select").hide();
        $("#modTable .remove-selected").hide();*/
        /*$("#modForm .addAndRemove").hide();
        $("#modForm .add-select").hide();
        $("#modForm .remove-selected").hide();*/
        $("#contentTable .addAndRemove").hide();
        $("#contentTable .add-select").hide();
        $("#contentTable .remove-selected").hide();
        $("#contentTable thead span").hide();
        secondStepSaveFlag = true;
    }else{
        //c463
        $("#contentTable tbody").children().find("[name='freePosition']").each(function(index, obj){
            //散拼前三个都要合并
            /*if(2==orderType){*/
                $(obj).parent().parent().children().eq(0).attr("rowspan","1");
                $(obj).parent().parent().children().eq(1).attr("rowspan","1");
                if("${fns:getUser().company.uuid}"!='7a81a03577a811e5bc1e000c29cf2586'
                    && "${fns:getUser().company.uuid}"!='7a8175bc77a811e5bc1e000c29cf2586'
                    && "${fns:getUser().company.uuid}"!='7a8177e377a811e5bc1e000c29cf2586'
                    && "${fns:getUser().company.uuid}"!='7a81a26b77a811e5bc1e000c29cf2586'
                    && "${fns:getUser().company.uuid}"!='58a27feeab3944378b266aff05b627d2'
                    && "${fns:getUser().company.uuid}"!='5c05dfc65cd24c239cd1528e03965021'
                    && "${fns:getUser().company.uuid}"!='980e4c74b7684136afd89df7f89b2bee'
                ){//大洋、新行者、青岛凯撒、拉美途、日信观光、起航假期、骡子假期     2个
                    $(obj).parent().parent().children().eq(2).attr("rowspan","1");
                    if("${fns:getUser().company.uuid}"=='7a81c5d777a811e5bc1e000c29cf2586'){//优加国际 4个
                        $(obj).parent().parent().children().eq(3).attr("rowspan","1");
                    }
                }

            /*}else{
                $(obj).parent().parent().children().eq(0).attr("rowspan","1");
                $(obj).parent().parent().children().eq(1).attr("rowspan","1");
            }*/

            $(obj).parent().parent().next().hide();
            if($(obj).parent().parent().next().find("input").val().trim() != ""){
                $(obj).parent().next().find(".groupNote").children().remove();
                $(obj).parent().next().find(".groupNote").append('<em class="groupNoteTipImg"></em>');
            }
        });
        top.$.jBox.info("请先修改完错误再提交", "警告");
        top.$('.jbox-body .jbox-icon').css('top','55px');
        //validatecount++;
        return validatecount;
    }
    saveNewGroupflag = true;
    return validatecount;
}

/*    var flag = validator1.form();*/
