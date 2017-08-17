	var tree, $key, lastValue = "", nodeList = [];
	$(document).ready(function() {
		var setting = {
			check : {
				enable : true,
				nocheckInherit : true
			},
			view : {
				selectedMulti : false,
				fontCss : function(treeId, treeNode) {
					return (!!treeNode.highlight) ? {
						"font-weight" : "bold",
						"color" : "#ff0000"
					} : {
						"font-weight" : "normal",
						"color" : "#333333"
					};
				}
			},
			data : {
				simpleData : {
					enable : true
				}
			},
			callback : {
				beforeClick : beforeClick,
				onCheck : onCheck
			}
		};
		function beforeClick(id, node) {
			tree.checkNode(node, !node.checked, true, true);
			return false;
		}

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

		// 用户-菜单
		var zNodes = [];
		$.ajax({
			type : "POST",
			url : "${ctx}/sys/user/deptUserList",
			success : function(msg) {
				zNodes = JSON.parse(msg);
				initDataTree();
			}
		});
		
		//删除已选择项目
		$("#addArea").on("click", "li i", function() {
			var $li = $(this).parent("li");
			var treeNode = tree.getNodeByTId($li.attr("forID"));
			tree.checkNode(treeNode, false, true);
			$li.remove();
			$(".role-rightTop span em").text($("#addArea li").length);
		});

		//清空已添加的用户
		$(".role-rightTop p").click(function() {
			$.jBox.confirm("确定要清空数据吗？", "提示", function(v, h, f) {
				if (v == 'ok') {
					//取消结点树所有的可选项
					tree.checkAllNodes(false);
					//清除已选择项目
					$("#addArea").empty();
					//设置已添加用户数为0
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

	function initDataTree() {
		// 初始化树结构
		tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
		// 默认展开全部节点
		tree.expandAll(true);
		//美化滚动条
		var $_roleLeftCen = $('.role-leftCen');
		var $_roleRightCen = $('.role-rightCen');

		$_roleLeftCen.mCustomScrollbar();
		$_roleRightCen.mCustomScrollbar();

	}

	function onCheck(e, treeId, treeNode) {
		var nodes = tree.getCheckedNodes(true);
		var $hasAdd = $("#addArea").find("li");
		var str_html = '';
		//遍历添加新增选项
		for ( var i = 0; i < nodes.length; i++) {
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
					str_html += '<li forID="' + nodes[i].tId + '"><span><input type="hidden" name="shareUser" value="' + nodes[i].id + '" />' + nodes[i].name + '</span><i>X</i></li>';
				}
			}
		}

		//遍历删除选项
		$hasAdd.each(function(index, element) {
			var isInclude = 0;
			for ( var i = 0; i < nodes.length; i++) {
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

		//设置已添加用户数为0
		$(".role-rightTop span em").text($("#addArea li").length);
	}