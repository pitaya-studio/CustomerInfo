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
                searchWholeProduct();
            };
    });

    searchWholeProduct();
});

// $(document).keydown(function(event){
//     if(event.keyCode == 13){ //绑定回车
//         $('#login-submit').click(); /自动/触发登录按钮
//     }
// });


//全部团控操作记录的ajax查询
function searchWholeProduct(content,pageno,pagesize){
    var selectDatetype,nameOrcode,selectdate,datefrom,dateto,pageno,pagesize;
    var sysCtx = $("#sysCtx").val();
    pageno = pageno||1;
    pagesize = pagesize||10;

    //判定是第几种查询方式，如果没传任何值则搜索全部，传递第二个或者第三个
    if(!content){
        $(".title_bottom").find(".sec_select input,.third_select input").attr("value","");
        selectDatetype = "1";
    } else if(content.attr("id") == "selectTime"){
        selectDatetype = "2";
    } else if(content.attr("id") == "wholeTimeEnd"){
        selectDatetype = 3;
    }
    nameOrcode = $("#searchPanelInput").val();
    selectdate =  $("#selectTime").val();
    datefrom = $("#wholeTimeBegin").val();
    dateto = $("#wholeTimeEnd").val();

    // window.location.href=sysCtx + "/activity/controlBoard/getWholeOpeRecord?selectDateType=" +selectDatetype
    //     +"&nameOrCode="+nameOrcode+"&selectDate="+selectdate+"&dateFrom="+datefrom+"&dateTo="+dateto;

    $.ajax({
        type: "POST",
        url: sysCtx + "/activity/controlBoard/getWholeOpeRecord",
        cache: false,
        async: false,
        dataType: "json",//返回的数据类型
        data: {selectDateType:selectDatetype,nameOrCode:nameOrcode,selectDate:selectdate,
            dateFrom:datefrom,dateTo:dateto,pageNo:pageno,pageSize:pagesize},
        success: function (data){
            var count = data.pageInfo.count;

            var html = "";
            if(data.wholeList.length==0){
                html = "<tr>" +
                    "<td colspan='7'>没有查找到记录</td>" +
                    "</tr>";
            }else {
                for (var i=0; i<data.wholeList.length;i++){
                    var date = data.wholeList[i].opetype;
                    var opeType=(date==1&&'收客')||(date==2&&'报名')||(date==3&&'余位调整')||
                        (date==4&&'订单修改')||(date==5&&'退团')||(date==6&&'转团')||(date==7&&'转团')
                        ||(date==8&&'订单取消')||(date==9&&'订单删除')||(date==10&&'财务驳回取消占位');
                    html += "<tr>" +
                        "<td>"+data.wholeList[i].opeTime+"</td>" +
                        "<td>"+data.wholeList[i].opeName+"</td>" +
                        "<td>"+data.wholeList[i].tralActivityName+"</td>" +
                        "<td>"+data.wholeList[i].groupCode+"</td>" +
                        "<td>"+opeType+"</td>" +
                        "<td>"+data.wholeList[i].amount+"</td>" +
                        "<td>"+data.wholeList[i].remarks+"</td></tr>";
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

function clearAllInput(){
     $(".third_select input").attr("value","");
     $(".sec_select").attr("value","");
     $("#searchPanelInput").attr("value","");
     $("#dateInput").attr("value","全部");
     $(".third_select,.sec_select").hide();

     searchWholeProduct();
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
    var dateInput = $("#dateInput").val();
    if( dateInput=="全部"){
    	searchWholeProduct("",page,pageSize);
    } 
    else if(dateInput=="按单日"){
        searchWholeProduct($("#selectTime"),page,pageSize);
    }else if(dateInput=="自定义"){
        searchWholeProduct($("#wholeTimeEnd"),page,pageSize);
    }
    
}