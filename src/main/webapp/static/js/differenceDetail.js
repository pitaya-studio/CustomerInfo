/**
 * Created by wanglijun on 2016/10/18.
 * 538需求 财务差额返回明细页面相关js
 */
//如果展开部分有查询条件的话，默认展开，否则收起
var ctx="";
function closeOrExpand(){
    var searchFormselect = $("#searchForm").find("select");
    var inputRequest = false;
    var selectRequest = false;
     for(var i=0;i<searchFormselect.length;i++) {
     var selectValue = $(searchFormselect[i]).children("option:selected").val();
     if(selectValue != "" && selectValue != null && selectValue != 0) {
     selectRequest = true;
     break;
     }
     }
     if(inputRequest||selectRequest) {
     $('.zksx').click();
     }else{
     $('.zksx').text('展开筛选');
     }
};

$(document).ready(function(){
    ctx=$("#ctx").val();
    getAgentinfos();
    getAgentContacts();
    HtmlForReturnDetail();
    //展开、收起筛选
    launch();
    //如果展开部分有查询条件的话，默认展开，否则收起
    // closeOrExpand();
    //反选
    $("#reverseCheck").click(function(event){
        if($("#checkAll").is(':checked')){
            $("#checkAll").removeAttr("checked")
        }
        $("input[name='checkBox']").each(function(){
            if($(this).is(':checked')){
                $(this).removeAttr('checked');
            }else{
                $(this).prop('checked',true);
            }
        });
    });
    //各种全选、全不选判定
    $(".table_checkbox,#reverseCheck").click(function(){
        var n=$("input[name='checkBox']:checked").length;
        if(n==($(".table_checkbox").length-1)){
            $("#checkAll").prop('checked', true);
        }else{
            $("#checkAll").removeAttr("checked");
        }
    });

    $("#checkAll").click(function(event){
        if($("#reverseCheck").is(':checked')){
            $("#reverseCheck").removeAttr("checked")
        }
        var flag = $(this).is(':checked');
        if(flag){
            $("input[name='checkBox']").each(function(){
                $(this).prop('checked',flag);
            });
        }else{
            $("input[name='checkBox']").each(function(){
                $(this).removeAttr('checked');
            });
        }
    });

});

/**
 * 批量确认付款状态与否
 */
function paymentConfirm(flag){
    var checkedInput = $('input:checkbox[name=checkBox]:checked');
    var len = checkedInput.length;
    if (len < 1) {
        $.jBox.tip('请先选择需要设置的订单', 'warnning');
        return false;
    }
    var orderId="";
    var payType=flag;
    for(var i=0;i<len;i++){
        orderId+=$(checkedInput[i]).val()+",";
    }
    orderId=orderId.substring(0,orderId.length-1);
    $.ajax({
        type: "POST",
        url: ctx + "/return/price/updateDifferencePayStatus",
        cache: false,
        dataType: "json",
        async: false,
        data: {"orderId":orderId,"payType":payType},
        success:function(data){
            if(data.result=="success"){
                $.jBox.tip('状态更新成功', 'success');
                //此时应该将 全选，或者反选  按钮的选中状态去掉
                $("#checkAll").removeAttr("check");
                $("#reverseCheck").removeAttr("check");
                HtmlForReturnDetail();
            }else{
                $.jBox.tip('状态更新失败', 'error');
            }
        },
        error:function(e){

      }
    })

}
/**
 * 获取渠道列表数据
 */
function getAgentinfos() {
    $.ajax({
        type: "POST",
        url: ctx + "/return/price/getT1Agentinfos",
        cache: false,
        dataType: "json",
        async: true,
        data: {},

        success: function (data) {
            var _agentifos = data.agentinfos;
            var htmlstr = $("select[name=agentifo]");
            htmlstr.html('');
            var _insertHtml = "";
            _insertHtml += "<option value='' >不限</option>";
            var num = _agentifos.length;
            if (num > 0) {
                for (var i = 0; i < num; i++) {
                    var _agentifo = _agentifos[i];
                    _insertHtml += ' <option value="' + _agentifo.id + '" >' + _agentifo.agentName + '</option>';
                }
                htmlstr.append(_insertHtml);
            }
            $("#agentifo").comboboxInquiry();
        },
        error: function (e) {

        }
    })
};
/**
 * 获取渠道联系人
 */
function getAgentContacts() {
    $.ajax({
        type: "POST",
        url: ctx + "/return/price/getT1Users",
        cache: false,
        dataType: "json",
        async: true,
        data: inputDetail,

        success: function (data) {
            var agentContacts = data.agentContacts;
            var htmlstr = $("select[name=agentUser]");
            htmlstr.html('');
            var _insertHtml = "";
            _insertHtml += "<option value='' >不限</option>";
            var num = agentContacts.length;
            if (num > 0) {
                for (var i = 0; i < num; i++) {
                    var agentContact = agentContacts[i];
                    _insertHtml += ' <option value="' + agentContact.contactName + '" >' + agentContact.contactName + '</option>';
                }
                htmlstr.append(_insertHtml);
            }
            $("#agentUser").comboboxInquiry();
        },
        error: function (e) {
        }
    })
};
var inputDetail=
    {
    "input" : "", //搜索框的值
    "agentId":"",//渠道id
    "agentContactId" :"",//渠道联系人id
    "payType":"",//付款状态：0：未付款，1：已付款
    "pageNo":"",
    "pageSize":""
}
/**
 *  财务差额返还明细
 * @constructor
 */
function HtmlForReturnDetail(){
    $.ajax({
        type: "POST",
        url: ctx + "/return/price/getReturnPriceDetail",
        cache: false,
        dataType: "json",
        async: true,
        data: inputDetail,
        beforeSend: function () {
            $("#loading").show();
        },
        success: function (data) {
            var _listResult=data.page;
            var _htmlstr = $("#noResult");
            _htmlstr.nextAll().remove();
            if(_listResult.length>0){
                var _insertHtml = "";
                var _count=data.count;
                var _pageSize=data.pageSize;
                var _pageNo=data.pageNo;
                var agentContacts = data.agentContacts;
                for(var i=0,j=_listResult.length;i<j;i++){
                    var _orderDetail=_listResult[i];
                    _insertHtml += "<tr>";
                    _insertHtml += '<td style="text-align: center"><input type="checkbox" ckass="table_checkbox" name="checkBox" value="'+_orderDetail.orderId+'" /> </td>';
                    _insertHtml += '<td class="tc">'+_orderDetail.acitivityName+'</td>';
                    _insertHtml += '<td class="tc">'+_orderDetail.groupNum+'</td>';
                    _insertHtml += '<td class="tc">'+_orderDetail.orderNum+'</td>';
                    _insertHtml += '<td class="tc">'+_orderDetail.personNum+'</td>';
                    _insertHtml += '<td class="tc">'+_orderDetail.saler+'</td>';
                    _insertHtml += '<td class="tc">'+_orderDetail.agentName+'</td>';
                    _insertHtml += '<td class="tc">'+_orderDetail.agentContactName+'</td>';
                    _insertHtml += '<td class="tc">'+_orderDetail.forcastReturnPriceMark+_orderDetail.forcastReturnPrice+'</td>';
                    _insertHtml += '<td class="tc">'+_orderDetail.returnPriceMark+_orderDetail.returnPrice+'</td>';
                    _insertHtml += '<td class="tc">'+(_orderDetail.payType==0?"未付款":"已付款")+'</td></tr>';
                }
                $("#noResult").hide();
                $("#contentTable_foot").show();
                _htmlstr.after(_insertHtml);
                var _webVersion="t2";
                var _pageTest={  count:_count,
                    pageSize:_pageSize,
                    pageNo:_pageNo
                }
                var _pageHtml= doPage(_pageTest,_webVersion);
                $(".pagination").empty().append(_pageHtml);
            }else{
                $("#noResult").show();
                $("#contentTable_foot").hide();
            }
        },
        error: function (e) {
            $("#noResult").show();
        },
        complete: function () {
            $("#loading").hide();
        }
    })
}
/**
 * 查询条件重置
 *
 */

function resetForm(){
    var inputArray = $('#searchForm').find("input[type!='hidden'][type!='submit'][type!='button']");
    var selectArray = $('#searchForm').find("select");
    for(var i=0;i<inputArray.length;i++){
        if($(inputArray[i]).val()){
            $(inputArray[i]).val('');
        }
    }
    for(var i=0;i<selectArray.length;i++){
        var selectOption = $(selectArray[i]).children("option");
        $(selectOption[0]).attr("selected","selected");
        $(selectOption[0]).nextAll().removeAttr("selected")
    }
}

function queryMarginInCondition(){
    inputDetail.input=$("#groupCode").val();
    inputDetail.agentId=$("#agentifo").val();
    inputDetail.agentContactId=$("#agentUser").val();
    inputDetail.payType=$("#payType").val();
    HtmlForReturnDetail();
}
/**
 * 分页的函数
 * @param page
 * @param pageSize
 */
function goPage(page,pageSize){
    inputDetail.pageNo=page;
    inputDetail.pageSize=pageSize;
    HtmlForReturnDetail();
}

