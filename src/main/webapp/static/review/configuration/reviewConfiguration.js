/**
 * Created by yanzhenxing on 2015/12/5.
 */

/* 保存流程配置*/
function saveReviewDefinition(contextPath){
    var depts=getSelectedDept();
    var productTypes=getSelectedProductType();
    var processTypes=getSelectedProcessType();
    var needNoReview=getNeedNoReview();
    var multiApplyPermit=getMultiApplyPermit();
    var paymentEqualsCost=getPaymentEqualsCost();
    if (depts.length==0||productTypes.length==0||processTypes.length==0){
        var tips = "您尚未选择";
        if (depts.length==0){
            tips=tips+" [部门]";
        }
        if(productTypes.length==0){
            tips=tips+"[产品类型]";
        }
        if(processTypes.length==0){
            tips=tips+"[流程类型]";
        }
        top.$.jBox.info(tips, "信息", { width: 250, showType:"slide", icon: "info",draggable: "true" });
        top.$('.jbox-body .jbox-icon').css('top','55px');
        return ;
    }
    $.ajax({
        type: "POST",
        async:false,
        url: contextPath + "/sys/review/configuration/save",
        dataType:"json",
        data:{
            depts : depts,
            productTypes : productTypes,
            processTypes : processTypes,
            needNoReview : needNoReview,
            multiApplyPermit :multiApplyPermit,
            paymentEqualsCost :paymentEqualsCost
        },
        success : function(result) {
            var data = eval(result);
            //保存成功直接跳转到设计页面
            if(data.code==0){
                //如果是无需审批，直接跳转到详细信息页
                if(needNoReview==1){
                    window.open (contextPath + "/sys/review/configuration/detail?serialNumber="+data.serialNumber,"_self");
                }else{//正常审批跳转到流程设计页面
                    window.open (contextPath + "/sys/review/configuration/model/design?modelId="+data.modelId+"&processKey="+data.processKey+"&serialNumber="+data.serialNumber,"_self");
                }

            }else{
                var tips = data.message;
                top.$.jBox.info(tips, "信息", { width: 400, height:300, showType:"slide", icon: "info",draggable: "true" });
                top.$('.jbox-body .jbox-icon').css('top','55px');
            }
        }
    });
}

/*
 修改流程配置
 */
function modifyReviewDefinition(contextPath,processKey,serialNumber,modelId){
    if(canSave==0){
        return ;
    }
    var depts=getSelectedDept();
    var productTypes=getSelectedProductType();
    var processTypes=getSelectedProcessType();
    var needNoReview=getNeedNoReview();
    var multiApplyPermit=getMultiApplyPermit();
    var paymentEqualsCost=getPaymentEqualsCost();
    if (depts.length==0||productTypes.length==0||processTypes.length==0){
        var tips = "您尚未选择";
        if (depts.length==0){
            tips=tips+" [部门]";
        }
        if(productTypes.length==0){
            tips=tips+"[产品类型]";
        }
        if(processTypes.length==0){
            tips=tips+"[流程类型]";
        }
        top.$.jBox.info(tips, "信息", { width: 250, showType:"slide", icon: "info",draggable: "true" });
        top.$('.jbox-body .jbox-icon').css('top','55px');
        return ;
    }
    $.ajax({
        type: "POST",
        async:false,
        url: contextPath + "/sys/review/configuration/modify",
        dataType:"json",
        data:{
            processKey : processKey,
            serialNumber : serialNumber,
            modelId : modelId,
            depts : depts,
            productTypes : productTypes,
            processTypes : processTypes,
            needNoReview : needNoReview,
            multiApplyPermit :multiApplyPermit,
            paymentEqualsCost :paymentEqualsCost
        },
        success : function(result) {
            var data = eval(result);
            if(data.code==0){
                var tips = "保存成功";
                top.$.jBox.info(tips, "信息", { width: 250, showType:"slide", icon: "info",draggable: "true" });
                top.$('.jbox-body .jbox-icon').css('top','55px');
                //编辑失效
                disable();
                //保存按钮失效
                if ($("#btnSave").length>0){
                    $("#btnSave").attr("disabled","disabled");
                }
                $("#btnReturn").show();
                $("#btnSave").hide();
                $("#btnSaveAndReturn").hide();
            }else{
                var tips = data.message;
                top.$.jBox.info(tips, "信息", { width: 250, showType:"slide", icon: "info",draggable: "true" });
                top.$('.jbox-body .jbox-icon').css('top','55px');
            }
        }
    });
}

/*
 修改流程配置，并返回到主页面
 */
function modifyReviewDefinitionAndReturn(contextPath,processKey,serialNumber,modelId){
    if(canSave==0){
        window.open (contextPath + "/sys/review/configuration/index","_self");
    }
    var depts=getSelectedDept();
    var productTypes=getSelectedProductType();
    var processTypes=getSelectedProcessType();
    var needNoReview=getNeedNoReview();
    var multiApplyPermit=getMultiApplyPermit();
    var paymentEqualsCost=getPaymentEqualsCost();
    if (depts.length==0||productTypes.length==0||processTypes.length==0){
        var tips = "您尚未选择";
        if (depts.length==0){
            tips=tips+" [部门]";
        }
        if(productTypes.length==0){
            tips=tips+"[产品类型]";
        }
        if(processTypes.length==0){
            tips=tips+"[流程类型]";
        }
        top.$.jBox.info(tips, "信息", { width: 250, showType:"slide", icon: "info",draggable: "true" });
        top.$('.jbox-body .jbox-icon').css('top','55px');
        return ;
    }
    $.ajax({
        type: "POST",
        async:false,
        url: contextPath + "/sys/review/configuration/modify",
        dataType:"json",
        data:{
            processKey : processKey,
            serialNumber : serialNumber,
            modelId : modelId,
            depts : depts,
            productTypes : productTypes,
            processTypes : processTypes,
            needNoReview : needNoReview,
            multiApplyPermit :multiApplyPermit,
            paymentEqualsCost :paymentEqualsCost
        },
        success : function(result) {
            var data = eval(result);
            if(data.code==0){
                window.open (contextPath + "/sys/review/configuration/index","_self");
            }else{
                var tips = data.message;
                top.$.jBox.info(tips, "信息", { width: 250, showType:"slide", icon: "info",draggable: "true" });
                top.$('.jbox-body .jbox-icon').css('top','55px');
            }
        }
    });
}

/**
 * 获取无需审批的勾选值
 * */
function getNeedNoReview(){
    if($("#needNoReview").is(":checked")){
        return 1;
    }else{
        return 0;
    }
}

/**
 * 获取再次申请受限的勾选值
 * */
function getMultiApplyPermit(){
    if($("#multiApplyPermit").is(":checked")){
        return 1;
    }else{
        return 0;
    }
}

/*
 *  获取付款等于成本的值
 * */
function getPaymentEqualsCost(){
    if($("#paymentEqualsCost").is(":checked")){
        return 1;
    }else{
        return 0;
    }
}

/*
 获取选择的部门id列表
 */
function getSelectedDept(){
    var depts=[];
    var treeObj = $.fn.zTree.getZTreeObj("departTree");
    var nodes = treeObj.getCheckedNodes(true);
    for(var i=0;i<nodes.length;i++){
        var node=nodes[i];
        //var children=node.children;
        //if(children.length==0){
            depts.push(node.id);
        //}
    }
    return depts;
}

/* 获取产品类型列表  */
function getSelectedProductType(){
    var productTypes=[];
    var nodes=$("#productType .on >input");
    for(var i=0;i<nodes.length;i++){
        var node=nodes[i];
        productTypes.push(node.value);
    }
    return productTypes;
}

/* 获取流程类型列表  */
function getSelectedProcessType(){
    var processTypes=[];
    var nodes=$("#processType .on >input");
    for(var i=0;i<nodes.length;i++){
        var node=nodes[i];
        processTypes.push(node.value);
    }
    return processTypes;
}

/**
 * 页面编辑已启动，可以被保存的变量
 */

var canSave=0;


/*
 点击编辑流程定义按钮时，允许勾选节点
 */
function edit(){
    /*
     恢复部门树可选
     */
    var tree = $.fn.zTree.getZTreeObj("departTree");
    var nodes = tree.transformToArray(tree.getNodes());
    for(var i=0;i<nodes.length;i++){
        var node=nodes[i];
        tree.setChkDisabled(node, false);
    }

    /*
     恢复产品类型可选
     */
    $("#productType  input").removeAttr("disabled");

    /*
     恢复流程类型可选
     */
    $("#processType  input").removeAttr("disabled");

    /*
     * 恢复特殊权限
     * */
    $("#needNoReview").removeAttr("disabled");
    $("#multiApplyPermit").removeAttr("disabled");
    $("#paymentEqualsCost").removeAttr("disabled");

    /**
     * 可以被保存
     */
    canSave=1;

    /**
     * 恢复保存按钮
     */
    if ($("#btnSave").length>0){
        $("#btnSave").removeAttr("disabled");
    }
}

/**
 * 不可编辑状态
 */
function disable(){
    /*
     *部门树不可选
     */
    var tree = $.fn.zTree.getZTreeObj("departTree");
    var nodes = tree.transformToArray(tree.getNodes());
    for(var i=0;i<nodes.length;i++){
        var node=nodes[i];
        tree.setChkDisabled(node, true);
    }

    /*
     产品类型不可选
     */
    $("#productType  input").attr("disabled","disabled");

    /*
     流程类型不可选
     */
    $("#processType  input").attr("disabled","disabled");

    /*
     * 恢复特殊权限
     * */
    $("#needNoReview").attr("disabled","disabled");
    $("#multiApplyPermit").attr("disabled","disabled");
    $("#paymentEqualsCost").attr("disabled","disabled");

    /**
     * 可以被保存
     */
    canSave=0;

    /**
     * 恢复保存按钮
     */
    if ($("#btnSave").length>0){
        $("#btnSave").attr("disabled","disabled");
    }
}

/**
 * 获取可保存的值
 * @returns {number}
 */
function getCanSave(){
    return canSave;
}

/*
 编辑模型页面
 */
function editModel(ctx,needNoReviewModelId,modelId,processKey,serialNumber){
    if(modelId==needNoReviewModelId){
        var tips = "无需审批的流程模型不允许编辑";
        top.$.jBox.info(tips, "信息", { width: 250, showType:"slide", icon: "info",draggable: "true" });
        top.$('.jbox-body .jbox-icon').css('top','55px');
    }else{
        //如果已经启动配置编辑，需要先保存配置
        if(canSave==1){
            var depts=getSelectedDept();
            var productTypes=getSelectedProductType();
            var processTypes=getSelectedProcessType();
            var needNoReview=getNeedNoReview();
            var multiApplyPermit=getMultiApplyPermit();
            var paymentEqualsCost=getPaymentEqualsCost();
            if (depts.length==0||productTypes.length==0||processTypes.length==0){
                var tips = "您尚未选择";
                if (depts.length==0){
                    tips=tips+" [部门]";
                }
                if(productTypes.length==0){
                    tips=tips+"[产品类型]";
                }
                if(processTypes.length==0){
                    tips=tips+"[流程类型]";
                }
                top.$.jBox.info(tips, "信息", { width: 250, showType:"slide", icon: "info",draggable: "true" });
                top.$('.jbox-body .jbox-icon').css('top','55px');
                return ;
            }
            $.ajax({
                type: "POST",
                async:false,
                url: ctx + "/sys/review/configuration/modify",
                dataType:"json",
                data:{
                    processKey : processKey,
                    serialNumber : serialNumber,
                    modelId : modelId,
                    depts : depts,
                    productTypes : productTypes,
                    processTypes : processTypes,
                    needNoReview : needNoReview,
                    multiApplyPermit :multiApplyPermit,
                    paymentEqualsCost :paymentEqualsCost
                },
                success : function(result) {
                    var data = eval(result);
                    if(data.code==0){
                        window.open (ctx+"/sys/review/configuration/model/design?modelId="+modelId+"&processKey="+processKey+"&serialNumber="+serialNumber);
                    }else{
                        var tips = data.message;
                        top.$.jBox.info(tips, "信息", { width: 250, showType:"slide", icon: "info",draggable: "true" });
                        top.$('.jbox-body .jbox-icon').css('top','55px');
                    }
                }
            });
        }else{//如果不需要保存，直接跳转
            window.open (ctx+"/sys/review/configuration/model/design?modelId="+modelId+"&processKey="+processKey+"&serialNumber="+serialNumber);
        }
    }
}
