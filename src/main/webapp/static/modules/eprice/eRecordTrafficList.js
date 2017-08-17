$(function() {
	eplist4saler.ajax.getProjectList();
	// 排序方式 0 倒序 1正序
	$("#sort-list-ul-id li[sortc]")
			.click(
					function() {
						var sortc = $(this).attr("sortc");
						var sortv = $(this).attr("sortv");

						// 正倒序排序图标和排序值更新
						$("#sort-list-ul-id i").hide();
						$(this).find("i").removeClass(
								"icon-arrow-down icon-arrow-up");
						$("#sort-list-ul-id li.activitylist_paixu_moren")
								.removeClass("activitylist_paixu_moren");
						$(this).remove("activitylist_paixu_left_biankuang")
								.addClass("activitylist_paixu_moren");
						if (sortv == 1) {
							$(this).attr("sortv", 0);
							$(this).find("i").addClass("icon-arrow-down");
						} else {
							$(this).attr("sortv", 1);
							$(this).find("i").addClass("icon-arrow-up");
						}
						$(this).find("i").show();
						sortv = sortc + "-" + sortv;

						// 多字段排序使用，保证每次更新对应字段的排序值
						// var input = $("#eprice-search-form-id
						// input[name=orderColumn][sortc="+sortc+"]");

						// 单字段排序，保证排序表单元素唯一性
						var input = $("#eprice-search-form-id input[name=orderColumn]");
						if ($(input).length < 1) {
							input = $('<input type="hidden" name="orderColumn" sortc="'
									+ sortc + '"/>');
							$("#eprice-search-form-id").append(input);
						}

						$(input).val(sortv);

						eplist4saler.ajax.getProjectList();
					});

	$("#eprice-search-form-btn-id").click(function() {
		eplist4saler.ajax.getProjectList();
	});
});

var eplist4saler = {
	dao : {

		/**
		 * 生成询价项目列表元素——询价项目 html string
		 * 
		 * @author lihua.xu
		 * @时间 2014年9月25日
		 * @param {}
		 *            m 询价项目对象
		 * @return {html string}
		 */
		project : function(m) {
			var html = new Array();
//			if(m.estimateStatus==1){
//				html.push('<tr pid="' + m.id + '"><td class="inquiry_new">' + m.id
//						+ '<br><br></td><td>' + m.lastBaseInfo.salerName
//						+ '</td><td>' + m.lastBaseInfo.customerName + '</td>');
//			}else{
				html.push('<tr pid="' + m.id + '"><td>' + m.id
						+ '<br><br></td><td>' + m.lastBaseInfo.salerName
						+ '</td><td>' + m.lastBaseInfo.customerName + '</td>');
//			}
			// 按角色控制客户联系人的可见性
			if ($("#agentinfo_visibility") && $("#agentinfo_visibility").val() == 1) {				
				html.push('<td>' + m.lastBaseInfo.contactPerson + '</td>');
			}
			
			if (m.type == 1) {
				html.push('<td>单团</td>');
			} else if (m.type == 3) {
				html.push('<td>游学</td>');
			} else if (m.type == 4) {
				html.push('<td>大客户</td>');
			}else if (m.type == 5) {
				html.push('<td>自由行</td>');
			}else if (m.type == 7) {
				html.push('<td>机票</td>');
			}else {
				html.push('<td>未知	</td>');
			}
			html.push('<td class="tc">' + $.trekizFormatDate({
				time : m.lastEstimatePriceTime,
				pattern : "yyyy-MM-dd"
			}) + '</td>');
			if (m.lastOperatorGivenTime != null) {
				html.push('<td class="tc">' + $.trekizFormatDate({
					time : m.lastOperatorGivenTime,
					pattern : "yyyy-MM-dd"
				}) + '</td>');
			} else {
				html.push('<td class="tc"><span class="tdred">待报价</span></td>');
			}

			html.push('<td class="tr">' + m.estimatePriceSum + '</td>');

			var namestr;
			if (m.estimateStatus == 2) {
				namestr = '已报价';
			} else if (m.estimateStatus == 3) {
				namestr = '已确定报价';
			} else if (m.estimateStatus == 1) {
				namestr = '待报价';
			} else if (m.estimateStatus == 0) {
				namestr = '已取消';
			} else if (m.estimateStatus == 4) {
				namestr = '已发产品';
			} else if (m.estimateStatus == 5) {
				namestr = '已生成订单';
			} else if (m.estimateStatus == 6) {
				namestr = '待分配';
			} else {
				namestr = '已报价';
			}

			html.push('<td >' + namestr + '</td>');
			html.push('<td class="tc"><a  class="team_a_click" name="expand-or-packup" href="javascript:void(0)">展开</a></tr>')
			html.push('<tr class="activity_team_top1" style="display:none" id="child1"><td class="team_top" colspan="10">');
			html.push('<table style="margin:0 auto;" class="table activitylist_bodyer_table" id="teamTable"><thead><tr>');
			html.push('<th width="4%">序号</th><th width="9%">询价日期</th><th width="9%">报价日期</th>');
			html.push('<th width="15%">出发城市/抵达城市</th>');
			 html.push('<th width="10%">机票报价</th>');
			html.push('<th width="10%">状态</th><th width="7%">操作</th></tr></thead></table>');
			html.push('<div class="table_activity_scroll"><table name="eprice-record-table" class="table activitylist_bodyer_table table-mod2-group"><tbody>');
			html.push('<tr style="display:none;"><td colspan="12" class="tc"><a href="'
							+ contextPath
							+ '/eprice/manager/project/erlist/'
							+ m.id
							+ '">点击更多询价记录</a></td></tr></tbody></table></div></td> </tr>');
			return html.join("");
		},

		/**
		 * 生成询价项目下属巡检记录列表元素——询价记录 html string
		 * 
		 * @author lihua.xu
		 * @时间 2014年9月25日
		 * @param {}
		 *            m 询价记录对象
		 * @return {html string}
		 */
		eprecord : function(m,i,pstatus) {
			var html = new Array();
			html.push('<tr><td width="4%">'+i+'</td><td width="9%" class="tc">' + $.trekizFormatDate({
						time : m.recordDate,
						pattern : "yyyy-MM-dd"
					}) + '</td>');
			var namestr = "";

			if (m.status <= 1) {
				namestr = '<span class="tdred">待报价</span>';
			} else {
				namestr = m.replyDate;
				namestr = $.trekizFormatDate({
					time : namestr,
					pattern : "yyyy-MM-dd"
				});
			}
			html.push('<td width="9%" class="tc">' + namestr + '</td>');// 报价时间
			if(m.city!=null){
				html.push('<td width="15%" class="tc">'+m.city+'</td>');	// 出发/抵达
			}else{
				html.push('<td width="15%" class="tc">待确定</td>');	// 出发/抵达
			}
			/** 2015 新增多币种统计总价 */
			if(m.replyPrice!=null){
				html.push('<td width="10%" class="tr Inquiry_c">￥'
						+ milliFormat(m.replyPrice,2) + '</td>');// 计调报价——成本报价
			}else{
				if(m.priceDetail!=null){
					html.push('<td width="10%" class="tr Inquiry_c" title='+m.priceDetail+'>已报价</td>');// 计调报价——成本报价
				}else{
					html.push('<td width="10%" class="tr"><span class="tdred">待报价</span></td>');
				}
			}
			var namestr;
			if(pstatus==0){
				namestr = '已取消';
			}else{
				if (m.status == 2) {
					namestr = '已报价';
				} else if (m.status == 3) {
					namestr = '已确定报价';
				} else if (m.status == 1) {
					namestr = '待报价';
				} else if (m.status == 0) {
					namestr = '已取消';
				} else if (m.status == 4) {
					namestr = '已发产品';
				} else if (m.status == 5) {
					namestr = '已生成订单';
				} else if (m.estimateStatus == 6) {
					namestr = '待分配';
				} else {
					namestr = '已报价';
				}
			}
		
			html.push('<td width="10%" class="tr">'+namestr+'</span></td>');
			html.push('<td class="p0" width="7%"><dl class="handle"><dt><img src="'+contextStaticPath+'/images/handle_cz.png" title="操作"/></dt><dd ><p><span></span>');
			
			if(pstatus==0){
				html.push('<a target="_blank" href="' + contextPath 
						+ '/eprice/manager/project/ePriceInfoReadOnly/' + m.rid + '/2'
						+ '">详情</a>');
			}else{
				if (m.status == 1  ) {
					html.push('<a target="_blank" href="' + contextPath
							+ '/eprice/manager/project/ePriceInfoReadOnly/' + m.rid + '/2'
							+ '">详情</a><br/><a  href="' + contextPath
							+ '/eprice/manager/project/replaytop/' + m.rid
							+ '">报价</a>');
				} else if (m.status == 3) {
					html.push('<a target="_blank" href="' + contextPath
							+ '/eprice/manager/project/ePriceInfoReadOnly/' + m.rid + '/2'
							+ '">详情</a><br/><a  href="' + contextPath
							+ '/airTicket/form.htm?recordId='+m.rid  
							+ '">发布产品</a><br/>');
				} else if( m.status == 2 || m.status == 4||m.status == 5){
					html.push('<a target="_blank" href="' + contextPath
							+ '/eprice/manager/project/ePriceInfoReadOnly/' + m.rid + '/2'
							+ '">详情</a>');
				} 
			}
			
			html.push('</p></dd></dl></td></tr>');

			return html.join("");

		}
	},

	service : {

		/**
		 * 生成询价项目列表元素——询价项目 jquery dhtml
		 * 
		 * @author lihua.xu
		 * @时间 2014年9月25日
		 * @param {}
		 *            m 询价项目对象
		 * @return {jquery dhtml}
		 */
		project : function(m) {
			var $html = $(eplist4saler.dao.project(m));

			$($html).find("a[name=expand-or-packup]").click(function() {
				var h = $(this).html();
				var ptr = $(this).parents("tr[pid]");
				var ntr = $(ptr).next("tr.activity_team_top1");
				if (h == "展开") {
					$(this).html("收起");
					$(ntr).show();

					var pid = $(ptr).attr("pid");
					$eprd = $(ntr).find("table[name=eprice-record-table]");

					eplist4saler.ajax.getEprecordList($eprd, pid);
				} else {
					$(this).html("展开");
					$(ntr).hide();
				}
			});

			return $html;
		},

		/**
		 * 生成询价项目下属的询价记录列表元素——询价记录 jquery dhtml
		 * 
		 * @author lihua.xu
		 * @时间 2014年9月26日
		 * @param {}
		 *            m 询价记录对象
		 * @return {jquery dhtml}
		 */
		eprecord : function(m,i,pstatus) {
			var $html = $(eplist4saler.dao.eprecord(m,i,pstatus));

			var d = $($html).find("div.inquiry_mouse");
			if (d.length > 0) {
				$(d).parent().hover(function() {
					$(this).find("div.inquiry_mouse").show();
				}, function() {
					$(this).find("div.inquiry_mouse").hide();
				});
			}

			$($html).find('.handle').hover(function() {
				if (0 != $(this).find('a').length) {
					$(this).addClass('handle-on');
					$(this).find('dd').addClass('block');
				}
			}, function() {
				$(this).removeClass('handle-on');
				$(this).find('dd').removeClass('block');
			});

			return $html;
		},

		/**
		 * 获取列表数据成功后，初始化列表
		 * 
		 * @author lihua.xu
		 * @时间 2014年9月25日
		 * @param {}
		 *            json
		 */
		getProjectListSuccess : function(json) {
			var list = json.page.result;
			var $p = $("#eprice-project-list-id>tbody");
			$($p).children().remove();
			for (var i = 0, len = list.length; i < len; i++) {
				$p.append(eplist4saler.service.project(list[i]));
			}

			$("#eprice-list-count-id").html(json.page.totalCount);
			$("#eprice-project-list-page-id").pageDom({
				eventFun : eplist4saler.ajax.getProjectList,
				count : json.page.totalCount,
				pageNo : json.page.pageNo,
				pageSize : json.page.pageSize
			});// eplist4saler.ajax.getProjectList,json.page.totalCount,json.page.pageNo,json.page.pageSize);
		},

		/**
		 * 获取列表数据成功后，初始化列表
		 * 
		 * @author lihua.xu
		 * @时间 2014年9月25日
		 * @param {}
		 *            json
		 */
		getEprecordListSuccess : function(json) {
			var list = json.page.result;
			var pstatus = json.project.estimateStatus;
			var $p = json.dom;
			$($p).children().remove();
			for (var i = 0, len = list.length; i < len; i++) {
				$p.append(eplist4saler.service.eprecord(list[i],i+1,pstatus));
			}

			if (json.page.pageCount > 1) {
				$($p).append(
						'<tr><td colspan="12" class="tc"><a href="'
								+ contextPath
								+ '/eprice/manager/project/erlist/' + m.id
								+ '">点击更多询价记录</a></td></tr>');
			}
		}

	},

	ajax : {

		getProjectList : function(pn, ps) {
			pn = pn || 1;
			ps = ps || 10;
			var the_param = $("#eprice-search-form-id").serialize();

			ajaxStart();
			$.ajax({
				type : "POST",
				url : contextPath
						+ "/eprice/manager/ajax/project/plisttrafficback?pageSize=" + ps +"&pageNo="
						+ pn,
				data : the_param,
				dataType : "text",
				success : function(html) {
					ajaxStop();
					var json;
					try {
						json = $.parseJSON(html);
					} catch (e) {
						json = {
							res : "error"
						};
					}

					if (json.res == "success") {
						eplist4saler.service
								.getProjectListSuccess(json);
					} else if (json.res == "data_error") {
						jBox.tip("输入数据不正确", 'error');
					} else {
						jBox.tip("系统错误", 'error');
					}
				}

			});
		},

		/**
		 * 获取指定询价项目id下属的询价记录分页列表
		 * 
		 * @author lihua.xu
		 * @时间 2014年9月30日
		 * @param {}
		 *            dom
		 * @param {}
		 *            pid
		 * @param {}
		 *            pn
		 */
		getEprecordList : function(dom, pid, pn) {
			pn = pn || 1;

			$.ajax({
				type : "POST",
				url : contextPath + "/eprice/manager/ajax/project/rtrafficlist/" + pid
						+ "?pageSize=10&pageNo=" + pn,
				data : "",
				dataType : "text",
				success : function(html) {
					var json;
					try {
						json = $.parseJSON(html);
					} catch (e) {
						json = {
							res : "error"
						};
					}

					if (json.res == "success") {
						json.dom = dom;
						eplist4saler.service.getEprecordListSuccess(json);
					} else if (json.res == "data_error") {
						jBox.tip("输入数据不正确", 'error');
					} else {
						jBox.tip("系统错误", 'error');
					}
				}

			});
		}
	}
};