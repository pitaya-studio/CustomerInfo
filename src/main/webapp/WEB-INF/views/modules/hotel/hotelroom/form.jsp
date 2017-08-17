<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>酒店房型信息维护</title>
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
	<script type="text/javascript" src="${ctxStatic}/js/dictComponent.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				rules:{
					roomName:{
						required:true,
						remote: {
							type: "POST",
							url: "${ctx}/hotelRoom/check?hotelUuid=${hotelRoom.hotelUuid}&uuid="+$('#uuid').val()
								}
					},
					sort:{
						required:true,
						digits:true
					}
				},
				submitHandler: function(form){
					var url = "";
					if($("#uuid").val()=='') {
						url="${ctx}/hotelRoom/save";
					} else {
						url="${ctx}/hotelRoom/update";
					}
					
					//处理容住率数据
					var uuidAndNums = [];
					var roomOccuRateUuids = [];
					var validate = false;
					var index = 0;
					
					$(".occupancyRate").each(function (){
						index++;
						var travelerTypeNum = $(this).find("input[name=travelerTypeNum]");
						var occuremark = $(this).find("input[name=occuremark]").val();
						var uuidAndNum = "";
						for(var i=0;i<travelerTypeNum.length; i++) {
							var uuid = $(travelerTypeNum[i]).attr("travelerTypeUUid");
							var num = $(travelerTypeNum[i]).val();
							
							if(uuid && num && num !='0') {
								uuidAndNum += uuid + "_" + num + ",";
							}
						}
						
						if(uuidAndNum == "") {
							validate = true;
							return false;
						}
// 						alert(occuremark);
						uuidAndNums.push(uuidAndNum+occuremark);
						
						var occupancyRateUUid = $(this).find("input[name=occupancyRateUUid]");
						if(occupancyRateUUid) {
							roomOccuRateUuids.push(occupancyRateUUid.val());
						}
					});
					if(validate) {
						$.jBox.tip("第" + index + "行的容住率人数最少填写一个，请重新输入！");
						return false;
					}
					
					var str="";
		            $("input[name='hotelMealType']:checkbox").each(function(){ 
		                if($(this).attr("checked")){
		                    str += $(this).val()+",";
		                }
		            });
		            $("#mealtype").val(str);
					//20150812 add by WangXK
					var hotelGuestTypeUuids="";
					var hotelGuestTypeNames="";
		            $("input[name='hotelGuestType']:checkbox").each(function(){ 
		                if($(this).attr("checked")){
		                	hotelGuestTypeUuids += $(this).val()+",";
		                    hotelGuestTypeNames += $(this).attr("data-value")+",";
		                }
		            });
		            $("#hotelGuestTypeUuids").val(hotelGuestTypeUuids);
		            $("#hotelGuestTypeNames").val(hotelGuestTypeNames);
		            
					$("#occupancyRates").val(uuidAndNums.join(";"));
					$("#roomOccuRateUuids").val(roomOccuRateUuids.join(";"));
					
					$("#btnSubmit").attr("disabled", "disabled");
					$.post(url,$("#inputForm").serialize(),function(data){
						if(data.message == "1"){
							$("#searchForm",window.opener.document).submit(); 
							$.jBox.tip("保存成功!");
							setTimeout(function(){window.close();},500);
						}else if(data.message == "2"){
							$("#searchForm",window.opener.document).submit(); 
							$.jBox.tip("修改成功!");
							setTimeout(function(){window.close();},500);
						}else if(data.message == "3"){
							$.jBox.tip(data.error,'warning');
							$("#btnSubmit").attr("disabled", false);
						}else{
							$.jBox.tip('操作异常！','warning');
							$("#btnSubmit").attr("disabled", false);
						}
					});
				},
				messages:{
					roomName:{remote:"名称已存在"},
				}
			});
			
			
			if($("#uuid").val()=='') {
				$("#sort").val("50");
			}
			
			//初始化
			bedTypeChange();
			

			$("div.maintain_add").on('blur',"input.add_house_a",function() {
				var pnum=0;
				$('.add_house_a').each(function(){
					var v = $(this).val();
					pnum+=Number(v);
				});
				$('#add_house_pnum').val(pnum);
			});
			
			 $('[name="guestTypeChk"]').on('change',function(){
		            var $this =$(this);
		            var $guestTypes = $('[name="guestTypes"]');
		            if($this.is(':checked')){
		                $guestTypes.show();
		            }else{
		                $guestTypes.hide();
		                $guestTypes.find('input[type="checkbox"]').removeAttr('checked');
		            }
		        });
		        $('[name="guestTypeChk"]').trigger('change');
		});
		
		function bedTypeChange() {
			var bed = $("#bed").val();
			if(bed != '') {
				$.post("${ctx}/sysCompanyDictView/getDescByUuid", {"uuid":bed},
					function(data){
						$("#bedTypeDescription").html(data);
					}
				);
			} else {
				$("#bedTypeDescription").html('');
			}
		}
		
		function addCompontCallBack() {
			bedTypeChange();
		}
		
		function add_house_people_num(obj){
			var pnum=0;
			$(obj).parent().parent().find('input.add_house_a').each(function(){
				var v = $(this).val();
				if(isNumber(v)) {
					pnum+=Number(v);
				} else {
					$(this).val('');
				}
			});
			$(obj).parent().parent().find('td').find('input.add_house_pnumclass').val(pnum);
		}
		
		function isNumber(pnum){
			var isNumber = /^\d+(\.\d+)?$/;
			return isNumber.test(pnum);
		}
		function add_house_new(obj, flag){
			var num=$('#add_house_num').val();
			var html='<tr class="occupancyRate">';
				<c:forEach items="${travelerTypes }" var="travelerType">
					html+='<td><input type="text" maxlength="1" class="add_house_a" onblur="add_house_people_num(this)" name="travelerTypeNum" travelerTypeUUid="${travelerType.uuid }" style=" padding: 4px 10px;"><i class="add_house_a_em">${travelerType.shortName }</i></td>';
				</c:forEach>
				html += '<td><input style="width: 80px;" type="text" name="occuremark"></td>';
				html += '<td><a onclick="del_house_new(this)" class="ydbz_x nofla gray">删除</a></td>';
				html+='<td>可住人数：<input type="text" name="occupancy" disabled="disabled" class="add_house_pnumclass" style="width:20px;" /> 人</td>';
				html+='</tr>';
			$(".occupancyRate").eq($(".occupancyRate").length-1).after(html);;
		}
		function del_house_new(obj){
			$(obj).parent().parent().remove();
		}
		
		function addCompontChange(obj, compName, type, inputType) {
			if($(obj).find('option:last').attr('selected') == 'selected') {
				addCompont('${ctx}' ,obj, compName, type, inputType);
			} else {
				bedTypeChange();
			}
		}
		
		//表单验证
		function validate(){
			var isNumber = /^\d+(\.\d+)?$/;
			var roomArea = $("#roomArea").val();//建筑面积
			if(roomArea != '' && !isNumber.test(roomArea)) {
			    $.jBox.tip("请输入正确的数字格式。", 'error', { focusId: "roomArea"}); 
			    $("#roomArea").focus();
				return false;
			}
			
			$("#inputForm").submit();
		}
		
	</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">房型基本信息</div>
	<form:form method="post" modelAttribute="hotelRoom" action="" class="form-horizontal" id="inputForm" novalidate="">
		<form:hidden path="uuid" />
		<form:hidden path="hotelUuid" />
		<%--<form:hidden path="hotelMealUuid" /> --%>
		<input type="hidden" id="occupancyRates" name="occupancyRates" />
		<input type="hidden" id="roomOccuRateUuids" name="roomOccuRateUuids" />
		<input type="hidden" id="mealtype" name="mealtype" />
		<input type="hidden" id="hotelGuestTypeUuids" name="hotelGuestTypeUuids" />
		<input type="hidden" id="hotelGuestTypeNames" name="hotelGuestTypeNames" />
		<div class="maintain_add">
			<p>
				<label>房型名称：</label>
				<form:input path="roomName" htmlEscape="false" maxlength="99" class="required" />
			</p>
			<p>
				<label>展示名称：</label>
				<form:input path="showName" htmlEscape="false" maxlength="99" />
			</p>
			<p  style=" width: 280px;">
                <label>房间数：</label>
                <form:input path="roomNumb" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"  
                                    onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'0')}else{this.value=this.value.replace(/\D/g,'')}"  htmlEscape="false" maxlength="50" data-type="number" /> 间
            </p>
			<p class="maintain_kong"></p>
			<p>
				<label>床型名称：</label>
				<trekiz:defineDict name="bed" type="hotel_bed_type" defaultValue="${hotelRoom.bed }" onchange="addCompontChange(this, 'hotelBedType', 'hotel_bed_type', 'select')" element="<option value=''>+添加</option>" />
				<span id="bedTypeDescription"></span>
			</p>
			<p class="maintain_kong"></p>
			<div class="add_house_people_p">
              <div class="add_house_people_p_title">容住率：</div>
			  <div class="add_house_people_p_table">
				  <table>
				  	<thead>
						<tr>
							<c:forEach items="${travelerTypes }" var="travelerType">
								<td>${travelerType.name}数 <br /><span style="color:#999999;">(${travelerType.rangeFrom}-${travelerType.rangeTo})</span></td>
							</c:forEach>
							<td align="center">备注</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
					</thead>
		<tbody>
			<!-- 当没有酒店容住率信息时，只显示新增行；反之，既显示新增行，有显示删除行 -->
			<c:if test="${not empty travelerTypes}">
			<c:choose>
				<c:when test="${hotelRoomOccuRates == null}">
					<tr class="occupancyRate">
						<c:forEach items="${travelerTypes }" var="travelerType">
							<td><input type="text" maxlength="1" class="add_house_a" onblur="add_house_people_num(this)" name="travelerTypeNum" travelerTypeUUid="${travelerType.uuid }" style=" padding: 4px 10px;" /><i class="add_house_a_em">${travelerType.shortName }</i></td>
						</c:forEach>
						<td><input style="width: 80px;" name="occuremark" type="text"/></td>
						<td><a id="add_occupancyRate" onclick="add_house_new(this)" class="ydbz_x nofla">新增</a></td>
						<td>可住人数：<input type="text" name="occupancy" disabled="disabled" class="add_house_pnumclass" style="width:20px;" /> 人</td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach items="${hotelRoomOccuRates }" var="hotelRoomOccuRate" varStatus="status">
						<tr class="occupancyRate">
							<input type="hidden" name="occupancyRateUUid" value="${hotelRoomOccuRate.uuid }" />
							<c:forEach items="${travelerTypes }" var="travelerType">
								<td>
									<input type="text" maxlength="1" class="add_house_a" onblur="add_house_people_num(this)" name="travelerTypeNum" 
										<c:forEach items="${hotelRoomOccuRate.hotelRoomOccuRateDetails }" var="roomOccuRateDetail">
											<c:if test="${travelerType.uuid == roomOccuRateDetail.travelerTypeUuid }">
												 value="${roomOccuRateDetail.count }" 
											</c:if>
										</c:forEach> 
									travelerTypeUUid="${travelerType.uuid }" />
									<i class="add_house_a_em">${travelerType.shortName }</i>
								</td>
							</c:forEach>
							<c:choose>
								<c:when test="${status.index == 0}">
									<td><input style="width: 80px;" name="occuremark" value="${hotelRoomOccuRate.remark}" type="text"/></td>
									<td><a id="add_occupancyRate" onclick="add_house_new(this)" class="ydbz_x nofla">新增</a></td>
								</c:when>
								<c:otherwise>
									<td><input style="width: 80px;" name="occuremark" value="${hotelRoomOccuRate.remark}" type="text"/></td>
									<td><a onclick="del_house_new(this)" class="ydbz_x nofla gray">删除</a></td>
								</c:otherwise>
							</c:choose>
							<td>可住人数：<input type="text" name="occupancy" value="${hotelRoomOccuRate.occupancy}" disabled="disabled" class="add_house_pnumclass" style="width:20px;" /> 人</td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
			</c:if>
				<tr>
					<td colspan="7"><span style="color:#999999;">注：A代表Adult；B代表Baby；C代表Children；O代表Old man；S代表Special populations；容住率类型的单位：岁，例如：成人数(10-15)：指成人的年龄范围是10到15岁</span></span></td>
				</tr>
		</tbody>
				  </table>
			  </div>
			</div>
            <input type="hidden" value="1" id="add_house_num" />
			<p class="maintain_kong"></p>
			<p style="display: none;">
				<label>可加床数量：</label>
				<form:input path="extraBedNum" htmlEscape="false" maxlength="11" class="required digits" /> 人
			</p>
			<p style="display: none;">
				<label>加床费用：</label>
				<form:input path="extraBedCost" htmlEscape="false" maxlength="11" class="shortinput" /> 元/人
			</p>
			<p style="display: none;">
				<label>加床乘客类型：</label>
				<form:select path="extraBedCustomer" id="extraBedCustomer" name="extraBedCustomer">
					<form:option value="">请选择</form:option>
					<c:forEach items="${travelerTypes }" var="entity">
						<form:option value="${entity.uuid}">${entity.name}</form:option>
					</c:forEach>
				</form:select>
			</p>
			<p class="maintain_kong"></p>
			<p>
				<label>建筑面积：</label>
				<input type="text" id="roomArea" name="roomArea" maxlength="11" data-type="number" value="${hotelRoom.roomArea }" /> 平米
			</p>
			<p class="maintain_kong"></p>
			<p>
				<label>排序：</label>
				<form:input path="sort" htmlEscape="false" maxlength="4" class="required digits" />
			</p>
			<p class="maintain_kong"></p>
                 <p class="maintain_pfull">
                    <span class="checkboxdiv">
                    </span>
                    <label><input type="checkbox" name="guestTypeChk" value="" checked="checked">住客类型：</label>  
                 </p>
            <p class="maintain_pfull" name="guestTypes">
                <label></label>
                <span class="checkboxdiv" >
                	  <c:forEach items="${hotelGuestTypes}" var="hotelGuestType" >
<!--<trekiz:defineDict name="hotelGuestTypeName" type="hotelGuestTypeName" input="checkbox"  defaultValue="${hotelGuestType.uuid }"/>--> 
                	  <label><input type="checkbox" name="hotelGuestType"  value="${hotelGuestType.uuid}" <c:if test="${relationString.contains(hotelGuestType.uuid) }">checked="checked"</c:if> data-value="${hotelGuestType.name}">${hotelGuestType.name}</input></label>
                	  </c:forEach>
                </span>
            </p>
			<p class="maintain_kong"></p>
			<p class="maintain_pfull">
				<label>房型特色：</label> 
				<span class="checkboxdiv"> 
					<trekiz:defineDict name="roomFeatures" type="room_feature" input="checkbox"  defaultValue="${hotelRoom.roomFeatures }"/> 
					<a onclick="addCompont('${ctx}',this,'roomFeatures','room_feature', 'checkbox')" class="ydbz_x nofla">添加</a> 
				</span>

			</p>
			<p class="maintain_pfull">
				<label>关联餐型：</label> 
				<span class="checkboxdiv">
					<c:forEach items="${mealList }" var="ml">
						<input type="checkbox" name="hotelMealType" value="${ml.uuid }"  <c:if test="${mealString.contains(ml.uuid) }">checked="checked"</c:if> >${ml.mealName }</input>
					</c:forEach>
				</span>

			</p>
			<p class="maintain_kong"></p>
			<p>
				<label class="activitylist_team_co3_text">入住日期：</label> 
				<input id="inDate" name="inDate" type="text" onclick="WdatePicker({maxDate:$('#outDate').val()})" value="<fmt:formatDate value="${hotelRoom.inDate }" pattern="yyyy-MM-dd" />" class="dateinput " />
			</p>
			<p>
				<label class="activitylist_team_co3_text">离店日期：</label> 
				<input id="outDate" name="outDate" type="text" onclick="WdatePicker({minDate:$('#inDate').val()})" value="<fmt:formatDate value="${hotelRoom.outDate }" pattern="yyyy-MM-dd" />" class="dateinput" />
			</p>
			<p class="maintain_kong"></p>
			<p class="maintain_pfull">
                <label class="activitylist_team_co3_text" >备注：</label>
                <textarea  style="width: 35%;height: 200px"  name="remark"  maxlength="2000" >${hotelRoom.remark}</textarea>
            </p>
			<p class="maintain_btn">
				<label>&nbsp;</label> 
				<input type="button" value="返&nbsp;&nbsp;&nbsp;回" class="btn btn-primary gray" onclick="window.close();" />
				<input type="button" value="提&nbsp;&nbsp;&nbsp;交" class="btn btn-primary" id="btnSubmit" onclick="validate()" />
			</p>
		</div>
	</form:form>
	<!--右侧内容部分结束-->
</body>
</html>
