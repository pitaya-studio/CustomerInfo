var CostManager = function CostManager(groupId){
	this.groupId = groupId;
};
CostManager.prototype.eventHandler = function(mark, $Table){
    var sum = 0;
    var income = 0;
    var gain = 0;
    var datas = $('[price-sum-bind=\'' + mark + '\']');
    
    for(var i = 0; i < datas.length; i++){
        sum += parseInt(datas[i].innerHTML);
    }
    var orders = $Table.find('td:nth-child(8) span');
    for(var i = 0; i < orders.length; i++){
        var orderPrice = orders[i].innerHTML.replace(',', "");
        gain += parseInt(orderPrice);
    }
    income = gain - sum;
    $('[price-sum-all=\'' + mark + '\']').html(sum);
    $('[price-sum-gain=\'' + mark + '\']').html(gain);
    $('[price-sum-income=\'' + mark + '\']').html(income);
};

/*
* 按类型获取成本
*/
CostManager.prototype.getCost = function(classType, $List){
    $.ajax({
        type: "GET",
        url: "${ctx}/cost/manager/getCosts/" + classType + "/" + this.groupId,
        dataType:"json",
        async:false,
        cache:false,
        data:{
        	states : "start"
        },
        success: function(array){
        	var htmldoc = "";
            for(var i = 0; i < array.length; i++){
            	htmldoc += '<tr id=' + classType + "_" + array[i].id + '> <td>' + (i + 1) + '</td><td>'+array[i].name+'</td><td price-sum-bind="' + classType + '">'+array[i].price+'</td><td>'+array[i].comment+'</td><td><a href="javascript:void(0)" onclick="modifyCost('+ array[i].id +', \'' + classType + '\')">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="return deleteCost('+array[i].id+', \'' + classType + '\');" class="button_delcb"></a></td></tr>';
            }
            $List.append(htmldoc);
        }
     });
};

CostManager.prototype.addCost = function addCost(text, classType, $List) {
    var _chengBenText = $(text).next().text();
    var $div = $("<div></div>")
                .append($('<div class="msg_div msg_div_cb"><p><span><font>*</font>项目名称 ：</span><input type="text" id="name" class="inputTxt" name="name" value="" /></p><p><span><font>*</font>项目金额 ：</span><input type="text" id="price" class="inputTxt" name="price" value=""/>元</p><p><span>项目备注 ：</span><textarea name="comment" maxlength="" class="" rows="3"></textarea></p></div>'));
    var html = $div.html();
    var submit = function (v, h, f) {
        var name = $.trim(f.name);
        var price = $.trim(f.price);
        var comment = f.comment;
        var reg = /^[\-\+]?\d+$/;
        if(v === 0){
            return true;
        }
        if(name==""){
            top.$.jBox.tip('项目名称不能为空', 'error', { focusId: 'name' }); return false;
        }
        else if(price==""){
            top.$.jBox.tip('项目金额不能为空', 'error', { focusId: 'price' }); return false;
        }
        else {
            if(!reg.test(price)){
                top.$.jBox.tip('项目金额请输入正整数','error' ,{ focusId: 'price' });
                return false;
            }else{
            	var jsonRes = {name : name, price : price, comment : comment, activityGroupId : this.groupId};
                $.ajax({
                    type: "POST",
                    url: "${ctx}/cost/manager/save",
                    cache:false,
                    dataType:"json",
                    async:false,
                    data:{value:JSON.stringify(jsonRes),
                    	type : classType},
                    success: function(array){
                        for(var i = 0; i < array.length; i++){
                        	var index = parseInt($List.find("td:first-child").last().html());
                        	if(!index){
                        		index = 0;
                        	}
                            $List.append('<tr id=' + classType + "_" + array[i].id + '> <td>' + (index + i + 1) + '</td><td>'+array[i].name+'</td><td price-sum-bind="' + classType + '">'+array[i].price+'</td><td>'+array[i].comment+'</td><td><a href="javascript:void(0)" onclick="modifyCost('+ array[i].id +', \'' + classType + '\')">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="return deleteCost('+array[i].id+', \'' + classType + '\');" class="button_delcb"></a></td></tr>');
                        }
                    },
                    error : function(e){
                    	top.$.jBox.tip('请求失败。','error' ,{ focusId: 'price' });
                    	return false;
                    }
                 });
            }
        }
    };
    $.jBox(html, { title: '成本录入', buttons:{'取消' : 0, '确定' : 1 }, submit: submit, height:290});
};   

var modifyCost = function(id, classType){
    
    var modifyObj = $('#' + classType + "_" + id);
    
    var $nameOld = modifyObj.find("td:nth-child(2)");
    var $priceOld = modifyObj.find("td:nth-child(3)");
    var $commentOld = modifyObj.find("td:nth-child(4)");
    
    var $div = $("<div></div>")
                .append($('<div class="msg_div msg_div_cb"><p><span><font>*</font>项目名称 ：</span><input type="text" id="name" class="inputTxt" name="name" value="' + $nameOld.html() + '" /></p><p><span><font>*</font>项目金额 ：</span><input type="text" id="price" class="inputTxt" name="price" value="' + $priceOld.html() + '"/>元</p><p><span>项目备注 ：</span><textarea name="comment" maxlength="" class="" rows="3">' + $commentOld.html() + '</textarea></p></div>'));
    var html = $div.html();
    var submit = function (v, h, f) {
        var name = $.trim(f.name);
        var price = $.trim(f.price);
        var comment = f.comment;
        var reg = /^\d+$/;
        if(v === 0){
            return true;
        }
        if(name==""){
            top.$.jBox.tip('项目名称不能为空','error', { focusId: 'name' }); return false;
        }
        else if(price==""){
            top.$.jBox.tip('项目金额不能为空','error' ,{ focusId: 'price' }); return false;
        }
        else {
            if(!reg.test(price)){
                top.$.jBox.tip('项目金额请输入正整数','error' ,{ focusId: 'price' });
                return false;
            }else{
            	var jsonRes = {id : id, name : name, price : price, comment : comment, activityGroupId : this.groupId};
                $.ajax({
                    type: "POST",
                    url: "${ctx}/cost/manager/modify",
                    cache:false,
                    dataType:"json",
                    async:false,
                    data:{value:JSON.stringify(jsonRes),
                        type : classType},
                    success: function(array){
                        
                    	$nameOld.html(array[0].name);
                    	$priceOld.html(array[0].price);
                    	$commentOld.html(array[0].comment);
                        
                    },
                    error : function(e){
                        top.$.jBox.tip('请求失败。','error' ,{ focusId: 'price' });
                        return false;
                    }
                 });
            }
        }
    };
    $.jBox(html, { title: '成本修改', buttons:{'取消' : 0, '确定' : 1 }, submit: submit, height:290});
    
};
var deleteCost = function(id, classType){
    $.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f) {
        if (v == 'ok') {
            $.ajax({
                type: "POST",
                url: "${ctx}/cost/manager/delete",
                cache:false,
                async:false,
                data:{id : id,
                    type : classType},
                success: function(e){
                    if(e == 'true'){
                        var rmObj = $('#' + classType + "_" + id);
                        var tmp = $('#' + classType + "_" + id + " + tr");
                        rmObj.remove();
                        while(tmp){
                        	var indexTd = tmp.find("td:nth-child(1)");
                        	var index = indexTd.html();
                        	if(indexTd && index){
                        		indexTd.html(index - 1);
                        		tmp = tmp.next();
                        	}else{
                        		break;
                        	}
                        }
                        if(tmp.length == 0){
                            $('#'+classType).attr('callListener',Math.floor(Math.random()*1000));
                        }
                        //IE7触发事件监听
                        if((navigator.appName == "Microsoft Internet Explorer") && (navigator.appVersion.match(/7./i)=='7.')){
                            document.getElementById(classType).setAttribute("callListener","New Attribute");
                        }
                    }else{
                        top.$.jBox.tip('请求失败。','error');
                        return false;
                    }
                    
                },
                error : function(e){
                    top.$.jBox.tip('请求失败。','error');
                    return false;
                }
             });
        }
    });
};

var save = function(){
    $.ajax({
        type: "POST",
        url: "${ctx}/cost/manager/flow/start/save/" + this.groupId,
        data : {types : "operator,operatorBudget"},
        async:false,
        success: function(e){
        	if(e == "true"){
        		location.href = '${ctx}/cost/manager/list';
        	}else{
        		top.$.jBox.tip('请求失败。','error');
        		return false;
        	}
        }
     });
};

var submit = function(){
    $.ajax({
        type: "POST",
        url: "${ctx}/cost/manager/flow/start/commit/" + this.groupId,
        data : {types : "operator,operatorBudget"},
        async:false,
        success: function(e){
            if(e == "true"){
            	location.href = '${ctx}/cost/manager/list';
            }else{
            	top.$.jBox.tip('请求失败。','error');
            	return false;
            }
        }
     });
};

function trimStr(str){
	return str.replace(/(^\s*)|(\s*$)/g,"");
}

//运控-成本录入-添加项目--小计
function costSums(obj,objshow,budgetType){
	var objMoney = {};
	obj.each(function(index, element) {
		//var currencyName = $(element).find("td[name='tdCurrencyName']").text();
		//var thisAccount = $(element).find("td[name='tdAccount']").text();

		var thisPrice = $(element).find("td[name='tdPrice']").text();
		var thisReview = $(element).find("td[name='tdReview']").text();
		var border=2;
		//去掉两边空格
		thisPrice=thisPrice.replace(/(^\s*)|(\s*$)/g, "");
		//找到金额中第一个数字位置
		for(var i=0;i<thisPrice.length;i++){
			if(thisPrice.substring(i,i+1).match(/^[0-9].*$/)){
				border=i;
				break;
			}
		}
		var currencyName =thisPrice.substring(0,border);
		//金额去掉第一个字符(币种)
		thisPrice=thisPrice.substring(border);
		
		if(budgetType == 2 || (budgetType == 0 && trimStr(thisReview) != '已取消' && trimStr(thisReview) != '取消申请')|| (budgetType == 1 && trimStr(thisReview) != '已取消' && (trimStr(thisReview) != '已驳回'&&trimStr(thisReview) != '审核失败(驳回)' && trimStr(thisReview) != '取消申请' && trimStr(thisReview) != '审批驳回') )){
		if(typeof objMoney[currencyName] == "undefined"){
			//objMoney[currencyName] = parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(",",""),10);
			objMoney[currencyName] = parseFloat(thisPrice.replace(",",""),10);
		}else{
			//objMoney[currencyName] += parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(",",""),10);
			objMoney[currencyName] += parseFloat(thisPrice.replace(",",""),10);
		}
		}
	});
	//输出结果
	var strCurrency = "";
	var sign = " + "; 
	for(var i in objMoney){
		var isNegative = /^\-/.test(objMoney[i]);
		if(isNegative){
			sign = " - ";
		}
		if(strCurrency != '' || (strCurrency == '' && isNegative)){
			strCurrency += sign;
		}
		strCurrency += i + milliFormat(objMoney[i].toString().replace(/^\-/g,''),'1');
	}
	if(objshow.length>0) objshow.text("  "+strCurrency);
}

//上传文件时，点击后弹窗进行上传文件(多文件上传)
//方法参数：${ctx}、显示上传文件名称input的id、当前点击元素
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
//	
//	/*移除产品行程校验提示信息label标签*/
//	$("#modIntroduction").remove();
	
	$.jBox("iframe:"+ ctx +"/cost/common/uploadFilesPage", {
		title: "多文件上传",
		width: 340,
		height: 365,
		buttons: {'完成上传':true},
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
//					//如果是产品行程介绍
//					if($(obj).attr("name") == 'introduction') {
//						$(obj).next().next("#introductionVaildator").val("true").trigger("blur");
//					}
//					//如果是签证资料的文件上传
//					if($(obj).attr("name").indexOf("signmaterial") >= 0) {
//						$(obj).parent().parent().parent().parent().next(".batch-ol").append('<li><span>'+ $(obj1).val() +'</span><a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(\''+ $(obj1).prev().val() +'\',\''+ $(obj).attr("name") +'\',this);">删除</a></li>');
//					}else{
						$(obj).next(".batch-ol").append('<li><span style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;max-width:173px;display:inline-block;float: left;" title="'+ $(obj1).val() +'">'+ $(obj1).val() 
								+'</span><a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(\''+ $(obj1).prev().val() +'\',\''+ $(obj).attr("name") +'\',this);">×</a></li>');
//					}
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
			
			//如果是产品行程介绍文件删除的话，需要进行必填验证
			if("introduction" == objName) {
				if(0 == $("#introductionVaildator").prev(".batch-ol").find("li").length) {
					$("#introductionVaildator").val("").trigger("blur");
				}
			}
		}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');		
}

function flashChecker()
{
	var hasFlash=0;        //是否安装了flash
	var flashVersion=0;    //flash版本
	
	if(document.all){
		var swf ;
		try{
		   swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash'); 
		} catch (e) {
			hasFlash=1;
			alert("请您退出当前浏览器,并安装当前浏览器的flash player软件,然后才能上传文件");
		}
		if(swf) { 
			hasFlash=1;
			VSwf=swf.GetVariable("$version");
			flashVersion=parseInt(VSwf.split(" ")[1].split(",")[0]); 
		}
	}else{
		if (navigator.plugins && navigator.plugins.length > 0){
			var swf=navigator.plugins["Shockwave Flash"];
			if (swf)  {
			   hasFlash=1;
		       var words = swf.description.split(" ");
		       for (var i = 0; i < words.length; ++i){
		         if (isNaN(parseInt(words[i]))) continue;
		         flashVersion = parseInt(words[i]);
			   }
			}
		}
	}
	return {f:hasFlash,v:flashVersion};
}

/*C476--打开下载附件窗口start*/
function showDownloadWin(ctx,costId,delFlag,hasVouchers){
	if(hasVouchers == "") {
//		$.jBox.tip('没有上传附件！','info');
		return;
	}
    $.jBox('iframe:'+ctx+'/cost/common/getCostVouchers/'+costId+'?delFlag='+delFlag,{
        title:'下载附件',
        width:500,
        height:400,
        buttons:{'关闭':true},
        loaded:function(h){
            //消除滚动条
            $(".jbox-content", top.document).css("overflow-y","hidden");
//            if(hasPermission){
//                $(h.find("iframe")[0].contentDocument).find('li').append('<em class="ico-del" title="删除" onclick="deleteAttachment(this)"></em>');
//            }
        }
    })
}

//实际成本下载附件
function download(id){
	window.open("${ctx}/sys/docinfo/download/"+id);
}
