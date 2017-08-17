<%@ page contentType="text/html;charset=UTF-8" %>
<%@page isELIgnored="false"%>  
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <title>酒店产品发布</title>
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
    <!-- json2格式化 -->
	<script type="text/javascript" src="${ctxStatic }/json/json2.js" ></script>
	<style type="text/css">
        #ofAnchor1 .activitylist_team_co3_text{
            min-width: 90px;
        }
        #ofAnchor1 .ipt-tips{
            left: 95px;
        }
        .activitylist_bodyer_right_team_co1 {
        width:17%
        }
    </style>
    <script type="text/javascript">
 	   var $ctx = "${ctx}";
       var map={};
       //表单提交
       function formValidate(obj) {
        	var validate = true;
    		var groupcodeTextArr = $("#contentTable").find("a[name=groupcodeText]");
    		if(groupcodeTextArr.length==1){
    			top.$.jBox.tip('请填写团期信息');
    			return false;
    		}
    		for(var i=1; i<groupcodeTextArr.length; i++) {
    			
	    		$.ajax({
					type:"post",
					url:g_context_url+"/activityHotel/checkedGroup",
					data:{
						"groupCode":$(groupcodeTextArr[i]).text()
					},
					success:function(data){
						if(data && data.message=="true"){
							$.jBox.tip($(groupcodeTextArr[i]).text()+"，该团号已存在！！！");
							validate = false;
						}
					}
				});
				
    			if((i == groupcodeTextArr.length-1) && validate) {
    				formSubmit($(obj));
    			}
    		}
    		
        }
	    function formSubmit(obj){
    	    //提交前验证.
    	    var activityName = $("#orderNum").val();
    	    if(activityName==null|| $.trim(activityName)==""){
    			top.$.jBox.tip('请填写产品名称');
    			return false;
    		}
    	    var activitySerNum = $("#orderPersonName").val();
    	   // if(activitySerNum==null|| $.trim(activitySerNum)==""){
    		//	top.$.jBox.tip('请填写控房单号');
    		//	return false;
    		//}
    	    var country = $("#country").val();
    	    if(country==null|| $.trim(country)==""){
    			top.$.jBox.tip('请选择国家');
    			return false;
    		}
    	    var islandUuid = $("#islandUuid").val();
    	    if(islandUuid==null|| $.trim(islandUuid)==""){
    			top.$.jBox.tip('请选择岛屿');
    			return false;
    		}
    	    var hotelUuid = $("#hotelUuid").val();
    	    if(hotelUuid==null|| $.trim(hotelUuid)==""){
    			top.$.jBox.tip('请选择酒店');
    			return false;
    		}
    	    var currencyId = $("#currencyId").val();
    	    if(currencyId==null|| $.trim(currencyId)==""){
    			top.$.jBox.tip('请选择币种');
    			return false;
    		}
    		
			var status = $(obj).attr("dataStatus");
			var url="${ctx}/activityHotel/saveActivityHotel?status="+status;
			$(obj).attr("disabled", "disabled");
			$(obj).siblings().attr("disabled", "disabled");
			$.post(url,$("#saveActivityHotelForm").serialize(),
				function(data){
					if(data.message=="1"){
						$.jBox.tip("添加成功!");
						setTimeout(function(){window.close();},900);
						window.opener.location.reload();
						//window.location.href=g_context_url+"/activityHotel/activityHotelList?status=0";
					}else if(data.message=="2"){
						$.jBox.tip(data.error,'warning');
						$(obj).attr("disabled", false);
						$(obj).siblings().attr("disabled", false);
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
            //国家联想输入
     	    initSuggest({});
     	    g_context_url = "${ctx}";
     	    //默认初始化马岱下的岛屿
     	    getAjaxSelect('island',$('#country'));
     		//可输入select
     		$("#hotelUuid").comboboxInquiry().on('comboboxinquiryselect', function(event, obj){
     			//var value = obj.value;
     			getAjaxSelect('hotelrank',$("#hotelUuid")[0]);
     		}); 
     	    
        });
        //删除已上传的文件
        function deleteFile(thisDom, fileID) {
            $(thisDom).parent("li").remove();
        }
        
    </script>
    <script type="text/javascript">
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

         /*    //清空已添加的城市
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
            }); */

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
						var filename = $(obj1).val();
						var fileid = $(obj1).prev().val();
						$(obj).parent().next(".batch-ol").append('<li><i class="sq_bz_icon"></i><span>'+ filename +'</span><a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(\''+ fileid +'\',\''+ $(obj).attr("name") +'\',this);">删除</a></li>');	
						
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
	 //查看价单
	 function showQuote() {
		 var countryVal = $('#country').val();
		 var hotelVal = $('#hotelUuid').val();
		 var islandVal = $('#islandUuid').val();
		 var aurl = "${ctx }/hotelPl/list?country="+countryVal+"&hotelUuid="+hotelVal+"&islandUuid="+islandVal;
		 window.open(aurl);
	 }
	</script>
    <!--上传文件script结束-->
    <!--级联查询开始 -->
    <script> 
		function getLocalTime(nS) {
	       	return new Date(parseInt(nS) * 1000).toLocaleString().replace(/:\d{1,2}$/,' ');
		} 
		 //时间戳的转换
		function   formatToDate(nowTime)   {  
			var now = new Date(nowTime);
            var year=now.getFullYear(); 
            var month=now.getMonth()+1; 
            var date = now.getDate();
            return year+"-"+month+"-"+date;     
            } 
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
        		   		$("#hotelrank").html("");
        	   		} else if(type == "hotel"){
        	   			$("#hotelUuid").empty();
        	   			$("#hotelrank").html("");
        		   		$("#hotelUuid").append("<option value='' >请选择</option>");
        	   		    $("#hotelUuid").comboboxInquiry('reset');
        	   		}else if(type =="islandway"){
        	   			$("#jbox_tq table.table_product_info > tbody > tr").eq(4).find("td:eq(1)").html('');
        	   		}else if(type=="foodtype"){
        	   			$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("td:eq(1) select:eq(0)").empty();
        	   			$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("td:eq(1) select:eq(0)").append("<option value=''>请选择</option>");
        	   		}else if(type=="hotelrank"){
        	   			$("#tableappend >tbody").empty();
        	   		}else{
        		   		$("#"+type).empty();
        	   		}
        	   		if(data){
        	   			if(type=="hotel"){ 
        		   			$.each(data.hotelList,function(i,n){
        		   			     $("#hotelUuid").append($("<option/>").text(n.nameCn).attr("value",n.uuid));
        		   			});
        		   			$("#islandUuid").attr('title',$("#islandUuid").find("option:selected").text());
        	   			}else if(type=="roomtype"){
        	   				
        	   				var occuStr ="";
        	   				$("#jbox_tq table.table_product_info > tbody > tr").eq(2).find("select:eq(0)").append($("<option/>").text('请选择').attr("value",''));
        	   				$.each(data.roomtype,function(i,n){
        	   					
        	   					if(n.occupancyRate!=null && n.occupancyRate!=""){
        	   						
        	   						occuStr = "("+n.occupancyRate+")";
        	   					}else{
        	   						occuStr="";
        	   					}
//         	   					debugger
        	   					$("#jbox_tq table.table_product_info > tbody > tr").eq(2).find("select:eq(0)").append($("<option/>").text(n.roomName+occuStr).attr("value",n.uuid));
        	   				
        	   				});
        	   			}
//         	   			else if(type=="foodtype"){
        	   				
//         	   				$.each(data.roomMeals,function(i,n){
        	   					
//         	   					$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("td:eq(1) select:eq(0)").append($("<option/>").text(n.mealName).attr("value",n.uuid));
        	   			
//         	   				});
        	   				
//         	   			}
        	   			else if(type=="islandway"){
	        	   			$.each(data.listIslandWay,function(i,n){
	        	   				var islandwayTd = $("#jbox_tq table.table_product_info > tbody > tr").eq(4).find("td:eq(1)");
                     			$(islandwayTd).append($("<input/>").attr("class","redio_martop_4").attr("type","checkbox").attr("data-text",n.label).val(n.uuid));
                     			$(islandwayTd).append(n.label);
                     			$(islandwayTd).append($("<span/>").attr("class","mr25"));
                     			
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
//        	   						html+=" <td class='tc font_c66 new_hotel_p_table2_tdf'>"+new Date(parseInt(n.inDate)*1000).toLocaleString()+"</td>";
//         	   						html+=" <td class='tc font_c66 new_hotel_p_table2_tdf'>"+n.inDate+"</td>";
//         	   						html+=" <td class='tc font_c66 new_hotel_p_table2_tdf'>"+new Date(parseInt(n.inDate) * 1000).toLocaleString().replace(/:\d{1,2}$/,' ')+"</td>";
        	   						html+=" <td class='tc font_c66 new_hotel_p_table2_tdf'>"+formatToDate(n.inDate)+"</td>";
       	   						var roomNameNight="";
       	   						$.each(n.rooms,function(i,n){
       	   							if(n.roomName==null){
//        	   							roomNameNight+=""+"*"+n.night+"晚</t>";
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
//         	   			else if(type=="travelerTypeRelations"){
//         	   				var html="";
//         	   				var travelerType = $("#jbox_tq table.table_product_info > tbody > tr").eq(6).find("td:eq(1)");
// 	        	   			$.each(data.travelerTypeRelations,function(i,n){
// 						   		html+="<div class='hotel_price_same_industry' name='hotelPrice' data-value='"+n.travelerTypeUuid+"'>";
// 						   		html+="<span>"+n.travelerTypeName+"</span>";
// 						   		html+="<select name='peoplePrice' class='w50_30 currency'>";
// 					   			$.each(data.currencyList,function(i,m){
// 					   				html+="<option value='"+m.id+"'>"+m.currencyMark+"</option>";
// 					   			});
// 						   		html+="</select>";
// 						   		html+="<input type='text' data-type='float' value='' name='orderPersonName2' class='inputTxt w50_30 adultPrice mr25' />";
// 						   		html+="</div>";
// 	        	   			});
// 						   	$(travelerType).append(html);
//         	   			}
        	   		}
        	   	}
        	});
        }
        
 </script>
 <!--级联查询结束 -->
</head>


<body>
                    <!--右侧内容部分开始-->
                    <div class="mod_nav">产品 &gt; 酒店产品 &gt; 酒店产品发布</div>
                    <div class="produceDiv">
                    <form:form id="saveActivityHotelForm" modelAttribute="activityHotelInput" action=""  method="post" class="form-horizontal" novalidate="">
                         <!--产品信息开始-->
                        <div class="mod_information" id="ofAnchor1">
                            <div class="mod_information mar_top0">
                                <div class="ydbz_tit island_productor_upload001"> <span class="ydExpand" data-target="#baseInfo"></span>基本信息</div>
                                <div style="margin-top:8px; min-width:1600px !important;" id="baseInfo">
                                    <div class="activitylist_bodyer_right_team_co2 wpr20 pr" style="height:40px;">
                                        <div class="activitylist_team_co3_text"><span class="xing">*</span>产品名称：</div>
                                        <input type="text" value="" class="inputTxt inputTxtlong" name="activityName" id="orderNum" flag="istips" maxlength="128"/>
                                        <span class="ipt-tips">命名规则 行程时间-海岛-交通方式</span>
                                    </div>
                                    <div class="kong"></div>
                                    <!-- 根据需求隐藏控房单号 @author zhangchao 2016/01/07 -->
                                   <!--  <div class="activitylist_bodyer_right_team_co1" style="display:none;">
                                        <div class="activitylist_team_co3_text" ><span class="xing">*</span>控房单号：</div>
                                        <input type="text" value="" name="activitySerNum" class="inputTxt" id="orderPersonName" maxlength="250"/>
                                    </div> -->
                                    <div class="activitylist_bodyer_right_team_co2  wpr20">
                                         <!--  <label><span class="xing">*</span>国家：</label> -->
                                          <div class="activitylist_team_co3_text"><span class="xing">*</span>国家：</div>
									      <trekiz:suggest name="country" style="width:150px;"
												defaultValue="${countryUuid}" callback="getAjaxSelect('island',$('#country'))"  displayValue="${countryName}"
												ajaxUrl="${ctx }/geography/getAllConListAjax" />
                                    </div>
                                    <div class="activitylist_bodyer_right_team_co1">
                                         <div class="activitylist_team_co3_text"><span class="xing">*</span>岛屿：</div>
                                         <select name="islandUuid" id="islandUuid" onchange="getAjaxSelect('hotel',this);">
										 </select>
                                    </div>
                                    <div class="kong"></div>
                                    <div class="activitylist_bodyer_right_team_co1">
                                        <div class="activitylist_team_co3_text"><span class="xing">*</span>酒店名称：</div>
                                        <select  class="selectinput" id="hotelUuid" name="hotelUuid" >
                                           <option value="" selected="selected"></option>
                                        </select>
                                        <span class="padr10"></span><a id="showQuote" onclick="showQuote()"  href="javascript:void(0);">查看价单</a>
                                    </div>
                                    <div class="activitylist_bodyer_right_team_co1">
                                        <div class="activitylist_team_co3_text">酒店星级：</div>
                                        <span class="y_xing" style="line-height:28px;" id="hotelrank"></span>
                                    </div>
                                    <div class="activitylist_bodyer_right_team_co1 currencySel" >
                                        <div class="activitylist_team_co3_text" >币种选择：</div>
                                         <!-- <div class="activitylist_team_co3_text"><span class="xing">*</span>币种选择：</div> -->
					                    <select id="currencyId" name="currencyId">
										<c:forEach items="${currencyList}" var="item">
											    <option value="${item.id}" >
										    		${item.currencyName}
										    	</option>
										</c:forEach>
									    </select>
                                    </div>
                                    <!--查询结果筛选条件排序开始-->
                                    <div class="filterbox add_new_tq">
                                        <div class="filter_btn"> <a class="btn btn-primary" href="#" id="addGroup">新增团期</a> </div>
                                    </div>
                                    <!--查询结果筛选条件排序结束-->
                                    <table  id="contentTable" class="table activitylist_bodyer_table_new sea_rua_table">
                                        <thead>
                                            <tr>
                                                <th width="5%">序号</th>
                                                <th width="5%">团号/日期 <input type="hidden" id="rgroup"/></th>
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
                                                <td class="tc">
                                                    <a href="#" target="_blank" name="groupcodeText"></a> <br />
                                                    <span></span>
                                                </td>
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
                                                <td class="tl" style="display: none">
                                                </td>
                                                <td class="p0">
                                                    <dl class="handle">
                                                        <dt> <img title="操作" src="${ctxStatic}/images/handle_cz.png" /> </dt>
                                                        <dd class="">
                                                            <p>
                                                                <span></span>
                                                                <a class="copyLink" href="javascript:void(0)">复制并新增</a>
                                                                <a class="updateLink" href="javascript:void(0)">修改</a>
<!--                                                                 <a href="javascript:void(0)">详情</a> -->
                                                                <a class="delLink" href="javascript:void(0)">删除</a>
                                                            </p>
                                                        </dd>
                                                    </dl>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <!--产品信息结束-->
                        <!--上传资料开始-->
                        <div style="clear:none;" class="kong"></div>
                        <div id="thirdStepDiv">
                            <!-- 上传文件 -->
                            <div class="ydbz_tit island_productor_upload001"> <span class="ydExpand" data-target="#up_fiels"></span>上传资料</div>
                            <div class="mod_information_3" id="up_fiels">
				               <div class="batch"  style="margin-top:10px;">
					                <p class="maintain_pfull new_kfang">
						                <label class="batch-label company_logo_pos">产品行程介绍：</label>
						                <input class="mod_infoinformation3_file" type="button" onclick="uploadFiles('${ctx}',null,this)" value="上传文件" name="DocIdPro">
					                </p>
				                    <ol class="batch-ol"></ol>
				               </div>
				               <div class="mod_information_d7"></div>
				               <div class="batch"  style="margin-top:10px;">
					                <p class="maintain_pfull new_kfang">
						                <label class="batch-label company_logo_pos">自费补充协议：</label>
						                <input class="mod_infoinformation3_file" type="button" onclick="uploadFiles('${ctx}',null,this)" value="上传文件" name="DocIdCost">
					                </p>
				                    <ol class="batch-ol"></ol>
				               </div>
				               <div class="mod_information_d7"></div>
				               <div class="batch"  style="margin-top:10px;">
					                <p class="maintain_pfull new_kfang">
						                <label class="batch-label company_logo_pos">其他补充协议：</label>
						                <input class="mod_infoinformation3_file" type="button" onclick="uploadFiles('${ctx}',null,this)" value="上传文件" name="DocIdOther">
					                </p>
				                    <ol class="batch-ol"></ol>
				               </div>
				               <div class="mod_information_d7"></div>
                            </div>
                        <div class="ydbz_tit island_productor_upload001 maring_top10"> <span class="ydExpand" data-target="#other_info"></span>其他信息</div>
                        <div class="other_info" id="other_info">
                            <textarea class="add_new_seatu_qt" onkeyup="getAcitivityNameLength1(200)" id="acitivityName"  name="memo" ></textarea>
                            <br />
                            <span style="color:#b2b2b2" class="acitivityNameSizeSpan">还可输入<span class="acitivityNameSize">200</span>个字</span>
                        </div>
                        <div class="ydbz_tit island_productor_upload001 maring_top10"> <span class="ydExpand" data-target="#productor_sel"></span>产品分享</div>
                       <!--  <div class="product_share" id="productor_sel">
                            <div class="coverarea">
                                <div class="role">
                                    <div class="role-left">
                                        <div class="role-leftTop">
                                            <input type="text" id="key" name="key" />
                                        </div>
                                        <div class="role-leftCen">
                                            <ul id="menuTree" class="ztree" style="margin-top:3px;"></ul>
                                            <input id="menuIds" name="menuIds" type="hidden" value="" />
                                        </div>
                                    </div>
                                    <div class="role-right">
                                        <div class="role-rightTop">
                                            <span> 已添加 <em>0</em>个用户 </span>
                                            <p> <i></i>清空 </p>
                                        </div>
                                        <div class="role-rightCen">
                                            <ul id="addArea"></ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div> -->
                        <%@ include file="/WEB-INF/views/include/userShareTree.jsp"%>
                        <div class="release_next_add">
                             <input type="button" value="取消" onclick="window.close();" class="btn btn-primary gray" /> 
                            <input type="button" value="保存"  class="btn btn-primary"  dataStatus="3" onclick="formValidate(this)"/>
                            <input type="button" value="提交"  class="btn btn-primary" dataStatus="1"  onclick="formValidate(this)"/>
                        </div>
                    </div>
                    <!--第三步上传资料结束-->
                </div>
                <!--右侧内容部分结束-->
   </form:form>
    <!--新增团期信息项弹出层开始-->
    <div id="jbox_hotel_add_product_fab" style="display:none;">
        <div class="add_product_info new_hotel_p_table">
            <table class="table_product_info " style="width:900px !important; " id="productInfoTable">
                <tr>
                    <td width="133" class="tr" style="width:121px !important;"><span class="xing">*</span>团号</td>
                    <td colspan="3"><input type="text" class="inputTxt w106 spread"  id="igroupcode"  onblur="checkedMassage(this)"/> <span class="new_flight_control" id="groupTip"></span></td>
                </tr>
                <tr>
                    <td class="tr"><span class="xing">*</span>日期</td>
                    <td colspan="3">
                        <input type="text"  id="tdate"  onclick="WdatePicker()" class="dateinput w106 required " readonly="readonly" />
<!--                         <a href="产品-酒店产品-优惠信息.html" target="_blank">[价单优惠信息]</a> <span class="padr10"></span> -->
                         <a  name="price_Info" href="产品-酒店产品-价格价单信息.html" target="_blank">[价单价格信息]</a> 
                    </td>
                </tr>
                <tr class="houseTypeTr">
                    <td class="tr" id="dash_line_blue">房型(容住率)*晚数</td>
                    <td colspan="3" id="dash_line_blue">
                        <p class="houseType">
                         	    <span>
                                	<select name="roomtype" id="roomtype">
<!-- 									<option value="">请选择</option> -->
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
                                <select  class="w50_30 mr3 currency" name="peoplePrice">
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
                <tr>
                    <td class="tr">上岛方式</td>
                    <td colspan="3">
							
                    </td>
                </tr>
                <tr>
                    <td class="tr">参考航班</td>
                    <td colspan="3">
                        <input type="text" class="inputTxt w106 spread" />
                        <span class="new_flight_control">注：多个航班号请用分号隔开</span></a>
                    </td>
                </tr>
                <tr>
                    <td class="tr">同行价格/人</td>
                    <td colspan="3">
<!--                     	<c:forEach items="${travelerTypes }" var="traveler"> -->
<!-- 	                        <div class="hotel_price_same_industry" id="hotelPrice" data-value="${traveler.uuid }"> -->
<!-- 	                            <span>${traveler.name }</span> -->
<!-- 	                            <select name="peoplePrice" class="w50_30 currency"> -->
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
                    <td><input maxlength="4" type="text" data-type="number" class="inputTxt w106 spread" /></td>
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
                            <select name="peoplePrice" class="w50_30 currency">
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
                        <select name="peoplePrice" class="w50_30">
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
                    <td class="tr valign_top" style="display:none">json</td>
                    <td colspan="3" style="display: none">
                    	<input type="hidden" name="jsoninput" value=""/>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <!--新增团期信息项弹出层结束-->
    <script type="text/javascript" src="${ctxStatic }/js/hotelProduct.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            // 国家--岛屿--酒店联动
            // 数据结构
           /*  var country = {
                "中国": {
                    "海南": ["希尔顿", "七天", "如家"],
                    "舟山": ["华住", "速八", "万豪"]
                },
                "日本": {
                    "九州": ["君澜", "七天", "如家"],
                    "北海道": ["希尔顿", "速八", "锦江"]
                }
            }
            $("#country").on('change', function () {
                var $island = $("#island").empty();
                var island = country[$("#country").val()] || {};
                for (var key in island) {
                    $island.append("<option>" + key + "</option>");
                }
                $("#island").change();
            });
            $("#island").on('change', function () {
                var $hotel = $("#hotel").empty();
                var island = country[$("#country").val()] || {};
                var hotel = island[$("#island").val()] || [];
                for (var i = 0, l = hotel.length; i < l; i++) {
                    $hotel.append("<option>" + hotel[i] + "</option>");
                }
            }); */
            
            $("#hotel").on('change', function () {
                // 获取酒店的房型、餐型和升级餐型
                // 为houseTypes 赋值
//                  houseTypes = {};
            });

            $("#addGroup").on('click', function () {
                var options = {
                    isAdd: true,
                    callback: writeRow
                }
                addGroup_box(options);
                var countryVal = $('#country').val();
                var hotelVal = $('#hotelUuid').val();
                var islandVal = $('#islandUuid').val();
                var aurl = "${ctx }/hotelPl/list?country="+countryVal+"&hotelUuid="+hotelVal+"&islandUuid="+islandVal;
                $("[name=price_Info]").attr("href",aurl);
                //给同行人价格赋值 
         		$("[name=peoplePrice]").val($("#currencyId").val());
            });

            $("#contentTable").on('click', 'a.copyLink', function () {
                var id = $("#contentTable tbody tr").has(this).prop('id');
                var options = {
                    isAdd: true,
                    callback: writeRow,
                    id: id,
                    data: readRow(id)
                }
                addGroup_box(options);
            }).on('click', 'a.updateLink', function () {
                var id = $("#contentTable tbody tr").has(this).prop('id');
                var options = {
                    isAdd: false,
                    callback: writeRow,
                    id: id,
                    data: readRow(id),
                    updateg:"updateg"
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

                var data = {};

                var $tds = $row.find("td");
                var cIndex = 0;
                var $td;
                // 序号
                cIndex++;
                // 团号
                data.NO = $tds.eq(cIndex).find('a').text();
                $("#rgroup").val(data.NO);
                // 日期
                data.date = $tds.eq(cIndex).find('span').text();
                cIndex++;
                // 房型 * 晚数
                data.houseTypes = [];
                var $tr = $row;
                for (var i = 0; $tr.is("tr[data-tag='" + id + "']") ; i++) {
                    $td = i ? $tr.find("td:first") : $tds.eq(cIndex);
                    var obj = {
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
                            item.upMealTypes.push({
                                mealType: { value: $(this).find("span:eq(0)").attr("data-value") },
                                currency: { value: $(this).find("span:eq(1)").attr("data-value") },
                                price: $(this).find("span:eq(2)").text()
                            });
                        });
                        obj.baseMealTypes.push(item);
                    }
                    data.houseTypes.push(obj);
                    $tr = $tr.next();
                }
                cIndex += 3;
                // 上岛方式
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
                    data.tradePrices.push({
                        currency: { value: $this.find("span:eq(1)").attr("data-value") },
                        price: $this.find("span:eq(2)").text(),
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
                data.priceDiff = {
                    currency: { value: $tds.eq(cIndex).find("span:eq(0)").attr("data-value") },
                    price: $tds.eq(cIndex).find("span:eq(1)").text(),
                    unit: { value: $tds.eq(cIndex).find("span:eq(2)").attr("data-value") }
                };
                cIndex++;
                // 需交定金
                data.deposit = {
                    currency: { value: $tds.eq(cIndex).find('span:eq(0)').attr("data-value") },
                    price: $tds.eq(cIndex).find('span:eq(1)').text()
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
//                     html.push('<p>', this.trafficWaystext, '</p>');
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
                    html.push('<p><span>', this.type, '</span>：<span data-value="', this.currency.value, '">', this.currency.text, '</span><span>', this.price, '</span></p>');
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
//             $("#saveActivityHotelForm").validate({
//     			rules:{
    				
//     			},
//     			submitHandler: function(form){
//     				var url="${ctx}/activityHotel/saveActivityHotel";
//     				$("#saveActivityHotelForm").attr("disabled", "disabled");
//     				$.post(url,$("#saveActivityHotelForm").serialize(),function(data){
//     					if(data.message=="1"){
//     						$("#searchForm",window.opener.document).submit();
//     						$.jBox.tip("添加成功!");
//     						setTimeout(function(){window.close();},500);
//     					}else if(data.message=="2"){
//     						$("#searchForm",window.opener.document).submit();
//     						$.jBox.tip("修改成功!");
//     						setTimeout(function(){window.close();},500);
//     					}else if(data.message=="3"){
//     						$.jBox.tip(data.error,'warning');
//     						$("#updateDraftSubmit").attr("disabled", false);
//     					}else{
//     						$.jBox.tip('系统异常，请重新操作!','warning');
//     						$("#updateDraftSubmit").attr("disabled", false);
//     					}
//     				});
//     			}
//     		});
            
        });
        
    </script>
</body>
</html>