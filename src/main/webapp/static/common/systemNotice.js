//204 通知中心-忽略提醒--start
function ignoreNotice(obj) {
	$(obj).parent().parent().animate({
		width : "0px"
	}, function () {
		$('.head-contract dd').parent().parent().removeClass('head-contract-on');
		$.get(sysCtx + "/sys/remind/removeIsRemind");
	}).hide();
}
//204 通知中心-忽略提醒--end

//204 通知中心-通知内容显示隐藏-start
function showNoticeList() {
	if ($(".noticeList").hasClass("expended")) {
		$(".noticeList").animate({
			height : "0px"
		}, function () {
			$(".noticeList").removeClass('expended');
		});
	} else {
		$(".noticeList").animate({
			height : "84px"
		}, function () {
			$(".noticeList").addClass('expended');
		});
	}
}
//204 通知中心-通知内容显示隐藏-end
