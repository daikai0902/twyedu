var init = [];

init.push(function () {
	//增加管理员
	$('#addGroupOwner').click(function(){
		for(var i=1; i<3;  i++){
			if(i == 2){
				$('#addGroupOwner').hide();
			}
			if($("#owner"+i).hasClass('hide')){
				$("#owner"+i).removeClass('hide');
				break;
			}
		}
	});
	
	var upload = $("#uploadify"),img=null;
	$(".uploadLogo").unbind().click(function(){
		img=$(this);
		upload.click();
	})
	
	//上传机构的图片
	upload.on("change", function(){
		var formData = new FormData($("#uploadForm")[0]);
	
		setTimeout(function(){
			var formData = new FormData($("#uploadForm")[0]);
			$.ajax({
			    url: '/application/upload' ,
			    type: 'POST',
			    data: formData,
			    async: false,
			    cache: false,
			    contentType: false,
			    processData: false,
			    success: function (json) {
			    	var _img = new Image();
			    	_img.onload = function(){
			    		img.attr("src",json.html);
			    		delete _img;
			    	};
			    	_img.src = json.html;
			    	upload.val("");
			    },
			    error: function (data) {
			        alert(data);
			    }
			});
		},100);
	});
	
	
	var uploadNewsImg = $("#uploadImg");
	$(".uploadImg").unbind().click(function(){
		imgDiv = $(this).parent();
		uploadNewsImg.click();
	})
	
	//上传机构的图片
	uploadNewsImg.on("change", function(){
		var array = uploadNewsImg.val().split('\\');
		picName = array[array.length-1];
		var formData = new FormData($("#uploadImgForm")[0]);
		setTimeout(function(){
			var formData = new FormData($("#uploadImgForm")[0]);
			$.ajax({
			    url: '/application/upload' ,
			    type: 'POST',
			    data: formData,
			    async: false,
			    cache: false,
			    contentType: false,
			    processData: false,
			    success: function (json) {
			    	var _img = new Image();
			    	imgDiv.find('span').remove();
			    	imgDiv.attr('data-src',json.html);
			    	imgDiv.append('<span class="label label-info" style="cursor: pointer;margin-left: 10px" data-src="'+json.html+'">'+picName+'<i class="fa fa-times"></i></span>')
			    	upload.val("");
			    },
			    error: function (data) {
			    	$.growl.error({ message: "图片上传失败！" });
			    }
			});
		},100);
	});
	
	//图片删除
	$('.imgDiv').on('click','span i',function(){
		var _this = $(this);
		_this.parents('.imgDiv').attr('data-src','');
		_this.parent().remove();
		return false;
	})
	
	//图片点击预览
	$('.imgDiv').on('click','span',function(){
		var _this = $(this);
		var src = _this.attr('data-src');
		var jsonUrl = {};
		var data = [];
		var item = {};
		item.src = src;
		data.push(item);
		jsonUrl.data = data;
		layer.photos({
			photos : jsonUrl
		});
	})
	
	
	$(".backloginout").on("click",function(){
		window.location.href = "/edu/back/logout";
	})
	
})

