<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/> 
<link rel="stylesheet" type="text/css" href="${ctxStatic}/jquery-uploadfile/uploadify.css">
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-uploadfile/jquery.uploadify.min.js?f=<%=System.currentTimeMillis()%>"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
<script type="text/javascript">
var returnFileNameMap="";
var dataArr = new Array();
var arrIndex = 0;
$(document).ready(function() {
	// QU-SDP-微信分销模块start 追加上传单张图片接口添加图片类型，图片大小两个参数  yang.gao 2017-01-06
	var imgtypedef = "*.jpg;*.jpeg;*.png;*.gif";
	var maxSizedef = 0; // 上传文件的大小限制 ，如果为整数型则表示以KB为单位的大小，如果是字符串，则可以使用(B, KB, MB, or GB)为单位，比如’2MB’；如果设置为0则表示无限制
	var imgtype = "${type}";
	var maxSize = "${maxSize}";
	if (imgtype != null && imgtype != "") {
		imgtypedef = imgtype;
	}
	if (maxSize != null && maxSize != "" && maxSize != "0") {
		maxSizedef = maxSize;
	}
	// QU-SDP-微信分销模块end
	
	$("#fileQueue").parent().css("height","200px");
 //mark fix uploadify chrome error.
	setTimeout(function(){
    $('#file_upload').uploadify({
        'swf':'${ctxStatic}/jquery-uploadfile/uploadify.swf',//上传按钮的图片，默认是这个flash文件
        'fileTypeExts':imgtypedef, // 文件类型 *.jpg;*.jpeg;*.png;*.gif (QU-SDP-微信分销模块  yang.gao 2017-01-06)
        'fileSizeLimit':maxSizedef, // 文件大小 (QU-SDP-微信分销模块  yang.gao 2017-01-06)
        'uploader':'${ctx}/MulUploadFile/upload;jsessionid=<%=session.getId()%>',
        'cancelImg':'${ctxStatic}/jquery-uploadfile/uploadify-cancel.png',//取消图片
        'method':'post',
        'queueID':'fileQueue',//上传显示进度条的那个div
        'buttonText' :'选择文件',
        'progressData' :'percentage',
        'auto' : false,
        'multi' : false,
        'onUploadSuccess' : function(fileObj, data, response){
        	if(data!='false'){
        		//返回data为key:value格式的字符串,key是文件原始名称,value是文件的相对路径(包含新文件名)
        		dataArr[arrIndex] = data;
        		arrIndex++;
        		top.$.jBox.tip('上传成功', 'success');
        	}else{
        		top.$.jBox.tip('上传失败', 'error');
        	}
        	
        },
        'onSelect': function(e, queueId, fileObj) {
        	if($("#fileQueue").find(".uploadify-queue-item").length > 1) {
        		//$("#fileQueue").find(".uploadify-queue-item").eq(0).remove();  // 将第一行从页面remove
        		$('#file_upload').uploadify('cancel',$('.uploadifive-queue-item').first().data('file')); // 将第一行直接删除
        	}
         }
    });
	},100);
    
    
<%--    $('#file_upload').uploadifySettings('queueData',{'rnd':Math.random(),'innerCode':'00001'});--%>
});
    function setUpoaldPath() {
		
		
		//把返回的data写到父窗口中id为uploadPath的div里边，用来保存返回的信息
		var divCon = $(".clickBtn",window.parent.document).parent().find(".uploadPath");
		var nameForFileId = $(".clickBtn",window.parent.document).attr("name");
		
		if(dataArr.length > 0) {
		    var i = dataArr.length - 1;
			//上传文件返回的ID
			var docId = dataArr[i].split("=")[0].substring(1);
			//上传文件的原始名称
			var docOriName = dataArr[i].split("=")[1];
			//上传文件返回的路径
			var docPath = dataArr[i].split("=")[2].substring(0, dataArr[i].split("=")[2].length-3 );
			divCon.children().remove();
			divCon.append("<input type='hidden' name='" + nameForFileId + "' value='" +docId+ "' />")
				.append("<input type='hidden' name='docOriName' value='" +docOriName+ "' />")
				.append("<input type='hidden' name='docPath' value='" +docPath+ "' />");

			divCon.next("#currentFiles").append("<input type='hidden' name='" + nameForFileId + "' value='" +docId+ "' />")
				.append("<input type='hidden' name='docOriName' value='" +docOriName+ "' />")
				.append("<input type='hidden' name='docPath' value='" +docPath+ "' />");

		    dataArr.length = 0;
		}else{
			//alert("请检查当前点击按钮后面否已经存在id为uploadPath的隐藏div");
		}
    }
</script>

<div id="fileQueue"></div>
<input id="file_upload" name="file_upload" type="file">
<p class="file_btn">
    <!-- 加上“*”表示当第一个文件上传成功后，立即上传以后队列中的文件，否则需要自己手动 -->
    <a href="javascript:$('#file_upload').uploadify('upload','*')" class="mf-upload">上传</a>
    <a href="javascript:$('#file_upload').uploadify('cancel',$('.uploadifive-queue-item').first().data('file'))" class="mf-cancel">取消上传</a>
    <a href="javascript:$('#file_upload').uploadify('stop','*')" class="mf-pause">停止</a>
    <a href="javascript:$('#file_upload').uploadify('cancel','*')" class="mf-empty">清空上传文件</a>
    <a onclick="$(this).prev().eq(2).click(); setUpoaldPath();" href="javascript:void(0)" class="successUpload"></a>
</p>
