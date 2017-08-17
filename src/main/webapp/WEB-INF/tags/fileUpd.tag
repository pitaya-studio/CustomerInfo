<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!--  <style>
div.jbox .jbox-button{text-indent:-9999px;overflow:hidden; margin-top:5px;width:16px; height:16px; background:url(../../static/images/upload.png) 0 -60px no-repeat; display:inline-block; overflow:hidden; cursor:pointer; vertical-align:middle;}
</style> -->
<%@ attribute name="fileList" type="java.util.List" required="true"
	description="文件列表"%>
<%@ attribute name="fieldName" type="java.lang.String" required="true"
	description="字段名"%>
<%@ attribute name="isForm" type="java.lang.Boolean" required="true" 
	description="是否form页面"%>	
<c:set var="ctx"
	value="${pageContext.request.contextPath}${fns:getAdminPath()}" />


<c:if test="${isForm }">

	<input type="button" name="${fieldName}" value="选择文件"
		class="mod_infoinformation3_file"
		onClick="fileUpdTagUpFiles('${ctx}',null,this)" />
	<div class="uploadPath" style="display: none">
		<c:forEach items="${fileList}" var="afile">
			<input type="hidden" name="${fieldName}" value="${afile.id}">
			<input type="hidden" name="docOriName" value="${afile.docName}">
			<input type="hidden" name="docPath" value="${afile.docPath}">
		</c:forEach>
	</div>
	<div class="currentFiles" style="display: none"></div>
</c:if>	
<ol class="batch-ol" style="margin: 0px 0px 10px 20px;">
	<c:forEach items="${fileList}" var="file" varStatus="s1">
		<li>
		<c:if test="${file.docName.indexOf('.jpg') > 0 || file.docName.indexOf('.png') > 0 ||file.docName.indexOf('.gif') > 0 ||file.docName.indexOf('.jpeg') > 0 || file.docName.indexOf('.bmp') > 0}">
			<a href="${fns:getConfig('imag.server.url')}${file.docPath}"  target="_blank"><img src="${fns:getConfig('imag.server.url')}/${file.docPath}"  width="60" height="60"/></a>
		</c:if>
		<span>${file.docName}</span>
		<a style="margin-left: 10px;"
			href="javascript:void(0)" onClick="fileUpdTagDlFiles(${file.id})">下载</a>
			<c:if test="${isForm}">
				<input type="hidden" name="${file}" value="${file.id}" /> <a style="margin-left: 10px;"
				class="batchDel" href="javascript:void(0)"
				onclick="fileUpdTagDelete(${file.id},'${fieldName}',this)">删除</a></li>
			</c:if>	
			</li>
	</c:forEach>
</ol>



<script type="text/javascript">

function fileUpdTagUpFiles(ctx, inputId, obj) {
	var fls=flashChecker();
	var s="";
	if(fls.f) {
	} else {
		alert("请您退出当前浏览器,并安装当前浏览器的flash player软件,然后才能上传文件"); 
		return;
	}
	
	
	$(obj).addClass("clickFileUpdBtn");

	
	$.jBox("iframe:"+ ctx +"/fileUpd/default", {
	    title: "多文件上传",
		width: 435,
 		height: 438,
 		buttons: {'关闭':false},
 		persistent:true,
 		loaded: function (h) {},
 		submit: function (v, h, f) {
			//取消上传
 			$(h.find("iframe")[0].contentDocument).find(".successUpload").trigger("click");
 			
 			// 上传的文件 显示出来
 			var ctFiles = $(obj).nextAll(".currentFiles").eq(0);
			if(ctFiles.children().length != 0) {
				var targetObj =ctFiles.next();
				ctFiles.children().each(function(index, child) {
					if(index%3==0){
						var names = $(child).next().val();
						var info = '<li>';
						if(names.indexOf('.jpg') > 0 || names.indexOf('.png') > 0 ||names.indexOf('.gif') > 0 ||names.indexOf('.jpeg') > 0 || names.indexOf('.bmp') > 0){
							info  = info +'<a href="${fns:getConfig('imag.server.url')}/'+$(child).next().next().val()+'"  target="_blank"><img src="${fns:getConfig('imag.server.url')}/'+$(child).next().next().val()+'"  width="60" height="60"/></a>';	
						}
						info  = info +'<span>'+$(child).next().val() +'</span>';
						info  = info +'<a style="margin-left: 10px;"href="javascript:void(0)" onClick="fileUpdTagDlFiles('+$(child).val()+')">下载</a>';
						info  = info +'<a class="batchDel" style="margin-left: 10px;" href="javascript:void(0)" onclick="fileUpdTagDelete(\''+ $(child).val() +'\',\''+ $(child).next().val() +'\',this);">删除</a>';
						info  = info +'</li>';
						
						targetObj.append(info);
						
					}
				});
			}
			ctFiles.empty();
			$(".clickFileUpdBtn",window.parent.document).removeClass("clickBtn");
 		}
	});
	var a = $(".jbox-button");
	a.css("padding","0");
	$(".jbox-content-loading").parent().css("width","460px");
	$(".jbox-title-panel").append(a);
	$(".jbox-close").hide();
	$(".jbox-button-panel").hide();

}
//删除现有的文件
function fileUpdTagDelete(inputVal, objName, obj) {
	
	top.$.jBox.confirm('确定要删除该文件？','系统提示',function(v){
		if(v=='ok'){
			
			if(inputVal != null && objName != null) {
				var delInput = $(".uploadPath").find("input[name='"+ objName +"'][value='"+ inputVal +"']");
				delInput.next().eq(0).remove();
				delInput.next().eq(0).remove();
				delInput.remove();
				$(obj).parent().remove();
			}
		
		}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');		
}

function fileUpdTagDlFiles(docid){
	var fileUrl =encodeURI("${ctx}/fileUpd/fileExists/" + docid + "/helloworld");
	 $.ajax({ url: encodeURI(fileUrl),async:false, success: function(result){	 
		  if("文件存在"==result){
				window.open("${ctx}/fileUpd/download/"+docid);
		  } else{
			  top.$.jBox.tip("文件不存在", 'warnning');
		  }
	}});
}


</script>