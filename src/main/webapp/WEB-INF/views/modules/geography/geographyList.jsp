<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler"/>
<title>基础信息维护-行政区域管理</title>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/treeTable/themes/vsStyle/treeTable.min.css" />
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/trekiz.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/treeTable/jquery.treeTable.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
$(function(){
	g_context_url = "${ctx}";
	
	
	if($("#ifSearch").val()!="true"){
    	$("#treeTable").treeTable({expandLevel : 1,beforeExpand:function($treeTable, id){
			    if ($('.' + id, $treeTable).length) { return; }
			    var labelVal = $("#labelVal").val();
				if((labelVal=="guonei"&&id!=1&&id!=2)||(labelVal=="guoji")){
					onExpand($treeTable,id,labelVal);
				}
			},
			 onSelect : function($treeTable, id) {
	          //   window.console && console.log('onSelect:' + id);
	             
	         }
			}	
	    );
	}
	 
});
 
function onExpand($treeTable,id,labelVal){
	 $.ajax({                 
			cache: true,                 
			type: "POST",                 
			url:g_context_url+ "/geography/searchChild",                 
			data:{ 
				"id":id
				},                
			 async: false,                 
			 success: function(result){
			 var lable = $("#labelVal").val();
			 var sortType = $("#sortUpdateType").val();
			 var  isReadOnly='readonly="readonly"';
			 if(sortType=="update"){
				 isReadOnly='';
			 }
			 var html ='';
			  for(var i = 0; i<result.length; i++){ 
				    var obj = result[i];
			  		if(labelVal=="guoji"&&obj.id==2){
			  			continue;
			  		}
				    if(obj.isLast!=null){ 
						html +=' <tr id="'+obj.id+'" pId="'+obj.parentId+'" hasChild="false">';
					}
				    else{
						html +=' <tr id="'+obj.id+'" pId="'+obj.parentId+'" >';
				    }
					html +=' <td id="top_'+obj.id+'">';
					html +=obj.nameCn+'【ID：'+obj.id+'】</td>';
					html +=' <td>';
					html +=' 	<a href="'+g_context_url+'/geography/saveList?parentId='+obj.id+'&level='+obj.level+'&lable='+lable+'" target="_self">添加子类</a>&#12288;';
					html +='    <a href="'+g_context_url+'/geography/modifyGeography?id='+obj.id+'&level='+obj.level+'&parentId='+obj.parentId+'&lable='+lable+'" target="_self">修改</a>&#12288;';
					html +='    <a onclick="del('+obj.id+')">删除</a>';
					html +=' </td>';
					html +='  <td class="tdSequence"><input type="text" value="'+obj.sort+'"  geoId="'+obj.id+'" name="sort" '+isReadOnly+' onblur='+"this.value=this.value.replace(/\\D/g,'');"+' onkeyup='+"this.value=this.value.replace(/\\D/g,'');"+' onafterpaste='+"this.value=this.value.replace(/\\D/g,'');"+'  maxlength="4"  '+'/></td>';
					html +='  <input id="hidden_'+obj.id+'"  type="hidden" value="'+obj.sort+'" name="hiddenSort" />';
					html +='  <input  type="hidden" value="'+obj.id+'" name="hiddenId" />';
					html +='  <input id="level" name="level" type="hidden" value="'+obj.level+'"/>';
					html +='</tr>';	 
	        } 
					$treeTable.addChilds(html);	
			}
		});
}
function checkCondition(){
	var conditionKey = $("#wholeSalerKey").val();
	if(conditionKey==null||$.trim(conditionKey)==""){
		window.location.href=g_context_url+"/geography/list?lable="+$("#labelVal").val();
	}else{
		$("#searchForm").submit();
	}
}


function del(id){
	if(!confirm("要删除该区域及所有子区域项吗？")){
		return false;
	}
	
	 $.ajax({                 
			cache: true,                 
			type: "POST",                 
			url:g_context_url+ "/geography/deleteGeography",                 
			data:{ 
				"id":id
				},                
			 async: false,                 
			 success: function(result){
			 if(result!=null&&result!=""){
				 top.$.jBox.tip(result);
				 return false;
			 }
			 top.$.jBox.tip("删除成功");
			 window.location.href=g_context_url+"/geography/list?lable="+$("#labelVal").val();
		}
	 });
}
function updatesort(){
	$("#saveid1").css("display","");
	$("#updateid1").css("display","none");
	$("#saveid2").css("display","");
	$("#updateid2").css("display","none");
	$("input[name='sort']").removeAttr("readonly");
	$("#sortUpdateType").val("update");
}
function savesort(){
	if(!confirm("确定要保存更改？")){
		return false;
	}
	$.ajax({                 
		cache: true,                 
		type: "POST",                 
		url:g_context_url+ "/geography/saveSort",                 
		dataType : "text",	
		data: $("#sortForm").serialize(),
		 async: false,                 
		 success: function(msg){
			 
		  if(msg!=null&&msg!=""){
			 top.$.jBox.tip(msg);
			 return false;
		 }  
		 top.$.jBox.tip("保存成功");
		 window.location.href=g_context_url+"/geography/list?lable="+$("#labelVal").val();
	}
 });
}
</script>
</head>
<body>
    <input type="hidden" id="ifSearch"   value="${ifSearch}"/>
    <page:applyDecorator name="sys_geography" >
        <page:param name="current">${lable}</page:param>
    </page:applyDecorator>
            	<!--右侧内容部分开始-->
    <form action="${ctx}/geography/search" method="post"  id="searchForm"  >
       <input type="hidden" id="labelVal" name="lable" value="${lable}"/><%--lable为区分国内和国际 --%>
       <input type="hidden" id="sortUpdateType"  value="notUpdate"/>
        <div class="activitylist_bodyer_right_team_co">
            <div class="activitylist_bodyer_right_team_co2">
                <input class="txtPro inputTxt radius4 " id="wholeSalerKey" name="wholeSalerKey" value="${wholeSalerKey}" type="text" placeholder="输入区域名称"/>
            </div>
            <div class="form_submit"><input class="btn btn-primary"  value="搜索" type="button" onclick="checkCondition()"></div>
            <p class="main-right-topbutt">
                <a class="primary" href="${ctx}/geography/addTopList?lable=${lable}" target="_self">添加顶级区域</a>
                <a href="javascript:void(0)" class="updateSeq" style="display:none" id="saveid1" onclick="savesort()" target="_self">保存更新</a>
                <a href="javascript:void(0)" class="updateSeq topButton_common" id="updateid1" onclick="updatesort()" target="_self">更新排序</a>
                <%--<input type="button" class="btn updateSeq" style="display:none" id="saveid1" onclick="savesort()" value="保存更新">--%>
                <%--<input type="button" class="btn updateSeq" id="updateid1" onclick="updatesort()" value="更新排序">--%>
            </p>
        </div>

    </form>
     <%--<div style="width:100%; height:35px;"></div>--%>
     <%--<div class="updateSeq" id="updateid1"><a onclick="updatesort()" class="ydbz_x">更新排序</a></div>--%>
    <form  id="sortForm">
        <table id="treeTable" class="table table-striped table-bordered" style="margin-bottom:10px;">
            <tr>
                <th>行政区域名称</th>
                <th width="18%">操作</th>
                <th width="8%">排序</th>
            </tr>
           <c:forEach var="entry" items="${list}" varStatus="v">
            <c:if test="${lable=='guonei'}">
             <tr id="${entry[0]}" pId="<c:if test="${entry[2]==2}">0</c:if>" <c:if test="${entry[4]!=null&&entry[4]!=''}">hasChild="true"</c:if>>
            </c:if>
             <c:if test="${lable=='guoji'}">
                    <tr id="${entry[0]}" pId="${entry[2]}" <c:if test="${entry[4]!=null&&entry[4]!=''}"> hasChild="true"</c:if>>
             </c:if>

                <td id="top_${entry[0]}">${entry[1]}【ID：${entry[0]}】</td>
                <input id="level" name="level" type="hidden" value="${entry[5]}"/>
                <td>
                    <a href="${ctx}/geography/saveList?parentId=${entry[0]}&lable=${lable}&level=${entry[5]}" target="_self">添加子类</a>&#12288;
                    <a href="${ctx}/geography/modifyGeography?id=${entry[0]}&lable=${lable}&level=${entry[5]}&parentId=${entry[2]}" target="_self">修改</a>&#12288;
                    <a onclick="del(${entry[0]})">删除</a>
                </td>
                <td class="tdSequence"><input name="sort"  type="text" value="${entry[3]}" readonly="readonly" onblur="this.value=this.value.replace(/\D/g,'');"   onkeyup="this.value=this.value.replace(/\D/g,'');"  onafterpaste="this.value=this.value.replace(/\D/g,'');" maxlength="4"/></td>
               <input id="hidden_${entry[0]}" value="${entry[3]}" name="hiddenSort" type="hidden"/>
               <input  value="${entry[0]}" name="hiddenId" type="hidden"/>
            </tr>
          </c:forEach>

        </table>
            	 <%--<div class="updateSeq" style="display:none" id="saveid2"><a onclick="savesort()" class="ydbz_x">保存更新</a></div>--%>
                 <%--<div class="updateSeq" id="updateid2"><a onclick="updatesort()" class="ydbz_x">更新排序</a></div>--%>
         <p class="main-right-topbutt">
             <a href="javascript:void(0)" class="updateSeq" style="display:none" id="saveid2" onclick="savesort()" target="_self">保存更新</a>
             <a href="javascript:void(0)" class="updateSeq" id="updateid2" onclick="updatesort()" target="_self">更新排序</a>
         </p>
    </form>
                <!--右侧内容部分结束-->
</body>
</html>