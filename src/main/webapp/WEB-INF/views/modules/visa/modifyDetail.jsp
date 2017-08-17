<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>
		修改记录
	</title>
	<meta name="decorator" content="wholesaler"/>
	<script type="text/javascript" src="${ctxStatic }/js/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="${ctxStatic }/js/jquery-migrate-1.js"></script>
	<script type="application/javascript" src="${ctxStatic }/jqueryUI/ui/jquery-ui.js"></script>
	<script type="text/javascript" src="${ctxStatic }/js/common.js"></script>
	<script type="text/javascript" src="${ctxStatic }/jquery.nicescroll/jquery.nicescroll.min.js"></script>

	<style>
		.modiContent {
			position: relative;
			display: block;
		}
		.content-group-log {
			width: 380px;
			margin: 0px auto;
			padding: 0px 10px 0px 10px;
		}
		.ellipsis-number-team {
			overflow: hidden;
			text-overflow: ellipsis;
			white-space: nowrap;
			display: block;
			width: 700px;
			max-width: none;
		}

		.recordDetailContainer {
			display: none;
			position: absolute;
			z-index: 9999;
			top: 10px;
			left: 0;
			width: 400px;
			max-height: 300px;
			overflow-y:auto ;
			overflow-x: hidden;
			background: #ffffff;
			border: 1px solid #cccccc;
			/*box-shadow: 2px 2px 2px 2px;*/
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
				//序号
				var order = $this.parent().parent().children().first().text();
				//内容数组
				var arr = $this.parent().find(".ellipsis-number-team").text().split("，");
				var html = "";
				for(var i = 0; i < arr.length; i++) {
					var style = "";
//					if (i == arr.length - 1) {
//						style = "<span class='point-time-group end'></span>";
//					} else {
						style = "<span class='point-time-group step_yes'></span>";
//					}
					html += "<section>" +
						style +
						"<aside>" +
						"<p class='things'>" +
						"<span class='duohangnormal'>"+arr[i]+"</span>" +
						"</p>" +
						"</aside>" +
						"</section>";
				}

				$("#"+order).html(html);
				setTimeout(function () {
					$this.parent().parent().parent().find('.recordDetailContainer').hide();
					$this.find(".ellipsis-number-team").siblings().show();
				});
			});
			$('.modiContent').on('mouseleave', function () {
				$(this).find(".ellipsis-number-team").siblings().hide();
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
	</script>

</head>

<body>
	<div style="margin: 0 10px" class="modiRecord_container">
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
				<c:forEach items="${logOrderList}" var="logOrder" varStatus="s">
					<tr group="travler1">
						<td class="tc">${s.count}</td>
						<td class="tc">${fns:getUserNameById(logOrder.create_by)}</td>
						<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${logOrder.create_date}"/></td>
						<td>
							<span class="modiContent"><span class="ellipsis-number-team">
								${logOrder.content}
							</span>

								<div class="recordDetailContainer">
									<%--<span class="close-group-log" title="关闭">x</span>--%>

									<div class="content-group-log">
										<article id="${s.count}">
											<section>
												<span class="point-time-group step_yes"></span>
												<aside>
													<p class="things">
														<span class="duohangnormal">【字段名称】由【原值】修改成了【现值】【字段名称】由【原值】修改成了【现值】【字段名称】由【原值】修改成了【现值】</span>
													</p>
												</aside>
											</section>
											<section>
												<span class="point-time-group step_yes"></span>
												<aside>
													<p class="things">
														<span class="duohangnormal">【字段名称】由【原值】修改成了【现值】</span>
													</p>
												</aside>
											</section>
											<section>
												<span class="point-time-group step_yes"></span>
												<aside>
													<p class="things">
														<span class="duohangnormal">【字段名称】由【原值】修改成了【现值】</span>
													</p>
												</aside>
											</section>
											<section>
												<span class="point-time-group step_yes"></span>
												<aside>
													<p class="things">
														<span class="duohangnormal">【字段名称】由【原值】修改成了【现值】</span>
													</p>
												</aside>
											</section>
											<section>
												<span class="point-time-group step_yes"></span>
												<aside>
													<p class="things">
														<span class="duohangnormal">【字段名称】由【原值】修改成了【现值】</span>
													</p>
												</aside>
											</section>
											<section>
												<span class="point-time-group end"></span>
												<aside>
													<p class="things text-green">
														<span class="duohangnormal">【字段名称】由【原值】修改成了【现值】</span>
													</p>
												</aside>
											</section>
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
