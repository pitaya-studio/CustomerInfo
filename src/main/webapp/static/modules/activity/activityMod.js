function mod(productId,ctx,activityKind,obj4class,requiredStraightPrice){

	var groupOpenDateArray = new Array();

	$("input[name='groupOpenDate']").each(function(){
		groupOpenDateArray.push($(this).val());
	});

	groupOpenDateArray.sort();
	for (var i=0;i<groupOpenDateArray.length-1;i++){
		if(groupOpenDateArray[i] == groupOpenDateArray[i+1]){
			top.$.jBox.info("同一产品下出团日期不能重复", "警告");
			return;
		}
	}

		var submit_times = 0;
		//批发商上架权限
		var shelfRightsStatus = $("#shelfRightsStatus").val();
		/**
		 * 校验团号是否进行过修改
		 */
		var isGroupCodeModified = false;
		$("#modTable").find("input[name='groupCode']").each(function(index,obj){
			//alert($(obj).val());
			
			//alert($(obj).next().next().val());

			var groupCodeOld = $(obj).val();
			var groupCodeNew = $(obj).next().next().val();
			if(groupCodeNew==undefined){
				groupCodeNew = $(obj).next().val();
			}
			
			if(groupCodeOld!=groupCodeNew){
				isGroupCodeModified = true;
			}
			
			
		});
		
		if(isGroupCodeModified){
			
	    	/**
	    	 * 对应需求  c460 
	    	 * 团期进行修改操作时给出提示信息
	    	 */
			$.jBox.confirm("团号修改后该团期下订单数据、财务数据、审批数据对应的团号会相应变化，确认修改？", "提示", function(v, h, f){
		        if (v == 'ok') {
		        	//debugger;
		        	var keyVal= $("input[name^='groupCode']") ;  
		        	var flag = false;
		        	$.each(keyVal,function(index){
		        		if(keyVal[index].value.length == 0)
		        		{
		        			top.$.jBox.info("团号不能为空！", "警告");
		        			top.$('.jbox-body .jbox-icon').css('top','55px');
		        			flag=true
		        			return false
		        		}
		        	}); 
		        	if(flag == true)
		        		return false;
		        	
		        	//处理c451，c453 重复提交的问题
		        	$(obj4class).addClass("disableCss");
		        	
		        	//团期酒店房型拼接,同一团期内使用逗号隔开
		        	assembleGroupHotelAndHouseType();
		        	if(activityKind == 2){
						if (!saveOldGroupflag || !saveNewGroupflag) {
							top.$.jBox.info("请先保存第二步", "警告");
							//处理c451，c453 重复提交的问题
							$(obj4class).removeClass("disableCss");
							return;
						}
					}else {
						if (!secondStepSaveFlag) {
							top.$.jBox.info("请先保存第二步", "警告");
							//处理c451，c453 重复提交的问题
							$(obj4class).removeClass("disableCss");
							return;
						}
					}
		        	add_modproductinfo();
		        	add_groupsvalidator();
		        	add_modgroupsvalidator();
		        	var flag = validator1.form();
		        	var flagVisa = visaValidator();
		        	
		        	/*验证产品行程文件是否上传，如果没有，则不能提交，前端页面提示上传信息*/
		        	/*var introduction_doc_len = $("#introduction_name").siblings("li").length;
			if(introduction_doc_len == 0){
				$("#introduction_name").after("<label id='modIntroduction' class='error'>产品行程文件不可缺失！</label>");
				//$("#introduction_name").rules("add",{required:true,messages:{required:"必填信息"}});
				return false;
			}*/
		        	if(flagVisa) {
		        		if(flag){
		        			//remove_filevalidator();//此方法以前存在，但会引起无法提交保存的问题，因此注释该方法，注释后能提交保存
		        			var html = $("#modForm").serializeJson();
		        			var json = JSON.stringify(html);
		        			// 修改前的数据
		        			var beforeEditData =$("#modForm").find("input[name='idss']");
		        			//修改后的数据
		        			var afterEditData= $("#modTable").find("input[name='groupCode']");
		        			//新增的团期数据
		        			var groupCodes = $("#contentTable").find("input[name='groupCode']");
							//t1t2-v4 0518是否上架T1平台(修改前数据)
							var isT1 = $("#modForm").find("input[name='isT1']");
		        			var submitflag = false;
		        			var newArray = new Array();
		        			var  beforeEditDataArray =  new Array();
		        			var  afterEditDataArray=  new Array();
							var isT1DataArray = new Array();
		        			if(afterEditData || groupCodes){
								//t1t2-v4 0518遍历修改前所有团期是否上架到T1平台的状态
								$(isT1).each(function(index,obj) {
									var id = $(obj).attr("src");
									var isT1State = $(obj).attr("id");
									var groupCode = $(obj).attr("value");
									if(isT1State == 1 && saveGroupIds.indexOf(id) != -1){
										isT1DataArray.push({id:id,groupCode:groupCode});
									}
								});
		        				$(groupCodes).each(function(index,obj){
		        					var id = $(obj).attr("id");
		        					var input = $(obj).val();
		        					if($.trim(input+"")!="")
		        						newArray.push({id:id,value:input,productId:productId});
		        				});
		        				
		        				$(beforeEditData).each(function(index,obj){
		        					var id = $(obj).attr("id");
		        					var input = $(obj).val();
		        					if($.trim(input+"")!="")
		        						beforeEditDataArray.push({id:id,value:input,productId:productId});
		        				});
		        				
		        				$(afterEditData).each(function(index,obj){
		        					var id =  $(obj).attr("src");
		        					var input = $(obj).next().text();
		        					if($.trim(input+"")!="")
		        						afterEditDataArray.push({id:id,value:input,productId:productId});
		        				});
		        				
		        				var productArray = new Array();
		        				productArray.push({value:activityKind,productId:productId,productType:activityKind});
		        				
		        				
		        				var totalArray =  new Array();
		        				totalArray.push(newArray);
		        				totalArray.push(beforeEditDataArray);
		        				totalArray.push(afterEditDataArray);
		        				totalArray.push(productArray);
		        				var validateArray =  newArray.concat(afterEditDataArray);
		        				var array = new Array();
		        				$(validateArray).each(function(index,obj){
		        					array.push($(obj).attr("value"));
		        				});
		        				
		        				if(isRepeat(array))
		        				{
									window.parent.jBox.close();
		        					/*游轮产品团号可以重复*/
		        					if(activityKind!="10"){
		        						top.$.jBox.info("团号重复", "警告");
		        						//处理c451，c453 重复提交的问题
		        						$(obj4class).removeClass("disableCss");
		        						return false;
		        					}
		        					
		        				}
		        				$.ajax({
		        					type: "POST",
		        					url: ctx+"/activity/manager/groupCodeUpdateValidate",
		        					dataType:"json",
		        					cache:false,
		        					async: false,
		        					data:{jsonresult:JSON.stringify(totalArray)},
		        					success : function(result){
		        						//debugger;
		        						var tips = "";
		        						$(result).each(function(i,obj){
		        							var id = obj.id;
		        							var flag = obj.flag;
		        							if(flag=="false"){
		        								submitflag = true;
		        								$("#"+id).css("border-color","red");
		        								tips += "团号发生重复，请重新录入<br>";
		        								
		        								//处理c451，c453 重复提交的问题
		        								$(obj4class).removeClass("disableCss");
		        							}
		        						});
		        						if(tips!=""){
		        							top.$.jBox.info(tips, "警告", { width: 250, showType:"slide", icon: "info",draggable: "true" });
		        							top.$('.jbox-body .jbox-icon').css('top','55px');
		        						}							
		        						if(!submitflag) {	
		        							if(submit_times == 0) {
		        								submit_times++;
		        								var groupPriceFlag = $("#groupPriceFlag").val();
		        								if (groupPriceFlag  == "true") {
		        									groupPriceJosn();
		        								}
												//t1t2-v4 0518批发商上架权限，启用
												if (shelfRightsStatus == 0) {
													var len = isT1DataArray.length;
													if(len > 0) {
														//拼接已上架状态的团号
														var groupCodes = '',groupIds = '';
														$(isT1DataArray).each(function(index,obj){
															if(len == 1) {
																groupCodes = $(obj).attr("groupCode") + "团期";
																groupIds = $(obj).attr("id");
															}else {
																groupCodes += $(obj).attr("groupCode") + "团期,";
																groupIds += $(obj).attr("id") + ",";
															}
														});
														//删除团号集合最后一个逗号
														if(groupCodes.indexOf(",") != -1) {
															groupCodes = groupCodes.substring(0,groupCodes.length - 1);
															groupIds = groupIds.substring(0,groupIds.length - 1);
															changeconfimIsT1Off(groupCodes,groupIds,ctx,submit_times,productId, activityKind, delGroupIds, delOtherFileIds, requiredStraightPrice);
														}else {
															changeconfimIsT1Off(groupCodes,groupIds,ctx,submit_times,productId, activityKind, delGroupIds, delOtherFileIds, requiredStraightPrice);
														}
													}else{
														// QU-SDP-微信分销模块start 修改微信上传广告图片验证 yang.gao 2017-01-09
														submitModForm(productId, ctx, activityKind ,delGroupIds, delOtherFileIds, requiredStraightPrice);
//														$("#modForm").attr("action",ctx+"/activity/manager/modsave?proId="+productId+"&delGroupIds="+delGroupIds+"&delOtherFileIds="+delOtherFileIds);
//														$("#modForm").submit();
														// QU-SDP-微信分销模块end yang.gao 2017-01-09
													}
												}else {//t1t2-v4 0518批发商上架权限，禁用
													// QU-SDP-微信分销模块start 修改微信上传广告图片验证 yang.gao 2017-01-09
													submitModForm(productId, ctx, activityKind ,delGroupIds, delOtherFileIds, requiredStraightPrice);
//													$("#modForm").attr("action",ctx+"/activity/manager/modsave?proId="+productId+"&delGroupIds="+delGroupIds+"&delOtherFileIds="+delOtherFileIds);
//													$("#modForm").submit();
													// QU-SDP-微信分销模块end yang.gao 2017-01-09
												}
		        								//$("#modForm").attr("action",ctx+"/activity/manager/modsave?proId="+productId+"&delGroupIds="+delGroupIds+"&delOtherFileIds="+delOtherFileIds);
		        								//$("#modForm").submit();
		        							}			
		        						}
		        					}
		        				});
		        			}else{
		        				// QU-SDP-微信分销模块start 修改微信上传广告图片验证 yang.gao 2017-01-09
								submitModForm(productId, ctx, activityKind ,delGroupIds, delOtherFileIds, requiredStraightPrice);
//								$("#modForm").attr("action",ctx+"/activity/manager/modsave?proId="+productId+"&delGroupIds="+delGroupIds+"&delOtherFileIds="+delOtherFileIds);
//								$("#modForm").submit();
								// QU-SDP-微信分销模块end yang.gao 2017-01-09
		        			}
		        			
		        		}
		        		else{
		        			top.$.jBox.info("请先修改完错误再提交", "警告");
		        			//处理c451，c453 重复提交的问题
		        			$(obj4class).removeClass("disableCss");
		        		}
		        	}
	            }else if (v == 'cancel'){
	                
	            }
	        });
			
			
			
			
		}else{
        	//debugger;
        	var keyVal= $("input[name^='groupCode']") ;  
        	var flag = false;
        	$.each(keyVal,function(index){
        		if(keyVal[index].value.length == 0)
        		{
        			top.$.jBox.info("团号不能为空！", "警告");
        			top.$('.jbox-body .jbox-icon').css('top','55px');
        			flag=true
        			return false
        		}
        	}); 
        	if(flag == true)
        		return false;
        	
        	//处理c451，c453 重复提交的问题
        	$(obj4class).addClass("disableCss");
        	
        	//团期酒店房型拼接,同一团期内使用逗号隔开
        	assembleGroupHotelAndHouseType();

			if(activityKind == 2){
				if (!saveOldGroupflag || !saveNewGroupflag) {
					top.$.jBox.info("请先保存第二步", "警告");
					//处理c451，c453 重复提交的问题
					$(obj4class).removeClass("disableCss");
					return;
				}
			}else {
				if (!secondStepSaveFlag) {
					top.$.jBox.info("请先保存第二步", "警告");
					//处理c451，c453 重复提交的问题
					$(obj4class).removeClass("disableCss");
					return;
				}
			}
        	add_modproductinfo();
        	add_groupsvalidator();
        	add_modgroupsvalidator();
//        	add_filevalidator();
        	var flag = validator1.form();
        	var flagVisa = visaValidator();
        	
        	/*验证产品行程文件是否上传，如果没有，则不能提交，前端页面提示上传信息*/
        	/*var introduction_doc_len = $("#introduction_name").siblings("li").length;
	if(introduction_doc_len == 0){
		$("#introduction_name").after("<label id='modIntroduction' class='error'>产品行程文件不可缺失！</label>");
		//$("#introduction_name").rules("add",{required:true,messages:{required:"必填信息"}});
		return false;
	}*/
        	if(flagVisa) {
        		if(flag){
        			//remove_filevalidator();//此方法以前存在，但会引起无法提交保存的问题，因此注释该方法，注释后能提交保存
        			var html = $("#modForm").serializeJson();
        			var json = JSON.stringify(html);
        			// 修改前的数据
        			var beforeEditData =$("#modForm").find("input[name='idss']");
        			//修改后的数据
        			var afterEditData= $("#modTable").find("input[name='groupCode']");
        			//新增的团期数据
        			var groupCodes = $("#contentTable").find("input[name='groupCode']");
					//t1t2-v4 0518是否上架T1平台(修改前数据)
					var isT1 = $("#modForm").find("input[name='isT1']");
        			var submitflag = false;
        			var newArray = new Array();
        			var  beforeEditDataArray =  new Array();
        			var  afterEditDataArray=  new Array();
        			var isT1DataArray = new Array();
        			if(afterEditData || groupCodes){
						//t1t2-v4 0518遍历修改前所有团期是否上架到T1平台的状态
						$(isT1).each(function(index,obj) {
							var id = $(obj).attr("src");
							var isT1State = $(obj).attr("id");
							var groupCode = $(obj).attr("value");
							if(isT1State == 1 && saveGroupIds.indexOf(id) != -1){
								isT1DataArray.push({id:id,groupCode:groupCode});
							}
						});
        				$(groupCodes).each(function(index,obj){
        					var id = $(obj).attr("id");
        					var input = $(obj).val();
        					if($.trim(input+"")!="")
        						newArray.push({id:id,value:input,productId:productId});
        				});
        				
        				$(beforeEditData).each(function(index,obj){
        					var id = $(obj).attr("id");
        					var input = $(obj).val();
        					if($.trim(input+"")!="")
        						beforeEditDataArray.push({id:id,value:input,productId:productId});
        				});
        				
        				$(afterEditData).each(function(index,obj){
        					var id =  $(obj).attr("src");
        					var input = $(obj).next().text();
        					if($.trim(input+"")!="")
        						afterEditDataArray.push({id:id,value:input,productId:productId});
        				});
        				
        				var productArray = new Array();
        				productArray.push({value:activityKind,productId:productId,productType:activityKind});
        				
        				
        				var totalArray =  new Array();
        				totalArray.push(newArray);
        				totalArray.push(beforeEditDataArray);
        				totalArray.push(afterEditDataArray);
        				totalArray.push(productArray);
        				var validateArray =  newArray.concat(afterEditDataArray);
        				var array = new Array();
        				$(validateArray).each(function(index,obj){
        					array.push($(obj).attr("value"));
        				});
        				
        				if(isRepeat(array))
        				{	
        					/*游轮产品团号可以重复*/
        					if(activityKind!="10"){
        						top.$.jBox.info("团号重复", "警告");
        						//处理c451，c453 重复提交的问题
        						$(obj4class).removeClass("disableCss");
        						return false;
        					}
        					
        				}
        				$.ajax({
        					type: "POST",
        					url: ctx+"/activity/manager/groupCodeUpdateValidate",
        					dataType:"json",
        					cache:false,
        					async: false,
        					data:{jsonresult:JSON.stringify(totalArray)},
        					success : function(result){
        						//debugger;
        						var tips = "";
        						$(result).each(function(i,obj){
        							var id = obj.id;
        							var flag = obj.flag;
        							if(flag=="false"){
        								submitflag = true;
        								$("#"+id).css("border-color","red");
        								tips += "团号发生重复，请重新录入<br>";
        								
        								//处理c451，c453 重复提交的问题
        								$(obj4class).removeClass("disableCss");
        							}
        						});
        						if(tips!=""){
        							top.$.jBox.info(tips, "警告", { width: 250, showType:"slide", icon: "info",draggable: "true" });
        							top.$('.jbox-body .jbox-icon').css('top','55px');
        						}							
        						if(!submitflag) {	
        							if(submit_times == 0) {
        								submit_times++;
        								var groupPriceFlag = $("#groupPriceFlag").val();
        								if (groupPriceFlag  == "true") {
        									groupPriceJosn();
        								}
										//t1t2-v4 0518批发商上架权限，启用
										if (shelfRightsStatus == 0) {
											var len = isT1DataArray.length;
											if(len > 0) {
												//拼接已上架状态的团号
												var groupCodes = '',groupIds = '';
												$(isT1DataArray).each(function(index,obj){
													if(len == 1) {
														groupCodes = $(obj).attr("groupCode") + "团期";
														groupIds = $(obj).attr("id");
													}else {
														groupCodes += $(obj).attr("groupCode") + "团期,";
														groupIds += $(obj).attr("id") + ",";
													}
												});
												//删除团号集合最后一个逗号
												if(groupCodes.indexOf(",") != -1) {
													groupCodes = groupCodes.substring(0,groupCodes.length - 1);
													groupIds = groupIds.substring(0,groupIds.length - 1);
													changeconfimIsT1Off(groupCodes,groupIds,ctx,submit_times,productId, activityKind, delGroupIds, delOtherFileIds, requiredStraightPrice);
												}else {
													changeconfimIsT1Off(groupCodes,groupIds,ctx,submit_times,productId, activityKind, delGroupIds, delOtherFileIds, requiredStraightPrice);
												}
											}else{
												// QU-SDP-微信分销模块start 修改微信上传广告图片验证 yang.gao 2017-01-09
												submitModForm(productId, ctx, activityKind ,delGroupIds, delOtherFileIds, requiredStraightPrice);
//												$("#modForm").attr("action",ctx+"/activity/manager/modsave?proId="+productId+"&delGroupIds="+delGroupIds+"&delOtherFileIds="+delOtherFileIds);
//												$("#modForm").submit();
												// QU-SDP-微信分销模块end yang.gao 2017-01-09
											}
										}else {//t1t2-v4 0518批发商上架权限，禁用
											// QU-SDP-微信分销模块start 修改微信上传广告图片验证 yang.gao 2017-01-09
											submitModForm(productId, ctx, activityKind ,delGroupIds, delOtherFileIds, requiredStraightPrice);
//											$("#modForm").attr("action",ctx+"/activity/manager/modsave?proId="+productId+"&delGroupIds="+delGroupIds+"&delOtherFileIds="+delOtherFileIds);
//											$("#modForm").submit();
											// QU-SDP-微信分销模块end yang.gao 2017-01-09
										}
        								//$("#modForm").attr("action",ctx+"/activity/manager/modsave?proId="+productId+"&delGroupIds="+delGroupIds+"&delOtherFileIds="+delOtherFileIds);
        								//$("#modForm").submit();
        							}			
        						}
        					}
        				});
        			}else{
						//批发商上架权限，启用
						if (shelfRightsStatus == 0) {

						}else {

						}
						// QU-SDP-微信分销模块start 修改微信上传广告图片验证 yang.gao 2017-01-09
						submitModForm(productId, ctx, activityKind ,delGroupIds, delOtherFileIds, requiredStraightPrice);
//						$("#modForm").attr("action",ctx+"/activity/manager/modsave?proId="+productId+"&delGroupIds="+delGroupIds+"&delOtherFileIds="+delOtherFileIds);
//						$("#modForm").submit();
						// QU-SDP-微信分销模块end yang.gao 2017-01-09
        			}
        			
        		}
        		else{
        			top.$.jBox.info("请先修改完错误再提交", "警告");
        			//处理c451，c453 重复提交的问题
        			$(obj4class).removeClass("disableCss");
        		}
        	}
			
		}
		/*add_modproductinfo();
		add_groupsvalidator();
		add_modgroupsvalidator();
//		add_filevalidator();
		var flag = validator1.form();
		var flagVisa = visaValidator();*/
		//modify by ymx for weChat T2 upload Poster Picture at 2017.1.11
//		uploadPosterPic();
		
	}
	
	/**
	 * 组装团期酒店房型
	 */
	function assembleGroupHotelAndHouseType(){
		//原有团期
		$("#modTable").children("tbody").find("tr[class!=noteTr][class!=noteTr11][class!=display-none]").each(function(index, element){
			if($(element).find("td[name=hotelhouse]").size() > 0){
				//组装酒店字符串
				var groupHotelStr = "";
				$(element).find("td[name=hotelhouse]").find("input[name=groupHotel]").each(function(index2, element2){
					if(index2 > 0){
						groupHotelStr += ",";
					}
					groupHotelStr += $(element2).val();
				});
				//设置隐藏域的值，如果没有则追加
				if($(element).find("input[name=groupHotelStr]").size() > 0){
					$(element).find("input[name=groupHotelStr]").val(groupHotelStr);
				} else {
					var hotelHtml = "<input type='hidden' name='groupHotelStr' value='" + groupHotelStr + "'>";
					$(element).find("td[name=hotelhouse]").append(hotelHtml);
				}
				//组装房型字符串
				var groupHouseTypeStr = "";
				$(element).find("td[name=hotelhouse]").find("input[name=groupHouseType]").each(function(index2, element2){
					if(index2 > 0){
						groupHouseTypeStr += ",";
					}
					groupHouseTypeStr += $(element2).val();
				});
				//设置隐藏域的值，如果没有则追加
				if($(element).find("input[name=groupHouseTypeStr]").size() > 0){
					$(element).find("input[name=groupHouseTypeStr]").val(groupHouseTypeStr);
				} else {
					var houseTypeHtml = "<input type='hidden' name='groupHouseTypeStr' value='" + groupHouseTypeStr + "'>";
					$(element).find("td[name=hotelhouse]").append(houseTypeHtml);
				}
			}
		});
		//新增团期
		$("#contentTable").children("tbody").find("tr[class!=noteTr]").each(function(index, element){
			if($(element).find("td[name=hotelhouse]").size() > 0){
				//组装酒店字符串
				var groupHotelStr = "";
				$(element).find("td[name=hotelhouse]").find("input[name=groupHotel]").each(function(index2, element2){
					if(index2 > 0){
						groupHotelStr += ",";
					}
					groupHotelStr += $(element2).val();
				});
				//设置隐藏域的值，如果没有则追加
				if($(element).find("input[name=groupHotelStr]").size() > 0){
					$(element).find("input[name=groupHotelStr]").val(groupHotelStr);
				} else {
					var hotelHtml = "<input type='hidden' name='groupHotelStr' value='" + groupHotelStr + "'>";
					$(element).find("td[name=hotelhouse]").append(hotelHtml);
				}
				//组装房型字符串
				var groupHouseTypeStr = "";
				$(element).find("td[name=hotelhouse]").find("input[name=groupHouseType]").each(function(index2, element2){
					if(index2 > 0){
						groupHouseTypeStr += ",";
					}
					groupHouseTypeStr += $(element2).val();
				});
				//设置隐藏域的值，如果没有则追加
				if($(element).find("input[name=groupHouseTypeStr]").size() > 0){
					$(element).find("input[name=groupHouseTypeStr]").val(groupHouseTypeStr);
				} else {
					var houseTypeHtml = "<input type='hidden' name='groupHouseTypeStr' value='" + groupHouseTypeStr + "'>";
					$(element).find("td[name=hotelhouse]").append(houseTypeHtml);
				}
			}
		});
	}
	
	
function isRepeat(arr) {
   var hash = {};
   for(var i in arr) {
   	var tmp = arr[i].toString().toUpperCase();
       if(hash[tmp])
       {
           return true;
       }
       // 不存在该元素，则赋值为true，可以赋任意值，相应的修改if判断条件即可
       hash[tmp] = true;
    }
   return false;
}
//t1t2-v2平台上架功能
function platform(path,activityId,activityKind,obj) {
	var groupSelf = $(obj).parent().parent().parent().parent().parent();
	//团期id
	var groupId = groupSelf.find("input[name='groupid']").val();
	var title = '';
	$.ajax({
		type: "POST",
		url: path + "/activity/manager/checkPriceRecord",
		data:{groupId:groupId},
		success: function(result){
			if(result.flag){
				title = "上架失败，因调整同行价或直客价请重新调整策略";
			}else {
				title = "上架成功";
				//上架成功，修改上架状态为1
				$.ajax({
					type: "POST",
					url: path + "/activity/manager/confimIsT1On",
					data: {groupId:groupId},
					success: function (data) {
						if (data.flag) {
							//重新执行提交保存刷新页面
							mod(activityId,path,activityKind,obj);
						} else {
							//$.jBox.tip('操作失败,原因:' + data.msg, 'error');
							//return false;
						}
					}
				})
			}
			$.jBox.info(title,"提示",{
				buttons: {"关闭": "0"},
				submit:function(v,h,f)
				{
					if (v == '0') {
						return true;
					}
				}
			})
		}
	});
}
function changeconfimIsT1Off(groupCodes,groupIds,ctx,submit_times,productId, activityKind, delGroupIds, delOtherFileIds, requiredStraightPrice){
$.jBox.info(groupCodes + "已经上架旅游交易系统，修改将导致"
		+"产品下架是否确认?","提示",{
			buttons: {"关闭": "0", "确认": "1"},
			submit:function(v,h,f){
				if (v == '1') {
					$.ajax({
						type: "POST",
						url: ctx+"/activity/manager/confimIsT1Off",
						data: {groupIdsBuffer: groupIds},
						success: function (data) {
							if (data.flag) {
								// QU-SDP-微信分销模块start 修改微信上传广告图片验证 yang.gao 2017-01-09
								submitModForm(productId, ctx, activityKind ,delGroupIds, delOtherFileIds, requiredStraightPrice);
//								$("#modForm").attr("action",ctx+"/activity/manager/modsave?proId="+productId+"&delGroupIds="+delGroupIds+"&delOtherFileIds="+delOtherFileIds);
//								$("#modForm").submit();
								// QU-SDP-微信分销模块end yang.gao 2017-01-09
							} else {
								$.jBox.tip('操作失败,原因:' + data.msg, 'error');
								return false;
							}
						}
					})
				}
				if (v == '0') {
					submit_times = 0;
					$("#submitAndSave").removeClass("disableCss");
					return true;
				}
			},closed:function (){
				submit_times = 0;
				$("#submitAndSave").removeClass("disableCss");
			}
		}
)
}

//QU-SDP-微信分销模块start 修改微信上传广告图片验证 yang.gao 2017-01-09
function submitModForm(productId, ctx, activityKind ,delGroupIds, delOtherFileIds, requiredStraightPrice) {
//	var requiredStraightPrice = ${requiredStraightPrice}; // 是否拥有计调权限
	if (activityKind == 2 && requiredStraightPrice) {
		// 图片上传剪裁插件公共部分调用
		var base64_img = uploadPosterPicCommon();
		// 如果出现图片规格验证错误则返回false
		if (base64_img == "false") {
			return false;
		}
		
		// 验证是否上传图片(如果没有回显数据就是第一次上传，需要判断如果没有上传就用提示用默认图片)
		if($(".cropper-view-box").children().attr("src").indexOf("images/wechat") != -1) {
			$.jBox.confirm("尚未添加微信分销广告图片，确认发布吗?", "提示", function(v, h, f) {
				if (v == 'ok') {
					// 进行提交操作带微信广告图片
					submitFormWithWeixin (productId, delGroupIds, delOtherFileIds, base64_img);
				} else if (v == 'cancel') {
					// 取消提交按钮禁用效果
					$("#submitAndSave").attr("class","ydbz_x");
				}
			});
		} else {
			// 进行提交操作带微信广告图片
			submitFormWithWeixin(productId, delGroupIds, delOtherFileIds, base64_img);
		}
//		var validImg = $("input[name='distributionAdImg']").next(".batch-ol").children().eq(0).html(); // 判断是否有上传图片
//		// 如果没有上传图片做信息提示
//		if (validImg == "") {
//			$.jBox.confirm("尚未添加微信分销广告图片，确认发布吗?", "提示", function(v, h, f) {
//				if (v == 'ok') {
//					$("#modForm").attr("action",ctx+"/activity/manager/modsave?proId="+productId+"&delGroupIds="+delGroupIds+"&delOtherFileIds="+delOtherFileIds);
//					$("#modForm").submit();
//				} else if (v == 'cancel') {
//					$("#submitAndSave").attr("class","ydbz_x");
//				}
//			});
//		} else {
//			$("#modForm").attr("action",ctx+"/activity/manager/modsave?proId="+productId+"&delGroupIds="+delGroupIds+"&delOtherFileIds="+delOtherFileIds);
//			$("#modForm").submit();
//		}
	} else {
		// 不带微信广告图片提交
		$("#modForm").attr("action",ctx+"/activity/manager/modsave?proId="+productId+"&delGroupIds="+delGroupIds+"&delOtherFileIds="+delOtherFileIds);
		$("#modForm").submit();
	}
}

// 修改-数据form提交带微信图片
function submitFormWithWeixin (productId, delGroupIds, delOtherFileIds, base64_img) {
	var _ctx=$("#ctx").val();
	
	$.ajax({
		url: _ctx+"/activity/manager/uploadImgByBaseCode",
		type: 'POST',
		data: {image:base64_img},
		timeout : 10000, //超时时间设置，单位毫秒
		async: true,
		success: function (result) {
			if (result.msg == "success") {
				$("#modForm").attr("action", _ctx+"/activity/manager/modsave?proId="+productId+"&delGroupIds="+delGroupIds+"&delOtherFileIds="+delOtherFileIds + "&docId=" + result.data);
				$("#modForm").submit();
			} else {
				if (result.msg == "false-2") {
					top.$.jBox.info("请上传规定的格式图片并且大小不超过2M.", "警告");
					$("#submitAndSave").attr("class","ydbz_x");
				} else {
					top.$.jBox.info("上传微信广告图片有误", "警告");
					$("#submitAndSave").attr("class","ydbz_x");
				}
			}
		},
		error: function (returndata) {
		}
	});
}

// 修改图片如果没有上传微信图片则放入默认图片
$(function() {
	if ($.trim($(".img-container").html()) == "") {
		var _ctx = $("#ctxStatic").val();
		var src = _ctx + "/images/wechat/weixindefaultpic.png";
		$(".img-container").html("<img id='defaultImgId' src='"+src+"' alt='Picture'>");
	}
})
//QU-SDP-微信分销模块end yang.gao 2017-01-09