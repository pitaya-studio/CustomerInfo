/**
 * @module qc.prototype
 * @version 2.0.0
 * @author yongsheng.kuang
 * @description 重写项目的js原生对象
 * @requires basic.js
 */
(function (window, document, undefined) {

    /**
     * @description 让string实例对象能够去除前后空格
     * @function
     * @global
     * @return {String} 去除前后空格的字符串
     */
    if (!String.prototype.trim) {
        String.prototype.trim = function () {
            return this.replace(/^\s+|\s+$/g, '');
        };
    }

    /**
     * @description 从数组中返回指定位置的对象或者值
     * @function
     * @global
     * @param elt {Object|String|Number|Array|Boolean|...} 需要查找的值
     * @return {Number} 数组中第一个满足和查找值相等的索引
     */
    if (!Array.prototype.indexOf) {
        Array.prototype.indexOf = function (elt) {
            var len = this.length ? this.length : 0;
            var from = Number(arguments[1]) || 0;
            from = (from < 0) ? Math.ceil(from) : Math.floor(from);
            if (from < 0) {
                from += len;
            }
            for (; from < len; from++) {
                if (from in this && this[from] === elt) {
                    return from;
                }
            }
            return -1;
        };
    }

    /**
     * @description 删除数组中指定的对象或者值
     * @function
     * @global
     * @param  item {Object|String|Number|Array|Boolean|...} 需要删除的对象或者值
     * @return {Boolean} 是否有对应的对象或者值
     */
    if (!Array.prototype.remove) {
        Array.prototype.remove = function (item) {
            var index = this.indexOf(item);
            if (index === -1) {
                return false;
            }
            for (var i = 0, n = 0; i < this.length; i++) {
                if (this[i] !== this[index]) {
                    this[n++] = this[i];
                }
            }
            this.length -= 1;
            return true;
        };
    }

    /**
     *@description 数组的分页方法
     * @function
     * @global
     * @param rowCount {Number:每页的行数}
     * @param index {Number:第几页} 默认第0页
     */
    if (!Array.prototype.paging) {
        Array.prototype.paging = function (rowCount, index) {
            index = (+index);
            if (!index) {
                index = 0;
            }
            return this.slice(rowCount * index, rowCount * (index + 1));
        };
    }

    /**
     * @description 按照指定的过滤方式筛选出数据
     * @function
     * @global
     * @param fun {Function(item,index,array):Boolean} 筛选function,如果返回值是true,则通过筛选
     * @return {Array} 通过筛选的新数组
     */
    if (!Array.prototype.filter) {
        Array.prototype.filter = function (fun) {
            var t = Object(this);
            var len = t.length ? this.length : 0;
            if (typeof fun !== "function") {
                throw new TypeError();
            }
            var res = [];
            var thisp = arguments[1];
            for (var i = 0; i < len; i++) {
                if (i in t) {
                    var val = t[i];
                    if (fun.call(thisp, val, i, t)) {
                        res.push(val);
                    }
                }
            }
            return res;
        };
    }
})(window, document);