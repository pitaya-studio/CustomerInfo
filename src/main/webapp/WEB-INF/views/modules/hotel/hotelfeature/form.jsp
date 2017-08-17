<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>酒店特色信息</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/default.validator.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
	<script type="text/javascript">
	$(document).ready(function() {
		$("#inputForm").validate({
			rules:{
				name:{
					required:true
				},
				sort:{
					required:true,
					digits:true
				}
			},
			submitHandler: function(form){
				if($("input[name=docOriName]").length==0){
					$.jBox.tip("请选择上传图标。", 'error'); 
					return false;
				}
				var url = "";
				if($("#id").val()=='') {
					url="${ctx}/hotelFeature/save";
				} else {
					url="${ctx}/hotelFeature/update";
				}
				$("#btnSubmit").attr("disabled", "disabled");
				$.post(url,$("#inputForm").serialize(),function(data){
					if(data=="1"){
						$("#searchForm",window.opener.document).submit(); 
						$.jBox.tip("保存成功!");
						setTimeout(function(){window.close();},500);
					}else if(data=="2"){
						$("#searchForm",window.opener.document).submit(); 
						$.jBox.tip("修改成功!");
						setTimeout(function(){window.close();},500);
					}else if(data=="3"){
						$.jBox.tip('用户公司不能为空','warning');
						$("#btnSubmit").attr("disabled", false);
					}else{
						$.jBox.tip('操作异常！','warning');
						$("#btnSubmit").attr("disabled", false);
					}
				});
				
			},
			errorElement:"em"
		});
		
		if($("#id").val()=='') {
			$("#sort").val("50");
		}
	});
	
	//上传文件时，点击后弹窗进行上传文件(多文件上传)
	//方法参数：${ctx}、显示上传文件名称input的id、当前点击元素
	function uploadFiles(ctx, inputId, obj) {
		var fls=flashChecker();
		var s="";
		if(fls.f) {
//			alert("您安装了flash,当前flash版本为: "+fls.v+".x");
		} else {
			alert("请您退出当前浏览器,并安装当前浏览器的flash player软件,然后才能上传文件"); 
			return;
		}
		
		//新建一个隐藏的div，用来保存文件上传后返回的数据
		if($(obj).parent().find(".uploadPath").length == 0) {
			$(obj).parent().append('<div class="uploadPath" style="display: none" ></div>');
			$(obj).parent().append('<div id="currentFiles" style="display: none" ></div>');		
		}
		
		$(obj).addClass("clickBtn");
		
		/*移除产品行程校验提示信息label标签*/
		$("#modIntroduction").remove();
		
		$.jBox("iframe:"+ ctx +"/hotel/uploadFilesPage", {
		    title: "多文件上传",
			width: 340,
	   		height: 365,
	   		buttons: {'关闭':true},
	   		persistent:true,
	   		loaded: function (h) {},
	   		submit: function (v, h, f) {
				$(h.find("iframe")[0].contentWindow.file_upload).next("p").find(".successUpload").click();
				if($(obj).parent().find("#currentFiles").find("[name='docOriName']").length != 0) {
					/*添加<li>之前，先将之前的<li>删除，然后再累加，以防止重复累加问题*/
//					if($(obj).attr("name") != 'costagreement'){
//						$(obj).next(".batch-ol").find("li").remove();
//					}
					
					
					
					$(obj).parent().find("#currentFiles").find("[name='docOriName']").each(function(index, obj1) {
						
						//只能上传一个图标 start
						//$(".uploadPath").find("input[name=docOriName][value!='"+$(obj1).val()+"']").each(function(index2, obj2){
						//	$(obj2).prev().eq(0).remove();
						//	$(obj2).next().eq(0).remove();
						//	$(obj2).remove();
						//});
						//$(".batch-ol").find("li").each(function(){
						//	$(this).remove();
						//});
						//只能上传一个图标 end
						
						$(obj).parent().next(".batch-ol").append('<li><span>'+ $(obj1).val() +'</span><a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(\''+ $(obj1).prev().val() +'\',\''+ $(obj).attr("name") +'\',this);">删除</a></li>');
						
					});
					if($(obj).parent().find("#currentFiles").children().length != 0) {
						$(obj).parent().find("#currentFiles").children().remove();
					}
				}
				
				$(".clickBtn",window.parent.document).removeClass("clickBtn");
	   		}
		});
		$(".jbox-close").hide();
	}
	
	//删除现有的文件
	function deleteFileInfo(inputVal, objName, obj) {
		top.$.jBox.confirm('确定要删除该文件？','系统提示',function(v){
			if(v=='ok'){
				if(inputVal != null && objName != null) {
					var delInput = $(".uploadPath").find("input[name='"+ objName +"'][value='"+ inputVal +"']");
					delInput.next().eq(0).remove();
					delInput.next().eq(0).remove();
					delInput.remove();
					
					/*删除上传文件后，文件信息会存放在id为currentFiles的div中，也需要把该div相关的上传文件信息删除*/
					var docName = $(obj).parent("li").parent("ol").parent().find("#currentFiles").find("input[name='"+ objName +"'][value='"+ inputVal +"']");
					docName.next().eq(0).remove();
					docName.next().eq(0).remove();
					docName.remove();
				
					
				}else if(inputVal == null && objName == null) {
					$(obj).parent().remove();
				}
				$(obj).parent("li").remove();

			}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');		
	}
	//下载文件
	function downloads(docid){
		window.open("${ctx}/sys/docinfo/download/"+docid);
    }
	
	</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">酒店特色信息</div>
	<form:form method="post" modelAttribute="hotelFeature" action="" class="form-horizontal" id="inputForm" >
		<form:hidden path="id" />
		<form:hidden path="uuid" />
		<form:hidden path="delFlag" />
		<form:hidden path="createBy" />
		<form:hidden path="createDate" />
		<form:hidden path="updateBy" />
		<form:hidden path="updateDate" />
		<form:hidden path="wholesalerId" />
		<div class="sysdiv sysdiv_coupon">
			<p>
				<label><em class="xing">*</em>名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="11" />
			</p>
			<p class="maintain_kong"></p>
			<p>
				<label>排序：</label>
				<form:input path="sort" type="text" maxlength="4"  />
			</p>
			<p class="maintain_kong"></p>
            <p class="maintain_pfull inputfile_box">
              <label><em class="xing">*</em>上传图标：</label>
				<input type="button" name="hotelFeaturesAnnexDocId" value="上传" class="btn btn-primary" onClick="uploadFiles('${ctx}',null,this)"/>
				<ol class="batch-ol">
				<c:forEach items="${haList}" var="file" varStatus="s1">
					<li>
						<span>${file.docName}</span><a style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${file.docId})">下载</a>
						<input type="hidden" name="hotelFeaturesAnnexDocId" value="${file.docId}" />
						<input type="hidden" name="docOriName" value="${file.docName}"/>
						<input type="hidden" name="docPath" value="${file.docPath}"/>
						<a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(null,'hotelFeaturesAnnexDocId',this)">删除</a>
					</li>
				</c:forEach>
				</ol>
			</p>
			<p class="maintain_kong"></p>
			<p class="maintain_pfull">
				<label>描述：</label>
				<form:textarea class="madintain_text"  maxlength="99" path="description" />
			</p>
			<p class="maintain_btn">
				<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
				<input type="submit" value="提&nbsp;&nbsp;&nbsp;交" class="btn btn-primary" id="btnSubmit" />
			</p>
		</div>
	</form:form>
	<!--右侧内容部分结束-->
</body>
</html>
