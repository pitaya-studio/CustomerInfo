<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>基础信息维护-游轮-修改</title>
    <meta name="decorator" content="wholesaler"/>
    <!-- <link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon"/> -->
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
     function commenFunction(obj,fileIDList,fileNameList,filePathList) {
	 		var fileIdArr = fileIDList.split(";");
	 		var fileNameArr = fileNameList.split(";");
	 		var filePathArr = filePathList.split(";");
	 		$(obj).parent().parent().next().find("li.lio").remove();
	 		for(var i=0; i<fileIdArr.length-1; i++) {
					//<span><a href="javascript:void(0);" title="点击下载附件">0001.jpg</a></span>
	    			var html = [];
	    			html.push('<li class="lio">');
	    			html.push('<a href="javascript:void(0)" onclick="downloads('+ fileIdArr[i] +')">'+fileNameArr[i]+'</a>');
	    			html.push('<a class="ico-download" title="删除" onclick="deleteFile('+"null"+',this)">删除</a>');
	    			html.push('<input type="hidden" name="docId"  value="' + fileIdArr[i] + '" />');
	    			html.push('<input type="hidden" name="docName" value="'+ fileNameArr[i] +'"/>');
	    			html.push('<input type="hidden" name="docPath" value="'+ filePathArr[i] +'"/>');
	    			html.push('</li>');
	    			$(obj).parent().parent().next().find("ol.batch-ol").append(html.join(''));
	 		}
	 		
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
	   		var div=$("div[name='cabinType']");
	   		for(var j=0;j<div.size();j++){
	   			var cabinTypeName="";
	   			if($(div[j]).find("input.cabinTypeName")!=undefined){
	   				cabinTypeName=$(div[j]).find("input.cabinTypeName").val();
	   			}
	   			var cabinTypeUuid="";
	   			if($(div[j]).find("input.cabinTypeUuid").val()!="" ){
	   				cabinTypeUuid=$(div[j]).find("input.cabinTypeUuid").val();
	   			}
	   			if(cabinTypeNames==""){
	    			cabinTypeNames=cabinTypeUuid+","+cabinTypeName+";";
	    		}else{
	    			cabinTypeNames=cabinTypeNames+cabinTypeUuid+","+cabinTypeName+";";
	    		}
	   		}
	    	$("#cabinTypeNames").val(cabinTypeNames);
	   } 
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
    	 getCabinTypeName();
    	 getDocId();
	    $.post("${ctx}/cruiseshipInfo/update",$("#addForm").serialize(),function(data){
						if(data!="0"){
							$.jBox.confirm("保存成功,是否跳转到游轮基础信息页？", "系统提示", function(v, h, f) {
								if(v=='ok'){
									location.href="${ctx}/cruiseshipInfo/list";
								}else if(v=='cancle'){
								
								}
							});		
						}else{
							$.jBox.tip("保存失败!");
						}
							
	  	  }); 
    	
    } 
    function downloads(fieldId){
		location.href="${ctx}/cruiseshipInfo/downLoad?docId="+fieldId;
	}
    function closeUpdate(){
    	location.href="${ctx}/cruiseshipInfo/list";
    }
    var deldocId="";
    function deleteFile(docId,obj){
    	var $li=$(obj).parent();
    	if(deldocId==""){
    		if(docId!=null){
    			deldocId=docId;
    		}
    	}else{
    		if(docId!=null){
    			deldocId=deldocId+","+docId;
    		}
    	}
    	$("#deldocId").val(deldocId);
    	$li.remove();
    }
    $(function(){
		var $em=$(".em");
		for(var i=0;i<$em.size();i++){
			if(i!=$em.size()-1){
				$($em[i]).attr("style","display:none;");
			}
		}
		if($em.size()==1){
			$($em[0]).next().attr("style","display:none");
		}		
    });
    function checkform(){
		   $.ajax({
				 type:"post",
				 url:"${ctx}/cruiseshipInfo/checks",
				 data:{
					 name:$("#acitivityName").val(),
					 uuid:$("#activityId").val()
				 },
				 success:function(data){
					 if(data){
						$("#sp1").attr("style","display:inline;");
					 }else{
						
					 }
				 }
			 });	
	   }
    function spanquiet(){
	   $("#sp").attr("style","display:none;");
	   $("#sp1").attr("style","display:none;");
   }
  	
  	$(function(){
  		$.ajax({
  			type:"post",
  			url:"${ctx}/cruiseshipInfo/updateCheck",
  			data:{
  				uuid:"${cruiseshipInfo.uuid }"
  			},
  			success:function(data){
  				if(data){
  					$("div[name='cabinType']").attr("bz","nodel");
  					$("i").attr("style","display:none;");
  				}else{
  					
  				}
  			}
  		});
  	});
     </script>
</head>
<body>
			<div class="bgMainRight">
                <!--右侧内容部分开始-->
                <div class="produceDiv">
                    <div style="width:100%; height:20px;"></div>
                    <form enctype="multipart/form-data" method="post" action="/a/airTicket/save" class=" form-search"
                          id="addForm" novalidate="novalidate">
                          <input id="token" name="token" type="hidden" value="${token}" />
                          <input type="hidden" name="docIds" value="" id="docIds">
                          <input type="hidden" name="id" value="${cruiseshipInfo.id }">
                          <input type="hidden" name="deluuids" value="" id="delUuid">
                           <input type="hidden" name="cabinTypeNames" value="" id="cabinTypeNames">
                           <input type="hidden" name="deldocId" value="" id="deldocId">
                        <div class="messageDiv">
                            <div class="kongr"></div>
                            <div class="mod_information_d1">
                                <label><span class="xing">*</span>游轮名称：</label>
                                <input name="acitivityName" class="inputTxt"  maxlength="30" value="${cruiseshipInfo.name }" id="acitivityName"
                                       type="text" onblur="checkform()" onfocus="spanquiet()">
                                  <input name="activityId" id="activityId" value="${cruiseshipInfo.uuid }" type="hidden">     
                                 <span style="display:none" id="sp" class="xing">必填字段</span> 
                                 <span style="display:none" id="sp1" class="xing">游轮名称重复</span>      
                            </div>
                            <div class="add-del-container">
                            <c:forEach items="${cruiseshipCabins }" var="c">
                                <div class="mod_information_d2" name="cabinType" style="min-width: 410px;">
                                    <label><span style="color:red">*</span>舱型：</label>
                                    <input name="cabinTypeName" value="${c.name }" maxlength="20" type="text" class="cabinTypeName"
                                           placeholder="最大20个字">
                                     <input type="hidden" value="${c.uuid }" name="cabinTypeUuid" class="cabinTypeUuid">      
                                    <span><em class="em"></em><i></i></span>
                                </div>
                             </c:forEach>   
                            </div>
                            <!--填写价格开始-->
                            <div style="" class="mod_information" id="secondStepDiv">
                                <div class="mod_information_d" id="secondStepTitle"><span
                                        style=" font-weight:bold; padding-left:20px;float:left">其他信息</span></div>
                                <div id="secondStepEnd">
                                    <div class="add-remarks">
                                        <span>备注：</span>
                                        <textarea name="memo" cols="" rows="" >${cruiseshipInfo.memo }</textarea></div>
                                </div>
                                <div class="kong"></div>
                            </div>

                            <!--填写价格结束-->
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
                                                            <c:forEach items="${list }" var="doc">
                                                                <li>
                                                                    <a href="javascript:void(0)" onclick="downloads(${doc.docId})">${doc.docName }</a>
                                                                    <a style="margin-left:10px;"
                                                                       href="javascript:void(0)"
                                                                       onclick="deleteFile('${doc.uuid}',this)">删除
                                                                    </a>
                                                                </li>
                                                              </c:forEach>  
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
                                    <input value="关闭"  onclick="closeUpdate();"  class="btn btn-primary gray" type="button">
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
