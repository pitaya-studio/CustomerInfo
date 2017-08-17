<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="X-UA-Compatible"content="IE=8">
    <%@ page contentType="text/html;charset=UTF-8" %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
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

    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1t2.js"></script>
    <script type="text/javascript" src="${ctxStatic}/agentToOffice/t1/agentbase/agentbase.js"></script>

</head>
<body>
<c:if test="${updateShow != 1 }">
    <%@ include file="/WEB-INF/views/modules/homepage/T1Head.jsp"%>
</c:if>
<div class="sea">
    <!--header start-->
    <!--header end-->

    <!--main start-->
    <div class="main">
        <div class="middle">
            <div class="main-left">
                <ul>
                    <li class="li-active"><i></i><a href="${ctx}/person/info/getAgentInfo?agentId=${agentId}&updateShow=${updateShow}">基本信息</a></li>
                    <li><i></i><a href="${ctx}/person/info/getAgentBank?agentId=${agentId}&updateShow=${updateShow}" style="color:#333333">银行账户</a></li>
                    <li><i></i><a href="${ctx}/person/info/getAgentQualification?agentId=${agentId}&updateShow=${updateShow}" style="color:#333333">资质信息</a></li>
                </ul>
            </div>
            <div class="main-right">
                <div class="content">
                	<c:if test="${updateShow != 1 }">
                    <div class="bread"><i></i>您的位置：<a href="javascript:void(0)" onclick="goHomePage('${ctx}');" style="color:#333">首页 </a>> 基本信息</div>
                    </c:if>
                    <div id="channel_information">
                        <p class="channel_information_p">渠道信息</p>
                        <div class="channel_brand">
                            <p>
                                <span>渠道品牌：</span>
                                <span class="channel_information_distance">${agentInfo.agentBrand}</span>
                            </p>
                        </div>
                        <div class="channel_brand">
                            <p>
                                <span>公司名称：</span>
                                <span class="channel_information_distance">${agentInfo.agentName}</span>
                            </p>
                        </div>
                        <div class="channel_brand">
                            <p>
                                <span>英文名称：</span>
                                <span class="channel_information_distance">${agentInfo.agentNameEn}</span>
                            <c:if test="${updateShow != 1 }">
                            <span class="redact redact_use">
                            <a class="orange float_right" href="javascript:void(0);" onclick="informationSpread(this)">编辑<em class="t1_2 copyReader"></em></a>
                            <a class="orange float_right hide" href="javascript:void(0);" onclick="informationSpread(this)">收起<em class="t1_2 pick_up"></em></a>
                            </span>
                            </c:if>
                            </p>
                            <div class="hide">
                            	<form action="" method="post">
                            		<input  type="hidden" name="id" value="${agentInfo.id }"/>
	                                <input type="text" name="agentNameEn" maxlength="50"/>
	                                <span class="save_btn_left background_orange" onclick="save_input(this);saveInfo('agentNameEn',this,'${ctx }')">保存</span>
                                </form>
                            </div>
                        </div>
                        <div class="channel_brand">
                            <p>
                                <span>所属地区：</span>
                                <span class="channel_information_distance">${agentInfo.agentAddress}</span>
                             <c:if test="${updateShow != 1 }">    
                            <span class="redact redact_use">
                            <a class="orange float_right" href="javascript:void(0);" onclick="informationSpread(this)">编辑<em class="t1_2 copyReader"></em></a>
                            <a class="orange float_right hide" href="javascript:void(0);" onclick="informationSpread(this)">收起<em class="t1_2 pick_up"></em></a>
                            </span>
                            </c:if>
                            </p>
                            <div class="hide">
                            	<form action="" method="post">
                            		<input  type="hidden" name="id" value="${agentInfo.id }"/>
	                                <select  id="belongsArea" onchange="chg(this,'${ctx }');" name="belongsArea">
	                                    <option value="-1">国家</option>
	                                    <c:forEach items="${countryList}" var="c">
	                                        <option value="${c.id}">${c.name}</option>
	                                    </c:forEach>
	                                </select>
	                                <select  id="belongsAreaProvince" onchange="chg(this,'${ctx }')" name="belongsAreaProvince">
	                                    <option value="-1">省（直辖市）</option>
	                                </select>
	                                <select  id="belongsAreaCity" name="belongsAreaCity">
	                                    <option value="-1">市（区）</option>
	                                </select>
                                <span class="save_btn_left background_orange" onclick="save_select(this);saveInfo('belongsArea',this,'${ctx }')">保存</span>
                                </form>
                            </div>
                        </div>
                        <div class="channel_brand">
                            <p>
                                <span>公司地址：</span>
                                <span class="channel_information_distance">${agentInfo.agentAddressFull}</span>
                              <c:if test="${updateShow != 1 }">
                            <span class="redact redact_use">
                            <a class="orange float_right" href="javascript:void(0);" onclick="informationSpread(this)">编辑<em class="t1_2 copyReader"></em></a>
                            <a class="orange float_right hide" href="javascript:void(0);" onclick="informationSpread(this)">收起<em class="t1_2 pick_up"></em></a>
                            </span>
                            </c:if>
                            </p>
                            <div class="hide">
                            	<form action="" method="post">
                            	<input  type="hidden" name="id" value="${agentInfo.id }"/>
                                <select  id="agentAddress" onchange="chg(this,'${ctx }');" name="agentAddress">
                                    <option value="-1">国家</option>
                                    <c:forEach items="${countryList}" var="c">
                                        <option value="${c.id}">${c.name}</option>
                                    </c:forEach>
                                </select>

                                <select  id="agentAddressProvince" onchange="chg(this,'${ctx }')" ; name="agentAddressProvince">
                                    <option value="-1">省（直辖市）</option>
                                </select>

                                <select  id="agentAddressCity" name="agentAddressCity">
                                    <option value="-1">市（区）</option>
                                </select>
                                <input id="road" type="text" class="input_width_250" name="agentAddressStreet" maxlength="100"/>
                                <span class="save_btn_left background_orange" onclick="save_select_input(this);saveInfo('agentAddress',this,'${ctx }')">保存</span>
                                </form>
                            </div>
                        </div>
                        <div class="channel_brand">
                            <p>
                                <span class="channel_phone_width">电话：</span>
                                <span class="channel_information_distance">${agentInfo.agentTelAreaCode} -${agentInfo.agentTel }</span>
                             <c:if test="${updateShow != 1 }">    
                            <span class="redact redact_use">
                            <a class="orange float_right" href="javascript:void(0);" onclick="informationSpread(this)">编辑 <em class="t1_2 copyReader"></em></a>
                            <a class="orange float_right hide" href="javascript:void(0);"
                               onclick="informationSpread(this)">收起 <em class="t1_2 pick_up"></em></a>
                            </span>
                            </c:if>
                            </p>

                            <div class="hide">
                            <form action="" method="post">
                            		<input  type="hidden" name="id" value="${agentInfo.id }"/>
                                  <span class="input ">
                                      <input type="text" class="min_input " name="agentTelAreaCode" maxlength="7" id="phone1"/>-
                                      <input type="text" class="min_right_input " name="agentTel" maxlength="13" id="phone2"/>
                                  </span>
                                <span class="save_btn_left background_orange" onclick="save_number(this);saveInfo('phone',this,'${ctx }')">保存</span>
                              </form>  
                            </div>
                        </div>
                        <div class="channel_brand">
                            <p>
                                <span class="channel_phone_width">传真：</span>
                                <span class="channel_information_distance">${agentInfo.agentFaxAreaCode}-${agentInfo.agentFax }</span>
                                 <c:if test="${updateShow != 1 }">
	                            <span class="redact redact_use">
		                            <a class="orange float_right" href="javascript:void(0);" onclick="informationSpread(this)">编辑
		                                <em class="t1_2 copyReader"></em></a>
		                            <a class="orange float_right hide" href="javascript:void(0);"
		                               onclick="informationSpread(this)">收起 <em class="t1_2 pick_up"></em></a>
		                         </span>
		                         </c:if>
                            </p>

                            <div class="hide">
                            	<form action="" method="post">
                            		<input  type="hidden" name="id" value="${agentInfo.id }"/>
                                <span class="input ">
                                     <input type="text" class="min_input " name="agentFaxAreaCode" maxlength="7" id="fax1"/>-
                                     <input type="text" class="min_right_input " name="agentFax" maxlength="13" id="fax2"/>
                                 </span>
                                <span class="save_btn_left background_orange" onclick="save_number(this);saveInfo('fax',this,'${ctx }')">保存</span>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div id="contact_information" style="overflow:hidden;">
                        <p class="channel_information_p">联系人信息
                        	<c:if test="${updateShow != 1 }">
                            <span onclick="new_contacts('${ctx}')"><em class="t1_2 linkman"></em>新建联系人</span>
                            </c:if>
                        </p>
                        <c:forEach items="${supplyContacts}" var="p">
                        <div class="contact_information">
                            <p class="head_background">
                                <span class="inline"><font>*</font>联系人：</span><span class="contact_information_name head_hide" id="contactName">${p.contactName}</span>
                                <input type="text" name="contactName" class="hide contact_information_input" maxlength="9"/>
                                <span class="inline"><font>*</font>手机号：</span><span class="head_hide inline" id="contactMobile">${p.contactMobile}</span>
                                <input type="text" name="contactMobile" class="hide contact_information_input"  maxlength="20"/>
                                <input type="hidden" name="contactId" value="${p.id}"/>
                                <span class="float_right">
                                    <span onclick="slide_contacts_open_close(this)" class="slide"><em class="t1_2 slide_open_close"></em></span>
                                </span>
                                 <c:if test="${updateShow != 1 }">
                                <span class="float_right hide delete_save">
                                	<span class="delete" onclick="delete_contacts(this, '${ctx}')" <c:if test="${fn:length(supplyContacts)<=1 }">style="display:none;"</c:if>><em class="t1_2 delete_t1_2"></em>删除</span>
                                    <span class="save hide" onclick="save_contacts('${ctx}', this);"><em class="t1_2 save_t1_2"></em>保存</span>
                                    <span class="compile" onclick="editor_contacts(this);"><em class="t1_2 compile_t1_2"></em>编辑</span>
                                </span>
                                </c:if>
                            </p>
                            <div class="hide">
                                <table class="channel_information_table">
                                    <tr>
                                        <td width="90">固定电话：</td>
                                        <td width="400">
                                            <span class="channel_phone">
                                                <span id="contactPhone"><c:if test="${p.contactPhone ne '-'}">${p.contactPhone}</c:if></span>
                                            </span>
                                            <span class="input">
                                                <%--需求调整，座机号码允许输入20位 2017/3/28 ymx Start--%>
                                                <input type="text" name="contactPhoneCode" class="min_input hide" maxlength="20"/><span style="display:none" class="gang1">-</span>
                                                <input type="text" name="contactPhone" class="min_right_input hide" maxlength="20"/>
                                                <%--需求调整，座机号码允许输入20位 2017/3/28 ymx End--%>
                                            </span>
                                        </td>
                                        <td  width="90">QQ：</td>
                                        <td  width="360">
                                            <span id="contactQQ">${p.contactQQ}</span>
                                            <input type="text" name="contactQQ" class="hide" maxlength="14"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>传真：</td>
                                        <td>
                                            <span class="channel_phone">
                                                <span id="contactFax"><c:if test="${p.contactFax ne '-'}">${p.contactFax}</c:if></span>
                                            </span>
                                            <span class="input">
                                                <input type="text" name="contactFaxCode" class="min_input hide" maxlength="6"/><span style="display:none" class="gang2">-</span>
                                                <input type="text" name="contactFax" class="min_right_input hide" maxlength="13"/>
                                            </span>
                                        </td>
                                        <td>电子邮箱：</td>
                                        <td>
                                            <span id="contactEmail" class="contactEmail" title="${p.contactEmail }">${p.contactEmail}</span>
                                            <input type="text" name="contactEmail" class="hide" maxlength="50"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>微信：</td>
                                        <td>
                                            <span id="wechatCode">${p.wechatCode}</span>
                                            <input type="text" name="wechatCode" class="hide" maxlength="20" style="width: 270px" value="${p.wechatCode}"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>描述：</td>
                                        <td>
                                            <span id="contactRemark" class="inline">${p.remarks}</span>
                                            <textarea name="contactRemark" cols="30" rows="10" class="contact_information_text hide" maxlength="200"></textarea>
                                        </td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                        </c:forEach>
                    </div>
                    <div><br/><br/></div>
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