<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <title>库存-库存修改页</title>
    <!--[if lte IE 6]>
    <link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css"/>
    <script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
    <![endif]-->
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css">
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
        .label-style {
	        vertical-align:middle;
	        margin:0;
        }
    </style>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/modules/stock/stock-list.js"></script>
    <script type="text/javascript">
        $(function () {
            //操作浮框
            operateHandler();
            $(".uiPrint").hover(function () {
                $(this).find("span").show();
            }, function () {
                $(this).find("span").hide();
            });

            $('.team_a_click').toggle(function () {
                $(this).addClass('team_a_click2')
            }, function () {
                $(this).removeClass('team_a_click2')
            });
            $ctx = "${ctx}";
        });
    </script>
    <script type="text/javascript">
    var isOk="${isOk}";
        function reserve() {
            var $popReserve = $.jBox($('#popReserve').html(),
                    {
                        title: "渠道和付款方式选择：",
                        buttons: {}
                    }
            );
            $popReserve.on('click', '#btnNext', function () {
                var status = $popReserve.data("status");
                if (status == 'payment') {
                    $popReserve.data('status', 'orderType');
                    $popReserve.trigger('pop.status.change');
                } else {
                    //@todo 需要开发继续处理
                }
            });
            $popReserve.on('click', '#btnPre', function () {
                var status = $popReserve.data("status");
                if (status == 'payment') {
                    $.jBox.close();
                } else {
                    $popReserve.data('status', 'payment');
                    $popReserve.trigger('pop.status.change');
                }
            });
            $popReserve.on('pop.status.change', function () {
                var status = $popReserve.data("status");
                var $btnPre = $popReserve.find('#btnPre');
                var $reservePayment = $popReserve.find("#reservePayment");
                var $reserveOrderType = $popReserve.find("#reserveOrderType");
                if (status == 'payment') {
                    $btnPre.text('取消');
                    $popReserve.find('.jbox-title').text('渠道和付款方式选择');
                    $reservePayment.show();
                    $reserveOrderType.hide();
                } else {
                    $btnPre.text('返回');
                    $popReserve.find('.jbox-title').text('订单类型选择');
                    $reservePayment.hide();
                    $reserveOrderType.show();
                }
            });
            $popReserve.data('status', 'payment');
        }
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
	</script>
	<script type="text/javascript">
	//删除方法
	function deleteOneStock(stockdetailuuid,obj){
		var flag = $("#relFlag").val();
		var $this=$(obj);
		if(flag=="true"){
			$.jBox.tip('该舱型已关联产品，无法删除!','warning');
		}else{
			top.$.jBox.confirm('确认要删除么？','系统提示',function(v){
				if(v=="ok"){
					$.ajax({
				        url:$ctx+"/cruiseshipStock/deleteStockDetailByUuid",
				        type:'post',
				        cache:false,
				        async:false,
				        data:{"stockdetailuuid":stockdetailuuid} ,
				        success:function(data){
				        	if(data=="1"){
				        		$.jBox.tip("删除成功!");
				        		setTimeout(function(){window.reload();},900);
				        		$this.parent().parent().parent().parent().parent().remove();
				        	}else{
				        		$.jBox.tip('系统异常，请重新操作!','warning');
				        	}
				        }
				    });
				}
			});
		}
    }
	//保存方法
	function editStockDetail(obj){
		//按钮禁用
		$(obj).attr("disabled", "disabled");
		$.ajax({
	        url:$ctx+"/cruiseshipStock/editStockDetail",
	        type:'post',
	        cache:false,
	        async:false,
	        data:$("#addForm").serialize(),
	        success:function(data){
	        	if(data=="1"){
	        		$.jBox.tip("修改成功!");
	        		setTimeout(function(){window.close();},900);
	        		window.opener.location.reload();
	        	}else{
	        		$.jBox.tip('系统异常，请重新操作!','warning');
	        		$(obj).attr("disabled", false);
	        	}
	        }
	    });
    }
	 function Jilu(uuid,stockUuid,cruiseshipInfoUuid,shipDate){
     	$.ajax({
     		type:"post",
     		url:$ctx+"/cruiseshipStock/stockUpdateList",
     		data:{
     			"uuid":uuid,
     			"stockUuid":stockUuid,
     			"cruiseshipInfoUuid":cruiseshipInfoUuid,
     			"shipDate":shipDate
     		},
     		async: false,
     		success:function(result){
     			 var html = "";
     			$("#editLog").find("tbody").find("tr").remove();
     			 $.each(result,function(i,n){
     				 var date=new Date(parseInt(n.createDate))
     				 var year=date.getFullYear();
     				 var month=date.getMonth()+1;
     				 var day=date.getDate();
     				 var formatDate=year+"-"+month+"-"+day
     				 var str="";
     				 if(n.operate_type==1){
     					 str="增加";
     				 }else{
     					 str="减少";
     				 }
     				  	  html+="<tr>";
     				      html+="<td  class='tc'>"+formatDate+"</td>";
     				      html+="<td  class='tc'>"+n.name+"</td>";
     				      html+="<td  class='tc'>库存"+str+""+Math.abs(n.stock_amount_after-n.stock_amount)+"余位"+str+""+Math.abs(n.free_position_after-n.free_position)+"</td>";
     				      html+="</tr>";
     			  });
     			$("#editLog").find("tbody").append(html); 
     		}
     	});
     	jbox_relevance_edit_record_pop(uuid);
     }
	 //关闭窗口
	function closeWindows(){
		window.open("${ctx}/cruiseshipStock/cruiseshipStockList");
		window.close();
    }
    </script>
</head>
<body>
    <!--右侧内容部分开始-->
    <div class="produceDiv">
        <div style="width:100%; height:20px;"></div>
        <form enctype="multipart/form-data" method="post" action="" class=" form-search" id="addForm" novalidate="novalidate">
        	<input type="hidden" value="${stock.uuid }" id="stockUuid" name="stockUuid"/><%-- --%>
        	<input id="token" name="token" type="hidden" value="${token}" />
        	<input type="hidden" value="${relFlag }" id="relFlag" /><%-- --%>
            <div class="messageDiv">
                <div class="kongr"></div>
                <div style="display: block;" class="ydxbd">
                    <div class="activitylist_bodyer_right_team_co1">
                        <label class="label-style">游轮名称：</label>
                        <span class="text-show ellipsis-text-detail" title="${stock.cruiseshipInfoName}">${stock.cruiseshipInfoName}</span>
                    </div>
                    <div class="activitylist_bodyer_right_team_co2">
                        <label class="label-style">船期：</label>
                        <span class="text-show"  title="<fmt:formatDate value="${stock.shipDate }" pattern="yyyy-MM-dd" />">
                       		<fmt:formatDate value="${stock.shipDate }" pattern="yyyy-MM-dd" />
                        </span>
                    </div>
                </div>
                <!--0023列表开始-->
                <div class="">
                    <div class="page">
                        <div class="pagination">
                            <dl>
                                <dd>
                                    <a onclick="jbox_relevance_record_edit_batch_pop();">批量修改</a>
                                </dd>
                            </dl>
                        </div>
                    </div>
                    <table id="contentTable" class="table activitylist_bodyer_table">
                        <thead>
	                        <tr>
	                            <th width="4%">序号</th>
	                            <th width="20%">舱型</th>
	                            <th width="10%">库存总数</th>
	                            <th width="5%">余位</th>
	                            <th width="6%">操作</th>
	                        </tr>
                        </thead>
                        <tbody>
	                         <c:forEach items="${details }" var="list" varStatus="s">
			                        <tr id="${list.cruiseshipCabinUuid }">
			                            <td class="tc">
			                               <%--  <input name="ids" value="${list.uuid}" type="checkbox"> --%>
			                                ${s.count}<input type="hidden" value="${list.uuid }" name="uuid"/>
			                            </td>
			                            <td class="tl" name="cabinName">${list.cruiseshipCabinName}</td>
			                            <td class="tc" name="stockNum">${list.stockAmount}</td>
			                            <td class="tc" name="surplusNum">${list.freePosition}</td>
			                            <td class="p0">
			                                <dl class="handle">
			                                    <dt><img title="操作" src="${ctxStatic }/images/handle_cz.png"></dt>
			                                    <dd class="">
			                                        <p>
			                                            <span></span>
			                                            <a onclick="jbox_relevance_record_edit_pop(this);">修改</a>
			                                            <a href="javascript:void(0)" onclick="deleteOneStock('${list.uuid}',this);">删除</a>
			                                            <a onclick="Jilu('${list.cruiseshipCabinUuid}','${stock.uuid }','${stock.cruiseshipInfoUuid }','${stock.shipDate }');">修改记录</a>
			                                        </p>
			                                    </dd>
			                                </dl>
			                            </td>
			                        </tr>
	                          </c:forEach>
                        </tbody>
                    </table>
                </div>
                <!--0023列表结束-->
                <!--填写价格开始-->
                <div style="" class="mod_information" id="secondStepDiv">
                    <div class="mod_information_d" id="secondStepTitle"><span
                            style=" font-weight:bold; padding-left:20px;float:left">其他信息</span>
                    </div>
                    <div id="secondStepEnd">
                        <div class="add-remarks">
                            <span>备注：</span>
                            <textarea name="memo" cols="" rows="" id ="memo">${stock.memo}</textarea></div>
                    </div>
                    <div class="mod_information_dzhan_d" id="secondStepBtn" style="display: none;">
                        <div class="release_next_add">
                            <input value="下一步" onclick="secondToThird()"
                                   class="btn btn-primary valid displayClick" type="button">
                        </div>
                    </div>
                    <div class="kong"></div>
                </div>
                <!--填写价格结束-->
                <div style="clear:none;" class="kong"></div>
                <div id="thirdStepDiv">
                    <!-- 上传文件 -->
                    <div class="mod_information_d7"></div>
                    <div class="mod_information_3 update-document">
                        <div class="upload_file_list">
                            <p class="maintain_pfull new_kfang">
								<label>游轮资料：</label> 
								<input type="button" name="hotelAnnexDocId" value="上传" class="btn btn-primary" onClick="uploadFiles('${ctx}',null,this)"/> <em class="tips_color">多文件，多格式上传附件。单个附件不大于20M，总共不大于20M。</em>
								<ol class="batch-ol">
									<c:forEach items="${annexList}" var="file" varStatus="s1">
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
                    </div>
                    <div class="release_next_add">
                        <input value="关闭"  onclick="closeWindows()"  class="btn btn-primary gray" type="button">
                        <input value="保存" onclick="editStockDetail(this)" class="btn btn-primary" type="button">
                    </div>
                </div>
        </form>
    </div>
    <!--右侧内容部分结束-->
    <!--S--批量修改弹出窗-->
    <div id="relevance-record-edit-batch-pop" class="display-none">
        <div class="relevance-record-edit-batch-pop">
            <div class="part-f">
                <ul name="cabinList">
                    <li><input type="checkbox"/></li>
                </ul>
            </div>
            <div class="mod_information_d7"></div>
            <div class="part-s">
                <label><i class="xing">*</i></label>
                <select name="cabinOrStock">
                    <option value="-1">请选择</option>
                </select>
                <select name="operate">
                    <option value="1">增加</option>
                    <option value="2">减少</option>
                </select>
                <input type="text" name="operateCount" data-type="number"/>
            </div>
        </div>
    </div>
    <!--E--批量修改弹出窗-->
    <!--S--修改弹出窗-->
    <div id="relevance-record-edit-pop" class="display-none">
        <div class="relevance-record-edit-batch-pop">
            <div class="part-f display-none">
            </div>
            <div class="mod_information_d7"></div>
            <div class="part-s">
                <label><i class="xing">*</i></label>
                <select name="cabinOrStock" >
                    <option value="-1">请选择1231231</option>
                </select>
                <select name="operate">
                    <option value="1">增加</option>
                    <option value="2">减少</option>
                </select>
                <input type="text" data-type="number" name="operateCount"/>
            </div>
        </div>
    </div>
    <!--E--修改弹出窗-->
    <!--S--修改记录弹窗-->
    <div id="relevance-edit-record-pop" class="display-none">
        <div class="relevance-record-pop">
            <table class="table table-striped table-bordered " id="editLog">
                <thead>
	                <tr>
	                    <th width="30%" class="tc">修改时间</th>
	                    <th width="20%" class="tc">修改人</th>
	                    <th width="50%" class="tc">修改内容</th>
	                </tr>
                </thead>
                <tbody>
	                <!-- <tr>
	                    <td  class="tc">2016-01-30</td>
	                    <td class="tc">张三</td>
	                    <td class="tc">库存增加10，余位增加10</td>
	                </tr>
	                <tr>
	                    <td  class="tc">2016-01-30</td>
	                    <td class="tc">张三</td>
	                    <td class="tc">库存增加10，余位增加10</td>
	                </tr>
	                <tr>
	                    <td  class="tc">2016-01-30</td>
	                    <td class="tc">张三</td>
	                    <td class="tc">库存增加10，余位增加10</td>
	                </tr> -->
              </tbody>
            </table>
        </div>
    </div>
     <div id="hiddenLogs" class="display-none">
       <c:forEach items="${stockLogs}" var="logs" varStatus="s">
          <tr class="${logs.cruiseshipCabinUuid}">
              <td  class="tc"><fmt:formatDate value="${logs.shipDate }" pattern="yyyy-MM-dd" /></td>
              <td class="tc">${fns:getUserById(logs.createBy).name}</td>
              <td class="tc">库存222222
              <c:choose> 
				  <c:when test="${logs.operateType==1}">   
				 		   增加 
				  </c:when> 
				  <c:otherwise>   
				    	减少  
				  </c:otherwise> 
			   </c:choose> 
              ${logs.operateNum }，
                                           余位 <c:choose> 
				  <c:when test="${logs.operateType==1}">   
				 		   增加 
				  </c:when> 
				  <c:otherwise>   
				    	减少  
				  </c:otherwise> 
			   </c:choose> 
              ${logs.operateNum }</td>
          </tr>
       </c:forEach>
    </div>
    <!--E--修改记录弹窗-->   
</div>
</body>
</html>
