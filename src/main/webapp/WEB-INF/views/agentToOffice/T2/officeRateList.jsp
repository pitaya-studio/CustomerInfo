<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler">
    <title>费率设置</title>
    <link rel="stylesheet" href="${ctxStatic}/css/common.css" /><!--css初始化代码-->
    <link rel="stylesheet" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.css" /><!--字体图标-->
    <link rel="stylesheet" href="${ctxStatic}/css/table.css" />
    <link rel="stylesheet" href="${ctxStatic}/css/page-T2.css" />
    <link rel="stylesheet" href="${ctxStatic}/css/input.css" />
    <link rel="stylesheet" href="${ctxStatic}/css/search.css" />
    <%--<link rel="stylesheet" href="${ctxStatic}/css/setRate.css" />--%>
    <link rel="stylesheet" href="${ctxStatic}/css/rateSetting-1.css" />
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
    <script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/rateSetting1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/search.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script type="text/javascript">
        function page(n,s){
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
        function search(){
        	$("#searchForm").attr("action","${ctx}/quauqAgent/manage/ratelist");
            $("#searchForm").submit();
        }
        function checkRateVal(obj){
            var val = $(obj).val();
            var val1 = val.replace(/[^\d.]/g,'');
            if (val != val1){
                $.jBox.tip('输入值存在非数字字符,已删除!','warnning');
            }
            $(obj).val(val1);
            if (val1 <= 100){
                return true;
            }else {
                var rateType = $(obj).next().find("input").val(); // 费率的类型，%,￥
                if (rateType == "百分比"){
                    $.jBox.tip('百分比类型，数值不可大于100!','warnning');
                    $(obj).val("");
                }
            }
        }
        // added for 展开/收起筛选 by tlw at 20170302 start
        $(function () {
            launch();
        });
        // added for 展开/收起筛选 by tlw at 20170302 end

//        $(function () {
//            $(".input_use").keyup(function(){
//                onlyNum(this);
//            });
//        });
//
//        //数字验证
//        function onlyNum(dom){
//            var thisvalue = $(dom).val();
//            var minusSign = false;
//            if(thisvalue){
//                if(/^\-/.test(thisvalue)){
//                    minusSign = true;
//                    thisvalue = thisvalue.substring(1);
//                }
//                thisvalue = thisvalue.replace(/[^\d\.]/g,"").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
//                var txt = thisvalue.split(".");
//                thisvalue = txt[0]+(txt.length>1?"."+txt[1]:"");
//                if(minusSign){
//                    thisvalue = '-' + thisvalue;
//                }
//                $(dom).val(thisvalue);
//            }else{
//                //$(dom).val(0);
//            }
//        }
    </script>
</head>
<body>
<%--added for tab标签 by tlw at 20170302 start--%>
<content tag="three_level_menu">
    <li class="active"><a href="javascript:void(0)">批发商费率管理</a></li>
</content>
<%--added for tab标签 by tlw at 20170302 end--%>
<!--页面右侧主体部分-->
        <form method="post" action="${ctx}/quauqAgent/manage/ratelist" id="searchForm">
            <input type="hidden" value="${page.pageNo}" name="pageNo" id="pageNo">
            <input type="hidden" value="${page.pageSize}" name="pageSize" id="pageSize">
            <div class="activitylist_bodyer_right_team_co">

            <div class="activitylist_bodyer_right_team_co2">
                <input type="text" name="companyName" class="searchInput" value="${companyName}"placeholder="请输入批发商名称">
            </div>
            <div class="zksx">筛选</div>
            <div class="form_submit">
                <input id="btn_search" class="btn btn-primary ydbz_x" type="button" onclick="search()" value="搜索">
                <%--<input class="btn ydbz_x" type="reset" value="清空所有条件">--%>
                <input class="btn ydbz_x" type="button"	onclick="resetSearchParams()" value="清空所有条件" />
                <input class="btn ydbz_x daochu_Excel" onclick="exportExcel('${ctx}')" type="button" value="导出Excel">
            </div>
                <div class="ydxbd">
                    <div class="activitylist_bodyer_right_team_co1">
                        <label class="activitylist_team_co3_text">抽成费率：</label>
                        <input class="input_use inputTxt" oninput="validNum(this)"   type="text"  name="chouchengRateMin" value = "${chouchengRateMin }"> 至
                        <input class="input_use inputTxt" oninput="validNum(this)" type="text"  name="chouchengRateMax" value = "${chouchengRateMax }">
                    </div>
                    <div class="activitylist_bodyer_right_team_co1" style="width:360px">
                        <label class="activitylist_team_co3_text" style="width:107px">QUAUQ产品费率：</label>
                        <input class="input_use inputTxt" oninput="validNum(this)" type="text"  name="quauqRateMin" value = "${quauqRateMin }"> 至
                        <input class="input_use inputTxt" oninput="validNum(this)" type="text"  name="quauqRateMax" value = "${quauqRateMax }">
                    </div>
                </div>


            <%--<div class="search-content" style="position: relative;">--%>
                <%--<input type="text" name="companyName" class="searchInput" value="${companyName}" flag="istips">--%>
                <%--<div class="search-btn" onclick="search()"><em class="fa fa-search"></em> 搜索</div>--%>
                <%--<div class="daochu_Excel" onclick="exportExcel('${ctx}')"> 导出Excel</div>--%>
            <%--</div>--%>
            <%--<div class="inline margin_r_50">--%>
            <%--抽成费率：<input class="input_use " oninput="validNum(this)"   type="text"  name="chouchengRateMin" value = "${chouchengRateMin }"> 至--%>
            				<%--<input class="input_use" oninput="validNum(this)" type="text"  name="chouchengRateMax" value = "${chouchengRateMax }">--%>
            <%--</div>		--%>
            <%--<div class="inline">--%>
            <%--QUAUQ产品费率：<input class="input_use" oninput="validNum(this)" type="text"  name="quauqRateMin" value = "${quauqRateMin }"> 至--%>
            				<%--<input class="input_use" oninput="validNum(this)" type="text"  name="quauqRateMax" value = "${quauqRateMax }">--%>
            <%--</div>--%>
            </div>
        </form>
        <!-- 搜索框 end -->
        <!-- 全选反选、设置默认费率按钮 begin-->
        <div id="all-select-button">
                    <span>
                        <input id="table-checkAll" type="checkbox">
                        <label for="table-checkAll" onclick="checkAll()">全选</label>
                    </span>
            <span id="table-checkReverse">反选</span>
            <%--<span class="rateSettingButton"onclick="jbox_set_rate_pop('${ctx}')">设置默认费率</span>--%>
            <input class="btn rateSettingButton" value="设置默认费率" type="button" onclick="jbox_set_rate_pop('${ctx}')">
        </div>
        <!-- 全选反选、设置默认费率按钮 end-->
        <!-- 表格 begin -->
        <table id="contentTable" class="activitylist_bodyer_table mainTable">
            <thead>
            <tr>
                <th width="4%" rowspan="2">序号</th>
                <th width="13%" rowspan="2">批发商名称</th>
                <th width="9%" rowspan="2">QUAUQ产品费率</th>
                <th width="9%" rowspan="2">抽成费率</th>
                <th width="20%" colspan="2">门店（默认费率）</th>
                <th width="20%" colspan="2">总社（默认费率）</th>
                <th width="20%" colspan="2">集团公司（默认费率）</th>
                <th width="5%" rowspan="2">操作</th>
            </tr>
            <tr>
                <th>QUAUQ费率</th>
                <th>渠道费率</th>
                <th>QUAUQ费率</th>
                <th>渠道费率</th>
                <th>QUAUQ费率</th>
                <th>渠道费率</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.list}" var="item" varStatus="status">
                <tr>
                    <td><input type="checkbox" class="table_checkbox" id="${item.companyUuid}" name="num">${status.count}</td>
                    <td>${item.companyName}</td>
                    <td class="quauq-pr">
                        <c:choose>
                            <c:when test="${item.quauqRate1 ne '-'}">${item.quauqRate1}</c:when>
                            <c:when test="${item.quauqRate2 ne '-'}">${item.quauqRate2}</c:when>
                            <c:when test="${item.quauqRate3 ne '-'}">${item.quauqRate3}</c:when>
                            <c:otherwise>0</c:otherwise>
                        </c:choose>
                    </td>
                    <!-- 抽成费率 560需求，add by chao.zhang -->
                     <td class="choucheng-pr">
                        <c:choose>
                            <c:when test="${item.chouchengRate1 ne '-'}">${item.chouchengRate1}</c:when>
                            <c:when test="${item.chouchengRate2 ne '-'}">${item.chouchengRate2}</c:when>
                            <c:when test="${item.chouchengRate3 ne '-'}">${item.chouchengRate3}</c:when>
                            <c:otherwise>0</c:otherwise>
                        </c:choose>
                    </td>
                    <td class="sales">
                        其他：<span class="quauq-or">${item.quauqOtherRate1}</span>
                    </td>
                    <td class="sales">
                        产品：<span class="channel-pr">${item.agentRate1}</span>
                        <br>
                        其他：<span class="channel-or">${item.agentOtherRate1}</span>
                    </td>
                    <td class="moffice">
                        其他：<span class="quauq-or">${item.quauqOtherRate2}</span>
                    </td>
                    <td class="moffice">
                        产品：<span class="channel-pr">${item.agentRate2}</span>
                        <br>
                        其他：<span class="channel-or">${item.agentOtherRate2}</span>
                    </td>
                    <td class="group">
                        其他：<span class="quauq-or">${item.quauqOtherRate3}</span>
                    </td>
                    <td class="group">
                        产品：<span class="channel-pr">${item.agentRate3}</span>
                        <br>
                        其他：<span class="channel-or">${item.agentOtherRate3}</span>
                    </td>
                    <td class="tc">
                        <a href="${ctx}/group/strategy/GroupRateStrategyList?companyId=${item.companyId}&companyName=${item.companyName}"  target="_blank">费率管理</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <!-- 表格 end -->
        <div class="pagination clearFix">${page}</div>
<!-- 设置默认费率弹窗 begin -->
<div id="rate_setting_pop"class="display-none">
    <div class="rate_setting_pop">
        <div class="quauq-product-rate">
            <span>
                <em class="red">* </em>QUAUQ产品费率：
                <input class="quauq-pr  select_input"type="text" maxlength="5" onkeyup="checkRateVal(this)" oninput="checkRateVal(this)">
                <div class="dl-select">
                    <input type="text" value="百分比" readonly>
                    <ul class="select-option" style="display: none;">
                        <li>百分比</li>
                        <li>金额￥</li>
                    </ul>
                </div>
            </span>
        </div>
        <div class="choucheng-product-rate">
            <span >
                抽成费率：
                <input class="choucheng-pr  choucheng-pr_use"type="text" maxlength="5" onkeyup="checkRateVal(this)" oninput="checkRateVal(this)">
                <div class="dl-select">
                    <input type="text" value="百分比" readonly>
                    <ul class="select-option" style="display: none;">
                        <li>百分比</li>
                        <li>金额￥</li>
                    </ul>
                </div>
            </span>
        </div>
        <div class="clear"></div>
        <!-- 160812 QUAUQ产品费率放在最上 E -->
        <div class="rate-setting" id="sales">
            <h5>门店：</h5>
            <span class="first-span">
                <p>QUAUQ其他费率</p>
                <input class="quauq-or"type="text" maxlength="5" onkeyup="checkRateVal(this)" oninput="checkRateVal(this)">
                <div class="dl-select">
                    <input type="text" value="百分比" readonly>
                    <ul class="select-option" style="display: none;">
                        <li>百分比</li>
                        <li>金额￥</li>
                    </ul>
                </div>
            </span>
            <span>
                <p>渠道产品费率</p>
                <input class="channel-pr"type="text" maxlength="5" onkeyup="checkRateVal(this)" oninput="checkRateVal(this)">
                <div class="dl-select">
                    <input type="text" value="百分比" readonly>
                    <ul class="select-option" style="display: none;">
                        <li>百分比</li>
                        <li>金额￥</li>
                    </ul>
                </div>
            </span>
            <span>
                <p>渠道其他费率</p>
                <input class="channel-or"type="text" maxlength="5" onkeyup="checkRateVal(this)" oninput="checkRateVal(this)">
                <div class="dl-select">
                    <input type="text" value="百分比" readonly>
                    <ul class="select-option" style="display: none;">
                        <li>百分比</li>
                        <li>金额￥</li>
                    </ul>
                </div>
            </span>


        </div>
        <div class="rate-setting" id="moffice">
            <h5>总社费率：</h5>
            <span class="first-span">
                <p>QUAUQ其他费率</p>
                <input class="quauq-or"type="text" maxlength="5" onkeyup="checkRateVal(this)" oninput="checkRateVal(this)">
                <div class="dl-select">
                    <input type="text" value="百分比" readonly>
                    <ul class="select-option" style="display: none;">
                        <li>百分比</li>
                        <li>金额￥</li>
                    </ul>
                </div>
            </span>
            <span>
                <p>渠道产品费率</p>
                <input class="channel-pr"type="text" maxlength="5" onkeyup="checkRateVal(this)" oninput="checkRateVal(this)">
                <div class="dl-select">
                    <input type="text" value="百分比" readonly>
                    <ul class="select-option" style="display: none;">
                        <li>百分比</li>
                        <li>金额￥</li>
                    </ul>
                </div>
            </span>
            <span>
                <p>渠道其他费率</p>
                <input class="channel-or"type="text" maxlength="5" onkeyup="checkRateVal(this)" oninput="checkRateVal(this)">
                <div class="dl-select">
                    <input type="text" value="百分比" readonly>
                    <ul class="select-option" style="display: none;">
                        <li>百分比</li>
                        <li>金额￥</li>
                    </ul>
                </div>
            </span>
        </div>
        <div class="rate-setting" id="group">
            <h5>集团公司费率：</h5>
            <span class="first-span">
                <p>QUAUQ其他费率</p>
                <input class="quauq-or"type="text" maxlength="5" onkeyup="checkRateVal(this)" oninput="checkRateVal(this)">
                <div class="dl-select">
                    <input type="text" value="百分比" readonly>
                    <ul class="select-option" style="display: none;">
                        <li>百分比</li>
                        <li>金额￥</li>
                    </ul>
                </div>
            </span>
            <span>
                <p>渠道产品费率</p>
                <input class="channel-pr"type="text" maxlength="5" onkeyup="checkRateVal(this)" oninput="checkRateVal(this)">
                <div class="dl-select">
                    <input type="text" value="百分比" readonly>
                    <ul class="select-option" style="display: none;">
                        <li>百分比</li>
                        <li>金额￥</li>
                    </ul>
                </div>
            </span>
            <span>
                <p>渠道其他费率</p>
                <input class="channel-or"type="text" maxlength="5" onkeyup="checkRateVal(this)" oninput="checkRateVal(this)">
                <div class="dl-select">
                    <input type="text" value="百分比" readonly>
                    <ul class="select-option" style="display: none;">
                        <li>百分比</li>
                        <li>金额￥</li>
                    </ul>
                </div>
            </span>
        </div>
    </div>
</div>
<!-- 设置默认费率弹窗 end -->
</body>
</html>
