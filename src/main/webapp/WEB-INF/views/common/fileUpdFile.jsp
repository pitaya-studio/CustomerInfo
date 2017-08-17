<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort();
%>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<link rel="stylesheet" type="text/css"
	href="${ctxStatic}/jquery-uploadfile/uploadify.css">
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css"
	rel="stylesheet" />
<style>
.muchFile {
	position: relative;
	width: auto;
	height: 360px;
	overflow: hidden;
	margin: 0;
	padding: 0;
}

.muchFile-top {
	width: auto;
	padding: 0 30px;
	height: 280px;
	overflow-y: auto;
}

.uploadify {
	position: static;
	text-align: center;
	padding-bottom: 25px;
	margin-top: 110px;
}

.uploadify-contiaue {
	float: left;
	margin-top: 10px;
	padding-left: 10px;
}

.uploadify-contiaue .uploadify-button {
	background: none;
	text-align: left;
	color: #5f7795;
}

.uploadify-button {
	display: inline-block;
	width: 50px;
	height: 28px;
	text-align: center;
	line-height: 28px;
	font-weight: normal;
	background: url(../../static/images/upload.png) 20px -20px no-repeat
		#f5ac19;
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	border-radius: 2px;
	border: 0 none;
	text-shadow: none;
}

.uploadify:hover .uploadify-button {
	background: url(../../static/images/upload.png) 20px -20px no-repeat
		#ffa319;
}

.uploadify-contiaue:hover .uploadify-button {
	background: none;
}

.uploadify-queue-item {
	background: none;
	max-width: none;
}

.uploadify-progress {
	height: 10px;
	width: auto;
	margin-right: 20px;
}

.uploadify-progress-bar {
	background-color: #62afe7;
	height: 10px;
}

.uploadify-queue-item .cancel a {
	background: url(../../static/images/upload.png) 0 0 no-repeat;
	margin-top: 21px;
}

.uploadify-queue-item .cancel a:hover {
	background-position: -16px 0;
}

/**底部**/
.file_btn {
	background-color: #f9f9f9;
	height: 55px;
	padding: 25px 0 0;
	margin: 0;
	position: absolute;
	left: 0;
	right: 0;
	bottom: 0;
	z-index: 3;
	text-align: center;
}

.file_btn a {
	color: #939292;
	background: none;
	padding: 0;
}

.file_btn a:hover {
	color: #62afe7;
}

.file_btn .mf-empty, .file_btn .mf-pause {
	position: absolute;
	top: 35px;
	right: 25px;
	height: 12px;
	line-height: 12px;
}

.file_btn .mf-pause {
	right: 55px;
}

.file_btn .mf-upload-jxadd {
	background-color: #fff;
	border: #e7e7e7 1px solid;
	color: #939292;
	padding: 0px 26px;
	height: 28px;
	line-height: 28px;
}

.file_btn .mf-upload {
	background-color: #f4a300;
	border: #f4a300 1px solid;
	color: #fff;
	padding: 0px 26px;
	height: 28px;
	line-height: 28px;
	margin-left: 10px;
}

.file_btn .mf-upload:hover {
	color: #fff;
	background-color: #f49000;
}

.file_btn .mf-upload-gary, .file_btn .mf-upload-gary:hover {
	background-color: #e8e8e8;
	border-color: #dedede;
	color: #939292;
}

/**弹框**/
.muchFile-box {
	background: #fff;
	width: 295px;
	height: 95px;
	position: absolute;
	left: 17%;
	top: 25%;
	box-shadow: 0 3px 5px #cccbcb;
	border: #e5e5e5 1px solid;
	overflow: hidden;
}

.muchFile-box p {
	text-align: center;
	color: #666;
	line-height: 60px;
	padding: 0;
	margin: 0;
}

.muchFile-box-foot {
	height: 33px;
	border-top: #e5e5e5 1px solid;
	text-align: center;
	line-height: 33px;
	cursor: pointer;
	position: absolute;
	bottom: 0;
	left: 0;
	right: 0;
}

.muchFile-box-btn {
	width: 50%;
	float: left;
	border-right: #e5e5e5 1px solid;
	color: #666;
}

.muchFile-box-btn:hover {
	background: #ededed;
}

/**上传成功**/
.muchFile-upload {
	width: 100%;
	height: 270px;
	background: #fff;
	overflow: hidden;
	position: absolute;
	left: 0;
	top: 0;
	right: 0;
	text-align: center;
}

.muchFile-upload p {
	font-family: "微软雅黑";
	color: #8fc31f;
	font-size: 32px;
	line-height: 35px;
	padding: 0;
	margin: 100px 0 20px;
}

.muchFile-upload p i {
	background: url(../../static/images/upload.png) 0 -90px no-repeat;
	width: 29px;
	height: 22px;
	vertical-align: middle;
	display: inline-block;
	margin-left: 10px;
}

.muchFile-upload a {
	color: #5f7795;
}

/**删除图标**/
.upload-delect {
	width: 16px;
	height: 16px;
	background: url(../../static/images/upload.png) 0 -60px no-repeat;
	display: inline-block;
	overflow: hidden;
	cursor: pointer;
	vertical-align: middle;
}
</style>


<script type="text/javascript"
	src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/jquery-uploadfile/jquery.uploadify.min.js?f=<%=System.currentTimeMillis()%>"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"
	type="text/javascript"></script>
<script type="text/javascript">
var returnFileNameMap="";
var dataArr = new Array();
var arrIndex = 0;
$(document).ready(function() {
	//判断上传区域的内容
	$("#fileQueue").bind("DOMNodeInserted",	function(){
		$(".mf-upload.mf-upload-gary").removeClass("mf-upload-gary");
		var m = $(this).children().size();
		if(m>0){
			$(".uploadify").addClass("uploadify-contiaue");
			$(".uploadify-button-text").text("继续添加");
			//显示按钮
			$(".file_btn").show();
		}
	})
	
	 //mark fix uploadify chrome error.
	setTimeout(function(){
	
    $('#file_upload').uploadify({
        'swf':'${ctxStatic}/jquery-uploadfile/uploadify.swf',//上传按钮的图片，默认是这个flash文件
        'uploader':'${ctx}/fileUpd/upload;jsessionid=<%=session.getId()%>',
											'cancelImg' : '${ctxStatic}/jquery-uploadfile/uploadify-cancel.png',//取消图片
											'method' : 'post',
											'queueID' : 'fileQueue',//上传显示进度条的那个div
											'buttonText' : '选择文件',
											'progressData' : 'percentage',
											'auto' : false,
											'multi' : true,
											'onUploadSuccess' : function(
													fileObj, data, response) {

												if (data != 'false') {
													//返回data为json
													dataArr[arrIndex] = $
															.parseJSON(data);

													arrIndex++;
													//修改显示按钮
													$(".uploadify")
															.removeClass(
																	"uploadify-contiaue");
													$(".uploadify-button-text")
															.text("添加文件");
													//隐藏按钮
													$(".file_btn").hide();
												} else {
													top.$.jBox.tip('上传失败',
															'error');
												}
												$(".mf-upload").addClass(
														"mf-upload-gary");
											}
										});
	},100);
<%--    $('#file_upload').uploadifySettings('queueData',{'rnd':Math.random(),'innerCode':'00001'});--%>
	});
	function setUpoaldPath(v) {

		//把返回的data写到父窗口中id为uploadPath的div里边，用来保存返回的信息
		var divCon = $(".clickFileUpdBtn", window.parent.document).parent()
				.find(".uploadPath");
		var nameForFileId = $(".clickFileUpdBtn", window.parent.document).attr(
				"name");

		if (divCon.length > 0) {
			for (var i = 0; i < dataArr.length; i++) {

				//上传文件返回的ID
				var docId = dataArr[i].id;
				//上传文件的原始名称
				var docOriName = dataArr[i].docName;
				//上传文件返回的路径
				var docPath = dataArr[i].docPath;
				divCon
						.append(
								"<input type='hidden' name='"+nameForFileId+"' value='" +docId+ "' />")
						.append(
								"<input type='hidden' name='docOriName' value='" +docOriName+ "' />")
						.append(
								"<input type='hidden' name='docPath' value='" +docPath+ "' />");

				$(divCon)
						.next()
						.append(
								"<input type='hidden' name='"+nameForFileId+"' value='" +docId+ "' />")
						.append(
								"<input type='hidden' name='docOriName' value='" +docOriName+ "' />")
						.append(
								"<input type='hidden' name='docPath' value='" +docPath+ "' />");
			}
			dataArr.length = 0;
		} else {
			alert("请检查当前点击按钮后面否已经存在id为uploadPath的隐藏div");
		}
	}

	//上传文件
	function uploadFiles() {
		if ($(".mf-upload-gary").size() == 0) {
			$('#file_upload').uploadify('upload', '*');
		}
	}

	function abc() {

	}
</script>

<!--开发代码开始-->
<div class="muchFile">
	<div class="muchFile-top">
		<div id="fileQueue"></div>
		<div id="file_upload" class="uploadify ">
			<div id="file_upload-button" class="uploadify-button ">
				<span class="uploadify-button-text">添加文件</span>
			</div>
		</div>

	</div>
	<p class="file_btn" style="display:none">
		<!-- 加上“*”表示当第一个文件上传成功后，立即上传以后队列中的文件，否则需要自己手动 -->


		<a href="javascript:uploadFiles()" class="mf-upload mf-upload-gary">开始上传</a><a
			href="javascript:$('#file_upload').uploadify('stop','*')"
			class="mf-pause">全部暂停</a> <a
			href="javascript:$('#file_upload').uploadify('cancel','*')"
			class="mf-empty">清空</a> <a
			onclick="$(this).prev().eq(2).click(); setUpoaldPath();"
			href="javascript:void(0)" class="successUpload"></a>
	</p>

</div>

