/**
 * Created by wanglijun on 2016/12/8.
 */
//判断浏览器是否支持 placeholder属性
(function($){

function isPlaceholder(){
    var input = document.createElement('input');
    return 'placeholder' in input;
}

if (!isPlaceholder()) {//不支持placeholder 用jquery来完成
    $(document).ready(function() {
        if(!isPlaceholder()){
            $("input").not("input[type='password']").each(//把input绑定事件 排除password框
                function(){
                    if($(this).val()=="" && $(this).attr("placeholder")!=""){
                        $(this).val($(this).attr("placeholder"));
                        this.style.color='#aaa';
                        $(this).focus(function(){
                            if($(this).val()==$(this).attr("placeholder")) $(this).val("");
                            $(this).css('color','#fff')
                        });
                        $(this).blur(function(){
                            if($(this).val()=="") $(this).val($(this).attr("placeholder"));this.style.color='#aaa';
                        });
                    }
                });
            //对password框的特殊处理1.创建一个text框 2获取焦点和失去焦点的时候切换
            var pwdField    = $("input[type=password]");
            var pwdVal      = pwdField.attr('placeholder');
            pwdField.after('<input id="password1" type="text"  name="password"  style="width:330px;" class="no_boder_radius required" value='+pwdVal+' autocomplete="off" />');
            var pwdPlaceholder = $('#password1');
            pwdPlaceholder.show();
            pwdField.hide();
            pwdPlaceholder.css('color','#aaa')

            pwdPlaceholder.focus(function(){
                pwdPlaceholder.hide();
                pwdField.show();
                pwdField.focus();
                pwdField.css('color','#fff')
            });

            pwdField.blur(function(){
                if(pwdField.val() == '') {
                    pwdPlaceholder.show();
                    pwdField.hide();
                    pwdPlaceholder.css('color','#aaa')
                }
            });

        }
    });

}
    }
)(jQuery)