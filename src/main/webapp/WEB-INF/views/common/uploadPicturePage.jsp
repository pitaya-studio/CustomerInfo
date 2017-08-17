<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/jquery-uploadfile/uploadify.css">
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-uploadfile/jquery.uploadify.min.js?f=<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" ></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/uploadPicAssembly/swfobject.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/uploadPicAssembly/fullAvatarEditor.js"></script>
<body>

<div style="width:632px;margin: 0 auto;text-align:center">
    <div>
        <p id="swfContainer">
            本组件需要安装Flash Player后才可使用，请从<a href="http://www.adobe.com/go/getflashplayer">这里</a>下载安装。
        </p>
        <input id="saveUploadUrl" type="hidden" name="saveUploadUrl" value="">
        <a id="successUpload" onclick="setUpoaldPath();" style="display:none;" href="javascript:void(0)"></a>
    </div>
</div>
<script type="text/javascript">
    var imgUrlReturn="";
    var swf="";
    var dataArr = new Array();
    var arrIndex = 0;
    swfobject.addDomLoadEvent(function () {
        swf = new fullAvatarEditor("${ctxStatic}/js/uploadPicAssembly/fullAvatarEditor.swf", "${ctxStatic}/js/uploadPicAssembly/expressInstall.swf", "swfContainer", {
                    id : 'swf',
                    upload_url : '${ctx}/MulUploadFile/uploadPicture;jsessionid=<%=session.getId()%>',	//上传接口
                    method : 'post',	//传递到上传接口中的查询参数的提交方式。更改该值时，请注意更改上传接口中的查询参数的接收方式
                    src_upload : 1,		//是否上传原图片的选项，有以下值：0-不上传；1-上传；2-显示复选框由用户选择
                    avatar_box_border_width : 0,
                    avatar_sizes : '95*125',
                    avatar_sizes_desc : '95*125像素'
                }, function (msg) {
                    switch(msg.code) {
                        case 3 :
                            if(msg.type == 0) {
                                alert("摄像头已准备就绪且用户已允许使用。");
                            } else if(msg.type == 1) {
                                alert("摄像头已准备就绪但用户未允许使用！");
                            } else {
                                alert("摄像头被占用！");
                            }
                            break;
                        case 5 :
                            if(msg.type == 0) {
                                if(msg.content) {
                                    dataArr[arrIndex] = msg.content;
                                    arrIndex++;
                                }
                            }
                            break;
                    }
                }
        );
    });
    window.swf=swf;
    function setUpoaldPath() {
        //把返回的data写到父窗口中id为uploadPath的div里边，用来保存返回的信息
        var divCon = $(".clickBtn",window.parent.document).parent().find(".uploadPath");
        var nameForFileId = $(".clickBtn",window.parent.document).attr("name");
        if(dataArr.length > 0) {
            var i = dataArr.length - 1;
            var result = dataArr[i].result;
            var itemArr = result.split("=");
            divCon.append("<input type='hidden' name='" + nameForFileId + "' value='" +itemArr[0]+ "' />")
                    .append("<input type='hidden' name='docOriName' value='" +itemArr[1]+ "' />")
                    .append("<input type='hidden' name='docPath' value='" +itemArr[2]+ "' />");

            divCon.next("#currentFiles").append("<input type='hidden' name='" + nameForFileId + "' value='" +itemArr[0]+ "' />")
                    .append("<input type='hidden' name='docOriName' value='" +itemArr[1]+ "' />")
                    .append("<input type='hidden' name='docPath' value='" +itemArr[2]+ "' />");
            dataArr.length = 0;
        }
    }
</script>
</body>
</html>
