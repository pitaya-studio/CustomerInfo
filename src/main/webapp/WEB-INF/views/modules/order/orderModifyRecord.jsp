<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>修改记录</title>
    <meta name="decorator" content="wholesaler" />
    <!-- <link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon"/> -->
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <!--[if lte IE 8]>
    <script>
       (function() {
             if (! 
             /*@cc_on!@*/
             0) return;
             var e = "abbr, article, aside, audio, canvas, datalist, details, dialog, eventsource, figure, footer, header, hgroup, mark, menu, meter, nav, output, progress, section, time, video".split(', ');
             var i= e.length;
             while (i--){
                 document.createElement(e[i])
             } 
        })();
    </script>
    <![endif]-->
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.min.js"></script>
    
    <style>
        .modiContent {
            position: relative;
            display:block;
        }

        .ellipsis-number-team {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            display: block;
            width: 1000px;
            max-width: none;
        }

        .recordDetailContainer {
            display: none;
            position: absolute;
            z-index: 9999;
            top: 20px;
            top:40px\9;/*BUG#15067 兼容IE8*/
            left: 0;
            width: 400px;
            /*height: 300px;*/
            background: #ffffff;
            border: 1px solid #cccccc;
            max-height: 300px;
            overflow-y: auto;
            overflow-x:hidden;
            /*box-shadow: 2px 2px 2px 2px;*/
        }
		.content-group-log {
		    width: 376px;
		    margin: 0px auto;
		    padding: 0px 10px 0px 10px;
		}
        .modiRecord_container article section aside {
            margin-left: 50px
        }

        .modiRecord_container .point-time-group {
            left: 30px;
        }

        .modiRecord_container .content-group-log article section::before {
            left: 30px;
        }

        .modiRecord_container .content-group-log {
            margin-top: 10px;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            $('.modiContent').on('mouseover', function () {
                var $this = $(this);
                var $cotent = $this.find("article[name='article']");
    
                var orderId = $("#orderId").val();
               	var expend = $this.parent().parent().find(":first-child").val();
               	
                setTimeout(function () {
                	$.ajax({
		            type:"POST",
		            async:false,  
		            url:"${ctx}/orderCommon/manage/getOrderModifyByTime",
		            dataType:"json",
		            data:{    
		                    orderId:orderId,
		                    expend:expend
		                 },
		            success:function(data){
		           		if(data.result == 'success'){
							if(parseOrderLogJson(data.logOrderListByExtend, $cotent)){
								$this.parent().parent().parent().find('.recordDetailContainer').hide();     		
			                    $this.find(".ellipsis-number-team").siblings().show();
							}  
		           		}
		            }
		     	})
                });
            });
            $('.modiContent').mouseleave(function () {
                var $this = $(this);
                    $this.find(".ellipsis-number-team").siblings().hide();
                
            });
            $(document).on('click', '.close-group-log', function () {
                $(this).parent().hide();
            });
              $(".recordDetailContainer").niceScroll({
                cursorcolor: "#ccc",//#CC0071 光标颜色
                cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
                touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
                cursorwidth: "5px", //像素光标的宽度
                cursorborder: "0", //     游标边框css定义
                cursorborderradius: "5px",//以像素为光标边界半径
                autohidemode: false //是否隐藏滚动条
            });
        });
        
        // 依据json解析内容展示
        //var $cotent = $("#article");
		function parseOrderLogJson(jsonList ,$cotent){
			var html = "";
			if(jsonList == undefined || jsonList == null || jsonList == ''){
				return false;
			}
			var json = eval(jsonList);
			// json数组个数
			var jsonLength = json.length;
			// 判断为空
			if (jsonLength && jsonLength != 0) {
				// 循环获取html组合
				for (var i = 0; i < jsonLength; i++) {
					html += "<section>";
					html += "<span class='point-time-group step_yes'></span>";
					html += "<aside><p class='things'>";
					html += "<span class='duohangnormal'>"+ json[i].content +"</span>";
					html += " </p></aside>";
					html += "</section>";
				}
				$cotent.empty().append(html);
			}
			return true;
		}

    </script>
</head>
<body>
<div style="margin: 0 10px" class="modiRecord_container">
<h5>订单修改记录</h5>
<hr style="border-top:1px solid #000;width:100%;height:1px;"/>
	<input type="hidden" id="orderId" value="${orderId }">
    <table class="activitylist_bodyer_table">
        <thead>
        <tr>
            <th width="8%">序号</th>
            <th width="8%">修改人</th>
            <th width="12%">修改时间</th>
            <th width="70%">修改内容</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${logOrderList }" var="logOrder" varStatus="s">
	        <tr group="travler1">
	        	<input type="hidden" value="${ logOrder.expend}">
	            <td class="tc">${s.index + 1 }</td>
	            <td class="tc">${logOrder.create_by }</td>
	            <td class="tc"><fmt:formatDate value='${logOrder.create_date }' pattern='yyyy-MM-dd HH:mm:ss'/></td>
	            <td>
	            
	                            <span class="modiContent">
	                            	<span class="ellipsis-number-team">${logOrder.content }</span>
	                            	
	                                <div class="recordDetailContainer">
	
	                                    <div class="content-group-log">
	                                        <article name="article">
	                                        
	                                        </article>
	                                    </div>
	                                </div>
	                            </span>
	            </td>
	        </tr>
        </c:forEach>
        
        </tbody>
    </table>
</div>

</body>
</html>
