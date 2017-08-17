<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>报价器</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
	<style type="text/css">
		.activitylist_bodyer_right_team_co1{
			min-width:270px;
		}
        .activitylist_team_co3_text_80.title-md{
            width: 100px;
        }
        .preferential-td{
            font-size: 16px;;
        }
    </style>
    <style type="text/css">
            .activitylist_bodyer_right_team_co1{
                min-width:270px;
            }
            .activitylist_team_co3_text_80.title-md {
                width: 100px;
            }

            .preferential-td {
                font-size: 16px;;
            }
            .preferential-empty{
                margin: 100px auto;
                line-height: 40px;
                font-size: 24px;
                text-align: center;
            }
    </style>
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactor3.js"></script>
	<script src="${ctxStatic}/jquery.zclip/jquery.zclip.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
	<script type="text/javascript">
		$(function() {
			
			
			//操作浮框
			operateHandler();
			
			//初始化所属国家下拉列表框
			changePosition();
			
			//加载去程交通下拉框改变事件
			$("#goTraffic").on("change", function(){
				$("#backTraffic").val($(this).val());
			});
		});
		
		
		
		//展开、关闭
        function expand(child, obj, srcActivityId) {
            if ($(child).css("display") == "none") {
                $(obj).html("收起明细");
                $(child).show();
               // $(obj).addClass('team_a_click2');
                $(obj).parents("td").addClass("td-extend").parent("tr");
                $(child).find("[name='copyQuotedPriceDtl']").zclip({
                    copy:getQuotedPriceDtlCopyText,
                    path:"${ctxStatic}/jquery.zclip/ZeroClipboard.swf"
                });
            } else {
                $(child).hide();
                //$(obj).removeClass('team_a_click2');
                $(obj).parents("td").removeClass("td-extend").parent("tr").removeClass("tr-hover");
                $(obj).html("展开明细");

            }
        }

        function sortTable(){

        }
		//境内境外 改变地址展现形式
		function changePosition(){
			if($("input[name=position]:checked").val()==2){//境外
				$("#country").attr("defaultValue","80415d01488c4d789494a67b638f8a37");//默认马尔代夫
				$("#country").attr("value","80415d01488c4d789494a67b638f8a37");
				initSuggest({"type":$("input[name=position]:checked").val()});
				getAjaxSelect('island',$('#country'));
			}else{//境内
				$("#country").attr("defaultValue","c89e0a6661b64d1e809d8873cf85bc80");//默认中国
				$("#country").attr("value","c89e0a6661b64d1e809d8873cf85bc80");
			    initSuggest({"type":$("input[name=position]:checked").val()});
			    getAjaxSelect('island',$('#country'));
			}
			$("#island").empty();  			
			$("#hotel").empty();
		}
		//级联查询
		function getAjaxSelect(type,obj){
			
			$.ajax({
				type: "POST",
			   	url: "${ctx}/hotelControl/ajaxCheck",
			   	async: true,
			   	data: {
						"type":type,
						"uuid":$(obj).val()
					  },
				dataType: "json",
			   	success: function(data){
			   		if(type == "island"){
			   			$("#island").empty();
			   			$("#island").append("<option value=''>请选择</option>");
			   			
			   			$("#hotel").empty();
				   		$("#hotel").append("<option value=''>请选择</option>");
				   		
				   		travelerTypes=data.travelerTypes;
				   		travelerTypesTemp = travelerTypes;
				   		writeTravelerType();
			   		} else if(type == "hotel"){
			   			$("#hotel").empty();
				   		$("#hotel").append("<option value=''>请选择</option>");
				   		
				   		travelerTypes=data.travelerTypes;
				   		travelerTypesTemp = travelerTypes;
				   		writeTravelerType();
			   		}else{
				   		$("#"+type).empty();
				   		$("#"+type).append("<option value=''></option>");
			   		}
			   		if(data){
			   		
			   			if(type=="hotel"){ 
			   				if(data.hotelList!=undefined){
				   				$.each(data.hotelList,function(i,n){
					   				if(i==0){
					   					$("#hotel").append($("<option/>").text(n.nameCn).attr("value",n.uuid).attr("selected",true));
					   					$("#hotel").change();
					   				}else{
					   					$("#hotel").append($("<option/>").text(n.nameCn).attr("value",n.uuid));
					   				}
					   			    
					   			});
			   				}
				   			$("#hotel").attr("title",$("#hotel").find("option:selected").text());
				   			$("#island").attr("title",$("#island").find("option:selected").text());
				   			//基础信息海岛游部分赋值
				   			baseInfo.islandText = $(obj).find("option:selected").text();
			   			}else if(type=="island"){
			   				$.each(data.islandList,function(i,n){
			   					 $("#island").append($("<option/>").text(n.islandName).attr("value",n.uuid));
			   				});
			   				
			   			}else if(type=="hotelRoom"){
			   				$("#hotel").attr("title",$("#hotel").find("option:selected").text());
				   			houseTypes={};
				   			if(data){
				   				houseTypes=data.houseTypes;
				   				travelerTypes=data.travelerTypes;
				   				travelerTypesTemp = travelerTypes;
				   				writeTravelerType();
				   			}
				   			var $sel = $("#stayTable tbody tr select.houseType");
			                $sel.empty();
			                for (var k in houseTypes) {
			                    var $option = $("<option>").attr({ "value": k }).text(houseTypes[k].name);
			                    $sel.append($option);
			                }
			               	$sel.change();
			   			}
			   		}
			   	}
			});
		}
		
		//快速生成订单 
		function rapidCreateOrder(obj,type){
		
			//var $this = $(obj).parents('.quotation_main_list_hotel_echo:first');
 			var $tr = $(obj).parents('tr:first');
			var preferential= $tr.data("preferential");
			var quotedPriceJsonStr;
			if(type==1){//优惠后     的快速生成订单链接
				
				$("#rapidCreateOrderForm").find("input[name=guestPriceJson]").val(JSON.stringify(preferential.guestPriceList));
            	$("#rapidCreateOrderForm").find("input[name=hotelPlUuid]").val($tr.parent().find("#preTotal").find("input[name=hotelPlUuid]").val());
            	quotedPriceJsonStr = $tr.prev().prev().find("input[name=quotedPriceJsonStr]").val();
			}else{//优惠前    的快速生成订单链接
				$("#rapidCreateOrderForm").find("input[name=guestPriceJson]").val($tr.find("input[name=guestTypePriceJsonStr]").val());
	            $("#rapidCreateOrderForm").find("input[name=hotelPlUuid]").val($tr.find("input[name=hotelPlUuid]").val());
	            quotedPriceJsonStr = $tr.find("input[name=quotedPriceJsonStr]").val();
			}
			
			if(quotedPriceJsonStr){
				var quotedPriceJsonObj = JSON.parse(quotedPriceJsonStr);
				var quotedPriceQueryJsonStr = JSON.stringify(quotedPriceJsonObj.quotedPriceQuery);
				$("#rapidCreateOrderForm").find("input[name=quotedPriceQueryJsonStr]").val(quotedPriceQueryJsonStr);
			}
			
            $("#rapidCreateOrderForm").submit();
		}
			
	</script>
</head>
<body>
	<form id="rapidCreateOrderForm" action="${ctx}/hotelPlSpeedGenOrder/page" method="post" target="_blank">
		<input type=hidden name="guestPriceJson" value=''/>
		<input type=hidden name="hotelPlUuid" value=''/>
		<input type=hidden name="quotedPriceQueryJsonStr" value=''/>
	</form>
	<!--右侧内容开始-->
      <div class="mod_nav">询价 > 酒店 > 报价 > 报价器</div>

                    <!--<iframe id="test" name="left" frameborder="0" src="询价-酒店-报价-报价器01.html" width="100%" height="100"></iframe>-->
                    <!--报价器-->
                    <div class="ydbzbox fs">
                        <div class="ydbz_tit island_productor_upload001">报价器</div>
                        <div class="quotation_main_list">
                            <div id="signChannelList">

                                <div class="activitylist_bodyer_right_team_co background_color_none" id="baseInfo">
                                    <div class="activitylist_bodyer_right_team_co1">
                                        <label class="activitylist_team_co3_text_80 title-md">位置属性：</label>
                                        <span style="height:28px; line-height:28px;">
	                                        <input style="margin: 0px ! important;" id="overseas" type="radio" name="position" class="w_radio" checked="checked"  value="2" onclick="changePosition();" /> 境外
	                                        <span class="padr10"></span>
	                                        <input style="margin: 0px ! important;" type="radio" name="position" class="w_radio"  onclick="changePosition();" value="1"/> 境内
                                        </span>
                                    </div>
                                    <div style="float: left; margin-left: 2%; width: 18%;min-width:270px;">
                                        <label class="activitylist_team_co3_text_80 title-md"><span class="xing">*</span>国家：</label>
                                    <trekiz:suggest id="country" name="country" style="width:150px;" defaultValue="" callback="getAjaxSelect('island',$('#country'))"  displayValue="${countryName}" ajaxUrl="${ctx}/geography/getGeoListAjax" />    
                                    </div>
                                    
                                    <div class="activitylist_bodyer_right_team_co1">
                                        <label class="activitylist_team_co3_text_80 title-md"><span class="xing">*</span>岛屿：</label>
                                        <select id="island"  onchange="getAjaxSelect('hotel',this);" style="width:150px;"></select>
                                    </div>
                                    <div class="kong"></div>

                                    <div class="activitylist_bodyer_right_team_co1">
                                        <label class="activitylist_team_co3_text_80 title-md"><span class="xing">*</span>酒店名称：</label>
                                        <select id="hotel" onchange="getAjaxSelect('hotelRoom',this);" style="width:150px;"></select>
                                    </div>
                                    <div class="activitylist_bodyer_right_team_co1">
                                        <label class="activitylist_team_co3_text_80 title-md">地接供应商：</label>
                                        <select id="ground" style="width:150px;">
                                            <option value="" selected="selected">请选择</option>
                                            <c:forEach items="${supplierInfos }" var="supplierInfo">
												<option value="${supplierInfo.id }">${supplierInfo.supplierName}</option>
											</c:forEach>
                                        </select>
                                        
                                    </div>
                                    <div class="activitylist_bodyer_right_team_co1">
                                        <label class="activitylist_team_co3_text_80 title-md">采购类型：</label>
                                        <select id="purchase" style="width:150px;">
                                            <option value="" selected="selected">请选择</option>
                                            <option value="0">内采</option>
                                            <option value="1">外采</option>
                                        </select>
                                    </div>
                                    <div class="kong"></div>


                                    <div class="activitylist_bodyer_right_team_co1">
                                        <label class="activitylist_team_co3_text_80 title-md"><span class="xing">*</span>去程交通：</label>
                                        
                                        <trekiz:defineDict name="goTraffic" type="islands_way"  style="width:150px;"/>
                                    </div>
                                    <div class="activitylist_bodyer_right_team_co1">
                                        <label class="activitylist_team_co3_text_80 title-md"><span class="xing">*</span>返程交通：</label>
                                        
                                        <trekiz:defineDict name="backTraffic" type="islands_way"  style="width:150px;"/>
                                    </div>
                                    <div class="activitylist_bodyer_right_team_co1">
                                        <label class="activitylist_team_co3_text_80 title-md"><span class="xing">*</span>间数：</label>
                                        <input id="roomNum" type="text" value="1" data-type="number" maxlength="4" />
                                    </div>
                                    
   									<div class="kong"></div>
   									
                                    <div  id="personNumStart"></div>

                                    <div class="kong"></div>

                                    <div class="activitylist_bodyer_right_team_co1">
                                        <label class="activitylist_team_co3_text_80 title-md">混住次数：</label>
                                        <input id="mixedNum" type="text"  data-type="number" maxlength="4"/>
                                    </div>
                                    <div class="kong"></div>
                                </div>
                            </div>

                            <div class="filterbox ">
                            <div class="filter_btn hotel_add_newdate_border_none">
                                <textarea name="dateList" id="dateList"
                                          style="width:500px;height:200px;display: none;"></textarea>
                                <a id="selDates" class="btn btn-primary" style="background-color: #0088cc">
                                请选择入住日期<i class="hotel_add_newdate_pop02_dateinput_new"></i> </a></div>
                        </div>
                            <table id="stayTable" class="table activitylist_bodyer_table_new contentTable_preventive small_table98_30"  style="display: none">
                                <thead>
                                <tr>
                                    <th width="15%"><span class="xing">*</span>入住日期</th>
                                    <th width="15%"><span class="xing">*</span>房型</th>
                                    <th width="25%">容住率</th>
                                    <th width="15%"><span class="xing">*</span>基础餐型</th>
                                    <th width="20%">升级餐型</th>
                                    <th width="5%">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr style="display:none;">
                                    <td class="tc"><span class="date"></span></td>
                                    <td class="tc">
                                        <select class="w120 houseType"></select>
                                    </td>
                                    <td class="tc"><span class="capacity"></span></td>
                                    <td class="tc">
                                        <select class="w120 baseMealType"></select>
                                    </td>
                                    <td class="tc">
                                        <select class="w120 upMealType"></select>
                                    </td>
                                    <td class="tc"><a class="delLink">删除</a></td>
                                </tr>
                                </tbody>
                            </table>
                            <!--报价 开始-->
                            <div class="cos_add_btn_bg quotation_main_list_update" style="height: 70px;">
							    <span class="note_msg_red red_normal">注： 1.此处成人数默认为2，间数默认为1
							    </span>
							    <span class="note_msg_red red_normal" style="top:30px;left:57px;">
							         2.当成人数=1，儿童数>0时，默认第一个儿童/人价格=成人/人价格，
							    </span>
							    <span class="note_msg_red red_normal" style="top:50px;left:67px;">
							            从第二个儿童开始享受儿童价；
							    </span>
							    <div class="release_next_add btn_mar_a">
							        <input id="quote" type="button" value="报价" class="btn btn-primary jbox-width100" />
							    </div>
							</div>
                        </div>
						
                    </div>

                    <!--报价结果开始-->
                    <div class="fs" id="priceResult">

                    </div>
                    <!--报价结果结束-->

                    <div class="hotel_price_ask_out01">
                        <!--报价 结束-->
                        <div class="release_next_add">
                            <input id="clearQuote" type="button" value="清空结果" class="btn btn-primary" /> &nbsp;
                            <input id="saveQuote" type="button" value="保存" class="btn btn-primary" />
                        </div>
                    </div>

					<!--保存弹窗 开始-->
                    <div class="jbox_save_ctermsg_bg">
						
                        <div class="jbox_save_ctermsg">
                            <div class="activitylist_bodyer_right_team_co1">
                                <label style="width: 90px; text-align: right">销售姓名：</label>
                                	${userDetail.name }
                            </div>
                            <div class="activitylist_bodyer_right_team_co1">
                                <label style="width: 90px; text-align: right">销售电话：</label>
                                ${userDetail.mobile }
                            </div>
                            <div class="activitylist_bodyer_right_team_co1">
                                <label style="width: 90px; text-align: right">销售邮箱：</label>
                                ${userDetail.email }
                            </div>
                            <div class="kong"></div>
                            <div class="activitylist_bodyer_right_team_co1 w100b" name="saleEmail">
                                <label style="width: 90px; text-align: right">询价客户类型：</label>
                                <span class="house_list_all">
                                    <span class="padr10">
                                        <input name="quoteType" type="radio" checked="checked" value="1" class="w_radio" data-relation=".collapse_01" />直客
                                    </span>
                                    <span class="padr10">
                                        <input name="quoteType" type="radio" value="2" class="w_radio" data-relation=".collapse_02" />同行
                                    </span>
                                    <span class="padr10">
                                        <input name="quoteType" type="radio" value="3" class="w_radio" data-relation=".collapse_03" />其他
                                    </span>
                                </span>
                            </div>
                            <div class="kong"></div>
                            <!--询价客户类型-直客开始-->
                            <div class="collapse_01">
                                <div class="activitylist_bodyer_right_team_co1">
                                    <label style="width: 90px; text-align: right">询价客户：</label>
                                    <input type="hidden" name="quoteObject" />
                                    <input type="text" class="inputTxt" name="txtCustomer" />
                                    <select name="slCompetition" class="hide">

                                    </select>
                                </div>
                                <div class="activitylist_bodyer_right_team_co1">
                                    <label style="width: 90px; text-align: right">联系人名：</label>
                                    <input type="text" class="inputTxt"  name="linkName"/>
                                </div>
                                <div class="activitylist_bodyer_right_team_co1">
                                    <label style="width: 90px; text-align: right">联系电话：</label>
                                    <input type="text" class="inputTxt"  name="linkPhone" />
                                </div>
                                <div class="kong"></div>
                                <div class="activitylist_bodyer_right_team_co2 long_text_input01 w100b">
                                    <label style="width: 90px; text-align: right">备注：</label>
                                    <textarea class="w80b" name="memo" style="height: 140px;"></textarea>
                                </div>
                            </div>

                        </div>
                        <input type=hidden name="guestPriceJson" value=''/>
						<input type=hidden name="hotelPlUuid" value=''/>
					
                    </div>
                    <!--保存弹窗 结束-->

                    
                <!--右侧内容结束-->
	</div>
	 <!--数据-->
    <script type="text/javascript">
        var countrys = {};
        var houseTypes = {};
        var travelerTypesTemp ={};
        var travelerTypes ={};
        var travelerTypes_quoted={};
        var preferentialTypeDic ={
            "1":"房费",
            "2":"餐费",
            "3":"交通",
            "4":"打包价"
        };
        var competitions=JSON.parse('${competitions}');
    </script>
    <!--报价器-->
    <script type="text/javascript">
        function writeTravelerType(){
        	
        	$("#personNumStart").empty();
        		
        	var travelerNumTheme="";
            for(var i in travelerTypes){
                travelerNumTheme+='<div class="activitylist_bodyer_right_team_co1">';
                travelerNumTheme+='<label class="activitylist_team_co3_text_80 title-md">';
                travelerNumTheme+='<span name="travelerTypeText"><input type=hidden value="'+travelerTypes[i].personType+'">'+travelerTypes[i].travelerTypeText+'</span>'
                travelerNumTheme+='数：</label>';
                if(travelerTypes[i].personType=="0"){
                	travelerNumTheme+='<input class="personNum" data-type="number"  type="text" value="2"  maxlength="4"/>';
                }else{
                	travelerNumTheme+='<input class="personNum" data-type="number"  type="text" value=""  maxlength="4"/>';
                }
                travelerNumTheme+='</div>';
            }
            
            $("#personNumStart").append(travelerNumTheme);
            if(!travelerTypes || travelerTypes.length==0){
				$("#personNumStart").hide();
	        }else if(travelerTypes.length>0){
				$("#personNumStart").show();
	        }
        }
        $(document).ready(function (e) {
			//动态输出游客类型
            //writeTravelerType();

            // 多日期选择
            
            $("#selDates").datepickerRefactor({
	            dateFormat: "yy-mm-dd",
	            target: "#dateList",
	            dayNamesMin: ["日", "一", "二", "三", "四", "五", "六"],
	            closeText: "关闭",
	            prevText: "前一月",
	            nextText: "后一月",
	            monthNames: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
	            numberOfMonths: 3,
	            onSelect: function (inst) {
	                var $dataList= $('#dateList');
	                var dateStr=inst;
	
	                if ($dataList.val().indexOf(dateStr)>-1) {
	                    // 删除日期
	                    $("#stayTable tr[data-date='" + dateStr + "']").remove();
	                    $dataList.val().replace(dateStr+',');
	                } else {
	                    $dataList.val(  $dataList.val()+dateStr+',');
	                    var $tbody = $("#stayTable tbody");
	                    var $tr = $tbody.find("tr:first").clone().attr("data-date", dateStr).show();
	                    $tr.find("span.date").text(dateStr)
	                    var $trs = $tbody.find("tr");
	                    var isMax = true;
	                    for (var i in $trs) {
	                        if ($tr.find("td:first").text() < $trs.eq(i).find("td:first").text()) {
	                            isMax = false;
	                            $trs.eq(i).before($tr);
	                            break;
	                        }
	                    }
	                    if (isMax) {
	                        $tbody.append($tr);
	                    }
	                    $("#stayTable").show();
	                    $tr.find("select.houseType").change();
	                }
	                return inst;
	            }
	        });

            $("#stayTable").on('click', "a.delLink", function () {
                // 删除行
                var $tr = $("#stayTable tr").has(this);
                var dateStr = $tr.attr("data-date");
                $tr.remove();
                $("#dateList").val($("#dateList").val().replace(dateStr+',', ''));
                $targetVal = $targetVal.replace(dateStr, '');
            }).on('change', 'select.houseType', function () {
             	// 房型改变
                var $tr = $("#stayTable tr").has(this);
                var value = $(this).val();
            	//var $sel = $tr.find("select.baseMealType").empty();
            	var $baseMealTd= $tr.find("select.baseMealType").parent().empty();
            	$baseMealTd.append('<select class="baseMealType"></select>');
            	$sel = $baseMealTd.find("select.baseMealType");
            	$tr.find("span.capacity").text('');
            	if(houseTypes){
	            	
	                var houseType = houseTypes[value];
	                 // 基础餐型
	                for (var k in houseType.baseMealTypes) {
	                   var $option = $("<option>").attr({ "value": k }).text(houseType.baseMealTypes[k].name).data("upMealtypes", houseType.baseMealTypes[k].upMealTypes);
	                    $sel.append($option);
	                }
	                
	                // 容住率
	                if(houseType.capacity){
	                	$tr.find("span.capacity").text(houseType.capacity);
	                	$tr.find("span.capacity").attr('title', houseType.capacityNote);
	                }
	               
            	}
               	$(this).attr("title",$(this).find("option:selected").text());
                $sel.change();
            }).on('change', 'select.baseMealType', function () {
            	var $tr = $("#stayTable tr").has(this);
	            var $sel = $tr.find("select.upMealType").empty();
	            if(houseTypes){
	            	// 基础餐型改变
	                
	                var upMealtypes = $(this).find("option:selected").data("upMealtypes");
	                
	                $sel.append("<option value=''></option>");
	                for (var k in upMealtypes) {
	                    var $option = $("<option>").attr({ "value": k }).text(upMealtypes[k].name);
	                    $sel.append($option);
	                }
	            }
            });

            $("#quote").on("click", function () {
                var data = {};
                data.country = $("#country").val() ;
                data.islandUuid = $("#island").val() ;
                data.hotelUuid =$("#hotel").val();
                data.arrivalIslandWay =$("#backTraffic").val();
                data.departureIslandWay = $("#goTraffic").val();
                data.mixliveNum = (+$("#mixedNum").val()) || 0;
                data.roomNum = (+$("#roomNum").val()) || 0;
                data.purchaseType = ($("#purchase").val()) ;
                data.supplierInfoId = ($("#ground").val());
                data.position = $("input[name=position]:checked").val();
                
                if($("#country").val()==""){
                	infoBox("报价操作国家不能为空！");
                	return;
                }
                if($("#island").val()==""){
                	infoBox("报价操作岛屿不能为空！");
                	return;
                }
                if($("#hotel").val()==""){
                	infoBox("报价操作酒店不能为空！");
                	return;
                }
                if($("#backTraffic").val()==""){
                	infoBox("报价操作返程交通不能为空！");
                	return;
                }
                if($("#goTraffic").val()==""){
                	infoBox("报价操作去程交通不能为空！");
                	return;
                }
                
                if(isNaN(data.roomNum) || data.roomNum< 1){
                    infoBox("间数不能少于1间");
                    return;
                }

				var perNumCount=0;
 				data.personNum = [];
                var $personNums= $("input.personNum").each(function () {
                    var $person = $(this);
                    data.personNum.push($person.val());
                    perNumCount+=$person.val();
                });
				if(perNumCount<1){
					infoBox("总人数不能少于1人");
					return;
				}

                data.quotedPriceRoomList = [];
                $("#stayTable tbody tr:visible").each(function () {
                
                    var $this = $(this);
                    data.quotedPriceRoomList.push({
                        hotelMealRiseUuid:$this.find("select.upMealType").val(),
                        hotelMealRiseText:$this.find("select.upMealType option:selected").text(),
                        hotelMealUuid : $this.find("select.baseMealType").val(),
                        hotelMealText : $this.find("select.baseMealType option:selected").text(),
                        hotelRoomUuid : $this.find("select.houseType").val(),
                        hotelRoomText : $this.find("select.houseType option:selected").text(),
                        hotelCapacity : $.trim($this.find(".capacity").text()),
                        inDate: $this.find("span.date").text()
                    });
                });
                if(data.quotedPriceRoomList.length ==0){
                    infoBox("入住日期不能为空");
                    return;
                }
                
                var b = false;
                $(".houseType").not($(".houseType")[0]).each(function(){
                	if($(this).val()==""){
                		b=true;
                		return false;
                	}
                });
                if(b){
                	infoBox("房型不能为空");
                	return;
                }
                
                b=false;
                $(".baseMealType").not($(".baseMealType")[0]).each(function(){
                	if($(this).val()==""||$(this).val()==null){
                		b=true;
                		return false;
                	}
                });
                if(b){
                	infoBox("基础餐型不能为空");
                	return;
                }
                
				
				//异步加载loading
                ajaxStart();
				$.post("${ctx}/autoQuotedPrice/checkRoomCapacity",{"queryJsonString":JSON.stringify(data)},function(result){
					result = JSON.parse(result);
					if(result){
						if(result.result=="success"){
							$.post("${ctx}/autoQuotedPrice/quotedPrice",{"queryJsonString":JSON.stringify(data)},function(result){
								if(result){
									try{
										travelerTypes_quoted = JSON.parse(result).travelerTypesQuotedList;
										buildPriceSheet(JSON.parse(result));
										ajaxStop();
										if(JSON.parse(result).message){
											alert(JSON.parse(result).message);
										}
									}catch(e){
										ajaxStop();
										if(JSON.parse(result).message){
											alert(JSON.parse(result).message);
										}else{
											alert("自动报价异常，请联系管理员！");
										}
									}
									
								}
							});
						}else{
							if(confirm("游客类型的人数超出房型容住率，您确定继续吗？")){
								$.post("${ctx}/autoQuotedPrice/quotedPrice",{"queryJsonString":JSON.stringify(data)},function(result){
									if(result){
										try{
											buildPriceSheet(JSON.parse(result));
											ajaxStop();
											if(JSON.parse(result).message){
												alert(JSON.parse(result).message);
											}
										}catch(e){
											ajaxStop();
											if(JSON.parse(result).message){
												alert(JSON.parse(result).message);
											}else{
												alert("自动报价异常，请联系管理员！");
											}
										}
										
									}
								});
							}else{
								ajaxStop();
							}
						}
					}else{
						ajaxStop();
					}
				});
				
				//异步加载loading
               /* ajaxStart();
				$.post("${ctx}/autoQuotedPrice/quotedPrice",{"queryJsonString":JSON.stringify(data)},function(result){
					if(result){
						try{
							buildPriceSheet(JSON.parse(result));
							ajaxStop();
							if(JSON.parse(result).message){
								alert(JSON.parse(result).message);
							}
						}catch(e){
							ajaxStop();
							if(JSON.parse(result).message){
								alert(JSON.parse(result).message);
							}else{
								alert("自动报价异常，请联系管理员！");
							}
						}
						
					}
				});*/
            });
            // 位置切换
            $("input:radio[name='position']").on('change', function () {
                var checked = $("#overseas").prop('checked');
                var $country = $("#country");
                if (checked) {
                    $country.removeAttr("disabled").prop("selectedIndex", 1);
                } else {
                    $country.attr("disabled", true).prop("selectedIndex", 0);
                }
                $("#country").change();
            });
            $("input:radio[name='position']").trigger("change");
        });
    </script>
    <!--报价详情-->
    <script type="text/javascript">
        //保存弹出窗选择的销售邮箱为同行
        var  saleMail_competitionValue='2';
        var rowID =0;
        $(document).ready(function(){
           var $slCompetition= $("[name='slCompetition']");
            $slCompetition.append('<option value="" selected">请选择</option>');
            for(var i in competitions){
                $slCompetition.append('<option value="'+competitions[i].competitionID+'">'+competitions[i].competitionText+'</option>');
            }
            $(document).on("click", "[name='usePreferential']", function () {
                var $tr = $(this).parents("tr:first");
                usePreferential($tr);
            });

            //删除价单
            $(document).on("click","[name='deleteQuotedPrice']",function(){
                var $quotationList = $(this).parents(".quotation_main_list_hotel_echo:first")
                confirmBox('确定要删除该条报价结果吗？',function(){
                    $quotationList.remove();
                    $("#priceResult").trigger("childCount.change");
                })
            });
            $("#saveQuote").hide();
            $("#saveQuote").click(function(){
                var  $pop = $.jBox($(".jbox_save_ctermsg_bg").html(),{
                    title:"填写询客信息：",
                    buttons:{ '确定':'1',"取消":"2" },
                    width:800,
                    height:400,
                    persistent: true,
                    submit:function(v, h, f){
                    	//$("#clearQuote").hide();
						//$("#saveQuote").hide();
						if(v==1){
							var quoteJsonBean = {};
	                    	quoteJsonBean.linkName = f.linkName;
	                    	quoteJsonBean.linkPhone = f.linkPhone;
	                    	quoteJsonBean.quoteType = f.quoteType;
	                    	quoteJsonBean.memo = f.memo;
	                    	if(f.quoteType==2){
	                    		quoteJsonBean.quoteObject = f.slCompetition;
	                    	}else{
	                    		quoteJsonBean.quoteObject = f.txtCustomer;
	                    	}
	                    	
	                    	quoteJsonBean.preferentialPriceJson=[];
	                    	$("input[name=preferentialPriceJson]").each(function(i){
	                    		if($(this).val()){
	                    			quoteJsonBean.preferentialPriceJson[i]=JSON.parse($(this).val());
	                    		}else{
	                    			quoteJsonBean.preferentialPriceJson[i]={};
	                    		}
	                    	});
	                    	
	                    	quoteJsonBean.quotedPriceJsonStr=[];
	                    	$("input[name=quotedPriceJsonStr]").each(function(i){
	                    		if($(this).val()){
	                    			quoteJsonBean.quotedPriceJsonStr[i]=JSON.parse($(this).val());
	                    		}else{
	                    			quoteJsonBean.quotedPriceJsonStr[i]={};
	                    		}
	                    	});
	                    	
	                    	//异步加载loading
                			ajaxStart();
	                    	$.ajax({
		                		type	:"post",
		                		url		:"${ctx}/hotelQuote/saveQuotedPriceInfo",
		                		dataType:"json",
		                		data 	:{"hotelQuoteJsonStr":JSON.stringify(quoteJsonBean)},
		                		success	:function(res){
		                			try{
			                			if(res.result=='1'){
			                				$.jBox.tip("保存成功！");
											window.location.href="${ctx}/hotelQuote/hotelQuoteList";
			                			}else{
			                				$.jBox.tip("保存失败！");
			                				ajaxStop();
			                				$("#clearQuote").show();
											$("#saveQuote").show();
			                			}
			                			
		                			}catch(e){
		                				$.jBox.tip("保存异常！");
		                				ajaxStop();
		                			}
		                			
		                		}
		                	});
						}
                    	
	                }
                });
                $pop.on("change","[name='saleEmail'] input",function(){
                    var $slCompetition=  $pop.find("[name='slCompetition']");
                   	$pop.find("[name='txtCustomer']").val("");
                    if($pop.find("[name='saleEmail'] :checked").val()==saleMail_competitionValue){
                        $slCompetition.show();
                        $slCompetition.siblings("input").hide();
                        $slCompetition.val("");
                        $pop.find("[name='linkName']").val("");
                        $pop.find("[name='linkPhone']").val("");
                    }
                    else{
                        $slCompetition.hide();
                        $slCompetition.siblings("input").show();
                        $pop.find("[name='linkName']").val("");
                        $pop.find("[name='linkPhone']").val("");
                    }
                });
                $pop.on("change","[name='slCompetition']",function(){
                    var $this = $(this);
                    $this.attr("title",$this.find("option:selected").text());
                    if(!$this.val()){
                        $pop.find("[name='linkName']").val("");
                        $pop.find("[name='linkPhone']").val("");
                    }
                    else{
                        var competition ;
                        var competitionID = $this.val();
                        for(var i in competitions){
                            if(competitionID ==competitions[i].competitionID){
                                competition=competitions[i];
                            }
                        }
                        if(competition){
                            $pop.find("[name='linkName']").val(competition.competitionName);
                            $pop.find("[name='linkPhone']").val(competition.competitionCellNo);
                        }else{
                            $pop.find("[name='linkName']").val("");
                            $pop.find("[name='linkPhone']").val("");
                        }
                    }
                });
            });
            $("#clearQuote").hide();
            $("#clearQuote").click(function(){
                confirmBox('确定要清空所有报价结果吗？',function(){
                    $("#priceResult").empty();
                    $("#priceResult").trigger("childCount.change");
                })
            });

            $("#priceResult").on("childCount.change",function(){
                if($(this).children().length==0){
                    $("#saveQuote").hide();
                    $("#clearQuote").hide();
                }
                else{
                    $("#saveQuote").show();
                    $("#clearQuote").show();
                }
            })
        });
        function buildPriceSheet(result) {
        	travelerTypes=new Array();
        	for (var i = 0; i < travelerTypesTemp.length; i++) {
	            travelerTypes.push(travelerTypesTemp[i]);
	        }
	        for (var i = 0; i < travelerTypes_quoted.length; i++) {
	            travelerTypes.push(travelerTypes_quoted[i]);
	        }
            if(result.status=="fail"){
                return;
            }
            for(var quotedPriceIndex in result.quotedPriceJsonList) {

                var quotedPriceJson = result.quotedPriceJsonList[quotedPriceIndex];
                var $priceResult = $("#priceResult");
                var sheetTheme = "";
                sheetTheme += '<div class="quotation_main_list_hotel_echo">';
                sheetTheme += '<div class="quotation_main_list_hotel_name">' + quotedPriceJson.quotedPriceQuery.hotelText + '<span class="del fr mr25" name="deleteQuotedPrice"></span></div>';
                sheetTheme += '<table class="quotation_main_list_hotel_stock">';
                sheetTheme += '<tbody><tr>';
                sheetTheme += '<td>';
                
                var hotelGroupText = quotedPriceJson.quotedPriceQuery.hotelGroupText===undefined?"":quotedPriceJson.quotedPriceQuery.hotelGroupText +',';
                
                sheetTheme += hotelGroupText+ quotedPriceJson.quotedPriceQuery.islandText;
                sheetTheme += '</td>';
                sheetTheme += '<td></td>';
                sheetTheme += '<td></td>';
                sheetTheme += '<td class="tr">';
                sheetTheme += '<span class=" mr25">' + quotedPriceJson.quotedPriceQuery.supplierInfoText + '</span>';
                sheetTheme += '<span class=" mr25">' + quotedPriceJson.quotedPriceQuery.purchaseTypeText + '</span>';
                sheetTheme += '<span class=" mr25"><a target="_blank" href="${ctx}/hotelPl/show/'+quotedPriceJson.hotelPlUuid+'">查看酒店价单</a></span>';
                sheetTheme += '</td>';
                sheetTheme += '</tr>';
                sheetTheme += '</tbody></table>';
                sheetTheme += '<div class="check_in_date_room_style">';
                sheetTheme += '<ul>';

                var checkinDate = quotedPriceJson.detailList[0];
                for (var i in quotedPriceJson.detailList) {
                    var inDate = quotedPriceJson.detailList[i].inDate;
                    if (inDate < checkinDate) {
                        checkinDate = inDate;
                    }
                }

                sheetTheme += '<li>入住起始日期：' + checkinDate + '</li>';
                sheetTheme += '<li style="width:300px; overflow:hidden; white-space: nowrap;text-overflow: ellipsis;"><span>房型*晚数：</span>';
                var roomDays = [];
                for (var i in quotedPriceJson.detailList) {
                    var quotedPriceRoom = quotedPriceJson.detailList[i];
                    var roomDaysLength = roomDays.length;
                    if (roomDaysLength > 0 && roomDays[roomDaysLength - 1].hotelRoomUuid == quotedPriceRoom.hotelRoomUuid) {
                        roomDays[roomDaysLength - 1].num += 1;
                    } else {
                        roomDays.push({
                            hotelRoomUuid: quotedPriceRoom.hotelRoomUuid,
                            hotelRoomName: quotedPriceRoom.hotelRoomName,
                            num: 1
                        });
                    }
                }
                var roomDayText = "";
                for (var i in roomDays) {
                    roomDayText += roomDays[i].hotelRoomName + "*" + roomDays[i].num + "晚,";
                }
                roomDayText = roomDayText.substr(0, roomDayText.length - 1);
                sheetTheme += '<em title="' + roomDayText + '">' + roomDayText + '</em>';
                sheetTheme += '</li>';
                sheetTheme += '<li><span>去程交通：</span><em>' + quotedPriceJson.quotedPriceQuery.departureIslandWayText + '</em></li>';
                sheetTheme += '<li><span>返程交通：</span><em>' + quotedPriceJson.quotedPriceQuery.arrivalIslandWayText + '</em></li>';
                for (var i in travelerTypes) {
                	if(travelerTypes[i].isThirdPerson&&travelerTypes[i].isThirdPerson==1){
                	
                	}else{
                		sheetTheme += '<li><span>' + travelerTypes[i].travelerTypeText + '数：</span><em>' + quotedPriceJson.quotedPriceQuery.personNum[i] + '</em></li>';
                	}
                }
                sheetTheme += '<li><span>间数：</span><em>' + quotedPriceJson.quotedPriceQuery.roomNum + '间</em></li>';
                sheetTheme += '<li><span>混住次数：</span><em>' + quotedPriceJson.quotedPriceQuery.mixliveNum + '次</em></li>';
                sheetTheme += '</ul>';
                sheetTheme += '</div>';
                sheetTheme += '<table id="contentTable_new" class="table activitylist_bodyer_table_new sea_rua_table">';
                sheetTheme += '<thead>';
                sheetTheme += '<tr>';
                sheetTheme += '';
                sheetTheme += '<th width="10%">报价结果</th>';
                for (var i in travelerTypes) {
                    sheetTheme += '<th width="10%">' + travelerTypes[i].travelerTypeText + '/人</th>';
                }
                //sheetTheme += '<th width="10%">第三人成人/人</th>';
                sheetTheme += '<th width="10%">混住费用</th>';
                sheetTheme += '<th width="15%">操作</th>';
                sheetTheme += '</tr>';
                sheetTheme += '</thead>';
                sheetTheme += '<tbody>';
                sheetTheme += '<tr id="preTotal">';
                //增加隐藏域，存放价格信息json start
                var tempJSONObject = {};
                for(var propName in quotedPriceJson){
                	if(propName!='preferentialTotal'){
                	tempJSONObject[propName]=quotedPriceJson[propName];
                	}
                }
                sheetTheme += '<input type=hidden name=guestTypePriceJsonStr value=\''+ JSON.stringify(tempJSONObject.guestPriceList) +'\'>';//快速订单使用
                sheetTheme += '<input type=hidden name=hotelPlUuid value=\''+ tempJSONObject.hotelPlUuid +'\'>';//快速订单使用
                sheetTheme += '<input type=hidden name=quotedPriceJsonStr value=\''+ JSON.stringify(tempJSONObject) +'\'>';//保存记录 使用
                sheetTheme += '<input type=hidden name=preferentialPriceJson >';//默认的优惠信息
                 //增加隐藏域，存放价格信息json end
                
                sheetTheme += '<td class="tc tdgreen">优惠前合计</td>';
                for (var i in travelerTypes) {
	                if(quotedPriceJson.guestPriceList[i].currencyText=="-"){
	                	sheetTheme += '<td class="tr quotation_main_list_hotel_name_f14 tdgreen">' + quotedPriceJson.guestPriceList[i].currencyText + '</td>';
	                }else{
	                	sheetTheme += '<td class="tr quotation_main_list_hotel_name_f14 tdgreen">' + quotedPriceJson.guestPriceList[i].currencyText + quotedPriceJson.guestPriceList[i].amountString + '</td>';
	                }
                    
                }
                //if(quotedPriceJson.thirdPersonPriceCurrencyText=="-"){
                //sheetTheme += '<td class="tr quotation_main_list_hotel_name_f14 tdgreen">' + quotedPriceJson.thirdPersonPriceCurrencyText  + '</td>';
               	//}else{
                //sheetTheme += '<td class="tr quotation_main_list_hotel_name_f14 tdgreen">' + quotedPriceJson.thirdPersonPriceCurrencyText + quotedPriceJson.thirdPersonPrice + '</td>';
                //}
                if(quotedPriceJson.mixlivePriceCurrencyText=="-"){
                sheetTheme += '<td class="tr quotation_main_list_hotel_name_f14 tdgreen">' + quotedPriceJson.mixlivePriceCurrencyText + '</td>';
                }else{
                sheetTheme += '<td class="tr quotation_main_list_hotel_name_f14 tdgreen">' + quotedPriceJson.mixlivePriceCurrencyText + quotedPriceJson.mixlivePriceString + '</td>';
                }
                
                sheetTheme += '<td class="tc p0"><div style="position: relative">';

                sheetTheme += '<a href="#" name="usePreferential">使用优惠</a> | ';
                sheetTheme += '<a onclick="rapidCreateOrder(this,0);" href="javascript:void(0)">快速生成订单</a> | ';
                sheetTheme += '<a onclick="expand(\'#quotedPrice_'+rowID+'\',this)" href="javascript:void(0)" class="">展开明细</a> | ';
                sheetTheme += '<a  name="copyQuotedPrice">复制</a>';
                sheetTheme += '</div></td>';
                sheetTheme += '</tr>';
                //<!--二级表格 开始-->
                sheetTheme += '<tr class="pro_two_table_bg tdbackground_color " id=quotedPrice_'+rowID+' name="quotedPriceDetailList">';
                sheetTheme += '<td colspan="10" class="product_twolist_table_bgtd " id="quotation_main_list_hotel_name_td">';
                sheetTheme += '<table id="background_color_white" class=" activitylist_bodyer_table_new mar_topnone10 ">';
                sheetTheme += '<thead>';
                sheetTheme += '<tr>';
                sheetTheme += '<th class="tc" width="10%">入住日期</th>';
                sheetTheme += '<th class="tc" width="10%">房型（容住率）</th>';
                sheetTheme += '<th class="tc" width="10%"> <p>餐型</p></th>';
                for (var i in travelerTypes) {
                    sheetTheme += '<th class="tc" width="15%">' + travelerTypes[i].travelerTypeText + '/人</th>';
                }

                //sheetTheme += '<th class="tc" width="15%">第三人成人/人</th>';
                sheetTheme += '<th class="tc" width="5%">操作</th>';
                sheetTheme += '</tr>';
                sheetTheme += '</thead>';
                sheetTheme += '<tbody>';
                for (var i in quotedPriceJson.detailList) {
                    var detail = quotedPriceJson.detailList[i];
                    sheetTheme += '<tr>';
                    sheetTheme += '<td class="tc"><p>' + detail.inDate + '</p></td>';
                    sheetTheme += '<td class="tc " title=' + detail.memo + '>' + detail.hotelRoomName + '<br/>（' + detail.hotelRoomOccupancyRate + '）</td>';
                    sheetTheme += '<td class="tc"><p>' + detail.hotelMealText + '</p></td>';
                    for (var j in travelerTypes) {
                    	if(detail.guestPriceList[j].currencyText=="-"){
                    	sheetTheme += '<td class="tr"><p>' + detail.guestPriceList[j].currencyText + '</p></td>';
                    	}else{
                    	sheetTheme += '<td class="tr"><p>' + detail.guestPriceList[j].currencyText + detail.guestPriceList[j].amountString + '</p></td>';
                    	}
                        
                    }
                    
                    //if(detail.thirdPersonPriceCurrencyText=="-"){
                    //sheetTheme += '<td class="tr"><p>' + detail.thirdPersonPriceCurrencyText + '</p></td>';
                    //}else{
                    //sheetTheme += '<td class="tr"><p>' + detail.thirdPersonPriceCurrencyText + detail.thirdPersonPrice + '</p></td>';
                    //}
                    sheetTheme += '<td class="tc"><div style="position: relative"><a name="copyQuotedPriceDtl">复制</a></div></td>';
                    sheetTheme += '</tr>';
                }
                sheetTheme += '</tbody>';
                sheetTheme += '</table>';
                sheetTheme += '<table id="background_color_white" class=" activitylist_bodyer_table_new mar_topnone10 ">';
                sheetTheme += '<tbody>';
                sheetTheme += '<tr>';
                sheetTheme += '<td class="tl" style="padding:30px;">';
                sheetTheme += '<p>备注：</p>';
                sheetTheme += '<p>' + quotedPriceJson.memo + '</p>';
                sheetTheme += '</td>';
                sheetTheme += '</tr>';
                sheetTheme += '</tbody>';
                sheetTheme += '</table>';
                sheetTheme += '</td>';
                sheetTheme += '</tr>';
                //<!--二级表格 结束-->
                sheetTheme += '</tbody>';
                sheetTheme += '</table>';
                sheetTheme += '</div>';
                var  $sheetTheme =$(sheetTheme);
                //$sheetTheme.find("#preTotal").data("preferentialTotal",quotedPriceJson.preferentialTotal);
                $sheetTheme.find("#preTotal").data("quotedPriceJsonNew",quotedPriceJson);
                $priceResult.append($sheetTheme);
                $("#priceResult").trigger("childCount.change");
                $sheetTheme.data("quotedPriceJson",quotedPriceJson);
                $sheetTheme.find("[name='copyQuotedPrice']").zclip({
                   copy:getQuotedPriceCopyText,
                    path:"${ctxStatic}/jquery.zclip/ZeroClipboard.swf"
                });
                rowID++;
            }
        }
        function usePreferential(nonePreferentialRow){
            var quotedPriceJson = $(nonePreferentialRow).data("quotedPriceJsonNew");
            var preferentialTotal =quotedPriceJson.preferentialTotal;
            var $popTemplate = $('#information-discount-pop2').clone().removeClass('information-discount-pop');
            var $nonepreferentialPrice = $popTemplate.find('#nonepreferentialPrice');
            /* for(var i in quotedPriceJson.guestPriceList ){
                var guestPrice = quotedPriceJson.guestPriceList[i];
                $nonepreferentialPrice.append(' <span>'+guestPrice.travelerTypeText+'/人 '+guestPrice.currencyText+guestPrice.amount+'</span>');
            }
            $nonepreferentialPrice.append(' <span>第三人成人/人 '+quotedPriceJson.thirdPersonPriceCurrencyText+quotedPriceJson.thirdPersonPrice+'</span>');
            $nonepreferentialPrice.append(' <span>混住费用/人 '+quotedPriceJson.mixlivePriceCurrencyText+quotedPriceJson.mixlivePrice+'</span>'); */
            for(var i in quotedPriceJson.guestPriceList ){
                var guestPrice = quotedPriceJson.guestPriceList[i];
                if(guestPrice.currencyText=="-"){
                	$nonepreferentialPrice.append(' <span>'+guestPrice.travelerTypeText+'/人 '+guestPrice.currencyText+'</span>');
                }else{
                	$nonepreferentialPrice.append(' <span>'+guestPrice.travelerTypeText+'/人 '+guestPrice.currencyText+guestPrice.amountString+'</span>');
                }
            }
            //if(quotedPriceJson.thirdPersonPriceCurrencyText=="-"){
            //	$nonepreferentialPrice.append(' <span>第三人成人/人 '+quotedPriceJson.thirdPersonPriceCurrencyText+'</span>');
            //}else{
           // 	$nonepreferentialPrice.append(' <span>第三人成人/人 '+quotedPriceJson.thirdPersonPriceCurrencyText+quotedPriceJson.thirdPersonPrice+'</span>');
            //}
            if(quotedPriceJson.mixlivePriceCurrencyText=="-"){
            	$nonepreferentialPrice.append(' <span>混住费用/人 '+quotedPriceJson.mixlivePriceCurrencyText+'</span>');
            }else{
            	$nonepreferentialPrice.append(' <span>混住费用/人 '+quotedPriceJson.mixlivePriceCurrencyText+quotedPriceJson.mixlivePrice+'</span>');
            }
            for(var i in preferentialTotal ) {
                var preferentialGroup = preferentialTotal[i];
                var $preferentialTable=$('<table width="890" align="center" cellspacing="3" class="information-discount-otherprojecttable"></table>');
                $preferentialTable.data('preferentialGroup',preferentialGroup);
                var preferentialCount=preferentialGroup.preferentialList?preferentialGroup.preferentialList.length:null;
                if (preferentialCount) {
                    $popTemplate.find('.information-discount-projectgroup').append($preferentialTable);
                    for (var j in preferentialGroup.preferentialList) {
                        var preferential = preferentialGroup.preferentialList[j];
                        var $tr = $('<tr></tr>');
                        if (j == 0) {
                            $tr.append('' +
                                    '<td class=" project tc" width="70" rowspan="' + preferentialCount + '"><label>' +
                                    '<input name="solution" type="radio" value="">' +
                                    '方案' + (+i + 1) + '</label></td>'
                            );
                        }
                        $tr.append('' +
                                '<td class="column-left tl" width="180">' +
                                preferential.preferentialName + '<br/> 下单代码：' +
                                preferential.bookingCode +
                                '</td>'
                        );

                        $tr.append('' +
                                '<td class="column  tl" width="320">' +
                                 (preferential.description===undefined?'':preferential.description)+
                                '</td>'
                        );

                        if (j == 0) {
                            var preferentialAmountHtml = "";
                            if (!preferentialGroup.totalPrice) {
                                for (var k in preferentialGroup.guestPriceList) {
                                    var guestPrice = preferentialGroup.guestPriceList[k];
                                    if(guestPrice.currencyText=="-"){
                                    preferentialAmountHtml += '<p>' + guestPrice.travelerTypeText + '/人优惠' + guestPrice.currencyText + '<br/>' ;
                                    }else{
                                    preferentialAmountHtml += '<p>' + guestPrice.travelerTypeText + '/人优惠' + guestPrice.currencyText + (guestPrice.preferAmount===undefined?'':guestPrice.preferAmount) + '<br/>' ;
                                    }
                                    if(guestPrice.currencyText=="-"){
                                    preferentialAmountHtml += '优惠后价格：<span class="information-discount-price">' + guestPrice.currencyText + '</span></p>';
                                    }else{
                                    preferentialAmountHtml += '优惠后价格：<span class="information-discount-price">' + guestPrice.currencyText + guestPrice.amountString + '</span></p>';
                                    }
                                    
                                }
                                //if(preferentialGroup.thirdPersonPriceCurrencyText=="-"){
                                //preferentialAmountHtml += '<p>第三人成人/人优惠' + preferentialGroup.thirdPersonPriceCurrencyText + '<br/>' ;
                                //preferentialAmountHtml += '优惠后价格：<span class="information-discount-price">' + preferentialGroup.thirdPersonPriceCurrencyText + '</span></p>';
                                //}else{
                                //preferentialAmountHtml += '<p>第三人成人/人优惠' + preferentialGroup.thirdPersonPriceCurrencyText + (guestPrice.amount===undefined?'':guestPrice.amount) + '<br/>' ;
                                //preferentialAmountHtml += '优惠后价格：<span class="information-discount-price">' + preferentialGroup.thirdPersonPriceCurrencyText + preferentialGroup.thirdPersonPrice + '</span></p>';
								//}
								if(preferentialGroup.mixlivePriceCurrencyText =="-"){
								preferentialAmountHtml += '<p>混住费用优惠后价格：<span class="information-discount-price">' + preferentialGroup.mixlivePriceCurrencyText + '</span></p>';
								}else{
								preferentialAmountHtml += '<p>混住费用优惠后价格：<span class="information-discount-price">' + preferentialGroup.mixlivePriceCurrencyText + preferentialGroup.mixlivePrice + '</span></p>';
								}
                                
                            }
                            else {
                            	if(preferentialGroup.totalPriceCurrencyText =="-"){
								preferentialAmountHtml = '<p>优惠后价格：<span class="information-discount-price">' + preferentialGroup.totalPriceCurrencyText + '</span></p>';
								}else{
								preferentialAmountHtml = '<p>优惠后价格：<span class="information-discount-price">' + preferentialGroup.totalPriceCurrencyText + preferentialGroup.totalPrice + '/间</span></p>';
								}
                                
                            }
                            $tr.append('<td class="column  tl" width="200" rowspan="' + preferentialCount + '">' +
                            preferentialAmountHtml +
                            '</td>>');
                        }
                        $preferentialTable.append($tr);
                    }
                }else{
                    $popTemplate.find('.information-discount-projectgroup').append('<div class="preferential-empty">暂无符合条件的优惠信息！</div>');
                    $popTemplate.find('.information-discount-btnouter').remove();
                }
            }
			if(!preferentialTotal){
				$popTemplate.find('.information-discount-projectgroup').append('<div class="preferential-empty">暂无符合条件的优惠信息！</div>');
                $popTemplate.find('.information-discount-btnouter').remove();
			}

            var $preferentialList4hotelPlTable = $popTemplate.find('.information-discount-other table');
            var preferentialList4hotelPl = quotedPriceJson.preferentialList4hotelPl;
            if (preferentialList4hotelPl && preferentialList4hotelPl.length) {
                for (var i in preferentialList4hotelPl) {
                    var otherPreferential = preferentialList4hotelPl[i];
                    var $tr = $('<tr></tr>');
                    $tr.append('<td class="information-discountrelated-otherproject-column-left tl" width="320" height="45">' +
                    otherPreferential.preferentialName + ' ' + '下单代码：' + otherPreferential.bookingCode +
                    '</td>');
                    $tr.append('<td class="information-discountrelated-otherproject-column-left tl" width="320" height="45">' +
                    (otherPreferential.description===undefined?'':otherPreferential.description)+
                    '</td>');
                    $preferentialList4hotelPlTable.append($tr);
                }
            }else{
                $popTemplate.find('.information-discount-other').empty().append('<div class="preferential-empty">暂无优惠推荐！</div>');
            }
            var $pop = $.jBox("<div name='PopUsePreferential'></div>",{
                title:"优惠信息",
                buttons:{  },
                width:950
            });
            $pop.find('[name="PopUsePreferential"]').append($popTemplate);
            $pop.on('change','input[name="solution"]',function(){
                $(this).parents('.information-discount-projectgroup:first').find('table').removeClass('information-discount-table').addClass('information-discount-otherprojecttable');
                $(this).parents('table:first').removeClass('information-discount-otherprojecttable').addClass('information-discount-table');
                $pop.find('#btnSelectPreferential').removeClass('gray');
            });
            $pop.on("click","#btnSelectPreferential:not(.gray)",function(){
                var selectedPreferential = $pop.find('input:checked').parents('table:first').data('preferentialGroup');
                selectPreferential(selectedPreferential,nonePreferentialRow);
                $.jBox.close();
            });
        }
        function selectPreferential(preferential,nonePreferentialRow){
            var $nonePreferentialRow = $(nonePreferentialRow);
            $nonePreferentialRow.siblings('[name!="quotedPriceDetailList"]').remove();
            var $preferentialRow =$('<tr></tr>');
            $preferentialRow.append('<td class="tc tdred">优惠后合计</td>');
            if(!preferential.totalPrice){
                for (var i in travelerTypes) {
                    $preferentialRow.append('');
                    if(preferential.guestPriceList[i].currencyText=="-"){
                    $preferentialRow.append( '<td class="tr quotation_main_list_hotel_name_f14 tdred preferential-td">' + preferential.guestPriceList[i].currencyText+ '</td>');
                    }else{
                    $preferentialRow.append( '<td class="tr quotation_main_list_hotel_name_f14 tdred preferential-td">' + preferential.guestPriceList[i].currencyText+ preferential.guestPriceList[i].amountString+ '</td>');
                    }
                    
                }
                //if(preferential.thirdPersonPriceCurrencyText=="-"){
                //$preferentialRow.append('<td class="tr quotation_main_list_hotel_name_f14 tdred preferential-td">'+preferential.thirdPersonPriceCurrencyText+'</td>');
                //}else{
                //$preferentialRow.append('<td class="tr quotation_main_list_hotel_name_f14 tdred preferential-td">'+preferential.thirdPersonPriceCurrencyText+preferential.thirdPersonPrice+'</td>');
                //}
                if(preferential.mixlivePriceCurrencyText=="-"){
                $preferentialRow.append('<td class="tr quotation_main_list_hotel_name_f14 tdred preferential-td">'+preferential.mixlivePriceCurrencyText+'</td>');
                }else{
                $preferentialRow.append('<td class="tr quotation_main_list_hotel_name_f14 tdred preferential-td">'+preferential.mixlivePriceCurrencyText+preferential.mixlivePrice+'</td>');
                }
                
            }else{
                var totalColSpan = travelerTypes.length+1;
                if(preferential.totalPriceCurrencyText=="-"){
                $preferentialRow.append('<td colspan="'+totalColSpan+'" class="tr quotation_main_list_hotel_name_f14 tdred preferential-td tc">'+preferential.totalPriceCurrencyText+'/间'+'</td>');
                }else{
                $preferentialRow.append('<td colspan="'+totalColSpan+'" class="tr quotation_main_list_hotel_name_f14 tdred preferential-td tc">'+preferential.totalPriceCurrencyText+preferential.totalPrice+'/间'+'</td>');
                }
                
            }
            $preferentialRow.append('<td class="p0 tc"><div style="position: relative">'
            +'<a name="usedPreferential">已使用优惠</a> | '
            //+'<a href="#" >快速生成订单</a> | '
            +'<a onclick="rapidCreateOrder(this,1);" href="javascript:void(0)">快速生成订单</a>|'
            +'<a name="copyPreferential">复制</a>'
            +'</div></td>');
            $preferentialRow.data("preferential",preferential);
			$nonePreferentialRow.find("input[name='preferentialPriceJson']").val(JSON.stringify(preferential));
            $nonePreferentialRow.parent().append($preferentialRow);
            $preferentialRow.find("[name='copyPreferential']").zclip({
                copy:getPreferentialCopyText,
                path:"jquery.zclip/ZeroClipboard.swf"
            });
            $preferentialRow.on("click","[name='usedPreferential']",function(){
                showUsedPreferential(preferential ,$(this));
            });
        }
        function showUsedPreferential(preferentialGroup,$el) {
            var $nonePreferentialRow = $el.parents('table:first').find('#preTotal');
            var quotedPriceJson = $nonePreferentialRow.data("quotedPriceJsonNew");
            var $popTemplate = $('#information-discount-pop2').clone().removeClass('information-discount-pop');
            $popTemplate.find('.information-discount-btnouter').remove();
            var $nonepreferentialPrice = $popTemplate.find('#nonepreferentialPrice');

            for(var i in quotedPriceJson.guestPriceList ){
                var guestPrice = quotedPriceJson.guestPriceList[i];
                if(guestPrice.currencyText=="-"){
                $nonepreferentialPrice.append(' <span>'+guestPrice.travelerTypeText+'/人 '+guestPrice.currencyText+'</span>');
                }else{
                $nonepreferentialPrice.append(' <span>'+guestPrice.travelerTypeText+'/人 '+guestPrice.currencyText+guestPrice.amountString+'</span>');
                }
                
            }
            //if(quotedPriceJson.thirdPersonPriceCurrencyText=="-"){
            //$nonepreferentialPrice.append(' <span>第三人成人/人 '+quotedPriceJson.thirdPersonPriceCurrencyText+'</span>');
            //}else{
            //$nonepreferentialPrice.append(' <span>第三人成人/人 '+quotedPriceJson.thirdPersonPriceCurrencyText+quotedPriceJson.thirdPersonPrice+'</span>');
            //}
            if(quotedPriceJson.mixlivePriceCurrencyText=="-"){
            $nonepreferentialPrice.append(' <span>混住费用/人 '+quotedPriceJson.mixlivePriceCurrencyText+'</span>');
            }else{
            $nonepreferentialPrice.append(' <span>混住费用/人 '+quotedPriceJson.mixlivePriceCurrencyText+quotedPriceJson.mixlivePrice+'</span>');
            }
            
            

            var $preferentialTable=$('<table width="890" align="center" cellspacing="3" class="information-discount-otherprojecttable"></table>');
            $preferentialTable.data('preferential',preferentialGroup);
            $popTemplate.find('.information-discount-projectgroup').append($preferentialTable);
            var preferentialCount=preferentialGroup.preferentialList.length;
            for(var j in preferentialGroup.preferentialList){
                    var preferential = preferentialGroup.preferentialList[j];
                    var $tr = $('<tr></tr>');
                    $tr.append('' +
                            '<td class="column-left tl" width="180">' +
                            preferential.preferentialName+'<br/> 下单代码：'+
                            preferential.bookingCode +
                            '</td>'
                    );

                    $tr.append('' +
                            '<td class="column  tl" width="320">' +
                            (preferential.description===undefined?'':preferential.description)+
                            '</td>'
                    );

                    if(j==0){
                        var preferentialAmountHtml="";
                        if(!preferentialGroup.totalPrice) {
                            for (var k in preferentialGroup.guestPriceList) {
                                var guestPrice = preferentialGroup.guestPriceList[k];
                                if(guestPrice.currencyText=="-"){
                                preferentialAmountHtml += '<p>' + guestPrice.travelerTypeText + '/人优惠' + guestPrice.currencyText + '<br/>' ;
                                }else{
                                preferentialAmountHtml += '<p>' + guestPrice.travelerTypeText + '/人优惠' + guestPrice.currencyText + (guestPrice.preferAmount===undefined?'':guestPrice.preferAmount) + '<br/>' ;
                                }
                                if(guestPrice.currencyText=="-"){
                                preferentialAmountHtml += '优惠后价格：<span class="information-discount-price">' + guestPrice.currencyText + '</span></p>';
                                }else{
                                preferentialAmountHtml += '优惠后价格：<span class="information-discount-price">' + guestPrice.currencyText + guestPrice.amountString + '</span></p>';
                                }
                            }
                            //if(preferentialGroup.thirdPersonPriceCurrencyText=="-"){
                            //preferentialAmountHtml += '<p>第三人成人/人优惠' + preferentialGroup.thirdPersonPriceCurrencyText + '<br/>' ;
                            //}else{
                            //preferentialAmountHtml += '<p>第三人成人/人优惠' + preferentialGroup.thirdPersonPriceCurrencyText + (preferentialGroup.thirdPersonPreferAmount===undefined?'':preferentialGroup.thirdPersonPreferAmount) + '<br/>' ;
                            //}
                            //if(preferentialGroup.thirdPersonPriceCurrencyText=="-"){
                            //preferentialAmountHtml += '优惠后价格：<span class="information-discount-price">' + preferentialGroup.thirdPersonPriceCurrencyText + '</span></p>';
                            //}else{
                            //preferentialAmountHtml += '优惠后价格：<span class="information-discount-price">' + preferentialGroup.thirdPersonPriceCurrencyText + preferentialGroup.thirdPersonPrice + '</span></p>';
                            //}
                            if(preferentialGroup.mixlivePriceCurrencyText=="-"){
                            preferentialAmountHtml += '<p>混住费用优惠后价格：<span class="information-discount-price">' + preferentialGroup.mixlivePriceCurrencyText + '</span></p>';
                            }else{
                            preferentialAmountHtml += '<p>混住费用优惠后价格：<span class="information-discount-price">' + preferentialGroup.mixlivePriceCurrencyText + preferentialGroup.mixlivePrice + '</span></p>';
                            }
                        }
                        else{
                        	if(preferentialGroup.totalPriceCurrencyText=="-"){
                        	preferentialAmountHtml='<p>优惠后价格：<span class="information-discount-price">' + preferentialGroup.totalPriceCurrencyText + '</span></p>';
                        	}else{
                        	preferentialAmountHtml='<p>优惠后价格：<span class="information-discount-price">' + preferentialGroup.totalPriceCurrencyText + preferentialGroup.totalPrice +'/间'+ '</span></p>';
                        	}
                            
                        }
                        $tr.append('<td class="column  tl" width="200" rowspan="'+preferentialCount+'">' +
                        preferentialAmountHtml +
                        '</td>>');
                    }
                    $preferentialTable.append($tr);
                $preferentialTable.append($tr);
                }
            $popTemplate.find('.information-discount-main').siblings().remove();

            $popTemplate.find('.information-discount-projectgroup').css({'height':'500px'});
            $popTemplate.find('.information-discount-main').css({'box-shadow':'none'});

            var $pop = $.jBox("<div name='PopUsePreferential'></div>",{
                title:"优惠信息",
                buttons:{  },
                width:950
            });
            $pop.find('[name="PopUsePreferential"]').append($popTemplate);
        }
        function getQuotedPriceCopyText(){
            var $sheetTheme = $(this).parents('.quotation_main_list_hotel_echo:first');
            var quotedPriceJson =  $sheetTheme.data("quotedPriceJson");
            var checkinDate = quotedPriceJson.detailList[0];
            for (var i in quotedPriceJson.detailList) {
                var inDate = quotedPriceJson.detailList[i].inDate;
                if (inDate < checkinDate) {
                    checkinDate = inDate;
                }
            }
            var endDate = quotedPriceJson.detailList[quotedPriceJson.detailList.length-1].inDate;
            var islandText = quotedPriceJson.quotedPriceQuery.islandText;
            var roomDays = [];
            for (var i in quotedPriceJson.detailList) {
                var quotedPriceRoom = quotedPriceJson.detailList[i];
                var roomDaysLength = roomDays.length;
                if (roomDaysLength > 0 && roomDays[roomDaysLength - 1].hotelRoomUuid == quotedPriceRoom.hotelRoomUuid) {
                    roomDays[roomDaysLength - 1].num += 1;
                } else {
                    roomDays.push({
                        hotelRoomUuid: quotedPriceRoom.hotelRoomUuid,
                        hotelRoomName: quotedPriceRoom.hotelRoomName,
                        num: 1
                    });
                }
            }
            var roomDayText = "";
            for (var i in roomDays) {
                roomDayText += roomDays[i].hotelRoomName + "*" + roomDays[i].num + "晚,";
            }
            roomDayText = roomDayText.substr(0, roomDayText.length - 1);

            var mealText ="";
            for(var i in quotedPriceJson.detailList){
                if(i==0){
                    mealText+=quotedPriceJson.detailList[i].hotelMealText;
                    if(i<quotedPriceJson.detailList.length-1){
                        mealText+=',';
                    }
                }
                else{
                    if(quotedPriceJson.detailList[i]!=quotedPriceJson.detailList[i-1]){
                        mealText+=quotedPriceJson.detailList[i].hotelMealText;
                        if(i<quotedPriceJson.detailList.length-1){
                            mealText+=',';
                        }
                    }
                }
            }
            var islandWayText=quotedPriceJson.quotedPriceQuery.departureIslandWayText;
            if(islandWayText!=quotedPriceJson.quotedPriceQuery.arrivalIslandWayText){
                islandWayText+=','+quotedPriceJson.quotedPriceQuery.arrivalIslandWayText;
            }
            var guestPriceListText="";
            for(var i in quotedPriceJson.guestPriceList){
                var guestPrice = quotedPriceJson.guestPriceList[i];
                var guestPriceText = guestPrice.travelerTypeText+'/人合计：'+guestPrice.currencyText+guestPrice.amountString;
                guestPriceListText+=guestPriceText+'\t    '
            }
            
            //if(quotedPriceJson.thirdPersonPriceCurrencyText&&quotedPriceJson.thirdPersonPriceCurrencyText=="-"){
            //	guestPriceListText+='第三人成人/人合计：'+quotedPriceJson.thirdPersonPriceCurrencyText+'\t';
            //}else{
            //	guestPriceListText+='第三人成人/人合计：'+quotedPriceJson.thirdPersonPriceCurrencyText+quotedPriceJson.thirdPersonPrice+'\t';
            //}
            if(quotedPriceJson.mixlivePriceCurrencyText&&quotedPriceJson.mixlivePriceCurrencyText=="-"){
            	guestPriceListText+='混住费合计：'+quotedPriceJson.mixlivePriceCurrencyText;
            }else{
            	guestPriceListText+='混住费合计：'+quotedPriceJson.mixlivePriceCurrencyText+quotedPriceJson.mixlivePrice;
            }
            
            return checkinDate+'~'+endDate+'\t    '+islandText+'\t    '+roomDayText+'\t    '+mealText+'\t    '+islandWayText+'\t    '+guestPriceListText;
        }
        function getQuotedPriceDtlCopyText(){
            var $sheetTheme = $(this).parents('.quotation_main_list_hotel_echo:first');
            var  $tr = $(this).parents('tr:first');
            var inDate = $tr.find("td:first").text();
            var quotedPriceJson =  $sheetTheme.data("quotedPriceJson");
            var quotedPriceJsonDtl;
            for(var i in quotedPriceJson.detailList){
                if(quotedPriceJson.detailList[i].inDate==inDate){
                    quotedPriceJsonDtl=quotedPriceJson.detailList[i];
                    break;
                }
            }


            var islandText = quotedPriceJson.quotedPriceQuery.islandText;
            var roomDayText=quotedPriceJsonDtl.hotelRoomName;
            var mealText =$tr.find("td:eq(2)").text();

            var islandWayText=quotedPriceJson.quotedPriceQuery.departureIslandWayText;
            if(islandWayText!=quotedPriceJson.quotedPriceQuery.arrivalIslandWayText){
                islandWayText+=','+quotedPriceJson.quotedPriceQuery.arrivalIslandWayText;
            }
            var guestPriceListText="";
            for(var i in quotedPriceJsonDtl.guestPriceList){
                var guestPrice = quotedPriceJsonDtl.guestPriceList[i];
                var guestPriceText = guestPrice.travelerTypeText+'/人：'+guestPrice.currencyText+guestPrice.amountString;
                guestPriceListText+=guestPriceText+'\t    '
            }
            //guestPriceListText+='第三人成人/人：'+quotedPriceJsonDtl.thirdPersonPriceCurrencyText+quotedPriceJsonDtl.thirdPersonPrice;
            return inDate+'\t    '+islandText+'\t    '+roomDayText+'\t    '+mealText+'\t    '+islandWayText+'\t    '+guestPriceListText;
        }
         function getPreferentialCopyText(){
            var $sheetTheme = $(this).parents('.quotation_main_list_hotel_echo:first');
            var $tr =$(this).parents('tr:first');
            var quotedPriceJson =  $sheetTheme.data("quotedPriceJson");
            var preferential = $tr.data("preferential");
            var checkinDate = quotedPriceJson.detailList[0];
            for (var i in quotedPriceJson.detailList) {
                var inDate = quotedPriceJson.detailList[i].inDate;
                if (inDate < checkinDate) {
                    checkinDate = inDate;
                }
            }
            var endDate = quotedPriceJson.detailList[quotedPriceJson.detailList.length-1].inDate;
            var islandText = quotedPriceJson.quotedPriceQuery.islandText;
            var roomDays = [];
            for (var i in quotedPriceJson.detailList) {
                var quotedPriceRoom = quotedPriceJson.detailList[i];
                var roomDaysLength = roomDays.length;
                if (roomDaysLength > 0 && roomDays[roomDaysLength - 1].hotelRoomUuid == quotedPriceRoom.hotelRoomUuid) {
                    roomDays[roomDaysLength - 1].num += 1;
                } else {
                    roomDays.push({
                        hotelRoomUuid: quotedPriceRoom.hotelRoomUuid,
                        hotelRoomName: quotedPriceRoom.hotelRoomName,
                        num: 1
                    });
                }
            }
            var roomDayText = "";
            for (var i in roomDays) {
                roomDayText += roomDays[i].hotelRoomName + "*" + roomDays[i].num + "晚,";
            }
            roomDayText = roomDayText.substr(0, roomDayText.length - 1);

            var mealText ="";
            for(var i in quotedPriceJson.detailList){
                if(i==0){
                    mealText+=quotedPriceJson.detailList[i].hotelMealText;
                    if(i<quotedPriceJson.detailList.length-1){
                        mealText+=',';
                    }
                }
                else{
                    if(quotedPriceJson.detailList[i]!=quotedPriceJson.detailList[i-1]){
                        mealText+=quotedPriceJson.detailList[i].hotelMealText;
                        if(i<quotedPriceJson.detailList.length-1){
                            mealText+=',';
                        }
                    }
                }
            }
            var islandWayText=quotedPriceJson.quotedPriceQuery.departureIslandWayText;
            if(islandWayText!=quotedPriceJson.quotedPriceQuery.arrivalIslandWayText){
                islandWayText+=','+quotedPriceJson.quotedPriceQuery.arrivalIslandWayText;
            }
            var priceListText = "";
            if(!preferential.totalPrice) {
                for (var i in preferential.guestPriceList) {
                    var guestPrice = preferential.guestPriceList[i];
                    var guestPriceText = guestPrice.travelerTypeText + '/人合计：' + guestPrice.currencyText + guestPrice.amountString;
                    priceListText += guestPriceText + '\t    '
                }
                //priceListText += '第三人成人/人合计：' + preferential.thirdPersonPriceCurrencyText + preferential.thirdPersonPrice + '\t    ';
                priceListText += '混住费合计：' + preferential.mixlivePriceCurrencyText + preferential.mixlivePrice;
            }
            else{
                priceListText='打包优惠合计：'+preferential.totalPriceCurrencyText+preferential.totalPrice+'/人';
            }
           return  checkinDate+'~'+endDate+'\t    '+islandText+'\t    '+roomDayText+'\t    '+mealText+'\t    '+islandWayText+'\t    '+priceListText;
        }
    </script>

<!--S优惠信息弹出层-->
<div class="information-discount-pop" id="information-discount-pop2">
    <div class="information-discount-main">
        <div class="title">
            <span>优惠前原价:</span>
            <span id="nonepreferentialPrice"></span>
        </div>
        <div class="information-discount-projectgroup">

        </div>
        <div class="information-discount-btnouter">
            <input value="确认" class="btn btn-primary gray" id="btnSelectPreferential" type="button">
        </div>
    </div>
    <div class="information-discount-main-about"> 相关优惠推荐</div>
    <div class="information-discount-other">
        <table width="890" align="center" cellspacing="3" class="information-discountrelated-table" >
        </table>
    </div>
</div>
<!--E优惠信息弹出层-->
</body>
</html>
