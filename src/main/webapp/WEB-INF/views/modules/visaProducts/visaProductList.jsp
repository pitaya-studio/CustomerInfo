
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>产品-签证产品列表</title>

<script src="${ctxStatic}/modules/visa/visaProductsList.js" type="text/javascript" ></script>

<script type="text/javascript">
var resetSearchParams = function() {
	$(':input', '#searchForm').not(':button, :submit, :reset, :hidden')
	.val('').removeAttr('checked').removeAttr('selected');
	$('#sysCountryId').val('');			
	$('#collarZoning').val('');		
	$('#visaType').val('');
	$('#sousuo').show();
}

	var ctx = "${ctx}";
	var countryId = "";

		$(function(){
			//搜索条件筛选
			launch();
			//产品名称文本框提示信息
			inputTips();
			//操作浮框
			operateHandler();
			
			//排序样式
			var _$orderBy = $("#orderBy").val();
		    if(_$orderBy==""){
		        _$orderBy="createDate DESC";
		    }else {
				if(_$orderBy == 'id DESC ASC') {
					_$orderBy="id ASC";
				}
			}
		    var orderBy = _$orderBy.split(" ");
		    $(".activitylist_paixu_left li").each(function(){
		       if ($(this).hasClass("li"+orderBy[0])){
		           orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
		           $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
		           $(this).attr("class","activitylist_paixu_moren");
		       }
		    });
			
		    $("#sysCountryId" ).comboboxInquiry();
		    $("#collarZoning" ).comboboxInquiry();
		    
		    $("#sysCountryId").next().find(".custom-combobox-input").blur(function(){
				if(countryId != $("#sysCountryId").val()) {
					countryId = $("#sysCountryId").val();
					getArea();
				}
			});
		    
		    //如果展开部分有查询条件的话，默认展开，否则收起	
			var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][type!='checkbox'][type!='text']");
			var searchFormselect = $("#searchForm").find("select");
			var inputRequest = false;
			var selectRequest = false;
			/*for(var i = 0; i<searchFormInput.length; i++) {
				if($(searchFormInput[i]).val() != " " && $(searchFormInput[i]).val() != null
						&& $(searchFormInput[i]).val() != "") {
					inputRequest = true;
				}
			}*/
			for(var i = 0; i<searchFormselect.length; i++) {
				if($(searchFormselect[i]).children("option:selected").val() != "" && $(searchFormselect[i]).children("option:selected").val() != " " &&
						$(searchFormselect[i]).children("option:selected").val() != null) {
					selectRequest = true;
				}
			}
			if(inputRequest||selectRequest) {
				$('.zksx').click();
			}
		    
		});
  
		//国家、领区联动
		function getArea() {
			if(countryId != '') {
				$.ajax({
					type:"POST",
					url:"${ctx}/visa/visaProducts/getArea?countryId="+countryId,
					async : false,
					success:function(result){
						$("#collarZoning").empty().append('<option value=" ">请选择</option>');
						if(result != null && result.length != 0) {
							for(var index = 0; index < result.length; index++) {
								$("#collarZoning").append('<option value=\'' + result[index][0] + '\'>' + result[index][1] + '</option>');
							}
						}
						$("#collarZoning" ).comboboxInquiry({
							"afterInvalid":function(event,data){
								$(this).trigger("click");
							}
						});
					}
				});
			}else if(countryId == ''){
				$("#collarZoning").empty().append('<option value=" ">请选择</option>');
			}
		}
		
		
   		//产品修改
  		function productModify(proId){
	    	$("#searchForm").attr("action","${ctx}/visa/visaProducts/mod/"+proId);
			$("#searchForm").submit();
	    }
  
 		var orderBy = null;
 
 		function sortby(sortBy,obj){
	           var temporderBy = $("#orderBy").val();
	           if(temporderBy.match(sortBy)){
	               sortBy = temporderBy;
	               if(sortBy.match(/ASC/g)){
	                   sortBy = sortBy.replace(/ASC/g,"");
	               }else{
	                   sortBy = $.trim(sortBy)+" ASC";
	               }
	           }
	           
	           $("#orderBy").val(sortBy);
	           $("#searchForm").attr("action","${ctx}/visa/visaProducts/list/${visaProducts.productStatus}");
	           $("#searchForm").submit();
	       }
            

		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/visa/visaProducts/list/${visaProducts.productStatus}");
			$("#searchForm").submit();
		}
		//产品删除
		function confirmxDel(mess,proId){
	   
			top.$.jBox.confirm(mess,'系统提示',function(v){
			if(v=='ok'){	 
				$("#searchForm").attr("action","${ctx}/visa/visaProducts/deleteVisaProducts/"+ proId);
				$("#searchForm").submit();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
			return false;
		}
		
		var visaProductIds = "";
		visaProductIds = "${visaProductIds}";
		function checkall(num){
			if(document.getElementById("allChk"+num).checked==true){
			 document.getElementById("allChk1").checked =true;
			 document.getElementById("allChk2").checked=true;
				 var ids = document.getElementsByName("visaProductId");    
				 
				 for (var i = 0; i < ids.length; i++)      
                 {            
                   ids[i].checked = true; 
                   if(visaProductIds.indexOf(ids[i].value) < 0){
                	   	visaProductIds = visaProductIds + ids[i].value+",";  
                  }     
                 }        
			   
			}				
			else{
				 document.getElementById("allChk1").checked =false;
			     document.getElementById("allChk2").checked=false;
			     var ids = document.getElementsByName("visaProductId");    
			     
			      for (var i = 0; i < ids.length; i++)      
                 {            
                   ids[i].checked = false; 
                   if(visaProductIds.indexOf(ids[i].value)  >= 0){
                	   visaProductIds = visaProductIds.replace(ids[i].value+",","");
                	   	//visaProductIds = visaProductIds + ids[i].value+",";  
                  }     
                 }   
				
			}
			$("#visaProductIds").val(visaProductIds);
		}
		
		
		//文件下载
		function downloads(docid,activitySerNum,acitivityName,iszip){
	    	if(iszip){
	    		var zipname = activitySerNum;
	    		window.open(encodeURI(encodeURI("${ctx}/sys/docinfo/zipdownload/"+docid+"/"+zipname)));
	    	}
	    		
	    	else
	    		window.open("${ctx}/sys/docinfo/download/"+docid);
	    }
	    
	    // 批量下架确认对话框
	    function confirmBatchOff(mess){
			top.$.jBox.confirm(mess,'系统提示',function(v){
			if(v=='ok'){
				//loading('正在提交，请稍等...');
				$("#searchForm").attr("action",ctx + "/visa/visaProducts/batchOnOrOffVisaProducts/2/"+visaProductIds);
				$("#searchForm").submit();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
			return false;
		}
		// 批量上架确认对话框
		function confirmBatchRelease(mess){
			top.$.jBox.confirm(mess,'系统提示',function(v){
			if(v=='ok'){
				//loading('正在提交，请稍等...');
				$("#searchForm").attr("action",ctx + "/visa/visaProducts/batchOnOrOffVisaProducts/3/"+visaProductIds);
				$("#searchForm").submit();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
			return false;
		}
		//批量上架和批量下架
	    function confirmBatchIsNull(mess,sta) {
			if(visaProductIds != ""){
				if(sta=='off'){
					confirmBatchOff(mess);
				}else if(sta=='del'){
					confirmBatchDel(mess);
				}else{
					confirmBatchRelease(mess);
				}
			}else{
				$.jBox.error('未选择产品','系统提示');
			}
		}
		function getDepartment(departmentId) {
			$("#departmentId").val(departmentId);
			$("#searchForm").submit();
		}
		
		
		 //对应需求    c460 
		function openGroupLibPage(){
			//debugger;
		    $.jBox("iframe:"+ctx+"/activity/groupcodelibrary/toGroupcodeLibraryBox?groupcodelibtype=6",{  //groupcodelibtype =6 为签证
		        title:"团号库",buttons:{'关闭':1},height:680,width:680,persistent:true
		    }).find("#jbox-content").css("overflow","hidden");
		}
		
</script>
</head>
<body>
<page:applyDecorator name="activity_visa_head">
    <page:param name="current">${visaProducts.productStatus }</page:param>
</page:applyDecorator>

<!-- 签证公告展示 -->
<%@ include file="/WEB-INF/views/modules/visa/visaPublicBulletinMarquee.jsp"%>

        <!--右侧内容部分开始-->
        <div class="activitylist_bodyer_right_team_co_bgcolor">
            <form:form id="searchForm" modelAttribute="visaProducts" action="${ctx}/visa/visaProducts/list/${visaProducts.productStatus}" method="post">

				<input type="hidden" id="ctx" value="${ctx}"/>
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
                <input id="visaProductIds" type="hidden" name="visaProductIds" value="${visaProductIds}"/>
				<input id="departmentId" name="departmentId" type="hidden" value="${departmentId }" />
                <div class="activitylist_bodyer_right_team_co">

                    <div class="activitylist_bodyer_right_team_co2 pr">
                        <label>搜索：</label>
                        <input style="width:260px" class="txtPro inputTxt inquiry_left_text"
                               id="productName" name="productName" value="${productName}" type="text"
                               placeholder="输入产品名称、团号，支持模糊匹配"/>
                        <%--<span id="sousuo" class="ipt-tips">输入产品名称、团号，支持模糊匹配</span>--%>
                    </div>
                    <a class="zksx">筛选</a>
                    <div class="form_submit">
                        <input class="btn btn-primary ydbz_x" value="搜索" type="submit">
                        <input type="button" value="清空所有条件" onclick="resetSearchParams()" class="btn ydbz_x" />
                        <c:if test="${fns:getUser().company.isNeedGroupCode eq 1}"> 
					    	
				    	</c:if>
                    </div>
                    <%--bug17584--%>
                    <p class="main-right-topbutt"><a class="primary"href="${ctx}/visa/visaProducts/addVisaInformation">发布新产品</a></p>
                    <div class="ydxbd">
                        <span></span>
                        <div class="activitylist_bodyer_right_team_co1">
                            <div class="activitylist_team_co3_text">签证国家：</div>
                            <select  name="sysCountryId" id="sysCountryId">
                               	<option value=" ">所有</option>
                                <c:forEach items="${countryInfoList}" var="visaCountry">
                                    <option value="${visaCountry[0]}" <c:if test="${fn:trim(sysCountryId) eq visaCountry[0]}"> selected="selected" </c:if> >${visaCountry[1]}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="activitylist_bodyer_right_team_co1">
                            <div class="activitylist_team_co3_text">领区：</div>
                            <select name="collarZoning" id="collarZoning">
                                <option value=" ">所有</option>
                                <c:forEach items="${collarZoningList}" var="collarZonings">
                                    <option value="${collarZonings[0]}" <c:if test="${collarZoning eq collarZonings[0]}"> selected="selected" </c:if> >${collarZonings[1]}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="activitylist_bodyer_right_team_co1">
                            <div class="activitylist_team_co3_text">签证类型：</div>
                            <div class="selectStyle">
                            <select name="visaType" id="visaType">
                                <option value=" ">所有</option>
                                <c:forEach items="${visaTypeList}" var="visaTypes">
                                    <option value="${visaTypes.key}" <c:if test="${visaType eq visaTypes.key}"> selected="selected"</c:if> >${visaTypes.value}</option>
                                </c:forEach>
                            </select>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
            <c:if test="${fn:length(page.list) ne 0}">
                <div class="activitylist_bodyer_right_team_co_paixu">
                    <div class="activitylist_paixu">
                        <div class="activitylist_paixu_left">
                            <ul>
                                <li class="activitylist_paixu_left_biankuang liid"><a onclick="sortby('id',this)">创建时间</a></li>
                                <li class="activitylist_paixu_left_biankuang liupdateDate"><a onclick="sortby('updateDate',this)">更新时间</a></li>
                                <li class="activitylist_paixu_left_biankuang livisaPrice"><a onclick="sortby('visaPrice',this)">成本价</a></li>
                                <!--  <li class="activitylist_paixu_moren"><a onclick="sortby('visaPay',this)">应收价<i class="icon icon-arrow-down"></i></a></li>-->
                                <li class="activitylist_paixu_left_biankuang livisaPay"><a onClick="sortby('visaPay',this)">应收价</a></li>

                            </ul>
                        </div>
                        <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
                        <div class="kong"></div>
                    </div>
                </div>
            </c:if>
        <div class="supplierLine" <c:if test="${empty showAreaList || fn:length(showAreaList) == 1}">style="display: none;"</c:if>>
			<%--<a <c:if test="${empty departmentId}">class="select"</c:if> href="javascript:void(0)" onclick="getDepartment('');">全部产品</a>--%>
			<c:forEach var="department" items="${showAreaList}" varStatus="status">
				<c:choose>
					<c:when test="${(department.id == departmentId) || (empty departmentId && status.index == 0)}">
						<a class="select" href="javascript:void(0)" onclick="getDepartment('${department.id}');">${department.name}</a>
					</c:when>
					<c:otherwise>
						<a href="javascript:void(0)" onclick="getDepartment('${department.id}');">${department.name}</a>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</div>

        <table id="contentTable" class="table mainTable activitylist_bodyer_table">
            <thead>
            <tr>
            	<th width="4%">序号</th>
                <th class="table_borderLeftN" width="4%">全选<input id="allChk1" name="allChk" onclick="checkall(1)" type="checkbox"></th>
                <th width="14%">产品名称</th>
                <c:set var="companyId" value="${fns:getUser().company.id }"></c:set>
                <c:set var="companyUUID" value="${fns:getUser().company.uuid }"></c:set>
	            <!-- C460V3  所有批发商显示团号-->
	             <!-- C460V5 非环球行显示该列-->
	            <c:if test="${fns:getUser().company.uuid ne '7a816f5077a811e5bc1e000c29cf2586'}">
	                <th width="11%">团号</th>
	            </c:if>
                <th width="7%">计调</th>
                <th width="7%">签证国家</th>
                <th width="7%">签证类型</th>
                <th width="7%">签证领区</th>
                <th width="8%">成本价格</th>
                <th width="8%">应收价格</th>
                <c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23'}">
                <th width="8%">发票税</th>
                </c:if>
                <th width="9%">发布时间</th>
                <th width="10%">下载签证资料模板</th>
                <th width="4%">操作</th>
            </tr>
            </thead>
            <tbody>

            <c:forEach items="${page.list}" var="visaProduct" varStatus="s">

                <tr id="parent1">
                	<td>${s.count}</td>
                    <td class="table_borderLeftN">
                        <input type="checkbox" name="visaProductId" value="${visaProduct.id }" onclick="idcheckchg(this)"/>
                    <td class="activity_name_td">
                        <a href="javascript:void(0)" onclick="javascript:window.open('${ctx}/visa/visaProducts/visaProductsDetail/${visaProduct.id}')">
                                ${visaProduct.productName}</a>
                    </td>
                    <!-- C460V3 展示团号 -->
                     <!-- C460V5 只有非环球行展示团号 -->
                    <c:if test="${fns:getUser().company.uuid ne '7a816f5077a811e5bc1e000c29cf2586'}">
                       <td>${visaProduct.groupCode }</td>
                    </c:if>
                    <td>${visaProduct.createBy.name }</td>
                    <td>
                    	<c:if test="${not empty visaProduct.sysCountryId}">
	                    	${fns:getCountryName(visaProduct.sysCountryId) }
                    	</c:if>
                    </td>
                    <td>
                        <c:forEach items="${visaTypeList}" var="visas">
                            <c:if test="${visas.key eq visaProduct.visaType}">
                                ${visas.value}
                            </c:if>
                        </c:forEach>    </td>
                    <td>
                    	<c:if test="${not empty visaProduct.collarZoning }">
                           ${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}
                       	</c:if>
                   	</td>
                    <td class="tr">${fns:getCurrencyInfo(visaProduct.currencyId,0,'mark')}<span class="tdred fbold"><fmt:formatNumber pattern="#,##0.00" value="${visaProduct.visaPrice}" /></span></td>
                    <td class="tr">${fns:getCurrencyInfo(visaProduct.currencyId,0,'mark')}<span class="tdblue fbold"><fmt:formatNumber pattern="#,##0.00" value="${visaProduct.visaPay}" /></span></td>
                    <!-- 0258-qyl -->
                    <c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23'}">
                    <c:if test="${!empty visaProduct.invoiceQZ }">
                    <td class="tr"><span class="tdblue fbold"><fmt:formatNumber pattern="#,##0.00" value="${visaProduct.invoiceQZ}" />&nbsp;%</span></td>
                    </c:if>
                    <c:if test="${empty visaProduct.invoiceQZ }">
                    <td class="tr"><span class="tdblue fbold"><fmt:formatNumber pattern="#,##0.00" value="0" />&nbsp;%</span></td>
                    </c:if>
                    </c:if>
                    <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${visaProduct.updateDate }"/></td>
                    <td>
                    	<c:set value="${fns:findFileIds(visaProduct.id)}" var="docIds"></c:set>
                    	<c:if test="${not empty docIds}">
                    		<a target="_blank" onclick="downloads('${docIds}','签证资料',null,true)">签证资料模板</a>
                   		</c:if>
                   	</td>
                    <td class="p0">
                        <dl class="handle">
                            <dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
                            <dd class="">
                                <p>
                                    <span></span>
                                    <a href="javascript:void(0)" onclick="javascript:productModify(${visaProduct.id})">修改</a><br>
                                    <a href="javascript:void(0)" onclick="return confirmxDel('要删除该产品吗？', ${visaProduct.id})">删除</a><br>
                                    <a href="javascript:void(0)" onclick="javascript:window.open('${ctx}/visa/visaProducts/visaProductsDetail/${visaProduct.id}')">详情</a>
                                	<c:if test="${visaProducts.productStatus eq 2}">
				                        <a href="${ctx}/visa/visaProducts/batchOnOrOffVisaProducts/2/${visaProduct.id}" onClick="return confirmx('需要将该产品下架吗', this.href)">下架</a>
			                    	</c:if>
			                        <c:if test="${visaProducts.productStatus eq 3}">
			                            <a href="${ctx}/visa/visaProducts/batchOnOrOffVisaProducts/3/${visaProduct.id}" onClick="return confirmx('需要将该产品上架吗', this.href)">上架</a>
			                        </c:if>
                                </p>
                            </dd>
                        </dl>

                    </td>

                </tr>

            </c:forEach>

            </tbody>
        </table>
        </div>
     	<div class="page">
                    <div class="pagination">
                <dl>
                    <dt><input  id="allChk2" name="allChk" onclick="checkall(2)" type="checkbox">全选</dt>
                    <dd>
                    	<c:if test="${visaProducts.productStatus eq 2}">
	                        <input type="button" class="btn ydbz_x" onclick="confirmBatchIsNull('需要将选择的产品下架吗','off')" value="批量下架">
                    	</c:if>
                        <c:if test="${visaProducts.productStatus eq 3}">
                            <input type="button" class="btn ydbz_x" onclick="confirmBatchIsNull('需要将选择的产品上架吗','on')" value="批量上架">
                        </c:if>
                        <%--<a onclick="confirmBatchIsNull('删除所有选择的产品吗','del')">批量删除</a>--%>
                        <input type="button" class="btn ydbz_x" onclick="confirmBatchIsNull('删除所有选择的产品吗','del')" value="批量删除">
                    </dd>
                </dl>
                <div class="endPage">
                    ${page}
                </div>
                <div style="clear:both;"></div>
            </div>
        </div>
</body>
</html></html>