    
    jQuery(function($){
        
        TrekizUtils = {
            showTips : {
                /**
                 * 顶端提示显示  
                 * @param {} msg 提示信息具体内容
                 */
                info : function(msg){
                    this._doShow(msg,"notification ui-state-default ui-corner-bottom","ui-icon ui-icon-info")
                },
                error : function(msg){
                    this._doShow(msg,"notification ui-state-error ui-corner-all","ui-icon ui-icon-alert")
                },
                success : function(msg){
                    this._doShow(msg,"notification ui-state-highlight ui-corner-all","ui-icon ui-icon-check")
                },
                _doShow : function(msg,divClass,iconClass){
                    return $("<div>" ).append($("<p/>").text(msg).append($("<span style=\"float: left;\"/>").addClass(iconClass)))
                        .appendTo(document.body )
                        .addClass(divClass)
                        .position({
                            my: "center top",
                            at: "center top",
                            of: window
                        })
                        .show({
                            effect: "blind"
                        })
                        .delay(1500)
                        .hide({
                            effect: "blind",
                            duration: "slow"
                        }, function() {
                            $(this).remove();
                        });
                }
            },
            /**
             * 提示信息jquery版本  用于统一的提示
             * @param {} title  显示title
             * @param {} msg    显示内容
             * @param {} okFun  点击确定 执行函数 （可选）
             * @param {} cancelFun 点击取消执行函数 （可选）
             * @return {}  返回当前页面
             */
            
            showAlert : function(title,msg,okFun,cancelFun){
                return $("<div>").attr("title",title)
                        .append($("<p/>").text(msg))
                        .appendTo(document.body )
                        .dialog({
                            modal: true,
                            buttons: {
                                "确定": function() {
                                    if(okFun){
                                        okFun();
                                    }
                                    $(this).dialog("close").remove();
                                },
                                "取消": function() {
                                    if(cancelFun){
                                        cancelFun();
                                    }
                                    $(this).dialog("close").remove();
                                }
                            }
                        });
            }
        }
    });