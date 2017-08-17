<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>库存-库存详情</title>
    <meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <!-- <link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon"/> -->
    <!--[if lte IE 6]>
    <link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css"/>
    <script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
    <![endif]-->
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/quauq.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css"/>
    <style type="text/css">
        .uiPrint span {
            width: 140px;
        }
        .pop-content {
            width: 100%;
            overflow: hidden;
            padding: 18px 0;
            display: none;
            background: #f4f4f4;
        }
        .label-style {
	        vertical-align:middle;
	        margin:0;
        }
    </style>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/modules/stock/stock-list.js"></script>
    <script type="text/javascript">
  	 //下载文件
	function downloads(docid){
		window.open("${ctx}/sys/docinfo/download/"+docid);
    }
    </script>
</head>
<body>
                <!--右侧内容部分开始-->
                <div class="produceDiv">
                    <div style="width:100%; height:20px;"></div>
                    <form enctype="multipart/form-data" method="post" action="/a/airTicket/save" class=" form-search"
                          id="addForm" novalidate="novalidate">
                        <div class="messageDiv">
                            <div class="kongr"></div>
                            <div style="display: block;" class="ydxbd">
                                <div class="activitylist_bodyer_right_team_co1">
                                    <label class="label-style">游轮名称：</label>
                                    <span class="text-show ellipsis-text-detail" title="${stock.cruiseshipInfoName}">${stock.cruiseshipInfoName}</span>
                                </div>
                                <div class="activitylist_bodyer_right_team_co2">
                                    <label class="label-style">船期：</label>
                                    <span class="text-show"  title="<fmt:formatDate value="${stock.shipDate }" pattern="yyyy-MM-dd" />">
                                   	 	<fmt:formatDate value="${stock.shipDate }" pattern="yyyy-MM-dd" />
                                    </span>
                                </div>
                            </div>
                            <!--0023列表开始-->
                                <table id="contentTable" class="table activitylist_bodyer_table">
                                    <thead>
                                    <tr>
                                        <th width="4%">序号</th>
                                        <th width="20%">舱型</th>
                                        <th width="10%"> 库存总数</th>
                                        <th width="5%">余位</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${details }" var="list" varStatus="s">
                                    <tr>
                                        <td class="tc">
                                            ${s.count}
                                        </td>
                                        <td class="tl">${list.cruiseshipCabinName}</td>
                                        <td class="tc">${list.stockAmount}</td>
                                        <td class="tc">${list.freePosition}</td>
                                    </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <!--0023列表结束-->
                            <!--填写价格开始-->
                            <div style="" class="mod_information" id="secondStepDiv">
                                <div class="mod_information_d" id="secondStepTitle"><span
                                        style=" font-weight:bold; padding-left:20px;float:left">其他信息</span></div>
                                <div id="secondStepEnd">
                                    <div class="add-remarks detail">
                                        <table>
                                            <tbody>
                                            <tr>
                                                <td class="vertical-align-top-td tr">备注：</td>
                                                <td class="tl">${stock.memo}</td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <div class="mod_information_dzhan_d" id="secondStepBtn" style="display: none;">
                                    <div class="release_next_add">
                                        <!--  input type="button" value="上一步" onclick="secondToOne()" class="btn btn-primary valid displayClick"-->
                                        <input value="下一步" onclick="secondToThird()"
                                               class="btn btn-primary valid displayClick" type="button">
                                    </div>
                                </div>
                                <div class="kong"></div>
                            </div>

                            <!--填写价格结束-->
                            <div style="clear:none;" class="kong"></div>
                            <div id="thirdStepDiv">
                                <!-- 上传文件 -->
                                <div class="mod_information_d7"></div>
                                <div class="mod_information_3 update-document">
                                    <div class="upload_file_list">
                                        <table style="vertical-align:middle;margin-top:10px;" name="company_logo"
                                               border="0">
                                            <tbody>
                                            <tr>
                                                <td class="tr"><label>游轮资料：</label></td>
                                                <td></td>
                                                <td></td>
                                                <!-- <td><input id="airticket_attach" name="airticket_attach"
                                                           class="mod_infoinformation3_file" value="选择文件"
                                                           onclick="uploadFiles('/a',null,this)" type="button">
                                                    <div class="uploadPath" style="display: none"></div>
                                                    <div id="currentFiles" style="display: none"></div>
                                                    <span class="color-gray">多文件，多格式上传附件，单个附件不大于20M，总共不大于20M</span>
                                                </td> -->
                                            </tr>
                                            <tr>
                                                <td colspan="3">
                                                    <div class="mod_information_dzhan">

                                                        <div class="batch">
                                                            <ol class="batch-ol">
                                                            <c:forEach items="${annexList}" var="file" varStatus="s1">
                                                                <li>
                                                                    <span>${file.docName}</span>
                                                                    <a style="margin-left:10px;"
                                                                       href="javascript:void(0)"
                                                                       onclick="downloads(${file.docId})">下载
                                                                    </a>
                                                                </li>
                                                               </c:forEach>
                                                            </ol>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <div class="release_next_add">
                                    <input value="关闭"  onclick="javascript:window.opener=null;window.open('','_self');window.close();"  class="btn btn-primary gray" type="button">
                                </div>
                            </div>
                    </form>
                </div>
                <!--右侧内容部分结束-->
</body>
</html>
