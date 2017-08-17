
(function($) {
	//把返回的json数据转换为html   table
	$.fn.JsonToHtmlHanlder = function(msg,includeTitle) {
		if(includeTitle==undefined){
			includeTitle = true;
		}
		var htmlStr;
		var $tableTile;
		var isAble = false;
		var $table = $('<table class="table"></table>');
		
		 $.each(msg.list,function(k,v){//k为序号 v为每行的值
		    //kin为表头数据
		    var $titleTr = $('<tr class="titleTr"></tr>');
		    if(!isAble){
		    	$titleTr.append($('<th>序号</th>'));
		    	if(includeTitle){
    		    	$titleTr.appendTo($table);
                }
		    }
		    var $tr = $('<tr class="tr"></tr>');
		    //行号
		    $tr.append( $('<td class="row">'+(parseInt(k)+1)+'</td>'));
		 	$tr.appendTo($table);
		 	$.each(v,function(kin,vin){
    		    if(!isAble){
    		    	$titleTr.append($('<th>'+kin+'</th>'));
    		    }
		 		$tr.append($('<td class="row">'+vin+'</td>'));
		 	});
		 	isAble = true;
          });
		return $table;
	};
})(jQuery);
