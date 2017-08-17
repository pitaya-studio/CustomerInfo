/**
 * Created by zhh on 2016/2/2.
 * 新增,修改游轮基础信息
 */
//新增舱型
$(document).on('click','[name="cabinType"] em',function(){
    var $this = $(this);
    var $current = $this.parents('div[name="cabinType"]:first');
    var $newCabinType = $current.clone();
    $current.after($newCabinType);
    $newCabinType.find('input').val('');
    $newCabinType.find('i').show();
    $current.find('em').hide();
    var div=$this.parent().parent();
    if(div.attr("bz")!="nodel"){
    	$current.find('i').show();
    }
});
//删除舱型
var deluuids="";
$(document).on('click','[name="cabinType"] i',function(){
    var $this = $(this);
    var $current = $this.parents('div[name="cabinType"]:first');
    var $prev = $current.prev('.mod_information_d2').length>0 ? $current.prev('.mod_information_d2'):$current.next();
    if(!$current.find('em').is(':hidden')){
        $prev.find('em').show();
    }
    if($("#delUuid")!=undefined){
    	deluuid=$current.find("input.cabinTypeUuid").val();
    	if(deluuids==""){
    		deluuids=deluuid;
    	}else{
    		deluuids=deluuids+","+deluuid;
    	}
    	$("#delUuid").val(deluuids);
    }
    var cabinTypeName = $current.find('[name="cabinTypeName"]').val();
    cabinTypeNames.splice($.inArray(cabinTypeName,cabinTypeNames),1);
    $current.remove();
    if($('[name="cabinType"]').length==1){
        $prev.find('i').hide();
    }
});

var cabinTypeNames = [];

$(document).on('blur','[name="cabinTypeName"]',function(){
	cabinTypeNames = [];
    var $this = $(this);
    var cruiseArr = [];//所有的舱型数组
    $this.parents('.add-del-container').find('input').each(function () {
        var cabinTypeName = $(this).val().toString().trim();
        if (cabinTypeName) {
            cruiseArr.push(cabinTypeName);
        }
    });


    //判断数组是否有重复
    var resultObj = {};
    var duplicateYN = false;
    if (cruiseArr.length > 0) {
        for (var i = 0; i < cruiseArr.length; i++) {
            if (!resultObj[cruiseArr[i]]) {
                cabinTypeNames.push(cruiseArr[i]);
                resultObj[cruiseArr[i]] = 1;
            }
            else {
                resultObj[cruiseArr[i]]++;
                $(this).val("");
            }
        }
    }
    for (var item in resultObj) {
        if (resultObj[item] > 1) {
            duplicateYN = true;
        }
    }
    if (duplicateYN) {
        $.jBox.tip('舱型不可重复', 'error');
        return ;
    }
})