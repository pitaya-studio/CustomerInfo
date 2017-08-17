<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>海岛游产品修改</title>
	
	<meta name="decorator" content="wholesaler"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.css" />
	
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/js/jquery-migrate-1.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/default.validator.js"  type="text/javascript"></script>
	<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.js"></script>
    <!--树形插件的脚本-->
    <script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.excheck-3.5.js"></script>
    <!--树形插件的脚本-->
    <script type="text/javascript" src="${ctxStatic}/js/jquery.mousewheel.min.js"></script>
    <!--滚动条插件脚本-->
    <script type="text/javascript" src="${ctxStatic}/js/jquery.mCustomScrollbar.js"></script>
    <!--产品模块的脚本-->
    <script type="text/javascript" src="${ctxStatic}/js/tmp.products.js"></script>
    <script type="text/javascript">var $ctx = "${ctx}";</script>
    <script type="text/javascript" src="${ctxStatic}/js/activityIslandProductEdit.js"></script>
 	 
 	<link href="${ctxStatic}/css/j.suggest.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
    	.ipt-tips {
		    position: absolute;
		    top: 5px;
		    white-space: nowrap;
		    color: #B2B2B2;
		    z-index: 1;
		    left: 100px;
	    }
    </style>
    <script src="${ctxStatic}/js/input-comp/j.dimensions.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/input-comp/j.suggest.js" type="text/javascript"></script>
	<script type="text/javascript">
		function toEditSubmit(obj){
			var url="${ctx}/activityIsland/updateacitivityisland";
			$(obj).attr("disabled", "disabled");
			if(!validateNum()){
				return ;
			}else{
				//保存失败时，先删除之前的值
			    $("input[name=allvisafile]").each(function(){
			    	$(this).remove();
			    });
			   $(".up_files_lists:visible").each(function(){
			       var arrc = new Array();
			       $(this).find("input[name=filedetail]").each(function(){
			        arrc.push($(this).val());
			       })
					$("#updateActivityIsland").append("<input type='hidden' name='allvisafile' value='"+arrc.join()+"' />");
				});				
				$.post(url,$("#updateActivityIsland").serialize(),function(data){
					if(data.message=="1"){
						$.jBox.tip("添加成功!");
						setTimeout(function(){window.close();},900);
					    window.opener.location.reload();
					}else if(data.message=="2"){
						$.jBox.tip("修改成功!");
						setTimeout(function(){window.close();},900);
					    //window.opener.location.reload();
					    window.opener.location="${ctx}/activityIsland/islandProductList?status=0&showType=2";
					}else if(data.message=="3"){
						$.jBox.tip(data.error,'warning');
						$("#updateSubmit").attr("disabled", false);
					}else{
						$.jBox.tip('系统异常，请重新操作!','warning');
						$("#updateSubmit").attr("disabled", false);
					}
				});
			}
		}
		function validateNum(){
			var activityName=$("input[name=activityName]").val();
			if(activityName==null||activityName==""){
				$.jBox.tip("产品名称不能为空,请填写产品名称!");
				return false;
			}
			var activitySerNum=$("input[name=activitySerNum]").val();
			if(activitySerNum==null||activitySerNum==""){
				$.jBox.tip("产品编号不能为空,请填写产品编号!");
				return false;
			}
			
			return true;
		}
		 $(function(){
		      	$("#thirdStepDiv .mod_information_d8_2 select[name='country']").comboboxSingle();
		      	//显示联运/分段联运价格
		      	islandShowPrice();
		      	//搜索条件筛选
		      	launch();
		      	//产品名称文本框提示信息
		      	inputTips();
		      	//其它信息字数限制
				//getAcitivityNameLength1(200);
		      	//操作浮框
		      	operateHandler();
	         	 // 删除签证资料
				 $("#ziliaoInfo").on("click", "input.up_load_visa_info_btn_del", function () {
		             // 删除签证资料
		             $("div.up_files_lists").has(this).remove();
		         });
		      });
		       
		      $(function(){
		    	    //initSuggest({});
					initSuggestClass("${ctx}/geography/getAllConListAjax",{});
					
					$(".suggest").each(function(i,o){
						var id = $(this).attr("id");
						var name = $(this).attr("name");
						$(this).parent().append('<input id="'+id.replace("suggest","")+'" type="hidden" name="'+name.replace("suggest","")+'" value="">');
						$(this).parent().append('<span id="Div'+id+'" class="suggest_ac_results ac_results" style="top: 363px; left: 168px; display: none;"></span>');
					});
									
			        $(document).on("click",".suggest",function(){
			        	var id = $(this).attr("id");
						var name = $(this).attr("name");
			        	$(this).suggest(countrys, {
							hot_list : commoncountrys,
							dataContainer : '#'+id.replace("suggest",""),
							//onSelect:activityIslandAjaxSelect('island',$('#country')),
							attachObject : "#Div"+id
						});
			        }).on("mouseover",".suggest",function(){
			        	var id = $(this).attr("id");
						var name = $(this).attr("name");
			        	$(this).suggest(countrys, {
							hot_list : commoncountrys,
							dataContainer : '#'+id.replace("suggest",""),
							//onSelect:activityIslandAjaxSelect('island',$('#country')),
							attachObject : "#Div"+id
						});
			        });
			
				});
	     var idGen = 1;
	 	 function addRecord() {
          var $temp = $("div.up_files_lists:first");
          $temp.parent().append($temp.clone().show());
          var $tempReplace = $("div.up_files_lists:last");
          $tempReplace.find("#suggestvisaCountry").attr("id","suggestvisaCountry"+idGen);
          $tempReplace.find("#visaCountry").attr("id","visaCountry"+idGen);
          $tempReplace.find("#DivsuggestvisaCountry").attr("id","DivsuggestvisaCountry"+idGen);
          idGen++;
          initSuggestClass("${ctx}/geography/getAllConListAjax",{});
          return false;
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
					$(obj).parent().find("#currentFiles").find("[name='docOriName']").each(function(index, obj1) {
						var conditionname = $(obj).attr("name");
						var filename = $(obj1).val();
						var fileid = $(obj1).prev().val();
						if(conditionname=="VisaFile"){
							var filepath = $(obj1).next().val();
							 $(obj).parent().parent().parent().next().append('<li><i class="sq_bz_icon"></i><span>'+ filename +'</span><a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(\''+ fileid +'\',\''+ $(obj).attr("name") +'\',this);">删除</a><input type="hidden" name="filedetail" value="'+ fileid+'#'+ filename+'#'+ filepath+'"/></li>');
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
	</script>
    <script type="text/javascript">
			var tree, $key, lastValue = "",
				nodeList = [];
			$(document).ready(function() {
				var setting = {
					check: {
						enable: true,
						nocheckInherit: true
					},
					view: {
						selectedMulti: false,
						fontCss: function(treeId, treeNode) {
							return (!!treeNode.highlight) ? {
								"font-weight": "bold",
								"color": "#ff0000"
							} : {
								"font-weight": "normal",
								"color": "#333333"
							};
						}
					},
					data: {
						simpleData: {
							enable: true
						}
					},
					callback: {
						beforeClick: beforeClick,
						onCheck: onCheck
					}
				};

				function beforeClick(id, node) {
						tree.checkNode(node, !node.checked, true, true);
						return false;
					}
					// 用户-菜单
				var zNodes = [{
					id: 1,
					pId: 0,
					name: "北京俄风行国际旅行有限公司"
				}, {
					id: 11,
					pId: 1,
					name: "北京分公司"
				}, {
					id: 12,
					pId: 11,
					name: "张三"
				}, {
					id: 13,
					pId: 11,
					name: "李四"
				}, {
					id: 131,
					pId: 11,
					name: "王五"
				}, {
					id: 5,
					pId: 1,
					name: "河北分公司"
				}, {
					id: 51,
					pId: 5,
					name: "王河北"
				}, {
					id: 52,
					pId: 5,
					name: "李小红"
				}, {
					id: 6,
					name: "上海俄风行国际旅行有限公司",
					children: [{
						id: 61,
						name: "小王"
					}, {
						id: 62,
						name: "小李"
					}]
				}];
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
								$hasAdd.each(function(index, element) {
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
						$hasAdd.each(function(index, element) {
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
				$("#addArea").on("click", "li i", function() {
					var $li = $(this).parent("li");
					var treeNode = tree.getNodeByTId($li.attr("forID"));
					tree.checkNode(treeNode, false, true);
					$li.remove();
				});
				//清空已添加的城市
				$(".role-rightTop p").click(function() {
					$.jBox.confirm("确定要清空数据吗？", "提示", function(v, h, f) {
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
				$key.val("").focus(function(e) {
					if ($key.hasClass("empty")) {
						$key.removeClass("empty");
					}
				}).blur(function(e) {
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
			function checkedMassage(obj){
			/* if(obj.value==null || obj.value==''){
         		$.jBox.tip("团号不能为空");
         		return false;
         	} */
				$.ajax({
					type:"post",
					url:"${ctx}/activityIsland/checkedGroup",
					data:{
						"groupCode":obj.value
					},
					success:function(data){
						if(data.message=="true"){
							$.jBox.tip("团号不能为空");
							obj.value="";
							return false;
						}
					}
				});
				
			}
		</script>
</head>		
<body>
		<page:applyDecorator name="activity_island_update" >
			<page:param name="current">online</page:param>
		</page:applyDecorator>
		<form id="updateActivityIsland"  modelAttribute="activityIslandInput" action="${ctx}/activityIsland/updateacitivityisland"  method="post" class="form-horizontal">
          <div class="mod_nav">产品 &gt; 海岛游产品 &gt; 海岛游产品修改</div>
          <input type="hidden" name="uuid" value="${activityIslandInput.uuid}"><!--  -->
          <div class="produceDiv">
               <div class="mod_information mar_top0" id="ofAnchor1">
                          <div class="mod_information mar_top0 " id="secondStepDiv ">
                              <div class="ydbz_tit"> <span class="ydExpand" data-target="#baseInfo"></span>基本信息 </div>
                              <div style="margin-top:8px;" id="baseInfo">
                                  <div class="activitylist_bodyer_right_team_co2 wpr20 pr" style="height:40px;">
                                      <div class="activitylist_team_co3_text">产品名称：</div>
                                      <input type="text" class="inputTxt inputTxtlong" name="activityName" id=activityName flag="istips" value="${activityIslandInput.activityName}"/>
                                      <span class="ipt-tips">命名规则 行程时间-海岛-交通方式</span>
                                  </div>
                                  <div class="kong"></div>
                                  <div class="activitylist_bodyer_right_team_co1">
                                      <div class="activitylist_team_co3_text">产品编号：</div>
                                      <input type="text" name="activitySerNum" class="inputTxt" id="activitySerNum" value="${activityIslandInput.activitySerNum}"/>
                                  </div>
                                  <div class="kong"></div>
                                  <div class="activitylist_bodyer_right_team_co1">
                                      <div class="activitylist_team_co3_text">国家：</div>
                                      <div class="activitylist_team_co3_text_contr" style="width:150px;text-overflow:ellipsis; white-space:nowrap;">
                                      		<trekiz:autoId2Name4Table tableName='sys_geography' sourceColumnName='uuid' srcColumnName='name_cn' value='${activityIslandInput.country}'/>
                                      </div>
                                  </div>
                                  <div class="activitylist_bodyer_right_team_co1">
                                      <div class="activitylist_team_co3_text">岛屿：</div>
                                      <div class="activitylist_team_co3_text_contr" style="width:150px;text-overflow:ellipsis; white-space:nowrap;">
                                      	<input type="hidden" id="islandUuid" name="islandUuid" value="${activityIslandInput.islandUuid}"> 
                                      	<trekiz:autoId2Name4Class classzName="Island" sourceProName="uuid" srcProName="islandName" value="${activityIslandInput.islandUuid }"/>
                                      </div>
                                  </div>
                                  <div class="activitylist_bodyer_right_team_co1">
                                      <div class="activitylist_team_co3_text">酒店名称：</div>
                                      <div class="activitylist_team_co3_text_contr" style="width:150px;text-overflow:ellipsis; white-space:nowrap;">
                                      		<input type="hidden" id="hotelUuid" name="hotelUuid" value="${activityIslandInput.hotelUuid}"> 
                                     		<trekiz:autoId2Name4Class classzName="Hotel" sourceProName="uuid" srcProName="nameCn" value="${activityIslandInput.hotelUuid }"/>
                                      </div>
                                  </div>
                                  <div class="activitylist_bodyer_right_team_co1">
                                      	<div class="activitylist_team_co3_text">币种选择：</div>
										<div class="activitylist_team_co3_text_contr" style="width:150px;text-overflow:ellipsis; white-space:nowrap;">
										<c:forEach items="${currencyList}" var="item">
											<c:if test="${activityIslandInput.currencyId==item.id}">${item.currencyName}</c:if>
										</c:forEach>
										<input type="hidden" value="${activityIslandInput.currencyId }" id="currencyId"/>
										</div>
                                  </div>
                                
                                  <table id="contentTable" class="table activitylist_bodyer_table_new sea_rua_table">
                                      <thead>
                                          <tr>
                                              <th width="8%">团号/日期</th>
                                              <th width="6%">房型*晚数</th>
                                              <th width="5%">基础餐型</th>
                                              <th width="9%">升级餐型&amp;升级价格</th>
                                              <th width="4%">上岛方式</th>
                                              <th width="7%">
						                                                    航班<br /> 起飞到达时间
                                              </th>
                                              <th width="12%">舱位等级&amp;价格&amp;余位</th>
                                              <th width="6%">余位总计</th>
                                              <th width="6%">预收/预报名</th>
                                              <th width="8%">单房差</th>
                                              <th width="8%">需交订金</th>
                                              <th width="4%">状态</th>
                                              <th width="13%">备注</th>
                                              <th width="4%">操作</th>
                                          </tr>
                                      </thead>
                                      <tbody>
										  <c:forEach var="islandGroup" items="${activityIslandInput.activityIslandGroupLists}" varStatus="v">
											  
											  <tr id="${v.count}" data-tag="${v.count}" style="display: table-row;">
	                                               <td rowspan="${islandGroup.baseMealNum}" class="tc">
	                                               <input type="hidden" id="islandGroupUuid" name="islandGroupUuid" value="${islandGroup.uuid }" />
	                                                   <a href="${ctx}/activityIsland/showActivityIslandDetail/${islandGroup.uuid}?type=date" target="_blank">${islandGroup.groupCode}</a> <br />
	                                                   <span><fmt:formatDate value='${islandGroup.groupOpenDate}' pattern='yyyy-MM-dd' type='date'/></span>
	                                               </td>
		                                           <c:choose>
		                                            	<c:when test="${fn:length(islandGroup.activityIslandGroupRoomList)==0}"><!-- 如果房间为空，则有三个单元格 -->
		                                            		<td></td><td></td><td></td>
		                                            	</c:when>
		                                            	<c:otherwise>
				                                            	<c:forEach begin="0" end="0" var="room" items="${islandGroup.activityIslandGroupRoomList}">
				                                               	 	<td rowspan="${fn:length(room.activityIslandGroupMealList)==0?1:fn:length(room.activityIslandGroupMealList)}" class="tc">
					                                               	 	<p>
					                                               	 		<span data-value="${room.hotelRoomUuid}">
																				<trekiz:autoId2Name4Class classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${room.hotelRoomUuid }"/>
																			</span>*<span data-value="${room.nights}"> ${room.nights }</span>晚 <br>
					                                               	 	</p>
				                                               	 	 </td>
				                                               	 	 <c:choose>
				                                               	 	 	<c:when test="${fn:length(room.activityIslandGroupMealList)==0}"><!-- 如果基础餐型为空，则有两个单元格 -->
				                                               	 	 		<td></td><td></td>
				                                               	 	 	</c:when>
				                                               	 	 	<c:otherwise>
				                      										<c:forEach begin="0" end="0" var="mealbase" items="${room.activityIslandGroupMealList}">
								                                                <td class="tc" data-value="${mealbase.hotelMealUuid}">
								                                                	<trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealbase.hotelMealUuid}"/>
								                                                </td>
								                                                <c:choose>
							                                            			<c:when test="${fn:length(mealbase.activityIslandGroupMealRiseList)==0}">
							                                            				<td></td>
							                                            			</c:when>
							                                            			<c:otherwise>
							                                            				<td class="tc">
												                                                <c:forEach var="mealrise" items="${mealbase.activityIslandGroupMealRiseList}">
														                                                <p>
														                                                	<span data-value="${mealrise.hotelMealUuid}">
														                                                		<!--<trekiz:defineDict className="wtext" name="hotelMeals" type="hotel_meal_type" dataScope="system" defaultValue="${mealrise.hotelMealUuid}" /> -->
														                                                		<trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealrise.hotelMealUuid}"/>
														                                                	</span>
														                                                	<span data-value="${mealrise.currencyId}">
																												<c:forEach items="${currencyList}" var="item">
																													<c:if test="${mealrise.currencyId==item.id}">${item.currencyMark}</c:if>
																												</c:forEach>
																											</span>
														                                                	<span><span><fmt:formatNumber  type="currency" pattern="###0.00" value="${mealrise.price}" /></span></span>/人
													                                              	</p>
												                                                </c:forEach>
											                                                </td>
							                                            			</c:otherwise>
							                                            		</c:choose>
								                                                
							                                               </c:forEach>                         	 	 	
				                                               	 	 	</c:otherwise>
				                                               	 	 </c:choose>	 
				                                               	 
				                                             </c:forEach>
		                                            	</c:otherwise>
		                                            </c:choose>
		                                            
		                                             <td rowspan="${islandGroup.baseMealNum}" class="tc">
			                                               	<c:forEach var="islandway" items="${fn:split(islandGroup.islandWay,';')}">
			                                               		<p data-value="${islandway}"><trekiz:autoId2Name4Table tableName="sys_company_dict_view" sourceColumnName="uuid" srcColumnName="label" value="${fn:trim(islandway)}"/></p>	
			                                               	</c:forEach>
		                                             </td>
		                                             <td rowspan="${islandGroup.baseMealNum}" class="tc">
			                                               <span data-value="${islandGroup.activityIslandGroupAirlineList[0].airline},${islandGroup.activityIslandGroupAirlineList[0].flightNumber}">${islandGroup.activityIslandGroupAirlineList[0].flightNumber}</span>
			                                               <br />
		                                               		<span data-start="<fmt:formatDate value='${islandGroup.activityIslandGroupAirlineList[0].departureTime}' pattern='HH:mm' type='date'/>" data-end="<fmt:formatDate value='${islandGroup.activityIslandGroupAirlineList[0].arriveTime}' pattern='HH:mm' type='date'/>" data-days="${islandGroup.activityIslandGroupAirlineList[0].dayNum}" class="lieHt30 fbold">
		                                               		<fmt:formatDate value="${islandGroup.activityIslandGroupAirlineList[0].departureTime}" pattern="HH:mm" type="date"/>
															-<fmt:formatDate value="${islandGroup.activityIslandGroupAirlineList[0].arriveTime}" pattern="HH:mm" type="date"/>
															<c:choose>
																<c:when test="${islandGroup.activityIslandGroupAirlineList[0].dayNum==0}">
																
																</c:when>
																<c:when test="${islandGroup.activityIslandGroupAirlineList[0].dayNum==1}">
																<br/><span class="lianyun_name next_day_icon" >次日</span>
																</c:when>
																<c:otherwise>
																 <br/> <span class="lianyun_name next_day_icon" > ${islandGroup.activityIslandGroupAirlineList[0].dayNum}+</span>
																</c:otherwise>
															</c:choose>
															</span>
		                                               </td>
		                                               <td rowspan="${islandGroup.baseMealNum}" class="tc">
			                                                <c:forEach var="airline" items="${islandGroup.activityIslandGroupAirlineList}" >
			                                               		<div class="cw_thjg_yw" data-ctrl="${airline.controlNum}" data-unctrl="${airline.uncontrolNum}">
			                                               			<span style="display:block">
			                                               			${airline.spaceLevelStr}<br>
			                                                		(<span class="or_color over_handle_cursor" title="控票：${airline.controlNum} 非控票：${airline.uncontrolNum}">
			                                                			余位：${airline.remNumber}
			                                                		</span>)</span>
			                                                		
					                                                	<c:forEach var="groupPrice" items="${islandGroup.activityIslandGroupPriceList}">
					                                                		<c:choose>
					                                                			<c:when test="${airline.uuid==groupPrice.activityIslandGroupAirlineUuid}">
						                                                			<p><span  data-value="${groupPrice.type}">
						                                                			<trekiz:autoId2Name4Table tableName='traveler_type' sourceColumnName='uuid' srcColumnName='name' value='${groupPrice.type}'/>
						                                                			</span>
						                                                			:
						                                                				<span data-value="${groupPrice.currencyId}">
						                                                					<c:forEach items="${currencyList}" var="item">
																								<c:if test="${groupPrice.currencyId==item.id}">${item.currencyMark}</c:if>
																							</c:forEach>
						                                                				</span>
						                                                				<span><fmt:formatNumber  type="currency" pattern="###0.00" value="${groupPrice.price}" /></span>
						                                                			</p>
					                                                			</c:when>
					                                                			
					                                                		</c:choose>
					                                                </c:forEach>
			                                               		</div>
			                                               	</c:forEach>
		                                               </td>
		                                               <td rowspan="${islandGroup.baseMealNum}" class="tc">
		                                               		<span class="tdred over_handle_cursor">${islandGroup.totalRemNum}</span>
		                                               </td>
		                                               <td rowspan="${islandGroup.baseMealNum}" class="tc" data-ctrlpriority="${islandGroup.priorityDeduction}">
		                                               		<span class="over_handle_cursor">${islandGroup.advNumber}/${islandGroup.bookingNum==null?0:islandGroup.bookingNum}</span>
		                                               </td>
		                                               <td rowspan="${islandGroup.baseMealNum}" class="tr">
			                                               	<span data-value="${islandGroup.currencyId}">
																<c:forEach items="${currencyList}" var="item">
																	<c:if test="${islandGroup.currencyId==item.id}">${item.currencyMark}</c:if>
																</c:forEach>
															</span>
															<span><fmt:formatNumber  type="currency" pattern="###0.00" value="${islandGroup.singlePrice}" /></span>
															<span data-value="${islandGroup.singlePriceUnit}">
																<c:choose>
																	<c:when test="${islandGroup.singlePriceUnit==1}">
																	/人
																	</c:when>
																	<c:when test="${islandGroup.singlePriceUnit==2}">
																	/间
																	</c:when>
																	<c:when test="${islandGroup.singlePriceUnit==3}">
																	/晚
																	</c:when>
																</c:choose>
															</span>
		                                               </td>
		                                               <td rowspan="${islandGroup.baseMealNum}" class="tr tdgreen">
			                                               	<span data-value="${islandGroup.frontMoneyCurrencyId}">
																 <c:forEach items="${currencyList}" var="item">
																	<c:if test="${islandGroup.frontMoneyCurrencyId==item.id}">${item.currencyMark}</c:if>
																</c:forEach>
															</span>
			                                               	<span class="fbold"><fmt:formatNumber  type="currency" pattern="###0.00" value="${islandGroup.frontMoney}" /></span>
		                                               </td>
		                                               <td rowspan="${islandGroup.baseMealNum}" class="tc">
			                                               	<span class="tdred">
			                                               		<c:choose>
			                                               			<c:when test="${islandGroup.status==1}">上架
			                                               			</c:when>
			                                               			<c:when test="${islandGroup.status==2}">下架
			                                               			</c:when>
			                                               			<c:when test="${islandGroup.status==3}">草稿
			                                               			</c:when>
			                                               			<c:when test="${islandGroup.status==4}">已删除
			                                               			</c:when>
			                                               			<c:otherwise>
			                                               				其他状态
			                                               			</c:otherwise>
			                                               		</c:choose>
			                                               	</span>
		                                               </td>
		                                               <td rowspan="${islandGroup.baseMealNum}" class="tl">${islandGroup.memo}</td>
		                                               <td rowspan="${islandGroup.baseMealNum}" class="p0">
		                                                   <dl class="handle">
		                                                       <dt> <img title="操作" src="${ctxStatic}/images/handle_cz.png" /> </dt>
		                                                       <dd class="">
		                                                           <p>
		                                                               <span></span>
		                                                               <a class="updateLink" href="javascript:void(0)">修改</a>
		                                                               <!-- 
		                                                               <a href="${ctx}/activityIsland/showActivityIslandDetail/${activityIslandInput.uuid}?type=product" target="_blank">详情</a>
		                                                           	 -->
		                                                           </p>
		                                                       </dd>
		                                                   </dl>
		                                               </td>
		                                           </tr>
		                                           
		                                           <c:forEach begin="0" var="room" items="${islandGroup.activityIslandGroupRoomList}" varStatus="status">
		                                              <c:choose>
		                                               	 	<c:when test="${status.index==0}">
		                                               	 	    <c:forEach begin="1" var="mealbase" items="${room.activityIslandGroupMealList}">
		                                               	 	    	<tr data-tag="${v.count}">
							                                               <td class="tc" data-value="${mealbase.hotelMealUuid}">
							                                                	<trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealbase.hotelMealUuid}"/>
							                                                </td>
							                                                <td class="tc">
								                                                <c:forEach var="mealrise" items="${mealbase.activityIslandGroupMealRiseList}">
										                                                <p>
										                                                	<span data-value="${mealrise.hotelMealUuid}">
										                                                		<trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealrise.hotelMealUuid}"/>
										                                                	</span>
										                                                	<span data-value="${mealrise.currencyId}">
																								<c:forEach items="${currencyList}" var="item">
																									<c:if test="${mealrise.currencyId==item.id}">${item.currencyMark}</c:if>
																								</c:forEach>
																							</span>
										                                                	<span><fmt:formatNumber  type="currency" pattern="###0.00" value="${mealrise.price}" /></span>/人
									                                                	</p>
								                                               </c:forEach>
								                                             </td>
							                                            </tr> 
						                                          </c:forEach>
		                                               	 	</c:when>
		                                           	
		                                               	 	<c:otherwise>
	                                               	 				<c:forEach var="mealbase" items="${room.activityIslandGroupMealList}" varStatus="sss">
											                                <c:choose>
	                                               	 							<c:when test="${sss.index==0 }">
	                                               	 								<tr data-tag="${v.count}">
													                                 	 <td rowspan="${fn:length(room.activityIslandGroupMealList)==0?1:fn:length(room.activityIslandGroupMealList)}" class="tc">
													                                  	 		<p>
													                                  	 			<span data-value="${room.hotelRoomUuid}">
																										<trekiz:autoId2Name4Class classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${room.hotelRoomUuid }"/>
																									</span>*<span data-value="${room.nights}"> ${room.nights }</span>晚
															                                  </p>
															                              </td>
														                                   <td class="tc" data-value="${mealbase.hotelMealUuid}">
														                                   		<trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealbase.hotelMealUuid}"/>
														                                   </td>
														                                   <td class="tc">
															                                    <c:forEach var="mealrise" items="${mealbase.activityIslandGroupMealRiseList}">
															                                      <p>
															                                      	<span data-value="${mealrise.hotelMealUuid}">
															                                      		<trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealrise.hotelMealUuid}"/>
															                                      	</span>
															                                      	<span data-value="${mealrise.currencyId}">
																												<c:forEach items="${currencyList}" var="item">
																													<c:if test="${mealrise.currencyId==item.id}">${item.currencyMark}</c:if>
																												</c:forEach>
																									</span>
															                                      	<span><fmt:formatNumber  type="currency" pattern="###0.00" value="${mealrise.price}" /></span>/人
															                                     	</p>
															                                    </c:forEach>
													                                   		</td>
													                                   	</tr>	
												                                   </c:when>
												                                   <c:otherwise>
												                                   		<tr data-tag="${v.count}">
												                                               <td class="tc" data-value="${mealbase.hotelMealUuid}">
												                                                	<trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealbase.hotelMealUuid}"/>
												                                                </td>
												                                                <td class="tc">
													                                                <c:forEach var="mealrise" items="${mealbase.activityIslandGroupMealRiseList}">
															                                                <p>
															                                                	<span data-value="${mealrise.hotelMealUuid}">
															                                                		<trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealrise.hotelMealUuid}"/>
															                                                	</span>
															                                                	<span data-value="${mealrise.currencyId}">
																													<c:forEach items="${currencyList}" var="item">
																														<c:if test="${mealrise.currencyId==item.id}">${item.currencyMark}</c:if>
																													</c:forEach>
																												</span>
															                                                	<span><fmt:formatNumber  type="currency" pattern="###0.00" value="${mealrise.price}" /></span>/人
														                                                	</p>
													                                                </c:forEach>
												                                               </td>
												                                          </tr>      
					                                               	 				</c:otherwise>
		                                               	 					</c:choose>
										                                 </c:forEach>    
		                                               	 	</c:otherwise>
		                                               	</c:choose>
		                                             </c:forEach>
										  </c:forEach>
                                      </tbody>
                                  </table>
                              </div>
                          </div>
                     </div>
                        <!--产品信息结束-->
                        <!--上传资料开始-->
                        <div id="thirdStepDiv">
                            <!-- 上传文件 -->
                            <div class="ydbz_tit"> <span class="ydExpand" data-target="#ziliaoInfo"></span>上传资料 </div>
                            <div class="mod_information_3 upload_file_new" id="ziliaoInfo">
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
                  <div class="batch" style="margin-top:10px;">
				    <label class="batch-label">签证资料：</label>
				     <img name=""  class="up_load_visa_info_btn" src="${ctxStatic }/images/add_11.jpg" onclick="addRecord()"/>
				      <div class="up_files_lists" style="display:none;">
                           <div class="up_load_visa_info">
                           <div class="activitylist_bodyer_right_team_co1" style="position: static;">
                                <label>国家：</label>
                               <input id="suggestvisaCountry" type="text" autocomplete="off" name="suggestvisaCountry" class="suggest" style="width: 120px; color: rgb(0, 0, 0);" value="" />
                            </div>
                           <div class="activitylist_bodyer_right_team_co1">
                               <div class="activitylist_team_co3_text">签证类型：</div>
                               <select id="visaType" name="visaType">
                                    <option value="" selected="selected">请选择</option>
	                               	<c:forEach items="${newVisaTypeList }" var="item">
									<option value="${item.id}" >${item.label}</option>
								    </c:forEach>
							    </select>		
                           </div>
                           <span class="padr10"></span>
                           <div class="activitylist_bodyer_right_team_co1">
                            <p class="maintain_pfull new_kfang">
                               <input type="button" name="VisaFile" value="上传文件" class="mod_infoinformation3_file maring_bottom10" onClick="uploadFiles('${ctx}',null,this)"/>
                               <input type="button" class="mod_infoinformation3_file maring_bottom10 up_load_visa_info_btn_del" value="删除" />
                            </p>
                           	   <ol class="batch-ol"></ol>
                           	</div> 
	                       </div>
	                       <ol class="batch-ol">
	                       </ol>
		              </div>
			        <c:forEach items="${activityIsland.activityIslandVisaFile}" var="visafile" varStatus="vs">
		              <div class="up_files_lists" style="display:block;">
		                           <div class="up_load_visa_info">
		                           <div class="activitylist_bodyer_right_team_co1" style="position: static;">
		                                <label>国家：</label>
		                               <input id="suggestvisaCountry11${vs.count}" type="text" autocomplete="off"  name="suggestvisaCountry" class="suggest" style="width: 120px; color: rgb(0, 0, 0);"
		                                value="<trekiz:autoId2Name4Table	tableName="sys_geography" sourceColumnName="uuid" srcColumnName="name_cn" value="${visafile.country}" />" />
		                            </div>
		                           <div class="activitylist_bodyer_right_team_co1">
		                               <div class="activitylist_team_co3_text">签证类型：</div>
		                               <select id="visaType" name="visaType">
                                    <option value="" selected="selected">请选择</option>
	                               	<c:forEach items="${newVisaTypeList }" var="item">
									<option value="${item.id}" <c:if test="${visafile.visaTypeId==item.id}">selected="selected"</c:if>>${item.label}</option>
								    </c:forEach>
							    </select>		
		                           </div>
		                           <span class="padr10"></span>
		                           <div class="activitylist_bodyer_right_team_co1">
		                            <p class="maintain_pfull new_kfang">
		                               <input type="button" name="VisaFile" value="上传文件" class="mod_infoinformation3_file maring_bottom10" onClick="uploadFiles('${ctx}',null,this)"/>
		                               <input type="button" class="mod_infoinformation3_file maring_bottom10 up_load_visa_info_btn_del" value="删除" />
		                            </p>
		                           	   <ol class="batch-ol"></ol>
		                           	</div> 
		                       </div>
		                       <ol class="batch-ol">
		                        <c:forEach items="${visafile.hotelAnnexList}" var="annexfile">
							      <li> <i class="sq_bz_icon"></i> <a>${annexfile.docName}</a> 
							            <input type="hidden" name="VisaFile" value="${file.docId}"/>
										<input type="hidden" name="docOriName" value="${file.docName}"/>
										<input type="hidden" name="docPath" value="${file.docPath}"/>
							            <a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(null,'hotelAnnexDocId',this)">删除</a>
							      </li>
							    </c:forEach>
		                       </ol>
		                   </div>
		            </c:forEach> 
			      </div>
                </div>
                            <div class="ydbz_tit"> <span class="ydExpand" data-target="#otherInfo"></span>其他信息 </div>
                            <div class="other_info" id="otherInfo">
                                <textarea class="add_new_seatu_qt" onkeyup="getAcitivityNameLength1(200)" id="memo" name="memo">${activityIslandInput.memo}</textarea>
                                <br />
                                <span style="color:#b2b2b2" class="acitivityNameSizeSpan">还可输入<span class="acitivityNameSize">200</span>个字</span>
                            </div>
                            <div class="ydbz_tit island_productor_upload001 maring_top10"> <span class="ydExpand" data-target="#productor_sel"></span>产品分享 </div>
                            <!-- <div class="product_share" id="productInfo">
                                <div class="coverarea">
                                    <label class="coverarea-label">交叉栏目：</label>
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
                                                <span> 已添加 <em>0</em>个城市 </span>
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
                                <!-- <input type="button" value="取消" onclick="javascript:window.location.href='/trekiz_wholesaler/a/activity/manager/list/2'"
                                       class="btn btn-primary gray" /> -->
                                <input type="button" value="取消" onclick="window.close();"
                                       class="btn btn-primary gray" />
                                <input id="updateSubmit" type="button" value="保存修改" class="btn btn-primary"  onclick="toEditSubmit(this)"/>
                            </div>
                        </div>
                        <!--第三步上传资料结束-->
                    </div>
		</form>
		
		
<!--新增团期信息项弹出层开始-->
	<div id="jbox_haidaoyou_fab">
		<div class="add_product_info new_hotel_p_table">
			<table class="table_product_info " style="width:900px !important; ">
				<tr>
					<td class="tr" style="width:121px !important;">团号</td>
					<td width="710" colspan="3"><input type="hidden"
						id="islandGroupUuid" name="islandGroupUuid" /> <input
						readonly="readonly" type="text" name="NO"
						class="inputTxt w106 spread" id="igroup" /></td>
				</tr>
				<tr>
					<td class="tr">日期</td>
					<td colspan="3"><input type="text" onclick="WdatePicker()"
						id="tdate" class="dateinput w106 required " readonly="readonly" />
					</td>
				</tr>
				<tr class="houseTypeTr">
					<td class="tr">房型*晚数</td>
					<td colspan="3">
						<p class="houseType">
							<span> <select class="w80">

							</select> <span class="w50_30">*</span> <input type="text"
								data-type="number" data-min="1" class="inputTxt w50_30" /> <span
								class="w50_30">晚</span> <a class="ydbz_x  addHouseType">新增</a> </span>
						</p></td>
				</tr>
				<tr class="houseTypeTr mealTypeTr">
					<td class="tr">基础餐型</td>
					<td colspan="3"><select class="w80">

					</select> <a class="ydbz_x addMealType">新增</a> <span class="padr10"></span>
						<span><input class="redio_martop_4" type="checkbox" />升级餐型</span>
					</td>
					<td class="tr new_hotel_p_table2_tdblue" style="display:none;">
						<span>升级餐型</span></td>
					<td style="display:none;">
						<p class="upMealType">
							<span> <select name="select10" class="w80 mr3">

							</select> <select name="select10" class="w50_30 mr3 currency">
									<c:forEach items="${currencyList}" var="item">
										<option value="${item.id}">${item.currencyMark}</option>
									</c:forEach>
							</select> <input type="text" data-type="float" class="inputTxt w50_30" />
								<a class="ydbz_x addUpMealType">新增</a> </span>
						</p></td>
				</tr>
				<tr class="islandTr">
					<td class="tr">上岛方式</td>
					<td width="350">没有选择酒店，无法显示上岛方式.</td>
					<td width="130" class="tr new_hotel_p_table2_tdblue">单房差</td>
					<td width="350"><span class="add_jbox_repeat_thj"> <select
							class="w50_30 currency">
								<c:forEach items="${currencyList}" var="item">
									<option value="${item.id}">${item.currencyMark}</option>
								</c:forEach>
						</select> <input type="text" data-type="float"
							class="inputTxt w50_30 babyPrice mr25" /> <select
							class="w50_30 currency">
								<option value="1">/人</option>
								<option value="2">/间</option>
								<option value="3">/晚</option>
						</select> </span></td>
				</tr>
				<tr>
					<td class="tr">航空公司</td>
					<td><select class="w125_sel_pop_addnewtimt selAirline"
						id="tairlinenumb">
							<option>请选择</option>

					</select></td>
					<td class="tr new_hotel_p_table2_tdblue">航班号</td>
					<td><select class="w125_sel_pop_addnewtimt fltNo"></select>
					</td>
				</tr>
				<tr>
					<td class="tr new_hotel_p_table2_tdblue">起飞时间</td>
					<td class="tl"><input type="text"
						onclick="WdatePicker({ dateFmt: 'HH:mm' })"
						class="dateinput w106 required startTime" readonly="readonly" />
					</td>
					<td class="tr new_hotel_p_table2_tdblue">到达时间</td>
					<td><input type="text"
						onclick="WdatePicker({ dateFmt: 'HH:mm' })"
						class="dateinput w106 required endTime" readonly="readonly" /> +
						<input type="text" data-type="number" class="inputTxt w50_30 days"
						value="0" data-min="0" /> 天</td>
				</tr>
				<tr>
					<td colspan="4" class="up_load_visa_info_td01 hotel_air_price">
						<table class="new_hotel_p_table2"
							style="width:900px !important; display:none;">
							<thead>
								<tr>
									<th style="-webkit-width:112px;-moz-width:110px;"
										class="tr new_hotel_p_table2_tdblue2 new_hotel_p_table2_tdblue2_mr20">舱位等级</th>
									<th width="710">无</th>
									<th width="31">合计</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td></td>
									<td></td>
									<td></td>
								</tr>
							</tbody>
						</table>
						<table class="new_hotel_p_table2" style="width:900px !important;">
							<tbody>
								<c:forEach items="${travelerTypes}" var="travelerType">
									<tr>
										<td width="103" class="tr nnew_hotel_p_table2_tdblue"
											data-text="${travelerType.name}"
											data-value="${travelerType.uuid}">
											${travelerType.name}同行<br />（机+酒）价/人</td>
										<td width="776" style="text-align:left !important; "><span
											class="add_jbox_repeat_thj2"> <select
												class="w50_30 currency" name="peoplePrice">
													<c:forEach items="${currencyList}" var="currency">
														<option value="${currency.id}">
															${currency.currencyMark}</option>
													</c:forEach>
											</select> <input type="text" data-type="float"
												class="inputTxt w50_30 babyPrice mr25" /> </span></td>
										<td style="display:none"></td>
									</tr>
								</c:forEach>
								<tr>
									<td class="tr new_hotel_p_table2_tdblue">控票数</td>
									<td class="tc" style="text-align:left !important;"><input
										type="text" data-type="number"
										class="inputTxt w50_30 spread fl " value="0"
										readonly="readonly" /></td>
									<td style="display:none"></td>
								</tr>
								<tr>
									<td class="tr new_hotel_p_table2_tdblue">非控票数</td>
									<td class="tl" style="text-align:left !important; "><input
										type="text" data-type="number" data-min="0"
										class="inputTxt w50_30 spread fkpNum" />
									</td>
									<td style="display:none"></td>
								</tr>
								<tr>
									<td class="tr new_hotel_p_table2_tdblue">票数总计</td>
									<td style="text-align:left !important; "></td>
									<td style="display:none"></td>
								</tr>
								<tr>
									<td class="tr new_hotel_p_table2_tdblue">余位</td>
									<td style="text-align:left !important; "></td>
									<td style="display:none"></td>
								</tr>
							</tbody>
						</table></td>
				</tr>
				<tr>
					<td class="tr">优先扣减</td>
					<td colspan="3"><span class="mar_left_35"> <input
							class="redio_martop_4" type="radio" name="kp_radio" id="kp_radio"
							checked="checked" /> <label for="kp_radio">控票数</label> </span> <span
						class="mar_left_35"> <input class="redio_martop_4"
							type="radio" name="kp_radio" id="fkp_radio" /> <label
							for="fkp_radio">非控票数</label> </span></td>
				</tr>
				<tr>
					<td class="tr">预收</td>
					<td><input type="text" data-type="number" data-min="0"
						class="inputTxt w50_30 spread" />人</td>
					<td class="tr new_hotel_p_table2_tdblue">需交订金</td>
					<td><select name="select13" class="w50_30">
							<c:forEach items="${currencyList}" var="item">
								<option value="${item.id}">${item.currencyMark}</option>
							</c:forEach>
					</select> <input type="text" data-type="float"
						class="inputTxt w50_30 spread" /></td>
				</tr>
				<tr>
					<td class="tr valign_top">备注</td>
					<td colspan="3"><textarea class="inputTxt spread"
							style=" width:90%; height:100px;"></textarea>
					</td>
				</tr>
			</table>
		</div>
	</div>
<!--新增团期信息项弹出层结束-->
    <script type="text/javascript">var $ctx = "${ctx}";</script>
		<script type="text/javascript">
			$(document).ready(function() {
				$("#contentTable").on('click', 'a.updateLink', function() {
					var id = $("#contentTable tbody tr").has(this).prop('id');
					var v_currency = $("#v_currency").val();
					var data = readRow(id);
					var options = {
					    isAdd: false,
					    callback: writeRow,
					    id: id,
					    uuid: data.uuid,
					    data:data,
					    tag:"editActivityIslandProduct",
					    hotelUuid:$("#hotelUuid").val(),
					    islandUuid:$("#islandUuid").val(),
					    v_currency:v_currency
				     };
					addGroup_box(options);
				});
				$("#up_files").on("click", "i.del_fj_icon", function() {
					$("ul.up_files_outer li").has(this).remove();
				});
				//读取页面现有数据，弹出层展示
				function readRow(id) {
				    var $row = $("#" + id);
				    if (!$row.length) return;
				    var data = {};
					
				    var $tds = $row.find("td");
				    var cIndex = 0;
				    var $td;
				    // 团号
				    data.uuid = $tds.eq(cIndex).find('#islandGroupUuid').val();
				    data.no = $tds.eq(cIndex).find('a').text();
				    // 日期
				    data.date = $tds.eq(cIndex).find('span').text();
				    cIndex++;
				    // 房型 * 晚数
				    data.houseTypes = [];
				    var $tr = $row;
				    for (var i = 0; $tr.is("tr[data-tag='" + id + "']") ; i++) {
				        $td = i ? $tr.find("td:first") : $tds.eq(cIndex);
				        var obj = {
				            houseType: { value: $td.find("span:eq(0)").attr("data-value"),text:  $td.find("span:eq(0)").Text},
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
	                    data.trafficWays.push({text:$(this).text(), value:$(this).attr("data-value")});
	                });
				    cIndex++;
				    // 航空公司 航班号
				    var arr = ($tds.eq(cIndex).find('span:eq(0)').attr("data-value") || '').split(',');
				    data.airline = {
				        value: arr[0],
				        flight: { value: arr[1] }
				    };
				    // 起飞时间
				    data.airline.flight.start = $tds.eq(cIndex).find('span:eq(1)').attr("data-start");
				    // 到达时间
				    data.airline.flight.end = $tds.eq(cIndex).find('span:eq(1)').attr("data-end");
				    // 到达天数
				    data.airline.flight.days = ($tds.eq(cIndex).find('span:eq(1)').attr("data-days") || 0);
				    cIndex++;
				    // 舱位等级&价格&余位
				    data.airline.prices = [];
				    data.airline.ctrlTickets = [];
				    data.airline.unCtrlTickets = [];
				    data.airline.usedTickets = [];
				    $tds.eq(cIndex).find("div").each(function (i) {
				        var $this = $(this);
				        data.airline.ctrlTickets.push($this.attr("data-ctrl"));
				        data.airline.unCtrlTickets.push($this.attr("data-unCtrl"));
				        data.airline.usedTickets.push($this.attr("data-used"));
				        $this.find("p").each(function (j) {
				           if (i == 0) {
				                data.airline.prices.push({ islandprice: [] });
				            }
				            data.airline.prices[j].islandprice.push({
				                t_uuid: $(this).find("span:eq(0)").attr("data-value"),
				                currency: {
				                    value: $(this).find("span:eq(1)").attr("data-value"),
				                },
				                price: $(this).find("span:eq(2)").text()
				            });
				        });
				    });
				    cIndex++;
				    // 余位/票数总计
				    cIndex++;
				    // 优先扣减
				    data.ctrlTicketPriority = $tds.eq(cIndex).attr("data-ctrlPriority") == "true";
				    // 预收/预报名
				    data.predictCount = $tds.eq(cIndex).find('span:eq(0)').text();
				    cIndex++;
				    // 单房差
				    data.priceDiff = {
				        currency: { value: $tds.eq(cIndex).find("span:eq(0)").attr("data-value") },
				        price: $tds.eq(cIndex).find("span:eq(1)").text(),
				        unit: { value: $tds.eq(cIndex).find("span:eq(2)").attr("data-value") }
				    };
				    cIndex++;
				    // 需交订金
				    data.deposit = {
				        currency: { value: $tds.eq(cIndex).find('span:eq(0)').attr("data-value") },
				        price: $tds.eq(cIndex).find('span:eq(1)').text()
				    };
				    cIndex++;
				    // 状态
				    cIndex++;
				    // 备注
				    data.comment = $tds.eq(cIndex).text();
				    return data;
				}
				//团期内容修改以后，将数据回写到页面中
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
				    // 团期
				    $tds.eq(cIndex).find('a').text(data.no);
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
				    //add by wangxk 20150703 
			        $.each(data.trafficWays, function () {
                    html.push('<p data-value="',this.value,'">', this.text, '</p>');
                });
				    $tds.eq(cIndex).html(html.join(''));
				    cIndex++;
				    // 航空公司
				    html = [];
				    if (data.airline.flight.value) {
				        // 航班号
				        html.push('<span data-value="', data.airline.value, ',', data.airline.flight.value, '">', data.airline.flight.text, '</span><br>');
				        // 航班
				        html.push('<span data-start="', data.airline.flight.start, '" data-end="', data.airline.flight.end, '" data-days="', data.airline.flight.days, '" class="lieHt30 fbold">', data.airline.flight.start, '-', data.airline.flight.end, '</span>');
				        if (data.airline.flight.days > 0) {
				            html.push('<span class="lianyun_name next_day_icon">', data.airline.flight.days == 1 ? '次日' : ('+' + data.airline.flight.days), '</span>');
				        }
				    }
				    $tds.eq(cIndex).html(html.join(''));
				    cIndex++;
				    // 舱位等级&价格&余位
				    $tds.eq(cIndex).empty();
				    $.each(data.airline.prices, function (i) {
				        var islandprice = data.airline.prices[i].islandprice;
				        $.each(islandprice, function (j) {
				            html = [];
				            if (i == 0) {
				                $tds.eq(cIndex).append('<div class="cw_thjg_yw" data-ctrl="' + data.airline.ctrlTickets[j] + '" data-unCtrl="' + data.airline.unCtrlTickets[j] + '"></div>');
				                // 舱位等级
				                if (data.airline.spaceGrade[j]) {
				                    html.push('<span style="display:block" data-value="',data.airline.spaceGrade[j].value,'">', data.airline.spaceGrade[j].text, '(<span class="or_color over_handle_cursor" title="控票：', data.airline.ctrlTickets[j], ' 非控票：', data.airline.unCtrlTickets[j], '">余位：', data.airline.remainTickets[j], '</span>)</span>');
				                }
				            }
				            var $div = $tds.eq(cIndex).find("div").eq(j);
				            html.push('<p><span data-value="',data.airline.touristType[i].value,'">', data.airline.touristType[i].text, '</span>：<span data-value="', this.currency.value, '">', this.currency.text, '</span><span>', this.price, '</span></p>');
				            $div.append(html.join(''));
				        });
				    });
				    cIndex++;
				    //余位/票数总计
				    html = ['<span class="tdred over_handle_cursor">', data.airline.amount.remainTicket, '</span>/<span>', data.airline.amount.ticket, '</span>'];
				    $tds.eq(cIndex).html(html.join(''));
				    cIndex++;
				    // 优先扣减
				    $tds.eq(cIndex).attr("data-ctrlPriority", data.ctrlTicketPriority);
				    // 预收/预报名
				    $tds.eq(cIndex).find('span:first').text(data.predictCount);
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
				    // 状态
				    cIndex++;
				    // 备注
				    $tds.eq(cIndex).html(data.comment);
				    $tds.eq(cIndex).append($("<input/>").attr("type","hidden").val(JSON.stringify(data)).attr("name","jsoninput"));
				    return true;
				}
			});
		</script>
</body>
	
</html>