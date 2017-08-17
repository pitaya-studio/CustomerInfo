<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="decorator" content="wholesaler"/>
<title>地接社管理-新增第4步-资质</title>
<meta name="decorator" content="wholesaler"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
$(function(){
	//上传动作
	btfile();
	//inpout 活的焦点
	inputTips();
	//地接社资质添加
	qdzz_add();

	$(".submitButton").click(function(){
			var len = $(".fileUploadButton").length;
			var valStr= "";
			for(var i = 0 ; i < len;i++){
				var name = "elseFileName["+i+"]";
				var idName = "elseFileId["+i+"]";
				var attrName = "input[name='"+name+"']";
				var idAttrName ="input[type='hidden'][name='"+idName+"']";
				var docId = $(idAttrName).val();
				var name = $(attrName).val();
				valStr += docId+","+name+";";
			};
			$("#elseFileHidden").val(valStr);
			$("input[type='hidden'][name='businessLicense']").val($("input[type='hidden'][name='supplierInfo.businessLicense']").val());
			$("input[type='hidden'][name='businessCertificate']").val($("input[type='hidden'][name='supplierInfo.businessCertificate']").val());
			$("input[type='hidden'][name='taxCertificate']").val($("input[type='hidden'][name='supplierInfo.taxCertificate']").val());
			$("input[type='hidden'][name='organizeCertificate']").val($("input[type='hidden'][name='supplierInfo.organizeCertificate']").val());
			$("input[type='hidden'][name='idCard']").val($("input[type='hidden'][name='supplierInfo.idCard']").val());
			$("input[type='hidden'][name='bankOpenLicense']").val($("input[type='hidden'][name='supplierInfo.bankOpenLicense']").val());
			$("input[type='hidden'][name='travelAptitudes']").val($("input[type='hidden'][name='supplierInfo.travelAptitudes']").val());
			$("#formAttach").submit();
	});
	
});

//文件下载
	 function downloads(docid,activitySerNum,acitivityName,iszip){
		window.open("${ctx}/sys/docinfo/download/"+docid);
	}
	//删除现有的文件
	function deleteFiles(id,o) {
		top.$.jBox.confirm('确定要删除该文件？','系统提示',function(v){
			if(v=='ok'){
				$(o).parent().remove();
			}
			},{buttonsFocus:1});
	}
//文件上传回调
	/**
     * 附件上传回调方法
     * @param {Object} obj button对象
     * @param {Object} fileIDList  文件表id
     * @param {Object} fileNameList 文件原名称
     * @param {Object} filePathList 文件url
     */
	 function commenFunction(obj,fileIDList,fileNameList,filePathList){
	    	var name = obj.name;
	    	$(obj).parent().find("span").remove();
	    	if(fileIDList.length>0){
	    		$("input[name='"+obj.name+"']").after("<span class='seach_checkbox_2'><b>"+fileNameList+"</b><input type='hidden' name='"+obj.name+"' value='"+fileIDList+"'/> <a style='margin-left:10px;' href='javascript:void(0)' onclick='downloads("+fileIDList+",\""+fileNameList+"\",1,true)'>下载</a> <a style='margin-left:10px;' href='javascript:void(0)' onclick='deleteFiles("+fileIDList+",this)'>删除</a></span>");
	 		}
	 }
 	//地接社资质
	function qdzz_add(){
		var i = $(".uploadFile").length;
		$('.wbyu-bot').on('click','.qdzz-add-remove',function(){$(this).parent('dt').remove();});
		$('.qdzz-add').click(function(){
			//$("dt.uploadFile").last().after('<dt class="uploadFile"><label>其他文件：</label><div class="pr fl"><input value="" class="inputTxt inputTxtlong" name="elseFileName['+i+']" id="" flag="istips"><span class="ipt-tips">文件名称</span></div><p class="fl"> <input type="button" name="elseFileId['+i+']" id="elseFileId['+i+']" class="btn btn-primary fileUploadButton" value="上传" onclick="uploadFiles(\'${ctx}\',\'agentinfo.license\',this,1);"/> <span id="upfileShow" class="seach_checkbox_2"></span><span class="fileLogo"></span></p><input type="hidden" name="elseFileSize" value="'+i+'"/><div class="ydbz_s qdzz-add-remove gray">删除</div></dt>');
			$("dt.uploadFile").last().after('<dt class="uploadFile"><label>其他文件：</label><div class="pr fl"><input value="" class="inputTxt inputTxtlong" name="elseFileName['+i+']" id="" flag="istips"><span class="ipt-tips">文件名称</span></div><p class="fl"> <input type="button" name="elseFileId['+i+']" id="elseFileId['+i+']" class="btn btn-primary fileUploadButton" value="上传" onclick="uploadFiles(\'${ctx}\',\'agentinfo.license\',this,1);"/> <span id="upfileShow" class="seach_checkbox_2"></span><span class="fileLogo"></span></p><input type="hidden" name="elseFileSize" value="'+i+'"/><input type="button" value="删除" class="ydbz_x btn qdzz-add-remove">');
			inputTips();
			i++;
			//addClickEvent($(this).parent().next().find("input[type='file']"));
		});
		
	}
 	
function check(){
	if (document.getElementById("supplierId").value != null && document.getElementById("supplierId").value != "") {
		top.$.jBox.tip('第四步保存成功', 'success');
		return true;
	} else {
		top.$.jBox.tip('请先填写基本信息', 'error');
		return false;
	}
}

function checkFirstForm(){
	top.$.jBox.tip('请先填写基本信息', 'error');
}
</script>
<style>
	.seach_checkbox_2{
		margin-top:5px
	}
</style>
</head>
<body>
	<page:applyDecorator name="supplier_op_head">
		<page:param name="current">supplierAdd</page:param>
	</page:applyDecorator>
            	<!--右侧内容部分开始-->
				<div class="supplierLine">
					<c:choose>
						<c:when test="${supplierId!=null && supplierId!='' }">
							<a href="${ctx}/supplier/supplierFirstForm?supplierId=${supplierId}">基本信息填写</a>
							<a href="${ctx}/supplier/supplierSecondForm?supplierId=${supplierId}">网站信息</a>
							<a href="${ctx}/supplier/supplierThirdForm?supplierId=${supplierId}">银行账户</a>
							<a href="${ctx}/supplier/supplierFourthForm?supplierId=${supplierId}" class="select">资质上传</a>
						</c:when>
						<c:otherwise>
							<a href="${ctx}/supplier/supplierFirstForm">基本信息填写</a>
							<a href="javascript:void(0)" onclick="checkFirstForm()">网站信息</a>
							<a href="javascript:void(0)" onclick="checkFirstForm()">银行账户</a>
							<a href="${ctx}/supplier/supplierFourthForm" class="select">资质上传</a>
						</c:otherwise>
					</c:choose>
				</div>
				<form modelAttribute="supplierInfo" method="post" action="${ctx}/supplier/saveFourthForm" class="form-horizontal" id="inputForm" onsubmit="return check()">
				<input type="hidden" name="supplierId" <c:if test="${supplierId!=null }">value="${supplierId }"</c:if><c:if test="${supplierId==null }">value=""</c:if>/>
				<div class="qdgl-cen">
					<dl class="wbyu-bot wbyu-bot2">
						<dt>
							<label>营业执照：</label>
							<input type="button" name="supplierInfo.businessLicense" id="businessLicense"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','supplierInfo.license',this,1);"/>
							<c:choose>
							<c:when test="${businessLicense != null}">
							<span class="seach_checkbox_2">
								<b>${businessLicense.docName}</b>
								<input type="hidden" name="businessLicense" value="${businessLicense.id}">
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${businessLicense.id}','${businessLicense.docName}',1,true)">下载</a> 
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${businessLicense.id}',this)">删除</a>
							</span>
							</c:when>
							</c:choose>
						</dt>
						<dt>
							<label>经营许可证：</label>
							<input type="button" name="supplierInfo.businessCertificate" id="businessCertificate"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','supplierInfo.license',this,1);"/>
							<c:choose>
							<c:when test="${businessCertificate != null}">
							<span class="seach_checkbox_2">
								<b>${businessCertificate.docName}</b>
								<input type="hidden" name="businessCertificate" value="${businessCertificate.id}">
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${businessCertificate.id}','${businessCertificate.docName}',1,true)">下载</a> 
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${businessCertificate.id}',this)">删除</a>
							</span>
							</c:when>
							</c:choose>
						</dt>
						<dt>
							<label>税务登记证：</label>
							<input type="button" name="supplierInfo.taxCertificate" id="taxCertificate"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','supplierInfo.license',this,1);"/>
							<c:choose>
							<c:when test="${taxCertificate != null}">
							<span class="seach_checkbox_2">
								<b>${taxCertificate.docName}</b>
								<input type="hidden" name="taxCertificate" value="${taxCertificate.id}">
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${taxCertificate.id}','${taxCertificate.docName}',1,true)">下载</a> 
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${taxCertificate.id}',this)">删除</a>
							</span>
							</c:when>
							</c:choose>
						</dt>
						<dt>
							<label>组织机构代码证：</label>
							 <input type="button" name="supplierInfo.organizeCertificate" id="organizeCertificate"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','supplierInfo.license',this,1);"/>
							<c:choose>
							<c:when test="${organizeCertificate != null}">
							<span class="seach_checkbox_2">
								<b>${organizeCertificate.docName}</b>
								<input type="hidden" name="organizeCertificate" value="${organizeCertificate.id}">
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${organizeCertificate.id}','${organizeCertificate.docName}',1,true)">下载</a> 
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${organizeCertificate.id}',this)">删除</a>
							</span>
							</c:when>
							</c:choose>
						</dt>
						<dt>
							<label>公司法人身份证（正反面在一起）：</label>
							<input type="button" name="supplierInfo.idCard" id="idCard"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','supplierInfo.license',this,1);"/>
							<c:choose>
							<c:when test="${idCard != null}">
							<span class="seach_checkbox_2">
								<b>${idCard.docName}</b>
								<input type="hidden" name="idCard" value="${idCard.id}">
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${idCard.id}','${idCard.docName}',1,true)">下载</a> 
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${idCard.id}',this)">删除</a>
							</span>
							</c:when>
							</c:choose>
						</dt>
                        <dt>
							<label>公司银行开户许可证：</label>
							<input type="button" name="supplierInfo.bankOpenLicense" id="bankOpenLicense"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','supplierInfo.license',this,1);"/>
							<c:choose>
							<c:when test="${bankOpenLicense != null}">
							<span class="seach_checkbox_2">
								<b>${bankOpenLicense.docName}</b>
								<input type="hidden" name="bankOpenLicense" value="${bankOpenLicense.id}">
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${bankOpenLicense.id}','${bankOpenLicense.docName}',1,true)">下载</a> 
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${bankOpenLicense.id}',this)">删除</a>
							</span>
							</c:when>
							</c:choose>
						</dt>
						<dt>
							<label>旅游业资质：</label>
							<input type="button" name="supplierInfo.travelAptitudes" id="travelAptitudes"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','supplierInfo.license',this,1);"/>
							<c:choose>
							<c:when test="${travelAptitudes != null}">
							<span class="seach_checkbox_2">
								<b>${travelAptitudes.docName}</b>
								<input type="hidden" name="travelAptitudes" value="${travelAptitudes.id}">
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${travelAptitudes.id}','${travelAptitudes.docName}',1,true)">下载</a> 
								<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${travelAptitudes.id}',this)">删除</a>
							</span>
							</c:when>
							</c:choose>
						</dt>
						<c:choose>
						<c:when test="${elseFileList != null}">
						<c:forEach varStatus="status" items="${elseFileList }" var="docinfo">
						<dt class="elseFileAdd uploadFile">
							<label>其他文件：</label>
							<div class="pr fl">
							<input class="inputTxt inputTxtlong" name="elseFileName[${status.index }]" value="${docinfo.elseFileName }" flag="istips"> 
                			<span class="ipt-tips">文件名称</span>
                			</div>
							<p class="fl">
								<input type="button" name="elseFileId[${status.index }]" class="btn btn-primary fileUploadButton" value="上传"  onclick="uploadFiles('${ctx}','supplierInfo.license',this,1);"/>
									<span class="seach_checkbox_2">
	           			   				<b>${docinfo.docName}</b>
	           			   				<input type="hidden" name="elseFileId[${status.index }]" value="${docinfo.id}">
										<a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads('${docinfo.id}','${docinfo.docName}',1,true)">下载</a> 
										<a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteFiles('${docinfo.id}',this)">删除</a>
	           			   				<input type="hidden" name="elseFileSize" value="${status.index }"/>
	           			   			</span>
							</p>
							<c:choose>
							<c:when test="${status.index == 0}"><div class="ydbz_s qdzz-add">添加+</div></c:when>
							<c:otherwise><div class="ydbz_s qdzz-add-remove gray">删除</div></c:otherwise>
							</c:choose>
						</dt>
						</c:forEach>
						</c:when>
						<c:otherwise>
						<dt class="elseFileAdd uploadFile">
							<label>其他文件：</label>
							<div class="pr fl">
								<input class="inputTxt inputTxtlong" name="elseFileName[0]" value="" flag="istips"> 
                				<span class="ipt-tips">文件名称</span>
							</div>
							<p class="fl">
								<input type="button" name="elseFileId[0]" class="btn btn-primary fileUploadButton" value="上传"  onclick="uploadFiles('${ctx}','supplierInfo.license',this,1);"/>
           			   			<span id="upfileShow" class="seach_checkbox"></span>
           			   			<span class="fileLogo"></span>
								<input type="hidden" name="elseFileSize" value="0"/>
							</p>
							<%--<div class="ydbz_s qdzz-add">添加+</div>--%>
							<input type="button" value="添加" class="ydbz_x btn btn-primary qdzz-add">
							</dt>
						</c:otherwise>
						</c:choose>
					</dl>
				</div>
					<input  type="hidden" name="businessLicense"/>
					<input  type="hidden" name="license"/>
					<input  type="hidden" name="taxCertificate"/>
					<input  type="hidden" name="organizeCertificate"/>
					<input  type="hidden" name="idCard"/>
					<input  type="hidden" name="bankOpenLicense"/>
					<input  type="hidden" name="travelAptitudes"/>
					<input id ="elseFileHidden" type="hidden" name="elseFile"/>
				<div class="dbaniu " style=" margin-left:100px;">
					<%--<a class="ydbz_x gray" href="${ctx}/supplier/supplierInfoList">返&nbsp;&nbsp;&nbsp;回</a>--%>
					<%--<a class="ydbz_x" href="${ctx}/supplier/supplierThirdForm?supplierId=${supplierId}">上一步</a>--%>
					<%--<input type="submit" value="提交" class="ydbz_x submit"></div>--%>
					<input type="button" value="返&nbsp;&nbsp;&nbsp;回" class="ydbz_x btn" onclick="location.href='${ctx}/supplier/supplierInfoList'">
					<input type="button" value="上一步" class="ydbz_x btn" onclick="location.href='${ctx}/supplier/supplierThirdForm?supplierId=${supplierId}'">
					<input type="submit" value="提交" class="ydbz_x btn btn-primary submit">
				</div>
				 </form>
				<!--右侧内容部分结束-->
</body>
</html>
