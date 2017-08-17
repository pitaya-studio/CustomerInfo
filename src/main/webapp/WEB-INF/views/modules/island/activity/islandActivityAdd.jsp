<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>订单-单团-销售订单-返佣详情</title>
    <%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.css">
    <!--树形插件的样式-->
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.mCustomScrollbar.css" />
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
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
      $(function(){
      	$("#thirdStepDiv .mod_information_d8_2 select[name='country']").comboboxSingle();
      	//显示联运/分段联运价格
      	islandShowPrice();
      	//搜索条件筛选
      	launch();
      	//产品名称文本框提示信息
      	inputTips();
      	//操作浮框
      	operateHandler();
      });
      //删除已上传的文件
      function deleteFile(thisDom,fileID){
      	$(thisDom).parent("li").remove();
      }
    </script>
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
      	var zNodes =[];
         $.ajax({
		            type: "POST",
		            url: "${ctx}/sys/user/deptUserList",
		            
		            success: function(msg){
		            	zNodes = JSON.parse(msg);
		            	initDataTree();
		            }
		        });
		function initDataTree(){
			// 初始化树结构
	      	tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
	      	// 默认展开全部节点
	      	tree.expandAll(true);
	      	//美化滚动条
	      	var $_roleLeftCen=$('.role-leftCen');
	      	var $_roleRightCen=$('.role-rightCen');
	      	
	      	$_roleLeftCen.mCustomScrollbar();
	      	$_roleRightCen.mCustomScrollbar();
		
		}      	
      	
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
	<!-- 顶部参数 -->
    <page:applyDecorator name="show_head">
	    <page:param name="desc">海岛游产品发布</page:param>
	</page:applyDecorator>
	
	<div class="produceDiv">
              <div style="width:100%; height:20px;"></div>
              <!--产品信息开始-->
              <div class="mod_information" id="ofAnchor1">
                <div class="mod_information mar_top0" id="secondStepDiv">
                  <div class="ydbz_tit pl20 secondStepTitle mar_top0">基本信息
                    <span id="ofAnchor5"></span>
                    <span style="float: right;padding-right: 10px;" data-target="#secondStepEnd">收起</span>
                  </div>
                  <div style="margin-top:8px;" id="secondStepEnd">
                    <div class="activitylist_bodyer_right_team_co2 wpr20 pr" style="height:40px;">
                      <div class="activitylist_team_co3_text">产品名称：</div>
                      <input type="text" value="" class="inputTxt inputTxtlong" name="orderNum" id="orderNum" flag="istips">
                      <span class="ipt-tips">命名规则 行程时间-海岛-交通方式</span>
                    </div>
                    <div class="kong"></div>
                    <div class="activitylist_bodyer_right_team_co1">
                      <div class="activitylist_team_co3_text">产品编号：</div>
                      <input type="text" value="" name="orderPersonName" class="inputTxt" id="orderPersonName">
                    </div>
                    <div class="activitylist_bodyer_right_team_co1">
                      <label>币种选择：</label>
                      <select>
                        <option>人民币</option>
                        <option>美元</option>
                      </select>
                    </div>
                    <div class="kong"></div>
                    <div class="activitylist_bodyer_right_team_co1">
                      <div class="activitylist_team_co3_text">国家：</div>
                      <select id="country">
                        <option>中国</option>
                        <option>日本</option>
                        <option>美国</option>
                      </select>
                    </div>
                    <div class="activitylist_bodyer_right_team_co1">
                      <div class="activitylist_team_co3_text">岛屿：</div>
                      <select id="island">
                        <option>太阳岛</option>
                        <option>西西岛</option>
                      </select>
                    </div>
                    <div class="activitylist_bodyer_right_team_co1">
                      <div class="activitylist_team_co3_text">酒店名称：</div>
                      <select id="hotel">
                        <option>希尔顿</option>
                        <option>五星</option>
                      </select>
                      <input value="查看价单" onclick="" class="btn btn-primary gray btn_sjdss10" type="button">
                    </div>
                    <!--查询结果筛选条件排序开始-->
                    <div class="filterbox add_new_tq">
                      <div class="filter_btn">
                        <a class="btn btn-primary" href="#" onclick="jbox_haidaoyou_fab();">新增团期</a>
                      </div>
                    </div>
                    <!--查询结果筛选条件排序结束-->
                    <table id="contentTable" class="table activitylist_bodyer_table sea_rua_table">
                      <thead>
                        <tr>
                          <th width="10%">团号
                            <br />日期</th>
                          <th width="10%">参考航班</th>
                          <th width="9%">房型*晚数</th>
                          <th width="7%">余位/预收</th>
                          <th width="10%">同行价格</th>
                          <th width="8%">单房差</th>
                          <th width="8%">需交定金</th>
                          <th width="5%">操作</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr>
                          <td class="tc">
                            <a href=".html" target="_blank">EFX-141208-DV</a>
                            <br />2015.05.11</td>
                          <td class="tc">MU1791</td>
                          <td class="tc">水上屋*4晚
                            <br />沙滩屋*2晚</td>
                          <td class="tc">
                            <span class="or_color">15</span>/99</td>
                          <td class="tr">
                            <p>成人：¥
                              <span class="tdred fbold">2,220.00</span>
                            </p>
                            <p>第三人：¥
                              <span class=" fbold">2,220.00</span>
                            </p>
                            <p>儿童：¥
                              <span class=" fbold">2,220.00</span>
                            </p>
                            <p>婴儿：¥
                              <span class=" fbold">2,220.00</span>
                            </p>
                          </td>
                          <td class="tr">¥
                            <span class=" fbold">2,220.00</span>
                          </td>
                          <td class="tr">¥99,999.99</td>
                          <td class="p0">
                            <dl class="handle">
                              <dt>
                                <img title="操作" src="images/handle_cz.png">
                              </dt>
                              <dd>
                                <p>
                                  <span></span>
                                  <a href="javascript:void(0)">修改</a>
                                  <a href="javascript:void(0)" onclick="return confirmxDel('要下架该产品吗？', 2)">下架</a>
                                  <a href="javascript:void(0)" onclick="return confirmxDel('要删除该产品吗？', 2)">删除</a>
                                  <a href="产品-酒店产品发布-酒店产品详情.html" target="_blank">详情</a>
                                </p>
                              </dd>
                            </dl>
                          </td>
                        </tr>
                        <tr>
                          <td class="tc">
                            <a href=".html" target="_blank">EFX-141208-DV</a>
                            <br />2015.05.11</td>
                          <td class="tc">MU1791</td>
                          <td class="tc">水上屋*4晚
                            <br />沙滩屋*2晚</td>
                          <td class="tc">
                            <span class="or_color">15</span>/99</td>
                          <td class="tr">
                            <p>成人：¥
                              <span class="tdred fbold">2,220.00</span>
                            </p>
                            <p>第三人：¥
                              <span class=" fbold">2,220.00</span>
                            </p>
                            <p>儿童：¥
                              <span class=" fbold">2,220.00</span>
                            </p>
                            <p>婴儿：¥
                              <span class=" fbold">2,220.00</span>
                            </p>
                          </td>
                          <td class="tr">¥
                            <span class=" fbold">2,220.00</span>
                          </td>
                          <td class="tr">¥99,999.99</td>
                          <td class="p0">
                            <dl class="handle">
                              <dt>
                                <img title="操作" src="images/handle_cz.png">
                              </dt>
                              <dd>
                                <p>
                                  <span></span>
                                  <a href="javascript:void(0)">修改</a>
                                  <a href="javascript:void(0)" onclick="return confirmxDel('要下架该产品吗？', 2)">下架</a>
                                  <a href="javascript:void(0)" onclick="return confirmxDel('要删除该产品吗？', 2)">删除</a>
                                  <a href="产品-酒店产品发布-酒店产品详情.html" target="_blank">详情</a>
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
                <div class="ydbz_tit pl20 secondStepTitle mar_topnone10" id="ofAnchor6">上传资料
                  <span style="float: right;padding-right: 10px;" data-target="div.mod_information_3">收起</span>
                </div>
                <div class="mod_information_3">
                  <div class="batch">
                    <label class="batch-label company_logo_pos">产品行程介绍：</label>
                    <input type="button" class="mod_infoinformation3_file" value="上传文件">
                    <ol class="batch-ol">
                      <li>
                        <span>新建 Microsoft Office Word 文档.docx产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍产品行程介绍</span>
                        <a
                        onclick="deleteFile(this,4433)" href="javascript:void(0)" class="batchDel">删除</a>
                      </li>
                      <li>
                        <span>产品行程介绍.docx</span>
                        <a onclick="deleteFile(this,4433)" href="javascript:void(0)" class="batchDel">删除</a>
                      </li>
                      <li>
                        <span>产品行程介绍.docx</span>
                        <a onclick="deleteFile(this,4433)" href="javascript:void(0)" class="batchDel">删除</a>
                      </li>
                    </ol>
                  </div>
                  <div class="mod_information_d7"></div>
                  <div class="batch" style="margin-top:10px;">
                    <label class="batch-label">自费补充协议：</label>
                    <input type="button" class="mod_infoinformation3_file" value="上传文件">
                    <ol class="batch-ol">
                      <li>
                        <span>新建 Microsoft Office Word 文档.docx</span>
                        <a onclick="deleteFile(this,4433)" href="javascript:void(0)" class="batchDel">删除</a>
                      </li>
                      <li>
                        <span>产品行程介绍.docx</span>
                        <a onclick="deleteFile(this,4433)" href="javascript:void(0)" class="batchDel">删除</a>
                      </li>
                      <li>
                        <span>产品行程介绍.docx</span>
                        <a onclick="deleteFile(this,4433)" href="javascript:void(0)" class="batchDel">删除</a>
                      </li>
                    </ol>
                  </div>
                  <div class="mod_information_d7"></div>
                  <div class="batch" style="margin-top:10px;">
                    <label class="batch-label">其他补充协议：</label>
                    <input type="button" class="mod_infoinformation3_file" value="上传文件">
                    <ol class="batch-ol">
                      <li>
                        <span>新建 Microsoft Office Word 文档.docx</span>
                        <a onclick="deleteFile(this,4433)" href="javascript:void(0)" class="batchDel">删除</a>
                      </li>
                      <li>
                        <span>产品行程介绍.docx</span>
                        <a onclick="deleteFile(this,4433)" href="javascript:void(0)" class="batchDel">删除</a>
                      </li>
                      <li>
                        <span>产品行程介绍.docx</span>
                        <a onclick="deleteFile(this,4433)" href="javascript:void(0)" class="batchDel">删除</a>
                      </li>
                    </ol>
                  </div>
                  <div class="mod_information_d7"></div>
                </div>
                <div class="ydbz_tit pl20 secondStepTitle">其他信息
                  <span style="float: right;padding-right: 10px;" data-target="div.other_info">收起</span>
                </div>
                <div class="other_info">
                  <textarea class="add_new_seatu_qt"></textarea>
                </div>
                <div class="ydbz_tit pl20 secondStepTitle">产品分享
                  <span style="float: right;padding-right: 10px;" data-target="div.product_share">收起</span>
                </div>
                <div class="product_share">
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
                          <span>已添加
                            <em>0</em>个城市</span>
                          <p>
                            <i></i>清空</p>
                        </div>
                        <div class="role-rightCen">
                          <ul id="addArea"></ul>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="release_next_add">
                  <input type="button" value="取消" onclick="javascript:window.location.href='/trekiz_wholesaler/a/activity/manager/list/2'"
                  class="btn btn-primary gray">
                  <input type="button" value="保存" onclick="submitForm(1);" class="btn btn-primary">
                  <input type="button" value="提交" onclick="jboxReleaseSelect();" class="btn btn-primary">
                </div>
              </div>
              <!--第三步上传资料结束-->
            </div>
	<!--右侧内容部分结束-->
</body>
</html>