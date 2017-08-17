<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>发布公告</title>
<meta name="decorator" content="wholesaler" />
<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"  type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/ckeditor/ckeditor.js"></script>
<script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		getNoticeTitLength();
		//公告范围
		$(".ggScope input[type='radio']").on("click", function() {
			if ($(this).prop("checked") && $(this).parent().text() == '部门公告') {
				$(this).parent().parent().next().show();
			} else {
				$(this).parent().parent().next().hide();
			}
		});
		//全选 部门公告
		$(".notice_qxuan")
				.on(
						"click",
						function() {
							if ($(this).prop("checked")) {
								$(this).parent().siblings().find(
										"input[type='checkbox']").attr(
										"checked", 'true');
							} else {
								$(this).parent().siblings().find(
										"input[type='checkbox']").removeAttr(
										"checked");
							}
						});
	});
	//公告标题输入长度  
	$("input.noticeTit").live("keyup blur", function(event) {
		var title= $(this).val();
		if(event.type=="keyup"){
			getNoticeTitLength();
			if(title.length>20){
				jBox.error("标题不要超过20个字","提示");
			}
		}
		if(event.type == "blur"){
			getNoticeTitLength();
			if(title.length>20){
				jBox.error("标题不要超过20个字","提示");
			}
		}
		
	});
	// 提交表单
	$("input.ydbz_s").live("click",function(){
		$("input.ydbz_s").attr("disabled","disabled");
		var idname = $(this).attr("id");
		if(idname=="issue"){
			// 提交公告
			valiParam(1);
		}else{
			// 提交公告草稿
			valiParam(0);
		}
		window.setTimeout(function(){
			$("input.ydbz_s").removeAttr("disabled");
		},3000);
	});
	

	function getNoticeTitLength() {
		var noticeTitLength = 20 - ($(".noticeTit").val().length);
		if (noticeTitLength < 0) {
			noticeTitLength=0;
		}
		$(".noticeTit").next().find("em").text(noticeTitLength);
	}
	
	// 提交表单
	function subForm(type){
		var the_param = $("#addMessage").serialize();
		the_param += "&saveStatus="+type;
		$.ajax({
			type : "POST",
			url : contextPath + "/message/addAJaxMsg",
			data : the_param,
			async : true, 
			dataType : "text"
		});
	}
	// 校验不合格参数
	function valiParam(type){
		var str=CKEDITOR.instances.content.getData();
		var ind = CKEDITOR.instances.content.document.getBody().getText();
		$("#content").val(str);// 将正文载入content
		$("#index").val(ind); // 获取纯文本
		var title = $("input[name=title]").val(); // 获取标题值
		if(title && str){
			var backTitle = $.trim(title);
			var backContent = $.trim(str);
			var bool = true;
			if(backTitle.length>20 || backTitle.length<1){
				$.jBox.tip("提交失败，公告标题不要超过20个字","提示");
				$("input.ydbz_s").removeAttr("disabled");
				bool = false;
			}
			if(bool && (backContent.length>10000 || backContent.length<1)){
				$.jBox.tip("提交失败，公告内容不要超过10000个字","提示");
				$("input.ydbz_s").removeAttr("disabled");
				bool = false;
			}
			if(bool && (backTitle.length>0 && backTitle.length<20 && backContent.length>0 && backContent.length<10000)){
				subForm(type);
				if (type == 0) {
					$.jBox.tip("保存成功", 'success');
				} else {
					$.jBox.tip("发布成功", 'success');
				}
				
				cleanForm();
			}
		}else{
			$.jBox.tip("提交失败，请检查您输入的数据","提示");
		}
	}
	// 清空表单
	function cleanForm(){
		$("input[name=overTime]").val("");
		$("input[name=title]").val("");
		$("#index").val(""); // 干掉正文预览
		$('#content').val(""); // 干掉上传正文
		CKEDITOR.instances.content.setData(""); // 干掉显示正文
		$("input[name=titleLightCss]").removeAttr('checked');
		$("input[name=titleVulgarCss]").removeAttr('checked');
		// 删除附件展示
		$("ol.batch-ol li").each(function(){
			$(this).remove();
		});
		$("div.uploadPath").empty();
		$("div.currentFiles").empty();
		$("input.noticeTit").blur();
	}
</script>
<style type="text/css">
	/*子table样式*/
	.activitylist_bodyer_table tr td{vertical-align:middle}
	.activity_team_top1 .team_top{background-color:#f5f5f5}
	.activity_team_top1 .team_top{border-top:1px solid #b5c8db}
	.activity_team_top1 .team_top table{border:none;background:none}
	.activity_team_top1 .team_top table thead tr th,.team_top table tr td{border:none;background:none; background-color:#f5f5f5;}
	.activitylist_bodyer_table .team_top thead tr{background:none;}
	.activity_team_top1 table thead tr th{color:#717171}
	#contentTable, #contentTable th, #contentTable td{ vertical-align:middle;}
	#contentTable th,#contentTable td{height:40px;}
	#contentTable .team_top td{height:auto}
	.activitylist_bodyer_table .tr-hover td{}
	#control_xon span{width:19%; float:left;}
	.activitylist_bodyer_table .tr-hover td{background-color:#c7dbec;background-image:url(../images/tr-hoverbg.gif); background-repeat: repeat-x;color:#394b61;border-bottom:1px solid #b5c8db;border-left: 1px solid #b5c8db;}
	.activitylist_bodyer_table .tr-hover td:first-child{border-left:1px solid #93a5bb}
	.activitylist_bodyer_table .tr-hover td:last-child{border-right:1px solid #93a5bb}
	.activitylist_bodyer_table .tr-hover:hover td{background-color:#C7DBEC}
	.activitylist_bodyer_table tr:hover td.team_top{background-color:#e1e8ed}
	.activitylist_bodyer_table tr:hover td{background-color:#f5f5f5}
	.activity_team_top1 .team_top{border:1px solid #93a5bb;padding:0 0 5px 0;margin-bottom:20px;background-color:#e1e8ed}
	.activity_team_top1 .team_top table thead tr th.t-th2{border-bottom:1px solid #DDDDDD}
</style>
</head>
<body>
	<page:applyDecorator name="message_my_create" >
    	<page:param name="current">mycreatemsg</page:param>
    </page:applyDecorator>
	<!--右侧内容部分开始-->
	<div class="noticeDetails">
		<form id="addMessage">
			<div class="noticeadd">
				<p class="notice_t">范围：</p>
				<p class="ggScope">
					<label><input  type="radio" name="msgType"  value="1" />全站公告</label>
					<label><input	type="radio" name="msgType"  value="2" checked="checked" />部门公告</label>
					<label><input	type="radio" name="msgType"  value="3" />渠道公告</label>
					<!-- 
					<label><input	type="radio" name="msgType"  value="4" />约签公告</label> -->
				</p>
				<p>
					<label>全选 <input type="checkbox" class="notice_qxuan" checked="checked" /></label>
					<c:forEach items= "${departmentSet }" var = "dep"> 
						<label><input type="checkbox"  name="departmentIds"  value="${dep.id }" checked="checked" />${dep.name }</label>
					</c:forEach>
				</p>
			</div>
			<div class="noticeadd noticeadd_tit">
				<p>
					<span>
						<b>标题：</b>
						<input type="text" name="title" class="noticeTit"/><em class="notice_tipsnum">您还可输入<em>20</em>字 </em>
					</span> 
					<span>
						<input type="checkbox"  name="titleLightCss"  value="1"/><i class="notice_he">高亮</i>
					</span>
					<span><input type="checkbox" name="titleVulgarCss"  value="1"/><i class="notice_bold">加粗</i></span>
					<span>
					 	<b>公告类型：</b> 
					 	<select id="msgNoticeType" name="msgNoticeType" class="">
							<option value="1">单团</option>
							<option value="2">散拼</option>
							<option value="3">游学</option>
							<option value="4">大客户</option>
							<option value="5">自由行</option>
							<option value="6">签证</option>
							<option value="7">机票</option>
							<option value="8">套餐</option>
							<option value="9">其他</option>
						</select> 
					</span> 
					<span>
						<b>过期时间：</b>
						<input type="text"  name="overTime" class="dateinput" readonly="readonly" onclick="WdatePicker({minDate:'%y-%M-%d'})" />
					</span>
				</p>
			</div>
			<div class="notice_upload">
				<input type="button" name="docinfoIds" value="添加附件" class="mod_infoinformation3_file"  style="height:30px" onClick="uploadFiles('${ctx}',null,this)"/>
				<ol class="batch-ol">
					<input type="hidden" name="docinfoIds_name" value="文件" />
				</ol>
			</div>
			<div class="noticehtml">
				<textarea id="content"  name="content"  class="ckeditor"></textarea>
				<input type="hidden" id="index"  name="index"   value=""/>
				<script type="text/javascript">CKEDITOR.replace('content');</script>
			</div>
			<div></div>
			<div class="noticebtn">
				<input type="button" class="ydbz_s" id="issue" value="发布"/>
				<input type="button" class="ydbz_s" id="draft" value="保存草稿"/>
			</div>
		</form>
	</div>
</body>
</html>