<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>产品-机票产品及发布-上传资料</title>
<style type="text/css">
	.disableCss{
				pointer-events:none;
				color:#afafaf;
				cursor:default
				} 
</style>
<script type="text/javascript" src="${ctxStatic}/modules/forTTS/airticket/airticketUploadFile.js"></script>

<script type="text/javascript">
$(function(){
	//搜索条件筛选
	launch();
	//产品名称文本框提示信息
	inputTips();
	//操作浮框
	operateHandler();
	g_context_url = "${ctx}";
	
	
	$(document).on('click','.departmentButton',function(){
		var $currentClick=$(this);
			var url = "/sys/department/treeData?officeId=" + ${deptId};
			// 正常打开	
			top.$.jBox.open("iframe:${ctx}/tag/treeselect?url="+encodeURIComponent(url)+"&module=&checked=&extId=&selectIds="+$currentClick.prev().prev().val(), "选择部门", 300, 420,{
				buttons:{"确定":"ok", "关闭":true}, submit:function(v, h, f){
					if (v=="ok"){
						var tree = h.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
						var ids = [], names = [], nodes = [];
						if ("" == "true"){
							nodes = tree.getCheckedNodes(true);
						}else{
							nodes = tree.getSelectedNodes();
						}
						for(var i=0; i<nodes.length; i++) {//
							ids.push(nodes[i].id);
							names.push(nodes[i].name);//
							break; // 如果为非复选框选择，则返回第一个选择  
						}
						$currentClick.prev().val(names);
						$currentClick.prev().prev().val(ids);
//	 					$("#departmentId").val(ids);
//	 					$("#departmentName").val(names);
						$("#departmentName").focus();
						$("#departmentName").blur();
					}
				}, loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				},persistent:true
			});
		});
});


//上传文件时，点击后弹窗进行上传文件(多文件上传)
//方法参数：${ctx}、显示上传文件名称input的id、当前点击元素
function uploadFiles(ctx, inputId, obj) {
	//新建一个隐藏的div，用来保存文件上传后返回的数据
	if($(obj).parent().find(".uploadPath").length == 0) {
		$(obj).parent().append('<div class="uploadPath" style="display: none" ></div>');
		$(obj).parent().append('<div id="currentFiles" style="display: none" ></div>');		
	}
	
	$(obj).addClass("clickBtn");
	
	$.jBox("iframe:"+ ctx +"/airTicket/uploadFilesPage", {
	    title: "多文件上传",
		width: 340,
   		height: 365,
   		buttons: {'完成上传':true},
   		persistent:true,
   		loaded: function (h) {},
   		submit: function (v, h, f) {
			$(h.find("iframe")[0].contentWindow.file_upload).next("p").find(".successUpload").click();
			if($(obj).parent().find("#currentFiles").find("[name='docOriName']").length != 0) {
				$(obj).parent().find("#currentFiles").find("[name='docOriName']").each(function(index, obj1) {
					$(obj).parent().parent().next().find(".batch-ol").append('<li><span>'+ $(obj1).val() +'</span><a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(\''+ $(obj1).prev().val() +'\',\''+ $(obj).attr("name") +'\',this);">删除</a></li>');
				});
				if($("#currentFiles").children().length != 0)
					$("#currentFiles").children().remove();
			}
			
			$(".clickBtn",window.parent.document).removeClass("clickBtn");
			//showAllDocfile();
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
			}
			$(obj).parent("li").remove();
		}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');		
}

//显示所有的附件
function showAllDocfile(){
	$.ajax({
         type: "GET",
         url: "test.json",
         data: {username:$("#username").val(), content:$("#content").val()},
         dataType: "json",
         success: function(data){
            $('#resText').empty();   //清空resText里面的所有内容
            var html = ''; 
            $.each(data, function(commentIndex, comment){
                  html += '<div class="comment"><h6>' + comment['username']
                            + ':</h6><p class="para"' + comment['content']
                            + '</p></div>';
            });
            $('#resText').html(html);
         }
     });
	$("#docfiles").append("<label name='introduction_file'>${afile.fileName }</label><a style='margin-left:10px;' href='javascript:void(0)' onclick='deleteDoc(${afile.docId})'>删除</a><div class='kong'></div>");
}

//var submitCount = 0;
function submitForm(type,obj){
	//防止多次提交产生多条记录
// 	if(submitCount>0){
// 		return;
// 	}
// 	submitCount = 1;
	$(obj).addClass("disableCss");
	
	if(3==type){//放弃
		$("#txt_isDrop").val("1");
	}else if(1==type){//草稿
		$("#txt_proStatus").val("1");
	}else{
		$("#txt_proStatus").val("2");//发布
		var companyId=$("#companyId").val();
		var companyUUID=$("#companyUUID").val();
		//C460 团号生成规则配置 groupCodeRuleJP为0时代表手动输入 
		var groupCodeRuleJP=$("#groupCodeRuleJP").val();
		var flag = false;
		if(companyUUID=='7a8177e377a811e5bc1e000c29cf2586' || companyUUID=='ed88f3507ba0422b859e6d7e62161b00' || companyUUID=='f5c8969ee6b845bcbeb5c2b40bac3a23' || companyUUID=='1d4462b514a84ee2893c551a355a82d2' || companyUUID=='58a27feeab3944378b266aff05b627d2' || companyUUID=='7a81c5d777a811e5bc1e000c29cf2586' || companyUUID=='5c05dfc65cd24c239cd1528e03965021' || groupCodeRuleJP==0){
			var groupCode = $("#groupCode").val();
			var oldGroupCode= $("#oldGroupCode").val();
			if(groupCode==""){
				top.$.jBox.tip("请填写团号!", 'warnning');
				$(obj).removeClass("disableCss");
				return;
			}else{
				var txt_ticketId=$("#txt_ticketId").val();
				
				//c451，c453, 修改操作时也进 团号校验
				if(groupCode!=oldGroupCode){
					$.ajax({
						type: "POST",
						async:false,
						url: g_context_url+"/activity/manager/checkAirTicketGroupCode",
						data: {
							"groupCode" : groupCode
						},
						success: function(data){
							if(data.result=="0"){
								top.$.jBox.tip("团号重复，请修改!", 'warnning');
								$('#groupCode').attr("readonly",false);
								$(obj).removeClass("disableCss");
							}else{
								flag = true;
							}
						}
					});
				}else{
					flag = true;
				}
			}
			if (flag==false) {
				return false;
			}
		}
	   //选择部门
		var deptSelect = $("#deptId").val();
	   	var modifyFlag = "${ismodify}"; 
	 	if(modifyFlag=='N'){//修改产品时不可修改部门，所以只在发布产品时校验 
	 		if(deptSelect==null || deptSelect==""){
	 		   top.$.jBox.tip("请选择部门!", 'warnning');
	 		  $(obj).removeClass("disableCss");
	 		   return;
	 	    } 
	 	}
	}
		$("#addForm").submit();
}

function deleteDoc(){
	
}

//上一步
function ThirdToSecond(){
	javascript:history.go(-1);
}
</script>
</head>
<body>
<page:applyDecorator name="show_head">
    <page:param name="desc">发布机票产品</page:param>
</page:applyDecorator>
 <!--右侧内容部分开始-->
                <div class="produceDiv">
					<div style="width:100%; height:20px;"></div>
					<div class="visa_num visa_num3"></div>
                    <form enctype="multipart/form-data" method="post" action="${ctx}/airTicket/save" class=" form-search" id="addForm" novalidate="novalidate">
                    
                    <input type="hidden" id="txt_ticketId" name="txt_ticketId" value="${airticket.id}"/>
                    <!-- 页面中返回旧 团号，作为是否校验的依据    c451，c453 -->
                    <input type="hidden" id="oldGroupCode" name="txt_ticketId" value="${oldGroupCode}"/>
                    
                    <input type="hidden" id="recordId" name="recordId" value="${recordId}"/>
                    <input type="hidden" id="txt_proStatus" name="txt_proStatus" value="2"/>
                    <input type="hidden" id="txt_scope" name="txt_scope" value="1"/>
                    <input type="hidden" id="txt_isDrop" name="txt_isDrop" value="0"/>
                    <input type="hidden" id="txt_jsonObj" name="txt_jsonObj" value='${jsonObj}'/>
                    <input type="hidden" id="companyId" name="companyId" value="${fns:getUser().company.id }" />
                    <input type="hidden" id="companyUUID" name="companyUUID" value="${fns:getUser().company.uuid }" />
                    <input type="hidden" id="groupCodeRuleJP" name="groupCodeRuleJP" value="${fns:getUser().company.groupCodeRuleJP }" />
                    <%--<input type="hidden" id="groupCode" name="groupCode" value="${airticket.groupCode }" />--%>
           <div class="messageDiv">
          	<div class="kongr"></div>
            <div class="kongr"></div>
            <div class="mod_information_d7"></div>
            <div class="kongr"></div>
            
            
            <div class="seach25 seach100">
            	<span class="seach_check"><label for="inquiry_radio_flights2"><input id="inquiry_radio_flights2" type="radio" onclick="inquiry_radio_flights()" <c:if test="${airticket.airType eq '3'}">checked="checked"</c:if> disabled="disabled" /> 单程</label></span>
                <span class="seach_check"><label for="inquiry_radio_flights1"><input id="inquiry_radio_flights1" type="radio" onclick="inquiry_radio_flights()" <c:if test="${airticket.airType eq '2'}">checked="checked"</c:if> disabled="disabled" /> 往返</label></span>
                <span class="seach_check"><label for="inquiry_radio_flights3"><input id="inquiry_radio_flights3" type="radio" onclick="inquiry_radio_flights()" <c:if test="${airticket.airType eq '1'}">checked="checked"</c:if> disabled="disabled" /> 多段</label></span>
            </div>
            
            <c:choose>
            <c:when test="${airticket.airType eq '3'}">
            <!--单程开始-->
            <div class="inquiry_flights2" style="display:block;">
       			<c:forEach items="${airticket.flightInfos}" var="list" varStatus="listIndex">
                  <div class="seach25 seach100">
                      <input type="hidden" name="hiddenradiovalue" <c:if test="${list.ticket_area_type eq '4'}"> value="4" </c:if> <c:if test="${list.ticket_area_type eq '1'}"> value="1" </c:if><c:if test="${list.ticket_area_type eq '2'}"> value="2" </c:if><c:if test="${list.ticket_area_type eq '3'}"> value="3" </c:if>  >
                      <span class="seach_check"><label for="radio14"><input type="radio"  name="searchRadio10" value="4" <c:if test="${list.ticket_area_type eq '4'}">checked="checked"</c:if> disabled="disabled" /> 国内</label></span>
                      <span class="seach_check"><label for="radio12"><input type="radio"  name="searchRadio10" value="2" <c:if test="${list.ticket_area_type eq '2'}">checked="checked"</c:if> disabled="disabled" /> 国际</label></span>
                      <span class="seach_check"><label for="radio11"><input type="radio"  name="searchRadio10" value="1" <c:if test="${list.ticket_area_type eq '1'}">checked="checked"</c:if> disabled="disabled" /> 内陆</label></span>                    
                      <span class="seach_check"><label for="radio13"><input type="radio"  name="searchRadio10" value="3" <c:if test="${list.ticket_area_type eq '3'}">checked="checked"</c:if> disabled="disabled" /> 国际+内陆</label></span>
                  </div>
                     <div class="seach25">
                         <p><span class="xing">*</span>出发城市：</p>
                         <p class="seach_r"><span class="disabledshowspan">
                        		${fns:getDictLabel(airticket.departureCity, 'from_area', '')}
                         </span></p>
                     </div>
                     <div class="seach25">
                         <p><span class="xing">*</span>到达城市：</p>
                         <p class="seach_r"><span class="disabledshowspan">
                         <c:forEach items="${arrivedCitys}" var="arrivedCitys">
			                       		<c:if test="${arrivedCitys.id == airticket.arrivedCity}">
			                       		${arrivedCitys.name}
			                       		</c:if>
		                     	   </c:forEach>
                         
                         </span></p>
                     </div>
                     <div class="seach25">
                         <p>预收人数：</p>
                         <p class="seach_r"><span class="disabledshowspan">${airticket.reservationsNum}人</span></p>
                     </div>
                     <div class="kong"></div>
                     
                     <div class="seach25">
		                <p><span class="xing">*</span>出发机场：</p>
		                <p class="seach_r"><span class="disabledshowspan">${list.paraMap.leaveAirport}</span></p>
		              </div>
		              <div class="seach25">
		                <p><span class="xing">*</span>到达机场：</p>
		               <p class="seach_r"><span class="disabledshowspan">${list.paraMap.destinationAirpost}</span></p>
		              </div>
		              <div class="kong"></div>
		              
                     <div class="seach25">
                         <p>出发时刻：</p>
                         <p class="seach_r"><span class="disabledshowspan"><fmt:formatDate value="${list.startTime}" pattern="yyyy-MM-dd HH:mm"/></span></p>
                     </div>
                     <div class="seach25">
                         <p>到达时刻：</p>
                         <p class="seach_r"><span class="disabledshowspan"><fmt:formatDate value="${list.arrivalTime}" pattern="yyyy-MM-dd HH:mm"/></span></p>
                     </div>
					<div class="seach25">
		                <p>航班号：</p>
		                <p class="seach_r"><span class="disabledshowspan">${list.flightNumber}</span></p>
		            </div>
                     <div class="kong"></div>
                     <div class="seach25">
                         <p>航空公司：</p>
                         <p class="seach_r"><span class="disabledshowspan">
                         ${fns:getAirlineNameByAirlineCode(list.airlines)}
                         </span></p>
                     </div>
                     <div class="seach25">
                         <p>舱位等级：</p>
                         <p class="seach_r"><span class="disabledshowspan">${fns:getDictLabel(list.spaceGrade, "spaceGrade_Type", "")}</span></p>
                     </div>
                     <div class="seach25">
                         <p>舱位：</p>
                         <p class="seach_r"><span class="disabledshowspan">${fns:getDictLabel(list.airspace, "airspace_Type", "")}</span></p>
                     </div>
                     <div class="kong"></div>
                 </c:forEach>           
            </div>     
             <!--单程结束-->  
            </c:when>
           
            <c:when test="${airticket.airType eq '2'}">
            <c:forEach items="${airticket.flightInfos}" var="list" varStatus="listIndex">
           	<!--往返开始-->
            <div class="inquiry_flights1">
            	<c:if test="${list.number==1}">
             		<div class="seach25 seach100">
             		 <input type="hidden" name="hiddenradiovalue" <c:if test="${list.ticket_area_type eq '4'}"> value="4" </c:if> <c:if test="${list.ticket_area_type eq '1'}"> value="1" </c:if><c:if test="${list.ticket_area_type eq '2'}"> value="2" </c:if><c:if test="${list.ticket_area_type eq '3'}"> value="3" </c:if>  >
                      <span class="seach_check"><label for="radio14"><input type="radio" value="4" name="searchRadio10" <c:if test="${list.ticket_area_type eq '4'}">checked="checked"</c:if> disabled="disabled" /> 国内</label></span>
                      <span class="seach_check"><label for="radio12"><input type="radio" value="2" name="searchRadio10" <c:if test="${list.ticket_area_type eq '2'}">checked="checked"</c:if> disabled="disabled" /> 国际</label></span>
                      <span class="seach_check"><label for="radio11"><input type="radio" value="1" name="searchRadio10" <c:if test="${list.ticket_area_type eq '1'}">checked="checked"</c:if> disabled="disabled" /> 内陆</label></span>                      
                      <span class="seach_check"><label for="radio13"><input type="radio" value="3" name="searchRadio10" <c:if test="${list.ticket_area_type eq '3'}">checked="checked"</c:if> disabled="disabled" /> 国际+内陆</label></span>
                  	</div>
                  	
                  	<div class="seach25">
                         <p><span class="xing">*</span>出发城市：</p>
                         <p class="seach_r"><span class="disabledshowspan">
                          ${fns:getDictLabel(airticket.departureCity, 'from_area', '')}
                         </span></p>
                     </div>
                     <div class="seach25">
                         <p><span class="xing">*</span>到达城市：</p>
                         <p class="seach_r"><span class="disabledshowspan">
                         <c:forEach items="${arrivedCitys}" var="arrivedCitys">
			                       		<c:if test="${arrivedCitys.id == airticket.arrivedCity}">
			                       		${arrivedCitys.name}
			                       		</c:if>
		                     	   </c:forEach>

                         </span></p>
                     </div>
                     <div class="seach25">
                         <p>预收人数：</p>
                         <p class="seach_r"><span class="disabledshowspan">${airticket.reservationsNum}人</span></p>
                     </div>

                     <div class="kong"></div>
	            </c:if>
	            
			  <c:if test="${list.number==1}">
              	<div class="title_samil">去程：</div>
              </c:if>
              <c:if test="${list.number==2}">
              	<div class="title_samil">返程：</div>
              </c:if>
              <div class="seach25">
                <p><span class="xing">*</span>出发机场：</p>
                <p class="seach_r"><span class="disabledshowspan">${list.paraMap.leaveAirport}</span></p>
              </div>
              <div class="seach25">
                <p><span class="xing">*</span>到达机场：</p>
               <p class="seach_r"><span class="disabledshowspan">${list.paraMap.destinationAirpost}</span></p>
              </div>
              <div class="kong"></div>
              
              <div class="seach25">
                   <p>出发时刻：</p>
                   <p class="seach_r"><span class="disabledshowspan"><fmt:formatDate value="${list.startTime}" pattern="yyyy-MM-dd HH:mm"/></span></p>
               </div>
               <div class="seach25">
                   <p>到达时刻：</p>
                   <p class="seach_r"><span class="disabledshowspan"><fmt:formatDate value="${list.arrivalTime}" pattern="yyyy-MM-dd HH:mm"/></span></p>
               </div>
				<div class="seach25">
	                <p>航班号：</p>
	                <p class="seach_r"><span class="disabledshowspan">${list.flightNumber}</span></p>
	            </div>
               <div class="kong"></div>
               <div class="seach25">
                   <p>航空公司：</p>
                   <p class="seach_r"><span class="disabledshowspan">
                 	 ${fns:getAirlineNameByAirlineCode(list.airlines)}
                   </span></p>
               </div>
               <div class="seach25">
                   <p>舱位等级：</p>
                   <p class="seach_r"><span class="disabledshowspan">${fns:getDictLabel(list.spaceGrade, "spaceGrade_Type", "")}</span></p>
               </div>
               <div class="seach25">
                   <p>舱位：</p>
                   <p class="seach_r"><span class="disabledshowspan">${fns:getDictLabel(list.airspace, "airspace_Type", "")}</span></p>
               </div>
               <div class="kong"></div>
               
            </div>
            <!--往返结束-->
             </c:forEach>   
            </c:when>
            
            <c:when test="${airticket.airType eq '1'}"> 
                  <!--多段开始-->
              <div class="inquiry_flights3" style="display:block;">
            	
           		 <div class="seach25">
                     <p><span class="xing">*</span>出发城市：</p>
                     <p class="seach_r"><span class="disabledshowspan">
                      ${fns:getDictLabel(airticket.departureCity, 'from_area', '')}
                     </span></p>
                 </div>
                 <div class="seach25">
                     <p><span class="xing">*</span>到达城市：</p>
                     <p class="seach_r"><span class="disabledshowspan">
                      <c:forEach items="${arrivedCitys}" var="arrivedCitys">
			                       		<c:if test="${arrivedCitys.id == airticket.arrivedCity}">
			                       		${arrivedCitys.name}
			                       		</c:if>
		                     	   </c:forEach>
                     </span></p>
                 </div>
                 <div class="seach25">
                     <p>预收人数：</p>
                     <p class="seach_r"><span class="disabledshowspan">${airticket.reservationsNum}人</span></p>
                 </div>
                 <div class="kong"></div>
            
			  <c:forEach items="${airticket.flightInfos}" var="list" varStatus="listIndex">
            	<div class="addFlights3Div">
                  <div class="title_samil">第${listIndex.index + 1}段：
                  	 <input type="hidden" name="hiddenradiovalue" <c:if test="${list.ticket_area_type eq '4'}"> value="4" </c:if> <c:if test="${list.ticket_area_type eq '1'}"> value="1" </c:if><c:if test="${list.ticket_area_type eq '2'}"> value="2" </c:if><c:if test="${list.ticket_area_type eq '3'}"> value="3" </c:if>  >
                    <span class="seach_check"><label for="radio34"><input id="radio34" type="radio" <c:if test="${list.ticket_area_type eq '4'}">checked="checked"</c:if> disabled="disabled"> 国内</label></span>
                    <span class="seach_check"><label for="radio32"><input id="radio32" type="radio" <c:if test="${list.ticket_area_type eq '2'}">checked="checked"</c:if> disabled="disabled"> 国际</label></span>
                    <span class="seach_check"><label for="radio31"><input id="radio31" type="radio" <c:if test="${list.ticket_area_type eq '1'}">checked="checked"</c:if> disabled="disabled"> 内陆</label></span>                   
                    <span class="seach_check"><label for="radio13"><input id="radio33" type="radio" <c:if test="${list.ticket_area_type eq '3'}">checked="checked"</c:if> disabled="disabled" /> 国际+内陆</label></span>
                  </div>
              <div class="seach25">
		                <p><span class="xing">*</span>出发机场：</p>
		                <p class="seach_r"><span class="disabledshowspan">${list.paraMap.leaveAirport}</span></p>
		              </div>
		              <div class="seach25">
		                <p><span class="xing">*</span>到达机场：</p>
		               <p class="seach_r"><span class="disabledshowspan">${list.paraMap.destinationAirpost}</span></p>
		              </div>
		              <div class="kong"></div>
              
              <div class="seach25">
                   <p>出发时刻：</p>
                   <p class="seach_r"><span class="disabledshowspan"><fmt:formatDate value="${list.startTime}" pattern="yyyy-MM-dd HH:mm"/></span></p>
               </div>
               <div class="seach25">
                   <p>到达时刻：</p>
                   <p class="seach_r"><span class="disabledshowspan"><fmt:formatDate value="${list.arrivalTime}" pattern="yyyy-MM-dd HH:mm"/></span></p>
               </div>
				<div class="seach25">
	                <p>航班号：</p>
	                <p class="seach_r"><span class="disabledshowspan">${list.flightNumber}</span></p>
	            </div>
               <div class="kong"></div>
               <div class="seach25">
                   <p>航空公司：</p>
                   <p class="seach_r"><span class="disabledshowspan">
                  ${fns:getAirlineNameByAirlineCode(list.airlines)}
                   </span></p>
               </div>
               <div class="seach25">
                   <p>舱位等级：</p>
                   <p class="seach_r"><span class="disabledshowspan">${fns:getDictLabel(list.spaceGrade, "spaceGrade_Type", "")}</span></p>
               </div>
               <div class="seach25">
                   <p>舱位：</p>
                   <p class="seach_r"><span class="disabledshowspan">${fns:getDictLabel(list.airspace, "airspace_Type", "")}</span></p>
               </div>
               <div class="kong"></div>
               
                </div>
                </c:forEach>
            </div>   
            <!--多段结束-->
            </c:when>
            </c:choose>
          </div>
                        <!--填写价格开始-->
                        <div style="" class="mod_information" id="secondStepDiv">   
                        <c:if test="${fns:getUser().company.uuid == '7a8177e377a811e5bc1e000c29cf2586' || fns:getUser().company.uuid == '7a81a03577a811e5bc1e000c29cf2586' || fns:getUser().company.uuid == 'ed88f3507ba0422b859e6d7e62161b00' || fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' || fns:getUser().company.uuid == '1d4462b514a84ee2893c551a355a82d2' || fns:getUser().company.uuid == '58a27feeab3944378b266aff05b627d2' || fns:getUser().company.uuid == '7a81c5d777a811e5bc1e000c29cf2586' || fns:getUser().company.uuid == '5c05dfc65cd24c239cd1528e03965021' || fns:getUser().company.groupCodeRuleJP==0}">
                            <div class="mod_information_d" id="secondStepTitle"><span style=" font-weight:bold; padding-left:20px;float:left">添加团号及价格</span></div>
                        </c:if>
                        <c:if test="${fns:getUser().company.uuid != '7a8177e377a811e5bc1e000c29cf2586' && fns:getUser().company.uuid != '7a81a03577a811e5bc1e000c29cf2586' && fns:getUser().company.uuid != 'ed88f3507ba0422b859e6d7e62161b00' && fns:getUser().company.uuid != 'f5c8969ee6b845bcbeb5c2b40bac3a23' && fns:getUser().company.uuid != '1d4462b514a84ee2893c551a355a82d2' && fns:getUser().company.uuid != '58a27feeab3944378b266aff05b627d2' && fns:getUser().company.uuid != '7a81c5d777a811e5bc1e000c29cf2586' && fns:getUser().company.uuid != '5c05dfc65cd24c239cd1528e03965021' && fns:getUser().company.groupCodeRuleJP!=0}">
                            <div class="mod_information_d" id="secondStepTitle"><span style=" font-weight:bold; padding-left:20px;float:left">添加价格</span></div>
                        </c:if>
                        <div id="secondStepEnd">
                            <div style="width:100%; height:10px;"></div>
                            <div class="add2_nei">
                            <c:if test="${fns:getUser().company.uuid == '7a8177e377a811e5bc1e000c29cf2586' || fns:getUser().company.uuid == '7a81a03577a811e5bc1e000c29cf2586' || fns:getUser().company.uuid == 'ed88f3507ba0422b859e6d7e62161b00' || fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' || fns:getUser().company.uuid == '1d4462b514a84ee2893c551a355a82d2' || fns:getUser().company.uuid == '58a27feeab3944378b266aff05b627d2' || fns:getUser().company.uuid == '7a81c5d777a811e5bc1e000c29cf2586' || fns:getUser().company.uuid == '5c05dfc65cd24c239cd1528e03965021' || fns:getUser().company.groupCodeRuleJP==0}">
                                <div class="kongr"></div>
                                <div class="seach25 seach100 pro-marks1">
                                    <p class="fbold f14" style="width:auto;margin-right:5px;"><span class="xing">*</span>团号：</p>
                                    <p class="seach_r">
                                        <input readonly="readonly" id="groupCode" name="groupCode" value="${airticket.groupCode }" type="text" maxlength="50" title="${airticket.groupCode }" onblur="removeCodeCss(this)" onafterpaste="replaceStr(this)" onKeyUp="validateLong(this)"/>
                                    </p>
                                </div>
                                <div class="kong"></div>
                            </c:if>
							<div class="title_samil">整体报价</div>
			                  <div class="clear"></div>
			                  <table class="table-mod2-group planeTick-table">
			                      <tbody>
			                      <tr>
			                          <td class="add2_nei_table">币种选择：</td>
			                          <td class="add2_nei_table_typetext">
			                          	<select nowclass="rmb" id="selectCurrency" class="sel-currency" disabled="disabled">
		                                  <option addclass="${fns:getCurrencyInfo(airticket.currency_id, 0, 'style')}" >${airticket.paraMap.currencyName}</option>
		                              	</select>
			                          </td>
			                          <td class="add2_nei_table"><input type="checkbox" class="ckb-tax"  ${airticket.istax==1?'checked':''} disabled="disabled"/>税费：</td>
		                              <td class="add2_nei_table_typetext">${fns:getCurrencyInfo(airticket.currency_id, 0, "mark")}<span class="disabledshowspan"><fmt:formatNumber type="number" value="${airticket.taxamt}" pattern="#,##0.00"/></span></td>
		                              <td class="add2_nei_table">应付账期：</td>
			                          <td class="add2_nei_table_typetext"><span class="disabledshowspan"><fmt:formatDate value="${airticket.payableDate}" pattern="yyyy-MM-dd"/></span></td>
		                              <td class="add2_nei_table">特殊人群最高人数：</td>
			                          <td class="add2_nei_table_typetext"><span class="disabledshowspan">${airticket.maxPeopleCount}</span></td>
			                      </tr>
			                      <tr>
			                       <td class="add2_nei_table">成人同行价：</td>
		                           <td class="add2_nei_table_typetext">${fns:getCurrencyInfo(airticket.currency_id, 0, "mark")}<span class="disabledshowspan"><fmt:formatNumber type="number" value="${airticket.settlementAdultPrice}" pattern="#,##0.00"/></span></td>
	                               <td class="add2_nei_table">儿童同行价：</td>
	                               <td class="add2_nei_table_typetext">${fns:getCurrencyInfo(airticket.currency_id, 0, "mark")}<span class="disabledshowspan"><fmt:formatNumber type="number" value="${airticket.settlementcChildPrice}" pattern="#,##0.00"/></span></td>
	                               <td class="add2_nei_table">特殊人群同行价：</td>
	                               <td class="add2_nei_table_typetext">${fns:getCurrencyInfo(airticket.currency_id, 0, "mark")}<span class="disabledshowspan"><fmt:formatNumber type="number" value="${airticket.settlementSpecialPrice}" pattern="#,##0.00"/></span></td>                               
	                             	<td class="add2_nei_table">儿童最高人数：</td>
			                          <td class="add2_nei_table_typetext"><span class="disabledshowspan"> ${airticket.maxChildrenCount}</span></td>
	                              </tr>
	                               <!-- 发票税:0258需求,限定为懿洋假期-tgy-s -->
                      				<c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' }"> 
                       				<tr>
						 			<td class="add2_nei_table">发票税：</td>
						 			<c:if test="${!empty airticket.invoiceTax}"><!-- 不为空时,展示具体的值 -->
                                    <td class="add2_nei_table_typetext"><span class="disabledshowspan"><fmt:formatNumber type="number" value="${airticket.invoiceTax}" pattern="#0.00"/></span>&nbsp;%</td>
                                    </c:if>
                                    <c:if test="${empty airticket.invoiceTax}"><!-- 为空时,展示0.00-->
                                    <td class="add2_nei_table_typetext"><span class="disabledshowspan"><fmt:formatNumber type="number" value="0" pattern="#0.00"/></span>&nbsp;%</td>
                                    </c:if>
                                    </tr>
                      				</c:if>
                      <!-- 发票税:0258需求,限定为懿洋假期-tgy-e -->
			                      <tr>
			                          <td class="add2_nei_table">订金：</td>
			                          <td class="add2_nei_table_typetext">${fns:getCurrencyInfo(airticket.currency_id, 0, "mark")}<span class="disabledshowspan"><fmt:formatNumber type="number" value="${airticket.depositamt}" pattern="#,##0.00"/></span></td>
			                          <!-- <td class="add2_nei_table">订金时限：</td>
			                          <td class="add2_nei_table_typetext wpr20"><span class="disabledshowspan"><fmt:formatDate value="${airticket.depositTime}" pattern="yyyy-MM-dd"/></span></td>
			                          <td class="add2_nei_table">取消时限：</td>
			                          <td class="add2_nei_table_typetext"><span class="disabledshowspan"><fmt:formatDate value="${airticket.cancelTimeLimit}" pattern="yyyy-MM-dd"/></span></td>
			                           -->
			                          
			                          <td class="add2_nei_table">备注：</td>
	                               	  <td class="add2_nei_table_typetext"><span class="disabledshowspan">${airticket.specialremark}</span></td>
			                      </tr>
			                      <c:if test="${ismodify eq 'N' }">
			                      	<tr>
			                      	  <td class="add2_nei_table"><span class="xing">*</span>所属部门：</td>
			                          <td class="add2_nei_table_typetext" colspan="2">
<!-- 			                              <select id="deptId" name="deptId" class="sel-currency"> -->
<%-- 			                                  <c:forEach items="${deptList}" var="list" varStatus="listIndex"> --%>
<%-- 		                                          <option value="${list.id }" <c:if test="${airticket.deptId==list.id }">selected="selected"</c:if>>${list.name }</option> --%>
<%-- 		                                      </c:forEach> --%>
<!-- 		                                  </select> -->
												<input id="deptId" name="deptId" type="hidden" value="${!empty deptMap.dept_id ? deptMap.dept_id : '' }">
												<input id="departmentName" name="departmentName" readonly="readonly" type="text" value="${!empty deptMap.deptName ? deptMap.deptName : '' }" style="">
												<a id="departmentButton" href="javascript:" class="btn departmentButton" style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
			                          </td>
			                      	</tr>
			                      </c:if>
			                      <c:if test="${ismodify eq 'Y' }">
			                      	 <tr>
			                      	 	<input id="deptId" name="deptId" type="hidden" value="${!empty deptMap.dept_id ? deptMap.dept_id : '' }">
			                      	 	<td class="add2_nei_table"><span class="xing">*</span>所属部门：</td>
			                      	 	<td class="add2_nei_table_typetext" colspan="2">${deptMap.deptName }</td>
			                      	 </tr>
			                      </c:if>
			                      </tbody>
			                  </table>
                                              
                            <c:if test="${airticket.airType eq '1'}">
                              <div class="title_samil">分段报价</div>
                              <c:if test="${airticket.isSection==1}">
                              <c:forEach items="${airticket.flightInfos}" var="list" varStatus="listIndex">
                              
                              <div class="flyMoreDiv">
                                <div class="title_samil">第${list.number}段：${list.paraMap.leaveAirport}-${list.paraMap.destinationAirpost}</div>
                                 <table class="table-mod2-group planeTick-table">
		                             <tbody><tr>
		                                <td class="add2_nei_table">币种选择：</td>
		                                <td class="add2_nei_table_typetext">
		                                    <select nowclass="rmb" id="selectCurrency" class="sel-currency" disabled="disabled">
		                                        <option addclass="" value="1">${list.paraMap.currencyName}</option>
		                                    </select>
		                                </td>
		                                <td class="add2_nei_table"><input type="checkbox" class="ckb-tax" ${list.istax==1?'checked':''} disabled="disabled" />税费：</td>
		                                <td class="add2_nei_table_typetext">¥<span class="disabledshowspan"><fmt:formatNumber type="number" value="${list.taxamt}" pattern="#,##0.00"/></span></td>
		                              <td class="add2_nei_table">成人同行价：</td>
		                               <td class="add2_nei_table_typetext">¥<span class="disabledshowspan"><fmt:formatNumber type="number" value="${list.settlementAdultPrice}" pattern="#,##0.00"/></span></td>
		                               </tr>
		                             <tr>
		                              
		                               <td class="add2_nei_table">儿童同行价：</td>
		                               <td class="add2_nei_table_typetext">¥<span class="disabledshowspan"><fmt:formatNumber type="number" value="${list.settlementcChildPrice}" pattern="#,##0.00"/></span></td>
		                               <td class="add2_nei_table">特殊人群同行价：</td>
		                               <td class="add2_nei_table_typetext">¥<span class="disabledshowspan"><fmt:formatNumber type="number" value="${list.settlementSpecialPrice}" pattern="#,##0.00"/></span></td>                               
		                               <td class="add2_nei_table">备注：</td>
		                               <td class="add2_nei_table_typetext"><span class="disabledshowspan">${list.remark}</span></td>
		                              </tr>
		                           </tbody>
		                           </table>             
                              </div>
                              </c:forEach>
                              </c:if>
                              <div class="kong"></div>
                              </c:if>
                              <div class="seach25 seach100 pro-marks1">
                                  <p class="fbold f14" style="width:auto;margin-right:5px;">备注：</p>
                                  <p class="seach_r"><span class="disabledshowspan">${airticket.remark}</span></p>
                              </div>
                              <div class="kong"></div>
                            </div>
                        </div>
                        <div class="mod_information_dzhan_d" id="secondStepBtn" style="display: none;">
                            <div class="release_next_add">
                                     <!--  input type="button" value="上一步" onclick="secondToOne()" class="btn btn-primary valid displayClick"-->
                                     <input type="button" value="下一步" onclick="secondToThird()" class="btn btn-primary valid displayClick">
                            </div>
                        </div>
                        <div class="kong"></div>
                    </div>
                    
                    
                    	<!--填写价格结束-->       
        				<div style="clear:none;" class="kong"></div>
                        <div id="thirdStepDiv">
                            <!-- 上传文件 -->
                            <div class="mod_information_d"><span style=" font-weight:bold; padding-left:20px">上传资料</span></div>
                            <div class="mod_information_3">
                                <div class="">                  
                                    <table border="0" style="vertical-align:middle;margin-top:10px;" name="company_logo">
                                        <tbody><tr>
                                            <td><label>上传报名表：</label></td>
                                            <td><input type="text" style="width:160px;" readonly="readonly" name="fileLogo" class="valid"></td>
                                            <td>
                                                <input type="button" id="airticket_attach" name="airticket_attach" class="mod_infoinformation3_file" value="选择文件" onClick="uploadFiles('${ctx}',null,this)">
                                            	<div class="uploadPath" style="display: none">
                                            	<c:forEach items="${airTicketDocs}" var="afile"> 
	                                            	<input type="hidden" name="airticket_attach" value="${afile.id}">
	                                            	<input type="hidden" name="docOriName" value="${afile.docName}">
	                                            	<input type="hidden" name="docPath" value="${afile.docPath}">
                                            	</c:forEach>
                                            	</div>
                                            	<div id="currentFiles" style="display: none"></div>
                                            </td>
                                        </tr>
                                        <tr>
                                        <td colspan="2">
	                                        <ol class="batch-ol">
	                                        	<c:forEach items="${airTicketDocs}" var="afile"> 
		                                        <li>
		                                        <span>${afile.docName}</span><a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo('${afile.id}','airticket_attach',this);">删除</a>
		                                        </li>
		                                        </c:forEach>
	                                        </ol>
                                        </td>
<!--                                         <td colspan="1"> -->
<!--                                         	<div id="docfiles"> -->
<!--                                         	<c:forEach items="${airTicketDocs}" var="afile">   -->
<!--                                         		<li> -->
<!-- 				                              <label id="introduction_file" name="introduction_file">${afile.docName }</label> -->
<!-- 				                              <a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteDoc('${afile.id}')">删除</a> -->
<!-- 				                              </li> -->
<!-- 				                              <li> -->
<!-- 				                              <label id="introduction_file" name="introduction_file">${afile.docName }</label> -->
<!-- 				                              <a style="margin-left:10px;" href="javascript:void(0)" onclick="deleteDoc('${afile.id}')">删除</a> -->
<!-- 				                              </li> -->
				                              
<!-- 				                           	</c:forEach> -->
<!--                                         	</div> -->
<!--                                         </td> -->
                                        </tr>
                                    </tbody>
                                    </table>        
                                    
                                </div>
                            </div>
                             <c:if test="${is_need_groupCode eq '1' }"><!-- 当批发商具有团号库权限时,才进行修改记录的展示  -->
                            <!-- 大洋    机票产品在修改提交页面显示团号修改记录，对应需求号 c451,c453 -->
                            <c:if test="${fns:getUser().company.uuid eq 'f5c8969ee6b845bcbeb5c2b40bac3a23' || fns:getUser().company.uuid eq '1d4462b514a84ee2893c551a355a82d2' || fns:getUser().company.uuid eq '7a81c5d777a811e5bc1e000c29cf2586' || fns:getUser().company.uuid eq '5c05dfc65cd24c239cd1528e03965021' || fns:getUser().company.groupCodeRuleJP==0}"> 
	                            <div class="mod_information">
	                                   <div class="mod_information_d">
	                                   	<span style=" font-weight:bold; padding-left:20px;float:left">修改记录</span>
	                                   </div>
	                            </div>
	                            
	                            <c:forEach items="${groupcodeModifiedRecords}" var="modifiedRecord" varStatus="listIndex">
	                               <div class="mod_information_dzhan">
		                                <span class="modifyTime" style="margin-left: 30px"><fmt:formatDate value="${modifiedRecord.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
		                                                                                                 【<span class="modifyType">团号</span> 】
		                                                                                                  由【<span class="exGroupNo">${modifiedRecord.groupcodeOld}</span> 】修改成【<span class="groupNo">${modifiedRecord.groupcodeNew}</span>】
		                                 by【<span class="modifyUser">${modifiedRecord.updateByName}</span>】
	                               </div>
			                    </c:forEach>
			                 </c:if>  
                             </c:if>

                            
                            
                             <div class="release_next_add">
                                 <!--  input type="button" value="上一步" onclick="ThirdToSecond()" class="btn btn-primary"-->
                                 <input type="button" value="放&nbsp;&nbsp;弃" onclick="if(confirm('确定放弃吗?')){submitForm(3,this);}" class="btn btn-primary gray">
                                 <!--  <input type="button" value="保存草稿" onclick="submitForm(1,this);" class="btn btn-primary">-->
                                 <input type="button" value="提交发布" onclick="submitForm(2,this);" class="btn btn-primary">
                             </div>
                        </div>
                    </form>
                </div>
				<!--右侧内容部分结束-->
</body>
</html>