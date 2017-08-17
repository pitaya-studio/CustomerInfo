<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="X-UA-Compatible"content="IE=8">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>基本信息</title>
    <c:choose>
        <c:when test="${fn:contains(pageContext.request.requestURL,'huitengguoji.com')}">
            <link href="${ctxStatic}/images/huiTeng/huiTengFavicon.ico" rel="shortcut icon"/>
        </c:when>
        <c:when test="${fn:contains(pageContext.request.requestURL,'travel.jsjbt')}">
            <link href="${ctxStatic}/images/jinLing/jinLingFavicon.ico" rel="shortcut icon"/>
        </c:when>
        <c:otherwise>
            <link href="http://t215.quauqsystem.com.cn/favicon.ico" rel="shortcut icon"/>
        </c:otherwise>
    </c:choose>
    <link href="${ctxStatic}/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/t1t2.css"/>

    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1t2.js"></script>
    <script type="text/javascript" src="${ctxStatic}/agentToOffice/t1/agentbase/agentbase.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery/ajaxfileupload.js"></script>
    <script type="text/javascript">
        function simpleFileUpload(obj, ctx, fileId){
            //1. 后缀检查
            suffixCheck(obj);
            var div = $(obj).parent().parent().next();
            div.find('.progress-bar').css('width', '0px');
            div.show();
            //2. 文件上传
            $.ajaxFileUpload({
                url: ctx + '/MulUploadFile/ajaxFileUpload',
                type : 'POST',
                secureuri : false,
                fileElementId : fileId,
                dataType : 'JSON',
                success : function(data, status){
                    if('success' == status){
                        var prefix = data.indexOf('{')
                        var suffix = data.indexOf('}')
                        var temp = data.substring(prefix, suffix+1);
                        var json = JSON.parse(temp);
                        div.find('img').attr("src",ctx + "/person/info/getLogo?id="+json.id);
                        div.find('.progress-bar').css('width', '390px');
                        div.find('.agent_qual_id').val(json.id);
                        div.find('.agent_qual_name').val(json.fileName);
                    }
                },
                error : function(data, status, e){
                    if(status == 'error'){
                        alert('上传文件失败');
                        return false;
                    }
                }
            })
        }

        function suffixCheck(obj){
            var allowExtend = ".jpg,.gif,.png"; //允许上传文件的后缀名
            var extention = obj.value.substring(obj.value.lastIndexOf(".") + 1).toLowerCase();
            var browserVersion = window.navigator.userAgent.toUpperCase();
            if(allowExtend.indexOf(extention) == -1){
                obj.value = ""; //清空选中文件
                if (browserVersion.indexOf("MSIE") > -1) {
                    obj.select();
                    document.selection.clear();
                }
                obj.outerHTML = obj.outerHTML;
                alert("仅支持" + allowExtend + "为后缀名的文件!");
                return false;
            }
        }

        $(function(){
            $('.channel_brand').each(function(){
                var docName = $(this).find('.qual_show').text();
                if(!$.trim(docName)){
                    $(this).children('p').children('em').hide();
                    $(this).find('.upload_success_show').hide();
                }
            })
        })
    </script>
</head>
<body>
<!--header start-->
<c:if test="${updateShow != 1 }">
    <%@ include file="/WEB-INF/views/modules/homepage/T1Head.jsp"%>
</c:if>
<div class="sea">
    <!--main start-->
    <div class="main">
        <div class="middle">
            <div class="main-left">
                <ul>
                    <li><i></i><a href="${ctx}/person/info/getAgentInfo?agentId=${agentId}&updateShow=${updateShow}" style="color:#333333">基本信息</a></li>
                    <li><i></i><a href="${ctx}/person/info/getAgentBank?agentId=${agentId}&updateShow=${updateShow}" style="color:#333333">银行账户</a></li>
                    <li class="li-active"><i></i><a href="${ctx}/person/info/getAgentQualification?agentId=${agentId}&updateShow=${updateShow}">资质信息</a></li>
                </ul>
            </div>
            <div class="main-right">
                <div class="content">
                	<c:if test="${updateShow != 1 }">
                    <div class="bread"><i></i>您的位置：<a href="javascript:void(0)" onclick="goHomePage('${ctx}');" style="color:#333">首页</a> > 资质信息</div>
                    </c:if>
                    <div id="channel_information">
                        <p class="channel_information_border"></p>
                        <div class="channel_brand">
                            <p>
                                <span class="channel_width">营业执照：</span>
                                <span onmouseover="showImg(this)" onmouseout="hideImg(this)"
                                  onmousemove="moveImg(this)" class="qual_show">${business.docName}</span>
                                <span class="showimg hide">
                                    <img src="${ctx}/person/info/getLogo?id=${business.id}" height="auto" width="500" id="business_short">
                                </span>
                                <em class="t1_2 down_img margin_left_20" onclick="download_qualication(this, '${ctx}')"></em>
                                <span class="redact redact_use">
                                    <c:if test="${'1' ne updateShow}">
                                        <a class="orange float_right" href="javascript:void(0);" onclick="informationSpread(this)">编辑<em class="t1_2 copyReader"></em></a>
                                        <a class="orange float_right hide" href="javascript:void(0);" onclick="informationSpread(this)">收起<em class="t1_2 pick_up"></em></a>
                                    </c:if>
                                </span>
                            </p>
                            <div class="hide">
                                <div>
                                    <p class="up_load_p">
                                        <label class="up_load_img" for="up_load_img"><em class="up_load_img_child t1_2"></em>
                                            <input type="file" value="上传图片" class="doc-upload" id="up_load_img" accept=".jpg,.png,.gif" name="docName"
                                                   onchange="simpleFileUpload(this, '${ctx}', 'up_load_img')"/>上传图片</label>
                                    </p>
                                    <div class="hide">
                                        <div class="up_load_div">
                                            <div class="float_left up_load_img_div">
                                                <img src="${ctxStatic}/images/T1T2/no-logo.jpg" alt="" style="width:100%;height: 100%"/>
                                            </div>
                                            <div>
                                                <div class="progress_new">
                                                    <div class="progress-bar background_green" >
                                                        <span class="sr-only"></span>
                                                    </div>
                                                </div>
                                                <div class="up_success_false">
                                                    <em class="t1_2 up_success"></em>
                                                </div>
                                            </div>
                                            <em class="t1_2 close_t1 float_right" onclick="delete_upload_img(this)"></em>
                                        </div>
                                        <p class="up_load_p_save up_load_p">
                                            <input type="hidden" value="" class="agent_qual_id"/>
                                            <input type="hidden" value="" class="agent_qual_name"/>
                                            <span class="save_btn background_orange" onclick="save_agent_qualication(this, '${ctx}', 'A')">保 存</span>
                                        </p>
                                    </div>
                                    <div class="upload_success_show">
                                        <em class="t1_2 up_front"></em>
                                        <span>${business.docName}</span>
                                        <em class="t1_2 down_img margin_left_20" onclick="download_qualication(this, '${ctx}')"></em>
                                        <em class="t1_2 close_t1 margin_left_10" onclick="delete_qualication(this, '${ctx}', 'A')"></em>
                                        <input type="hidden" value="${business.id}" />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="channel_brand">
                            <p>
                                <span class="channel_width">经营许可证：</span>
                                <span onmouseover="showImg(this)" onmouseout="hideImg(this)"
                                      onmousemove="moveImg(this)" class="qual_show">${license.docName}</span>
                                <span class="showimg hide">
                                    <img src="${ctx}/person/info/getLogo?id=${license.id}" height="auto" width="500">
                                </span>
                                <em class="t1_2 down_img margin_left_20" onclick="download_qualication(this, '${ctx}')"></em>
                                <span class="redact redact_use">
                                    <c:if test="${'1' ne updateShow}">
                                        <a class="orange float_right" href="javascript:void(0);" onclick="informationSpread(this)">编辑<em class="t1_2 copyReader"></em></a>
                                        <a class="orange float_right hide" href="javascript:void(0);" onclick="informationSpread(this)">收起<em class="t1_2 pick_up"></em></a>
                                    </c:if>
                                </span>
                            </p>
                            <div class="hide">
                                <div>
                                    <p class="up_load_p">
                                        <label class="up_load_img" for="up_load_img2"><em class="up_load_img_child t1_2"></em>
                                            <input type="file" value="上传图片" class="doc-upload" id="up_load_img2" accept=".jpg,.png,.gif" name="license"
                                                   onchange="simpleFileUpload(this, '${ctx}', 'up_load_img2')"/>上传图片</label>
                                    </p>
                                    <div class="hide">
                                        <div class="up_load_div">
                                            <div class="float_left up_load_img_div">
                                                <img src="${ctxStatic}/images/T1T2/no-logo.jpg" alt="" style="width:100%;height: 100%" />
                                            </div>
                                            <div>
                                                <div class="progress_new">
                                                    <div class="progress-bar background_green" >
                                                        <span class="sr-only"></span>
                                                    </div>
                                                </div>
                                                <div class="up_success_false">
                                                    <em class="t1_2 up_success"></em>
                                                </div>
                                            </div>
                                            <em class="t1_2 close_t1 float_right" onclick="delete_upload_img(this)"></em>
                                        </div>
                                        <p class="up_load_p_save up_load_p">
                                            <input type="hidden" value="" class="agent_qual_id"/>
                                            <input type="hidden" value="" class="agent_qual_name"/>
                                            <span class="save_btn background_orange" onclick="save_agent_qualication(this, '${ctx}', 'B')">保 存</span>
                                        </p>
                                    </div>
                                    <div class="upload_success_show">
                                        <em class="t1_2 up_front"></em>
                                        <span>${license.docName}</span>
                                        <em class="t1_2 down_img margin_left_20" onclick="download_qualication(this, '${ctx}')"></em>
                                        <em class="t1_2 close_t1 margin_left_10" onclick="delete_qualication(this, '${ctx}', 'B')"></em>
                                        <input type="hidden" value="${license.id}" />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="channel_brand">
                            <p>
                                <span class="channel_width">税务登记证：</span>
                                <span onmouseover="showImg(this)" onmouseout="hideImg(this)"
                                  onmousemove="moveImg(this)" class="qual_show">${taxCertificate.docName}</span>
                                <span class="showimg hide">
                                    <img src="${ctx}/person/info/getLogo?id=${taxCertificate.id}" height="auto" width="500">
                                </span>
                                <em class="t1_2 down_img margin_left_20" onclick="download_qualication(this, '${ctx}')"></em>
                                <span class="redact redact_use">
                                    <c:if test="${'1' ne updateShow}">
                                        <a class="orange float_right" href="javascript:void(0);" onclick="informationSpread(this)">编辑<em class="t1_2 copyReader"></em></a>
                                        <a class="orange float_right hide" href="javascript:void(0);" onclick="informationSpread(this)">收起<em class="t1_2 pick_up"></em></a>
                                    </c:if>
                                </span>
                            </p>
                            <div class="hide">
                                <div>
                                    <p class="up_load_p">
                                        <label class="up_load_img " for="up_load_img3"><em class="up_load_img_child t1_2"></em>
                                            <input type="file" value="上传图片" class="doc-upload" id="up_load_img3" accept=".jpg,.png,.gif" name="taxcertificate"
                                            onchange="simpleFileUpload(this, '${ctx}', 'up_load_img3')"/>上传图片</label>
                                    </p>
                                    <div class="hide">
                                        <div class="up_load_div">
                                            <div class="float_left up_load_img_div">
                                                <img src="${ctxStatic}/images/T1T2/no-logo.jpg" alt="" style="width:100%;height: 100%" />
                                            </div>
                                            <div>
                                                <div class="progress_new">
                                                    <div class="progress-bar background_green" >
                                                        <span class="sr-only"></span>
                                                    </div>
                                                </div>
                                                <div class="up_success_false">
                                                    <em class="t1_2 up_success"></em>
                                                </div>
                                            </div>
                                            <em class="t1_2 close_t1 float_right" onclick="delete_upload_img(this)"></em>
                                        </div>
                                        <p class="up_load_p_save up_load_p">
                                            <input type="hidden" value="" class="agent_qual_id"/>
                                            <input type="hidden" value="" class="agent_qual_name"/>
                                            <span class="save_btn background_orange" onclick="save_agent_qualication(this, '${ctx}', 'C')">保 存</span>
                                        </p>
                                    </div>
                                    <div class="upload_success_show">
                                        <em class="t1_2 up_front"></em>
                                        <span>${taxCertificate.docName}</span>
                                        <em class="t1_2 down_img margin_left_20" onclick="download_qualication(this, '${ctx}')"></em>
                                        <em class="t1_2 close_t1 margin_left_10" onclick="delete_qualication(this, '${ctx}', 'C')"></em>
                                        <input type="hidden" value="${taxCertificate.id}" />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="channel_brand">
                            <p>
                                <span class="channel_width">组织机构代码：</span>
                                <span onmouseover="showImg(this)" onmouseout="hideImg(this)"
                                      onmousemove="moveImg(this)" class="qual_show">${organizeCertificate.docName}</span>
                                <span class="showimg hide">
                                    <img src="${ctx}/person/info/getLogo?id=${organizeCertificate.id}" height="auto" width="500">
                                </span>
                                <em class="t1_2 down_img margin_left_20" onclick="download_qualication(this, '${ctx}')"></em>
                                <span class="redact redact_use">
                                    <c:if test="${'1' ne updateShow}">
                                        <a class="orange float_right" href="javascript:void(0);" onclick="informationSpread(this)">编辑<em class="t1_2 copyReader"></em></a>
                                        <a class="orange float_right hide" href="javascript:void(0);" onclick="informationSpread(this)"><em class="t1_2 pick_up"></em>收起</a>
                                    </c:if>
                                </span>
                            </p>
                            <div class="hide">
                                <div>
                                    <p class="up_load_p">
                                        <label class="up_load_img " for="up_load_img4"><em class="up_load_img_child t1_2"></em>
                                            <input type="file" value="上传图片" class="doc-upload" id="up_load_img4" accept=".jpg,.png,.gif" name="organizecertificate"
                                            onchange="simpleFileUpload(this, '${ctx}', 'up_load_img4')"/>上传图片</label>
                                    </p>
                                    <div class="hide">
                                        <div class="up_load_div">
                                            <div class="float_left up_load_img_div">
                                                <img src="${ctxStatic}/images/T1T2/no-logo.jpg" alt="" style="width:100%;height: 100%" />
                                            </div>
                                            <div>
                                                <div class="progress_new">
                                                    <div class="progress-bar background_green" >
                                                        <span class="sr-only"></span>
                                                    </div>
                                                </div>
                                                <div class="up_success_false">
                                                    <em class="t1_2 up_success"></em>
                                                </div>
                                            </div>
                                            <em class="t1_2 close_t1 float_right" onclick="delete_upload_img(this)"></em>
                                        </div>
                                        <p class="up_load_p_save up_load_p">
                                            <input type="hidden" value="" class="agent_qual_id"/>
                                            <input type="hidden" value="" class="agent_qual_name"/>
                                            <span class="save_btn background_orange" onclick="save_agent_qualication(this, '${ctx}', 'D')">保 存</span>
                                        </p>
                                    </div>
                                    <div class="upload_success_show">
                                        <em class="t1_2 up_front"></em>
                                        <span>${organizeCertificate.docName}</span>
                                        <em class="t1_2 down_img margin_left_20" onclick="download_qualication(this, '${ctx}')"></em>
                                        <em class="t1_2 close_t1 margin_left_10" onclick="delete_qualication(this, '${ctx}', 'D')"></em>
                                        <input type="hidden" value="${organizeCertificate.id}" />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="channel_brand">
                            <p>
                                <span class="channel_width relative"><span class="position_absolute_id">公司法人身份证： <br/> <span class="gray">正反面在一起</span></span></span>
                                <span onmouseover="showImg(this)" onmouseout="hideImg(this)"
                                      onmousemove="moveImg(this)" class="qual_show">${idCard.docName} &nbsp;</span>
                                <span class="showimg hide">
                                    <img src="${ctx}/person/info/getLogo?id=${idCard.id}" height="auto" width="500">
                                </span>
                                <em class="t1_2 down_img margin_left_20" onclick="download_qualication(this, '${ctx}')"></em>
                                <span class="redact redact_use">
                                    <c:if test="${'1' ne updateShow}">
                                        <a class="orange float_right" href="javascript:void(0);" onclick="informationSpread(this)">编辑<em class="t1_2 copyReader"></em></a>
                                        <a class="orange float_right hide" href="javascript:void(0);" onclick="informationSpread(this)">收起<em class="t1_2 pick_up"></em></a>
                                    </c:if>
                                </span>
                            </p>
                            <div class="hide">
                                <div>
                                    <p class="up_load_p">
                                        <label class="up_load_img " for="up_load_img5"><em class="up_load_img_child t1_2"></em>
                                            <input type="file" value="上传图片" class="doc-upload" id="up_load_img5" accept=".jpg,.png,.gif" name="idcard"
                                            onchange="simpleFileUpload(this, '${ctx}', 'up_load_img5')"/>上传图片</label>
                                    </p>
                                    <div class="hide">
                                        <div class="up_load_div">
                                            <div class="float_left up_load_img_div">
                                                <img src="${ctxStatic}/images/T1T2/no-logo.jpg" alt="" style="width:100%;height: 100%" />
                                            </div>
                                            <div>
                                                <div class="progress_new">
                                                    <div class="progress-bar background_green" >
                                                        <span class="sr-only"></span>
                                                    </div>
                                                </div>
                                                <div class="up_success_false">
                                                    <em class="t1_2 up_success"></em>
                                                </div>
                                            </div>
                                            <em class="t1_2 close_t1 float_right" onclick="delete_upload_img(this)"></em>
                                        </div>
                                        <p class="up_load_p_save up_load_p">
                                            <input type="hidden" value="" class="agent_qual_id"/>
                                            <input type="hidden" value="" class="agent_qual_name"/>
                                            <span class="save_btn background_orange" onclick="save_agent_qualication(this, '${ctx}', 'E')">保 存</span>
                                        </p>
                                    </div>
                                    <div class="upload_success_show">
                                        <em class="t1_2 up_front"></em>
                                        <span>${idCard.docName}</span>
                                        <em class="t1_2 down_img margin_left_20" onclick="download_qualication(this, '${ctx}')"></em>
                                        <em class="t1_2 close_t1 margin_left_10" onclick="delete_qualication(this, '${ctx}', 'E')"></em>
                                        <input type="hidden" value="${idCard.id}" />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="channel_brand">
                            <p>
                                <span class="channel_width">公司银行开户许可证：</span>
                                <span onmouseover="showImg(this)" onmouseout="hideImg(this)"
                                      onmousemove="moveImg(this)" class="qual_show">${bankOpenLicense.docName}</span>
                                <span class="showimg hide">
                                    <img src="${ctx}/person/info/getLogo?id=${bankOpenLicense.id}" height="auto" width="500">
                                </span>
                                <em class="t1_2 down_img margin_left_20" onclick="download_qualication(this, '${ctx}')"></em>
                                <span class="redact redact_use">
                                    <c:if test="${'1' ne updateShow}">
                                        <a class="orange float_right" href="javascript:void(0);" onclick="informationSpread(this)">编辑<em class="t1_2 copyReader"></em></a>
                                        <a class="orange float_right hide" href="javascript:void(0);" onclick="informationSpread(this)">收起<em class="t1_2 pick_up"></em></a>
                                    </c:if>
                                </span>
                            </p>
                            <div class="hide">
                                <div>
                                    <p class="up_load_p">
                                        <label class="up_load_img" for="up_load_img6"><em class="up_load_img_child t1_2"></em>
                                            <input type="file" value="上传图片" class="doc-upload" id="up_load_img6" accept=".jpg,.png,.gif" name="bankOpenLicense"
                                            onchange="simpleFileUpload(this, '${ctx}', 'up_load_img6')"/>上传图片</label>
                                    </p>
                                    <div class="hide">
                                        <div class="up_load_div">
                                            <div class="float_left up_load_img_div">
                                                <img src="${ctxStatic}/images/T1T2/no-logo.jpg" alt="" style="width:100%;height: 100%" />
                                            </div>
                                            <div>
                                                <div class="progress_new">
                                                    <div class="progress-bar background_green" >
                                                        <span class="sr-only"></span>
                                                    </div>
                                                </div>
                                                <div class="up_success_false">
                                                    <em class="t1_2 up_success"></em>
                                                </div>
                                            </div>
                                            <em class="t1_2 close_t1 float_right" onclick="delete_upload_img(this)"></em>
                                        </div>
                                        <p class="up_load_p_save up_load_p">
                                            <input type="hidden" value="" class="agent_qual_id"/>
                                            <input type="hidden" value="" class="agent_qual_name"/>
                                            <span class="save_btn background_orange" onclick="save_agent_qualication(this, '${ctx}', 'F')">保 存</span>
                                        </p>
                                    </div>
                                    <div class="upload_success_show">
                                        <em class="t1_2 up_front"></em>
                                        <span>${bankOpenLicense.docName}</span>
                                        <em class="t1_2 down_img margin_left_20" onclick="download_qualication(this, '${ctx}')"></em>
                                        <em class="t1_2 close_t1 margin_left_10" onclick="delete_qualication(this, '${ctx}', 'F')"></em>
                                        <input type="hidden" value="${bankOpenLicense.id}" />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="channel_brand">
                            <p>
                                <span class="channel_width">旅游业资质：</span>
                                <span onmouseover="showImg(this)" onmouseout="hideImg(this)"
                                      onmousemove="moveImg(this)" class="qual_show">${travelAptitudes.docName}</span>
                                <span class="showimg hide">
                                    <img src="${ctx}/person/info/getLogo?id=${travelAptitudes.id}" height="auto" width="500">
                                </span>
                                <em class="t1_2 down_img margin_left_20" onclick="download_qualication(this, '${ctx}')"></em>
                                <span class="redact redact_use">
                                    <c:if test="${'1' ne updateShow}">
                                        <a class="orange float_right" href="javascript:void(0);" onclick="informationSpread(this)">编辑<em class="t1_2 copyReader"></em></a>
                                        <a class="orange float_right hide" href="javascript:void(0);" onclick="informationSpread(this)">收起<em class="t1_2 pick_up"></em></a>
                                    </c:if>
                                </span>
                            </p>
                            <div class="hide">
                                <div>
                                    <p class="up_load_p">
                                        <label class="up_load_img " for="up_load_img7"><em class="up_load_img_child t1_2"></em>
                                            <input type="file" value="上传图片" class="doc-upload" id="up_load_img7" accept=".jpg,.png,.gif" name="travelAptitudes"
                                            onchange="simpleFileUpload(this, '${ctx}', 'up_load_img7')"/>上传图片</label>
                                    </p>
                                    <div class="hide">
                                        <div class="up_load_div">
                                            <div class="float_left up_load_img_div">
                                                <img src="${ctxStatic}/images/T1T2/no-logo.jpg" alt="" style="width:100%;height: 100%" />
                                            </div>
                                            <div>
                                                <div class="progress_new">
                                                    <div class="progress-bar background_green" >
                                                        <span class="sr-only"></span>
                                                    </div>
                                                </div>
                                                <div class="up_success_false">
                                                    <em class="t1_2 up_success"></em>
                                                </div>
                                            </div>
                                            <em class="t1_2 close_t1 float_right" onclick="delete_upload_img(this)"></em>
                                        </div>
                                        <p class="up_load_p_save up_load_p">
                                            <input type="hidden" value="" class="agent_qual_id"/>
                                            <input type="hidden" value="" class="agent_qual_name"/>
                                            <span class="save_btn background_orange" onclick="save_agent_qualication(this, '${ctx}', 'G')">保 存</span>
                                        </p>
                                    </div>
                                    <div class="upload_success_show">
                                        <em class="t1_2 up_front"></em>
                                        <span>${travelAptitudes.docName}</span>
                                        <em class="t1_2 down_img margin_left_20" onclick="download_qualication(this, '${ctx}')"></em>
                                        <em class="t1_2 close_t1 margin_left_10" onclick="delete_qualication(this, '${ctx}', 'G')"></em>
                                        <input type="hidden" value="${travelAptitudes.id}" />
                                    </div>
                                </div>
                            </div>
                        </div>

                        <c:if test="${empty elseFileList}">
                            <div class="channel_brand">
                                <p>
                                    <span class="channel_width">其他资质：</span>
                                <span onmouseover="showImg(this)" onmouseout="hideImg(this)"
                                      onmousemove="moveImg(this)" class="qual_show">&nbsp;</span>
                                <span class="showimg hide">
                                    <img src="" height="auto" width="500">
                                </span>
                                <span class="redact redact_use">
                                    <c:if test="${'1' ne updateShow}">
                                        <a class="orange float_right" href="javascript:void(0);" onclick="informationSpread(this)">编辑<em class="t1_2 copyReader"></em></a>
                                        <a class="orange float_right hide" href="javascript:void(0);" onclick="informationSpread(this)">收起<em class="t1_2 pick_up"></em></a>
                                    </c:if>
                                </span>
                                </p>
                                <div class="hide">
                                    <div>
                                        <p class="up_load_p">
                                            <label class="up_load_img" for="up_load_img100"><em class="up_load_img_child t1_2"></em>
                                                <input type="file" value="上传图片" class="doc-upload" id="up_load_img100" accept=".jpg,.png,.gif" name="otherqualication"
                                                       onchange="simpleFileUpload(this, '${ctx}', 'up_load_img100')"/>上传图片</label>
                                        </p>
                                        <div class="hide">
                                            <div class="up_load_div">
                                                <div class="float_left up_load_img_div">
                                                    <img src="${ctxStatic}/images/T1T2/no-logo.jpg" alt="" style="width:100%;height: 100%"/>
                                                </div>
                                                <div>
                                                    <div class="progress_new">
                                                        <div class="progress-bar background_green" >
                                                            <span class="sr-only"></span>
                                                        </div>
                                                    </div>
                                                    <div class="up_success_false">
                                                        <em class="t1_2 up_success"></em>
                                                    </div>
                                                </div>
                                                <em class="t1_2 close_t1 float_right" onclick="delete_upload_img(this)"></em>
                                            </div>
                                            <p class="up_load_p_save up_load_p">
                                                <input type="hidden" value="" class="agent_qual_id"/>
                                                <input type="hidden" value="" class="agent_qual_name"/>
                                                <span class="save_btn background_orange" onclick="save_agent_qualication(this, '${ctx}', 'H')">保 存</span>
                                            </p>
                                        </div>
                                        <div class="upload_success_show">
                                            <em class="t1_2 up_front"></em>
                                            <span>&nbsp;</span>
                                            <em class="t1_2 down_img margin_left_20" onclick="download_qualication(this, '${ctx}')"></em>
                                            <em class="t1_2 close_t1 margin_left_10" onclick="delete_qualication(this, '${ctx}', 'H')"></em>
                                            <input type="hidden" value="" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:forEach items="${elseFileList}" var="doc" varStatus="index">
                        <div class="channel_brand">
                            <p>
                                <span class="channel_width">其他资质：</span>
                                <span onmouseover="showImg(this)" onmouseout="hideImg(this)"
                                      onmousemove="moveImg(this)" class="qual_show">${doc.docName}</span>
                                <span class="showimg hide">
                                    <img src="${ctx}/person/info/getLogo?id=${doc.id}" height="auto" width="500">
                                </span>
                                <em class="t1_2 down_img margin_left_20" onclick="download_qualication(this, '${ctx}')"></em>
                                <span class="redact redact_use">
                                    <c:if test="${'1' ne updateShow}">
                                        <a class="orange float_right" href="javascript:void(0);" onclick="informationSpread(this)">编辑<em class="t1_2 copyReader"></em></a>
                                        <a class="orange float_right hide" href="javascript:void(0);" onclick="informationSpread(this)">收起<em class="t1_2 pick_up"></em></a>
                                    </c:if>
                                </span>
                            </p>
                            <div class="hide">
                                <div>
                                    <p class="up_load_p">
                                        <label class="up_load_img" for="up_load_img${8 + index.count}"><em class="up_load_img_child t1_2"></em>
                                            <input type="file" value="上传图片" class="doc-upload" id="up_load_img${8 + index.count}" accept=".jpg,.png,.gif" name="other_qualication"
                                            onchange="simpleFileUpload(this, '${ctx}', 'up_load_img${8 + index.count}')"/>上传图片</label>
                                    </p>
                                    <div class="hide">
                                        <div class="up_load_div">
                                            <div class="float_left up_load_img_div">
                                                <img src="${ctxStatic}/images/T1T2/no-logo.jpg" alt="" style="width:100%;height: 100%"/>
                                            </div>
                                            <div>
                                                <div class="progress_new">
                                                    <div class="progress-bar background_green" >
                                                        <span class="sr-only"></span>
                                                    </div>
                                                </div>
                                                <div class="up_success_false">
                                                    <em class="t1_2 up_success"></em>
                                                </div>
                                            </div>
                                            <em class="t1_2 close_t1 float_right" onclick="delete_upload_img(this)"></em>
                                        </div>
                                        <p class="up_load_p_save up_load_p">
                                            <input type="hidden" value="" class="agent_qual_id"/>
                                            <input type="hidden" value="" class="agent_qual_name"/>
                                            <span class="save_btn background_orange" onclick="save_agent_qualication(this, '${ctx}', 'H')">保 存</span>
                                        </p>
                                    </div>
                                    <div class="upload_success_show">
                                        <em class="t1_2 up_front"></em>
                                        <span>${doc.docName}</span>
                                        <em class="t1_2 down_img margin_left_20" onclick="download_qualication(this, '${ctx}')"></em>
                                        <em class="t1_2 close_t1 margin_left_10" onclick="delete_qualication(this, '${ctx}', 'H')"></em>
                                        <input type="hidden" value="${doc.id}" />
                                    </div>
                                </div>
                            </div>
                        </div>
                        </c:forEach>
                    </div>
                    <div>
                        <p class="gray channel_information_bottom">注：图片上传格式仅支持jpg, png, gif</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- main end -->
    <%@ include file="/WEB-INF/views/modules/homepage/t1footer.jsp"%>
</div>
</body>
</html>
