<%@ page contentType="text/html;charset=UTF-8" %>

<div class="seach25">
	<p>预计出团日期：</p>
	<p class="seach_r"><fmt:formatDate value="${record.admitRequirements.dgroupOutDate}" pattern="yyyy-MM-dd"/></p>
</div>
<div class="seach25">
	<p>出境口岸：</p>
	<p class="seach_r">${record.admitRequirements.outAreaName}</p>
</div>
<div class="seach25">
	<p>线路国家：</p>
	<p class="seach_r">${record.admitRequirements.travelCountry}</p>
</div>
<div class="seach25">
	<p>境外停留：</p>
	<p class="seach_r">${record.admitRequirements.outsideDaySum}天，${record.admitRequirements.outsideNightSum}晚 </p>
</div>
<div class="kong"></div>
      
<div id="travel-requirements-div-id">
	<span  id="travel-requirements-span-id" style="display: none;">${record.admitRequirements.travelRequirements}</span>
</div>
				 	
<div class="seach25 seach100"  fid="${record.admitRequirements.salerTripFile.id}">
	<p>行程附件：</p>
	<span class="fl">${record.admitRequirements.salerTripFile.fileName}</span>
	<c:if test="${upFileList.size()>=1}">
		<br/>
		<c:forEach items="${upFileList}" var="file">
			<div><strong>&nbsp;&nbsp;&nbsp;	·</strong>${file.fileName}</div>
		</c:forEach>
		<a class="ydbz_s" href="${ctx}/sys/docinfo/zipdownload/${upFileDocIds}/aditFiles">下载</a>
	</c:if>
	<c:if test="${upFileList.size()==0}">(无)</c:if>
</div>