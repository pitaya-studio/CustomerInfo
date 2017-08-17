<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="X-UA-Compatible"content="IE=8">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <title>基本信息</title>
    <c:choose>
        <c:when test="${fn:contains(pageContext.request.requestURL,'huitengguoji.com')}">
            <link href="${ctxStatic}/images/huiTeng/huiTengFavicon.ico" rel="shortcut icon"/>
        </c:when>
        <c:when test="${fn:contains(pageContext.request.requestURL,'travel.jsjbt')}">
            <link href="${ctxStatic}/images/jinLing/jinLingFavicon.ico" rel="shortcut icon"/>
        </c:when>
        <c:otherwise>
            <link href="http://t215.quauqsystem.com.cn/favicon.ico" rel="shortcut icon"/>
        </c:otherwise>
    </c:choose>

    <link href="${ctxStatic}/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/t1t2.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/bank_account.css"/>

    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1t2.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1Common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/agentToOffice/t1/agentbase/agentbase.js"></script>
	 <script type="text/javascript">

     $(function () {
         var p = $('.contact_information').first().find('p');
         var div = $('.contact_information').first().children('div');
         var _newdiv = div.eq(0);

         var update_show_id = $('#update_show_id').val();
         // update_show_id == 1 表示是查看方式
         if(update_show_id == '1'){
             div.show();
             return false;
         }
         //表示是以可编辑方式查看
         var accountName = p.find('#accountName').text();

         if(!$.trim(accountName)){
             //表示不存在银行账户信息
             var p = $('.contact_information').first().find('p');
             p.find('.head_hide').addClass('hide');
             p.find('input').show();
             var div = $('.contact_information').first().children('div');
             div.show();
             div.find('span').hide();
             div.find('input,textarea').show();
         }else{
             //表示已存在银行账户信息
             var div = $('.contact_information').first().children('div');
             div.show();
             _newdiv.hide();
         }
         var partten =/^[\u4e00-\u9fa5][A-Za-z0-9]+$/;
     	$(document).ready(function(){
     		 $('input[name=bankName]').keyup(function(){
                  if(!partten.test($(this).val())){
                      var a= $(this).val();
                      var b=a.replace(/[^a-z0-9\u4e00-\u9fa5]+/gi,'');
                      $(this).val(b)
                  }
              }); 

         });
     	var partten = /^\d+$/;
         $(document).ready(function(){
             $('input[name=bankAccountCode]').keyup(function(){
                 if(!partten.test($(this).val())){
                 	var a= $(this).val();
                     var b=a.replace(/[^\d]+/gi,'');
                     $(this).val(b);
                 }
             });
         });
        if(test="${not empty agentBank }"){

        }else{
            _newdiv.hide();
        }

     });
        var partten =/^[\u4e00-\u9fa5][A-Za-z0-9]+$/;
    	$(document).ready(function(){
    		 $('input[name=bankName]').keyup(function(){
                 if(!partten.test($(this).val())){
                     var a= $(this).val();
                     var b=a.replace(/[^a-z0-9\u4e00-\u9fa5]+/gi,'');
                     $(this).val(b)
                 }
             }); 

        });
    	var partten = /^\d+$/;
        $(document).ready(function(){
            $('input[name=bankAccountCode]').keyup(function(){
                if(!partten.test($(this).val())){
                	var a= $(this).val();
                    var b=a.replace(/[^\d]+/gi,'');
                    $(this).val(b);
                }
            });
        });
    </script>
    
</head>
<body>
<!--header start-->
<c:if test="${updateShow != 1 }">
    <%@ include file="/WEB-INF/views/modules/homepage/T1Head.jsp"%>
</c:if>
<!--header end-->
<div class="sea">
    <!--main start-->
    <div class="main">
        <div class="middle">
            <div class="main-left">
                <ul>
                    <li><i></i><a href="${ctx}/person/info/getAgentInfo?agentId=${agentId}&updateShow=${updateShow}" style="color:#333333">基本信息</a></li>
                    <li class="li-active"><i></i><a href="${ctx}/person/info/getAgentBank?agentId=${agentId}&updateShow=${updateShow}">银行账户</a></li>
                    <li><i></i><a href="${ctx}/person/info/getAgentQualification?agentId=${agentId}&updateShow=${updateShow}" style="color:#333333">资质信息</a></li>
                </ul>
            </div>
            <div class="main-right">
                <div class="content">
                	<c:if test="${updateShow != 1 }">
                    <div class="bread"><i></i>您的位置：<a href="javascript:void(0)" onclick="goHomePage('${ctx}');" style="color:#333">首页</a> > 银行账户</div>
                    </c:if>
                    <div id="contact_information">
                        <p class="new_bank_account">
                            <c:if test="${'1' ne updateShow}"><span onclick="new_agent_bank('${ctx}')"><em class="t1_2 plus_sign"></em>新建银行账户</span></c:if>
                            <input type="hidden" value="${updateShow}" id="update_show_id" />
                            <input type="hidden" name="t1Saler" value="${t1Saler }" id="t1Saler"/>
                        </p>
                        <c:choose>
                        	<c:when test="${not empty agentBank }">
                        		<c:forEach items="${agentBank}" var="bank">

                        		<c:choose>
                        			<c:when test="${bank.accountPayType ==1  }">
                                        <div class="contact_information">
                                            <c:if test="${updateShow ne 1}">
                                                <div class="dl-select small-dl new_absolute type_select" >
                                                <input style="width: 90px;" value="微信" type="text" name="accountType">
                                                <ul style="width: 102px; display: none;" class="select-option">
                                                    <li type="123">银行卡</li>
                                                    <li type="456">支付宝</li>
                                                    <li type="789">微信</li>
                                                </ul>
                                            </div>
                                            </c:if>
                                            <p class="head_background">
                                                <span class="inline"><font>*</font>账户类型：</span><span class="contact_information_name head_hide" id="accountType" title="">
		                                	<c:choose>
                                                <c:when test="${bank.accountPayType == 1 }">微信</c:when>
                                                <c:when test="${bank.accountPayType == 2 }">支付宝</c:when>
                                                <c:otherwise>
                                                    银行卡
                                                </c:otherwise>
                                            </c:choose>
		                                </span><span class="accountTyleClass"></span>
                                                <span class="bank_account hide"><span class="inline accountHeadSpan"><font>*</font>账户名称：</span><span class="contact_information_name head_hide" id="accountName" title="${bank.accountName }">${bank.accountName}</span><input type="text" name="accountName" class="hide contact_information_input" maxlength="50"/></span>
                                                <span class="alipay_account "><span class="inline accountHeadSpan"><font>*</font>
                                                		<span class="accountChange">微信</span>账户：</span>
                                                		<span class="contact_information_name head_hide" id="alipayNumber" title="${bank.bankAccountCode }" >${bank.bankAccountCode }</span>
                                                		<input type="text" name="alipayNumber" class="hide contact_information_input" maxlength="50"/>
                                                </span>
                                                <input type="hidden" name="bankId" value="${bank.id}"/>
                                               <%--  <input type="hidden" name="defaultFlag" value="${bank.defaultFlag}"/>
                                                <c:if test="${'1' ne updateShow}">
                                                    <c:if test="${'0' eq bank.defaultFlag}"><span class="default" id="defaultAccount">默认账户</span></c:if>
                                                    <c:if test="${'0' ne bank.defaultFlag}"><span class="setDefault setDefault_hover" id="setDefaultAccount" onclick="agent_bank_default(this, '${ctx}')">设为默认</span></c:if>
                                                </c:if> --%>
                                                <span class="float_right">
		                                    <span class="slide" onclick="slide_open_close(this)"><em class="t1_2 slide_open_close"></em></span>
		                                </span>
                                                <span class="float_right hide delete_save">
		                                    <c:if test="${'1' ne updateShow}"><span class="delete" onclick="delete_agent_bank(this, '${ctx}')"><em class="t1_2 delete_t1_2"></em>删除</span><span class="save hide" onclick="save_agent_bank(this, '${ctx}');"><em class="t1_2 save_t1_2"></em>保存</span><span class="compile" onclick="editor_agent_bank(this);"><em class="t1_2 compile_t1_2"></em>编辑</span></c:if>
		                                </span>

                                            </p>
                                            <div class="hide">
                                                <table class="channel_information_table bank_table hide">
                                                    <tr>
                                                        <td width="165" class="overflow"><font>*</font>账户号码：</td>
                                                        <td width="400">
                                                            <span id="bankAccountCode" class="overflow">${bank.bankAccountCode}</span>
                                                            <input class="hide" name="bankAccountCode" type="text" maxlength="50"/>
                                                        </td>
                                                        <td  width="165"></td>
                                                        <td  width="360">

                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td width="165" class="overflow"><font>*</font>开户行名称：</td>
                                                        <td width="400">
                                                            <span id="bankName" class="overflow">${bank.bankName}</span>
                                                            <input class="hide" name="bankName" type="text" maxlength="50"/>
                                                        </td>
                                                        <td  width="165"></td>
                                                        <td  width="360">

                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="overflow">开户行地址：</td>
                                                        <td >
                                                            <span id="bankAddr" class="overflow inline" style="word-break: break-all;display: inline-block;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;width: 500px;" title="${bank.bankAddr}">${bank.bankAddr}</span>
                                                            <input class="hide" type="text" name="bankAddr"/>
                                                        </td>
                                                        <td></td>
                                                        <td>

                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>描述：</td>
                                                        <td colspan="3">
                                                            <span id="bankRemark" style="display: inline-block;word-break: break-all" >${bank.remarks}</span>
                                                            <textarea name="bankRemark" cols="30" rows="10" class="contact_information_text hide"></textarea>
                                                        </td>
                                                    </tr>
                                                </table>
                                                <table class="channel_information_table alipay_table ">
                                                    <tr>
                                                        <td width="165" class="overflow"><font>*</font><span class="accountChange">微信</span>姓名：</td>
                                                        <td width="400">
                                                            <span id="alipay" class="overflow" title="${bank.accountName }">${bank.accountName }</span>
                                                            <input class="hide" name="alipay" type="text" maxlength="50"/>
                                                        </td>
                                                        <td  width="165"></td>
                                                        <td  width="360">

                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>描述：</td>
                                                        <td colspan="3">
                                                            <span id="alipayRemark" style="display: inline-block;word-break: break-all;" >${bank.remarks }</span>
                                                            <textarea name="alipayRemark" cols="30" rows="10" class="contact_information_text hide"></textarea>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                        			</c:when>
                        			<c:when test="${bank.accountPayType == 2 }">
                                        <div class="contact_information">
                                            <c:if test="${updateShow ne 1}">
                                            <div class="dl-select small-dl new_absolute type_select" >
                                                <input style="width: 90px;" value="支付宝" type="text" name="accountType">
                                                <ul style="width: 102px; display: none;" class="select-option">
                                                    <li type="123">银行卡</li>
                                                    <li type="456">支付宝</li>
                                                    <li type="789">微信</li>
                                                </ul>
                                            </div>
                                            </c:if>
                                            <p class="head_background">
                                                <span class="inline"><font>*</font>账户类型：</span><span class="contact_information_name head_hide" id="accountType" title="">
		                                	<c:choose>
                                                <c:when test="${bank.accountPayType == 1 }">微信</c:when>
                                                <c:when test="${bank.accountPayType == 2 }">支付宝</c:when>
                                                <c:otherwise>
                                                    银行卡
                                                </c:otherwise>
                                            </c:choose>
		                                </span>
		                                <span class="accountTyleClass"></span>
                                                <span class="bank_account hide"><span class="inline accountHeadSpan"><font>*</font>账户名称：</span>
	                                                <span class="contact_information_name head_hide" id="accountName" title="${bank.accountName }">${bank.accountName}</span>
	                                                <input type="text" name="accountName" class="hide contact_information_input" maxlength="50"/>
                                                </span>
                                                <span class="alipay_account "><span class="inline accountHeadSpan"><font>*</font><span class="accountChange">支付宝</span>账户：</span>
                                                	<span class="contact_information_name head_hide" id="alipayNumber" title="${bank.bankAccountCode }">${bank.bankAccountCode}</span>
                                                	<input type="text" name="alipayNumber" class="hide contact_information_input" maxlength="50"/>
                                                </span>
                                                <input type="hidden" name="bankId" value="${bank.id}"/>
                                               <%--  <input type="hidden" name="defaultFlag" value="${bank.defaultFlag}"/>
                                                <c:if test="${'1' ne updateShow}">
                                                    <c:if test="${'0' eq bank.defaultFlag}"><span class="default" id="defaultAccount">默认账户</span></c:if>
                                                    <c:if test="${'0' ne bank.defaultFlag}"><span class="setDefault setDefault_hover" id="setDefaultAccount" onclick="agent_bank_default(this, '${ctx}')">设为默认</span></c:if>
                                                </c:if> --%>
                                                <span class="float_right">
		                                    <span class="slide" onclick="slide_open_close(this)"><em class="t1_2 slide_open_close"></em></span>
		                                </span>
                                                <span class="float_right hide delete_save">
		                                    <c:if test="${'1' ne updateShow}"><span class="delete" onclick="delete_agent_bank(this, '${ctx}')"><em class="t1_2 delete_t1_2"></em>删除</span><span class="save hide" onclick="save_agent_bank(this, '${ctx}');"><em class="t1_2 save_t1_2"></em>保存</span><span class="compile" onclick="editor_agent_bank(this);"><em class="t1_2 compile_t1_2"></em>编辑</span></c:if>
		                                </span>

                                            </p>
                                            <div class="hide">
                                                <table class="channel_information_table bank_table hide">
                                                    <tr>
                                                        <td width="165" class="overflow"><font>*</font>账户号码：</td>
                                                        <td width="400">
                                                            <span id="bankAccountCode" class="overflow">${bank.bankAccountCode}</span>
                                                            <input class="hide" name="bankAccountCode" type="text" maxlength="50"/>
                                                        </td>
                                                        <td  width="165"></td>
                                                        <td  width="360">

                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td width="165" class="overflow"><font>*</font>开户行名称：</td>
                                                        <td width="400">
                                                            <span id="bankName" class="overflow">${bank.bankName}</span>
                                                            <input class="hide" name="bankName" type="text" maxlength="50"/>
                                                        </td>
                                                        <td  width="165"></td>
                                                        <td  width="360">

                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="overflow">开户行地址：</td>
                                                        <td >
                                                            <span id="bankAddr" class="overflow inline" style="word-break: break-all;display: inline-block;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;width: 500px;" title="${bank.bankAddr}">${bank.bankAddr}</span>
                                                            <input class="hide" type="text" name="bankAddr"/>
                                                        </td>
                                                        <td></td>
                                                        <td>

                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>描述：</td>
                                                        <td colspan="3">
                                                            <span id="bankRemark" style="display: inline-block;word-break: break-all;" title="${bank.remarks}">${bank.remarks}</span>
                                                            <textarea name="bankRemark" cols="30" rows="10" class="contact_information_text hide"></textarea>
                                                        </td>
                                                    </tr>
                                                </table>
                                                <table class="channel_information_table alipay_table ">
                                                    <tr>
                                                        <td width="165" class="overflow"><font>*</font><span class="accountChange">支付宝</span>姓名：</td>
                                                        <td width="400">
                                                            <span id="alipay" class="overflow" title="${bank.accountName}">${bank.accountName}</span>
                                                            <input class="hide" name="alipay" type="text" maxlength="50"/>
                                                        </td>
                                                        <td  width="165"></td>
                                                        <td  width="360">

                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>描述：</td>
                                                        <td colspan="3">
                                                            <span id="alipayRemark" style="display: inline-block;word-break: break-all;" title="${bank.remarks}">${bank.remarks}</span>
                                                            <textarea name="alipayRemark" cols="30" rows="10" class="contact_information_text hide"></textarea>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                        			</c:when>
                        			<c:otherwise>
                        				<div class="contact_information">
                                            <c:if test="${updateShow ne 1}">
                                    <div class="dl-select small-dl new_absolute type_select"  >
                                        <input style="width: 90px;" value="银行卡" type="text" name="accountType">
                                        <ul style="width: 102px; display: none;" class="select-option">
                                            <li type="123">银行卡</li>
                                            <li type="456">支付宝</li>
                                            <li type="789">微信</li>
                                        </ul>
                                    </div>
                                            </c:if>
		                            <p class="head_background">
		                                <span class="inline"><font>*</font>账户类型：</span><span class="contact_information_name head_hide" id="accountType" title="">
		                                	<c:choose>
		                                		<c:when test="${bank.accountPayType == 1 }">微信</c:when>
		                                		<c:when test="${bank.accountPayType == 2 }">支付宝</c:when>
		                                		<c:otherwise>
		                                			银行卡
		                                		</c:otherwise>
		                                	</c:choose>
		                                </span><span class="accountTyleClass"></span>
		                                <span class="bank_account"><span class="inline accountHeadSpan"><font>*</font>账户名称：</span><span class="contact_information_name head_hide" id="accountName" title="${bank.accountName }">${bank.accountName}</span><input type="text" name="accountName" class="hide contact_information_input" maxlength="50"/></span>
		                                <span class="alipay_account hide"><span class="inline accountHeadSpan"><font>*</font><span class="accountChange">支付宝</span>账户：</span><span class="contact_information_name head_hide" id="alipayNumber" ></span><input type="text" name="alipayNumber" class="hide contact_information_input" maxlength="50"/></span>
		                                <input type="hidden" name="bankId" value="${bank.id}"/>
		                                <%-- <input type="hidden" name="defaultFlag" value="${bank.defaultFlag}"/>
		                                <c:if test="${'1' ne updateShow}">
		                                    <c:if test="${'0' eq bank.defaultFlag}"><span class="default" id="defaultAccount">默认账户</span></c:if>
		                                    <c:if test="${'0' ne bank.defaultFlag}"><span class="setDefault setDefault_hover" id="setDefaultAccount" onclick="agent_bank_default(this, '${ctx}')">设为默认</span></c:if>
		                                </c:if> --%>
		                                <span class="float_right">
		                                    <span class="slide" onclick="slide_open_close(this)"><em class="t1_2 slide_open_close"></em></span>
		                                </span>
		                                <span class="float_right hide delete_save">
		                                    <c:if test="${'1' ne updateShow}"><span class="delete" onclick="delete_agent_bank(this, '${ctx}')"><em class="t1_2 delete_t1_2"></em>删除</span><span class="save hide" onclick="save_agent_bank(this, '${ctx}');"><em class="t1_2 save_t1_2"></em>保存</span><span class="compile" onclick="editor_agent_bank(this);"><em class="t1_2 compile_t1_2"></em>编辑</span></c:if>
		                                </span>
		
		                            </p>
		                            <div class="hide">
		                                <table class="channel_information_table bank_table">
		                                    <tr>
		                                        <td width="165" class="overflow"><font>*</font>账户号码：</td>
		                                        <td width="400">
		                                            <span id="bankAccountCode" class="overflow">${bank.bankAccountCode}</span>
		                                            <input class="hide" name="bankAccountCode" type="text" maxlength="50"/>
		                                        </td>
		                                        <td  width="165"></td>
		                                        <td  width="360">
		
		                                        </td>
		                                    </tr>
                                            <tr>
                                                <td width="165" class="overflow"><font>*</font>开户行名称：</td>
                                                <td width="400">
                                                    <span id="bankName" class="overflow">${bank.bankName}</span>
                                                    <input class="hide" name="bankName" type="text" maxlength="50"/>
                                                </td>
                                                <td  width="165"></td>
                                                <td  width="360">

                                                </td>
                                            </tr>
		                                    <tr>
		                                        <td class="overflow">开户行地址：</td>
		                                        <td >
		                                            <span id="bankAddr" class="overflow inline" style="word-break: break-all;display: inline-block;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;width: 500px;" title="${bank.bankAddr}">${bank.bankAddr}</span>
		                                            <input class="hide" type="text" name="bankAddr"/>
		                                        </td>
		                                        <td></td>
		                                        <td>
		
		                                        </td>
		                                    </tr>
		                                    <tr>
		                                        <td>描述：</td>
		                                        <td colspan="3">
		                                            <span id="bankRemark" style="display: inline-block;word-break: break-all" title="${bank.remarks}">${bank.remarks}</span>
		                                            <textarea name="bankRemark" cols="30" rows="10" class="contact_information_text hide"></textarea>
		                                        </td>
		                                    </tr>
		                                </table>
                                        <table class="channel_information_table alipay_table hide">
                                            <tr>
                                                <td width="165" class="overflow"><font>*</font><span class="accountChange">支付宝</span>姓名：</td>
                                                <td width="400">
                                                    <span id="alipay" class="overflow"></span>
                                                    <input class="hide" name="alipay" type="text" maxlength="50"/>
                                                </td>
                                                <td  width="165"></td>
                                                <td  width="360">

                                                </td>
                                            </tr>
                                            <tr>
                                                <td>描述：</td>
                                                <td colspan="3">
                                                    <span id="alipayRemark" style="display: inline-block;word-break: break-all" title=""></span>
                                                    <textarea name="alipayRemark" cols="30" rows="10" class="contact_information_text hide"></textarea>
                                                </td>
                                            </tr>
                                        </table>
		                            </div>
		                        </div>
                        			</c:otherwise>
                        		</c:choose>


		                        </c:forEach>
                        	</c:when>
                        	<c:otherwise>

                        		<div class="contact_information">
                                    <c:if test="${updateShow ne 1}">
                                    <div class="dl-select small-dl new_absolute type_select" >
                                        <input style="width: 90px;" value="银行卡" type="text" name="accountType">
                                        <ul style="width: 102px; display: none;" class="select-option">
                                            <li type="123">银行卡</li>
                                            <li type="456">支付宝</li>
                                            <li type="789">微信</li>
                                        </ul>
                                    </div>
                                    </c:if>
                                <p class="head_background">
                                    <span class="inline"><font>*</font>账户类型：</span><span class="contact_information_name head_hide" id="accountType" title=""></span><span class="accountTyleClass" ></span>
                                    <span class="bank_account"><span class="inline accountHeadSpan"><font>*</font>账户名称：</span><span class="contact_information_name head_hide" id="accountName" ></span><input type="text" name="accountName" class="hide contact_information_input" maxlength="50"/></span>
                                    <span class="alipay_account hide"><span class="inline accountHeadSpan"><font>*</font><span class="accountChange">支付宝</span>账户：</span><span class="contact_information_name head_hide" id="alipayNumber" ></span><input type="text" name="alipayNumber" class="hide contact_information_input" maxlength="50"/></span>
                                    <input type="hidden" name="bankId" value="${bank.id}"/>
                                    <%-- <input type="hidden" name="defaultFlag" value="${bank.defaultFlag}"/>
                                    <c:if test="${'1' ne updateShow}">
                                        <c:if test="${'0' eq bank.defaultFlag}"><span class="default" id="defaultAccount">默认账户</span></c:if>
                                        <c:if test="${'0' ne bank.defaultFlag}"><span class="setDefault setDefault_hover" id="setDefaultAccount" onclick="agent_bank_default(this, '${ctx}')">设为默认</span></c:if>
                                    </c:if> --%>
                                    <span class="float_right ">
                                        <span class="slide" onclick="slide_open_close(this)"><em class="t1_2 slide_open_close" style="background-position:-145px -48px"></em></span>
                                    </span>
                                    <span class="float_right hide delete_save">
                                        <c:if test="${'1' ne updateShow}"><span class="delete" onclick="delete_agent_bank(this, '${ctx}')"><em class="t1_2 delete_t1_2"></em>删除</span><span class="save" onclick="save_agent_bank(this, '${ctx}');"><em class="t1_2 save_t1_2"></em>保存</span><span class="compile hide" onclick="editor_agent_bank(this);"><em class="t1_2 compile_t1_2"></em>编辑</span></c:if>
                                    </span>
                                </p>
                                <div>
                                    <table class="channel_information_table bank_table">
                                        <tr>
                                            <td width="165" class="overflow"><font>*</font>账户号码：</td>
                                            <td width="400">
                                                <span id="bankAccountCode" class="overflow"></span>
                                                <input class="hide" name="bankAccountCode" type="text" maxlength="50"/>
                                            </td>
                                            <td  width="165"></td>
                                            <td  width="360">

                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="98" class="overflow"><font>*</font>开户行名称：</td>
                                            <td width="400">
                                                <span id="bankName" class="overflow"></span>
                                                <input class="hide" name="bankName" type="text" maxlength="50"/>
                                            </td>
                                            <td width="165"></td>
                                            <td width="360"></td>
                                        </tr>
                                        <tr>
                                            <td class="overflow">开户行地址：</td>
                                            <td>
                                                <span id="bankAddr"  class="overflow" style="display: inline-block;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;width: 500px;"></span>
                                                <input class="hide" type="text" name="bankAddr"/>
                                            </td>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>描述：</td>
                                            <td colspan="3">
                                                <span id="bankRemark" style="display: inline-block;word-break: break-all"></span>
                                                <textarea name="bankRemark" cols="30" rows="10" class="contact_information_text hide"></textarea>
                                            </td>
                                        </tr>
                                    </table>
                                    <table class="channel_information_table alipay_table hide">
                                        <tr>
                                            <td width="165" class="overflow"><font>*</font><span class="accountChange">支付宝</span>姓名：</td>
                                            <td width="400">
                                                <span id="alipay" class="overflow"></span>
                                                <input class="hide" name="alipay" type="text" maxlength="50"/>
                                            </td>
                                            <td  width="165"></td>
                                            <td  width="360">

                                            </td>
                                        </tr>
                                        <tr>
                                            <td>描述：</td>
                                            <td colspan="3">
                                                <span id="alipayRemark" style="display: inline-block;word-break: break-all" title=""></span>
                                                <textarea name="alipayRemark" cols="30" rows="10" class="contact_information_text hide"></textarea>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            </div>

                        	</c:otherwise>
                        </c:choose>
                    </div>
                    <div>
                        <br/><br/>
                    </div>
                </div>
            </div>
    <!--main end-->
</div>
        <!--footer start-->
    </div>
</div>
	<%@ include file="/WEB-INF/views/modules/homepage/t1footer.jsp"%>
</body>
</html>
