<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <title>酒店产品修改</title>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.mCustomScrollbar.css" />
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.js"></script>
    <!--树形插件的脚本-->
    <script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.excheck-3.5.js"></script>
    <!--树形插件的脚本-->              
    <script type="text/javascript" src="${ctxStatic}/js/jquery.mousewheel.min.js"></script>
    <!--滚动条插件脚本-->               
    <script type="text/javascript" src="${ctxStatic}/js/jquery.mCustomScrollbar.js"></script>
    <!--滚动条插件脚本-->              
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <!--产品模块的脚本-->              
    <script type="text/javascript" src="${ctxStatic}/js/tmp.products.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/handlebars.runtime-v3.0.3.js"></script>
    <!-- json2格式化 -->
	<script type="text/javascript" src="${ctxStatic }/json/json2.js" ></script>
    <script type="text/javascript">
    var $ctx = "${ctx}";
  //表单提交
    function formSubmit(obj){
	    //提交前验证
	    var activityName = $("#orderNum").val();
	    if(activityName==null|| $.trim(activityName)==""){
			top.$.jBox.tip('请填写产品名称');
			return false;
		}
	    var activitySerNum = $("#orderPersonName").val();
	    if(activitySerNum==null|| $.trim(activitySerNum)==""){
			top.$.jBox.tip('请填写控房单号');
			return false;
		}
	  
		//var status = $(obj).attr("dataStatus");
		var url="${ctx}/activityHotel/updateActivityHotel";
		$(obj).attr("disabled", "disabled");
		$(obj).siblings().attr("disabled", "disabled");
		$.post(url,$("#editActivityHotelForm").serialize(),
			function(data){
				if(data.message=="1"){
					$.jBox.tip("修改成功!");
					setTimeout(function(){window.close();},900);
				    window.opener.location.reload();
					//window.location.href=g_context_url+"/activityHotel/activityHotelList?status=0";
				}else if(data.message=="2"){
					$.jBox.tip("修改成功!");
					setTimeout(function(){window.close();},900);
				    window.opener.location.reload();
				}else if(data.message=="3"){
					$.jBox.tip(data.error,'warning');
					$(obj).attr("disabled", false);
					$(obj).siblings().attr("disabled", false);
				}else{
					$.jBox.tip('系统异常，请重新操作!','warning');
					$(obj).attr("disabled", false);
					$(obj).siblings().attr("disabled", false);
				}
			});
	}
        $(function () {
            $("#thirdStepDiv .mod_information_d8_2 select[name='country']").comboboxSingle();
            //显示联运/分段联运价格
            islandShowPrice();
            //搜索条件筛选
            launch();
            //产品名称文本框提示信息
            inputTips();
            //其它信息字数限制
            getAcitivityNameLength1(200);
            //操作浮框
            operateHandler();
        });
        //删除已上传的文件
        function deleteFile(thisDom, fileID) {
            $(thisDom).parent("li").remove();
        }

        var tree, $key, lastValue = "", nodeList = [];
        $(document).ready(function () {
            var setting = {
                check: {
                    enable: true,
                    nocheckInherit: true
                }, view: {
                    selectedMulti: false,
                    fontCss: function (treeId, treeNode) {
                        return (!!treeNode.highlight) ? { "font-weight": "bold", "color": "#ff0000" } : { "font-weight": "normal", "color": "#333333" };
                    }

                }, data: {
                    simpleData: { enable: true }
                }, callback: {
                    beforeClick: beforeClick,
                    onCheck: onCheck
                }
            };
            function beforeClick(id, node) {
                tree.checkNode(node, !node.checked, true, true);
                return false;
            }

            // 用户-菜单
            var zNodes = [
                { id: 1, pId: 0, name: "北京俄风行国际旅行有限公司" },
                { id: 11, pId: 1, name: "北京分公司" },
                { id: 12, pId: 11, name: "张三" },
                { id: 13, pId: 11, name: "李四" },
                { id: 131, pId: 11, name: "王五" },
                { id: 5, pId: 1, name: "河北分公司" },
                { id: 51, pId: 5, name: "王河北" },
                { id: 52, pId: 5, name: "李小红" },
                {
                    id: 6, name: "上海俄风行国际旅行有限公司",
                    children: [
                        { id: 61, name: "小王" },
                        { id: 62, name: "小李" }
                    ]

                }
            ];
            // 初始化树结构
            tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
            // 默认选择节点
            //var ids = $("[name='menuIds']").val().split(",");
            //	for(var i=0; i<ids.length; i++) {
            //		var node = tree.getNodeByParam("id", ids[i]);
            //		try{tree.checkNode(node, true, true);}catch(e){}
            //	}
            // 默认展开全部节点
            tree.expandAll(true);
            //美化滚动条
            var $_roleLeftCen = $('.role-leftCen');
            var $_roleRightCen = $('.role-rightCen');

            $_roleLeftCen.mCustomScrollbar();
            $_roleRightCen.mCustomScrollbar();

            function onCheck(e, treeId, treeNode) {
                var nodes = tree.getCheckedNodes(true);
                var $hasAdd = $("#addArea").find("li");
                var str_html = '';
                //遍历添加新增选项
                for (var i = 0; i < nodes.length; i++) {
                    //msg += nodes[i].name+"--"+nodes[i].id+"--"+nodes[i].pId+"\n";
                    if (!nodes[i].isParent) {
                        var isInclude = 0;
                        $hasAdd.each(function (index, element) {
                            if (nodes[i].tId == $(element).attr("forID")) {
                                isInclude = 1;
                                return;
                            }
                        });
                        if (!isInclude) {
                            str_html += '<li forID="' + nodes[i].tId + '"><span>' + nodes[i].name + '</span><i>X</i></li>';
                        }
                    }
                }

                //遍历删除选项
                $hasAdd.each(function (index, element) {
                    var isInclude = 0;
                    for (var i = 0; i < nodes.length; i++) {
                        if (nodes[i].tId == $(element).attr("forID")) {
                            isInclude = 1;
                            return;
                        }
                    }
                    if (!isInclude) {
                        $(element).remove();
                    }
                });
                if ("" != str_html) {
                    $("#addArea").prepend(str_html);
                }

                //设置已添加城市数为0
                $(".role-rightTop span em").text($("#addArea li").length);
            }

            //删除已选择项目
            $("#addArea").on("click", "li i", function () {
                var $li = $(this).parent("li");
                var treeNode = tree.getNodeByTId($li.attr("forID"));
                tree.checkNode(treeNode, false, true);
                $li.remove();
            });

            //清空已添加的城市
            $(".role-rightTop p").click(function () {
                $.jBox.confirm("确定要清空数据吗？", "提示", function (v, h, f) {
                    if (v == 'ok') {
                        //取消结点树所有的可选项
                        tree.checkAllNodes(false);
                        //清除已选择项目
                        $("#addArea").empty();
                        //设置已添加城市数为0
                        $(".role-rightTop span em").text(0);
                    }
                });
            });

            //搜索
            $key = $("#key");
            $key.val("").focus(function (e) {
                if ($key.hasClass("empty")) {
                    $key.removeClass("empty");
                }
            }).blur(function (e) {
                if ($key.get(0).value === "") {
                    $key.addClass("empty");
                }
                searchNode(e);
            }).bind("change keydown cut input propertychange", searchNode);
        });
        //提交表单前进行数据处理
        function toSubmit() {
            //获取全部勾选的项目
            var nodesChecked = tree.getCheckedNodes(true);
            var arrayID = [];
            for (var i = 0; i < nodesChecked.length; i++) {
                if (!nodesChecked[i].isParent) {
                    arrayID.push(nodesChecked[i].id);
                }
            }
            $("[name='menuIds']").val(arrayID);
            //提交表单
            $("#inputForm")[0].submit();
        }

        //搜索
        function searchNode(e) {
            // 取得输入的关键字的值
            var value = $.trim($key.get(0).value);

            // 按名字查询
            var keyType = "name";
            if ($key.hasClass("empty")) {
                value = "";
            }

            // 如果和上次一致，就退出不查了。
            if (lastValue === value) {
                return;
            }

            // 保存最后一次
            lastValue = value;

            // 如果要查空字串，就退出不查了。
            if (value === "") {
                return;
            }
            updateNodes(false);
            nodeList = tree.getNodesByParamFuzzy(keyType, value);
            updateNodes(true);
        }
        function updateNodes(highlight) {
            for (var i = 0, l = nodeList.length; i < l; i++) {
                nodeList[i].highlight = highlight;
                tree.updateNode(nodeList[i]);
                tree.expandNode(nodeList[i].getParentNode(), true, false, false);
            }
        } 
    </script>
     <!--上传文件script开始-->
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
					/* var changname = $(obj).attr("name");
					var uploadfile = $(obj).parent().find(".uploadPath");
					uploadfile.find("input[name=hotelAnnexDocIda]").attr("name",changname);
					uploadfile.find("input[name=docOriName]").attr("name",changname);
					uploadfile.find("input[name=docPath]").attr("name",changname); */
					$(obj).parent().find("#currentFiles").find("[name='docOriName']").each(function(index, obj1) {
						var conditionname = $(obj).attr("name");
						var filename = $(obj1).val();
						var fileid = $(obj1).prev().val();
						if(conditionname=="VisaFile"){
							var filepath = $(obj1).next().val();
						    $(obj).parent().next(".batch-ol").append('<li><i class="sq_bz_icon"></i><span>'+ filename +'</span><a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(\''+ fileid +'\',\''+ $(obj).attr("name") +'\',this);">删除</a><input type="hidden" name="filedetail" value="'+ fileid+'#'+ filename+'#'+ filepath+'"/></li>');
						}else{
							$(obj).parent().next(".batch-ol").append('<li><i class="sq_bz_icon"></i><span>'+ filename +'</span><a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(\''+ fileid +'\',\''+ $(obj).attr("name") +'\',this);">删除</a></li>');	
						}
						
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
	
	 function addRecord() {
         var $temp = $("div.up_files_lists:first");
         $temp.parent().append($temp.clone().show());
         return false;
     }
	 
	function getLocalTime(nS) {
       	return new Date(parseInt(nS) * 1000).toLocaleString().replace(/:\d{1,2}$/,' ');
	} 

	</script>
    <!--上传文件script结束-->
    <!--级联查询开始 -->
    <script> 
 	//级联查询
        function getAjaxSelect(type,obj){
        	$.ajax({
        		type: "POST",
        	   	url: "${ctx}/hotelControl/ajaxCheck",
        	   	async: false,
        	   	data: {
        				"type":type,
        				"uuid":$(obj).val()
        			  },
        		dataType: "json",
        	   	success: function(data){
        	   		if(type == "island"){
        	   			$("#islandUuid").empty();
        	   			$("#islandUuid").append("<option value=''>请选择</option>");
        	   			$("#hotelUuid").empty();
        		   		$("#hotelUuid").append("<option value=''>请选择</option>");
        	   		} else if(type == "hotel"){
        	   			$("#hotelUuid").empty();
        		   		$("#hotelUuid").append("<option value=''>请选择</option>");
        	   		}else if(type=="hotelrank"){
        	   			$("#tableappend >tbody").empty();
        	   		}else {
        		   		$("#"+type).empty();
        	   		}
        	   		if(data){
        	   			if(type=="hotel"){
        		   			$.each(data.hotelList,function(i,n){
        		   					 $("#hotelUuid").append($("<option/>").text(n.nameCn).attr("value",n.uuid));
        		   			});
        	   			}else if(type=="roomtype"){
        	   				$.each(data.roomtype,function(i,n){
        	   					 $("#jbox_tq table.table_product_info > tbody > tr").eq(2).find("select:eq(0)").append($("<option/>").text(n.roomName).attr("value",n.uuid));
        	   				});
        	   			}else if(type=="foodtype"){
        	   				$.each(data.hotelMeals,function(i,n){
        	   					//$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("select:eq(0)").append($("<option/>").text(n.mealName).attr("value",n.uuid));
        	   					$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("td:eq(1) select:eq(0)").append($("<option/>").text(n.mealName).attr("value",n.uuid));
        	   					$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("td:eq(3) select:eq(0)").append($("<option/>").text(n.mealName).attr("value",n.uuid));
        	   				});
        	   			}else if(type=="islandway"){
        	   				$.each(data.listIslandWay,function(i,n){
	        	   				var islandwayTd = $("#jbox_tq table.table_product_info > tbody > tr").eq(4).find("td:eq(1)");
                     			$(islandwayTd).append($("<input/>").attr("class","redio_martop_4").attr("type","checkbox").attr("data-text",n.label).val(n.uuid));
                     			$(islandwayTd).append(n.label);
                     			$(islandwayTd).append($("<span/>").attr("class","mr25"));
                     			
        	   				});
        	   				$.each(data.hotelList,function(i,n){
        	   					 $("#hotelUuid").append($("<option/>").text(n.nameCn).attr("value",n.uuid));
        	   				});
        	   			}else if(type=="island"){
        	   				$.each(data.islandList,function(i,n){
        	   					 $("#islandUuid").append($("<option/>").text(n.islandName).attr("value",n.uuid));
        	   				});
        	   			}else if(type=="hotelrank"){
        	  				var stsrtnum =""; 
	        	   			for (var i = 0; i < data.hotelrank; i++){
	        	   				stsrtnum +='★';
	        	   			}
	        	   			var html="";
	        	   			$.each(data.delists,function(i,n){
	        	   				if(n.purchaseType==0){
	       	   					    html+=" <tr data-type='內采'>";
	        	   				}else{
	       	   					    html+=" <tr data-type='外采'>";
	        	   				}
	        	   				//n.hotelControlDetailUuid
	        	   				html+=" <td class='tc font_c66 new_hotel_p_table2_tdf' style='display: none'><span data-text='hotelControlDetailUuid' >"+n.hotelControlDetailUuid+"</span></td>";
        	   						html+=" <td class='tc font_c66 new_hotel_p_table2_tdf'>"+getLocalTime(n.inDate)+"</td>";
       	   						var roomNameNight="";
       	   						$.each(n.rooms,function(i,n){
       	   							if(n.roomName==null){
       	   								roomNameNight+="";
       	   							}else{
	       	   							roomNameNight+=n.roomName+"*"+n.night+"晚</br>";
       	   							}
       	   						});
       	   						
       	   						html+="	<td class='tc font_c66 '><p>"+roomNameNight+"</p></td>";
       	   						if(n.hotelMealName==null){
	       	   						html+=" <td class='tc font_c66'><p></p></td>";
       	   						}else{
	       	   						html+=" <td class='tc font_c66'><p>"+n.hotelMealName+"</p></td>";
       	   						}
       	   						if(n.islandWayName==null){
	       	   						html+=" <td class='tc font_c66'></td>";
       	   						}else{
	       	   						html+=" <td class='tc font_c66'>"+n.islandWayName+"</td>";
       	   						}
//        	   						html+=" <td class='tc font_c66'>";
//        	   						html+=""+n.stock+" </td>";
       	   					
       	   						if(n.stock!=null){
       	   							html+="<td class='tc font_c66'>"+n.stock+" </td>";
       	   						}else{
       	   							html+="<td class='tc font_c66'> </td>";
       	   						}

       	   						if(n.groundSupplier!=null){
	       	   						html+=" <td class='tc font_c66'>"+n.groundSupplier+"</td>";
       	   						}else{
       	   							html+=" <td class='tc font_c66'></td>";
       	   						}
	       	   					if(n.purchaseType==0){
	       	   						html+=" <td class='tc font_c66'>内采</td>";
	        	   				}else{
	       	   						html+=" <td class='tc font_c66'>外采</td>";
	        	   				}
       	   						html+=" <td class='tc'>";
       	   						html+=" <input type='text' data-type='number' data-min='0' class='inputTxt w50_30 spread ' />";
       	   						html+=" </td>";
       	   					    html+=" </tr>";
       	   					});
	        	   			//升餐
        	   				$.each(data.hotelMeals,function(i,n){
        	   					$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("td:eq(3) select:eq(0)").append($("<option/>").text(n.mealName).attr("value",n.uuid));
        	   				});
	        	   			$("#tableappend >tbody").append(html);
	        	   		    $("#hotelrank").html(stsrtnum);
        	   			}else if(type=="travelerTypeRelations"){
        	   				//酒店下绑定的游客类型
        	   				var html="";
        	   				var travelerType = $("#jbox_tq table.table_product_info > tbody > tr").eq(6).find("td:eq(1)");
	        	   			$.each(data.travelerTypeRelations,function(i,n){
								//$(travelerType).append("<div class='hotel_price_same_industry' name='hotelPrice' data-value='"+n.travelerTypeUuid+"'>");
						   		html+="<div class='hotel_price_same_industry' name='hotelPrice' data-value='"+n.travelerTypeUuid+"'>";
						   		html+="<span>"+n.travelerTypeName+"</span>";
						   		html+="<select name='peoplePrice' class='w50_30 currency'>";
					   			$.each(data.currencyList,function(i,m){
					   				html+="<option value='"+m.id+"'>"+m.currencyMark+"</option>";
					   			});
						   		html+="</select>";
						   		html+="<input type='text' data-type='float' value='' name='orderPersonName2' class='inputTxt w50_30 adultPrice mr25' />";
						   		html+="</div>";
	        	   			});
						   	$(travelerType).append(html);
        	   			}
        	   		}
        	   	}
        	});
        }
 	
        function getAjaxSelectFood(type,obj){
        	$.ajax({
        		type: "POST",
        	   	url: "${ctx}/hotelControl/ajaxCheck",
        	   	async: false,
        	   	data: {
        				"type":type,
        				"uuid":obj
        			  },
        		dataType: "json",
        	   	success: function(data){
        	   		if(data){
        	   			if(type=="foodtype"){
        	   				$.each(data.hotelMeals,function(i,n){
        	   					//$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("select:eq(0)").append($("<option/>").text(n.mealName).attr("value",n.uuid));
        	   					$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("td:eq(1) select:eq(0)").append($("<option/>").text(n.mealName).attr("value",n.uuid));
        	   					$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("td:eq(3) select:eq(0)").append($("<option/>").text(n.mealName).attr("value",n.uuid));
        	   				});
        	   			}
        	   		}
        	   	}
        	});
        }
 	
 	
        
 </script>
 <!--级联查询结束 -->
</head>
<body>
                    <!--右侧内容部分开始-->
                    <div class="mod_nav">产品 &gt; 酒店产品 &gt; 酒店产品修改</div>
                    <div class="produceDiv">
                    <form:form id="editActivityHotelForm" modelAttribute="activityHotelInput" action=""  method="post" class="form-horizontal" novalidate="">
                        <!--产品信息开始-->
                         <div class="mod_information" id="ofAnchor1">
                            <div class="mod_information mar_top0">
                                <div class="ydbz_tit island_productor_upload001"> <span class="ydExpand" data-target="#baseInfo"></span>基本信息</div>
                                <!-- <div style="margin-top:8px; min-width:1600px !important;" id="baseInfo"> -->
                                <div style="margin-top:30px; min-width:1600px !important;" id="baseInfo">
                                    <div class="activitylist_bodyer_right_team_co2 wpr20 pr" style="height:40px;">
                                        <div class="activitylist_team_co3_text">产品名称：</div>
                                        <input type="text" value="${activityHotelInput.activityName}" class="inputTxt inputTxtlong" name="activityName" id="orderNum" flag="istips" maxlength="128"/>
                                        <span class="ipt-tips">命名规则 行程时间-海岛-交通方式</span>
                                    </div>
                                    <input type="hidden" value="${activityHotelInput.uuid }" name="uuid"/>
                                    <div class="kong"></div>
                                    <div class="activitylist_bodyer_right_team_co1">
                                        <div class="activitylist_team_co3_text">控房单号：</div>
                                        <input type="text" value="${activityHotelInput.activitySerNum}" name="activitySerNum" class="inputTxt" id="orderPersonName" maxlength="250"/>
                                    </div>
					               <div class="activitylist_bodyer_right_team_co1">
                                        <div class="activitylist_team_co3_text" >国家：</div>
                                        <div class="activitylist_team_co3_text_contr">
                                        <trekiz:autoId2Name4Table tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${activityHotelInput.country}"/>
                                        </div>
                                    </div>
                                    <div class="activitylist_bodyer_right_team_co1">
                                        <div class="activitylist_team_co3_text">岛屿：</div>
                                        <div style="width:150px;" class="activitylist_team_co3_text_contr">
                                        <trekiz:autoId2Name4Table tableName="island" sourceColumnName="uuid" srcColumnName="island_name" value="${activityHotelInput.islandUuid}"/>
                                        <input id="hiislandUuid" type="hidden" value="${activityHotelInput.islandUuid}"/>
                                        </div>
                                    </div>
                                    <div class="kong"></div>
                                    <div class="activitylist_bodyer_right_team_co1">
                                        <div class="activitylist_team_co3_text">酒店名称：</div>
                                     <!--    <div class="activitylist_team_co3_text_contr" > -->
                                       <div style="width:100px;text-overflow:ellipsis; white-space:nowrap; overflow:hidden" class="activitylist_team_co3_text_contr">
                                       
                                        <input id="inputhotelUuid"  type="hidden" value="${activityHotelInput.hotelUuid}"/>
                                        <trekiz:autoId2Name4Table tableName="hotel" sourceColumnName="uuid" srcColumnName="name_cn" value="${activityHotelInput.hotelUuid}"/>
                                        </div>
                                    </div>
                                    <div class="activitylist_bodyer_right_team_co1">
                                        <div class="activitylist_team_co3_text">酒店星级：</div>
                                        <span class="y_xing" style="line-height:28px;"><c:forEach begin="1" end="${startlevel}" step="1">★</c:forEach></span>
                                    </div>
                                    <div class="activitylist_bodyer_right_team_co1">
                                        <div class="activitylist_team_co3_text">币种选择：</div>
                                        <div class="activitylist_team_co3_text_contr">
                                        <trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_name" value="${activityHotelInput.currencyId}"/>
                                        </div>
                                    </div>
                                 </div>   
                                    <table id="contentTable" class="table activitylist_bodyer_table_new sea_rua_table">
                                      <thead>
                                        <tr>
                                          <th width="5%">序号</th>
                                          <th width="5%">团号/日期</th>
                                          <th width="7%">房型 * 晚数</th>
                                          <th width="6%">基础餐型</th>
                                          <th width="9%">升级餐型&amp;升餐价格&amp;人</th>
                                          <th width="6%">上岛方式</th>
                                          <th width="8%">参考航班</th>
                                          <th width="8%">同行价/人</th>
                                          <th width="10%"><p>余位/间数/预报名</p></th>
                                          <th width="10%">单房差</th>
                                          <th width="10%">需交订金</th>
                                          <th width="10%">备注</th>
                                          <th width="6%" style="display: none"></th>
                                          <th width="6%">操作</th>
                                        </tr>
                                      </thead>
                                      <tbody>
                                        <tr style="display:none;">
                                          <td class="tc"><span class="sequence"></span></td>
                                          <td class="tc"><a href="#" target="_blank"></a> <br />
                                            <span></span></td>
                                          <td class="tc"></td>
                                          <td class="tc"></td>
                                          <td class="tc"></td>
                                          <td class="tc"></td>
                                          <td class="tc"></td>
                                          <td class="tl"></td>
                                          <td class="tc"></td>
                                          <td class="tr"></td>
                                          <td class="tr tdgreen"></td>
                                          <td class="tl"></td>
                                          <td class="tl">
                                          </td>
                                           <td class="tl" style="display: none">
                                          </td>
                                          <td class="p0"><dl class="handle">
                                            <dt> <img title="操作" src="${ctxStatic}/images/handle_cz.png" /></dt>
                                            <dd class="">
                                              <p> <span></span> 
                                              <a class="updateLink" href="javascript:void(0)">修改</a> 
                                              <a href="javascript:void(0)">详情</a> 
                                            </dd>
                                          </dl></td>
                                           
                                        </tr>
                                        <c:forEach var="hotelGroup" items="${activityHotelInput.activityhotelGroupLists}"  varStatus="v">
                                            <c:set var="rowspanNum" value="1" />
											<c:if
												test="${hotelGroup.activityHotelGroupMealList != null && fn:length(hotelGroup.activityHotelGroupMealList) > 0}">
												<c:set var="rowspanNum" value="${ fn:length(hotelGroup.activityHotelGroupMealList) }" />
											</c:if>
<!-- 											data-tag="abc00000001${v.index}" -->
                                        <tr id="${hotelGroup.uuid }" data-tag="${hotelGroup.uuid }">
                                          <td class="tc" rowspan="${rowspanNum}"><span class="sequence">${v.count}</span></td>
                                          <td class="tc" rowspan="${rowspanNum}"><a href="#" target="_blank">${hotelGroup.groupCode}</a> <br />
                                            <span><fmt:formatDate value="${hotelGroup.groupOpenDate}" pattern="yyyy-MM-dd" /></span></td>
                                            
                                          <c:forEach var="hotelGroupRoom" items="${hotelGroup.activityHotelGroupRoomList}" varStatus="r" end="0"> 
<!-- <<<<<<< .mine -->
<!--                                              <td rowspan="${ fn:length(hotelGroupRoom.activityHotelGroupMealList) }" class="tc"><span datag-value="${hotelGroupRoom.uuid}" data-value="${hotelGroupRoom.hotelRoomUuid}">${hotelGroupRoom.hotelRoomUuid}</span>*<span>${hotelGroupRoom.nights}</span>晚</td> -->
<!-- ======= -->
                                             <td rowspan="${ fn:length(hotelGroupRoom.activityHotelGroupMealList) }" class="tc">
                                             <span data-value="${hotelGroupRoom.hotelRoomUuid}">
                                             <trekiz:autoId2Name4Table tableName="hotel_room" sourceColumnName="uuid" srcColumnName="room_name" value="${hotelGroupRoom.hotelRoomUuid}"/>
                                             </span>*<span>${hotelGroupRoom.nights}</span>晚</td>

                                          </c:forEach>
                                          
                                           <c:forEach var="hotelGroupMeal" items="${hotelGroup.activityHotelGroupRoomList[0].activityHotelGroupMealList}" varStatus="m" end="0"> 
                                             <td class="tc" data-value="${hotelGroupMeal.hotelMealUuid}">
                                             <trekiz:autoId2Name4Table tableName="hotel_meal"	sourceColumnName="uuid" srcColumnName="meal_name" value="${hotelGroupMeal.hotelMealUuid}" /></td>
                                          </c:forEach>
                                          <td class="tc">
                                            <c:forEach var="hotelGroupMealRise" items="${hotelGroup.activityHotelGroupRoomList[0].activityHotelGroupMealList[0].activityHotelGroupMealsRiseList}" varStatus="n" >
                                             <p><span data-value="${hotelGroupMealRise.hotelMealUuid }">
                                             <trekiz:autoId2Name4Table tableName="hotel_meal"	sourceColumnName="uuid" srcColumnName="meal_name" value="${hotelGroupMealRise.hotelMealUuid }" />
                                             </span><span data-value="${hotelGroupMealRise.currencyId}"  data-uuid="${hotelGroupMealRise.currencyId}">
                                              <trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${hotelGroupMealRise.currencyId}"/>
                                             </span>
                                             <span>
                                             	<fmt:formatNumber value='${hotelGroupMealRise.price}' pattern='#,###.00'/>
                                             </span>
                                             /人
                                             </p>
                                            </c:forEach>
                                           </td>
                                           
                                         <%--  <td rowspan="2" class="tc"><span data-value="1">水上屋(2A+2C/3A+1C)</span>*<span>1</span>晚</td>
                                          <td class="tc" data-value="11">BB1</td>
                                          <td class="tc">
                                            <p><span data-value="111">HB1</span><span data-value="$">$</span><span>50</span>/人</p>
                                            <p><span data-value="111">HB1</span><span data-value="$">$</span><span>100</span>/人</p>
                                           </td> --%>
                                            
                                            
                                          <td class="tc" rowspan="${rowspanNum}">
                                              <c:forEach var="island" items="${fn:split(hotelGroup.islandWay,';')}">
                                           		<p  data-value="${island}">
                                           		<trekiz:defineDict name="island_way" type="islands_way"		defaultValue="${island}" readonly="true" />
                                           		</p>
                                           	  </c:forEach>
                                          </td>
                                          <td class="tc" rowspan="${rowspanNum}">${hotelGroup.airline}</td>
                                          <td class="tl" rowspan="${rowspanNum}">
                                            <c:forEach var="hotelGroupPrice" items="${hotelGroup.activityHotelGroupPriceList}" varStatus="s"> 
	                                            <p><span  data-value="${hotelGroupPrice.type}">
		                                            <trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${hotelGroupPrice.type}"/>
		                                             </span>：
<!-- 		                                            <span data-value="${hotelGroupPrice.uuid}"> -->
		                                            <span data-value="${hotelGroupPrice.currencyId}">
		                                            <trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${hotelGroupPrice.currencyId}"/>
		                                            </span>
		                                           <span>
		                                           <fmt:formatNumber value='${hotelGroupPrice.price}' pattern='#,###.00'/>
		                                         </span></p>
                                            </c:forEach>
                                          </td>
                                          <td class="tc" rowspan="${rowspanNum}"><span class="tdred over_handle_cursor" data-ctrlroompriority="false" title="剩余控房：${hotelGroup.controlNum}间<c:if test="${hotelGroup.priorityDeduction=='1'}">（优先扣减）</c:if>+剩余非控房：${hotelGroup.uncontrolNum}间<c:if test="${hotelGroup.priorityDeduction=='2'}">（优先扣减）</c:if>">${hotelGroup.remNumber}</span>/<span data-unctrlroom="${hotelGroup.uncontrolNum}" data-ctrlroom="${hotelGroup.controlNum}">${hotelGroup.remNumber}</span>/${hotelGroup.preApplyNum}</td>
                                          <td class="tr" rowspan="${rowspanNum}">
	                                          <span data-value="${hotelGroup.currencyId}"><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${hotelGroup.currencyId}"/></span>
	                                          <span>
	                                        	  <fmt:formatNumber value='${hotelGroup.singlePrice}' pattern='#,###.00'/>	
	                                          </span>
	                                          <c:if test="${hotelGroup.singlePriceUnit=='1'}"><span data-value="1">/人</span></c:if>
	                                          <c:if test="${hotelGroup.singlePriceUnit=='2'}"><span data-value="2">/间</span></c:if>
	                                          <c:if test="${hotelGroup.singlePriceUnit=='3'}"><span data-value="3">/晚</span></c:if>
                                          </td>
                                          <td class="tr tdgreen" rowspan="${rowspanNum}">
	                                          <span data-value="${hotelGroup.frontMoneyCurrencyId}"><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${hotelGroup.frontMoneyCurrencyId}"/></span>
	                                          <span class="fbold">
	                                          	<fmt:formatNumber value='${hotelGroup.frontMoney}' pattern='#,###.00'/>	
	                                          </span>
                                          </td>
                                          <td class="tl" rowspan="${rowspanNum}">${hotelGroup.memo}</td>
                                          <td style="display: none"></td>
                                          <td class="p0" rowspan="${rowspanNum}"><dl class="handle">
                                            <dt> <img title="操作" src="${ctxStatic}/images/handle_cz.png" /></dt>
                                            <dd class="">
                                              <p> <span></span>  
                                              <a class="updateLink" href="javascript:void(0)">修改</a> 
                                              <a href="${ctx}/activityHotel/showActivityHotelDetail/${hotelGroup.uuid}?type=group" target="_blank">详情</a> 
                                            </dd>
                                          </dl></td>
                                         
                                        </tr>
                                        
                                      <c:forEach var="hotelGroupMeal" items="${hotelGroup.activityHotelGroupRoomList[0].activityHotelGroupMealList}" varStatus="m" begin="1">
                                       <tr data-tag="${hotelGroup.uuid }">
                                         <td class="tc" data-value="${hotelGroupMeal.hotelMealUuid}">
                                          <trekiz:autoId2Name4Table tableName="hotel_meal"	sourceColumnName="uuid" srcColumnName="meal_name" value="${hotelGroupMeal.hotelMealUuid}" />
                                         </td>
                                         <td class="tc">
                                          <c:forEach var="hotelGroupMealRise" items="${hotelGroupMeal.activityHotelGroupMealsRiseList}" varStatus="n" >
                                            <p><span data-value="${hotelGroupMealRise.hotelMealUuid }">
                                            <trekiz:autoId2Name4Table tableName="hotel_meal"	sourceColumnName="uuid" srcColumnName="meal_name" value="${hotelGroupMealRise.hotelMealUuid }" />
                                            </span><span data-value="${hotelGroupMealRise.currencyId}">
                                           <trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${hotelGroupMealRise.currencyId}"/>
                                            </span>
                                            <span>
                                            	<fmt:formatNumber value='${hotelGroupMealRise.price}' pattern='#,###.00'/>	
                                            </span>/人</p>
                                          </c:forEach> 
                                         </td>
                                       </tr>
                                       </c:forEach> 
                                       
                                       <c:forEach var="hotelGroupRoom" items="${hotelGroup.activityHotelGroupRoomList}" varStatus="r" begin="1">
	                                       <tr data-tag="${hotelGroup.uuid }">
	                                          <td class="tc" rowspan="${ fn:length(hotelGroupRoom.activityHotelGroupMealList) }"><span data-value="${hotelGroupRoom.hotelRoomUuid}">
	                                          <trekiz:autoId2Name4Table tableName="hotel_room" sourceColumnName="uuid" srcColumnName="room_name" value="${hotelGroupRoom.hotelRoomUuid}"/>
	                                          </span>*<span>${hotelGroupRoom.nights}</span>晚</td>
	                                          <c:forEach var="hotelGroupMeals" items="${hotelGroupRoom.activityHotelGroupMealList}" varStatus="r" end="0">
		                                          <td class="tc" data-value="${hotelGroupMeals.hotelMealUuid}">
		                                           <trekiz:autoId2Name4Table tableName="hotel_meal"	sourceColumnName="uuid" srcColumnName="meal_name" value="${hotelGroupMeals.hotelMealUuid}" />
		                                          </td>
		                                          <td class="tc">
	                                        		 <c:forEach var="hotelGroupMealRiseV" items="${hotelGroupMeals.activityHotelGroupMealsRiseList}" varStatus="n" >
			                                            <p><span data-value="${hotelGroupMealRiseV.hotelMealUuid }">
			                                            <trekiz:autoId2Name4Table tableName="hotel_meal"	sourceColumnName="uuid" srcColumnName="meal_name" value="${hotelGroupMealRiseV.hotelMealUuid }" />
			                                            </span><span data-value="${hotelGroupMealRiseV.currencyId}">
			                                           <trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${hotelGroupMealRiseV.currencyId}"/>
			                                            </span><span>${hotelGroupMealRiseV.price}</span>/人</p>
			                                          </c:forEach> 
	                                        	  </td>
	                                          </c:forEach>
	                                        </tr>
		                                       <c:forEach var="hotelGroupMealH" items="${hotelGroupRoom.activityHotelGroupMealList}" varStatus="r"  begin="1">
			                                        <tr data-tag="${hotelGroup.uuid }">
			                                           <td class="tc" data-value="">
			                                           <trekiz:autoId2Name4Table tableName="hotel_meal"	sourceColumnName="uuid" srcColumnName="meal_name" value="${hotelGroupMealH.hotelMealUuid}" />
                                                       </td>
				                                          <td class="tc">
		                                                   <c:forEach var="hotelGroupMealRises" items="${hotelGroupMealH.activityHotelGroupMealsRiseList}" varStatus="r"  >
				                                            <p><span data-value="${hotelGroupMealRises.uuid }">
				                                             <trekiz:autoId2Name4Table tableName="hotel_meal"	sourceColumnName="uuid" srcColumnName="meal_name" value="${hotelGroupMealRises.hotelMealUuid }" />
				                                            </span><span data-value="${hotelGroupMealRise.currencyId}">
				                                            <trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${hotelGroupMealRises.currencyId}"/>
				                                            </span><span>${hotelGroupMealRises.price}</span>/人</p>
				                                             </c:forEach>
				                                          </td>
			                                        </tr> 
		                                       
		                                      </c:forEach>
                                        </c:forEach>
                                        
                                       </c:forEach>
                                      </tbody>
                                    </table>
                                </div>
                            </div>
                        <!--产品信息结束-->
                        <!--上传资料开始-->
                        <div style="clear:none;" class="kong"></div>
                        <div id="thirdStepDiv">
                            <!-- 上传文件 -->
                            <div class="ydbz_tit island_productor_upload001"> <span class="ydExpand" data-target="#up_fiels"></span>上传资料</div>
                            <div class="mod_information_3 upload_file_new" id="up_fiels">
						       <div class="batch"  style="margin-top:10px;">
				                <p class="maintain_pfull new_kfang">
					                <label class="batch-label company_logo_pos">产品行程介绍：</label>
					                <input class="mod_infoinformation3_file" type="button" onclick="uploadFiles('${ctx}',null,this)" value="上传文件" name="DocIdPro"><em class="tips_color">多文件，多格式上传附件。单个附件不大于20M，总共不大于20M。</em>
				                </p>
			                    <ol class="batch-ol">
			                    <c:forEach items="${prodSchList}" var="file" varStatus="s1">
									<li>
								    	<i class="sq_bz_icon"></i>
										<span>${file.docName}</span>
										<input type="hidden" name="DocIdPro" value="${file.docId}"/>
										<input type="hidden" name="docOriName" value="${file.docName}"/>
										<input type="hidden" name="docPath" value="${file.docPath}"/>
										<a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(null,'hotelAnnexDocId',this)">删除</a>
									</li>
								</c:forEach>
			                    </ol>
			                  </div>
		                  <div class="mod_information_d7"></div>
		                 <div class="batch"  style="margin-top:10px;">
				                <p class="maintain_pfull new_kfang">
					                <label class="batch-label company_logo_pos">自费补充协议：</label>
				                <input class="mod_infoinformation3_file" type="button" onclick="uploadFiles('${ctx}',null,this)" value="上传文件" name="DocIdCost"><em class="tips_color">多文件，多格式上传附件。单个附件不大于20M，总共不大于20M。</em>
				                </p>
			                    <ol class="batch-ol">
			                    <c:forEach items="${costProtocolList}" var="file" varStatus="s1">
									<li>
								    	<i class="sq_bz_icon"></i>
										<span>${file.docName}</span>
										<input type="hidden" name="DocIdCost" value="${file.docId}"/>
										<input type="hidden" name="docOriName" value="${file.docName}"/>
										<input type="hidden" name="docPath" value="${file.docPath}"/>
										<a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(null,'hotelAnnexDocId',this)">删除</a>
									</li>
								</c:forEach>
			                    </ol>
			                  </div>
		                  <div class="mod_information_d7"></div>
		                  <div class="batch"  style="margin-top:10px;">
				                <p class="maintain_pfull new_kfang">
					               <label class="batch-label company_logo_pos">其他补充协议：</label>
				                   <input class="mod_infoinformation3_file" type="button" onclick="uploadFiles('${ctx}',null,this)" value="上传文件" name="DocIdOther"><em class="tips_color">多文件，多格式上传附件。单个附件不大于20M，总共不大于20M。</em>
				                </p>
			                    <ol class="batch-ol">
			                    <c:forEach items="${otherProtocolList}" var="file" varStatus="s1">
									<li>
								    	<i class="sq_bz_icon"></i>
										<span>${file.docName}</span>
										<input type="hidden" name="DocIdOther" value="${file.docId}"/>
										<input type="hidden" name="docOriName" value="${file.docName}"/>
										<input type="hidden" name="docPath" value="${file.docPath}"/>
										<a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(null,'hotelAnnexDocId',this)">删除</a>
									</li>
								</c:forEach>
			                    </ol>
			                  </div>
		                  <div class="mod_information_d7"></div>
                        </div>
                        <div class="ydbz_tit island_productor_upload001 maring_top10"> <span class="ydExpand" data-target="#other_info"></span>其他信息</div>
                        <div class="other_info" id="other_info">
                            <textarea class="add_new_seatu_qt" onkeyup="getAcitivityNameLength1(200)" id="acitivityName" name="memo" value="${activityHotelInput.memo}">${activityHotelInput.memo}</textarea>
                            <br />
                            <span style="color:#b2b2b2" class="acitivityNameSizeSpan">还可输入<span class="acitivityNameSize">200</span>个字</span>
                        </div>
                        <div class="ydbz_tit island_productor_upload001 maring_top10"> <span class="ydExpand" data-target="#productor_sel"></span>产品分享</div>
                        <%@ include file="/WEB-INF/views/include/userShareTree.jsp"%>
                        <div class="release_next_add">
                            <input type="button" value="取消" onclick="window.close();" class="btn btn-primary gray" />
                            <input type="button" value="保存修改"   onclick="formSubmit(this)" class="btn btn-primary" />
                           <!--  <input type="button" value="提交"  dataStatus="1" onclick="formSubmit(this)" class="btn btn-primary" /> -->
                        </div>
                    </div>
                    <!--第三步上传资料结束-->
                <!--右侧内容部分结束-->
    </form:form>
    <!--新增团期信息项弹出层开始-->
    <div id="jbox_hotel_add_product_fab" style="display:none;">
        <div class="add_product_info new_hotel_p_table">
            <table class="table_product_info " style="width:900px !important; " id="productInfoTable">
                <tr>
                    <td width="133" class="tr" style="width:121px !important;"><span class="xing">*</span>团号</td>
                    <td colspan="3"><input readonly="readonly" type="text" class="inputTxt w106 spread" /> <span class="new_flight_control"></span></td>
                </tr>
                <tr>
                    <td class="tr"><span class="xing">*</span>日期</td>
                    <td colspan="3">
                        <input type="text" onclick="WdatePicker()" id="tdate"  class="dateinput w106 required " readonly="readonly" />
                        <a href="${ctx }/hotelPl/list?country=${activityHotelInput.country}&hotelUuid=${activityHotelInput.hotelUuid}&islandUuid=${activityHotelInput.islandUuid}" target="_blank">[价单价格信息]</a>
                    </td>
                </tr>
                <tr class="houseTypeTr">
                    <td class="tr" id="dash_line_blue">房型(容住率)*晚数</td>
                    <td colspan="3" id="dash_line_blue">
                        <p class="houseType">
                         	    <span>
                                	<select name="roomtype" id="roomtype" onchange="getAjaxSelect('foodtype',this);">
									<option value="">请选择</option>
									
								</select>
                                <span class="w50_30">*</span>
                                <input type="text" data-type="number" data-min="1" class="inputTxt w50_30" />
                                <span class="w50_30">晚数</span> <a class="ydbz_x  addHouseType">新增</a>
                            </span>
                        </p>
                    </td>
                </tr>
                <tr class="houseTypeTr mealTypeTr">
                    <td class="tr">基础餐型</td>
                    <td colspan="3">
                        <select class="w80" id="foodtype">
                           <option value="">请选择</option>
                        </select>
                        <a class="ydbz_x addMealType">新增</a> <span class="padr10"></span> <span>
                            <input class="redio_martop_4" type="checkbox" onselect=""/>
                            升级餐型
                        </span>
                    </td>
                    <td class="tr new_hotel_p_table2_tdblue dash_line_blue" style="display:none;"><span>升级餐型</span></td>
                    <td style="display:none;">
                        <p class="upMealType">
                            <span>
                                <select name="select10" class="w80 mr3">
           		                     <option value="">请选择</option>
                                </select>
                                <select name="select10" class="w50_30 mr3 currency">
  									 <c:forEach items="${currencyList}" var="item">
									    <option value="${item.id}" >
								    		${item.currencyMark}
								    	</option>
								     </c:forEach>
                                </select>
                                <input type="text" data-type="float" class="inputTxt w50_30" />
                                <a class="ydbz_x addUpMealType">新增</a>
                            </span>
                        </p>
                    </td>
                </tr>
                <tr class="islandTr">
                    <td class="tr">上岛方式</td>
                    <td colspan="3">
							
                    </td>
                </tr>
                <tr>
                    <td class="tr">参考航班</td>
                    <td colspan="3">
                        <input type="text" class="inputTxt w106 spread" />
                        <span class="new_flight_control">注：多个航班号请用逗号隔开</span></a>
                    </td>
                </tr>
                <tr>
                    <td class="tr">同行价格/人</td>
                    <td colspan="3">
<!--                     	<c:forEach items="${travelerTypes }" var="traveler"> -->
<!-- 	                        <div class="hotel_price_same_industry" id="hotelPrice" data-value="${traveler.uuid }"> -->
<!-- 	                            <span>${traveler.name }</span> -->
<!-- 	                            <select name="select8" class="w50_30 currency"> -->
<!-- 	                                 <c:forEach items="${currencyList}" var="item"> -->
<!-- 									    <option value="${item.id}" > -->
<!-- 								    		${item.currencyMark} -->
<!-- 								    	</option> -->
<!-- 								     </c:forEach> -->
<!-- 	                            </select> -->
<!-- 	                            <input type="text" data-type="float" value="" name="orderPersonName2" class="inputTxt w50_30 adultPrice mr25" /> -->
<!-- 	                        </div> -->
<!--                     	</c:forEach> -->
                    </td>
                </tr>
                <tr>
                    <td class="tr">控房间数</td>
                    <td >
                        <input type="text" data-type="number" class="inputTxt w50_30 spread fl " value="0" readonly="readonly" />

                        <a class="fl maring_left10 selLink" style="position:relative;">
                                        选择
                            <div class="pop_inner_outer_hotel">
                                <div class="confirm_inner_outer_sel">
                                    <span class="mr25">
                                        <input class="redio_martop_4 procurement" type="checkbox" data-text="內采" checked="checked" />
                                        內采
                                    </span>
                                    <span class="mr25">
                                        <input class="redio_martop_4 procurement" type="checkbox" data-text="外采" />
                                        外采
                                    </span>
                                </div>
                                <table class="table  activitylist_bodyer_table_new" id="tableappend">
                                    <thead>
                                        <tr>
                                 	        <th width="16%" style="display: none">uuid</th>
                            	            <th width="16%">入住日期</th>
                                            <th width="14%">房型&amp;晚数</th>
                                            <th width="10%">基础餐型</th>
                                            <th width="11%">上岛方式</th>
                                            <th width="12%">余位/库存</th>
                                            <th width="16%">地接供应商</th>
                                            <th width="9%">采购类型</th>
                                            <th width="12%">使用库存数</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                      		
                                    </tbody>
                                </table>
                                <div class="btn_confirm_inner_outer"><input type="button" class="btn_confirm_inner_outer02 maring_bottom10 up_load_visa_info_btn_del" value="确定" /></div>
                            </div>
                        </a>
<!--                         <input  name="detailUuid" value=""/> -->
<!--                     	<input  name="detailNum" value=""/> -->
                    </td>
                    <td width="133" class="tr new_hotel_p_table2_tdblue">非控房间数</td>
                    <td><input type="text" data-type="number" class="inputTxt w106 spread" /></td>
                </tr>
                <tr>
                    <td class="tr">优先扣减</td>
                    <td colspan="3">
                        <span class="mar_left_35">
                            <input class="redio_martop_4" type="radio" name="kp_radio" id="kp_radio2" checked="checked" />
                            <label for="kp_radio2">控票数</label>
                        </span> <span class="mar_left_35">
                            <input class="redio_martop_4" type="radio" name="kp_radio" id="fkp_radio2" />
                            <label for="fkp_radio2">非控票数</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td class="tr">单房差</td>
                    <td>
                        <span class="add_jbox_repeat_thj">
                            <select name="select9" class="w50_30 currency">
                                  <c:forEach items="${currencyList}" var="item">
									    <option value="${item.id}" >
								    		${item.currencyMark}
								    	</option>
								  </c:forEach>
                            </select>
                            <input type="text" data-type="float" class="inputTxt w50_30 babyPrice mr25" />
                            <select name="select9" class="w60_30 currency">
                                <option>/人</option>
                                <option>/间</option>
                                <option>/晚</option>
                            </select>
                        </span>
                    </td>
                    <td width="133" class="tr new_hotel_p_table2_tdblue">需交订金</td>
                    <td>
                        <select name="select11" class="w50_30">
                              <c:forEach items="${currencyList}" var="item">
									    <option value="${item.id}" >
								    		${item.currencyMark}
								    	</option>
						      </c:forEach>
                        </select>
                        <input type="text" data-type="float" class="inputTxt w50_30 spread" />
                    </td>
                </tr>
                <tr>
                    <td class="tr valign_top">备注</td>
                    <td colspan="3"><textarea name="textarea" class="inputTxt spread" style=" width:90%; height:100px;"></textarea></td>
                </tr>
                <tr >
                    <td class="tr valign_top" style="display: none">json</td>
                    <td colspan="3" >
                    	<input type="hidden" name="jsoninput" value=""/>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <!--新增团期信息项弹出层结束-->
    
    <script type="text/javascript" src="${ctxStatic }/js/hotelProductedit.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {

            // 国家--岛屿--酒店联动
            $("#country").on('change', function () {
                var $island = $("#island").empty();
                var country = countrys[$("#country").val()] || {};
                var islands = country.islands;
                for (var key in islands) {
                    $island.append('<option value="' + key + '">' + islands[key].name + '</option>');
                }
                $island.change();
            }).change();
            $("#island").on('change', function () {
                var $hotel = $("#hotel").empty();
                var country = countrys[$("#country").val()] || {};
                var island = country.islands[$("#island").val()] || {};
                var hotels = island.hotels;
                for (var key in hotels) {
                    $hotel.append('<option value="' + key + '">' + hotels[key].name + '</option>');
                }
                $hotel.change();
            });
            $("#hotel").on('change', function () {
                // 获取酒店的房型、餐型和升级餐型
                // 为houseTypes 赋值
                // houseTypes = {};
            });

            $("#addGroup").on('click', function () {
                var options = {
                    isAdd: true,
                    callback: writeRow,
//                     houseTypes: houseTypes
                }
                addGroup_box(options);
            });

            $("#contentTable").on('click', 'a.copyLink', function () {
                var id = $("#contentTable tbody tr").has(this).prop('id');
                var options = {
                    isAdd: true,
                    callback: writeRow,
                    id: id,
//                  houseTypes: houseTypes,
                    data: readRow(id)
                }
                addGroup_box(options);
            }).on('click', 'a.updateLink', function () {
                var id = $("#contentTable tbody tr").has(this).prop('id');
                var islandUuid = $("#hiislandUuid").val();
                var options = {
                    isAdd: false,
                    callback: writeRow,
                    id: id,
//                  houseTypes: houseTypes,
                    data: readRow(id),
                    islandUuid:islandUuid
                }
                addGroup_box(options);
            }).on('click', 'a.delLink', function () {
                var id = $("#contentTable tbody tr").has(this).prop('id');
                $("#contentTable tbody tr[data-tag='" + id + "']").remove();
            });

            $("#up_fiels").on("click", "i.del_fj_icon", function () {
                // 删除上传的文件
                $("ul.up_files_outer li").has(this).remove();
            }).on("click", "input.up_load_visa_info_btn_del", function () {
                // 删除签证资料
                $("div.up_files_lists").has(this).remove();
            }).on('click', "input.up_load_visa_info_btn", function () {
                var $temp = $("div.up_files_lists:first");
                $temp.parent().append($temp.clone().show());
            });

            function readRow(id) {
                var $row = $("#" + id);
                if (!$row.length) return;

                var data = {hotelGroupUuid:id};
				
                var $tds = $row.find("td");
                var cIndex = 0;
                var $td;
                // 序号
                cIndex++;
                // 团号
                data.NO = $tds.eq(cIndex).find('a').text();
                // 日期
                data.date = $tds.eq(cIndex).find('span').text();
                cIndex++;
                // 房型 * 晚数
                data.houseTypes = [];
                var $tr = $row;
                for (var i = 0; $tr.is("tr[data-tag='" + id + "']") ; i++) {
                    $td = i ? $tr.find("td:first") : $tds.eq(cIndex);
                    var obj = {
//                     	grouproomuuid:$td.find("span:eq(1)").attr("datag-value"),
                        houseType: { value: $td.find("span:eq(0)").attr("data-value") },
                        night: $td.find("span:eq(1)").text()
                    };
                    obj.baseMealTypes = [];
                    var rowspan = (+$td.prop("rowspan") || 1);
                    for (var j = 0; j < rowspan; j++) {
                        var item = {};
                        if (j) {
                            $tr = $tr.next();
                            $td = $tr.find("td:first");
                        } else {
                            $td = $td.next();
                        }
                        // 基础餐型
                        item.mealType = { value: $td.attr("data-value") };
                        // 升级餐型
                        $td = $td.next();
                        item.upMealTypes = [];
                        $td.find("p").each(function () {
                        	var sc = $.trim($(this).find("span:eq(2)").text());
                        	sc=sc.replace(',','');
               				if(parseInt(sc)==sc){
               					sc=parseInt(sc);
               				}
                            item.upMealTypes.push({
                                mealType: { value: $(this).find("span:eq(0)").attr("data-value") },
                                currency: { value: $(this).find("span:eq(1)").attr("data-value") },
                                price: sc
                            });
                        });
                        obj.baseMealTypes.push(item);
                    }
                    data.houseTypes.push(obj);
                    $tr = $tr.next();
                }
                cIndex += 3;
                // 上岛方式
//                 data.trafficWays = [];
//                 $tds.eq(cIndex).find('p').each(function () {
//                     data.trafficWays.push($(this).text());
//                 });
				data.trafficWays = [];
                $tds.eq(cIndex).find('p').each(function () {
//                     data.trafficWays.push($(this).text());
                    data.trafficWays.push({
                    	trafficWaystext:$(this).text(),
                    	trafficWaysval :$(this).attr("data-value")
                    });
                });

                cIndex++;
                // 参考航班
                data.airlines = $tds.eq(cIndex).text();
                cIndex++;
                // 同行价
                data.tradePrices = [];
                $tds.eq(cIndex).find('p').each(function () {
                    var $this = $(this);
                    var txj = $.trim($this.find("span:eq(2)").text());
                    txj=txj.replace(',','');
    				if(parseInt(txj)==txj){
    					txj=parseInt(txj)
    				}
                    data.tradePrices.push({
                        currency: { value: $this.find("span:eq(1)").attr("data-value") },
                        price: txj,
                        uuid:$this.find("span:eq(0)").attr("data-value"),
                    });
                });
                cIndex++;
                // 余位/间数/预报名
                data.usedRoom = $tds.eq(cIndex).find("span:eq(1)").text() - $tds.eq(cIndex).find("span:eq(0)").text();
                data.ctrlRoom = $tds.eq(cIndex).find("span:eq(1)").attr("data-ctrlRoom");
                data.unCtrlRoom = $tds.eq(cIndex).find("span:eq(1)").attr("data-unCtrlRoom");
                data.ctrlRoomPriority = $tds.eq(cIndex).find("span:eq(0)").attr("data-ctrlRoomPriority");
                cIndex++;
                // 单房差
              	//去掉逗号后判断是否是整数，如果是只保留整数部分
                var dfc = $.trim($tds.eq(cIndex).find("span:eq(1)").text());
                dfc=dfc.replace(',','');
				if(parseInt(dfc)==dfc){
					dfc=parseInt(dfc)
				}
                data.priceDiff = {
                    currency: { value: $tds.eq(cIndex).find("span:eq(0)").attr("data-value") },
                    price: dfc,
                    unit: { value: $tds.eq(cIndex).find("span:eq(2)").attr("data-value") }
                };
                cIndex++;
                // 需交定金
                //去掉逗号后判断是否是整数，如果是只保留整数部分
                var dj = $.trim($tds.eq(cIndex).find('span:eq(1)').text());
                dj=dj.replace(',','');
				if(parseInt(dj)==dj){
					dj=parseInt(dj)
				}
                data.deposit = {
                    currency: { value: $tds.eq(cIndex).find('span:eq(0)').attr("data-value") },
                    price: dj
                };
                cIndex++;
                // 备注
                data.comment = $tds.eq(cIndex).text();
                return data;
            }

            function writeRow(data, options) {
                var $row = $("#" + options.id);

                if (options.isAdd) {
                    options.id = buildID();
                    $row.length || ($row = $("#contentTable tbody tr:first"));
                    $row = $row.clone().attr({ "id": options.id, "data-tag": options.id }).show();
                    $("#contentTable tbody").append($row);
                }

                $row.nextAll("tr[data-tag='" + options.id + "']").remove();
                var $tds = $row.find("td");

                var $td;

                var cIndex = 0;
                var html = [];
                // 序号
                setSequence();
                cIndex++;
                // 团期
                $tds.eq(cIndex).find('a').text(data.NO);
                // 日期
                $tds.eq(cIndex).find('span').text(data.date);
                cIndex++;
                // 房型
                var $tr = $row;
                $.each(data.houseTypes, function (i) {
                    if (i > 0) {
                        $tr.after('<tr><td class="tc"></td><td class="tc"></td><td class="tc"></td></tr>');
                        $tr = $tr.next();
                        $tr.attr('data-tag', options.id);
                        $td = $tr.find("td:first");
                    } else {
                        $td = $tds.eq(cIndex);
                    }
                    $td.attr("rowspan", this.baseMealTypes.length);
                    html = ['<span data-value="', this.houseType.value, '">', this.houseType.text, '</span>*<span>', this.night, '</span>晚'];
                    $td.html(html.join(''));
                    // 基础餐型
                    $.each(this.baseMealTypes, function (j) {
                        if (j) {
                            $tr.after('<tr><td class="tc"></td><td class="tc"></td></tr>');
                            $tr = $tr.next();
                            $tr.attr('data-tag', options.id);
                            $td = $tr.find("td:first");
                        } else {
                            $td = $td.next();
                        }
                        $td.text(this.mealType.text).attr("data-value", this.mealType.value);
                        //升级餐型
                        $td = $td.next();
                        html = [];
                        $.each(this.upMealTypes, function () {
                            var $this = $(this);
                            html.push('<p>');
                            html.push('<span data-value="', this.mealType.value, '">', this.mealType.text, '</span>');
                            html.push('<span data-value="', this.currency.value, '">', this.currency.text, '</span>');
                            html.push('<span>', this.price, '</span>/人');
                            html.push('</p>');
                        });
                        $td.html(html.join(''));
                    });
                });
                $tds.not(":eq(" + cIndex + "),:eq(" + (cIndex + 1) + "),:eq(" + (cIndex + 2) + ")").attr("rowspan", $("#contentTable tr[data-tag='" + options.id + "']").length);
                cIndex += 3;
                // 上岛方式
                html = [];
                $.each(data.trafficWays, function () {
//                     html.push('<p>', this, '</p>');
                	html.push('<p data-value="', this.trafficWaysval, '">', this.trafficWaystext, '</p>');
                });
                $tds.eq(cIndex).html(html.join(''));
                cIndex++;
                // 参考航班
                $tds.eq(cIndex).text(data.airlines);
                cIndex++;
                // 同行价
                html = [];
                $.each(data.tradePrices, function () {
                	html.push('<p><span data-value="',this.uuid,'">',this.type, '</span>：<span data-value="', this.currency.value, '">', this.currency.text, '</span><span>', this.price, '</span></p>');
                   // html.push('<p><span>', this.type, '</span>：<span data-value="', this.currency.value, '">', this.currency.text, '</span><span>', this.price, '</span></p>');
                });
                $tds.eq(cIndex).html(html.join(''));
                cIndex++;
                // 余位/间数/预报名
                data.usedRoom = (data.usedRoom || 0);
                html = ['<span class="tdred over_handle_cursor" data-ctrlRoomPriority="', data.ctrlRoomPriority, '" ',
                        'title="剩余控房：', data.ctrlRoom, '间', (data.ctrlRoomPriority ? '（优先扣减）' : ''), '+剩余非控房：', data.unCtrlRoom, '间', (!data.ctrlRoomPriority ? '（优先扣减）' : ''), '">', (data.ctrlRoom + data.unCtrlRoom - data.usedRoom), '</span>',
                        '/<span data-ctrlRoom="', data.ctrlRoom, '" data-unCtrlRoom="', data.unCtrlRoom, '">', (data.ctrlRoom + data.unCtrlRoom), '</span>/0'];
                $tds.eq(cIndex).html(html.join(''));
                cIndex++;
                // 单房差
                html = ['<span data-value="', data.priceDiff.currency.value, '">', data.priceDiff.currency.text, '</span>',
                        '<span>', data.priceDiff.price, '</span>',
                        '<span data-value="', data.priceDiff.unit.value, '">', data.priceDiff.unit.text, '</span>'];
                $tds.eq(cIndex).html(html.join(''));
                cIndex++;
                // 需交定金
                html = ['<span data-value="', data.deposit.currency.value, '">', data.deposit.currency.text, '</span>',
                        '<span class="fbold">', data.deposit.price, '</span>'];
                $tds.eq(cIndex).html(html.join(''));
                cIndex++;
                // 备注
                $tds.eq(cIndex).html(data.comment);
                
                cIndex++;
                $tds.eq(cIndex).html(data.json);
                return true;
            }

            function setSequence() {
                $("#contentTable tbody tr td:first-child span.sequence").each(function (i) {
                    $(this).text(i);
                });
            }
        });
    </script>
</body>
</html>