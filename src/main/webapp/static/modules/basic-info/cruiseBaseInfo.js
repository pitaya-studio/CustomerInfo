/*
 * Created by zhh on 2016/2/2.
 * 游轮基础信息维护列表
 *
 */

//全选
$(document).on('change', '[name="checkAll"]', function () {
        $('#contentTable').find('input[type="checkbox"]').prop('checked', $(this).prop('checked'));
});
//勾选复选框时判断全选状态
$(document).on('change','[name="cruiseCheck"]',function(){
    var $this = $(this);
    var $table = $this.parents('table:first');
    $('input[name="checkAll"]').prop('checked',!$table.find('input[name="cruiseCheck"]:not(:checked)').length>0);
});