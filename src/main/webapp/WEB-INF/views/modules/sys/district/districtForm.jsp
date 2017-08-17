<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html><head>
    <title>类型字典维护</title>
    <meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="${ctxStatic}/css/jh-style.css" type="text/css" rel="stylesheet">
    <link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet">
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/huanqiu-style.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jh-style.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.min.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.excheck-3.5.js"></script>
    <link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon"/>
    <link href="${ctxStatic}/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript">
        //选择审批后显示成本付款审批
        $(document).on('change', '[name="workflow-ls"]', function () {
            if ($('[name="workflow-ls"]:checked').length > 0) {
                $('[name="costPaymentApproval"]').show();
            } else {
                $('[name="costPaymentApproval"]').hide();
            }
            ;
        })

        $(function () {
            //搜索条件筛选
            launch();
            //操作浮框
            operateHandler();
            //取消一个checkbox 就要联动取消 全选的选中状态
            $("input[name='ids']").click(function () {
                if ($(this).attr("checked")) {

                } else {
                    $("input[name='allChk']").removeAttr("checked");
                }
            });
//              显示历史版本
            $(document).ready(function () {
                $('[name="display-history-img"]').click(function () {
                    $(".history-flow").show();
                });
            });
            var resetSearchParams = function () {
                $(':input', '#searchForm')
                        .not(':button, :submit, :reset, :hidden')
                        .val('')
                        .removeAttr('checked')
                        .removeAttr('selected');
                $('#country').val("");
            }
            $('#contentTable').on('change', 'input[type="checkbox"]', function () {
                if ($('#contentTable input[type="checkbox"]').length == $('#contentTable input[type="checkbox"]:checked').length) {
                    $('[name="allChk" ]').attr('checked', true);
                } else {
                    $('[name="allChk" ]').removeAttr('checked');
                }
            });
        });

        //展开收起
        function expand(child, obj) {
            if ($(child).is(":hidden")) {
                $(child).show();
                $(obj).parents("td").addClass("td-extend");
                $(obj).parents("tr").addClass("tr-hover");

            } else {
                $(child).hide();
                $(obj).parents("td").removeClass("td-extend");
                $(obj).parents("tr").removeClass("tr-hover");

            }
        }
        function checkall(obj) {
            if ($(obj).attr("checked")) {
                $('#contentTable input[type="checkbox"]').attr("checked", 'true');
                $("input[name='allChk']").attr("checked", 'true');
            } else {
                $('#contentTable input[type="checkbox"]').removeAttr("checked");
                $("input[name='allChk']").removeAttr("checked");
            }
        }

        function checkreverse(obj) {
            var $contentTable = $('#contentTable');
            $contentTable.find('input[type="checkbox"]').each(function () {
                var $checkbox = $(this);
                if ($checkbox.is(':checked')) {
                    $checkbox.removeAttr('checked');
                } else {
                    $checkbox.attr('checked', true);
                }
            });
        }
        $(document).ready(function () {
            $(document).on('click', '.productName input,.workflowName input', function () {
                var $checkbox = $(this);
                if ($checkbox.is(':checked')) {
                    $checkbox.parent().addClass('on');
                    $checkbox.parent().removeClass('off');
                } else {
                    $checkbox.parent().addClass('off');
                    $checkbox.parent().removeClass('on');
                }
            })

            $(document).find('.productName input,.workflowName input').each(function () {
                var $checkbox = $(this);
                if ($checkbox.is(':checked')) {
                    $checkbox.parent().addClass('on');
                    $checkbox.parent().removeClass('off');
                } else {
                    $checkbox.parent().addClass('off');
                    $checkbox.parent().removeClass('on');
                }
            });
        });
    </script>
    <script type="text/javascript">
    var areas='${destinationIds }';
    var split = new Array(); // 目的地id数组
    
    function zTreeOnCheck(event, treeId, treeNode) {
    	split = areas.split(",");
    	spliceAreas(event, treeId, treeNode);
    	
    	split.remove("");
    	areas = split.join(",");
    	$("#destinationIds").val(areas);
    }
    
    /**
    	递归实现遍历树形目录
    	@author wangyang
    */
    function spliceAreas(event, treeId, treeNode) {
		if (null != treeNode.children && treeNode.children.length > 0) {
			// 非叶子节点 递归遍历其子树直到叶子节点
			for (var i = 0; i < treeNode.children.length; i++) {
				var childNode = treeNode.children[i];
				spliceAreas(event, treeId, childNode);
			}
		} else {
			// 叶子节点 获取节点code值，添加或移除
			var code = treeNode.code + "";
			if (treeNode.checked) {
				split.push(code);// 添加目的地id
			} else {
				for (var j = 0; j < split.length; j++) {
					if (split[j] == code) {
						split.remove(code);// 移除目的地id
					}
				}
			}
		}
	}
    
    function zTreeOnClick(event, treeId, treeNode) {
    	tree.checkNode(treeNode, !treeNode.checked, true, true);
    };
        //ztree 设置
        var tree, setting = {
            check: {
                enable: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
        		onCheck: zTreeOnCheck,
        		onClick: zTreeOnClick
        	},
            view: { showIcon: true, selectedMulti: false, nameIsHTML: true, fontCss: setFontCss_ztree }
        };
        //部门
		var departs=${areaMap};

        $(function () {
            tree = $.fn.zTree.init($("#departTree"), setting, departs);
            //tree.expandAll(false);
        });
		
        function search() {
            $("#search").slideToggle(200);
            $("#txt").toggle();
            $("#key").focus();
        }
        /**
//       * 展开树
//       * @param treeId
//       */
      function expand_ztree(treeId){
          var treeObj = $.fn.zTree.getZTreeObj(treeId);
          treeObj.expandAll(true);
      }

//      /**
//       * 收起树：只展开根节点下的一级节点
//       * @param treeId
//       */
      function close_ztree(treeId){
          var treeObj = $.fn.zTree.getZTreeObj(treeId);
          var nodes = treeObj.transformToArray(treeObj.getNodes());
          var nodeLength = nodes.length;
          for (var i = 0; i < nodeLength; i++) {
              if (nodes[i].id == '0') {
                  //根节点：展开
                  treeObj.expandNode(nodes[i], true, true, false);
              } else {
                  //非根节点：收起
                  treeObj.expandNode(nodes[i], false, true, false);
              }
          }
      }

//      /**
//       * 搜索树，高亮显示并展示【模糊匹配搜索条件的节点s】
//       * @param treeId
//       * @param searchConditionId 文本框的id
//       */
      function search_ztree(treeId, searchConditionId){
          searchByFlag_ztree(treeId, searchConditionId, "");
      }

//      /**
//       * 搜索树，高亮显示并展示【模糊匹配搜索条件的节点s】
//       * @param treeId
//       * @param searchConditionId     搜索条件Id
//       * @param flag                  需要高亮显示的节点标识
//       */
      function searchByFlag_ztree(treeId, searchConditionId, flag){
          //<1>.搜索条件
          var searchCondition = $('#' + searchConditionId).val();
          //<2>.得到模糊匹配搜索条件的节点数组集合
          var highlightNodes = new Array();
          if (searchCondition != "") {
              var treeObj = $.fn.zTree.getZTreeObj(treeId);
              highlightNodes = treeObj.getNodesByParamFuzzy("name", searchCondition, null);
          }
          //<3>.高亮显示并展示【指定节点s】
          highlightAndExpand_ztree(treeId, highlightNodes, flag);
      }

//      /**
//       * 高亮显示并展示【指定节点s】
//       * @param treeId
//       * @param highlightNodes 需要高亮显示的节点数组
//       * @param flag           需要高亮显示的节点标识
//       */
      function highlightAndExpand_ztree(treeId, highlightNodes, flag){
          var treeObj = $.fn.zTree.getZTreeObj(treeId);
          //<1>. 先把全部节点更新为普通样式
          var treeNodes = treeObj.transformToArray(treeObj.getNodes());
          for (var i = 0; i < treeNodes.length; i++) {
              treeNodes[i].highlight = false;
              treeObj.updateNode(treeNodes[i]);
          }
          //<2>.收起树, 只展开根节点下的一级节点
          //close_ztree(treeId);
          //<3>.把指定节点的样式更新为高亮显示，并展开
          if (highlightNodes != null) {
              for (var i = 0; i < highlightNodes.length; i++) {
                  if (flag != null && flag != "") {
                      if (highlightNodes[i].flag == flag) {
                          //高亮显示节点，并展开
                          highlightNodes[i].highlight = true;
                          treeObj.updateNode(highlightNodes[i]);
                          //高亮显示节点的父节点的父节点....直到根节点，并展示
                          var parentNode = highlightNodes[i].getParentNode();
                          var parentNodes = getParentNodes_ztree(treeId, parentNode);
                          treeObj.expandNode(parentNodes, true, false, true);
                          treeObj.expandNode(parentNode, true, false, true);
                      }
                  } else {
                      //高亮显示节点，并展开
                      highlightNodes[i].highlight = true;
                      treeObj.updateNode(highlightNodes[i]);
                      //高亮显示节点的父节点的父节点....直到根节点，并展示
                      var parentNode = highlightNodes[i].getParentNode();
                      var parentNodes = getParentNodes_ztree(treeId, parentNode);
                      treeObj.expandNode(parentNodes, true, false, true);
                      treeObj.expandNode(parentNode, true, false, true);
                  }
              }
          }
      }

//      /**
//       * 递归得到指定节点的父节点的父节点....直到根节点
//       */
      function getParentNodes_ztree(treeId, node){
          if (node != null) {
              var treeObj = $.fn.zTree.getZTreeObj(treeId);
              var parentNode = node.getParentNode();
              return getParentNodes_ztree(treeId, parentNode);
          } else {
              return node;
          }
      }

      /**
       * 设置树节点字体样式
       */
      function setFontCss_ztree(treeId, treeNode) {
          if (treeNode.id == 0) {
              //根节点
              return {color:"#333", "font-weight":"bold"};
          } else if (treeNode.isParent == false){
              //叶子节点
              return (!!treeNode.highlight) ? {color:"#000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
          } else {
              //父节点
              return (!!treeNode.highlight) ? {color:"#000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
          }
      }
      
      var isModify = '${isModify }';
      var id = '${id }';
      //确定
    function save(){
    	
    	 if($("#label").val()==""){
    		$("#span1").attr("style","color:red;display:inline");
  			$("#span1").text("必填信息！");
    		//top.$.jBox.tip("线路名称为必填项！");
    		return;
    	 }
        if($("#label").val().length > 10) {
            $("#span1").attr("style","color:red;display:inline;width:100px;");
            $("#span1").text("长度不能超过10！");
            return;
        }
    	 var checkN=checkName(isModify, id);
    	 if(checkN){
    		 top.$.jBox.tip("该旅游区域已存在");
    		 return;
    	 } 
    	 if($("#destinationIds").val()==""){
    		 top.$.jBox.tip("相关城市为必填项！");
   		  	return;
    	 }
    	 /* var checkA = checkAreas(isModify, id);
    	 if(checkA){
    		 top.$.jBox.tip("已有线路存在此区域！");
    		  	return; 
    	 } */
    	 
    	 $.ajax({
    		 type:"post",
    		 url:"${ctx}/sys/district/saveOrUpdate?token=${token}",
    		 data:$("#companyDictForm").serialize(),
    		 success:function(result){
    			 if(result){
    				 top.$.jBox.tip("操作成功");
    				 parent.location.reload();
    			 }else{
    				 top.$.jBox.tip("操作失败");
    			 }
    		 }
    	 });
      } 
      //检查是否重复
      function checkName(isModify, id){
    	  var checkName = false;
    	  var name = $("#label").val();
    	  var url;
    	  if (isModify) {
    	  	url = "${ctx}/sys/district/checkDistrictName?id=" + id;
    	  } else {
    	  	url = "${ctx}/sys/district/checkDistrictName";
    	  }
    	  
    	  $.ajax({
    		 type:"post",
    		 url:url,
    		 async:false,
    		 data:{
    			 name:name
    		 },
    		 success:function(result){
    			 if(result){
    				 checkName = true;
    			 }else{
    				 checkName = false;
    			 }
    		 }
    	  });
    	  return checkName;
      }
      //检查所含区域是否重复
      function checkAreas(isModify, id) {
    	  var checkAreas = false;
    	  var destinationIds = $("#destinationIds").val();
    	  var url;
    	  if (isModify) {
    	  	url = "${ctx}/tourist/line/checkAreas?id=" + id;
    	  } else {
    	  	url = "${ctx}/tourist/line/checkAreas";
    	  }
    	  $.ajax({
     		 type:"post",
     		 url:url,
     		 async:false,
     		 data:{
     			 destinationIds : destinationIds
     		 },
     		 success:function(result){
     			 if(result){
     				 checkAreas=true;
     			 }else{
     				 checkAreas=false;
     			 }
     		 }
     	  });
    	  return checkAreas;
      }
	function checklabel(){
		if($("#label").val().trim()==""){
			$("#span1").attr("style","color:red;display:inline");
			$("#span1").text("必填信息！");
			return;
		}
        if($("#label").val().length > 10) {
            $("#span1").attr("style","color:red;display:inline;width:100px");
            $("#span1").text("长度不能超过10！");
            return;
        }
		var checkN=checkName(isModify, id);
		if(checkN){
			$("#span1").attr("style","color:red;display:inline");
			$("#span1").text("名称重复！");
			return;
		}
	}
	function goLabel(){
		$("#span1").attr("style","color:red;display:none");
		$("#span1").text("");
	}
    </script>
    <style type="text/css">
    	[class^="icon-"], [class*=" icon-"] {
       		background-position: -48px 0;
        	display: inline-block;
        	width: 14px;
        	height: 14px;
        	margin-top: 1px;
        	line-height: 14px;
        	vertical-align: text-top;
        	background-image: url("css/img/glyphicons-halflings.png");
        	background-repeat: no-repeat;
    	}
        .msg_div>div {
            padding-top: 8px;
            width: 280px;
            font-size: 12px;
            overflow: hidden;
        }
        .msg_div>div font {
            color: #ff0000;
            padding-right: 5px;
            position: absolute;
            left: 0;
        }
        .msg_div div>span {
            width: 75px;
            float: left;
            text-align: right;
            height: 28px;
            line-height: 28px;
            position: relative;
        }

        /*bug 16801 quauqadmin-后台数据维护-旅游区域-添加 ie8、9、10 样式错误*/
        /*添加了\9的css，同时将搜索的图标修改*/
        .msg_div div .inputTxt {
            width:200px;
            height: 28px;
            line-height: 28px;
            width: 185px\9;
            height: 20px\9;
            line-height: 20px\9;
        }
        .msg_div P{
            padding-top: 0px;
        }
        .msg_div P .inputTxt{
            width: 185px\9;
            height: 20px\9;
            line-height: 20px\9;
        }
        .searchImg{
            width: 22px;
            height: 26px;
            background: url(/static/images/glyphicons-halflings.png) no-repeat;
            background-position: -39px 7px;
            display: inline-block;
            position: absolute;
            right: 12px;
            cursor: pointer;
        }
        #tourInOut{
            width:200px;
            height: 28px;
            line-height: 28px;
        }
        /*后台数据维护-旅游区域-添加面板，线路信息提示样式修改*/
        #label{
            margin-bottom: 20px;
        }
        #span1{
            margin-left: 72px;
            margin-top: -18px;
            line-height: 14px;
            height: 14px;
        }
        /*bug 16801*/
    </style>

</head>

<body>
<form novalidate="novalidate" id="companyDictForm" action="${ctx }/sys/district/saveOrUpdate" method="post" target="_parent">
    <div class="msg_div">
        <!-- <input id="value" name="value" value="" type="hidden"/> -->
        <input type="hidden" name="id" value="${id }" id="id"/>
        <p>
            <span class="widthQudao"><font>*</font>区域名称 ：</span>
            <input id="label" class="inputTxt" name="name" value="${name }" maxlength="10" type="text"  onblur="checklabel()" onfocus="goLabel()">
            <span id="span1" style="color:red;display:none;"></span>
        </p>
        <p>
            <span class="widthQudao"><font>*</font>所属分类 ：</span>
            <select class="inputTxt" id="tourInOut" name="tourInOut">
                <option value="100000" <c:if test="${tourInOut eq 100000}">selected</c:if>>出境游</option>
                <option value="200000" <c:if test="${tourInOut eq 200000}">selected</c:if>>国内游</option>
            </select>
        </p>
        <div style="position: relative;">
        	<span class="widthQudao"><font>*</font>相关城市 ：</span>
        	<input id="search_condition" class="inputTxt" name="sort" value="" maxlength="10" type="text"><i class="searchImg" onclick="search_ztree('departTree', 'search_condition')"></i>
            <input id="destinationIds" name="destinationIds" value="${destinationIds }" type="hidden">
            <div style="margin-left: 68px;">
                <ul id="departTree" class="ztree"></ul>
            </div>
        </div>
        <p style="margin-left:80px;">
            <input class="btn btn-primary" value="确定" type="button"  id="add"  style="background:#5f7795;" onclick="save()">
            <input class="btn btn-primary gray" value="取消" onclick="window.parent.window.jBox.close()" type="button" style="background:#5f7795;">
        </p>
    </div>
    
</form>


</body></html>