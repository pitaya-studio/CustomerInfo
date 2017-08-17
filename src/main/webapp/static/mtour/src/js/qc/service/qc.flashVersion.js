/**
 * @description 检查用户的flash版本
 */
qc.service('qcFlashVersion', ['$window', function ($window) {
    var i_flash;
    var v_flash;
    // Netscape
    if ($window.document.all) {
        var swf;
        try {
            swf = new $window.ActiveXObject('ShockwaveFlash.ShockwaveFlash');
        } catch (e) {
        }
        if (swf) {
            i_flash = true;
            var VSwf = swf.GetVariable("$version");
            v_flash = VSwf.split(" ")[1];
            v_flash = v_flash.substring(0, v_flash.indexOf(','));
        }
    }
    else {
        for (var i = 0; i < $window.navigator.plugins.length; i++) {
            if ($window.navigator.plugins[i].name.toLowerCase().indexOf("shockwave flash") >= 0) {
                i_flash = true;
                v_flash = $window.navigator.plugins[i].description.substring($window.navigator.plugins[i].description.toLowerCase().lastIndexOf("flash ") + 6, $window.navigator.plugins[i].description.length);
                v_flash = v_flash.substring(0, v_flash.indexOf('.'));
            }
        }
    }
    this.installed = i_flash;
    this.version = v_flash;
}]);