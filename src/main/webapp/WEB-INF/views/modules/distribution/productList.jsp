<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="decorator" content="wholesaler"/>
    
    <c:if test="${productOrGroup eq 'product' }"><title>产品列表</title></c:if>
    <c:if test="${productOrGroup eq 'group' }"><title>团期列表</title></c:if>
    
	<script type="text/javascript" src="${ctxStatic}/js/jquery.placeholder.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.0/lib/jquery.form.js"></script>
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script type="text/javascript" src="${ctxStatic}/modules/order/single/activityListForOrder.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/priceList.css">
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/bootstrap.min.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jh-style.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/huanqiu-style.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/activityCalendar.css">
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/wechatList.css">
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/UIrebuild_V1.css">
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css"  >

    
    <script type="text/javascript">
        var $ctx = '${ctx}';
        
        $(function(){
            // 行程天数插件
            $(".spinner").spinner({
                spin: function (event, ui) {
                    if (ui.value > 365) {
                        $(this).spinner("value", 1);
                        return false;
                    } else if (ui.value <= 0) {
                        $(this).spinner("value", 365);
                        return false;
                    }
                }
            });
        });
        
        //条件重置
        function resetSearchParams() {
            $(':input', '#searchForm').not(':button, :submit, :reset, :hidden')
                    .val('').removeAttr('checked').removeAttr('selected');
            $('#wholeSalerKey').val('');
            /* $('#estimatePriceRecordUserName').val(''); */
            $('#fromArea').val('');
            $('#targetAreaId').val('');
            $('#groupOpenDate').val('');
            $('#groupCloseDate').val('');
            $('#createName').val('');
            $('#groupLead').val('');
            $('#trafficName').val('');
            $('#backArea').val('');
            $('#activityLevelId').val('');
            $('#activityDuration').val('');
            $('#selectCurrencyType').val('1');
            $('#settlementAdultPriceStart').val('');
            $('#settlementAdultPriceEnd').val('');
            $('#activityDuration').val('');
            $('#travelTypeId').val('');
            $('#productser').val('');
            $('#productType').val('');
            $('#sousuo').show();
        }
        
        function expand(child, obj) {
            if ($(child).is(":hidden")) {
                $(obj).html("关闭团期列表");
                $(child).show();
                $(obj).parents("td").addClass("td-extend");
            } else {
                if (!$(child).is(":hidden")) {
                    $(child).hide();
                    $(obj).parents("td").removeClass("td-extend");
                    $(obj).html("展开团期列表");
                }
            }
        }
        
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/weixin/distribution/productList/${activityKind}?newflag=newflag");
			$("#searchForm").submit();
	    }
	    
	    //全选操作
	    var activityIds = ""; // 隐藏ids
		function checkall(obj) {
			if (activityIds != "") {
				activityIds = activityIds + ",";
			}
			if($(obj).attr("checked")){
				$("input[name='allChk']").attr("checked",'true');
				$("input[name='activityId']").attr("checked",'true');
				$("input[name='activityId']:checked").each(function(i,a){
					var arr = activityIds.split(",");
					if(arr.indexOf(a.value) < 0){
						activityIds = activityIds + a.value+",";
					}
  				});
			} else {
				$("input[name='allChk']").removeAttr("checked");
				$("input[name='activityId']").removeAttr("checked");
				$("input[name='activityId']").each(function(i,a){
					var arr = activityIds.split(",");
					if(arr.indexOf(a.value) >= 0){					
						activityIds = activityIds.replace(a.value+",","");
					}
  				});
			}
			activityIds = activityIds.substring(0, activityIds.length -1);
			$("#activityIds").val(activityIds);
		}
		
		function idcheckchg(obj) {
			if (activityIds != "") {
				activityIds = activityIds + ",";
			}
			var value = $(obj).val();
			if($(obj).attr("checked")){
				if(activityIds.indexOf($(obj).val()) < 0){
					activityIds = activityIds+$(obj).val()+",";
				}
			}			
			else {
				if($("input[name='allChk']").attr("checked"))
					$("input[name='allChk']").removeAttr("checked");
				if(activityIds.indexOf($(obj).val()) >= 0){
					activityIds = activityIds.replace($(obj).val()+",","");
				}
			}
			activityIds = activityIds.substring(0, activityIds.length -1);
			$("#activityIds").val(activityIds);
		}
		
		// 产品列表--全选
		function checkallForActivity(obj) {
		
			if($(obj).attr("checked")){
				$("input[name='allChk']").attr("checked",'true');
				$("input[name='activityId']").attr("checked",'true');
				$("input[name='activityId']:checked").each(function(){
					idcheckchgForActivity(this);
  				});
			} else {
				$("input[name='allChk']").removeAttr("checked");
				$("input[name='activityId']").removeAttr("checked");
				$("input[name='activityId']").each(function(){
					idcheckchgForActivity(this);
  				});
			}
		}
		
		// 产品列表--产品列表“全选列”复选框
		function idcheckchgForActivity(obj) {
			
			if ($(obj).attr("checked")) {
				$(obj).parent().parent().next().find("input[name='groupId']").attr("checked", 'true');
			} else {
				$(obj).parent().parent().next().find("input[name='groupId']").removeAttr("checked");
			}
			$(obj).parent().parent().next().find("input[name='groupId']").each(function() {
				idcheckchgChild(this);
			});
		}
		
		// 产品列表--展开团期列表复选框
		function idcheckchgChild(obj, count) {
			var value = $(obj).val();
			var groupIdArray = new Array();
			
			if (activityIds != "") {
				groupIdArray = activityIds.split(",");
			}
	
			if ($(obj).attr("checked")) {
				if (groupIdArray.indexOf(value) == -1) {
					groupIdArray.push(value);
				}
				activityIds = groupIdArray.join(",");
			} else {
				if (groupIdArray.indexOf(value) > -1) {
					var index = groupIdArray.indexOf(value);
					groupIdArray.splice(index, 1);
				}
				activityIds = groupIdArray.join(",");
				$("#parent"+count).find("input[name='activityId']").eq(0).removeAttr("checked");
			}
			$("#activityIds").val(activityIds);
		}
		
		// 批量转发产生二维码
		function generateBatchQrCode() {
			var url = "${ctx}/weixin/distribution/generateQrCode";
			var activityIds = $("#activityIds").val(); // 批量ids
			var obj = $("#batchImgId");
			if (activityIds == "") {
				top.$.jBox.tip("请选择产品才能转发", "info");
				return;
			}
			
			$.jBox.confirm("是否确认转发选中的产品及团期", "提示", function(v, h, f) {
				if (v == 'ok') {
					getQrcodeAjax(url, activityIds, true, obj, true);
				} else if (v == 'cancel') {}
			});
		}
		
		$(function () {
		    //绑定事件
		    $(".getweChat").hover(handlerIn,handlerOut)
		})
				
		var timeout;
		
		var dataIdObj;
		/**
		 * 生成二维码  鼠标移入操作
		 */
		function handlerIn() {
			dataIdObj = $(this);
		    if(timeout) {
		        clearTimeout(timeout);
		    }
		    
		    timeout=setTimeout(getweChatImg, 350);
		}
		/**
		 * 生成二维码  鼠标移出操作
		 */
		function handlerOut() {
			if(timeout) {
		        clearTimeout(timeout);
		    }
		    //$(this).next().children().find("img[name='oneIdImgName']").attr("src","");
		    $(this).find(".wechat_box").hide();
		}
		/**
		 * 获取微信生成的二维码
		 */
		function  getweChatImg() {
			var dataId = dataIdObj.attr("data-id");
			var url = "${ctx}/weixin/distribution/generateQrCode";
			var obj = dataIdObj.next().children().find("img[name='oneIdImgName']");
			getQrcodeAjax(url, dataId, false, obj, false);
		}

        /**
         * 点击生成单个产品、团期的微信二维码（在travelActivityList.jsp里面使用）
         */
        function  SingleWeCode(dataIdObj) {
            var dataId = dataIdObj.attr("data-id");
            var url = "${ctx}/weixin/distribution/generateQrCode";
            var obj = $("#batchImgId");
            getQrcodeAjax(url, dataId, true, obj, false);
        }
		
		/**
		 * 通过团期ID获取二维码ajax
		 * url：请求地址
		 * activityIds:产品团期ID
		 * batchFlag:true:代表批量，false:代表单个
		 * obj：当前要设置的img对象
		 */
		function getQrcodeAjax(url, activityIds, batchFlag, obj, isMulti) {
			$.ajax({
				type : "post",
				url  : url,
				dataType: "text",
				data : {
					activityIds:activityIds,
					isMulti:isMulti
				},
				success : function(data) {
					if (batchFlag) {
						obj.attr("src", "data:image/png;base64," + data);
						var html = $("#qrhlId").html();
						//二维码下载需求 ymx 2017/3/23 Start
						$pop = $.jBox(html, {
			                title: "二维码转发",
			                buttons: {"关闭":0},
			                submit: function (v, h, f) {
			                    if (v == 1) {
			                    } else {
			                        return true; //close
			                    }
			                },
			                width: 540,
			                height: 320,
			                showScrolling: false,
			                persistent: true
			            });
						//二维码下载需求 ymx 2017/3/23 End
					} else {
						obj.attr("src", "data:image/png;base64," + data);
					}
				}
			});
		}
		
		/**
		 * 转发全部产品及团期
		 */
		function generateAllQrCode() {
			$.jBox.confirm("是否确认转发全部产品及团期", "提示", function(v, h, f) {
				if (v == 'ok') {
					var form = $("#searchForm");
					$.ajax({
						type: "post",
						url: "${ctx}/weixin/distribution/generateQrCodeByAllId",
						dataType: "text",
						data: form.serialize(),
						success : function(data) {
							var obj = $("#batchImgId");
							obj.attr("src", "data:image/png;base64," + data);
							var html = $("#qrhlId").html();
							//二维码下载需求 ymx 2017/3/23 Start
							$pop = $.jBox(html, {
					        	title: "二维码转发",
					            buttons: {"关闭":0},
					            submit: function (v, h, f) {
					               if (v == 1) {
					               } else {
					                   return true; //close
					               }
					            },
					            width: 540,
					            height: 320,
					            showScrolling: false,
					            persistent: true
					        });
							//二维码下载需求 ymx 2017/3/23 End
						}
					});
				} else if (v == 'cancel') {}
			});
		}
    </script>
</head>
<body>
<div class="activitylist_bodyer_right_team_co_bgcolor">
	<!-- 搜索查询 -->
	<%@ include file="/WEB-INF/views/modules/distribution/form.jsp"%>
	<!-- 产品/团期 -->
	<div class="supplierLine">
		<a onclick="productOrGroupList('product');" href="javascript:void(0)" id="productLabel">产品列表</a> 
		<a onclick="productOrGroupList('group');" href="javascript:void(0)" id="groupLabel">团期列表</a>
	</div>
	<!-- 排序 -->
	<%@ include file="/WEB-INF/views/modules/distribution/orderBy.jsp" %>
	
	<c:choose>
		<c:when test="${productOrGroup == 'product' }">
			<%-- 产品列表 --%>
			<%@ include file="/WEB-INF/views/modules/distribution/travelActivityList.jsp" %>
		</c:when>
		<c:otherwise>
			<%-- 团期列表 --%>
			<%@ include file="/WEB-INF/views/modules/distribution/productGrouplist.jsp" %>
		</c:otherwise>
	</c:choose>
</div>
<div id="qrhlId" style="display:none">
	<div>
		<div class="pull-left wechat_left margin_jbox">
			<img id="batchImgId" src="">
		</div>
		<div class="pull-left wechat_right wechat_right_jbox">
			<div class="wechat_top wechat_top_jbox">
				<div>扫描二维码分销至微信好友、朋友圈</div>
			</div>
			<div class="wechat_bottom">
				<div>打开微信，进入“扫一扫”</div>
                <div>从“发现”进入“扫一扫”</div><br>
                <%--微信二维码转发调整 ymx 2017/3/24 Start--%>
                <div class="gray_text">右键图片，点击另存为，即可将二维码保存至本地</div>
                <%--微信二维码转发调整 ymx 2017/3/24 End--%>
			</div>
		</div>
	</div>
</div>
<div class="page">
	<div class="pagination" style="overflow: visible">
		<dl>
			<dt>
				<c:choose>
					<c:when test="${productOrGroup == 'product' }">
	            		<input name="allChk" onclick="checkallForActivity(this)" type="checkbox">全选
					</c:when>
					<c:otherwise>
						<input name="allChk" onclick="checkall(this)" type="checkbox">全选
					</c:otherwise>
				</c:choose>
	        </dt>
	        <dd>
	            <%--<a onclick="generateBatchQrCode();">批量转发</a>--%>
				<input class="btn ydbz_x" type="button" value="批量转发" onclick="generateBatchQrCode();">
	            <%--<a onclick="generateAllQrCode();">全部转发</a>--%>
				<input class="btn ydbz_x" type="button" value="全部转发" onclick="generateAllQrCode();">
			</dd>
	    </dl>
	</div>
    <div class="pagination"  style="overflow: visible">
        <div class="endPage">
            ${page}
        </div>
        <div style="clear:both;"></div>
    </div>
</div>
</body>
</html>