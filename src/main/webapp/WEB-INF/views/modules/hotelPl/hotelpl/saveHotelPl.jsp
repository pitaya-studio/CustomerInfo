<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>酒店价单列表-<c:choose><c:when test="${updateFlag == 1}">新增</c:when><c:when test="${updateFlag == 2}">修改</c:when></c:choose>价单</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <!--[if lte IE 6]>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/bootstrap-ie6.min.css" />
    <script type="text/javascript" src="${ctxStatic}/js/bootstrap-ie.min.js"></script>
    <![endif]-->
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
    <style type="text/css">
        table td input, select, button {
            margin-bottom: 0px !important;
        }

        table td {
            vertical-align: middle;
            padding: 3px;
        }

        table.table-promotion td {
            vertical-align: middle;
            padding: 3px;
        }

        .table-noborder td {
            border: none;
        }

        .title-sm {
            width: 80px;
        }

        .title-md {
            width: 120px;
        }

        .title-lg {
            width: 160px;
        }
        .input_label_lg{
            width: 148px;
            margin-right: 1px;
        }
        .input_label_sm {
            margin-right: 20px;
        }

        .dateinput[diabled], input[disabled] { 
        	background: url(../images/dateinputbg.png) no-repeat right center !important; 
        }

        .save_button {
            margin-top: 20px;
        }

        .region_disabled {
            display: none;
        }
        i.region_disabled {
            display: none;
        }
        button.tabScroll_list {
            display: none;
        }

        .title_tabScroll_list {
            font-weight: bold;
            font-size: 16px;
        }

        .basemeal-allSelected {
            display: none;
        }

        /*不需要全部展开了
            需要的时候 请将 btn_open_in_bands_hide 替换为btn_open_in_bands
        */
        .btn_open_in_bands_hide {
            display: none;
        }
        #productInfo01 .activitylist_bodyer_right_team_co1{ 
        	min-width: 250px;
		} 
		#productInfo01 .activitylist_bodyer_right_team_co2{ 
			min-width: 522px;
		}
		.ui-autocomplete.ui-menu{
			width:180px;
		}
		.ui-menu .ui-menu-item a{
			white-space: nowrap;
		}
		.ellipsis{
			text-overflow: ellipsis; overflow: hidden; white-space: nowrap;
		}
		.fixedTable[name="tabContent_BaseMeal_HotelRoomPrice"]{
			overflow: auto;
		}
		.fixedTable[name="tabContent_BaseMeal_HotelRoomPrice"] table.activitylist_bodyer_table{ 
			table-layout: fixed;
			width: auto;
		}
		.fixedTable[name="tabContent_BaseMeal_HotelRoomPrice"] table.activitylist_bodyer_table th{ 
			max-width: 120px;
			min-width:85px;
			white-space: nowrap;
			overflow: hidden;
			text-overflow: ellipsis;
		}
    </style>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery.tabScroll.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery.customElement.js"></script>
    
    <script type="text/javascript" src="${ctxStatic}/js/dataStructure.js"></script>
    <script type="text/javascript" src="${ctxStatic}/hotelPl/common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/hotelPl/baseInfo.js"></script>
    <script type="text/javascript" src="${ctxStatic}/hotelPl/hotelTax.js"></script>
    <script type="text/javascript" src="${ctxStatic}/hotelPl/hotelRoomPrice.js"></script>
    <script type="text/javascript" src="${ctxStatic}/hotelPl/islandWay.js"></script>
    <script type="text/javascript" src="${ctxStatic}/hotelPl/riseMealPrice.js"></script>
    <script type="text/javascript" src="${ctxStatic}/hotelPl/holidayMeal.js"></script>
    <script type="text/javascript" src="${ctxStatic}/hotelPl/preferentialInfo.js"></script>
    <!--保存和编辑状态在各个区域的转换-->
    <script type="text/javascript">
        $(document).ready(function () {
            $(".price_region:gt(0)").each(function () {
                setRegionDisabled(this);
            });
            $("#btnEditBaseInfo").addClass("region_disabled");
            
            //初始化所属国家下拉列表框
			changePosition();
			
			//可输入select
     		$("#slSupplier").comboboxInquiry().on('comboboxinquiryselect', function(event, obj){
     			
     		});
     		$("#slSupplier").comboboxInquiry('showTitle');
     		$("#slHotel").comboboxInquiry().on('comboboxinquiryselect', function(event, obj){
		    	var hotelUuid = $(this).val();
		    	baseInfo.hotelText = $(this).find("option:selected").text();
		        if(hotelUuid == '') {
		        	$("#hotel_hotelGroup").text("");
		            $("#hotel_hotelStar").text("");
		            $("#hotel_address").text("");
		            $("#hotel_tel").text("");
		        } else {
		        	$.ajax({
		        		type: "POST",
		        	   	url: ctx + "/hotelPl/getHotelDetailInfo",
		        	   	async: false,
		        	   	data: {
		        				"hotelUuid":hotelUuid
		        			  },
		        		dataType: "json",
		        	   	success: function(data){
		        	   		if(data){
		        	   			currentHotel = data;
		    	   	        	$("#hotel_hotelGroup").text(currentHotel.hotelGroup);
		    	   	            $("#hotel_hotelStar").text(currentHotel.hotelStar);
		    	   	            $("#hotel_address").text(currentHotel.hotelAddress);
		    	   	            $("#hotel_tel").text(currentHotel.contactPhone);
		        	   		}
		        	   	}
		        	});
		        }
		        
     		}); 
     		
     		$("#slHotel").comboboxInquiry('showTitle');
        });
        
        function saveButtonCli(obj) {
          	var $this = $(obj);
            var $region = $this.parents(".price_region:first");
            setRegionDisabled($region);
            $(".edit_button").removeClass("region_disabled");
            $this.addClass("region_disabled");
        }
          
        function editButtonCli(obj) {
         	var $this = $(obj);
            var $region = $this.parents(".price_region:first");
            setRegionEnable($region);
            $region.find(".save_button").removeClass("region_disabled");
            $(".edit_button").addClass("region_disabled");
        }
        
        //境内境外 改变地址展现形式
		function changePosition(){
			initSuggest({"type":$("input[name=position]:checked").val()});
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
	    //上传文件后的名称回显
	    function commenFunction(obj,fileIDList,fileNameList,filePathList) {
			var fileIdArr = fileIDList.split(";");
			var fileNameArr = fileNameList.split(";");
			var filePathArr = filePathList.split(";");
			for(var i=0; i<fileIdArr.length-1; i++) {
				var html = '<li><a class="padr10" href="javascript:void(0)" onclick="downloads('+ fileIdArr[i] +')">'+ fileNameArr[i] +'</a>';
				html += '<span class="tdred" style="cursor:pointer;" onclick="deleteFileInfo(null,\'hotelFeaturesAnnexDocId\',this)">删除</span>';
				html += '<input type="hidden" name="hotelTraveler_files" />';
				html += '<input type="hidden" name="docId"  value="' + fileIdArr[i] + '" />';
				html += '<input type="hidden" name="docName" value="'+ fileNameArr[i] +'"/>';
				html += '<input type="hidden" name="docPath" value="'+ filePathArr[i] +'"/>';
				html += '</li>';
				$(obj).parent().parent().parent().find("ul").append(html);
				
			}
        }
        
      function saveBtnSaveAttach(obj){
        var docId = "";
        $(":input[name=docId]").each(function(){
        	docId+=$(this).val()+",";
        });
        var docName = "";
        $(":input[name=docName]").each(function(){
        	docName+=$(this).val()+",";
        });
        var docPath = "";
        $(":input[name=docPath]").each(function(){
        	docPath+=$(this).val()+",";
        });
      	$.ajax({
			type: "POST",
		   	url: "${ctx}/hotelPl/saveHotelAnnex",
		   	async: false,
		   	data: {
					"hotelPlUuid":baseInfo.uuid,
					"docId":docId,
					"docName":docName,
					"docPath":docPath
				  },
			dataType: "json",
		   	success: function(data){
		   		$.jBox.tip(data.message);
		   		saveButtonCli($(obj));
		   	}
		});
      }
      //当方位属性为内陆的时候隐藏岛屿名称
      function changeLocation(obj){
    	 var $selInput = $("#slHotel").next().find("input");
    	 if(obj=="1"){
    	 	 getAjaxSelect('countryToHotel',$('#slCountry'));
    		 $("#islandName").hide();
    		 $("#slIsland").attr("disabled",true);
    		 $selInput.val('');
    		 $selInput.attr('title','');
    	 }else{
    		 $("#islandName").show();
    		 $("#slIsland").attr("disabled",false);
    		 getAjaxSelect('hotel',$("#slIsland"));
    		 $selInput.val('');
    		 $selInput.attr('title','');
    	 }
      }
    </script>
    <script type="text/javascript">
    	var updateFlag='${updateFlag}';
		$(document).ready(function(){
		
		    if(updateFlag == "2"){
				<c:if test="${updateFlag == 2}">
			        //基本信息
			        //todo 需要后台处理数据
					//初始化基础信息
			        baseInfo=${baseInfo};
			        bindBaseInfo(baseInfo);
			        
			        //初始化优惠的币种信息
			        $(".selectWithCurrMark option[data-unitId=2]").each(function(){
			        	$(this).attr("data-unit", baseInfo.currencyMark);
			        });
			        
					//酒店房型价格基础数据
					hotelRoomMap = ${hotelRoomList};
					initRoomTypePrice();
					
			        islandWays = ${islandWays};
			        initIslandWay();
			        
			        hotelMeals = ${hotelMeals};
			        initAdditionalMeal();
			        //初始化优惠信息里的试用房型
			        houseTypes = ${houseTypes};
			        //为关联酒店赋值
					var relevancyHotels = ${relevancyHotels};
					var html=[];
					html.push('<option value="">请选择</option>');
					$.each(relevancyHotels,function(index,item){
						html.push('<option value="', item.uuid, '">', item.nameCn, '</option>');
					});
					$("#relevancyHotel").html(html.join('')).change();
				
			        //酒店税金
			        var hotelPlTaxPriceList = ${hotelPlTaxPriceList};
			        bindTaxDictionary(hotelPlTaxPriceList);
			        
			        //税金例外
			        var hotelPlTaxExceptionList = ${hotelPlTaxExceptionList};
			        bindTaxException(hotelPlTaxExceptionList);
					
					
			        //酒店房型價格
			        var roomPrices = ${roomPrices };
			        var hotelPlRoomMemoList = ${hotelPlRoomMemoList};
			        bindRoomTypePrices(roomPrices,hotelPlRoomMemoList);
					
			        //交通费用
			        var islandWayPriceList = ${islandWayPriceList};
			        var islandWayMemoList = ${islandWayMemoList};
			        bindIslandWay(islandWayPriceList,islandWayMemoList);
			        
			        //升餐费用
			        var hotelRiseMealMap = ${hotelRiseMealMap};
			        var hotelPlRiseMealMemoList = ${hotelPlRiseMealMemoList};
			        bindRiseMeal(hotelRiseMealMap,hotelPlRiseMealMemoList);
			
			        //强制节日餐
			        var hotelPlHolidayMealList = ${hotelPlHolidayMealList};
			        bindHolidayMeal(hotelPlHolidayMealList);
			
			        var favorInfos=${favorInfos};
			        bindPreferential(favorInfos);
			        initCurrency();
			        
			        //加载可关联的所有优惠信息
			        var preferentialRelevancyFavors = ${relevancyFavors};
			        var i=0;
   	   				preferentialRelevancyFavors && $.each(preferentialRelevancyFavors, function () {
   	   	                relevancyFavors.favors[this.uuid] = this.preferentialName;
   	   	                i++;
   	   	            });
			        relevancyFavors.length = i;
				</c:if>
		    } else {
		    	getAjaxSelect('island',$('#slCountry'));
		    }
		
		});
		function downloads(docid){
			window.open("${ctx}/sys/docinfo/download/"+docid);
		}
    </script>
</head>

<body>
	<!--右侧内容部分开始2-->
	<div class="mod_nav">酒店价单列表 &gt;<c:choose><c:when test="${updateFlag == 1}">新增</c:when><c:when test="${updateFlag == 2}">修改</c:when></c:choose>价单</div>

	<!-- 酒店价单json数据 -->
	<input type="hidden" id="suppliers" value="${suppliers }" />
	<input type="hidden" id="travelerTypes" value='${travelerTypes }' />
	<input type="hidden" id="hotelGuestTypes" value='${hotelGuestTypes }' />
	<input type="hidden" id="hotelRoomList" value='${hotelRoomList }' />
	<input type="hidden" id="islandWays" value="${islandWays }" />
	<input type="hidden" id="hotelMeals" value="${hotelMeals }" />
	<input type="hidden" id="hotelPlCtx" value="${ctx}" />
	<input type="hidden" name="hotelPlUuid">
	<input type="hidden" id="templatesesJson" value='${templatesesJson }'>
	<input type="hidden" id="updateFlag" value="${updateFlag}"/>
	
	<div class="produceDiv">
		<!--产品信息开始-->
		<div class="mod_information" id="ofAnchor1">
			<!--基本信息-->
			<div class="mod_information mar_top0">
				<div class="price_region">
					<!--标题-->
					<div class="ydbz_tit">
						<span class="ydExpand closeOrExpand"></span>基本信息
						<span class="fr padr10">
							<button class="btn btn-primary edit_button" id="btnEditBaseInfo" onclick="editButtonCli(this);" style="width: auto">修改</button>
						</span>
					</div>
					<!--基本信息内容-->
					<form method="post" modelAttribute="hotelPlInput" action="" class="form-horizontal" id="baseInfoInputForm" novalidate="">
						<input type="hidden" name="hotelPlUuid">
						<div style="margin-top: 8px; display: block;" id="productInfo01">
							<div class="activitylist_bodyer_right_team_co2" style="height:40px;width: 38%">
								<label class="title-md tr"><span class="xing">*</span>价单名称：</label>
								<input id="txtPriceMealName" name="name" style="width: 70%;" type="text" />
							</div>
							<div class="kong"></div>
							<div class="activitylist_bodyer_right_team_co1" style="height:40px;">
								<label class="title-md tr"><span class="xing">*</span>地接社供应商：</label>
								<select id="slSupplier" name="supplierInfoId">
									<c:forEach items="${supplierInfos }" var="supplierInfo">
										<option value="${supplierInfo.id }" <c:if test="${hotelPl.supplierInfoId == supplierInfo.id}">selected</c:if>>${supplierInfo.supplierName}</option>
									</c:forEach>
								</select>
							</div>
							<div class="activitylist_bodyer_right_team_co1">
								<label class="title-md tr">采购类型：</label> 
								<label><input type="radio" style="width: auto;margin: 0px" purchase-type="0" value="0" name="purchaseType" <c:if test="${hotelPl == null or hotelPl.purchaseType == 0}">checked</c:if>/>内采</label> 
								<label><input type="radio" style="width: auto;margin: 0px" purchase-type="1" value="1" name="purchaseType" <c:if test="${hotelPl.purchaseType == 1}">checked</c:if>/>外采</label>
							</div>
							<div class="activitylist_bodyer_right_team_co1">
								<label class="title-md tr">币种：</label>
								<select id="slCurrency" name="currencyId">
									<c:forEach items="${currencyList }" var="currency">
										<option currency-mark="${currency.currencyMark }" value="${currency.id }">${currency.currencyName }</option>
									</c:forEach>
								</select>
							</div>
							<div class="kong"></div>
							<div class="activitylist_bodyer_right_team_co1">
								<label class="title-md tr">位置属性：</label>
								<label><input type="radio" style="width: auto;margin: 0px" name="position" position-code="1" value="1" onclick="changePosition();" <c:if test="${hotelPl.position == 1}">checked</c:if>/>境内</label>
								<label><input type="radio" style="width: auto;margin: 0px" name="position" position-code="2" value="2" onclick="changePosition();" <c:if test="${hotelPl == null or hotelPl.position == 2}">checked</c:if>/>境外</label>
							</div>
<!-- 							根据前端提供的样式，修改国家的DIV样式 update by zhanghao -->
<!-- 						<div class="" style="width:18%; height:40px; float:left; margin-left:2%;left:0px; top:0px;"> -->
							<div class="" style="height: 40px; float: left; margin-left: 2%; left: 0px; top: 0px;min-width: 18%;">
								<label class="title-md tr"><span class="xing">*</span>国家：</label>
								<span>
									<trekiz:suggest id="slCountry" name="country" style="width:150px;" defaultValue="80415d01488c4d789494a67b638f8a37" callback="getAjaxSelect('island',$('#slCountry'))"  displayValue="${countryName}" ajaxUrl="${ctx}/geography/getGeoListAjax" />
								</span>
							</div>
							<div class="activitylist_bodyer_right_team_co1">
								<label class="title-md tr">方位属性：</label>
								<label><input type="radio" onclick="changeLocation(this.value)" style="width: auto;margin: 0px" name="areaType" value="1" areaType-code="1" <c:if test="${hotelPl.areaType == 1}">checked</c:if> />内陆</label>
								<label><input type="radio" onclick="changeLocation(this.value)" style="width: auto;margin: 0px" name="areaType" value="2" areaType-code="2" <c:if test="${hotelPl == null or hotelPl.areaType == 2}">checked</c:if>/>海岛</label>
							</div>
							<div class="kong"></div>
							<div class="activitylist_bodyer_right_team_co1" id="islandName">
								<label class="title-md tr"><span class="xing">*</span>岛屿名称：</label>
								<select name="islandUuid" id="slIsland" onchange="getAjaxSelect('hotel',this);" >
						 		</select>
							</div>
	
							<div class="activitylist_bodyer_right_team_co1 ellipsis">
								<label class="title-md tr"><span class="xing">*</span>酒店名称：</label>
								<select name="hotelUuid" id="slHotel" style="width:200px;"></select>
							</div>
	
							<div class="kong"></div>
							<div class="activitylist_bodyer_right_team_co1">
								<label class="title-md tr">酒店集团：</label>
								<span id="hotel_hotelGroup"></span>
							</div>
							<div class="activitylist_bodyer_right_team_co1">
								<label class="title-md tr">酒店等级：</label> 
								<span id="hotel_hotelStar"></span>
							</div>
	
							<div class="kong"></div>
							<div class="activitylist_bodyer_right_team_co2" style="width: 38%">
								<label class="title-md tr">酒店地址：</label><span id="hotel_address"></span>
							</div>
							<div class="activitylist_bodyer_right_team_co1">
								<label class="title-md tr">联系电话：</label><span id="hotel_tel"></span>
							</div>
							<div class="kong"></div>
							<div style="display: none" id="updateInfo">
								<div class="activitylist_bodyer_right_team_co1">
									<label class="title-md tr">创建人：</label><span id="createUserName"></span>
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<label class="title-md tr">更改人：</label><span id="UpdateUserName"></span>
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<label class="title-md tr">最近更新日期：</label><span id="UpdateUserDate"></span>
								</div>
							</div>
							<div class="kong"></div>
	
							<div class="tc">
								<input type="button" class="btn btn-primary" id="btnSaveBaseInfo" value="保存">
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="clear"></div>
			<!--价单详细信息-->
			<div id="priceMenuDetail" class="hide">
				<!--税金信息-->
				<div class="price_region">
					<div class="ydbz_tit">
						<span class="ydExpand closeOrExpand"></span>酒店税金 
						<span class="fr padr10">
							<button class="btn btn-primary edit_button" id="btnEditTax" onclick="editButtonCli(this);" style="width: auto">修改</button> 
						</span>
					</div>
					<div>
                    	<div>
                        	<h6>税金价格字典</h6>
                            <table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new" id="tableTaxDictionary">
                            	<thead>
                                	<tr>
                                    	<th width="30%">税种选择</th>
                                        <th width="50%">日期设定</th>
                                        <th width="20%">增收/人</th>
                                    </tr>
                            	</thead>
                                <tbody>
                                    <tr tax-type="1" tax-name="政府税" uuid="">
                                    	<td class="tc" name="taxName">政府税</td>
                                        <td class="tc">
                                        	<input type="text" onclick="WdatePicker({readOnly:true,onpicked:taxDictionaryDateChange})" class="dateinput w106 required " name="dtpTaxDictionaryStartDate" readonly="readonly" />
                                              	至
                                            <input type="text" onclick="WdatePicker({readOnly:true,onpicked:taxDictionaryDateChange})" class="dateinput w106 required " name="dtpTaxDictionaryEndDate" readonly="readonly" />
                                            <i class="price_sale_house_01"></i>
                                        </td>
                                        <td class="tc"  >
                                        	<span style="position: relative; display: inline-block;">
                                            	<em class="gray14" style="position: absolute;top:5px;right: 2px;">%<em></em></em>
                                                <input data-type="amount" maxlength="9" name="txtTaxAddValue" charge-type="1" data-type="float" charge-type-text="百分比" type="text" value="" style="width: 60px; padding-right: 20px;" class="tr" />
                                            </span>
                                       	</td>
                                    </tr>
                                    <tr tax-type="2" tax-name="服务税" uuid="">
                                    	<td class="tc" name="taxName">服务税</td>
                                    	<td class="tc">
                                        	<input type="text" onclick="WdatePicker({readOnly:true,onpicked:taxDictionaryDateChange})" class="dateinput w106 required " name="dtpTaxDictionaryStartDate" readonly="readonly" />
                                                	至
                                        	<input type="text" onclick="WdatePicker({readOnly:true,onpicked:taxDictionaryDateChange})" class="dateinput w106 required " name="dtpTaxDictionaryEndDate" readonly="readonly" />
                                            <i class="price_sale_house_01"></i>
                                        </td>
                                        <td class="tc" >
                                        	<span style="position: relative; display: inline-block;">
                                            	<em class="gray14" style="position: absolute;top:5px;right: 2px;">%<em></em></em>
                                                <input data-type="amount" maxlength="9" name="txtTaxAddValue" charge-type="1" data-type="float" charge-type-text="百分比" type="text" value="" style="width: 60px; padding-right: 20px;" class="tr" />
                                            </span>
                                        </td>
                                  	</tr>
                                    <tr tax-type="3" tax-name="床税" uuid="">
                                    	<td class="tc" name="taxName">床税</td>
                                    	<td class="tc">
                                        	<input type="text" onclick="WdatePicker({readOnly:true,onpicked:taxDictionaryDateChange})" class="dateinput w106 required " name="dtpTaxDictionaryStartDate" readonly="readonly" />
                                                 	至
                                            <input type="text" onclick="WdatePicker({readOnly:true,onpicked:taxDictionaryDateChange})" class="dateinput w106 required " name="dtpTaxDictionaryEndDate" readonly="readonly" />
                                            <i class="price_sale_house_01"></i>
                                        </td>
                                        <td class="tc" >
                                        	<input data-type="amount" maxlength="9" name='txtTaxAddValue' charge-type="2" data-type="float" charge-type-text="金额" tax-type="3" type='text' value='' style='width: 60px;padding-right: 10px' class="tr" />
                                        </td>
                                    </tr>
                                    <tr tax-type="4" tax-name="环保税" uuid="">
                                         <td class="tc" name="taxName">环保税</td>
                                         <td class="tc">
                                         	<input type="text" onclick="WdatePicker({readOnly:true,onpicked:taxDictionaryDateChange})" class="dateinput w106 required "  name="dtpTaxDictionaryStartDate" readonly="readonly"/>
                                         	 至
                                         	<input type="text" onclick="WdatePicker({readOnly:true,onpicked:taxDictionaryDateChange})" class="dateinput w106 required " name="dtpTaxDictionaryEndDate" readonly="readonly"/>
                                            <i class="price_sale_house_01"></i>
                                         </td>
                                         <td class="tc">
                                         	<input name='txtTaxAddValue' data-type="amount" charge-type="2" charge-type-text="金额" tax-type="4" type='text' value='' style='width: 60px;padding-right: 10px'  class="tr"/>
                                         </td>
                                     </tr>
                                </tbody>
                             </table>
                          </div>
                          <div>
                          	<h6>税金例外设置</h6>
                            <table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new" id="tableTaxExcept">
                            	<thead>
                                	<tr>
                                    	<th width="10%">税收例外类型</th>
                                    	<th width="30%">日期</th>
                                    	<th width="20%">例外税种</th>
                                    	<th width="40%">游客类型</th>
                                    </tr>
                               </thead>
                               <tbody>
                               		<tr exception-type="1" exception-name="房型" uuid="">
                                    	<td class="tc" name="exceptionName">房型</td>
                                        <td class="tc">
                                        	<input type="text" onclick="WdatePicker({readOnly:true,onpicked:taxExceptDateChange})" class="dateinput w106 required " name="dtpTaxExceptStartDate" readonly="readonly" />
                                                   	至
                                            <input type="text" onclick="WdatePicker({readOnly:true,onpicked:taxExceptDateChange})" class="dateinput w106 required " name="dtpTaxExceptEndDate" readonly="readonly" />
                                            <i class="price_sale_house_01"></i>
                                        </td>
                                        <td class="tl">
                                        	<label class="input_label_sm"> <input type="checkbox" style="margin: 0px" name="taxType" tax-type="1" />政府税</label>
                                        	<label class="input_label_sm"> <input type="checkbox" style="margin: 0px" name="taxType" tax-type="2" />服务税</label>
                                        	<label class="input_label_sm"> <input type="checkbox" style="margin: 0px" name="taxType" tax-type="3" />床税</label>
                                        	<label class="input_label_sm"> <input type="checkbox" style="margin: 0px" name="taxType" tax-type="4" /> 环保税</label>
                                        </td>
                                        <td name="taxtExceptionTravelList">
                                        	<label class="input_label_sm"> <input type="checkbox" style="margin: 0px" name="travelType" traveler-type="全部" checkbox-action="All" />全部</label>
                                        </td>
                                     </tr>
                                     <tr exception-type="2" exception-name="餐型" uuid="">
                                     	<td class="tc" name="exceptionName">餐型</td>
                                     	<td class="tc">
                                        	<input type="text" onclick="WdatePicker({readOnly:true,onpicked:taxExceptDateChange})" class="dateinput w106 required " name="dtpTaxExceptStartDate" readonly="readonly" />
                                                   	至
                                            <input type="text" onclick="WdatePicker({readOnly:true,onpicked:taxExceptDateChange})" class="dateinput w106 required " name="dtpTaxExceptEndDate" readonly="readonly" />
                                            <i class="price_sale_house_01"></i>
                                        </td>
                                        <td class="tl">
                                        	<label class="input_label_sm"> <input type="checkbox" style="margin: 0px" name="taxType" tax-type="1" />政府税</label>
                                            <label class="input_label_sm"> <input type="checkbox" style="margin: 0px" name="taxType" tax-type="2" />服务税</label>
                                        </td>
                                        <td name="taxtExceptionTravelList">
                                        	<label class="input_label_sm"> <input type="checkbox" style="margin: 0px" name="travelType" traveler-type="全部" checkbox-action="All" />全部</label>
                                        </td>
                                      </tr>
                                      <tr exception-type="3" exception-name="交通" uuid="">
                                      	<td class="tc" name="exceptionName">交通</td>
                                      	<td class="tc">
                                        	<input type="text" onclick="WdatePicker({readOnly:true,onpicked:taxExceptDateChange})" class="dateinput w106 required " name="dtpTaxExceptStartDate" readonly="readonly" />
                                                   	至
                                            <input type="text" onclick="WdatePicker({readOnly:true,onpicked:taxExceptDateChange})" class="dateinput w106 required " name="dtpTaxExceptEndDate" readonly="readonly" />
                                            <i class="price_sale_house_01"></i>
                                        </td>
                                        <td class="tl">
                                        	<label class="input_label_sm"> <input type="checkbox" style="margin: 0px" name="taxType" tax-type="1" />政府税</label>
                                            <label class="input_label_sm"> <input type="checkbox" style="margin: 0px" name="taxType" tax-type="2" />服务税</label>
                                        </td>
                                        <td name="taxtExceptionTravelList">
                                        	<label class="input_label_sm"> <input type="checkbox" style="margin: 0px" name="travelType" traveler-type="全部" checkbox-action="All" />全部</label>
                                        </td>
                                     </tr>
                                  </tbody>
                               </table>
                               <div class="tax-algorithm">
                                   <span>税金算法：</span> 
                                   <span><input name="taxArithmetic" type="radio" value="1" <c:if test="${hotelPl.taxArithmetic == 1 }">checked</c:if> /><label>连乘</label></span>
                                   <span><input name="taxArithmetic" type="radio" value="2" <c:if test="${hotelPl.taxArithmetic == 2 }">checked</c:if> /><label>分乘</label></span>
                               </div>
                            </div>
                            <div class="kong"></div>
                            <div class="tc"> <button class="btn btn-primary save_button" id="btnSaveTax">保存</button>  </div>
                            <input type="hidden" id="hotelTaxSaveFlag" />
                        </div>
                    </div>
				<!--各房型价格信息-->
				<div class="price_region">
					<div class="ydbz_tit">
						<span class="ydExpand closeOrExpand"></span>酒店房型价格<span class="fr padr10"> <a value="" class="btn_open_in_bands_hide" id="btnToggleRoomPrice">展开全部酒店价格</a>
							<button id="btnHotelRoomPrice" class="btn btn-primary edit_button" style="width: auto">新增日期</button>
							<button id="btnEditHotelRoomPrice" class="btn btn-primary region_disabled" onclick="editButtonCli(this);" style="width: auto;">修改</button>
						</span>
					</div>
					<div>
						<table id="tableHotelRoomPrice_CompetitionPrice" style="margin-bottom: 10px;">
							<tr>
								<td style="width: 120px;" class="tr">批量设置同行价：</td>
								<td style="width: 300px;">
									<select style="width: 80px;" id="slChangType">
										<option value="增加">增加</option>
										<option value="减少">减少</option>
									</select> 
									<input data-type="amount" style="width: 80px" type="text" id="txtChangePrice" maxlength="9"/>
									<input type="button" id="btnSetCompetitionPrice" value="确定" class="btn btn-info" style="width: auto;height: 28px"/>
								</td>
								<td>混住费用：</td>
								<td style="width: 150px;"><input data-type="amount" maxlength="9" id="txtMixedPrice" class="tr" style="width: 80px;" type="text" value="<fmt:formatNumber value='${hotelPl.mixliveAmount }' pattern="#0.00"/>" />/次</td>
							</tr>
						</table>

						<div class="sub_main_bands_sel">
							<ul style="position: absolute; left: 35px; margin: 0px 35px 0px 0px;" id="tabHeader_HotelRoomPrice">
							</ul>
							<i class="sub_main_bands_sel_l_greys"></i> <i class="sub_main_bands_sel_r_greys"></i>
						</div>
						<div class="sub_main_bands" id="dvContainer_HotelRoomPrice">
						</div>
						<div class="kong"></div>
						<div class="tc">
							<input type="button" class="btn btn-primary save_button" id="btnSaveHotelRoomPrice" value="保存" />
						</div>
                        <input type="hidden" id="hotelHotelRoomPriceSaveFlag" />
					</div>
				</div>
				<!--交通费用信息-->
				<div class="price_region">
					<div class="ydbz_tit">
						<span class="ydExpand closeOrExpand"></span>交通费用 
						<span class="fr padr10"> 
							<a id="btnToggleIslandWay" class="btn_open_in_bands_hide">展开全部交通费用</a>
							<button id="btnAddIslandWay" class="btn btn-primary edit_button" style="width: auto">新增上岛方式及日期</button>
							<button id="btnEditIslandWay" class="btn btn-primary region_disabled" onclick="editButtonCli(this);" style="width: auto;">修改</button>
						</span>
					</div>
					<div>
						<div class="sub_main_bands_sel">
							<i class="sub_main_bands_sel_l_greys"></i>
							<ul id="tabHeader_IslandWay">
							</ul>
							<i class="sub_main_bands_sel_r_actives"></i>
						</div>
						<div class="sub_main_bands" id="dvContainer_IslandWay">

						</div>
						<div class="kong"></div>
						<div class="tc">
							<button class="btn btn-primary save_button" id="btnSaveIslandWay">保存</button>
						</div>
                        <input type="hidden" id="hotelIslandWaySaveFlag" />
					</div>
				</div>
				<!--升餐费用信息-->
				<div class="price_region">
					<div class="ydbz_tit">
						<span class="ydExpand closeOrExpand"></span>升餐费用
						<span class="fr padr10"> <a id="btnToggleRiseMeal" class="btn_open_in_bands_hide">展开全部升餐费用</a>
							<button id="btnAddRiseMeal" class="btn btn-primary edit_button" style="width: auto">新增升级餐型</button>
							<button id="btnEditRiseMeal" class="btn btn-primary region_disabled" onclick="editButtonCli(this);" style="width: auto">修改</button>
						</span>
					</div>
					<div>
						<div class="sub_main_bands_sel">
							<i class="sub_main_bands_sel_l_greys"></i>
							<ul id="tabHeader_BaseMeal">
							</ul>
							<i class="sub_main_bands_sel_r_actives"></i>
						</div>
						<div class="sub_main_bands" id="dvContainer_BaseMeal"></div>
						<div class="kong"></div>
						<div class="tc">
							<button class="btn btn-primary save_button" id="btnSaveRiseMeal">保存</button>
						</div>
                        <input type="hidden" id="hotelRiseMealSaveFlag" />
					</div>
				</div>
				<!--节日餐信息-->
				<div class="price_region">
					<div class="ydbz_tit">
						<span class="ydExpand closeOrExpand"></span>强制性节日餐 <span class="fr padr10">
							<button id="btnAddHolidayMeal" class="btn btn-primary edit_button" style="width: auto">新增节日餐</button>
							<button id="btnEditHolidayMeal" class="btn btn-primary" onclick="editButtonCli(this);" style="width: auto">修改</button> 
						</span>
					</div>
					<div>
						<div datarow-emptyrow-behavior="hide" class="hide">
							<table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new" id="tbHolidayMeal">
								<thead>
									<tr>
										<th width="150px"><span class="xing">*</span>节日餐名称</th>
										<th width="310px"><span class="xing">*</span>日期</th>
										<th name="theadOperation" width="50px">操作</th>
									</tr>
								</thead>
								<tbody></tbody>
							</table>
							<table style="width: 100%;margin-top: 10px">
								<tr>
									<td class="tr" style="width:80px;vertical-align: top;">备注：</td>
									<td><textarea style="width:99%;height: 50px;" name="galamealMemo">${hotelPl.galamealMemo }</textarea>
                                    </td>
								</tr>
							</table>
						</div>
						<div class="kong"></div>
						<div class="tc">
							<button class="btn btn-primary" id="btnSaveHolidayMeal">保存</button>
						</div>
                        <input type="hidden" id="hotelHolidayMealSaveFlag" />
					</div>
				</div>
				<!--优惠信息-->
				<div class="price_region">
					<div class="ydbz_tit">
						<span class="ydExpand closeOrExpand"></span>优惠信息 <span class="fr padr10">
							<button id="btnAddPromotion" class="btn btn-primary edit_button" style="width: auto; ">新增优惠信息</button>
							<button id="btnEditPromotion" class="btn btn-primary" onclick="editButtonCli(this);" style="width: auto">修改</button>
						</span>
					</div>
					<div>
						<div id="favorTemp" style="display:none;" uuid="">
							<div class="preferential_information_list">
								<p class="hotel_house_price_show" style="display:none;">
									优惠名称：<span></span>
								</p>
								<div class="mod_information_d2 ">
									下单代码：<span></span>
								</div>
								<div class="mod_information_d2 ">
									入住日期：<span></span>
								</div>
								<div class="mod_information_d2 ">
									预订日期：<span></span>
								</div>
							</div>
							<table class="table activitylist_bodyer_table_new sea_rua_table favorTable">
								<thead>
									<tr>
										<th width="8%">适用房型</th>
										<th width="5%">基础餐型</th>
										<th width="4%">交通</th>
										<th width="13%">优惠事项</th>
										<th width="7%">要求</th>
										<th width="4%">优惠关联</th>
										<th width="4%">操作</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="tc"></td>
										<td class="tc"></td>
										<td class="tc"></td>
										<td class="tc">
											<p class="tl">
												优惠类型：<span class="favorType"></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="favorTypeOutHtml"></span>
											</p>
											<p class="favorContent"></p>
											<div class="houseCharge" style="display:none;">
												<p class="tl">
													<strong>房费</strong>
												</p>
												<table
													class="table activitylist_bodyer_table_new contentTable_preventive">
													<thead>
														<tr>
															<th width="15%">游客类型</th>
															<th width="30%">优惠方式</th>
															<th width="25%">加税</th>
														</tr>
													</thead>
													<tbody>
														<tr>
															<td class="tc"></td>
															<td class="tl"></td>
															<td class="tl"></td>
														</tr>
													</tbody>
												</table>
											</div>
											<div class="mealCharge" style="display:none;">
												<p class="tl">
													<strong>餐费</strong>
												</p>
												<table class="table activitylist_bodyer_table_new contentTable_preventive">
													<thead>
														<tr>
															<th width="15%">游客类型</th>
															<th width="30%">餐型</th>
															<th width="30%">优惠方式</th>
														</tr>
													</thead>
													<tbody>
														<tr>
															<td class="tc"></td>
															<td class="tl"></td>
															<td class="tl"></td>
														</tr>
													</tbody>
												</table>
											</div>
											<div class="trafficCharge" style="display:none;">
												<p class="tl">
													<strong>交通费</strong>
												</p>
												<table class="table activitylist_bodyer_table_new contentTable_preventive">
													<thead>
														<tr>
															<th width="15%">游客类型</th>
															<th width="30%">交通</th>
															<th width="30%">优惠方式</th>
														</tr>
													</thead>
													<tbody>
														<tr>
															<td class="tc"></td>
															<td class="tl"></td>
															<td class="tl"></td>
														</tr>
													</tbody>
												</table>
											</div>
											<div class="taxDiv" style="display:none; text-align:left;">

                                            </div>
											<p class="tl comment"></p></td>
										<td class="tl"></td>
										<td class="tl"></td>
										<td class="p0">
											<dl class="handle">
												<dt>
													<img title="操作" src="${ctxStatic}/images/handle_cz.png" />
												</dt>
												<dd class="">
													<p>
														<span></span> 
														<a class="copyLink" href="javascript:void(0)">复制并新增</a> 
														<a class="updateLink" href="javascript:void(0)">修改</a> 
														<a class="delLink" href="javascript:void(0)">删除</a>
													</p>
												</dd>
											</dl>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
						<div id="favor_nav" class="sub_main_bands_sel">
							<ul style="position:absolute; left:35px; margin:0px 35px 0px 0px;"></ul>
							<i class="sub_main_bands_sel_l_greys"></i> <i class="sub_main_bands_sel_r_greys"></i>
						</div>
						<div id="favor" class="sub_main_bands"></div>
						<div class="kong"></div>
						<div class="tc">
							<button class="btn btn-primary save_button" id="btnSavePromotion">保存</button>
						</div>
						<input id="preferentialSaveFlag" type="hidden"/>
					</div>
				</div>
				<!--附件-->
                <div class="price_region">
                    <div class="ydbz_tit">
                        <span class="ydExpand closeOrExpand"></span>上传资料
                        <span class="fr padr10">
                                 <button id="btnEditAttach" class="btn btn-primary edit_button" onclick="editButtonCli(this);" style="width: auto">修改</button>
                        </span>
                    </div>
                    <div class="batch" style="margin:10px 0px 0px 30px; ">
                        <table style="width: 100%" >
                            <tr>
                                <td class="tr" style="width: 80px; vertical-align: top;padding-top: 8px" rowspan="2" >上传资料：</td>
                                <td>
                                    <input type="button" value="上传文件" class="btn btn-primary" onclick="uploadFiles('${ctx}','',this,'false')"/>
                                   
                                </td>
                                 </tr>
                                <tr>
                                <td style="padding-left: 15px">
	                                 <ul style="margin-left:0;">
	                                 	<c:forEach items="${annexList }" var="annex">
				                          <li>
				                              <i class="sq_bz_icon"></i>
				                              <a onclick="downloads(${annex.docId})">${annex.docName }</a>
				                              <input type="hidden" name="docId" value="${annex.docId}"/>
											  <input type="hidden" name="docName" value="${annex.docName}"/>
											  <input type="hidden" name="docPath" value="${annex.docPath}"/>
				                              <i class="download_icon" onclick="downloads(${annex.docId})"></i>&nbsp;&nbsp;&nbsp;&nbsp;
				                              <a class="batchDel handle region_disabled" href="javascript:void(0)" onclick="deleteFileInfo(null,'hotelAnnexDocId',this)">删除</a>
				                          </li>
                      				     </c:forEach> 
									</ul>
                                </td>
                                </tr>
                           
                        </table>
                    </div>
                    <div class="tc">
                        <button class="btn btn-primary save_button" id="btnSaveAttach" onclick="saveBtnSaveAttach(this);">保存</button>
                    </div>
                </div>
				<!--酒店备注-->
				<div class="price_region">
					<div class="ydbz_tit">
						<span class="ydExpand closeOrExpand"></span>酒店备注 <span class="fr padr10">
							<button id="btnEditOtherInfo" class="btn btn-primary edit_button" onclick="editButtonCli(this);" style="width: auto">修改</button> 
						</span>
					</div>
					<div class="other_info" id="otherInfo" style="padding-top:0px;padding-bottom:0px;">
						<table style="width: 100%">
							<tbody>
								<tr>
									<td class="tr" style="width: 80px; vertical-align: top;padding-top: 8px" rowspan="2">备注：</td>
									<td>
										<textarea style="width: 99%;height: 80px;" name="hotelPlMemo">${hotelPl.memo }</textarea>
                                     </td>
								</tr>
							</tbody>
						</table>
						<div class="kong"></div>
						<div class="tc">
							<button class="btn btn-primary save_button" id="btnSaveOtherInfo">保存</button>
						</div>
					</div>
				</div>
				<div style="clear:none;" class="kong"></div>
			</div>
		</div>
		<!--右侧内容部分结束-->
	</div>
	<!--右侧内容部分结束2-->
	<!--弹出窗开始-->
	<div style="display:none">
		<div id="popHotelRoomPrice">
			<table style="width: 100%;margin-top: 15px;">
				<tr>
					<td class="tr" style="width: 100px; vertical-align: top;" rowspan="2">房型：</td>
					<td>
						<label> <input type="checkbox" style="margin: 0px" checkbox-action="All" hotel-room-uuid="All" checked="checked" />全部</label>
					</td>
				</tr>
				<tr>
					<td id="pop_roomPrice_roomType">
						<!--to bind:需要从服务器获取房型来绑定-->
					</td>
				</tr>
				<tr class="roomPriceDate">
					<td class="tr"><span class="xing">*</span>日期：</td>
					<td>
						<input type="text" onclick="WdatePicker()" class="dateinput w106 required " name="dtpRoomPriceStartDate" readonly="readonly" /> 
						至
						<input type="text" onclick="WdatePicker()" class="dateinput w106 required " name="dtpRoomPriceEndDate" readonly="readonly" /> 
						<i class="price_sale_house_01" id="btnAddRoomPriceDate"></i>
					</td>
				</tr>
			</table>
		</div>
		<div id="popIslandWay">
			<table style="width: 100%;margin-top: 15px;">
				<tr>
					<td class="tr" style="width: 100px; vertical-align: top;" rowspan="2">上岛方式：</td>
					<td>
						<label> <input type="checkbox" checked="checked" style="margin: 0px" name="IslandWay" checkbox-action="All" island-way-uuid="All" />全部</label>
					</td>
				</tr>
				<tr>
					<td id="pop_IslandWay"></td>
				</tr>
				<tr class="islandLoadDate">
					<td class="tr"><span class="xing">*</span>日期：</td>
					<td>
						<input type="text" onclick="WdatePicker()" class="dateinput w106 required " name="dtpIslandWayStartDate" readonly="readonly" /> 
						至 
						<input type="text" onclick="WdatePicker()" class="dateinput w106 required " name="dtpIslandWayEndDate" readonly="readonly" /> 
						<i class="price_sale_house_01" id="btnAddIslandWayDate"></i>
					</td>
				</tr>
			</table>
		</div>
		<div id="popBaseMeal">
			<table style="width: 100%;margin-top: 15px;">
				<tr>
					<td class="tr" style="width: 100px; vertical-align: top;">基础餐型：</td>
					<td id="pop_BaseMeal"></td>

				</tr>
				<tr>
					<td class="tr" style="width: 100px; vertical-align: top;">升级餐型：</td>
					<td id="pop_riseMeal"></td>

				</tr>
				<tr class="riseDate">
					<td class="tr"><span class="xing">*</span>日期</td>
					<td>
						<input type="text" onclick="WdatePicker()" class="dateinput w106 required " name="dtpRiseMealStartDate" readonly="readonly" /> 至 
						<input type="text" onclick="WdatePicker()" class="dateinput w106 required " name="dtpRiseMealEndDate" readonly="readonly" /> 
						<i class="price_sale_house_01" id="btnAddRiseMealDate"></i>
					</td>
				</tr>
			</table>
		</div>
		<div id="popAddPromotion">
			<div style="max-height: 500px;overflow: auto;">
				<table style="width:900px;margin: 10px auto;" cellpadding="0" cellspacing="0" class=" table table activitylist_bodyer_table_new contentTable_preventive mar_topnone10 houseChargeTable ">
					<tr>
						<td class="tr new_hotel_p_table2_tdblue " style="width: 130px"><span class="xing">*</span>优惠名称：</td>
						<td colspan="3">
							<input id="txtPromotionName" type="text" style="width: 243px" maxlength="50" />
						</td>
					</tr>
					<tr>
						<td class="tr new_hotel_p_table2_tdblue ">下单代码：</td>
						<td colspan="3">
							<input id="txtOrderCode" type="text" style="width: 243px" maxlength="20" />
						</td>
					</tr>
					<tr>
						<td class="tr new_hotel_p_table2_tdblue">入住日期：</td>
						<td style="width: 260px">
							<input type="text" id="txtCheckInDate" data-type="date" data-group="rzDate" class="dateinput w106 required " readonly="readonly"/> 至
							<input type="text" id="txtCheckOutDate" data-type="date" data-group="rzDate" class="dateinput w106 required " readonly="readonly" />
						</td>
						<td class="tr new_hotel_p_table2_tdblue" style="width: 100px">预定：</td>
						<td>
							<input type="text" id="txt" data-type="date" data-group="ydDate" class="dateinput w106 required " readonly="readonly" /> 至 
							<input type="text" data-type="date" data-group="ydDate" class="dateinput w106 required " readonly="readonly" />
						</td>
					</tr>
					<tr class="applyHouseTypeTr">
						<td class="tr new_hotel_p_table2_tdblue"><span class="xing">*</span>适用房型：</td>
						<td>
							<select style="width: 119px" class="houseTypeSel suitRommType"></select>
							* <input type="text" data-type="number" data-min="1" style="width: 40px" class="roomNight" value="1" /> 晚数 
							<i class="price_sale_house_01"></i></td>
						<td class="tr new_hotel_p_table2_tdblue"><span class="xing">*</span>基础餐型：</td>
						<td style="padding-top: 6px"></td>
					</tr>
					<tr class="trafficTr">
						<td class="tr new_hotel_p_table2_tdblue">交通：</td>
						<td colspan="3">
							<label> 
								<input type="checkbox" value="0" class="chkAll mar_10 mar_topnone10" />全部
							</label> 
							<c:forEach items="${islandWayList }" var="islandWay">
								<label><input class="mar_10 mar_topnone10" type="checkbox" value="${islandWay.uuid }" data-text="${islandWay.label }" />${islandWay.label }</label> 
							</c:forEach>
						</td>
					</tr>
					<tr class="relevancyHotelTr">
						<td class="tr new_hotel_p_table2_tdblue">
							<label> <input class="mar_10 mar_topnone10" type="checkbox" />关联酒店：</label>
						</td>
						<td colspan="3">
							<select style="width: 256px" disabled="disabled" id="relevancyHotel">
							</select>
						</td>
					</tr>
					<tr class="relevancyHouseTypeTr" style="display:none;">
						<td class="tr new_hotel_p_table2_tdblue">关联适用房型：</td>
						<td>
							<select style="width: 119px" class="relSuitRommType"></select> * 
							<input type="text" data-type="number" data-min="1" style="width: 40px" class="roomNight" value="1" /> 晚数 <i class="price_sale_house_01"></i>
						</td>
						<td class="tr">关联基础餐型：</td>
						<td style="padding-top: 6px"></td>
					</tr>
					<tr class="relevancyTrafficTr" style="display:none;">
						<td class="tr new_hotel_p_table2_tdblue">关联交通：</td>
						<td colspan="3">
							<label> <input type="checkbox" style="margin: 0px" class="chkAll mar_10 mar_topnone10" />全部</label> 
							<c:forEach items="${islandWayList }" var="islandWay">
								<label><input class="mar_10 mar_topnone10" type="checkbox" value="${islandWay.uuid }" style="margin: 0px" data-text="${islandWay.label }" />${islandWay.label }</label> 
							</c:forEach>
						</td>
					</tr>
					<tr>
						<td class="tr valign_top new_hotel_p_table2_tdblue">优惠事项：</td>
						<td colspan="3" style="padding: 0px">
							<table width="100%" class="table-noborder">
								<tr>
                                    <td colspan="4" class="tl">
                                        <label class="w100_30">优惠类型：</label>
                                        <select class="favorType">
                                        	<c:forEach items="${templateses }" var="templates" varStatus="status">
                                            	<option value="${templates.uuid }" data-favorType="${templates.type }">${templates.name }</option>
                                        	</c:forEach>
                                        </select>
                                    </td>
                                </tr>
								<tr>
                                    <td colspan="4" class="tl favorTypeOutHtml">
                                    	
                                    </td>
                                </tr>
								<tr class="chargeTr">
									<td colspan="4" class="tr">
										<div>
											<div class="lib_Menubox">
												<ul>
													<li class="hover">房费</li>
													<li>餐费</li>
													<li>交通费</li>
												</ul>
											</div>
											<div class="lib_Contentbox">
												<div>
													<table class="table activitylist_bodyer_table_new contentTable_preventive mar_topnone10 houseChargeTable">
														<thead>
															<tr>
																<th width="10%">游客类型</th>
																<th width="28%">优惠方式</th>
																<th width="30%">加税</th>
																<th width="7%">操作</th>
															</tr>
														</thead>
														<tbody>
															<c:forEach items="${travelerTypeList }" var="travelerType">
																<tr>
																	<td class="tc" data-value="${travelerType.uuid }">${travelerType.name }</td>
																	<td class="tl">
																		<select name="select13" class="w80_30 selectWithCurrMark">
																			<option value="0">请选择</option>
																			<option value="1" data-unit="$" data-unitId="2">合计</option>
																			<option value="2" data-unit="%" data-unitid="1">打折</option>
																			<option value="3" data-unit="$" data-unitid="2">减金额</option>
                                                                            <option value="4">减最低</option>
																		</select> 
																		<span style="ime-mode: disabled; display:none;" class="currencySpan">
																			<em class="gray14" style="position: absolute;top:5px;left: 5px;">$<em></em></em>
																			<input name="txtPreferentialPrice" data-type="float" class="inputTxt w50_30 spread currency amt" type="text" amt-code="" currency=""/>
																		</span>
																		<span style="display:none;" class="sign"></span>
																	</td>
																	<td class="tl">
																		<input class="mar_10 mar_topnone10" type="checkbox" data-text="政府税" value="1" />政府税 
																		<input class="mar_10 mar_topnone10" type="checkbox" data-text="服务税" value="2" />服务税 
																		<input class="mar_10 mar_topnone10" type="checkbox" data-text="床税" value="3" />床税
																		<input class="mar_10 mar_topnone10" type="checkbox" data-text="环保税" value="4"/> 环保税
																	</td>
																	<td class="tc"><a class="resetLink">重置</a>
																	</td>
																</tr>
															</c:forEach>
														</tbody>
													</table>
												</div>
												<div style="display:none">
													<table
														class="table activitylist_bodyer_table_new contentTable_preventive mar_topnone10 mealChargeTable">
														<thead>
															<tr>
																<th width="10%">游客类型</th>
																<th width="25%">餐型</th>
																<th width="28%">优惠方式</th>
                                                                <th width="30%" style="display:none;">加税</th>
																<th width="7%">操作</th>
															</tr>
														</thead>
														<tbody>
															<c:forEach items="${travelerTypeList }" var="travelerType" varStatus="status">
																<tr>
																	<td class="tc" data-value="${travelerType.uuid }">${travelerType.name }</td>
																	<td class="tl"><span class=""><input type="checkbox" style="margin: 0px" class="chkAll" data-text="全部" data-group="mc${status.count }" /> 全部</span></td>
																	<td class="tl">
																		<select name="select13" class="w80_30 selectWithCurrMark">
																			<option value="0">请选择</option>
																			<option value="1" data-unit="$" data-unitid="2">合计</option>
																			<option value="2" data-unit="%" data-unitid="1">打折</option>
																			<option value="3" data-unit="$" data-unitid="2">减金额</option>
																			<option value="4">减最低</option>
																		</select> 
																		<span style="ime-mode: disabled; display:none;" class="currencySpan">
																			<em class="gray14" style="position: absolute;top:5px;left: 5px;">$<em></em></em>
																			<input name="txtPreferentialPrice" data-type="float" class="inputTxt w50_30 spread currency amt" type="text" amt-code="" currency=""/>
																		</span>
																		<span style="display:none;" class="sign"></span>
																	</td>
																	<td class="tl" style="display:none;">
																		<input class="mar_10 mar_topnone10" type="checkbox" data-text="政府税" value="1" />政府税
																		<input class="mar_10 mar_topnone10" type="checkbox" data-text="服务税" value="2" />服务税
																		<input class="mar_10 mar_topnone10" type="checkbox" data-text="床税" value="3" />床税
																	</td>
																	<td class="tc"><a class="resetLink">重置</a>
																	</td>
																</tr>
															</c:forEach>
														</tbody>
													</table>
												</div>
												<div style="display:none">
													<table class="table activitylist_bodyer_table_new contentTable_preventive mar_topnone10 trafficChargeTable">
														<thead>
															<tr>
																<th width="10%">游客类型</th>
																<th width="25%">交通</th>
																<th width="28%">优惠方式</th>
																<th width="30%" style="display:none;">加税</th>
																<th width="7%">操作</th>
															</tr>
														</thead>
														<tbody>
															<c:forEach items="${travelerTypeList }" var="travelerType" varStatus="status">
																<tr>
																	<td class="tc" data-value="${travelerType.uuid }">${travelerType.name }</td>
																	<td class="tl"><span><input type="checkbox" style="margin: 0px" data-text="全部" class="chkAll" data-group="tc${status.count }" /> 全部</span></td>
																	<td class="tl">
																		<select name="select13" class="w80_30 selectWithCurrMark">
																			<option value="0">请选择</option>
																			<option value="1" data-unit="$" data-unitid="2">合计</option>
																			<option value="2" data-unit="%" data-unitid="1">打折</option>
																			<option value="3" data-unit="$" data-unitid="2">减金额</option>
																			<option value="4">减最低</option>
																		</select>
																		
																		<span style="ime-mode: disabled; display:none;" class="currencySpan">
																			<em class="gray14" style="position: absolute;top:5px;left: 5px;">$<em></em></em>
																			<input name="txtPreferentialPrice" data-type="float" class="inputTxt w50_30 spread currency amt" type="text" amt-code="" currency=""/>
																		</span>
																		<span style="display:none;" class="sign"></span>
																	</td>
																	<td class="tl" style="display:none;">
																		<input class="mar_10 mar_topnone10" type="checkbox" data-text="政府税" value="1" />政府税 
																		<input class="mar_10 mar_topnone10" type="checkbox" data-text="服务税" value="2" />服务税 
																		<input class="mar_10 mar_topnone10" type="checkbox" data-text="床税" value="3" />床税
																	</td>
																	<td class="tc"><a class="resetLink">重置</a>
																	</td>
																</tr>
															</c:forEach>
														</tbody>
													</table>
												</div>
											</div>
										</div>
									</td>
                                    <td class="tl" colspan="4" style="display:none;">
                                        <input class="mar_10 mar_topnone10" type="checkbox" data-text="政府税" value="1" />政府税
                                        <input class="mar_10 mar_topnone10" type="checkbox" data-text="服务税" value="2" />服务税
                                        <input class="mar_10 mar_topnone10" type="checkbox" data-text="床税" value="3" />床税
										<input class="mar_10 mar_topnone10" type="checkbox" data-text="环保税" value="4"/> 环保税
                                    </td>
								</tr>
								<tr>
									<td width="15%" class="tr">备注：</td>
									<td width="85%" colspan="3" class="tl">
										<textarea rows="10" style="width: 95%;height: 50px;"></textarea>
									</td>
								</tr>
							</table></td>
					</tr>
					<tr>
						<td class="tr valign_top new_hotel_p_table2_tdblue">要求：</td>
						<td colspan="3" style="padding: 0px">
							<table width="100%" class="table-noborder">
								<tr>
									<td colspan="2" class="tl">
										<span><label class="w100_30">起订晚数：</label><input data-type="number" data-min="1" class="inputTxt w116_30 spread minNight" type="text" /> 晚</span> 
										<span><label class="w100_30">起订间数：</label><input data-type="number" data-min="1" class="inputTxt w116_30 spread minRoom" type="text" /> 间</span>
									</td>
								</tr>
								<tr>
									<td class="tl"><label class="w100_30">不适用日期：</label>
									</td>
									<td class="tl NADateTd">
									<span class="new_price_list_pop_lb fl" style="width: 50%"> 
										<input type="text" data-type="date" name="dtpRoomPriceStartDate" data-group="dtpRoomPriceDate" class="dateinput w106 required " readonly="readonly" /> 
										至
										<input type="text" name="dtpRoomPriceEndDate" data-type="date" data-group="dtpRoomPriceDate" class="dateinput w106 required " readonly="readonly" /> 
										<i class="price_sale_house_01"></i>
									</span>
									</td>
								</tr>
								<tr style="display:none;">
									<td width="14%" class="tl"><label class="w100_30">不适用房型：</label>
									</td>
									<td width="86%" class="tl NAHouseTypeTd">
										<span class="new_price_list_pop_lb">
											<select name="select" class="w116_30">
											</select> 
											<i class="price_sale_house_01"></i>
										</span>
									</td>
								</tr>
								<tr>
									<td colspan="2" class="tl">
										<span>
											<label class="w100_30">适用第三人：</label>
											<input class="mar_top0 mar_10" name="applicableThirdPerson" type="radio" checked="checked" />是 
											<input class="mar_top0 mar_10" name="applicableThirdPerson" type="radio" /> 否
										</span>
									</td>
								</tr>
								<tr>
									<td colspan="2" class="tl">
										<span>
											<label class="w100_30">优惠叠加：</label>
											<input class="mar_top0 mar_10" name="allowSuperposed" type="radio" checked="checked" />允许
											<input class="mar_top0 mar_10" name="allowSuperposed" type="radio" />不允许
										</span>
									</td>
								</tr>
								<tr>
									<td class="tr" width="15%">备注：</td>
									<td>
										<textarea rows="10" style="width: 95%;height: 50px;"></textarea>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr style="display:none;" class="preferentialRel">
						<td class="tr valign_top new_hotel_p_table2_tdblue">优惠关联：</td>
						<td colspan="3" style="padding: 0px">
						</td>
					</tr>
					<tr>
						<td class="tr valign_top new_hotel_p_table2_tdblue"><span class="xing">*</span>优惠描述：</td>
						<td colspan="3" class="preferentialDesc t1">
							<textarea rows="10" style="width: 95%;height: 50px;"></textarea>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<!--弹出窗结束-->
</body>
</html>