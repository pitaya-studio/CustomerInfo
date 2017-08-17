<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>基础信息-游轮-列表页</title>
    <meta name="decorator" content="wholesaler"/>
   <!--  <link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon"/> -->
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/modules/basic-info/cruise/cruiseBaseInfo.js"></script>
<script type="text/javascript">

$(function(){
	//搜索条件筛选
	launch();
	//产品名称文本框提示信息
	inputTips();
	//操作浮框
	operateHandler();
	//取消一个checkbox 就要联动取消 全选的选中状态
	$("input[name='ids']").click(function(){
		
		if($("input[name='ids']:checked").length==$("input[name='ids']").length){
			$("input[name='checkAll']").attr("checked",true);
		}else{
			$("input[name='checkAll']").removeAttr("checked",false);
		}
	});
	var _$orderBy = $("#orderBy").val();
	 var orderBy = _$orderBy.split(" ");
     $(".activitylist_paixu_left li").each(function(){
         if ($(this).hasClass("activitylist_paixu_moren")){
            orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
            $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
             $(this).attr("class","activitylist_paixu_moren");
         }
     });
});
function show(uuid){
	window.open("${ctx}/cruiseshipInfo/show/" + uuid );
}
function edit(uuid){
	window.open( "${ctx}/cruiseshipInfo/edit/" + uuid );
}
function del(uuid){
	console.log(uuid)
	var ids = [];
	ids.push(uuid);
	v_deleteItems(ids);
}
function checkall(obj){
	if($(obj).attr("checked")){
		$("input[name='ids']").attr("checked",'true');
		$("input[name='allChoose']").attr("checked",'true');
	}else{
		$("input[name='ids']").removeAttr("checked");
		$("input[name='allChoose']").removeAttr("checked");
	}
}

function alldel(){
	if($("[name=ids]:checkbox:checked").length>0){
		var ids = [];
		$("[name=ids]:checkbox:checked").each(function(){ids.push($(this).val())});
		v_deleteItems(ids);
	}else{
		top.$.jBox.tip('请选择后进行删除操作','warning');
	}
}

function v_deleteItems(ids){
	$.jBox.confirm("确定要删除数据吗？", "系统提示", function(v, h, f) {
		if (v == 'ok') {
			$.jBox.tip("正在删除数据...", 'loading');
			//$.post( //"${ctx}/cruiseshipInfo/delete", {"uuids":ids.join(",")}, 
				//function(data){
				//	if(data.result=="1"){
						//top.$.jBox.tip('删除成功!');
						//$("#searchForm").submit();
						$.ajax({
							type:"post",
							url:"${ctx}/cruiseshipInfo/delete",
							data:{
								uuids:ids.join(",")
							},
							success:function(object){
								if(object.result=="1"){
									top.$.jBox.tip('删除成功!');
									$("#searchForm").submit();
								}else if(object=="2"){
									top.$.jBox.tip('删除失败,数据有关联信息!','warning');
								}else{
									top.$.jBox.tip('删除失败,数据有关联信息!','warning');
								}
							}
						});
				//	}else{
					//	top.$.jBox.tip('删除失败','warning');
					//}
			//	}
		//	);
			
		} else if (v == 'cancel') {
                
		}
	});
	
}
function page(n,s){
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").attr("action","${ctx}/cruiseshipInfo/list");
	$("#searchForm").submit();
}
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
            $("#searchForm").submit();
  }
function query(){
	$("#searchForm").submit();
}
function updateForm(uuid){
	$.ajax({
		type:"post",
		url:"${ctx}/cruiseshipInfo/updateCheck",
		data:{
			uuid:uuid
		},
		success:function(data){
			if(data){
				$.jBox.confirm("已关联库存，如果修改则同时影响库存信息，是否确认？", "系统提示", function(v, h, f) {
					if(v=='ok'){
						location.href="${ctx}/cruiseshipInfo/show/"+uuid;
					}else if(v=='cancle'){
					
					}
				});		
			}else{
				location.href="${ctx}/cruiseshipInfo/show/"+uuid;
			}
		}
	});
}
</script>	
</head>
<body>
    <div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
        <form id="searchForm" action="${ctx}/cruiseshipInfo/list" method="post">
            <div class="activitylist_bodyer_right_team_co">
                <div class="activitylist_bodyer_right_team_co2 wpr20 pr">
                    <input id="cruises" name="name" class="inputTxt inputTxtlong"
                           onkeyup="this.value=this.value.replaceColonChars()" onafterpaste="this.value=this.value.replaceColonChars()"
                           placeholder="仅支持游轮名称模糊搜索" value="${name }">
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize"  type="hidden" value="${page.pageSize}"/>
					<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>  
                </div>
                <div class="form_submit">
                    <input id="btn_search" class="btn btn-primary ydbz_x" type="button" onclick="query(0)" value="搜索">
                </div>
                <p class="main-right-topbutt"><a href="${ctx}/cruiseshipInfo/form" target="_blank">新增游轮</a></p>
            </div>
        </form>
        <div class="activitylist_paixu activitylist_bodyer_right_team_co_paixu">
            <div class="activitylist_paixu_left">
                <ul>
                    <li class="activitylist_paixu_moren"><a onclick="sortby('createDate',this)">创建日期</a></li>
                </ul>
            </div>
            <div class="kong"></div>
        </div>
        <table id="contentTable" class="activitylist_bodyer_table mainTable">
            <thead>
                <th>序号</th>
                <th>游轮名称</th>
                <th>创建日期</th>
                <th>创建人</th>
                <th>操作</th>
            </thead>
            <tbody>
            <c:forEach items="${page.list }" var="cr" varStatus="s">
                <tr>
                    <td class="tc" name="index"><input type="checkbox" name="ids" value="${cr.uuid }"/>${s.count }</td>
                    <td class="tc">${cr.name }</td>
                    <td class="tc"><f:formatDate value="${cr.createDate }" pattern="yyyy-MM-dd" /></td>
                    <td class="tc"><trekiz:autoId2Name4Table tableName="sys_user" sourceColumnName="id" srcColumnName="name" value="${cr.createBy }"/></td>
                    <td class="tc"><a name="detail" href="${ctx }/cruiseshipInfo/edit/${cr.uuid}" target="_blank">详情</a>&nbsp;&nbsp;<a name="modify"  onclick="updateForm('${cr.uuid}')"  target="_blank">修改</a>&nbsp;&nbsp;<a name="delete" onclick="del('${cr.uuid}')">删除</a></td>
                </tr>
              </c:forEach>  
                <tr>
                    <td colspan="5">
                        <input type="checkbox" name="checkAll"/>全选
                        <input type="button" class="btn btn-default" style="height: 26px;" onclick="alldel()"  value="批量删除"/>
                    </td>
                </tr>
            </tbody>
        </table>

        <div class="page">
				<div class="pagination">
						<div class="endPage">${page }</div>
				</div>
		</div>
    </div>
</body>
</html>
<script type="text/javascript">


</script>
