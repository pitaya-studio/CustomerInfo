
<%@page import="com.trekiz.admin.modules.visa.entity.VisaPublicBulletin"%>
<%@page import="com.trekiz.admin.common.persistence.Page"%>
<%@page import="com.trekiz.admin.modules.visa.service.VisaPublicBulletinService"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>签证成本审核</title>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/modules/visa/visaProductsList.js"></script>

<script type="text/javascript">
$(function(){
    $("div.message_box div.message_box_li").hover(function(){
        $("div.message_box_li_hover",this).show();
    },function(){
          $("div.message_box_li_hover",this).hide();
    });

	//搜索条件筛选
	launch();
	//产品名称文本框提示信息
	inputTips();
	//操作浮框
	operateHandler();
	
	var _$orderBy = $("#orderBy").val();
    if(_$orderBy==""){
        _$orderBy="id DESC";
        $("#orderBy").val("id");
    }
	
    var orderBy = _$orderBy.split(" ");
    $(".activitylist_paixu_left li").each(function(){
        if ($(this).hasClass("li"+orderBy[0])){
            orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
            $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
            $(this).attr("class","activitylist_paixu_moren");
        }
    });

     var _$review = $("#review").val();
     if(_$review==""){
         $("#isRecord").addClass("select");
      }else{
                $("#isRecord"+_$review).addClass("select"); 
       } 

            //如果展开部分有查询条件的话，默认展开，否则收起   
            var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
            var searchFormselect = $("#searchForm").find("select");
            var inputRequest = false;
            var selectRequest = false;
            for(var i = 0; i<searchFormInput.length; i++) {
                if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
                    inputRequest = true;
                }
            }
            for(var i = 0; i<searchFormselect.length; i++) {
                if($(searchFormselect[i]).children("option:selected").val() != "" && 
                        $(searchFormselect[i]).children("option:selected").val() != "100" &&
                        $(searchFormselect[i]).children("option:selected").val() != null) {
                    selectRequest = true;
                }
            }
            if(inputRequest||selectRequest) {
                $('.zksx').click();
            }    

});
  
  
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
	           $("#searchForm").attr("action","${ctx}/cost/review/visaList/${reviewLevel}");
	           $("#searchForm").submit();
	       }
            

function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/cost/review/visaList/${reviewLevel}");
			$("#searchForm").submit();
}
		
var visaProductIds = "";
visaProductIds = "${visaProductIds}";
	
function changeIsRecord(itemRecord){
          $("#review").val(itemRecord);
          $(".supplierLine .select").removeClass("select");
          $("#isRecord"+itemRecord).addClass("select");
          $("#searchForm").submit();
}

	  var activityIds = "";
      function checkall(obj){
			if($(obj).attr("checked")){
				$("input[name='allChk']").attr("checked",'true');
				$("input[name='activityId']").attr("checked",'true');
				$("input[name='activityId']:checked").each(function(i,a){
					if(activityIds.indexOf(a.value) < 0){					
						activityIds = activityIds + a.value+",";
					}
  				});
			}				
			else{
				$("input[name='allChk']").removeAttr("checked");
				$("input[name='activityId']").removeAttr("checked");
				$("input[name='activityId']").each(function(i,a){
					if(activityIds.indexOf(a.value) >= 0){					
						activityIds = activityIds.replace(a.value+",","");
					}
  				});
			}
			$("#activityIds").val(activityIds);
				
		}
		
		
		function idcheckchg(obj){
			var value = $(obj).val();
			if($(obj).attr("checked")){
				if(activityIds.indexOf($(obj).val()) < 0){
					activityIds = activityIds+$(obj).val()+",";
				}
			}			
			else{
				if($("input[name='allChk']").attr("checked"))
					$("input[name='allChk']").removeAttr("checked");
				if(activityIds.indexOf($(obj).val()) >= 0){
					
					activityIds = activityIds.replace($(obj).val()+",","");
				}
			}
			$("#activityIds").val(activityIds);
				
		}
		
		function batchAuditing(){
			var activityIds = $("#activityIds").val();
			if(activityIds == ""){
				$.jBox.info('至少选择一记录进行操作','系统提示');
				return;
			}
			
			$.ajax({
	              type: "POST",
	              url: "",
	              data: {
	                  activityIds : $("#activityIds").val()
	              },
	              success: function(msg) {
	                  // 页面刷新处理
	              }
	          });
		}
		
		function cancelOp(costRecordId,orderType,nowLevel){
	    $.jBox.confirm("确定要撤销此审核吗？","提示",function(v,h,f){
		if(v=='ok'){
			$.ajax({
				type:"POST",
				url:"${ctx}/cost/review/cancelOp",
				data:{costRecordId:costRecordId,orderType:orderType,nowLevel:nowLevel},
					success:function(data){
                        $.jBox.tip('操作成功', 'success');
						$("#searchForm").submit();					
				   },
                    error : function(e){					  
                        top.$.jBox.tip('请求失败。','error');
                        return false;
                    }
			})
		}
	})
	}
	
		//全选&反选操作
	function checkall(obj){
	    if($(obj).attr("checked")){
	        $('#contentTable input[type="checkbox"]').attr("checked",'true');
	        $("input[name='allChk']").attr("checked",'true');
	    }else{
	        $('#contentTable input[type="checkbox"]').removeAttr("checked");
	        $("input[name='allChk']").removeAttr("checked");
	    }
	}
	
	function checkreverse(obj){
	    var $contentTable = $('#contentTable');
	    $contentTable.find('input[type="checkbox"]').each(function(){
	        var $checkbox = $(this);
	        if($checkbox.is(':checked')){
	            $checkbox.removeAttr('checked');
	        }else{
	            $checkbox.attr('checked',true);
	        }
			 $checkbox.trigger('change');
	    });
	}
	
	function batchApproval(ctx,checkBox) {
		var tmp=0;
		var count=0;
	    $("input[name='"+checkBox+"']").each(function(){ 
	    	if ($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
	           tmp=tmp +","+$(this).attr('value');
	           count++;
	    	}
	    });    
	    if(tmp=="0"){
	        alert("请选择审批记录");
	        return;
	    }
	    $('#tip').text($('#tip').text().replace('num', count));
		$.jBox($("#batch-verify-list").html(), {
			title: "批量审批", buttons: { '取消': 0,'驳回': 1, '通过': 2 }, submit: function (v, h, f) {
				var reviewComment = f.reviewComment;
				if (v == "1") {
					$.ajax({
						type:"POST",
						url:"${ctx}/cost/review/batchDeny",
						data:{code:tmp,reviewComment:reviewComment},
						success:function(data){
							$("#searchForm").submit();
						},
						error:function(){
							alert('返回数据失败');
						}
					});
				} else if (v == "2") {
					$.ajax({
						type:"POST",
						url:"${ctx}/cost/review/batchPass",
						data:{code:tmp,reviewComment:reviewComment},
						success:function(data){
							$("#searchForm").submit();
						},
						error:function(){
							alert('返回数据失败');
						}
					});
				}
			}, height: 250, width: 350
		});
		inquiryCheckBOX();
	}
		
		
/* function sortby(sortBy,obj){
    var temporderBy = $("#orderBy").val();
    if(temporderBy.match(sortBy)){
        sortBy = temporderBy;
        if(sortBy.match(/ASC/g)){
            sortBy = $.trim(sortBy.replace(/ASC/gi,""))+" DESC";
        }else{
            sortBy = $.trim(sortBy.replace(/DESC/gi,""))+" ASC";
        }
    }else{
        sortBy = sortBy+" DESC";
    }
    
    $("#orderBy").val(sortBy);
    $("#searchForm").submit();
} */		
</script>
  <style type="text/css">

.message_box{width:100%;padding:0px 0 40px 0;}
.message_box_li { width:246px;height:33px;float:left;margin:5px 20px 5px 0;}
.message_box_li_a{max-width:240px;padding:0 5px;margin-top:9px;height:24px;line-height:24px;background:#a8b9d3;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px;color:#ffffff;float:left;position:relative;cursor:pointer;}
.message_box_li .curret{background:#62afe7;}
.message_box_li_em{background:#ff3333;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px;color:#ffffff;padding:2px;height:10px;min-width:14px;line-height:10px;text-align:center;float:left;position:absolute;z-index:4;right:-12px;top:-9px;font-size:12px; }
.message_box_li_a span{float:left;}
.message_box_li_hover{width:auto;line-height:24px;color:#5f7795;font-size:12px;border:1px solid #cccccc;-moz-border-radius: 2px; -webkit-border-radius: 2px;border-radius:2px; -webkit-box-shadow:0 0 2px #b2b0b1; -moz-box-shadow:0 0 2px #b2b0b1; box-shadow:0 0 2px #b2b0b1;position:absolute;z-index:999;background:#ffffff;cursor:pointer;left:-5px;padding:0 5px;display:none; }
</style>
</head>
<body>
    <!--右侧内容部分开始-->
  <!--  <div class="rolelist_btn">
        <c:forEach items="${userJobs}" var="userJob">
            <c:if test="${userJobId == userJob.id}">
                <a class="ydbz_x" href="${ctx}/cost/review/visaList/${reviewLevel}?userJobId=${userJob.id}">${userJob.deptName}_${userJob.jobName}</a>
            </c:if> 
            <c:if test="${userJobId != userJob.id}">
                <a class="ydbz_x gray" href="${ctx}/cost/review/visaList/${reviewLevel}?userJobId=${userJob.id}">${userJob.deptName}_${userJob.jobName}</a>
            </c:if> 
        </c:forEach>
    </div>  -->

      <div class="message_box">
          <c:forEach items="${userJobs}" var="role">
            <div class="message_box_li">
              <c:choose>
                <c:when test="${userJobId==role.id}">
                      <a  href="${ctx}/cost/review/visaList/${reviewLevel}?userJobId=${role.id}">
                      <div class="message_box_li_a curret">
                <span>
                ${fns:abbrs(role.deptName,role.jobName,40)}
                </span>
                <c:if test ="${role.count gt 0}">
                  <div class="message_box_li_em" >
                  ${role.count}               
                  </div>
                </c:if>
                <c:if test="${fns:getStringLength(role.deptName,role.jobName) gt 37}">
                  <div class="message_box_li_hover">
                    ${role.deptName }_${role.jobName }
                  </div>
                </c:if>
              </div>
                      </a>
                  </c:when>
                <c:otherwise>
                  <a  href="${ctx}/cost/review/visaList/${reviewLevel}?userJobId=${role.id}">
                      <div class="message_box_li_a">
                <span>${fns:abbrs(role.deptName,role.jobName,40)}</span>
                <c:if test ="${role.count gt 0}">
                  <div class="message_box_li_em" >
                  ${role.count}         
                  </div>
                </c:if>
                <c:if test="${fns:getStringLength(role.deptName,role.jobName) gt 37}">
                  <div class="message_box_li_hover">
                    ${role.deptName }_${role.jobName }
                  </div>
                </c:if>
              </div>
                      </a>
                  </c:otherwise>
              </c:choose>
              
            </div>
          </c:forEach>  
        </div>


    <c:if test="${reviewLevel==1}">                   
      <page:applyDecorator name="cost_review_head">
      <page:param name="current">visa</page:param>
      </page:applyDecorator> 
    </c:if>
    <c:if test="${reviewLevel>=2}">                   
      <page:applyDecorator name="cost_review_manager">
      <page:param name="current">visa</page:param>
      </page:applyDecorator> 
    </c:if>
        <p class="main-right-topbutt"></p>
        <div class="activitylist_bodyer_right_team_co_bgcolor">
            <form:form id="searchForm" modelAttribute="VisaProducts" action="${ctx}/cost/review/visaList/${reviewLevel}" method="post">

                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
                <input id="visaProductIds" type="hidden" name="visaProductIds" value="${visaProductIds}"/>
                <input id="review" name="review"   type="hidden"  value="${review}">
                <input id="reviewLevel"  type="hidden"  name="reviewLevel"  value="${reviewLevel}">
                <input id="activityIds" type="hidden" name="activityIds"/>
                <input id="userJobId" name="userJobId"  type="hidden"  value="${userJobId}">  
                <div class="activitylist_bodyer_right_team_co">
                
                     <!-- 公告显示 -->
					<!-- <dl class="notice">
						<dt class="tdred tr">公告：</dt>
						<dd class="list_lh">
							<ul>
								<li>${visaPublicBulletin.content}</li>
								<li><a href="" target="_blank">test</a></li>
							</ul>
						</dd>
					</dl> -->

                    <div class="activitylist_bodyer_right_team_co2 pr wpr20">
                    	<!-- 去掉产品名称查询条件 -->
                    	<!--  
                        <label>产品名称：</label>
                        <input style="width:260px" class="txtPro inputTxt inquiry_left_text" id="productName" name="productName" value="${productName}" type="text" flag="istips" />
                        <span class="ipt-tips">仅支持产品名称的搜索</span>
                        -->
                        <label>签证国家：</label>
                        <!-- 添加签证国家/地区查询条件 -->
                        <input style="width:260px" class="txtPro inputTxt inquiry_left_text" id="sysCountryName" name="sysCountryName" value="${sysCountryName}" type="text" flag="istips" />
                    </div>
                    <div class="form_submit">
                        <input class="btn btn-primary" value="搜索" type="submit">
                    </div>
                    <a class="zksx">展开筛选</a>
                    <div class="ydxbd">
                    	<div class="activitylist_bodyer_right_team_co1">
                            <div class="activitylist_team_co3_text">地接社：</div>
                            <select  name="supplierCompanyId" id="supplierCompanyId">
                                <option value="" >所有</option>
                                <c:forEach items="${supplierList}" var="supplierlist">
                                	<option value="${supplierlist.id}" <c:if test="${supplierlist.id==supplierCompanyId}">selected="selected"</c:if>>${supplierlist.supplierName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="activitylist_bodyer_right_team_co1">
                            <div class="activitylist_team_co3_text">渠道商：</div>
                            <select name="agentId">
                                <option value="" >所有</option>
                                <c:forEach items="${agentInfo}" var="agentInfo">
                                	<option value="${agentInfo.id}" <c:if test="${agentInfo.id==agentId}">selected="selected"</c:if>>${agentInfo.agentName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="activitylist_bodyer_right_team_co3">
                            <div class="activitylist_team_co3_text">领区：</div>
                            <select name="collarZoning" id="collarZoning">
                                <option value="" >所有</option>
                                <c:forEach items="${visaDistrictList}" var="visaDistrict">
                                    <option value="${visaDistrict.key}" <c:if test="${collarZoning eq visaDistrict.key}">
                                        selected
                                    </c:if>>
                                        <c:out value="${visaDistrict.value}" />
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <!--
                        <div class="activitylist_bodyer_right_team_co1">
                            <div class="activitylist_team_co3_text">签证国家：</div>
                            <select  name="sysCountryId" id="sysCountryId">
                                <option value="" >所有</option>
                                <c:forEach items="${visaCountryList}" var="visaCountry">
                                    <option value="${visaCountry.id}" <c:if test="${sysCountryId eq visaCountry.id}">
                                        selected
                                    </c:if> >
                                        <c:out value="${visaCountry.countryName_cn}"
                                                />
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        -->
                        <div class="activitylist_bodyer_right_team_co1">
                            <div class="activitylist_team_co3_text">签证类型：</div>
                            <select  name="visaType" id="visaType">
                                <option value="" >所有</option>
                                <c:forEach items="${visaTypeList}" var="visaType">
                                    <option value="${visaType.key}"  <c:if test="${visaTypeId eq visaType.key}">
                                        selected
                                    </c:if>  >
                                        <c:out value="${visaType.value}" /></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="kong"></div>
                </div>
            </form:form>
                <div class="activitylist_bodyer_right_team_co_paixu">
                    <div class="activitylist_paixu">
                        <div class="activitylist_paixu_left">
                            <ul>
                                <li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li><li class="activitylist_paixu_left_biankuang liid"><a onclick="sortby('id',this)">创建时间</a></li>
                                <li class="activitylist_paixu_left_biankuang liupdateDate"><a onclick="sortby('updateDate',this)">更新时间</a></li>
                                <li class="activitylist_paixu_left_biankuang livisaPay"><a onClick="sortby('visaPay',this)">应收价</a></li>

                            </ul>
                        </div>
                        <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
                        <div class="kong"></div>
                    </div>
                </div>

    <div class="supplierLine">
        <a href="javascript:void(0)" onclick="changeIsRecord(11)" id="isRecord11">全部</a>
        <a href="javascript:void(0)" onclick="changeIsRecord('')" id="isRecord">待本人审核</a>
        <a href="javascript:void(0)" onclick="changeIsRecord(1)" id="isRecord1">审核中</a>
        <a href="javascript:void(0)" onclick="changeIsRecord(2)" id="isRecord2">已通过</a>
        <a href="javascript:void(0)" onclick="changeIsRecord(0)" id="isRecord0">未通过</a>
        <a href="javascript:void(0)" onclick="changeIsRecord(5)" id="isRecord5">已取消</a>
    </div>
   
        <table id="contentTable" class="table activitylist_bodyer_table">
            <thead>
            <tr>           
                <th width="4%">序号</th>
                <th width="8%">成本名称</th>   
                <th width="9%">渠道商/地接社</th>   
                <th width="7%">金额</th>  
                <th width="8%">产品名称</th> 
                <th width="6%">签证国家</th>
                <th width="6%">签证类型</th>
                <th width="6%">签证领区</th>
                <th width="7%">成本价格</th>
                <th width="7%">应收价格</th>
                <th width="8%">发布时间</th>
                <th width="8%">审核状态</th>
                <th width="5%">审核</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>


            <c:forEach items="${page.list}" var="visaProduct" varStatus="s">
                <tr id="parent1">             
                    <td class="tc">
                       <c:if test="${empty review }"><input type="checkbox" name="checkBox" id="checkBox" value="${visaProduct.id}_${reviewLevel}_${reviewCompanyId}_6" /></c:if> ${s.count}
                    </td>
                    <td class="tc">${visaProduct.name}</td>
                     <td class="tc">${visaProduct.supplyName}</td>
                     <td class="tc">${visaProduct.currencyMark} ${visaProduct.totalPrice}</td>       
                      <td class="activity_name_td">
                        <a href="javascript:void(0)"  onclick="javascript:window.open('${ctx}/visa/preorder/visaProductsDetail/${visaProduct.visaId}')">
                        ${visaProduct.productName}</a>
                    </td>
                    <td>
                        <c:forEach items="${visaCountryList}" var="country">
                            <c:if test="${country.id eq visaProduct.sysCountryId}">
                            ${country.countryName_cn}
                            </c:if>
                        </c:forEach> </td>
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
                    <td class="tr">
                    <c:forEach items="${curlist}" var="currency">
                            <c:if test="${currency.id eq visaProduct.currencyId}">
                                ${currency.currencyMark}
                            </c:if>
                        </c:forEach><font color="red"><fmt:formatNumber pattern="#.00" value="${visaProduct.visaPrice}" /></font> </span>起
                    </td>   
                    <td class="tr">
                    <c:forEach items="${curlist}" var="currency">
                            <c:if test="${currency.id eq visaProduct.currencyId}">
                                ${currency.currencyMark}
                            </c:if>
                        </c:forEach><font color="green"><fmt:formatNumber pattern="#.00" value="${visaProduct.visaPay}" /></font> </span>起
                    </td>                   
                    <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${visaProduct.updateDate }"/></td>
                     <td class="tc">                    
                     ${fns:getNextCostReview(visaProduct.id)}              
                </td>
                <td class="tc">
                    <c:if test="${ visaProduct.nowLevel == reviewLevel && visaProduct.review==1 }"> 
                              <a  href="${ctx}/cost/review/visa/${visaProduct.visaId}/${reviewLevel}/${reviewCompanyId}" target="_blank">审核</a>

                            </c:if> 
       <c:if test="${fns:getUser().id eq visaProduct.updateBy.id
					&& visaProduct.review eq 1 && visaProduct.nowLevel ne  1 && visaProduct.nowLevel-1 eq reviewLevel}">   
		<a href="#" onclick="cancelOp('${visaProduct.id }','6','${visaProduct.nowLevel }')">撤销</a> 
			</c:if>
                  
                </td>
                    <td class="p0">                                             
                              <a  href="${ctx}/cost/review/visaRead/${visaProduct.visaId}/${reviewLevel}" target="_blank">查看</a>&nbsp;
						      <a href="${ctx }/cost/manager/forcastList/${visaProduct.visaId}/6" target="_blank">预报单</a>&nbsp;
						      <a href="${ctx }/cost/manager/settleList/${visaProduct.visaId}/6" target="_blank">结算单</a>  
                         </td>
                  </tr>
            </c:forEach>
            </tbody>
        </table>
     	<div class="page">
     		<c:if test="${empty review }">
				<div class="pagination">
					<dl>
						<dt>
							<input name="allChk" onclick="checkall(this)" type="checkbox">
							全选
						</dt>
						<dt>
							<input name="reverseChk" onclick="checkreverse(this)"
								type="checkbox"> 反选
						</dt>
						<dd>
							<a onclick="batchApproval('${ctx }','checkBox');">批量审批</a>
						</dd>
					</dl>
				</div>
			</c:if>
			<div class="pagination">
			
                <div class="endPage">
                    ${page}
                </div>
                <div style="clear:both;"></div>
            </div>
        </div>
    </div>
	<div class="batch-verify-list" id="batch-verify-list"
		style="padding:20px;">
		<table width="100%"
			style="padding:10px !important; border-collapse: separate;">
			<tr>
				<td></td>
			</tr>
			<tr>
				<td><p id="tip">您好，当前您提交了num个审核项目，是否执行批量操作？</p>
				</td>
			</tr>
			<tr>
				<td><p>备注：</p>
				</td>
			</tr>
			<tr>
				<td><label> <textarea name="reviewComment"
							id="reviewComment" style="width: 290px;"></textarea> </label>
				</td>
			</tr>
		</table>
	</div>
</body>
</html></html>