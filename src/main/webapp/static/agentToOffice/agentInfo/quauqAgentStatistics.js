var sysCtx;
$(function () {
	sysCtx = $("#sysCtx").val();
	$("#agentId").comboboxInquiry();
	$("#companyId").comboboxInquiry();
});

function page(n, s) {
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").submit();
	return false;
}

function search(type) {
	if (type == 1) {
		$("#searchForm").attr("action", sysCtx + "/quauqAgent/manage/quauqAgentStatistics").submit();
	} else {
		window.open(sysCtx + "/quauqAgent/manage/downloadAllOrder" + "?" + $('#searchForm').serialize());
		$("#searchForm").attr("action", sysCtx + "/quauqAgent/manage/quauqAgentStatistics").submit();
	}
}
