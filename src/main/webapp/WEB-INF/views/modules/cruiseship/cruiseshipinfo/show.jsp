<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>基础信息维护-游轮-详情</title>
   <meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
   <!--  <link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon"/> -->
    <!--[if lte IE 6]>
    <link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css"/>
    <script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
    <![endif]-->
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/quauq.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script type="text/javascript">
     
    </script>
    <script type="text/javascript">
       
        function downloads(fiedId){
        	   location.href="${ctx}/cruiseshipInfo/downLoad?docId="+fiedId;
        }
        function closeWindows(){
    		window.open("${ctx}/cruiseshipInfo/list");
    		window.close();
        }

        /* function closeAll(){
        	window.close();
        } */
    </script>
</head>
<body>
        <!--左侧无需输出-->
       
                <!--右侧内容部分开始-->
                <div class="produceDiv">
                    <div style="width:100%; height:20px;"></div>
                    <form enctype="multipart/form-data" method="post" action="/a/airTicket/save" class=" form-search"
                          id="addForm" novalidate="novalidate">
                        <div class="messageDiv">
                            <div class="kongr"></div>
                            <div class="mod_information_d1 detail">
                                <label><span class="xing">*</span>游轮名称：</label>
                                <span class="text-show ellipsis-text-detail" title="${cruiseshipInfoInput.name }">${cruiseshipInfoInput.name }</span>
                            </div>
                            <div class="add-del-container">
                           	<c:forEach items="${cabinCruiseshipCabins }" var="c">
                                <div class="mod_information_d2 detail">
                                    <label>舱型：</label>
                                   <span class="text-show ellipsis-text-detail" title="">${c.name }</span>
                                </div>
                             </c:forEach>   
                            </div>
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
                                                    <td class="tl">${cruiseshipInfoInput.memo }</td>
                                                </tr>
                                            </tbody>
                                        </table>
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
                                            </tr>
                                            <tr>
                                                <td colspan="3">
                                                    <div class="mod_information_dzhan">

                                                        <div class="batch">
                                                            <ol class="batch-ol">
                                                            <c:forEach items="${list}" var="doc">
                                                                <li>
                                                                	<span>${doc.docName }</span>
                                                                    <a style="margin-left:10px;"
                                                                       href="javascript:void(0)"
                                                                       onclick="downloads(${doc.docId})">下载
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
                                    <input value="关闭"  onclick="closeWindows()"  class="btn btn-primary gray" type="button" >
                                </div>
                            </div>
                          </div> 
                         </div> 
                    </form>
                </div>
                <!--右侧内容部分结束-->
          
</body>
</html>