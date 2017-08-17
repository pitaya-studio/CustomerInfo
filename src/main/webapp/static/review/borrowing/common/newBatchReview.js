/**
 * 批量审核通用js
 * @param obj
 */

// 全选
	function t_checkall(obj){
		if(obj.checked){
			$("input[name=activityId]").not("input:checked").each(function(){
				this.checked = true;
			});
			$("input[name=allChk]").not("input:checked").each(function(){
				this.checked = true;
			});
		}else{
			$("input[name=activityId]:checked").each(function(){
				this.checked = false;
			});
			$("input[name=allChk]:checked").each(function(){
				this.checked = false;
			});
		}
	}
	// 反选
	function t_checkallNo(obj){
		$("input[name=activityId]").each(function(){
			$(this).attr("checked",!$(this).attr("checked"));
		});
		t_allchk();
	}
	// 如变成全选，则需要勾选全选框
	function t_allchk() {
		var chknum = $("input[name='activityId']").size();
		var chk = 0;
		$("input[name='activityId']").each(function() {
			if ($(this).attr("checked") == true) {
				chk++;
			}
		});
		if (chknum == chk) {//全选 
			$("input[name='allChk']").attr("checked", true);
		} else {//不全选 
			$("input[name='allChk']").attr("checked", false);
		}
	}
	function payedConfirmNew(ctx,orderid,agentid,userLevel,url){
	    var str="";
	    var num = 0; // 审批项目数量
			$('[name=activityId]:checkbox:checked').each(function(){
				str+=$(this).val()+",";
				num++;
			});
			
			if("" == str)
				{
				$.jBox.tip("请选择需要审批的记录！"); 
				return false;
				}
				
		var theurl = "iframe:"+ctx+"/orderPay/returnMoneyConfirm?orderid="+orderid+"&agentid="+agentid+"&chosenNum="+num+"&type=1";
		$.jBox(theurl,{		
			    title: "批量审批",
				width:830,
		   		height: 300,
		   		iframeScrolling:'no', // iframe 不使用滚动条
		   		buttons:{'取消': 2,'驳回':0,'通过':1},
		   		persistent:true,
		   		loaded: function (h) {},
		   		submit: function(v,h,f){
		   			if(v==1 || v==0){
		   			 var remarks= $(h.find("iframe")[0].contentWindow.remarks).val();
		   			 if(remarks.length>200){
		   				$.jBox.tip("字符长度过长，请输入少于200个字符", 'error');
		            	return;
		   			 }
				     dataparam={ 
        		 				  revids:str,
							      remarks:remarks,
							      userLevel:userLevel,
							      result:v
				     };
			           
				      $.ajax({ 
				          type:"POST",
				          url:ctx+url,
				          dataType:"json",
				          data:dataparam,
				          success:function(data){
				        	//  alert(data.msg);
				        	// alert("success");
				               $("#searchForm").submit();
				          }
				      });
		   			}
		   			
		   		}
		});
	}