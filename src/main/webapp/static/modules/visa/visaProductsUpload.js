
function  submitForm(status,thisobj){
	  //处理办签资料关于"其他"的处理:未勾选,其他后的内容不保存;勾选才保存-start
	    //处理原件的其他
	     if(!($("#originalProjectType4Others").attr("checked")=="checked")){ //未勾选,内容清空
	    	 $("input[name='original_Project_Name']").val("");
	     } 
	    //处理副件的其他
	     if(!($("#copyProjectType4Others").attr("checked")=="checked")){ //未勾选,内容清空
	    	 $("input[name='copy_Project_Name']").val("");
	     } 
	  //处理办签资料关于"其他"的处理:未勾选,其他后的内容不保存;勾选才保存-end
	  $(thisobj).addClass("disableCss");
	  $("#productStatus").val(status);
	  $("#addForm").attr("action","../../visa/visaProducts/save.htm");
	  $("#addForm").submit();
}
