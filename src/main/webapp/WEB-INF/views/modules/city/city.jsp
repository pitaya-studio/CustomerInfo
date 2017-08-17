<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>superadmin-基础信息-城市管理</title>
<link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
<link href="css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.css"><!--树形插件的样式-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.mCustomScrollbar.css" /><!--滚动条插件样式-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jh-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/huanqiu-style.css" />
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.js"></script><!--树形插件的脚本-->
<script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.excheck-3.5.js"></script><!--树形插件的脚本-->
<script type="text/javascript" src="${ctxStatic}/js/jquery.mousewheel.min.js"></script><!--滚动条插件脚本-->
<script type="text/javascript" src="${ctxStatic}/js/jquery.mCustomScrollbar.js"></script><!--滚动条插件脚本-->
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
var tree, $key, lastValue = "", nodeList = [];
$(document).ready(function(){
	var setting = {
		check:{
			enable:true,
			nocheckInherit:true
		},view:{
			selectedMulti:false,
			fontCss:function(treeId, treeNode) {
				return (!!treeNode.highlight) ? {"font-weight":"bold","color":"#ff0000"} : {"font-weight":"normal","color":"#333333"};
			}

		},data:{
			simpleData:{enable:true}
		},callback:{
			beforeClick: beforeClick,
			onCheck: onCheck
		}
	};
	function beforeClick(id, node) {
		tree.checkNode(node, !node.checked, true, true);
		return false;
	}
	
	// 用户-菜单	
	var zNodes =eval($("#treeData").val());
	/* var zNodes =[
		{ id:1, pId:0, name:"亚洲"},
	  	{ id:11, pId:1, name:"日本"},
	  	{ id:13, pId:1, name:"韩国"},
	  	{ id:13, pId:1, name:"泰国"},
	  	{ id:5, pId:0, name:"大洋洲"},
	  	{ id:51, pId:5, name:"新西兰"},
	  	{ id:52, pId:5, name:"澳大利亚",children:[
			{id:61,name:"悉尼"},
			{id:62,name:"墨尔本"}
		]}
   ]; */
	// 初始化树结构
	tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
	// 默认选择节点
	var id = $("#searchId").val();
	$(("#"+id),window.parent.document).find("a").each(function(){		
		var node = tree.getNodeByParam("id", $(this).attr("value"));
		try{tree.checkNode(node, true, true);
		    onCheck('',$(this).attr("value"),node);
		}catch(e){}
	});
	
	// 默认展开全部节点
	tree.expandAll(false);
	//美化滚动条
	var $_roleLeftCen=$('.role-leftCen');
	var $_roleRightCen=$('.role-rightCen');
	
	$_roleLeftCen.mCustomScrollbar();
	$_roleRightCen.mCustomScrollbar();
	
	function onCheck(e, treeId, treeNode) {
		var nodes = tree.getCheckedNodes(true);
		var $hasAdd = $("#addArea").find("li");
		var str_html = '';
		//遍历添加新增选项
		for (var i = 0; i < nodes.length; i++) {
        	//msg += nodes[i].name+"--"+nodes[i].id+"--"+nodes[i].pId+"\n";
			if(!nodes[i].isParent){
				var isInclude = 0;
				$hasAdd.each(function(index, element) {
					if(nodes[i].tId == $(element).attr("forID")){
						isInclude = 1;
						return;
					}
				});
				if(!isInclude){
					str_html += '<li forID="' + nodes[i].tId +'"><span>' + nodes[i].name +'</span><i>X</i></li>';
				}
			}
        }
		
		//遍历删除选项
		$hasAdd.each(function(index, element) {
            var isInclude = 0;
			for (var i = 0; i < nodes.length; i++) {
				if(nodes[i].tId == $(element).attr("forID")){
					isInclude = 1;
					return;
				}
			}
			if(!isInclude){
				$(element).remove();
			}
        });
		if("" != str_html){
			$("#addArea").prepend(str_html);
		}
		
		//设置已添加城市数为0
		$(".role-rightTop span em").text($("#addArea li").length);
	}
	
	//删除已选择项目
	$("#addArea").on("click","li i",function(){
		var $li = $(this).parent("li");
		var treeNode = tree.getNodeByTId($li.attr("forID"));
		tree.checkNode(treeNode,false,true);
		$li.remove();
	});
	
	//清空已添加的城市
	$(".role-rightTop p").click(function(){
		$.jBox.confirm("确定要清空数据吗？","提示",function(v,h,f){
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
	$key.val("").focus(function(e){
		if ($key.hasClass("empty")) {
			$key.removeClass("empty");
		}
	}).blur(function(e){
		if ($key.get(0).value === "") {
			$key.addClass("empty");
		}
		searchNode(e);
	}).bind("change keydown cut input propertychange", searchNode);
});
//提交表单前进行数据处理
function toSubmit(){
	//获取全部勾选的项目
	var nodesChecked = tree.getCheckedNodes(true);
	var arrayID = [];
	for(var i=0;i<nodesChecked.length;i++){
		if(!nodesChecked[i].isParent){
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
	for(var i=0, l=nodeList.length; i<l; i++) {
		nodeList[i].highlight = highlight;				
		tree.updateNode(nodeList[i]);
		tree.expandNode(nodeList[i].getParentNode(), true, false, false);
	}
}

</script>
</head>

<body>
<!--右侧内容部分开始-->
                        <div class="coverarea">
                            <div class="role">
                                <div class="role-left">
                                    <div class="role-leftTop">
                                        <input type="text" id="key" name="key" style="height:30px;"/>
                                    </div>
                                    <div class="role-leftCen">
                                        <ul id="menuTree" class="ztree" style="margin-top:3px;"></ul>
                                        <input id="menuIds" name="menuIds" type="hidden" value=""/>
                                    </div>
                                </div>
                                <div class="role-right">
                                    <div class="role-rightTop">
                                        <span>已添加<em>0</em>个城市</span>
                                        <p><i></i>清空</p>
                                    </div>
                                    <div class="role-rightCen">
                                        <ul id="addArea"></ul>
                                    </div>
                                </div>    
                            </div>
                          <input id="treeData" type="hidden" name="treeData"  value="${treeData}"/>    
                          <input id="searchId" type="hidden" name="searchId"  value="${searchId}"/>    
                        </div>
                <!--右侧内容部分结束-->    

</body>
</html>
