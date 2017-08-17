/**
 * Created by zong on 2016/7/6.
 */
//66 提交时验证押金，押金+担保金额是否填写 START
function submitCheck(ctx){
    var manyValue=[];
    var flag = true;
    $("#tableId").find("tr").each(function(){
        var _jQueryObj_01=$(this).find("td").first().children().first();
        if(_jQueryObj_01.attr("checked")=="checked"){

            var aloneValue={};
            aloneValue["id"]=_jQueryObj_01.attr("id");
            var jQueryObj=$(this).find("td.useLess").each(function(){

                if($(this).children().first().next().length>0){
                    aloneValue["currencyId"]=$(this).children().first().val();
                    aloneValue["amount"]=$(this).children().first().next().val();
                    //aloneValue.push($(this).children().first().next().val());
                    if((aloneValue["guaranteeType"] == 2 || aloneValue["guaranteeType"] == 3) &&　(aloneValue["amount"]==undefined || aloneValue["amount"] == "")){
                        top.$.jBox.tip("游客未填写押金金额");
                        flag = false;
                        return;
                    }
                }else{
                    //if($(this).children().first().attr("tagName")=="select"){
                    var _xxx=$(this).children().first();
                    if(_xxx[0].tagName=="SELECT"){
                        //var guaranteeType = $(this).children().first().val();
                        aloneValue["guaranteeType"]=$(this).children().first().val();

                    }else{
                        aloneValue["remark"]=$(this).children().first().val();
                    }
                    //aloneValue.push($(this).children().first().val());
                }
            });
            manyValue.push(aloneValue);
        }
    });
    if(manyValue.length == 0) {
        top.$.jBox.tip("请选择游客！", "warning");
        return false;
    }
    if(!flag) {
        return false;
    }

    var deptId = $("#deptId").val();
    var orderId = $("#orderId").val();
    $.ajax({
        url : ctx+'/guaranteeMod/apply',
        type : 'post',
        data : {objs:JSON.stringify(manyValue),deptId:deptId,orderId:orderId},
        async : true,
        success : function(data) {
            if (data.result == "success") {
                top.$.jBox.tip("担保变更申请成功", "success");
                window.location.href = ctx + '/guaranteeMod/list/' + orderId;
            } else {
                tokenTimes = 0;
                top.$.jBox.tip(data.msg, "warning");
            }
        }
    });


    /*$(".selectGuarantee").each(function(){
     if($(this).val()=="2" || $(this).val()=="3"){
     if($(this).parent().next().find("input").val()==""){
     var name =$(this).parent().prev().prev().prev().text();
     top.$.jBox.tip("游客未填写押金金额");
     return false;
     }
     }
     });*/
    console.log(manyValue);
}
//66 提交时验证押金，押金+担保金额是否填写 END


