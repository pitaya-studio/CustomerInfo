/**
 * Created by xcihao sun on 2016/02/02.
 */
//库存列表-关联记录弹出层

//库存列表-关联记录弹出层

function jbox_relevance_record_pop() {
    $.jBox($("#relevance-record-pop").html(), {
        title: "关联记录",submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: 300, width: 500,
        persistent: true
    });
    inquiryCheckBOX();
}

//库存列表-批量删除弹出层

function jbox_relevance_record_del_pop() {
	var boxes=$("input[name='ids']:checked");
	if(boxes.size()<1){
		top.$.jBox.tip("请选择产品",'warning');
		return;
	}
	var uuid="";
	for(var i=0;i<boxes.size();i++){
		if(uuid==""){
			uuid=$(boxes[i]).val();
		}else{
			uuid=uuid+","+$(boxes[i]).val();
		}
	}
	top.$.jBox.confirm('确认删除么？','系统提示',function(v){
		if(v=="ok"){
			$.ajax({
	    		type:"POST",
	    		url:$ctx+"/cruiseshipStock/cruiseshipStockGroupRelBatchDelete",
	    		data:{
	    			uuids:uuid
	    		},
	    		success:function(data){
	    			console.log(data);
	    			if(data=="库存已关联，删除失败"){
	    				top.$.jBox.tip("库存已关联，删除失败!","error");
	    			}else{
	    				top.$.jBox.tip("删除成功","success");
	    				location.href=$ctx+"/cruiseshipStock/cruiseshipStockList";
	    			}
	    		}
	    	});
		}
	});
}


//库存批量修改-弹出层

function jbox_relevance_record_edit_batch_pop() {
    $pop= $.jBox($("#relevance-record-edit-batch-pop").html(), {
        title: "选择舱型", buttons: {'关闭': 0, '保存': 1}, submit: function (v, h, f) {
            if (v == "1") {
                var $cabinOrStock = h.find('[name="cabinOrStock"]');
                var $addOrReduce = h.find('[name="operate"]');
                var $cabinList = h.find('[name="cabinList"]');
                //选中的舱型id数组
                var cabinUuids = [];
                $cabinList.find('[type="checkbox"]:checked').each(function(){
                    cabinUuids.push($(this).attr('uuid'));
                });
                if(cabinUuids.length==0){
                    $.jBox.tip('请选择要修改的舱型');
                    return false;
                }
                if($cabinOrStock.val()=='-1'){
                    $.jBox.tip('请选择修改库存或余位');
                    return false;
                }
                var operateCount = h.find('[name="operateCount"]').val();
                if(operateCount==''){
                    $.jBox.tip('请输入要修改的数量');
                    return false;
                }
                operateCount = +operateCount;
                var cabinOrStock=$cabinOrStock.val();
                var addOrReduce=$addOrReduce.val();
                var stockUuid=$("#stockUuid").val();
                var stockAndSurplusNums = getStockAndSurplus(cabinUuids);
               if(isOk=="true"){ 
                if($cabinOrStock.val()=='0'){
                    $.jBox.confirm('是否同时修改余位','系统提示',function(v, h, f){
                        if(v=='ok'){
                            var bool=updateStock($addOrReduce.val(),0,stockAndSurplusNums,operateCount);
                            if(bool){
                            	return;
                            }
                            var uuids="";
                        	var $checkbox=$("input[type='checkbox']:checked");
                        	for(var i=0;i<$checkbox.size();i++){
                        		if(uuids==""){
                        			uuids=$($checkbox[i]).val();
                        		}else{
                        			uuids=uuids+","+$($checkbox[i]).val();
                        		}
                        	}
                            $.ajax({                 
                				type: "POST",                 
                				url:$ctx+"/cruiseshipStock/cruiseshipStockGroupRelUpdate",                    
                				data:{
                					"cabinOrStock":cabinOrStock,
                            		"operateCount":operateCount,
                            		"operate":addOrReduce,
                            		"stockUuid":stockUuid,
                            		"isOk":1,
                            		"uuid":uuids
                				}, 
                				dataType: "json",
                				success: function(data) {
                						
                				}             
                			});
                        }else{
                            var bool=updateStock($addOrReduce.val(),1,stockAndSurplusNums,operateCount);
                            if(bool){
                            	return;
                            }
                            var uuids="";
                        	var $checkbox=$("input[type='checkbox']:checked");
                        	for(var i=0;i<$checkbox.size();i++){
                        		if(uuids==""){
                        			uuids=$($checkbox[i]).val();
                        		}else{
                        			uuids=uuids+","+$($checkbox[i]).val();
                        		}
                        	}
                            $.ajax({                 
                				type: "POST",                 
                				url:$ctx+"/cruiseshipStock/cruiseshipStockGroupRelUpdate",                    
                				data:{
                					"cabinOrStock":cabinOrStock,
                            		"operateCount":operateCount,
                            		"operate":addOrReduce,
                            		"stockUuid":stockUuid,
                            		"isOk":0,
                            		"uuid":uuids
                				}, 
                				dataType: "json",
                				success: function(data) {
                						
                				}             
                			});
                        }
                    })
                }else{
                    $.jBox.confirm('是否同时修改库存','系统提示',function(v, h, f){
                        if(v=='ok'){
                            var bool=updateSurplus($addOrReduce.val(),0,stockAndSurplusNums,operateCount);
                            if(bool){
                            	return;
                            }
                            var uuids="";
                        	var $checkbox=$("input[type='checkbox']:checked");
                        	for(var i=0;i<$checkbox.size();i++){
                        		if(uuids==""){
                        			uuids=$($checkbox[i]).val();
                        		}else{
                        			uuids=uuids+","+$($checkbox[i]).val();
                        		}
                        	}
                            $.ajax({                 
                				type: "POST",                 
                				url:$ctx+"/cruiseshipStock/cruiseshipStockGroupRelUpdate",                    
                				data:{
                					"cabinOrStock":cabinOrStock,
                            		"operateCount":operateCount,
                            		"operate":addOrReduce,
                            		"stockUuid":stockUuid,
                            		"isOk":1,
                            		"uuid":uuids
                				}, 
                				dataType: "json",
                				success: function(data) {
                						
                				}             
                			});
                        }else{
                           var bool= updateSurplus($addOrReduce.val(),1,stockAndSurplusNums,operateCount);
                           if(bool){
                        	   return;
                           }
                            var uuids="";
                        	var $checkbox=$("input[type='checkbox']:checked");
                        	for(var i=0;i<$checkbox.size();i++){
                        		if(uuids==""){
                        			uuids=$($checkbox[i]).val();
                        		}else{
                        			uuids=uuids+","+$($checkbox[i]).val();
                        		}
                        	}
                            $.ajax({                 
                				type: "POST",                 
                				url:$ctx+"/cruiseshipStock/cruiseshipStockGroupRelUpdate",                    
                				data:{
                					"cabinOrStock":cabinOrStock,
                            		"operateCount":operateCount,
                            		"operate":addOrReduce,
                            		"stockUuid":stockUuid,
                            		"isOk":0,
                            		"uuid":uuids
                				}, 
                				dataType: "json",
                				success: function(data) {
                						
                				}             
                			});
                        }
                    })
                }
                return false;
              }else{
            	   var bool=updateStock($addOrReduce.val(),0,stockAndSurplusNums,operateCount);
            	   if(bool){
            		   return;
            	   }
                   var uuids="";
               	var $checkbox=$("input[type='checkbox']:checked");
               	for(var i=0;i<$checkbox.size();i++){
               		if(uuids==""){
               			uuids=$($checkbox[i]).val();
               		}else{
               			uuids=uuids+","+$($checkbox[i]).val();
               		}
               	}
                   $.ajax({                 
       				type: "POST",                 
       				url:$ctx+"/cruiseshipStock/cruiseshipStockGroupRelUpdate",                    
       				data:{
       					"cabinOrStock":cabinOrStock,
                   		"operateCount":operateCount,
                   		"operate":addOrReduce,
                   		"stockUuid":stockUuid,
                   		"isOk":1,
                   		"uuid":uuids
       				}, 
       				dataType: "json",
       				success: function(data) {
       						
       				}             
       			});
              }   
            }
        },loaded:function(h){
            renderCabinSelect(h);
            renderCabinOrStock(h);
        }, height: 200, width: 500,
        persistent: true
    });
    inquiryCheckBOX();
    
    $pop.find("#jbox-content").parent().attr("style","min-width:50px;height:122px;");
}
/**
 * 批量修改库存
 * @param type 1增加  2减少
 * @param updateSurplus 是否更新余位 0更新 1不更新
 * @param stockAndSurplusNums 包含库存数量余位数量的数组
 * @param operateCount 变化的数量
 */
function updateStock(type,updateSurplus,stockAndSurplusNums,operateCount){
	var bool=false;
    for(var i= 0,len=stockAndSurplusNums.length;i<len;i++){
        var stockAndSurplusNumInfo = stockAndSurplusNums[i];
        var stockNum = stockAndSurplusNumInfo.stockNum;
        var surplusNum = stockAndSurplusNumInfo.surplusNum;
        //增加
        if(type==1){
            //更新余位
            if(updateSurplus==0){
                stockAndSurplusNumInfo.stockNum+=operateCount;
                stockAndSurplusNumInfo.surplusNum+=operateCount;
            }else{
                stockAndSurplusNumInfo.stockNum+=operateCount;
            }
        }
        //减少
        else{
            if(updateSurplus==0){
                if(surplusNum-operateCount<0){
                    $.jBox.tip('减少数量超出余位数量,请修改!');
                    return true;
                }else{
                    stockAndSurplusNumInfo.stockNum-=operateCount;
                    stockAndSurplusNumInfo.surplusNum-=operateCount;
                }
            }else{
                if(stockNum-operateCount<0){
                    $.jBox.tip('减少数量超出库存数量,请修改!');
                    return true;
                }else if(stockNum-operateCount<surplusNum){
                    $.jBox.tip('库存数小于余位数,请修改!');
                    return true;
                }else{
                    stockAndSurplusNumInfo.stockNum-=operateCount;
                }
            }
        }
    }
    setStockAndSurplus(stockAndSurplusNums);
    $.jBox.close(true);
    return bool;
}
/**
 * 批量修改余位
 * @param type 1增加  2减少
 * @param updateStock 是否更新库存 0更新 1不更新
 * @param stockAndSurplusNums 包含库存数量余位数量的数组
 * @param operateCount 变化的数量
 * return msg 错误提示信息
 */
function updateSurplus(type,updateStock,stockAndSurplusNums,operateCount){
	var bool=false;
    for(var i= 0,len=stockAndSurplusNums.length;i<len;i++){
        var stockAndSurplusNumInfo = stockAndSurplusNums[i];
        var stockNum = stockAndSurplusNumInfo.stockNum;
        var surplusNum = stockAndSurplusNumInfo.surplusNum;
        //增加
        if(type==1){
            //更新库存
            if(updateStock==0){
                stockAndSurplusNumInfo.stockNum+=operateCount;
                stockAndSurplusNumInfo.surplusNum+=operateCount;
            }else{
                if(surplusNum+operateCount>stockNum){
                    $.jBox.tip('余位数超过库存数,请修改!');
                    return true;
                }else{
                    stockAndSurplusNumInfo.surplusNum+=operateCount;
                }
            }
        }
        //减少
        else{
            if(surplusNum-operateCount<0){
                $.jBox.tip('减少数量超出余位数量,请修改!');
                return true;
            }else{
                if(updateStock==0){
                    stockAndSurplusNumInfo.stockNum-=operateCount;
                    stockAndSurplusNumInfo.surplusNum-=operateCount;
                }else{
                    stockAndSurplusNumInfo.surplusNum-=operateCount;
                }
            }
        }
    }
    setStockAndSurplus(stockAndSurplusNums);
    $.jBox.close(true);
}

function renderCabinSelect(h){
    var $ul = h.find('[name="cabinList"]');
    $ul.empty();
    var cabinList = getCabinList();
    for(var i=0,len=cabinList.length;i<len;i++){
        var cabinInfo = cabinList[i];
        $ul.append('<li><input type="checkbox" uuid='+cabinInfo.cabinUuid+'  value='+cabinInfo.uuid+'><label>'+cabinInfo.cabinName+'</label></li>')
    }
}

function renderCabinOrStock(h){
    //是否关联
    var isConnect = isOk;
    var $cabinOrStock = h.find('[name="cabinOrStock"]');
    $cabinOrStock.empty();
    $('<option value="-1">请选择</option>').appendTo($cabinOrStock);
    $('<option value="0">库存</option>').appendTo($cabinOrStock);
    if(isConnect=="true"){
        $('<option value="1">余位</option>').appendTo($cabinOrStock);
    }
}
//设置库存与余位
function setStockAndSurplus(stockAndSurplusNums){
    for(var i= 0,len=stockAndSurplusNums.length;i<len;i++){
        var stockAndSurplusInfo = stockAndSurplusNums[i];
        var $tr = $('#contentTable').find('#'+stockAndSurplusInfo.cabinUuid);
        $tr.find('[name="stockNum"]').text(stockAndSurplusInfo.stockNum);
        $tr.find('[name="surplusNum"]').text(stockAndSurplusInfo.surplusNum);
    }
}
//获取库存与余位
function getStockAndSurplus(cabinUuids){
    var stockAndSurplusNums = [];
    for(var i=0,len=cabinUuids.length;i<len;i++){
        var $tr = $('#contentTable').find('#'+cabinUuids[i]);
        var stockAndSurplusInfo = {
            cabinUuid:cabinUuids[i],
            stockNum:+$tr.find('[name="stockNum"]').text(),
            surplusNum:+$tr.find('[name="surplusNum"]').text()
        }
        stockAndSurplusNums.push(stockAndSurplusInfo);
    }
    return stockAndSurplusNums;
}

//获取舱型信息
function getCabinList(){
    var cabinList = [];
    $('#contentTable tbody tr').each(function(){
        var $this = $(this);
        var cabin = {
            cabinUuid:$this.prop('id'),
            cabinName:$this.find('[name="cabinName"]').text(),
            uuid:$this.find("input[name='uuid']").val()
        }
        cabinList.push(cabin);
    });
    return cabinList;
}

//库存修改-弹出层
function jbox_relevance_record_edit_pop(obj) {
    var $tr = $(obj).parents('tr:first');
    //库存总数
    var $stockNum = $tr.find('[name="stockNum"]');
    var stockNum = +$stockNum.text();
    //余位
    var $surplusNum = $tr.find('[name="surplusNum"]');
    var surplusNum = +$surplusNum.text();
    var $pop = $.jBox($("#relevance-record-edit-pop").html(), {
        title: "修改", buttons: {'关闭': 0, '保存': 1}, submit: function (v, h, f) {
            if (v == "1") {
                var $cabinOrStock = h.find('[name="cabinOrStock"]');
                var $addOrReduce = h.find('[name="operate"]');
                if($cabinOrStock.val()=='-1'){
                    $.jBox.tip('请选择修改库存或余位');
                    return false;
                }
                var operateCount = h.find('[name="operateCount"]').val();
                if(operateCount==''){
                    $.jBox.tip('请输入要修改的数量');
                    return false;
                }
                operateCount = operateCount;
               var cabinOrStock=$cabinOrStock.val();
               var addOrReduce=$addOrReduce.val();
               var stockUuid=$("#stockUuid").val();
               var $uuid=$tr.find("input[name='uuid']");
               var uuid=$uuid.val();
                if (isOk=="true") {
					                //增加
                if($addOrReduce.val()=='1'){
                    //选择库存
                    if($cabinOrStock.val()=='0'){
                        $.jBox.confirm('是否同时修改余位','系统提示',function(v, h, f){
                            if(v=='ok'){
                                $stockNum.text(parseInt(stockNum)+parseInt(operateCount));
                                $surplusNum.text(parseInt(surplusNum)+parseInt(operateCount));
                                $.ajax({                 
                    				type: "POST",                 
                    				url:$ctx+"/cruiseshipStock/cruiseshipStockGroupRelUpdate",                    
                    				data:{
                    					"cabinOrStock":cabinOrStock,
                                		"operateCount":operateCount,
                                		"operate":addOrReduce,
                                		"stockUuid":stockUuid,
                                		"isOk":1,
                                		"uuid":uuid
                    				}, 
                    				dataType: "json",
                    				success: function(data) {
                    						
                    				}             
                    			});
                            }else{
                                $stockNum.text(parseInt(stockNum)+parseInt(operateCount));
                                $.ajax({                 
                    				type: "POST",                 
                    				url:$ctx+"/cruiseshipStock/cruiseshipStockGroupRelUpdate",                    
                    				data:{
                    					"cabinOrStock":cabinOrStock,
                                		"operateCount":operateCount,
                                		"operate":addOrReduce,
                                		"stockUuid":stockUuid,
                                		"isOk":0,
                                		"uuid":uuid
                    				}, 
                    				dataType: "json",
                    				success: function(data) {
                    						
                    				}             
                    			});
                            }
                            $.jBox.close(true);
                        })
                    }else{
                        $.jBox.confirm('是否同时修改库存','系统提示',function(v, h, f){
                            if(v=='ok'){
                            	isOk=1;
                                $stockNum.text(parseInt(stockNum)+parseInt(operateCount));
                                $surplusNum.text(parseInt(surplusNum)+parseInt(operateCount));
                                $.ajax({                 
                    				type: "POST",                 
                    				url:$ctx+"/cruiseshipStock/cruiseshipStockGroupRelUpdate",                    
                    				data:{
                    					"cabinOrStock":cabinOrStock,
                                		"operateCount":operateCount,
                                		"operate":addOrReduce,
                                		"stockUuid":stockUuid,
                                		"isOk":1,
                                		"uuid":uuid
                    				}, 
                    				dataType: "json",
                    				success: function(data) {
                    						
                    				}             
                    			});
                            }else{
                                if(parseInt(surplusNum)+parseInt(operateCount)>parseInt(stockNum)){
                                    $.jBox.tip('余位数超过库存数,请修改!');
                                    return ;
                                }else{
                                    $surplusNum.text(parseInt(surplusNum)+parseInt(operateCount));
                                    $.ajax({                 
                        				type: "POST",                 
                        				url:$ctx+"/cruiseshipStock/cruiseshipStockGroupRelUpdate",                    
                        				data:{
                        					"cabinOrStock":cabinOrStock,
                                    		"operateCount":operateCount,
                                    		"operate":addOrReduce,
                                    		"stockUuid":stockUuid,
                                    		"isOk":0,
                                    		"uuid":uuid
                        				}, 
                        				dataType: "json",
                        				success: function(data) {
                        						
                        				}             
                        			});
                                }
                            }
                            $.jBox.close(true);
                        })
                    }
                }
                //减少
                else{
                    //选择库存
                    if($cabinOrStock.val()=='0'){
                        $.jBox.confirm('是否同时修改余位','系统提示',function(v, h, f){
                            if(v=='ok'){
                                if(parseInt(surplusNum)-parseInt(operateCount)<0){
                                    $.jBox.tip('减少数量超出余位数量,请修改!');
                                    return ;
                                }else{
                                    $stockNum.text(parseInt(stockNum)-parseInt(operateCount));
                                    $surplusNum.text(parseInt(surplusNum)-parseInt(operateCount));
                                    $.ajax({                 
                        				type: "POST",                 
                        				url:$ctx+"/cruiseshipStock/cruiseshipStockGroupRelUpdate",                    
                        				data:{
                        					"cabinOrStock":cabinOrStock,
                                    		"operateCount":operateCount,
                                    		"operate":addOrReduce,
                                    		"stockUuid":stockUuid,
                                    		"isOk":1,
                                    		"uuid":uuid
                        				}, 
                        				dataType: "json",
                        				success: function(data) {
                        						
                        				}             
                        			});
                                }
                            }else{
                                if(parseInt(stockNum)-parseInt(operateCount)<0){
                                    $.jBox.tip('减少数量超出库存数量,请修改!');
                                    return ;
                                }else if(parseInt(stockNum)-parseInt(operateCount)<parseInt(surplusNum)){
                                    $.jBox.tip('库存数小于余位数,请修改');
                                    return ;
                                }else{
                                    $stockNum.text(parseInt(stockNum)-parseInt(operateCount));
                                    $.ajax({                 
                        				type: "POST",                 
                        				url:$ctx+"/cruiseshipStock/cruiseshipStockGroupRelUpdate",                    
                        				data:{
                        					"cabinOrStock":cabinOrStock,
                                    		"operateCount":operateCount,
                                    		"operate":addOrReduce,
                                    		"stockUuid":stockUuid,
                                    		"isOk":0,
                                    		"uuid":uuid
                        				}, 
                        				dataType: "json",
                        				success: function(data) {
                        						
                        				}             
                        			});
                                }
                            }
                            $.jBox.close(true);
                        })
                    }else{
                        $.jBox.confirm('是否同时修改库存','系统提示',function(v, h, f){
                            if(v=='ok'){
                                if(parseInt(surplusNum)-parseInt(operateCount)<0){
                                    $.jBox.tip('减少数量超出余位数量,请修改!');
                                    return ;
                                }else{
                                    $stockNum.text(parseInt(stockNum)-parseInt(operateCount));
                                    $surplusNum.text(parseInt(surplusNum)-parseInt(operateCount));
                                    $.ajax({                 
                        				type: "POST",                 
                        				url:$ctx+"/cruiseshipStock/cruiseshipStockGroupRelUpdate",                    
                        				data:{
                        					"cabinOrStock":cabinOrStock,
                                    		"operateCount":operateCount,
                                    		"operate":addOrReduce,
                                    		"stockUuid":stockUuid,
                                    		"isOk":1,
                                    		"uuid":uuid
                        				}, 
                        				dataType: "json",
                        				success: function(data) {
                        						
                        				}             
                        			});
                                }
                            }else{
                                if(parseInt(surplusNum)-parseInt(operateCount)<0){
                                    $.jBox.tip('减少数量超出余位数量,请修改!');
                                    return ;
                                }else{
                                    $surplusNum.text(parseInt(surplusNum)-parseInt(operateCount));
                                    $.ajax({                 
                        				type: "POST",                 
                        				url:$ctx+"/cruiseshipStock/cruiseshipStockGroupRelUpdate",                    
                        				data:{
                        					"cabinOrStock":cabinOrStock,
                                    		"operateCount":operateCount,
                                    		"operate":addOrReduce,
                                    		"stockUuid":stockUuid,
                                    		"isOk":0,
                                    		"uuid":uuid
                        				}, 
                        				dataType: "json",
                        				success: function(data) {
                        						
                        				}             
                        			});
                                }
                            }
                            $.jBox.close(true);
                        })
                    }
                }
                return false;
		}else{
			if($addOrReduce.val()=='1'){
				 $stockNum.text(parseInt(stockNum)+parseInt(operateCount));
                 $surplusNum.text(parseInt(stockNum)+parseInt(operateCount));
                 $.ajax({                 
     				type: "POST",                 
     				url:$ctx+"/cruiseshipStock/cruiseshipStockGroupRelUpdate",                    
     				data:{
     					"cabinOrStock":cabinOrStock,
                 		"operateCount":operateCount,
                 		"operate":addOrReduce,
                 		"stockUuid":stockUuid,
                 		"isOk":1,
                 		"uuid":uuid
     				}, 
     				dataType: "json",
     				success: function(data) {
     						
     				}             
     			});
                 $.jBox.close(true);
			}else{
				 if(parseInt(stockNum)-parseInt(operateCount)<0){
                     $.jBox.tip('减少数量超出余位数量,请修改!');
                     return ;
                 }else{
                     $stockNum.text(parseInt(stockNum)-parseInt(operateCount));
                     $surplusNum.text(parseInt(stockNum)-parseInt(operateCount));
                     $.ajax({                 
         				type: "POST",                 
         				url:$ctx+"/cruiseshipStock/cruiseshipStockGroupRelUpdate",                    
         				data:{
         					"cabinOrStock":cabinOrStock,
                     		"operateCount":operateCount,
                     		"operate":addOrReduce,
                     		"stockUuid":stockUuid,
                     		"isOk":1,
                     		"uuid":uuid
         				}, 
         				dataType: "json",
         				success: function(data) {
         						
         				}             
         			});
			}
		}
			  $.jBox.close(true);
		}}
		},loaded:function(h){
            renderCabinOrStock(h);
        },height: 200, width: 500,
        persistent: true
    });
    //inquiryCheckBOX();
}
//库存修改记录
function jbox_relevance_edit_record_pop(uuid) {
    $.jBox($("#relevance-edit-record-pop").html(), {
        title: "修改记录",submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: 300, width: 500,
        persistent: true
    });
    inquiryCheckBOX();
}

//生成团控表   对应需求  223
function jbox_create_group_control_pop(thisObj) {
	
	// debugger;
	var groupCodeArray = new Array();
	//修改时  对游轮  有新增舱型（游轮产品的团号重复）的情况进行校验
	//同一产品的团号  如有重复的   只能其中 一个进行  关联
	$("#contentTable").find("input[name=groupCode]").each(function(){
		var cruiseGroupControlIdTemp = $(this).parents("tr:first").find('input[name=cruiseGroupControlId]').val();
		var groupCodeTemp = $(this).parents("tr:first").find('input[name=groupCode]').val();
		if(""!=groupCodeTemp){
			//alert(groupCodeTemp);
			if(""!=cruiseGroupControlIdTemp){
				groupCodeArray.push(groupCodeTemp);
			}
		}
	});
	
	//发送ajax请求查询游轮名称的下拉列表信息
	var htmlTmp="";
	var pathHeader=$("#ctx").val();
	//拼接生成团控列表的弹窗
	htmlTmp+='<div id="create-group-control-pop" class="">';
	htmlTmp+='<div class="create-group-control-pop">';
	htmlTmp+='<div class="part-f">';
	htmlTmp+='<span>';
	htmlTmp+='<label for=""><i class="xing">*</i>游轮名称</label>';
	htmlTmp+='<select name="selectedCruiseshipName" id="selectedCruiseshipName" onchange="changeCruiseshipName(this);">';
	htmlTmp+='<option value="-1">请选择</option>';
	//发送ajax请求查询游轮的名称
	//alert(pathHeader);
	$.ajax({
	   url:pathHeader+"/activity/manager/info4CruiseshipNamesUuids",
	   type:"post",
	   async:false,
	   success:function(res){
		   var infos=res.cruiseshipNamesUuids;
		   if("none"!=infos){ //游轮名称不为空时才进行拼接
			   for(var i=0;i<infos.length;i++){
			   htmlTmp+='<option value="'+infos[i].cruiseship_uuid+'">'+infos[i].cruiseship_name+'</option>';
			   }
		   }
	   }
	});
	htmlTmp+='</select>';
	htmlTmp+='</span>';
	htmlTmp+='<span>';
	htmlTmp+='<label for=""><i class="xing">*</i>船期</label>';
	htmlTmp+='<select name="selectedCruiseshipDate" id="selectedCruiseshipDate" onchange="changeCruiseshipDate(this)">';
	htmlTmp+='</select>';
	htmlTmp+='</span>';
	htmlTmp+='</div>';
	htmlTmp+='<div class="mod_information_d7"></div>';
	htmlTmp+='<table class="table table-striped table-bordered " id="stockInfo">';
	htmlTmp+='<thead>';
	htmlTmp+='<tr>';
	htmlTmp+='<th width="20%" class="tc">序号</th>';
	htmlTmp+='<th width="30%" class="tc">舱型</th>';
	htmlTmp+='<th width="20%" class="tc">库存总数</th>';
	htmlTmp+='<th width="30%" class="tc">余位</th>';
	htmlTmp+='</tr>';
	htmlTmp+='</thead>';
	htmlTmp+='<tbody>';
	
	htmlTmp+='</tbody>';
	htmlTmp+='</table>';
	htmlTmp+='</div>';
	htmlTmp+='</div>';
	
  /*  $.jBox($("#create-group-control-pop").html(), {
        title: "生成团控表", buttons: {'关闭': 0, '保存': 1}, submit: function (v, h, f) {
            if (v == "1") {
            	//debugger;
            	var cruiseGroupControlId = $("#create-group-control-pop").find("td:first").attr("cruiseGroupControlId");
            	$(thisObj).parents("tr:first").find('input[name=cruiseGroupControlId]').val(cruiseGroupControlId);
            	
            	
                return true;
            }
        }, height: 300, width: 500,
        persistent: true
    });*/
	$.jBox(htmlTmp, {
        title: "生成团控表", buttons: {'关闭': 0, '保存': 1}, submit: function (v, h, f) {
            if (v == "1") {
            	//debugger;
            	//加上保存校验:游轮名称和船期为必填项
            	var itemShipName=$("#selectedCruiseshipName").find("option:selected").val();
            	var itemShipDate=$("#selectedCruiseshipDate").find("option:selected").val();
            	if(itemShipName=='-1'||itemShipDate==null){
            		  top.$.jBox.info("游轮名称和船期为必填项<br/>请选择后再保存!", "错误提示");
            		  return false;
            	}else{
            		
            		//debugger;
            		var cruiseGroupControlId = $("#create-group-control-pop").find("td:first").attr("cruiseGroupControlId");
            		
            		//修改时  对游轮  有新增舱型（游轮产品的团号重复）的情况进行校验
            		//同一产品的团号  如有重复的   只能其中 一个进行  关联
            		var groupCode = $(thisObj).parents("tr:first").find('input[name=groupCode]').val();
            		var cruiseGroupControlIdOld = $(thisObj).parents("tr:first").find('input[name=cruiseGroupControlId]').val();
            		var arrayContaintGroupCode = groupCodeArray.indexOf(groupCode);
            		if(-1==arrayContaintGroupCode){//没被付过值
            			$(thisObj).parents("tr:first").find('input[name=cruiseGroupControlId]').val(cruiseGroupControlId);
            		}else{//已经被付过值  如果cruiseGroupControlIdOld 有值 ：就是被付过值的那个，还可以修改
            			if(""!=cruiseGroupControlIdOld){
            				$(thisObj).parents("tr:first").find('input[name=cruiseGroupControlId]').val(cruiseGroupControlId);
            			}else{
            				top.$.jBox.tip("该产品相同团号已关联团控表！",'warning');
            			}
            		}
            		
            		
            		return true;
            	}
            }
        }, height: 300, width: 500,
        persistent: true
    });
	$("#selectedCruiseshipName").attr("title",$("#selectedCruiseshipName option:selected").text());
	$("#selectedCruiseshipDate").attr("title",$("#selectedCruiseshipDate option:selected").text());
    inquiryCheckBOX();
}

//查看关联团控表    对应需求  223
function jbox_view_group_control_pop(thisObj) {
	//debugger;
	var  cruiseGroupControlIdVal = $(thisObj).parents("tr:first").find('input[name=cruiseGroupControlId]').val();
	//var cruiseGroupControlIdVal=$("#stockInfo").find("tbody").find("td:first").attr("cruisegroupcontrolid");
	//获得该activitygroup表中的id---如果不是查看已有团期,则这里的取到的值不会为团期id,比如会是时间值
	var agId=$(thisObj).parent().parent().parent().parent().parent().find("td:first").find("input:first").val();
	var globalPath=$("#ctx").val();
	var htmlAppend="";
	//alert(cruiseGroupControlIdVal);
	//alert(globalPath);
	$.ajax({
		url:globalPath+"/activity/manager/getDetailsById?keyId="+cruiseGroupControlIdVal+"&agId="+agId,
		type:"post",
		async: false,//不设置会出问题
		success:function(relInfo){
		 var detail=relInfo.details;
		 htmlAppend+='<div id="view-group-control-pop">';
		 htmlAppend+='<div class="create-group-control-pop">';
		 htmlAppend+='<table class="table table-striped table-bordered ">';
		 htmlAppend+='<thead>';
		 htmlAppend+='<tr>';
		 htmlAppend+='<th width="20%" class="tc">船期</th>';
		 htmlAppend+='<th width="60%" class="tc">游轮名称</th>';
		 htmlAppend+='<th width="20%" class="tc">关联日期</th>';
		 htmlAppend+='</tr>';
		 htmlAppend+='</thead>';
		 htmlAppend+='<tbody>';
		
		 if("none"!=detail){ //查询到有关联团控信息,才进行tbody内信息拼接
			 for(var i=0;i<detail.length;i++){
				 htmlAppend+='<tr>'; 
				 htmlAppend+='<th width="20%" class="tc">'+detail[i].ship_date+'</th>';
				 htmlAppend+='<th width="60%" class="tc">'+detail[i].ship_name+'</th>';
				 htmlAppend+='<th width="20%" class="tc">'+detail[i].create_date+'</th>';
				 htmlAppend+='</tr>';
			 }
		 }
		 htmlAppend+='</tbody>';
		 htmlAppend+='</table>';
		 htmlAppend+='</div>'; 
		 htmlAppend+='</div>';
		}
	});
   /* $.jBox($("#view-group-control-pop").html(), {
        title: "查看关联团控表", buttons: {'关闭': 0}, submit: function (v, h, f) {
            if (v == "1") {
            	
            	
            	
                return true;
            }
        }, height: 400, width: 600,
        persistent: true
    });*/
	 $.jBox(htmlAppend, {
	        title: "查看关联团控表", buttons: {'关闭': 0}, submit: function (v, h, f) {
	            if (v == "1") {
	            	
	            	
	            	
	                return true;
	            }
	        }, height: 400, width: 600,
	        persistent: true
	    });
    inquiryCheckBOX();
}
//编辑关联团控表
function jbox_edit_group_control_pop(thisObj) {
	
	
	//debugger;
	//修改时  对游轮  有新增舱型（游轮产品的团号重复）的情况进行校验
	//同一产品的团号  如有重复的   只能其中 一个进行  关联
	var groupCodeArray = new Array();
	$("#modTable").find("input[name=idss]").each(function(){
		var cruiseGroupControlIdTemp = $(this).parents("tr:first").find('input[name=cruiseGroupControlId]').val();
		var groupCodeTemp = $(this).parents("tr:first").find('input[name=idss]').val();
		if(""!=groupCodeTemp){
			//alert(groupCodeTemp);
			if(""!=cruiseGroupControlIdTemp){
				groupCodeArray.push(groupCodeTemp);
			}
		}
	});
	
	
	//223需求:拼接编辑关联团控列表-s//
	var htmlTmp="";
	    
	var pathTmp=$("#ctx").val();
	//当前团期的cruiseship_stock_detail表的主键id.
	var cruiseGroupControlIdVal = $(thisObj).parents('td:first').next().find('input[name=cruiseGroupControlId]').val();
	//获得该activitygroup表中的id
	var agId=$(thisObj).parent().parent().parent().parent().parent().find("td:first").find("input:first").val();
	    //查询当前库存游轮的信息,以及所有库存游轮的名称,uuid--s//
	      $.ajax({
	    	url:pathTmp+"/activity/manager/infosOfCruiseshipStockDetail?agId="+agId+"&csdIdTemp="+cruiseGroupControlIdVal,
	    	type:"post",
	    	async:false,
	    	success:function(res){
	    		var shipNames=res.shipNamesUuids;//库存游轮名称和游轮uuid的集合
	    		var shipdates=res.shipdatesList; //当前游轮的所有船期集合
	    		var detailInfos=res.detailsInfo; //当前游轮当前船期的船期库存信息
	    		var rel_date=res. rel_date;     //关联日期
	    		var rel_op=res.operator;        //关联的操作的操作人
	    		var selShipdate=res.selShipDate;//选中的船期
	    		htmlTmp+='<div id="create-group-control-pop" class="">';
	    	    htmlTmp+='<div class="create-group-control-pop">';
	    	    htmlTmp+='<div class="part-f">';
	    	    htmlTmp+='<span>';
	    	    htmlTmp+='<label for=""><i class="xing">*</i>游轮名称</label>';
	    	    htmlTmp+='<select name="selectedCruiseshipName2" id="selectedCruiseshipName2" onchange="changeCruiseshipName2(this);">';
	    	    htmlTmp+='<option value="-1">请选择</option>';
	    		if("none"!=shipNames){  //游轮名称下拉列表不为空的时候,拼接游轮名称下拉项
	    			for(var i=0;i<shipNames.length;i++){
	    				 //将当前游轮名称项设置为选中项
	    				if(shipNames[i].cruiseship_uuid==detailInfos[0].cruiseship_uuid){
	    					htmlTmp+='<option value="'+shipNames[i].cruiseship_uuid+'" selected="selected">'+shipNames[i].cruiseship_name+'</option>';
	    				}else{
	    					htmlTmp+='<option value="'+shipNames[i].cruiseship_uuid+'">'+shipNames[i].cruiseship_name+'</option>';
	    				}
	    				 
	    			}
	    		}
	    		htmlTmp+='</select>';
	    		htmlTmp+='</span>';
	    		htmlTmp+='<span>';
	    		htmlTmp+='<label for=""><i class="xing">*</i>船期</label>';
	    		htmlTmp+='<select name="selectedCruiseshipDate2" id="selectedCruiseshipDate2" onchange="changeCruiseshipDate2(this)">';
	    		if("none"!=shipdates){ //当前游轮的船期不为空的时候,才进行船期下拉项的拼接
	    			for(var j=0;j<shipdates.length;j++){
	    				//将当前船期项设置为选中项
	    				if(shipdates[j].ship_date==selShipdate){
	    					htmlTmp+=' <option value="'+shipdates[j].ship_date+'" selected="selected">'+shipdates[j].ship_date+'</option>';
	    				}else{
	    					htmlTmp+=' <option value="'+shipdates[j].ship_date+'">'+shipdates[j].ship_date+'</option>';
	    				}
	    			}
	    		}
	    		htmlTmp+='</select>';
	    		htmlTmp+='</span>';
	    		htmlTmp+='</div>';
	    		htmlTmp+='<div class="mod_information_d7"></div>';
	    		htmlTmp+='<div class="part-f">';
	    		htmlTmp+='<span>';
	    		htmlTmp+='<label for="" style="margin-left:30px;">关联时间:&nbsp;&nbsp;</label>';
	    		/*htmlTmp+='<div class="text-show ellipsis-text-detail" id="relDate">'+rel_date+'</div>';*/
	    		htmlTmp+='<label id="relDate">'+rel_date+'</label>';
	    		htmlTmp+='</span>';
	    		htmlTmp+='<span>';
	    		htmlTmp+='<label for="" style="margin-left:30px;">操作人:&nbsp;&nbsp;</label>';
	    		/*htmlTmp+='<div class="text-show ellipsis-text-detail" id="relOp">'+rel_op+'</div>';*/
	    		htmlTmp+='<label id="relOp">'+rel_op+'</label>';
	    		htmlTmp+='</span>';
	    		htmlTmp+='</div>';
	    		htmlTmp+='<table class="table table-striped table-bordered " id="stockInfo2">';
	    		htmlTmp+='<thead>';
	    		htmlTmp+='<tr>';
	    		htmlTmp+='<th width="20%" class="tc">序号</th>';
	    		htmlTmp+='<th width="30%" class="tc">舱型</th>';
	    		htmlTmp+='<th width="20%" class="tc">库存总数</th>';
	    		htmlTmp+='<th width="30%" class="tc">余位</th>';
	    		htmlTmp+='</tr>';
	    		htmlTmp+='</thead>';
	    		htmlTmp+='<tbody>';
	    		if("none"!=detailInfos){ //当前游轮的船期库存信息不为空时才进行库存信息的拼接
	    			 for(var k=0;k<detailInfos.length;k++){
	    				   htmlTmp+='<tr>';
	    				   htmlTmp+='<td class="tc" cruiseGroupControlId="'+detailInfos[k].detailId+'">'+(k+1)+'</td>';
	    				   htmlTmp+='<td  class="tc">'+detailInfos[k].cabin_name+'</td>'
	    				   htmlTmp+='<td  class="tc">'+detailInfos[k].stock_amount+'</td>';
	    				   htmlTmp+='<td  class="tc">'+detailInfos[k].free_positon+'</td>';
	    				   htmlTmp+='</tr>';
	    			 }
	    		}
	    		htmlTmp+='</tbody>';
	    		htmlTmp+='</table>';
	    		htmlTmp+='</div>';
	    		htmlTmp+='</div>';
	    	}
	      });
	    //查询库存游轮的名称和船期--e//
	//223需求:拼接编辑关联团控列表-e//
    $.jBox(htmlTmp, {
        title: "编辑关联团控表", buttons: {'关闭': 0,'保存':1}, submit: function (v, h, f) {
        	if (v == "1") {
            	debugger;
            	//加上保存校验:游轮名称和船期为必填项
            	var itemShipName=$("#selectedCruiseshipName2").find("option:selected").val();
            	var itemShipDate=$("#selectedCruiseshipDate2").find("option:selected").val();
            	if(itemShipName=='-1'||itemShipDate==null){
            		  top.$.jBox.info("游轮名称和船期为必填项<br/>请选择后再保存!", "错误提示");
            		 return false;
            	}else{
            		//debugger;
            		var cruiseGroupControlEditId = $("#create-group-control-pop").find("td:first").attr("cruiseGroupControlId");
            		//$(thisObj).parents("tr:first").find('input[name=cruiseGroupControlId]').val(cruiseGroupControlEditId);
            		
            		//修改时  对游轮  有新增舱型（游轮产品的团号重复）的情况进行校验
            		//同一产品的团号  如有重复的   只能其中 一个进行  关联
            		var groupCode = $(thisObj).parents("tr:first").find('input[name=idss]').val();
            		var cruiseGroupControlIdOld = $(thisObj).parents("tr:first").find('input[name=cruiseGroupControlId]').val();
            		var arrayContaintGroupCode = groupCodeArray.indexOf(groupCode);
            		if(-1==arrayContaintGroupCode){//没被付过值
            			//$(thisObj).parents("tr:first").find('input[name=cruiseGroupControlId]').val(cruiseGroupControlId);
            			$(thisObj).parents('td:first').next().find('input[name=cruiseGroupControlId]').val(cruiseGroupControlEditId);
            		}else{//已经被付过值  如果cruiseGroupControlIdOld 有值 ：就是被付过值的那个，还可以修改
            			if(""!=cruiseGroupControlIdOld){
            				//$(thisObj).parents("tr:first").find('input[name=cruiseGroupControlId]').val(cruiseGroupControlId);
            				$(thisObj).parents('td:first').next().find('input[name=cruiseGroupControlId]').val(cruiseGroupControlEditId);
            			}else{
            				top.$.jBox.tip("该产品相同团号已关联团控表！",'warning');
            			}
            		}
            		
            		return true;
            	}
            }
        }, height: 400, width: 600,
        persistent: true
    });
  //下拉项名字过长时的提示信息
	$("#selectedCruiseshipName2").attr("title",$("#selectedCruiseshipName2 option:selected").text());
	$("#selectedCruiseshipDate2").attr("title",$("#selectedCruiseshipDate2 option:selected").text());
    inquiryCheckBOX();
}
//查看关联团控表-单独条

function jbox_view_single_group_control_pop() {
    $.jBox($("#view-single-group-control-pop").html(), {
        title: "查看", buttons: {'关闭': 0, '保存': 1}, submit: function (v, h, f) {
            if (v == "1") {
                return true;
            }
        }, height: 300, width: 500,
        persistent: true
    });
    inquiryCheckBOX();
}



function getCabinStocks(){
	var cabinStocks = [];
	$('#contentTable tbody tr').each(function(){
		var $this = $(this);
		var cabinStock = {
				cabinStockUuid:$this.prop('id'),
				cabinStockName:$this.find('[name="cabinName"]').text(),
				cabinStockNum:$this.find('[name="stockNum"]').text(),
				cabinSurplusNum:$this.find('[name="surplusNum"]').text()
		}
		cabinStocks.push(cabinStock);	
	});
	return cabinStock;
}

function changeCruiseshipName(obj){
	 var cruiseshipUuid=$(obj).val();
	 var pathTmp=$("#ctx").val();
	 $.ajax({
  	   url:pathTmp+"/activity/manager/getShipDateByCruiseshipUuid?uuid="+cruiseshipUuid,
  	   type:"post",
  	   async:false,
  	   success:function(result){
  		 var cruiseshipUUid=result.cruiseshipUUid;
  		 var shipdateList=result.shipdateList;
  		 var shipStockDetailInfos=result.shipStockDetailInfo;
  		// debugger;
  		//---拼出某游轮的船期下拉列表--223-s//
           var html="";
  		 //清空下拉列表的缓存
  		 $("#selectedCruiseshipDate").empty();
  		 if("none"!=shipdateList){ //游轮名称不为请选择时,才要进行船期下拉项的拼接
  		 for(var i=0;i<shipdateList.length;i++){
  			// alert(shipdateList[i].ship_date);
  			 html+=' <option value="'+shipdateList[i].ship_date+'">'+shipdateList[i].ship_date+'</option>';
  		 }
  			 $("#selectedCruiseshipDate").append(html); 
  		 } 
  			 //debugger;
  			 //解决弹窗刷新造成的游轮名称下拉列表项被重置默认的问题
  			   var sel=$("#selectedCruiseshipName").find("option");
  			   sel.each(function(i,n){
  				  //alert(this.value) 
  				  if(n.value==cruiseshipUUid){
  					 $(n).attr("selected","selected");
  				  } 
  				 // alert(n.value+"--"+(n.value==cruiseshipUUid));
  			   });   
  			   //拼接第一次带出的船期库存的信息.--s
     			   $("#stockInfo").find("tbody").empty();//清除多次选择游轮名称缓存影响
     			   var htmlStock="";
     			   if("none"!=shipStockDetailInfos){ //游轮名称不为默认请选择时,才进行船期具体信息的拼接
     			   for(var j=0;j<shipStockDetailInfos.length;j++){
     				   htmlStock+='<tr>';
     				   htmlStock+='<td class="tc" cruiseGroupControlId="'+shipStockDetailInfos[j].detailId+'">'+(j+1)+'</td>';
     				   htmlStock+='<td  class="tc">'+shipStockDetailInfos[j].cabin_name+'</td>'
     				   htmlStock+='<td  class="tc">'+shipStockDetailInfos[j].stock_amount+'</td>';
  				       htmlStock+='<td  class="tc">'+shipStockDetailInfos[j].free_positon+'</td>';
     				   htmlStock+='</tr>';
     			   }
     			   $("#stockInfo").find("tbody").append(htmlStock);
     			   //拼接第一次带出的船期库存的信息.--e
     			   }
  												
  		  //---拼出某游轮的船期下拉列表--223-e//
  	   }
  	   });
	    //下拉项名字过长时的提示信息
	    $("#selectedCruiseshipName").attr("title",$("#selectedCruiseshipName option:selected").text());
		$("#selectedCruiseshipDate").attr("title",$("#selectedCruiseshipDate option:selected").text());
}
function changeCruiseshipDate(obj){
	 var selCruiseshipDateElt=$(obj).val();
	 var selCruiseshipNameElt=$("#selectedCruiseshipName option:selected").val();
	 var pathTmp=$("#ctx").val();
   	  //发送ajax查询选中游轮选中船期的船期信息
   	  $.ajax({
   		url:pathTmp+"/activity/manager/getInfoByUuidDate?uuid="+selCruiseshipNameElt+"&shipdate="+selCruiseshipDateElt,
   		type:"post",
   		async:false,
   		success:function(res){
   			var infos=res.detailInfo;
   			//拼接查询到船期的具体信息
   			$("#stockInfo").find("tbody").empty();
   			var htmlAdd="";
   			for(var j=0;j<infos.length;j++){
   				   htmlAdd+='<tr>';
   				   htmlAdd+='<td class="tc" cruiseGroupControlId="'+infos[j].detailId+'">'+(j+1)+'</td>';
   				   htmlAdd+='<td  class="tc">'+infos[j].cabin_name+'</td>'
   				   htmlAdd+='<td  class="tc">'+infos[j].stock_amount+'</td>';
   				   htmlAdd+='<td  class="tc">'+infos[j].free_positon+'</td>';
   				   htmlAdd+='</tr>';
   			   }
   			   $("#stockInfo").find("tbody").append(htmlAdd);
   			   //解决弹窗刷新造成的船期名称下拉列表项被重置默认的问题
   			   var sel=$("#selectedCruiseshipDate").find("option");
   			   sel.each(function(i,n){
   				  //alert(this.value) 
   				  if(n.value==selCruiseshipDateElt){
   					 $(n).attr("selected","selected");
   				  } 
   				 // alert(n.value+"--"+(n.value==cruiseshipUUid));
   			   });   
   		}
   	  });
   	////下拉项名字过长时的提示信息  
   	$("#selectedCruiseshipName").attr("title",$("#selectedCruiseshipName option:selected").text());
	$("#selectedCruiseshipDate").attr("title",$("#selectedCruiseshipDate option:selected").text());
     }
function changeCruiseshipName2(obj){//与方法changeCruiseshipName几乎相同
	 var cruiseshipUuid=$(obj).val();
	 var pathTmp=$("#ctx").val();
	// alert(cruiseshipUuid);
	 $.ajax({
	  	   url:pathTmp+"/activity/manager/getShipDateByCruiseshipUuid?uuid="+cruiseshipUuid,
	  	   type:"post",
	  	   async:false,
	  	   success:function(result){
	  		 var cruiseshipUUid=result.cruiseshipUUid;
	  		 var shipdateList=result.shipdateList;
	  		 var shipStockDetailInfos=result.shipStockDetailInfo;
	  		// debugger;
	  		//---拼出某游轮的船期下拉列表--223-s//
	           var html="";
	  		 //清空下拉列表的缓存
	  		 $("#selectedCruiseshipDate2").empty();
	  		//变更游轮名称后,关联时间和操作人的信息清空
		  		$("#relDate").empty();
		  		$("#relOp").empty();
	  		 if("none"!=shipdateList){ //游轮名称不为请选择时,才要进行船期下拉项的拼接
	  		 for(var i=0;i<shipdateList.length;i++){
	  			// alert(shipdateList[i].ship_date);
	  			 html+=' <option value="'+shipdateList[i].ship_date+'">'+shipdateList[i].ship_date+'</option>';
	  		 }
	  			 $("#selectedCruiseshipDate2").append(html); 
	  		 } 
	  			 //debugger;
	  			 //解决弹窗刷新造成的游轮名称下拉列表项被重置默认的问题
	  			   var sel=$("#selectedCruiseshipName2").find("option");
	  			   sel.each(function(i,n){
	  				  //alert(this.value) 
	  				  if(n.value==cruiseshipUUid){
	  					 $(n).attr("selected","selected");
	  				  } 
	  				 // alert(n.value+"--"+(n.value==cruiseshipUUid));
	  			   });   
	  			   //拼接第一次带出的船期库存的信息.--s
	     			   $("#stockInfo2").find("tbody").empty();//清除多次选择游轮名称缓存影响
	     			   var htmlStock="";
	     			   if("none"!=shipStockDetailInfos){ //游轮名称不为默认请选择时,才进行船期具体信息的拼接
	     			   for(var j=0;j<shipStockDetailInfos.length;j++){
	     				   htmlStock+='<tr>';
	     				   htmlStock+='<td class="tc" cruiseGroupControlId="'+shipStockDetailInfos[j].detailId+'">'+(j+1)+'</td>';
	     				   htmlStock+='<td  class="tc">'+shipStockDetailInfos[j].cabin_name+'</td>'
	     				   htmlStock+='<td  class="tc">'+shipStockDetailInfos[j].stock_amount+'</td>';
	  				       htmlStock+='<td  class="tc">'+shipStockDetailInfos[j].free_positon+'</td>';
	     				   htmlStock+='</tr>';
	     			   }
	     			   $("#stockInfo2").find("tbody").append(htmlStock);
	     			   //拼接第一次带出的船期库存的信息.--e
	     			   }
	  												
	  		  //---拼出某游轮的船期下拉列表--223-e//
	  	   }
	  	   });
	 	//下拉项名字过长时的提示信息
		$("#selectedCruiseshipName2").attr("title",$("#selectedCruiseshipName2 option:selected").text());
		$("#selectedCruiseshipDate2").attr("title",$("#selectedCruiseshipDate2 option:selected").text());
}
function changeCruiseshipDate2(obj){//与方法changeCruiseshipDate几乎完全相同
	 var selCruiseshipDateElt=$(obj).val();
	 var selCruiseshipNameElt=$("#selectedCruiseshipName2 option:selected").val();
	 var pathTmp=$("#ctx").val();
   	  //发送ajax查询选中游轮选中船期的船期信息
   	  $.ajax({
   		url:pathTmp+"/activity/manager/getInfoByUuidDate?uuid="+selCruiseshipNameElt+"&shipdate="+selCruiseshipDateElt,
   		type:"post",
   		async:false,
   		success:function(res){
   			var infos=res.detailInfo;
   			//拼接查询到船期的具体信息
   			$("#stockInfo2").find("tbody").empty();
   			var htmlAdd="";
   			for(var j=0;j<infos.length;j++){
   				   htmlAdd+='<tr>';
   				   htmlAdd+='<td class="tc" cruiseGroupControlId="'+infos[j].detailId+'">'+(j+1)+'</td>';
   				   htmlAdd+='<td  class="tc">'+infos[j].cabin_name+'</td>'
   				   htmlAdd+='<td  class="tc">'+infos[j].stock_amount+'</td>';
   				   htmlAdd+='<td  class="tc">'+infos[j].free_positon+'</td>';
   				   htmlAdd+='</tr>';
   			   }
   			   $("#stockInfo2").find("tbody").append(htmlAdd);
   			   //解决弹窗刷新造成的船期名称下拉列表项被重置默认的问题
   			   var sel=$("#selectedCruiseshipDate2").find("option");
   			   sel.each(function(i,n){
   				  //alert(this.value) 
   				  if(n.value==selCruiseshipDateElt){
   					 $(n).attr("selected","selected");
   				  } 
   				 // alert(n.value+"--"+(n.value==cruiseshipUUid));
   			   });   
   		}
   	  });
   	//下拉项名字过长时的提示信息
  	$("#selectedCruiseshipName2").attr("title",$("#selectedCruiseshipName2 option:selected").text());
  	$("#selectedCruiseshipDate2").attr("title",$("#selectedCruiseshipDate2 option:selected").text());
}