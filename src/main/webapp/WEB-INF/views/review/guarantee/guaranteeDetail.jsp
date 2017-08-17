<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
	    <meta name="decorator" content="wholesaler"/>
		<title>担保变更申请</title>

		<script type="text/javascript">
			$(function(){  
				//input获得失去焦点提示信息显示隐藏  
				inputTips();
				rebates.init();

				//返佣对象的选择 如果为供应商则带出供应商信息---C475--start
				$('#rebateObject').comboboxInquiry();
				//返佣对象类型联动
				$(document).on('change', '.rebateTarget', function () {
					if ($(this).find('option:selected').val() == '2') {//选择返佣供应商
						$('#rebateObject').comboboxInquiry('resetEmpty');
						$('#rebateObject').find("option[value='0']").attr('selected','selected');
						$('.supplyNameSpan').show();
						$('#default').show();
						$('#domesticAccount').hide();
						$('#overseasAccount').hide();
					}
					else {
						$('.supplyNameSpan').hide();
						$('.supplyInfoSpan').hide();
					}
				});
				//返佣对象类型联动，选择境内境外账号联动时
				$(document).on('change', '#accountType', function () {
					if ($(this).find('option:selected').val() == '1') {//选择境内账号
						$('#default').hide();
						$('#domesticAccount').show();
						$('#overseasAccount').hide();
					}
					else if($(this).find('option:selected').val() == '2'){
						$('#default').hide();
						$('#domesticAccount').hide();
						$('#overseasAccount').show();
					}else{
						$('#default').show();
						$('#domesticAccount').hide();
						$('#overseasAccount').hide();
					}
				});
				//返佣对象类型联动，选择开户行时关联账号号码
				$(document).on('change', 'select[name="bankName"]', function () {
					var bankAccountCode=$(this).find("option:selected").attr("bankAccountCode");
					if(bankAccountCode == undefined){
						$(this).parent().find("select[name='bankAccount']").empty();
						$(this).parent().find("select[name='bankAccount']").append("<option value='0' selected='selected'>请选择</option>");
					}else{
						/*$(this).parent().find("select[name='bankAccount']").text(bankAccountCode);*/
						$(this).parent().find("select[name='bankAccount']").empty();
						$(this).parent().find("select[name='bankAccount']").append("<option value='"+bankAccountCode+"' selected='selected'>"+bankAccountCode+"</option>");
					}

				});
				//返佣供应商联动,选择供应商名称时
				$(document).on('comboboxinquiryselect', '.supplyName', function () {
					var selectedSupply = $(this).find('option:selected').val();
					if (selectedSupply != '0') {//有供应商选择
						var url = "${ctx}"+"/rebatesupplier/manager/bankinfo/"+selectedSupply;
						$.ajax({
							type: "POST",
							url: url,
							dataType:"json",
							success: function(data){
								//渲染境内账号
								$("#domesticAccount select[name='bankName']").empty();
								var optionStr="<option value='0'>请选择</option>";
								$("#domesticAccount select[name='bankName']").append(optionStr);
								if(data.domestic.length>0){
									$.each(data.domestic,function(i, n){
										var optionStr = "<option value='"+n.id+"' bankAccountCode='"+ n.bankAccountCode+"'>"+n.bankName+"</option>";
										$("#domesticAccount select[name='bankName']").append(optionStr);
									});
								}

								//渲染境外账号
								$("#overseasAccount select[name='bankName']").empty();
								var optionStr="<option value='0'>请选择</option>";
								$("#overseasAccount select[name='bankName']").append(optionStr);
								if(data.overseas.length>0){
									$.each(data.overseas,function(i, n){
										var optionStr = "<option value='"+n.id+"' bankAccountCode='"+ n.bankAccountCode+"'>"+n.bankName+"</option>";
										$("#overseasAccount select[name='bankName']").append(optionStr);
									});
								}
							}
						});
						$('.supplyInfoSpan').show();
						$('#default').show();
						$('#domesticAccount').hide();
						$('#overseasAccount').hide();
						//清除之前的选项
						$("#accountType").find("option[value='0']").attr("selected","selected");
						$("select[name='bankName']").find("option[value='0']").attr("selected","selected");
						$("select[name='bankAccount']").find("option[value='0']").attr("selected","selected");
						/*$("label[name='bankAccount']").text("");*/
					}
					else {
						$('.supplyInfoSpan').hide();
					}
				});
				//返佣对象的选择 如果为供应商则带出供应商信息---C475--end
			});



		</script>
	</head>
<body>
	<!--66 start-->
	<div class="bgMainRight">
		<div class="mod_nav">订单 > 销售签证订单 > 押金转担保记录 > 担保变更详情</div>
		<div class="ydbzbox fs">
			<div class="ydbz_tit">订单详情</div>
			<ul class="ydbz_info ydbz_infoli25">
				<li>
					<span>销售：</span>
					白静
				</li>
				<li>
					<span>下单时间：</span>
					2015-07-31 03:18:26
				</li>
				<li>
					<span>团队类型：</span>
					单办签
				</li>
				<li>
					<span>收客人：</span>
					白静
				</li>
				<li>
					<span>订单号：</span>
					BJH150731021
				</li>
				<li style="width: 51%;">
					<span>订单团号：</span>
					15QZ-07
				</li>
				<li>
					<span>订单金额：</span>
					¥1,100.00
				</li>
				<li>
					<span>操作人：</span>
					夏炳翠
				</li>
				<li>
					<span>办签人数：</span>
					1人
				</li>
				<li>
					<span>下单人：</span>
					白静
				</li>
			</ul>
			<div class="ydbz_tit">产品信息</div>
			<div class="orderdetails2">
				<p class="ydbz_mc">美签 个旅</p>
				<ul class="ydbz_info">
					<li>
						<span>产品编号：</span>
						BJHQXGJLXSYXZRGS1337
					</li>
					<li>
						<span>签证国家：</span>
						美国
					</li>
					<li>
						<span>签证类别：</span>
						个签
					</li>
					<li>
						<span>领区：</span>
						北京
					</li>
					<li>
						<span>应收价格：</span>
						¥1,100.00人
					</li>
					<li>
						<span>创建时间：</span>
						2015-02-11 15:00:01
					</li>
				</ul>
			</div>
			<div class="ydbz_tit">游客担保变更</div>

			<table id="contentTable" class="activitylist_bodyer_table" style="margin:10px;width: 99%;">
				<thead>
				<tr>
					<th width="5%">游客</th>
					<th width="5%">原担保类型</th>
					<th width="7%">原押金金额</th>
					<!-- <th width="10%">下单时间</th> -->
					<th width="10%">申请担保类型</th>
					<th width="10%">申请交押金金额</th>
					<th width="10%">备注</th>

				</tr>
				</thead>
				<tbody>
					<tr>
						<td>AAA</td>
						<td class=" tc fbold">押金</td>
						<td class=" tc fbold">￥2,000.00</td>
						<td class="tc">担保</td>
						<td>
							￥200.00
						</td>
						<td class="tc">
							xxxxxx

						</td>
					</tr>
					<tr>
						<td>AAA</td>
						<td class=" tc fbold">押金</td>
						<td class=" tc fbold">￥2,000.00</td>
						<td class="tc">
							押金+担保
						</td>
						<td>
							￥100.00
						</td>
						<td class="tc">
							xxxxxxx
						</td>
					</tr>
				</tbody>
			</table>

			<div class="mod_information">
				<div class="mod_information_d">
					<div class="ydbz_tit">审核动态</div>
				</div>
			</div>

			<div class="ydbz_sxb" id="secondDiv">
				<div class="ydBtn ydBtn2">

					<a class="ydbz_x" onClick="">返回</a>

				</div>
			</div>
		</div>
	</div>
	<!--66 END-->
</body>
</html>