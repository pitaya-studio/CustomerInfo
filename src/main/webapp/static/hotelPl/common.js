//删除行处理
var delButtonTheme = '';
delButtonTheme += '<a class="deleteButton" href="javascript:void(0)">删除</a>';
$(document).ready(function () {
    //主页面所有的删除行操作
    $(document).on("click", ".deleteButton", function () {
		var $delButton = $(this);
    	$.jBox.confirm("确定要删除数据吗？", "系统提示", function(v, h, f) {
			if (v == 'ok') {
		        //删除行所在的上级容器，通过该容器来实现各个table行删除后的不同反应
		        var $datarowmptyBehaviorContainer = $delButton.parents("[datarow-emptyRow-behavior]")
		        var $tbody = $delButton.parents("tbody:first");
		        var $tr = $delButton.parents("tr:first");
		        $tr.remove();
		        $datarowmptyBehaviorContainer.trigger("datarow.delete", $tr);
		        if ($tbody.children().length == 0) {
		            $datarowmptyBehaviorContainer.trigger("datarow.emptyRow");
		        }
			}
		});
    });
});
$(document).on("datarow.emptyRow", "[datarow-emptyRow-behavior]", function () {
    var $this = $(this);
    var behavior = $this.attr("datarow-emptyRow-behavior");
    switch (behavior) {
        case "hide":
            $this.hide();
            break
        case "emptyRow":
            $this.empty();
            $this.trigger("emptied");
            break;
    }
});

//期间处理
/**
 * 验证数组中的所有的开始日期和所有的结束日期是否完全相同
 * @param arrayList 期间数组
 * @returns {boolean} 是否完全相同
 */
function isSamePeriod(arrayList) {
    var isSame = true;
    var arrayCount = arrayList.length;
    if (arrayCount < 2) {
        return true;
    }
    var firstArrayLength = arrayList[0].length;
    for (var i = 1; i < arrayCount; i++) {
        if (firstArrayLength != arrayList[i].length) {
            return false;
        }
    }
    for (var j = 0; j < firstArrayLength; j++) {
        for (var k = 1; k < arrayCount; k++) {
            var arr = arrayList[k];
            var prevArr = arrayList[k - 1];
            if (!isSameValueObject(arr[j], prevArr[j])) {
                return false;
            }
        }
    }
    return isSame;
}

/**
 * 验证2个对象是否相同(只验证1层，且其属性都是值类型)
 * @param obj1 对象1
 * @param obj2 对象2
 * @returns {boolean} 是否相同
 */
function isSameValueObject(obj1, obj2) {
    var isSame = true;
    for (var prop in obj1) {
        if (obj1[prop] != obj2[prop]) {
            return false;
        }
    }
    return isSame;
}

/**
 * 验证期间数组是否存在重叠的期间
 * @param periodList 期间数组
 * @param duplicatedList 重复的信息
 */
function checkReduplicated(periodList, duplicatedList) {
    if (periodList.length == 0) {
        return;
    }
    var period = periodList[0];
    var remainPeriodList = periodList.slice(1, periodList.length);
    for (var i in remainPeriodList) {
        var targetPeriod = remainPeriodList[i];
        //如果存在没有输入的期间，不验证
        if (period.startDate && period.endDate && targetPeriod.startDate && targetPeriod.endDate) {
            if (((period.startDate <= targetPeriod.startDate) && (period.endDate >= targetPeriod.startDate))
                || ((period.startDate <= targetPeriod.endDate) && (period.endDate >= targetPeriod.endDate))
                || ((period.startDate >= targetPeriod.startDate) && (period.endDate <= targetPeriod.endDate))
            ) {
                duplicatedList.push({
                    period: period,
                    targetPeriod: targetPeriod
                });
            }
        }
    }
    checkReduplicated(remainPeriodList, duplicatedList);
}


//数组处理
/**
 * 通过指定属性和属性值 获取数组中的该对象
 * @param arr 对象数组
 * @param propName 属性名
 * @param propValue 属性值
 * @returns {*} 指定属性的对象
 */
function getObjectByProp(arr, propName, propValue) {
    if (!arr || arr.length == 0) {
        return null;
    }
    for (var index in arr) {
        if (arr[index][propName] == propValue) {
            return arr[index];
        }
    }
}
/**
 * 将一维对象数组按照对象指定的属性 分组为相同指定属性在一起的 二维数组
 * @param list 一维数组
 * @param propName 指定的属性
 * @returns {Array} 处理后的二维数组
 */
function groupArrayByProp(list,propName){
    var groupedArray =[];
    var retainList =[];
    for(var i in list){
        var object = list[i];
        var sameValueArray=null;
        for(var j in groupedArray){
            if(groupedArray[j].length>0 && object[propName]==groupedArray[j][0][propName]){
                sameValueArray =groupedArray[j];
                break;
            }
        }
        if(sameValueArray){
            sameValueArray.push(object);
        }else{
            groupedArray.push([object]);
        }
    }
    return groupedArray;
}

//按照顺序插入元素
$.fn.extend({
    /**
     * 在现有的元素集合中按照顺序插入一个新的元素，
     * @param element 新的元素
     * @param parent 元素集合的父级元素
     * @param getSortedValue 获取排序的值，排序的时候会按照该值进行排序
     */
    sortedInsert:function(element,parent,getSortedValue){
        var $elements = $(this);
        $parent = $(parent);
        var $element = $(element);
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
});

//复选框的赋值和取值
$.fn.extend({
    checkedList:function(getValue,list){
        var $this = $(this);
        if(list){
            for(var i =0;i<$this.length;i++){
                var $checkbox = $this.eq(i);
                var value = getValue($checkbox);
                var isChecked =false;
                for(var j in list){
                    if(value==list[j]){
                        isChecked= true;
                    }
                }
                if(isChecked){
                    $checkbox.attr("checked",true);
                }else   {
                    $checkbox.removeAttr("checked");
                }
            }
            return $this;
        }else {
            var list =[];
            for(var i =0;i<$this.length;i++){
                var $checkbox = $this.eq(i);
                if($checkbox.is(':checked')){
                    list.push(getValue($checkbox));
                }
            }
            return list;
        }
    },
   getCheckedTexts:function(getText){
        var $this = $(this);
        var list =[];
        for(var i =0;i<$this.length;i++){
            var $checkbox = $this.eq(i);
            if($checkbox.is(':checked')){
                list.push(getText($checkbox));
            }
        }
        return list.toString();
    }
});

//设置区域的可用状态
function setRegionDisabled(region) {
    var $region = $(region);
    $region.find("input,select,textarea").attr("disabled", "disabled");
    $region.find(".price_sale_house_01,.price_sale_house_02,.deleteButton,button,.handle").addClass("region_disabled");
    $region.data("status", "disabled");
    $("#btnSubmit").show();
}
function setRegionEnable(region) {
    var $region = $(region);
    $region.find("input,select,textarea").removeAttr("disabled");
    $region.find(".price_sale_house_01,.price_sale_house_02,.deleteButton,button,.handle").removeClass("region_disabled");
    $region.data("status", "enable");
    $("#btnSubmit").hide();
}

//trim处理
String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.lTrim = function () {
    return this.replace(/(^\s*)/g, "");
}
String.prototype.rTrim = function () {
    return this.replace(/(\s*$)/g, "");
}

function getTextByAttribute(attribute) {
	if(attribute == null || attribute == undefined) {
		return "";
	} else {
		return attribute;
	}
}

/**  
 * 将数值四舍五入(保留2位小数)后格式化成金额形式  
 *  
 * @param num 数值(Number或者String)  
 * @return 金额格式的字符串,如'1,234,567.45'  
 * @type String  
 */    
function formatCurrency(num) {
	if(num == undefined || num == null || num == '') {
		return '';
	}
    num = num.toString().replace(/\$|\,/g,'');    
    if(isNaN(num))    
    num = "0";    
    sign = (num == (num = Math.abs(num)));    
    num = Math.floor(num*100+0.50000000001);    
    cents = num%100;    
    num = Math.floor(num/100).toString();    
    if(cents<10)    
    cents = "0" + cents;    
    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)    
    num = num.substring(0,num.length-(4*i+3))+','+    
    num.substring(num.length-(4*i+3));    
    return (((sign)?'':'-') + num + '.' + cents);    
} 
