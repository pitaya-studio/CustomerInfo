<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>渠道管理-新增渠道资质</title>

<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
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
	//渠道资质添加
	qdzz_add();
	///updateSecondForm/{id}
	$(".preButton").click(function(){
		window.location.href="${ctx}/agent/manager/updateSecondForm/${agentId}";	
	});
	
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
				if(typeof(docId)=="undefined"){
					if(!(name==null||name=="")){
						top.$.jBox.info("'其他文件'请上传之后再提交", "警告");
						return ;
					}
				}else{
					valStr += docId+","+name+";";
				}
				
			};
			$("#elseFileHidden").val(valStr);
			
			$("input[type='hidden'][name='businessLicense']").val($("input[type='hidden'][name='agentinfo.businessLicense2']").val());
			$("input[type='hidden'][name='license']").val($("input[type='hidden'][name='agentinfo.license2']").val());
			$("input[type='hidden'][name='taxCertificate']").val($("input[type='hidden'][name='agentinfo.taxCertificate2']").val());
			$("input[type='hidden'][name='organizeCertificate']").val($("input[type='hidden'][name='agentinfo.organizeCertificate2']").val());
			$("input[type='hidden'][name='idCard']").val($("input[type='hidden'][name='agentinfo.idCard2']").val());
			$("input[type='hidden'][name='bankOpenLicense']").val($("input[type='hidden'][name='agentinfo.bankOpenLicense2']").val());
			$("input[type='hidden'][name='travelAptitudes']").val($("input[type='hidden'][name='agentinfo.travelAptitudes2']").val());
			$("#formAttach").submit();
	});
	
});

//文件下载
	 function downloads(docid,activitySerNum,acitivityName,iszip){
	    	if(iszip){
	    		var zipname = activitySerNum;
	    		window.open("${ctx}/sys/docinfo/zipdownload/"+docid+"/"+zipname);
	    	}
	    		
	    	else
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
	    		$("input[name='"+obj.name+"']").after("<span><b>"+fileNameList+"</b><input type='hidden' name='"+obj.name+"' value='"+fileIDList+"'/> <a style='margin-left:10px;' href='javascript:void(0)' onclick='downloads("+fileIDList+",\""+fileNameList+"\",1,true)'>下载</a> <a style='margin-left:10px;' href='javascript:void(0)' onclick='deleteFiles("+fileIDList+",this)'>删除</a></span>");
	 		}
	 }
 	//渠道 渠道资质
	function qdzz_add(){
		$('.wbyu-bot').on('click','.qdzz-add-remove',function(){
				$(this).parent('dt').remove();
				$(".uploadFile").each(function(index,element){
						//更改name
						var fileName = $(element).find('div input.inputTxtlong').attr("name");
						var fileNameArr = fileName.split("[");
						var fileNameTrue = fileNameArr[0];
						fileNameTrue = fileNameTrue+"["+index+"]";
						$(element).find('div input.inputTxtlong').attr("name",fileNameTrue);
						//
						var buttonName = $(element).find('p .fileUploadButton').attr("name");
						var buttonNameArr = buttonName.split("[");
						var buttonNameTrue = buttonNameArr[0];
						buttonNameTrue = buttonNameTrue+"["+index+"]";
						$(element).find('p .fileUploadButton').attr("name",buttonNameTrue);
				});
		});
		$('.qdzz-add').click(function(){
			var i = $(".uploadFile").length;
// 			$(this).parent().append('<dt class="uploadFile"><label>其他文件：</label><div class="pr fl"><input value=""  class="inputTxt inputTxtlong" name="elseFileName['+i+']" id="" flag="istips"><span class="ipt-tips">文件名称</span></div><p class="fl"> <input type="button" name="elseFileId['+i+']" id="elseFileId['+i+']"  class="btn btn-primary fileUploadButton" value="上传"  onclick="uploadFiles(\'${ctx}\',\'agentinfo.license\',this,1);"/><span id="upfileShow" class="seach_checkbox"></span><span class="fileLogo"></span></p><div class="ydbz_s qdzz-add-remove gray">删除</div></dt>');
			$("dt.uploadFile").last().after('<dt class="uploadFile"><label>其他文件：</label><div class="pr fl"><input value=""  class="inputTxt inputTxtlong" name="elseFileName['+i+']" id="" flag="istips"><span class="ipt-tips">文件名称</span></div><p class="fl"> <input type="button" name="elseFileId['+i+']" id="elseFileId['+i+']"  class="btn btn-primary fileUploadButton" value="上传"  onclick="uploadFiles(\'${ctx}\',\'agentinfo.license\',this,1);"/><span id="upfileShow" class="seach_checkbox"></span><span class="fileLogo"></span></p><input type="button"  class="btn qdzz-add-remove" value="删除"></dt>');//bug17525统一按钮样式
			inputTips();
			//addClickEvent($(this).parent().next().find("input[type='file']"));
		});
		
	}   
	
</script>
</head>
<body>
<style type="text/css">label{ cursor:inherit;}</style>
<page:applyDecorator name="agent_op_head" >
</page:applyDecorator>
<!-- <div id="sea"> -->
<!-- 	<div class="main"> -->
<!--         <div class="main-right"> -->
<!--             <ul class="nav nav-tabs"> -->
<!--             </ul> -->
<!--             <div class="bgMainRight"> -->
            	<!--右侧内容部分开始-->
            
				<div class="supplierLine">
					<a href="javascript:void(0)">基本信息填写</a>
					<a href="javascript:void(0)">银行账户</a>
					<a href="javascript:void(0)" class="select">资质上传</a>
				</div>
				<div class="qdgl-cen">
					<dl class="wbyu-bot wbyu-bot2">
						<dt>
							<label>营业执照：</label>
							<!-- 
							<input type="text" class="w210" name="" disabled="disabled">
							<input type="button" value="浏览" class="btn btn-primary sc-chuan">
							<input type="file" class="input-file" name="">
							 -->
							<input type="button" name="agentinfo.businessLicense2" id="businessLicense"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','agentinfo.businessLicense',this,1);"/>
							<!-- 
							<span><b>Agentinfo.java</b><input type="hidden" name="agentinfo.businessLicense2" value="6187"></span>
							 -->
						</dt>
						<dt>
							<label>经营许可证：</label>
							<!-- 
							<input type="text" id="license" class="w210" name="license" disabled="disabled" onclick="uploadFiles('${ctx}','license',this);">
							<input type="button" value="浏览" class="btn btn-primary sc-chuan">
							<input type="file" class="input-file" name="">-->
							<input type="button" name="agentinfo.license2" id="license"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','agentinfo.license',this,1);"/>
           			   		
						</dt>
						<dt>
							<label>税务登记证：</label>
							<!--  
							<input type="text" class="w210" name="" disabled="disabled">
							<input type="button" value="浏览" class="btn btn-primary sc-chuan">
							<input type="file" class="input-file" name="">
							-->
							<input type="button" name="agentinfo.taxCertificate2" id="taxCertificate"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','agentinfo.license',this,1);"/>
						</dt>
						<dt>
							<label>组织机构代码证：</label>
							<!--  
							<input type="text" class="w210" name="" disabled="disabled">
							<input type="button" value="浏览" class="btn btn-primary sc-chuan">
							<input type="file" class="input-file" name="">
							 -->
							 <input type="button" name="agentinfo.organizeCertificate2" id="organizeCertificate"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','agentinfo.license',this,1);"/>
						</dt>
						<dt>
							<label>公司法人身份证（正反面在一起）：</label>
							<!-- 
							<input type="text" class="w210" name="" disabled="disabled">
							<input type="button" value="浏览" class="btn btn-primary sc-chuan">
							<input type="file" class="input-file" name=""> -->
							<input type="button" name="agentinfo.idCard2" id="idCard"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','agentinfo.license',this,1);"/>
							
						</dt>
                        <dt>
							<label>公司银行开户许可证：</label>
							<!-- 
							<input type="text" class="w210" name="" disabled="disabled">
							<input type="button" value="浏览" class="btn btn-primary sc-chuan">
							<input type="file" class="input-file" name=""> -->
							<input type="button" name="agentinfo.bankOpenLicense2" id="bankOpenLicense"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','agentinfo.license',this,1);"/>
						</dt>
						<dt>
							<label>旅游业资质：</label><!-- 
							<input type="text" class="w210" name="" disabled="disabled">
							<input type="button" value="浏览" class="btn btn-primary sc-chuan">
							<input type="file" class="input-file" name=""> -->
							<input type="button" name="agentinfo.travelAptitudes2" id="travelAptitudes"  class="btn btn-primary" value="上传"  onclick="uploadFiles('${ctx}','agentinfo.license',this,1);"/>
						</dt>
						<dt class="uploadFile">
							<label>其他文件：</label>
							<div class="pr fl">
								<input value="" class="inputTxt inputTxtlong" name="elseFileName[0]"  flag="istips"> 
                				<span class="ipt-tips">文件名称</span>
							</div>
							<p class="fl">
								<input type="button" name="elseFileId[0]"   class="btn btn-primary fileUploadButton" value="上传"  onclick="uploadFiles('${ctx}','agentinfo.license',this,1);"/>
           			   			<span id="upfileShow" class="seach_checkbox"></span>
           			   			<span class="fileLogo"></span>
							</p>
							<%--bug17525统一按钮样式--%>
							<%--<div class="ydbz_s qdzz-add">添加+</div>--%>
							<input type="button"  class="btn btn-primary qdzz-add" value="添加">
						</dt>
						<!-- 
						<dt class="uploadFile" style="display:none">
							<label>其他文件：</label>
							<div class="pr fl">
								<input value=""  class="inputTxt inputTxtlong" name="elseFileName['+i+']" id="" flag="istips">
								<span class="ipt-tips">文件名称</span>
							</div>
							<p class="fl">
								 <input type="button" name="elseFileId['+i+']" id="elseFileId['+i+']"  class="btn btn-primary fileUploadButton" value="上传"  onclick="uploadFiles(\'${ctx}\',\'agentinfo.license\',this,1);"/>
								 <span id="upfileShow" class="seach_checkbox"></span>
								 <span class="fileLogo"></span>
							</p>
							<div class="ydbz_s qdzz-add-remove gray">删除</div>
						</dt>
						 -->
					</dl>
				</div>
				<form:form action="${ctx }/agent/manager/saveThirdForm/${agentId }" method="post" id="formAttach" modelAttribute="agentinfo">
					<input  type="hidden" name="businessLicense"/>
					<input  type="hidden" name="license"/>
					<input  type="hidden" name="taxCertificate"/>
					<input  type="hidden" name="organizeCertificate"/>
					<input  type="hidden" name="idCard"/>
					<input  type="hidden" name="bankOpenLicense"/>
					<input  type="hidden" name="travelAptitudes"/>
					<input id ="elseFileHidden" type="hidden" name="elseFile"/>
				 </form:form>
				<div class="dbaniu " style=" margin-left:100px;">
<!-- 					<a class="ydbz_s gray">返回</a> -->
					<%--bug17525统一按钮样式--%>
					<%--<a class="ydbz_s preButton">上一步</a>--%>
					<%--<a class="ydbz_s submitButton">提交</a>--%>
					<input type="button"  class="btn btn-primary preButton" value="上一步">
					<input type="button"  class="btn btn-primary submitButton" value="提交">
				</div>
			
				<!--右侧内容部分结束-->
<!--             </div> -->
<!--         </div> -->
<!-- 	</div> -->
    <!--footer
    <div class="bs-footer">
        <p>公司电话：010-85711691 | 技术支持电话：010-85711691-8006 | 客服电话：400-018-5090  | 传真：010-85711891<br/>Copyright &copy; 2012-2014 接待社交易管理后台</p>
        <div class="footer-by">Powered By Trekiz Technology</div>
    </div>-->
    <!--footer***end-->
<!-- </div> -->
</body>
</html>
