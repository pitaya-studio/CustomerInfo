/**
 * Created by zhh on 2016/1/28.
 */
//选中的国家
var selectedCountry = {};
$(document).on('click','[name="modify"]',function(){
    var $tds = $(this).parents('tr:first').children();
    var $pop = $.jBox($('#touristInfoModifyArea').html() ,{
        title:'修改',
        buttons:{'取消':0,'确认':1},
        submit: function(v, h, f){

        },
        height: 400,
        width: 350
    });
    var $combo = $pop.find('#selectCountry').comboboxInquiry();
    $combo.on('comboboxinquiryselect', function () {
        var $this = $(this);
        var val =  $this.val();
        var text = $this.children(':selected').text();
        if(val!=-1&&!selectedCountry[val]){
            selectedCountry[val] = text;
            $pop.find('.seach_checkbox_user').append('<a value="'+val+'">'+text+'</a>');
        }
    });
    //带入编号值
    $pop.find('[name="number"]').val($tds.eq(0).text());
    $pop.find('[name="name"]').val($tds.eq(1).text());
    //此时应获取适用国家的id与name,使用以下方法生成
    var val = '001';
    var text = '阿根廷';
    $pop.find('.seach_checkbox_user').append('<a value="'+val+'">'+text+'</a>');
    selectedCountry[val] = text;
    $pop.find('[name="mark"]').val($tds.eq(3).text());
});

$(function(){
    $(document).on("click", ".seach_checkbox_user em", function (e) {
        var $a = $(this).parent();
        delete selectedCountry[$a.attr('value')];
        $a.remove();
        stopDefault(e);
    })
    $(document).on("hover",".seach_checkbox_user a",function(e){
        $(this).append("<em></em>")
    })
    $(document).on("mouseleave",".seach_checkbox_user a",function(e){
        $(this).parent().find('em').remove();
    })

})