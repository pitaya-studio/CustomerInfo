<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<html>
<head>
	<c:choose>
			<c:when test="${flag==1}">
				<title>控房新增日期信息</title>
			</c:when>
			<c:otherwise>
				<title>新增控房信息</title>
			</c:otherwise>
		</c:choose>
	
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	
	<script type="text/javascript" src="${ctxStatic}/js/little_logo.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/default.validator.js"></script>
	
	<script type="text/javascript">
		$(function() {
			//搜索条件筛选
			launch();
			//产品名称文本框提示信息
			inputTips();
			//操作浮框
			operateHandler();
			
			//新增控房日期时
			if('${flag}' == '1') {
				//初始化房型相关信息
				getAjaxSelect('roomtype', '${hotelControl.hotelUuid }');
            	
            	$.ajax({
					type: "POST",
				   	url: "${ctx}/hotelControl/getHotelRoomMealsData",
				   	data: {
							"hotelUuid":'${hotelControl.hotelUuid }'
						  },
					dataType: "json",
				   	success: function(data){
				   		if(data.hotelRoomMeal){
				   			allMealTypes = jQuery.parseJSON(data.hotelRoomMeal);
				   		}
				   	}
				});
				
				//初始化上岛方式数据
				getAjaxSelect('islandway', '${hotelControl.islandUuid }');
				
			//新增控房时
			} else {
				//初始化国家的标签,出错之后，下面的就不能加载，所以放在最后
				initSuggest({});
				//初始化马尔代夫下的岛屿
				getAjaxSelect('island',$('#country').val());
			}
			
			//点击新增库存的保存按钮
			$("#hotelControlInputForm").validate({
				rules:{
					
				},
				submitHandler: function(form){
					var url="${ctx}/hotelControl/saveHotelControl" ;
					//在此验证必输的数据
					if(!validateNum('${flag}')){
						return ;
					}
					
					//airlines;//航空公司，多个航空公司用“;”分隔
					$("input[name=airlines]").each(function(){
						var airlineArray = $(this).parent().find("input[name=airline]");
						
						var airlines = '';
						for(var i=0; i<airlineArray.length; i++) {
							airlines += $(airlineArray[i]).val();
							if(i != airlineArray.length-1) {
								airlines += ';';
							}
						}
						$(this).val(airlines);
					});
					
					//rooms;//房型和晚数拼接字符串的数组；格式如[“水上屋uuid*1晚数-沙滩屋uuid*2晚数”，“水上屋uuid*1晚数-沙滩屋uuid*2晚数”]
					$("input[name=rooms]").each(function(){
						var roomTypeArray = $(this).parent().find("select[name=roomType]");
						
						var nightArray = $(this).parent().find("input[name=night]");
						var rooms = '';
						for(var i=0; i<roomTypeArray.length; i++) {
							rooms += $(roomTypeArray[i]).val();
							rooms += '*';
							if($(nightArray[i]).val() != '') {
								rooms += $(nightArray[i]).val();
							} else {
								rooms += '0';
							}
							
							if(i != roomTypeArray.length-1) {
								rooms += '-';
							}
						}
						$(this).val(rooms);
					});
					
					//hotelMeals;//餐型；格式如[“餐型uuid1;餐型uuid2-餐型uuid3;餐型uuid4”，“餐型uuid5;餐型uuid6-餐型uuid7;餐型uuid8”]
					$("#contentTable tbody").each(function(){
						var hotelMeals = '';
						$(this).find("tr").each(function(){
							var hotelMealArr = $(this).find("td[name=mealType]").find("select.wtext");
							for(var i=0; i<hotelMealArr.length; i++) {
								hotelMeals += $(hotelMealArr[i]).val();
								if(i != hotelMealArr.length-1) {
									hotelMeals += ";";
								}
							}
							hotelMeals += "-";
						});
						
						if(hotelMeals != '') {
							hotelMeals = hotelMeals.substring(0, hotelMeals.length-1);
						}
						
						$(this).find("input[name=hotelMeals]").val(hotelMeals);
					});
					
					$.post(url,$("#hotelControlInputForm").serialize(),function(data){
						if(data.message=="1"){
							//$("#searchForm",window.opener.document).submit();
							$.jBox.tip("添加成功!");
							setTimeout(function(){window.close();},500);
							window.opener.location.reload();
						} else if(data.message=="2"){
							//$("#searchForm",window.opener.document).submit();
							$.jBox.tip("修改成功!");
							setTimeout(function(){window.close();},500);
							window.opener.location.reload();
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
		});
		
		//进行数据验证
		function validateNum(flag){
			var strflag = true;
			if(flag==0){
				var hotelname=$("input[name=name]").val();
				if(hotelname==null||hotelname==""){
					$.jBox.tip("控房名称不能为空,请填写控房名称!");
					return false;
				}
				
				var country=$("input[name=country]").val();
				if(country==null||country==""){
					$.jBox.tip("国家不能为空,请选择国家!");
					return false;
				}

				/* var islanduuid=$("select[name=islandUuid]").val();
				if(islanduuid==null||islanduuid==""){
					$.jBox.tip("岛屿名称不能为空,请选择岛屿!");
					return false;
				} */
				
				var airline=$("select[name=hotelUuid]").val();
				if(airline==null||airline==""){
					$.jBox.tip("酒店名称不能为空,请选择酒店名称!");
					return false;
				}
			}
			var airline=$("select[name=currencyId]").val();
			if(airline==null||airline==""){
				$.jBox.tip("币种选择不能为空,请选择币种!");
				return false;
			}
			if($("input[name=inDates]").length<1){
				$.jBox.tip("新增控房没有控房记录，请添加控房记录并保存！");
				return false;
			}
			$.each($("input[name=inDates]"), function() {
				var date = $(this).val();
				if(date==null||date==""){
					$.jBox.tip("入住日期不能为空,请为日期为空的明细选择日期!");
					strflag=false;
				}
			});
			
			return strflag;
		}
		
		//酒店控房保存草稿
		function saveHotelControl() {
			$("input[name=statuss]").val(1);
			$("#hotelControlInputForm").submit();
		}
		
		//酒店控房提交
		function commitHotelControl() {
			$("input[name=statuss]").val(0);
			$("#hotelControlInputForm").submit();
		}
		
		//级联查询
		function getAjaxSelect(type,objVal){
			$.ajax({
				type: "POST",
			   	url: "${ctx}/hotelControl/ajaxCheck",
			   	data: {
						"type":type,
						"uuid":objVal
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
			   		} else if(type == "island"){
			   			$("select[name=islandUuid]").empty();
				   		$("select[name=islandUuid]").append("<option value=''>不限</option>");
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
			   			}else if(type=="islandway"){
			   				/* $.each(data.listIslandWay,function(i,n){
			   					 var arr = $("select[name=islandWays]");
			   					 for(var j=0; j<arr.length; j++) {
			   					 	$(arr[j]).append($("<option/>").text(n.label).attr("value",n.uuid));
			   					 }
			   				}); */
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

           a {
               display: inline-block;
           }
	</style>
	<script type="text/javascript">
        $.fn.extend({
            selectedText:function(){
                var $sl=$(this);
                return $(this).find('option:selected').text();
            }
        });
        var allMealTypes={};
    </script>
    <script type="text/javascript">
        $(document).ready(function() {
            var $contentTable = $("#contentTable");
            $("#slSupplier").comboboxInquiry();
            $("#slSupplier").comboboxInquiry('showTitle').on('select', function(event, obj){
            	//add by wangXK 20150819
            	var $input = $("#slSupplier").next().find("input");
            	$input.data('ui-tooltip-title',$("#slSupplier").find('option:selected').text());
                $input.tooltip('close');
     		});
            
            $("select[name='islandUuid']").comboboxInquiry().on('comboboxinquiryselect', function(event, obj){
            	
            	getAjaxSelect('islandway',$("select[name='islandUuid']").val());
            	//add by wangXK 20150819
            	var $input = $("select[name='islandUuid']").next().find("input");
            	$input.data('ui-tooltip-title',$("select[name='islandUuid']").find('option:selected').text());
                $input.tooltip('close');
     		});
            
            $("select[name='islandUuid']").comboboxInquiry('showTitle');
            
            $("select[name='hotelUuid']").comboboxInquiry().on('comboboxinquiryselect', function(event, obj){
            	getAjaxSelect('roomtype',$("select[name='hotelUuid']").val());
            	
            	$.ajax({
					type: "POST",
				   	url: "${ctx}/hotelControl/getHotelRoomMealsData",
				   	data: {
							"hotelUuid":$("#hotelUuid").val()
						  },
					dataType: "json",
				   	success: function(data){
				   		if(data.hotelRoomMeal){
				   			allMealTypes = jQuery.parseJSON(data.hotelRoomMeal);
				   		}
				   	}
				});
            	//add by wangXK 20150819
            	var $input = $("select[name='hotelUuid']").next().find("input");
            	$input.data('ui-tooltip-title',$("select[name='hotelUuid']").find('option:selected').text());
                $input.tooltip('close');
     		});
            $("select[name='hotelUuid']").comboboxInquiry('showTitle');
            
            $("select[name='currency']").on('change',function(){
                var currencyCode= $("select[name='currency']").val();
                $contentTable.find('td[name="housePrice"] [name="slValue"]').val(currencyCode);
                $contentTable.find('td[name="housePrice"] select').val(currencyCode);
                $contentTable.find('td[name="housePrice"] [name="slText"]').text(currencyCode);
                if($editTempTbody){
                    $editTempTbody.find('td[name="housePrice"] [name="slValue"]').val(currencyCode);
                    $editTempTbody.find('td[name="housePrice"] select').val(currencyCode);
                    $editTempTbody.find('td[name="housePrice"] [name="slText"]').text(currencyCode);
                }
            });

            $contentTable.on("click", '[name="houseType"] a[data-delTag]', function() {
                // 删除房型
                $(this).parents('tr:first').remove();
                resetRowSpan();
            }).on("click", '[name="flightNo"] a[data-deltag]', function() {
                // 删除航班
                $(this).parent().remove();
            }).on("click", '[name="mealType"] a[data-deltag]', function() {
                // 删除餐型
                $(this).parent().remove();
            }).on("click", '[name="operation"] a[data-deltag]', function() {
                $(this).parents('tbody:first').remove();
                $contentTable.trigger('rowCountChange');
            }).on('click', 'a.saveAndNew', function() {
                // 保存并新增
                var $tbody = $(this).parents('tbody:first');
                var $newTbody = copyTbody($tbody);
                $contentTable.append($newTbody);
                saveTbody($tbody);
                $contentTable.trigger('rowCountChange');
            }).on('click', 'a.new', function() {
                // 新增
                var $tbody = $contentTable.find('tbody:first');
                var $newTbody = copyTbody($tbody)
                initTbody($newTbody);
                $contentTable.append($newTbody);
                $contentTable.trigger('rowCountChange');
            }).on('click', 'a.save', function() {
                // 保存
                saveTbody($(this).parents('tbody:first'));
            }).on('click', 'a.edit', function() {
                // 编辑
                editTbody($(this).parents('tbody:first'));
            }).on('click', 'a.cancel', function() {
                // 取消
                cancelEdit($(this).parents('tbody:first'));
            }).on('click', 'a.addFlight', function() {
                //增加航班
                addFlight($(this).parent().parent());
            }).on('click', 'a.addHouseType', function() {
                //增加房型
                addHouseType($(this).parent().parent());
            }).on('click', 'a.addMealType', function() {
                //增加餐型
                addMealType($(this).parent().parent());
            }).on('change','td[name="houseType"] select',function(){
                var $slHouseType=$(this);
                var $mealTypes = $slHouseType.parents('tr:first').find('td[name="mealType"] select');
                $mealTypes.empty();
                var mealTypes =allMealTypes[$slHouseType.val()];
                for(var index in mealTypes){
                    $mealTypes.append('<option value="'+mealTypes[index].hotelMealUuid+'">'+mealTypes[index].hotelMealName+'</option>');
                };
                $slHouseType.attr("title",$slHouseType.find("option:selected").text());
            });

            $contentTable.on('rowCountChange',function(){
                if($contentTable.find('tbody tr').length==1){
                    $contentTable.find('td[name="operation"] a[data-deltag]').hide();
                    $contentTable.find('td[name="operation"] a[data-deltag]').prev().hide();
                }else{
                    $contentTable.find('td[name="operation"] a[data-deltag]').show();
                    $contentTable.find('td[name="operation"] a[data-deltag]').prev().show();
                }
            });
            $contentTable.trigger('rowCountChange');
            $contentTable.find('td[name="houseType"] select').trigger('change');

            // 复制
            function copyTbody($tbody) {
            /*     $tbody.find('select').each(function(){
                    var $sl = $(this);
                    $sl.after('<span name="slText">'+$sl.selectedText()+'</span>');
                    $sl.after('<input type="hidden" name="slValue" value="'+$sl.val()+'"/>');
                });
                $tbody.find('input[type="text"]').each(function(){
                    var $text = $(this);
                    $text.after('<span name="inputText">'+$text.val()+'</span>');
                }); 
           */
                var $newTbody =$tbody.clone();
                $newTbody.find('[name="slValue"]').each(function(){
                    var $this = $(this);
                    $this.siblings('select').val($this.val());
                });
                $newTbody.find('[name="inputText"]').each(function(){
                    var $this = $(this);
                    $this.siblings('input[type="text"]').val($this.text());
                });
                //$tbody.find('[ name="slText"],[name="slValue"],[name="inputText"]').remove();
                $newTbody.find('[ name="slText"],[name="slValue"],[name="inputText"]').remove();
                return $newTbody;
            }

            // 保存
            function saveTbody($tbody) {
                $tbody.find('select').each(function(){
                    var $sl = $(this);
                    $sl.after('<span name="slText">'+$sl.selectedText()+'</span>');
                    $sl.after('<input type="hidden" name="slValue" value="'+$sl.val()+'"/>');
                    $sl.hide();
                });
                $tbody.find('input[type="text"]').each(function(){
                    var $text = $(this);
                    $text.after('<span name="inputText">'+$text.val()+'</span>');
                    $text.hide();
                });
                $tbody.find('td[name="operation"] [name="new"]').hide();
                $tbody.find('td[name="operation"] [name="edit"]').hide();
                $tbody.find('td[name="operation"] [name="text"]').show();
                $tbody.find('td:not([name="operation"]) a').hide();
                $editTempTbody=null;
            }

            var $editTempTbody;//编辑状态下的临时tbody,在取消的时候用来恢复之前的数据
            // 编辑
            function editTbody($tbody) {
                $editTempTbody = $tbody.clone();
                $tbody.find('[ name="slText"],[name="slValue"],[name="inputText"]').remove();
                $tbody.find('select,input').show();
                $tbody.find('td[name="operation"] [name="edit"]').show();
                $tbody.find('td:not([name="operation"]) a').show();

                $tbody.find('[name="slText"]').hide();
                $tbody.find('[name="inputText"]').hide();
                $tbody.find('td[name="operation"] [name="text"]').hide();
            }
            //取消
            function cancelEdit($tbody){
                var $oldTbody = $editTempTbody.clone();
                $editTempTbody=null;
                $oldTbody.find('select,input').hide();
                $oldTbody.find('td[name="operation"] [name="edit"]').hide();
                $oldTbody.find('td:not([name="operation"]) a').hide();

                $oldTbody.find('[name="slText"]').show();
                $oldTbody.find('[name="inputText"]').show();
                $oldTbody.find('td[name="operation"] [name="text"]').show();
                $oldTbody.find('[name="slValue"]').each(function(){
                    var $slValue = $(this);
                    $slValue.siblings('select').val($slValue.val());
                });
                $oldTbody.find('[name="inputText"]').each(function(){
                    var $inputText = $(this);
                    $inputText.siblings('input[type="text"]').val($inputText.text());
                });
                $tbody.after($oldTbody);
                $tbody.remove();
            }
            // 增加航班
            function addFlight($td, val) {
                var $newP = $td.find("p:first").clone();
                $newP.find("a").replaceWith('<a href="javascript:;" data-delTag="p">删除</a>');
                if (val) {
                    $newP.find("input.wtext").val(val);
                }
                $td.append($newP);
                $newP.find("input").val('');
            }

            /// 增加房型
            function addHouseType($td) {
                var $newHouseType = $td.clone();
                $newHouseType.find("a").replaceWith('<a href="javascript:;" data-delTag="p">删除</a>');
                $newHouseType.find("input.wnum1").val(1);
                
                var $newMealType=$td.next().clone();
                $newMealType.find("select.wtext").empty();
                
                var $newTr = $('<tr></tr>');
                $newTr.append($newHouseType).append($newMealType);
                $newTr.find('td[name="mealType"]').children(':gt(1)').remove();
                $td.parents('tbody:first').append($newTr);
                resetRowSpan();
            }
            //新增餐型
            function addMealType($td, type) {
                var $newP = $td.find("p:first").clone();
                $newP.find("a").replaceWith('<a href="javascript:;" data-delTag="p">删除</a>');
                if (type) {
                    $newP.find("select.wtext").val(type);
                }
                $td.append($newP);
            }

            //初始化一个新的tbody,以便新增
            function initTbody($tbody){
                $tbody.find('tr:gt(0)').remove();
                $tbody.find('td[name="flightNo"] p:gt(0)').remove();
                $tbody.find('td[name="mealType"] p:gt(0)').remove();
                $tbody.find('select').val('');
                $tbody.find('input[type="text"]').val('');

                $tbody.find('td[name="housePrice"] select').val($('[name="currency"]').val());
                //房型的晚数默认是1
                $tbody.find('td[name="houseType"] .wnum1').val(1);

                $tbody.find('[ name="slText"],[name="slValue"],[name="inputText"]').remove();
                $tbody.find('select,input').show();
                $tbody.find('td[name="operation"] [name="new"]').show();
                $tbody.find('td[name="operation"] [name="edit"]').hide();
                $tbody.find('td[name="operation"] [name="text"]').hide();
                $tbody.find('td:not([name="operation"]) a').show();
            }


            function resetRowSpan(){
                var $contentTable = $('#contentTable');
                $contentTable.find('tbody').each(function(){
                    var $tbody=$(this);
                    var rowspan=$tbody.children().length;
                    $tbody.find('td:not([name="houseType"],[name="mealType"])').each(function(){
                        $(this).attr('rowspan',rowspan);
                    });
                });
            }
        });
    </script>
    <script  type="text/javascript">
	//上传文件时，点击后弹窗进行上传文件(多文件上传)
	//方法参数：${ctx}、显示上传文件名称input的id、当前点击元素
	function uploadFile(ctx, inputId, obj) {
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
</head>
<body>
	<page:applyDecorator name="hotel_control_add" >
		<page:param name="current">online</page:param>
	</page:applyDecorator>
	
	<div>
	<form:form id="hotelControlInputForm" modelAttribute="hotelControlInput" action=""  method="post" class="form-horizontal" novalidate="">
		<input type="hidden" name="flag" value="${flag}"/>
		<!--右侧内容部分开始-->
		<div class="mod_nav">库存管理 &gt; 控房管理 &gt; 新增控房</div>
			<div class="maintain_add">
				<c:choose>
					<c:when test="${flag==0}">
						<p>
							<label> <span class="xing">*</span>控房名称：</label>
							<input type="text" name="name" htmlEscape="false" maxlength="32" class="wtext"/>
						</p>
						<p>
							<label> <span class="xing">*</span>地接供应商：</label> 
							<select id="slSupplier" name="groundSupplier">
								<c:forEach items="${supplierInfos }" var="supplierInfo">
									<option value="${supplierInfo.id }">${supplierInfo.supplierName }</option>
								</c:forEach>
							</select>
						</p>
						<p class="maintain_kong"></p>
						<p>
							<label> <span class="xing">*</span>国家：</label> 
							<trekiz:suggest name="country" style="width:150px;" defaultValue="80415d01488c4d789494a67b638f8a37" displayValue=""  callback="getAjaxSelect('island',$('#country').val())"  ajaxUrl="${ctx}/geography/getAllConListAjax" />
						</p>
						<p>
							<label> <span class="xing">*</span>岛屿名称：</label> 
							<select name="islandUuid" id="islandUuid" >
								<option value="">不限</option>
							</select>
						</p>
						<p>
							<label> <span class="xing">*</span>采购类型：</label> 
							<select name="purchaseType">
								<option value="0">内采</option>
								<option value="1">外采</option>
							</select>
						</p>
						<p class="maintain_kong"></p>
			
						<p>
							<label>酒店集团：</label> 
							<trekiz:defineDict name="hotelGroup" type="hotel_group" defaultValue="${hotelControl.hotelGroup}" />
						</p>
						<p>
							<label> <span class="xing">*</span>酒店名称：</label> 
							<select name="hotelUuid" id="hotelUuid">
								<option value="">不限</option>
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
					</c:when>
					<c:when test="${flag==1}">
						<input type="hidden" name="uuid" value="${hotelControl.uuid }"/>
						<p>
							<label> <span class="xing">*</span>控房名称：</label>
							${hotelControl.name }
						</p>
						<p>
							<label> <span class="xing">*</span>地接供应商：</label> 
							<trekiz:autoId2Name4Table tableName='supplier_info' sourceColumnName='id' srcColumnName='supplierName' value='${hotelControl.groundSupplier }'/>
						</p>
						<p class="maintain_kong"></p>
						<p>
							<label> <span class="xing">*</span>国家：</label> 
							<trekiz:autoId2Name4Table tableName='sys_geography' sourceColumnName='uuid' srcColumnName='name_cn' value='${hotelControl.country}'/>
						</p>
						<p>
							<label> <span class="xing">*</span>岛屿名称：</label> 
							<trekiz:autoId2Name4Class classzName="Island" sourceProName="uuid" srcProName="islandName" value="${hotelControl.islandUuid }"/>
						</p>
						<p>
							<label> <span class="xing">*</span>采购类型：</label> 
							<c:choose>
								<c:when test="${hotelControl.purchaseType==0}">内采</c:when>
								<c:when test="${hotelControl.purchaseType==1}">外采</c:when>
							</c:choose>
						</p>
						<p class="maintain_kong"></p>
			
						<p>
							<label>酒店集团：</label> 
							<trekiz:defineDict name="hotelGroup" type="hotel_group" defaultValue="${hotelControl.hotelGroup}" readonly="true" />
						</p>
						<p>
							<label> <span class="xing">*</span>酒店名称：</label> 
							<trekiz:autoId2Name4Class classzName="Hotel" sourceProName="uuid" srcProName="nameCn" value="${hotelControl.hotelUuid }"/>
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
					</c:when>
				</c:choose> 
			</div>
		<table id="contentTable" class="table activitylist_bodyer_table">
			<thead>
				<tr>
					<th width="8%">入住日期</th>
					<th width="10%">航班</th>
					<th width="10%">房型 * 晚数</th>
					<th width="7%">餐型</th>
					<th width="6%">上岛方式</th>
					<th width="8%">房间总价</th>
					<th width="6%">现有库存（间）</th>
					<th width="6%">备注</th>
					<th width="8%">操作</th>
				</tr>
			</thead>
			<tbody>
				<input type="hidden" name="rooms" />
				<tr>
					<input type="hidden" name="hotelMeals" />
					<td class="tc" name="indate">
						<input type="hidden" name="statuss" />
						<input type="text" id="groupOpenDate" class="wdate dateinput" name="inDates" value="" onfocus="WdatePicker()" readonly="readonly" />
					</td>
					<td class="tc" name="flightNo">
						<input type="hidden" name="airlines" />
						<p>
							<input type="text" name="airline" class="wtext" maxlength="10" />
							<a class="addFlight" href="javascript:;">新增</a>
						</p>
					</td>
					<td class="tc" name="houseType">
						<p>
							<span> 
								<select class="wtext" name="roomType" id="roomType"></select>
							</span>* 
							<span> 
								<input type="text" class="wnum1" value="1" name="night" maxlength="3"/>
							</span>晚
							<a class="addHouseType" href="javascript:;">新增</a>
						</p></td>
					<td class="tc" name="mealType">
						<p>
							<select class="wtext">
							</select> 
							<a class="addMealType" href="javascript:;">新增</a>
						</p>
					</td>
					<td class="tc" name="islandWay">
						<trekiz:defineDict id="islandWays" name="islandWays" type="islands_way" className="wtext" />
					</td>
					<td class="tc" name="housePrice">
						<select name="detailCurrencys" class="wnum2 currency">
							<c:forEach items="${currencyList}" var="item">
							    <option value="${item.id}" 
							    	<c:choose>
							    		<c:when test="${detail.currencyId==item.id}">selected="selected"</c:when>
							    		<c:when test="${detail.currencyId == null && item.currencyName == '美元'}">selected="selected"</c:when>
							    	</c:choose>>
							    	${item.currencyMark}
							    </option>
							</c:forEach>
						</select>
						<input type="text" class="wnum2" name="totalPrices"/>
					</td>
					<td class="tc" name="stockQty">
						<input type="text" class="wnum1" name="stocks" maxlength="3"/>
					</td>
					<td class="tc" name="memo">
						<input type="text" class="wtext" name="memos" maxlength="255"/>
					</td>
					<td class=" tc p0" name="operation">
						<div name="new">
							<a class="save" href="javascript:void(0)">保存</a> 
							<span class="jiange_li">|</span> 
							<a class="saveAndNew" href="javascript:void(0)">保存并新增</a> 
							<span class="jiange_li">|</span>
							<a data-deltag="tr" href="javascript:void(0)">删除</a>
						</div>
						<div name="edit" style="display: none">
							<a class="save" href="javascript:void(0)">保存</a>
							<span class="jiange_li">|</span>
							<a class="cancel" href="javascript:void(0)">取消</a>
						</div>
						<div name="text" style="display: none">
							<a class="new" href="javascript:void(0)">新增</a>
							<span class="jiange_li">|</span>
							<a class="edit" href="javascript:void(0)">修改</a>
							<span class="jiange_li">|</span>
							<a data-deltag="tr" href="javascript:void(0)">删除</a>
						</div></td>
	
				</tr>
			</tbody>
		</table>
		<!--右侧内容部分结束-->
		<div class="sysdiv sysdiv_coupon">
			<p class="maintain_pfull new_kfang">
				<label>上传附件：</label>
				<input type="button" name="hotelAnnexDocId" value="上传" class="btn btn-primary" onClick="uploadFile('${ctx}',null,this)"/> <em class="tips_color">多文件，多格式上传附件。单个附件不大于20M，总共不大于20M。</em>
					<ol class="batch-ol">
						<c:forEach items="${haList}" var="file" varStatus="s1">
							<li>
								<span>${file.docName}</span>
								<input type="hidden" name="hotelAnnexDocId" value="${file.docId}"/>
								<input type="hidden" name="docOriName" value="${file.docName}"/>
								<input type="hidden" name="docPath" value="${file.docPath}"/>
								<a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(null,'hotelAnnexDocId',this)">删除</a>
							</li>
						</c:forEach>
					</ol>
			</p>
			<p class="maintain_pfull new_kfang">
				<label>备注：</label>
				<c:choose>
					<c:when test="${flag == 0}"><textarea name="memo" class="madintain_text" placeholder="" maxlength="255"></textarea></c:when>
					<c:when test="${flag == 1}">${hotelControl.memo }</c:when>
				</c:choose>
				
			</p>
		</div>
		<div class="release_next_add">
			<input type="button" class="btn" value="取消" onclick="window.close();"><%--bug 17531 去掉 class gray和btn-primary --%>
			<input type="button" class="btn btn-primary" value="保存草稿" onclick="saveHotelControl()"> 
			<input type="button" class="btn btn-primary" value="提交" onclick="commitHotelControl()">
		</div>
		</form:form>
	</div>
</body>
</html>