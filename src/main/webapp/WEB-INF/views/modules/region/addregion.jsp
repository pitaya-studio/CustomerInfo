<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>基础信息维护-地理区域管理</title>
<link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.css"><!--树形插件的样式-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.mCustomScrollbar.css" /><!--滚动条插件样式-->
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
$(function(){
	g_context_url = "${ctx}";
	})
var tree, $key, lastValue = "", nodeList = [];
$(document).ready(function(){
	var setting = {
		check:{
			enable:true,
			chkboxType: { "Y": "", "N": "s" }
			//nocheckInherit:true
		},view:{
			selectedMulti:false,
			fontCss:function(treeId, treeNode) {
				return (!!treeNode.highlight) ? {"font-weight":"bold","color":"#ff0000"} : {"font-weight":"normal","color":"#333333"};
			}

		},data:{
			simpleData:{enable:true}
		},callback:{
			//beforeClick: beforeClick,
			onCheck: onCheck
		},
		async: {  
            enable: true,  
            url:g_context_url+"/geography/secondList",  
            autoParam:["id=id"],  
            otherParam:{"lable":"${lable}"},  
            dataFilter: filter  
        }  
	};
	
    function filter(treeId, parentNode, childNodes) {  
        if (!childNodes) return null;  
        for (var i=0, l=childNodes.length; i<l; i++) {  
            childNodes[i].name = childNodes[i].name.replace('','');  
        }  
        return childNodes;  
    } 
	function beforeClick(id, node) {
		tree.checkNode(node, !node.checked, true, true);
		return false;
	}
	
	// 用户-菜单
	
	var zNodes =eval($("#treeData").val());
	// 初始化树结构
	tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
	// 默认选择节点
 	var ids = $("[name='outKey']").val().split(",");
	for(var i=0; i<ids.length; i++) {
		var node = tree.getNodeByParam("id", ids[i]);
		try{tree.checkNode(node, true, true);
		onCheck('',ids[i],node);
		}catch(e){}
	} 
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
				var isInclude = 0;
				$hasAdd.each(function(index, element) {
					if(nodes[i].id == $(element).attr("forID")){
						isInclude = 1;
						return;
					}
				});
				if(!isInclude){
					str_html += '<li forID="' + nodes[i].id +'"><span>' + nodes[i].name +'</span><i>X</i></li>';
				}
        }
		//遍历删除选项
		$hasAdd.each(function(index, element) {
            var isInclude = 0;
			for (var i = 0; i < nodes.length; i++) {
				if(nodes[i].id == $(element).attr("forID")){
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
		
		//设置已添加城市
		$(".role-rightTop span em").text($("#addArea li").length);
	}
	
	//删除已选择项目
	$("#addArea").on("click","li i",function(){
		var $li = $(this).parent("li");
		var treeNode = tree.getNodeByParam("id", $li.attr("forID"));
		
		tree.checkNode(treeNode,false,true);
		$li.remove();
		$(".role-rightTop span em").text($("#addArea li").length);
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
	var nameCn = $("#name").val();
	if(nameCn==null|| $.trim(nameCn)==""){
		top.$.jBox.tip('请填写区域名称');
		return ;
	}
	//获取全部勾选的项目
	var nodesChecked = tree.getCheckedNodes(true);
	var arrayID = [];
	for(var i=0;i<nodesChecked.length;i++){
		if(!nodesChecked[i].isParent){
			arrayID.push(nodesChecked[i].id);
		}
	}
	if(arrayID.length>=1){
	$("[name='outKey']").val(arrayID);
	}
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
//使用的方法
function onSubmit(){
	var nameCn = $("#name").val();
	if(nameCn==null|| $.trim(nameCn)==""){
		top.$.jBox.tip('请填写区域名称');
		return false;
	}
	
	
	var nodesChecked = tree.getCheckedNodes(true);
	var arrayID = [];
	for(var i=0;i<nodesChecked.length;i++){
		//if(!nodesChecked[i].isParent){}
			arrayID.push(nodesChecked[i].id);
		
	}
	$("[name='outKey']").val(arrayID);
	

	$("#submitButton").attr("disabled", true);
	$.ajax({
		type : "POST",
		url : g_context_url+"/region/saveOrUpdateRegion",
		data :$("#inputForm").serialize(),
		dataType : "text",
		error: function(request) {                     
			top.$.jBox.tip('操作失败');
			$("#submitButton").attr("disabled", true);
		},                 
		success: function(data) { 
			if(data!=null&&data!=""){
				top.$.jBox.tip(data);
				$("#submitButton").attr("disabled", false);
				return false;
			}                
			top.$.jBox.tip("操作成功！");
			window.location.href=g_context_url+"/region/regionList?lable="+$("#lable").val();
			return true;
		}   
	
	});
}
</script>
</head>
<body>
        <page:applyDecorator name="sys_regionAdd" >
  		    <page:param name="current">${lable}</page:param>
  		    <page:param name="kind">${kind}</page:param>
	    </page:applyDecorator>   
	     <input id="treeData" type="hidden" name="treeData"  value="${treeData}"/> 
				<!--右侧内容部分开始-->
                <form id="inputForm" class="form-horizontal" action="${ctx}/region/saveOrUpdateRegion" method="post">
                 <input type="hidden" id="lable" name="lable" value="${lable}"/> 
                 <input id="kind" type="hidden" name="kind"  value="${kind}"/>
                 <input id="id" type="hidden" name="id"  value="${id}"/>
                    	<p>
                            <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;状态：</label>
						     <c:choose>
							    <c:when test="${kind =='update'}">
							       <span>
							          <select name="status">
							          	<option value="0" <c:if test="${sysRegion.status=='0'}">selected="selected"</c:if>>启用</option>
							            <option value="1" <c:if test="${sysRegion.status=='1'}">selected="selected"</c:if>>停用</option>
							          </select>
							        </span>
							    </c:when>    
						        <c:otherwise>
						           <span>
						              <select name="status">
						              	<option value="0" selected="selected">启用</option>
						                  <option value="1">停用</option>
						              </select>
						          </span>
						        </c:otherwise>
						   </c:choose>
                        </p>
                    	<p>
                            <label>区域名称：</label>
                            <span>
                                <input name="name" id="name" value="${sysRegion.name}" type="text"  maxlength="25">
                            </span>
                        </p>
                    <div class="coverarea">
                        <label class="coverarea-label" style="width:60px">覆盖范围：</label>
                        <div class="role">
                            <div class="role-left">
                                <div class="role-leftTop">
                                    <input id="key" name="key" type="text"  maxlength="122" />
                                </div>
                                <div class="role-leftCen mCustomScrollbar _mCS_1 mCS_no_scrollbar">
                                <div tabindex="0" id="mCSB_1" class="mCustomScrollBox mCS-light mCSB_vertical mCSB_inside">
                                <div id="mCSB_1_container" class="mCSB_container mCS_y_hidden mCS_no_scrollbar_y" style="position:relative; top:0; left:0;" dir="ltr">
                                    <ul id="menuTree" class="ztree" style="margin-top: 3px; -moz-user-select: none;">
                                    </ul>
                                     <input id="menuIds" name="outKey" type='hidden'  maxlength="122" value="${outKey}"/>
                                </div><div style="display: none;" id="mCSB_1_scrollbar_vertical" class="mCSB_scrollTools mCSB_1_scrollbar mCS-light mCSB_scrollTools_vertical"><div class="mCSB_draggerContainer"><div id="mCSB_1_dragger_vertical" class="mCSB_dragger" style="position: absolute; min-height: 30px; height: 0px; top: 0px;" oncontextmenu="return false;"><div style="line-height: 30px;" class="mCSB_dragger_bar"></div></div><div class="mCSB_draggerRail"></div></div></div></div></div>
                            </div>
                            <div class="role-right">
                                <div class="role-rightTop">
                                    <span>已添加<em>0</em>个城市</span>
                                    <p><i></i>清空</p>
                                </div>
                                <div class="role-rightCen mCustomScrollbar _mCS_2 mCS_no_scrollbar"><div tabindex="0" id="mCSB_2" class="mCustomScrollBox mCS-light mCSB_vertical mCSB_inside"><div id="mCSB_2_container" class="mCSB_container mCS_y_hidden mCS_no_scrollbar_y" style="position:relative; top:0; left:0;" dir="ltr">
                                    <ul id="addArea"></ul>
                                </div><div style="display: none;" id="mCSB_2_scrollbar_vertical" class="mCSB_scrollTools mCSB_2_scrollbar mCS-light mCSB_scrollTools_vertical"><div class="mCSB_draggerContainer"><div id="mCSB_2_dragger_vertical" class="mCSB_dragger" style="position: absolute; min-height: 30px; top: 0px;" oncontextmenu="return false;"><div style="line-height: 30px;" class="mCSB_dragger_bar"></div></div><div class="mCSB_draggerRail"></div></div></div></div></div>
                            </div>    
                        </div>                            
                    </div>
                        <p class="maintain_pfull">
                            <label>区域描述：</label>
                            <textarea class="madintain_text" maxlength="150" name="description" >${sysRegion.description}</textarea>
                         </p>
                      <div class="release_next_add">
                        <input type="button" class="btn btn-primary" id="submitButton" value="确定"  onclick="onSubmit()"  />
                        <input type="button" class="btn btn-primary gray" onclick="history.go(-1)" value="返回" />
                    </div>
                   </form>
                <!--右侧内容部分结束-->     
</body>
</html>
