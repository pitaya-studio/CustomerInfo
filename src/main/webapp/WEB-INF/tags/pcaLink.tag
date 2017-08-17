<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="provinceField" type="java.lang.String"
	required="true" description="省份字段"%>
<%@ attribute name="provinceValue" type="java.lang.String"
	required="false" description="省份"%>
<%@ attribute name="cityField" type="java.lang.String" required="true"
	description="城市字段"%>
<%@ attribute name="cityValue" type="java.lang.String" required="false"
	description="城市"%>
<%@ attribute name="areaField" type="java.lang.String" required="true"
	description="区域字段"%>
<%@ attribute name="areaValue" type="java.lang.String" required="false"
	description="区域"%>
<script type="text/javascript" src="${ctxStatic}/js/area.js"></script>

<div class="selectListPcaLink">
	<select class="provincePcaLink" name="${provinceField}">
	</select> <select class="cityPcaLink" name="${cityField}">
	</select> <select class="districtPcaLink" name="${areaField}">
	</select>
</div>

<script type="text/javascript">
	$(function() {
		$(".selectListPcaLink").each(
				function() {
					var url = "${ctxStatic}/js/GovDistrict.json";
					var areaJson;
					var temp_html;
					var oProvince = $(this).find(".provincePcaLink");
					var oCity = $(this).find(".cityPcaLink");
					var oDistrict = $(this).find(".districtPcaLink");
					var isInit = false;
					var tmp = "";
					//初始化省
					var province = function() {
						$.each(areaJson.children, function(i, province) {
							tmp = $("<option value='"+province.code+"'>"
									+ province.name + "</option>");
							oProvince.append($(tmp));
						});
						city();
					};
					//赋值市
					var city = function() {
						oCity.empty();
						var n = oProvince.get(0).selectedIndex;
						$.each(areaJson.children[n].children, function(i, city) {
							tmp = $("<option value='"+city.code+"'>" + city.name
									+ "</option>");
							oCity.append($(tmp));
						});
						district();
					};
					//赋值县
					var district = function() {
						oDistrict.empty();
						var m = oProvince.get(0).selectedIndex;
						var n = oCity.get(0).selectedIndex;
					/* 	if (typeof (areaJson[m].c[n].d) == "undefined") {
							oDistrict.css("display", "none");
						} else { */
							oDistrict.css("display", "inline");
							$.each(areaJson.children[m].children[n].children, function(i, district) {
								tmp = $("<option value='"+district.code+"'>"
										+ district.name + "</option>");
								oDistrict.append(tmp);
							});
						/* }
						; */
						//第一次初始化
						if (!isInit) {
							
							init();
						}
						
					};
					//选择省改变市
					oProvince.change(function() {
						city();
					});
					//选择市改变县
					oCity.change(function() {
						district();
					});
					//获取json数据
					$.getJSON(url, function(data) {
						areaJson = data;
						province(areaJson);

					});
					//初始化
					  function init() {
						  isInit = true;
						if ("${provinceValue}" != "") {
							$(".provincePcaLink").find(
									'option[value="${provinceValue}"]').prop(
									"selected", 'selected');
							city();
						}
						if ("${cityValue}" != "") {
							$(".cityPcaLink").find(
									'option[value="${cityValue}"]').prop(
									"selected", 'selected');
							district();
						}
						if ("${areaValue}" != "") {
							$(".districtPcaLink").find(
									"option[value='${areaValue}']").prop(
									"selected", 'selected');
						}
						
					}

				});
	});
</script>