$(function(){

    inputTips();
 //全选
    $("#table-checkAll").click(function(){
        if($("#table-checkAll").is(":checked")){
            $('[name=num]:checkbox').prop('checked', true);
        }else{
            $('[name=num]:checkbox').removeAttr("checked");
        }
    });  
    //反选
    $("#table-checkReverse").click(function(){
        $('[name=num]:checkbox').each(function(){
            this.checked=!this.checked;
        });
    });
    //各种全选、全不选判定
    $(".table_checkbox,#table-checkReverse").click(function(){
        var n=$("input[name='num']:checked").length;
        if(n==$(".table_checkbox").length){
            $("#table-checkAll").prop('checked', true);
        }else{
            $("#table-checkAll").removeAttr("checked");
        }
    });

});

//点击设置默认费率
function jbox_set_rate_pop(ctx){

    var $check = $("#contentTable input[type='checkbox']");
    //已选择供应商
    if($check.is(':checked')){ 
        $pop = $.jBox($("#rate_setting_pop").html(), {
            title : "客户类型", buttons: {'提交':1,'取消':0},submit: function (v, h, f) {
                if (v == "1") {
                    //检验必填项
                    if($pop.find(".quauq-pr").val()==""){
                        $pop.find(".quauq-pr").addClass("inputError").focus().next().children("input").addClass("inputError-right");
                        $.jBox.tip('请填写Quauq产品费率','warnning');
                        return false;
                    }

                    //保存填写的信息，每一个input对应一个item。
                    var itemArr = [];
                    var $input = $pop.find(".rate-setting span").children("input");
                    $input.each(function(){
                        var item = {};
                        item.agentName = $(this).parent().parent().attr("id"); //门店，总社，集团
                        item.rateName = $(this).attr("class"); // quauq其他。渠道产品,其他。
                        var rateType = $(this).next().find("input").val(); // 费率的类型，%,￥
                        if(rateType == "百分比"){ // js进行转换一下
                            item.rateType = 0;
                        }else if(rateType == "金额￥"){
                            item.rateType = 1;
                        }
                        item.rate = $(this).val();
                        itemArr.push(item);
                    });

                    //每一个渠道类型都添加上quauq产品费率，后台处理方便。
                    var quauqRate = $pop.find(".quauq-pr").val();
                    var quauqRateType = $pop.find(".quauq-pr").next().children("input").val();;
                    if(quauqRateType == "百分比"){
                        quauqRateType = 0;
                    }else if(quauqRateType == "金额￥"){
                        quauqRateType = 1;
                    }
                    var agentTypeArr = ["sales","moffice","group"];
                    for (var i = 0; i < agentTypeArr.length; i++){
                        var item = {};
                        item.agentName = agentTypeArr[i];
                        item.rateName = "quauq-pr";
                        item.rate = quauqRate;
                        item.rateType = quauqRateType;
                        itemArr.push(item);
                    }
                    //560需求 抽成费率 add by chao.zhang
                    var chouchengRate = $pop.find(".choucheng-pr").val();
                    var chouchengRateType = $pop.find(".choucheng-pr").next().children("input").val();
                    if(chouchengRateType == "百分比"){
                    	chouchengRateType = 0;
                    }else if(chouchengRateType == "金额￥"){
                    	chouchengRateType = 1;
                    }
                    for (var i = 0; i < agentTypeArr.length; i++){
                        var item = {};
                        item.agentName = agentTypeArr[i];
                        item.rateName = "choucheng-pr";
                        item.rate = chouchengRate;
                        item.rateType = chouchengRateType;
                        itemArr.push(item);
                    }
                    //获取选中公司的uuid
                    var uuidArr = [];
                    var $checked = $("#contentTable").find("input[type=checkbox]:checked");
                    $checked.each(function(){
                        uuidArr.push($(this).attr("id"));
                    });

                    //向后台发送数据
                    $.ajax({
                        url : ctx +"/quauqAgent/manage/saveDefaultRate",
                        type : 'post',
                        data : {items:JSON.stringify(itemArr),uuids:JSON.stringify(uuidArr)},
                        async : true,
                        success : function(data){
                            if (data == "1"){
                            	$("#searchForm").attr("action",ctx+"/quauqAgent/manage/ratelist");
                                $("#searchForm").submit();
                            }else {
                                $.jBox.tip('费率设置失败,数据解析错误，请检查输入项。','warnning');
                            }
                        }
                    });
                }
            },  width: 550,height:450,
                persistent: true
        });

        //获取原有数据 S

        var $checked=$("#contentTable input[type='checkbox']:checked");
        var rate = "";
        var unit = "";

        if($checked.length==1){
            var qpr=$checked.parents("tr").find(".quauq-pr").text().trim();
            if(qpr!=="-"){
                var result=isPercent(qpr);
                rate=result[0];
                unit=result[1];
                $pop.find(".quauq-pr").val(rate);
                $pop.find(".quauq-pr").next().find("input").val(unit);
            }
            var ccr=$checked.parents("tr").find(".choucheng-pr").text().trim();  
            if(ccr!=="-"){
                var result=isPercent(ccr);
                rate=result[0];
                unit=result[1];
                $pop.find(".choucheng-pr").val(rate);
                $pop.find(".choucheng-pr").next().find("input").val(unit);
            }
            $checked.parents("tr").find("td span").each(function(){
                var rateClass = $(this).attr("class");
                var rateId = $(this).parent().attr("class");
                var rateText = $(this).text().trim();
                if(rateText!=="-"){
                    var result=isPercent(rateText);
                    rate=result[0];
                    unit=result[1];
                    $pop.find("#"+rateId+" ."+rateClass).val(rate);
                    $pop.find("#"+rateId+" ."+rateClass).next().find("input").val(unit);
                }
            });
        }
        function isPercent(str){
            var index = str.indexOf("%");
            var rate ="";
            var unit ="";
            if(index!==-1){
                rate = str.substring(0,index);
                unit = "百分比";
            }else{
                rate = str.substring(4);
                unit = "金额￥";
            }
            var result = [rate,unit];
            return result;
        }
        //获取原有数据 E

        $pop.find(".dl-select input").click(function(event){
            var $lastSelect=$pop.find(".rate_setting_pop .rate-setting:last-child");
            $lastSelect.find(".select-option").addClass("lastSelect");
            var event = event || window.enent;
            event.stopPropagation();
            var flag = $(this).parent().children("ul").is(":hidden");
            $pop.find(".rate_setting_pop").find(".dl-select ul").hide();
            if(flag){
                $(this).parent().children("ul").show();
            }else{
                $(this).parent().children("ul").hide();
            }
        });
        $pop.find(".dl-select ul li").each(function(){
            $(this).click(function(){
                var value = $(this).text();
                $(this).parent().hide();
                $(this).parent().parent().children("input").val(value);
                if (value == "百分比"){
                    var $rate = $(this).parent().parent().prev("input");
                    var val = $rate.val();
                    if(val <= 100){
                        return true;
                    }
                    if (val != ""){
                        $.jBox.tip('百分比类型，数值不可大于100!','warnning');
                        $rate.val("");
                    }
                }
            });
        });
        //160812-点击别处下拉框消失S
        $(document).click(function(){
            $pop.find(".dl-select ul").hide();
        });
        //160812-点击别处下拉框消失E

        $pop.find(".rate-setting span").children("input").keyup(function(){
            if($(this).val()!==""){
                $(this).removeClass("inputError").next().children("input").removeClass("inputError-right");
            }
        });
    }else{
        //未选择批发商
        $.jBox.tip('请选择批发商','warnning');
        return false;
    }
}

//导出excel
function exportExcel(ctx){
	$("#searchForm").attr("action",ctx+"/quauqAgent/manage/exportExcel");
	$("#searchForm").submit();
}
