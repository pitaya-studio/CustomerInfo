$(function(){
	//新增单团询价项目第一个表单初始化，主要是事件绑定处理
	addaeprice.service.firstFormInit();
	
	//新增单团询价项目第二个表单初始化，主要是事件绑定处理
   	addaeprice.service.secondFormInit();
   	
   	//新增单团询价项目第三个表单初始化，主要是事件绑定处理
   	addaeprice.service.thirdFormInit();
	
   	// 地接计调多选事件
   	addaeprice.service.findVendor();
   	// 线路国家多选事件
   	addaeprice.service.findCountry();
   	// 机票计调多选事件
   	addaeprice.service.findTicket();
 // 绑定出发抵达城市名称
   	$("#eprice-c-div-p-id").on("change","select[name=startCityId]",function(){
		addaeprice.service.startCityName($(this));
	});
	$("#eprice-c-div-p-id").on("change","select[name=endCityId]",function(){
		addaeprice.service.endCityName($(this));
	});
	
	$("select[name=outAreaId]").change(function(){
		addaeprice.service.outAreaName($(this));
	});
	
	$("[name=history-back]").click(function(){
		window.location.href = contextPath + "/eprice/manager/project/list4saler";
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
	});
	
	$("#base_adultSum").blur(function(){
		addSum();
	});
	$("#base_childSum").blur(function(){
		addSum();
	});
	$("#base_specialPersonSum").blur(function(){
		addSum();
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
		
		/**
		 * 询价接待要求表单json数据，通过这些数据生成询价接待表单
		 * @author lihua.xu
		 * @时间 2014年9月22日
		 * @type jsonArray
		 */
		travelRequirementsInputs :travelRequirements.formData,
		bigClass :travelRequirements.bigClass,
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
				
				if(m.isExistOther){
					html += '<a class="add_seachcheck"></a>';
				}
             		
				html += "</p></div>";
              	  
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
			
			outAreaName : function(obj){
				var outAreaName = obj.find("option:selected").text();
				obj.next("input[name=outAreaName]").val(outAreaName);
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
					var id = $(this).val();
					if(id!=(-1)){
						$("#togetherman").val($("#hidename"+id).val());
						$("#togetherphone").val($("#hidephone"+id).val());
						$("#togetherOtherContactWay").val($("#hideOther"+id).val());
						
					}else{
						$("#togetherman").val("");
						$("#togetherphone").val("");
						$("#togetherOtherContactWay").val("");
					}
				});
			},
			
			
			/**
			 * 新增单团询价项目第二个表单初始化，主要是事件绑定处理
			 * @author lihua.xu
			 * @时间 2014年9月22日
			 */
			secondFormInit : function(){
				/**
				 * 将接待要求动态表单数据生成接待要求动态表单
				 */
				addaeprice.service.tRInputsJson2html();
				
				
				/**
				 * 接待内容询价：预计出团日期
				 */
				$("#dgroup-out-date-id").focus(function(){
					var $this = this;
					WdatePicker({
						            minDate:'%y-%M-%d',
									onpicking:function(dp){
												var vvv=dp.cal.getNewDateStr();
												if($($this).val()==null || $($this).val()=='' ){
													$($this).val(vvv);
												}
										}
								});
				});
				
				//添加新的自定义询价接待要求
				$("#eprice-b-form-id .inquiry2AddIpt").click(function(){
			                $(this).before(addaeprice.service.inquiry2AddIpt());
			            });
				$("body").delegate("#eprice-travel-require-div-id :checkbox","click",function(){
					var $this = $(this);
					var name = $this.attr("name");
					var id = $this.attr("id");
					var class1 = $this.attr("class");
					// 设定单选
					if(class1.indexOf("lonely")!=-1){
						$("#eprice-travel-require-div-id input:checkbox[name=" + name + "][id!=" + id + "][class=lonely]").attr("checked",false);
					}
				});

			     /**
			      * 添加新的询价要求（文件域模块）
			      */       
			     $("#eprice-b-form-id .inquiry_zxadd").click(function(){
			                 $(this).parent().parent().append(addaeprice.service.inquiryZxtext());
							//$(this).hide();
			            });
			            
			     /**
			      * 行程文档文件域change上传事件绑定
			      */
			     $("#saler-trip-file-id").change(function(){
			     	try{
			     		addaeprice.service.fileUpload($(this).attr("id"));
			     	}catch(e){
			     	}
			     });
			     
			     /**
			      * 行程文档上传按钮
			      */
			     $("#saler-trip-btn-id").click(function(){
			     	$("#saler-trip-file-id").click();
			     });
			     
			     /**
			      * 询价项目新增表单第二个表单（接待要求表单）提交按钮事件绑定
			      */
			     $("#eprice-b-form-submit-btn-id").click(function(){
					addaeprice.ajax.secondSubmit();
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
//					alert(1);
//					return;
					var allPersonSum = $("#traffic_allPersonSum").val();
					var adultSum = $("#traffic_adultSum").val();
					var childSum = $("#traffic_childSum").val();
					var specialPersonSum = $("#traffic_specialPersonSum").val();
					if(allPersonSum==null||allPersonSum==""){
						$("#traffic_allPersonSum").val(0);
					}
					if(adultSum==null||adultSum==""){
						$("#traffic_adultSum").val(0);
					}
					if(childSum==null||childSum==""){
						$("#traffic_childSum").val(0);
						$("#traffic_childSum").focus();
						$("#traffic_childSum").blur();
					}
					if(!specialPersonSum){
						$("#traffic_specialPersonSum").val(0);
					}
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
					html2 += '<option value="'+i+'">'+i+':00</option>';
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
					if(Number(i)>=Number($(nexts).val())){
						$(nexts).val(1+Number(i));
					}
				});
				
				//次联动，当startTime2下拉菜单值小于等于startTime1下拉菜单值时，自动更新startTime1下拉菜单值
				$("#eprice-c-div-p-id select[name=startTime2]").change(function(){
					var i = $(this).val();
					var prevs = $(this).prev("select");
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
					$(hspdom).find("select[name=startTime2]").val(hs[1]);
				});
				$("#eprice-c-div-p-id select[name=startTimeType]").change();
			},
			
			
			/**
			 * 询价第二个表单——询价接待要求的其他要求dom生成和事件绑定
			 * @author lihua.xu
			 * @时间 2014年9月22日
			 */
			other : function(m){
				var $html = $(addaeprice.dao.other(m));
				$($html).find(".inquiry_zxdel").click(function(){
					 $(this).parent().parent().find('a').show();
				 	$(this).parent().remove();
				});
				
				return $html;
			},
			
			/**
			 * 询价第二个表单——询价接待要求动态表单dom生成和事件绑定
			 * @author lihua.xu
			 * @时间 2014年9月22日
			 */
			require : function(m){
				var $html = $(addaeprice.dao.require(m));
				
				//”+“按钮事件
				$($html).find(".add_seachcheck").click(function(){
				    $(this).before(addaeprice.service.other(m));
					$(this).parent().find('.add_seachcheck').hide();
				});
				return $html;
			
			},
			
			/**
			 * 通过json数据生成动态询价接待要求表单
			 * @author lihua.xu
			 * @时间 2014年9月22日
			 */
			tRInputsJson2html : function(){
				var jsonBigClass =addaeprice.bigClass;
				var json = addaeprice.travelRequirementsInputs;
				for (var i = 0; i < jsonBigClass.length; i++) {
					var $html = $('<div name="'+jsonBigClass[i].code+'"><strong>'+jsonBigClass[i].name  +'</strong><div class="kong"></div></div>');
					for(var j = 0; j < json.length; j++){
						if((jsonBigClass[i].code==""&&typeof(json[j].bigClass) == "undefined")||(jsonBigClass[i].code==json[j].bigClass)){
							$html.append(addaeprice.service.require(json[j]));
						}
					}
					$("#eprice-travel-require-div-id").append($html);
				}
				
				
			},
			
			/**
			 * 添加新的自定义询价接待要求dom生成和事件处理
			 * @author lihua.xu
			 * @时间 2014年9月22日
			 */
			inquiry2AddIpt : function(){
				var $html = $(addaeprice.dao.inquiry2AddIpt());
				$($html).find("input[flag=istips]").flagTips();
				
				//删除
				$($html).find(".inquiry_zxdel").click(function(){
					$(this).parent().parent().find('a').show();
					$(this).parent().remove();
            	});
            	
            	return $html;
			},
			
			/**
			 * 添加新的询价要求dom生成和事件绑定（添加新的文本域模块）
			 * @author lihua.xu
			 * @时间 2014年9月22日
			 */
			inquiryZxtext : function(){
				var $html = $(addaeprice.dao.inquiryZxtext());
				//删除
				$($html).find(".inquiry_zxdel").click(function(){
					 $(this).parent().parent().find('a').show();
					 $(this).parent().remove();
			    });
			    
			    return $html;
			},
			
			/**
			 * 将第二表单的用户填写数据序列化成可提交格式
			 * @author lihua.xu
			 * @时间 2014年9月22日
			 */
			secondFormSerialize : function(){
				var param = "projectId="+addaeprice.epriceProjectid;
				// 地接计调数组
				// 20150902 修改为只用计调主管。去掉全部计调数组。
				param += "&formanager="+$("#eprice-b-form-id").find("select[name=formanager] option:selected").val();
				
				param += "&dgroupOutDate="+$("#dgroup-out-date-id").val();//预计出团日期
				param += "&outAreaName="+$("#eprice-b-form-id input[name=outAreaName]").val();//出境口岸
				param += "&outAreaId="+$("#eprice-b-form-id select[name=outAreaId]").val();
				//线路国家id
				var travelCountryIdList = new Array();
				var ncoui = 0;
				$("#eprice-b-form-id input[name=travelCountryId]").each(function(){
					travelCountryIdList[ncoui] = $(this).val();
					ncoui++;
				});
				param += "&travelCountryId="+travelCountryIdList;//线路国家id
				
				//线路国家name
				var travelCountryList = new Array();
				var ncou = 0;
				$("#eprice-b-form-id input[name=travelCountry]").each(function(){
					travelCountryList[ncou] = $(this).val();
					ncou++;
				});
				param += "&travelCountry="+travelCountryList;
				param += "&outsideDaySum="+$("#eprice-b-form-id input[name=outsideDaySum]").val();//境外停留白天天数
				param += "&outsideNightSum="+$("#eprice-b-form-id input[name=outsideNightSum]").val();//境外停留夜晚天数
				
				var fileNum = 0;
				var arrFile = new Array();
				var arrFileName = new Array();
				var arrFilePath = new Array();
				$("input[name=salerTripFileId]").each(function(){
					arrFile[fileNum] = $(this).val();
					fileNum++;
				});
				fileNum = 0;
				$("input[name=salerTripFileName]").each(function(){
					arrFileName[fileNum] = $(this).val();
					fileNum++;
				});
				fileNum = 0;
				$("input[name=salerTipFilePath]").each(function(){
					arrFilePath[fileNum] = $(this).val();
					fileNum++;
				});
				fileNum=0;
				param += "&salerTripFileId="+arrFile;//行程文档Id
				param += "&salerTripFileName="+arrFileName;//行程文档name
				param += "&salerTipFilePath="+arrFilePath;//行程文档path
				
				var json = {};
				
				//询价接待要求主体数据（可接受多个值）
				$.each(addaeprice.travelRequirementsInputs,function(i,m){
					
					var option = {};
					var num = 0;
					$("#eprice-travel-require-div-id input[name="+m.code+"][type=checkbox]").each(function(){
						//option.option = "option_"+num;
						var check = $(this).attr("checked");
						var t = false;
						var opt = {};
						opt.name = m.name;
						opt.check = "unchecked";
						if(check=="checked"){
							t = true;
							opt.check = "checked";
						}
						opt.type = $(this).val();
						var child = m.options["option_"+opt.type];
						if(opt.type && opt.type!=0){//非其他选项
							opt.title = encodeURIComponent(child.name);
							if(child.info){
								t = true;//存在备注情况
								opt.info = child.info;
								var div = $("#eprice-travel-require-div-id div[name=require][namecode="+m.code+"]");
								opt.info.value = encodeURIComponent($(div).find("input[pname="+m.code+"-"+child.value+"]").val());
							}
						}else{//其他选项
							opt.title = encodeURIComponent($("#eprice-travel-require-div-id input[name="+m.code+"]").next("input:text").val());
							if(opt.title){//其他选项中有数据
								t = true;
							}
						}
						if(t){
							option["option_"+num] = opt;
						}
						
						num++;
					});
					
					json[m.code]=option;
				}
			);
				
				//自定义新增询价接待要求
				var otherRequirements = [];
				$("#eprice-b-form-id input[name=otherRequirement]").each(function(){
					var o = {};
					o.value = encodeURIComponent($(this).val());
					otherRequirements.push(o);
				});				
				json.otherRequirements = otherRequirements;
				
				//询价要求
				var epriceRequirements =  [];
				$("#eprice-b-form-id textarea[name=epriceRequirement]").each(function(){
					var value = $(this).val();
					if(value && value != ""){
						var o = {};
						o.value = encodeURIComponent($(this).val());
						epriceRequirements.push(o);
					}
					
					
				});
				json.epriceRequirements = epriceRequirements;
				
				param += "&travelRequirements="+JSON.stringify(json);
				
				return param;
				
				
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
					
					param += "&projectId="+addaeprice.epriceProjectid;
					param += "&isAppFlight="+isAppFlight;
					return param;
				}else{
					param += "&isAppFlight="+isAppFlight;
				}
				// 机票计调数组
//				var toperatorUerIdList = new Array();
//				var nt = 0;
//				$("input[name=toperatorUserId]").each(function(){
//					toperatorUerIdList[nt] = $(this).val();
//					nt++;
//				});
//				param += "&toperatorUserId="+toperatorUerIdList;
				// 20150902 修改为只用计调主管。去掉全部计调数组。
				param += "&formanager="+$("#eprice-c-form-0-id").find("select[name=formanager] option:selected").val();
				
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
				//机票申请基本信息
				param += "&"+$("#eprice-c-form-4-id").serialize();
				
				param += "&projectId="+addaeprice.epriceProjectid;
				
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
				var fileName = v;
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
			 * 搜索计调名
			 */
			findVendor : function(){
				//搜索计调名
				$("select[name='vendorOperator']" ).comboboxInquiry({
					"afterInvalid":function(event,data){
						//被选择项的value值
						var selectValue = $(this).val();
						var Array_default = new Array("请搜索计调");
						if(-1 == $.inArray(data,Array_default)){
							var isIncluded = 0;
							$("#vendorOperatorShow a").each(function(index, element) {
								if(data == $(element).text()){
									isIncluded = 1;
									return;
								}
							});
							if(isIncluded){
								jBox.tip("您已选择");
							}else{
								$("#vendorOperatorShow").append('<a>{0}<input type="hidden" name="aoperatorUserId" value="{1}" /></a>'.replace("{0}",data).replace("{1}",selectValue));
							}
						}
					}
				});
			},
			
			/**
			 * 搜索国家
			 */
			findCountry : function(){
				//搜索国家
				$("select[name='country']" ).comboboxInquiry({
					"afterInvalid":function(event,data){
						var selectValue = $(this).val();
						
						var Array_default = new Array("搜索国家");
						if(-1 == $.inArray(data,Array_default)){
							var isIncluded = 0;
							$("#countryShow a").each(function(index, element) {
								if(data == $(element).text().slice(0,-1)){
									isIncluded = 1;
									return;
								}
							});
							if(isIncluded){
								jBox.tip("您已选择");
							}else{
								$("#countryShow").append('<a style="padding:0px 20px 0px 15px;position:relative;">{0}<input type="hidden" name="travelCountryId" value="{1}"/><input type="hidden" name="travelCountry" value="{2}"/><span style="position:absolute;right:3px;top:4px;cursor: pointer;" class="deleteicon" href="javascript:void(0)" onclick="deleteSelected(this)">x</span></a>'
										.replace("{0}",data).replace("{1}",selectValue).replace("{2}",data));
							}
						}
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
								if(data == $(element).text()){
									isIncluded = 1;
									return;
								}
							});
							if(isIncluded){
								jBox.tip("您已选择");
							}else{
								$("#ticketOperatorShow").append('<a>{0}<input type="hidden" name="toperatorUserId" value="{0}"/></a>'.replace("{0}",data).replace("{0}",selectValue));
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
				var the_param = $("#eprice-a-form-id").serialize();
				the_param += "&truemanager="+1; // 指定为计调主管询价
				if(addaeprice.epriceProject!=null){
					the_param += "&projectId="+addaeprice.epriceProjectid;
				}
				$.ajax({
					type : "POST",
					url : contextPath+"/eprice/manager/ajax/project/onesave",
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
							addaeprice.epriceProjectid = json.model.id;
							addaeprice.epriceProject = json.model;
							oneToTwo();
							jBox.tip("提交成功", 'info');
						}else if(json.res=="data_error"){
							jBox.tip("输入数据不正确", 'error');
						}else{
							jBox.tip(json.res, 'error');
						}
					}
				
				});
					
			},
			
			/**
			 * 询价第二个表单——接待内容询价异步提交，点击第二个表单的“下一步”按钮时触发
			 * @author lihua.xu
			 * @时间 2014年9月23日
			 */
			secondSubmit : function(){
				
				if($("#eprice-b-form-id").validate().form()) {
				var the_param = addaeprice.service.secondFormSerialize();
				$.ajax({
					type : "POST",
					url : contextPath+"/eprice/manager/ajax/project/twosaveformanager",
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
							TwoToThree();
							jBox.tip("提交成功", 'info');
						}else if(json.res=="data_error"){
							jBox.tip("输入数据不正确", 'error');
						}else{
							jBox.tip(json.res, 'error');
						}
					}
				
				});
			}
					
			},
			
			
			/**
			 * 询价第三个表单——接待内容询价异步提交，点击第三个表单的“下一步”按钮时触发
			 * @author lihua.xu
			 * @时间 2014年9月24日
			 */
			thirdSubmit : function(){
				
				var the_param = addaeprice.service.thirdFormSerialize();
				
				$.ajax({
					type : "POST",
					url : contextPath+"/eprice/manager/ajax/project/treeformanagersave",
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
							location.href=contextPath+"/eprice/manager/project/list4saler";
							jBox.tip("提交成功", 'info');
						}else if(json.res=="data_error"){
							jBox.tip("输入数据不正确", 'error');
							$("#eprice-c-form-submit-btn-id").removeAttr("disabled"); 
						}else if(json.res!=null&&json.res!=""){
							jBox.tip(json.res, 'error');
							$("#eprice-c-form-submit-btn-id").removeAttr("disabled"); 
						}else{
							jBox.tip("系统错误", 'error');
							$("#eprice-c-form-submit-btn-id").removeAttr("disabled"); 
						}
					}
				
				});
					
			},
			
			/**
			 * 查询接待计调列表
			 */
			findAoperatorList : function(){
				$.ajax({
					type : "POST",
					url : contextPath+"/sys/role/roleOfUsers",
					dataType : "text",
					success : function(html){
						var json;
						try{
							json = $.parseJSON(html);
						}catch(e){
							json = {res:"error"};
						}
						
						if(json.res=="success"){
							jBox.tip("提交成功", 'info');
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

