$(function(){
	
	//异步获取第一分页
	erecordList.ajax.getRecordList();
	// 返回询价列表
	$("[name=history-back]").click(function(){
		if(history.length>0){
			history.back();
		}else{
			location.href=contextPath+"/eprice/manager/project/list4saler";
		}
	});
	//排序方式 0 倒序 1正序
	$("#sort-list-ul-id li[sortc]").click(function(){
		var sortc = $(this).attr("sortc");
		var sortv = $(this).attr("sortv");
		
		//正倒序排序图标和排序值更新
		$("#sort-list-ul-id i").hide();
		$(this).find("i").removeClass("icon-arrow-down icon-arrow-up");
		$("#sort-list-ul-id li.activitylist_paixu_moren").removeClass("activitylist_paixu_moren");
		$(this).remove("activitylist_paixu_left_biankuang").addClass("activitylist_paixu_moren");
		if(sortv==1){
			$(this).attr("sortv",0);
			$(this).find("i").addClass("icon-arrow-down");
		}else{
			$(this).attr("sortv",1);
			$(this).find("i").addClass("icon-arrow-up");
		}
		$(this).find("i").show();
		sortv = sortc+"-"+sortv;
		
		//多字段排序使用，保证每次更新对应字段的排序值
		//var input = $("#eprice-search-form-id input[name=orderColumn][sortc="+sortc+"]");
		
		//单字段排序，保证排序表单元素唯一性
		var input = $("#eprice-search-form-id input[name=orderColumn]");
		if($(input).length<1){
			 input = $('<input type="hidden" name="orderColumn" sortc="'+sortc+'"/>');
			 $("#eprice-search-form-id").append(input);
		}
		
		$(input).val(sortv);
		
		erecordList.ajax.getRecordList();
	});
	
});



function outPriceOnClick(outPrice){
	var value = $(outPrice).val();
	if(value=="待报价"||value==""||value=="invalid value"||value=="NaN"){
		 $(outPrice).val("");
		 return;
	} 
	value = rmoney(value);
	 $(outPrice).val(value);
}


function outPriceOnblur(outPrice,recordId){
	//原始外报价
	var preOutPrice = $("#hid_outPrice"+recordId).val();
	//成本价	
	var operatorPrice = $("#hid_operatorPrice"+recordId).val();
	var newOutPrice = $(outPrice).val();
	if(newOutPrice==preOutPrice&&newOutPrice!=null&&newOutPrice!=""){
		$(outPrice).val(milliFormat(newOutPrice,2));
		return;
	}
	if(newOutPrice==null||newOutPrice==""){
		$(outPrice).val("待报价");
	}else{
		$(outPrice).val(milliFormat(newOutPrice,2));
	}
	var params ="outPrice="+newOutPrice+"&recordId="+recordId;
	$.ajax({
		type : "POST",
		url : contextPath+"/eprice/manager/ajax/project/changeEpriceOutPrice",
		data : params,
		dataType : "text",
		success : function(html){
			var json;
			try{
				json = $.parseJSON(html);
			}catch(e){
				json = {res:"error"};							
			}
			if(json.res=="success"){
				$("#hid_outPrice"+recordId).val(newOutPrice);
				if(newOutPrice!=null&&newOutPrice!=""&&operatorPrice!=null&&operatorPrice!=""&&operatorPrice!="null"){
					$("#settlementPrice"+recordId).html("￥"+milliFormat(doubleSub(newOutPrice,operatorPrice),2));
				}else{
					$("#settlementPrice"+recordId).html("待报价");
				}
			}else if(json.res=="data_error"){
				jBox.tip("输入数据不正确", 'error');
			}else{
				jBox.tip(json.res, 'error');
			}
		}
	
	});
	
	
}

var erecordList = {
		dao : {
			
			/**
			 * 生成询价项目下属巡检记录列表元素——询价记录 html string
			 * @author lihua.xu
			 * @时间 2014年9月30日
			 * @param {} m 询价记录对象
			 * @return {html string}
			 */
			record : function(m,i,createUser,pstatus){
				var html = new Array();
				html.push('<tr><td width="4%">'+i+'</td><td width="7%" class="tc">'+$.trekizFormatDate({time:m.modifyTime,pattern:"yyyy-MM-dd"})+'</td>');
				var namestr = "";
				// (只在确定报价时，出现被选中计调员)
				if( m.estimateStatus==3 || m.estimateStatus==4 ||m.estimateStatus==5){
					
					if(m.acceptToperatorReply!=null){
						namestr+= m.acceptToperatorReply.userName+"<br/>";
					}
					html.push('<td width="7%">'+namestr+'</td>');//计调
					
				}else{
					html.push('<td width="7%"></td>');//计调
				}
				
				if(m.estimateStatus<=1 || m.estimateStatus==6){
					namestr = '<span class="tdred">待报价</span>';
				}else{
					if(m.lastAoperatorPriceTime>=m.lastToperatorPriceTime){
						namestr = m.lastAoperatorPriceTime;
					}else{
						namestr = m.lastToperatorPriceTime;
					}
					namestr = $.trekizFormatDate({time:namestr,pattern:"yyyy-MM-dd"});
				}
           		html.push('<td width="7%" class="tc">'+namestr+'</td>');//报价时间
           		var namestr2 = "";
           		if(m.lastToperatorStartOutTime){
           			namestr2 = m.lastToperatorStartOutTime;
           			namestr2 = $.trekizFormatDate({time:namestr2,pattern:"yyyy-MM-dd"});
           			html.push('<td width="7%" class="tc">'+namestr2+'</td>');//出发日期
           		}else{
           			html.push('<td width="7%" class="tc">未确定</td>');//出发日期
           		}
           		//航班类型
           		var trafficLineType = m.trafficRequirements.trafficLineType;
               	 if(trafficLineType){
               		 if(trafficLineType==1){
               			 html.push('<td width="7%">往返</td>');
               		 }else if(trafficLineType==2){
               			html.push('<td width="7%">单程</td>');
               		 }else if(trafficLineType==3){
               			html.push('<td width="7%">多段</td>');
               		 }else{
               			 html.push('<td width="7%">未确定</td>');
               		 };
               	 }else{
               		 html.push('<td width="7%">未确定</td>');
               	 }
               	 // 出发城市
                if(m.startCity){
                	html.push('<td width="7%">'+m.startCity+'</td>');
                }else{
                	html.push('<td width="7%">未确定</td>');
                }
                // 目的地城市
                if(m.startCity){
                	html.push('<td width="7%">'+m.endCity+'</td>');
                }else{
                	html.push('<td width="7%">未确定</td>');
                }
                if(m.operatorPrice){
                    html.push('<td width="7%" class="tr Inquiry_c">¥'+milliFormat(m.operatorPrice,2)+'</td>');//计调报价——成本报价  
                }else{
                	html.push('<td width="7%" class="tr"><span class="tdred">待报价</span></td>');  
                }
                
                
                
                if(m.estimateStatus>=3){
                	if(m.outPrice!=null){
                		html.push('<td  width="10%"><input style="text-align:right;"  onfocus="outPriceOnClick(this)" onblur="outPriceOnblur(this,'+m.id+')"  type="text" class="rmbp17" name="" value='+milliFormat(m.outPrice,2)+' /></td>');
                	}else{
                		html.push('<td  width="10%"><input style="text-align:right;"  onfocus="outPriceOnClick(this)" onblur="outPriceOnblur(this,'+m.id+')"  type="text" class="rmbp17" name="" value="待报价" /></td>');
                	}
                }else{
                	html.push('<td width="10%"  class="tr"><span class="tdred">待报价</span></td>');
                }
                
                
                if(m.outPrice!=null&&m.operatorPrice!=null&&m.operatorPrice!="null"){
                	html.push('<td width="10%" class="tr"><span class="tdred" id="settlementPrice'+m.id+'">¥'+milliFormat((m.outPrice-m.operatorPrice),2)+'</span></td>');
                }else{
                	html.push('<td width="10%" class="tr"><span class="tdred" id="settlementPrice'+m.id+'">待报价</span></td>');
                }
                
                html.push('<input type="hidden" id="hid_outPrice'+m.id+'" value='+m.outPrice+' />');
                html.push('<input type="hidden" id="hid_operatorPrice'+m.id+'" value='+m.operatorPrice+' />');
                if(pstatus==0){
                	namestr = '已取消';
                }else{
	                if(m.estimateStatus==2){
	           			namestr = '已报价';
	           		}else if(m.estimateStatus==3){
	           			namestr = '已确定报价';
	           		}else if(m.estimateStatus==1){
	           			namestr = '待报价';
	           		}else if(m.estimateStatus==0){
	           			namestr = '已取消';
	           		}else if(m.estimateStatus==4){
	           			namestr = '已发产品';
	           		}else if(m.estimateStatus==5){
	           			namestr = '已生成订单';
	           		}else if (m.estimateStatus == 6) {
	    				namestr = '待分配';
	    			} else{
	           			namestr = m.estimateStatus;
	           		}
                }
           		
           		html.push('<td width="8%">'+namestr+'</td>');
           		
           		html.push('<td class="p0" width="7%"><dl class="handle"><dt><img src="'+contextStaticPath+'/images/handle_cz.png" title="操作"/></dt><dd><p><span></span>');
           		html.push('<a  href="'+contextPath+'/eprice/manager/project/recordinfo/'+m.id+'">详情</a><br>');
           		if(pstatus!=0){
	           		if(m.estimateStatus==1 || m.estimateStatus==2){
	           			if(m.estimateStatus==2){
	           				html.push('<a  href="'+contextPath+'/eprice/manager/project/recordinfoChange/'+m.id+'">确认报价</a><br>');
	           			}
	//           			if(m.type=="1"){
	           				html.push('<a href="'+contextPath+'/eprice/manager/project/onceagain/'+m.type+'/'+m.id+'">再次询价</a><br/>');
	//           			}else{
	//           				html.push('<a href="'+contextPath+'/eprice/manager/project/onceagain/b/'+m.id+'">再次询价</a><br/>');
	//           			}
	           		}
                // 当计调报价被采纳时，才会允许下载计调附件
//                if((m.estimateStatus==3 || m.estimateStatus==4 ) && m.acceptAoperatorReply!=null && m.acceptAoperatorReply.tripFile!=null){
//                    html.push('<a  href="'+contextPath+'/eprice/manager/ajax/project/file/download/'+m.acceptAoperatorReply.tripFile.id+'">下载报单价</a></p></dd></dl></td></tr>');
//                }
           		}
                    return html.join("");
			}
		},
		
		service : {
			
			/**
			 * 生成询价项目下属的询价记录列表元素——询价记录 jquery dhtml
			 * @author lihua.xu
			 * @时间 2014年9月26日
			 * @param {} m 询价记录对象
			 * @return {jquery dhtml}
			 */
			record : function(m,i,createUser,pstatus){
				var $html = $(erecordList.dao.record(m,i,createUser,pstatus));
				
				var d = $($html).find("div.inquiry_mouse");
				if(d.length>0){
					$(d).parent().hover(function(){
						$(this).find("div.inquiry_mouse").show();
					},function(){
						$(this).find("div.inquiry_mouse").hide();
					});
				}
				
				$($html).find('.handle').hover(function() {
					if(0 != $(this).find('a').length){
						$(this).addClass('handle-on');
						$(this).find('dd').addClass('block');
					}
				},function(){
					$(this).removeClass('handle-on');
					$(this).find('dd').removeClass('block');
				});
				
				return $html;
			},
			
			/**
			 * 获取列表数据成功后，初始化列表
			 * @author lihua.xu
			 * @时间 2014年9月25日
			 * @param {} json
			 */
			getRecordListSuccess : function(json){
				var list = json.page.result;
				var createUser = json.createUser;
				var pstatus = json.project.estimateStatus;
				var $p = $("#eprice-recode-list-id");
				$($p).children().remove();
				for(var i = 0,len = list.length; i<len; i++){
					$p.append(erecordList.service.record(list[i],i+1,createUser,pstatus));
				}
				
				$("#eprice-list-count-id").html(json.page.totalCount);
				$("#eprice-record-list-page-id").pageDom({	
													eventFun : erecordList.ajax.getRecordList,
													count : json.page.totalCount,
													pageNo : json.page.pageNo,
													pageSize : json.page.pageSize
													});
			}
		},
		
		ajax : {
			
			/**
			 * 获取指定询价项目id下属的询价记录分页列表
			 * @author lihua.xu
			 * @时间 2014年9月30日
			 * @param {} pn
			 */
			getRecordList : function(pn, ps){
				pn = pn || 1;
				ps = ps || 10;
				var the_param = $("#eprice-search-form-id").serialize();
				$.ajax({
					type : "POST",
					url : contextPath+"/eprice/manager/ajax/project/recordlist/"+project.id+"?pageSize=" + ps + "&pageNo="+pn,
					data : the_param,
					dataType : "text",
					success : function(html){
						var json;
						try{
							json = $.parseJSON(html);
						}catch(e){
							json = {res:"error"};							
						}
						
						if(json.res=="success"){
							erecordList.service.getRecordListSuccess(json);
						}else if(json.res=="data_error"){
							jBox.tip("输入数据不正确", 'error');
						}else{
							jBox.tip("请检查您输入的参数,或稍后重试", 'error');
						}
					}
				
				});
			}
		
		}
};


function doubleSub(num1,num2){
	return Math.round((parseFloat(num1)*1000 - parseFloat(num2)*1000))/1000;
}

function doubleAdd(num1,num2){
	return Math.round((parseFloat(num1)*1000 + parseFloat(num2)*1000))/1000;
}