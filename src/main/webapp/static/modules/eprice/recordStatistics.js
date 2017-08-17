$(function(){
	recordStatistics.service.colspanCount();
	$("td").each(function(){
		var con = $(this).text();
		if(!isNaN(con) && con!="0"){
			$(this).css("color","green");
		}
	});
});

var recordStatistics = {
		service : {
			
			// 计算竖列总计值
			colspanCount : function(){
				// 遍历全部统计表格
				$("table[name='tableStatistics']").each(function(){
					var colsNum = parseInt($(this).find("th.tablehead").length)-1;
					
					// 根据线路国家数量判断是否存在行数 ( 暂时默认线路国家不为空)
					if(true){
						// 获取行数
						var rowsNum = 	parseInt($(this).find("td.stat0").length);
						var allcount = 0;
						// 统计列值集合
						for(var n=0;n<colsNum;n++){	// 遍历列数
							var cou = 0;
							// 累加列的值
							for(var m=0;m<rowsNum;m++){	// 遍历行数
								cou += parseInt($(this).find("td[class=stat"+n+"]:eq("+m+")").text());
							}
							// 将累加值放入指定总计框
							$(this).find("td[class=count"+n+"]").text(cou);
							allcount += parseInt($(this).find("td[class=count"+n+"]").text());
						}
						// 统计整个表格内的累计值
						$(this).find("td[class='all']").text(allcount);
					}
				});
			}
		},
		ajax : {
			// 改变搜索部门分组
			changeDeptId : function(){
				var departmentId = $("#deptSelect").val();
				$("#departmentId").val(departmentId);
				if(departmentId == "") {
					departmentId = $("#tempDepartmentId").val();
				}
				var isParentsAndChildren = $("#isParentsAndChildren").val();
				$.ajax({
			        type: "POST",
			        url: "${ctx}/orderStatistics/manage/getSalerByDept?dom=" + Math.random(),
			        data: {
			        	departmentId : departmentId,
			        	isParentsAndChildren : isParentsAndChildren
			        },
			        success: function(msg) {
			            if(msg == "") {
			            	$(".activitylist_bodyer_right_team_co3").hide();
			            	$("#salerId").val("");
			            } else {
			            	var salerArr = eval(msg);
			            	$(".activitylist_bodyer_right_team_co3").show();
			            	$("#salerId").empty();
			            	
			            	var deptId = $("#departmentId").val();
			            	var html = "<option value='" + deptId + "'>全部</option>";
			        		$("#salerId").append(html);
			            	$.each(salerArr, function(key,value) {
			            		var html = "<option value='" + value.salerId + "'>" + value.salerName + "</option>";
			            		$("#salerId").append(html);
			            	});
			            }
			        }
			    });
			}
		}

};