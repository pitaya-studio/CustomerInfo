/**
 * Created by wanglijun on 2016/11/24.
 * 为辉腾定制的相关功能的js
 * //本页面带下划线的id  下划线后面的内容为页面名字
 */
$(document).ready(function(){
    var domain=window.location.host;
    if(domain.indexOf("huitengguoji.com")!=-1){//huiteng
    // if(domain.indexOf("quauqsystem.com")==-1){//test
        $("#gray_t1Head").length>0? $(".gray").text("客服电话：010-87887758"):"";
        $("#company_logo").length>0? $("#company_logo").removeClass("hedear-logo").addClass("ht_hedear-logo"):"";
        $("#logo_t1Head").length>0? $("#logo_t1Head").addClass(""):"";
        $("#selfFooter").length>0?$("#selfFooter").show():"";
        // $("#logo_t1Head").length>0? $("#logo_t1Head").addClass("ht_hedear-logo"):"";
        $("#keyword").attr("data_from")=="jumpParam"? $("#keyword").attr("placeholder","产品名称 / 团号 / 目的地"):"";
        //隐藏批发商认证
        $("#wholesalers_t1Head").length>0?$("#wholesalers_t1Head").hide():"";
        //隐藏橘黄色区域选择框
        $("#topTab_jumpParam").length>0?$("#topTab_jumpParam").hide():"";
        //认证详情
        $("#pop_permission_detail").length>0?$("#pop_permission_detail").hide():"";
        //隐藏供应商 -orderRecord.jsp
        $("#supStorm").attr("data_from")=="orderRecord"? $("#supStorm").hide():"";
        //t1OrderList.jsp
        $("#supStorm").attr("data_from")=="t1OrderList"? $("#supStorm").hide():"";
    }else  if(domain.indexOf("travel.jsjbt")!=-1) {//jinling
        $("#gray_t1Head").length>0? $(".gray").text("客服电话：025-51860966"):"";
        $("#company_logo").length>0? $("#company_logo").removeClass("hedear-logo").addClass("jinLing_hedear-logo"):"";
        $("#logo_t1Head").length>0? $("#logo_t1Head").addClass(""):"";
        $("#selfFooter").length>0?$("#selfFooter").show():"";
        // $("#logo_t1Head").length>0? $("#logo_t1Head").addClass("ht_hedear-logo"):"";
        $("#keyword").attr("data_from")=="jumpParam"? $("#keyword").attr("placeholder","产品名称 / 团号 / 目的地"):"";
        //隐藏批发商认证
        $("#wholesalers_t1Head").length>0?$("#wholesalers_t1Head").hide():"";
        //隐藏橘黄色区域选择框
        $("#topTab_jumpParam").length>0?$("#topTab_jumpParam").hide():"";
        //认证详情
        $("#pop_permission_detail").length>0?$("#pop_permission_detail").hide():"";
        //隐藏供应商 -orderRecord.jsp
        $("#supStorm").attr("data_from")=="orderRecord"? $("#supStorm").hide():"";
        //t1OrderList.jsp
        $("#supStorm").attr("data_from")=="t1OrderList"? $("#supStorm").hide():"";

    }else{//common
        //修改上部条左上角内容
        $("#gray_t1Head").length>0? $(".gray").text("旅游交易预订系统"):"";
        //修改公司logo
        $("#company_logo").length>0? $("#company_logo").addClass("hedear-logo"):"";
        $("#logo_t1Head").length>0? $("#logo_t1Head").addClass("hedear-logo"):"";
        //修改版权
        $("#commonFooter").length>0? $("#commonFooter").show():"";
        // $("#logo_t1Head").length>0? $("#logo_t1Head").addClass("hedear-logo"):"";
        //修改列表页的搜索框默认值
        $("#keyword").attr("data_from")=="jumpParam"? $("#keyword").attr("placeholder","产品名称 / 供应商 / 团号 / 目的地"):"";
        //隐藏批发商认证
        $("#wholesalers_t1Head").length>0?$("#wholesalers_t1Head").show():"";
        //隐藏橘黄色区域选择框
        $("#topTab_jumpParam").length>0?$("#topTab_jumpParam").show():"";
        //认证详情
        $("#pop_permission_detail").length>0?$("#pop_permission_detail").show():"";
        //隐藏批发商筛选条件
        //隐藏供应商  -orderRecord.jsp
        $("#supStorm").attr("data_from")=="orderRecord"? $("#supStorm").show():"";
        //t1OrderList.jsp
        $("#supStorm").attr("data_from")=="t1OrderList"? $("#supStorm").show():"";
    }

});
