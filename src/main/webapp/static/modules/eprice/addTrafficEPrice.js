$(function(){
	//新增单团询价项目第一个表单（基本信息表单）初始化，主要是事件绑定处理
	addaeprice.service.firstFormInit();
	
   	//新增单团询价项目第三个表单（机票表单）初始化，主要是事件绑定处理
   	addaeprice.service.thirdFormInit();
	
   	// 机票计调多选事件
   	addaeprice.service.findTicket();
	
	// 绑定出发抵达城市名称
	$("#eprice-c-div-p-id").on("change","select[name=startCityId]",function(){
		addaeprice.service.startCityName($(this));
	});
	$("#eprice-c-div-p-id").on("change","select[name=endCityId]",function(){
		addaeprice.service.endCityName($(this));
	});
	$("[name=history-back]").click(function(){
	    location.href=contextPath+"/eprice/manager/project/list4saler";
	});
	
	$("#base_allPersonSum").blur(function(){
		var allPersonSum = $("#base_allPersonSum").val();
		var adultSum = $("#base_adultSum").val();
		var childSum = $("#base_childSum").val();
		var specialPersonSum = $("#base_specialPersonSum").val();
		var cha = allPersonSum - childSum - specialPersonSum;
		if(allPersonSum==0){
			$("#base_specialPersonSum").val(0);
			$("#base_childSum").val(0);
			$("#base_adultSum").val(0);
		}else if(cha>=1){
			$("#base_adultSum").val(cha);
		}else{
			$("#base_adultSum").val(1);
			if(allPersonSum-1-childSum>=0){
				$("#base_specialPersonSum").val(allPersonSum-1-childSum);
			}else{
				$("#base_specialPersonSum").val(0);
				$("#base_childSum").val(allPersonSum-1);
			}
		}
		$("#traffic_adultSum").val($("#base_adultSum").val());
		$("#traffic_allPersonSum").val(allPersonSum);
		$("#traffic_childSum").val($("#base_childSum").val());
	});
	
	$("#base_adultSum").blur(function(){
		addSum();
		$("#traffic_adultSum").val($("#base_adultSum").val());
	});
	
	$("#base_childSum").blur(function(){
		addSum();
		$("#traffic_childSum").val($("#base_childSum").val());
	});
	
	$("#base_specialPersonSum").blur(function(){
		addSum();
		$("#traffic_specialPersonSum").val($("#base_specialPersonSum").val());
	});
	
});



function addSum(){
	var adultSum = parseInt($("#base_adultSum").val());
	var childSum = parseInt($("#base_childSum").val());
	var specialPersonSum = parseInt($("#base_specialPersonSum").val());
	if(isNaN(adultSum)){
		adultSum=0;
	}
	if(isNaN(childSum)){
		childSum=0;
	}
	if(isNaN(specialPersonSum)){
		specialPersonSum=0;
	}
	var sum =  adultSum + childSum + specialPersonSum;
	$("#base_allPersonSum").val(sum);
	$("#traffic_allPersonSum").val(sum);
	
}

function deleteSelected(obj){
	$(obj).parent().remove();
}

var addaeprice = {
		//询价项目id
		epriceProjectid : null,
		//询价项目对象
		epriceProject : null,
		
		startTimes : [[0,8],[8,18],[18,0]],
		
		nolist : [["一","二","三","四","五","六","七","八","九","十"],["","","十","百","千"]],
		
		
		dao : {
			
			/**
			 * 询价第二个表单——询价接待要求动态表单dom的html字符串组装
			 * @author lihua.xu
			 * @时间 2014年9月22日
			 */
			require : function(m){
				var html = '<div name="require" namecode="'+m.code+'" class="seach25 seach100"><p>'+m.name+'：</p> <p class="seach_r">';
				var o;
				var id;
				for(var key in m.options){
					o = m.options[key];
					id = m.code+"-"+o.value+"-"+key;
					
					html += '<span class="seach_check"><input type="checkbox"  id="'+id+'" name="'+m.code+'" value="'+o.value+'" class="'+o.class1+'"/><label for="'+id+'">'+o.name+'</label></span>';
					
					if(o.info){
						var cl = o["class"]||"";
						id += '-info';
						html += '<span class="seach_check">';
						if(o.info.prevname){
							html += o.info.prevname;
						}
						html += '<input type="'+o.info.type+'" id="'+id+'" pname="'+m.code+'-'+o.value+'" name="'+m.code+'" class="'+cl+'" />';
						if(o.info.sufname){
							html += '<label for="'+id+'">'+o.info.sufname+'</label>';
						}		
						html += '</span>';
					}
				}
             		
             	html += '<a class="add_seachcheck"></a></p></div>';
              	  
               	return html;
			},
			
			/**
			 * 询价第二个表单——询价接待要求的其他要求dom的html字符串组装
			 * @author lihua.xu
			 * @时间 2014年9月22日
			 */
			other : function(m){
				var html = '<span class="seach_check seach_checkadd"><input type="checkbox" name="'+m.code+'" id="'+m.code+'_other" value="0" class="'+m.class1+'">';
					html += '<input type="text" name="travelRequirements"><a class="inquiry_zxdel">删除</a></span>';
					return html;
			},
			
			/**
			 * 添加新的自定义询价接待要求dom的html字符串组装
			 * @author lihua.xu
			 * @时间 2014年9月22日
			 */
			inquiry2AddIpt : function(){
				var html = '<div class="seach25 seach100 pr jd-xs"><p>&nbsp;</p><span class="seach_check"><input type="text" flag="istips" name="otherRequirement" title="填写其他内容"></span><a class="inquiry_zxdel">删除</a></div>';
				return html;
			},
			
			/**
			 * 添加新的询价要求dom的html字符串组装（添加新的文本域模块）
			 * @author lihua.xu
			 * @时间 2014年9月22日
			 */
			inquiryZxtext : function(){
				var html = '<span class="inquiry_zxtext"><textarea name="epriceRequirement"></textarea><a class="inquiry_zxdel">删除</a></span>';
				return html;			
			}
			
		},
		
		
		service : {

			/**
			 * 根据选择，获取出发城市填入隐藏input
			 */
			startCityName : function(obj){
				var startName = obj.find("option:selected").text();
				obj.next().next("input[name=startCityName]").val(startName);
			},
			/**
			 * 根据选择，获取到达城市填入隐藏input
			 */
			endCityName : function(obj){
				var endName = obj.find("option:selected").text();
				obj.next().next("input[name=endCityName]").val(endName);
			},
			
			/**
			 * 新增单团询价项目第一个表单初始化，主要是事件绑定处理
			 * @author lihua.xu
			 * @时间 2014年9月19日
			 */
			firstFormInit : function(){
				//询价客户类型选择 联动显示事件
				$("#eprice-a-form-id input:radio[name=customerType]").click(function(){
					var refshow =$("#eprice-a-form-id input:radio[name=customerType]:checked").attr("refshow");
					$("#eprice-a-form-id").find("div[name=inquiry_radio_people]").hide();
					$("#eprice-a-form-id").find("div[name=inquiry_radio_people] input").attr("disabled", "disabled");
					$("#eprice-a-form-id").find("div[name=inquiry_radio_people] select").attr("disabled", "disabled");
					
					 $("#eprice-a-form-id ."+refshow).find("input").removeAttr("disabled");
					 $("#eprice-a-form-id ."+refshow).find("select").removeAttr("disabled");
					 $("#eprice-a-form-id ."+refshow).show();
				});
				$("#eprice-a-form-id input:radio[name=customerType]:checked").trigger("click");
				
				
				
				//票务计调选择对申请机票表单的影响
				$("#eprice-a-form-id input[name=toperatorUserId]").click(function(){
					var checkeds = $("#eprice-a-form-id input[name=toperatorUserId]:checked");
					if(checkeds.length>0){
						$("#is-app-flight-id").trigger("checkedclick");
						
					}else{
						$("#is-app-flight-id").trigger("uncheckedclick");
					}
				});
				
				
				/**
				 * 询价项目新增表单第一个表单（询价基本信息表单）提交按钮事件绑定
				 */
				$("#eprice-a-form-submit-btn-id").click(function(){
					var allPersonSum = $("input[name=allPersonSum]").val();
					var adultSum = $("input[name=adultSum]").val();
					var childSum = $("input[name=childSum]").val();
					var specialPersonSum = $("input[name=specialPersonSum]").val();
					if(!allPersonSum){
						$("input[name=allPersonSum]").val(0);
					}
					if(!adultSum){
						$("input[name=adultSum]").val(0);
					}
					if(!childSum){
						$("input[name=childSum]").val(0);
						$("input[name=childSum]").focus();
						$("input[name=childSum]").blur();
					}
					if(!specialPersonSum){
						$("input[name=specialPersonSum]").val(0);
					}
					addaeprice.ajax.firstSubmit();
				});
				
				/**
				 * 询价项目，同行选择触发联系人，电话的变动
				 */
				$("select[name=customerAgentId]").change(function(){
				//	alert($(this).parent().next().attr("class"));
				//	alert($(this).parent().next().find("p.seach_r").attr("class"));
					var id = $(this).val();
					if(id!=(-1)){
						$("#togetherman").val($("#hidename"+id).val());
						$("#togetherphone").val($("#hidephone"+id).val());
						$("#togetherOtherContactWay").val($("#hideOther"+id).val());
//						$("#togetherman").removeAttr("readonly");
//						$("#togetherman").attr("readonly","readonly");
						
//						$("#togetherphone").removeAttr("readonly");
//						$("#togetherphone").attr("readonly","readonly");
					}else{
						$("#togetherman").val("");
						$("#togetherphone").val("");
						$("#togetherOtherContactWay").val("");
//						$("#togetherman").removeAttr("readonly");
//						$("#togetherphone").removeAttr("readonly");
					}
				});
			},
			
			/**
			 * 新增单团询价项目第三个表单初始化，主要是事件绑定处理
			 * @author lihua.xu
			 * @时间 2014年9月23日
			 */
			thirdFormInit : function(){
				/**
				 * 是否申请机票，如果勾选申请，则显示机票申请表单，如果不勾选申请，则不显示机票申请表单
				 */
				$("#is-app-flight-id").bind("checkedclick",function(){
					$("#eprice-c-div-id").show();
					$(this).prop("checked",true);
				}).bind("uncheckedclick",function(){
					$("#eprice-c-div-id").hide();
					$(this).prop("checked",false);
				}).click(function(){
					if($(this).prop("checked")){
						$(this).trigger("checkedclick");
					}else{
						$(this).trigger("uncheckedclick");
					}
				});
				
				/**
				 * 线路类型选择关联表单内容显示的变化
				 * 往返 单程 多段
				 */
				$("#eprice-c-div-id input[name=trafficLineType]").click(function(){
					var checkdom = $("#eprice-c-div-id input[name=trafficLineType]:checked");
					var refshow = $(checkdom).attr("refshow");
					$("#eprice-c-div-id div[name=inquiry-flights-div]").hide();
					$("#eprice-c-div-id div[name=inquiry-flights-div]."+refshow).show();
					if($(checkdom).val() == 3){
						$("#area-type-div-id").hide();
					}else{
						$("#area-type-div-id").show();
					}
				});
				
				$("#eprice-c-div-id input[name=trafficLineType]:checked").trigger("click");
				 
				
				//初始化出发时间区间下拉菜单，同时绑定区间的2个下拉菜单的联动事件
				addaeprice.service.startTimeInit();
				
				
				$("#eprice-c-form-3-id").on("renoset","span[name=traffic-line-no]",function(){
					var p = $(this).parents("div[name=traffic-line-div-name]");
					var i = $("#eprice-c-form-3-id div[name=traffic-line-div-name]").index(p);
					$(this).next("input[name=no]").val(++i);
					var d;
					if(i<=10){
						d = addaeprice.nolist[0][i-1];
					}else if(10<i && i<100){
						d = addaeprice.nolist[0][i/10]+addaeprice.nolist[0][9]+addaeprice.nolist[0][i%10];
					}else{
						d = i;
					}
					
					$(this).html("第"+d+"段:");
					
					var p1 = $(this).parent();
					
					//初始化地域选择表单元素组
					$(p1).find("span[name=area-type-span]").each(function(index){
						var id = "area-type-id-"+index+(i-1);
						var name = "areaType"+(i-1);
						$(this).find("input:radio").attr("id",id).attr("name",name);
						$(this).find("label").attr("for",id);
					});
				});
				
				//刷新多段线路的某段的地域类型真实表单元素的值
				$("#eprice-c-form-3-id div[name=area-type-div]").live("areaTypeChecked",function(){
					var v = $(this).find("input:radio:checked").val();
					
					$(this).find("input[name=areaType]").val(v);
				});
				
				$("#eprice-c-form-3-id input[myname=areaType]").live("click",function(){
						$(this).parents("div[name=area-type-div]").trigger("areaTypeChecked");
				});
				
				
				$("#eprice-c-form-1-id div[name=area-type-div]").live("areaTypeChecked1",function(){
					var v = $(this).find("input:radio:checked").val();
					
					$(this).find("input[name=areaType]").val(v);
				});
				
				$("#eprice-c-form-1-id input[myname=areaType]").live("click",function(){
					$(this).parents("div[name=area-type-div]").trigger("areaTypeChecked1");
				});
				
				$("#eprice-c-form-2-id div[name=area-type-div]").live("areaTypeChecked2",function(){
					var v = $(this).find("input:radio:checked").val();
					
					$(this).find("input[name=areaType]").val(v);
				});
				
				$("#eprice-c-form-2-id input[myname=areaType]").live("click",function(){
					$(this).parents("div[name=area-type-div]").trigger("areaTypeChecked2");
				});
				
				//线路删除按钮事件绑定处理
				$("#eprice-c-form-3-id a[name=del-traffic-line-name]").live("click",function(){
					var p = $(this).parents("div[name=traffic-line-div-name]");
					var i = $("#eprice-c-form-3-id div[name=traffic-line-div-name]").index(p);
					//验证是防止通过firbug工具，把第二段线路的删除按钮打开，把第二段线路删除了
					if(i>1){
						$(p).remove();
						
						//如果被删除的线路不是最后的线路，则需要被它后面的线路的段号从新更新
						if(i!=$("#eprice-c-form-3-id div[name=traffic-line-div-name]").length){
							i--;
							$("#eprice-c-form-3-id div[name=traffic-line-div-name]:gt("+i+")").each(function(){
								$(this).find("span[name=traffic-line-no]").trigger("renoset");
							});
						}
						
					}
				});
				
				//增加线路事件绑定
				$("#add-traffic-line-id").click(function(){
					var dom = $("#trafficTemplate").html();
					
					//$(dom).find("span[name=traffic-line-no]").trigger("renoset");
					//$(dom).removeAttr("id");
					//$(dom).find("input[name=startDate]").val("");
					//$(dom).find("a[name=del-traffic-line-name]").parent().show();
					$("#eprice-c-form-3-id").append(dom);
					$("#eprice-c-form-3-id").find("span[name=traffic-line-no]:last").trigger("renoset");
					var v = $("#traffic-line-2-div-id div[name=area-type-div] input[name=areaType]").val();
					$("#traffic-line-2-div-id div[name=area-type-div] input:radio[value="+v+"]").prop("checked",true);
					$(dom).find("div[name=area-type-div] input:radio[value="+v+"]").prop("checked",true);
					
					//设置下拉框模糊匹配
					var $newStage = $("#eprice-c-form-3-id").find("[name='traffic-line-div-name']:last");
					$newStage.find(".selectinput").comboboxInquiry({
						"afterInvalid":function(event,data){
							$(this).trigger("change");
						}
					});
				});
				
				//提交询价按钮点击事件绑定处理
				$("#eprice-c-form-submit-btn-id").click(function(){
					$("#eprice-c-form-submit-btn-id").attr("disabled","disabled");
					addaeprice.ajax.thirdSubmit();
				});
				
				
				
				
			},
			
			/**
			 * 初始化出发时间区间下拉菜单，同时绑定区间的2个下拉菜单的联动事件
			 * @author lihua.xu
			 * @时间 2014年9月23日
			 */
			startTimeInit : function(){
				var html1 = "";
				var html2 = "";
				for(var i=0;i<=23;i++){
					html1 += '<option value="'+i+'">'+i+':00</option>';
				}
				
				for(var i=0;i<=23;i++){
					if(i==8){
						html2 += '<option value="'+i+'" selected = "selected">'+i+':00</option>';
					}else{
						html2 += '<option value="'+i+'">'+i+':00</option>';
					}
				}
				
				$("select[name=startTime1]").each(function(){
						$(this).html(html1);
					});
					
				$("select[name=startTime2]").each(function(){
						$(this).html(html2);
					});
				
				//主联动，startTime1下拉菜单值决定startTime2下拉菜单的菜单项
				$("#eprice-c-div-p-id select[name=startTime1]").change(function(){
					var i = $(this).val();
					var nexts = $(this).next("select");
//					$(nexts).find("option").show();
//					$(nexts).find("option:lt("+Number(i)+")").hide();
//					alert($(nexts).val());
//					alert($(nexts).val()<=i);
					if(Number(i)>=Number($(nexts).val())){
						$(nexts).val(1+Number(i));
					}
					
				});
				
				//次联动，当startTime2下拉菜单值小于等于startTime1下拉菜单值时，自动更新startTime1下拉菜单值
				$("#eprice-c-div-p-id select[name=startTime2]").change(function(){
					var i = $(this).val();
					var prevs = $(this).prev("select");
					//$(prevs).find("option").show();
					//$(prevs).find("option:gt("+i+")").hide();
					if(Number($(prevs).val())>=Number(i)){
						$(prevs).val(Number(i)-1);
					}
				});
				
				//出发时刻值改变联动出发时间区间
				$("#eprice-c-div-p-id").on("change","select[name=startTimeType]",function(){
					var i = $(this).val();
					var hs = addaeprice.startTimes[i-1];
					var hspdom = $(this).parents("div.seach25").next();
					$(hspdom).find("select[name=startTime1]").val(hs[0]);
					$(hspdom).find("select[name=startTime2]").val(hs[1])
				});
				$("#eprice-c-div-p-id select[name=startTimeType]").change();
			},
			
			
			
			
			
			/**
			 * 将第三表单的用户填写数据序列化成可提交格式
			 * @author lihua.xu
			 * @时间 2014年9月24日
			 */
			thirdFormSerialize : function(){
				var param = "";
				var isAppFlight = $("#is-app-flight-id").prop("checked");
				if(!isAppFlight){//未申请机票
					param += "isAppFlight="+isAppFlight;
					return param;
				}
				
				param += "isAppFlight="+isAppFlight;
				// 机票计调数组
				var toperatorUerIdList = new Array();
				var nt = 0;
				$("input[name=toperatorUserId]").each(function(){
					toperatorUerIdList[nt] = $(this).val();
					nt++;
				});
				param += "&toperatorUserId="+toperatorUerIdList;
				
				var trafficLineType = $("#eprice-c-form-0-id input[name=trafficLineType]:checked").val();
				param += "&trafficLineType="+trafficLineType;
				//交通路线类型：往返
				if(trafficLineType==1){
					
					param += "&"+$("#eprice-c-form-1-id").serialize();
					
				}else if(trafficLineType==2){//交通路线类型：单程
					
					param += "&"+$("#eprice-c-form-2-id").serialize();
				
				}else{//交通路线类型：多段
				
					param += "&"+$("#eprice-c-form-3-id").serialize();
					
				}
				
				//交通路线类型：往返和单程，线路地域类型数据单独处理
//				if(trafficLineType==1 || trafficLineType==2){
//					param += "&qAreaType="+ $("#eprice-c-form-0-id input[name=qAreaType]:checked").val();
//				}
				
				
				//机票申请基本信息
				param += "&"+$("#eprice-c-form-4-id").serialize();
				
				param += "&projectId="+addaeprice.epriceProjectid;
				
				param += "&projectId="+addaeprice.epriceProjectid;
				
				param += "&onceAgain="+$("#id_onceAgain").val();
				//alert(param);
				
				return param;
			},
			
			/**
			 * 处理文件域的ajaxFileUpload上传
			 * @author lihua.xu
			 * @时间 2014年9月22日
			 * @param {} id 文件域的id
			 */
			fileUpload : function(id){
				var v = $("#"+id).val();
				var fileName = v;//.substr(v.indexOf("."));
				$("#saler-trip-file-name-id").val(fileName);
				$.ajaxFileUpload({  
	                url:contextPath+ "/eprice/manager/ajax/project/file/upload",            //需要链接到服务器地址  
	                secureuri:false,  
	                fileElementId:id,//文件选择框的id属性
	                dataType : 'json',
	                success: function(data, status){
	                	//alert(data);
	                   if(data.res=="success"){
	                   		$("#saler-trip-file-id-id").val(data.model.id);
	                   		jBox.tip("上传成功！", 'info');
	                   }else{
	                   		alert(data.res);
	                   }
	                },
	                error: function (data, status, e){
	                	jBox.tip("文件错误！", 'info');
	                        
	                }  
	            });  
			},
			
			/**
			 * 机票询价--搜索计调名
			 */
			findTicket : function(){
				//机票询价--搜索计调名
				$("select[name='ticketOperator']" ).comboboxInquiry({
					"afterInvalid":function(event,data){
						var selectValue = $(this).val();
						var Array_default = new Array("请搜索计调");
						if(-1 == $.inArray(data,Array_default)){
							var isIncluded = 0;
							$("#ticketOperatorShow a").each(function(index, element) {
								if(data == $(element).text().slice(0,-1)){
									isIncluded = 1;
									return;
								}
							});
							if(isIncluded){
								jBox.tip("您已选择");
							}else{
								$("#ticketOperatorShow").append('<a style="padding:0px 20px 0px 15px;position:relative;">{0}<input type="hidden" name="toperatorUserId" value="{0}"/><span style="position:absolute;right:3px;top:4px;cursor: pointer;" class="deleteicon" href="javascript:void(0)" onclick="deleteSelected(this)">x</span></a>'
										.replace("{0}",data).replace("{0}",selectValue));
								//$("#ticketOperatorShow").append('<input type="hidden" name="toperatorUserId" value="{0}"/>'.replace("{0}",selectValue));
							}
						}
					}
				});
			}
		
		},
		
		ajax : {
			
			/**
			 * 询价第一个表单——询价基础信息异步提交，点击第一个表单的“下一步”按钮时触发
			 * @author lihua.xu
			 * @时间 2014年9月22日
			 */
			firstSubmit : function(){
				//oneToTwo();
				//return;
				
				var the_param = $("#eprice-a-form-id").serialize();
				the_param += "&projectId="+$("#id_projectId").val();
				the_param += "&onceAgain="+$("#id_onceAgain").val();
				$.ajax({
					type : "POST",
					url : contextPath+"/eprice/manager/ajax/project/trafficOneSave",
					data : the_param,
					dataType : "text",
					//async : true,
					success : function(html){
						var json;
						try{
							json = $.parseJSON(html);
						}catch(e){
							json = {res:"error"};							
						}
						
						if(json.res=="success"){
							addaeprice.epriceProjectid = json.model.id;
							addaeprice.epriceProject = json.model;
							oneToThree();
							//addaeprice.ajax.thirdSubmit();
						}else if(json.res=="data_error"){
							jBox.tip("输入数据不正确", 'error');
						}else{
							jBox.tip(json.res, 'error');
						}
					}
				
				});
					
			},
			
			
			/**
			 * 询价第三个表单——接待内容询价异步提交，点击第三个表单的“下一步”按钮时触发
			 * @author lihua.xu
			 * @时间 2014年9月24日
			 */
			thirdSubmit : function(){
				
				/*var the_param = addaeprice.service.secondFormSerialize();
				TwoToThree();
				return;*/
				var the_param = addaeprice.service.thirdFormSerialize();
				
				$.ajax({
					type : "POST",
					url : contextPath+"/eprice/manager/ajax/project/trafficTwoSave",
					data : the_param,
					dataType : "text",
					success : function(html){
						var json;
						try{
							json = $.parseJSON(html);
						}catch(e){
							json = {res:"error"};	
							$("#eprice-c-form-submit-btn-id").removeAttr("disabled"); 
						}
						
						if(json.res=="success"){
							jBox.tip("提交成功", 'info');
							location.href=contextPath+"/eprice/manager/project/list4saler";
						}else if(json.res=="data_error"){
							jBox.tip("输入数据不正确", 'error');
							$("#eprice-c-form-submit-btn-id").removeAttr("disabled"); 
						}else{
							jBox.tip(json.res, 'error');
							$("#eprice-c-form-submit-btn-id").removeAttr("disabled"); 
						}
					}
				
				});
					
			}
		}
};

