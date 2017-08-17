/**
 * Created by wanglijun on 2016/10/12.
 */
/**
 * 只需后台传递当前总条数,当前页码、每页条数多少即可，即totalpage
 * @param page
 */

function initPage(){
    this.first = 1;

    this.last = parseInt(this.count / (this.pageSize < 1 ? 20 : this.pageSize) + this.first - 1);

    if (this.count % this.pageSize != 0 || this.last == 0) {
        this.last++;
    }

    if (this.last < this.first) {
        this.last = this.first;
    }

    if (this.pageNo <= 1) {
        this.pageNo = this.first;
        this.firstPage=true;
    }

    if (this.pageNo >= this.last) {
        this.pageNo = this.last;
        this.lastPage=true;
    }

    if (this.pageNo < this.last - 1) {
        this.next = this.pageNo + 1;
    } else {
        this.next = this.last;
    }

    if (this.pageNo > 1) {
        this.prev = this.pageNo - 1;
    } else {
        this.prev = this.first;
    }

    //2
    if (this.pageNo < this.first) {// 如果当前页小于首页
        this.pageNo = this.first;
    }

    if (this.pageNo > this.last) {// 如果当前页大于尾页
        this.pageNo = this.last;
    }
 return this;
};


function doPage(page1){
    var page=initPage.call(page1)
    // page=pageTest;
    var count=page.count;//当前数据量
    var pageNo=page.pageNo;//当前页码
    var pageSize=page.pageSize;
    var count=page.count;
    var first=page.first;
    var last=page.last;
    var prev=page.prev;
    var last=page.last;
    var firstPage=page.firstPage;//是否是首页
    var lastPage=page.lastPage;//是否是尾页
    var next=page.next;
    var length=8;//省略点中间显示页码的个数
    var slider=1;//页码过多的时候，省略点前后显示的页码个数


    var pageHtml="";
    pageHtml+="<ul>";
    pageHtml+="<li><span class=\"total\">共" + count + "条</span></li>";
    if(pageNo == first){//如果当前页是首页
        pageHtml+="<li class=\"disabled\"><span href=\"javascript:\" style=\"padding: 0;border: 0;\"><em class=\"fa fa-angle-left_own \" style=\"margin: 0;\"></em></span></li>";
    }else{
        pageHtml+="<li><span onclick=\"javascript:goPage("+prev+","+pageSize+");\" style=\"padding: 0;border: 0;\"><em class=\"fa fa-angle-left_own \" style=\"margin: 0;\"></em></span></li>";
    }
    //中间显示的页码 显示的开始页
    var begin = pageNo - (length / 2);

    if (begin < first) {
        begin = first;
    }
    //中间显示的页码 显示的结束页码
    var end = begin + length - 1;

    if (end >= last) {
        end = last;
        begin = end - length + 1;
        if (begin < first) {
            begin = first;
        }
    }

    if (begin > first) {
        var i = 0;
        for (i = first; i < first + slider && i < begin; i++) {
            pageHtml+="<li><span onclick=\"javascript:goPage("+i+","+pageSize+");\">"
                + (i + 1 - first) + "</span></li>";
        }
        if (i < begin) {
            pageHtml+="<li class=\"disabled\"><span onclick=\"javascript:\">...</span></li>\n";
        }
    }

    for ( j = begin; j <= end; j++) {
        if (j == pageNo) {
            pageHtml+="<li class=\"active\"><span onclick=\"javascript:\">" + (j + 1 - first)
                + "</span></li>\n";
        } else {
            pageHtml+="<li><span onclick=\"javascript:goPage("+j+","+pageSize+");\">"
                + (j + 1 - first) + "</span></li>\n";
        }
    }

    if (last - end > slider) {
        pageHtml+="<li class=\"disabled\"><span onclick=\"javascript:\">...</span></li>\n";
        end = last - slider;
    }

    for ( k = end + 1; k <= last; k++) {
        pageHtml+="<li><span onclick=\"javascript:goPage("+k+","+pageSize+");\">"
            + (k + 1 - first) + "</span></li>\n";
    }

    if (pageNo == last) {
        pageHtml+="<li class=\"disabled\"><span onclick=\"javascript:\" style=\"padding: 0;border: 0;\"><em class=\"fa fa-angle-right_own \" style=\"margin: 0;\"></em></span></li>";
    } else {
        pageHtml+="<li><span onclick=\"javascript:goPage("+next+","+pageSize+");\" style=\"padding: 0;border: 0;\"><em class=\"fa fa-angle-right_own \" style=\"margin: 0;\"></em></span></li>";
    }

    pageHtml+="<li><span class=\"skip\" href=\"javascript:page(2,10);\">跳至 <input type=\"text\" onkeydown=\"var e=window.event||event||this;var c=e.keyCode||e.which;if(c==13) goPage(this.value,"+pageSize+");\" onkeypress=\"var e=window.event||event||this;var c=e.keyCode||e.which;if(c==13) goPage(this.value,"+pageSize+");\"/>页</span></li>"
        +"<li><span class=\"skip\" href=\"javascript:void(0);\">每页 <input type=\"text\" value=\""+pageSize+"\" onkeydown=\"var e=window.event||event||this;var c=e.keyCode||e.which;if(c==13) goPage("+pageNo+",this.value);\" onkeypress=\"var e=window.event||event||this;var c=e.keyCode||e.which;if(c==13) goPage("+pageNo+",this.value);\" onclick=\"this.select();\" />条</span></li>";

     pageHtml+="</ul>";

    pageHtml+="<div style=\"clear:both;\"></div>";
    return pageHtml;
}
