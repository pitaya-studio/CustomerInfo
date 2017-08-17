<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<html>
<head>
	<title>产品修改</title>

	<style>
		.custom-combobox-toggle {
			height: 26px;
			margin-left: -1px;
			padding: 0;
			/* support: IE7 */
			*height: 1.7em;
			*top: 0.1em;
		}
		.custom-combobox-input {
			margin: 0;
			padding: 0.3em;width:100px;
		}
		.ui-autocomplete{height:200px;overflow:auto;}




		/* 可见用户*/
		.mod_information_d2.user_show > div{
			display:inline-block;
			width:200px;
			overflow:hidden;
			text-overflow:ellipsis;
		}

		/* 团号一行显示*/
		.ellipsis-number-team{overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 150px; display: block;}

		/*修复bug:団期修改时币种和价格分行显示*/
		.table-mod2-group input.ipt-currency, .activitylist_bodyer_table input.rmb {
			background-color: #FFF;
			width: 63%;
			padding: 4px 0px;
		}
		/*0071需求样式 */
		label.myerror {
			color: #ea5200;
			font-weight: bold;
			margin-left: 0px;
			padding-bottom: 2px;
			padding-left: 0px;
		}
		.batch-ol li {
			overflow: hidden;
		}
		.grounding-two,.grounding-one{
			display:none;
		}

	</style>


	<meta name="decorator" content="wholesaler"/>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactor2.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctxStatic}/json/jquery.json.ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/i18n/jquery-ui-i18n.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-other/jquery-combobox_prop.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/jquery.disabled.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/activity/dynamic.validator.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/jquery.date.format.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/jquery.file.filter.js" type="text/javascript"></script>
	<shiro:hasPermission name="price:project">
		<script src="${ctxStatic}/modules/activity/groupPrice.js" type="text/javascript"></script>
	</shiro:hasPermission>
	<script src="${ctxStatic}/modules/activity/activityMod.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/activity/activityModForLoose.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/product/discount-setting.js" type="text/javascript"></script>

	<!-- 223 需求 -->
	<script type="text/javascript" src="${ctxStatic}/modules/stock/stock-list.js"></script>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/quauq.css"/>

	<!-- 微信 需求 -->
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/wechatList.css"/>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/dist/cropper.css"/>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/dist/main.css"/>

	<!-- t1t2v4 0518 -->
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/new-op.css"/>
	<!-- 0258 -->
	<script type="text/javascript" src="${ctxStatic}/js/validationRules.js"></script>

	<script type="text/javascript">
		var saveOldGroupflag = true;
		var saveNewGroupflag = true;
		var modifycount = 0;
		function  changedNumber(){
			modifycount ++;
		}
//        var saveGroupCodes = '';
        var saveGroupIds = '';
        function changedSaveGroupIds(groupId){
            saveGroupIds += groupId + ",";
        }
		/*
		 *4 t1t2 打通：
		 *散拼产品发布  点击下一步是时获取成人、儿童、特殊人群的 价格策略。
		 *在方法  function oneToSecond()中  为
		 *成人、儿童、特殊人群的 价格策略 赋值
		 */
		var adultQuauqPriceStrategy; //成人价策略      结构'1:200,2:10#2:20'
		var childrenQuauqPriceStrategy;//儿童价策略     结构'1:100,2:10'
		var spicalQuauqPriceStratety;//特殊人群价策略     结构'2:200,3:15'


		var ctxStatic = '${ctxStatic}';
		var activityKind = '${travelActivity.activityKind}';

		//  对应需求  223
		var isneedCruiseGroupControl = '${isneedCruiseGroupControl}';

		//  对应需求  c460
		var groupCodeRuleDT = '${groupCodeRuleDT}';
		
		var requiredStraightPrice = '${requiredStraightPrice}';
		operateHandler();

		var delGroupIds ="";
		var delOtherFileIds = "";
		var validator1;
		var errorArray = new Array();
		$(function(){
			$("div[name='more-price']").hover(function() {
				$(this).children(".mp-floating").show();
			},function(){
				$(this).children(".mp-floating").hide();
			});
			//518 鼠标hover表格时上架下架标志变换 S
			$(".g-w").on("mouseover mouseleave",function(event){
				if(event.type=="mouseover"){
					$(this).css("background-image","none").prev().show();
				}else if(event.type=="mouseleave"){
					$(this).css("background-image","url(${ctxStatic}/images/g-w.png)").prev().hide();
				}
			});
		});
		function downloads(docid){
			window.open("${ctx}/sys/docinfo/download/"+docid);
		}

		function preview(docid,docName){

			if(docName != null) {
				var arr = docName.split(".");
				if(arr.length >= 2) {
					if(arr[arr.length - 1] != "jpg" && arr[arr.length - 1] != "png" && arr[arr.length - 1] != "pdf" && arr[arr.length - 1] != "doc" && arr[arr.length - 1] != "docx" && arr[arr.length - 1] != "excel") {
						alert("此类型文件不支持预览功能！");
					}else{
						window.location.href = "${ctx}/activity/preview/view?docid="+docid;
					}
				}
			}
		}

		function addfile(obj){

			var file = "<div class=\"activityMod_product\" style=\"margin-top:8px;\">"+$("#othertemplate").clone().html()+"</div>";
			$("#thirdStepContent").append(file);
		}
		function oneToggle(){
			$("#oneStepTitle").find("span:eq(0)").toggle(
					function(){
						$(this).removeClass("ydExpand").addClass("ydClose");
						$("#oneStepContent").hide();
						$("#oneModBtn").hide();
					},
					function(){
						$(this).removeClass("ydClose").addClass("ydExpand");
						$("#oneStepContent").show();
						$("#oneModBtn").show();
					}
			);
		}

		function secondToggle(){
			$("#secondStepTitle").find("span:eq(0)").toggle(
					function(){
						$(this).removeClass("ydExpand").addClass("ydClose");
						$("#secondStepContent").hide();
						$("#secondModBtn").hide();
					},
					function(){
						$(this).removeClass("ydClose").addClass("ydExpand");
						$("#secondStepContent").show();
						$("#secondModBtn").show();
					}
			);
		}

		function expandRemark(obj){
			var columnsNum = 0;
			$("#modTable thead tr:eq(0)").find("th").each(function(){
				if($(this).text().trim() != '团号'){
					if($(this).attr('colspan')){
						columnsNum = columnsNum + parseInt($(this).attr('colspan'));
					}else{
						columnsNum = columnsNum + 1;
					}
				}else{
					columnsNum = columnsNum+1;
				}
			});
			columnsNum = columnsNum -1;
			var remarkValue = $(obj).parent().parent().next().next().find("input").val();
			$(obj).parent().parent().next().find("span").text(remarkValue);
			$(obj).parent().parent().next().find("a[name=cancelRemark]").removeClass("displayClick");
			$(obj).parent().parent().next().find("input").addClass("disabledClass");
			$(obj).parent().parent().next().find("td:eq(0)").attr("colspan",columnsNum);
			$(obj).parent().parent().next().show();
		}

		//c463
		function expandRemark4Add(obj){
			//debugger;
			var remarkValue = $(obj).parents("tr:first").next().find("input").val();
			var activityKind = "${travelActivity.activityKind}";
			var groupPriceFlag = $("#groupPriceFlag").val();
			var str = '<tr ><td ';
			if(activityKind==2){//散拼
				/*****************/
				if("${shiroFlag}"==0){
					if("${fns:getUser().company.uuid}"=='7a81c5d777a811e5bc1e000c29cf2586'){//优加
						if(groupPriceFlag=='true'){
							str += ' colspan="20">';
						}else{
							str += ' colspan="19">';
						}
					}else if("${fns:getUser().company.uuid}"=='75895555346a4db9a96ba9237eae96a5'){//奢华之旅
						if(groupPriceFlag=='true'){
							str += ' colspan="19">';
						}else{
							str += ' colspan="18">';
						}
					}else{
						if(groupPriceFlag=='true'){
							if("${fns:getUser().company.uuid}"=='f5c8969ee6b845bcbeb5c2b40bac3a23'){//懿洋假期
								str += ' colspan="20">';
							}else{
								str += ' colspan="19">';
							}
						}else{
							if("${fns:getUser().company.uuid}"=='f5c8969ee6b845bcbeb5c2b40bac3a23'){//懿洋假期
								str += ' colspan="19">';
							}else if("${fns:getUser().company.uuid}"=='7a8175bc77a811e5bc1e000c29cf2586'){//新行者
								str += ' colspan="17">';
							}else if("${fns:getUser().company.uuid}"=='7a81a26b77a811e5bc1e000c29cf2586'){//拉美途
								str += ' colspan="18">';
							}else if("${fns:getUser().company.uuid}"=='7a8177e377a811e5bc1e000c29cf2586'){//青岛凯撒
								str += ' colspan="17">';
							}else if("${fns:getUser().company.uuid}"=='58a27feeab3944378b266aff05b627d2'){//日信观光
								str += ' colspan="17">';
							}else if("${fns:getUser().company.uuid}"=='5c05dfc65cd24c239cd1528e03965021'){//起航假期
								str += ' colspan="18">';
							}else if("${fns:getUser().company.uuid}"=='980e4c74b7684136afd89df7f89b2bee'){//骡子假期
								str += ' colspan="18">';
							}else{
								str += ' colspan="18">';
							}
						}
					}
				}else{
					if("${fns:getUser().company.uuid}"=='7a81c5d777a811e5bc1e000c29cf2586' || "${fns:getUser().company.uuid}"=='75895555346a4db9a96ba9237eae96a5'){
						if(groupPriceFlag=='true'){
							str += ' colspan="21">';
						}else{
							str += ' colspan="20">';
						}
					}else{
						if(groupPriceFlag=='true'){
							if("${fns:getUser().company.uuid}"=='f5c8969ee6b845bcbeb5c2b40bac3a23'){//懿洋假期
								str += ' colspan="21">';
							}else{
								str += ' colspan="20">';
							}
						}else{
							if("${fns:getUser().company.uuid}"=='f5c8969ee6b845bcbeb5c2b40bac3a23'){//懿洋假期
								str += ' colspan="20">';
							}else{
								str += ' colspan="19">';
							}
						}
					}
				}
				/****************/
			}else if(activityKind==10){//游轮
				if(groupPriceFlag=='true'){
					if("${fns:getUser().company.uuid}"=='f5c8969ee6b845bcbeb5c2b40bac3a23'){//懿洋假期
						str += ' colspan="17">';
					}else{
						str += ' colspan="16">';
					}
				}else{
					if("${fns:getUser().company.uuid}"=='f5c8969ee6b845bcbeb5c2b40bac3a23'){//懿洋假期
						str += ' colspan="16">';
					}else{
						str += ' colspan="15">';
					}
				}
			}else{
				if(groupPriceFlag=='true'){
					if("${fns:getUser().company.uuid}"=='f5c8969ee6b845bcbeb5c2b40bac3a23'){//懿洋假期
						str += ' colspan="16">';
					}else{
						str += ' colspan="15">';
					}
				}else{
					if("${fns:getUser().company.uuid}"=='f5c8969ee6b845bcbeb5c2b40bac3a23'){//懿洋假期
						str += ' colspan="15">';
					}else{
						str += ' colspan="14">';
					}
				}
			}
			str += '<div class="remarks-containers">备注:<input disabled="disabled" type="text" name="groupNotes" class="groupNotes disabledClass" value="'+remarkValue+'"></div></td>';
			str += '<td class="tc"><a href="javascript:void(0)" class="unSaveNotes01" name="cancelRemark">返回</a></td></tr>';

			if($(obj).parents("tr:first").next().next().find("a").text()!="返回"){
				$(obj).parents("tr:first").next().after(str);
				$(obj).parents("tr:first").next().next().find("a[name=cancelRemark]").removeClass("displayClick");
			}else{
				$(obj).parents("tr:first").next().next().find("a[name=cancelRemark]").removeClass("displayClick");
				$(obj).parents("tr:first").next().next().find("span").text(remarkValue);
				$(obj).parents("tr:first").next().next().show();
			}
		}

		$(document).ready(function() {
			var columnCount = null;
			/*c463为团期添加备注--start*/
			$(document).on('click', '.groupNote', function () {//备注
				debugger;
				var kind = $("#activityKind").val();
				if(2==kind){//散拼前三个都要合并
					$(this).parents('tr:first').children().eq(0).attr("rowspan","2");
					$(this).parents('tr:first').children().eq(1).attr("rowspan","2");
					if("${fns:getUser().company.uuid}"!='7a81a03577a811e5bc1e000c29cf2586'
							&& "${fns:getUser().company.uuid}"!='7a8175bc77a811e5bc1e000c29cf2586'
							&& "${fns:getUser().company.uuid}"!='7a8177e377a811e5bc1e000c29cf2586'
							&& "${fns:getUser().company.uuid}"!='7a81a26b77a811e5bc1e000c29cf2586'
							&& "${fns:getUser().company.uuid}"!='58a27feeab3944378b266aff05b627d2'
							&& "${fns:getUser().company.uuid}"!='5c05dfc65cd24c239cd1528e03965021'
							&& "${fns:getUser().company.uuid}"!='980e4c74b7684136afd89df7f89b2bee'
					){//大洋、新行者、青岛凯撒、拉美途 、日信观光  、起航假期、骡子假期  2个
						$(this).parents('tr:first').children().eq(2).attr("rowspan","2");
						if(columnCount == null){
							columnCount =$(this).parents('tr:first').next().children('td').eq(0).attr('colspan');

						}
						$(this).parents('tr:first').next().children('td').eq(0).attr("colspan",parseInt(columnCount)-1);
						if("${fns:getUser().company.uuid}"=='7a81c5d777a811e5bc1e000c29cf2586'){//优加国际 4个
							$(this).parents('tr:first').children().eq(3).attr("rowspan","2");
							$(this).parents('tr:first').next().children('td').eq(0).attr("colspan",parseInt(columnCount)-2);
						}
					}

				}else{
					$(this).parents('tr:first').children().eq(0).attr("rowspan","2");
					$(this).parents('tr:first').children().eq(1).attr("rowspan","2");
				}

				$(this).parents('tr:first').next().show();
			});
			//取消
			$(document).on('click', '.unSaveNotes', function () {
				var kind = $("#activityKind").val();
				if(2==kind){//散拼前两个都要合并
					$(this).parents('tr:first').prev().children().eq(0).attr("rowspan","1");
					$(this).parents('tr:first').prev().children().eq(1).attr("rowspan","1");
					if("${fns:getUser().company.uuid}"!='7a81a03577a811e5bc1e000c29cf2586'
							&& "${fns:getUser().company.uuid}"!='7a8175bc77a811e5bc1e000c29cf2586'
							&& "${fns:getUser().company.uuid}"!='7a8177e377a811e5bc1e000c29cf2586'
							&& "${fns:getUser().company.uuid}"!='7a81a26b77a811e5bc1e000c29cf2586'
							&& "${fns:getUser().company.uuid}"!='58a27feeab3944378b266aff05b627d2'
							&& "${fns:getUser().company.uuid}"!='5c05dfc65cd24c239cd1528e03965021'
							&& "${fns:getUser().company.uuid}"!='980e4c74b7684136afd89df7f89b2bee'
					){//大洋、新行者、青岛凯撒、拉美途、日信观光、起航假期、骡子假期    2个
						$(this).parents('tr:first').prev().children().eq(2).attr("rowspan","1");
						var columnCount =$(this).parents('tr:first').children('td').eq(0).attr('colspan');
						$(this).parents('tr:first').children('td').eq(0).attr("colspan",parseInt(columnCount)+1);
						if("${fns:getUser().company.uuid}"=='7a81c5d777a811e5bc1e000c29cf2586'){//优加国际 4个
							$(this).parents('tr:first').prev().children().eq(3).attr("rowspan","1");
							$(this).parents('tr:first').children('td').eq(0).attr("colspan",parseInt(columnCount)+2);
						}
					}

				}else{
					$(this).parents('tr:first').prev().children().eq(0).attr("rowspan","1");
					$(this).parents('tr:first').prev().children().eq(1).attr("rowspan","1");
				}

				$(this).parents('tr:first').find("input").val("");
				$(this).parents('tr:first').hide();
				$(this).parent().parent().prev().find(".groupNote").children().remove();
			});
			//保存
			$(document).on('click', '.saveNotes', function () {
				var kind = $("#activityKind").val();
				if(2==kind){//散拼前两个都要合并
					$(this).parents('tr:first').prev().children().eq(0).attr("rowspan","1");
					$(this).parents('tr:first').prev().children().eq(1).attr("rowspan","1");
					if("${fns:getUser().company.uuid}"!='7a81a03577a811e5bc1e000c29cf2586'
							&& "${fns:getUser().company.uuid}"!='7a8175bc77a811e5bc1e000c29cf2586'
							&& "${fns:getUser().company.uuid}"!='7a8177e377a811e5bc1e000c29cf2586'
							&& "${fns:getUser().company.uuid}"!='7a81a26b77a811e5bc1e000c29cf2586'
							&& "${fns:getUser().company.uuid}"!='58a27feeab3944378b266aff05b627d2'
							&& "${fns:getUser().company.uuid}"!='5c05dfc65cd24c239cd1528e03965021'
							&& "${fns:getUser().company.uuid}"!='980e4c74b7684136afd89df7f89b2bee'
					){//大洋、新行者、青岛凯撒、拉美途、日信观光、起航假期、骡子假期     2个
						$(this).parents('tr:first').prev().children().eq(2).attr("rowspan","1");
						var columnCount =$(this).parents('tr:first').children('td').eq(0).attr('colspan');
						$(this).parents('tr:first').children('td').eq(0).attr("colspan",parseInt(columnCount)+1);
						if("${fns:getUser().company.uuid}"=='7a81c5d777a811e5bc1e000c29cf2586'){//优加国际 4个
							$(this).parents('tr:first').prev().children().eq(3).attr("rowspan","1");
							$(this).parents('tr:first').children('td').eq(0).attr("colspan",parseInt(columnCount)+2);
						}
					}

				}else{
					$(this).parents('tr:first').prev().children().eq(0).attr("rowspan","1");
					$(this).parents('tr:first').prev().children().eq(1).attr("rowspan","1");
				}

				$(this).parents('tr:first').hide();
				$(this).parent().parent().prev().find(".groupNote").children().remove();
				if($(this).parents('tr:first').find("input").val().trim() != ""){
					$(this).parent().parent().prev().find(".groupNote").append('<em class="groupNoteTipImg"></em>');
				}
			});

			var orderType = $("#activityKind").val();
			var tuijian = $("#tuijian").val();
			var hasTuijian = false;
			if(orderType == 2 & tuijian == 'true'){
				hasTuijian = true;
			}
			$(".selectinput" ).comboboxInquiry();
			$("#fromArea").comboboxInquiry();
			$("#backArea").comboboxInquiry();
			$(".custom-combobox-input").css("width","90px");

			//前端js
			//文本框内的提示信息
			inputTips();
			//币种选择
			selectCurrency();
			transportSelect();

			$(".transport_city").delegate(".transportDel","click",function(){
				$(this).parent().remove();
			});
			var datepicker= $(".groupDate").datepickerRefactor({dateFormat: "yy-mm-dd",
						target:"#dateList",
						dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
						closeText:"关闭",
						prevText:"前一月",
						nextText:"后一月",
						monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"],
// 			    												minDate:getCurDate(),
						minDate:'2015-01-01',
						maxDate:'2020-12-31',
						numberOfMonths:3,
						closeBeforeDays:"#closeBeforeDays",
						visaBeforeDays:"#visaBeforeDays",
						visaCountryCopy:"#visaCountryCopy",
						visaCopyBtn:"#visaCopyBtn",
						planPositionCopy:"#planPositionCopy",
						planPositionBtn:"#planPositionBtn",
						freePositionCopy:"#freePositionCopy",
						freePositionBtn:"#freePositionBtn",
						invoiceTaxCopy:"#invoiceTaxCopy",
						invoiceTaxBtn:"#invoiceTaxBtn",
						secondStepContent:"#secondStepContent"
					},
					"#groupOpenDate","#groupCloseDate","#groupTable");

			jQuery.extend(jQuery.validator.messages, {
				required: "必填信息",
				digits:"请输入正确的数字",
				number : "请输入正确的数字价格"
			});
			validator1 = $("#modForm").validate({
				rules:{
					activitySerNum:{
						remote: {
							type: "POST",
							url: "${ctx}/activity/manager/serNumRepeat?proId="+$("#proId").val()
						}
					}
				},
				messages:{
					activitySerNum:{remote:"编号已存在"}
				},
				errorPlacement: function(error, element) {
					if($(element).attr("id")=="introduction")
						error.appendTo (element.parent().parent());
					else if($(element).hasClass("spinner_day"))
						error.appendTo (element.parent().parent());
					else if ( element.is(":radio") )
						error.appendTo ( element.parent() );
					else if ( element.is(":checkbox") )
						error.appendTo ( element.parent() );
					else if ( element.is("input") )
						error.appendTo ( element.parent() );
					else if($(element).attr("id")=="fromArea" || $(element).attr("id")=="backArea")
						error.appendTo ( element.parent() );
					else
						error.insertAfter(element);
				},
				showErrors:function(errorMap,errorList){
					this.defaultShowErrors();
					errorArray = errorArray.concat(errorList);
				},
				ignore:":hidden[id!='fromArea'][id!='backArea']"
			});

			validator2 = $("#modForm").validate({
				rules:{
					activitySerNum:{
						remote: {
							type: "POST",
							url: "${ctx}/activity/manager/serNumRepeat?proId="+$("#proId").val()
						}
					}
				},
				messages:{
					activitySerNum:{remote:"编号已存在"}
				},
				errorPlacement: function(error, element) {
					if($(element).attr("id")=="introduction")
						error.appendTo (element.parent().parent());
					else if($(element).hasClass("spinner_day"))
						error.appendTo (element.parent().parent());
					else if ( element.is(":radio") )
						error.appendTo ( element.parent() );
					else if ( element.is(":checkbox") )
						error.appendTo ( element.parent() );
					else if ( element.is("input") )
						error.appendTo ( element.parent() );
					else if($(element).attr("id")=="fromArea" || $(element).attr("id")=="backArea")
						error.appendTo ( element.parent() );
					else
						error.insertAfter(element);
				},
				showErrors:function(errorMap,errorList){
					this.defaultShowErrors();
					errorArray = errorArray.concat(errorList);
				},
				ignore:":hidden[id!='fromArea'][id!='backArea']"
			});

			$("#fromArea" ).comboboxInquiry({
				"afterInvalid":function(event,data){
					$(this).trigger("click");
				}
			});
			$("#backArea" ).comboboxInquiry({
				"afterInvalid":function(event,data){
					$(this).trigger("click");
				}
			});


			$("#thirdStepContent .mod_information_d8_2 select[name='country']" ).combobox();

			var ids = "";
			var names = "";
			var disabledates = "";
			<c:forEach var="data" items="${travelActivity.targetAreaList}" varStatus="d">
			ids = ids + "${data.id}"+",";
			names = names +"${data.name}"+",";
			</c:forEach>
			<c:forEach var="group" items="${travelActivity.activityGroups}" varStatus="d">
			//disabledates = disabledates + $(document).formatCSTDate("${group.groupOpenDate}",{format:"yyyy-MM-dd"})+",";
			disabledates += "<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/>"+",";
			</c:forEach>
			$(document).datepickerRefactor.initdisable(disabledates);

			$("#targetAreaId").val(ids.toString().substring(0,ids.length-1));
			$("#targetAreaName").val(names.toString().substring(0,names.length-1));


			//可见用户
			var userIds = "";
			var userNames = "";
			<c:forEach var="data" items="${travelActivity.opUserList}" varStatus="d">
			userIds = userIds + "${data.id}"+",";
			userNames = userNames +"${data.name}"+",";
			</c:forEach>
			$("#opUserId").val(userIds.toString().substring(0,userIds.length-1));
			$("#opUserName").val(userNames.toString().substring(0,userNames.length-1));

			getAcitivityNameLength();
			//产品名称输入长度
			$("#acitivityName").live("keyup",function(){
				getAcitivityNameLength();
			});
			$("#acitivityName").live("blur",function(){
				getAcitivityNameLength();
			});
			function getAcitivityNameLength() {
				var acitivityNameLength = 50-($("#acitivityName").val().length);
				if(acitivityNameLength>=0){
					$(".acitivityNameSize").text(acitivityNameLength);
				}
			}
			$( ".spinner_day" ).spinner({
				spin: function( event, ui ) {
					if ( ui.value > 365 ) {
						$( this ).spinner( "value", 1 );
						return false;
					} else if ( ui.value <= 0 ) {
						$( this ).spinner( "value", 365 );
						return false;
					}
				}
			});

			$( ".spinner" ).spinner({
				spin: function( event, ui ) {
					if ( ui.value > 365 ) {
						$( this ).spinner( "value", 0 );
						return false;
					} else if ( ui.value < 0 ) {
						$( this ).spinner( "value", 365 );
						return false;
					}
				}
			});
			$( ".spinner_hour" ).spinner({
				spin: function( event, ui ) {
					if ( ui.value > 23 ) {
						$( this ).spinner( "value", 0 );
						return false;
					} else if ( ui.value < 0 ) {
						$( this ).spinner( "value", 23 );
						return false;
					}
				}
			});
			$( ".spinner_fen" ).spinner({
				spin: function( event, ui ) {
					if ( ui.value > 59 ) {
						$( this ).spinner( "value", 0 );
						return false;
					} else if ( ui.value < 0 ) {
						$( this ).spinner( "value", 59 );
						return false;
					}
				}
			});
			//步骤1
			$(".divDurationDays").children("span").removeClass();
			$(".divRemainDays").children("span").removeClass();
			$(".acitivityNameSizeSpan").hide();
			$("#oneSaveBtn").hide();
			$("#oneStepContent").disableContainer({blankText : "",tipTarget:["targetAreaName"]});
			$("#oneStepContent").disableContainer({blankText : "",tipTarget:["opUserName"]});
			disabledcheckbox();

			$("input[name='payMode']").each(function(){
				if($(this).prop("checked")){
					var str = $(this).attr("id");
					if(str!='payMode_full' && str!='payMode_op'){
						$(this).next().next().find("span.xing").show();
						//$(this).next().next().next().find("input").rules("remove");
						//$(this).next().next().next().find("input").rules("add",{digits:true,nonzero:true,messages:{required:"必填信息",digits:"请输入正确的数字",nonzero:"输入不能为0"}});
					}
				}

			});

			oneToggle();
			//步骤2
			//$("#secondModBtn").show();
			$("#secondSaveBtn").hide();
			$("#secondStepAdd").hide();
			$("#secondStepEnd").disableContainer({blankText : ""});
			$("#secondStepMod").disableContainer({blankText : ""});
			//$("#addNewGroup").undisableContainer()
			//$("#secondStepEnd").undisableContainer();
			$("#contentTable thead span").hide();



			//debugger;  处理币种符号篡位的问题
			var currencyTypeArrStr = "${currencyTypeArrStr}";
			var currencyTypeArrStrArray= currencyTypeArrStr.split('###');

			$("#modTable tbody").children().children().find("input[name=settlementAdultPrice]").parent().find("span").each(function(index,obj){//if($(obj).text()!=null&&$(obj).text()!="")
				$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[0]+"</span>");});
			$("#modTable tbody").children().children().find("input[name=settlementcChildPrice]").parent().find("span").each(function(index,obj){//if($(obj).text()!=null&&$(obj).text()!="")
				$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[1]+"</span>");});
			$("#modTable tbody").children().children().find("input[name=settlementSpecialPrice]").parent().find("span").each(function(index,obj){//if($(obj).text()!=null&&$(obj).text()!="")
				$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[2]+"</span>");});




			if(${travelActivity.activityKind} == 2) {

				$("#modTable tbody").children().find("input[name=suggestAdultPrice]").parent().find("span").each(function(index,obj){
					//if($(obj).text()!=null&&$(obj).text()!="")
					$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[3]+"</span>");});
				$("#modTable tbody").children().find("input[name=suggestChildPrice]").parent().find("span").each(function(index,obj){
					//if($(obj).text()!=null&&$(obj).text()!="")
					$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[4]+"</span>");});
				$("#modTable tbody").children().find("input[name=suggestSpecialPrice]").parent().find("span").each(function(index,obj){
					//if($(obj).text()!=null&&$(obj).text()!="")
					$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[5]+"</span>");});

				// t1t2   打通   处理 quauq 的币种符号
				$("#modTable tbody").children().children().find("input[name=quauqAdultPrice]").parent().find("span").each(function(index,obj){//if($(obj).text()!=null&&$(obj).text()!="")
					$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[0]+"</span>");});
				$("#modTable tbody").children().children().find("input[name=quauqChildPrice]").parent().find("span").each(function(index,obj){//if($(obj).text()!=null&&$(obj).text()!="")
					$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[1]+"</span>");});
				$("#modTable tbody").children().children().find("input[name=quauqSpecialPrice]").parent().find("span").each(function(index,obj){//if($(obj).text()!=null&&$(obj).text()!="")
					$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[2]+"</span>");});
				$("#modTable tbody").children().children().find("input[name=supplyAdultPrice]").parent().find("span").each(function(index,obj){//if($(obj).text()!=null&&$(obj).text()!="")
					$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[0]+"</span>");});
				$("#modTable tbody").children().children().find("input[name=supplyChildPrice]").parent().find("span").each(function(index,obj){//if($(obj).text()!=null&&$(obj).text()!="")
					$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[1]+"</span>");});
				$("#modTable tbody").children().children().find("input[name=supplySpecialPrice]").parent().find("span").each(function(index,obj){//if($(obj).text()!=null&&$(obj).text()!="")
					$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[2]+"</span>");});


				//这是特殊人群数maxPeopleCountDefine，不应该添加红色样式，也不需要添加币种符号

				/*
				 *t1t2    打通   后 定金  和 单房差  的币种 取 团期  currency_type 后两段值
				 */
				// debugger;
				var currencyTypeCount = currencyTypeArrStrArray[index].split(',').length;
				$("#modTable tbody").children().find("input[name=payDeposit]").parent().find("span").each(function(index,obj){
					if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[6]+"</span>");});
				$("#modTable tbody").children().find("input[name=singleDiff]").parent().find("span").each(function(index,obj){
					if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[7]+"</span>");});


				// 109优惠-----------------------------
				$("#modTable tbody").children().find("input[name=adultDiscountPrice]").parent().find("span").each(function(index,obj){
					if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[8]+"</span>");});
				$("#modTable tbody").children().find("input[name=childDiscountPrice]").parent().find("span").each(function(index,obj){
					if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[9]+"</span>");});

				$("#modTable tbody").children().find("input[name=specialDiscountPrice]").parent().find("span").each(function(index,obj){
					if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[10]+"</span>");});



			}else{
				if(${travelActivity.activityKind} == 10) {
					$("#modTable tbody").children().find("input[name=suggestAdultPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[2]+"</span>");});
					$("#modTable tbody").children().find("input[name=suggestChildPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[3]+"</span>");});
				}else{
					$("#modTable tbody").children().find("td:eq(8)").find("span").each(function(index,obj){
						if(!$(obj).parent().find("input").attr("name") =="maxChildrenCountDefine" || !$(obj).parent().find("input").attr("name") =="maxPeopleCountDefine")
							if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[3]+"</span>");
					});
				}

				//wxw  added 20160107  处理 币种符号串位问题
				if(${travelActivity.activityKind} == 10) { //游轮
					$("#modTable tbody").children().find("input[name=payDeposit]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[4]+"</span>");});
					$("#modTable tbody").children().find("input[name=singleDiff]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[5]+"</span>");});
				}else if(${travelActivity.activityKind} == 2){ //散拼
					$("#modTable tbody").children().find("input[name=payDeposit]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[6]+"</span>");});
					$("#modTable tbody").children().find("input[name=singleDiff]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[7]+"</span>");});
				}else{ //单团，游学，大客户，自由行
					$("#modTable tbody").children().find("input[name=payDeposit]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[3]+"</span>");});
					$("#modTable tbody").children().find("input[name=singleDiff]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span  >"+$(obj).text()+"</span>").prev("input").before("<span class='curType'>"+currencyTypeArrStrArray[index].split(',')[4]+"</span>");});
				}


			}

			$("#modTable tbody").children().find("p[name='targetHere']").children().remove();
			// 234需求，屏蔽上传出团通知
			//$("#modTable tbody").children().find("p[name='targetHere']").append('<a id="" name="uploadGroupFile" class="uploadGroupFileClass" onclick="uploadGroupFile(\'${ctx}\', this)" href="javascript:void(0)">上传出团通知</a>');
			//t1t2-v2增加平台上架到T1的功能
			if(${travelActivity.activityKind} == 2) {
				//判读是否有quauq成人价、quauq儿童价或者quauq特殊人群其中的一种
				$("#modTable tbody").children().find("p[name='targetHere'][priceFlag='Y']").append('<a id="" name="platform" class="uploadGroupFileClass" onclick="platform(\'${ctx}\',${travelActivity.id},${travelActivity.activityKind},this)" href="javascript:void(0)">平台上架</a>');
			}
			var groupPriceFlag = $("#groupPriceFlag").val();
			if (groupPriceFlag  == "true") {
				$("#modTable tbody [name=groupPriceTd]").each(function() {
					$(this)
					if ($(this).is(":hidden")) {
						$(this).prev().prev().prev().find("td[name='targetHere']").append('<span name=\"expandPricing\" show=\"true\">展开价格方案</span>');
					}
					else {
						$(this).prev().prev().prev().find("td[name='targetHere']").append('<span name=\"expandPricing\" show=\"true\">收起价格方案</span>');
					}
				});
			}

			<!-- C436 -->
//   		$("#modTable tbody").children().find("td[name='targetHere'][remarkFlag='Y']").append('&nbsp;&nbsp;<span id="remark_"'+ serialId + '  class="uploadGroupFileClass" onclick="expandRemark(this)" href="javascript:void(0)">备注<em class="groupNoteTipImg"></em></span>');
//   		$("#modTable tbody").children().find("td[name='targetHere'][remarkFlag='N']").append('&nbsp;&nbsp;<span id="remark_"'+ serialId + ' class="uploadGroupFileClass" onclick="expandRemark(this)" href="javascript:void(0)">备注</span>');
			/*C463备注超长处理 */
			$("#modTable").find("input[name='groupNotes']").each(function(index,obj){
				$(obj).parent("div:first").removeClass("remarks-containers");
			});


			// 对应需求号  223   点修改前查看
			if(${travelActivity.activityKind} == 10) {
				//alert(isneedCruiseGroupControl)
				if(isneedCruiseGroupControl == '1'){
					//opertion = opertion  + '<a class="a1" href="javascript:void(0)"  onclick="jbox_create_group_control_pop(this);"></a>';
					//opertion = opertion  + '<a class="a1" href="javascript:void(0)" onclick="jbox_view_group_control_pop(this);"></a>';
					//修改前只能查看
					//$("#modTable tbody").children().find("p[name='targetHere']").append('<a id="" class="uploadGroupFileClass" onclick="jbox_create_group_control_pop( this)" href="javascript:void(0)">生成团控表</a>');
					$("#modTable tbody").children().find("p[name='targetHere']").append('<a id="" class="uploadGroupFileClass" onclick="jbox_view_group_control_pop(this)" href="javascript:void(0)">查看关联团控表</a>');
				}
			}
			secondToggle();

			//步骤3
			$("#thirdStepTitle").find("span:eq(0)").toggle(
					function(){
						$(this).removeClass("ydExpand").addClass("ydClose");
						$("#thirdStepContent").hide();
					},
					function(){
						$(this).removeClass("ydClose").addClass("ydExpand");
						$("#thirdStepContent").show();
					}
			);
			$("input[name='product_level']").css("line-height","25px");
			$("input[name='product_type']").css("line-height","25px");
			//subCode(true,true);
			subCode(true,false);

			//机票产品信息
			<c:if test="${not empty travelActivity.activityAirTicket.id}">
			getAirTicketInfo("${travelActivity.activityAirTicket.productCode}","${ctx}");
			$(".linkAir").show();
			</c:if>
		});

		//判断产品编号和团号是否需要截取：超过10位的要截取
		function subCode(activitySerNum,groupCode) {
			if(activitySerNum) {
				//判断产品编号：超过20位的截取然后加冒号
				var activitySerNum = $("#activitySerNum").siblings("span").html();
				if(activitySerNum && activitySerNum.length >20) {
					activitySerNum_temp = activitySerNum.substring(0,20) + "......";
					$("#activitySerNum").siblings("span").html(activitySerNum_temp);
					$("#activitySerNum").siblings("span").wrapInner("<a href='###' style='text-decoration: none; color:inherit; cursor:default;' title='" + activitySerNum + "'></a>");
				}
			}
//		if(groupCode) {
//			//判断团期编号：超过10位的截取然后加冒号
//			var groupCodeArr = $("[name='groupCode']").siblings(".disabledshowspan");
//			$(groupCodeArr).each(function() {
//				var groupCode = $(this).html();
//				if(groupCode && groupCode.length >10) {
//					groupCode_temp = groupCode.substring(0,10) + "......";
//					$(this).html(groupCode_temp);
//					$(this).wrapInner("<a href='###' style='text-decoration: none; color:inherit; cursor:default;' title='" + groupCode + "'></a>");
//				}
//			});
//		}
		}


		function oneStepMod(){
			//525线路玩法
			var ids = [];
			var targetAreaIds = '${targetAreaId}';
			var touristLineId = '${touristLineId}';
			ids = targetAreaIds.split(',');
			$.ajax({
				type: "POST",
				async:false,
				url: "${ctx}/activity/manager/getMatchLine",
				data: {areaIds:JSON.stringify(ids)},
				success: function(data){
					var optStr = "";
					if (data.length > 0){
						for (var i = 0; i < data.length; i++){
							if(touristLineId == data[i].touristLineId)
								optStr += "<option value= '" + data[i].touristLineId + "' selected='selected'>" + data[i].touristLineName + "</option>";
							else {
								optStr += "<option value= '" + data[i].touristLineId + "'>" + data[i].touristLineName + "</option>";
							}
						}
						if(touristLineId == '0')
							optStr += "<option value='0' selected='selected'>其他</option>";
						else
							optStr += "<option value= '0'>其他</option>";
					}else {
						optStr += "<option value='0' selected='selected'>其他</option>";
					}
					$("#touristLineId").html(optStr);
				}
			});
			$("#oneStepTitle").find("span:eq(0)").removeClass("ydClose ydExpand");
			$("#oneStepContent").undisableContainer();
			$("#groupNoMark").attr('disabled','true');
			removedisabledcheckbox();
			$("#oneSaveBtn").show();
			$("#oneModBtn").hide();
			$(".acitivityNameSizeSpan").show();
			$(".divDurationDays").children("span").addClass("ui-spinner ui-widget ui-widget-content ui-corner-all");
			$(".divDurationDays").children("span:last").removeClass();
			$(".divRemainDays").children("span").addClass("ui-spinner ui-widget ui-widget-content ui-corner-all");
			$(".divRemainDays").children("span:last").removeClass();
			var trafficModeValue = $("#trafficMode").val();

			<%--		$("#outArea").find("[value='${travelActivity.outArea}']").attr("selected",true);				--%>
			if("${relevanceFlagId}".indexOf(trafficModeValue)<0) {
				$("#trafficName").hide();
				$("#trafficName option[value='']").attr("selected", true);
			}

		}
		// 判断，如果支付方式没有被选中，则清空等待时间
		function clearValue(){
			$("input[name='payMode']").each(function(){
				if(!$(this).prop("checked")){
					$(this).next().next().next().find("input[name^='remainDays']").val("");
				}
			});
		}

		// 产品基本信息修改提交保存
		function oneStepSave(){

			//----------  t1t2 校验产品是否有价格策略匹配到  beggin ------------
			//debugger;
			var activityKind = '${travelActivity.activityKind}';

			/*
			 *涉及表：agent_price_strategy，price_strategy
			 *
			 *对应需求号 0426 t1t2 打通
			 *产品发布前根据 出发城市，目的地（线路），旅游类型，产品类型，产品系列  校验  是否有可匹配的价格策略
			 *并根据策略计算出    一套  quauq 价，返回到页面：具体的匹配规则如下：
			 *1.var adultQuauqPriceStrategy; //成人价策略      结构'1:200,2:10'  加200 减 10，
			 *2.var childrenQuauqPriceStrategy;//儿童价策略     结构'1:100,2:10'  加100减10
			 *3.var spicalQuauqPriceStratety;//特殊人群价策略     结构'2:200,3:15'  减200，打15折
			 *4.1代表加，2代表减，3代表乘%
			 *
			 *
			 */
			if("2"==activityKind){

				//获取产品基本策略匹配参数
				var fromArea = $("#fromArea").val(); //出发城市
				var targetAreaId = $("#targetAreaId").val(); //目的地
				var travelTypeId = $("#travelTypeId").val(); //旅游类型
				var activityTypeId = $("#activityTypeId").val(); //产品类型
				var activityLevelId = $("#activityLevelId").val(); //产品系列

				var flag = false;
				$.ajax({
					type: "POST",
					async:false,
					url: "${ctx}/activity/manager/checkActivityPriceStrategy",
					data: {"fromArea":fromArea,"targetAreaId":targetAreaId,"travelTypeId":travelTypeId,"activityTypeId":activityTypeId,"activityLevelId":activityLevelId},
					success: function(data){
						//debugger;
						if(data.result=="0"){
							/*top.$.jBox.info("未匹配到相关渠道价格策略，请配置相关策略。", "警告",{
							 buttons: {"取消": "0", "新建策略": "1"},
							 submit:function(v,h,f){
							 if(v=='1'){
							 //window.location.href ="${ctx}/pricingStrategy/manager/addt";
							 window.open("${ctx}/pricingStrategy/manager/addt");
							 }else{
							 //alert("000");
							 }
							 }
							 });
							 flag = true;*/
						}else{
							//quauqPrice4Adult##2:12#2:12,3:2#2:13
							//quauqPrice4Child##2:13#2:1#2:14
							//quauqPrice4SpicalPerson##2:15
							adultQuauqPriceStrategy = data.quauqPrice4Adult; //成人价策略      结构'1:200,2:10#1:100,2:10'  加200 减 10，
							childrenQuauqPriceStrategy = data.quauqPrice4Child;//儿童价策略     结构'1:100,2:10'  加100减10
							spicalQuauqPriceStratety = data.quauqPrice4SpicalPerson;//特殊人群价策略     结构'2:200,3:15'  减200，打15折
						}
					}
				});
//			if(flag){
//				return;
//			}



			}

			//----------  t1t2 校验产品是否有价格策略匹配到  end ------------


			var priceInput = document.getElementsByName('intermodalGroupPrice');
			var priceInputAll = document.getElementsByName('intermodalAllPrice');
			for(var i = 0; i < priceInput.length; i++){
				if($.trim(priceInput[i].value) == ''){
					priceInput[i].value = 0;
				}
			}
			if($.trim(priceInputAll.value) == ''){
				priceInputAll.value = 0;
			}
			add_modproductinfo();


			var flag = validator1.form();
			if(flag){
				clearValue();
				$(".acitivityNameSizeSpan").hide();
				$("#oneStepTitle").find("span:eq(0)").addClass("ydExpand");

				//验证订金占位保留时限不能全部为空或为零
				//79需求设置订金占位为非必填项--2016/2/29-wenchao.lv--需求暂停暂时屏蔽
				/* if($("#payMode_deposit").prop("checked") && ($("#remainDays_deposit_hour").val()=="0" && $("#remainDays_deposit").val()=="0" && $("#remainDays_deposit_fen").val()=="0")) {
				 top.$.jBox.info("订金占位 保留时限不能全部为零!", "警告");
				 return;
				 } */
				//79需求设置订金占位为非必填项--2016/2/29-wenchao.lv
				if($("#payMode_deposit").prop("checked") && ($("#remainDays_deposit_hour").val()=="" ||$("#remainDays_deposit_hour").val()=="0") && ($("#remainDays_deposit").val()=="" ||$("#remainDays_deposit").val()=="0") && ($("#remainDays_deposit_fen").val()=="" ||$("#remainDays_deposit_fen").val()=="0")){
					top.$.jBox.info("订金占位 保留时限不能全部为空或零!", "警告");
					return;
				}
				//验证预占位时限不能全部为空或为零
				if($("#payMode_advance").prop("checked") && ($("#remainDays_advance").val()=="" ||$("#remainDays_advance").val()=="0") && ($("#remainDays_advance_hour").val()=="" ||$("#remainDays_advance_hour").val()=="0") && ($("#remainDays_advance_fen").val()=="" ||$("#remainDays_advance_fen").val()=="0")){
					top.$.jBox.info("预占位 保留时限不能全部为空或零!", "警告");
					return;
				}
				//验证资料占位时限不能全部为空或为零
				if($("#payMode_data").prop("checked") && ($("#remainDays_data").val()=="" ||$("#remainDays_data").val()=="0") && ($("#remainDays_data_hour").val()=="" ||$("#remainDays_data_hour").val()=="0") && ($("#remainDays_data_fen").val()=="" ||$("#remainDays_data_fen").val()=="0")){
					top.$.jBox.info("资料占位 保留时限不能全部为空或零!", "警告");
					return;
				}
				//验证担保占位时限不能全部为空或为零
				if($("#payMode_guarantee").prop("checked") && ($("#remainDays_guarantee").val()=="" ||$("#remainDays_guarantee").val()=="0") && ($("#remainDays_guarantee_hour").val()=="" ||$("#remainDays_guarantee_hour").val()=="0") && ($("#remainDays_guarantee_fen").val()=="" ||$("#remainDays_guarantee_fen").val()=="0")){
					top.$.jBox.info("担保占位 保留时限不能全部为空或零!", "警告");
					return;
				}
				//验证确认单占位时限不能全部为空或为零
				if($("#payMode_express").prop("checked") && ($("#remainDays_express").val()=="" ||$("#remainDays_express").val()=="0") && ($("#remainDays_express_hour").val()=="" ||$("#remainDays_express_hour").val()=="0") && ($("#remainDays_express_fen").val()=="" ||$("#remainDays_express_fen").val()=="0")){
					top.$.jBox.info("确认单占位 保留时限不能全部为空或零!", "警告");
					return;
				}
				var remainDays_deposit_hour = $("#remainDays_deposit_hour").val();
				var remainDays_deposit_fen = $("#remainDays_deposit_fen").val();

				var remainDays_advance_hour = $("#remainDays_advance_hour").val();
				var remainDays_advance_fen = $("#remainDays_advance_fen").val();

				var remainDays_data_hour = $("#remainDays_data_hour").val();
				var remainDays_data_fen = $("#remainDays_data_fen").val();

				var remainDays_guarantee_hour = $("#remainDays_guarantee_hour").val();
				var remainDays_guarantee_fen = $("#remainDays_guarantee_fen").val();

				var remainDays_express_hour = $("#remainDays_express_hour").val();
				var remainDays_express_fen = $("#remainDays_express_fen").val();

				if(remainDays_deposit_hour=="") remainDays_deposit_hour=0;
				if(remainDays_deposit_fen=="") remainDays_deposit_fen=0;
				if(remainDays_advance_hour=="") remainDays_advance_hour=0;
				if(remainDays_advance_fen=="") remainDays_advance_fen=0;
				if(remainDays_data_hour=="") remainDays_data_hour=0;
				if(remainDays_data_fen=="") remainDays_data_fen=0;
				if(remainDays_guarantee_hour=="") remainDays_guarantee_hour=0;
				if(remainDays_guarantee_fen=="") remainDays_guarantee_fen=0;
				if(remainDays_express_hour=="") remainDays_express_hour=0;
				if(remainDays_express_fen=="") remainDays_express_fen=0;

				if($("#payMode_deposit").prop("checked") && parseInt(remainDays_deposit_hour)>23){
					top.$.jBox.info("订金占位 保留小时数需为0~23的正整数!", "警告");
					return;
				}
				if($("#payMode_deposit").prop("checked") && parseInt(remainDays_deposit_fen)>59){
					top.$.jBox.info("订金占位 保留分钟数需为0~59的正整数!", "警告");
					return;
				}

				if($("#payMode_advance").prop("checked") && parseInt(remainDays_advance_hour)>23){
					top.$.jBox.info("预占位 保留小时数需为0~23的正整数!", "警告");
					return;
				}
				if($("#payMode_advance").prop("checked") && parseInt(remainDays_advance_fen)>59){
					top.$.jBox.info("预占位 保留分钟数需为0~59的正整数!", "警告");
					return;
				}

				if($("#payMode_data").prop("checked") && parseInt(remainDays_data_hour)>23){
					top.$.jBox.info("资料占位 保留小时数需为0~23的正整数!", "警告");
					return;
				}
				if($("#payMode_data").prop("checked") && parseInt(remainDays_data_fen)>59){
					top.$.jBox.info("资料占位 保留分钟数需为0~59的正整数!", "警告");
					return;
				}

				if($("#payMode_guarantee").prop("checked") && parseInt(remainDays_guarantee_hour)>23){
					top.$.jBox.info("担保占位 保留小时数需为0~23的正整数!", "警告");
					return;
				}
				if($("#payMode_guarantee").prop("checked") && parseInt(remainDays_guarantee_fen)>59){
					top.$.jBox.info("担保占位 保留分钟数需为0~59的正整数!", "警告");
					return;
				}

				if($("#payMode_express").prop("checked") && parseInt(remainDays_express_hour)>23){
					top.$.jBox.info("确认单占位 保留小时数需为0~23的正整数!", "警告");
					return;
				}
				if($("#payMode_express").prop("checked") && parseInt(remainDays_express_fen)>59){
					top.$.jBox.info("确认单占位 保留分钟数需为0~59的正整数!", "警告");
					return;
				}

				oneToggle();
				$(".divDurationDays").children("span").removeClass();
				$(".divRemainDays").children("span").removeClass();
				$("#oneStepContent").disableContainer({blankText : "",tipTarget:["targetAreaName"]});
				$("#oneStepContent").disableContainer({blankText : "",tipTarget:["opUserName"]});
				disabledcheckbox();
				$("#oneSaveBtn").hide();
				$("#oneModBtn").show();
				remove_modproductinfo();
				subCode(true,false);
				//如果之前关联了机票产品，但是后来又更改了，并且更改的方式不关联机票产品，则需要去对掉机票产品的关联。
				var value = $("#trafficMode option:selected").val();
				if($(".airInfo").find("div").length != 0 && !"${relevanceFlagId}".split(",").contains(value)) {
					$(".activityAirTicketId").val("");
					$(".airInfo-tit1").remove();
					$(".airInfo-con").remove();
				}
			}else{
				top.$.jBox.info("请先修改完错误再提交", "警告");
			}

		}


		function getMinDateNew(obj){

			return $(obj).parent().prev().prev().children("input[name='groupOpenDate']").val();
		}
		//产品基本信息-->添加团期和价格（隐藏并修改样式）
		function disabledcheckbox() {
			$("#payModeText").html("");
			$(":checkbox[name='payMode']").each(function(index, obj) {
				$(this).css("display","none");
				$(this).next("font").css("display","none");
				$("[id^=label]").css("display","none");
				$("span.payModeSpan").css("display","none");
				$(this).next().next().next().next().hide();
				$(this).next().next().next().next().next().hide();
				$("#payModeText").css("display","inline-block");
				if($(this).prop("checked")) {
					var payModeHtml = $("#payModeText").html();
					if(payModeHtml != "") {
						if($(this).val() != 3 && $(this).val() != 7 && $(this).val() != 8) {
							var remainDays = $("[name$=" + $(this).attr("id").split("_")[1] + "]").val();
							var remainHours = $("[name$=" + $(this).attr("id").split("_")[1] + "_hour]").val();
							var remainMin = $("[name$=" + $(this).attr("id").split("_")[1] + "_fen]").val();

							if(remainDays==""){
								remainDays=0;
							}
							if(remainHours==""){
								remainHours=0;
							}
							if(remainMin==""){
								remainMin=0;
							}

							if(remainDays == 0 && remainHours==0 && remainMin==0) {
								//79需求订金占位设置非必填项，當订金输入为空时默认保留永久不得自动删除须手动删除--2016/02/29--wenchao.lv
								//payModeHtml += ",&nbsp;&nbsp;" + $(this).next("font").text().replace(/(^\s*)|(\s*$)/g,'')+"(保留永久)";
								payModeHtml += ",&nbsp;&nbsp;" + $(this).next("font").text().replace(/(^\s*)|(\s*$)/g,'');
							} else {
								payModeHtml += ",&nbsp;&nbsp;" + $(this).next("font").text().replace(/(^\s*)|(\s*$)/g,'') + "（保留" +  remainDays + "天"+remainHours+"时"+remainMin+"分）";
							}
						} else {
							payModeHtml += ",&nbsp;&nbsp;" + $(this).next("font").text();
						}
					} else {
						if($(this).val() != 3 && $(this).val() != 7 && $(this).val() != 8) {
							var remainDays = $("[name$=" + $(this).attr("id").split("_")[1] + "]").val();
							var remainHours = $("[name$=" + $(this).attr("id").split("_")[1] + "_hour]").val();
							var remainMin = $("[name$=" + $(this).attr("id").split("_")[1] + "_fen]").val();
							if(remainDays==""){
								remainDays=0;
							}
							if(remainHours==""){
								remainHours=0;
							}
							if(remainMin==""){
								remainMin=0;
							}
							if(remainDays == 0 && remainHours==0 && remainMin==0) {
								//79需求订金占位设置非必填项，當订金输入为空时默认保留永久不得自动删除须手动删除--2016/02/29--wenchao.lv--需求暂停暂时屏蔽
								//payModeHtml = $(this).next("font").text().replace(/(^\s*)|(\s*$)/g,'')+"(保留永久)";
								payModeHtml = $(this).next("font").text().replace(/(^\s*)|(\s*$)/g,'');
							} else {
								payModeHtml = $(this).next("font").text().replace(/(^\s*)|(\s*$)/g,'') + "（保留" +  remainDays + "天"+remainHours+"时"+remainMin+"分）";
							}
						}else {
							payModeHtml = $(this).next("font").text().replace(/(^\s*)|(\s*$)/g,'');
						}
					}
					$("#payModeText").html(payModeHtml).show();
				}

			});

			//可见用户hover事件提示框效果
			$("label:contains('可见用户')").next('div').find("span").attr('title',$("label:contains('可见用户')").next('div').find("span").text());
			var temp = $("label:contains('可见用户')").next('div').find("span").text();
			var temparray = temp.split(',');
			if(temparray != null && temparray != ""){
				if(temparray.length==1){
					$("label:contains('可见用户')").next('div').find("span").text(temparray[0]);
				}else if(temparray.length==2){
					$("label:contains('可见用户')").next('div').find("span").text(temparray[0]+","+temparray[1]);
				}else{
					$("label:contains('可见用户')").next('div').find("span").text(temparray[0]+","+temparray[1]+",...");
				}
			}

		}
		function secondStepModValidate() {
			//secondStepSaveFlag = false;
			saveNewGroupflag = false;
			secondStepMod();
		}
		var secondStepSaveFlag = true;
		function secondStepMod(){
			var activityKind = '${travelActivity.activityKind}';
			/*C463备注超长时处理 */
			$("#modTable").find("input[name='groupNotes']").each(function(index,obj){
				$(obj).parent("div:first").addClass("remarks-containers");
			});

			$("#contentTable").find("input[name='groupRemark']").each(function(index,obj){
				$(obj).parent("div:first").addClass("remarks-containers");
			});
			$("#contentTable").find("input[name='groupNotes']").each(function(index,obj){
				$(obj).parent("div:first").addClass("remarks-containers");
			});

			//secondStepSaveFlag = false;
			$("#modTable tbody").children().find("p[name='targetHere']").children().remove();
			$("#secondModBtn").hide();
			$("#secondSaveBtn").show();
//		$("#secondStepTitle").find("span:eq(0)").removeClass("ydClose ydExpand");
			$("#secondStepAdd").show();
			$(".noteTr11").hide();
			//518添加新团期表头复制按钮 bug16317 S
			$("#addNewGroup").undisableContainer();
			$("#secondStepEnd").undisableContainer();
			$("#contentTable thead span").show();
			//518添加新团期表头复制按钮 bug16317 E
			var columnsNum = 0;
			$("#modTable thead tr:eq(0)").find("th").each(function(){
				if($(this).text().trim() != '团号'){
					if($(this).attr('colspan')){
						columnsNum = columnsNum + parseInt($(this).attr('colspan'));
					}else{
						columnsNum = columnsNum + 1;
					}
				}else{
					columnsNum = columnsNum+1;
				}
			});
			$(".noteTr").find("td:eq(0)").attr("colspan",columnsNum);
//1024			$(".noteTr").show();
//			C463新增団期列表备注
			$("#contentTable tbody").children().find("[name='freePosition']").each(function(index, obj){
				if($(obj).parent().parent().next().find("input").val().trim() != ""){
					$(obj).parent().parent().find(".groupNote").children().remove();
					$(obj).parent().parent().find(".groupNote").append('<em class="groupNoteTipImg"></em>');
				}
				if($(obj).parent().parent().next().next().find("a").text() == "返回"){
					$(obj).parent().parent().next().next().hide();
				}
				$(obj).parent().parent().next().hide();
			});

			//修复：新增的団期保存后，再次点击修改，操作项消失的问题
			$("#contentTable tbody").children().find("dl").each(function(index, obj){
				$(obj).show();
			});
			//bug16403 添加新团期时隐藏操作项外的价格方案和备注 S
            $("#contentTable #expandPricing").each(function(){
                $(this).parent().next().remove();
                $(this).parent().remove();
            });
			//bug16403 添加新团期时隐藏操作项外的价格方案和备注 E

			//显示团期价格操作按钮
			$("#modTable .addAndRemove").hide();
			$("#modTable .add-select").hide();
			$("#modTable .remove-selected").hide();
			$("#modForm .addAndRemove").hide();
			$("#modForm .add-select").hide();
			$("#modForm .remove-selected").hide();

		}

		function removedisabledcheckbox() {
			$(":checkbox[name='payMode']").each(function(index, obj) {
				$(this).css('display','inline-block');
				$(this).next("font").show();
				$("[id^=label]").show();
				$("span.payModeSpan").css('display','inline');
				$(this).next().next().next().next().show();
				$(this).next().next().next().next().next().show();
				if($(this).prop("checked")){
					$(this).next().next().find("span.xing").css("display","inline");
					$(this).next().next().next().find("input").removeAttr("disabled");
					$(this).next().next().next().next().find("input").removeAttr("disabled");
					$(this).next().next().next().next().next().find("input").removeAttr("disabled");
				}else{
					$(this).next().next().find("span.xing").css("display","none");
					$(this).next().next().next().find("input").attr("disabled","disabled");
					$(this).next().next().next().next().find("input").attr("disabled","disabled");
					$(this).next().next().next().next().next().find("input").attr("disabled","disabled");
					$(this).next().next().next().find("input").val("");
					$(this).next().next().next().next().find("input").val("");
					$(this).next().next().next().next().next().find("input").val("");
				}
			});
			$("#payModeText").css("display","none");
		}

		function secondStepSave(){
			var validatecount = 0;
			//0071-进来先清空提示span-防止bug-s
			$("[name='span4visaCountry']").empty();
			//0071-进来先清空提示span-防止bug-e
			/*C463备注超长处理 */
			$("#modTable").find("input[name='groupNotes']").each(function(index,obj){
				$(obj).parent("div:first").removeClass("remarks-containers");
			});
			$("#contentTable").find("input[name='groupRemark']").each(function(index,obj){
				$(obj).parent("div:first").removeClass("remarks-containers");
			});
			$("#contentTable").find("input[name='groupNotes']").each(function(index,obj){
				$(obj).parent("div:first").removeClass("remarks-containers");
			});

			var orderType = $("#activityKind").val();
			var tuijian = $("#tuijian").val();
			var hasTuijian = false;
			if(orderType == 2 & tuijian == 'true'){
				hasTuijian = true;
			}

			add_groupsvalidator();
			add_modgroupsvalidator();

			//**************0071-签证国家长度限定为50字-s**********
			var arrCurrentVisaCountries=$('#secondStepMod [name="visaCountry"]');//现有签证国家
			var arrAddVisaCountries=$('#groupTable [name="visaCountry"]');//新增签证国家
			var flag4VisaCountry=false;
			for(var i=0;i<arrCurrentVisaCountries.length;i++){
				if(arrCurrentVisaCountries[i].value.length>50){
					$("#secondStepMod #visaCountry"+(i+1)).after('<label name="span4visaCountry" style="display:block"  class="myerror">请输入一个长度最多是50的字符串</label>');
					flag4VisaCountry=true;
					validatecount++;
				}
			}
			for(var i=0;i<arrAddVisaCountries.length;i++){
				if(arrAddVisaCountries[i].value.length>50){
					$("#groupTable #visaCountry"+i).after('<label name="span4visaCountry" style="display:block"  class="myerror">请输入一个长度最多是50的字符串</label>');
					flag4VisaCountry=true;
					validatecount++;
				}
			}
			if(flag4VisaCountry==true){
				top.$.jBox.info("请先修改完错误再提交", "警告");
				top.$('.jbox-body .jbox-icon').css('top','55px');
				validatecount++;
				return validatecount;
			}
			//**************0071-签证国家长度限定为50字-e**********

			//**********************特殊人群数不能大于预收人数的校验-s*******//
			var flag4MaxPeopleCountPlanPosition=false;
			var flag4MaxChildrenCountPlanPosition=false;
			var flag4MaxCountPlanPosition=false;

			//当前占位
			var currentPositionNum = 0;
			var currentPositionPeopleNum = 0;
			var currentPositionChildrenFlag = false;
			var currentPositionPeopleFlag = false;
			var currentPositionElts = $("input[name='nopayPeoplePosition']");
			var currentChildrenPositionElts = $("input[name='nopayChildrenPosition']");
			var groupCodes = $("input[name='groupCode']");

			var maxPeopleCountElts=$("input[name='maxPeopleCount']");
			var maxChildrenCountElts=$("input[name='maxChildrenCount']");
			var planPositionElts=$("input[name='planPosition']");
			//t1t2-v4 518bug-15889出团日期不能早于截团日期
			var groupOpenDateElts = $("input[name='groupOpenDate']");
			var groupCloseDateElts = $("input[name='groupCloseDate']");
			var currentDateFlag = false;
			for(var i=0;i<maxChildrenCountElts.length;i++){
				if(groupOpenDateElts[i].value < groupCloseDateElts[i].value) {
					currentDateFlag = true;
					break;
				}
				if(maxChildrenCountElts[i].value != "" && parseInt(maxChildrenCountElts[i].value)>parseInt(planPositionElts[i].value)){ //特殊人群数大于预收人数,则校验标志置为真
					flag4MaxChildrenCountPlanPosition=true;
					break;//终止循环
				}
				if(currentChildrenPositionElts[i]&& currentChildrenPositionElts[i].value !="" && maxChildrenCountElts[i].value != "" && parseInt(maxChildrenCountElts[i].value)<parseInt(currentChildrenPositionElts[i].value)){ //特殊人群数大于预收人数,则校验标志置为真
					currentPositionChildrenFlag=true;
					currentPositionNum = currentChildrenPositionElts[i].value;
					break;//终止循环
				}
				if(maxChildrenCountElts[i].value != "" && maxPeopleCountElts[i].value != "" && (parseInt(maxChildrenCountElts[i].value)+parseInt(maxPeopleCountElts[i].value))>parseInt(planPositionElts[i].value)){ //特殊人群数大于预收人数,则校验标志置为真
					flag4MaxCountPlanPosition=true;
					break;//终止循环
				}
			}
			if(currentDateFlag){
				top.$.jBox.info("\"团号为"+groupCodes[i].value+"的出团日期不能早于截团日期\"", "警告");
				validatecount++;
				return validatecount;
			}
			if(flag4MaxChildrenCountPlanPosition){
				top.$.jBox.info("\"团号为"+groupCodes[i].value+"的儿童最高人数\"不能大于\"预收\"", "警告");
				validatecount++;
				return validatecount;
			}
			if(currentPositionChildrenFlag){
				validatecount++;
				top.$.jBox.info("\"团号为"+groupCodes[i].value+"的儿童最高人数\"不能小于\"已占位儿童人数"+currentPositionNum+"\"", "警告");
				return validatecount;
			}
			for(var i=0;i<maxPeopleCountElts.length;i++){
				if(maxPeopleCountElts[i].value != "" && parseInt(maxPeopleCountElts[i].value)>parseInt(planPositionElts[i].value)){ //特殊人群数大于预收人数,则校验标志置为真
					flag4MaxPeopleCountPlanPosition=true;
					break;//终止循环
				}
				if(maxPeopleCountElts[i].value != "" && currentPositionElts[i] && currentPositionElts[i].value != "" && currentPositionElts[i]&&parseInt(maxPeopleCountElts[i].value)<parseInt(currentPositionElts[i].value)){ //特殊人群数大于预收人数,则校验标志置为真
					currentPositionPeopleFlag=true;
					currentPositionPeopleNum = currentPositionElts[i].value;
					break;//终止循环
				}
				if(maxChildrenCountElts[i].value != "" && maxPeopleCountElts[i].value != ""  && (parseInt(maxChildrenCountElts[i].value)+parseInt(maxPeopleCountElts[i].value))>parseInt(planPositionElts[i].value)){ //特殊人群数大于预收人数,则校验标志置为真
					flag4MaxCountPlanPosition=true;
					break;//终止循环
				}
			}
			if(flag4MaxPeopleCountPlanPosition){
				validatecount++;
				top.$.jBox.info("\"团号为"+groupCodes[i].value+"的特殊人群最高人数\"不能大于\"预收\"", "警告");
				return validatecount;
			}
			if(currentPositionPeopleFlag){
				validatecount++;
				top.$.jBox.info("\"团号为"+groupCodes[i].value+"的特殊人群最高人数\"不能小于\"已占位特殊人群人数"+currentPositionPeopleNum+"\"", "警告");
				return validatecount;
			}

			if(flag4MaxCountPlanPosition){
				validatecount++;
				top.$.jBox.info("\"团号为"+groupCodes[i].value+"的儿童与特殊人群最高人数之和\"不能大于\"预收\"", "警告");
				return validatecount;
			}

			var keyVal= $("input[name^='groupCode']") ;


			var flag = validator1.form();
			if(flag) {
				$("#secondStepTitle").find("span:eq(0)").addClass("ydExpand");
				secondToggle();
				$("#secondStepEnd").disableContainer({blankText: ""});
//					$("#secondStepEnd").find("input[name=groupCode]").next().attr("style","word-break: break-all;display: block;word-wrap: break-word;");
//				$("#secondStepMod").disableContainer({blankText : ""});
				var serialId = $(obj).parent().parent().find("input[name='serial']").val();
				$(obj).parent().parent().parent().find("#tr_" + serialId).hide();

				$("#secondStepAdd").hide();
				$("#secondModBtn").show();
				$("#secondSaveBtn").hide();
//				$("#contentTable thead span").hide();
				if(hasTuijian){
					$("#modTable tbody").children().find("input[name=settlementAdultPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
					$("#modTable tbody").children().find("input[name=settlementcChildPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
					$("#modTable tbody").children().find("input[name=settlementSpecialPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
				}else{
					$("#modTable tbody").children().find("input[name=settlementAdultPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
					$("#modTable tbody").children().find("input[name=settlementcChildPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
					$("#modTable tbody").children().find("input[name=settlementSpecialPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
				}
				if(${travelActivity.activityKind} == 2) {
					if(hasTuijian){
						$("#modTable tbody").children().find("input[name=suggestAdultPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
						$("#modTable tbody").children().find("input[name=suggestChildPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
						$("#modTable tbody").children().find("input[name=suggestSpecialPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
						$("#modTable tbody").children().find("input[name=payDeposit]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
						$("#modTable tbody").children().find("input[name=singleDiff]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
					}else{
						$("#modTable tbody").children().find("input[name=suggestAdultPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
						$("#modTable tbody").children().find("input[name=suggestChildPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
						$("#modTable tbody").children().find("input[name=suggestSpecialPrice]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
						$("#modTable tbody").children().find("input[name=payDeposit]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
						$("#modTable tbody").children().find("input[name=singleDiff]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
					}
				}else{
					$("#modTable tbody").children().find("input[name=payDeposit]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
					$("#modTable tbody").children().find("input[name=singleDiff]").parent().find("span").each(function(index,obj){if($(obj).text()!=null&&$(obj).text()!="")$(obj).html("<span class='tdred'>"+$(obj).text()+"</span>");});
				}
				$("#contentTable tbody").children().find("td:eq(0)").attr("class","tc");
				$("#contentTable tbody").children().find("td:eq(1)").attr("class","tc");
				$("#contentTable tbody").children().find("td:eq(4)").attr("class","tc");

				$("#modTable tbody").children().find("p[name='targetHere']").children().remove();
				// 234需求，屏蔽上传出团通知
				//$("#modTable tbody").children().find("p[name='targetHere']").append('<a id="" name="uploadGroupFile" class="uploadGroupFileClass" onclick="uploadGroupFile(\'${ctx}\', this)" href="javascript:void(0)">上传出团通知</a>');
//	   		var groupPriceFlag = $("#groupPriceFlag").val();
//	    	if (groupPriceFlag  == "true") {
//	    		$("#modForm tbody [name=groupPriceTd]").each(function() {
//					if ($(this).is(":hidden")) {
//						$(this).prev().prev().prev().find("td[name='targetHere']").append('<a name=\"expandPricing\" show=\"true\">展开价格方案</a>');
//					} else {
//						$(this).prev().prev().prev().find("td[name='targetHere']").append('<a name=\"expandPricing\" show=\"true\">收起价格方案</a>');
//					}
//				});
//	    	}



				//C463现有団期列表备注
//	   		$("#modTable tbody tr[id^='groupRemark']").each(function(){
//	   			var $currNoteTr=$(this).find('input').val();
//	   			if($currNoteTr.trim()==""){
//	   				$(this).prev().prev().find("td[name='targetHere']").append('&nbsp;&nbsp;<a id="" name="uploadGroupFile" class="uploadGroupFileClass" onclick="expandRemark(this)" href="javascript:void(0)">备注</a>');
//	   			}else{
//	   				$(this).prev().prev().find("td[name='targetHere']").append('&nbsp;&nbsp;<a id="" name="uploadGroupFile" class="uploadGroupFileClass" onclick="expandRemark(this)" href="javascript:void(0)">备注<em class="groupNoteTipImg"></em></a>');
//	   			}
//	   			$(this).prev().hide();
//	   		})
				/*收起已有团期备注*/
				$(".noteTr").hide();

				//C463新增団期列表备注
				if('f5c8969ee6b845bcbeb5c2b40bac3a23'=='${fns:getUser().company.uuid}'){//懿洋假期
					var nameStr = 'invoiceTax';
				}else{
					var nameStr = 'freePosition';
				}
				$("#contentTable tbody").children().find("[name="+nameStr+"]").each(function(index, obj){
					//修复：新增的団期保存后，再次点击修改，操作项消失的问题
					//$(obj).parent().next().empty();
					$(obj).parent().next().children().hide();
					// 234需求，屏蔽上传出团通知
					//$(obj).parent().next().append('<p><a id="" name="uploadGroupFile" onclick="uploadGroupFile(\'${ctx}\', this)" href="javascript:void(0)">上传出团通知</a></p>');
					var groupPriceFlag = $("#groupPriceFlag").val();
					if (groupPriceFlag  == "true") {
						$(this)
						if ($(this).is(":hidden")) {
							$(obj).parent().next().append('<p style=\"margin-bottom:0\"><a id=\"expandPricing\" name=\"expandPricing\" show=\"true\">展开价格方案</a></p>');
						}
						else {
							$(obj).parent().next().append('<p style=\"margin-bottom:0\"><a id=\"expandPricing\" name=\"expandPricing\" show=\"true\">收起价格方案</a></p>');
						}
					}
					if($(obj).parent().parent().next().find("input").val().trim() != ""){
						$(obj).parent().next().append('<p style=\"margin-bottom:0\"><a class="uploadGroupFileClass" name="uploadGroupFile" onclick="expandRemark4Add(this)">备注<em class="groupNoteTipImg"></em></a></p>');
					}else{
						$(obj).parent().next().append('<p style=\"margin-bottom:0\"><a class="uploadGroupFileClass" name="uploadGroupFile" onclick="expandRemark4Add(this)">备注</a></p>');
					}

					//散拼前三个都要合并
					if(2==orderType){
						$(obj).parent().parent().children().eq(0).attr("rowspan","1");
						$(obj).parent().parent().children().eq(1).attr("rowspan","1");
						if("${fns:getUser().company.uuid}"!='7a81a03577a811e5bc1e000c29cf2586'
								&& "${fns:getUser().company.uuid}"!='7a8175bc77a811e5bc1e000c29cf2586'
								&& "${fns:getUser().company.uuid}"!='7a8177e377a811e5bc1e000c29cf2586'
								&& "${fns:getUser().company.uuid}"!='7a81a26b77a811e5bc1e000c29cf2586'
								&& "${fns:getUser().company.uuid}"!='58a27feeab3944378b266aff05b627d2'
								&& "${fns:getUser().company.uuid}"!='5c05dfc65cd24c239cd1528e03965021'
								&& "${fns:getUser().company.uuid}"!='980e4c74b7684136afd89df7f89b2bee'
						){//大洋、新行者、青岛凯撒、拉美途、日信观光、起航假期、骡子假期    2个
							$(obj).parent().parent().children().eq(2).attr("rowspan","1");
							if("${fns:getUser().company.uuid}"=='7a81c5d777a811e5bc1e000c29cf2586'){//优加国际 4个
								$(obj).parent().parent().children().eq(3).attr("rowspan","1");
							}
						}

					}else{
						$(obj).parent().parent().children().eq(0).attr("rowspan","1");
						$(obj).parent().parent().children().eq(1).attr("rowspan","1");
					}

					$(obj).parent().parent().next().hide();
				});


				// 对应需求号  223  点修改保存后  显示查看
				if(${travelActivity.activityKind} == 10) {
					//alert(isneedCruiseGroupControl)
					if(isneedCruiseGroupControl == '1'){
						$("#modTable tbody").children().find("p[name='targetHere']").append('' + '<a onClick="jbox_view_group_control_pop(this)" href="javascript:void(0)">查看关联团控表</a>' + '');
					}
				}


				$("#modTable tbody").children().find("[name='uploadGroupFile']").removeClass("displayClick");
				$("#modTable tbody").children().find("[name='uploadGroupFile']").show();

// 	   		for(var i = 5 ;i <= 12 ;i ++) {
// 	   			$("#contentTable tbody").children(":visible").find("td:eq("+i+")").attr("class","tr");
// 	   		}
				remove_groupsvalidator();
				remove_modgroupsvalidator();
				//subCode(false,true);
				subCode(false,false);

				//生成币种Id串，并记录在每个团期后面
				var len = $("#contentTable tbody").find("input").length;
				if(len!=0){
					$("#contentTable").children("tbody").find("tr[class*='-']").each(function(index,obj){
						var currencyIdStr = "";
						$(obj).find("input").each(function(index1,obj1) {
							if($(obj1).hasClass("ipt-currency")) {
								currencyIdStr += $(obj1).attr("var") + ",";
							}
						});
						currencyIdStr = currencyIdStr.substring(0,currencyIdStr.length-1);
						if($(obj).find("[name='groupCurrencyType']").length != 0) {
							$(obj).find("[name='groupCurrencyType']").remove();
						}
						$(obj).append("<input type='hidden' name='groupCurrencyType' value='"+currencyIdStr+"' />");
					});
				}

				//隐藏操作按钮
				$("#modTable .addAndRemove").hide();
				$("#modTable .add-select").hide();
				$("#modTable .remove-selected").hide();
				$("#modForm .addAndRemove").hide();
				$("#modForm .add-select").hide();
				$("#modForm .remove-selected").hide();
				$("#contentTable thead span").hide();
				secondStepSaveFlag = true;
			}else{
				//c463
				$("#contentTable tbody").children().find("[name='freePosition']").each(function(index, obj){
					//散拼前三个都要合并
					if(2==orderType){
						$(obj).parent().parent().children().eq(0).attr("rowspan","1");
						$(obj).parent().parent().children().eq(1).attr("rowspan","1");
						if("${fns:getUser().company.uuid}"!='7a81a03577a811e5bc1e000c29cf2586'
								&& "${fns:getUser().company.uuid}"!='7a8175bc77a811e5bc1e000c29cf2586'
								&& "${fns:getUser().company.uuid}"!='7a8177e377a811e5bc1e000c29cf2586'
								&& "${fns:getUser().company.uuid}"!='7a81a26b77a811e5bc1e000c29cf2586'
								&& "${fns:getUser().company.uuid}"!='58a27feeab3944378b266aff05b627d2'
								&& "${fns:getUser().company.uuid}"!='5c05dfc65cd24c239cd1528e03965021'
								&& "${fns:getUser().company.uuid}"!='980e4c74b7684136afd89df7f89b2bee'
						){//大洋、新行者、青岛凯撒、拉美途、日信观光、起航假期、骡子假期     2个
							$(obj).parent().parent().children().eq(2).attr("rowspan","1");
							if("${fns:getUser().company.uuid}"=='7a81c5d777a811e5bc1e000c29cf2586'){//优加国际 4个
								$(obj).parent().parent().children().eq(3).attr("rowspan","1");
							}
						}

					}else{
						$(obj).parent().parent().children().eq(0).attr("rowspan","1");
						$(obj).parent().parent().children().eq(1).attr("rowspan","1");
					}

					$(obj).parent().parent().next().hide();
					if($(obj).parent().parent().next().find("input").val().trim() != ""){
						$(obj).parent().next().find(".groupNote").children().remove();
						$(obj).parent().next().find(".groupNote").append('<em class="groupNoteTipImg"></em>');
					}
				});
				top.$.jBox.info("请先修改完错误再提交", "警告");
				top.$('.jbox-body .jbox-icon').css('top','55px');
				validatecount++;
				return validatecount;
			}
			return validatecount;
		}

		var submit_times = 0;


		function visaValidator() {
			var flag = true;
			$("#thirdStepContent [name='visaType']").each(function(index, obj) {
				var visaVal = $(obj).val();
				var countryVal = $(obj).parent().parent().children().children("[name='country']").val();
				if(countryVal == "" && visaVal != "") {
					flag = false;
					top.$.jBox.info("请选择签证类型对应签证国家", "警告");
                    removeDisable();//bug16442 保存不成功时将保存恢复可用状态
					return false;
				}
				if(countryVal != "" && visaVal == "") {
					flag = false;
					top.$.jBox.info("请选择签证国家对应签证类型", "警告");
                    removeDisable();//bug16442
					return false;
				}
			});
			return flag;
		}
		//79需求订金占位设置非必填项--2016/02/29--wenchao.lv
		//為避免衝突公用方法問題
		function paychgsubsription(obj) {
			if($(obj).prop("checked")){
				//$(obj).next().next().find("span.xing").css("display","inline");
				//$(obj).next().next().next().find("input").rules("remove");
				$(obj).next().next().next().find("input").removeAttr("disabled");
				$(obj).next().next().next().next().find("input").removeAttr("disabled");
				$(obj).next().next().next().next().next().find("input").removeAttr("disabled");
				//$(obj).next().next().next().find("input").rules("add",{required:true,digits:true,nonzero:true,messages:{required:"必填信息",digits:"请输入正确的数字",nonzero:"输入不能字",nonzero:"输入不能为0"}});
			}else{
				//$(obj).next().next().find("span.xing").css("display","none");
				$(obj).next().next().next().find("input").rules("remove");
				$(obj).next().next().next().find("input").val("");
				$(obj).next().next().next().next().find("input").val("");
				$(obj).next().next().next().next().next().find("input").val("");
				$(obj).next().next().next().find("input").attr("disabled","disabled");
				$(obj).next().next().next().next().find("input").attr("disabled","disabled");
				$(obj).next().next().next().next().next().find("input").attr("disabled","disabled");
			}
		}
		//79需求订金占位设置非必填项--2016/02/29--wenchao.lv
		function paychg(obj) {
			if($(obj).prop("checked")) {
				$(obj).next().next().find("span.xing").css("display","inline");
				//$(obj).next().next().next().find("input").rules("remove");
				$(obj).next().next().next().find("input").removeAttr("disabled");
				$(obj).next().next().next().next().find("input").removeAttr("disabled");
				$(obj).next().next().next().next().next().find("input").removeAttr("disabled");
				//$(obj).next().next().next().find("input").rules("add",{required:true,digits:true,nonzero:true,messages:{required:"必填信息",digits:"请输入正确的数字",nonzero:"输入不能为0"}});
			} else {
				$(obj).next().next().find("span.xing").css("display","none");
				$(obj).next().next().next().find("input").rules("remove");
				$(obj).next().next().next().find("input").val("");
				$(obj).next().next().next().next().find("input").val("");
				$(obj).next().next().next().next().next().find("input").val("");
				$(obj).next().next().next().find("input").attr("disabled","disabled");
				$(obj).next().next().next().next().find("input").attr("disabled","disabled");
				$(obj).next().next().next().next().next().find("input").attr("disabled","disabled");
			}
		}

		function trafficchg(){
			var value=$("#trafficMode option:selected").val();
			if("${relevanceFlagId}"!="" && "${relevanceFlagId}".split(",").contains(value))
			{
				$(".ml25").css("display","inline-block");
			}else {
				$(".ml25").css("display","none");
			}
		}

		function removevisafile(msg,obj){

			top.$.jBox.confirm(msg,'系统提示',function(v){
				if(v=='ok'){
					var divobj = $(obj).parent().parent().parent().parent().parent();
					$(divobj).remove();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');


		}
		//对新增团期的删除
		function delGroupDate(obj){
			$(document).delGroup1(obj);
		}
		function writeGroupDate(){
			$(document).writeDate();
		}

		//对已有团期的删除
		function delmodgroup(obj){
			//判断是否有已有占位，如果有占位，则不能删除
			var groupId = $(obj).parent().parent().find("input[name='groupid']").val();
			var groupCode = $(obj).parent().parent().find("input[name='groupCode']").val();
			$.ajax({
				type: "POST",
				async:false,
				url: "${ctx}/activity/manager/hasOrder",
				data: {"groupId":groupId},
				success: function(result){
					if(result.data=="true" || result.data== true){
						top.$.jBox.info("团号为"+groupCode+"的团期已存在占位，不能删除", "警告");
						return;
					}else{
						var groupSelf = $(obj).parent().parent();
						var id = groupSelf.find("input[name='groupid']").val();
						var date = groupSelf.find("input[name='groupOpenDate']").val();
						$(document).datepickerRefactor.deldisable(date);
						delGroupIds += id + ",";
						//修改bug16375
						//groupSelf.next().next().next().remove();
						//如果存在价格方案,则将其转换为input输入框
						if(groupSelf.next().next().next().find('table').hasClass('table')){
							groupSelf.next().next().next().remove();
						}
						groupSelf.next().next().remove();
						groupSelf.next().remove();
						groupSelf.remove();

						if(0 == $("#modTable").find("tbody").find("input[name='groupid']").length) {
							$("#secondStepContent").find("#secondStepMod").hide();
							$("#secondStepContent").find(".addNewGroup").show();
						}
					}
				}
			});
		}

		function removeCodeCss(obj){
			$(obj).removeAttr("style");
		}

		//删除签证资料
		function removefile(msg,obj){
			top.$.jBox.confirm(msg,'系统提示',function(v){
				if(v=='ok'){
					var divobj = $(obj).parent().parent().parent().parent().parent();
					$(divobj).remove();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
			return false;
		}


		//修改部分
		//控制截团时间
		function takeOrderOpenDate(obj){
			var groupOD = $(obj).parent().prev().children("input[name='groupOpenDate']").val();
			return groupOD;
		}
		//控制截团时间
		function takeOpenDate(obj){
			var getGroupCD = $(obj).attr("id");
			var index = getGroupCD.charAt(14);
			var strGroupOD = "#groupOpenDate";
			var groupOD = strGroupOD.concat(index);
			var OpenDate = $(groupOD).val();
			var strArrOD = $(groupOD).attr("value").split("-");
			var thisMouth = parseInt(strArrOD[1])+1;
			var thisYear = parseInt(strArrOD[0]);
			if(thisMouth==13){
				thisYear+=1;
				thisMouth=1;
			}
			var thisDate = thisYear+"-"+thisMouth+"-"+strArrOD[2];
			return thisDate;

		}
		//控制材料截止日期
		function takeVisaDate(obj){
			var getGroupCD = $(obj).attr("id");
			if (getGroupCD) {
				var index = getGroupCD.substring(8,getGroupCD.length);
				var strGroupOD = "#groupOpenDate";
				var groupOD = strGroupOD.concat(index);
				var OpenDate = $(groupOD).val();
				return OpenDate;
			}
		}
		function takeModVisaDate(obj) {
			var groupOD = $(obj).parent().parent().children().first().children("input[name='groupOpenDate']").val();
//			alert(groupOD);
			return groupOD;
		}
		//验证日期合法
		function getMinDate(obj){
			var getGroupCD = $(obj).attr("id");
			var index = getGroupCD.substring(14,getGroupCD.length);
			var strGroupOD = "#groupOpenDate";
//			var strVisaDate = "#visaDate";
			var groupOD = strGroupOD.concat(index);
//   		var groupCD = strGroupOD.charAt(0).concat(getGroupCD);
			var maxDate = $(groupOD).val();
			return maxDate;

		}
		function getCurDate(){
			var curDate = new Date();
			var times = curDate.getTime();
			times += 24*3600*1000;
			curDate.setTime(times);
			var dateStr = curDate.getFullYear()+"-"+(curDate.getMonth()+1)+"-"+curDate.getDate();
			return dateStr;
		}
		function selfDefine(obj,type,soure){
			if($("#"+type).css("display")=="none"){
				$(obj).html("取消");
				$("#"+type).css("display","block");
				$("#"+type).attr("disabled",false);
				$("#"+soure).css("display","none");
				$("#"+soure).attr("disabled",true);
			}else{
				$(obj).html("自定义");
				$("#"+type).css("display","none");
				$("#"+type).attr("disabled",true);
				$("#"+soure).css("display","block");
				$("#"+soure).attr("disabled",false);
			}
		}
		//空位数量初始等于预收人数
		function comparePosition(obj){
			var plan = $(obj).val();
			$(obj).parent().next().find("input").val(plan);
			$(obj).parent().next().find("input").focus();
			$(obj).parent().next().find("input").blur();
		}
		/**
		 bug 8729 预收与余位取消联动关系 change by 20151010
		 function comparePositionMod(obj){
    		var plan = $(obj).val();
    		$(obj).parent().next().find("input").val(plan);
    		$(obj).parent().next().find("input").focus();
    		$(obj).parent().next().find("input").blur();
    	}
		 */

		/**
		 * mod_infoinformation3_file：第一个按钮
		 * btn：第二个按钮
		 * src：object
		 * dest：object_file
		 */
		function uploadreserve(btn,src,dest){
			if($(src).css("display")=="none"){
				$(".mod_infoinformation3_file").val("选择文件");//第一个按钮变"选择文件"
				$(src).show().parent().show();//object显示
				$(dest).hide();//object_file隐藏
				$(btn).val("取消上传");//第二个按钮变"取消上传"
			}else{
				var file = $(src);
				file.val("");
				file.after(file.clone().val(""));
				//file.remove();
				$(src).hide();
				$(src).next().next().val("");
				$(src).parent().hide();
				$(dest).show();
				$(btn).val("上传文件");
			}
		}
		function muluploadreserve(btn,src,dest){
			var s = $(btn).parent().parent().find("input[name='"+src+"']")[0];
			var d = $(btn).parent().parent().find("input[name='"+dest+"']")[0];
			if($(s).css("display")=="none"){
				$(s).css("display","inline");
				$(s).parent().css("display","");
				$(d).css("display","none");
				$(btn).val("取消上传");
			}else if($(s).css("display")=="block"){
				var file = $(s);
				file.after(file.clone().val(""));
				file.remove();

				file = $(btn).parent().parent().find("input[name='"+src+"']")[0];
				$(file).css("display","none");
				$(file).parent().css("display","none");
				$(file).next().next().val("");
				$(d).css("display","inline");
				$(btn).val("上传文件");
			}
		}
		function inFileName(obj){
			var flag = $(obj).fileInclude({includes:[".xlsx",".xls",".doc",".docx"]});
			var dest = $(obj).parent().find("input[name='fileLogo']");
			if(flag){
				var res = $(obj).val();
				$(dest).val(res);
			}else{
				$(obj).val("");
				$(dest).val("");
				top.$.jBox.info("文件上传仅支持带有.xlsx,.xls,.doc,.docx后缀名的文件", "警告");
				top.$('.jbox-body .jbox-icon').css('top','55px');
			}

		}


		(function( $ ) {
			$.widget( "custom.combobox", {
				_create: function() {
					this.wrapper = $( "<span>" )
							.addClass( "custom-combobox" )
							.insertAfter( this.element );

					this.element.hide();
					this._createAutocomplete();
					this._createShowAllButton();
				},

				_createAutocomplete: function() {
					var selected = this.element.children( ":selected" ),
							value = selected.val() ? selected.text() : "";

					this.input = $( "<input>" )
							.appendTo( this.wrapper )
							.val( value )
							.attr( "title", "" )
							.addClass( "custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left" )
							.autocomplete({
								delay: 0,
								minLength: 0,
								source: $.proxy( this, "_source" )
							})
							.tooltip({
								tooltipClass: "ui-state-highlight"
							});

					this._on( this.input, {
						autocompleteselect: function( event, ui ) {
							ui.item.option.selected = true;
							this._trigger( "select", event, {
								item: ui.item.option
							});
						},

						autocompletechange: "_removeIfInvalid"
					});
				},

				_createShowAllButton: function() {
					var input = this.input,
							wasOpen = false;

					$( "<a>" )
							.attr( "tabIndex", -1 )
							.attr( "title", "选择" )
							.tooltip()
							.appendTo( this.wrapper )
							.button({
								icons: {
									primary: "ui-icon-triangle-1-s"
								},
								text: false
							})
							.removeClass( "ui-corner-all" )
							.addClass( "custom-combobox-toggle ui-corner-right" )
							.mousedown(function() {
								wasOpen = input.autocomplete( "widget" ).is( ":visible" );
							})
							.click(function() {
								input.focus();

								// Close if already visible
								if ( wasOpen ) {
									return;
								}

								// Pass empty string as value to search for, displaying all results
								input.autocomplete( "search", "" );
							});
				},

				_source: function( request, response ) {
					var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
					response( this.element.children( "option" ).map(function() {
						var text = $( this ).text();
						if ( this.value && ( !request.term || matcher.test(text) ) )
							return {
								label: text,
								value: text,
								option: this
							};
					}) );
				},

				_removeIfInvalid: function( event, ui ) {

					// Selected an item, nothing to do
					if ( ui.item ) {
						return;
					}

					// Search for a match (case-insensitive)
					var value = this.input.val(),
							valueLowerCase = value.toLowerCase(),
							valid = false;
					this.element.children( "option" ).each(function() {
						if ( $( this ).text().toLowerCase() === valueLowerCase ) {
							this.selected = valid = true;
							return false;
						}
					});

					// Found a match, nothing to do
					if ( valid ) {
						return;
					}

					// Remove invalid value
					this.input
							.val( "" )
							.attr( "title", value + "" )
							.tooltip( "open" );
					this.element.val( "" );
					this._delay(function() {
						this.input.tooltip( "close" ).attr( "title", "" );
					}, 2500 );
					this.input.data( "ui-autocomplete" ).term = "";
				},

				_destroy: function() {
					this.wrapper.remove();
					this.element.show();
				}
			});
		})( jQuery );

		function activitySerNumEmpty() {
			if($("#activitySerNum").val()=="")
				$("#activitySerNum").val($(".activitySerNumHid").val());
		}

		function transportAdd(element, index){
			var middle = 'onkeyup="this.value=this.value.replace(/[^\\d\\+\\-]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^\\d\\+\\-]/g,\'\')"';
			$(element).parent().parent().append('<p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input class="valid" id="intermodalGroupPart' + (index+1) + '" name="intermodalGroupPart" maxlength="50" type="text"><label>联运价格：</label><input class="valid rmbp17" id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="8" type="text" ' + middle + ' /><a class="ydbz_s transportAdd" onclick="javascript:transportAdd(this,' + (index+1) + ');">增加</a><a class="ydbz_s transportDel">删除</a></p>');
			$('.transportAdd').attr('onclick', 'transportAdd(this, ' + (index + 1) + ')');
		}

		//获取当前金额的币种，用于显示在输入框下面的span中。
		function getCurrencyMark(obj) {
			return $(".choose-currency").find("p[id='"+$(obj).prev().attr("var")+"']").attr("addclass");
		}

		function downloads(docid){
			window.open("${ctx}/sys/docinfo/download/"+docid);
		}
		//添加签证资料
		function addvisafile(obj){

			var inputCount = $(obj).attr("class");

			var tempEle = $("[name='signtemplate']").clone();
			tempEle.find("td").eq(5).find("input").attr("id","signmaterial" + inputCount);
			tempEle.find("td").eq(6).append('<input type="button" name="signmaterial' + inputCount + '" value="选择文件" class="mod_infoinformation3_file" onClick="uploadFiles(\'${ctx}\',\'' + "signmaterial" + inputCount + '\',this)"></input>');
			tempEle.find("td").eq(6).append('<input type="hidden" name="fileGroup" value="' + inputCount + '" />');

			var html = "<div id=\"visafile\" class=\"mod_information_d8_2\" style=\"margin-top:5px;\">"+tempEle.html()+"</div>";



			$("#visaData").append(html);


			$("#visaData .mod_information_d8_2 select[name='country']").combobox();

			add_visaFileValidator();
		}

		function cancelAirTicket() {
			top.$.jBox.confirm('确认要取消关联的机票产品？','系统提示',function(v){
				if(v=='ok'){
					$('.activityAirTicketId').val('');
					$(".changeAirSpan").attr("id","");
					if($(".airInfo").find("div").length != 0) {
						$(".airInfo-tit1").remove();
						$(".airInfo-con").remove();
						$(".otherInfo").remove();
					}
				}
			});
		}

		function checkall_new(obj){
			if($(obj).attr("checked")){
				$('#contentTable input[name="ids"]').attr("checked",'true');
				$("input[name='allChk']").attr("checked",'true');
			}else{
				$('#contentTable input[name="ids"]').removeAttr("checked");
				$("input[name='allChk']").removeAttr("checked");
			}
		}



		function checkall_news(obj){
			if($(obj).attr("checked")){
				$('#modTable input[name="idss"]').attr("checked",'true');
				$("input[name='allChks']").attr("checked",'true');
			}else{
				$('#modTable input[name="idss"]').removeAttr("checked");
				$("input[name='allChks']").removeAttr("checked");
			}
		}

		/**
		 *
		 * @param {} obj
		 */
		var flag=10;
		function validateLong(obj)
		{
			replaceStr(obj);
			if($(obj).val().length<=49){
				flag = 10;
			}
			if($(obj).val().length>=50)
			{
				if($(obj).val().length==50 && flag==10){
					//$.jBox.tip("团号只能输入50个字符","true");
					flag++;
				}else{
					$.jBox.tip("团号超过50个字符，请修改","error");
				}
				return false;
			}
		}


		$(document).on('click','.control-display',function(){
			if($(this).text()!="收起"){
				$(this).text('收起')
				$(this).parent().css('height','auto');
			}else{
				$(this).text('展开所选择的团')
				$(this).parent().css('height','30px');
			}

		});

		// 223 需求   取消团期关联团控表
		function cancel_group_control(thisObj){
			var cruiseGroupControlId =  $(thisObj).parents('td:first').next().find('input[name=cruiseGroupControlId]').val();
			if(""!=cruiseGroupControlId){
				$.jBox.confirm("确定要取消关联团控表？","提示",function(v,h,f){
					if (v == 'ok') {
						//debugger;
						$(thisObj).parents('td:first').next().find('input[name=cruiseGroupControlId]').val("");
					}
				});

			} else{
				top.$.jBox.info("该团期没有关联团控表!", "警告");
			}
		}
		//bug12826 ----- begin -------
		/*修改出团日期后回调团号同步方法 */
		function syncGroupCode(obj){
			var $current = $(obj).parent().next().next().next().find("[name='groupCode']");
			asynchangeGroupCode($current);
		}

		/*游轮新增舱型产生的相同出团日期的団期修改团号时，同步修改相同出团日期的团号 */
		function asynchangeGroupCode(obj){
			//当前団期的出团日期
			var $groupOpenDateTemp = $(obj).parent().prev().prev().prev().find("[name='groupOpenDate']").val();
			//当前团号
			var $groupCodeTemp = $(obj).val();
			//循环tbody下的每个出团日期input,和当前值相等时更新团号值
			$(obj).parent().parent().parent().find("[name='groupOpenDate']").each(function(i,n){
				if(n.value == $groupOpenDateTemp){
					$(n).parent().parent().find("[name='groupCode']").val($groupCodeTemp);
				}
			});

		}
		// ----- end -------


		function removeDisable(){
			$("#submitAndSave").removeClass("disableCss");
		}

		/**
		 *四舍五入，保留位数为
		 *num:要格式化的数字
		 *scale: 保留的位数
		 */
		function xround(num,scale){
			var resultTemp;
			if(scale>0){
				resultTemp = Math.round(num * Math.pow(10, scale)) / Math.pow(10, scale);
			}
			return resultTemp.toFixed(2);
		}
		//修改时记录原来的数据
		function beforeModify(obj){
			$(obj).each(function(){
				var oldVal = $(this).val();
				$(this).attr("oldVal",oldVal);
			});
		}

		//t1t2-v4 518修改
		function modify(obj) {
			saveOldGroupflag = false;
			modifycount = 0;
//			bug16368 保存修改前的值 S
			var $tr = $(obj).parent().parent();
			var $input=$tr.find("td input[type='text']");
			if($(obj).parent().parent().next().next().next().find('table').hasClass('table')) {
				var $priceTableInput = $tr.next().next().next().find("td input[type='text']");
				beforeModify($priceTableInput);
			}
			var $oldNotes = $tr.next().next().find("input[name='groupRemark']");
			beforeModify($input);

			beforeModify($oldNotes);
//			bug16368 保存修改前的值 E
			var groupId = $(obj).parent().parent().find("input[name='groupid']").val();
			$(obj).parent().parent().undisableContainer();
			$(obj).css('display','none');
			//如果存在价格方案,则将其转换为input输入框
			if($(obj).parent().parent().next().next().next().find('table').hasClass('table')){
				$(obj).parent().parent().next().next().next().undisableContainer();
			}

			$(obj).parent().children("span[name='hideBtn']").show();
			$(obj).parent().children("span[name='deleteBtn']").hide();
			$(obj).parent().children("span[name='expandPricing']").hide();
			beforeOperate(obj);
			/*if($(obj).parent().parent().find('.g-w').hasClass('grounding')) {
				$.jBox.info("该团期已上架旅游交易预订系统，如需修改" +
						"则产品将下架，是否确认修改？","提示",{
					buttons: {"关闭": "0", "确认": "1"},
					submit:function(v,h,f)
					{
						if (v == '1') {
							$.ajax({
								type: "POST",
								url: "${ctx}/activity/manager/confimIsT1Off",
								data: {groupIdsBuffer: groupId},
								success: function (data) {
									if (data.flag) {
										$(obj).parent().parent().undisableContainer();
										$(obj).parent().parent().next().next().next().undisableContainer();
										$(obj).css('display','none');
										$(obj).parent().children("span[name='hideBtn']").show();
										$(obj).parent().children("span[name='deleteBtn']").hide();
										$(obj).parent().children("span[name='expandPricing']").hide();
										$(obj).parent().parent().children("td").first().children(".withdraw-relative").find('.g-w').removeClass('grounding').addClass('withdraw');
										$(obj).parent().parent().children("td").first().children(".withdraw-relative").find('.withdraw-hover').html();
										$(obj).parent().parent().children("td").first().children(".withdraw-relative").find('.withdraw-hover').html('已下架旅游交易系统');
										$(obj).parent().parent().children("td").first().children(".withdraw-relative").find('.withdraw-hover').removeClass('grounding-hover').addClass('withdraw-hover');
										var checkboxStyle = $(obj).parent().parent().children("td").first().children(".withdraw-relative").find("#"+""+groupId+"").css("display");
										if(checkboxStyle == 'none') {
											$(obj).parent().parent().children("td").first().children(".withdraw-relative").find("#"+""+groupId+"").attr("style","display: inline-block;");
										}else {
											var checkboxHtml = "<input type=\"checkbox\" id=\"" + groupId + "\" class=\"tableCheckBox\" name=\"groupNo\" style=\"display: inline-block\">";
											$(obj).parent().parent().children("td").first().children(".withdraw-relative").children('input').first().before(checkboxHtml);
										}
//										$(obj).parent().children('.stack-btn').show();
										beforeOperate(obj);
									} else {
										$.jBox.tip('操作失败,原因:' + data.msg, 'error');
										return false;
									}
								}
							})
						}
						if (v == '0') {
							return true;
						}
					}
				});
			}else{
				$(obj).parent().parent().undisableContainer();
				$(obj).parent().parent().next().next().next().undisableContainer();
				$(obj).css('display','none');
				$(obj).parent().children("span[name='hideBtn']").show();
				$(obj).parent().children("span[name='deleteBtn']").hide();
				$(obj).parent().children("span[name='expandPricing']").hide();
				beforeOperate(obj);
			}*/
		}
		//t1t2-v4 518取消
		function cancelMod(obj) {
			$(obj).parent().parent().disableContainerCancel();//bug16368
			if($(obj).parent().parent().next().next().next().find('table').hasClass('table')){
				$(obj).parent().parent().next().next().next().disableContainerCancel();
			}
			//input为空时将-替换为空 S
			$(obj).parent().parent().find(".disabledshowspan").each(function(){
				if($(this).text()=="—"){
					$(this).text("");
				}
			});
			//input为空时将-替换为空 E
			$(obj).parent().parent().find("label[for='freePosition1']").remove();//清除错误提醒
			//bug16368 将备注恢复到修改前 S
			var $note = $(obj).parent().parent().next().next().find("input[name='groupRemark']");
			var oldNote = $note.attr("oldval");
			$note.val(oldNote);
			//bug16368 将备注恢复到修改前 E
			$(obj).parent().children("span[name='hideBtn']").hide();
			$(obj).parent().children("span[name='deleteBtn']").show();
			$(obj).parent().children("span[name='expandPricing']").show();
			$(obj).parent().children("span").first().show();
			afterOperate(obj);
			modifycount = 0;
			saveOldGroupflag = true;
		}

		/**
		 * 成功保存已存在的团期提示
		  */
		function saveSuccess(vcount, obj){
			if(vcount == 0) {
				$(obj).parent().parent().disableContainer();
				if($(obj).parent().parent().next().next().next().find('table').hasClass('table')){
					$(obj).parent().parent().next().next().next().disableContainer();
				}
				//$(obj).parent().parent().next().next().next().disableContainer();
				$(obj).parent().parent().find(".disabledshowspan").each(function(){
					if($(this).text()=="—"){
						$(this).text("");
					}
				});
				$("#modTable").find("input[name='settlementAdultPrice']").each(function(){
					if($(this).css("display") != 'none'){
						saveOldGroupflag = false;
					}
				});
				$(obj).parent().children("span[name='hideBtn']").hide();
				$(obj).parent().children("span[name='deleteBtn']").show();
				$(obj).parent().children("span[name='expandPricing']").show();
				$(obj).parent().children("span").first().show();
				afterOperate(obj);
				jBox.info("保存成功", "提示");
			}
		}
		//t1t2-v4 518保存确认
		function saveMod(obj) {
			var groupId = $(obj).parent().parent().find("input[name='groupid']").val();
			var groupCode = $(obj).parent().parent().find("input[name='groupCode']").val();
            var isT1 = $(obj).parent().parent().find("input[name='ist1']").val();
			
			var suggestAdultPrice = $(obj).parent().parent().find("input[name='suggestAdultPrice']").val();
			if (requiredStraightPrice == 'true') {
				if (suggestAdultPrice == '0' || suggestAdultPrice == '0.0' || suggestAdultPrice == '0.00' 
					|| suggestAdultPrice == '' || suggestAdultPrice == null) {
					top.$.jBox.info("必须设置直客价", "警告");
					return;
				}
			}
			var vcount = saveSingleGroup(obj);

            if(isT1 == 1) {
                changedSaveGroupIds(groupId);
            }
		
		}
		//t1t2-v4 518保存前操作
		function beforeOperate(obj) {
			var serialId = $(obj).parent().parent().find("input[name='serial']").val();
			$(obj).parent().parent().parent().find("#remark_" + serialId).hide();
			$(obj).parent().parent().parent().find("#gRemark_" + serialId).hide();
			$(obj).parent().parent().parent().find("#groupRemark_" + serialId).find("td:eq(0)").attr("colspan",20);
			$(obj).parent().parent().parent().find("#groupRemark_" + serialId).show();
			$(obj).parent().parent().parent().find("#groupRemark_" + serialId).find("input[name='groupRemark']").attr('style','display: inline-block;').next().remove();
			//显示团期价格操作按钮
			$("#modTable .addAndRemove").show();
			$(obj).parent().parent().children("td[name='hotelhouse']").children("div[name='hotelhouseDiv']").find("em[name='addSelect_"+serialId+"']").show();
			$(obj).parent().parent().children("td[name='hotelhouse']").children("div[name='hotelhouseDiv']").find("em[name='removeSelect_"+serialId+"']").show();
			//$("#modTable .remove-selected").show();
			$("#modForm .addAndRemove").show();
			$(obj).parent().parent().next().next().next().find(".add-select").show();
			$(obj).parent().parent().next().next().next().find(".remove-selected").hide();
//		$("#modForm .remove-selected").show();
		}
		//t1t2-v4 518保存后操作
		function afterOperate(obj) {
			var serialId = $(obj).parent().parent().find("input[name='serial']").val();
			$(obj).parent().parent().parent().find("#gRemark_" + serialId).hide();
			$(obj).parent().parent().parent().find("#groupRemark_" + serialId).hide();
			$(obj).parent().parent().parent().find("#remark_" + serialId).show();
//		$(obj).parent().parent().parent().find(".disabledshowspan").show();
			//隐藏团期价格操作按钮
//		$("#modTable .addAndRemove").hide();
			$(obj).parent().parent().children("td[name='hotelhouse']").children("div[name='hotelhouseDiv']").find("em[name='addSelect_"+serialId+"']").attr('style','display: none;');
			$(obj).parent().parent().children("td[name='hotelhouse']").children("div[name='hotelhouseDiv']").find("em[name='removeSelect_"+serialId+"']").attr('style','display: none;');
			//$("#modTable .remove-selected").hide();
//		$("#modForm .addAndRemove").hide();
			$(obj).parent().parent().next().next().next().find(".add-select").attr('style','display: none;');
			$(obj).parent().parent().next().next().next().find(".remove-selected").attr('style','display: none;');
			//$("#modForm .remove-selected").hide();
			//删除错误校验信息
			$(obj).parent().parent().find("label.error").remove();
		}
		//t1t2-v4 518保存单个团期的数据
		function saveSingleGroup(obj) {
			var vcount = 0;
			// 433需求 只有预收或只有余位数变更后，保存时，弹出提示
			var proId = $("#proId").val();
			var groupId = $(obj).parent().parent().find("input[name='groupid']").val();
			var planPositon = $(obj).parent().parent().find("input[name='planPosition']").val();
			var freePositon = $(obj).parent().parent().find("input[name='freePosition']").val();
			if(groupId != undefined){
				$.ajax({
					type:"POST",
					url:"${ctx}/activity/manager/checkGroupPlanAndFreePositonChange",
					dataType:"json",
					async:false,
					contentType: "application/x-www-form-urlencoded;charset=utf-8",//默认按照jsp页面的contentType编码来encode,所以这个可以不用设置
					data:{
						proId:proId,
						groupId:groupId,
						plans:planPositon,
						frees:freePositon
					},
					success:function(result){
						if(result.data == 'success'){
							if(result.changePlan){// 只修改了预收数，没有修改余位数
								//vcount = 1;//bug16414
								$.jBox.confirm("团号为"+result.changeGroupId+"团期，预收数已发生变化，是否要修改余位数？", "系统提示", function (v, h, f) {
									if (v == 'ok') {
										//secondStepMod();
										return;
									} else if (v == 'cancel') {
										//vcount = secondStepSave();
										vcount = saveOldGroup(obj);
										saveSuccess(vcount, obj)
										/*if(vcount == 0) {
											$(obj).parent().children("span[name='hideBtn']").hide();
											$(obj).parent().children("span[name='deleteBtn']").show();
											$(obj).parent().children("span[name='expandPricing']").show();
											$(obj).parent().children("span").first().show();
											$(obj).parent().parent().disableContainer({blankText : ""});
											$(obj).parent().children(".uploadGroupFileClass").show();
										}*/
										return;
									}
								});
							}else if(result.changeFree){// 只修改了余位数，没有修改预收数时
								//vcount = 1;//bug16414
								$.jBox.confirm("团号为"+result.changeGroupId+"团期，余位数已发生变化，是否要修改预收数？", "系统提示", function (v, h, f) {
									if (v == 'ok') {
										//secondStepMod();
										return;
									} else if (v == 'cancel') {
										//vcount = secondStepSave();
										vcount = saveOldGroup(obj);

										/*if(vcount == 0) {
											$(obj).parent().children("span[name='hideBtn']").hide();
											$(obj).parent().children("span[name='deleteBtn']").show();
											$(obj).parent().children("span[name='expandPricing']").show();
											$(obj).parent().children("span").first().show();
											$(obj).parent().parent().disableContainer({blankText : ""});
											$(obj).parent().children(".uploadGroupFileClass").show();
										}*/
										saveSuccess(vcount, obj);
										return;
									}
								});
							}else{// 预收数和余位数都不变化或都变化时
								//vcount = secondStepSave();
								vcount = saveOldGroup(obj);
								saveSuccess(vcount, obj);
								return;
							}
						}else if(result.data == 'fail'){
//							top.$.jBox.info(result.fialMsg, "警告");
							var _secondStepMod= $("#secondStepMod").validate({
								//var secondStepMod= $tr.parent().validate({
								rules:{
									activitySerNum:{
										remote: {
											type: "POST",
											url: "${ctx}/activity/manager/serNumRepeat?proId="+$("#proId").val()
										}
									}
								},
								messages:{
									activitySerNum:{remote:"编号已存在"}
								},
								errorPlacement: function(error, element) {
									if($(element).attr("id")=="introduction")
										error.appendTo (element.parent().parent());
									else if($(element).hasClass("spinner_day"))
										error.appendTo (element.parent().parent());
									else if ( element.is(":radio") )
										error.appendTo ( element.parent() );
									else if ( element.is(":checkbox") )
										error.appendTo ( element.parent() );
									else if ( element.is("input") )
										error.appendTo ( element.parent() );
									else if($(element).attr("id")=="fromArea" || $(element).attr("id")=="backArea")
										error.appendTo ( element.parent() );
									else
										error.insertAfter(element);
								},
								showErrors:function(errorMap,errorList){
									this.defaultShowErrors();
									errorArray = errorArray.concat(errorList);
								},
								ignore:":hidden[id!='fromArea'][id!='backArea'][id='secondStepEnd']"
							});
							var flag = _secondStepMod.form();
						}
					}
				})
				return vcount;
			}
		}
		// 保存
		function confirmSaveGroup(){

		}
		//批量平台上架
		function batchGrounding(obj){
			var length = $("#modTable .withdraw-relative input[type='checkbox']:checked").length;
			if(length==0){
				$.jBox.tip('请选择要操作的数据','warnning');
				return false;
			}
			var $check = $("#modTable input[name='groupNo']").not(":disabled").next().next().next("input[name='idss']");
			$check.each(function(obj){
					var $obj =  $(this);
					var groupId = $obj.attr('id');
				$.ajax({
					type: "POST",
					url: "${ctx}/activity/manager/confimIsT1On",
					data: {groupId:groupId},
					success: function (data) {
						if (data.flag) {
							//重新执行提交保存刷新页面
							$obj.parent().parent().find('.g-w').removeClass('withdraw').addClass('grounding');
							$obj.parent().parent().find('.grounding-one').html();
							$obj.parent().parent().find('.grounding-one').html('已上架旅游交易系统');
							$obj.parent().parent().find('.grounding-one').removeClass('withdraw-hover').addClass('grounding-hover');
							$obj.parent().parent().find('input[type="checkBox"]').hide();
//							$obj.parent().parent().parent().find('.stack-btn').hide();
						} else {
							$.jBox.tip('操作失败,原因:' + data.msg, 'error');

						}
					}
				})
			});
		}
		//平台上架
		function grounding(groupId,obj){
			var title;
			$.ajax({
				type:"post",
				url:"${ctx}/activity/manager/checkPriceRecord",
				data:{groupId:groupId},
				success:function(result){
					if(result.flag){
						title = "上架失败，因调整同行价或直客价请重新调整策略";
					}else {
						title = "上架成功";
						$.ajax({
							type: "POST",
							url: "${ctx}/activity/manager/confimIsT1On",
							data: {groupId:groupId},
							success: function (data) {
								if (data.flag) {
									//$.jBox.tip('操作成功！');
									$(obj).css("display","none");
									$(obj).parent().parent().find('.g-w').removeClass('withdraw').addClass('grounding');
									$(obj).parent().parent().find('.grounding-one').removeClass('withdraw-hover').addClass('grounding-hover');
									$(obj).parent().parent().find('input[type="checkbox"]').hide();
									$(obj).parent().find('.morePrice').css('display','block');
								} else {
									$.jBox.tip('操作失败,原因:' + data.msg, 'error');

								}
							}
						})
					}
					$.jBox.info(title,"提示",{
						buttons: {"关闭": "0"},
						submit:function(v,h,f)
						{
							if (v == '0') {
								return true;
							}
						}
					})
				}
			})
		}
		function setPricingStrategy(obj,groupid,activityid,activityName,isCheck){
			openPricingStrategy(isCheck,obj,groupid,activityid,activityName,'/pricingStrategy/manager/editPricingStrategy/'+groupid+"/"+activityid,"/pricingStrategy/manager/addActivityStrategy");
		}
		function openPricingStrategy(isCheck,obj,groupid,activityid,activityName,url,addUrl){
			var ctx = $('#ctx').val();
			//新建一个隐藏的div，用来保存文件上传后返回的数据
			if($(obj).parent().find("#hiddenParm").length == 0)
				$(obj).parent().append('<div  style="display: none" id="hiddenParm"></div>');
			$(obj).addClass("clickBtn");
			$.jBox("iframe:"+ ctx + url, {
				title: activityName,
				width: 773,
				height: 400,
				buttons: {'提交':true,'取消':false},
				persistent:true,
				loaded: function (h) {},
				submit: function (v, h, f) {
					//获取价格策略值
					var adultPri = $("#adultPri").val();
					var childrenPri = $("#childrenPri").val();
					var specialPri = $("#specialPri").val();
					$("#hiddenParm").remove();
					$(".clickBtn",window.parent.document).removeClass("clickBtn");
					if(!v){
						return true;
					}else if(isCheck){
						$.jBox.confirm("已存在QUAUQ价，是否替换","提示",function(v,h,f){
							if(v=='ok'){
								if(v){
									$.ajax({
										type:"POST",
										url:ctx+addUrl,
										data:{groupid:groupid,activityid:activityid,adultPri:adultPri,childrenPri:childrenPri,specialPri:specialPri},
										success:function(data){
											if('ok' == data.flag){
												loading('正在提交，请稍等...');
												window.location.reload();
											}else{
												$.jBox.confirm("设置失败","提示",function(v,h,f){
													if(v=='ok'){
														return;
													}
													if(v=='cancel'){
														return;
													}
												});
											}
										}
									});
								}
								return;
							}
							if(v=='cancel'){
								flag = true;
								return true;
							}
						});
						return false;
					}else{
						if(v){
							$.ajax({
								type:"POST",
								url:ctx+addUrl,
								data:{groupid:groupid,activityid:activityid,adultPri:adultPri,childrenPri:childrenPri,specialPri:specialPri},
								success:function(data){
									if('ok' == data.flag){
										loading('正在提交，请稍等...');
										window.location.reload();
									}else{
										$.jBox.confirm("设置失败","提示",function(v,h,f){
											if(v=='ok'){
												return;
											}
											if(v=='cancel'){
												return;
											}
										});
									}
								}
							});
						}
					}
				}
			});
			$("#jbox-content").css("overflow","hidden");
		}
		// 选择目标区域执行的操作，原先使用tags:treeselect标签，现在需要和旅游线路联动，所以单独拿出来进行处理。
		function selectTargetArea(){
			top.$.jBox.open("iframe:${ctx}/tag/treeselect?url="+encodeURIComponent("/activity/manager/filterTreeData1?kind=${param.kind}")
					+"&checked=true&selectIds="+$("#targetAreaId").val(),"选择区域", 320, 420,{
				buttons:{"确定":"ok","关闭":true}, submit:function(v, h, f){
					if (v=="ok"){
						var tree = h.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
						var ids = [], names = [], nodes = [];
						nodes = tree.getCheckedNodes(true);

						//青岛凯撒目的地选择出境游时显示activityForm.jsp中的部门团号缩写div
						var companyUUID="${fns:getUser().company.uuid }";
						if(companyUUID=='7a8177e377a811e5bc1e000c29cf2586'){
							if(nodes.length>0){
								for(var i=0; i<nodes.length; i++) {
									if(nodes[0].id=='1' && nodes[0].name=='目的地'){
										if(nodes[i].id=='100000' && nodes[i].name=='出境游'){
											//选择了出境游时显示
											$("#deptCodeDiv").show();
											$("#deptCodeFlag").val('1');
											break;
										}else{
											//未选择出境游时隐藏
											$("#deptCodeDiv").hide();
											$("#deptCodeFlag").val('0');
										}
									}
								}
							}else{
								//未选择出境游时隐藏
								$("#deptCodeDiv").hide();
								$("#deptCodeFlag").val('0');
							}
						}
						for(var i=0; i<nodes.length; i++) {//
							if (nodes[i].isParent){
								continue; // 如果为复选框选择，则过滤掉父节点
							}
							ids.push(nodes[i].id);
							names.push(nodes[i].name);
						}
						$("#targetAreaId").val(ids);
						$("#targetAreaName").val(names);
						$("#targetAreaName").focus();
						$("#targetAreaName").blur();
						// 如果存在线路玩法，发送异步请求，进行联动操作
						var $touristLineId = $("#targetAreaId").parent().parent().next().find("#touristLineId");
						if ($touristLineId[0]){
							$.ajax({
								type: "POST",
								async:false,
								url: "${ctx}/activity/manager/getMatchLine",
								data: {areaIds:JSON.stringify(ids)},
								success: function(data){
									var optStr = "";
									if (data.length > 0){
										for (var i = 0; i < data.length; i++){
											optStr += "<option value= '" + data[i].touristLineId + "'>" + data[i].touristLineName + "</option>";
										}
										optStr += "<option value= '0'>其他</option>";
									}else {
										optStr += "<option value='0' selected='selected'>其他</option>";
									}
									$("#touristLineId").html(optStr);
								}
							});

						}
					}
				},loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				},persistent:true
			});
		}
	</script>
	<style type="text/css">
		.breadcrumb{background-color: #FFFFFF;}
		label{cursor:inherit;}
		.disableCss{
			pointer-events:none;
			color:#afafaf;
			cursor:default
		}

	</style>
</head>
<body>
<input type="hidden" id="ctx" value="${ctx}"/>
<page:applyDecorator name="show_head">
	<page:param name="desc">
		<c:choose>
			<c:when test="${travelActivity.activityKind eq '1'}">单团产品修改</c:when>
			<c:when test="${travelActivity.activityKind eq '2'}">散拼产品修改</c:when>
			<c:when test="${travelActivity.activityKind eq '3'}">游学产品修改</c:when>
			<c:when test="${travelActivity.activityKind eq '4'}">大客户产品修改</c:when>
			<c:when test="${travelActivity.activityKind eq '5'}">自由行产品修改</c:when>
			<c:when test="${travelActivity.activityKind eq '10'}">游轮产品修改</c:when>
			<c:otherwise>产品修改</c:otherwise>
		</c:choose>
	</page:param>
</page:applyDecorator>
<div class="produceDiv">
	<form:form id="modForm" modelAttribute="travelActivity" action="${ctx}/activity/manager/modsave?proId=" method="post" class="form-search" enctype="multipart/form-data" autocomplete="off">
		<input type="hidden" id="shelfRightsStatus" name="shelfRightsStatus" value="${shelfRightsStatus }"/>
		<input type="hidden" id="proId" value="${travelActivity.id }"/>
		<input type="hidden" name="kind" value="${travelActivity.activityKind}"/>
		<input type="hidden" name="activityStatus" id="activityStatus" value="${travelActivity.activityStatus }"/>
		<input type="hidden" id="deptId" value="${travelActivity.deptId }"/>
		<input type="hidden" id="activityKind" name="activityKind" value="${travelActivity.activityKind }"/>
		<c:set var="groupPriceFlag" value="false"></c:set>
		<shiro:hasPermission name="price:project">
			<c:set var="groupPriceFlag" value="true"></c:set>
		</shiro:hasPermission>
		<input type="hidden" id="groupPriceFlag" value="${groupPriceFlag }">
		<shiro:hasPermission name="calendarLoose:book:order">
			<c:set var="tuijian" value="true"></c:set>
			<input type="hidden" id="tuijian" value="${tuijian }">
		</shiro:hasPermission>
		<div class="mod_nav" style="margin-bottom: 10px;">产品修改</div>

		<a href="" style="text-decoration: none"></a>

		<div id="oneStepTitle" class="ydbz_tit" style="margin:10px 1% 0 1%;"><span class="ydExpand closeOrExpand"></span><span>产品基本信息</span></div>
		<div id="oneStepContent" class="mod_information_dzhan">
			<div class="mod_information_dzhan_d" style="margin-left: 10px;">

				<div class="kongr"></div>
				<div class="mod_information_d2"  style="width: 100%;">
					<div class="fl" style="width:55%">
						<label><span class="xing">*</span>产品名称：</label>
						<form:input path="acitivityName" cssClass="inputTxt"  maxlength="50"  cssStyle=" width: 65%"/>
						<span class="acitivityNameSizeSpan" style="color:#b2b2b2;">还可输入<span class="acitivityNameSize">50</span>个字</span>
					</div>
					<c:if test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' }">
						<div class="mod_information_d_tb fl" style="width:300px;">
							<label><span class="xing">*</span>团号标识：</label>
							<form:select path="groupNoMark" itemValue="key" itemLabel="value" class="valid" disabled="disabled">
								<form:option value="">请选择</form:option>
								<form:options items="${groupNoMarks}" />
							</form:select>
							<input id="groupNoName" class="valid" name="groupNoName" value="${travelActivity.groupNoMark }" type="text" style="width:50px;" readonly="readonly" />
						</div>
					</c:if>
				</div>
				<div class="kongr"></div>

				<div class="mod_information_d2">
					<label><span class="xing">*</span>出发城市：</label>
					<form:select path="fromArea" itemValue="key" itemLabel="value" class="required">
						<form:option value="">请选择</form:option>
						<form:options items="${fromAreas}" />
					</form:select>
				</div>
				<div class="mod_information_d2 ">
					<label>&nbsp;&nbsp;&nbsp;<span class="xing">*</span>目的地：</label>
					<%--<tags:treeselect id="targetArea" name="targetAreaIdList" value="${targetAreaIds}"  labelName="targetAreaNameList" labelValue="${targetAreaNames}"--%>
									 <%--title="区域" url="/activity/manager/filterTreeData1?kind=${travelActivity.activityKind}" cssClass="required targetArea_no_input" checked="true"/>--%>
					<div class="input-append">
						<input id="targetAreaId" name="targetAreaIdList" type="hidden" value="${targetAreaIds}"/>
						<input id="targetAreaName" name="targetAreaNameList" readonly="readonly" type="text" value="${targetAreaNames}"/>
						<a id="targetAreaButton" href="javascript:" class="btn" style="_padding-top:6px;" onclick="selectTargetArea()">&nbsp;选择&nbsp;</a>
					</div>
				</div>
				<div class="mod_information_d2 ">
					<label><span class="xing">*</span>游玩线路：</label>
					<form:select path="touristLineId" itemValue="key" itemLabel="value">
						<form:option value="">请选择</form:option>
						<form:options items="${touristLineMap}" />
						<form:option value="0">其他</form:option>
					</form:select>
				</div>
				<c:if test="${travelActivity.activityKind eq '10' }">
					<div class="mod_information_d2">
						<label><span class="xing">*</span>返回城市：</label>
						<form:select path="backArea" itemValue="key" itemLabel="value" class="required">
							<form:option value="">请选择</form:option>
							<form:options items="${fromAreas}" />
						</form:select>
					</div>
				</c:if>
				<c:if test="${travelActivity.activityKind ne '10' }">
					<div class="mod_information_d2">
						<label><span class="xing">*</span>交通方式：</label>
						<form:select path="trafficMode" itemValue="key" itemLabel="value" onchange="trafficchg()">
							<form:option value="">请选择</form:option>
							<form:options items="${trafficModes}"/>
						</form:select>
						<a target="_self" href="javascript:;" class="ml25" onclick="linkAirTicket1('${ctx}')" style="display: none">关联机票产品</a>

		            	<span class="linkAir" style="display: none">
							<span onclick="showAirInfo(this)" class="linkAir-spn" id="zhankaiguanlian">展开关联机票产品信息</span>
								<div class="airInfo-arrow" style="display: block;">
									<i></i>
								</div>
							<span>
							丨<span id="${travelActivity.activityAirTicket.productCode }" onclick="linkAirTicket('${ctx}',$(this).attr('id'))" class="changeAirSpan linkAir-spn" name="xiugaiguanlian">修改关联机票产品</span>
							丨<span onClick="cancelAirTicket()" class="linkAir-spn" id="quxiaoguanlian">取消关联机票产品</span>
							</span>
						</span>
					</div>
				</c:if>
				<div class="kongr"></div>
				<div class="mod_information_d2">
					<label>部门：</label><span>${deptName }</span>
				</div>
				<div class="airInfo" style="display: none;">
					<form:hidden path="activityAirTicket.id" cssClass="activityAirTicketId"/>
				</div>
				<div class="kongr"></div>

				<div class="mod_information_d2">
					<label>产品系列：</label>
					<form:select path="activityLevelId" itemValue="key" itemLabel="value" >
						<form:option value="">请选择</form:option>
						<form:options items="${productLevels}" />
					</form:select>
				</div>
				<div class="mod_information_d2">
					<div class="divDurationDays">
						<label for="spinner"  class="txt2 fl"><span class="xing">*</span>行程天数：</label>
						<input id="activityDuration" class="spinner_day" name="activityDuration" maxlength="3" value="${travelActivity.activityDuration }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
						<span style="padding-left:5px;">天</span>
					</div>
				</div>
				<div class="mod_information_d2">
					<label>领队：</label>
					<form:input path="groupLead" maxlength="20"/>
				</div>

				<div class="mod_information_d2 user_show">
					<label>可见用户：</label>
					<tags:treeselect id="opUser" name="opUserIdList" value="${opUserIds}"  labelName="opUserNameList" labelValue="${opUserNames}"
									 title="用户" url="/activity/manager/filterOpUserTreeData" cssClass="targetArea_no_input" checked="true"/>
				</div>
				<form:hidden path="activitySerNum"/>
				<div class="kongr"></div>
				<c:if test="${travelActivity.activityKind ne '10' }">
					<div class="mod_information_d2">
						<label>旅游类型：</label>
						<form:select path="travelTypeId" itemValue="key" itemLabel="value">
							<form:option value="">请选择</form:option>
							<form:options items="${travelTypes}" />
						</form:select>
					</div>

					<div class="mod_information_d2">
						<label>产品类型：</label>
						<form:select path="activityTypeId" itemValue="key" itemLabel="value" >
							<form:option value="">请选择</form:option>
							<form:options items="${productTypes}" />
						</form:select>
					</div>
					<div class="kongr"></div>
				</c:if>

				<div class="mod_information_d2  add-paytype">
					<label><span class="xing">*</span>付款方式：</label><font id="payModeText" style="display: none"></font>
					<input type="checkbox" class="ckb_mod" id="payMode_deposit" name="payMode" value="1" onclick="paychg(this)" <c:if test="${travelActivity.payMode_deposit==1}">checked="checked"</c:if>/><font>订金占位&#12288;&#12288;</font>
					<label for="spinner" class="txt2" id="label_deposit"><span class="xing
				" id="deposit_xing" style="display: none;">*</span>保留时限：</label>

				<span id="payMode_deposit_span" class="payModeSpan">
					<input id="remainDays_deposit" class="spinner" name="remainDays_deposit" maxlength="3"
						   <c:if test="${travelActivity.remainDays_deposit == '' || travelActivity.remainDays_deposit == null}">value="0"</c:if>
						   <c:if test="${travelActivity.remainDays_deposit != '' && travelActivity.remainDays_deposit != null}">value="${travelActivity.remainDays_deposit}"</c:if>
						   onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
					<span style="padding-left:5px;">天</span>
				</span>
				<span id="payMode_deposit_span" class="payModeSpan">
					<input id="remainDays_deposit_hour" class="spinner_hour" name="remainDays_deposit_hour" maxlength="2"
						   <c:if test="${travelActivity.remainDays_deposit_hour == '' || travelActivity.remainDays_deposit_hour == null}">value="0"</c:if>
						   <c:if test="${travelActivity.remainDays_deposit_hour != '' && travelActivity.remainDays_deposit_hour != null}">value="${travelActivity.remainDays_deposit_hour}"</c:if>
						   onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
					<span style="padding-left:5px;">时</span>
				</span>
				<span id="payMode_deposit_span" class="payModeSpan">
					<input id="remainDays_deposit_fen" class="spinner_fen" name="remainDays_deposit_fen" maxlength="2"
						   <c:if test="${travelActivity.remainDays_deposit_fen == '' || travelActivity.remainDays_deposit_fen == null}">value="0"</c:if>
						   <c:if test="${travelActivity.remainDays_deposit_fen != '' && travelActivity.remainDays_deposit_fen != null}">value="${travelActivity.remainDays_deposit_fen}"</c:if>
						   onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
					<span style="padding-left:5px;">分</span>
				</span>

					<br />
					<label>&nbsp;</label>
					<input type="checkbox" class="ckb_mod" id="payMode_advance" name="payMode" value="2" onclick="paychg(this)" <c:if test="${travelActivity.payMode_advance==1}">checked="checked"</c:if>/><font>预占位&#12288;&#12288;&#12288;</font>
					<label for="spinner" class="txt2" id="label_advance"><span class="xing" id="advance_xing" style="display: none;">*</span>保留时限：</label>

				<span id="payMode_advance_span" class="payModeSpan">
					<input id="remainDays_advance" class="spinner" name="remainDays_advance" maxlength="3"
						   <c:if test="${travelActivity.remainDays_advance == '' || travelActivity.remainDays_advance == null}">value="0"</c:if>
						   <c:if test="${travelActivity.remainDays_advance != '' && travelActivity.remainDays_advance != null}">value="${travelActivity.remainDays_advance}"</c:if>
						   onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
					<span style="padding-left:5px;">天</span>
				</span>
				<span id="payMode_advance_span" class="payModeSpan">
					<input id="remainDays_advance_hour" class="spinner_hour" name="remainDays_advance_hour" maxlength="2"
						   <c:if test="${travelActivity.remainDays_advance_hour == '' || travelActivity.remainDays_advance_hour == null}">value="0"</c:if>
						   <c:if test="${travelActivity.remainDays_advance_hour != '' && travelActivity.remainDays_advance_hour != null}">value="${travelActivity.remainDays_advance_hour}"</c:if>
						   onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
					<span style="padding-left:5px;">时</span>
				</span>
				<span id="payMode_advance_span" class="payModeSpan">
					<input id="remainDays_advance_fen" class="spinner_fen" name="remainDays_advance_fen" maxlength="2"
						   <c:if test="${travelActivity.remainDays_advance_fen == '' || travelActivity.remainDays_advance_fen == null}">value="0"</c:if>
						   <c:if test="${travelActivity.remainDays_advance_fen != '' && travelActivity.remainDays_advance_fen != null}">value="${travelActivity.remainDays_advance_fen}"</c:if>
						   onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
					<span style="padding-left:5px;">分</span>
				</span>

					<br class="space"/>
					<c:if test="${travelActivity.activityKind eq '2' or travelActivity.activityKind eq '10'}">
						<label class="space">&nbsp;</label>
						<input type="checkbox" class="ckb_mod" id="payMode_data" name="payMode" value="4" onclick="paychg(this)" <c:if test="${travelActivity.payMode_data==1}">checked="checked"</c:if>/><font>资料占位&#12288;&#12288;</font>
						<label for="spinner" class="txt2" id="label_data"><span class="xing" id="data_xing" style="display: none;">*</span>保留时限：</label>
					<span id="payMode_data_span" class="payModeSpan">
						<input id="remainDays_data" class="spinner" name="remainDays_data" maxlength="3"
							   <c:if test="${travelActivity.remainDays_data == '' || travelActivity.remainDays_data == null}">value="0"</c:if>
							   <c:if test="${travelActivity.remainDays_data != '' && travelActivity.remainDays_data != null}">value="${travelActivity.remainDays_data}"</c:if>
							   onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
						<span style="padding-left:5px;">天</span>
					</span>
					<span id="payMode_data_span" class="payModeSpan">
						<input id="remainDays_data_hour" class="spinner_hour" name="remainDays_data_hour" maxlength="2"
							   <c:if test="${travelActivity.remainDays_data_hour == '' || travelActivity.remainDays_data_hour == null}">value="0"</c:if>
							   <c:if test="${travelActivity.remainDays_data_hour != '' && travelActivity.remainDays_data_hour != null}">value="${travelActivity.remainDays_data_hour}"</c:if>
							   onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
						<span style="padding-left:5px;">时</span>
					</span>
					<span id="payMode_data_span" class="payModeSpan">
						<input id="remainDays_data_fen" class="spinner_fen" name="remainDays_data_fen" maxlength="2"
							   <c:if test="${travelActivity.remainDays_data_fen == '' || travelActivity.remainDays_data_fen == null}">value="0"</c:if>
							   <c:if test="${travelActivity.remainDays_data_fen != '' && travelActivity.remainDays_data_fen != null}">value="${travelActivity.remainDays_data_fen}"</c:if>
							   onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
						<span style="padding-left:5px;">分</span>
					</span>

						<br class="space"/>
						<label class="space">&nbsp;</label>
						<input type="checkbox" class="ckb_mod" id="payMode_guarantee" name="payMode" value="5" onclick="paychg(this)" <c:if test="${travelActivity.payMode_guarantee==1}">checked="checked"</c:if>/><font>担保占位&#12288;&#12288;</font>
						<label for="spinner" class="txt2" id="label_guarantee"><span class="xing" id="guarantee_xing" style="display: none;">*</span>保留时限：</label>
					<span id="payMode_guarantee_span" class="payModeSpan">
						<input id="remainDays_guarantee" class="spinner" name="remainDays_guarantee" maxlength="3"
							   <c:if test="${travelActivity.remainDays_guarantee == '' || travelActivity.remainDays_guarantee == null}">value="0"</c:if>
							   <c:if test="${travelActivity.remainDays_guarantee != '' && travelActivity.remainDays_guarantee != null}">value="${travelActivity.remainDays_guarantee}"</c:if>
							   onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
						<span style="padding-left:5px;">天</span>
					</span>
					<span id="payMode_guarantee_span" class="payModeSpan">
						<input id="remainDays_guarantee_hour" class="spinner_hour" name="remainDays_guarantee_hour" maxlength="2"
							   <c:if test="${travelActivity.remainDays_guarantee_hour == '' || travelActivity.remainDays_guarantee_hour == null}">value="0"</c:if>
							   <c:if test="${travelActivity.remainDays_guarantee_hour != '' && travelActivity.remainDays_guarantee_hour != null}">value="${travelActivity.remainDays_guarantee_hour}"</c:if>
							   onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
						<span style="padding-left:5px;">时</span>
					</span>
					<span id="payMode_guarantee_span" class="payModeSpan">
						<input id="remainDays_guarantee_fen" class="spinner_fen" name="remainDays_guarantee_fen" maxlength="2"
							   <c:if test="${travelActivity.remainDays_guarantee_fen == '' || travelActivity.remainDays_guarantee_fen == null}">value="0"</c:if>
							   <c:if test="${travelActivity.remainDays_guarantee_fen != '' && travelActivity.remainDays_guarantee_fen != null}">value="${travelActivity.remainDays_guarantee_fen}"</c:if>
							   onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
						<span style="padding-left:5px;">分</span>
					</span>

						<br class="space"/>
						<label class="space">&nbsp;</label>
						<input type="checkbox" class="ckb_mod" id="payMode_express" name="payMode" value="6" onclick="paychg(this)" <c:if test="${travelActivity.payMode_express==1}">checked="checked"</c:if>/><font>确认单占位&#12288;</font>
						<label for="spinner" class="txt2" id="label_express"><span class="xing" id="express_xing" style="display: none;">*</span>保留时限：</label>
					<span id="payMode_express_span" class="payModeSpan">
						<input id="remainDays_express" class="spinner" name="remainDays_express" maxlength="3"
							   <c:if test="${travelActivity.remainDays_express == '' || travelActivity.remainDays_express == null}">value="0"</c:if>
							   <c:if test="${travelActivity.remainDays_express != '' && travelActivity.remainDays_express != null}">value="${travelActivity.remainDays_express}"</c:if>
							   onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
						<span style="padding-left:5px;">天</span>
					</span>
					<span id="payMode_express_span" class="payModeSpan">
						<input id="remainDays_express_hour" class="spinner_hour" name="remainDays_express_hour" maxlength="2"
							   <c:if test="${travelActivity.remainDays_express_hour == '' || travelActivity.remainDays_express_hour == null}">value="0"</c:if>
							   <c:if test="${travelActivity.remainDays_express_hour != '' && travelActivity.remainDays_express_hour != null}">value="${travelActivity.remainDays_express_hour}"</c:if>
							   onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
						<span style="padding-left:5px;">时</span>
					</span>
					<span id="payMode_express_span" class="payModeSpan">
						<input id="remainDays_express_fen" class="spinner_fen" name="remainDays_express_fen" maxlength="2"
							   <c:if test="${travelActivity.remainDays_express_fen == '' || travelActivity.remainDays_express_fen == null}">value="0"</c:if>
							   <c:if test="${travelActivity.remainDays_express_fen != '' && travelActivity.remainDays_express_fen != null}">value="${travelActivity.remainDays_express_fen}"</c:if>
							   onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
						<span style="padding-left:5px;">分</span>
					</span>
						<br class="space"/>
					</c:if>
					<label class="space">&nbsp;</label>
					<input type="checkbox" class="ckb_mod" id="payMode_full" name="payMode" value="3" <c:if test="${travelActivity.payMode_full==1}">checked="checked"</c:if>/><font>全款支付</font>

					<br class="space"/>
					<label class="space">&nbsp;</label>
					<input type="checkbox" class="ckb_mod" id="payMode_op" name="payMode" value="7" <c:if test="${travelActivity.payMode_op==1}">checked="checked"</c:if>/><font>计调确认占位</font>

					<!-- wangxinwei added 20151111,添加，财务确认占位，对应需求号 C362 -->
					<c:if test="${fns:getUser().company.orderPayMode eq 1}">
						<br class="space"/>
						<label class="space">&nbsp;</label>
						<input type="checkbox" class="ckb_mod" id="payMode_cw" name="payMode" value="8" <c:if test="${travelActivity.payMode_cw==1}">checked="checked"</c:if>/><font>财务确认占位</font>
					</c:if>

				</div>
				<div class="kongr"></div>
			</div>
		</div>
		<div id="oneModBtn" class="mod_information_d5">
			<div class="ydBtn"><a class="ydbz_x" href="javascript:void(0)" onclick="oneStepMod()">修改</a></div>
		</div>
		<div id="oneSaveBtn" class="add3_information_d5">
			<div class="ydBtn"><a class="ydbz_x" href="javascript:void(0)" onclick="oneStepSave()">保存</a></div>
		</div>

		<c:set var="groupsize" value="${fn:length(travelActivity.activityGroups)}"></c:set>
		<c:if test="${travelActivity.activityKind eq '10' }">
			<select id="spaceType" name="spaceType" style="display:none">
				<c:forEach items="${fns:getDictList('cruise_type')}" var="dict">
					<option value="${dict.value }">${dict.label }</option>
				</c:forEach>
			</select>
		</c:if>
		<div id="secondStepTitle" style="margin:10px 1% 0 1%;"class=" ydbz_tit"><span class="ydExpand closeOrExpand"></span><span>现有团期修改</span></div>
		<div id="secondStepContent">
			<div id="secondStepMod">
				<c:if test="${groupsize ne 0}">
					<div class="discount-setting-container tr">
						<c:if test="${travelActivity.activityKind eq '2' && (fns:getUser().company.uuid eq '7a81c5d777a811e5bc1e000c29cf2586'
                         		|| fns:getUser().company.uuid eq '75895555346a4db9a96ba9237eae96a5') }">
							<input class="btn btn-primary" onclick="jbox__discount_setting_pop_fab_mod();" value="修改优惠额度" type="button">
						</c:if>
						<!--仅为展示效果，后端需要适当判断进行提示，-->
						<!--<input class="btn btn-primary" onclick="jbox__nosel_group_discount_setting_pop_pop_fab();" value="未选择团期设置优惠额度" type="button">-->
					</div>
					<table id="modTable" class="table table-striped table-bordered table-condensed table-mod2-group" style="width:98%;margin:10px auto;">
						<thead>
						<tr>
							<c:if test="${travelActivity.activityKind eq '2'}">
								<c:if test="${fns:getUser().company.uuid eq '7a81c5d777a811e5bc1e000c29cf2586' || fns:getUser().company.uuid eq '75895555346a4db9a96ba9237eae96a5' }">
									<th width="4%" rowspan="2">全选
										<input class="none-height-input" name="allChks" onclick="checkall_news(this)" type="checkbox"></th>
								</c:if>
								<%--<shiro:hasPermission name="calendarLoose:book:order">									--%>
								<%--<th rowspan="2" width="2%">推荐</th>--%>
								<%--</shiro:hasPermission>--%>
							</c:if>
							<c:if test="${fns:getUser().company.uuid ne '7a81b21a77a811e5bc1e000c29cf2586' }">
								<th rowspan="2" width="7%">团号</th>
							</c:if>
							<!-- 因bug#13135,对于越柬和名扬批发商,也展示团号 -s-->
							<c:if test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' }">
								<th rowspan="2" width="6%">团号</th>
							</c:if>
							<th rowspan="2" width="6%">出团日期</th>
							<th rowspan="2" width="6%">截团日期</th>
							<!-- 因bug#13135,对于越柬和名扬批发商,也展示团号 -e-->
							<th rowspan="2" width="6%">应付账期</th>
							<!-- 因bug#13135,对于越柬和名扬批发商,也展示团号 -e-->
							<th rowspan="2" width="6%">签证国家</th>
							<th rowspan="2" width="6%">资料截止日期</th>
							<c:set var="colspanNum" value="3"></c:set>
							<c:if test="${travelActivity.activityKind eq '10' }">
								<th rowspan="2" width="4%">舱型</th>
								<c:set var="colspanNum" value="2"></c:set>
							</c:if>
							<shiro:hasPermission name="price:project">
								<th rowspan="2" width="9%">酒店房型</th>
							</shiro:hasPermission>
							<th width="11%" class="t-th2" colspan="${colspanNum }">同行价</th>
								<%--<th width="11%" class="t-th2" colspan="2">trekiz价</th>--%>
							<c:if test="${travelActivity.activityKind eq '2' or travelActivity.activityKind eq '10'}">
								<th width="11%" class="t-th2" colspan="${colspanNum }">
									<%-- <shiro:hasPermission name="looseProduct:operation:requiredStraightPrice">
										<span class="xing">*</span>
									</shiro:hasPermission> --%>
									直客价
								</th>
							</c:if>

							<!--  t1t2    打通 -->
								<%--<c:if test="${travelActivity.activityKind eq '2' and isT1_flag eq '1' and pricingStrategy_flag eq '1' }">--%>
								<%--<th width="11%" class="t-th2" colspan="${colspanNum }">QUAUQ渠道策略</th>--%>
								<%--<th width="11%" class="t-th2" colspan="${colspanNum }">供应价<br>（含服务费）</th>--%>
								<%--</c:if>--%>

							<c:if test="${travelActivity.activityKind ne '10' }">
								<th rowspan="2" width="3%">儿童最高人数</th>
								<th rowspan="2" width="3%">特殊人群最高人数</th>
							</c:if>
							<th rowspan="2" width="3%">订金</th>
							<th rowspan="2" width="3%">单房差</th>
							<th rowspan="2" width="4%">预收<c:if test="${travelActivity.activityKind eq '10'}">/间</c:if></th>
							<th rowspan="2" width="4%">余位<c:if test="${travelActivity.activityKind eq '10'}">/间</c:if></th>
							<!-- 0258 懿洋假期 发票税 -->
							<c:if test="${fns:getUser().company.uuid eq 'f5c8969ee6b845bcbeb5c2b40bac3a23' }">
								<th rowspan="2" width="4%">发票税</th>
							</c:if>
							<th rowspan="2" width="7%">操作</th>
						</tr>
						<tr>
							<c:choose>
								<c:when test="${travelActivity.activityKind eq '10' }">
									<th>1/2人</th>
									<th>3/4人</th>
									<th>1/2人</th>
									<th>3/4人</th>
								</c:when>
								<c:otherwise>
									<th>成人</th>
									<th>儿童</th>
									<th>特殊人群</th>
									<c:if test="${travelActivity.activityKind eq '2'}">
										<th>成人</th>
										<th>儿童</th>
										<th>特殊人群</th>
									</c:if>

									<!--  t1t2    打通 -->
									<%--<c:if test="${travelActivity.activityKind eq '2' and isT1_flag eq '1' and pricingStrategy_flag eq '1' }">--%>
									<%--<th>成人</th>--%>
									<%--<th>儿童</th>--%>
									<%--<th>特殊人群</th>--%>
									<%--<th>成人</th>--%>
									<%--<th>儿童</th>--%>
									<%--<th>特殊人群</th>--%>
									<%--</c:if>--%>


								</c:otherwise>
							</c:choose>
						</tr>
						</thead>
						<tbody>
						<c:forEach items="${travelActivity.activityGroupList}" var="group" varStatus="s2">
							<tr id="tr_${s2.count}">
								<c:if test="${travelActivity.activityKind eq '2'}">
									<c:if test="${fns:getUser().company.uuid eq '7a81c5d777a811e5bc1e000c29cf2586' || fns:getUser().company.uuid eq '75895555346a4db9a96ba9237eae96a5' }">
										<td><input class="none-height-input" name="idss"  type="checkbox"></td>
									</c:if>
									<%--<shiro:hasPermission name="calendarLoose:book:order">--%>
									<%--<td>--%>
									<%--<input type="checkbox" name="recommend" id="recommend${s2.count }" value="${s2.count - fn:length(travelActivity.activityGroups) - 1}" <c:if test="${not empty group.recommend and group.recommend == 1 }">checked="checked"</c:if> >--%>
									<%--</td>--%>
									<%--</shiro:hasPermission>--%>
								</c:if>
								<c:choose>
									<c:when test="${fns:getUser().company.uuid eq '7a45838277a811e5bc1e000c29cf2586' || fns:getUser().company.uuid eq '7a81a03577a811e5bc1e000c29cf2586' || fns:getUser().company.uuid eq 'f5c8969ee6b845bcbeb5c2b40bac3a23' || fns:getUser().company.uuid eq '1d4462b514a84ee2893c551a355a82d2' || fns:getUser().company.uuid eq '7a81c5d777a811e5bc1e000c29cf2586' || fns:getUser().company.uuid eq '5c05dfc65cd24c239cd1528e03965021' || fns:getUser().company.uuid == '58a27feeab3944378b266aff05b627d2' || (fns:getUser().company.groupCodeRuleDT==0 && fns:getUser().company.uuid != '7a8177e377a811e5bc1e000c29cf2586' && fns:getUser().company.uuid != 'ed88f3507ba0422b859e6d7e62161b00')}"><!-- && fns:getUser().company.uuid != '7a45838277a811e5bc1e000c29cf2586'-->
										<!-- 大洋、非常国际、起航假期 团号可修改 -->
										<!-- bug12826  新增舱型的团号修改时同步-->
										<td>
											<div class="withdraw-relative" >
												<c:if test="${shelfRightsStatus == 0}">
													<c:choose>
														<c:when test="${group.isT1 == 1}">
															<em class="grounding-hover">已上架旅游交易系统</em>
															<em class="g-w grounding"></em>
														</c:when>
														<c:otherwise>
															<!--团期处于未上架状态时，不显示状态标签-->
															<c:choose>
																<c:when test="${not empty group.pricingStrategy.adultPricingStrategy || not empty group.pricingStrategy.childrenPricingStrategy || not empty group.pricingStrategy.specialPricingStrategy}">
																	<em class="withdraw-hover grounding-one" >已下架旅游交易系统</em>
																	<em class="g-w withdraw grounding-two"></em>
																	<%--<input type="checkbox" class="tableCheckBox" name="groupNo"/>--%>
																</c:when>
																<c:otherwise>
																	<em class="grounding-one"></em>
																	<em class="g-w grounding-two"></em>
																	<input type="checkbox" class="tableCheckBox" name="groupNo" disabled="disabled" style="display: none"/>
																</c:otherwise>
															</c:choose>
														</c:otherwise>
													</c:choose>
												</c:if>

												<input type="text" name="groupCode" id="groupCode${s2.count }" maxlength="50" src="${group.id}" onchange="asynchangeGroupCode(this);"  onafterpaste="replaceStr(this);" onkeyup="validateLong(this);asynchangeGroupCode(this);"  value="${group.groupCode}"/>
												<input type="hidden" name="idss" id="${group.id}"  src="${group.id}"  maxlength="50"  value="${group.groupCode}"/>
												<input type="hidden" name="serial" value="${s2.count }"	/>
												<input type="hidden" name="isT1" id="${group.isT1}" src="${group.id}" value="${group.groupCode }"/>

											</div>
										</td>
									</c:when>
									<c:when test="${fns:getUser().company.uuid ne '7a81b21a77a811e5bc1e000c29cf2586' && fns:getUser().company.uuid != 'f5c8969ee6b845bcbeb5c2b40bac3a23' && fns:getUser().company.uuid ne '7a81a03577a811e5bc1e000c29cf2586' && fns:getUser().company.uuid ne '1d4462b514a84ee2893c551a355a82d2' && fns:getUser().company.uuid ne '7a81c5d777a811e5bc1e000c29cf2586' && fns:getUser().company.uuid ne '5c05dfc65cd24c239cd1528e03965021' && fns:getUser().company.uuid != '58a27feeab3944378b266aff05b627d2' && (fns:getUser().company.groupCodeRuleDT==1 || fns:getUser().company.uuid == '7a8177e377a811e5bc1e000c29cf2586' || fns:getUser().company.uuid == 'ed88f3507ba0422b859e6d7e62161b00' || fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' || fns:getUser().company.uuid == '7a45838277a811e5bc1e000c29cf2586')}">
										<!-- 对应需求 c460 fns:getUser().company.groupCodeRuleDT==0 部分 -->
										<td>
											<div class="withdraw-relative" >
												<c:if test="${shelfRightsStatus == 0}">
													<c:choose>
														<c:when test="${group.isT1 == 1}">
															<em class="grounding-hover">已上架旅游交易系统</em>
															<em class="g-w grounding"></em>
														</c:when>
														<c:otherwise>
															<!--团期处于未上架状态时，不显示状态标签-->
															<c:choose>
																<c:when test="${not empty group.pricingStrategy.adultPricingStrategy || not empty group.pricingStrategy.childrenPricingStrategy || not empty group.pricingStrategy.specialPricingStrategy}">
																	<em class="withdraw-hover grounding-one" >已下架旅游交易系统</em>
																	<em class="g-w withdraw grounding-two"></em>
																	<%--<input type="checkbox" class="tableCheckBox" name="groupNo"/>--%>
																</c:when>
																<c:otherwise>
																	<em class="grounding-one"></em>
																	<em class="g-w grounding-two"></em>
																	<input type="checkbox" class="tableCheckBox" name="groupNo" disabled="disabled" style="display: none"/>
																</c:otherwise>
															</c:choose>
														</c:otherwise>
													</c:choose>
												</c:if>
												<input type="hidden" name="groupCode" maxlength="50" value="${group.groupCode}"/>
												<span class="" title="${group.groupCode}">${group.groupCode}</span><%--bug16405 团号需折行显示，去掉class=ellipsis-number-team--%>
												<input type="hidden" name="idss" id="${group.id}"   maxlength="50"  value="${group.groupCode}"/>
												<input type="hidden" name="serial" value="${s2.count }"	/>
												<input type="hidden" name="isT1" id="${group.isT1}" src="${group.id}" value="${group.groupCode }"/>
											</div>
										</td>
									</c:when>
									<c:otherwise>
										<!-- 因为bug13135,产品决定应付账期和团号都进行展示 -->
										<c:if test="${fns:getUser().company.uuid ne '282f94197bc711e5aef0000c294db7c6'}">
											<td>
												<span>${group.groupCode}</span>
												<input type="hidden" name="groupCode" value="${group.groupCode}" maxlength="50"  src="${group.id}"  onafterpaste="replaceStr(this)" onKeyUp="validateLong(this)"/>
												<input type="hidden" name="idss" id="${group.id}"    src="${group.id}"  value="${group.groupCode}"/>
												<input type="hidden" name="serial" value="${s2.count }"	/>
												<input type="hidden" name="isT1" id="${group.isT1}" src="${group.id}" value="${group.groupCode }"/>
											</td>
										</c:if>
									</c:otherwise>
								</c:choose>
								<td class="tc">
									<input type="hidden" id="ist1${s2.count}" name="ist1" value="${group.isT1}" ></input>
									<input type="hidden" id="groupid${s2.count}" name="groupid" value="${group.id}" ></input>
									<!-- bug12826  新增舱型的团号修改时同步-->
									<input type="text" name="groupOpenDate" id="groupOpenDateId${s2.count}" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/>" onClick="WdatePicker()" onchange="syncGroupCode(this);" class="inputTxt required"/> </td>


								<td class="tc"><input type="text" name="groupCloseDate" class="inputTxt" id="groupCloseDateId${s2.count}"
													  value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupCloseDate}"/>"
													  onClick="WdatePicker({maxDate:takeOrderOpenDate(this)})"/>
								</td>


								<td class="tc"><input type="text" name="yingFuDate" class="inputTxt" id="yingFuDate${s2.count}"
													  value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.payableDate}"/>" />
								</td>
								<td><input type="text" name="visaCountry" id="visaCountry${s2.count }" value="${group.visaCountry}" /></td>
								<td class="tc"><input type="text" name="visaDate" id="visaDate${s2.count}" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate }"/>" onClick="WdatePicker({maxDate:takeModVisaDate(this)})" /></td>
								<c:if test="${travelActivity.activityKind eq '10' }">
									<td>
										<select id="spaceType" name="spaceType">
											<c:forEach items="${fns:getDictList('cruise_type')}" var="dict">
												<option value="${dict.value }" <c:if test="${dict.value eq group.spaceType }">selected="selected"</c:if> >${dict.label }</option>
											</c:forEach>
										</select>
											<%-- 												<span>${fns:getDictLabel(group.spaceType, "out_area", "-") }</span> --%>
									</td>
								</c:if>

								<!-- 团期酒店房型 -->
								<shiro:hasPermission name="price:project">
									<td name="hotelhouse">
										<c:forEach var="hotelhouse" items="${group.hotelHouseList }" varStatus="status">
											<div  name="hotelhouseDiv">
												<label>酒店：</label>
												<input width="4%" type="text" name="groupHotel" value="${hotelhouse.hotel }" maxlength="50" class="valid disabledClass" style="display:none; width:42px; padding-left:2px; padding-right:2px; margin-left:1px;">
												<span class="disabledshowspan">${hotelhouse.hotel }</span>&nbsp;
												<label>房型：</label>
												<input width="4%" type="text" name="groupHouseType" value="${hotelhouse.houseType }" maxlength="50" class="valid disabledClass" style="display:none; width:42px; padding-left:2px; padding-right:2px; margin-left:1px;">
												<span class="disabledshowspan">${hotelhouse.houseType }</span>
												<em class="add-select" name="addSelect_${s2.count}" onclick="addHotelAndHouseType(this)" style="height:19px;"></em>
												<c:if test="${status.count > 1}">
													<em class='remove-selected' name="removeSelect_${s2.count}" onclick="delHotelAndHouseType(this)" style='display:inline-block;height:19px;'></em>
												</c:if>
											</div>
										</c:forEach>
									</td>
								</shiro:hasPermission>

								<td class="tr">
									<!-- t1t2   打通  添加   -->
									<input type="text" name="settlementAdultPrice" id="settlementAdultPrice" onchange="changedNumber();" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)"
									<c:if test='${group.settlementAdultPrice eq 0}'>

									</c:if>
									<c:if test='${group.settlementAdultPrice ne 0}'>
										   value='<fmt:formatNumber value="${group.settlementAdultPrice}" pattern="##0.00"/>'
									</c:if>
										   class="ipt-currency" maxlength="14" ><!-- bug13184条件放宽长度由8变为10,阻止报错 -->
								</td>

								<!-- t1t2   打通  添加  -->
								<td class="tr"><input type="text" name="settlementcChildPrice" onchange="changedNumber();" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)" id="settlementcChildPrice${s2.count}" value="<c:if test="${group.settlementcChildPrice eq 0}"></c:if><c:if test="${group.settlementcChildPrice ne 0}"><fmt:formatNumber value="${group.settlementcChildPrice}" pattern="##0.00"/></c:if>"  class="ipt-currency" maxlength="14"  /></td><!-- bug13184条件放宽长度由8变为10,阻止报错 -->
								<c:if test="${travelActivity.activityKind ne '10' }">
									<!-- t1t2   打通  添加  -->
									<td class="tr"><input type="text" name="settlementSpecialPrice" id="settlementSpecialPrice${s2.count}" onchange="changedNumber();" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)" value="<c:if test="${group.settlementSpecialPrice eq 0}"></c:if><c:if test="${group.settlementSpecialPrice ne 0}"><fmt:formatNumber value="${group.settlementSpecialPrice}" pattern="##0.00"/></c:if>"  class="ipt-currency" maxlength="14" /></td><!-- bug13184条件放宽长度由8变为10,阻止报错 -->
								</c:if>
									<%--<td><input type="text" name="trekizPrice" id="trekizPrice${s2.count}" value="<c:if test="${group.trekizPrice eq 0}"></c:if><c:if test="${group.trekizPrice ne 0}">${group.trekizPrice}</c:if>" class="${fns:getCurrencyInfo(group.currencyType,0,'style')}" maxlength="8"></td>
										<td><input type="text" name="trekizChildPrice" id="trekizChildPrice${s2.count}" value="<c:if test="${group.trekizChildPrice eq 0}"></c:if><c:if test="${group.trekizChildPrice ne 0}">${group.trekizChildPrice}</c:if>"  class="${fns:getCurrencyInfo(group.currencyType,0,'style')}" maxlength="8"/></td>
										--%>
								<c:if test="${travelActivity.activityKind eq '2' or travelActivity.activityKind eq '10'}">
									<td class="tr"><input type="text" name="suggestAdultPrice" id="suggestAdultPrice${s2.count}" onchange="changedNumber();" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)" value="<c:if test="${group.suggestAdultPrice eq 0}"></c:if><c:if test="${group.suggestAdultPrice ne 0}"><fmt:formatNumber value="${group.suggestAdultPrice}" pattern="##0.00"/></c:if>"  class="ipt-currency" maxlength="14" /></td><!-- 原来maxlength=8,因为bug,所以放开条件为10 -->
									<td class="tr"><input type="text" name="suggestChildPrice" id="suggestChildPrice${s2.count}" onchange="changedNumber();" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)" value="<c:if test="${group.suggestChildPrice eq 0}"></c:if><c:if test="${group.suggestChildPrice ne 0}"><fmt:formatNumber value="${group.suggestChildPrice}" pattern="##0.00"/></c:if>"  class="ipt-currency" maxlength="14" /></td><!-- bug13184条件放宽长度由8变为10,阻止报错 -->
									<c:if test="${travelActivity.activityKind eq '2' }">
										<td class="tr"><input type="text" name="suggestSpecialPrice" id="suggestSpecialPrice${s2.count}" onchange="changedNumber();" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)"  value="<c:if test="${group.suggestSpecialPrice eq 0}"></c:if><c:if test="${group.suggestSpecialPrice ne 0}"><fmt:formatNumber value="${group.suggestSpecialPrice}" pattern="##0.00"/></c:if>"  class="ipt-currency" maxlength="14"/></td><!-- bug13184条件放宽长度由8变为10,阻止报错 -->
									</c:if>
									<c:if test="${travelActivity.activityKind ne '10' }">
										<td class="tr"><input type="text" name="maxChildrenCount" id="maxChildrenCount${s2.count}"  onafterpaste="this.value=this.value.replace(/\D|^0.+/g,'')" onkeyup="this.value=this.value.replace(/\D|^0.+/g,'')" maxlength="8" value="${group.maxChildrenCount}"/><input type="hidden" id="nopayChildrenPosition${s2.count}" name="nopayChildrenPosition" value="${group.nopayChildrenPosition}"></td>
										<td class="tr"><input type="text" name="maxPeopleCount" id="maxPeopleCount${s2.count}"  onafterpaste="this.value=this.value.replace(/\D|^0.+/g,'')" onkeyup="this.value=this.value.replace(/\D|^0.+/g,'')" maxlength="8" value="${group.maxPeopleCount}"/><input type="hidden" id="nopayPeoplePosition${s2.count}" name="nopayPeoplePosition" value="${group.nopayPeoplePosition}"></td>
									</c:if>
									<td class="tr"><input type="text" name="payDeposit" id="payDeposit${s2.count}" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)" value="<c:if test="${group.payDeposit eq 0}"></c:if><c:if test="${group.payDeposit ne 0}">${group.payDeposit}</c:if>"  class="ipt-currency" maxlength="10"/></td><!-- bug13184条件放宽长度由8变为10,阻止报错 -->
									<td class="tr"><input type="text" name="singleDiff" id="singleDiff${s2.count}" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)" value="<c:if test="${group.singleDiff eq 0}"></c:if><c:if test="${group.singleDiff ne 0}">${group.singleDiff}</c:if>"  class="ipt-currency" maxlength="10"/></td><!-- bug13184条件放宽长度由8变为10,阻止报错 -->
								</c:if>
								<c:if test="${travelActivity.activityKind ne '2' and travelActivity.activityKind ne '10'}">
									<td class="tr"><input type="text" name="maxChildrenCount" id="maxChildrenCount${s2.count}"  onafterpaste="this.value=this.value.replace(/\D|^0.+/g,'')" onkeyup="this.value=this.value.replace(/\D|^0.+/g,'')" maxlength="8" value="${group.maxChildrenCount}"/><input type="hidden" id="nopayChildrenPosition${s2.count}" name="nopayChildrenPosition" value="${group.nopayChildrenPosition}"></td>
									<td class="tr"><input type="text" name="maxPeopleCount" id="maxPeopleCount${s2.count}"  onafterpaste="this.value=this.value.replace(/\D|^0.+/g,'')" onkeyup="this.value=this.value.replace(/\D|^0.+/g,'')" maxlength="8" value="${group.maxPeopleCount}"/><input type="hidden" id="nopayPeoplePosition${s2.count}" name="nopayPeoplePosition" value="${group.nopayPeoplePosition}"></td>
									<td class="tr"><input type="text" name="payDeposit" id="payDeposit${s2.count}" value="<c:if test="${group.payDeposit eq 0}"></c:if><c:if test="${group.payDeposit ne 0}">${group.payDeposit}</c:if>"  class="ipt-currency" maxlength="10"/></td><!-- bug13184条件放宽长度由8变为10,阻止报错 -->
									<td class="tr"><input type="text" name="singleDiff" id="singleDiff${s2.count}" value="<c:if test="${group.singleDiff eq 0}"></c:if><c:if test="${group.singleDiff ne 0}">${group.singleDiff}</c:if>"  class="ipt-currency" maxlength="10"/></td><!-- bug13184条件放宽长度由8变为10,阻止报错 -->
								</c:if>
								<td class="tr"><input type="text" name="planPosition" id="planPosition${s2.count}" value="${group.planPosition}" maxlength="3" class="required"/></td>
								<td class="tr"><input type="text" name="freePosition" id="freePosition${s2.count}" value="${group.freePosition}" maxlength="3" class="required"/></td>
								<!-- 0258 懿洋假期 发票税 -->
								<c:if test="${fns:getUser().company.uuid eq 'f5c8969ee6b845bcbeb5c2b40bac3a23'}">
									<td class="tr"><input type="text" style="width:60px" name="invoiceTax" id="invoiceTax${s2.count}" value="${group.invoiceTax==null?0:group.invoiceTax}"  onkeyup="checkValue(this);" onafterpaste="checkValue(this);"/>%</td>
								</c:if>
								<td  class="opera-new" name="targetHere">
									<input type="hidden" value="${group.openDateFile}" name="openDateFile" />
									<input type="hidden" value="${group.currencyType }" name="groupCurrencyType">
									<span onclick="modify(this);">修改</span>
									<span onclick="saveMod(this);" name="hideBtn" style="display:none;">保存</span>
									<span onclick="cancelMod(this);" name="hideBtn" style="display:none;color:red;">取消</span>
									<span onclick="delmodgroup(this);" name="deleteBtn">删除</span>
									<%--<c:if test="${group.isT1 == 0 }">--%>
										<%--<span class="stack-btn" onclick="grounding('${group.id}',this)">平台上架</span>--%>
									<%--</c:if>--%>
									<%--<c:if test="${group.isT1 != 0 }">--%>
										<%--<span style="display:none" class="stack-btn" onclick="grounding('${group.id}',this)">平台上架</span>--%>
									<%--</c:if>--%>
									<%--<c:if test="${hasPricingStrategyPermission != 0}">--%>
										<%--<span onclick="setPricingStrategy(this,${group.id},${ group.srcActivityId},'${acitivityName }','${group.pricingStrategy.alertFlag}')" class="setting-btn">设定定价策略</span>--%>
									<%--</c:if>--%>
									<%--<c:if test="${travelActivity.activityKind eq '2' and isT1_flag eq '1' and pricingStrategy_flag eq '1' }">--%>
										<%--<div  name="more-price"  style="display:inline-block;position:relative;">--%>
											<%--更多价格--%>
											<%--<div class="mp-floating" style="display:none">--%>
												<%--<span></span>--%>
												<%--<table width="100%" class="table_fixed border_t">--%>
													<%--<thead >--%>
													<%--<tr>--%>
														<%--<th colspan="2" width="50%">QUAUQ策略</th>--%>
														<%--<th colspan="2" width="50%">供应价</th>--%>
													<%--</tr>--%>
													<%--</thead>--%>
													<%--<tbody>--%>
													<%--<tr>--%>
														<%--<td class="mp-name" width="5%">成人：</td>--%>
														<%--<td width="45%" class="tl">--%>
															<%--<c:forEach items="${pricingStrategyList}" var="pricingStrategys" varStatus="status">--%>
																<%--<c:if test="${status.index + 1 eq s2.count}">--%>
																	<%--<c:if test="${pricingStrategys.adultPricingStrategy == ''}">--%>
																		<%-----%>
																	<%--</c:if>--%>
																	<%--${pricingStrategys.adultPricingStrategy}--%>
																<%--</c:if>--%>
															<%--</c:forEach>--%>
														<%--</td>--%>
														<%--<td class="mp-name" width="5%">成人：</td>--%>
														<%--<td width="45%" class="tl">--%>
															<%--<c:choose>--%>
																<%--<c:when test="${group.quauqAdultPrice ne null}">--%>
																	<%--${fns:getCurrencyInfo(group.currencyType,0,'mark')}--%>
																	<%--<fmt:parseNumber type="number" value="${group.adultRetailPrice}" />--%>
																<%--</c:when>--%>
																<%--<c:otherwise>-</c:otherwise>--%>
															<%--</c:choose>--%>
														<%--</td>--%>

													<%--</tr>--%>
													<%--<tr>--%>
														<%--<td class="mp-name" width="5%">儿童：</td>--%>
														<%--<td width="45%" class="tl">--%>
															<%--<c:forEach items="${pricingStrategyList}" var="pricingStrategys" varStatus="status">--%>
																<%--<c:if test="${status.index + 1 eq s2.count}">--%>
																	<%--<c:if test="${pricingStrategys.childrenPricingStrategy == ''}">--%>
																		<%-----%>
																	<%--</c:if>--%>
																	<%--${pricingStrategys.childrenPricingStrategy}--%>
																<%--</c:if>--%>
															<%--</c:forEach>--%>
														<%--</td>--%>
														<%--<td class="mp-name" width="5%">儿童：</td>--%>
														<%--<td width="45%" class="tl">--%>
															<%--<c:choose>--%>
																<%--<c:when test="${group.quauqChildPrice ne null}">--%>
																	<%--${fns:getCurrencyInfo(group.currencyType,1,'mark')}--%>
																	<%--<fmt:parseNumber type="number" value="${group.childRetailPrice}" />--%>
																<%--</c:when>--%>
																<%--<c:otherwise>-</c:otherwise>--%>
															<%--</c:choose>--%>
														<%--</td>--%>

													<%--</tr>--%>
													<%--<tr>--%>
														<%--<td class="mp-name" width="5%">特殊人群：</td>--%>
														<%--<td width="45%" class="tl">--%>
															<%--<c:forEach items="${pricingStrategyList}" var="pricingStrategys" varStatus="status">--%>
																<%--<c:if test="${status.index + 1 eq s2.count}">--%>
																	<%--<c:if test="${pricingStrategys.specialPricingStrategy == ''}">--%>
																		<%-----%>
																	<%--</c:if>--%>
																	<%--${pricingStrategys.specialPricingStrategy}--%>
																<%--</c:if>--%>
															<%--</c:forEach>--%>
														<%--</td>--%>
														<%--<td class="mp-name" width="5%">特殊人群：</td>--%>
														<%--<td width="45%" class="tl"><c:choose>--%>
															<%--<c:when test="${group.quauqSpecialPrice ne null}">--%>
																<%--${fns:getCurrencyInfo(group.currencyType,2,'mark')}--%>
																<%--<fmt:parseNumber type="number" value="${group.specialRetailPrice}" />--%>
															<%--</c:when>--%>
															<%--<c:otherwise>-</c:otherwise>--%>
														<%--</c:choose>--%>
														<%--</td>--%>
													<%--</tr>--%>
													<%--</tbody>--%>
												<%--</table>--%>
											<%--</div>--%>
										<%--</div>--%>
									<%--</c:if>--%>
									<c:choose>
										<c:when test="${!empty group.groupRemark}">
											&nbsp;&nbsp;<span id="remark_${s2.count}" class="uploadGroupFileClass" onclick="expandRemark(this)" href="javascript:void(0)">备注<em class="groupNoteTipImg"></em></span>
										</c:when>
										<c:otherwise>
											&nbsp;&nbsp;<span id="remark_${s2.count}" class="uploadGroupFileClass" onclick="expandRemark(this)" href="javascript:void(0)">备注</span>
										</c:otherwise>
									</c:choose>
								</td>
								<c:if test="${travelActivity.activityKind eq '2'}">
									<td style="display:none" ><%--161018 将hidden='hidden'改成display:none 兼容ie9--%>
										<input type="text" name="adultDiscountPrice" id="adultDiscountPrice${s2.count}"
											   value="<c:if test="${group.adultDiscountPrice eq 0}"></c:if>
												<c:if test="${group.adultDiscountPrice ne 0}"> <fmt:formatNumber value="${group.adultDiscountPrice}" /></c:if>"
											   class="ipt-currency" maxlength="8"/>
									</td>
									<td style="display:none" >
										<input type="text" name="childDiscountPrice" id="childDiscountPrice${s2.count}"
											   value="<c:if test="${group.childDiscountPrice eq 0}"></c:if>
												<c:if test="${group.childDiscountPrice ne 0}"> <fmt:formatNumber value="${group.childDiscountPrice}" /></c:if>"
											   class="ipt-currency" maxlength="8"/>
									</td>
									<td style="display:none" >
										<input type="text" name="specialDiscountPrice" id="specialDiscountPrice${s2.count}"
											   value="<c:if test="${group.specialDiscountPrice eq 0}"></c:if>
												<c:if test="${group.specialDiscountPrice ne 0}"> <fmt:formatNumber value="${group.specialDiscountPrice}" /></c:if>"
											   class="ipt-currency" maxlength="8"/>
									</td>
								</c:if>

								<!-- 对应需求号   223  -->
								<c:if test="${travelActivity.activityKind eq '10'}">
									<c:if test="${isneedCruiseGroupControl eq '1'}">
										<td style="display:none" >
											<!--
												  <input type="text" name="cruiseGroupControlId" id="cruiseGroupControlId" value="${group.cruiseshipStockDetailId}"/>
												  -->
											<input type='hidden' name='cruiseGroupControlId' value='${group.cruiseshipStockDetailId}' />
										</td>
									</c:if>
								</c:if>
							</tr>

							<tr class="noteTr11" style="display:none" id="gRemark_${s2.count}">
								<td>
									<div class="remarks-containers">备注:
										<input type="text" name="groupNotes" class="groupNotes" value="${group.groupRemark }">
									</div>
								</td>
								<td class="tc">
									<a href="javascript:void(0)" class="unSaveNotes01" name="cancelRemark">返回</a>
								</td>
							</tr>

							<tr class="noteTr" style="display:none" id="groupRemark_${s2.count}">
								<td >
									<div class="remarks-containers">备注:
										<input type="text" class="groupNotes" value="${group.groupRemark }" name="groupRemark">
										<input type='hidden' name='priceJson' value='${group.priceJson}' ></input>
										<em class="clearNotes"></em>
									</div>
								</td>
							</tr>

						</c:forEach>
						</tbody>
					</table>
				</c:if>
			</div>
			<!-- 518 全选反选批量上架 begin -->
			<%--<div class="selectInTable">--%>
                        <%--<span>--%>
                             <%--<input class="table-checkAll" name="checkAll" onclick="tableCheckAll(this);" type="checkbox">--%>
                             <%--<label>全选</label>--%>
						<%--</span>--%>
				<%--<span class="table-checkReverse" name="checkReverse" onclick="tableCheckReverse(this);">反选</span>--%>
				<%--<input onclick="batchGrounding(this);" type="button" class="btn btn-primary" value="批量上架平台">--%>
			<%--</div>--%>
			<!-- 518 全选反选批量上架 end -->
				<%-- 			<c:if test="${groupsize ne 0 and travelActivity.activityKind ne '10'}">style="display: none"</c:if> --%>
			<div class="addNewGroup"  >
				<div id="secondStepTitle"  class="mod_information_d"><span style=" font-weight:bold; padding-left:20px">添加新出团日期</span></div>
				<!-- 				<div style="margin-top:8px;" class="team_bill" ><span style="padding-left:20px; color:#FFF;"></span></div>				 -->
				<div id="secondStepAdd">
					<div style="width:100%; height:20px;"></div>
						<%--                <div class="team_bill"><span style="padding-left:20px; color:#FFF;">填写团期价格信息</span></div>--%>
					<div class="add2_line">
						<div class="add2_line_text">填写团期价格信息：</div>
						<div class="add2-money">
							<label><span class="xing"></span>币种选择：</label>
							<select id="selectCurrency" name="selectCurrency" >
								<c:forEach items="${currencyList}" var="currency">
									<option value="${currency.id}" var="${currency.convertLowest}" id="${currency.currencyMark}">${currency.currencyName}</option>
								</c:forEach>
							</select>
							<span></span>
						</div>
					</div>
					<div style="width:100%; height:20px;"></div>
					<div class="add2_nei">
						<table class="table-mod2-group">
							<!--区分供应商 -->
							<input type="hidden" value="${fns:getUser().company.id }" id="companyId"/>
							<input type="hidden" value="${fns:getUser().company.uuid }" id="companyUUID"/>
							<div class="choCur" style="display: none;">
								<dl class="choose-currency">
									<dt>选择币种</dt>
									<dd>
										<c:forEach items="${currencyList}" var="currency" varStatus="s">
											<p id="${currency.id}" class="<c:if test="${s.index eq '0'}">p-checked</c:if>" name="${currency.currencyStyle}" addclass="${currency.currencyMark}">${currency.currencyName}</p>
										</c:forEach>
									</dd>
								</dl>
							</div>
							<c:choose>
								<%--    -----游轮产品----- 	 --%>
								<c:when test="${travelActivity.activityKind eq '10'}">
									<tr>
										<td class="add2_nei_table"><span class="fr">1/2人同行价1：</span></td>
										<td class="add2_nei_table_typetext">
											<input id="settlementAdultPriceDefine" type="text" maxlength="8" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)"/>
										</td>
										<td class="add2_nei_table"><span class="fr">3/4人同行价：</span></td>
										<td class="add2_nei_table_typetext">
											<input  id="settlementcChildPriceDefine" type="text" maxlength="8" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)" />
										</td>
									</tr>
									<tr>
										<td class="add2_nei_table"><span class="fr">1/2人直客价：</span></td>
										<td class="add2_nei_table_typetext">
											<input id="suggestAdultPriceDefine" type="text" maxlength="8" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)"/>
										</td>
										<td class="add2_nei_table"><span class="fr">3/4人直客价：</span></td>
										<td class="add2_nei_table_typetext">
											<input id="suggestChildPriceDefine" type="text" maxlength="8" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)"/>
										</td>
									</tr>
								</c:when>
								<%--    -----游轮产品----- 	 --%>
								<c:otherwise>
									<tr>
										<td class="add2_nei_table"><span class="fr">成人同行价：</span></td>
										<td class="add2_nei_table_typetext">
											<input id="settlementAdultPriceDefine" type="text" maxlength="14" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)" />
										</td>
										<td class="add2_nei_table"><span class="fr">儿童同行价：</span></td>
										<td class="add2_nei_table_typetext"><input  id="settlementcChildPriceDefine" type="text" maxlength="14" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)"/></td>
										<td class="add2_nei_table"><span class="fr">特殊人群同行价：</span></td>
										<td class="add2_nei_table_typetext"><input id="settlementSpecialPriceDefine" type="text" maxlength="14" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)"/></td>
										<td class="add2_nei_table"><label class="label-dw">特殊人群最高人数：   </label> </td>
										<td class="add2_nei_table_typetext" style="width: 110px;">
											<input id="maxPeopleCountDefine" type="input" class="inputTxt" name="maxPeopleCountDefine" value="" style="width:60px;height:18px;"  maxlength="8"
												   onafterpaste="this.value=this.value.replace(/\D|^0.+/g,'')" onkeyup="this.value=this.value.replace(/\D|^0.+/g,'')"/>
											<span style="padding-left:5px;">人</span>
										</td>



										<td class="add2_nei_table_typetext"><div class="marks-people" style="width: 180px;"><label class="label-dw">备注：</label><form:input path="specialRemark" cssClass="ipt-otherPeople" flag="istips" maxlength="50"/></div></td>

									</tr>
									<c:if test="${travelActivity.activityKind eq '2'}">
										<tr>
											<td class="add2_nei_table"><span class="fr">成人直客价：</span></td>
											<td class="add2_nei_table_typetext"><input id="suggestAdultPriceDefine" type="text" maxlength="14" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)" /></td>
											<td class="add2_nei_table"><span class="fr">儿童直客价：</span></td>
											<td class="add2_nei_table_typetext"><input id="suggestChildPriceDefine" type="text" maxlength="14" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)" /></td>
											<td class="add2_nei_table"><span class="fr">特殊人群直客价：</span></td>
											<td class="add2_nei_table_typetext"><input id="suggestSpecialPriceDefine" type="text" maxlength="14" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)" /></td>
										</tr>
									</c:if>
								</c:otherwise>
							</c:choose>
							<tr>
								<td class="add2_nei_table"><span class="fr">需交订金：</span></td>
								<td class="add2_nei_table_typetext"><input id="payDepositDefine" type="text" maxlength="8" class="ipt-currency" flag="istips" style="width:60px;" onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)"/></td>
								<td class="add2_nei_table"><span class="fr">单房差：</span></td>
								<td class="add2_nei_table_typetext"><input id="singleDiffDefine" type="text" maxlength="8" class="ipt-currency" flag="istips" style="width:60px;"onkeyup="isMoney(this)" onafterpaste="isMoney(this)" onblur="isMoney(this)" /></td>
								<c:if test="${travelActivity.activityKind ne '10'}">
									<td class="add2_nei_table_typetext" colspan="2"><label class="label-dw">单位：</label><form:input path="singleDiffUnit" value="间 / 夜" cssClass="ipt-fjc" maxlength="50" /></td>
									<td class="add2_nei_table"><label class="label-dw">儿童最高人数：   </label> </td>
									<td class="add2_nei_table_typetext">
										<input id="maxChildrenCountDefine"  name="maxChildrenCountDefine" type="input" class="inputTxt" maxlength="8" value=""  style="width:60px;height:18px;"
											   onafterpaste="this.value=this.value.replace(/\D|^0.+/g,'')" onkeyup="this.value=this.value.replace(/\D|^0.+/g,'')"/>
										<span style="padding-left:5px;">人</span>
									</td></c:if>
							</tr>
							<c:if test="${fn:length(groupCodeRule) ne 0}">
								<tr>
									<td class="add2_nei_table">团号类别：</td>
									<td>
										<select id="groupCodeRule" onChange="changeGroupRule(this)" style="width: 77px;">
											<c:forEach items="${groupCodeRule}" var="codeRule">
												<option value="${codeRule[0]}">${codeRule[1]}</option>
											</c:forEach>
										</select>
									</td>
								</tr>
							</c:if>
						</table>
						<div class="kong"></div>
					</div>

					<div class="add2_nei add2_nei_line">
						<div class="add2_nei_a">选择出团日期：</div>
						<div class="add2_nei_b">（温馨提示：日历框中选择出团日期，按shift键同时移动鼠标多选日期）</div>
						<div class="kong"></div>
					</div>
					<div class="add2_nei add2_nei_chosedate">
						<textarea name="dateList" id="dateList" style="width:500px;height:200px;display: none;"></textarea>
						<label>出团日期：</label>
						<a class="groupDate">选择日期</a>
						<input class="inputTxt selectedDate" style="background-color:#CCCCCC;width:80px; display: none;" readonly="">
						<input id="groupOpenDate" class="inputTxt" name="groupOpenDateBegin" readonly style="display: none;"/>
						<input id="groupCloseDate" class="inputTxt" name="groupCloseDateEnd" readonly  style="display: none;"/>&nbsp;&nbsp;
					</div>

						<%--             <div class="release_next_add">--%>
						<%--                 <input class="btn btn-primary" type="button" onclick="writeGroupDate()" value="提交团期"/>--%>
						<%--               </div>           --%>
				</div>

				<div id="secondStepEnd">
					<div style="width:100%; height:30px;"></div>
					<div style="width:100%; height:10px;"></div>
					<c:set var="youhuiFlag" value="0"></c:set>
					<div id="groupTable" style="margin-top:8px;">
						<c:if test="${travelActivity.activityKind eq '2' && (fns:getUser().company.uuid eq '7a81c5d777a811e5bc1e000c29cf2586'
                         || fns:getUser().company.uuid eq '75895555346a4db9a96ba9237eae96a5') }">
							<c:set var="youhuiFlag" value="1"></c:set>
							<!--S--109需求增加设置优惠额度-->
							<div class="discount-setting-container tr">
								<input class="btn btn-primary" onclick="jbox__discount_setting_pop_fab();" value="设置优惠额度" type="button">
								<!--仅为展示效果，后端需要适当判断进行提示，-->
								<!--<input class="btn btn-primary" onclick="jbox__nosel_group_discount_setting_pop_pop_fab();" value="未选择团期设置优惠额度" type="button">-->
							</div>
						</c:if>
						<table id="contentTable" class="table table-striped table-bordered table-condensed table-mod2-group psf_table_son" style="width:98%;margin:10px auto">
							<thead>
							<tr>
								<c:if test="${travelActivity.activityKind eq '2'}">
									<c:if test="${fns:getUser().company.uuid eq '7a81c5d777a811e5bc1e000c29cf2586' || fns:getUser().company.uuid eq '75895555346a4db9a96ba9237eae96a5' }">
										<th width="4%" rowspan="2">全选
											<input class="none-height-input" name="allChk" onclick="checkall_new(this)" type="checkbox"></th>
									</c:if>
									<%--<shiro:hasPermission name="calendarLoose:book:order">--%>
									<%--<th rowspan="2" width="2%">推荐</th>--%>
									<%--</shiro:hasPermission>--%>
								</c:if>
								<th rowspan="2" width="6%">出团日期</th>
								<th rowspan="2" width="3%">截团日期<span><br>提前天数<br/><select id="closeBeforeDays" name="closeBeforeDays" disabled="disabled"></select></span></th>
								<th rowspan="2" width="6%">应付账期</th>
								<!-- 因bug#13135,产品决定,不管是名扬还是越柬批发商,应付账期和团号都展示-s -->
								<c:if test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' }">
									<th rowspan="2" width="6%">团号</th>
								</c:if>
								<!-- 因bug#13135,产品决定,不管是名扬还是越柬批发商,应付账期和团号都展示-e -->
								<c:if test="${fns:getUser().company.uuid ne '7a81b21a77a811e5bc1e000c29cf2586' }">
									<th rowspan="2" width="7%"><span class="xing">*</span>团号</th>
								</c:if>
								<th rowspan="2" width="3%">签证国家<span><br><input id="visaCountryCopy" type="text" name="visaCountryCopy" class="visacountrycopy_input" disabled="disabled"/><br/><input id="visaCopyBtn" type="button" value="复制" onclick="visaCopy()" class="visa_copy" disabled="disabled"/></span></th>
								<th rowspan="2" width="3%">资料截止<span><br>提前天数<br/><select id="visaBeforeDays" name="visaBeforeDays" disabled="disabled"></select></span></th>
								<c:set var="colspanNum" value="3"></c:set>
								<c:if test="${travelActivity.activityKind eq '10'}">
									<th rowspan="2" width="4%">舱型</th>
									<c:set var="colspanNum" value="2"></c:set>
								</c:if>
								<shiro:hasPermission name="price:project">
									<th rowspan="2" width="14%">酒店房型</th>
								</shiro:hasPermission>
								<th width="11%" class="t-th2" colspan="${colspanNum }">同行价</th>
								<c:if test="${travelActivity.activityKind eq '2' or travelActivity.activityKind eq '10'}">
									<th width="11%" class="t-th2" colspan="${colspanNum }">直客价</th>
								</c:if>

								<c:if test="${travelActivity.activityKind ne '10'}">
									<th width="4%" rowspan="2">儿童最高人数
									<th width="4%" rowspan="2">特殊人群最高人数
									</th>
								</c:if>
								<th width="5%" rowspan="2">需交订金</th>
								<th width="5%" rowspan="2">单房差</th>
								<th width="3%" rowspan="2">预收<c:if test="${travelActivity.activityKind eq '10'}">/间</c:if><span><br><input id="planPositionCopy" name="planPositionCopy" class="visacountrycopy_input" maxlength="3" disabled="disabled"/><br/><input id="planPositionBtn" type="button" value="复制" onclick="positionCopy(this)" class="visa_copy" disabled="disabled"/></span></th>
								<th width="3%" rowspan="2">余位<c:if test="${travelActivity.activityKind eq '10'}">/间</c:if><span><br><input id="freePositionCopy" name="freePositionCopy" class="visacountrycopy_input" maxlength="3" disabled="true"/><br/><input id="freePositionBtn" type="button" value="复制" onclick="positionCopy(this)" class="visa_copy" disabled="true"/></span></th>
								<!-- 0258 懿洋假期发票税 -->
								<c:if test="${fns:getUser().company.uuid eq 'f5c8969ee6b845bcbeb5c2b40bac3a23' }">
									<th width="6%" rowspan="2">发票税<span><br><input style="width:60px" id="invoiceTaxCopy" name="invoiceTaxCopy" onkeyup="checkValue(this);" onafterpaste="checkValue(this);" class="visacountrycopy_input" disabled="true"/>%<br/><input id="invoiceTaxBtn" type="button" value="复制" onclick="positionCopy4YYJQ(this)" class="visa_copy" disabled="true"/></span></th>
								</c:if>

								<th width="5%" rowspan="2">操作<span><br></span>
									<a href="javascript:void(0)" onclick="delAllGroupDate()">全部<br>删除</a>
							</tr>
							<tr>
								<c:choose>
									<c:when test="${travelActivity.activityKind eq '10' }">
										<th>1/2人</th>
										<th>3/4人</th>
										<th>1/2人</th>
										<th>3/4人</th>
									</c:when>
									<c:otherwise>
										<th>成人</th>
										<th>儿童</th>
										<th>特殊人群</th>
										<c:if test="${travelActivity.activityKind eq '2'}">
											<th>成人</th>
											<th>儿童</th>
											<th>特殊人群</th>
										</c:if>
									</c:otherwise>
								</c:choose>
							</tr>
							</thead>
							<tbody>
							<tr id="emptygroup">
								<c:set var="shiroFlag" value="0"></c:set>
								<td name="countLineNum">暂无价格信息，请选择日期</td>
							</tr>
							</tbody>
						</table>
					</div>
					<div class="kong"></div>
				</div>
			</div>
		</div>

		<div id="secondModBtn" class="nextDiv" style="text-align:center;width:100%;">
			<div class="ydBtn" style="width:85px;"><a class="ydbz_x" href="javascript:void(0)" onclick="secondStepModValidate()">添加团期</a></div></div>
		<div id="secondSaveBtn" class="nextDiv" style="text-align:center;width:100%;">
			<div class="ydBtn"><a class="ydbz_x" href="javascript:void(0)" onclick="saveNewGroup();">保存</a></div></div>



		<!-- 上传文件 -->
		<div class="team_ins">
			<div id="thirdStepTitle" class="ydbz_tit" style="margin:10px 1% 0 1%;"><span class="ydExpand closeOrExpand"></span><span>上传文件修改</span></div>
			<div id="thirdStepContent" class="mod_information_3">
				<div class="batch"  style="margin-top:10px;">
					<label class="batch-label company_logo_pos">
						<!-- 139大洋国旅产品非必填 -->
						<c:choose>
							<c:when test="${fns:getUser().company.uuid ne '7a81a03577a811e5bc1e000c29cf2586' }">
								产品行程介绍：
							</c:when>
							<c:otherwise>产品行程介绍：</c:otherwise>
						</c:choose>
					</label>
					<input type="button" name="introduction" value="选择文件" class="mod_infoinformation3_file" onClick="uploadFiles('${ctx}',null,this)"/>
					<ol class="batch-ol">
						<c:set var="introductionVaildator" value="" ></c:set>
						<c:forEach items="${travelActivity.activityFiles}" var="file" varStatus="s1">
							<c:if test="${file.fileType==1}">
								<c:set var="introductionVaildator" value="true" ></c:set>
								<li>
									<span>${file.docInfo.docName}</span><a class="batchDl" style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${file.docInfo.id})">下载</a>
									<input type="hidden" name="introduction" value="${file.docInfo.id}"/>
									<a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(null,'introduction',this)">删除</a>
								</li>
							</c:if>
						</c:forEach>
						<input id="introduction_name" name="introduction_name" value="产品行程介绍" type="hidden" />
					</ol>
					<c:if test="${fns:getUser().company.uuid ne '7a81a03577a811e5bc1e000c29cf2586' }">
						<input type="text" value="${introductionVaildator }" id="introductionVaildator" style="width:1px; height:1px; margin:0; padding:0; border:none; position:absolute; z-index:-1;" />
					</c:if>
					<!-- 139大洋国旅产品非必填 -->
				</div>
				<div class="mod_information_d7"></div>
				<div class="batch"  style="margin-top:10px;">
					<label class="batch-label company_logo_pos">自费补充协议：</label>
					<input type="button" name="costagreement" value="选择文件" class="mod_infoinformation3_file" onClick="uploadFiles('${ctx}',null,this)"/>
					<ol class="batch-ol">
						<c:forEach items="${travelActivity.activityFiles}" var="file" varStatus="s1">
							<c:if test="${file.fileType==2}">
								<li>
									<span>${file.docInfo.docName}</span><a class="batchDl" style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${file.docInfo.id})">下载</a>
									<input type="hidden" name="costagreement" value="${file.docInfo.id}"/>
									<a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(null,null,this)">删除</a>
								</li>
							</c:if>
						</c:forEach>
						<input id="costagreement_name"  name="costagreement_name" value="自费补充协议" type="hidden"/>
					</ol>
				</div>
				<div class="mod_information_d7"></div>
				<div class="batch"  style="margin-top:10px;">
					<label class="batch-label company_logo_pos">其他补充协议：</label>
					<input type="button" name="otheragreement" value="选择文件" class="mod_infoinformation3_file" onClick="uploadFiles('${ctx}',null,this)"/>
					<ol class="batch-ol">
						<c:forEach items="${travelActivity.activityFiles}" var="file" varStatus="s1">
							<c:if test="${file.fileType==3}">
								<li>
									<span>${file.docInfo.docName}</span><a class="batchDl" style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${file.docInfo.id})">下载</a>
									<input type="hidden" name="otheragreement" value="${file.docInfo.id}"/>
									<a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(null,null,this)">删除</a>
								</li>
							</c:if>
						</c:forEach>
						<input id="otheragreement_name"  name="otheragreement_name" value="其他补充协议" type="hidden"/>
					</ol>
				</div>
				<div class="mod_information_d7"></div>
				<div class="batch"  style="margin-top:10px;">
					<label class="batch-label company_logo_pos">其他文件：</label>
					<input type="button" name="otherFiles" value="选择文件" class="mod_infoinformation3_file" onClick="uploadFiles('${ctx}',null,this)"/>
					<ol class="batch-ol">
						<c:forEach items="${travelActivity.activityFiles}" var="file" varStatus="s1">
							<c:if test="${file.fileType==5}">
								<li>
									<span>${file.docInfo.docName}</span><a class="batchDl" style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${file.docInfo.id})">下载</a>
									<input type="hidden" name="otherFiles" value="${file.docInfo.id}"/>
									<a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(null,null,this)">删除</a>
								</li>
							</c:if>
						</c:forEach>
						<input class="otherfile_name" name="otherfile_name" value="其他文件" type="hidden"/>
					</ol>
				</div>
				<div class="mod_information_d7"></div>
				<div id="visaData">
					<c:if test="${travelActivity.activityKind ne '10' }">
						<div class="batch" id="visaflag" style="margin-top:8px;margin-bottom:8px;">
							<label>签证资料文件：</label><input type="button" class="mod_infoinformation3_file" value="+添加" onclick="addvisafile(this)">
						</div>
					</c:if>
					<!--                 已上传签证资料 -->
					<c:forEach items="${visaMap[travelActivity.id] }" var="visaList" varStatus="ms">
						<c:forEach items="${visaList }" var="visaMaps">
							<c:set var="countryId" value="${fn:split(visaMaps.key,'/')[0]}"></c:set>
							<c:set var="visaTypeId" value="${fn:split(visaMaps.key,'/')[1]}"></c:set>
							<div class="mod_information_d8_2" style="margin-top:5px;">
								<table border="0" style="vertical-align:middle;" name="company_logo">
									<tbody>
									<tr>
										<td><label style="width: auto;">国家：</label></td>
										<td>
											<select name="country" id="country${ms.index }">
												<option value="">请选择</option>
												<c:forEach items="${countryList }" var="country">
													<option value="${country.id }" <c:if test="${country.id eq countryId}">selected="selected"</c:if> >${country.countryName_cn}</option>
												</c:forEach>
											</select>
										</td>
										<td><label style="width: auto;">签证类型：</label></td>
										<td>
											<select name="visaType" onchange="removeDisable()" id="visaType${ms.index }">
												<option value="">请选择</option>
												<c:forEach items="${visaTypes }" var="visaType">
													<option value="${visaType.value }" <c:if test="${visaType.label eq visaTypeId}">selected="selected"</c:if> >${visaType.label}</option>
												</c:forEach>
											</select>
										</td>
										<td>
											<label style="padding-top:4px;width: auto;">签证资料：</label>
										</td>
										<td>
											<input class="mod_infoinformation3_file" type="button" onclick="uploadFiles('${ctx }','signmaterial${ms.index}',this)" value="选择文件" name="signmaterial${ms.index}">
											<input type="hidden" value="${ms.index }" name="fileGroup">
										</td>
										<td>
											<a class="mod_infoinformation3_del" onclick="removefile('确定删除该文件吗',this)" href="javascript:void(0)">删除</a>
										</td>
									</tr>
									</tbody>
								</table>
								<ol class="batch-ol">
									<c:forEach items="${visaMaps.value }" var="docInfo">
										<li>
											<span>${docInfo.docName }</span><a class="batchDl" style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${docInfo.id})">下载</a>
											<input type="hidden" name="signmaterial${ms.index }" value="${docInfo.id}"/>
											<a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(null,null,this)">删除</a>
										</li>
									</c:forEach>
								</ol>
							</div>
						</c:forEach>
					</c:forEach>

					<!--                 已上传签证资料 -->

				</div>
				<!--微信新文件上传 Strat-->
				<c:if test="${travelActivity.activityKind eq '2'}">
				<shiro:hasPermission name="looseProduct:operation:requiredStraightPrice">
				<div class="mod_information_d7"></div>
				<div class="we_chat_update">
					<label class="batch-label company_logo_pos">上传微信图片：</label>
					<span class="hint_span"> 建议上传JPG、JPEG、GIF、PNG格式，大小不超过2M </span>
					<div class="preview_content">
						<div class="preview_con_left">
							<div class="col-md-9">
								<div class="img-container">
								<c:forEach items="${travelActivity.activityFiles}" var="file" varStatus="s1">
									<c:if test="${file.fileType==6}">
										<img src="${ctx}/person/info/getLogo?id=${file.docInfo.id}" alt="Picture">
									</c:if>
								</c:forEach>
								</div>
							</div>
							<div class="control_img">
								<div class="btn-group">
									<label class="btn btn-primary btn-upload" for="inputImage">
										<input class="sr-only" id="inputImage" name="file" type="file">
										<span class="docs-tooltip" onclick="uploadImage();" data-toggle="tooltip">
                                              <i class="fa fa-upload"></i> 上传图片
                                            </span>
									</label>
									<button class="btn btn-primary" data-method="setDragMode" data-option="crop" type="button">
                                            <span class="docs-tooltip" data-toggle="tooltip" onclick="uploadImage();" title="裁剪图片">
                                              <span class="icon icon-crop"></span>
                                            </span>
									</button>
									<button class="btn btn-primary" data-method="clear" type="button">
                                            <span class="docs-tooltip" data-toggle="tooltip" title="取消裁剪">
                                              <span class="icon icon-remove"></span>
                                            </span>
									</button>
									<button class="btn btn-primary" data-method="zoom" data-option="0.2" type="button">
											<span class="docs-tooltip" data-toggle="tooltip" title="放大图片">
											  <span class="icon icon-zoom-in"></span>
											</span>
									</button>
									<button class="btn btn-primary" data-method="zoom" data-option="-0.2" type="button">
											<span class="docs-tooltip" data-toggle="tooltip" title="缩小图片">
											  <span class="icon icon-zoom-out"></span>
											</span>
									</button>
									<button class="btn btn-primary" data-method="reset" type="button">
											<span class="docs-tooltip" data-toggle="tooltip" title="重置">
											  <span class="icon icon-refresh"></span>
											</span>
									</button>
                                    <%--<button class="btn btn-primary" data-method="destroy" type="button">--%>
                                            <%--<span class="docs-tooltip" data-toggle="tooltip" title="取消修改">--%>
                                              <%--<span class="icon icon-off"></span>--%>
                                            <%--</span>--%>
                                    <%--</button>--%>
								</div>
							</div>

								<%--删去--%>
								<%--<div class="container">--%>
								<%--<div class="row">--%>
							<div class="col-md-9 disable_btn">
								<div class="btn-group btn-group-crop">
									<button class="btn btn-primary" data-method="getCroppedCanvas" type="button">
										<span class="docs-tooltip" data-toggle="tooltip"></span>
									</button>
								</div>
								<!-- Show the cropped image in modal -->
								<div class="modal fade docs-cropped" id="getCroppedCanvasModal" aria-hidden="true" aria-labelledby="getCroppedCanvasTitle" role="dialog" tabindex="-1">
									<div class="modal-dialog">
										<div class="modal-content">
											<div class="modal-header">
												<button class="close" data-dismiss="modal" type="button" aria-hidden="true">&times;</button>
												<h4 class="modal-title" id="getCroppedCanvasTitle">Cropped</h4>
											</div>
											<div class="modal-body"></div>
											<div class="modal-footer">
												<button class="btn btn-primary" data-dismiss="modal" type="button">上传</button>
												<button class="btn btn-primary" type="button">Close</button>
											</div>
										</div>
									</div>
								</div>
							</div>
								<%--</div>--%>
								<%--</div>--%>
								<%--删去--%>
						</div>

						<div class="preview_img list_previw">
							<ul>
								<li><div class="img-preview preview-sm"></div></li>
							</ul>
						</div>
						<div class="preview_img detail_previw">
							<div class="img-preview preview-md"></div>
						</div>
					</div>
				</div>
				</shiro:hasPermission>
			</c:if>
				<!--微信新文件上传 End-->
				<div id="template" style="display:none;">
					<select name="country">
						<option value="">请选择</option>
						<c:forEach items="${countryList }" var="country">
							<option value="${country.id }">${country.countryName_cn}</option>
						</c:forEach>
					</select>
					<select name="visaType">
						<option value="">请选择</option>
						<c:forEach items="${visaTypes }" var="visaType">
							<option value="${visaType.value }">${visaType.label}</option>
						</c:forEach>
					</select>
				</div>
				
			<!-- QU-SDP-微信分销模块start 为散拼修改产品添加上传微信广告图片功能  yang.gao 2017-01-06 -->
			<c:if test="${travelActivity.activityKind eq '2'}">
				<shiro:hasPermission name="looseProduct:operation:requiredStraightPrice">
					<!-- <div class="mod_information_d7"></div> -->
					<!--<div class="batch"  style="margin-top:10px;"> -->
						<!--<label class="batch-label company_logo_pos">微信分销广告图片：</label>-->
						<!--<input type="button" name="distributionAdImg" value="选择文件" class="mod_infoinformation3_file" onClick="uploadSingleFileByParam('${ctx}',null,this,'.jpg;*.jpeg;*.png;*.gif','2MB')"/>-->
						<!--<ol class="batch-ol">-->
							<c:forEach items="${travelActivity.activityFiles}" var="file" varStatus="s1">
								<c:if test="${file.fileType==6}">
									<!--<li>-->
										<!--<span>${file.docInfo.docName}</span><a class="batchDl" style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${file.docInfo.id})">下载</a>-->
										<input type="hidden" name="distributionAdImg" value="${file.docInfo.id}"/>
										<!--<a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(null,null,this)">删除</a>-->
									<!--</li>-->
								</c:if>
							</c:forEach>
							<input class="distributionAdImg_name" name="distributionAdImg_name" value="微信分销广告图片" type="hidden"/>
						<!--</ol>-->
					<%--</div>--%>
				</shiro:hasPermission>
			</c:if>
			<!-- QU-SDP-微信分销模块end -->
			
			</div>
			<div name="signtemplate" style="display:none;" class="mod_information_d6">
				<table border="0"  name="company_logo" style="vertical-align:middle;">
					<tr>
						<td><label style="width: auto;">国家：</label></td>
						<td><select name="country">
							<option value="">请选择</option>
							<c:forEach items="${countryList }" var="country">
								<option value="${country.id }">${country.countryName_cn}</option>
							</c:forEach>
						</select>
						</td>
						<td><label style="width: auto;">签证类型：</label></td>
						<td>
							<select name="visaType" onchange="removeDisable()">
								<option value="">请选择</option>
								<c:forEach items="${visaTypes }" var="visaType">
									<option value="${visaType.value }">${visaType.label}</option>
								</c:forEach>
							</select>
						</td>
						<td><label style="padding-top:4px;width: auto;">签证资料：</label></td>
						<td>
						</td>
						<td>
						</td>
						<td>
							<a href="javascript:void(0)" class="mod_infoinformation3_del" onclick="removefile('确定删除该文件吗',this)">删除</a>
						</td>
					</tr>
				</table>
				<ol class="batch-ol"></ol>
				<input class="signmaterial_name" name="signmaterial_name" value="签证资料" type="hidden" />
			</div>
		</div>
		<c:if test="${is_need_groupCode eq '1' }"><!-- 当批发商具有团号库权限时,才进行修改记录的展示  -->
			<!-- 大洋87、非常国际、优加、起航假期  机票产品团号修改记录 -->
			<div class="team_ins" style="margin-top:32px;">
				<c:if test="${fns:getUser().company.uuid == '7a81a03577a811e5bc1e000c29cf2586'  ||   fns:getUser().company.uuid == '1d4462b514a84ee2893c551a355a82d2' || fns:getUser().company.uuid == '7a81c5d777a811e5bc1e000c29cf2586' || fns:getUser().company.uuid == '5c05dfc65cd24c239cd1528e03965021' || fns:getUser().company.groupCodeRuleDT==0}">
					<div class="mod_information">
						<div class="mod_information_d"><div class="ydbz_tit">修改记录</div></div>
					</div>
					<c:forEach items="${groupcodeModifiedRecordmap}" var="groupcodeModified" varStatus="listIndex">
						<div class="mod_information_dzhan">
							<span class="modifyTime" style="margin-left: 30px;font-weight:bold;">当前团号【<font color="red">${groupcodeModified.key}</font>】修改过程记录如下:</span>
						</div>

						<c:forEach items="${groupcodeModified.value}" var="modifiedRecord" varStatus="listIndex">
							<div class="mod_information_dzhan">
								<span class="modifyTime" style="margin-left: 30px"> <fmt:formatDate value="${modifiedRecord.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /> </span>
								【<span class="modifyType">团号</span> 】
								由【<span class="exGroupNo">${modifiedRecord.groupcodeOld}</span> 】修改成【<span class="groupNo">${modifiedRecord.groupcodeNew}</span>】
								by【<span class="modifyUser">${modifiedRecord.updateByName}</span>】
							</div>
						</c:forEach>
					</c:forEach>
				</c:if>
			</div>
		</c:if>


		<div class="ydBtn ydBtn2">
			<c:choose>
				<c:when test="${travelActivity.activityKind == 2}">
					<input class="ydbz_x gray" type="button" value="返   回" onClick="window.location.href='${ctx}/activity/manager/list/2/${travelActivity.activityKind}'"/>
				</c:when>
				<c:otherwise>
					<input class="ydbz_x gray" type="button" value="返   回" onClick="history.go(-1)"/>
				</c:otherwise>
			</c:choose>
			<!-- QU-SDP-微信分销模块start 提交方法追加权限 yang.gao 2017-01-09 -->
			<a id="submitAndSave" class="ydbz_x" onclick="mod(${travelActivity.id},'${ctx}',${travelActivity.activityKind},this,${requiredStraightPrice})" href="javascript:void(0)">提交保存</a>
			<!-- QU-SDP-微信分销模块end 提交方法追加权限 yang.gao 2017-01-09 -->
		</div>
	</form:form>
</div>

<style type="text/css">
	#product_level{ top:330px !important; left:685px !important; width:125px; z-index:999;}
	#product_type{ top:277px !important; left:268px !important; width:125px; z-index:999;}
</style>
<script type="application/javascript">
	$(function() {

		// 无信息的时候，合并单元格个数设置
		$("td[name=countLineNum]").each(function(index, obj) {
			var countLineNum = $(this).parents("tbody").parent().children("thead:eq(0)").find("th").length;
			$(this).attr("colspan", countLineNum);
		});

		if("${trafficFalg}" == "true"){
			var value=$("#trafficMode option:selected").val();
			if("${relevanceFlagId}".split(",").contains(value)){
				//  if(value == "飞机")

				<%--                $("#trafficName").css("display","inline-block");--%>
				<%--                $("#flightInfo").css("display","inline-block");--%>
				$(".ml25").css("display","inline-block");
				if(!${airTicketFlg}){
					$('#zhankaiguanlian').hide();
					$('[name="xiugaiguanlian"]').parent().hide();
				}
			}
		}else {
			<%--                $("#trafficName").css("display","none");--%>
			<%--                $("#trafficName option[value='']").attr("selected", true);--%>
			<%--                $("#flightInfo").css("display","none");--%>
			<%--                $("#flightInfo option[value='']").attr("selected", true);--%>
			$(".ml25").css("display","none");
			if(${airTicketFlg}){
				$('[name="xiugaiguanlian"]').parent().hide();
			}
		}

	});
</script>
<!--E关联机票-分配弹窗-->
<!--S--C109--设置优惠额度弹窗-->

<div id="discount-setting-pop" class="display-none">
	<div class="discount-setting-pop">
		<div class="first-part">
			<div class="control-display">展示所选择的团</div>
			<ul>
			</ul>
		</div>
		<div class="mod_details2_tabletype">
			<table class="table" style="width:100%;">
				<thead>
				<tr>
					<th colspan="3">同行价优惠额度</th>
				</tr>
				<tr>
					<th class="tc" width="12%">成人</th>
					<th class="tc" width="9%">儿童</th>
					<th class="tc" width="9%">特殊人群</th>
				</tr>
				</thead>
				<tbody>

				</tbody>
			</table>
		</div>
	</div>
</div>

<!--S--C109--设置优惠额度弹窗-->
<!--S--C109--查看优惠额度弹窗-->

<div id="view-discount-setting-pop"  class="display-none">
	<div class="discount-setting-pop">
		<div class="mod_details2_tabletype">
			<table class="table" style="width:100%;">
				<thead>
				<tr>
					<th colspan="3">同行价优惠额度</th>
				</tr>
				<tr>
					<th class="tc" width="12%">成人</th>
					<th class="tc" width="9%">儿童</th>
					<th class="tc" width="9%">特殊人群</th>
				</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>
	</div>
</div>

<!--S--C109--查看优惠额度弹窗-->
<!--S--C109--修改优惠额度弹窗-->

<div id="modify-discount-setting-pop"  class="display-none">
	<div class="discount-setting-pop">
		<div class="mod_details2_tabletype">
			<table class="table" style="width:100%;">
				<thead>
				<tr>
					<th colspan="3">同行价优惠额度</th>
				</tr>
				<tr>
					<th class="tc" width="12%">成人</th>
					<th class="tc" width="9%">儿童</th>
					<th class="tc" width="9%">特殊人群</th>
				</tr>
				</thead>
				<tbody>
				<tr>
					<td class="tc">￥<input type="text" name="discount" value="1000"/></td>
					<td class="tc">￥<input type="text" name="discount" value="1000"/></td>
					<td class="tc">￥<input type="text" name="discount" value="1000"/></td>
				</tr>
				<tr>
					<td class="tc">￥<input type="text" name="discount" value="1000"/></td>
					<td class="tc">￥<input type="text" name="discount" value="1000"/></td>
					<td class="tc">￥<input type="text" name="discount" value="1000"/></td>
				</tr>
				</tbody>
			</table>
		</div>
	</div>

	<!--S--C109--修改优惠额度弹窗-->
	<!--S--C109--未选择团期设置优惠提示-->
	<div id="nosel-group-discount-setting-pop" class="display-none">
		<div class="nosel-group-discount-setting-pop">
			请选择团！
		</div>
	</div>
	<!--E--C109--未选择团期设置优惠提示-->
</div>

<div id="pricingTableTem" class="display-none">
	<table id="pricePlanTable" class="table table-striped table-bordered table-condensed table-mod2-group psf_table_son">
		<thead>
		<tr>
			<th width="5%" rowspan="2">序号</th>
			<th width="30%" class="tc" rowspan="2">价格方案</th>
			<th width="15%" class="tc" colspan="3">同行价</th>
			<c:if test="${travelActivity.activityKind eq '2'}">
				<th width="15%" class="tc" colspan="3">直客价</th>
			</c:if>
			<th width="25%" rowspan="2">备注</th>
			<th width="5%" rowspan="2">操作</th>
		</tr>
		<tr>
			<th>成人</th>
			<th>儿童</th>
			<th>特殊人群</th>
			<c:if test="${travelActivity.activityKind eq '2'}">
				<th>成人</th>
				<th>儿童</th>
				<th>特殊人群</th>
			</c:if>
		</tr>
		</thead>
		<tbody>
		<tr>
			<td name="index">1</td>
			<td>
				<p>
							<span>
								<label>酒店：</label>
							</span>
					<input width="4%" type="text" name="hotel" class="pricing-scheme" maxlength="50">
							<span>
								<label>房型：</label>
							</span>
					<input width="4%" type="text" name="houseType" class="pricing-scheme" maxlength="50">

							<span class="addAndRemove"><em class="add-select" name="addPricing" style="height:19px"></em>
							<em class="remove-selected" name="deletePricing" style="height:19px"></em></span>
				</p>
			</td>
			<td class="tr tdCurrency">
				<span name="currencyMark">¥</span>
				<input width="4%" type="text" name="thcr" class="ipt-currency" data-type="amount" maxlength="8">
			</td>
			<td class="tr tdCurrency">
				<span name="currencyMark">¥</span>
				<input width="4%" type="text" name="thet" class="ipt-currency" data-type="amount" maxlength="8">
			</td>
			<td class="tr tdCurrency">
				<span name="currencyMark">¥</span>
				<input width="4%" type="text" name="thts" class="ipt-currency" data-type="amount" maxlength="8">
			</td>
			<c:if test="${travelActivity.activityKind eq '2'}">
				<td class="tr tdCurrency">
					<span name="currencyMark">¥</span>
					<input width="4%" type="text" name="zkcr" class="ipt-currency" data-type="amount" maxlength="8">
				</td>
				<td class="tr tdCurrency">
					<span name="currencyMark">¥</span>
					<input width="4%" type="text" name="zket" class="ipt-currency" data-type="amount" maxlength="8">
				</td>
				<td class="tr tdCurrency">
					<span name="currencyMark">¥</span>
					<input width="4%" type="text" name="zkts" class="ipt-currency" data-type="amount" maxlength="8">
				</td>
			</c:if>
			<td class="tc">
				<input type="text" name="remark" class="nopadding" maxlength="50"/>
			</td>
			<td>
				<em class="add-select" name="addPricingRow"></em>
				<em class="remove-selected" name="deletePricingRow"></em>
			</td>
		</tr>
		</tbody>
	</table>
</div>

<!-- 微信 需求 -->
<%--<script src="${ctxStatic}/js/picView/bootstrap.min.js"></script>--%>
<script src="${ctxStatic}/js/dist/cropper.js"></script>
<script src="${ctxStatic}/js/dist/main.js"></script>
<script src="${ctxStatic}/js/dist/weChat_saveSelf.js"></script>
</body>
</html>
