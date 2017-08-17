<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单-签证-面签通知</title>

<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link rel="stylesheet" href="${ctxStatic }/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic }/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactor2.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/i18n/jquery-ui-i18n.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-other/jquery-combobox_prop.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/jquery.disabled.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/jquery.date.format.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/jquery.file.filter.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/modules/activity/activity.js"></script>
<script type="text/javascript" src="${ctxStatic}/modules/visa/visaProductsUpload.js" ></script>
<script type="text/javascript">
$(function(){
	//操作浮框
	operateHandler();
	$(".bgMainRight").on("hover",".handle",function(){
		if(0 != $(this).find('a').length){
			$(this).addClass('handle-on');
			$(this).find('dd').addClass('block');
		}
	});
	$(".bgMainRight").on("mouseleave",".handle",function(){
		$(this).removeClass('handle-on');
		$(this).find('dd').removeClass('block');
	});
});
//0214------附件上传函数-s//

 function uploadFiles(ctx, inputId, obj) {
	var fls=flashChecker();
	var s="";
	if(fls.f) {
//		alert("您安装了flash,当前flash版本为: "+fls.v+".x");
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
				/*添加<li>之前，先将之前的<li>删除，然后再累加，以防止重复累加问题*/
//				if($(obj).attr("name") != 'costagreement'){
//					$(obj).next(".batch-ol").find("li").remove();
//				}
				
				$(obj).parent().find("#currentFiles").find("[name='docOriName']").each(function(index, obj1) {
					//如果是产品行程介绍
					if($(obj).attr("name") == 'introduction') {
						$(obj).next().next("#introductionVaildator").val("true").trigger("blur");
					}
					//如果是签证资料的文件上传
					if($(obj).attr("name").indexOf("signmaterial") >= 0) {
						$(obj).parent().parent().parent().parent().next(".batch-ol").append('<li><span>'+ $(obj1).val() +'</span><a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(\''+ $(obj1).prev().val() +'\',\''+ $(obj).attr("name") +'\',this);">删除</a></li>');
					}else{
						//0214-这里将文件名称变为链接的形式
						$(obj).next(".batch-ol").append('<li><a onclick="downloads(\''+$(obj1).prev().val()+'\',\''+$(obj1).val()+'\',null,false)"><span>'+ $(obj1).val() +'</span></a><a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(\''+ $(obj1).prev().val() +'\',\''+ $(obj).attr("name") +'\',this);" title="删除">X</a></li>');
					}
				});
				if($(obj).parent().find("#currentFiles").children().length != 0) {
					$(obj).parent().find("#currentFiles").children().remove();
				}
			}
			
			$(".clickBtn",window.parent.document).removeClass("clickBtn");
   		}
	});
	$("#jbox-content").css("overflow", "hidden");
	$(".jbox-close").hide();
}
//0214------附件上传函数-e//
//0214------附件下载函数-s//
function downloads(docid,activitySerNum,acitivityName,iszip){
	    	if(iszip){
	    		var zipname = activitySerNum;
	    		window.open("${ctx}/sys/docinfo/zipdownload/"+docid+"/"+zipname);
	    	}
	    		
	    	else
	    		window.open("${ctx}/sys/docinfo/download/"+docid);
	    }
//0214------附件下载函数-e//

</script>
<script type="text/javascript">
//0214----附件提交保存函数-s//
function saveAttachedFile(obj){
	//拿到订单id
	var visaOrderId='${visaOrderId}';
	//alert(visaOrderId);
  //附件的docinfo id
  var docinfoids=[];
  $("[name='otherfile']:hidden").each(function(i,n){
  	docinfoids[i]=$(n).val();
  });
  //debugger;
  //发送ajax请求将附件和订单的关系保存到表visa_order_file表中
 $.ajax({
	   url:"${ctx}/visa/interviewNotice/saveAttachedFile",
	   type:"post",
	   async:false,
	   data:{
		   "visaOrderId":visaOrderId,
		   "docinfoids":docinfoids.toString()
	   },
	   success:function(msg){
	      if(msg){
	    	  top.$.jBox.info("操作成功!", "提示信息"); 
	      }else{
	    	  top.$.jBox.info("操作失败!", "警告"); 
	      }
	   }
 });
}
//0214----附件提交保存函数-e//

</script>
    <style>
        .main-right-topbutt a {
            top:-10px;
        }
    </style>
</head>
<body>
<p class="main-right-topbutt"><a href="${ctx }/visa/interviewNotice/toCreate?orderId=${param.orderId }">新建面签通知</a></p>
<div class="ydbz_tit">
	面签通知管理
    		</div>

            <table id="contentTable" class="table activitylist_bodyer_table">
                <thead>
                    <tr>
                        <th width="10%">新建时间</th>
                        <th width="10%">约签国家</th>
                        <th width="10%">领区</th>
                         <th width="10%">预约地点</th>
                        <th width="10%">约签时间</th>
                        <th width="10%">说明会时间</th>
                        <th width="10%">联系人</th>
                        <th width="10%">联系电话</th>
                        <th width="10%">办签人数</th>
			            <th width="10%">操作</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${list}" var="item">
                	<tr id="parent1" >
                        <td class="tc"><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd"/></td>
						<td class="tc">${item.country }</td>
						<td class="tc">${item.area }</td>
						<td class="tc">${item.address }</td>
						<td class="tc"><fmt:formatDate value="${item.interviewTime }" pattern="yyyy-MM-dd HH:mm"/></td>
						<td class="tc"><fmt:formatDate value="${item.explainationTime }" pattern="yyyy-MM-dd HH:mm"/></td>
						<td class="tc">${item.contactMan }</td>
						<td class="tc">${item.contactWay }</td>
						<td class="tc">${item.num }</td>
                        <td class="p0">
                            <dl class="handle">
                                <dt><img title="操作" src="${ctxStatic }/images/handle_cz.png"></dt>
                                <dd>
						<p>
							<span></span>
							<a href="javascript:void(0)" id="switcher-${item.id }">展开</a>
							<a id="preview-${item.id }" href="javascript:void(0)">预览</a>
							<a id="edit-${item.id }" href="javascript:void(0)">编辑</a>
							<a id="del-${item.id }" href="javascript:void(0)">删除</a>
						</p>
                                </dd>
                            </dl>
                        </td>   
                    </tr>
		<tr class="activity_team_top1" id="layer-${item.id }" style="display: none;">
			<td class="team_top" colspan="15">
                <table style="margin:0 auto;" class="table activitylist_bodyer_table" id="teamTable_${item.id }" >
                   	<thead>
                           <tr>
                               <th width="10%">姓名</th>
                               <th width="10%">护照号</th>
                               <th width="10%">AA码</th>
                               <th width="10%">签证类别</th>
                               <th width="10%">签证国家</th>
                               <th width="10%">护照状态</th>
                               <th width="10%">担保</th>
                               <th width="10%">应收金额</th>		
							   <th width="10%">应收押金<br />达账押金</th>
							   <th width="10%">下载</th>												
                           </tr>
                       </thead>
                       <tbody></tbody>
				</table>
			</td>
		</tr>
		</c:forEach>
                </tbody>
            </table>
        	<!-- <div class="page">
                <div class="pagination">
                    <div class="endPage"><ul>
                        <li class="disabled"><a href="javascript:">« 上一页</a></li>
                        <li class="active"><a href="javascript:">1</a></li>
                        <li><a href="javascript:page(2,10);">2</a></li>
                        <li><a href="javascript:page(3,10);">3</a></li>
                        <li><a href="javascript:page(4,10);">4</a></li>
                        <li><a href="javascript:page(5,10);">5</a></li>
                        <li><a href="javascript:page(6,10);">6</a></li>
                        <li><a href="javascript:page(7,10);">7</a></li>
                        <li><a href="javascript:page(8,10);">8</a></li>
                        <li class="disabled"><a href="javascript:">...</a></li>
                        <li><a href="javascript:page(28,10);">28</a></li>
                        <li><a href="javascript:page(2,10);">下一页 »</a></li>
                        <li class="disabled controls"><a href="javascript:">当前 <input value="1" onkeypress="var e=window.event||event||this;var c=e.keyCode||e.which;if(c==13)page(this.value,10);" onclick="this.select();" type="text"> / <input value="10" onkeypress="var e=window.event||event||this;var c=e.keyCode||e.which;if(c==13)page(1,this.value);" onclick="this.select();" type="text"> 条，共 272 条</a></li>
                    </ul>
                    <div style="clear:both;"></div></div>
                    <div style="clear:both;"></div>
                </div>	
			</div> -->

<!-- 0214新增面签通知附件上传-s -->
<div>
                    <!--0214-qyl-begin  -->
                    <%--  <div class="mod_information_3">
                	 附件：
                	 <input name="otherfileAttached" type="text"  style="display:none;" disabled="disabled">
                	 <input type="button" name="otherfile" value="上传" class="mod_infoinformation3_file" onClick="uploadFiles('${ctx}',null,this)"/>
                	 <input type="button" name="otherfile" value="上传" class="mod_infoinformation3_file" onClick="uploadFiles('${ctx}','otherfileAttached',this,'false')"/>
                     <ol class="batch-ol" style="margin-left:40px;"></ol>
                     </div> --%>
                    <!-- tgy-s --> 
                    <div class="mod_information_3">
                                                            附件：
	               	<input type="button" name="otherfile" value="上传" class="mod_infoinformation3_file" onClick="uploadFiles('${ctx}',null,this)"/>
	               	<ol class="batch-ol" style="margin-left:40px;">
						<c:forEach items="${docInfoList}" var="docInfo">
							<li>
							    <input type="hidden" value="${docInfo.id}" name="otherfile"> 
								<a onclick="downloads(${docInfo.id},'','',false)"><span>${docInfo.docName}</span></a>
								<a class="batchDel" onclick="deleteFileInfo(null,null,this);" href="javascript:void(0)" title="删除">X</a>
							</li>
						</c:forEach>
					</ol>
				  </div>
                   <!-- tgy-e -->   
                    <div class="mod_information_3">
                      <input type="button" value="附件提交保存" onclick="saveAttachedFile(this)" class="btn btn-primary">
                    </div>
</div>
<!-- 0214新增面签通知附件上传-e -->
<script type="text/javascript">
$(function(){
	$("a[id^=del-]").click(function(){
		var id=this.id.split("-")[1];
		del(id);
	})
	$("a[id^=edit-]").click(function(){
		var id=this.id.split("-")[1];
		edit(id);
	})
	$("a[id^=preview-]").click(function(){
		var id=this.id.split("-")[1];
		preview(id);
	})
	$("a[id^=switcher-]").click(function(){
		var id=this.id.split("-")[1];
		switcher(id);
	})
	//$("#teamTable tbody").children().remove();
	//$("#teamTable tbody").append('<tr><td>张三</td><td>HOU059234<br /><span class="tdred">即将过期</span></td><td>AA3453425</td><td>个签</td><td>美国</td><td class="tc">已签收</td><td>担保</td><td class="p0 tr"></tr>');
})
var del=function(id){
	top.$.jBox.confirm('确定要删除吗？','系统提示',function(v){
        if(v=='ok'){
            $.ajax({
            	url:"${ctx}/visa/interviewNotice/delete",
            	data:{
            		id:id
            	},
            	dataType:"json",
            	success:function(response){
            		window.location.reload();
            		return false;
            	}
            })
        }
        },{buttonsFocus:1}
    );
}
var edit=function(id){
	window.location.href="${ctx}/visa/interviewNotice/toUpdate?interviewId="+id+"&orderId=${param.orderId }";
	return false;
}
var preview=function(id){
	window.location.href="${ctx}/visa/interviewNotice/preview?interviewId="+id;
	return false;
}
var switcher=function(id){
	var path = '${ctxStatic}';
	var layer=$("#layer-"+id);
	var target=$("#switcher-"+id);
	if(layer.css("display")=="none"){
		$.ajax({
			type:"post",
			url:"${ctx}/visa/interviewNotice/travelerList",
			dataType:"json",
			data:{
				interviewId:id
			},
			async:false,
			success:function(response){
				var o;
				var tbody=$("#teamTable_"+id+" tbody");
				tbody.children().remove();
				for(var i=0;i<response.length;i++){
					o=response[i];
					var downloadpath = "${ctx}/visa/order/downloadInterviewNotice?orderId="+o.orderId+"&travelerId="+o.id;
					var r='<tr>';
					r+='<td class="tc">'+o.name+'</td>';
					r+='<td class="tc">'+(o.passportCode==null?"":o.passportCode)+'</td>';
					r+='<td class="tc">'+(o.aaCode==null?"":o.aaCode)+'</td>';
					r+='<td class="tc">'+o.visaType+'</td>';
					r+='<td class="tc">'+o.country+'</td>';
					r+='<td class="tc">'+o.passportStatus+'</td>';
					r+='<td class="tc">'+o.guaranteeStatus+'</td>';
					r+='<td class="tc"><span style="color:red">'+o.payPrice+'</span></td>';
					r+='<td class="p0 tr"><div class="yfje_dd"><span class="fbold">'+o.totalDeposit+'</span></div><div class="dzje_dd"><span class="fbold">'+o.accountedDeposit+'</span></div></td>';
					r+='<td class="p0"><dl class="handle"><dt><img title="下载" src="'+path+'/images/handle_xz.png"></dt><dd><p><span></span><a  href="'+downloadpath+'">个人下载</a></p></dd></dl></td>';
					r+='</tr>';
					tbody.append(r);
				}
			}
		})
		target.html("收起");
		layer.show();
		target.parents("td").addClass("td-extend").parent("tr").addClass("tr-hover");
	}else{
		target.html("展开");
		layer.hide();
		target.parents("td").removeClass("td-extend").parent("tr").removeClass("tr-hover");
	}
}
//展开、关闭航程
function expand(child,obj,srcActivityId){
	if($(child).css("display")=="none"){
			$(obj).html("收起");
			$(child).show();
			$(obj).parents("td").addClass("td-extend").parent("tr").addClass("tr-hover");
		//}
	}else{
		$(child).hide();
		$(obj).parents("td").removeClass("td-extend").parent("tr").removeClass("tr-hover");
		$(obj).html("展开");
	}
}
</script>
</body>
</html>