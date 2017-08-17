<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>海岛游产品发布</title>
    <meta name="decorator" content="wholesaler"/>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.css" />
    <!--树形插件的样式-->
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.mCustomScrollbar.css" />
    <!--滚动条插件样式-->
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
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
    <script type="text/javascript">
    	//全局变量
    	var $ctx = "${ctx}";
		var airlines = jQuery.parseJSON('${airlines}');
		var houseTypes = {};
    	
        $(function () {
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
            
            //初始化国家控件
       	 	initSuggest({});
       	 	getAjaxSelect('island',$('#country'));
       	 	
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
					attachObject : "#Div"+id
				});
	        }).on("mouseover",".suggest",function(){
	        	var id = $(this).attr("id");
				var name = $(this).attr("name");
	        	$(this).suggest(countrys, {
					hot_list : commoncountrys,
					dataContainer : '#'+id.replace("suggest",""),
					attachObject : "#Div"+id
				});
	        });
        });
        //删除已上传的文件
        function deleteFile(thisDom, fileID) {
            $(thisDom).parent("li").remove();
        }
        
        //查看价单
		function showHotelPl() {
			var countryVal = $('#country').val();
			var hotelVal = $('#hotelUuid').val();
			var islandVal = $('#islandUuid').val();
			var aurl = "${ctx }/hotelPl/list?country="+countryVal+"&hotelUuid="+hotelVal+"&islandUuid="+islandVal;
			window.open(aurl);
		}
		
		//级联查询
		function getAjaxSelect(type,obj){
			$.ajax({
				type: "POST",
			   	url: $ctx + "/hotelControl/ajaxCheck",
			   	async: false,
			   	data: {
						"type":type,
						"uuid":$(obj).val()
					  },
				dataType: "json",
			   	success: function(data){
			   		if(type == "island"){
			   			$("#islandUuid").empty();
			   			$("#islandUuid").append("<option value=''>不限</option>");
			   			
			   			$("#hotelUuid").empty();
				   		$("#hotelUuid").append("<option value=''>不限</option>");
			   		} else if(type == "hotel"){
			   			$("#hotelUuid").empty();
				   		$("#hotelUuid").append("<option value=''>不限</option>");
			   		} else if(type == "hotelRoom") {
			   			//初始化新增团期的游客类型同行价显示
			   			/* initTravelerType(); */
			   		} else if(type == "islandway") {
			   			$("#hotelUuid").empty();
				   		$("#hotelUuid").append("<option value=''>不限</option>");
			   		} else {
				   		$("#"+type).empty();
				   		$("#"+type).append("<option value=''></option>");
			   		}
			   		if(data){
			   			if(type == "islandway"){ 
				   			$.each(data.hotelList,function(i,n){
				   			    $("#hotelUuid").append($("<option/>").text(n.nameCn).attr("value",n.uuid));
				   			});
				   			
				    	   	var islandTd = $("#jbox_haidaoyou_fab tr.islandTr").find("td:eq(1)");
				    	   	islandTd.text('');
				   			data.listIslandWay && $.each(data.listIslandWay, function(i,item){
		    	   				islandTd.append($("<input/>").addClass("redio_martop_4").attr("type","checkbox").attr("data-text",item.label).val(item.uuid));
		    	    	   		islandTd.append(item.label);
		    	    	   		islandTd.append($("<span/>").addClass("mr25"));
				   			});
				   			
				   			$("#islandUuid").attr("title",$("#islandUuid").find("option:selected").text());
			   			}else if(type=="island"){
			   				$.each(data.islandList,function(i,n){
			   					 $("#islandUuid").append($("<option/>").text(n.islandName).attr("value",n.uuid));
			   				});
			   				
				   			$("#islandUuid").attr("title",$("#islandUuid").find("option:selected").text());
			   			} else if(type == "hotelRoom") {
				   			var hotelrankCount = data.hotelrank;
			   				var ranks = '';
			   				if(!isNaN(hotelrankCount)){
								for(var i=0;i<hotelrankCount;i++){
				   					ranks+="★";
				   				}        	   				
			   				}
			   				$(".y_xing").text(ranks);
			   				
			   				houseTypes = jQuery.parseJSON(data.houseTypesJsonData);
			   				
			   				$("#hotelUuid").attr("title",$("#hotelUuid").find("option:selected").text());
			   			}
			   		}
			   	}
			});
		}
		
		 var idGen = 1;
		 function addRecord() {
	         var $temp = $("div.up_files_lists:first");
	         $temp.parent().append($temp.clone().show());
	         var $tempReplace = $("div.up_files_lists:last");
	         $tempReplace.find("#suggestvisaCountry").attr("id","suggestvisaCountry"+idGen);
	         $tempReplace.find("#visaCountry").attr("id","visaCountry"+idGen);
	         $tempReplace.find("#DivsuggestvisaCountry").attr("id","DivsuggestvisaCountry"+idGen);
	         idGen++;
	         initSuggest("${ctx}/geography/getAllConListAjax",{});
	         return false;
	     }
		
		//表单提交
	    function formValidate(obj) {
	     	var validate = true;
	 		var groupcodeTextArr = $("#contentTable").find("a[name=groupcodeText]");
	 		for(var i=1; i<groupcodeTextArr.length; i++) {
	    		$.ajax({
					type:"post",
					url:"${ctx}/activityIsland/checkedGroup",
					async:false,
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
	 				toSaveSubmit($(obj));
	 			}
	 		}
	     }
    
    	function toSaveSubmit(obj){
    		//add 校验
    		if(!validateNum()){
				return ;
			}
			//保存失败时，先删除之前的值
		    $("input[name=allvisafile]").each(function(){
		    	$(this).remove();
		    });
		    $(".up_files_lists:visible").each(function(){
		       var arrc = new Array();
		       $(this).find("input[name=filedetail]").each(function(){
		       		arrc.push($(this).val());
		       });
				$("#saveActivityIslandForm").append("<input type='hidden' name='allvisafile' value='"+arrc.join()+"' />");
			});
			//add
			var status = $(obj).attr("data-value");
			var url="${ctx}/activityIsland/saveActivityIsland?status="+status;
			$(obj).attr("disabled", "disabled");
			$.post(
				url,
				$("#saveActivityIslandForm").serialize(),
				function(data){
					if(data.message=="1"){
						$.jBox.tip("添加成功!");
						setTimeout(function(){window.close();},900);
					    window.opener.location.reload();
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
		
		function validateNum(){
			var activityName=$("input[name=activityName]").val();
			if(activityName==null||activityName==""){
				$.jBox.tip("产品名称不能为空,请填写产品名称!");
				return false;
			}
			var activitySerNum=$("input[name=activitySerNum]").val();
			var status=true;
			if(activitySerNum==null||activitySerNum==""){
				$.jBox.tip("产品编号不能为空,请填写产品编号!");
				return false;
			}else {
				$.ajax({
					type:"POST",
					dataType:"json",
					url:"${ctx}/activityIsland/checkedSerNum?activitySerNum="+activitySerNum,
					async:false,
					success:function(data){
						if(data.message=="true"){
							status=false;
							$.jBox.tip("产品编号已存在!");
						}
					}
				});
				return status;
			} 
			var country=$("input[name=country]").val();
			if(country==null||country==""){
				$.jBox.tip("国家名称不能为空,请填写国家名称!");
				return false;
			}
			var islanduuid=$("select[name=islandUuid]").val();
			if(islanduuid==null||islanduuid==""){
				$.jBox.tip("岛屿不能为空,请选择岛屿!");
				return false;
			}
			var hotelUuid = $("select[name=hotelUuid]").val();
			if(hotelUuid==null||hotelUuid==""){
				$.jBox.tip("酒店名称不能为空,请选择酒店名称!");
				return false;
			}
			return true;
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
    
      <!--上传文件script-->
	  <script type="text/javascript">
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
							   /*  $(obj).parent().next(".batch-ol").append('<li><i class="sq_bz_icon"></i><span>'+ filename +'</span><a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(\''+ fileid +'\',\''+ $(obj).attr("name") +'\',this);">删除</a><input type="hidden" name="filedetail" value="'+ fileid+'#'+ filename+'#'+ filepath+'"/></li>'); */
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
		</script>
</head>

<body>
	<div>
		<div>
			<!--右侧内容部分开始-->
			<div class="mod_nav">产品 &gt; 海岛游产品 &gt; 海岛游产品发布</div>
			<div class="produceDiv">
				<form id="saveActivityIslandForm" modelAttribute="activityIslandInput" action=""  method="post" class="form-horizontal" novalidate="">
				<!--产品信息开始-->
				<div class="mod_information" id="ofAnchor1">
					<div class="mod_information mar_top0">
						<div class="ydbz_tit island_productor_upload001">
							<span class="ydExpand" data-target="#baseInfo"></span>基本信息
						</div>
						<div style="margin-top:8px; min-width:1400px !important;" id="baseInfo">
							<div class="activitylist_bodyer_right_team_co2 wpr20 pr" style="height:40px;">
								<div style="width:90px;" class="activitylist_team_co3_text"><span class="xing">*</span>产品名称：</div>
								<input type="text" value="" class="inputTxt inputTxtlong" name="activityName" id="activityName" flag="istips" /> 
								<span class="ipt-tips">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;命名规则 行程时间-海岛-交通方式</span>
							</div>
							<div class="kong"></div>
							<div class="activitylist_bodyer_right_team_co1">
								<div style="width:90px;" class="activitylist_team_co3_text"><span class="xing">*</span>产品编号：</div>
								<input type="text" value="" name="activitySerNum" class="inputTxt" id="activitySerNum" />
							</div>
							<div style="width:18%; height:40px; float:left; margin-left:2%; left:0px; top:0px;">
								<div class="activitylist_team_co3_text"><span class="xing">*</span>国家：</div>
								<trekiz:suggest id="country" name="country" style="width:150px;" defaultValue="80415d01488c4d789494a67b638f8a37" callback="getAjaxSelect('island',$('#country'))" displayValue="${countryName}" ajaxUrl="${ctx}/geography/getAllConListAjax" />
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
								<div style="width:90px;" class="activitylist_team_co3_text"><span class="xing">*</span>岛屿：</div>
								<select name="islandUuid" id="islandUuid" onchange="getAjaxSelect('islandway',this);">
								<option value="">不限</option>
							</select>
							</div>
							<div class="activitylist_bodyer_right_team_co1">
								<div style="width:90px;" class="activitylist_team_co3_text"><span class="xing">*</span>酒店名称：</div>
								<select name="hotelUuid" id="hotelUuid" onchange="getAjaxSelect('hotelRoom',this);">
									<option value="">不限</option>
								</select>
								<span class="padr10"></span>
								<a id="showHotelPl" onclick="showHotelPl()"  href="javascript:void(0);">查看价单</a>
							</div>
							<div class="activitylist_bodyer_right_team_co1">
								<div class="activitylist_team_co3_text">酒店星级：</div>
								<span class="y_xing" style="line-height:28px;"></span>
							</div>
							<!--查询结果筛选条件排序开始-->
							<div class="filterbox add_new_tq">
								<div class="filter_btn">
									<a class="btn btn-primary" href="#" id="addGroup">新增团期</a>
								</div>
							</div>
							<!--查询结果筛选条件排序结束-->
							<table id="contentTable" class="table activitylist_bodyer_table_new sea_rua_table">
									<thead>
										<tr>
											<th width="8%">团号/日期</th>
											<th width="5%">房型 * 晚数</th>
											<th width="4%">基础餐型</th>
											<th width="7%">升级餐型<br />升餐价格</th>
											<th width="4%">上岛方式</th>
											<th width="8%">航班<br /> 起飞到达时间</th>
											<th width="13%">舱位等级&amp;价格&amp;余位</th>
											<th width="5%">
												<p>
													余位<br />票数总计
												</p></th>
											<th width="6%">
												<p>预收/预报名</p></th>
											<th width="6%">单房差</th>
											<th width="8%">需交订金</th>
											<th width="8%">备注</th>
											<th width="4%">操作</th>
										</tr>
									</thead>
								<tbody>
									<tr style="display:none;">
										<td class="tc">
											<a name="groupcodeText"></a><br/><span></span>
										</td>
										<td class="tc"></td>
										<td class="tc"></td>
										<td class="tc"></td>
										<td class="tc"></td>
										<td class="tc"></td>
										<td class="tc"></td>
										<td class="tc"></td>
										<td class="tc">
											<span class="over_handle_cursor"></span>/<span>0</span>
										</td>
										<td class="tr"></td>
										<td class="tr tdgreen"></td>
										<td class="tl"></td>
										<td class="p0">
											<dl class="handle">
												<dt>
													<img title="操作" src="${ctxStatic}/images/handle_cz.png" />
												</dt>
												<dd class="">
													<p>
														<span></span> 
														<a class="copyLink" href="javascript:void(0)">复制</a> 
														<a class="updateLink" href="javascript:void(0)">修改</a> 
														<a class="delLink" href="javascript:void(0)">删除</a>
													</p>
												</dd>
											</dl></td>
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
		                   <%-- <img name=""  class="up_load_visa_info_btn" src="${ctxStatic }/images/add_11.jpg" onclick="addRecord()"/> --%>
		                   <input type="button" class="mod_infoinformation3_file" value="+添加" onclick="addRecord()"/>
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
		                           <!-- <div class="mod_information_d7"></div> -->
		                       </div>
		                       <ul class="up_files_outer">
		                       </ul>
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
					<input type="button" value="取消" onclick="window.close()" class="btn btn-primary gray" /> 
					<input type="button" value="保存" class="btn btn-primary" data-value="3" onclick="formValidate(this)" id="saveActivityIsland"/>
        			<input type="button" value="提交" class="btn btn-primary" data-value="1" onclick="formValidate(this)"/>
				</div>
			</div>
			</form>
			<!--第三步上传资料结束-->
		</div>
		<!--右侧内容部分结束-->
	</div>
	<!--新增团期信息项弹出层开始-->
	<div id="jbox_haidaoyou_fab">
		<div class="add_product_info new_hotel_p_table">
			<table class="table_product_info " style="width:900px !important; ">
				<tr>
					<td class="tr" style="width:121px !important;">团号</td>
					<td width="710" colspan="3">
						<input type="text" class="inputTxt w106 spread" name="groupCode"/>
					</td>
				</tr>
				<tr>
					<td class="tr">日期</td>
					<td colspan="3">
						<input type="text" onclick="WdatePicker()" class="dateinput w106 required " readonly="readonly" name="groupDate"/>
					</td>
				</tr>
				<tr class="houseTypeTr">
					<td class="tr">房型*晚数</td>
					<td colspan="3">
						<p class="houseType">
							<span> 
								<select class="w80">
								</select> 
								<span class="w50_30">*</span> 
								<input type="text" data-type="number" data-min="1" class="inputTxt w50_30" /> 
								<span class="w50_30">晚</span> 
								<a class="ydbz_x  addHouseType">新增</a> 
							</span>
						</p>
					</td>
				</tr>
				<tr class="houseTypeTr mealTypeTr">
					<td class="tr">基础餐型</td>
					<td colspan="3">
						<select class="w80">
						</select> 
						<a class="ydbz_x addMealType">新增</a> 
						<span class="padr10"></span>
						<span> <input class="redio_martop_4" type="checkbox" />升级餐型 </span>
					</td>
					<td class="tr new_hotel_p_table2_tdblue" style="display:none;"><span>升级餐型</span>
					</td>
					<td style="display:none;" class="upMealTypeTd">
						<p class="upMealType">
							<span> 
								<select name="select10" class="w80 mr3">
								</select> 
								<select name="select10" class="w50_30 mr3 currency groupCurrency">
									<c:forEach items="${currencyList}" var="item">
									    <option value="${item.id}" >${item.currencyMark}</option>
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
					<td width="350">
						
					</td>
					<td width="130" class="tr new_hotel_p_table2_tdblue">单房差</td>
					<td width="350">
						<span class="add_jbox_repeat_thj"> 
							<select class="w50_30 currency groupCurrency">
								<c:forEach items="${currencyList}" var="item">
								    <option value="${item.id}" >${item.currencyMark}</option>
								</c:forEach>
							</select> 
							<input type="text" data-type="float" class="inputTxt w50_30 babyPrice mr25" /> 
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
						<select class="w125_sel_pop_addnewtimt selAirline" name="airlineComp">
							<option value="">请选择</option>
						</select>
					</td>
					<td class="tr new_hotel_p_table2_tdblue">航班号</td>
					<td><select class="w125_sel_pop_addnewtimt fltNo"></select></td>
				</tr>
				<tr>
					<td class="tr new_hotel_p_table2_tdblue">起飞时间</td>
					<td class="tl">
						<input type="text" onclick="WdatePicker({ dateFmt: 'HH:mm' })" class="dateinput w106 required startTime" readonly="readonly" />
					</td>
					<td class="tr new_hotel_p_table2_tdblue">到达时间</td>
					<td>
						<input type="text" onclick="WdatePicker({ dateFmt: 'HH:mm' })" class="dateinput w106 required endTime" readonly="readonly" /> +
						<input type="text" data-type="number" class="inputTxt w50_30 days" value="0" data-min="0" /> 天
					</td>
				</tr>
				<tr>
					<td colspan="4" class="up_load_visa_info_td01 hotel_air_price">
						<table class="new_hotel_p_table2" style="width:900px !important; ">
							
						</table>
						<table class="new_hotel_p_table2" style="width:900px !important;" id="travelerTypePrice">
							
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
					<td><input type="text" data-type="number" data-min="0" class="inputTxt w50_30 spread" /> 人</td>
					<td class="tr new_hotel_p_table2_tdblue">需交订金</td>
					<td>
						<select name="select13" class="w50_30 groupCurrency">
							<c:forEach items="${currencyList}" var="item">
							    <option value="${item.id}" >${item.currencyMark}</option>
							</c:forEach>
						</select>
						<input type="text" data-type="float" class="inputTxt w50_30 spread" />
					</td>
				</tr>
				<tr>
					<td class="tr valign_top">备注</td>
					<td colspan="3">
						<textarea class="inputTxt spread" style=" width:90%; height:100px;"></textarea>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<!--新增团期信息项弹出层结束-->
  	<script type="text/javascript" src="${ctxStatic}/js/activityIslandProduct.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#addGroup").on('click', function () {
                var options = {
                    isAdd: true,
                    callback: writeRow,
                    houseTypes: houseTypes
                }
                addGroup_box(options);
                $(".groupCurrency").val($("#currencyId").val());
            });

            $("#contentTable").on('click', 'a.copyLink', function () {
                var id = $("#contentTable tbody tr").has(this).prop('id');
                var options = {
                    isAdd: true,
                    callback: writeRow,
                    id: id,
                    houseTypes: houseTypes,
                    data: readRow(id)
                }
                addGroup_box(options);

            }).on('click', 'a.updateLink', function () {
                var id = $("#contentTable tbody tr").has(this).prop('id');
                var options = {
                    isAdd: false,
                    callback: writeRow,
                    id: id,
                    houseTypes: houseTypes,
                    data: readRow(id)
                }
                addGroup_box(options);
            }).on('click', 'a.delLink', function () {
                var $islandGroupTr = $("#contentTable tbody tr").has(this);
                var id = $islandGroupTr.prop('id');
                var groupcodeText = $islandGroupTr.find('a[name=groupcodeText]').text();
				if(groupcodeText != ""){
					delete map[groupcodeText];
				}
				
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
                // 团号
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
                    data.trafficWays.push($(this).text());
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
                        // 6.29 价格结构修改
                        if (i == 0) {
                            data.airline.prices.push({ islandprice: [] });
                        }
                        data.airline.prices[j].islandprice.push({
                            currency: {
                                value: $(this).find("span:eq(0)").attr("data-value"),
                            },
                            price: $(this).find("span:eq(1)").text()
                        });
                        // end
                    });
                });
                cIndex++;
                // 余位/票数总计
                cIndex++;
                // 优先扣减
                data.ctrlTicketPriority = $tds.eq(cIndex).attr("data-ctrlPriority");
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
                $.each(data.trafficWays, function () {
                    html.push('<p data-value="',this.value,'">', this.text, '</p>');
                });
                $tds.eq(cIndex).html(html.join(''));
                cIndex++;
                // 航空公司
                html = [];
                if(data.airline.value && data.airline.flight.value == "") {
                	// 航班号
                    html.push('<span data-value="', data.airline.value, ',', data.airline.flight.value, '">', '', '</span><br>');
                }else if (data.airline.flight.value) {
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
                         html.push('<p>', data.airline.touristType[i].text, '：<span data-value="', this.currency.value, '">', this.currency.text, '</span><span>', this.price, '</span></p>');
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
                // 备注
                $tds.eq(cIndex).html(data.comment);
                $tds.eq(cIndex).append($("<input/>").attr("type","hidden").val(JSON.stringify(data)).attr("name","jsoninput"));
                return true;
            }
        });
    </script>
</body>
</html>