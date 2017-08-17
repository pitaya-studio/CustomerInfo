<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>上传资料</title>
<meta name="decorator" content="wholesaler" />
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.js"></script> 
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script type="text/javascript">
var visaTmp="";	  //签证附件
$(function(){
	var fileArray = eval($("#fileArray").val());
	$.each(fileArray,function(key, value) {
		for(var i in value) {
			if(i == 1) {
				var dest = $("#passportfile").parent().parent().find("input[name='fileLogo']").val(value[i]);
			} else if(i == 2) {
				var dest = $("#photofile").parent().parent().find("input[name='fileLogo']").val(value[i]);
			} else if(i == 3) {
				var dest = $("#idcardfrontfile").parent().parent().find("input[name='fileLogo']").val(value[i]);
			} else if(i == 4) {
				var dest = $("#idcardbackfile").parent().parent().find("input[name='fileLogo']").val(value[i]);
			} else if(i == 5) {
				var dest = $("#entryformfile").parent().parent().find("input[name='fileLogo']").val(value[i]);
			} else if(i == 6) {
				var dest = $("#housefile").parent().parent().find("input[name='fileLogo']").val(value[i]);
			} else if(i == 7) {
				var dest = $("#residencefile").parent().parent().find("input[name='fileLogo']").val(value[i]);
			} else if(i == 0) {
				var dest = $("#otherfile").parent().parent().find("input[name='fileLogo']").val(value[i]);
			} else if(i == 8) {
				visaTmp = visaTmp + value[i] + ";";
				var dest = $("#visaannexfile").parent().parent().find("input[name='fileLogo']").val(visaTmp);
			}
		}
	});
});
function inFileName(obj) {
    var res = $(obj).val();
    var dest = $(obj).parent().parent().find("input[name='fileLogo']")[0];
    $(dest).val(res);
}

function downloadFiles() {
	var docIds = $("#docIds").val();
	if(!docIds || docIds == "") {
		alert("没有要下载的游客资料");
		return false;
	} else {
		window.open(encodeURI(encodeURI("${ctx}/sys/docinfo/zipdownload/" + docIds + "/游客资料")));
	}
}
</script>
</head>
<body>
<page:applyDecorator name="show_head">
    <page:param name="desc">游客列表</page:param>
</page:applyDecorator>
	<form id="offlineform_5" method="post" enctype="multipart/form-data" action="${ctx}/orderCommon/manage/uploadTravelerFiles" style="margin:0px; padding:0px;">
		<input name="travelerId" value="${travelerId}" type="hidden">
		<input id="docIds" name="docIds" value="${docIds}" type="hidden">
		<input id='fileArray' name='fileArray' value='${fileArray}' type='hidden'>
		<!--右侧内容部分开始-->
		<div class="mod_nav">订单 > ${orderTypeStr}> 上传资料</div>
		<div class="ydbzbox fs">                    
			<div class="tourist-left-upload">
				<div class="ydbz_tit orderdetails_titpr">上传资料<span class="tdred">（${travelerName}）</span>
					<c:if test="${not empty docIds && docIds != ''}">
						<a class="ydbz_x" href="javascript:void(0)" onclick="downloadFiles()">全部下载</a>
					</c:if>
				</div>
				<ul class="ydbz_2uploadfile ydbz_scleft">
					<li class="seach25 seach33">
						<p>护照首页：</p>
						<span>
							<input name="fileLogo" type="text" value="" />
						</span>
						<span class="pr">
							<input type="file" onchange="inFileName(this)" hidefocus="" name="passportfile" id="passportfile" class="inputFile" /> 
                            <input type="button" class="mod_infoinformation3_file btninput_file" value="选择文件">
                        </span>
					</li>
					<li class="seach25 seach33">
						<p>身份证正面：</p>
						<span>
							<input name="fileLogo" type="text" value="" />
						</span>
						<span class="pr">
							<input type="file" onchange="inFileName(this)" hidefocus="" name="idcardfrontfile" id="idcardfrontfile" class="inputFile" /> 
                            <input type="button" class="mod_infoinformation3_file btninput_file" value="选择文件">
                        </span>
					</li>
					<li class="seach25 seach33">
						<p>申请表格：</p>
						<span>
							<input name="fileLogo" type="text" value="" />
						</span>
						<span class="pr">
							<input type="file" onchange="inFileName(this)" hidefocus="" name="entryformfile" id="entryformfile" class="inputFile" /> 
                            <input type="button" class="mod_infoinformation3_file btninput_file" value="选择文件">
                        </span>
					</li>
					<li class="seach25 seach33">
						<p>房产证：</p>
						<span>
							<input name="fileLogo" type="text" value="" />
						</span>
						<span class="pr">
							<input type="file" onchange="inFileName(this)" hidefocus="" name="housefile" id="housefile" class="inputFile" /> 
                            <input type="button" class="mod_infoinformation3_file btninput_file" value="选择文件">
                        </span>
					</li>
					<li class="seach25 seach33">
						<p>电子照片：</p>
						<span>
							<input name="fileLogo" type="text" value="" />
						</span>
						<span class="pr">
							<input type="file" onchange="inFileName(this)" hidefocus="" name="photofile" id="photofile" class="inputFile" /> 
                            <input type="button" class="mod_infoinformation3_file btninput_file" value="选择文件">
                        </span>
					</li>
					<li class="seach25 seach33">
						<p>身份证反面：</p>
						<span>
							<input name="fileLogo" type="text" value="" />
						</span>
						<span class="pr">
							<input type="file" onchange="inFileName(this)" hidefocus="" name="idcardbackfile" id="idcardbackfile" class="inputFile" /> 
                            <input type="button" class="mod_infoinformation3_file btninput_file" value="选择文件">
                        </span>
					</li>
					<p class="kong"></p>
					<li class="seach25 seach33">
						<p>户口本：</p>
						<span>
							<input name="fileLogo" type="text" value="" />
						</span>
						<span class="pr">
							<input type="file" onchange="inFileName(this)" hidefocus="" name="residencefile" id="residencefile" class="inputFile" /> 
                            <input type="button" class="mod_infoinformation3_file btninput_file" value="选择文件">
                        </span>
					</li>
					<li class="seach25 seach33">
						<p>其　它：</p>
							<input name="fileLogo" type="text" value="" />
						<span class="pr">
							<input type="file" onchange="inFileName(this)" hidefocus="" name="otherfile" id="otherfile" class="inputFile" /> 
                            <input type="button" class="mod_infoinformation3_file btninput_file" value="选择文件">
                        </span>
					</li>
					<li class="seach25 seach33">
						<p>签证附件：</p>
						<input name="fileLogo" type="text" value="" />
						<span class="pr">
							<input type="file" onchange="inFileName(this)" hidefocus="" name="visaannexfile" id="visaannexfile" class="inputFile" />
                            <input type="button" class="mod_infoinformation3_file btninput_file" value="选择文件">
                        </span>
					</li>
					<p class="kong"></p>
				</ul>
				<div class="release_next_add"><input type="submit" class="btn btn-primary" value="保  存" /></div>
			</div>
		</div>
		<!--右侧内容部分结束-->
	</form>
</body>
</html>