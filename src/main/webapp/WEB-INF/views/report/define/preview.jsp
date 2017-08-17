<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>报表定义列表</title>
  <meta name="decorator" content="wholesaler"/>
  <script type="text/javascript">
    $(function() {
//      $("[name='invoiceDate']").datepicker();
//      <!-- S 89需求-->
//      $("[name='bankName']").comboboxInquiry({
//        removeIfInvalid:false
//      });
//      $("[name='bankAccount']").comboboxInquiry({
//        removeIfInvalid:false
//      });
//
//      $("[name='accountName']").comboboxInquiry();

//      $('[name="bankName"]+.custom-combobox').on( "autocompleteselect", function( event, ui ) {
//        alert(ui.item.option.value);
//        //仅为展示效果
//        var data = [[0,0,0,0,0,'6223 7343 7321 1244 987'],[0,0,0,0,0,'6223 7343 1233 1421 453']];
//        var options = '';
//        if (data != null) {
//          for ( var i = 0; i < data.length; i++) {
//            options += '<option value="'+data[i][5]+'">'
//                    + data[i][5] + '</option>';
//          }
//        }
//        $("#bankAccount").html('');
//        $("#bankAccount").append(options);
//      });
//      $('[name="bankName"]+.custom-combobox').on( "autocompletechange", function( event, ui ) {
//        var value = $(this).children('input').val(),
//                valueLowerCase = value.toLowerCase(),
//                valid = false;
//        $(this).prev().children("option").each(function () {
//          if ($(this).text().toLowerCase() === valueLowerCase) {
//            this.selected = valid = true;
//            return false;
//          }
//        });
//        if (valid) {
//          this._trigger("afterInvalid", null, value);
//          return;
//        }
//        if(!valid){
//          $("#bankAccount").html('<option value=""></option>');
//        }
//      });

//      $('[name="bankName"]+.custom-combobox input').on('keyup',function(){
//        var value = this.value.replace(/[^A-Za-z\u4e00-\u9fa5\s]/g,'');
//        this.value = value.length>100?value.substr(0,100):value;
//      });
//      $('[name="bankAccount"]+.custom-combobox input').on('keyup',function(){
//        var  value = this.value.replace(/[^0-9\s]/g,'');
//        this.value = value.length>100?value.substr(0,100):value;
//      });
      //E 89需求-->
//      $(".patorder_a1").click(
//              function() {
//                $(this).css({
//                  "color" : "#3A7851",
//                  "backgroundColor" : "#FFF"
//                }).siblings().css({
//                  "color" : "#000",
//                  "backgroundColor" : ""
//                });
//                $(this).parent().siblings().children('#offlinebox_1').show().siblings().hide();
//              }).click();
//
//      $(".patorder_a2").click(
//              function() {
//                $(this).css({
//                  "color" : "#3A7851",
//                  "backgroundColor" : "#FFF"
//                }).siblings().css({
//                  "color" : "#000",
//                  "backgroundColor" : ""
//                });
//                $(this).parent().siblings().children('#offlinebox_2').show().siblings().hide();
//              });
//
//      $(".patorder_a3").click(
//              function() {
//                $(this).css({
//                  "color" : "#3A7851",
//                  "backgroundColor" : "#FFF"
//                }).siblings().css({
//                  "color" : "#000",
//                  "backgroundColor" : ""
//                });
//                $(this).parent().siblings().children('#offlinebox_4').show().siblings().hide();
//              });
//
//      $(".patorder_a4").click(
//              function() {
//                $(this).css({
//                  "color" : "#3A7851",
//                  "backgroundColor" : "#FFF"
//                }).siblings().css({
//                  "color" : "#000",
//                  "backgroundColor" : ""
//                });
//                $(this).parent().siblings().children('#offlinebox_5').show().siblings().hide();
//              });
  <c:forEach items="${lists}" var="list" varStatus="s">
      <c:choose>
        <c:when test="${s.count eq 1}">
          $(".patorder_a${s.count}").click(
                  function() {
                    $(this).css({
                      "color" : "#3A7851",
                      "backgroundColor" : "#FFF"
                    }).siblings().css({
                      "color" : "#000",
                      "backgroundColor" : ""
                    });
                    $(this).parent().siblings().children('#offlinebox_${s.count}').show().siblings().hide();
                  }).click();
        </c:when>
        <c:otherwise>
          $(".patorder_a${s.count}").click(
                  function() {
                    $(this).css({
                      "color" : "#3A7851",
                      "backgroundColor" : "#FFF"
                    }).siblings().css({
                      "color" : "#000",
                      "backgroundColor" : ""
                    });
                    $(this).parent().siblings().children('#offlinebox_${s.count}').show().siblings().hide();
                  });
        </c:otherwise>
      </c:choose>
      <%--<c:if test="${s.count eq 1}">--%>
        <%--$(".patorder_a${s.count}").click(--%>
                <%--function() {--%>
                  <%--$(this).css({--%>
                    <%--"color" : "#3A7851",--%>
                    <%--"backgroundColor" : "#FFF"--%>
                  <%--}).siblings().css({--%>
                    <%--"color" : "#000",--%>
                    <%--"backgroundColor" : ""--%>
                  <%--});--%>
                  <%--$(this).parent().siblings().children('#offlinebox_${s.count}').show().siblings().hide();--%>
                <%--}).click();--%>
      <%--</c:if>--%>
      <%--<c:otherwise>--%>
      <%--$(".patorder_a${s.count}").click(--%>
              <%--function() {--%>
                <%--$(this).css({--%>
                  <%--"color" : "#3A7851",--%>
                  <%--"backgroundColor" : "#FFF"--%>
                <%--}).siblings().css({--%>
                  <%--"color" : "#000",--%>
                  <%--"backgroundColor" : ""--%>
                <%--});--%>
                <%--$(this).parent().siblings().children('#offlinebox_${s.count}').show().siblings().hide();--%>
              <%--});--%>
      <%--</c:otherwise>--%>
  </c:forEach>

//      $payforRadio = $('input[name=paymentStatus]');
//      $payforRadio.change(function() {
//        var payforRadioVal = $(this).val();
//        if (payforRadioVal != 1) {
//          /* 				top.$.jBox.confirm('您是否按月结支付？', '系统提示', function(v) {
//           if (v == 'ok') { */
//          $('#payBtn').text('确认');
//          /* 					} else {
//           $('input[name=paymentStatus]')[1].checked = true;
//           $('#payBtn').text('确认支付');
//           }
//           }, {
//           buttonsFocus : 1
//           });
//           top.$('.jbox-body .jbox-icon').css('top', '55px');
//           return false; */
//        } else {
//          $('#payBtn').text('确认支付');
//        }
//      });
    });
  </script>

</head>
<body>
<div id="offline_paybox" class="pay_clearfix" style="clear: both;">
  <div id="payorderbgcolor" style="display: block; z-index: 2;height:30px; position:relative;">
    <c:forEach items="${lists}" var="list" varStatus="s">
      <div class="patorder_a${s.count}" style="border-width: 1px 1px medium; border-style: solid solid none; border-color: rgb(204, 204, 204); border-radius: 3px 3px 0px 0px; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">Tab${s.count}</div>
    </c:forEach>
    <%--<div class="patorder_a1" style="border-width: 1px 1px medium; border-style: solid solid none; border-color: rgb(204, 204, 204); border-radius: 3px 3px 0px 0px; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">支票</div>--%>
    <%--<div class="patorder_a2" style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">现金支付</div>--%>
    <%--<div class="patorder_a3" style="border-width: 1px 1px medium; border-style: solid solid none; border-color: rgb(204, 204, 204); border-radius: 3px 3px 0px 0px; width: 90px; height: 29px; text-align: center; color: rgb(58, 120, 81); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer; background-color: rgb(255, 255, 255);">汇款</div>--%>
  </div>

  <div class="payORDER" style="clear:both; padding:20px; border:1px #cccccc solid; margin-top:-2px; margin-top:-1px;background-color:#FFF;">

  <c:forEach items="${lists}" var="list" varStatus="s">

    <div id="offlinebox_${s.count}" class="payDiv" style="display: none;">

      <table border="1">

        <c:forEach items="${list}" var="report1" varStatus="ss1" end="0">
          <tr>
            <c:forEach items="${report1}" var="temp1" varStatus="sss1">
              <td style="min-width: 30px" class="tc">${temp1.key}</td>
            </c:forEach>
          </tr>
        </c:forEach>

          <c:forEach items="${list}" var="report" varStatus="ss">
            <tr>
            <c:forEach items="${report}" var="temp" varStatus="sss">
              <td style="min-width: 30px" class="tc">${temp.value}</td>
            </c:forEach>
            </tr>
          </c:forEach>

      </table>
      <%--<form id="offlineform_1" method="post" action="/a/orderPay/savePay" style="margin:0px; padding:0px;" novalidate="novalidate">--%>
        <%--<span style="color:#f00;">*为确保您的订单能够及时处理，请正确填写以下信息。</span>--%>
        <%--<input type="hidden" name="orderId" value="10416">--%>
        <%--<input type="hidden" name="orderNum" value="ZGQ160111036">--%>
        <%--<input type="hidden" name="orderType" value="10">--%>
        <%--<input type="hidden" name="businessType" value="1">--%>
        <%--<input type="hidden" name="payPriceType" value="1">--%>
        <%--<input type="hidden" name="isCommonOrder" value="yes">--%>
        <%--<input type="hidden" name="fileIDList" class="fileIDList">--%>
        <%--<input type="hidden" name="fileNameList" class="fileNameList">--%>
        <%--<input type="hidden" name="filePathList" class="filePathList">--%>
        <%--<input type="hidden" name="visaId" value="">--%>
        <%--<input type="hidden" name="paymentStatus" value="">--%>
        <%--<input type="hidden" name="orderDetailUrl" value="/a/orderCommon/manage/orderDetail/10416">--%>
        <%--<input type="hidden" name="paymentStatusLbl" value="">--%>
        <%--<input type="hidden" name="payType" value="1">--%>
        <%--<table width="100%" cellpadding="5" cellspacing="0" border="0">--%>
          <%--<tbody><tr>--%>
            <%--<td class="trtextaling">加元金额：C$</td>--%>
            <%--<td>--%>
              <%--<input type="text" maxlength="13" onblur="valdate('true',this,'1600.00','加元');" value="1600.00" name="dqzfprice">--%>
            <%--</td>--%>
            <%--<input type="hidden" value="40" name="currencyIdPrice">--%>
          <%--</tr>--%>
          <%--</tbody><tbody>--%>
        <%--<tr>--%>
          <%--<td class="trtextaling"><span style="color:#f00;">*</span>来款单位：</td>--%>
          <%--<td class="trtextalingi"><input type="text"  maxlength="25" name="payerName" class="required" value="达美旅游集团" id="payerNameID_1"></td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
          <%--<td class="trtextaling"><span style="color:#f00;">*</span>支票号：</td>--%>
          <%--<td class="trtextalingi"><input class="check_char_or_num required" maxlength="10" type="text" name="checkNumber" id="checkNumber"></td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
          <%--<td class="trtextaling"><span style="color:#f00;">*</span>开票日期：</td>--%>
          <%--<td class="trtextalingi"><input type="text" name="invoiceDate" class="required" readonly="" id="invoiceDate"></td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
          <%--<td class="trtextaling payforFiles-t"><span style="color:#f00;">*</span>支票图片：</td>--%>
          <%--<td class="trtextalingi payforFiles">--%>
            <%--<input type="button" name="DocInfoIds" class="btn btn-primary" value="上传" onclick="uploadFiles('/a',null,this);">--%>
            <%--<ol class="batch-ol">--%>
            <%--</ol>--%>
          <%--</td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
          <%--<td class="trtextaling" style="">备注信息：</td>--%>
          <%--<td class="trtextalingi" style="vertical-align:top">--%>
            <%--<textarea maxlength="120" name="remarks" style="width:500px; resize:none;"></textarea>--%>
          <%--</td>--%>
        <%--</tr>--%>
        <%--</tbody>--%>
        <%--</table>--%>
      <%--</form>--%>
    </div>
    </c:forEach>
    <%--<div id="offlinebox_2" style="display:none;" class="payDiv">--%>
      <%--aaa--%>
      <%--<form id="offlineform_2" method="post" action="/a/orderPay/savePay" style="margin:0px; padding:0px;" novalidate="novalidate">--%>
        <%--<span style="color:#F00">* 为确保您的订单能够及时处理，请付款后，正确填写POS单号。</span>--%>
        <%--<input type="hidden" name="orderId" value="10416">--%>
        <%--<input type="hidden" name="orderNum" value="ZGQ160111036">--%>
        <%--<input type="hidden" name="orderType" value="10">--%>
        <%--<input type="hidden" name="businessType" value="1">--%>
        <%--<input type="hidden" name="payPriceType" value="1">--%>
        <%--<input type="hidden" name="isCommonOrder" value="yes">--%>
        <%--<input type="hidden" name="fileIDList" class="fileIDList">--%>
        <%--<input type="hidden" name="fileNameList" class="fileNameList">--%>
        <%--<input type="hidden" name="filePathList" class="filePathList">--%>
        <%--<input type="hidden" name="visaId" value="">--%>
        <%--<input type="hidden" name="paymentStatus" value="">--%>
        <%--<input type="hidden" name="orderDetailUrl" value="/a/orderCommon/manage/orderDetail/10416">--%>
        <%--<input type="hidden" name="paymentStatusLbl" value="">--%>
        <%--<input type="hidden" name="payType" value="2">--%>
        <%--<table width="100%" cellpadding="5" cellspacing="0" border="0">--%>
          <%--<tbody>--%>
          <%--<tr>--%>
            <%--<td class="trtextaling"><span style="color:#f00;">*</span>POS单号：</td>--%>
            <%--<td class="trtextalingi"><input type="text" name="posNo" maxlength="20" class="required"></td>--%>
          <%--</tr>--%>
          <%--<tr>--%>
            <%--<td class="trtextaling"><span style="color:#f00;">*</span>POS机终端号：</td>--%>
            <%--<td class="trtextalingi"><input type="text" name="posTagEend" maxlength="20" class="required"></td>--%>
          <%--</tr>--%>
          <%--<tr>--%>
            <%--<td class="trtextaling"><span style="color:#f00;">*</span>POS机所属银行：</td>--%>
            <%--<td class="trtextalingi"><input type="text" name="posBank" maxlength="20" class="required"></td>--%>
          <%--</tr>--%>
          <%--<tr>--%>
            <%--<td class="trtextaling" style="">备注信息：</td>--%>
            <%--<td class="trtextalingi" style="vertical-align:top">--%>
              <%--<textarea name="remarks" maxlength="120" style="width:500px;  resize:none;"></textarea></td>--%>
          <%--</tr>--%>
          <%--</tbody>--%>
        <%--</table>--%>
      <%--</form>--%>
    <%--</div>--%>
    <!-- 现金支付 -->
    <%--<div id="offlinebox_3" style="display:none;" class="payDiv">--%>
      <%--bbbb--%>
      <%--<form id="offlineform_3" method="post" action="/a/orderPay/savePay" style="margin:0px; padding:0px;" novalidate="novalidate">--%>
        <%--<span style="color:#F00">* 为确保您的订单能够及时处理，请到财务开具证明。</span>--%>
        <%--<input type="hidden" name="orderId" value="10416">--%>
        <%--<input type="hidden" name="orderNum" value="ZGQ160111036">--%>
        <%--<input type="hidden" name="orderType" value="10">--%>
        <%--<input type="hidden" name="businessType" value="1">--%>
        <%--<input type="hidden" name="payPriceType" value="1">--%>
        <%--<input type="hidden" name="isCommonOrder" value="yes">--%>
        <%--<input type="hidden" name="fileIDList" class="fileIDList">--%>
        <%--<input type="hidden" name="fileNameList" class="fileNameList">--%>
        <%--<input type="hidden" name="filePathList" class="filePathList">--%>
        <%--<input type="hidden" name="visaId" value="">--%>
        <%--<input type="hidden" name="paymentStatus" value="">--%>
        <%--<input type="hidden" name="orderDetailUrl" value="/a/orderCommon/manage/orderDetail/10416">--%>
        <%--<input type="hidden" name="paymentStatusLbl" value="">--%>
        <%--<input type="hidden" name="payType" value="3">--%>
        <%--<table width="100%" cellpadding="5" cellspacing="0" border="0">--%>
          <%--<tbody>--%>
          <%--<tr>--%>
            <%--<td class="trtextaling">加元金额：C$</td>--%>
            <%--<td><input type="text" maxlength="13" onblur="valdate('true',this,'1600.00','加元');" value="1600.00" name="dqzfprice"></td>--%>
            <%--<input type="hidden" value="40" name="currencyIdPrice">--%>
          <%--</tr>--%>
          <%--<tr>--%>
            <%--<td class="trtextaling"><span style="color:#f00;">*</span>来款单位：</td>--%>
            <%--<td class="trtextalingi"><input type="text"  maxlength="25" name="payerName" class="required" value="达美旅游集团" id="payerNameID_3"></td>--%>
          <%--</tr>--%>
          <%--<tr class="trVoucher_3">--%>
            <%--<td class="trtextaling payforFiles-t">支付凭证：</td>--%>
            <%--<td class="payforFiles">--%>
              <%--<input name="passportfile" type="text" style="display:none;" disabled="disabled">--%>
              <%--<input type="button" name="DocInfoIds" class="btn btn-primary" value="上传" onclick="uploadFiles('/a',null,this);">--%>
              <%--<ol class="batch-ol">--%>
              <%--</ol>--%>
              <%--<label style="display:none;font-size: 12px;font-weight: normal;padding-left: 0;margin-left: 10px;padding-bottom: 2px;width: 60px;color: #ea5200">必填信息</label>--%>
            <%--</td>--%>
          <%--</tr>--%>
          <%--<tr>--%>
            <%--<td class="trtextaling" style="">备注信息：</td>--%>
            <%--<td class="trtextalingi" style="vertical-align:top">--%>
              <%--<textarea name="remarks" maxlength="120" style="width:500px;resize:none;"></textarea>--%>
            <%--</td>--%>
          <%--</tr>--%>
          <%--</tbody>--%>
        <%--</table>--%>
      <%--</form>--%>
    <%--</div>--%>
    <!-- 汇款 -->
    <%--<div id="offlinebox_4" style="" class="payDiv">--%>
      <%--<form id="offlineform_4" method="post" action="/a/orderPay/savePay" style="margin:0px; padding:0px;" novalidate="novalidate">--%>
        <%--<input type="hidden" name="orderId" value="10416">--%>
        <%--<input type="hidden" name="orderNum" value="ZGQ160111036">--%>
        <%--<input type="hidden" name="orderType" value="10">--%>
        <%--<input type="hidden" name="businessType" value="1">--%>
        <%--<input type="hidden" name="payPriceType" value="1">--%>
        <%--<input type="hidden" name="isCommonOrder" value="yes">--%>
        <%--<input type="hidden" name="fileIDList" class="fileIDList">--%>
        <%--<input type="hidden" name="fileNameList" class="fileNameList">--%>
        <%--<input type="hidden" name="filePathList" class="filePathList">--%>
        <%--<input type="hidden" name="visaId" value="">--%>
        <%--<input type="hidden" name="paymentStatus" value="">--%>
        <%--<input type="hidden" name="orderDetailUrl" value="/a/orderCommon/manage/orderDetail/10416">--%>
        <%--<input type="hidden" name="paymentStatusLbl" value="">--%>
        <%--<input type="hidden" name="payType" value="4">--%>
        <%--<table width="100%" cellpadding="5" cellspacing="0" border="0">--%>
          <%--<tbody>--%>
          <%--<tr>--%>
            <%--<td class="trtextaling">美元金额：$</td>--%>
            <%--<td><input type="text" onblur="valdate('true',this,'1600.00','加元');" maxlength="13" value="1600.00" name="dqzfprice"></td>--%>
            <%--<input type="hidden" value="40" name="currencyIdPrice">--%>
          <%--</tr>--%>
          <%--<!--S 20160113 127-->--%>
          <%--<tr>--%>
            <%--<td class="trtextaling"><span style="color:#f00;">*</span>来款单位：</td>--%>
            <%--<td class="trtextalingi">--%>
              <%--<input type="text"  maxlength="25" name="payerName" class="required" value="达美旅游集团" id="payerNameID_4">--%>
            <%--</td>--%>
          <%--</tr>--%>
          <%--<tr>--%>
            <%--<td class="trtextaling">来款行名称：</td>--%>
            <%--<td class="trtextalingi sel-bank-of-name">--%>
              <%--<select name="bankName" onchange="changeToBank('1623',[this.options[this.options.selectedIndex].value])">--%>
                <%--<option value=""></option>--%>
                <%--<option value="中国银行">中国银行</option>--%>
              <%--</select>--%>
            <%--</td>--%>
          <%--</tr>--%>
          <%--<tr>--%>
            <%--<td class="trtextaling">来款账户：</td>--%>
            <%--<td class="trtextalingi  sel-bank-of-name">--%>
              <%--<select id="bankAccount" name="bankAccount">--%>
                <%--<option value=""></option>--%>
              <%--</select>--%>
            <%--</td>--%>
          <%--</tr>--%>
          <%--<tr>--%>
            <%--<td class="trtextaling">账户名：</td>--%>
            <%--<td class="trtextalingi sel-bank-of-name">--%>
              <%--<select name="accountName"  style="display: none;">--%>
                <%--<option value=""></option>--%>
                <%--<option value="1">vvv</option>--%>
              <%--</select>--%>
            <%--</td>--%>
          <%--</tr>--%>
          <%--<tr>--%>
            <%--<td class="trtextaling" style="padding:0px;">--%>
              <%--<span style="color:#f00;">*</span>收款行名称：--%>
            <%--</td>--%>
            <%--<td class="trtextalingi ">--%>

              <%--<select id="toBankNname" name="toBankNname"  onchange="changeOpenBank('69',[this.options[this.options.selectedIndex].value])"min="0" validate="required:true"><!-- min="0" validate="required:true"-->--%>
                <%--<option value="-1">--请选择--</option>--%>
                <%--<option value="建设银行">建设银行</option>--%>
                <%--<option value="工商银行">工商银行</option>--%>
              <%--</select>--%>
            <%--</td>--%>
          <%--</tr>--%>
          <%--<tr>--%>
            <%--<td class="trtextaling" style="padding:0px;"><span style="color:#f00;">*</span>收款账户：</td>--%>
            <%--<td class="trtextalingi sel-bank-of-name">--%>

              <%--<select id="toBankAccount" name="toBankAccount" max="0" validate="required:true">--%>
                <%--<option value="1">--请选择--</option>--%>
                <%--<option value="-1">账户1</option>--%>
              <%--</select>--%>
            <%--</td>--%>
          <%--</tr>--%>
          <%--<!--E 20160113 127-->--%>
          <%--<tr class="trVoucher_4">--%>
            <%--<td class="trtextaling payforFiles-t"><span style="color:#f00;">*</span>支付凭证：</td>--%>
            <%--<td class="payforFiles">--%>
              <%--<input type="button" name="DocInfoIds" class="btn btn-primary" value="上传" onclick="uploadFiles('/a',null,this);">--%>
              <%--<ol class="batch-ol">--%>
              <%--</ol>--%>
              <%--<label style="display:none;font-size: 12px;font-weight: normal;padding-left: 0;margin-left: 10px;padding-bottom: 2px;width: 60px;color: #ea5200">必填信息</label>--%>
            <%--</td>--%>
          <%--</tr>--%>
          <%--<tr>--%>
            <%--<td class="trtextaling" style="">备注信息：</td>--%>
            <%--<td class="trtextalingi" style="vertical-align:top">--%>
              <%--<textarea name="remarks" maxlength="120" style="width:500px; resize:none;">00</textarea>--%>
            <%--</td>--%>
          <%--</tr>--%>
          <%--</tbody>--%>
        <%--</table>--%>
      <%--</form>--%>
    <%--</div>--%>
    <%--<div id="offlinebox_5" class="payDiv" style="display: none;">--%>
      <%--<form id="offlineform_5" method="post" action="/a/orderPay/savePay" style="margin:0px; padding:0px;" novalidate="novalidate">--%>
        <%--<input type="hidden" name="orderId" value="10416">--%>
        <%--<input type="hidden" name="orderNum" value="ZGQ160111036">--%>
        <%--<input type="hidden" name="orderType" value="10">--%>
        <%--<input type="hidden" name="businessType" value="1">--%>
        <%--<input type="hidden" name="payPriceType" value="1">--%>
        <%--<input type="hidden" name="isCommonOrder" value="yes">--%>
        <%--<input type="hidden" name="fileIDList" class="fileIDList">--%>
        <%--<input type="hidden" name="fileNameList" class="fileNameList">--%>
        <%--<input type="hidden" name="filePathList" class="filePathList">--%>
        <%--<input type="hidden" name="visaId" value="">--%>
        <%--<input type="hidden" name="paymentStatus" value="">--%>
        <%--<input type="hidden" name="orderDetailUrl" value="/a/orderCommon/manage/orderDetail/10416">--%>
        <%--<input type="hidden" name="paymentStatusLbl" value="">--%>
        <%--<input type="hidden" name="payType" value="5">--%>
        <%--<table width="100%" cellpadding="5" cellspacing="0" border="0">--%>
          <%--<tbody>--%>
          <%--<tr>--%>
            <%--<td class="trtextaling">加元金额：C$</td>--%>
            <%--<td><input type="text" maxlength="13" onblur="valdate('true',this,'1600.00','加元');" value="1600.00" name="dqzfprice"></td>--%>
            <%--<input type="hidden" value="40" name="currencyIdPrice">--%>
          <%--</tr>--%>

          <%--<tr>--%>
            <%--<td class="trtextaling"><span style="color:#f00;">*</span>支付方式：</td>--%>
            <%--<td class="trtextalingi">--%>
              <%--<input type="text" name="fastPayType" maxlength="20" class="required" id="fastPayType">--%>
            <%--</td>--%>
          <%--</tr>--%>
          <%--<tr class="trVoucher_5">--%>
            <%--<td class="trtextaling payforFiles-t">--%>
              <%--<span style="color:#f00;">*</span>支付凭证：--%>
            <%--</td>--%>
            <%--<td class="payforFiles">--%>
              <%--<input type="button" name="DocInfoIds" class="btn btn-primary" value="上传" onclick="uploadFiles('/a',null,this);">--%>
              <%--<ol class="batch-ol">--%>
              <%--</ol>--%>
              <%--<label style="display:none;font-size: 12px;font-weight: normal;padding-left: 0;margin-left: 10px;padding-bottom: 2px;width: 60px;color: #ea5200">必填信息</label>--%>
            <%--</td>--%>
          <%--</tr>--%>
          <%--<tr>--%>
            <%--<td class="trtextaling" style="">备注信息：</td>--%>
            <%--<td class="trtextalingi" style="vertical-align:top">--%>
              <%--<textarea name="remarks" maxlength="120" style="width:500px;resize:none;"></textarea>--%>
            <%--</td>--%>
          <%--</tr>--%>
          <%--</tbody>--%>
        <%--</table>--%>
      <%--</form>--%>
    <%--</div>--%>
  <%--</div>--%>
</div>
</body>
</html>
