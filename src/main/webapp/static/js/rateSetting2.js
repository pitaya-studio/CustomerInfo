/**
 * Created by ymx on 2016/8/10.
 */
$(function () {
    //切换
    $(".tab-switch li").click(function(){
       /* $(this).siblings().removeClass("select-tab");
        $(this).addClass("select-tab");*/
    });

    inputTips();

    $(".clear-all").click(function(){
        $(".items-container span").removeClass("selected");
        $(".items-container span:first-child").addClass("selected");
        $(".dateinput").val("");
        if($("input[name='nameCode'")){
        	$("input[name='nameCode'").val('');
        }
    });

    //全选
    $("#selectAll").click(function(){
        if($("#selectAll").is(":checked")){
            $('[name=indexBox]:checkbox').prop('checked', true);
        }else{
            $('[name=indexBox]:checkbox').removeAttr("checked");
        }
    });

    //反选
    $("#selectElse").click(function(){
    	/*var selectAll = true;*/
        $('[name=indexBox]:checkbox').each(function(){
            this.checked=!this.checked;
            if(!this.checked){
            	selectAll = false;
            }
        });
        /*if(selectAll){
        	alert(selectAll);
        	$("#selectAll").prop('checked', true);
        }else{
        	$("#selectAll").prop('checked', false);
        }*/
    });

    //各种全选、全不选判定
    $(".table_checkbox,#selectElse").click(function(){
        var n=$("input[name='indexBox']:checked").length;
        if(n==$(".table_checkbox").length){
            $("#selectAll").prop('checked', true);
        }else{
            $("#selectAll").removeAttr("checked");
        }
    });

    //各种降序、升序
    $(".up_and_down").click(function(){
        if($(this).find("i").hasClass("order_selected")){
            $(".order_selected").removeClass("order_selected").siblings("i").addClass("order_selected");
        }else{
            $(".order_selected").removeClass("order_selected")
            $(this).find(".fa-sort-asc").addClass("order_selected");
        }
        queryList();
    });

});