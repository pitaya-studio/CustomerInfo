<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
	<c:choose>
		<c:when test="${flag==1}">
			<title>控房-机票库存新增日期 </title>
		</c:when>
		<c:otherwise>
			<title>控房-新增机票库存 </title>
		</c:otherwise>
	</c:choose>
	
	<meta name="decorator" content="wholesaler"/>
	<link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/js/jquery-migrate-1.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/default.validator.js"  type="text/javascript"></script>
	<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/tmp.basicInfo.js" type="text/javascript" ></script>
	
		<script type="text/javascript">
		
		$(document).ready(function() {
			//点击机票库存的提交按钮
			$("#addFlightControl").validate({
				rules:{
					
				},
				submitHandler: function(form){
					$("input[name=statuss]").val(1);
					//在此验证必输的数据
					if(!validateNum()){
						return ;
					}
					var url="${ctx}/flightControl/saveflightcontrol";
					$("#saveSubmit").attr("disabled", "disabled");
					$.post(url,$("#addFlightControl").serialize(),function(data){
						if(data.message=="1"){
							//$("#searchForm",window.opener.document).submit();
							$.jBox.tip("添加成功!");
							//setTimeout(function(){window.close();},500);
						}else if(data.message=="2"){
							//$("#searchForm",window.opener.document).submit();
							$.jBox.tip("修改成功!");
							//setTimeout(function(){window.close();},500);
						}else if(data.message=="3"){
							$.jBox.tip(data.error,'warning');
							$("#saveSubmit").attr("disabled", false);
						}else{
							$.jBox.tip('系统异常，请重新操作!','warning');
							$("#saveSubmit").attr("disabled", false);
						}
					});
				}
			});
			//点击机票库存的保存草稿箱按钮
			$("#addFlightControl").validate({
				rules:{
					
				},
				submitHandler: function(form){
					$("input[name=statuss]").val(0);
					//在此不用验证数据					
					var url="${ctx}/flightControl/saveflightcontrol";
					$("#saveDraftSubmit").attr("disabled", "disabled");
					$.post(url,$("#addFlightControl").serialize(),function(data){
						if(data.message=="1"){
							//$("#searchForm",window.opener.document).submit();
							$.jBox.tip("添加成功!");
							//setTimeout(function(){window.close();},500);
						}else if(data.message=="2"){
							//$("#searchForm",window.opener.document).submit();
							$.jBox.tip("修改成功!");
							//setTimeout(function(){window.close();},500);
						}else if(data.message=="3"){
							$.jBox.tip(data.error,'warning');
							$("#saveDraftSubmit").attr("disabled", false);
						}else{
							$.jBox.tip('系统异常，请重新操作!','warning');
							$("#saveDraftSubmit").attr("disabled", false);
						}
					});
				}
			});
			//点击机票库存新增日期的提交按钮
			$("#updateFlightControl").validate({
				rules:{
					
				},
				submitHandler: function(form){
					$("input[name=statuss]").val(1);
					if(!validateNum()){
						return ;
					}
					var url="${ctx}/flightControl/saveflightcontrol?flag=1";
					$("#updateSubmit").attr("disabled", "disabled");
					$.post(url,$("#updateFlightControl").serialize(),function(data){
						if(data.message=="1"){
							//$("#searchForm",window.opener.document).submit();
							$.jBox.tip("添加成功!");
							//setTimeout(function(){window.close();},500);
						}else if(data.message=="2"){
							//$("#searchForm",window.opener.document).submit();
							$.jBox.tip("修改成功!");
							//setTimeout(function(){window.close();},500);
						}else if(data.message=="3"){
							$.jBox.tip(data.error,'warning');
							$("#updateSubmit").attr("disabled", false);
						}else{
							$.jBox.tip('系统异常，请重新操作!','warning');
							$("#updateSubmit").attr("disabled", false);
						}
					});
				}
			});
			//点击机票库存新增日期的保存草稿箱按钮
			$("#updateFlightControl").validate({
				rules:{
					
				},
				submitHandler: function(form){
					$("input[name=statuss]").val(0);
					if(!validateNum()){
						return ;
					}
					var url="${ctx}/flightControl/saveflightcontrol?flag=1";
					$("#updateDraftSubmit").attr("disabled", "disabled");
					$.post(url,$("#updateFlightControl").serialize(),function(data){
						if(data.message=="1"){
							//$("#searchForm",window.opener.document).submit();
							$.jBox.tip("添加成功!");
							//setTimeout(function(){window.close();},500);
						}else if(data.message=="2"){
							//$("#searchForm",window.opener.document).submit();
							$.jBox.tip("修改成功!");
							//setTimeout(function(){window.close();},500);
						}else if(data.message=="3"){
							$.jBox.tip(data.error,'warning');
							$("#updateDraftSubmit").attr("disabled", false);
						}else{
							$.jBox.tip('系统异常，请重新操作!','warning');
							$("#updateDraftSubmit").attr("disabled", false);
						}
					});
				}
			});
			function validateNum(){
				/**var flightname=$("input[name=name]").val();
				if(flightname==null||flightname==""){
					$.jBox.tip("控票名称不能为空,请填写控票名称!");
					return false;
				}

				var country=$("input[name=country]").val();
				if(country==null||country==""){
					$.jBox.tip("国家不能为空,请选择国家!");
					return false;
				}

				var islanduuid=$("select[name=islandUuid]").val();
				if(islanduuid==null||islanduuid==""){
					$.jBox.tip("岛屿不能为空,请选择岛屿!");
					return false;
				}
				
				var airline=$("select[name=airline]").val();
				if(airline==null||airline==""){
					$.jBox.tip("航空公司不能为空,请选择航空公司!");
					return false;
				}
				
				var strflag=true;
				$.each($("input[name=departureDates]"), function() {
					var date = $(this).val();
					if(date==null||date==""){
						$.jBox.tip("日期不能为空,请为日期为空的明细选择日期!");
						strflag=false;
					}
				});
				
				var spacetypes = $("input[name=spaceGradeTypes]");
				
				$.each(spacetypes, function() {
					var spaceGradeLevel = $(this).val();
					if(spaceGradeLevel==null||spaceGradeLevel==""){
						$.jBox.tip("舱位等级不能为空,请为舱位等级为空的明细选择舱位等级!");
						strflag=false;
					}
				});
				
				var stocks = $("input[name=stocks]");
				
				$.each(stocks, function() {
					var stock = $(this).val();
					if(stock==null||stock==""){
						$.jBox.tip("库存不能为空,请为库存等级为空的明细填写库存!");
						strflag=false;
					}
				});
				*/
				return strflag;
			}
			
		});
		
			$(function() {
				//搜索条件筛选
				launch();
				//产品名称文本框提示信息
				inputTips();
				//操作浮框
				operateHandler();
				//初始化国家的标签
				
				
				var $contentTable = $("#contentTable");
				$("select[name='country']").combobox();
				$("select[name='islands']").combobox();
				$("select[name='hotelname']").combobox();
				/* $("select[name='currency']").combobox({
					"select": function() {
						$contentTable.find('span.currency').text($("select[name='currency']").val());
						$contentTable.find('select.currency').val($("select[name='currency']").val());
					}
				}); */
				
				$("select[name='currencyId']").on('change',function(){
					$contentTable.find('span.currency').text($("select[name='currencyId']").val());
					$contentTable.find('select.currency').val($("select[name='currencyId']").val());
				});
				
				var $templateTr = $contentTable.find("tbody tr:first").clone();
				$templateTr.find('a.saveAndNew').after('<span class="jiange_li">|</span><a data-delTag="tr" href="javascript:void(0)">删除</a>')
				$contentTable.on("click", "a[data-delTag]", function() {
					// 删除
					var $this = $(this);
					var delTag = $this.attr("data-delTag");
					$contentTable.find(delTag).has($this).remove();
				}).on('click', 'a.saveAndNew', function() {
					// 保存并新增
					var $tr = $(this).parent().parent();
					$tr.after(saveTr($tr));
				}).on('click', 'a.new', function() {
					// 新增
					var $tr = $(this).parent().parent();
					var $newTr = editTr($tr);
					$tr.after($newTr);
				}).on('click', 'a.save', function() {
					// 保存
					var $tr = $(this).parent().parent();
					$tr.replaceWith(saveTr($tr));
				}).on('click', 'a.edit', function() {
					// 编辑
					var $tr = $(this).parent().parent();
					var $newTr = editTr($tr).data("source", $tr);
					$newTr.find('td:last').html('<a class="save" href="javascript:void(0)">保存</a><span class="jiange_li">|</span><a class="cancel" href="javascript:void(0)">取消</a>');
					$tr.replaceWith($newTr);
				}).on('click', 'a.cancel', function() {
					// 取消
					var $tr = $(this).parent().parent();
					var $source = $tr.data("source");
					$tr.replaceWith($source);
				}).on('click', 'a.addHotel', function() {
					//增加参考酒店
					addHotel($(this).parent().parent());
				});
				// 保存
				function saveTr($tr) {
					var $newTr = $templateTr.clone();
					var $newTds = $newTr.find("td").empty();
					var $oldTds = $tr.find("td");
					var ci = 0;
					// 出发日期
					$newTds.eq(ci).text($oldTds.eq(ci).find("input").val());
					//add by wangxk
					$newTds.eq(ci).append("<input type=hidden name='departureDates'>");
					$newTds.eq(ci).find("input[name=departureDates]").val($newTds.eq(ci).text());
					ci++;
					// 舱位等级
					$newTds.eq(ci).text($oldTds.eq(ci).find("option:selected").text());
					// add by wangxk
					$newTds.eq(ci).append("<input type=hidden name='spaceGradeTypes'>");
					$newTds.eq(ci).find("input[name=spaceGradeTypes]").val($oldTds.eq(ci).find("select").val());
					
					ci++;
					// 总价格
					$newTds.eq(ci).html('<span class="currency">' + $oldTds.eq(ci).find("option:selected").text() + '</span><span>' + $oldTds.eq(ci).find("input").val() + '</span>');
					//add by wangxk  币种id+总额 
					$newTds.eq(ci).append("<input type=hidden name='priceCurrencyIds'>");
					$newTds.eq(ci).find("input[name=priceCurrencyIds]").val($oldTds.eq(ci).find("select").val());
					$newTds.eq(ci).append("<input type=hidden name='prices'>");
					$newTds.eq(ci).find("input[name=prices]").val($oldTds.eq(ci).find("input").val());
					
					ci++;
					// 总税
					$newTds.eq(ci).html('<span class="currency">' + $oldTds.eq(ci).find("option:selected").text() + '</span><span>' + $oldTds.eq(ci).find("input").val() + '</span>');
					//add by wangxk  总税币种id+总额 
					$newTds.eq(ci).append("<input type=hidden name='taxesCurrencyIds'>");
					$newTds.eq(ci).find("input[name=taxesCurrencyIds]").val($oldTds.eq(ci).find("select").val());
					$newTds.eq(ci).append("<input type=hidden name='taxesPrices'>");
					$newTds.eq(ci).find("input[name=taxesPrices]").val($oldTds.eq(ci).find("input").val());
					
					ci++;
					// 库存
					$newTds.eq(ci).text($oldTds.eq(ci).find("input").val());
					//add by wangxk
					$newTds.eq(ci).append("<input type=hidden name='stocks'>");
					$newTds.eq(ci).find("input[name=stocks]").val($oldTds.eq(ci).find("input").val());
					ci++;
				    // 参考酒店
					//add by wangxk
					var hotelUuid=[];
					$.each($oldTds.eq(ci).find("p"), function() {
						var type = $(this).find("select").val();
						var text = $(this).find("option:selected").text();
						$newTds.eq(ci).append('<p><span> ' + text + ' </span> ');
						hotelUuid.push(type);
					});
					
					$newTds.eq(ci).append("<input type=hidden name='hotelUuids'>");
					$newTds.eq(ci).find("input[name=hotelUuids]").val(hotelUuid.join(";"));
					ci++;
					// 备注
					$newTds.eq(ci).text($oldTds.eq(ci).find("input").val());
					//add by wangxk
					$newTds.eq(ci).append("<input type=hidden name='memos'>");
					$newTds.eq(ci).find("input[name=memos]").val($oldTds.eq(ci).find("input").val());
					ci++;
					// 操作
					$newTds.eq(ci).html('<a class="new" href="javascript:void(0)">新增</a><span class="jiange_li">|</span><a class="edit" href="javascript:void(0)">修改</a><span class="jiange_li">|</span><a data-delTag="tr" href="javascript:void(0)">删除</a>');
					return $newTr;
				}
				
				// 编辑
				function editTr($tr) {
						var $newTr = $templateTr.clone();
						var $newTds = $newTr.find("td");
						var $oldTds = $tr.find("td");
						var ci = 0;
						// 入住日期
						$newTds.eq(ci).find("input.dateinput").val($oldTds.eq(ci).text());
						ci++;
						// 舱位等级
						$newTds.eq(ci).find("select").val($oldTds.eq(ci).text());
						ci++;
					    // 价格
						$newTds.eq(ci).find("select").val($oldTds.eq(ci).find('span').eq(0).text());
						$newTds.eq(ci).find("input").val($oldTds.eq(ci).find('span').eq(1).text());
						ci++;
						// 总税
						$newTds.eq(ci).find("select").val($oldTds.eq(ci).find('span').eq(0).text());
						$newTds.eq(ci).find("input").val($oldTds.eq(ci).find('span').eq(1).text());
						ci++;
					    // 库存
						$newTds.eq(ci).find("input").val($oldTds.eq(ci).text());
						ci++;
						// 参考酒店
						$.each($oldTds.eq(ci).find("p"), function(i) {
							if (i == 0) {
								$newTds.eq(ci).find('p select').val($(this).text());
							} else {
								addHotel($newTds.eq(ci), $(this).text());
							}
						});
						ci++;
					    // 备注
						$newTds.eq(ci).find("input").val($oldTds.eq(ci).text());
						return $newTr;
					}
					
					/// 增加参考酒店
				function addHotel($td, val) {
						var $newP = $td.find("p:first").clone();
						$newP.find("a").replaceWith('<a href="javascript:;" data-delTag="p">删除</a>');
						if (val) {
							$newP.find("input.wtext").val(val);
						}
						$td.append($newP);
					}
					
				initSuggest({});
		});
			 //删除产品的提示信息
			function confirmxDel(mess, proId) {
				top.$.jBox.confirm(mess, '系统提示', function(v) {
					if (v == 'ok') {
						//loading('正在提交，请稍等...');
					}
				}, {
					buttonsFocus: 1
				});
				return false;
			}

			function confirmBatchIsNull(mess, sta) {
					if (activityIds != "") {
						if (sta == 'off') {
							confirmBatchOff(mess);
						} else if (sta == 'del') {
							confirmBatchDel(mess);
						}
					} else {
						$.jBox.info('未选择产品', '系统提示');
					}
				}
				//批量删除确认对话框

			function confirmBatchDel(mess) {
					top.$.jBox.confirm(mess, '系统提示', function(v) {
						if (v == 'ok') {
							//loading('正在提交，请稍等...');
							//$("#searchForm").attr("action","/a/activity/manager/batchdel/"+activityIds);
							//$("#searchForm").submit();
						}
					}, {
						buttonsFocus: 1
					});
					return false;
				}
				//排序

			function sorts(sortBy, element) {
				var _this = $(element);
				_this.parent().siblings("th").children("i").attr("class", "icon_sort");
				var sortFlag = _this.attr("class");
				//降序
				if (sortFlag == "icon_sort icon_up") {
					_this.attr("class", "icon_sort icon_down");
				}
				//升序
				else if (sortFlag == "icon_sort icon_down") {
					_this.attr("class", "icon_sort icon_up");
				} else {
					_this.attr("class", "icon_sort icon_up");
				}
			}
		</script>
		<script type="text/javascript">
			(function($) {
				$.widget("custom.combobox", {
					_create: function() {
						this.wrapper = $("<span>").addClass("custom-combobox").insertAfter(this.element);
						this.element.hide();
						this._createAutocomplete();
						this._createShowAllButton();
					},
					_createAutocomplete: function() {
						var selected = this.element.children(":selected"),
							value = selected.val() ? selected.text() : "";
						this.input = $("<input>").appendTo(this.wrapper).val(value).attr("title", "").addClass("custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left").autocomplete({
							delay: 0,
							minLength: 0,
							source: $.proxy(this, "_source")
						}).tooltip({
							tooltipClass: "ui-state-highlight"
						});
						this._on(this.input, {
							autocompleteselect: function(event, ui) {
								ui.item.option.selected = true;
								this._trigger("select", event, {
									item: ui.item.option
								});
							},
							autocompletechange: "_removeIfInvalid"
						});
					},
					_createShowAllButton: function() {
						var input = this.input,
							wasOpen = false;
						$("<a>").attr("tabIndex", -1).attr("title", "选择").tooltip().appendTo(this.wrapper).button({
							icons: {
								primary: "ui-icon-triangle-1-s"
							},
							text: false
						}).removeClass("ui-corner-all").addClass("custom-combobox-toggle ui-corner-right").mousedown(function() {
							wasOpen = input.autocomplete("widget").is(":visible");
						}).click(function() {
							input.focus();
							// Close if already visible
							if (wasOpen) {
								return;
							}
							// Pass empty string as value to search for, displaying all results
							input.autocomplete("search", "");
						});
					},
					_source: function(request, response) {
						var matcher = new RegExp($.ui.autocomplete.escapeRegex(request.term), "i");
						response(this.element.children("option").map(function() {
							var text = $(this).text();
							if (this.value && (!request.term || matcher.test(text)))
								return {
									label: text,
									value: text,
									option: this
								};
						}));
					},
					_removeIfInvalid: function(event, ui) {
						// Selected an item, nothing to do
						if (ui.item) { //console.log(ui.item);
							this._trigger("afterInvalid", null, ui.item.value);
							return;
						}
						// Search for a match (case-insensitive)
						var value = this.input.val(),
							valueLowerCase = value.toLowerCase(),
							valid = false;
						this.element.children("option").each(function() {
							if ($(this).text().toLowerCase() === valueLowerCase) {
								this.selected = valid = true;
								return false;
							}
						});
						// Found a match, nothing to do
						if (valid) {
							this._trigger("afterInvalid", null, value);
							return;
						}
						// Remove invalid value
						this.input.val("").attr("title", value + "").tooltip("open");
						this.element.val("");
						this._delay(function() {
							this.input.tooltip("close").attr("title", "");
						}, 2500);
						this.input.data("ui-autocomplete").term = "";
					},
					_destroy: function() {
						this.wrapper.remove();
						this.element.show();
					}
				});
			})(jQuery);
			
			//国家级联查询
			function getAjaxSelect(type,obj){
				$.ajax({
					type: "POST",
					
				   	url: "${ctx}/hotelControl/ajaxCheck",
				   	data: {
							"type":type,
							"uuid":$(obj).val()
						  },
					dataType: "json",
				   	success: function(data){
				   		if(type == "islandway"){
				   			$("select[name=islandway]").empty();
				   			$("#hotelUuid").empty();
					   		$("select[name=islandway]").append("<option value=''>不限</option>");
					   		$("#hotelUuid").append("<option value=''>不限</option>");
				   		} else if(type == "roomtype"){
				   			$("select[name=roomType]").empty();
					   		$("select[name=roomType]").append("<option value=''>不限</option>");
				   		} else {
					   		$("#"+type).empty();
					   		$("#"+type).append("<option value=''>不限</option>");
				   		}
				   		if(data){
				   			if(type=="hotel"){
					   			$.each(data.hotelList,function(i,n){
					   					 $("#"+type).append($("<option/>").text(n.nameCn).attr("value",n.uuid));
					   			});
				   			}else if(type=="roomtype"){
				   				$.each(data.roomtype,function(i,n){
				   					 var arr = $("select[name=roomType]");
				   					 for(var j=0; j<arr.length; j++) {
				   					 	$(arr[j]).append($("<option/>").text(n.roomName).attr("value",n.uuid));
				   					 }
				   				});
				   			}else if(type=="foodtype"){
				   				$.each(data.hotelMeals,function(i,n){
				   					 $("#"+type).append($("<option/>").text(n.mealName).attr("value",n.uuid));
				   				});
				   			}else if(type=="islandway"){
				   				$.each(data.listIslandWay,function(i,n){
				   					 var arr = $("select[name=islandway]");
				   					 for(var j=0; j<arr.length; j++) {
				   					 	$(arr[j]).append($("<option/>").text(n.label).attr("value",n.uuid));
				   					 }
				   				});
				   				$.each(data.hotelList,function(i,n){
				   					 $("#hotelUuid").append($("<option/>").text(n.nameCn).attr("value",n.uuid));
				   				});
				   			}else if(type=="island"){
				   				$.each(data.islandList,function(i,n){
				   					 $("#islandUuid").append($("<option/>").text(n.islandName).attr("value",n.uuid));
				   				});
				   			}
				   		}
				   	}
				});
			}
			//航空公司级联查询
			function getAirAjaxSelect(obj){
				$.ajax({
					type: "POST",
				   	url: "${ctx}/flightControl/getSpaceFromAirCom",
				   	data: {
							"airlineCode":$(obj).val()
						  },
					dataType: "json",
					success: function(data){
				   		if(data){
				   			$.each(data.spaceLevel,function(i,n){
				   				 $("select[name=spaceGradelevel]").append($("<option/>").text(n).attr("value",i));
				   			});
				   		}
				   	}
				});
			}
		</script>
	<script  type="text/javascript">
	//上传文件时，点击后弹窗进行上传文件(多文件上传)
	//方法参数：${ctx}、显示上传文件名称input的id、当前点击元素
	function uploadFiles(ctx, inputId, obj) {
		var fls=flashChecker();
		//var s="";
		if(fls.f) {
//			alert("您安装了flash,当前flash版本为: "+fls.v+".x");
		} else {
			alert("请您退出当前浏览器,并安装当前浏览器的flash player软件,然后才能上传文件"); 
			return;
		}
		
		//新建一个隐藏的div，用来保存文件上传后返回的数据
		if($(obj).parent().find(".uploadPath").length == 0) {
			$(obj).parent().append('<div class="uploadPath" style="display: none" ></div>');
			$(obj).parent().append('<div id="currentFiles" style="display: none" ></div>');		
		}
		
		$(obj).addClass("clickBtn");
		
		/*移除产品行程校验提示信息label标签*/
		$("#modIntroduction").remove();
		
		$.jBox("iframe:"+ ctx +"/hotel/uploadFilesPage", {
		    title: "多文件上传",
			width: 340,
	   		height: 365,
	   		buttons: {'关闭':true},
	   		persistent:true,
	   		loaded: function (h) {},
	   		submit: function (v, h, f) {
				$(h.find("iframe")[0].contentWindow.file_upload).next("p").find(".successUpload").click();
				if($(obj).parent().find("#currentFiles").find("[name='docOriName']").length != 0) {
					/*添加<li>之前，先将之前的<li>删除，然后再累加，以防止重复累加问题*/
//					if($(obj).attr("name") != 'costagreement'){
//						$(obj).next(".batch-ol").find("li").remove();
//					}
					
					$(obj).parent().find("#currentFiles").find("[name='docOriName']").each(function(index, obj1) {
						
						$(obj).parent().next(".batch-ol").append('<li><span>'+ $(obj1).val() +'</span><a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(\''+ $(obj1).prev().val() +'\',\''+ $(obj).attr("name") +'\',this);">删除</a></li>');
						
					});
					if($(obj).parent().find("#currentFiles").children().length != 0) {
						$(obj).parent().find("#currentFiles").children().remove();
					}
				}
				
				$(".clickBtn",window.parent.document).removeClass("clickBtn");
	   		}
		});
		$(".jbox-close").hide();
	}
	
	//删除现有的文件
	function deleteFileInfo(inputVal, objName, obj) {
		top.$.jBox.confirm('确定要删除该文件？','系统提示',function(v){
			if(v=='ok'){
				if(inputVal != null && objName != null) {
					var delInput = $(".uploadPath").find("input[name='"+ objName +"'][value='"+ inputVal +"']");
					delInput.next().eq(0).remove();
					delInput.next().eq(0).remove();
					delInput.remove();
					
					/*删除上传文件后，文件信息会存放在id为currentFiles的div中，也需要把该div相关的上传文件信息删除*/
					var docName = $(obj).parent("li").parent("ol").parent().find("#currentFiles").find("input[name='"+ objName +"'][value='"+ inputVal +"']");
					docName.next().eq(0).remove();
					docName.next().eq(0).remove();
					docName.remove();
				
					
				}else if(inputVal == null && objName == null) {
					$(obj).parent().remove();
				}
				$(obj).parent("li").remove();

			}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');		
	}
	</script>
		<style type="text/css">
			.custom-combobox-toggle {
				height: 26px;
				margin: 0 0 0 -1px;
				padding: 0;
				/* support: IE7 */
				
				*height: 1.7em;
				*top: 0.1em;
			}
			.custom-combobox-input {
				margin: 0;
				padding: 0.3em;
				width: 166px;
			}
			.ui-autocomplete {
				height: 200px;
				overflow: auto;
			}
			.sort {
				color: #0663A2;
				cursor: pointer;
			}
			.custom-combobox input[type="text"] {
				height: 26px;
				width: 166px;
			}
			.activitylist_bodyer_table .wdate {
				width: 80px !important;
			}
			.activitylist_bodyer_table .wtext {
				vertical-align: middle;
				margin: 0;
				width: 80px !important;
			}
			.activitylist_bodyer_table .wnum1 {
				width: 30px !important;
				margin: 0;
			}
			.activitylist_bodyer_table .wnum2 {
				width: 50px !important;
				margin: 0;
			}
			.qdgl-cen .batch-label {
				width: 100px;
				cursor: text
			}
			.new_kfang label {
				width: 100px !important;
			}
			.jiange_li {
				color: #08c;
				padding-left: 3px;
				padding-right: 3px;
			}
			.maintain_add p{min-width:300px;}
		</style>
</head>		
<body>
	
	<c:choose>
		<c:when test="${flag==1}">
			<page:applyDecorator name="flight_control_update" >
				<page:param name="current">online</page:param>
			</page:applyDecorator>
			<form id="updateFlightControl" modelAttribute="flightControlInput" action=""  method="post" class="form-horizontal">
				<input type="hidden" name="statuss" />
				<input type="hidden" name="adddateuuid" value="${flightControl.uuid }"/>
				<!-- <input type="hidden" name="flightlevel" value="hotel" /> -->
				<input type="hidden" name="flag" value="${flag }" />
				<div class="ydbz_tit pl20">机票库存新增日期</div>
				<div class="maintain_add">
					<p>
						<label> <span class="xing">*</span>控票名称：</label>
						${flightControl.name }
					</p>
					<p>
						<label> <span class="xing">*</span>国家：</label>
							<trekiz:autoId2Name4Table tableName='sys_geography' sourceColumnName='uuid' srcColumnName='name_cn' value='${flightControl.country}'/>
					</p>
					<p>
						<label> <span class="xing">*</span>岛屿名称：</label>
						<trekiz:autoId2Name4Class classzName="Island" sourceProName="uuid" srcProName="islandName" value="${flightControl.islandUuid }"/>
					</p>
					<p class="maintain_kong"></p>
					<p>
						<label>航空公司：</label>
						${fns:getAirlineNameByAirlineCode(flightControl.airline)}	

					</p>
					<p>
						<label> <span class="xing">*</span>币种选择：</label>
						<select id="currencyId" name="currencyId">
							<c:forEach items="${currencyList}" var="item">
							    <option value="${item.id}" 
							    	<c:choose>
							    		<c:when test="${hotelControl.currencyId==item.id}">selected="selected"</c:when>
							    		<c:when test="${hotelControl.currencyId == null && item.currencyName == '美元'}">selected="selected"</c:when>
							    	</c:choose>>
							    	${item.currencyName}
							    </option>
							</c:forEach>
						</select>
					</p>
					<p class="maintain_kong"></p>
				</div>		
		</c:when>
		<c:otherwise>
			<page:applyDecorator name="flight_control_add" >
				<page:param name="current">online</page:param>
			</page:applyDecorator>
			<form id="addFlightControl" modelAttribute="flightControlInput" action=""  method="post" class="form-horizontal">
			<input type="hidden" name="statuss" />
			<div class="ydbz_tit pl20">新增机票库存</div>
			<div class="maintain_add">
				<p>
					<label> <span class="xing">*</span>控票名称：</label>
					<input type="text" name="name"/>
				</p>
				<p>
					<label> <span class="xing">*</span>国家：</label>
						<trekiz:suggest name="country" style="width:150px;" defaultValue="" displayValue=""  callback="getAjaxSelect('island',$('#country'))"  ajaxUrl="${ctx}/geography/getAllConListAjax" />
				</p>
				<p>
					<label> <span class="xing">*</span>岛屿名称：</label>
					<select name="islandUuid" id="islandUuid" onchange="getAjaxSelect('islandway',this);" ajaxUrl="${ctx}/geography/getAllConListAjax" >
							<option value="">不限</option>
					</select>
				</p>
				<p class="maintain_kong"></p>
				<p>
					<label>航空公司：</label>
					<select id="airline" name="airline" onchange="getAirAjaxSelect($('#airline'));" >
							<option value="">不限</option>
							<c:forEach items="${airlines_list}" var="flightline">
								<option value="${flightline.airlineCode}">${flightline.airlineName}</option>
							</c:forEach>
					</select>
				</p>
				<p>
					<label> <span class="xing">*</span>币种选择：</label>
					<select id="currencyId" name="currencyId">
						<c:forEach items="${currencyList}" var="item">
						    <option value="${item.id}" 
						    	<c:choose>
						    		<c:when test="${hotelControl.currencyId==item.id}">selected="selected"</c:when>
						    		<c:when test="${hotelControl.currencyId == null && item.currencyName == '美元'}">selected="selected"</c:when>
						    	</c:choose>>
						    	${item.currencyName}
						    </option>
						</c:forEach>
					</select>
				</p>
				<p class="maintain_kong"></p>
			</div>								
		</c:otherwise>
	</c:choose>
						
		<table id="contentTable" class="table activitylist_bodyer_table">
					<thead>
						<tr>
							<th width="8%">日期</th>
							<th width="10%">舱位等级</th>
							<th width="10%">价格</th>
							<th width="8%">总税</th>
							<th width="6%">库存</th>
							<th width="6%">参考酒店</th>
							<th width="6%">备注</th>
							<th width="5%">操作</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td class="tc">
								<input type="text" id="groupOpenDate" class="wdate dateinput" name="groupOpenDate" value="" onfocus="WdatePicker()" readonly="readonly" />
							</td>
							<td class="tc">
								<p>
								<c:choose>
									<c:when test="${flag==1 }">
										 <select id="spaceGradelevel" name="spaceGradelevel" ><!--后台 spaceGradeTypes -->
											<option value="">不限</option>
											<c:forEach items="${spaceGradelist}" var="spaceGrade">
												<option value="${spaceGrade.key}">${spaceGrade.value}</option>
											</c:forEach>
										</select>
									</c:when>
									<c:otherwise>
										 <select id="spaceGradelevel" name="spaceGradelevel" ><!--后台 spaceGradeTypes -->
											<option value="">不限</option>
										 </select>
									</c:otherwise>
								</c:choose>
							     
								</p>
							</td>
							<td class="tc">
								<p>
								    <select name="priceCurrencysId" class="wnum2 currency">
										<c:forEach items="${currencyList}" var="item">
										    <option value="${item.id}"> ${item.currencyMark} </option>
										</c:forEach>
			                        <input type="text" class="wnum2" />
								</p>
							</td>
							<td class="tc">
									<select name="taxesCurrencysId" class="wnum2 currency">
										<c:forEach items="${currencyList}" var="item">
										    <option value="${item.id}"> ${item.currencyMark} </option>
										</c:forEach>
									</select>
								  <input type="text" class="wnum2"  />
						    </td>
							<td class="tc"><input type="text" class="wtext" name="kucun"/></td>
							<td class="tc">
								  <p>
									
									<c:choose>
										<c:when test="${flag==1}">
											<select name=hotelUuid id="hotelUuid" >
												<option>不限</option>
												<c:forEach items="${hotelmap}" var="item">
													<option value="${item.uuid }">${item.nameCn}</option>
												</c:forEach>
											</select>
										</c:when>
										<c:otherwise>
											<select name=hotelUuid id="hotelUuid" onchange="getAjaxSelect('roomtype',this);">
												<option selected="selected">不限</option>
											</select>
										</c:otherwise>
									</c:choose>
			                        <a class="addHotel" href="javascript:void(0);">新增</a>
			                        </p>
		                      </td>
							<td class="tc">
								   <input type="text" class="wtext" name="remark"/>
							</td>
							<td class=" tc p0">
								   <a class="saveAndNew" href="javascript:void(0)">保存并新增</a>
							</td>
						</tr>
					</tbody>
			</table>
			<!--右侧内容部分结束-->
			<div class="sysdiv sysdiv_coupon">
				<p class="maintain_pfull new_kfang">
					<label>上传附件：</label>
					<input type="button" name="hotelAnnexDocId" value="上传" class="btn btn-primary" onClick="uploadFiles('${ctx}',null,this)"/> <em class="tips_color">多文件，多格式上传附件。单个附件不大于20M，总共不大于20M。</em>
					<ol class="batch-ol"></ol>
				</p>
				<p class="maintain_pfull new_kfang">
					<label>备注：</label>
					<textarea name="memo" class="madintain_text" placeholder=""></textarea>
				</p>
			</div>
			<div class="release_next_add">
				<c:choose>
					<c:when test="${flag==1}">
						<input type="button" class="btn btn-primary gray" value="取消">
						<input id="updateDraftSubmit" type="submit" class="btn btn-primary" value="保存草稿">
						<input id ="updateSubmit" type="submit" class="btn btn-primary" value="提交">
					</c:when>
					<c:otherwise>
						<input type="button" class="btn btn-primary gray" value="取消">
						<input id="saveDraftSubmit" type="submit" class="btn btn-primary" value="保存草稿">
						<input id ="saveSubmit" type="submit" class="btn btn-primary" value="提交">
					</c:otherwise>
				</c:choose>
			</div>
	</form>
	
</body>
	
</html>