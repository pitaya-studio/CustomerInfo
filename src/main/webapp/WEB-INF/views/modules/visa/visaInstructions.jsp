<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>接待社后台</title>
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/modules/visa/visa.js" type="text/javascript"></script>
<script src="${ctxStatic}/flexbox-0.9.6/FlexBox/js/jquery.flexbox.min.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/flexbox-0.9.6/FlexBox/css/jquery.flexbox.css" />
<script type="text/javascript">

$(function(){
	
    $.ajax({
        type: "POST",
        url: "${ctx}/sys/country/list",
        success: function(msg){
        	var countries = {};
        	countries.results = msg
            countries.total = countries.results.length;
		    $('#selectcountry').flexbox(countries, {
		        watermark: '请输入国家名',
		        paging: false,
		        maxVisibleRows: 8
		    });
        }
     });
	
	$("#seachButton").click(function(){
		var countrys="";
        $(".showCountryClass").each(function(key,value){
           if($(value).is(":checked")){
        	   countrys = countrys + $(this).attr("name")+";";
            $('.vise2').show();
			  };
       });
		if(!countrys||countrys.length<=0){
			return;
		}else{
			$.ajax({
	            type: "POST",
	            url: "${ctx}/visa/instruction/showFileByCountrys",
	            data: {
	            	countrys:countrys
	            },
	            success: function(msg){
	            	showSeachVisaResult($("#reslutUl"),msg.data,msg.visaType);
	            	$(".resultNum").text(msg.data.length+"条");
	            }
	         });
		}
	});
});
</script>
<style type="text/css">
.vise{ background-color: #F5F5F5; border-radius: 4px;list-style: none outside none; margin: 0 0 20px; padding: 8px 15px;}
#seachButton{ background:#62AFE7;color:#fff;border-radius: 4px;border:none; padding: 4px 12px;}
#reslutUl li{ display:inline-block;}
.main-right { border-top: 3px #D0D7DD solid; background: #EDF0F2; margin-left: 100px;padding:0px 30px 30px 30px;}
.row{margin-left: 0px ;}
.resultli{margin-left: 15px ;display: block;width: 150px;}
.visainstructions_button{ margin-top:15px;display:block;background:#62AFE7;border:none;border-radius:4px;color:#fff;padding:4px 12px;}
</style>
</head>
<body>
    <content tag="three_level_menu">
        <shiro:hasPermission name="product:manager:view"><li><a href="${ctx}/activity/manager/list">已发布产品</a></li></shiro:hasPermission>
        <shiro:hasPermission name="product:manager:add"><li><a href="${ctx}/activity/manager/form">发布新产品</a></li></shiro:hasPermission>
        <li class="active"><a href="#">签证资料</a></li> 
    </content>
    <div class="vise">
    <p style="font:12px/1.5 '宋体'">根据以下条件检索国家签证，点“<span style="color:#3A7851">查询</span>”查看结果</p>
    <div class="team_bill">签证资料库</div>
		<table style="font-size:12px;border-bottom:1px dotted #b3b3b3">
			<tr>
				<td style="vertical-align:top;padding:10px 10px 10px 0; width:70px;">签证国家：</td>
				<td style="vertical-align:top;padding:10px 10px 10px 0;">
				    <c:forEach items="${validCountry}" var="Country">
						<div style='width:150px;float: left;' class='item'>
							<label style="font-size:12px;"> <input class='showCountryClass' TYPE='Checkbox' name='${Country.id}' style="margin:0 5px 5px 0;"> 
								<span>${Country.countryName_cn}</span>
							</label>
						</div>
					</c:forEach>
				</td>
			</tr>
			<tr>
				<td></td>
				<td><input id="seachButton" type="button" value="搜索"></td>
			</tr>
		</table>
 		<div class="vise2" style="display:none;">
      <p style="font:12px/1.5 '宋体'; margin-top:20px;">搜索结果<span style="color:#f4905c; padding-left:5px;" class="resultNum"></span></p>
         <ul id="reslutUl" style="padding:8px 0;background:#fff;border:1px solid #dbe5ed;">
         </ul>
		</div>
	<shiro:hasPermission name="visa:instruction:upload">
	<div>
	   <form action="${ctx}/visa/instruction/upload" method="post" enctype="multipart/form-data">
			<table>
				<tr>
					<td style="vertical-align:top;padding:10px 10px 10px 0;font-size:12px;width:70px;">签证国家：</td>
					<td style="vertical-align:bottom;padding:10px 10px 10px 0;font-size:12px;">
						<div id="selectcountry" style="padding-top:3px;"></div></td>
				</tr>

				<tr>
					<td style="padding:10px 10px 10px 0;font-size:12px;width:70px;">签证类型：</td>
					<td style="padding-top:3px;"><c:forEach items="${visaType}" var="vtype">
							<label style="display:inline-block; padding-right:15px;"> <input  style=" vertical-align:top" class='showvisaTypeClass' TYPE="radio"
								value='${vtype.value}' name="radiovisaType"> <span style=" vertical-align: middle" >${vtype.label}</span>
							</label>
						</c:forEach></td>
				</tr>
			</table>
			
			<div style=" padding-left:80px;">
            <input type="file" name="file" /> 
            <input class="visainstructions_button" type="submit" value="上传" style=" font-size:12px;"/>
         </div>
       </form>
	</div>
	</shiro:hasPermission>
		</div>		
</body>
</html>