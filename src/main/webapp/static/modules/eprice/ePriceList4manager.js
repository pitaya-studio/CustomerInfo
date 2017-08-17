$(function(){
	
	$("[name=operatorUid]" ).comboboxInquiry();
	$("[name=custId]" ).comboboxInquiry();
	$("[name=salerId]" ).comboboxInquiry();

	eplist4manager.ajax.getProjectList();
	
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
		
		eplist4manager.ajax.getProjectList();
		
		
		$("span[class=moneyFormat]").each(function(){
			var t = false;
			var value = $(this).html();
			var value1 = value.replace("¥", "") ;
			if(value!=value1){
				t = true;
			}
			value = milliFormat(value1.trim(),2);
			if(t){
				value = "¥"+value;
			}
			$(this).html(value);
			
		});
		
		
	});
	
	$("#eprice-search-form-btn-id").click(function(){
		eplist4manager.ajax.getProjectList();
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


var eplist4manager = {
		dao : {
			
			/**
			 * 生成询价项目列表元素——询价项目 html string
			 * @author lihua.xu
			 * @时间 2014年9月25日
			 * @param {} m 询价项目对象
			 * @return {html string}
			 */
			project : function(m){
				var html = new Array();
//					if(m.estimateStatus==1){
//						html.push('<tr pid="'+m.id+'"><td class="inquiry_new tc">'+m.id+'<br><br></td><td class="tc">'+m.lastBaseInfo.salerName+'</td><td class="tc">'+m.lastBaseInfo.customerName+'</td>');	
//					}else{
						html.push('<tr pid="'+m.id+'" pstatus="'+m.estimateStatus+'"><td class="tc">'+m.id+'<br><br></td><td class="tc">'+m.userName+'</td><td class="tc">'+m.lastBaseInfo.customerName+'</td>');
//					}
					if ($("#agentinfo_visibility") && $("#agentinfo_visibility").val() == 1) {						
						html.push('<td class="tc">'+m.lastBaseInfo.contactPerson+'</td>');
					}
					if(m.type==1){
						html.push('<td class="tc" >单团</td>');
					}else if(m.type==4){
						html.push('<td class="tc" >大客户</td>');
					}else if(m.type==3){
						html.push('<td class="tc" >游学</td>');
					}else if(m.type==5){
						html.push('<td class="tc" >自由行</td>');
					}else if(m.type==7){
						html.push('<td class="tc">机票</td>');
					}
					html.push('<td class="tc">'+$.trekizFormatDate({time:m.lastEstimatePriceTime,pattern:"yyyy-MM-dd"})+'</td>');
					if(m.lastOperatorGivenTime!=null){
						html.push('<td class="tc">'+$.trekizFormatDate({time:m.lastOperatorGivenTime,pattern:"yyyy-MM-dd"})+'</td>');
					}else{
						html.push('<td class="tc"><span class="tdred">待报价</span></td>');
					}
                
               		html.push('<td class="tc">'+m.estimatePriceSum+'</td>');
                
               		var namestr;
                    if(m.estimateStatus==2){
               			namestr = '已报价';
               		}else if(m.estimateStatus==3){
               			namestr = '确定报价';
               		}else if(m.estimateStatus==6){
               			namestr = '待分配';
               		}else if(m.estimateStatus==1){
               			namestr = '待报价';
               		}else if(m.estimateStatus==0){
               			namestr = '已取消';
               		}else if(m.estimateStatus==4){
               			namestr = '已发产品';
               		}else if(m.estimateStatus==5){
               			namestr = '已生成订单';
               		}else{
               			namestr = m.estimateStatus;
               		}
               		
               		html.push('<td class="tc">'+namestr+'</td>');
               		if( m.type!=7){// 单团，展开表格项
                   		html.push('<td class="tc"><a  class="team_a_click" name="expand-or-packup" href="javascript:void(0)">展开</a>');
                   		html.push('<tr class="activity_team_top1" style="display:none" id="child1"><td class="team_top" colspan="10">');
                   		html.push('<table style="margin:0 auto;" class="table activitylist_bodyer_table" id="teamTable"><thead><tr>');
                   		html.push('<th width="4%">序号</th><th width="9%">询价日期</th><th width="9%">指定计调</th><th width="7%">确认报价计调</th>');
                   		html.push('<th width="9%">报价日期</th> <th width="9%">预计出团日期</th><th width="9%">境外停留天数</th>');
                        html.push('<th width="8%">线路国家</th><th width="10%">成本价</th><th width="10%">外报价</th>');
                        html.push('<th width="8%">报价状态</th><th width="7%">操作</th></tr></thead></table>');
                        html.push('<div class="table_activity_scroll"><table name="eprice-record-table" class="table activitylist_bodyer_table table-mod2-group"><tbody>');
                        html.push('<tr ><td colspan="13" class="tc"><a href="'+contextPath+'/eprice/manager/project/erlist/'+m.id+'">点击更多询价记录</a></td></tr></tbody></table></div></td> </tr>');
               		}else if( m.type==7){	// 机票展开表格项	
               			html.push('<td class="tc"><a  class="team_a_click" name="expand-or-packup" href="javascript:void(0)">展开</a>');
                   		html.push('<tr class="activity_team_top1" style="display:none" id="child1"><td class="team_top" colspan="10">');
                   		html.push('<table style="margin:0 auto;" class="table activitylist_bodyer_table" id="teamTable"><thead><tr>');
                   		html.push('<th width="4%">序号</th><th width="7%">询价日期</th><th width="7%">计调员</th><th width="7%">报价日期</th> <th width="7%">出发日期</th><th width="7%">航班类型</th>');
                        html.push('<th width="9%">出发城市</th><th width="9%">目的地城市</th><th width="10%">成本价</th><th width="10%">外报价</th>');
                        html.push('<th width="8%">报价状态</th><th width="7%">操作</th></tr></thead></table>');
                        html.push('<div class="table_activity_scroll"><table name="eprice-record-table" class="table activitylist_bodyer_table table-mod2-group"><tbody>');
               		} 
                    
                return html.join("");
			},
			
			/**
			 * 生成询价项目下属巡检记录列表元素——询价记录 html string
			 * @author lihua.xu
			 * @时间 2014年9月25日
			 * @param {} m 询价记录对象
			 * @return {html string}
			 */
			eprecord : function(m,xc,bj,i,pstatus){
				var html = new Array();
					html.push('<tr><td width="4%">'+i+'</td><td width="9%">'+$.trekizFormatDate({time:m.modifyTime,pattern:"yyyy-MM-dd"})+'</td>');
					
					/* 获取指定计调 start */
					var userNameArr = [];
					userNameArr = getJsonUserName(m.aoperatorUserJson, userNameArr);
					userNameArr = getJsonUserName(m.toperatorUserJson, userNameArr);
					var userNames = userNameArr.join(",");
					if (userNames.length > 10) {
						html.push('<td width="9%" title="' + userNames + '">' + userNames.substring(0, 10) + '……</td>');//指定计调
					} else {
						html.push('<td width="9%">' + userNames + '</td>');//指定计调
					}
					/* 获取指定计调 end */
					
					var namestr = "";
					// (只在确定报价时，出现被选中计调员)
					if( m.estimateStatus==3 || m.estimateStatus==4 || m.estimateStatus==5){
						
						if(m.acceptAoperatorReply!=null){
							namestr+= m.acceptAoperatorReply.operatorUserName+"<br/>";
						}
						if(m.acceptToperatorReply!=null){
							namestr+= m.acceptToperatorReply.operatorUserName+"<br/>";
						}
						html.push('<td width="7%">'+namestr+'</td>');
						
					}else{
						html.push('<td width="7%"></td>');//计调
					}
					if(m.estimateStatus<=1 || m.estimateStatus==6){
						namestr = '<span>待报价</span>';
					}else{
						if(m.lastAoperatorPriceTime>=m.lastToperatorPriceTime){
							namestr = m.lastAoperatorPriceTime;
						}else{
							namestr = m.lastToperatorPriceTime;
						}
						namestr = $.trekizFormatDate({time:namestr,pattern:"yyyy-MM-dd"});
					}
               		html.push('<td width="9%">'+namestr+'</td>');//报价时间
               		
                    html.push('<td width="9%">'+$.trekizFormatDate({time:m.admitRequirements.dgroupOutDate,pattern:"yyyy-MM-dd"})+'</td>');//预计出团日期
                    html.push('<td width="9%">'+m.admitRequirements.outsideDaySum+'天'+m.admitRequirements.outsideNightSum+'晚</td>');//境外停留天数
                    var countryNames = m.admitRequirements.travelCountry;
                    if (countryNames.length > 10) {
						html.push('<td width="8%" title="' + countryNames + '">' + countryNames.substring(0, 10) + '……</td>');//线路国家
					} else {
						html.push('<td width="8%">'+countryNames+'</td>');//线路国家
					}
                   
                    // 临时判断,没有机票计调
                    if(m.operatorPrice&&m.outPrice){
                    	b=true;
                    }
                    
                    if(m.estimateStatus==3){
                    	if(m.operatorPrice){
                    		html.push('<td width="10%" class="tr Inquiry_c">¥'+milliFormat(m.operatorPrice,2)+'</td>');//计调报价——成本报价  
                    	}else{
                    		var aop ;
                    		if(m.acceptAoperatorReply){
                    			aop = m.acceptAoperatorReply.priceDetail;
                    		}else{
                    			aop = "";
                    		}
                    		var top;
                    		if(m.acceptToperatorReply){
                    			top = m.acceptToperatorReply.priceDetail;
                    		}else{
                    			top = "";
                    		}
                            html.push('<td width="10%" class="tr Inquiry_c" title='+aop+';'+top+' >确认报价</td>');//计调报价——成本报价  
                    	}
					}else{
                    	 if(m.estimateStatus==2){
                    		 html.push('<td width="10%"><span class="tdred"> 待确认报价</span></td>');
                    	 }else{
                    		 html.push('<td width="10%"><span class="tdred"> 待报价</span></td>');
                    	 }
                    }
                    
                     if(m.estimateStatus>=3 && m.estimateStatus<6){
                    	 if(m.outPrice){
                    		 html.push('<td  width="10%"><input style="text-align:right;" onfocus="outPriceOnClick(this)" onblur="outPriceOnblur(this,'+m.id+')"  type="text" class="rmbp17" name="" value='+milliFormat(m.outPrice,2)+' /></td>');
                    	 }else{
                    		 html.push('<td  width="10%"><input style="text-align:right;"  onfocus="outPriceOnClick(this)" onblur="outPriceOnblur(this,'+m.id+')"  type="text" class="rmbp17" name="" value="待报价" /></td>');
                    	 }
                     }else{
                    	 html.push('<td width="10%"><span class="tdred"> 待报价</span></td>');
                     }
                     
                    html.push('<td width="8%"><input type="hidden" id="hid_outPrice'+m.id+'" value='+m.outPrice+' />');
                    html.push('<input type="hidden" id="hid_operatorPrice'+m.id+'" value='+m.operatorPrice+' />');
                    
                    if(pstatus==0){
                    	namestr = '已取消';
                    }else{
	                    if(m.estimateStatus==2){
	               			namestr = '已报价';
	               		}else if(m.estimateStatus==3){
	               			namestr = '确定报价';
	               		}else if(m.estimateStatus==6){
	               			namestr = '待分配';
	               		}else if(m.estimateStatus==1){
//	               			if (m.status==6) {
//	               				namestr = '待分配';
//	                         } else {
//	                        	 namestr = '待报价';
//	                         }
	               			namestr = '待报价';
	               		}else if(m.estimateStatus==0){
	               			namestr = '已取消';
	               		}else if(m.estimateStatus==4){
	               			namestr = '已发产品';
	               		}else if(m.estimateStatus==5){
	               			namestr = '已生成订单';
	               		}else{
	               			namestr = m.estimateStatus;
	               		}
                    }
               		
               		html.push(namestr+'</td>');
               		
               		html.push('<td class="p0" width="7%"><dl class="handle"><dt><img src="'+contextStaticPath+'/images/handle_cz.png" title="操作"/></dt><dd><p><span></span>');
               		html.push('<a  href="'+contextPath+'/eprice/manager/project/recordinfo/' + m.id + '?isOpManager=true">详情</a><br>');
               		if (pstatus != 0) {
               			if (m.estimateStatus == 1 || m.estimateStatus == 2 || m.estimateStatus == 6) {
	               			html.push('<a href="' + contextPath + '/eprice/manager/project/distributeOp/' + m.id + '" target="_blank">分配计调</a><br/>');
	               		}
               		}
                    
                    return html.join("");
                        
			},
			/**
			 * 生成询价项目下属巡检记录列表元素——询价记录 html string
			 * @author lihua.xu
			 * @时间 2014年9月25日
			 * @param {} m 询价记录对象
			 * @return {html string}
			 */
			eprecordtraffic : function(m,i,pstatus){
				var html = new Array();
					html.push('<tr><td width="4%">'+i+'</td><td width="7%">'+$.trekizFormatDate({time:m.modifyTime,pattern:"yyyy-MM-dd"})+'</td>');
					var namestr = "";
					// (只在确定报价时，出现被选中计调员)
					if( m.estimateStatus==3 || m.estimateStatus==4 || m.estimateStatus==5){
						
						if(m.acceptToperatorReply!=null){
							namestr+= m.acceptToperatorReply.operatorUserName+"<br/>";
						}
						html.push('<td width="7%">'+namestr+'</td>');//计调
						
					}else{
						html.push('<td width="7%"></td>');//计调
					}
					
					if(m.estimateStatus<=1 || m.estimateStatus==6){
						namestr = '<span>待报价</span>';
					}else{
						if(m.lastAoperatorPriceTime>=m.lastToperatorPriceTime){
							namestr = m.lastAoperatorPriceTime;
						}else{
							namestr = m.lastToperatorPriceTime;
						}
						namestr = $.trekizFormatDate({time:namestr,pattern:"yyyy-MM-dd"});
					}
               		html.push('<td width="7%">'+namestr+'</td>');//报价时间
               		var namestr2 = "";
               		if(m.lastToperatorStartOutTime){
               			namestr2 = m.lastToperatorStartOutTime;
               			namestr2 = $.trekizFormatDate({time:namestr2,pattern:"yyyy-MM-dd"});
               			html.push('<td width="7%">'+namestr2+'</td>');//出发日期
               		}else{
               			html.push('<td width="7%">未确定</td>');//出发日期
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
	                	html.push('<td width="9%">'+m.startCity+'</td>');
	                }else{
	                	html.push('<td width="9%">未确定</td>');
	                }
	                // 目的地城市
	                if(m.startCity){
	                	html.push('<td width="9%">'+m.endCity+'</td>');
	                }else{
	                	html.push('<td width="9%">未确定</td>');
	                }
                    if(m.estimateStatus==3){
                    	if(m.operatorPrice){
                    		 html.push('<td width="10%" class="tr Inquiry_c">¥'+milliFormat(m.operatorPrice,2)+'</td>');//计调报价——成本报价 
                    	}else{
                            html.push('<td width="10%" class="tr Inquiry_c" title='+m.acceptToperatorReply.priceDetail+' >确认报价</td>');//计调报价——成本报价  
                    	}
					}else{
                    	if(m.estimateStatus==2){
                    		html.push('<td width="10%"><span class="tdred">待确认报价</span></td>');
                    	}else{
                    		html.push('<td width="10%"><span class="tdred">待报价</span></td>');
                    	}
                    }
                    
                    
                    if(m.estimateStatus>=3 && m.estimateStatus<6){
                    	if(m.outPrice!=null){
                    		html.push('<td  width="10%"><input style="text-align:right;"  onfocus="outPriceOnClick(this)" onblur="outPriceOnblur(this,'+m.id+')"  type="text" class="rmbp17" name="" value='+milliFormat(m.outPrice,2)+' /></td>');
                    	}else{
                    		html.push('<td  width="10%"><input style="text-align:right;"  onfocus="outPriceOnClick(this)" onblur="outPriceOnblur(this,'+m.id+')"  type="text" class="rmbp17" name="" value="待报价" /></td>');
                    	}
                    }else{
                    	html.push('<td width="10%"><span class="tdred">待报价</span></td>');
                    }
                    
                    html.push('<td width="8%"><input type="hidden" id="hid_outPrice'+m.id+'" value='+m.outPrice+' />');
                    html.push('<input type="hidden" id="hid_operatorPrice'+m.id+'" value='+m.operatorPrice+' />');
                    if(pstatus==0){
                    	namestr = '已取消';
                    }else{
	                    if(m.estimateStatus==2){
	               			namestr = '已报价';
	               		}else if(m.estimateStatus==3){
	               			namestr = '确定报价';
	               		}else if(m.estimateStatus==6){
	               			namestr = '待分配';
	               		}else if(m.estimateStatus==1){
//	               			if (m.status==6) {
//	               				namestr = '待分配';
//	                         } else {
//	                        	 namestr = '待报价';
//	                         }
	               			namestr = '待报价';
	               		}else if(m.estimateStatus==0){
	               			namestr = '已取消';
	               		}else if(m.estimateStatus==4){
	               			namestr = '已发产品';
	               		}else if(m.estimateStatus==5){
	               			namestr = '已生成订单';
	               		}else{
	               			namestr = m.estimateStatus;
	               		}     	
                    }
               		
               		html.push(namestr+'</td>');
               		
               		html.push('<td class="p0" width="7%"><dl class="handle"><dt><img src="'+contextStaticPath+'/images/handle_cz.png" title="操作"/></dt><dd><p><span></span>');
               		html.push('<a  href="'+contextPath+'/eprice/manager/project/recordinfo/' + m.id + '?isOpManager=true">详情</a><br>');
               		if (pstatus != 0) {
	               		if (m.estimateStatus == 1 || m.estimateStatus == 2 || m.estimateStatus == 6) {
	               			html.push('<a href="' + contextPath + '/eprice/manager/project/distributeOp/' + m.id + '" target="_blank">分配计调</a><br/>');
	               		}
               		}
                    return html.join("");
                        
			}
		},
		
		
		service : {
			
			/**
			 * 生成询价项目列表元素——询价项目 jquery dhtml
			 * @author lihua.xu
			 * @时间 2014年9月25日
			 * @param {} m 询价项目对象
			 * @return {jquery dhtml}
			 */
			project : function(m){
				var $html = $(eplist4manager.dao.project(m));
				
				$($html).find("a[name=expand-or-packup]").click(function(){
					var h = $(this).html();
					var ptr = $(this).parents("tr[pid]");
					var ptrStatus = $(this).parents("tr[pstatus]");
					var ntr = $(ptr).next("tr.activity_team_top1");
				
					if(h=="展开"){
						$(this).html("收起");
						$(ntr).show();
						
						var pid = $(ptr).attr("pid");
						var pstatus = $(ptrStatus).attr("pstatus");
						$eprd = $(ntr).find("table[name=eprice-record-table]");
						eplist4manager.ajax.getEprecordList($eprd,pid,pstatus);
					}else{
						$(this).html("展开");
						$(ntr).hide();
					}
				});
				
				return $html;
			},
			
			
			
			/**
			 * 生成询价项目下属的询价记录列表元素——询价记录 jquery dhtml
			 * @author lihua.xu
			 * @时间 2014年9月26日
			 * @param {} m 询价记录对象
			 * @return {jquery dhtml}
			 */
			eprecord : function(m,xcList,bjList,orderNum,pstatus){
				var $html = $(eplist4manager.dao.eprecord(m,xcList,bjList,orderNum,pstatus));
				
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
			eprecordtraffic : function(m,i,pstatus){
				var $html = $(eplist4manager.dao.eprecordtraffic(m,i,pstatus));
				
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
			getProjectListSuccess : function(json){
				var list = json.page.result;
				var $p = $("#eprice-project-list-id>tbody");
				$($p).children().remove();
				for(var i = 0,len = list.length; i<len; i++){
					$p.append(eplist4manager.service.project(list[i]));
				}
				
				$("#eprice-list-count-id").html(json.page.totalCount);
				$("#eprice-project-list-page-id").pageDom({	
													eventFun : eplist4manager.ajax.getProjectList,
													count : json.page.totalCount,
													pageNo : json.page.pageNo,
													pageSize : json.page.pageSize
													});//eplist4manager.ajax.getProjectList,json.page.totalCount,json.page.pageNo,json.page.pageSize);
			},
			
			/**
			 * 获取列表数据成功后，初始化列表
			 * @author lihua.xu
			 * @时间 2014年9月25日
			 * @param {} json
			 */
			getEprecordListSuccess : function(json,pstatus){
				var list = json.page.result;
				var xcFileIdsList = json.xcFilesIds;
				var bjFilesIdsList = json.bjFilesIds;
				var $p = json.dom;
				$($p).children().remove();
				var type ="";
				for(var i = 0,len = list.length; i<len; i++){
					if(i>2){
						break;
					}
					// 通过type，选择显示机票询价记录或单团（可以含机票）询价记录
					type = list[i].type;
					if(list[i].type==1||list[i].type==3||list[i].type==5||list[i].type==4){
						$p.append(eplist4manager.service.eprecord(list[i],xcFileIdsList[i],bjFilesIdsList[i],i+1,pstatus));
					}else if(list[i].type==7){
						$p.append(eplist4manager.service.eprecordtraffic(list[i],i+1,pstatus));
					}
				}
				if(list.length>2){
					
					if(type!=7){
						$($p).append('<tr><td colspan="12" class="tc"><a href="'+contextPath+'/eprice/manager/project/erlist/'+list[0].pid+'" target="_blank">点击更多询价记录</a></td></tr>');
					}else{
						$($p).append('<tr><td colspan="13" class="tc"><a href="'+contextPath+'/eprice/manager/project/erlist/'+list[0].pid+'" target="_blank">点击更多询价记录</a></td></tr>');
					}
				}
//				if(json.page.pageCount>1){
//					$($p).append('<tr><td colspan="12" class="tc"><a href="'+contextPath+'/eprice/manager/project/erlist/'+m.id+'">点击更多询价记录</a></td></tr>');
//				}
			}
		},
		
		ajax : {
			
			getProjectList : function(pn, ps){
				pn = pn || 1;
				ps = ps || 10;
				var the_param = $("#eprice-search-form-id").serialize();

				ajaxStart();
				$.ajax({
					type : "POST",
					url : contextPath+"/eprice/manager/ajax/project/plist4manager?pageSize=" + ps + "&pageNo="+pn,
					data : the_param,
					dataType : "text",
					success : function(html){
						ajaxStop();
						var json;
						try{
							json = $.parseJSON(html);
						}catch(e){
							json = {res:"error"};							
						}
						
						if(json.res=="success"){
							eplist4manager.service.getProjectListSuccess(json);
						}else if(json.res=="data_error"){
							jBox.tip("输入数据不正确", 'error');
						}else{
							jBox.tip("系统错误", 'error');
						}
					}
				
				});
			},
			
			/**
			 * 获取指定询价项目id下属的询价记录分页列表
			 * @author lihua.xu
			 * @时间 2014年9月30日
			 * @param {} dom
			 * @param {} pid
			 * @param {} pn
			 */
			getEprecordList : function(dom,pid,pstatus,pn){
				pn = pn || 1;
				
				$.ajax({
					type : "POST",
					url : contextPath+"/eprice/manager/ajax/project/recordlistForManager/"+pid+"?pageSize=10&pageNo="+pn,
					data : "",
					dataType : "text",
					success : function(html){
						var json;
						try{
							json = $.parseJSON(html);
						}catch(e){
							json = {res:"error"};							
						}
						
						if(json.res=="success"){
							json.dom = dom;
							eplist4manager.service.getEprecordListSuccess(json,pstatus);
						}else if(json.res=="data_error"){
							jBox.tip("输入数据不正确", 'error');
						}else{
							jBox.tip("系统错误", 'error');
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

function getJsonUserName(userJsonStr, userNameArr) {
	if (userJsonStr) {
		var userArr = eval(userJsonStr);
		for (var i = 0; i < userArr.length; i++) {
			var userObj = userArr[i];
			if ($.inArray(userObj.userName, userNameArr) == -1) {
				userNameArr.push(userObj.userName);
			}
		}
	}
	return userNameArr;
}
function exportExcel(){
	var listSearchForm = $("#eprice-search-form-id").serialize();
	$("#exportExcel").attr("href",contextPath+"/eprice/manager/project/exportManagerEstimateExcel?lsf=listSearchForm");
};

