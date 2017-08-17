<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>海岛游产品发布 </title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
<link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
 <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" />
 <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
 <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.css" />
 <!--树形插件的样式-->
 <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.mCustomScrollbar.css" />
 <!--滚动条插件样式-->
 <link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />


	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/default.validator.js"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>

 <!--产品模块的脚本-->
 <script type="text/javascript" src="${ctxStatic}/js/tmp.products.js"></script>

 <script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.js"></script>
 <!--树形插件的脚本-->
 <script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.excheck-3.5.js"></script>
 <!--树形插件的脚本-->
 <script type="text/javascript" src="${ctxStatic}/js/jquery.mousewheel.min.js"></script>
 <!--滚动条插件脚本-->
 <script type="text/javascript" src="${ctxStatic}/js/jquery.mCustomScrollbar.js"></script>






<%--

<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
 <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
 <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
 <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
 <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
 <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
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

 --%>
<script type="text/javascript">
	function formSubmit(obj){
		var status = $(obj).attr("data-value");
		var url="${ctx}/activityIsland/saveActivityIsland?status="+status;
		$(obj).attr("disabled", "disabled");
		$.post(
			url,
			$("#saveActivityIslandForm").serialize(),
			function(data){
				if(data.message=="1"){
					$.jBox.tip("添加成功!");
				}else if(data.message=="2"){
					$.jBox.tip("修改成功!");
				}else if(data.message=="3"){
					$.jBox.tip(data.error,'warning');
					$(obj).attr("disabled", false);
				}else{
					$.jBox.tip('系统异常，请重新操作!','warning');
					$(obj).attr("disabled", false);
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
       	//初始化国家标签
         initSuggest({});
         //其它信息字数限制
         getAcitivityNameLength1(200);
         //操作浮框
         operateHandler();
         
       
     });
     //删除已上传的文件
  /*    function deleteFile(thisDom, fileID) {
         $(thisDom).parent("li").remove();
     } */
     
     function shangchuan() {
         var $temp = $("div.up_files_lists:first");
         $temp.parent().append($temp.clone().show());
         return false;
     }
 </script>
  <!--上传文件script-->
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
	</script>
    <!--上传文件script-->
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
 <script> 
 	//级联查询
        function getAjaxSelect(type,obj,tag){
        	$.ajax({
        		type: "POST",
        	   	url: "${ctx}/hotelControl/ajaxCheck",
        	   	data: {
        				"type":type,
        				"uuid":$(obj).val()
        			  },
        		dataType: "json",
        		async:false,
        	   	success: function(data){
        	   		if(type == "islandway"){
        	   			$("#jbox_tq table.table_product_info > tbody > tr").eq(4).find("td:eq(1)").html('');
        	   			if(tag!='notHotel'){
	        	   			$("#hotelUuid").empty();
	        		   		$("#hotelUuid").append("<option value=''>不限</option>");
        	   			}
        	   		} else if(type == "roomtype"){
        	   			$("select[name=roomType]").empty();
        		   		$("select[name=roomType]").append("<option value=''>不限</option>");
        	   		} else if(type=="hotelrank"){
        	   			$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("td:eq(3) select:eq(0)").empty();
        	   			$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("td:eq(3) select:eq(0)").append("<option value=''>不限</option>");
        	   		} else if(type=="foodtype"){
        	   			$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("td:eq(1) select:eq(0)").empty();
        	   			$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("td:eq(1) select:eq(0)").append("<option value=''>不限</option>");
        	   		}else {
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
        	   					 $("#jbox_tq table.table_product_info > tbody > tr").eq(2).find("select:eq(0)").append($("<option/>").text(n.roomName).attr("value",n.uuid));
        	   				});
        	   			}else if(type=="foodtype"){
        	   				$.each(data.roomMeals,function(i,n){
        	   					//$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("select:eq(0)").append($("<option/>").text(n.mealName).attr("value",n.uuid));
        	   					$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("td:eq(1) select:eq(0)").append($("<option/>").text(n.mealName).attr("value",n.uuid));
        	   					//$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("td:eq(3) select:eq(0)").append($("<option/>").text(n.mealName).attr("value",n.uuid));
        	   				});
        	   			}else if(type=="islandway"){
        	   				var islandwayTD = $("#jbox_tq table.table_product_info .islandwayTD");
        	   				islandwayTD.html('');
        	   				$.each(data.listIslandWay,function(i,n){
                     			islandwayTD.append($("<input/>").attr("class","redio_martop_4").attr("type","checkbox").attr("data-text",n.label).attr("data-value",n.uuid));
                     			islandwayTD.append(n.label);
                     			islandwayTD.append($("<span/>").attr("class","mr25"));
        	   				});
        	   				if(tag!='notHotel'){
		        	   			$.each(data.hotelList,function(i,n){
	        	   					 $("#hotelUuid").append($("<option/>").text(n.nameCn).attr("value",n.uuid));
	        	   				});
	        	   			}
        	   			}else if(type=="island"){
        	   				$.each(data.islandList,function(i,n){
        	   				
        	   					 $("#islandUuid").append($("<option/>").text(n.islandName).attr("value",n.uuid));
        	   				});
        	   			}else if(type=="hotelrank"){
        	   				var rankCount = data.hotelrank;
        	   				var ranks = '';
        	   				if(!isNaN(rankCount)){
								for(var i=0;i<rankCount;i++){
        	   					ranks+="★";
        	   				}        	   				
        	   				}
        	   				$(".y_xing").text(ranks);
        	   				
        	   				//升餐
        	   				$.each(data.hotelMeals,function(i,n){
        	   					$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("td:eq(3) select:eq(0)").append($("<option/>").text(n.mealName).attr("value",n.uuid));
        	   				});
        	   			}
        	   		}
        	   	}
        	});
        }
        function checkedMassage(obj){
//         	if(obj.value==null || obj.value==''){
//         		$.jBox.tip("团号不能为空");
//         		return false;
//         	}
				/* alert($("#agroup").val()); *//*  */
				//alert(2);
				$("#contentTable_new").find("tr:visible").each(function(){
					alert($(this).find("a[name=groupCode]").text());
				});
				if($("#agroup").val()==$(obj).val()){
					$.jBox.tip("该团号已存在");
				}
        	$.ajax({
        		type:"post",
        		url:"${ctx}/activityIsland/checkedGroup",
        		data:{
        			"groupCode":obj.value
        		},
        		success:function(data){
        			if(data.message=="true"){
        				$.jBox.tip("该团号已存在");
        				obj.value="";
        				return false;
        			}
        		}
        	});
        	
        }
				
			
		
 </script>
</head>
<body>
	<page:applyDecorator name="activity_island" >
     <page:param name="showType">${showType}</page:param>
     <page:param name="showType">${activityStatus}</page:param>
  </page:applyDecorator>
  <br/>
<!--右侧内容部分开始-->
<div class="mod_nav">产品 &gt; 海岛游产品 &gt; 海岛游产品发布</div>
<div class="produceDiv">
<form id="saveActivityIslandForm" modelAttribute="ActivityIslandInput" action=""  method="post" class="form-horizontal" novalidate="">
    <!--产品信息开始-->
    <div class="mod_information" id="ofAnchor1">
        <div class="mod_information mar_top0">
            <div class="ydbz_tit island_productor_upload001"> <span class="ydExpand" data-target="#baseInfo"></span>基本信息</div>
            <div style="margin-top:8px; min-width:1600px !important;" id="baseInfo">
                <div class="activitylist_bodyer_right_team_co2 wpr20 pr" style="height:40px;">
                    <div class="activitylist_team_co3_text">产品名称：</div>
                    <input type="text" value="" class="inputTxt inputTxtlong" name="activityName" id="orderNum" flag="istips" />
                    <span class="ipt-tips">命名规则 行程时间-海岛-交通方式</span>
                </div>
                <div class="kong"></div>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">产品编号：</div>
                    <input type="text" value="" name="activitySerNum" class="inputTxt" id="orderPersonName" />
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">国家：</div>
                     <trekiz:suggest name="country" style="width:150px;" defaultValue="" displayValue=""  callback="getAjaxSelect('island',$('#country'))"  ajaxUrl="${ctx}/geography/getAllConListAjax" />
                    <!-- <select id="country">
                        <option>中国</option>
                        <option>日本</option>
                        <option>美国</option>
                    </select> -->
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <label>币种选择：</label>
                    <select id="currencyId" name="currencyId">
						<c:forEach items="${currencyList}" var="item">
							    <option value="${item.id}" >
						    		${item.currencyName}
						    	</option>
						</c:forEach>
					</select>
                </div>
                <div class="kong"></div>
                
                
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">岛屿：</div>
                   <select name="islandUuid" id="islandUuid" onchange="getAjaxSelect('islandway',this);">
							<option value="">不限</option>
					</select>
                  <!--   <select id="island">
                        <option>太阳岛 </option>
                        <option>西西岛 </option>
                    </select> -->
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">酒店名称：</div>
                    <select name="hotelUuid" id="hotelUuid" onchange="getAjaxSelect('hotelrank',this);">
						<option value="">不限</option>
					</select>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">酒店星级：</div>
                    <span class="y_xing" style="line-height:28px;"></span>
                </div>
                <!--查询结果筛选条件排序开始-->
                <div class="filterbox add_new_tq">
                    <div class="filter_btn"> <a class="btn btn-primary" href="#" id="addGroup">新增团期</a> </div>
                </div>
                <!--查询结果筛选条件排序结束-->
                <table id="contentTable_new" class="table activitylist_bodyer_table_new sea_rua_table">
                    <thead>
                        <tr>
                            <th width="8%">团号/日期</th>
                            <th width="5%">房型 * 晚数</th>
                            <th width="4%">基础餐型</th>
                            <th width="7%">升级餐型&amp;升餐价格</th>
                            <th width="4%">上岛方式</th>
                            <th width="8%">
                                航班<br />
                                起飞到达时间
                            </th>
                            <th width="13%">舱位等级&amp;价格&amp;余位</th>
                            <th width="5%">
                                <p>余位/票数总计</p>
                            </th>
                            <th width="4%">
                                <p>预收/预报名</p>
                            </th>
                            <th width="8%">单房差</th>
                            <th width="8%">需交订金</th>
                            <th width="8%">备注</th>
                            <th width="4%">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr style="display:none;">
                            <td class="tc" id="">
                                <a href="#" target="_blank" name="groupCode"></a> <br />
                                <span></span>
                            </td>
                            <td class="tc"></td>
                            <td class="tc"></td>
                            <td class="tc"></td>
                            <td class="tc"></td>
                            <td class="tc"></td>
                            <td class="tc"></td>
                            <td class="tc"></td>
                            <td class="tc"><span class="over_handle_cursor" title="50"></span>/<span>0</span></td>
                            <td class="tr"></td>
                            <td class="tr tdgreen"></td>
                            <td class="tl"></td>
                            <td class="p0">
                                <dl class="handle">
                                    <dt> <img title="操作" src="${ctxStatic }/images/handle_cz.png" /> </dt>
                                    <dd class="">
                                        <p>
                                            <span></span>
                                            <a class="copyLink" href="javascript:void(0)">复制</a>
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
               <div class="batch" style="margin-top:10px;">
                   <label class="batch-label">上传签证资料：</label>
                   <img name=""  class="up_load_visa_info_btn" src="${ctxStatic }/images/add_11.jpg" onclick="addRecord()"/>
                   <div class="up_files_lists" style="display:none;">
                           <div class="up_load_visa_info">
                           <div class="activitylist_bodyer_right_team_co1">
                               <div class="activitylist_team_co3_text">国家：</div>
                               <select id="country">
                                   <option>马尔代夫</option>
                                   <option>日本</option>
                                   <option>美国</option>
                               </select>
                           </div>
                           <div class="activitylist_bodyer_right_team_co1">
                               <div class="activitylist_team_co3_text">签证类型：</div>
                               <select id="island">
                                   <option>请选择class="batch"</option>
                                   <option>商务</option>
                               </select>
                           </div>
                           <span class="padr10"></span>
                            <p class="maintain_pfull new_kfang">
                               <input type="button" name="VisaFile" value="上传文件" class="mod_infoinformation3_file maring_bottom10" onClick="uploadFiles('${ctx}',null,this)"/>
                                 <input type="button" class="mod_infoinformation3_file maring_bottom10 up_load_visa_info_btn_del" value="删除" />
                            </p>
                           	   <ol class="batch-ol"></ol> 
                           <div class="mod_information_d7"></div>
                       </div>
                       <ul class="up_files_outer">
                       </ul>
                   </div>

                   <div class="up_files_lists">
                       <div class="up_load_visa_info">
                           <div class="activitylist_bodyer_right_team_co1">
                               <div class="activitylist_team_co3_text">国家：</div>
                               <select id="country">
                                   <option>马尔代夫</option>
                                   <option>日本</option>
                                   <option>美国</option>
                               </select>
                           </div>
                           <div class="activitylist_bodyer_right_team_co1">
                               <div class="activitylist_team_co3_text">签证类型：</div>
                               <select id="island">
                                   <option>请选择class="batch"</option>
                                   <option>商务</option>
                               </select>
                           </div>
                           <span class="padr10"></span>
                            <p class="maintain_pfull new_kfang">
                               <input type="button" name="VisaFile" value="上传文件" class="mod_infoinformation3_file maring_bottom10" onClick="uploadFiles('${ctx}',null,this)"/>
                                 <input type="button" class="mod_infoinformation3_file maring_bottom10 up_load_visa_info_btn_del" value="删除" />
                            </p>
                           	   <ol class="batch-ol"></ol> 
                           <div class="mod_information_d7"></div>
                       </div>
                        <ul class="up_files_outer">
                       </ul>
                   </div>
               </div>
                    <div class="mod_information_d7"></div>
           </div>
       </div>
    <!--上传资料开始-->
    <div class="ydbz_tit island_productor_upload001 maring_top10"> <span class="ydExpand" data-target="#other_info"></span>其他信息</div>
    <div class="other_info" id="other_info">
        <textarea class="add_new_seatu_qt" onkeyup="getAcitivityNameLength1(200)" id="acitivityName" name="memo"></textarea>
        <br />
        <span style="color:#b2b2b2" class="acitivityNameSizeSpan">还可输入<span class="acitivityNameSize">200</span>个字</span>
    </div>
    <div class="ydbz_tit island_productor_upload001 maring_top10"> <span class="ydExpand" data-target="#productor_sel"></span>产品分享</div>
    <%@ include file="/WEB-INF/views/include/userShareTree.jsp"%>
    <div class="release_next_add">
        <input type="button" value="取消" onclick="javascript:window.location.href='/trekiz_wholesaler/a/activity/manager/list/2'"
               class="btn btn-primary gray" />
       <input id="saveActivityIsland" type="button" value="保存" class="btn btn-primary" data-value="3" onclick="formSubmit(this)"/>
        <input type="button" value="提交" class="btn btn-primary" data-value="1" onclick="formSubmit(this)"/>
    </div>
</div>
   </form>
 <!--新增团期信息项弹出层开始-->
 <div id="jbox_haidaoyou_fab">
     <div class="add_product_info new_hotel_p_table">
         <table class="table_product_info " style="width:900px !important; ">
             <tr>
                 <td class="tr" style="width:121px !important;">团号</td>
                 <td width="710" colspan="3"><input type="text" class="inputTxt w106 spread" onblur="checkedMassage(this)" id="igroupcode"/></td>
             </tr>
             <tr>
                 <td class="tr">日期</td>
                 <td colspan="3"><input type="text" onclick="WdatePicker()" class="dateinput w106 required " readonly="readonly" /></td>
             </tr>
             <tr>
                 <td class="tr">房型*晚数</td>
                 <td colspan="3">
                     <p class="houseType">
                         <span>
                             <!-- <select class="w80">
                                 <option>水上屋</option>
                                 <option>沙滩屋</option>
                             </select> -->
                             <select name="roomtype" id="roomtype" onchange="getAjaxSelect('foodtype',this);">
								<option value="">不限</option>
							</select>
                             <span class="w50_30">*</span>
                             <input type="text" data-type="number" data-min="1" class="inputTxt w50_30" />
                             <span class="w50_30">晚</span> <a class="ydbz_x  addHouseType">新增</a>
                         </span>
                     </p>
                 </td>
             </tr>
             <tr class="houseTypeTr mealType">
                 <td class="tr">基础餐型</td>
                 <td colspan="3">
                     <select class="w80">
                         <option value="">不限</option>
                     </select>
                     <a class="ydbz_x addMealType">新增</a> <span class="padr10"></span> <span>
                         <input class="redio_martop_4" type="checkbox" />
                         升级餐型
                     </span>
                 </td>
                 <td class="tr new_hotel_p_table2_tdblue" style="display:none;"><span>升级餐型</span></td>
                 <td style="display:none;">
                     <p class="upMealType">
                         <span>
                             <select name="select10" class="w80 mr3">
                                 <option value="">不限</option>
                             </select>
                             <select name="select10" class="w50_30 mr3 currency">
                                <c:forEach items="${currencyList}" var="item">
								    <option value="${item.id}" >
							    		${item.currencyMark}
							    	</option>
								</c:forEach>
                             </select>
                             <input type="text" data-type="float"  class="inputTxt w50_30"  />
                             <a class="ydbz_x addUpMealType">新增</a>
                         </span>
                     </p>
                 </td>
             </tr>
             <tr>
                 <td class="tr">上岛方式</td>
                 <td width="350" class="islandwayTD">
                    <input class="redio_martop_4" type="checkbox" data-text="水飞" />
                     水飞 <span class="mr25"></span>
                     <input class="redio_martop_4" type="checkbox" data-text="内飞" />
                     内飞 <span class="mr25"></span>
                     <input class="redio_martop_4" type="checkbox" data-text="快艇" />
                     快艇
            			<!-- <select class="w180_30 ascendWay"  name="islandway" id="islandway" onchange="getAjaxSelect('',this);">
											<option selected="selected">不限</option>
							</select>   -->
                 </td>
                 <td width="130" class="tr new_hotel_p_table2_tdblue">单房差</td>
                 <td width="350">
                     <span class="add_jbox_repeat_thj">
                         <select class="w50_30 currency" >
                         	<c:forEach items="${currencyList}" var="item">
							    <option value="${item.id}" >
						    		${item.currencyMark}
						    	</option>
							</c:forEach>
                         </select>
                         <input type="text" data-type="float"  class="inputTxt w50_30 babyPrice mr25" />
                         <select class="w50_30 currency">
                             <option value="1">/人</option>
                             <option value="2">/间</option>
                             <option value="3">/晚</option>
                         </select>
                     </span>
                 </td>
             </tr>
             <tr>
                 <td class="tr">航空公司</td>
                 <td>
                     <select class="w125_sel_pop_addnewtimt selAirline">
                        <option value=''>请选择</option>
                        <c:forEach items="${airlines_list }" var="airlineInfo">
                         	<option value="${airlineInfo.airlineCode }">${airlineInfo.airlineName }</option>
                         </c:forEach>
                     </select>
                 </td>
                 <td class="tr new_hotel_p_table2_tdblue">航班号</td>
                 <td>
                     <select class="w125_sel_pop_addnewtimt fltNo"></select>
                 </td>
             </tr>
             <tr>
                 <td class="tr new_hotel_p_table2_tdblue">起飞时间</td>
                 <td class="tl">
                     <input type="text" onclick="WdatePicker({ dateFmt: 'HH:mm' })" class="dateinput w106 required startTime" readonly="readonly" />
                 </td>
                 <td class="tr new_hotel_p_table2_tdblue">到达时间 </td>
                 <td><input type="text" onclick="WdatePicker({ dateFmt: 'HH:mm' })" class="dateinput w106 required endTime"  readonly="readonly"/> + <input type="text" data-type="number" class="inputTxt w50_30 days" value="0" data-min="0" /> 天</td>
             </tr>
             <tr>
                 <td colspan="4" class="up_load_visa_info_td01 hotel_air_price">
                     <table class="new_hotel_p_table2" style="width:900px !important; display:none;">
                     </table>
                   	
                   
                   
                     <table class="new_hotel_p_table2" style="width:900px !important;">
                         <tbody>
                         <c:forEach items="${travelerTypes }" var="travelerType">
                             <tr>
                                 <td width="103" class="tr nnew_hotel_p_table2_tdblue" data-text="${travelerType.name }" data-value="${travelerType.uuid }">
                                     ${travelerType.name }同行<br />
                                     （机+酒）价/人
                                 </td>
                                 <td width="776" style="text-align:left !important; ">
                                     <span class="add_jbox_repeat_thj2">
                                         <select name="select5" class="w50_30 currency">
                                             <c:forEach items="${currencyList}" var="item">
											    <option value="${item.id}" >
										    		${item.currencyMark}
										    	</option>
											</c:forEach>
                                         </select>
                                         <input type="text" data-type="float" class="inputTxt w50_30 babyPrice mr25" />
                                     </span>
                                 </td>
                             </tr>
                        </c:forEach>
                             <tr>
                                 <td class="tr new_hotel_p_table2_tdblue">控票数</td>
                                 <td class="tc" style="text-align:left !important;">
                                     <input type="text" data-type="number" class="inputTxt w50_30 spread fl " value="0"/>
                                     <!-- <a class="fl maring_left10"> <span>选择</span> --> <span class="new_flight_control">该处需先选择航空公司才可以填写。</span></a>
                                 </td>
                             </tr>
                             <tr>
                                 <td class="tr new_hotel_p_table2_tdblue">非控票数</td>
                                 <td class="tl" style="text-align:left !important; "><input type="text" data-type="number" data-min="0" class="inputTxt w50_30 spread fkpNum" /></td>
                             </tr>
                             <tr>
                                 <td class="tr new_hotel_p_table2_tdblue">票数总计</td>
                                 <td style="text-align:left !important; "></td>
                             </tr>
                             <tr>
                                 <td class="tr new_hotel_p_table2_tdblue">余位</td>
                                 <td style="text-align:left !important; ">10</td>
                             </tr>
                         </tbody>
                     </table>
                 </td>
             </tr>
             <tr>
                 <td class="tr">优先扣减</td>
                 <td colspan="3">
                     <span class="mar_left_35">
                         <input class="redio_martop_4" type="radio" name="kp_radio" id="kp_radio" checked="checked" />
                         <label for="kp_radio">控票数</label>
                     </span>
                     <span class="mar_left_35">
                         <input class="redio_martop_4" type="radio" name="kp_radio" id="fkp_radio" />
                         <label for="fkp_radio">非控票数</label>
                     </span>
                 </td>
             </tr>
             <tr>
                 <td class="tr">预收</td>
                 <td>
                     <input type="text" data-type="number" data-min="0" class="inputTxt w50_30 spread" />
                     人
                 </td>
                 <td class="tr new_hotel_p_table2_tdblue">需交订金</td>
                 <td>
                     <select name="select13" class="w50_30">
                         <c:forEach items="${currencyList}" var="item">
							    <option value="${item.id}" >
						    		${item.currencyMark}
						    	</option>
							</c:forEach>
                     </select>
                     <input type="text" data-type="float"  class="inputTxt w50_30 spread" />
                 </td>
             </tr>
             <tr>
                 <td class="tr valign_top">备注</td>
                 <td colspan="3"><textarea class="inputTxt spread" style=" width:90%; height:100px;"></textarea></td>
             </tr>
         </table>
     </div>
 </div>
 <script type="text/javascript">
     $(document).ready(function () {
         // 国家--岛屿--酒店联动
         // 数据结构
/*          var country = {
             "中国": {
                 "海南": ["希尔顿", "七天", "如家"],
                 "舟山": ["华住", "速八", "万豪"]
             },
             "日本": {
                 "九州": ["君澜", "七天", "如家"],
                 "北海道": ["希尔顿", "速八", "锦江"]
             }
         } */
        /* $("#country").on('change', function () {
             var $island = $("#island").empty();
             var island = country[$("#country").val()] || {};
             for (var key in island) {
                 $island.append("<option>" + key + "</option>");
             }
             $("#island").change();
         }); */
        /*  $("#island").on('change', function () {
             var $hotel = $("#hotel").empty();
             var island = country[$("#country").val()] || {};
             var hotel = island[$("#island").val()] || [];
             for (var i = 0, l = hotel.length; i < l; i++) {
                 $hotel.append("<option>" + hotel[i] + "</option>");
             }
         }); */

         $("#addGroup").on('click', function () {
         	addGroup_box();
         	 getAjaxSelect('roomtype',$("#hotelUuid"));
             getAjaxSelect('islandway',$("#islandUuid"),'notHotel');
             getAjaxSelect('hotelrank',$("#hotelUuid"));
         });

         $("#contentTable_new").on('click', 'a.copyLink', function () {
             var id = $("#contentTable_new tbody tr").has(this).prop('id');
             addGroup_box(id, true);
         }).on('click', 'a.updateLink', function () {
             var id = $("#contentTable_new tbody tr").has(this).prop('id');
             addGroup_box(id, false);
         }).on('click', 'a.delLink', function () {
             var id = $("#contentTable_new tbody tr").has(this).prop('id');
             $("#contentTable_new tbody tr[data-tag='" + id + "']").remove();
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
         var airlinesData = ${airlineInfoAll};
         var addGroup_box = (function () {
             var $tempRow = $("#contentTable_new tbody tr:first").clone();
			
             var boxHtml = $("#jbox_haidaoyou_fab").html();
             
		//查询舱位等级
		function getSpaceLevelAjax(type,airlineCode,flightnum){
			var _data = {};
			var res = null;
			if(type=='space_level'){//舱位等级
				if(airlineCode==null || airlineCode=='' || flightnum==null || flightnum=='' || flightnum=='请选择'){
					return null;
				}
				_data={"type":type,"airlineCode":airlineCode,"flightnum":flightnum};
			}else if(type=='traveler_type'){
				_data={"type":type};
			}else if(type=='currency_list'){
				_data={"type":type};
			}else{
				return null;
			}
			$.ajax({
	        		type: "POST",
	        		async:false,
	        	   	data: _data,
	        		dataType: "json",
	        		url: "${ctx}/activityIsland/getAirlineInfoByType",
	        	   	success: function(data){
	        	   		res = data;
	        	   	}
	        	});
				return res;
		}
             function initEvent() {
                 var $table = $("#jbox_tq table.table_product_info");
                 // 房型模板
                 var $pHouseType = $table.find("p.houseType:first").clone();
                 $pHouseType.find('a.addHouseType').replaceWith('<a class="ydbz_x gray delHouseType">删除</a>');
                 // 餐型模板
                 var $trMealType = $table.find("tr.mealType:first").clone();
                 $trMealType.find('a.addMealType').replaceWith('<a class="ydbz_x gray delMealType">删除</a>');
                 $trMealType.find('td:first').hide();
                 // 升级餐型模板
                 var $trUpMealType = $table.find("p.upMealType:first").clone();
                 $trUpMealType.find('a.addUpMealType').replaceWith('<a class="ydbz_x gray delUpMealType">删除</a>');

                 $table.on('click', 'a.addHouseType', function () {
                     // 添加房型
                     $pHouseType = $table.find("p.houseType:first").clone();
                 	 $pHouseType.find('a.addHouseType').replaceWith('<a class="ydbz_x gray delHouseType">删除</a>');
                     $table.find("p.houseType:last").after($pHouseType.clone());
                      //$table.find("p.houseType:last").after($table.find("p.houseType:first").clone());
                 }).on('click', 'a.delHouseType', function () {
                     // 删除房型
                     $table.find("p.houseType").has(this).remove();
                 }).on('click', 'a.addMealType', function () {
                 	 $trMealType = $table.find("tr.mealType:first").clone();
	                 $trMealType.find('a.addMealType').replaceWith('<a class="ydbz_x gray delMealType">删除</a>');
	                 $trMealType.find('td:first').hide();
                     // 添加餐型
               		 $table.find("tr.mealType:last").after($trMealType.clone());
                     $table.find("tr.mealType:first td:first").attr("rowspan", $table.find("tr.mealType").length);
                 }).on('click', 'a.delMealType', function () {
                     // 删除餐型
                     $table.find("tr.mealType").has(this).remove();
                     $table.find("tr.mealType:first td:first").attr("rowspan", $table.find("tr.mealType").length);
                 }).on('click', 'a.addUpMealType', function () {
	                  var $trUpMealType_ = $table.find("p.upMealType:first").clone();
	                 $trUpMealType_.find('a.addUpMealType').replaceWith('<a class="ydbz_x gray delUpMealType">删除</a>');
                     // 添加升级餐型
                     $table.find("td").has(this).append($trUpMealType_.clone());
                 }).on('click', 'a.delUpMealType', function () {
                     // 删除升级餐型
                     $table.find("p.upMealType").has(this).remove();
                 }).on("click", "tr.mealType input:checkbox", function () {
                     // 是否升级餐型
                     var checked = $(this).prop("checked");
                     $table.find("td").has(this).attr("colspan", checked ? 1 : 3);
                     $table.find("td").has(this).nextAll("td")[checked ? "show" : "hide"]();
                 }).on("click", "a.selLink", function (event) {
                     // 选择控票数
                     var $parentTr = $(this).parentsUntil("tr").parent();
                     var offset = $parentTr.next().offset();
                     $(this).find("div.pop_inner_outer").show().offset(offset);
                     $table.find("div.pop_inner_outer").not($(this).find("div.pop_inner_outer")).hide();
                     event.stopPropagation();
                 }).on('change', 'select.selAirline', function () {
                     // 更改航空公司
                     var FLTNos = airlinesData[$(this).val()];
                     var options = [];
                     if (FLTNos) {
                     	options.push("<option data-start='' data-end=''>请选择</option>");
                         for (var key in FLTNos) {
                             options.push("<option data-start='", FLTNos[key].start, "' data-end='", FLTNos[key].end, "' data-day='", (FLTNos[key].day || 0), "'>", key, "</option>");
                         }
                     }
                     $table.find("select.fltNo").empty().append(options.join('')).change();
                 }).on('change', 'select.fltNo', function () {
                 		var airlineCode = $("#jbox_tq table.table_product_info .selAirline").val();
                 		var space_level = $("#jbox_tq table.table_product_info .fltNo").val();
                     // 更改航班
                     if ($(this).prop("selectedIndex") <= 0) {
                         $table.find("td.hotel_air_price > table:eq(0)").hide();
                         $table.find("td.hotel_air_price > table:eq(1)").show();
                         $table.find("input.startTime").attr("disabled", "disabled").val('');
                         $table.find("input.endTime").attr("disabled", "disabled").val('');
                         $table.find("input.days").attr("disabled", "disabled").val('');
                     } else {
                    	 var res_1 = getSpaceLevelAjax('space_level',airlineCode,space_level);
                     	if(res_1!=null){
                     	var res_2 = getSpaceLevelAjax('traveler_type','','');
                     	var res_3 = getSpaceLevelAjax('currency_list','','');
                     	//动态航班--余位部分  开始
                     	//动态 舱位等级
                     	var tableNode = $table.find("td.hotel_air_price > table:eq(0)");
                     	$(tableNode).html('');
                     	var trNode1 = $("<tr/>");
                     	$(trNode1).append($("<th/>").attr("style","width:112px").attr("class","tr new_hotel_p_table2_tdblue2 new_hotel_p_table2_tdblue2_mr20").text("舱位等级"));
                     	$(res_1.space_level).each(function(i,item){
                     		$(trNode1).append($("<th/>").attr("style","width:"+(671/($(res_1.space_level).length))+"px").attr("data-value",item.spaceLevel).text(item.space));
                     	});
                     	$(trNode1).append($("<th/>").attr("style","width:31").text("合计"));
                     	var theadNode = $("<thead/>").append(trNode1);
                     	$(tableNode).append(theadNode);
                     	//动态 同行价
                     	var tbodyNode = $("<tbody/>");
                     	$(res_2.traveler_type).each(function(i,item){
                         	var trNode2 = $("<tr/>");
                     		var tdNode2_1 = $("<td/>").attr("class","tr new_hotel_p_table2_tdblue").attr("data-text",item.name).attr("data-value",item.uuid).html(item.name+"同行<br/>（机+酒）价/人");
                     		$(trNode2).append(tdNode2_1);
                     		$(res_1.space_level).each(function(j,n){
                     			var tdNode2_2 = $("<td/>").attr("style","text-align:left !important; ");
                     			var spanNode_ = $("<span/>").attr("class","add_jbox_repeat_thj2");
                     			var selectNode_ = $("<select/>").attr("name","select5").attr("class","w50_30 currency");
                     			$(res_3.currency_list).each(function(k,m){
                     				var optionNode_ = $("<option/>").val(m.id).text(m.currencyMark);
                     				$(selectNode_).append(optionNode_);
                     			});
                     			$(spanNode_).append(selectNode_);
                     			$(spanNode_).append($("<input/>").attr("type","text").attr("data-type","float").attr("class","inputTxt w50_30 babyPrice mr25"));
                     			$(tdNode2_2).append(spanNode_);
                     			
                     			$(trNode2).append(tdNode2_2);
                     		});
                     		var tdNode2_3 = $("<td/>").text("---");
                     		$(trNode2).append(tdNode2_3);
                     		$(tbodyNode).append(trNode2);
                     	});  
                     	//动态 控票数
                         var trNode3 = $("<tr/>");
                         $(trNode3).append($("<td/>").attr("class","tr new_hotel_p_table2_tdblue").text("控票数"));
                         $(res_1.space_level).each(function(){
                         	var tdNode3_2 = $("<td/>").attr("class","tc").attr("style","width:215px !important");
                         	$(tdNode3_2).append($("<input/>").attr("type","text").attr("class","inputTxt w50_30 spread mar_left_35"));
                     		$(trNode3).append(tdNode3_2);
                     	 });
                         $(trNode3).append($("<td/>"));   
                         $(tbodyNode).append(trNode3);
                     	//动态 非控票数
                         var trNode4 = $("<tr/>");
                         var tdNode4_1 = $("<td/>").attr("class","tr new_hotel_p_table2_tdblue").text("非控票数");
                         $(trNode4).append(tdNode4_1);
                         $(res_1.space_level).each(function(){
                     		var tdNode4_2 = $("<td/>").append($("<input/>").attr("type","text").attr("data-type","number").attr("data-min","0").attr("class","inputTxt w50_30 spread mar_left_35 fkpNum"));
                     		$(trNode4).append(tdNode4_2);
                     	 });
                     	 var tdNode4_3 = $("<td/>");
                         $(trNode4).append(tdNode4_3);   
                         $(tbodyNode).append(trNode4);
                     	//动态 票数总计
                     	var trNode5 = $("<tr/>");
                         var tdNode5_1 = $("<td/>").attr("class","tr new_hotel_p_table2_tdblue").text("票数总计");
                         $(trNode5).append(tdNode5_1);
                         $(res_1.space_level).each(function(){
                     		var tdNode5_2 = $("<td/>");
                     		$(trNode5).append(tdNode5_2);
                     	 });
                         var tdNode5_3 = $("<td/>");
                         $(trNode5).append(tdNode5_3);   
                         $(tbodyNode).append(trNode5); 
                     	 //动态 余位
                     	 var trNode6 = $("<tr/>");
                         var tdNode6_1 = $("<td/>").attr("class","tr new_hotel_p_table2_tdblue").text("余位");
                         $(trNode6).append(tdNode6_1);
                         $(res_1.space_level).each(function(){
                     		var tdNode6_2 = $("<td/>").text(10);
                     		$(trNode6).append(tdNode6_2);
                     	 });
                         var tdNode6_3 = $("<td/>").text(30);
                         $(trNode6).append(tdNode6_3);   
                         $(tbodyNode).append(trNode6); 
                         $(tableNode).append(tbodyNode);
                         }
                     	 //动态  结束
                         $table.find("td.hotel_air_price > table:eq(1)").hide();
                         $table.find("td.hotel_air_price > table:eq(0)").show();
                         $table.find("input.startTime").removeAttr("disabled");
                         $table.find("input.endTime").removeAttr("disabled");
                         $table.find("input.days").removeAttr("disabled");
                         var $option = $(this).find("option:selected");
                         $table.find("input.startTime").val($option.attr("data-start") || '');
                         $table.find("input.endTime").val($option.attr("data-end") || '');
                         $table.find("input.days").val($option.attr("data-day") || '0');
                     };
                 }).on('change', 'input:checkbox.procurement', function () {
                     var text = $(this).attr("data-text");
                     var checked = $(this).prop("checked");
                     $table.find("div.pop_inner_outer").has(this).find("tr[data-type='" + text + "']")[checked ? "show" : "hide"]();
                 }).on('click', "div.pop_inner_outer input.btn_confirm_inner_outer02", function (event) {
                     // 选择控票数 确定
                     var $div = $("div.pop_inner_outer").has(this);
                     var sum = 0;
                     $div.find("tr:visible td:last-child").each(function () {
                         sum += (parseInt($(this).find("input").val()) || 0);
                     });
                     $("a.selLink").has(this).prev("input").val(sum);
                     $div.hide();
                     event.stopPropagation();
                     ticketAmount();
                 }).on("blur", "input.fkpNum", function () {
                     // 非空票数失去焦点事件
                     ticketAmount();
                 });
                 $(document).click(function (event) {
                     if (!$table.find("div.pop_inner_outer").has(event.target).length && !$table.find("div.pop_inner_outer").is(event.target)) {
                         $table.find("div.pop_inner_outer").hide();
                     }
                 });
                 $table.find("input:checkbox.procurement").change();
             }

             function initData(id) {
             	 getAjaxSelect('roomtype',$("#hotelUuid"));
             	 getAjaxSelect('islandway',$("#islandUuid"),'notHotel')
             	 
                 var $row = $("#" + id);
                 if (!$row.length) return;

                 var $tds = $row.find("td");
                 var $dataTrs = $("#jbox_tq table.table_product_info > tbody > tr");//弹出层行
                 var cIndex = 0;
                 var rIndex = 0;
                 var $td;
                 // 团号
                 $dataTrs.eq(rIndex).find("input").val($tds.eq(cIndex).find('a').text());
                 rIndex++;
                 // 日期
                 $dataTrs.eq(rIndex).find("input").val($tds.eq(cIndex).find('span').text());
                 rIndex++;
                 cIndex++;
                 // 房型
                 $tds.eq(cIndex).find('p').each(function (i) {
                     if (i > 0) {
                         $dataTrs.find("a.addHouseType").click();
                     }
                     var $lastP = $('#jbox_tq p.houseType:last');
                     var spanVal = $(this).find("span:eq(0)").attr("data-value");
                     var optionObj;
                     var options = $lastP.find("select:eq(0)").find("option");
                     $(options).each(function(_i,_item){
                     	if($(_item).val()==spanVal){
                     		optionObj = _item; 
                     	}
                     });
                     $(optionObj).attr("selected","selected");
                     $lastP.find("input").val($(this).find("span:eq(1)").text());
                 });
                 var roomType_0 = $tds.eq(cIndex).find("p > select:eq(0)")
                 getAjaxSelect('foodtype',roomType_0);
                 cIndex++;
                 rIndex++;
                 // 餐型
                 $("#contentTable_new tr[data-tag='" + id + "']").each(function (i) {
                     if (i > 0) {
                         $dataTrs.find("a.addMealType").click();
                     }
                     $td = $(this).find("td").eq(i ? 0 : 2);
                     var $lastTr = $('#jbox_tq tr.mealType:last');
                   	//列表 基础餐型
                   $lastTr.find("select:eq(0)").val($td.attr("data-value"));
                     var $ps = $td.next().find("p");
                     if ($ps.length) {
                         $lastTr.find("input:checkbox").click();
                         $.each($ps, function (j) {
                             if (j > 0) {
                                 $lastTr.find("a.addUpMealType").click();
                             }
                             var $lastP = $('#jbox_tq p.upMealType:last');
                             $lastP.find("select:eq(0)").val($(this).find("span:eq(0)").attr("data-value"));
                             $lastP.find("select:eq(1)").val($(this).find("span:eq(1)").attr("data-value"));
                             $lastP.find("input").val($(this).find("span:eq(2)").text());
                         });
                     }
                     rIndex++;
                 });
                 cIndex += 2;
                 $dataTrs = $("#jbox_tq table.table_product_info > tbody > tr");
                 // 上岛方式
                 $td = $dataTrs.eq(rIndex).find("td:eq(1)");
                 $tds.eq(cIndex).find('p').each(function () {
                     $td.find("input:checkbox[data-text='" + $(this).text() + "']").prop("checked", true);
                 });
                 cIndex++;
                 // 单房差
                 $td = $dataTrs.eq(rIndex).find("td:eq(3)");
                 $td.find("select:eq(0)").val($tds.eq(9).find("span:eq(0)").attr("data-value"));
                 $td.find("input").val($tds.eq(9).find("span:eq(1)").text());
                 $td.find("select:eq(1)").val($tds.eq(9).find("span:eq(2)").attr("data-value"));
                 rIndex++;
                 // 航空公司
                 var airlines = ($tds.eq(cIndex).find('span:eq(0)').attr("data-value") || '').split(',');
                 $dataTrs.eq(rIndex).find("select:eq(0)").val(airlines[0]).change();
                 // 航班号
                 $dataTrs.eq(rIndex).find("select:eq(1)").val(airlines[1]).change();
                 rIndex++;
                /*  // 起飞时间
                 $dataTrs.eq(rIndex).find("input:eq(0)").val($tds.eq(cIndex).find('span:eq(1)').attr("data-start"));
                 // 到达时间
                 $dataTrs.eq(rIndex).find("input:eq(1)").val($tds.eq(cIndex).find('span:eq(1)').attr("data-end"));
                 // 到达天数
                 $dataTrs.eq(rIndex).find("input:eq(2)").val($tds.eq(cIndex).find('span:eq(1)').attr("data-day") || 0); */
                 rIndex++;
                 cIndex++;

                 var airSelected = airlines.length == 2;
                 //舱位等级&价格&余位
                 if (airSelected) {
                     var $trs = $dataTrs.eq(rIndex).find("td.hotel_air_price > table:eq(0) > tbody > tr");
                     
                     $tds.eq(cIndex).children('div').each(function (i) {
                         // 控票数
                         $trs.eq(-4).children("td").eq(i + 1).find("input:first").val($(this).attr("data-kp"));
                         // 非空票数
                         $trs.eq(-3).children("td").eq(i + 1).find("input:first").val($(this).attr("data-fkp"));
                         $(this).find("p").each(function (j) {
                             var $this = $(this);
                             if (j == 0) {
                             } else {
                                 $td = $trs.eq(j - 1).find("td").eq(i + 1);
                                 $td.find("select:eq(0)").val($this.find("span:eq(0)").attr("data-value"));
                                 $td.find("input").val($this.find("span:eq(1)").text());
                             }
                         });
                     });
                     ticketAmount();
                 } else {
                     var $trs = $dataTrs.eq(rIndex).find("td.hotel_air_price > table:eq(1) > tbody > tr");
                     // 非空票数
                     $trs.eq(-3).children("td:eq(1)").find("input:first").val($tds.eq(cIndex).find('div').attr("data-fkp"));
                     $tds.eq(cIndex).find("p").each(function (j) {
                         var $this = $(this);
                         $td = $trs.eq(j).find("td:eq(1)");
                         $td.find("select:eq(0)").val($this.find("span:eq(0)").attr("data-value"));
                         $td.find("input").val($this.find("span:eq(1)").text());
                     });
                 }
                 rIndex++;
                 cIndex++;
                 // 余位/票数总计
                 cIndex++;
                 // 优先扣减
                 var checked = $tds.eq(cIndex).attr("data-kp") == "true";
                 $dataTrs.eq(rIndex).find("input:radio").eq(checked ? 0 : 1).attr("checked", "checked");
                 rIndex++;
                 // 预收/预报名
                 $td = $dataTrs.eq(rIndex).find("td:eq(1)");
                 $td.find("input:eq(0)").val($tds.eq(cIndex).find('span:eq(0)').text());
                 cIndex++;
                 // 单房差
                 cIndex++;
                 // 需交定金
                 $td = $dataTrs.eq(rIndex).find("td:eq(3)");
                 $td.find("select").val($tds.eq(cIndex).find('span:eq(0)').attr("data-value"));
                 $td.find("input:eq(0)").val($tds.eq(cIndex).find('span:eq(1)').text());
                 cIndex++;
                 rIndex++;
                 // 备注
                 var ttt = $tds.eq(cIndex).attr("data-text");
                 $dataTrs.eq(rIndex).find("textarea").val(ttt);
             }

             function saveData(id, isAdd) {
                 var $row = $("#" + id);

                 if (isAdd) {
                     id = buildID();
                     $row.length || ($row = $tempRow);
                     $row = $row.clone().attr({ "id": id, "data-tag": id }).show();
                     $("#contentTable_new tbody").append($row);
                 }

                 $row.nextAll("tr[data-tag='" + id + "']").remove();
                 var $tds = $row.find("td");
                 var $dataTrs = $("#jbox_tq table.table_product_info > tbody > tr");
                 var $td;

                 var rIndex = 0;
                 var cIndex = 0;
                 var html = [];
                 // 团期
                 $tds.eq(cIndex).find('a').text($dataTrs.eq(rIndex).find("input").val());
                 //团期-隐藏域值
                 var $groupCodes = $dataTrs.eq(rIndex).find("input").val()==''?null:$dataTrs.eq(rIndex).find("input").val(); 
                 //团期-隐藏域
                 $tds.eq(cIndex).find("input[name=groupCodes]").remove();
                 $tds.eq(cIndex).append($("<input/>").attr("type","hidden").attr("name","groupCodes").val($groupCodes));
                 rIndex++;
                 // 日期
                
                 var groupDateStr = $dataTrs.eq(rIndex).find("input").val();
                 $tds.eq(cIndex).find('span').text(groupDateStr);
                 //日期-隐藏域值
                 var $groupOpenDates = $dataTrs.eq(rIndex).find("input").val()==''?null:$dataTrs.eq(rIndex).find("input").val();
                 $tds.eq(cIndex).find("input[name=groupOpenDates]").remove();
                 $tds.eq(cIndex).append($("<input/>").attr("type","hidden").attr("name","groupOpenDates").val($groupOpenDates));
                 rIndex++;
                 cIndex++;
                 // 房型晚数
                  var $roomsNights = '';//房型晚数-隐藏域值
                 var $len_roomsNights = $dataTrs.eq(rIndex).find("p").size();//房型晚数-拼串-计数器
                 $dataTrs.eq(rIndex).find("p").each(function (i) {
                	 var $len_roomsNights_1 = $(this).find('span').size();//房型晚数-拼串-计数器-1
                     var $this = $(this);
                     html.push('<p>');
                     html.push('<span data-value="', $this.find("select:eq(0)").val(), '">', $this.find("select:eq(0) option:selected").text(), '</span>*');
                     html.push('<span>', $this.find("input:eq(0)").val(), '</span>晚');
                     html.push('</p>');
                     $roomsNights+=$this.find("select:eq(0)").val()==''?null:$this.find("select:eq(0)").val();
                     $roomsNights+="-";
                     $roomsNights+=$this.find("input:eq(0)").val()==''?null:$this.find("input:eq(0)").val();
                     if(i<$len_roomsNights-1){//拼串-末位不加连接符
                 		$roomsNights +=";";
                 	}
                 });
                 $tds.eq(cIndex).html(html.join(''));
                 //房型晚数-隐藏域
                 $tds.eq(cIndex).find("input[name=roomsNights]").remove();
                 $tds.eq(cIndex).append($("<input/>").attr("type","hidden").attr("name","roomsNights").val($roomsNights));
                 rIndex++;
                 cIndex++;
                 // 餐型
                 var $mealTypeTr = $dataTrs.filter("tr.mealType");
                 var $lastTr = $row;
                 //餐型-隐藏域值
                 var $islandGroupMeals='';
                 //餐型-拼串-计数器
                 var len_islandGroupMeals=$mealTypeTr.size();
                 $.each($mealTypeTr, function (i) {
                     var $this = $(this);
                     if (i > 0) {
                         html = ['<tr data-tag="', id, '"><td class="tc"></td><td class="tc"></td></tr>'];
                         var $tr = $(html.join(''));
                         $lastTr.after($tr);
                         $lastTr = $tr;
                     }
                     $td = $lastTr.find("td").eq(i ? 0 : 2);
                     $td.text($this.find("select:eq(0) option:selected").text()).attr("data-value", $this.find("select:eq(0)").val());
                     $islandGroupMeals +=$this.find("select:eq(0)").val()==''?null:$this.find("select:eq(0)").val();//基本餐型-
                     
                     //升级餐型
                     html = [];
                     //升级餐型-拼串-计数器
                     var $len_islandGroupMeals_1 = $this.find("td:eq(3):visible p").size();
                     //基本餐型-升级餐型
                     if($len_islandGroupMeals_1>0){
                     	$islandGroupMeals+='-';
                     }
                     $this.find("td:eq(3):visible p").each(function (j) {
                     	
                         var $this = $(this);
                         html.push('<p>');
                         html.push('<span data-value="', $this.find("select:eq(0)").val(), '">', $this.find("select:eq(0) option:selected").text(), '</span>');
                         html.push('<span data-value="', $this.find("select:eq(1)").val(), '">', $this.find("select:eq(1) option:selected").text(), '</span>');
                         html.push('<span>', $this.find("input:eq(0)").val(), '</span>/人');
                         html.push('</p>');
                         var $islandGroupMeals_type = $this.find("select:eq(0)").val()==''?null:$this.find("select:eq(0)").val();
                         var $islandGroupMeals_currencyId = $this.find("select:eq(1)").val()==''?null:$this.find("select:eq(1)").val();
                         var $islandGroupMeals_price = $this.find("input:eq(0)").val()==''?null:$this.find("input:eq(0)").val();
                         $islandGroupMeals+=$islandGroupMeals_type+'@'+$islandGroupMeals_currencyId+'@'+$islandGroupMeals_price;
                        if(j<$len_islandGroupMeals_1-1){//升级餐型#升级餐型
                        		$islandGroupMeals +='#';
                        } 
                     });
                     if(i<len_islandGroupMeals-1){//基本餐型-升级餐型@币种@价格#升级餐型@币种@价格；基本餐型-升级餐型@币种@价格#升级餐型@币种@价格
                         	$islandGroupMeals+=';';
                     }
                     $td.next().html(html.join(''));
                     rIndex++;
                 });
                 $tds.not(":eq(2),:eq(3)").prop("rowspan", $mealTypeTr.length);
                 //餐型-隐藏域
                 if($islandGroupMeals=='null'){
                 	$islandGroupMeals='';
                 }
                 $td.find("input[name=islandGroupMeals]").remove();
                 $td.append($("<input/>").attr("type","hidden").attr("name","islandGroupMeals").val($islandGroupMeals));
                 cIndex += 2;
                 // 上岛方式
                 html = [];
                 //上岛方式-隐藏域值
                 var $islandWays = '';
                 //上岛方式-拼串-计数器
                 var $len_islandWays = $dataTrs.eq(rIndex).find("td:eq(1) input:checkbox:checked").size();
                 $dataTrs.eq(rIndex).find("td:eq(1) input:checkbox:checked").each(function (i) {
                     html.push('<p>', $(this).attr('data-text'), '</p>');
                     $islandWays+=$(this).attr('data-value')==''?null:$(this).attr('data-value');//上岛方式-拼串
                     if(i<$len_islandWays-1){//最后一位不加';'
                     	$islandWays+=";";
                     }
                 });
                 $tds.eq(cIndex).html(html.join(''));
                 //上岛方式-隐藏域
                 $tds.eq(cIndex).find("input[name=islandWays]").remove();
                 $tds.eq(cIndex).append($("<input/>").attr("type","hidden").attr("name","islandWays").val($islandWays));
                 cIndex++;
                 // 单房差
                 $td = $dataTrs.eq(rIndex).find("td:eq(3)");
                 html = ['<span data-value="', $td.find("select:eq(0)").val(), '">', $td.find("select:eq(0) option:selected").text(), '</span>',
                         '<span>', $td.find("input:eq(0)").val(), '</span>',
                         '<span data-value="', $td.find("select:eq(1)").val(), '">', $td.find("select:eq(1) option:selected").text(), '</span>'];
                 $tds.eq(9).html(html.join(''));
                 //单房差-隐藏域值
                 var $singleCurrencyIds = $td.find("select:eq(0)").val()==''?null:$td.find("select:eq(0)").val();
                 var $singlePrices = $td.find("input:eq(0)").val()==''?null:$td.find("input:eq(0)").val();
                 var $singlePriceUnits = $td.find("select:eq(1)").val()==''?null:$td.find("select:eq(1)").val();
                 //单房差-隐藏域
                 $tds.eq(9).find("input[name=currencyIds]").remove();
                 $tds.eq(9).find("input[name=singlePrices]").remove();
                 $tds.eq(9).find("input[name=singlePriceUnits]").remove();
                 $tds.eq(9).append($("<input/>").attr("type","hidden").attr("name","currencyIds").val($singleCurrencyIds));
                 $tds.eq(9).append($("<input/>").attr("type","hidden").attr("name","singlePrices").val($singlePrices));
                 $tds.eq(9).append($("<input/>").attr("type","hidden").attr("name","singlePriceUnits").val($singlePriceUnits));
                 rIndex++;
                 //cIndex++;
                 // 航空公司
                 html = [];
                 $td = $dataTrs.eq(rIndex).find("td:eq(1)");
                 var airlineIndex = $td.find("select:eq(0)").prop("selectedIndex");
                 //航空公司-隐藏域值
                 var $islandGroupAirlines = '';
                 
                 if (airlineIndex) {
                     var airline = $td.find("select:eq(0)").val();
                     //航班-隐藏域-航空公司
                     if(airline!=''){
                     	$islandGroupAirlines+=airline+"-";
                     }
                     //  航班号
                     $td = $dataTrs.eq(rIndex).find("td:eq(3)");
                     var FLTNo = $td.find("select:eq(0)").val();
                     //航班-隐藏域-航班号
                     var $islandGroupAirlines_fltNo = FLTNo==''?null:FLTNo;
                     //拼串-航班号
                     $islandGroupAirlines+=FLTNo+"-";
                     var airlineText = $td.find("select:eq(0) option:selected").text();
                     if(airlineText=='请选择'){
                     	airlineText='';
                     }
                     html.push('<span data-value="', airline, ',', FLTNo, '">', airlineText, '</span><br>');
                     rIndex++;
                     // 航班
                     var start = $dataTrs.eq(rIndex).find("input:eq(0)").val();
                     //航班-隐藏域-起飞时间
                     var $islandGroupAirlines_start = start==''?null:start;
                     //拼串-起飞时间
                     $islandGroupAirlines+=$islandGroupAirlines_start+"-";//隐藏域拼 起飞时间
                     var end = $dataTrs.eq(rIndex).find("input:eq(1)").val();
                     //航班-隐藏域-到达时间
                     var $islandGroupAirlines_end = end==''?null:end;
                     //拼串-到达时间
                     $islandGroupAirlines+=end+"-";
                     var days = parseInt($dataTrs.eq(rIndex).find("input:eq(2)").val());
                     //航班-隐藏域-天数
                     var $islandGroupAirlines_days = null;
                     if(isNaN(days)){
                     	days = null;
                     }else if(isNaN==''){
                     	days = null;
                     }else{
                     	$islandGroupAirlines_days = days;
                     }
                     //拼串-天数
                     $islandGroupAirlines+=days+"-";
                     html.push('<span data-start="', start, '" data-end="', end, '" data-day="', days, '" class="lieHt30 fbold">', start, '-', end, '</span>');
                     if (days > 0) {
                         html.push('<span class="lianyun_name next_day_icon">', days == 1 ? '次日' : ('+' + days), '</span>');
                     }
                     
                     rIndex++;
                 } else {
                     rIndex += 2;
                 }
                 $tds.eq(cIndex).html(html.join(''));
                 cIndex++;
                 // 舱位等级&价格&余位
                 html = [];
                 var rowNames = [];
                 if (airlineIndex) {
                 	//航班-隐藏域-舱位等级
                 	var $islandGroupAirlines_level = null;
                     var $ths = $dataTrs.eq(rIndex).find("td.hotel_air_price > table:eq(0) > thead > tr >th");
                     var $trs = $dataTrs.eq(rIndex).find("td.hotel_air_price > table:eq(0) > tbody > tr");
                     var $islandGroupAirlines_='';
                     
                     for (var i = 1, cl = $ths.length - 1; i < cl; i++) {
                     	$islandGroupAirlines_+=$islandGroupAirlines;
                     	//机票价格-隐藏域值
                 		var $islandGroupPrices='';
                 		$islandGroupAirlines_level = $ths.eq(i).attr("data-value");
                     
                  		$islandGroupAirlines_+=$islandGroupAirlines_level+"-";//隐藏域拼 舱位等级
                         // 控票数
                         var kpNum = $trs.eq(-4).children("td").eq(i).find("input:first").val();
                         var $islandGroupAirlines_kpNum = null;
                         if(isNaN(kpNum)){
                         	$islandGroupAirlines_kpNum = null;
                         }else if(kpNum==''){
                         	$islandGroupAirlines_kpNum = null;
                         }else{
                         	$islandGroupAirlines_kpNum = kpNum;
                         }
                         //拼串-控票数
                         $islandGroupAirlines_+=$islandGroupAirlines_kpNum+"-";
                         // 非控票数
                         var fkpNum = $trs.eq(-3).children("td").eq(i).find("input:first").val();
                         var $islandGroupAirlines_fkpNum = null;
                         if(isNaN(kpNum)){
                         	$islandGroupAirlines_fkpNum = null;
                         }else if(kpNum==''){
                         	$islandGroupAirlines_fkpNum = null;
                         }else{
                         	$islandGroupAirlines_fkpNum = fkpNum;
                         }
                         //拼串-非控票数
                         $islandGroupAirlines_+=$islandGroupAirlines_fkpNum+"-";
                         //余位
                         var islandGroupAirlines_yw = $trs.eq(-1).children("td").eq(i).text();
                         var $islandGroupAirlines_yw = null;
                         if(isNaN(islandGroupAirlines_yw)){
                         	$islandGroupAirlines_yw = null;
                         }else if(islandGroupAirlines_yw==''){
                         	$islandGroupAirlines_yw = null;
                         }else{
                         	$islandGroupAirlines_yw = islandGroupAirlines_yw;
                         }
                         //拼串-余位
                         $islandGroupAirlines_+=$islandGroupAirlines_yw;
/*                          if(i<3){
                         	$islandGroupAirlines_+=';';
                         } */
                         html.push('<div class="cw_thjg_yw" data-kp="', kpNum, '" data-fkp="', fkpNum, '">');
                         html.push("<p>", $ths.eq(i).text(), "(");
                         html.push('<span class="or_color over_handle_cursor" title="控票：', kpNum, ' 非控票：', fkpNum, '">余位：', $trs.eq(-1).children("td").eq(i).text(), '</span>');
                         html.push(')</p>');
                         for (var j = 0; j < $trs.length - 4; j++) {
                             i == 1 && (rowNames[j] = $trs.eq(j).find("td:eq(0)").attr("data-text"));
                             $td = $trs.eq(j).find("td").eq(i);
                             html.push("<p>", rowNames[j], "：");
                             html.push('<span data-value="', $td.find("select:eq(0)").val(), '">', $td.find("select:eq(0) option:selected").text(), '</span>');
                             html.push('<span>', $td.find("input:eq(0)").val(), '</span>');
                             html.push("</p>");
                             var ck_type = $trs.eq(j).find("td:eq(0)").attr("data-value");
                          $islandGroupPrices+=ck_type+'@';
                          $islandGroupPrices+=$td.find("select:eq(0)").val()+'@';//价格相关 隐藏域
        					$islandGroupPrices+=$td.find("input:eq(0)").val()==''?null:$td.find("input:eq(0)").val();
        					if(j<$trs.length-4-1){
        						$islandGroupPrices+='#';
        					}
                         }
                         if($islandGroupPrices!=null && $islandGroupPrices!=''){
                         	 $islandGroupAirlines_+="-";
                          	$islandGroupAirlines_+=$islandGroupPrices;
                         }else{
                          	$islandGroupAirlines_+="-";
                          	$islandGroupAirlines_+=null;
                         }
                        
                         if(i<cl-1){
                         	$islandGroupAirlines_+=';';
                         }
                         html.push('</div>');
                     }
                 } else {
                 	var $islandGroupPrices='';
                 	//航班-隐藏域-航空公司
                  	var airline = $td.find("select:eq(0)").val();
                  	if(airline!=''){
                  		$islandGroupAirlines+=$airline+"-";
                  	}
                  	//航班号
                  	$islandGroupAirlines+="null-";
                  	//起飞时间
                  	$islandGroupAirlines+="null-";
                  	//到达时间
                  	$islandGroupAirlines+="null-";
                  	//天数
                  	$islandGroupAirlines+="null-";
                  	//舱位等级
                  	$islandGroupAirlines+="null-";
                     
                     var $trs = $dataTrs.eq(rIndex).find("td.hotel_air_price > table:eq(1) > tbody > tr");
                     //控票数
                     var kpNum = null;
                     if(airline!=''){
                     	kpNum = null;
                     }else{
                     	kpNum = $trs.eq(-4).children("td:eq(1)").find("input:first").val();
                     }
                     $islandGroupAirlines+=kpNum+"-";
                     // 非控票数
                     var fkpNum = $trs.eq(-3).children("td:eq(1)").find("input:first").val();
                     if(fkpNum==''){
                     	fkpNum = null;
                     }
                     $islandGroupAirlines+=fkpNum+"-";//隐藏域拼 非控票数
                     //隐藏域拼接 余位
                     var $yw = $trs.eq(-1).children("td").eq(i).text();
                     if($yw==''){
                     	$yw=null;
                     }
                     $islandGroupAirlines+=$yw+"-";
                     html.push('<div class="cw_thjg_yw" data-fkp="', fkpNum, '">');
                     for (var j = 0; j < $trs.length - 4; j++) {
                         rowNames[j] = $trs.eq(j).find("td:eq(0)").attr("data-text");
                         $td = $trs.eq(j).find("td:eq(1)");
                         html.push("<p>", rowNames[j], "：");
                         html.push('<span data-value="', $td.find("select:eq(0)").val(), '">', $td.find("select:eq(0) option:selected").text(), '</span>');
                         html.push('<span>', $td.find("input:eq(0)").val(), '</span>');
                         html.push("</p>");
                         var ck_type = $trs.eq(j).find("td:eq(0)").attr("data-value");
                         $islandGroupPrices+=ck_type+'@';
                         $islandGroupPrices+=$td.find("select:eq(0)").val()+'@';//价格相关 隐藏域
       					$islandGroupPrices+=$td.find("input:eq(0)").val()==''?null:$td.find("input:eq(0)").val();
       					if(j<$trs.length-4-1){
        						$islandGroupPrices+='#';
        				}
                     }
                     $islandGroupAirlines+=$islandGroupPrices;
                     $islandGroupAirlines_=$islandGroupAirlines;
                     html.push('</div>');
                 }
                 $tds.eq(cIndex).html(html.join(''));
                 //--机票相关信息（隐藏域）开始--
                 $tds.eq(cIndex).find("input[name=islandGroupAirlines]").remove();
                 $tds.eq(cIndex).append($("<input/>").attr("type","hidden").attr("name","islandGroupAirlines").val($islandGroupAirlines_));
                 //$tds.eq(cIndex).append($("<input/>").attr("type","text").attr("name","islandGroupPrices").val($islandGroupPrices));
                 //--机票相关信息（隐藏域）结束--
                
                 cIndex++;
                 rIndex++;
                 // 余位/票数总计
                 html = ['<span class="tdred over_handle_cursor">', $trs.eq(-1).find("td:eq(-1)").text(), '</span>/<span>', $trs.eq(-2).find("td:eq(-1)").text(), '</span>'];
                 $tds.eq(cIndex).html(html.join(''));
                 cIndex++;
                 // 优先扣减
                 $tds.eq(cIndex).attr("data-kp", $dataTrs.eq(rIndex).find("input:radio:eq(0)").prop("checked"));
                //优先扣减-隐藏域值
                 var $priorityDeductions = null;
                 if($dataTrs.eq(rIndex).find("input:radio:eq(0)").attr("checked")=="checked"){
                 	$priorityDeductions=1;
                 }else if($dataTrs.eq(rIndex).find("input:radio:eq(1)").attr("checked")=="checked"){
                 	$priorityDeductions=2;
                 }
                 $tds.eq(cIndex).find("input[name=priorityDeductions]").remove();
                 $tds.eq(cIndex).append($("<input/>").attr("type","hidden").attr("name","priorityDeductions").val($priorityDeductions));
                 
                 rIndex++;
                 // 预收/预报名
                 $tds.eq(cIndex).find('span:first').text($dataTrs.eq(rIndex).find("input:first").val());
                 //预收 隐藏域
                 $tds.eq(cIndex).find("input[name=advNumbers]").remove();
                 $tds.eq(cIndex).append($("<input/>").attr("type","hidden").attr("name","advNumbers").val($dataTrs.eq(rIndex).find("input:first").val()));
                 cIndex++;
                 // 单房差
                 cIndex++;
                 // 需交定金
                 $td = $dataTrs.eq(rIndex).find("td:eq(3)");
                 html = ['<span data-value="', $td.find("select:eq(0)").val(), '">', $td.find("select:eq(0) option:selected").text(), '</span>',
                         '<span class="fbold">', $td.find("input:eq(0)").val(), '</span>'];
                 $tds.eq(cIndex).html(html.join(''));
                 //定金币种 隐藏域
                 $tds.eq(cIndex).find("input[name=frontMoneyCurrencyIds]").remove();
                 $tds.eq(cIndex).append($("<input/>").attr("type","hidden").attr("name","frontMoneyCurrencyIds").val($td.find("select:eq(0)").val()));
                 //定金金额 隐藏域
                 $tds.eq(cIndex).find("input[name=frontMoneys]").remove();
                 $tds.eq(cIndex).append($("<input/>").attr("type","hidden").attr("name","frontMoneys").val($td.find("input:eq(0)").val()));
                 rIndex++;
                 cIndex++;
                 // 备注
                 var ttt = $dataTrs.eq(rIndex).find("textarea");
                 $tds.eq(cIndex).html($dataTrs.eq(rIndex).find("textarea").val()).attr("data-text",$dataTrs.eq(rIndex).find("textarea").val());
                 $tds.eq(cIndex).find("input[name=memos]");
                 $tds.eq(cIndex).append($("<input/>").attr("type","hidden").attr("name","memos").val($dataTrs.eq(rIndex).find("textarea").text()));
                 rIndex++;
                 cIndex++;
             }

             function ticketAmount() {
                 $("#jbox_tq td.hotel_air_price > table").each(function (i) {
                     var $this = $(this);
                     var $trs = $this.children("tbody").children("tr");
                     if ($this.is(":visible")) {
                         if (i == 0) {
                             var kpSum = 0, fkpSum = 0;
                             var $tds = $trs.eq(-2).children("td:not(:first,:last)");
                             $tds.each(function (i) {
                                 var kpNum = $trs.eq(-4).children("td").eq(i + 1).find('input:first').val();
                                 kpNum = parseInt(kpNum) || 0;
                                 var fkpNum = $trs.eq(-3).children("td").eq(i + 1).find('input:first').val();
                                 fkpNum = parseInt(fkpNum) || 0;
                                 $(this).text(kpNum + fkpNum);
                                 kpSum += kpNum;
                                 fkpSum += fkpNum;
                             });
                             $trs.eq(-4).children("td:last").text(kpSum);
                             $trs.eq(-3).children("td:last").text(fkpSum);
                             $trs.eq(-2).children("td:last").text(kpSum + fkpSum);
                         } else {
                             var fkpNum = $trs.eq(-3).children("td").eq(1).find('input:first').val();
                             $trs.eq(-2).children("td:last").text(parseInt(fkpNum) || 0);
                         }
                     }
                 })
             }

             return function (id, isAdd) {
                 isAdd = isAdd || !id;
                 $.jBox(boxHtml, {
                     title: isAdd ? "新增团期信息项" : "修改团期信息项", id: "jbox_tq", buttons: { '保存': 1 }, submit: function (v, h, f) {
                         if (v == 1) {
                             saveData(id, isAdd);
                         }
                     }, height: '560', width: 980
                 });
                 initEvent();
                 if (id) {
                     initData(id);
                 }
             }
         })();
     });
 </script>
 
</body>
</html>
