/**
 * Created by zhh on 2016/2/3.
 * 库存添加页
 */
//已选船期
var cruiseshipDates = [];
//舱型信息
var cabins = [];
$(function(){
    $('#cruiseDate').datepicker({
        dateFormat:'yy-mm-dd',
        onSelect:function(){
            var $cruiseDate = $('#cruiseDate');
            if($('#cruise').val()=='-1'){
                $cruiseDate.val('');
                $.jBox.tip('请选择游轮','info');
                return ;
            }
            var currentDate = $cruiseDate.val();
            if(cruiseshipDates){
	            for(var i= 0,len=cruiseshipDates.length;i<len;i++){
	                if(cruiseshipDates[i]==currentDate){
	                    $cruiseDate.val('');
	                    $.jBox.tip('已存在该团期','info');
	                    return ;
	                }
	            }
            }else{
            	cruiseshipDates = [];
            }
            cruiseshipDates.push(currentDate);
            var uuid = guid();
            var $div = $('<div></div>',{
                id:uuid
            }).attr('date',currentDate);
            createTable($div,uuid);
            //备注和上传文件
            $div.append($('#otherInfo').clone().removeClass('display-none').prop('id',''));
            var $li = $('<li></li>',{
                class:'pr active'
            }).attr('data-for',uuid).text(currentDate);
            $('<i>x</i>').appendTo($li);
            sortInsert($li,$("#selectedDates ul"),function($el){
                return new Date($el.text()?$el.text().replace(/-/g,   "/"):'1970/01/01');
            });
            tabScroll($("#selectedDates"), "[data-for='" + uuid + "']",$div);
        }
    });
})
//组织参数
function getPostData(obj){
    var postData = {
        requestType:"mtour data",
        param:base64encode(JSON.stringify(obj))
    }
    return postData;
}
//
function guid() {
    return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
};
//
function S4() {
    return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
};
//
$(document).on('change','#cruise',function(){
    var $this = $(this);
    //清除内容
    $('#selectedDates ul').children().remove();
    $('#cabinInfo').children().remove();
    $('#cruiseDate').val('');
    var param = {
        cruiseshipInfoUuid:$this.val()
    }
    var postData = getPostData(param);
    $.ajax({
        url:$ctx+"/cruiseshipStock/getCabinAndShipDate",
        type:'post',
        cache:false,
        async:false,
        data:postData,
        success:function(data){
        	var data = eval('(' + data + ')'); 
	    	/*if(data.data.cruiseshipDate){
	             cruiseshipDates = data.data.cruiseshipDate;
	        }*/
        	cruiseshipDates = data.data.cruiseshipDate;
	        if(data.data.cruiseshipCabin){
	             cabins = data.data.cruiseshipCabin;
	        }
        },
        error:function(data){
        }
    })
});

/**
 *
 * @param uuid 船期库存的id
 */
function createTable($parent,uuid){
    var $table = $('<table></table>',{
        class:"table table-striped table-bordered",
        name:"stockInfoList"
    }).appendTo($parent);
    var headHtml = '<thead>' +
        ' <th width="10%">序号</th>' +
        ' <th width="10%">舱型</th>' +
        ' <th>库存总数</th>' +
        ' <th>余位</th>' +
        ' </thead>'
    $table.append(headHtml);
    var $tbody = $('<tbody></tbody>').appendTo($table);
    for(var i= 0,len=cabins.length;i<len;i++){
        var $tr = $('<tr></tr>',{
            uuid:cabins[i].cruiseshipCabinUuid
        }).appendTo($tbody);
        ///生成序号
        createTd($tr,i+1);
        //生成舱型
        createTd($tr,cabins[i].cruiseshipCabinName);
        //生成库存
        createTd($tr,$('<input />',{
                name:'reserve',
                value:0
            }).attr('data-type','number')
        );
        //生成余位
        createTd($tr,$('<input />',{
                name:'residue',
                value:0,
                readOnly:'readOnly'
            })
        );
    }
};
//创建单元格
function createTd($tr,content){
    $('<td></td>').append(content).appendTo($tr);
}
//鼠标移出库存框
$(document).on('blur','[name="reserve"]',function(){
    var $this = $(this);
    $this.val(+$this.val())
    $this.parents('tr:first').find('[name="residue"]').val($this.val());
});
//按顺序插入
function  sortInsert($element,$parent,getSortedValue){
    var $elements = $parent.children();
    var originLength = $elements.length;
    if (originLength == 0) {
        $parent.append($element);
    } else {
        var isInserted = false;
        for (var i = 0; i < originLength; i++) {
            if (getSortedValue($element) < getSortedValue($elements.eq(i))) {
                $elements.eq(i).before($element);
                isInserted = true;
                break;
            }
        }
        if (!isInserted) {
            $elements.last().after($element);
        }
    }
}
//保存
$(document).on('click','#save',function(){
	//按钮禁用
	$(obj).attr("disabled", "disabled");
    //获取填写的数据
    var datas = [];
    var cruiseshipInfoUuid = $('#cruise').val();
    $('#cabinInfo').children('div').each(function(){
        var $this = $(this);
        var data = {};
        var shipDate = $this.attr('date');
        data.cruiseshipInfoUuid = cruiseshipInfoUuid;
        data.shipDate = shipDate;
        data.memo = $this.find('div[class="add-remarks"] textarea').val();
        //暂无获取日期和上传资料的方法
        data.attachment = [];
        $this.find('input[name="docOriName"]').each(function(){
        	var $that = $(this);
            var attachmentInfo = {
        		docId:$that.prev().val(),
        		docPath:$that.next().val(),
        		docName:$that.val()
            };
            data.attachment.push(attachmentInfo);
         });
        //舱型
        data.stockInfoList = [];
        $this.find('[name="stockInfoList"] tbody tr').each(function(){
            var stockAmount = +$(this).find('[name="reserve"]').val();
            var stockInfo = {
                shipDate:shipDate,
                cruiseshipInfoUuid:cruiseshipInfoUuid,
                cruiseshipCabinUuid:$(this).attr("uuid"),
                stockAmount:stockAmount,
                freePosition:stockAmount
            };
            data.stockInfoList.push(stockInfo);
        });
        datas.push(data);
    });
    var token=$("#token").val();
    $.ajax({
        url:$ctx+"/cruiseshipStock/saveCruiseshipStock?token="+token,
        type:'post',
        cache:false,
        async:false,
        data:getPostData(datas),
        success:function(data){
        	if(data=="1"){
        		$.jBox.tip("添加成功!");
        		setTimeout(function(){window.close();},900);
        		window.opener.location.reload();
        	}else if(data=="0"){
        		$.jBox.tip('系统异常，请重新操作!','warning');
				$(obj).attr("disabled", false);
        	}
        }
    });
});
//删除
$(document).on('click','#selectedDates li i',function(){
    var $this = $(this);
    var $li = $this.parent();
    var dateText = $li.text().substr(0,$li.text().length-1);
    var index = $li.attr('data-index');
    $li.remove();
    $('#cabinInfo').children('div').eq(index).remove();

    //重排index
    $('#selectedDates li').each(function(index){
        $(this).attr('data-index',index);

    });
    cruiseshipDates.remove(dateText);
    $('#selectedDates li').eq($('#selectedDates li').length-1).addClass('active');
    $('#cabinInfo div').eq($('#selectedDates li').length-1).show();
})
