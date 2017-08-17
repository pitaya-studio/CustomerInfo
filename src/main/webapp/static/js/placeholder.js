/**
 * Created by wanglijun on 2016/10/18.
 * 为了使IE兼容placeholder
 */
$(document).ready(function(){
    var doc=document,
        inputs=doc.getElementsByTagName('input'),
        supportPlaceholder='placeholder'in doc.createElement('input'),

        placeholder=function(input){
            var text=input.getAttribute('placeholder'),
                defaultValue=input.defaultValue;
            if(defaultValue==''){
                input.value=text
                $(input).css('color','#ccc')
            }
            input.onfocus=function(){
                if(input.value===text)
                {
                    this.value=''

                }
                $(input).css('color','#333')
            };
            input.onblur=function(){
                if(input.value===''){
                    this.value=text
                    $(input).css('color','#ccc')
                }
            }
        };

    if(!supportPlaceholder){
        for(var i=0,len=inputs.length;i<len;i++){
            var input=inputs[i],
                text=input.getAttribute('placeholder');
            if(input.type==='text'&&text){
                placeholder(input)
            }
        }
    }
});
