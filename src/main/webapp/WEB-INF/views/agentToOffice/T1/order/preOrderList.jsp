<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="decorator" content="wholesaler"/>
    <title>订单列表</title>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/channelPricing.css"/>
    <script src="${ctxStatic}/modules/order/orderList.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/orderInform.js" type="text/javascript"></script>
</head>
<body>
<!-- 搜索查询 -->
<%@ include file="/WEB-INF/views/modules/order/list/orderForm.jsp" %>
<div class="table-lists Mrn MinW">
    <table style="width:100%;" id="ProTab">
        <thead>
        <tr class="table-lists-header">
            <th style="width:44px;" class="tc">序号</th>
            <th style="width:150px;">提交编号</th>
            <th style="min-width:310px;">下单信息</th>
            <th style="width:70px;">人数</th>
            <th style="min-width:260px;">渠道信息</th>
            <th style="width:210px;">价格信息</th>
            <th style="width:110px;" class="tc">操作</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td class="tc">
                1
            </td>
            <td>
                <div class="pro-name">KFC001</div>
            </td>
            <td>
                <div class="pro-name">
                    <div>
                        别样韩国-首尔超值6日游
                    </div>
                    <div>
                        <span >团号:</span>66666
                    </div>
                    <div>
                        <span >下单时间:</span>2016-10-13 16:16:16
                    </div>
                </div>
            </td>
            <td>
                <div class="pro-name">4</div>
            </td>
            <td>
                <div class="pro-name">
                    <div>
                        北京青年旅行社
                    </div>
                    <div>
                        <span >联系人:</span>麦当劳
                    </div>
                    <div>
                        <span >电话:</span>400-851-7517
                    </div>
                </div>
            </td>
            <td>
                <div class="pro-name">
                    <div>
                        <span class="order-title">系统结算价:</span>$3800
                    </div>
                    <div>
                        <span class="order-title">价格总额:</span>$3800
                    </div>
                    <div>
                        <span class="order-title">门店结算价差额返还:</span>$3800
                    </div>
                </div>
            </td>
            <td class="tc">
                <div>待处理</div>
                <div >
                    <a href="javascript:void(0);">下单</a>
                    <a href="javascript:void(0);" class="orderUndo">取消</a>
                </div>
            </td>
        </tr>
        <tr class="tr_child height_30">
            <td class="paddingL order_remark" colspan="7">
                <span>备注：</span>这是填写的备注信息。
            </td>
        </tr>
        </tbody>
    </table>
</div>
<div class="pagination">
    <ul>
        <li class="disabled"><a href="">« 上一页</a></li>
        <li class="active"><a href="">1</a></li>
        <li><a href="">2</a></li>
        <li><a href="">3</a></li>
        <li><a href="">4</a></li>
        <li><a href="">5</a></li>
        <li><a href="">6</a></li>
        <li><a href="">7</a></li>
        <li><a href="">8</a></li>
        <li class="disabled"><a href="">...</a></li>
        <li><a href="">16</a></li>
        <li><a href="">下一页 »</a></li>
        <li class="disabled controls">
            <a href="">第 <input type="text" value="1"> / 16 页 ， 每页 <input type="text" value="10"> 条，共 157 条</a>
        </li>
    </ul>
</div>
</body>
</html>

