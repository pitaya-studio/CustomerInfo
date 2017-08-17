/**
 * Created by xcihao sun on 2015/12/25.
 */
/* C0068-报名模块添加团期列表-散拼-start*/

$(document).on('change','#contentTable input[type="checkbox"]',function(){
    var chkAll = 0;
    $('#contentTable input[type="checkbox"]').each(function () {
        if (!$(this).attr('checked')) {
            chkAll++;
        }
    })
    if (chkAll == 0) {
        $('[name="allChk"]').attr('checked', 'checked');
    }
    else {
        $('[name="allChk"]').removeAttr('checked');
    }
})
//反选
function checkreverse(obj) {
    $('#contentTable input[type="checkbox"]').each(function () {
        $(this).attr("checked", !$(this).attr("checked"));
    });
    var chkAll = 0;
    $('#contentTable input[type="checkbox"]').each(function () {
        if (!$(this).attr('checked')) {
            chkAll++;
        }
    })
    if (chkAll == 0) {
        $('[name="allChk"]').attr('checked', 'checked');
    }
    else {
        $('[name="allChk"]').removeAttr('checked');
    }
}
//全选
function checkall(obj) {
    if ($(obj).attr("checked")) {
        $('#contentTable input[type="checkbox"]').attr("checked", 'checked');
        //$("input[name='allChk']").attr("checked",'checked');
    } else {
        $('#contentTable input[type="checkbox"]').removeAttr("checked");
        //$("input[name='allChk']").removeAttr("checked");
    }
}
//批量开票
function jbox__batch_invoice_op(ctx){
    var msg;
    var n = 0;
    var ids = "";
    //提示
    $('#contentTable input[type="checkbox"]:checked').each(function (){
        var $tds = $(this).parent().siblings();
        var temp = "'" + this.value + "'";
        if( n== 0){
        	ids += temp;
        	n++;
        } else {
        	ids += "," + temp;
        }
        if($tds.eq(3).text().trim().indexOf('已领取') > 0){
            msg = "选择的数据中有已领取数据！";
            return ;
        }else if($tds.eq(10).text().trim()=='已开票'){
            msg = "选择的开票数据中有已开票数据！";
            return;
        }
    });
    if(ids == ""){
    	$.jBox.tip("请选择开票数据！");
        return;
    }
    if(msg){
        $.jBox.tip(msg);
        return;
    }
    var html = "";
    $.ajax({
    	type : 'POST',
    	url : ctx + '/invoice/limit/invoceopenlist',
    	data : {
    		ids : ids
    	},
    	success : function(data){
    		if(data.flag == 'success'){
    			var now = getNowFormatDate();
    			//批量开票弹窗 start
    			html = "<div id='batch-invoice-op'>"
    			    + "<div class='batch-invoice-op'>"
    			    + " <table class='activitylist_bodyer_table'>"
    			    + " <thead>"
    			    + " <tr>"
    			    + "<th>序号</th>"
    			    + "<th>开票项目</th>"
    			    + "<th>开票类型</th>"
    			    + "<th>开票方式</th>"
    			    + "<th>发票抬头</th>"
    			    + "<th>开票客户</th>"
    			    + "<th>开票原因</th>"
    			    + "<th>本次开票</th>"
    			    + "<th><span class='xing'>*</span>发票号</th>"
    			    + "<th><span class='xing'>*</span>开票日期</th>"
    			    + "<th>开票备注</th>"
    			    + "</tr>"
    			    + "</thead>"
    			    + "<tbody id='vipbody'>";
    			    for(var i = 0; i < data.list.length;i++){
    			    	var tn = i + 1;
	    			    html += "<tr>"
	    			    + "<td>" + tn + "<input name = 'uuid' value = '" + data.list[i].uuid + "' type='hidden'/></td>"
	    			    + "<td class='tc'>" + data.list[i].invoiceSubjectLabel + "</td>"
	    			    + "<td class='tc'>" + data.list[i].invoiceTypeLabel + "</td>"
	    			    + "<td class='tc'>" + data.list[i].invoiceModeLabel + "</td>"
	    			    + "<td class='tc'>" + data.list[i].invoiceHead + "</td>"
	    			    + "<td class='tc'>" + data.list[i].invoiceCustomer + "</td>"
	    			    + "<td class='tc'><p class='batch-invoice-reason' title='" + data.list[i].remarks + "'>" + data.list[i].remarks + "</p></td>"
	    			    + "<td class='tr'>" + data.list[i].invoiceAmount + "</td>"
	    			    + "<td class='tr'><input name = 'invoiceNumber' type='text'/></td>"
	    			    + "<td class='tr'><input class='inputTxt dateinput' name='invoiceTime' value='" + now + "'  onClick='WdatePicker()' readonly/></td>"
	    			    + "<td class='tr'><input name = 'numberRemark' type='text'/></td>"
	    			    + "</tr>";
    			    }
    			    html += "</tbody>"
    			    + "</table>"
    			    + "</div>"
    			    + "</div>";
    		//批量开票弹窗 end
			    //批量开票提示列表
			    $.jBox(html, {
			        title: "批量开票", buttons: {'开票': 1,'取消':2}, submit: function (v, h, f) {
			            if (v == "1") {
			            	var datas = new Array();
			            	var dn = 0;
			            	var errmsg = "";
			            	$('#vipbody tr').each(function (){
			            		var uuid = $("input[name='uuid']")[dn].value;
			            		var invoiceNumber = $("input[name='invoiceNumber']")[dn].value;
			            		var numberRemark = $("input[name='numberRemark']")[dn].value;
			            		var invoiceTime = $("input[name='invoiceTime']")[dn].value;
			            		if(invoiceNumber == null || invoiceNumber == ""){
			            			errmsg = "发票号不能为空";
			            			return;
			            		}
			            		if(invoiceTime == null || invoiceTime == ""){
			            			errmsg = "开票时间不能为空";
			            			return;
			            		}
			            		var s = new Object();
			            		s.uuid = uuid;
			            		s.invoiceNumber = invoiceNumber;
			            		s.numberRemark = numberRemark;
			            		s.invoiceTime = invoiceTime;
			            		datas[dn] = s;
			            		dn++;
			            	});
			            	if(errmsg != ""){
			            		$.jBox.tip(errmsg);
			            		return false;
			            	}
			            	$.ajax({
			            		type : 'POST',
			            		url : ctx + '/invoice/limit/invocebatchopen',
			            		data : {
			            			datas : JSON.stringify(datas)
			            		},
			            		success : function(v){
			            			if(v == "success"){
			            				$("#searchForm").submit();
			            			} else {
			            				if(v != "error"){
			            					$.jBox.tip(v);
			            					$("#searchForm").submit();
			            				} else {
			            					$.jBox.tip("开票失败");
			            				}
			            			}
			            		}
			            	});
			            }
			        },height: '400', width: 1000,
			        persistent: true
			    });
			    inquiryCheckBOX();
    		} else {
    			$.jBox.tip(data.msg);
    	        return;
    		}
    	}
    })
}
/**
 * 获取当前系统时间
 * 日期格式为   yyyy-MM-dd
 * @author yang.wang
 * */
function getNowFormatDate(){
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	
	if(month >=1 && month <= 9){
		month = '0' + month;
	}
	if(day >=1 && day <= 9){
		day = '0' + day;
	}
	
	var currentdate = year + '-' + month + '-' + day;
	return currentdate;
}
//批量领取
function batchReceive(ctx){
	var tmp = "";
    var msg = "";
    //提示
    $('#contentTable input[type="checkbox"]:checked').each(function (){
        var $tds = $(this).parent().siblings();
        if($tds.eq(3).text().trim().indexOf('（已领取）') > 0){
            msg = "选择的数据中有已领取数据！";
            return ;
        }else if($tds.eq(11).text().trim()=='待开票'){
            msg = "选择的数据中有待开票数据！";
            return ;
        }else if($(this).attr("checked")!=null && $(this).attr("checked")=="checked") {
			tmp = tmp + $(this).attr('value') + ",";
		}
    });
	$('#contentTable input[type="checkbox"]:checked').each(function (){
		var $tds = $(this).parent().siblings();
		if($tds.eq(11).text().trim()=='待开票'){
			msg = "选择的数据中有待开票数据！";
			return ;
		}
	});
    if(msg == "" && tmp == ""){
		msg = "请选择领取记录!";
	}
    if(msg){
        $.jBox.tip(msg);
        return;
    }
    
    $.ajax({
		type:"POST",
		url:ctx + "/invoice/limit/batchReceive",
		data:{uuids:tmp},
		success:function(data){
			$("#searchForm").submit();
		},
		error:function(){
			alert('返回数据失败');
		}
	});
}

/* C0068-报名模块添加团期列表-散拼-end*/

/*获取地址栏查询字符串 @yudong.xu*/
function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}

/*导出到excel @yudong.xu*/
function exportToExcel(ctx){
	//表单所有参数
	var vs = getQueryString('verifyStatus');//审核状态
	//var v = "<input type='hidden' name='verifyStatus' value='" + vs+ "'/>";
	//$("#searchForm").append(v);
	$("#searchForm").attr("action",ctx + "/invoice/limit/exportexcel?verifyStatus=" + vs);//先改变一下form的action，提交后需再改回来。
	$("#searchForm").submit();
	$("#searchForm").attr("action",ctx + "/invoice/limit/supplyinvoicelist?verifyStatus=" + vs);
}