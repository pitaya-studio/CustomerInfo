<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <title>库存-库存添加页</title>
    <!--[if lte IE 6]>
    <link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css"/>
    <script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
    <![endif]-->
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
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
        .sub_main_bands_sel li i{
		    right: 6px;
		    top: -10px;
		    position: absolute;
		    color: #cccccc;
		    height: 12px;
		    display: none;
		}
		.sub_main_bands_sel li:hover i{
		    display: block;
		}
    </style>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery.tabScroll.js"></script>
    <script type="text/javascript" src="${ctxStatic}/mtour/static/js/common/quauq.base64.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script type="text/javascript">
	    $(function () {
	    	$ctx = "${ctx}";
	    });
    </script>
    <script type="text/javascript">
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
						
						$(obj).parent().next(".batch-ol").append('<li><span>'+ $(obj1).val() +'</span><a class="batchDel" href="javascript:void(0)" onclick="downloads(\''+ $(obj1).prev().val() +'\');">下载</a> <a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(\''+ $(obj1).prev().val() +'\',\''+ $(obj).attr("name") +'\',this);">删除</a></li>');
						
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
    function inputTipText(){    
		$("input[class*=grayTips]") //所有样式名中含有grayTips的input   
		.each(function(){   
   		var oldVal=$(this).val();   //默认的提示性文本   
   		$(this)   
  	 	.css({"color":"#888"})  //灰色   
   		.focus(function(){   
    	if($(this).val()!=oldVal){$(this).css({"color":"#000"});}else{$(this).val("").css({"color":"#888"});}   
   		})   
   		.blur(function(){   
    	if($(this).val()==""){$(this).val(oldVal).css({"color":"#888"});}   
   		})   
   		.keydown(function(){$(this).css({"color":"#000"});});   
     
	});   
	}   
   //关闭窗口
	function closeWindows(){
		window.open("${ctx}/cruiseshipStock/cruiseshipStockList");
		window.close();
    }
	</script>
    <script type="text/javascript" src="${ctxStatic}/modules/store/addStore.js"></script>
</head>
<body>
<!--右侧内容部分开始-->
<div class="produceDiv">
    <div style="width:100%; height:20px;"></div>
    <form enctype="multipart/form-data" method="post" action="" class="form-search" id="addForm" novalidate="novalidate">
    	<input id="token" name="token" type="hidden" value="${token}" />
       <div class="messageDiv">
           <div class="kongr"></div>
           <div style="display: block;" class="ydxbd">
               <div class="activitylist_bodyer_right_team_co1">
                   <label><i class="xing">*</i>选择游轮：</label>
                   <select id="cruise">
                       <option value="-1">请选择</option>
                       <c:forEach items="${cruiseshipList}" var="item">
							    <option value="${item.uuid}" >
						    		${item.name}
						    	</option>
						</c:forEach>
                   </select>
               </div>
               <div class="activitylist_bodyer_right_team_co2">
                   <label><i class="xing">*</i>选择船期：</label>
                   <input id="cruiseDate" class="inputTxt dateinput" style="text-indent: 0">
               </div>
           </div>
           <!--滚动日期&列表部分-->
           <div class="date-roll-list-table"></div>
           <div class="sub_main_bands_sel" id="selectedDates">
               <ul style="position: absolute; left: 35px; margin: 0px 35px 0px 0px;"></ul>
               <i class="sub_main_bands_sel_l_greys"></i>
               <i class="sub_main_bands_sel_r_greys"></i>
           </div>
           <div id="cabinInfo" class="sub_main_bands"></div>
           <div class="mod_information display-none" id="otherInfo">
               <div class="mod_information_d">
               		<span style=" font-weight:bold; padding-left:20px;float:left">其他信息</span>
               </div>
               <div id="secondStepEnd">
                   <div class="add-remarks">
                       <span>备注：</span><textarea></textarea>
                   </div>
                   <div class="mod_information_3 update-document">
                       <div class="upload_file_list">
                   			    <!-- 文件上传的处理 -->
								<p class="maintain_pfull new_kfang">
									<label>游轮资料：</label> 
									<input type="button" name="hotelAnnexDocId" value="上传" class="btn btn-primary" onClick="uploadFiles('${ctx}',null,this)"/> 
									<em class="tips_color">多文件，多格式上传附件。单个附件不大于20M，总共不大于20M。</em>
									<ol class="batch-ol"><li></li></ol>
								</p>
                            </div>
                       </div>
                    </div>
                </div>
                <div id="thirdStepDiv">
                    <div class="release_next_add">
                        <%-- <input value="关闭" class="btn btn-primary gray" type="button" id="close" onclick="javascript:window.close();window.open('${ctx}/cruiseshipStock/cruiseshipStockList');"> --%>
                        <input value="关闭" class="btn btn-primary gray" type="button" id="close" onclick="closeWindows()">
                        <input value="保存" class="btn btn-primary" type="button" id="save">
                    </div>
                </div>
            </div>
        </form>
    </div>
    <!--右侧内容部分结束-->
</body>
</html>
