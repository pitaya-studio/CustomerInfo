/**
 * 数组删除指定的元素
 * @param obj
 */
Array.prototype.removeParmFlagValue=function(obj){
	    for(var i =0;i <this.length;i++){
		    var temp = this [i];
		    /*if(!isNaN(obj)){
		    	temp=i;
		    }*/
		    if(temp == obj){
		    for(var j = i;j <this.length;j++){
		    	this[j]=this [j+1];
		    }
		    this.pop();
		    }
	    }
    } 

	//ids
	var fromAreas=new Array();
	var targetAreas=new Array();
	var travelTypes=new Array();
	var productTypes=new Array();
	var productLevels=new Array();
	//names
	var fromAreaNames=new Array();
	var targetAreaNames=new Array();
	var travelTypeNames=new Array();
	var productTypeNames=new Array();
	var productLevelNames=new Array();
	var $ctx = "";
	
	function chose_mult_set_ini(select, values) {
        var arr = values.split(",");
        var length = arr.length;
        var value = "";
        for (var  i = 0; i < length; i++) {
            value = arr[i];
            $(select + " option[value=‘" + value + "‘]").attr('selected', 'selected');
        }
        $(select).trigger("liszt:updated");
    }
	
	$(document).ready(function() {
    		$ctx = '${ctx}';
    		 chose_mult_set_ini('#dl_chose2', '1,3,5,8');

    	        //初始化
    	     $(".chzn-select").chosen();
    	});
	 window.onload = function () {
		 $("em[name='initChecken']").click();
		 $("select.selectW").change();
     };


     /**
      * 单选 加入变量
      * @param arrayFlag
      * @param pushVal
      * @param pushValName
      */
	function pushValue(arrayFlag,pushVal,pushValName,callback){
		if(arrayFlag == "fromAreas"){
			fromAreas.push(pushVal);
			fromAreaNames.push(pushValName);
		}else if(arrayFlag == "targetAreas"){
			targetAreas.push(pushVal);
			targetAreaNames.push(pushValName);
		}else if(arrayFlag == "travelTypes"){
			travelTypes.push(pushVal);
			travelTypeNames.push(pushValName);
		}else if(arrayFlag == "productTypes"){
			productTypes.push(pushVal);
			productTypeNames.push(pushValName);
		}else if(arrayFlag == "productLevels"){
			productLevels.push(pushVal);
			productLevelNames.push(pushValName);
		}
		if(fromAreas && fromAreas.length >0 && targetAreas && targetAreas.length>0 && travelTypes && travelTypes.length>0 && productTypes && productTypes.length >0 && productLevels && productLevels.length > 0){
			$.ajax({
				type:"POST",
				url:$("#sysUrl").val()+"/judgeExist",
				data:{fromAreas:fromAreas+"",targetAreas:targetAreas+"",travelTypes:travelTypes+"",productTypes:productTypes+"",productLevels:productLevels+"",priceStrategyId:$("#updateId")?$("#updateId").val():null},
				success:function(data){
					if('ok' == data.flag){
						callback();
						return true;
					}else if("ALREADY_EXISTS" == data.flag){
						if(arrayFlag == "fromAreas"){
							fromAreas.removeParmFlagValue(pushVal);
							fromAreaNames.removeParmFlagValue(pushValName);
						}else if(arrayFlag == "targetAreas"){
							targetAreas.removeParmFlagValue(pushVal);
							targetAreaNames.removeParmFlagValue(pushValName);
						}else if(arrayFlag == "travelTypes"){
							travelTypes.removeParmFlagValue(pushVal);
							travelTypeNames.removeParmFlagValue(pushValName);
						}else if(arrayFlag == "productTypes"){
							productTypes.removeParmFlagValue(pushVal);
							productTypeNames.removeParmFlagValue(pushValName);
						}else if(arrayFlag == "productLevels"){
							productLevels.removeParmFlagValue(pushVal);
							productLevelNames.removeParmFlagValue(pushValName);
						}
						$.jBox.confirm("产品条件重复，不能添加","提示",function(v,h,f){
			        		if(v=='ok'){
			        			return;
			        		}
			        		if(v=='cancel'){
			        			return;
			        		}
			        	});
					}
				}
			});
		}else{
			callback();
		}
	}
	
	/**
	 * 单选 删除变量
	 * @param arrayFlag
	 * @param pushVal
	 * @param pushValName
	 */
	function delValue(arrayFlag,pushVal,pushValName){
		if(arrayFlag == "fromAreas"){
			fromAreas.removeParmFlagValue(pushVal);
			fromAreaNames.removeParmFlagValue(pushValName);
		}else if(arrayFlag == "targetAreas"){
			targetAreas.removeParmFlagValue(pushVal);
			targetAreaNames.removeParmFlagValue(pushValName);
		}else if(arrayFlag == "travelTypes"){
			travelTypes.removeParmFlagValue(pushVal);
			travelTypeNames.removeParmFlagValue(pushValName);
		}else if(arrayFlag == "productTypes"){
			productTypes.removeParmFlagValue(pushVal);
			productTypeNames.removeParmFlagValue(pushValName);
		}else if(arrayFlag == "productLevels"){
			productLevels.removeParmFlagValue(pushVal);
			productLevelNames.removeParmFlagValue(pushValName);
		}
	}
	
	/**
	 * 删除所有
	 * @param arrayFlag
	 */
	function delAll(arrayFlag){
		if(arrayFlag == "fromAreas"){
			fromAreas = new Array();
			fromAreaNames = new Array();
		}else if(arrayFlag == "targetAreas"){
			targetAreas = new Array();
			targetAreaNames = new Array();
		}else if(arrayFlag == "travelTypes"){
			travelTypes = new Array();
			travelTypeNames = new Array();
		}else if(arrayFlag == "productTypes"){
			productTypes = new Array();
			productTypeNames = new Array();
		}else if(arrayFlag == "productLevels"){
			productLevels = new Array();
			productLevelNames = new Array();
		}
	}
	
	/**
	 * 全选
	 * @param arrayFlag
	 */
	function pushAll(arrayFlag,callback){
		tempIds = null;
		temNames = null;
		if(arrayFlag == "fromAreas"){
			tempIds = fromAreas;
			temNames = fromAreaNames;
			fromAreas = new Array();
			fromAreaNames = new Array();
			$("#fromAreas>li>span>input").each(function(){
				fromAreas.push($(this).val());
			});
			$("#fromAreas>li>span>font").each(function(){
				fromAreaNames.push($(this).text());
			});
		}else if(arrayFlag == "targetAreas"){
			tempIds = targetAreas;
			temNames = targetAreaNames;
			targetAreas = new Array();
			targetAreaNames = new Array();
			$("#targetAreas>li>span>input").each(function(){
				targetAreas.push($(this).val());
			});
			$("#targetAreas>li>span>font").each(function(){
				targetAreaNames.push($(this).text());
			});
		}else if(arrayFlag == "travelTypes"){
			tempIds = travelTypes;
			temNames = travelTypeNames;
			travelTypes = new Array();
			travelTypeNames = new Array();
			$("#travelTypes>li>span>input").each(function(){
				travelTypes.push($(this).val());
			});
			$("#travelTypes>li>span>font").each(function(){
				travelTypeNames.push($(this).text());
			});
		}else if(arrayFlag == "productTypes"){
			tempIds = productTypes;
			temNames = productTypeNames;
			productTypes = new Array();
			productTypeNames = new Array();
			$("#productTypes>li>span>input").each(function(){
				productTypes.push($(this).val());
			});
			$("#productTypes>li>span>font").each(function(){
				productTypeNames.push($(this).text());
			});
		}else if(arrayFlag == "productLevels"){
			tempIds = productLevels;
			temNames = productLevelNames;
			productLevels = new Array();
			productLevelNames = new Array();
			$("#productLevels>li>span>input").each(function(){
				productLevels.push($(this).val());
			});
			$("#productLevels>li>span>font").each(function(){
				productLevelNames.push($(this).text());
			});
		}
		if(fromAreas && fromAreas.length >0 && targetAreas && targetAreas.length>0 && travelTypes && travelTypes.length>0 && productTypes && productTypes.length >0 && productLevels && productLevels.length > 0){
			$.ajax({
				type:"POST",
				url:$("#sysUrl").val()+"/judgeExist",
				data:{fromAreas:fromAreas+"",targetAreas:targetAreas+"",travelTypes:travelTypes+"",productTypes:productTypes+"",productLevels:productLevels+"",priceStrategyId:$("#updateId")?$("#updateId").val():null},
				success:function(data){
					if('ok' == data.flag){
						callback();
						return true;
					}else if("ALREADY_EXISTS" == data.flag){
						if(arrayFlag == "fromAreas"){
							fromAreas= tempIds;
							fromAreaNames=temNames;
						}else if(arrayFlag == "targetAreas"){
							targetAreas=tempIds;
							targetAreaNames=temNames;
						}else if(arrayFlag == "travelTypes"){
							travelTypes= tempIds;
							travelTypeNames=temNames;
						}else if(arrayFlag == "productTypes"){
							productTypes= tempIds;
							productTypeNames=temNames;
						}else if(arrayFlag == "productLevels"){
							productLevels=tempIds;
							productLevelNames=temNames;
						}
						$.jBox.confirm("产品条件重复，不能添加","提示",function(v,h,f){
			        		if(v=='ok'){
			        			return;
			        		}
			        		if(v=='cancel'){
			        			return;
			        		}
			        	});
					}
				}
			});
		}else{
			callback();
		}
	}
	
	//    更多
    function more(moreId) {
        $("#"+moreId+".lookMore").show();
        $("#"+moreId+"M.more").hide();
        $("#"+moreId+"L.less").show();
    }
    
    //    收起
    function less(lessId) {
        $("#"+lessId+".lookMore").hide();
        $("#"+lessId+"M.more").show();
        $("#"+lessId+"L.less").hide();
    }
    
    //        checkbox按钮
    function checkBoxClick(obj,arrayFlag,checkVal,checkValName) {
        var $this = $(obj);
        if ($this.hasClass("notChecked")) {
        	pushValue(arrayFlag,checkVal,checkValName,function(){
        		$this.css("display", "none");
        		$this.parent().next().children().children().children(".notChecked").css("display", "none");
        		$this.siblings(".allChecked").css("display", "inline-block");
        		$this.parent().next().children().children().children(".allChecked").css("display", "inline-block");
        	});
        } else if ($this.hasClass("allChecked")) {
        	delValue(arrayFlag,checkVal,checkValName);
            $this.css("display", "none");
            $this.parent().next().children().children().children(".allChecked").css("display", "none");
            $this.siblings(".notChecked").css("display", "inline-block");
            $this.parent().next().children().children().children(".notChecked").css("display", "inline-block");
        } else {
            $this.css("display", "none");
            $this.parent().next().children().children().children(".notChecked").css("display", "none");
            $this.siblings(".allChecked").css("display", "inline-block");
            $this.parent().next().children().children().children(".allChecked").css("display", "inline-block");
        }
        var quanXuanLength = $this.parent().parent().parent().children().children().children(".allChecked").length;
        var quanXuanFirst = $this.parent().parent().parent().children().children().children(".allChecked").eq(0).css("display");
        for (var n = 0; n < quanXuanLength; n++) {
            var quanXuanAll = $this.parent().parent().parent().children().children().children(".allChecked").eq(n).css("display");
            if (quanXuanFirst != quanXuanAll) {
                $this.parent().parent().parent().parent().parent().children(".quanXuan").children(".notAllChecked").css("display", "inline-block");
                $this.parent().parent().parent().parent().parent().children(".quanXuan").children(".notChecked").css("display", "none");
                $this.parent().parent().parent().parent().parent().children(".quanXuan").children(".allChecked").css("display", "none");
                break;
            } else if (quanXuanFirst == quanXuanAll && quanXuanFirst == "none") {
                $this.parent().parent().parent().parent().parent().children(".quanXuan").children(".notAllChecked").css("display", "none");
                $this.parent().parent().parent().parent().parent().children(".quanXuan").children(".notChecked").css("display", "inline-block");
                $this.parent().parent().parent().parent().parent().children(".quanXuan").children(".allChecked").css("display", "none");
            } else if (quanXuanFirst == quanXuanAll && quanXuanFirst == "inline-block") {
                $this.parent().parent().parent().parent().parent().children(".quanXuan").children(".notAllChecked").css("display", "none");
                $this.parent().parent().parent().parent().parent().children(".quanXuan").children(".notChecked").css("display", "none");
                $this.parent().parent().parent().parent().parent().children(".quanXuan").children(".allChecked").css("display", "inline-block");
            }
        }
        var length = $(".zhanKaiEngland").children().children(".allChecked").length;
        var first = $(".zhanKaiEngland").children().children(".allChecked").eq(0).css("display");
        for (var i = 0; i < length; i++) {
            var all = $(".zhanKaiEngland").children().children(".allChecked").eq(i).css("display");
            if (first != all) {
                $this.parent().parent().parent().prev().children(".notAllChecked").css("display", "inline-block");
                $this.parent().parent().parent().prev().children(".notChecked").css("display", "none");
                $this.parent().parent().parent().prev().children(".allChecked").css("display", "none");
                $this.parent().parent().parent().parent().parent().parent().parent().children(".quanXuan").children(".notAllChecked").css("display", "inline-block");
                $this.parent().parent().parent().parent().parent().parent().parent().children(".quanXuan").children(".notChecked").css("display", "none");
                $this.parent().parent().parent().parent().parent().parent().parent().children(".quanXuan").children(".allChecked").css("display", "none");
                break;
            } else if (first == all && first == "none") {
                $this.parent().parent().parent().prev().children(".notAllChecked").css("display", "none");
                $this.parent().parent().parent().prev().children(".notChecked").css("display", "inline-block");
                $this.parent().parent().parent().prev().children(".allChecked").css("display", "none");
            } else if (first == all && first == "inline-block") {
                $this.parent().parent().parent().prev().children(".notAllChecked").css("display", "none");
                $this.parent().parent().parent().prev().children(".notChecked").css("display", "none");
                $this.parent().parent().parent().prev().children(".allChecked").css("display", "inline-block");
            }
            if (first == all && first == "none" && quanXuanFirst == quanXuanAll && quanXuanFirst == "none") {
                $this.parent().parent().parent().parent().parent().parent().parent().children(".quanXuan").children(".notAllChecked").css("display", "none");
                $this.parent().parent().parent().parent().parent().parent().parent().children(".quanXuan").children(".notChecked").css("display", "inline-block");
                $this.parent().parent().parent().parent().parent().parent().parent().children(".quanXuan").children(".allChecked").css("display", "none");
            } else if (first == all && first == "inline-block" && quanXuanFirst == quanXuanAll && quanXuanFirst == "inline-block") {
                $this.parent().parent().parent().parent().parent().parent().parent().children(".quanXuan").children(".notAllChecked").css("display", "none");
                $this.parent().parent().parent().parent().parent().parent().parent().children(".quanXuan").children(".notChecked").css("display", "none");
                $this.parent().parent().parent().parent().parent().parent().parent().children(".quanXuan").children(".allChecked").css("display", "inline-block");
            }
        }
    }
    
    //    全选按钮
    function allcheckBoxClick(obj,arrayFlag) {
        var $this = $(obj);
        if ($this.hasClass("notChecked")) {
        	pushAll(arrayFlag,function(){
        		$this.css("display", "none");
        		$this.siblings(".allChecked").css("display", "inline-block");
        		$this.parent().siblings().children().children().children().children(".allChecked").css("display", "inline-block");
        		$this.parent().siblings().children().children().children().children().children().children(".allChecked").css("display", "inline-block");
        		$this.parent().siblings().children().children().children().children(".notChecked").css("display", "none");
        		$this.parent().siblings().children().children().children().children().children().children(".notChecked").css("display", "none");
        	});
        } else if ($this.hasClass("allChecked")) {
        	delAll(arrayFlag);
            $this.css("display", "none");
            $this.siblings(".notChecked").css("display", "inline-block");
            $this.parent().siblings().children().children().children().children(".allChecked").css("display", "none");
            $this.parent().siblings().children().children().children().children().children().children(".allChecked").css("display", "none");
            $this.parent().siblings().children().children().children().children(".notChecked").css("display", "inline-block");
            $this.parent().siblings().children().children().children().children().children().children(".notChecked").css("display", "inline-block");
        } else {
            $this.css("display", "none");
            $this.siblings(".allChecked").css("display", "inline-block");
            $this.parent().siblings().children().children().children().children(".allChecked").css("display", "inline-block");
            $this.parent().siblings().children().children().children().children().children().children(".allChecked").css("display", "inline-block");
            $this.parent().siblings().children().children().children().children(".notChecked").css("display", "none");
            $this.parent().siblings().children().children().children().children(".notAllChecked").css("display", "none");
            $this.parent().siblings().children().children().children().children().children().children(".notChecked").css("display", "none");
        }
    }
    
    //    展开页
    $(".eColor").hover(function () {
        $(".eColorSpread").show();
        $(".spreadUp").css("display", "inline-block");
        $(".spreadDown").css("display", "none");
    }, function () {
        $(".eColorSpread").hide();
        $(".spreadUp").css("display", "none");
        $(".spreadDown").css("display", "inline-block")
    });
    
    //优惠内容添加按钮
    function add_select(obj){
        var $this=$(obj);
        var p=$this.parent().parent().clone();
        p.find(".heightInput").val("");
        p.children(".privilegesW").css("visibility","hidden");
        p.children(".relative").children(".absolute").hide();
        $this.parent().parent().after(p);
    }
    
    //优惠内容删除按钮
    function remove_selected(obj){
        var $this=$(obj);
        $this.parent().parent().remove();
    }
    
    //新增渠道优惠按钮
    function newTrench(){
        var table=$(".trenchBox").eq(0).clone();
        var agentTypes = "";
       $("select.chzn-select:eq(0)>option").each(function(obj){
    	   agentTypes = agentTypes+'<option value="'+$(this).val()+'" >'+$(this).text()+'</option>';
        });
       var agentLevels = "";
       $("select.chzn-select:eq(1)>option").each(function(obj){
    	   agentLevels = agentLevels+'<option value="'+$(this).val()+'" >'+$(this).text()+'</option>';
        });
        table.children("tbody").children("tr").children("td:first").remove();
        var td='<td>'
                +'<span class="marginLeft" style="margin-right: 6px">渠道类型：</span>'
                +'<select data-placeholder="请选择..."  class="chzn-select" multiple  id="dl_chose2">'
                + agentTypes
                +'</select>'
                +'<span class="marginLeft" style="margin-right: 6px">渠道等级：</span>'
                +'<select data-placeholder="请选择..."  class="chzn-select" multiple  id="dl_chose3">'
                +agentLevels
                +'</select>'
                +'</td>';
        table.children("tbody").children("tr").children("td:first").before(td);
        table.find("input").val("");
        var length=$(".trenchBox").length;
        table.find(".absolute").hide();
        $(".trenchBox").eq(length-1).after(table);
        $(".trenchBox").eq(length).find('.adult:gt(0)').remove();
        $(".trenchBox").eq(length).find('.children:gt(0)').remove();
        $(".trenchBox").eq(length).find('.specialCrowd:gt(0)').remove();
        $("select.selectW").change();
        $(".chzn-select").chosen();
    }
    
    //删除渠道优惠按钮
    function deleteTrench(obj){
        var $this=$(obj);
            $this.parent().parent().parent().parent().remove();
    }
    
    //    删除按钮显示隐藏功能判断
    $(function () {
        $(document).on('mouseover', '[name="selected"]', function () {
            if($(this).parent().children(("."+$(this).attr("class"))).length!=1){
                $(this).find('.remove-selected').css('display', 'inline-block');
            }
            $(this).parent().children(("."+$(this).attr("class"))).eq(0).find('.remove-selected').css('display', 'none');
        }).on('mouseleave', '[name="selected"]', function () {
            $(this).find('.remove-selected').hide();
        });
        //        渠道优惠DIV按钮
        $(document).on('mouseover', '[class="trenchBox"]', function () {
            if($("."+$(this).attr("class")).length!=1){
                $(this).children().children().children().children('.remove-selected').css('display', 'inline-block');
            }
        }).on('mouseleave', '[class="trenchBox"]', function () {
            $(this).find('.remove-selected').hide();
        });
    });
    
    //    %
    function zheKou(obj){
        var $this=$(obj);
        if($this.find("option:selected").text()=="折扣"){
            $this.parent().next().children(".zheKou").show();
            $this.parent().next().children('.heightInput').keyup();
        }
       /* else if($this.find("option:selected").text()=="直减"){
            $this.parent().next().children(".zheKou").hide();
        }*/
        else{
            $this.parent().next().children(".zheKou").hide();
        }
    }
    
    
    //添加价格策略
    function addPriceTrategy(ctx,priceStrategyId){
    	var msg = "";
    	//校验必填项是否填写正确
    	if(fromAreas.length < 1 ){
    		//出发城市是必选项
    		msg = "出发城市是必选项";
    	}
    	if(targetAreas.length < 1 && msg == ""){
    		//线路是必选项
    		msg = "线路是必选项";
    	}
    	if(travelTypes.length < 1 && msg == ""){
    		//旅游类型是必选项
    		msg = "旅游类型是必选项";
    	}
    	if(productTypes.length < 1 && msg == ""){
    		//产品类型是必选项
    		msg = "产品类型是必选项";
    	}
    	if(productLevels.length < 1 && msg == ""){
    		//产品系列是必选项
    		msg = "产品系列是必选项";
    	}
    	
    	var priceTrategyMsgsValue = "";
    	$("#priceTrategyMsgs>table").each(function(){
    		var temStr = "";
    		//渠道类型  非空
    		var agentTypeNames = "";
    		var agentTypeIds = "";
    		$(this).find('ul.chosen-choices:eq(0)>li.search-choice').each(function(){
    			var tempTypeName =$(this).find('span').text();
    			if(tempTypeName != null && tempTypeName != ""){
    				agentTypeNames =  agentTypeNames +tempTypeName+"," ;
    				var tempIndex = $(this).find('a').attr('data-option-array-index');
    				agentTypeIds = agentTypeIds +$(this).parent().parent().prev().find("option:eq("+tempIndex+")").val()+",";
    			}
    		});
    		if((agentTypeNames == null || agentTypeNames.trim() == "")&&msg == ""){
    			msg="渠道类型不能为空";
    		}
    		temStr = temStr+agentTypeIds+"-"+agentTypeNames;//0,1
    		//渠道等级  非空
    		var agentLevelNames = "";
    		var agentLevelIds = "";
    		$(this).find('ul.chosen-choices:eq(1)>li.search-choice').each(function(){
    			var tempLevelName =  $(this).find('span').text();
    			if(tempLevelName != null && tempLevelName !=""){
    				agentLevelNames = agentLevelNames +tempLevelName+",";
    				var tempIndex = $(this).find('a').attr('data-option-array-index');
    				agentLevelIds = agentLevelIds +$(this).parent().parent().prev().find("option:eq("+tempIndex+")").val()+",";
    			}
    		});
    		if((agentLevelNames == null || agentLevelNames.trim() == "") && msg == ""){
    			msg ="渠道等级不能为空";
    		}
    		temStr = temStr+"-"+agentLevelIds+"-"+agentLevelNames;//2,3
    		//成人价格
    		var audltValue = "";
    		$(this).find('select[name="adultValues"]').each(function(){
    			var audltTemp = $(this).parent().next().children("input").val()
    			if((audltTemp == null || audltTemp.trim() == "") && msg == "" && audltValue == ""){
    				msg = "请填写成人优惠价内容";
    			}else if(audltTemp == null || audltTemp.trim() == ""){
    				
    			}else{
    				audltValue = audltValue+$(this).find("option:selected").val()+"_"+$(this).find("option:selected").text()+":"+audltTemp+",";
    			}
    		});
    		temStr = temStr+"-"+audltValue;//4
    		//儿童价额
    		var childrenValues = "";
    		$(this).find('select[name="childrenValues"]').each(function(){
    			var audltTemp = $(this).parent().next().children("input").val()
    			if((audltTemp == null || audltTemp.trim() == "") && msg == ""){
    				/*msg = "请填写儿童优惠价内容";*/
//    				childrenValues = childrenValues+$(this).find("option:selected").val()+"_"+$(this).find("option:selected").text()+":"+$(this).parent().next().children("input").val()+",";
    			}else if(audltTemp == null || audltTemp.trim() == ""){
    			}else{
    				childrenValues = childrenValues+$(this).find("option:selected").val()+"_"+$(this).find("option:selected").text()+":"+$(this).parent().next().children("input").val()+"_child"+",";
    			}
    		});
    		if(childrenValues != ""){
    			temStr = temStr+"-"+childrenValues;//5
    		}
    		//特殊人群价格
    		var specialCrowdValues = "";
    		$(this).find('select[name="specialCrowdValues"]').each(function(){
    			var audltTemp = $(this).parent().next().children("input").val()
    			if((audltTemp == null || audltTemp.trim() == "") && msg == ""){
    				/*msg = "请填写特殊人群优惠价内容";*/
//    				specialCrowdValues = specialCrowdValues+$(this).find("option:selected").val()+"_"+$(this).find("option:selected").text()+":"+$(this).parent().next().children("input").val()+",";
    			}else if(audltTemp == null || audltTemp.trim() == ""){
    			}else{
    				specialCrowdValues = specialCrowdValues+$(this).find("option:selected").val()+"_"+$(this).find("option:selected").text()+":"+$(this).parent().next().children("input").val()+"_special"+",";
    				
    			}
    		});
    		if(specialCrowdValues != ""){
    			temStr = temStr+"-"+specialCrowdValues;//6
    		}
    		//agentPriceStrategyId
    		/*if(priceStrategyId){
    			if($(this).children("input[name='angentPriceStrategyId']")&&$(this).children("input[name='angentPriceStrategyId']").val().trim()!=""){
    				temStr = temStr+"-"+$(this).children("input[name='angentPriceStrategyId']").val();//7
    			}else{
    				temStr = temStr+"-"+"null";
    			}
    		}*/
    		temStr = temStr+"+"
    		priceTrategyMsgsValue = priceTrategyMsgsValue+temStr;
    	});
    	
//    	alert(priceTrategyMsgsValue);
    	var tempArray = priceTrategyMsgsValue.split("+");
    	for(var i = 0; i < tempArray.length; ++i){
    		for(var j = i+1; j < tempArray.length; ++j){
    			if(tempArray[i] == tempArray[j]){
    				if(msg == ""){
    					msg="价格策略不能相同，请修改";
    				}
    			}
    			if(msg=="" && tempArray[i].trim() != "" && tempArray[j].trim() != ""){
    				var val12 = tempArray[i].split('-');
    				var val1122 = tempArray[j].split('-');
    				if(judgeIsContain(val12[0],val12[2],val1122[0],val1122[2])){
    					msg="渠道等级和渠道类型有重复，请修改";
    				}
    			}
    		}
    	}
    	if(msg != ""){
    		$.jBox.confirm(msg,"提示",function(v,h,f){
        		if(v=='ok'){
        			return;
        		}
        		if(v=='cancel'){
        			return;
        		}
        	});
    	}else{
    		var targetUrl = ctx;
    		if(priceStrategyId){
    			ctx=ctx+"/update";
    		}else{
    			ctx=ctx+"/add";
    		}
//    		return;
    		$.ajax({
				type:"POST",
				url:ctx,
				data:{fromAreas:fromAreas+"",targetAreas:targetAreas+"",travelTypes:travelTypes+"",productTypes:productTypes+"",productLevels:productLevels+"",priceTrategyMsgsValue:priceTrategyMsgsValue,fromAreaNames:fromAreaNames+"",targetAreaNames:targetAreaNames+"",travelTypeNames:travelTypeNames+"",productTypeNames:productTypeNames+"",productLevelNames:productLevelNames+"",priceStrategyId:priceStrategyId},
				success:function(data){
					if('ok' == data.flag){
						loading('正在提交，请稍等...');
						window.location.href = targetUrl + "/list";
					}else if("ALREADY_EXISTS" == data.flag){
						$.jBox.confirm("产品条件重复，不能添加","提示",function(v,h,f){
			        		if(v=='ok'){
			        			return;
			        		}
			        		if(v=='cancel'){
			        			return;
			        		}
			        	});
					}else if(null !=data.errorMsg && "" != data.errorMsg){
						$.jBox.confirm(data.errorMsg,"提示",function(v,h,f){
			        		if(v=='ok'){
			        			return;
			        		}
			        		if(v=='cancel'){
			        			return;
			        		}
			        	});
					}
				}
			})
    	}
    };
    
    /**
     * 取消提交
     */
    cancleSubmit = function(ctx){
    	window.location.href = ctx + "/list";
    }
    
    
    judgeIsContain = function(var1,var2,var11,var22){
    	var1 = var1.replace(',',' ').trim().split(" ");
    	var2 = var2.replace(',',' ').trim().split(" ");
    	for(var i=0; i < var1.length; ++i) {
    	    for(var j=0; j < var2.length; ++j){
    	    	if(var11.indexOf(var1[i]) != -1 && var22.indexOf(var2[j]) != -1) return true;
    	    }
    	}
    }
    
    /**
     * 判断价格格式是否正确
     * @param obj
     */
    function changeClearPriceSum(obj){
        var money = obj.value;
        if(money && money != ""){
            if(money >= 0){
                var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
                var txt = ms.split(".");
                if( txt[0] &&  txt[0].length > 9 && $(obj).next().css("display")=='none'){
                	txt[0] = txt[0].substring(0,9);
                }else if(txt[0] &&  txt[0].length > 2 && $(obj).next().css("display")!='none'){
                	if(txt[0] >100){
                		txt[0] = txt[0].substring(0,3);
                	}
                	if(txt[0] >100){
                		txt[0] = txt[0].substring(0,2);
                	}
                }
                obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
            }else{
                obj.value = '';
            }
        }
//        changeClearPriceByInputChange($(obj).closest("form"));
    }