function downLoadZfpz(docInfoId) {
			var ctx = $("#ctx").val();
			var docIdArr = docInfoId.split(",");
			for (var i = 0;i < docIdArr.length; i++) {
				var docInfoId = docIdArr[i];
				if (docInfoId != "") {
					window.open(ctx + "/sys/docinfo/download/" + docIdArr[i]);
				}
			}
		}	
		
	
		//文件下载
		function downloads4SpecialDeman(docid) {
			
			var ctx = $("#ctx").val();
			//alert(ctx);
			window.open(ctx + "/sys/docinfo/download/"+docid);
		}
		
		/* 下载支付凭证 */
		/* $(document).delegate(".showpayVoucher", "click", function () {
			var orderIDValue = $(this).attr("lang");
			var contextPath = $("#ctx").val();
			$.ajax({
				type : "POST",
				url : contextPath + "/sys/docinfo/payVoucherList/" + orderIDValue,
				data : {
					orderId : orderIDValue
				},
				success : function (msg) {
					$.each(msg, function (key, value) {
						var id = value.id;
						window.open(contextPath + "/sys/docinfo/download/" + id);
					});
				}
			});
		}); */
        
//        function details(groupCode,activityId){
//        	alert('${ctx}');
//        	debugger;
//            var iframe = "iframe:${ctx}/activity/manager/viewDetail/" + activityId + "/" + groupCode;
//            $.jBox(iframe, {
//                title: "详情",
//                width: 880,
//                height: 630,
//                persistent: true,
//                buttons:false
//            });
//        }
		
      //弹出详情
        /*function details(groupCode,activityId){
            var iframe = "iframe:" + encodeURI(encodeURI("${ctx}/activity/manager/viewDetail/" + activityId + "/" + groupCode));
            $.jBox(iframe, {
                title: "详情",
                width: 880,
                height: 630,
                persistent: true,
                buttons:false
            });
        }*/
        
		
		//展开特殊需求
	    /* function details_all(obj){
	    	debugger;
	    	var $this=$(obj);
	    	$this.parent().parent().addClass("hide");
	    	$this.parent().parent().next().removeClass("hide");
	    } */
	    $(function(){
			$(".td_less").each(function(){
				if($(this).text().length>116){
					var str = $(this).text().substr(0,116) + " ...";
					$(this).text(str);
				}
			});
		})
		
		
		function downloads(docIds) {
			if(null==docIds || ''==docIds || 'undefined'==docIds){ //判断文档id是否为空
		        top.$.jBox.tip("没有行程单可供下载!");
		        return false;
		    }
			contextPath = $("#ctx").val();
			window.open(encodeURI(encodeURI(contextPath + "/sys/docinfo/zipdownload/" + docIds + "/行程单")));
		}
		
		
		/**
		 * 导出订单中关于游客信息 downloadData
		 * param orderId 订单ID或下载文件ID
		 * param downloadType 下载类型：游客资料、出团通知、确认单、面签通知
		 */
		function downloadData(orderId, downloadType) {
			//debugger;
			var contextPath =  $("#ctx").val();
			if ("traveler" == downloadType) {
				if (existData(orderId)) {
					$("#orderId").val(orderId);
					$("#downloadType").val(downloadType);
					$("#exportForm").submit();
				}
			} else if ("confirmation" == downloadType || "group" == downloadType) {
				window.open(encodeURI(encodeURI(contextPath + "/sys/docinfo/download/" + orderId)));
			}
		}
		
		/**
		 * 验证订单是否有游客信息
		 * param orderId 订单ID
		 */
		function existData(orderId) {
			var contextPath =  $("#ctx").val();
			var flag = false;
			$.ajax({
				type : "POST",
				async : false,
				url : contextPath + "/orderCommon/manage/existExportData",
				dataType : "json",
				data : {
					orderId : orderId
				},
				success : function (result) {
					var data = eval(result);
					if (data && data[0].flag == "true") {
						flag = true;
					} else {
						var tips = data[0].warning;
						top.$.jBox.info(tips, "警告", {
							width : 250,
							showType : "slide",
							icon : "info",
							draggable : "true"
						});
						top.$('.jbox-body .jbox-icon').css('top', '55px');
					}
				}
			});
			return flag;
		}