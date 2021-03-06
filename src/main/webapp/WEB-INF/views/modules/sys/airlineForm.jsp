<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>基础信息维护-交通信息-新增航空公司</title>
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"
	type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		// 初始设置
		changeArea();
		showViewShipInfo($("#showAirlineInfo"));
	});

	$(function() {
		//限制使用了onlyAlpha类样式的控件只能输入字母
		$(".onlyAlpha").onlyAlpha();
		//新增航空公司
		$(".airCompany")
				.on(
						"click",
						"span[data='addCompany']",
						function() {
							var $this = $(this);
							$this.parent().before($("#TemplateCompany").html());
							$this.parent().prev().find(
									"span[data='companyOrder']").text(
									$(".companyInfo:visible").length);
						})
				.on(
						"click",
						"span[data='deleteAirline']",
						function() {//删除航空公司
							if($("div.companyInfo:visible").length==1) return;
							var $this = $(this);
							$this.parents("div.companyInfo").remove();
							$("span[data='companyOrder']").each(
									function(index, element) {
										if ($(element).is(":visible")) {
											$(element).text(index + 1);
										}
									});
						})
				.on(
						"click",
						"span[data='addShipGrade']",
						function() {//新增舱位等级
							var $this = $(this);
							var index = 1 + $this.parentsUntil("div.airInfo")
									.parent().find(
											"span[data='shipGradeOrder']").length;
							$this.parent().before($("#TemplateShip").html());
							$this.parent().prev().prev().find(
									"span[data='shipGradeOrder']").text(index);
						})
				.on(
						"click",
						"span[data='deleteShipGrade']",
						function() {//删除舱位等级
							if($(this).parents("div.airInfo").find("div.air_fly_number_add_d").length==1) return;
							var $airInfo = $(this).parentsUntil("div.airInfo")
									.parent();
							var $thisDiv = $(this).parentsUntil(
									"div.air_fly_number_add_d").parent();
							var $line = $thisDiv.next();
							$thisDiv.remove();
							$line.remove();
							$airInfo.find("span[data='shipGradeOrder']").each(
									function(index) {
										$(this).text(index + 1);
									});
						})
				.on(
						"click",
						"span[data='addShip']",
						function() {//新增舱位
							var $container = $(this).parentsUntil(
									"div.air_fly_number_add_d").parent().find(
									"ul.listShip");
							var index = 1 + $container.find("li").length;
							var html = '<li>舱位<span>'
									+ index
									+ '</span>：<input type="text" name="shipGrade"><strong class="del_flght_Shipping_space"></strong></li>';
							$container.append(html);
						}).on("click", ".listShip li strong", function() {//删除舱位
					if($(this).parents(".listShip").find("li").length==1) return;
					var $container = $(this).parents(".listShip");
					$(this).parent("li").remove();
					//重新排序
					$container.find("li").each(function(index, element) {
						$(element).find("span").text(index + 1);
					});
				}).on(
						'click',
						"span[data='addShipnumber']",
						function() {
							// 新增航班
							$(this).parentsUntil("div.companyInfo").parent()
									.find("div.airInfo:last").after(
											$("#TemplateCompany div.airInfo")
													.clone().show());
						}).on(
						'click',
						"span[data='deleteShip']",
						function() {
							if ($("div.companyInfo").has(this).find(
									"div.airInfo").length == 1)
								return;
							// 删除航班
							$("div.airInfo").has(this).remove();
						});
	});

	//展开补充舱位相关信息
	function showViewShipInfo(dom) {
		var $this = $(dom);
		var $airInfo = $this.parents(".seach25").nextAll(".airInfo");
		if (!$airInfo.is(":visible")) {
			$airInfo.slideDown();
			$this.siblings(".airInfo-arrow").show();
			$this.text($this.text().replace("展开", "收起"));
		}
	}

	//展开补充舱位相关信息
	function showShipInfo(dom) {
		var $this = $(dom);
		var $airInfo = $this.parents(".seach25").nextAll(".airInfo");
		if ($airInfo.is(":visible")) {
			$airInfo.slideUp();
			$this.siblings(".airInfo-arrow").hide();
			$this.text($this.text().replace("收起", "展开"));
		} else {
			$airInfo.slideDown();
			$this.siblings(".airInfo-arrow").show();
			$this.text($this.text().replace("展开", "收起"));
		}
	}

	function saveForm() {
		var area = $("input[name='area']:checked").val();
		var country = $("#countryId").val();
		if(country==null || country==''){
			$.jBox.tip("请选择国家!");
			$("#countryId").focus();
			return;
		}
		var airlineName = "";
		var airlineCode = "";
		var flightnumber = "";
		var departuretime = "";
		var arrivaltime = "";
		var dayNum = "";
		var shipName = "";
		var shipGrade = "";
		
		var submitFlag = true;
		//保存依据: ';'跨航空公司    ','跨航班号    '_'跨仓位等级    '!'仓位
		var airlineCodeMap = {};
		var flightMap = {};
		$("div.companyInfo:visible").each(function(){
			//收集航空公司名称
			$(this).find("input[name='airlineName']").each(function(){
				if($(this).val()==null || $(this).val()==''){
					$.jBox.tip("请输入航空公司!");
					$(this).focus();
					submitFlag = false;
					return;
				}
				airlineName += transNull($(this).val())+";";
			});
			if(!submitFlag){
				return;
			}
			//收集航空公司二字码
			$(this).find("input[name='airlineCode']").each(function(){
				if($(this).val()==null || $(this).val()==''){
					$.jBox.tip("请输入二字码!");
					$(this).focus();
					submitFlag = false;
					return;
				}
				//edit by majiancheng 2015-11-10
				//校验二字码是否字母
				submitFlag = isValidContent_en_num($(this).val(),"二字码必须为英文字母或数字,请重新输入!");
				if(!submitFlag){
					return;
				}
				//校验页面二字码的唯一性
				if(!airlineCodeMap[$(this).val()]){
					airlineCodeMap[$(this).val()] = 1;				
				}else{
					top.$.jBox.tip('二字码"'+$(this).val()+'"重复,请重新输入!', 'error');
					submitFlag = false;
					airlineCodeMap = {};
					return;
				}			
				//校验二字码唯一性
				submitFlag = checkAirlineCode(area,$(this).val());
				if(!submitFlag){
					return;
				}
				airlineCode += transNull($(this).val())+";";
			});
			if(!submitFlag){
				return;
			}
			//收集航班号
			$(this).find("input[name='flightnumber']").each(function(){
				if(!flightMap[$(this).val()]){
					flightMap[$(this).val()] = 1;
				}else{
					/* top.$.jBox.tip('航班号"'+$(this).val()+'"重复,请重新输入!', 'error');
					submitFlag = false;
					flightMap = {};
					return; */
				}
				flightnumber += transNull($(this).val())+",";
			});
			if(!submitFlag){
				return;
			}
			flightnumber = flightnumber+";";
			//收集出发时间
			$(this).find("input[name='departuretime']").each(function(){
				departuretime += transNull($(this).val())+",";
			});
			departuretime = departuretime+";";
			//收集到达时间
			$(this).find("input[name='arrivaltime']").each(function(){
				arrivaltime += transNull($(this).val())+",";
			});
			arrivaltime = arrivaltime+";";
			//收集天数
			$(this).find("input[name='dayNum']").each(function(){
				dayNum += transNull($(this).val())+",";
			});
			dayNum = dayNum+";";
			//收集仓位等级    仓位
			$(this).find("div.airInfo").each(function(){
				var spaceLevelMap = {};
				$(this).find("input[name='shipName']").each(function(){
					if(!spaceLevelMap[$(this).val()]){
						spaceLevelMap[$(this).val()] = 1;
					}else{
						submitFlag = false;
						top.$.jBox.tip('舱位等级"'+$(this).val()+'"重复,请重新输入!', 'error');
						return;
					}
					shipName += transNull($(this).val())+"_";
				});
				if(!submitFlag){
					return;
				}
				shipName = (shipName==''?'$':shipName)+",";
			    $(this).find("div.air_fly_number_add_d").each(function(){
					$(this).find("input[name='shipGrade']").each(function(){
						if($(this).val()!=null && $(this).val()!=''){
						    //edit by majiancheng 2015-11-10
							submitFlag = isValidContent_en_num($(this).val(),'舱位必须为英文字母或数字!');
						}
						shipGrade += transNull($(this).val())+"!";
					});
					shipGrade = (shipGrade==''?'$':shipGrade)+"_";
				});
				if(!submitFlag){
					return;
				}
				shipGrade = (shipGrade==''?'$':shipGrade)+",";
			});
			shipName = shipName+";";
			shipGrade = shipGrade+";";
		});
		if(!submitFlag){
			return;
		}
		$.post("${ctx}/sys/airline/saveForm",
	       {
	        v_area:area,
	        v_country:country,
            v_airlineName:airlineName,
            v_airlineCode:airlineCode,
            v_flightnumber:flightnumber,
            v_departuretime:departuretime,
            v_arrivaltime:arrivaltime,
            v_dayNum:dayNum,
            v_shipName:shipName,
            v_shipGrade:shipGrade,
            v_tree:new Date()
            },function(data){
            	top.$.jBox.tip(data.msg);
            	setTimeout(function(){
            		if(data.ret=='success'){
            			window.location="${ctx}/sys/airline/list/"+area;
            		}
            	},900);
            	
            }
        );
	}
	
	//将空值保存为$
	function transNull(v){
		if(v==null || v==''){
			return "$";
		}
		return v;
	}

	function changeArea() {
		// 初始
		var countryObj = document.getElementById("countryId");
		countryObj.options.length = 0;

		var area = $("input[name='area']:checked").val();
		if ("1" == area) {
			$.ajax({
				type : "POST",
				url : "${ctx}/sys/airline/getCountryIdByName",
				cache : false,
				dataType : "json",
				async : false,
				data : {
					countryName : "中国"
				},
				success : function(result) {
					if (result.flag == "success") {
						var countryId = result.countryId;
						countryObj.options.add(new Option("", ""));
						countryObj.options.add(new Option("中国", countryId));
					}
				},
				error : function(e) {
					top.$.jBox.info("请求失败。", "错误");
					return false;
				}
			});
		} else if ("2" == area) {
			$.ajax({
				type : "GET",
				url : "${ctx}/sys/airline/getCountryList",
				cache : false,
				dataType : "json",
				async : false,
				data : {
					airportArea : "2"
				},
				success : function(result) {
					$(result).each(function(i, obj) {
						var opt = document.createElement("option");
						opt.value = obj.id;
						opt.text = obj.label;
						countryObj.appendChild(opt);
					});
				},
				error : function(e) {
					top.$.jBox.info("请求失败。", "错误");
					return false;
				}
			});
		}
	}

	function forwardList() {
		var area = $("input[name='area']:checked").val();
		window.location.href = "${ctx}/sys/airline/list/" + area;
	}
	
	/**
	 * 判定输入项为汉字
	 */
	function isValidContent_cn(strName,msg) {
		var str = strName;
		var reg = /[^\u4e00-\u9fa5]/;
		if (reg.test(str)) {
		    top.$.jBox.tip(msg, 'error');
			return false;
		}
		return true;
	}

	function isValidContent_cn_en_num(str) {
		var reg = /[^\u4e00-\u9fa5\w\d]/;
		if (reg.test(str)) {
			return false;
		}
		return true;
	}

	function isValidContent_en(str,msg) {
		var reg = /^[A-Za-z]+$/;
		if (!reg.test(str)) {
			top.$.jBox.tip(msg, 'error');
			return false;
		}
		return true;
	}
	
	function isValidContent_en_num(str,msg) {
		var reg = /^[A-Za-z0-9]+$/;
		if (!reg.test(str)) {
			top.$.jBox.tip(msg, 'error');
			return false;
		}
		return true;
	}
	
	//校验航空公司二字码唯一性
	function checkAirlineCode(area,airlineCode){
	    var flag = true;
		var jsonRes = {
				area : area,
				airlineCode : airlineCode
			};
		$.ajax({
			type : "POST",
			url : "${ctx}/sys/airline/checkSameAirlineCode",
			cache : false,
			dataType : "json",
			async : false,
			data : jsonRes,
			success : function(data) {
				if (data.flag == "error") {
					top.$.jBox.tip('二字码"'+airlineCode+'"在系统中已存在,请重新输入!', 'error');
					flag = false;
				}
			},
			error : function(e) {
				top.$.jBox.tip('请求失败。', 'error', {
					focusId : 'currencyName'
				});
				flag = false;
			}
		});
		return flag;
	}
</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit">新增航空公司</div>
	<form:form id="inputForm" modelAttribute="airlineinfo" action="${ctx}/sys/user/save" method="post" class="form-horizontal">
		<div class="airCompany">
			<div class="seach25 seach100">
				<p class="seach_check">
					<label for="inquiry_radio_flights1"><input
						name="area" id="airlineArea" type="radio" value="1"
						checked="checked" onclick="changeArea()" />国内航空公司</label>
				</p>
				<p class="seach_check">
					<label for="inquiry_radio_flights2"><input
						name="area" id="airlineArea" type="radio" value="2"
						onclick="changeArea()" />国外航空公司</label>
				</p>
			</div>
			<div class="kongr"></div>
			<div class="inquiry_flights1">
				<div class="seach25 seach100">
					<p>
						<font color="red">*&nbsp;</font>国家：
					</p>
					<p class="seach_r">
						<select id="countryId" name="countryId">
						</select>
					</p>
				</div>
				<div class="kongr20"></div>
				<!--航空公司信息开始-->
				<div class="companyInfo" style="border-bottom:none;">
					<div class="seach25 seach100">
						<p>
							<font color="red">*&nbsp;</font>航空公司<span data="companyOrder">1</span>：
						</p>
						<input type="text" class="airCompanyName" id="airlineName" name="airlineName" maxlength="30" />&#12288;&#12288; 
						<font color="red">*&nbsp;</font><span>二字码：</span>
						<input type="text" id="airlineCode" name="airlineCode" maxlength="2" />
						<span class="linkAir">
							<span class="del_flght_numbers" data="deleteAirline"></span>
							<span onclick="showShipInfo(this)" class="linkAir-spn">展开补充舱位相关信息</span>
							<div class="airInfo-arrow">
								<i></i>
							</div></span>
					</div>
					<div class="airInfo">
						<div class="seach25 seach100">
							<div class="air_fly_number_add">
								<p>航班号：</p>
								<input type="text" class="airCompanyName" name="flightnumber" id="flightnumber"  maxlength="10"/>
								<span class="del_flght_numbers" data="deleteShip"></span>
								<span class="ml20 linkAir-spn" data="addShipnumber">+新增航班</span>
							</div>
							<div class="air_fly_number_add">
								<p>航班出发时间：</p>
								<input id="departuretime" style="width:150px;" name="departuretime" 
								       class="required dateinputBg" onclick="WdatePicker({dateFmt:'HH:mm'})" readonly="readonly" type="text" />
							</div>
							<div class="air_fly_number_add">
								<p>航班到达时间：</p>
								<input id="arrivaltime" style="width:150px;" name="arrivaltime"
									   class="required dateinputBg" onclick="WdatePicker({dateFmt:'HH:mm'})" readonly="readonly" type="text" />
							</div>
							<div class="air_fly_number_add">
								<p>天数：</p>
								<input id="dayNum" name="dayNum" type="text" data-type="number" onafterpaste="this.value=this.value.replace(/\D/g,'')" 
								       onkeyup="this.value=this.value.replace(/\D/g,'')" maxlength="2"/>
							</div>
							<div class="air_fly_number_add_d">
								<p>
									舱位等级<span data="shipGradeOrder">1</span>：
								</p>
								<p class="seach_r">
									<input type="text" id="shipName" name="shipName" maxlength="60"/>
									<span class="del_flght_numbers" data="deleteShipGrade"></span>
									<span class="ml20 linkAir-spn" data="addShip">+新增舱位</span>
								</p>
								<div class="kong"></div>
								<ul class="listShip clearfix">
									<li>舱位<span>1</span>：
									<input type="text" name="shipGrade" class="onlyAlpha" maxlength="2" />
									<strong class="del_flght_Shipping_space"></strong></li>
									<!-- <li>舱位<span>2</span>：
									<input type="text" name="shipGrade" class="onlyAlpha" maxlength="2" />
									<strong class="del_flght_Shipping_space"></strong></li> -->
								</ul>
							</div>
							<div class="airInfo-tit1"></div>
							<div class="clearfix">
								<span class="linkAir-spn air_fly_number_add_d2" data="addShipGrade">+新增舱位等级</span>
							</div>
						</div>
					</div>
				</div>
				<!--航空公司信息结束-->
				<div class="clearfix">
					<span class="ydbz_s" data="addCompany">+新增航空公司</span>
				</div>
			</div>
			<br><br>
					<div class="ydBtn ydBtn2" style="width:600px">
						<tags:message content="${message}" />
						<a class="ydbz_s" onclick="saveForm()">保存</a>&nbsp;&nbsp; 
						<a class="ydbz_s" onclick="forwardList();">返回</a>
					</div>
		</div>
	</form:form>
	
	<!--航空公司模板开始-->
	<div id="TemplateCompany" style="display:none;">
		<div class="companyInfo">
			<div class="seach25 seach100">
				<p>
					<font color="red">*&nbsp;</font>航空公司<span data="companyOrder">1</span>：
				</p>
				<input type="text" class="airCompanyName" id="airlineName" name="airlineName"  maxlength="30"/>&#12288;&#12288; 
				<span><font color="red">*&nbsp;</font>二字码：</span>
				<input type="text" id="airlineCode" name="airlineCode" maxlength="2" /> 
				<span class="linkAir">
					<span class="del_flght_numbers" data="deleteAirline"></span>
					<span onclick="showShipInfo(this)" class="linkAir-spn">展开补充舱位相关信息</span>
					<div class="airInfo-arrow">
						<i></i>
					</div></span>
			</div>
			<div class="airInfo">
				<div class="seach25 seach100">
					<div class="air_fly_number_add">
						<p>航班号：</p>
						<input type="text" class="airCompanyName" name="flightnumber" id="flightnumber"  maxlength="10"/>
						<span class="del_flght_numbers" data="deleteShip"></span>
						<span class="ml20 linkAir-spn" data="addShipnumber">+新增航班</span>
					</div>
					<div class="air_fly_number_add">
						<p>航班出发时间：</p>
						<input id="departuretime" name="departuretime" class="required dateinputBg" style="width:150px;"
							   onclick="WdatePicker({dateFmt:'HH:mm'})" readonly="readonly" type="text" />
					</div>
					<div class="air_fly_number_add">
						<p>航班到达发时间：</p>
						<input id="arrivaltime" name="arrivaltime"
							class="required dateinputBg" style="width:150px;"
							onclick="WdatePicker({dateFmt:'HH:mm'})"
							readonly="readonly" type="text" />
					</div>
					<div class="air_fly_number_add">
						<p>天数：</p>
						<input id="dayNum" name="dayNum" type="text" data-type="number" onafterpaste="this.value=this.value.replace(/\D/g,'')"
							   onkeyup="this.value=this.value.replace(/\D/g,'')" maxlength="2"/>
					</div>
					<div class="air_fly_number_add_d">
						<p>
							舱位等级<span data="shipGradeOrder">1</span>：
						</p>
						<p class="seach_r">
							<input type="text" id="shipName" name="shipName" />
							<span class="del_flght_numbers" data="deleteShipGrade"></span>
							<span class="ml20 linkAir-spn" data="addShip">+新增舱位</span>
						</p>
						<div class="kong"></div>
						<ul class="listShip clearfix">
							<li>舱位<span>1</span>：
							<input type="text" name="shipGrade" class="onlyAlpha" maxlength="2" />
							<strong class="del_flght_Shipping_space"></strong></li>
							<!-- <li>舱位<span>2</span>：
							<input type="text" name="shipGrade" class="onlyAlpha" maxlength="2" />
							<strong class="del_flght_Shipping_space"></strong></li> -->
						</ul>
					</div>
					<div class="airInfo-tit1"></div>
					<div class="clearfix">
						<span class="linkAir-spn air_fly_number_add_d2" data="addShipGrade">+新增舱位等级</span>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!--航空公司模板结束-->
	<!--舱位等级模板开始-->
	<div id="TemplateShip" style="display:none;">
		<div class="air_fly_number_add_d">
			<p>
				舱位等级<span data="shipGradeOrder">1</span>：
			</p>
			<p class="seach_r">
				<input type="text" name="shipName" />
				<span class="del_flght_numbers" data="deleteShipGrade"></span>
				<span class="ml20 linkAir-spn" data="addShip">+新增舱位</span>
			</p>
			<div class="kong"></div>
			<ul class="listShip clearfix">
				<li>舱位<span>1</span>：
					<input style="ime-mode: disabled;" name="shipGrade" class="onlyAlpha" maxlength="2" type="text">
					<strong class="del_flght_Shipping_space"></strong>
				</li>
			</ul>
		</div>
		<div class="airInfo-tit1"></div>
	</div>
	<!--舱位等级模板结束-->
	<!--右侧内容部分结束-->
</body>
</html>
