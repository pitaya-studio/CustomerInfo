<%@ page contentType="text/html;charset=UTF-8" %>
<script type="text/javascript">
$(function(){
	//公告
	notice();
	$(".list_lh").myScroll({
		speed:80, //数值越大，速度越慢
		rowHeight:20 //li的高度
	});
	
});

</script>

<!-- 公告显示 -->
<dl class="notice">
<dt class="tdred tr">公告：</dt>
<dd class="list_lh">
		<ul>
			 <c:forEach items="${fns:findVisaPublicBulletins()}" var="visaPublicBulletinVar">
                   <li>${visaPublicBulletinVar.content}</li>
             </c:forEach> 
		</ul>
	</dd>
</dl>