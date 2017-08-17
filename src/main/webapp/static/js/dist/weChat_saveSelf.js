/**
 * Created by ymx on 2017/1/11.
 * T2  微信端 图片上传保存相关操作
 */

/**
 * T2 散拼产品修改或者发布  上传宣传图片时，提交保存操作 调用(没有用到此方法)
 */
function uploadPosterPic(){
    //模拟操作
    $(".btn-primary[data-method='getCroppedCanvas']").trigger('click');
    //取得result
    // window.canvas_img;
    var base64_img;

    var $image = $('.img-container > img');
    if (window.canvas_img&&window.canvas_img[0]&&window.canvas_img[0].src){//本地原图上传，或者是服务器照片没做修改
        if($image.hasClass("from_local")){//说明从本地原图传的
            //图片大小尺寸判断
            base64_img=window.base64_from_local;
        }else{//服务器端的照片没做修改
            var img = new Image();
            img.src = window.canvas_img[0].src;

            var canvas = document.createElement("canvas");
            canvas.width = img.width;
            canvas.height = img.height;
            var ctx = canvas.getContext("2d");
            ctx.drawImage(img, 0, 0, img.width, img.height);
            var ext = img.src.substring(img.src.lastIndexOf(".")+1).toLowerCase();
            base64_img = canvas.toDataURL("image/"+ext);
        }
    }else{//这个地方就是裁剪图片了
        base64_img = window.canvas_img.toDataURL('image/png');
    }
    var _ctx=$("#ctx").val();
    //进行提交操作
    $.ajax({
        url: _ctx+"/activity/manager/uploadImgByBaseCode",
        type: 'POST',
        data: {image:base64_img},
        timeout : 10000, //超时时间设置，单位毫秒
        async: true,
        success: function (result) {
            console.log("那就上传成功了呗")

        },
        error: function (returndata) {
            console.log("那就上传失败了呗")

        }
    });
}


function getBase64Image(img) {

}

/**
 * T2 散拼产品修改或者发布公共部分调用
 * gaoyang
 * 2017-1-16
 */
function uploadPosterPicCommon() {
	// 模拟操作
	$(".btn-primary[data-method='getCroppedCanvas']").trigger('click');
	var base64_img;
	// 取得result
	// window.canvas_img;
	var $image = $('.img-container > img');
	if (window.canvas_img&&window.canvas_img[0]&&window.canvas_img[0].src){// 本地原图上传，或者是服务器照片没做修改
		if($image.parent().hasClass("from_local")){ // 说明从本地原图传的
			// 图片大小尺寸判断
			if($image.parent().attr("data-size")==='false') {
				// 提示图片大小超过2Mb建议剪裁或者更换图片
				// 如果return false 则取消整体提交
				$.jBox.tip("请上传规定的格式图片并且大小不超过2M.","warning");
				$("#submitAndSave").attr("class","ydbz_x");
				return "false";
			}
			if($image.parent().attr("data-px")==='false') {
				// 提示图片尺寸不对，影响预览效果。建议剪裁或者更换图片
				$.jBox.tip("照片比例与系统不符，请完成裁剪","warning");
				$("#submitAndSave").attr("class","ydbz_x");
				return "false";
			}
			base64_img=window.base64_from_local;
		}else{ // 服务器端的照片没做修改
			var img = new Image();
			img.src = window.canvas_img[0].src;
			var canvas = document.createElement("canvas");
			canvas.width = img.width;
			canvas.height = img.height;
			var ctx = canvas.getContext("2d");
			ctx.drawImage(img, 0, 0, img.width, img.height);
			var ext = img.src.substring(img.src.lastIndexOf(".")+1).toLowerCase();
			base64_img = canvas.toDataURL("image/"+ext);
		}
	}else{ // 这个地方就是裁剪图片了
		base64_img = window.canvas_img.toDataURL('image/png');
	}
	
	return base64_img;
}


function uploadImage() {
    var $image = $('.img-container > img'),
        $dataX = $('#dataX'),
        $dataY = $('#dataY'),
        $dataHeight = $('#dataHeight'),
        $dataWidth = $('#dataWidth'),
        $dataRotate = $('#dataRotate'),
        options = {
            aspectRatio: 75 / 41,
            preview: '.img-preview',
            autoCrop: true,
            crop: function (data) {
                $dataX.val(Math.round(data.x));
                $dataY.val(Math.round(data.y));
                $dataHeight.val(Math.round(data.height));
                $dataWidth.val(Math.round(data.width));
                $dataRotate.val(Math.round(data.rotate));
            }
        };
    $image.data('cropper').options.autoCrop=true;
    $image.on({
        'build.cropper': function (e) {
        },
        'built.cropper': function (e) {
        },
        'dragstart.cropper': function (e) {
        },
        'dragmove.cropper': function (e) {
        },
        'dragend.cropper': function (e) {
        },
        'zoomin.cropper': function (e) {
        },
        'zoomout.cropper': function (e) {
        }
    }).cropper(options);
}