<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>基础信息维护-岛屿管理-添加修改信息</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	      <!-- <script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script> -->

	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/default.validator.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/dictComponent.js"></script>
	<script type="text/javascript">
		var $ctx = "${ctx}";
		$(document).ready(function(){
			 
			$("#inputForm").validate({
				rules:{
					islandName:{
						required:true,
						remote: {
							type: "POST",
							url: "${ctx}/island/check?uuid="+$('#uuid').val()
								}
						},
					sort:{
						required:true,
						digits:true
					},
					dictUuid:{
						required:true
					}
				},
				submitHandler: function(form){
					var url = "";
					if($("#uuid").val()=='') {
						url="${ctx}/island/save";
					} else {
						url="${ctx}/island/update";
					}
					$("#btnSubmit").attr("disabled", "disabled");
					$.post(url,$("#inputForm").serialize(),function(data){
						if(data.message=="1"){
							$("#searchForm",window.opener.document).submit(); 
							$.jBox.tip("保存成功!");
							setTimeout(function(){window.close();},500);
						}else if(data.message=="2"){
							$("#searchForm",window.opener.document).submit(); 
							$.jBox.tip("修改成功!");
							setTimeout(function(){window.close();},500);
						}else if(data.message=="3"){
							$.jBox.tip(data.error,'warning');
							$("#btnSubmit").attr("disabled", false);
						}else if(data.message == "4") {//酒店表单中的新增岛屿功能
							$("#islandUuid option:last",window.opener.document).before("<option value="+data.uuid+">"+data.islandName+"</option>");
							$("#islandUuid",window.opener.document).val(""+data.uuid+"");
							$.jBox.tip("保存成功!");
							setTimeout(function(){window.close();},500);
						}else{
							$.jBox.tip('系统异常，请重新操作!','warning');
							$("#btnSubmit").attr("disabled", false);
						}
					});
					
				},
				messages:{
					islandName:{remote:"名称已存在"}
				}
			});
			
			if($("#uuid").val()=='') {
				$("#sort").val("50");
			}
			
			//设置地理属性默认值
			domesticOverseas(obj);
			//初始化名称部分组件展开
			if(($("#spelling").val() != '') || ($("#shortSpelling").val() != '') || ($("#islandNameEn").val() != '') || ($("#shortNameEn").val() != '')) {
				showText($("#showTextButton"));
			}
		});
		
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
	    function validate() {
	    	var isPinyin=/^[a-zA-Z]+$/; //拼音验证
			var isEnglish=/^[a-zA-Z\s]+$/; //英文验证
			
			var spelling = $.trim($("#spelling").val()); //岛屿全拼
			var shortSpelling = $.trim($("#shortSpelling").val());//全拼缩写
			var islandNameEn = $.trim($("#islandNameEn").val());//英文名称
			var shortNameEn = $.trim($("#shortNameEn").val());//英文缩写
			
			if(spelling != '' && !isPinyin.test(spelling)) {
			    $.jBox.tip("岛屿全拼只能输入拼音。", 'error', { focusId: "spelling" }); 
				return false;
			}
			
			if(shortSpelling != '' && !isPinyin.test(shortSpelling)) {
			    $.jBox.tip("全拼缩写只能输入拼音。", 'error', { focusId: "shortSpelling" }); 
				return false;
			}
			
			if(islandNameEn != '' && !isEnglish.test(islandNameEn)) {
			    $.jBox.tip("英文名称只能输入英文和空格。", 'error', { focusId: "islandNameEn" }); 
				return false;
			}
			
			if(shortNameEn != '' && !isEnglish.test(shortNameEn)) {
			    $.jBox.tip("英文缩写只能输入英文和空格。", 'error', { focusId: "shortNameEn" }); 
				return false;
			}
			
			if($("input[name=islandWay]:checked").length == 0) {
	    		$.jBox.tip("上岛方式为必选字段!","warning");
	    		return false;
	    	}
			if($("#position").val() == 1) {
				if($("#country").val()==''){
					$.jBox.tip("请选择国家");
					return false;
				} 
			}else if($("#position").val() == 2) {
				if($("#overseas_state").val()==''){
					$.jBox.tip("请选择国家");
					return false;
				} 
			}
			
			$("#inputForm").submit();
	    }
	    
	</script>
</head>
<body>
	<div>
	<!--右侧内容部分开始-->
		<div class="produceDiv">
			<form:form method="post" modelAttribute="island" action="" class="form-horizontal" id="inputForm" novalidate="">
				<form:hidden path="uuid" />
				<input type=hidden  name="source" value="${source }"/>
				<!--基本信息开始-->
				<div class="ydbz_tit pl20">基本信息</div>
				<div class="seach25">
                    <p><span class="xing">*</span>位置属性：</p>
                    <form:select path="position" class="domesticOverseas" onchange="domesticOverseas(this);">
                        <option value="">请选择</option>
                        <c:forEach items="${positionMap}" var="item">
                        	<option value="${item.key}" <c:if test="${item.key==island.position}">selected="selected"</c:if>>${item.value}</option>
                        </c:forEach>
                    </form:select>
                    
                    
                </div>
				<%--
				<div class="seach25">
					<p>
						<span class="xing">*</span>国家：
					</p>
					<select name="islandCountry" id="islandCountry" class="required">
					</select>
				</div>
				 --%>
				<div class="seach25">
					<p>
						<span class="xing">*</span>岛屿类型：
					</p>
					<trekiz:defineDict name="type" input="select" type="islands_type" className="required" defaultValue="${island.type}" onchange="addCompont('${ctx}', this, 'islandsType', 'islands_type', 'select')" element="<option value=''>+添加</option>" />
				</div>
				<div class="kong"></div>
				<div class="hostBasics">
					<label><em class="xing">*</em>岛屿名称：</label>
					<form:input path="islandName" htmlEscape="false" maxlength="49" class="required" />
					<label>名称缩写：</label>
					<form:input path="shortName" htmlEscape="false" maxlength="49" />
					<button onclick="showText(this)" class="efx-button" type="button" id="showTextButton">
						展开全部<i></i>
					</button>
				</div>
				<div class="hostBasics" style="display:none;">
					<label>岛屿全拼：</label> <form:input path="spelling" htmlEscape="false" maxlength="49" />
					<label>全拼缩写：</label> <form:input path="shortSpelling" htmlEscape="false" maxlength="49" />
							<div class="kong"></div> 
					<label>英文名称：</label> <form:input path="islandNameEn" htmlEscape="false" maxlength="49" />
					<label>英文缩写：</label> <form:input path="shortNameEn" htmlEscape="false" maxlength="49" />
				</div>
				<div class="kongr"></div>
				<dl class="host-check">
					<dt>岛屿主题：</dt>
					<dd>
						<trekiz:defineDict name="topic" type="islands_topic" input="checkbox" defaultValue="${island.topic}" />
						<span class="host-check-add" onclick="addCompont('${ctx}',this,'topic', 'islands_topic', 'checkbox')">+添 加</span>
					</dd>
				</dl>
				<dl class="host-check">
					<dt>
						<em class="xing">*</em>上岛方式：
					</dt>
					<dd>
						<trekiz:defineDict name="islandWay" type="islands_way" input="checkbox" defaultValue="${island.islandWay}"/>
						<span class="host-check-add" onclick="addCompont('${ctx}', this,'islandWay', 'islands_way', 'checkbox')">+添 加</span>
					</dd>
				</dl>
				<div class="sysdiv sysdiv_coupon">
					<p>
						<label><em class="xing">*</em>排序：</label> <span> <form:input path="sort" htmlEscape="false" maxlength="4" class="required" /></span>
					</p>
					
					<p class="domestic">
                        <label><em class="xing">*</em>地理位置：</label>
                        <!--<span> 中国 </span>-->
                        <span class="sysselect_s">
                            <form:select path="country">
                            	<c:forEach items="${countrys}" var="country">
                            		<c:choose>
			                        	<c:when test="${country.uuid==island.country}">
			                        		<form:option value="${country.uuid }" selected="selected">${country.nameCn} </form:option>
			                        	</c:when>
			                        	<c:otherwise>
			                        		<form:option value="${country.uuid }">${country.nameCn} </form:option>
			                        	</c:otherwise>
			                        </c:choose>
                            	</c:forEach>
                            </form:select>
	                        <form:select path="province" onclick="getGeoSelectState('',$('#country'),'province')">
	                        	<c:forEach items="${provinces}" var="province">
                            		<c:choose>
			                        	<c:when test="${province.uuid==island.province}">
			                        		<form:option value="${province.uuid }" selected="selected">${province.nameCn}</form:option>
			                        	</c:when>
			                        	<c:otherwise>
			                        		<form:option value="${province.uuid }">${province.nameCn} </form:option>
			                        	</c:otherwise>
			                        </c:choose>
                            	</c:forEach>
	                        </form:select>
	                        <form:select path="city" onclick="getGeoSelectState('',$('#province'),'city')">
	                        	<c:forEach items="${citys}" var="city">
                            		<c:choose>
			                        	<c:when test="${city.uuid==island.province}">
			                        		<form:option value="${city.uuid }" selected="selected">${city.nameCn} </form:option>
			                        	</c:when>
			                        	<c:otherwise>
			                        		<form:option value="${city.uuid }">${city.nameCn} </form:option>
			                        	</c:otherwise>
			                        </c:choose>
                            	</c:forEach>
	                        </form:select>
	                        <form:select path="district"  onclick="getGeoSelectState('',$('#city'),'district')">
	                        	<c:forEach items="${districts}" var="district">
                            		<c:choose>
			                        	<c:when test="${district.uuid==island.province}">
			                        		<form:option value="${district.uuid }" selected="selected">${district.nameCn} </form:option>
			                        	</c:when>
			                        	<c:otherwise>
			                        		<form:option value="${district.uuid }">${district.nameCn} </form:option>
			                        	</c:otherwise>
			                        </c:choose>
                            	</c:forEach>
	                        </form:select>
                            <form:input path="shortAddress" htmlEscape="false"  />
                        </span>
                    </p>
			
                    <p class="overseas">
                        <label><em class="xing">*</em>地理位置：</label>
                        <span class="sysselect_s">
                            <select id="overseas_state" name="overseas_state">
                            	<c:forEach items="${countrys}" var="country">
                            		<c:choose>
			                        	<c:when test="${country.uuid==island.country}">
			                        		<option value="${country.uuid }" selected="selected">${country.nameCn} </option>
			                        	</c:when>
			                        	<c:otherwise>
			                        		<option value="${country.uuid }">${country.nameCn} </option>
			                        	</c:otherwise>
			                        </c:choose>
                            	</c:forEach>
                            </select>
                         	<select id="overseas_province" name="overseas_province" onclick="getGeoSelectState('',$('#country'),'province')">
                         		<c:forEach items="${provinces}" var="province">
                            		<c:choose>
			                        	<c:when test="${province.uuid==island.province}">
			                        		<option value="${province.uuid }" selected="selected">${province.nameCn} </option>
			                        	</c:when>
			                        	<c:otherwise>
			                        		<option value="${province.uuid }">${province.nameCn} </option>
			                        	</c:otherwise>
			                        </c:choose>
                            	</c:forEach>
                         	</select>
                        	<select id="overseas_city" name="overseas_city" onclick="getGeoSelectState('',$('#province'),'city')">
                        		<c:forEach items="${citys}" var="city">
                            		<c:choose>
			                        	<c:when test="${city.uuid==island.province}">
			                        		<option value="${city.uuid }" selected="selected">${city.nameCn} </option>
			                        	</c:when>
			                        	<c:otherwise>
			                        		<option value="${city.uuid }">${city.nameCn} </option>
			                        	</c:otherwise>
			                        </c:choose>
                            	</c:forEach>
                        	</select>
                            <input type='text' name="shortAddress" id="shortAddress" value="${island.shortAddress}"/>
                        </span>
                    </p>
					<p>
						<label>岛屿地址：</label> <span> <form:input path="islandAddress" htmlEscape="false" maxlength="11"/></span>
					</p>
					<!-- 
					<p>
						<label>岛屿坐标：</label> <span> <input type="text"></span>
					</p>
					 -->
				</div>

				<div class="sysdiv sysdiv_coupon">
					<p class="maintain_pfull">
						<label>岛屿描述：</label>
						<form:textarea path="description" class="madintain_text" maxlength="5000" placeholder="文字描述不超过5000字"/>
					</p>
					<p class="maintain_pfull">
						<label>岛屿附件及攻略：</label>
						<input type="button" name="hotelAnnexDocId" value="上传" class="btn btn-primary" onclick="uploadFiles('${ctx}',null,this)" />
						<em class="tips_color">多文件，多格式上传附件。单个附件不大于20M，总共不大于20M。</em>
						<ol class="batch-ol">
							<c:forEach items="${hotelAnnexs}" var="file" varStatus="s1">
								<li>
									<span>${file.docName}</span><a style="margin-left:10px;" href="javascript:void(0)" onclick="downloads(${file.docId})">下载</a>
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
					<input type="button" value="提&nbsp;&nbsp;&nbsp;交" class="btn btn-primary" id="btnSubmit" onclick="validate()"/>
                </div>
			</form:form>
		</div> <!--右侧内容部分结束-->
	</div>
</body>
</html>
