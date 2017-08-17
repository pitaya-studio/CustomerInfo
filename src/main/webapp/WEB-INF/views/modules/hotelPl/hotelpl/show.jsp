<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>酒店价单维护详情</title>
    <meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <!--[if lte IE 6]>
        <link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
        <script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
    <![endif]-->
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
    <style type="text/css">
        .sub_main_bands .hotel_house_price_show, .sub_main_bands > div {
            display: none;
        }

        .sub_main_bands_unfold .hotel_house_price_show, .sub_main_bands_unfold > div {
            display: block;
        }
        
        .btn-primary {
		    background: #5f7795 none repeat scroll 0 0;
		    border: medium none;
		    box-shadow: none;
		    color: #fff;
		    font: 12px Arial,"微软雅黑";
		    height: 28px;
		    padding-left: 15px;
		    padding-right: 15px;
		    text-shadow: none;
		    width: 72px;
		}
		
		.btn-primary:hover, .btn-primary:focus, .btn-primary:active, .btn-primary.active, .btn-primary.disabled, .btn-primary[disabled]{
			background:#28b2e6;
			color:#fff;
		}
		
		
		.ellipsis{
			text-overflow: ellipsis; overflow: hidden; white-space: nowrap;
		}
    </style>
    
    <script type="text/javascript">
    	var updateFlag = 2;
		$(document).ready(function(){
	        //基本信息
	        //todo 需要后台处理数据
			//初始化基础信息
	        baseInfo=${baseInfo};
	        bindBaseInfo(baseInfo);
	        
	        //酒店房型价格基础数据
			hotelRoomMap = ${hotelRoomList};
			initRoomTypePrice();
			
			//绑定交通方式基础信息
			islandWays = ${islandWays};
			initIslandWay();
			
			hotelMeals = ${hotelMeals};
	        initAdditionalMeal();
			
			//酒店
	        var roomPrices = ${roomPrices };
	        var hotelPlRoomMemoList = ${hotelPlRoomMemoList};
	        bindDetailRoomTypePrices(roomPrices,hotelPlRoomMemoList);
	        
	        //酒店税金
	        var hotelPlTaxPriceList = ${hotelPlTaxPriceList};
	        bindDetailTaxDictionary(hotelPlTaxPriceList);
	        
	        //税金例外
	        var hotelPlTaxExceptionList = ${hotelPlTaxExceptionList};
	        bindDetailTaxException(hotelPlTaxExceptionList);
	        
	        //交通费用
	        var islandWayPriceList = ${islandWayPriceList};
	        var islandWayMemoList = ${islandWayMemoList};
	        bindDetailIslandWay(islandWayPriceList,islandWayMemoList);
	        
	        //升餐费用
	        var hotelRiseMealMap = ${hotelRiseMealMap};
	        var hotelPlRiseMealMemoList = ${hotelPlRiseMealMemoList};
	        bindDetailRiseMeal(hotelRiseMealMap,hotelPlRiseMealMemoList);
	
	        //强制节日餐
	        var hotelPlHolidayMealList = ${hotelPlHolidayMealList};
	        bindDetailHolidayMeal(hotelPlHolidayMealList);
	        
	        $("#hotel_hotelGroup").text(baseInfo.hotelGroup);
		    $("#hotel_hotelStar").text(baseInfo.hotelStar);
		    $("#hotel_address").text(baseInfo.hotelAddress);
		    $("#hotel_tel").text(baseInfo.contactPhone);
	        
		});
		</script>
		<script type="text/javascript">
		
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
		 function PrintPrice(){
             var $content = $('.bgMainRight').clone();
             $content.css({'padding':'20px'});
             $content.find('td,th').css({height:'auto'});
             $content.find('#ofAnchor1').css({'margin-top':'0px'});


             $content.find('.activitylist_bodyer_right_team_co2').css({'height':'20px'});
             $content.find('.activitylist_bodyer_right_team_co1').css({'min-width': '180px','height':'20px'});

             $content.find('.sub_main_bands_sel').remove();
             $content.find('.sub_main_bands').children().show();

             $content.find('.ydbz_tit').css({'padding-left':'5px','margin-top':'20px','margin-bottom':'10px'}).next().show();

//             $content.find('.ydbz_tit:first').css({'margin-top':'0px'});

				$content.find(".title_tabScroll_list").remove();
			    $content.find("[name='tabContent_HotelRoomPrice']").has("[name='tabContent_BaseMeal_HotelRoomPrice']").each(function (index) {
			        var $this = $(this);
			        var roomType = $this.attr("hotel-room-uuid");
			        var hotelRoomText = $("#tabHeader_HotelRoomPrice").find("[hotel-room-uuid='" + roomType + "']").text();
			        var titleTheme = "<div class='title_tabScroll_list'>" + (index + 1) + "." + hotelRoomText + "</div>"
			        $this.prepend(titleTheme);
			    });		
			    $content.find("[name='tabContent_IslandWay']").has("[name='tbIslandWay']").each(function (index) {
			        var $this = $(this);
			        var islandWay = $this.attr("island-way-uuid");
			        var islandWayText = $("#tabHeader_IslandWay").find("[island-way-uuid='" + islandWay + "']").text();
			        var titleTheme = "<div class='title_tabScroll_list'>" + (index + 1) + "." + islandWayText + "</div>"
			        $this.prepend(titleTheme);
			    });
			    $content.find("[name='tabContent_BaseMeal']").has("[name='tbRiseMeal']").each(function (index) {
			        var $this = $(this);
			        var baseMealUuid = $this.attr("base-meal-uuid");
			        var baseMealText = $("#tabHeader_BaseMeal").find("[base-meal-uuid='" + baseMealUuid + "']").text();
			        var titleTheme = "<div class='title_tabScroll_list'>" + (index + 1) + "." + baseMealText + "</div>"
			        $this.prepend(titleTheme);
			    });
			    
             $content.find('.title_tabScroll_list').show();
             
             $content.find('#favor').children().show();
             $content.find('.hotel_house_price_show').show();
             
            
             $content.find('.ydbz_tit').next().find('.hotel_house_price_show').css({'margin-top':'10px','margin-bottom':'5px'});
             $content.find('.ydbz_tit').next().find('.hotel_house_price_show:first').css({'margin-top':'0px'});

             $content.find('.sub_mian_bands_det').css({'margin-top': '5px','margin-bottom':'5px','height':'20px','line-height':'20px'});

             $content.find('.mod_information_d2 ').css({'min-width':'210px'});

             $content.find('.note_msg_bg_tb').css({'margin-top':'5px'});
             $content.find('.note_msgs').css({'height': 'auto','overflow-y':'hidden'});
             $content.find('input').remove();
             $content.find('.ydExpand ').remove();
             $content.find('.download_icon ').remove();

            printPage($content.find('.produceDiv'));
         }
         function pagesetup_null() {
             try {
                 var RegWsh = new ActiveXObject("WScript.Shell");
                 hkey_key = "header";
                 RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "");
                 hkey_key = "footer";
                 RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "");
                 hkey_key = "margin_left";
                 RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.0");
                 hkey_key = "margin_right";
                 RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.0");
                 hkey_key = "margin_top";
                 RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.0");
                 hkey_key = "margin_bottom";
                 RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.0");
             } catch (e) {}
         }
         function pagesetup_default() {
             try {
                 var RegWsh = new ActiveXObject("WScript.Shell");
                 hkey_key = "header";
                 RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "&w&b页码，&p/&P");
                 hkey_key = "footer";
                 RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "&u&b&d");
                 hkey_key = "margin_left";
                 RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.750000");
                 hkey_key = "margin_right";
                 RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.750000");
                 hkey_key = "margin_top";
                 RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.750000");
                 hkey_key = "margin_bottom";
                 RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.750000");
             } catch (e) {}
         }
         function printPage($el) {
             pagesetup_null();
             if ($el) {
                 var $sea =$('#sea')
                 $sea.hide();
                 $('body').append($el);
                 window.print();
                 $el.remove();
                 $sea.show();
             } else {
                 window.print();
             }
             pagesetup_default();
         }

    </script>
    <script type="text/javascript">
        $(function () {
            //搜索条件筛选
            launch_new1();
            launch_new2();
            //产品名称文本框提示信息
            inputTips();
            //操作浮框
            operateHandler();
        });
        //展开筛选按钮
        function launch_new1() {
            $('#zksx1').click(function () {
                if ($('#ydxbd1').is(":hidden") == true) {
                    $('#ydxbd1').show();
                    $(this).text('收起筛选');
                    $(this).addClass('zksx-on');
                } else {
                    $('#ydxbd1').hide();
                    $(this).text('展开筛选');
                    $(this).removeClass('zksx-on');
                }
            });
        }

        function launch_new2() {
            $('#zksx2').click(function () {
                if ($('#ydxbd2').is(":hidden") == true) {
                    $('#ydxbd2').show();
                    $(this).text('收起筛选');
                    $(this).addClass('zksx-on');
                } else {
                    $('#ydxbd2').hide();
                    $(this).text('展开筛选');
                    $(this).removeClass('zksx-on');
                }
            });
        }
        //展开收起
        function expand(child, obj) {
            if ($(child).is(":hidden")) {
                $(child).show();
                $(obj).parents("td").addClass("td-extend");
                $(obj).parents("tr").addClass("tr-hover");
                $(obj).html("收起");
            } else {
                $(child).hide();
                $(obj).parents("td").removeClass("td-extend");
                $(obj).parents("tr").removeClass("tr-hover");
                $(obj).html("展开");
            }
        }
        //展开收起支付记录

        function expandShowPay(child, obj) {
            if ($(child).is(":hidden")) {
                $(child).show();
                $(obj).parents("td").addClass("td-extend");
                $(obj).parents("tr").addClass("tr-hover");
            } else {
                $(child).hide();
                $(obj).parents("td").removeClass("td-extend");
                $(obj).parents("tr").removeClass("tr-hover");
            }
        }
        //展开、关闭

        function expand(child, obj, srcActivityId) {
            if ($(child).css("display") == "none") {
                $(obj).html("收起");
                $(child).show();
                $(obj).addClass('team_a_click2');
                $(obj).parents("td").addClass("td-extend").parent("tr").addClass("tr-hover");
            } else {
                $(child).hide();
                $(obj).removeClass('team_a_click2');
                $(obj).parents("td").removeClass("td-extend").parent("tr").removeClass("tr-hover");
                $(obj).html("展开");
            }
        }

		function downloads(docid){
			window.open("${ctx}/sys/docinfo/download/"+docid);
		}
    </script>
   
</head>
<body>

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
	
       <!--右侧内容部分开始-->
       <div class="mod_nav">产品 &gt; 酒店产品 &gt; 酒店价单详情</div>
          <div class="produceDiv">
          <input type="hidden" value="${hotelPl.uuid }" id="hotelPlUuid"/>
              <!--产品信息开始-->
              <div class="mod_information" id="ofAnchor1">
                  <div class="mod_information mar_top0" id="secondStepDiv">
                     <div class="ydbz_tit"><span class="ydExpand closeOrExpand"></span>基本信息</div>
                      <div style="margin-top: 8px; display: block;" id="productInfo01">
                          <div class="activitylist_bodyer_right_team_co2 wpr20 pr" style="height:40px;">
                              <label class="jbox-width100">地接社供应商：</label>
                              <trekiz:autoId2Name4Table tableName='supplier_info' sourceColumnName='id' srcColumnName='supplierName' value='${hotelPl.supplierInfoId }'/>
                          </div>
                          <div class="kong"></div>
                          <div class="activitylist_bodyer_right_team_co1">
                              <label class="jbox-width100 tr">位置国家：</label>
                              <c:if test="${hotelPl.position==1 }">境内</c:if>
                              <c:if test="${hotelPl.position==2 }">境外</c:if>
                          </div>
                          <div class="activitylist_bodyer_right_team_co1">
                              <label class="jbox-width100 tr">国家：</label>
                              	<trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid"	srcColumnName="name_cn" value="${hotelPl.country}" />
                          </div>
                          <div class="activitylist_bodyer_right_team_co1">
                              <label class="jbox-width100 tr">位置属性：</label>
                               <c:if test="${hotelPl.position==1 }">内陆</c:if>
                               <c:if test="${hotelPl.position==2 }">海岛</c:if>
                          </div>
                          <div class="kong"></div>
                           <div class="activitylist_bodyer_right_team_co1">
                              <label class="jbox-width100 tr">币种：</label>
                              <trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_name" value="${hotelPl.currencyId }" />
                          </div>
                          <div class="activitylist_bodyer_right_team_co1">
                              <label class="jbox-width100 tr">采购类型：</label> 
                              <c:if test="${hotelPl.purchaseType==0 }">内采</c:if>
                              <c:if test="${hotelPl.purchaseType==1 }">外采</c:if>
                          </div>
                          <div class="activitylist_bodyer_right_team_co1">
                              <label class="jbox-width100 tr">混住费：</label>
                              <c:if test="${hotelPl.mixliveAmount!= null}">
                              <trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${hotelPl.mixliveCurrencyId }" />
                              <fmt:formatNumber  type="currency" pattern="#,##0.00" value="${hotelPl.mixliveAmount }" />/次
                              </c:if>
                          </div>
                          <div class="kong"></div>
                          <div class="activitylist_bodyer_right_team_co1">
                              <label class="jbox-width100 tr">岛屿名称：</label>
                              <trekiz:autoId2Name4Table tableName="island" sourceColumnName="uuid" srcColumnName="island_name" value="${hotelPl.islandUuid}" />
                          </div>
                          <div class="activitylist_bodyer_right_team_co2 ellipsis">
                              <label class="jbox-width100 tr">酒店名称：</label>
                              <trekiz:autoId2Name4Table tableName="hotel" sourceColumnName="uuid" srcColumnName="name_cn" value="${hotelPl.hotelUuid }"/>
                          </div>
                        <div class="kong"></div>
                          <div class="activitylist_bodyer_right_team_co1">
                              <label class="jbox-width100 tr">酒店集团：</label>
                              <span id="hotel_hotelGroup"></span>
                          </div>
                          <div class="activitylist_bodyer_right_team_co2">
                              <label class="jbox-width100 tr">酒店等级：</label>
                              <span id="hotel_hotelStar"></span>
                          </div>
                        <div class="kong"></div>
                          <div class="activitylist_bodyer_right_team_co1" style="width: 38%">
                              <label class="jbox-width100 tr">酒店地址：</label>
                             <span id="hotel_address"></span>
                          </div>
                          <div class="activitylist_bodyer_right_team_co2">
								<label class="title-md tr">联系电话：</label><span id="hotel_tel"></span>
							</div>
                      </div>
                  </div>
                  
                  <div class="ydbz_tit" style="clear:both;"><span class="ydExpand closeOrExpand"></span>酒店税金</div>
			<div class="sub_mian_bands_det_other">税金价格字典</div>
			<table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new" id="tableTaxDictionary">
				<thead>
					<tr>
						<th width="7%">税种选择</th>
						<th width="7%">日期设定</th>
						<th width="7%">增收/人</th>
					</tr>
				</thead>
				<tbody>
					<tr tax-type="1" tax-name="政府税" uuid="">
						<td class="tc" name="taxName">政府税</td>
						<td class="tc" name="taxStartEndDate"></td>
						<td class="tc" name="taxAmount"></td>
					</tr>
					<tr tax-type="2" tax-name="服务税" uuid="">
						<td class="tc" name="taxName">服务税</td>
						<td class="tc" name="taxStartEndDate"></td>
						<td class="tc" name="taxAmount"></td>
					</tr>
					<tr tax-type="3" tax-name="床税" uuid="">
						<td class="tc" name="taxName">床税</td>
						<td class="tc" name="taxStartEndDate"></td>
						<td class="tc" name="taxAmount"></td>
					</tr>
					<tr tax-type="4" tax-name="环保税" uuid="">
						<td class="tc" name="taxName">环保税</td>
						<td class="tc" name="taxStartEndDate"></td>
						<td class="tc" name="taxAmount"></td>
					</tr>
				</tbody>
			</table>
			<div class="sub_mian_bands_det">税金例外设置</div>
			<table
				class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new" id="tableTaxExcept">
				<thead>
					<tr>
						<th width="7%">收税例外类型</th>
						<th width="7%">日期</th>
						<th width="7%">例外税种</th>
						<th width="7%">游客类型</th>
					</tr>
				</thead>
				<tbody>
					<tr exception-type="1" exception-name="房型" uuid="">
						<td class="tc" name="exceptionName">房型</td>
						<td class="tc" name="exceptionDate"></td>
						<td class="tl" name="exceptionTax"></td>
						<td name="taxtExceptionTravelList"></td>
					</tr>
					<tr exception-type="2" exception-name="餐型" uuid="">
						<td class="tc" name="exceptionName">餐型</td>
						<td class="tc" name="exceptionDate"></td>
						<td class="tl" name="exceptionTax"></td>
						<td name="taxtExceptionTravelList"></td>
					</tr>
					<tr exception-type="3" exception-name="交通" uuid="">
						<td class="tc" name="exceptionName">交通</td>
						<td class="tc" name="exceptionDate"></td>
						<td class="tl" name="exceptionTax"></td>
						<td name="taxtExceptionTravelList"></td>
					</tr>
				</tbody>
			</table>
			<div class="tax-algorithm">
            	<span>税金算法：</span>
            	<span><label><c:choose><c:when test="${hotelPl.taxArithmetic == 1}">连乘</c:when><c:when test="${hotelPl.taxArithmetic == 2}">分乘</c:when></c:choose></label></span>
            </div>
			<div>
				<div class="ydbz_tit">
					<span class="ydExpand closeOrExpand"></span>酒店房型价格<span class="fr padr10"><input value="展开全部酒店价格" class="btn btn-primary mar_10 btn_open_in_bands" type="button" />
					</span>
				</div>
				<div>
					<table id="tableHotelRoomPrice_CompetitionPrice" style="margin-bottom: 20px; font-weight: bold;">
						<tbody>
							<tr>
								<td>混住费用：</td>
								<td style="width: 150px;"><span>${hotelPl.currencyMark} <fmt:formatNumber value="${hotelPl.mixliveAmount }" pattern="#,##0.00"/></span></span>/次</td>
							</tr>
						</tbody>
					</table>
					<div class="sub_main_bands_sel">
						<ul style="position: absolute; left: 35px; margin: 0px 35px 0px 0px;" id="tabHeader_HotelRoomPrice">
						</ul>
						<i class="sub_main_bands_sel_l_greys sub_main_bands_sel_l_actives"></i>
						<i class="sub_main_bands_sel_r_greys"></i>
					</div>
					<div class="sub_main_bands" id="dvContainer_HotelRoomPrice">
					</div>
			</div>
			<div>
				<div class="ydbz_tit">
					<span class="ydExpand closeOrExpand"></span>交通费用<span class="fr padr10">
						<input value="展开全部交通费用" class="btn btn-primary mar_10 btn_open_in_bands" type="button" />
					</span>
				</div>
				<div>
					<div class="sub_main_bands_sel">
						<ul style="position:absolute; left:35px; margin:0px 35px 0px 0px;" id="tabHeader_IslandWay">
						</ul>
						<i class="sub_main_bands_sel_l_greys"></i>
						<i class="sub_main_bands_sel_r_greys"></i>
					</div>
					<div class="sub_main_bands" id="dvContainer_IslandWay">
					</div>
					<div class="kong"></div>
				</div>
			</div>
			<div>
				<div class="ydbz_tit">
					<span class="ydExpand closeOrExpand"></span>升餐费用<span class="fr padr10">
						<input value="展开全部升餐费用" class="btn btn-primary mar_10 btn_open_in_bands" type="button" />
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
				</div>
			</div>
			<div>
				<div class="ydbz_tit">
					<span class="ydExpand closeOrExpand"></span>强制性节日餐
				</div>
				<div datarow-emptyrow-behavior="hide" class="hide">
					<table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new" id="tbHolidayMeal">
						<thead>
							<tr>
								<th width="150px">节日餐名称</th>
								<th width="310px"><span class="xing">*</span>日期</th>
								<th name="theadOperation" width="50px">操作</th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
					<div class="note_msg_bg_tb">
                        <label class="note_msg_labs">备注：</label>
                        <div class="note_msgs">
                            ${hotelPl.galamealMemo }
                        </div>
                    </div>
				</div>
				<div class="kong"></div>
			</div>
			<div>
				
				<div class="ydbz_tit">
					<span class="ydExpand closeOrExpand"></span>优惠信息<span class="fr padr10">
						<input value="展开全部优惠信息" class="btn btn-primary mar_10 btn_open_in_bands" type="button" />
					</span>
				</div>
				<div>
					<div id="favorTemp" style="display:none;">
						<p class="hotel_house_price_show">
							<span></span>.优惠类型：<span></span>
						</p>
						<div class="preferential_information_list">
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
						<table
							class="table activitylist_bodyer_table_new sea_rua_table favorTable">
							<thead>
								<tr>
									<th width="8%">适用房型</th>
									<th width="5%">基础餐型</th>
									<th width="4%">交通</th>
									<th width="13%">优惠事项</th>
									<th width="7%">要求</th>
									<th width="4%">优惠关联</th>
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
														<td class="tc">成人</td>
														<td class="tl">打折 50%</td>
														<td class="tl">政府税，服务税，床税</td>
													</tr>
												</tbody>
											</table>
										</div>
										<div class="mealCharge" style="display:none;">
											<p class="tl">
												<strong>餐费</strong>
											</p>
											<table
												class="table activitylist_bodyer_table_new contentTable_preventive">
												<thead>
													<tr>
														<th width="15%">游客类型</th>
														<th width="30%">餐型</th>
														<th width="30%">优惠方式</th>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td class="tc">成人</td>
														<td class="tl">BB</td>
														<td class="tl">打折 50%</td>
													</tr>
												</tbody>
											</table>
										</div>
										<div class="trafficCharge" style="display:none;">
											<p class="tl">
												<strong>交通费</strong>
											</p>
											<table
												class="table activitylist_bodyer_table_new contentTable_preventive">
												<thead>
													<tr>
														<th width="15%">游客类型</th>
														<th width="30%">交通</th>
														<th width="30%">优惠方式</th>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td class="tc">成人</td>
														<td class="tl">水飞,内飞</td>
														<td class="tl">打折 50%</td>
													</tr>
												</tbody>
											</table>
										</div>
										<div class="taxDiv" style="display:none; text-align:left;">
										</div>
										<p class="tl comment"></p></td>
									<td class="tl"></td>
									<td class="tl"></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div id="favor_nav" class="sub_main_bands_sel">
						<ul style="position:absolute; left:35px; margin:0px 35px 0px 0px;">
						</ul>
						<i class="sub_main_bands_sel_l_greys"></i> <i
							class="sub_main_bands_sel_r_greys"></i>
					</div>
					<div id="favor" class="sub_main_bands"></div>
					<div class="kong"></div>
				</div>
			</div>
			<div>
				<div class="ydbz_tit">
					<span class="ydExpand closeOrExpand"></span>上传资料
				</div>
				<div class="batch" style="margin:10px 0px 0px 30px; ">
					<ul>
						<c:forEach items="${annexList }" var="annex">
							<li><i class="sq_bz_icon"></i> <a
								onclick="downloads(${annex.docId})">${annex.docName }</a> <i
								class="download_icon" onclick="downloads(${annex.docId})"></i></li>
						</c:forEach>
					</ul>
				</div>
			</div>
			<div>
				<div class="ydbz_tit">
					<span class="ydExpand closeOrExpand"></span>其他信息
				</div>
				<div class="other_info" id="otherInfo"
					style="padding-top:0px;padding-bottom:0px;">
					<p>${hotelPl.memo }</p>
				</div>
				<div style="clear:none;" class="kong"></div>
			</div>
				<div id="thirdStepDiv">
					<div class="release_next_add">
						<!-- <input type="button" value="导出" onclick="" class="btn btn-primary" /> -->
						<input type="button" value="打印" onclick="PrintPrice();"
							class="btn btn-primary" /> <input type="button" value="关闭"
							onclick="window.close();" class="btn btn-primary" />
					</div>
				</div>
				</form>
			</div>
			<!--右侧内容部分结束-->
           	<script type="text/javascript">
            $(document).ready(function () {
                $("div.sub_main_bands_sel").each(function () {
                    tabScroll($(this));
                });

                var favorInfos = ${favorInfos};

                for (var i = 0, l = favorInfos.length; i < l; i++) {
                    writeFavorTr(favorInfos[i]);
                }

            });
            // 向列表写优惠信息
            function writeFavorTr(data) {
                var id = buildID();

                var $div = $("#favorTemp").clone().attr("id", id);
                $("#favor").append($div);
                $("#favor_nav ul").append('<li data-for="' + id + '"></li>');

                $("#favor_nav li[data-for='" + id + "']").text(data.preferentialName);
                tabScroll($("#favor_nav"), "[data-for='" + id + "']");

                var html = [];
                // 优惠名称
                $div.find("p.hotel_house_price_show span:first").text($("#favor > div").length);
                $div.find("p.hotel_house_price_show span:last").text(data.preferentialName);


                var $span = $div.find("div.preferential_information_list span");
                // 下单代码
                $span.eq(0).text(data.bookingCode);
                // 入住日期
                $span.eq(1).text(formatDate([data.inDateString, data.outDateString]));
                // 预定日期
                $span.eq(2).text(formatDate([data.bookingStartDateString, data.bookingEndDateString]));

                var $tbody = $div.find("table.favorTable > tbody");
                $tbody.children("tr:not(:first)").remove();
                // 适用房型
                var roomList = data.preferentialRoomList && $.grep(data.preferentialRoomList, function (room) {
                    return room.hotelUuid == data.hotelUuid;
                });
                roomList && $.each(roomList, function (i) {
                    if (i) {
                        $tbody.append('<tr><td class="tc"></td><td class="tc"></td></tr>');
                    }
                    var $tr = $tbody.children("tr:last");
                    html = [];
                    html.push('<p>房型*晚数：<span>', this.hotelRoomText, '*', this.nights, '晚</span></p>');
                    html.push('<p>', this.occupancyRate, '</p>')
                    $tr.find("td:first").html(html.join(''));
                    // 基础餐型
                    html = [];
                    this.hotelMealList && $.each(this.hotelMealList, function () {
                        html.push('<p>', this.mealName, '</p>');
                    });
                    $tr.find("td:eq(1)").html(html.join(''));

                    if (i == 0) {
                        // 交通
                        html = [];
                        data.islandWayList && $.each(data.islandWayList, function () {
                            html.push('<p>', this.label, '</p>');
                        });
                        $tr.find("td:eq(2)").html(html.join('')).attr("rowspan", roomList.length);
                    }
                });
                // 关联酒店
                if (data.isRelation) {
                    roomList = data.preferentialRoomList && $.grep(data.preferentialRoomList, function (room) { return room.hotelUuid == data.hotelPlPreferentialRelHotel.hotelUuid });
                    roomList && $.each(roomList, function (i) {
                        $tbody.append('<tr class="relevancy"><td class="tc"></td><td class="tc"></td></tr>');
                        var $tr = $tbody.children("tr:last");
                        html = [];
                        html.push('<p>关联岛屿:<span>', data.hotelPlPreferentialRelHotel.hotelText, '</span></p>');
                        html.push('<p>房型*晚数：<span>', this.hotelRoomText, '*', this.nights, '晚</span></p>');
                        html.push('<p>', this.occupancyRate, '</p>')
                        $tr.find("td:first").html(html.join(''));
                        // 基础餐型
                        html = [];
                        this.hotelMealList && $.each(this.hotelMealList, function () {
                            html.push('<p>', this.mealName, '</p>');
                        });
                        $tr.find("td:eq(1)").html(html.join(''));

                        if (i == 0) {
                            $tr.append('<td class="tc"></td>');
                            // 交通
                            html = [];
                            data.hotelPlPreferentialRelHotel.islandWayList && $.each(data.hotelPlPreferentialRelHotel.islandWayList, function () {
                                html.push('<p>', this.label, '</p>');
                            });
                            $tr.find("td:eq(2)").html(html.join('')).attr("rowspan", roomList.length);
                        }
                    });
                }
                var $tds = $tbody.children("tr:first").children("td");
                // 优惠事项
                $tds.eq(3).find("span.favorType").text(data.matter.preferentialTemplatesText);
                
                //拼接优惠类型描述
			    var favorTypeOutHtml;
			    
			    var liveNights;
				var freeNights;
				var earlyDays;
				var totalPrice;
				data.matter.matterValues && $.each(data.matter.matterValues, function(){
					if(this.myKey == "liveNights") {
						liveNights = this.myValue;
					} else if(this.myKey == "freeNights") {
						freeNights = this.myValue;
					} else if(this.myKey == "earlyDays") {
						earlyDays = this.myValue;
					} else if(this.myKey == "totalPrice") {
						totalPrice = this.myValue;
					}
				});
				
			    if(data.matter.type == "1") {
			    	favorTypeOutHtml = "住："+ getTextByAttribute(liveNights) +"晚，免："+ getTextByAttribute(freeNights) +"晚";
			    } else if(data.matter.type == "2") {
			    	favorTypeOutHtml = "提前预定天数："+ getTextByAttribute(earlyDays) +"天";
			    } else if(data.matter.type == "3") {
			    	favorTypeOutHtml = "合计：" + baseInfo.currencyMark + getTextByAttribute(totalPrice);
			    }
			    $tds.eq(3).find("span.favorTypeOutHtml").text(favorTypeOutHtml);
			    
                if (data.matter.type == "3") {
                    $tds.eq(3).find("div.houseCharge").hide();
                    $tds.eq(3).find("div.mealCharge").hide();
                    $tds.eq(3).find("div.trafficCharge").hide();
                    if (data.matter.preferentialTaxMap["4"]) {
                        var item = data.matter.preferentialTaxMap["4"][0];
                        var $div = $tds.eq(3).find("div.taxDiv");
                        if (item && item.istaxText) {
                            $tds.eq(3).find("div.taxDiv").text(item.istaxText).show();
                        }
                    }
                } else {
                    $tds.eq(3).find("div.taxDiv").hide();
                    // 房费
                    if(data.matter.preferentialTaxMap["1"]) {
	                    var houseCharges = $.grep(data.matter.preferentialTaxMap["1"], function (obj) {
	                        return obj.preferentialType != "0";
	                    });
	                    var $houseChange = $tds.eq(3).find("div.houseCharge")[houseCharges.length ? "show" : "hide"]().find("table > tbody");
	                    if (houseCharges.length) {
	                        $houseChange.find("tr:not(:first)").remove();
	                        $.each(houseCharges, function (i) {
	                            if (i) {
	                                $houseChange.append($houseChange.find("tr:first").clone());
	                            };
	                            var $lastTds = $houseChange.find("tr:last td");
	                            $lastTds.eq(0).text(this.travelerTypeText);
	                            if(this.chargeType == 2) {
			                        $lastTds.eq(1).html(getTextByAttribute(this.preferentialTypeText) + " " + (this.chargeTypeText||"") + (this.preferentialAmount||""));
			                    } else {
			                        $lastTds.eq(1).html(getTextByAttribute(this.preferentialTypeText) + " " + (this.preferentialAmount||"") + (this.chargeTypeText||""));
			                    }
	                            $lastTds.eq(2).text(this.istaxText);
	                        });
	                    }
                    }
                    // 餐费
                    if(data.matter.preferentialTaxMap["2"]) {
                    	var mealCharges = $.grep(data.matter.preferentialTaxMap["2"], function (obj) {
	                        return obj.preferentialType != "0";
	                    });
	                    var $mealCharge = $tds.eq(3).find("div.mealCharge")[mealCharges.length ? "show" : "hide"]().find("table > tbody");
	                    if (mealCharges.length) {
	                        $mealCharge.find("tr:not(:first)").remove();
	                        $.each(mealCharges, function (i) {
	                            if (i) {
	                                $mealCharge.append($mealCharge.find("tr:first").clone());
	                            };
	                            var $lastTds = $mealCharge.find("tr:last td");
	                            $lastTds.eq(0).text(this.travelerTypeText);
	                            $lastTds.eq(1).text(this.hotelMealText);
	                            if(this.chargeType == 2) {
			                        $lastTds.eq(2).text(getTextByAttribute(this.preferentialTypeText) + "： " + getTextByAttribute(this.chargeTypeText||"") + formatCurrency(this.preferentialAmount));
			                    } else {
			                        $lastTds.eq(2).text(getTextByAttribute(this.preferentialTypeText) + "： " + getTextByAttribute(this.preferentialAmount) + getTextByAttribute(this.chargeTypeText||""));
			                    }
	                            $lastTds.eq(3).text(this.istaxText);
	                        });
	                    }
                    }
                    
                    // 交通费
                    if(data.matter.preferentialTaxMap["3"]) {
	                    var trafficCharges = $.grep(data.matter.preferentialTaxMap["3"], function (obj) {
	                        return obj.preferentialType != "0";
	                    });
	                    var $trafficCharge = $tds.eq(3).find("div.trafficCharge")[trafficCharges.length ? "show" : "hide"]().find("table > tbody");
	                    if (trafficCharges.length) {
	                        $trafficCharge.find("tr:not(:first)").remove();
	                        $.each(trafficCharges, function (i) {
	                            if (i) {
	                                $trafficCharge.append($trafficCharge.find("tr:first").clone());
	                            };
	                            var $lastTds = $trafficCharge.find("tr:last td");
	                            $lastTds.eq(0).text(this.travelerTypeText);
	                            $lastTds.eq(1).text(this.islandWayText);
	                            if(this.chargeType == 2) {
			                        $lastTds.eq(2).text(getTextByAttribute(this.preferentialTypeText) + "： " + getTextByAttribute(this.chargeTypeText||"") + formatCurrency(this.preferentialAmount));
			                    } else {
			                        $lastTds.eq(2).text(getTextByAttribute(this.preferentialTypeText) + "： " + getTextByAttribute(this.preferentialAmount) + getTextByAttribute(this.chargeTypeText||""));
			                    }
	                            $lastTds.eq(3).text(this.istaxText);
	                        });
	                    }
                    }
                }
                $tds.eq(3).find("p.comment").text("备注 ： " + data.matter.memo);
                // 要求
                html = [];
                html.push('<p>起订晚数 ： ');
                if (+(data.require.bookingNights) > 0) {
                    html.push(data.require.bookingNights, "晚</p>");
                } else {
                    html.push("不限</p>");
                }
                html.push('<p>起订间数 ： ');
                if (+(data.require.bookingNumbers) > 0) {
                    html.push(data.require.bookingNumbers, "间</p>");
                } else {
                    html.push("不限</p>");
                }
                html.push('<p>不适用日期 ： ', data.require.notApplicableDate || '不限', "</p>");
                //html.push('<p>不适用房型 ： ', data.require.notApplicableRoomName || '不限', "</p>");
                html.push('<p>适用第三人 ： ', (data.require.applicableThirdPerson ? '否' : '是'), '</p>');
                html.push(data.require.isSuperposition ? '<p class="tdred">优惠叠加 ：不允许 </p>' : '<p class="tdgreen">优惠叠加 ： 允许</p>');
                html.push('<p>备注 ： ', data.require.memo, '</p>');
                $tds.eq(4).html(html.join(''));
                // 优惠关联
                html = [];
                data.hotelPlPreferentialRels && $.each(data.hotelPlPreferentialRels, function (i) {
                    html.push('<p>', (i + 1), '、<span>', this.relHotelPlPreferentialName, '</span></p>');
                });
                $tbody.children("tr:first").children("td:eq(5)").html(html.join(''));
                //
                $tbody.children("tr:first").children("td:gt(2)").attr("rowspan", $tbody.children("tr").length);
            }
            // 格式化日期范围
            function formatDate(dates) {
                /* var newDates = [];
                $.each(dates, function () {
                    newDates.push(this.replace(/-/g, "."));
                }); */
                return dates.join("-");
            }
        </script>
    </div>
</body>
</html>