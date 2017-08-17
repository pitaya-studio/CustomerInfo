/**
 * Created by ymx on 2016/10/25.
 */



/*调整数量点击下拉*/
$(function () {

    $(".select_date input").click(function(){
        $(this).parent().children("ul").toggle();
    });

    $("body").click(function(){
        var e = e || window.event;
        var target = e.target || e.srcElement;
        if( target.id != "dateInput" && target.parentNode.id != "dateOption"){
            $(".select_date").children("ul").hide();
        }
    });

    $(".date_option li").click(function(){
            var value = $(this).text();
            $(this).parent().hide();
            var $div = $(this).parent().parent();
            $div.children("input").val(value);

            /*选择不同的查询方式，显示不同的input;
            * 0是全部、1是按单日、2为自定义
            * 当切换查询方式时，将之前输入的日期清空，防止取值冲突*/
            if($(this).index()==1){
                $div.parent().find(".third_select").hide();
                $div.parent().find(".third_select input").attr("value","");
                $div.parent().find(".sec_select").show();
            }else if($(this).index()==2){
                $div.parent().find(".sec_select").hide();
                $div.parent().find(".sec_select input").attr("value","");
                $div.parent().find(".third_select").show();
            }else{
                $div.parent().find(".sec_select input,.third_select input").attr("value","");
                $div.parent().find(".third_select,.sec_select").hide();
                searchSingleProduct();
            };
    });

    searchSingleProduct();
});

function searchSingleProduct(pageno,pagesize){
    var selectDatetype,selectdate,datefrom,dateto,groupid;
    var sysCtx = $("#sysCtx").val();
    var groupid = $("#groupId").val();
    pageno = pageno||1;
    pagesize = pagesize||10;

    if($("#dateInput").val() == "全部"){
        $(".title_bottom").find(".sec_select input,.third_select input").attr("value","");
        selectDatetype = 1;
    } else if($("#dateInput").val() == "按单日"){
        selectDatetype = 2;
    } else if($("#dateInput").val() == "自定义"){
        selectDatetype = 3;
    }

    selectdate =  $("#selectTime").val();
    datefrom = $("#singleTimeBegin").val();
    dateto = $("#singleTimeEnd").val();

    $.ajax({
        type: "POST",
        url: sysCtx + "/activity/controlBoard/getSingleOpeRecord",
        cache: false,
        async: false,
        dataType: "json",//返回的数据类型
        data: {selectDateType:selectDatetype,selectDate:selectdate,dateFrom:datefrom,
            dateTo:dateto,groupId:groupid,pageNo:pageno,pageSize:pagesize},
        success: function (data){

            $(".title_bold").html("产品名称："+data.groupMessage.acitivityName);
            $(".groupNum").html(data.groupMessage.groupCode);
            $(".group_date").html(data.groupMessage.groupOpenDate);
            var count = data.pageInfo.count;
            // pageno = data.pageInfo.pageno||"1";
            // pagesize = data.pageInfo.pagesize||"10";

            var html = "";
            if(data.singleList.length==0){
                html = "<tr> " +
                    "<td colspan='5'>没有查找到记录</td>" +
                    "</tr>";
            }else {
                for (var i=0; i<data.singleList.length;i++){
                    var date = data.singleList[i].opetype;
                    var opeType=(date==1&&'收客')||(date==2&&'报名')||(date==3&&'余位调整')||
                        (date==4&&'订单修改')||(date==5&&'退团')||(date==6&&'转团')||(date==7&&'转团')
                        ||(date==8&&'订单取消')||(date==9&&'订单删除')||(date==10&&'财务驳回取消占位');
                    html += "<tr>" +
                        "<td>"+data.singleList[i].opeTime+"</td>" +
                        "<td>"+data.singleList[i].opeName+"</td>" +
                        "<td>"+opeType+"</td>" +
                        "<td>"+data.singleList[i].amount+"</td>" +
                        "<td>"+data.singleList[i].remarks+"</td></tr>";
                }
            }

            $("#contentTable tbody").html(html);

            var _webVersion="t2";
            var _pageTest={
                count:count,
                pageSize:pagesize,
                pageNo:pageno
            }
            var _pageHtml= doPage(_pageTest,_webVersion,goPage);
            $(".pagination").empty().append(_pageHtml);
        },
        error: function (){
            alert("查找失败！");
        }
    });

}
var   inputDetail={
    "pageNo":"",
    "pageSize":""
}

/**
 * 分页的函数
 * @param page
 * @param pageSize
 */
function goPage(page,pageSize){
    inputDetail.pageNo=page;
    inputDetail.pageSize=pageSize;
    searchSingleProduct(page,pageSize);
}