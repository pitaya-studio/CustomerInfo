/**
 * Created by wanglijun on 2016/10/21.
 */
$(function(){
    var ctx=$("#ctx").val();
    var _orderId =sessionStorage.getItem("orderId");
    //去后台获取数据，并做展示
    $.ajax({
        type: "POST",
        url: ctx+"/t1/preOrder/manage/t1OrderDetail",
        cache:false,
        dataType:"json",
        async:false,
        data:{"orderId":_orderId},
        success: function(data) {
            var _productInfo=data.productInfo;
            var _contacts=data.contacts;
            var _transDetail=data.transDetail;
            var _remarks=data.remarks;
            var _orderStatus=data.orderStatus;
            var _payInfo=data.payInfo
            //下单详情
            var _t2OrderHtml="";
            for(var _key in _productInfo){
                if(!_productInfo[_key]){
                    _productInfo[_key]="";
                }
            }
            _t2OrderHtml+='<div class="ydbz_tit">下单详情</div><div class="orderdetails1">'
            _t2OrderHtml+='<table border="0" width="90%" style="margin-left:0;"><tbody>'
            _t2OrderHtml+='<tr><td class="mod_details2_d1">提交编号：</td>'
            _t2OrderHtml+='<td class="mod_details2_d2">'+data.orderCode+'</td>'
            _t2OrderHtml+='<td class="mod_details2_d1">订单状态：</td>'
            _t2OrderHtml+='<td class="mod_details2_d2">'+_orderStatus+'</td>'
            _t2OrderHtml+='<td class="mod_details2_d1">提交时间：</td>'
            _t2OrderHtml+='<td class="mod_details2_d2">'+data.orderSubmitDate+'</td></tr>'
            _t2OrderHtml+='<tr><td class="mod_details2_d1">批发商：</td>'
            _t2OrderHtml+='<td class="mod_details2_d2">'+_contacts.companyName+'</td>'
            _t2OrderHtml+='<td class="mod_details2_d1">联系人：</td>'
            _t2OrderHtml+='<td class="mod_details2_d2">'+_contacts.salerName+'</td>'
            _t2OrderHtml+='<td class="mod_details2_d1">电话：</td>'
            _t2OrderHtml+='<td class="mod_details2_d2">'+_contacts.salerPhone+'</td></tr>'
            _t2OrderHtml+='</tbody></table></div>';
            //产品信息
            _t2OrderHtml+='<div class="ydbz_tit">产品信息</div><div class="orderdetails2">';
            _t2OrderHtml+='<p class="ydbz_mc">'+_productInfo.productName+'</p>';
            _t2OrderHtml+=' <ul class="ydbz_info">';
            _t2OrderHtml+='<li><span>团号：</span>'+_productInfo.groupCode+'</li>';
            _t2OrderHtml+='<li><span>出团日期：</span>'+_productInfo.groupOpenDate+'</li>';
            _t2OrderHtml+='<li><span>出发城市：</span>'+_productInfo.fromArea+'</li>';
            _t2OrderHtml+='<li><span>行程天数：</span>'+_productInfo.activityDuration+'天</li>';
            _t2OrderHtml+='<li><span>交通工具：</span>'+_productInfo.trafficMode+'</li>';
            _t2OrderHtml+='</ul></div>';
            //交易明细
            _t2OrderHtml+='<div class="ydbz_tit">交易明细</div>' +
                '<table class="sus-table"><thead>' +
                '<tr><th width="150"></th><th width="255" class="tr">成人</th>' +
                '<th width="255" class="tr">儿童</th><th width="255" class="tr">特殊人群</th>' +
                ' </tr></thead><tbody>' +
                '<tr><td>实际结算价</td>' ;
            var _tempHtml=_transDetail.adultPrice=='--'?'--':_transDetail.adultCurrencyMark+_transDetail.adultPrice+'/人';
            _t2OrderHtml+='<td>'+_tempHtml+'</td>';
            _tempHtml=_transDetail.childPrice=='--'?'--':_transDetail.childCurrencyMark+_transDetail.childPrice+'/人';
            _t2OrderHtml+='<td>'+_tempHtml+'</td>';
            _tempHtml=_transDetail.specialPrice=='--'?'--':_transDetail.specialCurrencyMark+_transDetail.specialPrice+'/人';
            _t2OrderHtml+='<td>'+_tempHtml+'</td>';
            _t2OrderHtml+='</tr><tr>';
            _t2OrderHtml+='<td>系统结算价</td>';
            var _tempHtml=_transDetail.companyAdultPrice=='--'?'--':_transDetail.companyAdultCurrencyMark+_transDetail.companyAdultPrice+'/人';
            _t2OrderHtml+='<td>'+_tempHtml+'</td>';
            var _tempHtml=_transDetail.companyChildPrice=='--'?'--':_transDetail.companyChildCurrencyMark+_transDetail.companyChildPrice+'/人';
            _t2OrderHtml+='<td>'+_tempHtml+'</td>';
            var _tempHtml=_transDetail.companySpecialPrice=='--'?'--':_transDetail.companySpecialCurrencyMark+_transDetail.companySpecialPrice+'/人';
            _t2OrderHtml+='<td>'+_tempHtml+'</td>';
            _t2OrderHtml+='</tr><tr>';

            _t2OrderHtml+='<td>人数</td>';
            var _tempNum=_transDetail.adultNum=='--'?'--':(_transDetail.adultNum +'人')
            _t2OrderHtml+='<td>'+_tempNum+'</td>';
            var _tempNum=_transDetail.childNum=='--'?'--':(_transDetail.childNum +'人')
            _t2OrderHtml+='<td>'+_tempNum+'</td>';
            var _tempNum=_transDetail.specialNum=='--'?'--':(_transDetail.specialNum +'人')
            _t2OrderHtml+='<td>'+_tempNum+'</td>';
            _t2OrderHtml+='</tr><tr>';
            _t2OrderHtml+='<td><div>小计</div><div class="susTdGray">(实际结算价×人数)</div></td>';

            var _tempMark=[_transDetail.adultCurrencyMark,_transDetail.childCurrencyMark,_transDetail.specialCurrencyMark];
            var _tempSum=[_transDetail.adultSum,_transDetail.childSum,_transDetail.specialSum];
            for(var i=0;i<_tempMark.length;i++){
                if(_tempSum[i]=='--'){
                    _t2OrderHtml+='<td><span class="orange">--</span></td>';
                }else{
                    _t2OrderHtml+='<td>'+_tempMark[i]+'<span class="orange">'+_tempSum[i]+'</span></td>';
                }

            }
            _t2OrderHtml+='</tr><tr>';
            _t2OrderHtml+='<td  colspan="4"  class="summary">门店结算价差额返还总计：'+_transDetail.specialCurrencyMark+'<span>'+(_transDetail.profitsSum)+'</span></td></tr>';
            _t2OrderHtml+='</tbody></table>'
            //收款方式
            var _bankInfo=_payInfo.bankInfo;
            var _zfbInfo=_payInfo.zfbInfo;
            var _wxInfo=_payInfo.wxInfo;
            var _payId=data.payId;
            //收款方式是不存在的情况
            var _payInfoNoExit=false;
            _t2OrderHtml+='<div class="ydbz_tit">收款方式</div><div class="orderdetails2">';
            _t2OrderHtml+=' <ul class="ydbz_info">';
            if(_bankInfo.length>0){//说明选择的是银行卡支付
                for(var _temp=0;_temp<_bankInfo.length;_temp++){
                    if(_payId==_bankInfo[_temp].payId){
                        _t2OrderHtml+='<li><span>收款类型：</span>银行卡</li>';
                        _t2OrderHtml+='<li><span>账户号码：</span>尾号'+_bankInfo[_temp].bankCodeEnd.substring(_bankInfo[_temp].bankCodeEnd.length-4,_bankInfo[_temp].bankCodeEnd.length)+'</li>';
                        _payInfoNoExit=true;
                    }
                }

            }
            if(_zfbInfo.length>0){//说明选择的是支付宝支付
                for(var _temp=0;_temp<_zfbInfo.length;_temp++){
                    if(_payId==_zfbInfo[_temp].payId){
                        _t2OrderHtml+='<li><span>收款类型：</span>支付宝</li>';
                        _t2OrderHtml+='<li><span>账户号码：</span>'+_zfbInfo[_temp].accountCode+'</li>';
                        _payInfoNoExit=true;
                    }
                }

            }
            if(_wxInfo.length>0){//说明选择的是微信支付
                for(var _temp=0;_temp<_wxInfo.length;_temp++){
                    if(_payId==_wxInfo[_temp].payId){
                        _t2OrderHtml+='<li><span>收款类型：</span>微信</li>';
                        _t2OrderHtml+='<li><span>账户号码：</span>'+_wxInfo[_temp].accountCode+'</li>';
                        _payInfoNoExit=true;
                    }
                }

            }
            if(!_payInfoNoExit){
                _t2OrderHtml+='<li><span style="text-align: center">无</span></li>';
            }
            _t2OrderHtml+='</ul></div>';
            _t2OrderHtml+='<div class="ydbz_tit">备注</div>'
            _t2OrderHtml+='<div class="orderdetails2 remark-div">'+_remarks+'</div>';
             $("#ctx").after(_t2OrderHtml);
        },
            error:function(e){
                jBox.tip("请求失败，请联系系统管理员", 'error');
        }
    })

})