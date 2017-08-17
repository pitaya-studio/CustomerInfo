<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta/>
<title>预定-单团填写下单信息</title>

<meta/>
<meta http-equiv="Cache-Control" content="no-store" /><meta http-equiv="Pragma" content="no-cache" /><meta http-equiv="Expires" content="0" />
<meta http-equiv="X-UA-Compatible" content="IE=7,IE=9,IE=10" />
<link href="${ctxStatic}/bootstrap/2.3.1/css_default/bootstrap.min.css" type="text/css" rel="stylesheet" />
<!--[if lte IE 6]><link href="${ctxStatic}/bootstrap/bsie/css/bootstrap-ie6.min.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/bootstrap/bsie/js/bootstrap-ie.min.js" type="text/javascript"></script><![endif]-->
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css" rel="stylesheet" type="text/css" />
<link href="${ctxStatic}/forTTS/css/jh-style.css?ver=1" type="text/css" rel="stylesheet" />
<link href="${ctxStatic}/forTTS/css/huanqiu-style.css" type="text/css" rel="stylesheet" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
<link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/common/mustache.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/trekiz.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/vendor.service_mode1.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/json/json2.js" type="text/javascript" ></script>

<!-- meta name="decorator" content="wholesaler" /> -->
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/manageorder.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/jquery.disabled.js" type="text/javascript"></script>
<style type="text/css">
input[disabled],select[disabled],textarea[disabled],input[readonly],select[readonly],textarea[readonly],
input[disabled]:focus,select[disabled]:focus,textarea[disabled]:focus,input[readonly]:focus, select[readonly]:focus,textarea[readonly]:focus{
    cursor:auto;
    background:transparent;
    border:0px;
    box-shadow:inset 0 0px 0px rgba(0,0,0,0.075)
}
</style>

<script type="text/javascript">

	$(function(){
		//各块信息展开与收起
		$(".ydClose").click(function(){
			var obj_this = $(this);
			if(obj_this.attr("class").match("ydExpand")) {
				obj_this.removeClass("ydExpand");
				obj_this.parent().nextAll("[flag=messageDiv]").eq(0).hide();
			} else {
				obj_this.addClass("ydExpand");
				obj_this.parent().nextAll("[flag=messageDiv]").eq(0).show();
			}
		});
		//证件类型
		$("input[name=papersType]").live("click",function(){			
			var $this = $(this);
			var $siblingsCkb = $this.parents(".tourist-ckb").children("input[type=checkbox]");
			var thisIndex = $siblingsCkb.index($this);
			var $tips = $this.parents(".tourist-info1").siblings(".zjlx-tips").eq(0);
			if($this.attr('checked')) {
				if(!$tips.is(":visible")) {
					$tips.show();
				}
				$tips.children("ul").eq(thisIndex).show();
			} else {
				$tips.children("ul").eq(thisIndex).hide(500,function() {
					var isshow = 0;
					$tips.children("ul").each(function(index, element) {
	                    if($(element).is(":visible")){
							isshow++;
						}
	                });
					if(0 == isshow) {
						$tips.hide();
					}
				});
				
			}
		});
		
		$("input[name='tempOfPapersType']").each(function(index, obj) {
			var papersType = $(this).val();
			$("[name='papersType']", $(this).parent()).each(function() {
				if(papersType && papersType.indexOf($(this).val()) != -1) {
					$(this).attr("checked", true);
					$(this).trigger("click").trigger("click");
				}
			});
			var idCard = $(this).next("input").val();
			if(idCard) {
				var value = idCard.split(",");
				$("input[name='idCard']:visible", $(this).parent().parent().next("div")).each(function(index, obj) {
					$(this).val(value[index]);
				});
			}
		});
	});

	//得到焦点事件：隐藏填写费用名称提示
	function payforotherIn(doc) {
	    var obj = $(doc);
	    obj.siblings(".ipt-tips2").hide();
	}
	
	//失去焦点事件：如果输入框中没有值，则提示填写费用名称
	function payforotherOut(doc){
	    var obj = $(doc);
	    if(!obj.val()){
	        obj.siblings(".ipt-tips2").show();
	    }
	}
	
	//点击提示错误信息中 "修改" 后错误输入框得到焦点
	function focusIpt(doc){
	    $(doc).parent().find('input[type=text].ipt2').trigger("focus");
	}

</script>		
	
	


<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript">
$(function(){
	//预定第二步是否联运
    ydbz2interradio();
	//预定第二步自备签
	ydbz2zibeiqian();
	//文本框提示信息显示隐藏
	$("input[flag=istips]").live("focusin",function(){
		var obj_this = $(this);
		obj_this.next("span").hide();
	})
	$("input[flag=istips]").live("focusout",function(){
		var obj_this = $(this);
		if(obj_this.val()!=""){
			obj_this.next("span").hide();
		}else{
			obj_this.next("span").show();
		}
		})
	if($("input[flag=istips]").val()!=""){
		$("input[flag=istips]").next("span").hide();
	}
	$(".ipt-tips").live("click",function(){
		var obj_this = $(this);
		obj_this.prev("input").focus();
	})
	
	

	});

</script>

</head>

<body>
<div id="sea">
  <header>
		<div class="hedear">
      		<div class="hedear-left">
                <div class="hedear-logo"></div>
                <div class="clear"></div>
      		</div>
            <div class="hedear-right">
                <ul class="hedear-nav">
                    <li class="head-home"><a href="#">后台首页</a></li>
                    <li class="head-logout"><a href="#">退出</a></li>
                    <div class="clear"></div>
                </ul>
				<p class="header-user"><em>王涛</em>，您好，登录时间 <em>2014-5-12 12:36:52</em><span class="header-userspan">
			接口销售：郭晓勤</span><span>销售人电话：13666984589</span></p>
            </div>
      		<div class="clear"></div>
		</div>
    </header>
  <div class="main">
    <div class="main-left">
            <h2 class="mainMenu mainMenu-on"><span class="iconMenu iconMenu-10"></span>询价<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul" style="display:block;">
                <li class="mainMenu-ul-on"><a href="#" target="_self">询价记录</a></li>
            </ul>
            <h2 class="mainMenu"><span class="iconMenu iconMenu-1"></span>预定<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">单团</a></li>
                <li><a href="#" target="_self">散拼</a></li>
                <li><a href="#" target="_self">游学</a></li>
                <li><a href="#" target="_self">大客户</a></li>
                <li><a href="#" target="_self">自由行</a></li>
                <li><a href="#" target="_self">签证</a></li>
                <li><a href="#" target="_self">机票</a></li>
            </ul>
            <!--订单开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-2"></span>订单<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li><a href="#" target="_self">全部</a></li>
                <li class="mainMenu-ul-on"><a href="#" target="_self">已支付订单</a></li>
                <li><a href="#" target="_self">未支付订单</a></li>
                <li><a href="#" target="_self">已占位</a></li>
                <li><a href="#" target="_self">已取消</a></li>
            </ul>
            <!--订单结束-->
            <!--渠道商开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-3"></span>渠道商<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">渠道商查询</a></li>
                <li><a href="#" target="_self">渠道商添加</a></li>
                <li><a href="#" target="_self">定价策略查询</a></li>
                <li><a href="#" target="_self">定价策略添加</a></li>
            </ul>
            <!--渠道商结束-->
            <!--财务(渠)开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-4"></span>财务(渠)<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self"><!--渠道商-->发票申请</a></li>
                <li><a href="#" target="_self"><!--渠道商-->发票审核记录查询</a></li>
            </ul>
            <!--财务(渠)结束-->
            <!--财务(供)开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-4"></span>财务(供)<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">发票管理</a></li>
                <li><a href="#" target="_self">收益管理</a></li>
            </ul>
            <!--财务(供)结束-->
            <!--产品开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-5"></span>产品<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">已上架产品</a></li>
                <li><a href="#" target="_self">草稿中产品</a></li>
                <li><a href="#" target="_self">已下架产品</a></li>
                <li><a href="#" target="_self">发布新产品</a></li>
            </ul>
            <!--产品结束-->
            <!--运控开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-6"></span>运控<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">库存查询</a></li>
            </ul>
            <!--运控结束-->
            <!--系统设置开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-7"></span>系统设置<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">个人信息</a></li>
                <li><a href="#" target="_self">修改密码</a></li>
                <li><a href="#" target="_self">账号管理</a></li>
                <li><a href="#" target="_self">账号添加</a></li>
            </ul>
            <!--系统设置结束-->
            <!--基础信息开始-->
            <h2 class="mainMenu"><span class="iconMenu iconMenu-8"></span>基础信息<i class="iconMenu-arrow"></i></h2>
            <ul class="mainMenu-ul">
                <li class="mainMenu-ul-on"><a href="#" target="_self">旅游类型</a></li>
                <li><a href="#" target="_self">产品系列</a></li>
                <li><a href="#" target="_self">产品类型</a></li>
                <li><a href="#" target="_self">交通方式</a></li>
                <li><a href="#" target="_self">出发城市</a></li>
                <li><a href="#" target="_self">目的地区域</a></li>
                <li><a href="#" target="_self">航空公司</a></li>
            </ul>
            <!--基础信息结束-->
            <!--模块说明开始-->
            <h2 class="mainMenu mainMenu-last"><span class="iconMenu iconMenu-9"></span>模块说明<!--<i class="iconMenu-arrow"></i>--></h2>
            <!--模块说明结束-->
		</div>
    <div class="main-right">
       

<input type="hidden" value="0" id="leftpayReservePosition">
<input type="hidden" value="0" id="placeHolderType">
<input type="hidden" value="1172" id="productId">
<input type="hidden" value="2" id="payStatus">
<input type="hidden" value="1" id="payMode">
<input type="hidden" value="6" id="remainDays">
<input type="hidden" value="2451" id="productGroupId">
<input type="hidden" value="60" id="payDeposit">
<input type="hidden" value="" id="frontMoney">
<input type="hidden" value="1800" id="etj">
<input type="hidden" value="1600" id="crj">
<input type="hidden" value="2000" id="tsj">
<input type="hidden" value="0" id="intermodalType">
<input type="hidden" value="80" id="singleDiff">
<input id="orderid" type="hidden" value="">
<input id="freePosition" type="hidden" value="100">
<input id="orderPosition" type="hidden" value="0">
<input id="agentId" type="hidden" value="-1">

<!-- 预订单位名称和id -->
<input id="orderCompanyName" type="hidden" value="非签约渠道">
<input id="orderCompany" type="hidden" value="-1">

<!-- 游客模板 -->

<div id="travelerTemplate" style="display: none;">
<form>
	<div class="tourist">
		<div class="tourist-t">
			<a class="btn-del" style="cursor:pointer;" name="deleteTraveler" >删除</a>
			<input type="hidden" name ="id" value=""  class="traveler" >
			<span class="add_seachcheck" onclick="boxCloseOnAdd(this,'1')"></span><label class="ydLable">游客<em class="travelerIndex"></em>:</label>
            <div class="tourist-t-off">张文博<span class="fr">结算价：<span class="gray14">￥</span><span class="ydFont2">1,600</span></span></div>
			<div class="tourist-t-on">
            <label><input type="radio" class="traveler" name="personTypeinner" value="1" checked="checked"/>成人</label>
			<label><input type="radio" class="traveler" name="personTypeinner" value="2" />儿童</label>
			<label><input type="radio" class="traveler" name="personTypeinner" value="3" />特殊人群</label>
           <div class="tourist-t-r"> 是否联运：<label><input type="radio" class="ydbz2intermodal1" name="ydbz2intermodalType" value="1" checked="checked" onclick="ydbz2intermodal(this)"/>不需要</label>
			<label><input type="radio" name="ydbz2intermodalType" value="2" onclick="ydbz2intermodal(this)"/>需要</label>
            <span><select onchange="ydbz2interselect(this)" name="">
                        <option value="2000">全国联运</option>
                        <option value="3000">河北联运</option>
                    </select>
                    联运价格：￥<em>2000</em></span></div></div>
		</div>
		
		<div class="tourist-con" flag="messageDiv">
			<!--游客信息左侧开始-->
			<div class="tourist-left">
            <div class="ydbz_tit ydbz_tit_child"><span class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></span>基本信息</div>
				<ul class="tourist-info1 clearfix" flag="messageDiv">
	                <li>
	                	<label class="ydLable"><span class="xing">*</span>姓名：</label>
	                	<input type="text" maxlength="30" name="travelerName" class="traveler" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()">
	                </li>
	                
	                <li>
	                 	<label class="ydLable">英文／拼音：</label><input type="text" maxlength="30" name="travelerPinyin" class="traveler traveler2">
	               	</li>
	               	
	               	<li>
	                    <label class="ydLable"><span class="xing">*</span>性别：</label>
	                    <select name="travelerSex" class="selSex">
							<option value="1" selected="selected">男</option>
							<option value="2" >女</option>
	                    </select> 
	                </li>
	                
	                <li>
	                    <label class="ydLable">国籍：</label>
	                    <select name="nationality" class="selCountry">
							
							    <option value="21" 
							>阿联酋</option>
							
							    <option value="41" 
							>阿尔及利亚</option>
							
							    <option value="51" 
							>阿塞拜疆</option>
							
							    <option value="61" 
							>阿尔巴尼亚</option>
							
							    <option value="71" 
							>亚美尼亚</option>
							
							    <option value="111" 
							>阿根廷</option>
							
							    <option value="121" 
							>澳大利亚</option>
							
							    <option value="141" 
							>奥地利</option>
							
							    <option value="171" 
							>南极</option>
							
							    <option value="181" 
							>巴林</option>
							
							    <option value="201" 
							>博茨瓦纳</option>
							
							    <option value="211" 
							>百慕达</option>
							
							    <option value="221" 
							>比利时</option>
							
							    <option value="231" 
							>巴哈马</option>
							
							    <option value="241" 
							>孟加拉</option>
							
							    <option value="261" 
							>波斯尼亚和黑塞哥维那</option>
							
							    <option value="271" 
							>玻利维亚</option>
							
							    <option value="281" 
							>缅甸</option>
							
							    <option value="301" 
							>白俄罗斯</option>
							
							    <option value="331" 
							>巴西</option>
							
							    <option value="351" 
							>不丹</option>
							
							    <option value="361" 
							>保加利亚</option>
							
							    <option value="381" 
							>文莱</option>
							
							    <option value="401" 
							>加拿大</option>
							
							    <option value="411" 
							>柬埔寨</option>
							
							    <option value="431" 
							>斯里兰卡</option>
							
							    <option value="441" 
							>刚果</option>
							
							    <option value="451" 
							>刚果民主共和国</option>
							
							    <option value="461" 
							selected="selected">中国</option>
							
							    <option value="471" 
							>智利</option>
							
							    <option value="501" 
							>喀麦隆</option>
							
							    <option value="521" 
							>哥伦比亚</option>
							
							    <option value="551" 
							>哥斯达黎加</option>
							
							    <option value="571" 
							>古巴</option>
							
							    <option value="581" 
							>佛得角</option>
							
							    <option value="591" 
							>库克群岛</option>
							
							    <option value="601" 
							>塞浦路斯</option>
							
							    <option value="611" 
							>丹麦</option>
							
							    <option value="671" 
							>厄瓜多尔</option>
							
							    <option value="681" 
							>埃及</option>
							
							    <option value="691" 
							>爱尔兰</option>
							
							    <option value="711" 
							>爱沙尼亚</option>
							
							    <option value="741" 
							>埃塞俄比亚</option>
							
							    <option value="761" 
							>捷克共和国</option>
							
							    <option value="781" 
							>芬兰</option>
							
							    <option value="791" 
							>斐济</option>
							
							    <option value="831" 
							>法属波利尼西亚</option>
							
							    <option value="851" 
							>法国</option>
							
							    <option value="891" 
							>格鲁吉亚</option>
							
							    <option value="901" 
							>加纳</option>
							
							    <option value="941" 
							>格陵兰岛</option>
							
							    <option value="951" 
							>德国</option>
							
							    <option value="981" 
							>关岛</option>
							
							    <option value="991" 
							>希腊</option>
							
							    <option value="1041" 
							>海地</option>
							
							    <option value="1051" 
							>香港</option>
							
							    <option value="1091" 
							>克罗地亚</option>
							
							    <option value="1101" 
							>匈牙利</option>
							
							    <option value="1111" 
							>冰岛</option>
							
							    <option value="1121" 
							>印尼</option>
							
							    <option value="1131" 
							>马恩岛</option>
							
							    <option value="1141" 
							>印度</option>
							
							    <option value="1171" 
							>伊朗</option>
							
							    <option value="1181" 
							>以色列</option>
							
							    <option value="1191" 
							>意大利</option>
							
							    <option value="1221" 
							>日本</option>
							
							    <option value="1241" 
							>牙买加</option>
							
							    <option value="1261" 
							>约旦</option>
							
							    <option value="1291" 
							>肯尼亚</option>
							
							    <option value="1301" 
							>吉尔吉斯斯坦</option>
							
							    <option value="1311" 
							>韩国</option>
							
							    <option value="1361" 
							>科威特</option>
							
							    <option value="1381" 
							>哈萨克斯坦</option>
							
							    <option value="1391" 
							>老挝</option>
							
							    <option value="1411" 
							>拉脱维亚</option>
							
							    <option value="1421" 
							>立陶宛</option>
							
							    <option value="1431" 
							>利比里亚</option>
							
							    <option value="1441" 
							>斯洛伐克</option>
							
							    <option value="1461" 
							>列支敦士登</option>
							
							    <option value="1481" 
							>卢森堡</option>
							
							    <option value="1501" 
							>马达加斯加</option>
							
							    <option value="1511" 
							>马提尼克岛</option>
							
							    <option value="1521" 
							>澳门</option>
							
							    <option value="1551" 
							>蒙古</option>
							
							    <option value="1581" 
							>黑山</option>
							
							    <option value="1591" 
							>前南斯拉夫的马其顿共和国</option>
							
							    <option value="1611" 
							>摩纳哥</option>
							
							    <option value="1621" 
							>摩洛哥</option>
							
							    <option value="1631" 
							>毛里求斯</option>
							
							    <option value="1651" 
							>毛里塔尼亚</option>
							
							    <option value="1681" 
							>马尔代夫</option>
							
							    <option value="1691" 
							>墨西哥</option>
							
							    <option value="1701" 
							>马来西亚</option>
							
							    <option value="1711" 
							>莫桑比克</option>
							
							    <option value="1771" 
							>尼日利亚</option>
							
							    <option value="1781" 
							>荷兰</option>
							
							    <option value="1801" 
							>挪威</option>
							
							    <option value="1811" 
							>尼泊尔</option>
							
							    <option value="1821" 
							>瑙鲁</option>
							
							    <option value="1861" 
							>新西兰</option>
							
							    <option value="1871" 
							>巴拉圭</option>
							
							    <option value="1891" 
							>秘鲁</option>
							
							    <option value="1911" 
							>南沙</option>
							
							    <option value="1921" 
							>巴基斯坦</option>
							
							    <option value="1931" 
							>波兰</option>
							
							    <option value="1941" 
							>巴拿马</option>
							
							    <option value="1951" 
							>葡萄牙</option>
							
							    <option value="1961" 
							>巴布亚新几内亚</option>
							
							    <option value="1971" 
							>帕劳</option>
							
							    <option value="1991" 
							>卡塔尔</option>
							
							    <option value="2011" 
							>塞尔维亚</option>
							
							    <option value="2041" 
							>罗马尼亚</option>
							
							    <option value="2051" 
							>菲律宾</option>
							
							    <option value="2061" 
							>波多黎各</option>
							
							    <option value="2071" 
							>俄罗斯</option>
							
							    <option value="2091" 
							>沙特阿拉伯</option>
							
							    <option value="2121" 
							>塞舌尔</option>
							
							    <option value="2131" 
							>南非</option>
							
							    <option value="2141" 
							>塞内加尔</option>
							
							    <option value="2161" 
							>斯洛文尼亚</option>
							
							    <option value="2181" 
							>圣马力诺</option>
							
							    <option value="2191" 
							>新加坡</option>
							
							    <option value="2211" 
							>西班牙</option>
							
							    <option value="2251" 
							>瑞典</option>
							
							    <option value="2271" 
							>阿拉伯叙利亚共和国</option>
							
							    <option value="2281" 
							>瑞士</option>
							
							    <option value="2311" 
							>泰国</option>
							
							    <option value="2321" 
							>塔吉克斯坦</option>
							
							    <option value="2351" 
							>汤加</option>
							
							    <option value="2381" 
							>突尼斯</option>
							
							    <option value="2391" 
							>东帝汶</option>
							
							    <option value="2401" 
							>土耳其</option>
							
							    <option value="2431" 
							>土库曼斯坦</option>
							
							    <option value="2441" 
							>坦桑尼亚</option>
							
							    <option value="2461" 
							>英国</option>
							
							    <option value="2471" 
							>乌克兰</option>
							
							    <option value="2481" 
							>美国</option>
							
							    <option value="2501" 
							>乌拉圭</option>
							
							    <option value="2511" 
							>乌兹别克斯坦</option>
							
							    <option value="2531" 
							>委内瑞拉</option>
							
							    <option value="2551" 
							>越南</option>
							
							    <option value="2571" 
							>梵蒂冈</option>
							
							    <option value="2581" 
							>纳米比亚</option>
							
							    <option value="2641" 
							>斯威士兰</option>
							
							    <option value="2671" 
							>赞比亚</option>
							
							    <option value="2681" 
							>津巴布韦</option>
							
							    <option value="2731" 
							>马耳他</option>
							
							    <option value="2771" 
							>台湾</option>
							
							    <option value="2832" 
							>朝鲜</option>
							
	                   	</select>
	               	</li>
	               	
	                <li>
	                	<label class="ydLable">出生日期：</label><input type="text" maxlength="" name="" class="traveler traveler2 dateinput">
	                </li>
	                
	               	<li>
	               		<label class="ydLable"><span class="xing"></span>联系电话：</label>
	               		<input type="text" name="telephone" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
	                </li>
	                <li>
	               		<label class="ydLable"><span class="xing">*</span>护照号：</label>
	               		<input type="text" name="telephone" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
	                </li>
                    <li>
	               		<label class="ydLable"><span class="xing">*</span>护照有效期：</label><input type="text" maxlength="" name="" class="traveler traveler2 dateinput">
	                </li>
                    <li>
	               		<label class="ydLable"><span class="xing"></span>身份证号：</label>
	               		<input type="text" name="telephone" class="traveler" maxlength="50" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" >
	                </li>	                
                </ul>
                <div class="ydbz_tit ydbz_tit_child"><span class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></span>签证信息</div>
                <ul flag="messageDiv"><div class="ydbz_tit ydbz_tit_child">自备签 <p class="tourist-ckb">
						<label class="ydLable">自备签国家：</label>
						<input type="checkbox" class="traveler" value="1" name="zibeiqian"><label class="ckb-txt">自备美签</label>
						<input type="checkbox" class="traveler" value="2" name="zibeiqian"><label class="ckb-txt">自备加签</label>
					</p></div>
               
                    <div class="zjlx-tips  zibei-tips" style="display: none;">
					<i class="arrow1">&nbsp;</i>
					<ul class="tourist-type clearfix" style="display: none;">
						<li>
							<label class="ydLable">美国签证有效期：</label>
							<input type="text" class="traveler2 dateinput" name="">
						</li>
					</ul>
				   
				    <ul class="tourist-type clearfix" style="display: none;">
				        <li>
					        <label class="ydLable">加拿大签证有效期：</label>
					        <input type="text" class="traveler2 dateinput" name="idCard">
				        </li>
				    </ul>
				    
				</div>
                    <div class="ydbz_tit ydbz_tit_child">申请办签
                    
                    </div><ul class="ydbz_2uploadfile"><li class="seach25 seach33"><p class="seachlongp">预计出团日期：</p>2014-08-08</li><li class="seach25 seach33"><p>签证类别：</p><select>
          <option value="个签">个签</option>
          <option value="探亲">探亲</option>
          <option value="照会">照会</option>
          <option value="邀请">邀请</option>
          <option value="照会+邀请">照会+邀请</option>
          <option value="团签">团签</option>
          <option value="其他" selected="">其他</option>
        </select></li><li class="seach25 seach33"><p class="seachlongp">填写其他签证类别：</p><input type="text"></li>
              <p class="kong"></p></ul></ul>
                <div class="ydbz_tit ydbz_tit_child"><span class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></span>上传资料</div>
                <ul flag="messageDiv" class="ydbz_2uploadfile"><li class="seach25 seach33"><p>护照首页：</p><p class="seach_r">
    					<input type="file" onchange="ydbz2interfile(this)" class="ydbz_2filebtn"><input type="button" class="btn btn-primary" value="上传"><span class="fileLogo"></span></p></li><li class="seach25 seach33"><p>身份证正面：</p><input type="file" onchange="ydbz2interfile(this)" class="ydbz_2filebtn"><input type="button" class="btn btn-primary" value="上传"><span class="fileLogo"></span></li><li class="seach25 seach33"><p>报名表：</p><input type="file" onchange="ydbz2interfile(this)" class="ydbz_2filebtn"><input type="button" class="btn btn-primary" value="上传"><span class="fileLogo"></span></li>
               <p class="kong"></p><li class="seach25 seach33"><p>照片：</p><input type="file" onchange="ydbz2interfile(this)" class="ydbz_2filebtn"><input type="button" class="btn btn-primary" value="上传"><span class="fileLogo"></span></li><li class="seach25 seach33"><p>身份证反面：</p><input type="file" onchange="ydbz2interfile(this)" class="ydbz_2filebtn"><input type="button" class="btn btn-primary" value="上传"><span class="fileLogo"></span></li><li class="seach25 seach33"><p>其它：</p><input type="file" onchange="ydbz2interfile(this)" class="ydbz_2filebtn"><input type="button" class="btn btn-primary" value="上传"><span class="fileLogo"></span></li> <p class="kong"></p></ul>
                <div class="ydbz_tit ydbz_tit_child"><span class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></span>办证资料</div>
                <ul flag="messageDiv" class="seach25 seach100 ydbz_2uploadfile"><p>原件：</p>护照已领取、房产证未领取<br/><p>复印件：</p>户口本未领取、房产证未领取</ul>
                <div class="ydbz_tit ydbz_tit_child">　　备注：<textarea class="textarea_long"></textarea></div>
                <ul><span class="ydbz_2uploadfile_span"></span></ul>
                <div class="zjlx-tips">
					<i class="arrow1">&nbsp;</i>
					<ul class="tourist-type clearfix">
						<li>
							<label class="ydLable">身份证号码：</label>
							<input type="text" name="idCard" style="width: 150px" class="traveler" maxlength="30" onkeyup="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')" >
						</li>
					</ul>
				    <ul class="tourist-type clearfix">
				        <li>
				        	<label class="ydLable">护照号码：</label>
				        	<input type="text" name="idCard" class="traveler" maxlength="30" onkeyup="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')" >
				        </li>
				        <li><label class="ydLable">发证日期：</label><input type="text" name="issuePlace" class="traveler"></li>
				        <li><label class="ydLable">有效期：</label><input type="text" name="validityDate" class="traveler"></li>
				    </ul>
				    <ul class="tourist-type clearfix">
				        <li>
					        <label class="ydLable">警官证号码：</label>
					        <input type="text" name="idCard" class="traveler" maxlength="30" onkeyup="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')" >
				        </li>
				    </ul>
				    <ul class="tourist-type clearfix">
				        <li>
				        	<label class="ydLable">军官证号码：</label>
				        	<input type="text" name="idCard" class="traveler" maxlength="30" onkeyup="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')" >
				        </li>
				    </ul>
				    <ul class="tourist-type clearfix">
				        <li>
				        	<label class="ydLable">其他证件号码：</label>
				        	<input type="text" name="idCard" class="traveler" maxlength="30" onkeyup="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-a-zA-Z]/g,'')" >
				        </li>
				    </ul>
				</div>
			</div>
            <!--游客信息左侧结束-->
            
            <!--游客信息右侧开始-->
			<div class="tourist-right">
			    <div class="clearfix">
			        <ul class="tourist-info2">
			            <li class="tourist-info2-first">
			                <label class="ydLable2 ydColor1">住房要求：</label>
			                <select class="selZF" name="hotelDemand" >
			                    <option value="1" >单人间</option>
			                    <option value="2" selected="selected">双人间</option>
			                </select>
			            </li>
			            <li><label class="ydLable2">单人房差：</label>¥<span>0</span>/间 x<input type="text" class="ipt4">晚<input type="hidden" name="singleDiff"  class="traveler" value="0" ></li>
                         <li><label class="ydLable2">单房差小计：</label>¥<span>0</span></li>
			            <li><label class="ydLable2">单价：</label>¥<span class="ydFont1">1,600</span>
			                <input type="hidden" readonly name="srcPrice"  class="traveler" value="1600" >
			            </li>
			        </ul>
			    </div>
			    <div class="yd-line"></div>
			    <div class="clearfix">
			        <a name="addcost" class="btn-addBlue"
			            >添加其他费用</a>
			    
			        <div class="payfor-otherDiv"></div>
			    </div>
			    <div class="yd-line"></div>
                <div class="yd-total fr"><a class="ydbz_x">应用全部</a></div>
			    <div class="yd-total clearfix"><div class="fr"><label class="ydLable2">结算价：</label><span class="gray14">￥</span><span class="ydFont2">1,600</span>
			    <input type="hidden" name="payPrice" class="traveler" value="1600" >
			    </div></div>
			</div>
			<!--游客信息右侧结束-->
		</div>
	</div>
</form>
</div>

<ul class="nav nav-tabs" >
	
	 
		<li class="active"><a href="#">预定</a></li>
	
</ul>
  <div class="bgMainRight">  </div> 
<div class="ydbzbox fs">
<div class="ydbz yd-step1 " id="stepbar" >&nbsp;</div>
  <div class="ydbz_tit">订单基本信息</div>
  
  <p class="ydbz_mc">JHGJLXS5535--预报名--001</p>
  <ul class="ydbz_info">
                    <li>
                        <span>团号：</span>
                        <em class="fArial">
                            4324234DF</em>
                    </li>
                     <li><span>出团日期：</span>2014-11-20</li>
                    <li><span>可报名人数：</span>
                            

                            100</li>
                            <li><span>出发城市：</span>北京</li>
                            <li><span>行程天数：</span>6天</li>
                            <li><span>游客出发区域：</span>
                        
                            
                            
                                北京
                            
                        
                    </li>
                           
<!--
                            <li>
                        <span>产品编号：</span>
                        <em class="fArial">
                             
                                  
                                  
                                    JHGJLXS5535 
                                 
                            
                        </em>
                    </li>
                    
                   
                    <li><span>产品系列：</span>大众系列</li>
                    
                    
                    
                    <li>
                        <span style="width:171px;" >创建时间（补单时填写）：</span>
                        <input id="createDate" class="inputTxt dateinput" name="createDate" type="text" onclick="WdatePicker()" >
                    </li>-->
                    
 <li style="width:100%;"id="mddtargetAreaNames" title="苏州,南京"><span >目的地：</span>苏州,南京</li>
                 </ul>
                 
                 <form id="productOrderTotal">
                     <ul class="ydbz_dj specialPrice">
                     <li style="display: none;">
                     <input type="text" id="orderPersonelNum" onkeyup="this.value=this.value.replace(/\D/g,'')" 
            			onafterpaste="this.value=this.value.replace(/\D/g,'')" value="0" 
            			class="required" >
                     
                     </li>
                        <li><span class="ydtips">单价</span>
                           <p>成人：<font color="#FF0000">¥1,600</font> 元</p>
                           <p>儿童：<font color="#FF0000">¥1,800</font> 元</p>
                           <p>特殊人群：<font color="#FF0000">¥2,000</font> 元</p>
                        </li>
                        <li>
                        	<span class="ydtips"><span class="xing">*</span>出行人数</span>
                           	<p>成人：
                           		<input type="text" id="orderPersonNumAdult"  
                           		onkeyup="this.value=this.value.replace(/\D/g,'')"
								onafterpaste="this.value=this.value.replace(/\D/g,'')" 
            					value="0" 
            					class="required" 
            					> 人
            				</p>
                           <p>儿童：
                           		<input type="text" id="orderPersonNumChild" 
                           		onkeyup="this.value=this.value.replace(/\D/g,'')" 
            					onafterpaste="this.value=this.value.replace(/\D/g,'')" value="0" 
            					class="required" 
            					> 人
            				</p>
            				<p>特殊人群：
                           		<input type="text" id="orderPersonNumSpecial" 
                           		onkeyup="this.value=this.value.replace(/\D/g,'')" 
            					onafterpaste="this.value=this.value.replace(/\D/g,'')" value="0" 
            					class="required" 
            					> 人
            				</p>
                        </li>
                     </ul>
                 </form>
                 
                    <div class="ydbz_tit"><span class="ydExpand closeOrExpand"></span>填写预订人信息</div>
                    <div flag="messageDiv">
                         <form id="orderpersonMesdtail">
                         <p class="ydbz_qdmc">预订渠道：<select id="agentIdIn" name="agentIdIn">
	                    <option value="">不限</option>
	                    <option value="-1">非签约渠道</option>		                
		                	<option value="">思锐创途</option>		                
		                	<option value="">渠道商江河</option>		                
		                	<option value="">渠道商北中广</option>		                
		                	<option value="">江河国际旅行社</option>		                
		                	<option value="">渠道商江河</option>		                
		                	<option value="">渠道商锘劲乐团</option
		                	><option value="114">渠道商锘劲乐团</option>		                
	                </select>
                         
                         </p>
                         <ul class="ydbz_qd" id="orderpersonMes">
                            <li>
                              <label><span class="xing">*</span>渠道联系人：</label>
                               
                              <input maxlength="10" type="text" id="orderPersonName" value="" class="required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()"/>
                            </li>
                            <li>
                               <label><span class="xing">*</span>渠道联系人电话：</label>
                               <input maxlength="15" type="text" id="orderPersonPhoneNum" value="" class="required" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" />
                            </li><li>
                               <label>固定电话：</label>
                               <input maxlength="15" type="text" class="" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')" /><span class="ydbz_x yd1AddPeople" onclick="yd1AddPeople(this)">添加</span>
                            </li>
                 
                            
                         </ul>
                        </form>
                    </div>
					<div class="ydBtn-down orderPersonMsg" style="display:none;" ><span>&nbsp;</span></div>
                 
  
<div class="ydbz_sxb" id="oneToSecondOutStepDiv" ><div class="ydBtn" id="oneToSecondStepDiv"><span class="ydbz_x" >下一步</span></div></div>

  <div id="manageOrder_m"  style='display:none;' >
  <div id="contact">
  <div class="ydbz_tit"> <span class="ydExpand closeOrExpand"></span> 特殊需求</div>
                 <div class="ydbz2_lxr" flag="messageDiv">
                    <form class="contactTable">
                                <p>
                                   <label style="vertical-align:top">特殊需求：</label><input type="hidden" name ="id" value="">
                                   <textarea name="remark"></textarea>
                                </p>
                    </form>
                 </div>
                 <div class="ydBtn-down orderPersonMsg" style="display:none;" ><span>&nbsp;</span></div>
  </div>
  </div>

  <div  id="manageOrder_new"  style='display:none;' >
  <div class="ydbz_tit">请填写游客信息</div>
  
 <div class="warningtravelerNum">
                    
                                暂无游客信息
                    
                  </div>
    
     <div id="traveler">
      
    </div>
  
    <!--添加游客按钮开始-->
    <div class="touristBtn"><a class="btn-addGrey" id="addTraveler">添加游客</a></div>
    <!--添加游客按钮结束-->
  
    <div class="ydbz_sxb" id="secondDiv"  style='display:none;' >
        <div class="ydBtn ydBtn2">
            <a class="ydbz_s" id="secondToOneStepDiv" >上一步</a>
            <a class="ydbz_x" id="secondToThirdStepDiv">下一步</a>
        </div>
    </div>
    
    
    <div class="ydbz_sxb" id="thirdDiv"  style='display:none;'>
        <div class="ydBtn ydBtn2">
            <div class="ydbz_s" id="thirdToSecondTStepDiv" >上一步</div>
                
                   <c:if test="${productorder.payStatus !=99 }">
                    <div class="ydbz_x" id="thirdToFourthStepDiv" >
                        <c:if test="${productorder.payStatus==3}">
                                                   保存
                        </c:if>
                      <c:if test="${productorder.payStatus!=3}">
                            <c:if test="${(productorder.payStatus!=5) || (productorder.totalMoney ne productorder.payedMoney)}">
                                                 保存并支付
                            </c:if>
                            <c:if test="${(productorder.payStatus==5) && (productorder.totalMoney eq productorder.payedMoney)}">
                                                                    保存
                            </c:if>
                        </c:if>
                    </div>
               </c:if>
               
        </div>
    </div>
    
    
        <div style="text-align:right; font-size:12px; margin-top:20px;  padding-top:10px; padding-right:10px;">
        	<b style="font-size:18px">订单总额：</b><span class="gray14">￥</span><span id="travelerSumPrice" class="tdred f20"></span>
        </div>
    
    
    
    
  </div>
  
  </div>

	   <!--<div class="bs-footer">
		    <div style="float:left; margin-top:10px;"><img src="/images/jishuzhichi-trekiz.png" /></div>
		    <div style="float:left; margin-left:30px; padding-left:30px; border-left:1px #e1e1e1 solid">思锐创途网络技术（北京）有限公司<br />
		    公司电话：010-85711691 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;客服电话：400-018-5090&nbsp;&nbsp;&nbsp;传真：010-85711891 &nbsp;&nbsp;&nbsp;技术支持：思锐创途&nbsp;&nbsp;&nbsp;技术支持电话：010-85711691-8006
		    </div>
		    <div class="clear"></div>
	   </div>--> 
      
    </div>
 </div>
     <!--footer-->
    <div class="bs-footer">
        <p>客服电话：010-85718666  <br/>Copyright © 2012-${fns:getConfig('copyrightYear')} 接待社交易管理后台</p>
        <div class="footer-by">Powered By Trekiz Technology</div>
    </div>
    <!--footer***end-->
</div>
<script type="text/javascript">
$(document).ready(function(e) {
    var leftmenuid = $("#leftmenuid").val();
    $(".main-nav").find("li").each(function(index, element) {
        if($(this).attr("menuid")==leftmenuid){
            $(this).css("background", "url(/resource/images/img/pfs_nav_hover.png)");
        }
    });
});
$(function(){
    $('.closeNotice').click(function(){
        var par = $(this).parent().parent();
        par.hide();
        par.prev().removeClass('border-bottom');
        par.prev().find('.notice-date').show();
    });
    $('.showNotice').click(function(){
        $(this).parent().hide();
        var par = $(this).parent().parent();
        par.addClass('border-bottom');
        par.next().show();
    });
});
$(function(){
	$('.main-nav li').click(function(){
		$(this).addClass('select').siblings().removeClass('select');
	})
})

String.prototype.formatNumberMoney= function(pattern){
	  var strarr = this?this.toString().split('.'):['0'];   
	  var fmtarr = pattern?pattern.split('.'):[''];   
	  var retstr='';   
	  var str = strarr[0];
	  var fmt = fmtarr[0];
	  var i = str.length-1;     
	  var comma = false;   
	  for(var f=fmt.length-1;f>=0;f--){   
	    switch(fmt.substr(f,1)){   
	      case '#':   
	        if(i>=0 ) retstr = str.substr(i--,1) + retstr;   
	        break;   
	      case '0':   
	        if(i>=0) retstr = str.substr(i--,1) + retstr;   
	        else retstr = '0' + retstr;   
	        break;   
	      case ',':   
	         comma = true;   
	         retstr=','+retstr;   
	        break;   
	     }   
	   }   
	  if(i>=0){   
	    if(comma){   
	      var l = str.length;   
	      for(;i>=0;i--){   
	         retstr = str.substr(i,1) + retstr;   
	        if(i>0 && ((l-i)%3)==0) retstr = ',' + retstr;   
	       }   
	     }   
	    else retstr = str.substr(0,i+1) + retstr;   
	   }   
	  
	   retstr = retstr+'.';   
	   
	   str=strarr.length>1?strarr[1]:'';   
	   fmt=fmtarr.length>1?fmtarr[1]:'';   
	   i=0;   
	  for(var f=0;f<fmt.length;f++){   
	    switch(fmt.substr(f,1)){   
	      case '#':   
	        if(i<str.length) retstr+=str.substr(i++,1);   
	        break;   
	      case '0':   
	        if(i<str.length) retstr+= str.substr(i++,1);   
	        else retstr+='0';   
	        break;   
	     }   
	   }   
	  return retstr.replace(/^,+/,'').replace(/\.$/,'');   
}

String.prototype.replaceSpecialChars=function(regEx){
	if(!regEx){
		regEx = /[\`\"\~\!\@#$\%\^\&\*\(\)\+\=\|\{\}\'\:\;\'\,\/\/\[\/\/\]\.\<\>\/\?\~\！\@#\￥\%\…\…\&\*\（\）\—\—\+\|\{\}\【\】\‘\；\：\”\“\’\。\，\、\？]/g;
	}
	return this.replace(regEx,'');
	
};



</script>
<!-- 
<SCRIPT LANGUAGE="JavaScript" src=http://float2006.tq.cn/floatcard?adminid=9557094&sort=0 ></SCRIPT>
 -->
</body>
</html>
