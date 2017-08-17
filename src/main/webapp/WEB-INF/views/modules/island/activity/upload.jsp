<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
 <html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="decorator" content="wholesaler"/>
    <title></title>
    <link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
    <link type="text/css" rel="stylesheet" href="jquery-validation/1.11.0/jquery.validate.min.css" />
    <link type="text/css" rel="stylesheet" href="jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
    <link type="text/css" rel="stylesheet" href="jqueryUI/themes/base/jquery.ui.all.css" />
    <script type="text/javascript" src="js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/tmp.products.js"></script>
    <script type="text/javascript">
  
    function flashChecker()
    {
    	var hasFlash=0;     //是否安装了flash
    	var flashVersion=0;   //flash版本
    	
    	if(document.all){
    		var swf ;
    		try
    		{
    		   swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash'); 
    		} catch (e) {
    			hasFlash=1;
    			alert("请您退出当前浏览器,并安装当前浏览器的flash player软件,然后才能上传文件");
    		}
    		if(swf) { 
    			hasFlash=1;
    			VSwf=swf.GetVariable("$version");
    			flashVersion=parseInt(VSwf.split(" ")[1].split(",")[0]); 
    		}
    	}else{
    		if (navigator.plugins && navigator.plugins.length > 0){
    			var swf=navigator.plugins["Shockwave Flash"];
    			if (swf)  {
    			   hasFlash=1;
    		       var words = swf.description.split(" ");
    		       for (var i = 0; i < words.length; ++i){
    		         if (isNaN(parseInt(words[i]))) continue;
    		         flashVersion = parseInt(words[i]);
    			   }
    			}
    		}
    	}
    	return {f:hasFlash,v:flashVersion};
    }
    //方法参数：${ctx}、显示上传文件名称input的id、当前点击元素
    function uploadFiles(ctx, inputId, obj) {
    	var fls=flashChecker();
    	var s="";
    	if(fls.f) {
//    		alert("您安装了flash,当前flash版本为: "+fls.v+".x");
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
    	
    	$.jBox("iframe:"+ ctx +"/activity/manager/uploadFilesPage", {
    	    title: "多文件上传",
    		width: 340,
       		height: 365,
       		buttons: {'关闭':true},
       		persistent:true,
       		loaded: function (h) {},
       		submit: function (v, h, f) {
    			$(h.find("iframe")[0].contentWindow.file_upload).next("p").find(".successUpload").click();
    			if($(obj).parent().find("#currentFiles").find("[name='docOriName']").length != 0) {
    				
    				$(obj).parent().find("#currentFiles").find("[name='docOriName']").each(function(index, obj1) {
    					//如果是产品行程介绍
    					if($(obj).attr("name") == 'introduction') {
    						$(obj).next().next("#introductionVaildator").val("true").trigger("blur");
    					}
    					//如果是签证资料的文件上传
    					if($(obj).attr("name").indexOf("signmaterial") >= 0) {
    						$(obj).parent().parent().parent().parent().next(".batch-ol").append('<li><span>'+ $(obj1).val() +'</span><a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(\''+ $(obj1).prev().val() +'\',\''+ $(obj).attr("name") +'\',this);">删除</a></li>');
    					}else{
    						$(obj).next(".batch-ol").append('<li><span>'+ $(obj1).val() +'</span><a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(\''+ $(obj1).prev().val() +'\',\''+ $(obj).attr("name") +'\',this);">删除</a></li>');
    					}
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
    			
    			//如果是产品行程介绍文件删除的话，需要进行必填验证
    			if("introduction" == objName) {
    				if(0 == $("#introductionVaildator").prev(".batch-ol").find("li").length) {
    					$("#introductionVaildator").val("").trigger("blur");
    				}
    			}
    		}
    		},{buttonsFocus:1});
    		top.$('.jbox-body .jbox-icon').css('top','55px');		
    }
    
    
    </script>
  </head>
  
  <body>
    <!-- 上传文件 -->
    <!-- 加在产品发布 form 中  -->
            <div class="ydbz_tit pl20 secondStepTitle" id="ofAnchor6">上传资料
                  <span style="float: right;padding-right: 10px;" data-target='div.mod_information_3'>收起</span>
            </div>
            <div class="mod_information_3">
                <div class="batch"  style="margin-top:10px;">
                	<label class="batch-label company_logo_pos"><span>*</span>产品行程介绍：</label>
					<input type="button" name="introduction" value="选择文件" class="mod_infoinformation3_file" onClick="uploadFiles('${ctx}',null,this)"/>  
					<ol class="batch-ol">
					<input type="hidden" name="introduction_name" value="产品行程介绍"/>
					</ol>
					<input type="text" value="" id="introductionVaildator" class="required" style="width:1px; height:1px; margin:0; padding:0; border:none; position:absolute; z-index:-1;" />
                </div>
                <div class="mod_information_d7"></div>
                <div class="batch" style="margin-top:10px;">
                	<label class="batch-label company_logo_pos">自费补充协议：</label>
					<input type="button" name="costagreement" value="选择文件" class="mod_infoinformation3_file" onClick="uploadFiles('${ctx}',null,this)"/>
					<ol class="batch-ol">
					<input type="hidden" name="costagreement_name" value="自费补充协议"/>
					</ol>
                </div>
                <div class="mod_information_d7"></div>
                <div class="batch" style="margin-top:10px;">
                	<label class="batch-label company_logo_pos">其他补充协议：</label>
					<input type="button" name="otheragreement" value="选择文件" class="mod_infoinformation3_file" onClick="uploadFiles('${ctx}',null,this)"/>
					<ol class="batch-ol">
					<input type="hidden" name="otheragreement_name" value="其他补充协议"/>
					</ol>
                </div>
                <div class="mod_information_d7"></div>
                <div class="batch" style="margin-top:10px;">
                	<label class="batch-label company_logo_pos">其他文件：</label>
					<input type="button" name="otherfile" value="选择文件" class="mod_infoinformation3_file" onClick="uploadFiles('${ctx}',null,this)"/>
					<ol class="batch-ol">
					<input type="hidden" name="otherfile_name" value="其他文件"/>
					</ol>
                </div>
             </div>

  </body>
</html>
            
            
            
            