<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>控房-新增控房-控房修改</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<!--[if lte IE 6]>
		<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
		<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
	<![endif]-->
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />

	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/default.validator.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			/* inputTipText(); */
			$("#inputForm").validate({
				rules:{
					
				},
				submitHandler: function(form){
					
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
					$("#contentTable tbody").each(function(){
						var roomTypeArray = null;
						roomTypeArray = $(this).find("select[name=roomType]");
						if(!roomTypeArray) {
							roomTypeArray = $(this).find("input[name=roomType]");
						}
						
						var nightArray = $(this).find("input[name=night]");
						
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
						
						$(this).find("input[name=rooms]").val(rooms);
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
					
					//组装酒店控房航空公司uuids
					$("input[name=airlineUuids]").each(function(){
						var airlineUuidArray = $(this).parent().find("input[name=airlineUuid]");
						
						var airlineUuids = '';
						for(var i=0; i<airlineUuidArray.length; i++) {
							airlineUuids += $(airlineUuidArray[i]).val();
							if(i != airlineUuidArray.length-1) {
								airlineUuids += ';';
							}
						}
						$(this).val(airlineUuids);
						
					});
					
					//组装酒店控房房型uuids
					$("#contentTable tbody").each(function(){
						var $roomUuids = $(this).find("input[name=roomUuids]");
						var roomUuids = '';
							
						var roomUuidArray = $(this).find("tr input[name=roomUuid]");
						
						for(var i=0; i<roomUuidArray.length; i++) {
							roomUuids += $(roomUuidArray[i]).val();
							if(i != roomUuidArray.length-1) {
								roomUuids += ';';
							}
						}
						
						$roomUuids.val(roomUuids);
					});
					
					var url="${ctx}/hotelControl/updateHotelControl";
					
					$("#btnSubmit").attr("disabled", "disabled");
					$.post(url,$("#inputForm").serialize(),function(data){
						if(data.message=="2"){
							$("#searchForm",window.opener.document).submit();
							$.jBox.tip("修改成功!");
							setTimeout(function(){window.close();},500);
						}else if(data.message=="3"){
							$.jBox.tip(data.error,'warning');
							$("#btnSubmit").attr("disabled", false);
						}else{
							$.jBox.tip('系统异常，请重新操作!','warning');
							$("#btnSubmit").attr("disabled", false);
						}
					});
				}
			});
			
			//初始化国家自定义标签
			if('${editFlag}' == 'true'){
				initSuggest({});
			}
			
			//绑定修改币种事件
			$("#currency").change(function(){
				var arr = $("select[name=detailCurrencys]");
				for(var j=0; j<arr.length; j++) {
					$(arr[j]).val($("#currency").val());
				}
			});
			
			var allMealTypes = jQuery.parseJSON('${hotelRoomMeal}');
			
			//当控房详情信息中房型是下拉框时
			$('td[name=houseType] select').on('change', function(){
                var $slHouseType=$(this);
                var $mealTypes = $slHouseType.parents('tr:first').find('td[name="mealType"] select');
                $mealTypes.empty();
                var mealTypes =allMealTypes[$slHouseType.val()];
                for(var index in mealTypes){
                    $mealTypes.append('<option value="'+mealTypes[index].hotelMealUuid+'">'+mealTypes[index].hotelMealName+'</option>');
                }
            }).change();
            
            //当控房详情信息中房型是文本时
			$('td[name=houseType] input[name=roomType]').each(function(){
				var $inRoomType=$(this);
				var $mealTypes = $inRoomType.parents('tr:first').find('td[name="mealType"] select');
                $mealTypes.empty();
                var mealTypes =allMealTypes[$inRoomType.val()];
                for(var index in mealTypes){
                    $mealTypes.append('<option value="'+mealTypes[index].hotelMealUuid+'">'+mealTypes[index].hotelMealName+'</option>');
                }
			});
            
            var roomMealsRel = jQuery.parseJSON('${roomMealsRel}');
            
            $('input[name=roomUuid]').each(function(){
            	var roomMap = roomMealsRel[$(this).val()];
            	
            	var $roomTd = $(this).parent();
            	
            	//当控房详情信息中房型是下拉框时
           		$roomTd.find("select[name=roomType]").each(function(){
            		var $mealTd = $roomTd.next();
            		var hotelMealArr = roomMap[$(this).val()];
            		var hotelMealP = $mealTd.find('p').clone();
                	hotelMealP.find("a").replaceWith('<a href="javascript:;" data-delTag="p">删除</a>');
            		
            		for(var i=0; i<hotelMealArr.length; i++) {
            			if(i>0) {
							$mealTd.append('<p>'+hotelMealP.html()+'</p>');
            			}
            		}
            		var j=0;
            		$mealTd.find('select').each(function(){
            			$(this).val(hotelMealArr[j++]);
            		});
            	});
            	
            	//当控房详情信息中房型是文本时
           		$roomTd.find("input[name=roomType]").each(function(){
            		var $mealTd = $roomTd.next();
            		var hotelMealArr = roomMap[$(this).val()];
            		var hotelMealP = $mealTd.find('p').clone();
                	hotelMealP.find("a").replaceWith('<a href="javascript:;" data-delTag="p">删除</a>');
            		
            		for(var i=0; i<hotelMealArr.length; i++) {
            			if(i>0) {
							$mealTd.append('<p>'+hotelMealP.html()+'</p>');
            			}
            		}
            		var j=0;
            		$mealTd.find('select').each(function(){
            			$(this).val(hotelMealArr[j++]);
            		});
            	});
            	
            	
            });
            
            $('a.addMealType').on('click', function() {
                //增加餐型
                addMealType($(this).parent().parent());
            });
            
            $('[name="mealType"] a[data-deltag]').on("click", function() {
                // 删除餐型
                $(this).parent().remove();
            });
            
		});
		
		//新增餐型
        function addMealType($td, type) {
            var $newP = $td.find("p:first").clone();
            $newP.find("a").replaceWith('<a href="javascript:;" data-delTag="p" onclick="delMealType(this);">删除</a>');
            if (type) {
                $newP.find("select.wtext").val(type);
            }
            $td.append($newP);
        }
        
        // 删除餐型
        function delMealType(obj) {
            // 删除餐型
            $(obj).parent().remove();
        }
	</script>
	<script type="text/javascript">
		//级联查询
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
			   			$("#hotelUuid").empty();
				   		$("#hotelUuid").append("<option value=''>不限</option>");
			   		} else if(type == "roomtype"){
			   			$("select[name=roomType]").empty();
				   		$("select[name=roomType]").append("<option value=''>不限</option>");
			   		} else if(type == "island"){
			   			$("select[name=islandUuid]").empty();
				   		$("select[name=islandUuid]").append("<option value=''>不限</option>");
				   		
			   			$("#hotelUuid").empty();
				   		$("#hotelUuid").append("<option value=''>不限</option>");
				   		
			   			$("select[name=roomType]").empty();
				   		$("select[name=roomType]").append("<option value=''>不限</option>");
				   		
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
	
	<script>
	//上传文件时，点击后弹窗进行上传文件(多文件上传)
	//方法参数：${ctx}、显示上传文件名称input的id、当前点击元素
	function uploadFiles(ctx, inputId, obj) {
		var fls=flashChecker();
		var s="";
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
	//下载文件
	function downloads(docid){
		window.open("${ctx}/sys/docinfo/download/"+docid);
    }
    function inputTipText(){    
		$("input[class*=grayTips]") //所有样式名中含有grayTips的input   
		.each(function(){   
   		var oldVal=$(this).val();   //默认的提示性文本   
   		$(this)   
  	 	.css({"color":"#888"})  //灰色   
   		.focus(function(){   
    	if($(this).val()!=oldVal){$(this).css({"color":"#000"});}else{$(this).val("").css({"color":"#888"});}   
   		})   
   		.blur(function(){   
    	if($(this).val()==""){$(this).val(oldVal).css({"color":"#888"});}   
   		})   
   		.keydown(function(){$(this).css({"color":"#000"});});   
     
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
	</style>
</head>
<body>
	<div>
		<!--右侧内容部分开始-->
		<div class="ydbz_tit pl20">控房修改</div>
			<form:form method="post" modelAttribute="hotelControlInput" action="" class="form-horizontal" id="inputForm" novalidate="">
				<form:hidden path="uuid" />
				<input type="hidden" name="editType" value="${editType }"/>
				<div class="maintain_add">
					<p>
						<label> <span class="xing">*</span>控房名称：</label> 
						<form:input path="name" htmlEscape="false" maxlength="49" />
					</p>
					<p>
						<label> <span class="xing">*</span>地接供应商：</label> 
						<select id="slSupplier" name="groundSupplier">
							<c:forEach items="${supplierInfos }" var="supplierInfo">
								<option value="${supplierInfo.id }" <c:if test="${supplierInfo.id == hotelControlInput.groundSupplier }">selected</c:if>>${supplierInfo.supplierName }</option>
							</c:forEach>
						</select>
					</p>
					<p class="maintain_kong"></p>
					<p>
						<label> <span class="xing">*</span>国家：</label>
						<c:choose>
							<c:when test="${editFlag }">
								<trekiz:suggest name="country" style="width:150px;" defaultValue="${hotelControlInput.country}" callback="getAjaxSelect('island',$('#country'))"  displayValue="<trekiz:autoId2Name4Table tableName='sys_geography' sourceColumnName='uuid' srcColumnName='name_cn' value='${hotelControlInput.country}'/>" ajaxUrl="${ctx}/geography/getAllConListAjax" />
							</c:when>
							<c:otherwise>
								<form:hidden path="country" />
								<trekiz:autoId2Name4Table tableName='sys_geography' sourceColumnName='uuid' srcColumnName='name_cn' value='${hotelControlInput.country}'/>
							</c:otherwise>
						</c:choose>
					</p>
					<p>
						<label> <span class="xing">*</span>岛屿名称：</label>
						<c:choose>
							<c:when test="${editFlag }">
								<select name="islandUuid" id="islandUuid" onchange="getAjaxSelect('islandway',this);">
									<option value="">不限</option>
									<c:forEach items="${islandList }" var="item">
										<option value="${item.uuid}" <c:if test="${item.uuid == hotelControlInput.islandUuid }">selected="selected"</c:if>>${item.islandName}</option>
									</c:forEach>
								</select>
							</c:when>
							<c:otherwise>
								<form:hidden path="islandUuid" />
								<trekiz:autoId2Name4Class classzName="Island" sourceProName="uuid" srcProName="islandName" value="${hotelControlInput.islandUuid }"/>
							</c:otherwise>
						</c:choose>
					</p>
					<p>
						<label> <span class="xing">*</span>采购类型：</label> 
						<c:choose>
							<c:when test="${editFlag }">
								<select name="purchaseType">
								<option value="0" selected="selected">内采</option>
								<option value="1">外采</option>
							</select>
							</c:when>
							<c:otherwise>
								<form:hidden path="purchaseType" />
								<c:choose>
									<c:when test="${hotelControlInput.purchaseType==0}">内采</c:when>
									<c:when test="${hotelControlInput.purchaseType==1}">外采</c:when>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</p>
					<p class="maintain_kong"></p>
					<p>
						<label>酒店集团：</label>
						<c:choose>
							<c:when test="${editFlag }">
								<trekiz:defineDict name="hotelGroup" type="hotel_group" defaultValue="${hotelControlInput.hotelGroup}" />
							</c:when>
							<c:otherwise>
								<form:hidden path="hotelGroup" />
								<trekiz:defineDict name="hotelGroup" type="hotel_group" defaultValue="${hotelControlInput.hotelGroup}" readonly="true" />
							</c:otherwise>
						</c:choose>
					</p>
					<p>
						<label> <span class="xing">*</span>酒店名称：</label>
						<c:choose>
							<c:when test="${editFlag }">
								<select name="hotelUuid" id="hotelUuid" onchange="getAjaxSelect('roomtype',this);">
									<option value="">不限</option>
									<c:forEach items="${hotelList }" var="item">
										<option value="${item.uuid}" <c:if test="${item.uuid == hotelControlInput.hotelUuid }">selected="selected"</c:if>>${item.nameCn}</option>
									</c:forEach>
								</select>
							</c:when>
							<c:otherwise>
								<form:hidden path="hotelUuid" />
								<trekiz:autoId2Name4Class classzName="Hotel" sourceProName="uuid" srcProName="nameCn" value="${hotelControlInput.hotelUuid }"/>
							</c:otherwise>
						</c:choose>
					</p>
					<p>
						<label> <span class="xing">*</span>币种选择：</label>
						<c:choose>
							<c:when test="${editFlag }">
								<select id="currency" name="currencyId">
									<c:forEach items="${currencyList}" var="item">
									    <option value="${item.id}" 
									    	<c:choose>
									    		<c:when test="${hotelControlInput.currencyId==item.id}">selected="selected"</c:when>
									    		<c:when test="${hotelControlInput.currencyId == null && item.currencyName == '美元'}">selected="selected"</c:when>
									    	</c:choose>>
									    	${item.currencyName}
									    </option>
									</c:forEach>
								</select>
							</c:when>
							<c:otherwise>
								<form:hidden path="currencyId" />
								<c:forEach items="${currencyList}" var="item">
									<c:if test="${hotelControlInput.currencyId==item.id}">${item.currencyName}</c:if>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</p>
					<p class="maintain_kong"></p>
				</div>
				<br/>
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
						</tr>
					</thead>
					<c:forEach var="detail" items="${hotelControlInput.hotelControlDetails}" varStatus="v">
						<tbody detailUuid="${detail.uuid}">
							<input type="hidden" name="detailUuids" value="${detail.uuid}" />
							<tr>
								<input type="hidden" name="hotelMeals" />
								<td class="tc" rowspan="${fn:length(detail.roomList)}">
									<c:choose>
										<c:when test="${detail.status == 0 }">
											<input type="hidden" name="inDates" value="${detail.inDate}" />
											<fmt:formatDate pattern="yyyy-MM-dd" value="${detail.inDate}" />
										</c:when>
										<c:otherwise>
											<input type="text" id="inDate" class="wdate dateinput" name="inDates" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${detail.inDate}" />" onfocus="WdatePicker()" readonly="readonly" />
										</c:otherwise>
									</c:choose>
									
								</td>
								<td class="tc" rowspan="${fn:length(detail.roomList)}">
									<input type="hidden" name="airlines" />
									<input type="hidden" name="airlineUuids" />
									<c:forEach items="${detail.flightList }" var="flight">
										<input type="hidden" name="airlineUuid" value="${flight.uuid}" />
										<c:forEach items="${fn:split(flight.airline ,';') }" var="item">
											<p>
												<input type="text" class="wtext" name="airline" value="${item }" />
											</p>
										</c:forEach>
										<c:if test="${flight.airline == null || flight.airline == ''}">
											<p>
												<input type="text" class="wtext" name="airline" value="" />
											</p>
										</c:if>
									</c:forEach>
									<c:if test="${fn:length(detail.flightList) == 0 }">
										<input type="text" class="wtext" name="airline" value="" />
									</c:if>
								</td>
								<td class="tc" name="houseType">
									<input type="hidden" name="rooms" />
									<input type="hidden" name="roomUuids" />
									<c:forEach var="room" items="${detail.roomList }" varStatus="status">
										<c:if test="${status.index == 0 }">
											<input type="hidden" name="roomUuid" value="${room.uuid}" />
											<p>
												<span>
													<c:choose>
														<c:when test="${detail.status == 0 }">
															<input type="hidden" name="roomType" value="${room.roomUuid}" />
															<trekiz:autoId2Name4Class classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${room.roomUuid }"/>
														</c:when>
														<c:otherwise>
															<select class="wtext" name="roomType">
																<option value="">不限</option>
																<c:forEach items="${roomList }" var="item">
																	<option value="${item.uuid}" <c:if test="${item.uuid == room.roomUuid }">selected="selected"</c:if>>${item.roomName}</option>
																</c:forEach>
															</select> 
														</c:otherwise>
													</c:choose>
												</span>* 
												<span> 
													<c:choose>
														<c:when test="${detail.status == 0 }">
															<input type="hidden" name="night" value="${room.night}" />
															${room.night }
														</c:when>
														<c:otherwise>
															<input type="text" class="wnum1" name="night" value="${room.night }" maxlength="3" />
														</c:otherwise>
													</c:choose>
												</span>晚
											</p>
										</c:if>
									</c:forEach>
								</td>
								<td class="tc" name="mealType">
									<p>
										<select class="wtext"></select>
		                            	<a class="addMealType" href="javascript:;">新增</a>
		                            </p>
								</td>
								<td class="tc" rowspan="${fn:length(detail.roomList)}">
									<trekiz:defineDict id="islandWays" name="islandWays" type="islands_way" className="wtext" defaultValue="${detail.islandWay }"/>
								</td>
								<td class="tc" rowspan="${fn:length(detail.roomList)}">
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
									<input type="text" class="wnum2" name="totalPrices" value="${detail.totalPrice}" maxlength="49" />
								</td>
								<td class="tc" rowspan="${fn:length(detail.roomList)}">
									<input type="text" class="wnum1" name="stocks" value="${detail.stock}" maxlength="5" />
								</td>
								<td class="tc" rowspan="${fn:length(detail.roomList)}">
									<input type="text" class="wtext" name="memos" value="${detail.memo}" maxlength="200" />
								</td>
							</tr>
							<c:forEach var="room" items="${detail.roomList }" varStatus="status">
								<c:if test="${status.index != 0 }">
									<tr>
		                                <td class="tc" name="houseType">
		                                	<input type="hidden" name="roomUuid" value="${room.uuid}" />
		                                    <p>
												<span>
													<c:choose>
														<c:when test="${detail.status == 0 }">
															<input type="hidden" name="roomType" value="${room.roomUuid}" />
															<trekiz:autoId2Name4Class classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${room.roomUuid }"/>
														</c:when>
														<c:otherwise>
															<select class="wtext" name="roomType">
																<option value="">不限</option>
																<c:forEach items="${roomList }" var="item">
																	<option value="${item.uuid}" <c:if test="${item.uuid == room.roomUuid }">selected="selected"</c:if>>${item.roomName}</option>
																</c:forEach>
															</select> 
														</c:otherwise>
													</c:choose>
												</span>* 
												<span>
													<c:choose>
														<c:when test="${detail.status == 0 }">
															<input type="hidden" name="night" value="${room.night}" />
															${room.night }
														</c:when>
														<c:otherwise>
															<input type="text" class="wnum1" name="night" value="${room.night }" maxlength="3" />
														</c:otherwise>
													</c:choose>
												</span>晚
											</p>
		                                </td>
		                                <td class="tc" name="mealType">
		                                    <p>
		                                        <select class="wtext"></select>
		                                        <a class="addMealType" href="javascript:;">新增</a>
		                                    </p>
		                                </td>
		                            </tr>
								</c:if>
							</c:forEach>
						</tbody>
					</c:forEach>
				</table>
				<div class="sysdiv sysdiv_coupon">
					<p class="maintain_pfull new_kfang">
						<label>上传附件：</label> 
						<input type="button" name="hotelAnnexDocId" value="上传" class="btn btn-primary" onClick="uploadFiles('${ctx}',null,this)"/> <em class="tips_color">多文件，多格式上传附件。单个附件不大于20M，总共不大于20M。</em>
						
						
						<ol class="batch-ol">
							
							<c:forEach items="${haList}" var="file" varStatus="s1">
								<li>
									<span>${file.docName}</span><a style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${file.docId})">下载</a>
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
						
						<textarea id="memo" name="memo" >${hotelControlInput.memo}</textarea>
					</p>
				</div>
				<div class="release_next_add">
					<input type="button" value="取消" class="btn btn-primary gray" onclick="window.close();" />
					<input type="submit" value="保存修改" class="btn btn-primary" id="btnSubmit"/>
				</div>
		</form:form>
		<!--右侧内容部分结束-->
	</div>
</body>
</html>
