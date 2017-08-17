$(function(){
 	// 机票计调初始化界面
	addTrafficJs.service.initTraffic();
});


var addTrafficJs = {
		
		service : {
			
			initTraffic:function(){
				
				var trafficLineType = $("#id_trafficLineTypeVal").val();
				$("#inquiry_radio_flights"+trafficLineType).attr('checked', 'checked');
			}
	
		
		}
		
};

