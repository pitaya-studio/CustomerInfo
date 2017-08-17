<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>基础信息维护-酒店-基础信息添加</title>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<%@include file="/WEB-INF/views/include/treetable.jsp"%>
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<!-- <script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script> -->
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/default.validator.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<!--基础信息维护模块的脚本-->
<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
<script type="text/javascript">
		var $ctx = "${ctx}";
		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					
					//酒店主题
					var topics = [];
					$("[name=topics]:checkbox:checked").each(function(){topics.push($(this).val())});
					$("#topic").val(topics.join(","));
					//酒店设施
					var facilitiess = [];
					$("[name=facilitiess]:checkbox:checked").each(function(){facilitiess.push($(this).val())});
					$("#facilities").val(facilitiess.join(","));
					//酒店特色
					var features = [];
					$("[name=features]:checkbox:checked").each(function(){features.push($(this).val())});
					$("#feature").val(features.join(","));
					// 20150812 add by WangXK
					var travelerTypeUuids="";
					var travelerTypeNames="";
		            $("input[name='travelerType']:checkbox").each(function(){ 
		                if($(this).attr("checked")){
		                	travelerTypeUuids += $(this).val()+",";
		                	travelerTypeNames += $(this).attr("data-value")+",";
		                }
		            });
		            $("#travelerTypeUuids").val(travelerTypeUuids);
		            $("#travelerTypeNames").val(travelerTypeNames);
					
					//如果酒店类型是内陆 则清空 海岛信息
					if($("#areaType").val()== 1){
						$("#islandUuid").val('');
						$("#islandWay").val('');
					}
					
					var url = "";
					if($("#id").val()=='') {
						url="${ctx}/hotel/save";
					} else {
						url="${ctx}/hotel/update";
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
				errorContainer: "#messageBox",
				errorElement: "em" ,
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
			//天数插件
			$( ".spinner" ).spinner({
				spin: function( event, ui ) {
				if ( ui.value > 365 ) {
					$( this ).spinner( "value", 1 );
					return false;
				} else if ( ui.value < 0 ) {
					$( this ).spinner( "value", 365 );
					return false;
				}
				}
			});
			domesticOverseas(obj);
			//设置酒店类型默认值
			typeOfHotel($("#areaType"));
			
			//初始化名称部分组件展开
			if(($("#spelling").val() != '') || ($("#shortSpelling").val() != '') || ($("#nameEn").val() != '') || ($("#shortNameEn").val() != '')) {
				showText($("#showTextButton"));
			}
		});
		
		//酒店类别添加
		function addHotelType(obj){
			if($(obj).find('option:last').attr('selected') != 'selected') {
				return ;
			}
			var html = '<div class="jbox_type">'+
			   '<p><label style="width:90px">酒店类别：</label><input type="text" id="hotelTypeLabel" name="hotelTypeLabel"/></p>'+
	           '<p><label style="width:90px"><em class="xing">*</em>排序：</label><input type="text" maxlength="4" id="hotelTypeSort" name="hotelTypeSort"/></p>'+
			   '<p><label style="width:90px">描述：</label><textarea id="hotelTypeDescription" maxlength="99" name="hotelTypeDescription" ></textarea></p>'+
	           '</div>';
			$.jBox(html, { title: "添加酒店类别", submit: function (v, h, f) {
			    if (f.hotelTypeLabel == '') {
			        $.jBox.tip("请输入酒店类别。", 'error', { focusId: "hotelTypeLabel" }); 
			        return false;
			    } 
			    if (f.hotelTypeSort == '') {
			        $.jBox.tip("请输入酒店类别的排序。", 'error', { focusId: "hotelTypeSort" }); 
			        return false;
			    }
			    var reg = new RegExp("^[0-9]*$");
				if(!reg.test(f.hotelTypeSort)) {
					$.jBox.tip("酒店类别的排序只能输入整数。", 'error', { focusId: "hotelTypeSort" }); 
					return false;
				} 
			    if (f.hotelTypeDescription.length>=30) {
			        $.jBox.tip("酒店类别的描述不能大于30字数。", 'error', { focusId: "hotelTypeDescription" }); 
			        return false;
			    }
			    $.post("${ctx}/sysCompanyDictView/saveAjax", {"type":"hotel_type","label":f.hotelTypeLabel,"sort":f.hotelTypeSort,"description":f.hotelTypeDescription},
					function(data){
						if(data.result=="1"){
							top.$.jBox.tip('添加成功','success');
							$(obj).find('option:selected').before('<option value="'+data.uuid+'">'+f.hotelTypeLabel+'</option>');
							$(obj).find('option:selected').prev().prop("selected",true);
							
							return true;
						}else{
							top.$.jBox.tip(data.message,'warning');
							return false;
						}
					}
				);
			} ,height:300,persistent: true});
			debugger;
			var optionObj = $(this).find("option[checked=checked]");
			if(optionObj.val() == '0') {
				$(this).val(-1);
			}
		}
		
		//酒店星级添加
		function addHotelStar(obj){
			if($(obj).find('option:last').attr('selected') != 'selected') {
				return ;
			}
			var html = '<div class="jbox_type">'+
				'<p><label style="width:90px"><em class="xing">*</em>星级名称：</label><input type="text" id="hotelStarLabel" name="hotelStarLabel"/></p>'+
				'<p><label style="width:90px"><em class="xing">*</em>星级类型：</label>'+$("#hotelStarTypeTemplate").html()+'</p>'+
				'<p><label style="width:90px"><em class="xing">*</em>排序：</label><input type="text" maxlength="4" id="hotelStarSort" name="hotelStarSort"/></p>'+
				'<p><label style="width:90px">描述：</label><textarea id="hotelStarDescription" maxlength="99" name="hotelStarDescription" ></textarea></p>'+
      			'</div>';
			$.jBox(html, { title: "添加酒店星级", submit: function (v, h, f) {
				if (f.hotelStarLabel == '') {
			        $.jBox.tip("请输入酒店星级名称。", 'error', { focusId: "hotelStarLabel" }); 
			        return false;
			    }
				if (f.hotelStarType == '') {
			        $.jBox.tip("请选择酒店星级类型。", 'error', { focusId: "hotelStarType" }); 
			        return false;
			    }
			    if (f.hotelStarSort == '') {
			        $.jBox.tip("请输入酒店星级的排序。", 'error', { focusId: "hotelStarSort" }); 
			        return false;
			    }
			    var reg = new RegExp("^[0-9]*$");
				if(!reg.test(f.hotelStarSort)) {
					$.jBox.tip("酒店星级的排序只能输入整数。", 'error', { focusId: "hotelStarSort" }); 
					return false;
				} 
			    if (f.hotelStarDescription.length>=30) {
			        $.jBox.tip("酒店星级的描述不能大于30字数。", 'error', { focusId: "hotelStarDescription" }); 
			        return false;
			    }
			    
			    $.post("${ctx}/hotelStar/saveAjax", {"dictUuid":f.hotelStarType,"label":f.hotelStarLabel,"sort":f.hotelStarSort,"description":f.hotelStarDescription},
					function(data){
						if(data.result=="1"){
							top.$.jBox.tip('添加成功','success');
							$(obj).find('option:selected').before('<option value="'+data.uuid+'">'+f.hotelStarLabel+'</option>');
							$(obj).find('option:selected').prev().prop("selected",true);
							
							return true;
						}else{
							top.$.jBox.tip(data.message,'warning');
							return false;
						}
					}
				);
			} ,height:330,persistent: true});
		}

		
		//酒店主题添加
		function addHotelTopic(obj){
			var html = '<div class="jbox_type">'+
			   '<p><label style="width:90px"><em class="xing">*</em>酒店主题：</label><input type="text" id="hotelTopicLabel" name="hotelTopicLabel"/></p>'+
	           '<p><label style="width:90px"><em class="xing">*</em>排序：</label><input type="text" maxlength="4" id="hotelTopicSort" name="hotelTopicSort"/></p>'+
			   '<p><label style="width:90px">描述：</label><textarea id="hotelTopicDescription" maxlength="99" name="hotelTopicDescription" ></textarea></p>'+
	           '</div>';
			$.jBox(html, { title: "添加酒店主题", submit: function (v, h, f) {
			    if (f.hotelTopicLabel == '') {
			        $.jBox.tip("请输入酒店主题。", 'error', { focusId: "hotelTopicLabel" }); 
			        return false;
			    }
			    if (f.hotelTopicSort == '') {
			        $.jBox.tip("请输入酒店主题的排序。", 'error', { focusId: "hotelTopicSort" }); 
			        return false;
			    }
			    var reg = new RegExp("^[0-9]*$");
				if(!reg.test(f.hotelTopicSort)) {
					$.jBox.tip("酒店主题的排序只能输入整数。", 'error', { focusId: "hotelTopicSort" }); 
					return false;
				} 
			    if (f.hotelTopicDescription.length>=30) {
			        $.jBox.tip("酒店主题的描述不能大于30字数。", 'error', { focusId: "hotelTopicDescription" }); 
			        return false;
			    }
			    $.post("${ctx}/sysCompanyDictView/saveAjax", {"type":"hotel_topic","label":f.hotelTopicLabel,"sort":f.hotelTopicSort,"description":f.hotelTopicDescription},
					function(data){
						if(data.result=="1"){
							top.$.jBox.tip('添加成功','success');
							$(obj).before('<label><input type="checkbox" name="topics" id="topics_'+data.uuid+'" value="'+data.uuid+'">'+f.hotelTopicLabel+'</label>');
							
							return true;
						}else{
							top.$.jBox.tip(data.message,'warning');
							return false;
						}
					}
				);
			} ,height:300,persistent: true});
		}
		
		//酒店设施添加
		function addHotelFacilities(obj){
			var html = '<div class="jbox_type">'+
			   '<p><label style="width:90px"><em class="xing">*</em>酒店设施：</label><input type="text" id="hotelFacilitiesLabel" name="hotelFacilitiesLabel"/></p>'+
	           '<p><label style="width:90px"><em class="xing">*</em>排序：</label><input type="text" maxlength="4" id="hotelFacilitiesSort" name="hotelFacilitiesSort"/></p>'+
			   '<p><label style="width:90px">描述：</label><textarea id="hotelFacilitiesDescription" maxlength="99" name="hotelFacilitiesDescription" ></textarea></p>'+
	           '</div>';
			$.jBox(html, { title: "添加酒店设施", submit: function (v, h, f) {
			    if (f.hotelFacilitiesLabel == '') {
			        $.jBox.tip("请输入酒店设施。", 'error', { focusId: "hotelFacilitiesLabel" }); 
			        return false;
			    }
			    if (f.hotelFacilitiesSort == '') {
			        $.jBox.tip("请输入酒店设施的排序。", 'error', { focusId: "hotelFacilitiesSort" }); 
			        return false;
			    }
			    var reg = new RegExp("^[0-9]*$");
				if(!reg.test(f.hotelFacilitiesSort)) {
					$.jBox.tip("酒店设施的排序只能输入整数。", 'error', { focusId: "hotelFacilitiesSort" }); 
					return false;
				} 
			    if (f.hotelFacilitiesDescription.length>=30) {
			        $.jBox.tip("酒店设施的描述不能大于30字数。", 'error', { focusId: "hotelFacilitiesDescription" }); 
			        return false;
			    }
			    $.post("${ctx}/sysCompanyDictView/saveAjax", {"type":"hotel_facilities","label":f.hotelFacilitiesLabel,"sort":f.hotelFacilitiesSort,"description":f.hotelFacilitiesDescription},
					function(data){
						if(data.result=="1"){
							top.$.jBox.tip('添加成功','success');
							$(obj).before('<label><input type="checkbox" name="facilitiess" id="facilitiess_'+data.uuid+'" value="'+data.uuid+'">'+f.hotelFacilitiesLabel+'</label>');
							
							return true;
						}else{
							top.$.jBox.tip(data.message,'warning');
							return false;
						}
					}
				);
			} ,height:300,persistent: true});
		}
		
		
		//酒店特色添加
		function addHotelFeatures(obj){
			var html = '<div class="jbox_type">'+
			   '<p><label style="width:90px"><em class="xing">*</em>酒店特色：</label><input type="text" id="hotelFeaturesLabel" name="hotelFeaturesLabel"/></p>'+
			   '<p><label style="width:90px"><em class="xing">*</em>排序：</label><input type="text" maxlength="4" id="hotelFeaturesSort" name="hotelFeaturesSort"/></p>'+
			   ' <p class="maintain_pfull inputfile_box"><label style="width:90px"><em class="xing">*</em>上传图标：</label><input type="button" name="hotelFeaturesAnnexDocId" value="上传" class="btn btn-primary" onClick="uploadFiles1(\'${ctx}\',null,this)"/><ol class="batch-ol"></ol></p>'+
			   '<p><label style="width:90px">描述：</label><textarea id="hotelFeaturesDescription" maxlength="99" name="hotelFeaturesDescription" ></textarea></p>'+
	           '</div>';
			$.jBox(html, { title: "添加酒店特色", submit: function (v, h, f) {
			    if (f.hotelFeaturesLabel == '') {
			        $.jBox.tip("请输入酒店特色。", 'error', { focusId: "hotelFeaturesLabel" }); 
			        return false;
			    }
			    if (f.hotelFeaturesSort == '') {
			        $.jBox.tip("请输入酒店特色的排序。", 'error', { focusId: "hotelFeaturesSort" }); 
			        return false;
			    }
			    var reg = new RegExp("^[0-9]*$");
				if(!reg.test(f.hotelFeaturesSort)) {
					$.jBox.tip("酒店特色的排序只能输入整数。", 'error', { focusId: "hotelFeaturesSort" }); 
					return false;
				} 
				if (f.hotelFeaturesSort == '') {
			        $.jBox.tip("请输入酒店特色的排序。", 'error', { focusId: "hotelFeaturesSort" }); 
			        return false;
			    }
				if(f.docOriName == ''){
					$.jBox.tip("请选择上传图标。", 'error'); 
					return false;
				}
			    if (f.hotelFeaturesDescription.length>=30) {
			        $.jBox.tip("酒店特色的描述不能大于30字数。", 'error', { focusId: "hotelFeaturesDescription" }); 
			        return false;
			    }
			    
			    $.post("${ctx}/hotelFeature/saveAjax", 
			    	{
			    	"name":f.hotelFeaturesLabel,
			    	"sort":f.hotelFeaturesSort,
			    	"description":f.hotelFeaturesDescription,
			    	"hotelFeaturesAnnexDocId":f.hotelFeaturesAnnexDocId,
			    	"docOriName":f.docOriName,
			    	"docPath":f.docPath
			    	},
					function(data){
						if(data.result=="1"){
							top.$.jBox.tip('添加成功','success');
							$(obj).before('<label><input type="checkbox" name="features" id="features_'+data.uuid+'" value="'+data.uuid+'">'+f.hotelFeaturesLabel+'</label>');
							
							return true;
						}else{
							top.$.jBox.tip(data.message,'warning');
							return false;
						}
					}
				);
			} ,height:330,persistent: true});
		}
		
		//上传文件时，点击后弹窗进行上传文件(多文件上传)
		//方法参数：${ctx}、显示上传文件名称input的id、当前点击元素
		function uploadFiles1(ctx, inputId, obj) {
			var fls=flashChecker();
			var s="";
			if(fls.f) {
//				alert("您安装了flash,当前flash版本为: "+fls.v+".x");
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
//						if($(obj).attr("name") != 'costagreement'){
//							$(obj).next(".batch-ol").find("li").remove();
//						}
						
						
						
						$(obj).parent().find("#currentFiles").find("[name='docOriName']").each(function(index, obj1) {
							
							//只能上传一个图标 start
							$(obj1).parent().find(".uploadPath").find("input[name=docOriName][value!='"+$(obj1).val()+"']").each(function(index2, obj2){
								$(obj2).prev().eq(0).remove();
								$(obj2).next().eq(0).remove();
								$(obj2).remove();
							});
							$(obj1).parent().find(".batch-ol").find("li").each(function(){
								$(this).remove();
							});
							//只能上传一个图标 end
							
							$(obj).parent().next(".batch-ol").append('<li><span>'+ $(obj1).val() +'</span><a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo1(\''+ $(obj1).prev().val() +'\',\''+ $(obj).attr("name") +'\',this);">删除</a></li>');
							
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
		function deleteFileInfo1(inputVal, objName, obj) {
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
		
		//岛屿添加
		function addIsland(obj){
			if($(obj).find('option:last').attr('selected') == 'selected') {
				window.open( "${ctx}/island/form?source=hotelForm" ) ;
			}
		}
		
		//岛屿上岛方式添加
		function addIslandsWay(obj){
			if($(obj).find('option:last').attr('selected') != 'selected') {
				return ;
			}
			var html = '<div class="jbox_type">'+
			   '<p><label style="width:90px"><em class="xing">*</em>上岛方式：</label><input type="text" id="islandsWayLabel" name="islandsWayLabel"/></p>'+
	           '<p><label style="width:90px"><em class="xing">*</em>排序：</label><input type="text" maxlength="4" id="islandsWaySort" name="islandsWaySort"/></p>'+
			   '<p><label style="width:90px">描述：</label><textarea id="islandsWayDescription" maxlength="99" name="islandsWayDescription" ></textarea></p>'+
	           '</div>';
			$.jBox(html, { title: "添加岛屿上岛方式", submit: function (v, h, f) {
			    if (f.islandsWayLabel == '') {
			        $.jBox.tip("请输入上岛方式。", 'error', { focusId: "islandsWayLabel" }); 
			        return false;
			    }
			    if (f.islandsWaySort == '') {
			        $.jBox.tip("请输入上岛方式的排序。", 'error', { focusId: "islandsWaySort" }); 
			        return false;
			    }
			    var reg = new RegExp("^[0-9]*$");
				if(!reg.test(f.islandsWaySort)) {
					$.jBox.tip("上岛方式的排序只能输入整数。", 'error', { focusId: "islandsWaySort" }); 
					return false;
				} 
			    if (f.islandsWayDescription.length>=30) {
			        $.jBox.tip("上岛方式的描述不能大于30字数。", 'error', { focusId: "islandsWayDescription" }); 
			        return false;
			    }
			    $.post("${ctx}/sysCompanyDictView/saveAjax", {"type":"islands_way","label":f.islandsWayLabel,"sort":f.islandsWaySort,"description":f.islandsWayDescription},
					function(data){
						if(data.result=="1"){
							top.$.jBox.tip('添加成功','success');
							$(obj).find('option:selected').before('<option value="'+data.uuid+'">'+f.islandsWayLabel+'</option>');
							$(obj).find('option:selected').prev().prop("selected",true);
							
							return true;
						}else{
							top.$.jBox.tip(data.message,'warning');
							return false;
						}
					}
				);
			} ,height:300});
		}
		
		//上传文件时，点击后弹窗进行上传文件(多文件上传)
		//方法参数：${ctx}、显示上传文件名称input的id、当前点击元素
		function uploadFiles(ctx, inputId, obj) {
			var fls=flashChecker();
			var s="";
			if(fls.f) {
//				alert("您安装了flash,当前flash版本为: "+fls.v+".x");
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
//						if($(obj).attr("name") != 'costagreement'){
//							$(obj).next(".batch-ol").find("li").remove();
//						}
						
						$(obj).parent().find("#currentFiles").find("[name='docOriName']").each(function(index, obj1) {
							
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
	    
	    //表单验证
		function validate(){
			
			var isPinyin=/^[a-zA-Z]+$/; //拼音验证
			var isEnglish=/^[a-zA-Z\s]+$/; //英文验证
			var isMobile=/^(?:13\d|15\d|18\d)\d{5}(\d{3}|\*{3})$/; //手机号码验证规则
			var isPhone=/^((0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;   //座机验证规则
			
			var spelling = $.trim($("#spelling").val()); //酒店全拼
			var shortSpelling = $.trim($("#shortSpelling").val());//全拼缩写
			var nameEn = $.trim($("#nameEn").val());//英文名称
			var shortNameEn = $.trim($("#shortNameEn").val());//英文缩写
			var telephone = $.trim($("#telephone").val());//电话号码
			var fax = $.trim($("#fax").val());//传真号码
			var chargeMobile = $.trim($("#chargeMobile").val());//联系人手机
			var chargeTelephone = $.trim($("#chargeTelephone").val());//联系人电话
			var chargeFax = $.trim($("#chargeFax").val());//联系人传真
			
			if(spelling != '' && !isPinyin.test(spelling)) {
			    $.jBox.tip("酒店全拼只能输入拼音。", 'error', { focusId: "spelling" }); 
				return false;
			}
			
			if(shortSpelling != '' && !isPinyin.test(shortSpelling)) {
			    $.jBox.tip("全拼缩写只能输入拼音。", 'error', { focusId: "shortSpelling" }); 
				return false;
			}
			
			if(nameEn != '' && !isEnglish.test(nameEn)) {
			    $.jBox.tip("英文名称只能输入英文和空格。", 'error', { focusId: "nameEn" }); 
				return false;
			}
			
			if(shortNameEn != '' && !isEnglish.test(shortNameEn)) {
			    $.jBox.tip("英文缩写只能输入英文和空格。", 'error', { focusId: "shortNameEn" }); 
				return false;
			}
			
			if($("#position").val() == 1) {
				if($("#country").val()==''){
					$.jBox.tip("请选择国家");
					return false;
				} 
				if($("#province").val()==''){
					$.jBox.tip("请选择省份");
					return false;
				}
				if($("#city").val()==''){
					$.jBox.tip("请选择城市");
					return false;
				}
				if(telephone != '' && !isMobile.test(telephone)) {
				    $.jBox.tip("请输入正确的电话号码。", 'error', { focusId: "telephone" }); 
					return false;
				}
				
				if(fax != '' && !isPhone.test(fax)) {
				    $.jBox.tip("请输入正确的传真号码。", 'error', { focusId: "fax" }); 
					return false;
				}
				
				if(chargeMobile != '' && !isMobile.test(chargeMobile)){
				    $.jBox.tip("请输入正确的手机号码。", 'error', { focusId: "chargeMobile" }); 
					return false;
				}
				
				if(chargeTelephone != '' && !isPhone.test(chargeTelephone)){
				    $.jBox.tip("请输入正确的电话号码。", 'error', { focusId: "chargeTelephone" }); 
					return false;
				}
				
				
				if(chargeFax != '' && !isPhone.test(chargeFax)){
				    $.jBox.tip("请输入正确的传真号码。", 'error', { focusId: "chargeFax" }); 
					return false;
				}
			}else if($("#position").val() == 2) {
				if($("#overseas_state").val()==''){
					$.jBox.tip("请选择国家");
					return false;
				} 
				if($("#overseas_province").val()==''){
					$.jBox.tip("请选择省份");
					return false;
				}
				if($("#overseas_city").val()==''){
					$.jBox.tip("请选择城市");
					return false;
				}
				
			}

			if($("#areaType").val()== 2){
				if($("#islandUuid").val()==''){
					$.jBox.tip("请选择岛屿");
					return false;
				}
			}
			var flag = true;
			if($("#position").val() == 1) {
				$("input[name=mobiles]").each(function(index, obj) {
					if(!isMobile.test($(obj).val()) && $.trim($(obj).val())!='') {
				    	$.jBox.tip("请输入正确的手机号码。", 'error'); 
						$(obj).focus();
						flag = false;
						return false;
					}
				});
				
				flag && $("input[name=telephones]").each(function(index, obj) {
					if(!isPhone.test($(obj).val()) && $.trim($(obj).val())!='') {
				    	$.jBox.tip("请输入正确的电话号码。", 'error'); 
						$(obj).focus();
						flag = true;
						return false;
					}
					
				});
				
				flag && $("input[name=faxs]").each(function(index, obj) {
					if(!isPhone.test($(obj).val()) && $.trim($(obj).val())!='') {
				    	$.jBox.tip("请输入正确的传真号码。", 'error'); 
						$(obj).focus();
						flag = false;
						return false;
					}
				});
				
				if(!flag) {
					return false;
				}
			}
			
			$("#inputForm").submit();
		}
	</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="produceDiv">
		<form:form method="post" modelAttribute="hotel" action="${ctx}/hotel/save" class="form-search" id="inputForm" novalidate="">
			<form:hidden path="id" />
			<input name="createDate"  id="createDate" type="hidden" value="<fmt:formatDate value="${hotel.createDate}" pattern="yyyy-MM-dd" />" 
			<form:hidden path="createBy" />
			<form:hidden path="delFlag" />
			<form:hidden path="uuid" />
			<form:hidden path="wholesalerId" />
			<input type="hidden" id="travelerTypeUuids" name="travelerTypeUuids" value=""/>
			<input type="hidden" id="travelerTypeNames" name="travelerTypeNames" value=""/>
			<!--基本信息开始-->
			<div class="ydbz_tit pl20">基本信息</div>
			<div class="seach25">
                <p><span class="xing">*</span>位置属性：</p>
                <form:select path="position" class="domesticOverseas" onchange="domesticOverseas(this);">
                    <form:option value="" selected="selected">请选择</form:option>
                    <form:option value="1">境内岛屿</form:option>
                    <form:option value="2">境外岛屿</form:option>
                </form:select>
            </div>
			<div class="seach25">
				<p>
					<span class="xing">*</span>酒店类型：
				</p>
				<form:select path="areaType" onchange="typeOfHotel(this)" class="required">
					<form:option value="">请选择</form:option>
					<form:option value="1">内陆</form:option>
					<form:option value="2">海岛</form:option>
				</form:select>
			</div>
			<div class="seach25">
				<p>
					酒店类别：
				</p>
				<trekiz:defineDict name="type" input="select" type="hotel_type" defaultValue="${hotel.type}" onchange="addHotelType(this)" element="<option value='0'>+添加</option>" />
			</div>
<!-- 			<div class="seach25"> -->
<!-- 				<p>是否连锁酒店：</p> -->
<!-- 				<select name="" id=""> -->
<!-- 					<option value="">无</option> -->
<!-- 					<option value="">如家</option> -->
<!-- 					<option value="">七天</option> -->
<!-- 				</select> -->
<!-- 			</div> -->
			<div class="seach25">
				<p>
					<span class="xing">*</span>酒店集团：
				</p>
				<trekiz:defineDict name="hotelGroup" input="select" type="hotel_group" defaultValue="${hotel.hotelGroup}"  />
			</div>
			<div class="kong"></div>
			<div class="hostBasics">
				<label><em class="xing">*</em>酒店名称：</label>
				<form:input path="nameCn" maxlength="50"  class="required "/>
				<label>名称缩写：</label>
				<form:input path="shortNameCn" maxlength="50" />
				<button onclick="showText(this)" class="efx-button" type="button" id="showTextButton">
					展开全部<i></i>
				</button>
			</div>
			<div class="hostBasics" style="display:none;">
				<label>酒店全拼：</label>
				<form:input path="spelling" maxlength="50" />
				<label>全拼缩写：</label>
				<form:input path="shortSpelling" maxlength="50" />
				<div class="kong"></div>
				<label>英文名称：</label>
				<form:input path="nameEn" maxlength="50" />
				<label>英文缩写：</label>
				<form:input path="shortNameEn" maxlength="50" />
			</div>
			<div class="kongr"></div>
			<!--入住信息开始-->
			<div class="ydbz_tit pl20">入住信息</div>
			<div class="hostOccupancy">
				<div class="seach25">
					<p>酒店星級：</p>
					
						<select name="star" onchange="addHotelStar(this)">
							<option value="" >请选择</option>
							<c:if test="${!empty whoStarList }">
							<c:forEach var="entry" items="${whoStarList }">
							<option value="${entry.uuid }" <c:if test="${hotel.star ==entry.uuid}">selected</c:if>>${entry.label }</option>
							</c:forEach>
							</c:if>
							<option value="">+添加</option>
						</select>
					
					
					<span class="pl15 fcolor-ccc">此酒店的住宿标准级别</span>
				</div>
				<div class="seach25">
					<p for="spinner">酒店层高：</p>
					<input id="floor" class="number spinner" name="floor" maxlength="3" value="${hotel.floor}" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" /> 
					<span class="pl15 fcolor-ccc">此酒店楼层数量</span>
				</div>
				<div class="kong"></div>
				<div class="seach25">
					<p>开业日期：</p>
					<input name="openingDate"  id="openingDate" type="text" onclick="WdatePicker()" class="dateinput" value="<fmt:formatDate value="${hotel.openingDate}" pattern="yyyy-MM-dd" />" readonly/> 
					<span class="pl15 fcolor-ccc">酒店开业的时间</span>
				</div>
				<div class="seach25" style="width:500px; margin-right:auto;">
					<p>最后一次装修时间：</p>
					<input name="lastDecoDate" id="lastDecoDate" type="text" onclick="WdatePicker()" class="dateinput" value="<fmt:formatDate value="${hotel.lastDecoDate}" pattern="yyyy-MM-dd" />" readonly/> 
					<span class="pl15 fcolor-ccc">距离现在最近一次的装修时间是什么时候</span>
				</div>
				<div class="kong"></div>
				<div class="seach25">
					<p>入住时间：</p>
					<input name="inDate" id="inDate" type="text" onclick="WdatePicker()" class="dateinput"  value="<fmt:formatDate value="${hotel.inDate}" pattern="yyyy-MM-dd" />" readonly/> 
					<span class="pl15 fcolor-ccc">最早可以入住的时间</span>
				</div>
				<div class="seach25">
					<p>离店时间：</p>
					<input name="outDate" id="outDate" type="text" onclick="WdatePicker()" class="dateinput"  value="<fmt:formatDate value="${hotel.outDate}" pattern="yyyy-MM-dd" />" readonly/> 
					<span class="pl15 fcolor-ccc">最晚离店时间</span>
				</div>
				<div class="kongr"></div>

			</div>
			<dl class="host-check">
				<dt>酒店主题：</dt>
				<dd>
					<form:hidden path="topic" />
					<trekiz:defineDict name="topics" input="checkbox" type="hotel_topic" defaultValue="${hotel.topic}"/>
					<span class="host-check-add" onclick="addHotelTopic(this)">+添 加</span>
				</dd>
			</dl>
			<dl class="host-check">
				<dt>酒店设施：</dt>
				<dd>
					<form:hidden path="facilities" />
					<trekiz:defineDict name="facilitiess" input="checkbox" type="hotel_facilities" defaultValue="${hotel.facilities}"/>
					<span class="host-check-add" onclick="addHotelFacilities(this)">+ 添 加</span>
				</dd>
			</dl>
			<dl class="host-check">
				<dt>酒店特色：</dt>
				<dd>
					<form:hidden path="feature" />
					<c:if test="${!empty hfList }">
						<c:forEach var="entry" items="${hfList }">
						<label><input type="checkbox" value="${entry.uuid }" name="features" id="features" <c:if test="${fn:indexOf(hotel.feature, entry.uuid)>-1}">checked</c:if>>${entry.name }</label>
						</c:forEach>
					</c:if>
					<span class="host-check-add" onclick="addHotelFeatures(this)">+ 添 加</span>
					
				</dd>
			</dl>
			<dl class="host-check">
                  <dt>关联游客类型：</dt>
                  <dd>
                <input id="travelerType" name="travelerType" value="" type="hidden">
                <c:forEach items="${travelerTypes}" var="travelerType">
                	<label> <input name="travelerType" type="checkbox" value="${travelerType.uuid}" <c:if test="${travelerTypeString.contains(travelerType.uuid) }">checked="checked"</c:if> data-value="${travelerType.name}"> ${travelerType.name}</label>
                </c:forEach>
                <!-- 
                <label>
                      <input value="f5cccd2b0f3c4616bd9c5eef80500962" name="features" id="features" type="checkbox">
                      双人</label>
                <label>
                      <input value="60a37a9de7a84df8aecae4081216dd1b" name="features" id="features" type="checkbox">
                      儿童（0-2）</label>
                <label>
                      <input value="4d5dd8c6311841dd97bbe49afc9f47af" name="features" id="features" type="checkbox">
                      儿童（2-12）</label>
                
                <label><input value="4d5dd8c6311841dd97bbe49afc9f47af" name="features" id="features" type="checkbox">
                儿童（12-16）
                </label>
                 -->
              </dd>
                </dl>
			<div class="sysdiv sysdiv_coupon">
				<p>
					<label>酒店地址：</label> <span> <form:input path="address"  maxlength="100" /></span>
				</p>
				<p>
					<label>酒店网址：</label> <span> <form:input path="website"  maxlength="100"   /></span>
				</p>
				<p class="domestic">
                        <label><em class="xing">*</em>地理位置：</label>
                        <!--<span> 中国 </span>-->
                        <span class="sysselect_s">
                            <form:select path="country"></form:select>
	                        <form:select path="province"></form:select>
	                        <form:select path="city"></form:select>
	                        <form:select path="district"></form:select>
	                        <form:input path="shortAddress" />
                        </span>
                    </p>
			
                    <p class="overseas">
                        <label><em class="xing">*</em>地理位置：</label>
                        <span class="sysselect_s">
                            <select id="overseas_state" name="overseas_state"></select>
                         	<select id="overseas_province" name="overseas_province"></select>
                        	<select id="overseas_city" name="overseas_city"></select>
                            <input type='text' name="shortAddress" id="shortAddress" />
                        </span>
                    </p>
				<p>
					<label>电话：</label> <span> <form:input path="telephone"  maxlength="33"  /></span>
				</p>
				<p>
					<label>传真：</label> <span> <form:input path="fax"  maxlength="33"  /></span>
				</p>
			</div>
			<div class="hostOccupancy typeOfHost" style="display:none;">
				<div class="seach25">
					<p><span class="xing">*</span>岛屿名称：</p>
					
					<select name="islandUuid" id="islandUuid" onchange="addIsland(this)">
						<option value="" >请选择</option>
						<c:if test="${!empty islandList }">
						<c:forEach var="entry" items="${islandList }">
						<option value="${entry.uuid }" <c:if test="${hotel.islandUuid ==entry.uuid}">selected</c:if>>${entry.islandName }</option>
						</c:forEach>
						</c:if>
						<option value="">+添加</option>
					</select>
					
				</div>
				<div class="seach25">
					<p>上岛方式：</p>
					<trekiz:defineDict name="islandWay" input="select" type="islands_way" defaultValue="${hotel.islandWay}" onchange="addIslandsWay(this)" element="<option value=''>+添加</option>" />
				</div>
				<div class="kongr"></div>
			</div>
			<!--入住信息结束-->
			<!--联系人信息开始-->
			<div class="ydbz_tit pl20">联系人信息</div>
			<!--其他费用开始-->
			<div class="otherExpenses">
				<div class="otherExpenses-list otherExpenses-Dqlist">
					<label><span>负责人：</span><form:input path="chargePerson" maxlength="33" /></label>
					<div class="kongr20"></div>
					<label><span>手机：</span><form:input path="chargeMobile" maxlength="33" /></label> 
					<label><span>固定电话：</span><form:input path="chargeTelephone" maxlength="33" /></label> 
					<label><span>传真：</span><form:input path="chargeFax" maxlength="33" /></label> 
					<label><span>Email：</span><form:input path="chargeEmail" maxlength="33"  class="email"/></label>
				</div>
			</div>
			
			
			<c:if test="${fn:length(hcList)==0 }">
			<div class="otherExpenses">
				<div class="otherExpenses-list otherExpenses-Dqlist">
					<label><span>联系人<em>1</em>：</span><input type="text" name="names"/></label>
					<div class="kongr20"></div>
					<label><span>手机：</span><input type="text" name="mobiles" /></label> 
					<label><span>固定电话：</span><input type="text" name="telephones" /></label> 
					<label><span>传真：</span><input type="text" name="faxs" /></label> 
					<label><span>Email：</span><input type="text" name="emails"  class="email"/></label>
				</div>
			</div>
			</c:if>
			<c:if test="${fn:length(hcList)!=0 }">
			<c:forEach var="entry" items="${hcList}" varStatus="v">
				<div class="otherExpenses">
					<div class="otherExpenses-list otherExpenses-Dqlist">
						<label><span>联系人<em>${v.index + 1 }</em>：</span><input type="text" name="names" value="${entry.name}"/></label>
						<div class="kongr20"></div>
						<label><span>手机：</span><input type="text" name="mobiles"  value="${entry.mobile}"/></label> 
						<label><span>固定电话：</span><input type="text" name="telephones"  value="${entry.telephone}"/></label> 
						<label><span>传真：</span><input type="text" name="faxs"  value="${entry.fax}"/></label> 
						<label><span>Email：</span><input type="text" name="emails"  value="${entry.email}" class="email"/></label>
						<c:if test="${v.index !=0 }"><label onclick="deleteContacts(this)" class="deleteCosts">删除</label></c:if>
					</div>
				</div>
			</c:forEach>
			</c:if>
			
			<div class="addCosts">
				<span onclick="addContacts(this)">添加联系人</span>
			</div>
			<!--其他费用结束-->
			<div class="ydbz_tit pl20">其他信息</div>
			<div class="sysdiv sysdiv_coupon">
				<p class="maintain_pfull">
					<label>酒店摘要：</label>
					<form:textarea path="remark" class="madintain_text" maxlength="5000" placeholder="文字描述不超过5000字"/>
				</p>
				<p class="maintain_pfull">
					<label>酒店描述：</label>
					<form:textarea path="description" class="madintain_text" maxlength="5000" placeholder="文字描述不超过5000字"/>
				</p>
				<p class="maintain_pfull">
					<label>酒店资料：</label> <input type="button" name="hotelAnnexDocId" value="上传" class="btn btn-primary" onClick="uploadFiles('${ctx}',null,this)"/> <em class="tips_color">多文件，多格式上传附件。单个附件不大于20M，总共不大于20M。</em>
						
						
						<ol class="batch-ol">
							
							<c:forEach items="${haList}" var="file" varStatus="s1">
								<li>
									<span>${file.docName}</span><a style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${file.docId})">下载</a>
									<input type="hidden" name="hotelAnnexDocId" value="${file.docId}"/>
									<input type="hidden" name="docOriName" value="${file.docName}"/>
									<input type="hidden" name="docPath" value="${file.docPath}"/>
									<a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(null,'hotelAnnexDocId',this)">删除</a>
								</li>
							</c:forEach>
						</ol>
				</p>

			</div>
			<div class="release_next_add">
				<input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
				<input type="button" value="提&nbsp;&nbsp;&nbsp;交" class="btn btn-primary" id="btnSubmit" onclick="validate();"/>
			</div>
		</form:form>

	</div>
	<!--其他费用模板开始-->
	<div id="otherExpensesTemplate" style="display:none;">
		<div class="otherExpenses">
			<div class="otherExpenses-list otherExpenses-Dqlist">
				<label><span>联系人<em></em>：
				</span><input type="text" name="names"/></label>
				<div class="kongr20"></div>
				<label><span>手机：</span><input type="text" name="mobiles" /></label> 
				<label><span>固定电话：</span><input type="text" name="telephones" /></label> 
				<label><span>传真：</span><input type="text" name="faxs" /></label> 
				<label><span>Email：</span><input type="text" name="emails"  class="email"/></label>
				<label onclick="deleteContacts(this)" class="deleteCosts">删除</label>
			</div>
		</div>
	</div>
	<div id="hotelStarTypeTemplate" style="display:none;">
		<trekiz:defineDict name="hotelStarType" input="select" type="hotel_star" />
	</div>
	
	<!--其他费用模板结束-->
	<!--右侧内容部分结束-->
</body>
</html>
