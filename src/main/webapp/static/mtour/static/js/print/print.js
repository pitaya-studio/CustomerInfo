function pagesetup_null() {
    try {
        var RegWsh = new ActiveXObject("WScript.Shell");
        hkey_key = "header";
        RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "");
        hkey_key = "footer";
        RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "");
        hkey_key = "margin_left";
        RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.0");
        hkey_key = "margin_right";
        RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.0");
        hkey_key = "margin_top";
        RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.0");
        hkey_key = "margin_bottom";
        RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.0");
    } catch (e) {}
}
function pagesetup_default() {
    try {
        var RegWsh = new ActiveXObject("WScript.Shell");
        hkey_key = "header";
        RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "&w&b页码，&p/&P");
        hkey_key = "footer";
        RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "&u&b&d");
        hkey_key = "margin_left";
        RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.750000");
        hkey_key = "margin_right";
        RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.750000");
        hkey_key = "margin_top";
        RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.750000");
        hkey_key = "margin_bottom";
        RegWsh.RegWrite(hkey_root + hkey_path + hkey_key, "0.750000");
    } catch (e) {}
}
function printPage($el) {
    pagesetup_null();
    if ($el) {
        var $sea = $('body').children();
        $sea.hide();
        var $new=$el.clone();
        $new.show();
        $('body').append($new);
        window.print();
        $new.remove();
        $sea.show();
    } else {
        window.print();
    }
    pagesetup_default();
}
function GetUrlParams() {
    var args=new Object();
    var query=location.search.substring(1);//获取查询串
    var pairs=query.split("&");//在逗号处断开
    for(var   i=0;i<pairs.length;i++) {
        var pos=pairs[i].indexOf('=');//查找name=value
        if(pos==-1)   continue;//如果没有找到就跳过
        var argname=pairs[i].substring(0,pos);//提取name
        var value=pairs[i].substring(pos+1);//提取value
        args[argname]=decodeURIComponent(value);//存为属性
    }
    return args;
}

$(document).on('click', '#btnPrint', function () {
    printPage($('#printDiv'));
});
