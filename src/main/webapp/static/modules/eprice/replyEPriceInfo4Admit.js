$(function(){
	var j = $.trim($("#travel-requirements-span-id").html());
	
	replayEPrice.travelRequirementsJson = $.parseJSON(j);
	
	replayEPrice.service.travelRequirementShow();
	replayEPrice.service.zeroNum();
	// 返回计调列表
	$("[name=history-back]").click(function(){
		if(history.length>0){
			history.back();
		}else{
			location.href=contextPath+"/eprice/manager/project/erecordlist";
		}
	});
	  /**
      * 行程文档文件域change上传事件绑定
      */
     $("#saler-trip-file-id").change(function(){
     	try{
     		replayEPrice.service.fileUpload($(this).attr("id"));
     	}catch(e){
     		//alert(e);
     	}
     });
     /**
      * 行程文档上传按钮
      */
     $("#saler-trip-btn-id").click(function(){
     	$("#saler-trip-file-id").click();
     });
     
     $("#other-price-btn-id").click(function(){
     	replayEPrice.service.otherPrice();
     });
     /**
      * 新增汇率不为0验证，并捎带总值计算 20150511
      */
     $("input.exchangerate").live("blur",function(){
    	  var num = $(this).val();
    	  var reg = new RegExp(/^\d+\.?\d{0,3}$/);
    	  if(!reg.test(num)){
    		  if(parseFloat(num)>0){
        		  $.jBox.tip("汇率值最多保留三位小数","提示");
    			  $(this).val(Math.round(parseFloat(num)*1000)/1000);
    		  }else{
        		  $.jBox.tip("输入汇率值有误","提示");
        		  $(this).val("1.000");
    		  }
    	  }
    	  // 调用总值计算
       	  var type = $("input[name=priceType]:checked").val();
       	  if(type==1){
           	  allEprice();
       	  }else if(type==2){
           	  partEprice();
       	  }
     });
     
     /**
      * 新增表单提交 20150511
      */
     $("#reply-eprice-btn-id").click(function(){
    	 var adultNum = $("#reply-eprice-form-id input[name=adultSum]").val();
		 var childNum = $("#reply-eprice-form-id input[name=childSum]").val();
		 var specialNum = $("#reply-eprice-form-id input[name=specialSum]").val();
    	 var type= $("input[name=priceType]:checked").val();
    	 if(type==1){// 整体报价校验
    		 var boolAll = true;
    		 if(parseInt(adultNum)>0){
    			 $("ul.adult-dt-price li:visible select[name=adultCurrency]  option:selected").each(function(){
    				 var currcy = parseFloat($(this).val());
    				 if(currcy<=0 || isNaN(currcy)){
    					 boolAll = false;
    				 }
    			 });
    			 $("ul.adult-dt-price li:visible input[name=adultAmount]").each(function(){
    				 var amount = parseFloat($(this).val());
    				 if(amount<=0 || isNaN(amount)){
    					 boolAll = false;
    				 }
    			 });
			 }
			 if(parseInt(childNum)>0){
    			 $("ul.child-dt-price li:visible select[name=childCurrency]  option:selected").each(function(){
    				 var currcy = parseFloat($(this).val());
    				 if(currcy<=0 || isNaN(currcy)){
    					 boolAll = false;
    				 }
    			 });
    			 $("ul.child-dt-price li:visible input[name=childAmount]").each(function(){
    				 var amount = parseFloat($(this).val());
    				 if(amount<=0 || isNaN(amount)){
    					 boolAll = false;
    				 }
    			 });
			 }
			 if(parseInt(specialNum)>0){
    			 $("ul.special-dt-price li:visible select[name=specialCurrency]  option:selected").each(function(){
    				 var currcy = parseFloat($(this).val());
    				 if(currcy<=0 || isNaN(currcy)){
    					 boolAll = false;
    				 }
    			 });
    			 $("ul.special-dt-price li:visible input[name=specialAmount]").each(function(){
    				 var amount = parseFloat($(this).val());
    				 if(amount<=0 || isNaN(amount)){
    					 boolAll = false;
    				 }
    			 });
			 }
			 // 报价上传文件
			 if($("#companyUuid").val() == '049984365af44db592d1cd529f3008c3' && $("#upfileShow").children().size() <= 0) {
				 $.jBox.tip("请上传文件");
				 return false;
			 }
        	 
        	 if(boolAll){
        		 $.jBox.confirm("提交报价，您确定吗？","提示",function(v, h, f){
        			 if(v=="ok"){
        				 //treplayEPrice.ajax.replyPriceAjax();
                		 $("#reply-eprice-form-id").submit();
        			 }else{
        				 $.jBox.tip("提交报价取消");
        			 }
        		 });
        	 }else{
        		 $.jBox.tip("请检查您输入的数据是否有空值","警告");
        	 }
    	 }else if(type==2){// 细分报价校验
    		 var boolAll = true;
    		 if(parseInt(adultNum)>0){
    			 $("ul.adult-part-price li:visible select[name=adultPartCurrency]  option:selected").each(function(){
    				 var currcy = parseFloat($(this).val());
    				 if(currcy<=0 || isNaN(currcy)){
    					 boolAll = false;
    				 }
    			 });
    			 $("ul.adult-part-price li:visible input[name=adultPartAmount]").each(function(){
    				 var amount = parseFloat($(this).val());
    				 if(amount<=0 || isNaN(amount)){
    					 boolAll = false;
    				 }
    			 });
			 }
			 if(parseInt(childNum)>0){
    			 $("ul.child-part-price li:visible select[name=childPartCurrency]  option:selected").each(function(){
    				 var currcy = parseFloat($(this).val());
    				 if(currcy<=0 || isNaN(currcy)){
    					 boolAll = false;
    				 }
    			 });
    			 $("ul.child-part-price li:visible input[name=childPartAmount]").each(function(){
    				 var amount = parseFloat($(this).val());
    				 if(amount<=0 || isNaN(amount)){
    					 boolAll = false;
    				 }
    			 });
			 }
			 if(parseInt(specialNum)>0){
    			 $("ul.special-part-price li:visible select[name=specialPartCurrency]  option:selected").each(function(){
    				 var currcy = parseFloat($(this).val());
    				 if(currcy<=0 || isNaN(currcy)){
    					 boolAll = false;
    				 }
    			 });
    			 $("ul.special-part-price li:visible input[name=specialPartAmount]").each(function(){
    				 var amount = parseFloat($(this).val());
    				 if(amount<=0 || isNaN(amount)){
    					 boolAll = false;
    				 }
    			 });
			 }
        	 // 将分组人数写入表单
        	 $("#price-detaill-dl-part-id span.adultPersonNum").each(function(){
        		 var num = parseInt($(this).html()); // 获取每组人数
        		 if(num>0){
        			 $(this).parent().find("input[name=adultPartNum]").val(num); // 将分组人数写入表单隐藏域
        		 }
        	 });
        	 $("#price-detaill-dl-part-id span.childPersonNum").each(function(){
        		 var num = parseInt($(this).html()); // 获取每组人数
        		 if(num>0){
        			 $(this).parent().find("input[name=childPartNum]").val(num); // 将分组人数写入表单隐藏域
        		 }
        	 });
        	 $("#price-detaill-dl-part-id span.specialPersonNum").each(function(){
        		 var num = parseInt($(this).html()); // 获取每组人数
        		 if(num>0){
        			 $(this).parent().find("input[name=specialPartNum]").val(num); // 将分组人数写入表单隐藏域
        		 }
        	 });
        	 
        	 // 报价上传文件
			 if($("#companyUuid").val() == '049984365af44db592d1cd529f3008c3' && $("#upfileShow").children().size() <= 0) {
				 $.jBox.tip("请上传文件");
				 return false;
			 }
        	 if(boolAll){
        		 $.jBox.confirm("确定提交报价吗？","提示",function(v,h,f){
        			 if(v=="ok"){
        				 //treplayEPrice.ajax.replyPriceAjax();
                		 $("#reply-eprice-form-id").submit();
        			 }else{
        				 $.jBox.tip("提交报价取消");
        			 }
            	 });
        	 }else{
        		 $.jBox.tip("请检查您输入的数据是否有空值","警告");
        	 }
    	 }
     });
     
     /* 原提交按钮 20150511废弃
     $("#reply-eprice-btn-id").click(function(){
		 var adult = $("#adult-price-input-id").val();
		 var adultnum = $("#adult-price-input-id-num").val();
		 var child = $("#child-price-input-id").val();
		 var childnum = $("#child-price-input-id-num").val();
		 var specialPersonPrice = $("#special-person-price-input-id").val();
		 
		 var specialnum = $("#special-person-price-input-id-num").val();
		 
    	 if((adultnum==0?1:adult)<=0 || (childnum==0?1:child)<=0 || (specialnum==0?1:specialPersonPrice)<=0){
    		 $.jBox.confirm("输入的价格出现零或者负值，您确定吗？","提示",function(v, h, f){
    			 if(v=="ok"){
    				 replayEPrice.ajax.replySubmit();
    			 }else{
    				 $.jBox.tip("报价取消");
    			 }
    		 });
    	 }else{
    		 $.jBox.confirm("报价提交，您确定吗？","提示",function(v, h, f){
    			 if(v=="ok"){
    				 replayEPrice.ajax.replySubmit();
    			 }else{
    				 $.jBox.tip("报价取消");
    			 }
    		 });
    	 }
     });*/
	
     // 切换整体报价和细分报价 20150511新增
     $("#reply-eprice-form-id input[type=radio]").live("click",function(){
    	 var val = $(this).val();
    	 $("#price-detaill-dl-all-id").css("display","none");
    	 $("#price-detaill-dl-part-id").css("display","none");
    	 if(val==1){ // 显示整体报价
    		 $("#price-detaill-dl-all-id").css("display","");
    	 }else{ // 显示细分报价
    		 $("#price-detaill-dl-part-id").css("display","");
    	 }
     });
     /**
      * 20150511新增 begin
      *  增加整体报价 
      *  */
     // 成人整体报价
     $("#reply-eprice-form-id ul.adult-dt-price input[name=addNewPrice]").live("click",function(){
    	 var number = $("input[name=adultSum]").val();
    	 if(parseInt(number)>0){
        	 var copy= $("li.adult-dt-price:first").clone();
    		 $("ul.adult-dt-price").append(copy);
    		 $("ul.adult-dt-price li.adult-dt-price:last").css("display","");
    		 $("ul.adult-dt-price label.adultTitle:last").html("");
    		 $("ul.adult-dt-price label.adultTitle:last").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
    		 $("ul.adult-dt-price input[name=addNewPrice]:last").remove();
    		 $("ul.adult-dt-price input[name=adultAmount]:last").after('&nbsp;&nbsp;<input type="button" name="delNewPrice" class="btn btn-primary gray mar_xjia_del" value="删除"/>');
    	 }else{
    		 jBox.tip("当前人数为0","info");
    	 }
     });
     // 儿童整体报价
     $("#reply-eprice-form-id ul.child-dt-price input[name=addNewPrice]").live("click",function(){ 
    	 var number = $("input[name=childSum]").val();
    	 if(parseInt(number)>0){
        	 var copy= $("li.child-dt-price:first").clone();
    		 $("ul.child-dt-price").append(copy);
    		 $("ul.child-dt-price li.child-dt-price:last").css("display","");
    		 $("ul.child-dt-price label.childTitle:last").html("");
    		 $("ul.child-dt-price label.childTitle:last").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
    		 
    		 $("ul.child-dt-price input[name=addNewPrice]:last").remove();
    		 $("ul.child-dt-price input[name=childAmount]:last").after('&nbsp;&nbsp;<input type="button" name="delNewPrice" class="btn btn-primary gray mar_xjia_del" value="删除"/>');
    	 }else{
    		 jBox.tip("当前人数为0","info");
    	 }
     });
     // 特殊人群整体报价
     $("#reply-eprice-form-id ul.special-dt-price input[name=addNewPrice]").live("click",function(){
    	 var number = $("input[name=specialSum]").val();
    	 if(parseInt(number)>0){
        	 var copy= $("li.special-dt-price:first").clone();
    		 $("ul.special-dt-price").append(copy);
    		 $("ul.special-dt-price li.special-dt-price:last").css("display","");
    		 $("ul.special-dt-price label.specialTitle:last").html("");
    		 $("ul.special-dt-price label.specialTitle:last").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
    		 
    		 $("ul.special-dt-price input[name=addNewPrice]:last").remove();
    		 $("ul.special-dt-price input[name=specialAmount]:last").after('&nbsp;&nbsp;<input type="button" name="delNewPrice" class="btn btn-primary gray mar_xjia_del" value="删除"/>');
    	 }else{
    		 jBox.tip("当前人数为0","info");
    	 }
     });
     // 删除成人新增整体报价
     $("#reply-eprice-form-id ul.adult-dt-price input[name=delNewPrice]").live("click",function(){
    	 $(this).parents("li.adult-dt-price").remove();
    	 allEprice();
     });
     // 删除儿童新增整体报价
     $("#reply-eprice-form-id ul.child-dt-price input[name=delNewPrice]").live("click",function(){
    	 $(this).parents("li.child-dt-price").remove();
    	 allEprice();
     });
     // 删除特殊人群新增整体报价
     $("#reply-eprice-form-id ul.special-dt-price input[name=delNewPrice]").live("click",function(){
    	 $(this).parents("li.special-dt-price").remove();
    	 allEprice();
     });
     
  // 成人细分报价
     $("#reply-eprice-form-id ul.adult-part-price input[name=addNewpartPrice]").live("click",function(){
    	 var number = $("input[name=adultSum]").val();
    	 if(parseInt(number)>1){
        	 var copy= $("li.adult-part-price:first").clone();
    		 $("ul.adult-part-price").append(copy);
    		 $("ul.adult-part-price li.adult-part-price:last").css("display","");
    		 $("ul.adult-part-price label.adultTitle:last").html("");
    		 $("ul.adult-part-price label.adultTitle:last").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
    		 
    		 $("ul.adult-part-price span.adultPersonNum:last").remove();
    		 $("ul.adult-part-price label.adultPartNum:last").after('<input type="text"  style="width:50px" name="adultPersonNum"  class="adultPersonNum"  value="0"/>');
    		 
    		 $("ul.adult-part-price input[name=addNewpartPrice]:last").remove();
    		 $("ul.adult-part-price input[name=adultPersonNum]:last").after('&nbsp;&nbsp;<input type="button" name="delNewPrice" class="btn btn-primary gray mar_xjia_del" value="删除"/>');
    	 }else{
    		 jBox.tip("当前人数不足以细分","info");
    	 }
     });
     // 儿童细分报价
     $("#reply-eprice-form-id ul.child-part-price input[name=addNewpartPrice]").live("click",function(){
    	 var number= $("input[name=childSum]").val();
    	 if(parseInt(number)>1){
        	 var copy= $("li.child-part-price:first").clone();
    		 $("ul.child-part-price").append(copy);
    		 $("ul.child-part-price li.child-part-price:last").css("display","");
    		 $("ul.child-part-price label.childTitle:last").html("");
    		 $("ul.child-part-price label.childTitle:last").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

    		 $("ul.child-part-price span.childPersonNum:last").remove();
    		 $("ul.child-part-price label.childPartNum:last").after('<input type="text"  style="width:50px" name="childPersonNum"  class="childPersonNum"  value="0"/>');
    		 
    		 $("ul.child-part-price input[name=addNewpartPrice]:last").remove();
    		 $("ul.child-part-price input[name=childPersonNum]:last").after('&nbsp;&nbsp;<input type="button" name="delNewPrice" class="btn btn-primary gray mar_xjia_del" value="删除"/>');
    	 }else{
    		 jBox.tip("当前人数不足以细分","info");
    	 }
     });
     // 特殊人群细分报价
     $("#reply-eprice-form-id ul.special-part-price input[name=addNewpartPrice]").live("click",function(){
    	 var number = $("input[name=specialSum]").val();
    	 if(parseInt(number)>1){
        	 var copy= $("li.special-part-price:first").clone();
    		 $("ul.special-part-price").append(copy);
    		 $("ul.special-part-price li.special-part-price:last").css("display","");
    		 $("ul.special-part-price label.specialTitle:last").html("");
    		 $("ul.special-part-price label.specialTitle:last").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

    		 $("ul.special-part-price span.specialPersonNum:last").remove();
    		 $("ul.special-part-price label.specialPartNum:last").after('<input type="text"  style="width:50px" name="specialPersonNum"  class="specialPersonNum"  value="0"/>');
    		 
    		 $("ul.special-part-price input[name=addNewpartPrice]:last").remove();
    		 $("ul.special-part-price input[name=specialPersonNum]:last").after('&nbsp;&nbsp;<input type="button" name="delNewPrice" class="btn btn-primary gray mar_xjia_del" value="删除"/>');
    	 }else{
    		 jBox.tip("当前人数不足以细分","info");
    	 }
     });
     // 删除成人新增细分报价
     $("#reply-eprice-form-id ul.adult-part-price input[name=delNewPrice]").live("click",function(){
    	 // 交还被删除组的人数
    	 var delNum = parseInt($(this).prev("span.adultPersonNum").html());
    	 if(delNum<=0 || isNaN(delNum)){
    		 delNum = 0;
    	 }
    	 var num = parseInt($("#reply-eprice-form-id ul.adult-part-price li.adult-part-price:eq(1) span.adultPersonNum").html());
    	 $("#reply-eprice-form-id ul.adult-part-price li.adult-part-price:eq(1) span.adultPersonNum").html(delNum+num);
    	 // 删掉组
    	 $(this).parents("li.adult-part-price").remove();
    	 // 重新计算统计值
    	 partEprice();
    	 
    	// allEprice();
     });
     // 删除儿童新增细分报价
     $("#reply-eprice-form-id ul.child-part-price input[name=delNewPrice]").live("click",function(){
    	 // 交还被删除组的人数
    	 var delNum = parseInt($(this).prev("span.childPersonNum").html());
    	 if(delNum<=0 || isNaN(delNum)){
    		 delNum = 0;
    	 }
    	 var num = parseInt($("#reply-eprice-form-id ul.child-part-price li.child-part-price:eq(1) span.childPersonNum").html());
    	 $("#reply-eprice-form-id ul.child-part-price li.child-part-price:eq(1) span.childPersonNum").html(delNum+num);
    	 // 删掉组
    	 $(this).parents("li.child-part-price").remove();
    	 // 重新计算统计值
    	 partEprice();
     });
     // 删除特殊人群新增细分报价
     $("#reply-eprice-form-id ul.special-part-price input[name=delNewPrice]").live("click",function(){
    	 // 交还被删除组的人数
    	 var delNum = parseInt($(this).prev("span.specialPersonNum").html());
    	 if(delNum<=0 || isNaN(delNum)){
    		 delNum = 0;
    	 }
    	 var num = parseInt($("#reply-eprice-form-id ul.special-part-price li.special-part-price:eq(1) span.specialPersonNum").html());
    	 $("#reply-eprice-form-id ul.special-part-price li.special-part-price:eq(1) span.specialPersonNum").html(delNum+num);
    	 // 删掉组
    	 $(this).parents("li.special-part-price").remove();
    	 // 重新计算统计值
    	 partEprice();
     });
     
     // 细分报价 切换成人币种
     $("#reply-eprice-form-id select[name=adultPartCurrency]").live("change",function(){
    	 var val = $(this).val(); // 币种汇率
    	 var id = $("option:selected", this).attr("id"); // 币种ID
    	 var mark = $("option:selected", this).attr("title"); // 币种标识
    	 var name = $("option:selected", this).text(); // 币种名称
    	 $(this).nextAll("input[name=adultPartExchangerate]").val(val);
    	 $(this).nextAll("input[name=adultPartCurrencyId]").val(id);
    	 $(this).nextAll("input[name=adultPartCurrencyName]").val(name);
    	 $(this).nextAll("span.adultPartCurrencyMark").text("");
    	 $(this).nextAll("span.adultPartCurrencyMark").text(mark);
    	 partEprice();
     });
     // 细分报价 切换儿童币种
     $("#reply-eprice-form-id select[name=childPartCurrency]").live("change",function(){
    	 var val = $(this).val(); // 币种汇率
    	 var id = $("option:selected", this).attr("id"); // 币种ID
    	 var mark = $("option:selected", this).attr("title"); // 币种标识
    	 var name = $("option:selected", this).text(); // 币种名称
    	 $(this).nextAll("input[name=childPartExchangerate]").val(val);
    	 $(this).nextAll("input[name=childPartCurrencyId]").val(id);
    	 $(this).nextAll("input[name=childPartCurrencyName]").val(name);
    	 $(this).nextAll("span.childPartCurrencyMark").text("");
    	 $(this).nextAll("span.childPartCurrencyMark").text(mark);
    	 partEprice();
     });
     // 细分报价 切换特殊人群币种
     $("#reply-eprice-form-id select[name=specialPartCurrency]").live("change",function(){
    	 var val = $(this).val(); // 币种汇率
    	 var id = $("option:selected", this).attr("id"); // 币种ID
    	 var mark = $("option:selected", this).attr("title"); // 币种标识
    	 var name = $("option:selected", this).text(); // 币种名称
    	 $(this).nextAll("input[name=specialPartExchangerate]").val(val);
    	 $(this).nextAll("input[name=specialPartCurrencyId]").val(id);
    	 $(this).nextAll("input[name=specialPartCurrencyName]").val(name);
    	 $(this).nextAll("span.specialPartCurrencyMark").text("");
    	 $(this).nextAll("span.specialPartCurrencyMark").text(mark);
    	 partEprice();
     });
     // 细分报价成人人数计算
     $("li.adult-part-price input.adultPersonNum").live("blur",function(){
    	 var number = parseInt($("span.adultPersonNum:eq(1)").html());
    	 var theNumber = parseInt($(this).val());
    	 if(theNumber>0 && theNumber<number){
    		 number = number-theNumber;
    		 $("span.adultPersonNum:eq(1)").html("");
    		 $("span.adultPersonNum:eq(1)").html(number);
    		 // 将文本框改成span，防止再次修改
    		 $(this).parent().find("label.adultPartNum").after('<span class="adultPersonNum"   style="width:100px">'+theNumber+'</span>');
    		 $(this).remove();
    		 partEprice();
    	 }else{
    		 jBox.tip("细分人数非法","警告");
    		 $(this).val(0);
    	 }
     });
  // 细分报价儿童人数计算
     $("li.child-part-price input.childPersonNum").live("blur",function(){
    	 var number = parseInt($("span.childPersonNum:eq(1)").html());
    	 var theNumber = parseInt($(this).val());
    	 if(theNumber>0 && theNumber<number){
    		 number = number-theNumber;
    		 $("span.childPersonNum:eq(1)").html("");
    		 $("span.childPersonNum:eq(1)").html(number);
    		 // 将文本框改成span，防止再次修改
    		 $(this).parent().find("label.childPartNum").after('<span class="childPersonNum"   style="width:100px">'+theNumber+'</span>');
    		 $(this).remove();
    		 partEprice();
    	 }else{
    		 jBox.tip("细分人数非法","警告");
    		 $(this).val(0);
    	 }
     });
  // 细分报价特殊人群人数计算
     $("li.special-part-price input.specialPersonNum").live("blur",function(){
    	 var number = parseInt($("span.specialPersonNum:eq(1)").html());
    	 var theNumber = parseInt($(this).val());
    	 if(theNumber>0 && theNumber<number){
    		 number = number-theNumber;
    		 $("span.specialPersonNum:eq(1)").html("");
    		 $("span.specialPersonNum:eq(1)").html(number);
    		 // 将文本框改成span，防止再次修改
    		 $(this).parent().find("label.specialPartNum").after('<span class="specialPersonNum"   style="width:100px">'+theNumber+'</span>');
    		 $(this).remove();
    		 partEprice();
    	 }else{
    		 jBox.tip("细分人数非法","警告");
    		 $(this).val(0);
    	 }
     });
     
     // 整体报价 切换成人币种
     $("#reply-eprice-form-id select[name=adultCurrency]").live("change",function(){
    	 var val = $(this).val(); // 币种汇率
    	 var id = $("option:selected", this).attr("id"); // 币种ID
    	 var mark = $("option:selected", this).attr("title"); // 币种标识
    	 var name = $("option:selected", this).text(); // 币种名称
    	 $(this).nextAll("input[name=adultExchangerate]").val(val);
    	 $(this).nextAll("input[name=adultCurrencyId]").val(id);
    	 $(this).nextAll("input[name=adultCurrencyName]").val(name);
    	 $(this).nextAll("span.adultCurrencyMark").text("");
    	 $(this).nextAll("span.adultCurrencyMark").text(mark);
    	 allEprice();
     });
     // 整体报价 切换儿童币种
     $("#reply-eprice-form-id select[name=childCurrency]").live("change",function(){
    	 var val = $(this).val(); // 币种汇率
    	 var id = $("option:selected", this).attr("id"); // 币种ID
    	 var mark = $("option:selected", this).attr("title"); // 币种标识
    	 var name = $("option:selected", this).text(); // 币种名称
    	 $(this).nextAll("input[name=childExchangerate]").val(val);
    	 $(this).nextAll("input[name=childCurrencyId]").val(id);
    	 $(this).nextAll("input[name=childCurrencyName]").val(name);
    	 $(this).nextAll("span.childCurrencyMark").text("");
    	 $(this).nextAll("span.childCurrencyMark").text(mark);
    	 allEprice();
     });
     // 整体报价 切换特殊人群币种
     $("#reply-eprice-form-id select[name=specialCurrency]").live("change",function(){
    	 var val = $(this).val(); // 币种汇率
    	 var id = $("option:selected", this).attr("id"); // 币种ID
    	 var mark = $("option:selected", this).attr("title"); // 币种标识
    	 var name = $("option:selected", this).text(); // 币种名称
    	 $(this).nextAll("input[name=specialExchangerate]").val(val);
    	 $(this).nextAll("input[name=specialCurrencyId]").val(id);
    	 $(this).nextAll("input[name=specialCurrencyName]").val(name);
    	 $(this).nextAll("span.specialCurrencyMark").text("");
    	 $(this).nextAll("span.specialCurrencyMark").text(mark);
    	 allEprice();
     });
     // 整体报价总计
     $("#reply-eprice-form-id input.allNum").live("blur",function(){
    	 if(parseFloat($(this).val())>0){
    		 var num = Math.round($(this).val()*100)/100; // 保留两位小数
    		 $(this).val(num);
        	 allEprice();
    	 }else{
    		 $(this).val(0);
    		 $.jBox.tip("输入数值不合法","提示");
    	 }
     });
     // 细分报价总计
     $("#reply-eprice-form-id input.partNum").live("blur",function(){
    	 if(parseFloat($(this).val())>0){
    		 var num = Math.round($(this).val()*100)/100; // 保留两位小数
    		 $(this).val(num);
        	 partEprice();
    	 }else{
    		 $(this).val(0);
    		 $.jBox.tip("输入数值不合法","提示");
    	 }
     });
     /** 20150511新增 end*/
});
/** 20150511新增 begin*/
//整体报价总计
function  allEprice(){
	// 计算成人总价
	var adultMap ={}; // key 结构：币种ID_币种名称_币种符号_币种汇率
	// 合计多币种成人价格
	$("#price-detaill-dl-all-id ul.adult-dt-price  input[name=adultAmount]").each(function(){
		 var id = $(this).prev().prev().val();
		 var name = $(this).prev().val();
		 var mark = $(this).prev().prev().prev().text();
		 var currency = $(this).parents("li.adult-dt-price").find("input[name=adultExchangerate]").val(); // 币种汇率
		 if(parseInt($(this).val())>0){
			 if(adultMap[id+"_"+name+"_"+mark+"_"+currency]){ // 如果数组中已经存在，则累加
//				 var num = parseInt(adultMap[id+"_"+name+"_"+mark+"_"+currency])+parseInt($(this).val());
//				 adultMap[id+"_"+name+"_"+mark+"_"+currency] = num;
				 $.jBox.tip("出现汇率相同的重复币种！","警告");
				 $(this).val(0);
			 }else{ // 如果数组中不存在，则放入
				 var num =parseFloat($(this).val());
				 adultMap[id+"_"+name+"_"+mark+"_"+currency] = num;
			 }
//			 var num = parseInt(parseInt($(this).val()));
//			 adultMap[id+"_"+name+"_"+mark+"_"+currency] = num;
		 }
	});
	// 计算乘以成人人数后的成人价格
	var adultNum = $("input[name=adultSum]").val(); // 成人数量
	if(adultMap && adultNum!=0){
		for(var k in adultMap){
			//console.log("成人原值统计："+k+"--"+adultMap[k]);
			adultMap[k] =Math.round(parseFloat(adultMap[k])*adultNum*100)/100;
			//console.log("成人乘以人数统计："+k+"--"+adultMap[k]);
		};
	}
	
	// 计算儿童总价
	var childMap = {};// key 结构：币种ID_币种名称_币种符号_币种汇率
	$("#price-detaill-dl-all-id ul.child-dt-price  input[name=childAmount]").each(function(){
		var id = $(this).prev().prev().val();
		 var name = $(this).prev().val();
		 var mark = $(this).prev().prev().prev().text();
		 var currency = $(this).parents("li.child-dt-price").find("input[name=childExchangerate]").val(); // 币种汇率
		 if(parseInt($(this).val())>0){
			 if(childMap[id+"_"+name+"_"+mark+"_"+currency]){ // 如果数组中已经存在，则累加
//				 var num = parseInt(childMap[id+"_"+name+"_"+mark+"_"+currency])+parseInt($(this).val());
//				 childMap[id+"_"+name+"_"+mark+"_"+currency] = num;
				 $.jBox.tip("出现汇率相同的重复币种！","警告");
				 $(this).val(0);
			 }else{ // 如果数组中不存在，则放入
				 var num =parseFloat($(this).val());
				 childMap[id+"_"+name+"_"+mark+"_"+currency] = num;
			 }
//			 var num = parseInt(parseInt($(this).val()));
//			 childMap[id+"_"+name+"_"+mark+"_"+currency] = num;
		 }
	});
	// 计算乘以儿童人数后的价格
	var childNum = $("input[name=childSum]").val(); // 成人数量
	if(childMap && childNum!=0){
		for(var k in childMap){
			//console.log("儿童原值统计："+k+"--"+childMap[k]);
			childMap[k] = Math.round(parseFloat(childMap[k])*childNum*100)/100;
			//console.log("儿童乘以人数统计："+k+"--"+childMap[k]); 
		};
	}
	
	// 计算特殊人群总价
	var specialMap = {};// key 结构：币种ID_币种名称_币种符号_币种汇率
	$("#price-detaill-dl-all-id ul.special-dt-price  input[name=specialAmount]").each(function(){
		var id = $(this).prev().prev().val();
		 var name = $(this).prev().val();
		 var mark = $(this).prev().prev().prev().text();
		 var currency = $(this).parents("li.special-dt-price").find("input[name=specialExchangerate]").val(); // 币种汇率
		 if(parseInt($(this).val())>0){
			 if(specialMap[id+"_"+name+"_"+mark+"_"+currency]){ // 如果数组中已经存在，则累加
//				 var num = parseInt(specialMap[id+"_"+name+"_"+mark+"_"+currency])+parseInt($(this).val());
//				 specialMap[id+"_"+name+"_"+mark+"_"+currency] = num;
				 $.jBox.tip("出现汇率相同的重复币种！","警告");
				 $(this).val(0);
			 }else{ // 如果数组中不存在，则放入
				 var num = parseFloat($(this).val());
				 specialMap[id+"_"+name+"_"+mark+"_"+currency] = num;
			 }
//			 var num = parseInt(parseInt($(this).val()));
//			 specialMap[id+"_"+name+"_"+mark+"_"+currency] = num;
		 }
	});
	// 计算乘以特殊人群人数后的价格
	var specialNum = $("input[name=specialSum]").val(); // 特殊人群数量
	if(specialMap && specialNum!=0){
		for(var k in specialMap){
			//console.log("特殊人群原值统计："+k+"--"+specialMap[k]);
			specialMap[k] = Math.round(parseFloat(specialMap[k])*specialNum*100)/100;
			//console.log("特殊人群乘以人数统计："+k+"--"+specialMap[k]);
		};
	}
	
	
	
	/* 展示在“整体报价”总计栏中(合计计算)
	 // 总计成人、儿童、特殊人群的总价
	var allMap = {}; // 全部总价
	// 加入成人总价
	if(adultMap && adultNum!=0){ 
		for(var k in adultMap){
			if(allMap[k]!=null){
				allMap[k] = parseInt(allMap[k])+parseInt(adultMap[k]);
			}else{
				allMap[k] = parseInt(adultMap[k]);
			}
		}
	}
	// 加入儿童总价
	if(childMap && childNum!=0){ 
		for(var k in childMap){
			if(allMap[k]!=null){
				allMap[k] = parseInt(allMap[k])+parseInt(childMap[k]);
			}else{
				allMap[k] = parseInt(childMap[k]);
			}
		}
	}
	// 加入特殊人群总价
	if(specialMap && specialNum!=0){ 
		for(var k in specialMap){
			if(allMap[k]!=null){
				allMap[k] = parseInt(allMap[k])+parseInt(specialMap[k]);
			}else{
				allMap[k] = parseInt(specialMap[k]);
			}
		}
	}
	var strall = "总计：<em class='red20'>0.00</em>";
	if(allMap){
		strall = "总计：";
		for(var k in allMap){
			if(parseInt(allMap[k])!=NaN){
				var ss = k.split("_"); //key 结构：币种ID_币种名称_币种符号_币种汇率
				strall += "<em class='gray14'>"+ss[2]+"</em> <em class='red20'>"+allMap[k]+".00</em> <em class='gray14'>"+ss[1]+"</em>  ";
			}
		}
	}*/
	/* 展示在“整体报价”总计栏中（分类计算） */
	var strall = "";
	if(adultMap && parseInt(adultNum)>0){ // 成人总价
		strall += "合计成人报价，其中：<em class='blur20'>"+adultNum+"</em>人共 ";
		for(var k in adultMap){
			if(parseInt(adultMap[k])!=NaN){ //key 结构：币种ID_币种名称_币种符号_币种汇率
				var part = k.split("_");
				strall += "<em class='gray14'>"+part[2]+"</em> <em class='red20'>"+adultMap[k]+"</em> ；";
			}
		}
		strall +="<br/>";
	}
	if(childMap && parseInt(childNum)>0){ // 儿童总价
		strall += "合计儿童报价，其中：<em class='blur20'>"+childNum+"</em>人共 ";
		for(var k in childMap){
			if(parseInt(childMap[k])!=NaN){
				var part = k.split("_");
				strall += " <em class='gray14'>"+part[2]+"</em> <em class='red20'>"+childMap[k]+"</em> ；";
			}
		}
		strall +="<br/>";
	}
	if(specialMap && parseInt(specialNum)>0){ // 特殊人群总价
		strall += "合计特殊人群报价，其中：<em class='blur20'>"+specialNum+"</em>人共  ";
		for(var k in specialMap){
			if(parseInt(specialMap[k])!=NaN){
				var part = k.split("_");
				strall += "<em class='gray14'>"+part[2]+"</em> <em class='red20'>"+specialMap[k]+"</em> ；";
			}
		}
		strall +="<br/>";
	}
	//console.log("总计："+strall);
	$("#price-detaill-dl-all-id p.strAll").html(strall);
}
// 细分报价总计
function partEprice(){
	var b = true;
	// 计算成人细分总价 
	var adultPartMap ={ }; // key 结构：币种ID_币种名称_币种符号_币种汇率_组内人数
	$("#price-detaill-dl-part-id ul.adult-part-price  input[name=adultPartAmount]").each(function(){
		
//		 var id = $(this).prev().prev().prev().val(); // 币种
//		 var name = $(this).prev().prev().val(); // 币种名称
//		 var mark = $(this).prev().prev().prev().prev().html(); // 币种符号
//		 var num = $(this).next().next().html(); // 组内人数
//		 var currency = $(this).prev().prev().prev().prev().prev().val(); // 币种汇率
		 var id = $(this).parents("li.adult-part-price").find("input[name=adultPartCurrencyId]").val(); // 币种
		 var name = $(this).parents("li.adult-part-price").find("input[name=adultPartCurrencyName]").val(); // 币种名称
		 var mark = $(this).parents("li.adult-part-price").find("span.adultPartCurrencyMark").html(); // 币种符号
		 var num = $(this).parents("li.adult-part-price").find("span.adultPersonNum").html(); // 组内人数
		 var currency = $(this).parents("li.adult-part-price").find("input[name=adultPartExchangerate]").val(); // 币种汇率
		 
		 if(parseInt(num)>0 && parseInt($(this).val())>0){ // 组内人数大于0,金额也大于0 才合法
			 if(adultPartMap[id+"_"+name+"_"+mark+"_"+currency+"_"+num]){
				 jBox.tip("细分报价出现重复！","警告");
				 b=false;
				 return false;
			 }else{
				 // 将组内人数乘以组内报价后的结果写入数组
				 adultPartMap[id+"_"+name+"_"+mark+"_"+currency+"_"+num] = parseFloat($(this).val())*parseFloat(num);
			 }
		 }
	});
	// 计算儿童细分总价 
	var childPartMap ={ }; // key 结构：币种ID_币种名称_币种符号_币种汇率_组内人数
	$("#price-detaill-dl-part-id ul.child-part-price  input[name=childPartAmount]").each(function(){
		var id = $(this).parents("li.child-part-price").find("input[name=childPartCurrencyId]").val(); // 币种
		 var name = $(this).parents("li.child-part-price").find("input[name=childPartCurrencyName]").val(); // 币种名称
		 var mark = $(this).parents("li.child-part-price").find("span.childPartCurrencyMark").html(); // 币种符号
		 var num = $(this).parents("li.child-part-price").find("span.childPersonNum").html(); // 组内人数
		 var currency = $(this).parents("li.child-part-price").find("input[name=childPartExchangerate]").val(); // 币种汇率
		 if(parseInt(num)>0 && parseInt($(this).val())>0){ // 组内人数大于0,金额也大于0 才合法
			 if(childPartMap[id+"_"+name+"_"+mark+"_"+currency+"_"+num]){
				 jBox.tip("细分报价出现重复！","警告");
				 b=false;
				 return false;
			 }else{
				 // 将组内人数乘以组内报价后的结果写入数组
				 childPartMap[id+"_"+name+"_"+mark+"_"+currency+"_"+num] = parseFloat($(this).val())*parseFloat(num);
			 }
		 }
	});
	// 计算特殊人群细分总价 
	var specialPartMap ={ }; // key 结构：币种ID_币种名称_币种符号_币种汇率_组内人数
	$("#price-detaill-dl-part-id ul.special-part-price  input[name=specialPartAmount]").each(function(){
		var id = $(this).parents("li.special-part-price").find("input[name=specialPartCurrencyId]").val(); // 币种
		 var name = $(this).parents("li.special-part-price").find("input[name=specialPartCurrencyName]").val(); // 币种名称
		 var mark = $(this).parents("li.special-part-price").find("span.specialPartCurrencyMark").html(); // 币种符号
		 var num = $(this).parents("li.special-part-price").find("span.specialPersonNum").html(); // 组内人数
		 var currency = $(this).parents("li.special-part-price").find("input[name=specialPartExchangerate]").val(); // 币种汇率
		 if(parseInt(num)>0 && parseInt($(this).val())>0){ // 组内人数大于0,金额也大于0 才合法
			 if(specialPartMap[id+"_"+name+"_"+mark+"_"+currency+"_"+num]){
				 jBox.tip("细分报价出现重复！","警告");
				 b=false;
				 return false;
			 }else{
				 // 将组内人数乘以组内报价后的结果写入数组
				 specialPartMap[id+"_"+name+"_"+mark+"_"+currency+"_"+num] = parseFloat($(this).val())*parseFloat(num);
			 }
		 }
	});
	if(!b){
		return ;
	}
	// 合计细分总价计算
	var strPart = "";
	if(adultPartMap){ // 成人总价
		strPart += "合计成人报价，其中：";
		for(var k in adultPartMap){
			if(parseFloat(adultPartMap[k])!=NaN){
				var part = k.split("_");
				strPart += "<em class='blur20'>"+part[4]+"</em>人共 <em class='gray14'>"+part[2]+"</em> <em class='red20'>"+adultPartMap[k]+"</em> ；";
			}
		}
		strPart +="<br/>";
	}
	if(childPartMap){ // 儿童总价
		strPart += "合计儿童报价，其中：";
		for(var k in childPartMap){
			if(parseFloat(childPartMap[k])!=NaN){
				var part = k.split("_");
				strPart += "<em class='blur20'>"+part[4]+"</em>人共 <em class='gray14'>"+part[2]+"</em> <em class='red20'>"+childPartMap[k]+"</em>；";
			}
		}
		strPart +="<br/>";
	}
	if(specialPartMap){ // 特殊人群总价
		strPart += "合计特殊人群报价，其中：";
		for(var k in specialPartMap){
			if(parseFloat(specialPartMap[k])!=NaN){
				var part = k.split("_");
				strPart += "<em class='blur20'>"+part[4]+"</em>人共 <em class='gray14'>"+part[2]+"</em> <em class='red20'>"+specialPartMap[k]+"</em>；";
			}
		}
		strPart +="<br/>";
	}
	//console.log("总计："+strPart);
	$("#price-detaill-dl-part-id p.strPart").html(strPart);
}

/**
 *  向整体报价表单中填值
 *  param: personMap 报价数组,personNum 报价人数,personType 报价类型（1、成人；2、儿童；3、特殊人群）
 *  
 * 
function allPriceForm(personMap,personNum,personType){
	var tempMap = {}; // 临时数组；
	if(personMap && personNum>0){  // key 结构：币种ID_币种名称_币种符号_币种汇率
		for(var tem in personMap){
			if(tempMap[tem]!=null){
				tempMap[tem] = parseInt(tempMap[tem])+parseInt(personMap[tem]);
			}else{
				tempMap[tem] = parseInt(personMap[tem]);
			}
		}
	}
	if(tempMap){
		for(var t in tempMap){
			if(parseInt(tempMap[t])!=NaN){
				var ss = t.split("_");
				$("#all_price_form").append("<input type='hidden' name='exchangerate' value='"+ss[3]+"'/>");		 // 汇率
				$("#all_price_form").append("<input type='hidden' name='currencyId' value='"+ss[0]+"'/>");		 // 币种ID
				$("#all_price_form").append("<input type='hidden' name='amount' value='"+ss[1]+"'/>");		 // 金额
				$("#all_price_form").append("<input type='hidden' name='personType' value='"+personType+"'/>");		 // 1、成人；2、儿童；3、特殊人群
			}
		}
		$("#all_price_form").append("<input type='hidden' name='personNum' value='"+personNum+"'/>");		 // 总人数
	}
} */ 

/** 20150511新增 end*/
var replayEPrice = {
		/**
		 * 询价接待要求表单json数据，通过这些数据生成询价接待表单
		 * @author lihua.xu
		 * @时间 2014年9月22日
		 * @type jsonArray
		 */
		travelRequirementsInputs :travelRequirements.formData,
		bigClass:travelRequirements.bigClass,
		travelRequirementsJson : null,
		
		dao : {
			/**
			 * 生成普通接待要求元素 html string，
			 * @author lihua.xu
			 * @时间 2014年9月28日
			 * @param {} m 接待要求内容
			 * @return {html string}
			 */
			travelRequirement : function(m,name){
				var html1 = '<div class="seach25 seach100"><p>&nbsp;&nbsp;&nbsp;&nbsp;'+name+'：</p><p class="seach_r">';
				 
				var msg = "(无)";
				if(m.option_0){
					html = replayEPrice.dao.travelRequirementOption(m.option_0);
					if(html!=""){ msg = ""; html1+=html;}
				}
				if(m.option_1){
					html = replayEPrice.dao.travelRequirementOption(m.option_1);
					if(html!=""){ msg = ""; html1+=html;}
				} 
				if(m.option_2){
					html = replayEPrice.dao.travelRequirementOption(m.option_2);
					if(html!=""){ msg = ""; html1+=html;}
				} 
				if(m.option_3){
					html = replayEPrice.dao.travelRequirementOption(m.option_3);
					if(html!=""){ msg = ""; html1+=html;}
				} 
				if(m.option_4){
					html = replayEPrice.dao.travelRequirementOption(m.option_4);
					if(html!=""){ msg = ""; html1+=html;}
				} 
				if(m.option_5){
					html = replayEPrice.dao.travelRequirementOption(m.option_5);
					if(html!=""){ msg = ""; html1+=html;}
				} 
				if(m.option_6){
					html = replayEPrice.dao.travelRequirementOption(m.option_6);
					if(html!=""){ msg = ""; html1+=html;}
				} 
				if(m.option_7){
					html = replayEPrice.dao.travelRequirementOption(m.option_7);
					if(html!=""){ msg = ""; html1+=html;}
				} 
				
				html1 += msg+'</p></div>';
				
				return html1;
			},
			
			
			/**
			 * 生成其他接待要求元素 html string，
			 * @author lihua.xu
			 * @时间 2014年9月28日
			 * @param {} m 其他接待要求列表
			 * @return {html string}
			 */
			otherRequirements : function(m){
				if(m==null || m.length==0){
					return "";
				}
				var t = [];
				for (var i = 0,len=m.length; i < len; i++) {
					t[i] = m[i].value;
				}
				var html = '<div class="seach25 seach100"><p>其他要求：</p><p class="seach_r">';
					html += '<span class="seach_check">';
					html += t.join(",");
					html += '</span></p></div>';
				return html;
			
			},
			
			/**
			 * 生成询价要求元素 html string，
			 * @author lihua.xu
			 * @时间 2014年9月28日
			 * @param {} m 询价要求列表
			 * @return {html string}
			 */
			epriceRequirements : function(m){
				if(m==null || m.length==0){
					return "";
				}
				var t = [];
				for (var i = 0,len=m.length; i < len; i++) {
					t[i] = m[i].value;
				}
				var html = '<div class="seach25 seach100"><p>询价要求：</p><p class="seach_r">';
					html += '<span class="seach_check">';
					html += t.join(",");
					html += '</span></p></div>';
				return html;
			
			},
			
			/**
			 * 生成其他价格输入项组 html string，
			 * @author lihua.xu
			 * @时间 2014年9月28日
			 * @return {html string}
			 */
			otherPrice : function(){  
				var html = '<dl class="wbyu-bot" name="otherPrice"><dt><label>其它：</label><input name="title" type="text" /></dt>';
					html += '<dt><label>价格：</label><input class="rmbp17" name="price" type="text" />元/人 <input class="seach_shortinput" name="sum" type="text" />人</dt>';
					html += '<dd class="ydbz_s gray clear-btn">删除</dd></dl>';
				return html;
			},
			
			
			
			travelRequirementOption : function(option){
				var htmls="";
				if(option.check=="unchecked"){
					return '';
				}
				
				if(option.title && option.title!="undefined"){
					htmls += '<span class="seach_check">'+option.title;
				}else{
					htmls += '<span class="seach_check">';
				}
				if(option.info){
					if(option.info.value){
						htmls += '-';
						if(option.info.prevname){
							htmls += option.info.prevname;
						}
						htmls += option.info.value;
						if(option.info.sufname){
							htmls += option.info.sufname;
						}
					}
				}
				htmls += '</span>';
				return htmls;
			}
		},
		
		service : {
			/**
			 * 生成普通接待要求元素 jquery dhtml，
			 * @author lihua.xu
			 * @时间 2014年9月28日
			 * @param {} m 接待要求内容
			 * @return {jquery dhtml}
			 */
			travelRequirement : function(m,name){
				var $html = $(replayEPrice.dao.travelRequirement(m,name));
					
				return $html;
			},
			/** 该方法于20150511 废弃，由新的多币种合计计算代替
			countPrice : function(){
				var adult = Number($("#adult-price-input-id").val()!=null?$("#adult-price-input-id").val():0);
				var child = Number($("#child-price-input-id").val()!=null?$("#child-price-input-id").val():0);;
				var special = Number($("#special-person-price-input-id").val()!=null?$("#special-person-price-input-id").val():0);
				var adultnum = Number($("#adultSum").text());
				var childnum = Number($("#childSum").text());
				var specialPersonSum = Number($("#specialPersonSum").text());
				
				var others = $("dl[name=otherPrice] input[name=price]");
				var otherNums = $("dl[name=otherPrice] input[name=sum]");
				
				var all =  adult*adultnum + child*childnum + special*specialPersonSum;
				// 附加费用数量数组
				if(others && otherNums){
					$.each(others,function(i,val){
						// 附加费用人数数组
						$.each(otherNums,function(n,nval){
							if(i==n){
								all=Number(all)+(Number($(val).val())*Number($(nval).val()));
								return true;
							}
						});
					});	
				}
				all = all.toFixed(2);
				$("#price-all-div-id em.red20").empty();
				$("#price-all-div-id em.red20").append(all);
				//alert($("#price-all-div-id em.red20").text());
				return all;
			},
			*/
			/**
			 * 生成其他接待要求元素 query dhtml，
			 * @author lihua.xu
			 * @时间 2014年9月28日
			 * @param {} m 其他接待要求列表
			 * @return {query dhtml}
			 */
			otherRequirements : function(m){
				var $html = $(replayEPrice.dao.otherRequirements(m));
					
				return $html;
			},
			
			/**
			 * 生成询价要求元素 query dhtml，
			 * @author lihua.xu
			 * @时间 2014年9月28日
			 * @param {} m 询价要求列表
			 * @return {query dhtml}
			 */
			epriceRequirements : function(m){
				var $html = $(replayEPrice.dao.epriceRequirements(m));
					
				return $html;
			},
			
			/**
			 * 初始化渲染询价接待要求
			 * @author lihua.xu
			 * @时间 2014年9月28日
			 */
			travelRequirementShow : function(){
				var json = replayEPrice.travelRequirementsJson;
				var o;
				var $div = $("#travel-requirements-div-id");
				
				//其他接待要求
				var otherRequirements = json.otherRequirements;
				delete json.otherRequirements;
				//询价要求
				var	 epriceRequirements =  json.epriceRequirements;
				delete json.epriceRequirements;
				
				
				var jsonBigClass = replayEPrice.bigClass;
				var jsonRequestInputs = replayEPrice.travelRequirementsInputs;
				for(var i =0;i<jsonBigClass.length;i++){
					var t = true;
					var $bigClassDiv = $('<div name="'+jsonBigClass[i].code+'"><strong>'+jsonBigClass[i].name  +'</strong><div class="kong"></div></div>');
					for(var j=0; j<jsonRequestInputs.length;j++ ){
						if((jsonBigClass[i].code==""&&typeof(jsonRequestInputs[j].bigClass) == "undefined")
								||(jsonBigClass[i].code==jsonRequestInputs[j].bigClass)){
							o = json[jsonRequestInputs[j].code];
//							if(getJsonLength(o)>0){
//								t = true;
//							}
							if(o){
								o.code = jsonRequestInputs[j].code;
								var $html = replayEPrice.service.travelRequirement(o,jsonRequestInputs[j].name);
								$bigClassDiv.append($html);	
							}
						}
					}
					if(t){
						$div.append($bigClassDiv);
					}
				}
				
				
				
				
//				for(var k in json){
//					o = json[k];
//					o.code = k;
//					$div.append(epriceInfo.service.travelRequirement(o));
//				}
//				
				$div.append(replayEPrice.service.otherRequirements(otherRequirements));
				$div.append(replayEPrice.service.epriceRequirements(epriceRequirements));
				
			},
			
			/**
			 * 生成其他价格输入项组 query dhtml
			 * @author lihua.xu
			 * @时间 2014年9月28日
			 */
			otherPrice : function(){
				var $html = $(replayEPrice.dao.otherPrice());
				
				$($html).find(".clear-btn").click(function(){
					$(this).parents("dl.wbyu-bot").remove();
				});
				
				$("#price-all-div-id").before($html);
			},
			
			/**
			 * 回复表单数据序列化成可提交形式
			 * @return {string formSerialize}
			 */
			replyFormSerialize : function(){
				var param = "content="+$("#reply-eprice-form-id textarea[name=content]").val();
				param += "&rid="+record.id;
				var fid = $("#saler-trip-file-id-id").val();
				if(fid!=null && fid!=""){
					param += "&salerTripFileId="+fid;
				}
				
				var json = {};
				
				json.adult = {price:$("#adult-price-input-id").val()};
				json.child = {price:$("#child-price-input-id").val()};
				json.specialPerson = {price:$("#special-person-price-input-id").val()};
				json.other = [];
				
				$("#reply-eprice-form-id dl[name=otherPrice]").each(function(){
					json.other.push({title:$(this).find("input[name=title]").val(),price:$(this).find("input[name=price]").val(),sum:$(this).find("input[name=sum]").val()});
				});
				
				param += "&priceDetail="+JSON.stringify(json);
				
				
				var arrFile = new Array();
				var arrFileName = new Array();
				var arrFilePath = new Array();
				var fileNum = 0;
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
				
				param += "&salerTripFileId="+arrFile;//行程文档Id
				param += "&salerTripFileName="+arrFileName;//行程文档name
				param += "&salerTipFilePath="+arrFilePath;//行程文档path
				
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
	                   		//alert(data.res);
	                   		jBox.tip("上传失败",data.res);
	                   }
	                },
	                error: function (data, status, e){
	                	//alert("fileerror");
	                	jBox.tip("文件错误！", 'info');
	                        
	                }  
	            });  
			},
			
			zeroNum : function(){
				var adult = $("#adult-price-input-id").val();
				var child = $("#child-price-input-id").val();
				var specialPersonPrice = $("#special-person-price-input-id").val();
				
				if(!adult){
					$("#adult-price-input-id").val(0);
				}
				if(!child){
					$("#child-price-input-id").val(0);
				}
				if(!specialPersonPrice){
					$("#special-person-price-input-id").val(0);
				}
			}
		},
		
		ajax : {
			
			/**
			 * 计调回复提交
			 * @author lihua.xu
			 * @时间 2014年9月28日
			 * 
			 */
			replySubmit : function(){
				var the_param = replayEPrice.service.replyFormSerialize();
				
				$.ajax({
					type : "POST",
					url : contextPath+"/eprice/manager/ajax/project/reply4admit",
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
							jBox.tip("提交成功", 'info');
							window.location.reload();
						}else if(json.res=="data_error"){
							jBox.tip("输入数据不正确", 'error');
						}else if(json.res!=null&&json.res!=""){
							jBox.tip(json.res, 'error');
						}else{
							jBox.tip("系统错误", 'error');
						}
					}
				
				});
			}
			
		}
	
};
function getJsonLength(jsonData){
	var jsonLength = 0;
	for(var item in jsonData){
		jsonLength++;
	}
	return jsonLength;
	}