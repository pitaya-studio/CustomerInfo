$(function () {
    $('.remark_div_child').niceScroll({
        cursorcolor: "#ccc",//#CC0071 �����ɫ
        cursoropacitymax: 1, //�ı䲻͸���ȷǳ���괦�ڻ״̬��scrollabar���ɼ�״̬������Χ��1��0
        touchbehavior: false, //ʹ����϶���������̨ʽ���Դ����豸
        cursorwidth: "5px", //���ع��Ŀ��
        cursorborder: "0", //     �α�߿�css����
        cursorborderradius: "5px",//������Ϊ���߽�뾶
        autohidemode: false //�Ƿ����ع�����
    });
    $(".remark_548").mouseover(function () {
        $(this).children().show();
        $(".remark_div_child").getNiceScroll().resize();
    });
    $(".remark_548").mouseleave(function () {
        $(this).children().hide();
        if ($(this).find("input[type='checkbox']").is(':checked')) {
            var reviewId = $(this).find("input[type='hidden']").val();
            $.ajax({ 
        		type:"GET",
        		url:ctx+"/costNew/payManager/removeShowRemark",
        		dataType:"json",
        		data:{reviewId:reviewId},
        		success: function(data) {
        			if (data.flag) {
        				
        			}
        		}
            });
        	
        	$(this).remove();
        }
    })
});