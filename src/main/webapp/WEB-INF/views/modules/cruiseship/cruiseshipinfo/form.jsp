<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN""http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>基础信息维护-游轮-新增</title>
    <meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <!-- <link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon"/> -->
    <!--[if lte IE 6]>
    <link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css"/>
    <script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
    <![endif]-->
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/quauq.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css"/>
    <style type="text/css">
        .uiPrint span {
            width: 140px;
        }

        .pop-content {
            width: 100%;
            overflow: hidden;
            padding: 18px 0;
            display: none;
            background: #f4f4f4;
        }
    </style>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/modules/basic-info/cruise/addCruise.js"></script>
    <script type="text/javascript">
    function closeWindows(){
		window.open("${ctx}/cruiseshipInfo/list");
		window.close();
    }

	//上传文件后回显文件
	 function commenFunction(obj,fileIDList,fileNameList,filePathList) {
	 		var fileIdArr = fileIDList.split(";");
	 		var fileNameArr = fileNameList.split(";");
	 		var filePathArr = filePathList.split(";");
	 		$(obj).parent().parent().next().find("ol.batch-ol").html('');
	 		for(var i=0; i<fileIdArr.length-1; i++) {
					//<span><a href="javascript:void(0);" title="点击下载附件">0001.jpg</a></span>
	    			var html = [];
	    			html.push('<li>');
	    			html.push('<a onclick="downloads('+ fileIdArr[i] +')">'+fileNameArr[i]+'</a>');
	    			html.push('<a class="ico-download" title="删除" onclick="deleteFile(this)">删除</a>');
	    			html.push('<input type="hidden" name="docId"  value="' + fileIdArr[i] + '" />');
	    			html.push('<input type="hidden" name="docName" value="'+ fileNameArr[i] +'"/>');
	    			html.push('<input type="hidden" name="docPath" value="'+ filePathArr[i] +'"/>');
	    			html.push('</li>');
	    			$(obj).parent().parent().next().find("ol.batch-ol").append(html.join(''));
	 		}
	    }
	    //保存
	    function save(){
	    	if($("#sp1").attr("style")=="display:inline;"){
	    		return;
	    	}
	    	var acname=$("#acitivityName").val();
	    	if(acname==""){
	    		$("#sp").attr("style","display:inline;");
	    		return;
	    	}
	    	var input=$("input[name='cabinTypeName']");
	    	for(var i=0;i<input.size();i++){
	    		if($(input[i]).val()==""){
	    			$.jBox.tip("舱型为必填字段!");
	    			return;
	    		}
	    	}
	    	getDocId();
	    	 getCabinTypeName();
	    	$.post("${ctx}/cruiseshipInfo/save",$("#addForm").serialize(),function(data){
						if(data!="0"){
							$.jBox.confirm("保存成功,是否跳转到游轮基础信息页？", "系统提示", function(v, h, f) {
								if(v=='ok'){
									closeWindows();
								}else if(v=='cancle'){
								
								}
							});		
						}else{
							$.jBox.tip("保存失败!");
						}
							
	  	  }); 
	   } 
	   function getDocId(){
	    	var docIds="";
	    	var inputs=$('input[name="docId"]');
	    	for(var i=0;i<inputs.size();i++){
	    		var docId=$(inputs[i]).val();
	    		if(docIds==""){
	    			docIds=docId;
	    		}else{
	    			docIds=docIds+","+docId;
	    		}
	    	}
	    	$("#docIds").val(docIds);
	    } 
	   function getCabinTypeName(){
	   		var cabinTypeNames="";
	    	var inputs=$('input[name="cabinTypeName"]');
	    	for(var i=0;i<inputs.size();i++){
	    		var cabinTypeName=$(inputs[i]).val();
	    		if(cabinTypeNames==""){
	    			cabinTypeNames=cabinTypeName;
	    		}else{
	    			cabinTypeNames=cabinTypeNames+","+cabinTypeName;
	    		}
	    	}
	    	$("#cabinTypeNames").val(cabinTypeNames);
	   } 
	   function downloads(fieldId){
		   location.href="${ctx}/cruiseshipInfo/downLoad?docId="+fieldId;
	   }
	   function deleteFile(obj){
		   $(obj).parent().remove();
	   }
	   function checkform(){
		   $.ajax({
				 type:"post",
				 url:"${ctx}/cruiseshipInfo/check",
				 data:{
					 name:$("#acitivityName").val()
				 },
				 success:function(data){
					 if(data){
						$("#sp1").attr("style","display:inline;")
					 }else{
						
					 }
				 }
			 });	
	   }
	   function spanquiet(){
			   $("#sp").attr("style","display:none;");
		   $("#sp1").attr("style","display:none;");
	   }
    </script>
</head>
<body>
		<div class="bgMainRight">
                <!--右侧内容部分开始-->
                <div class="produceDiv">
                    <div style="width:100%; height:20px;"></div>
                    <form enctype="multipart/form-data" method="post" action="/a/airTicket/save" class="  "
                          id="addForm" novalidate="novalidate">
                          <input type="hidden" name="docIds" value="" id="docIds"/>
                           <input type="hidden" name="cabinTypeNames" value="" id="cabinTypeNames"/>
                           <input id="token" name="token" type="hidden" value="${token}" />
                        <div class="messageDiv">
                            <div class="kongr"></div>
                            <div class="mod_information_d1">
                                <label><span class="xing">*</span>游轮名称：</label>
                                <input id="acitivityName" name="acitivityName" class="required" value="" maxlength="30"
                                       type="text" placeholder="最大30个字" onblur="checkform()" onfocus="spanquiet()">
                                 <span style="display:none" id="sp" class="xing">必填字段</span> 
                                 <span style="display:none" id="sp1" class="xing">游轮名称重复</span>     
                            </div>
                            <div class="add-del-container">
                                <div class="mod_information_d2" name="cabinType" style="min-width: 410px;">
                                    <label><span style="color:red">*</span>舱型：</label>
                                    <input name="cabinTypeName" maxlength="20" type="text"
                                           placeholder="最大20个字" >
                                    <span><em></em><i style="display:none;"></i></span>
                                </div>
                            </div>
                            <!--填写价格开始-->
                            <div style="" class="mod_information" id="secondStepDiv">
                                <div class="mod_information_d" id="secondStepTitle"><span
                                        style=" font-weight:bold; padding-left:20px;float:left">其他信息</span></div>
                                <div id="secondStepEnd">
                                    <div class="add-remarks">
                                        <span>备注：</span>
                                        <textarea name="memo" cols="" rows=""></textarea></div>
                                </div>
                                <div class="mod_information_dzhan_d" id="secondStepBtn" style="display: none;">
                                    <div class="release_next_add">
                                        <!--  input type="button" value="上一步" onclick="secondToOne()" class="btn btn-primary valid displayClick"-->
                                        <input value="下一步" onclick="secondToThird()"
                                               class="btn btn-primary valid displayClick" type="button">
                                    </div>
                                </div>
                                <div class="kong"></div>
                            </div>
                            <!--填写结束-->
                            <div style="clear:none;" class="kong"></div>
                            <div id="thirdStepDiv">
                                <!-- 上传文件 -->
                                <div class="mod_information_d7"></div>
                                <div class="mod_information_3 update-document">
                                    <div class="upload_file_list">
                                        <table style="vertical-align:middle;margin-top:10px;" name="company_logo"
                                               border="0">
                                            <tbody>
                                            <tr>
                                                <td class="tr"><label>游轮资料：</label></td>
                                                <td></td>
                                                <td><input id="airticket_attach" name="airticket_attach"
                                                           class="mod_infoinformation3_file" value="选择文件"
                                                           onclick="uploadFiles('${ctx}','',this,'false')" type="button">
                                                    <div class="uploadPath" style="display: none"></div>
                                                    <div id="currentFiles" style="display: none"></div>
                                                    <span class="color-gray">多文件，多格式上传附件，单个附件不大于20M，总共不大于20M</span>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="3">
                                                    <div class="mod_information_dzhan">

                                                        <div class="batch">
                                                            <ol class="batch-ol">
                                                                <li>
                                                                    
                                                                </li>
                                                            </ol>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <div class="release_next_add">
                                    <input value="关闭"  onclick="closeWindows()"  class="btn btn-primary gray" type="button">
                                    <input value="保存" onclick="save()" class="btn btn-primary" type="button">
                                </div>
                                
                            </div>
                           </div> 
                    </form>
                </div>
                <!--右侧内容部分结束-->
           </div>     
</body>
</html>

