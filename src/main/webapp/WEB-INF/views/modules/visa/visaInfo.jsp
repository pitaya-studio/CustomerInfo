<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
.qz_title {
    background: url("http://file.cits.com.cn//css/b2c/visaCss/../../../images/b2c/bgimages/qz_title.gif") no-repeat scroll 0 0 rgba(0, 0, 0, 0);
    height: 34px;
    overflow: hidden;
    width: 494px;
}

</style>
<title>批发商产品管理平台</title>
<script type="text/javascript" src="http://testcn.trekiz.com/resource/scripts/jquery-1.9.0.js"></script>
<script type="text/javascript" src="http://testcn.trekiz.com/resource/scripts/jquery.form-3.36.0.min.js"></script>
<script type="text/javascript" src="http://testcn.trekiz.com/resource/scripts/jh-tab.js"></script>
<script type="text/javascript" src="http://testcn.trekiz.com/resource/scripts/calendar_v201301_chis.js"></script>
<script type="text/javascript" src="http://testcn.trekiz.com/resource/scripts/common_function.js"></script>
</head>
<body>
<div id="sea">
	 <div class="hedear">
	 <jsp:include page="top.jsp"></jsp:include>
	 </div>
	 
     <div class="main">
         <div class="main-left">
        <jsp:include page="left.jsp"></jsp:include>
         </div>
         <div class="main-right">
         
          <div class="qz_title left">
            <h1 class="fenlei" style="font-size:12px; padding-left:30px;">中国</h1>
          </div>
          
          <div class="cityList">
              
             <div style="height:80px; width:100px">
               <c:forEach items="${visaCityList}" var="visaInfo">
                     <Span>${visaInfo.visaType}</Span>
               </c:forEach>
             </div>
          
          </div>
            
         </div>
     </div>
     
	 <div class="bs-footer">
	 <jsp:include page="bottom.jsp"></jsp:include>
	 </div>
</div>	 
</body>
</html>