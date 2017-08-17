<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<head>
<meta name="decorator" content="wholesaler" />
<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
<title>
	<c:if test="${newORold==0 }">公司信息-新增第1步-基本信息</c:if>
	<c:if test="${newORold==1 }">公司信息-修改第1步-基本信息</c:if>
</title>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"  type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/ckeditor/ckeditor.js"></script>
<script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>

<!--地区树形插件-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.css"><!--树形插件的样式-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.mCustomScrollbar.css" /><!--滚动条插件样式-->
<script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.js"></script><!--树形插件的脚本-->
<script type="text/javascript" src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.excheck-3.5.js"></script><!--树形插件的脚本-->
<script type="text/javascript" src="${ctxStatic}/js/jquery.mousewheel.min.js"></script><!--滚动条插件脚本-->
<script type="text/javascript" src="${ctxStatic}/js/jquery.mCustomScrollbar.js"></script><!--滚动条插件脚本-->
<script type="text/javascript">
/** 境内覆盖区域  */
var tree,tree2, $key,$key2, lastValue = "",lastValue2 = "", nodeList = [],nodeList2 = [];
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

	function beforeClick(id, node) {
		tree2.checkNode(node, !node.checked, true, true);
		return false;
	}
	
	// 用户-菜单	
	var zNodes =eval($("#treeData").val());
	var zNodes2 =eval($("#treeData2").val());
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
   tree2 = $.fn.zTree.init($("#menuTree2"), setting, zNodes2);
	/*/ 默认选择节点
	var id = $("#searchId").val();
	$(("#"+id),window.parent.document).find("a").each(function(){		
		var node = tree.getNodeByParam("id", $(this).attr("value"));
		try{tree.checkNode(node, true, true);
		    onCheck('',$(this).attr("value"),node);
		}catch(e){}
	});*/
	// 默认选择节点
	// 国内
	var ids = $("#menuIds").val().split(",");
	for(var i=0; i<ids.length; i++) {
		var node = tree.getNodeByParam("id", ids[i]);
		try{tree.checkNode(node, true, true);
		onCheck('',ids[i],node);
		}catch(e){}
	}
	// 国外
	var ids2 = $("#menuIds2").val().split(",");
	for(var i=0; i<ids2.length; i++) {
		var node2 = tree2.getNodeByParam("id", ids2[i]);
		try{tree2.checkNode(node2, true, true);
		onCheck('',ids2[i],node2);
		}catch(e){}
	}
	
	// 默认展开全部节点
	tree.expandAll(false);
	tree2.expandAll(false);
	//美化滚动条
	var $_roleLeftCen=$('.role-leftCen');
	var $_roleRightCen=$('.role-rightCen');
	
	$_roleLeftCen.mCustomScrollbar();
	$_roleRightCen.mCustomScrollbar();
	
	function onCheck(e, treeId, treeNode) {
		var nodes = tree.getCheckedNodes(true);
		var nodes2 = tree2.getCheckedNodes(true);
		var $hasAdd = $("#addArea").find("li");
		var $hasAdd2 = $("#addArea2").find("li");
		var str_html = '';
		var str_html2 = '';
		//遍历添加新增选项(国内)
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
		//遍历添加新增选项(国外)
		for (var i = 0; i < nodes2.length; i++) {
        	//msg += nodes[i].name+"--"+nodes[i].id+"--"+nodes[i].pId+"\n";
			if(!nodes2[i].isParent){
				var isInclude = 0;
				$hasAdd2.each(function(index, element) {
					if(nodes2[i].tId == $(element).attr("forID")){
						isInclude = 1;
						return;
					}
				});
				if(!isInclude){
					str_html2 += '<li forID="' + nodes2[i].tId +'"><span>' + nodes2[i].name +'</span><i>X</i></li>';
				}
			}
        }
		
		//遍历删除选项(国内)
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
		//遍历删除选项（国外）
		$hasAdd2.each(function(index, element) {
            var isInclude = 0;
			for (var i = 0; i < nodes2.length; i++) {
				if(nodes2[i].tId == $(element).attr("forID")){
					isInclude = 1;
					return;
				}
			}
			if(!isInclude){
				$(element).remove();
			}
        });
		if("" != str_html2){
			$("#addArea2").prepend(str_html2);
		}
		
		//设置已添加城市数为0
		$(".role-rightTop span em").text($("#addArea li").length);
		$("#role-rightTop2 span em").text($("#addArea2 li").length);
	}
	
	//删除已选择项目(国内)
	$("#addArea").on("click","li i",function(){
		var $li = $(this).parent("li");
		var treeNode = tree.getNodeByTId($li.attr("forID"));
		tree.checkNode(treeNode,false,true);
		$li.remove();
	});
	//删除已选择项目（国外）
	$("#addArea2").on("click","li i",function(){
		var $li = $(this).parent("li");
		var treeNode2 = tree2.getNodeByTId($li.attr("forID"));
		tree2.checkNode(treeNode2,false,true);
		$li.remove();
	});
	
	//清空已添加的城市(国内)
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
	//清空已添加的城市（国外）
	$("#role-rightTop2 p").click(function(){
		$.jBox.confirm("确定要清空数据吗？","提示",function(v,h,f){
			if (v == 'ok') {
				//取消结点树所有的可选项
				tree2.checkAllNodes(false);
				//清除已选择项目
				$("#addArea2").empty();
				//设置已添加城市数为0
				$("#role-rightTop span em").text(0);
			}
		});
	});
	
	//搜索(国内)
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
	
	//搜索(国外)
	$key2 = $("#key2");
	$key2.val("").focus(function(e){
		if ($key2.hasClass("empty")) {
			$key2.removeClass("empty");
		}
	}).blur(function(e){
		if ($key2.get(0).value === "") {
			$key2.addClass("empty");
		}
		searchNode(e);
	}).bind("change keydown cut input propertychange", searchNode2);
});

//提交表单前进行 区域范围的 数据处理
function toSubmit(){
	//获取全部勾选的项目
	// 国内
	var nodesChecked = tree.getCheckedNodes(true);
	var arrayID = [];
	for(var i=0;i<nodesChecked.length;i++){
		if(!nodesChecked[i].isParent){
			arrayID.push(nodesChecked[i].id);
		}
	}
	$("[name='menuIds']").val(arrayID);
	console.log("1:"+arrayID);
	// 国外
	var nodesChecked2 = tree2.getCheckedNodes(true);
	var arrayID2 = [];
	for(var i=0;i<nodesChecked2.length;i++){
		if(!nodesChecked2[i].isParent){
			arrayID2.push(nodesChecked2[i].id);
		}
	}
	$("[name='menuIds2']").val(arrayID2);

	console.log("2:"+arrayID2);
	
	//提交表单
	//$("#inputForm")[0].submit();
}

//搜索（国内）
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
//搜索（国外）
function searchNode2(e) {
	// 取得输入的关键字的值
	var value2 = $.trim($key2.get(0).value);
	
	// 按名字查询
	var keyType2 = "name";
	if ($key2.hasClass("empty")) {
		value2 = "";
	}
	
	// 如果和上次一致，就退出不查了。
	if (lastValue2 === value2) {
		return;
	}
	
	// 保存最后一次
	lastValue2 = value2;
	
	// 如果要查空字串，就退出不查了。
	if (value2 === "") {
		return;
	}
	updateNodes2(false);
	nodeList2= tree2.getNodesByParamFuzzy(keyType2, value2);
	updateNodes2(true);
}
function updateNodes2(highlight) {
	for(var i=0, l=nodeList2.length; i<l; i++) {
		nodeLis2t[i].highlight = highlight;				
		tree2.updateNode(nodeList2[i]);
		tree2.expandNode(nodeList2[i].getParentNode(), true, false, false);
	}
}

</script>
<!--供应商模块的脚本-->
<script type="text/javascript" src="${ctxStatic}/js/tmp.supplier.js"></script>
<script type="text/javascript">
$(function(){
	//上传动作
	btfile();
	// 初始化地理位置
	changePosition($(".domesticOverseas"),'country');
	// 添加备注
	$("#textRemarks").blur(function(){
		var str = $("#textRemarks").attr("value");
		str = $.trim(str);
		$("input[name=remarks]").val(str);
	});
	/*/ 提交联系人
	$("input[name=saveContact]").click(this,function(event){
		contactForm(event);
	});*/
	// 返回上一步
	$("#back").click(function(){
		javascript:history.go(-1);
	});
	// 下一步
	$("#next").click(function(){
		$("input[name=saveOrNext]").val(0);
		valForm();
	});
	// 提交
	$("#save").click(function(){
		$("input[name=saveOrNext]").val(1);
		valForm();
	});
	// 区号修正
	$("input.districtCode").blur(function(){
		var districtCode = $(this).val();
		districtCode = $.trim(districtCode);
		if(districtCode){
			$("input.districtCode").each(function(){
				$(this).val(districtCode);
			});
			$("input[name=districtCode]").val(districtCode);
		}
	})
});

//境内境外 改变地址展现形式
function changePosition(obj,name){
	
	var doVal=$('.domesticOverseas option:selected').val();
	if(doVal == 1){
		$(".sysselect_s").html('');
		$(".sysselect_s").append("<select name='country' id='country' onchange=\"getGeoSelect('',this,'province');\"></select>");
		$(".sysselect_s").append("<select name='province' id='province' onchange=\"getGeoSelect('',this,'city');\"></select>");
		$(".sysselect_s").append("<select name='city' id='city' onchange=\"getGeoSelect('',this,'district');\"></select>");
		$(".sysselect_s").append("<select name='district' id='district'></select>");
		$(".sysselect_s").append("<input type='text' name='shortAddress' id='shortAddress'>");
		getGeoSelect($(obj).val(),'',name);
		
		var shortAddress = "${office.address}";
		if(shortAddress){
			$("#shortAddress").val(shortAddress);
		}
	}else if(doVal == 2){
		$(".sysselect_s").html('');
		$(".sysselect_s").append("<select name='country' id='country' onchange=\"getGeoSelect('',this,'province');\"></select>");
		$(".sysselect_s").append("<select name='province' id='province' ></select>");
		$(".sysselect_s").append("<input type='text' name='shortAddress' id='shortAddress'>");
		getGeoSelect($(obj).val(),'',name);
		
		var shortAddress = "${office.address}";
		if(shortAddress){
			$("#shortAddress").val(shortAddress);
		}
	}else{
		$(".sysselect_s").html('');
	};
}

function getGeoSelect(type,obj,name){
	
	if((type||obj)&&name){
		$.ajax({
			type: "POST",
		   	url: "${ctx}/geography/getGeoListAjax",
		   	data: {
				"type":type,
				"parentId":$(obj).val()
			},
			dataType: "json",
		   	success: function(data){
		   		$("#"+name).empty();
		   		$("#"+name).append("<option value=''>请选择</option>");
		   		if(data){
		   			var val="";
	   			
		   			if(name=="country"){
		   				val="${office.countryId}";
	   				}else if(name=="province"){
		   				val="${office.provinceId}";
	   				}else if(name=="city"){
		   				val="${office.cityId}";
	   				}else if(name=="district"){
		   				val="${office.districtId}";
	   				}
		   			$.each(data,function(){
		   				var selected="";
	   					if(val==this.uuid)selected="selected=\"selected\"";
	   					$("#"+name).append("<option value='"+this.uuid+"' "+selected+">"+this.nameCn+"</option>");
		   			})
		   		}
				if($("#"+name).val()!=""){
					getGeoSelect('',$("#"+name),tranferGeo(name));
				}
		   	}
		});
	}
}

function tranferGeo(obj){
	if(obj=='country'){
		return 'province';
	}else if(obj=='province'){
		return 'city';
	}else if(obj=='city'){
		return 'district';
	}
	return '';
} 	

// 提交联系人表单
function contactForm(obj){
	var the_param = "&contactName="+$(obj).parents("p.shopPeopleP").find("input[name=contactName]").val();
	the_param+="&contactMobile="+$(obj).parents("p.shopPeopleP").find("input[name=contactMobile]").val();
	the_param+="&contactPhone="+$(obj).parents("p.shopPeopleP").find("input[name=contactPhone]").val();
	the_param+="&contactFax="+$(obj).parents("p.shopPeopleP").find("input[name=contactFax]").val();
	the_param+="&contactQQ="+$(obj).parents("p.shopPeopleP").find("input[name=contactQQ]").val();
	the_param+="&contactEmail="+$(obj).parents("p.shopPeopleP").find("input[name=contactEmail]").val();
	the_param+="&supplierId=${office.id}"; // 指定批发商ID
	if($(obj).parents("p.shopPeopleP").find("input[name=id]").val()){ // 判断该联系人是否已经存在
		the_param+="&id="+$(obj).parents("p.shopPeopleP").find("input[name=id]").val();
	}
	
	// 校验联系人
	var n=0;
	var contactName = $(obj).parents("p.shopPeopleP").find("input[name=contactName]").val();
	if(!contactName){
		$.jBox.tip("联系人不可为空","info");
		n++;
	}
	var contactMobile =$(obj).parents("p.shopPeopleP").find("input[name=contactMobile]").val();
	var numReg = new RegExp("^[0-9]*$");
	if(!numReg.test(contactMobile)){
		$.jBox.tip("手机号码只能为数字","info");
		n++;
	}
	var contactPhone=$(obj).parents("p.shopPeopleP").find("input[name=contactPhone]").val();
	if(!numReg.test(contactPhone)){
		$.jBox.tip("固定电话号码只能为数字","info");
		n++;
	}
	var contactFax=$(obj).parents("p.shopPeopleP").find("input[name=contactFax]").val();
	if(!numReg.test(contactFax)){
		$.jBox.tip("传真号码只能为数字","info");
		n++;
	}

	var contactQQ=$(obj).parents("p.shopPeopleP").find("input[name=contactQQ]").val();
	var numReg = new RegExp("^[0-9]*$");
	if(!numReg.test(contactQQ)){
		$.jBox.tip("QQ号码只能为数字","info");
		n++;
	}

	var contactEmail=$(obj).parents("p.shopPeopleP").find("input[name=contactEmail]").val();
	var emailReg = new RegExp("/^/w+((-/w+)|(/./w+))*/@[A-Za-z0-9]+((/.|-)[A-Za-z0-9]+)*/.[A-Za-z0-9]+$/");
	if(!emailReg.test(contactEmail)){
		$.jBox.tip("邮箱格式不正确","info");
		n++;
	}
	if(n==0){
		ajaxContact(the_param);
	}
}

// ajax 提交批发商联系人
function ajaxContact(the_param){
	$.ajax({
		type : "POST",
		url : contextPath+"/company/saler/addNewContact",
		data : the_param,
		dataType : "text",
		success:function(html){
			var json;
			try{
				json = $.parseJSON(html);
			}catch(e){
				json = {res:"error"};
			}
			if(json.res=="success"){
				jBox.tip("提交成功", 'info');
				// 保存成功后，将联系人ID回填，以备修改
				$(obj).parents("p.shopPeopleP").find("input[name=id]").val(json.supplyContacts.id);
			}else if(json.res=="data_error"){
				jBox.tip(json.mes,"info");
			}else{
				jBox.tip("系统繁忙，请稍后再试", 'error');
			}
		}
	});
}


// 删除批发商联系人
function delContact(obj){
	var param = "&id="+$(obj).parents("p.shopPeopleP").find("input[name=id]").val();
	if(param){ // 如果已经填写联系人，则用ajax删除数据库(有ID存在，说明该联系人已经提交)
		$.ajax({
			type : "POST",
			url : contextPath+"/manage/saler/delNewContact",
			data : param,
			dataType : "text",
			success:function(html){
				var json;
				try{
					json = $.parseJSON(html);
				}catch(e){
					json = {res:"error"};
				}
				if(json.res=="success"){
					jBox.tip("删除成功", 'info');
				}else if(json.res=="data_error"){
					jBox.tip(json.mes,"info");
				}else{
					jBox.tip("系统繁忙，请稍后再试", 'error');
				}
			}
		})
	}
	// 删除页面
	shopPeopleDel(obj);
}

// 批发商表单验证
function valForm(){
	var n= 0;
	var ch = $("input[name=typeUuid]:checked");
	var supplierBrand = $("input[name=supplierBrand]").val();
	if(ch.length<1){
		$.jBox.tip("请选择批发商类型",'info');
		n++;
	}
	if(!supplierBrand){
		$.jBox.tip("请输入批发商品牌",'info');
		n++;
	}
	var districtCode = $("input[name=districtCode]").val();
	var numReg = new RegExp("^[0-9]*$");
	if(!numReg.test(districtCode)){
		$.jBox.tip("分区号只能为数字","info");
		n++;
	}
	var phone = $("input[name=phone]").val();
	if(!numReg.test(phone)){
		$.jBox.tip("电话号码只能为数字","info");
		n++;
	}
	var fax = $("input[name=fax]").val();
	if(!numReg.test(fax)){
		$.jBox.tip("传真号码只能为数字","info");
		n++;
	}
	
	if(n==0){
		subForm();
	}
}

// 提交批发商基础信息表单
function subForm(){
	toSubmit(); // 对国内，国外的覆盖区域进行整理。
	var the_param = $("#subWholeForm").serialize();
	console.log(the_param);
	$.ajax({
		type : "POST",
		url : contextPath+"/company/saler/addWholeOfficeOne",
		data : the_param,
		dataType : "text",
		success:function(html){
			var json;
			try{
				json = $.parseJSON(html);
			}catch(e){
				json = {res:"error"};
			}
			if(json.res=="success"){
				jBox.tip("提交成功", 'info');
				var saveOrnext = $("input[name=saveOrNext]").val();
				if(saveOrnext==0){
					// 进入下一步
					location.href =contextPath + "/company/saler/gotoAddWholeOfficeTwo/${office.id}/${newORold}";
				}else if(saveOrnext==1){
					// 跳回批发商列表
					location.href =contextPath + "/company/saler/salerlist";
				}
			}else if(json.res=="data_error"){
				jBox.tip(json.mes,"info");
			}else{
				jBox.tip("系统繁忙，请稍后再试", 'error');
			}
		}
	})
}

</script>
</head>
<body>
        <!--右侧内容部分开始-->
		<div class="supplierLine">
			<a href="javascript:void(0)" class="select">基本信息填写</a>
			<a href="javascript:void(0)">网站信息</a>
			<a href="javascript:void(0)">银行账户</a>
			<a href="javascript:void(0)">资质上传</a>
			<!-- 用于区分新增/修改批发商；0：新增；1：修改; 默认为新增 -->
			<input type="hidden"  name="newORold" value="${newOrold }"/>
		</div>
        <form class="form-horizontal" id="subWholeForm"  name="subForm">
          <div class="sysdiv sysdiv_coupon qdgl-cen">
          <!-- 
             <div class="control-group">
				<label class="control-label"><span style="color:#f00;">*</span>&nbsp;上级节点:</label>
				<div class="controls">
	                <tags:treeselect id="office" name="parentId" value="${office.parent.id}" labelName="theName" labelValue="${office.parent.name}"
						title="批发商" url="/sys/office/treeData" extId="${office.id}" cssClass="required"/>
				</div>
			</div>
			-->
            <p>
              <label><em class="xing">*</em>批发商类型：</label>
              <span class="checkboxdiv">
              	<select name="frontier" class="domesticOverseas required" onchange="changePosition(this,'country');" style="width:100px">
              		<c:if test="${office.frontier ==1 }">
						<option value="1" selected="selected">境内批发商</option>
						<option value="2">境外批发商</option>
              		</c:if>
              		<c:if test="${office.frontier ==2 }">
						<option value="1" >境内批发商</option>
						<option value="2" selected="selected">境外批发商</option>
              		</c:if>
				</select>
			  </span>
              <span class="checkboxdiv">
	              <c:if test="${not empty typeList }">
	              	<c:forEach items="${typeList }" var ="wholetype">
	              		<input type="checkbox" name="typeUuid"  value="${wholetype.uuid }" 
	              			<c:forEach items="${officeTypeList }" var="officetype">
	              				<c:if test="${officetype.sysdefinedictUUID == wholetype.uuid }">checked="checked"</c:if>
	              			</c:forEach>
	              		/>
	              		<label for="typeUuid">${wholetype.label }</label>
	              	</c:forEach>
	              </c:if>
              </span>
			</p>
			<!-- 
			<p>
              <label><em class="xing">*</em>批发商等级：</label>
              <span>
             	<select name="levelUuid">
             		<c:forEach items="${levelList }" var="level">
             			<option value="${level.uuid }" <c:if test="${office.level.uuid == level.uuid }">selected</c:if>>${level.label }</option>
             		</c:forEach>
             	</select>
              </span>
			</p>
			<p>
				<label>是否启用：</label>
				<span>
					<select name="status">
	             		<option value="1" <c:if test="${office.status==1 }">selected</c:if>>启用</option>
	             		<option value="2" <c:if test="${office.status==2 }">selected</c:if>>停用</option>
	             	</select>
             	</span>
			</p>
			 -->
            <p>
              <label><em class="xing">*</em>批发商品牌：</label>
              <span>
              	<input type="text" maxlength="50" name="supplierBrand"  value="${office.supplierBrand }">
              </span>
			</p>
            <p>
              <label>公司名称：</label>
              <span>
              <input type="text" maxlength="50" name="companyName"  value="${office.companyName }">
              </span>
			</p>
            <p>
              <label>英文名称：</label>
              <span>
              <input type="text" maxlength="50" name="enname"  value="${office.enname }">
              </span>
			</p>
			<!-- 
            <p>
              <label>批发商名称：</label>
              <span>
              <input type="text" maxlength="50" name="name"  value="${office.name }">
              </span>
			</p>
            <p>
              <label>批发商编码：</label>
              <span>
              <input type="text" maxlength="50" name="code"  value="${office.code }"/>
              </span>
			</p> -->
			<!--  境内覆盖区域begin -->
			<div class="coverarea">
				 <label class="coverarea-label">境内覆盖区域：</label>
                  <div class="role">
                      <div class="role-left">
                          <div class="role-leftTop">
                              <input type="text" id="key" name="key" />
                          </div>
                          <div class="role-leftCen">
                              <ul id="menuTree" class="ztree" style="margin-top:3px;"></ul>
                              <input id="menuIds" name="menuIds" type="hidden" value="${office.areaInternal }"/>
                          </div>
                      </div>
                      <div class="role-right">
                          <div class="role-rightTop" >
                              <span>已添加<em>0</em>个城市</span>
                              <p><i></i>清空</p>
                          </div>
                          <div class="role-rightCen">
                              <ul id="addArea"></ul>
                          </div>
                      </div>    
                  </div>
              </div>
			<!--  境内覆盖区域end -->
			<!--  境外覆盖区域begin -->
			<div class="coverarea">
				 <label class="coverarea-label">境外覆盖区域：</label>
                  <div class="role">
                      <div class="role-left">
                          <div class="role-leftTop">
                              <input type="text" id="key2" name="key2" />
                          </div>
                          <div class="role-leftCen">
                              <ul id="menuTree2" class="ztree" style="margin-top:3px;"></ul>
                              <input id="menuIds2" name="menuIds2" type="hidden" value="${office.areaOverseas }"/>
                          </div>
                      </div>
                      <div class="role-right">
                          <div class="role-rightTop" id="role-rightTop2">
                              <span>已添加<em>0</em>个城市</span>
                              <p><i></i>清空</p>
                          </div>
                          <div class="role-rightCen">
                              <ul id="addArea2"></ul>
                          </div>
                      </div>    
                  </div>
                <input  type="hidden" id="treeData2"  value="${treeOutsideData}"/>   
              </div>
			<!--  境外覆盖区域end -->
			<p class="domestic">
				<label>公司地址：</label> 
				<span class="sysselect_s"> 
				
				</span>
			</p>
            <p>
              <label>电话：</label>
              <input  type="hidden" name="districtCode"  value="${office.districtCode }"/>
              <span><input type="text" class="sysinput_s districtCode"  value="${office.districtCode }"/>
              <span class="sysinput_span">-</span>
              <input type="text" class="inputTxt" name="phone" value="${office.phone }"/>例如：010-87475943</span>
			</p>
            <p>
              <label>传真：</label>
             <span><input type="text" class="sysinput_s districtCode"  value="${office.districtCode }"/>
             <span class="sysinput_span">-</span>
             <input type="text" class="inputTxt" name="fax" value="${office.fax }"/>例如：010-87475943</span>
			</p>
			<input type="hidden" name="id" value="${office.id }"/>
			<input type="hidden" name="remarks" value="${office.remarks }"/>
			<input type="hidden" name="saveOrNext" value="0"/>
        </div>
     </form>
     
     <input id="treeData" type="hidden"  value="${treeData}"/>   
     	<div class="sysdiv sysdiv_coupon qdgl-cen">
             <p class="shopPeopleP">
             		<input type="hidden" name="id"  value="${firstSupply.id }""/>
	             	<label>联系人<em>1</em>：</label>
	               <span><input type="text" name="contactName" value="${firstSupply.contactName }"></span>
	               <label>手机：</label>
	               <span><input type="text" name="contactMobile"  value="${firstSupply.contactMobile }"/></span>
	               <a class="ydbz_x" onclick="shopPeopleAdd(this)">添加</a>
	               <span class="kongr20"></span>
	               <label>固定电话：</label>
	               <span><input type="text" name="contactPhone"  value="${firstSupply.contactPhone }"/></span>
	               <label>传真：</label>
	               <span><input type="text" name="contactFax"  value="${firstSupply.contactFax }"/></span>
	               <span class="kongr20"></span>
	               <label>Email：</label>
	               <span><input type="text" name="contactEmail"  value="${firstSupply.contactEmail }"/></span>
	               <label>QQ：</label>
	               <span><input type="text" name="contactQQ"  value="${firstSupply.contactQQ }"/></span>
	               <span><input type="button" class="ydbz_x"  name="saveContact" value="提交" onclick="contactForm(this)"/></span>
             </p>
              <p class="shopPeopleP">
              		<input type="hidden" name="id"  value="${secondSupply.id }"/>
	              	<label>联系人<em>2</em>：</label>
	               <span><input type="text" name="contactName"  value="${secondSupply.contactName }"></span>
	               <label>手机：</label>
	               <span><input type="text" name="contactMobile"  value="${secondSupply.contactMobile }"></span>
	               <span class="kongr20"></span>
	               <label>固定电话：</label>
	               <span><input type="text" name="contactPhone"  value="${secondSupply.contactPhone }"></span>
	               <label>传真：</label>
	               <span><input type="text" name="contactFax"  value="${secondSupply.contactFax }"></span>
	               <span class="kongr20"></span>
	               <label>Email：</label>
	               <span><input type="text" name="contactEmail"  value="${secondSupply.contactEmail }"></span>
	               <label>QQ：</label>
	               <span><input type="text" name="contactQQ"  value="${secondSupply.contactQQ }"></span>
	               <span><input type="button" name="saveContact"  class="ydbz_x"  value="提交" onclick="contactForm(this)"/></span>
             </p>
             <p style="display:none" class="shopPeopleNone">
             		<input type="hidden" name="id"  value=""/>
	             	<label>联系人<em></em>：</label>
	               <span><input type="text" name="contactName"></span>
	               <label>手机：</label>
	               <span><input type="text" name="contactMobile"></span>
	               <a class="ydbz_x gray" onclick="delContact(this)">删除</a>
	               <span class="kongr20"></span>
	               <label>固定电话：</label>
	               <span><input type="text" name="contactPhone"></span>
	               <label>传真：</label>
	               <span><input type="text" name="contactFax"></span>
	               <span class="kongr20"></span>
	               <label>Email：</label>
	               <span><input type="text" name="contactEmail"></span>
	               <label>QQ：</label>
	               <span><input type="text" name="contactQQ"></span>
	               <span><input type="button" name="saveContact"  class="ydbz_x"  value="提交" onclick="contactForm(this)"/></span>
              </p>
          </div>
          <!-- 用于从第二步回退回第一步，或者修改批发商时使用，增加其他联系人 -->
          <c:forEach items="${supplyContactsList }"  var="contact"  varStatus="cindex">
	          <div class="sysdiv sysdiv_coupon qdgl-cen">
	             <p style="" class="shopPeopleP">
	             		<input type="hidden" name="id"  value="${contact.id }"/>
		             	<label>联系人<em>${cindex.index+3 }</em>：</label>
		               <span><input type="text" name="contactName" value="${contact.contactName }"></span>
		               <label>手机：</label>
		               <span><input type="text" name="contactMobile"  value="${contact.contactMobile }"></span>
		               <a class="ydbz_x gray" onclick="delContact(this)">删除</a>
		               <span class="kongr20"></span>
		               <label>固定电话：</label>
		               <span><input type="text" name="contactPhone" value="${contact.contactPhone }"></span>
		               <label>传真：</label>
		               <span><input type="text" name="contactFax" value="${contact.contactFax }"></span>
		               <span class="kongr20"></span>
		               <label>Email：</label>
		               <span><input type="text" name="contactEmail" value="${contact.contactEmail }"></span>
		               <label>QQ：</label>
		               <span><input type="text" name="contactQQ" value="${contact.contactQQ }"></span>
		               <span><input type="button" name="saveContact"  class="ydbz_x"  value="提交" onclick="contactForm(this)"/></span>
	              </p>
	          </div>
          </c:forEach>
			<p>
              <label>描述：</label>
              <span>
              <textarea rows="3" class="input-xlarge" maxlength="200" id="textRemarks">${office.remarks }</textarea>
              </span>
			</p>
         <div class="dbaniu" style=" margin-left:100px;">
         	<input type="hidden" id="rid"/>
	         <a class="ydbz_s gray" href="javascript:void(0)" id="back">返回</a>
	         <a class="ydbz_s" href="javascript:void(0)" id="next">下一步</a>
	         <a class="ydbz_s" href="javascript:void(0)" id="save">提交</a>
         </div>
       
        <!--右侧内容部分结束--> 
</body>
